/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.PromotionPriceUpdaterDao;


/**
 * @author 1022570
 *
 */
@Component(value = "promotionPriceUpdaterDao")
public class PromotionPriceUpdaterDaoImpl implements PromotionPriceUpdaterDao
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionPriceUpdaterDaoImpl.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.PromotionPriceUpdaterDao#getRequiredPromotionList()
	 */
	@Override
	public List<ProductPromotionModel> getRequiredPromotionList(final Date mplConfigDate)
	{
		LOG.debug("Fetching promotion Details");
		List<ProductPromotionModel> PromotionResult = new ArrayList<ProductPromotionModel>();
		final String queryString = "SELECT {" + ProductPromotionModel.PK + "} " + MarketplacecommerceservicesConstants.QUERYFROM
				+ ProductPromotionModel._TYPECODE + " AS pr} " + " WHERE" + "({pr." + ProductPromotionModel.MODIFIEDTIME
				+ "} >= ?earlierDate  " + " OR ({pr." + ProductPromotionModel.STARTDATE + "} >= ?earlierDate AND {pr."
				+ ProductPromotionModel.STARTDATE + "} <= sysdate ) " + ") AND {pr: " + ProductPromotionModel.IMMUTABLEKEYHASH
				+ "} IS NULL " + " ORDER BY {pr:" + ProductPromotionModel.PRIORITY + "} ASC";

		LOG.debug("QUERY>>>>>>" + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		query.addQueryParameter("earlierDate", mplConfigDate);
		//return
		// YTODO Auto-generated method stub

		PromotionResult = getFlexibleSearchService().<ProductPromotionModel> search(query).getResult();
		return PromotionResult;

	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

}