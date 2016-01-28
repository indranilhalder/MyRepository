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
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

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

import com.tisl.mpl.core.model.MplShowcaseComponentModel;
import com.tisl.mpl.core.model.MplShowcaseItemComponentModel;
import com.tisl.mpl.facade.brand.BrandFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.model.cms.components.BestPicksCarouselComponentModel;
import com.tisl.mpl.model.cms.components.CMSMediaParagraphComponentModel;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;


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

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	private static final String MISSING_IMAGE_URL = "/store/_ui/desktop/theme-blue/images/missing-product-300x300.jpg";

	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.GALLERY);

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
	public JSONObject getBrandsYouLove()
	{
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		final JSONObject brandsYouLoveJson = new JSONObject();
		final ContentSlotModel homepageSection3Slot = cmsPageService.getContentSlotByUidForPage("homepage", "Section3Slot-Homepage",
				"Online");
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

				String title = "";
				if (StringUtils.isNotEmpty(brandsYouLoveComponent.getTitle()))
				{
					title = brandsYouLoveComponent.getTitle();
				}
				brandsYouLoveJson.put("title", title);
				final JSONArray subComponentJsonArray = new JSONArray();
				if (CollectionUtils.isNotEmpty(brandsYouLoveComponent.getShowcaseItems()))
				{

					String brandLogoUrl = "";
					for (final MplShowcaseItemComponentModel showcaseItem : brandsYouLoveComponent.getShowcaseItems())
					{
						final JSONObject showCaseItemJson = new JSONObject();
						showCaseItemJson.put("compId", showcaseItem.getUid());
						if (null != showcaseItem.getLogo() && StringUtils.isNotEmpty(showcaseItem.getLogo().getURL()))
						{
							brandLogoUrl = showcaseItem.getLogo().getURL();
						}
						showCaseItemJson.put("brandLogoUrl", brandLogoUrl);
						showCaseItemJson.put("showByDefault", showcaseItem.getShowByDefault());
						if (showcaseItem.getShowByDefault().booleanValue())
						{
							ProductData firstProduct = null;
							ProductData secondProduct = null;

							if (showcaseItem.getProduct1() != null)
							{
								firstProduct = productFacade.getProductForOptions(showcaseItem.getProduct1(), PRODUCT_OPTIONS);
								showCaseItemJson.put("firstProductImageUrl", getProductPrimaryImageUrl(firstProduct));
								showCaseItemJson.put("firstProductTitle", firstProduct.getProductTitle());
								showCaseItemJson.put("firstProductUrl", firstProduct.getUrl());
							}
							if (showcaseItem.getProduct2() != null)
							{
								secondProduct = productFacade.getProductForOptions(showcaseItem.getProduct2(), PRODUCT_OPTIONS);
								showCaseItemJson.put("secondproductImageUrl", getProductPrimaryImageUrl(secondProduct));
								showCaseItemJson.put("secondProductTitle", secondProduct.getProductTitle());
								showCaseItemJson.put("secondProductUrl", secondProduct.getUrl());
							}
							if (StringUtils.isNotEmpty(showcaseItem.getText()))
							{
								showCaseItemJson.put("text", showcaseItem.getText());
							}

							if (null != showcaseItem.getBannerImage() && StringUtils.isNotEmpty(showcaseItem.getBannerImage().getURL()))
							{
								showCaseItemJson.put("bannerImageUrl", showcaseItem.getBannerImage().getURL());
							}

							if (StringUtils.isNotEmpty(showcaseItem.getBannerText()))
							{
								showCaseItemJson.put("bannerText", showcaseItem.getBannerText());
							}

						}
						subComponentJsonArray.add(showCaseItemJson);
					}
				}

				brandsYouLoveJson.put("subComponents", subComponentJsonArray);


			}
		}


		return brandsYouLoveJson;

	}


	@ResponseBody
	@RequestMapping(value = "/getBestPicks", method = RequestMethod.GET)
	public JSONObject getBestPicks()
	{
		LOG.info("Check 1 :: Inside getBestPicks emthod");
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		final JSONObject bestPicks = new JSONObject();
		final ContentSlotModel homepageSection4CSlot = cmsPageService.getContentSlotByUidForPage("homepage",
				"Section4CSlot-Homepage", "Online");

		if (CollectionUtils.isNotEmpty(homepageSection4CSlot.getCmsComponents()))
		{
			LOG.info("Check 2 :: Inside if (CollectionUtils.isNotEmpty(homepageSection4CSlot.getCmsComponents()))");
			components = homepageSection4CSlot.getCmsComponents();
		}

		for (final AbstractCMSComponentModel component : components)
		{
			LOG.info("Check 3 :: Inside for (final AbstractCMSComponentModel component : components)");
			LOG.info("Found Component>>>>with id :::" + component.getUid());
			if (component instanceof BestPicksCarouselComponentModel)
			{
				LOG.info("Check 4 :: Inside if (component instanceof BestPicksCarouselComponentModel)");
				final BestPicksCarouselComponentModel bestPickCarouselComponent = (BestPicksCarouselComponentModel) component;
				String title = "";
				if (StringUtils.isNotEmpty(bestPickCarouselComponent.getTitle()))
				{
					LOG.info("Check 5 :: Inside if (StringUtils.isNotEmpty(bestPickCarouselComponent.getTitle()))");
					title = bestPickCarouselComponent.getTitle();
				}

				bestPicks.put("title", title);

				final JSONArray subComponentJsonArray = new JSONArray();
				if (CollectionUtils.isNotEmpty(bestPickCarouselComponent.getBestPicksItems()))
				{
					LOG.info("Check 6 :: Inside if (CollectionUtils.isNotEmpty(bestPickCarouselComponent.getBestPicksItems()))");
					String imageURL = "";
					String text = "";
					String linkUrl = "";

					for (final CMSMediaParagraphComponentModel bestPickItem : bestPickCarouselComponent.getBestPicksItems())
					{
						LOG.info("Check 7 :: Inside for (final CMSMediaParagraphComponentModel bestPickItem");
						final JSONObject bestPickItemJson = new JSONObject();

						if (null != bestPickItem.getMedia().getURL() && StringUtils.isNotEmpty(bestPickItem.getMedia().getURL()))
						{
							LOG.info("Check 8 :: media null check");
							imageURL = bestPickItem.getMedia().getURL();
						}

						bestPickItemJson.put("imageUrl", imageURL);

						if (null != bestPickItem.getContent() && StringUtils.isNotEmpty(bestPickItem.getContent()))
						{
							LOG.info("Check 9 :: text null check");
							text = bestPickItem.getContent();
						}

						bestPickItemJson.put("text", text);

						if (null != bestPickItem.getUrl() && StringUtils.isNotEmpty(bestPickItem.getUrl()))
						{
							LOG.info("Check 10 :: url null check");
							linkUrl = bestPickItem.getUrl();
						}

						bestPickItemJson.put("url", linkUrl);

						subComponentJsonArray.add(bestPickItemJson);
					}
					LOG.info("Check 11 :: Outside for (final CMSMediaParagraphComponentModel bestPickItem");
				}
				LOG.info("Check 12 :: Outside if (CollectionUtils.isNotEmpty(bestPickCarouselComponent.getBestPicksItems()))");
				bestPicks.put("subItems", subComponentJsonArray);

			}
		}
		return bestPicks;
	}

	/**
	 * @param firstProduct
	 * @return
	 */
	private String getProductPrimaryImageUrl(final ProductData productData)
	{
		final List<ImageData> images = (List<ImageData>) productData.getImages();
		String imageUrl = MISSING_IMAGE_URL;
		if (images != null)
		{
			if (images.get(0).getUrl() != null)
			{
				imageUrl = images.get(0).getUrl();
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


}

