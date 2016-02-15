package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractCustomerNameCustomColumn;

public class CustomerFirstNameColumn extends AbstractCustomerNameCustomColumn{
	
	private static final String EMPTY_SPACE = "";

	@Override
	protected String getCustomerNameValue(UserModel paramUserModel,
			Locale paramLocale) throws ValueHandlerException {
		// TODO Auto-generated method stub
		if (paramUserModel != null) {
			String firstName = paramUserModel.getName();

			if (paramUserModel instanceof CustomerModel) {
				CustomerModel customer=(CustomerModel) paramUserModel;
				firstName=customer.getFirstName();

			}
			return firstName;
		}
		return EMPTY_SPACE;
	}

}
