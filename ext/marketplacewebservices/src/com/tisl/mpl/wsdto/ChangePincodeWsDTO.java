/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.ArrayList;


/**
 * @author TCS
 *
 */
public class ChangePincodeWsDTO implements java.io.Serializable
{
	private ArrayList<ProdDataWsDTO> productdata;
	private double subtotalPrice;
	private double deliveryTotal;
	private double discountTotal;
	private double totalPrice;

	//	public ChangePincodeWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the productdata
	 */
	public ArrayList<ProdDataWsDTO> getProductdata()
	{
		return productdata;
	}

	/**
	 * @param productdata
	 *           the productdata to set
	 */
	public void setProductdata(final ArrayList<ProdDataWsDTO> productdata)
	{
		this.productdata = productdata;
	}

	/**
	 * @return the subtotalPrice
	 */
	public double getSubtotalPrice()
	{
		return subtotalPrice;
	}

	/**
	 * @param subtotalPrice
	 *           the subtotalPrice to set
	 */
	public void setSubtotalPrice(final double subtotalPrice)
	{
		this.subtotalPrice = subtotalPrice;
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
