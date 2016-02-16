/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sourceforge.pmd.util.StringUtil;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCommerceCartDao;
import com.tisl.mpl.model.StateModel;


/**
 * @author TCS
 *
 */
public class MplCommerceCartDaoImpl implements MplCommerceCartDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;


	/**
	 * Method for cart details for cart id
	 *
	 * @param cartCode
	 *
	 * @return CartModel
	 *
	 * @throws InvalidCartException
	 *
	 */
	@Override
	public CartModel getCart(final String cartCode) throws InvalidCartException
	{
		CartModel cartModel = null;
		final StringBuilder cartModelQuery = new StringBuilder(40);
		cartModelQuery.append("SELECT {").append(CartModel.PK).append("} ");
		cartModelQuery.append("FROM {").append(CartModel._TYPECODE).append("} ");
		cartModelQuery.append("WHERE {").append(CartModel.CODE).append("} = ?").append(CartModel.CODE);

		final Map<String, Object> params = new HashMap<String, Object>(1);
		params.put(CartModel.CODE, cartCode);

		final SearchResult<CartModel> searchRes = getFlexibleSearchService().search(cartModelQuery.toString(), params);
		if (searchRes != null && searchRes.getCount() > 0)
		{
			cartModel = searchRes.getResult().get(0);
		}
		return cartModel;

	}

	@Override
	public CartModel getCartByGuid(final String guid) throws InvalidCartException
	{
		CartModel cartModel = null;
		final StringBuilder cartModelQuery = new StringBuilder(40);
		cartModelQuery.append("SELECT {").append(CartModel.PK).append("} ");
		cartModelQuery.append("FROM {").append(CartModel._TYPECODE).append("} ");
		cartModelQuery.append("WHERE {").append(CartModel.GUID).append("} = ?").append(CartModel.GUID);

		final Map<String, Object> params = new HashMap<String, Object>(1);
		params.put(CartModel.GUID, guid);

		final SearchResult<CartModel> searchRes = getFlexibleSearchService().search(cartModelQuery.toString(), params);
		if (searchRes != null && searchRes.getCount() > 0)
		{
			cartModel = searchRes.getResult().get(0);
		}
		return cartModel;
	}

	@Override
	public CartModel getCartForGuidAndSiteAndUser(final String guid, final BaseSiteModel site, final UserModel user)
	{

		CartModel cartModel = null;
		final Map params = new HashMap();
		params.put("guid", guid);
		params.put("site", site);
		params.put("user", user);
		final List carts = doSearch(
				"SELECT {pk} FROM {Cart} WHERE {guid} = ?guid AND {site} = ?site AND {user} = ?user ORDER BY {modifiedtime} DESC",
				params, CartModel.class);
		if ((carts != null) && (!(carts.isEmpty())))
		{
			cartModel = ((CartModel) carts.get(0));
		}
		return cartModel;
	}



	protected <T> List<T> doSearch(final String query, final Map<String, Object> params, final Class<T> resultClass)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}

		fQuery.setResultClassList(Collections.singletonList(resultClass));
		final SearchResult searchResult = getFlexibleSearchService().search(fQuery);
		return searchResult.getResult();
	}

	/*
	 * @Desc fetching state details for a state name
	 * 
	 * @param stateName
	 * 
	 * @return StateModel
	 * 
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<StateModel> fetchStateDetails(final String stateName) throws EtailNonBusinessExceptions
	{
		List<StateModel> stateModelList = null;
		if (StringUtil.isNotEmpty(stateName))
		{
			final String queryString = "SELECT {sm.PK} FROM {" + StateModel._TYPECODE + " AS sm}" + " WHERE {sm:"
					+ StateModel.DESCRIPTION + "}=?stateName";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("stateName", stateName);
			stateModelList = getFlexibleSearchService().<StateModel> search(query).getResult();
		}
		return stateModelList;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
