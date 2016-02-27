/**
 *
 */
package com.tisl.mpl.pojo;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.jobs.PromotionCreationJob;


/**
 * @author TCS
 *
 */
public class CampaignData
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionCreationJob.class.getName());

	private String identifier;
	private String title;
	private String promotionGrp;
	private String description;
	private String enabled;
	private String priority;
	private String channel;
	private String products;
	private String categories;
	private String excludedProducts;
	private String catMinAmnt;
	private String quantity;
	private String maxDiscount;
	private String isPercentage;
	private String percentage;
	private String discountPrices;
	private String giftProducts;
	private String startDate;
	private String endDate;
	private String restrictions;
	private String firedMessage;
	private String couldFireMessage;
	private String secProducts;
	private String secCategories;
	private String threshTotals;
	private String isTship;
	private String isSShip;
	private String discountType;
	private String deliveryMode;
	private String freecount;
	private String url;


	/**
	 * @return the identifier
	 */
	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * @param identifier
	 *           the identifier to set
	 */
	public void setIdentifier(final String identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *           the title to set
	 */
	public void setTitle(final String title)
	{
		this.title = title;
	}

	/**
	 * @return the promotionGrp
	 */
	public String getPromotionGrp()
	{
		return promotionGrp;
	}

	/**
	 * @param promotionGrp
	 *           the promotionGrp to set
	 */
	public void setPromotionGrp(final String promotionGrp)
	{
		this.promotionGrp = promotionGrp;
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
	 * @return the enabled
	 */
	public String getEnabled()
	{
		return enabled;
	}

	/**
	 * @param enabled
	 *           the enabled to set
	 */
	public void setEnabled(final String enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * @return the priority
	 */
	public String getPriority()
	{
		return priority;
	}

	/**
	 * @param priority
	 *           the priority to set
	 */
	public void setPriority(final String priority)
	{
		this.priority = priority;
	}

	/**
	 * @return the channel
	 */
	public String getChannel()
	{
		return channel;
	}

	/**
	 * @param channel
	 *           the channel to set
	 */
	public void setChannel(final String channel)
	{
		this.channel = channel;
	}

	/**
	 * @return the products
	 */
	public String getProducts()
	{
		return products;
	}

	/**
	 * @param products
	 *           the products to set
	 */
	public void setProducts(final String products)
	{
		this.products = products;
	}

	/**
	 * @return the categories
	 */
	public String getCategories()
	{
		return categories;
	}

	/**
	 * @param categories
	 *           the categories to set
	 */
	public void setCategories(final String categories)
	{
		this.categories = categories;
	}

	/**
	 * @return the excludedProducts
	 */
	public String getExcludedProducts()
	{
		return excludedProducts;
	}

	/**
	 * @param excludedProducts
	 *           the excludedProducts to set
	 */
	public void setExcludedProducts(final String excludedProducts)
	{
		this.excludedProducts = excludedProducts;
	}

	/**
	 * @return the catMinAmnt
	 */
	public String getCatMinAmnt()
	{
		return catMinAmnt;
	}

	/**
	 * @param catMinAmnt
	 *           the catMinAmnt to set
	 */
	public void setCatMinAmnt(final String catMinAmnt)
	{
		this.catMinAmnt = catMinAmnt;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity()
	{
		return quantity;
	}

	/**
	 * @param quantity
	 *           the quantity to set
	 */
	public void setQuantity(final String quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * @return the maxDiscount
	 */
	public String getMaxDiscount()
	{
		return maxDiscount;
	}

	/**
	 * @param maxDiscount
	 *           the maxDiscount to set
	 */
	public void setMaxDiscount(final String maxDiscount)
	{
		this.maxDiscount = maxDiscount;
	}

	/**
	 * @return the isPercentage
	 */
	public String getIsPercentage()
	{
		return isPercentage;
	}

	/**
	 * @param isPercentage
	 *           the isPercentage to set
	 */
	public void setIsPercentage(final String isPercentage)
	{
		this.isPercentage = isPercentage;
	}

	/**
	 * @return the percentage
	 */
	public String getPercentage()
	{
		return percentage;
	}

	/**
	 * @param percentage
	 *           the percentage to set
	 */
	public void setPercentage(final String percentage)
	{
		this.percentage = percentage;
	}

	/**
	 * @return the discountPrices
	 */
	public String getDiscountPrices()
	{
		return discountPrices;
	}

	/**
	 * @param discountPrices
	 *           the discountPrices to set
	 */
	public void setDiscountPrices(final String discountPrices)
	{
		this.discountPrices = discountPrices;
	}

	/**
	 * @return the giftProducts
	 */
	public String getGiftProducts()
	{
		return giftProducts;
	}

	/**
	 * @param giftProducts
	 *           the giftProducts to set
	 */
	public void setGiftProducts(final String giftProducts)
	{
		this.giftProducts = giftProducts;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *           the startDate to set
	 */
	public void setStartDate(final String startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate
	 *           the endDate to set
	 */
	public void setEndDate(final String endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return the restrictions
	 */
	public String getRestrictions()
	{
		return restrictions;
	}

	/**
	 * @param restrictions
	 *           the restrictions to set
	 */
	public void setRestrictions(final String restrictions)
	{
		this.restrictions = restrictions;
	}

	/**
	 * @return the firedMessage
	 */
	public String getFiredMessage()
	{
		return firedMessage;
	}

	/**
	 * @param firedMessage
	 *           the firedMessage to set
	 */
	public void setFiredMessage(final String firedMessage)
	{
		this.firedMessage = firedMessage;
	}

	/**
	 * @return the couldFireMessage
	 */
	public String getCouldFireMessage()
	{
		return couldFireMessage;
	}

	/**
	 * @param couldFireMessage
	 *           the couldFireMessage to set
	 */
	public void setCouldFireMessage(final String couldFireMessage)
	{
		this.couldFireMessage = couldFireMessage;
	}

	/**
	 * @return the secProducts
	 */
	public String getSecProducts()
	{
		return secProducts;
	}

	/**
	 * @param secProducts
	 *           the secProducts to set
	 */
	public void setSecProducts(final String secProducts)
	{
		this.secProducts = secProducts;
	}

	/**
	 * @return the secCategories
	 */
	public String getSecCategories()
	{
		return secCategories;
	}

	/**
	 * @param secCategories
	 *           the secCategories to set
	 */
	public void setSecCategories(final String secCategories)
	{
		this.secCategories = secCategories;
	}

	/**
	 * @return the threshTotals
	 */
	public String getThreshTotals()
	{
		return threshTotals;
	}

	/**
	 * @param threshTotals
	 *           the threshTotals to set
	 */
	public void setThreshTotals(final String threshTotals)
	{
		this.threshTotals = threshTotals;
	}

	/**
	 * @return the isTship
	 */
	public String getIsTship()
	{
		return isTship;
	}

	/**
	 * @param isTship
	 *           the isTship to set
	 */
	public void setIsTship(final String isTship)
	{
		this.isTship = isTship;
	}

	/**
	 * @return the isSShip
	 */
	public String getIsSShip()
	{
		return isSShip;
	}

	/**
	 * @param isSShip
	 *           the isSShip to set
	 */
	public void setIsSShip(final String isSShip)
	{
		this.isSShip = isSShip;
	}

	/**
	 * @return the discountType
	 */
	public String getDiscountType()
	{
		return discountType;
	}

	/**
	 * @param discountType
	 *           the discountType to set
	 */
	public void setDiscountType(final String discountType)
	{
		this.discountType = discountType;
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
	 * @return the freecount
	 */
	public String getFreecount()
	{
		return freecount;
	}

	/**
	 * @param freecount
	 *           the freecount to set
	 */
	public void setFreecount(final String freecount)
	{
		this.freecount = freecount;
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 *           the url to set
	 */
	public void setUrl(final String url)
	{
		this.url = url;
	}





}