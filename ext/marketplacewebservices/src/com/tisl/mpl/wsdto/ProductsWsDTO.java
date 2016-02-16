/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.ArrayList;


/**
 * @author TCS
 *
 */
public class ProductsWsDTO implements java.io.Serializable
{
	private String sellerId;
	private String sellerName;
	private String code;
	private String productName;
	private String imageUrl;
	private String description;
	private ArrayList<TypeVariantWsDTO> typevariant;
	private double singleProductPrice;
	private double totalProductPrice;
	private double offerPrice;
	private String deliveryDetails;
	private double qtyOfStock;
	private double qtySelectedByUser;
	private String stockLevelStatus;

	//	public ProductsWsDTO()
	//	{
	//
	//	}

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
	 * @return the sellerName
	 */
	public String getSellerName()
	{
		return sellerName;
	}

	/**
	 * @param sellerName
	 *           the sellerName to set
	 */
	public void setSellerName(final String sellerName)
	{
		this.sellerName = sellerName;
	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the productName
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * @param productName
	 *           the productName to set
	 */
	public void setProductName(final String productName)
	{
		this.productName = productName;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl()
	{
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *           the imageUrl to set
	 */
	public void setImageUrl(final String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *           the description to set
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * @return the typevariant
	 */
	public ArrayList<TypeVariantWsDTO> getTypevariant()
	{
		return typevariant;
	}

	/**
	 * @param typevariant
	 *           the typevariant to set
	 */
	public void setTypevariant(final ArrayList<TypeVariantWsDTO> typevariant)
	{
		this.typevariant = typevariant;
	}

	/**
	 * @return the singleProductPrice
	 */
	public double getSingleProductPrice()
	{
		return singleProductPrice;
	}

	/**
	 * @param singleProductPrice
	 *           the singleProductPrice to set
	 */
	public void setSingleProductPrice(final double singleProductPrice)
	{
		this.singleProductPrice = singleProductPrice;
	}

	/**
	 * @return the totalProductPrice
	 */
	public double getTotalProductPrice()
	{
		return totalProductPrice;
	}

	/**
	 * @param totalProductPrice
	 *           the totalProductPrice to set
	 */
	public void setTotalProductPrice(final double totalProductPrice)
	{
		this.totalProductPrice = totalProductPrice;
	}

	/**
	 * @return the offerPrice
	 */
	public double getOfferPrice()
	{
		return offerPrice;
	}

	/**
	 * @param offerPrice
	 *           the offerPrice to set
	 */
	public void setOfferPrice(final double offerPrice)
	{
		this.offerPrice = offerPrice;
	}

	/**
	 * @return the deliveryDetails
	 */
	public String getDeliveryDetails()
	{
		return deliveryDetails;
	}

	/**
	 * @param deliveryDetails
	 *           the deliveryDetails to set
	 */
	public void setDeliveryDetails(final String deliveryDetails)
	{
		this.deliveryDetails = deliveryDetails;
	}

	/**
	 * @return the qtyOfStock
	 */
	public double getQtyOfStock()
	{
		return qtyOfStock;
	}

	/**
	 * @param qtyOfStock
	 *           the qtyOfStock to set
	 */
	public void setQtyOfStock(final double qtyOfStock)
	{
		this.qtyOfStock = qtyOfStock;
	}

	/**
	 * @return the qtySelectedByUser
	 */
	public double getQtySelectedByUser()
	{
		return qtySelectedByUser;
	}

	/**
	 * @param qtySelectedByUser
	 *           the qtySelectedByUser to set
	 */
	public void setQtySelectedByUser(final double qtySelectedByUser)
	{
		this.qtySelectedByUser = qtySelectedByUser;
	}

	/**
	 * @return the stockLevelStatus
	 */
	public String getStockLevelStatus()
	{
		return stockLevelStatus;
	}

	/**
	 * @param stockLevelStatus
	 *           the stockLevelStatus to set
	 */
	public void setStockLevelStatus(final String stockLevelStatus)
	{
		this.stockLevelStatus = stockLevelStatus;
	}



}
