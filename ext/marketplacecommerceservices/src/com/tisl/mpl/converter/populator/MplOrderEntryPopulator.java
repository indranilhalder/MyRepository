package com.tisl.mpl.converter.populator;

import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commercefacades.order.data.ImeiDetailData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.strategies.ModifiableChecker;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;


/**
 * Converter for converting order / cart entries
 */
public class MplOrderEntryPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{
	private Converter<ProductModel, ProductData> productConverter;
	private Converter<MplZoneDeliveryModeValueModel, MarketplaceDeliveryModeData> deliveryModeConverter;
	private PriceDataFactory priceDataFactory;
	private ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker;
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;
	private Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentConverter;

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	protected Converter<MplZoneDeliveryModeValueModel, MarketplaceDeliveryModeData> getDeliveryModeConverter()
	{
		return deliveryModeConverter;
	}

	@Required
	public void setDeliveryModeConverter(
			final Converter<MplZoneDeliveryModeValueModel, MarketplaceDeliveryModeData> deliveryModeConverter)
	{
		this.deliveryModeConverter = deliveryModeConverter;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	protected ModifiableChecker<AbstractOrderEntryModel> getEntryOrderChecker()
	{
		return entryOrderChecker;
	}

	@Required
	public void setEntryOrderChecker(final ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker)
	{
		this.entryOrderChecker = entryOrderChecker;
	}

	protected Converter<PointOfServiceModel, PointOfServiceData> getPointOfServiceConverter()
	{
		return pointOfServiceConverter;
	}

	@Required
	public void setPointOfServiceConverter(final Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter)
	{
		this.pointOfServiceConverter = pointOfServiceConverter;
	}



	/**
	 * @return the consignmentConverter
	 */
	public Converter<ConsignmentEntryModel, ConsignmentEntryData> getConsignmentConverter()
	{
		return consignmentConverter;
	}

	/**
	 * @param consignmentConverter
	 *           the consignmentConverter to set
	 */
	public void setConsignmentConverter(final Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentConverter)
	{
		this.consignmentConverter = consignmentConverter;
	}



	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (null != source.getProduct())
		{
			addCommon(source, target);
			addProduct(source, target);
			addTotals(source, target);
			addDeliveryMode(source, target);
			addDeliveryModes(source, target);
			addConsignmentValue(source, target);
			addPromotionValue(source, target);
			addImeiDetails(source, target);
		}
	}


	/**
	 * @param source
	 * @param target
	 */
	private void addImeiDetails(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		if (null != source.getImeiDetail())
		{
			final List<String> identifiers = new ArrayList<String>();
			final ImeiDetailData imediaData = new ImeiDetailData();
			if (source.getImeiDetail().getSerialNum() != null)
			{
				imediaData.setSerialNum(source.getImeiDetail().getSerialNum());
			}
			if (source.getImeiDetail().getIdentifiers() != null && !source.getImeiDetail().getIdentifiers().isEmpty())
			{
				for (final String identifier : source.getImeiDetail().getIdentifiers())
				{
					identifiers.add(identifier);
				}
				imediaData.setIdentifiers(identifiers);
			}

			target.setImeiDetails(imediaData);
		}

	}

	/**
	 * @param source
	 * @param target
	 */
	private void addPromotionValue(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		if (null != source)
		{
			if (null != source.getIsBOGOapplied())
			{
				target.setIsBOGOapplied(source.getIsBOGOapplied().booleanValue());
			}
			if (null != source.getGiveAway())
			{
				target.setGiveAway(source.getGiveAway().booleanValue());
			}
			if (null != source.getAssociatedItems() && !source.getAssociatedItems().isEmpty())
			{
				final List<String> associatedProducts = new ArrayList<String>();
				for (final String associatedItem : source.getAssociatedItems())
				{
					associatedProducts.add(associatedItem);
				}
				target.setAssociatedItems(associatedProducts);
			}
			if (null != source.getCartLevelDisc() && source.getCartLevelDisc().doubleValue() != 0.0)
			{
				target.setCartLevelDisc(createPrice(source, source.getCartLevelDisc()));
			}
			if (null != source.getTotalProductLevelDisc() && source.getTotalProductLevelDisc().doubleValue() != 0.0)
			{
				target.setProductLevelDisc(createPrice(source, source.getTotalProductLevelDisc()));
			}
			if (null != source.getNetSellingPrice() && source.getNetSellingPrice().doubleValue() != 0.0)
			{
				target.setNetSellingPrice(createPrice(source, source.getNetSellingPrice()));
			}
			if (null != source.getNetAmountAfterAllDisc() && source.getNetAmountAfterAllDisc().doubleValue() != 0.0)
			{
				target.setAmountAfterAllDisc(createPrice(source, source.getNetAmountAfterAllDisc()));
			}
			if (null != source.getCartLevelPercentageDisc() && source.getCartLevelPercentageDisc().doubleValue() != 0.0)
			{

				target.setCartLevelPercentage(Integer.toString(source.getCartLevelPercentageDisc().intValue()));
			}
			if (null != source.getProdLevelPercentageDisc() && source.getProdLevelPercentageDisc().doubleValue() != 0.0)
			{
				target.setProdLevelPercentage(Integer.toString(source.getProdLevelPercentageDisc().intValue()));
			}
			if (null != source.getTotalSalePrice() && source.getTotalSalePrice().doubleValue() != 0.0)
			{
				target.setTotalSalePrice(createPrice(source, source.getTotalSalePrice()));
			}

			if (null != source.getFreeCount())
			{
				target.setFreeCount(source.getFreeCount());
			}
			if (null != source.getQualifyingCount())
			{
				target.setQualifyingCount(source.getQualifyingCount());
			}


		}

	}

