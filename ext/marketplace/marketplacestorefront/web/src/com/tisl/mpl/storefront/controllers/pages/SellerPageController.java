package com.tisl.mpl.storefront.controllers.pages;


import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.SearchBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerMasterService;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.controllers.pages.SearchPageController.UserPreferencesData;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Controller for a seller page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/s")
public class SellerPageController extends AbstractSearchPageController
{
	@Resource(name = "mplSellerMaster")
	private MplSellerMasterService mplSellerMasterService;

	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;

	@Resource(name = "cmsPageService")
	private MplCmsPageService mplCmsPageService;

	@Resource(name = "searchBreadcrumbBuilder")
	private SearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = ModelAttributetConstants.ACCOUNT_BREADCRUMB_BUILDER)
	private ResourceBreadcrumbBuilder sellerBreadcrumbBuilder;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	//	@Resource(name = "productSearchFacade")
	//	private ProductSearchFacade<ProductData> productSearchFacade;

	private static final String LAST_LINK_CLASS = "active";

	protected static final Logger LOG = Logger.getLogger(SellerPageController.class);

	protected static final String SELLER_ID_PATH_VARIABLE_PATTERN = "/{sellerID:.*}";

	protected static final String SEARCH_CMS_PAGE_ID = "search";
	protected static final String SELLER_LISTING_CMS_PAGE_ID = "sellerListing";
	private static final String BZ_ERROR_CMS_PAGE = "businessErrorFound";
	private static final String NEW_SELLER_URL_PATTERN_PAGINATION = "/{sellerID}/page-{page}";

	// For TPR-198
	private static final String COMPILE_PATTERN = "page-[0-9]+";
	private static final String METAROBOTS_VALUE = "noindex,follow";


	//@RequestMapping(value = SELLER_ID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@SuppressWarnings("boxing")
	@RequestMapping(value =
	{ NEW_SELLER_URL_PATTERN_PAGINATION, SELLER_ID_PATH_VARIABLE_PATTERN }, method = RequestMethod.GET)
	public String seller(@PathVariable("sellerID") final String sellerID,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final HttpServletRequest request) throws UnsupportedEncodingException
	{

		populateSellarResult(sellerID, searchQuery, page, pageSize, showMode, sortCode, model, request);
		return getViewForPage(model);
	}


	// For AJAX Load : TPR-198
	@SuppressWarnings("boxing")
	@RequestMapping(value =
	{ NEW_SELLER_URL_PATTERN_PAGINATION + "/getFacetData", SELLER_ID_PATH_VARIABLE_PATTERN + "/getFacetData" }, method = RequestMethod.GET)
	public String sellerFacetSearch(@PathVariable("sellerID") final String sellerID,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "pageSize", required = false) final Integer pageSize,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final HttpServletRequest request) throws UnsupportedEncodingException
	{

		populateSellarResult(sellerID, searchQuery, page, pageSize, showMode, sortCode, model, request);
		return ControllerConstants.Views.Fragments.Product.SellerResultsPage;
	}

	/**
	 * @param sellerID
	 * @param searchQuery
	 * @param page
	 * @param pageSize
	 * @param showMode
	 * @param sortCode
	 * @param model
	 * @param request
	 * @return String
	 */
	@SuppressWarnings("boxing")
	private String populateSellarResult(final String sellerID, final String searchQuery, int page, Integer pageSize,
			final ShowMode showMode, final String sortCode, final Model model, final HttpServletRequest request)
			throws UnsupportedEncodingException
	{
		//Set the drop down text if the attribute is not empty or null

		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		model.addAttribute("searchCode", sellerID);
		//Check if there is a landing page for the seller
		final SellerMasterModel sellerMaster = mplSellerMasterService.getSellerMaster(sellerID);

		try
		{
			if (mplSellerMasterService.getSellerMaster(sellerID) != null)
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
				pageSize = pageSize != null ? pageSize : getSearchPageSize();
				final UserPreferencesData preferencesData = updateUserPreferences(pageSize);
				if (preferencesData != null && preferencesData.getPageSize() != null)
				{
					pageSize = preferencesData.getPageSize().intValue();
				}


				final String sellerName = sellerMaster.getLegalName();

				final ContentPageModel sellerLandingPage = getLandingPageForSeller(sellerMaster);
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						Collections.singletonList(new Breadcrumb("#", sellerName, LAST_LINK_CLASS)));

				model.addAttribute(ModelAttributetConstants.METAROBOTS, METAROBOTS_VALUE);
				final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(sellerLandingPage.getKeywords());
				final String metaDescription = MetaSanitizerUtil.sanitizeDescription(sellerLandingPage.getDescription());
				setUpMetaData(model, metaKeywords, metaDescription);

				/**
				 * offer facets
				 */
				final PageableData pageableData = createPageableData(page, pageSize, null, ShowMode.Page);
				final SearchStateData searchState = new SearchStateData();
				final SearchQueryData searchQueryData = new SearchQueryData();
				searchQueryData.setValue(sellerName);

				searchState.setQuery(searchQueryData);
				searchPageData = searchFacade.dropDownSearch(searchState, sellerID, MarketplaceCoreConstants.SELLER_ID, pageableData);
				searchPageData = updatePageData(searchPageData, sellerID, searchQuery);
				final StringBuilder urlBuilder = new StringBuilder(200);
				final List<FacetData<SearchStateData>> facets = searchPageData.getFacets();
				if (CollectionUtils.isNotEmpty(facets))
				{
					LOG.debug("Iterating through all Facets");
					for (final FacetData<SearchStateData> facet : facets)
					{
						LOG.debug("Checking for Promotional Facets");
						if (facet.getCode().equalsIgnoreCase("allpromotions"))
						{
							LOG.debug("Facet Code matched for promotional facets");
							for (final FacetValueData<SearchStateData> facetvalue : facet.getValues())
							{
								LOG.debug("Appending url" + facetvalue.getQuery().getUrl());
								urlBuilder.append(facetvalue.getQuery().getUrl());
								urlBuilder.append('&');
							}
						}
					}
					LOG.debug("Url for Shop the Sale" + urlBuilder.toString());
					model.addAttribute(ModelAttributetConstants.SHOP_THE_SALE_URL, urlBuilder.toString());
				}

				storeCmsPageInModel(model, sellerLandingPage);
			}
			else
			{
				final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
				storeCmsPageInModel(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));

				model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
						sellerBreadcrumbBuilder.getBreadcrumbs(MessageConstants.BREADCRUMB_NOT_FOUND));
				GlobalMessages.addErrorMessage(model, messageKey);

				storeContentPageTitleInModel(model, MessageConstants.BUSINESS_ERROR);
				LOG.error("No Seller Found");
			}


		}
		catch (final CMSItemNotFoundException e)
		{
			try
			{
				final SearchQueryData searchQueryData = new SearchQueryData();
				final String sellerLegalName = sellerMaster.getLegalName();

				searchQueryData.setValue(sellerLegalName);
				SellerSearchEvaluator sellerSearch = null;
				sellerSearch = new SellerSearchEvaluator(sellerID, XSSFilterUtil.filter(searchQuery), page, showMode, sortCode);
				sellerSearch.doSearch(searchQuery, pageSize);
				populateModel(model, sellerSearch.getSearchPageData(), showMode);
				model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, sellerLegalName);
				model.addAttribute(ModelAttributetConstants.HIDE_DEPARTMENTS, Boolean.TRUE);
				model.addAttribute(ModelAttributetConstants.OTHER_PRODUCTS, true);
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						searchBreadcrumbBuilder.getBreadcrumbs(null, sellerSearch.getSearchPageData()));
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						Collections.singletonList(new Breadcrumb(ModelAttributetConstants.HASH_VAL, sellerLegalName, LAST_LINK_CLASS)));

				storeCmsPageInModel(model, getContentPageForLabelOrId(SELLER_LISTING_CMS_PAGE_ID));
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
					LOG.error("Exception occured " + e1);
				}
			}
		}
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
				LOG.error("Exception occured " + e1);
			}
		}
		return null;
	}

	/**
	 * @param categoryName
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	private ContentPageModel getLandingPageForSeller(final SellerMasterModel sellerMaster) throws CMSItemNotFoundException
	{

		final ContentPageModel landingPage = mplCmsPageService.getLandingPageForSeller(sellerMaster);

		if (landingPage == null)
		{
			throw new CMSItemNotFoundException("Could not find a landing page for the seller " + sellerMaster.getLegalName());
		}

		return landingPage;

	}

	protected class SellerSearchEvaluator
	{
		private final SearchQueryData searchQueryData = new SearchQueryData();
		private final int page;
		private final ShowMode showMode;
		private final String sortCode;
		private final String sellerID;
		//		private CategoryPageModel categoryPage;
		//		private boolean showCategoriesOnly;
		private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData;


		public SellerSearchEvaluator(final String sellerID, final String searchQuery, final int page, final ShowMode showMode,
				final String sortCode)
		{

			this.searchQueryData.setValue(searchQuery);
			this.page = page;
			this.showMode = showMode;
			this.sortCode = sortCode;
			this.sellerID = sellerID;
		}






		public void doSearch(final String searchQuery, final int pageSize)
		{
			//showCategoriesOnly = false;

			if (searchQueryData.getValue() == null)
			{

				final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);

				// Direct category link without filtering
				final SearchStateData searchState = new SearchStateData();
				searchState.setQuery(searchQueryData);
				searchPageData = searchFacade.dropDownSearchForSeller(searchState, sellerID, MarketplaceCoreConstants.SELLER_ID,
						pageableData);

				searchPageData = updatePageData(searchPageData, sellerID, searchQuery);


			}
			else
			{

				final SearchStateData searchState = new SearchStateData();
				searchState.setQuery(searchQueryData);

				final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
				searchPageData = searchFacade.dropDownSearchForSellerListing(searchState, sellerID, pageableData);

				searchPageData = updatePageData(searchPageData, sellerID, searchQuery);

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



}
