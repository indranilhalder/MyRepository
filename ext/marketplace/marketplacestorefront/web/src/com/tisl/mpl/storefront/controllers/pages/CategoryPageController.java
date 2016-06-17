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
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCategoryPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
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
/*
 * SEO : Changed for new url pattern acceptance
 */
//@RequestMapping(value = "/**/c")
public class CategoryPageController extends AbstractCategoryPageController
{
	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "cmsPageService")
	private MplCmsPageService mplCmsPageService;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	//Below Lines Commented as Sonar Fix
	//Start
	//	@Autowired
	//	private HeroProductDefinitionService heroService;
	//End
	//	@Resource(name = "accProductFacade")
	//	private ProductFacade productFacade;
	//	@Resource
	//	private BuyBoxFacade buyBoxFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;
	private static final String NEW_CATEGORY_URL_PATTERN = "/**/c-{categoryCode:.*}";
	private static final String NEW_CATEGORY_URL_PATTERN_PAGINATION = "/**/c-{categoryCode:.*}/page-{page}";
	private static final String CATEGORY_URL_OLD_PATTERN = "/**/c";
	//	private static final String LAST_LINK_CLASS = "active";

	private static final String PAGE = "page";

	protected static final Logger LOG = Logger.getLogger(CategoryPageController.class);
	//Added For TISPRD-1243
	private static final String DROPDOWN_BRAND = "MBH";
	private static final String DROPDOWN_CATEGORY = "MSH";

	/**
	 * @desc Main method for category landing pages SEO : Changed to accept new pattern and new pagination changes TISCR
	 *       340
	 * @param categoryCode
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param pageSize
	 * @param dropDownText
	 * @param model
	 * @param request
	 * @param response
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value =
	{ NEW_CATEGORY_URL_PATTERN, NEW_CATEGORY_URL_PATTERN_PAGINATION }, method = RequestMethod.GET)
	public String category(@PathVariable("categoryCode") String categoryCode,
			@RequestParam(value = "q", required = false) String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") int pageNo,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize,
			@RequestParam(value = "searchCategory", required = false) String dropDownText,
			@RequestParam(value = "resetAll", required = false) final boolean resetAll, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException
	{
		categoryCode = categoryCode.toUpperCase();
		String searchCode = new String(categoryCode);
		//SEO: New pagination detection TISCR 340
		pageNo = getPaginatedPageNo(request);
		//applying search filters
		if (searchQuery != null)
		{
			getfilterListCountForSize(searchQuery);
			model.addAttribute("sizeCount", Integer.valueOf(getfilterListCountForSize(searchQuery)));
			model.addAttribute("searchQueryValue", searchQuery);
		}
		//TISPRD-2315(checking whether the link has been clicked for pagination)
		if (checkIfPagination(request) && searchQuery == null)
		{
			searchQuery = ":relevance";
		}

		//Storing the user preferred search results count
		updateUserPreferences(pageSize);

		//final List<ProductModel> heroProducts = new ArrayList<ProductModel>();
		if (StringUtils.isNotEmpty(searchCode) && !(searchCode.substring(0, 5).equals(categoryCode))
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

			//Commented out for TISPT-225
			/*
			 * final SolrHeroProductDefinitionModel solrModel =
			 * heroService.getSolrHeroProductDefinitionForCategory(category); if (null != solrModel) { heroProducts =
			 * solrModel.getProducts(); }
			 */
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
					//return redirection;
					response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					response.setHeader("Location", redirection);
					return null;
				}

				final ContentPageModel categoryLandingPage = getLandingPageForCategory(category);

				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
						categoryCode, searchQuery, pageNo, showMode, sortCode, count, resetAll);

				final List<ProductData> normalProductDatas = searchPageData.getResults();
				//Set department hierarchy
				//if (normalProductDatas.size() > 0)
				if (CollectionUtils.isNotEmpty(normalProductDatas))
				{
					model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
					model.addAttribute("departments", searchPageData.getDepartments());
				}
				//set url for 1st page
				//				if (checkIfPagination(request) && searchQuery.quals && page == 0 && null != searchPageData.getCurrentQuery())
				//				{
				//					searchPageData.getCurrentQuery().setUrl(
				//							searchPageData.getCurrentQuery().getUrl()
				//									.substring(0, searchPageData.getCurrentQuery().getUrl().indexOf("/page")));
				//				}

				final String categoryName = category.getName();


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

				/*
				 * final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData =
				 * (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch( categoryCode,
				 * searchQuery, page, showMode, sortCode, count, resetAll);
				 */
				final String performSearch = performSearchAndGetResultsPage(categoryCode, searchQuery, pageNo, showMode, sortCode,
						model, request, response);
				//Commented out for TISPT-225
				/*
				 * final List<ProductData> commonNormalProducts = new ArrayList<ProductData>(); final List<ProductData>
				 * normalProductDatas = searchPageData.getResults();
				 *
				 * if (null != normalProductDatas) { for (final ProductData normalProduct : normalProductDatas) { for (final
				 * ProductModel heroProduct : heroProducts) { if
				 * (normalProduct.getCode().equalsIgnoreCase(heroProduct.getCode())) {
				 * commonNormalProducts.add(normalProduct); } } } } if (!commonNormalProducts.isEmpty()) {
				 * normalProductDatas.removeAll(commonNormalProducts); model.addAttribute("normalProducts",
				 * normalProductDatas); } else { model.addAttribute("normalProducts", normalProductDatas); }
				 * model.addAttribute("heroProducts", commonNormalProducts); populateModel(model, searchPageData,
				 * ShowMode.Page); final List<Breadcrumb> breadcrumbs =
				 * getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, searchPageData);
				 * populateTealiumData(breadcrumbs, model);
				 */
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
	 * @param breadcrumbs
	 * @param model
	 */
	private void populateTealiumData(final List<Breadcrumb> breadcrumbs, final Model model)
	{
		String breadcrumbName = "";
		int count = 1;
		if (CollectionUtils.isNotEmpty(breadcrumbs))
		{
			for (final Breadcrumb breadcrumb : breadcrumbs)
			{
				breadcrumbName += breadcrumb.getName();
				if (count < breadcrumbs.size())
				{
					breadcrumbName += ":";

				}
				count++;
			}

			model.addAttribute("site_section", breadcrumbs.get(0).getName() != null ? breadcrumbs.get(0).getName() : "");

		}

		model.addAttribute("page_name", "Product Grid:" + breadcrumbName);


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

	/**
	 * @description SEO: SEO: changed to match the old pattern. In case of dual pattern match include
	 *              NEW_CATEGORY_URL_PATTERN in @RequestMapping
	 * @param categoryCode
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @return FacetRefinement<SearchStateData>
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	@RequestMapping(value = CATEGORY_URL_OLD_PATTERN + CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/facets", method = RequestMethod.GET)
	public FacetRefinement<SearchStateData> getFacets(@PathVariable("categoryCode") String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") final int pageNum,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		categoryCode = categoryCode.toUpperCase();
		return performSearchAndGetFacets(categoryCode, searchQuery, pageNum, showMode, sortCode);
	}

	/**
	 * @description SEO: changed to match the old pattern. In case of dual pattern match include NEW_CATEGORY_URL_PATTERN
	 *              in @RequestMapping
	 * @param categoryCode
	 * @param searchQuery
	 *
	 * @param showMode
	 * @param sortCode
	 * @return SearchResultsData<ProductData>
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	@RequestMapping(value = CATEGORY_URL_OLD_PATTERN + CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/results", method = RequestMethod.GET)
	public SearchResultsData<ProductData> getResults(@PathVariable("categoryCode") String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") final int pgNum,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		categoryCode = categoryCode.toUpperCase();
		return performSearchAndGetResultsData(categoryCode, searchQuery, pgNum, showMode, sortCode);
	}

	/**
	 * @description method is called to create PageableData.
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
			final String searchQuery, final int pgNo, final ShowMode showMode, final String sortCode, final int pageSize,
			final boolean resetAll)
	{
		final PageableData pageableData = createPageableData(pgNo, pageSize, sortCode, showMode);

		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = productSearchFacade
				.categorySearch(categoryCode, searchState, pageableData);
		searchPageData = updatePageData(searchPageData, categoryCode, searchQuery);
		return searchPageData;
	}

	private int getfilterListCountForSize(final String searchQuery)
	{
		final Iterable<String> splitStr = Splitter.on(':').split(searchQuery);
		return Iterables.frequency(splitStr, "size");
	}

	/**
	 * @desc
	 */
	@Override
	protected String checkRequestUrl(final HttpServletRequest request, final HttpServletResponse response, String resolvedUrlPath)
			throws UnsupportedEncodingException
	{
		String newUrl = null;
		final String uri = request.getRequestURI();
		if (uri.contains(PAGE))
		{
			final Pattern p = Pattern.compile("page-[0-9]+");
			final Matcher m = p.matcher(uri);
			if (m.find())
			{
				if (!StringUtils.isEmpty(m.group()))
				{
					resolvedUrlPath = resolvedUrlPath + "/" + m.group();
				}
			}
		}
		//return super.checkRequestUrl(request, response, resolvedUrlPath);
		try
		{
			final String resolvedUrl = response.encodeURL(request.getContextPath() + resolvedUrlPath);
			final String requestURI = URIUtil.decode(request.getRequestURI(), "utf-8");
			final String decoded = URIUtil.decode(resolvedUrl, "utf-8");
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
					//return "redirect:" + resolvedUrlPath + "?" + queryString;
				}
				else
				{
					//return "redirect:" + resolvedUrlPath;
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


	private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> updatePageData(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData,
			final String whichSearch, final String searchQuery)
	{
		// YTODO Auto-generated method stub
		if (null != whichSearch)
		{
			final List<BreadcrumbData> removeBredCrumb = new ArrayList<BreadcrumbData>();
			for (final BreadcrumbData updateBreadCrumb : searchPageData.getBreadcrumbs())
			{
				if (updateBreadCrumb.getFacetValueCode().equalsIgnoreCase(whichSearch))
				{
					removeBredCrumb.add(updateBreadCrumb);
				}
			}
			for (final BreadcrumbData remove : removeBredCrumb)
			{
				searchPageData.getBreadcrumbs().remove(remove);
			}


		}

		return searchPageData;
	}

	/**
	 * @Desc SEO pagaination URI capture and page no determination TISCR 340
	 * @param request
	 * @return int
	 */
	private int getPaginatedPageNo(final HttpServletRequest request)
	{
		int pages = 0;
		final String uri = request.getRequestURI();
		if (uri.contains(PAGE))
		{
			final Pattern p = Pattern.compile("page-[0-9]+");
			final Matcher m = p.matcher(uri);
			if (m.find())
			{
				final String pageNoVal = m.group().split("-")[1];
				if (null != pageNoVal)
				{
					pages = Integer.parseInt(pageNoVal);
					pages = pages - 1;
				}
			}
		}
		return pages;
	}




	@Override
	protected String performSearchAndGetResultsPage(final String categoryCode, final String searchQuery, final int pgNumbers,
			final ShowMode showMode, final String sortCode, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws UnsupportedEncodingException
	{
		final CategoryModel category = getCommerceCategoryService().getCategoryForCode(categoryCode);

		final String redirection = checkRequestUrl(request, response, getCategoryModelUrlResolver().resolve(category));
		if (StringUtils.isNotEmpty(redirection))
		{
			return redirection;
		}

		final CategoryPageModel categoryPage = getCategoryPage(category);

		final CategorySearchEvaluator categorySearch = new CategorySearchEvaluator(categoryCode, XSSFilterUtil.filter(searchQuery),
				pgNumbers, showMode, sortCode, categoryPage);
		categorySearch.doSearch();

		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = categorySearch
				.getSearchPageData();

		if (searchPageData != null)
		{
			model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
			model.addAttribute("departments", searchPageData.getDepartments());
		}
		final boolean showCategoriesOnly = categorySearch.isShowCategoriesOnly();

		storeCmsPageInModel(model, categorySearch.getCategoryPage());
		storeContinueUrl(request);

		populateModel(model, searchPageData, ShowMode.Page);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, searchPageData));
		model.addAttribute("showCategoriesOnly", Boolean.valueOf(showCategoriesOnly));
		model.addAttribute("categoryName", category.getName());
		//model.addAttribute("pageType", PageType.Category);
		model.addAttribute("pageType", PageType.CATEGORY.name());
		model.addAttribute("userLocation", getCustomerLocationService().getUserLocation());
		model.addAttribute("otherProducts", true);
		updatePageTitle(category, searchPageData.getBreadcrumbs(), model);
		if (CollectionUtils.isNotEmpty(searchPageData.getResults()))
		{
			model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
		}

		final RequestContextData requestContextData = getRequestContextData(request);
		requestContextData.setCategory(category);
		requestContextData.setSearch(searchPageData);

		if (searchQuery != null)
		{
			model.addAttribute("metaRobots", "noindex,follow");
		}

		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(category.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		final List<Breadcrumb> breadcrumbs = getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, searchPageData);
		populateTealiumData(breadcrumbs, model);
		return getViewPage(categorySearch.getCategoryPage());

	}

	/**
	 * check if the request contains paging information
	 *
	 * @param request
	 * @return pagination
	 */
	private boolean checkIfPagination(final HttpServletRequest request)
	{
		final String uri = request.getRequestURI();
		boolean pagination = false;
		if (uri.contains(PAGE))
		{
			pagination = true;

		}
		return pagination;
	}

}
