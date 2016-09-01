/**
 *
 */
package com.tisl.mpl.standardizationfactory;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tils.mpl.media.impl.MplMediaDaoImpl;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.SizesystemModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public class StandardizationDaoImpl implements StandardizationDao
{
	private static final Logger LOG = Logger.getLogger(MplMediaDaoImpl.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;


	@Override
	public Double findConversionUnit(final String to, final String from)

	{

		try
		{
			final StringBuffer queryString = new StringBuffer(500);

			queryString.append("select {ss.PK} from {Sizesystem as ss} where {ss.from}=?from and {ss.to}=?to");


			LOG.debug("Find SizeSystem::" + queryString.toString());
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
			query.addQueryParameter("from", from.toUpperCase());
			query.addQueryParameter("to", to.toUpperCase());
			if (flexibleSearchService.<SizesystemModel> search(query).getResult().size() <= 0)
			{
				return null;
			}
			else
			{
				return flexibleSearchService.<SizesystemModel> search(query).getResult().get(0).getConversionUnit();
			}

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


	@Override
	public List<SizesystemModel> getStandardValue(final String propertyName)
	{
		try
		{
			final StringBuffer queryString = new StringBuffer(500);

			queryString.append("select {ss.PK} from {Sizesystem as ss} where {ss.propertyname}=?propertyName");


			LOG.debug("Find SizeSystem::" + queryString.toString());
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());

			query.addQueryParameter("propertyName", propertyName);
			if (flexibleSearchService.<SizesystemModel> search(query).getResult().size() <= 0)
			{
				return null;
			}
			else
			{
				return flexibleSearchService.<SizesystemModel> search(query).getResult();
			}

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