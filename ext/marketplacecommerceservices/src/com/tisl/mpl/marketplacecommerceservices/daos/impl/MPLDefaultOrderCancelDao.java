/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.ordercancel.dao.impl.DefaultOrderCancelDao;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.MPLOrderCancelDao;
import com.tisl.mpl.util.ExceptionUtil;



/**
 * @author TCS
 *
 */
public class MPLDefaultOrderCancelDao extends DefaultOrderCancelDao implements MPLOrderCancelDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MPLDefaultOrderCancelDao.class.getName());

	@Override
	public List<OrderCancelRecordEntryModel> getAllCancelled(final Date startDate) throws Exception
	{
		final Map<String, Object> params = new HashMap<>();
		//params.put("returnRequest", "");
		params.put("startDate", startDate);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					MarketplacecommerceservicesConstants.CANCELLED_REPORT_QUERY_START, params);
			return flexibleSearchService.<OrderCancelRecordEntryModel> search(query).getResult();
			/*
			 * if (result.getTotalCount() > 1)
			 * 
			 * { throw new EtailBusinessExceptions("Cancel  not found"); }
			 */
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			return null;
		}
	}


	@Override
	public List<OrderCancelRecordEntryModel> getAllCancelled(final Date startDate, final Date endDate) throws Exception
	{
		final Map<String, Object> params = new HashMap<>();
		//params.put("returnRequest", "");
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					MarketplacecommerceservicesConstants.CANCELLED_REPORT_QUERY_START_END, params);
			return flexibleSearchService.<OrderCancelRecordEntryModel> search(query).getResult();

		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			return null;
		}
	}

	@Override
	public List<OrderCancelRecordEntryModel> getAllCancelled() throws Exception
	{
		final Map<String, Object> params = new HashMap<>();
		//params.put("returnRequest", "");
		//params.put("startDate", startDate);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.CANCELLED_REPORT_QUERY,
					params);
			return flexibleSearchService.<OrderCancelRecordEntryModel> search(query).getResult();
			/*
			 * if (result.getTotalCount() > 1)
			 * 
			 * { throw new EtailBusinessExceptions("Cancel  not found"); }
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
