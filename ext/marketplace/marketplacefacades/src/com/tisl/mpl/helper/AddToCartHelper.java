/**
 *
 */
package com.tisl.mpl.helper;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tis.mpl.facade.data.ProductValidationData;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
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
	protected static final Logger LOG = Logger.getLogger(AddToCartHelper.class);

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

	/*
	 * public boolean isProductFreebie(final String productCode) { boolean isProductFreebie = false; final double
	 * freebiePriceThreshId = Double.parseDouble(getConfigurationService().getConfiguration().getString(
	 * MarketplacecommerceservicesConstants.FREEBIEPRICETHRESHOLD));
	 * 
	 * final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode); if (buyboxdata != null) { final PriceData
	 * mopPrice = buyboxdata.getPrice();
	 * 
	 * if (mopPrice.getValue() != null && mopPrice.getValue().doubleValue() <= freebiePriceThreshId) { isProductFreebie =
	 * true;
	 * 
	 * } }
	 * 
	 * return isProductFreebie; }
	 */

	/* cart not opening issue --due to ussid mismatch */

	public ProductValidationData isProductValid(final String productCode, final String ussid)
	{

		final ProductValidationData validationData = new ProductValidationData();
		validationData.setValidproduct(Boolean.TRUE);
		validationData.setFreebie(Boolean.FALSE);

		try
		{

			final BuyBoxModel buyboxdata = buyBoxFacade.getpriceForUssid(ussid);
			if (buyboxdata != null)
			{
				if (!buyboxdata.getProduct().equalsIgnoreCase(productCode))
				{
					validationData.setValidproduct(Boolean.FALSE); //product id & ussid not valid
				}

				final Double mopPrice = buyboxdata.getPrice();
				final double freebiePriceThreshId = Double.parseDouble(getConfigurationService().getConfiguration().getString(
						MarketplacecommerceservicesConstants.FREEBIEPRICETHRESHOLD));


				if (mopPrice != null && mopPrice.doubleValue() <= freebiePriceThreshId)
				{
					validationData.setFreebie(Boolean.TRUE); //freebie product

				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("ussid not found : " + e);
			validationData.setValidproduct(Boolean.FALSE); //product id & ussid not valid

		}

		return validationData;


	}


}
