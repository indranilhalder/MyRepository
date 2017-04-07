/**
 *
 */
package com.tisl.mpl.helper;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;


/**
 * @author TCS
 *
 */
public class AddToCartHelper
{
	@Autowired
	private ConfigurationService configurationService;
	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;

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

	public boolean isProductFreebie(final String productCode)
	{
		boolean isProductFreebie = false;
		final double freebiePriceThreshId = Double.parseDouble(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.FREEBIEPRICETHRESHOLD));

		final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode);
		if (buyboxdata != null)
		{
			final PriceData mopPrice = buyboxdata.getPrice();

			if (mopPrice.getValue() != null && mopPrice.getValue().doubleValue() <= freebiePriceThreshId)
			{
				isProductFreebie = true;

			}
		}

		return isProductFreebie;
	}


}
