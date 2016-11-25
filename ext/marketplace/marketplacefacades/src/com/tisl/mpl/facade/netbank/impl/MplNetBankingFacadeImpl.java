/**
 *
 */
package com.tisl.mpl.facade.netbank.impl;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.netbank.MplNetBankingFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplNetBankingService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.wsdto.EMIBankListWsDTO;
import com.tisl.mpl.wsdto.EMIBankWsDTO;
import com.tisl.mpl.wsdto.EMITermRateDataForMobile;


/**
 * @author TCS
 *
 */
public class MplNetBankingFacadeImpl implements MplNetBankingFacade
{
	@Resource(name = "mplNetBankingService")
	private MplNetBankingService mplNetBankingService;
	@Resource(name = "mplPaymentService")
	private MplPaymentService mplPaymentService;
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

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
	public EMIBankListWsDTO getEMIBanks(final Double cartValue) throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{

		List<EMIBankModel> emiBankList = new ArrayList<EMIBankModel>();
		final List<EMIBankWsDTO> emiBankWsListDTO = new ArrayList<EMIBankWsDTO>();
		final EMIBankListWsDTO emiBankListWsDTO = new EMIBankListWsDTO();
		CustomerData customerData = null;
		try
		{
			//emiBankList= mplNetBankingService.getEMIBanks(cartValue);
			//TISPRD-7276
			emiBankList = mplPaymentService.getEMIBanks(cartValue);
			customerData = customerFacade.getCurrentCustomer();
			if (null == customerData)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			else
			{
				final String uid = customerData.getUid();
				if (null == uid && StringUtils.isNotEmpty(uid))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
				}
				else
				{
					try
					{
						if (emiBankList.isEmpty())
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9029);
						}
						else
						{

							for (final EMIBankModel emibanking : emiBankList)
							{
								final EMIBankWsDTO eMIBankWsDTO = new EMIBankWsDTO();

								if (StringUtils.isNotEmpty(emibanking.getName().getBankName()))
								{
									eMIBankWsDTO.setEmiBank(emibanking.getName().getBankName());
								}
								if (StringUtils.isNotEmpty(emibanking.getCode()))
								{
									eMIBankWsDTO.setCode(emibanking.getCode());
								}
								if (StringUtils.isNotEmpty(emibanking.getEmiLowerLimit().toString()))
								{
									eMIBankWsDTO.setEmiLowerLimit(emibanking.getEmiLowerLimit().toString());
								}
								if (StringUtils.isNotEmpty(emibanking.getEmiUpperLimit().toString()))
								{
									eMIBankWsDTO.setEmiUpperLimit(emibanking.getEmiUpperLimit().toString());
								}
								emiBankWsListDTO.add(eMIBankWsDTO);
							}

							//TISEE-929
							final Comparator<EMIBankWsDTO> byName = (final EMIBankWsDTO o1, final EMIBankWsDTO o2) -> o1.getEmiBank()
									.compareTo(o2.getEmiBank());

							Collections.sort(emiBankWsListDTO, byName);
							emiBankListWsDTO.setBankList(emiBankWsListDTO);
							emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						}

					}

					catch (final EtailNonBusinessExceptions e)
					{
						//						ExceptionUtil.etailNonBusinessExceptionHandler(e);
						//						if (null != e.getErrorMessage())
						//						{
						//							emiBankListWsDTO.setError(e.getErrorMessage());
						//						}
						//						emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
						throw e;
					}
					catch (final EtailBusinessExceptions e)
					{
						//						ExceptionUtil.etailBusinessExceptionHandler(e, null);
						//						if (null != e.getErrorMessage())
						//						{
						//							emiBankListWsDTO.setError(e.getErrorMessage());
						//						}
						//						emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
						throw e;
					}
				}
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
		return emiBankListWsDTO;

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
