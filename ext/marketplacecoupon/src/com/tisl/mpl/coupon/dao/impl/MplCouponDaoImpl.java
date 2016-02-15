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
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
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
	private static final Logger LOG = Logger.getLogger(MplCouponDaoImpl.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;

	@Override
	public List<VoucherModel> findVoucher()
	{
		final String queryString = MarketplacecouponConstants.VOUCHERWITHINDATEQUERY;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		LOG.debug("Query::::::::" + query.toString());

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.coupon.dao.MplCouponDao#findClosedVoucher()
	 */
	@Override
	public Set<Map<VoucherModel, DateRestrictionModel>> findClosedVoucher()
	{
		final Set<Map<VoucherModel, DateRestrictionModel>> voucherWithStartDateMap = new LinkedHashSet<Map<VoucherModel, DateRestrictionModel>>();

		final String queryString = MarketplacecouponConstants.CLOSED_VOUCHER;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.setResultClassList(Arrays.asList(VoucherModel.class, DateRestrictionModel.class));

		final SearchResult<List<Object>> result = flexibleSearchService.search(query);


		for (final List<Object> row : result.getResult())
		{
			final Map<VoucherModel, DateRestrictionModel> resultMap = new HashMap<VoucherModel, DateRestrictionModel>();
			final VoucherModel voucher = (VoucherModel) row.get(0);
			final DateRestrictionModel dateRestriction = (DateRestrictionModel) row.get(1);

			if (null != dateRestriction.getStartDate())
			{
				try
				{
					resultMap.put(voucher, dateRestriction);
					voucherWithStartDateMap.add(resultMap);
				}
				catch (final Exception e)
				{
					LOG.debug(e.getMessage());
				}
			}
		}
		return voucherWithStartDateMap;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.coupon.dao.MplCouponDao#findClosedVoucher(de.hybris.platform.core.model.user.CustomerModel,
	 * de.hybris.platform.commerceservices.search.pagedata.PageableData)
	 */

	@Override
	public SearchPageData<VoucherModel> findClosedVoucher(final CustomerModel customer, final PageableData pageableData)
	{

		try
		{

			final StringBuilder groupBiulder = new StringBuilder(200);

			final Set<PrincipalGroupModel> groups = customer.getGroups();
			for (final PrincipalGroupModel principal : groups)
			{
				groupBiulder.append(" OR {ur.users} like ('%").append(principal.getPk().getLongValue()).append("%') ");
			}

			final Map queryParams = new HashMap();
			queryParams.put("customerPk", customer);
			//queryParams.put("store",
			//store); //queryParams.put("type", "Parent"); //queryParams.put("creationtime", fromDate);

			final String CLOSED_VOUCHER_ = "select {v.pk} from {Promotionvoucher as v JOIN userrestriction as ur ON {v.pk}={ur.voucher} JOIN daterestriction as dr ON {v.pk}={dr.voucher}} where {dr.startdate} <= sysdate and sysdate<= {dr.enddate} "
					+ " AND ( {ur.users} like ('%"
					+ customer.getPk().getLongValue()
					+ "%')"
					+ groupBiulder.toString()
					//check voucher invalidation table
					+ " ) AND {v.redemptionQuantityLimitPerUser} >"
					+ " ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk} "
					+ " AND  {vin.user}='"
					+ customer.getPk().getLongValue()
					+ "' "
					+ " }})"
					+ " AND {v.redemptionQuantityLimit} >"
					+ " ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk}}})"
					+ " ORDER BY {dr.startdate} ASC";

			//System.out.println("Query :::::::::::::::" + CLOSED_VOUCHER_);
			final List sortQueries = Arrays.asList(new SortQueryData[]
			{ createSortQueryData("byDate", CLOSED_VOUCHER_
			//"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer AND {versionID} IS NULL AND {store} = ?store AND {type} = ?type AND {creationtime} >= ?creationtime ORDER BY {creationtime} DESC, {pk}"
			) });


			return pagedFlexibleSearchService.search(sortQueries, "byDate", queryParams, pageableData);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.coupon.dao.MplCouponDao#findClosedVoucher(de.hybris.platform.core.model.user.CustomerModel,
	 * de.hybris.platform.commerceservices.search.pagedata.PageableData)
	 */
	//	@Override
	//	public SearchPageData<VoucherModel> findClosedVoucher(final CustomerModel customer, final PageableData pageableData)
	//	{
	//		// YTODO Auto-generated method stub
	//		return null;
	//	}

}