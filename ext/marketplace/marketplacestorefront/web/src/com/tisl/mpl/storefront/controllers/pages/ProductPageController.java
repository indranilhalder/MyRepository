/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.storefront.data.MetaElementData;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ReviewValidator;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.acceleratorstorefrontcommons.variants.VariantSortStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import atg.taglib.json.util.JSONException;

import com.granule.json.JSON;
import com.granule.json.JSONArray;
import com.granule.json.JSONObject;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants.USER;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.comparator.SizeGuideHeaderComparator;
import com.tisl.mpl.facade.product.SizeGuideFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.product.RichAttributeData;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.SizeGuideData;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.PDPEmailNotificationService;
import com.tisl.mpl.pincode.facade.PinCodeServiceAvilabilityFacade;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.forms.SellerInformationDetailsForm;
import com.tisl.mpl.util.ExceptionUtil;



/**
 * Controller for product details page
 */


@Controller
@Scope("tenant")
@RequestMapping(value = "/**/p")
public class ProductPageController extends AbstractPageController
{

	/**
	 *
	 */
	private static final String FOOTWEAR = "Footwear";

	/**
	 *
	 */
	private static final String CLOTHING = "Clothing";

	/**
	 *
	 */
	private static final String STOCK_DATA = "stockData";

	/**
	 *
	 */
	private static final String PINCODE_CHECKED = "pincodeChecked";

	/**
	 *
	 */
	private static final String IS_NEW = "isNew";

	private static final String FULLFILMENT_TYPE = "fullfilmentType";

	/**
	 *
	 */
	private static final String IS_ONLINE_EXCLUSIVE = "isOnline";

	/**
	 *
	 */
	private static final String SELECTED_SIZE = "selectedSize";

	/**
	 *
	 */
	private static final String DEFAULT_SELECTED_SIZE = "defaultSelectedSize";

	/**
	 *
	 */
	private static final String ELECTRONICS = "electronics";

	private static final String IMG_COUNT = "imgCount";

	private static final String SKU_ID_FOR_COD = "skuIdForCod";
	private static final String SKU_ID_FOR_HD = "skuIdForHD";
	private static final String SKU_ID_FOR_ED = "skuIdForED";

