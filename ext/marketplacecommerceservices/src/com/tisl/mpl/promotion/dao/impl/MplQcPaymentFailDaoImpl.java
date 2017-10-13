/**
 *
 */
package com.tisl.mpl.promotion.dao.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.dao.MplQcPaymentFailDao;


/**
 * @author Techouts
 *
 */
public class MplQcPaymentFailDaoImpl implements MplQcPaymentFailDao
{

	private static final Logger LOG = Logger.getLogger(MplBinErrorDaoImpl.class);
	public static final String QC_PAYMENT_QUERY = "select {pk} from {WalletApportionReturnInfo as w} where {w.status}=?status and  {w.type}=?cancel";
	public static final String QC_PAYMENT_QUERY1 = "select {pk} from {WalletApportionReturnInfo as w} where {w.status}=?status and  {w.type}=?cancel or {w.type}=?return";

	//public static final String QC_PAYMENT_QUERY = "select {pk} from {WalletApportionReturnInfo as w} where {w.status}=?status and  {w.type}=?cancel or {w.type}=?return";
	public static final String QC_ORDER_QUERY = "select {pk} from {Order as o} where {w.code}=?code";

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<WalletApportionReturnInfoModel> getPendingQcPayments()
	{
		LOG.debug("Fetching Bin Error Details");
		final String queryString = QC_PAYMENT_QUERY;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.WALLETAPPORTIONINFOSTATUS, "PENDING");
		query.addQueryParameter("cancel", "CANCEl");
		//query.addQueryParameter("return", "CANCEl");
		try {
		return flexibleSearchService.<WalletApportionReturnInfoModel> search(query).getResult();
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			LOG.debug("Fetching Bin Error Details");
			final String queryString1 = QC_PAYMENT_QUERY1;

			final FlexibleSearchQuery queryy= new FlexibleSearchQuery(queryString1);
			queryy.addQueryParameter(MarketplacecommerceservicesConstants.WALLETAPPORTIONINFOSTATUS, "PENDING");
			queryy.addQueryParameter("cancel", "CANCEl");
			queryy.addQueryParameter("return", "RETURN");
			//query.addQueryParameter("return", "CANCEl");
			return flexibleSearchService.<WalletApportionReturnInfoModel> search(queryy).getResult();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flexibleSearchService.<WalletApportionReturnInfoModel> search(query).getResult();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.promotion.dao.MplQcPaymentFailDao#getOrder(java.lang.String)
	 */
	@Override
	public OrderModel getOrder(final String orderCode)
	{
		LOG.debug("Fetching order Details");
		final String queryString = QC_ORDER_QUERY;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", orderCode);
		if (!flexibleSearchService.<OrderModel> search(query).getResult().isEmpty())
		{
			return flexibleSearchService.<OrderModel> search(query).getResult().get(0);
		}
		else
		{
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.promotion.dao.MplQcPaymentFailDao#getRmsVerificationFailedOrders(java.lang.String)
	 */
	@Override
	public List<OrderModel> getRmsVerificationFailedOrders(String orderStatus)
	{
		try
		{
			LOG.debug("Fetching the Orders with status " +orderStatus);
			final String queryString = MarketplacecommerceservicesConstants.RMSVERIFICATIONFAILEDQUERY;
			final FlexibleSearchQuery orderListQuery = new FlexibleSearchQuery(queryString);
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.RMSVERIFICATIONFAILEDSTATUS, orderStatus);
			final List<OrderModel> orderList = flexibleSearchService.<OrderModel> search(orderListQuery).getResult();

			return orderList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

}
