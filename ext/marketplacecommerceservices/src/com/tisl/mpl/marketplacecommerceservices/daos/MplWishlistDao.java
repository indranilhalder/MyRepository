/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplWishlistDao
{
	public abstract List<Wishlist2Model> findAllWishlists(UserModel paramUserModel);
}
