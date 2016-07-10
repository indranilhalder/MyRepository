/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.SopPaymentDetailsForm;


/**
 * @author TCS
 *
 */
public class PaymentForm extends SopPaymentDetailsForm
{
	private String EMIBankCode;
	private String NBBankCode;
	private String paymentMode;
	private String termRate;
	private String selectedTerm;
	private String juspayOrderId;


	/**
	 * @return the eMIBankCode
	 */
	public String getEMIBankCode()
	{
		return EMIBankCode;
	}

	/**
	 * @param eMIBankCode
	 *           the eMIBankCode to set
	 */
	public void setEMIBankCode(final String eMIBankCode)
	{
		EMIBankCode = eMIBankCode;
	}

	/**
	 * @return the nBBankCode
	 */
	public String getNBBankCode()
	{
		return NBBankCode;
	}

	/**
	 * @param nBBankCode
	 *           the nBBankCode to set
	 */
	public void setNBBankCode(final String nBBankCode)
	{
		NBBankCode = nBBankCode;
	}

	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode()
	{
		return paymentMode;
	}

	/**
	 * @param paymentMode
	 *           the paymentMode to set
	 */
	public void setPaymentMode(final String paymentMode)
	{
		this.paymentMode = paymentMode;
	}

	/**
	 * @return the termRate
	 */
	public String getTermRate()
	{
		return termRate;
	}

	/**
	 * @param termRate
	 *           the termRate to set
	 */
	public void setTermRate(final String termRate)
	{
		this.termRate = termRate;
	}

	/**
	 * @return the selectedTerm
	 */
	public String getSelectedTerm()
	{
		return selectedTerm;
	}

	/**
	 * @param selectedTerm
	 *           the selectedTerm to set
	 */
	public void setSelectedTerm(final String selectedTerm)
	{
		this.selectedTerm = selectedTerm;
	}

	/**
	 * @return the juspayOrderId
	 */
	public String getJuspayOrderId()
	{
		return juspayOrderId;
	}

	/**
	 * @param juspayOrderId
	 *           the juspayOrderId to set
	 */
	public void setJuspayOrderId(final String juspayOrderId)
	{
		this.juspayOrderId = juspayOrderId;
	}




}
