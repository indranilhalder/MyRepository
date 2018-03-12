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

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.FPCRefundEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.marketplacecommerceservices.daos.ForwardPaymentCleanUpDao;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class ForwardPaymentCleanUpDaoImpl implements ForwardPaymentCleanUpDao
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ForwardPaymentCleanUpDaoImpl.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	
	private static final String CODE = "code";
	private static final String START_TIME = "startTime";
	private static final String END_TIME = "endTime";
	private static final String ORDER_TYPE = "orderType";
	private static final String ORDER_STATUS = "orderStatus";
	private static final String GUID = "guid";
	private static final String ORDER_STATUS_ONE = "orderStatusOne";
	private static final String ORDER_STATUS_TWO = "orderStatusTwo";
	private static final String AUDIT_ID = "auditId";
	private static final String EXPIRED_FLAG = "expiredFlag";

	@Override
	public MplConfigurationModel fetchConfigDetails(final String code)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_MPLCONFIG;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(CODE, code);

		final List<MplConfigurationModel> result = flexibleSearchService.<MplConfigurationModel> search(query).getResult();

		return CollectionUtils.isNotEmpty(result) ? result.get(0) : null;
	}

	@Override
	public List<OrderModel> fetchOrdersWithMultiplePayments(final Date startTime, final Date endTime)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_MULTIPAYMENT;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(START_TIME, startTime);
		query.addQueryParameter(END_TIME, endTime);
		query.addQueryParameter(ORDER_TYPE, MarketplacecommerceservicesConstants.PARENTORDER);
		query.addQueryParameter(ORDER_STATUS, OrderStatus.PAYMENT_SUCCESSFUL);

		return flexibleSearchService.<OrderModel> search(query).getResult();
	}

	@Override
	public List<MplPaymentAuditModel> fetchAuditsForGUID(final String guid)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_AUDITBYGUID;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(GUID, guid);

		return flexibleSearchService.<MplPaymentAuditModel> search(query).getResult();
	}



	@Override
	public List<OrderModel> fetchPaymentFailedOrders(final Date startTime, final Date endTime)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_FAILEDPAYMENT;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(START_TIME, startTime);
		query.addQueryParameter(END_TIME, endTime);
		query.addQueryParameter(ORDER_TYPE, MarketplacecommerceservicesConstants.PARENTORDER);
		query.addQueryParameter(ORDER_STATUS_ONE, OrderStatus.PAYMENT_FAILED);
		query.addQueryParameter(ORDER_STATUS_TWO, OrderStatus.PAYMENT_TIMEOUT);

		return flexibleSearchService.<OrderModel> search(query).getResult();
	}

	@Override
	public List<OrderModel> fetchCodChargedOrder(final Date startTime, final Date endTime)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_CODCHARGED;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(START_TIME, startTime);
		query.addQueryParameter(END_TIME, endTime);
		query.addQueryParameter(ORDER_TYPE, MarketplacecommerceservicesConstants.PARENTORDER);
		query.addQueryParameter(ORDER_STATUS, OrderStatus.PAYMENT_SUCCESSFUL);

		return flexibleSearchService.<OrderModel> search(query).getResult();
	}

	@Override
	public List<MplPaymentAuditModel> fetchAuditsWithoutOrder(final Date startTime, final Date endTime)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_AUDITWITHOUTORDER;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(START_TIME, startTime);
		query.addQueryParameter(END_TIME, endTime);

		return flexibleSearchService.<MplPaymentAuditModel> search(query).getResult();
	}

	@Override
	public List<OrderModel> fetchRmsFailedOrders(final Date startTime, final Date endTime)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_RMSFAILED;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(START_TIME, startTime);
		query.addQueryParameter(END_TIME, endTime);
		query.addQueryParameter(ORDER_TYPE, MarketplacecommerceservicesConstants.PARENTORDER);
		query.addQueryParameter(ORDER_STATUS, OrderStatus.RMS_VERIFICATION_FAILED);

		return flexibleSearchService.<OrderModel> search(query).getResult();
	}

	@Override
	public FPCRefundEntryModel fetchRefundEntryForAuditId(final String auditId)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_REFUNDENTRY;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(AUDIT_ID, auditId);

		final List<FPCRefundEntryModel> result = flexibleSearchService.<FPCRefundEntryModel> search(query).getResult();

		return (null != result && !result.isEmpty()) ? result.get(0) : null;
	}


	@Override
	public List<FPCRefundEntryModel> fetchSpecificRefundEntries(final String expiredFlag)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_REFUNDENTRIES;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(EXPIRED_FLAG, expiredFlag);

		return flexibleSearchService.<FPCRefundEntryModel> search(query).getResult();
	}


	@Override
	public OrderModel fetchParentOrderByGUID(final String guid)
	{
		final String queryString = MarketplacecommerceservicesConstants.FPC_QUERY_PARENTORDER;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(GUID, guid);
		query.addQueryParameter(ORDER_TYPE, MarketplacecommerceservicesConstants.PARENTORDER);

		final List<OrderModel> result = flexibleSearchService.<OrderModel> search(query).getResult();
		return CollectionUtils.isNotEmpty(result) ? result.get(0) : null;
	}

	@Override
	public List<OrderModel> fetchCliqCashOrdersWithMultiplePayments(Date startTime, Date endTime)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT  {ord:" + OrderModel.PK + "} ");
		queryString.append(" FROM {" + OrderModel._TYPECODE + " AS ord},");
		queryString.append(" {" + MplPaymentAuditModel._TYPECODE + " AS mpa}");
		queryString.append(" WHERE {ord:" + OrderModel.GUID + "} = {mpa:" + MplPaymentAuditModel.CARTGUID + "}");
		queryString.append(" AND {ord:" + OrderModel.STATUS + "} = ?orderStatus");
		queryString.append(" AND {ord:" + OrderModel.CREATIONTIME + "}  BETWEEN ?startTime and ?endTime");
		queryString.append(" AND {ord:" + OrderModel.TYPE + "}  = ?orderType");
		queryString.append(" AND {ord:" + OrderModel.SPLITMODEINFO + "}  = ?cliqCash");
		queryString.append(" GROUP BY {ord:" + OrderModel.PK + "}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("startTime", startTime);
		query.addQueryParameter("endTime", endTime);
		query.addQueryParameter("orderType", MarketplacecommerceservicesConstants.PARENTORDER);
		query.addQueryParameter("orderStatus", OrderStatus.PAYMENT_SUCCESSFUL);
		query.addQueryParameter("cliqCash", MarketplacecommerceservicesConstants.PAYMENT_MODE_LIQ_CASH);

		return flexibleSearchService.<OrderModel> search(query).getResult();
	}
	
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}