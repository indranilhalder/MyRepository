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
package com.tisl.mpl.v2.controller;

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.catalog.CatalogFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commercefacades.product.data.ProductReferencesData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.product.data.SuggestionData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commercefacades.storefinder.StoreFinderStockFacade;
import de.hybris.platform.commercefacades.storefinder.data.StoreFinderStockSearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductReferenceListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ReviewListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ReviewWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.StockWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.SuggestionListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.queues.ProductExpressUpdateElementListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.SearchStateWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.StoreFinderStockSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetBuilder;
import de.hybris.platform.commercewebservicescommons.mapping.impl.FieldSetBuilderContext;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.promotions.util.Tuple3;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.localization.Localization;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.constants.YcommercewebservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.brand.MplFollowedBrandFacade;
import com.tisl.mpl.facade.category.MplCategoryFacade;
import com.tisl.mpl.facade.compare.MplProductCompareFacade;
import com.tisl.mpl.facade.product.SizeGuideFacade;
import com.tisl.mpl.facades.product.data.ProductCompareData;
import com.tisl.mpl.formatters.WsDateFormatter;
import com.tisl.mpl.marketplacecommerceservices.services.product.MplProductService;
import com.tisl.mpl.product.data.ReviewDataList;
import com.tisl.mpl.product.data.SuggestionDataList;
import com.tisl.mpl.queues.data.ProductExpressUpdateElementData;
import com.tisl.mpl.queues.data.ProductExpressUpdateElementDataList;
import com.tisl.mpl.queues.impl.ProductExpressUpdateQueue;
import com.tisl.mpl.service.MplProductWebService;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.stock.CommerceStockFacade;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.utility.SearchSuggestUtilityMethods;
import com.tisl.mpl.v2.helper.ProductsHelper;
import com.tisl.mpl.validator.PointOfServiceValidator;
import com.tisl.mpl.wsdto.BreadcrumbResponseWsDTO;
import com.tisl.mpl.wsdto.DepartmentHierarchyWs;
import com.tisl.mpl.wsdto.EgvProductInfoWSDTO;
import com.tisl.mpl.wsdto.FollowedBrandWsDto;
import com.tisl.mpl.wsdto.LuxHeroBannerWsDTO;
import com.tisl.mpl.wsdto.MplFollowedBrandsWsDto;
import com.tisl.mpl.wsdto.MplNewProductDetailMobileWsData;
import com.tisl.mpl.wsdto.ProductAPlusWsData;
import com.tisl.mpl.wsdto.ProductCompareWsDTO;
import com.tisl.mpl.wsdto.ProductDetailMobileWsData;
import com.tisl.mpl.wsdto.ProductInfoWSDTO;
import com.tisl.mpl.wsdto.ProductSearchPageWsDto;
import com.tisl.mpl.wsdto.ProductSearchPagefacateWsDTO;
import com.tisl.mpl.wsdto.SizeGuideWsDTO;


