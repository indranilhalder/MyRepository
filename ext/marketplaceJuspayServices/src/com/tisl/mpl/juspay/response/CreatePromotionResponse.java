package com.tisl.mpl.juspay.response;

import com.tisl.mpl.juspay.model.Promotion;




/**
 * Represents the response received when a promotion is applied against an order.
 */
public class CreatePromotionResponse
{
	private boolean success;
	private Promotion promotion;

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(final boolean success)
	{
		this.success = success;
	}

	public Promotion getPromotion()
	{
		return promotion;
	}

	public void setPromotion(final Promotion promotion)
	{
		this.promotion = promotion;
	}
}
