/**
 *
 */
package com.tisl.mpl.order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "StatusUpdate")
public class OrderStatusUpdateDTO implements java.io.Serializable
{

	private String parentOrderId;
	private String orderUpdateTime;
	private SellerOrderDTO sellerOrder;

	/**
	 * @return the parentOrderId
	 */
	@XmlElement(name = "ParentOrderId")
	public String getParentOrderId()
	{
		return parentOrderId;
	}

	/**
	 * @param parentOrderId
	 *           the parentOrderId to set
	 */
	public void setParentOrderId(final String parentOrderId)
	{
		this.parentOrderId = parentOrderId;
	}

	/**
	 * @return the sellerOrder
	 */
	@XmlElement(name = "sellerOrder")
	public SellerOrderDTO getSellerOrder()
	{
		return sellerOrder;
	}

	/**
	 * @param sellerOrder
	 *           the sellerOrder to set
	 */
	public void setSellerOrder(final SellerOrderDTO sellerOrder)
	{
		this.sellerOrder = sellerOrder;
	}

	/**
	 * @return the orderUpdateTime
	 */
	@XmlElement(name = "orderUpdateTime")
	public String getOrderUpdateTime()
	{
		return orderUpdateTime;
	}

	/**
	 * @param orderUpdateTime
	 *           the orderUpdateTime to set
	 */
	public void setOrderUpdateTime(final String orderUpdateTime)
	{
		this.orderUpdateTime = orderUpdateTime;
	}




}
