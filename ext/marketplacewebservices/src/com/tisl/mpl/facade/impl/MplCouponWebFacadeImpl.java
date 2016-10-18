/**
 *
 */
package com.tisl.mpl.facade.impl;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

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
import com.tisl.mpl.service.MplCouponWebService;
import com.tisl.mpl.util.ExceptionUtil;
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
	public ApplyCouponsDTO applyVoucher(final String couponCode, final CartModel cartModel, final OrderModel orderModel,
			final String paymentMode) throws VoucherOperationException, CalculationException, NumberFormatException,
			JaloInvalidParameterException, JaloSecurityException, EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		final ApplyCouponsDTO applycouponDto = new ApplyCouponsDTO();
		final boolean redeem = true;
		boolean couponRedStatus = false;
		VoucherDiscountData data = new VoucherDiscountData();
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
				if (cartModel != null && StringUtils.isNotEmpty(paymentMode))
				{
					//Apply the voucher
					couponRedStatus = mplCouponFacade.applyVoucher(couponCode, cartModel, null);

					LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

					//Calculate and set data attributes
					data = mplCouponFacade.calculateValues(null, cartModel, couponRedStatus, redeem);

					paymentInfo = new HashMap<String, Double>();
					paymentInfo.put(paymentMode, Double.valueOf(cartModel.getTotalPriceWithConv().doubleValue()));

					//Update paymentInfo in session
					mplCouponFacade.updatePaymentInfoSession(paymentInfo, cartModel);

					//getSessionService().removeAttribute("bank");	//Do not remove---needed later
					if (data != null && data.getCouponDiscount() != null && data.getCouponDiscount().getValue() != null)
					{
						//Price data new calculation for 2 decimal values
						applycouponDto.setCouponDiscount(String.valueOf(data.getCouponDiscount().getValue()
								.setScale(2, BigDecimal.ROUND_HALF_UP)));
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
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9330);
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
	public ReleaseCouponsDTO releaseVoucher(final String couponCode, final CartModel cartModel, final OrderModel orderModel,
			final String paymentMode) throws RequestParameterException, WebserviceValidationException, MalformedURLException,
			NumberFormatException, JaloInvalidParameterException, VoucherOperationException, CalculationException,
			JaloSecurityException, JaloPriceFactoryException, EtailBusinessExceptions
	{
		ReleaseCouponsDTO releaseCouponsDTO = new ReleaseCouponsDTO();
		Map<String, Double> paymentInfo = null;
		try
		{
			//Release coupon for cartModel
			if (null == orderModel)
			{

				LOG.debug("Step 1:::The coupon code to be released by the customer is ::: " + couponCode);
				if (cartModel != null && StringUtils.isNotEmpty(paymentMode))
				{
					//Release the coupon
					mplCouponFacade.releaseVoucher(couponCode, cartModel, null);

					//Recalculate cart after releasing coupon
					mplCouponFacade.recalculateCartForCoupon(cartModel, null); //Handled changed method signature for TPR-629

					//Update paymentInfo from model
					paymentInfo = new HashMap<String, Double>();
					paymentInfo.put(paymentMode, Double.valueOf(cartModel.getTotalPriceWithConv().doubleValue()));
					mplCouponFacade.updatePaymentInfoSession(paymentInfo, cartModel);
					releaseCouponsDTO
							.setTotal(String.valueOf(mplCheckoutFacade.createPrice(cartModel, cartModel.getTotalPriceWithConv())
									.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9508);
				}
				releaseCouponsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			//Release coupon for orderModel
			else
			{

				LOG.debug("Step 1:::The coupon code to be released by the customer is ::: " + couponCode);

				//Release the coupon
				mplCouponFacade.releaseVoucher(couponCode, null, orderModel);

				//Recalculate cart after releasing coupon
				mplCouponFacade.recalculateCartForCoupon(null, orderModel); //Handled changed method signature for TPR-629

				//Update paymentInfo in
				paymentInfo = new HashMap<String, Double>();
				paymentInfo.put(paymentMode, Double.valueOf(orderModel.getTotalPriceWithConv().doubleValue()));

				mplCouponFacade.updatePaymentInfoSession(paymentInfo, orderModel);

				releaseCouponsDTO.setTotal(String.valueOf(mplCheckoutFacade
						.createPrice(orderModel, orderModel.getTotalPriceWithConv()).getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				releaseCouponsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS);

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