/**
 * Web Services Controller to expose the functionality of the
 * {@link de.hybris.platform.commercefacades.product.ProductFacade} and SearchFacade.
 *
 * @pathparam productCode Product identifier
 * @pathparam storeName Store identifier
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/products")
public class ProductsController extends BaseController
{
	/**
	 *
	 */
	private static final String LSH = "LSH";
	private static final String BASIC_OPTION = "BASIC";
	private static final Set<ProductOption> OPTIONS;
	private static final String MAX_INTEGER = "2147483647";
	private static final int CATALOG_ID_POS = 0;
	private static final int CATALOG_VERSION_POS = 1;
	private static final Logger LOG = Logger.getLogger(ProductsController.class);
	private static final String DROPDOWN_BRAND = "MBH";
	private static final String DROPDOWN_CATEGORY = "MSH";
	private static final String BACKSLASH_S = "\\s+";
	/* SONAR FIX */
	//private static final String PRODUCT_OLD_URL_PATTERN = "/**/p";

	private static String PRODUCT_OPTIONS = "";
	@Resource(name = "storeFinderStockFacade")
	private StoreFinderStockFacade storeFinderStockFacade;
	@Resource(name = "cwsProductFacade")
	private ProductFacade productFacade;
	@Resource(name = "wsDateFormatter")
	private WsDateFormatter wsDateFormatter;
	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;
	//	@Resource(name = "solrSearchStateConverter")
	//	private Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter;
	@Resource(name = "httpRequestReviewDataPopulator")
	private Populator<HttpServletRequest, ReviewData> httpRequestReviewDataPopulator;
	@Resource(name = "reviewValidator")
	private Validator reviewValidator;
	@Resource(name = "reviewDTOValidator")
	private Validator reviewDTOValidator;
	@Resource(name = "commerceStockFacade")
	private CommerceStockFacade commerceStockFacade;
	@Resource(name = "pointOfServiceValidator")
	private PointOfServiceValidator pointOfServiceValidator;
	@Resource(name = "productExpressUpdateQueue")
	private ProductExpressUpdateQueue productExpressUpdateQueue;
	@Resource(name = "catalogFacade")
	private CatalogFacade catalogFacade;
	@Resource(name = "productsHelper")
	private ProductsHelper productsHelper;
	@Resource(name = "mplProductWebService")
	private MplProductWebService mplProductWebService;
	@Resource(name = "sizeGuideFacade")
	private SizeGuideFacade sizeGuideFacade;
	@Resource(name = "mplProductCompareFacade")
	private MplProductCompareFacade mplProductCompareFacade;
	@Resource(name = "fieldSetBuilder")
	private FieldSetBuilder fieldSetBuilder;
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18nService")
	private I18NService i18nService;
	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;
	@Resource(name = "mplCategoryFacade")
	private MplCategoryFacade mplCategoryFacade;

	@Resource(name = "productService")
	private MplProductService productService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	private static final String CUSTOMER = "ROLE_CUSTOMERGROUP";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final String EXP = "[^a-zA-Z&0-9\\s+]+";
	private static final String FOLLOW_BRAND_ERROR = "Followed Brand Error";
	@Resource
	private UserService userService;

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Resource
	private SearchSuggestUtilityMethods searchSuggestUtilityMethods;
	//critical sonar fix
	@Resource(name = "categoryService")
	private CategoryService categoryService;


	@Resource(name = "mplFollowedBrandFacade")
	private MplFollowedBrandFacade mplFollowedBrandFacade;

	static
	{
		for (final ProductOption option : ProductOption.values())
		{
			PRODUCT_OPTIONS = PRODUCT_OPTIONS + option.toString() + " ";
		}
		PRODUCT_OPTIONS = PRODUCT_OPTIONS.trim().replace(" ", YcommercewebservicesConstants.OPTIONS_SEPARATOR);
		OPTIONS = extractOptions(PRODUCT_OPTIONS);
	}

	private static Set<ProductOption> extractOptions(final String options)
	{
		final String optionsStrings[] = options.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<ProductOption> opts = new HashSet<ProductOption>();
		for (final String option : optionsStrings)
		{
			opts.add(ProductOption.valueOf(option));
		}
		return opts;
	}

	/**
	 * Returns a list of products and additional data such as: available facets, available sorting and pagination
	 * options. It can include spelling suggestions.To make spelling suggestions work you need to:
	 * <ul>
	 * <li>Make sure enableSpellCheck on the SearchQuery is set to true. By default it should be already set to true.</li>
	 * <li>Have indexed properties configured to be used for spellchecking.</li>
	 * </ul>
	 *
	 * @queryparam query Serialized query, free text search, facets.<br>
	 *             The format of a serialized query:
	 *             <b>freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2</b>
	 * @queryparam currentPage The current result page requested.
	 * @queryparam pageSize The number of results returned per page.
	 * @queryparam sort Sorting method applied to the display search results.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List of products
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public ProductSearchPageWsDTO searchProducts(@RequestParam(required = false) final String query,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false) final String sort, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletResponse response)
	{
		final ProductSearchPageWsDTO result = productsHelper.searchProducts(query, currentPage, pageSize, sort,
				addPaginationField(fields));
		// X-Total-Count header
		setTotalCountHeader(response, result.getPagination());
		return result;
	}

	/**
	 * Returns {@value BaseController#HEADER_TOTAL_COUNT} header with total number of products satisfying a query. It
	 * doesn't return HTTP body.
	 *
	 * @queryparam query Serialized query, free text search, facets.<br>
	 *             The format of a serialized query:
	 *             <b>freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2</b>
	 */
	@RequestMapping(value = "/search", method = RequestMethod.HEAD)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	public void countSearchProducts(@RequestParam(required = false) final String query, final HttpServletResponse response)
	{
		final ProductSearchPageData<SearchStateData, ProductData> result = productsHelper.searchProducts(query, 0, 1, null);
		setTotalCountHeader(response, result.getPagination());
	}

	/**
	 * Returns details of a single product according to a product code.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Product details
	 */
	/**
	 * @param productCode
	 * @param fields
	 * @return ProductDetailMobileWsDTO
	 */
	/* Mobile product details for product code begins */
	@RequestMapping(value = "/{productCode}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PRIVATE, maxAge = 120)
	@Cacheable(value = "productCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,#productCode,#fields)")
	@ResponseBody
	public ProductDetailMobileWsData getProductByCode(@PathVariable String productCode,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletRequest request,
			@RequestParam(required = false) final String channel) throws MalformedURLException
	{
		ProductDetailMobileWsData product = new ProductDetailMobileWsData();

		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductByCode: code=" + sanitize(productCode) + " | options=" + PRODUCT_OPTIONS);
		}
		try
		{
			URL requestUrl = null;
			if (null != request && null != request.getRequestURL())
			{
				requestUrl = new URL(request.getRequestURL().toString());
			}
			String portString = MarketplacecommerceservicesConstants.EMPTYSPACE;
			String baseUrl = MarketplacecommerceservicesConstants.EMPTYSPACE;
			if (null != requestUrl)
			{
				if (0 != requestUrl.getPort())
				{
					portString = requestUrl.getPort() == -1 ? MarketplacecommerceservicesConstants.EMPTYSPACE
							: MarketplacecommerceservicesConstants.COLON + requestUrl.getPort();
				}
				if (null != requestUrl.getHost() && null != requestUrl.getProtocol())
				{
					baseUrl = requestUrl.getProtocol() + MarketplacecommerceservicesConstants.COLON_SLASH + requestUrl.getHost()
							+ portString + MarketplacecommerceservicesConstants.EMPTYSPACE;
					//+ MarketplacewebservicesConstants.FORGOTPASSWORD_URL;
				}
			}

			product = mplProductWebService.getProductdetailsForProductCode(productCode, baseUrl, channel);
			//TPR-978
			final ProductAPlusWsData aPlusProductData = mplProductWebService.getAPluscontentForProductCode(productCode);
			if (null != aPlusProductData)
			{
				product.setAPlusContent(aPlusProductData);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				product.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				product.setErrorCode(e.getErrorCode());
			}
			product.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			product.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			if (null != e.getErrorMessage())
			{
				product.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				product.setErrorCode(e.getErrorCode());
			}
		}
		//TPR-799
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			product.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			product.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			product.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return product;
	}

	/* Mobile product details for product code ends */
	/**
	 * Returns product's stock level for a particular store (POS).
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Stock level information for product in store
	 * @throws WebserviceValidationException
	 *            If store doesn't exist
	 * @throws StockSystemException
	 *            When stock system is not enabled on this site
	 */
	@RequestMapping(value = "/{productCode}/stock/{storeName}", method = RequestMethod.GET)
	@ResponseBody
	public StockWsDTO getStockData(@PathVariable final String baseSiteId, @PathVariable final String productCode,
			@PathVariable final String storeName, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException, StockSystemException
	{
		validate(storeName, "storeName", pointOfServiceValidator);
		if (!commerceStockFacade.isStockSystemEnabled(baseSiteId))
		{
			throw new StockSystemException("Stock system is not enabled on this site", StockSystemException.NOT_ENABLED, baseSiteId);
		}
		final StockData stockData = commerceStockFacade.getStockDataForProductAndPointOfService(productCode, storeName);
		final StockWsDTO dto = dataMapper.map(stockData, StockWsDTO.class, fields);
		return dto;
	}







	/**
	 * Returns product's stock levels sorted by distance from specific location passed by free-text parameter or
	 * longitude and latitude parameters. The following two sets of parameters are available:
	 * <ul>
	 * <li>location (required), currentPage (optional), pageSize (optional) or</li>>
	 * <li>longitude (required), latitude (required), currentPage (optional), pageSize(optional).</li>
	 * </ul>
	 *
	 * @queryparam location Free-text location
	 * @queryparam latitude Longitude location parameter.
	 * @queryparam longitude Latitude location parameter.
	 * @queryparam currentPage The current result page requested.
	 * @queryparam pageSize The number of results returned per page.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return product's stock levels
	 */
	@RequestMapping(value = "/{productCode}/stock", method = RequestMethod.GET)
	@ResponseBody
	public StoreFinderStockSearchPageWsDTO searchProductStockByLocation(@PathVariable final String productCode,
			@RequestParam(required = false) final String location, @RequestParam(required = false) final Double latitude,
			@RequestParam(required = false) final Double longitude,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletResponse response)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductStockByLocation: code=" + sanitize(productCode) + " | location=" + sanitize(location)
					+ " | latitude=" + latitude + " | longitude=" + longitude);
		}

		final StoreFinderStockSearchPageData result = doSearchProductStockByLocation(productCode, location, latitude, longitude,
				currentPage, pageSize);

		// X-Total-Count header
		setTotalCountHeader(response, result.getPagination());

		return dataMapper.map(result, StoreFinderStockSearchPageWsDTO.class, addPaginationField(fields));
	}

	/**
	 * Returns {@value BaseController#HEADER_TOTAL_COUNT} header with a total number of product's stock levels. It does
	 * not return the HTTP body. The following two sets of parameters are available:
	 * <ul>
	 * <li>location (required) or</li>
	 * <li>longitude (required), latitude (required).</li>
	 * </ul>
	 *
	 * @queryparam location Free-text location
	 * @queryparam latitude Longitude location parameter.
	 * @queryparam longitude Latitude location parameter.
	 */
	@RequestMapping(value = "/{productCode}/stock", method = RequestMethod.HEAD)
	public void countSearchProductStockByLocation(@PathVariable final String productCode,
			@RequestParam(required = false) final String location, @RequestParam(required = false) final Double latitude,
			@RequestParam(required = false) final Double longitude, final HttpServletResponse response)
	{
		final StoreFinderStockSearchPageData result = doSearchProductStockByLocation(productCode, location, latitude, longitude, 0,
				1);

		setTotalCountHeader(response, result.getPagination());
	}

	protected StoreFinderStockSearchPageData doSearchProductStockByLocation(final String productCode, final String location,
			final Double latitude, final Double longitude, final int currentPage, final int pageSize)
	{
		final Set<ProductOption> opts = extractOptions(BASIC_OPTION);
		StoreFinderStockSearchPageData result;
		if (latitude != null && longitude != null)
		{
			result = storeFinderStockFacade.productSearch(createGeoPoint(latitude, longitude),
					productFacade.getProductForCodeAndOptions(productCode, opts), createPageableData(currentPage, pageSize, null));
		}
		else if (location != null)
		{
			result = storeFinderStockFacade.productSearch(location, productFacade.getProductForCodeAndOptions(productCode, opts),
					createPageableData(currentPage, pageSize, null));
		}
		else
		{
			throw new RequestParameterException("You need to provide location or longitute and latitute parameters",
					RequestParameterException.MISSING, "location or longitute and latitute");
		}
		return result;
	}

	/**
	 * Returns the reviews for a product with a given product code.
	 *
	 * @return product's review list
	 */
	@RequestMapping(value = "/{productCode}/users/{userId}/reviews", method = RequestMethod.GET)
	@ResponseBody
	public ReviewListWsDTO getPaginatedProductReviews(
			@PathVariable final String productCode,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = "0") final int page,
			@RequestParam(value = MarketplacewebservicesConstants.SORT, required = false, defaultValue = MarketplacecommerceservicesConstants.BY_DATE) final String sort,
			@RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false, defaultValue = "desc") final String orderBy)
	{
		final ReviewDataList reviewDataList = new ReviewDataList();
		if (pageSize == null)
		{
			pageSize = Integer.valueOf(configurationService.getConfiguration().getString(
					"ratingAndReview.webservice.getReviews.pageSize", "10"));
		}
		final PageableData pageableData = createPageableData(page, pageSize.intValue(), sort, ShowMode.Page);
		final Tuple3<List<ReviewData>, Long, Integer> tuple3 = mplProductWebService.getReviews(productCode, pageableData, orderBy);
		reviewDataList.setReviews(tuple3.getFirst());
		reviewDataList.setPageNumber(Integer.valueOf(page));
		reviewDataList.setPageSize(pageSize);
		reviewDataList.setTotalNoOfReviews(tuple3.getSecond());
		reviewDataList.setTotalNoOfPages(tuple3.getThird());
		return dataMapper.map(reviewDataList, ReviewListWsDTO.class, fields);
	}

	/**
	 * Returns the reviews for a product with a given product code.
	 *
	 * @return product's review list
	 */
	@RequestMapping(value = "/{productCode}/reviews", method = RequestMethod.GET)
	@ResponseBody
	public ReviewListWsDTO getProductReviews(@PathVariable final String productCode,
			@RequestParam(required = false) final Integer maxCount,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final ReviewDataList reviewDataList = new ReviewDataList();
		reviewDataList.setReviews(mplProductWebService.getReviews(productCode, maxCount));
		return dataMapper.map(reviewDataList, ReviewListWsDTO.class, fields);
	}

	/**
	 * Creates a new customer review as an anonymous user.
	 *
	 * @formparam rating This parameter is required. Value needs to be between 1 and 5.
	 * @formparam alias
	 * @formparam headline This parameter is required.
	 * @formparam comment This parameter is required.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Created review
	 * @throws WebserviceValidationException
	 *            When given parameters are incorrect
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{productCode}/reviews", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ReviewWsDTO createEditReview(@PathVariable final String productCode,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletRequest request)
			throws WebserviceValidationException
	{
		final ReviewData reviewData = new ReviewData();
		httpRequestReviewDataPopulator.populate(request, reviewData);
		final String reviewId = request.getParameter("id");
		validate(reviewData, "reviewData", reviewValidator);
		ReviewData reviewDataRet = null;
		if (StringUtils.isNotBlank(reviewId))
		{
			reviewDataRet = productService.editDeleteReviewEntry(getUserService().getCurrentUser(),
					productService.getProductForCode(productCode), reviewId, true, reviewData);
		}
		else
		{
			reviewDataRet = productFacade.postReview(productCode, reviewData);
		}
		final ReviewWsDTO dto = dataMapper.map(reviewDataRet, ReviewWsDTO.class, fields);
		return dto;
	}

	/**
	 * Delete's a Review for a logged in Customer
	 *
	 *
	 * @return void
	 * @throws WebserviceValidationException
	 *            When given parameters are incorrect
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{productCode}/{reviewId}/deleteReview", method = RequestMethod.GET)
	@ResponseBody
	public void deleteReview(@PathVariable final String productCode, @PathVariable final String reviewId,
			final HttpServletRequest request) throws WebserviceValidationException
	{
		if (StringUtils.isNotBlank(reviewId))
		{
			productService.editDeleteReviewEntry(getUserService().getCurrentUser(), productService.getProductForCode(productCode),
					reviewId, false, null);
		}

	}

	/**
	 * Creates a new customer review as an anonymous user.
	 *
	 * @param review
	 *           Object contains review details like : rating, alias, headline, comment
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @bodyparams headline,alias,rating,date,comment
	 * @return Created review
	 * @throws WebserviceValidationException
	 *            When given parameters are incorrect
	 */
	@RequestMapping(value = "/{productCode}/reviews", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ReviewWsDTO createReview(@PathVariable final String productCode, @RequestBody final ReviewWsDTO review,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) throws WebserviceValidationException
	{
		validate(review, "review", reviewDTOValidator);
		final ReviewData reviewData = dataMapper.map(review, ReviewData.class, "alias,rating,headline,comment");
		final ReviewData reviewDataRet = productFacade.postReview(productCode, reviewData);
		final ReviewWsDTO dto = dataMapper.map(reviewDataRet, ReviewWsDTO.class, fields);
		return dto;
	}

	/**
	 * Returns references for a product with a given product code. Reference type specifies which references to return.
	 *
	 * @queryparam pageSize Maximum size of returned results.
	 * @queryparam referenceType Reference type according to enum ProductReferenceTypeEnum
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List of product references
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/{productCode}/references", method = RequestMethod.GET)
	@ResponseBody
	public ProductReferenceListWsDTO exportProductReferences(@PathVariable final String productCode,
			@RequestParam(required = false, defaultValue = MAX_INTEGER) final int pageSize,
			@RequestParam final String referenceType, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<ProductOption> opts = Lists.newArrayList(OPTIONS);
		final ProductReferenceTypeEnum referenceTypeEnum = ProductReferenceTypeEnum.valueOf(referenceType);

		final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(productCode,
				referenceTypeEnum, opts, Integer.valueOf(pageSize));
		final ProductReferencesData productReferencesData = new ProductReferencesData();
		productReferencesData.setReferences(productReferences);

		return dataMapper.map(productReferencesData, ProductReferenceListWsDTO.class, fields);
	}

	private PageableData createPageableData(final int currentPage, final int pageSize, final String sort)
	{
		final PageableData pageable = new PageableData();

		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		pageable.setSort(sort);
		return pageable;
	}

	private GeoPoint createGeoPoint(final Double latitude, final Double longitude)
	{
		final GeoPoint point = new GeoPoint();
		point.setLatitude(latitude.doubleValue());
		point.setLongitude(longitude.doubleValue());

		return point;
	}

	/**
	 * Returns a list of all available suggestions related to a given term and limits the results to a specific value of
	 * the max parameter.
	 *
	 * @queryparam term Specified term
	 * @queryparam max Specifies the limit of results.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List of auto suggestions
	 */
	@RequestMapping(value = "/suggestions", method = RequestMethod.GET)
	@ResponseBody
	public SuggestionListWsDTO getSuggestions(@RequestParam(required = true, defaultValue = " ") final String term,
			@RequestParam(required = true, defaultValue = "10") final int max,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<SuggestionData> suggestions = new ArrayList<>();
		final SuggestionDataList suggestionDataList = new SuggestionDataList();

		List<AutocompleteSuggestionData> autoSuggestions = productSearchFacade.getAutocompleteSuggestions(term);
		if (max < autoSuggestions.size())
		{
			autoSuggestions = autoSuggestions.subList(0, max);
		}

		for (final AutocompleteSuggestionData autoSuggestion : autoSuggestions)
		{
			final SuggestionData suggestionData = new SuggestionData();
			suggestionData.setValue(autoSuggestion.getTerm());
			suggestions.add(suggestionData);
		}

		suggestionDataList.setSuggestions(suggestions);

		return dataMapper.map(suggestionDataList, SuggestionListWsDTO.class, fields);
	}

	/**
	 * Returns products added to the express update feed. Returns only elements updated after the provided timestamp.The
	 * queue is cleared using a defined cronjob.
	 *
	 * @queryparam timestamp Only items newer than the given parameter are retrieved from the queue. This parameter
	 *             should be in RFC-8601 format.
	 * @queryparam catalog Only products from this catalog are returned. Format: <b>catalogId:catalogVersion</b>
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List of products added to the express update feed
	 * @throws RequestParameterException
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/expressupdate", method = RequestMethod.GET)
	@ResponseBody
	public ProductExpressUpdateElementListWsDTO expressUpdate(@RequestParam final String timestamp,
			@RequestParam(required = false) final String catalog,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws RequestParameterException
	{
		final Date timestampDate;
		try
		{
			timestampDate = wsDateFormatter.toDate(timestamp);
		}
		catch (final IllegalArgumentException ex)
		{
			throw new RequestParameterException("Wrong time format. The only accepted format is ISO-8601.",
					RequestParameterException.INVALID, "timestamp", ex);
		}
		final ProductExpressUpdateElementDataList productExpressUpdateElementDataList = new ProductExpressUpdateElementDataList();
		final List<ProductExpressUpdateElementData> products = productExpressUpdateQueue.getItems(timestampDate);
		filterExpressUpdateQueue(products, validateAndSplitCatalog(catalog));
		productExpressUpdateElementDataList.setProductExpressUpdateElements(products);
		return dataMapper.map(productExpressUpdateElementDataList, ProductExpressUpdateElementListWsDTO.class, fields);
	}

	private void filterExpressUpdateQueue(final List<ProductExpressUpdateElementData> products, final List<String> catalogInfo)
	{
		if (catalogInfo.size() == 2 && StringUtils.isNotEmpty(catalogInfo.get(CATALOG_ID_POS))
				&& StringUtils.isNotEmpty(catalogInfo.get(CATALOG_VERSION_POS)) && CollectionUtils.isNotEmpty(products))
		{
			final Iterator<ProductExpressUpdateElementData> dataIterator = products.iterator();
			while (dataIterator.hasNext())
			{
				final ProductExpressUpdateElementData productExpressUpdateElementData = dataIterator.next();
				if (!catalogInfo.get(CATALOG_ID_POS).equals(productExpressUpdateElementData.getCatalogId())
						|| !catalogInfo.get(CATALOG_VERSION_POS).equals(productExpressUpdateElementData.getCatalogVersion()))
				{
					dataIterator.remove();
				}
			}
		}
	}

	protected List<String> validateAndSplitCatalog(final String catalog) throws RequestParameterException
	{
		final List<String> catalogInfo = new ArrayList<>();
		if (StringUtils.isNotEmpty(catalog))
		{
			catalogInfo.addAll(Lists.newArrayList(Splitter.on(':').trimResults().omitEmptyStrings().split(catalog)));
			if (catalogInfo.size() == 2)
			{
				catalogFacade.getProductCatalogVersionForTheCurrentSite(catalogInfo.get(CATALOG_ID_POS),
						catalogInfo.get(CATALOG_VERSION_POS), Collections.EMPTY_SET);
			}
			else if (!catalogInfo.isEmpty())
			{
				throw new RequestParameterException("Invalid format. You have to provide catalog as 'catalogId:catalogVersion'",
						RequestParameterException.INVALID, "catalog");
			}
		}
		return catalogInfo;
	}

	/**
	 * @return the mplProductWebService
	 */
	public MplProductWebService getMplProductWebService()
	{
		return mplProductWebService;
	}

	/**
	 * @param mplProductWebService
	 *           the mplProductWebService to set
	 */
	public void setMplProductWebService(final MplProductWebService mplProductWebService)
	{
		this.mplProductWebService = mplProductWebService;
	}

	/**
	 * @description method is called to search the available products
	 * @param searchQuery
	 * @param page
	 * @param pageSize
	 * @param showMode
	 * @param sortCode
	 * @param fields
	 * @return ProductSearchPagefacateWsDTO
	 */
	@RequestMapping(value = "/searchproductdto", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public ProductSearchPagefacateWsDTO searchProductfacatedto(@RequestParam(required = false) final String searchQuery,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int page,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize, final ShowMode showMode,
			@RequestParam(required = false) final String sortCode,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final ProductSearchPagefacateWsDTO productSearchPagefacate = new ProductSearchPagefacateWsDTO();

		try
		{
			final ProductSearchPageWsDTO result = productsHelper.searchProductsfacatedto(searchQuery, page, pageSize, showMode,
					sortCode, fields);
			productSearchPagefacate.setProductSearchPage(result);
			productSearchPagefacate.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			productSearchPagefacate.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPagefacate.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + ":" + e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			productSearchPagefacate.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPagefacate.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + ":" + e);
		}
		return productSearchPagefacate;
	}

	/**
	 * @description Return Size Guide Data of a Product
	 * @queryparam productCode
	 *
	 */
	@RequestMapping(value = "/{productCode}/sizeGuide", method = RequestMethod.GET)
	@ResponseBody
	public SizeGuideWsDTO getSizeGuide(@PathVariable final String productCode, @RequestParam(required = false) final Boolean isPwa)
	{
		SizeGuideWsDTO sizeGuideDataList = new SizeGuideWsDTO();
		try
		{
			sizeGuideDataList = sizeGuideFacade.getWSProductSizeguide(productCode, isPwa);
			if (!(sizeGuideDataList != null))
			{
				throw new RequestParameterException(MarketplacecommerceservicesConstants.B9205);
			}
			sizeGuideDataList.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				sizeGuideDataList.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				sizeGuideDataList.setErrorCode(e.getErrorCode());
			}
			sizeGuideDataList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				sizeGuideDataList.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				sizeGuideDataList.setErrorCode(e.getErrorCode());
			}
			sizeGuideDataList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			sizeGuideDataList.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			sizeGuideDataList.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			sizeGuideDataList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return sizeGuideDataList;
	}

	/**
	 * @description method is called to search the available products with respect to CategoryID ,
	 *              searchText,sortCode,pageSize
	 * @param searchText
	 * @param categoryID
	 * @param page
	 * @param pageSize
	 * @param sortCode
	 * @param fields
	 * @return ProductSearchPageWsDto
	 */

	@RequestMapping(value = "/conceirgeSearch", method = RequestMethod.POST, produces = MarketplacecommerceservicesConstants.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductSearchPageWsDto conceirgeSearch(@RequestParam final String age, @RequestParam final String genderOrTitle,
			@RequestParam final String typeOfProduct, @RequestParam final String reasonOrEvent,
			@RequestParam(required = false) final int page, @RequestParam(required = false) final int pageSize,
			@RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		final ProductSearchPageWsDto productSearchPage = new ProductSearchPageWsDto();
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		final PageableData pageableData = createPageableData(page, pageSize, null, ShowMode.Page);
		searchQueryData.setValue(typeOfProduct);
		searchState.setQuery(searchQueryData);
		searchPageData = searchFacade.conceirgeSearch(age, genderOrTitle, reasonOrEvent, searchState, pageableData);
		searchSuggestUtilityMethods.setSearchPageData(productSearchPage, searchPageData);

		final ProductSearchPageWsDto sortingvalues = dataMapper.map(searchPageData, ProductSearchPageWsDto.class, fields);
		if (null != sortingvalues)
		{
			if (null != sortingvalues.getPagination())
			{
				productSearchPage.setPagination(sortingvalues.getPagination());
			}
			if (null != sortingvalues.getSorts())
			{
				productSearchPage.setSorts(sortingvalues.getSorts());
			}
			if (null != sortingvalues.getCurrentQuery())
			{
				productSearchPage.setCurrentQuery(sortingvalues.getCurrentQuery());
			}
			if (null != sortingvalues.getSpellingSuggestion())
			{
				productSearchPage.setSpellingSuggestion(searchPageData.getSpellingSuggestion().getSuggestion().replaceAll(EXP, ""));
			}
		}
		return productSearchPage;
	}

	/**
	 * @desc SERP search with key word redirects
	 * @param searchText
	 * @param typeID
	 * @param page
	 * @param pageSize
	 * @param sortCode
	 * @param isTextSearch
	 * @param isFilter
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/serpsearch", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MarketplacecommerceservicesConstants.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductSearchPageWsDto searchProductDto(@RequestParam(required = false) String searchText,
			@RequestParam(required = false) String typeID, @RequestParam(required = false) int page,
			@RequestParam(required = false) int pageSize, @RequestParam(required = false) String sortCode,
			@RequestParam(required = false, defaultValue = "false") final boolean isTextSearch,
			@RequestParam(required = false) boolean isFilter, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false) final boolean isFromLuxuryWeb)
	{

		final ProductSearchPageWsDto productSearchPage = new ProductSearchPageWsDto();
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		Map<String, List<String>> params = null;
		String url = null;
		try
		{
			if (StringUtils.isNotBlank(searchText))
			{
				//For Keyword Redirection
				if (isTextSearch)
				{
					params = searchSuggestUtilityMethods.getKeywordSearch(searchText);
					if (MapUtils.isNotEmpty(params))
					{
						//setting parameter again as per keyword redirect
						if (params.containsKey("searchText"))
						{
							searchText = params.get("searchText").get(0);
						}
						if (params.containsKey("typeID"))
						{
							typeID = params.get("typeID").get(0);
						}
						if (params.containsKey("page"))
						{
							//suggestion to parseInt
							page = Integer.parseInt(params.get("page").get(0));
						}
						if (params.containsKey("pageSize"))
						{
							//suggestion to parseInt
							pageSize = Integer.parseInt(params.get("pageSize").get(0));
						}
						if (params.containsKey("sortCode"))
						{
							sortCode = params.get("sortCode").get(0);
						}
						if (params.containsKey("isFilter"))
						{
							//suggestion to parseBoolean
							isFilter = Boolean.parseBoolean(params.get("isFilter").get(0));
						}
						//fetching keyword url
						if (params.containsKey("keywordUrl"))
						{
							url = params.get("keywordUrl").get(0);
						}
					}
				}
				//End For Keyword Redirection

				final PageableData pageableData = createPageableData(page, pageSize, sortCode, ShowMode.Page);
				final SearchStateData searchState = new SearchStateData();
				final SearchQueryData searchQueryData = new SearchQueryData();
				searchQueryData.setValue(searchText);
				searchState.setQuery(searchQueryData);
				if (isFromLuxuryWeb)
				{
					searchState.setLuxurySiteFrom(MarketplacecommerceservicesConstants.CHANNEL_WEB);
				}
				//				else
				//				{
				//					searchState.setLuxurySiteFrom(MarketplacecommerceservicesConstants.CHANNEL_APP);
				//				}

				if (StringUtils.isNotEmpty(typeID))
				{
					if (typeID.equalsIgnoreCase("all"))
					{
						searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
								.textSearch(searchState, pageableData);
						//PR-23 start
						if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
						{
							if (StringUtils.isNotEmpty(searchText))
							{

								final String[] elements = searchText.trim().split(BACKSLASH_S);

								//if (elements.length == 2 || elements.length == 3)
								if (elements.length >= 2)
								{
									//	final SearchStateData searchStateAll = new SearchStateData();
									//	final SearchQueryData searchQueryDataAll = new SearchQueryData();
									//	searchQueryDataAll.setValue(searchText);
									searchState.setNextSearch(true);
									//searchStateAll.setQuery(searchQueryDataAll);
									searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
											.textSearch(searchState, pageableData);

									//searchPageData.getCurrentQuery().setTwoTokenNextSearch(true);
								}
							}
						}
						//PR-23 end
					}
					else if (typeID.startsWith(DROPDOWN_CATEGORY) || typeID.startsWith(DROPDOWN_BRAND) || typeID.startsWith(LSH))
					{
						//searchPageData = productSearchFacade.categorySearch(typeID, searchState, pageableData);
						searchPageData = searchFacade.searchCategorySearch(typeID, searchState, pageableData);
						//PR-23 start
						if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
						{
							if (StringUtils.isNotEmpty(searchText))
							{

								final String[] elements = searchText.trim().split(BACKSLASH_S);

								//if (elements.length == 2 || elements.length == 3)
								if (elements.length >= 2)
								{

									searchState.setNextSearch(true);
									searchPageData = searchFacade.searchCategorySearch(typeID, searchState, pageableData);

									//searchPageData.getCurrentQuery().setTwoTokenNextSearch(true);
								}
							}
						}
						//PR-23 end

						final CategoryModel category = categoryService.getCategoryForCode(typeID);
						final List<SimpleBannerComponentModel> crossSellBanners = category.getCrosssellBanners();
						if (CollectionUtils.isNotEmpty(crossSellBanners))
						{
							final SimpleBannerComponentModel crossSellBannerModel = crossSellBanners.get(0);
							final LuxHeroBannerWsDTO bannerDto = new LuxHeroBannerWsDTO();
							bannerDto.setBannerUrl(crossSellBannerModel.getUrlLink());
							final MediaModel media = crossSellBannerModel.getMedia();
							if (null != media)
							{
								bannerDto.setBannerMedia(media.getURL2());
								bannerDto.setAltText(media.getAltText());
							}
							productSearchPage.setCrosssellBanner(bannerDto);
						}

						final List<SimpleBannerComponentModel> dynamicBanner = category.getDynamicBanners();
						if (CollectionUtils.isNotEmpty(dynamicBanner))
						{
							final SimpleBannerComponentModel dynamicBannerModel = dynamicBanner.get(0);
							final LuxHeroBannerWsDTO bannerDto = new LuxHeroBannerWsDTO();
							bannerDto.setBannerUrl(dynamicBannerModel.getUrlLink());
							final MediaModel media = dynamicBannerModel.getMedia();
							if (null != dynamicBannerModel.getMedia())
							{
								bannerDto.setBannerMedia(media.getURL2());
								bannerDto.setAltText(media.getAltText());
							}
							productSearchPage.setPlpHeroBanner(bannerDto);
						}
					}
					else
					{
						searchPageData = searchFacade.dropDownSearch(searchState, typeID, MarketplaceCoreConstants.SNS_SELLER_ID,
								pageableData);

						//PR-23 start
						if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
						{
							if (StringUtils.isNotEmpty(searchText))
							{

								final String[] elements = searchText.trim().split(BACKSLASH_S);

								//if (elements.length == 2 || elements.length == 3)
								if (elements.length >= 2)
								{

									searchState.setNextSearch(true);
									searchPageData = searchFacade.dropDownSearch(searchState, typeID,
											MarketplaceCoreConstants.SNS_SELLER_ID, pageableData);

									//searchPageData.getCurrentQuery().setTwoTokenNextSearch(true);
								}
							}
						}
						//PR-23 end


					}

					// Get results from All category when we don't get any results for dropdown category/brand/seller
					if (searchPageData != null && !typeID.equalsIgnoreCase("all")
							&& searchPageData.getPagination().getTotalNumberOfResults() == 0)
					{
						searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
								.textSearch(searchState, pageableData);

						//PR-23 start
						if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
						{
							if (StringUtils.isNotEmpty(searchText))
							{

								final String[] elements = searchText.trim().split(BACKSLASH_S);

								//if (elements.length == 2 || elements.length == 3)
								if (elements.length >= 2)
								{

									searchState.setNextSearch(true);
									searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
											.textSearch(searchState, pageableData);

									//searchPageData.getCurrentQuery().setTwoTokenNextSearch(true);
								}
							}
						}
						//PR-23 end


					}

					//TPR-6528
					if (null != searchPageData.getSpellingSuggestion()
							&& StringUtils.isNotEmpty(searchPageData.getSpellingSuggestion().getSuggestion()))
					{
						productSearchPage.setSpellingSuggestion(searchPageData.getSpellingSuggestion().getSuggestion()
								.replaceAll(EXP, ""));
						final SearchStateData searchStateAll = new SearchStateData();
						final SearchQueryData searchQueryDataAll = new SearchQueryData();
						searchQueryDataAll.setValue(searchPageData.getSpellingSuggestion().getSuggestion().replaceAll("[()]+", ""));
						searchStateAll.setQuery(searchQueryDataAll);
						if (isFromLuxuryWeb)
						{
							searchStateAll.setLuxurySiteFrom(MarketplacecommerceservicesConstants.CHANNEL_WEB);
						}
						searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) searchFacade
								.textSearch(searchStateAll, pageableData);
					}

					if (isFilter)
					{
						searchSuggestUtilityMethods.setFilterData(productSearchPage, searchPageData);
					}
					else
					{
						searchSuggestUtilityMethods.setSearchPageData(productSearchPage, searchPageData);
					}
				}
				else
				{
					productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					productSearchPage.setError(MarketplacecommerceservicesConstants.INVALIDSEARCHKEY);
				}

			}
			else if (StringUtils.isNotBlank(typeID))
			{
				if (typeID.startsWith(DROPDOWN_CATEGORY) || typeID.startsWith(DROPDOWN_BRAND) || typeID.startsWith(LSH))
				{
					searchPageData = productsHelper.searchProductsForCategory(typeID, page, pageSize, sortCode);
				}
				else
				{
					searchPageData = productsHelper.searchProductsForSeller(typeID, page, pageSize, sortCode);
				}

				if (isFilter)
				{
					searchSuggestUtilityMethods.setFilterData(productSearchPage, searchPageData);
				}
				else
				{
					searchSuggestUtilityMethods.setSearchPageData(productSearchPage, searchPageData);
				}
			}
			else
			{
				productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				productSearchPage.setError(MarketplacecommerceservicesConstants.INVALIDSEARCHKEY);
			}

			if (StringUtils.isNotEmpty(typeID))
			{
				productSearchPage.setCategoryCode(typeID);
			}
			//For Keyword
			if (StringUtils.isNotEmpty(url))
			{
				productSearchPage.setKeywordRedirectUrl(url);
			}
			/*
			 * final ProductSearchPageWsDto sortingvalues = searchProductsfacatedtonew(searchText, typeID, page, pageSize,
			 * ShowMode.Page, sortCode, fields);
			 */

			final ProductSearchPageWsDto sortingvalues = dataMapper.map(searchPageData, ProductSearchPageWsDto.class, fields);
			if (null != sortingvalues)
			{
				if (null != sortingvalues.getPagination())
				{
					productSearchPage.setPagination(sortingvalues.getPagination());
				}
				if (null != sortingvalues.getSorts())
				{
					productSearchPage.setSorts(sortingvalues.getSorts());
				}
				if (null != sortingvalues.getCurrentQuery())
				{
					productSearchPage.setCurrentQuery(sortingvalues.getCurrentQuery());
				}

			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				productSearchPage.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				productSearchPage.setErrorCode(e.getErrorCode());
			}
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			if (null != e.getErrorMessage())
			{
				productSearchPage.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				productSearchPage.setErrorCode(e.getErrorCode());
			}
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			productSearchPage.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			productSearchPage.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return productSearchPage;
	}

	/**
	 * @description method is called to search products details with respect to categoryId and searchQuery
	 * @param searchQuery
	 * @param page
	 * @param pageSize
	 * @param showMode
	 * @param sortCode
	 * @return ProductSearchPageData
	 */
	public ProductSearchPageData<SearchStateData, ProductData> searchProductslist(final String searchQuery, final int page,
			final int pageSize, final ShowMode showMode, final String sortCode)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		final ProductSearchPageData<SearchStateData, ProductData> sourceResultproduct = productSearchFacade.textSearch(searchState,
				pageableData);
		return sourceResultproduct;
	}

	/**
	 * @description method is called to search products details with respect to categoryId and searchQuery
	 * @param searchText
	 * @param categoryID
	 * @param currentPage
	 * @param pageSize
	 * @param showMode
	 * @param sort
	 * @param fields
	 * @return ProductSearchPageWsDto
	 */

	public ProductSearchPageWsDto searchProductsfacatedtonew(final String searchText, final String categoryID,
			final int currentPage, final int pageSize, final ShowMode showMode, final String sort, final String fields)
	{
		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> sourceResult2 = searchProductslist2(
				searchText, categoryID, currentPage, pageSize, showMode, sort);


		return dataMapper.map(sourceResult2, ProductSearchPageWsDto.class, fields);

	}

	/**
	 * @description method is called to search products details with respect to categoryId and searchQuery
	 * @param searchQuery
	 * @param categoryID
	 * @param page
	 * @param pageSize
	 * @param showMode
	 * @param sortCode
	 * @return ProductCategorySearchPageData
	 */
	public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchProductslist2(final String searchQuery,
			final String categoryID, final int page, final int pageSize, final ShowMode showMode, final String sortCode)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = productSearchFacade
				.categorySearch(categoryID, searchState, pageableData);
		return searchPageData;
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

	@SuppressWarnings("finally")
	@RequestMapping(value = "/compare/{firstProductCode}/{secondProductCode}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public ProductCompareWsDTO compareProducts(@PathVariable final String firstProductCode,
			@PathVariable final String secondProductCode, @RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		ProductCompareWsDTO dto = null;

		final ProductCompareData productCompareData = mplProductCompareFacade.getWebServicesForProductCompare(firstProductCode,
				secondProductCode);

		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		//context.setRecurrencyLevel(countRecurrencyForCatalogData(contentData));
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(ProductCompareData.class, DataMapper.FIELD_PREFIX, fields,
				context);

		dto = dataMapper.map(productCompareData, ProductCompareWsDTO.class, fieldSet);

		return dto;
	}


	@RequestMapping(value = "/collectionSearch", method = RequestMethod.POST, produces = MarketplacecommerceservicesConstants.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductSearchPageWsDto collectionSearch(@RequestParam final String collectionId,
			@RequestParam(required = false) final int page, @RequestParam(required = false) final int pageSize,
			@RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		final PageableData pageableData = createPageableData(page, pageSize, null, ShowMode.Page);
		searchState.setQuery(searchQueryData);
		final ProductSearchPageWsDto productSearchPage = new ProductSearchPageWsDto();
		searchPageData = searchFacade.collectionSearch(collectionId, searchState, pageableData);
		searchSuggestUtilityMethods.setSearchPageData(productSearchPage, searchPageData);

		final ProductSearchPageWsDto sortingvalues = dataMapper.map(searchPageData, ProductSearchPageWsDto.class, fields);
		if (null != sortingvalues)
		{
			if (null != sortingvalues.getPagination())
			{
				productSearchPage.setPagination(sortingvalues.getPagination());
			}
			if (null != sortingvalues.getSorts())
			{
				productSearchPage.setSorts(sortingvalues.getSorts());
			}
			if (null != sortingvalues.getCurrentQuery())
			{
				productSearchPage.setCurrentQuery(sortingvalues.getCurrentQuery());
			}
			if (null != sortingvalues.getSpellingSuggestion())
			{
				productSearchPage.setSpellingSuggestion(searchPageData.getSpellingSuggestion().getSuggestion().replaceAll(EXP, ""));
			}
		}
		return productSearchPage;
	}



	@RequestMapping(value = "/getDepartmentFilter", method = RequestMethod.POST, produces = MarketplacecommerceservicesConstants.APPLICATION_JSON_VALUE)
	@ResponseBody
	public DepartmentHierarchyWs departmentFilter(@RequestParam(required = false) final String searchText,
			@RequestParam(required = false) final String typeID,
			@RequestParam(required = false, defaultValue = "category") final String type)
	{

		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		if (StringUtils.isNotBlank(searchText))
		{
			final PageableData pageableData = createPageableData(0, 24, "relevance", ShowMode.Page);
			//final PageableData pageableData = createPageableData(0, getSearchPageSize(), null, ShowMode.Page);

			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			searchQueryData.setValue(searchText);
			searchState.setQuery(searchQueryData);

			if (typeID != null)
			{
				if (typeID.equalsIgnoreCase("all") && null != pageableData)
				{

					searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
							.textSearch(searchState, pageableData);

				}
				else if (typeID.startsWith("MSH") || typeID.startsWith("MBH") || typeID.startsWith(LSH))
				{

					searchPageData = searchFacade.searchCategorySearch(typeID, searchState, pageableData);

				}
				else
				{

					searchPageData = searchFacade
							.dropDownSearch(searchState, typeID, MarketplaceCoreConstants.SELLER_ID, pageableData);
				}
			}
			//final List<String> filter = new ArrayList<String>();
			//filter.add(
			//"/MSH1:Sales:L0:false:0/MSH12:Electronics:L1:true:0/MSH1214:Appliances:L2:false:0/MSH1214102:Airconditioners and Coolers:L3:false:0/MSH1214102:Airconditioners and Coolers:L4:false:0 ");
			//filter.add("/MSH1:Sales:L1:false:0");
			//filter.add("/MSH12:Electronics:L1:true:0");

			for (final String filter : searchPageData.getDepartmentHierarchyData().getHierarchyList())
			{
				//System.out.println("\n\n\nFilter:" + filter);
				LOG.debug("\n\n\nFilter:" + filter);
			}
			return searchSuggestUtilityMethods
					.getDepartmentHierarchy(searchPageData.getDepartmentHierarchyData().getHierarchyList());
			//return searchSuggestUtilityMethods.getDepartmentHierarchy(filter);
		}
		return null;
	}


	// ######################### TISLUX-356 START

	@RequestMapping(value = "/getBreadcrumb", method = RequestMethod.POST, produces = MarketplacecommerceservicesConstants.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BreadcrumbResponseWsDTO getBreadcrumb(@RequestParam(required = false) final String code,
			@RequestParam(required = false, defaultValue = "category") final String type)
	{
		try
		{
			final BreadcrumbResponseWsDTO breadcrumbData = mplCategoryFacade.getBreadcrumb(code, type);
			return breadcrumbData;
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	// ######################### TISLUX-356 END

	/**
	 * Returns the reviews for a product with a given product code.
	 *
	 * @return product's review list
	 */
	@RequestMapping(value = "/productInfo", method = RequestMethod.GET)
	//@CacheControl(directive = CacheControlDirective.PRIVATE, maxAge = 120)
	//@Cacheable(value = "productCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,#productCode,#fields)")
	@ResponseBody
	public ProductInfoWSDTO getProductInfo(@RequestParam(required = true) final String productCodes,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletRequest request,
			@RequestParam(required = false) final String channel)
	{
		final List<String> productCodeList = new ArrayList<String>();
		ProductDetailMobileWsData productWSData = null;
		final List<ProductDetailMobileWsData> productWSDataList = new ArrayList<ProductDetailMobileWsData>();
		final ProductInfoWSDTO productDTOList = new ProductInfoWSDTO();
		String productCode = "";

		if (productCodes.indexOf(",") != -1)
		{
			productCode = productCodes.toUpperCase();
			for (final String prdStr : productCode.split(","))
			{
				productCodeList.add(prdStr);
			}
		}
		else
		{
			productCode = productCodes.toUpperCase();
			productCodeList.add(productCode);
		}

		try
		{
			URL requestUrl = null;
			if (null != request && null != request.getRequestURL())
			{
				requestUrl = new URL(request.getRequestURL().toString());
			}
			String portString = MarketplacecommerceservicesConstants.EMPTYSPACE;
			String baseUrl = MarketplacecommerceservicesConstants.EMPTYSPACE;
			if (null != requestUrl)
			{
				if (0 != requestUrl.getPort())
				{
					portString = requestUrl.getPort() == -1 ? MarketplacecommerceservicesConstants.EMPTYSPACE
							: MarketplacecommerceservicesConstants.COLON + requestUrl.getPort();
				}
				if (null != requestUrl.getHost() && null != requestUrl.getProtocol())
				{
					baseUrl = requestUrl.getProtocol() + MarketplacecommerceservicesConstants.COLON_SLASH + requestUrl.getHost()
							+ portString + MarketplacecommerceservicesConstants.EMPTYSPACE;
					//+ MarketplacewebservicesConstants.FORGOTPASSWORD_URL;
				}
			}
			for (final String productCodeRef : productCodeList)
			{
				try
				{
					productWSData = mplProductWebService.getProductInfoForProductCode(productCodeRef, baseUrl, channel);
				}
				catch (final Exception e)
				{
					LOG.error("ProductInfo not available for" + productCodeRef);
					ExceptionUtil.getCustomizedExceptionTrace(e);
					continue;
				}

				if (productWSData != null)
				{
					productWSDataList.add(productWSData);
				}
			}
			if (productWSDataList.size() > 0)
			{
				productDTOList.setResults(productWSDataList);
				productDTOList.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				productDTOList.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E9048));
				productDTOList.setErrorCode(MarketplacecommerceservicesConstants.E9048);
				productDTOList.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				productDTOList.setError(e.getErrorMessage());
			}
			productDTOList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				productDTOList.setError(e.getErrorMessage());
			}
			productDTOList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		//TPR-799
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			productDTOList.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			productDTOList.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			productDTOList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return productDTOList;
	}




	@RequestMapping(value = "/{productCode}", params = "isPwa", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PRIVATE, maxAge = 120)
	@Cacheable(value = "productCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,#productCode,#fields)")
	@ResponseBody
	public MplNewProductDetailMobileWsData getProduct(@PathVariable String productCode,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletRequest request,
			@RequestParam(required = false) final String channel) throws MalformedURLException
	{
		MplNewProductDetailMobileWsData product = new MplNewProductDetailMobileWsData();

		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductByCode: code=" + sanitize(productCode) + " | options=" + PRODUCT_OPTIONS);
		}

		try
		{
			URL requestUrl = null;
			if (null != request && null != request.getRequestURL())
			{
				requestUrl = new URL(request.getRequestURL().toString());
			}
			String portString = MarketplacecommerceservicesConstants.EMPTYSPACE;
			String baseUrl = MarketplacecommerceservicesConstants.EMPTYSPACE;
			if (null != requestUrl)
			{
				if (0 != requestUrl.getPort())
				{
					portString = requestUrl.getPort() == -1 ? MarketplacecommerceservicesConstants.EMPTYSPACE
							: MarketplacecommerceservicesConstants.COLON + requestUrl.getPort();
				}
				if (null != requestUrl.getHost() && null != requestUrl.getProtocol())
				{
					baseUrl = requestUrl.getProtocol() + MarketplacecommerceservicesConstants.COLON_SLASH + requestUrl.getHost()
							+ portString + MarketplacecommerceservicesConstants.EMPTYSPACE;
					//+ MarketplacewebservicesConstants.FORGOTPASSWORD_URL;
				}
			}

			product = mplProductWebService.getProductdetails(productCode, baseUrl, channel);
			final ProductAPlusWsData aPlusProductData = mplProductWebService.getAPluscontentForProductCode(productCode);
			if (null != aPlusProductData)
			{
				product.setAPlusContent(aPlusProductData);
			}
			product.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG.toUpperCase());

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				product.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				product.setErrorCode(e.getErrorCode());
			}
			product.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			product.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			if (null != e.getErrorMessage())
			{
				product.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				product.setErrorCode(e.getErrorCode());
			}
		}
		//TPR-799
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			product.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			product.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			product.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}


		return product;
	}





	/**
	 * Returns the reviews for a product with a given product code.
	 *
	 * @return product's review list
	 */
	@RequestMapping(value = "/egvProductInfo", method = RequestMethod.GET)
	//@CacheControl(directive = CacheControlDirective.PRIVATE, maxAge = 120)
	//@Cacheable(value = "productCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,#productCode,#fields)")
	@ResponseBody
	public EgvProductInfoWSDTO getEgvProductInfo(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletRequest request)
	{
		EgvProductInfoWSDTO egvProductData = new EgvProductInfoWSDTO();
		try
		{
			egvProductData = mplProductWebService.getEgvProductDetails();
			if (null != egvProductData)
			{
				egvProductData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				egvProductData = new EgvProductInfoWSDTO();
				egvProductData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				egvProductData.setError(e.getErrorMessage());
			}
			egvProductData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				egvProductData.setError(e.getErrorMessage());
			}
			egvProductData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		//TPR-799
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			egvProductData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			egvProductData.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			egvProductData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return egvProductData;
	}


	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	/**
	 * @param messageSource
	 *           the messageSource to set
	 */
	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	/**
	 * @return the i18nService
	 */
	public I18NService getI18nService()
	{
		return i18nService;
	}

	/**
	 * @param i18nService
	 *           the i18nService to set
	 */
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	//NU-38 start
	@RequestMapping(value = "/searchProducts", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MarketplacecommerceservicesConstants.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductSearchPageWsDto searchProductsNew(@RequestParam(required = false) final String searchText,
			@RequestParam(required = false) final int page, @RequestParam(required = false) final int pageSize,
			@RequestParam(required = false) final String sortCode, @RequestParam(required = false) final boolean isFilter,
			@RequestParam(required = false) final boolean isPwa, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final ProductSearchPageWsDto productSearchPage = new ProductSearchPageWsDto();
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;

		try
		{
			final PageableData pageableData = createPageableData(page, pageSize, sortCode, ShowMode.Page);
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			searchQueryData.setValue(searchText);
			searchState.setQuery(searchQueryData);
			searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
					.textSearch(searchState, pageableData);
			//PR-23 start
			if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
			{
				if (StringUtils.isNotEmpty(searchText))
				{
					final String[] elements = searchText.trim().split(BACKSLASH_S);

					if (elements.length >= 2)
					{
						searchState.setNextSearch(true);

						searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
								.textSearch(searchState, pageableData);

					}
				}
			}
			//PR-23 end
			if (null != searchPageData.getSpellingSuggestion()
					&& StringUtils.isNotEmpty(searchPageData.getSpellingSuggestion().getSuggestion()))
			{
				productSearchPage.setSpellingSuggestion(searchPageData.getSpellingSuggestion().getSuggestion().replaceAll(EXP, ""));
				final SearchStateData searchStateAll = new SearchStateData();
				final SearchQueryData searchQueryDataAll = new SearchQueryData();
				searchQueryDataAll.setValue(searchPageData.getSpellingSuggestion().getSuggestion().replaceAll("[()]+", ""));
				searchStateAll.setQuery(searchQueryDataAll);

				searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) searchFacade.textSearch(
						searchStateAll, pageableData);
			}

			if (isFilter)
			{
				searchSuggestUtilityMethods.setFilterWsData(productSearchPage, searchPageData);
			}
			else
			{
				searchSuggestUtilityMethods.setSearchPageWsData(productSearchPage, searchPageData);
			}
			final ProductSearchPageWsDto sortingvalues = dataMapper.map(searchPageData, ProductSearchPageWsDto.class, fields);
			if (null != sortingvalues)
			{
				if (null != sortingvalues.getPagination())
				{
					final PaginationWsDTO pagination = sortingvalues.getPagination();

					final PaginationWsDTO paginationWsDTO = new PaginationWsDTO();

					paginationWsDTO.setCurrentPage(pagination.getCurrentPage());
					paginationWsDTO.setPageSize(pagination.getPageSize());
					paginationWsDTO.setTotalPages(pagination.getTotalPages());
					paginationWsDTO.setTotalResults(pagination.getTotalResults());

					productSearchPage.setPagination(paginationWsDTO);


				}
				if (null != sortingvalues.getSorts())
				{
					productSearchPage.setSorts(sortingvalues.getSorts());
				}
				if (null != sortingvalues.getCurrentQuery())
				{
					final SearchStateWsDTO currentQuery = new SearchStateWsDTO();

					currentQuery.setUrl(sortingvalues.getCurrentQuery().getUrl());
					currentQuery.setQuery(sortingvalues.getCurrentQuery().getQuery());

					currentQuery.setAppliedSort(sortingvalues.getPagination().getSort());

					final String query = sortingvalues.getCurrentQuery().getQuery().getValue();

					final String[] arr = query.split(":");

					if (arr.length > 2)
					{
						currentQuery.setAppliedFilters(query.substring(query.indexOf(":", query.indexOf(":") + 1) + 1));
						currentQuery.setSearchQuery(arr[0]);
					}
					else if (arr.length == 2 || query.indexOf(":", query.indexOf(":") + 1) == -1)
					{
						currentQuery.setSearchQuery(arr[0]);
						currentQuery.setAppliedFilters(" ");
					}
					else if (arr.length == 0)
					{
						if (StringUtils.isNotEmpty(query))
						{
							currentQuery.setSearchQuery(query);
						}
					}

					productSearchPage.setCurrentQuery(currentQuery);
				}

			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				productSearchPage.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				productSearchPage.setErrorCode(e.getErrorCode());
			}
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			if (null != e.getErrorMessage())
			{
				productSearchPage.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				productSearchPage.setErrorCode(e.getErrorCode());
			}
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			productSearchPage.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			productSearchPage.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return productSearchPage;
	}

	//NU-38 end

	@RequestMapping(value = "/getFollowedBrands", method = RequestMethod.GET, produces = MarketplacecommerceservicesConstants.APPLICATION_JSON_VALUE)
	@ResponseBody
	public MplFollowedBrandsWsDto getFollowedBrands(final String fields, @RequestParam(required = true) final String gender,
			@RequestParam(required = false) final boolean isPwa)


	{
		final MplFollowedBrandsWsDto mplFollowedBrandsWsDto = new MplFollowedBrandsWsDto();

		try
		{
			List<FollowedBrandWsDto> followedBrandList = new ArrayList<FollowedBrandWsDto>();

			followedBrandList = mplFollowedBrandFacade.getFollowedBrands(gender);

			if (CollectionUtils.isNotEmpty(followedBrandList))
			{
				mplFollowedBrandsWsDto.setFollowedBrandList(followedBrandList);
			}
			else
			{
				mplFollowedBrandsWsDto.setStatus(MarketplacewebservicesConstants.FAILURE);
				mplFollowedBrandsWsDto.setMessage(Localization.getLocalizedString(MarketplacecommerceservicesConstants.NU350));
				mplFollowedBrandsWsDto.setErrorCode(MarketplacecommerceservicesConstants.NU350);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(FOLLOW_BRAND_ERROR + e.getMessage());
			if (null != e.getErrorMessage())
			{
				mplFollowedBrandsWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				mplFollowedBrandsWsDto.setErrorCode(e.getErrorCode());
			}
			mplFollowedBrandsWsDto.setStatus(MarketplacewebservicesConstants.FAILURE);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			LOG.error(FOLLOW_BRAND_ERROR + e.getMessage());
			mplFollowedBrandsWsDto.setMessage(Localization.getLocalizedString(MarketplacecommerceservicesConstants.NU350));
			mplFollowedBrandsWsDto.setErrorCode(MarketplacecommerceservicesConstants.NU350);
			mplFollowedBrandsWsDto.setStatus(MarketplacewebservicesConstants.FAILURE);

		}
		return mplFollowedBrandsWsDto;


	}


	@RequestMapping(value = "{mcvId}/updateFollowedBrands", method = RequestMethod.POST, produces = MarketplacecommerceservicesConstants.APPLICATION_JSON_VALUE)
	@ResponseBody
	public MplFollowedBrandsWsDto updateFollowedBrands(@PathVariable final String mcvId, final String fields,
			@RequestParam(required = true) final String brands, @RequestParam(required = true) final boolean follow,
			@RequestParam(required = false) final boolean isPwa)


	{
		final MplFollowedBrandsWsDto mplFollowedBrandsWsDto = new MplFollowedBrandsWsDto();
		try
		{
			final boolean status = mplFollowedBrandFacade.updateFollowedBrands(mcvId, brands, follow);

			if (status)
			{
				mplFollowedBrandsWsDto.setStatus(MarketplacewebservicesConstants.SUCCESS);
				mplFollowedBrandsWsDto.setMessage(Localization.getLocalizedString(MarketplacecommerceservicesConstants.NU450));
			}
			else
			{
				mplFollowedBrandsWsDto.setStatus(MarketplacewebservicesConstants.FAILURE);
				mplFollowedBrandsWsDto.setMessage(Localization.getLocalizedString(MarketplacecommerceservicesConstants.NU250));
				mplFollowedBrandsWsDto.setErrorCode(MarketplacecommerceservicesConstants.NU250);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(FOLLOW_BRAND_ERROR + e.getMessage());
			if (null != e.getErrorMessage())
			{
				mplFollowedBrandsWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				mplFollowedBrandsWsDto.setErrorCode(e.getErrorCode());
			}
			mplFollowedBrandsWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			LOG.error(FOLLOW_BRAND_ERROR + e.getMessage());
			mplFollowedBrandsWsDto.setMessage(Localization.getLocalizedString(MarketplacecommerceservicesConstants.NU250));
			mplFollowedBrandsWsDto.setErrorCode(MarketplacecommerceservicesConstants.NU250);
			mplFollowedBrandsWsDto.setStatus(MarketplacecommerceservicesConstants.NU250);

		}
		return mplFollowedBrandsWsDto;
	}

	/**
	 * @return the mplFollowedBrandFacade
	 */
	public MplFollowedBrandFacade getMplFollowedBrandFacade()
	{
		return mplFollowedBrandFacade;
	}

	/**
	 * @param mplFollowedBrandFacade
	 *           the mplFollowedBrandFacade to set
	 */
	public void setMplFollowedBrandFacade(final MplFollowedBrandFacade mplFollowedBrandFacade)
	{
		this.mplFollowedBrandFacade = mplFollowedBrandFacade;
	}
}
