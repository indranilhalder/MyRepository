/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * @author TCS
 *
 */
public class MplAddToCartForm
{
	

	@NotNull(message = "{basket.error.quantity.notNull}")
	@Min(value = 0, message = "{basket.error.quantity.invalid}")
	@Digits(fraction = 0, integer = 10, message = "{basket.error.quantity.invalid}")
	private long qty = 1L;
	private long stock = 1L;


	public void setQty(final long quantity)
	{
		this.qty = quantity;
	}

	public long getQty()
	{
		return qty;
	}

	/**
	 * @return the stock
	 */
	public long getStock()
	{
		return stock;
	}

	/**
	 * @param stock
	 *           the stock to set
	 */
	public void setStock(final long stock)
	{
		this.stock = stock;
	}
}
