/**
 * 
 */
package com.tisl.mpl.storefront.web.forms;



/**
 * @author 682160
 * 
 */
public class SendSmsOtpForm
{
	private String mobilenumber;
	private String OTPNumber;

	/**
	 * @return the mobilenumber
	 */

	/**
	 * @return the oTPNumber
	 */
	public String getOTPNumber()
	{
		return OTPNumber;
	}

	/**
	 * @param oTPNumber
	 *           the oTPNumber to set
	 */
	public void setOTPNumber(final String oTPNumber)
	{
		OTPNumber = oTPNumber;
	}

	/**
	 * @NotNull(message = "{forgottenPwd.email.invalid}")
	 * 
	 * @Size(min = 1, max = 255, message = "{forgottenPwd.email.invalid}")
	 * 
	 * @Email(message = "{forgottenPwd.email.invalid}") //
	 */
	public String getMobilenumber()
	{
		return mobilenumber;
	}

	/**
	 * @param mobilenumber
	 *           the mobilenumber to set
	 */
	public void setMobilenumber(final String mobilenumber)
	{
		this.mobilenumber = mobilenumber;
	}

}
