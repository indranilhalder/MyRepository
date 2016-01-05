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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.locationrole.LocationRole;
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
import com.tisl.mpl.model.SellerInformationModel;


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


			if (richAttributeModel.get(0).getDeliveryFulfillModes() != null
					&& richAttributeModel.get(0).getDeliveryFulfillModes().getCode() != null)

			{
				final String fulfilmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode().toUpperCase();

				target.setFulfillmentMode(MplCodeMasterUtility.getglobalCode(fulfilmentType));
			}
			else
			{
				LOG.debug("CustomOmsOrderLinePopulator : FulfillmentMode  is null ");
			}

			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = source.getMplDeliveryMode();
			if (mplZoneDeliveryModeValueModel != null && mplZoneDeliveryModeValueModel.getDeliveryMode() != null
					&& mplZoneDeliveryModeValueModel.getDeliveryMode().getCode() != null)
			{
				final String deliveryModeCode = mplZoneDeliveryModeValueModel.getDeliveryMode().getCode().toUpperCase();
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
			target.setCollectName("name");
			target.setCollectPhoneNumber("9681684233");
			final LocationRole locationRole = LocationRole.SHIPPING;
			final Set<LocationRole> locationRoles = new HashSet<LocationRole>();
			locationRoles.add(locationRole);
			target.setLocationRoles(locationRoles);
			target.setEstimatedDelivery(source.getOrder().getModifiedtime()); // need to be changed
			target.setStoreID("Store1");

			target.setUnitTax(new Amount(source.getOrder().getCurrency().getIsocode(), Double
					.valueOf(getOndemandTaxCalculationService().calculatePreciseUnitTax(
							source.getOrder().getEntries().get(0).getTaxValues(),
							source.getOrder().getEntries().get(0).getQuantity().doubleValue(),
							source.getOrder().getEntries().get(0).getOrder().getNet().booleanValue()).doubleValue())));
			target.setTaxCategory(getTaxCodeStrategy().getTaxCodeForCodeAndOrder(
					source.getOrder().getEntries().get(0).getProduct().getCode(), source.getOrder().getEntries().get(0).getOrder()));

		}
		else
		{
			LOG.debug("CustomOmsOrderLinePopulator : orderentry  is null ");
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
}
