/**
 *
 */
package com.tisl.mpl.integration.oms.order.populators;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commerceservices.externaltax.TaxCodeStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.OrderJewelEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.integration.commons.services.OndemandTaxCalculationService;
import de.hybris.platform.integration.oms.order.service.ProductAttributeStrategy;
import de.hybris.platform.integration.oms.order.strategies.OrderEntryNoteStrategy;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.Resource;

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
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.globalcodes.utilities.MplCodeMasterUtility;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerMasterService;
import com.tisl.mpl.marketplacecommerceservices.service.PriceBreakupService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;


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
	/* SONAR FIX JEWELLERY */
	//	@Resource(name = "productConverter")
	//	private Converter<ProductModel, ProductData> productConverter;
	/* SONAR FIX JEWELLERY */
	//	@Resource(name = "productConfiguredPopulator")
	//	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	//Added for 3782
	private PriceBreakupService priceBreakupService;

	@Autowired
	private MplSellerMasterService mplSellerMasterService;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;

	@Override
	public void populate(final OrderEntryModel source, final OrderLine target) throws ConversionException
	{
		if (source.getSelectedUSSID() != null)
		{
			//Added for jewellery
			String ussid = null;
			String jwlSkuid = null;

			if (null != source.getProduct()
					&& source.getProduct().getProductCategoryType()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))
			{
				final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(source
						.getSelectedUSSID());
				ussid = jewelleryInfo.get(0).getPCMUSSID();
				jwlSkuid = source.getSelectedUSSID().substring(6, source.getSelectedUSSID().length());
			}
			else
			{
				ussid = source.getSelectedUSSID();
			}

			//final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
			//	source.getSelectedUSSID());

			final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(ussid,source.getOrder().getStore().getCatalogs().get(0).getActiveCatalogVersion());

			//jewellery ends


			List<RichAttributeModel> richAttributeModel = null;
			if (sellerInfoModel != null)
			{

				if (StringUtils.isNotEmpty(jwlSkuid))
				{
					target.setSkuId(jwlSkuid); //Added for jewellery
				}
				else
				{
					target.setSkuId(sellerInfoModel.getSellerSKU());
				}
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
			if (null != source.getProduct() && null != source.getProduct().getSellerInformationRelator())
			{
				final List<SellerInformationModel> sellerList = (List<SellerInformationModel>) source.getProduct()
						.getSellerInformationRelator();
				final List<RichAttributeModel> sellerRichAttributeList = new ArrayList<RichAttributeModel>();
				if (null != sellerList)
				{
					for (final SellerInformationModel seller : sellerList)
					{
						if (null != seller.getRichAttribute())
						{
							final ArrayList<RichAttributeModel> richattributeList = new ArrayList<RichAttributeModel>(
									seller.getRichAttribute());
							sellerRichAttributeList.add(richattributeList.get(0));
						}
					}
				}
				/*
				 * final List<RichAttributeModel> productRichAttribute = (List<RichAttributeModel>) source.getProduct()
				 * .getRichAttribute();
				 */
				if (null != sellerRichAttributeList.get(0) && null != sellerRichAttributeList.get(0).getReturnAtStoreEligible())
				{
					productReturnToStoreEligibility = sellerRichAttributeList.get(0).getReturnAtStoreEligible().getCode();
				}
			}
			//			if (null != source.getProduct() && null != source.getProduct().getRichAttribute()
			//					&& null != source.getProduct().getRichAttribute())
			//			{
			//				final List<RichAttributeModel> productRichAttribute =  (List<RichAttributeModel>)source.getProduct().getRichAttribute();
			//				// INC144316390 - Orders are not getting processed with normal flow due to Product Rich attribute missing   START
			//				if (null != productRichAttribute && !productRichAttribute.isEmpty() && null != productRichAttribute.get(0) && null !=productRichAttribute.get(0).getReturnAtStoreEligible())
			//				{
			//					productReturnToStoreEligibility = productRichAttribute.get(0).getReturnAtStoreEligible().getCode();
			//				}
			//				// INC144316390 - Orders are not getting processed with normal flow due to Product Rich attribute missing   END
			//			}
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


			/* Added For Jewellery */
			if (null != source.getProduct()
					&& source.getProduct().getProductCategoryType()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))
			{

				if (null != source.getOrder() && null != source.getOrder().getParentReference())
				{
					//Checking the pancard request is true or not
					if (source.getOrder().getParentReference().getPanVerificationReq().booleanValue())
					{
						target.setIsPANCARDVerificationReq("Y");
					}
					else
					{
						target.setIsPANCARDVerificationReq("N");
					}
				}
				populateJewelleryInfo(source, target);
			}

			if (StringUtils.isNotEmpty(source.getProductRootCatCode()))
			{
				target.setCategoryName(source.getProductRootCatCode());
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

			//New Changes for Jewellery
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
		final OrderJewelEntryModel jewelleryEntry = source.getOrderJewelEntry();

		if (null != jewelleryEntry)
		{
			if (null != jewelleryEntry.getPurity())
			{
				target.setPurity(jewelleryEntry.getPurity());
			}
			if (null != jewelleryEntry.getMetalValue())
			{
				target.setMetalValue(jewelleryEntry.getMetalValue());
			}
			if (null != jewelleryEntry.getSolitaireValue())
			{
				target.setSolitaireValue(jewelleryEntry.getSolitaireValue());
			}
			if (null != jewelleryEntry.getDiamondValue())
			{
				target.setDiamondValue(jewelleryEntry.getDiamondValue());
			}
			if (null != jewelleryEntry.getStoneValue())
			{
				target.setStoneValue(jewelleryEntry.getStoneValue());
			}
			if (null != jewelleryEntry.getMakingCharge())
			{
				target.setMakingCharges(jewelleryEntry.getMakingCharge());
			}
			if (null != jewelleryEntry.getWastageTax())
			{
				target.setWastageCharges(jewelleryEntry.getWastageTax());
			}
			if (null != jewelleryEntry.getMetalRate())
			{
				target.setMetalRate(jewelleryEntry.getMetalRate().toString());
			}
			if (null != jewelleryEntry.getSolitaireRate())
			{
				target.setSolitaireRate(jewelleryEntry.getSolitaireRate().toString());
			}
			if (null != jewelleryEntry.getDiamondRateType1())
			{
				target.setDiamondRateType1(jewelleryEntry.getDiamondRateType1().toString());
			}
			if (null != jewelleryEntry.getDiamondColorType1())
			{
				target.setDiamondColorType1(jewelleryEntry.getDiamondColorType1());
			}
			if (null != jewelleryEntry.getDiamondClarityType1())
			{
				target.setDiamondClarityType1(jewelleryEntry.getDiamondClarityType1());
			}
			if (null != jewelleryEntry.getDiamondRateType2())
			{
				target.setDiamondRateType2(jewelleryEntry.getDiamondRateType2().toString());
			}
			if (null != jewelleryEntry.getDiamondColorType2())
			{
				target.setDiamondColorType2(jewelleryEntry.getDiamondColorType2());
			}
			if (null != jewelleryEntry.getDiamondClarityType2())
			{
				target.setDiamondClarityType2(jewelleryEntry.getDiamondClarityType2());
			}
			if (null != jewelleryEntry.getDiamondRateType3())
			{
				target.setDiamondRateType3(jewelleryEntry.getDiamondRateType3().toString());
			}
			if (null != jewelleryEntry.getDiamondColorType3())
			{
				target.setDiamondColorType3(jewelleryEntry.getDiamondColorType3());
			}
			if (null != jewelleryEntry.getDiamondClarityType3())
			{
				target.setDiamondClarityType3(jewelleryEntry.getDiamondClarityType3());
			}
			if (null != jewelleryEntry.getDiamondRateType4())
			{
				target.setDiamondRateType4(jewelleryEntry.getDiamondRateType4().toString());
			}
			if (null != jewelleryEntry.getDiamondColorType4())
			{
				target.setDiamondColorType4(jewelleryEntry.getDiamondColorType4());
			}
			if (null != jewelleryEntry.getDiamondClarityType4())
			{
				target.setDiamondClarityType4(jewelleryEntry.getDiamondClarityType4());
			}
			if (null != jewelleryEntry.getDiamondRateType5())
			{
				target.setDiamondRateType5(jewelleryEntry.getDiamondRateType5().toString());
			}
			if (null != jewelleryEntry.getDiamondColorType5())
			{
				target.setDiamondColorType5(jewelleryEntry.getDiamondColorType5());
			}
			if (null != jewelleryEntry.getDiamondClarityType5())
			{
				target.setDiamondClarityType5(jewelleryEntry.getDiamondClarityType5());
			}
			if (null != jewelleryEntry.getDiamondRateType6())
			{
				target.setDiamondRateType6(jewelleryEntry.getDiamondRateType6().toString());
			}
			if (null != jewelleryEntry.getDiamondColorType6())
			{
				target.setDiamondColorType6(jewelleryEntry.getDiamondColorType6());
			}
			if (null != jewelleryEntry.getDiamondClarityType6())
			{
				target.setDiamondClarityType6(jewelleryEntry.getDiamondClarityType6());
			}
			if (null != jewelleryEntry.getDiamondRateType7())
			{
				target.setDiamondRateType7(jewelleryEntry.getDiamondRateType7().toString());
			}
			if (null != jewelleryEntry.getDiamondColorType7())
			{
				target.setDiamondColorType7(jewelleryEntry.getDiamondColorType7());
			}
			if (null != jewelleryEntry.getDiamondClarityType7())
			{
				target.setDiamondClarityType7(jewelleryEntry.getDiamondClarityType7());
			}

			if (null != jewelleryEntry.getStoneRateType1())
			{
				target.setStoneRateType1(jewelleryEntry.getStoneRateType1().toString());
			}
			if (null != jewelleryEntry.getStoneType1())
			{
				target.setStoneType1(jewelleryEntry.getStoneType1());
			}
			if (null != jewelleryEntry.getStoneSizeType1())
			{
				target.setStoneSizeType1(jewelleryEntry.getStoneSizeType1());
			}

			if (null != jewelleryEntry.getStoneRateType2())
			{
				target.setStoneRateType2(jewelleryEntry.getStoneRateType2().toString());
			}
			if (null != jewelleryEntry.getStoneType2())
			{
				target.setStoneType2(jewelleryEntry.getStoneType2());
			}
			if (null != jewelleryEntry.getStoneSizeType2())
			{
				target.setStoneSizeType2(jewelleryEntry.getStoneSizeType2());
			}

			if (null != jewelleryEntry.getStoneRateType3())
			{
				target.setStoneRateType3(jewelleryEntry.getStoneRateType3().toString());
			}
			if (null != jewelleryEntry.getStoneType3())
			{
				target.setStoneType3(jewelleryEntry.getStoneType3());
			}
			if (null != jewelleryEntry.getStoneSizeType3())
			{
				target.setStoneSizeType3(jewelleryEntry.getStoneSizeType3());
			}

			if (null != jewelleryEntry.getStoneRateType4())
			{
				target.setStoneRateType4(jewelleryEntry.getStoneRateType4().toString());
			}
			if (null != jewelleryEntry.getStoneType4())
			{
				target.setStoneType4(jewelleryEntry.getStoneType4());
			}
			if (null != jewelleryEntry.getStoneSizeType4())
			{
				target.setStoneSizeType4(jewelleryEntry.getStoneSizeType4());
			}

			if (null != jewelleryEntry.getStoneRateType5())
			{
				target.setStoneRateType5(jewelleryEntry.getStoneRateType5().toString());
			}
			if (null != jewelleryEntry.getStoneType5())
			{
				target.setStoneType5(jewelleryEntry.getStoneType5());
			}
			if (null != jewelleryEntry.getStoneSizeType5())
			{
				target.setStoneSizeType5(jewelleryEntry.getStoneSizeType5());
			}

			if (null != jewelleryEntry.getStoneRateType6())
			{
				target.setStoneRateType6(jewelleryEntry.getStoneRateType6().toString());
			}
			if (null != jewelleryEntry.getStoneType6())
			{
				target.setStoneType6(jewelleryEntry.getStoneType6());
			}
			if (null != jewelleryEntry.getStoneSizeType6())
			{
				target.setStoneSizeType6(jewelleryEntry.getStoneSizeType6());
			}

			if (null != jewelleryEntry.getStoneRateType7())
			{
				target.setStoneRateType7(jewelleryEntry.getStoneRateType7().toString());
			}
			if (null != jewelleryEntry.getStoneType7())
			{
				target.setStoneType7(jewelleryEntry.getStoneType7());
			}
			if (null != jewelleryEntry.getStoneSizeType7())
			{
				target.setStoneSizeType7(jewelleryEntry.getStoneSizeType7());
			}

			if (null != jewelleryEntry.getStoneRateType8())
			{
				target.setStoneRateType8(jewelleryEntry.getStoneRateType8().toString());
			}
			if (null != jewelleryEntry.getStoneType8())
			{
				target.setStoneType8(jewelleryEntry.getStoneType8());
			}
			if (null != jewelleryEntry.getStoneSizeType8())
			{
				target.setStoneSizeType8(jewelleryEntry.getStoneSizeType8());
			}

			if (null != jewelleryEntry.getStoneRateType9())
			{
				target.setStoneRateType9(jewelleryEntry.getStoneRateType9().toString());
			}
			if (null != jewelleryEntry.getStoneType9())
			{
				target.setStoneType9(jewelleryEntry.getStoneType9());
			}
			if (null != jewelleryEntry.getStoneSizeType9())
			{
				target.setStoneSizeType9(jewelleryEntry.getStoneSizeType9());
			}

			if (null != jewelleryEntry.getStoneRateType10())
			{
				target.setStoneRateType10(jewelleryEntry.getStoneRateType10().toString());
			}
			if (null != jewelleryEntry.getStoneType10())
			{
				target.setStoneType10(jewelleryEntry.getStoneType10());
			}
			if (null != jewelleryEntry.getStoneSizeType10())
			{
				target.setStoneSizeType10(jewelleryEntry.getStoneSizeType10());
			}

			//TISJEW-3469
			if (null != jewelleryEntry.getPriceBreakuponInvoice())
			{
				target.setPriceBreakuponInvoice(jewelleryEntry.getPriceBreakuponInvoice());
			}

			if (null != jewelleryEntry.getMetalName())
			{
				target.setMetalName(jewelleryEntry.getMetalName());
			}

			if (null != jewelleryEntry.getBrandName())
			{
				target.setBrandName(jewelleryEntry.getBrandName());
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

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}
}

