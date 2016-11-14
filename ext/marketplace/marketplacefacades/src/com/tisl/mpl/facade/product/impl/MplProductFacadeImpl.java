/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;

import com.tisl.mpl.facade.product.MplProductFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplProductService;


/**
 * @author i313024
 *
 */
public class MplProductFacadeImpl implements MplProductFacade
{

	private MplProductService mplProductService;



	/**
	 * @return the mplProductService
	 */
	public MplProductService getMplProductService()
	{
		return mplProductService;
	}



	/**
	 * @param mplProductService
	 *           the mplProductService to set
	 */
	public void setMplProductService(final MplProductService mplProductService)
	{
		this.mplProductService = mplProductService;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.product.MplProductFacade#getProductFeatureModelByProductAndQualifier(de.hybris.platform.
	 * commercefacades.product.data.ProductData, java.lang.String)
	 */
	@Override
	public ProductFeatureModel getProductFeatureModelByProductAndQualifier(final ProductData product, final String qualifier)
	{
		// YTODO Auto-generated method stub
		if (product != null & qualifier != null)
		{
			final String code = product.getCode();
			final List<ProductFeatureModel> productFeatures = mplProductService.findProductFeaturesByQualifierAndProductCode(code,
					qualifier);
			if (productFeatures != null && productFeatures.size() > 0)
			{
				return productFeatures.get(0);
			}
		}
		return null;
	}

}
