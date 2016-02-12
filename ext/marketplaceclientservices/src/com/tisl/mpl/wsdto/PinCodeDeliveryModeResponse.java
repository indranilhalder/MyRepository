package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Item", propOrder =
{ "USSID", "FulfilmentType", "TransportMode", "DeliveryMode" })
public class PinCodeDeliveryModeResponse
{
	@XmlElement(name = "USSID")
	private String USSID;
	@XmlElement(name = "FulfilmentType")
	private String FulfilmentType;

	@XmlElement(name = "TransportMode")
	private String TransportMode;

	@XmlElement(name = "DeliveryMode")
	private List<DeliveryModeResOMSWsDto> DeliveryMode;

	/**
	 * @return the uSSID
	 */
	public String getUSSID()
	{
		return USSID;
	}

	/**
	 * @param uSSID
	 *           the uSSID to set
	 */
	public void setUSSID(final String uSSID)
	{
		USSID = uSSID;
	}


	public List<DeliveryModeResOMSWsDto> getDeliveryMode()
	{
		return DeliveryMode;
	}

	/**
	 * @param deliveryMode
	 *           the deliveryMode to set
	 */
	public void setDeliveryMode(final List<DeliveryModeResOMSWsDto> deliveryMode)
	{
		DeliveryMode = deliveryMode;
	}

	/**
	 * @return the fulfilmentType
	 */
	public String getFulfilmentType()
	{
		return FulfilmentType;
	}

	/**
	 * @param fulfilmentType
	 *           the fulfilmentType to set
	 */
	public void setFulfilmentType(final String fulfilmentType)
	{
		FulfilmentType = fulfilmentType;
	}

	public String getTransportMode()
	{
		return TransportMode;
	}

	public void setTransportMode(final String transportMode)
	{
		TransportMode = transportMode;
	}



}
