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
package com.tisl.mpl.storefront.controllers.misc;

import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSContentSlotService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.media.MediaService;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.HomepageComponentService;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


@Controller
@Scope("tenant")
public class SiteMapController extends AbstractPageController
{
	private final String SITEMAP_CMS_PAGE = "sitemap";

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Resource(name = "siteBaseUrlResolutionService")
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DefaultCMSContentSlotService contentSlotService;

	@Resource
	private HomepageComponentService homepageComponentService;

	//TPR-1285 Dynamic sitemap Changes Start
	@Resource(name = "mediaService")
	private MediaService mediaService;
	@Autowired
	private HttpServletRequest request;


	private final static String SITEMAPURLPATTERN = "/{sitemapName:.+sitemap+.*}.xml";

	//TPR-1285 Dynamic sitemap Changes Ends

	@RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces = "application/xml")
	public String getSitemapXml(final Model model, final HttpServletResponse response)
	{
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();

		String mediaUrlForSite = siteBaseUrlResolutionService.getMediaUrlForSite(currentSite, false, "");

		final String usePropertyFile = configurationService.getConfiguration().getString("mpl.sitemap.propertyfile.use", "false");

		if (usePropertyFile.equalsIgnoreCase("true"))
		{
			mediaUrlForSite = configurationService.getConfiguration().getString("mpl.sitemap.url", "www.tatacliq.com");
		}

		final List<String> siteMapUrls = new ArrayList<>();

		final Collection<MediaModel> siteMaps = currentSite.getSiteMaps();
		for (final MediaModel siteMap : siteMaps)
		{
			siteMapUrls.add(mediaUrlForSite + siteMap.getCode());
		}
		model.addAttribute("siteMapUrls", siteMapUrls);

		return ControllerConstants.Views.Pages.Misc.MiscSiteMapPage;
	}

	/**
	 * @description method is called to get the sitemap
	 * @return String
	 */
	@RequestMapping(value = RequestMappingUrlConstants.SITEMAP, method = RequestMethod.GET)
	public String sitemap(
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = ModelAttributetConstants.ONE_VAL) final int page,
			final Model modelSpring) throws Exception
	{


		final Map<CategoryModel, Map<CategoryModel, Collection<CategoryModel>>> megaMap = new LinkedHashMap<CategoryModel, Map<CategoryModel, Collection<CategoryModel>>>();

		final ArrayList<CategoryModel> departments = new ArrayList<CategoryModel>();

		final ContentSlotModel contentSlotModel = contentSlotService.getContentSlotForId("NavigationBarSlot");
		final List<AbstractCMSComponentModel> componentLists = contentSlotModel.getCmsComponents();
		for (final AbstractCMSComponentModel model : componentLists)
		{
			if (model instanceof NavigationBarCollectionComponentModel)
			{
				final NavigationBarCollectionComponentModel deptModel = (NavigationBarCollectionComponentModel) model;
				final Collection<NavigationBarComponentModel> navigationBars = deptModel.getComponents();

				for (final NavigationBarComponentModel navigationBar : navigationBars)
				{
					final CategoryModel department = navigationBar.getLink().getCategory();
					departments.add(department);
				}

				for (final CategoryModel categoryModel : departments)
				{
					try
					{
						final Map<CategoryModel, Collection<CategoryModel>> innerLevelMap = new HashMap<CategoryModel, Collection<CategoryModel>>();

						final CategoryModel department = categoryService.getCategoryForCode(categoryModel.getCode());
						final Collection<CategoryModel> secondLevelCategories = department.getCategories();



						// Iterating through the second level categories
						for (final CategoryModel secondLevelCategory : secondLevelCategories)
						{
							/* code changes for TISPRD-3183 */
							// Fetching the third level category against a second
							// level category
							final Collection<CategoryModel> thirdLevelCategory = secondLevelCategory.getCategories();

							for (final CategoryModel thirdLevelCategories : thirdLevelCategory)
							{

								final String categoryPathChildlevel3 = getCategoryPath(thirdLevelCategories);

								final StringBuilder catName3 = new StringBuilder();
								catName3.append(thirdLevelCategories.getName()).append("||").append(categoryPathChildlevel3);
								thirdLevelCategories.setName(catName3.toString());
							}
							final String categoryPathChildlevel2 = getCategoryPath(secondLevelCategory);

							final StringBuilder catName2 = new StringBuilder();
							catName2.append(secondLevelCategory.getName()).append("||").append(categoryPathChildlevel2);
							secondLevelCategory.setName(catName2.toString());

							/* code changes end for TISPRD-3183 */
							// Storing the third level categories in a map
							//thirdLevelCategoryMap.put(secondLevelCategory.getCode(), thirdLevelCategory);
							innerLevelMap.put(secondLevelCategory, thirdLevelCategory);
						}

						// Storing the second level categories in a map
						//secondLevelCategoryMap.put(department.getCode(), secondLevelCategories);
						megaMap.put(categoryModel, innerLevelMap);
					}
					catch (final EtailBusinessExceptions businessException)
					{
						ExceptionUtil.etailBusinessExceptionHandler(businessException, null);
					}
					catch (final EtailNonBusinessExceptions nonBusinessException)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler(nonBusinessException);
					}
					catch (final Exception exception)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
					}
				}
			}
		}

		modelSpring.addAttribute("megaMap", megaMap);

		storeCmsPageInModel(modelSpring, getContentPageForLabelOrId(SITEMAP_CMS_PAGE));
		setUpMetaDataForContentPage(modelSpring, getContentPageForLabelOrId(SITEMAP_CMS_PAGE));
		return getViewForPage(modelSpring);
	}

	private String getCategoryPath(final CategoryModel Category)
	{
		String categoryPathChildlevel = null;
		try
		{
			categoryPathChildlevel = GenericUtilityMethods.buildPathString(homepageComponentService.getCategoryPath(Category));
			if (StringUtils.isNotEmpty(categoryPathChildlevel))
			{
				categoryPathChildlevel = URLDecoder.decode(categoryPathChildlevel, ModelAttributetConstants.UTF8);
				categoryPathChildlevel = categoryPathChildlevel.toLowerCase();
				categoryPathChildlevel = GenericUtilityMethods.changeUrl(categoryPathChildlevel);
			}
		}
		catch (final EtailBusinessExceptions businessException)
		{
			ExceptionUtil.etailBusinessExceptionHandler(businessException, null);
		}
		catch (final EtailNonBusinessExceptions nonBusinessException)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(nonBusinessException);
		}
		catch (final Exception exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
		}


		return categoryPathChildlevel;
	}

	/**
	 * TPR-1285 Dynamic sitemap Changes This method returns the sub pages under sitemap page for TPR-1285 Dynamic sitemap
	 *
	 * @param sitemapName
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = SITEMAPURLPATTERN, method = RequestMethod.GET, produces = "application/xml")
	public String getIndividualSitemapXml(@PathVariable("sitemapName") final String sitemapName, final Model model)
	{
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();

		final Collection<MediaModel> siteMaps = currentSite.getSiteMaps();
		final String mediaName = sitemapName + ".xml";
		for (final MediaModel siteMap : siteMaps)
		{
			if (siteMap.getCode().equalsIgnoreCase(mediaName))
			{
				final byte[] mediaByte = mediaService.getDataFromMedia(siteMap);
				final String xmlString = new String(mediaByte);
				LOG.info(xmlString);
				model.addAttribute("siteMapXml", xmlString);
			}
		}

		return ControllerConstants.Views.Pages.Misc.MiscIndividualSiteMapPage;
	}


}
