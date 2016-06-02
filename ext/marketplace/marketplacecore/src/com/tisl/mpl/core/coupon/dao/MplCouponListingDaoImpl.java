/**
 *
 */
package com.tisl.mpl.core.coupon.dao;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class MplCouponListingDaoImpl implements MplCouponListingDao
{
	private static final Logger LOG = Logger.getLogger(MplCouponListingDaoImpl.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	public static final String VOUCHERWITHINDATEQUERY = "select {d.voucher} from {DateRestriction as d} where sysdate>={d.startdate} and sysdate<={d.enddate}";

	@Override
	public List<VoucherModel> findVoucher()
	{
		final String queryString = VOUCHERWITHINDATEQUERY;

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
	 * @see com.tisl.mpl.core.coupon.dao.impl.MplCouponListingDao#findClosedVoucher()
	 */
	@Override
	public Set<Map<VoucherModel, DateRestrictionModel>> findClosedVoucher()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.core.coupon.dao.MplCouponListingDao#findVoucherWithRestrictions()
	 */
	@Override
	public List<VoucherModel> findVoucherWithRestrictions(final ProductModel product)
	{
		final String queryString = "select {d.voucher} from {DateRestriction as d} " + "where sysdate >= {d.startdate}	"
				+ "and sysdate <= {d.enddate}	" + "and {d.voucher} IN ({{" + "select {pr.voucher}  from {ProductRestriction as pr} "
				+ " where {pr.products} like '%" + product.getPk() + "%'	}})";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		return getFlexibleSearchService().<VoucherModel> search(query).getResult();
	}




}