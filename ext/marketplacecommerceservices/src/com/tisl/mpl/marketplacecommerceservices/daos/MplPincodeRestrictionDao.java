/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;


import java.util.List;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.RestrictionsetupModel;


/**
 * @author TCS
 *
 */
public interface MplPincodeRestrictionDao
{

	public List<RestrictionsetupModel> getRestrictedPincode(final String pincode, List<String> articleSKUID,
			List<String> sellerId, String listingID, List<String> categoryCode) throws EtailNonBusinessExceptions;

	/**
	 * @param pincode
	 * @param articleSKUID
	 * @param sellerId
	 * @param productCode
	 * @param categoryCode
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */
	List<RestrictionsetupModel> getRestrictedPincodeCart(String pincode, List<String> articleSKUID, List<String> sellerId,
			List<String> productCode, List<String> categoryCode) throws EtailNonBusinessExceptions;
}
