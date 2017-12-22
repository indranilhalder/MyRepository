/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "DuplicateTicketCheck")
@XmlType(propOrder =
{ "customerID", "hsoOrderId", "subOrderId", "l0CatCode", "l1CatCode", "l2CatCode", "l3CatCode", "transactionId" })
public class DuplicateTicketMasterXMLData
{
	private String customerID;
	private String hsoOrderId;
	private String subOrderId;
	private String transactionId;
	private String L0CatCode;
	private String L1CatCode;
	private String L2CatCode;
	private String L3CatCode;

	/**
	 * @return the l0CatCode
	 */
	@XmlElement(name = "L0CatCode")
	public String getL0CatCode()
	{
		return L0CatCode;
	}

	/**
	 * @param l0CatCode
	 *           the l0CatCode to set
	 */
	public void setL0CatCode(final String l0CatCode)
	{
		L0CatCode = l0CatCode;
	}

	/**
	 * @return the l1CatCode
	 */
	@XmlElement(name = "L1CatCode")
	public String getL1CatCode()
	{
		return L1CatCode;
	}

	/**
	 * @param l1CatCode
	 *           the l1CatCode to set
	 */
	public void setL1CatCode(final String l1CatCode)
	{
		L1CatCode = l1CatCode;
	}

	/**
	 * @return the l2CatCode
	 */
	@XmlElement(name = "L2CatCode")
	public String getL2CatCode()
	{
		return L2CatCode;
	}

	/**
	 * @param l2CatCode
	 *           the l2CatCode to set
	 */
	public void setL2CatCode(final String l2CatCode)
	{
		L2CatCode = l2CatCode;
	}

	/**
	 * @return the l3CatCode
	 */
	@XmlElement(name = "L3CatCode")
	public String getL3CatCode()
	{
		return L3CatCode;
	}

	/**
	 * @param l3CatCode
	 *           the l3CatCode to set
	 */
	public void setL3CatCode(final String l3CatCode)
	{
		L3CatCode = l3CatCode;
	}




	/**
	 * @return the customerID
	 */
	@XmlElement(name = "CustomerId")
	public String getCustomerID()
	{
		return customerID;
	}

	/**
	 * @param customerID
	 *           the customerID to set
	 */
	public void setCustomerID(final String customerID)
	{
		this.customerID = customerID;
	}

	/**
	 * @return the subOrderId
	 */
	@XmlElement(name = "SubOrderId")
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
	 * @return the transactionId
	 */
	@XmlElement(name = "TransactionID")
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @param transactionId
	 *           the transactionId to set
	 */
	public void setTransactionId(final String transactionId)
	{
		this.transactionId = transactionId;
	}

	/**
	 * @return the hSOOrderID
	 */
	@XmlElement(name = "HSOOrderID")
	public String getHsoOrderId()
	{
		return hsoOrderId;
	}

	/**
	 * @param hsoOrderId
	 *           the hsoOrderId to set
	 */
	public void setHsoOrderId(final String hsoOrderId)
	{
		this.hsoOrderId = hsoOrderId;
	}






}