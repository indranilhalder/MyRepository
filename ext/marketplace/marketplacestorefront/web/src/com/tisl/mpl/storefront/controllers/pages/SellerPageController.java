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
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
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

	@RequestMapping(value = SELLER_ID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String seller(@PathVariable("sellerID") final String sellerID,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException
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
				final String sellerName = sellerMaster.getLegalName();

				final ContentPageModel sellerLandingPage = getLandingPageForSeller(sellerMaster);
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						Collections.singletonList(new Breadcrumb("#", sellerName, LAST_LINK_CLASS)));

				model.addAttribute("metaRobots", "noindex,follow");
				final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(sellerLandingPage.getKeywords());
				final String metaDescription = MetaSanitizerUtil.sanitizeDescription(sellerLandingPage.getDescription());
				setUpMetaData(model, metaKeywords, metaDescription);

				/**
				 * offer facets
				 */

				final PageableData pageableData = createPageableData(0, getSearchPageSize(), null, ShowMode.Page);
				final SearchStateData searchState = new SearchStateData();
				final SearchQueryData searchQueryData = new SearchQueryData();
				searchQueryData.setValue(sellerName);
				searchState.setQuery(searchQueryData);
				searchPageData = searchFacade.dropDownSearch(searchState, sellerID, MarketplaceCoreConstants.SELLER_ID, pageableData);
				final StringBuilder urlBuilder = new StringBuilder(200);
				final List<FacetData<SearchStateData>> facets = searchPageData.getFacets();
				//if (null != facets && facets.size() > 0)
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
					model.addAttribute("shop_the_sale_url", urlBuilder.toString());
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
				final SellerSearchEvaluator sellerSearch = new SellerSearchEvaluator(sellerID, XSSFilterUtil.filter(searchQuery),
						page, showMode, sortCode);
				sellerSearch.doSearch();

				populateModel(model, sellerSearch.getSearchPageData(), showMode);
				model.addAttribute("dropDownText", sellerLegalName);
				model.addAttribute("hideDepartments", Boolean.TRUE);
				model.addAttribute("otherProducts", true);
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						searchBreadcrumbBuilder.getBreadcrumbs(null, sellerSearch.getSearchPageData()));
				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						Collections.singletonList(new Breadcrumb("#", sellerLegalName, LAST_LINK_CLASS)));

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

		return getViewForPage(model);
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

		public void doSearch()
		{
			//showCategoriesOnly = false;
			if (searchQueryData.getValue() == null)
			{

				final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);

				// Direct category link without filtering
				final SearchStateData searchState = new SearchStateData();
				final SearchQueryData searchQueryData = new SearchQueryData();
				searchState.setQuery(searchQueryData);
				searchPageData = searchFacade.dropDownSearchForSeller(searchState, sellerID, MarketplaceCoreConstants.SELLER_ID,
						pageableData);

			}
			else
			{

				final SearchStateData searchState = new SearchStateData();
				searchState.setQuery(searchQueryData);

				final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
				searchPageData = searchFacade.dropDownSearchForSellerListing(searchState, sellerID, pageableData);
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
}
