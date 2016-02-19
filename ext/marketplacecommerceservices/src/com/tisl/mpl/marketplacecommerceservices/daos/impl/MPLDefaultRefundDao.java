/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.ordercancel.exceptions.AmbiguousOrderCancelConfigurationException;
import de.hybris.platform.refund.dao.impl.DefaultRefundDao;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderRefundConfigModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MPLRefundDao;
import com.tisl.mpl.util.ExceptionUtil;



/**
 * @author 890223
 *
 */
public class MPLDefaultRefundDao extends DefaultRefundDao implements MPLRefundDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MPLDefaultRefundDao.class.getName());

	@Override
	public OrderRefundConfigModel getOrderRefundConfiguration() throws Exception

	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {" + OrderRefundConfigModel._TYPECODE + "}");

		final SearchResult result = (SearchResult) flexibleSearchService.search(query);
		if (result.getTotalCount() > 1)

		{
			throw new AmbiguousOrderCancelConfigurationException("Only one Order Refund Configuration is Allowed");
		}
		return ((result.getTotalCount() == 0) ? null : (OrderRefundConfigModel) result.getResult().get(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MPLRefundDao#getAllRefunds()
	 */
	@Override
	public List<RefundEntryModel> getAllRefunds(final Date startDate) throws Exception
	{
		final Map<String, Object> params = new HashMap<>();
		//params.put("returnRequest", "");
		params.put("startDate", startDate);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					MarketplacecommerceservicesConstants.REFUND_REPORT_QUERY_START, params);
			return flexibleSearchService.<RefundEntryModel> search(query).getResult();
			/*
			 * if (result.getTotalCount() > 1)
			 *
			 * { throw new EtailBusinessExceptions("Refund  is Allowed"); }
			 */
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MPLRefundDao#getAllRefunds()
	 */
	@Override
	public List<RefundEntryModel> getAllRefunds() throws Exception
	{
		final Map<String, Object> params = new HashMap<>();
		//params.put("returnRequest", "");
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.REFUND_REPORT_QUERY,
					params);
			return flexibleSearchService.<RefundEntryModel> search(query).getResult();
			/*
			 * if (result.getTotalCount() > 1)
			 *
			 * { throw new EtailBusinessExceptions("Refund  is Allowed"); }
			 */
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MPLRefundDao#getAllRefunds()
	 */
	@Override
	public List<RefundEntryModel> getAllRefunds(final Date startDate, final Date endDate) throws Exception
	{
		final Map<String, Object> params = new HashMap<>();
		//params.put("returnRequest", "");
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					MarketplacecommerceservicesConstants.REFUND_REPORT_QUERY_START_END, params);
			return flexibleSearchService.<RefundEntryModel> search(query).getResult();
			/*
			 * if (result.getTotalCount() > 1)
			 *
			 * { throw new EtailBusinessExceptions("Refund  is Allowed"); }
			 */
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			return null;
		}
	}

	/**
	 * @return the flexibleSearchService
	 */
	@Override
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	@Override
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


}
