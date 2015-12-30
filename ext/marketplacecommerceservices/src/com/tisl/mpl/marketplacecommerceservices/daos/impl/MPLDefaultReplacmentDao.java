/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.returns.dao.impl.DefaultReplacementOrderDao;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.MPLReplacementDao;
import com.tisl.mpl.util.ExceptionUtil;



/**
 * @author 890223
 *
 */
public class MPLDefaultReplacmentDao extends DefaultReplacementOrderDao implements MPLReplacementDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MPLDefaultReplacmentDao.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MPLRefundDao#getAllRefunds()
	 */
	@Override
	public List<ReplacementEntryModel> getAllReplacement(final Date startDate) throws Exception
	{
		final Map<String, Object> params = new HashMap<>();
		//params.put("returnRequest", "");
		params.put("startDate", startDate);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					MarketplacecommerceservicesConstants.REPLACE_REPORT_QUERY_START, params);
			return flexibleSearchService.<ReplacementEntryModel> search(query).getResult();
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
	public List<ReplacementEntryModel> getAllReplacement(final Date startDate, final Date endDate) throws Exception
	{
		final Map<String, Object> params = new HashMap<>();
		//params.put("returnRequest", "");
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					MarketplacecommerceservicesConstants.REPLACE_REPORT_QUERY_START_END, params);
			return flexibleSearchService.<ReplacementEntryModel> search(query).getResult();
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
