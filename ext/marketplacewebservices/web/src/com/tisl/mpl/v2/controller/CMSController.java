/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetBuilder;
import de.hybris.platform.commercewebservicescommons.mapping.impl.FieldSetBuilderContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.data.HelpmeShopCategoryData;
import com.tisl.mpl.data.HelpmeShopData;
import com.tisl.mpl.facade.cms.MplCmsFacade;
import com.tisl.mpl.facade.helpmeshop.impl.DefaultHelpMeShopFacadeImpl;
import com.tisl.mpl.facades.cms.data.CollectionPageData;
import com.tisl.mpl.facades.cms.data.HeroProductData;
import com.tisl.mpl.facades.cms.data.MplPageData;
import com.tisl.mpl.facades.cms.data.PageData;
import com.tisl.mpl.utility.SearchSuggestUtilityMethods;
import com.tisl.mpl.v2.helper.ProductsHelper;
import com.tisl.mpl.wsdto.CollectionPageWsDTO;
import com.tisl.mpl.wsdto.HelpmeShopCategoryWsDTO;
import com.tisl.mpl.wsdto.HelpmeShopWsDTO;
import com.tisl.mpl.wsdto.HeroProductWsDTO;
import com.tisl.mpl.wsdto.MplPageComponentsWsDTO;
import com.tisl.mpl.wsdto.MplPageWsDTO;
import com.tisl.mpl.wsdto.PageWsDTO;
import com.tisl.mpl.wsdto.ProductSearchPageWsDto;


