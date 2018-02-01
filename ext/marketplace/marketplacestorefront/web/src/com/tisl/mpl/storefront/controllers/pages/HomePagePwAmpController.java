/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.enums.ShowCaseLayout;
import com.tisl.mpl.core.model.MplBigFourPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplShowcaseComponentModel;
import com.tisl.mpl.core.model.MplShowcaseItemComponentModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.marketplacecommerceservices.service.HomepageComponentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.web.data.MplAutocompleteAmpData;
import com.tisl.mpl.storefront.web.data.MplAutocompleteResultData;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = "/pwamp")
public class HomePagePwAmpController extends HomePageController
{

	private static final String VERSION = "version";
	private static final String HOMEPAGE = "homepage";
	private static final String TITLE = "title";
	private static final String EMPTY_STRING = "";
	private static final String EXCEPTION_MESSAGE_PRICE = "Exception to fetch price for product code";
	private static final String EXCEPTION_MESSAGE_SHOWCASE = "Exception  in getJSONForShowCaseItem";
	private static final String EXCEPTION_MESSAGE_NEWEXCLUSIVE = "Exception in getNewAndExclusive";
	private static final String Y = "Y";
	private static final String COMPONENT_UID_PATH_VARIABLE_PATTERN = "{componentUid:.*}";
	private static final String DROPDOWN_BRAND = "MBH";
	private static final String DROPDOWN_CATEGORY = "MSH";
	private static final String URL = "url";
	private static final String ITEMS = "items";

	@Resource(name = "cmsPageService")
	private MplCmsPageService cmsPageService;
	@Resource(name = "homepageComponentService")
	private HomepageComponentService homepageComponentService;
	@Autowired
	private ConfigurationService configurationService;
	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;
	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;
	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;

	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;
	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;

