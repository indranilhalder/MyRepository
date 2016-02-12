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
@XmlRootElement(name = "ReturnLogisticsResponse")
@XmlType(propOrder =
{ "orderlines" })
public class ReturnLogisticsResponse
{
	private List<OrderLineDataResponse> orderlines;

	/**
	 * @return the orderlines
	 */
	@XmlElement(name = "OrderLine")
	public List<OrderLineDataResponse> getOrderlines()
	{
		return orderlines;
	}

	/**
	 * @param orderlines
	 *           the orderlines to set
	 */
	public void setOrderlines(final List<OrderLineDataResponse> orderlines)
	{
		this.orderlines = orderlines;
	}

}
