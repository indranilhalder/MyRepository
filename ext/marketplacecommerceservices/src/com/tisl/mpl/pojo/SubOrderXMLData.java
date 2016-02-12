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
@XmlRootElement(name = "SubOrderInfo")
@XmlType(propOrder =
{ "subOrderId", "transactionInfoList" })
public class SubOrderXMLData
{
	private String subOrderId;
	private List<ChildOrderXMlData> transactionInfoList;

	/**
	 * @return the subOrderId
	 */
	@XmlElement(name = "SubOrderID")
	public String getSubOrderId()
	{
		return subOrderId;
	}

	/**
	 * @param subOrderId
	 *           the subOrderId to set
	 */
	public void setSubOrderId(final String subOrderId)
	{
		this.subOrderId = subOrderId;
	}

	/**
	 * @return the transactionInfoList
	 */
	@XmlElement(name = "TransactionInfo")
	public List<ChildOrderXMlData> getTransactionInfoList()
	{
		return transactionInfoList;
	}

	/**
	 * @param transactionInfoList
	 *           the transactionInfoList to set
	 */
	public void setTransactionInfoList(final List<ChildOrderXMlData> transactionInfoList)
	{
		this.transactionInfoList = transactionInfoList;
	}

}
