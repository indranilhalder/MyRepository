/**
 * 
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.localization.Localization;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.cms.data.WalletCreateData;
import com.tisl.mpl.facades.egv.data.EgvDetailsData;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.egv.service.cart.MplEGVCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
import com.tisl.mpl.service.MplEgvWalletService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.ApplyCliqCashWsDto;
import com.tisl.mpl.wsdto.BuyingEgvRequestWsDTO;
import com.tisl.mpl.wsdto.BuyingEgvResponceWsDTO;
import com.tisl.mpl.wsdto.EgvCheckMobileNumberWsDto;
import com.tisl.mpl.wsdto.EgvWalletCreateRequestWsDTO;
import com.tisl.mpl.wsdto.EgvWalletCreateResponceWsDTO;
import com.tisl.mpl.wsdto.RedeemCliqVoucherWsDTO;
import com.tisl.mpl.wsdto.ResendEGVNotificationWsDTO;
import com.tisl.mpl.wsdto.TotalCliqCashBalanceWsDto;
import com.tisl.mpl.wsdto.UserCliqCashWsDto;

/**
 * @author IT
 *
 */

@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
@Controller
@RequestMapping(value = "/{baseSiteId}/users", headers = "Accept=application/xml,application/json")
@CacheControl(directive = CacheControlDirective.PRIVATE)
public class WalletController
{
	private static final Logger LOG = Logger.getLogger(WalletController.class);
	
	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource
	private UserService userService;
	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	
	@Resource
	private CommerceCartService commerceCartService;

	@Resource
	private MplCartFacade mplCartFacade;

	
	@Resource
	private MplOrderFacade mplOrderFacade;

	
	@Autowired
	private MplPaymentWebFacade mplPaymentWebFacade;

	@Autowired
	private MplWalletFacade mplWalletFacade;
	
	@Resource(name = "mplDefaultPriceDataFactory")
	private DefaultPriceDataFactory priceDataFactory;
	
	@Autowired
	private CommonI18NService commonI18NService;
	
	@Autowired
	private MplEGVCartService mplEGVCartService;
	

	@Resource(name = "mplVoucherService")
	private MplVoucherService mplVoucherService;
	
	@Autowired
	private RegisterCustomerFacade registerCustomerFacade;
	
	@Autowired
	private MplEgvWalletService mplEgvWalletService;
	
	@Autowired
   private MplCouponFacade mplCouponFacade;
	private static final String CUSTOMER = "ROLE_CUSTOMERGROUP";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String APPLICATION_TYPE = "application/json";
	
