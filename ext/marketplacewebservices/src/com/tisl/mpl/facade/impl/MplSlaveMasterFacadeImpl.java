/**
 * 
 */
package com.tisl.mpl.facade.impl;

import de.hybris.platform.storelocator.model.PointOfServiceModel;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.facades.MplSlaveMasterFacade;
import com.tisl.mpl.service.MplSlaveMasterService;
import com.tisl.mpl.wsdto.SellerSlaveDTO;

/**
 * @author TECH
 *
 */
public class MplSlaveMasterFacadeImpl implements MplSlaveMasterFacade
{
	
	private static final Logger LOG = Logger.getLogger(MplSlaveMasterFacadeImpl.class);
	
	@Resource(name = "mplSlaveMasterService")
	private MplSlaveMasterService mplSlaveMasterService;

	/**
	 * For SlaveOnboarding.
	 * @param sellerSlaveDto
	 * @return 
	 */
	@Override
	public String insertUpdate(final SellerSlaveDTO sellerSlaveDto)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from insertUpdate method");
		}
		// YTODO Auto-generated method stub
		return mplSlaveMasterService.insertUpdate(sellerSlaveDto);
	}

	/**
	 * Gets PointOfService model for given sellerId and slaveId.
	 * @param sellerId
	 * @param slaveId
	 * @return PointOfService model
	 */
	@Override
	public PointOfServiceModel findPOSBySellerAndSlave(final String sellerId, final String slaveId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from findPOSBySellerAndSlave method in facade");
		}
		return mplSlaveMasterService.findPOSBySellerAndSlave(sellerId, slaveId);
	}

	/**
	 * Gets a PointOfService Model for a given pos name.
	 * @param storeName.
	 * @return PointOfService Model
	 */
	@Override
	public PointOfServiceModel findPOSByName(final String posName)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from findPOSByName method in facade");
		}
		return mplSlaveMasterService.findPOSByName(posName);
	}

	/**
	 * Gets a PointOfService Model for a given slaveId.
	 * @param slaveId
	 * @return PointOfService Model
	 */
	@Override
	public PointOfServiceModel checkPOSForSlave(final String slaveId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from checkPOSForSlave method in facade");
		}
		return mplSlaveMasterService.checkPOSForSlave(slaveId);
	}

}
