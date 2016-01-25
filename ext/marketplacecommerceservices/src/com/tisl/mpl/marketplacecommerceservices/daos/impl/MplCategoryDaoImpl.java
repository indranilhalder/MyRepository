/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.daos.impl.DefaultCategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.tisl.mpl.marketplacecommerceservices.daos.MplCategoryDao;


/**
 * @author TCS
 *
 */
public class MplCategoryDaoImpl extends DefaultCategoryDao implements MplCategoryDao
{

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplCatalogDao#getCategoryModelForName(de.hybris.platform.catalog
	 * .model.CatalogVersionModel, java.lang.String)
	 */

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Override
	public CategoryModel getCategoryModelForName(final CatalogVersionModel catalogVersion, final String catalogName)
	{
		//final StringBuilder categoryQuery = new StringBuilder("SELECT {cat." + CategoryModel.PK + "} "); SONAR Fixes

		CategoryModel categoryModel = null;

		final StringBuilder categoryQuery = new StringBuilder(70);
		categoryQuery.append("SELECT {cat." + CategoryModel.PK + "} ");
		categoryQuery.append("FROM {" + CategoryModel._TYPECODE + " AS cat} ");
		categoryQuery.append("WHERE {cat." + CategoryModel.NAME + "} = ?" + CategoryModel.NAME);
		categoryQuery.append(" AND {cat." + CategoryModel.CATALOGVERSION + "} = (?" + CategoryModel.CATALOGVERSION + ")");

		final Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(CategoryModel.CATALOGVERSION, catalogVersion);
		params.put(CategoryModel.NAME, catalogName);

		final SearchResult<CategoryModel> searchRes = getFlexibleSearchService().search(categoryQuery.toString(), params);
		if (searchRes != null && searchRes.getCount() > 0)
		{
			//return searchRes.getResult().get(0); SONAR Fixes
			categoryModel = searchRes.getResult().get(0);
		}

		//return null; SONAR Fixes
		return categoryModel;

	}

	@Override
	public CategoryModel getCategoryModelForCode(final CatalogVersionModel catalogVersion, final String catalogCode)
	{
		//final StringBuilder query = new StringBuilder("SELECT {cat." + CategoryModel.PK + "} "); SONAR Fixes
		CategoryModel categoryModel = null;

		final StringBuilder categoryCodeQuery = new StringBuilder(70);
		categoryCodeQuery.append("SELECT {cat." + CategoryModel.PK + "} ");
		categoryCodeQuery.append("FROM {" + CategoryModel._TYPECODE + " AS cat} ");
		categoryCodeQuery.append("WHERE {cat." + CategoryModel.CODE + "} = ?" + CategoryModel.CODE);
		categoryCodeQuery.append(" AND {cat." + CategoryModel.CATALOGVERSION + "} = (?" + CategoryModel.CATALOGVERSION + ")");

		final Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(CategoryModel.CATALOGVERSION, catalogVersion);
		params.put(CategoryModel.CODE, catalogCode);

		final SearchResult<CategoryModel> searchRes = getFlexibleSearchService().search(categoryCodeQuery.toString(), params);
		if (searchRes != null && searchRes.getCount() > 0)
		{
			// return searchRes.getResult().get(0); SONAR Fixes
			categoryModel = searchRes.getResult().get(0);
		}

		//return null; SONAR Fixes
		return categoryModel;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplCategoryDao#getMplRootCategoriesForCatalogVersion(de.hybris.
	 * platform .catalog.model.CatalogVersionModel)
	 */
	@Override
	public Collection<CategoryModel> getMplRootCategoriesForCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		// YTODO Auto-generated method stub
		//final StringBuilder query = new StringBuilder("SELECT {cat." + CategoryModel.PK + "} "); SONAR Fixes
		Collection<CategoryModel> categoryModelCollection = null;
		final StringBuilder rootCategoryQuery = new StringBuilder(50);
		rootCategoryQuery.append("SELECT {cat." + CategoryModel.PK + "} ");
		rootCategoryQuery.append("FROM {" + CategoryModel._TYPECODE + " AS cat} ");
		rootCategoryQuery.append("WHERE {cat." + CategoryModel.CATALOGVERSION + "} = ?" + CategoryModel.CATALOGVERSION);

		final Map<String, Object> params = new HashMap<String, Object>(2);
		params.put(CategoryModel.CATALOGVERSION, catalogVersion);
		final SearchResult<CategoryModel> searchRes = getFlexibleSearchService().search(rootCategoryQuery.toString(), params);
		if (searchRes != null && searchRes.getCount() > 0)
		{
			//return searchRes.getResult(); SONAR Fixes
			categoryModelCollection = searchRes.getResult();
		}
		//return null; SONAR Fixes
		return categoryModelCollection;
	}

	@Override
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	protected FlexibleSearchService getFlexibleSearchService()
	{
		return this.flexibleSearchService;
	}


}
