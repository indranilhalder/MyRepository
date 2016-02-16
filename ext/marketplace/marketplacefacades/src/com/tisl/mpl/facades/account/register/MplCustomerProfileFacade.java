/**
 *
 */
package com.tisl.mpl.facades.account.register;

import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.facades.product.data.DayData;
import com.tisl.mpl.facades.product.data.GenderData;
import com.tisl.mpl.facades.product.data.MonthData;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.product.data.YearData;


/**
 * @author TCS
 *
 */
public interface MplCustomerProfileFacade
{

	/**
	 * @param email
	 * @return MplCustomerProfileData
	 */
	public MplCustomerProfileData getCustomerProfileDetail(String email);

	/**
	 * @param mplCustomerProfileData
	 * @throws DuplicateUidException
	 */
	public void updateCustomerProfile(MplCustomerProfileData mplCustomerProfileData) throws DuplicateUidException;

	/**
	 * @return List
	 */
	public List<GenderData> getGenders();

	/**
	 * @return List
	 */
	public List<DayData> getDayList();

	/**
	 * @return List
	 */
	public List<MonthData> getMonthList();

	/**
	 * @return List
	 */
	public List<YearData> getYearList();

	/**
	 * @return List
	 */
	public List<YearData> getYearAnniversaryList();

	/**
	 *
	 * @param newUid
	 * @param currentPassword
	 * @throws DuplicateUidException
	 * @throws PasswordMismatchException
	 */
	public void changeUid(String newUid, String currentPassword) throws DuplicateUidException, PasswordMismatchException;

	/**
	 *
	 * @param mplCustomerProfileData
	 */
	public void sendEmailForUpdate(final MplCustomerProfileData mplCustomerProfileData);

	/**
	 * @param cardToken
	 */
	void removeCardDetail(String cardToken);

	/**
	 * @param emailId
	 * @return boolean
	 */
	boolean checkUniquenessOfEmail(String emailId);

	/**
	 *
	 * @param updatedDetailList
	 * @param profileUpdateUrl
	 */
	public void sendEmailForUpdateCustomerProfile(final List<String> updatedDetailList, final String profileUpdateUrl);

	/**
	 *
	 * @param suppliedPassword
	 * @return boolean
	 */
	public boolean changePassword(final String suppliedPassword);

	/**
	 * @param displayUid
	 */
	public Map<String, String> setPreviousDataToMap(String displayUid, String channel);

	/**
	 * @param preSavedDetailMap
	 * @param displayUid
	 * @param profileUpdateUrl
	 */
	public void checkChangesForSendingEmail(Map<String, String> preSavedDetailMap, String displayUid, String profileUpdateUrl);

	/**
	 * @param currentUser
	 */
	public void sendEmailForChangePassword(CustomerModel currentUser);


}