	private static final String CUSTOMER_CARE_NUMBER = "1-800-282-8282";
	private static final String CUSTOMER_CARE_EMAIL = "hello@tatacliq.com";


	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ProductPageController.class);


	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Resource(name = "productModelUrlResolver")
	private UrlResolver<ProductModel> productModelUrlResolver;
	@Resource(name = "pdpEmailService")
	private PDPEmailNotificationService pdpEmailService;
	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;
	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "productBreadcrumbBuilder")
	private ProductBreadcrumbBuilder productBreadcrumbBuilder;

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	@Resource(name = "variantSortStrategy")
	private VariantSortStrategy variantSortStrategy;

	@Resource(name = "reviewValidator")
	private ReviewValidator reviewValidator;

	@Autowired
	private ConfigurationService configurationService;
	@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	private SessionService sessionService;

	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;
	@Resource(name = "pinCodeFacade")
	private PinCodeServiceAvilabilityFacade pinCodeFacade;



	@Resource(name = "sizeGuideFacade")
	private SizeGuideFacade sizeGuideFacade;

	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;

	@Autowired
	private SizeGuideHeaderComparator sizeGuideHeaderComparator;

	@Autowired
	private UserService userService;



	/**
	 * @param buyBoxFacade
	 *           the buyBoxFacade to set
	 */
	public void setBuyBoxFacade(final BuyBoxFacade buyBoxFacade)
	{
		this.buyBoxFacade = buyBoxFacade;
	}

	/**
	 *
	 * @param productCode
	 * @param dropDownText
	 * @param model
	 * @param request
	 * @param response
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws UnsupportedEncodingException
	 */

	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String productDetail(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			@RequestParam(value = "searchCategory", required = false, defaultValue = " ") final String dropDownText,
			@RequestParam(value = ModelAttributetConstants.SELECTED_SIZE, required = false) final String selectedSize,
			final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		try
		{
			LOG.debug("**************************************opening pdp for*************" + productCode);
			final ProductModel productModel = productService.getProductForCode(productCode);

			final String redirection = checkRequestUrl(request, response, productModelUrlResolver.resolve(productModel));

			if (StringUtils.isNotEmpty(redirection))
			{
				return redirection;
			}
			if (null != sessionService.getAttribute(ModelAttributetConstants.PINCODE))
			{
				model.addAttribute(ModelAttributetConstants.PINCODE, sessionService.getAttribute(ModelAttributetConstants.PINCODE));
			}

			populateProductDetailForDisplay(productModel, model, request);
			// for MSD
			final String msdjsURL = configurationService.getConfiguration().getString("msd.js.url");
			final Boolean isMSDEnabled = Boolean.valueOf(configurationService.getConfiguration().getString("msd.enabled"));
			final String msdRESTURL = configurationService.getConfiguration().getString("msd.rest.url");
			model.addAttribute(new ReviewForm());
			//			final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(productCode,
			//					Arrays.asList(ProductReferenceTypeEnum.SIMILAR, ProductReferenceTypeEnum.ACCESSORIES),
			//					Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE), null);
			//			model.addAttribute(ModelAttributetConstants.PRODUCT_REFERENCES, productReferences);
			model.addAttribute(ModelAttributetConstants.PAGE_TYPE, PageType.PRODUCT.name());
			model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, dropDownText);
			model.addAttribute(ModelAttributetConstants.SELECTED_SIZE, selectedSize);
			model.addAttribute(ModelAttributetConstants.PRODUCT_CATEGORY_TYPE, productModel.getProductCategoryType());
			// for MSD
			model.addAttribute(ModelAttributetConstants.MSD_JS_URL, msdjsURL);
			model.addAttribute(ModelAttributetConstants.IS_MSD_ENABLED, isMSDEnabled);
			model.addAttribute(ModelAttributetConstants.MSD_REST_URL, msdRESTURL);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
			//return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);
			//frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			//return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);
			//return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}

		return getViewForPage(model);
	}

	/**
	 * @description SizeGuide PopUp of a Product
	 * @param productCode
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */

	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.SIZE_GUIDE, method = RequestMethod.GET)
	public String viewSizeGuide(
			@RequestParam(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			final Model model) throws CMSItemNotFoundException
	{
		try
		{

			final ProductModel productModel = productService.getProductForCode(productCode);

			final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
					ProductOption.SELLER, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
					ProductOption.GALLERY, ProductOption.PROMOTIONS, ProductOption.VARIANT_FULL, ProductOption.CLASSIFICATION));


			populateProductData(productData, model);
			final Map<String, List<SizeGuideData>> sizeguideList = sizeGuideFacade.getProductSizeguide(productCode,
					productData.getRootCategory());
			final List<String> headerMap = getHeaderdata(sizeguideList, productData.getRootCategory());
			LOG.info("***************headerMap" + headerMap);
			if (null != productData.getBrand())
			{
				model.addAttribute(ModelAttributetConstants.SIZE_CHART_HEADER_BRAND, productData.getBrand().getBrandname());
			}
			//if(productBreadcrumbBuilder.getBreadcrumbs(productModel).>0)

			if (CollectionUtils.isNotEmpty(productBreadcrumbBuilder.getBreadcrumbs(productModel))
					&& null != productBreadcrumbBuilder.getBreadcrumbs(productModel).get(1).getName()
					&& !productBreadcrumbBuilder.getBreadcrumbs(productModel).get(1).getName().isEmpty())
			{
				model.addAttribute(ModelAttributetConstants.SIZE_CHART_HEADER_CAT,
						new StringBuilder().append(productBreadcrumbBuilder.getBreadcrumbs(productModel).get(1).getName()));
			}

			model.addAttribute(ModelAttributetConstants.HEADER_SIZE_GUIDE, headerMap);
			model.addAttribute(ModelAttributetConstants.PRODUCT_SIZE_GUIDE, sizeguideList);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		return ControllerConstants.Views.Fragments.Product.SizeGuidePopup;


	}




	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.BUYBOZFORSIZEGUIDEAJAX, method = RequestMethod.GET)
	public @ResponseBody JSONObject getBuyboxDataForSizeGuide(
			@RequestParam(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			@RequestParam(ControllerConstants.Views.Fragments.Product.SELLER_ID) final String sellerId) throws JSONException,
			CMSItemNotFoundException, UnsupportedEncodingException, com.granule.json.JSONException
	{
		final JSONObject buyboxJson = new JSONObject();
		buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.EMPTY);
		try
		{
			final BuyBoxData buyboxdata = buyBoxFacade.buyboxForSizeGuide(productCode, sellerId);
			if (buyboxdata != null)
			{
				if (null != buyboxdata.getAvailable())
				{
					if (null != sessionService.getAttribute(ModelAttributetConstants.PINCODE)
							&& null != sessionService.getAttribute(ModelAttributetConstants.PINCODE_DETAILS))
					{
						for (final PinCodeResponseData response : (List<PinCodeResponseData>) sessionService
								.getAttribute(ModelAttributetConstants.PINCODE_DETAILS))
						{
							if (response.getUssid().equals(buyboxdata.getSellerArticleSKU()))
							{
								if (response.getIsServicable().equalsIgnoreCase("Y"))
								{
									buyboxJson.put(ControllerConstants.Views.Fragments.Product.AVAILABLESTOCK, response.getStockCount());
								}
								buyboxJson.put(ControllerConstants.Views.Fragments.Product.PINCODE_SERVICABILITY,
										response.getIsServicable());

							}
						}

					}
					else
					{
						buyboxJson.put(ControllerConstants.Views.Fragments.Product.AVAILABLESTOCK, buyboxdata.getAvailable());
					}


					if (buyboxdata.getSpecialPrice() != null && buyboxdata.getSpecialPrice().getValue().doubleValue() > 0)
					{
						buyboxJson.put(ControllerConstants.Views.Fragments.Product.SPECIAL_PRICE, buyboxdata.getSpecialPrice()
								.getFormattedValue());
					}
					else if (buyboxdata.getPrice().getValue().doubleValue() > 0.0)
					{
						buyboxJson.put(ControllerConstants.Views.Fragments.Product.PRICE, buyboxdata.getPrice().getFormattedValue());
					}
					else
					{
						buyboxJson.put(ControllerConstants.Views.Fragments.Product.PRICE, buyboxdata.getMrp().getFormattedValue());
					}
					buyboxJson.put(ControllerConstants.Views.Fragments.Product.MRP, buyboxdata.getMrp().getValue());
					buyboxJson.put(ControllerConstants.Views.Fragments.Product.SELLER_ID, buyboxdata.getSellerId());
					buyboxJson.put(ControllerConstants.Views.Fragments.Product.SELLER_NAME, buyboxdata.getSellerName());
					buyboxJson.put(ControllerConstants.Views.Fragments.Product.SELLER_ARTICLE_SKU, buyboxdata.getSellerArticleSKU());
				}
				else
				{

					LOG.debug("***************************Inproper BuyBox data********************");
					buyboxJson.put(ModelAttributetConstants.NOSELLER, ControllerConstants.Views.Fragments.Product.NO_PRODUCT);

				}
			}
			else
			{

				LOG.debug("***************************Inproper BuyBox data********************");
				buyboxJson.put(ModelAttributetConstants.NOSELLER, ControllerConstants.Views.Fragments.Product.NO_PRODUCT);

			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.ERROR_OCCURED);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.ERROR_OCCURED);
		}
		return buyboxJson;
	}

	private List<String> getHeaderdata(final Map<String, List<SizeGuideData>> sizeguideList, final String categoryType)
	{
		final Map<String, String> headerMap = new HashMap<String, String>();
		final List<String> headerMapData = new ArrayList<String>();
		for (final String key : sizeguideList.keySet())
		{
			if (categoryType.equalsIgnoreCase(CLOTHING))
			{
				for (final SizeGuideData data : sizeguideList.get(key))
				{

					if (null == headerMap.get(data.getDimensionSize()))
					{
						headerMap.put(data.getDimensionSize(), data.getDimensionSize());
					}

				}

			}
			else if (categoryType.equalsIgnoreCase(FOOTWEAR))
			{
				for (final SizeGuideData data : sizeguideList.get(key))
				{
					if (data.getAge() != null)
					{
						headerMap.put(configurationService.getConfiguration().getString("footwear.header.age"), "Y");
					}
					if (data.getDimension() != null)
					{
						headerMap.put(configurationService.getConfiguration().getString("footwear.header.footlength"), "Y");
					}
					if (data.getDimensionSize() != null)
					{
						headerMap.put(configurationService.getConfiguration().getString("footwear.header.UK"), "Y");
					}
					if (data.getDimensionValue() != null)
					{
						headerMap.put(configurationService.getConfiguration().getString("footwear.header.Witdth"), "Y");
					}
					if (data.getEuroSize() != null)
					{
						headerMap.put(configurationService.getConfiguration().getString("footwear.header.EURO"), "Y");
					}
					if (data.getUsSize() != null)
					{
						headerMap.put(configurationService.getConfiguration().getString("footwear.header.US"), "Y");
					}
				}
			}
		}
		for (final String keyData : headerMap.keySet())
		{
			headerMapData.add(keyData);
		}

		Collections.sort(headerMapData, sizeGuideHeaderComparator);
		return headerMapData;
	}

	/**
	 * this method checks the servicability of a pincode and fetches list of servicable sellers/skuids from oms
	 *
	 * @param pin
	 * @param productCode
	 * @param seller
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws NullPointerException
	 */
	@ResponseBody
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.CHECK_PINCODE, method = RequestMethod.GET)
	public List<PinCodeResponseData> getPincodeServicabilityDetails(@RequestParam(value = "pin") final String pin,
			@RequestParam(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			final Model model) throws CMSItemNotFoundException
	{
		List<PinCodeResponseData> response = null;

		try
		{
			//TISSEC-11
			final String regex = "\\d{6}";

			if (pin.matches(regex))
			{
				sessionService.setAttribute(ModelAttributetConstants.PINCODE, pin);
				response = pinCodeFacade.getResonseForPinCode(productCode, pin, populatePinCodeServiceData(productCode));
				sessionService.setAttribute(ModelAttributetConstants.PINCODE_DETAILS, response);
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		return response;

	}


	@Deprecated
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/zoomImages", method = RequestMethod.GET)
	public String showZoomImages(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			@RequestParam(value = "galleryPosition", required = false) final String galleryPosition, final Model model)
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Collections.singleton(ProductOption.GALLERY));
		final List<Map<String, ImageData>> images = productDetailsHelper.getGalleryImages(productData);
		populateProductData(productData, model);
		if (galleryPosition != null)
		{
			try
			{
				model.addAttribute("zoomImageUrl", images.get(Integer.parseInt(galleryPosition)).get("zoom").getUrl());
			}
			catch (final IndexOutOfBoundsException | NumberFormatException ioebe)
			{
				model.addAttribute("zoomImageUrl", ModelAttributetConstants.EMPTY);
			}
		}
		return ControllerConstants.Views.Fragments.Product.ZoomImagesPopup;
	}

	/**
	 * Populating other sellers for displaying into sellersDetail page
	 *
	 * @param productCode
	 * @param model
	 * @param ussid
	 * @return getViewForPage
	 * @throws CMSItemNotFoundException
	 * @throws EtailNonBusinessExceptions
	 */

	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/viewSellers", method =
	{ RequestMethod.POST, RequestMethod.GET })
	public String viewSellers(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			@RequestParam(value = ModelAttributetConstants.SELECTED_SIZE, required = false) final String selectedSize,
			final Model model, @Valid final SellerInformationDetailsForm form, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		try
		{
			final ProductModel productModel = productService.getProductForCode(productCode);

			final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
					ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY, ProductOption.CATEGORIES,
					ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL));
			final String sharePath = configurationService.getConfiguration().getString("social.share.path");
			populateProductData(productData, model);
			final List<String> deliveryInfoList = new ArrayList<String>();
			deliveryInfoList.add(ModelAttributetConstants.EXPRESS_DELIVERY);
			deliveryInfoList.add(ModelAttributetConstants.HOME_DELIVERY);
			/* deliverychange */
			final Map<String, Map<String, Integer>> deliveryModeATMap = productDetailsHelper.getDeliveryModeATMap(deliveryInfoList);
			updatePageTitle(productData, model);
			model.addAttribute(ControllerConstants.Views.Fragments.Product.DELIVERY_MODE_MAP, deliveryModeATMap);
			final String emiCuttOffAmount = configurationService.getConfiguration().getString("marketplace.emiCuttOffAmount");
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, productBreadcrumbBuilder.getBreadcrumbs(productModel));
			model.addAttribute(ModelAttributetConstants.EMI_CUTTOFFAMOUNT, emiCuttOffAmount);
			model.addAttribute(ModelAttributetConstants.SELLERS_SKU_ID_LIST, form.getSellersSkuListId());
			model.addAttribute(SKU_ID_FOR_ED, form.getSkuIdForED());
			model.addAttribute(SKU_ID_FOR_HD, form.getSkuIdForHD());
			model.addAttribute(SKU_ID_FOR_COD, form.getSkuIdForCod());
			model.addAttribute(ModelAttributetConstants.SKU_IDS_WITH_NO_STOCK, form.getSkuIdsWithNoStock());
			final List<PinCodeResponseData> stockDataArray = new ArrayList<PinCodeResponseData>();
			if (null != form.getStockDataArray() && !form.getStockDataArray().isEmpty())
			{
				final List stockData = (List) JSON.parse(form.getStockDataArray());
				final JSONArray recs = new JSONArray(stockData);
				for (int i = 0; i < recs.length(); ++i)
				{
					final PinCodeResponseData data = new PinCodeResponseData();
					final JSONObject rec = recs.getJSONObject(i);
					final String ussid = rec.getString("ussid");
					final String stock = rec.getString("stock");
					data.setUssid(ussid);
					data.setStockCount(Integer.valueOf(stock));
					stockDataArray.add(data);
				}
				model.addAttribute(STOCK_DATA, stockDataArray);
			}

			model.addAttribute(ControllerConstants.Views.Fragments.Product.PAGE_LIMIT, configurationService.getConfiguration()
					.getString(ControllerConstants.Views.Fragments.Product.OTHERSELLERS_PAGE_LIMIT));
			model.addAttribute(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE, productData.getCode());
			model.addAttribute(ModelAttributetConstants.SHARED_PATH, sharePath);
			model.addAttribute(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE, productData.getCode());
			model.addAttribute(ModelAttributetConstants.SHARED_PATH, sharePath);
			if (null != sessionService.getAttribute(ModelAttributetConstants.PINCODE))
			{
				model.addAttribute(ModelAttributetConstants.PINCODE, sessionService.getAttribute(ModelAttributetConstants.PINCODE));
			}
			getRequestContextData(request).setProduct(productModel);
			storeCmsPageInModel(model, getContentPageForLabelOrId(ControllerConstants.Views.Fragments.Product.VIEW_SELLERS));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ControllerConstants.Views.Fragments.Product.VIEW_SELLERS));
			final String metaDescription = productData.getSeoMetaDescription();
			final String metaTitle = productData.getSeoMetaTitle();
			final String pdCode = productData.getCode();
			model.addAttribute(DEFAULT_SELECTED_SIZE, form.getSelectedSizeVariant());
			model.addAttribute(ModelAttributetConstants.SELECTED_SIZE, selectedSize);
			model.addAttribute(PINCODE_CHECKED, form.getIsPinCodeChecked());
			model.addAttribute(ModelAttributetConstants.SELLER_PAGE, ModelAttributetConstants.Y);
			model.addAttribute(ModelAttributetConstants.PRODUCT_CATEGORY_TYPE, productModel.getProductCategoryType());
			setUpMetaData(model, metaDescription, metaTitle, pdCode);
			final String googleClientid = configurationService.getConfiguration().getString("google.data-clientid");
			final String facebookAppid = configurationService.getConfiguration().getString("facebook.app_id");
			model.addAttribute(ModelAttributetConstants.GOOGLECLIENTID, googleClientid);
			model.addAttribute(ModelAttributetConstants.FACEBOOKAPPID, facebookAppid);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);

		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);

		}
		return getViewForPage(model);

	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * Populating information for quick view
	 *
	 * @param productCode
	 * @param model
	 * @return ControllerConstants
	 * @throws CMSItemNotFoundException
	 * @throws EtailNonBusinessExceptions
	 */
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/quickView", method = RequestMethod.GET)
	public String showQuickView(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			@RequestParam(value = ModelAttributetConstants.SELECTED_SIZE, required = false) final String selectedSize,
			final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
				ProductOption.SELLER, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
				ProductOption.GALLERY, ProductOption.PROMOTIONS, ProductOption.VARIANT_FULL, ProductOption.CLASSIFICATION));
		try
		{
			populateProductData(productData, model);
			displayConfigurableAttribute(productData, model);
			getRequestContextData(request).setProduct(productModel);
			final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode);
			buyBoxFacade.getRichAttributeDetails(productModel, buyboxdata.getSellerArticleSKU());
			model.addAttribute(ModelAttributetConstants.BUYBOX_USSID, buyboxdata.getSellerArticleSKU());
			model.addAttribute(ModelAttributetConstants.SP_PRICE, buyboxdata.getSpecialPrice());
			model.addAttribute(ModelAttributetConstants.MRP_PRICE, buyboxdata.getMrp());
			model.addAttribute(ModelAttributetConstants.MOP_PRICE, buyboxdata.getPrice());
			model.addAttribute(ControllerConstants.Views.Fragments.Product.AVAILABLESTOCK, buyboxdata.getAvailable());
			model.addAttribute(ControllerConstants.Views.Fragments.Product.ALL_OF_STOCK, buyboxdata.getAllOOStock());

			final String sellerName = buyboxdata.getSellerName();
			model.addAttribute(ModelAttributetConstants.SELLER_NAME, sellerName);
			model.addAttribute(ModelAttributetConstants.SELLER_ID, buyboxdata.getSellerId());
			String isCodEligible = ModelAttributetConstants.EMPTY;
			for (final SellerInformationData seller : productData.getSeller())
			{
				if (seller.getUssid().equals(buyboxdata.getSellerArticleSKU()))
				{
					isCodEligible = seller.getIsCod();
				}
			}
			model.addAttribute(ModelAttributetConstants.IS_COD_ELIGIBLE, isCodEligible);
			model.addAttribute(IS_ONLINE_EXCLUSIVE, Boolean.valueOf(buyBoxFacade.getRichAttributeDetails(productModel,
					buyboxdata.getSellerArticleSKU()).isOnlineExclusive()));
			model.addAttribute(IS_NEW, Boolean.valueOf(buyBoxFacade.getRichAttributeDetails(productModel,
					buyboxdata.getSellerArticleSKU()).getNewProduct()));
			model.addAttribute(FULLFILMENT_TYPE, buyBoxFacade
					.getRichAttributeDetails(productModel, buyboxdata.getSellerArticleSKU()).getFulfillment());
			model.addAttribute(SELECTED_SIZE, selectedSize);
			final String emiCuttOffAmount = configurationService.getConfiguration().getString("marketplace.emiCuttOffAmount");
			final String sharePath = configurationService.getConfiguration().getString("social.share.path");
			model.addAttribute(ModelAttributetConstants.EMI_CUTTOFFAMOUNT, emiCuttOffAmount);
			model.addAttribute(ModelAttributetConstants.SHARED_PATH, sharePath);
			model.addAttribute(IMG_COUNT, Integer.valueOf(productDetailsHelper.getCountForGalleryImages()));
			final String googleClientid = configurationService.getConfiguration().getString("google.data-clientid");
			final String facebookAppid = configurationService.getConfiguration().getString("facebook.app_id");
			model.addAttribute(ModelAttributetConstants.GOOGLECLIENTID, googleClientid);
			model.addAttribute(ModelAttributetConstants.FACEBOOKAPPID, facebookAppid);
		}
		catch (final EtailBusinessExceptions e)
		{
			//	ExceptionUtil.etailBusinessExceptionHandler(e, model);
			frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}

		return ControllerConstants.Views.Fragments.Product.QuickViewPopup;
	}

	@Deprecated
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/review", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String postReview(@PathVariable final String productCode, final ReviewForm form, final BindingResult result,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttrs)
			throws CMSItemNotFoundException
	{
		getReviewValidator().validate(form, result);

		final ProductModel productModel = productService.getProductForCode(productCode);

		if (result.hasErrors())
		{
			updatePageTitle(productModel, model);
			GlobalMessages.addErrorMessage(model, "review.general.error");
			model.addAttribute("showReviewForm", Boolean.TRUE);
			populateProductDetailForDisplay(productModel, model, request);
			storeCmsPageInModel(model, getPageForProduct(productModel));
			return getViewForPage(model);
		}

		final ReviewData review = new ReviewData();
		review.setHeadline(XSSFilterUtil.filter(form.getHeadline()));
		review.setComment(XSSFilterUtil.filter(form.getComment()));
		review.setRating(form.getRating());
		review.setAlias(XSSFilterUtil.filter(form.getAlias()));
		productFacade.postReview(productCode, review);
		GlobalMessages.addFlashMessage(redirectAttrs, GlobalMessages.CONF_MESSAGES_HOLDER, "review.confirmation.thank.you.title");

		return REDIRECT_PREFIX + productModelUrlResolver.resolve(productModel);
	}

	@Deprecated
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/reviewhtml/"
			+ ControllerConstants.Views.Fragments.Product.REVIEWS_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String reviewHtml(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			@PathVariable("numberOfReviews") final String numberOfReviews, final Model model, final HttpServletRequest request)
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final List<ReviewData> reviews;
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Arrays.asList(ProductOption.BASIC, ProductOption.REVIEW));

		if ("all".equals(numberOfReviews))
		{
			reviews = productFacade.getReviews(productCode);
		}
		else
		{
			final int reviewCount = Math.min(Integer.parseInt(numberOfReviews), (productData.getNumberOfReviews() == null ? 0
					: productData.getNumberOfReviews().intValue()));
			reviews = productFacade.getReviews(productCode, Integer.valueOf(reviewCount));
		}

		getRequestContextData(request).setProduct(productModel);
		model.addAttribute("reviews", reviews);
		model.addAttribute("reviewsTotal", productData.getNumberOfReviews());
		model.addAttribute(new ReviewForm());

		return ControllerConstants.Views.Fragments.Product.ReviewsTab;
	}

	@Deprecated
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/writeReview", method = RequestMethod.GET)
	public String writeReview(@PathVariable final String productCode, final Model model) throws CMSItemNotFoundException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		model.addAttribute(new ReviewForm());
		setUpReviewPage(model, productModel);
		return ControllerConstants.Views.Pages.Product.WriteReview;
	}

	@Deprecated
	protected void setUpReviewPage(final Model model, final ProductModel productModel) throws CMSItemNotFoundException
	{
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(productModel.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(productModel.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		storeCmsPageInModel(model, getPageForProduct(productModel));
		model.addAttribute(ModelAttributetConstants.PRODUCT,
				productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC)));
		updatePageTitle(productModel, model);
	}

	@Deprecated
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/writeReview", method = RequestMethod.POST)
	public String writeReview(@PathVariable final String productCode, final ReviewForm form, final BindingResult result,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttrs)
			throws CMSItemNotFoundException
	{
		getReviewValidator().validate(form, result);

		final ProductModel productModel = productService.getProductForCode(productCode);

		if (result.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "review.general.error");
			populateProductDetailForDisplay(productModel, model, request);
			setUpReviewPage(model, productModel);
			return ControllerConstants.Views.Pages.Product.WriteReview;
		}

		final ReviewData review = new ReviewData();
		review.setHeadline(XSSFilterUtil.filter(form.getHeadline()));
		review.setComment(XSSFilterUtil.filter(form.getComment()));
		review.setRating(form.getRating());
		review.setAlias(XSSFilterUtil.filter(form.getAlias()));
		productFacade.postReview(productCode, review);
		GlobalMessages.addFlashMessage(redirectAttrs, GlobalMessages.CONF_MESSAGES_HOLDER, "review.confirmation.thank.you.title");

		return REDIRECT_PREFIX + productModelUrlResolver.resolve(productModel);
	}

	@ExceptionHandler(UnknownIdentifierException.class)
	public String handleUnknownIdentifierException(final UnknownIdentifierException exception, final HttpServletRequest request)
	{
		request.setAttribute("message", exception.getMessage());
		return FORWARD_PREFIX + "/404";
	}

	protected void updatePageTitle(final ProductModel productModel, final Model model)
	{
		model.addAttribute(CMS_PAGE_TITLE, productModel.getTitle());
	}

	protected void updatePageTitle(final ProductData product, final Model model)
	{
		model.addAttribute(CMS_PAGE_TITLE, product.getSeoMetaTitle());
	}

	/**
	 * Populating all details of product from product model
	 *
	 * @param productModel
	 * @param model
	 * @param request
	 * @throws CMSItemNotFoundException
	 */


	protected void populateProductDetailForDisplay(final ProductModel productModel, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		getRequestContextData(request).setProduct(productModel);
		try
		{
			final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
					ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY, ProductOption.CATEGORIES,
					ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL));
			updatePageTitle(productData, model);
			//		sortVariantOptionData(productData);
			storeCmsPageInModel(model, getPageForProduct(productModel));
			populateProductData(productData, model);

			final List<String> deliveryInfo = new ArrayList<String>();

			deliveryInfo.add(ModelAttributetConstants.EXPRESS_DELIVERY);
			deliveryInfo.add(ModelAttributetConstants.HOME_DELIVERY);

			/* delivery change */
			/* final Map<String, String> deliveryModeATMap = productDetailsHelper.getDeliveryModeATMap(deliveryInfo); */
			final Map<String, Map<String, Integer>> deliveryModeATMap = productDetailsHelper.getDeliveryModeATMap(deliveryInfo);
			model.addAttribute(ControllerConstants.Views.Fragments.Product.DELIVERY_MODE_MAP, deliveryModeATMap);
			displayConfigurableAttribute(productData, model);
			//if (productModel.getProductCategoryType().equalsIgnoreCase(ELECTRONICS))
			if (ELECTRONICS.equalsIgnoreCase(productModel.getProductCategoryType()))
			{
				productDetailsHelper.groupGlassificationData(productData);
			}
			final String validTabs = configurationService.getConfiguration().getString(
					"mpl.categories." + productData.getRootCategory());

			final String sharePath = configurationService.getConfiguration().getString("social.share.path");
			//For Gigya
			final String isGigyaEnabled = configurationService.getConfiguration().getString(MessageConstants.USE_GIGYA);
			final String gigyaAPIKey = configurationService.getConfiguration().getString("gigya.apikey");
			final String gigyaRatingURL = configurationService.getConfiguration().getString("gigya.rating.url");
			//end gigya
			final String emiCuttOffAmount = configurationService.getConfiguration().getString("marketplace.emiCuttOffAmount");
			final String cliqCareNumber = configurationService.getConfiguration().getString("cliq.care.number");
			final String cliqCareMail = configurationService.getConfiguration().getString("cliq.care.mail");
			final String googleClientid = configurationService.getConfiguration().getString("google.data-clientid");
			final String facebookAppid = configurationService.getConfiguration().getString("facebook.app_id");

			/* Configurable tabs to be displayed in the PDP page */
			model.addAttribute(ModelAttributetConstants.VALID_TABS, validTabs);
			model.addAttribute(ModelAttributetConstants.SHARED_PATH, sharePath);
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, productBreadcrumbBuilder.getBreadcrumbs(productModel));
			//For Gigya
			model.addAttribute(ModelAttributetConstants.GIGYA_API_KEY, gigyaAPIKey);
			model.addAttribute(ModelAttributetConstants.IS_GIGYA_ENABLED, isGigyaEnabled);
			model.addAttribute(ModelAttributetConstants.RATING_REVIEW_URL, gigyaRatingURL);
			//End Gigya


			model.addAttribute(ModelAttributetConstants.EMI_CUTTOFFAMOUNT, emiCuttOffAmount);
			model.addAttribute(ModelAttributetConstants.CLIQCARENUMBER,
					((cliqCareNumber == null || cliqCareNumber.isEmpty()) ? CUSTOMER_CARE_NUMBER : cliqCareNumber));
			model.addAttribute(ModelAttributetConstants.CLIQCAREMAIL,
					((cliqCareMail == null || cliqCareMail.isEmpty()) ? CUSTOMER_CARE_EMAIL : cliqCareMail));
			model.addAttribute(ModelAttributetConstants.GOOGLECLIENTID, googleClientid);
			model.addAttribute(ModelAttributetConstants.FACEBOOKAPPID, facebookAppid);

			final String metaDescription = productData.getSeoMetaDescription();
			final String metaTitle = productData.getSeoMetaTitle();
			final String productCode = productData.getCode();
			setUpMetaData(model, metaDescription, metaTitle, productCode);
		}
		//populateVariantSizes(productData);
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, ModelAttributetConstants.E0000);
		}

	}





	//TODO
	protected void setUpMetaData(final Model model, final String metaDescription, final String metaTitle, final String productCode)
	{
		final List<MetaElementData> metadata = new LinkedList<>();
		metadata.add(createMetaElement(ModelAttributetConstants.DESCRIPTION, metaDescription));
		//metadata.add(createMetaElement("title", metaTitle));
		//metadata.add(createMetaElement("productCode", productCode));
		model.addAttribute(ModelAttributetConstants.METATAGS, metadata);
	}

	/**
	 * Displaying classification attributes in the Details tab of the PDP page
	 *
	 * @param productData
	 * @param model
	 */
	private void displayConfigurableAttribute(final ProductData productData, final Model model)
	{
		final Map<String, String> mapConfigurableAttribute = new HashMap<String, String>();
		final List<String> warrentyList = new ArrayList<String>();
		try
		{
			/* Checking the presence of classification attributes */
			if (null != productData.getClassifications())
			{
				final List<ClassificationData> ConfigurableAttributeList = new ArrayList<ClassificationData>(
						productData.getClassifications());
				for (final ClassificationData configurableAttributData : ConfigurableAttributeList)
				{
					final List<FeatureData> featureDataList = new ArrayList<FeatureData>(configurableAttributData.getFeatures());

					for (final FeatureData featureData : featureDataList)
					{

						final List<FeatureValueData> featureValueList = new ArrayList<FeatureValueData>(featureData.getFeatureValues());
						if (null != productData.getRootCategory())
						{
							final String properitsValue = configurationService.getConfiguration().getString(
									ModelAttributetConstants.CONFIGURABLE_ATTRIBUTE + productData.getRootCategory());
							//apparel
							final FeatureValueData featureValueData = featureValueList.get(0);
							if ((ModelAttributetConstants.CLOTHING.equalsIgnoreCase(productData.getRootCategory()))
									|| (ModelAttributetConstants.FOOTWEAR.equalsIgnoreCase(productData.getRootCategory())))
							{

								if (null != properitsValue && featureValueData.getValue() != null
										&& properitsValue.toLowerCase().contains(featureData.getName().toLowerCase()))
								{
									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}

							} //end apparel
							  //electronics
							else
							{
								if (properitsValue.toLowerCase().contains(configurableAttributData.getName().toLowerCase()))

								{

									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}

								if (featureData.getName().toLowerCase().contains(ModelAttributetConstants.WARRANTY.toLowerCase()))
								{
									warrentyList.add(featureValueData.getValue());
								}
							}

						}
					}
				}
			}
			//model.addAttribute(ModelAttributetConstants.MAP_CONFIGURABLE_ATTRIBUTE, mapConfigurableAttribute);
			if (ModelAttributetConstants.CLOTHING.equalsIgnoreCase(productData.getRootCategory())
					|| ModelAttributetConstants.FOOTWEAR.equalsIgnoreCase(productData.getRootCategory()))
			{
				model.addAttribute(ModelAttributetConstants.MAP_CONFIGURABLE_ATTRIBUTE, mapConfigurableAttribute);
			}
			else
			{
				final Map<String, String> treeMapConfigurableAttribute = new TreeMap<String, String>(mapConfigurableAttribute);
				model.addAttribute(ModelAttributetConstants.MAP_CONFIGURABLE_ATTRIBUTE, treeMapConfigurableAttribute);
			}

			model.addAttribute(ModelAttributetConstants.WARRANTY, warrentyList);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw new EtailNonBusinessExceptions(e, ModelAttributetConstants.E0000);
		}

	}

	/**
	 * @description Populating product data
	 *
	 * @param productData
	 * @param model
	 */

	protected void populateProductData(final ProductData productData, final Model model) throws EtailNonBusinessExceptions
	{
		model.addAttribute(ModelAttributetConstants.GALLERY_IMAGES, productDetailsHelper.getGalleryImages(productData));
		model.addAttribute(ModelAttributetConstants.PRODUCT, productData);
	}

	protected void sortVariantOptionData(final ProductData productData)
	{
		try
		{
			if (CollectionUtils.isNotEmpty(productData.getBaseOptions()))
			{
				for (final BaseOptionData baseOptionData : productData.getBaseOptions())
				{
					if (CollectionUtils.isNotEmpty(baseOptionData.getOptions()))
					{
						Collections.sort(baseOptionData.getOptions(), variantSortStrategy);
					}
				}
			}

			if (CollectionUtils.isNotEmpty(productData.getVariantOptions()))
			{
				Collections.sort(productData.getVariantOptions(), variantSortStrategy);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}


	protected ReviewValidator getReviewValidator()
	{
		return reviewValidator;
	}

	protected AbstractPageModel getPageForProduct(final ProductModel product) throws CMSItemNotFoundException
	{
		return cmsPageService.getPageForProduct(product);
	}

	/**
	 * @description method is to add products in wishlist in popup in pdp
	 * @param productCode
	 * @param model
	 * @param request
	 * @param response
	 * @throws CMSItemNotFoundException
	 */
	@ResponseBody
	@RequestMapping(value = RequestMappingUrlConstants.ADD_WISHLIST_IN_POPUP, method = RequestMethod.GET)
	//@RequireHardLogIn
	public boolean addWishListsForPDP(@RequestParam(ModelAttributetConstants.PRODUCT) final String productCode,
			@RequestParam("ussid") final String ussid, @RequestParam("wish") final String wishName,
			@RequestParam("sizeSelected") final String sizeSelected, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);

		boolean add = false;
		try
		{
			add = productDetailsHelper.addToWishListInPopup(productCode, ussid, wishName, Boolean.valueOf(sizeSelected));

		}
		catch (final EtailBusinessExceptions e)
		{

			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}

		return add;

	}

	/**
	 * @description method is view wishlists and create default wishlist on opening popup in pdp
	 * @param productCode
	 * @param model
	 * @param request
	 * @param response
	 * @throws CMSItemNotFoundException
	 */
	@ResponseBody
	@RequestMapping(value = RequestMappingUrlConstants.VIEW_WISHLISTS_IN_POPUP, method = RequestMethod.GET)
	public List<WishlistData> showWishListsForPDP(
			@RequestParam(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		List<WishlistData> wishListData = null;
		model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
		try
		{
			wishListData = productDetailsHelper.showWishListsInPopUp(productCode);


		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		return wishListData;
	}




	/**
	 * This method is responsible for fetching winning seller USSID, price and other seller count It will be invoked by
	 * PDP Ajax call and it will return JSON response
	 *
	 *
	 * @param productCode
	 * @return buyboxJson
	 * @throws JSONException
	 * @throws CMSItemNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws com.granule.json.JSONException
	 */
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/buybox", method = RequestMethod.GET)
	public @ResponseBody JSONObject getBuyboxPrice(
			@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode) throws JSONException,
			CMSItemNotFoundException, UnsupportedEncodingException, com.granule.json.JSONException
	{
		final JSONObject buyboxJson = new JSONObject();
		buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.EMPTY);
		try
		{
			final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode);
			if (buyboxdata != null)
			{


				if (buyboxdata.getSpecialPrice() != null && buyboxdata.getSpecialPrice().getValue().doubleValue() > 0)
				{
					buyboxJson.put(ControllerConstants.Views.Fragments.Product.SPECIAL_PRICE, buyboxdata.getSpecialPrice());
				}
				// populate json with price,ussid,sellername and other details
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.PRICE, buyboxdata.getPrice());
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.MRP, buyboxdata.getMrp());
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.SELLER_NAME, buyboxdata.getSellerName());
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.SELLER_ARTICLE_SKU, buyboxdata.getSellerArticleSKU());
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.AVAILABLESTOCK, buyboxdata.getAvailable());
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.OTHERS_SELLERS_COUNT, buyboxdata.getNumberofsellers());
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.MIN_PRICE, buyboxdata.getMinPrice());
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.ALL_OF_STOCK, buyboxdata.getAllOOStock());
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.SELLER_ID, buyboxdata.getSellerId());
			}
			else
			{

				LOG.debug("***************************Inproper BuyBox data********************");
				buyboxJson.put(ModelAttributetConstants.NOSELLER, ControllerConstants.Views.Fragments.Product.NO_SELLERS);

			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.ERROR_OCCURED);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.ERROR_OCCURED);
		}
		return buyboxJson;
	}

	@ResponseBody
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/getRichAttributes", method = RequestMethod.GET)
	public RichAttributeData getRichAttributesDetails(
			@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			@RequestParam("buyboxid") final String buyboxid)
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		RichAttributeData richAttributeData = null;
		try
		{
			richAttributeData = buyBoxFacade.getRichAttributeDetails(productModel, buyboxid);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);

			richAttributeData = null;
		}
		return richAttributeData;
	}

	/**
	 * This is the GET method which fetches the list of banks
	 *
	 * @param productVal
	 * @return List<String>
	 */
	@ResponseBody
	@RequestMapping(value = "/enlistEMIBanks", method = RequestMethod.GET)
	public List<String> enlistEMIBanks(@RequestParam("productVal") final String productVal) throws CMSItemNotFoundException
	{
		Map<String, String> emiBankMap = null;
		final List<String> emiBankNames = new ArrayList<String>();
		try
		{

			final Double productValDbl = (productVal == null) ? new Double(0.0) : Double.valueOf(productVal);
			//getting the EMI related banks
			emiBankMap = mplPaymentFacade.getEMIBankNames(productValDbl);
			for (final Map.Entry<String, String> entry : emiBankMap.entrySet())
			{
				emiBankNames.add(entry.getKey());
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}

		return emiBankNames;
	}

	/**
	 * This is the GET method which fetches the bank terms for EMI mode of Payment
	 *
	 * @param selectedEMIBank
	 * @return List<EMITermRateData>
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.GETTERMS, method = RequestMethod.GET)
	public @ResponseBody List<EMITermRateData> getBankTerms(final String productVal, final String selectedEMIBank)
	{
		List<EMITermRateData> emiTermRate = null;

		try
		{
			final Double productValDbl = (productVal == null) ? new Double(0.0) : Double.valueOf(productVal);
			if (!(productValDbl.equals(ModelAttributetConstants.EMPTY)))
			{
				emiTermRate = mplPaymentFacade.getBankTerms(selectedEMIBank, productValDbl);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}

		return emiTermRate;

	}

	/**
	 * This is the GET method which checks wether is logged in or not
	 *
	 * @return boolean
	 */


	@ResponseBody
	@RequestMapping(value = RequestMappingUrlConstants.CHECKUSER, method = RequestMethod.GET)
	@RequireHardLogIn
	public boolean checkUser()
	{
		boolean flag = true;
		try
		{
			final UserModel user = userService.getCurrentUser();

			if (null != user.getName() && user.getName().equalsIgnoreCase(USER.ANONYMOUS_CUSTOMER))
			{
				flag = false;
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return false;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return false;
		}

		return flag;
	}



	/**
	 * @param deliveryMode
	 * @param ussid
	 * @return deliveryModeData
	 */
	private MarketplaceDeliveryModeData fetchDeliveryModeDataForUSSID(final String deliveryMode, final String ussid)
	{
		final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
		final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = mplCheckoutFacade
				.populateDeliveryCostForUSSIDAndDeliveryMode(deliveryMode, MarketplaceFacadesConstants.INR, ussid);

		final PriceData priceData = productDetailsHelper.formPriceData(mplZoneDeliveryModeValueModel.getValue());
		deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
		deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
		deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
		deliveryModeData.setSellerArticleSKU(ussid);
		deliveryModeData.setDeliveryCost(priceData);
		return deliveryModeData;
	}



	/**
	 * populating the request data to be send to oms
	 *
	 * @param productCode
	 * @return requestData
	 */
	private List<PincodeServiceData> populatePinCodeServiceData(final String productCode)
	{

		final List<PincodeServiceData> requestData = new ArrayList<>();
		PincodeServiceData data = null;
		MarketplaceDeliveryModeData deliveryModeData = null;
		try
		{
			final ProductModel productModel = productService.getProductForCode(productCode);
			final ProductData productData = productFacade.getProductForOptions(productModel,
					Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));

			for (final SellerInformationData seller : productData.getSeller())
			{
				final List<MarketplaceDeliveryModeData> deliveryModeList = new ArrayList<MarketplaceDeliveryModeData>();
				data = new PincodeServiceData();
				if ((null != seller.getDeliveryModes()) && !(seller.getDeliveryModes().isEmpty()))
				{
					for (final MarketplaceDeliveryModeData deliveryMode : seller.getDeliveryModes())
					{
						deliveryModeData = fetchDeliveryModeDataForUSSID(deliveryMode.getCode(), seller.getUssid());
						deliveryModeList.add(deliveryModeData);
					}
					data.setDeliveryModes(deliveryModeList);
				}
				if (null != seller.getFullfillment() && StringUtils.isNotEmpty(seller.getFullfillment()))
				{
					data.setFullFillmentType(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getFullfillment().toUpperCase()));
				}
				if (null != seller.getShippingMode() && (StringUtils.isNotEmpty(seller.getShippingMode())))
				{
					data.setTransportMode(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getShippingMode().toUpperCase()));
				}
				if (null != seller.getSpPrice() && !(seller.getSpPrice().equals(ModelAttributetConstants.EMPTY)))
				{
					data.setPrice(new Double(seller.getSpPrice().getValue().doubleValue()));
				}
				else if (null != seller.getMopPrice() && !(seller.getMopPrice().equals(ModelAttributetConstants.EMPTY)))
				{
					data.setPrice(new Double(seller.getMopPrice().getValue().doubleValue()));
				}
				else if (null != seller.getMrpPrice() && !(seller.getMrpPrice().equals(ModelAttributetConstants.EMPTY)))
				{
					data.setPrice(new Double(seller.getMrpPrice().getValue().doubleValue()));
				}
				else
				{
					LOG.info("*************** No price avaiable for seller :" + seller.getSellerID());
					continue;
				}
				if (null != seller.getIsCod() && StringUtils.isNotEmpty(seller.getIsCod()))
				{
					data.setIsCOD(seller.getIsCod());
				}
				data.setSellerId(seller.getSellerID());
				data.setUssid(seller.getUssid());
				data.setIsDeliveryDateRequired(ControllerConstants.Views.Fragments.Product.N);
				requestData.add(data);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}

		catch (final Exception e)
		{

			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return requestData;
	}

	/**
	 * This method is responsible for fetching winning seller USSID, price and other seller count It will be invoked by
	 * PDP Ajax call and it will return JSON response
	 *
	 *
	 * @param productCode
	 * @return buyboxJson
	 * @throws JSONException
	 * @throws CMSItemNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws com.granule.json.JSONException
	 */
	@ResponseBody
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/otherSellerDetails", method = RequestMethod.GET)
	public List<SellerInformationData> getOtherSellerDetails(
			@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode) throws JSONException,
			CMSItemNotFoundException, UnsupportedEncodingException, com.granule.json.JSONException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC, ProductOption.SELLER));
		List<SellerInformationData> sellerInformationDataList = null;
		try
		{
			sellerInformationDataList = buyBoxFacade.getsellersDetails(productCode);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
			}
		}

		return sellerInformationDataList;
	}

	/**
	 * This method is responsible for sending emails
	 *
	 *
	 * @param emailIds
	 * @param productId
	 * @return successful
	 *
	 */

	@ResponseBody
	@RequestMapping(value = "/sendEmail", method = RequestMethod.GET)
	public boolean sendEmail(@RequestParam("emailList") final String emailIds, @RequestParam("productId") final String productId,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		boolean successful = false;
		try
		{
			successful = true;
			final ProductModel productModel = productService.getProductForCode(productId);
			final ArrayList emailList = new ArrayList(Arrays.asList(emailIds.split(";")));
			final String url = new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName())
					.append(":").append(request.getServerPort()).append(request.getContextPath()).toString();
			pdpEmailService.sendMail(emailList, productModel, url);
			/* Email code */
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}

		return successful;
	}



}
