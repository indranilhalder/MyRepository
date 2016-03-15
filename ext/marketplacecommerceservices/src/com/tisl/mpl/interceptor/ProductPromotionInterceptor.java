/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.model.BuyAandBGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBPrecentageDiscountModel;
import com.tisl.mpl.model.BuyAandBgetCModel;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;


/**
 * @author TCS
 *
 */
public class ProductPromotionInterceptor implements PrepareInterceptor
{

	private static final Logger LOG = Logger.getLogger(ProductPromotionInterceptor.class);


	@Resource
	private CatalogVersionService catalogVersionService;
	@Autowired
	private ProductService productService;
	@Autowired
	private MplPromotionHelper mplPromotionHelper;
	@Autowired
	private ConfigurationService configurationService;




	/**
	 * The Method to auto populate a promotion group.
	 *
	 * @param abstractPromotion
	 */
	private void populatePromotionGroup(final AbstractPromotionModel abstractPromotion)
	{
		if (null != abstractPromotion && null == abstractPromotion.getPromotionGroup())
		{
			abstractPromotion.setPromotionGroup(mplPromotionHelper.fetchPromotionGroupDetails(configurationService
					.getConfiguration().getString("promotion.default.promotionGroup.identifier")));
		}
	}

	/**
	 * Add Multiple Products Details Before Promotion Model Save
	 *
	 * @param arg1
	 * @param object
	 * @throws InterceptorException
	 */
	@Override
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{

		LOG.debug("Inside ProductPromotionInterceptor");

		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(configurationService
				.getConfiguration().getString("cronjob.promotion.catelog"),
				configurationService.getConfiguration().getString("cronjob.promotion.catalogVersionName"));

		final String data = configurationService.getConfiguration().getString("promotion.bulkupload.products.realTime",
				MarketplacecommerceservicesConstants.TRUE);

		if (StringUtils.equalsIgnoreCase(data, MarketplacecommerceservicesConstants.TRUE))
		{
			if (object instanceof ProductPromotionModel)
			{
				//@Description :To check if an Enabled Promotions exists with the Product and same priority
				final ProductPromotionModel promotion = (ProductPromotionModel) object;
				populatePromotionGroup(promotion);

				if (StringUtils.isNotEmpty(promotion.getProductCodeList()))
				{
					final String productCodes = promotion.getProductCodeList();
					final List<ProductModel> newProductModelList = new ArrayList<ProductModel>();

					final StringTokenizer newProductCodeTokens = new StringTokenizer(productCodes,
							MarketplacecommerceservicesConstants.PROMO_PRODUCT_UPLOAD_SEPARATOR);


					if (null != catalogVersionModel)
					{
						while (newProductCodeTokens.hasMoreTokens())
						{
							newProductModelList.add(productService.getProductForCode(catalogVersionModel, newProductCodeTokens
									.nextToken().trim()));
						}
						final Collection<ProductModel> existingProductList = promotion.getProducts();

						final List<ProductModel> finalProductList = new ArrayList<ProductModel>();
						final Set<ProductModel> productModelSet = new HashSet<ProductModel>();
						if (null != existingProductList && !existingProductList.isEmpty())
						{
							finalProductList.addAll(existingProductList);
						}
						finalProductList.addAll(newProductModelList);
						productModelSet.addAll(finalProductList);
						finalProductList.clear();
						finalProductList.addAll(productModelSet);
						promotion.setProducts(finalProductList);
						promotion.setProductCodeList(MarketplacecommerceservicesConstants.EMPTY);
					}

				}

				if (StringUtils.isNotEmpty(promotion.getExcludedProductCodeList()))
				{

					final String excludedProductCodes = promotion.getExcludedProductCodeList();
					final List<ProductModel> newExclProductModelList = new ArrayList<ProductModel>();

					final StringTokenizer newExclProductCodeTokens = new StringTokenizer(excludedProductCodes,
							MarketplacecommerceservicesConstants.PROMO_PRODUCT_UPLOAD_SEPARATOR);

					if (null != catalogVersionModel)
					{
						while (newExclProductCodeTokens.hasMoreTokens())
						{

							newExclProductModelList.add(productService.getProductForCode(catalogVersionModel, newExclProductCodeTokens
									.nextToken().trim()));
						}
						final Collection<ProductModel> existingExcludedProductList = promotion.getExcludedProducts();

						final List<ProductModel> finalExcludedProductList = new ArrayList<ProductModel>();
						final Set<ProductModel> excludedProductModelSet = new HashSet<ProductModel>();
						if (null != existingExcludedProductList && !existingExcludedProductList.isEmpty())
						{
							finalExcludedProductList.addAll(existingExcludedProductList);
						}
						finalExcludedProductList.addAll(newExclProductModelList);
						excludedProductModelSet.addAll(finalExcludedProductList);
						finalExcludedProductList.clear();
						finalExcludedProductList.addAll(excludedProductModelSet);
						promotion.setExcludedProducts(finalExcludedProductList);
						promotion.setExcludedProductCodeList(MarketplacecommerceservicesConstants.EMPTY);
					}
				}

			}


			// Check for second qualifying products for BuyAandBPrecentageDiscount type promotion
			if (object instanceof BuyAandBPrecentageDiscountModel)
			{
				final BuyAandBPrecentageDiscountModel promotion = (BuyAandBPrecentageDiscountModel) object;
				populatePromotionGroup(promotion);
				if (StringUtils.isNotEmpty(promotion.getSecondProductCodeList()))
				{
					final String productCodes = promotion.getSecondProductCodeList();
					final List<ProductModel> newSecondProductModelList = new ArrayList<ProductModel>();

					final StringTokenizer newSecondProductCodeTokens = new StringTokenizer(productCodes,
							MarketplacecommerceservicesConstants.PROMO_PRODUCT_UPLOAD_SEPARATOR);

					if (null != catalogVersionModel)
					{
						while (newSecondProductCodeTokens.hasMoreTokens())
						{
							newSecondProductModelList.add(productService.getProductForCode(catalogVersionModel,
									newSecondProductCodeTokens.nextToken().trim()));
						}
						final Collection<ProductModel> existingSecondProductList = promotion.getSecondProducts();

						final List<ProductModel> finalSecondProductList = new ArrayList<ProductModel>();
						final Set<ProductModel> productModelSet = new HashSet<ProductModel>();
						if (null != existingSecondProductList && !existingSecondProductList.isEmpty())
						{
							finalSecondProductList.addAll(existingSecondProductList);
						}
						finalSecondProductList.addAll(newSecondProductModelList);
						productModelSet.addAll(finalSecondProductList);
						finalSecondProductList.clear();
						finalSecondProductList.addAll(productModelSet);
						promotion.setSecondProducts(finalSecondProductList);
						promotion.setSecondProductCodeList(MarketplacecommerceservicesConstants.EMPTY);
					}

				}
			}
			else if (object instanceof BuyAandBgetCModel)
			{
				final BuyAandBgetCModel promotion = (BuyAandBgetCModel) object;
				populatePromotionGroup(promotion);
				if (StringUtils.isNotEmpty(promotion.getSecondProductCodeList()))
				{
					final String productCodes = promotion.getSecondProductCodeList();
					final List<ProductModel> newSecondProductModelList = new ArrayList<ProductModel>();

					final StringTokenizer newSecondProductCodeTokens = new StringTokenizer(productCodes,
							MarketplacecommerceservicesConstants.PROMO_PRODUCT_UPLOAD_SEPARATOR);

					if (null != catalogVersionModel)
					{
						while (newSecondProductCodeTokens.hasMoreTokens())
						{
							newSecondProductModelList.add(productService.getProductForCode(catalogVersionModel,
									newSecondProductCodeTokens.nextToken().trim()));
						}
						final Collection<ProductModel> existingSecondProductList = promotion.getSecondProducts();

						final List<ProductModel> finalSecondProductList = new ArrayList<ProductModel>();
						final Set<ProductModel> productModelSet = new HashSet<ProductModel>();
						if (null != existingSecondProductList && !existingSecondProductList.isEmpty())
						{
							finalSecondProductList.addAll(existingSecondProductList);
						}
						finalSecondProductList.addAll(newSecondProductModelList);
						productModelSet.addAll(finalSecondProductList);
						finalSecondProductList.clear();
						finalSecondProductList.addAll(productModelSet);
						promotion.setSecondProducts(finalSecondProductList);
						promotion.setSecondProductCodeList(MarketplacecommerceservicesConstants.EMPTY);
					}

				}
			}
			else if (object instanceof BuyAandBGetPrecentageDiscountCashbackModel)
			{
				final BuyAandBGetPrecentageDiscountCashbackModel promotion = (BuyAandBGetPrecentageDiscountCashbackModel) object;
				populatePromotionGroup(promotion);
				if (StringUtils.isNotEmpty(promotion.getSecondProductCodeList()))
				{
					final String productCodes = promotion.getSecondProductCodeList();
					final List<ProductModel> newSecondProductModelList = new ArrayList<ProductModel>();

					final StringTokenizer newSecondProductCodeTokens = new StringTokenizer(productCodes,
							MarketplacecommerceservicesConstants.PROMO_PRODUCT_UPLOAD_SEPARATOR);

					if (null != catalogVersionModel)
					{
						while (newSecondProductCodeTokens.hasMoreTokens())
						{
							newSecondProductModelList.add(productService.getProductForCode(catalogVersionModel,
									newSecondProductCodeTokens.nextToken().trim()));
						}
						final Collection<ProductModel> existingSecondProductList = promotion.getSecondProducts();

						final List<ProductModel> finalSecondProductList = new ArrayList<ProductModel>();
						final Set<ProductModel> productModelSet = new HashSet<ProductModel>();
						if (null != existingSecondProductList && !existingSecondProductList.isEmpty())
						{
							finalSecondProductList.addAll(existingSecondProductList);
						}
						finalSecondProductList.addAll(newSecondProductModelList);
						productModelSet.addAll(finalSecondProductList);
						finalSecondProductList.clear();
						finalSecondProductList.addAll(productModelSet);
						promotion.setSecondProducts(finalSecondProductList);
						promotion.setSecondProductCodeList(MarketplacecommerceservicesConstants.EMPTY);
					}

				}
			}
			else if (object instanceof BuyAandBGetPromotionOnShippingChargesModel)
			{
				final BuyAandBGetPromotionOnShippingChargesModel promotion = (BuyAandBGetPromotionOnShippingChargesModel) object;
				populatePromotionGroup(promotion);
				if (StringUtils.isNotEmpty(promotion.getSecondProductCodeList()))
				{
					final String productCodes = promotion.getSecondProductCodeList();
					final List<ProductModel> newSecondProductModelList = new ArrayList<ProductModel>();

					final StringTokenizer newSecondProductCodeTokens = new StringTokenizer(productCodes,
							MarketplacecommerceservicesConstants.PROMO_PRODUCT_UPLOAD_SEPARATOR);

					if (null != catalogVersionModel)
					{
						while (newSecondProductCodeTokens.hasMoreTokens())
						{
							newSecondProductModelList.add(productService.getProductForCode(catalogVersionModel,
									newSecondProductCodeTokens.nextToken().trim()));
						}
						final Collection<ProductModel> existingSecondProductList = promotion.getSecondProducts();

						final List<ProductModel> finalSecondProductList = new ArrayList<ProductModel>();
						final Set<ProductModel> productModelSet = new HashSet<ProductModel>();
						if (null != existingSecondProductList && !existingSecondProductList.isEmpty())
						{
							finalSecondProductList.addAll(existingSecondProductList);
						}
						finalSecondProductList.addAll(newSecondProductModelList);
						productModelSet.addAll(finalSecondProductList);
						finalSecondProductList.clear();
						finalSecondProductList.addAll(productModelSet);
						promotion.setSecondProducts(finalSecondProductList);
						promotion.setSecondProductCodeList(MarketplacecommerceservicesConstants.EMPTY);
					}
				}
			}
		}
		else if (!StringUtils.equalsIgnoreCase(data, MarketplacecommerceservicesConstants.TRUE)
				&& object instanceof ProductPromotionModel)
		{
			final ProductPromotionModel promotion = (ProductPromotionModel) object;
			promotion.setProductCodeList(MarketplacecommerceservicesConstants.EMPTY);
			promotion.setExcludedProductCodeList(MarketplacecommerceservicesConstants.EMPTY);
		}

	}

	/**
	 * @return the mplPromotionHelper
	 */
	public MplPromotionHelper getMplPromotionHelper()
	{
		return mplPromotionHelper;
	}

	/**
	 * @param mplPromotionHelper
	 *           the mplPromotionHelper to set
	 */
	public void setMplPromotionHelper(final MplPromotionHelper mplPromotionHelper)
	{
		this.mplPromotionHelper = mplPromotionHelper;
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
}
