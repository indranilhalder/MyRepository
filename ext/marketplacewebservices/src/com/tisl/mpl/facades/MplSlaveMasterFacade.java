/**
 * 
 */
package com.tisl.mpl.facades;

import de.hybris.platform.storelocator.model.PointOfServiceModel;

import com.tisl.mpl.wsdto.SellerSlaveDTO;


/**
 * @author TECH
 * 
 */
public interface MplSlaveMasterFacade
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
	 * Get a PointOfService model for a given slaveId
	 * 
	 * @author TECH
	 * @param slaveId
	 * @return PointOfServiceModel
	 */
	public PointOfServiceModel checkPOSForSlave(final String slaveId);

}
