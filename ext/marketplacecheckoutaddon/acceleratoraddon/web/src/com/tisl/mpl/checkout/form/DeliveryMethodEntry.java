/**
 *
 */
package com.tisl.mpl.checkout.form;

/**
 * @author TCS
 *
 */
public class DeliveryMethodEntry
{
	private String entryNumber;
	private String sellerArticleSKU;
	private String deliveryCode;

	/**
	 * @return the entryNumber
	 */
	public String getEntryNumber()
	{
		return entryNumber;
	}

	/**
	 * @param entryNumber
	 *           the entryNumber to set
	 */
	public void setEntryNumber(final String entryNumber)
	{
		this.entryNumber = entryNumber;
	}

	/**
	 * @return the deliveryCode
	 */
	public String getDeliveryCode()
	{
		return deliveryCode;
	}

	/**
	 * @param deliveryCode
	 *           the deliveryCode to set
	 */
	public void setDeliveryCode(final String deliveryCode)
	{
		this.deliveryCode = deliveryCode;
	}

	/**
	 * @return the sellerArticleSKU
	 */
	public String getSellerArticleSKU()
	{
		return sellerArticleSKU;
	}

	/**
	 * @param sellerArticleSKU
	 *           the sellerArticleSKU to set
	 */
	public void setSellerArticleSKU(final String sellerArticleSKU)
	{
		this.sellerArticleSKU = sellerArticleSKU;
	}

}
