/**
 *
 */
package com.tisl.mpl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "SalesOrders")
@XmlType(propOrder =
{ "orderDataList" })
public class BulkSalesOrderXMLData
{
	private List<SalesOrderXMLData> orderDataList;

	/**
	 * @return the orderDataList
	 */
	@XmlElement(name = "SalesOrderPaymentInformationData")
	public List<SalesOrderXMLData> getOrderDataList()
	{
		return orderDataList;
	}

	/**
	 * @param orderDataList
	 *           the orderDataList to set
	 */
	public void setOrderDataList(final List<SalesOrderXMLData> orderDataList)
	{
		this.orderDataList = orderDataList;
	}
}
