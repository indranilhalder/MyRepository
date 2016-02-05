/**
 *
 */
package com.tisl.mpl.coupon.dao.impl;

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

import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.dao.MplCouponDao;


/**
 * @author TCS
 *
 */
public class MplCouponDaoImpl implements MplCouponDao
{
	private static final Logger LOG = Logger.getLogger(MplCouponDaoImpl.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;

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



}