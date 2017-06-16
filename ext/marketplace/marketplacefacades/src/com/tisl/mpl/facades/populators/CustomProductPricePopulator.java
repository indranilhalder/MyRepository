/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class CustomProductPricePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{

	//protected static final Logger LOG = Logger.getLogger(CustomProductBasicPopulator.class);
	private CommercePriceService commercePriceService;
	private PriceDataFactory priceDataFactory;

	protected CommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	@Resource
	private MplPriceRowService mplPriceRowService;

	/**
	 * @return the mplPriceRowService
	 */
	public MplPriceRowService getMplPriceRowService()
	{
		return mplPriceRowService;
	}

	/**
	 * @param mplPriceRowService
	 *           the mplPriceRowService to set
	 */
	public void setMplPriceRowService(final MplPriceRowService mplPriceRowService)
	{
		this.mplPriceRowService = mplPriceRowService;
	}

	@Required
	public void setCommercePriceService(final CommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * @description method is to populate price details from different sellers to be displayed in pdp
	 * @param productModel
	 * @param productData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */
	//for Jewellery
	@Resource
	private BuyBoxService buyBoxService;


	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException,
			EtailNonBusinessExceptions
	{
		if (null != productData.getSeller())
		{
			final List<SellerInformationData> sellerDataList = productData.getSeller();
			final Date sysDate = new Date();
			String sellerArticleSKUs = null;
			final StringBuilder sellerArticleSKUsBuilder = new StringBuilder();

			//changes for Jewellery pincode service in pdp
			if (productModel.getProductCategoryType().equalsIgnoreCase(MarketplaceFacadesConstants.PRODUCT_TYPE))
			{
				List<BuyBoxModel> buyboxModelListAll = new ArrayList<BuyBoxModel>();
				String sellerArticleSKU = null;
				for (final SellerInformationData sellerDList : sellerDataList)
				{
					sellerArticleSKU = fetchVariantUSSID(sellerDList);
					buyboxModelListAll = buyBoxService.buyboxPriceForJewellery(sellerDList.getUssid());
					sellerArticleSKU = buyboxModelListAll.get(0).getSellerArticleSKU();
					sellerArticleSKUsBuilder.append('\'').append(sellerArticleSKU).append('\'').append(',');
					sellerArticleSKUs = sellerArticleSKUsBuilder.toString();
				}
			}
			//end
			else
			{
				sellerArticleSKUs = GenericUtilityMethods.getSellersUSSIDs(sellerDataList);
			}


			if (null != sellerArticleSKUs && sellerArticleSKUs.length() > 0)
			{
				sellerArticleSKUs = sellerArticleSKUs.substring(0, sellerArticleSKUs.length() - 1);
				final Map<String, PriceRowModel> mopMap = mplPriceRowService.getAllPriceRowDetail(sellerArticleSKUs);
				for (final SellerInformationData sellerInformationData : sellerDataList)
				{
					PriceRowModel priceRowModel = null;
					if (productModel.getProductCategoryType().equalsIgnoreCase(MarketplaceFacadesConstants.PRODUCT_TYPE))
					{
						final String sellerArticleSKU = fetchVariantUSSID(sellerInformationData);
						priceRowModel = mopMap.get(sellerArticleSKU);
					}
					else
					{
						priceRowModel = mopMap.get(sellerInformationData.getUssid());
					}

					if (null != priceRowModel)
					{
						//set MOP
						final Double mopPrice = priceRowModel.getPrice();
						if (null != priceRowModel.getPrice())
						{
							sellerInformationData.setMopPrice(formPriceData(mopPrice));
						}

						final Double mrpPrice = priceRowModel.getMrp();
						if (null != priceRowModel.getMrp())
						{
							sellerInformationData.setMrpPrice(formPriceData(mrpPrice));
						}
						//set Special Price
						final Double specialPrice = priceRowModel.getSpecialPrice();
						if (null != specialPrice && specialPrice.doubleValue() > 0.0)
						{
							final boolean status = GenericUtilityMethods.compareDate(priceRowModel.getPromotionStartDate(),
									priceRowModel.getPromotionEndDate(), sysDate);
							if (status)
							{
								sellerInformationData.setSpPrice(formPriceData(specialPrice));
							}
							else
							{
								sellerInformationData.setSpPrice(formPriceData(new Double(0.0)));
							}
						}
					}

				}
				productData.setSeller(sellerDataList);
			}
		}
	}



	/**
	 * Converting variantussid from pcmussid
	 *
	 * @param sellerDataList
	 * @return variantussid
	 */
	//added for jewellery
	public String fetchVariantUSSID(final SellerInformationData sellerDataList)
	{
		List<BuyBoxModel> buyboxModelListAll = new ArrayList<BuyBoxModel>();
		String sellerArticleSKU = null;
		buyboxModelListAll = buyBoxService.buyboxPriceForJewellery(sellerDataList.getUssid());
		sellerArticleSKU = buyboxModelListAll.get(0).getSellerArticleSKU();
		return sellerArticleSKU;
	}

	//end

	/**
	 * Converting datatype of price
	 *
	 * @param price
	 * @return pData
	 */

	public PriceData formPriceData(final Double price)
	{
		final PriceData priceData = new PriceData();
		priceData.setPriceType(PriceDataType.BUY);
		priceData.setValue(new BigDecimal(price.doubleValue()));
		priceData.setCurrencyIso(MarketplaceFacadesConstants.INR);
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(priceData.getCurrencyIso());
		currency.setSymbol(priceData.getCurrencyIso());
		final PriceData pData = priceDataFactory.create(PriceDataType.BUY, priceData.getValue(), currency);
		return pData;
	}
}
