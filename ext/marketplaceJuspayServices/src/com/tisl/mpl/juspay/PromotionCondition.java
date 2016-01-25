package com.tisl.mpl.juspay;

/**
 * Represents a condition under which the promotion is applied. Typically used for representing card_number which has to
 * be used for completing an order.
 */
public class PromotionCondition
{
	private String dimension;
	private String value;

	public String getDimension()
	{
		return dimension;
	}

	public void setDimension(final String dimension)
	{
		this.dimension = dimension;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(final String value)
	{
		this.value = value;
	}
}
