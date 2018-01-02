/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

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

	@Override
	public MplZoneDeliveryModeValueModel getDeliveryCost(final String deliveryCode, final String currencyIsoCode,
			final String sellerArticleSKU, final String fulfillmentType)
	{
		ServicesUtil.validateParameterNotNull(deliveryCode, "deliveryCode cannot be null");
		ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
		return getMplDeliveryCostDao().getDeliveryCost(deliveryCode, currencyIsoCode, sellerArticleSKU, fulfillmentType);
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

	/**
	 *
	 * Added for CAR-266
	 *
	 * @param deliveryMode
	 * @param currencyIsoCode
	 * @param skuid
	 */
	@Override
	public List<MplZoneDeliveryModeValueModel> getDeliveryCost(final StringBuilder deliveryMode, final String currencyIsoCode,
			final String skuid)
	{
		final List<MplZoneDeliveryModeValueModel> dataList = getMplDeliveryCostDao()
				.getDeliveryModesAndCost(currencyIsoCode, skuid);
		final List<MplZoneDeliveryModeValueModel> finalList = new ArrayList<MplZoneDeliveryModeValueModel>();

		if (CollectionUtils.isNotEmpty(dataList))
		{
			for (final MplZoneDeliveryModeValueModel oModel : dataList)
			{
				if (deliveryMode.toString().contains(oModel.getDeliveryMode().getCode()))
				{
					finalList.add(oModel);
				}
			}
		}
		return finalList;

	}

}
