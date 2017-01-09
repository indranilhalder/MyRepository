/**
 *
 */
package com.tisl.mpl.promotion.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.promotion.dao.impl.UpdatePromotionalPriceDaoImpl;
import com.tisl.mpl.promotion.service.UpdatePromotionalPriceService;
import com.tisl.mpl.promotion.service.UpdateSplPriceHelperService;


/**
 * @author TCS
 *
 */
public class UpdatePromotionalPriceServiceImpl implements UpdatePromotionalPriceService
{
	private ModelService modelService;


	@Resource(name = "mplUpdatePromotionPriceDao")
	private UpdatePromotionalPriceDaoImpl updatePromotionalPriceDao;

	private static final Logger LOG = Logger.getLogger(UpdatePromotionalPriceServiceImpl.class);
	private static String CODE = "code";
	@Autowired
	private ProductService productService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	@Autowired
	private ConfigurationService configurationService;

	private UpdateSplPriceHelperService updateSplPriceHelperService;


	public ModelService getModelService()
	{
		return modelService;
	}


	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * The Method Updates Special Price in Price Row for eligible Products
	 *
	 * @param products
	 * @param categories
	 * @param value
	 * @param startDate
	 * @param endtDate
	 * @param percent
	 * @param priority
	 * @param sellers
	 * @param brands
	 */
	@Override
	public void updatePromotionalPrice(final List<Product> products, final List<Category> categories, final Double value,
			final Date startDate, final Date endtDate, final boolean percent, final Integer priority, final List<String> sellers,
			final List<String> brands, final String promoCode, final List<String> rejectSellerList,
			final List<String> rejectBrandList, final Double maxDiscount, final List<Product> exproductList)
	{

		try
		{
			clearExistingData(promoCode);
			final List<String> product = new ArrayList<String>();
			List<String> stagedProductList = new ArrayList<String>();
			final List<String> promoproductList = new ArrayList<String>();
			final List<PriceRowModel> priceList = new ArrayList<PriceRowModel>();
			List<String> exProductList = new ArrayList<String>();

			//Added for TISPRD-8074
			if (CollectionUtils.isNotEmpty(exproductList))
			{
				exProductList = getExcludedProductData(exproductList);
			}

			if (CollectionUtils.isNotEmpty(products))
			{
				for (final Product itrProduct : products)
				{
					if (getBrandsForProduct(itrProduct, brands, rejectBrandList) && validateProductData(itrProduct, priority))
					{
						product.add(itrProduct.getAttribute("pk").toString());
						promoproductList.add(itrProduct.getAttribute(CODE).toString()); //For staged Product Details
					}
				}
			}

			if (CollectionUtils.isNotEmpty(categories))
			{
				//TISPRO-352 : Fix
				final ConcurrentHashMap<List<String>, List<String>> categoryDetailsMap = updateSplPriceHelperService
						.getEligibleProductList(brands, rejectBrandList, priority, categories, exProductList);
				if (MapUtils.isNotEmpty(categoryDetailsMap))
				{
					for (final ConcurrentHashMap.Entry<List<String>, List<String>> entry : categoryDetailsMap.entrySet())
					{
						product.addAll(entry.getKey());
						promoproductList.addAll(entry.getValue());
						LOG.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
					}
				}
			}

			LOG.debug("******** Special Price - Promotion Applicable product List:" + product);
			//filter product list based on brand restriction


			if (!product.isEmpty())
			{
				stagedProductList = getStagedProductDetails(promoproductList); // For adding the staged catalog price Row for Product
				if (CollectionUtils.isNotEmpty(stagedProductList))
				{
					product.addAll(stagedProductList);
				}

				boolean updateSpecialPrice = false;
				final List<PriceRowModel> priceRow = updatePromotionalPriceDao.fetchPricedData(product);
				for (final PriceRowModel price : priceRow)
				{
					//updating price row
					boolean isEligibletoDisable = false;
					if (CollectionUtils.isEmpty(sellers) && CollectionUtils.isEmpty(rejectSellerList))
					{
						updateSpecialPrice = true;//set the flag true if there is no seller restriction
					}
					else
					{
						updateSpecialPrice = isPriceToUpdate(price, sellers, rejectSellerList);
						if (!updateSpecialPrice)
						{
							isEligibletoDisable = true;
						}
					}

					if (updateSpecialPrice)
					{
						if (!percent)
						{
							price.setPromotionStartDate(startDate);
							price.setPromotionEndDate(endtDate);
							price.setIsPercentage(Boolean.valueOf(percent));
							price.setPromotionValue(value);
							price.setPromotionIdentifier(promoCode);
							price.setMaxDiscount(maxDiscount);
						}
						else
						{
							price.setPromotionStartDate(startDate);
							price.setPromotionEndDate(endtDate);
							price.setIsPercentage(Boolean.valueOf(percent));
							price.setPromotionValue(value);
							price.setPromotionIdentifier(promoCode);
							price.setMaxDiscount(maxDiscount);
						}
						priceList.add(price);
					}
					else if (!updateSpecialPrice && isEligibletoDisable)
					{
						LOG.debug("Removing Promotion Details from the Price Row : USSID not eligible for Promotion");
						price.setPromotionStartDate(null);
						price.setPromotionEndDate(null);
						price.setIsPercentage(null);
						price.setPromotionValue(null);
						price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
						price.setMaxDiscount(null);

						LOG.debug("Saving Price Row ");
						priceList.add(price);
					}

				}

				if (CollectionUtils.isNotEmpty(priceList))
				{
					modelService.saveAll(priceList);
				}
			}

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * //Added for TISPRD-8074
	 *
	 * @param exproductList
	 * @return exProductList
	 */
	private List<String> getExcludedProductData(final List<Product> exproductList)
	{
		final List<String> exProductList = new ArrayList<String>();

		for (final Product product : exproductList)
		{
			if (StringUtils.isNotEmpty(product.getCode()))
			{
				exProductList.add(product.getCode());
			}
		}

		return exProductList;
	}

	/**
	 * The Method clears Existing PriceRow Data
	 *
	 * @param promoCode
	 */
	private void clearExistingData(final String promoCode)
	{
		if (StringUtils.isNotEmpty(promoCode))
		{
			final List<PriceRowModel> priceRowList = updatePromotionalPriceDao.fetchPromoPriceData(promoCode);
			final List<PriceRowModel> finalList = new ArrayList<PriceRowModel>();
			if (CollectionUtils.isNotEmpty(priceRowList))
			{
				for (final PriceRowModel price : priceRowList)
				{
					price.setPromotionStartDate(null);
					price.setPromotionEndDate(null);
					price.setIsPercentage(null);
					price.setPromotionValue(null);
					price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
					price.setMaxDiscount(null);

					finalList.add(price);
				}

				if (CollectionUtils.isNotEmpty(finalList))
				{
					modelService.saveAll(finalList);
				}
			}
		}
	}

	/**
	 * For TISPRD-383 Get Staged Product Details
	 *
	 * @param productList
	 * @return stagedProductList
	 */
	private List<String> getStagedProductDetails(final List<String> productList)
	{
		final List<String> stagedProductList = new ArrayList<String>();

		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(configurationService
				.getConfiguration().getString("cronjob.promotion.catelog"), MarketplacecommerceservicesConstants.STAGED);

		if (CollectionUtils.isNotEmpty(productList))
		{
			for (final String productCode : productList)
			{
				final ProductModel product = productService.getProductForCode(catalogVersionModel, productCode);
				stagedProductList.add(product.getPk().toString());
			}
		}

		return stagedProductList;
	}

	/**
	 * @Description: To check whether to update the special price or not if seller restriction exists
	 * @param price
	 * @param sellers
	 * @param rejectSellerList
	 * @return: updateSpecialPrice
	 */
	private boolean isPriceToUpdate(final PriceRowModel price, final List<String> sellers, final List<String> rejectSellerList)
	{
		boolean updateSpecialPrice = false;
		final List<SellerInformationModel> sellerModels = new ArrayList<SellerInformationModel>(price.getProduct()
				.getSellerInformationRelator());

		if (CollectionUtils.isNotEmpty(sellers))
		{
			for (final SellerInformationModel seller : sellerModels)
			{
				if (sellers.contains(seller.getSellerID())
						&& price.getSellerArticleSKU().equalsIgnoreCase(seller.getSellerArticleSKU()))
				{
					LOG.debug("******** Special Price - Promotion Applicable ussid List:" + seller.getSellerArticleSKU());
					updateSpecialPrice = true;
				}
			}
		}
		else if (CollectionUtils.isNotEmpty(rejectSellerList))
		{
			for (final SellerInformationModel seller : sellerModels)
			{
				if (rejectSellerList.contains(seller.getSellerID())
						&& price.getSellerArticleSKU().equalsIgnoreCase(seller.getSellerArticleSKU()))
				{
					LOG.debug("******** Special Price - Promotion Applicable ussid List:" + seller.getSellerArticleSKU());
					updateSpecialPrice = false;
					break;
				}
				else
				{
					updateSpecialPrice = true;
				}
			}
		}


		return updateSpecialPrice;
	}




	/**
	 * The Method disables Special Price in Price Row for eligible Products
	 *
	 * @param promoCode
	 * @param products
	 * @param categories
	 * @param isEnabled
	 * @param priority
	 * @param quantity
	 * @param brands
	 */
	@Override
	public void disablePromotionalPrice(final List<Product> products, final List<Category> categories, final boolean isEnabled,
			final Integer priority, final List<String> brands, final Long quantity, final List<String> rejectSellerList,
			final List<String> rejectBrandList, final String promoCode)
	{
		try
		{
			clearExistingData(promoCode);
			final List<String> product = new ArrayList<String>();
			List<String> stagedProductList = new ArrayList<String>();
			final List<String> promoproductList = new ArrayList<String>();
			final List<PriceRowModel> priceList = new ArrayList<PriceRowModel>();

			if (CollectionUtils.isNotEmpty(products))
			{
				for (final Product itrProduct : products)
				{
					if (getBrandsForProduct(itrProduct, brands, rejectBrandList) && validateProductData(itrProduct, priority))
					{
						product.add(itrProduct.getAttribute("pk").toString());
						promoproductList.add(itrProduct.getAttribute(CODE).toString());
					}
				}
			}

			if (CollectionUtils.isNotEmpty(categories))
			{
				//TISPRO-352 : Fix
				final ConcurrentHashMap<List<String>, List<String>> categoryDetailsMap = updateSplPriceHelperService
						.getEligibleProductList(brands, rejectBrandList, priority, categories);
				if (MapUtils.isNotEmpty(categoryDetailsMap))
				{
					for (final ConcurrentHashMap.Entry<List<String>, List<String>> entry : categoryDetailsMap.entrySet())
					{
						product.addAll(entry.getKey());
						promoproductList.addAll(entry.getValue());
						LOG.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
					}
				}
			}


			LOG.debug("******** Special Price - Disable Promotion Applicable product List:" + product);


			if (!product.isEmpty())
			{
				stagedProductList = getStagedProductDetails(promoproductList); // For adding the staged catalog price Row for Product
				if (CollectionUtils.isNotEmpty(stagedProductList))
				{
					product.addAll(stagedProductList);
				}

				final List<PriceRowModel> priceRow = updatePromotionalPriceDao.fetchPricedData(product);
				for (final PriceRowModel price : priceRow)
				{
					if (!isEnabled)
					{
						price.setPromotionStartDate(null);
						price.setPromotionEndDate(null);
						price.setIsPercentage(null);
						price.setPromotionValue(null);
						price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
						price.setMaxDiscount(null);
					}
					else if (null != quantity && quantity.longValue() > 1) //For TISPRD-383 : If Validated remove Special Price Details
					{
						price.setPromotionStartDate(null);
						price.setPromotionEndDate(null);
						price.setIsPercentage(null);
						price.setPromotionValue(null);
						price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
						price.setMaxDiscount(null);
					}
					priceList.add(price);
				}

				if (CollectionUtils.isNotEmpty(priceList))
				{
					modelService.save(priceList);
				}

			}
		}

		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * Validate Product Promotion Priority
	 *
	 * @param product
	 * @param priority
	 * @return boolean
	 */
	private boolean validateProductData(final Product product, final Integer priority)
	{
		boolean flag = false;
		try
		{
			final ProductModel oModel = productService.getProductForCode(getDefaultPromotionsManager().catalogData(),
					product.getCode());
			if (null != oModel)
			{
				flag = updateSplPriceHelperService.validateProductData(oModel, priority);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return flag;
	}

	/**
	 * Validates Brand Specific Restriction
	 *
	 * @param product
	 * @param brands
	 * @param rejectBrandList
	 * @return allow
	 */
	private boolean getBrandsForProduct(final Product product, final List<String> brands, final List<String> rejectBrandList)
	{
		boolean allow = false;
		List<String> brandList = null;

		try
		{

			if (brands.isEmpty() && rejectBrandList.isEmpty())//no need to proceed if there is no brand restriction
			{
				return true;
			}

			final List<Category> categories = getImmediateSuperCategory(product);
			if (categories != null && !categories.isEmpty())
			{
				brandList = new ArrayList<String>();
				for (final Category category : categories)
				{
					if (category.getCode().startsWith("MBH"))
					{
						brandList.add(category.getCode());
					}

				}
			}

			if (CollectionUtils.isNotEmpty(brands) && CollectionUtils.isNotEmpty(brandList))
			{
				final String productBrand = brandList.get(0);
				if (brands.contains(productBrand))
				{
					allow = true;
				}
			}


			if (CollectionUtils.isNotEmpty(rejectBrandList) && CollectionUtils.isNotEmpty(brandList))
			{
				final String productBrand = brandList.get(0);
				if (rejectBrandList.contains(productBrand))
				{
					allow = false;
				}
				else
				{
					allow = true;
				}
			}

			LOG.debug("******** Special Price - product:" + product.getCode() + " is brand restricted.");
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return allow;
	}

	/**
	 * Jalo Method to return super categories of a product
	 *
	 * @param product
	 * @return superCategories
	 */
	private List<Category> getImmediateSuperCategory(final Product product)
	{
		List<Category> superCategories = new ArrayList<Category>();

		if (product != null)
		{

			try
			{
				superCategories = (List<Category>) product.getAttribute("supercategories");
			}
			catch (final JaloInvalidParameterException e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}
			catch (final JaloSecurityException e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}
			catch (final Exception e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}

		}

		return superCategories;
	}


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


	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}


	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
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
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	/**
	 * @return the updateSplPriceHelperService
	 */
	public UpdateSplPriceHelperService getUpdateSplPriceHelperService()
	{
		return updateSplPriceHelperService;
	}


	/**
	 * @param updateSplPriceHelperService
	 *           the updateSplPriceHelperService to set
	 */
	public void setUpdateSplPriceHelperService(final UpdateSplPriceHelperService updateSplPriceHelperService)
	{
		this.updateSplPriceHelperService = updateSplPriceHelperService;
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}


}
