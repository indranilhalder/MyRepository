/**
 *
 */
package com.tisl.mpl.seller.product.facades.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.tisl.mpl.marketplacecommerceservices.service.ProductOfferDetailService;
import com.tisl.mpl.seller.product.facades.ProductOfferDetailFacade;


/**
 * @author TCS
 *
 */

//Added for displaying Non HMC configurable offer messages , TPR-589
public class ProductOfferDetailFacadeImpl implements ProductOfferDetailFacade
{

	@Resource(name = "prodOfferDetService")
	ProductOfferDetailService prodOfferDetService;

	/**
	 * @Description This method is used to fetch message from OfferDetail for a product
	 * @param productCode
	 * @return message
	 */
	@Override
	public Map<String, Map<String, String>> showOfferMessage(final String productCode)
	{
		return prodOfferDetService.showOfferMessage(productCode);
	}

}
