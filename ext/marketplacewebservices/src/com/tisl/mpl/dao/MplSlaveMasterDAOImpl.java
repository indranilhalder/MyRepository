/**
 * 
 */
package com.tisl.mpl.dao;


import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * SlaveMaster DAO implementation class to perform slave related dao operation.
 * 
 * @author TECHOUTS
 * 
 */
public class MplSlaveMasterDAOImpl implements MplSlaveMasterDAO
{

	private static final Logger LOG = Logger.getLogger(MplSlaveMasterDAOImpl.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * Checks the existing slave in DB based on given slaveId.
	 * 
	 * @param slaveId
	 * @return returns POS model if found else null.
	 */
	@Override
	public PointOfServiceModel checkPOSForSlave(final String slaveId)
	{
		PointOfServiceModel posModel;
		LOG.info("**************** Check PointOfService with slaveId ******************** : " + slaveId);

		final String queryString = MarketplacewebservicesConstants.POS_QUERY_FOR_SLAVE;
		try
		{
			//create the flexible search query
			final FlexibleSearchQuery posQuery = new FlexibleSearchQuery(queryString);
			posQuery.addQueryParameter(MarketplacewebservicesConstants.POS_SLAVEID, slaveId);
			final List<PointOfServiceModel> posList = flexibleSearchService.<PointOfServiceModel> search(posQuery).getResult();

			if (!posList.isEmpty())
			{
				//fetching POS from DB using flexible search query
				posModel = posList.get(0);
				return posModel;
			}
		}
		catch (final FlexibleSearchException e)
		{
			LOG.debug("Exception while quering POS for a slaveId: " + slaveId + ": " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.debug("Exception while quering POS for a slaveId: " + slaveId + ": " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			LOG.debug("Exception while quering POS for a slaveId: " + slaveId + ": " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return null;
	}


	/**
	 * This method wrapped POS data for given selllerId and SlaveId
	 * 
	 * @param sellerId
	 * @param SlaveId
	 * @return POS object
	 */
	@Override
	public PointOfServiceModel findPOSForSellerAndSlave(final String sellerId, final String slaveId)
	{
		LOG.debug("in dao findPOSForSellerAndSlave ");
		final String queryString = MarketplacewebservicesConstants.POS_QUERY_FOR_SELLER_AND_SLAVE;
		try
		{
			//create the flexible search query
			LOG.debug("call to commerse db search for stores given sellerId and SlaveId ");
			final FlexibleSearchQuery posQuery = new FlexibleSearchQuery(queryString);
			posQuery.addQueryParameter(MarketplacewebservicesConstants.POS_SLAVEID, slaveId);
			posQuery.addQueryParameter(MarketplacewebservicesConstants.POS_SELLERID, sellerId);
			posQuery.addQueryParameter(MarketplacewebservicesConstants.POS_ACTIVE, MarketplacewebservicesConstants.ACTIVE);
			final List<PointOfServiceModel> posList = flexibleSearchService.<PointOfServiceModel> search(posQuery).getResult();

			if (!posList.isEmpty())
			{
				//fetching POS from DB using flexible search query
				return posList.get(0);
			}
		}
		catch (final FlexibleSearchException e)
		{
			LOG.debug("Exception while quering POS for a slaveId: " + slaveId + "And SellerId " + sellerId + ":" + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.debug("Exception while quering POS for a slaveId: " + slaveId + "And SellerId " + sellerId + ": " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			LOG.debug("Exception while quering POS for a slaveId: " + slaveId + "And SellerId " + sellerId + ": " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return null;
	}

	/**
	 * @author TECH This method retrieves pos model from db for given posname.
	 * @param posName
	 * @return pos model if available else null.
	 * 
	 */
	public PointOfServiceModel findPOSForStoreName(final String posName)
	{
		final String queryString = MarketplacewebservicesConstants.POS_QUERY_FOR_POSNAME;
		try
		{
			//create the flexible search query
			final FlexibleSearchQuery posQuery = new FlexibleSearchQuery(queryString);
			posQuery.addQueryParameter(MarketplacewebservicesConstants.POS_NAME, posName);
			posQuery.addQueryParameter(MarketplacewebservicesConstants.POS_ACTIVE, MarketplacewebservicesConstants.ACTIVE);
			final List<PointOfServiceModel> posList = flexibleSearchService.<PointOfServiceModel> search(posQuery).getResult();

			if (!posList.isEmpty())
			{
				//fetching POS from DB using flexible search query
				return posList.get(0);
			}
		}
		catch (final FlexibleSearchException e)
		{
			LOG.debug("Exception while quering POS for a given POSNAME: " + posName + ":" + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.debug("Exception while quering POS for a given POSNAME: " + posName + ":" + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			LOG.debug("Exception while quering POS for a given POSNAME: " + posName + ":" + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return null;

	}

}
