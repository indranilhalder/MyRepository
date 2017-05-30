/**
 *
 */
package com.tisl.mpl.seller.product.facades.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.Map;

import javax.annotation.Resource;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
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
	private ProductOfferDetailService prodOfferDetService;

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


	//update the message for Freebie product TPR-1754
	/**
	 * @Description Added for displaying freebie messages other than default freebie message
	 * @param ussId
	 * @return freebie message
	 */
	@Override
	public Map<String, String> showFreebieMessage(final String ussId)
			throws EtailNonBusinessExceptions, FlexibleSearchException, UnknownIdentifierException
	{
		return prodOfferDetService.showFreebieMessage(ussId);
	}
}
