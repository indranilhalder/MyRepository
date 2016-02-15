/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.jalo.product.Product;

import java.util.Comparator;
import java.util.Map;


public class ValueComparator implements Comparator<Product>
{

	private final Map<Product, Double> base;

	public ValueComparator(final Map<Product, Double> base)
	{
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with equals.
	public int compare(final Product a, final Product b)
	{
		if (base.get(a).doubleValue() >= base.get(b).doubleValue())
		{
			return -1;
		}
		else
		{
			return 1;
		} // returning 0 would merge keys
	}
}
