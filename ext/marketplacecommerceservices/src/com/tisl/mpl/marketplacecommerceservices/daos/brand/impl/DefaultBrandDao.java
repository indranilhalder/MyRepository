/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.brand.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.brand.BrandDao;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;


/**
 * @author TCS
 *
 */
public class DefaultBrandDao extends AbstractItemDao implements BrandDao
{

	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;

	@Override
	protected ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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

	/**
	 * @description It is used for fetching all distinct brands of online catalog version
	 * @param catalogVersion
	 * @return list of brands
	 */
	@Override
	public List<String> getAllBrandListForCurrentCatalogVersion(final CatalogVersionModel catalogVersion)
			throws EtailNonBusinessExceptions
	{
		//		validateParameterNotNull(catalogVersion, "catalogVersion must not be null!");
		//		final FlexibleSearchQuery query = new FlexibleSearchQuery( //
		//				"SELECT DISTINCT {" + ProductModel.BRANDNAME + "} as brand " //
		//						+ "FROM {" + ProductModel._TYPECODE + "} " //
		//						+ "WHERE {" + ProductModel.CATALOGVERSION + "}=?catalogVersion");
		//
		//
		//		final Map<String, Object> params = (Map) Collections.singletonMap(CategoryModel.CATALOGVERSION, catalogVersion);
		//		query.addQueryParameters(params);
		//		query.setResultClassList(Collections.singletonList(String.class));
		//		final List<String> products = getFlexibleSearchService().<String> search(query).getResult();

		//return products;

		return null;

	}


	/**
	 * @description This method checks whether the email id exists or not
	 * @param emailId
	 * @return boolean value
	 */
	@Override
	public boolean checkEmailId(final String emailId)
	{

		if (null != emailId && !emailId.isEmpty())
		{
			final String queryString = //
			"SELECT {p:" + MplNewsLetterSubscriptionModel.PK + "}" //
					+ "FROM {" + MplNewsLetterSubscriptionModel._TYPECODE + " AS p} "//
					+ "WHERE " + "{p:" + MplNewsLetterSubscriptionModel.EMAILID + "}=?emailId ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("emailId", emailId);
			if ((flexibleSearchService.<MplNewsLetterSubscriptionModel> search(query).getResult().isEmpty()))
			{
				return true;
			}

		}
		return false;

	}

	@Override
	public List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(final String emailId)
	{

		if (null != emailId && !emailId.isEmpty())
		{
			final String queryString = //
			"SELECT {p:" + MplNewsLetterSubscriptionModel.PK + "}" //
					+ "FROM {" + MplNewsLetterSubscriptionModel._TYPECODE + " AS p} "//
					+ "WHERE " + "{p:" + MplNewsLetterSubscriptionModel.EMAILID + "}=?emailId "//
					+ "AND " + "{p:" + MplNewsLetterSubscriptionModel.ISLUXURY + "}IS NULL OR"//
					+ "{p:" + MplNewsLetterSubscriptionModel.ISLUXURY + "} = ?isluxury";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("emailId", emailId);
			query.addQueryParameter("isluxury", MarketplacecommerceservicesConstants.IS_LUXURY);
			/*
			 * if ((flexibleSearchService.<MplNewsLetterSubscriptionModel> search(query).getResult().isEmpty())) { return
			 * false; }
			 */
			return flexibleSearchService.<MplNewsLetterSubscriptionModel> search(query).getResult();
		}
		//return true;
		return null;
	}

	@Override
	public List<MplNewsLetterSubscriptionModel> checkEmailIdForMarketplace(final String emailId)
	{
		if (null != emailId && !emailId.isEmpty())
		{
			final String queryString = //
			"SELECT {p:" + MplNewsLetterSubscriptionModel.PK + "}" //
					+ "FROM {" + MplNewsLetterSubscriptionModel._TYPECODE + " AS p} "//
					+ "WHERE " + "{p:" + MplNewsLetterSubscriptionModel.EMAILID + "}=?emailId "//
					+ "AND " + "{p:" + MplNewsLetterSubscriptionModel.ISMARKETPLACE + "}IS NULL OR"//
					+ "{p:" + MplNewsLetterSubscriptionModel.ISMARKETPLACE + "} = ?ismarketplace";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("emailId", emailId);
			query.addQueryParameter("ismarketplace", MarketplacecommerceservicesConstants.IS_MARKETPLACE);
			/*
			 * if ((flexibleSearchService.<MplNewsLetterSubscriptionModel> search(query).getResult().isEmpty())) { return
			 * false; }
			 */
			return flexibleSearchService.<MplNewsLetterSubscriptionModel> search(query).getResult();
		}
		//return true;
		return null;
	}
}
