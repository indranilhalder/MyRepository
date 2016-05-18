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

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.ShowCaseLayout;
import com.tisl.mpl.core.model.MplShowcaseComponentModel;
import com.tisl.mpl.core.model.MplShowcaseItemComponentModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.brand.BrandFacade;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.marketplacecommerceservices.service.HomepageComponentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.util.CSRFTokenManager;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Controller for home page
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.ROOT)
public class HomePageController extends AbstractPageController
{

	@Resource
	private ModelService modelService;

	@Resource(name = "brandFacade")
	private BrandFacade brandFacade;

	@Resource(name = "cmsPageService")
	private MplCmsPageService cmsPageService;

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;


	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;

	@Resource(name = "homepageComponentService")
	private HomepageComponentService homepageComponentService;

	private static final String VERSION = "version";
	private static final String HOMEPAGE = "homepage";
	private static final String TITLE = "title";


	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	//store url changes
	private static final String MISSING_IMAGE_URL = "/_ui/desktop/theme-blue/images/missing-product-300x300.jpg";

	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.GALLERY);

	private static final String EMPTY_STRING = "";

	private static final String EXCEPTION_MESSAGE_PRICE = "Exception to fetch price for product code";

	private static final String EXCEPTION_MESSAGE_SHOWCASE = "Exception  in getJSONForShowCaseItem";

	private static final String EXCEPTION_MESSAGE_NEWEXCLUSIVE = "Exception in getNewAndExclusive";

	private static final String userFirstName = "userFirstName";

	/**
	 * @description this is called to load home page
	 * @param logout
	 * @param model
	 * @param redirectModel
	 * @return getViewForPage
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String home(
			@RequestParam(value = ModelAttributetConstants.LOGOUT, defaultValue = ModelAttributetConstants.FALSE) final boolean logout,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (logout)
		{
			//GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, "account.confirmation.signout.title");
			return REDIRECT_PREFIX + ROOT;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(null));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
		updatePageTitle(model, getContentPageForLabelOrId(null));

		return getViewForPage(model);
	}

	/**
	 * @description this is called to update the page title
	 * @param model
	 * @param cmsPage
	 */
	protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveHomePageTitle(cmsPage.getTitle()));
	}



	@ResponseBody
	@RequestMapping(value = "/getBrandsYouLove", method = RequestMethod.GET)
	public JSONObject getBrandsYouLove(@RequestParam(VERSION) final String version)
	{
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		JSONObject brandsYouLoveJson = new JSONObject();
		try
		{
			final ContentSlotModel homepageSection3Slot = cmsPageService.getContentSlotByUidForPage(HOMEPAGE,
					"Section3Slot-Homepage", version);


			if (CollectionUtils.isNotEmpty(homepageSection3Slot.getCmsComponents()))
			{
				components = homepageSection3Slot.getCmsComponents();
			}


			for (final AbstractCMSComponentModel component : components)
			{
				LOG.info("Found Component>>>>with id :::" + component.getUid());

				if (component instanceof MplShowcaseComponentModel)
				{
					final MplShowcaseComponentModel brandsYouLoveComponent = (MplShowcaseComponentModel) component;
					brandsYouLoveJson = getJSONForShowcaseComponent(brandsYouLoveComponent);
				}
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


		return brandsYouLoveJson;

	}

	/**
	 * @param showCaseComponent
	 */
	private JSONObject getJSONForShowcaseComponent(final MplShowcaseComponentModel showCaseComponent)
	{
		final JSONObject showCaseComponentJson = new JSONObject();
		String title = EMPTY_STRING;
		if (StringUtils.isNotEmpty(showCaseComponent.getTitle()))
		{
			title = showCaseComponent.getTitle();
		}
		showCaseComponentJson.put(TITLE, title);
		final JSONArray subComponentJsonArray = new JSONArray();

		if (CollectionUtils.isNotEmpty(showCaseComponent.getShowcaseItems()))
		{

			String brandLogoUrl = EMPTY_STRING;
			for (final MplShowcaseItemComponentModel showcaseItem : showCaseComponent.getShowcaseItems())
			{
				final JSONObject showCaseItemJson = new JSONObject();
				showCaseItemJson.put("compId", showcaseItem.getUid());
				showCaseItemJson.put("showByDefault", showcaseItem.getShowByDefault());
				if (null != showCaseComponent.getLayout() && showCaseComponent.getLayout().equals(ShowCaseLayout.BRANDSHOWCASE))
				{
					if (null != showcaseItem.getLogo() && StringUtils.isNotEmpty(showcaseItem.getLogo().getURL()))
					{
						brandLogoUrl = showcaseItem.getLogo().getURL();
					}
					showCaseItemJson.put("brandLogoUrl", brandLogoUrl);
				}
				else
				{
					String headerText = EMPTY_STRING;
					if (StringUtils.isNotEmpty(showcaseItem.getHeaderText()))
					{
						headerText = showcaseItem.getHeaderText();
					}
					showCaseItemJson.put("headerText", headerText);
				}
				subComponentJsonArray.add(showCaseItemJson);
			}
		}

		showCaseComponentJson.put("subComponents", subComponentJsonArray);

		return showCaseComponentJson;


	}

	@ResponseBody
	@RequestMapping(value = "/getBrandsYouLoveContent", method = RequestMethod.GET)
	public JSONObject getBrandsYouLoveContent(@RequestParam(value = "id") final String componentId)
	{
		MplShowcaseItemComponentModel showcaseItem = null;
		JSONObject showCaseItemJson = new JSONObject();
		LOG.info("Finding component with id::::" + componentId);
		try
		{

			showcaseItem = (MplShowcaseItemComponentModel) cmsComponentService.getSimpleCMSComponent(componentId);
			LOG.info("Found component with id::::" + componentId);

			showCaseItemJson = getJSONForShowCaseItem(showcaseItem, ShowCaseLayout.BRANDSHOWCASE);

		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(e.getStackTrace());
			LOG.error("Could not find component with id::::" + componentId);

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
		return showCaseItemJson;
	}


	/**
	 * @param showcaseItem
	 * @param brandshowcase
	 * @return
	 */
	private JSONObject getJSONForShowCaseItem(final MplShowcaseItemComponentModel showcaseItem, final ShowCaseLayout showcaseLayout)
	{
		final JSONObject showCaseItemJson = new JSONObject();
		ProductData firstProduct = null;
		ProductData secondProduct = null;
		try
		{
			if (showcaseItem.getProduct1() != null)
			{

				firstProduct = productFacade.getProductForOptions(showcaseItem.getProduct1(), PRODUCT_OPTIONS);
				showCaseItemJson.put("firstProductImageUrl", getProductPrimaryImageUrl(firstProduct));
				showCaseItemJson.put("firstProductTitle", firstProduct.getProductTitle());
				showCaseItemJson.put("firstProductUrl", firstProduct.getUrl());
				String price = null;
				try
				{
					price = getProductPrice(firstProduct);
				}
				catch (final EtailBusinessExceptions e)
				{
					price = EMPTY_STRING;
					LOG.error(EXCEPTION_MESSAGE_PRICE + firstProduct.getCode());
				}
				catch (final EtailNonBusinessExceptions e)
				{
					price = EMPTY_STRING;
					LOG.error(EXCEPTION_MESSAGE_PRICE + firstProduct.getCode());
				}
				catch (final Exception e)
				{
					price = EMPTY_STRING;
					LOG.error(EXCEPTION_MESSAGE_PRICE + firstProduct.getCode());
				}
				showCaseItemJson.put("firstProductPrice", price);

			}
			if (null != showcaseLayout && showcaseLayout.equals(ShowCaseLayout.BRANDSHOWCASE))
			{
				if (showcaseItem.getProduct2() != null)
				{
					secondProduct = productFacade.getProductForOptions(showcaseItem.getProduct2(), PRODUCT_OPTIONS);
					showCaseItemJson.put("secondproductImageUrl", getProductPrimaryImageUrl(secondProduct));
					showCaseItemJson.put("secondProductTitle", secondProduct.getProductTitle());
					showCaseItemJson.put("secondProductUrl", secondProduct.getUrl());
					String price = null;
					try
					{
						price = getProductPrice(secondProduct);
					}
					catch (final EtailBusinessExceptions e)
					{
						price = EMPTY_STRING;
						LOG.error(EXCEPTION_MESSAGE_PRICE + secondProduct.getCode());
					}
					catch (final EtailNonBusinessExceptions e)
					{
						price = EMPTY_STRING;
						LOG.error(EXCEPTION_MESSAGE_PRICE + secondProduct.getCode());
					}
					catch (final Exception e)
					{
						price = EMPTY_STRING;
						LOG.error(EXCEPTION_MESSAGE_PRICE + secondProduct.getCode());
					}
					showCaseItemJson.put("secondProductPrice", price);
				}
				if (StringUtils.isNotEmpty(showcaseItem.getBannerText()))
				{
					showCaseItemJson.put("bannerText", showcaseItem.getBannerText());
				}
			}
			if (StringUtils.isNotEmpty(showcaseItem.getText()))
			{
				showCaseItemJson.put("text", showcaseItem.getText());
			}

			if (null != showcaseItem.getBannerImage() && StringUtils.isNotEmpty(showcaseItem.getBannerImage().getURL()))
			{
				showCaseItemJson.put("bannerImageUrl", showcaseItem.getBannerImage().getURL());
			}

			if (null != showcaseItem.getBannerUrl() && StringUtils.isNotEmpty(showcaseItem.getBannerUrl()))
			{
				showCaseItemJson.put("bannerUrl", showcaseItem.getBannerUrl());
			}

			showCaseItemJson.put("icid", showcaseItem.getPk().getLongValueAsString());
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(EXCEPTION_MESSAGE_SHOWCASE, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(EXCEPTION_MESSAGE_SHOWCASE, e);
		}
		catch (final Exception e)
		{
			LOG.error(EXCEPTION_MESSAGE_SHOWCASE, e);
		}
		return showCaseItemJson;
	}

	@ResponseBody
	@RequestMapping(value = "/getBestPicks", method = RequestMethod.GET)
	public JSONObject getBestPicks(@RequestParam(VERSION) final String version)
	{
		JSONObject getBestPicksJson = new JSONObject();
		try
		{
			final ContentSlotModel homepageSection4CSlot = cmsPageService.getContentSlotByUidForPage(HOMEPAGE,
					"Section4CSlot-Homepage", version);
			//return homepageComponentService.getBestPicksJSON(homepageSection4CSlot);
			getBestPicksJson = homepageComponentService.getBestPicksJSON(homepageSection4CSlot);
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
		return getBestPicksJson;
	}

	@ResponseBody
	@RequestMapping(value = "/getProductsYouCare", method = RequestMethod.GET)
	public JSONObject getProductsYouCare(@RequestParam(VERSION) final String version)
	{
		JSONObject getProductsYouCareJson = new JSONObject();
		try
		{
			final ContentSlotModel homepageSection4DSlot = cmsPageService.getContentSlotByUidForPage(HOMEPAGE,
					"Section4DSlot-Homepage", version);
			getProductsYouCareJson = homepageComponentService.getProductsYouCareJSON(homepageSection4DSlot);
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
		return getProductsYouCareJson;
	}



	@ResponseBody
	@RequestMapping(value = "/getNewAndExclusive", method = RequestMethod.GET)
	public JSONObject getNewAndExclusive(@RequestParam(VERSION) final String version)
	{
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		final JSONObject newAndExclusiveJson = new JSONObject();
		try
		{
			final ContentSlotModel homepageSection4BSlot = cmsPageService.getContentSlotByUidForPage(HOMEPAGE,
					"Section4BSlot-Homepage",

					version);
			if (CollectionUtils.isNotEmpty(homepageSection4BSlot.getCmsComponents()))
			{
				components = homepageSection4BSlot.getCmsComponents();
			}


			for (final AbstractCMSComponentModel component : components)
			{
				LOG.info("Found Component>>>>with id :::" + component.getUid());

				if (component instanceof ProductCarouselComponentModel)
				{
					final ProductCarouselComponentModel newAndExclusiveComponent = (ProductCarouselComponentModel) component;

					String title = EMPTY_STRING;
					if (StringUtils.isNotEmpty(newAndExclusiveComponent.getTitle()))
					{
						title = newAndExclusiveComponent.getTitle();
					}
					newAndExclusiveJson.put(TITLE, title);
					final JSONArray newAndExclusiveJsonArray = new JSONArray();
					if (CollectionUtils.isNotEmpty(newAndExclusiveComponent.getProducts()))
					{
						for (final ProductModel newAndExclusiveProducts : newAndExclusiveComponent.getProducts())
						{

							final JSONObject newAndExclusiveProductJson = new JSONObject();
							ProductData product = null;
							product = productFacade.getProductForOptions(newAndExclusiveProducts, PRODUCT_OPTIONS);
							newAndExclusiveProductJson.put("productImageUrl", getProductPrimaryImageUrl(product));
							newAndExclusiveProductJson.put("productTitle", product.getProductTitle());
							newAndExclusiveProductJson.put("productUrl", product.getUrl());
							String price = null;
							try
							{
								price = getProductPrice(product);
							}
							catch (final EtailBusinessExceptions e)
							{
								price = EMPTY_STRING;
								LOG.error(EXCEPTION_MESSAGE_PRICE + product.getCode());
							}
							catch (final EtailNonBusinessExceptions e)
							{
								price = EMPTY_STRING;
								LOG.error(EXCEPTION_MESSAGE_PRICE + product.getCode());
							}
							catch (final Exception e)
							{
								price = EMPTY_STRING;
								LOG.error(EXCEPTION_MESSAGE_PRICE + product.getCode());
							}
							newAndExclusiveProductJson.put("productPrice", price);
							newAndExclusiveJsonArray.add(newAndExclusiveProductJson);
						}
						newAndExclusiveJson.put("newAndExclusiveProducts", newAndExclusiveJsonArray);
					}
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(EXCEPTION_MESSAGE_NEWEXCLUSIVE, e);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(EXCEPTION_MESSAGE_NEWEXCLUSIVE, e);

		}
		catch (final Exception e)
		{
			LOG.error(EXCEPTION_MESSAGE_NEWEXCLUSIVE, e);
		}
		return newAndExclusiveJson;

	}

	//product-price

	/**
	 * @param buyBoxData
	 * @param product
	 * @return productPrice
	 */
	private String getProductPrice(final ProductData product)
	{
		String productPrice = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			final BuyBoxData buyBoxData = buyBoxFacade.buyboxPrice(product.getCode());

			if (buyBoxData != null)
			{

				if (buyBoxData.getSpecialPrice() != null)
				{
					productPrice = buyBoxData.getSpecialPrice().getFormattedValue();
				}
				else if (buyBoxData.getPrice() != null)
				{
					productPrice = buyBoxData.getPrice().getFormattedValue();
				}
				else
				{
					productPrice = buyBoxData.getMrp().getFormattedValue();
				}
			}
			LOG.info("ProductPrice>>>>>>>" + productPrice);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return productPrice;
	}


	/* Home Page Promotional Banner */
	@ResponseBody
	@RequestMapping(value = "/getPromoBannerHomepage", method = RequestMethod.GET)
	public JSONObject getPromoBannerHomepage(@RequestParam(VERSION) final String version)
	{
		JSONObject getPromoBannerHomepageJson = new JSONObject();
		try
		{
			final ContentSlotModel homepageSection4ASlot = cmsPageService.getContentSlotByUidForPage(HOMEPAGE,
					"Section4ASlot-Homepage",

					version);

			//return getJsonBanner(homepageSection4ASlot, "promo");
			getPromoBannerHomepageJson = homepageComponentService.getJsonBanner(homepageSection4ASlot, "promo");
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
		return getPromoBannerHomepageJson;
	}

	/* Home Page StayQued */
	@ResponseBody
	@RequestMapping(value = "/getStayQuedHomepage", method = RequestMethod.GET)
	public JSONObject getStayQuedHomepage(@RequestParam(VERSION) final String version)
	{
		JSONObject getStayQuedHomepageJson = new JSONObject();
		try
		{
			final ContentSlotModel homepageSection5ASlot = cmsPageService.getContentSlotByUidForPage(HOMEPAGE,
					"Section5ASlot-Homepage",

					version);
			//return getJsonBanner(homepageSection5ASlot, "stayQued");
			getStayQuedHomepageJson = homepageComponentService.getJsonBanner(homepageSection5ASlot, "stayQued");
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

		return getStayQuedHomepageJson;

	}


	@ResponseBody
	@RequestMapping(value = "/getCollectionShowcase", method = RequestMethod.GET)
	public JSONObject getCollectionShowcase(@RequestParam(VERSION) final String version)
	{
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		JSONObject collectionShowcase = new JSONObject();
		try
		{
			final ContentSlotModel homepageSection6Slot = cmsPageService.getContentSlotByUidForPage(HOMEPAGE,
					"Section6Slot-Homepage", version);
			if (CollectionUtils.isNotEmpty(homepageSection6Slot.getCmsComponents()))
			{
				components = homepageSection6Slot.getCmsComponents();
			}


			for (final AbstractCMSComponentModel component : components)
			{
				LOG.info("Found Component>>>>with id :::" + component.getUid());

				if (component instanceof MplShowcaseComponentModel)
				{
					final MplShowcaseComponentModel collectionShowcaseComponent = (MplShowcaseComponentModel) component;
					collectionShowcase = getJSONForShowcaseComponent(collectionShowcaseComponent);
				}
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

		return collectionShowcase;

	}

	@ResponseBody
	@RequestMapping(value = "/getShowcaseContent", method = RequestMethod.GET)
	public JSONObject getShowcaseContent(@RequestParam(value = "id") final String componentId)
	{
		MplShowcaseItemComponentModel showcaseItem = null;
		JSONObject showCaseItemJson = new JSONObject();
		LOG.info("Finding component with id::::" + componentId);
		try
		{

			showcaseItem = (MplShowcaseItemComponentModel) cmsComponentService.getSimpleCMSComponent(componentId);
			LOG.info("Found component with id::::" + componentId);

			showCaseItemJson = getJSONForShowCaseItem(showcaseItem, ShowCaseLayout.COLLECTIONSHOWCASE);

		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(e.getStackTrace());
			LOG.error("Could not find component with id::::" + componentId);

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
		return showCaseItemJson;
	}


	/**
	 * @param productData
	 * @return imageUrl
	 */
	private String getProductPrimaryImageUrl(final ProductData productData)
	{
		final List<ImageData> images = (List<ImageData>) productData.getImages();
		String imageUrl = MISSING_IMAGE_URL;

		if (CollectionUtils.isNotEmpty(images))
		{
			for (final ImageData image : images)
			{
				if (image.getMediaPriority() != null && image.getMediaPriority().intValue() == 1
						&& image.getFormat().equalsIgnoreCase("searchPage"))
				{
					imageUrl = image.getUrl();
				}
			}
		}

		return imageUrl;
	}

	/**
	 * @description Used to store emailid for newslettersubscription
	 * @param emailId
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = ModelAttributetConstants.NEWSLETTER, method = RequestMethod.GET)
	public String saveNewsletterSubscriptionEmail(@RequestParam(value = "email") String emailId)
	{
		final MplNewsLetterSubscriptionModel newsLetter = modelService.create(MplNewsLetterSubscriptionModel.class);
		emailId = emailId.toLowerCase();
		if (!validateEmailAddress(emailId))
		{
			return "mailFormatError";
		}
		else
		{
			newsLetter.setEmailId(emailId);
			final boolean result = brandFacade.checkEmailId(emailId);

			//newsLetter.setIsSaved(Boolean.TRUE);

			if (result)
			{
				modelService.save(newsLetter);
				return "success";
			}
			else
			{
				return "fail";
			}

		}

	}

	public boolean validateEmailAddress(final String email)
	{
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		final Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * @description Used to store emailid for newslettersubscription
	 * @param emailId
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/setheader", method = RequestMethod.GET)
	public Map<String, Object> setHeaderData(final HttpSession session)
	{

		final Map<String, Object> header = new HashMap<String, Object>();
		final CartData cartData = cartFacade.getMiniCart();
		header.put("cartcount", String.valueOf(cartData.getTotalItems()));

		header.put("dts", CSRFTokenManager.getTokenForSession(session));

		//customer name in the header
		if (!userFacade.isAnonymousUser())
		{
			header.put("loggedInStatus", true);
			final Object sessionDisplayName = session.getAttribute(userFirstName);
			if (sessionDisplayName == null)
			{
				final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
				String firstName = currentCustomer.getName();
				if (StringUtils.isNotEmpty(firstName))
				{
					if (StringUtils.contains(firstName, '@'))
					{
						firstName = StringUtils.EMPTY;
					}
					else if (StringUtils.length(firstName) > 25)
					{
						firstName = StringUtils.substring(firstName, 0, 25);
					}
				}
				else
				{
					firstName = StringUtils.EMPTY;
				}
				header.put(userFirstName, firstName);
				session.setAttribute(userFirstName, firstName);
			}
			else
			{
				header.put(userFirstName, sessionDisplayName);
			}
		}
		else
		{
			header.put("loggedInStatus", false);
			header.put(userFirstName, null);
		}

		return header;
	}
}
