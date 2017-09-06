/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewelleryPriceRowModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.PriceBreakupDao;



/**
 * @author TCS
 *
 */
public class PriceBreakupDaoImpl implements PriceBreakupDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.PriceBreakupDao#getPricebreakup(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<JewelleryPriceRowModel> getPricebreakup(final String ussid)
	{
		// YTODO Auto-generated method stub
		try
		{
			final String queryString = "select {jp." + JewelleryPriceRowModel.PK + "} from {" + JewelleryPriceRowModel._TYPECODE
					+ " as jp} where {jp." + JewelleryPriceRowModel.USSID + "}" + "=?ussid";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter("ussid", ussid);

			return flexibleSearchService.<JewelleryPriceRowModel> search(query).getResult();

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

	//
	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.PriceBreakupDao#getJewelInfo(java.lang.String)
	 */
	@Override
	public List<JewelleryInformationModel> getJewelInfo(final String ussid)
	{
		// YTODO Auto-generated method stub
		try
		{
			final String queryString = "select {jp." + JewelleryInformationModel.PK + "} from {"
					+ JewelleryInformationModel._TYPECODE + " as jp} where {jp." + JewelleryInformationModel.USSID + "}" + "=?ussid";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter("ussid", ussid);

			return flexibleSearchService.<JewelleryInformationModel> search(query).getResult();

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
