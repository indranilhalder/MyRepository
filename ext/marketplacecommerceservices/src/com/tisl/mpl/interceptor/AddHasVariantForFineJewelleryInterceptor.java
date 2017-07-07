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
			final Boolean isHasVariant = Boolean.valueOf(true);
			if (sellerInfo.getProductSource() != null)
			{
				listingId = sellerInfo.getProductSource().getCode();
				final List<ProductModel> findProductsByCode = defaultProductDao.findProductsByCode(listingId);
				for (final ProductModel product : findProductsByCode)
				{
					categoryId = product.getProductCategoryType();
				}
				if (categoryId != null && categoryId.equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))
				{
					sellerInfo.setHasVariant(isHasVariant);
				}

			}
		}
	}
}
