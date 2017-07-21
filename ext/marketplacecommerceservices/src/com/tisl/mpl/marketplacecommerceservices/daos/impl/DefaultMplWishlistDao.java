/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.MplWishlistDao;


/**
 * @author 594165
 *
 */
public class DefaultMplWishlistDao implements MplWishlistDao
{
	//TPR-5787 autowired changed to resource
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<Wishlist2Model> findAllWishlists(final UserModel user)
	{
		final String queryString = //
		"SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user ORDER BY {creationtime} desc";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.USERPARAM, user);
		return flexibleSearchService.<Wishlist2Model> search(query).getResult();
	}

	@Override
	public List<Wishlist2EntryModel> findWishlistByUserAndUssid(final UserModel user, final String ussid)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.WISHLIST_BY_USSID);
		query.addQueryParameter(MarketplacecommerceservicesConstants.USERPARAM, user);
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
		query.addQueryParameter(MarketplacecommerceservicesConstants.USERPARAM, user);
		return flexibleSearchService.<Wishlist2Model> search(query).getResult();
	}




	@Override
	public Wishlist2Model findMobileWishlistswithName(final UserModel user, final String name)
	{
		final String queryString = //
		"SELECT {pk} FROM {Wishlist2 as wish} WHERE {wish.user} = ?user AND {wish.name}=?name";
		final Map<String, Object> params = new HashMap<String, Object>(1);
		params.put(MarketplacecommerceservicesConstants.USERPARAM, user);
		params.put("name", name);

		final SearchResult<Wishlist2Model> searchRes = flexibleSearchService.search(queryString, params);
		if (searchRes != null && searchRes.getCount() > 0)
		{
			return searchRes.getResult().get(0);
		}

		return null;
	}

	//CAR Project performance issue fixed
	/**
	 * Description -- Method will access single WishlistModel for user with respect to Wishlistname
	 *
	 * @return Wishlist2Model
	 */

	@Override
	public int findMobileWishlistswithNameCount(final UserModel user, final String name)
	{
		final String queryString = "SELECT {pk} FROM {Wishlist2 as wish} WHERE {wish.user} = ?user AND {wish.name} like '%" + name
				+ "%' ";
		final Map<String, Object> params = new HashMap<String, Object>(1);
		params.put(MarketplacecommerceservicesConstants.USERPARAM, user);
		//params.put("name", name);

		final SearchResult<Wishlist2Model> searchRes = flexibleSearchService.search(queryString, params);
		if (searchRes != null && searchRes.getCount() > 0)
		{
			return searchRes.getCount();
		}

		return 0;
	}


}
