/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.voucher.model.ProductRestrictionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class CouponProdRestSaveInterceptor implements PrepareInterceptor<ProductRestrictionModel>
{

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ProductService productService;

	/**
	 * @Description : This Method is evaluated when product restriction is created
	 * @param: paramMODEL
	 * @param: paramInterceptorContext
	 */
	@Override
	public void onPrepare(final ProductRestrictionModel paramMODEL, final InterceptorContext paramInterceptorContext)
			throws InterceptorException
	{
		if (null != paramMODEL && null != paramMODEL.getProductCodeList() && !paramMODEL.getProductCodeList().isEmpty())
		{
			final String productCodes = paramMODEL.getProductCodeList();
			final List<ProductModel> newProductModelList = new ArrayList<ProductModel>();

			final StringTokenizer newProductCodeTokens = new StringTokenizer(productCodes, ",");

			final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(configurationService
					.getConfiguration().getString("cronjob.promotion.catelog"),
					configurationService.getConfiguration().getString("cronjob.promotion.catalogVersionName"));
			if (null != catalogVersionModel)
			{
				while (newProductCodeTokens.hasMoreTokens())
				{

					newProductModelList.add(productService.getProductForCode(catalogVersionModel, newProductCodeTokens.nextToken()
							.trim()));
				}
				final Collection<ProductModel> existingProductList = paramMODEL.getProducts();

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
				paramMODEL.setProducts(finalProductList);
			}

		}

	}


}
