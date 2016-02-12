/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplWishlistService
{
	/**
	 * @param paramWishlist2Model
	 * @param paramProductModel
	 * @param desired
	 * @param paramWishlist2EntryPriority
	 * @param comment
	 * @param ussid
	 * @param selectedSize
	 */
	void addWishlistEntry(Wishlist2Model paramWishlist2Model, ProductModel paramProductModel, Integer desired,
			Wishlist2EntryPriority paramWishlist2EntryPriority, String comment, String ussid, boolean selectedSize);

	public abstract List<Wishlist2Model> getWishlists(UserModel user);
}
