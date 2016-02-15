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
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteResultData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.url.UrlResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplCategoryService;
import com.tisl.mpl.model.cms.components.NeedHelpComponentModel;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.breadcrumb.impl.MplSearchBreadcrumbBuilder;
import com.tisl.mpl.storefront.web.data.MplAutocompleteResultData;


@Controller
@Scope("tenant")
@RequestMapping("/micrositeSearch")
public class MicrositeSearchPageController extends AbstractSearchPageController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MicrositeSearchPageController.class);

	private static final String COMPONENT_UID_PATH_VARIABLE_PATTERN = "{componentUid:.*}";

	private static final String SEARCH_CMS_PAGE_ID = "search";
	private static final String NO_RESULTS_CMS_PAGE_ID = "searchEmpty";

	private static final String DROPDOWN_BRAND = "brand-";
	private static final String DROPDOWN_CATEGORY = "category-";
	private static final String DROPDOWN_SELLER = "seller-";

	public static final String REDIRECT_PREFIX = "redirect:";

	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "searchBreadcrumbBuilder")
	private MplSearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "customerLocationService")
	private CustomerLocationService customerLocationService;

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	//	@Resource(name = "configurationService") Avoid unused private fields
	//	private ConfigurationService configurationService;

	@Resource(name = "mplCategoryServiceImpl")
	private MplCategoryService mplCategoryService;

	//	@Resource(name = "enumerationService") Avoid unused private fields
	//	private EnumerationService enumerationService;
	//	@Resource(name = "defaultCategoryService")
	//	private DefaultCategoryService defaultCategoryService;
	//	@Resource(name = "defaultProductService")
	//	private DefaultProductService defaultProductService;

	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;

	//	@Resource(name = "sessionService") Avoid unused private fields
	//	private SessionService sessionService;

	@Resource(name = "categoryModelUrlResolver")
	private UrlResolver<CategoryModel> categoryModelUrlResolver;

	//	@Resource(name = "helpMeShopFacade")  Avoid unused private fields
	//	private HelpMeShopFacade helpMeShopFacade;



	/**
	 *
	 * @param searchText
	 * @param dropDownText
	 * @param request
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "{mSellerID}", method = RequestMethod.GET, params = "!q")
	public String textSearch(@RequestParam(value = "text", defaultValue = "") final String searchText,
			@RequestParam(value = "micrositeSearchCategory") final String dropDownValue,
			@PathVariable("mSellerID") final String mSellerID, final HttpServletRequest request, final Model model)
					throws CMSItemNotFoundException
	{
		//---------------start--------------
		boolean foundCat = false;


		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		String dropDownText = "";

		if (StringUtils.isNotBlank(searchText) && mSellerID != null)
		{
			final PageableData pageableData = createPageableData(0, getSearchPageSize(), null, ShowMode.Page);

			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			searchQueryData.setValue(XSSFilterUtil.filter(searchText));
			searchState.setQuery(searchQueryData);
			if (dropDownValue == null || dropDownValue.equalsIgnoreCase(MarketplaceCoreConstants.ALL_CATEGORY))
			{
				searchQueryData.setValue(searchText);
				searchState.setQuery(searchQueryData);
				searchPageData = searchFacade.dropDownSearchForSeller(searchState, mSellerID, "ALL", pageableData);

			}
			else
			{

				if (dropDownValue.startsWith(DROPDOWN_CATEGORY))
				{
					foundCat = true;
					dropDownText = dropDownValue.replaceFirst(DROPDOWN_CATEGORY, "");
					searchQueryData.setValue(searchText);
					searchState.setQuery(searchQueryData);
					searchPageData = searchFacade.sellerCategorySearch(dropDownText, mSellerID, searchState, pageableData);
				}
				if (!foundCat)
				{
					if (dropDownValue.startsWith(DROPDOWN_BRAND))
					{

						dropDownText = dropDownValue.replaceFirst(DROPDOWN_BRAND, "");
						searchQueryData.setValue(searchText);
						searchState.setQuery(searchQueryData);
						searchPageData = searchFacade.sellerCategorySearch(dropDownText, mSellerID, searchState, pageableData);

					}
				}

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
		}
		else
		{
			if (dropDownValue.trim().equalsIgnoreCase(MarketplaceCoreConstants.ALL_CATEGORY))
			{
				dropDownText = dropDownValue;
				storeCmsPageInModel(model, getContentPageForLabelOrId(null));
			}
			else
			{
				boolean brandMatch = false;
				boolean sellerMatch = false;
				boolean categoryMatch = false;
				if (dropDownValue.startsWith(DROPDOWN_BRAND))
				{
					dropDownText = dropDownValue.replaceFirst(DROPDOWN_BRAND, "");
					brandMatch = true;
				}
				if (dropDownValue.startsWith(DROPDOWN_SELLER))
				{
					dropDownText = dropDownValue.replaceFirst(DROPDOWN_SELLER, "");
					sellerMatch = true;
				}
				if (dropDownValue.startsWith(DROPDOWN_CATEGORY))
				{
					dropDownText = dropDownValue.replaceFirst(DROPDOWN_CATEGORY, "");
					categoryMatch = true;
				}

				if (brandMatch)
				{
					storeCmsPageInModel(model,
							getContentPageForLabelOrId(dropDownText.trim().toLowerCase() + MarketplaceCoreConstants.BRAND_CMS_PAGE_ID));
				}
				else if (sellerMatch)
				{
					storeCmsPageInModel(model,
							getContentPageForLabelOrId(dropDownText.trim().toLowerCase() + MarketplaceCoreConstants.SELLER_CMS_PAGE_ID));
				}

				else if (categoryMatch)
				{
					final CategoryModel category = mplCategoryService
							.getCategoryModelForCode(cmsSiteService.getCurrentCatalogVersion(), dropDownText);
					//Added to redirect to the category landing page for blank search with category selected
					return REDIRECT_PREFIX + categoryModelUrlResolver.resolve(category);
				}
			}
		}

		model.addAttribute("pageType", PageType.PRODUCTSEARCH.name());
		model.addAttribute("metaRobots", "noindex,follow");

		final String metaDescription = MetaSanitizerUtil
				.sanitizeDescription(getMessageSource().getMessage("search.meta.description.results", null,
						"search.meta.description.results", getI18nService().getCurrentLocale()) + " " + searchText + " "
				+ getMessageSource().getMessage("search.meta.description.on", null, "search.meta.description.on",
						getI18nService().getCurrentLocale()) + " " + getSiteName());
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
		setUpMetaData(model, metaKeywords, metaDescription);

		model.addAttribute("dropDownText", dropDownText);
		model.addAttribute("searchCategoryType", MplConstants.CATEGORY);
		model.addAttribute("searchCategoryValue", dropDownText);
		model.addAttribute("autoselectSearchDropDown", Boolean.FALSE);

		if (searchPageData != null)
		{
			model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
		}

		return getViewForPage(model);
	}


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
	 *
	 * @param componentUid
	 * @param term
	 * @param category
	 * @return AutocompleteResultData
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings("boxing")
	@ResponseBody
	@RequestMapping(value = "/autocomplete/" + COMPONENT_UID_PATH_VARIABLE_PATTERN
			+ "/sellerID/{mSellerID}", method = RequestMethod.GET)
	public AutocompleteResultData getAutocompleteSuggestions(@PathVariable final String componentUid,
			@PathVariable("mSellerID") final String mSellerID, @RequestParam("term") final String term,
			@RequestParam("category") final String category) throws CMSItemNotFoundException
	{
		final MplAutocompleteResultData resultData = new MplAutocompleteResultData();
		String dropDownText = "";

		//final SearchBoxComponentModel component = (SearchBoxComponentModel) cmsComponentService.getSimpleCMSComponent(componentUid);

		if (mSellerID != null)
		{
			//resultData.setSuggestions(subList(productSearchFacade.getAutocompleteSuggestions(term), component.getMaxSuggestions()));
			resultData.setSuggestions(productSearchFacade.getAutocompleteSuggestions(term));
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			searchQueryData.setValue(resultData.getSuggestions().size() > 0 ? resultData.getSuggestions().get(0).getTerm() : term);
			searchState.setQuery(searchQueryData);
			searchState.setSns(true);

			final PageableData pageableData = null;

			ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;


			if (!(resultData.getSuggestions().isEmpty()))
			{

				boolean categoryMatch = false;
				final boolean sellerMatch = false;
				if (MarketplaceCoreConstants.ALL_CATEGORY.equalsIgnoreCase(category))
				{

					searchPageData = searchFacade.dropDownSearchForSeller(searchState, mSellerID, "ALL", pageableData);
					resultData.setCategories(searchPageData.getMicrositeSnsCategories());
					resultData.setBrands(searchPageData.getAllBrand());
				}
				else
				{





					if (category.startsWith(DROPDOWN_CATEGORY))
					{
						categoryMatch = true;
						dropDownText = category.replaceFirst(DROPDOWN_CATEGORY, "");
						searchPageData = searchFacade.sellerCategorySearch(dropDownText, mSellerID, searchState, pageableData);

					}
					if (!categoryMatch && !sellerMatch)
					{
						if (category.startsWith(DROPDOWN_BRAND))
						{
							dropDownText = category.replaceFirst(DROPDOWN_BRAND, "");
							searchPageData = searchFacade.sellerCategorySearch(dropDownText, mSellerID, searchState, pageableData);


						}
					}

					resultData.setCategories(searchPageData.getMicrositeSnsCategories());
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

		return resultData;
	}


	/**
	 *
	 * @param list
	 * @param maxElements
	 * @return List
	 */

	protected <E> List<E> subList(final List<E> list, final int maxElements)
	{
		if (CollectionUtils.isEmpty(list))
		{
			return Collections.emptyList();
		}

		if (list.size() > maxElements)
		{
			return list.subList(0, maxElements);
		}

		return list;
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
}
