package com.tisl.mpl.marketplacecommerceservices.service.impl;

/**
 * @author 594407
 *
 */
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.marketplacecommerceservices.daos.MplCategoryDao;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MplCategoryServiceImplTest
{
	@Mock
	private MplCategoryServiceImpl mplCategoryServiceImpl;

	@Mock
	private MplCategoryDao mplCategoryDao;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private CategoryService categoryService;
	@Mock
	private CategoryModel categoryModel;
	//@Mock
	//private DefaultCategoryService defaultCategoryService;
	@Mock
	private Configuration configuration;
	@Mock
	private CatalogVersionModel catalogVersionModel;

	public void setUp()
	{
		mplCategoryServiceImpl = new MplCategoryServiceImpl();

		mplCategoryServiceImpl.setMplCategoryDao(mplCategoryDao);

	}

	@Test
	public void testgetMatchingCategory()
	{

		Collection<CategoryModel> matchedCategoryList = Arrays.asList(categoryModel);
		final String mplrootCategory = "MPH1";
		final String clothingUpper = "Clothing";
		final String categoryData = "Clothing";
		final Collection<CategoryModel> intraMaxSize = null;
		//int intraMaxSize=0;
		given(configurationService.getConfiguration()).willReturn(configuration);
		given(configuration.getString("marketplace.mplcatalog.rootcategory.code", "")).willReturn(mplrootCategory);
		given(categoryService.getCategoryForCode(mplrootCategory)).willReturn(categoryModel);
		matchedCategoryList = categoryModel.getAllSubcategories();
		matchedCategoryList.add(categoryModel);
		given(categoryModel.getName()).willReturn(clothingUpper);
		matchedCategoryList.add(categoryModel);
		given(categoryModel.getAllSubcategories()).willReturn(intraMaxSize);
		matchedCategoryList.add(categoryModel);
		mplCategoryServiceImpl.getMatchingCategory(categoryData);

	}

	@Test
	public void testgetMplRootCategoriesForCatalogVersion()
	{
		final Collection<CategoryModel> categoryModelList = Arrays.asList(categoryModel);
		given(mplCategoryDao.getMplRootCategoriesForCatalogVersion(catalogVersionModel)).willReturn(categoryModelList);
		mplCategoryServiceImpl.getMplRootCategoriesForCatalogVersion(catalogVersionModel);
		
	}
}
