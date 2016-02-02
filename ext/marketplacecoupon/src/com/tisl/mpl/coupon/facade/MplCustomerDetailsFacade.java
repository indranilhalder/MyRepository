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

	//void getCustomer(String eventType, CurrencyModel currency, Double discountVal, Boolean isFreeShipping, Integer noOfDays);

	void getCartCustomer(String eventType, CurrencyModel currency, Double discountVal, Boolean isFreeShipping, Integer noOfDays);

	Map<String, List<PrincipalModel>> anniversaryVoucherDetails(String eventType, Date eventStartDate, Date eventEndDate);

	Map<String, List<PrincipalModel>> birthdayVoucherDetails(String eventType, Date eventStartDate, Date eventEndDate);

	Map<String, List<PrincipalModel>> purchaseBasedVoucherDetails(String eventType, Date eventStartDate, Date eventEndDate,
			Double purAmtOrderValue);

	Map<String, List<PrincipalModel>> firstTimeRegVoucherDetails(String eventType, Date eventStartDate, Date eventEndDate,
			int firstTimeRegNoOfDays);

	Map<String, List<PrincipalModel>> cartNotShoppedVoucherDetails(String eventType, int cartNotShoppedNoOfDays);

	Map<String, List<PrincipalModel>> cartAbanAtPmtPageDetails(String eventType, Date eventStartDate, Date eventEndDate,
			Boolean cartAbanIsGreater, Double cartAbanCartValue);

	Map<String, List<PrincipalModel>> cartAbanAtCartPageDetails(String eventType, Date eventStartDate, Date eventEndDate,
			Boolean cartAbanIsGreater, Double cartAbanCartValue);

	void saveVoucherVals(Map<String, List<PrincipalModel>> eventCustomerMap, CurrencyModel currency, Double discountVal,
			String voucherCode, Date couponStartDate, Date couponEndDate, int redemptionLmtPerUser, int redemptionQtyLimit);


}