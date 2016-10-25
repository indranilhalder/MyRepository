/**
 * 
 */
package com.tisl.mpl.facade.codpayment.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;
import com.tisl.mpl.data.CODSelfShipData;

/**
 * @author TECHOUTS
 *
 */
public class MplCustomerBankDetailsReversePopulator implements Populator<CODSelfShipData,MplCustomerBankAccountDetailsModel>
{

	
	@Override
	public void populate(CODSelfShipData source, MplCustomerBankAccountDetailsModel target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		
		target.setAccountNumber(source.getBankAccount());
		target.setAccountHolderName(source.getName());
		target.setCustomerName(source.getName());
		target.setBankName(source.getBankName());
		target.setCustomerId(source.getCustomerNumber());
		target.setIfscCode(source.getBankKey());
		target.setPaymentMode(source.getPaymentMode());
		target.setTitle(source.getTitle());
		target.setOrderTag(source.getOrderTag());
	}

}
