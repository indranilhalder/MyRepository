package com.tisl.mpl.atssync.consumer;

import de.hybris.platform.core.Registry;
import de.hybris.platform.integration.commons.services.OndemandGlobalPreferenceService;
import de.hybris.platform.ordersplitting.impl.DefaultWarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.export.api.ats.ATSExportFacade;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;
import com.hybris.oms.export.api.ats.AvailabilityToSellQuantityDTO;
import com.tisl.mpl.constants.MarketplaceatssyncConstants;
import com.tisl.mpl.marketplacecommerceservices.service.ExtStockService;


/**
 *
 * @author TECHOUTS
 *
 */
public class CustomAtsSyncConsumer
{
	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "stockService")
	private ExtStockService stockService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private OndemandGlobalPreferenceService ondemandGlobalPreferenceService;
	private ATSExportFacade atsExportFacade;


	@Autowired
	private DefaultWarehouseService defaultWarehouseService;
	private static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
	private static final String GROUP_ID = "group.id";
	private static final String ENABLE_AUTO_COMMIT = "enable.auto.commit";
	private static final String AUTO_COMMIT_INTERVAL_MS = "auto.commit.interval.ms";
	private static final String SESSION_TIMEOUT_MS = "session.timeout.ms";
	private static final String AUTO_OFFSET_RESTORE = "auto.offset.reset";
	private static final String KEY_DESERIALIZER = "key.deserializer";
	private static final String VALUE_DESERIALIZER = "value.deserializer";
	private static final String SOURCE = "OMS";
	private static final String WAREHOUSE_CODE = "mpl_warehouse";
	private final Log LOG = LogFactory.getLog(getClass());

	private static Properties createConsumerConfig(final String brokers, final String groupId, final String autocommit,
			final String autocommitInterval, final String sessionTimeout, final String autooffset, final String keyDeserializer,
			final String valueDeserializer)
	{
		final Properties props = new Properties();
		props.put(BOOTSTRAP_SERVERS, brokers);
		props.put(GROUP_ID, groupId);
		props.put(ENABLE_AUTO_COMMIT, autocommit);
		props.put(AUTO_COMMIT_INTERVAL_MS, autocommitInterval);
		props.put(SESSION_TIMEOUT_MS, sessionTimeout);
		props.put(AUTO_OFFSET_RESTORE, autooffset);
		props.put(KEY_DESERIALIZER, keyDeserializer);
		props.put(VALUE_DESERIALIZER, valueDeserializer);
		return props;

	}

	//this method will be called during bean initialization time
	public void consumer()
	{
		//method call to pool ats 
		call();
	}

	public void call()
	{
		LOG.info("consumer Started");
		final String brokers = configurationService.getConfiguration().getString(MarketplaceatssyncConstants.ATS_KAFKA_BROKERS);
		final String topic = configurationService.getConfiguration().getString(MarketplaceatssyncConstants.ATS_KAFKA_TOPIC);
		final String groupId = configurationService.getConfiguration().getString(MarketplaceatssyncConstants.ATS_KAFKA_GROUP_ID);
		final String autocommit = configurationService.getConfiguration().getString(
				MarketplaceatssyncConstants.ATS_KAFKA_ENABLE_AUTO_COMMIT);
		final String autocommitInterval = configurationService.getConfiguration().getString(
				MarketplaceatssyncConstants.ATS_KAFKA_AUTO_COMMIT_INTERVAL_MS);
		final String sessionTimeout = configurationService.getConfiguration().getString(
				MarketplaceatssyncConstants.ATS_KAFKA_SESSION_TIMEOUT_MS);
		final String autooffset = configurationService.getConfiguration().getString(
				MarketplaceatssyncConstants.ATS_KAFKA_AUTO_OFFSET_RESTORE);
		final String keyDeserializer = configurationService.getConfiguration().getString(
				MarketplaceatssyncConstants.ATS_KAFKA_KEY_DESERIALIZER);
		final String valueDeserializer = configurationService.getConfiguration().getString(
				MarketplaceatssyncConstants.ATS_KAFKA_VALUE_DESERIALIZER);
		final int pollingCount = configurationService.getConfiguration()
				.getInt(MarketplaceatssyncConstants.ATS_KAFKA_POLLING_COUNT);

		LOG.info("Broker Name :" + brokers + "===== Group ID:" + groupId + "===== Topic:" + topic);
		final Properties prop = createConsumerConfig(brokers, groupId, autocommit, autocommitInterval, sessionTimeout, autooffset,
				keyDeserializer, valueDeserializer);
		final KafkaConsumer<String, AvailabilityToSellDTO> consumer = new KafkaConsumer<>(prop);
		consumer.subscribe(Arrays.asList(topic.trim()));
		LOG.info("Kafka Properties" + prop);
		LOG.info("Polling Starting");
		final ExecutorService executor = Executors.newCachedThreadPool();
		executor.submit(() -> {
			while (true)
			{
				try
				{
					//set the polling count to consumer
				final ConsumerRecords<String, AvailabilityToSellDTO> records = consumer.poll(pollingCount);
				for (final ConsumerRecord<String, AvailabilityToSellDTO> record : records)
				{
					final AvailabilityToSellDTO availableTosellDto = record.value();
					if (availableTosellDto != null)
					{
						final Iterator iterator = availableTosellDto.getQuantities().iterator();
						while (null != iterator && iterator.hasNext())
						{
							final AvailabilityToSellQuantityDTO quantityDTO = (AvailabilityToSellQuantityDTO) iterator.next();
							try
							{
								if (quantityDTO.getLocationId() != null)
								{
									if (LOG.isDebugEnabled())
									{
										LOG.debug("ATS Received : " + quantityDTO.getSkuId()+ " Available Qty:" + quantityDTO.getQuantity());
									}
									if (!Registry.hasCurrentTenant())
									{
										//set the tenant if no active tenants
										Registry.activateMasterTenant();
									}
									final WarehouseModel warehouse = defaultWarehouseService.getWarehouseForCode(WAREHOUSE_CODE);

									StockLevelModel stockLevelModel = stockService.getStockLevel(quantityDTO.getSkuId(), warehouse);
									if (stockLevelModel == null)
									{
										stockLevelModel = modelService.create(StockLevelModel.class);
										stockLevelModel.setProductCode(quantityDTO.getSkuId());
										stockLevelModel.setSellerArticleSKU(quantityDTO.getSkuId());
										stockLevelModel.setWarehouse(warehouse);
										stockLevelModel.setReserved(0);
										stockLevelModel.setOverSelling(0);
										stockLevelModel.setSource(SOURCE);
										if (LOG.isDebugEnabled())
										{
											LOG.info("Inside : Stock model  : Creating StocklevelModel for ID :" + quantityDTO.getSkuId()
													+ " Available Qty:" + quantityDTO.getQuantity());
										}

									}
									stockLevelModel.setAvailable(quantityDTO.getQuantity());
									modelService.save(stockLevelModel);
								}

                        //set the latest updated time to give delete request 
								//getAtsExportFacade().unmarkSkuForExport(availableTosellDto.getLatestChange());
								//set the latest updated time to give next ATS-POLL request
								//getOndemandGlobalPreferenceService().setLastOmsAtsPollingTime(availableTosellDto.getLatestChange());
							}
							catch (Exception e)
							{
								LOG.error("Error occured during Processing AvailabilityToSellQuantityDTO :" + quantityDTO.getLocationId()
										+ "===" + quantityDTO.getQuantity() + "====" + quantityDTO.getSkuId() + "Error Message"
										+ e.getMessage());

							}
						}
					}
				}
			}
			catch (final Exception e)
			{
				LOG.error("Error Occured during Consuming ATS for " + e.getMessage());
			}

		}//while
	}) ;
	}

	/**
	 * @return the ondemandGlobalPreferenceService
	 */
	public OndemandGlobalPreferenceService getOndemandGlobalPreferenceService()
	{
		return ondemandGlobalPreferenceService;
	}

	/**
	 * @param ondemandGlobalPreferenceService
	 *           the ondemandGlobalPreferenceService to set
	 */
	public void setOndemandGlobalPreferenceService(final OndemandGlobalPreferenceService ondemandGlobalPreferenceService)
	{
		this.ondemandGlobalPreferenceService = ondemandGlobalPreferenceService;
	}

	/**
	 * @return the atsExportFacade
	 */
	public ATSExportFacade getAtsExportFacade()
	{
		return atsExportFacade;
	}

	/**
	 * @param atsExportFacade
	 *           the atsExportFacade to set
	 */
	public void setAtsExportFacade(final ATSExportFacade atsExportFacade)
	{
		this.atsExportFacade = atsExportFacade;
	}
}
