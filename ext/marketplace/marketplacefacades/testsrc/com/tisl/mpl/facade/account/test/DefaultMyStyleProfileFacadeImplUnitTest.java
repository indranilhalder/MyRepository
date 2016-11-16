/**
 *
 */
package com.tisl.mpl.facade.account.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MyRecommendationsBrandsModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.facade.mystyleprofile.impl.DefaultMyStyleProfileFacadeImpl;
import com.tisl.mpl.facades.product.data.MyStyleProfileData;
import com.tisl.mpl.marketplacecommerceservices.service.MyStyleProfileService;


/**
 * @author TCS
 *
 */
@UnitTest
public class DefaultMyStyleProfileFacadeImplUnitTest
{
	@Mock
	private MyStyleProfileService myStyleProfileService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private CategoryService categoryService;
	@Mock
	private CategoryModel categoryModel;
	@Mock
	private MyStyleProfileData myStyleProfileData;
	@Mock
	private DefaultMyStyleProfileFacadeImpl defaultMyStyleProfileFacadeImpl;
	@Mock
	private MyRecommendationsConfigurationModel myRecommendationsConfigurationModel;
	CatalogVersionModel version;

	private static final Logger LOG = Logger.getLogger(DefaultMyStyleProfileFacadeImplUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.myStyleProfileService = Mockito.mock(MyStyleProfileService.class);
		//this.defaultMyStyleProfileFacadeImpl.setMyStyleProfileService(myStyleProfileService);
		this.categoryService = Mockito.mock(CategoryService.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.version = Mockito.mock(CatalogVersionModel.class);
		this.categoryModel = Mockito.mock(CategoryModel.class);
		this.myStyleProfileData = Mockito.mock(MyStyleProfileData.class);
		this.defaultMyStyleProfileFacadeImpl = Mockito.mock(DefaultMyStyleProfileFacadeImpl.class);
		this.myRecommendationsConfigurationModel = Mockito.mock(MyRecommendationsConfigurationModel.class);
	}

	@SuppressWarnings("deprecation")
	private CatalogVersionModel getCatalogData()
	{
		final CatalogModel catalogModel = Mockito.mock(CatalogModel.class);
		final CatalogVersionModel mockCatalogVersion = Mockito.mock(CatalogVersionModel.class);
		BDDMockito.when(mockCatalogVersion.getVersion()).thenReturn("Online");
		BDDMockito.when(catalogModel.getCatalogVersions()).thenReturn(Sets.newHashSet(mockCatalogVersion));
		return mockCatalogVersion;

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSaveCategoryData()
	{
		final String categoryCode = "MSH10";
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		//TISSEC-50
		//		categoryCodeList.add("MSH10");//TODO : Please enter category code
		Mockito.when(myStyleProfileData.getCategoryCodeList()).thenReturn(categoryCodeList);
		Mockito.when(categoryService.getCategory(getCatalogData(), categoryCode)).thenReturn(categoryModel);
		categoryList.add(categoryModel);

		Mockito.doNothing().when(myStyleProfileService).saveCategoryData(categoryList);
		defaultMyStyleProfileFacadeImpl.saveCategoryData(myStyleProfileData);
		LOG.info("Method : testSaveCategoryData >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchCategoryData()
	{
		final String categoryCode = "MSH10";
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		//		final List<String> categoryCodeList = new ArrayList<String>();
		Mockito.when(categoryService.getCategory(getCatalogData(), categoryCode)).thenReturn(categoryModel);
		categoryList.add(categoryModel);
		LOG.info("Method : testFetchCategoryData >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetInterstedCategories()
	{
		final List<CategoryModel> list = new ArrayList<CategoryModel>();
		Mockito.when(myStyleProfileService.getInterstedCategories()).thenReturn(list);
		final List<CategoryModel> listTest = defaultMyStyleProfileFacadeImpl.getInterstedCategories();
		Assert.assertEquals(list, listTest);
		LOG.info("Method : testGetInterstedCategories >>>>>>>");
	}

	@Test
	public void testIsStyleProfileCreated()
	{
		Mockito.when(Boolean.valueOf(myStyleProfileService.isStyleProfileCreated())).thenReturn(Boolean.valueOf(true));
		defaultMyStyleProfileFacadeImpl.isStyleProfileCreated();
		LOG.info("Method : testIsStyleProfileCreated >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchRecommendedData()
	{
		final List<MyRecommendationsConfigurationModel> myModelList = new ArrayList<MyRecommendationsConfigurationModel>();
		final String genderData = MarketplacecommerceservicesConstants.EMPTY;//TODO: Please enter gender data
		Mockito.when(myStyleProfileService.fetchRecommendedData(genderData)).thenReturn(myModelList);
		final List<MyRecommendationsConfigurationModel> myModelListTest = defaultMyStyleProfileFacadeImpl
				.fetchRecommendedData(genderData);
		Assert.assertEquals(myModelList, myModelListTest);
		LOG.info("Method : testFetchRecommendedData >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchBrands()
	{
		final List<MyRecommendationsConfigurationModel> myModelList = new ArrayList<MyRecommendationsConfigurationModel>();
		final String genderData = MarketplacecommerceservicesConstants.EMPTY;//TODO: Please enter gender data
		final String catCode = "MSH10";//TODO : Please enter category code
		Mockito.when(myStyleProfileService.fetchBrands(genderData, catCode)).thenReturn(myModelList);
		final List<MyRecommendationsConfigurationModel> myModelListTest = defaultMyStyleProfileFacadeImpl.fetchBrands(genderData,
				catCode);
		Assert.assertEquals(myModelList, myModelListTest);
		LOG.info("Method : testFetchBrands >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSaveBrandData()
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final String categoryCode = "MSH10";
		Mockito.when(myStyleProfileData.getCategoryCodeList()).thenReturn(categoryCodeList);
		Mockito.when(categoryService.getCategory(getCatalogData(), categoryCode)).thenReturn(categoryModel);
		categoryList.add(categoryModel);

		Mockito.doNothing().when(myStyleProfileService).saveBrandData(categoryList);
		defaultMyStyleProfileFacadeImpl.saveBrandData(myStyleProfileData);
		LOG.info("Method : testSaveBrandData >>>>>>>");
	}


	@Test
	public void testSaveGenderData()
	{
		final String genderData = MarketplacecommerceservicesConstants.EMPTY;//TODO: Please enter gender data
		Mockito.doNothing().when(myStyleProfileService).saveGenderData(genderData);
		defaultMyStyleProfileFacadeImpl.saveGenderData(genderData);
		LOG.info("Method : testSaveGenderData >>>>>>>");
	}

	@Test
	public void testFetchGenderData()
	{
		final String genderData = MarketplacecommerceservicesConstants.EMPTY;
		Mockito.when(myStyleProfileService.fetchGenderData()).thenReturn(genderData);
		defaultMyStyleProfileFacadeImpl.fetchGenderData();
		LOG.info("Method : testFetchGenderData >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSaveSubCategoryData()
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final String categoryCode = "MSH10";
		Mockito.when(myStyleProfileData.getCategoryCodeList()).thenReturn(categoryCodeList);
		Mockito.when(categoryService.getCategory(getCatalogData(), categoryCode)).thenReturn(categoryModel);
		categoryList.add(categoryModel);
		Mockito.doNothing().when(myStyleProfileService).saveSubCategoryData(categoryList);
		defaultMyStyleProfileFacadeImpl.saveSubCategoryData(myStyleProfileData);
		LOG.info("Method : testSubCategoryData >>>>>>>");
	}

	@Test
	public void testRemoveMyStyleProfile()
	{
		final boolean testTrue = true;
		Mockito.when(Boolean.valueOf(myStyleProfileService.removeMyStyleProfile())).thenReturn(Boolean.valueOf(testTrue));
		defaultMyStyleProfileFacadeImpl.removeMyStyleProfile();
		LOG.info("Method : testRemoveMyStyleProfile >>>>>>>");
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveSingleBrand()
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final String categoryCode = "MSH10";
		Mockito.when(myStyleProfileData.getCategoryCodeList()).thenReturn(categoryCodeList);
		Mockito.when(categoryService.getCategory(getCatalogData(), categoryCode)).thenReturn(categoryModel);
		categoryList.add(categoryModel);
		Mockito.doNothing().when(myStyleProfileService).removeSingleBrand(categoryList);
		defaultMyStyleProfileFacadeImpl.removeSingleBrand(myStyleProfileData);
		LOG.info("Method : testSubCategoryData >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveSingleCategory()
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final String categoryCode = "MSH10";
		Mockito.when(myStyleProfileData.getCategoryCodeList()).thenReturn(categoryCodeList);
		Mockito.when(categoryService.getCategory(getCatalogData(), categoryCode)).thenReturn(categoryModel);
		categoryList.add(categoryModel);
		final List<String> brandCodeList = new ArrayList<String>();
		brandCodeList.add("MSH10");//TODO : Please enter category code
		final String brandCode = brandCodeList.get(0);

		Mockito.when(categoryService.getCategory(getCatalogData(), brandCode)).thenReturn(categoryModel);
		categoryList.add(categoryModel);
		Mockito.doNothing().when(myStyleProfileService).removeSingleCategory(categoryList, categoryList);
		defaultMyStyleProfileFacadeImpl.removeSingleCategory(myStyleProfileData, myStyleProfileData);
		LOG.info("Method : testRemoveSingleCategory >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchSubCategories()
	{
		final String genderData = "";
		final String catCode = "MSH10";
		final List<CategoryModel> catlist = new ArrayList<CategoryModel>();
		Mockito.when(myStyleProfileService.fetchSubCategories(genderData, catCode)).thenReturn(catlist);
		final List<CategoryModel> catlistTest = defaultMyStyleProfileFacadeImpl.fetchSubCategories(genderData, catCode);
		Assert.assertEquals(catlist, catlistTest);
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testGetInterstedBrands()
	{
		final List<CategoryModel> brandList = new ArrayList<CategoryModel>();
		Mockito.when(myStyleProfileService.getInterstedBrands()).thenReturn(brandList);
		final List<CategoryModel> brandListTest = defaultMyStyleProfileFacadeImpl.getInterstedBrands();
		Assert.assertEquals(brandList, brandListTest);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchSubCatdOfBrands()
	{
		final String catCode = "MSH10";
		final List<MyRecommendationsBrandsModel> recommendationsBrands = new ArrayList<MyRecommendationsBrandsModel>();
		Mockito.when(myStyleProfileService.fetchSubCatdOfBrands(catCode)).thenReturn(recommendationsBrands);
		final List<MyRecommendationsBrandsModel> recommendationsBrandsTest = defaultMyStyleProfileFacadeImpl
				.fetchSubCatdOfBrands(catCode);
		Assert.assertEquals(recommendationsBrands, recommendationsBrandsTest);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testModifyBrand()
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final String categoryCode = "MSH10";
		Mockito.when(myStyleProfileData.getCategoryCodeList()).thenReturn(categoryCodeList);
		Mockito.when(categoryService.getCategory(getCatalogData(), categoryCode)).thenReturn(categoryModel);
		categoryList.add(categoryModel);
		Mockito.doNothing().when(myStyleProfileService).modifyBrand(categoryList);
		defaultMyStyleProfileFacadeImpl.modifyBrand(myStyleProfileData);
		LOG.info("Method : testSubCategoryData >>>>>>>");
	}

	@Test
	public void tesModifyCategory()
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final String categoryCode = "MSH10";
		Mockito.when(myStyleProfileData.getCategoryCodeList()).thenReturn(categoryCodeList);
		Mockito.when(categoryService.getCategory(getCatalogData(), categoryCode)).thenReturn(categoryModel);
		categoryList.add(categoryModel);
		Mockito.doNothing().when(myStyleProfileService).modifyCategory(categoryList);
		defaultMyStyleProfileFacadeImpl.modifyCategory(myStyleProfileData);
		LOG.info("Method : testSubCategoryData >>>>>>>");
	}
	//	protected DefaultPromotionManager getDefaultPromotionsManager()
	//	{
	//		//	return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	//		final DefaultPromotionManager defaultPromotionManager = (DefaultPromotionManager) Registry.getApplicationContext().getBean(
	//				"defaultPromotionManager");
	//		return defaultPromotionManager;
	//
	//	}
}
