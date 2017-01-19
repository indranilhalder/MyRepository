/**
 *
 */
package com.tisl.mpl.integration.oms.order.populators;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.externaltax.TaxCodeStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.OrderJewelEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.integration.commons.services.OndemandTaxCalculationService;
import de.hybris.platform.integration.oms.order.service.ProductAttributeStrategy;
import de.hybris.platform.integration.oms.order.strategies.OrderEntryNoteStrategy;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.CouponDto;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderlineFulfillmentType;
import com.hybris.oms.domain.order.Promotion;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Quantity;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.globalcodes.utilities.MplCodeMasterUtility;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.PriceBreakupService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */

//Added for 3782
/*
 * @Resource(name = "productFeatureJewelleryOrderService") private ProductFeatureJewelleryOrderService
 * productFeatureJewelleryOrderService;
 */


public class CustomOmsOrderLinePopulator implements Populator<OrderEntryModel, OrderLine>
{
	private static final Logger LOG = Logger.getLogger(CustomOmsOrderLinePopulator.class);

	private MplSellerInformationService mplSellerInformationService;
	private TaxCodeStrategy taxCodeStrategy;
	private ProductAttributeStrategy productAttributeStrategy;
	private OrderEntryNoteStrategy orderEntryNoteStrategy;
	private OndemandTaxCalculationService ondemandTaxCalculationService;
	@Resource(name = "productConverter")
	private Converter<ProductModel, ProductData> productConverter;
	@Resource(name = "productConfiguredPopulator")
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	//Added for 3782
	private PriceBreakupService priceBreakupService;

	@Override
	public void populate(final OrderEntryModel source, final OrderLine target) throws ConversionException
	{
		if (source.getSelectedUSSID() != null)
		{
			final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
					source.getSelectedUSSID());
			List<RichAttributeModel> richAttributeModel = null;
			if (sellerInfoModel != null)
			{
				target.setSkuId(sellerInfoModel.getSellerSKU());
				target.setSellerId(sellerInfoModel.getSellerID());
				richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
			}
			target.setUssId(source.getSelectedUSSID());
			target.setUnitPrice(new Amount("INR", Double.valueOf(source.getBasePrice().doubleValue()))); //price should be changed
			if (source.getProduct() instanceof PcmProductVariantModel)
			{
				final PcmProductVariantModel product = ((PcmProductVariantModel) source.getProduct());
				if (product != null)
				{
					//target.setSkuId(product.getCode());
					target.setProductSize(product.getSize());
				}
			}
			populateData(source, target);
			if (richAttributeModel != null)
			{
				populateRichAttribute(richAttributeModel, target);
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator : getRichAttribute for sellerinformationmodel is null ");
			}

			target.setApportionedShippingCharge(source.getCurrDelCharge().doubleValue());
			target.setSellerOrderId(source.getOrder().getCode());
			if (null != source.getProduct() && StringUtils.isNotEmpty(source.getProduct().getName()))
			{
				target.setProductName(source.getProduct().getName());
			}

			/* Added For Jewellery */
			if (null != source.getProduct()
					&& source.getProduct().getProductCategoryType()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))
			{
				populateJewelleryInfo(source, target);
			}
			/*
			 * source.getOrderJewelEntry().getDiamondRate1(); source.getOrderJewelEntry().getDiamondRate2();
			 * source.getOrderJewelEntry().getDiamondRate3(); source.getOrderJewelEntry().getDiamondRate4();
			 * source.getOrderJewelEntry().getDiamondRate5(); source.getOrderJewelEntry().getDiamondRate6();
			 * source.getOrderJewelEntry().getDiamondRate7(); source.getOrderJewelEntry().getDiamondtotalprice();
			 * source.getOrderJewelEntry().getGemStoneRate1(); source.getOrderJewelEntry().getGemStoneRate2();
			 * source.getOrderJewelEntry().getGemStoneRate3(); source.getOrderJewelEntry().getGemStoneRate4();
			 * source.getOrderJewelEntry().getGemStoneRate5(); source.getOrderJewelEntry().getGemStoneRate6();
			 * source.getOrderJewelEntry().getGemStoneRate7(); source.getOrderJewelEntry().getGemStoneRate8();
			 * source.getOrderJewelEntry().getGemStoneRate9(); source.getOrderJewelEntry().getGemStoneRate10();
			 * source.getOrderJewelEntry().getGemstonetotalprice(); source.getOrderJewelEntry().getMakingCharge();
			 * source.getOrderJewelEntry().getWastageTax();
			 *
			 * }
			 */


