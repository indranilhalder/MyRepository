package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.granule.json.JSONException;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.facade.shopbylook.ShopByLookFacade;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.breadcrumb.impl.MplSearchBreadcrumbBuilder;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.pages.SearchPageController.UserPreferencesData;
import com.tisl.mpl.util.MplCompetingProductsUtility;


/**
 * @author TCS
 *
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = "/collection")
public class ShopByLookController extends AbstractSearchPageController
{
	private static final Logger LOG = Logger.getLogger(ShopByLookController.class);

	@Resource(name = "shopByLookFacade")
	private ShopByLookFacade shopbyLookFacade;
	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "mplCompetingProductsUtility")
	private MplCompetingProductsUtility mplCompetingProductsUtility;

	@Resource(name = "searchBreadcrumbBuilder")
	private MplSearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "customerLocationService")
	private CustomerLocationService customerLocationService;

	private static final String BLANKSTRING = "";
	private static final String REFINE_SEARCH_URL_PATTERN = "/page-{page}";
	private static final String REFINE_FACET_SEARCH_URL_PATTERN_1 = "/page-{page}/**/getFacetData";
	private static final String REFINE_FACET_SEARCH_URL_PATTERN_2 = "/getFacetData";
	private static final String COMPILE_PATTERN = "page-[0-9]+";
	private static final String DROPDOWN_CATEGORY = "MSH";
	private static final String NO_RESULTS_CMS_PAGE_ID = "searchEmpty";
	private static final String SEARCH_CMS_PAGE_ID = "search";
	private static final String LOOK_ID = "{look-id}";

	/**
	 * @desc this method fetches the shop by collection based on look-id provided.
	 * @param lookId
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings("boxing")
	@RequestMapping(method = RequestMethod.GET, value = LOOK_ID)
	public String loadShopTheLooks(@PathVariable("look-id") final String lookId,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "page", required = false, defaultValue = "0") final Integer page, final Model model)
			throws CMSItemNotFoundException
	{
		String formedPaginationUrl = null;

		int count = getSearchPageSize();
		final UserPreferencesData preferencesData = updateUserPreferences(count);

		if (null != searchQuery || null != sortCode)
		{
			if (preferencesData != null && preferencesData.getPageSize() != null)
			{
				count = preferencesData.getPageSize().intValue();
			}
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
					lookId, searchQuery, page, showMode, sortCode, count, null);
			final String url = searchPageData.getCurrentQuery().getUrl();
			formedPaginationUrl = url.replace("/search", "");
			searchPageData.getCurrentQuery().setUrl(formedPaginationUrl);
			populateModel(model, searchPageData, showMode);
		}
		else
		{
			//ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
			//final SearchStateData searchState = new SearchStateData();
			//final SearchQueryData searchQueryData = new SearchQueryData();
			//final PageableData pageableData = createPageableData(page, getSearchPageSize(), null, ShowMode.Page);
			//searchState.setQuery(searchQueryData);
			//searchPageData = searchFacade.collectionSearch(lookId, searchState, pageableData);
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
					lookId, searchQuery, page, showMode, sortCode, count, null);
			final String url = searchPageData.getCurrentQuery().getUrl();
			formedPaginationUrl = url.replace("/search", "");
			searchPageData.getCurrentQuery().setUrl(formedPaginationUrl);
			populateModel(model, searchPageData, ShowMode.Page);
			//Checking Department Hierarchy
			model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
			model.addAttribute("departments", searchPageData.getDepartments());
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> competingProductsSearchPageData = mplCompetingProductsUtility
					.getCompetingProducts(searchPageData);

			if (competingProductsSearchPageData != null)
			{
				model.addAttribute("competingProductsSearchPageData", competingProductsSearchPageData);

			}
		}

		if (null != lookId)
		{
			final String LAST_LINK_CLASS = "active";
			final Date date = new Date();
			Date dateStart = null;
			Date endDate = null;

			final List<MplShopByLookModel> shopByLookProducts = shopbyLookFacade.getAllShopByLookProducts(lookId);
			/*
			 * check for validity of the shop the look page
			 */

			for (final MplShopByLookModel shopByLook : shopByLookProducts)
			{
				dateStart = shopByLook.getStartDate();
				endDate = shopByLook.getEndDate();
				final List<Breadcrumb> shopTheLookBreadCrub = new ArrayList<Breadcrumb>();
				final Breadcrumb crub = new Breadcrumb("#", shopByLook.getCollectionName(), LAST_LINK_CLASS);
				shopTheLookBreadCrub.add(crub);
				model.addAttribute(WebConstants.BREADCRUMBS_KEY, shopTheLookBreadCrub);

				if (null != dateStart && null != endDate && date.after(dateStart) && date.before(endDate))
				{
					//SONAR Fix
					LOG.debug("Date falls between dateStart and endDate");
				}
				else
				{
					model.addAttribute(ModelAttributetConstants.PRODUCT, null);
					model.addAttribute(ModelAttributetConstants.SHOP_THE_LOOK_PAGE_EXPIRED, "yes");
				}
			}


			model.addAttribute(ModelAttributetConstants.SHOP_BY_LOOK, "shopbylook");
		}
		/*
		 * common page model loading
		 */
		ContentPageModel shopByLookPage = null;
		try
		{
			shopByLookPage = getContentPageForLabelOrId(lookId);
		}
		catch (final CMSItemNotFoundException e)
		{
			return FORWARD_PREFIX + "/404";
			//storeCmsPageInModel(model, getCmsPageService().getDefaultCategoryPage());
		}
		if (null != shopByLookPage)
		{
			storeCmsPageInModel(model, shopByLookPage);
			setUpMetaDataForContentPage(model, shopByLookPage);
		}
		return getViewForPage(model);

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

	/**
	 *
	 * @param searchText
	 * @param model
	 */
	protected void updatePageTitle(final String searchText, final Model model)
	{
		storeContentPageTitleInModel(
				model,
				getPageTitleResolver().resolveContentPageTitle(
						getMessageSource().getMessage("search.meta.title", null, "search.meta.title",
								getI18nService().getCurrentLocale())
								+ " " + searchText));
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
	protected ProductSearchPageData<SearchStateData, ProductData> performSearch(final String lookId, final String searchQuery,
			final int page, final ShowMode showMode, final String sortCode, final int pageSize, final String pageFacets)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
		pageableData.setPageFacets(pageFacets);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		return searchFacade.collectionSearch(lookId, searchState, pageableData);
	}

	/**
	 * @return the shopbyLookFacade
	 */
	public ShopByLookFacade getShopbyLookFacade()
	{
		return shopbyLookFacade;
	}

	/**
	 * @param shopbyLookFacade
	 *           the shopbyLookFacade to set
	 */
	public void setShopbyLookFacade(final ShopByLookFacade shopbyLookFacade)
	{
		this.shopbyLookFacade = shopbyLookFacade;
	}

	/**
	 * @return the searchFacade
	 */
	public DefaultMplProductSearchFacade getSearchFacade()
	{
		return searchFacade;
	}

	/**
	 * @param searchFacade
	 *           the searchFacade to set
	 */
	public void setSearchFacade(final DefaultMplProductSearchFacade searchFacade)
	{
		this.searchFacade = searchFacade;
	}

	/**
	 * @return the sessionService
	 */
	@Override
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @desc Method for SERP for AJAX call : TPR-198
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
	{ LOOK_ID + REFINE_FACET_SEARCH_URL_PATTERN_1, LOOK_ID + REFINE_FACET_SEARCH_URL_PATTERN_2 })
	public String refineFacetSearch(@PathVariable("look-id") final String lookId, @RequestParam("q") final String searchQuery,
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "text", required = false) final String searchText,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException, JSONException, ParseException
	{

		populateRefineSearchResult(lookId, searchQuery, page, showMode, sortCode, searchText, pageSize, request, model);
		return ControllerConstants.Views.Pages.Search.FacetResultPanel;
	}

	/**
	 * @desc private method, responsible for populating the entire search page data for view
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param searchText
	 * @param pageSize
	 * @param request
	 * @param model
	 */
	private void populateRefineSearchResult(final String lookId, final String searchQuery, int page, final ShowMode showMode,
			final String sortCode, final String searchText, final Integer pageSize, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
		final String uri = request.getRequestURI();
		if (uri.contains("page"))
		{
			final Pattern p = Pattern.compile(COMPILE_PATTERN);
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
		model.addAttribute(ModelAttributetConstants.SIZE_COUNT, Integer.valueOf(Iterables.frequency(splitStr, "size")));
		model.addAttribute(ModelAttributetConstants.SEARCH_QUERY_VALUE, searchQuery);
		int count = getSearchPageSize();
		final UserPreferencesData preferencesData = updateUserPreferences(pageSize);
		if (preferencesData != null && preferencesData.getPageSize() != null)
		{
			count = preferencesData.getPageSize().intValue();
		}
		// Get page facets to include in facet field exclude tag
		final String pageFacets = request.getParameter("pageFacetData");
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
				lookId, searchQuery, page, showMode, sortCode, count, pageFacets);
		model.addAttribute("currentQuery", searchPageData.getCurrentQuery().getQuery().getValue());
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

		model.addAttribute(ModelAttributetConstants.SEARCH_CODE, searchCode);
		model.addAttribute(ModelAttributetConstants.SEARCH_CATEGORY, searchCategory);
		model.addAttribute(ModelAttributetConstants.IS_CONCEIRGE, "false");
		if (searchPageData != null)
		{
			model.addAttribute(ModelAttributetConstants.DEPARTMENT_HIERARCHY_DATA, searchPageData.getDepartmentHierarchyData());
			model.addAttribute(ModelAttributetConstants.DEPARTMENTS, searchPageData.getDepartments());
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> competingProductsSearchPageData = mplCompetingProductsUtility
					.getCompetingProducts(searchPageData);

			if (competingProductsSearchPageData != null)
			{
				model.addAttribute(ModelAttributetConstants.COMPETING_PRODUCTS_SEARCH_PAGE_DATA, competingProductsSearchPageData);
			}

		}
		model.addAttribute(ModelAttributetConstants.SHOP_BY_LOOK, "shopbylook");
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
		final List<Breadcrumb> breadcrumbs = searchBreadcrumbBuilder.getBreadcrumbs(null, searchPageData);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		//populateTealiumData(breadcrumbs, model);

		model.addAttribute(ModelAttributetConstants.PAGE_TYPE, PageType.PRODUCTSEARCH.name());

		//Changes Done for TPR-432

		if (searchPageData.getResults() != null)
		{
			searchPageData.setSpellingSuggestion(null);
		}


		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getMessageSource().getMessage(
				ModelAttributetConstants.SEARCH_META_DESC, null, ModelAttributetConstants.SEARCH_META_DESC,
				getI18nService().getCurrentLocale())
				+ " "
				+ searchText
				+ " "
				+ getMessageSource().getMessage(ModelAttributetConstants.SEARCH_META_DESC_ON, null,
						ModelAttributetConstants.SEARCH_META_DESC_ON, getI18nService().getCurrentLocale()) + " " + getSiteName());

		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);

		setUpMetaData(model, metaKeywords, metaDescription);
	}

	/**
	 * @param searchPageData
	 * @return
	 */
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
	{ LOOK_ID + REFINE_SEARCH_URL_PATTERN, LOOK_ID + BLANKSTRING })
	public String refineSearch(@PathVariable("look-id") final String lookId, @RequestParam("q") final String searchQuery,
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "text", required = false) final String searchText,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException, JSONException, ParseException
	{

		populateRefineSearchResult(lookId, searchQuery, page, showMode, sortCode, searchText, pageSize, request, model);
		return getViewForPage(model);
	}
}
