/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.pincode;

import de.hybris.platform.core.model.product.PincodeModel;

/**
 * @author Dileep
 *
 */
public interface PincodeService
{
	/**
	 * @Description getting the pincodemodel using the pincode
	 * @param pincode
	 * @return PincodeModel
	 */
	public PincodeModel getDetails(final String pincode);
}
