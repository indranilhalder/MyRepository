/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.pincode.impl;

import de.hybris.platform.core.model.product.PincodeModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.PincodeDetailsDao;
import com.tisl.mpl.marketplacecommerceservices.pincode.PincodeService;

/**
 * @author Dileep
 *
 */
public class PincodeServiceImpl implements PincodeService
{
	@Autowired
	private PincodeDetailsDao pincodeDetailsDao;
	
	private static final Logger LOG = Logger.getLogger(PincodeServiceImpl.class);

	@Override
	public PincodeModel getDetails(String pincode)
	{
		PincodeModel pincodeModel;
		LOG.info("Pincode Service class ");
		try
		{
			pincodeModel = pincodeDetailsDao.getPincodeModel(pincode);
			LOG.info("Getting all the details of the pincode in  Service Class");
		}
		catch(Exception e)
		{
			throw new EtailBusinessExceptions();
		}
		return pincodeModel;
	}

}
