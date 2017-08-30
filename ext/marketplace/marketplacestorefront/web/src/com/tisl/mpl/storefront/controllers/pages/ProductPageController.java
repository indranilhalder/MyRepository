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

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.storefront.data.MetaElementData;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ReviewValidator;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.acceleratorstorefrontcommons.variants.VariantSortStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSImageComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductContentData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
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

import com.google.gson.Gson;
import com.granule.json.JSON;
import com.granule.json.JSONArray;
import com.granule.json.JSONObject;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.constants.MplConstants.USER;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.VideoComponentModel;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.storelocator.MplStoreLocatorFacade;
import com.tisl.mpl.facade.comparator.SizeGuideHeaderComparator;
import com.tisl.mpl.facade.product.MplProductFacade;
import com.tisl.mpl.facade.product.SizeGuideFacade;
import com.tisl.mpl.facade.product.impl.CustomProductFacadeImpl;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.MplAjaxProductData;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.product.RichAttributeData;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.facades.product.data.SizeGuideData;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.marketplacecommerceservices.service.PDPEmailNotificationService;
import com.tisl.mpl.pincode.facade.PinCodeServiceAvilabilityFacade;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.seller.product.facades.ProductOfferDetailFacade;
import com.tisl.mpl.service.MplGigyaReviewCommentServiceImpl;
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
//@RequestMapping(value = "/**/p")
public class ProductPageController extends MidPageController
{
	/**
	 *
	 */
	private static final String PRODUCT_SIZE_TYPE = "productSizeType";
	/**
	 *
	 */
	private static final String FOOTWEAR = "Footwear";

	/**
	 *
	 */
	private static final String CLOTHING = "Clothing";

	/**
	 * Added Size Guide For Accessories
	 */
	private static final String ACCESSORIES = "Accessories";
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
	private static final String SELECTED_SIZE = "selectedSize";

	/**
	 *
	 */
	private static final String DEFAULT_SELECTED_SIZE = "defaultSelectedSize";

	/**
	 *
	 */
	private static final String ELECTRONICS = "electronics";

	/**
	 * Added by I313024 for TATAUNISTORE-15 START ::::
	 */
	private static final String WATCHES = "Watches";
	/**
	 * Added by I313024 for TATAUNISTORE-15 END ::::
	 */
	private static final String IMG_COUNT = "imgCount";

	private static final String SKU_ID_FOR_COD = "skuIdForCod";
	private static final String SKU_ID_FOR_HD = "skuIdForHD";
	private static final String SKU_ID_FOR_ED = "skuIdForED";
	private static final String SKU_ID_FOR_CNC = "skuIdForCNC";

	private static final String CUSTOMER_CARE_NUMBER = "1-800-282-8282";
	private static final String CUSTOMER_CARE_EMAIL = "hello@tatacliq.com";
	private static final String PRODUCT_OLD_URL_PATTERN = "/**/p";
	private static final String BOXING = "boxing";
	private static final String USSID = "ussid";
	//TPR-3736
	private static final String IA_USS_IDS = "iaUssIds";

	private static final String REGEX = "[^\\w\\s]";

