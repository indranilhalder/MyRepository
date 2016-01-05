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

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.enumeration.EnumerationService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.core.enums.SearchDropDownBrand;
import com.tisl.mpl.core.enums.SearchDropDownSeller;
import com.tisl.mpl.core.model.BrandCollectionComponentModel;
import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */

@Controller
@Scope("tenant")
@RequestMapping(value = "/brands")
public class BrandPageController extends AbstractSearchPageController
{

	protected static final Logger LOG = Logger.getLogger(BrandPageController.class);
	private static final String BRAND_LISTING_PAGE = "brandlist";
	private static final String HASH = "#";

	//	@Resource(name = "productSearchFacade")
	//	private ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;



	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	private static final String LAST_LINK_CLASS = "active";
	private Map<String, List<CategoryModel>> sortedMap;
	private Map<String, List<CategoryModel>> sortedMapForAToZ;
	private Map<String, List<CategoryModel>> CmsSortedMap;

	private static final String CODE = "MBH1";

	private final Collection<CategoryModel> allBrandList = new ArrayList<CategoryModel>();
	private List<CategoryModel> allBrandListForCategory = new ArrayList<CategoryModel>();



	private static final String ERROR_CMS_PAGE = "notFound";

	/**
	 * This method find the appropriate page for a brand and redirects accordingly
	 *
	 * @param brandName
	 * @param model
	 * @param request
	 * @return String
	 * @throws UnsupportedEncodingException
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getPageForBrand(@RequestParam("brandName") final String brandName, final Model model,
			final HttpServletRequest request) throws UnsupportedEncodingException, CMSItemNotFoundException
	{



		//Setting the brand name in search dropdown
		final List<SearchDropDownBrand> brandList = enumerationService.getEnumerationValues(SearchDropDownBrand.class);
		final List<SearchDropDownSeller> sellerList = enumerationService.getEnumerationValues(SearchDropDownSeller.class);



		//Populating Seller in search dropdown
		for (int index = 0; index < sellerList.size(); index++)
		{
			if (sellerList.get(index).getCode().equalsIgnoreCase(brandName))
			{
				for (final SearchDropDownSeller dropdownSeller : sellerList)
				{

					if (dropdownSeller.getCode().equalsIgnoreCase(brandName))
					{
						model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, dropdownSeller.getCode());
					}
				}

			}
		}

		//Populating brand in search dropdown
		for (final SearchDropDownBrand dropdownBrand : brandList)
		{

			if (dropdownBrand.getCode().equalsIgnoreCase(brandName))
			{
				model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, dropdownBrand.getCode());
			}
		}
		//Perform the brand search for products
		boolean showFacets = true;
		showFacets = true;
		/*
		 * final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData =
		 * performBrandSearch(brandName); if (searchPageData != null && searchPageData.getResults() != null) { showFacets
		 * = true; }
		 */
		//populateModel(model, searchPageData, ShowMode.Page);
		model.addAttribute(ModelAttributetConstants.SHOW_CATEGORIES_ONLY, Boolean.FALSE);

