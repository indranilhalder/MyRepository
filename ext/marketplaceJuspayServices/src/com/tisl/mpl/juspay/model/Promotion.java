package com.tisl.mpl.juspay.model;



import java.util.List;

import com.tisl.mpl.juspay.PromotionCondition;
import com.tisl.mpl.juspay.PromotionStatus;


/**
 * Represents a promotional discount upon one or more conditions.
 */
public class Promotion
{
	private String id;
	private String orderId;
	private Double discountAmount;
	private PromotionStatus status;
	private List<PromotionCondition> promotionConditions;

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	public Double getDiscountAmount()
	{
		return discountAmount;
	}

	public void setDiscountAmount(final Double discountAmount)
	{
		this.discountAmount = discountAmount;
	}

	public PromotionStatus getStatus()
	{
		return status;
	}

	public void setStatus(final PromotionStatus status)
	{
		this.status = status;
	}

	public List<PromotionCondition> getPromotionConditions()
	{
		return promotionConditions;
	}

	public void setPromotionConditions(final List<PromotionCondition> promotionConditions)
	{
		this.promotionConditions = promotionConditions;
	}

	@Override
	public String toString()
	{
		return "Promotion{" + "id='" + id + '\'' + ", orderId='" + orderId + '\'' + ", discountAmount=" + discountAmount
				+ ", status=" + status + ", promotionConditions=" + promotionConditions + '}';
	}
}
