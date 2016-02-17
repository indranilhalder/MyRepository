/**
 *
 */
package com.tisl.mpl.facade.converter;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;


/**
 * @author 181725
 *
 */
public class MplProductConverter
{

	private BuyBoxFacade buyBoxFacade;

	/**
	 * @return the buyBoxFacade
	 */
	public BuyBoxFacade getBuyBoxFacade()
	{
		return buyBoxFacade;
	}

	/**
	 * @param buyBoxFacade
	 *           the buyBoxFacade to set
	 */
	public void setBuyBoxFacade(final BuyBoxFacade buyBoxFacade)
	{
		this.buyBoxFacade = buyBoxFacade;
	}

	private static final Logger LOG = Logger.getLogger(MplProductConverter.class);

	public <SOURCE, TARGET> List<TARGET> convertAll(final Collection<? extends SOURCE> sourceList,
			final Converter<SOURCE, TARGET> converter)
	{
		Assert.notNull(converter);

		if (sourceList == null || sourceList.isEmpty())
		{
			return Collections.emptyList();
		}

		final List<TARGET> result = new ArrayList<TARGET>(sourceList.size());

		for (final SOURCE source : sourceList)
		{
			final ProductModel productModel = (ProductModel) source;
			try
			{

				final BuyBoxData buyboxData = buyBoxFacade.buyboxPrice(productModel.getCode());
				if (buyboxData != null && buyboxData.getPrice() != null && buyboxData.getMrp() != null)
				{
					result.add(converter.convert(source));
				}
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + "Buybox price is not available for the product");
			}
			catch (final Exception e)
			{
				LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + "Exception while calling buybox for the product");
			}

		}
		return result;
	}
}
