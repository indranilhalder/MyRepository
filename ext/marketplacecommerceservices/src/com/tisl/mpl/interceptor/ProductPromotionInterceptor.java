/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.promotion.helper.MplPromotionHelper;


/**
 * @author TCS
 *
 */
public class ProductPromotionInterceptor implements PrepareInterceptor<ProductPromotionModel>
{

	private static final Logger LOG = Logger.getLogger(ProductPromotionInterceptor.class);

	@Resource
	private CategoryService categoryService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Autowired
	private ProductService productService;
	@Autowired
	private MplPromotionHelper mplPromotionHelper;

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

	@Autowired
	private ConfigurationService configurationService;

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

	@Override
	public void onPrepare(final ProductPromotionModel promotion, final InterceptorContext arg1) throws InterceptorException
	{
		LOG.debug("ProductPromotionInterceptor");

		populatePromotionGroup(promotion);

		if (null != promotion.getProductCodeList() && !promotion.getProductCodeList().isEmpty())
		{
			final String productCodes = promotion.getProductCodeList();
			final List<ProductModel> newProductModelList = new ArrayList<ProductModel>();

			final StringTokenizer newProductCodeTokens = new StringTokenizer(productCodes, ",");

			final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
					configurationService.getConfiguration().getString("cronjob.promotion.catelog"),
					configurationService.getConfiguration().getString("cronjob.promotion.catalogVersionName"));
			if (null != catalogVersionModel)
			{
				while (newProductCodeTokens.hasMoreTokens())
				{

					newProductModelList
							.add(productService.getProductForCode(catalogVersionModel, newProductCodeTokens.nextToken().trim()));
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
				promotion.setProductCodeList("");
			}

		}
	}

	/**
	 * The Method to auto populate a promotion group.
	 *
	 * @param abstractPromotion
	 */
	private void populatePromotionGroup(final AbstractPromotionModel abstractPromotion)
	{
		if (null != abstractPromotion && null == abstractPromotion.getPromotionGroup())
		{
			abstractPromotion.setPromotionGroup(mplPromotionHelper.fetchPromotionGroupDetails(
					configurationService.getConfiguration().getString("promotion.default.promotionGroup.identifier")));
		}
	}
}
