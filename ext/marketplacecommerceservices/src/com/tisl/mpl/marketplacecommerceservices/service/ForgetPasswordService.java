package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;



/**
 * @author 682160
 *
 */
public interface ForgetPasswordService
{

	List<CustomerModel> getCustomer(String email);

	String getOriginalToken(String tokenFromURL, CustomerModel customerModel);

	public void forgottenPasswordEmail(CustomerModel paramCustomerModel, String securePasswordUrl);

	void updatePassword(String token, String newPassword) throws TokenInvalidatedException;

	public String forgottenPasswordSMS(CustomerModel paramCustomerModel);

}
