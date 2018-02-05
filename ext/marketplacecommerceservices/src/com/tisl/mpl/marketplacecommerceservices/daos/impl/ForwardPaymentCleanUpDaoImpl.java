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

	@Override
	public MplConfigurationModel fetchConfigDetails(final String code)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT {mcf:" + MplConfigurationModel.PK + "}");
		queryString.append(" FROM {" + MplConfigurationModel._TYPECODE + " AS mcf}");
		queryString.append(" WHERE" + "{mcf:" + MplConfigurationModel.MPLCONFIGCODE + "} = ?code");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);

		final List<MplConfigurationModel> result = flexibleSearchService.<MplConfigurationModel> search(query).getResult();

		return CollectionUtils.isNotEmpty(result) ? result.get(0) : null;
	}

	@Override
	public List<OrderModel> fetchOrdersWithMultiplePayments(final Date startTime, final Date endTime)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT  {ord:" + OrderModel.PK + "} ");
		queryString.append(" FROM {" + OrderModel._TYPECODE + " AS ord},");
		queryString.append(" {" + MplPaymentAuditModel._TYPECODE + " AS mpa}");
		queryString.append(" WHERE {ord:" + OrderModel.GUID + "} = {mpa:" + MplPaymentAuditModel.CARTGUID + "}");
		queryString.append(" AND {ord:" + OrderModel.STATUS + "} = ?orderStatus");
		queryString.append(" AND {ord:" + OrderModel.CREATIONTIME + "}  BETWEEN ?startTime and ?endTime");
		queryString.append(" AND {ord:" + OrderModel.TYPE + "}  = ?orderType");
		queryString.append(" GROUP BY {ord:" + OrderModel.PK + "}");
		queryString.append(" HAVING COUNT(1) > 1");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("startTime", startTime);
		query.addQueryParameter("endTime", endTime);
		query.addQueryParameter("orderType", MarketplacecommerceservicesConstants.PARENTORDER);
		query.addQueryParameter("orderStatus", OrderStatus.PAYMENT_SUCCESSFUL);

		return flexibleSearchService.<OrderModel> search(query).getResult();
	}


	@Override
	public List<MplPaymentAuditModel> fetchAuditsForGUID(final String guid)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT  {mpa:" + MplPaymentAuditModel.PK + "} ");
		queryString.append(" FROM {" + MplPaymentAuditModel._TYPECODE + " AS mpa");
		queryString.append("} WHERE {mpa:" + MplPaymentAuditModel.CARTGUID + "}  = ?guid");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("guid", guid);

		return flexibleSearchService.<MplPaymentAuditModel> search(query).getResult();
	}



	@Override
	public List<OrderModel> fetchPaymentFailedOrders(final Date startTime, final Date endTime)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT  {ord:" + OrderModel.PK + "} ");
		queryString.append(" FROM {" + OrderModel._TYPECODE + " AS ord}");
		queryString.append(" WHERE {ord:" + OrderModel.STATUS + "} IN ( ?orderStatusOne , ?OrderStatusTwo )");
		queryString.append(" AND {ord:" + OrderModel.CREATIONTIME + "}  BETWEEN ?startTime and ?endTime");
		queryString.append(" AND {ord:" + OrderModel.TYPE + "}  = ?orderType");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("startTime", startTime);
		query.addQueryParameter("endTime", endTime);
		query.addQueryParameter("orderType", MarketplacecommerceservicesConstants.PARENTORDER);
		query.addQueryParameter("orderStatusOne", OrderStatus.PAYMENT_FAILED);
		query.addQueryParameter("orderStatusTwo", OrderStatus.PAYMENT_TIMEOUT);

		return flexibleSearchService.<OrderModel> search(query).getResult();
	}

	@Override
	public List<MplPaymentAuditModel> fetchAuditsWithoutOrder(final Date startTime, final Date endTime)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT {pa:" + MplPaymentAuditModel.PK + "}");
		queryString.append(" FROM {" + MplPaymentAuditModel._TYPECODE + " AS pa");
		queryString.append(" LEFT JOIN " + OrderModel._TYPECODE + " AS ord");
		queryString.append(" ON {pa:" + MplPaymentAuditModel.CARTGUID + "} = {ord:" + OrderModel.GUID + "}}");
		queryString.append(" WHERE {ord:" + OrderModel.PK + "} is null");
		queryString.append(" AND {pa:" + MplPaymentAuditModel.CREATIONTIME + "}");
		queryString.append(" BETWEEN ?startTime AND ?endTime");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("startTime", startTime);
		query.addQueryParameter("endTime", endTime);

		return flexibleSearchService.<MplPaymentAuditModel> search(query).getResult();
	}

	@Override
	public List<OrderModel> fetchRmsFailedOrders(final Date startTime, final Date endTime)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT  {ord:" + OrderModel.PK + "} ");
		queryString.append(" FROM {" + OrderModel._TYPECODE + " AS ord}");
		queryString.append(" WHERE {ord:" + OrderModel.STATUS + "} = ?orderStatus");
		queryString.append(" AND {ord:" + OrderModel.CREATIONTIME + "}  BETWEEN ?startTime and ?endTime");
		queryString.append(" AND {ord:" + OrderModel.TYPE + "}  = ?orderType");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("startTime", startTime);
		query.addQueryParameter("endTime", endTime);
		query.addQueryParameter("orderType", MarketplacecommerceservicesConstants.PARENTORDER);
		query.addQueryParameter("orderStatus", OrderStatus.RMS_VERIFICATION_FAILED);

		return flexibleSearchService.<OrderModel> search(query).getResult();
	}

	@Override
	public FPCRefundEntryModel fetchRefundEntryForAuditId(final String auditId)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT {fre:" + FPCRefundEntryModel.PK + "}");
		queryString.append(" FROM {" + FPCRefundEntryModel._TYPECODE + " AS fre}");
		queryString.append(" WHERE" + "{fre:" + FPCRefundEntryModel.AUDITID + "} = ?auditId");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("auditId", auditId);

		final List<FPCRefundEntryModel> result = flexibleSearchService.<FPCRefundEntryModel> search(query).getResult();

		return (null != result && !result.isEmpty()) ? result.get(0) : null;
	}


	@Override
	public List<FPCRefundEntryModel> fetchSpecificRefundEntries(final String expiredFlag)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT {fre:" + FPCRefundEntryModel.PK + "}");
		queryString.append(" FROM {" + FPCRefundEntryModel._TYPECODE + " AS fre}");
		queryString.append(" WHERE" + "{fre:" + FPCRefundEntryModel.ISEXPIRED + "} = ?expiredFlag");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("expiredFlag", expiredFlag);

		return flexibleSearchService.<FPCRefundEntryModel> search(query).getResult();
	}


	@Override
	public OrderModel fetchParentOrderByGUID(final String guid)
	{
		final StringBuilder queryString = new StringBuilder(500);
		queryString.append("SELECT {ord:" + OrderModel.PK + "}");
		queryString.append(" FROM {" + OrderModel._TYPECODE + " AS ord}");
		queryString.append(" WHERE" + "{ord:" + OrderModel.GUID + "} = ?GUID");
		queryString.append(" AND {ord:" + OrderModel.TYPE + "}  = ?orderType");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("guid", guid);
		query.addQueryParameter("orderType", MarketplacecommerceservicesConstants.PARENTORDER);

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

}
