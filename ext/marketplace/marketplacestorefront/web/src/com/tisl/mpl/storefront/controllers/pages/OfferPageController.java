package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.SearchBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.controllers.pages.SearchPageController.UserPreferencesData;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Controller for a Offer page
 */
@Controller
@Scope("tenant")
//@RequestMapping(value = "/**/o")
public class OfferPageController extends AbstractSearchPageController
{

	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	/*
	 * @Resource(name = "cmsPageService") private MplCmsPageService mplCmsPageService;
	 */

	@Resource(name = "searchBreadcrumbBuilder")
	private SearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	/*
	 * @Resource(name = ModelAttributetConstants.ACCOUNT_BREADCRUMB_BUILDER) private ResourceBreadcrumbBuilder
	 * sellerBreadcrumbBuilder;
	 */

	private static final String LAST_LINK_CLASS = "active";
	private static final String PAGE = "page";

	protected static final Logger LOG = Logger.getLogger(OfferPageController.class);

	protected static final String CATEGORY_ID_PATH_VARIABLE_PATTERN = "/{categoryID:.*}";
	protected static final String CATEGORY_ID_PATH_VARIABLE_PATTERN_NEW = "/**/o/{categoryID:.*}";

	protected static final String OFFER_LISTING_CMS_PAGE_ID = "offerPageListing";

	protected static final String CHANNEL = "web";

	//TISPRD-1867
	private static final String NEW_OFFER_URL_PATTERN_PAGINATION = "/**/view-all-offers";
	private static final String NEW_OFFER_NEW_URL_PATTERN_PAGINATION = "/**/view-all-offers/page-{pageNo}";

