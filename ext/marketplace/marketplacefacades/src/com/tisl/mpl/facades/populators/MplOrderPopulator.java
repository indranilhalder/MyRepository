/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.EMIPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.NetbankingPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.ThirdPartyWalletInfoModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplPaymentInfoData;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.MplCartOfferVoucherModel;


/**
 * @author TCS
 *
 */
public class MplOrderPopulator extends AbstractOrderPopulator<OrderModel, OrderData>
{


	@Override
	public void populate(final OrderModel source, final OrderData target)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);

		addCommon(source, target);
		addDetails(source, target);
		addTotals(source, target);
		addEntries(source, target);
		addPromotions(source, target);
		addDeliveryAddress(source, target);
		addDeliveryMethod(source, target);
		addPaymentInformation(source, target);
		checkForGuestCustomer(source, target);
		addDeliveryStatus(source, target);
		addPaymentDetails(source, target);
		addPrincipalInformation(source, target);
		addConvinienceCharges(source, target);
		addVoucherDiscount(source, target);
		addPickupPersonDetails(source, target);
		addDeliverryAddressList(source, target);

		if (CollectionUtils.isNotEmpty(source.getAllPromotionResults()))
		{
			final Set<PromotionResultModel> eligiblePromoList = source.getAllPromotionResults();
			boolean isShippingPromoApplied = false;
			for (final PromotionResultModel promotionResultModel : eligiblePromoList)
			{
				//TISSIT-1776
				if (promotionResultModel != null && promotionResultModel.getPromotion() != null)
				{
					final AbstractPromotionModel promotion = promotionResultModel.getPromotion();

					if (promotionResultModel.getCertainty().floatValue() == 1.0F
							&& (promotion instanceof BuyAGetPromotionOnShippingChargesModel
									|| promotion instanceof BuyAandBGetPromotionOnShippingChargesModel
									|| promotion instanceof BuyAboveXGetPromotionOnShippingChargesModel))
					{
						isShippingPromoApplied = true;
						break;
					}
				}
			}

			if (isShippingPromoApplied)
			{
				addDeliveryModePromotion(source, target);
			}

		}
	}


	/**
	 * @param source
	 * @param target
	 */
	private void addConvinienceCharges(final OrderModel source, final OrderData target)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);
		if (source.getConvenienceCharges() != null)
		{
			target.setConvenienceChargeForCOD(createPrice(source, source.getConvenienceCharges()));
		}
	}


	protected void addDetails(final OrderModel source, final OrderData target)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);
		target.setCreated(source.getDate());
		target.setStatus(source.getStatus());
		target.setStatusDisplay(source.getStatusDisplay());
		target.setType(source.getType());
		final CurrencyModel currency = source.getCurrency();
		if (null != currency)
		{
			target.setCurrencySymbol(currency.getSymbol());
		}
	}


	//For Payment
	protected void addPaymentDetails(final OrderModel source, final OrderData target)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);

		final MplPaymentInfoData mplPaymentInfo = new MplPaymentInfoData();
		final EMITermRateData emiInfo = new EMITermRateData();

		// Commenting  Deeply nested if..then statements are hard to read
		// No need to check extra null for x where x instanceof y as instnace of internally handles it

		//if (null != source.getPaymentInfo()) // SONAR Commenting  Deeply nested if..then statements are hard to read
		//{
		if (source.getPaymentInfo() instanceof EMIPaymentInfoModel)
		{
			final EMIPaymentInfoModel emiPaymentInfoModel = (EMIPaymentInfoModel) source.getPaymentInfo();
			if (null != emiPaymentInfoModel.getInterestPaid())
			{
				emiInfo.setInterestPayable(emiPaymentInfoModel.getInterestPaid().toString());
			}
			if (null != emiPaymentInfoModel.getInterest())
			{
				emiInfo.setInterestRate(emiPaymentInfoModel.getInterest().toString());
			}
			if (null != emiPaymentInfoModel.getMonthlyInstallment())
			{
				emiInfo.setMonthlyInstallment(emiPaymentInfoModel.getMonthlyInstallment().toString());
			}
			emiInfo.setTerm(emiPaymentInfoModel.getTermSelected());
			mplPaymentInfo.setEmiInfo(emiInfo);
			mplPaymentInfo.setCardAccountHolderName(emiPaymentInfoModel.getCcOwner());
			mplPaymentInfo.setCardIssueNumber(emiPaymentInfoModel.getNumber());
			if (null != emiPaymentInfoModel.getType())
			{
				mplPaymentInfo.setCardCardType(emiPaymentInfoModel.getType().toString());
			}
			mplPaymentInfo.setCardExpirationMonth(Integer.valueOf(emiPaymentInfoModel.getValidToMonth()));
			mplPaymentInfo.setCardExpirationYear(Integer.valueOf(emiPaymentInfoModel.getValidToYear()));
			mplPaymentInfo.setPaymentOption("EMI");
			mplPaymentInfo.setBillingAddress(getAddressConverter().convert(emiPaymentInfoModel.getBillingAddress()));
			if (null != emiPaymentInfoModel.getBankSelected() && null != emiPaymentInfoModel.getBankSelected().getName()
					&& StringUtils.isNotEmpty(emiPaymentInfoModel.getBankSelected().getName().getBankName()))
			{
				mplPaymentInfo.setBank(emiPaymentInfoModel.getBankSelected().getName().getBankName());
			}
			target.setMplPaymentInfo(mplPaymentInfo);
		}
		if (source.getPaymentInfo() instanceof CreditCardPaymentInfoModel)
		{
			final CreditCardPaymentInfoModel ccPaymentInfoModel = (CreditCardPaymentInfoModel) source.getPaymentInfo();
			mplPaymentInfo.setCardAccountHolderName(ccPaymentInfoModel.getCcOwner());
			mplPaymentInfo.setCardIssueNumber(ccPaymentInfoModel.getNumber());
			if (null != ccPaymentInfoModel.getType())
			{
				mplPaymentInfo.setCardCardType(ccPaymentInfoModel.getType().toString());
			}
			mplPaymentInfo.setCardExpirationMonth(Integer.valueOf(ccPaymentInfoModel.getValidToMonth()));
			mplPaymentInfo.setCardExpirationYear(Integer.valueOf(ccPaymentInfoModel.getValidToYear()));
			mplPaymentInfo.setPaymentOption("Credit Card");
			mplPaymentInfo.setBillingAddress(getAddressConverter().convert(ccPaymentInfoModel.getBillingAddress()));
			target.setMplPaymentInfo(mplPaymentInfo);
		}
		if (source.getPaymentInfo() instanceof DebitCardPaymentInfoModel)
		{
			final DebitCardPaymentInfoModel dcPaymentInfoModel = (DebitCardPaymentInfoModel) source.getPaymentInfo();
			mplPaymentInfo.setCardAccountHolderName(dcPaymentInfoModel.getCcOwner());
			mplPaymentInfo.setCardIssueNumber(dcPaymentInfoModel.getNumber());
			if (null != dcPaymentInfoModel.getType())
			{
				mplPaymentInfo.setCardCardType(dcPaymentInfoModel.getType().toString());
			}
			mplPaymentInfo.setCardExpirationMonth(Integer.valueOf(dcPaymentInfoModel.getValidToMonth()));
			mplPaymentInfo.setCardExpirationYear(Integer.valueOf(dcPaymentInfoModel.getValidToYear()));
			mplPaymentInfo.setPaymentOption("Debit Card");
			target.setMplPaymentInfo(mplPaymentInfo);
		}
		if (source.getPaymentInfo() instanceof NetbankingPaymentInfoModel)
		{
			final NetbankingPaymentInfoModel nbPaymentInfoModel = (NetbankingPaymentInfoModel) source.getPaymentInfo();
			mplPaymentInfo.setCardAccountHolderName(nbPaymentInfoModel.getBankOwner());
			mplPaymentInfo.setPaymentOption("Netbanking");
			if (null != nbPaymentInfoModel.getBank() && null != nbPaymentInfoModel.getBank().getName()
					&& StringUtils.isNotEmpty(nbPaymentInfoModel.getBank().getName().getBankName()))
			{
				mplPaymentInfo.setBank(nbPaymentInfoModel.getBank().getName().getBankName());
			}
			target.setMplPaymentInfo(mplPaymentInfo);
		}
		if (source.getPaymentInfo() instanceof CODPaymentInfoModel)
		{
			final CODPaymentInfoModel codPaymentInfoModel = (CODPaymentInfoModel) source.getPaymentInfo();
			mplPaymentInfo.setCardAccountHolderName(codPaymentInfoModel.getCashOwner());
			mplPaymentInfo.setPaymentOption("COD");
			target.setMplPaymentInfo(mplPaymentInfo);
		}

		//Added for third Party Wallet

		if (source.getPaymentInfo() instanceof ThirdPartyWalletInfoModel)
		{
			final ThirdPartyWalletInfoModel tpWalletPaymentInfoModel = (ThirdPartyWalletInfoModel) source.getPaymentInfo();
			mplPaymentInfo.setCardAccountHolderName(tpWalletPaymentInfoModel.getWalletOwner());

			//To change the name of payment option later
			mplPaymentInfo.setPaymentOption(tpWalletPaymentInfoModel.getProviderName());
			//mplPaymentInfo.setBillingAddress(getAddressConverter().convert(tpWalletPaymentInfoModel.getBillingAddress()));
			target.setMplPaymentInfo(mplPaymentInfo);
		}
		//Ended here for third party wallet

		//}
	}

	private void addDeliveryModePromotion(final AbstractOrderModel source, final AbstractOrderData target)
	{
		Double discountedPrice = Double.valueOf(target.getTotalDiscounts().getValue().doubleValue());
		boolean deliveryCostPromotionApplied = false;

		for (final AbstractOrderEntryModel entryModel : source.getEntries())
		{
			for (final OrderEntryData entryData : target.getEntries())
			{
				if (entryModel.getSelectedUSSID() != null && entryData.getSelectedUssid() != null
						&& entryModel.getCurrDelCharge() != null && entryModel.getPrevDelCharge() != null
						&& entryModel.getSelectedUSSID().equalsIgnoreCase(entryData.getSelectedUssid()))
				{
					final Double prevDeliveryCost = entryModel.getPrevDelCharge();
					final Double currDeliveryCost = entryModel.getCurrDelCharge();
					final Double deliveryCostDisc = Double.valueOf(prevDeliveryCost.doubleValue() - currDeliveryCost.doubleValue());

					if (deliveryCostDisc.doubleValue() > 0)
					{
						discountedPrice = Double.valueOf(discountedPrice.doubleValue() + deliveryCostDisc.doubleValue());
						deliveryCostPromotionApplied = true;
					}
				}
			}
		}

		if (discountedPrice.doubleValue() > 0 && deliveryCostPromotionApplied)
		{
			final BigDecimal totalDiscount = new BigDecimal(discountedPrice.doubleValue());
			final PriceData cartTotalDiscount = getPriceDataFactory().create(PriceDataType.BUY, totalDiscount,
					MarketplacecommerceservicesConstants.INR);
			target.setTotalDiscounts(cartTotalDiscount);
		}

	}


	@Override
	protected double getOrderDiscountsAmount(final AbstractOrderModel source)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);

		double discounts = 0.0d;

		final List<AbstractOrderEntryModel> entryList = new ArrayList<>(source.getEntries());

		if (CollectionUtils.isNotEmpty(entryList))
		{
			for (final AbstractOrderEntryModel entry : entryList)
			{
				final Double cartDiscount = (null != entry.getCartLevelDisc() && entry.getCartLevelDisc().doubleValue() > 0)
						? entry.getCartLevelDisc() : Double.valueOf(0);
				final Double cartCouponDiscount = (null != entry.getCartCouponValue() && entry.getCartCouponValue().doubleValue() > 0)
						? entry.getCartCouponValue() : Double.valueOf(0);

				discounts += cartDiscount.doubleValue() + cartCouponDiscount.doubleValue();
			}
		}


		return discounts;
	}



	private void addVoucherDiscount(final OrderModel source, final OrderData target)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);

		double userCouponDiscounts = 0.0d;
		double cartCouponDiscounts = 0.0d;

		final List<DiscountValue> discountList = source.getGlobalDiscountValues(); // discounts on the cart itself
		final List<DiscountModel> voucherList = source.getDiscounts();
		if (CollectionUtils.isNotEmpty(discountList))
		{
			for (final DiscountValue discount : discountList)
			{
				for (final DiscountModel voucher : voucherList)
				{
					if (discount.getCode().equalsIgnoreCase(voucher.getCode()))
					{

						final double value = discount.getAppliedValue();
						if ((voucher instanceof PromotionVoucherModel) && !(voucher instanceof MplCartOfferVoucherModel))
						{
							if (value > 0.0d)
							{
								userCouponDiscounts += value;
							}
						}
						else if (voucher instanceof MplCartOfferVoucherModel)
						{
							if (value > 0.0d)
							{
								cartCouponDiscounts += value;
							}
						}


					}
				}

			}
		}

		target.setCouponDiscount(createPrice(source, Double.valueOf(userCouponDiscounts)));

		target.setCartCouponDiscount(createPrice(source, Double.valueOf(cartCouponDiscounts)));

	}

	private void addPickupPersonDetails(final OrderModel source, final OrderData target)
	{
		target.setPickupName(source.getPickupPersonName());
		target.setPickupPhoneNumber(source.getPickupPersonMobile());
	}

	private void addDeliverryAddressList(final OrderModel source, final OrderData target)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);

		final List<AddressData> addressDataList = new ArrayList<AddressData>();

		final Collection<AddressModel> addressModelListsource = source.getUser().getAddresses();
		if (addressModelListsource != null)
		{
			for (final AddressModel addressModel : addressModelListsource)
			{
				final AddressData addressData = getAddressConverter().convert(addressModel);
				addressDataList.add(addressData);
			}
		}
		target.setDeliveryAddressList(addressDataList);
	}
}
