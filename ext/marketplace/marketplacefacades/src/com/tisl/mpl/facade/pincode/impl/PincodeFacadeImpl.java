/**
 * 
 */
package com.tisl.mpl.facade.pincode.impl;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.facade.pincode.PincodeFacede;
import com.tisl.mpl.facades.data.PincodeData;
import com.tisl.mpl.marketplacecommerceservices.pincode.PincodeService;

/**
 * @author Dileep
 *
 */
public class PincodeFacadeImpl implements PincodeFacede
{
	@Autowired
	private PincodeService pincodeDetailsService;
	@Autowired
	private Converter<PincodeModel, PincodeData> pincodeConverter;
	
	private static final Logger LOG = Logger.getLogger(PincodeFacadeImpl.class);
	
	
	@Override
	public PincodeData getAllDetails(String pincode)
	{
		PincodeData pincodeData = null;
		LOG.info("Pincode Facade Class ");
		try
		{
			PincodeModel pincodeModel = pincodeDetailsService.getDetails(pincode);
			LOG.info("Getting Pincode Model All the Details of the Pincode and call to Converted");
			pincodeData = getPincodeConverter().convert(pincodeModel);
			LOG.info("After Converter Call");
		}
		catch(Exception e)
		{
			throw new EtailBusinessExceptions();
		}
		return pincodeData;
	}
	
	/**
	 * @return the pincodeConverter
	 */
	public Converter<PincodeModel, PincodeData> getPincodeConverter()
	{
		return pincodeConverter;
	}
	/**
	 * @param pincodeConverter the pincodeConverter to set
	 */
	public void setPincodeConverter(Converter<PincodeModel, PincodeData> pincodeConverter)
	{
		this.pincodeConverter = pincodeConverter;
	}

}
