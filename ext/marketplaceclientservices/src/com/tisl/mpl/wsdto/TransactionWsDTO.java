/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author 884206
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(/* name = "transactionelement", */propOrder =
{ "transactionId", "sellerId", "listingId", "ussid", "price", "apportionedPrice", "apportionedCodPrice", "promotionCode",
		"isaGift", "deliveryMode", "shippingCharge", "slaveId", "isCod", "isReturnable", "productSize", "transportMode",
		"fulfilmentType", "cad", "pd" })
public class TransactionWsDTO
{
	@XmlElement(name = "TransactionId")
	private String transactionId;
	@XmlElement(name = "SellerID")
	private String sellerId;
	@XmlElement(name = "ListingID")
	private String listingId;
	@XmlElement(name = "USSID")
	private String ussid;
	@XmlElement(name = "Price")
	private double price;
	@XmlElement(name = "ApportionedPrice")
	private double apportionedPrice;
	@XmlElement(name = "ApportionedCODPrice")
	private double apportionedCodPrice;
	@XmlElement(name = "PromotionCode")
	private String promotionCode;
	@XmlElement(name = "IsaGift")
	private boolean isaGift;
	@XmlElement(name = "DeliveryMode")
	private String deliveryMode;
	@XmlElement(name = "ShippingCharge")
	private double shippingCharge;
	@XmlElement(name = "slaveId")
	private String slaveId;
	@XmlElement(name = "isCOD")
	//private boolean isCod;
	private boolean Cod;
	@XmlElement(name = "isReturnable")
	//private boolean isReturnable;
	private boolean Returnable;
	@XmlElement(name = "productSize")
	private String productSize;
	@XmlElement(name = "transportMode")
	private String transportMode;
	@XmlElement(name = "fulfillmentType")
	private String fulfilmentType;

	@XmlElement(name = "CustomerAddress")
	private CustomerAddressWsDTO cad;
	@XmlElement(name = "ProductDetails")
	private ProductDetailsWsDTO pd;

	//	public TransactionWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the transactionId
	 */
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
	 * @return the sellerId
	 */
	public String getSellerId()
	{
		return sellerId;
	}

	/**
	 * @param sellerId
	 *           the sellerId to set
	 */
	public void setSellerId(final String sellerId)
	{
		this.sellerId = sellerId;
	}

	/**
	 * @return the listingId
	 */
	public String getListingId()
	{
		return listingId;
	}

	/**
	 * @param listingId
	 *           the listingId to set
	 */
	public void setListingId(final String listingId)
	{
		this.listingId = listingId;
	}

	/**
	 * @return the ussid
	 */
	public String getUssid()
	{
		return ussid;
	}

	/**
	 * @param ussid
	 *           the ussid to set
	 */
	public void setUssid(final String ussid)
	{
		this.ussid = ussid;
	}

	/**
	 * @return the price
	 */
	public double getPrice()
	{
		return price;
	}

	/**
	 * @param price
	 *           the price to set
	 */
	public void setPrice(final double price)
	{
		this.price = price;
	}

	/**
	 * @return the apportionedPrice
	 */
	public double getApportionedPrice()
	{
		return apportionedPrice;
	}

	/**
	 * @param apportionedPrice
	 *           the apportionedPrice to set
	 */
	public void setApportionedPrice(final double apportionedPrice)
	{
		this.apportionedPrice = apportionedPrice;
	}

	/**
	 * @return the apportionedCodPrice
	 */
	public double getApportionedCodPrice()
	{
		return apportionedCodPrice;
	}

	/**
	 * @param apportionedCodPrice
	 *           the apportionedCodPrice to set
	 */
	public void setApportionedCodPrice(final double apportionedCodPrice)
	{
		this.apportionedCodPrice = apportionedCodPrice;
	}

	/**
	 * @return the promotionCode
	 */
	public String getPromotionCode()
	{
		return promotionCode;
	}

