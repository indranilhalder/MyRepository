/**
 *
 */
package com.tisl.mpl.xml.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "LPAWBUpdate")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
{ "orderId", "orderLine" })
public class LPAWBUpdate
{
	@XmlElement(name = "OrderId")
	private String orderId;

	@XmlElement(name = "OrderLine")
	private List<OrderLine> orderLine = new ArrayList();


	/**
	 * @return the orderId
	 */
	public String getOrderId()
	{
		return orderId;
	}

	/**
	 * @param orderId
	 *           the orderId to set
	 */
	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	/**
	 * @return the orderLine
	 */
	public List<OrderLine> getOrderLine()
	{
		return orderLine;
	}

	/**
	 * @param orderLine
	 *           the orderLine to set
	 */
	public void setOrderLine(final List<OrderLine> orderLine)
	{
		this.orderLine = orderLine;
	}
}
