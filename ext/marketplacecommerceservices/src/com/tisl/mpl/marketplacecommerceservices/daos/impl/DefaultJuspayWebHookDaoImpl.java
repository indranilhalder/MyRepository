/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.marketplacecommerceservices.daos.JuspayWebHookDao;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
@Component(value = "juspayWebHookDao")
public class DefaultJuspayWebHookDaoImpl implements JuspayWebHookDao
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultJuspayWebHookDaoImpl.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * @Decsription : Fetch Web Hook Table Details
	 * @return: List<JuspayWebhookModel>
	 */
	@Override
	public List<JuspayWebhookModel> fetchWebHookData()
	{
		LOG.debug("Fetching WebHook Details");
		final String queryString = //
		"SELECT {jwm:" + JuspayWebhookModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + JuspayWebhookModel._TYPECODE + " AS jwm} ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		return getFlexibleSearchService().<JuspayWebhookModel> search(query).getResult();
	}

	/**
	 * @Description : Fetch Audit Details Based on orderId
	 * @param :
	 *           orderId
	 */
	@Override
	public MplPaymentAuditModel fetchAuditData(final String orderId)
	{
		final String queryString = //
		"SELECT {p:" + MplPaymentAuditModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + MplPaymentAuditModel._TYPECODE + " AS p} where" + "{p."
				+ MplPaymentAuditModel.AUDITID + "} = ?orderId";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("orderId", orderId);
		return getFlexibleSearchService().<MplPaymentAuditModel> searchUnique(query);
	}

	/**
	 * @Description : Fetch Order Details Based on guid
	 * @param guid
	 */
	@Override
	public List<OrderModel> fetchOrder(final String guid)
	{
		final String queryString = //
		"SELECT {o:" + OrderModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + OrderModel._TYPECODE + " AS o} where" + "{o." + OrderModel.GUID
				+ "} = ?guid";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("guid", guid);
		return getFlexibleSearchService().<OrderModel> search(query).getResult();
	}

	/**
	 * @Description : Fetch Cron Details for Last Run time
	 * @param: code
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		final String queryString = //
		"SELECT {cm:" + MplConfigurationModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + MplConfigurationModel._TYPECODE + " AS cm } where" + "{cm."
				+ MplConfigurationModel.MPLCONFIGCODE + "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, code);
		return getFlexibleSearchService().<MplConfigurationModel> searchUnique(query);
	}

	/**
	 * @Decsription : Fetch Web Hook Table Details
	 * @param: mplConfigDate
	 * @param: startTime
	 */
	@Override
	public List<JuspayWebhookModel> fetchSpecificWebHookData(final Date mplConfigDate, final Date startTime)
	{
		final String queryString = //
		"SELECT {j:" + JuspayWebhookModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + JuspayWebhookModel._TYPECODE + " AS j} where " + "{j."
				+ JuspayWebhookModel.MODIFIEDTIME + "} <= ?earlierDate  and " + "{j." + JuspayWebhookModel.ISEXPIRED
				+ "} = ?expiredData";

		LOG.debug("earlierDate" + mplConfigDate);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("earlierDate", mplConfigDate);
		query.addQueryParameter("expiredData", MarketplacecommerceservicesConstants.WEBHOOK_ENTRY_EXPIRED);
		return getFlexibleSearchService().<JuspayWebhookModel> search(query).getResult();
	}

	/**
	 * @Decsription : Fetch Base Store Details
	 */
	@Override
	public BaseStoreModel getJobTAT()
	{
		final String queryString = //
		"SELECT {bs:" + BaseStoreModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + BaseStoreModel._TYPECODE + " AS bs } where" + "{bs."
				+ BaseStoreModel.UID + "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, MarketplacecommerceservicesConstants.BASESTORE_UID);
		return getFlexibleSearchService().<BaseStoreModel> searchUnique(query);
	}

	/**
	 * @Decsription : Fetch Audit Based on EBS Status
	 */
	@Override
	public List<MplPaymentAuditModel> fetchAUDITonEBS(final String ebsStatusReview)
	{
		final String queryString = "select {a.pk} from {JuspayEBSResponse as e}, {MplPaymentAudit as a}, {EBSResponseStatus as r}"
				+ "where {e.audit}={a.pk} and {e.ebsRiskStatus}={r.pk} and {r.code}=?status and {a.isExpired}=?expiredData";

		LOG.debug("status" + ebsStatusReview);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("status", ebsStatusReview);
		query.addQueryParameter("expiredData", MarketplacecommerceservicesConstants.WEBHOOK_ENTRY_EXPIRED);
		return getFlexibleSearchService().<MplPaymentAuditModel> search(query).getResult();
	}

	/**
	 * @Decsription : Fetch Order Details Based on GUID
	 * @param: guid
	 */
	@Override
	public OrderModel fetchOrderOnGUID(final String guid)
	{
		OrderModel orderModel = null;

		final String queryString = //
		"SELECT {om:" + OrderModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + OrderModel._TYPECODE + " AS om } where" + "{om." + OrderModel.GUID
				+ "} = ?code and " + "{om." + OrderModel.TYPE + "} = ?type";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, guid);
		query.addQueryParameter("type", "Parent");
		final List<OrderModel> orderModelList = getFlexibleSearchService().<OrderModel> search(query).getResult();
		if (!CollectionUtils.isEmpty(orderModelList))
		{
			orderModel = orderModelList.get(0);
		}
		return orderModel;
	}

	/**
	 * @Decsription : Fetch Parent Order Details Based on GUID
	 * @param: guid
	 */
	@Override
	public OrderModel fetchParentOrder(final String cartGUID)
	{
		final String queryString = //
		"SELECT {o:" + OrderModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + OrderModel._TYPECODE + " AS o } where" + "{o." + OrderModel.GUID
				+ "} = ?code and " + "{o." + OrderModel.TYPE + "} = ?type";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, cartGUID);
		query.addQueryParameter("type", "Parent");
		return getFlexibleSearchService().<OrderModel> searchUnique(query);
	}


	/**
	 * @Decsription : Fetch Audit Based on EBS Risk for the risk which are empty
	 */
	@Override
	public List<MplPaymentAuditModel> fetchAuditForEmptyRisk(final String ebsRiskPercentage)
	{
		final String queryString = "select {a.pk} from {JuspayEBSResponse as e}, {MplPaymentAudit as a}"
				+ "where {e.audit}={a.pk} and {e.ebsRiskPercentage}=?ebsRiskPercentage and {a.isExpired}=?expiredData";

		LOG.debug("ebsRiskPercentage" + ebsRiskPercentage);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("ebsRiskPercentage", ebsRiskPercentage);
		query.addQueryParameter("expiredData", MarketplacecommerceservicesConstants.WEBHOOK_ENTRY_EXPIRED);
		return getFlexibleSearchService().<MplPaymentAuditModel> search(query).getResult();
	}

	/**
	 * To fetch records from RefundTransactionMappingModel based on refund request id
	 *
	 * @param juspayRefundId
	 * @return List<RefundTransactionMappingModel>
	 */
	@Override
	public List<RefundTransactionMappingModel> fetchRefundTransactionMapping(final String juspayRefundId)
	{
		final String queryString = //
		"SELECT {rtm:" + RefundTransactionMappingModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + RefundTransactionMappingModel._TYPECODE + " AS rtm} where"
				+ "{rtm." + RefundTransactionMappingModel.JUSPAYREFUNDID + "} = ?juspayRefundId";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("juspayRefundId", juspayRefundId);
		return getFlexibleSearchService().<RefundTransactionMappingModel> search(query).getResult();
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