	private static final List<ProductOption> PRODUCT_OPTIONS2 = Arrays.asList(ProductOption.HOMEPAGEPRODUCTS);
	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.GALLERY);
	//Sonar fix
	private static final String DISP_PRICE = "dispPrice";
	private static final String STRIKE_PRICE = "strikePrice";

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	String pwampHome(final HttpServletRequest request, final HttpServletResponse response, final Model model,
			final RedirectAttributes redirectModel)
	{
		final Cookie[] cookies = request.getCookies();

		if (cookies != null)
		{
			for (final Cookie cookie : cookies)
			{
				if (cookie.getName().equals("mpl-userType"))
				{
					model.addAttribute("user_type", cookie.getValue());
				}
				if (cookie.getName().equals("mpl-user"))
				{
					model.addAttribute("user_id", cookie.getValue());
				}
			}
			model.addAttribute("sessionId", request.getSession().getId());
			model.addAttribute("visitorIp", getVisitorIp(request));
		}
		try
		{
			this.home(false, model, redirectModel, request);
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error("HomePagePwAmpController :: pwampHome" + e.getMessage());
		}
		return "/pages/pwamp/home";
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
	 * @description This method gives us the images of desktop and mobile banners
	 * @param version
	 * @return JSONObject
	 */
	@Override
	@SuppressWarnings("boxing")
	@ResponseBody
	@RequestMapping(value = "/getHomePageBanners", method = RequestMethod.GET)
	public JSONObject getHomePageBanners(@RequestParam(VERSION) final String version,
			@RequestParam(required = false, defaultValue = "", value = "init-load") final String initLoad)
	{
		final JSONArray ampArray = new JSONArray();
		final JSONObject ampObj = new JSONObject();
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

						final LinkedHashSet<LinkedHashMap<String, String>> mobileBannersSet = new LinkedHashSet<LinkedHashMap<String, String>>();
						final LinkedHashSet<LinkedHashMap<String, String>> desktopBannersSet = new LinkedHashSet<LinkedHashMap<String, String>>();

						for (final BannerComponentModel banner : homePageBanners.getBanners())
						{

							if (banner instanceof MplBigPromoBannerComponentModel)
							{
								final MplBigPromoBannerComponentModel bannerComponent = (MplBigPromoBannerComponentModel) banner;
								if (bannerComponent.getVisible() && homepageComponentService.showOnTimeRestriction(bannerComponent))
								{
									if (null != banner.getBannerView() && banner.getBannerView().getCode().equalsIgnoreCase("mobileView"))
									{
										final LinkedHashMap<String, String> moblileBanners = new LinkedHashMap<String, String>();
										moblileBanners.put(URL, bannerComponent.getBannerImage().getUrl());
										moblileBanners.put("href", bannerComponent.getUrlLink());
										moblileBanners.put("pk", bannerComponent.getPk().getLongValueAsString());
										mobileBannersSet.add(moblileBanners);
									}
									else
									{
										final LinkedHashMap<String, String> desktopBanners = new LinkedHashMap<String, String>();
										desktopBanners.put(URL, bannerComponent.getBannerImage().getUrl());
										desktopBannersSet.add(desktopBanners);
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
										final LinkedHashMap<String, String> moblileBanners = new LinkedHashMap<String, String>();
										moblileBanners.put(URL, bannerComponent.getBannerImage().getUrl());
										moblileBanners.put("href", bannerComponent.getUrlLink());
										moblileBanners.put("pk", bannerComponent.getPk().getLongValueAsString());
										mobileBannersSet.add(moblileBanners);
									}
									else
									{
										final LinkedHashMap<String, String> desktopBanners = new LinkedHashMap<String, String>();
										desktopBanners.put(URL, bannerComponent.getBannerImage().getUrl());
										desktopBannersSet.add(desktopBanners);
									}
								}
							}
							else
							{
								if (banner.getVisible() && homepageComponentService.showOnTimeRestriction(banner))
								{
									if (null != banner.getBannerView() && banner.getBannerView().getCode().equalsIgnoreCase("mobileView"))
									{
										final LinkedHashMap<String, String> moblileBanners = new LinkedHashMap<String, String>();
										moblileBanners.put(URL, banner.getMedia().getUrl());
										moblileBanners.put("href", banner.getUrlLink());
										moblileBanners.put("pk", banner.getPk().getLongValueAsString());
										mobileBannersSet.add(moblileBanners);
									}
									else
									{
										final LinkedHashMap<String, String> desktopBanners = new LinkedHashMap<String, String>();
										desktopBanners.put(URL, banner.getMedia().getUrl());
										desktopBannersSet.add(desktopBanners);
									}
								}
							}
							if (StringUtils.isNotEmpty(initLoad))
							{
								break;
							}

						}
						homePageBannerJson.put("desktopBanners", desktopBannersSet);
						homePageBannerJson.put("moblileBanners", mobileBannersSet);

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
		ampArray.add(homePageBannerJson);
		ampObj.put(ITEMS, ampArray);
		return ampObj;
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/getBrandsYouLove", method = RequestMethod.GET)
	public JSONObject getBrandsYouLove(@RequestParam(VERSION) final String version)
	{
		final JSONArray ampArray = new JSONArray();
		final JSONObject ampObj = new JSONObject();
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
		ampArray.add(brandsYouLoveJson);
		ampObj.put(ITEMS, ampArray);
		return ampObj;

	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/getBestPicks", method = RequestMethod.GET)
	public JSONObject getBestPicks(@RequestParam(VERSION) final String version)
	{
		final JSONObject ampObj = new JSONObject();
		final JSONArray ampArray = new JSONArray();
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
		ampArray.add(getBestPicksJson);
		ampObj.put(ITEMS, ampArray);
		return ampObj;
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/getProductsYouCare", method = RequestMethod.GET)
	public JSONObject getProductsYouCare(@RequestParam(VERSION) final String version)
	{
		final JSONArray ampArray = new JSONArray();
		final JSONObject ampObj = new JSONObject();
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
		ampArray.add(getProductsYouCareJson);
		ampObj.put(ITEMS, ampArray);
		return ampObj;
	}

	/* Home Page StayQued */
	@Override
	@SuppressWarnings("boxing")
	@ResponseBody
	@RequestMapping(value = "/getStayQuedHomepage", method = RequestMethod.GET)
	public JSONObject getStayQuedHomepage(@RequestParam(VERSION) final String version)
	{
		final JSONArray ampArray = new JSONArray();
		final JSONObject ampObj = new JSONObject();
		JSONObject getStayQuedHomepageJson = new JSONObject();
		try
		{
			final ContentSlotModel homepageSection5ASlot = cmsPageService.getContentSlotByUidForPage(HOMEPAGE,
					"Section5ASlot-Homepage",

					version);

			//return getJsonBanner(homepageSection5ASlot, "stayQued");
			getStayQuedHomepageJson = homepageComponentService.getJsonBannerAmp(homepageSection5ASlot, "stayQued");
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
		final Random rand = new Random();
		final int range = rand.nextInt(3) + 1;
		final JSONArray items = (JSONArray) getStayQuedHomepageJson.get("allBannerJsonObject");
		ampArray.add(items.get(range - 1));
		ampObj.put(ITEMS, ampArray);
		return ampObj;

	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/getNewAndExclusive", method = RequestMethod.GET)
	public JSONObject getNewAndExclusive(@RequestParam(VERSION) final String version)
	{
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		final JSONArray ampArray = new JSONArray();
		final JSONObject ampObj = new JSONObject();
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
		ampArray.add(newAndExclusiveJson);
		ampObj.put(ITEMS, ampArray);
		return ampObj;

	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/getCollectionShowcase", method = RequestMethod.GET)
	public JSONObject getCollectionShowcase(@RequestParam(VERSION) final String version)
	{
		final JSONArray ampArray = new JSONArray();
		final JSONObject ampObj = new JSONObject();
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
		ampArray.add(collectionShowcase);
		ampObj.put(ITEMS, ampArray);
		return ampObj;

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
								//showCaseItemJson.put("text", showcaseItem.getText());
								showCaseItemJson.put("text", showcaseItem.getText_mobile());

							}
						}
						if (null != showcaseItem.getBannerText())
						{
							if (StringUtils.isNotEmpty(showcaseItem.getBannerText()))
							{
								//showCaseItemJson.put("bannerText", showcaseItem.getBannerText());
								showCaseItemJson.put("bannerText", showcaseItem.getBannerTextMobile());
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
						//showCaseItemJson.put("headerText", headerText);
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
				if (StringUtils.isNotEmpty(showcaseItem.getBannerTextMobile()))
				{
					showCaseItemJson.put("bannerText", showcaseItem.getBannerTextMobile());
				}
			}
			if (StringUtils.isNotEmpty(showcaseItem.getText_mobile()))
			{
				showCaseItemJson.put("text", showcaseItem.getText_mobile());
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

	private long calculateDays(final Date dateEarly, final Date dateLater)
	{
		return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
	}

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

	//	@RequestMapping(value = "/getAllCategories", method = RequestMethod.GET)
	//	@ResponseBody
	//	public DepartmentListHierarchyDataAmp fetchAllCategories()
	//	{
	//		final DepartmentListHierarchyDataAmp shopByDeptData = new DepartmentListHierarchyDataAmp();
	//		final List<DepartmentHierarchyData> deptDataList = new ArrayList<DepartmentHierarchyData>();
	//		//	List<DepartmentSubHierarchyData> subDeptDataList = null;
	//		DepartmentHierarchyData deptData = null;
	//		DepartmentSubHierarchyData subDeptData = null;
	//		List<NavigationBarComponentModel> departmentList = new ArrayList<NavigationBarComponentModel>();
	//		Date modifiedTime = null;
	//		subDeptData = new DepartmentSubHierarchyData();
	//
	//		DepartmentSuperSubHierarchyData thirdLevelCat = null;
	//		thirdLevelCat = new DepartmentSuperSubHierarchyData();
	//		List<DepartmentSubHierarchyData> subDeptDataList = null;
	//		List<DepartmentSuperSubHierarchyData> superSubDeptDataList = null;
	//
	//
	//		try
	//		{
	//
	//			String componentUid = null;
	//			if (null != configurationService && null != configurationService.getConfiguration()
	//					&& null != ControllerConstants.SHOPBYDEPTCOMPONENT)
	//			{
	//				componentUid = configurationService.getConfiguration().getString(ControllerConstants.SHOPBYDEPTCOMPONENT);
	//			}
	//
	//			final NavigationBarCollectionComponentModel shopByDeptComponent = (NavigationBarCollectionComponentModel) cmsComponentService
	//					.getSimpleCMSComponent(componentUid);
	//
	//
	//
	//
	//			if (CollectionUtils.isNotEmpty(shopByDeptComponent.getComponents()))
	//			{
	//				departmentList = shopByDeptComponent.getComponents();
	//			}
	//
	//			for (final NavigationBarComponentModel dept : departmentList)
	//			{
	//				final CMSLinkComponentModel superNode = dept.getLink();
	//				final CMSNavigationNodeModel linkNode = dept.getNavigationNode();
	//				deptData = new DepartmentHierarchyData();
	//				subDeptDataList = new ArrayList<DepartmentSubHierarchyData>();
	//
	//				//Super category
	//				if (null != modifiedTime && null != dept.getModifiedtime() && dept.getModifiedtime().compareTo(modifiedTime) > 0)
	//				{
	//					modifiedTime = dept.getModifiedtime();
	//				}
	//
	//				if (null != superNode.getCategory() && StringUtils.isNotEmpty(superNode.getCategory().getCode()))
	//				{
	//					deptData.setSuper_category_id(superNode.getCategory().getCode());
	//				}
	//				if (StringUtils.isNotEmpty(superNode.getLinkName()))
	//				{
	//					deptData.setSuper_category_name(superNode.getLinkName());
	//				}
	//
	//				//TISQAUAT-613--Start
	//				if (StringUtils.isNotEmpty(superNode.getDeeplinkType()))
	//				{
	//					deptData.setDeepLinkType(superNode.getDeeplinkType());
	//				}
	//				if (StringUtils.isNotEmpty(superNode.getDeeplinkTypeId()))
	//				{
	//					deptData.setDeepLinkId(superNode.getDeeplinkTypeId());
	//				}
	//				if (StringUtils.isNotEmpty(superNode.getDeeplinkTypeVal()))
	//				{
	//					deptData.setDeepLinkVal(superNode.getDeeplinkTypeVal());
	//				}
	//				//TISQAUAT-613--End
	//
	//				if (StringUtils.isNotEmpty(superNode.getUrl()))
	//				{
	//					deptData.setUrl(superNode.getUrl());
	//				}
	//
	//				//Sub category
	//				if (CollectionUtils.isNotEmpty(linkNode.getChildren()))
	//				{
	//					for (final CMSNavigationNodeModel sublink : linkNode.getChildren())
	//					{
	//						if (CollectionUtils.isNotEmpty(sublink.getLinks()))
	//						{
	//							subDeptData = new DepartmentSubHierarchyData();
	//							superSubDeptDataList = new ArrayList<DepartmentSuperSubHierarchyData>();
	//
	//							final CMSLinkComponentModel sublinknode = sublink.getLinks().get(0);
	//
	//							if (null != modifiedTime && null != sublink.getModifiedtime()
	//									&& sublink.getModifiedtime().compareTo(modifiedTime) > 0)
	//							{
	//								modifiedTime = sublink.getModifiedtime();
	//							}
	//
	//							if (null != sublinknode.getCategory() && StringUtils.isNotEmpty(sublinknode.getCategory().getCode()))
	//							{
	//								subDeptData.setSub_category_id(sublinknode.getCategory().getCode());
	//							}
	//							if (StringUtils.isNotEmpty(sublink.getTitle()))
	//							{
	//								subDeptData.setSub_category_name(sublink.getTitle());
	//							}
	//							//TISQAUAT-613--Start
	//							if (StringUtils.isNotEmpty(sublinknode.getDeeplinkType()))
	//							{
	//								subDeptData.setDeepLinkType(sublinknode.getDeeplinkType());
	//							}
	//							if (StringUtils.isNotEmpty(sublinknode.getDeeplinkTypeId()))
	//							{
	//								subDeptData.setDeepLinkId(sublinknode.getDeeplinkTypeId());
	//							}
	//							if (StringUtils.isNotEmpty(sublinknode.getDeeplinkTypeVal()))
	//							{
	//								subDeptData.setDeepLinkVal(sublinknode.getDeeplinkTypeVal());
	//							}
	//
	//							//TISQAUAT-613--End
	//							if (StringUtils.isNotEmpty(sublinknode.getUrl()))
	//							{
	//								subDeptData.setUrl(sublinknode.getUrl());
	//							}
	//							int count = 0;
	//							//Super sub category
	//							for (final CMSLinkComponentModel thirdLevelsublink : sublink.getLinks())
	//							{
	//
	//								if (count > 0)
	//								{
	//									thirdLevelCat = new DepartmentSuperSubHierarchyData();
	//
	//									if (null != modifiedTime && null != thirdLevelsublink.getModifiedtime()
	//											&& thirdLevelsublink.getModifiedtime().compareTo(modifiedTime) > 0)
	//									{
	//										modifiedTime = thirdLevelsublink.getModifiedtime();
	//									}
	//
	//									if (null != thirdLevelsublink.getCategory()
	//											&& StringUtils.isNotEmpty(thirdLevelsublink.getCategory().getCode()))
	//									{
	//										thirdLevelCat.setSuper_sub_category_id(thirdLevelsublink.getCategory().getCode());
	//									}
	//									if (StringUtils.isNotEmpty(thirdLevelsublink.getLinkName()))
	//									{
	//										thirdLevelCat.setSuper_sub_category_name(thirdLevelsublink.getLinkName());
	//									}
	//									//TISQAUAT-613--Start
	//									if (StringUtils.isNotEmpty(thirdLevelsublink.getDeeplinkType()))
	//									{
	//										thirdLevelCat.setDeepLinkType(thirdLevelsublink.getDeeplinkType());
	//									}
	//									if (StringUtils.isNotEmpty(thirdLevelsublink.getDeeplinkTypeId()))
	//									{
	//										thirdLevelCat.setDeepLinkId(thirdLevelsublink.getDeeplinkTypeId());
	//									}
	//									if (StringUtils.isNotEmpty(thirdLevelsublink.getDeeplinkTypeVal()))
	//									{
	//										thirdLevelCat.setDeepLinkVal(thirdLevelsublink.getDeeplinkTypeVal());
	//									}
	//									//TISQAUAT-613--End
	//									if (StringUtils.isNotEmpty(thirdLevelsublink.getUrl()))
	//									{
	//										thirdLevelCat.setUrl(thirdLevelsublink.getUrl());
	//									}
	//
	//									superSubDeptDataList.add(thirdLevelCat);
	//								}
	//								count++;
	//
	//							}
	//
	//							subDeptData.setSupersubCategories(superSubDeptDataList);
	//							subDeptDataList.add(subDeptData);
	//						}
	//					}
	//
	//					if (!subDeptDataList.isEmpty())
	//					{
	//						deptData.setSubCategories(subDeptDataList);
	//					}
	//				}
	//
	//				deptDataList.add(deptData);
	//			}
	//
	//			if (null != modifiedTime && !StringUtils.isEmpty(modifiedTime.toString()))
	//			{
	//				shopByDeptData.setModifiedTime(modifiedTime.toString());
	//			}
	//
	//			if (!deptDataList.isEmpty())
	//			{
	//				shopByDeptData.setItems(deptDataList);
	//			}
	//
	//		}
	//		catch (final CMSItemNotFoundException e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, ControllerConstants.B9004);
	//		}
	//		catch (final Exception e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, ControllerConstants.B9004);
	//		}
	//		return shopByDeptData;
	//	}

	/**
	 *
	 * @param componentUid
	 * @param term
	 * @param category
	 * @return AutocompleteResultData
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings("boxing")
	@ResponseBody
	@RequestMapping(value = "/autocomplete/" + COMPONENT_UID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public MplAutocompleteAmpData getAutocompleteSuggestions(@PathVariable final String componentUid,
			@RequestParam("term") final String term, @RequestParam("category") final String category)
			throws CMSItemNotFoundException
	{
		final MplAutocompleteResultData resultData = new MplAutocompleteResultData();
		final MplAutocompleteAmpData ampResultData = new MplAutocompleteAmpData();
		try
		{
			final List<AutocompleteSuggestionData> suggestions = productSearchFacade.getAutocompleteSuggestions(term);
			if (CollectionUtils.isNotEmpty(suggestions) && suggestions.size() > 0)
			{

				resultData.setSuggestions(suggestions);
			}
			else
			{
				String substr = "";
				substr = term.substring(0, term.length() - 1);

				resultData.setSuggestions(productSearchFacade.getAutocompleteSuggestions(substr));


			}

			//resultData.setSuggestions(productSearchFacade.getAutocompleteSuggestions(term));
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			/*********** Fixing for Defect TISPRO-58 and TISPRD-346 Start */
			String tempSuggestion = "";
			final List<AutocompleteSuggestionData> suggestionsList = resultData.getSuggestions();
			if (CollectionUtils.isNotEmpty(suggestionsList))
			{
				final String firstSuggestion = suggestionsList.get(0).getTerm();

				final StringTokenizer termWordCount = new StringTokenizer(term, " ");
				final int count = termWordCount.countTokens();

				final String[] suggestedTerm = firstSuggestion.split(" ");
				for (int i = 0; i < count; i++)
				{
					if (i > 0)
					{
						tempSuggestion = tempSuggestion + " " + suggestedTerm[i];
					}
					else
					{
						tempSuggestion = suggestedTerm[i];
					}
				}
			}
			else
			{
				tempSuggestion = term;
			}

			searchQueryData.setValue(tempSuggestion);
			/*********** Fixing for Defect TISPRO-58 and TISPRD-346 End */
			//searchQueryData.setValue(resultData.getSuggestions().size() > 0 ? resultData.getSuggestions().get(0).getTerm() : term);
			searchState.setQuery(searchQueryData);
			searchState.setSns(true);

			final PageableData pageableData = null;

			ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;


			if (CollectionUtils.isNotEmpty(resultData.getSuggestions()))
			{

				if (category.startsWith(MarketplaceCoreConstants.ALL_CATEGORY))
				{
					searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
							.textSearch(searchState, pageableData);
					resultData.setCategories(searchPageData.getSnsCategories());
					resultData.setBrands(searchPageData.getAllBrand());
					//allSearchFlag = true;
				}
				else
				//if (!allSearchFlag)
				{
					if (category.startsWith(DROPDOWN_CATEGORY) || category.startsWith(DROPDOWN_BRAND))
					{
						searchPageData = searchFacade.categorySearch(category, searchState, pageableData);
					}
					else
					{
						searchPageData = searchFacade.dropDownSearch(searchState, category, "sellerId", pageableData);
					}
					resultData.setCategories(searchPageData.getSnsCategories());
					resultData.setBrands(searchPageData.getAllBrand());

				}




				final List<ProductData> suggestedProducts = searchPageData.getResults();

				//this is done to remove some of the data issues where we
				//have null images or price
				if (suggestedProducts != null)
				{
					cleanSearchResults(suggestedProducts);
					//resultData.setProductNames(subList(suggestedProducts, component.getMaxSuggestions()));
					resultData.setProducts(suggestedProducts);
					resultData.setSearchTerm(resultData.getSuggestions().size() > 0 ? resultData.getSuggestions().get(0).getTerm()
							: term);
				}


			}
		}
		catch (final EtailNonBusinessExceptions eb)
		{
			LOG.debug("Error occured in getAutocompleteSuggestions :" + eb.getMessage());
		}
		final List<MplAutocompleteResultData> listAmp = new ArrayList<MplAutocompleteResultData>();
		listAmp.add(resultData);
		ampResultData.setItems(listAmp);
		return ampResultData;
	}

	/**
	 *
	 * @param resultData
	 *
	 */
	private void cleanSearchResults(final List<ProductData> resultData)
	{
		for (final ProductData productData : resultData)
		{
			if (productData.getImages() == null)
			{
				final List<ImageData> images = new ArrayList<ImageData>(Arrays.asList(new ImageData()));
				productData.setImages(images);
			}
			if (productData.getPrice() == null)
			{
				productData.setPrice(new PriceData());
			}
		}
	}
}