	/**
	 * @param orderEntry
	 * @param target
	 */
	private void addConsignmentValue(final AbstractOrderEntryModel orderEntry, final OrderEntryData target)
	{
		if (null != orderEntry)
		{
			target.setOrderLineId(orderEntry.getOrderLineId());
			//For testing purpose
			//target.setOrderLineId("1234567890");
			target.setTransactionId(orderEntry.getTransactionID());

			if (null != orderEntry.getConsignmentEntries())
			{
				for (final ConsignmentEntryModel consignmentEntry : orderEntry.getConsignmentEntries())
				{

					target.setConsignment(prepareConsignment(consignmentEntry.getConsignment()));
				}
			}
		}
	}

	private ConsignmentData prepareConsignment(final ConsignmentModel consignment)
	{
		final ConsignmentData consignmentData = new ConsignmentData();
		consignmentData.setCode(consignment.getCode());
		consignmentData.setStatus(consignment.getStatus());
		consignmentData.setTrackingID(consignment.getTrackingID());
		return consignmentData;
	}

	/**
	 * @param orderEntry
	 * @param target
	 */
	private void addDeliveryModes(final AbstractOrderEntryModel orderEntry, final OrderEntryData target)
	{
		if (orderEntry.getMplZoneDeliveryModeValue() != null)
		{
			final List<MarketplaceDeliveryModeData> deliveryModesData = new ArrayList<MarketplaceDeliveryModeData>();
			for (final MplZoneDeliveryModeValueModel deliveryMode : orderEntry.getMplZoneDeliveryModeValue())
			{
				final MarketplaceDeliveryModeData deliveryModeData = getDeliveryModeConverter().convert(deliveryMode);
				deliveryModesData.add(deliveryModeData);
			}
			target.setDeliveryModes(deliveryModesData);
			if (orderEntry.getCurrDelCharge().intValue() > 0)
			{
				target.setCurrDelCharge(createPrice(orderEntry, orderEntry.getCurrDelCharge()));
			}
			else if (null != orderEntry.getRefundedDeliveryChargeAmt() && orderEntry.getRefundedDeliveryChargeAmt().intValue() > 0)
			{
				target.setCurrDelCharge(createPrice(orderEntry, orderEntry.getRefundedDeliveryChargeAmt()));
			}
			else
			{
				target.setCurrDelCharge(createPrice(orderEntry, orderEntry.getCurrDelCharge()));
			}
		}
	}

	protected void addDeliveryMode(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (orderEntry.getMplDeliveryMode() != null)
		{
			entry.setMplDeliveryMode(getDeliveryModeConverter().convert(orderEntry.getMplDeliveryMode()));

		}

		if (orderEntry.getDeliveryPointOfService() != null)
		{
			entry.setDeliveryPointOfService(getPointOfServiceConverter().convert(orderEntry.getDeliveryPointOfService()));
		}
		if (orderEntry.getExpectedDeliveryDate() != null)
		{
			entry.setExpectedDeliveryDate(orderEntry.getExpectedDeliveryDate());
		}
	}

	protected void addCommon(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		entry.setEntryNumber(orderEntry.getEntryNumber());
		entry.setQuantity(orderEntry.getQuantity());
		entry.setSelectedUssid(orderEntry.getSelectedUSSID());
		adjustUpdateable(entry, orderEntry);
	}


	protected void adjustUpdateable(final OrderEntryData entry, final AbstractOrderEntryModel entryToUpdate)
	{
		entry.setUpdateable(getEntryOrderChecker().canModify(entryToUpdate));
	}

	protected void addProduct(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		entry.setProduct(getProductConverter().convert(orderEntry.getProduct()));
	}

	protected void addTotals(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (orderEntry.getBasePrice() != null)
		{
			entry.setBasePrice(createPrice(orderEntry, orderEntry.getBasePrice()));
		}
		if (orderEntry.getTotalPrice() != null)
		{
			entry.setTotalPrice(createPrice(orderEntry, orderEntry.getTotalPrice()));
		}
	}

	protected PriceData createPrice(final AbstractOrderEntryModel orderEntry, final Double val)
	{
		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(val.doubleValue()),
				orderEntry.getOrder().getCurrency());
	}
}
