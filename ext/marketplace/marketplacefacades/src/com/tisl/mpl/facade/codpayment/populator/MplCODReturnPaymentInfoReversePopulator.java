/**
 * 
 */
package com.tisl.mpl.facade.codpayment.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;



import org.springframework.util.Assert;

import com.tisl.mpl.core.model.BankDetailsInfoToFICOHistoryModel;
import com.tisl.mpl.data.CODSelfShipData;

/**
 * @author TECHOUTS
 *
 * populated CODSelfShipData to BankDetailsInfoToFICOHistoryModel
 */
public class MplCODReturnPaymentInfoReversePopulator implements Populator<CODSelfShipData,BankDetailsInfoToFICOHistoryModel>
{

	
	@Override
	public void populate(CODSelfShipData source, BankDetailsInfoToFICOHistoryModel target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		
		target.setAmount(source.getAmount());
		target.setBankBranch(source.getBankBranch());
		target.setBankCity(source.getBankCity());
		target.setBankCounrty(source.getBankCounrty());
		target.setBankKey(source.getBankKey());
		target.setBankName(source.getBankName());
		target.setBankStreet(source.getBankStreet());
		target.setCity(source.getCity());
		target.setCountry(source.getCountry());
		target.setCustomerNumber(source.getCustomerNumber());
		target.setName(source.getName());
		target.setOrderDate(source.getOrderDate());
		target.setOrderTag(source.getOrderTag());
		target.setOrderRefNo(source.getOrderRefNo());
		target.setPaymentMode(source.getPaymentMode());
		target.setPobox(source.getPobox());
		target.setRegion(source.getRegion());
		target.setStreet(source.getStreet());
		target.setTitle(source.getTitle());
		target.setTransactionID(source.getTransactionID());
		target.setTransactionDate(source.getTransactionDate());
		target.setTransactionType(source.getTransactionType());
	
	}

}
