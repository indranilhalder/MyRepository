/**
 * 
 */
package com.tisl.mpl.xml.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author TECHOUTS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( propOrder ={ "orderId","transactionId","returnType","rtsStore","logisticsID","lpNameOther","awbNum","shipmentCharge","shipmentProofURL"})
@XmlRootElement(name = "ReturnInfo")
public class RTSAndRSSReturnInfoRequest
{
	@XmlElement(name = "orderId")
	private String orderId;
	@XmlElement(name = "transactionId")
	private String transactionId;
	@XmlElement(name = "ReturnType")
	private String returnType;
	@XmlElement(name = "RTSStore")
	private List<String> rtsStore;
	@XmlElement(name = "LogisticsID")
	private String logisticsID;
	@XmlElement(name = "LPNameOther")
	private String lpNameOther;
	@XmlElement(name = "AWBNum")
	private String awbNum;
	@XmlElement(name = "shipmentCharge")
	private String shipmentCharge;
	@XmlElement(name = "shipmentProofURL")
	private String shipmentProofURL;
	/**
	 * @return the orderId
	 */
	public String getOrderId()
	{
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId()
	{
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId)
	{
		this.transactionId = transactionId;
	}
	/**
	 * @return the returnType
	 */
	public String getReturnType()
	{
		return returnType;
	}
	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(String returnType)
	{
		this.returnType = returnType;
	}
	/**
	 * @return the rtsStore
	 */
	public List<String> getRtsStore()
	{
		return rtsStore;
	}
	/**
	 * @param rtsStore the rtsStore to set
	 */
	public void setRtsStore(List<String> rtsStore)
	{
		this.rtsStore = rtsStore;
	}
	/**
	 * @return the logisticsID
	 */
	public String getLogisticsID()
	{
		return logisticsID;
	}
	/**
	 * @param logisticsID the logisticsID to set
	 */
	public void setLogisticsID(String logisticsID)
	{
		this.logisticsID = logisticsID;
	}
	/**
	 * @return the lpNameOther
	 */
	public String getLpNameOther()
	{
		return lpNameOther;
	}
	/**
	 * @param lpNameOther the lpNameOther to set
	 */
	public void setLpNameOther(String lpNameOther)
	{
		this.lpNameOther = lpNameOther;
	}
	/**
	 * @return the awbNum
	 */
	public String getAwbNum()
	{
		return awbNum;
	}
	/**
	 * @param awbNum the awbNum to set
	 */
	public void setAwbNum(String awbNum)
	{
		this.awbNum = awbNum;
	}
	/**
	 * @return the shipmentCharge
	 */
	public String getShipmentCharge()
	{
		return shipmentCharge;
	}
	/**
	 * @param shipmentCharge the shipmentCharge to set
	 */
	public void setShipmentCharge(String shipmentCharge)
	{
		this.shipmentCharge = shipmentCharge;
	}
	/**
	 * @return the shipmentProofURL
	 */
	public String getShipmentProofURL()
	{
		return shipmentProofURL;
	}
	/**
	 * @param shipmentProofURL the shipmentProofURL to set
	 */
	public void setShipmentProofURL(String shipmentProofURL)
	{
		this.shipmentProofURL = shipmentProofURL;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "RTSAndRSSReturnInfoRequest [orderId=" + orderId + ", transactionId=" + transactionId + ", returnType=" + returnType
				+ ", rtsStore=" + rtsStore + ", logisticsID=" + logisticsID + ", lpNameOther=" + lpNameOther + ", awbNum=" + awbNum
				+ ", shipmentCharge=" + shipmentCharge + ", shipmentProofURL=" + shipmentProofURL + "]";
	}
	



}
