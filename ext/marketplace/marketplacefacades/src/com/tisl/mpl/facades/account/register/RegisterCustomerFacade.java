/**
 *
 */
package com.tisl.mpl.facades.account.register;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import com.tisl.mpl.facades.product.data.ExtRegisterData;
import com.tisl.mpl.facades.product.data.SendInvoiceData;


/**
 * @author 594031
 *
 */
public interface RegisterCustomerFacade
{
	void register(ExtRegisterData registerData) throws DuplicateUidException, UnknownIdentifierException, IllegalArgumentException;

	/**
	 * @param data
	 * @return
	 */
	ExtRegisterData registerSocial(ExtRegisterData registerData, final boolean isMobile)
			throws UnknownIdentifierException, IllegalArgumentException;

	void forgottenPassword(final String uid);

	/**
	 * @param invoiceData
	 */
	public void sendInvoice(final SendInvoiceData sendInvoiceData, CustomerModel customerModel);


	/**
	 * @param data
	 * @return
	 */
	boolean checkUniquenessOfEmail(ExtRegisterData data);

	/**
	 * @param data
	 * @param isMobile
	 * @param timestamp
	 * @param signature
	 * @return
	 */
	ExtRegisterData registerSocialforMobile(ExtRegisterData data, boolean isMobile, String timestamp, String signature);
}
