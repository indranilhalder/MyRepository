/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplNotificationService;



/**
 * @author TCS
 */
public class MplNotificationServiceImpl implements MplNotificationService
{
	@Autowired
	private ModelService modelService;

	private static final Logger LOG = Logger.getLogger(MplNotificationServiceImpl.class);

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}



	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
		LOG.debug("Saved Into Model");
	}

	/*
	 *
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService#getPriceRowDetail(de.hybris.platform.europe1
	 * .model.PriceRowModel)
	 *
	 * @Javadoc Method to Retrieve Pricerow based on articleSKUID
	 *
	 * @param articleSKUID
	 *
	 * @return listOfPrice
	 */

	@Override
	public void saveToNotification(final OrderStatusNotificationModel osnm)
	{
		modelService.save(osnm);

	}

	/*
	 * @Javadoc Method to Retrieve Pricerow based on articleSKUIDs
	 *
	 * @param aticleSKUIDs
	 *
	 * @return mopMap
	 */



}
