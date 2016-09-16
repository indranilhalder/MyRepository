/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;


/**
 * @author TCS
 *
 */
//Added for displaying Non HMC configurable offer messages , TPR-589
public interface ProductOfferDetailDao
{
	public SearchResult<List<Object>> showOfferMessage(String productCode);
}
