/**
 *
 */
package com.tisl.mpl.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "LPAWBUpdate")
public class PanCardResDTO
{
	private String orderId;
	private final List<OrderLineResData> orderLine = new ArrayList();

	/**
	 * @return the orderId
	 */
	@XmlElement(name = "OrderId")
	public String getOrderId()
	{
		return orderId;
	}

	/**
	 * @return the orderLine
	 */
	@XmlElement(name = "OrderLine")
	public List<OrderLineResData> getOrderLine()
	{
		return orderLine;
	}

	/**
	 * @param orderId
	 *           the orderId to set
	 */
	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}


}
