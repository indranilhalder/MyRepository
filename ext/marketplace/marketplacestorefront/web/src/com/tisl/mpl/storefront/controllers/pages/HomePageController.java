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
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSContentSlotService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.ShowCaseLayout;
import com.tisl.mpl.core.model.MplBigFourPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplShowcaseComponentModel;
import com.tisl.mpl.core.model.MplShowcaseItemComponentModel;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.brand.BrandFacade;
import com.tisl.mpl.facade.cms.MplCmsFacade;
import com.tisl.mpl.facade.latestoffers.LatestOffersFacade;
import com.tisl.mpl.facade.stw.STWWidgetFacade;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.facades.cms.data.FooterLinkData;
import com.tisl.mpl.facades.data.FooterComponentData;
import com.tisl.mpl.facades.data.LatestOffersData;
import com.tisl.mpl.facades.data.STWJsonRecomendationData;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.marketplacecommerceservices.service.HomepageComponentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.security.cookie.PDPPincodeCookieGenerator;
import com.tisl.mpl.storefront.util.CSRFTokenManager;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
//Sonar fix
//import com.tisl.mpl.constants.MplConstants;


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

	@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	private SessionService sessionService;

	//TPR-6654
	@Resource(name = "pdpPincodeCookieGenerator")
	private PDPPincodeCookieGenerator pdpPincodeCookie;

	@Resource(name = "homepageComponentService")
	private HomepageComponentService homepageComponentService;

	@Resource(name = "latestOffersFacade")
	private LatestOffersFacade latestOffersFacade;
	@Autowired
	private DefaultCMSContentSlotService contentSlotService;
	@Autowired
	private ConfigurationService configurationService;

	@Resource(name = "notificationFacade")
	private NotificationFacade notificationFacade;

	@Resource(name = "STWFacade")
	private STWWidgetFacade stwWidgetFacade;

	//Sonar fix
	private static final String DISP_PRICE = "dispPrice";
	private static final String STRIKE_PRICE = "strikePrice";

	/**
	 * @return the stwWidgetFacade
	 */
	public STWWidgetFacade getStwWidgetFacade()
	{
		return stwWidgetFacade;
	}

	/**
	 * @param stwWidgetFacade
	 *           the stwWidgetFacade to set
	 */
	public void setStwWidgetFacade(final STWWidgetFacade stwWidgetFacade)
	{
		this.stwWidgetFacade = stwWidgetFacade;
	}

	@Resource(name = "mplCmsFacade")
	private MplCmsFacade mplCmsFacade;

	/**
	 * @return the notificationFacade
	 */
	public NotificationFacade getNotificationFacade()
	{
		return notificationFacade;
	}

	/**
	 * @param notificationFacade
	 *           the notificationFacade to set
	 */
	public void setNotificationFacade(final NotificationFacade notificationFacade)
	{
		this.notificationFacade = notificationFacade;
	}



	private static final String VERSION = "version";
	private static final String HOMEPAGE = "homepage";
	private static final String TITLE = "title";
	private static final String Y = "Y";


	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	//store url changes
	//private static final String MISSING_IMAGE_URL = "/_ui/desktop/theme-blue/images/missing-product-300x300.jpg";

	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.GALLERY);

	//#3 Change in Populator
	private static final List<ProductOption> PRODUCT_OPTIONS2 = Arrays.asList(ProductOption.HOMEPAGEPRODUCTS);

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
	@SuppressWarnings("boxing")
	@RequestMapping(method = RequestMethod.GET)
	public String home(
			@RequestParam(value = ModelAttributetConstants.LOGOUT, defaultValue = ModelAttributetConstants.FALSE) final boolean logout,
			final Model model, final RedirectAttributes redirectModel, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		//try
		//{
		// TPR-1072 START
		//			final boolean crawlingFlag = configurationService.getConfiguration().getBoolean(ModelAttributetConstants.CRAWLINGFLAG);
		//			final String userAgent = request.getHeader(ModelAttributetConstants.USERAGENT);
		//			final String slotUid = ModelAttributetConstants.FOOTERSLOT;
		//			//change for TISSTRT-1527
		//			if (crawlingFlag && !StringUtils.isEmpty(userAgent)
		//					&& userAgent.toLowerCase().contains(ModelAttributetConstants.GOOGLEBOT))
		//			{
		//				//Footer content
		//				getFooterContent(slotUid, model);
		//				//Shop by navigation
		//				final ContentSlotModel contentSlotModel = contentSlotService
		//						.getContentSlotForId(ModelAttributetConstants.NAVIGATIONBARSLOT);
		//				final List<AbstractCMSComponentModel> componentLists = contentSlotModel.getCmsComponents();
		//				for (final AbstractCMSComponentModel cmsmodel : componentLists)
		//				{
		//					if (cmsmodel instanceof NavigationBarCollectionComponentModel)
		//					{
		//						final NavigationBarCollectionComponentModel deptModel = (NavigationBarCollectionComponentModel) cmsmodel;
		//
		//						model.addAttribute(ModelAttributetConstants.COMPONENT, deptModel);
		//
		//					}
		//
		//				}
		//				//Shop by brand
		//				final List<BrandComponentModel> brands = cmsPageService.getBrandsForShopByBrand();
		//				final List<ShopByBrandData> shopByBrandDataList = new ArrayList<ShopByBrandData>();
		//				BrandComponentModel brandComponent = null;
		//				for (final BrandComponentModel brand : brands)
		//				{
		//					final String component = brand.getUid();
		//					//AtoZ brands starts
		//					if (component.equals(ModelAttributetConstants.ATOZBRANDSCOMPONENT))
		//					{
		//						Map<Character, List<CategoryModel>> sortedMap = null;
		//
		//						sortedMap = brandFacade.getAllBrandsFromCmsCockpit(component);
		//
		//						Map<Character, List<CategoryModel>> GroupBrandsAToE = new TreeMap<Character, List<CategoryModel>>();
		//						Map<Character, List<CategoryModel>> GroupBrandsFToJ = new TreeMap<Character, List<CategoryModel>>();
		//						Map<Character, List<CategoryModel>> GroupBrandsKToO = new TreeMap<Character, List<CategoryModel>>();
		//						Map<Character, List<CategoryModel>> GroupBrandsPToT = new TreeMap<Character, List<CategoryModel>>();
		//						Map<Character, List<CategoryModel>> GroupBrandsUToZ = new TreeMap<Character, List<CategoryModel>>();
		//
		//						GroupBrandsAToE = getBrandsForRange('A', 'E', sortedMap);
		//						GroupBrandsFToJ = getBrandsForRange('F', 'J', sortedMap);
		//						GroupBrandsKToO = getBrandsForRange('K', 'O', sortedMap);
		//						GroupBrandsPToT = getBrandsForRange('P', 'T', sortedMap);
		//						GroupBrandsUToZ = getBrandsForRange('U', 'Z', sortedMap);
		//
		//
		//						model.addAttribute(ModelAttributetConstants.A_E_Brands, GroupBrandsAToE);
		//						model.addAttribute(ModelAttributetConstants.F_J_Brands, GroupBrandsFToJ);
		//						model.addAttribute(ModelAttributetConstants.K_O_Brands, GroupBrandsKToO);
		//						model.addAttribute(ModelAttributetConstants.P_T_Brands, GroupBrandsPToT);
		//						model.addAttribute(ModelAttributetConstants.U_Z_Brands, GroupBrandsUToZ);
		//					}
		//					//AtoZ brands ends
		//
		//					final ShopByBrandData shopByBrandData = new ShopByBrandData();
		//					if (StringUtils.isNotEmpty(component))
		//					{
		//						brandComponent = (BrandComponentModel) cmsComponentService.getSimpleCMSComponent(component);
		//					}
		//					shopByBrandData.setLayout(brandComponent.getLayout());
		//					shopByBrandData.setMasterBrandName(brandComponent.getMasterBrandName());
		//					shopByBrandData.setMasterBrandUrl(brandComponent.getMasterBrandURL());
		//					shopByBrandData.setSubBrandList(brandComponent.getSubBrandList());
		//					shopByBrandData.setSubBrands(brandComponent.getSubBrands());
		//					shopByBrandDataList.add(shopByBrandData);
		//					model.addAttribute(ModelAttributetConstants.SHOPBYBRANDDATALIST, shopByBrandDataList);
		//					for (final CategoryModel category : brandComponent.getSubBrands())
		//					{
		//						String categoryPath = GenericUtilityMethods.urlSafe(category.getName());
		//						if (StringUtils.isNotEmpty(categoryPath))
		//						{
		//							try
		//							{
		//								categoryPath = URLDecoder.decode(categoryPath, "UTF-8");
		//							}
		//							catch (final UnsupportedEncodingException e)
		//							{
		//								LOG.error(e.getMessage());
		//							}
		//							categoryPath = categoryPath.toLowerCase();
		//							categoryPath = GenericUtilityMethods.changeUrl(categoryPath);
		//						}
		//						category.setName(category.getName() + "||" + categoryPath);
		//					}
		//				}
		//				model.addAttribute(ModelAttributetConstants.GOOGLEBOT, ModelAttributetConstants.GOOGLEBOT);
		//				//				}
		//			}
		// TPR-1072 END
		if (logout)
		{
			//GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, "account.confirmation.signout.title");
			return REDIRECT_PREFIX + ROOT;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(null));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
		updatePageTitle(model, getContentPageForLabelOrId(null));

		//UF-287
		final JSONObject singleBanner = getHomePageBanners("Online", "yes");
		model.addAttribute("mobileBanner", singleBanner.get("moblileBanners"));
		model.addAttribute("desktopBanner", singleBanner.get("desktopBanners"));
		//}
		//		catch (final EtailBusinessExceptions e)
		//		{
		//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		//
		//		}
		//		catch (final EtailNonBusinessExceptions e)
		//		{
		//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		//
		//		}
		//		catch (final Exception e)
		//		{
		//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
		//					MarketplacecommerceservicesConstants.E0000));
		//		}
		return getViewForPage(model);
	}

	@Override
	protected ContentPageModel getContentPageForLabelOrId(final String labelOrId) throws CMSItemNotFoundException
	{
		final String siteId = getSiteConfigService().getProperty("luxury.site.id");
		if ((getCmsSiteService().getCurrentSite().getUid()).equalsIgnoreCase(siteId))
		{
			final String gender = getCustomerFacade().getCurrentCustomer().getGender();
			if (gender != null && !(gender.isEmpty()))
			{
				switch (gender)
				{
					case MessageConstants.MALE:
					{
						final String key = getSiteConfigService().getProperty(MessageConstants.MENLANDING);
						return super.getContentPageForLabelOrId(key);
					}
					case MessageConstants.FEMALE:
					{
						final String key = getSiteConfigService().getProperty(MessageConstants.WOMENLANDING);
						return super.getContentPageForLabelOrId(key);
					}
					default:
					{
						return super.getContentPageForLabelOrId(labelOrId);
					}
				}
			}

		}
		return super.getContentPageForLabelOrId(labelOrId);
	}

	//	/**
	//	 * @param AtoZ
	//	 *           brands range
	//	 * @param endCharacter
	//	 * @param sortedMap
	//	 * @return map
	//	 */
	//	@SuppressWarnings(
	//	{ "boxing", "javadoc" })
	//	private Map<Character, List<CategoryModel>> getBrandsForRange(final Character startCharacter, final Character endCharacter,
	//			final Map<Character, List<CategoryModel>> sortedMap)
	//	{
	//		final Map<Character, List<CategoryModel>> brandsByRange = new HashMap();
	//
	//		for (final Entry<Character, List<CategoryModel>> entry : sortedMap.entrySet())
	//		{ //ASCII Value of entry key
	//			final int entryKeyCharacterASCII = entry.getKey();
	//
	//			//ASCII value of startCharacter
	//			final int startCharacterASCII = startCharacter;
	//
	//			//ASCII value of endCharacter
	//			final int endCharacterASCII = endCharacter;
	//
	//			if (entryKeyCharacterASCII >= startCharacterASCII && entryKeyCharacterASCII <= endCharacterASCII)
	//			{
	//				brandsByRange.put(entry.getKey(), entry.getValue());
	//			}
	//
	//		}
	//		return new TreeMap<Character, List<CategoryModel>>(brandsByRange);
	//	}

	/**
	 * @description this is called to update the page title
	 * @param model
	 * @param cmsPage
	 */
	protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage)
	{
		//PRDI-422
		storeContentPageTitleInModel(model,
				StringEscapeUtils.unescapeHtml(getPageTitleResolver().resolveHomePageTitle(cmsPage.getTitle())));
	}

	/**
	 * @description This method gives us the images of desktop and mobile banners
	 * @param version
	 * @return JSONObject
	 */
	@SuppressWarnings("boxing")
	@ResponseBody
	@RequestMapping(value = "/getHomePageBanners", method = RequestMethod.GET)
	public JSONObject getHomePageBanners(@RequestParam(VERSION) final String version,
			@RequestParam(required = false, defaultValue = "", value = "init-load") final String initLoad)
	{
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		final JSONObject homePageBannerJson = new JSONObject();
		try
		{
			final ContentSlotModel homeSlotSection1 = cmsPageService.getContentSlotByUidForPage(HOMEPAGE, "Section1-TMPHomepage",
					version);
			if (CollectionUtils.isNotEmpty(homeSlotSection1.getCmsComponents()))
			{
				components = homeSlotSection1.getCmsComponents();
			}
			for (final AbstractCMSComponentModel component : components)
			{
				//SONAR FIX
				LOG.info(MarketplacecommerceservicesConstants.FOUNDCOMPONENT + component.getUid());

				if (component instanceof RotatingImagesComponentModel)
				{

					if (component.getVisible().booleanValue() && homepageComponentService.showOnTimeRestriction(component))
					{
						final RotatingImagesComponentModel homePageBanners = (RotatingImagesComponentModel) component;

						final LinkedHashSet<String> desktopBanners = new LinkedHashSet<String>();
						final LinkedHashSet<String> moblileBanners = new LinkedHashSet<String>();
						for (final BannerComponentModel banner : homePageBanners.getBanners())
						{

							if (banner instanceof MplBigPromoBannerComponentModel)
							{
								final MplBigPromoBannerComponentModel bannerComponent = (MplBigPromoBannerComponentModel) banner;
								if (bannerComponent.getVisible() && homepageComponentService.showOnTimeRestriction(bannerComponent))
								{
									if (null != banner.getBannerView() && banner.getBannerView().getCode().equalsIgnoreCase("mobileView"))
									{
										moblileBanners.add(bannerComponent.getBannerImage().getUrl());
									}
									else
									{
										desktopBanners.add(bannerComponent.getBannerImage().getUrl());
									}
								}

							}
							else if (banner instanceof MplBigFourPromoBannerComponentModel)
							{
								final MplBigFourPromoBannerComponentModel bannerComponent = (MplBigFourPromoBannerComponentModel) banner;
								if (bannerComponent.getVisible() && homepageComponentService.showOnTimeRestriction(bannerComponent))
								{
									if (null != banner.getBannerView() && banner.getBannerView().getCode().equalsIgnoreCase("mobileView"))
									{
										moblileBanners.add(bannerComponent.getBannerImage().getUrl());
									}
									else
									{
										desktopBanners.add(bannerComponent.getBannerImage().getUrl());
									}
								}
							}
							else
							{
								if (banner.getVisible() && homepageComponentService.showOnTimeRestriction(banner))
								{
									if (null != banner.getBannerView() && banner.getBannerView().getCode().equalsIgnoreCase("mobileView"))
									{
										moblileBanners.add(banner.getMedia().getUrl());
									}
									else
									{
										desktopBanners.add(banner.getMedia().getUrl());
									}
								}
							}
							if (StringUtils.isNotEmpty(initLoad))
							{
								break;
							}

						}
						homePageBannerJson.put("desktopBanners", desktopBanners);
						homePageBannerJson.put("moblileBanners", moblileBanners);
					}
					else
					{
						LOG.info(MarketplacecommerceservicesConstants.HOMEPAGELOGINFO);
					}
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
		return homePageBannerJson;
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
				//SONAR FIX
				LOG.info(MarketplacecommerceservicesConstants.FOUNDCOMPONENT + component.getUid());

				if (component instanceof MplShowcaseComponentModel)
				{
					//TPR-559 Show/Hide Components and Sub-components //TPR-558 Scheduling of banners
					if (component.getVisible().booleanValue() && homepageComponentService.showOnTimeRestriction(component))
					{
						final MplShowcaseComponentModel brandsYouLoveComponent = (MplShowcaseComponentModel) component;
						brandsYouLoveJson = getJSONForShowcaseComponent(brandsYouLoveComponent);
					}
					else
					{
						LOG.info(MarketplacecommerceservicesConstants.HOMEPAGELOGINFO);
					}
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
			String bannerImageUrl = EMPTY_STRING;
			String brandLogoAltText = EMPTY_STRING;
			for (final MplShowcaseItemComponentModel showcaseItem : showCaseComponent.getShowcaseItems())
			{
				//TPR-559 Show/Hide Components and Sub-components //TPR-558 Scheduling of banners
				if (showcaseItem.getVisible().booleanValue() && homepageComponentService.showOnTimeRestriction(showcaseItem))
				{
					final JSONObject showCaseItemJson = new JSONObject();
					showCaseItemJson.put("compId", showcaseItem.getUid());
					showCaseItemJson.put("showByDefault", showcaseItem.getShowByDefault());
					if (null != showCaseComponent.getLayout() && showCaseComponent.getLayout().equals(ShowCaseLayout.BRANDSHOWCASE))
					{
						if (null != showcaseItem.getLogo())
						{
							if (StringUtils.isNotEmpty(showcaseItem.getLogo().getURL()))
							{
								brandLogoUrl = showcaseItem.getLogo().getURL();
							}
							showCaseItemJson.put("brandLogoUrl", brandLogoUrl);
							if (StringUtils.isNotEmpty(showcaseItem.getLogo().getAltText()))
							{
								brandLogoAltText = showcaseItem.getLogo().getAltText();
							}
							showCaseItemJson.put("brandLogoAltText", brandLogoAltText);
						}

						if (null != showcaseItem.getBannerImage())
						{
							if (StringUtils.isNotEmpty(showcaseItem.getBannerImage().getURL()))
							{
								bannerImageUrl = showcaseItem.getBannerImage().getURL();
							}
							showCaseItemJson.put("bannerImageUrl", bannerImageUrl);
						}
						if (null != showcaseItem.getText())
						{
							if (StringUtils.isNotEmpty(showcaseItem.getText()))
							{
								showCaseItemJson.put("text", showcaseItem.getText());
							}
						}
						if (null != showcaseItem.getBannerText())
						{
							if (StringUtils.isNotEmpty(showcaseItem.getBannerText()))
							{
								showCaseItemJson.put("bannerText", showcaseItem.getBannerText());
							}
						}
						if (null != showcaseItem.getBannerUrl() && StringUtils.isNotEmpty(showcaseItem.getBannerUrl()))
						{
							showCaseItemJson.put("bannerUrl", showcaseItem.getBannerUrl());
						}
					}
					else
					{
						String headerText = EMPTY_STRING;
						if (StringUtils.isNotEmpty(showcaseItem.getHeaderText()))
						{
							headerText = showcaseItem.getHeaderText();
						}
						showCaseItemJson.put("headerText", headerText);
						//UF-420 starts
						try
						{
							JSONObject showCaseItemDetailJson = new JSONObject();
							MplShowcaseItemComponentModel showcaseItemDetail = null;
							showcaseItemDetail = (MplShowcaseItemComponentModel) cmsComponentService.getSimpleCMSComponent(showcaseItem
									.getUid());
							showCaseItemDetailJson = getJSONForShowCaseItem(showcaseItemDetail, ShowCaseLayout.COLLECTIONSHOWCASE);
							showCaseItemJson.put("details", showCaseItemDetailJson);
						}
						catch (final CMSItemNotFoundException e)
						{
							LOG.error(e.getStackTrace());
							LOG.error("Could not find component with id::::" + showcaseItem.getUid());

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
						//UF-420 ends
					}

					subComponentJsonArray.add(showCaseItemJson);
				}
				else
				{
					LOG.info(MarketplacecommerceservicesConstants.HOMEPAGELOGINFO);
				}
			}
		}
		// Changes implemented for TPR-1121
		showCaseComponentJson.put("autoPlay", showCaseComponent.getAutoPlay());
		showCaseComponentJson.put("slideBy", showCaseComponent.getSlideBy());
		showCaseComponentJson.put("autoplayTimeout", showCaseComponent.getAutoplayTimeout());
		showCaseComponentJson.put("subComponents", subComponentJsonArray);

		return showCaseComponentJson;


	}

	@SuppressWarnings("deprecation")
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

				//PT fix : Thread Lock found in brands you love component
				firstProduct = productFacade.getProductForOptions(showcaseItem.getProduct1(), PRODUCT_OPTIONS2);
				if (StringUtils.isBlank(firstProduct.getHomepageImageurl()))
				{
					showCaseItemJson.put("firstProductImageUrl", GenericUtilityMethods.getMissingImageUrl());
				}
				else
				{
					showCaseItemJson.put("firstProductImageUrl", firstProduct.getHomepageImageurl());
				}
				if (StringUtils.isBlank(firstProduct.getProductTitle()))
				{
					showCaseItemJson.put("firstProductTitle", "");
				}
				else
				{
					showCaseItemJson.put("firstProductTitle", firstProduct.getProductTitle());
				}
				if (StringUtils.isBlank(firstProduct.getUrl()))
				{
					showCaseItemJson.put("firstProductUrl", "");
				}
				else
				{
					showCaseItemJson.put("firstProductUrl", firstProduct.getUrl());
				}
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
		final String allowNew = configurationService.getConfiguration().getString("attribute.new.display");
		Date existDate = null;
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
				//SONAR FIX
				LOG.info(MarketplacecommerceservicesConstants.FOUNDCOMPONENT + component.getUid());

				if (component instanceof ProductCarouselComponentModel)
				{
					//TPR-559 Show/Hide Components and Sub-components //TPR-558 Scheduling of banners
					if (component.getVisible().booleanValue() && homepageComponentService.showOnTimeRestriction(component))
					{
						final ProductCarouselComponentModel newAndExclusiveComponent = (ProductCarouselComponentModel) component;
						String title = EMPTY_STRING;


						if (StringUtils.isNotEmpty(newAndExclusiveComponent.getTitle()))
						{
							title = newAndExclusiveComponent.getTitle();
						}

						//#1 reduced calls to newAndExclusiveComponent.getProducts() using
						final List<ProductModel> productList = newAndExclusiveComponent.getProducts();
						newAndExclusiveJson.put(TITLE, title);

						// Changes implemented for TPR-1121
						newAndExclusiveJson.put("autoPlay", newAndExclusiveComponent.getAutoPlayNewIn());
						newAndExclusiveJson.put("slideBy", newAndExclusiveComponent.getSlideByNewIn());
						newAndExclusiveJson.put("autoplayTimeout", newAndExclusiveComponent.getAutoplayTimeoutNewIn());

						final JSONArray newAndExclusiveJsonArray = new JSONArray();

						if (CollectionUtils.isNotEmpty(productList))
						{

							for (final ProductModel newAndExclusiveProducts : productList)
							{
								//START :code added for 'NEW' tag on the product image
								for (final SellerInformationModel seller : newAndExclusiveProducts.getSellerInformationRelator())
								{
									if (null != seller.getStartDate() && new Date().after(seller.getStartDate())
											&& null != seller.getEndDate() && new Date().before(seller.getEndDate()))
									{
										if (null != allowNew && allowNew.equalsIgnoreCase(Y))
										{
											//Find the oldest startDate of the seller
											if (null == existDate)
											{
												existDate = seller.getStartDate();
											}
											else if (existDate.after(seller.getStartDate()))
											{
												existDate = seller.getStartDate();
											}
										}
									}
								}
								final JSONObject newAndExclusiveProductJson = new JSONObject();
								if (null != existDate && isNew(existDate))
								{
									newAndExclusiveProductJson.put("isNew", Y);
								}
								//END :code added for 'NEW' tag on the product image



								ProductData product = null;
								product = productFacade.getProductForOptions(newAndExclusiveProducts, PRODUCT_OPTIONS2);
								//#4 Image Call
								if (StringUtils.isBlank(product.getHomepageImageurl()))
								{
									newAndExclusiveProductJson.put("productImageUrl", GenericUtilityMethods.getMissingImageUrl());
								}
								else
								{
									newAndExclusiveProductJson.put("productImageUrl", product.getHomepageImageurl());
								}
								newAndExclusiveProductJson.put("productTitle", product.getProductTitle());
								newAndExclusiveProductJson.put("productUrl", product.getUrl());
								Map<String, String> priceMap = new HashMap<String, String>();
								String price = null;
								try
								{
									//UF-319
									priceMap = getProductPriceNewAndExclusive(product);
									price = priceMap.get(DISP_PRICE);
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
								//#2 If Price is available then only show Products
								if (!StringUtils.isEmpty(price))
								{
									newAndExclusiveProductJson.put("productPrice", priceMap);
									newAndExclusiveJsonArray.add(newAndExclusiveProductJson);
								}

								existDate = null;

							}
							//#2 If Price is available then only show Products
							if (!CollectionUtils.isEmpty(newAndExclusiveJsonArray))
							{
								newAndExclusiveJson.put("newAndExclusiveProducts", newAndExclusiveJsonArray);
							}
						}
					}
					else
					{
						LOG.info(MarketplacecommerceservicesConstants.HOMEPAGELOGINFO);
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

	private boolean isNew(final Date existDate)
	{
		boolean newAttr = false;
		if (null != existDate)
		{
			final Date sysDate = new Date();
			final long dayDiff = calculateDays(existDate, sysDate);

			LOG.info("******" + existDate + "  --- dayDiff: " + dayDiff);
			final String validDaysSt = configurationService.getConfiguration().getString("attribute.new.validDays");
			final int validDays = validDaysSt == null ? 0 : Integer.parseInt(validDaysSt);

			if (validDays > dayDiff)
			{
				newAttr = true;
			}
		}

		return newAttr;
	}

	private long calculateDays(final Date dateEarly, final Date dateLater)
	{
		return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
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
					productPrice = buyBoxData.getSpecialPrice().getFormattedValueNoDecimal();
				}
				else if (buyBoxData.getPrice() != null)
				{
					productPrice = buyBoxData.getPrice().getFormattedValueNoDecimal();
				}
				else
				{
					productPrice = buyBoxData.getMrp().getFormattedValueNoDecimal();
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

	//Product-price map for UF-319

	/**
	 * @param buyBoxData
	 * @param product
	 * @return productPrice
	 */
	private Map<String, String> getProductPriceNewAndExclusive(final ProductData product)
	{
		final Map<String, String> productPriceMap = new HashMap<String, String>();
		String productPrice = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			final BuyBoxData buyBoxData = buyBoxFacade.buyboxPrice(product.getCode());

			if (buyBoxData != null)
			{
				if (buyBoxData.getSpecialPrice() != null)
				{
					productPrice = buyBoxData.getSpecialPrice().getFormattedValueNoDecimal();
					if (productPrice != null && StringUtils.isNotEmpty(productPrice))
					{
						productPriceMap.put(DISP_PRICE, productPrice);
					}
				}
				if (buyBoxData.getPrice() != null)
				{
					productPrice = buyBoxData.getPrice().getFormattedValueNoDecimal();
					if (productPrice != null && StringUtils.isNotEmpty(productPrice) && productPriceMap.get(DISP_PRICE) == null)
					{
						productPriceMap.put(DISP_PRICE, productPrice);
					}
					/*
					 * else if (productPrice != null && StringUtils.isNotEmpty(productPrice)) {
					 * productPriceMap.put("strikePrice", productPrice); }
					 */
				}
				if (buyBoxData.getMrp() != null)
				{
					productPrice = buyBoxData.getMrp().getFormattedValueNoDecimal();
					if (productPrice != null && StringUtils.isNotEmpty(productPrice) && productPriceMap.get(DISP_PRICE) == null)
					{
						productPriceMap.put(DISP_PRICE, productPrice);
						productPriceMap.put(STRIKE_PRICE, MarketplacecommerceservicesConstants.EMPTY);
					}
					else if (productPrice != null && StringUtils.isNotEmpty(productPrice))
					{
						productPriceMap.put(STRIKE_PRICE, productPrice);
					}
					else
					{
						if (productPriceMap.get(DISP_PRICE) == null)
						{
							productPriceMap.put(DISP_PRICE, MarketplacecommerceservicesConstants.EMPTY);
						}
						if (productPriceMap.get(STRIKE_PRICE) == null)
						{
							productPriceMap.put(STRIKE_PRICE, MarketplacecommerceservicesConstants.EMPTY);
						}

					}
				}
			}
			else
			{
				productPriceMap.put(DISP_PRICE, productPrice);
				productPriceMap.put(STRIKE_PRICE, productPrice);
			}
			LOG.info("ProductPrice>>>>>>>" + productPrice);
			//productPriceMap.put("price", productPrice);
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

		return productPriceMap;
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
				//SONAR FIX
				LOG.info(MarketplacecommerceservicesConstants.FOUNDCOMPONENT + component.getUid());

				if (component instanceof MplShowcaseComponentModel)
				{
					//TPR-559 Show/Hide Components and Sub-components //TPR-558 Scheduling of banners
					if (component.getVisible().booleanValue() && homepageComponentService.showOnTimeRestriction(component))
					{
						final MplShowcaseComponentModel collectionShowcaseComponent = (MplShowcaseComponentModel) component;
						collectionShowcase = getJSONForShowcaseComponent(collectionShowcaseComponent);
					}
					else
					{
						LOG.info(MarketplacecommerceservicesConstants.HOMEPAGELOGINFO);
					}
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
	public JSONObject getShowcaseContent(@RequestParam(value = "id") final String componentId, final HttpServletRequest request)
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

	//TPR-1672
	@ResponseBody
	@RequestMapping(value = "/getBestOffers", method = RequestMethod.GET)
	public JSONObject getBestOffers(@RequestParam(VERSION) final String version)
	{
		JSONObject getBestOffersJson = new JSONObject();
		try
		{
			final ContentSlotModel homepageSectionBestOfferSlot = cmsPageService.getContentSlotByUidForPage(HOMEPAGE,
					"BestOffersSectionSlot-Homepage", version);
			//return homepageComponentService.getBestPicksJSON(homepageSection4CSlot);
			getBestOffersJson = homepageComponentService.getBestOffersJSON(homepageSectionBestOfferSlot);
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
		return getBestOffersJson;
	}



	/**
	 * @param productData
	 * @return imageUrl
	 */
	private String getProductPrimaryImageUrl(final ProductData productData)
	{
		final List<ImageData> images = (List<ImageData>) productData.getImages();
		//String imageUrl = MISSING_IMAGE_URL;
		String imageUrl = GenericUtilityMethods.getMissingImageUrl();

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
	public String saveNewsletterSubscriptionEmail(@RequestParam(value = "email") String emailId,
			@RequestParam(value = "gender", required = false, defaultValue = ModelAttributetConstants.EMPTY) final String gender,
			@RequestParam(value = "isLuxury", required = false, defaultValue = ModelAttributetConstants.FALSE) final Boolean isLuxury)
	{
		final MplNewsLetterSubscriptionModel newsLetter = modelService.create(MplNewsLetterSubscriptionModel.class);
		emailId = emailId.toLowerCase();
		if (!validateEmailAddress(emailId))
		{
			return "mailFormatError";
		}
		else
		{

			if (isLuxury)
			{
				final List<MplNewsLetterSubscriptionModel> newsLetterSubscriptionList = brandFacade.checkEmailIdForluxury(emailId,
						"1");

				if (null == newsLetterSubscriptionList || newsLetterSubscriptionList.isEmpty())
				{
					newsLetter.setEmailId(emailId);
					newsLetter.setGender(gender);
					newsLetter.setIsLuxury(Boolean.TRUE);
					modelService.save(newsLetter);
					return "success";
				}
				else
				{
					return "fail";
				}
			}
			/*
			 * else { //newsLetter.setEmailId(emailId); final boolean result = brandFacade.checkEmailId(emailId);
			 *
			 * //newsLetter.setIsSaved(Boolean.TRUE);
			 *
			 * if (result) { newsLetter.setEmailId(emailId); newsLetter.setIsMarketplace(Boolean.TRUE);
			 * modelService.save(newsLetter); return "success"; }
			 */
			else
			{
				final List<MplNewsLetterSubscriptionModel> newsLetterSubscriptionList = brandFacade
						.checkEmailIdForMarketplace(emailId);

				if (null != newsLetterSubscriptionList && !newsLetterSubscriptionList.isEmpty())
				{
					for (final MplNewsLetterSubscriptionModel mplNewsLetterSubscriptionModel : newsLetterSubscriptionList)
					{
						if ((mplNewsLetterSubscriptionModel.getEmailId().equalsIgnoreCase(emailId))
								&& (!(mplNewsLetterSubscriptionModel.getIsMarketplace().booleanValue()) || mplNewsLetterSubscriptionModel
										.getIsMarketplace() == null))
						{
							mplNewsLetterSubscriptionModel.setIsMarketplace(Boolean.TRUE);
							modelService.save(mplNewsLetterSubscriptionModel);
						}

					}
					return "success";
				}
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

	private static HttpServletRequest getRequest()
	{
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}


	@ResponseBody
	@RequestMapping(value = "/fetchToken", method = RequestMethod.GET)
	public JSONObject fetchToken(final HttpSession session)
	{
		final HttpServletRequest request = getRequest();
		final String visitorIP = getVisitorIpAddress(request);
		String sessionId = session.getId();

		if (sessionId.contains("."))
		{
			final String[] parts = sessionId.split("\\.");
			sessionId = parts[0];

		}
		final JSONObject sessionDetails = new JSONObject();
		sessionDetails.put("token", CSRFTokenManager.getTokenForSession(session));
		sessionDetails.put("sessionId", sessionId);
		sessionDetails.put("vistiorIp", visitorIP);
		return sessionDetails;

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

		//header.put("dts", CSRFTokenManager.getTokenForSession(session));

		//customer name in the header
		if (!userFacade.isAnonymousUser())
		{
			header.put("loggedInStatus", true);
			final Object sessionDisplayName = session.getAttribute(userFirstName);
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();


			List<NotificationData> notificationMessagelist = new ArrayList<NotificationData>();
			final String customerUID = currentCustomer.getUid();
			if (null != customerUID)
			{
				notificationMessagelist = getNotificationFacade().getNotificationDetail(customerUID, true);

				if (null != notificationMessagelist && !notificationMessagelist.isEmpty())
				{
					//final int notificationCount = 0;
					final int notificationCount = notificationMessagelist.size();
					/*
					 * for (final NotificationData single : notificationMessagelist) { if (single.getNotificationRead() !=
					 * null && !single.getNotificationRead().booleanValue()) { notificationCount++; }
					 *
					 * }
					 */

					header.put("notificationCount", notificationCount);
				}
				else
				{
					header.put("notificationCount", null);
				}
			}


			if (sessionDisplayName == null)
			{

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
			header.put("notificationCount", null);
		}

		return header;
	}



	@RequestMapping(value = "/listOffers", method = RequestMethod.GET)
	public String get(final Model model, final HttpServletRequest request)
	{
		LatestOffersData latestOffersData = new LatestOffersData();
		try
		{
			final ContentSlotModel homepageHeaderConcierge = contentSlotService.getContentSlotForId("HeaderLinksSlot");
			latestOffersData = latestOffersFacade.getLatestOffers(homepageHeaderConcierge);
			model.addAttribute("latestOffersData", latestOffersData);
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
		//return getBestPicksJson;

		return ControllerConstants.Views.Fragments.Home.LatestOffers;
	}

	//TPR-1072
	@RequestMapping(value = "/getFooterContent", method = RequestMethod.GET)
	public String getFooterContent(@RequestParam(value = "id") final String slotId, final Model model)
	{
		try
		{


			final FooterComponentData fData = mplCmsFacade.getContentSlotData(slotId);


			model.addAttribute("footerSocialIconList", fData.getFooterSocialIconList());
			model.addAttribute("footerText", fData.getFooterText());
			model.addAttribute("notice", fData.getNotice());
			model.addAttribute("footerAppImageList", fData.getFooterAppImageList());
			model.addAttribute("navigationNodes", fData.getNavigationNodes());
			model.addAttribute("wrapAfter", fData.getWrapAfter());
			//			//Need help section
			model.addAttribute("contactNumber", fData.getContactNumber());

			// For TPR-5733
			final Map<Integer, Map<Integer, FooterLinkData>> mplFooterLinkRowList = mplCmsFacade.getFooterLinkData();
			if (MapUtils.isNotEmpty(mplFooterLinkRowList))
			{
				model.addAttribute(ModelAttributetConstants.FOOTER_LINK_LIST, mplFooterLinkRowList);
			}
			else
			{
				LOG.debug("##########################   Returned empty list for footer link ####################################");
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
		return ControllerConstants.Views.Fragments.Home.FooterPanel;
	}

	private static String getVisitorIpAddress(final HttpServletRequest request)
	{
		final String[] HEADERS_TO_TRY =
		{ "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
				"HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };
		for (final String header : HEADERS_TO_TRY)
		{
			final String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip))
			{
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

	private static String getVisitorIp(final HttpServletRequest request)
	{
		final String REMOTE_IP = request.getHeader("REMOTE_ADDR");
		final String HTTP_FORWARDED_FOR = request.getHeader("HTTP_FORWARDED_FOR");

		if (REMOTE_IP != null && REMOTE_IP.length() != 0 && !"unknown".equalsIgnoreCase(REMOTE_IP))
		{
			return REMOTE_IP;
		}
		else if (HTTP_FORWARDED_FOR != null && HTTP_FORWARDED_FOR.length() != 0 && !"unknown".equalsIgnoreCase(HTTP_FORWARDED_FOR))
		{
			return HTTP_FORWARDED_FOR;
		}
		else
		{
			return request.getRemoteAddr();
		}
	}

	/**
	 * UF-258 This is a Sales Traffic Widget with upstream data from the Dew Server
	 */

	@ResponseBody
	@RequestMapping(value = "/getStwrecomendations", method = RequestMethod.GET)
	public JSONObject getStwWidgetDada(final HttpServletRequest request)
	{
		final JSONObject STWJObject = new JSONObject();
		final String stwUse = configurationService.getConfiguration().getString("stw.use");
		if (stwUse.equalsIgnoreCase("Y"))
		{
			final Map<String, String[]> stwRequest = request.getParameterMap();
			final List<STWJsonRecomendationData> stwRecData = getStwWidgetFacade().getSTWWidgetFinalData(stwRequest);
			final String stwCategories = configurationService.getConfiguration().getString("stw.categories");
			final String stwWidgetHeading = configurationService.getConfiguration().getString("stw.heading");
			final String stwWidgetBlpHeading = configurationService.getConfiguration().getString("stw.blpheading");
			STWJObject.put("STWBlpHeading", stwWidgetBlpHeading);
			STWJObject.put("STWHeading", stwWidgetHeading);
			STWJObject.put("STWElements", stwRecData);
			STWJObject.put("STWCategories", stwCategories);
			STWJObject.put("visiterIP", getVisitorIp(request));
		}
		return STWJObject;
	}

	//PRDI-422
	@Override
	public void setUpMetaData(final Model model, final String metaKeywords, final String metaDescription)
	{
		model.addAttribute(ModelAttributetConstants.KEYWORDS, metaKeywords);
		model.addAttribute(ModelAttributetConstants.DESCRIPTION, metaDescription);
	}

	//TPR-6654
	@ResponseBody
	@RequestMapping(value = "/homePincode", method = RequestMethod.GET)
	public void persistPincode(@RequestParam(value = "pin") final String pin, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		int pincodeCookieMaxAge;
		final Cookie cookie = GenericUtilityMethods.getCookieByName(request, "pdpPincode");
		final String cookieMaxAge = configurationService.getConfiguration().getString("pdpPincode.cookie.age");
		pincodeCookieMaxAge = (Integer.valueOf(cookieMaxAge)).intValue();
		final String domain = configurationService.getConfiguration().getString("shared.cookies.domain");
		try
		{
			final String regex = "\\d{6}";
			if (pin.matches(regex))
			{
				if (cookie != null && cookie.getValue() != null)
				{
					cookie.setValue(pin);
					cookie.setMaxAge(pincodeCookieMaxAge);
					cookie.setPath("/");

					if (null != domain && !domain.equalsIgnoreCase("localhost"))
					{
						cookie.setSecure(true);
					}
					cookie.setDomain(domain);
					response.addCookie(cookie);
					sessionService.setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, pin);
				}
				else
				{
					pdpPincodeCookie.addCookie(response, pin);
					sessionService.setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, pin);
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
	}
}
