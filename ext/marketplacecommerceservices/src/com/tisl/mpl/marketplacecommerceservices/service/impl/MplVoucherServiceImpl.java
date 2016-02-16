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
	 * @Description This method recalculates the cart after redeeming/releasing the voucher *
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
				if (null != cartEntryModel.getGiveAway() && !cartEntryModel.getGiveAway().booleanValue())
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
		cartModel.setTotalPrice(Double.valueOf((null != cartModel.getTotalPrice() ? cartModel.getTotalPrice().doubleValue() : 0.0d)
				+ (null != deliveryCost ? deliveryCost.doubleValue() : 0.0d)));

		// Freebie item changes
		getMplCommerceCartServiceImpl().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap);

		LOG.debug("Step 6:::Recalculation done successfully");

		getModelService().save(cartModel);

	}




	/**
	 * @Description Checks the cart after applying the voucher
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
		LOG.debug("Step 7:::Inside checking cart after applying voucher");
		//Total amount in cart updated with delay... Calculating value of voucher regarding to order
		final double cartSubTotal = cartModel.getSubtotal().doubleValue();
		double voucherCalcValue = 0.0;
		double promoCalcValue = 0.0;
		List<DiscountValue> discountList = cartModel.getGlobalDiscountValues(); //Discount values against the cart
		final String voucherCode = ((PromotionVoucherModel) lastVoucher).getVoucherCode();
		VoucherDiscountData discountData = new VoucherDiscountData();

		final List<DiscountModel> voucherList = cartModel.getDiscounts(); //List of discounts against the cart
		if (CollectionUtils.isNotEmpty(discountList) && CollectionUtils.isNotEmpty(voucherList))
		{
			for (final DiscountValue discount : discountList)
			{
				if (discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode())) //Only 1 voucher can be applied and code is mandatory field
				{
					voucherCalcValue = discount.getValue();
				}
				else
				{
					promoCalcValue = discount.getValue();
				}
			}
		}

		final StringBuilder sb = new StringBuilder();
		LOG.debug(sb.append("Step 8:::Voucher discount in cart is ").append(voucherCalcValue)
				.append(" & promo discount in cart is ").append(promoCalcValue));

		if (!lastVoucher.getAbsolute().booleanValue() && voucherCalcValue != 0 && null != lastVoucher.getMaxDiscountValue()
				&& voucherCalcValue > lastVoucher.getMaxDiscountValue().doubleValue()) //When discount value is greater than coupon max discount value
		{
			LOG.debug("Step 11:::Inside max discount block");
			discountList = setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher, lastVoucher
					.getMaxDiscountValue().doubleValue());
			cartModel.setGlobalDiscountValues(discountList);
			getMplDefaultCalculationService().calculateTotals(cartModel, false);
			getModelService().save(cartModel);

			discountData.setCouponDiscount(discountUtility.createPrice(cartModel,
					Double.valueOf(lastVoucher.getMaxDiscountValue().doubleValue())));
		}

		else if (voucherCalcValue != 0 && (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0) //When discount value is greater than cart totals after applying promotion
		{
			LOG.debug("Step 12:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
			discountData = releaseVoucherAfterCheck(cartModel, voucherCode, null, applicableOrderEntryList, voucherList);
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
					netAmountAfterAllDisc += ((null != entry.getProductPromoCode() && StringUtils.isNotEmpty(entry
							.getProductPromoCode())) || (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry
							.getCartPromoCode()))) ? entry.getNetAmountAfterAllDisc().doubleValue() : entry.getTotalPrice()
							.doubleValue();

					productPrice += entry.getTotalPrice().doubleValue();
				}

				LOG.debug("Step 14:::netAmountAfterAllDisc is " + netAmountAfterAllDisc + " & productPrice is " + productPrice);


				if ((productPrice < 1) || (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)) //When discount value is greater than entry totals after applying promotion
				{
					LOG.debug("Step 15:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
					discountData = releaseVoucherAfterCheck(cartModel, voucherCode, Double.valueOf(productPrice),
							applicableOrderEntryList, voucherList);
				}
				else
				{
					discountData.setCouponDiscount(discountUtility.createPrice(cartModel, Double.valueOf(voucherCalcValue)));
				}
			}
			else if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList)) //When applicable entries are empty
			{
				LOG.debug("Step 13,14,15/1:::applicable entries empty");
				discountData = releaseVoucherAfterCheck(cartModel, voucherCode, null, applicableOrderEntryList, voucherList);
			}
			else
			{
				discountData.setCouponDiscount(discountUtility.createPrice(cartModel, Double.valueOf(voucherCalcValue)));
			}
		}

		return discountData;
	}


	/**
	 * @Description This is for releasing voucher
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
		releaseVoucher(voucherCode, cartModel); //Releases voucher
		recalculateCartForCoupon(cartModel); //Recalculates cart after releasing voucher
		getModelService().save(cartModel);

		String msg = null;

		discountData.setCouponDiscount(discountUtility.createPrice(cartModel, Double.valueOf(0)));
		if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList))
		{
			msg = "not_applicable";
		}
		else if (null != productPrice && productPrice.doubleValue() < 1)
		{
			msg = "freebie";
		}
		else
		{
			msg = "Price_exceeded";
		}

		discountData.setRedeemErrorMsg(msg);
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
			applicableOrderList.add(((VoucherEntry) iter.next()).getOrderEntry());
		}

		return applicableOrderList;
	}



	/**
	 * @Description This method adds global discount
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

		final Iterator iter = discountList.iterator();

		//Remove the existing discount and add a new discount row
		while (iter.hasNext())
		{
			final DiscountValue discount = (DiscountValue) iter.next();
			if (CollectionUtils.isNotEmpty(voucherList) && discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
			{
				discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discount.getCurrencyIsoCode());

				iter.remove();
				break;
			}
		}

		//need to remove code review
		//		for (final DiscountValue discount : discountList)
		//		{
		//			if (CollectionUtils.isNotEmpty(voucherList) && discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
		//			{
		//				discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discount.getCurrencyIsoCode());
		//
		//				discountList.remove(discount);
		//				break;
		//			}
		//		}
		discountList.add(discountValue);
		return discountList;
	}


	/**
	 * @Description This is for releasing voucher
	 * @param cartModel
	 * @param voucherCode
	 * @throws VoucherOperationException
	 */
	@Override
	public void releaseVoucher(final String voucherCode, final CartModel cartModel) throws VoucherOperationException
	{
		LOG.debug("Step 2:::Inside releaseVoucher");
		validateVoucherCodeParameter(voucherCode);
		final VoucherModel voucher = getVoucherService().getVoucher(voucherCode); //Finds voucher for the selected code
		if (voucher == null)
		{
			throw new VoucherOperationException("Voucher not found: " + voucherCode);
		}
		else if (cartModel != null)
		{
			LOG.debug("Step 3:::Voucher and cart is not null");
			try
			{
				getVoucherService().releaseVoucher(voucherCode, cartModel); //Releases the voucher from the cart
				LOG.debug("Step 4:::Voucher released");
				final List<AbstractOrderEntryModel> entryList = new ArrayList<AbstractOrderEntryModel>();
				for (final AbstractOrderEntryModel entry : getOrderEntryModelFromVouEntries(voucher, cartModel))//Resets the coupon details against the entries
				{
					entry.setCouponCode("");
					entry.setCouponValue(Double.valueOf(0.00D));
					entryList.add(entry);
				}
				if (CollectionUtils.isNotEmpty(entryList))
				{
					getModelService().saveAll(entryList);
				}

				LOG.debug("Step 5:::CouponCode, CouponValue  resetted");
			}
			catch (final JaloPriceFactoryException e)
			{
				throw new VoucherOperationException("Couldn't release voucher: " + voucherCode);
			}
		}
	}



	/**
	 * Validates the voucher code parameter
	 *
	 * @param voucherCode
	 */
	protected void validateVoucherCodeParameter(final String voucherCode)
	{
		if (StringUtils.isBlank(voucherCode))
		{
			throw new IllegalArgumentException("Parameter voucherCode must not be empty");
		}
	}


	/**
	 * @Description For getting list of applicable AbstractOrderEntryModel from voucherEntrySet
	 * @param voucherModel
	 * @param cartModel
	 * @return public List<AbstractOrderEntryModel>
	 */
	@Override
	public List<AbstractOrderEntryModel> getOrderEntryModelFromVouEntries(final VoucherModel voucherModel,
			final CartModel cartModel)
	{
		LOG.debug("Step 11:::Inside getOrderEntryModelFromVouEntries");

		final List<AbstractOrderEntry> orderEntryList = getOrderEntriesFromVoucherEntries(voucherModel, cartModel);

		final List<AbstractOrderEntryModel> applicableOrderList = new ArrayList<AbstractOrderEntryModel>();

		for (final AbstractOrderEntry entry : orderEntryList) //Converts applicable order entries from AbstractOrderEntry to AbstractOrderEntryModel
		{
			applicableOrderList.add((AbstractOrderEntryModel) getModelService().get(entry));
		}

		return applicableOrderList;
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
		LOG.debug("Step 16:::Inside setApportionedValueForVoucher");
		if (CollectionUtils.isNotEmpty(cartModel.getDiscounts()))
		{
			final Voucher voucherObj = (Voucher) getModelService().getSource(voucher);

			double totalApplicablePrice = 0.0D;
			BigDecimal percentageDiscount = null;

			for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
			{
				totalApplicablePrice += entry.getTotalPrice() != null ? entry.getTotalPrice().doubleValue() : 0.0D;
			}

			final double discountValue = voucherObj.getValueAsPrimitive();

			if (voucherObj.isAbsoluteAsPrimitive())
			{
				percentageDiscount = BigDecimal.valueOf(discountValue / totalApplicablePrice).multiply(
						BigDecimal.valueOf(Long.parseLong(MarketplacecommerceservicesConstants.HUNDRED)));//TODo 100 in variable
			}
			else
			{
				percentageDiscount = BigDecimal.valueOf(discountValue);
				final double totalSavings = (totalApplicablePrice * percentageDiscount.doubleValue())
						/ Long.parseLong(MarketplacecommerceservicesConstants.HUNDRED);
				final double totalMaxDiscount = voucher.getMaxDiscountValue() != null ? voucher.getMaxDiscountValue().doubleValue()
						: 0.0D;

				if (totalMaxDiscount != 0.0D && totalSavings > totalMaxDiscount)
				{
					percentageDiscount = BigDecimal.valueOf(voucher.getMaxDiscountValue().doubleValue() / totalApplicablePrice)
							.multiply(BigDecimal.valueOf(Long.parseLong(MarketplacecommerceservicesConstants.HUNDRED)));

				}

			}

			LOG.debug("Step 17:::percentageDiscount is " + percentageDiscount);

			double totalAmtDeductedOnItemLevel = 0.00D;

			updateAppOrderEntriesForFreebieBogo(applicableOrderEntryList);

			for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
			{
				BigDecimal entryLevelApportionedPrice = null;
				double currNetAmtAftrAllDisc = 0.00D;

				final double entryTotalPrice = entry.getTotalPrice().doubleValue();

				if (entryTotalPrice > 1) //For freebie & bogo, 0.01 priced product, isBogoApplied flag can't be checked as same product might be free and non free for BOGO
				{
					if (voucherObj.isAbsoluteAsPrimitive())
					{
						if (applicableOrderEntryList.indexOf(entry) == (applicableOrderEntryList.size() - 1))
						{
							final BigDecimal discountPriceValue = BigDecimal.valueOf(discountValue);
							entryLevelApportionedPrice = getApportionedValueForSingleEntry(discountPriceValue,
									totalAmtDeductedOnItemLevel);
						}
						else
						{
							entryLevelApportionedPrice = getApportionedValueForEntry(percentageDiscount, entryTotalPrice);
							totalAmtDeductedOnItemLevel += entryLevelApportionedPrice.doubleValue();
						}
					}
					else
					{
						entryLevelApportionedPrice = getApportionedValueForEntry(percentageDiscount, entryTotalPrice);
					}

					LOG.debug("Step 18:::entryLevelApportionedPrice is " + entryLevelApportionedPrice);

					entry.setCouponCode(null != voucherCode ? voucherCode : voucher.getCode());
					entry.setCouponValue(Double.valueOf(entryLevelApportionedPrice.doubleValue()));

					if ((null != entry.getProductPromoCode() && !entry.getProductPromoCode().isEmpty())
							|| (null != entry.getCartPromoCode() && !entry.getCartPromoCode().isEmpty()))
					{
						final double netAmtAftrAllDisc = entry.getNetAmountAfterAllDisc() != null ? entry.getNetAmountAfterAllDisc()
								.doubleValue() : 0.00D;
						currNetAmtAftrAllDisc = getCurrNetAmtAftrAllDisc(netAmtAftrAllDisc, entryLevelApportionedPrice);
					}
					else
					{
						currNetAmtAftrAllDisc = getCurrNetAmtAftrAllDisc(entryTotalPrice, entryLevelApportionedPrice);
					}
					LOG.debug("Step 19:::currNetAmtAftrAllDisc is " + currNetAmtAftrAllDisc);

					entry.setNetAmountAfterAllDisc(Double.valueOf(currNetAmtAftrAllDisc));
					//getModelService().save(entry);
				}
			}

			getModelService().saveAll(applicableOrderEntryList);//TODO save outside loop
		}


	}

	/**
	 * @Descriotion This is for getting apportioned value for a single entry
	 * @param discountPriceValue
	 * @param totalAmtDeductedOnItemLevel
	 * @return BigDecimal
	 */

	private BigDecimal getApportionedValueForSingleEntry(final BigDecimal discountPriceValue,
			final double totalAmtDeductedOnItemLevel)
	{

		return discountPriceValue.subtract(BigDecimal.valueOf(totalAmtDeductedOnItemLevel));
	}


	/**
	 * @Descriotion This is for getting apportioned value for entry
	 * @param percentageDiscount
	 * @param entryTotalPrice
	 * @return BigDecimal
	 */
	BigDecimal getApportionedValueForEntry(final BigDecimal percentageDiscount, final double entryTotalPrice)
	{
		return (percentageDiscount.divide(BigDecimal.valueOf(Long.parseLong(MarketplacecommerceservicesConstants.HUNDRED))))
				.multiply(BigDecimal.valueOf(entryTotalPrice));
	}


	/**
	 * @Descriotion This is for getting apportioned value for entry
	 * @param percentageDiscount
	 * @param entryTotalPrice
	 * @return BigDecimal
	 */
	double getCurrNetAmtAftrAllDisc(final double amount, final BigDecimal entryLevelApportionedPrice)
	{
		return amount > entryLevelApportionedPrice.doubleValue() ? (amount - entryLevelApportionedPrice.doubleValue()) : Double
				.parseDouble(MarketplacecommerceservicesConstants.ZEROPOINTZEROONE);
	}


	/**
	 * @Descriotion This is for checking total items in cart
	 * @param applicableOrderEntryList
	 * @return boolean
	 */
	//	private boolean checkTotalIteminCart(final List<AbstractOrderEntryModel> applicableOrderEntryList)
	//	{
	//		boolean flag = false;
	//		int count = 0;
	//		for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
	//		{
	//			if (null != entry.getTotalPrice() && entry.getTotalPrice().doubleValue() > 1)
	//			{
	//				count++;
	//			}
	//		}
	//		if (count < 2)
	//		{
	//			flag = true;
	//		}
	//		return flag;
	//	}


	private void updateAppOrderEntriesForFreebieBogo(final List<AbstractOrderEntryModel> applicableOrderEntryList)
	{
		final Iterator iterator = applicableOrderEntryList.iterator();
		while (iterator.hasNext())
		{
			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) iterator.next();
			if (null != entry.getTotalPrice() && entry.getTotalPrice().doubleValue() < 1)
			{
				iterator.remove();
			}
		}
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
