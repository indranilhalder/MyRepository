/**
 *
 */
package com.tisl.mpl.coupon.facade;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.security.PrincipalModel;

import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * @author TCS
 *
 */
public interface MplCustomerDetailsFacade
{

	/**
	 *
	 * @param eventType
	 * @param currency
	 * @param discountVal
	 * @param isFreeShipping
	 * @param noOfDays
	 */
	void getCartCustomer(String eventType, CurrencyModel currency, Double discountVal, Boolean isFreeShipping, Integer noOfDays);

	/**
	 *
	 * @param eventType
	 * @param eventStartDate
	 * @param eventEndDate
	 * @return Map<String, List<PrincipalModel>>
	 */
	Map<String, List<PrincipalModel>> anniversaryVoucherDetails(String eventType, Date eventStartDate, Date eventEndDate);

	/**
	 *
	 * @param eventType
	 * @param eventStartDate
	 * @param eventEndDate
	 * @return Map<String, List<PrincipalModel>>
	 */
	Map<String, List<PrincipalModel>> birthdayVoucherDetails(String eventType, Date eventStartDate, Date eventEndDate);

	/**
	 *
	 * @param eventType
	 * @param eventStartDate
	 * @param eventEndDate
	 * @param purAmtOrderValue
	 * @return Map<String, List<PrincipalModel>>
	 */
	Map<String, List<PrincipalModel>> purchaseBasedVoucherDetails(String eventType, Date eventStartDate, Date eventEndDate,
			Double purAmtOrderValue);

	/**
	 *
	 * @param eventType
	 * @param eventStartDate
	 * @param eventEndDate
	 * @param firstTimeRegNoOfDays
	 * @return Map<String, List<PrincipalModel>>
	 */
	Map<String, List<PrincipalModel>> firstTimeRegVoucherDetails(String eventType, Date eventStartDate, Date eventEndDate,
			int firstTimeRegNoOfDays);

	/**
	 *
	 * @param eventType
	 * @param cartNotShoppedNoOfDays
	 * @return Map<String, List<PrincipalModel>>
	 */
	Map<String, List<PrincipalModel>> cartNotShoppedVoucherDetails(String eventType, int cartNotShoppedNoOfDays);

	/**
	 *
	 * @param eventType
	 * @param eventStartDate
	 * @param eventEndDate
	 * @param cartAbanIsGreater
	 * @param cartAbanCartValue
	 * @return Map<String, List<PrincipalModel>>
	 */
	Map<String, List<PrincipalModel>> cartAbanAtPmtPageDetails(String eventType, Date eventStartDate, Date eventEndDate,
			Boolean cartAbanIsGreater, Double cartAbanCartValue);

	/**
	 *
	 * @param eventType
	 * @param eventStartDate
	 * @param eventEndDate
	 * @param cartAbanIsGreater
	 * @param cartAbanCartValue
	 * @return Map<String, List<PrincipalModel>>
	 */
	Map<String, List<PrincipalModel>> cartAbanAtCartPageDetails(String eventType, Date eventStartDate, Date eventEndDate,
			Boolean cartAbanIsGreater, Double cartAbanCartValue);

	/**
	 *
	 * @param eventCustomerMap
	 * @param currency
	 * @param discountVal
	 * @param voucherCode
	 * @param couponStartDate
	 * @param couponEndDate
	 * @param redemptionLmtPerUser
	 * @param redemptionQtyLimit
	 */
	void saveVoucherVals(Map<String, List<PrincipalModel>> eventCustomerMap, CurrencyModel currency, Double discountVal,
			String voucherCode, Date couponStartDate, Date couponEndDate, int redemptionLmtPerUser, int redemptionQtyLimit);


}