/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import java.util.Arrays;


/**
 * @author Techouts
 *
 */
public class MplReturnsForm
{

	private String returnReason;
	private String refundType;
	private String accountNumber;
	private String reEnterAccountNumber;
	private String refundMode;
	private String accountHolderName;
	private String bankName;
	private String iFSCCode;
	private String title;
	private String returnMethod;
	private String firstName;
	private String[] storeIds;
	private String addressType;
	private String lastName;
	private String addrLine1;
	private String addrLine2;
	private String addrLine3;
	private String landMark;
	private String pincode;
	private String phoneNumber;
	private String city;
	private String state;
	private String country;
	private String isDefault;
	private String scheduleReturnDate;
	private String scheduleReturnTime;
	private String isCODorder;
	private String orderCode;
	private String transactionId;
	private String ussid;
	private String transactionType;
	private String reverseSeal;
	//TPR-5954
	private String comments;
	private String subReturnReason;
	private String imagePath;

	/**
	 * @return the comments
	 */
	public String getComments()
	{
		return comments;
	}

	/**
	 * @param comments
	 *           the comments to set
	 */
	public void setComments(final String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return the subReturnReason
	 */
	public String getSubReturnReason()
	{
		return subReturnReason;
	}

	/**
	 * @param subReturnReason
	 *           the subReturnReason to set
	 */
	public void setSubReturnReason(final String subReturnReason)
	{
		this.subReturnReason = subReturnReason;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath()
	{
		return imagePath;
	}

	/**
	 * @param imagePath
	 *           the imagePath to set
	 */
	public void setImagePath(final String imagePath)
	{
		this.imagePath = imagePath;
	}

	/**
	 * @return the transactionType
	 */
	public String getTransactionType()
	{
		return transactionType;
	}

	/**
	 * @param transactionType
	 *           the transactionType to set
	 */
	public void setTransactionType(final String transactionType)
	{
		this.transactionType = transactionType;
	}

	/**
	 * @return the returnReason
	 */
	public String getReturnReason()
	{
		return returnReason;
	}

	/**
	 * @param returnReason
	 *           the returnReason to set
	 */
	public void setReturnReason(final String returnReason)
	{
		this.returnReason = returnReason;
	}

	/**
	 * @return the refundType
	 */
	public String getRefundType()
	{
		return refundType;
	}

	/**
	 * @param refundType
	 *           the refundType to set
	 */
	public void setRefundType(final String refundType)
	{
		this.refundType = refundType;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber()
	{
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *           the accountNumber to set
	 */
	public void setAccountNumber(final String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the reEnterAccountNumber
	 */
	public String getReEnterAccountNumber()
	{
		return reEnterAccountNumber;
	}

	/**
	 * @param reEnterAccountNumber
	 *           the reEnterAccountNumber to set
	 */
	public void setReEnterAccountNumber(final String reEnterAccountNumber)
	{
		this.reEnterAccountNumber = reEnterAccountNumber;
	}

	/**
	 * @return the refundMode
	 */
	public String getRefundMode()
	{
		return refundMode;
	}

	/**
	 * @param refundMode
	 *           the refundMode to set
	 */
	public void setRefundMode(final String refundMode)
	{
		this.refundMode = refundMode;
	}

	/**
	 * @return the accountHolderName
	 */
	public String getAccountHolderName()
	{
		return accountHolderName;
	}

	/**
	 * @param accountHolderName
	 *           the accountHolderName to set
	 */
	public void setAccountHolderName(final String accountHolderName)
	{
		this.accountHolderName = accountHolderName;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName()
	{
		return bankName;
	}

	/**
	 * @param bankName
	 *           the bankName to set
	 */
	public void setBankName(final String bankName)
	{
		this.bankName = bankName;
	}

	/**
	 * @return the iFSCCode
	 */
	public String getiFSCCode()
	{
		return iFSCCode;
	}

	/**
	 * @param iFSCCode
	 *           the iFSCCode to set
	 */
	public void setiFSCCode(final String iFSCCode)
	{
		this.iFSCCode = iFSCCode;
	}

	/**
	 * @return the ttitle
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *           the title to set
	 */
	public void setTtitle(final String title)
	{
		this.title = title;
	}

	/**
	 * @return the returnMethod
	 */
	public String getReturnMethod()
	{
		return returnMethod;
	}

	/**
	 * @param returnMethod
	 *           the returnMethod to set
	 */
	public void setReturnMethod(final String returnMethod)
	{
		this.returnMethod = returnMethod;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the storeIds
	 */
	public String[] getStoreIds()
	{
		return storeIds;
	}

	/**
	 * @param storeIds
	 *           the storeIds to set
	 */
	public void setStoreIds(final String[] storeIds)
	{
		this.storeIds = storeIds;
	}

	/**
	 * @return the addressType
	 */
	public String getAddressType()
	{
		return addressType;
	}

	/**
	 * @param addressType
	 *           the addressType to set
	 */
	public void setAddressType(final String addressType)
	{
		this.addressType = addressType;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *           the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the addrLine1
	 */
	public String getAddrLine1()
	{
		return addrLine1;
	}

	/**
	 * @param addrLine1
	 *           the addrLine1 to set
	 */
	public void setAddrLine1(final String addrLine1)
	{
		this.addrLine1 = addrLine1;
	}

	/**
	 * @return the addrLine2
	 */
	public String getAddrLine2()
	{
		return addrLine2;
	}

	/**
	 * @param addrLine2
	 *           the addrLine2 to set
	 */
	public void setAddrLine2(final String addrLine2)
	{
		this.addrLine2 = addrLine2;
	}

	/**
	 * @return the addrLine3
	 */
	public String getAddrLine3()
	{
		return addrLine3;
	}

	/**
	 * @param addrLine3
	 *           the addrLine3 to set
	 */
	public void setAddrLine3(final String addrLine3)
	{
		this.addrLine3 = addrLine3;
	}

	/**
	 * @return the landMark
	 */
	public String getLandMark()
	{
		return landMark;
	}

	/**
	 * @param landMark
	 *           the landMark to set
	 */
	public void setLandMark(final String landMark)
	{
		this.landMark = landMark;
	}

	/**
	 * @return the pincode
	 */
	public String getPincode()
	{
		return pincode;
	}

	/**
	 * @param pincode
	 *           the pincode to set
	 */
	public void setPincode(final String pincode)
	{
		this.pincode = pincode;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *           the phoneNumber to set
	 */
	public void setPhoneNumber(final String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *           the city to set
	 */
	public void setCity(final String city)
	{
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}

	/**
	 * @return the isDefault
	 */
	public String getIsDefault()
	{
		return isDefault;
	}

	/**
	 * @param isDefault
	 *           the isDefault to set
	 */
	public void setIsDefault(final String isDefault)
	{
		this.isDefault = isDefault;
	}

	/**
	 * @return the scheduleReturnDate
	 */
	public String getScheduleReturnDate()
	{
		return scheduleReturnDate;
	}

	/**
	 * @param scheduleReturnDate
	 *           the scheduleReturnDate to set
	 */
	public void setScheduleReturnDate(final String scheduleReturnDate)
	{
		this.scheduleReturnDate = scheduleReturnDate;
	}

	/**
	 * @return the scheduleReturntime
	 */
	public String getScheduleReturnTime()
	{
		return scheduleReturnTime;
	}

	/**
	 * @param scheduleReturnTime
	 *           the scheduleReturnTime to set
	 */
	public void setScheduleReturnTime(final String scheduleReturnTime)
	{
		this.scheduleReturnTime = scheduleReturnTime;
	}

	/**
	 * @return the isCODorder
	 */
	public String getIsCODorder()
	{
		return isCODorder;
	}

	/**
	 * @param isCODorder
	 *           the isCODorder to set
	 */
	public void setIsCODorder(final String isCODorder)
	{
		this.isCODorder = isCODorder;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @param transactionId
	 *           the transactionId to set
	 */
	public void setTransactionId(final String transactionId)
	{
		this.transactionId = transactionId;
	}

	/**
	 * @return the ussid
	 */
	public String getUssid()
	{
		return ussid;
	}

	/**
	 * @param ussid
	 *           the ussid to set
	 */
	public void setUssid(final String ussid)
	{
		this.ussid = ussid;
	}

	/**
	 * @param title
	 *           the title to set
	 */
	public void setTitle(final String title)
	{
		this.title = title;
	}


	/**
	 * @return the reverseSeal
	 */
	public String getReverseSeal()
	{
		return reverseSeal;
	}

	/**
	 * @param reverseSeal
	 *           the reverseSeal to set
	 */
	public void setReverseSeal(final String reverseSeal)
	{
		this.reverseSeal = reverseSeal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MplReturnsForm [returnReason=" + returnReason + ", refundType=" + refundType + ", accountNumber=" + accountNumber
				+ ", reEnterAccountNumber=" + reEnterAccountNumber + ", refundMode=" + refundMode + ", accountHolderName="
				+ accountHolderName + ", bankName=" + bankName + ", iFSCCode=" + iFSCCode + ", title=" + title + ", returnMethod="
				+ returnMethod + ", firstName=" + firstName + ", storeIds=" + Arrays.toString(storeIds) + ", addressType="
				+ addressType + ", lastName=" + lastName + ", addrLine1=" + addrLine1 + ", addrLine2=" + addrLine2 + ", addrLine3="
				+ addrLine3 + ", landMark=" + landMark + ", pincode=" + pincode + ", phoneNumber=" + phoneNumber + ", city=" + city
				+ ", state=" + state + ", country=" + country + ", isDefault=" + isDefault + ", scheduleReturnDate="
				+ scheduleReturnDate + ", scheduleReturnTime=" + scheduleReturnTime + ", isCODorder=" + isCODorder + ", orderCode="
				+ orderCode + ", transactionId=" + transactionId + ", ussid=" + ussid + ", reverseSeal=" + reverseSeal + "]";
	}

}
