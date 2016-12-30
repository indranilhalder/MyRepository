/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao;


/**
 * @author TCS
 *
 */
public class MplJewelleryDaoImpl implements MplJewelleryDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao#getJewelleryUssid(java.lang.String)
	 */
	@Override
	public List<JewelleryInformationModel> getJewelleryUssid(final String productCode)
	{
		try
		{
			final String classAttrquery = "select {pk} from {JewelleryInformation} " + "where {productCode} " + "		IN ('"
					+ productCode + "')}})";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(classAttrquery);
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
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao#getJewelleryInfoByUssid(java.lang.String)
	 */
	@Override
	public List<JewelleryInformationModel> getJewelleryInfoByUssid(final String ussid)
	{
		try
		{
			final String classAttrquery = "select {pk} from {JewelleryInformation} " + "where {ussid} " + "		IN ('" + ussid + "')";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(classAttrquery);
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
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

}