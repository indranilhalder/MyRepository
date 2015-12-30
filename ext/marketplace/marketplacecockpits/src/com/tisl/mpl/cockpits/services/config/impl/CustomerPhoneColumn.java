package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractCustomerNameCustomColumn;

public class CustomerPhoneColumn extends AbstractCustomerNameCustomColumn {
	
	private static final String EMPTY_SPACE = "";

	@Override
	protected String getCustomerNameValue(UserModel user, Locale locale)
			throws ValueHandlerException {
		// TODO Auto-generated method stub
		if (user != null) {
				String phoneNumber = null;

			if (user instanceof CustomerModel) {
				CustomerModel customer=(CustomerModel) user;
				phoneNumber = customer.getMobileNumber();
			}
			return phoneNumber;
		}
		return EMPTY_SPACE;
	}
}


