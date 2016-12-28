/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.FreebieDetailModel;
import com.tisl.mpl.core.model.ProductFreebieDetailModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.ProductOfferDetailDao;


/**
 * @author TCS
 *
 */
//Added for displaying Non HMC configurable offer messages , TPR-589
public class ProductOfferDetailDaoImpl extends AbstractItemDao implements ProductOfferDetailDao
{

	private final static Logger LOG = Logger.getLogger(ProductOfferDetailDaoImpl.class.getName());

	/**
	 * @Description This method is used to fetch message from OfferDetail for a product
	 * @param productCode
	 * @return message
	 */
	@Override
	public SearchResult<List<Object>> showOfferMessage(final String productCode)
	{
		try
		{

			final String queryString = MarketplacecommerceservicesConstants.PRODUCTOFFERDETMSGQUERY;
			LOG.debug("queryString: " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter(MarketplacecommerceservicesConstants.OFFERPRODUCTID, productCode);

			query.addQueryParameter(MarketplacecommerceservicesConstants.SYSDATE, new Date());

			query.setResultClassList(Arrays.asList(String.class, String.class, String.class, String.class, String.class));
			final SearchResult<List<Object>> result = search(query);
			if (null != result)
			{
				return result;
			}
			return null;
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

	//update the message for Freebie product TPR-1754
	/**
	 * @Description Added for displaying freebie messages other than default freebie message
	 * @param ussId
	 *           return freebie message
	 */
	@Override
	public SearchResult<List<Object>> showFreebieMessage(final String ussId)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.PRODUCTFREEBEEDETMSGQUERY;
			LOG.debug("queryStringg: " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter(MarketplacecommerceservicesConstants.FREEBIEUSSID, ussId);

			query.setResultClassList(Arrays.asList(ProductFreebieDetailModel.class, FreebieDetailModel.class));

			final SearchResult<List<Object>> result = search(query);
			if (null != result)
			{
				return result;
			}
			return null;
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
