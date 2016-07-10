/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.PasswordMismatchException;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import com.tisl.mpl.facades.product.data.GenderData;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;


/**
 * @author 594031
 *
 */
public interface MplCustomerProfileService
{

	/**
	 * @param emailid
	 * @return MplCustomerProfileData
	 */
	MplCustomerProfileData getCustomerProfileDetail(String emailid);

	/**
	 * @param customer
	 * @param name
	 * @param uid
	 */
	void updateCustomerProfile(CustomerModel customer, String name, String uid);

	/**
	 * @return
	 */
	List<GenderData> getGenders();

	/**
	 * @param newUid
	 * @param currentPassword
	 */
	void changeUid(String paramString1, String paramString2) throws DuplicateUidException, PasswordMismatchException;

	/**
	 * @param emailId
	 * @return
	 */
	boolean checkUniquenessOfEmail(String emailId);

}
