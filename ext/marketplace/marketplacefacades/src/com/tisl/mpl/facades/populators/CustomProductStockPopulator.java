/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplStockService;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class CustomProductStockPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{
	//protected static final Logger LOG = Logger.getLogger(CustomProductBasicPopulator.class);
	private Converter<ProductModel, StockData> stockConverter;

	protected Converter<ProductModel, StockData> getStockConverter()
	{
		return stockConverter;
	}

	@Resource
	private MplStockService mplStockService;



	/**
	 * @return the mplStockService
	 */
	public MplStockService getMplStockService()
	{
		return mplStockService;
	}

	/**
	 * @param mplStockService
	 *           the mplStockService to set
	 */
	public void setMplStockService(final MplStockService mplStockService)
	{
		this.mplStockService = mplStockService;
	}

	@Required
	public void setStockConverter(final Converter<ProductModel, StockData> stockConverter)
	{
		this.stockConverter = stockConverter;
	}


	/**
	 * @description method is to populate stock details from different sellers to be displayed in pdp
	 * @param source
	 * @param target
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException, EtailNonBusinessExceptions
	{

		if (null != target.getSeller())
		{
			final List<SellerInformationData> sellerDataList = target.getSeller();
			String sellerArticleSKUs = GenericUtilityMethods.getSellersUSSIDs(sellerDataList);
			if (null != sellerArticleSKUs && sellerArticleSKUs.length() > 0)
			{
				sellerArticleSKUs = sellerArticleSKUs.substring(0, sellerArticleSKUs.length() - 1);
				final Map<String, Integer> availableStockMap = mplStockService.getAllStockLevelDetail(sellerArticleSKUs);
				for (final SellerInformationData sellerInformationData : sellerDataList)
				{
					sellerInformationData.setAvailableStock(availableStockMap.get(sellerInformationData.getUssid()));
				}
				target.setSeller(sellerDataList);
			}
		}
	}
}
