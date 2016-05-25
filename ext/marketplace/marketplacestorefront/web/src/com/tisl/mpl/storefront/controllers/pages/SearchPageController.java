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
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteResultData;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
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
import com.granule.json.JSONException;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.helpmeshop.HelpMeShopFacade;
import com.tisl.mpl.model.cms.components.NeedHelpComponentModel;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.breadcrumb.impl.MplSearchBreadcrumbBuilder;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.data.MplAutocompleteResultData;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.MplCompetingProductsUtility;


@Controller
@Scope("tenant")
@RequestMapping("/search")
@SuppressWarnings(
{ "PMD" })
public class SearchPageController extends AbstractSearchPageController
{
	/**
	 *
	 */
	private static final String ALL = "all";

	/**
	 *
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SearchPageController.class);

	private static final String COMPONENT_UID_PATH_VARIABLE_PATTERN = "{componentUid:.*}";

	private static final String SEARCH_CMS_PAGE_ID = "search";
	private static final String NO_RESULTS_CMS_PAGE_ID = "searchEmpty";

	private static final String DROPDOWN_BRAND = "MBH";
	private static final String DROPDOWN_CATEGORY = "MSH";
	private static final String DROPDOWN_MICROSITE_BRAND = "brand-";
	private static final String DROPDOWN_MICROSITE_CATEGORY = "category-";
	//private static final String DROPDOWN_SELLER = "seller-"; Avoid unused private fields such as 'DROPDOWN_SELLER'.


	private static final String NEW_EXCLUSIVE_BREADCRUMB = "Discover New & Exclusive";
	private static final String LAST_LINK_CLASS = "active";
	public static final String REDIRECT_PREFIX = "redirect:";
	private static final String BLANKSTRING = "";


	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;

	@Autowired
	private CatalogVersionService catalogVersionService;
	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "searchBreadcrumbBuilder")
	private MplSearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "customerLocationService")
	private CustomerLocationService customerLocationService;

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	@Resource(name = "mplCompetingProductsUtility")
	private MplCompetingProductsUtility mplCompetingProductsUtility;

	@Resource(name = "categoryService")
	private CategoryService categoryService;



	//@Resource(name = "cmsSiteService") Avoid unused private fields
	//private CMSSiteService cmsSiteService;

	//@Resource(name = "configurationService")
	//private ConfigurationService configurationService;

	//	@Resource(name = "mplCategoryServiceImpl") Avoid unused private fields
	//	private MplCategoryService mplCategoryService;

	//	@Resource(name = "enumerationService")
	//	private EnumerationService enumerationService;


	//	@Resource(name = "defaultCategoryService")
	//	private DefaultCategoryService defaultCategoryService;


	@Resource(name = "defaultProductService")
	private DefaultProductService defaultProductService;

	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	//	@Resource(name = "categoryModelUrlResolver")
	//	private UrlResolver<CategoryModel> categoryModelUrlResolver;

	@Resource(name = "helpMeShopFacade")
	private HelpMeShopFacade helpMeShopFacade;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;


	/**
	 *
	 * @param searchText
	 * @param dropDownText
	 * @param request
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, params = "!q")
	public String textSearch(@RequestParam(value = "text", defaultValue = "") final String searchText,
			@RequestParam(value = ModelAttributetConstants.SEARCH_CATEGORY, required = false) final String dropDownValue,
			@RequestParam(value = "micrositeSearchCategory", required = false) final String micrositedropDownValue,
			@RequestParam(value = "mSellerID", required = false) final String mSellerID, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
		//---------------start--------------
		String whichSearch = null;
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		final String dropDownText = "";
		boolean allSearchFlag = false;
		String searchCategory = ALL;
		String micrositeDropDownText = "";
		final PageableData pageableData = createPageableData(0, getSearchPageSize(), null, ShowMode.Page);

		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		if (dropDownValue != null)
		{
			searchCategory = dropDownValue;
		}
		searchQueryData.setValue(XSSFilterUtil.filter(searchText));
		searchState.setQuery(searchQueryData);
		try
		{


			if (micrositedropDownValue != null)
			{

				if (micrositedropDownValue.equalsIgnoreCase(MarketplaceCoreConstants.ALL_CATEGORY))
				{

					//	searchPageData = searchFacade.dropDownSearchForSeller(searchState, mSellerID, "ALL", pageableData);
					searchPageData = searchFacade.dropDownSearchForMicrosite(searchState, mSellerID, "ALL", pageableData);

				}
				else
				{

					if (micrositedropDownValue.startsWith(DROPDOWN_MICROSITE_CATEGORY))
					{
						micrositeDropDownText = micrositedropDownValue.replaceFirst(DROPDOWN_MICROSITE_CATEGORY, "");

					}
					else if (micrositedropDownValue.startsWith(DROPDOWN_MICROSITE_BRAND))
					{

						micrositeDropDownText = micrositedropDownValue.replaceFirst(DROPDOWN_MICROSITE_BRAND, "");


					}

					searchPageData = searchFacade.sellerCategorySearch(micrositeDropDownText, mSellerID, searchState, pageableData);


				}


			}

			else
			{
				if (dropDownValue != null)
				{

					if (dropDownValue.startsWith(MarketplaceCoreConstants.ALL_CATEGORY))
					{
						allSearchFlag = true;
					}

					if (!allSearchFlag)
					{
						if (dropDownValue.startsWith(DROPDOWN_CATEGORY) || dropDownValue.startsWith(DROPDOWN_BRAND))
						{
							searchPageData = searchFacade.searchCategorySearch(dropDownValue, searchState, pageableData);
							whichSearch = dropDownValue;
						}
						else
						{
							searchPageData = searchFacade.dropDownSearch(searchState, dropDownValue, MarketplaceCoreConstants.SELLER_ID,
									pageableData);
							whichSearch = dropDownValue;
						}

						if (searchPageData != null && searchPageData.getPagination().getTotalNumberOfResults() == 0)
						{
							allSearchFlag = true;
						}
					}

					//Check whether we get results for category/brand/seller
					if (allSearchFlag)
					{
						final SearchStateData searchStateAll = new SearchStateData();
						final SearchQueryData searchQueryDataAll = new SearchQueryData();
						searchQueryDataAll.setValue(XSSFilterUtil.filter(searchText));
						searchStateAll.setQuery(searchQueryDataAll);
						searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) searchFacade
								.textSearch(searchStateAll, pageableData);
						searchCategory = ALL;
					}

				}


			}

			searchPageData = updatePageData(searchPageData, whichSearch, searchText);

			if (searchPageData == null)
			{
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
			}
			else if (searchPageData.getKeywordRedirectUrl() != null)
			{
				// if the search engine returns a redirect, just
				return "redirect:" + searchPageData.getKeywordRedirectUrl();
			}
			else if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
			{
				model.addAttribute("searchPageData", searchPageData);
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
				updatePageTitle(searchText, model);
			}
			else
			{
				storeContinueUrl(request);
				populateModel(model, searchPageData, ShowMode.Page);
				storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
				updatePageTitle(searchText, model);
			}
			model.addAttribute(MarketplaceCoreConstants.USER_LOCATION, customerLocationService.getUserLocation());
			getRequestContextData(request).setSearch(searchPageData);
			if (searchPageData != null)
			{
				if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
				{
					final NeedHelpComponentModel need = cmsComponentService.getSimpleCMSComponent("NeedHelp");
					model.addAttribute("contactNumber", need.getContactNumber());
					model.addAttribute(WebConstants.BREADCRUMBS_KEY,
							searchBreadcrumbBuilder.getEmptySearchResultBreadcrumbs(searchText));
				}
				else
				{
					model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(null, searchText,
							CollectionUtils.isEmpty(searchPageData.getBreadcrumbs())));
				}
			}

			model.addAttribute("pageType", PageType.PRODUCTSEARCH.name());
			model.addAttribute("metaRobots", "noindex,follow");



			final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getMessageSource()
					.getMessage(ModelAttributetConstants.SEARCH_META_DESC, null, ModelAttributetConstants.SEARCH_META_DESC,


			getI18nService().getCurrentLocale()) + " " + searchText + " "
					+ getMessageSource().getMessage(ModelAttributetConstants.SEARCH_META_DESC_ON, null,
							ModelAttributetConstants.SEARCH_META_DESC_ON, getI18nService().getCurrentLocale())
					+ " " + getSiteName());

			final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
			setUpMetaData(model, metaKeywords, metaDescription);

			model.addAttribute("dropDownText", dropDownText);
			model.addAttribute(ModelAttributetConstants.SEARCH_CATEGORY, searchCategory);
			model.addAttribute("autoselectSearchDropDown", Boolean.FALSE);
			model.addAttribute("isConceirge", "false");
			if (searchPageData != null)
			{
				model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
				model.addAttribute("departments", searchPageData.getDepartments());
				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> competingProductsSearchPageData = mplCompetingProductsUtility
						.getCompetingProducts(searchPageData);

				if (competingProductsSearchPageData != null)
				{
					model.addAttribute("competingProductsSearchPageData", competingProductsSearchPageData);
				}
			}
			model.addAttribute("searchCode", searchCategory);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, e.getErrorMessage());
			//frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			//return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final Exception exp)
		{


			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exp, MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, exp.getMessage());
			//frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		return getViewForPage(model);
	}

	/**
	 * @param searchPageData
	 * @return
	 */
	private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> updatePageData(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData, final String whichSearch,
			final String searchQuery)
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
	 *
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param searchText
	 * @param request
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, params = "q", value =
	{ "/page-{page}", "" })
	public String refineSearch(@RequestParam("q") final String searchQuery,
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = "0") int page,
			@RequestParam(value = "show", defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "text", required = false) final String searchText,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException, JSONException, ParseException
	{


		final String uri = request.getRequestURI();
		if (uri.contains("page"))
		{
			final Pattern p = Pattern.compile("page-[0-9]+");
			final Matcher m = p.matcher(uri);
			if (m.find())
			{
				final String pageNo = m.group().split("-")[1];
				if (null != pageNo)
				{
					page = Integer.parseInt(pageNo);
					page = page - 1;
				}
			}
		}

		final Iterable<String> splitStr = Splitter.on(':').split(searchQuery);
		model.addAttribute("sizeCount", Integer.valueOf(Iterables.frequency(splitStr, "size")));
		model.addAttribute("searchQueryValue", searchQuery);
		int count = getSearchPageSize();
		final UserPreferencesData preferencesData = updateUserPreferences(pageSize);
		if (preferencesData != null && preferencesData.getPageSize() != null)
		{
			count = preferencesData.getPageSize().intValue();
		}

		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
				searchQuery, page, showMode, sortCode, count);
		searchPageData = updatePageData(searchPageData, null, searchQuery);
		/* Storing the user preferred search results count - END */
		final String searchCategory = request.getParameter(ModelAttributetConstants.SEARCH_CATEGORY);
		String searchCode = searchCategory;
		if (searchCategory != null && searchCategory.startsWith(DROPDOWN_CATEGORY))
		{
			if (searchCategory.substring(0, 5).equals(searchCategory))
			{
				searchCode = searchCategory;
			}
			else
			{
				searchCode = searchCategory.substring(0, 5);
			}
		}

		model.addAttribute("searchCode", searchCode);
		model.addAttribute(ModelAttributetConstants.SEARCH_CATEGORY, searchCategory);
		model.addAttribute("isConceirge", "false");
		if (searchPageData != null)
		{
			model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
			model.addAttribute("departments", searchPageData.getDepartments());
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> competingProductsSearchPageData = mplCompetingProductsUtility
					.getCompetingProducts(searchPageData);

			if (competingProductsSearchPageData != null)
			{
				model.addAttribute("competingProductsSearchPageData", competingProductsSearchPageData);
			}

		}

		populateModel(model, searchPageData, showMode);
		model.addAttribute(MarketplaceCoreConstants.USER_LOCATION, customerLocationService.getUserLocation());

		if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
		{
			updatePageTitle(searchPageData.getFreeTextSearch(), model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
		}
		else
		{
			storeContinueUrl(request);
			updatePageTitle(searchPageData.getFreeTextSearch(), model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
		}
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(null, searchPageData));
		model.addAttribute("pageType", PageType.PRODUCTSEARCH.name());



		final String metaDescription = MetaSanitizerUtil
				.sanitizeDescription(getMessageSource().getMessage(ModelAttributetConstants.SEARCH_META_DESC, null,
						ModelAttributetConstants.SEARCH_META_DESC, getI18nService().getCurrentLocale()) + " " + searchText + " "
				+ getMessageSource().getMessage(ModelAttributetConstants.SEARCH_META_DESC_ON, null,
						ModelAttributetConstants.SEARCH_META_DESC_ON, getI18nService().getCurrentLocale()) + " " + getSiteName());

		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
		setUpMetaData(model, metaKeywords, metaDescription);

		return getViewForPage(model);
	}

	//	@RequestMapping(method = RequestMethod.GET, params = "q")
	//	public String refineSearch(@RequestParam("q") final String searchQuery,
	//			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = "0") final int page,
	//			@RequestParam(value = "show", defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
	//			@RequestParam(value = "sort", required = false) final String sortCode,
	//			@RequestParam(value = "text", required = false) final String searchText,
	//			@RequestParam(value = "pageSize", required = false) final Integer pageSize, final HttpServletRequest request,
	//			final Model model) throws CMSItemNotFoundException
	//	{
	//
	//		/* Storing the user preferred search results count - START */
	//		final UserPreferencesData preferencesData = updateUserPreferences(pageSize);
	//
	//		int count = getSearchPageSize();
	//		if (preferencesData != null && preferencesData.getPageSize() != null)
	//		{
	//			count = preferencesData.getPageSize().intValue();
	//		}
	//
	//		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
	//				searchQuery, page, showMode, sortCode, count);
	//		/* Storing the user preferred search results count - END */
	//
	//		final String searchCategory = request.getParameter(ModelAttributetConstants.SEARCH_CATEGORY);
	//		String searchCode = searchCategory;
	//		if (searchCategory != null && searchCategory.startsWith(DROPDOWN_CATEGORY))
	//		{
	//			if (searchCategory.substring(0, 5).equals(searchCategory))
	//			{
	//				searchCode = searchCategory;
	//			}
	//			else
	//			{
	//				searchCode = searchCategory.substring(0, 5);
	//			}
	//		}
	//
	//		model.addAttribute("searchCode", searchCode);
	//		model.addAttribute(ModelAttributetConstants.SEARCH_CATEGORY, searchCategory);
	//		model.addAttribute("isConceirge", "false");
	//		if (searchPageData != null)
	//		{
	//			model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
	//			model.addAttribute("departments", searchPageData.getDepartments());
	//			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> competingProductsSearchPageData = mplCompetingProductsUtility
	//					.getCompetingProducts(searchPageData);
	//
	//			if (competingProductsSearchPageData != null)
	//			{
	//				model.addAttribute("competingProductsSearchPageData", competingProductsSearchPageData);
	//			}
	//
	//		}
	//
	//		populateModel(model, searchPageData, showMode);
	//		model.addAttribute(MarketplaceCoreConstants.USER_LOCATION, customerLocationService.getUserLocation());
	//
	//		if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
	//		{
	//			updatePageTitle(searchPageData.getFreeTextSearch(), model);
	//			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
	//		}
	//		else
	//		{
	//			storeContinueUrl(request);
	//			updatePageTitle(searchPageData.getFreeTextSearch(), model);
	//			storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
	//		}
	//		model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(null, searchPageData));
	//		model.addAttribute("pageType", PageType.PRODUCTSEARCH.name());
	//
	//		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getMessageSource().getMessage(
	//				ModelAttributetConstants.SEARCH_META_DESC, null, ModelAttributetConstants.SEARCH_META_DESC,
	//				getI18nService().getCurrentLocale())
	//				+ " "
	//				+ searchText
	//				+ " "
	//				+ getMessageSource().getMessage(ModelAttributetConstants.SEARCH_META_DESC_ON, null,
	//						ModelAttributetConstants.SEARCH_META_DESC_ON, getI18nService().getCurrentLocale()) + " " + getSiteName());
	//
	//		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
	//		setUpMetaData(model, metaKeywords, metaDescription);
	//
	//		return getViewForPage(model);
	//	}

	/**
	 *
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param pageSize
	 * @return ProductSearchPageData
	 */
	protected ProductSearchPageData<SearchStateData, ProductData> performSearch(final String searchQuery, final int page,
			final ShowMode showMode, final String sortCode, final int pageSize)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);

		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		String sellerId = null;
		if (searchQuery != null && searchQuery.indexOf("sellerId:") > 1)
		{
			sellerId = searchQuery.substring(searchQuery.indexOf("sellerId:") + 9, searchQuery.indexOf("sellerId:") + 15);
		}
		return updatePageData((ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
				.textSearch(searchState, pageableData), sellerId, searchQuery);
	}

	/**
	 *
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @return SearchResultsData
	 * @throws CMSItemNotFoundException
	 */
	@ResponseBody
	@RequestMapping(value = "/results", method = RequestMethod.GET)
	public SearchResultsData<ProductData> jsonSearchResults(@RequestParam("q") final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws CMSItemNotFoundException
	{
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = performSearch(searchQuery, page, showMode,
				sortCode, getSearchPageSize());
		final SearchResultsData<ProductData> searchResultsData = new SearchResultsData<>();
		searchResultsData.setResults(searchPageData.getResults());
		searchResultsData.setPagination(searchPageData.getPagination());
		return searchResultsData;
	}


	/**
	 * DISPLAY ONLINE and NEW TRENDING PRODUCTS UPLOADED BY BUSINESS AS PROMOTED PRODUCTS
	 *
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @throws CMSItemNotFoundException
	 */


	@RequestMapping(value = "/viewOnlineProducts", method = RequestMethod.GET)
	public String displayNewAndExclusiveProducts(@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0", required = false) final int page,
			@RequestParam(value = "show", defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = "sort", required = false) String sortCode, final HttpServletRequest request, final Model model)
					throws CMSItemNotFoundException
	{

		try
		{


			if (StringUtils.isEmpty(sortCode))

			{
				sortCode = "promotedpriority-asc";
			}


			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = performSearchForOnlineProducts(
					searchQuery, page, showMode, sortCode, getSearchPageSize());

			storeContinueUrl(request);

			populateModel(model, searchPageData, ShowMode.Page);
			getRequestContextData(request).setSearch(searchPageData);
			model.addAttribute(MarketplaceCoreConstants.USER_LOCATION, customerLocationService.getUserLocation());
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					Collections.singletonList(new Breadcrumb("#", NEW_EXCLUSIVE_BREADCRUMB, LAST_LINK_CLASS)));
			model.addAttribute("pageType", PageType.PRODUCT.name());
			model.addAttribute("hideDepartments", Boolean.TRUE);
			//Code to hide the applied facet for promotedProduct
			if (searchPageData.getBreadcrumbs() != null && searchPageData.getBreadcrumbs().size() == 1)
			{
				final String facetCode = searchPageData.getBreadcrumbs().get(0).getFacetCode();
				if (StringUtils.isNotEmpty(facetCode) && facetCode.equalsIgnoreCase("promotedProduct"))
				{
					searchPageData.setBreadcrumbs(null);
				}
			}

			if (!searchPageData.getResults().isEmpty())
			{
				model.addAttribute("normalProducts", searchPageData.getResults());

			}

			storeCmsPageInModel(model, getCmsPageService().getDefaultCategoryPage());
			if (getCmsSiteService().getCurrentSite() != null
					&& StringUtils.isNotEmpty(getCmsSiteService().getCurrentSite().getName()))
			{
				storeContentPageTitleInModel(model, getCmsSiteService().getCurrentSite().getName());
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{

			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, e.getErrorMessage());

		}

		catch (final Exception exp)
		{
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exp, MarketplacecommerceservicesConstants.E0000));



			return frontEndErrorHelper.callNonBusinessError(model, exp.getMessage());

		}


		return getViewForPage(model);
	}


	/**
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param searchPageSize
	 */
	private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> performSearchForOnlineProducts(
			final String searchQuery, final int page, final ShowMode showMode, final String sortCode, final int searchPageSize)
	{

		final PageableData pageableData = createPageableData(page, page, sortCode, ShowMode.Page);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();

		if (StringUtils.isNotEmpty(searchQuery))
		{
			searchQueryData.setValue(searchQuery);
		}

		searchState.setQuery(searchQueryData);


		return searchFacade.mplOnlineAndNewProductSearch(searchState, pageableData);

	}

	/**
	 *
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @return FacetRefinement
	 * @throws CMSItemNotFoundException
	 */
	@ResponseBody
	@RequestMapping(value = "/facets", method = RequestMethod.GET)
	public FacetRefinement<SearchStateData> getFacets(@RequestParam("q") final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws CMSItemNotFoundException
	{
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);

		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = productSearchFacade.textSearch(searchState,
				createPageableData(page, getSearchPageSize(), sortCode, showMode));
		final List<FacetData<SearchStateData>> facets = refineFacets(searchPageData.getFacets(),
				convertBreadcrumbsToFacets(searchPageData.getBreadcrumbs()));
		final FacetRefinement<SearchStateData> refinement = new FacetRefinement<>();
		refinement.setFacets(facets);
		refinement.setCount(searchPageData.getPagination().getTotalNumberOfResults());
		refinement.setBreadcrumbs(searchPageData.getBreadcrumbs());
		return refinement;
	}

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
	public AutocompleteResultData getAutocompleteSuggestions(@PathVariable final String componentUid,
			@RequestParam("term") final String term, @RequestParam("category") final String category) throws CMSItemNotFoundException
	{
		final MplAutocompleteResultData resultData = new MplAutocompleteResultData();

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
					resultData
							.setSearchTerm(resultData.getSuggestions().size() > 0 ? resultData.getSuggestions().get(0).getTerm() : term);
				}


			}
		}
		catch (final EtailNonBusinessExceptions eb)
		{
			LOG.debug("Error occured in getAutocompleteSuggestions :" + eb.getMessage());
		}

		return resultData;
	}

	/**
	 *
	 * @param age
	 * @param genderOrTitle
	 * @param typeOfProduct
	 * @param reasonOrEvent
	 * @param request
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/helpmeshop", method = RequestMethod.POST)
	public String getHelpMeShop(@RequestParam(value = "age", defaultValue = "0") final String age,
			@RequestParam(value = "genderOrTitle", defaultValue = ModelAttributetConstants.PAGE_VAL) final String genderOrTitle,
			@RequestParam(value = "typeOfProduct", required = false) final String typeOfProduct,
			@RequestParam(value = "reasonOrEvent", required = false) final String reasonOrEvent, final HttpServletRequest request,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize, final Model model)
					throws CMSItemNotFoundException
	{
		if (StringUtils.isNotBlank(typeOfProduct))
		{
			final PageableData pageableData = createPageableData(0, getSearchPageSize(), null, ShowMode.Page);

			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			searchQueryData.setValue(typeOfProduct);
			searchState.setQuery(searchQueryData);
			//final CategoryModel matchedCategory = mplCategoryService.getMatchingCategory(genderOrTitle);
			//	ProductSearchPageData<SearchStateData, ProductData> searchPageData = null;
			ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
			if (genderOrTitle != null)
			{
				//final String category = matchedCategory.getCode();
				searchPageData = searchFacade.conceirgeSearch(age, genderOrTitle, reasonOrEvent, searchState, pageableData);
				model.addAttribute("isConceirge", "true");
				model.addAttribute(ModelAttributetConstants.SEARCH_CATEGORY, genderOrTitle);
			}

			if (searchPageData == null)
			{
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
			}
			else if (searchPageData.getKeywordRedirectUrl() != null)
			{
				// if the search engine returns a redirect, just
				return "redirect:" + searchPageData.getKeywordRedirectUrl();
			}
			else if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
			{
				model.addAttribute("searchPageData", searchPageData);
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
				updatePageTitle(typeOfProduct, model);
			}
			else
			{
				storeContinueUrl(request);
				populateModel(model, searchPageData, ShowMode.Page);
				storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
				if (CollectionUtils.isNotEmpty(searchPageData.getResults()))
				{
					model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
				}
				updatePageTitle(typeOfProduct, model);
			}
			model.addAttribute(MarketplaceCoreConstants.USER_LOCATION, customerLocationService.getUserLocation());
			getRequestContextData(request).setSearch(searchPageData);
			if (searchPageData != null)
			{
				model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(null, typeOfProduct,
						CollectionUtils.isEmpty(searchPageData.getBreadcrumbs())));
			}
		}
		else
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
		}
		model.addAttribute("pageType", PageType.PRODUCTSEARCH.name());
		model.addAttribute("metaRobots", "noindex,follow");


		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(
				getMessageSource().getMessage(ModelAttributetConstants.SEARCH_META_DESC, null, getI18nService().getCurrentLocale())
						+ " " + typeOfProduct + " " + getMessageSource().getMessage(ModelAttributetConstants.SEARCH_META_DESC_ON, null,

		getI18nService().getCurrentLocale()) + " " + getSiteName());
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(typeOfProduct);
		setUpMetaData(model, metaKeywords, metaDescription);

		return getViewForPage(model);



	}

	@RequestMapping(value = "/helpmeshop", method = RequestMethod.GET)
	public String getHelpMeShop(@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,

	@RequestParam(value = "pageSize", required = false) final Integer pageSize,
			@RequestParam(value = "searchCategory", required = false) final String searchCategory, final HttpServletRequest request,

	final Model model) throws CMSItemNotFoundException
	{
		if (searchQuery != null)
		{
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
					searchQuery, page, showMode, sortCode, getSearchPageSize());
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
			if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
			{
				model.addAttribute("searchPageData", searchPageData);
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
				updatePageTitle(searchPageData.getFreeTextSearch(), model);
			}
			else
			{
				storeContinueUrl(request);
				populateModel(model, searchPageData, ShowMode.Page);
				storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
				updatePageTitle(searchPageData.getFreeTextSearch(), model);
			}
			model.addAttribute("pageType", PageType.PRODUCTSEARCH.name());
			model.addAttribute("metaRobots", "noindex,follow");
			if (CollectionUtils.isNotEmpty(searchPageData.getResults()))
			{
				model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
			}
			// --------------Issue TISSIT-1827 solved ------------//
			model.addAttribute(ModelAttributetConstants.SEARCH_CATEGORY, searchCategory);
			//	final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchPageData.getFreeTextSearch());


			final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getMessageSource()
					.getMessage(ModelAttributetConstants.SEARCH_META_DESC, null, ModelAttributetConstants.SEARCH_META_DESC,

			getI18nService().getCurrentLocale()) + " " + searchPageData.getFreeTextSearch() + " "
					+ getMessageSource().getMessage(ModelAttributetConstants.SEARCH_META_DESC_ON, null,
							ModelAttributetConstants.SEARCH_META_DESC_ON, getI18nService().getCurrentLocale())
					+ " " + getSiteName());

			final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchPageData.getFreeTextSearch());
			setUpMetaData(model, metaKeywords, metaDescription);

		}
		return getViewForPage(model);
	}

	@ResponseBody
	@RequestMapping(value = "/helpmeshopforcategories", method = RequestMethod.GET)
	public List<CategoryData> getHelpMeShopValuesforCategory(@RequestParam("categoryCode") final String categoryCode,
			final Model model, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{

		final List<CategoryData> categoryDatas = helpMeShopFacade.getCategoryForConceirgeSearch(categoryCode);
		return categoryDatas;
	}


	/**
	 *
	 * @param list
	 * @param maxElements
	 * @return List
	 */

	/*
	 * protected <E> List<E> subList(final List<E> list, final int maxElements) { if (CollectionUtils.isEmpty(list)) {
	 * return Collections.emptyList(); }
	 *
	 * if (list.size() > maxElements) { return list.subList(0, maxElements); }
	 *
	 * return list; }
	 */

	/**
	 *
	 * @param searchText
	 * @param model
	 */
	protected void updatePageTitle(final String searchText, final Model model)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveContentPageTitle(
				getMessageSource().getMessage("search.meta.title", null, "search.meta.title", getI18nService().getCurrentLocale())
						+ " " + searchText));
	}

	/**
	 * @param model
	 * @param searchPageData
	 * @param showMode
	 */
	@Override
	protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		super.populateModel(model, searchPageData, showMode);
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

	/* Storing the user preferred search results count - START */

	public static class UserPreferencesData
	{
		private Integer pageSize;

		public Integer getPageSize()
		{
			return pageSize;
		}

		public void setPageSize(final Integer pageSize)
		{
			if (pageSize != null && pageSize.intValue() > MAX_PAGE_LIMIT)
			{
				// Ensure that we don't exceed the limit - for performance reasons.
				//this.pageSize = new Integer(MAX_PAGE_LIMIT); Avoid instantiating Integer objects. Call Integer.valueOf() instead.
				this.pageSize = Integer.valueOf(MAX_PAGE_LIMIT);
			}
			else
			{
				this.pageSize = pageSize;
			}
		}

	}


	protected void setUserPreferences(final UserPreferencesData userPreferencesData)
	{
		final Session session = sessionService.getCurrentSession();
		session.setAttribute("preferredSearchPreferences", userPreferencesData);
	}

	protected UserPreferencesData getUserPreferences()
	{
		final Session session = sessionService.getCurrentSession();
		UserPreferencesData userPreferencesData = session.getAttribute("preferredSearchPreferences");
		if (userPreferencesData == null)
		{
			userPreferencesData = new UserPreferencesData();
			setUserPreferences(userPreferencesData);
		}
		return userPreferencesData;
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

	private boolean findAllOutOfStock(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		if (searchPageData.getFacets() != null && searchPageData.getFacets().size() > 0)
		{
			final Collection<FacetData<SearchStateData>> facets = searchPageData.getFacets();

			for (final FacetData<SearchStateData> facet : facets)
			{
				if (facet.getCode().equalsIgnoreCase("inStockFlag"))
				{
					if (facet.getValues().size() > 1)
					{
						return false;
					}
					else if (facet.getValues().size() == 1 && facet.getValues().get(0).getCode().equalsIgnoreCase("false"))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private CategoryModel getCompetingProductsCategory(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		if (searchPageData.getResults() != null && searchPageData.getResults().size() > 0)
		{
			final ProductData productData = searchPageData.getResults().get(0);
			final ProductModel productModel = defaultProductService.getProductForCode(productData.getCode());
			final Collection<CategoryModel> superCategories = productModel.getSupercategories();

			for (final CategoryModel categoryModel : superCategories)
			{
				if (categoryModel instanceof ClassificationClassModel)
				{
					continue;
				}
				else if (categoryModel.getCode().startsWith("MSH"))
				{
					return categoryModel;
				}

			}
		}
		return null;
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
	@RequestMapping(value = "showAllCartEntries", method = RequestMethod.GET)
	public List<String> getAllCartList(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		//	final CartData cartData = null;

		final List<String> productList = new ArrayList<String>();
		//model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
		try
		{
			final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
			if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
			{
				for (final OrderEntryData entry : cartData.getEntries())
				{
					productList.add(entry.getProduct().getCode());

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
		return productList;
	}

	private SearchStateData getCompetingProductsSearchState(final String searchText)
	{
		final SearchStateData competingProductsSearchState = new SearchStateData();
		final SearchQueryData competingProductsSearchQueryData = new SearchQueryData();

		competingProductsSearchQueryData.setValue(XSSFilterUtil.filter(searchText));
		competingProductsSearchState.setQuery(competingProductsSearchQueryData);
		competingProductsSearchQueryData.setValue(searchText);
		competingProductsSearchState.setQuery(competingProductsSearchQueryData);

		return competingProductsSearchState;
	}
}