	//TISPRD-1867
	//@RequestMapping(value = CATEGORY_ID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequestMapping(value = CATEGORY_ID_PATH_VARIABLE_PATTERN_NEW, method = RequestMethod.GET)
	public String offer(@PathVariable("categoryID") final String categoryID,
			@RequestParam(value = "offer", required = false) final String offerID,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException,
			CMSItemNotFoundException
	{

		try
		{
			final OfferSearchEvaluator offerSearch = new OfferSearchEvaluator(offerID, XSSFilterUtil.filter(searchQuery), page,
					showMode, sortCode, categoryID);
			offerSearch.doSearch();

			populateModel(model, offerSearch.getSearchPageData(), showMode);
			if (null != offerSearch.getSearchPageData())
			{
				model.addAttribute("currentQuery", offerSearch.getSearchPageData().getCurrentQuery().getQuery().getValue());
			}
			model.addAttribute("hideDepartments", Boolean.TRUE);
			model.addAttribute("otherProducts", true);

			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					searchBreadcrumbBuilder.getBreadcrumbs(null, offerSearch.getSearchPageData()));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					Collections.singletonList(new Breadcrumb("#", "Offers", LAST_LINK_CLASS)));
			model.addAttribute("offer", offerID);

			storeCmsPageInModel(model, getContentPageForLabelOrId(OFFER_LISTING_CMS_PAGE_ID));
		}
		catch (final Exception exp)
		{

			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exp,
					MarketplacecommerceservicesConstants.E0000));

			return frontEndErrorHelper.callNonBusinessError(model, exp.getMessage());

		}
		return getViewForPage(model);
	}


	//Added to render all the offer related products
	//@RequestMapping(value = "/viewAllOffers/page-{pageNo}/getFacetData",
	@RequestMapping(value =
	{ NEW_OFFER_URL_PATTERN_PAGINATION + "/getFacetData", NEW_OFFER_NEW_URL_PATTERN_PAGINATION + "/getFacetData" }, method = RequestMethod.GET)
	public String offerRelatedFacetResult(@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0", required = false) final int page,
			@RequestParam(value = "show", defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{

		populateRefineSearchResult(searchQuery, page, showMode, sortCode, pageSize, request, model);
		return ControllerConstants.Views.Fragments.Product.SearchResultsPanel;
	}

	/**
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param pageSize
	 * @param request
	 * @param model
	 * @return null or exception message
	 */
	private String populateRefineSearchResult(final String searchQuery, int page, final ShowMode showMode, final String sortCode,
			final Integer pageSize, final HttpServletRequest request, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final String uri = request.getRequestURI();
			if (uri.contains(PAGE))
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
			int count = getSearchPageSize();
			final UserPreferencesData preferencesData = updateUserPreferences(pageSize);
			if (preferencesData != null && preferencesData.getPageSize() != null)
			{
				count = preferencesData.getPageSize().intValue();
			}
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = performSearchForAllOffers(
					searchQuery, page, showMode, sortCode, count);
			populateModel(model, searchPageData, ShowMode.Page);
			model.addAttribute("offers", Boolean.TRUE);
			//model.addAttribute("hideDepartments", Boolean.TRUE);
			model.addAttribute("otherProducts", true);
			if (searchPageData != null)
			{
				model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
			}


			//Code to hide the applied facet for isOfferExisting
			//Find Bug Fix
			if (searchPageData != null && searchPageData.getBreadcrumbs() != null && searchPageData.getBreadcrumbs().size() == 1)
			{
				final String facetCode = searchPageData.getBreadcrumbs().get(0).getFacetCode();
				if (StringUtils.isNotEmpty(facetCode) && facetCode.equalsIgnoreCase("isOffersExisting"))
				{
					searchPageData.setBreadcrumbs(null);
				}
			}


			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					Collections.singletonList(new Breadcrumb("#", "Offers", LAST_LINK_CLASS)));

			storeCmsPageInModel(model, getContentPageForLabelOrId(OFFER_LISTING_CMS_PAGE_ID));

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, e.getErrorMessage());

		}
		catch (final Exception exp)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exp,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, exp.getMessage());

		}
		return null;
	}


	//Added to render all the offer related products
	//@RequestMapping(value = "/viewAllOffers/page-{pageNo}",
	@RequestMapping(value =
	{ NEW_OFFER_URL_PATTERN_PAGINATION, NEW_OFFER_NEW_URL_PATTERN_PAGINATION }, method = RequestMethod.GET)
	public String displayOfferRelatedProducts(@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0", required = false) final int page,
			@RequestParam(value = "show", defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
		populateRefineSearchResult(searchQuery, page, showMode, sortCode, pageSize, request, model);
		return getViewForPage(model);
	}

	/**
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param searchPageSize
	 */
	private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> performSearchForAllOffers(
			final String searchQuery, final int page, final ShowMode showMode, final String sortCode, final int searchPageSize)
	{
		final PageableData pageableData = createPageableData(page, searchPageSize, sortCode, ShowMode.Page);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();

		if (StringUtils.isNotEmpty(searchQuery))
		{
			searchQueryData.setValue(searchQuery);
		}

		searchState.setQuery(searchQueryData);

		return searchFacade.searchAllOffers(searchState, pageableData);
	}

	//End

	protected class OfferSearchEvaluator
	{
		private final SearchQueryData searchQueryData = new SearchQueryData();
		private final int page;
		private final ShowMode showMode;
		private final String sortCode;
		private final String offerID;
		private final String categoryID;
		private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData;

		public OfferSearchEvaluator(final String offerID, final String searchQuery, final int page, final ShowMode showMode,
				final String sortCode, final String categoryID)
		{
			this.searchQueryData.setValue(searchQuery);
			this.page = page;
			this.showMode = showMode;
			this.sortCode = sortCode;
			this.offerID = offerID;
			this.categoryID = categoryID;
		}

		public void doSearch()
		{
			//showCategoriesOnly = false;
			if (searchQueryData.getValue() == null)
			{
				final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
				final SearchStateData searchState = new SearchStateData();
				final SearchQueryData searchQueryData = new SearchQueryData();
				searchState.setQuery(searchQueryData);
				searchPageData = searchFacade.dropDownSearchForOffer(searchState, offerID, pageableData, categoryID, CHANNEL);

			}
			else
			{
				final SearchStateData searchState = new SearchStateData();
				searchState.setQuery(searchQueryData);
				final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
				searchPageData = searchFacade.dropDownSearchForOfferListing(searchState, offerID, pageableData, categoryID);

			}
		}

		public int getPage()
		{
			return page;
		}


		public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> getSearchPageData()
		{
			return searchPageData;
		}
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

	protected void setUserPreferences(final UserPreferencesData userPreferencesData)
	{
		final Session session = sessionService.getCurrentSession();
		session.setAttribute("preferredCategoryPreferences", userPreferencesData);
	}
}