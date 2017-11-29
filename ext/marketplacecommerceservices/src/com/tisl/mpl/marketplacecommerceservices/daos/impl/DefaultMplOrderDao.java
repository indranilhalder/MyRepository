/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.OrderShortUrlInfoModel;
import com.tisl.mpl.core.model.ReturnReasonModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;


/**
 * @author TCS
 *
 */
public class DefaultMplOrderDao implements MplOrderDao
{
	private static final Logger LOG = Logger.getLogger(DefaultMplOrderDao.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;


	//Sonar fix
	private static final String PARENT_KEY = "Parent";
	private static final String SUB_ORDER_KEY = "SubOrder";
	private static final String NULL_POINTER_OCCURER = "Null pointer Exception occured: ";


	@Override
	public List<ReturnReasonModel> getReturnReasonForOrderItem()
	{
		try
		{
			final String queryString = //
			"SELECT {" + ReturnReasonModel.PK + "}" //
					+ "FROM {" + ReturnReasonModel._TYPECODE + "} ORDER BY {" + ReturnReasonModel.REASONCODE + "} ASC";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			final List<ReturnReasonModel> listOfData = flexibleSearchService.<ReturnReasonModel> search(query).getResult();
			return listOfData;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	//TPR-5954
	@Override
	public List<ReturnReasonModel> getCategorySpecificReturnReason(final String L2CatCode) throws Exception
	{

		ReturnReasonModel returnReasons = null;
		final List<ReturnReasonModel> listOfData = new ArrayList<ReturnReasonModel>();
		try
		{
			final String queryString = "select {reasonCodes},{reasonDesc} from {ReturnReasonCatChild} where {reasonCodes} in ({{select {reasonCode} from "
					+ "{ReturnReasonCatMaster} where {prodCat}='" + L2CatCode + "' }})";


			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
			fQuery.setResultClassList(Arrays.asList(String.class, String.class));
			final SearchResult<List<Object>> rows = flexibleSearchService.search(fQuery);

			for (final List<Object> row : rows.getResult())
			{
				returnReasons = new ReturnReasonModel();
				if (null != row.get(0))
				{
					returnReasons.setReasonCode(String.valueOf(row.get(0)));
				}
				if (null != row.get(1))
				{
					returnReasons.setReasonDescription(String.valueOf(row.get(1)));
				}
				listOfData.add(returnReasons);
				returnReasons = null; //for quick garbage collection
			}
			return listOfData;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	//TPR-5954
	@Override
	public List<ReturnReasonModel> getSubReturnReason(final String parentReturnReasonCode) throws Exception
	{
		ReturnReasonModel returnReasons = null;
		final List<ReturnReasonModel> listOfData = new ArrayList<ReturnReasonModel>();
		try
		{
			final String queryString = "select {reasonCodes}, {reasonDesc} from {ReturnReasonCatChild} where {parentRelation} = '"
					+ parentReturnReasonCode + "' ";


			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
			fQuery.setResultClassList(Arrays.asList(String.class, String.class));
			final SearchResult<List<Object>> rows = flexibleSearchService.search(fQuery);

			for (final List<Object> row : rows.getResult())
			{
				returnReasons = new ReturnReasonModel();
				if (null != row.get(0))
				{
					returnReasons.setReasonCode(String.valueOf(row.get(0)));
				}
				if (null != row.get(1))
				{
					returnReasons.setReasonDescription(String.valueOf(row.get(1)));
				}
				listOfData.add(returnReasons);
				returnReasons = null; //for quick garbage collection
			}
			return listOfData;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	//TPR-5954
	@Override
	public String fetchReasonDesc(final String returnReasonCode) throws Exception
	{
		String returnReasons = null;
		try
		{
			final String queryString = "select {reasonCodes}, {reasonDesc} from {ReturnReasonCatChild} where {reasonCodes} = '"
					+ returnReasonCode + "' ";
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
			fQuery.setResultClassList(Arrays.asList(String.class, String.class));
			final SearchResult<List<Object>> rows = flexibleSearchService.search(fQuery);

			for (final List<Object> row : rows.getResult())
			{
				if (null != row.get(1))
				{
					returnReasons = (String.valueOf(row.get(1)));
				}
			}
			return returnReasons;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 *
	 */
	@Override
	public SearchPageData<OrderModel> findSubOrdersByCustomerAndStore(final CustomerModel customerModel,
			final BaseStoreModel store, final OrderStatus[] status, final PageableData pageableData)
	{
		try
		{
			//		ServicesUtil.validateParameterNotNull(customerModel, "Customer must not be null");
			//		ServicesUtil.validateParameterNotNull(store, "Store must not be null");

			final Map queryParams = new HashMap();
			queryParams.put("customer", customerModel);
			queryParams.put("store", store);
			queryParams.put(MarketplacecommerceservicesConstants.TYPE, "SubOrder");
			List sortQueries;
			if ((status != null) && (status.length > 0))
			{
				queryParams.put("statusList", Arrays.asList(status));

				sortQueries = Arrays
						.asList(new SortQueryData[]
						{
								createSortQueryData(
										MarketplacecommerceservicesConstants.BY_DATE,
										"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {status} IN (?statusList) AND {type} = ?type ORDER BY {creationtime} DESC, {pk}"),
								createSortQueryData(
										MarketplacecommerceservicesConstants.BY_ORDER_NO,
										"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {status} IN (?statusList) AND {type} = ?type ORDER BY {code},{creationtime} DESC, {pk}") });
			}
			else
			{
				sortQueries = Arrays
						.asList(new SortQueryData[]
						{
								createSortQueryData(
										MarketplacecommerceservicesConstants.BY_DATE,
										"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {type} = ?type ORDER BY {creationtime} DESC, {pk}"),
								createSortQueryData(
										MarketplacecommerceservicesConstants.BY_ORDER_NO,
										"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {type} = ?type ORDER BY {code},{creationtime} DESC, {pk}") });
			}

			return pagedFlexibleSearchService.search(sortQueries, MarketplacecommerceservicesConstants.BY_DATE, queryParams,
					pageableData);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 *
	 */
	@Override
	public SearchPageData<OrderModel> findParentOrdersByCustomerAndStore(final CustomerModel customerModel,
			final BaseStoreModel store, final OrderStatus[] status, final PageableData pageableData)
	{
		try
		{
			//		ServicesUtil.validateParameterNotNull(customerModel, "Customer must not be null");
			//		ServicesUtil.validateParameterNotNull(store, "Store must not be null");

			final Map queryParams = new HashMap();
			queryParams.put("customer", customerModel);
			queryParams.put("store", store);
			queryParams.put(MarketplacecommerceservicesConstants.TYPE, PARENT_KEY);
			List sortQueries;
			if ((status != null) && (status.length > 0))
			{
				queryParams.put("statusList", Arrays.asList(status));

				sortQueries = Arrays
						.asList(new SortQueryData[]
						{
								createSortQueryData(
										MarketplacecommerceservicesConstants.BY_DATE,
										"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {status} IN (?statusList) AND {type} = ?type ORDER BY {creationtime} DESC, {pk}"),
								createSortQueryData(
										MarketplacecommerceservicesConstants.BY_ORDER_NO,
										"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {status} IN (?statusList) AND {type} = ?type ORDER BY {code},{creationtime} DESC, {pk}") });
			}
			else
			{
				sortQueries = Arrays
						.asList(new SortQueryData[]
						{
								createSortQueryData(
										MarketplacecommerceservicesConstants.BY_DATE,
										"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {type} = ?type ORDER BY {creationtime} DESC, {pk}"),
								createSortQueryData(
										MarketplacecommerceservicesConstants.BY_ORDER_NO,
										"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {type} = ?type ORDER BY {code},{creationtime} DESC, {pk}") });
			}

			return pagedFlexibleSearchService.search(sortQueries, MarketplacecommerceservicesConstants.BY_DATE, queryParams,
					pageableData);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Desc : Returns the order history with duration as filter TISEE-1855
	 * @param customerModel
	 * @param store
	 * @param paramPageableData
	 * @param fromDate
	 * @return SearchPageData
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public SearchPageData<OrderModel> getPagedFilteredParentOrderHistory(final CustomerModel customerModel,
			final BaseStoreModel store, final PageableData paramPageableData, final Date fromDate)
	{
		try
		{
			final Map queryParams = new HashMap();
			queryParams.put("customer", customerModel);
			queryParams.put("store", store);
			queryParams.put(MarketplacecommerceservicesConstants.TYPE, PARENT_KEY);
			queryParams.put("creationtime", fromDate);

			final List sortQueries = Arrays
					.asList(new SortQueryData[]
					{
							createSortQueryData(
									MarketplacecommerceservicesConstants.BY_DATE,
									"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {type} = ?type AND {creationtime} >= ?creationtime ORDER BY {creationtime} DESC, {pk}"),
							createSortQueryData(
									MarketplacecommerceservicesConstants.BY_ORDER_NO,
									"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {type} = ?type AND {creationtime} >= ?creationtime ORDER BY {code},{creationtime} DESC, {pk}") });


			return pagedFlexibleSearchService.search(sortQueries, MarketplacecommerceservicesConstants.BY_DATE, queryParams,
					paramPageableData);

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}



	/**
	 * This method returns the consignment model depending on the code passed as parameter
	 *
	 * @param consignmentCode
	 * @return ConsignmentModel
	 *
	 */
	@Override
	public ConsignmentModel fetchConsignment(final String consignmentCode)
	{
		try
		{
			ConsignmentModel consignmentModel = new ConsignmentModel();
			final String queryString = MarketplacecommerceservicesConstants.CONSIGNMENTNQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery consignmentQuery = new FlexibleSearchQuery(queryString);
			consignmentQuery.addQueryParameter(MarketplacecommerceservicesConstants.CONSIGNMENTCODE, consignmentCode);

			//fetching consignment from DB using flexible search query
			final List<ConsignmentModel> consignmentList = flexibleSearchService.<ConsignmentModel> search(consignmentQuery)
					.getResult();
			if (null != consignmentList && !consignmentList.isEmpty())
			{
				consignmentModel = consignmentList.get(0);
				return consignmentModel;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	@Override
	public List<CancellationReasonModel> fetchCancellationReason()
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.CANCELLATIONREASONQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery cancellationReasonQuery = new FlexibleSearchQuery(queryString);

			//fetching cancellation reasons from DB using flexible search query
			final List<CancellationReasonModel> cancellationReasonList = flexibleSearchService.<CancellationReasonModel> search(
					cancellationReasonQuery).getResult();
			if (null != cancellationReasonList && !cancellationReasonList.isEmpty())
			{
				return cancellationReasonList;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	/**
	 *
	 * @param cartGUID
	 * @return MplPaymentAuditModel
	 */
	@Override
	public MplPaymentAuditModel getAuditList(final String cartGUID)
	{
		try
		{
			MplPaymentAuditModel mplPaymentAudit = new MplPaymentAuditModel();
			final String queryString = MarketplacecommerceservicesConstants.AUDITWITHGUIDQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery auditWithGUIDQuery = new FlexibleSearchQuery(queryString);
			auditWithGUIDQuery.addQueryParameter(MarketplacecommerceservicesConstants.CARTGUID, cartGUID);

			//fetching list of Audit Entries from DB using flexible search query
			final List<MplPaymentAuditModel> mplPaymentAuditList = flexibleSearchService.<MplPaymentAuditModel> search(
					auditWithGUIDQuery).getResult();
			if (null != mplPaymentAuditList && !mplPaymentAuditList.isEmpty())
			{
				mplPaymentAudit = mplPaymentAuditList.get(0);
				return mplPaymentAudit;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Desc : To fetch order model list for a guid //TISPRD-181
	 * @param cartModel
	 * @return List<OrderModel>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<OrderModel> getOrderForGuid(final CartModel cartModel) throws EtailNonBusinessExceptions
	{
		try
		{
			//
			final String query = "SELECT {om:pk} FROM {Order as om} WHERE {guid} = ?guid and ( {type} = ?type OR {type} IS NULL ) ";
			final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
			flexiQuery.addQueryParameter("guid", cartModel.getGuid());
			flexiQuery.addQueryParameter(MarketplacecommerceservicesConstants.TYPE, PARENT_KEY);
			final List<OrderModel> orderModelList = flexibleSearchService.<OrderModel> search(flexiQuery).getResult();
			return (CollectionUtils.isNotEmpty(orderModelList)) ? orderModelList : null;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 *
	 * To get short-url for order
	 */
	@Override
	public String getShortUrl(final String orderCode)
	{
		LOG.info("getting short URL for order:" + orderCode);
		String shortUrl = null;
		try
		{
			final String query = "SELECT {osu:pk} FROM {OrderShortUrlInfo as osu} WHERE {orderId} = ?orderId";
			final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
			flexiQuery.addQueryParameter("orderId", orderCode);
			final List<OrderShortUrlInfoModel> orderModelList = flexibleSearchService.<OrderShortUrlInfoModel> search(flexiQuery)
					.getResult();
			if (null != orderModelList && orderModelList.size() > 0)
			{
				shortUrl = orderModelList.get(0).getShortURL();
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("short Url for order:" + orderCode + " is " + shortUrl);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return shortUrl;
	}

	/**
	 *
	 * To get AbstractOrderEntryModel Beased on transactionId
	 */
	@Override
	public AbstractOrderEntryModel getEntryModel(final String transactionId)
	{
		try
		{
			final String queryString = "SELECT {srm:" + AbstractOrderEntryModel.PK + "}" + " FROM {"
					+ AbstractOrderEntryModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:" + AbstractOrderEntryModel.TRANSACTIONID
					+ "}=?code ";
			if (LOG.isDebugEnabled())
			{
				LOG.debug("In getEntryModel - transactionId ***" + transactionId);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
			fQuery.addQueryParameter("code", transactionId);

			final List<AbstractOrderEntryModel> listOfData = flexibleSearchService.<AbstractOrderEntryModel> search(fQuery)
					.getResult();
			return !listOfData.isEmpty() ? listOfData.get(0) : null;
		}
		catch (final Exception e)
		{
			LOG.error("Error while searching  AbstractOrderEntryModel model by transactionId  " + transactionId);
		}
		return null;

	}

	//TPR-5225
	@Override
	public List<OrderModel> getOrderByMobile(final String mobileNo, final int queryCount, final int transactionLimit)
	{
		try
		{
			LOG.debug("QueryCount**" + queryCount);
			//SDI-1193
			LOG.debug("transactionlimit**" + transactionLimit);
			final String query = MarketplacecommerceservicesConstants.MOBILE_QUERY;
			final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
			flexiQuery.addQueryParameter("mobileNo", mobileNo);
			flexiQuery.addQueryParameter("type", PARENT_KEY);
			flexiQuery.addQueryParameter("queryCount", String.valueOf(queryCount));
			flexiQuery.addQueryParameter("transactionLimit", String.valueOf(transactionLimit));
			final List<OrderModel> listOfData = flexibleSearchService.<OrderModel> search(flexiQuery).getResult();
			return !listOfData.isEmpty() ? listOfData : null;
		}
		catch (final NullPointerException e)
		{
			LOG.error(NULL_POINTER_OCCURER, e);
		}
		catch (final Exception e)
		{
			LOG.error("Error while fetching orderModel by mobileNo  ", e);
		}
		return null;
	}

	//TPR-5225
	@Override
	public String getL4CategoryName(final String productCode)
	{
		String l4CategoryId = null;
		try
		{
			final String query = MarketplacecommerceservicesConstants.MOBILE_QUERY_FOR_L4CATEGORY;
			final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
			flexiQuery.addQueryParameter("productCode", productCode);
			final List<CategoryModel> l4List = flexibleSearchService.<CategoryModel> search(flexiQuery).getResult();
			if (null != l4List && l4List.size() > 0)
			{
				l4CategoryId = l4List.get(0).getName();
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("l4 category id is " + l4CategoryId);
			}
		}
		catch (final NullPointerException e)
		{
			LOG.error(NULL_POINTER_OCCURER, e);
		}
		catch (final Exception e)
		{
			LOG.error("Error while fetching l4 category", e);
		}
		return l4CategoryId;
	}

	//TPR-4841
	@Override
	public OrderModel fetchOrderByTransaction(final String transactionId)
	{
		try
		{
			final String query = MarketplacecommerceservicesConstants.TRANSACTION_QUERY;
			final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
			flexiQuery.addQueryParameter("transactionId", transactionId);
			flexiQuery.addQueryParameter("type", SUB_ORDER_KEY);
			final List<OrderModel> listOfData = flexibleSearchService.<OrderModel> search(flexiQuery).getResult();
			return !listOfData.isEmpty() ? listOfData.get(0) : null;
		}
		catch (final NullPointerException e)
		{
			LOG.error(NULL_POINTER_OCCURER, e);
		}
		catch (final Exception e)
		{
			LOG.error("Error while fetching orderModel by transactionId  ", e);
		}
		return null;
	}

	//TPR-4840
	@Override
	public OrderModel getOrderByParentOrder(final String orderRefNo)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.PARENT_ORDER_QUERY;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
			fQuery.addQueryParameter("type", PARENT_KEY);
			fQuery.addQueryParameter("orderRefNo", orderRefNo);
			final List<OrderModel> listOfData = flexibleSearchService.<OrderModel> search(fQuery).getResult();
			return !listOfData.isEmpty() ? listOfData.get(0) : null;
		}
		catch (final NullPointerException e)
		{
			LOG.error(NULL_POINTER_OCCURER, e);
		}
		catch (final Exception e)
		{
			LOG.error("Error while fetching orderModel by orderId  ", e);
		}
		return null;

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
	 * @return the pagedFlexibleSearchService
	 */
	public PagedFlexibleSearchService getPagedFlexibleSearchService()
	{
		return pagedFlexibleSearchService;
	}

	/**
	 * @param pagedFlexibleSearchService
	 *           the pagedFlexibleSearchService to set
	 */
	public void setPagedFlexibleSearchService(final PagedFlexibleSearchService pagedFlexibleSearchService)
	{
		this.pagedFlexibleSearchService = pagedFlexibleSearchService;
	}

}
