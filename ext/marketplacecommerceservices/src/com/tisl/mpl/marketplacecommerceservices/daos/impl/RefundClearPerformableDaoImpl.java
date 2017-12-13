/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
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
		MplConfigurationModel mplConfigurationModel = new MplConfigurationModel();
		try
		{
			final String queryString = //
			"SELECT {cm:" + MplConfigurationModel.PK
					+ "} "//
					+ MarketplacecommerceservicesConstants.QUERYFROM + MplConfigurationModel._TYPECODE + " AS cm } where" + "{cm."
					+ MplConfigurationModel.MPLCONFIGCODE + "} = ?code";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, code);
			mplConfigurationModel = getFlexibleSearchService().<MplConfigurationModel> searchUnique(query);
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}

		return mplConfigurationModel;

	}




	@Override
	public List<OrderModel> getRefundClearOrders(final Date date, final Date startDate)
	{
		List<OrderModel> orderModelList = new ArrayList<OrderModel>();
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
			orderModelList = getFlexibleSearchService().<OrderModel> search(orderListQuery).getResult();

		}
		catch (final Exception e)
		{
			LOG.error(e);
		}

		return orderModelList;
	}



	@Override
	public List<MplPaymentAuditModel> fetchAuditDataList(final String guid)
	{
		List<MplPaymentAuditModel> paymentAuditModelList = new ArrayList<MplPaymentAuditModel>();
		try
		{
			final String queryString = //
			"SELECT {p:" + MplPaymentAuditModel.PK
					+ "} "//
					+ MarketplacecommerceservicesConstants.QUERYFROM + MplPaymentAuditModel._TYPECODE + " AS p} where" + "{p."
					+ MplPaymentAuditModel.CARTGUID + "} = ?guid";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("guid", guid);
			paymentAuditModelList = getFlexibleSearchService().<MplPaymentAuditModel> search(query).getResult();
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return paymentAuditModelList;

	}


	@Override
	public List<JuspayOrderStatusModel> fetchWebhookTableStatus(final String reqId)
	{
		List<JuspayOrderStatusModel> hookList = null;
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.REFUNDCLEARWEBHHOKQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery hookListQuery = new FlexibleSearchQuery(queryString);
			hookListQuery.addQueryParameter(MarketplacecommerceservicesConstants.WEBHOOKREQSTATUS, reqId);

			hookList = flexibleSearchService.<JuspayOrderStatusModel> search(hookListQuery).getResult();


		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return hookList;
	}


	@Override
	public List<RefundTransactionMappingModel> fetchRefundTransactionMapping(final String juspayRefundId)
	{

		List<RefundTransactionMappingModel> refundTransactionMappingList = new ArrayList<RefundTransactionMappingModel>();
		try
		{
			final String queryString = //
			"SELECT {rtm:" + RefundTransactionMappingModel.PK
					+ "} "//
					+ MarketplacecommerceservicesConstants.QUERYFROM + RefundTransactionMappingModel._TYPECODE + " AS rtm} where"
					+ "{rtm." + RefundTransactionMappingModel.JUSPAYREFUNDID + "} = ?juspayRefundId";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("juspayRefundId", juspayRefundId);
			refundTransactionMappingList = getFlexibleSearchService().<RefundTransactionMappingModel> search(query).getResult();
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}

		return refundTransactionMappingList;
	}

	@Override
	public RefundTransactionMappingModel fetchRefundTransactionByEntry(final AbstractOrderEntryModel orderEntry)
	{

		RefundTransactionMappingModel refundTransactionMappingModel = new RefundTransactionMappingModel();
		try
		{
			final String queryString = //
			"SELECT {rtm:" + RefundTransactionMappingModel.PK
					+ "} "//
					+ MarketplacecommerceservicesConstants.QUERYFROM + RefundTransactionMappingModel._TYPECODE + " AS rtm} where"
					+ "{rtm." + RefundTransactionMappingModel.REFUNDEDORDERENTRY + "} = ?orderEntry";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("orderEntry", orderEntry.getPk());

			refundTransactionMappingModel = getFlexibleSearchService().<RefundTransactionMappingModel> searchUnique(query);
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}

		return refundTransactionMappingModel;
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
