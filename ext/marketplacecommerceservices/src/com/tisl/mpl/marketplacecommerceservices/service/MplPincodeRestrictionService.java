/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;

import java.util.List;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;




/**
 * @author TCS
 *
 */
public interface MplPincodeRestrictionService
{



	/*
	 * public List<RestrictionsetupModel> getRestrictedPincode(final String articleSKUID, final String sellerId, final
	 * String listingID, final String pincode, String categoryCode) throws EtailNonBusinessExceptions;
	 */

	public List<PincodeServiceData> getRestrictedPincode(final List<String> articleSKUID, final List<String> sellerId,
			final String listingID, final String pincode, final List<PincodeServiceData> reqData) throws EtailNonBusinessExceptions;

	/**
	 * @param pincode
	 * @param reqData
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */
	public List<PincodeServiceData> getRestrictedPincodeCart(final String pincode, final List<PincodeServiceData> reqData)
			throws EtailNonBusinessExceptions;
}
