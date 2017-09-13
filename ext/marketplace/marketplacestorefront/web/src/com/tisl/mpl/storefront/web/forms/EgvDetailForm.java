/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import de.hybris.platform.validation.annotations.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Email;



/**
 * @author TUL
 *
 */
public class EgvDetailForm
{
	private double giftRange;

	@Min(value = 10, message = "{egv.card.minimum.amount.error.message}")
	@Max(value = 15000, message = "{egv.card.maximum.amount.error.message}")
	private double openTextAmount;

	@NotEmpty
	@Email
	private String toEmailAddress;

	@NotEmpty
	@Email
	private String fromEmailAddress;

	@NotEmpty
	private String messageBox;
	@NotEmpty
	private int totalEGV;

	/**
	 * @return the totalEGV
	 */
	public int getTotalEGV()
	{
		return totalEGV;
	}

	/**
	 * @param totalEGV
	 *           the totalEGV to set
	 */
	public void setTotalEGV(final int totalEGV)
	{
		this.totalEGV = totalEGV;
	}

	@NotEmpty
	private String productCode;


	/**
	 * @return the prductCode
	 */
	public String getProductCode()
	{
		return productCode;
	}

	/**
	 * @param prductCode
	 *           the prductCode to set
	 */
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	/**
	 * @return the giftRange
	 */
	public double getGiftRange()
	{
		return giftRange;
	}

	/**
	 * @param giftRange
	 *           the giftRange to set
	 */
	public void setGiftRange(final double giftRange)
	{
		this.giftRange = giftRange;
	}

	/**
	 * @return the openTextAmount
	 */
	public double getOpenTextAmount()
	{
		return openTextAmount;
	}

	/**
	 * @param openTextAmount
	 *           the openTextAmount to set
	 */
	public void setOpenTextAmount(final double openTextAmount)
	{
		this.openTextAmount = openTextAmount;
	}

	/**
	 * @return the toEmailAddress
	 */
	public String getToEmailAddress()
	{
		return toEmailAddress;
	}

	/**
	 * @param toEmailAddress
	 *           the toEmailAddress to set
	 */
	public void setToEmailAddress(final String toEmailAddress)
	{
		this.toEmailAddress = toEmailAddress;
	}

	/**
	 * @return the fromEmailAddress
	 */
	public String getFromEmailAddress()
	{
		return fromEmailAddress;
	}

	/**
	 * @param fromEmailAddress
	 *           the fromEmailAddress to set
	 */
	public void setFromEmailAddress(final String fromEmailAddress)
	{
		this.fromEmailAddress = fromEmailAddress;
	}

	/**
	 * @return the messageBox
	 */
	public String getMessageBox()
	{
		return messageBox;
	}

	/**
	 * @param messageBox
	 *           the messageBox to set
	 */
	public void setMessageBox(final String messageBox)
	{
		this.messageBox = messageBox;
	}




}
