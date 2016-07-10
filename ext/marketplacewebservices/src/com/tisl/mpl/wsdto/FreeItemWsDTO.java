/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.ArrayList;


/**
 * @author TCS
 *
 */
public class FreeItemWsDTO implements java.io.Serializable
{
	private String sellerId;
	private String sellerName;
	private String code;
	private String productName;
	private String imageUrl;
	private String description;
	private ArrayList<TypeVariantWsDTO> typevariant;

	//	public FreeItemWsDTO()
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


}
