/**
 *
 */
package com.tisl.mpl.services;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplStyleProfileModel;
import com.tisl.mpl.core.model.MyRecommendationsBrandsModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MyStyleProfileDao;
import com.tisl.mpl.marketplacecommerceservices.service.impl.DefaultMyStyleProfileServiceImpl;


/**
 * @author TCS
 *
 */
@UnitTest
public class DefaultMyStyleProfileServiceImplUnitTest
{
	@Mock
	private MplStyleProfileModel oModel;
	@Mock
	private CustomerModel customer;
	@Mock
	private DefaultMyStyleProfileServiceImpl defaultMyStyleProfileServiceImpl;
	@Mock
	private MyStyleProfileDao myStyleProfileDao;
	@Mock
	private MyRecommendationsConfigurationModel configurationModel;
	@Autowired
	private ModelService modelService;
	@Autowired
	private UserService userService;
	private static final Logger LOG = Logger.getLogger(DefaultMyStyleProfileServiceImplUnitTest.class);
	@Mock
	private CategoryModel category;
	@Mock
	private CategoryService categoryService;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private CatalogVersionModel catalogVersionModel;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.defaultMyStyleProfileServiceImpl = Mockito.mock(DefaultMyStyleProfileServiceImpl.class);

		final MyStyleProfileDao myStyleProfileDao = Mockito.mock(MyStyleProfileDao.class);
		defaultMyStyleProfileServiceImpl.setMyStyleProfileDao(myStyleProfileDao);

