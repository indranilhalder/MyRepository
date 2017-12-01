/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
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
		try
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
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}




	@Override
	public List<OrderModel> getRefundClearOrders(final Date date, final Date startDate)
	{
		try
		{

			final String queryString = MarketplacecommerceservicesConstants.REFUNDCLEARORDERQUERY;
			//forming the flexible search query
			final FlexibleSearchQuery orderListQuery = new FlexibleSearchQuery(queryString);
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.ORDERSTATUSONE,
					OrderStatus.REFUND_INITIATED.getCode());
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.ORDERSTATUSTWO,
					OrderStatus.REFUND_IN_PROGRESS.getCode());
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.PAYMENTPENDINGSKIPTIME, date);
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.STARTTIME, startDate);
			orderListQuery.addQueryParameter(MarketplacecommerceservicesConstants.ORDERTYPE,
					MarketplacecommerceservicesConstants.SUBORDER);

			//fetching order list from DB using flexible search query
			final List<OrderModel> orderList = getFlexibleSearchService().<OrderModel> search(orderListQuery).getResult();

			return orderList;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	@Override
	public List<MplPaymentAuditModel> fetchAuditDataList(final String guid)
	{
		try
		{
			final String queryString = //
			"SELECT {p:" + MplPaymentAuditModel.PK
					+ "} "//
					+ MarketplacecommerceservicesConstants.QUERYFROM + MplPaymentAuditModel._TYPECODE + " AS p} where" + "{p."
					+ MplPaymentAuditModel.CARTGUID + "} = ?guid";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("guid", guid);
			return getFlexibleSearchService().<MplPaymentAuditModel> search(query).getResult();
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}


	@Override
	public List<JuspayOrderStatusModel> fetchWebhookTableStatus(final String reqId)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.REFUNDCLEARWEBHHOKQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery hookListQuery = new FlexibleSearchQuery(queryString);
			hookListQuery.addQueryParameter(MarketplacecommerceservicesConstants.WEBHOOKREQSTATUS, reqId);

			final List<JuspayOrderStatusModel> hookList = flexibleSearchService.<JuspayOrderStatusModel> search(hookListQuery)
					.getResult();

			return hookList;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}


	@Override
	public List<RefundTransactionMappingModel> fetchRefundTransactionMapping(final String juspayRefundId)
	{
		try
		{
			final String queryString = //
			"SELECT {rtm:" + RefundTransactionMappingModel.PK
					+ "} "//
					+ MarketplacecommerceservicesConstants.QUERYFROM + RefundTransactionMappingModel._TYPECODE + " AS rtm} where"
					+ "{rtm." + RefundTransactionMappingModel.JUSPAYREFUNDID + "} = ?juspayRefundId";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("juspayRefundId", juspayRefundId);
			return getFlexibleSearchService().<RefundTransactionMappingModel> search(query).getResult();
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	@Override
	public RefundTransactionMappingModel fetchRefundTransactionByEntry(final AbstractOrderEntryModel orderEntry)
	{

		try
		{
			final String queryString = //
			"SELECT {rtm:" + RefundTransactionMappingModel.PK
					+ "} "//
					+ MarketplacecommerceservicesConstants.QUERYFROM + RefundTransactionMappingModel._TYPECODE + " AS rtm} where"
					+ "{rtm." + RefundTransactionMappingModel.REFUNDEDORDERENTRY + "} = ?orderEntry";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("orderEntry", orderEntry.getPk());

			return getFlexibleSearchService().<RefundTransactionMappingModel> searchUnique(query);
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






}
