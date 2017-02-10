/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplWishlistDao
{
	public abstract List<Wishlist2Model> findAllWishlists(UserModel paramUserModel);

	public List<Wishlist2EntryModel> findWishlistByUserAndUssid(final UserModel user, final String ussid);

	/**
	 * @param user
	 * @return
	 */
	public abstract List<Wishlist2Model> getWishListAgainstUser(UserModel user);

	//CAR Project performance issue fixed
	/**
	 * Description -- Method will access single WishlistModel for user with respect to Wishlistname
	 *
	 * @return Wishlist2Model
	 */
	public Wishlist2Model findMobileWishlistswithName(final UserModel user, final String name);

	//CAR Project performance issue fixed
	/**
	 * Description -- Method will provide count for user with respect to Wishlistname
	 *
	 * @return int
	 */
	public int findMobileWishlistswithNameCount(final UserModel user, final String name);

	/**
	 * Description -- Method will access single Entry of a Wishlist
	 *
	 * @return Wishlist2Model
	 */
	public Wishlist2EntryModel findWishlistEntryByProductAndUssid(final String ussid);
}
