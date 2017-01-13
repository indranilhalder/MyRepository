/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.NPSEmailerModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.FetchSalesOrderDao;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
@Component(value = "fetchSalesOrderDao")
public class DefaultFetchSalesOrderDaoImpl implements FetchSalesOrderDao
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultFetchSalesOrderDaoImpl.class.getName());

	private final String PARENT = "Parent";
	private final String SUB = "SubOrder";
	private static final String SELECT_CLASS = "SELECT {p:";
	private static final String FROM_CLASS = "FROM {";
	//private static final String WHERE_CLASS = "WHERE ";
	private static final String P_CLASS = "{p.";
	private static final String TYPE_CLASS = "} = ?type";

	private static final String TYPE = "type";

	/**
	 * @return the pARENT
	 */
	public String getPARENT()
	{
		return PARENT;
	}


	@Autowired
	private FlexibleSearchService flexibleSearchService;



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
	 * @Description : Fetch Cron Configuration Details
	 * @param: code
	 * @return : MplConfigurationModel
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		final String queryString = //
		SELECT_CLASS + MplConfigurationModel.PK
				+ "} "//
				+ FROM_CLASS + MplConfigurationModel._TYPECODE + " AS p } where" + P_CLASS + MplConfigurationModel.MPLCONFIGCODE
				+ "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);
		return flexibleSearchService.<MplConfigurationModel> searchUnique(query);
	}


	/**
	 * @Description : Fetch Parent Order Details
	 * @return : List<OrderModel>
	 */
	@Override
	public List<OrderModel> fetchOrderDetails()
	{
		LOG.debug("db call fetch details");
		final String queryString = //
		SELECT_CLASS + OrderModel.PK + "} "//
				+ FROM_CLASS + OrderModel._TYPECODE + " AS p } where" + P_CLASS + OrderModel.TYPE + TYPE_CLASS;

		LOG.debug("db call fetch details success");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(TYPE, PARENT);
		LOG.debug("********** fetch order details query " + query);
		return flexibleSearchService.<OrderModel> search(query).getResult();

	}


	@Override
	public List<OrderModel> fetchCancelOrderDetails()
	{
		final List<OrderModel> orderlist = new ArrayList<OrderModel>();
		LOG.debug("db call fetch all details: cancel 1 st time call");
		final String queryString = //
		SELECT_CLASS + OrderModel.PK + "} "//
				+ FROM_CLASS + OrderModel._TYPECODE + " AS p } where" + P_CLASS + OrderModel.TYPE + TYPE_CLASS;

		LOG.debug("db call fetch details success");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(TYPE, SUB);
		LOG.debug("********** fetch order details query " + query);
		final List<OrderModel> orderList = flexibleSearchService.<OrderModel> search(query).getResult();
		if (orderList != null && orderList.size() > 0)
		{
			for (final OrderModel orderModel : orderList)
			{
				if (orderModel.getVersionID() == null)
				{
					orderlist.add(orderModel);
				}
			}
		}
		return orderlist;

	}



	/**
	 * @Description : Fetch Parent Order Details within stipulated Date Range
	 * @return : List<OrderModel>
	 */
	@Override
	public List<OrderModel> fetchSpecifiedData(final Date startTime, final Date endTime)
	{
		LOG.debug("db call fetch  specified details");
		final String queryString = //
		SELECT_CLASS + OrderModel.PK
				+ "} "//
				+ FROM_CLASS + OrderModel._TYPECODE + " AS p} where " + P_CLASS + OrderModel.CREATIONTIME
				+ "} BETWEEN ?earlierDate and ?presentDate and " + P_CLASS + OrderModel.TYPE + TYPE_CLASS;
		LOG.debug("db call fetch  specified details success");
		LOG.debug("earlierDate" + startTime);
		LOG.debug("presentDate" + endTime);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("earlierDate", startTime);
		query.addQueryParameter("presentDate", endTime);
		query.addQueryParameter(TYPE, PARENT);
		LOG.debug("********** specified data query" + query);
		return flexibleSearchService.<OrderModel> search(query).getResult();

	}



	@Override
	public List<OrderModel> fetchSpecifiedCancelData(final Date earlierDate, final Date presentDate)
	{
		//TISPRO-129 && TISPRD 2511 query modified
		final List<OrderModel> orderlist = new ArrayList<OrderModel>();
		LOG.debug("********inside dao for selecting specified cancel order data**********");
		final String query = "SELECT DISTINCT {cur:" + OrderModel.PK + "} " + " FROM {" + OrderModel._TYPECODE + " AS cur "
				+ "LEFT JOIN " + OrderHistoryEntryModel._TYPECODE + "  AS adr  ON {cur:" + OrderModel.PK + "}={adr:"
				+ OrderHistoryEntryModel.ORDER + "} " + "} WHERE ({adr:" + OrderHistoryEntryModel.MODIFIEDTIME
				+ "} BETWEEN ?earlierDate and ?presentDate)" + "and " + "{cur." + OrderModel.TYPE + TYPE_CLASS;

		final Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("earlierDate", earlierDate);
		params.put("presentDate", presentDate);
		params.put(TYPE, SUB);
		final FlexibleSearchQuery queryString = new FlexibleSearchQuery(query);
		LOG.debug("********** specified data query" + queryString);
		final SearchResult<OrderModel> searchRes = flexibleSearchService.search(query, params);// removing toString SONAR Analysis
		if (searchRes != null && searchRes.getCount() > 0)
		{
			for (final OrderModel orderModel : searchRes.getResult())
			{
				//TISPRO-129
				if (orderModel.getVersionID() == null)
				{
					orderlist.add(orderModel);
				}
			}
		}
		return orderlist;
	}

	/**
	 *
	 */
	@Override
	public Map<OrderModel, AbstractOrderEntryModel> fetchOrderDetailsforDeliveryMail()
	{
		final Map<OrderModel, AbstractOrderEntryModel> orderWithSingleEntry = new HashMap<OrderModel, AbstractOrderEntryModel>();

		final String queryString = "SELECT {po.pk},{oe.pk},{po.user} FROM  {" + ConsignmentModel._TYPECODE + " AS c JOIN "
				+ ConsignmentEntryModel._TYPECODE + " " + "AS ce ON {ce.consignment} = {c.PK} JOIN"
				+ " ConsignmentStatus as cs ON {c.status} = {cs.PK} JOIN " + AbstractOrderEntryModel._TYPECODE
				+ " AS oe ON {ce.orderentry}= {oe.PK} JOIN " + OrderModel._TYPECODE + " AS co  ON {c.order}={co.PK} JOIN "
				+ OrderModel._TYPECODE + " AS po  ON {co.parentreference} = {po.PK}} " + "WHERE "
				//+ "{c.deliveryDate}  BETWEEN "+ "(sysdate-10) AND (sysdate-1) AND "
				+ "{cs.code}='DELIVERED'";

		//final String queryString = "select {po.pk},{oe.pk},{oe.orderlineid} from {" + ConsignmentModel._TYPECODE + " as c JOIN "
		//	+ ConsignmentEntryModel._TYPECODE + " as ce ON {ce.consignment} = {c.PK}" + " JOIN "
		//	+ AbstractOrderEntryModel._TYPECODE + " as oe ON {ce.orderentry}= {oe.PK}" + " JOIN " + OrderModel._TYPECODE
		//	+ " as co  ON {c.order}={co.PK}" + " JOIN " + OrderModel._TYPECODE + " as po  ON {co.parentreference} = {po.PK}} ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.setResultClassList(Arrays.asList(OrderModel.class, AbstractOrderEntryModel.class, CustomerModel.class));

		final SearchResult<List<Object>> result = flexibleSearchService.search(query);

		if (!result.getResult().isEmpty())
		{
			for (final List<Object> obj : result.getResult())
			{
				final OrderModel orderModel = (OrderModel) obj.get(0);
				final AbstractOrderEntryModel absOrderEntryModel = (AbstractOrderEntryModel) obj.get(1);
				//final CustomerModel customerModel = (CustomerModel) obj.get(2);

				if (!orderWithSingleEntry.containsKey(orderModel))
				{
					orderWithSingleEntry.put(orderModel, absOrderEntryModel);
				}
			}
		}
		return orderWithSingleEntry;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.FetchSalesOrderDao#getTransactionIdCount(de.hybris.platform.core
	 * .model.order.OrderModel)
	 */
	@Override
	public Map<String, Integer> getTransactionIdCount()
	{
		// YTODO Auto-generated method stub
		LOG.debug("********inside dao for getTransactionIdCount**********");
		final Map<String, Integer> npsTableMap = new HashMap<String, Integer>();
		final String queryString = "select {nps.parentOrderNo},COUNT({nps.transactionId}) from {" + NPSEmailerModel._TYPECODE
				+ " as nps }  WHERE {nps.AllEmailSent}=false group by {nps.parentOrderNo} ";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.setResultClassList(Arrays.asList(OrderModel.class, Integer.class));
		final SearchResult<List<Object>> result = flexibleSearchService.search(query);
		if (result.getResult().isEmpty())
		{
			//throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3000);
		}
		else
		{

			for (final List<Object> obj : result.getResult())
			{


				final String parentOrdeNo = ((OrderModel) obj.get(0)).getCode();
				final Integer transactionIdCount = (Integer) obj.get(1);



				npsTableMap.put(parentOrdeNo, transactionIdCount);

			}


		}
		return npsTableMap;

	}

	@Override
	public Map<String, NPSEmailerModel> getTransactionIdList()
	{
		LOG.debug("********inside dao for getTransactionIdList**********");
		final Map<String, NPSEmailerModel> npsEmailerTransactionIdList = new HashMap<String, NPSEmailerModel>();
		final String queryString = "select {nps.transactionId},{nps.pk} from {" + NPSEmailerModel._TYPECODE
				+ " as nps} WHERE {nps.AllEmailSent}=false";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.setResultClassList(Arrays.asList(String.class, NPSEmailerModel.class));
		final SearchResult<List<Object>> result = flexibleSearchService.search(query);
		if (result.getResult().isEmpty())
		{
			//throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3000);
		}
		else
		{
			for (final List<Object> obj : result.getResult())
			{


				final String transactionIdList = (String) obj.get(0);
				final NPSEmailerModel npsEmailerModel = (NPSEmailerModel) obj.get(1);



				npsEmailerTransactionIdList.put(transactionIdList, npsEmailerModel);

			}

		}
		return npsEmailerTransactionIdList;
	}

	@Override
	public Map<String, Integer> getorderModelTransactionCount(final Set<String> parentOrderSet)
	{
		LOG.debug("********inside dao for getorderModelTransactionCount**********");
		final Map<String, Integer> transactionIdCountMap = new HashMap<String, Integer>();


		String orderIdsStr;

		final StringBuilder orderIds = new StringBuilder();

		final Iterator<String> iter = parentOrderSet.iterator();
		while (iter.hasNext())
		{


			orderIds.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + iter.next()
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA + MarketplacecommerceservicesConstants.COMMA_DELIMITER);

		}
		orderIdsStr = orderIds.toString();
		orderIdsStr = orderIdsStr.substring(0, orderIdsStr.length() - 1);

		final String queryString = "select {po.code},count(*) from {Order as po JOIN Order as co ON {po.PK} = {co.parentReference} JOIN OrderEntry as oe ON {co.PK} = {oe.order}  JOIN ConsignmentEntry as ce  ON {oe.PK}={ce.orderEntry} JOIN Consignment as c   ON {c.PK}={ce.consignment} JOIN"
				+ " ConsignmentStatus as cs  ON  {c.status} ={cs.PK} } where {cs.code} !=  'ORDER_CANCELLED' and {po.code} in ("
				+ orderIdsStr + ") group by {po.code}";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.setResultClassList(Arrays.asList(String.class, Integer.class));
		final SearchResult<List<Object>> result = flexibleSearchService.search(query);
		if (result.getResult().isEmpty())
		{
			//throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3000);
		}
		else
		{
			for (final List<Object> obj : result.getResult())
			{

				final String parentOrderId = (String) obj.get(0);
				final Integer transactionIdCount = (Integer) obj.get(1);




				transactionIdCountMap.put(parentOrderId, transactionIdCount);

			}
		}
		return transactionIdCountMap;
	}

	@Override
	public List<Map> getOrderModelTransactionId(final Set<String> parentOrderIds)
	{

		LOG.debug("********inside dao for getOrderModelTransactionId**********");

		final List<Map> transIdList = new ArrayList<Map>();
		if (CollectionUtils.isEmpty(parentOrderIds))
		{
			return transIdList;
		}

		final Map<String, OrderModel> orderModelMap = new HashMap<String, OrderModel>();
		final Map<String, List<String>> transactionIdMap = new HashMap<String, List<String>>();
		final StringBuilder orderIds = new StringBuilder();
		String orderIdsStr;
		final Iterator<String> iter = parentOrderIds.iterator();
		while (iter.hasNext())
		{

			orderIds.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + iter.next()
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA + MarketplacecommerceservicesConstants.COMMA_DELIMITER);

		}
		orderIdsStr = orderIds.toString();
		orderIdsStr = orderIdsStr.substring(0, orderIdsStr.length() - 1);
		final String queryString = "select {po.pk},{oe.orderlineid} from {Order as po JOIN Order as co ON {po.PK} = {co.parentReference} JOIN OrderEntry as oe ON {co.PK} = {oe.order}  JOIN ConsignmentEntry as ce  ON {oe.PK}={ce.orderEntry} JOIN Consignment as c   ON {c.PK}={ce.consignment} JOIN"
				+ " ConsignmentStatus as cs  ON  {c.status} ={cs.PK} } where {cs.code} =  'DELIVERED' and {po.code} in ("
				+ orderIdsStr + ")";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.setResultClassList(Arrays.asList(OrderModel.class, String.class));
		final SearchResult<List<Object>> result = flexibleSearchService.search(query);
		if (result.getResult().isEmpty())
		{
			//throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3000);
		}
		else
		{
			for (final List<Object> obj : result.getResult())
			{
				final OrderModel order = (OrderModel) obj.get(0);
				final String orderId = order.getCode();
				final String transactionId = (String) obj.get(1);

				if (transactionIdMap.get(orderId) == null)
				{
					final List<String> transIds = new ArrayList<String>();
					transIds.add(transactionId);
					transactionIdMap.put(orderId, transIds);
					orderModelMap.put(orderId, order);
				}
				else
				{
					transactionIdMap.get(orderId).add(transactionId);
					orderModelMap.put(orderId, order);
				}


			}
			transIdList.add(transactionIdMap);
			transIdList.add(orderModelMap);
		}



		return transIdList;
	}

	@Override
	public Map<String, String> fetchOrderIdsToday()
	{
		//code here
		LOG.debug("********inside dao for fetchOrderIdsToday**********");
		final Map<String, String> ordersEmailSntTodayMap = new HashMap<String, String>();

		//final int date1 = date.getDate();
		final String queryString = "select {po.code} from {Order as po JOIN NPSEmailer as nps ON {po.PK}={nps.parentOrderNo}} where {nps.timeSent}= to_date('05-JAN-17','DD-MON-YY')";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		System.out.println("queryString" + queryString);

		query.setResultClassList(Arrays.asList(String.class));
		final List<String> orderIdList = flexibleSearchService.<String> search(query).getResult();
		if (orderIdList != null)
		{
			for (final String str : orderIdList)
			{
				ordersEmailSntTodayMap.put(str, null);
			}
		}
		return ordersEmailSntTodayMap;

	}



}