	/**
	 * @param promotionCode
	 *           the promotionCode to set
	 */
	public void setPromotionCode(final String promotionCode)
	{
		this.promotionCode = promotionCode;
	}

	/**
	 * @return the isaGift
	 */
	public boolean isIsaGift()
	{
		return isaGift;
	}

	/**
	 * @param isaGift
	 *           the isaGift to set
	 */
	public void setIsaGift(final boolean isaGift)
	{
		this.isaGift = isaGift;
	}

	/**
	 * @return the deliveryMode
	 */
	public String getDeliveryMode()
	{
		return deliveryMode;
	}

	/**
	 * @param deliveryMode
	 *           the deliveryMode to set
	 */
	public void setDeliveryMode(final String deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	/**
	 * @return the shippingCharge
	 */
	public double getShippingCharge()
	{
		return shippingCharge;
	}

	/**
	 * @param shippingCharge
	 *           the shippingCharge to set
	 */
	public void setShippingCharge(final double shippingCharge)
	{
		this.shippingCharge = shippingCharge;
	}

	/**
	 * @return the slaveId
	 */
	public String getSlaveId()
	{
		return slaveId;
	}

	/**
	 * @param slaveId
	 *           the slaveId to set
	 */
	public void setSlaveId(final String slaveId)
	{
		this.slaveId = slaveId;
	}

	/**
	 * @return the isCod
	 */
	/*
	 * public boolean isCod() { return isCod; }
	 *//**
	 * @param isCod
	 *           the isCod to set
	 */
	/*
	 * public void setCod(final boolean isCod) { this.isCod = isCod; }
	 *//**
	 * @return the isReturnable
	 */
	/*
	 * public boolean isReturnable() { return isReturnable; }
	 *//**
	 * @param isReturnable
	 *           the isReturnable to set
	 */
	/*
	 * public void setReturnable(final boolean isReturnable) { this.isReturnable = isReturnable; }
	 */

	/**
	 * @return the productSize
	 */
	public String getProductSize()
	{
		return productSize;
	}

	/**
	 * @return the cod
	 */
	public boolean isCod()
	{
		return Cod;
	}

	/**
	 * @param cod
	 *           the cod to set
	 */
	public void setCod(final boolean cod)
	{
		Cod = cod;
	}

	/**
	 * @return the returnable
	 */
	public boolean isReturnable()
	{
		return Returnable;
	}

	/**
	 * @param returnable
	 *           the returnable to set
	 */
	public void setReturnable(final boolean returnable)
	{
		Returnable = returnable;
	}

	/**
	 * @param productSize
	 *           the productSize to set
	 */
	public void setProductSize(final String productSize)
	{
		this.productSize = productSize;
	}

	/**
	 * @return the transportMode
	 */
	public String getTransportMode()
	{
		return transportMode;
	}

	/**
	 * @param transportMode
	 *           the transportMode to set
	 */
	public void setTransportMode(final String transportMode)
	{
		this.transportMode = transportMode;
	}

	/**
	 * @return the fulfilmentType
	 */
	public String getFulfilmentType()
	{
		return fulfilmentType;
	}

	/**
	 * @param fulfilmentType
	 *           the fulfilmentType to set
	 */
	public void setFulfilmentType(final String fulfilmentType)
	{
		this.fulfilmentType = fulfilmentType;
	}

	/**
	 * @return the ca
	 */
	public CustomerAddressWsDTO getCad()
	{
		return cad;
	}

	/**
	 * @param ca
	 *           the ca to set
	 */
	public void setCad(final CustomerAddressWsDTO ca)
	{
		this.cad = ca;
	}

	/**
	 * @return the pd
	 */
	public ProductDetailsWsDTO getPd()
	{
		return pd;
	}

	/**
	 * @param pd
	 *           the pd to set
	 */
	public void setPd(final ProductDetailsWsDTO pd)
	{
		this.pd = pd;
	}




}
