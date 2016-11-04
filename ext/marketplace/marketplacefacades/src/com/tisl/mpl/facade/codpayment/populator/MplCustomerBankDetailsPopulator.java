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
public class MplCustomerBankDetailsPopulator implements Populator<MplCustomerBankAccountDetailsModel, CODSelfShipData>
{

	
	@Override
	public void populate(MplCustomerBankAccountDetailsModel source, CODSelfShipData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		
		target.setBankAccount(source.getAccountNumber());
		target.setName(source.getAccountHolderName());
		target.setBankName(source.getBankName());
		target.setCustomerNumber(source.getCustomerId());
		target.setBankKey(source.getIfscCode());
		target.setPaymentMode(source.getPaymentMode());
		target.setTitle(source.getTitle());
	}

}
