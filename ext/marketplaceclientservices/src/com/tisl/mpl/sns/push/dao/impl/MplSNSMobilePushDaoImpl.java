/**
 *
 */
package com.tisl.mpl.sns.push.dao.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.sns.push.dao.MplSNSMobilePushDao;


/**
 * @author TCS
 *
 */
public class MplSNSMobilePushDaoImpl implements MplSNSMobilePushDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	//	@Autowired
	//	private ModelService modelService;



	private static final Logger LOG = Logger.getLogger(MplSNSMobilePushDaoImpl.class);
	private static final String ERROR_400 = "ERROR_400";

	/**
	 * get customer details for an email id
	 *
	 * @param originalUid
	 * @return CustomerModel
	 */
	@Override
	public CustomerModel getCustomerProfileDetail(final String originalUid) throws ClientEtailNonBusinessExceptions
	{
		try
		{

			final String queryString = //
			"SELECT {c:" + CustomerModel.PK + "}" //
					+ "FROM {" + CustomerModel._TYPECODE + " AS c} "//
					+ "WHERE " + "{c:" + CustomerModel.ORIGINALUID + "}=?code ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("code", originalUid);

			final List<CustomerModel> listOfData = flexibleSearchService.<CustomerModel> search(query).getResult();
			return listOfData.get(0);
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions(ERROR_400);
		}
		catch (final NullPointerException e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions(ERROR_400);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions(ERROR_400);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions(ERROR_400);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.sns.push.dao.MplSNSMobilePushDao#getCustomerProfileDetail(java.lang.String)
	 */
	@Override
	public CustomerModel getCustomerProfileDetailForUid(final String uid) throws ClientEtailNonBusinessExceptions
	{
		try
		{

			final String queryString = //
			"SELECT {c:" + CustomerModel.PK + "}" //
					+ "FROM {" + CustomerModel._TYPECODE + " AS c} "//
					+ "WHERE " + "{c:" + CustomerModel.UID + "}=?code ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("code", uid);

			final List<CustomerModel> listOfData = flexibleSearchService.<CustomerModel> search(query).getResult();
			return listOfData.get(0);
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions(ERROR_400);
		}
		catch (final NullPointerException e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions(ERROR_400);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions(ERROR_400);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions(ERROR_400);
		}
	}
}
