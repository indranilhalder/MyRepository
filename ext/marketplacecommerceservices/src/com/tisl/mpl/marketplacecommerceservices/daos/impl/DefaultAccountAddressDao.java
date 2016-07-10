/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.AccountAddressDao;
import com.tisl.mpl.model.StateModel;


/**
 * @author TCS
 *
 */
public class DefaultAccountAddressDao implements AccountAddressDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;


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


	@Override
	public List<StateModel> getStates()
	{
		try
		{
			final String queryString = //
			"SELECT {" + StateModel.PK + "}" //
					+ "FROM {" + StateModel._TYPECODE + "} ORDER BY {" + StateModel.DESCRIPTION + "} ASC";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			final List<StateModel> listOfData = flexibleSearchService.<StateModel> search(query).getResult();
			return listOfData;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}
}
