/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplStyleProfileModel;
import com.tisl.mpl.core.model.MyRecommendationsBrandsModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MyStyleProfileDao;


/**
 * @author TCS
 *
 */
@Component(value = "myStyleProfileDao")
public class DefaultMyStyleProfileDaoImpl implements MyStyleProfileDao
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultMyStyleProfileDaoImpl.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private CategoryService categoryService;

	private final String SELECT_P = "SELECT {p:";
	private final String FROM_ = "FROM {";
	private final String AS_P = " AS p }";

	/**
	 * @Description : Fetch Data corresponding to gender
	 * @param: genderData
	 * @return
	 */
	@Override
	public List<MyRecommendationsConfigurationModel> fetchRecommendedData(final String genderData)
	{
		try
		{
			LOG.debug("Fetching My Recommendation Data");
			final String queryString = //
			SELECT_P + MyRecommendationsConfigurationModel.PK + "} "//
					+ FROM_ + MyRecommendationsConfigurationModel._TYPECODE + AS_P;

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			return flexibleSearchService.<MyRecommendationsConfigurationModel> search(query).getResult();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MyStyleProfileDao#fetchBrands(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<MyRecommendationsConfigurationModel> fetchBrands(final String genderData, final String catCode)
	{
		try
		{
			LOG.debug("Fetching My fetchBrands Data");
			final String queryString = //
			SELECT_P + MyRecommendationsConfigurationModel.PK + "} " + FROM_ + MyRecommendationsConfigurationModel._TYPECODE + AS_P
					+ " WHERE {p:" + MyRecommendationsConfigurationModel.GENDERDATA + "}=?genderData AND " + "{p:"
					+ MyRecommendationsConfigurationModel.CONFIGUREDCATEGORY + "}=?catCode";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("genderData", genderData);
			query.addQueryParameter("catCode", catCode);
			return flexibleSearchService.<MyRecommendationsConfigurationModel> search(query).getResult();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MyStyleProfileDao#fetchSubCatdOfBrands(java.lang.String)
	 */
	@Override
	public List<MyRecommendationsBrandsModel> fetchSubCatdOfBrands(final String catCode)
	{
		try
		{
			final CategoryModel catModel = categoryService.getCategoryForCode(catCode);

			final String queryString = //
			SELECT_P + MyRecommendationsBrandsModel.PK + "} " + FROM_ + MyRecommendationsBrandsModel._TYPECODE + AS_P + " WHERE {p:"
					+ MyRecommendationsBrandsModel.CONFIGUREDBRANDS + "}=?configBrand";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("configBrand", catModel.getPk().toString());
			return flexibleSearchService.<MyRecommendationsBrandsModel> search(query).getResult();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * @Description : Fetching preferred categories against device ID
	 * 
	 * @param: device ID
	 * 
	 * @return: List of MplStyleProfileModel
	 */
	@Override
	public List<MplStyleProfileModel> fetchCatBrandOfDevice(final String deviceId)
	{
		LOG.debug("Fetching fetchCatandBrandsOfDevice Data");
		try
		{
			final String queryString = SELECT_P + MplStyleProfileModel.PK + "} " + FROM_ + MplStyleProfileModel._TYPECODE + AS_P
					+ " WHERE {p:" + MplStyleProfileModel.DEVICEID + "}=?deviceId";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("deviceId", deviceId);
			return flexibleSearchService.<MplStyleProfileModel> search(query).getResult();
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MyStyleProfileDao#fetchBrandOfDevice(java.lang.String)
	 */
	@Override
	public List<MplStyleProfileModel> fetchBrandOfDevice(final String deviceId)
	{
		LOG.debug("Fetching fetchBrandOfDevice Data");
		try
		{
			final String queryString = SELECT_P + MplStyleProfileModel.PK + "} " + FROM_ + MplStyleProfileModel._TYPECODE + AS_P
					+ " WHERE {p:" + MplStyleProfileModel.DEVICEID + "}=?deviceId";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("deviceId", deviceId);
			return flexibleSearchService.<MplStyleProfileModel> search(query).getResult();
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}
}
