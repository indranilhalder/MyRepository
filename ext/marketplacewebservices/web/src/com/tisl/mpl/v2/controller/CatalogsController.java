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

import de.hybris.platform.commercefacades.catalog.CatalogFacade;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CatalogData;
import de.hybris.platform.commercefacades.catalog.data.CatalogVersionData;
import de.hybris.platform.commercefacades.catalog.data.CatalogsData;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.commercewebservicescommons.dto.catalog.CatalogListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.catalog.CatalogVersionWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.catalog.CatalogWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.catalog.CategoryHierarchyWsDTO;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetBuilder;
import de.hybris.platform.commercewebservicescommons.mapping.impl.FieldSetBuilderContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.category.data.LandingDetailsforCategoryData;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.service.MplCustomBrandService;
import com.tisl.mpl.service.MplCustomCategoryService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.BrandListHierarchyData;
import com.tisl.mpl.wsdto.BrandListHierarchyWsDTO;
import com.tisl.mpl.wsdto.DepartmentListHierarchyData;
import com.tisl.mpl.wsdto.DepartmentListHierarchyWsDTO;
import com.tisl.mpl.wsdto.ProductForCategoryData;
import com.tisl.mpl.wsdto.ProductForCategoryWsDTO;


/**
 *
 * @pathparam catalogId Catalog identifier
 * @pathparam catalogVersionId Catalog version identifier
 * @pathparam categoryId Category identifier
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/catalogs")
public class CatalogsController extends BaseController
{
	private static final Set<CatalogOption> OPTIONS;
	static
	{
		OPTIONS = getOptions();
	}

	@Resource(name = "cwsCatalogFacade")
	private CatalogFacade catalogFacade;
	@Resource(name = "fieldSetBuilder")
	private FieldSetBuilder fieldSetBuilder;
	//Shop by Brand Mobile
	@Resource(name = "mplCustomBrandService")
	private MplCustomBrandService mplCustomBrandService;
	//Shop by dept
	@Resource(name = "mplCustomCategoryService")
	private MplCustomCategoryService mplCustomCategoryService;

	private static final Logger LOG = Logger.getLogger(CatalogsController.class);

	/**
	 * Returns all catalogs with versions defined for the base store.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return All catalogs defined for the base store.
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public CatalogListWsDTO getCatalogs(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<CatalogData> catalogDataList = catalogFacade.getAllProductCatalogsForCurrentSite(OPTIONS);
		final CatalogsData catalogsData = new CatalogsData();
		catalogsData.setCatalogs(catalogDataList);

		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		context.setRecurrencyLevel(countRecurrecyLevel(catalogDataList));
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(CatalogListWsDTO.class, DataMapper.FIELD_PREFIX, fields,
				context);

		final CatalogListWsDTO catalogListDto = dataMapper.map(catalogsData, CatalogListWsDTO.class, fieldSet);
		return catalogListDto;
	}


	/**
	 * Returns a information about a catalog based on its ID, along with versions defined for the current base store.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Catalog structure
	 */
	@RequestMapping(value = "/{catalogId}", method = RequestMethod.GET)
	@ResponseBody
	public CatalogWsDTO getCatalog(@PathVariable final String catalogId,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CatalogData catalogData = catalogFacade.getProductCatalogForCurrentSite(catalogId, OPTIONS);

		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		context.setRecurrencyLevel(countRecurrencyForCatalogData(catalogData));
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(CatalogWsDTO.class, DataMapper.FIELD_PREFIX, fields, context);

		final CatalogWsDTO dto = dataMapper.map(catalogData, CatalogWsDTO.class, fieldSet);
		return dto;
	}

	// Mobile all Category
	/**
	 * To get all categories Shop by department component
	 *
	 * @param fields
	 * @return DepartmentListHierarchyWsDTO
	 */
	@RequestMapping(value = "/getAllCategories", method = RequestMethod.GET)
	@ResponseBody
	public DepartmentListHierarchyWsDTO fetchAllCategories(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		DepartmentListHierarchyData shopByDeptData = new DepartmentListHierarchyData();
		DepartmentListHierarchyWsDTO deptListDto = new DepartmentListHierarchyWsDTO();
		try
		{
			shopByDeptData = mplCustomCategoryService.getAllCategories();
			final FieldSetBuilderContext context = new FieldSetBuilderContext();
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(DepartmentListHierarchyData.class, DataMapper.FIELD_PREFIX,
					fields, context);
			if (null != shopByDeptData && null != fieldSet)
			{
				deptListDto = dataMapper.map(shopByDeptData, DepartmentListHierarchyWsDTO.class, fieldSet);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				deptListDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				deptListDto.setErrorCode(e.getErrorCode());
			}
			deptListDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				deptListDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				deptListDto.setErrorCode(e.getErrorCode());
			}
			deptListDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return deptListDto;
	}


	/**
	 * To get all brands Shop by brand component
	 *
	 * @param fields
	 * @return BrandListHierarchyWsDTO
	 */
	@RequestMapping(value = "/allBrands", method = RequestMethod.GET)
	@ResponseBody
	public BrandListHierarchyWsDTO getAllBrands(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		BrandListHierarchyData shopbydeptdata = new BrandListHierarchyData();
		BrandListHierarchyWsDTO brandListdto = new BrandListHierarchyWsDTO();
		try
		{
			shopbydeptdata = mplCustomBrandService.getShopByBrand();
			final FieldSetBuilderContext context = new FieldSetBuilderContext();
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(BrandListHierarchyWsDTO.class, DataMapper.FIELD_PREFIX,
					fields, context);

			if (null != shopbydeptdata && null != fieldSet)
			{
				brandListdto = dataMapper.map(shopbydeptdata, BrandListHierarchyWsDTO.class, fieldSet);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				brandListdto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				brandListdto.setErrorCode(e.getErrorCode());
			}
			brandListdto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				brandListdto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				brandListdto.setErrorCode(e.getErrorCode());
			}
			brandListdto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return brandListdto;
	}

	/**
	 * get product details for a category
	 *
	 * @param categoryId
	 * @param fields
	 * @return ProductForCategoryWsDTO
	 */
	@RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
	@ResponseBody
	public ProductForCategoryWsDTO getProductForCategory(

	@PathVariable final String categoryId, @RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		final ProductForCategoryData productForCategory = mplCustomCategoryService.getCategoryforCategoryid(categoryId);
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(ProductForCategoryWsDTO.class, DataMapper.FIELD_PREFIX, fields,
				context);
		final ProductForCategoryWsDTO dto = dataMapper.map(productForCategory, ProductForCategoryWsDTO.class, fieldSet);
		return dto;
	}

	/**
	 * Returns information about category landing page.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Information about category landing page
	 */

	@RequestMapping(value = "/DetailsForCategoryLandingPage/{categoryId}", method = RequestMethod.GET)
	@ResponseBody
	public LandingDetailsforCategoryData getDetailsForCategoryLandingPage(

	@PathVariable final String categoryId, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		try
		{
			final LandingDetailsforCategoryData detailsForCategoryLanding = mplCustomCategoryService

			.getCategoryforCategoryNameUsingId(categoryId);



			return detailsForCategoryLanding;
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			throw new EtailNonBusinessExceptions(e);
		}
	}

	/**
	 * Returns information about catalog version that exists for the current base store.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Information about catalog version
	 */
	@RequestMapping(value = "/{catalogId}/{catalogVersionId}", method = RequestMethod.GET)
	@ResponseBody
	public CatalogVersionWsDTO getCatalogVersion(@PathVariable final String catalogId,
			@PathVariable final String catalogVersionId, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CatalogVersionData catalogVersionData = catalogFacade.getProductCatalogVersionForTheCurrentSite(catalogId,
				catalogVersionId, OPTIONS);

		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		context.setRecurrencyLevel(countRecurrencyForCatalogVersionData(catalogVersionData));
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(CatalogVersionWsDTO.class, DataMapper.FIELD_PREFIX, fields,
				context);

		final CatalogVersionWsDTO dto = dataMapper.map(catalogVersionData, CatalogVersionWsDTO.class, fieldSet);
		return dto;
	}

	/**
	 * Returns information about category that exists in a catalog version available for the current base store.
	 *
	 * @queryparam currentPage The current result page requested.
	 * @queryparam pageSize The number of results returned per page.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Information about category
	 */
	@RequestMapping(value = "/{catalogId}/{catalogVersionId}/categories/{categoryId}", method = RequestMethod.GET)
	@ResponseBody
	public CategoryHierarchyWsDTO getCategories(@PathVariable final String catalogId, @PathVariable final String catalogVersionId,
			@PathVariable final String categoryId,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(defaultValue = "DEFAULT,totalNumber,pageSize,numberOfPages,currentPage") final String fields)
	{
		final PageOption page = PageOption.createForPageNumberAndPageSize(currentPage, pageSize);
		final CategoryHierarchyData categoryHierarchyData = catalogFacade.getCategoryById(catalogId, catalogVersionId, categoryId,
				page, OPTIONS);

		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		context.setRecurrencyLevel(countRecurrencyForCategoryHierarchyData(1, categoryHierarchyData));
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(CategoryHierarchyWsDTO.class, DataMapper.FIELD_PREFIX, fields,
				context);

		final CategoryHierarchyWsDTO dto = dataMapper.map(categoryHierarchyData, CategoryHierarchyWsDTO.class, fieldSet);
		return dto;
	}

	private static Set<CatalogOption> getOptions()
	{
		final Set<CatalogOption> opts = new HashSet<>();
		opts.add(CatalogOption.BASIC);
		opts.add(CatalogOption.CATEGORIES);
		opts.add(CatalogOption.SUBCATEGORIES);
		return opts;
	}

	private int countRecurrecyLevel(final List<CatalogData> catalogDataList)
	{
		int recurrencyLevel = 1;
		int value;
		for (final CatalogData catalog : catalogDataList)
		{
			value = countRecurrencyForCatalogData(catalog);
			if (value > recurrencyLevel)
			{
				recurrencyLevel = value;
			}
		}
		return recurrencyLevel;
	}

	private int countRecurrencyForCatalogData(final CatalogData catalog)
	{
		int retValue = 1;
		int value;
		for (final CatalogVersionData version : catalog.getCatalogVersions())
		{
			value = countRecurrencyForCatalogVersionData(version);
			if (value > retValue)
			{
				retValue = value;
			}
		}
		return retValue;
	}

	private int countRecurrencyForCatalogVersionData(final CatalogVersionData catalogVersion)
	{
		int retValue = 1;
		int value;
		for (final CategoryHierarchyData hierarchy : catalogVersion.getCategoriesHierarchyData())
		{
			value = countRecurrencyForCategoryHierarchyData(1, hierarchy);
			if (value > retValue)
			{
				retValue = value;
			}
		}
		return retValue;
	}

	private int countRecurrencyForCategoryHierarchyData(int currentValue, final CategoryHierarchyData hierarchy)
	{
		currentValue++;
		int retValue = currentValue;
		int value;
		for (final CategoryHierarchyData subcategory : hierarchy.getSubcategories())
		{
			value = countRecurrencyForCategoryHierarchyData(currentValue, subcategory);
			if (value > retValue)
			{
				retValue = value;
			}
		}
		return retValue;
	}
}
