package com.tisl.mpl.facade.category.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.catalog.impl.DefaultCatalogFacade;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.facade.category.MplCategoryFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplCategoryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;
import com.tisl.mpl.wsdto.BreadcrumbListWsDTO;
import com.tisl.mpl.wsdto.BreadcrumbResponseWsDTO;


/**
 * @author TCS
 *
 */
public class MplCategoryFacadeImpl extends DefaultCatalogFacade implements MplCategoryFacade
{
	private static final Logger LOG = Logger.getLogger(MplCategoryFacadeImpl.class);
	private ConfigurationService configurationService;
	@Autowired
	private UrlResolver<ProductModel> productModelUrlResolver;
	@Autowired
	private UrlResolver<CategoryModel> categoryModelUrlResolver;
	private ProductService productService;




	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}





	@Autowired
	private Converter<CategoryModel, CategoryData> categoryConverter;


	private CMSSiteService cmsSiteService;


	/**
	 * @return the cmsSiteService
	 */
	public CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	/**
	 * @param cmsSiteService
	 *           the cmsSiteService to set
	 */
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

	/**
	 * @return the mplCategoryService
	 */
	public MplCategoryService getMplCategoryService()
	{
		return mplCategoryService;
	}

	/**
	 * @param mplCategoryService
	 *           the mplCategoryService to set
	 */
	public void setMplCategoryService(final MplCategoryService mplCategoryService)
	{
		this.mplCategoryService = mplCategoryService;
	}


	private MplCategoryService mplCategoryService;

	private MplSellerInformationService mplSellerInformationService;

	/**
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}

	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}

	@Override
	public String getSellerInformationBySellerName(final String sellerName)
	{
		final SellerInformationModel sellerInformationModel = mplSellerInformationService.getSellerInformationBySellerName(
				cmsSiteService.getCurrentCatalogVersion(), sellerName.toUpperCase());
		if (null != sellerInformationModel)
		{
			final String sellerID = sellerInformationModel.getSellerID();
			return sellerID;
		}
		return null;
	}

	@Override
	public String getActiveSellerRootCategoryBySellerId(final String sellerId)
	{
		final SellerSalesCategoryModel sellerStructureModel = mplSellerInformationService
				.getActiveSellerRootCategoryBySellerId(sellerId.toUpperCase());
		final String SellerRootCategoryId = (null != sellerStructureModel && sellerStructureModel.getSellerRootCategoryId() != null ? sellerStructureModel
				.getSellerRootCategoryId() : null);
		return SellerRootCategoryId;
	}

	/**
	 * It will fetch the sub category tree for the given category name. (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.category.MplCategoryFacade#getShopBrandCategories(java.lang.String)
	 *
	 * @param sellerName
	 */
	@Override
	public CategoryData getShopBrandCategories(final String sellerName)

	{
		final String sellerId = getSellerInformationBySellerName(sellerName);
		if (sellerId != null)
		{
			final String sellerRootCategoryId = getActiveSellerRootCategoryBySellerId(sellerId);
			Preconditions.checkArgument(sellerName != null, "Category is required to perform this operation, null given");
			final String categoryCode = getConfigurationService().getConfiguration().getString(
					"marketplace.mplcatalog.seller.sales.category.root.id");

			final CategoryModel topCategoryDetails = mplCategoryService.getCategoryModelForCode(
					cmsSiteService.getCurrentCatalogVersion(), categoryCode);//SSH1

			boolean isCategoryIDAvailable = false;

			CategoryData secondLevelCategoryData = null;
			//List<CategoryData> thirdLevelCategorydataList = null;
			//CategoryData thirdLevelCategoryData = null;
			//	CategoryData fourthLevelCategoryData = null;
			//List<CategoryData> fourthLevelCategorydataList = null;
			//	List<CategoryData> fifthLevelCategorydataList = null;
			//	CategoryData fifthLevelCategoryData = null;

			if (topCategoryDetails == null)
			{
				throw new UnknownIdentifierException("Category with code '" + categoryCode
						+ "' not found! (Active session catalogversions: " + cmsSiteService.getCurrentCatalogVersion() + ")");
			}

			for (final CategoryModel secondLevelCategoryModel : topCategoryDetails.getCategories())
			{
				if (secondLevelCategoryModel.getCode().equalsIgnoreCase(sellerRootCategoryId))
				{
					isCategoryIDAvailable = true;

					secondLevelCategoryData = recursioveMethod(secondLevelCategoryModel);

					/*
					 * secondLevelCategoryData = categoryConverter.convert(secondLevelCategoryModel);
					 *
					 * thirdLevelCategorydataList = new ArrayList<CategoryData>();
					 *
					 *
					 * if (!secondLevelCategoryModel.getCategories().isEmpty()) { for (final CategoryModel
					 * thirdLevelCategoryModel : secondLevelCategoryModel.getCategories()) { thirdLevelCategoryData =
					 * categoryConverter.convert(thirdLevelCategoryModel); fourthLevelCategorydataList = new
					 * ArrayList<CategoryData>(); if (!thirdLevelCategoryModel.getCategories().isEmpty()) { for (final
					 * CategoryModel fourthLevelCategoryModel : thirdLevelCategoryModel.getCategories()) {
					 * fourthLevelCategoryData = categoryConverter.convert(fourthLevelCategoryModel);
					 * fifthLevelCategorydataList = new ArrayList<CategoryData>(); if
					 * (!fourthLevelCategoryModel.getCategories().isEmpty()) { for (final CategoryModel
					 * fifthLevelCategoryModel : fourthLevelCategoryModel.getAllSubcategories()) { fifthLevelCategoryData =
					 * categoryConverter.convert(fifthLevelCategoryModel);
					 * fifthLevelCategorydataList.add(fifthLevelCategoryData); }
					 * fourthLevelCategoryData.setSubCategories(fifthLevelCategorydataList); }
					 * fourthLevelCategorydataList.add(fourthLevelCategoryData); }
					 * thirdLevelCategoryData.setSubCategories(fourthLevelCategorydataList); }
					 * thirdLevelCategorydataList.add(thirdLevelCategoryData); }
					 *
					 *
					 * } secondLevelCategoryData.setSubCategories(thirdLevelCategorydataList);
					 */
				}
			}
			if (!isCategoryIDAvailable)
			{
				throw new EtailBusinessExceptions("Category with name '" + sellerName
						+ "' not found! (Active session catalogversions: " + cmsSiteService.getCurrentCatalogVersion() + ")");
			}
			return secondLevelCategoryData;
		}
		return null;
	}

	CategoryData recursioveMethod(final CategoryModel CurrentLevelcategoryModel)
	{
		final List<CategoryModel> categories = CurrentLevelcategoryModel.getCategories();

		if (!CollectionUtils.isEmpty(categories))
		{
			final CategoryData categoryData = categoryConverter.convert(CurrentLevelcategoryModel);
			List<CategoryData> CategorydataList = null;
			CategorydataList = new ArrayList<CategoryData>();

			for (final CategoryModel NextLevelcategoryModel : categories)
			{
				CategoryData categoryData1 = null;
				categoryData1 = recursioveMethod(NextLevelcategoryModel);
				CategorydataList.add(categoryData1);
			}
			categoryData.setSubCategories(CategorydataList);
			return categoryData;
		}
		else
		{
			final CategoryData categoryData = categoryConverter.convert(CurrentLevelcategoryModel);
			return categoryData;
		}

	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	// ######################### TISLUX-356 START

	@Override
	public BreadcrumbResponseWsDTO getBreadcrumb(final String code, final String type)
	{
		BreadcrumbResponseWsDTO responseBreadcrumb = null;

		if (MarketplaceFacadesConstants.CATEGORY.equalsIgnoreCase(type))
		{
			final CategoryModel category = getCategoryService().getCategoryForCode(code);
			responseBreadcrumb = getBreadcrumbsForCategory(category);
		}
		else if (MarketplaceFacadesConstants.PRODUCT.equalsIgnoreCase(type))
		{
			final ProductModel product = getProductService().getProductForCode(code);
			responseBreadcrumb = getBreadcrumbsForProduct(product);
		}
		responseBreadcrumb.setRootName(MarketplaceFacadesConstants.ROOT_NAME);
		return responseBreadcrumb;
	}

	/**
	 * @param categoryModel
	 * @return BreadcrumbResponseWsDTO
	 */
	private BreadcrumbResponseWsDTO getBreadcrumbsForCategory(final CategoryModel categoryModel)
	{
		final List<CategoryModel> categoryHierarchy = getCategoryService().getPathForCategory(categoryModel);
		final BreadcrumbResponseWsDTO breadcrumbResponse = new BreadcrumbResponseWsDTO();
		final List<BreadcrumbListWsDTO> breadcrumbList = new ArrayList<BreadcrumbListWsDTO>();

		if (!CollectionUtils.isEmpty(categoryHierarchy) && categoryHierarchy.size() > 1)
		{
			int count = 0;
			for (final CategoryModel category : categoryHierarchy)
			{
				final BreadcrumbListWsDTO categoryBreadcrumb = new BreadcrumbListWsDTO();
				if (count == 0)
				{
					count++;
					continue;
				}
				categoryBreadcrumb.setCode(category.getCode());
				categoryBreadcrumb.setName(category.getName());
				categoryBreadcrumb.setUrl(getCategoryUrl(category));
				categoryBreadcrumb.setLevel(Integer.valueOf(count++));
				breadcrumbList.add(categoryBreadcrumb);
			}
		}
		breadcrumbResponse.setBreadcrumbList(breadcrumbList);
		return breadcrumbResponse;
	}

	/**
	 * @param product
	 * @return BreadcrumbResponseWsDTO
	 */
	private BreadcrumbResponseWsDTO getBreadcrumbsForProduct(final ProductModel product)
	{
		final BreadcrumbResponseWsDTO breadcrumbResponse = new BreadcrumbResponseWsDTO();
		final List<BreadcrumbListWsDTO> breadcrumbList = new ArrayList<BreadcrumbListWsDTO>();
		final List<CategoryModel> categoryHierarchy = getCategoryPathForProduct(product, configurationService.getConfiguration()
				.getString(MarketplacecommerceservicesConstants.LUX_SALESCATEGORYTYPE), CategoryModel.class);

		if (!CollectionUtils.isEmpty(categoryHierarchy) && categoryHierarchy.size() > 1)
		{
			int count = 0;
			for (final CategoryModel category : categoryHierarchy)
			{
				final BreadcrumbListWsDTO categoryBreadcrumb = new BreadcrumbListWsDTO();
				if (count == 0)
				{
					count++;
					continue;
				}
				categoryBreadcrumb.setCode(category.getCode());
				categoryBreadcrumb.setName(category.getName());
				categoryBreadcrumb.setUrl(getCategoryUrl(category));
				categoryBreadcrumb.setLevel(Integer.valueOf(count++));
				breadcrumbList.add(categoryBreadcrumb);
			}

			final BreadcrumbListWsDTO productBreadcrumb = new BreadcrumbListWsDTO();
			productBreadcrumb.setCode(product.getCode());
			productBreadcrumb.setName(product.getName());
			productBreadcrumb.setUrl(getProductUrl(product));
			productBreadcrumb.setLevel(Integer.valueOf(count));
			breadcrumbList.add(productBreadcrumb);
		}
		breadcrumbResponse.setBreadcrumbList(breadcrumbList);
		return breadcrumbResponse;
	}

	private List<CategoryModel> getCategoryPathForProduct(final ProductModel product, final String rootCategory,
			final Class... includeOnlyCategories)
	{
		final List<CategoryModel> result = new ArrayList<CategoryModel>();
		final Collection<CategoryModel> currentLevel = new ArrayList<CategoryModel>();
		currentLevel.addAll(product.getSupercategories());

		while (!CollectionUtils.isEmpty(currentLevel))
		{
			CategoryModel categoryModel = null;
			for (final CategoryModel category : currentLevel)
			{
				if (categoryModel == null && shouldAddPathElement(category.getClass(), includeOnlyCategories)
						&& category.getCode().startsWith(rootCategory))
				{
					categoryModel = category;
				}
			}
			currentLevel.clear();
			if (categoryModel != null)
			{
				currentLevel.addAll(categoryModel.getSupercategories());
				result.add(categoryModel);
			}
		}

		Collections.reverse(result);
		return result;
	}

	private boolean shouldAddPathElement(final Class element, final Class... includeOnlyCategories)
	{
		boolean result = false;
		if (ArrayUtils.isEmpty(includeOnlyCategories))
		{
			result = true;
		}
		else
		{
			if (ArrayUtils.contains(includeOnlyCategories, element))
			{
				result = true;
			}
		}
		return result;
	}

	protected ProductModel getBaseProduct(final ProductModel product)
	{
		if (product instanceof VariantProductModel)
		{
			return getBaseProduct(((VariantProductModel) product).getBaseProduct());
		}
		return product;
	}


	protected String getCategoryUrl(final CategoryModel category)
	{
		final String categoryUrl = categoryModelUrlResolver.resolve(category);
		return categoryUrl;
	}

	protected String getProductUrl(final ProductModel product)
	{
		final String productUrl = productModelUrlResolver.resolve(product);
		return productUrl;
	}

	// ######################### TISLUX-356 END

}