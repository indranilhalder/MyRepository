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
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.EMIPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.NetbankingPaymentInfoModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;

import java.math.BigDecimal;
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
									|| promotion instanceof BuyAandBGetPromotionOnShippingChargesModel || promotion instanceof BuyAboveXGetPromotionOnShippingChargesModel))
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

}
