/**
 *
 */
package com.tisl.mpl.storefront.util;

import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

import java.util.Comparator;


/**
 * @author 594165
 *
 */
public class AllProductsInWishlistByDate implements Comparator<Wishlist2EntryModel>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Wishlist2EntryModel arg0, final Wishlist2EntryModel arg1)
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
