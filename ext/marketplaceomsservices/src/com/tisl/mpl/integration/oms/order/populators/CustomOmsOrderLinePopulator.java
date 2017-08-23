/**
 *
 */
package com.tisl.mpl.integration.oms.order.populators;

import de.hybris.platform.commerceservices.externaltax.TaxCodeStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.integration.commons.services.OndemandTaxCalculationService;
import de.hybris.platform.integration.oms.order.service.ProductAttributeStrategy;
import de.hybris.platform.integration.oms.order.strategies.OrderEntryNoteStrategy;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.CouponDto;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderlineFulfillmentType;
import com.hybris.oms.domain.order.Promotion;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Quantity;
import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.globalcodes.utilities.MplCodeMasterUtility;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerMasterService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */


public class CustomOmsOrderLinePopulator implements Populator<OrderEntryModel, OrderLine>
{
	private static final Logger LOG = Logger.getLogger(CustomOmsOrderLinePopulator.class);

	private MplSellerInformationService mplSellerInformationService;
	private TaxCodeStrategy taxCodeStrategy;
	private ProductAttributeStrategy productAttributeStrategy;
	private OrderEntryNoteStrategy orderEntryNoteStrategy;
	private OndemandTaxCalculationService ondemandTaxCalculationService;
	@Autowired
	private MplSellerMasterService mplSellerMasterService;




