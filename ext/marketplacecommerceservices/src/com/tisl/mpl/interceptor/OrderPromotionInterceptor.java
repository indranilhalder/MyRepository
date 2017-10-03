/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
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
import com.tisl.mpl.promotion.helper.MplPromotionHelper;


/**
 * @author TCS
 *
 */
public class OrderPromotionInterceptor implements PrepareInterceptor
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
	 *
	 *
	 * @param arg1
	 * @param object
	 * @throws InterceptorException
	 */
	@Override
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{

		LOG.debug("Inside OrderPromotionInterceptor");

		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(configurationService
				.getConfiguration().getString("cronjob.promotion.catelog"),
				configurationService.getConfiguration().getString("cronjob.promotion.catalogVersionName"));

		final String data = configurationService.getConfiguration().getString("promotion.bulkupload.products.realTime",
				MarketplacecommerceservicesConstants.TRUE);


		if (StringUtils.equalsIgnoreCase(data, MarketplacecommerceservicesConstants.TRUE))
		{
			if (object instanceof OrderPromotionModel)
			{
				//@Description :To check if an Enabled Promotions exists with the Product and same priority
				final OrderPromotionModel promotion = (OrderPromotionModel) object;
				populatePromotionGroup(promotion);

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
		}
		else if (!StringUtils.equalsIgnoreCase(data, MarketplacecommerceservicesConstants.TRUE)
				&& object instanceof ProductPromotionModel)
		{
			final OrderPromotionModel promotion = (OrderPromotionModel) object;
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
