/**
 *
 */
package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.product.Product;

import java.util.List;


/**
 * @author TCS
 *
 */
public class PromotionUtilityPOJO
{
	private List<Product> promoProductList;

	/**
	 * @return the promoProductList
	 */
	public List<Product> getPromoProductList()
	{
		return promoProductList;
	}

	/**
	 * @param promoProductList
	 *           the promoProductList to set
	 */
	public void setPromoProductList(final List<Product> promoProductList)
	{
		this.promoProductList = promoProductList;
	}

}
