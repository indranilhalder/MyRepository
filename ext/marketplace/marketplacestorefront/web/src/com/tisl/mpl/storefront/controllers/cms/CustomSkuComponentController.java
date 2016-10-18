/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
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
import com.tisl.mpl.core.model.CustomSkuComponentModel;
import com.tisl.mpl.facade.shopbylook.ShopByLookFacade;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.breadcrumb.impl.MplSearchBreadcrumbBuilder;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.pages.SearchPageController.UserPreferencesData;
import com.tisl.mpl.util.MplCompetingProductsUtility;


/**
 * @author TCS
 *
 */
@Controller("CustomSkuComponentController")
@Scope("tenant")
@RequestMapping(value =
{ ControllerConstants.Actions.Cms.CustomSkuComponent, "CustomSkuCollection" })
public class CustomSkuComponentController extends AbstractCMSComponentController<CustomSkuComponentModel>
{

	private static final Logger LOG = Logger.getLogger(CustomSkuComponentController.class);

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

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	private static final String BLANKSTRING = "";
	private static final String REFINE_SEARCH_URL_PATTERN = "/page-{page}";
	private static final String REFINE_FACET_SEARCH_URL_PATTERN_1 = "/page-{page}/**/getFacetData";
	private static final String REFINE_FACET_SEARCH_URL_PATTERN_2 = "/getFacetData";
	private static final String COMPILE_PATTERN = "page-[0-9]+";
	private static final String DROPDOWN_CATEGORY = "MSH";
	public static final String CMS_PAGE_MODEL = "cmsPage";
	public static final String PAGE_ROOT = "pages/";
	public static final String CUSTOM_SKU_GRID_PAGE = "pages/search/customSkuGridPage";

	public static final int MAX_PAGE_LIMIT = 100; // should be configured
	private static final String PAGINATION_NUMBER_OF_RESULTS_COUNT = "pagination.number.results.count";

	public static final String LOOK_ID = "{look-id}";

	//private String lookId = "";// Blocked for SONAR

	public static enum ShowMode
	{
		Page, All
	}

	protected void customSku(final String searchQuery, final String sortCode, final ShowMode showMode, final Integer page,
			final CustomSkuComponentModel sku, final Model model)
	{
		LOG.debug("Inside Method : customSku");

		String formedPaginationUrl = null;

		if (null != searchQuery || null != sortCode)
		{
			int count = getSearchPageSize();
			final UserPreferencesData preferencesData = updateUserPreferences(count);

			if (preferencesData != null && preferencesData.getPageSize() != null)
			{
				count = preferencesData.getPageSize().intValue();
			}
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
					sku.getLabelOrId(), searchQuery, page, showMode, sortCode, count, null);
			final String url = searchPageData.getCurrentQuery().getUrl();
			formedPaginationUrl = url.replace("/search", "");
			searchPageData.getCurrentQuery().setUrl(formedPaginationUrl);
			populateModel(model, searchPageData, showMode);
		}
		else
		{
			ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			final PageableData pageableData = createPageableData(page, getSearchPageSize(), null, ShowMode.Page);
			searchState.setQuery(searchQueryData);
			searchPageData = searchFacade.collectionSearch(sku.getLabelOrId(), searchState, pageableData);
			final String url = searchPageData.getCurrentQuery().getUrl();
			formedPaginationUrl = url.replace("/search", "");
			searchPageData.getCurrentQuery().setUrl(formedPaginationUrl);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.HttpServletRequest
	 * , org.springframework.ui.Model, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@SuppressWarnings("boxing")
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final CustomSkuComponentModel component)
	{
		final String searchQuery = request.getParameter("q");
		final String sortCode = request.getParameter("sort");
		//final String showMode = request.getParameter("show");
		//if(null!= showMode && "Page".equals(showMode)){
		//	ShowMode.All
		//}
		Integer page = null;
		if (null != request.getParameter("page"))
		{
			page = Integer.parseInt(request.getParameter("page"));
		}
		else
		{
			page = Integer.valueOf(0);
		}
		//this.lookId = component.getLabelOrId(); // Blocked for SONAR
		this.customSku(searchQuery, sortCode, ShowMode.Page, page, component, model);

	}

	/**
	 * One
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param sortCode
	 * @param showMode
	 * @return
	 */
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

	/**
	 * Two
	 *
	 * @param pageSize
	 * @return
	 */
	protected UserPreferencesData updateUserPreferences(final Integer pageSize)
	{
		final UserPreferencesData preferencesData = getUserPreferences();

		if (pageSize != null)
		{
			preferencesData.setPageSize(pageSize);
		}

		return preferencesData;
	}

	/**
	 * Three
	 *
	 * @param userPreferencesData
	 */
	protected void setUserPreferences(final UserPreferencesData userPreferencesData)
	{
		final Session session = sessionService.getCurrentSession();
		session.setAttribute("preferredSearchPreferences", userPreferencesData);
	}

