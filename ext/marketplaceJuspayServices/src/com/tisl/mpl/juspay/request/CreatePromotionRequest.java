package com.tisl.mpl.juspay.request;



import java.util.List;

import com.tisl.mpl.juspay.PromotionCondition;


/**
 * Represents a promotion and applies the discount amount conditionally on the order.
 */
public class CreatePromotionRequest
{
	private String orderId;
	private double discountAmount;

	private List<PromotionCondition> promotionConditions;

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	public double getDiscountAmount()
	{
		return discountAmount;
	}

	public void setDiscountAmount(final double discountAmount)
	{
		this.discountAmount = discountAmount;
	}

	public List<PromotionCondition> getPromotionConditions()
	{
		return promotionConditions;
	}

	public void setPromotionConditions(final List<PromotionCondition> promotionConditions)
	{
		this.promotionConditions = promotionConditions;
	}

	public CreatePromotionRequest withOrderId(final String orderId)
	{
		this.orderId = orderId;
		return this;
	}

	public CreatePromotionRequest withDiscountAmount(final double discountAmount)
	{
		this.discountAmount = discountAmount;
		return this;
	}

	public CreatePromotionRequest withDimensions(final List<PromotionCondition> promotionConditions)
	{
		this.promotionConditions = promotionConditions;
		return this;
	}
}
