/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import de.hybris.platform.servicelayer.search.SearchResult;


/**
 * @author TCS
 *
 */
//Added for displaying Non HMC configurable offer messages , TPR-589
public interface ProductOfferDetailDao
{
	public SearchResult<List<Object>> showOfferMessage(String productCode, Boolean isPwa);

	//update the message for Freebie product TPR-1754
	public SearchResult<List<Object>> showFreebieMessage(String ussId);
}
