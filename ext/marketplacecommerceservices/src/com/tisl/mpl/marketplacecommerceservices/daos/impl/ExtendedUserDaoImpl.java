package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.CustomerOldEmailDetailsModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.ExtendedUserDao;
import com.tisl.mpl.marketplacecommerceservices.service.impl.ExtendedUserServiceImpl;



/**
 * The Class ExtendedUserDaoImpl - default {@link ExtendedUserDao} implementation used by
 * {@link ExtendedUserServiceImpl}.
 *
 * @author TCS Reply
 */
public class ExtendedUserDaoImpl extends DefaultGenericDao<CustomerModel> implements ExtendedUserDao
{
	private static final String ANNONYMOUS = "anonymous";
	private static final String FIND_ALL_CUSTOMERS_BY_DATE_RANGE = "SELECT {c.pk} FROM {Customer as c} WHERE ({c.modifiedtime} >= ?startDate AND {c.modifiedtime} <= ?endDate) OR ({c.creationtime} >= ?startDate AND {c.creationtime} <= ?endDate)";
	private static final String FIND_CUSTOMER_BY_ORIGINALUID = "SELECT {c.pk} FROM {Customer as c} WHERE ({c.originalUid} = ?originalUID )";
	private static final String FIND_CUSTOMER_BY_UID = "SELECT {c.pk} FROM {Customer as c} WHERE ({c.UID} = ?uid )";
	private static final String FIND_CUSTOMER_BY_OLDORIGINALUID = "SELECT {cd.pk} FROM {CustomerOldEmailDetails as cd} WHERE ({cd.oldOriginalUid} = ?oldOriginalUid )";

	/**
	 * Instantiates a new extended user dao impl.
	 */
	public ExtendedUserDaoImpl()
	{
		super(CustomerModel._TYPECODE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @extaccelerator.core.user.dao.ExtendedUserDao#findUserByUIDandSiteID(java.lang.String, java.lang.String)
	 */
	@Override
	public CustomerModel findUserByUID(final String uid)
	{
		final List<CustomerModel> resList;
		final Map parameters = new HashMap();

		if (ANNONYMOUS.equals(uid))
		{
			parameters.put(CustomerModel.UID, uid);
		}
		else
		{
			parameters.put(CustomerModel.ORIGINALUID, uid);

		}

		resList = find(parameters);

		if (resList.size() > 1)
		{
			throw new AmbiguousIdentifierException(MarketplacecommerceservicesConstants.FOUND + resList.size()
					+ " users with the unique uid '" + uid + "'");
		}
		return resList.isEmpty() ? null : resList.get(0);
	}

	/**
	 * @param uid
	 * @return UserModel
	 */
	@Override
	public UserModel findUserByEmailId(final String uid)
	{
		final List resList = find(Collections.singletonMap("uid", uid));
		if (resList.size() > 1)
		{
			throw new AmbiguousIdentifierException(MarketplacecommerceservicesConstants.FOUND + resList.size()
					+ " users with the unique uid '" + uid + "'");
		}
		return ((resList.isEmpty()) ? null : (UserModel) resList.get(0));
	}

	/**
	 * @param originalUID
	 * @return CustomerModel
	 */
	@Override
	public CustomerModel findUserByoriginalUID(final String originalUID)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CUSTOMER_BY_ORIGINALUID);
		query.addQueryParameter("originalUID", originalUID);
		final List<CustomerModel> resList = getFlexibleSearchService().<CustomerModel> search(query).getResult();
		if (resList.size() > 1)
		{
			throw new AmbiguousIdentifierException(MarketplacecommerceservicesConstants.FOUND + resList.size()
					+ " users with the unique emailid '" + originalUID + "'");
		}
		return resList.isEmpty() ? null : resList.get(0);
	}

	/**
	 * set old emailid when user is changing the emailId
	 *
	 * @param oldOriginalUID
	 * @return CustomerModel
	 */
	@Override
	public CustomerOldEmailDetailsModel findUserByOldoriginalUID(final String oldOriginalUID)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CUSTOMER_BY_OLDORIGINALUID);
		query.addQueryParameter("oldOriginalUID", oldOriginalUID);
		final List<CustomerOldEmailDetailsModel> resList = getFlexibleSearchService().<CustomerOldEmailDetailsModel> search(query)
				.getResult();
		if (resList.size() > 1)
		{
			throw new AmbiguousIdentifierException(MarketplacecommerceservicesConstants.FOUND + resList.size()
					+ " users with the unique emailid '" + oldOriginalUID + "'");
		}
		return resList.isEmpty() ? null : resList.get(0);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @extaccelerator.core.user.dao.ExtendedUserDao#findAllUsers()
	 */
	@Override
	public List<CustomerModel> findAllUsers()
	{
		final List<CustomerModel> resList = find();
		return resList.isEmpty() ? null : resList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see core.user.dao.ExtendedUserDao#findNewAndModifiedUsersByDateRange(java.util.Date, java.util.Date)
	 */
	@Override
	public List<CustomerModel> findNewAndModifiedUsersByDateRange(final Date startDate, final Date endDate)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ALL_CUSTOMERS_BY_DATE_RANGE);
		query.addQueryParameter("startDate", startDate);
		query.addQueryParameter("endDate", endDate);
		final List<CustomerModel> resList = getFlexibleSearchService().<CustomerModel> search(query).getResult();
		return resList;
	}

	@Override
	public CustomerModel findCustomerModelByUID(final String uid)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CUSTOMER_BY_UID);
		query.addQueryParameter("UID", uid);
		final List<CustomerModel> resList = getFlexibleSearchService().<CustomerModel> search(query).getResult();
		if (resList.size() > 1)
		{
			throw new AmbiguousIdentifierException(MarketplacecommerceservicesConstants.FOUND + resList.size()
					+ " users with the unique emailid '" + uid + "'");
		}
		return resList.isEmpty() ? null : resList.get(0);
	}



	/**
	 * This method is used to return userModel based on user ID
	 *
	 * @param uid
	 * @return UserModel
	 *
	 */
	@Override
	public UserModel getUserByUid(final String uid)
	{
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.FIND_USER_BY_UID);
			query.addQueryParameter("UID", uid);
			final List<UserModel> resList = getFlexibleSearchService().<UserModel> search(query).getResult();

			if (resList.size() > 1)
			{
				throw new AmbiguousIdentifierException(MarketplacecommerceservicesConstants.FOUND + resList.size()
						+ " users with the unique uid '" + uid + "'");
			}
			return resList.isEmpty() ? null : resList.get(0);
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}
}
