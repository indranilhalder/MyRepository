/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

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
import com.tisl.mpl.model.BuyAPercentageDiscountModel;


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
	public List<BuyAPercentageDiscountModel> getRequiredPromotionList(final Date mplConfigDate)
	{

		LOG.debug("Fetching promotion Details");
		List<BuyAPercentageDiscountModel> PromotionResult = new ArrayList<BuyAPercentageDiscountModel>();

		final String queryString = //
		"SELECT {" + BuyAPercentageDiscountModel.PK + "} " + MarketplacecommerceservicesConstants.QUERYFROM
				+ BuyAPercentageDiscountModel._TYPECODE + " AS pr} " + " WHERE SYSDATE BETWEEN {pr:"
				+ BuyAPercentageDiscountModel.STARTDATE + "} AND {pr:" + BuyAPercentageDiscountModel.ENDDATE + "}" + " AND {pr:"
				+ BuyAPercentageDiscountModel.ENABLED + "} = '1' AND " + "({pr." + BuyAPercentageDiscountModel.STARTDATE
				+ "} >= ?earlierDate AND " + "{pr." + BuyAPercentageDiscountModel.STARTDATE + "} <= SYSDATE)   ORDER BY {pr:"
				+ BuyAPercentageDiscountModel.PRIORITY + "} ASC";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		query.addQueryParameter("earlierDate", mplConfigDate);
		//return
		// YTODO Auto-generated method stub

		PromotionResult = getFlexibleSearchService().<BuyAPercentageDiscountModel> search(query).getResult();
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