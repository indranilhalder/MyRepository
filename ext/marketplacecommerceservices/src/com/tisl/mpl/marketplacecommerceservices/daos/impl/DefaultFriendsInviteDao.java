/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.FriendsModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.FriendsInviteDao;


/**
 * @author TCS
 *
 */
public class DefaultFriendsInviteDao extends DefaultGenericDao<FriendsModel> implements FriendsInviteDao
{
	public DefaultFriendsInviteDao(final String typecode)
	{
		super(typecode);
	}

	private static final String SELECT = "SELECT {";

	@Override
	public List<FriendsModel> findFriendsForACustomer(final String customerEmail)
	{
		try
		{
			final String query1 = SELECT + FriendsModel.PK + "}" + "FROM {" + FriendsModel._TYPECODE + "}" + "WHERE {"
					+ FriendsModel.CUSTOMEREMAIL + "} =?customerEmail"; //
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put(FriendsModel.CUSTOMEREMAIL, customerEmail);

			final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query1);
			searchQuery.addQueryParameters(params);
			searchQuery.setResultClassList(Collections.singletonList(FriendsModel.class));
			final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
			return searchResult.getResult();
		}
		catch (final IllegalArgumentException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final NullPointerException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.FriendsInviteDao#findFriendInvitedForAnAffiliate(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<FriendsModel> findFriendInvitedForAnAffiliate(final String affiliateId, final String friendsEmail)
	{
		try
		{
			final String query1 = SELECT + FriendsModel.PK + "}" + " FROM {" + FriendsModel._TYPECODE + "}" + " WHERE {"
					+ FriendsModel.AFFILIATEID + "} =?affiliateId" + " AND {" + FriendsModel.FRIENDSEMAIL + "}=?friendsEmail";
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put(FriendsModel.AFFILIATEID, affiliateId);
			params.put(FriendsModel.FRIENDSEMAIL, friendsEmail);

			final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query1);
			searchQuery.addQueryParameters(params);
			searchQuery.setResultClassList(Collections.singletonList(FriendsModel.class));
			final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
			return searchResult.getResult();
		}
		catch (final IllegalArgumentException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final NullPointerException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.FriendsInviteDao#findFriendsInvitedForAnAffiliate(java.lang.String)
	 */
	@Override
	public List<FriendsModel> findFriendsInvitedForAnAffiliate(final String affiliateId)
	{
		try
		{
			final String query1 = SELECT + FriendsModel.PK + "}" + "FROM {" + FriendsModel._TYPECODE + "}" + "WHERE {"
					+ FriendsModel.AFFILIATEID + "} =?affiliateId"; //
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put(FriendsModel.AFFILIATEID, affiliateId);

			final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query1);
			searchQuery.addQueryParameters(params);
			searchQuery.setResultClassList(Collections.singletonList(FriendsModel.class));
			final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
			return searchResult.getResult();
		}
		catch (final IllegalArgumentException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final NullPointerException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.FriendsInviteDao#findFriendsInvitedByAffIdAndFriendsEmail(de.hybris
	 * .platform.core.model.user.FriendsModel)
	 */
	@Override
	public List<FriendsModel> findFriendsInvitedByAffIdAndFriendsEmail(final FriendsModel friendsModel)
	{
		try
		{
			final String affiliateId = friendsModel.getAffiliateId();
			final String friendsEmail = friendsModel.getFriendsEmail();

			final String query1 = SELECT
					+ FriendsModel.PK
					+ "}"
					+ "FROM {Friends as f join Customer as c on {f.affiliateId}={c.uid}} where {f.affiliateId} = ?affiliateId and {f.friendsEmail} = ?friendsEmail";

			final Map<String, Object> params = new HashMap<String, Object>();
			params.put(FriendsModel.AFFILIATEID, affiliateId);
			params.put(FriendsModel.FRIENDSEMAIL, friendsEmail);

			final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query1);
			searchQuery.addQueryParameters(params);
			searchQuery.setResultClassList(Collections.singletonList(FriendsModel.class));
			final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
			return searchResult.getResult();
		}
		catch (final IllegalArgumentException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final NullPointerException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 *
	 */
	@Override
	public List<CustomerModel> findCustomerForUid(final String uid)
	{
		try
		{
			final String queryString = //
			"SELECT {p:" + CustomerModel.PK + "}" //
					+ "FROM {" + CustomerModel._TYPECODE + " AS p} "//
					+ "WHERE " + "{p:" + CustomerModel.UID + "}=?uid ";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("uid", uid);
			final SearchResult searchResult = getFlexibleSearchService().search(query);
			return searchResult.getResult();
		}
		catch (final IllegalArgumentException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final NullPointerException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}
}
