/**
 *
 */
package com.tisl.mpl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "Promotion")
@XmlType(propOrder =
{ "promoType", "priority", "promoStartDate", "promoEndDate", "promoCode", "description", "enabled", "channel", "primaryProduct",
		"secondaryProduct", "primaryCategory", "secondaryCategory", "excludedProduct", "freeProduct", "isPercentageDiscount",
		"discount", "categoryMinimumValue", "qcount", "restriction", "brand", "seller" })
public class PromotionXMLData
{
	private String enabled;
	private List<String> freeProduct;
	private List<String> primaryCategory;
	private List<String> restriction;
	private List<String> secondaryProduct;
	private List<String> seller;
	private List<String> excludedProduct;
	private List<String> secondaryCategory;
	private String discount;
	private String qcount;
	private List<String> primaryProduct;
	private String promoStartDate;
	private String priority;
	private String categoryMinimumValue;
	private String description;
	private String promoEndDate;
	private String isPercentageDiscount;
	private List<String> brand;
	private String promoType;
	private String channel;
	private String promoCode;

	//	public PromotionXMLData()
	//	{
	//	}


	public void setEnabled(final String enabled)
	{
		this.enabled = enabled;
	}

	@XmlElement(name = "enabled")
	public String getEnabled()
	{
		return enabled;
	}


	public void setFreeProduct(final List<String> freeProduct)
	{
		this.freeProduct = freeProduct;
	}

	@XmlElement(name = "freeProduct")
	public List<String> getFreeProduct()
	{
		return freeProduct;
	}


	public void setPrimaryCategory(final List<String> primaryCategory)
	{
		this.primaryCategory = primaryCategory;
	}

	@XmlElement(name = "primaryCategory")
	public List<String> getPrimaryCategory()
	{
		return primaryCategory;
	}


	public void setRestriction(final List<String> restriction)
	{
		this.restriction = restriction;
	}

	@XmlElement(name = "restriction")
	public List<String> getRestriction()
	{
		return restriction;
	}


	public void setSecondaryProduct(final List<String> secondaryProduct)
	{
		this.secondaryProduct = secondaryProduct;
	}

	@XmlElement(name = "secondaryProduct")
	public List<String> getSecondaryProduct()
	{
		return secondaryProduct;
	}


	public void setSeller(final List<String> seller)
	{
		this.seller = seller;
	}

	@XmlElement(name = "seller")
	public List<String> getSeller()
	{
		return seller;
	}


	public void setExcludedProduct(final List<String> excludedProduct)
	{
		this.excludedProduct = excludedProduct;
	}

	@XmlElement(name = "excludedProduct")
	public List<String> getExcludedProduct()
	{
		return excludedProduct;
	}


	public void setSecondaryCategory(final List<String> secondaryCategory)
	{
		this.secondaryCategory = secondaryCategory;
	}

	@XmlElement(name = "secondaryCategory")
	public List<String> getSecondaryCategory()
	{
		return secondaryCategory;
	}


	public void setDiscount(final String discount)
	{
		this.discount = discount;
	}

	@XmlElement(name = "discount")
	public String getDiscount()
	{
		return discount;
	}


	public void setQcount(final String qcount)
	{
		this.qcount = qcount;
	}

	@XmlElement(name = "qcount")
	public String getQcount()
	{
		return qcount;
	}


	public void setPrimaryProduct(final List<String> primaryProduct)
	{
		this.primaryProduct = primaryProduct;
	}

	@XmlElement(name = "primaryProduct")
	public List<String> getPrimaryProduct()
	{
		return primaryProduct;
	}


	public void setPromoStartDate(final String promoStartDate)
	{
		this.promoStartDate = promoStartDate;
	}

	@XmlElement(name = "promoStartDate")
	public String getPromoStartDate()
	{
		return promoStartDate;
	}


	public void setPriority(final String priority)
	{
		this.priority = priority;
	}

	@XmlElement(name = "priority")
	public String getPriority()
	{
		return priority;
	}


	public void setCategoryMinimumValue(final String categoryMinimumValue)
	{
		this.categoryMinimumValue = categoryMinimumValue;
	}

	@XmlElement(name = "categoryMinimumValue")
	public String getCategoryMinimumValue()
	{
		return categoryMinimumValue;
	}


	public void setDescription(final String description)
	{
		this.description = description;
	}

	@XmlElement(name = "description")
	public String getDescription()
	{
		return description;
	}


	public void setPromoEndDate(final String promoEndDate)
	{
		this.promoEndDate = promoEndDate;
	}

	@XmlElement(name = "promoEndDate")
	public String getPromoEndDate()
	{
		return promoEndDate;
	}


	public void setIsPercentageDiscount(final String isPercentageDiscount)
	{
		this.isPercentageDiscount = isPercentageDiscount;
	}

	@XmlElement(name = "isPercentageDiscount")
	public String getIsPercentageDiscount()
	{
		return isPercentageDiscount;
	}


	public void setBrand(final List<String> brand)
	{
		this.brand = brand;
	}

	@XmlElement(name = "brand")
	public List<String> getBrand()
	{
		return brand;
	}


	public void setPromoType(final String promoType)
	{
		this.promoType = promoType;
	}

	@XmlElement(name = "promoType")
	public String getPromoType()
	{
		return promoType;
	}


	public void setChannel(final String channel)
	{
		this.channel = channel;
	}

	@XmlElement(name = "channel")
	public String getChannel()
	{
		return channel;
	}


	public void setPromoCode(final String promoCode)
	{
		this.promoCode = promoCode;
	}

	@XmlElement(name = "promoCode")
	public String getPromoCode()
	{
		return promoCode;
	}




}
