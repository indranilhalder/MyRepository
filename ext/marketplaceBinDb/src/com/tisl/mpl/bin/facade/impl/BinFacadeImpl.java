/**
 *
 */
package com.tisl.mpl.bin.facade.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tisl.mpl.bin.constants.MarketplaceBinDbConstants;
import com.tisl.mpl.bin.facade.BinFacade;
import com.tisl.mpl.bin.service.BinService;
import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.BinData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
@Component
@Qualifier(MarketplaceBinDbConstants.BINFACADE)
public class BinFacadeImpl implements BinFacade
{
	private static final Logger LOG = Logger.getLogger(BinFacadeImpl.class);
	@Resource
	private BinService binService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	/**
	 * This method takes the bin from Controller and calls the service to check bin details
	 *
	 * @param bin
	 * @return BinModel
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public BinModel performBinCheck(final String bin) throws EtailNonBusinessExceptions
	{
		return getBinService().checkBin(bin);
	}


	/**
	 * This method is used to perform BIN check when customer enters the card number in Payment screen
	 *
	 * @param binNumber
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public BinData binCheck(final String binNumber) throws EtailNonBusinessExceptions
	{

		BinModel bin = new BinModel();
		final BinData binData = new BinData();
		final String ebsDowntime = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.EBS_DOWNTIME);
		try
		{
			bin = performBinCheck(binNumber);
			if (StringUtils.isNotEmpty(ebsDowntime) && ebsDowntime.equalsIgnoreCase("Y"))
			{
				if (null != bin && StringUtils.isNotEmpty(bin.getBankName()) && StringUtils.isNotEmpty(bin.getCardType()))
				{
					binData.setBankName(bin.getBankName());
					binData.setCardType(bin.getCardType());
					binData.setIsValid((StringUtils.isNotEmpty(bin.getIssuingCountry()) && bin.getIssuingCountry().equalsIgnoreCase(
							"India")) ? true : false);
				}
				else
				{
					binData.setIsValid(false);
				}
			}
			else
			{
				if (null != bin && StringUtils.isNotEmpty(bin.getBankName()) && StringUtils.isNotEmpty(bin.getCardType()))
				{
					binData.setBankName(bin.getBankName());
					binData.setCardType(bin.getCardType());
				}
				binData.setIsValid(true);
			}

			//Setting Bank Name in Session for Promotions : Code Change for TISPRO-175
			getSessionService().setAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN,
					(null != bin && StringUtils.isNotEmpty(bin.getBankName())) ? bin.getBankName() : null);


			LOG.debug("From session=====Bank:::::::"
					+ getSessionService().getAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN));

			if (null == getSessionService().getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION))
			{
				final Map<String, Double> paymentInfo = getSessionService().getAttribute(
						MarketplacecommerceservicesConstants.PAYMENTMODE);
				for (final Map.Entry<String, Double> entry : paymentInfo.entrySet())
				{
					if (!(MarketplacecommerceservicesConstants.WALLET.equalsIgnoreCase(entry.getKey())))
					{
						getSessionService().setAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION, entry.getKey());
						break;
					}
				}
			}

		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
			throw new EtailNonBusinessExceptions(e);
		}
		return binData;

	}



	/**
	 * @return the binService
	 */
	public BinService getBinService()
	{
		return binService;
	}

	/**
	 * @param binService
	 *           the binService to set
	 */
	public void setBinService(final BinService binService)
	{
		this.binService = binService;
	}


	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}


	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}



}
