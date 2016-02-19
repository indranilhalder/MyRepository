/**
 *
 */
package com.tisl.mpl.facades.account.register;

import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import com.tisl.mpl.enums.OTPTypeEnum;


/**
 * @author TCS
 *
 */
public interface ForgetPasswordFacade
{

	void forgottenPasswordForEmail(final String uid, final String securePasswordUrl);

	void updatePassword(String token, String newPassword) throws TokenInvalidatedException;

	boolean checkPasswordEncodedFormat(String emailId, String currPwd);

	String getOriginalToken(String tokenFromURL, String emailId);

	String forgottenPassword(String email);

	String validateMobile(String email);

	String generateOTPNumber(String emailId);

	boolean validateOTP(String uid, String enteredOTPNumber, OTPTypeEnum OTPType, long expiryTime);

	void sendEmailForUpdateCustomerProfile(CustomerModel currentUser, List<String> updatedDetailList, String profileUpdateUrl);

}
