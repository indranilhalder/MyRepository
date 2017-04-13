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
@XmlType(propOrder ={"ussId" ,"EDD", "nextEDD" ,"isScheduled", "CODEligible"  })
public class EDDInfoWsDTO
{
	@XmlElement(name = "ussId")
	private String ussId;
	
	@XmlElement(name = "EDD")
	private String EDD;
	
	@XmlElement(name = "nextEDD")
	private String nextEDD;
	
	@XmlElement(name = "isScheduled")
	private String isScheduled;
	
	@XmlElement(name = "CODEligible")
	private String CODEligible;

	

	/**
	 * @return the eDD
	 */
	public String getEDD()
	{
		return EDD;
	}

	/**
	 * @param eDD the eDD to set
	 */
	public void setEDD(String eDD)
	{
		EDD = eDD;
	}

	/**
	 * @return the nextEDD
	 */
	public String getNextEDD()
	{
		return nextEDD;
	}

	/**
	 * @param nextEDD the nextEDD to set
	 */
	public void setNextEDD(String nextEDD)
	{
		this.nextEDD = nextEDD;
	}

	/**
	 * @return the isScheduled
	 */
	public String getIsScheduled()
	{
		return isScheduled;
	}

	/**
	 * @param isScheduled the isScheduled to set
	 */
	public void setIsScheduled(String isScheduled)
	{
		this.isScheduled = isScheduled;
	}


	/**
	 * @return the ussId
	 */
	public String getUssId()
	{
		return ussId;
	}

	/**
	 * @param ussId the ussId to set
	 */
	public void setUssId(String ussId)
	{
		this.ussId = ussId;
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
}
