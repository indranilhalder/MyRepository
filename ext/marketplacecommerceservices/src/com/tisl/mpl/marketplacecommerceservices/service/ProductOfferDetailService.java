/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.Map;


/**
 * @author TCS
 *
 */


//Added for displaying Non HMC configurable offer messages , TPR-589


public interface ProductOfferDetailService
{
	/**
	 * @Description This method is used to fetch message from OfferDetail for a product
	 * @param productCode
	 * @return message
	 */
	public Map<String, Map<String, String>> showOfferMessage(String productCode);

	//update the message for Freebie product TPR-1754

	public Map<String, Map<String, String>> showFreebieMessage(String ussId);
}
