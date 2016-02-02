package com.tisl.mpl.interceptor;

/**
 *
 */

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;


public class CartSaveInterceptor implements PrepareInterceptor
{

	private static final Logger LOG = Logger.getLogger(CartSaveInterceptor.class);

	@Autowired
	private MplVoucherService mplVoucherService;
	@Autowired
	private ModelService modelService;


	/**
	 * @Description : The Method checks the Promotion Priority prior to its creation from HMC
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{
		LOG.debug(Localization.getLocalizedString("payment.cartsaveinterceptor.message"));
		if (object instanceof CartModel)
		{
			final CartModel cartModel = (CartModel) object;

			//			//Coupon check
			//
			//			final double cartSubTotal = cartModel.getSubtotal().doubleValue();
			//			double voucherCalcValue = 0.0;
			//			double promoCalcValue = 0.0;
			//			final List<DiscountValue> discountList = cartModel.getGlobalDiscountValues();
			//
			//			final List<DiscountModel> voucherList = cartModel.getDiscounts();
			//			String voucherCode = "";
			//			List<AbstractOrderEntryModel> applicableOrderEntryList = new ArrayList<AbstractOrderEntryModel>();
			//			if (CollectionUtils.isNotEmpty(voucherList))
			//			{
			//				voucherCode = ((PromotionVoucherModel) voucherList.get(0)).getVoucherCode();
			//				applicableOrderEntryList = mplVoucherService.getOrderEntryModelFromVouEntries((VoucherModel) voucherList.get(0),
			//						cartModel);
			//
			//				if (CollectionUtils.isNotEmpty(discountList))
			//				{
			//					for (final DiscountValue discount : discountList)
			//					{
			//						if (CollectionUtils.isNotEmpty(voucherList)
			//								&& discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
			//						{
			//							voucherCalcValue = discount.getValue();
			//						}
			//						else if (!discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
			//						{
			//							promoCalcValue = discount.getValue();
			//						}
			//					}
			//				}
			//			}
			//
			//
			//
			//			if (voucherCalcValue != 0 && (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0)
			//			{
			//				LOG.debug("Step 12:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
			//				try
			//				{
			//					mplVoucherService.releaseVoucher(voucherCode, cartModel);
			//				}
			//				catch (final VoucherOperationException e)
			//				{
			//					// YTODO Auto-generated catch block
			//					e.printStackTrace();
			//				}
			//				try
			//				{
			//					mplVoucherService.recalculateCartForCoupon(cartModel);
			//				}
			//				catch (final JaloPriceFactoryException e)
			//				{
			//					// YTODO Auto-generated catch block
			//					e.printStackTrace();
			//				}
			//				catch (final CalculationException e)
			//				{
			//					// YTODO Auto-generated catch block
			//					e.printStackTrace();
			//				}
			//				cartModel.setCouponErrorMsg("Price_exceeded");
			//				//modelService.save(cartModel);
			//				//Throw exception with specific information
			//				//throw new VoucherOperationException("Voucher " + voucherCode + " cannot be redeemed: total price exceeded");
			//			}
			//
			//			else
			//			{
			//				double netAmountAfterAllDisc = 0.0D;
			//				double productPrice = 0.0D;
			//				boolean flag = false;
			//
			//				if (CollectionUtils.isNotEmpty(applicableOrderEntryList))
			//				{
			//					LOG.debug("Step 13:::applicableOrderEntryList is not empty");
			//					for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
			//					{
			//						if ((null != entry.getProductPromoCode() && StringUtils.isNotEmpty(entry.getProductPromoCode()))
			//								|| (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry.getCartPromoCode())))
			//						{
			//							netAmountAfterAllDisc += entry.getNetAmountAfterAllDisc().doubleValue();
			//							flag = true;
			//						}
			//
			//						productPrice += entry.getTotalPrice().doubleValue();
			//					}
			//
			//					LOG.debug("Step 14:::netAmountAfterAllDisc is " + netAmountAfterAllDisc + " & productPrice is " + productPrice);
			//
			//
			//					if ((productPrice < 1) || (flag && voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)
			//							|| (!flag && voucherCalcValue != 0 && (productPrice - voucherCalcValue) <= 0))
			//					{
			//						LOG.debug("Step 15:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
			//						try
			//						{
			//							mplVoucherService.releaseVoucher(voucherCode, cartModel);
			//						}
			//						catch (final VoucherOperationException e)
			//						{
			//							// YTODO Auto-generated catch block
			//							e.printStackTrace();
			//						}
			//						try
			//						{
			//							mplVoucherService.recalculateCartForCoupon(cartModel);
			//						}
			//						catch (final JaloPriceFactoryException e)
			//						{
			//							// YTODO Auto-generated catch block
			//							e.printStackTrace();
			//						}
			//						catch (final CalculationException e)
			//						{
			//							// YTODO Auto-generated catch block
			//							e.printStackTrace();
			//						}
			//						if ((productPrice < 1))
			//						{
			//							cartModel.setCouponErrorMsg("freebie");
			//						}
			//						cartModel.setCouponErrorMsg("Price_exceeded");
			//						//modelService.save(cartModel);
			//						//Throw exception with specific information
			//					}
			//				}
			//				else if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList))
			//				{
			//					try
			//					{
			//						mplVoucherService.releaseVoucher(voucherCode, cartModel);
			//					}
			//					catch (final VoucherOperationException e)
			//					{
			//						// YTODO Auto-generated catch block
			//						e.printStackTrace();
			//					}
			//					try
			//					{
			//						mplVoucherService.recalculateCartForCoupon(cartModel);
			//					}
			//					catch (final JaloPriceFactoryException e)
			//					{
			//						// YTODO Auto-generated catch block
			//						e.printStackTrace();
			//					}
			//					catch (final CalculationException e)
			//					{
			//						// YTODO Auto-generated catch block
			//						e.printStackTrace();
			//					}
			//					cartModel.setCouponErrorMsg("Price_exceeded");
			//				}
			//			}



			if (null != cartModel.getConvenienceCharges())
			{
				cartModel.setTotalPriceWithConv(Double.valueOf(cartModel.getTotalPrice().doubleValue()
						+ cartModel.getConvenienceCharges().doubleValue()));
			}
			else
			{
				cartModel.setTotalPriceWithConv(cartModel.getTotalPrice());
			}
		}

	}

}
