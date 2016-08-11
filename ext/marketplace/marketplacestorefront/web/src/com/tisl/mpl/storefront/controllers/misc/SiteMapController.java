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

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.core.model.DepartmentCollectionComponentModel;
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
	private DefaultCMSContentSlotService contentSlotService;

	@Autowired
	private HomepageComponentService homepageComponentService;

	@RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces = "application/xml")
	public String getSitemapXml(final Model model, final HttpServletResponse response)
	{
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();

		final String mediaUrlForSite = siteBaseUrlResolutionService.getMediaUrlForSite(currentSite, false, "");

		final List<String> siteMapUrls = new ArrayList<>();

		final Collection<MediaModel> siteMaps = currentSite.getSiteMaps();
		for (final MediaModel siteMap : siteMaps)
		{
			siteMapUrls.add(mediaUrlForSite + siteMap.getURL());
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

		final ContentSlotModel contentSlotModel = contentSlotService.getContentSlotForId("NavigationBarSlot");
		final List<AbstractCMSComponentModel> componentLists = contentSlotModel.getCmsComponents();
		for (final AbstractCMSComponentModel model : componentLists)
		{
			if (model instanceof DepartmentCollectionComponentModel)
			{
				final DepartmentCollectionComponentModel deptModel = (DepartmentCollectionComponentModel) model;
				final Collection<CategoryModel> mainDepts = deptModel.getDepartmentCollection();
				for (final CategoryModel categoryModel : mainDepts)
				{
					try
					{
						final Map<CategoryModel, Collection<CategoryModel>> innerLevelMap = new HashMap<CategoryModel, Collection<CategoryModel>>();

						final CategoryModel department = categoryService.getCategoryForCode(categoryModel.getCode());
						final Collection<CategoryModel> secondLevelCategories = department.getCategories();



						// Iterating through the second level categories
						for (final CategoryModel secondLevelCategory : secondLevelCategories)
						{
							/* code for TISPRD-3183 */
							String categoryPathChildlevel2 = GenericUtilityMethods.buildPathString(homepageComponentService
									.getCategoryPath(secondLevelCategory));
							if (StringUtils.isNotEmpty(categoryPathChildlevel2))
							{
								categoryPathChildlevel2 = URLDecoder.decode(categoryPathChildlevel2, "UTF-8");
								categoryPathChildlevel2 = categoryPathChildlevel2.toLowerCase();
								categoryPathChildlevel2 = GenericUtilityMethods.changeUrl(categoryPathChildlevel2);
							}
							secondLevelCategory.setName(secondLevelCategory.getName() + "||" + categoryPathChildlevel2);


							// Fetching the third level category against a second
							// level category
							final Collection<CategoryModel> thirdLevelCategory = secondLevelCategory.getCategories();

							for (final CategoryModel thirdLevelCategories : thirdLevelCategory)
							{

								String categoryPathChildlevel3 = GenericUtilityMethods.buildPathString(homepageComponentService
										.getCategoryPath(thirdLevelCategories));
								if (StringUtils.isNotEmpty(categoryPathChildlevel3))
								{
									categoryPathChildlevel3 = URLDecoder.decode(categoryPathChildlevel3, "UTF-8");
									categoryPathChildlevel3 = categoryPathChildlevel3.toLowerCase();
									categoryPathChildlevel3 = GenericUtilityMethods.changeUrl(categoryPathChildlevel3);
								}

								thirdLevelCategories.setName(thirdLevelCategories.getName() + "||" + categoryPathChildlevel3);
							}
							/* code end for TISPRD-3183 */
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

}
