/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;


/**
 * @author TCS
 *
 */
public class YouMayLikeWsDTO implements java.io.Serializable
{
	private String sellerId;
	private String sellerName;
	private List<AllImageWsDTO> allimg;
	private String brandName;
	private String productId;
	private String productName;
	private String productLogo;
	private String productFlag;
	private String productDescription;
	private double price;
	private double offerPrice;
	private String emiTag;
	private String styleNote;
	private String tataPromise;



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
	 * @return the allimg
	 */
	public List<AllImageWsDTO> getAllimg()
	{
		return allimg;
	}

	/**
	 * @param allimg
	 *           the allimg to set
	 */
	public void setAllimg(final List<AllImageWsDTO> allimg)
	{
		this.allimg = allimg;
	}

	/**
	 * @return the brandName
	 */
	public String getBrandName()
	{
		return brandName;
	}

	/**
	 * @param brandName
	 *           the brandName to set
	 */
	public void setBrandName(final String brandName)
	{
		this.brandName = brandName;
	}

	/**
	 * @return the productId
	 */
	public String getProductId()
	{
		return productId;
	}

	/**
	 * @param productId
	 *           the productId to set
	 */
	public void setProductId(final String productId)
	{
		this.productId = productId;
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
	 * @return the productLogo
	 */
	public String getProductLogo()
	{
		return productLogo;
	}

	/**
	 * @param productLogo
	 *           the productLogo to set
	 */
	public void setProductLogo(final String productLogo)
	{
		this.productLogo = productLogo;
	}

	/**
	 * @return the productFlag
	 */
	public String getProductFlag()
	{
		return productFlag;
	}

	/**
	 * @param productFlag
	 *           the productFlag to set
	 */
	public void setProductFlag(final String productFlag)
	{
		this.productFlag = productFlag;
	}

	/**
	 * @return the productDescription
	 */
	public String getProductDescription()
	{
		return productDescription;
	}

	/**
	 * @param productDescription
	 *           the productDescription to set
	 */
	public void setProductDescription(final String productDescription)
	{
		this.productDescription = productDescription;
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
	 * @return the emiTag
	 */
	public String getEmiTag()
	{
		return emiTag;
	}

	/**
	 * @param emiTag
	 *           the emiTag to set
	 */
	public void setEmiTag(final String emiTag)
	{
		this.emiTag = emiTag;
	}

	/**
	 * @return the styleNote
	 */
	public String getStyleNote()
	{
		return styleNote;
	}

	/**
	 * @param styleNote
	 *           the styleNote to set
	 */
	public void setStyleNote(final String styleNote)
	{
		this.styleNote = styleNote;
	}

	/**
	 * @return the tataPromise
	 */
	public String getTataPromise()
	{
		return tataPromise;
	}

	/**
	 * @param tataPromise
	 *           the tataPromise to set
	 */
	public void setTataPromise(final String tataPromise)
	{
		this.tataPromise = tataPromise;
	}

}
