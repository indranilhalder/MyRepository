package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCategoryDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplCategoryService;


/**
 * @author TCS
 *
 */
public class MplCategoryServiceImpl extends DefaultCategoryService implements MplCategoryService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplCatalogService#getCategoryModelForName(de.hybris.platform.
	 * catalog.model.CatalogVersionModel, java.lang.String)
	 */


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	private MplCategoryDao mplCategoryDao;

	/**
	 * @param catModel
	 * @param catalogName
	 * @return CategoryModel
	 */
	@Override
	public CategoryModel getCategoryModelForName(final CatalogVersionModel catModel, final String catalogName)
	{
		final CategoryModel categoryModel = mplCategoryDao.getCategoryModelForName(catModel, catalogName);
		if (categoryModel != null)
		{
			return categoryModel;
		}
		return null;

	}

	/**
	 * @param catModel
	 * @param catalogCode
	 * @return CategoryModel
	 */
	@Override
	public CategoryModel getCategoryModelForCode(final CatalogVersionModel catModel, final String catalogName)
	{
		final CategoryModel categoryModel = mplCategoryDao.getCategoryModelForCode(catModel, catalogName);
		if (categoryModel != null)
		{
			return categoryModel;
		}
		return null;
	}


	/**
	 * @param categoryData
	 * @return CategoryModel
	 */
	@Override
	public CategoryModel getMatchingCategory(final String categoryData)
	{

		// YTODO Auto-generated method stub
		final List<CategoryModel> matchedCategoryList = new ArrayList<CategoryModel>();
		final String mplrootCategory = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.ROOTCATEGORY, "");
		final CategoryModel rootCategory = categoryService.getCategoryForCode(mplrootCategory);


		if (rootCategory.getSupercategories() == null || rootCategory.getSupercategories().isEmpty())
		{
			final Collection<CategoryModel> subCategoryList = rootCategory.getAllSubcategories();
			for (final CategoryModel category : subCategoryList)
			{
				if (category != null && category.getName() != null && categoryData != null
						&& category.getName().toLowerCase(Locale.ENGLISH).startsWith(categoryData.toLowerCase(Locale.ENGLISH)))
				{

					matchedCategoryList.add(category);

				}
			}
		}
		int maxSize = 0;
		int index = 0;
		for (int i = 0; i < matchedCategoryList.size(); i++)
		{

			final CategoryModel category = matchedCategoryList.get(i);
			final int intraMaxSize = category.getAllSubcategories().size();
			if (intraMaxSize > maxSize)
			{
				maxSize = intraMaxSize;
				index = i;
			}
		}
		CategoryModel maxCategory = null;
		if (!matchedCategoryList.isEmpty())
		{
			maxCategory = matchedCategoryList.get(index);

		}
		return maxCategory;


	}

	/**
	 * @param catalogVersion
	 * @return Collection<CategoryModel>
	 */
	@Override
	public Collection<CategoryModel> getMplRootCategoriesForCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		// YTODO Auto-generated method stub
		final Collection<CategoryModel> categoryModel = mplCategoryDao.getMplRootCategoriesForCatalogVersion(catalogVersion);
		if (categoryModel != null)
		{
			return categoryModel;
		}
		return null;
	}

	/**
	 * @return the mplCategoryDao
	 */
	public MplCategoryDao getMplCategoryDao()
	{
		return mplCategoryDao;
	}


	/**
	 * @param mplCategoryDao
	 *           the mplCategoryDao to set
	 */
	@Required
	public void setMplCategoryDao(final MplCategoryDao mplCategoryDao)
	{
		this.mplCategoryDao = mplCategoryDao;
	}


}
