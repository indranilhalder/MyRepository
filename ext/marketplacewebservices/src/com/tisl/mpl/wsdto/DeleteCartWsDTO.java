/**
 *
 */
package com.tisl.mpl.wsdto;

/**
 * @author TCS
 *
 */
public class DeleteCartWsDTO implements java.io.Serializable
{
	private double subtotal;
	private double deliveryTotal;
	private double discountTotal;
	private double totalPrice;

	//	public DeleteCartWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the subtotal
	 */
	public double getSubtotal()
	{
		return subtotal;
	}

	/**
	 * @param subtotal
	 *           the subtotal to set
	 */
	public void setSubtotal(final double subtotal)
	{
		this.subtotal = subtotal;
	}

	/**
	 * @return the deliveryTotal
	 */
	public double getDeliveryTotal()
	{
		return deliveryTotal;
	}

	/**
	 * @param deliveryTotal
	 *           the deliveryTotal to set
	 */
	public void setDeliveryTotal(final double deliveryTotal)
	{
		this.deliveryTotal = deliveryTotal;
	}

	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal()
	{
		return discountTotal;
	}

	/**
	 * @param discountTotal
	 *           the discountTotal to set
	 */
	public void setDiscountTotal(final double discountTotal)
	{
		this.discountTotal = discountTotal;
	}

	/**
	 * @return the totalPrice
	 */
	public double getTotalPrice()
	{
		return totalPrice;
	}

	/**
	 * @param totalPrice
	 *           the totalPrice to set
	 */
	public void setTotalPrice(final double totalPrice)
	{
		this.totalPrice = totalPrice;
	}

}
