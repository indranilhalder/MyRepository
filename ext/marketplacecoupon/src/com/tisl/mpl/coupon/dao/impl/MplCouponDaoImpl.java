/**
 *
 */
package com.tisl.mpl.coupon.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.dao.MplCouponDao;


/**
 * @author 752131
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



}