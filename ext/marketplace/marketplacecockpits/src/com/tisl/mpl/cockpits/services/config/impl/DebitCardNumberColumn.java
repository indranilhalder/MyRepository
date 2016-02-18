package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;

public class DebitCardNumberColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	@Override
	protected Object getItemValue(ItemModel model, Locale arg1)
			throws ValueHandlerException {
		if (model instanceof DebitCardPaymentInfoModel) {
			String debitCardNumber = ((DebitCardPaymentInfoModel) model)
					.getNumber();
			if (StringUtils.isNotEmpty(debitCardNumber)) {
				debitCardNumber = debitCardNumber.substring(Math.max(0,
						debitCardNumber.length() - 4));
				return "XXXX"+debitCardNumber;
			}
		}
		return StringUtils.EMPTY;
	}
}