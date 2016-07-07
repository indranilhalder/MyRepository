/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeDetailsDao;


/**
 * @author Dileep
 *
 */
public class MplPincodeDetailsDaoImpl implements MplPincodeDetailsDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(MplPincodeDetailsDaoImpl.class);


	@Override
	public PincodeModel getPincodeModel(final String pincode)
	{
		try
		{
			final String query = "select {" + PincodeModel.PK + "} from {" + PincodeModel._TYPECODE + "} where {"
					+ PincodeModel.PINCODE + "}=?pincode";
			final FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(query);
			flexQuery.addQueryParameter("pincode", pincode);
			LOG.info("Query String ::::::::: " + query);
			final SearchResult<PincodeModel> result = flexibleSearchService.search(flexQuery);
			if (null != result && null != result.getResult() && !result.getResult().isEmpty())
			{
				return result.getResult().get(0);
			}
			throw new EtailBusinessExceptions();

		}
		catch (final FlexibleSearchException exception)
		{
			throw new EtailNonBusinessExceptions(exception, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}
}
