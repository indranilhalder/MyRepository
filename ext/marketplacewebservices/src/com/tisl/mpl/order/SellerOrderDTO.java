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
@XmlRootElement(name = "sellerOrder")
public class SellerOrderDTO
{
	private String sellerOrderId;
	private String sellerOrderStatus;
	private OrderLineDTO orderLine;

	/**
	 * @return the sellerOrderId
	 */
	@XmlElement(name = "sellerOrderId")
	public String getSellerOrderId()
	{
		return sellerOrderId;
	}

	/**
	 * @param sellerOrderId
	 *           the sellerOrderId to set
	 */
	public void setSellerOrderId(final String sellerOrderId)
	{
		this.sellerOrderId = sellerOrderId;
	}

	/**
	 * @return the sellerOrderStatus
	 */
	@XmlElement(name = "sellerOrderStatus")
	public String getSellerOrderStatus()
	{
		return sellerOrderStatus;
	}

	/**
	 * @param sellerOrderStatus
	 *           the sellerOrderStatus to set
	 */
	public void setSellerOrderStatus(final String sellerOrderStatus)
	{
		this.sellerOrderStatus = sellerOrderStatus;
	}

	/**
	 * @return the orderLine
	 */
	@XmlElement(name = "orderLines")
	public OrderLineDTO getOrderLine()
	{
		return orderLine;
	}

	/**
	 * @param orderLine
	 *           the orderLine to set
	 */

	public void setOrderLine(final OrderLineDTO orderLine)
	{
		this.orderLine = orderLine;
	}


}
