/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.MplWishlistDao;


/**
 * @author 594165
 *
 */
public class DefaultMplWishlistDao implements MplWishlistDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<Wishlist2Model> findAllWishlists(final UserModel user)
	{
		final String queryString = //
		"SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user ORDER BY {creationtime} desc";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("user", user);
		return flexibleSearchService.<Wishlist2Model> search(query).getResult();
	}

}