	private static final String NEW_LINE = "\n";//Sonar Fix



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

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "sizeGuideFacade")
	private SizeGuideFacade sizeGuideFacade;

	@Resource(name = "mplStoreLocatorFacade")
	private MplStoreLocatorFacade mplStoreLocatorFacade;

	//	@Autowired
	//	private MplCheckoutFacade mplCheckoutFacade;

	@Autowired
	private SizeGuideHeaderComparator sizeGuideHeaderComparator;

	@Autowired
	private UserService userService;

	@Resource(name = "mplProductFacade")
	private MplProductFacade mplProductFacade;

	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;

	@Resource(name = "prodOfferDetFacade")
	private ProductOfferDetailFacade prodOfferDetFacade;

	//TPR-978
	@Resource(name = "cmsPageService")
	private MplCmsPageService mplCmsPageService;

	@Autowired
	private MplGigyaReviewCommentServiceImpl mplGigyaReviewService;


	//TPR-4389
	/*
	 * @SuppressWarnings("unused") private BrowserType bType;
	 */

	//Sonar fix
	//@Autowired
	//private ConfigurationService configService;
	@Resource(name = "customProductFacade")
	private CustomProductFacadeImpl customProductFacade;


	/**
	 * @param buyBoxFacade
	 *           the buyBoxFacade to set
	 */
	public void setBuyBoxFacade(final BuyBoxFacade buyBoxFacade)
	{
		this.buyBoxFacade = buyBoxFacade;
	}

	/**
	 * @return the mplProductFacade
	 */
	public MplProductFacade getMplProductFacade()
	{
		return mplProductFacade;
	}


	/**
	 * @param mplProductFacade
	 *           the mplProductFacade to set
	 */
	public void setMplProductFacade(final MplProductFacade mplProductFacade)
	{
		this.mplProductFacade = mplProductFacade;
	}

	@Autowired
	private ConfigurationService configService;

	/**
	 * @return the configService
	 */
	@Autowired
	public ConfigurationService getConfigService()
	{
		return configService;
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

	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN, method = RequestMethod.GET)
	public String productDetail(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode,
			@RequestParam(value = "searchCategory", required = false, defaultValue = " ") final String dropDownText,
			@RequestParam(value = ModelAttributetConstants.SELECTED_SIZE, required = false) final String selectedSize,
			@RequestParam(value = MarketplacecommerceservicesConstants.SELLERIDPARAM, required = false) final String sellerId, //CKD:TPR-250
			final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		//final was written here
		String returnStatement = null;

		final String userAgent = request.getHeader("user-agent");
		final String siteurl = request.getRequestURL().toString(); //UF-277
		model.addAttribute("siteurl", siteurl); //UF-277
		//	final Boolean isProductPage = true;
		//CKD:TPR-250:Start
		model.addAttribute("msiteBuyBoxSellerId", StringUtils.isNotBlank(sellerId) ? sellerId : null);
		//CKD:TPR-250:End

		try
		{
			if (null != productCode)
			{
				productCode = productCode.toUpperCase();
			}

			LOG.debug("**************************************opening pdp for*************" + productCode);

			//Changes for UF-238
			if (!StringUtils.isEmpty(userAgent) && userAgent.toLowerCase().contains("googlebot"))
			{

				final String aPlusContent = crawlerDetect(productCode, model, request);
				if (aPlusContent != null)
				{
					model.addAttribute("aplusHTML", aPlusContent);
				}
				else
				{
					model.addAttribute("aplusHTML", "");
				}
			}
			else
			{
				model.addAttribute("aplusHTML", "");
			}


			// TPR- 4389 STARTS FROM HERE

			final ProductModel productModel = productService.getProductForCode(productCode);
			final Map<String, String> reviewAndRating = mplGigyaReviewService.getReviewsAndRatingByCategoryId(
					productModel.getProductCategoryType(), productCode);

			if (reviewAndRating != null)
			{
				for (final Map.Entry<String, String> entry : reviewAndRating.entrySet())
				{
					final String commentCount = entry.getKey();
					final String ratingCount = entry.getValue();
					model.addAttribute("commentCount", commentCount);
					model.addAttribute("averageRating", ratingCount);

				}
			}
			//   TPR-4389 ENDS HERE

			if (productModel.getLuxIndicator() != null
					&& productModel.getLuxIndicator().getCode().equalsIgnoreCase(ControllerConstants.Views.Pages.Cart.LUX_INDICATOR)
					&& !isLuxurySite())
			{
				LOG.debug("**********The product is a luxury product.Hence redirecting to luxury website***********" + productCode);
				final String luxuryHost = configurationService.getConfiguration().getString("luxury.resource.host");
				final String luxuryProductUrl = luxuryHost + "/p-" + productCode;
				LOG.debug("Redirecting to ::::::" + luxuryProductUrl);
				response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				response.setHeader("Location", luxuryProductUrl);
			}
			final String redirection = checkRequestUrl(request, response, productModelUrlResolver.resolve(productModel));

			if (StringUtils.isNotEmpty(redirection))
			{
				//returnStatement = redirection;
				response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				response.setHeader("Location", redirection);
			}

			else
			{
				if (null != sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE))
				{
					model.addAttribute(ModelAttributetConstants.PINCODE,
							sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE));
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

				//CAR-255
				/*
				 * final ProductData productData = productFacade.getProductForOptions(productModel,
				 * Arrays.asList(ProductOption.BASIC, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
				 * ProductOption.GALLERY, ProductOption.CATEGORIES, // ProductOption.PROMOTIONS,
				 * ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL));
				 */







				/*
				 * final String brandName = productData.getBrand().getBrandname(); final String metaDescription =
				 * ModelAttributetConstants.Product_Page_Meta_Description
				 * .replace(ModelAttributetConstants.META_VARIABLE_ZERO, productData.getName())
				 * .replace(ModelAttributetConstants.META_VARIABLE_ONE, brandName)
				 * .replace(ModelAttributetConstants.META_VARIABLE_TWO, productModel.getProductCategoryType()); final String
				 * metaKeywords = ModelAttributetConstants.Product_Page_Meta_Keywords
				 * .replace(ModelAttributetConstants.META_VARIABLE_ZERO, productData.getName())
				 * .replace(ModelAttributetConstants.META_VARIABLE_ONE, productData.getName())
				 * .replace(ModelAttributetConstants.META_VARIABLE_TWO, productData.getName())
				 * .replace(ModelAttributetConstants.META_VARIABLE_THREE, productData.getName())
				 * .replace(ModelAttributetConstants.META_VARIABLE_FOUR, productData.getName())
				 * .replace(ModelAttributetConstants.META_VARIABLE_FIVE, productData.getName());
				 */


				//CAR-255
				/*
				 * final String metaTitle = productData.getSeoMetaTitle(); final String pdCode = productData.getCode();
				 * final String metaDescription = productData.getSeoMetaDescription(); //TISPRD-4977 final String
				 * metaKeyword = productData.getSeoMetaKeyword(); //final String metaKeywords = productData.gets
				 *
				 * setUpMetaData(model, metaDescription, metaTitle, pdCode, metaKeyword);
				 */
				//AKAMAI fix
				if (productModel instanceof PcmProductVariantModel)
				{
					final PcmProductVariantModel variantProductModel = (PcmProductVariantModel) productModel;
					model.addAttribute(ModelAttributetConstants.PRODUCT_SIZE, variantProductModel.getSize());
				}

				returnStatement = getViewForPage(model);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			returnStatement = frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			returnStatement = frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);


		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			returnStatement = frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);

		}


		return returnStatement;
	}

	//Changes for UF-238
	@SuppressWarnings("unused")
	private String crawlerDetect(final String productCode, final Model model, final HttpServletRequest request)
	{

		String ApluscontentResponse = null;
		final StringBuilder content = new StringBuilder();
		BufferedReader bufferedReader = null;

		try
		{
			final String proxyPort = configService.getConfiguration().getString(
					MarketplacecclientservicesConstants.RATING_PROXY_PORT);
			final String proxySet = configService.getConfiguration().getString(
					MarketplacecclientservicesConstants.RATING_PROXY_ENABLED);
			final String proxyHost = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY);
			final int proxyPortInt = Integer.parseInt(proxyPort);
			final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
			final String domain = request.getRequestURL().toString();

			//URL Object
			final URL aplushtml = new URL(domain + "/p-fetchPageContents?productCode=" + productCode);

			//URL Connection object
			final HttpURLConnection pdpConnection = (HttpURLConnection) aplushtml.openConnection();
			pdpConnection.connect();
			bufferedReader = new BufferedReader(new InputStreamReader(pdpConnection.getInputStream()));

			String line;
			while ((line = bufferedReader.readLine()) != null)
			{
				content.append(line + NEW_LINE);
			}
		}
		catch (final IOException e)
		{
			System.err.println("An IOException was caught :" + e.getMessage());
		}
		finally
		{

			try
			{

				if (null != content.toString())
				{
					ApluscontentResponse = StringEscapeUtils.unescapeHtml(content.toString());
					LOG.debug("Click to call web service return " + ApluscontentResponse);
				}
				bufferedReader.close();
			}
			catch (final Exception ex)
			{
				ex.printStackTrace();
			}


		}
		return ApluscontentResponse;
	}

	/**
	 * @param model
	 * @param metaDescription
	 * @param metaTitle
	 * @param pdCode
	 */
	//	private void setUpMetaData(final Model model, final String metaDescription, final String metaTitle, final String pdCode)
	//	{
	//		final List<MetaElementData> metadata = new LinkedList<>();
	//		metadata.add(createMetaElement(ModelAttributetConstants.DESCRIPTION, metaDescription));
	//		//metadata.add(createMetaElement(ModelAttributetConstants.TITLE, metaTitle));
	//		metadata.add(createMetaElement("productCode", pdCode));
	//		//metadata.add(createMetaElement(ModelAttributetConstants.KEYWORDS, metaKeywords));
	//		model.addAttribute(ModelAttributetConstants.METATAGS, metadata);
	//		model.addAttribute(ModelAttributetConstants.PMETATTITLE, metaTitle); //TISPRD-4977
	//
	//	}

	/**
	 * @param productData
	 * @param breadcrumbs
	 *           This method populates Tealium data for PDP
	 */
	private void populateTealiumData(final ProductData productData, final Model model, final List<Breadcrumb> breadcrumbs)
	{
		final List<String> productCategoryList = new ArrayList<String>();
		final List<String> productCategoryIdList = new ArrayList<String>();
		//final String productCategory = null;
		String productBrand = null;
		String productSku = null;
		String productPrice = null;
		String productName = null;
		String categoryId = null;
		String productUnitPrice = null;
		String productSubCategoryName = null;

		//For Analytics Data Layer schema changes
		int productStock = 0;


		try
		{

			if (productData != null && productData.getCategories() != null)
			{
				for (final CategoryData category : productData.getCategories())
				{
					productCategoryList.add(category.getName());
					productCategoryIdList.add(category.getCode());
				}
			}
			final Object[] productCategoryStrings = productCategoryList.toArray();
			//Object[] productCategoryIdStrings = productCategoryIdList.toArray();

			if (productCategoryStrings.length > 0)
			{
				//productCategory = (String) productCategoryStrings[0];
				categoryId = productCategoryIdList.get(0);
			}
			if (productCategoryStrings.length >= 2)
			{
				//productSubCategoryName =(String) productCategoryStrings[1];
				productSubCategoryName = ((String) productCategoryStrings[1]).replaceAll(" ", "_").toLowerCase();
			}
			if (productData != null)
			{
				if (productData.getCode() != null)
				{
					productSku = productData.getCode();

					final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productSku);
					if (buyboxdata != null)
					{
						final PriceData specialPrice = buyboxdata.getSpecialPrice();
						final PriceData mrp = buyboxdata.getMrp();
						final PriceData mop = buyboxdata.getPrice();

						if (mrp != null)
						{
							productUnitPrice = mrp.getValue().toPlainString();
						}
						if (specialPrice != null)
						{
							productPrice = specialPrice.getValue().toPlainString();
						}
						else if (null != mop && null != mop.getValue())
						{
							productPrice = mop.getValue().toPlainString();
						}
						else
						{
							LOG.error("Error in fetching price for tealium for product code : " + productData.getCode());
						}

						if (buyboxdata.getAvailable() != null)
						{
							productStock = buyboxdata.getAvailable().intValue();
							model.addAttribute("product_stock_count", productStock);

						}

						populateOtherPriceDataForTealium(model, productUnitPrice, productPrice);


						//TPR-4358 | product availibity online
						if (productStock > 0)
						{
							model.addAttribute("out_of_stock", Boolean.FALSE); //For tealium
							model.addAttribute("product_availability", "Available online"); //For Schema.org
						}
						else
						{
							model.addAttribute("out_of_stock", Boolean.TRUE); //For Tealium
							model.addAttribute("product_availability", "Not Available online"); //For Schema.org
						}
					}
					//}

				}

				if (productData.getName() != null)
				{
					productName = productData.getName();
				}


				if (productData.getBrand() != null)
				{
					productBrand = productData.getBrand().getBrandname();
				}


			}

			if (CollectionUtils.isNotEmpty(breadcrumbs))
			{
				model.addAttribute("site_section", breadcrumbs.get(0).getName());
				String breadcrumbName = "";
				int count = 1;
				for (final Breadcrumb breadcrumb : breadcrumbs)
				{
					breadcrumbName += breadcrumb.getName();
					if (count < breadcrumbs.size())
					{
						breadcrumbName += ":";

					}
					count++;
				}
				model.addAttribute("page_name", "Product Details:" + breadcrumbName);
			}
			final DecimalFormat df = new DecimalFormat("0.00");
			model.addAttribute("product_unit_price", df.format(Double.parseDouble(productUnitPrice)));
			model.addAttribute("product_list_price", df.format(Double.parseDouble(productPrice)));
			//model.addAttribute("product_list_price", productPrice);

			//Rounding MOP and MRP upto 2 decimal places
			model.addAttribute("product_name", productName);
			model.addAttribute("product_sku", productSku);
			model.addAttribute("page_category_name", "");
			model.addAttribute("category_id", categoryId);
			model.addAttribute("page_section_name", "");
			model.addAttribute("product_id", productData.getCode());
			model.addAttribute("site_section_detail", productData.getRootCategory());
			model.addAttribute("product_brand", productBrand);
			//model.addAttribute("product_category", productCategory);
			model.addAttribute("product_category", breadcrumbs.get(0).getName());
			model.addAttribute("page_subcategory_name_L3", productSubCategoryName);

			//TPR-430 Start
			if (breadcrumbs.size() > 0)
			{
				model.addAttribute(ModelAttributetConstants.PRODUCT_CATEGORY, breadcrumbs.get(0).getName().replaceAll(REGEX, "")
						.replaceAll(" ", "_").toLowerCase());
			}
			if (breadcrumbs.size() > 1)
			{
				model.addAttribute(ModelAttributetConstants.PAGE_SUBCATEGORY_NAME, breadcrumbs.get(1).getName().replaceAll(REGEX, "")
						.replaceAll(" ", "_").toLowerCase());
			}
			if (breadcrumbs.size() > 2)
			{
				model.addAttribute(ModelAttributetConstants.PAGE_SUBCATEGORY_NAME_L3,
						breadcrumbs.get(2).getName().replaceAll(REGEX, "").replaceAll(" ", "_").toLowerCase());
			}
			//For KIDSWEAR tealium data
			if (breadcrumbs.size() > 3)
			{
				model.addAttribute(ModelAttributetConstants.PAGE_SUBCATEGORY_NAME_L4,
						breadcrumbs.get(3).getName().replaceAll(REGEX, "").replaceAll(" ", "_").toLowerCase());
			}
			//TPR-430 End
			//TPR-672 START
			if (CollectionUtils.isNotEmpty(productData.getPotentialPromotions()))
			{
				for (final PromotionData promodata : productData.getPotentialPromotions())
				{

					model.addAttribute("product_applied_promotion_title", promodata.getTitle().toLowerCase());
					model.addAttribute("product_applied_promotion_code", promodata.getCode().toLowerCase());

				}

			}
			//TPR-672 END
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while populating tealium Data for product" + productData.getCode() + ":::" + ex.getMessage());
			//throw ex;
		}
	}

	/**
	 * @param model
	 * @param productUnitPrice
	 * @param productPrice
	 */
	//For Analytics Data layer schema changes
	private void populateOtherPriceDataForTealium(final Model model, final String productUnitPrice, final String productPrice)
	{
		//final double mrp = Integer.parseInt(productUnitPrice);
		//final double mop = Integer.parseInt(productPrice);
		final double mrp = Double.parseDouble(productUnitPrice);
		final double mop = Double.parseDouble(productPrice);
		final double discount = mrp - mop;
		final double percentageDiscount = (discount / mrp) * 100;
		final BigDecimal roundedOffValue = new BigDecimal((int) percentageDiscount);
		model.addAttribute("product_discount", new BigDecimal((int) discount));
		model.addAttribute("product_discount_percentage", roundedOffValue);
	}

	/**
	 * @description SizeGuide PopUp of a Product
	 * @param productCode
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */

	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + ControllerConstants.Views.Fragments.Product.SIZE_GUIDE, method = RequestMethod.GET)
	public String viewSizeGuide(
			@RequestParam(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			@RequestParam(value = ControllerConstants.Views.Fragments.Product.SIZESELECTED) final String sizeSelected,
			final Model model) throws CMSItemNotFoundException
	{
		Map<String, List<SizeGuideData>> sizeguideList = null;
		try
		{

			final StringBuilder allVariants = new StringBuilder();
			final ProductModel productModel = productService.getProductForCode(productCode);

			final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
					ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES, ProductOption.SELLER,
					//					ProductOption.GALLERY, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
					ProductOption.VARIANT_FULL));


			final List<Breadcrumb> breadcrumbList = productBreadcrumbBuilder.getBreadcrumbs(productModel);
			populateProductData(productData, model);
			sizeguideList = sizeGuideFacade.getProductSizeguide(productCode, productData.getRootCategory());

			final List<String> headerMap = getHeaderdata(sizeguideList, productData.getRootCategory());
			if (null != productData.getBrand())
			{
				model.addAttribute(ModelAttributetConstants.SIZE_CHART_HEADER_BRAND, productData.getBrand().getBrandname());
			}
			if (FOOTWEAR.equalsIgnoreCase(productData.getRootCategory())
					|| ACCESSORIES.equalsIgnoreCase(productData.getRootCategory()))
			{
				if (sizeguideList != null && CollectionUtils.isNotEmpty(sizeguideList.get(productCode)))
				{
					model.addAttribute(ModelAttributetConstants.PRODUCT_SIZE_GUIDE, sizeguideList);
				}
				else
				{
					model.addAttribute(ModelAttributetConstants.PRODUCT_SIZE_GUIDE, null);
				}
				//TISPRO-208
				if (CollectionUtils.isNotEmpty(breadcrumbList))
				{
					final String categoryString = breadcrumbList.size() > 1 ? breadcrumbList.get(1).getName() : "";
					model.addAttribute(ModelAttributetConstants.SIZE_CHART_HEADER_CAT, new StringBuilder().append(categoryString));

				}
				else
				{
					model.addAttribute(ModelAttributetConstants.SIZE_CHART_HEADER_CAT, null);
				}
			}
			else if (CLOTHING.equalsIgnoreCase(productData.getRootCategory()))
			{
				model.addAttribute(ModelAttributetConstants.PRODUCT_SIZE_GUIDE, sizeguideList);


				//TISPRO-208
				if (CollectionUtils.isNotEmpty(breadcrumbList))
				{
					model.addAttribute(ModelAttributetConstants.SIZE_CHART_HEADER_CAT,
							new StringBuilder().append(breadcrumbList.get(0).getName()));

				}
				else

				{
					model.addAttribute(ModelAttributetConstants.SIZE_CHART_HEADER_CAT, null);
				}
			}

			if (null != sizeSelected)
			{
				model.addAttribute(ModelAttributetConstants.SELECTEDSIZE, sizeSelected);
			}
			else
			{
				model.addAttribute(ModelAttributetConstants.SELECTEDSIZE, null);
			}
			model.addAttribute(ModelAttributetConstants.HEADER_SIZE_GUIDE, headerMap);

			model.addAttribute(PRODUCT_SIZE_TYPE, productDetailsHelper.getSizeType(productModel));

			//TISPRM-56
			if (CollectionUtils.isNotEmpty(productData.getAllVariantsId()))
			{
				for (final String variants : productData.getAllVariantsId())
				{
					allVariants.append('\'').append(variants).append('\'').append(',');
				}

				final int length = allVariants.length();
				final String allVariantsString = allVariants.substring(0, length - 1);
				model.addAttribute(MarketplacecommerceservicesConstants.ALLVARIANTSSTRING, allVariantsString);
			}

		}
		/*
		 * catch (final EtailNonBusinessExceptions e) { ExceptionUtil.etailNonBusinessExceptionHandler(e); }
		 */
		catch (final EtailNonBusinessExceptions e)
		{
			if (MarketplacecommerceservicesConstants.E0018.equalsIgnoreCase(e.getErrorCode()))
			{
				model.addAttribute(ModelAttributetConstants.PRODUCT_SIZE_GUIDE, null);
			}
			else
			{
				model.addAttribute(ModelAttributetConstants.PRODUCT_SIZE_GUIDE, "dataissue");
			}

			ExceptionUtil.etailNonBusinessExceptionHandler(e);

		}
		return ControllerConstants.Views.Fragments.Product.SizeGuidePopup;


	}


	/**
	 * Get buybox data in respect of productCode and sellerId for sizeguide
	 *
	 * @param productCode
	 * @param sellerId
	 * @return JSONObject
	 * @throws JSONException
	 * @throws CMSItemNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws com.granule.json.JSONException
	 */

	@SuppressWarnings(BOXING)
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + ControllerConstants.Views.Fragments.Product.BUYBOZFORSIZEGUIDEAJAX, method = RequestMethod.GET)
	public @ResponseBody JSONObject getBuyboxDataForSizeGuide(
			@RequestParam(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode,
			@RequestParam(ControllerConstants.Views.Fragments.Product.SELLER_ID) final String sellerId) throws JSONException,
			CMSItemNotFoundException, UnsupportedEncodingException, com.granule.json.JSONException
	{
		LOG.debug(String.format("BUYBOZFORSIZEGUIDEAJAX : productCode:  %s | sellerId : %s ", productCode, sellerId));

		final JSONObject buyboxJson = new JSONObject();
		buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.EMPTY);
		try
		{
			//	This method is responsible to get the buybox data for the given product code and seller ID
			final BuyBoxData buyboxdata = buyBoxFacade.buyboxForSizeGuide(productCode, sellerId);
			if (buyboxdata != null)
			{
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.AVAILABLESTOCK,
						null != buyboxdata.getAvailable() ? buyboxdata.getAvailable() : ModelAttributetConstants.NOVALUE);

				buyboxJson.put(ControllerConstants.Views.Fragments.Product.SPECIAL_PRICE, null != buyboxdata.getSpecialPrice()
						&& null != buyboxdata.getSpecialPrice().getFormattedValue()
						&& !buyboxdata.getSpecialPrice().getFormattedValue().isEmpty() ? buyboxdata.getSpecialPrice()
						.getFormattedValue() : ModelAttributetConstants.NOVALUE);
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.PRICE,
						null != buyboxdata.getPrice() && null != buyboxdata.getPrice().getFormattedValue()
								&& !buyboxdata.getPrice().getFormattedValue().isEmpty() ? buyboxdata.getPrice().getFormattedValue()
								: ModelAttributetConstants.NOVALUE);
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.MRP,
						null != buyboxdata.getMrp() && null != buyboxdata.getMrp().getFormattedValue()
								&& !buyboxdata.getMrp().getFormattedValue().isEmpty() ? buyboxdata.getMrp().getFormattedValue()
								: ModelAttributetConstants.NOVALUE);

				/*
				 * buyboxJson.put(ControllerConstants.Views.Fragments.Product.SPECIAL_PRICE, null !=
				 * buyboxdata.getSpecialPrice() && null != buyboxdata.getSpecialPrice().getFormattedValueNoDecimal() &&
				 * !buyboxdata.getSpecialPrice().getFormattedValueNoDecimal().isEmpty() ? buyboxdata.getSpecialPrice()
				 * .getFormattedValueNoDecimal() : ModelAttributetConstants.NOVALUE);
				 * buyboxJson.put(ControllerConstants.Views.Fragments.Product.PRICE, null != buyboxdata.getPrice() && null
				 * != buyboxdata.getPrice().getFormattedValueNoDecimal() &&
				 * !buyboxdata.getPrice().getFormattedValueNoDecimal().isEmpty() ? buyboxdata.getPrice()
				 * .getFormattedValueNoDecimal() : ModelAttributetConstants.NOVALUE);
				 * buyboxJson.put(ControllerConstants.Views.Fragments.Product.MRP, null != buyboxdata.getMrp() && null !=
				 * buyboxdata.getMrp().getFormattedValueNoDecimal() &&
				 * !buyboxdata.getMrp().getFormattedValueNoDecimal().isEmpty() ? buyboxdata.getMrp()
				 * .getFormattedValueNoDecimal() : ModelAttributetConstants.NOVALUE);
				 */


				buyboxJson.put(ControllerConstants.Views.Fragments.Product.SELLER_ID, buyboxdata.getSellerId());

				buyboxJson.put(ControllerConstants.Views.Fragments.Product.SELLER_NAME,
						null != buyboxdata.getSellerName() ? buyboxdata.getSellerName() : ModelAttributetConstants.EMPTY);

				buyboxJson.put(ControllerConstants.Views.Fragments.Product.SELLER_ARTICLE_SKU,
						null != buyboxdata.getSellerArticleSKU() ? buyboxdata.getSellerArticleSKU() : ModelAttributetConstants.EMPTY);


				//TISPRM-33
				if (null != buyboxdata.getMrp())
				{
					if (buyboxdata.getSpecialPrice() != null && buyboxdata.getSpecialPrice().getValue().doubleValue() > 0)
					{

						final double savingPriceCal = buyboxdata.getMrp().getDoubleValue()
								- buyboxdata.getSpecialPrice().getDoubleValue();
						final double savingPriceCalPer = (savingPriceCal / buyboxdata.getMrp().getDoubleValue()) * 100;
						//Critical Sonar Fix
						//fix for INC144314955
						//final double roundedOffValuebefore = Math.round(savingPriceCalPer * 100.0) / 100.0;
						final double roundedOffValuebefore = Math.floor((savingPriceCalPer * 100.0) / 100.0);
						final BigDecimal roundedOffValue = new BigDecimal((int) roundedOffValuebefore);

						buyboxJson.put(ControllerConstants.Views.Fragments.Product.SAVINGONPRODUCT, roundedOffValue);
					}
					else if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getValue().doubleValue() > 0)
					{
						final double savingPriceCal = buyboxdata.getMrp().getDoubleValue() - buyboxdata.getPrice().getDoubleValue();
						final double savingPriceCalPer = (savingPriceCal / buyboxdata.getMrp().getDoubleValue()) * 100;
						//Critical Sonar Fix
						//fix for INC144314955
						//final double roundedOffValuebefore = Math.round(savingPriceCalPer * 100.0) / 100.0;
						final double roundedOffValuebefore = Math.floor((savingPriceCalPer * 100.0) / 100.0);
						final BigDecimal roundedOffValue = new BigDecimal((int) roundedOffValuebefore);
						buyboxJson.put(ControllerConstants.Views.Fragments.Product.SAVINGONPRODUCT, roundedOffValue);
					}
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

	/**
	 * Set the hedder data of the sizeguide
	 *
	 * @param sizeguideList
	 * @param categoryType
	 * @return List<String>
	 */
	private List<String> getHeaderdata(final Map<String, List<SizeGuideData>> sizeguideList, final String categoryType)
	{
		final Map<String, String> headerMap = new HashMap<String, String>();
		final List<String> headerMapData = new ArrayList<String>();
		try
		{

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
				else if (categoryType.equalsIgnoreCase(ACCESSORIES))
				{
					for (final SizeGuideData data : sizeguideList.get(key))
					{
						if (data.getAge() != null && StringUtils.isNotBlank(data.getAge()))
						{
							headerMap.put(configurationService.getConfiguration().getString("fashionaccessories.sizeguide.header.age"),
									"Y");
							if (!headerMapData.contains(configurationService.getConfiguration().getString(
									"fashionaccessories.sizeguide.header.age")))
							{
								headerMapData.add(configurationService.getConfiguration().getString(
										"fashionaccessories.sizeguide.header.age"));
							}
						}
						if (data.getDimensionSize() != null && StringUtils.isNotBlank(data.getDimensionSize()))
						{
							headerMap.put(configurationService.getConfiguration().getString("fashionaccessories.sizeguide.header.size"),
									"Y");
							if (!headerMapData.contains(configurationService.getConfiguration().getString(
									"fashionaccessories.sizeguide.header.size")))
							{
								headerMapData.add(configurationService.getConfiguration().getString(
										"fashionaccessories.sizeguide.header.size"));
							}
						}
						if (data.getCmsBeltSize() != null && StringUtils.isNotBlank(data.getCmsBeltSize()))
						{
							headerMap.put(
									configurationService.getConfiguration().getString("fashionaccessories.sizeguide.header.cmsbeltsize"),
									"Y");
							if (!headerMapData.contains(configurationService.getConfiguration().getString(
									"fashionaccessories.sizeguide.header.cmsbeltsize")))
							{
								headerMapData.add(configurationService.getConfiguration().getString(
										"fashionaccessories.sizeguide.header.cmsbeltsize"));
							}
						}
						if (data.getInchesBeltSize() != null && StringUtils.isNotBlank(data.getInchesBeltSize()))
						{
							headerMap.put(
									configurationService.getConfiguration()
											.getString("fashionaccessories.sizeguide.header.inchesbeltsize"), "Y");
							if (!headerMapData.contains(configurationService.getConfiguration().getString(
									"fashionaccessories.sizeguide.header.inchesbeltsize")))
							{
								headerMapData.add(configurationService.getConfiguration().getString(
										"fashionaccessories.sizeguide.header.inchesbeltsize"));
							}
						}
						if (data.getCmsWaistSize() != null && StringUtils.isNotEmpty(data.getCmsWaistSize()))
						{
							headerMap.put(
									configurationService.getConfiguration().getString("fashionaccessories.sizeguide.header.cmswaistsize"),
									"Y");
							if (!headerMapData.contains(configurationService.getConfiguration().getString(
									"fashionaccessories.sizeguide.header.cmswaistsize")))
							{
								headerMapData.add(configurationService.getConfiguration().getString(
										"fashionaccessories.sizeguide.header.cmswaistsize"));
							}
						}
						if (data.getInchesWaistSize() != null && StringUtils.isNotEmpty(data.getInchesWaistSize()))
						{
							headerMap.put(
									configurationService.getConfiguration().getString(
											"fashionaccessories.sizeguide.header.incheswaistsize"), "Y");
							if (!headerMapData.contains(configurationService.getConfiguration().getString(
									"fashionaccessories.sizeguide.header.incheswaistsize")))
							{
								headerMapData.add(configurationService.getConfiguration().getString(
										"fashionaccessories.sizeguide.header.incheswaistsize"));
							}
						}
						if (data.getInchesBeltLength() != null && StringUtils.isNotEmpty(data.getInchesBeltLength()))
						{
							headerMap.put(
									configurationService.getConfiguration().getString(
											"fashionaccessories.sizeguide.header.inchesbeltlength"), "Y");
							if (!headerMapData.contains(configurationService.getConfiguration().getString(
									"fashionaccessories.sizeguide.header.inchesbeltlength")))
							{
								headerMapData.add(configurationService.getConfiguration().getString(
										"fashionaccessories.sizeguide.header.inchesbeltlength"));
							}
						}
					}
				}
			}

			if (!categoryType.equalsIgnoreCase(ACCESSORIES))
			{
				for (final String keyData : headerMap.keySet())
				{
					headerMapData.add(keyData);
				}


				Collections.sort(headerMapData, sizeGuideHeaderComparator);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
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
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + ControllerConstants.Views.Fragments.Product.CHECK_PINCODE, method = RequestMethod.GET)
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
				LOG.debug("productCode:" + productCode + "pinCode:" + pin);
				final PincodeModel pinCodeModelObj = pincodeServiceFacade.getLatAndLongForPincode(pin);
				final LocationDTO dto = new LocationDTO();
				Location myLocation = null;
				if (null != pinCodeModelObj)
				{
					try
					{

						dto.setLongitude(pinCodeModelObj.getLongitude().toString());
						dto.setLatitude(pinCodeModelObj.getLatitude().toString());
						myLocation = new LocationDtoWrapper(dto);
						LOG.debug("Selected Location for Latitude:" + myLocation.getGPS().getDecimalLatitude());
						LOG.debug("Selected Location for Longitude:" + myLocation.getGPS().getDecimalLongitude());
						//sessionService.setAttribute(ModelAttributetConstants.PINCODE, pin);
						//TPR-6654
						sessionService.setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, pin);
						response = pinCodeFacade.getResonseForPinCode(productCode, pin,
								pincodeServiceFacade.populatePinCodeServiceData(productCode, myLocation.getGPS()));

						return response;
					}
					catch (final Exception e)
					{
						ExceptionUtil.getCustomizedExceptionTrace(e);
						LOG.debug("configurableRadius values is empty please add radius property in properties file ");
					}
				}

			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		return response;

	}


	@Deprecated
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/zoomImages", method = RequestMethod.GET)
	public String showZoomImages(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode,
			@RequestParam(value = "galleryPosition", required = false) final String galleryPosition, final Model model)
	{
		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
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

	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/viewSellers", method =
	{ RequestMethod.POST, RequestMethod.GET })
	public String viewSellers(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode,
			@RequestParam(value = ModelAttributetConstants.SELECTED_SIZE, required = false) final String selectedSize,
			@RequestParam(value = MarketplacecommerceservicesConstants.SELLERIDPARAM, required = false) final String sellerId, //CKD:TPR-250
			final Model model, @Valid final SellerInformationDetailsForm form, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final StringBuilder allVariants = new StringBuilder();
		String returnStatement = null;
		//CKD:TPR-250:Start
		model.addAttribute("msiteBuyBoxSellerId", StringUtils.isNotBlank(sellerId) ? sellerId : null);
		//CKD:TPR-250:End
		try
		{
			if (null != productCode)
			{
				productCode = productCode.toUpperCase();
			}
			final ProductModel productModel = productService.getProductForCode(productCode);

			final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
					ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY, ProductOption.CATEGORIES,
					//The promotion populator has been added as a business requirement to display promotion details in other sellers page
					ProductOption.PROMOTIONS,
					//ProductOption.CLASSIFICATION,
					ProductOption.VARIANT_FULL));
			final String sharePath = configurationService.getConfiguration().getString("social.share.path");
			populateProductData(productData, model);
			//CKD:TPR-250:Start
			prepareBrandInfoData(model, productData);
			//CKD:TPR-250:End
			final List<String> deliveryInfoList = new ArrayList<String>();

			deliveryInfoList.add(ModelAttributetConstants.EXPRESS_DELIVERY);
			deliveryInfoList.add(ModelAttributetConstants.HOME_DELIVERY);
			deliveryInfoList.add(ModelAttributetConstants.CLICK_AND_COLLECT);

			/* deliverychange */
			final Map<String, Map<String, Integer>> deliveryModeATMap = productDetailsHelper.getDeliveryModeATMap(deliveryInfoList);
			//commented for UF-33
			//updatePageTitle(productData, model);
			model.addAttribute(ControllerConstants.Views.Fragments.Product.DELIVERY_MODE_MAP, deliveryModeATMap);
			final String emiCuttOffAmount = configurationService.getConfiguration().getString("marketplace.emiCuttOffAmount");
			final List<Breadcrumb> breadcrumbList = productBreadcrumbBuilder.getBreadcrumbs(productModel);
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbList);
			model.addAttribute(ModelAttributetConstants.EMI_CUTTOFFAMOUNT, emiCuttOffAmount);
			model.addAttribute(ModelAttributetConstants.SELLERS_SKU_ID_LIST, form.getSellersSkuListId());
			model.addAttribute(SKU_ID_FOR_ED, form.getSkuIdForED());
			model.addAttribute(SKU_ID_FOR_HD, form.getSkuIdForHD());
			model.addAttribute(SKU_ID_FOR_CNC, form.getSkuIdForCNC());
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
					final String ussid = rec.getString(USSID);
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
			//			if (null != sessionService.getAttribute(ModelAttributetConstants.PINCODE))
			//			{
			//				model.addAttribute(ModelAttributetConstants.PINCODE, sessionService.getAttribute(ModelAttributetConstants.PINCODE));
			//			}
			final String pincode = (String) sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			if (StringUtils.isNotEmpty(pincode))
			{
				model.addAttribute(ModelAttributetConstants.PINCODE,
						sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE));
				model.addAttribute(ControllerConstants.Views.Fragments.Product.STORE_AVAIL, mplProductFacade.storeLocatorPDP(pincode));
			}
			getRequestContextData(request).setProduct(productModel);
			storeCmsPageInModel(model, getContentPageForLabelOrId(ControllerConstants.Views.Fragments.Product.VIEW_SELLERS));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ControllerConstants.Views.Fragments.Product.VIEW_SELLERS));
			final String metaDescription = productData.getSeoMetaDescription();
			final String metaTitle = productData.getSeoMetaTitle();
			final String pdCode = productData.getCode();
			final String metaKeyword = productData.getSeoMetaKeyword();
			model.addAttribute(DEFAULT_SELECTED_SIZE, form.getSelectedSizeVariant());
			model.addAttribute(ModelAttributetConstants.SELECTED_SIZE, selectedSize);
			model.addAttribute(PINCODE_CHECKED, form.getIsPinCodeChecked());
			model.addAttribute(ModelAttributetConstants.SELLER_PAGE, ModelAttributetConstants.Y);
			model.addAttribute(ModelAttributetConstants.PRODUCT_CATEGORY_TYPE, productModel.getProductCategoryType());
			model.addAttribute(PRODUCT_SIZE_TYPE, productDetailsHelper.getSizeType(productModel));
			setUpMetaData(model, metaDescription, metaTitle, pdCode, metaKeyword);
			final String googleClientid = configurationService.getConfiguration().getString("google.data-clientid");
			final String facebookAppid = configurationService.getConfiguration().getString("facebook.app_id");
			model.addAttribute(ModelAttributetConstants.GOOGLECLIENTID, googleClientid);
			model.addAttribute(ModelAttributetConstants.FACEBOOKAPPID, facebookAppid);
			populateTealiumData(productData, model, breadcrumbList);
			//AKAMAI fix
			if (productModel instanceof PcmProductVariantModel)
			{
				final PcmProductVariantModel variantProductModel = (PcmProductVariantModel) productModel;
				model.addAttribute("productSize", variantProductModel.getSize());
			}
			//Fix  TISUATMS-831 from SAP
			showSizeGuideForFA(productModel, model);

			//Fix  TISUATMS-831 from SAP end
			if (CollectionUtils.isNotEmpty(productData.getAllVariantsId()))
			{
				//get left over variants
				if (productData.getAllVariantsId().size() > 1)
				{
					productData.getAllVariantsId().remove(productData.getCode());
				}
				for (final String variants : productData.getAllVariantsId())
				{
					allVariants.append(variants).append(',');
				}

				final int length = allVariants.length();
				final String allVariantsString = allVariants.substring(0, length - 1);
				model.addAttribute(MarketplacecommerceservicesConstants.ALLVARIANTSSTRING, allVariantsString);
			}
			returnStatement = getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			//return frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
			returnStatement = frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("EtailNonBusinessExceptions::viewSellers::ProductPageController");
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			returnStatement = frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);

		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			returnStatement = frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);

		}


		return returnStatement;

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
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/quickView", method = RequestMethod.GET)
	public String showQuickView(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode,
			@RequestParam(value = ModelAttributetConstants.SELECTED_SIZE, required = false) final String selectedSize,
			@RequestParam(value = MarketplacecommerceservicesConstants.SELLERIDPARAM, required = false) final String sellerId, //CKD:TPR-250
			final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
		final StringBuilder allVariants = new StringBuilder();
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
				ProductOption.SELLER, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
				//ProductOption.GALLERY, ProductOption.PROMOTIONS, ProductOption.VARIANT_FULL, ProductOption.CLASSIFICATION));
				ProductOption.GALLERY, ProductOption.VARIANT_FULL));//Fix for TISPT-150
		//final String returnStatement = null;
		//CKD:TPR-250:Start
		model.addAttribute("msiteBuyBoxSellerId", StringUtils.isNotBlank(sellerId) ? sellerId : null);
		prepareBrandInfoData(model, productData);
		//CKD:TPR-250:End
		try
		{
			populateProductData(productData, model);
			displayConfigurableAttribute(productData, model);
			getRequestContextData(request).setProduct(productModel);
			model.addAttribute(IMG_COUNT, Integer.valueOf(productDetailsHelper.getCountForGalleryImages()));
			model.addAttribute(SELECTED_SIZE, selectedSize);
			//			final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode);
			//			//buyBoxFacade.getRichAttributeDetails(productModel, buyboxdata.getSellerArticleSKU());
			//			model.addAttribute(ModelAttributetConstants.BUYBOX_USSID, buyboxdata.getSellerArticleSKU());
			//			model.addAttribute(ModelAttributetConstants.SP_PRICE, buyboxdata.getSpecialPrice());
			//			model.addAttribute(ModelAttributetConstants.MRP_PRICE, buyboxdata.getMrp());
			//			model.addAttribute(ModelAttributetConstants.MOP_PRICE, buyboxdata.getPrice());
			//			model.addAttribute(ControllerConstants.Views.Fragments.Product.AVAILABLESTOCK, buyboxdata.getAvailable());
			//			model.addAttribute(ControllerConstants.Views.Fragments.Product.ALL_OF_STOCK, buyboxdata.getAllOOStock());
			//
			//			final String sellerName = buyboxdata.getSellerName();
			//			model.addAttribute(ModelAttributetConstants.SELLER_NAME, sellerName);
			//			model.addAttribute(ModelAttributetConstants.SELLER_ID, buyboxdata.getSellerId());
			//			String isCodEligible = ModelAttributetConstants.EMPTY;
			//			for (final SellerInformationData seller : productData.getSeller())
			//			{
			//				if (seller.getUssid().equals(buyboxdata.getSellerArticleSKU()))
			//				{
			//					isCodEligible = seller.getIsCod();
			//				}
			//			}

			//Remove multiple DB calls
			//			final RichAttributeData richAttribute = buyBoxFacade.getRichAttributeDetails(productModel,
			//					buyboxdata.getSellerArticleSKU());
			//
			//			model.addAttribute(ModelAttributetConstants.IS_COD_ELIGIBLE, isCodEligible);
			//			model.addAttribute(IS_ONLINE_EXCLUSIVE, Boolean.valueOf(richAttribute.isOnlineExclusive()));
			//			model.addAttribute(IS_NEW, richAttribute.getNewProduct());
			//			model.addAttribute(FULLFILMENT_TYPE, richAttribute.getFulfillment());

			final String emiCuttOffAmount = configurationService.getConfiguration().getString("marketplace.emiCuttOffAmount");
			final String sharePath = configurationService.getConfiguration().getString("social.share.path");
			model.addAttribute(ModelAttributetConstants.EMI_CUTTOFFAMOUNT, emiCuttOffAmount);
			model.addAttribute(ModelAttributetConstants.SHARED_PATH, sharePath);
			final String googleClientid = configurationService.getConfiguration().getString("google.data-clientid");
			final String facebookAppid = configurationService.getConfiguration().getString("facebook.app_id");
			model.addAttribute(PRODUCT_SIZE_TYPE, productDetailsHelper.getSizeType(productModel));
			model.addAttribute(ModelAttributetConstants.GOOGLECLIENTID, googleClientid);
			model.addAttribute(ModelAttributetConstants.FACEBOOKAPPID, facebookAppid);

			//AKAMAI fix
			if (productModel instanceof PcmProductVariantModel)
			{
				final PcmProductVariantModel variantProductModel = (PcmProductVariantModel) productModel;
				model.addAttribute("productSizeQuick", variantProductModel.getSize());
			}
			showSizeGuideForFA(productModel, model);
			if (CollectionUtils.isNotEmpty(productData.getAllVariantsId()))
			{
				//get left over variants
				if (productData.getAllVariantsId().size() > 1)
				{
					productData.getAllVariantsId().remove(productData.getCode());
				}
				for (final String variants : productData.getAllVariantsId())
				{
					allVariants.append(variants).append(',');
				}

				final int length = allVariants.length();
				final String allVariantsString = allVariants.substring(0, length - 1);
				model.addAttribute(MarketplacecommerceservicesConstants.ALLVARIANTSSTRING, allVariantsString);
			}
			//returnStatement = ControllerConstants.Views.Fragments.Product.QuickViewPopup;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			model.addAttribute(ModelAttributetConstants.ERROR, ModelAttributetConstants.TRUE);
			//frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
			//returnStatement = ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			model.addAttribute(ModelAttributetConstants.ERROR, ModelAttributetConstants.TRUE);
			//frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			//returnStatement = ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}



		return ControllerConstants.Views.Fragments.Product.QuickViewPopup;
	}

	@Deprecated
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/review", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String postReview(@PathVariable String productCode, final ReviewForm form, final BindingResult result,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttrs)
			throws CMSItemNotFoundException

	{
		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
		String returnStatement = null;
		getReviewValidator().validate(form, result);

		final ProductModel productModel = productService.getProductForCode(productCode);

		if (result.hasErrors())
		{
			updatePageTitle(productModel, model);
			GlobalMessages.addErrorMessage(model, "review.general.error");
			model.addAttribute("showReviewForm", Boolean.TRUE);
			populateProductDetailForDisplay(productModel, model, request);
			storeCmsPageInModel(model, getPageForProduct(productModel));
			returnStatement = getViewForPage(model);
		}
		else
		{

			final ReviewData review = new ReviewData();
			review.setHeadline(XSSFilterUtil.filter(form.getHeadline()));
			review.setComment(XSSFilterUtil.filter(form.getComment()));
			review.setRating(form.getRating());
			review.setAlias(XSSFilterUtil.filter(form.getAlias()));
			productFacade.postReview(productCode, review);
			GlobalMessages
					.addFlashMessage(redirectAttrs, GlobalMessages.CONF_MESSAGES_HOLDER, "review.confirmation.thank.you.title");
			returnStatement = REDIRECT_PREFIX + productModelUrlResolver.resolve(productModel);
		}



		return returnStatement;
	}

	@Deprecated
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/reviewhtml/"
			+ ControllerConstants.Views.Fragments.Product.REVIEWS_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String reviewHtml(@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode,
			@PathVariable("numberOfReviews") final String numberOfReviews, final Model model, final HttpServletRequest request)
	{
		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
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
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/writeReview", method = RequestMethod.GET)
	public String writeReview(@PathVariable String productCode, final Model model) throws CMSItemNotFoundException
	{
		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
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
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/writeReview", method = RequestMethod.POST)
	public String writeReview(@PathVariable String productCode, final ReviewForm form, final BindingResult result,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttrs)
			throws CMSItemNotFoundException
	{
		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
		String returnStatement = null;
		getReviewValidator().validate(form, result);

		final ProductModel productModel = productService.getProductForCode(productCode);

		if (result.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "review.general.error");
			populateProductDetailForDisplay(productModel, model, request);
			setUpReviewPage(model, productModel);
			returnStatement = ControllerConstants.Views.Pages.Product.WriteReview;
		}
		else
		{

			final ReviewData review = new ReviewData();
			review.setHeadline(XSSFilterUtil.filter(form.getHeadline()));
			review.setComment(XSSFilterUtil.filter(form.getComment()));
			review.setRating(form.getRating());
			review.setAlias(XSSFilterUtil.filter(form.getAlias()));
			productFacade.postReview(productCode, review);
			GlobalMessages
					.addFlashMessage(redirectAttrs, GlobalMessages.CONF_MESSAGES_HOLDER, "review.confirmation.thank.you.title");
			returnStatement = REDIRECT_PREFIX + productModelUrlResolver.resolve(productModel);
		}
		return returnStatement;



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
		//model.addAttribute(CMS_PAGE_TITLE,
		//		ModelAttributetConstants.Product_Page_Title.replace(ModelAttributetConstants.META_VARIABLE_ZERO, product.getName()));
		//TISPRD-5001 Reflecting seoTitle under <title> tag
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
			//CKD:TPR-250:Start
			prepareBrandInfoData(model, productData);
			//CKD:TPR-250:End
			//commented for UF-33
			//updatePageTitle(productData, model);
			final StringBuilder allVariants = new StringBuilder();
			//		sortVariantOptionData(productData);
			storeCmsPageInModel(model, getPageForProduct(productModel));
			populateProductData(productData, model);

			final List<String> deliveryInfo = new ArrayList<String>();

			deliveryInfo.add(ModelAttributetConstants.EXPRESS_DELIVERY);
			deliveryInfo.add(ModelAttributetConstants.HOME_DELIVERY);
			deliveryInfo.add(ModelAttributetConstants.CLICK_AND_COLLECT);

			/* delivery change */
			/* final Map<String, String> deliveryModeATMap = productDetailsHelper.getDeliveryModeATMap(deliveryInfo); */
			final Map<String, Map<String, Integer>> deliveryModeATMap = productDetailsHelper.getDeliveryModeATMap(deliveryInfo);
			model.addAttribute(ControllerConstants.Views.Fragments.Product.DELIVERY_MODE_MAP, deliveryModeATMap);
			//TPR-6654
			final String pincode = (String) sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			if (StringUtils.isNotEmpty(pincode))
			{
				model.addAttribute(ModelAttributetConstants.PINCODE,
						sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE));
				model.addAttribute(ControllerConstants.Views.Fragments.Product.STORE_AVAIL, mplProductFacade.storeLocatorPDP(pincode));
			}
			displayConfigurableAttribute(productData, model);
			//if (productModel.getProductCategoryType().equalsIgnoreCase(ELECTRONICS))
			if (ELECTRONICS.equalsIgnoreCase(productModel.getProductCategoryType())
					|| WATCHES.equalsIgnoreCase(productModel.getProductCategoryType()))
			{
				productDetailsHelper.groupGlassificationData(productData);
			}
			final String validTabs = configurationService.getConfiguration().getString(
					"mpl.categories." + productData.getRootCategory());

			final String sharePath = configurationService.getConfiguration().getString("social.share.path");
			//For Gigya
			final String isGigyaEnabled = configurationService.getConfiguration().getString(MessageConstants.USE_GIGYA);
			final String siteId = getSiteConfigService().getProperty("luxury.site.id");
			final String gigyaAPIKey;
			final String gigyaRatingURL;
			if ((getCmsSiteService().getCurrentSite().getUid()).equalsIgnoreCase(siteId))
			{

				gigyaAPIKey = configurationService.getConfiguration().getString("luxury.gigya.apikey");
				gigyaRatingURL = configurationService.getConfiguration().getString("luxury.gigya.rating.url");
			}
			else
			{
				gigyaAPIKey = configurationService.getConfiguration().getString("gigya.apikey");
				gigyaRatingURL = configurationService.getConfiguration().getString("gigya.rating.url");
			}
			//end gigya
			final String emiCuttOffAmount = configurationService.getConfiguration().getString("marketplace.emiCuttOffAmount");
			final String cliqCareNumber = configurationService.getConfiguration().getString("cliq.care.number");
			final String cliqCareMail = configurationService.getConfiguration().getString("cliq.care.mail");
			final String googleClientid = configurationService.getConfiguration().getString("google.data-clientid");
			final String facebookAppid = configurationService.getConfiguration().getString("facebook.app_id");

			/* Configurable tabs to be displayed in the PDP page */
			model.addAttribute(ModelAttributetConstants.VALID_TABS, validTabs);
			model.addAttribute(ModelAttributetConstants.SHARED_PATH, sharePath);
			final List<Breadcrumb> breadcrumbs = productBreadcrumbBuilder.getBreadcrumbs(productModel);
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);

			//UF-160
			final boolean isLargeApplnc = checkIfLargeAppliance(breadcrumbs);
			model.addAttribute(ModelAttributetConstants.IS_LARGE_APPLIANCE, isLargeApplnc);
			//UF-160 ends

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
			model.addAttribute(PRODUCT_SIZE_TYPE, productDetailsHelper.getSizeType(productModel));
			final String metaDescription = productData.getSeoMetaDescription();
			final String metaTitle = productData.getSeoMetaTitle();
			final String productCode = productData.getCode();
			final String metaKeyword = productData.getSeoMetaKeyword();
			setUpMetaData(model, metaDescription, metaTitle, productCode, metaKeyword);
			populateTealiumData(productData, model, breadcrumbs);

			/**
			 * Add Filter for FA START :::::
			 */
			boolean showSizeGuideForFA = true;
			if (ModelAttributetConstants.FASHION_ACCESSORIES.equalsIgnoreCase(productModel.getProductCategoryType()))
			{
				final Collection<CategoryModel> superCategories = productModel.getSupercategories();
				final String configurationFA = configurationService.getConfiguration().getString(
						"accessories.sideguide.category.showlist");
				final String[] configurationFAs = configurationFA.split(",");
				for (final CategoryModel supercategory : superCategories)
				{
					if (supercategory.getCode().startsWith("MPH"))
					{
						int num = 0;
						for (final String fashow : configurationFAs)
						{
							if (!supercategory.getCode().startsWith(fashow))
							{
								num++;
								if (num == configurationFAs.length)
								{
									showSizeGuideForFA = false;
									break;
								}
							}
						}

						break;
					}
				}


			}
			model.addAttribute("showSizeGuideForFA", showSizeGuideForFA);
			/**
			 * Add Filter for FA END :::::
			 */
			//TISPRM-56
			if (CollectionUtils.isNotEmpty(productData.getAllVariantsId()))
			{
				//get left over variants
				if (productData.getAllVariantsId().size() > 1)
				{
					productData.getAllVariantsId().remove(productData.getCode());
					for (final String variants : productData.getAllVariantsId())
					{
						allVariants.append(variants).append(',');
					}

					final int length = allVariants.length();
					final String allVariantsString = allVariants.substring(0, length - 1);
					model.addAttribute(MarketplacecommerceservicesConstants.ALLVARIANTSSTRING, allVariantsString);
				}
			}

			// TPR-743
			if (!ELECTRONICS.equalsIgnoreCase(productModel.getProductCategoryType()))
			{
				findCanonicalProduct(productData, model);
			}

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



	/**
	 * @param productData
	 * @param model
	 */
	private void findCanonicalProduct(final ProductData productData, final Model model)
	{
		if (CollectionUtils.isNotEmpty(productData.getVariantOptions()))
		{
			final List<VariantOptionData> variants = productData.getVariantOptions();
			final String canonicalUrl = variants.get(0).getUrl();
			model.addAttribute(ModelAttributetConstants.CANONICAL_URL, canonicalUrl);
		}
	}


	protected void setUpMetaData(final Model model, final String metaDescription, final String metaTitle,
			final String productCode, final String metaKeywords)
	{
		final List<MetaElementData> metadata = new LinkedList<>();
		metadata.add(createMetaElement(ModelAttributetConstants.DESCRIPTION, metaDescription));
		//TISPRD-4977
		if (null != metaKeywords)
		{
			metadata.add(createMetaElement(ModelAttributetConstants.KEYWORDS, metaKeywords));
		}
		//metadata.add(createMetaElement(ModelAttributetConstants.TITLE, metaTitle));
		metadata.add(createMetaElement("productCode", productCode));
		model.addAttribute(ModelAttributetConstants.METATAGS, metadata);
		model.addAttribute(ModelAttributetConstants.PMETATTITLE, metaTitle); //TISPRD-4977
		//Added for UF-33
		model.addAttribute(CMS_PAGE_TITLE, metaTitle);
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
		final Map<String, Map<String, String>> mapConfigurableAttributes = new LinkedHashMap<String, Map<String, String>>();
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
						final Map<String, String> productFeatureMap = new LinkedHashMap<String, String>();
						final List<FeatureValueData> featureValueList = new ArrayList<FeatureValueData>(featureData.getFeatureValues());
						//CKD:CAR-289
						/*
						 * final ProductFeatureModel productFeature =
						 * mplProductFacade.getProductFeatureModelByProductAndQualifier( productData, featureData.getCode());
						 */
						if (null != productData.getRootCategory())
						{
							final String properitsValue = configurationService.getConfiguration().getString(
									ModelAttributetConstants.CONFIGURABLE_ATTRIBUTE + productData.getRootCategory());
							final String descValues = configurationService.getConfiguration().getString(
									ModelAttributetConstants.DESC_PDP_PROPERTIES + productData.getRootCategory());
							//apparel
							final FeatureValueData featureValueData = featureValueList.get(0);
							if ((ModelAttributetConstants.CLOTHING.equalsIgnoreCase(productData.getRootCategory()))
									|| (ModelAttributetConstants.FOOTWEAR.equalsIgnoreCase(productData.getRootCategory())))
							{

								//								mapConfigurableAttribute.put(featureValueData.getValue(),
								//											productFeature != null && productFeature.getUnit() != null
								//													&& !productFeature.getUnit().getSymbol().isEmpty() ? productFeature.getUnit().getSymbol()
								//													: "");
								if (productFeatureMap.size() > 0)
								{
									productFeatureMap.clear();
								}
								if (null != properitsValue && featureValueData.getValue() != null
										&& properitsValue.toLowerCase().contains(featureData.getName().toLowerCase()))
								{
									//CKD:CAR-289
									final ProductFeatureModel productFeature = mplProductFacade
											.getProductFeatureModelByProductAndQualifier(productData, featureData.getCode());
									productFeatureMap.put(featureValueData.getValue(),
											productFeature != null && productFeature.getUnit() != null
													&& !productFeature.getUnit().getSymbol().isEmpty() ? productFeature.getUnit().getSymbol()
													: "");
									mapConfigurableAttributes.put(featureData.getName(), productFeatureMap);
								}
							} //end apparel
							  //electronics

							else if (ModelAttributetConstants.FASHION_ACCESSORIES.equalsIgnoreCase(productData.getRootCategory())
									|| ModelAttributetConstants.WATCHES.equalsIgnoreCase(productData.getRootCategory()))
							{
								final String[] propertiesValues = properitsValue.split(",");
								if (propertiesValues != null && propertiesValues.length > 0)
								{
									//CKD:CAR-289
									final ProductFeatureModel productFeature = mplProductFacade
											.getProductFeatureModelByProductAndQualifier(productData, featureData.getCode());
									for (final String value : propertiesValues)
									{
										if (value.equalsIgnoreCase(featureData.getName()))
										{
											if (productFeatureMap.size() > 0)
											{
												productFeatureMap.clear();
											}
											productFeatureMap.put(featureValueData.getValue(),
													productFeature != null && productFeature.getUnit() != null
															&& !productFeature.getUnit().getSymbol().isEmpty() ? productFeature.getUnit()
															.getSymbol() : "");
											mapConfigurableAttributes.put(featureData.getName(), productFeatureMap);
										}
									}
								}
								if (descValues != null && StringUtils.isNotBlank(descValues))
								{
									final String[] descValue = descValues.split(",");
									if (descValue != null && descValue.length > 0)
									{
										for (final String value : descValue)
										{
											if (value.equalsIgnoreCase(featureData.getName()))
											{
												mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
											}
										}
									}

								}

								if (featureData.getName().toLowerCase().contains(ModelAttributetConstants.WARRANTY.toLowerCase()))
								{
									warrentyList.add(featureValueData.getValue());
								}
							}
							else
							{
								if (StringUtils.isBlank(properitsValue.toLowerCase()))
								{
									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}
								if (properitsValue.toLowerCase().contains(configurableAttributData.getCode().toLowerCase()))

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
				model.addAttribute(ModelAttributetConstants.MAP_CONFIGURABLE_ATTRIBUTES, mapConfigurableAttributes);

			}
			else if (ModelAttributetConstants.FASHION_ACCESSORIES.equalsIgnoreCase(productData.getRootCategory())
					|| ModelAttributetConstants.WATCHES.equalsIgnoreCase(productData.getRootCategory()))
			{
				model.addAttribute(ModelAttributetConstants.MAP_CONFIGURABLE_ATTRIBUTES, mapConfigurableAttributes);
				final Map<String, String> treeMapConfigurableAttribute = new TreeMap<String, String>(mapConfigurableAttribute);
				model.addAttribute(ModelAttributetConstants.MAP_CONFIGURABLE_ATTRIBUTE, treeMapConfigurableAttribute);
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
		// For TPR-429
		final List<SellerInformationData> sellerInfoList = productData.getSeller();
		final List<String> sellerList = new ArrayList<String>();
		for (final SellerInformationData seller : sellerInfoList)
		{
			sellerList.add(seller.getSellerID());
		}
		model.addAttribute(ModelAttributetConstants.PDP_SELLER_IDS, sellerList);
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
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + RequestMappingUrlConstants.ADD_WISHLIST_IN_POPUP, method = RequestMethod.GET)
	//@RequireHardLogIn
	public boolean addWishListsForPDP(@RequestParam(ModelAttributetConstants.PRODUCT) final String productCode,
			@RequestParam(USSID) final String ussid, @RequestParam("wish") final String wishName,
			@RequestParam("sizeSelected") final String sizeSelected, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);

		boolean add = false;
		try
		{
			//add = productDetailsHelper.addToWishListInPopup(productCode, ussid, wishName, Boolean.valueOf(sizeSelected));
			add = productDetailsHelper.addSingleToWishList(productCode, ussid, Boolean.valueOf(sizeSelected));

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
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + RequestMappingUrlConstants.VIEW_WISHLISTS_IN_POPUP, method = RequestMethod.GET)
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
	@SuppressWarnings(BOXING)
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/buybox", method = RequestMethod.GET)
	public @ResponseBody JSONObject getBuyboxPrice(
			@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode,
			@RequestParam("variantCode") String variantCode,
			@RequestParam(value = MarketplacecommerceservicesConstants.SELLERIDPARAM, required = false) final String sellerId //CKD:TPR-250
	) throws JSONException, CMSItemNotFoundException, UnsupportedEncodingException, com.granule.json.JSONException
	{
		JSONObject buyboxJson = new JSONObject();
		buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.EMPTY);
		try
		{
			if (null != productCode)
			{
				if (!StringUtils.isEmpty(variantCode))
				{
					final List<String> variantCodes = (List<String>) JSON.parse(variantCode);
					variantCode = StringUtils.join(variantCodes, ",");
					productCode = productCode + "," + variantCode;
				}
				productCode = productCode.toUpperCase();
			}

			//CKD:TPR-250:Start : passing microsite seller Id
			//final Map<String, Object> buydata = buyBoxFacade.buyboxPricePDP(productCode);
			final Map<String, Object> buydata = buyBoxFacade.buyboxPricePDP(productCode, sellerId, "Web");
			//CKD:TPR-250:End
			//changes for tpr-1375,getting entire list of buybox data
			if (buydata != null)

			{
				final List<BuyBoxData> buyboxdata = (List<BuyBoxData>) buydata.get("buyboxList");
				buyboxJson = getPopulatedBuyBoxJson(buydata, buyboxJson);
				buyboxJson.put("buyboxList", buyboxdata);
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
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/getRichAttributes", method = RequestMethod.GET)
	public RichAttributeData getRichAttributesDetails(
			@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode,
			@RequestParam("buyboxid") final String buyboxid)
	{
		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
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
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + "-enlistEMIBanks", method = RequestMethod.GET)
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
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}

		return emiBankNames;
	}

	/**
	 * This is the GET method which fetches the bank terms for EMI mode of Payment
	 *
	 * @param selectedEMIBank
	 * @return List<EMITermRateData>
	 */
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + "-getTerms", method = RequestMethod.GET)
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
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + RequestMappingUrlConstants.CHECKUSER, method = RequestMethod.GET)
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
			flag = false;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			flag = false;
		}

		return flag;
	}



	/**
	 * @param deliveryMode
	 * @param ussid
	 * @return deliveryModeData
	 */
	/*
	 * private MarketplaceDeliveryModeData fetchDeliveryModeDataForUSSID(final String deliveryMode, final String ussid) {
	 *
	 * final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData(); final
	 * MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = mplCheckoutFacade
	 * .populateDeliveryCostForUSSIDAndDeliveryMode(deliveryMode, MarketplaceFacadesConstants.INR, ussid);
	 *
	 * final PriceData priceData = productDetailsHelper.formPriceData(mplZoneDeliveryModeValueModel.getValue());
	 * deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
	 * deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
	 * deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
	 * deliveryModeData.setSellerArticleSKU(ussid); deliveryModeData.setDeliveryCost(priceData); return deliveryModeData;
	 * }
	 */




	/**
	 * populating the request data to be send to oms
	 *
	 * @param productCode
	 * @return requestData
	 */
	/*
	 * private List<PincodeServiceData> populatePinCodeServiceData(final String productCode) {
	 *
	 *
	 *
	 * final List<PincodeServiceData> requestData = new ArrayList<>(); PincodeServiceData data = null;
	 *
	 * MarketplaceDeliveryModeData deliveryModeData = null; try { final ProductModel productModel =
	 *
	 *
	 * productService.getProductForCode(productCode); final ProductData productData =
	 *
	 * productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC, ProductOption.SELLER,
	 * ProductOption.PRICE));
	 *
	 *
	 * for (final SellerInformationData seller : productData.getSeller()) { final List<MarketplaceDeliveryModeData>
	 *
	 * deliveryModeList = new ArrayList<MarketplaceDeliveryModeData>(); data = new PincodeServiceData(); if ((null !=
	 *
	 * seller.getDeliveryModes()) && !(seller.getDeliveryModes().isEmpty())) { for (final MarketplaceDeliveryModeData
	 *
	 * deliveryMode : seller.getDeliveryModes()) { deliveryModeData =
	 *
	 * fetchDeliveryModeDataForUSSID(deliveryMode.getCode(), seller.getUssid()); deliveryModeList.add(deliveryModeData);
	 *
	 *
	 * } data.setDeliveryModes(deliveryModeList); } if (null != seller.getFullfillment() &&
	 *
	 * StringUtils.isNotEmpty(seller.getFullfillment())) {
	 *
	 * data.setFullFillmentType(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getFullfillment().toUpperCase())); }
	 *
	 * if (null != seller.getShippingMode() && (StringUtils.isNotEmpty(seller.getShippingMode()))) {
	 *
	 * data.setTransportMode(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getShippingMode().toUpperCase())); } if
	 *
	 * (null != seller.getSpPrice() && !(seller.getSpPrice().equals(ModelAttributetConstants.EMPTY))) { data.setPrice(new
	 *
	 * Double(seller.getSpPrice().getValue().doubleValue())); } else if (null != seller.getMopPrice() &&
	 *
	 * !(seller.getMopPrice().equals(ModelAttributetConstants.EMPTY))) { data.setPrice(new
	 *
	 * Double(seller.getMopPrice().getValue().doubleValue())); } else if (null != seller.getMrpPrice() &&
	 *
	 * !(seller.getMrpPrice().equals(ModelAttributetConstants.EMPTY))) { data.setPrice(new
	 *
	 * Double(seller.getMrpPrice().getValue().doubleValue())); } else {
	 *
	 *
	 *
	 * LOG.info("*************** No price avaiable for seller :" + seller.getSellerID()); continue; } if (null !=
	 *
	 *
	 * seller.getIsCod() && StringUtils.isNotEmpty(seller.getIsCod())) { data.setIsCOD(seller.getIsCod()); }
	 *
	 *
	 *
	 * data.setSellerId(seller.getSellerID()); data.setUssid(seller.getUssid());
	 *
	 * data.setIsDeliveryDateRequired(ControllerConstants.Views.Fragments.Product.N); requestData.add(data); } } catch
	 *
	 *
	 *
	 *
	 *
	 * (final EtailBusinessExceptions e) { ExceptionUtil.etailBusinessExceptionHandler(e, null); }
	 *
	 *
	 *
	 * catch (final Exception e) {
	 *
	 * throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000); } return requestData; }
	 */



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
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/otherSellerDetails", method = RequestMethod.GET)
	public List<SellerInformationData> getOtherSellerDetails(
			@PathVariable(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode) throws JSONException,
			CMSItemNotFoundException, UnsupportedEncodingException, com.granule.json.JSONException
	{
		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
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
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + "-sendEmail", method = RequestMethod.GET)
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

	@Override
	protected String checkRequestUrl(final HttpServletRequest request, final HttpServletResponse response,
			final String resolvedUrlPath) throws UnsupportedEncodingException
	{
		try
		{
			final String resolvedUrl = response.encodeURL(request.getContextPath() + resolvedUrlPath);
			final String requestURI = URIUtil.decode(request.getRequestURI(), "utf-8");
			final String decoded = URIUtil.decode(resolvedUrl, "utf-8");
			String newUrl = null;
			if (StringUtils.isNotEmpty(requestURI) && requestURI.endsWith(decoded))
			{
				return null;
			}
			else
			{
				//  org.springframework.web.servlet.View.RESPONSE_STATUS_ATTRIBUTE = "org.springframework.web.servlet.View.responseStatus"
				request.setAttribute("org.springframework.web.servlet.View.responseStatus", HttpStatus.MOVED_PERMANENTLY);
				final String queryString = request.getQueryString();
				if (queryString != null && !queryString.isEmpty())
				{
					newUrl = resolvedUrlPath + "?" + queryString;

				}
				else
				{
					newUrl = resolvedUrlPath;
				}
				return newUrl;


			}
		}
		catch (final URIException e)
		{
			throw new UnsupportedEncodingException();
		}
	}

	/**
	 * This is the GET method which fetches the bank last modified wishlist
	 *
	 *
	 * @return Wishlist2Model
	 */
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + "-getLastModifiedWishlistByUssid", method = RequestMethod.GET)
	public @ResponseBody boolean getLastModifiedWishlist(@RequestParam(USSID) final String ussid)
	{
		boolean existUssid = false;

		try
		{
			existUssid = productDetailsHelper.getLastModifiedWishlistByUssid(ussid);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}

		return existUssid;

	}

	/**
	 * @Description Added for displaying offer messages other than promotion, TPR-589: Displaying offer message from
	 *              offer models
	 * @param productCode
	 * @return buyboxJson
	 * @throws com.granule.json.JSONException
	 */
	@ResponseBody
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.PRODUCT_CODE_PATH_NEW_PATTERN + "/getOfferMessage", method = RequestMethod.GET)
	public JSONObject populateOfferMessage(
			@RequestParam(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) final String productCode)
			throws com.granule.json.JSONException
	{
		final JSONObject buyboxJson = new JSONObject();
		buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.EMPTY);
		try
		{
			if (StringUtils.isNotEmpty(productCode))
			{
				final Map<String, Map<String, String>> offerMessageMap = prodOfferDetFacade.showOfferMessage(productCode);


				// populate json with offer message
				if (MapUtils.isNotEmpty(offerMessageMap))
				{
					buyboxJson.put(ControllerConstants.Views.Fragments.Product.OFFERMESSAGEMAP, offerMessageMap);
				}
			}

		}
		catch (final EtailBusinessExceptions e)

		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.ERROR_OCCURED);
		}
		catch (

		final EtailNonBusinessExceptions e)

		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.ERROR_OCCURED);
		}
		return buyboxJson;
	}

	/**
	 * changes for TPR-1375 populating buybox details in json format
	 *
	 * @param buydata
	 * @param buyboxJson
	 * @throws com.granule.json.JSONException
	 */
	private JSONObject getPopulatedBuyBoxJson(final Map<String, Object> buydata, final JSONObject buyboxJson)
			throws com.granule.json.JSONException
	{
		// YTODO Auto-generated method stub
		final BuyBoxData buyboxdata = (BuyBoxData) buydata.get("pdp_buy_box");
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

		buyboxJson.put("isOOsForMicro", buydata.get("isOOsForMicro"));//TPR-250

		//	 TISRLEE-1586 03-01-2017
		buyboxJson.put(ControllerConstants.Views.Fragments.Product.ID_ED_SELLER_HANDLING_TIME, buyboxdata.isIsSellerHandlingTime());

		final Map<String, Integer> stockAvailibilty = new TreeMap<String, Integer>();
		final List<String> noStockPCodes = (List<String>) buydata.get("no_stock_p_codes");
		for (final String pCode : noStockPCodes)
		{
			stockAvailibilty.put(pCode, Integer.valueOf(0));
		}
		buyboxJson.put(ControllerConstants.Views.Fragments.Product.AVAILABILITY, stockAvailibilty);
		//TISPRM-33
		if (null != buyboxdata.getMrp())
		{
			if (buyboxdata.getSpecialPrice() != null && buyboxdata.getSpecialPrice().getValue().doubleValue() > 0)
			{
				final double savingPriceCal = buyboxdata.getMrp().getDoubleValue().doubleValue()
						- buyboxdata.getSpecialPrice().getDoubleValue().doubleValue();
				final double savingPriceCalPer = (savingPriceCal / buyboxdata.getMrp().getDoubleValue().doubleValue()) * 100;
				//Critical Sonar Fix
				//fix for INC144314955
				//final double roundedOffValuebefore = Math.round(savingPriceCalPer * 100.0) / 100.0;
				final double roundedOffValuebefore = Math.floor((savingPriceCalPer * 100.0) / 100.0);
				final BigDecimal roundedOffValue = new BigDecimal((int) roundedOffValuebefore);

				buyboxJson.put(ControllerConstants.Views.Fragments.Product.SAVINGONPRODUCT, roundedOffValue);
			}
			else if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getValue().doubleValue() > 0)
			{
				final double savingPriceCal = buyboxdata.getMrp().getDoubleValue().doubleValue()
						- buyboxdata.getPrice().getDoubleValue().doubleValue();
				final double savingPriceCalPer = (savingPriceCal / buyboxdata.getMrp().getDoubleValue().doubleValue()) * 100;
				//Critical Sonar Fix
				//fix for INC144314955
				//final double roundedOffValuebefore = Math.round(savingPriceCalPer * 100.0) / 100.0;
				final double roundedOffValuebefore = Math.floor((savingPriceCalPer * 100.0) / 100.0);
				final BigDecimal roundedOffValue = new BigDecimal((int) roundedOffValuebefore);
				buyboxJson.put(ControllerConstants.Views.Fragments.Product.SAVINGONPRODUCT, roundedOffValue);
			}
		}
		return buyboxJson;
	}

	//TPR-978
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + "-fetchPageContents", method = RequestMethod.GET)
	public String fetchPageContents(@RequestParam(value = "productCode") String productCode, final Model model)
			throws com.granule.json.JSONException
	{

		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
		//System.out.println("**************************************fetchPageContents*************" + productCode);
		final ProductModel productModel = productService.getProductForCode(productCode);
		ContentPageModel contentPage = null;
		List<String> contentList = null;
		List<String> imageList = null;
		List<String> videoList = null;

		//Added for status 500 errors INC_11128
		//Return this view when no Content Page is found for the Product
		String returnString = "/pages/layout/productContentblank";
		final Map<String, ProductContentData> productContentDataMap = new HashMap<String, ProductContentData>();
		try
		{
			contentPage = getContentPageForProduct(productModel);
			if (null != contentPage)
			{
				for (final ContentSlotForPageModel contentSlotForPageModel : contentPage.getContentSlots())
				{
					final ProductContentData productContentData = new ProductContentData();
					productContentData.setSection(contentSlotForPageModel.getPosition());

					contentList = new ArrayList<String>();
					imageList = new ArrayList<String>();
					videoList = new ArrayList<String>();

					for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlotForPageModel.getContentSlot()
							.getCmsComponents())
					{

						if (abstractCMSComponentModel instanceof CMSParagraphComponentModel)
						{
							final CMSParagraphComponentModel paragraphComponent = (CMSParagraphComponentModel) abstractCMSComponentModel;
							contentList.add(paragraphComponent.getContent());
						}

						if (abstractCMSComponentModel instanceof CMSImageComponentModel)
						{
							final CMSImageComponentModel cmsImageComponent = (CMSImageComponentModel) abstractCMSComponentModel;
							imageList.add(cmsImageComponent.getMedia().getUrl2());
						}

						if (abstractCMSComponentModel instanceof SimpleBannerComponentModel)
						{
							final SimpleBannerComponentModel bannerComponent = (SimpleBannerComponentModel) abstractCMSComponentModel;
							if (bannerComponent.getMedia() != null && StringUtils.isNotEmpty(bannerComponent.getMedia().getUrl2()))
							{
								imageList.add(bannerComponent.getMedia().getUrl2());
							}
							else if (StringUtils.isNotEmpty(bannerComponent.getUrlLink()))
							{
								imageList.add(bannerComponent.getUrlLink());

							}
						}

						if (abstractCMSComponentModel instanceof VideoComponentModel)
						{
							final VideoComponentModel bannerComponent = (VideoComponentModel) abstractCMSComponentModel;
							videoList.add(bannerComponent.getVideoUrl());
						}

					}

					productContentData.setTextList(contentList);
					productContentData.setImageList(imageList);
					productContentData.setVideoList(videoList);
					productContentDataMap.put(contentSlotForPageModel.getPosition(), productContentData);

				}

				//INC_11128
				model.addAttribute("productContentDataMap", productContentDataMap);
				storeCmsPageInModel(model, getContentPageForLabelOrId(contentPage.getUid()));
				returnString = "/pages/" + contentPage.getMasterTemplate().getFrontendTemplateName();


			} //final end of if
			  //INC_11128
			  //commented as returned inside the if block
			  //storeCmsPageInModel(model, getContentPageForLabelOrId(contentPage.getUid()));
		}
		catch (final CMSItemNotFoundException e)
		{

			LOG.error("CMS Item error while fetching A+ content", e);

		}


		//INC_11128
		//commented as returned inside the if block
		//return "/pages/" + contentPage.getMasterTemplate().getFrontendTemplateName();
		return returnString;

	}

	private ContentPageModel getContentPageForProduct(final ProductModel product) throws CMSItemNotFoundException
	{
		//INC_11128
		//Commented for status 500 errors
		//		if (productContentPage == null)
		//		{
		//			throw new CMSItemNotFoundException("Could not find a product content for the product" + product.getName());
		//		}


		return mplCmsPageService.getContentPageForProduct(product);
	}


	/**
	 * @description method is to remove products in wishlist in in pdp
	 * @param productCode
	 * @param model
	 * @param request
	 * @param response
	 * @throws CMSItemNotFoundException
	 */
	@ResponseBody
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + "-removeFromWl", method = RequestMethod.GET)
	//@RequireHardLogIn
	public boolean removeFromWl(@RequestParam(ModelAttributetConstants.PRODUCT) final String productCode,
			@RequestParam(USSID) final String ussid, @RequestParam("wish") final String wishName, final Model model)
			throws CMSItemNotFoundException
	{
		model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);

		boolean remove = false;
		try
		{
			remove = productDetailsHelper.removeFromWishList(productCode, ussid);

		}
		catch (final EtailBusinessExceptions e)
		{

			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}

		return remove;


	}


	/**
	 * This is used to verify the configured MPH category product can get SizeGuide & choose size
	 *
	 * @param productModel
	 */

	public void showSizeGuideForFA(final ProductModel productModel, final Model model)
	{
		boolean showSizeGuideForFA = true;
		//AKAMAI fix
		if (productModel instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel variantProductModel = (PcmProductVariantModel) productModel;


			if (ModelAttributetConstants.FASHION_ACCESSORIES.equalsIgnoreCase(variantProductModel.getProductCategoryType()))
			{
				final Collection<CategoryModel> superCategories = variantProductModel.getSupercategories();
				final String configurationFA = configurationService.getConfiguration().getString(
						"accessories.sideguide.category.showlist");
				final String[] configurationFAs = configurationFA.split(",");
				for (final CategoryModel supercategory : superCategories)
				{
					if (supercategory.getCode().startsWith("MPH"))
					{
						int num = 0;
						for (final String fashow : configurationFAs)
						{
							if (!supercategory.getCode().startsWith(fashow))
							{
								num++;
								if (num == configurationFAs.length)
								{
									showSizeGuideForFA = false;
									break;
								}
							}
						}

						break;
					}
				}


			}
			model.addAttribute("showSizeGuideForFA", showSizeGuideForFA);

		}
	}


	//TPR-3736
	/**
	 * @param ussids
	 * @return dataMap
	 * @throws JSONException
	 * @throws com.granule.json.JSONException
	 */
	@SuppressWarnings(BOXING)
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + "-getIAResponse", method = RequestMethod.GET)
	public @ResponseBody JSONObject getIAResponse(@RequestParam(IA_USS_IDS) final String ussids) throws JSONException,
			com.granule.json.JSONException
	{
		final String ussidList[] = ussids.split(",");
		final StringBuilder ussidIds = new StringBuilder();
		for (int i = 0; i < ussidList.length; i++)
		{
			ussidIds.append(MarketplacecommerceservicesConstants.INVERTED_COMMA);
			ussidIds.append(ussidList[i]);
			ussidIds.append(MarketplacecommerceservicesConstants.INVERTED_COMMA);
			ussidIds.append(',');//Sonar fix
		}
		final JSONObject buyboxJson = new JSONObject();
		final Map<String, List<Double>> dataMap = buyBoxFacade.getBuyBoxDataForUssids(ussidIds.toString().substring(0,
				ussidIds.lastIndexOf(",")));
		LOG.debug("##################Data Map for IA" + dataMap);
		return buyboxJson.put("iaResponse", dataMap);
	}

	/**
	 * @Description Added for displaying freebie messages other than default freebie message
	 * @param ussId
	 * @return populateFreebieMessage
	 * @throws com.granule.json.JSONException
	 */

	//update the message for Freebie product TPR-1754

	@ResponseBody
	@RequestMapping(value = ControllerConstants.Views.Fragments.Product.USSID_CODE_PATH_NEW_PATTERN + "/getFreebieMessage", method = RequestMethod.GET)
	public JSONObject populateFreebieMessage(@RequestParam(ControllerConstants.Views.Fragments.Product.USSID) final String ussId)
			throws com.granule.json.JSONException, EtailNonBusinessExceptions, FlexibleSearchException, UnknownIdentifierException
	{
		final JSONObject buyboxJson = new JSONObject();
		buyboxJson.put(ModelAttributetConstants.ERR_MSG, ModelAttributetConstants.EMPTY);
		try
		{
			if (StringUtils.isNotEmpty(ussId))
			{
				//				final Map<String, Map<String, String>> offerMessageMap = prodOfferDetFacade.showFreebieMessage(ussId);

				final Map<String, String> offerMessageMap = prodOfferDetFacade.showFreebieMessage(ussId);


				// populate json with offer message
				if (MapUtils.isNotEmpty(offerMessageMap))
				{
					buyboxJson.put(ControllerConstants.Views.Fragments.Product.OFFERMESSAGEMAP, offerMessageMap);

				}
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

	/**
	 * @param model
	 * @param productData
	 */
	//CKD:TPR-250: Start

	private void prepareBrandInfoData(final Model model, final ProductData productData)
	{
		if (null != productData.getBrand())
		{
			model.addAttribute("msiteBrandName", StringUtils.isNotBlank(productData.getBrand().getBrandname()) ? productData
					.getBrand().getBrandname().toLowerCase() : null);
			/*
			 * model.addAttribute("msiteBrandCode", StringUtils.isNotBlank(productData.getBrand().getBrandCode()) ?
			 * productData .getBrand().getBrandCode().toLowerCase() : null);
			 */
			model.addAttribute("msiteBrandCode", getBrandCodeFromSuperCategories(productData.getBrand().getBrandCode()));

			if (null != productData.getBrand().getBrandDescription())
			{
				model.addAttribute(
						"brandInfo",
						productData.getBrand().getBrandDescription().length() <= MplConstants.BRANDINFO_CHAR_LIMIT ? productData
								.getBrand().getBrandDescription() : StringUtils.substring(productData.getBrand().getBrandDescription(),
								0, MplConstants.BRANDINFO_CHAR_LIMIT));
			}

		}
	}

	/**
	 * PCM will send hierarchies together in brandcode field of the brand feed, this method will extract the brand
	 * hierarchy code alone
	 *
	 * @param superCategories
	 * @return brandCode
	 */
	private String getBrandCodeFromSuperCategories(final String superCategories)
	{
		String[] superCatArray = null;
		String brandCode = null;
		if (StringUtils.isNotBlank(superCategories))
		{
			superCatArray = superCategories.split(MplConstants.COMMA);
			for (final String superCat : superCatArray)
			{
				if (superCat.toUpperCase().startsWith(MplConstants.BRAND_HIERARCHY_ROOT_CATEGORY_CODE))
				{
					brandCode = superCat;
					break;
				}
			}
		}
		return brandCode;
	}

	//CKD:TPR-250: End

	@SuppressWarnings(BOXING)
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + "-ajaxProductData", method = RequestMethod.GET)
	public String getAjaxProductDataForProductCode(
			@RequestParam(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode, final Model model)
	{
		String returnStatement = null;
		MplAjaxProductData mplAjaxProductData = null;
		final Gson gson = new Gson();
		try
		{
			if (null != productCode)
			{
				productCode = productCode.toUpperCase();
			}
			LOG.debug("***************************ajaxProductData call for*************" + productCode);
			final ProductModel productModel = productService.getProductForCode(productCode);
			mplAjaxProductData = new MplAjaxProductData();
			final ProductData productData = customProductFacade.getProductForAjaxOptions(productModel,
					Arrays.asList(ProductOption.PROMOTIONS));
			mplAjaxProductData.setCode(productData.getCode());
			mplAjaxProductData.setName(productData.getName());
			mplAjaxProductData.setUrl(productData.getUrl());
			mplAjaxProductData.setArticleDescription(productData.getArticleDescription());
			if (productModel instanceof PcmProductVariantModel)
			{
				final PcmProductVariantModel variantProductModel = (PcmProductVariantModel) productModel;
				mplAjaxProductData.setProductSize(variantProductModel.getSize());
			}
			mplAjaxProductData.setPotentialPromotions(productData.getPotentialPromotions());
			final List<SellerInformationData> sellerInfoList = productData.getSeller();
			final List<String> sellerList = new ArrayList<String>();
			for (final SellerInformationData seller : sellerInfoList)
			{
				sellerList.add(seller.getSellerID());
			}
			mplAjaxProductData.setPdpSellerIDs(sellerList);
			//UF-33 starts//  //TISSTRT-1587//
			final String metaTitle = productData.getSeoMetaTitle();
			final Map<String, String> seoMap = new HashMap<String, String>();
			seoMap.put("metaTitle", metaTitle);
			mplAjaxProductData.setMapConfigurableAttribute(seoMap);
			//UF-33 ends//
			//			final String msdRESTURL = configurationService.getConfiguration().getString("msd.rest.url");
			//			mplAjaxProductData.setMsdRESTURL(msdRESTURL);
			model.addAttribute(ModelAttributetConstants.PRODUCT, productData);
			//TPR-6654
			final String pincode = (String) sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			if (StringUtils.isNotEmpty(pincode))
			{
				model.addAttribute(ControllerConstants.Views.Fragments.Product.STORE_AVAIL, mplProductFacade.storeLocatorPDP(pincode));
				mplAjaxProductData.setPincode(pincode);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			mplAjaxProductData.setError(e.getErrorMessage());
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			mplAjaxProductData.setError(e.getErrorMessage());
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			mplAjaxProductData.setError(e.getMessage());
		}
		finally
		{
			model.addAttribute("ajaxData", gson.toJson(mplAjaxProductData));
			returnStatement = ControllerConstants.Views.Fragments.Product.AJAXPRODUCTDATA;
		}
		return returnStatement;
	}

	@SuppressWarnings(BOXING)
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + "-productClassAttribs", method = RequestMethod.GET)
	public @ResponseBody String getAjaxProductClassAttribs(
			@RequestParam(ControllerConstants.Views.Fragments.Product.PRODUCT_CODE) String productCode, final Model model)
			throws com.granule.json.JSONException
	{
		LOG.debug("***************************productClassAttribs call for*************" + productCode);
		String returnStatement = null;
		final Gson gson = new Gson();
		MplAjaxProductData mplAjaxProductData = null;
		try
		{
			if (null != productCode)
			{
				productCode = productCode.toUpperCase();
			}
			final ProductModel productModel = productService.getProductForCode(productCode);
			final ProductData productData = customProductFacade.getProductForAjaxOptions(productModel,
					Arrays.asList(ProductOption.CATEGORIES, ProductOption.CLASSIFICATION));
			mplAjaxProductData = new MplAjaxProductData();
			displayConfigurableAttribute(productData, model);
			final String validTabs = configurationService.getConfiguration().getString(
					"mpl.categories." + productData.getRootCategory());
			mplAjaxProductData.setValidTabs(validTabs);
			mplAjaxProductData.setMapConfigurableAttribute((Map<String, String>) model.asMap().get(
					ModelAttributetConstants.MAP_CONFIGURABLE_ATTRIBUTE));
			mplAjaxProductData.setMapConfigurableAttributes((Map<String, Map>) model.asMap().get(
					ModelAttributetConstants.MAP_CONFIGURABLE_ATTRIBUTES));
			mplAjaxProductData.setWarranty((List<String>) model.asMap().get(ModelAttributetConstants.WARRANTY));
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			mplAjaxProductData.setError(e.getErrorMessage());
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			mplAjaxProductData.setError(e.getErrorMessage());
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			mplAjaxProductData.setError(e.getMessage());
		}
		finally
		{
			returnStatement = gson.toJson(mplAjaxProductData);
		}
		return returnStatement;
	}

	private boolean isLuxurySite()
	{

		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		if (null != currentBaseSite && StringUtils.isNotBlank(currentBaseSite.getUid())
				&& MarketplaceFacadesConstants.LuxuryPrefix.equals(currentBaseSite.getUid()))
		{
			return true;
		}
		return false;
	}


	/**
	 * This is used to check if the product is a Large Appliance for UF-160
	 *
	 * @param breadcrumbs
	 */
	private boolean checkIfLargeAppliance(final List<Breadcrumb> breadcrumbs)
	{
		boolean isLargeAppliance = false;
		final String largeApplncCatCode = configurationService.getConfiguration().getString("categories.largeAppliance.code");
		if (StringUtils.isNotEmpty(largeApplncCatCode))
		{
			final List<String> catCodeList = Arrays.asList(largeApplncCatCode.split(","));

			for (final Breadcrumb br : breadcrumbs)
			{
				final String catCode = br.getCategoryCode();
				if (catCodeList.contains(catCode))
				{
					isLargeAppliance = true;
					break;
				}
			}
		}
		return isLargeAppliance;

	}

	/**
	 * @desc Returns nearby store location based on pincode.
	 *
	 * @return Store details
	 */
	@RequestMapping(value = PRODUCT_OLD_URL_PATTERN + ControllerConstants.Views.Fragments.Product.STORE, method = RequestMethod.GET)
	public String getAllStoreForPincode(
			@PathVariable(value = ControllerConstants.Views.Fragments.Product.PINCODE) final String pincode, final Model model)

	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getAllStoreForPincode method");
		}
		try
		{
			if (StringUtils.isNotEmpty(pincode))
			{
				model.addAttribute(ModelAttributetConstants.PINCODE,
						sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE));
				model.addAttribute(ControllerConstants.Views.Fragments.Product.STORE_AVAIL, mplProductFacade.storeLocatorPDP(pincode));
			}
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}

		return ControllerConstants.Views.Fragments.Product.StoreLocatorPopup;
	}
}
