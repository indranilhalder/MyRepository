package com.tisl.mpl.atssync.consumer;

import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;
import com.hybris.oms.export.api.ats.AvailabilityToSellQuantityDTO;
import com.tisl.mpl.marketplacecommerceservices.service.ExtStockService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.ordersplitting.impl.DefaultWarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.apache.log4j.Logger;


import java.util.Iterator;

/**
 * Created by i325571 on 08/02/17.
 */
public class AtsConsumer implements Runnable {

    private static final Logger LOG = Logger.getLogger(AtsConsumer.class);

    private KafkaStream<byte[], byte[]> m_stream;
    private int m_threadNumber;

    private AvailabilityToSellDTODeserializer availabilityToSellDTODeserializer;

    private ModelService modelService;

    private ExtStockService stockService;

    private DefaultWarehouseService defaultWarehouseService;

    private static final String WAREHOUSE_CODE = "mpl_warehouse";
    private static final String SOURCE = "OMS";

    public AtsConsumer(KafkaStream<byte[], byte[]> a_stream, int a_threadNumber, AvailabilityToSellDTODeserializer atsDto,ModelService modelService,
    ExtStockService stockService,DefaultWarehouseService defaultWarehouseService) {
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
        this.availabilityToSellDTODeserializer = atsDto;
        this.modelService = modelService;
        this.stockService = stockService;
        this.defaultWarehouseService = defaultWarehouseService;
    }

    @Override
    public void run() {

        if (!Registry.hasCurrentTenant())
        {
            //set the tenant if no active tenants
            Registry.activateMasterTenant();
        }

        final WarehouseModel warehouse = defaultWarehouseService.getWarehouseForCode(WAREHOUSE_CODE);
        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
        while (it.hasNext()) {
            final AvailabilityToSellDTO availableTosellDto =
                    availabilityToSellDTODeserializer.deserialize(null, it.next().message());
            if (availableTosellDto != null)
            {
                final Iterator iterator = availableTosellDto.getQuantities().iterator();
                while (iterator.hasNext())
                {
                    final AvailabilityToSellQuantityDTO quantityDTO = (AvailabilityToSellQuantityDTO) iterator.next();
                    try
                    {
                        if (quantityDTO.getLocationId() != null)
                        {
                            if (LOG.isDebugEnabled())
                            {
                                LOG.debug("ATS Received : " + quantityDTO.getSkuId()+ " Available Qty:" + quantityDTO.
                                        getQuantity() + " ON Thread : "+m_threadNumber);
                            }

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
                                    LOG.debug("Inside : Stock model  : Creating StocklevelModel for ID :" + quantityDTO.getSkuId()
                                            + " Available Qty:" + quantityDTO.getQuantity());
                                }

                            }
                            else if(null != stockLevelModel.getSequenceTS() && stockLevelModel.getSequenceTS() > availableTosellDto.getLatestChange())
                            {
                                if(LOG.isDebugEnabled())
                                {
                                    LOG.debug("Message Ignored due to old timestamp for SKU : "+quantityDTO.getSkuId() + " quantitity : "+quantityDTO.getQuantity());
                                }
                                continue;
                            }
                            stockLevelModel.setSequenceTS(availableTosellDto.getLatestChange());
                            stockLevelModel.setAvailable(quantityDTO.getQuantity());
                            modelService.save(stockLevelModel);
                        }

                    }
                    catch (Exception e)
                    {
                        LOG.error("Error occured during Processing AvailabilityToSellQuantityDTO :" + quantityDTO.getLocationId()
                                + "===" + quantityDTO.getQuantity() + "====" + quantityDTO.getSkuId() + "Timestamp" +availableTosellDto.getLatestChange()
                                + "Error Message" + e);

                    }
                }
            }

        }

        this.run();
    }
}
