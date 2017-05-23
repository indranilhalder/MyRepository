/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.product.PincodeModel;

/**
 * @author Dileep
 *
 */
public interface PincodeDetailsDao
{
	/**
	 * @Description Getting all the details  using the pincode
	 * @param pincode
	 * @return PincodeModel
	 */
	public PincodeModel getPincodeModel(final String pincode);
}