	@Override
	public void populate(final OrderEntryModel source, final OrderLine target) throws ConversionException
	{
		if (source.getSelectedUSSID() != null)
		{
			final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
					source.getSelectedUSSID(), source.getOrder().getStore().getCatalogs().get(0).getActiveCatalogVersion());

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
			String isReturnToStoreEligible = MarketplaceomsservicesConstants.NO;
			String productReturnToStoreEligibility = null;
			String sellerReturnToStoreEligibility = null;
			if (null != source.getProduct() && null != source.getProduct().getRichAttribute()
					&& null != source.getProduct().getRichAttribute())
			{
				final List<RichAttributeModel> productRichAttribute = (List<RichAttributeModel>) source.getProduct()
						.getRichAttribute();
				// INC144316390 - Orders are not getting processed with normal flow due to Product Rich attribute missing   START
				if (null != productRichAttribute && !productRichAttribute.isEmpty() && null != productRichAttribute.get(0)
						&& null != productRichAttribute.get(0).getReturnAtStoreEligible())
				{
					productReturnToStoreEligibility = productRichAttribute.get(0).getReturnAtStoreEligible().getCode();
				}
				// INC144316390 - Orders are not getting processed with normal flow due to Product Rich attribute missing   END
			}
			if (null != richAttributeModel && null != richAttributeModel.get(0)
					&& null != richAttributeModel.get(0).getReturnAtStoreEligible())
			{
				sellerReturnToStoreEligibility = richAttributeModel.get(0).getReturnAtStoreEligible().getCode();
			}
			if (null != sellerReturnToStoreEligibility
					&& sellerReturnToStoreEligibility.equalsIgnoreCase(MarketplaceomsservicesConstants.YES)
					&& null != productReturnToStoreEligibility
					&& productReturnToStoreEligibility.equalsIgnoreCase(MarketplaceomsservicesConstants.YES))
			{
				isReturnToStoreEligible = MarketplaceomsservicesConstants.YES;
			}
			if (null != isReturnToStoreEligible)
			{

				target.setIsReturnToStoreEligible(isReturnToStoreEligible);
			}
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

			if (source.getOrder() != null && source.getOrder().getStatus() != null
					&& source.getOrder().getStatus().getCode() != null)
			{
				//PT issue for One touch cancellation--fix
				String orderStatus = null;
				if (source.getOrder().getParentReference() != null)
				{
					orderStatus = source.getOrder().getParentReference().getStatus().getCode().toUpperCase();
				}
				else
				{
					orderStatus = source.getOrder().getStatus().getCode().toUpperCase();
				}
				target.setOrderLineStatus(MplCodeMasterUtility.getglobalCode(orderStatus));
				//PT issue for One touch cancellation--fix
				//target.setOrderLineStatus(MplCodeMasterUtility.getglobalCode(source.getOrder().getStatus().getCode().toUpperCase()));


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

			//TISPRDT-1226
			if (source.getCurrDelCharge() != null)
			{
				target.setShippingCharge(source.getCurrDelCharge().doubleValue());
			}
			else if (source.getPrevDelCharge() != null && source.getPrevDelCharge().doubleValue() > 0)
			{
				target.setShippingCharge(source.getPrevDelCharge().doubleValue());
			}

			/*
			 * if (source.getPrevDelCharge() != null && source.getPrevDelCharge().doubleValue() > 0) {
			 * target.setShippingCharge(source.getPrevDelCharge().doubleValue()); } else if (source.getCurrDelCharge() !=
			 * null) { target.setShippingCharge(source.getCurrDelCharge().doubleValue()); }
			 */
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
				//TPR-1347---START
				target.setCrmParentRef(source.getParentTransactionID());
				//TPR-1347---END

			}
			//TPR-1347---START
			if (source.getIsBOGOapplied() != null && source.getIsBOGOapplied().booleanValue()
					&& CollectionUtils.isNotEmpty(source.getAssociatedItems()))
			{
				//target.setParentTransactionID(source.getParentTransactionID());
				target.setCrmParentRef(source.getParentTransactionID());
			}
			//TPR-1347---END

			if (richAttributeModel.get(0).getDeliveryFulfillModeByP1() != null
					&& richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode() != null)

			{
				final String fulfilmentType = richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode().toUpperCase();
				target.setFulfillmentMode(fulfilmentType);
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator : FulfillmentMode  is null ");
			}
			// Added the fields for OMS Order create
			/*
			 * if (richAttributeModel.get(0).getDeliveryFulfillModeByP1() != null &&
			 * richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode() != null)
			 * 
			 * { final String fulfilmentType =
			 * richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode().toUpperCase();
			 * target.setFulfillmentTypeP1(fulfilmentType); }
			 * 
			 * if (richAttributeModel.get(0).getDeliveryFulfillModes() != null &&
			 * richAttributeModel.get(0).getDeliveryFulfillModes().getCode() != null)
			 * 
			 * { final String fulfilmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode().toUpperCase();
			 * if(fulfilmentType.equalsIgnoreCase(MarketplaceomsservicesConstants.BOTH)){
			 * if(richAttributeModel.get(0).getDeliveryFulfillModeByP1
			 * ().getCode().toUpperCase().equalsIgnoreCase(MarketplaceomsservicesConstants.TSHIP)){
			 * target.setFulfillmentTypeP2(MarketplaceomsservicesConstants.SSHIP); }else{
			 * target.setFulfillmentTypeP2(MarketplaceomsservicesConstants.TSHIP); } }else{
			 * target.setFulfillmentTypeP2(fulfilmentType); } } else {
			 * LOG.debug("CustomOmsOrderLinePopulator : FulfillmentTypeP2  is null "); }
			 */

			if (source.getFulfillmentMode() != null)
			{
				target.setFulfillmentMode(String.valueOf(source.getFulfillmentMode()));
			}


			if (source.getFulfillmentTypeP1() != null)
			{
				target.setFulfillmentTypeP1(String.valueOf(source.getFulfillmentTypeP1().toUpperCase()));
			}
			else if (richAttributeModel.get(0).getDeliveryFulfillModeByP1() != null
					&& richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode() != null)

			{
				final String fulfilmentType = richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode().toUpperCase();
				target.setFulfillmentTypeP1(fulfilmentType);
			}
			if (source.getFulfillmentTypeP2() != null)
			{
				target.setFulfillmentTypeP2(String.valueOf(source.getFulfillmentTypeP2()));
			}

			if (richAttributeModel.get(0).getIsPrecious() != null && richAttributeModel.get(0).getIsPrecious().getCode() != null)

			{
				final String isPrecious = richAttributeModel.get(0).getIsPrecious().getCode().toUpperCase();

				if (isPrecious.equalsIgnoreCase("YES") || isPrecious.equalsIgnoreCase(MarketplaceomsservicesConstants.Y))
				{
					target.setIsPrecious(MarketplaceomsservicesConstants.Y);
				}
				else
				{
					target.setIsPrecious(MarketplaceomsservicesConstants.N);
				}

			}
			else
			{
				target.setIsPrecious(MarketplaceomsservicesConstants.N);
				LOG.debug("CustomOmsOrderLinePopulator : IsFragile  is null ");
			}

			if (richAttributeModel.get(0).getIsFragile() != null && richAttributeModel.get(0).getIsFragile().getCode() != null)

			{
				final String isFragile = richAttributeModel.get(0).getIsFragile().getCode().toUpperCase();
				if (isFragile.equalsIgnoreCase("YES") || isFragile.equalsIgnoreCase(MarketplaceomsservicesConstants.Y))
				{
					target.setIsFragile(MarketplaceomsservicesConstants.Y);
				}
				else
				{
					target.setIsFragile(MarketplaceomsservicesConstants.N);
				}
			}
			else
			{
				target.setIsPrecious(MarketplaceomsservicesConstants.N);
				LOG.debug("CustomOmsOrderLinePopulator : IsFragile  is null ");
			}
			//Added R2.3 Change
			if (StringUtils.isNotEmpty(source.getEdScheduledDate()))
			{
				target.setEdScheduledDate(String.valueOf(source.getEdScheduledDate()));
			}

			if (StringUtils.isNotEmpty(source.getTimeSlotFrom()))
			{


				final String timeSlotFrom = source.getEdScheduledDate().concat(" " + source.getTimeSlotFrom());
				final SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
				final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				format2.setTimeZone(TimeZone.getTimeZone("GMT"));

				try
				{
					target.setTimeSlotFrom(String.valueOf(format2.format(format1.parse(timeSlotFrom))));
				}
				catch (final ParseException e)
				{
					LOG.error("unable to parse timeslots " + e.getMessage());
				}
			}

			if (StringUtils.isNotEmpty(source.getTimeSlotTo()))
			{
				final String timeSlotTo = source.getEdScheduledDate().concat(" " + source.getTimeSlotTo());
				final SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
				final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				format2.setTimeZone(TimeZone.getTimeZone("GMT"));
				try
				{
					target.setTimeSlotTo(String.valueOf(format2.format(format1.parse(timeSlotTo))));
				}
				catch (final ParseException e)
				{
					LOG.error("unable to parse timeslots " + e.getMessage());
				}
			}
			//Added R2.3 Change
			if (source.getScheduledDeliveryCharge() != null)
			{
				target.setScheduledDeliveryCharge(source.getScheduledDeliveryCharge().doubleValue());
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
			//target.setEstimatedDelivery(source.getOrder().getModifiedtime()); // need to be changed
			if (source.getExpectedDeliveryDate() != null)
			{
				target.setEstimatedDelivery(source.getExpectedDeliveryDate());
			}
			else
			{
				target.setEstimatedDelivery(source.getOrder().getModifiedtime()); // need to be changed
			}

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


			//added new attribute isLPAWBEdit for orderLine
			final SellerMasterModel sellerMasterModel = mplSellerMasterService.getSellerMaster(sellerInfoModel.getSellerID());
			if (sellerMasterModel != null && StringUtils.isNotEmpty(sellerMasterModel.getIsLPAWBEdit())
					&& MarketplaceomsservicesConstants.Y.equalsIgnoreCase(sellerMasterModel.getIsLPAWBEdit()))
			{
				target.setIsLPAWBEdit(Boolean.TRUE);
			}
			else
			{
				target.setIsLPAWBEdit(Boolean.FALSE);
			}
		}
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
			target.setIsaGift(MarketplaceomsservicesConstants.TRUE);
			target.setIsAFreebie(MarketplaceomsservicesConstants.TRUE);
		}
		else
		{
			target.setIsaGift(MarketplaceomsservicesConstants.FALSE);
			target.setIsAFreebie(MarketplaceomsservicesConstants.FALSE);
		}
		//TPR-1345--START
		if (source.getIsBOGOapplied() != null && source.getIsBOGOapplied().booleanValue())
		{
			target.setIsBOGO(Boolean.TRUE);
			//target.setIsBOGO(MarketplaceomsservicesConstants.TRUE);
		}
		else
		{
			target.setIsBOGO(Boolean.FALSE);
			//target.setIsBOGO(MarketplaceomsservicesConstants.FALSE);
		}
		//TPR-1345--END
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



}
