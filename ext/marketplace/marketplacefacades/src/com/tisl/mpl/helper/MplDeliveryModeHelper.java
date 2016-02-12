/**
 *
 */
package com.tisl.mpl.helper;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.enums.ClickAndCollectEnum;
import com.tisl.mpl.core.enums.ExpressDeliveryEnum;
import com.tisl.mpl.core.enums.HomeDeliveryEnum;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;


/**
 * @author TCS
 *
 */
public class MplDeliveryModeHelper
{
	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;

	public MplDeliveryCostService getMplDeliveryCostService()
	{
		return mplDeliveryCostService;
	}

	public void setMplDeliveryCostService(final MplDeliveryCostService mplDeliveryCostService)
	{
		this.mplDeliveryCostService = mplDeliveryCostService;
	}

	private PriceDataFactory priceDataFactory;

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
	 * Populating different delivery modes
	 *
	 * @param rich
	 * @param skuid
	 * @return-MarketplaceDeliveryModeData
	 */
	public List<MarketplaceDeliveryModeData> getDeliveryModeLlist(final RichAttributeModel rich, final String skuid)
	{
		final List<MarketplaceDeliveryModeData> deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
		if (null != rich.getHomeDelivery() && rich.getHomeDelivery().equals(HomeDeliveryEnum.YES))
		{
			final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = getMplDeliveryCostService().getDeliveryCost(
					MarketplaceFacadesConstants.HD, MarketplaceFacadesConstants.INR, skuid);
			//Populating Delivery Modes for each USSID
			if (mplZoneDeliveryModeValueModel != null)
			{
				final PriceData priceData = formPriceData(mplZoneDeliveryModeValueModel.getValue());
				deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
				deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
				deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
				deliveryModeData.setSellerArticleSKU(skuid);
				deliveryModeData.setDeliveryCost(priceData);
				deliveryModeDataList.add(deliveryModeData);
			}

		}
		if (null != rich.getClickAndCollect() && rich.getClickAndCollect().equals(ClickAndCollectEnum.YES))
		{
			final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = getMplDeliveryCostService().getDeliveryCost(
					MarketplaceFacadesConstants.C_C, MarketplaceFacadesConstants.INR, skuid);
			if (mplZoneDeliveryModeValueModel != null)
			{
				final PriceData priceData = formPriceData(mplZoneDeliveryModeValueModel.getValue());
				deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
				deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
				deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
				deliveryModeData.setSellerArticleSKU(mplZoneDeliveryModeValueModel.getSellerArticleSKU());
				deliveryModeData.setDeliveryCost(priceData);
				deliveryModeDataList.add(deliveryModeData);
			}
		}
		if (null != rich.getExpressDelivery() && rich.getExpressDelivery().equals(ExpressDeliveryEnum.YES))
		{
			final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = getMplDeliveryCostService().getDeliveryCost(
					MarketplaceFacadesConstants.EXPRESS, MarketplaceFacadesConstants.INR, skuid);
			if (mplZoneDeliveryModeValueModel != null)
			{
				final PriceData priceData = formPriceData(mplZoneDeliveryModeValueModel.getValue());
				deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
				deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
				deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
				deliveryModeData.setSellerArticleSKU(skuid);
				deliveryModeData.setDeliveryCost(priceData);
				deliveryModeDataList.add(deliveryModeData);
			}
		}
		return deliveryModeDataList;
	}

	private PriceData formPriceData(final Double price)
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
