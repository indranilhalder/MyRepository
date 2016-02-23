package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.ReturnReasonModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.AWBResponseData;
import com.tisl.mpl.facades.data.StatusRecordData;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.service.MplAwbStatusService;
import com.tisl.mpl.xml.pojo.AWBStatusResponse;
import com.tisl.mpl.xml.pojo.AWBStatusResponse.AWBResponseInfo;
import com.tisl.mpl.xml.pojo.AWBStatusResponse.AWBResponseInfo.StatusRecords;


/**
 *
 * @author TCS
 *
 */
@Component
public class DefaultMplOrderService implements MplOrderService
{
	@Autowired
	private MplOrderDao mplOrderDao;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Autowired
	private MplAwbStatusService mplAwbStatusService;
	@Autowired
	private MplProductDao productDao;
	private final static Logger LOG = Logger.getLogger(DefaultMplOrderService.class);

	/**
	 * @return the mplOrderDao
	 */
	public MplOrderDao getMplOrderDao()
	{
		return mplOrderDao;
	}

	/**
	 * @param mplOrderDao
	 *           the mplOrderDao to set
	 */
	public void setMplOrderDao(final MplOrderDao mplOrderDao)
	{
		this.mplOrderDao = mplOrderDao;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the mplAwbStatusService
	 */
	public MplAwbStatusService getMplAwbStatusService()
	{
		return mplAwbStatusService;
	}

	/**
	 * @param mplAwbStatusService
	 *           the mplAwbStatusService to set
	 */
	public void setMplAwbStatusService(final MplAwbStatusService mplAwbStatusService)
	{
		this.mplAwbStatusService = mplAwbStatusService;
	}




	/**
	 *
	 */
	@Override
	public List<ReturnReasonData> getReturnReasonForOrderItem()
	{
		try
		{
			final List<ReturnReasonData> reasonDataList = new ArrayList<ReturnReasonData>();
			final List<ReturnReasonModel> reasonModelList = mplOrderDao.getReturnReasonForOrderItem();
			//if (null != reasonModelList && reasonModelList.size() > 0)
			if (CollectionUtils.isNotEmpty(reasonModelList))
			{
				for (final ReturnReasonModel newModel : reasonModelList)
				{
					final ReturnReasonData oReasonData = new ReturnReasonData();

					oReasonData.setReasonDescription(newModel.getReasonDescription());
					oReasonData.setCode(newModel.getReasonCode());

					reasonDataList.add(oReasonData);
				}
			}
			return reasonDataList;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 *
	 */
	@Override
	public SearchPageData<OrderModel> getSubOrderList(final CustomerModel customerModel, final BaseStoreModel store,
			final OrderStatus[] status, final PageableData pageableData)
	{
		try
		{
			ServicesUtil.validateParameterNotNull(customerModel, MarketplacecommerceservicesConstants.CUSTOMER_MODEL_CANNOT_BE_NULL);
			ServicesUtil.validateParameterNotNull(store, MarketplacecommerceservicesConstants.STORE_MUST_NOT_BE_NULL);
			ServicesUtil.validateParameterNotNull(pageableData, MarketplacecommerceservicesConstants.PAGEABLEDATA_MUST_NOT_BE_NULL);
			return mplOrderDao.findSubOrdersByCustomerAndStore(customerModel, store, status, pageableData);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 *
	 */
	@Override
	public SearchPageData<OrderModel> getParentOrderList(final CustomerModel customerModel, final BaseStoreModel store,
			final OrderStatus[] status, final PageableData pageableData)
	{
		try
		{
			ServicesUtil.validateParameterNotNull(customerModel, MarketplacecommerceservicesConstants.CUSTOMER_MODEL_CANNOT_BE_NULL);
			ServicesUtil.validateParameterNotNull(store, MarketplacecommerceservicesConstants.STORE_MUST_NOT_BE_NULL);
			ServicesUtil.validateParameterNotNull(pageableData, MarketplacecommerceservicesConstants.PAGEABLEDATA_MUST_NOT_BE_NULL);
			return mplOrderDao.findParentOrdersByCustomerAndStore(customerModel, store, status, pageableData);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Desc : Returns the order history with duration as filter TISEE-1855
	 * @param paramCustomerModel
	 * @param paramBaseStoreModel
	 * @param paramPageableData
	 * @param fromDate
	 * @return SearchPageData
	 */
	@Override
	public SearchPageData<OrderModel> getPagedFilteredParentOrderHistory(final CustomerModel paramCustomerModel,
			final BaseStoreModel paramBaseStoreModel, final PageableData paramPageableData, final Date fromDate)
	{
		try
		{
			ServicesUtil.validateParameterNotNull(paramCustomerModel,
					MarketplacecommerceservicesConstants.CUSTOMER_MODEL_CANNOT_BE_NULL);
			ServicesUtil.validateParameterNotNull(paramBaseStoreModel, MarketplacecommerceservicesConstants.STORE_MUST_NOT_BE_NULL);
			ServicesUtil.validateParameterNotNull(paramPageableData,
					MarketplacecommerceservicesConstants.PAGEABLEDATA_MUST_NOT_BE_NULL);
			ServicesUtil.validateParameterNotNull(fromDate, MarketplacecommerceservicesConstants.FROM_DATE_MUST_NOT_BE_NULL);

			return mplOrderDao.getPagedFilteredParentOrderHistory(paramCustomerModel, paramBaseStoreModel, paramPageableData,
					fromDate);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}


	@Override
	public ConsignmentModel fetchConsignment(final String consignmentCode)
	{
		try
		{
			return mplOrderDao.fetchConsignment(consignmentCode);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	@Override
	public List<CancellationReasonModel> getCancellationReason()
	{
		try
		{
			return mplOrderDao.fetchCancellationReason();
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplAwbStatusService#prepAwbStatus(com.tisl.mpl.xml.pojo.AWBStatusResponse)
	 */
	@Override
	public AWBResponseData prepAwbStatus(final String awbNumber, final String tplCode)
	{
		final AWBResponseData awbResponseFromOms = new AWBResponseData();
		AWBStatusResponse awbResponseFrom = new AWBStatusResponse();
		List<StatusRecordData> statusRecords = null;
		String enable = MarketplacecommerceservicesConstants.EMPTY;
		SimpleDateFormat smdfDate = null;
		SimpleDateFormat smdfTime = null;
		try
		{
			smdfDate = new SimpleDateFormat(MarketplacecclientservicesConstants.DATE_FORMAT_AWB);
			smdfTime = new SimpleDateFormat(MarketplacecclientservicesConstants.TIME_FORMAT_AWB);
			//Consuming AWBStatus URL
			enable = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.PROPERTY_KEY_OMS_AWB_ACTIVE);
			if (enable.equalsIgnoreCase("Y"))
			{
				awbResponseFrom = mplAwbStatusService.prepAwbNumbertoOMS(awbNumber, tplCode);

				if (awbResponseFrom.getAWBResponseInfo().size() > 0)
				{

					for (final AWBResponseInfo status : awbResponseFrom.getAWBResponseInfo())
					{
						awbResponseFromOms.setResponseCode(status.getResponseCode());
						awbResponseFromOms.setShipmentStatus(status.getShipmentStatus());
						statusRecords = new ArrayList<>();
						for (final StatusRecords statusRecord : status.getStatusRecords())
						{
							final StatusRecordData statusRecordData = new StatusRecordData();
							statusRecordData.setDate(statusRecord.getDate());
							statusRecordData.setTime(statusRecord.getTime());
							statusRecordData.setStatusDescription(statusRecord.getStatusDescription());
							statusRecordData.setLocation(statusRecord.getLocation());
							statusRecords.add(statusRecordData);
						}
						awbResponseFromOms.setStatusRecords(statusRecords);
					}
				}
			}
			else
			{
				awbResponseFromOms.setResponseCode("COR-10897");
				awbResponseFromOms.setShipmentStatus("Near Central Hub");
				statusRecords = new ArrayList<>();
				final StatusRecordData statusRecord = new StatusRecordData();
				statusRecord.setDate(new Date().toString());
				statusRecord.setLocation("Kolkata");
				statusRecord.setStatusDescription("Near Central Hub");
				statusRecord.setTime(new Date().toString());

				final StatusRecordData statusRecord2 = new StatusRecordData();
				final Date dateN = new Date();


				statusRecord2.setDate(smdfDate.format(dateN));
				statusRecord2.setLocation("Chennai");
				statusRecord2.setStatusDescription("Picked from Warehouse");
				statusRecord2.setTime(smdfTime.format(dateN));

				statusRecords.add(statusRecord2);
				statusRecords.add(statusRecord);
				awbResponseFromOms.setStatusRecords(statusRecords);
			}
		}
		catch (final Exception e)
		{
			LOG.debug("-------------------AWB Service error------------");
			return awbResponseFromOms;
		}
		return awbResponseFromOms;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplOrderService#findProductsByCode(java.lang.String)
	 */
	@Override
	public ProductModel findProductsByCode(final String productCode)
	{
		validateParameterNotNull(productCode, "Parameter code must not be null");
		final List<ProductModel> products = productDao.findProductsByCodeNew(productCode);

		validateIfSingleResult(products, format("Product with code '%s' not found!", productCode),
				format("Product code '%s' is not unique, %d products found!", productCode, Integer.valueOf(products.size())));

		return products.get(0);
	}
}
