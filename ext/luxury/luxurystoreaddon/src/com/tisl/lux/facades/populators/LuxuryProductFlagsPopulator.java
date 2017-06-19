/**
 *
 */
package com.tisl.lux.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Date;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.enums.OnlineExclusiveEnum;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author Madhavan
 *
 */
public class LuxuryProductFlagsPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{


	private MplBuyBoxUtility mplBuyBoxUtility;

	private ConfigurationService configurationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		productData.setIsProductNew(Boolean.valueOf(findIsNew(productModel)));
		productData.setIsOfferExisting(Boolean.valueOf(findIsOnSale(productModel)));
		productData.setIsOnlineExclusive(Boolean.valueOf(findIsExclusive(productModel)));

	}


	/**
	 * @param productModel
	 * @return
	 */
	private boolean findIsNew(final ProductModel productModel)
	{
		Date existDate = null;
		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{
			if (seller != null)
			{
				//Find the oldest startDate of the seller
				if (null == existDate && seller.getStartDate() != null)
				{
					existDate = seller.getStartDate();
				}
				else if (seller.getStartDate() != null && existDate.after(seller.getStartDate()))
				{
					existDate = seller.getStartDate();
				}
			}
		}

		if (null != existDate && isNew(existDate))
		{
			return true;
		}
		return false;
	}

	private boolean isNew(final Date existDate)
	{
		boolean newAttr = false;
		if (null != existDate)
		{
			final Date sysDate = new Date();
			final long dayDiff = calculateDays(existDate, sysDate);

			final String validDaysSt = configurationService.getConfiguration().getString("attribute.new.validDays");
			final int validDays = validDaysSt == null ? 0 : Integer.parseInt(validDaysSt);

			if (validDays > dayDiff)
			{
				newAttr = true;
			}
		}

		return newAttr;
	}


	private long calculateDays(final Date dateEarly, final Date dateLater)
	{
		return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * @param productModel
	 * @return
	 */
	private boolean findIsOnSale(final ProductModel productModel)
	{
		double discountedPercent = 0.0;
		final BuyBoxModel buyboxWinner = mplBuyBoxUtility.getLeastPriceBuyBoxModel(productModel);
		if (buyboxWinner != null)
		{

			if (null != buyboxWinner.getSpecialPrice() && buyboxWinner.getSpecialPrice().intValue() > 0)
			{
				discountedPercent = ((buyboxWinner.getMrp().doubleValue() - buyboxWinner.getSpecialPrice().doubleValue()) * 100)
						/ buyboxWinner.getMrp().doubleValue();
			}
			else if (null != buyboxWinner.getPrice() && buyboxWinner.getPrice().intValue() > 0
					&& buyboxWinner.getMrp().intValue() > buyboxWinner.getPrice().intValue())
			{
				discountedPercent = ((buyboxWinner.getMrp().doubleValue() - buyboxWinner.getPrice().doubleValue()) * 100)
						/ buyboxWinner.getMrp().doubleValue();
			}
		}

		return discountedPercent > 0.0;
	}

	/**
	 * @param productModel
	 * @return
	 */
	private boolean findIsExclusive(final ProductModel productModel)
	{
		boolean isOnlineExclusive = false;
		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{
			if (seller != null)
			{
				final OnlineExclusiveEnum onlineExclusiveEnum = seller.getOnlineExclusive();
				if (null != onlineExclusiveEnum)
				{
					isOnlineExclusive = true;
					break;
				}
			}
		}
		return isOnlineExclusive;
	}


	/**
	 * @return the mplBuyBoxUtility
	 */
	public MplBuyBoxUtility getMplBuyBoxUtility()
	{
		return mplBuyBoxUtility;
	}

	/**
	 * @param mplBuyBoxUtility
	 *           the mplBuyBoxUtility to set
	 */
	public void setMplBuyBoxUtility(final MplBuyBoxUtility mplBuyBoxUtility)
	{
		this.mplBuyBoxUtility = mplBuyBoxUtility;
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
