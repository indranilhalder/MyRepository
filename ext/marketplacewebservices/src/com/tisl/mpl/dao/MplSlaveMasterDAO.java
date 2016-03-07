/**
 * 
 */
package com.tisl.mpl.dao;

import de.hybris.platform.storelocator.model.PointOfServiceModel;


/**
 * SlaveMaster DAO interface to perform slave related dao operation.
 * 
 * @author TECHOUTS
 * 
 */
public interface MplSlaveMasterDAO
{
	/**
	 * method to check existing slaves based on slaveId.
	 * 
	 * @param slaveId
	 * @return if available then return POS model else null.
	 */
	public PointOfServiceModel checkPOSForSlave(final String slaveId);

	public PointOfServiceModel findPOSForSellerAndSlave(final String sellerId, final String slaveId);

	public PointOfServiceModel findPOSForStoreName(final String posName);


}
