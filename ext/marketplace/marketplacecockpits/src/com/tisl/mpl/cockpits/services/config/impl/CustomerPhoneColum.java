package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Collection;
import java.util.Locale;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractCustomerNameCustomColumn;
import de.hybris.platform.mobileservices.model.text.UserPhoneNumberModel;

public class CustomerPhoneColum extends AbstractCustomerNameCustomColumn {

	@Override
	protected String getCustomerNameValue(UserModel user, Locale locale)
			throws ValueHandlerException {
		// TODO Auto-generated method stub
		if (user != null) {
			/*if(user.getDefaultShipmentAddress()!=null)
			{
				return user.getDefaultShipmentAddress().getPhone1();
			}*/
				String phoneNumber = null;

			if (user instanceof CustomerModel) {
				
				CustomerModel customer=(CustomerModel) user;
				phoneNumber = customer.getMobileNumber();
				/*if(customer.getDefaultShipmentAddress()!=null)
				{
					return customer.getDefaultShipmentAddress().getPhone1();
				}*/

			}
			return phoneNumber;
		}
		return "";
	}
}


