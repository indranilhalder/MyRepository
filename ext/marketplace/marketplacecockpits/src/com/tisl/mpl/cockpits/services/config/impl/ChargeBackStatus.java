package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

public class ChargeBackStatus extends AbstractSimpleCustomColumnConfiguration <String, ItemModel>
{
	public static final String PAYMENT_STATUS = "Unsuccessful_ Chargeback";
	@Override
	protected String getItemValue(ItemModel itemModel, Locale locale)
			throws ValueHandlerException {
		
		String paymentstatus = StringUtils.EMPTY;
		if(itemModel instanceof PaymentTransactionEntryModel)
		{
			final PaymentTransactionEntryModel paymentEntry = (PaymentTransactionEntryModel) itemModel;
			final PaymentTransactionModel paymentTransMod = paymentEntry.getPaymentTransaction();
			
			if(paymentTransMod != null && paymentTransMod.getPlannedAmount().equals(Double.valueOf(0.00)))
			{
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