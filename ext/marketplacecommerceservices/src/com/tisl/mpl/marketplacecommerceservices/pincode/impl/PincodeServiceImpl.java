/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.pincode.impl;

import de.hybris.platform.core.model.product.PincodeModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeDetailsDao;
import com.tisl.mpl.marketplacecommerceservices.pincode.PincodeService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author Dileep
 *
 */
public class PincodeServiceImpl implements PincodeService
{
	@Autowired
	private MplPincodeDetailsDao mplPincodeDetailsDao;

	private static final Logger LOG = Logger.getLogger(PincodeServiceImpl.class);

	@Override
	public PincodeModel getDetails(final String pincode)
	{
		PincodeModel pincodeModel = null;
		try
		{
			pincodeModel = mplPincodeDetailsDao.getPincodeModel(pincode);
		}
		catch (final EtailBusinessExceptions businessException)
		{
			ExceptionUtil.etailBusinessExceptionHandler(businessException, null);
		}
		catch (final Exception e)
		{
			LOG.error(" Fails to get Details of the pincode  :: " + e);
		}
		return pincodeModel;
	}

}