		//Check if there is a landing page for the brand with the brandName
		try
		{
			final ContentPageModel brandPageLandingPage = getContentPageForLabelOrId(brandName);
			storeCmsPageInModel(model, brandPageLandingPage);
			model.addAttribute(ModelAttributetConstants.SHOW_FACETS, showFacets);
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					Collections.singletonList(new Breadcrumb(HASH, brandName, LAST_LINK_CLASS)));
			setUpMetaDataForContentPage(model, brandPageLandingPage);
		}
		catch (final CMSItemNotFoundException e)
		{

			//storeCmsPageInModel(model, getCmsPageService().getDefaultCategoryPage());


			storeCmsPageInModel(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));
			GlobalMessages.addErrorMessage(model, "system.error.page.not.found");
			return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;

		}



		return getViewForPage(model);


	}




	/*
	 * private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> performBrandSearch(final String
	 * brandName) { //populating the search result in brand landing page final PageableData pageableData =
	 * createPageableData(0, getSearchPageSize(), null, ShowMode.Page); final SearchStateData searchState = new
	 * SearchStateData(); final SearchQueryData searchQueryData = new SearchQueryData();
	 * searchQueryData.setValue((brandName)); searchState.setQuery(searchQueryData); final String rootCategory =
	 * ROOT_CATEGORY; searchQueryData.setValue(brandName); searchState.setQuery(searchQueryData); return
	 * productSearchFacade.categorySearch(rootCategory, searchState, pageableData); }
	 */



	/**
	 * @description It is used for populating all brands Category and all brand list for a category from the database and
	 *              for fetching the manually populated brand category
	 * @param model
	 *
	 * @return view page of model for label id 'brandlist'
	 */

	@RequestMapping(value = "/brandlist", method = RequestMethod.GET)
	public String getListOfBrandsCategory(final Model model) throws CMSItemNotFoundException
	{
		try
		{

			final CategoryModel categoryModel = categoryService.getCategoryForCode(CODE);


			List<CategoryModel> subcategoryList = new ArrayList<CategoryModel>();
			subcategoryList = categoryModel.getCategories();

			final Map<String, Map<String, List<CategoryModel>>> allBrandListMapForCode = new HashMap();

			String categoryCode = null;
			boolean present = false;

			if (subcategoryList != null)
			{
				for (final CategoryModel category : subcategoryList)
				{

					allBrandListForCategory = category.getCategories();

					for (final CategoryModel subBrand : category.getAllSubcategories())
					{
						for (final CategoryModel brand : allBrandList)
						{
							if (brand.getCode().equalsIgnoreCase(subBrand.getCode()))
							{
								present = true;
							}
						}
						if (!present)
						{
							allBrandList.add(subBrand);
						}
					}






					categoryCode = category.getCode();
					sortedMapForAToZ = getBrandsInAplhabeticalOrder(allBrandList);
					sortedMap = getBrandsInAplhabeticalOrder(allBrandListForCategory);
					allBrandListMapForCode.put(categoryCode, sortedMap);



				}
			}


			//START : This Section is for Cms Managed Brand list
			List<BrandComponentModel> componentList = new ArrayList<BrandComponentModel>();
			Collection<CategoryModel> subBrandList = new ArrayList<CategoryModel>();
			final Map<String, Map<String, List<CategoryModel>>> mapForCmsBrands = new HashMap();

			final SimpleCMSComponentModel component = cmsComponentService
					.getSimpleCMSComponent("MultiAndOurFavouriteBrandComponent");

			final BrandCollectionComponentModel component1 = (BrandCollectionComponentModel) component;

			componentList = component1.getBrandCollection();

			if (componentList != null)
			{

				for (final BrandComponentModel brandComponent : componentList)
				{
					subBrandList = brandComponent.getSubBrands();

					final String masterBrandUid = brandComponent.getUid();

					if (!brandComponent.getUid().equalsIgnoreCase("A-ZBrands"))
					{

						for (final CategoryModel subBrand1 : brandComponent.getSubBrands())
						{
							LOG.debug("###### category code :#####" + subBrand1.getCode());
							CmsSortedMap = getBrandsInAplhabeticalOrder(subBrandList);
							mapForCmsBrands.put(masterBrandUid, CmsSortedMap);

						}


					}


				}
			}


			//END:This Section is for Cms Managed Brand list

			LOG.debug("  Size of allBrandListMapForCode  " + allBrandListMapForCode.size());


			model.addAttribute(ModelAttributetConstants.BRAND_COMPONENT_COLLECTION, componentList);
			model.addAttribute(ModelAttributetConstants.MULTI_BRANDS_MAP, mapForCmsBrands);

			model.addAttribute(ModelAttributetConstants.SUB_CATEGORY_LIST, subcategoryList);
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					Collections.singletonList(new Breadcrumb(HASH, "Brand", LAST_LINK_CLASS)));
			model.addAttribute(ModelAttributetConstants.ALL_BRAND_LIST_FOR_CATEGORY, allBrandListForCategory);
			model.addAttribute(ModelAttributetConstants.ALL_BRANDLIST_MAP, allBrandListMapForCode);
			model.addAttribute(ModelAttributetConstants.SORTED_MAP, sortedMap);
			model.addAttribute(ModelAttributetConstants.SORTED_MAP_AToZ, sortedMapForAToZ);
			storeCmsPageInModel(model, getContentPageForLabelOrId(BRAND_LISTING_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BRAND_LISTING_PAGE));

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
		return getViewForPage(model);
	}


	/**
	 * This method segregates the brands in alphabetical order like A having Addidas,Allensolly
	 *
	 * @param allBrandList1
	 *
	 *
	 *
	 * @return Map<String, List<CategoryModel>>
	 */
	private Map<String, List<CategoryModel>> getBrandsInAplhabeticalOrder(final Collection<CategoryModel> allBrandList1)
	{

		final ConcurrentMap<String, List<CategoryModel>> alphabeticalBrands = new ConcurrentHashMap();

		if (allBrandList1 != null)
		{
			for (final CategoryModel category : allBrandList1)
			{ //gets the first character from a sub brand name
				final String firstCharacter = category.getName().substring(0, 1);

				if (alphabeticalBrands.isEmpty())
				{
					//Creating the first entry for an empty map
					final List<CategoryModel> subBrandModelList = new ArrayList();
					subBrandModelList.add(category);
					alphabeticalBrands.put(firstCharacter, subBrandModelList);
				}
				else
				{
					boolean exists = false;

					for (final Entry<String, List<CategoryModel>> entry : alphabeticalBrands.entrySet())
					{
						if (entry.getKey().equals(firstCharacter))
						{

							final List<CategoryModel> value = entry.getValue();
							value.add(category);
							entry.setValue(value);

							exists = true;
						}

					}
					if (!exists)
					{
						//if the entry doesn't contain any existing entry
						final List<CategoryModel> subBrandModelList = new ArrayList();
						subBrandModelList.add(category);
						alphabeticalBrands.put(firstCharacter, subBrandModelList);
					}


				}
			}
		}


		final Map<String, List<CategoryModel>> sortedMap = new TreeMap<String, List<CategoryModel>>(alphabeticalBrands);

		return sortedMap;
	}







}
