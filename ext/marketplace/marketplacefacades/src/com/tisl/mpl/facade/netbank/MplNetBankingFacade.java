/**
 *
 */
package com.tisl.mpl.facade.netbank;

import de.hybris.platform.commercefacades.user.data.CountryData;

import java.util.List;

import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.wsdto.EMITermRateDataForMobile;


/**
 * @author TCS
 *
 */
public interface MplNetBankingFacade
{
	public List<EMIBankModel> getEMIBanks(final Double cartValue);

	public List<EMITermRateDataForMobile> getBankTerms(final String bank, final Double totalAmount);

	public List<CountryData> getCountries();
}
