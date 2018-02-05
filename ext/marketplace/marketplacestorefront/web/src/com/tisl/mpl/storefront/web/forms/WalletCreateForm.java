/**
 * 
 */
package com.tisl.mpl.storefront.web.forms;

/**
 * @author Pankaj
 *
 */
public class WalletCreateForm
{
	private boolean firstNameFlag;
	private boolean lastNameFlag;
	private boolean mobileNoFlag;
	private String qcVerifyFirstName;
	private String qcVerifyLastName;
	private String qcVerifyMobileNo;
	private String otpNumber;
	
	/**
	 * @return the qcVerifyFirstName
	 */
	public String getQcVerifyFirstName()
	{
		return qcVerifyFirstName;
	}
	/**
	 * @param qcVerifyFirstName the qcVerifyFirstName to set
	 */
	public void setQcVerifyFirstName(String qcVerifyFirstName)
	{
		this.qcVerifyFirstName = qcVerifyFirstName;
	}
	/**
	 * @return the qcVerifyLastName
	 */
	public String getQcVerifyLastName()
	{
		return qcVerifyLastName;
	}
	/**
	 * @param qcVerifyLastName the qcVerifyLastName to set
	 */
	public void setQcVerifyLastName(String qcVerifyLastName)
	{
		this.qcVerifyLastName = qcVerifyLastName;
	}
	/**
	 * @return the qcVerifyMobileNo
	 */
	public String getQcVerifyMobileNo()
	{
		return qcVerifyMobileNo;
	}
	/**
	 * @param qcVerifyMobileNo the qcVerifyMobileNo to set
	 */
	public void setQcVerifyMobileNo(String qcVerifyMobileNo)
	{
		this.qcVerifyMobileNo = qcVerifyMobileNo;
	}
	/**
	 * @return the otpNumber
	 */
	public String getOtpNumber()
	{
		return otpNumber;
	}
	/**
	 * @param otpNumber the otpNumber to set
	 */
	public void setOtpNumber(String otpNumber)
	{
		this.otpNumber = otpNumber;
	}
	/**
	 * @return the firstNameFlag
	 */
	public boolean isFirstNameFlag()
	{
		return firstNameFlag;
	}
	/**
	 * @param firstNameFlag the firstNameFlag to set
	 */
	public void setFirstNameFlag(boolean firstNameFlag)
	{
		this.firstNameFlag = firstNameFlag;
	}
	/**
	 * @return the lastNameFlag
	 */
	public boolean isLastNameFlag()
	{
		return lastNameFlag;
	}
	/**
	 * @param lastNameFlag the lastNameFlag to set
	 */
	public void setLastNameFlag(boolean lastNameFlag)
	{
		this.lastNameFlag = lastNameFlag;
	}
	/**
	 * @return the mobileNoFlag
	 */
	public boolean isMobileNoFlag()
	{
		return mobileNoFlag;
	}
	/**
	 * @param mobileNoFlag the mobileNoFlag to set
	 */
	public void setMobileNoFlag(boolean mobileNoFlag)
	{
		this.mobileNoFlag = mobileNoFlag;
	}
}
