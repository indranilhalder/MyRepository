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
}
