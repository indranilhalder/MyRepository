/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.facades.product.data.ExtRegisterData;


/**
 * @author 314180
 * 
 */
public interface ExtDefaultCustomerService extends CustomerAccountService
{
	void changeUid(String newUid) throws DuplicateUidException;

	public void registerUser(final CustomerModel customerModel, final String password, final String affiliateId)
			throws DuplicateUidException;

	public void registerCockpit(final CustomerModel customerModel, final String password) throws DuplicateUidException;

	public void internalSaveCustomerForSocialLogin(final CustomerModel customerModel);

	public ExtRegisterData registerUserForSocialSignup(final CustomerModel customerModel);

	public String generateOTPNumber(final String customerUid);
}
