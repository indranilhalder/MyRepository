/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.GenerateOTPDao;


/**
 * @author TCS
 *
 */
public class GenerateOTPDaoImpl implements GenerateOTPDao
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.GenerateOTPDao#generateUID()
	 */

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(GenerateOTPDaoImpl.class);

	/**
	 * @description method is called to fetch the OTP number and type from Database return List
	 */
	@Override
	public List<OTPModel> fetchOTP(final String customerPK, final OTPTypeEnum OTPType)
	{
		try
		{
			// YTODO Auto-generated method stub
			final String queryString = "SELECT {o:" + OTPModel.PK + "} "//
					+ "FROM {" + OTPModel._TYPECODE + " AS o} " + "WHERE ({o:" + OTPModel.CUSTOMERID + "})=?customerPK" + " AND ({o:"
					+ OTPModel.ISVALIDATED + "})='0' AND ({o:" + OTPModel.OTPTYPE + "})=?OTPType" + " ORDER BY {o:"
					+ OTPModel.CREATIONTIME + "} DESC";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("customerPK", customerPK);
			query.addQueryParameter("OTPType", OTPType.getCode());

			LOG.info(query.toString());

			return flexibleSearchService.<OTPModel> search(query).getResult();
		}

		catch (final IllegalArgumentException ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}


	}
}
