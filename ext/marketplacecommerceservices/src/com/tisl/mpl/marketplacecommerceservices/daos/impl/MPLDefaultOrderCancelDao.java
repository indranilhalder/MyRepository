/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.ordercancel.dao.impl.DefaultOrderCancelDao;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
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
	 * TISCR-410 : this method picks up the stage in which the order status is currently
	 *
	 * @param orderEntryStatus
	 * @return String
	 *
	 */
	@Override
	public String getOrderStatusStage(final String orderEntryStatus)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.ORDERSTAGEQUERY;
			LOG.debug("queryString: " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			return getFlexibleSearchService().<String> search(query).getResult().get(0);
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
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
