/**
 *
 */
package com.tisl.mpl.facade.pincode.impl;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.pincode.MplPincodeFacede;
import com.tisl.mpl.facades.data.PincodeData;
import com.tisl.mpl.marketplacecommerceservices.pincode.PincodeService;


/**
 * @author Dileep
 *
 */
public class MplPincodeFacadeImpl implements MplPincodeFacede
{
	@Autowired
	private PincodeService mplPincodeDetailsService;
	@Autowired
	private Converter<PincodeModel, PincodeData> mplPincodeConverter;

	private static final Logger LOG = Logger.getLogger(MplPincodeFacadeImpl.class);


	@Override
	public PincodeData getAllDetails(final String pincode)
	{
		PincodeData pincodeData = null;
		try
		{
			final PincodeModel pincodeModel = mplPincodeDetailsService.getDetails(pincode);
			LOG.info(" :: Converter Data to Model :: ");
			pincodeData = getMplPincodeConverter().convert(pincodeModel);
		}
		catch (final EtailBusinessExceptions businessException)
		{
			throw businessException;
		}
		catch (final EtailNonBusinessExceptions nonBusiness)
		{
			throw nonBusiness;
		}
		catch (final Exception e)
		{
			throw new EtailBusinessExceptions();
		}
		return pincodeData;
	}


	/**
	 * @return the mplPincodeConverter
	 */
	public Converter<PincodeModel, PincodeData> getMplPincodeConverter()
	{
		return mplPincodeConverter;
	}


	/**
	 * @param mplPincodeConverter
	 *           the mplPincodeConverter to set
	 */
	public void setMplPincodeConverter(final Converter<PincodeModel, PincodeData> mplPincodeConverter)
	{
		this.mplPincodeConverter = mplPincodeConverter;
	}



}