			/*
			 * if (null != source.getProduct()) { final ProductModel product = source.getProduct(); for (final
			 * CategoryModel category : product.getClassificationClasses().) { category.getName();
			 *
			 *
			 * if (category.getCode().contains("MSH")) { category.getName(); //category.gets }
			 *
			 * }
			 *
			 *
			 * }
			 */
			/* Added For Jewellery */


			if (source.getOrder() != null && source.getOrder().getStatus() != null
					&& source.getOrder().getStatus().getCode() != null)
			{
				target.setOrderLineStatus(MplCodeMasterUtility.getglobalCode(source.getOrder().getStatus().getCode().toUpperCase()));
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator : Order model status  is null ");
			}

			final Quantity quantity = new Quantity();
			quantity.setUnitCode(MarketplaceomsservicesConstants.UnitCode_Pieces);
			quantity.setValue(Integer.parseInt(MarketplaceomsservicesConstants.UnitCode_Pieces_Value));
			target.setQuantity(quantity);
			target.setQuantityUnassigned(quantity); // this value is for OOTB

			// for promotion dummy value added
			final List<Promotion> promotions = new ArrayList<Promotion>();
			if (source.getProductPromoCode() != null)
			{
				final Promotion prodPromotion = new Promotion();
				prodPromotion.setPromotionCode(source.getProductPromoCode());
				prodPromotion.setPromotionValue(source.getTotalProductLevelDisc().toString());
				promotions.add(prodPromotion);
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator : getProductPromoCode  is null ");
			}
			if (source.getCartPromoCode() != null)
			{
				final Promotion cartPromotion = new Promotion();
				cartPromotion.setPromotionCode(source.getCartPromoCode());
				cartPromotion.setPromotionValue(source.getCartLevelDisc().toString());
				promotions.add(cartPromotion);
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator : getCartPromoCode  is null ");
			}

			target.setPromotion(promotions);
			target.setApprotionedPrice(source.getNetAmountAfterAllDisc().doubleValue() > 0 ? source.getNetAmountAfterAllDisc()
					.doubleValue() : 2.0);
			//Code Blocked since coupon has been out of scope for Release2

			if (source.getCouponCode() != null && !source.getCouponCode().isEmpty())
			{

				final CouponDto coupon = new CouponDto();
				coupon.setCouponCode(source.getCouponCode());
				if (source.getCouponValue().doubleValue() > 0D)
				{
					coupon.setCouponValue(source.getCouponValue().toString());
				}

				//coupon.setSellerID(sellerInfoModel.getSellerID());
				final ArrayList<CouponDto> couponList = new ArrayList<>();
				couponList.add(coupon);
				target.setCoupon(couponList);
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator : there is no coupon for the order ");
			}

			if (source.getPrevDelCharge() != null && source.getPrevDelCharge().doubleValue() > 0)
			{
				target.setShippingCharge(source.getPrevDelCharge().doubleValue());
			}
			else if (source.getCurrDelCharge() != null)
			{
				target.setShippingCharge(source.getCurrDelCharge().doubleValue());
			}
			if (source.getOrder().getPaymentInfo() instanceof CODPaymentInfoModel)
			{
				target.setIsCOD(true);
			}
			else
			{
				target.setIsCOD(false);
			}

			target.setFulfillmentType(OrderlineFulfillmentType.REGULAR);

			//TISUTO-128
			if (source.getGiveAway() != null && source.getGiveAway().booleanValue()
					&& CollectionUtils.isNotEmpty(source.getAssociatedItems()))
			{
				target.setParentTransactionID(source.getParentTransactionID());

			}

			if (null != richAttributeModel.get(0) && richAttributeModel.get(0).getDeliveryFulfillModes() != null
					&& richAttributeModel.get(0).getDeliveryFulfillModes().getCode() != null)

