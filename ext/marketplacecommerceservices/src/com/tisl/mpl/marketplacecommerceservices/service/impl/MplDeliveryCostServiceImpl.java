/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplDeliveryCostDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;


/**
 * @author TCS
 *
 */
public class MplDeliveryCostServiceImpl implements MplDeliveryCostService
{
	@Resource(name = "mplDeliveryCostDao")
	private MplDeliveryCostDao mplDeliveryCostDao;

	@Override
	public MplZoneDeliveryModeValueModel getDeliveryCost(final String deliveryCode, final String currencyIsoCode,
			final String sellerArticleSKU)
	{
		ServicesUtil.validateParameterNotNull(deliveryCode, "deliveryCode cannot be null");
		ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
		return getMplDeliveryCostDao().getDeliveryCost(deliveryCode, currencyIsoCode, sellerArticleSKU);
	}



	/**
	 * @return the mplDeliveryCostDao
	 */
	public MplDeliveryCostDao getMplDeliveryCostDao()
	{
		return mplDeliveryCostDao;
	}

	/**
	 * @param mplDeliveryCostDao
	 *           the mplDeliveryCostDao to set
	 */
	public void setMplDeliveryCostDao(final MplDeliveryCostDao mplDeliveryCostDao)
	{
		this.mplDeliveryCostDao = mplDeliveryCostDao;
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService#getDeliveryModesAndCost(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<MplZoneDeliveryModeValueModel> getDeliveryModesAndCost(final String currencyIsoCode, final String sellerArticleSku)
	{
		ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
		ServicesUtil.validateParameterNotNull(sellerArticleSku, "sellerArticleSku cannot be null");
		return getMplDeliveryCostDao().getDeliveryModesAndCost(currencyIsoCode, sellerArticleSku);
	}

}
