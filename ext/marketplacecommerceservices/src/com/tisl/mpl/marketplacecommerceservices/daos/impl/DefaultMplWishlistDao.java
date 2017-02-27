/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
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

	@Override
	public List<Wishlist2EntryModel> findWishlistByUserAndUssid(final UserModel user, final String ussid)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.WISHLIST_BY_USSID);
		query.addQueryParameter("user", user);
		query.addQueryParameter("ussid", ussid);
		return flexibleSearchService.<Wishlist2EntryModel> search(query).getResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplWishlistDao#getWishListAgainstUser(de.hybris.platform.core.model
	 * .user.UserModel)
	 */
	@Override
	public List<Wishlist2Model> getWishListAgainstUser(final UserModel user)
	{

		final String queryString = //
		"SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user ORDER BY {modifiedtime} desc";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("user", user);
		return flexibleSearchService.<Wishlist2Model> search(query).getResult();
	}



}
