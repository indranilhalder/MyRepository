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


import de.hybris.platform.acceleratorcms.model.components.ProductGridComponentModel;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorservices.storefront.data.MetaElementData;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCategoryPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
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
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.tisl.lux.facade.CommonUtils;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.model.CustomSkuComponentModel;
import com.tisl.mpl.core.model.PriorityBrandsModel;
import com.tisl.mpl.core.model.SeoContentModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.category.MplCategoryFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.marketplacecommerceservices.service.brand.impl.DefaultBrandService;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
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
	/**
	 *
	 */
	private static final String LSH = "LSH";


	/**
	 *
	 */
	//private static final String LSH1 = "LSH1";

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "cmsPageService")
	private MplCmsPageService mplCmsPageService;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	//Added for TISLUX-91 s
	@Autowired
	private ConfigurationService configurationService;

	@Resource(name = "brandService")
	private DefaultBrandService brandService;

	@Autowired
	private MplCategoryFacade mplCategoryFacade;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Autowired
	private CommonUtils commonUtils;


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

	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;

	private static final String NEW_CATEGORY_URL_PATTERN = "/**/c-{categoryCode:.*}";
	private static final String NEW_CATEGORY_URL_PATTERN_PAGINATION = "/**/c-{categoryCode:.*}/page-{page}";
	private static final String CATEGORY_URL_OLD_PATTERN = "/**/c";
	//	private static final String LAST_LINK_CLASS = "active";

	private static final String PAGE = "page";
	private static final String PAGEVAl = "Page";
	private static final String SHOW = "show";
	private static final String SORT = "sort";
	private static final String CATERGORYCODE = "categoryCode";

	protected static final Logger LOG = Logger.getLogger(CategoryPageController.class);
	//Added For TISPRD-1243
	private static final String DROPDOWN_BRAND = "MBH";
	private static final String DROPDOWN_CATEGORY = "MSH";

	//Added for TPR-198
	private static final String RELEVANCE = ":relevance";
	private static final String EXCEPTION_OCCURED = ">> Exception occured ";
	private static final String LOCATION = "Location";//SonarFix
	private static final String PAGE_FACET_DATA = "pageFacetData";

	//TPR_1282
	private static final String CATEGORY_FOOTER_TEXT = "categoryFooterTxt";
	private static final String SPECIAL_CHARACTERS = "[^\\w\\s]";

	//SonarFix
	private static final String BRANDNAME = "brand";
	private static final String SPACE_CHARACTERS = "\\s";
	private static final String SLASH_C = "/c-";

	private int pageSiseCount;



	private static final String NEW_BRAND_URL_PATTERN = "/**/c-{categoryCode}/b-{brandCode}";
	private static final String NEW_BRAND_URL_PATTERN_PAGINATION = "/**/c-{categoryCode}/b-{brandCode}/page-{page}";
	/* TPR-1283 --Ends */

	//UF-15,16
	//sonar fix
	//private static final Integer PAGE_SIZE = new Integer(24);

	//sonar fix
	private static final String PAGE_SIZE_KEY = "pageSize";

	private static final Integer PAGE_SIZE = Integer.valueOf(24);

	private static final String SEARCH_CATEGORY_KEY = "searchCategory";
	private static final String RESET_ALL_KEY = "resetAll";
	private static final String CAT_NAME_KEY = "catName";
	private static final String CAT_CODE_KEY = "catCode";
	private static final String BUY_KEY = " Buy ";
	private static final String ONLINE_KEY = " Online";
	private static final String LAZY_INTERFACE_KEY = "lazyInterface";

	//UF-265
	//private static final String PRODUCT_GRID_COMPONENT_POSITION = "Section4B";
	//For most of the templates PLP slot is Section4B
	//For BrandPageTemplate PLP slot is Section3
	//For FootwearCategoryLandingPageTemplate PLP slot is Section7B
	//For FootwearBrandLandingPageTemplate PLP slot is Section4
	//SDI-1078
	private static final String[] PRODUCT_GRID_COMPONENT_POSITION_LIST =
	{ "Section4B", "Section3", "Section7B", "Section4" };
	private static final String CUSTOM_SKU_COMPONENT_POSITION = "Section4A";

	/**
	 * @return the pageSiseCount
	 */
	public int getPageSiseCount()
	{
		return pageSiseCount;
	}

	/**
	 * @param pageSiseCount
	 *           the pageSiseCount to set
	 */
	public void setPageSiseCount(final int pageSiseCount)
	{
		this.pageSiseCount = pageSiseCount;
	}

	/**
	 * TPR-1283 CHANGES
	 * 
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
	 */
	@RequestMapping(value =
	{ NEW_BRAND_URL_PATTERN }, method = RequestMethod.GET)
	public String brand(@PathVariable(CATERGORYCODE) String categoryCode, @PathVariable("brandCode") String brandCode,
			@RequestParam(value = "q", required = false) String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") int pageNo,
			@RequestParam(value = SHOW, defaultValue = PAGEVAl) final ShowMode showMode,
			@RequestParam(value = SORT, required = false) final String sortCode,
			@RequestParam(value = PAGE_SIZE_KEY, required = false) final Integer pageSize,
			@RequestParam(value = SEARCH_CATEGORY_KEY, required = false) String dropDownText,
			@RequestParam(value = RESET_ALL_KEY, required = false) final boolean resetAll, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
	{
		//EQA review comments added
		try
		{
			final boolean isBrand = true;
			if (request.getServletPath().contains("&"))
			{
				request.getServletPath().replace("&", "-").replaceAll(SPACE_CHARACTERS, "").toLowerCase();
			}
			else
			{
				request.getServletPath().replaceAll(SPACE_CHARACTERS, "-").toLowerCase();
			}
			categoryCode = categoryCode.toUpperCase();
			brandCode = brandCode.toUpperCase();
			if (StringUtils.isNotEmpty(brandCode))
			{
				searchQuery = ":relevance:brand:" + brandCode;
			}
			String searchCode = new String(categoryCode);
			//SEO: New pagination detection TISCR 340
			pageNo = getPaginatedPageNo(request);
			//applying search filters
			if (searchQuery != null)
			{
				getfilterListCountForSize(searchQuery);
				model.addAttribute(ModelAttributetConstants.SIZE_COUNT, Integer.valueOf(getfilterListCountForSize(searchQuery)));
				model.addAttribute(ModelAttributetConstants.SEARCH_QUERY_VALUE, searchQuery);
			}
			//TISPRD-2315(checking whether the link has been clicked for pagination)
			if (checkIfPagination(request) && searchQuery == null)
			{
				searchQuery = RELEVANCE;
			}

			// Get page facets to include in facet field exclude tag
			final String pageFacets = request.getParameter(PAGE_FACET_DATA);

			//Storing the user preferred search results count
			updateUserPreferences(pageSize);

			if (!commonUtils.isLuxurySite() && StringUtils.isNotEmpty(searchCode)
					&& (categoryCode.startsWith(MplConstants.SALES_HIERARCHY_ROOT_CATEGORY_CODE))
					&& !(searchCode.substring(0, 5).equals(categoryCode)))
			{
				searchCode = searchCode.substring(0, 5);

			}
			model.addAttribute(ModelAttributetConstants.SEARCH_CODE, searchCode);
			model.addAttribute(ModelAttributetConstants.IS_CATEGORY_PAGE, Boolean.TRUE);
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
				model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, dropDownText);

			}
			else
			{
				final String categoryName = (category == null) ? "" : category.getName();
				model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, categoryName);
			}
			updateUserPreferences(pageSize);

			if (category != null)
			{

				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
						categoryCode, searchQuery, pageNo, showMode, sortCode, getSearchPageSize(), resetAll, pageFacets);

				//method signature modified for TPR-1283
				final String performSearch = performSearchAndGetResultsPage(categoryCode, searchQuery, pageNo, showMode, sortCode,
						model, request, response, "", category, isBrand);


				final List<ProductData> normalProductDatas = searchPageData.getResults();
				model.addAttribute("normalProducts", normalProductDatas);

				//Set department hierarchy

				if (CollectionUtils.isNotEmpty(normalProductDatas))
				{
					model.addAttribute(ModelAttributetConstants.DEPARTMENT_HIERARCHY_DATA, searchPageData.getDepartmentHierarchyData());
					model.addAttribute(ModelAttributetConstants.DEPARTMENTS, searchPageData.getDepartments());
					model.addAttribute(ModelAttributetConstants.CURRENT_QUERY, searchPageData.getCurrentQuery().getQuery().getValue());
				}

				final String categoryName = category.getName();


				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, categoryName, false));
				model.addAttribute("categoryName", categoryName);

				populateModel(model, searchPageData, ShowMode.Page);
				model.addAttribute(ModelAttributetConstants.SHOW_CATEGORIES_ONLY, Boolean.FALSE);
				// TPR-1282: For Category Footer
				if (null != category.getCategoryFooterText())
				{
					model.addAttribute(CATEGORY_FOOTER_TEXT, category.getCategoryFooterText());
				}

				/* Added for TPR-1283 --Starts */
				String catName = "";

				//PRDI-547 fix
				catName = getCategoryModelUrlResolver().resolve(category);

				final String newcatName = catName.substring(1, catName.lastIndexOf('/'));
				model.addAttribute(CAT_NAME_KEY, newcatName);
				model.addAttribute(CAT_CODE_KEY, categoryCode.toLowerCase());
				//model.addAttribute("brand", Boolean.valueOf(true));//SonarFix
				model.addAttribute(BRANDNAME, Boolean.TRUE);
				/* Added for TPR-1283 --Ends */

				//update seo details
				final CategoryModel brandModel = categoryService.getCategoryForCode(brandCode);
				//meta tags are modified for TPR-1283
				String metaKeywords = null;
				String metaDescription = null;
				String metaTitle = null;

				//EQA Review Comments added
				if (brandModel != null)
				{
					final String brandName = brandModel.getName();

					final String cateName = category.getName();

					metaKeywords = brandName + " " + cateName + ", " + brandName + " " + cateName + ONLINE_KEY + "," + BUY_KEY
							+ brandName + " " + cateName + "," + BUY_KEY + brandName + " " + cateName + ONLINE_KEY;
					//EQA Review Comments added
					metaDescription = brandName + " " + cateName + ModelAttributetConstants.DESCRIPTION_TEXT1 + brandName + " "
							+ cateName + ModelAttributetConstants.DESCRIPTION_TEXT2 + cateName + " by " + brandName
							+ ModelAttributetConstants.DESCRIPTION_TEXT3;

					//EQA Review Comments added
					metaTitle = brandName + " " + cateName + ModelAttributetConstants.TITLE_TEXT1 + brandName + " " + cateName
							+ ModelAttributetConstants.TITLE_TEXT2;

					updatePageTitle(model, metaTitle);
					setUpMetaData(model, metaKeywords, metaDescription);

					//update seo details
					populateModel(model, searchPageData, ShowMode.Page);

					/* TPR-1283 CHANGES --Starts */
					//Added for heading change of the PLP of brand facet
					String supercatcode = categoryCode;
					if (!commonUtils.isLuxurySite())
					{
						supercatcode = categoryCode.substring(0, 5);
					}
					if (categoryCode != supercatcode)
					{
						final String header = brandName + " " + cateName;
						//model.addAttribute("flag", Boolean.valueOf(true));//SonarFix
						model.addAttribute("flag", Boolean.TRUE);
						model.addAttribute("modified_header", header);
						model.addAttribute("cateName", cateName);
					}

					if (categoryCode.equals(supercatcode))
					{
						response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
						final String categName = category.getName();
						response.setHeader(LOCATION, "/" + categName + SLASH_C + categoryCode.toLowerCase());
					}
					//set empty for TPR-1283
					model.addAttribute("otherProducts", "");
					/* TPR-1283 CHANGES --Ends */
					return performSearch;
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
				LOG.error(EXCEPTION_OCCURED + e1);
			}
		}

		return getViewForPage(model);
	}


	/**
	 * INC144317957 CHANGES
	 * 
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
	 */
	@RequestMapping(value =
	{ NEW_BRAND_URL_PATTERN_PAGINATION }, method = RequestMethod.GET)
	public String brandPagination(@PathVariable(CATERGORYCODE) String categoryCode, @PathVariable("brandCode") String brandCode,
			@RequestParam(value = "q", required = false) String searchQuery, @PathVariable("page") int pageNo,
			@RequestParam(value = SHOW, defaultValue = PAGEVAl) final ShowMode showMode,
			@RequestParam(value = SORT, required = false) final String sortCode,
			@RequestParam(value = PAGE_SIZE_KEY, required = false) final Integer pageSize,
			@RequestParam(value = SEARCH_CATEGORY_KEY, required = false) String dropDownText,
			@RequestParam(value = RESET_ALL_KEY, required = false) final boolean resetAll, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
	{
		//EQA review comments added
		try
		{
			final boolean isBrand = true;
			if (request.getServletPath().contains("&"))
			{
				request.getServletPath().replace("&", "-").replaceAll(SPACE_CHARACTERS, "").toLowerCase();
			}
			else
			{
				request.getServletPath().replaceAll(SPACE_CHARACTERS, "-").toLowerCase();
			}
			categoryCode = categoryCode.toUpperCase();
			brandCode = brandCode.toUpperCase();
			if (StringUtils.isNotEmpty(brandCode))
			{
				searchQuery = ":relevance:brand:" + brandCode;
			}
			String searchCode = new String(categoryCode);
			//SEO: New pagination detection TISCR 340
			pageNo = getPaginatedPageNo(request);
			//applying search filters
			if (searchQuery != null)
			{
				getfilterListCountForSize(searchQuery);
				model.addAttribute(ModelAttributetConstants.SIZE_COUNT, Integer.valueOf(getfilterListCountForSize(searchQuery)));
				model.addAttribute(ModelAttributetConstants.SEARCH_QUERY_VALUE, searchQuery);
			}
			//TISPRD-2315(checking whether the link has been clicked for pagination)
			if (checkIfPagination(request) && searchQuery == null)
			{
				searchQuery = RELEVANCE;
			}

			// Get page facets to include in facet field exclude tag
			final String pageFacets = request.getParameter(PAGE_FACET_DATA);

			//Storing the user preferred search results count
			updateUserPreferences(pageSize);

			if (!commonUtils.isLuxurySite() && StringUtils.isNotEmpty(searchCode)
					&& !(searchCode.substring(0, 5).equals(categoryCode))
					&& (categoryCode.startsWith(MplConstants.SALES_HIERARCHY_ROOT_CATEGORY_CODE)))
			{
				searchCode = searchCode.substring(0, 5);

			}
			model.addAttribute(ModelAttributetConstants.SEARCH_CODE, searchCode);
			model.addAttribute(ModelAttributetConstants.IS_CATEGORY_PAGE, Boolean.TRUE);
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
				model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, dropDownText);

			}
			else
			{
				final String categoryName = (category == null) ? "" : category.getName();
				model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, categoryName);
			}
			updateUserPreferences(pageSize);

			if (category != null)
			{

				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
						categoryCode, searchQuery, pageNo, showMode, sortCode, getSearchPageSize(), resetAll, pageFacets);

				//method signature modified for TPR-1283
				final String performSearch = performSearchAndGetResultsPage(categoryCode, searchQuery, pageNo, showMode, sortCode,
						model, request, response, "", category, isBrand);


				final List<ProductData> normalProductDatas = searchPageData.getResults();
				model.addAttribute("normalProducts", normalProductDatas);

				//Set department hierarchy

				if (CollectionUtils.isNotEmpty(normalProductDatas))
				{
					model.addAttribute(ModelAttributetConstants.DEPARTMENT_HIERARCHY_DATA, searchPageData.getDepartmentHierarchyData());
					model.addAttribute(ModelAttributetConstants.DEPARTMENTS, searchPageData.getDepartments());
					model.addAttribute(ModelAttributetConstants.CURRENT_QUERY, searchPageData.getCurrentQuery().getQuery().getValue());
				}

				final String categoryName = category.getName();


				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, categoryName, false));
				model.addAttribute("categoryName", categoryName);

				populateModel(model, searchPageData, ShowMode.Page);
				model.addAttribute(ModelAttributetConstants.SHOW_CATEGORIES_ONLY, Boolean.FALSE);
				// TPR-1282: For Category Footer
				if (null != category.getCategoryFooterText())
				{
					model.addAttribute(CATEGORY_FOOTER_TEXT, category.getCategoryFooterText());
				}

				/* Added for TPR-1283 --Starts */
				String catName = "";
				//PRDI-547 fix
				catName = getCategoryModelUrlResolver().resolve(category);

				final String newcatName = catName.substring(1, catName.lastIndexOf('/'));
				model.addAttribute(CAT_NAME_KEY, newcatName);
				model.addAttribute(CAT_CODE_KEY, categoryCode.toLowerCase());
				//model.addAttribute("brand", Boolean.valueOf(true));//SonarFix
				model.addAttribute(BRANDNAME, Boolean.TRUE);
				/* Added for TPR-1283 --Ends */

				//update seo details
				final CategoryModel brandModel = categoryService.getCategoryForCode(brandCode);
				//meta tags are modified for TPR-1283
				String metaKeywords = null;
				String metaDescription = null;
				String metaTitle = null;

				//EQA Review Comments added
				if (brandModel != null)
				{
					final String brandName = brandModel.getName();

					final String cateName = category.getName();

					metaKeywords = brandName + " " + cateName + ", " + brandName + " " + cateName + ONLINE_KEY + "," + BUY_KEY
							+ brandName + " " + cateName + "," + BUY_KEY + brandName + " " + cateName + ONLINE_KEY;
					//EQA Review Comments added
					metaDescription = brandName + " " + cateName + ModelAttributetConstants.DESCRIPTION_TEXT1 + brandName + " "
							+ cateName + ModelAttributetConstants.DESCRIPTION_TEXT2 + cateName + " by " + brandName
							+ ModelAttributetConstants.DESCRIPTION_TEXT3;

					//EQA Review Comments added
					metaTitle = brandName + " " + cateName + ModelAttributetConstants.TITLE_TEXT1 + brandName + " " + cateName
							+ ModelAttributetConstants.TITLE_TEXT2;

					updatePageTitle(model, metaTitle);
					setUpMetaData(model, metaKeywords, metaDescription);

					//update seo details
					populateModel(model, searchPageData, ShowMode.Page);

					/* TPR-1283 CHANGES --Starts */
					//Added for heading change of the PLP of brand facet
					final String supercatcode = categoryCode.substring(0, 5);
					if (categoryCode != supercatcode)
					{
						final String header = brandName + " " + cateName;
						//model.addAttribute("flag", Boolean.valueOf(true));//SonarFix
						model.addAttribute("flag", Boolean.TRUE);
						model.addAttribute("modified_header", header);
						model.addAttribute("cateName", cateName);
					}

					if (categoryCode.equals(supercatcode))
					{
						response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
						final String categName = category.getName();
						response.setHeader(LOCATION, "/" + categName + SLASH_C + categoryCode.toLowerCase());
					}
					//set empty for TPR-1283
					model.addAttribute("otherProducts", "");
					/* TPR-1283 CHANGES --Ends */
					return performSearch;
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
				LOG.error(EXCEPTION_OCCURED + e1);
			}
		}

		return getViewForPage(model);
	}

	/**
	 * @desc Method for category landing pages for AJAX call : TPR-198
	 * @param categoryCode
	 * @param searchQuery
	 * @param resetAll
	 * @param showMode
	 * @param sortCode
	 * @param pageSize
	 * @param dropDownText
	 * @param model
	 * @param request
	 * @param response
	 * @return String
	 */

	@RequestMapping(value = NEW_CATEGORY_URL_PATTERN + "/getFacetData", method = RequestMethod.GET)
	public String getFacetData(@PathVariable("categoryCode") String categoryCode,
			@RequestParam(value = "q", required = false) String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") int pageNo,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = PAGE_SIZE_KEY, required = false) final Integer pageSize,
			@RequestParam(value = SEARCH_CATEGORY_KEY, required = false) String dropDownText,
			@RequestParam(value = RESET_ALL_KEY, required = false) final boolean resetAll, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
	{
		//EQA review comments added
		try
		{
			categoryCode = categoryCode.toUpperCase();
			String searchCode = new String(categoryCode);
			//SEO: New pagination detection TISCR 340
			pageNo = getPaginatedPageNo(request);

			//CKD:TPR-250 :Start
			if (null != searchQuery && searchQuery.contains(MarketplacecommerceservicesConstants.SELLERIDSEARCH))
			{
				String sellerId = null;
				String sellerName = null;
				try
				{
					sellerId = searchQuery.split(MarketplacecommerceservicesConstants.SELLERIDSEARCH, 2)[1].substring(0, 6);
					sellerName = mplCategoryFacade.getSellerInformationBySellerID(sellerId);
				}
				catch (final Exception ex)
				{
					LOG.error("CategoryPage-Problem retrieving microsite SellerId / Sellername for left hand facets >>>>>", ex);
				}
				model.addAttribute("msiteSellerId", sellerId);
				model.addAttribute("mSellerID", sellerId);
				model.addAttribute("mSellerName", sellerName);
			}
			//CKD:TPR-250: End


			//applying search filters
			if (searchQuery != null)
			{
				getfilterListCountForSize(searchQuery);
				model.addAttribute(ModelAttributetConstants.SIZE_COUNT, Integer.valueOf(getfilterListCountForSize(searchQuery)));
				model.addAttribute(ModelAttributetConstants.SEARCH_QUERY_VALUE, searchQuery);
			}
			//TISPRD-2315(checking whether the link has been clicked for pagination)
			if (checkIfPagination(request) && searchQuery == null)
			{
				searchQuery = RELEVANCE;
			}

			// Get page facets to include in facet field exclude tag
			final String pageFacets = request.getParameter(PAGE_FACET_DATA);

			//Storing the user preferred search results count
			updateUserPreferences(pageSize);

			if (!commonUtils.isLuxurySite() && StringUtils.isNotEmpty(searchCode)
					&& !(searchCode.substring(0, 5).equals(categoryCode))
					&& (categoryCode.startsWith(MplConstants.SALES_HIERARCHY_ROOT_CATEGORY_CODE)))
			{
				searchCode = searchCode.substring(0, 5);

			}
			model.addAttribute(ModelAttributetConstants.SEARCH_CODE, searchCode);
			model.addAttribute(ModelAttributetConstants.IS_CATEGORY_PAGE, Boolean.TRUE);

			final CategoryModel category = categoryService.getCategoryForCode(categoryCode);
			/* Added for TPR-1283 --Starts */
			String catName = "";
			//PRDI-547 fix
			catName = getCategoryModelUrlResolver().resolve(category);

			final String newcatName = catName.substring(1, catName.lastIndexOf('/'));
			model.addAttribute(CAT_NAME_KEY, newcatName);
			model.addAttribute(CAT_CODE_KEY, categoryCode.toLowerCase());
			/* Added for TPR-1283 --Ends */
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
				model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, dropDownText);

			}
			else
			{
				final String categoryName = (category == null) ? "" : category.getName();
				model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, categoryName);
			}
			int count = getSearchPageSize();
			//Check if there is a landing page for the category
			final UserPreferencesData preferencesData = updateUserPreferences(pageSize);
			if (preferencesData != null && preferencesData.getPageSize() != null)
			{
				count = preferencesData.getPageSize().intValue();
			}

			if (category != null)
			{

				final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
						categoryCode, searchQuery, pageNo, showMode, sortCode, count, resetAll, pageFacets);

				final List<ProductData> normalProductDatas = searchPageData.getResults();
				//Set department hierarchy

				if (CollectionUtils.isNotEmpty(normalProductDatas))
				{
					model.addAttribute(ModelAttributetConstants.DEPARTMENT_HIERARCHY_DATA, searchPageData.getDepartmentHierarchyData());
					model.addAttribute(ModelAttributetConstants.DEPARTMENTS, searchPageData.getDepartments());
					model.addAttribute(ModelAttributetConstants.CURRENT_QUERY, searchPageData.getCurrentQuery().getQuery().getValue());
				}

				final String categoryName = category.getName();


				model.addAttribute(WebConstants.BREADCRUMBS_KEY,
						getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, categoryName, false));
				populateModel(model, searchPageData, ShowMode.Page);
				model.addAttribute(ModelAttributetConstants.NORMAL_PRODUCTS, normalProductDatas);
				model.addAttribute(ModelAttributetConstants.SHOW_CATEGORIES_ONLY, Boolean.FALSE);
				// TPR-1282: For Category Footer
				if (null != category.getCategoryFooterText())
				{
					model.addAttribute(CATEGORY_FOOTER_TEXT, category.getCategoryFooterText());
				}

			}
		}

		//TISPRD-5986  MSH category 404 error handling
		catch (final UnknownIdentifierException ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(ex,
					MarketplacecommerceservicesConstants.E0023));
			//LOG.error(EXCEPTION_OCCURED + ex + "  Category code: " + categoryCode + " not found!");
			try
			{
				return frontEndErrorHelper.callNonBusinessError(model, ex.getMessage());
			}
			catch (final CMSItemNotFoundException e1)
			{
				LOG.error(EXCEPTION_OCCURED + e1);
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
				LOG.error(EXCEPTION_OCCURED + e1);
			}
		}

		return ControllerConstants.Views.Fragments.Product.SearchResultsPanel;
	}

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
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value =
	{ NEW_CATEGORY_URL_PATTERN }, method = RequestMethod.GET)
	public String category(@PathVariable(CATERGORYCODE) String categoryCode,
			@RequestParam(value = "q", required = false) String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") int pageNo,
			@RequestParam(value = SHOW, defaultValue = PAGEVAl) final ShowMode showMode,
			@RequestParam(value = SORT, required = false) final String sortCode,
			@RequestParam(value = PAGE_SIZE_KEY, required = false) Integer pageSize,
			@RequestParam(value = SEARCH_CATEGORY_KEY, required = false) String dropDownText,
			@RequestParam(value = RESET_ALL_KEY, required = false) final boolean resetAll,
			@RequestParam(value = LAZY_INTERFACE_KEY, required = false) final String lazyInterface, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
	{
		String returnStatement = null;
		//EQA review comments added
		try
		{
			final boolean isBrand = false;
			//UF-15
			pageSize = PAGE_SIZE;
			categoryCode = categoryCode.toUpperCase();

			//CKD:TPR-250-Start
			identifyMicroSellerId(searchQuery, model, request);
			//CKD:TPR-250-End
			if (!redirectIfLuxuryCategory(categoryCode, response))
			{
				String searchCode = new String(categoryCode);
				//SEO: New pagination detection TISCR 340
				pageNo = getPaginatedPageNo(request);
				//applying search filters
				/* TPR-1283 Changes --Starts */
				final CategoryModel category = categoryService.getCategoryForCode(categoryCode);
				final String urlName = getCategoryModelUrlResolver().resolve(category);
				final String resolvedcatName = urlName.substring(1, urlName.lastIndexOf('/'));

				/* TPR-1283 Changes --Ends */
				//applying search filters
				if (searchQuery != null)
				{
					getfilterListCountForSize(searchQuery);
					model.addAttribute(ModelAttributetConstants.SIZE_COUNT, Integer.valueOf(getfilterListCountForSize(searchQuery)));
					model.addAttribute(ModelAttributetConstants.SEARCH_QUERY_VALUE, searchQuery);
				}
				/* PRDI-411 FIX--Start */
				boolean isRedirectRequired = true;

				final Enumeration<String> enums = request.getParameterNames();

				while (enums.hasMoreElements())
				{
					final String paramKey = enums.nextElement();
					if (paramKey.contains(ModelAttributetConstants.ICID))
					{
						isRedirectRequired = false;
						break;
					}
					else if (paramKey.equalsIgnoreCase(ModelAttributetConstants.SHARE))
					{
						isRedirectRequired = false;
						break;
					}
				}
				/* PRDI-411 FIX--End */

				/* TPR-1283 changes --Starts */
				if (StringUtils.isNotEmpty(searchQuery) && searchQuery.contains(BRANDNAME) && isRedirectRequired)
				{
					final Iterable<String> splitStr = Splitter.on(':').split(searchQuery);
					//final int count = Integer.valueOf(Iterables.frequency(splitStr, "brand")).intValue();//SonarFix
					final int count = Iterables.frequency(splitStr, BRANDNAME);
					if (count == 1)
					{
						String brandCode = "";
						int cnt = 0;
						final String[] tokens = searchQuery.split(":");
						for (final String token : tokens)
						{
							if (cnt == 1)
							{
								brandCode = token;
								break;
							}
							if (token.equals(BRANDNAME))
							{
								cnt = 1;
							}
						}
						final String brand = categoryService.getCategoryForCode(brandCode).getName().toLowerCase();
						//brand.toLowerCase();
						String brandName = "";


						if (brand.contains("&"))
						{
							brandName = URLDecoder.decode(brand, "UTF-8").replace("&", "-").replaceAll(SPACE_CHARACTERS, "");
						}
						else
						{
							brandName = URLDecoder.decode(brand, "UTF-8").replaceAll(SPACE_CHARACTERS, "-");
						}

						response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
						final String appenedName = resolvedcatName + ("-") + brandName;
						response.setHeader(LOCATION,
								"/" + appenedName + SLASH_C + categoryCode.toLowerCase() + "/b-" + brandCode.toLowerCase());
						return null;
					}
				}
				model.addAttribute(CAT_NAME_KEY, resolvedcatName);
				model.addAttribute(CAT_CODE_KEY, categoryCode.toLowerCase());
				/* TPR-1283 changes --Ends */

				// Get page facets to include in facet field exclude tag
				final String pageFacets = request.getParameter(PAGE_FACET_DATA);
				//Storing the user preferred search results count
				final UserPreferencesData preferencesData = updateUserPreferences(pageSize); // CAR-236 and CAR-238(1) the page size is only fetched here and used later Codereview Point # 3 , line # 380 & 450 & 510
				int count = getSearchPageSize();
				if (preferencesData != null && preferencesData.getPageSize() != null)
				{
					count = preferencesData.getPageSize().intValue();
				} // End Change for // CAR-236

				//final List<ProductModel> heroProducts = new ArrayList<ProductModel>();
				if (!commonUtils.isLuxurySite() && StringUtils.isNotEmpty(searchCode)
						&& !(searchCode.substring(0, 5).equals(categoryCode))
						&& (categoryCode.startsWith(MplConstants.SALES_HIERARCHY_ROOT_CATEGORY_CODE)))
				{
					searchCode = searchCode.substring(0, 5);
				}
				model.addAttribute(ModelAttributetConstants.SEARCH_CODE, searchCode);
				model.addAttribute(ModelAttributetConstants.IS_CATEGORY_PAGE, Boolean.TRUE);

				//CategoryModel category = null;

				try
				{
					boolean isPlpPresentInLanding = false;
					boolean isCustomSkuPresent = false;
					//category = categoryService.getCategoryForCode(categoryCode);
					final ContentPageModel categoryLandingPage = getLandingPageForCategory(category); // CAR-237 moved here for called only Once rather  line # 409 , 469 & 1053 available Code review pt#4
					//PRDI-802 : check for availability of ProductGridComponent in the page
					ContentSlotData contentSlotData = null;
					//SDI-1078 : checking in all slots for PLP accross the templates
					for (final String slotPosition : PRODUCT_GRID_COMPONENT_POSITION_LIST)
					{
						//To break out from outer loop
						if (isPlpPresentInLanding)
						{
							break;
						}
						if (categoryLandingPage != null)
						{
							contentSlotData = mplCmsPageService.getContentSlotForPage(categoryLandingPage, slotPosition);
							if (contentSlotData != null && CollectionUtils.isNotEmpty(contentSlotData.getCMSComponents()))
							{
								for (final AbstractCMSComponentModel component : contentSlotData.getCMSComponents())
								{
									if (component instanceof ProductGridComponentModel)
									{
										isPlpPresentInLanding = true;
										break;
									}
									else
									{
										LOG.debug("#### It is not a ProductGridComponent in position " + slotPosition);
									}
								}
							}
						}

					}

					/* CAR-242 Moved here for calling once */
					ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
					//PRDI-802 : check for availability of CustomSkuComponent in the page
					if (categoryLandingPage != null)
					{
						contentSlotData = mplCmsPageService.getContentSlotForPage(categoryLandingPage, CUSTOM_SKU_COMPONENT_POSITION);
					}

					if (contentSlotData != null && CollectionUtils.isNotEmpty(contentSlotData.getCMSComponents()))
					{
						for (final AbstractCMSComponentModel component : contentSlotData.getCMSComponents())
						{
							if (component instanceof CustomSkuComponentModel)
							{
								isCustomSkuPresent = true;
								final CustomSkuComponentModel customSku = (CustomSkuComponentModel) component;
								searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearchForCustomSku(
										customSku.getLabelOrId(), searchQuery, pageNo, showMode, sortCode, count, pageFacets);
							}
							else
							{
								LOG.debug("#### It is not a CustomSkuComponent");
							}
						}
					}
					//SEO
					this.getSEOContents(category, model, categoryLandingPage);


					if (!isCustomSkuPresent && isPlpPresentInLanding)
					{
						//TISPRD-2315(checking whether the link has been clicked for pagination)
						if (checkIfPagination(request) && searchQuery == null)
						{
							searchQuery = RELEVANCE;
						}
						searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
								categoryCode, searchQuery, pageNo, showMode, sortCode, count, resetAll, pageFacets);
					}
					final JSONArray priorityBrandsJsonArray = new JSONArray();
					final JSONObject priorityBrand = new JSONObject();

					LOG.info("*****category code******" + categoryCode);
					final List<PriorityBrandsModel> priorityBrands = brandService.priorityBrands(categoryCode);

					if (CollectionUtils.isNotEmpty(priorityBrands))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("priority brands size" + priorityBrands.size());
						}
						for (final PriorityBrandsModel priorityBrandsModel : priorityBrands)
						{
							priorityBrandsJsonArray.add(priorityBrandsModel.getBrandId());
						}
					}
					priorityBrand.put("priorityBrands", priorityBrandsJsonArray);

					model.addAttribute("PriorityBrandArray", priorityBrand.toJSONString());

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
						model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, dropDownText);
					}
					else
					{
						final String categoryName = (category == null) ? "" : category.getName();
						model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, categoryName);
					}
					//int count = getSearchPageSize();  //moved up
					//Check if there is a landing page for the category

					//final UserPreferencesData preferencesData = updateUserPreferences(pageSize); // CAR-236 redefined at the top line # 380 for review comment Point # 3
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
							response.setHeader(LOCATION, redirection);
							return null;
						}

						//final ContentPageModel categoryLandingPage = getLandingPageForCategory(category); // CAR-237 called above at line 392 once. doing the same logic to throw pt # 4
						if (categoryLandingPage == null)
						{
							//throw new CMSItemNotFoundException("Could not find a landing page for the category" + category.getName());
							//changes for CAR-280
							throw new CMSItemNotFoundException("Category Landing page is not configured for PLP" + category.getName());
						}

						if (isPlpPresentInLanding)
						{
							final List<ProductData> normalProductDatas = searchPageData.getResults();
							//Set department hierarchy
							//if (normalProductDatas.size() > 0)
							if (CollectionUtils.isNotEmpty(normalProductDatas))
							{
								model.addAttribute(ModelAttributetConstants.DEPARTMENT_HIERARCHY_DATA,
										searchPageData.getDepartmentHierarchyData());
								model.addAttribute(ModelAttributetConstants.DEPARTMENTS, searchPageData.getDepartments());
								model.addAttribute(ModelAttributetConstants.CURRENT_QUERY, searchPageData.getCurrentQuery().getQuery()
										.getValue());
							}
							model.addAttribute(ModelAttributetConstants.NORMAL_PRODUCTS, normalProductDatas);
							populateModel(model, searchPageData, ShowMode.Page);

						}

						final String categoryName = category.getName();
						//TPR-243
						//setUpMetaDataForContentPage(model, categoryLandingPage);

						model.addAttribute(ModelAttributetConstants.PRODUCT_CATEGORY, categoryName.replaceAll(SPECIAL_CHARACTERS, "")
								.replaceAll(" ", "_").toLowerCase());
						model.addAttribute(WebConstants.BREADCRUMBS_KEY,
								getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, categoryName, false));
						model.addAttribute(ModelAttributetConstants.SHOW_CATEGORIES_ONLY, Boolean.FALSE);
						storeCmsPageInModel(model, categoryLandingPage);
					}
					returnStatement = getViewForPage(model);
				}
				catch (final CMSItemNotFoundException exp)

				{

					LOG.error("************** category method exception " + exp.getMessage());
					//TISPRD-2315(checking whether the link has been clicked for pagination)
					if (checkIfPagination(request) && searchQuery == null)
					{
						searchQuery = RELEVANCE;
					}
					//ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exp,
					//MarketplacecommerceservicesConstants.E0000));

					try
					{
						// final UserPreferencesData preferencesData = updateUserPreferences(pageSize); // CAR-236 redefined at the top line # 380 for review comment Point # 3
						if (preferencesData != null && preferencesData.getPageSize() != null)
						{
							count = preferencesData.getPageSize().intValue();
							setPageSiseCount(count);
						}
						//method signature modified for TPR-1283
						final String performSearch = performSearchAndGetResultsPage(categoryCode, searchQuery, pageNo, showMode,
								sortCode, model, request, response, pageFacets, category, isBrand);

						//UF-15
						if (null != lazyInterface && lazyInterface.equals("Y"))
						{
							model.addAttribute(LAZY_INTERFACE_KEY, Boolean.TRUE);
						}
						else
						{
							model.addAttribute(LAZY_INTERFACE_KEY, Boolean.FALSE);
						}
						return performSearch;
					}
					catch (final Exception exception)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception,
								MarketplacecommerceservicesConstants.E0000));
						try
						{
							return frontEndErrorHelper.callNonBusinessError(model, exp.getMessage());
						}
						catch (final CMSItemNotFoundException e1)
						{
							LOG.error(EXCEPTION_OCCURED + e1);
						}
					}
				}

				//TISPRD-5986  MSH category 404 error handling
				catch (final UnknownIdentifierException ex)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(ex,
							MarketplacecommerceservicesConstants.E0023));
					//LOG.error(EXCEPTION_OCCURED + ex + "  Category code: " + categoryCode + " not found!");
					try
					{
						return frontEndErrorHelper.callNonBusinessError(model, ex.getMessage());
					}
					catch (final CMSItemNotFoundException e1)
					{
						LOG.error(EXCEPTION_OCCURED + e1);
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
						LOG.error(EXCEPTION_OCCURED + e1);
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(EXCEPTION_OCCURED + e);
		}
		return returnStatement;
	}

	/**
	 * @desc Added for INC144317961 to eliminate unwanted url redirection
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
	 */

	@SuppressWarnings("boxing")
	@RequestMapping(value =
	{ NEW_CATEGORY_URL_PATTERN_PAGINATION }, method = RequestMethod.GET)
	public String categoryPagination(@PathVariable(CATERGORYCODE) String categoryCode,
			@RequestParam(value = "q", required = false) String searchQuery, @PathVariable(PAGE) int pageNo,
			@RequestParam(value = SHOW, defaultValue = PAGEVAl) final ShowMode showMode,
			@RequestParam(value = SORT, required = false) final String sortCode,
			@RequestParam(value = PAGE_SIZE_KEY, required = false) Integer pageSize,
			@RequestParam(value = SEARCH_CATEGORY_KEY, required = false) String dropDownText,
			@RequestParam(value = RESET_ALL_KEY, required = false) final boolean resetAll,
			@RequestParam(value = LAZY_INTERFACE_KEY, required = false) final String lazyInterface, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
	{
		String returnStatement = null;
		//EQA review comments added
		try
		{
			final boolean isBrand = false;
			//UF-15
			pageSize = PAGE_SIZE;
			categoryCode = categoryCode.toUpperCase();

			//CKD:TPR-250-Start
			identifyMicroSellerId(searchQuery, model, request);
			//CKD:TPR-250-End
			if (!redirectIfLuxuryCategory(categoryCode, response))
			{
				String searchCode = new String(categoryCode);
				//SEO: New pagination detection TISCR 340
				pageNo = getPaginatedPageNo(request);
				model.addAttribute("pageNo", pageNo);
				/* TPR-1283 Changes --Starts */
				final CategoryModel category = categoryService.getCategoryForCode(categoryCode);
				final String urlName = getCategoryModelUrlResolver().resolve(category);
				final String resolvedcatName = urlName.substring(1, urlName.lastIndexOf('/'));

				/* TPR-1283 Changes --Ends */
				//applying search filters
				if (searchQuery != null)
				{
					getfilterListCountForSize(searchQuery);
					model.addAttribute(ModelAttributetConstants.SIZE_COUNT, Integer.valueOf(getfilterListCountForSize(searchQuery)));
					model.addAttribute(ModelAttributetConstants.SEARCH_QUERY_VALUE, searchQuery);
				}

				model.addAttribute(CAT_NAME_KEY, resolvedcatName);
				model.addAttribute(CAT_CODE_KEY, categoryCode.toLowerCase());
				/* TPR-1283 changes --Ends */

				// Get page facets to include in facet field exclude tag
				final String pageFacets = request.getParameter(PAGE_FACET_DATA);
				//Storing the user preferred search results count
				final UserPreferencesData preferencesData = updateUserPreferences(pageSize); // CAR-236 and CAR-238(1) the page size is only fetched here and used later Codereview Point # 3 , line # 380 & 450 & 510
				int count = getSearchPageSize();
				if (preferencesData != null && preferencesData.getPageSize() != null)
				{
					count = preferencesData.getPageSize().intValue();
				} // End Change for // CAR-236

				if (!commonUtils.isLuxurySite() && StringUtils.isNotEmpty(searchCode)
						&& !(searchCode.substring(0, 5).equals(categoryCode))
						&& (categoryCode.startsWith(MplConstants.SALES_HIERARCHY_ROOT_CATEGORY_CODE)))
				{
					searchCode = searchCode.substring(0, 5);
				}
				model.addAttribute(ModelAttributetConstants.SEARCH_CODE, searchCode);
				model.addAttribute(ModelAttributetConstants.IS_CATEGORY_PAGE, Boolean.TRUE);
				try
				{
					boolean isCustomSkuPresent = false;
					final ContentPageModel categoryLandingPage = getLandingPageForCategory(category); // CAR-237 moved here for called only Once rather  line # 409 , 469 & 1053 available Code review pt#4

					//UF-265
					/* CAR-242 Moved here for calling once */
					ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
					//PRDI-802 : check for availability of CustomSkuComponent in the page
					ContentSlotData contentSlotData = null;
					if (categoryLandingPage != null)
					{
						contentSlotData = mplCmsPageService.getContentSlotForPage(categoryLandingPage, CUSTOM_SKU_COMPONENT_POSITION);
					}
					if (contentSlotData != null && CollectionUtils.isNotEmpty(contentSlotData.getCMSComponents()))
					{
						for (final AbstractCMSComponentModel component : contentSlotData.getCMSComponents())
						{
							if (component instanceof CustomSkuComponentModel)
							{
								isCustomSkuPresent = true;
								final CustomSkuComponentModel customSku = (CustomSkuComponentModel) component;
								searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearchForCustomSku(
										customSku.getLabelOrId(), searchQuery, pageNo, showMode, sortCode, count, pageFacets);
							}
							else
							{
								LOG.debug("#### It is not a CustomSkuComponent");
							}
						}
					}

					if (!isCustomSkuPresent)
					{
						//TISPRD-2315(checking whether the link has been clicked for pagination)
						if (checkIfPagination(request) && searchQuery == null)
						{
							searchQuery = RELEVANCE;
						}
						searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
								categoryCode, searchQuery, pageNo, showMode, sortCode, count, resetAll, pageFacets);
					}

					//SEO
					this.getSEOContents(category, model, categoryLandingPage);



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
						model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, dropDownText);
					}
					else
					{
						final String categoryName = (category == null) ? "" : category.getName();
						model.addAttribute(ModelAttributetConstants.DROP_DOWN_TEXT, categoryName);
					}
					//int count = getSearchPageSize();  //moved up
					//Check if there is a landing page for the category

					//final UserPreferencesData preferencesData = updateUserPreferences(pageSize); // CAR-236 redefined at the top line # 380 for review comment Point # 3
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
							response.setHeader(LOCATION, redirection);
							return null;
						}

						//final ContentPageModel categoryLandingPage = getLandingPageForCategory(category); // CAR-237 called above at line 392 once. doing the same logic to throw pt # 4
						if (categoryLandingPage == null)
						{
							//changes for CAR-280
							throw new CMSItemNotFoundException("Category Landing page is not configured for PLP" + category.getName());
						}

						final List<ProductData> normalProductDatas = searchPageData.getResults();
						//Set department hierarchy
						if (CollectionUtils.isNotEmpty(normalProductDatas))
						{
							model.addAttribute(ModelAttributetConstants.DEPARTMENT_HIERARCHY_DATA,
									searchPageData.getDepartmentHierarchyData());
							model.addAttribute(ModelAttributetConstants.DEPARTMENTS, searchPageData.getDepartments());
							model.addAttribute(ModelAttributetConstants.CURRENT_QUERY, searchPageData.getCurrentQuery().getQuery()
									.getValue());
						}

						final String categoryName = category.getName();
						//TPR-243
						//setUpMetaDataForContentPage(model, categoryLandingPage);

						model.addAttribute(ModelAttributetConstants.PRODUCT_CATEGORY, categoryName.replaceAll(SPECIAL_CHARACTERS, "")
								.replaceAll(" ", "_").toLowerCase());
						model.addAttribute(WebConstants.BREADCRUMBS_KEY,
								getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, categoryName, false));
						populateModel(model, searchPageData, ShowMode.Page);
						model.addAttribute(ModelAttributetConstants.NORMAL_PRODUCTS, normalProductDatas);
						model.addAttribute(ModelAttributetConstants.SHOW_CATEGORIES_ONLY, Boolean.FALSE);
						storeCmsPageInModel(model, categoryLandingPage);
					}
					returnStatement = getViewForPage(model);
				}
				catch (final CMSItemNotFoundException exp)

				{
					LOG.error("************** category method exception " + exp.getMessage());
					//ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exp,
					//MarketplacecommerceservicesConstants.E0000));
					if (checkIfPagination(request) && searchQuery == null)
					{
						searchQuery = RELEVANCE;
					}
					try
					{
						// final UserPreferencesData preferencesData = updateUserPreferences(pageSize); // CAR-236 redefined at the top line # 380 for review comment Point # 3
						if (preferencesData != null && preferencesData.getPageSize() != null)
						{
							count = preferencesData.getPageSize().intValue();
							setPageSiseCount(count);
						}
						//method signature modified for TPR-1283
						final String performSearch = performSearchAndGetResultsPage(categoryCode, searchQuery, pageNo, showMode,
								sortCode, model, request, response, pageFacets, category, isBrand);

						//UF-15
						if (null != lazyInterface && lazyInterface.equals("Y"))
						{
							model.addAttribute(LAZY_INTERFACE_KEY, Boolean.TRUE);
						}
						else
						{
							model.addAttribute(LAZY_INTERFACE_KEY, Boolean.FALSE);
						}
						return performSearch;
					}

					catch (final Exception exception)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception,
								MarketplacecommerceservicesConstants.E0000));
						try
						{
							return frontEndErrorHelper.callNonBusinessError(model, exp.getMessage());
						}
						catch (final CMSItemNotFoundException e1)
						{
							LOG.error(EXCEPTION_OCCURED + e1);
						}
					}
				}

				//TISPRD-5986  MSH category 404 error handling
				catch (final UnknownIdentifierException ex)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(ex,
							MarketplacecommerceservicesConstants.E0023));
					//LOG.error(EXCEPTION_OCCURED + ex + "  Category code: " + categoryCode + " not found!");
					try
					{
						return frontEndErrorHelper.callNonBusinessError(model, ex.getMessage());
					}
					catch (final CMSItemNotFoundException e1)
					{
						LOG.error(EXCEPTION_OCCURED + e1);
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
						LOG.error(EXCEPTION_OCCURED + e1);
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(EXCEPTION_OCCURED + e);
		}
		return returnStatement;
	}


	/**
	 * @param searchQuery
	 * @param model
	 * @param request
	 */
	private void identifyMicroSellerId(final String searchQuery, final Model model, final HttpServletRequest request)
	{
		final String requestURL = request.getRequestURL().toString();
		//TPR-4471
		//		String sellerName = null;
		String sellerId = null;
		if (requestURL.matches(MplConstants.MSITE_SLR_SLS_HIERARCHY_URL_PTRN_RGX))
		{
			try
			{
				sellerId = requestURL.split(MplConstants.MSITE_SLR_SLS_PTRN_PART1, 2)[1].substring(0, 6);
			}
			catch (final StringIndexOutOfBoundsException ex)
			{
				LOG.error("SellerId should not be less than 6 characters >>>>>", ex);
			}
			catch (final Exception ex)
			{
				LOG.error("Category Page-Problem retrieving microsite SellerId / Sellername from seller Sales Hierarchy URL >>>>>",
						ex);
			}
		}
		else if (null != searchQuery && searchQuery.contains(MarketplacecommerceservicesConstants.SELLERIDSEARCH))
		{
			try
			{
				sellerId = searchQuery.split(MarketplacecommerceservicesConstants.SELLERIDSEARCH, 2)[1].substring(0, 6);
			}
			catch (final StringIndexOutOfBoundsException ex)
			{
				LOG.error("SellerId should not be less than 6 characters >>>>>", ex);
			}
			catch (final Exception ex)
			{
				LOG.error("Category Page-Problem retrieving microsite SellerId / Sellername from category carousal URL >>>>>", ex);
			}
		}
		//				if (StringUtils.isNotBlank(sellerId))
		//				{
		//					sellerName = mplCategoryFacade.getSellerInformationBySellerID(sellerId);
		//				}

		model.addAttribute("msiteSellerId", sellerId);
		model.addAttribute("mSellerID", sellerId);
		//				model.addAttribute("mSellerName", sellerName);

	}

	/**
	 * @param categoryCode
	 * @param response
	 */
	private boolean redirectIfLuxuryCategory(final String categoryCode, final HttpServletResponse response)
	{
		boolean redirect = false;

		// Return false if the current is LUXURY. No Third party redirect required is the category starts with LSH while customer is in with Lux site.
		if (isLuxurySite())
		{
			return redirect;
		}

		if (categoryCode.startsWith(LSH))
		{
			redirect = true;
			LOG.debug("**********The category is a luxury category.Hence redirecting to luxury website***********" + categoryCode);
			final String luxuryHost = configurationService.getConfiguration().getString("luxury.resource.host");
			final String luxuryCategoryUrl = luxuryHost + SLASH_C + categoryCode.toLowerCase();
			LOG.debug("Redirecting to ::::::" + luxuryCategoryUrl);
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader(LOCATION, luxuryCategoryUrl);
		}
		return redirect;
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
		//TPR-430
		if (breadcrumbs.size() > 0)
		{
			model.addAttribute(ModelAttributetConstants.PRODUCT_CATEGORY,
					breadcrumbs.get(0).getName().replaceAll(SPECIAL_CHARACTERS, "").replaceAll(" ", "_").toLowerCase());
		}
		if (breadcrumbs.size() > 1)
		{
			model.addAttribute(ModelAttributetConstants.PAGE_SUBCATEGORY_NAME,
					breadcrumbs.get(1).getName().replaceAll(SPECIAL_CHARACTERS, "").replaceAll(" ", "_").toLowerCase());
		}
		if (breadcrumbs.size() > 2)
		{
			model.addAttribute(ModelAttributetConstants.PAGE_SUBCATEGORY_NAME_L3,
					breadcrumbs.get(2).getName().replaceAll(SPECIAL_CHARACTERS, "").replaceAll(" ", "_").toLowerCase());
		}
	}

	/**
	 * @param category
	 * @return ContentPageModel
	 * @throws CMSItemNotFoundException
	 */
	private ContentPageModel getLandingPageForCategory(final CategoryModel category) //throws CMSItemNotFoundException
	{
		// CAR-240 All changes done as per this CAR
		ContentPageModel landingPage = null;

		try
		{
			landingPage = mplCmsPageService.getLandingPageForCategory(category);
		}
		catch (final Exception e) //will catch CMSItemNotFoundException or any other exception
		{
			LOG.error("Error in  getLandingPageForCategory() method" + e);
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
	public FacetRefinement<SearchStateData> getFacets(@PathVariable(CATERGORYCODE) String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") final int pageNum,
			@RequestParam(value = SHOW, defaultValue = PAGEVAl) final ShowMode showMode,
			@RequestParam(value = SORT, required = false) final String sortCode) throws UnsupportedEncodingException
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
	public SearchResultsData<ProductData> getResults(@PathVariable(CATERGORYCODE) String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = PAGE, defaultValue = "0") final int pgNum,
			@RequestParam(value = SHOW, defaultValue = PAGEVAl) final ShowMode showMode,
			@RequestParam(value = SORT, required = false) final String sortCode) throws UnsupportedEncodingException
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
			final boolean resetAll, final String pageFacets)
	{
		final PageableData pageableData = createPageableData(pgNo, pageSize, sortCode, showMode);
		pageableData.setPageFacets(pageFacets);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = productSearchFacade
				.categorySearch(categoryCode, searchState, pageableData);
		searchPageData = updatePageData(searchPageData, categoryCode, searchQuery);
		return searchPageData;
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
	protected ProductSearchPageData<SearchStateData, ProductData> performSearchForCustomSku(final String lookId,
			final String searchQuery, final int page, final ShowMode showMode, final String sortCode, final int pageSize,
			final String pageFacets)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
		pageableData.setPageFacets(pageFacets);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		return searchFacade.collectionSearch(lookId, searchState, pageableData);
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



	//method signature modified for TPR-1283
	protected String performSearchAndGetResultsPage(final String categoryCode, final String searchQuery, final int pgNumbers,
			final ShowMode showMode, final String sortCode, final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final String pageFacets, final CategoryModel category, final boolean isBrand)
			throws UnsupportedEncodingException
	{
		//final CategoryModel category = getCommerceCategoryService().getCategoryForCode(categoryCode); // commented as its sent as method parameter

		/* Changes made for TISLUX-91 starts */
		//condition added for TPR-1283
		if (!isBrand)
		{
			final String redirection = checkRequestUrl(request, response, getCategoryModelUrlResolver().resolve(category));
			if (StringUtils.isNotEmpty(redirection))
			{
				return redirection;

			}
		}

		final CategoryPageModel categoryPage = getCategoryPage(category);

		final CategorySearchEvaluator categorySearch = new CategorySearchEvaluator(categoryCode, XSSFilterUtil.filter(searchQuery),
				pgNumbers, showMode, sortCode, categoryPage, pageFacets);
		categorySearch.doSearch();

		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = categorySearch
				.getSearchPageData();
		if (searchPageData != null)
		{
			model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
			model.addAttribute("departments", searchPageData.getDepartments());
			model.addAttribute("currentQuery", searchPageData.getCurrentQuery().getQuery().getValue());
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
		// TPR-1282: For Category Footer
		if (null != category.getCategoryFooterText())
		{
			model.addAttribute(CATEGORY_FOOTER_TEXT, category.getCategoryFooterText());
		}


		updatePageTitle(category, searchPageData.getBreadcrumbs(), model);
		if (CollectionUtils.isNotEmpty(searchPageData.getResults()))
		{
			model.addAttribute("departmentHierarchyData", searchPageData.getDepartmentHierarchyData());
		}

		final RequestContextData requestContextData = getRequestContextData(request);
		requestContextData.setCategory(category);
		requestContextData.setSearch(searchPageData);
		/* TISPRD-2987 */
		// if (searchQuery != null && checkIfPagination(request) && sortCode == null)
		// {
		// 	model.addAttribute("metaRobots", "index,follow");
		// }
		if (searchQuery != null)
		{
			model.addAttribute("metaRobots", "index,follow");
		}

		/* (TPR-243) SEO Meta Tags and Titles for Listing Page */

		final List<SeoContentModel> seoContent = new ArrayList<SeoContentModel>(category.getSeoContents());
		String metaKeywords = null;
		String metaDescription = null;
		String metaTitle = null;
		if (CollectionUtils.isNotEmpty(seoContent))
		{

			metaKeywords = seoContent.get(seoContent.size() - 1).getSeoMetaKeyword();
			metaDescription = seoContent.get(seoContent.size() - 1).getSeoMetaDescription();
			metaTitle = seoContent.get(seoContent.size() - 1).getSeoMetaTitle();
			setUpMetaDataForSeo(model, metaKeywords, metaDescription, metaTitle);
			updatePageTitle(model, metaTitle);
		}
		else
		{

			metaKeywords = MetaSanitizerUtil.sanitizeKeywords(category.getKeywords());
			metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
			updatePageTitle(category, searchPageData.getBreadcrumbs(), model);
			setUpMetaData(model, metaKeywords, metaDescription);

		}

		final List<Breadcrumb> breadcrumbs = getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, searchPageData);
		populateTealiumData(breadcrumbs, model);
		return getViewPage(categorySearch.getCategoryPage());
	}




	/* changes for metaData content - (TPR-243) SEO Meta Tags and Titles */
	/**
	 * @param model
	 * @param metaKeywords
	 * @param metaDescription
	 * @param metaTitle
	 */
	private void setUpMetaDataForSeo(final Model model, final String metaKeywords, final String metaDescription,
			final String metaTitle)
	{
		final List<MetaElementData> metadata = new LinkedList<>();
		//Add the String constant in the ModelAttributeConstants file
		metadata.add(createMetaElement(ModelAttributetConstants.KEYWORDS, metaKeywords));
		metadata.add(createMetaElement(ModelAttributetConstants.DESCRIPTION, metaDescription));
		//metadata.add(createMetaElement("title", metaTitle));
		model.addAttribute(ModelAttributetConstants.METATAGS, metadata);
		//PRDI-422
		model.addAttribute(ModelAttributetConstants.KEYWORDS, metaKeywords);
		model.addAttribute(ModelAttributetConstants.DESCRIPTION, metaDescription);
	}

	/* PageTitle in header - (TPR-243) SEO Meta Tags and Titles */
	private void updatePageTitle(final Model model, final String metaTitle)
	{
		//Add the String constant in the ModelAttributeConstants file
		model.addAttribute(ModelAttributetConstants.METAPAGETITLE, metaTitle);
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

	//TISPRO-623
	@Override
	protected int getSearchPageSize()
	{
		int count = getSiteConfigService().getInt("storefront.search.pageSize", 0);
		if (getPageSiseCount() > 0)
		{
			count = getPageSiseCount();
		}
		//return 24;
		return count;
	}

	private void getSEOContents(final CategoryModel category, final Model model, final ContentPageModel categoryLandingPage)
	{
		String metaKeywords = null;
		String metaDescription = null;
		String metaTitle = null;
		//ContentPageModel categoryLandingPage;
		Collection<SeoContentModel> seoContentList = null;

		try
		{
			//categoryLandingPage = getLandingPageForCategory(category); // CAR -237 not called as categoryLandingPage is already sent and available Code review pt#4

			//			if (categoryLandingPage == null)
			//			{
			//				throw new CMSItemNotFoundException("Could not find a landing page for the category" + category.getName());
			//			}

			//changes for CAR-280
			//if (categoryLandingPage == null)
			//{
			//throw new CMSItemNotFoundException("Could not find a landing page for the category" + category.getName());
			//}


			//(TPR-243) SEO Meta Tags and Titles for Landing Page *: starts
			seoContentList = category.getSeoContents(); // CAR-235 - added to remove duplicate category.getSeoContents() calls at LIne # 1005 & 1064
			if (CollectionUtils.isEmpty(seoContentList)) // CAR-235 - changed for CodeReview category.getSeoContents() multiple call
			{
				setUpMetaDataForContentPage(model, categoryLandingPage);
			}
			/*
			 * (TPR-243) SEO Meta Tags and Titles for Landing Page *: ends else
			 */
			else
			{
				final List<SeoContentModel> seoContent = new ArrayList<SeoContentModel>(seoContentList); // CAR-235 changed for CodeReview category.getSeoContents() multiple call
				if (seoContent.size() >= 1)
				{
					metaKeywords = seoContent.get(seoContent.size() - 1).getSeoMetaKeyword();
					metaDescription = seoContent.get(seoContent.size() - 1).getSeoMetaDescription();
					metaTitle = seoContent.get(seoContent.size() - 1).getSeoMetaTitle();
				}
				setUpMetaDataForSeo(model, metaKeywords, metaDescription, metaTitle);
				updatePageTitle(model, metaTitle);
			}

			//setUpMetaDataForContentPage(model, categoryLandingPage);
		}
		//changes for CAR-280
		catch (final Exception e)
		{
			LOG.debug("SEO meta content error ---" + e.getMessage());
		}
	}

	protected class CategorySearchEvaluator
	{
		private final String categoryCode;
		private final SearchQueryData searchQueryData = new SearchQueryData();
		private final int page;
		private final ShowMode showMode;
		private final String sortCode;
		private CategoryPageModel categoryPage;
		private boolean showCategoriesOnly;
		private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData;
		private final String pageFacets;

		public CategorySearchEvaluator(final String categoryCode, final String searchQuery, final int page,
				final ShowMode showMode, final String sortCode, final CategoryPageModel categoryPage, final String pageFacets)
		{
			this.categoryCode = categoryCode;
			this.searchQueryData.setValue(searchQuery);
			this.page = page;
			this.showMode = showMode;
			this.sortCode = sortCode;
			this.categoryPage = categoryPage;
			this.pageFacets = pageFacets;
		}

		public void doSearch()
		{
			showCategoriesOnly = false;
			if (searchQueryData.getValue() == null)
			{
				// Direct category link without filtering
				searchPageData = getProductSearchFacade().categorySearch(categoryCode);
				if (categoryPage != null)
				{
					showCategoriesOnly = !categoryHasDefaultPage(categoryPage)
							&& CollectionUtils.isNotEmpty(searchPageData.getSubCategories());
				}
			}
			else
			{
				// We have some search filtering
				if (categoryPage == null || !categoryHasDefaultPage(categoryPage))
				{
					// Load the default category page
					categoryPage = getDefaultCategoryPage();
				}

				final SearchStateData searchState = new SearchStateData();
				searchState.setQuery(searchQueryData);

				final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
				pageableData.setPageFacets(pageFacets);
				searchPageData = getProductSearchFacade().categorySearch(categoryCode, searchState, pageableData);
			}
		}


		public int getPage()
		{
			return page;
		}

		public CategoryPageModel getCategoryPage()
		{
			return categoryPage;
		}

		public boolean isShowCategoriesOnly()
		{
			return showCategoriesOnly;
		}

		public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> getSearchPageData()
		{
			return searchPageData;
		}
	}

	private boolean isLuxurySite()
	{

		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		if (null != currentBaseSite && StringUtils.isNotBlank(currentBaseSite.getUid())
				&& MarketplaceFacadesConstants.LuxuryPrefix.equals(currentBaseSite.getUid()))
		{
			return true;
		}
		return false;
	}

}