/**
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/cms")
public class CMSController extends BaseController
{
	private final String MOBILE_DISCOVER_UID = "MobileHomepageDiscover";
	private final String MOBILE_SHOWCASE_UID = "MobileHomepageShowCase";
	//private final String MOBILE_HOMEPAGE_UID = "MobileHomepage";	//SONAR Fix
	//private static final String MOBILE_BRANDPAGE_UID = "MobileBrandPage";	//SONAR Fix
	//private static final String MOBILE_CATEGORYPAGE_UID = "MobileCategoryPage";	//SONAR Fix
	private static final String MOBILE_DEALSBANNERPAGE_UID = "MobileDealsBannerPage";
	private static final String MOBILE_DEALSPRODUCTPAGE_UID = "MobileDealsProductPage";

	/*
	 * private static final Set<CatalogOption> OPTIONS;
	 *
	 * static { OPTIONS = getOptions(); }
	 */

	@Resource(name = "helpMeShopFacade")
	private DefaultHelpMeShopFacadeImpl helpMeShopFacade;

	@Resource(name = "mplCmsFacade")
	private MplCmsFacade mplCmsFacade;

	@Resource(name = "fieldSetBuilder")
	private FieldSetBuilder fieldSetBuilder;

	@Resource(name = "productsHelper")
	private ProductsHelper productsHelper;

	@Autowired
	private SearchSuggestUtilityMethods searchSuggestUtilityMethods;


	private static final String DEFAULT = "DEFAULT";

	private static final String CHANNEL = "mobile";

	private static final Logger LOG = Logger.getLogger(CMSController.class);


	@RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
	@ResponseBody
	public PageWsDTO getCatalog(@PathVariable final String categoryId, @RequestParam(defaultValue = DEFAULT) final String fields)
			throws CMSItemNotFoundException, NullPointerException
	{
		PageWsDTO dto = null;
		try
		{
			final PageData contentData = mplCmsFacade.getCategoryLandingPageForMobile(categoryId);


			final FieldSetBuilderContext context = new FieldSetBuilderContext();
			//context.setRecurrencyLevel(countRecurrencyForCatalogData(contentData));
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(PageWsDTO.class, DataMapper.FIELD_PREFIX, fields, context);

			dto = dataMapper.map(contentData, PageWsDTO.class, fieldSet);
			dto.setListing(null);
		}
		catch (final Exception e)
		{
			//e.printStackTrace();
			LOG.error(" getCatalog Exception is : ", e);

			final PageData contentData = mplCmsFacade.populatePageType(categoryId, false, "listing");

			final FieldSetBuilderContext context = new FieldSetBuilderContext();
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(PageWsDTO.class, DataMapper.FIELD_PREFIX, fields, context);

			dto = dataMapper.map(contentData, PageWsDTO.class, fieldSet);

			final ProductSearchPageWsDto productSearchPage = new ProductSearchPageWsDto();

			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = productsHelper
					.searchProductsForCategory(categoryId, 0, 20, "relevance");

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
					productSearchPage.setSpellingSuggestion(sortingvalues.getSpellingSuggestion());
				}
			}

			dto.setListing(productSearchPage);
			dto.setTitle(mplCmsFacade.getCategoryNameForCode(categoryId));

			final HeroProductData heroProductData = mplCmsFacade.getHeroProducts(categoryId);
			final Set<String> fieldSetHero = fieldSetBuilder.createFieldSet(HeroProductWsDTO.class, DataMapper.FIELD_PREFIX, fields,
					context);
			final HeroProductWsDTO heroProductWsDto = dataMapper.map(heroProductData, HeroProductWsDTO.class, fieldSetHero);
			dto.setHeroProducts(heroProductWsDto);

		}
		//Removed Finally block for sonar fixes
		return dto;


	}

	/*
	 * private static Set<CatalogOption> getOptions() { final Set<CatalogOption> opts = new HashSet<>();
	 * opts.add(CatalogOption.BASIC); opts.add(CatalogOption.CATEGORIES); opts.add(CatalogOption.SUBCATEGORIES); return
	 * opts; }
	 */

	@RequestMapping(value = "/homepage", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public MplPageWsDTO getHomepage(@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		MplPageWsDTO dto;
		final MplPageData homePageData = mplCmsFacade.getHomePageForMobile();
		final FieldSetBuilderContext context = new FieldSetBuilderContext();

		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(MplPageData.class, DataMapper.FIELD_PREFIX, fields, context);
		dto = dataMapper.map(homePageData, MplPageWsDTO.class, fieldSet);
		return dto;

	}

	@RequestMapping(value = "/homepage/discover", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public MplPageComponentsWsDTO getHomepageDiscover(@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		final List<MplPageWsDTO> dtos = new ArrayList<MplPageWsDTO>();
		final MplPageComponentsWsDTO components = new MplPageComponentsWsDTO();
		final List<MplPageData> homePageDatas = mplCmsFacade.getPageInformationForPageId(MOBILE_DISCOVER_UID);
		final List<Date> pageLastModifiedTime = new ArrayList<Date>();
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		for (final MplPageData homePageData : homePageDatas)
		{
			pageLastModifiedTime.add(homePageData.getLastModifiedTime());
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(MplPageData.class, DataMapper.FIELD_PREFIX, fields, context);
			final MplPageWsDTO dto = dataMapper.map(homePageData, MplPageWsDTO.class, fieldSet);
			dtos.add(dto);
		}
		Collections.sort(pageLastModifiedTime);
		if (!pageLastModifiedTime.isEmpty())
		{
			components
					.setLastModifiedTime(getFormatedLastModifiedDateTime(pageLastModifiedTime.get(pageLastModifiedTime.size() - 1)));
		}
		components.setMplPageComponent(dtos);
		return components;

	}

	// this method is used for getting the string format of date.
	public String getFormatedLastModifiedDateTime(final Date date)
	{
		final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		final String lastValue = formatter.format(date);
		return lastValue;
	}

	@RequestMapping(value = "/homepage/showcase", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public MplPageComponentsWsDTO getHomepageShowcase(@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		final List<MplPageWsDTO> dtos = new ArrayList<MplPageWsDTO>();
		final MplPageComponentsWsDTO components = new MplPageComponentsWsDTO();
		final List<MplPageData> homePageDatas = mplCmsFacade.getPageInformationForPageId(MOBILE_SHOWCASE_UID);
		final List<Date> pageLastModifiedTime = new ArrayList<Date>();
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		for (final MplPageData homePageData : homePageDatas)
		{
			pageLastModifiedTime.add(homePageData.getLastModifiedTime());
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(MplPageData.class, DataMapper.FIELD_PREFIX, fields, context);
			final MplPageWsDTO dto = dataMapper.map(homePageData, MplPageWsDTO.class, fieldSet);
			dtos.add(dto);
		}
		Collections.sort(pageLastModifiedTime);
		if (!pageLastModifiedTime.isEmpty())
		{
			components
					.setLastModifiedTime(getFormatedLastModifiedDateTime(pageLastModifiedTime.get(pageLastModifiedTime.size() - 1)));
		}
		components.setMplPageComponent(dtos);
		return components;
	}

	@RequestMapping(value = "/brand/{brandID:.*}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public MplPageComponentsWsDTO getBrandPage(@PathVariable("brandID") final String brandID,
			@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		final List<MplPageWsDTO> dtos = new ArrayList<MplPageWsDTO>();
		final MplPageComponentsWsDTO components = new MplPageComponentsWsDTO();
		final List<MplPageData> mplPageDatas = mplCmsFacade.getPageInformationForPageId(brandID);
		final List<Date> pageLastModifiedTime = new ArrayList<Date>();
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		for (final MplPageData mplPageData : mplPageDatas)
		{
			pageLastModifiedTime.add(mplPageData.getLastModifiedTime());
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(MplPageData.class, DataMapper.FIELD_PREFIX, fields, context);
			final MplPageWsDTO dto = dataMapper.map(mplPageData, MplPageWsDTO.class, fieldSet);
			dtos.add(dto);
		}
		Collections.sort(pageLastModifiedTime);
		if (!pageLastModifiedTime.isEmpty())
		{
			components
					.setLastModifiedTime(getFormatedLastModifiedDateTime(pageLastModifiedTime.get(pageLastModifiedTime.size() - 1)));
		}
		components.setMplPageComponent(dtos);
		return components;
	}

	@RequestMapping(value = "/mplcategory/{categoryID:.*}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public MplPageComponentsWsDTO getCategoryPage(@PathVariable("categoryID") final String categoryID,
			@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		final List<MplPageWsDTO> dtos = new ArrayList<MplPageWsDTO>();
		final MplPageComponentsWsDTO components = new MplPageComponentsWsDTO();
		final List<MplPageData> mplPageDatas = mplCmsFacade.getPageInformationForPageId(categoryID);
		final List<Date> pageLastModifiedTime = new ArrayList<Date>();
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		for (final MplPageData mplPageData : mplPageDatas)
		{
			pageLastModifiedTime.add(mplPageData.getLastModifiedTime());
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(MplPageData.class, DataMapper.FIELD_PREFIX, fields, context);
			final MplPageWsDTO dto = dataMapper.map(mplPageData, MplPageWsDTO.class, fieldSet);
			dtos.add(dto);
		}
		Collections.sort(pageLastModifiedTime);
		if (!pageLastModifiedTime.isEmpty())
		{
			components
					.setLastModifiedTime(getFormatedLastModifiedDateTime(pageLastModifiedTime.get(pageLastModifiedTime.size() - 1)));
		}
		components.setMplPageComponent(dtos);
		return components;
	}

	@RequestMapping(value = "/deals/banners", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public MplPageComponentsWsDTO getDeasBannerPage(@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		final List<MplPageWsDTO> dtos = new ArrayList<MplPageWsDTO>();
		final MplPageComponentsWsDTO components = new MplPageComponentsWsDTO();
		final List<MplPageData> mplPageDatas = mplCmsFacade.getPageInformationForPageId(MOBILE_DEALSBANNERPAGE_UID);
		final List<Date> pageLastModifiedTime = new ArrayList<Date>();
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		for (final MplPageData mplPageData : mplPageDatas)
		{
			pageLastModifiedTime.add(mplPageData.getLastModifiedTime());
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(MplPageData.class, DataMapper.FIELD_PREFIX, fields, context);
			final MplPageWsDTO dto = dataMapper.map(mplPageData, MplPageWsDTO.class, fieldSet);
			dtos.add(dto);
		}

		Collections.sort(pageLastModifiedTime);
		if (!pageLastModifiedTime.isEmpty())
		{
			components
					.setLastModifiedTime(getFormatedLastModifiedDateTime(pageLastModifiedTime.get(pageLastModifiedTime.size() - 1)));
		}
		components.setMplPageComponent(dtos);
		return components;
	}

	@RequestMapping(value = "/deals/products", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public MplPageComponentsWsDTO getDealsProductPage(@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		final List<MplPageWsDTO> dtos = new ArrayList<MplPageWsDTO>();
		final MplPageComponentsWsDTO components = new MplPageComponentsWsDTO();
		final List<MplPageData> mplPageDatas = mplCmsFacade.getPageInformationForPageId(MOBILE_DEALSPRODUCTPAGE_UID);
		final List<Date> pageLastModifiedTime = new ArrayList<Date>();
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		for (final MplPageData mplPageData : mplPageDatas)
		{
			pageLastModifiedTime.add(mplPageData.getLastModifiedTime());
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(MplPageData.class, DataMapper.FIELD_PREFIX, fields, context);
			final MplPageWsDTO dto = dataMapper.map(mplPageData, MplPageWsDTO.class, fieldSet);
			dtos.add(dto);
		}
		Collections.sort(pageLastModifiedTime);
		if (!pageLastModifiedTime.isEmpty())
		{
			components
					.setLastModifiedTime(getFormatedLastModifiedDateTime(pageLastModifiedTime.get(pageLastModifiedTime.size() - 1)));
		}
		components.setMplPageComponent(dtos);
		return components;
	}


	@RequestMapping(value = "/helpmeshop", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public HelpmeShopWsDTO getHelpmeShop(@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		HelpmeShopWsDTO dto;
		final HelpmeShopData helpmeShopData = helpMeShopFacade.getWebServiceForConceirgeSearch();
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(HelpmeShopData.class, DataMapper.FIELD_PREFIX, fields, context);
		dto = dataMapper.map(helpmeShopData, HelpmeShopWsDTO.class, fieldSet);
		return dto;
	}


	@RequestMapping(value = "/helpmeshopcategory/{categoryId}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public HelpmeShopCategoryWsDTO getHelpmeShopCategory(@PathVariable final String categoryId,
			@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		HelpmeShopCategoryWsDTO dto;
		final HelpmeShopCategoryData helpmeShopCategoryData = helpMeShopFacade.getWebServicesForConceirgeCategory(categoryId);
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(HelpmeShopCategoryData.class, DataMapper.FIELD_PREFIX, fields,
				context);
		dto = dataMapper.map(helpmeShopCategoryData, HelpmeShopCategoryWsDTO.class, fieldSet);
		//Removed Finally block for sonar fixes
		return dto;
	}

	@RequestMapping(value = "/shopbylook/{collectionId}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public CollectionPageWsDTO getHelpmeShopCollection(@PathVariable final String collectionId,
			@RequestParam(defaultValue = DEFAULT) final String fields, final HttpServletRequest request)
	{
		CollectionPageWsDTO dto = null;
		CollectionPageData helpmeShopCategoryData;
		try
		{
			helpmeShopCategoryData = mplCmsFacade.populateCollectionPage(collectionId, request);
			final FieldSetBuilderContext context = new FieldSetBuilderContext();
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(CollectionPageWsDTO.class, DataMapper.FIELD_PREFIX, fields,
					context);
			dto = dataMapper.map(helpmeShopCategoryData, CollectionPageWsDTO.class, fieldSet);

		}
		catch (final CMSItemNotFoundException e)
		{
			// YTODO Auto-generated catch block
			//e.printStackTrace();
			LOG.error("CMSItemNotFoundException : ", e);
		}
		catch (final NullPointerException e)
		{
			// YTODO Auto-generated catch block
			//e.printStackTrace();
			LOG.error("NullPointerException : ", e);
		}
		//Removed Finally block for sonar fixes
		return dto;


	}

	@RequestMapping(value = "/seller/{sellerId}", method = RequestMethod.GET)
	@ResponseBody
	public PageWsDTO getCatalogForSeller(@PathVariable final String sellerId,
			@RequestParam(defaultValue = DEFAULT) final String fields) throws CMSItemNotFoundException, NullPointerException
	{
		PageWsDTO dto = null;
		try
		{
			final PageData contentData = mplCmsFacade.getSellerLandingPageForMobile(sellerId);


			final FieldSetBuilderContext context = new FieldSetBuilderContext();
			//context.setRecurrencyLevel(countRecurrencyForCatalogData(contentData));
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(PageWsDTO.class, DataMapper.FIELD_PREFIX, fields, context);

			dto = dataMapper.map(contentData, PageWsDTO.class, fieldSet);
			dto.setListing(null);
		}
		catch (final Exception e)
		{
			//e.printStackTrace();
			LOG.error(" getCatalogForSeller Exception : ", e);

			final PageData contentData = mplCmsFacade.populateSellerPageType(sellerId, "listing");

			final FieldSetBuilderContext context = new FieldSetBuilderContext();
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(PageWsDTO.class, DataMapper.FIELD_PREFIX, fields, context);

			dto = dataMapper.map(contentData, PageWsDTO.class, fieldSet);

			final ProductSearchPageWsDto productSearchPage = new ProductSearchPageWsDto();

			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = productsHelper
					.searchProductsForSeller(sellerId, 0, 20, "relevance");

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
					productSearchPage.setSpellingSuggestion(sortingvalues.getSpellingSuggestion());
				}
			}

			dto.setListing(productSearchPage);
			dto.setTitle(mplCmsFacade.getSellerMasterName(sellerId));

			//final HeroProductData heroProductData = mplCmsFacade.getHeroProducts(categoryId);
			//final Set<String> fieldSetHero = fieldSetBuilder.createFieldSet(HeroProductWsDTO.class, DataMapper.FIELD_PREFIX, fields,
			//context);
			//final HeroProductWsDTO heroProductWsDto = dataMapper.map(heroProductData, HeroProductWsDTO.class, fieldSetHero);
			//dto.setHeroProducts(heroProductWsDto);

		}
		//Removed Finally block for sonar fixes
		return dto;


	}

	@RequestMapping(value = "/offer/{categoryId}", method = RequestMethod.GET)
	@ResponseBody
	public PageWsDTO getOfferListing(
			//@PathVariable final String offerId,
			@PathVariable final String categoryId, @RequestParam(value = "offer", required = true) final String offerID,
			@RequestParam(defaultValue = DEFAULT) final String fields) throws CMSItemNotFoundException, NullPointerException
	{
		PageWsDTO dto = null;

		final PageData contentData = mplCmsFacade.populateOfferPageType(offerID, "listing");

		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(PageWsDTO.class, DataMapper.FIELD_PREFIX, fields, context);

		dto = dataMapper.map(contentData, PageWsDTO.class, fieldSet);

		final ProductSearchPageWsDto productSearchPage = new ProductSearchPageWsDto();

		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = productsHelper
				.searchProductsForOffer(offerID, 0, 20, "relevance", categoryId, CHANNEL);

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
				productSearchPage.setSpellingSuggestion(sortingvalues.getSpellingSuggestion());
			}
		}

		dto.setListing(productSearchPage);
		return dto;

	}
}