			{
				final String fulfilmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode().toUpperCase();

				target.setFulfillmentMode(MplCodeMasterUtility.getglobalCode(fulfilmentType));
				// Setting parentFullfillment type to freebie in the case of SSHIP
				if (source.getGiveAway().booleanValue())
				{
					try
					{
						final String parentFullfillmentType = getMplSellerInformationService().getFullfillmentTypeOfParent(source);
						if (null != parentFullfillmentType && null != source.getFulfillmentType())
						{
							LOG.debug("Parent entry fullFillment type :" + parentFullfillmentType);
							if (source.getFulfillmentType().equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIPCODE)
									&& parentFullfillmentType.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIPCODE))
							{
								target.setFulfillmentMode(parentFullfillmentType.toUpperCase());
								LOG.info(" Parent is SSHIP  and FreeBie is TSHIP : Setting FreeBie Fulfillemt Type as SSHIP");
							}
						}

					}
					catch (final EtailBusinessExceptions e)
					{
						LOG.error("Exception occured while setting fullFillMent Type for freebie " + e.getErrorCode());
					}
					catch (final Exception e)
					{
						LOG.error("Exception occured while setting fullFillMent Type for freebie " + e.getMessage());
					}
				}
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator : FulfillmentMode  is null ");
			}

			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = source.getMplDeliveryMode();
			String deliveryModeCode = null;
			if (mplZoneDeliveryModeValueModel != null && mplZoneDeliveryModeValueModel.getDeliveryMode() != null
					&& mplZoneDeliveryModeValueModel.getDeliveryMode().getCode() != null)
			{
				deliveryModeCode = mplZoneDeliveryModeValueModel.getDeliveryMode().getCode().toUpperCase();
				target.setDeliveryType(MplCodeMasterUtility.getglobalCode(deliveryModeCode));
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator : DeliveryType  is null ");
			}

			if (richAttributeModel.get(0).getShippingModes() != null
					&& richAttributeModel.get(0).getShippingModes().getCode() != null)
			{

				target.setTransportMode(MplCodeMasterUtility.getglobalCode(richAttributeModel.get(0).getShippingModes().getCode()
						.toUpperCase()));
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator TransportMode is null ");
			}
			//OOTB These values are set  to pass the out of the box checks , these fields are not required for OMS
			// Store 1 will be replaced once the Click and Collect functionality is implemented
			//OMS expecting pickup person name and mobile number for each order line setting those  values from Order Model
			if (source.getOrder() != null)
			{
				target.setCollectName(source.getOrder().getPickupPersonName());
				target.setCollectPhoneNumber(source.getOrder().getPickupPersonMobile());
			}
			final LocationRole locationRole = LocationRole.SHIPPING;
			final Set<LocationRole> locationRoles = new HashSet<LocationRole>();
			locationRoles.add(locationRole);
			target.setLocationRoles(locationRoles);
			target.setEstimatedDelivery(source.getOrder().getModifiedtime()); // need to be changed

			if (deliveryModeCode != null && source.getDeliveryPointOfService() != null
					&& MplCodeMasterUtility.getglobalCode(deliveryModeCode).equalsIgnoreCase(MarketplaceomsservicesConstants.CNC))
			{
				target.setStoreID(source.getDeliveryPointOfService().getSlaveId());
			}

			if (source.getCollectionDays() != null)
			{
				target.setCollectionDays(String.valueOf(source.getCollectionDays()));
			}

			//source.getCollectionDays()
			target.setUnitTax(new Amount(source.getOrder().getCurrency().getIsocode(), Double.valueOf(0.0)));
			target.setTaxCategory(MarketplaceomsservicesConstants.TAX_CATEGORY);



		}
		else
		{
			LOG.debug("CustomOmsOrderLinePopulator : orderentry  is null ");
		}
	}


	//Added for jewellery
	/**
	 * @param category
	 */
	/*
	 * private void getCategoryName(final CategoryModel category) { // YTODO Auto-generated method stub try { if
	 * (!category.getSupercategories().isEmpty()) { for (final CategoryModel superCategory :
	 * category.getSupercategories()) { getCategoryName(superCategory); } } } catch (final Exception e) { throw new
	 * EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000); }
	 *
	 * }
	 */

	//Added for 3782

	/**
	 * @param source
	 * @param target
	 */

	private void populateJewelleryInfo(final OrderEntryModel source, final OrderLine target)
	{

		final ProductData productData = productConverter.convert(source.getProduct());
		productConfiguredPopulator.populate(source.getProduct(), productData,
				Arrays.asList(ProductOption.CATEGORIES, ProductOption.CLASSIFICATION));
		if (null != productData.getClassifications())
		{
			final List<ClassificationData> ConfigurableAttributeList = new ArrayList<ClassificationData>(
					productData.getClassifications());
			for (final ClassificationData classification : ConfigurableAttributeList)
			{
				classification.getName();
			}

		}
		final OrderJewelEntryModel jewelleryEntry = source.getOrderJewelEntry();
		if (null != jewelleryEntry.getDiamondRate1())
		{
			jewelleryEntry.getDiamondRate1();
		}
		if (null != jewelleryEntry.getDiamondRate2())
		{
			jewelleryEntry.getDiamondRate2();
		}
		if (null != jewelleryEntry.getDiamondRate3())
		{
			jewelleryEntry.getDiamondRate3();
		}
		if (null != jewelleryEntry.getDiamondRate4())
		{
			jewelleryEntry.getDiamondRate4();
		}
		if (null != jewelleryEntry.getDiamondRate5())
		{
			jewelleryEntry.getDiamondRate5();
		}
		if (null != jewelleryEntry.getDiamondRate6())
		{
			jewelleryEntry.getDiamondRate6();
		}
		if (null != jewelleryEntry.getDiamondRate7())
		{
			jewelleryEntry.getDiamondRate7();
		}
		if (null != jewelleryEntry.getDiamondtotalprice())
		{
			jewelleryEntry.getDiamondtotalprice();
		}
		if (null != jewelleryEntry.getGemStoneRate1())
		{
			jewelleryEntry.getGemStoneRate1();
		}
		if (null != jewelleryEntry.getGemStoneRate1())
		{
			jewelleryEntry.getGemStoneRate1();
		}
		if (null != jewelleryEntry.getGemStoneRate2())
		{
			jewelleryEntry.getGemStoneRate2();
		}
		if (null != jewelleryEntry.getGemStoneRate3())
		{
			jewelleryEntry.getGemStoneRate3();
		}
		if (null != jewelleryEntry.getGemStoneRate4())
		{
			jewelleryEntry.getGemStoneRate4();
		}
		if (null != jewelleryEntry.getGemStoneRate5())
		{
			jewelleryEntry.getGemStoneRate5();
		}
		if (null != jewelleryEntry.getGemStoneRate6())
		{
			jewelleryEntry.getGemStoneRate6();
		}
		if (null != jewelleryEntry.getGemStoneRate7())
		{
			jewelleryEntry.getGemStoneRate7();
		}
		if (null != jewelleryEntry.getGemStoneRate8())
		{
			jewelleryEntry.getGemStoneRate8();
		}
		if (null != jewelleryEntry.getGemStoneRate9())
		{
			jewelleryEntry.getGemStoneRate9();
		}
		if (null != jewelleryEntry.getGemStoneRate10())
		{
			jewelleryEntry.getGemStoneRate10();
		}
		if (null != jewelleryEntry.getGemstonetotalprice())
		{
			jewelleryEntry.getGemstonetotalprice();
		}
		if (null != jewelleryEntry.getMakingCharge())
		{
			jewelleryEntry.getMakingCharge();
		}
		if (null != jewelleryEntry.getWastageTax())
		{
			jewelleryEntry.getWastageTax();
		}


		//final ProductFeatureModel jewelleryProductFeature = productFeatureJewelleryOrderService.ProductFeatureJewelleryOrderService(source.getSelectedUSSID());


		//source.getProduct().getProductFeatureComponents();
		//jewelleryEntry.getAbstractOrderEntryjewel().getProduct().getSupercategories();
	}

	/**
	 * @param source
	 * @param target
	 */
	private void populateData(final OrderEntryModel source, final OrderLine target)
	{
		// YTODO Auto-generated method stub
		if (null != source.getConvenienceChargeApportion())
		{
			target.setApprotionedCODPrice(source.getConvenienceChargeApportion().doubleValue());
		}
		else
		{
			LOG.debug("CustomOmsOrderLinePopulator : getConvenienceChargeApportion is null ");
		}
		if (source.getGiveAway() != null && source.getGiveAway().booleanValue())
		{
			target.setIsaGift(MarketplaceomsservicesConstants.YES);
			target.setIsAFreebie(MarketplaceomsservicesConstants.YES);
		}
		else
		{
			target.setIsaGift(MarketplaceomsservicesConstants.NO);
			target.setIsAFreebie(MarketplaceomsservicesConstants.NO);
		}

		target.setOrderLineId((source.getOrderLineId() != null) ? source.getOrderLineId() : source.getTransactionID());


	}

	/**
	 *
	 */
	private void populateRichAttribute(final List<RichAttributeModel> richAttributeModel, final OrderLine target)
	{
		// YTODO Auto-generated method stub

		if (StringUtils.isNotEmpty(richAttributeModel.get(0).getCancellationWindow()))
		{
			target.setCancellationAllowed(richAttributeModel.get(0).getCancellationWindow());
		}
		else
		{
			target.setCancellationAllowed("No");
		}
		if (StringUtils.isNotEmpty(richAttributeModel.get(0).getReturnWindow()))
		{
			target.setReturnsAllowed(richAttributeModel.get(0).getReturnWindow());
		}
		else
		{
			target.setReturnsAllowed("No");
		}
		if (StringUtils.isNotEmpty(richAttributeModel.get(0).getReplacementWindow()))
		{
			target.setReplacementAllowed(richAttributeModel.get(0).getReplacementWindow());
		}
		else
		{
			target.setReplacementAllowed("No");
		}
		if (StringUtils.isNotEmpty(richAttributeModel.get(0).getExchangeAllowedWindow()))
		{
			target.setExchangeAllowed(richAttributeModel.get(0).getExchangeAllowedWindow());
		}
		else
		{
			target.setExchangeAllowed("No");
		}

	}

	protected TaxCodeStrategy getTaxCodeStrategy()
	{
		return this.taxCodeStrategy;
	}

	@Required
	public void setTaxCodeStrategy(final TaxCodeStrategy taxCodeStrategy)
	{
		this.taxCodeStrategy = taxCodeStrategy;
	}

	protected ProductAttributeStrategy getProductAttributeStrategy()
	{
		return this.productAttributeStrategy;
	}

	@Required
	public void setProductAttributeStrategy(final ProductAttributeStrategy productAttributeStrategy)
	{
		this.productAttributeStrategy = productAttributeStrategy;
	}

	protected OndemandTaxCalculationService getOndemandTaxCalculationService()
	{
		return this.ondemandTaxCalculationService;
	}

	@Required
	public void setOndemandTaxCalculationService(final OndemandTaxCalculationService ondemandTaxCalculationService)
	{
		this.ondemandTaxCalculationService = ondemandTaxCalculationService;
	}

	protected OrderEntryNoteStrategy getOrderEntryNoteStrategy()
	{
		return this.orderEntryNoteStrategy;
	}

	@Required
	public void setOrderEntryNoteStrategy(final OrderEntryNoteStrategy orderEntryNoteStrategy)
	{
		this.orderEntryNoteStrategy = orderEntryNoteStrategy;
	}

	/**
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}

	/**
	 * @param mplSellerInformationService
	 *           the mplSellerInformationService to set
	 */
	@Required
	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}

	//Added for 3782
	/**
	 * @return the priceBreakupService
	 */
	public PriceBreakupService getPriceBreakupService()
	{
		return priceBreakupService;
	}

	/**
	 * @param priceBreakupService
	 *           the priceBreakupService to set
	 */
	public void setPriceBreakupService(final PriceBreakupService priceBreakupService)
	{
		this.priceBreakupService = priceBreakupService;
	}
}
