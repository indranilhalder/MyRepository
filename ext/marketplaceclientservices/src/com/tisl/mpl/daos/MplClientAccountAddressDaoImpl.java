/**
 *
 */
package com.tisl.mpl.daos;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.model.StateModel;


/**
 * @author TCS
 *
 */
public class MplClientAccountAddressDaoImpl implements MplClientAccountAddressDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(MplClientAccountAddressDaoImpl.class);

	/*
	 * @return:List<StateModel>
	 * 
	 * @description:Method to get all states
	 */
	@Override
	public List<StateModel> getStates()
	{
		List<StateModel> listOfData = null;
		try
		{
			final String queryString = //
			"SELECT {" + StateModel.PK + "}" //
					+ "FROM {" + StateModel._TYPECODE + "}";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			listOfData = flexibleSearchService.<StateModel> search(query).getResult();
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		if (null != listOfData && listOfData.size() > 0)
		{
			return listOfData;
		}
		else
		{
			return null;
		}
	}

	/*
	 * @param description
	 * 
	 * @return StateModel
	 * 
	 * @description:Method to get state model based on state description
	 */
	@Override
	public StateModel getStateByDescription(final String description)
	{
		List<StateModel> listOfData = null;
		try
		{

			final String queryString = "SELECT {p:" + StateModel.PK + "}" //
					+ "FROM {" + StateModel._TYPECODE + " AS p} "//
					+ "WHERE " + "{p:" + StateModel.DESCRIPTION + "}=?description ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("description", description);

			listOfData = flexibleSearchService.<StateModel> search(query).getResult();

		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		if (null != listOfData && listOfData.size() > 0)
		{
			return listOfData.get(0);
		}
		else
		{
			return null;
		}
	}
	//
}
