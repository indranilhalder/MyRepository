/**
 * 
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Tech
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder ={"StoreId" ,"Qty", "FulfillmentType" ,"ServiceableSlaves" })
public class CNCServiceableSlavesWsDTO
{
	@XmlElement(name = "StoreId")
	private String StoreId;
	
	@XmlElement(name = "Qty")
	private Integer Qty;
	
	@XmlElement(name = "FulfillmentType")
	private String FulfillmentType;
	
	@XmlElement(name = "ServiceableSlaves")
	private List<ServiceableSlavesDTO> ServiceableSlaves;

	/**
	 * @return the storeId
	 */
	public String getStoreId()
	{
		return StoreId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId)
	{
		StoreId = storeId;
	}

	/**
	 * @return the qty
	 */
	public Integer getQty()
	{
		return Qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(Integer qty)
	{
		Qty = qty;
	}

	/**
	 * @return the fulfillmentType
	 */
	public String getFulfillmentType()
	{
		return FulfillmentType;
	}

	/**
	 * @param fulfillmentType the fulfillmentType to set
	 */
	public void setFulfillmentType(String fulfillmentType)
	{
		FulfillmentType = fulfillmentType;
	}

	/**
	 * @return the serviceableSlaves
	 */
	public List<ServiceableSlavesDTO> getServiceableSlaves()
	{
		return ServiceableSlaves;
	}

	/**
	 * @param serviceableSlaves the serviceableSlaves to set
	 */
	public void setServiceableSlaves(List<ServiceableSlavesDTO> serviceableSlaves)
	{
		ServiceableSlaves = serviceableSlaves;
	}
}
