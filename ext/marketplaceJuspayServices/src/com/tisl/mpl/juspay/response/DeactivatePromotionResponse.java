package com.tisl.mpl.juspay.response;

import com.tisl.mpl.juspay.model.Promotion;



public class DeactivatePromotionResponse
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
