/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.ReturnReasonModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;


/**
 * @author TCS
 *
 */
public class DefaultMplOrderDao implements MplOrderDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;


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
			queryParams.put("type", "SubOrder");
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
			queryParams.put("type", "Parent");
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
			queryParams.put("type", "Parent");
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
