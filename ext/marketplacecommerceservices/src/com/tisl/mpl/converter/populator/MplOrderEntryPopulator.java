package com.tisl.mpl.converter.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commercefacades.order.data.ImeiDetailData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.strategies.ModifiableChecker;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;


/**
 * Converter for converting order / cart entries
 */
public class MplOrderEntryPopulator extends OrderEntryPopulator
{
	private Converter<ProductModel, ProductData> productConverter;
	private Converter<MplZoneDeliveryModeValueModel, MarketplaceDeliveryModeData> mplDeliveryModeConverter;
	private PriceDataFactory priceDataFactory;
	private ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker;
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;
	private Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentConverter;
	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;

	private static final String FINEJEWELLERY = "FineJewellery";

	@Override
	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Override
	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	@Override
	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Override
	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	@Override
	protected ModifiableChecker<AbstractOrderEntryModel> getEntryOrderChecker()
	{
		return entryOrderChecker;
	}

	@Override
	@Required
	public void setEntryOrderChecker(final ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker)
	{
		this.entryOrderChecker = entryOrderChecker;
	}

	@Override
	protected Converter<PointOfServiceModel, PointOfServiceData> getPointOfServiceConverter()
	{
		return pointOfServiceConverter;
	}

	@Override
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
			addSellerInformation(source, target);
			//populateSellerInfo(source, target);
			addDeliverySlots(source, target);
			//TPR-1083 Start
			addExchange(source, target);
			//TPR-1083 End


		}
		target.setIsRefundable(source.isIsRefundable());
	}


	/**
	 * @param source
	 * @param target
	 */

	private void addDeliverySlots(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		if (null != source.getScheduledDeliveryCharge())
		{
			target.setScheduledDeliveryCharge(source.getScheduledDeliveryCharge());
		}
		if (null != source.getEdScheduledDate())
		{
			target.setSelectedDeliverySlotDate(source.getEdScheduledDate());
		}
		if (null != source.getTimeSlotFrom())
		{
			target.setTimeSlotFrom(source.getTimeSlotFrom());
		}
		if (null != source.getTimeSlotTo())
		{
			target.setTimeSlotTo(source.getTimeSlotTo());
		}

		if (StringUtils.isNotEmpty(source.getSddDateBetween()))
		{
			target.setEddDateBetWeen(source.getSddDateBetween());
		}
	}

	/**
	 * @param source
	 * @param target
	 */
	private void addSellerInformation(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		// YTODO Auto-generated method stub
		if (null != target.getProduct() && null != target.getProduct().getSeller())
		{
			for (final SellerInformationData seller : target.getProduct().getSeller())
			{
				if (target.getProduct().getRootCategory().equalsIgnoreCase(FINEJEWELLERY))
				{
					final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(source
							.getSelectedUSSID());
					if (CollectionUtils.isNotEmpty(jewelleryInfo))
					{
						if (StringUtils.isNotEmpty(seller.getUssid()) && seller.getUssid().equals(jewelleryInfo.get(0).getPCMUSSID())) //added for fine jewellery
						{
							target.setSelectedSellerInformation(seller);
							break;
						}
					}
				}
				else if (StringUtils.isNotEmpty(source.getSelectedUSSID()) && StringUtils.isNotEmpty(seller.getUssid())
						&& seller.getUssid().equalsIgnoreCase(source.getSelectedUSSID()))
				{
					target.setSelectedSellerInformation(seller);
					break;
				}
			}

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
			//UF-260 starts here
			if (null != source.getCartAdditionalDiscPerc() && source.getCartAdditionalDiscPerc().doubleValue() != 0.0)
			{
				target.setCartAdditionalDiscPerc(createPrice(source, source.getCartAdditionalDiscPerc()));
			}
			//UF-260 ends here
			if (null != source.getTotalSalePrice() && source.getTotalSalePrice().doubleValue() != 0.0)
			{
				target.setTotalSalePrice(createPrice(source, source.getTotalSalePrice()));
			}
			//TPR-774--  ProductPerDisDisplay and TotalMrp added and converted into data
			if (null != source.getProductPerDiscDisplay() && source.getProductPerDiscDisplay().doubleValue() != 0.0)
			{
				target.setProductPerDiscDisplay(createPrice(source, source.getProductPerDiscDisplay()));
			}
			if (null != source.getTotalMrp() && source.getTotalMrp().doubleValue() != 0.0)
			{
				target.setTotalMrp(createPrice(source, source.getTotalMrp()));
			}
			//TPR-774
			if (null != source.getFreeCount())
			{
				target.setFreeCount(source.getFreeCount());
			}
			if (null != source.getQualifyingCount())
			{
				target.setQualifyingCount(source.getQualifyingCount());
			}

			//Added

			if (StringUtils.isNotEmpty(source.getProductPromoCode()))
			{
				target.setProductPromoCode(source.getProductPromoCode());
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
				final MarketplaceDeliveryModeData deliveryModeData = getMplDeliveryModeConverter().convert(deliveryMode);
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



	//	private void populateSellerInfo(final AbstractOrderEntryModel source, final OrderEntryData target)
	//	{
	//		final ProductModel productModel = source.getProduct();
	//		final List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel.getSellerInformationRelator();
	//
	//		// TO-DO
	//		for (final SellerInformationModel sellerInformationModel : sellerInfo)
	//		{
	//			if (productModel.getProductCategoryType().equalsIgnoreCase(FINEJEWELLERY))
	//			{
	//				final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(source
	//						.getSelectedUSSID());
	//
	//				if (sellerInformationModel.getSellerArticleSKU().equals(jewelleryInfo.get(0).getPCMUSSID())) //added for fine jewellery
	//				{
	//					final SellerInformationData sellerInfoData = new SellerInformationData();
	//					sellerInfoData.setSellername(sellerInformationModel.getSellerName());
	//					sellerInfoData.setUssid(sellerInformationModel.getSellerArticleSKU());
	//					sellerInfoData.setSellerID(sellerInformationModel.getSellerID());
	//					target.setSelectedSellerInformation(sellerInfoData);
	//					break;
	//				}
	//			}
	//			else
	//			{
	//				if (sellerInformationModel.getSellerArticleSKU().equals(source.getSelectedUSSID()))
	//				{
	//					final SellerInformationData sellerInfoData = new SellerInformationData();
	//					sellerInfoData.setSellername(sellerInformationModel.getSellerName());
	//					sellerInfoData.setUssid(sellerInformationModel.getSellerArticleSKU());
	//					sellerInfoData.setSellerID(sellerInformationModel.getSellerID());
	//					target.setSelectedSellerInformation(sellerInfoData);
	//					break;
	//				}
	//			}
	//		}
	//	}
	//





	@Override
	protected void addDeliveryMode(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (orderEntry.getMplDeliveryMode() != null)
		{
			entry.setMplDeliveryMode(getMplDeliveryModeConverter().convert(orderEntry.getMplDeliveryMode()));

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

	@Override
	protected void addCommon(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		entry.setEntryNumber(orderEntry.getEntryNumber());
		entry.setQuantity(orderEntry.getQuantity());
		entry.setSelectedUssid(orderEntry.getSelectedUSSID());
		//TISPT-385
		entry.setParentTransactionID(orderEntry.getParentTransactionID());
		adjustUpdateable(entry, orderEntry);
		if (CollectionUtils.isNotEmpty(orderEntry.getProduct().getBrands()))
		{
			final List<BrandModel> brandList = new ArrayList<BrandModel>(orderEntry.getProduct().getBrands());
			entry.setBrandName(brandList.get(0).getName());

		}

	}

	@Override
	protected void adjustUpdateable(final OrderEntryData entry, final AbstractOrderEntryModel entryToUpdate)
	{
		entry.setUpdateable(getEntryOrderChecker().canModify(entryToUpdate));
	}

	@Override
	protected void addProduct(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		entry.setProduct(getProductConverter().convert(orderEntry.getProduct()));
	}

	@Override
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
		//TPR-774
		if (null != orderEntry.getMrp())
		{
			final BigDecimal mprBigDec = new BigDecimal((int) orderEntry.getMrp().doubleValue());
			final PriceData entryMrp = getPriceDataFactory().create(PriceDataType.BUY, mprBigDec,
					MarketplacecommerceservicesConstants.INR);
			entry.setMrp(entryMrp);
		}
		else
		{
			final BigDecimal mprBigDec = BigDecimal.valueOf(0);
			final PriceData entryMrp = getPriceDataFactory().create(PriceDataType.BUY, mprBigDec,
					MarketplacecommerceservicesConstants.INR);
			entry.setMrp(entryMrp);
		}
	}

	@Override
	protected PriceData createPrice(final AbstractOrderEntryModel orderEntry, final Double val)
	{
		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(val.doubleValue()),
				orderEntry.getOrder().getCurrency());
	}

	/* SONAR FIX JEWELLERY */
	//	private void populateSellerInfo(final AbstractOrderEntryModel source, final OrderEntryData target)
	//	{
	//		final ProductModel productModel = source.getProduct();
	//		final List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel.getSellerInformationRelator();
	//
	//		for (final SellerInformationModel sellerInformationModel : sellerInfo)
	//		{
	//			if (productModel.getProductCategoryType().equalsIgnoreCase(FINEJEWELLERY))
	//			{
	//				final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(source
	//						.getSelectedUSSID());
	//				if (CollectionUtils.isNotEmpty(jewelleryInfo))
	//				{
	//					if (sellerInformationModel.getSellerArticleSKU().equals(jewelleryInfo.get(0).getPCMUSSID())) //added for fine jewellery
	//					{
	//						final SellerInformationData sellerInfoData = new SellerInformationData();
	//						sellerInfoData.setSellername(sellerInformationModel.getSellerName());
	//						sellerInfoData.setUssid(sellerInformationModel.getSellerArticleSKU());
	//						sellerInfoData.setSellerID(sellerInformationModel.getSellerID());
	//						target.setSelectedSellerInformation(sellerInfoData);
	//						break;
	//					}
	//				}
	//			}
	//
	//			else
	//			{
	//				if (sellerInformationModel.getSellerArticleSKU().equals(source.getSelectedUSSID()))
	//				{
	//					final SellerInformationData sellerInfoData = new SellerInformationData();
	//					sellerInfoData.setSellername(sellerInformationModel.getSellerName());
	//					sellerInfoData.setUssid(sellerInformationModel.getSellerArticleSKU());
	//					sellerInfoData.setSellerID(sellerInformationModel.getSellerID());
	//					target.setSelectedSellerInformation(sellerInfoData);
	//					break;
	//				}
	//			}
	//		}
	//	}

	/**
	 * @param source
	 * @param target
	 */
	private void addExchange(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		if (StringUtils.isNotEmpty(source.getExchangeId()))
		{
			target.setExchangeApplied(source.getExchangeId());
		}
		else
		{
			target.setExchangeApplied("");
		}


	}

	/**
	 * @return the mplDeliveryModeConverter
	 */
	public Converter<MplZoneDeliveryModeValueModel, MarketplaceDeliveryModeData> getMplDeliveryModeConverter()
	{
		return mplDeliveryModeConverter;
	}

	/**
	 * @param mplDeliveryModeConverter
	 *           the mplDeliveryModeConverter to set
	 */
	@Required
	public void setMplDeliveryModeConverter(
			final Converter<MplZoneDeliveryModeValueModel, MarketplaceDeliveryModeData> mplDeliveryModeConverter)
	{
		this.mplDeliveryModeConverter = mplDeliveryModeConverter;
	}
}
