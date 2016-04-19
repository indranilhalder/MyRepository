/**
 * 
 */
package com.tisl.mpl.service;

import de.hybris.platform.storelocator.model.PointOfServiceModel;

import com.tisl.mpl.wsdto.SellerSlaveDTO;


/**
 * @author TECHOUTS
 * 
 */
public interface MplSlaveMasterService
{
	/**
	 * 
	 * @param sellerSlaveDto
	 * @return
	 */
	public String insertUpdate(final SellerSlaveDTO sellerSlaveDto);

	/**
	 * 
	 * @param sellerId
	 * @param slaveId
	 * @return
	 */
	public PointOfServiceModel findPOSBySellerAndSlave(final String sellerId, final String slaveId);

	/**
	 * @param posName
	 * @return returns POSModel for given pos.
	 */
	public PointOfServiceModel findPOSByName(final String posName);
	
	/**
	 * Gets PointOfService model for a given slaveId.
	 * @param slaveId
	 * @return PointOfService model
	 */
	public PointOfServiceModel checkPOSForSlave(final String slaveId);
}