	/**
	 * This API is used to USe Cliq Cash While Placing Order 
	 * 
	 * 
	 * @param cartGuid
	 * @return
	 * @throws EtailNonBusinessExceptions
	 * @throws EtailBusinessExceptions
	 * @throws CalculationException
	 */
	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.APPLY_CLIQCASH, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ApplyCliqCashWsDto applyCliqCash(@RequestParam final String cartGuid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException
	{
		ApplyCliqCashWsDto applyCliqCashWsDto = new ApplyCliqCashWsDto();
		OrderModel orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
		CartModel cart = null;
		LOG.info("Applying  cliq Cash For Order Guid " + cartGuid);

		try
		{
			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				Double totalWalletAmount = cart.getTotalWalletAmount();

				applyCliqCashWsDto = mplEgvWalletService.applyCLiqCash(cart, totalWalletAmount);
			}
			else
			{
				Double totalWalletAmount = orderModel.getTotalWalletAmount();
				applyCliqCashWsDto = mplEgvWalletService.applyCLiqCash(orderModel, totalWalletAmount);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			applyCliqCashWsDto.setError(ex.getErrorCode());
			applyCliqCashWsDto.setErrorCode(ex.getErrorCode());
			applyCliqCashWsDto.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while applying Cliq cash" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			applyCliqCashWsDto.setError(ex.getMessage());
			LOG.error("Exception occrred while applying Cliq cash" + ex.getMessage());
		}
		return applyCliqCashWsDto;

	}

	
/**
 * This API is used to Remove the Cliq Cash From Cart
 * @param cartGuid
 * @return
 * @throws EtailNonBusinessExceptions
 * @throws EtailBusinessExceptions
 * @throws CalculationException
 */

	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.REMOVE_CLIQCASH, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ApplyCliqCashWsDto removeCliqCash(@RequestParam final String cartGuid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException
	{
		LOG.info("Removing cliq Cash ");
		 ApplyCliqCashWsDto removeCliqCashWsDto = new ApplyCliqCashWsDto();
		 OrderModel orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
		CartModel cart = null;
		try
		{
			
			//  Removing the cliqCash balacne from Cart and Setting SplitModeInfo To JUSPAY
			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				commerceCartService.recalculateCart(cart);
				 
				removeCliqCashWsDto.setDiscount(cart.getTotalDiscounts());
				if (null != cart.getTotalPrice())
				{
					removeCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
				}
			//	removeCliqCashWsDto.setPaybleAmount(cart.getTotalPrice());
				if(null !=  cart.getTotalPrice()) {
					Double amount = cart.getTotalPrice();
			        BigDecimal bigDecimal = new BigDecimal(amount.doubleValue());
					final String decimalFormat = "0.00";
					final DecimalFormat df = new DecimalFormat(decimalFormat);
					final String totalPayableAmount = df.format(bigDecimal);
					removeCliqCashWsDto.setPaybleAmount(totalPayableAmount);
				}
				
				
				final Tuple2<Boolean, String> cartCouponObj = mplEgvWalletService.isCartVoucherPresent(cart.getDiscounts());

				boolean isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();
				String cartCouponCode =null;
				 if(isCartVoucherPresent) {
					 cartCouponCode = cartCouponObj.getSecond();
					 mplCouponFacade.removeLastCartCoupon(cart);
				 }
				 if (isCartVoucherPresent)
					{
						cartCouponCode = cartCouponObj.getSecond();
						cart.setCheckForBankVoucher("true");
						modelService.save(cart);
						mplCouponFacade.applyCartVoucher(cartCouponCode, cart, null);
						cart.setCheckForBankVoucher("false");
					}
				  cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
					cart.setPayableNonWalletAmount(cart.getTotalPrice());
					cart.setPayableWalletAmount(Double.valueOf(0.0D));
					modelService.save(cart);
					modelService.refresh(cart);
					removeCliqCashWsDto = mplEgvWalletService.setTotalPrice(removeCliqCashWsDto, cart);
				removeCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
			//	orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
				AbstractOrderModel order =orderModel; 
			//	commerceCartService.recalculateCart((CartModel)order);
				
					removeCliqCashWsDto.setDiscount(orderModel.getTotalDiscounts());
					if (null != orderModel.getTotalPrice())
					{
						BigDecimal bigDecimal = new BigDecimal(orderModel.getTotalPrice().doubleValue());
						final String decimalFormat = configurationService.getConfiguration().getString("site.decimal.format", "0.00");
						final DecimalFormat df = new DecimalFormat(decimalFormat);
						final String totalPriceFormatted = df.format(bigDecimal);
						removeCliqCashWsDto.setPaybleAmount(totalPriceFormatted);
					}
					final Tuple2<Boolean, String> cartCouponObj = mplEgvWalletService.isCartVoucherPresent(order.getDiscounts());

					boolean isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();
					String cartCouponCode =null;
					 if(isCartVoucherPresent) {
						 cartCouponCode = cartCouponObj.getSecond();
						 mplCouponFacade.removeLastCartCoupon(cart);
					 }
					 if (isCartVoucherPresent)
						{
							cartCouponCode = cartCouponObj.getSecond();
							orderModel.setCheckForBankVoucher("true");
							modelService.save(orderModel);
							mplCouponFacade.applyCartVoucher(cartCouponCode, null, orderModel);
							orderModel.setCheckForBankVoucher("false");
						}
				   	 orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
						orderModel.setPayableNonWalletAmount(orderModel.getTotalPrice());
						orderModel.setPayableWalletAmount(Double.valueOf(0.0D));
						modelService.save(orderModel);
						modelService.refresh(orderModel);
						removeCliqCashWsDto =mplEgvWalletService.setTotalPrice(removeCliqCashWsDto, orderModel);
					removeCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			removeCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			removeCliqCashWsDto.setErrorCode(ex.getErrorCode());
			removeCliqCashWsDto.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while Removing Cliq cash" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			removeCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			removeCliqCashWsDto.setError(ex.getMessage());
			LOG.error("Exception occrred while Removing Cliq cash" + ex.getMessage());
		}
		return removeCliqCashWsDto;

	}



/**
 * This API Is used to Redeem Cliq Cash using Cart Number and Pin 
 * @param couponCode
 * @param passKey
 * @return
 * @throws EtailNonBusinessExceptions
 * @throws EtailBusinessExceptions
 * @throws CalculationException
 */

	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.REDEEM_CLIQ_VOUCHER, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public RedeemCliqVoucherWsDTO redeemCliqVoucher(@RequestParam final String couponCode, @RequestParam final String passKey,
			@RequestParam(required=false) final String cartGuid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

	{
		LOG.info("Redeeming CLiq Cash Voucher Card Number " + couponCode);
		RedeemCliqVoucherWsDTO redeemCliqVoucherWsDTO = new RedeemCliqVoucherWsDTO();
		OrderModel orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
		CartModel cart = null;
		ApplyCliqCashWsDto applyCliqCashWsDto = null;
//		if (null != cartGuid)
//		{
//			cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
//		}
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			if (null != currentCustomer
					&& (null == currentCustomer.getIsWalletActivated() || !currentCustomer.getIsWalletActivated().booleanValue()))
			{
				redeemCliqVoucherWsDTO.setIsWalletLimitReached(false);
				redeemCliqVoucherWsDTO.setIsWalletCreated(false);
				if(null != currentCustomer.getIsqcOtpVerify() && currentCustomer.getIsqcOtpVerify().booleanValue() )
				{
					redeemCliqVoucherWsDTO.setIsWalletOtpVerified(true);
				}
				WalletCreateData walletCreateData =mplWalletFacade.getWalletCreateData();
				if(null != walletCreateData) {
					redeemCliqVoucherWsDTO.setFirstName(walletCreateData.getQcVerifyFirstName());
					redeemCliqVoucherWsDTO.setLastName(walletCreateData.getQcVerifyLastName());
					redeemCliqVoucherWsDTO.setMobileNumber(walletCreateData.getQcVerifyMobileNo());
				}
				redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				return redeemCliqVoucherWsDTO;
			}
			else
			{
				LOG.debug("Calling To QC For Adding money to Wallet");
				final RedimGiftCardResponse response = mplWalletFacade.getAddEGVToWallet(couponCode, passKey);
				if (null != response && null != response.getResponseCode() && response.getResponseCode() == Integer.valueOf(0))
				{
					final TotalCliqCashBalanceWsDto totalCliqCashBalance = new TotalCliqCashBalanceWsDto();
					if (null != response.getWallet() && null != response.getWallet().getBalance())
					{
						final BigDecimal walletAmount = new BigDecimal(response.getWallet().getBalance().doubleValue());
						final CurrencyModel currency = commonI18NService.getCurrency(MarketplacecommerceservicesConstants.INR);
						final long valueLong = walletAmount.setScale(0, BigDecimal.ROUND_FLOOR).longValue();
						final String totalPriceNoDecimalPntFormatted = Long.toString(valueLong);
						StringBuilder stbND = new StringBuilder(20);
						if (null != currency && null != currency.getSymbol())
						{
							stbND = stbND.append(currency.getSymbol()).append(totalPriceNoDecimalPntFormatted);
						}
						redeemCliqVoucherWsDTO.setVoucherValue(stbND.toString());
						final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, walletAmount,
								MarketplacecommerceservicesConstants.INR);
						if (null != priceData)
						{
							totalCliqCashBalance.setCurrencyIso(priceData.getCurrencyIso());
							totalCliqCashBalance.setDoubleValue(priceData.getDoubleValue());
							totalCliqCashBalance.setFormattedValue(priceData.getFormattedValue());
							totalCliqCashBalance.setPriceType(priceData.getPriceType());
							totalCliqCashBalance.setFormattedValueNoDecimal(priceData.getFormattedValueNoDecimal());
							totalCliqCashBalance.setValue(priceData.getValue());
							redeemCliqVoucherWsDTO.setTotalCliqCashBalance(totalCliqCashBalance);
							redeemCliqVoucherWsDTO.setAcknowledgement("Congrats!  Money has been added to your Cliq Cash balance");
							redeemCliqVoucherWsDTO.setIsWalletLimitReached(false);
							redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							if (null == orderModel)
							{
								cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
								applyCliqCashWsDto = mplEgvWalletService.applyCLiqCash(cart, response.getWallet().getBalance());

							}
							else if (null != cartGuid)
							{
								applyCliqCashWsDto = mplEgvWalletService.applyCLiqCash(orderModel, response.getWallet().getBalance());
							}
							if (null != applyCliqCashWsDto)
							{
								redeemCliqVoucherWsDTO.setApplyCliqCash(applyCliqCashWsDto);
							}
						}
						else
						{
							redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
							redeemCliqVoucherWsDTO.setError(response.getResponseMessage());
						}


					}
					else
					{
						redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
						redeemCliqVoucherWsDTO.setError(response.getResponseMessage());
					}
				}
				else
				{
					redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
					if (null != response)
					{
						redeemCliqVoucherWsDTO.setError(response.getResponseMessage());
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			redeemCliqVoucherWsDTO.setErrorCode(ex.getErrorCode());
			redeemCliqVoucherWsDTO.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while Redeeming Cliq cash" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			redeemCliqVoucherWsDTO.setError(ex.getMessage());
			LOG.error("Exception occrred while Redeeming Cliq cash" + ex.getMessage());
		}
		return redeemCliqVoucherWsDTO;
	}


	@Secured(
			{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
			@RequestMapping(value = MarketplacewebservicesConstants.CREATE_ELECTRONICS_GIFTCARD_AMOUNT, method = RequestMethod.POST, produces = APPLICATION_TYPE)
			@ResponseBody
			public BuyingEgvResponceWsDTO calculateGiftCardAmount(@RequestBody final BuyingEgvRequestWsDTO buyingEgvRequest)
					throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

			{
				LOG.info("Calculating Electronics Gift Cart Amount");
				final BuyingEgvResponceWsDTO buyingEgvResponce = new BuyingEgvResponceWsDTO();
				int quantity =1;
				double amountUserSelectedPerQty = 0.0D;
				double paybleAmount = 0.0D;
				try
				{
					if (null != buyingEgvRequest)
					{
						if (buyingEgvRequest.getQuantity() > 0)
						{
							quantity = buyingEgvRequest.getQuantity();
						}
						if (null != buyingEgvRequest.getPriceSelectedByUserPerQuantity() &&
								buyingEgvRequest.getPriceSelectedByUserPerQuantity().doubleValue() >0.0D)
						{
							amountUserSelectedPerQty = buyingEgvRequest.getPriceSelectedByUserPerQuantity().doubleValue();
							LOG.debug("amountUserSelectedPerQty  :"+amountUserSelectedPerQty);
						}
						if (quantity > 0 && amountUserSelectedPerQty > 0.0D)
						{
							paybleAmount = quantity*amountUserSelectedPerQty;
						}
						if(paybleAmount > 0.0D) {
							LOG.debug("Toatal Payable Amount :"+paybleAmount);
							buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							
							buyingEgvResponce.setPaybleAmount(Double.valueOf(paybleAmount));
							buyingEgvResponce.setTotalPrice(Double.valueOf(paybleAmount));
							buyingEgvResponce.setDiscounts(Double.valueOf(0.0D));
						}else {
							buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
						}
					}
					else
					{
						buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
					}
				}
		catch (final EtailNonBusinessExceptions ex)
		{
			buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			buyingEgvResponce.setErrorCode(ex.getErrorCode());
			buyingEgvResponce.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while Calculating Electronics Gift Card Amount " + ex.getMessage());
		}
		catch (final Exception ex)
		{
			buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			buyingEgvResponce.setError(ex.getMessage());
			LOG.error("Exception occrred Calculating Electronics Gift Card Amount " + ex.getMessage());
		}

		return buyingEgvResponce;
	}

	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.CREATE_ELECTRONICS_GIFTCARD_GUID, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public BuyingEgvResponceWsDTO createGiftCardGuid(@RequestBody final BuyingEgvRequestWsDTO buyingEgvRequest)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

	{
		LOG.info("Creating  Electronics Gift Card Guid");
		final BuyingEgvResponceWsDTO buyingEgvResponce = new BuyingEgvResponceWsDTO();
		CartData giftCartData = null;
		try
		{
			 EgvDetailsData egvDetailsData=null;
			if(null != buyingEgvRequest){
				egvDetailsData = populateEGVFormToData(buyingEgvRequest);
			}
			if (null != egvDetailsData)
			{
				giftCartData = mplCartFacade.getGiftCartModel(egvDetailsData);
			}
			if (null != giftCartData)
			{
				if (null != giftCartData.getTotalPrice())
				{
					buyingEgvResponce.setTotalPrice(giftCartData.getTotalPrice().getDoubleValue());
					buyingEgvResponce.setPaybleAmount(giftCartData.getTotalPrice().getDoubleValue());
				}
				if (null != giftCartData.getTotalDiscounts())
				{
					buyingEgvResponce.setDiscounts(giftCartData.getTotalDiscounts().getDoubleValue());
				}
				if (null != giftCartData.getGuid())
				{
					buyingEgvResponce.setEgvCartGuid(giftCartData.getGuid());
				}
				buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			buyingEgvResponce.setErrorCode(ex.getErrorCode());
			buyingEgvResponce.setError(ex.getErrorMessage());
			LOG.error("Exception occrred Creating  Electronics Gift Card Guid" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			buyingEgvResponce.setError(ex.getMessage());
			LOG.error("Exception occrred Creating  Electronics Gift Card Guid" + ex.getMessage());
		}

		return buyingEgvResponce;
	}
	
	private EgvDetailsData populateEGVFormToData(final BuyingEgvRequestWsDTO requestData)
	{
		final EgvDetailsData egvDetailsData = new EgvDetailsData();
		if (null != requestData)
		{
			egvDetailsData.setProductCode(requestData.getProductID());
			if (null != requestData.getPriceSelectedByUserPerQuantity()
					&& requestData.getPriceSelectedByUserPerQuantity().doubleValue() > 0.0D)
			{
				egvDetailsData.setGiftRange(requestData.getPriceSelectedByUserPerQuantity().doubleValue());
			}
			if (null != requestData.getPriceSelectedByUserPerQuantity()
					&& requestData.getPriceSelectedByUserPerQuantity().doubleValue() > 0.0D)
			{
				egvDetailsData.setOpenTextAmount(requestData.getPriceSelectedByUserPerQuantity().doubleValue());
			}
			if (null != requestData.getReceiverEmailID())
			{
				egvDetailsData.setToEmailAddress(requestData.getReceiverEmailID());
			}
			if (null != requestData.getFrom())
			{
				egvDetailsData.setFromEmailAddress(requestData.getFrom());
			}
			if (null != requestData.getPriceSelectedByUserPerQuantity())
			{
				egvDetailsData.setGiftRange(requestData.getPriceSelectedByUserPerQuantity().doubleValue());
			}
			if (null != requestData.getMobileNumber())
			{
				egvDetailsData.setFromPhoneNo(requestData.getMobileNumber());
			}

			if (null != requestData.getMessageOnCard())
			{
				egvDetailsData.setMessageBox(requestData.getMessageOnCard());
			}

			egvDetailsData.setTotalEGV(requestData.getQuantity());
		}

		return egvDetailsData;
	}
	
	
	
	@Secured(
		{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
		@RequestMapping(value = MarketplacewebservicesConstants.RESEND_NOTIFICATION_EGV, method = RequestMethod.POST, produces = APPLICATION_TYPE)
		@ResponseBody
		public ResendEGVNotificationWsDTO resendNotificationEgv(@RequestParam final String orderId)
				throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

{
	ResendEGVNotificationWsDTO responce = new ResendEGVNotificationWsDTO();
	try
	{

		if (orderId != null)
		{
			LOG.info("SendNotificationRecipient  ");
			mplOrderFacade.sendNotificationEGVOrder(orderId);
			responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
		}
	}
	catch (final Exception ex)
	{
		responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
		responce.setError(ex.getMessage());
		LOG.error("Exception occrred Creating  Electronics Gift Card Guid" + ex.getMessage());
	}
	return responce;
}

@Secured(
		{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
		@RequestMapping(value = MarketplacewebservicesConstants.USER_CLIQCASH_DETAILS, method = RequestMethod.POST, produces = APPLICATION_TYPE)
		@ResponseBody
		public UserCliqCashWsDto getUserCliqCashDetails()
				throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

	{
		UserCliqCashWsDto responce = new UserCliqCashWsDto();
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			if (null != currentCustomer)
			{
				responce = mplEgvWalletService.getUserCliqCashDetails(currentCustomer);
				if (null == responce)
				{
					responce = new UserCliqCashWsDto();
					responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				}
			}
			else
			{
				responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				responce.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B5002));
				responce.setErrorCode(MarketplacecommerceservicesConstants.B5002);
			}
		}

		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				responce.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				responce.setErrorCode(e.getErrorCode());
			}
			responce.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ex)
		{
			{
				responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				responce.setError(ex.getMessage());
				LOG.error("Exception occrred Getting Cliq Cash Details of user " + ex.getMessage());
			}
		}
		return responce;
	}


@Secured(
		{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
		@RequestMapping(value = MarketplacewebservicesConstants.CHECK_WALLET_MOBILE_NUMBER, method = RequestMethod.POST, produces = APPLICATION_TYPE)
		@ResponseBody
		public EgvCheckMobileNumberWsDto checkWalletMobileNumber(@RequestBody final EgvWalletCreateRequestWsDTO request ,@RequestParam(required=false) boolean isUpdateProfile)
				throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

	{
		EgvCheckMobileNumberWsDto responce = new EgvCheckMobileNumberWsDto();
		try
		{
			responce = mplEgvWalletService.checkWalletMobileNumber(request, isUpdateProfile);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorCode())
			{
				responce.setErrorCode(e.getErrorCode());
				responce.setError(Localization.getLocalizedString(e.getErrorCode()));
			}
			responce.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ex)
		{
			{
				responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				responce.setError(ex.getMessage());
				LOG.error("Exception occrred while saving mobile number for QC wallet " + ex.getMessage());
			}
		}
		return responce;
	}




@Secured(
		{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
		@RequestMapping(value = MarketplacewebservicesConstants.VERIFY_WALLET_OTP, method = RequestMethod.POST, produces = APPLICATION_TYPE)
		@ResponseBody
		public EgvWalletCreateResponceWsDTO verifyWalletOtp(@RequestParam final String otp)
				throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

	{
		EgvWalletCreateResponceWsDTO 	responce = new EgvWalletCreateResponceWsDTO();

		try
		{
			if (null != otp && !otp.isEmpty())
			{
				final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
					responce=	mplEgvWalletService.verifyOtpAndCreateWallet(currentCustomer, otp);
					if(null != responce) {
						responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						responce.setMessage(MarketplacecommerceservicesConstants.WALLET_ACTIVATED_MESSAGE);
					}

			}else {
				responce.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B5014));
				responce.setErrorCode(MarketplacecommerceservicesConstants.B5014);
				responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorCode())
			{
				responce.setError(Localization.getLocalizedString(e.getErrorCode()));
				responce.setErrorCode(e.getErrorCode());
			}
			responce.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ex)
		{
			{
				responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				responce.setError(ex.getMessage());
				LOG.error("Exception occrred while saving mobile number for QC wallet " + ex.getMessage());
			}
		}
		return responce;
	}
}
