/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeRestrictionDao;
import com.tisl.mpl.model.RestrictionsetupModel;


/**
 * @author TCS
 *
 */
@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class MplPinCodeRestrictionServiceImplTest
{

	@Mock
	private MplPincodeRestrictionDao mplPincodeRestrictionDao;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private ProductService productService;
	@Mock
	private DefaultPromotionManager defaultPromotionManager;

	private final MplPincodeRestrictionServiceImpl restrictionService = new MplPincodeRestrictionServiceImpl();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		//		restrictionService.setDefaultPromotionManager(defaultPromotionManager);
		//		restrictionService.setProductService(productService);
		//	restrictionService.setConfigurationService(configurationService);
		restrictionService.setMplPincodeRestrictionDao(mplPincodeRestrictionDao);
	}

	@Test
	public void getRestrictedUssidForCodandPrepaid()
	{
		//final boolean booleanTshipCODRestricted = false;
		final Map<String, Boolean> ussidMap = new HashMap<String, Boolean>();
		//final Map<String, Boolean> sellerIdMap = new HashMap<String, Boolean>();
		final List<String> articleSKUID = new ArrayList<String>();
		articleSKUID.add("US1200");
		final List<String> sellerIdList = new ArrayList<String>();
		sellerIdList.add("12800");
		final String listingID = "987654321";
		final String pincode = "123400";
		final PincodeServiceData serviceData = Mockito.mock(PincodeServiceData.class);
		final CopyOnWriteArrayList<PincodeServiceData> reqList = new CopyOnWriteArrayList<PincodeServiceData>();
		reqList.add(serviceData);
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		given(productService.getProductForCode(listingID)).willReturn(productModel);
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final CategoryModel categorymodel = Mockito.mock(CategoryModel.class);
		categoryList.add(categorymodel);
		given(defaultPromotionManager.getcategoryData(productModel)).willReturn(categoryList);
		final String categoryCode = "fcMensWear";
		given(categorymodel.getCode()).willReturn(categoryCode);
		categoryCodeList.add(categoryCode);
		final RestrictionsetupModel restrictionModelMock = Mockito.mock(RestrictionsetupModel.class);
		final List<RestrictionsetupModel> restrictionList = new ArrayList<RestrictionsetupModel>();
		restrictionList.add(restrictionModelMock);
		given(mplPincodeRestrictionDao.getRestrictedPincode(pincode, articleSKUID, sellerIdList, listingID, categoryCodeList))
				.willReturn(restrictionList);
		given(restrictionModelMock.getSellerId()).willReturn("12800");
		given(restrictionModelMock.getUSSID()).willReturn("US1200");
		given(restrictionModelMock.getCodRestricted()).willReturn("Y");
		given(restrictionModelMock.getPrepaidRestricted()).willReturn("Y");
		ussidMap.put(restrictionModelMock.getUSSID(), Boolean.valueOf(true));
		given(serviceData.getUssid()).willReturn("US1200");
		restrictionService.getRestrictedPincode(articleSKUID, sellerIdList, listingID, pincode, reqList);

	}

	@Test
	public void getRestrictedUssidForCodOnly()
	{

		final List<String> articleSKUID = new ArrayList<String>();
		articleSKUID.add("US1200");
		final List<String> sellerIdList = new ArrayList<String>();
		sellerIdList.add("12800");
		final String listingID = "987654321";
		final String pincode = "123400";
		final PincodeServiceData serviceData = Mockito.mock(PincodeServiceData.class);
		final CopyOnWriteArrayList<PincodeServiceData> reqList = new CopyOnWriteArrayList<PincodeServiceData>();
		reqList.add(serviceData);
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		given(productService.getProductForCode(listingID)).willReturn(productModel);
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final CategoryModel categorymodel = Mockito.mock(CategoryModel.class);
		categoryList.add(categorymodel);
		given(defaultPromotionManager.getcategoryData(productModel)).willReturn(categoryList);
		final String categoryCode = "fcMensWear";
		given(categorymodel.getCode()).willReturn(categoryCode);
		categoryCodeList.add(categoryCode);
		final RestrictionsetupModel restrictionModelMock = Mockito.mock(RestrictionsetupModel.class);
		final List<RestrictionsetupModel> restrictionList = new ArrayList<RestrictionsetupModel>();
		restrictionList.add(restrictionModelMock);
		given(mplPincodeRestrictionDao.getRestrictedPincode(pincode, articleSKUID, sellerIdList, listingID, categoryCodeList))
				.willReturn(restrictionList);
		given(restrictionModelMock.getSellerId()).willReturn("12800");
		given(restrictionModelMock.getUSSID()).willReturn("US1200");
		given(restrictionModelMock.getCodRestricted()).willReturn("Y");
		given(restrictionModelMock.getPrepaidRestricted()).willReturn("N");
		given(serviceData.getUssid()).willReturn("US1200");
		restrictionService.getRestrictedPincode(articleSKUID, sellerIdList, listingID, pincode, reqList);
		restrictionService.getRestrictedPincode(articleSKUID, sellerIdList, listingID, pincode, reqList);

	}

	@Test
	public void getRestrictedSellerIdForCodandPrepaid()
	{
		final Map<String, Boolean> sellerIdMap = new HashMap<String, Boolean>();
		final List<String> articleSKUID = new ArrayList<String>();
		articleSKUID.add("US1200");
		final List<String> sellerIdList = new ArrayList<String>();
		sellerIdList.add("12800");
		final String listingID = "987654321";
		final String pincode = "123400";
		final PincodeServiceData serviceData = Mockito.mock(PincodeServiceData.class);
		final CopyOnWriteArrayList<PincodeServiceData> reqList = new CopyOnWriteArrayList<PincodeServiceData>();
		reqList.add(serviceData);
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		given(productService.getProductForCode(listingID)).willReturn(productModel);
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final CategoryModel categorymodel = Mockito.mock(CategoryModel.class);
		categoryList.add(categorymodel);
		given(defaultPromotionManager.getcategoryData(productModel)).willReturn(categoryList);
		final String categoryCode = "fcMensWear";
		given(categorymodel.getCode()).willReturn(categoryCode);
		categoryCodeList.add(categoryCode);
		final RestrictionsetupModel restrictionModelMock = Mockito.mock(RestrictionsetupModel.class);
		final List<RestrictionsetupModel> restrictionList = new ArrayList<RestrictionsetupModel>();
		restrictionList.add(restrictionModelMock);

		given(mplPincodeRestrictionDao.getRestrictedPincode(pincode, articleSKUID, sellerIdList, listingID, categoryCodeList))
				.willReturn(restrictionList);
		given(restrictionModelMock.getSellerId()).willReturn("12800");
		given(restrictionModelMock.getPrimaryCatID()).willReturn("fcMensWear");
		given(restrictionModelMock.getCodRestricted()).willReturn("Y");
		given(restrictionModelMock.getPrepaidRestricted()).willReturn("Y");
		sellerIdMap.put(restrictionModelMock.getSellerId(), Boolean.valueOf(true));
		restrictionService.getRestrictedPincode(articleSKUID, sellerIdList, listingID, pincode, reqList);
	}

	@Test
	public void getRestrictedSellerIdForForCodandPrepaid()
	{
		//final Map<String, Boolean> ussidMap = new HashMap<String, Boolean>();
		final Map<String, Boolean> sellerIdMap = new HashMap<String, Boolean>();
		final List<String> articleSKUID = new ArrayList<String>();
		articleSKUID.add("US1200");
		final List<String> sellerIdList = new ArrayList<String>();
		//	final boolean booleanTshipCODRestricted = false;
		sellerIdList.add("12800");
		final String listingID = "987654321";
		final String pincode = "123400";
		final PincodeServiceData serviceData = Mockito.mock(PincodeServiceData.class);
		final CopyOnWriteArrayList<PincodeServiceData> reqList = new CopyOnWriteArrayList<PincodeServiceData>();
		reqList.add(serviceData);
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		given(productService.getProductForCode(listingID)).willReturn(productModel);
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final CategoryModel categorymodel = Mockito.mock(CategoryModel.class);
		categoryList.add(categorymodel);
		given(defaultPromotionManager.getcategoryData(productModel)).willReturn(categoryList);
		final String categoryCode = "fcMensWear";
		given(categorymodel.getCode()).willReturn(categoryCode);
		categoryCodeList.add(categoryCode);
		final RestrictionsetupModel restrictionModelMock = Mockito.mock(RestrictionsetupModel.class);
		final List<RestrictionsetupModel> restrictionList = new ArrayList<RestrictionsetupModel>();
		restrictionList.add(restrictionModelMock);
		given(mplPincodeRestrictionDao.getRestrictedPincode(pincode, articleSKUID, sellerIdList, listingID, categoryCodeList))
				.willReturn(restrictionList);
		given(restrictionModelMock.getSellerId()).willReturn("12800");
		given(restrictionModelMock.getPrimaryCatID()).willReturn("fcMensWear");
		given(restrictionModelMock.getCodRestricted()).willReturn("Y");
		given(restrictionModelMock.getPrepaidRestricted()).willReturn("N");
		sellerIdMap.put(restrictionModelMock.getSellerId(), Boolean.valueOf(true));
		restrictionService.getRestrictedPincode(articleSKUID, sellerIdList, listingID, pincode, reqList);
	}

	@Test
	public void getRestrictedCategoryIdForCodandPrepaid()
	{

		final List<String> articleSKUID = new ArrayList<String>();
		articleSKUID.add("US1200");
		final List<String> sellerIdList = new ArrayList<String>();
		sellerIdList.add("12800");
		final String listingID = "987654321";
		final String pincode = "123400";
		final PincodeServiceData serviceData = Mockito.mock(PincodeServiceData.class);
		final CopyOnWriteArrayList<PincodeServiceData> reqList = new CopyOnWriteArrayList<PincodeServiceData>();
		reqList.add(serviceData);
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		given(productService.getProductForCode(listingID)).willReturn(productModel);
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final CategoryModel categorymodel = Mockito.mock(CategoryModel.class);
		categoryList.add(categorymodel);
		given(defaultPromotionManager.getcategoryData(productModel)).willReturn(categoryList);
		final String categoryCode = "fcMensWear";
		given(categorymodel.getCode()).willReturn(categoryCode);
		categoryCodeList.add(categoryCode);
		final RestrictionsetupModel restrictionModelMock = Mockito.mock(RestrictionsetupModel.class);
		final List<RestrictionsetupModel> restrictionList = new ArrayList<RestrictionsetupModel>();
		restrictionList.add(restrictionModelMock);
		given(mplPincodeRestrictionDao.getRestrictedPincode(pincode, articleSKUID, sellerIdList, listingID, categoryCodeList))
				.willReturn(restrictionList);
		given(restrictionModelMock.getPrimaryCatID()).willReturn("fcMensWear");
		given(restrictionModelMock.getCodRestricted()).willReturn("Y");
		given(restrictionModelMock.getPrepaidRestricted()).willReturn("Y");
		restrictionService.getRestrictedPincode(articleSKUID, sellerIdList, listingID, pincode, reqList);
		//final boolean block = true;
	}

	@Test
	public void getRestrictedCategoryIdForCodOnly()
	{
		//		final Map<String, Boolean> ussidMap = new HashMap<String, Boolean>();
		//		final Map<String, Boolean> sellerIdMap = new HashMap<String, Boolean>();
		final List<String> articleSKUID = new ArrayList<String>();
		articleSKUID.add("US1200");
		final List<String> sellerIdList = new ArrayList<String>();
		sellerIdList.add("12800");
		final String listingID = "987654321";
		final String pincode = "123400";
		final PincodeServiceData serviceData = Mockito.mock(PincodeServiceData.class);
		final CopyOnWriteArrayList<PincodeServiceData> reqDataList = new CopyOnWriteArrayList<PincodeServiceData>();
		reqDataList.add(serviceData);
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		given(productService.getProductForCode(listingID)).willReturn(productModel);
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final CategoryModel categorymodel = Mockito.mock(CategoryModel.class);
		categoryList.add(categorymodel);
		given(defaultPromotionManager.getcategoryData(productModel)).willReturn(categoryList);
		final String categoryCode = "fcMensWear";
		given(categorymodel.getCode()).willReturn(categoryCode);
		categoryCodeList.add(categoryCode);
		final RestrictionsetupModel restrictionModelMock = Mockito.mock(RestrictionsetupModel.class);
		final List<RestrictionsetupModel> restrictionList = new ArrayList<RestrictionsetupModel>();
		restrictionList.add(restrictionModelMock);
		given(mplPincodeRestrictionDao.getRestrictedPincode(pincode, articleSKUID, sellerIdList, listingID, categoryCodeList))
				.willReturn(restrictionList);
		given(restrictionModelMock.getPrimaryCatID()).willReturn("fcMensWear");
		given(restrictionModelMock.getCodRestricted()).willReturn("Y");
		given(restrictionModelMock.getPrepaidRestricted()).willReturn("N");
		restrictionService.getRestrictedPincode(articleSKUID, sellerIdList, listingID, pincode, reqDataList);
	}

	@Test
	public void getRestrictedListingIdForCodandPrepaid()
	{

		final List<String> articleSKUID = new ArrayList<String>();
		articleSKUID.add("US1200");
		final List<String> sellerIdList = new ArrayList<String>();
		sellerIdList.add("12800");
		final String listingID = "987654321";
		final String pincode = "123400";
		final PincodeServiceData serviceData = Mockito.mock(PincodeServiceData.class);
		final CopyOnWriteArrayList<PincodeServiceData> reqList = new CopyOnWriteArrayList<PincodeServiceData>();
		reqList.add(serviceData);
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		given(productService.getProductForCode(listingID)).willReturn(productModel);
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final CategoryModel categorymodel = Mockito.mock(CategoryModel.class);
		categoryList.add(categorymodel);
		given(defaultPromotionManager.getcategoryData(productModel)).willReturn(categoryList);
		final String categoryCode = "fcMensWear";
		given(categorymodel.getCode()).willReturn(categoryCode);
		categoryCodeList.add(categoryCode);
		final RestrictionsetupModel restrictionModelMock = Mockito.mock(RestrictionsetupModel.class);
		final List<RestrictionsetupModel> restrictionList = new ArrayList<RestrictionsetupModel>();
		restrictionList.add(restrictionModelMock);
		given(mplPincodeRestrictionDao.getRestrictedPincode(pincode, articleSKUID, sellerIdList, listingID, categoryCodeList))
				.willReturn(restrictionList);
		given(restrictionModelMock.getListingID()).willReturn("987654321");
		given(restrictionModelMock.getCodRestricted()).willReturn("Y");
		given(restrictionModelMock.getPrepaidRestricted()).willReturn("Y");
		restrictionService.getRestrictedPincode(articleSKUID, sellerIdList, listingID, pincode, reqList);
	}

	@Test
	public void getRestrictedListingIdForCodOnly()
	{
		/*
		 * final Map<String, Boolean> ussidMap = new HashMap<String, Boolean>(); final Map<String, Boolean> sellerIdMap =
		 * new HashMap<String, Boolean>();
		 */
		final List<String> articleSKUID = new ArrayList<String>();
		articleSKUID.add("US1200");
		final List<String> sellerIdList = new ArrayList<String>();
		sellerIdList.add("12800");
		final String listingID = "987654321";
		final String pincode = "123400";
		final PincodeServiceData serviceData = Mockito.mock(PincodeServiceData.class);
		final CopyOnWriteArrayList<PincodeServiceData> reqList = new CopyOnWriteArrayList<PincodeServiceData>();
		reqList.add(serviceData);
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		given(productService.getProductForCode(listingID)).willReturn(productModel);
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		final List<String> categoryCodeList = new ArrayList<String>();
		final CategoryModel categorymodel = Mockito.mock(CategoryModel.class);
		categoryList.add(categorymodel);
		given(defaultPromotionManager.getcategoryData(productModel)).willReturn(categoryList);
		final String categoryCode = "fcMensWear";
		given(categorymodel.getCode()).willReturn(categoryCode);
		categoryCodeList.add(categoryCode);
		final RestrictionsetupModel restrictionModelMock = Mockito.mock(RestrictionsetupModel.class);
		final List<RestrictionsetupModel> restrictionList = new ArrayList<RestrictionsetupModel>();
		restrictionList.add(restrictionModelMock);
		given(mplPincodeRestrictionDao.getRestrictedPincode(pincode, articleSKUID, sellerIdList, listingID, categoryCodeList))
				.willReturn(restrictionList);
		given(restrictionModelMock.getListingID()).willReturn("987654321");
		given(restrictionModelMock.getCodRestricted()).willReturn("Y");
		given(restrictionModelMock.getPrepaidRestricted()).willReturn("N");
		//	final boolean booleanTshipCODRestricted = true;
		restrictionService.getRestrictedPincode(articleSKUID, sellerIdList, listingID, pincode, reqList);
	}

}
