/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.EqualAttributes;


/**
 * @author 682160
 *
 */
@EqualAttributes(message = "{validation.checkPwd.equals}", value =
{ "pwd", "checkPwd" })
public class MplUpdatePwdForm
{
	private String pwd;
	private String checkPwd;
	private String token;
	private String customerUniqueID;

	/**
	 * @return the pwd
	 */
	public String getPwd()
	{
		return pwd;
	}

	/**
	 * @param pwd
	 *           the pwd to set
	 */
	public void setPwd(final String pwd)
	{
		this.pwd = pwd;
	}

	/**
	 * @return the checkPwd
	 */
	public String getCheckPwd()
	{
		return checkPwd;
	}

	/**
	 * @param checkPwd
	 *           the checkPwd to set
	 */
	public void setCheckPwd(final String checkPwd)
	{
		this.checkPwd = checkPwd;
	}

	/**
	 * @return the token
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @param token
	 *           the token to set
	 */
	public void setToken(final String token)
	{
		this.token = token;
	}

	/**
	 * @return the customerUniqueID
	 */
	public String getCustomerUniqueID()
	{
		return customerUniqueID;
	}

	/**
	 * @param customerUniqueID
	 *           the customerUniqueID to set
	 */
	public void setCustomerUniqueID(final String customerUniqueID)
	{
		this.customerUniqueID = customerUniqueID;
	}

}
