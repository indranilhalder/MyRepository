/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.GigyaDataPullDao;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class GigyaDataPullDaoImpl implements GigyaDataPullDao
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(GigyaDataPullDaoImpl.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.GigyaDataPullDao#fetchConfigDetails(java.lang.String)
	 */
	@Override
	public MplConfigurationModel fetchConfigDetails(final String code)
	{
		// YTODO Auto-generated method stub
		final String queryString = //
		"SELECT {p:" + MplConfigurationModel.PK
				+ "} "//
				+ "FROM {" + MplConfigurationModel._TYPECODE + " AS p } where" + "{p." + MplConfigurationModel.MPLCONFIGCODE
				+ "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);
		return flexibleSearchService.<MplConfigurationModel> searchUnique(query);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.GigyaDataPullDao#fetchProductDetails()
	 */
	@Override
	public Map<String, Date> fetchProductDetails()
	{
		final Map<String, Date> resultMap = new LinkedHashMap<String, Date>();
		try
		{
			final String queryString = configurationService.getConfiguration().getString("gigya.query.firstCall");
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.setResultClassList(Arrays.asList(Date.class, String.class, String.class));
			final SearchResult<List<Object>> result = flexibleSearchService.search(query);
			for (final List<Object> row : result.getResult())
			{
				final Date date = (Date) row.get(0);
				final String code = (String) row.get(1);
				final String categoryType = (String) row.get(2);
				final String codeCategory = code + ":" + categoryType;
				resultMap.put(codeCategory, date);
			}
		}
		catch (final Exception e)
		{
			LOG.debug(e.getMessage());
		}

		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.GigyaDataPullDao#specificCustomerDetails(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public Map<String, Date> specificProductDetails(final Date lastFetchedDate)
	{
		final Map<String, Date> resultMap = new LinkedHashMap<String, Date>();
		try
		{
			final String queryString = configurationService.getConfiguration().getString("gigya.query.Call");
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("lastFetchedDate", lastFetchedDate);
			query.setResultClassList(Arrays.asList(Date.class, String.class, String.class));
			final SearchResult<List<Object>> result = flexibleSearchService.search(query);
			for (final List<Object> row : result.getResult())
			{
				final Date date = (Date) row.get(0);
				final String code = (String) row.get(1);
				final String categoryType = (String) row.get(2);
				final String codeCategory = code + ":" + categoryType;
				resultMap.put(codeCategory, date);
			}
		}
		catch (final Exception e)
		{
			LOG.debug(e.getMessage());
		}

		return resultMap;
	}

}
