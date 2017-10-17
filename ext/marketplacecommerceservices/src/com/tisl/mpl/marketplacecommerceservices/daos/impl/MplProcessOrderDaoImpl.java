/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplProcessOrderDao;


/**
 * @author TCS
 *
 */
public class MplProcessOrderDaoImpl implements MplProcessOrderDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * 
	 * (non-Javadoc) //PaymentFix2017:- queryTAT added
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplProcessOrderDao#getPaymentPedingOrders()
	 */
	@Override
	public List<OrderModel> getPaymentPedingOrders(final String statusCode, final Date queryTAT)
	{
		try
		{
			//final String queryString = MarketplacecommerceservicesConstants.PAYMENTPENDINGORDERQUERY;
			final String queryString = MarketplacecommerceservicesConstants.PAYMENTPENDINGQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery orderListQuery = new FlexibleSearchQuery(queryString);
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.PAYMENTPENDINGSTATUS, statusCode);
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.PAYMENTPENDINGSKIPTIME, queryTAT);
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.ORDERTYPE,
					MarketplacecommerceservicesConstants.PARENT);

			//fetching PAYMENT PENDING order list from DB using flexible search query
			final List<OrderModel> orderList = getFlexibleSearchService().<OrderModel> search(orderListQuery).getResult();

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

	@Override
	public List<JuspayWebhookModel> getEventsForPendingOrders(final String reqId)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.PAYMENTPENDINGWEBHOOKUERY;

			//forming the flexible search query
			final FlexibleSearchQuery hookListQuery = new FlexibleSearchQuery(queryString);
			hookListQuery.addQueryParameter(MarketplacecommerceservicesConstants.WEBHOOKREQSTATUS, reqId);

			//fetching Webhook entries for Payment Pending orders
			final List<JuspayWebhookModel> hookList = flexibleSearchService.<JuspayWebhookModel> search(hookListQuery).getResult();

			return hookList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
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

	/**
	 * fetch orders in pending or refund initiated status
	 */
	@Override
	public List<OrderModel> getPendingOrRefundInitiatedOrders(final String statusCode1, final String statusCode2)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.PAYMENTPENDING;
			//			/*final String queryString = "SELECT {o.pk},{o.iswallet} FROM {order as o},{OrderStatus as os},{WalletEnum as w} WHERE  {o.status}={os.pk} and {o.iswallet}={w.pk} and ({os.code}='PAYMENT_PENDING' or {os.code}='REFUND_INITIATED') and {w.code}='mRupee'"
			//					.intern();*/
			//forming the flexible search query
			final FlexibleSearchQuery orderListQuery = new FlexibleSearchQuery(queryString);
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.STATUS1, statusCode1);
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.STATUS2, statusCode2);
			//fetching PAYMENT PENDING order list from DB using flexible search query
			final List<OrderModel> orderList = getFlexibleSearchService().<OrderModel> search(orderListQuery).getResult();
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
