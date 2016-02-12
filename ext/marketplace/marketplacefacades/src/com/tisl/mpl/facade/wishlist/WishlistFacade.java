/**
 *
 */
package com.tisl.mpl.facade.wishlist;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;

import com.tisl.mpl.data.EditWishlistNameData;



/**
 * @author TCS
 *
 */
public interface WishlistFacade
{
	int getSize();

	Wishlist2Model removeProductFromWl(final String productCode, final String wishlistName, String ussid);

	List<Wishlist2Model> getAllWishlists();

	Wishlist2Model createNewWishlist(final UserModel user, final String name, final String description);

	boolean addProductToWishlist(final Wishlist2Model wishlist, final String productCode, final String ussid, boolean sizeSelected);

	Wishlist2Model getWishlistForName(final String wishlistName);

	Wishlist2Model editWishlistName(EditWishlistNameData editWishlistNameData);

	void removeProductFromWL(final String orderCode);

	/**
	 * @param ussid
	 * @param productData1
	 * @return
	 */
	ProductData getBuyBoxPrice(String ussid, ProductData productData1);
}
