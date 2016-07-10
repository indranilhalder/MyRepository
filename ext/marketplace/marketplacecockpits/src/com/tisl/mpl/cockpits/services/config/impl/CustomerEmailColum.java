package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractCustomerNameCustomColumn;
import de.hybris.platform.cscockpit.services.label.impl.CustomerModelLabelProvider;
import org.zkoss.spring.SpringUtil;

public class CustomerEmailColum extends AbstractCustomerNameCustomColumn {
	
	protected CustomerModelLabelProvider getCustomerModelLabelProvider() {
		return ((CustomerModelLabelProvider) SpringUtil
				.getBean("customerModelLabelProvider"));
	}

	@Override
	protected String getCustomerNameValue(UserModel paramUserModel,
			Locale paramLocale) throws ValueHandlerException {
		// TODO Auto-generated method stub
		
		if (paramUserModel != null) {
			String email = paramUserModel.getUid();

			if (paramUserModel instanceof CustomerModel) {
				CustomerModel customer=(CustomerModel) paramUserModel;
				email=customer.getOriginalUid();

			}
			return email;
		}
		return "";
	}

}