	/**
	 * Four
	 *
	 * @return
	 */
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
		model.addAttribute("shopbylook", "shopbylook");
		model.addAttribute("lookId", lookId);
		populateModel(model, searchPageData, showMode);
		model.addAttribute(MarketplaceCoreConstants.USER_LOCATION, customerLocationService.getUserLocation());

		if (searchPageData.getPagination().getTotalNumberOfResults() != 0)
		{
			storeContinueUrl(request); // Sonar Fix
			//updatePageTitle(searchPageData.getFreeTextSearch(), model);
			//storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
		}
		//		else
		//		{
		//
		//			//updatePageTitle(searchPageData.getFreeTextSearch(), model);
		//			//storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
		//		}
		final List<Breadcrumb> breadcrumbs = searchBreadcrumbBuilder.getBreadcrumbs(null, searchPageData);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadcrumbs);
		//populateTealiumData(breadcrumbs, model);

		model.addAttribute(ModelAttributetConstants.PAGE_TYPE, PageType.PRODUCTSEARCH.name());

		//Changes Done for TPR-432

		if (searchPageData.getResults() != null)
		{
			searchPageData.setSpellingSuggestion(null);
		}


		/*
		 * final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getMessageSource().getMessage(
		 * ModelAttributetConstants.SEARCH_META_DESC, null, ModelAttributetConstants.SEARCH_META_DESC,
		 * getI18nService().getCurrentLocale()) + " " + searchText + " " +
		 * getMessageSource().getMessage(ModelAttributetConstants.SEARCH_META_DESC_ON, null,
		 * ModelAttributetConstants.SEARCH_META_DESC_ON, getI18nService().getCurrentLocale()) + " " + getSiteName());
		 *
		 * final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
		 */

		//setUpMetaData(model, metaKeywords, metaDescription);
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
	 * Get the default search page size.
	 *
	 * @return the number of results per page, <tt>0</tt> (zero) indicated 'default' size should be used
	 */
	protected int getSearchPageSize()
	{
		return getSiteConfigService().getInt("storefront.search.pageSize", 0);
	}

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
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
		return CUSTOM_SKU_GRID_PAGE;
	}

	protected String getViewForPage(final Model model)
	{
		if (model.containsAttribute(CMS_PAGE_MODEL))
		{
			final AbstractPageModel page = (AbstractPageModel) model.asMap().get(CMS_PAGE_MODEL);
			if (page != null)
			{
				return getViewForPage(page);
			}
		}
		return null;
	}

	protected String getViewForPage(final AbstractPageModel page)
	{
		if (page != null)
		{
			final PageTemplateModel masterTemplate = page.getMasterTemplate();
			if (masterTemplate != null)
			{
				final String targetPage = cmsPageService.getFrontendTemplateName(masterTemplate);
				if (targetPage != null && !targetPage.isEmpty())
				{
					return PAGE_ROOT + targetPage;
				}
			}
		}
		return null;
	}

	protected void storeContinueUrl(final HttpServletRequest request)
	{
		final StringBuilder url = new StringBuilder();
		url.append(request.getServletPath());
		final String queryString = request.getQueryString();
		if (queryString != null && !queryString.isEmpty())
		{
			url.append('?').append(queryString);
		}
		getSessionService().setAttribute(WebConstants.CONTINUE_URL, url.toString());
	}

	protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		final int numberPagesShown = getSiteConfigService().getInt(PAGINATION_NUMBER_OF_RESULTS_COUNT, 5);

		model.addAttribute("numberPagesShown", Integer.valueOf(numberPagesShown));
		model.addAttribute("searchPageData", searchPageData);
		model.addAttribute("isShowAllAllowed", calculateShowAll(searchPageData, showMode));
		model.addAttribute("isShowPageAllowed", calculateShowPaged(searchPageData, showMode));
	}

	protected Boolean calculateShowAll(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean.valueOf((showMode != ShowMode.All && //
				searchPageData.getPagination().getTotalNumberOfResults() > searchPageData.getPagination().getPageSize())
				&& isShowAllAllowed(searchPageData));
	}

	protected Boolean calculateShowPaged(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean
				.valueOf(showMode == ShowMode.All
						&& (searchPageData.getPagination().getNumberOfPages() > 1 || searchPageData.getPagination().getPageSize() == getMaxSearchPageSize()));
	}

	/**
	 * Special case, when total number of results > {@link #MAX_PAGE_LIMIT}
	 */
	protected boolean isShowAllAllowed(final SearchPageData<?> searchPageData)
	{
		return searchPageData.getPagination().getNumberOfPages() > 1
				&& searchPageData.getPagination().getTotalNumberOfResults() < MAX_PAGE_LIMIT;
	}

	protected int getMaxSearchPageSize()
	{
		return MAX_PAGE_LIMIT;
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


}
