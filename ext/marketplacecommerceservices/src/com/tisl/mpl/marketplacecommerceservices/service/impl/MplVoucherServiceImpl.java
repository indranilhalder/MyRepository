/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.order.impl.MplDefaultCalculationService;
import com.tisl.mpl.util.DiscountUtility;


/**
 * @author TCS
 *
 */
public class MplVoucherServiceImpl implements MplVoucherService
{
	private static final Logger LOG = Logger.getLogger(MplVoucherServiceImpl.class);

	//@Autowired
	//private CommerceCartService commerceCartService;
	@Autowired
	private MplDefaultCalculationService mplDefaultCalculationService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private VoucherModelService voucherModelService;
	@Autowired
	private MplCommerceCartServiceImpl mplCommerceCartServiceImpl;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private MplCommerceCartCalculationStrategy mplCommerceCartCalculationStrategy;
	@Autowired
	private DiscountUtility discountUtility;



	/**
	 * This method recalculates the cart after redeeming/releasing the voucher
	 *
	 * @param cartModel
	 *
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void recalculateCartForCoupon(final CartModel cartModel) throws JaloPriceFactoryException, CalculationException
	{
		LOG.debug("Step 5:::Inside recalculating cart after voucher apply");
		final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
		final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

		if (cartModel != null && cartModel.getEntries() != null)
		{
			for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
			{
				if (cartEntryModel != null && null != cartEntryModel.getGiveAway() && !cartEntryModel.getGiveAway().booleanValue()
						&& cartEntryModel.getSelectedUSSID() != null)
				{
					freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
					freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
				}
			}
		}

		//recalculating cart
		final Double deliveryCost = cartModel.getDeliveryCost();
		//getCommerceCartService().recalculateCart(cartModel);

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		getMplCommerceCartCalculationStrategy().recalculateCart(parameter);

		cartModel.setDeliveryCost(deliveryCost);
		cartModel.setTotalPrice(Double.valueOf(cartModel.getTotalPrice().doubleValue() + deliveryCost.doubleValue()));

		// Freebie item changes
		getMplCommerceCartServiceImpl().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap);

		LOG.debug("Step 6:::Recalculation done successfully");

		getModelService().save(cartModel);

	}




	/**
	 * Checks the cart after applying the voucher
	 *
	 * @param lastVoucher
	 * @param cartModel
	 * @throws ModelSavingException
	 * @throws VoucherOperationException
	 * @throws CalculationException
	 * @throws JaloSecurityException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 * @throws JaloPriceFactoryException
	 */
	@Override
	public VoucherDiscountData checkCartAfterApply(final VoucherModel lastVoucher, final CartModel cartModel,
			final List<AbstractOrderEntryModel> applicableOrderEntryList) throws ModelSavingException, VoucherOperationException,
			CalculationException, NumberFormatException, JaloInvalidParameterException, JaloSecurityException,
			JaloPriceFactoryException
	{
		final long startTime = System.currentTimeMillis();
		LOG.debug("Step 7:::Inside checking cart after applying voucher");
		//Total amount in cart updated with delay... Calculating value of voucher regarding to order
		final double cartSubTotal = cartModel.getSubtotal().doubleValue();
		double voucherCalcValue = 0.0;
		double promoCalcValue = 0.0;
		List<DiscountValue> discountList = cartModel.getGlobalDiscountValues();
		final String voucherCode = ((PromotionVoucherModel) lastVoucher).getVoucherCode();
		final VoucherDiscountData discountData = new VoucherDiscountData();

		final List<DiscountModel> voucherList = cartModel.getDiscounts();
		if (CollectionUtils.isNotEmpty(discountList))
		{
			for (final DiscountValue discount : discountList)
			{
				if (CollectionUtils.isNotEmpty(voucherList) && discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
				{
					voucherCalcValue = discount.getValue();
				}
				else if (!discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
				{
					promoCalcValue = discount.getValue();
				}
			}
		}

		LOG.debug("Step 8:::Voucher discount in cart is " + voucherCalcValue + " & promo discount in cart is " + promoCalcValue);

		if (!lastVoucher.getAbsolute().booleanValue() && voucherCalcValue != 0 && null != lastVoucher.getMaxDiscountValue()
				&& voucherCalcValue > lastVoucher.getMaxDiscountValue().doubleValue())
		{
			LOG.debug("Step 11:::Inside max discount block");
			discountList = setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher, lastVoucher
					.getMaxDiscountValue().doubleValue());
			cartModel.setGlobalDiscountValues(discountList);
			getMplDefaultCalculationService().calculateTotals(cartModel, false);
			getModelService().save(cartModel);

			discountData.setCouponDiscount(discountUtility.createPrice(cartModel,
					Double.valueOf(lastVoucher.getMaxDiscountValue().doubleValue())));

			return discountData;
		}

		else if (voucherCalcValue != 0 && (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0)
		{
			LOG.debug("Step 12:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
			return releaseVoucherAfterCheck(cartModel, voucherCode, null, applicableOrderEntryList, voucherList);
		}

		else
		{
			double netAmountAfterAllDisc = 0.0D;
			double productPrice = 0.0D;
			//final boolean flag = false;

			if (CollectionUtils.isNotEmpty(applicableOrderEntryList))
			{
				LOG.debug("Step 13:::applicableOrderEntryList is not empty");
				for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
				{
					if ((null != entry.getProductPromoCode() && StringUtils.isNotEmpty(entry.getProductPromoCode()))
							|| (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry.getCartPromoCode())))
					{
						netAmountAfterAllDisc += entry.getNetAmountAfterAllDisc().doubleValue();
					}
					else
					{
						netAmountAfterAllDisc += entry.getTotalPrice().doubleValue();
					}

					productPrice += entry.getTotalPrice().doubleValue();
				}

				LOG.debug("Step 14:::netAmountAfterAllDisc is " + netAmountAfterAllDisc + " & productPrice is " + productPrice);


				if ((productPrice < 1) || (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0))
				{
					final long endTime = System.currentTimeMillis();
					LOG.debug("Exiting service checkCartAfterApply====== " + (endTime - startTime));
					LOG.debug("Step 15:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
					return releaseVoucherAfterCheck(cartModel, voucherCode, Double.valueOf(productPrice), applicableOrderEntryList,
							voucherList);
				}
			}

			else if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList))
			{
				LOG.debug("Step 13,14,15/1:::applicable entries empty");
				final long endTime = System.currentTimeMillis();
				LOG.debug("Exiting service checkCartAfterApply====== " + (endTime - startTime));
				return releaseVoucherAfterCheck(cartModel, voucherCode, null, applicableOrderEntryList, voucherList);
			}

			discountData.setCouponDiscount(discountUtility.createPrice(cartModel, Double.valueOf(voucherCalcValue)));
			final long endTime = System.currentTimeMillis();
			LOG.debug("Exiting service checkCartAfterApply====== " + (endTime - startTime));
			return discountData;
		}

		//return discountData;

	}


	/**
	 *
	 * @param cartModel
	 * @param voucherCode
	 * @return VoucherDiscountData
	 * @throws VoucherOperationException
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 */
	private VoucherDiscountData releaseVoucherAfterCheck(final CartModel cartModel, final String voucherCode,
			final Double productPrice, final List<AbstractOrderEntryModel> applicableOrderEntryList,
			final List<DiscountModel> voucherList) throws VoucherOperationException, JaloPriceFactoryException, CalculationException
	{
		final VoucherDiscountData discountData = new VoucherDiscountData();
		releaseVoucher(voucherCode, cartModel);
		recalculateCartForCoupon(cartModel);
		//mplDefaultCalculationService.calculateTotals(cartModel, false);
		getModelService().save(cartModel);

		discountData.setCouponDiscount(discountUtility.createPrice(cartModel, Double.valueOf(0)));
		if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList))
		{
			discountData.setRedeemErrorMsg("not_applicable");
		}
		else if (null != productPrice && productPrice.doubleValue() < 1)
		{
			discountData.setRedeemErrorMsg("freebie");
		}
		else
		{
			discountData.setRedeemErrorMsg("Price_exceeded");
		}

