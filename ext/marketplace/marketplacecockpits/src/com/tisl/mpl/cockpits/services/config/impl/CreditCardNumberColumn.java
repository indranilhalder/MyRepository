package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;

public class CreditCardNumberColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	@Override
	protected Object getItemValue(ItemModel model, Locale arg1)
			throws ValueHandlerException {
		if (model instanceof CreditCardPaymentInfoModel) {
			String creditCardNumber = ((CreditCardPaymentInfoModel) model)
					.getNumber();
			if (StringUtils.isNotEmpty(creditCardNumber)) {
				creditCardNumber = creditCardNumber.substring(Math.max(0,
						creditCardNumber.length() - 4));
				return "XXXX"+creditCardNumber;
			}
		}
		return StringUtils.EMPTY;
	}
}