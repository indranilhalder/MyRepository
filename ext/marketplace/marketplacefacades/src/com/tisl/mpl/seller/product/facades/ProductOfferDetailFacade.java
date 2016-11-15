/**
 *
 */
package com.tisl.mpl.seller.product.facades;

import java.util.Map;


/**
 * @author TCS
 *
 */
//Added for displaying Non HMC configurable offer messages , TPR-589
public interface ProductOfferDetailFacade
{


	public Map<String, Map<String, String>> showOfferMessage(final String productCode);

	//update the message for Freebie product TPR-1754

	public Map<String, Map<String, String>> showFreebieMessage(String ussId);
}
