/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
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

	public List<Wishlist2EntryModel> getWishlistByUserAndUssid(final UserModel user, final String ussid);

	/**
	 * @param user
	 * @return
	 */
	List<Wishlist2Model> getWishListAgainstUser(UserModel user);

	//CAR Project performance issue fixed

	/**
	 * Description -- Method will access single WishlistModel for user with respect to Wishlistname
	 *
	 * @return Wishlist2Model
	 */

	public Wishlist2Model findMobileWishlistswithName(UserModel user, String name);

	/**
	 * Description -- Method will access single Entry of a Wishlist
	 *
	 * @return Wishlist2Model
	 */
	public Wishlist2EntryModel findWishlistEntryByProductAndUssid(final String ussid);
}
