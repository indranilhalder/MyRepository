/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.product.impl.MplProductDaoImpl;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public class AddHasVariantForFineJewelleryInterceptor implements PrepareInterceptor
{

	@Resource(name = "defaultProductDao")
	MplProductDaoImpl defaultProductDao;

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AddHasVariantForFineJewelleryInterceptor.class);


	@Override
	public void onPrepare(final Object model, final InterceptorContext arg) throws InterceptorException
	{
		if (model instanceof SellerInformationModel)
		{
			final SellerInformationModel sellerInfo = (SellerInformationModel) model;
			String listingId = null;
			String categoryId = null;
			//	final Boolean isHasVariant = Boolean.valueOf(true);//SONAR FIX JEWELLERY
			final Boolean isHasVariant = Boolean.TRUE;
			if (null != sellerInfo.getProductSource())
			{
				if (StringUtils.isNotEmpty(sellerInfo.getProductSource().getCode()))
				{
					listingId = sellerInfo.getProductSource().getCode();
					final List<ProductModel> findProductsByCode = defaultProductDao.findProductForHasVariant(listingId);
					if (CollectionUtils.isNotEmpty(findProductsByCode))
					{
						for (final ProductModel product : findProductsByCode)
						{
							if (StringUtils.isNotEmpty(product.getProductCategoryType()))
							{
								categoryId = product.getProductCategoryType();
								break;
							}
						}
					}
					if (StringUtils.isNotEmpty(categoryId)
							&& MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(categoryId))
					{
						sellerInfo.setHasVariant(isHasVariant);
					}

				}
			}
		}
	}
}
