/**
 *
 */
package com.tisl.mpl.facades.account.register;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.order.OrderModel;
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
	void register(ExtRegisterData registerData, final int platformNumber) throws DuplicateUidException,
			UnknownIdentifierException, IllegalArgumentException;//TPR-6272 parameter platformNumber added

	/**
	 * @param data
	 * @return
	 */
	ExtRegisterData registerSocial(ExtRegisterData registerData, final boolean isMobile, final int platformNumber)
			throws UnknownIdentifierException, IllegalArgumentException; //SDI-639

	void forgottenPassword(final String uid);

	/**
	 * @param invoiceData
	 */
	public void sendInvoice(final SendInvoiceData sendInvoiceData, CustomerModel customerModel, OrderModel orderModel);



	/**
	 * @param data
	 * @return
	 */
	public boolean checkUniquenessOfEmail(ExtRegisterData data);

	//NU-30
	public boolean checkMobileNumberUnique(ExtRegisterData data);

	//NU-30
	public boolean checkEmailIdUnique(ExtRegisterData data);

}
