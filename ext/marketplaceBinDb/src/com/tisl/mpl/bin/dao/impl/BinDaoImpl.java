/**
 *
 */
package com.tisl.mpl.bin.dao.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tisl.mpl.bin.constants.MarketplaceBinDbConstants;
import com.tisl.mpl.bin.dao.BinDao;
import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
@Component
@Qualifier(MarketplaceBinDbConstants.BINDAO)
public class BinDaoImpl implements BinDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;


	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(MarketplacecommerceservicesConstants.CONFIGURATION_SER,
				ConfigurationService.class);
	}


	/**
	 * This method fetches the details wrt a bin
	 *
	 * @param bin
	 * @return BinModel
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public BinModel fetchBankFromBin(final String bin) throws EtailNonBusinessExceptions
	{
		BinModel binModel = null;
		final String binVersion = getConfigurationService().getConfiguration().getString(
				MarketplaceBinDbConstants.BIN_PRESENT_VERSION, MarketplacecommerceservicesConstants.EMPTY);
		try
		{
			final String queryString = MarketplaceBinDbConstants.BANKFORBINQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery bankQuery = new FlexibleSearchQuery(queryString);
			bankQuery.addQueryParameter(MarketplaceBinDbConstants.BINNO, bin);
			bankQuery.addQueryParameter(MarketplaceBinDbConstants.BIN_VERSION, binVersion);
			final List<BinModel> binList = getFlexibleSearchService().<BinModel> search(bankQuery).getResult();
			if (CollectionUtils.isNotEmpty(binList))
			{
				//fetching BIN data from DB using flexible search query
				binModel = binList.get(0);
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
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return binModel;
	}


	//Getters and setters

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


	/**
	 * @description : Generate Bank Data for .csv
	 * @return List<String>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<String> getBankDetails() throws EtailNonBusinessExceptions
	{
		List<String> bankList = new ArrayList<String>();
		try
		{
			final String queryString = MarketplaceBinDbConstants.BANKDATAQUERY;

			final FlexibleSearchQuery bankQuery = new FlexibleSearchQuery(queryString);
			final List resultClassList = new ArrayList();
			resultClassList.add(String.class);
			bankQuery.setResultClassList(resultClassList);
			bankList = getFlexibleSearchService().<String> search(bankQuery).getResult();

		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return bankList;
	}
}
