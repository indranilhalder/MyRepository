package com.tisl.mpl.cockpits.services.config.impl;


import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.EMIPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

public class PaymentModeColumn  extends AbstractSimpleCustomColumnConfiguration<String, ItemModel> {
	
	public static final String EMI_PAYMENT_MODE = "CREDIT-EMI";
	@Override
	protected String getItemValue(ItemModel itemModel, Locale locale)
			throws ValueHandlerException {
		
		String paymentmode = StringUtils.EMPTY;
		if(itemModel instanceof PaymentTransactionEntryModel)
		{
			final PaymentTransactionEntryModel ptE = (PaymentTransactionEntryModel) itemModel;
			final PaymentTransactionModel pt = ptE.getPaymentTransaction();
			final AbstractOrderModel order = pt.getOrder();
			final PaymentInfoModel paymentInfo = order.getPaymentInfo();
			if(paymentInfo != null && paymentInfo instanceof EMIPaymentInfoModel)
			{
				if(StringUtils.isNotEmpty(paymentInfo.getCode()))
				{
					paymentmode = EMI_PAYMENT_MODE;
				}
			}
			else
			{
				paymentmode = ptE.getPaymentMode().getMode();
			}
		}
		return paymentmode;
	}
}