		//		this.productService = Mockito.mock(ProductService.class);
		//		this.categoryService = Mockito.mock(CategoryService.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.userService = Mockito.mock(UserService.class);
		this.category = Mockito.mock(CategoryModel.class);
		this.customer = Mockito.mock(CustomerModel.class);
		this.oModel = Mockito.mock(MplStyleProfileModel.class);
		this.myStyleProfileDao = Mockito.mock(MyStyleProfileDao.class);
		this.configurationModel = Mockito.mock(MyRecommendationsConfigurationModel.class);
		this.category = Mockito.mock(CategoryModel.class);
		this.categoryService = Mockito.mock(CategoryService.class);
		this.catalogVersionService = Mockito.mock(CatalogVersionService.class);
		this.catalogVersionModel = Mockito.mock(CatalogVersionModel.class);
	}

	@Test
	public void testSaveCategoryData()
	{
		//		final CustomerModel customer = modelService.create(CustomerModel.class);
		//		final MplStyleProfileModel oModel = modelService.create(MplStyleProfileModel.class);
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);

		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);
		//	final MplStyleProfileModel oModel = customer.getMyStyleProfile();
		//		oModel.setPreferredCategory(categoryList);
		//		oModel.setIsStyleProfileCreated(Boolean.FALSE);
		//		customer.setMyStyleProfile(oModel);

		Mockito.doNothing().when(modelService).save(oModel);
		Mockito.doNothing().when(modelService).save(customer);
		defaultMyStyleProfileServiceImpl.saveCategoryData(categoryList);
		LOG.info("Method : testSaveCategoryData >>>>>>>");
	}

	@Test
	public void testSaveBrandData()
	{
		//		final CustomerModel customer = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);

		//	final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);

		//	final MplStyleProfileModel oModel = customer.getMyStyleProfile();
		//		oModel.setPreferredCategory(categoryList);
		//		oModel.setIsStyleProfileCreated(Boolean.FALSE);
		//		customer.setMyStyleProfile(oModel);

		Mockito.doNothing().when(modelService).save(oModel);
		Mockito.doNothing().when(modelService).save(customer);
		defaultMyStyleProfileServiceImpl.saveBrandData(categoryList);
		LOG.info("Method : testSaveCategoryData >>>>>>>");
	}

	@Test
	public void testSaveGenderData()
	{
		//		final CustomerModel customer = new CustomerModel();
		final String genderData = "";
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);

		//		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();

		//	final MplStyleProfileModel oModel = customer.getMyStyleProfile();
		//		oModel.setPreferredCategory(categoryList);
		//		oModel.setIsStyleProfileCreated(Boolean.FALSE);
		//		customer.setMyStyleProfile(oModel);

		Mockito.doNothing().when(modelService).save(oModel);
		Mockito.doNothing().when(modelService).save(customer);
		defaultMyStyleProfileServiceImpl.saveGenderData(genderData);
		LOG.info("Method : testSaveCategoryData >>>>>>>");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetInterstedCategories()
	{
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);
		final List<CategoryModel> catList = new ArrayList<CategoryModel>();
		Mockito.when(customer.getMyStyleProfile().getPreferredCategory()).thenReturn(catList);
		final Collection<CategoryModel> catListTest = defaultMyStyleProfileServiceImpl.getInterstedCategories();
		Assert.assertEquals(catList, catListTest);
	}

	@Test
	public void testIsStyleProfileCreated()
	{
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);
		Mockito.when(customer.getMyStyleProfile().getIsStyleProfileCreated()).thenReturn(Boolean.valueOf(true));
		defaultMyStyleProfileServiceImpl.isStyleProfileCreated();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchRecommendedData()
	{
		final String genderData = "";
		final List<MyRecommendationsConfigurationModel> recommendationList = new ArrayList<MyRecommendationsConfigurationModel>();
		Mockito.when(myStyleProfileDao.fetchRecommendedData(genderData)).thenReturn(recommendationList);
		final List<MyRecommendationsConfigurationModel> recommendationListTest = defaultMyStyleProfileServiceImpl
				.fetchRecommendedData(genderData);
		Assert.assertEquals(recommendationList, recommendationListTest);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testfetchBrands()
	{
		final String genderData = "";
		final String catCode = "MSH10";
		final List<MyRecommendationsConfigurationModel> brandDataList = Mockito
				.spy(new ArrayList<MyRecommendationsConfigurationModel>());
		final List<MyRecommendationsConfigurationModel> dataList = new ArrayList<MyRecommendationsConfigurationModel>();
		Mockito.when(myStyleProfileDao.fetchRecommendedData(genderData)).thenReturn(dataList);
		Mockito.doThrow(new RuntimeException()).when(brandDataList).add(configurationModel);
		//		verify(brandDataList.add(configurationModel));
		final List<MyRecommendationsConfigurationModel> brandDataListTest = defaultMyStyleProfileServiceImpl.fetchBrands(
				genderData, catCode);
		Assert.assertEquals(dataList, brandDataListTest);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchGenderData()
	{
		final String gender = "";
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);
		Mockito.when(oModel.getSelectedGender()).thenReturn(gender);
		defaultMyStyleProfileServiceImpl.fetchGenderData();
		//		assertEquals("And should equals what the mock returned", gender, genderTest);
		//		Assert.assertEquals(gender, genderTest);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchSubCategories()
	{
		final String catCode = "MSH10";
		final String genderData = "";
		final List<CategoryModel> subCategoryData = new ArrayList<CategoryModel>();
		Mockito.when(categoryService.getCategory(getCatalogData(), catCode)).thenReturn(category);
		Mockito.when(categoryService.getAllSubcategoriesForCategory(category)).thenReturn(subCategoryData);
		final List<CategoryModel> subCategoryDataTest = defaultMyStyleProfileServiceImpl.fetchSubCategories(genderData, catCode);
		Assert.assertEquals(subCategoryData, subCategoryDataTest);
	}

	protected CatalogVersionModel getCatalogData()
	{
		//	return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
		//		final DefaultPromotionManager defaultPromotionManager = (DefaultPromotionManager) Registry.getApplicationContext().getBean(
		//				"defaultPromotionManager");
		//		return defaultPromotionManager;
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		return catalogVersionModel;

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetInterstedBrands()
	{
		final Collection<CategoryModel> catModel = new ArrayList<CategoryModel>();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);
		Mockito.when(customer.getMyStyleProfile().getPreferredBrand()).thenReturn(catModel);
		final Collection<CategoryModel> catModelTest = defaultMyStyleProfileServiceImpl.getInterstedBrands();
		Assert.assertEquals(catModel, catModelTest);
	}

	@Test
	public void testSaveSubCategoryData()
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);
		Mockito.doNothing().when(modelService).save(oModel);
		Mockito.doNothing().when(modelService).save(customer);
		defaultMyStyleProfileServiceImpl.saveSubCategoryData(categoryList);
	}

	@Test
	public void testRemoveMyStyleProfile()
	{
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);
		Mockito.doNothing().when(modelService).remove(oModel);
		defaultMyStyleProfileServiceImpl.removeMyStyleProfile();
	}

	@Test
	public void testRemoveSingleBrand()
	{
		final List<CategoryModel> newBrandList = new ArrayList<CategoryModel>();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		defaultMyStyleProfileServiceImpl.removeSingleBrand(newBrandList);
	}

	@Test
	public void testRemoveSingleCategory()
	{
		final List<CategoryModel> newCatList = new ArrayList<CategoryModel>();
		final ArrayList<CategoryModel> preferredBrandListCopy = new ArrayList<CategoryModel>();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		defaultMyStyleProfileServiceImpl.removeSingleCategory(newCatList, preferredBrandListCopy);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchSubCatdOfBrands()
	{
		final String catCode = "MSH10";
		final List<MyRecommendationsBrandsModel> recommendationsBrands = new ArrayList<MyRecommendationsBrandsModel>();
		Mockito.when(myStyleProfileDao.fetchSubCatdOfBrands(catCode)).thenReturn(recommendationsBrands);
		final List<MyRecommendationsBrandsModel> recommendationsBrandsTest = defaultMyStyleProfileServiceImpl
				.fetchSubCatdOfBrands(catCode);
		Assert.assertEquals(recommendationsBrands, recommendationsBrandsTest);
	}

	@Test
	public void testModifyBrand()
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);
		Mockito.doNothing().when(modelService).save(oModel);
		Mockito.doNothing().when(modelService).save(customer);
		defaultMyStyleProfileServiceImpl.modifyBrand(categoryList);
	}

	@Test
	public void testModifyCategory()
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customer.getMyStyleProfile()).thenReturn(oModel);
		Mockito.doNothing().when(modelService).save(oModel);
		Mockito.doNothing().when(modelService).save(customer);
		defaultMyStyleProfileServiceImpl.modifyCategory(categoryList);
	}
}
