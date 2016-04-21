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

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class CouponProdRestSaveInterceptor implements PrepareInterceptor<ProductRestrictionModel>
{

	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "productService")
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
		try
		{
			if (null != paramMODEL && StringUtils.isNotEmpty(paramMODEL.getProductCodeList()))
			{
				final String productCodes = paramMODEL.getProductCodeList(); //List of product codes separated by commas
				final List<ProductModel> newProductModelList = new ArrayList<ProductModel>();

				final StringTokenizer newProductCodeTokens = new StringTokenizer(productCodes,
						MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				final CatalogVersionModel catalogVersionModel = getCatalogVersionService().getCatalogVersion(
						getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGID),
						getConfigurationService().getConfiguration().getString(
								MarketplacecommerceservicesConstants.DEFAULTCATALOGVERISONID));
				if (null != catalogVersionModel)
				{
					while (newProductCodeTokens.hasMoreTokens())
					{
						newProductModelList.add(getProductService().getProductForCode(catalogVersionModel,
								newProductCodeTokens.nextToken().trim()));
					}
					final Collection<ProductModel> existingProductList = paramMODEL.getProducts();

					final List<ProductModel> finalProductList = new ArrayList<ProductModel>();
					final Set<ProductModel> productModelSet = new HashSet<ProductModel>();
					if (CollectionUtils.isNotEmpty(existingProductList))
					{
						finalProductList.addAll(existingProductList);
					}
					finalProductList.addAll(newProductModelList);
					productModelSet.addAll(finalProductList);
					finalProductList.clear();
					finalProductList.addAll(productModelSet);
					paramMODEL.setProducts(finalProductList); //Setting the products in product restriction
				}

			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}
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





}
