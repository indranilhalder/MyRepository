package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractCustomerNameCustomColumn;

public class CustomerIdColumn extends AbstractCustomerNameCustomColumn {
	
	private static final String EMPTY_SPACE = "";

	@Override
	protected String getCustomerNameValue(UserModel userModel, Locale arg1)
			throws ValueHandlerException {
		// TODO Auto-generated method stub
		if (userModel != null) {
			String customerId = userModel.getUid();

			if (userModel instanceof CustomerModel) {
				CustomerModel customer=(CustomerModel) userModel;
				customerId=customer.getUid();

			}
			return customerId;
		}
		return EMPTY_SPACE;
	}

}
