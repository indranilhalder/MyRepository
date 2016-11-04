/**
 * 
 */
package com.tisl.mpl.storefront.web.forms.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.storefront.businessvalidator.CommonAsciiValidator;
import com.tisl.mpl.storefront.web.forms.MplReturnsForm;
import com.tisl.mpl.storefront.web.forms.ReturnPincodeCheckForm;

/**
 * @author Techouts
 *
 */
public class MplReturnFormValidator 
{
	private static final int MAX_FIELD_LENGTH_140 = 140;
	private static final int MAX_FIELD_LENGTH_10 = 10;
	private static final int MAX_FIELD_LENGTH_6 = 6;
	private static final int MAX_FIELD_LENGTH_11 = 11;
	private static final int MAX_FIELD_LENGTH_16 = 16;
	private static final int MAX_FIELD_LENGTH_UPDATED = 40;
	public static final String MOBILE_REGEX = "^[0-9]*$";
	public static final String NAME_REGEX = "[a-zA-Z]+\\.?";
	
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	
	public String returnValidate(final MplReturnsForm returnsForm)
	{
		final String errorMsg = validateStandardFields(returnsForm);
		return errorMsg;
	}
	
	public String validateStandardFields(final MplReturnsForm returnsForm)
	{
		
		 String returnReason=returnsForm.getReturnReason();
		 String refundType=returnsForm.getRefundType();
		 String accountNumber=returnsForm.getAccountNumber();
		 String reEnterAccountNumber=returnsForm.getAccountNumber();
		 String refundMode=returnsForm.getRefundMode();
		 String accountHolderName=returnsForm.getAccountHolderName();
		 String bankName=returnsForm.getBankName();
		 String iFSCCode=returnsForm.getiFSCCode();
		 String title=returnsForm.getTitle();
		 String returnMethod=returnsForm.getReturnMethod();
		 String firstName=returnsForm.getFirstName();
		 String [] storeIds=returnsForm.getStoreIds();
		 String addressType=returnsForm.getAddressType();
		 String lastName=returnsForm.getLastName();
		 String addrLine1=returnsForm.getAddrLine1();
		 String addrLine2=returnsForm.getAddrLine2();
		 String addrLine3=returnsForm.getAddrLine3();
		 String landMark=returnsForm.getLandMark();
		 String pincode=returnsForm.getPincode();
		 String phoneNumber=returnsForm.getPhoneNumber();
		 String city=returnsForm.getCity();
		 String state=returnsForm.getState();
		 String country=returnsForm.getCountry();
		 String isDefault=returnsForm.getIsDefault();
		 String scheduleReturnDate=returnsForm.getScheduleReturnDate();
		 String scheduleReturnTime=returnsForm.getScheduleReturnTime();
		 String orderCode=returnsForm.getOrderCode();
		 String transactionId=returnsForm.getTransactionId();
		 String ussid=returnsForm.getUssid();
		 String transactionType=returnsForm.getTransactionId();
		
		String returnStatement = null;
		boolean validState = false;
		final List<StateData> stateDataList = accountAddressFacade.getStates();
		
		for (final StateData stateData : stateDataList)
		{
			if (state != null && stateData.getName().equalsIgnoreCase(state))
			{
				validState = true;
				break;
			}
		}
		
		if (!validState)
		{
			returnStatement = "address.state.invalid";
		}
		
		else if (StringUtils.isEmpty(firstName))
		{
			returnStatement = "address.firstName.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(firstName)
				|| StringUtils.length(firstName) > MAX_FIELD_LENGTH_140)
		{
			returnStatement = "address.firstName.invalid.new";
		}
		else if (StringUtils.isEmpty(lastName))
		{
			returnStatement = "address.lastName.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(lastName)
				|| StringUtils.length(lastName) > MAX_FIELD_LENGTH_140)
		{
			returnStatement = "address.lastName.invalid.new";
		}

		else if (StringUtils.isEmpty(addrLine1))
		{
			returnStatement = "address.line1.invalid";
		}
		else if (StringUtils.length(addrLine1) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.line1.invalid.length";
		}
		else if (StringUtils.isEmpty(addrLine2))
		{
			returnStatement = "address.line2.invalid";
		}
		else if (StringUtils.length(addrLine2) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.line2.invalid.length";
		}
		else if (StringUtils.isEmpty(addrLine3))
		{
			returnStatement = "address.line3.invalid";
		}
		else if (StringUtils.length(addrLine3) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.line3.invalid.length";
		}
		else if (StringUtils.isEmpty(landMark))
		{
			returnStatement = "address.line3.invalid";
		}
		else if (StringUtils.length(landMark) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.line3.invalid.length";
		}

		else if (StringUtils.isEmpty(city) || StringUtils.length(city) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.townCity.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(city))
		{
			returnStatement = "address.townCity.invalid.alphaAndSpace";
		}
		else if (StringUtils.isEmpty(state))
		{
			returnStatement = "address.state.invalid";
		}

		else if (StringUtils.isEmpty(phoneNumber))
		{
			returnStatement = "address.mobileNumber.invalid";
		}
		else if (!CommonAsciiValidator.validateNumericWithoutSpace(phoneNumber) || StringUtils.length(phoneNumber) != MAX_FIELD_LENGTH_10)
		{
			returnStatement = "address.mobileNumber.invalid.numeric.length";
		}
		else if (StringUtils.isEmpty(pincode))
		{
			returnStatement = "address.postcode.invalid";
		}
		else if (!CommonAsciiValidator.validateNumericWithoutSpace(pincode) || StringUtils.length(pincode) != MAX_FIELD_LENGTH_6)
		{
			returnStatement = "address.postcode.invalid.numeric.length";
		}
		else if (StringUtils.isEmpty(addressType))
		{
			returnStatement = "address.addressType.select";
		}
		else if (StringUtils.isEmpty(country))
		{
			returnStatement = "address.country.invalid";
		}
		else if (StringUtils.isEmpty(returnReason))
		{
			returnStatement = "address.return.invalid";
		}
		else if (StringUtils.isEmpty(accountNumber))
		{
			returnStatement = "address.accountnumber.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(accountNumber)
				|| StringUtils.length(accountNumber) > MAX_FIELD_LENGTH_16)
		{
			returnStatement = "address.accountnumber.invalid.new";
		}
		else if (StringUtils.isEmpty(reEnterAccountNumber))
		{
			returnStatement = "address.reenteraccountnumber.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(reEnterAccountNumber)
				|| StringUtils.length(reEnterAccountNumber) > MAX_FIELD_LENGTH_16)
		{
			returnStatement = "address.reenteraccountnumber.invalid.new";
		}
		
		else if (StringUtils.isEmpty(iFSCCode))
		{
			returnStatement = "address.ifsccode.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(iFSCCode)
				|| StringUtils.length(iFSCCode) > MAX_FIELD_LENGTH_11)
		{
			returnStatement = "address.accountNumber.invalid.new";
		}
		else if (StringUtils.isEmpty(accountHolderName))
		{
			returnStatement = "address.accountholdername.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(accountHolderName)
				|| StringUtils.length(accountHolderName) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.accountholdername.invalid.new";
		}
		else if (StringUtils.isEmpty(bankName))
		{
			returnStatement = "address.bankname.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(bankName)
				|| StringUtils.length(bankName) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.bankname.invalid.new";
		}
		else if (StringUtils.isEmpty(title))
		{
			returnStatement = "address.title.invalid";
		}
		else if (StringUtils.isEmpty(ussid))
		{
			returnStatement = "address.ussid.invalid";
		}
		else if (StringUtils.isEmpty(orderCode))
		{
			returnStatement = "address.ordercode.invalid";
		}
		else if (StringUtils.isEmpty(scheduleReturnTime))
		{
			returnStatement = "address.schedulereturntime.invalid";
		}
		else if (StringUtils.isEmpty(scheduleReturnDate))
		{
			returnStatement = "address.schedulereturndate.invalid";
		}
		else if (StringUtils.isEmpty(transactionType))
		{
			returnStatement = "address.transactiontype.invalid";
		}
		else if (StringUtils.isEmpty(refundType))
		{
			returnStatement = "address.refundtype.invalid";
		}
		else if (StringUtils.isEmpty(refundMode))
		{
			returnStatement = "address.refundmode.invalid";
		}
		else if (StringUtils.isEmpty(transactionId))
		{
			returnStatement = "address.transactionid.invalid";
		}
	
		else
		{
			returnStatement = "success";
		}
		
		return returnStatement;
		
		
	}

}