		return discountData;
	}



	/**
	 * @Description: For getting list of applicable AbstractOrderEntry from voucherEntrySet
	 * @param voucherModel
	 * @param cartModel
	 * @return list of applicable AbstractOrderEntry
	 */
	@Override
	public List<AbstractOrderEntry> getOrderEntriesFromVoucherEntries(final VoucherModel voucherModel, final CartModel cartModel)
	{
		LOG.debug("Step 9:::Inside getOrderEntriesFromVoucherEntries");
		final VoucherEntrySet voucherEntrySet = getVoucherModelService().getApplicableEntries(voucherModel, cartModel);
		final Iterator iter = voucherEntrySet.iterator();
		final List<AbstractOrderEntry> applicableOrderList = new ArrayList<AbstractOrderEntry>();

		while (iter.hasNext())
		{
			final VoucherEntry voucherEntry = (VoucherEntry) iter.next();
			final AbstractOrderEntry entry = voucherEntry.getOrderEntry();

			LOG.debug("Step 10:::applicable entry ===" + entry);

			applicableOrderList.add(entry);

		}

		return applicableOrderList;
	}



	/**
	 * This method adds global discount
	 *
	 * @param discountList
	 * @param voucherList
	 * @param cartSubTotal
	 * @param promoCalcValue
	 * @param lastVoucher
	 * @param discountAmt
	 * @return List<DiscountValue>
	 */
	@Override
	public List<DiscountValue> setGlobalDiscount(final List<DiscountValue> discountList, final List<DiscountModel> voucherList,
			final double cartSubTotal, final double promoCalcValue, final VoucherModel lastVoucher, final double discountAmt)
	{
		DiscountValue discountValue = null;
		for (final DiscountValue discount : discountList)
		{
			if (CollectionUtils.isNotEmpty(voucherList) && discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
			{
				discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discount.getCurrencyIsoCode());

				discountList.remove(discount);
				break;
			}
		}
		discountList.add(discountValue);
		return discountList;
	}


	@Override
	public void releaseVoucher(final String voucherCode, final CartModel cartModel) throws VoucherOperationException
	{
		LOG.debug("Step 2:::Inside releaseVoucher");
		final VoucherModel voucher = getVoucherModel(voucherCode);
		if (null != voucher && cartModel != null)
		{
			LOG.debug("Step 3:::Voucher and cart is not null");
			try
			{
				getVoucherService().releaseVoucher(voucherCode, cartModel);
				LOG.debug("Step 4:::Voucher released");
				for (final AbstractOrderEntryModel entry : getOrderEntryModelFromVouEntries(voucher, cartModel))//cartModel.getEntries()
				{
					entry.setCouponCode("");
					entry.setCouponValue(Double.valueOf(0.00D));
					getModelService().save(entry);
				}

				LOG.debug("Step 5:::CouponCode, CouponValue  resetted");
				return;
			}
			catch (final JaloPriceFactoryException e)
			{
				throw new VoucherOperationException("Couldn't release voucher: " + voucherCode);
			}
		}
	}


	/**
	 *
	 * @param voucherModel
	 * @param cartModel
	 * @return public List<AbstractOrderEntryModel>
	 */
	@Override
	public List<AbstractOrderEntryModel> getOrderEntryModelFromVouEntries(final VoucherModel voucherModel,
			final CartModel cartModel)
	{
		final long startTime = System.currentTimeMillis();
		LOG.debug("Step 11:::Inside getOrderEntryModelFromVouEntries");

		final List<AbstractOrderEntry> orderEntryList = getOrderEntriesFromVoucherEntries(voucherModel, cartModel);

		final Iterator iter = orderEntryList.iterator();
		final List<AbstractOrderEntryModel> applicableOrderList = new ArrayList<AbstractOrderEntryModel>();

		while (iter.hasNext())
		{
			final AbstractOrderEntryModel entryModel = ((AbstractOrderEntryModel) getModelService().get(iter.next()));

			LOG.debug("Step 12:::applicable entry ===" + entryModel);

			applicableOrderList.add(entryModel);

		}

		final long endTime = System.currentTimeMillis();
		LOG.debug("Exiting service getOrderEntryModelFromVouEntries====== " + (endTime - startTime));
		return applicableOrderList;
	}


	/**
	 * Returns voucher model from the voucher code
	 *
	 * @param voucherCode
	 * @return VoucherModel
	 * @throws VoucherOperationException
	 */
	@Override
	public VoucherModel getVoucherModel(final String voucherCode) throws VoucherOperationException
	{
		final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
		if (voucher == null)
		{
			throw new VoucherOperationException("Voucher not found: " + voucherCode);
		}
		return voucher;
	}



	/**
	 * @Description: For Apportioning of vouchers
	 * @param voucher
	 * @param cartModel
	 * @param voucherCode
	 */
	@Override
	public void setApportionedValueForVoucher(final VoucherModel voucher, final CartModel cartModel, final String voucherCode,
			final List<AbstractOrderEntryModel> applicableOrderEntryList)
	{
		final long startTime = System.currentTimeMillis();
		LOG.debug("Step 16:::Inside setApportionedValueForVoucher");
		if (CollectionUtils.isNotEmpty(cartModel.getDiscounts()))
		{
			final Voucher voucherObj = (Voucher) getModelService().getSource(voucher);

			double totalApplicablePrice = 0.0D;
			BigDecimal percentageDiscount = null;

			for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
			{
				totalApplicablePrice += entry.getTotalPrice().doubleValue();
			}

			final double discountValue = voucherObj.getValueAsPrimitive();

			if (voucherObj.isAbsoluteAsPrimitive())
			{
				percentageDiscount = BigDecimal.valueOf(discountValue / totalApplicablePrice).multiply(BigDecimal.valueOf(100));
			}
			else
			{
				percentageDiscount = BigDecimal.valueOf(discountValue);
				final double totalSavings = (totalApplicablePrice * percentageDiscount.doubleValue()) / 100;
				final double totalMaxDiscount = voucher.getMaxDiscountValue() != null ? voucher.getMaxDiscountValue().doubleValue()
						: 0.0D;

				if (totalMaxDiscount != 0.0D && totalSavings > totalMaxDiscount)
				{
					percentageDiscount = BigDecimal.valueOf(voucher.getMaxDiscountValue().doubleValue() / totalApplicablePrice)
							.multiply(BigDecimal.valueOf(100));

				}

			}

			LOG.debug("Step 17:::percentageDiscount is " + percentageDiscount);

			double totalAmtDeductedOnItemLevel = 0.00D;

			for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
			{
				BigDecimal entryLevelApportionedPrice = null;
				double currNetAmtAftrAllDisc = 0.00D;

				final double entryTotalPrice = entry.getTotalPrice().doubleValue();

				if (entryTotalPrice > 1) //For freebie & bogo, 0.01 priced product, isBogoApplied can't be checked as same product might be free and non free for BOGO
				{

					if (voucherObj.isAbsoluteAsPrimitive())
					{
						if (checkTotalIteminCart(applicableOrderEntryList))
						{
							entryLevelApportionedPrice = BigDecimal.valueOf(discountValue);
						}
						else
						{
							if (applicableOrderEntryList.indexOf(entry) == (applicableOrderEntryList.size() - 1))
							{
								//final BigDecimal discountPriceValue = (percentageDiscount.divide(BigDecimal.valueOf(100))).multiply(BigDecimal.valueOf(totalApplicablePrice));
								final BigDecimal discountPriceValue = BigDecimal.valueOf(discountValue);
								entryLevelApportionedPrice = discountPriceValue.subtract(BigDecimal.valueOf(totalAmtDeductedOnItemLevel));
							}
							else
							{
								entryLevelApportionedPrice = (percentageDiscount.divide(BigDecimal.valueOf(100))).multiply(BigDecimal
										.valueOf(entryTotalPrice));
								totalAmtDeductedOnItemLevel += entryLevelApportionedPrice.doubleValue();
							}
						}
					}
					else
					{
						entryLevelApportionedPrice = (percentageDiscount.divide(BigDecimal.valueOf(100))).multiply(BigDecimal
								.valueOf(entryTotalPrice));
					}

					LOG.debug("Step 18:::entryLevelApportionedPrice is " + entryLevelApportionedPrice);

					entry.setCouponCode(null != voucherCode ? voucherCode : voucher.getCode());
					entry.setCouponValue(Double.valueOf(entryLevelApportionedPrice.doubleValue()));

					if ((null != entry.getProductPromoCode() && !entry.getProductPromoCode().isEmpty())
							|| (null != entry.getCartPromoCode() && !entry.getCartPromoCode().isEmpty()))
					{
						final double netAmtAftrAllDisc = entry.getNetAmountAfterAllDisc() != null ? entry.getNetAmountAfterAllDisc()
								.doubleValue() : 0.00D;

						if (netAmtAftrAllDisc > entryLevelApportionedPrice.doubleValue())
						{
							currNetAmtAftrAllDisc = netAmtAftrAllDisc - entryLevelApportionedPrice.doubleValue();

						}
						else
						{
							currNetAmtAftrAllDisc = Double.parseDouble(MarketplacecommerceservicesConstants.ZEROPOINTZEROONE);
						}

					}
					else
					{
						if (entryTotalPrice > entryLevelApportionedPrice.doubleValue())
						{
							currNetAmtAftrAllDisc = entryTotalPrice - entryLevelApportionedPrice.doubleValue();

						}
						else
						{
							currNetAmtAftrAllDisc = Double.parseDouble(MarketplacecommerceservicesConstants.ZEROPOINTZEROONE);
						}

					}
					LOG.debug("Step 19:::currNetAmtAftrAllDisc is " + currNetAmtAftrAllDisc);

					entry.setNetAmountAfterAllDisc(Double.valueOf(currNetAmtAftrAllDisc));
					getModelService().save(entry);
				}
			}
		}

		final long endTime = System.currentTimeMillis();
		LOG.debug("Exiting service setApportionedValueForVoucher====== " + (endTime - startTime));
	}

	/**
	 * @param applicableOrderEntryList
	 * @return
	 */
	private boolean checkTotalIteminCart(final List<AbstractOrderEntryModel> applicableOrderEntryList)
	{
		// YTODO Auto-generated method stub
		int count = 0;
		for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
		{
			if (entry.getTotalPrice().doubleValue() > 1)
			{
				count++;
			}


		}
		if (count < 2)
		{
			return true;
		}
		return false;
	}



	/**
	 * @return the mplDefaultCalculationService
	 */
	public MplDefaultCalculationService getMplDefaultCalculationService()
	{
		return mplDefaultCalculationService;
	}




	/**
	 * @param mplDefaultCalculationService
	 *           the mplDefaultCalculationService to set
	 */
	public void setMplDefaultCalculationService(final MplDefaultCalculationService mplDefaultCalculationService)
	{
		this.mplDefaultCalculationService = mplDefaultCalculationService;
	}




	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}




	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}




	/**
	 * @return the voucherModelService
	 */
	public VoucherModelService getVoucherModelService()
	{
		return voucherModelService;
	}




	/**
	 * @param voucherModelService
	 *           the voucherModelService to set
	 */
	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
	}




	/**
	 * @return the mplCommerceCartServiceImpl
	 */
	public MplCommerceCartServiceImpl getMplCommerceCartServiceImpl()
	{
		return mplCommerceCartServiceImpl;
	}




	/**
	 * @param mplCommerceCartServiceImpl
	 *           the mplCommerceCartServiceImpl to set
	 */
	public void setMplCommerceCartServiceImpl(final MplCommerceCartServiceImpl mplCommerceCartServiceImpl)
	{
		this.mplCommerceCartServiceImpl = mplCommerceCartServiceImpl;
	}




	/**
	 * @return the voucherService
	 */
	public VoucherService getVoucherService()
	{
		return voucherService;
	}




	/**
	 * @param voucherService
	 *           the voucherService to set
	 */
	public void setVoucherService(final VoucherService voucherService)
	{
		this.voucherService = voucherService;
	}




	/**
	 * @return the mplCommerceCartCalculationStrategy
	 */
	public MplCommerceCartCalculationStrategy getMplCommerceCartCalculationStrategy()
	{
		return mplCommerceCartCalculationStrategy;
	}




	/**
	 * @param mplCommerceCartCalculationStrategy
	 *           the mplCommerceCartCalculationStrategy to set
	 */
	public void setMplCommerceCartCalculationStrategy(final MplCommerceCartCalculationStrategy mplCommerceCartCalculationStrategy)
	{
		this.mplCommerceCartCalculationStrategy = mplCommerceCartCalculationStrategy;
	}









}
