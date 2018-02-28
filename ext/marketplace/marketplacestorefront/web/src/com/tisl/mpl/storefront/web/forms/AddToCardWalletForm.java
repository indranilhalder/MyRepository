/**
 * 
 */
package com.tisl.mpl.storefront.web.forms;

import java.util.List;

/**
 * @author Tech
 *
 */
public class AddToCardWalletForm
{

	private String cardNumber;
	private String cardPin;
	private String submit;
	
	private String page;
	private String reqType;
	/**
	 * @return the cardNumber
	 */
	public String getCardNumber()
	{
		return cardNumber;
	}
	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber)
	{
		this.cardNumber = cardNumber;
	}
	/**
	 * @return the cardPin
	 */
	public String getCardPin()
	{
		return cardPin;
	}
	/**
	 * @param cardPin the cardPin to set
	 */
	public void setCardPin(String cardPin)
	{
		this.cardPin = cardPin;
	}
	/**
	 * @return the submit
	 */
	public String getSubmit()
	{
		return submit;
	}
	/**
	 * @param submit the submit to set
	 */
	public void setSubmit(String submit)
	{
		this.submit = submit;
	}
	/**
	 * @return the page
	 */
	public String getPage()
	{
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(String page)
	{
		this.page = page;
	}
	/**
	 * @return the reqType
	 */
	public String getReqType()
	{
		return reqType;
	}
	/**
	 * @param reqType the reqType to set
	 */
	public void setReqType(String reqType)
	{
		this.reqType = reqType;
	}
}
