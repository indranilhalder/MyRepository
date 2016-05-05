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


import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCategoryPageController;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commercesearch.model.SolrHeroProductDefinitionModel;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.HeroProductDefinitionService;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.controllers.pages.SearchPageController.UserPreferencesData;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Controller for a category page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/c")
public class CategoryPageController extends AbstractCategoryPageController
{
	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "cmsPageService")
	private MplCmsPageService mplCmsPageService;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	@Autowired
	private HeroProductDefinitionService heroService;
	//	@Resource(name = "accProductFacade")
	//	private ProductFacade productFacade;
	//	@Resource
	//	private BuyBoxFacade buyBoxFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;
	//private static final String NEW_CATEGORY_URL_PATTERN = "/**/c-{categoryCode:.*}";
	//private static final String NEW_CATEGORY_URL_PATTERN_PAGINATION = "/**/c-{categoryCode:.*}/page-{page}";
	//	private static final String LAST_LINK_CLASS = "active";

	private static final String PAGE = "page";

	protected static final Logger LOG = Logger.getLogger(CategoryPageController.class);
	//Added For TISPRD-1243
	private static final String DROPDOWN_BRAND = "MBH";
	private static final String DROPDOWN_CATEGORY = "MSH";

	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String category(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize,
			@RequestParam(value = "searchCategory", required = false) String dropDownText, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException
	{
		String searchCode = new String(categoryCode);

		//applying search filters
		if (searchQuery != null)
		{
			getfilterListCountForSize(searchQuery);
			model.addAttribute("sizeCount", Integer.valueOf(getfilterListCountForSize(searchQuery)));
			model.addAttribute("searchQueryValue", searchQuery);
		}
		//Storing the user preferred search results count
		updateUserPreferences(pageSize);

		List<ProductModel> heroProducts = new ArrayList<ProductModel>();
		if (!(searchCode.substring(0, 5).equals(categoryCode))
				&& categoryCode.startsWith(MplConstants.SALES_HIERARCHY_ROOT_CATEGORY_CODE))
		{
			searchCode = searchCode.substring(0, 5);

		}
		model.addAttribute("searchCode", searchCode);
		model.addAttribute("isCategoryPage", Boolean.TRUE);
		final CategoryModel category = categoryService.getCategoryForCode(categoryCode);
		//Set the drop down text if the attribute is not empty or null
		if (dropDownText != null && !dropDownText.isEmpty())
		//Added For TISPRD-1243

		{

			if (dropDownText.startsWith(DROPDOWN_CATEGORY) || dropDownText.startsWith(DROPDOWN_BRAND))

			{
				final CategoryModel categoryModel = categoryService.getCategoryForCode(dropDownText);

				if (categoryModel != null)
				{
					dropDownText = (StringUtils.isNotEmpty(categoryModel.getName())) ? categoryModel.getName() : dropDownText;

				}
			}
			//Added For TISPRD-1243
			model.addAttribute("dropDownText", dropDownText);

		}
		else
		{
			//Set the search drop down text to category name
			/*
			 * Getting all Hero products as configured in back office
			 */
			final SolrHeroProductDefinitionModel solrModel = heroService.getSolrHeroProductDefinitionForCategory(category);
			if (null != solrModel)
			{
				heroProducts = solrModel.getProducts();
			}
			final String categoryName = (category == null) ? "" : category.getName();
			model.addAttribute("dropDownText", categoryName);
		}
		int count = getSearchPageSize();
		//Check if there is a landing page for the category
		try
		{
			final UserPreferencesData preferencesData = updateUserPreferences(pageSize);
			if (preferencesData != null && preferencesData.getPageSize() != null)
			{
				count = preferencesData.getPageSize().intValue();
			}

			if (category != null)
			{

				final String redirection = checkRequestUrl(request, response, getCategoryModelUrlResolver().resolve(category));
				if (StringUtils.isNotEmpty(redirection))
				{
					return redirection;
				}

				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
						categoryCode, searchQuery, page, showMode, sortCode, count);

				final List<ProductData> normalProductDatas = searchPageData.getResults();
				//Set department hierarchy
				//if (normalProductDatas.size() > 0)
				if (CollectionUtils.isNotEmpty(normalProductDatas))
				{
					model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
				}



				final String categoryName = category.getName();

				final ContentPageModel categoryLandingPage = getLandingPageForCategory(category);
				//model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				//Collections.singletonList(new Breadcrumb("#", categoryName, LAST_LINK_CLASS)));
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, categoryName, false));
				populateModel(model, searchPageData, ShowMode.Page);
				model.addAttribute("normalProducts", normalProductDatas);
				model.addAttribute("showCategoriesOnly", Boolean.FALSE);
				storeCmsPageInModel(model, categoryLandingPage);


			}
		}
		catch (final CMSItemNotFoundException e)
		{
			try
			{
				final UserPreferencesData preferencesData = updateUserPreferences(pageSize);
				if (preferencesData != null && preferencesData.getPageSize() != null)
				{
					count = preferencesData.getPageSize().intValue();
				}

				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
						categoryCode, searchQuery, page, showMode, sortCode, count);
				final String performSearch = performSearchAndGetResultsPage(categoryCode, searchQuery, page, showMode, sortCode,
						model, request, response);

				final List<ProductData> commonNormalProducts = new ArrayList<ProductData>();
				final List<ProductData> normalProductDatas = searchPageData.getResults();

				if (null != normalProductDatas)
				{
					for (final ProductData normalProduct : normalProductDatas)
					{
						for (final ProductModel heroProduct : heroProducts)
						{
							if (normalProduct.getCode().equalsIgnoreCase(heroProduct.getCode()))
							{
								commonNormalProducts.add(normalProduct);
							}
						}
					}
				}
				if (!commonNormalProducts.isEmpty())
				{
					normalProductDatas.removeAll(commonNormalProducts);
					model.addAttribute("normalProducts", normalProductDatas);
				}
				else
				{
					model.addAttribute("normalProducts", normalProductDatas);
				}
				model.addAttribute("heroProducts", commonNormalProducts);
				populateModel(model, searchPageData, ShowMode.Page);
				return performSearch;
			}
			catch (final Exception exp)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
						MarketplacecommerceservicesConstants.E0000));
				try
				{
					return frontEndErrorHelper.callNonBusinessError(model, exp.getMessage());
				}
				catch (final CMSItemNotFoundException e1)
				{
					LOG.error(">> Exception occured " + e1);
				}
			}

		}
		/*
		 * catch (final EtailNonBusinessExceptions e) { ExceptionUtil.etailNonBusinessExceptionHandler(e); return
		 * frontEndErrorHelper.callNonBusinessError(model, e.getErrorMessage());
		 * //frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS); //return
		 * ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage; }
		 */
		catch (final Exception exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception,
					MarketplacecommerceservicesConstants.E0000));
			try
			{
				return frontEndErrorHelper.callNonBusinessError(model, exception.getMessage());
			}
			catch (final CMSItemNotFoundException e1)
			{
				LOG.error(">> Exception occured " + e1);
			}
		}
		return getViewForPage(model);
	}

	/**
	 * @param category
	 * @return ContentPageModel
	 * @throws CMSItemNotFoundException
	 */
	private ContentPageModel getLandingPageForCategory(final CategoryModel category) throws CMSItemNotFoundException
	{

		final ContentPageModel landingPage = mplCmsPageService.getLandingPageForCategory(category);

		if (landingPage == null)
		{
			throw new CMSItemNotFoundException("Could not find a landing page for the category" + category.getName());
		}

		return landingPage;

	}

	@ResponseBody
	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/facets", method = RequestMethod.GET)
	public FacetRefinement<SearchStateData> getFacets(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		return performSearchAndGetFacets(categoryCode, searchQuery, page, showMode, sortCode);
	}

	@ResponseBody
	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/results", method = RequestMethod.GET)
	public SearchResultsData<ProductData> getResults(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		return performSearchAndGetResultsData(categoryCode, searchQuery, page, showMode, sortCode);
	}

	/**
	 * @description method is called to create PageableData
	 * @param pageNumber
	 * @param pageSize
	 * @param sortCode
	 * @param showMode
	 * @return PageableData
	 */
	@Override
	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MarketplacecommerceservicesConstants.MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	protected UserPreferencesData updateUserPreferences(final Integer pageSize)
	{
		final UserPreferencesData preferencesData = getUserPreferences();

		if (pageSize != null)
		{
			preferencesData.setPageSize(pageSize);
		}

		return preferencesData;
	}

	protected void setUserPreferences(final UserPreferencesData userPreferencesData)
	{
		final Session session = sessionService.getCurrentSession();
		session.setAttribute("preferredCategoryPreferences", userPreferencesData);
	}

	protected UserPreferencesData getUserPreferences()
	{
		final Session session = sessionService.getCurrentSession();
		UserPreferencesData userPreferencesData = session.getAttribute("preferredCategoryPreferences");
		if (userPreferencesData == null)
		{
			userPreferencesData = new UserPreferencesData();
			setUserPreferences(userPreferencesData);
		}
		return userPreferencesData;
	}

	/**
	 *
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param pageSize
	 * @return ProductSearchPageData
	 */
	protected ProductSearchPageData<SearchStateData, ProductData> performSearch(final String categoryCode,
			final String searchQuery, final int page, final ShowMode showMode, final String sortCode, final int pageSize)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);

		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		return productSearchFacade.categorySearch(categoryCode, searchState, pageableData);
	}

	private int getfilterListCountForSize(final String searchQuery)
	{
		final Iterable<String> splitStr = Splitter.on(':').split(searchQuery);
		//		model.addAttribute("sizeCount", Integer.valueOf(Iterables.frequency(splitStr, "size")));
		//		model.addAttribute("searchQueryValue", searchQuery);
		final String[] temp = searchQuery.split(":");
		final int countFreq = Iterables.frequency(splitStr, "size");
		//  int preCount=0
		int countValue = 0;
		for (int i = 0; i < temp.length; i++)
		{
			if (temp[i].equals("size"))
			{
				countValue++;
				//countFreq = 1;
			}
			else if (countValue >= 1)
			{
				if (countValue == countFreq)
				{
					break;
				}
			}
		}
		return Iterables.frequency(splitStr, "size");
	}
}
