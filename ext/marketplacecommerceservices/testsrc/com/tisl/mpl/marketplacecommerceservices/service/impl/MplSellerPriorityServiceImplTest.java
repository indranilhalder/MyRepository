/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tisl.mpl.core.model.MplSellerPriorityModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerPriorityDao;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "classpath:/marketplacecommerceservices/marketplace_servicelayer_mock_base-test.xml" })
public class MplSellerPriorityServiceImplTest
{
	@Resource(name = "mplSellerPriorityDao")
	private MplSellerPriorityDao mplSellerPriorityDao;
	@Autowired
	private ModelService modelService;

	final MplSellerPriorityServiceImpl sellerPriority = new MplSellerPriorityServiceImpl();

	@Test
	public void testUpdatatingPriorityForListingIdAndCategory()
	{
		final List<MplSellerPriorityModel> sellerPriorityModels = new ArrayList<MplSellerPriorityModel>();
		final MplSellerPriorityModel sellerPriority = modelService.create(EmployeeModel.class);
		sellerPriorityModels.add(sellerPriority);
		assertEquals(sellerPriorityModels, mplSellerPriorityDao.getAllSellerPriorities());
		assertNotNull(sellerPriority.getIsActive());
		assertEquals(Boolean.TRUE, sellerPriority.getIsActive());
		assertEquals(new SimpleDateFormat("22/11/2015"), sellerPriority.getPriorityStartDate());
		assertEquals(new SimpleDateFormat("22/11/2016"), sellerPriority.getPriorityEndDate());
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		assertEquals(categoryModel, sellerPriority.getCategoryId());
		final ProductModel productModel = modelService.create(ProductModel.class);
		assertEquals(productModel, sellerPriority.getListingId());
		testCategoryLevel(categoryModel, Integer.valueOf(0).intValue());
		final SellerMasterModel sellerMasterModel = modelService.create(SellerMasterModel.class);
		validateUssidForSellers(categoryModel, sellerMasterModel);
		validateUssidFromSkuId(productModel, sellerMasterModel);
		this.sellerPriority.updateSellerPriorityDetails();
	}

	@Test
	public void testUpdatatingPriorityForCategoryId()
	{
		final List<MplSellerPriorityModel> sellerPriorityModels = new ArrayList<MplSellerPriorityModel>();
		final MplSellerPriorityModel sellerPriority = modelService.create(EmployeeModel.class);
		sellerPriorityModels.add(sellerPriority);
		assertEquals(sellerPriorityModels, mplSellerPriorityDao.getAllSellerPriorities());
		assertNotNull(sellerPriority.getIsActive());
		assertEquals(Boolean.TRUE, sellerPriority.getIsActive());
		assertEquals(new SimpleDateFormat("22/11/2015"), sellerPriority.getPriorityStartDate());
		assertEquals(new SimpleDateFormat("22/11/2016"), sellerPriority.getPriorityEndDate());
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		assertEquals(categoryModel, sellerPriority.getCategoryId());
		final ProductModel productModel = modelService.create(ProductModel.class);
		assertEquals(productModel, sellerPriority.getListingId());
		testCategoryLevel(categoryModel, Integer.valueOf(0).intValue());
		final SellerMasterModel sellerMasterModel = modelService.create(SellerMasterModel.class);
		validateUssidFromSkuId(productModel, sellerMasterModel);
		this.sellerPriority.updateSellerPriorityDetails();
	}


	@Test
	public void testUpdatatingPriorityForListingId()
	{
		final List<MplSellerPriorityModel> sellerPriorityModels = new ArrayList<MplSellerPriorityModel>();
		final MplSellerPriorityModel sellerPriority = modelService.create(EmployeeModel.class);
		sellerPriorityModels.add(sellerPriority);
		assertEquals(sellerPriorityModels, mplSellerPriorityDao.getAllSellerPriorities());
		assertNotNull(sellerPriority.getIsActive());
		assertEquals(Boolean.TRUE, sellerPriority.getIsActive());
		assertEquals(new SimpleDateFormat("22/11/2015"), sellerPriority.getPriorityStartDate());
		assertEquals(new SimpleDateFormat("22/11/2016"), sellerPriority.getPriorityEndDate());
		assertEquals(null, sellerPriority.getCategoryId());
		assertEquals(null, sellerPriority.getListingId());
		final SellerMasterModel sellerMasterModel = modelService.create(SellerMasterModel.class);
		validateUssidFromSkuId(sellerPriority.getListingId(), sellerMasterModel);
		this.sellerPriority.updateSellerPriorityDetails();
	}


	@Test
	private void validateUssidForSellers(final CategoryModel category, final SellerMasterModel sellerMasterModel)
	{
		final List<String> ussidList = new ArrayList<String>();
		ussidList.add("US100");
		final CategoryModel testCategoryModel = modelService.create(CategoryModel.class);
		final CategoryModel testCategoryModel1 = modelService.create(CategoryModel.class);
		final ProductModel productModel = modelService.create(ProductModel.class);
		sellerMasterModel.setId("s0001");
		final List<CategoryModel> subCategorymodels = new ArrayList<CategoryModel>();
		subCategorymodels.add(testCategoryModel);
		subCategorymodels.add(testCategoryModel1);
		final Collection<CategoryModel> result = category.getCategories();
		assertThat(result).contains(testCategoryModel);
		final List<ProductModel> productModelList = new ArrayList<ProductModel>();
		productModelList.add(productModel);
		assertEquals(productModelList, testCategoryModel.getProducts());
		final SellerInformationModel selleryModel1 = modelService.create(SellerInformationModel.class);
		selleryModel1.setSellerArticleSKU("US100");
		selleryModel1.setSellerID("s0001");
		assertThat(productModel.getSellerInformationRelator()).contains(selleryModel1);
		assertEquals(sellerMasterModel.getId(), selleryModel1.getSellerID());
		assertEquals(ussidList, selleryModel1.getSellerArticleSKU());
		this.sellerPriority.updateSellerPriorityDetails();
		// then
	}


	@Test
	private void validateUssidFromSkuId(final ProductModel listingId, final SellerMasterModel sellerMasterModel)
	{
		final List<String> ussidList = new ArrayList<String>();
		ussidList.add("US100");
		sellerMasterModel.setId("s0001");
		//	final ProductModel productModel = modelService.create(ProductModel.class);
		final List<ProductModel> productModelList = new ArrayList<ProductModel>();
		productModelList.add(listingId);
		final SellerInformationModel selleryModel1 = modelService.create(SellerInformationModel.class);
		selleryModel1.setSellerArticleSKU("US100");
		selleryModel1.setSellerID("s0001");
		assertThat(listingId.getSellerInformationRelator()).contains(selleryModel1);
		assertEquals(sellerMasterModel.getId(), selleryModel1.getSellerID());
		assertEquals(ussidList, selleryModel1.getSellerArticleSKU());

	}

	@Test
	private void testCategoryLevel(final CategoryModel categoryId, int count)
	{
		final CategoryModel testCategoryModel = modelService.create(CategoryModel.class);
		assertThat(categoryId.getSupercategories()).contains(testCategoryModel);
		count = count + 1;
		final int finalCount = 1;
		assertEquals(count, finalCount);
	}






}
