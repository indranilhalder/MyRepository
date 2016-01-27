/**
 *
 */
package com.tisl.mpl.facade.netbank.impl;

import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.netbank.MplNetBankingFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplNetBankingService;
import com.tisl.mpl.wsdto.EMITermRateDataForMobile;


/**
 * @author TCS
 *
 */
public class MplNetBankingFacadeImpl implements MplNetBankingFacade
{
	@Resource(name = "mplNetBankingService")
	private MplNetBankingService mplNetBankingService;

	/**
	 * @return the mplNetBankingService
	 */
	public MplNetBankingService getMplNetBankingService()
	{
		return mplNetBankingService;
	}

	/**
	 * @param mplNetBankingService
	 *           the mplNetBankingService to set
	 */
	public void setMplNetBankingService(final MplNetBankingService mplNetBankingService)
	{
		this.mplNetBankingService = mplNetBankingService;
	}

	/**
	 * @description method is called to get the Details of Banks which are available for EMI
	 * @param cartValue
	 * @return EMIBankModel
	 */
	@Override
	public List<EMIBankModel> getEMIBanks(final Double cartValue) throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		List<EMIBankModel> emiBankList = new ArrayList<EMIBankModel>();
		try
		{
			emiBankList = mplNetBankingService.getEMIBanks(cartValue);
		}
		catch (final EtailBusinessExceptions businessException)
		{
			throw businessException;
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9013);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return emiBankList;

	}

	/**
	 * @description method is called to get the Details of EMI Terms for Banks
	 * @param bank
	 * @param totalAmount
	 * @return EMITermRateDataForMobile
	 */
	@Override
	public List<EMITermRateDataForMobile> getBankTerms(final String bank, final Double totalAmount)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		List<EMITermRateDataForMobile> emiTermRate = new ArrayList<EMITermRateDataForMobile>();
		emiTermRate = mplNetBankingService.getBankTerms(bank, totalAmount);
		try
		{
			if (null != emiTermRate)
			{
				return emiTermRate;
			}
			return emiTermRate;
		}

		catch (final EtailBusinessExceptions businessException)
		{
			throw businessException;
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9013);
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

	/**
	 * @description method is called to get the List of Countries available for billing
	 * @return CountryData
	 */
	@Override
	public List<CountryData> getCountries()
	{
		return mplNetBankingService.getCountries();
	}
}
