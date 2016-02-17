/**
 *
 */
package com.tisl.mpl.facade.account.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.service.MyStyleProfileService;


/**
 * @author TCS
 *
 */
@UnitTest
public class DefaultMyStyleProfileFacadeImplUnitTest
{
	private MyStyleProfileService myStyleProfileService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private CategoryService categoryService;

	private static final Logger LOG = Logger.getLogger(DefaultMyStyleProfileFacadeImplUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		//this.defaultMyStyleProfileFacadeImpl = new DefaultMyStyleProfileFacadeImpl();

		this.myStyleProfileService = Mockito.mock(MyStyleProfileService.class);
		//this.defaultMyStyleProfileFacadeImpl.setMyStyleProfileService(myStyleProfileService);

		//this.productService = Mockito.mock(ProductService.class);
		this.categoryService = Mockito.mock(CategoryService.class);
		this.modelService = Mockito.mock(ModelService.class);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSaveCategoryData()
	{
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		final List<CategoryModel> categoryList = Arrays.asList(categoryModel);

		final List<String> categoryCodeList = new ArrayList<String>();
		categoryCodeList.add("MSH1");
		final String categoryCode = categoryCodeList.get(0);

		final CategoryModel category = new CategoryModel();
		Mockito.when(categoryService.getCategory(getDefaultPromotionsManager().catalogData(), categoryCode)).thenReturn(category);
		categoryList.add(category);

		Mockito.doNothing().when(myStyleProfileService).saveCategoryData(categoryList);
		LOG.info("Method : testSaveCategoryData >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchCategoryData()
	{
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		final List<CategoryModel> categoryList = Arrays.asList(categoryModel);
		final List<String> categoryCodeList = new ArrayList<String>();
		categoryCodeList.add("MSH1");
		final String categoryCode = categoryCodeList.get(0);

		final CategoryModel category = new CategoryModel();
		Mockito.when(categoryService.getCategory(getDefaultPromotionsManager().catalogData(), categoryCode)).thenReturn(category);
		categoryList.add(category);

		LOG.info("Method : testFetchCategoryData >>>>>>>");
	}

	@Test
	public void testGetInterstedCategories()
	{
		Mockito.doNothing().when(myStyleProfileService).getInterstedCategories();
		LOG.info("Method : testGetInterstedCategories >>>>>>>");
	}

	@Test
	public void testIsStyleProfileCreated()
	{
		Mockito.doNothing().when(myStyleProfileService).isStyleProfileCreated();
		LOG.info("Method : testIsStyleProfileCreated >>>>>>>");
	}

	@Test
	public void testFetchRecommendedData()
	{
		final MyRecommendationsConfigurationModel myModel = modelService.create(MyRecommendationsConfigurationModel.class);
		final List<MyRecommendationsConfigurationModel> myModelList = Arrays.asList(myModel);
		final String genderData = "MALE";
		Mockito.when(myStyleProfileService.fetchRecommendedData(genderData)).thenReturn(myModelList);
		LOG.info("Method : testFetchRecommendedData >>>>>>>");
	}

	@Test
	public void testFetchBrands()
	{
		final MyRecommendationsConfigurationModel myModel = modelService.create(MyRecommendationsConfigurationModel.class);
		final List<MyRecommendationsConfigurationModel> myModelList = Arrays.asList(myModel);
		final String genderData = "MALE";
		final String catCode = "MSH1";
		Mockito.when(myStyleProfileService.fetchBrands(genderData, catCode)).thenReturn(myModelList);
		LOG.info("Method : testFetchBrands >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSaveBrandData()
	{
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		final List<CategoryModel> categoryList = Arrays.asList(categoryModel);

		final List<String> categoryCodeList = new ArrayList<String>();
		categoryCodeList.add("MSH1");
		final String categoryCode = categoryCodeList.get(0);

		final CategoryModel category = new CategoryModel();
		Mockito.when(categoryService.getCategory(getDefaultPromotionsManager().catalogData(), categoryCode)).thenReturn(category);
		categoryList.add(category);

		Mockito.doNothing().when(myStyleProfileService).saveBrandData(categoryList);
		LOG.info("Method : testSaveBrandData >>>>>>>");
	}


	@Test
	public void testSaveGenderData()
	{
		final String genderData = "MALE";
		Mockito.doNothing().when(myStyleProfileService).saveGenderData(genderData);
		LOG.info("Method : testSaveGenderData >>>>>>>");
	}

	@Test
	public void testFetchGenderData()
	{
		Mockito.doNothing().when(myStyleProfileService).fetchGenderData();
		LOG.info("Method : testFetchGenderData >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSubCategoryData()
	{
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		final List<CategoryModel> categoryList = Arrays.asList(categoryModel);

		final List<String> categoryCodeList = new ArrayList<String>();
		categoryCodeList.add("MSH1");
		final String categoryCode = categoryCodeList.get(0);

		final CategoryModel category = new CategoryModel();
		Mockito.when(categoryService.getCategory(getDefaultPromotionsManager().catalogData(), categoryCode)).thenReturn(category);
		categoryList.add(category);

		Mockito.doNothing().when(myStyleProfileService).saveSubCategoryData(categoryList);
		LOG.info("Method : testSubCategoryData >>>>>>>");
	}

	@Test
	public void testRemoveMyStyleProfile()
	{
		Mockito.doNothing().when(myStyleProfileService).removeMyStyleProfile();
		LOG.info("Method : testRemoveMyStyleProfile >>>>>>>");
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveSingleBrand()
	{
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		final List<CategoryModel> categoryList = Arrays.asList(categoryModel);

		final List<String> categoryCodeList = new ArrayList<String>();
		categoryCodeList.add("MBH1");
		final String categoryCode = categoryCodeList.get(0);

		final CategoryModel category = new CategoryModel();
		Mockito.when(categoryService.getCategory(getDefaultPromotionsManager().catalogData(), categoryCode)).thenReturn(category);
		categoryList.add(category);

		Mockito.doNothing().when(myStyleProfileService).removeSingleBrand(categoryList);
		LOG.info("Method : testSubCategoryData >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveSingleCategory()
	{
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		final List<CategoryModel> categoryList = Arrays.asList(categoryModel);

		final List<String> categoryCodeList = new ArrayList<String>();
		categoryCodeList.add("MSH1");
		final String categoryCode = categoryCodeList.get(0);

		final CategoryModel category = new CategoryModel();
		Mockito.when(categoryService.getCategory(getDefaultPromotionsManager().catalogData(), categoryCode)).thenReturn(category);
		categoryList.add(category);

		final CategoryModel brandModel = modelService.create(CategoryModel.class);
		final List<CategoryModel> brandList = Arrays.asList(brandModel);

		final List<String> brandCodeList = new ArrayList<String>();
		brandCodeList.add("MBH1");
		final String brandCode = brandCodeList.get(0);

		final CategoryModel brand = new CategoryModel();
		Mockito.when(categoryService.getCategory(getDefaultPromotionsManager().catalogData(), brandCode)).thenReturn(brand);
		brandList.add(brand);


		Mockito.doNothing().when(myStyleProfileService).removeSingleCategory(categoryList, brandList);
		LOG.info("Method : testRemoveSingleCategory >>>>>>>");
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}
}
