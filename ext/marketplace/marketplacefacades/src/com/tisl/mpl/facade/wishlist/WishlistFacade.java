/**
 *
 */
package com.tisl.mpl.facade.wishlist;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
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


	Wishlist2Model removeProductFromWl(final String productCode, final String wishlistName); /* Changes for INC144313867 */

	List<Wishlist2Model> getAllWishlists();

	Wishlist2Model createNewWishlist(final UserModel user, final String name, final String description);

	boolean addProductToWishlist(final Wishlist2Model wishlist, final String productCode, final String ussid, boolean sizeSelected);

	Wishlist2Model getWishlistForName(final String wishlistName);

	Wishlist2Model editWishlistName(EditWishlistNameData editWishlistNameData);

	void removeProductFromWL(final String orderCode);

	/**
	 * @param entryModel
	 * @return
	 */
	boolean removeWishlistEntry(final Wishlist2EntryModel entryModel);

	/**
	 * @param ussid
	 * @param productData1
	 * @return
	 */
	ProductData getBuyBoxPrice(String ussid, ProductData productData1);

	/**
	 * @param ussid
	 * @param wishlistEntry
	 * @return
	 */
	public List<Wishlist2EntryModel> getAllWishlistByUssid(final String ussid);

	/**
	 * @param orderDetails
	 */
	void remProdFromWLForConf(OrderData orderDetails, UserModel userModel); //TISPT-175 : changing to reduce populator call multiple times


	/**
	 * Desc It will fetch all wishlists for a customer/user TISPT-179
	 *
	 * @param userModel
	 * @return List<Wishlist2Model>
	 */
	List<Wishlist2Model> getAllWishlistsForCustomer(UserModel userModel);

	/**
	 * @param user
	 * @return
	 */
	Wishlist2Model getSingleWishlist(UserModel user);

	//CAR Project performance issue fixed

	/**
	 * Description -- Method will access single WishlistModel for user with respect to Wishlistname
	 *
	 * @return Wishlist2Model
	 */

	Wishlist2Model findMobileWishlistswithName(UserModel user, String name);

	//CAR Project performance issue fixed

	/**
	 * Description -- Method will count for user with respect to Wishlistname
	 *
	 * @return Wishlist2Model
	 */

	int findMobileWishlistswithNameCount(UserModel user, String name);

	/**
	 * Description -- To add product in Wishlistname
	 *
	 * @return boolean
	 */
	public boolean addProductToWishlistMobile(final Wishlist2Model wishlist, final String productCode, final String ussid,
			final boolean selectedSize);
}
