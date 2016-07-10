/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.ArrayList;


/**
 * @author TCS
 *
 */
public class CartDetailsWsDTO implements java.io.Serializable
{
	private String pincode;
	private ArrayList<ProductsWsDTO> proddet;
	private ArrayList<FreeItemWsDTO> freeItem;
	private ArrayList<ProtectionPlanWsDTO> protectionPlan;
	private ArrayList<AddListWsDTO> addlist;
	private double subtotal;
	private double deliveryTotal;
	private double discountTotal;
	private double totalPrice;
	private ArrayList<YouMayLikeWsDTO> youmayLike;

	//	public CartDetailsWsDTO()
	//	{
	//
	//	}

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
	 * @return the proddet
	 */
	public ArrayList<ProductsWsDTO> getProddet()
	{
		return proddet;
	}

	/**
	 * @param proddet
	 *           the proddet to set
	 */
	public void setProddet(final ArrayList<ProductsWsDTO> proddet)
	{
		this.proddet = proddet;
	}

	/**
	 * @return the freeItem
	 */
	public ArrayList<FreeItemWsDTO> getFreeItem()
	{
		return freeItem;
	}

	/**
	 * @param freeItem
	 *           the freeItem to set
	 */
	public void setFreeItem(final ArrayList<FreeItemWsDTO> freeItem)
	{
		this.freeItem = freeItem;
	}

	/**
	 * @return the protectionPlan
	 */
	public ArrayList<ProtectionPlanWsDTO> getProtectionPlan()
	{
		return protectionPlan;
	}

	/**
	 * @param protectionPlan
	 *           the protectionPlan to set
	 */
	public void setProtectionPlan(final ArrayList<ProtectionPlanWsDTO> protectionPlan)
	{
		this.protectionPlan = protectionPlan;
	}

	/**
	 * @return the addlist
	 */
	public ArrayList<AddListWsDTO> getAddlist()
	{
		return addlist;
	}

	/**
	 * @param addlist
	 *           the addlist to set
	 */
	public void setAddlist(final ArrayList<AddListWsDTO> addlist)
	{
		this.addlist = addlist;
	}

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

	/**
	 * @return the youmayLike
	 */
	public ArrayList<YouMayLikeWsDTO> getYoumayLike()
	{
		return youmayLike;
	}

	/**
	 * @param youmayLike
	 *           the youmayLike to set
	 */
	public void setYoumayLike(final ArrayList<YouMayLikeWsDTO> youmayLike)
	{
		this.youmayLike = youmayLike;
	}


}
