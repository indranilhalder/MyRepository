/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import java.util.Date;

import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author 592217
 *
 */
public class OrderPlacedFeedbackEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	private String orderCode;
	Date dateOfDelivery;
	private String refNo;
	private String trancNo;
	private String nameOfProduct;
	private String feedback;
	SellerInformationModel seller;
	private String sellerName;

	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
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
	 * @return the dateOfDelivery
	 */
	public Date getDateOfDelivery()
	{
		return dateOfDelivery;
	}

	/**
	 * @param dateOfDelivery
	 *           the dateOfDelivery to set
	 */
	public void setDateOfDelivery(final Date dateOfDelivery)
	{
		this.dateOfDelivery = dateOfDelivery;
	}

	/**
	 * @return the refNo
	 */
	public String getRefNo()
	{
		return refNo;
	}

	/**
	 * @param refNo
	 *           the refNo to set
	 */
	public void setRefNo(final String refNo)
	{
		this.refNo = refNo;
	}

	/**
	 * @return the trancNo
	 */
	public String getTrancNo()
	{
		return trancNo;
	}

	/**
	 * @param trancNo
	 *           the trancNo to set
	 */
	public void setTrancNo(final String trancNo)
	{
		this.trancNo = trancNo;
	}

	/**
	 * @return the nameOfProduct
	 */
	public String getNameOfProduct()
	{
		return nameOfProduct;
	}

	/**
	 * @param nameOfProduct
	 *           the nameOfProduct to set
	 */
	public void setNameOfProduct(final String nameOfProduct)
	{
		this.nameOfProduct = nameOfProduct;
	}

	/**
	 * @return the feedback
	 */
	public String getFeedback()
	{
		return feedback;
	}

	/**
	 * @param feedback
	 *           the feedback to set
	 */
	public void setFeedback(final String feedback)
	{
		this.feedback = feedback;
	}

	/**
	 * @return the seller
	 */
	public SellerInformationModel getSeller()
	{
		return seller;
	}

	/**
	 * @param seller
	 *           the seller to set
	 */
	public void setSeller(final SellerInformationModel seller)
	{
		this.seller = seller;
	}

}
