/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerPriorityReportDAO;


/**
 * @author TCS
 *
 */
public class MplSellerPriorityReportDAOImpl implements MplSellerPriorityReportDAO
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplSellerPriorityReportDAOImpl.class.getName());

	/**
	 * Gets the list of Seller Priority Changed Details with in date renge
	 *
	 * @return List<SavedValuesModel>
	 *
	 */
	@Override
	public List<SavedValuesModel> getSellerPriorityDetails(final Date startDate, final Date endDate)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.SELLERPRIORITYWITHINDATEQUERY;
			final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.SIMPLEDATEFORMATDB);
			final String startDataInput = sdf.format(startDate);
			final String endDataInput = sdf.format(endDate);
			//forming the flexible search query
			final FlexibleSearchQuery changedDataQry = new FlexibleSearchQuery(queryString);
			changedDataQry.addQueryParameter(MarketplacecommerceservicesConstants.START_DATE, startDataInput);
			changedDataQry.addQueryParameter(MarketplacecommerceservicesConstants.END_DATE, endDataInput);
			//	final int abc = 4 / 0;

			LOG.info("**************** getSellerPriorityDetails Query ******************** : changedDataQry : " + changedDataQry);

			final List<SavedValuesModel> savedValue = flexibleSearchService.<SavedValuesModel> search(changedDataQry).getResult();

			LOG.info("**************** getSellerPriorityDetails ******************** : savedValue : " + savedValue);

			return savedValue;

			//forming the flexible search query
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
	 * Gets the list of Seller Priority Changed Details with no date range
	 *
	 * @return List<SavedValuesModel>
	 *
	 */
	@Override
	public List<SavedValuesModel> getAllSellerPriorityDetails()
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.SELLERPRIORITYQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery changedDataQry = new FlexibleSearchQuery(queryString);

			LOG.info("**************** getAllSellerPriorityDetails Query ******************** : changedDataQry : " + changedDataQry);

			final List<SavedValuesModel> savedValue = flexibleSearchService.<SavedValuesModel> search(changedDataQry).getResult();

			LOG.info("**************** getAllSellerPriorityDetails ******************** : savedValue : " + savedValue);

			return savedValue;

			//forming the flexible search query
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
