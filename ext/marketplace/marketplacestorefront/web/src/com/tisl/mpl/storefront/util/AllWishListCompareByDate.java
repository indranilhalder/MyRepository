/**
 * 
 */
package com.tisl.mpl.storefront.util;

import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.Comparator;


/**
 * @author TCS
 * 
 */
public class AllWishListCompareByDate implements Comparator<Wishlist2Model>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Wishlist2Model arg0, final Wishlist2Model arg1)
	{

		if (arg1.getCreationtime().compareTo(arg0.getCreationtime()) > 0)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}



}
