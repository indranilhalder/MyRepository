/**
 *
 */
package com.tisl.mpl.coupon.dao.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.dao.MplCouponDao;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public class MplCouponDaoImpl implements MplCouponDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;


	/**
	 * This method is used to fetch the active coupons from the database
	 *
	 * @return List<VoucherModel>
	 *
	 */
	@Override
	public List<VoucherModel> findVoucher()
	{
		final String queryString = MarketplacecouponConstants.VOUCHERWITHINDATEQUERY;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		return getFlexibleSearchService().<VoucherModel> search(query).getResult();
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


	/**
	 * This method is used to find closed vouchers
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherModel>
	 */
	@Override
	public SearchPageData<VoucherModel> findClosedVoucher(final CustomerModel customer, final PageableData pageableData)
	{

		try
		{
			final StringBuilder queryBiulder = new StringBuilder(500);
			final StringBuilder groupBiulder = new StringBuilder(200);

			final Set<PrincipalGroupModel> groups = customer.getGroups();
			for (final PrincipalGroupModel principal : groups)
			{
				groupBiulder.append(" OR {ur.users} like ('%").append(principal.getPk().getLongValue()).append("%') ");
			}

			final Map queryParams = new HashMap();
			queryParams.put("customerPk", customer);

			queryBiulder
					.append(
							"select {v.pk} from {Promotionvoucher as v JOIN userrestriction as ur ON {v.pk}={ur.voucher} JOIN daterestriction as dr ON {v.pk}={dr.voucher}} where {dr.startdate} <= sysdate and sysdate<= {dr.enddate} ")
					//for checking current user and user group
					.append(" AND ( {ur.users} like ('%")
					.append(customer.getPk().getLongValue())
					.append("%')")
					.append(groupBiulder.toString())
					//check voucher invalidation table
					.append(" )AND {v.redemptionQuantityLimitPerUser} >")
					.append(" ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk} ")
					.append(" AND  {vin.user}='").append(customer.getPk().getLongValue()).append("' ").append(" }})")
					.append(" AND {v.redemptionQuantityLimit} >")
					.append(" ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk}}})")
					.append(" ORDER BY {dr.startdate} ASC");

			final String CLOSED_VOUCHER = queryBiulder.toString();
			final List sortQueries = Arrays.asList(new SortQueryData[]
			{ createSortQueryData("byDate", CLOSED_VOUCHER

			) });


			return pagedFlexibleSearchService.search(sortQueries, "byDate", queryParams, pageableData);

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}

	
	/**
	 * Method used to find orders with redeemed vouchers for a user
	 *
	 * @param customer
	 * @return Set<Map<OrderModel, VoucherModel>>
	 */
	@Override
	public Set<Map<OrderModel, VoucherModel>> findVoucherRedeemedOrder(final CustomerModel customer)
	{

		final Set<Map<OrderModel, VoucherModel>> voucherRedeemedOrderMap = new LinkedHashSet<Map<OrderModel, VoucherModel>>();

		final String queryString = "select {or.pk} as Order_Model ,{v.pk} as Voucher_Model from "
				+ "{voucher as v JOIN VoucherInvalidation as ur ON  {v.pk}={ur.voucher} JOIN order "
				+ "as or ON {ur.order}={or.pk}} where {ur.user} like ('%" + customer.getPk().getLongValue() + "%') "
				+ "ORDER BY {or.creationtime} DESC";

		LOG.debug("Query :::::::::::::::   " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.setResultClassList(Arrays.asList(OrderModel.class, VoucherModel.class));
		final Map<OrderModel, VoucherModel> resultMap = new HashMap<OrderModel, VoucherModel>();

		final SearchResult<List<Object>> result = flexibleSearchService.search(query);


		for (final List<Object> row : result.getResult())
		{
			final OrderModel order = (OrderModel) row.get(0);
			final VoucherModel voucher = (VoucherModel) row.get(1);

			if (null != order.getDate() && order.getType().equalsIgnoreCase(PARENT))
			{
				try
				{
					resultMap.put(order, voucher);
					voucherRedeemedOrderMap.add(resultMap);
				}
				catch (final Exception e)
				{
					LOG.debug(e.getMessage());
				}
			}
		}
		return voucherRedeemedOrderMap;
	}
	
	/**
	 * Method used to find voucher invalidations for a user
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherInvalidationModel>
	 */
	@Override
	public SearchPageData<VoucherInvalidationModel> findVoucherHistoryRedeemedOrders(final CustomerModel customer,
			final PageableData pageableData)
	{
		try
		{

			final Map queryParams = new HashMap();
			queryParams.put("customerPk", customer);

			final String VOUCHER_HISTORY_QUERY = "select {vi.pk} from {VoucherInvalidation as vi JOIN voucher as v ON  {v.pk}={vi.voucher}  "
					+ "JOIN order as or ON {vi.order}={or.pk}} where  {vi.user} like"
					+ "('%"
					+ customer.getPk().getLongValue()
					+ "%') ORDER BY {vi.creationtime} DESC";

			System.out.println("Query :::::::::::::::" + VOUCHER_HISTORY_QUERY);
			final List sortQueries = Arrays.asList(new SortQueryData[]
			{ createSortQueryData("byDate", VOUCHER_HISTORY_QUERY) });

			return pagedFlexibleSearchService.search(sortQueries, "byDate", queryParams, pageableData);

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * Method used to create sort query data
	 *
	 * @param sortCode
	 * @param query
	 * @return SortQueryData
	 */
	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}
}
