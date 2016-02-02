/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		//TISPRO-129
		final List<OrderModel> orderlist = new ArrayList<OrderModel>();
		LOG.debug("********inside dao for selecting specified cancel order data**********");
		final String query = "SELECT DISTINCT {cur:" + OrderModel.PK + "} " + " FROM {" + OrderModel._TYPECODE + " AS cur "
				+ "LEFT JOIN " + OrderHistoryEntryModel._TYPECODE + "  AS adr  ON {cur:" + OrderModel.PK + "}={adr:"
				+ OrderHistoryEntryModel.ORDER + "} " + "} WHERE ({cur:" + OrderModel.MODIFIEDTIME
				+ "}  BETWEEN ?earlierDate and ?presentDate" + " OR {adr:" + OrderHistoryEntryModel.MODIFIEDTIME
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

}
