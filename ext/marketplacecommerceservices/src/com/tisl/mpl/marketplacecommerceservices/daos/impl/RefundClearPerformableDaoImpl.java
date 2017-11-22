/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.RefundClearPerformableDao;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author 1079689
 *
 */
public class RefundClearPerformableDaoImpl implements RefundClearPerformableDao
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(RefundClearPerformableDaoImpl.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.RefundClearPerformableDao#getCronDetails(java.lang.String)
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		final String queryString = //
		"SELECT {cm:" + MplConfigurationModel.PK
				+ "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + MplConfigurationModel._TYPECODE + " AS cm } where" + "{cm."
				+ MplConfigurationModel.MPLCONFIGCODE + "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, code);
		return getFlexibleSearchService().<MplConfigurationModel> searchUnique(query);
	}




	@Override
	public List<OrderModel> getRefundClearOrders(final Date date)
	{
		//final String queryString = MarketplacecommerceservicesConstants.PAYMENTPENDINGORDERQUERY;
		final String queryString = MarketplacecommerceservicesConstants.REFUNDCLEARORDERQUERY;

		//forming the flexible search query
		final FlexibleSearchQuery orderListQuery = new FlexibleSearchQuery(queryString);
		orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.ORDERSTATUSONE,
				OrderStatus.REFUND_INITIATED.getCode());
		orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.ORDERSTATUSTWO,
				OrderStatus.REFUND_IN_PROGRESS.getCode());
		orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.PAYMENTPENDINGSKIPTIME, date);
		orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.ORDERTYPE,
				MarketplacecommerceservicesConstants.SUBORDER);

		//fetching PAYMENT PENDING order list from DB using flexible search query
		final List<OrderModel> orderList = getFlexibleSearchService().<OrderModel> search(orderListQuery).getResult();

		return orderList;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}






}
