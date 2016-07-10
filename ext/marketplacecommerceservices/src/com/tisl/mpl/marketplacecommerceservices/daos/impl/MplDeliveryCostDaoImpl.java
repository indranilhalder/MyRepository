/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplDeliveryCostDao;


/**
 * @author TCS
 *
 */
public class MplDeliveryCostDaoImpl implements MplDeliveryCostDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(MplDeliveryCostDaoImpl.class);

	@Override
	public MplZoneDeliveryModeValueModel getDeliveryCost(final String deliveryCode, final String currencyIsoCode,
			final String sellerArticleSku)
	{

		MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = null;
		if (StringUtils.isNotEmpty(deliveryCode) && StringUtils.isNotEmpty(currencyIsoCode)
				&& StringUtils.isNotEmpty(sellerArticleSku))
		{
			PK deliveryPk = null;

			final StringBuilder deliveryCodeQuery = new StringBuilder(20);
			deliveryCodeQuery.append("SELECT DISTINCT {del:").append(DeliveryModeModel.PK).append("} ");
			deliveryCodeQuery.append(" FROM {").append(DeliveryModeModel._TYPECODE).append(" AS del ");
			deliveryCodeQuery.append("} WHERE upper({del:").append(DeliveryModeModel.CODE).append("}) = ?deliveryCode");


			final Map<String, Object> params1 = new HashMap<String, Object>(2);
			params1.put("deliveryCode", deliveryCode.toUpperCase());

			final SearchResult<DeliveryModeModel> searchRes = getFlexibleSearchService().search(deliveryCodeQuery.toString(),
					params1);
			if (searchRes != null && searchRes.getCount() == 1)
			{
				deliveryPk = searchRes.getResult().get(0).getPk();
			}
			else
			{
				LOG.debug("Data setup issue in MplZoneDeliveryModel : Delivery mode pk not found");
			}
			if (deliveryPk != null)
			{
				final StringBuilder mplDeliveryModelQuery = new StringBuilder(40);
				mplDeliveryModelQuery.append("SELECT DISTINCT {val:").append(MplZoneDeliveryModeValueModel.PK).append("} ");
				mplDeliveryModelQuery.append(" FROM {").append(MplZoneDeliveryModeValueModel._TYPECODE).append(" AS val ");
				mplDeliveryModelQuery.append(" JOIN ").append(DeliveryModeModel._TYPECODE).append(" AS dm ON {val:")
						.append(MplZoneDeliveryModeValueModel.DELIVERYMODE);
				mplDeliveryModelQuery.append(" }={dm:").append(DeliveryModeModel.PK).append("} ");
				mplDeliveryModelQuery.append(" JOIN ").append(CurrencyModel._TYPECODE).append("  AS CUR  ON {val:")
						.append(MplZoneDeliveryModeValueModel.CURRENCY);
				mplDeliveryModelQuery.append("}={CUR:").append(CurrencyModel.PK).append("} ");
				mplDeliveryModelQuery.append("} WHERE {val:").append(MplZoneDeliveryModeValueModel.DELIVERYMODE)
						.append("} = ?deliveryCode");
				mplDeliveryModelQuery.append("  AND upper({CUR:").append(CurrencyModel.ISOCODE).append("}) = ?currencyIsoCode ");
				mplDeliveryModelQuery.append(" AND {val:").append(MplZoneDeliveryModeValueModel.SELLERARTICLESKU)
						.append("} = ?sellerArticleSku");


				final Map<String, Object> params = new HashMap<String, Object>(2);
				params.put("deliveryCode", deliveryPk.toString());
				params.put("currencyIsoCode", currencyIsoCode.toUpperCase());
				params.put("sellerArticleSku", sellerArticleSku);

				final SearchResult<MplZoneDeliveryModeValueModel> searchRes1 = getFlexibleSearchService().search(
						mplDeliveryModelQuery.toString(), params);
				if (searchRes1 != null && searchRes1.getCount() > 0)
				{
					mplZoneDeliveryModeValueModel = searchRes1.getResult().get(0);
				}
			}

		}
		else
		{
			LOG.debug("MplDeliveryCostDaoImpl : getDeliveryCost : deliveryCode,currencyIsoCode,sellerArticleSku is null or empty ");
		}
		return mplZoneDeliveryModeValueModel;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplDeliveryCostDao#getDeliveryModesAndCost(java.lang.String)
	 */
	@Override
	public List<MplZoneDeliveryModeValueModel> getDeliveryModesAndCost(final String currencyIsoCode, final String sellerArticleSku)
	{
		ServicesUtil.validateParameterNotNull(sellerArticleSku, "sellerArticleSku cannot be null");

		List<MplZoneDeliveryModeValueModel> mplDeliveryModelList = null;

		final StringBuilder mplDeliveryModequery = new StringBuilder(150);
		mplDeliveryModequery.append("SELECT DISTINCT {val:" + MplZoneDeliveryModeValueModel.PK + "} ");
		mplDeliveryModequery.append(" FROM {" + MplZoneDeliveryModeValueModel._TYPECODE + " AS val ");
		mplDeliveryModequery.append(" JOIN " + CurrencyModel._TYPECODE + "  AS CUR  ON {val:"
				+ MplZoneDeliveryModeValueModel.CURRENCY + "}={CUR:" + CurrencyModel.PK + "} ");

		mplDeliveryModequery.append("} WHERE {val:" + MplZoneDeliveryModeValueModel.SELLERARTICLESKU + "} = ?sellerArticleSku");
		mplDeliveryModequery.append(" AND upper({CUR:" + CurrencyModel.ISOCODE + "}) = ?currencyIsoCode ");

		final Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("sellerArticleSku", sellerArticleSku);
		params.put("currencyIsoCode", currencyIsoCode.toUpperCase());

		final SearchResult<MplZoneDeliveryModeValueModel> searchRes = getFlexibleSearchService().search(
				mplDeliveryModequery.toString(), params);
		if (searchRes != null && searchRes.getCount() > 0)
		{
			mplDeliveryModelList = searchRes.getResult();
		}
		return mplDeliveryModelList;
	}
}
