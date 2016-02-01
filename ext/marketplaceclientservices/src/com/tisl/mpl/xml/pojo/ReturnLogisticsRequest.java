/**
 *
 */
package com.tisl.mpl.xml.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "ReturnLogisticsRequest")
@XmlType(propOrder =
{ "orderlines" })
public class ReturnLogisticsRequest
{
	private List<OrderLineData> orderlines;

	/**
	 * @return the orderlines
	 */
	@XmlElement(name = "OrderLine")
	public List<OrderLineData> getOrderlines()
	{
		return orderlines;
	}

	/**
	 * @param orderlines
	 *           the orderlines to set
	 */
	public void setOrderlines(final List<OrderLineData> orderlines)
	{
		this.orderlines = orderlines;
	}



}
