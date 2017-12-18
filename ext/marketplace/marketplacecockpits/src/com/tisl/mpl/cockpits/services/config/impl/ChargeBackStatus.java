package com.tisl.mpl.cockpits.services.config.impl;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

public class ChargeBackStatus extends AbstractSimpleCustomColumnConfiguration <String, ItemModel>
{
	public static final String PAYMENT_STATUS = "Unsuccessful_Chargeback";
	@Override
	protected String getItemValue(ItemModel itemModel, Locale locale)
			throws ValueHandlerException {
		
		String paymentstatus = StringUtils.EMPTY;
		if(itemModel instanceof PaymentTransactionEntryModel)
		{
			final PaymentTransactionEntryModel paymentEntry = (PaymentTransactionEntryModel) itemModel;
			final PaymentTransactionModel paymentTransMod = paymentEntry.getPaymentTransaction();
			
			if(paymentEntry != null && paymentEntry.getAmount() !=null && (paymentEntry.getAmount().compareTo(new BigDecimal(0.00)) == 0)){
				
				paymentstatus = PAYMENT_STATUS;
			}
			else
			{
				paymentstatus = paymentTransMod.getStatus();
			}
		}
			return paymentstatus;

	}
}