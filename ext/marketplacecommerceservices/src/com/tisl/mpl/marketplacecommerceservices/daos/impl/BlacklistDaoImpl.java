/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BlacklistModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.BlacklistDao;


/**
 * @author TCS
 *
 */
@Component(value = "blacklistDao")
public class BlacklistDaoImpl implements BlacklistDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BlacklistDaoImpl.class.getName());

	/**
	 * It gets the list of blacklisted customers
	 *
	 * @return List<BlacklistModel>
	 *
	 */
	@Override
	public List<BlacklistModel> getAllCustomers()
	{
		final String queryString = "SELECT {" + BlacklistModel.PK + "} FROM {" + BlacklistModel._TYPECODE + "}";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		return getFlexibleSearchService().<BlacklistModel> search(query).getResult();

	}


	/**
	 * It gets the list of blacklisted Mobile Number
	 *
	 * @return List<BlacklistModel>
	 *
	 */
	@Override
	public List<BlacklistModel> getMobileNumber(final String phoneNumber)
	{

		try
		{
			final String queryString = MarketplacecommerceservicesConstants.MOBILEBLACKLIST;

			//forming the flexible search query
			final FlexibleSearchQuery mobileBlackListQuery = new FlexibleSearchQuery(queryString);
			mobileBlackListQuery.addQueryParameter(MarketplacecommerceservicesConstants.MOBILENO, phoneNumber);

			final List<BlacklistModel> mobileBlackList = getFlexibleSearchService().<BlacklistModel> search(mobileBlackListQuery)
					.getResult();


			return mobileBlackList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}


	}


	/**
	 * It checks whether the customer exists in blacklist model or not
	 *
	 * @param emailId
	 * @return boolean
	 *
	 */
	@Override
	public boolean isExistingCustomer(final String emailId)
	{
		final String queryString = //
		"SELECT {b:" + BlacklistModel.PK + "}" //
				+ MarketplacecommerceservicesConstants.QUERYFROM + BlacklistModel._TYPECODE + " AS b} "//
				+ MarketplacecommerceservicesConstants.QUERYWHERE + "{b:" + BlacklistModel.EMAILID
				+ MarketplacecommerceservicesConstants.QUERYEMAIL;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.EMAIL_ID_suffix, emailId);
		if (!(getFlexibleSearchService().<BlacklistModel> search(query).getResult().isEmpty()))
		{
			return true;
		}
		return false;
	}

	/**
	 * It gets the list of all the customers from the blacklist model with the given emailId
	 *
	 * @param emailId
	 * @return List<BlacklistModel>
	 *
	 */
	@Override
	public List<BlacklistModel> getBlacklistedCustomer(final String emailId)
	{
		final String queryString = //
		"SELECT {p:" + BlacklistModel.PK + "}" //
				+ MarketplacecommerceservicesConstants.QUERYFROM + BlacklistModel._TYPECODE + " AS p} "//
				+ MarketplacecommerceservicesConstants.QUERYWHERE + "{p:" + BlacklistModel.EMAILID
				+ MarketplacecommerceservicesConstants.QUERYEMAIL;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.EMAIL_ID_suffix, emailId);
		return getFlexibleSearchService().<BlacklistModel> search(query).getResult();
	}

	/**
	 * It checks whether the customer is valid or not
	 *
	 * @param emailId
	 * @return boolean
	 *
	 */
	@Override
	public boolean isValidUser(final String emailId)
	{
		boolean isExisting = false;
		final String queryString = "SELECT {c:" + CustomerModel.PK + "}" //
				+ MarketplacecommerceservicesConstants.QUERYFROM + CustomerModel._TYPECODE + " As c}"//
				+ MarketplacecommerceservicesConstants.QUERYWHERE + "{c:" + CustomerModel.ORIGINALUID
				+ MarketplacecommerceservicesConstants.QUERYEMAIL;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.EMAIL_ID_suffix, emailId);
		if (!(getFlexibleSearchService().<CustomerModel> search(query).getResult().isEmpty()))
		{
			isExisting = true;
		}
		return isExisting;
	}


	/**
	 * It will take the customer id from the customer model
	 *
	 * @param uid
	 * @return List<CustomerModel>
	 *
	 */
	@Override
	public List<CustomerModel> getId(final String uid)
	{
		final String queryString = "SELECT {cm:" + CustomerModel.PK + "}" //
				+ MarketplacecommerceservicesConstants.QUERYFROM + CustomerModel._TYPECODE + " As cm}"//
				+ MarketplacecommerceservicesConstants.QUERYWHERE + "{cm:" + CustomerModel.ORIGINALUID
				+ MarketplacecommerceservicesConstants.QUERYEMAIL;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.EMAIL_ID_suffix, uid);

		return getFlexibleSearchService().<CustomerModel> search(query).getResult();

	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.BlacklistDao#isBlackListed(de.hybris.platform.core.model.user.
	 * CustomerModel, java.lang.String)
	 */
	@Override
	public boolean isBlackListed(final CustomerModel customer, final String ipAddress)
	{
		// YTODO Auto-generated method stub
		return false;
	}



	/**
	 * This method is used to find a customer blacklisted or not
	 *
	 * @param customerPk
	 * @param emailId
	 * @param phoneNumber
	 * @param ipAddress
	 * @return List<BlacklistModel>
	 */
	@Override
	public List<BlacklistModel> isBlackListedCustomer(final String customerPk, final String emailId, final String phoneNumber,
			final String ipAddress)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.CODBLACKLISTQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery codBlackListQuery = new FlexibleSearchQuery(queryString);
			codBlackListQuery.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERID, customerPk);
			codBlackListQuery.addQueryParameter(MarketplacecommerceservicesConstants.EMAILID, emailId);
			codBlackListQuery.addQueryParameter(MarketplacecommerceservicesConstants.MOBILENO, phoneNumber);
			codBlackListQuery.addQueryParameter(MarketplacecommerceservicesConstants.IPADDRESS, ipAddress);

			final List<BlacklistModel> codBlackList = getFlexibleSearchService().<BlacklistModel> search(codBlackListQuery)
					.getResult();

			return codBlackList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	/**
	 * This Method returns data within a date range for Blacklist Model
	 *
	 * @param :
	 *           startDate
	 * @param :
	 *           endDate
	 */
	@Override
	public List<BlacklistModel> getSpecificCustomers(final Date startDate, final Date endDate)
	{
		final String queryString = //
		"SELECT {bm:" + BlacklistModel.PK + "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + BlacklistModel._TYPECODE + " AS bm} where " + "{bm."
				+ BlacklistModel.CREATIONTIME + "} BETWEEN ?startDate and ?endDate  ";

		LOG.debug("Fetching Black List Details within Range");
		LOG.debug("STARTDATE" + startDate);
		LOG.debug("ENDDATE" + endDate);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("startDate", startDate);
		query.addQueryParameter("endDate", endDate);
		return getFlexibleSearchService().<BlacklistModel> search(query).getResult();
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
