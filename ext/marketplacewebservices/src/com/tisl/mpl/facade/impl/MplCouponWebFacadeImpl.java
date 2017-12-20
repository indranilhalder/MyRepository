/**
 *
 */
package com.tisl.mpl.facade.impl;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.util.Tuple2;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.MplCouponWebFacade;
import com.tisl.mpl.model.MplCartOfferVoucherModel;
import com.tisl.mpl.service.MplCouponWebService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.ApplyCartCouponsDTO;
import com.tisl.mpl.wsdto.ApplyCouponsDTO;
import com.tisl.mpl.wsdto.CommonCouponsDTO;
import com.tisl.mpl.wsdto.ReleaseCouponsDTO;


/**
 * @author TCS
 *
 */
public class MplCouponWebFacadeImpl implements MplCouponWebFacade
{
	private static final Logger LOG = Logger.getLogger(MplCouponWebFacadeImpl.class);

	@Resource(name = "mplCouponWebService")
	private MplCouponWebService mplCouponWebService;
	@Resource(name = "mplCheckoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "mplCouponFacade")
	private MplCouponFacade mplCouponFacade;

	/**
	 * @Description : For getting the details of all the Coupons available for the User
	 */

	@Override
	//	public CommonCouponsDTO getCoupons(final int currentPage, final int pageSize, final String emailId, final String usedCoupon,
	//			final String sortCode)
	public CommonCouponsDTO getCoupons(final int currentPage, final String emailId, final String usedCoupon, final String sortCode)
	{
		return (mplCouponWebService.getCoupons(currentPage, emailId, usedCoupon, sortCode));
	}


	@Override
	public ApplyCouponsDTO applyVoucher(final String couponCode, CartModel cartModel, OrderModel orderModel,
			final String paymentMode) throws VoucherOperationException, CalculationException, NumberFormatException,
			JaloInvalidParameterException, JaloSecurityException, EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		final ApplyCouponsDTO applycouponDto = new ApplyCouponsDTO();
		final boolean redeem = true;
		boolean couponRedStatus = false;
		VoucherDiscountData data = new VoucherDiscountData();
		boolean isCartVoucherPresent = false;
		String cartCouponCode = MarketplacecommerceservicesConstants.EMPTY;
		Map<String, Double> paymentInfo = null;
		try
		{
			//Redeem coupon for cartModel
			if (orderModel == null)
			{
				//				Commented-----to be implemented in R2 later
				//		final Collection<BankModel> bankList = getBaseStoreService().getCurrentBaseStore().getBanks();
				//		if (StringUtils.isEmpty(bankNameSelected))
				//		{
				//			getSessionService().setAttribute("bank", bankNameSelected);
				//		}
				//		else
				//		{
				//			for (final BankModel bank : bankList)
				//			{
				//				if (bank.getBankName().equalsIgnoreCase(bankNameSelected))
				//				{
				//					//setting the bank in session to be used for Promotion
				//					getSessionService().setAttribute("bank", bank);
				//					break;
				//				}
				//			}
				//		}
				if (cartModel != null /* && StringUtils.isNotEmpty(paymentMode) */)
				{


					final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(cartModel.getDiscounts());

					isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();

					if (isCartVoucherPresent)
					{
						cartCouponCode = cartCouponObj.getSecond();
						cartModel = (CartModel) mplCouponFacade.removeLastCartCoupon(cartModel); // Removing any Cart level Coupon Offer
					}

					//Apply the voucher
					couponRedStatus = mplCouponFacade.applyVoucher(couponCode, cartModel, null);


					LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

					//Calculate and set data attributes
					data = mplCouponFacade.calculateValues(null, cartModel, couponRedStatus, redeem);

					paymentInfo = new HashMap<String, Double>();
					paymentInfo.put(paymentMode, Double.valueOf(cartModel.getTotalPriceWithConv().doubleValue()));

					//Update paymentInfo in session
					mplCouponFacade.updatePaymentInfoSession(paymentInfo, cartModel);

					if (StringUtils.isNotEmpty(cartCouponCode))
					{
						data = reapplyCartCoupon(data, cartCouponCode, cartModel);
					}

					//getSessionService().removeAttribute("bank");	//Do not remove---needed later
					if (data != null && data.getCouponDiscount() != null && data.getCouponDiscount().getValue() != null)
					{
						//Price data new calculation for 2 decimal values
						applycouponDto.setCouponDiscount(
								String.valueOf(data.getCouponDiscount().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));

					}

					applycouponDto.setTotal(String.valueOf(mplCheckoutFacade.createPrice(cartModel, cartModel.getTotalPriceWithConv())
							.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));

				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9500);
				}
				applycouponDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			else
			{
				final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(orderModel.getDiscounts());

				isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();

				if (isCartVoucherPresent)
				{
					cartCouponCode = cartCouponObj.getSecond();
					orderModel = (OrderModel) mplCouponFacade.removeLastCartCoupon(orderModel); // Removing any Cart level Coupon Offer
				}
				//Apply the voucher
				couponRedStatus = mplCouponFacade.applyVoucher(couponCode, null, orderModel);

				LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

				//Calculate and set data attributes
				data = mplCouponFacade.calculateValues(orderModel, null, couponRedStatus, redeem);
				if (StringUtils.isNotEmpty(paymentMode))
				{
					paymentInfo = new HashMap<String, Double>();
					paymentInfo.put(paymentMode, Double.valueOf(orderModel.getTotalPriceWithConv().doubleValue()));
				}
				//Update paymentInfo in session
				mplCouponFacade.updatePaymentInfoSession(paymentInfo, orderModel);

				if (StringUtils.isNotEmpty(cartCouponCode))
				{
					data = reapplyCartCoupon(data, cartCouponCode, orderModel);
				}

				//getSessionService().removeAttribute("bank");	//Do not remove---needed later
				if (data != null && data.getCouponDiscount() != null && data.getCouponDiscount().getValue() != null)
				{
					applycouponDto.setCouponDiscount(data.getCouponDiscount().getValue().toPlainString());

				}

				applycouponDto.setTotal(String.valueOf(mplCheckoutFacade.createPrice(orderModel, orderModel.getTotalPriceWithConv())
						.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));

				applycouponDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}

		}
		catch (final VoucherOperationException e)
		{
			applycouponDto.setStatus(MarketplacecommerceservicesConstants.FAILURE);

			if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCPRICEEXCEEDED))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9501);
			}
			else if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCINVALID))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9503);
			}
			else if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCEXPIRED))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9505);
			}
			else if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCISSUE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9507);
			}
			else if (e.getMessage().contains(MarketplacewebservicesConstants.EXCNOTAPPLICABLE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9509);
			}
			else if (e.getMessage().contains(MarketplacewebservicesConstants.EXCNOTRESERVABLE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9510);
			}
			else if (e.getMessage().contains(MarketplacewebservicesConstants.EXCFREEBIE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9511);
			}
			else if (e.getMessage().contains(MarketplacecouponConstants.EXCUSERINVALID))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9512);
			}
			/* Added for TPR-1290 */
			else if (e.getMessage().contains(MarketplacecouponConstants.EXCFIRSTPURUSERINVALID))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9332);
			}
			//TPR-4460 Changes
			else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_MOBILE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9303);
			}
			else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_WEB))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9302);
			}
			else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_CALLCENTRE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9304);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9507);
		}
		return applycouponDto;
	}

	/* TPR 7486 */
	@Override
	public ApplyCartCouponsDTO applyCartVoucher(final String manuallyselectedvoucher, CartModel cartModel, OrderModel orderModel,
			final String paymentMode) throws VoucherOperationException, CalculationException, NumberFormatException,
			JaloInvalidParameterException, JaloSecurityException, EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		final String couponCode = mplCouponFacade.getCouponCode(manuallyselectedvoucher);
		final ApplyCartCouponsDTO applycouponDto = new ApplyCartCouponsDTO();
		//final boolean redeem = true;
		boolean couponRedStatus = false;
		VoucherDiscountData data = new VoucherDiscountData();
		Map<String, Double> paymentInfo = null;
		try
		{
			//Redeem coupon for cartModel
			if (orderModel == null)
			{
				if (cartModel != null /* && StringUtils.isNotEmpty(paymentMode) */)
				{
					//Apply the voucher

					cartModel = (CartModel) mplCouponFacade.removeLastCartCoupon(cartModel);
					if (StringUtils.isNotEmpty(couponCode))
					{
						couponRedStatus = mplCouponFacade.applyCartVoucher(couponCode, cartModel, null);
						LOG.debug("Cart Coupon Redemption Status is >>>>" + couponRedStatus);
						//data = getMplCouponFacade().populateCartVoucherData(null, cartModel, couponRedStatus, true, couponCode);
					}


					//couponRedStatus = mplCouponFacade.applyVoucher(couponCode, cartModel, null);

					//LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

					//Calculate and set data attributes
					//data = mplCouponFacade.calculateValues(null, cartModel, couponRedStatus, redeem);
					data = mplCouponFacade.populateCartVoucherData(null, cartModel, couponRedStatus, true, couponCode);

					paymentInfo = new HashMap<String, Double>();
					paymentInfo.put(paymentMode, Double.valueOf(cartModel.getTotalPriceWithConv().doubleValue()));

					//Update paymentInfo in session
					mplCouponFacade.updatePaymentInfoSession(paymentInfo, cartModel);

					//getSessionService().removeAttribute("bank");	//Do not remove---needed later
					if (data != null && data.getMbDiscountAftrCVoucher() != null
							&& data.getMbDiscountAftrCVoucher().getValue() != null)
					{
						applycouponDto.setDiscount(
								String.valueOf(data.getMbDiscountAftrCVoucher().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
					}

					applycouponDto.setTotal(String.valueOf(mplCheckoutFacade.createPrice(cartModel, cartModel.getTotalPriceWithConv())
							.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));

				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9500);
				}
				applycouponDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			else
			{

				orderModel = (OrderModel) mplCouponFacade.removeLastCartCoupon(orderModel);
				if (StringUtils.isNotEmpty(couponCode))
				{
					couponRedStatus = mplCouponFacade.applyCartVoucher(couponCode, null, orderModel);
					LOG.debug("Cart Coupon Redemption Status is >>>>" + couponRedStatus);
					//data = getMplCouponFacade().populateCartVoucherData(orderModel, null, couponRedStatus, true, couponCode);
				}



				//Apply the voucher
				//couponRedStatus = mplCouponFacade.applyVoucher(couponCode, null, orderModel);

				//LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

				//Calculate and set data attributes
				//data = mplCouponFacade.calculateValues(orderModel, null, couponRedStatus, redeem);
				data = mplCouponFacade.populateCartVoucherData(orderModel, null, couponRedStatus, true, couponCode);
				/*
				 * if (StringUtils.isNotEmpty(paymentMode)) {
				 */
				paymentInfo = new HashMap<String, Double>();
				paymentInfo.put(paymentMode, Double.valueOf(orderModel.getTotalPriceWithConv().doubleValue()));
				/* } */
				//Update paymentInfo in session
				mplCouponFacade.updatePaymentInfoSession(paymentInfo, orderModel);
				//getSessionService().removeAttribute("bank");	//Do not remove---needed later
				if (data != null && data.getMbDiscountAftrCVoucher() != null && data.getMbDiscountAftrCVoucher().getValue() != null)
				{
					applycouponDto.setDiscount(
							String.valueOf(data.getMbDiscountAftrCVoucher().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}

				applycouponDto.setTotal(String.valueOf(mplCheckoutFacade.createPrice(orderModel, orderModel.getTotalPriceWithConv())
						.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));

				applycouponDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}

		}
		catch (final VoucherOperationException e)
		{
			applycouponDto.setStatus(MarketplacecommerceservicesConstants.FAILURE);

			if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCPRICEEXCEEDED))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9501);
			}
			else if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCINVALID))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9503);
			}
			else if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCEXPIRED))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9505);
			}
			else if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCISSUE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9507);
			}
			else if (e.getMessage().contains(MarketplacewebservicesConstants.EXCNOTAPPLICABLE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9509);
			}
			else if (e.getMessage().contains(MarketplacewebservicesConstants.EXCNOTRESERVABLE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9510);
			}
			else if (e.getMessage().contains(MarketplacewebservicesConstants.EXCFREEBIE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9511);
			}
			else if (e.getMessage().contains(MarketplacecouponConstants.EXCUSERINVALID))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9512);
			}
			/* Added for TPR-1290 */
			else if (e.getMessage().contains(MarketplacecouponConstants.EXCFIRSTPURUSERINVALID))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9332);
			}
			//TPR-4460 Changes
			else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_MOBILE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9303);
			}
			else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_WEB))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9302);
			}
			else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_CALLCENTRE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9304);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9507);
		}
		return applycouponDto;
	}

	@Override
	public ReleaseCouponsDTO releaseVoucher(final String couponCode, CartModel cartModel, OrderModel orderModel,
			final String paymentMode) throws RequestParameterException, WebserviceValidationException, MalformedURLException,
			NumberFormatException, JaloInvalidParameterException, VoucherOperationException, CalculationException,
			JaloSecurityException, JaloPriceFactoryException, EtailBusinessExceptions
	{
		ReleaseCouponsDTO releaseCouponsDTO = new ReleaseCouponsDTO();
		//final Map<String, Double> paymentInfo = null;
		final boolean redeem = false;
		VoucherDiscountData data = new VoucherDiscountData();
		boolean isCartVoucherPresent = false;
		String cartCouponCode = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			//Release coupon for cartModel
			if (null != cartModel)
			{

				LOG.debug("Step 1:::The coupon code to be released by the customer is ::: " + couponCode);
				final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(cartModel.getDiscounts());
				isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();

				if (isCartVoucherPresent)
				{
					cartCouponCode = cartCouponObj.getSecond();
					cartModel = (CartModel) mplCouponFacade.removeLastCartCoupon(cartModel); // Removing any Cart level Coupon Offer
				}


				//Release the coupon
				mplCouponFacade.releaseVoucher(couponCode, cartModel, null);

				//Recalculate cart after releasing coupon
				mplCouponFacade.recalculateCartForCoupon(cartModel, null); //Handled changed method signature for TPR-629

				//data = mplCouponFacade.calculateValues(null, cartModel, true, redeem);
				if (StringUtils.isNotEmpty(cartCouponCode))
				{
					try
					{
						final boolean applyStatus = mplCouponFacade.applyCartVoucher(cartCouponCode, cartModel, null);
						final VoucherDiscountData newData = mplCouponFacade.populateCartVoucherData(null, cartModel, applyStatus, true,
								couponCode);

						data = newData;
						//data.setTotalDiscount(newData.getTotalDiscount());
						//data.setTotalPrice(newData.getTotalPrice());

					}
					catch (final VoucherOperationException e)
					{
						LOG.debug("Failed to apply Voucher with Code >>>" + cartCouponCode);
					}
					catch (final Exception e)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
					}
				}
				else
				{
					data = mplCouponFacade.calculateValues(null, cartModel, true, redeem);

				}

				//Update paymentInfo from model
				//paymentInfo = new HashMap<String, Double>();
				//paymentInfo.put(paymentMode, Double.valueOf(cartModel.getTotalPriceWithConv().doubleValue()));
				//mplCouponFacade.updatePaymentInfoSession(paymentInfo, cartModel);
				if (null != data.getTotalPrice() && null != data.getTotalPrice().getValue())
				{
					releaseCouponsDTO.setTotal(String.valueOf(data.getTotalPrice().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}

				releaseCouponsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			//Release coupon for orderModel
			else if (null != orderModel)
			{

				LOG.debug("Step 1:::The coupon code to be released by the customer is ::: " + couponCode);
				final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(orderModel.getDiscounts());
				isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();

				if (isCartVoucherPresent)
				{
					cartCouponCode = cartCouponObj.getSecond();
					orderModel = (OrderModel) mplCouponFacade.removeLastCartCoupon(orderModel); // Removing any Cart level Coupon Offer
				}

				//Release the coupon
				mplCouponFacade.releaseVoucher(couponCode, null, orderModel);

				//Recalculate cart after releasing coupon
				mplCouponFacade.recalculateCartForCoupon(null, orderModel); //Handled changed method signature for TPR-629

				//data = mplCouponFacade.calculateValues(orderModel, null, true, redeem);
				if (StringUtils.isNotEmpty(cartCouponCode))
				{
					try
					{
						final boolean applyStatus = mplCouponFacade.applyCartVoucher(cartCouponCode, null, orderModel);
						final VoucherDiscountData newData = mplCouponFacade.populateCartVoucherData(orderModel, null, applyStatus, true,
								couponCode);

						data = newData;
						//data.setTotalDiscount(newData.getTotalDiscount());
						//data.setTotalPrice(newData.getTotalPrice());

					}
					catch (final VoucherOperationException e)
					{
						LOG.debug("Failed to apply Voucher with Code >>>" + cartCouponCode);
					}
					catch (final Exception e)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
					}
				}
				else
				{
					data = mplCouponFacade.calculateValues(orderModel, null, true, redeem);

				}


				//Update paymentInfo in
				//paymentInfo = new HashMap<String, Double>();
				//paymentInfo.put(paymentMode, Double.valueOf(orderModel.getTotalPriceWithConv().doubleValue()));

				//mplCouponFacade.updatePaymentInfoSession(paymentInfo, orderModel);
				if (null != data.getTotalPrice() && null != data.getTotalPrice().getValue())
				{
					releaseCouponsDTO.setTotal(String.valueOf(data.getTotalPrice().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
				releaseCouponsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS);

			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9508);
			}
		}
		catch (final VoucherOperationException e)
		{
			LOG.error(MarketplacecouponConstants.COUPONRELISSUE, e);
			releaseCouponsDTO = setRelDataForException(releaseCouponsDTO);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			releaseCouponsDTO = setRelDataForException(releaseCouponsDTO);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
			releaseCouponsDTO = setRelDataForException(releaseCouponsDTO);
		}


		return releaseCouponsDTO;
	}

	//TPR - 7486
	private boolean checkforCartVoucherRemoved(final List<DiscountModel> discounts)
	{
		boolean isCartVoucherRemoved = true;

		if (CollectionUtils.isNotEmpty(discounts))
		{
			for (final DiscountModel discount : discounts)
			{
				if (discount instanceof MplCartOfferVoucherModel)
				{
					isCartVoucherRemoved = false;
					break;
				}
			}
		}

		return isCartVoucherRemoved;
	}

	//TPR - 7486
	private Tuple2<Boolean, String> isCartVoucherPresent(final List<DiscountModel> discounts)
	{
		boolean flag = false;
		String couponCode = MarketplacecommerceservicesConstants.EMPTY;
		if (CollectionUtils.isNotEmpty(discounts))
		{
			for (final DiscountModel discount : discounts)
			{
				if (discount instanceof MplCartOfferVoucherModel)
				{
					final MplCartOfferVoucherModel object = (MplCartOfferVoucherModel) discount;
					flag = true;
					couponCode = object.getVoucherCode();
					break;
				}
			}
		}

		final Tuple2<Boolean, String> cartCouponObj = new Tuple2(Boolean.valueOf(flag), couponCode);

		return cartCouponObj;
	}

	/**
	 * Re apply Cart Coupon TPR - 7486
	 *
	 * @param data
	 * @param cartCouponCode
	 * @param cartModel
	 * @return dataPojo
	 */
	private VoucherDiscountData reapplyCartCoupon(final VoucherDiscountData data, final String cartCouponCode,
			final CartModel cartModel)
	{
		final VoucherDiscountData dataPojo = data;
		try
		{
			final boolean applyStatus = mplCouponFacade.applyCartVoucher(cartCouponCode, cartModel, null);
			final VoucherDiscountData newData = mplCouponFacade.populateCartVoucherData(null, cartModel, applyStatus, true,
					cartCouponCode);

			dataPojo.setTotalDiscount(newData.getTotalDiscount());
			dataPojo.setTotalPrice(newData.getTotalPrice());

		}
		catch (final VoucherOperationException e)
		{
			LOG.debug("Failed to apply Voucher with Code >>>" + cartCouponCode);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}

		return dataPojo;
	}

	/**
	 * Re apply Cart Coupon
	 *
	 * @param data
	 * @param cartCouponCode
	 * @param oModel
	 * @return dataPojo
	 */
	private VoucherDiscountData reapplyCartCoupon(final VoucherDiscountData data, final String cartCouponCode,
			final OrderModel oModel)
	{
		final VoucherDiscountData dataPojo = data;
		try
		{
			final boolean applyStatus = mplCouponFacade.applyCartVoucher(cartCouponCode, null, oModel);
			final VoucherDiscountData newData = mplCouponFacade.populateCartVoucherData(oModel, null, applyStatus, true,
					cartCouponCode);

			data.setTotalDiscount(newData.getTotalDiscount());
			data.setTotalPrice(newData.getTotalPrice());

		}
		catch (final VoucherOperationException e)
		{
			LOG.debug("Failed to apply Voucher with Code >>>" + cartCouponCode);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}

		return dataPojo;
	}


	/* TPR - 7486 */
	@Override
	public ReleaseCouponsDTO releaseCartVoucher(final String couponCode, CartModel cartModel, OrderModel orderModel,
			final String paymentMode) throws RequestParameterException, WebserviceValidationException, MalformedURLException,
			NumberFormatException, JaloInvalidParameterException, VoucherOperationException, CalculationException,
			JaloSecurityException, JaloPriceFactoryException, EtailBusinessExceptions
	{
		ReleaseCouponsDTO releaseCouponsDTO = new ReleaseCouponsDTO();
		//final Map<String, Double> paymentInfo = null;
		//final boolean redeem = false;
		boolean isCartVoucherRemoved = false;
		VoucherDiscountData data = new VoucherDiscountData();
		try
		{
			//Release coupon for cartModel
			if (null != cartModel)
			{

				LOG.debug("Step 1:::The cart coupon code to be released by the customer is ::: " + couponCode);


				cartModel = (CartModel) mplCouponFacade.removeCartCoupon(cartModel);

				isCartVoucherRemoved = checkforCartVoucherRemoved(cartModel.getDiscounts());

				if (isCartVoucherRemoved)
				{
					data = mplCouponFacade.populateCartVoucherData(null, cartModel, false, false, couponCode);
				}


				//Release the coupon
				//mplCouponFacade.releaseVoucher(couponCode, cartModel, null);

				//Recalculate cart after releasing coupon
				//mplCouponFacade.recalculateCartForCoupon(cartModel, null); //Handled changed method signature for TPR-629

				//data = mplCouponFacade.calculateValues(null, cartModel, true, redeem);

				//Update paymentInfo from model
				//paymentInfo = new HashMap<String, Double>();
				//paymentInfo.put(paymentMode, Double.valueOf(cartModel.getTotalPriceWithConv().doubleValue()));
				//mplCouponFacade.updatePaymentInfoSession(paymentInfo, cartModel);
				if (null != data.getTotalPrice() && null != data.getTotalPrice().getValue())
				{
					releaseCouponsDTO.setTotal(String.valueOf(data.getTotalPrice().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}

				releaseCouponsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			//Release coupon for orderModel
			else if (null != orderModel)
			{

				LOG.debug("Step 1:::The cart coupon code to be released by the customer is ::: " + couponCode);
				orderModel = (OrderModel) mplCouponFacade.removeCartCoupon(orderModel);
				isCartVoucherRemoved = checkforCartVoucherRemoved(orderModel.getDiscounts());

				if (isCartVoucherRemoved)
				{
					data = mplCouponFacade.populateCartVoucherData(orderModel, null, false, false, couponCode);
				}
				//Release the coupon
				//mplCouponFacade.releaseVoucher(couponCode, null, orderModel);

				//Recalculate cart after releasing coupon
				//mplCouponFacade.recalculateCartForCoupon(null, orderModel); //Handled changed method signature for TPR-629

				//data = mplCouponFacade.calculateValues(orderModel, null, true, redeem);

				//Update paymentInfo in
				//paymentInfo = new HashMap<String, Double>();
				//paymentInfo.put(paymentMode, Double.valueOf(orderModel.getTotalPriceWithConv().doubleValue()));

				//mplCouponFacade.updatePaymentInfoSession(paymentInfo, orderModel);
				if (null != data.getTotalPrice() && null != data.getTotalPrice().getValue())
				{
					releaseCouponsDTO.setTotal(String.valueOf(data.getTotalPrice().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
				releaseCouponsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS);

			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9508);
			}
		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			releaseCouponsDTO = setRelDataForException(releaseCouponsDTO);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
			releaseCouponsDTO = setRelDataForException(releaseCouponsDTO);
		}


		return releaseCouponsDTO;
	}

	/**
	 * This method sets Release Coupon DTO for exception cases
	 *
	 * @param releaseCouponsDTO
	 * @param absModel
	 * @return ReleaseCouponsDTO
	 */
	private ReleaseCouponsDTO setRelDataForException(final ReleaseCouponsDTO releaseCouponsDTO)
	{

		releaseCouponsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE);
		releaseCouponsDTO.setErrorCode(MarketplacecommerceservicesConstants.B9508);
		releaseCouponsDTO.setError(MarketplacewebservicesConstants.COUPONRELISSUE);
		return releaseCouponsDTO;
	}

}
