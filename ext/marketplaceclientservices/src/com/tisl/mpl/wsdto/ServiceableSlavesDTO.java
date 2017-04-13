/**
 * 
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Tech
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceableSlaves", propOrder =
{ "SlaveId", "LogisticsID", "CODEligible", "Priority", "TransactionType" })
public class ServiceableSlavesDTO
{

	@XmlElement(name = "SlaveId")
	private String SlaveId;
	
	@XmlElement(name = "LogisticsID")
	private String LogisticsID;
	
	@XmlElement(name = "CODEligible")
	private String CODEligible;
	
	@XmlElement(name = "Priority")
	private String Priority;
	
	@XmlElement(name = "TransactionType")
	private String TransactionType;

	
	
	/**
	 * @return the slaveId
	 */
	public String getSlaveId()
	{
		return SlaveId;
	}

	/**
	 * @param slaveId the slaveId to set
	 */
	public void setSlaveId(String slaveId)
	{
		SlaveId = slaveId;
	}

	/**
	 * @return the logisticsID
	 */
	public String getLogisticsID()
	{
		return LogisticsID;
	}

	/**
	 * @param logisticsID the logisticsID to set
	 */
	public void setLogisticsID(String logisticsID)
	{
		LogisticsID = logisticsID;
	}

	/**
	 * @return the cODEligible
	 */
	public String getCODEligible()
	{
		return CODEligible;
	}

	/**
	 * @param cODEligible the cODEligible to set
	 */
	public void setCODEligible(String cODEligible)
	{
		CODEligible = cODEligible;
	}

	/**
	 * @return the priority
	 */
	public String getPriority()
	{
		return Priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority)
	{
		Priority = priority;
	}

	/**
	 * @return the transactionType
	 */
	public String getTransactionType()
	{
		return TransactionType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType)
	{
		TransactionType = transactionType;
	}


}
