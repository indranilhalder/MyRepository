/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.EMITermRowModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPaymentDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplNetBankingService;
import com.tisl.mpl.util.MplEMICalculator;
import com.tisl.mpl.wsdto.EMITermRateDataForMobile;


/**
 * @author TCS
 *
 */
public class MplNetBankingServiceImpl implements MplNetBankingService
{
	@Autowired
	private MplPaymentDao mplPaymentDao;

	/**
	 * @description method is called to get the Details of Banks which are available for EMI
	 * @param cartValue
	 * @return EMIBankModel
	 */
	@Override
	public List<EMIBankModel> getEMIBanks(final Double cartValue) throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{

		final List<EMIBankModel> emiBankList = new ArrayList<EMIBankModel>();
		try
		{
			//			if (null != cartValue && null != mplPaymentDao.getEMIBanks(cartValue))
			//			{
			//				emiBankList = mplPaymentDao.getEMIBanks(cartValue);
			//			}
			//			return emiBankList;
			//TISPRO-179
			if (null != cartValue)
			{
				final List<EMIBankModel> emiBankDetailsList = mplPaymentDao.getEMIBanks(cartValue, null);
				if (CollectionUtils.isNotEmpty(emiBankDetailsList))
				{
					emiBankList.addAll(emiBankDetailsList);
				}
			}
			return emiBankList;
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
	 * @description method is called to get the Details of EMI Terms for Banks
	 * @param bank
	 * @param totalAmount
	 * @return EMITermRateDataForMobile
	 */
	@Override
	public List<EMITermRateDataForMobile> getBankTerms(final String bank, final Double totalAmount)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		//final List<EMIBankModel> termList = getMplPaymentDao().getEMIBankTerms(bank);
		final List<EMIBankModel> termList = mplPaymentDao.getEMIBankTerms(bank);
		final List<EMITermRateDataForMobile> emiTermRate = new ArrayList<EMITermRateDataForMobile>();
		try
		{
			if (null != termList)
			{
				for (final EMIBankModel term : termList)
				{
					if (null != term.getEMITermRates())
					{
						for (final EMITermRowModel termRate : term.getEMITermRates())
						{
							final EMITermRateDataForMobile termDatas = new EMITermRateDataForMobile();
							termDatas.setTerm(termRate.getTermInMonths().toString());
							termDatas.setInterestRate(String.format(MarketplacecommerceservicesConstants.FORMATONE,
									termRate.getInterestRate()));
							final Double termInMonths = Double.valueOf(termRate.getTermInMonths().doubleValue());
							final Double interestRatePerMonth = Double.valueOf((termRate.getInterestRate().doubleValue())
									/ MarketplacecommerceservicesConstants.MONTHDENO);
							final Double emi = MplEMICalculator.emiCalculator(termInMonths, interestRatePerMonth, totalAmount);
							termDatas.setMonthlyInstallment(String.format(MarketplacecommerceservicesConstants.FORMAT, emi));
							final Double interestPayable = MplEMICalculator.interestPayable(emi, termInMonths, totalAmount);
							termDatas.setInterestPayable(String.format(MarketplacecommerceservicesConstants.FORMAT, interestPayable));
							emiTermRate.add(termDatas);
						}
					}
					else
					{
						emiTermRate.add(null);
					}

				}

				return emiTermRate;
			}
			else
			{
				return null;
			}
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
		final List<CountryModel> countryList = mplPaymentDao.getCountries();
		final Map<String, String> countryMap = new TreeMap<String, String>();

		if (null != countryList)
		{
			for (final CountryModel country : countryList)
			{
				countryMap.put(country.getName(), country.getIsocode());
			}
		}

		final List<CountryData> duplicateRemovedList = new ArrayList<CountryData>();

		for (final Map.Entry<String, String> entry : countryMap.entrySet())
		{
			final CountryData dataDupRemoved = new CountryData();
			dataDupRemoved.setIsocode(entry.getValue());
			dataDupRemoved.setName(entry.getKey());
			duplicateRemovedList.add(dataDupRemoved);
		}
		return duplicateRemovedList;
	}
}
