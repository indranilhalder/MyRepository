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

		return flexibleSearchService.<MplConfigurationModel> searchUnique(query);
	}

	@Override
	public List<OrderModel> fetchSpecificOrders(final Date startTime, final Date endTime)
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
		queryString.append(" FROM {" + MplPaymentAuditModel._TYPECODE + " AS mpa ");
		queryString.append("} WHERE {mpa:" + MplPaymentAuditModel.CARTGUID + "}  = ?guid");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("guid", guid);

		return flexibleSearchService.<MplPaymentAuditModel> search(query).getResult();
	}

}
