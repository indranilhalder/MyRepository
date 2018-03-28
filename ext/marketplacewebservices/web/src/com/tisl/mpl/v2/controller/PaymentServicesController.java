/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.bin.service.BinService;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.MplPromoPriceWsDTO;
import com.tisl.mpl.data.SavedCardData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.facades.cms.data.WalletCreateData;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.model.MplCartOfferVoucherModel;
import com.tisl.mpl.model.PaymentModeRestrictionModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.service.MplCartWebService;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.CliqCashWsDto;
import com.tisl.mpl.wsdto.MplSavedCardDTO;
import com.tisl.mpl.wsdto.PaymentServiceWsDTO;
import com.tisl.mpl.wsdto.PaymentServiceWsData;
import com.tisl.mpl.wsdto.PriceWsPwaDTO;
import com.tisl.mpl.wsdto.TotalCliqCashBalanceWsDto;


/**
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/payments", headers = "Accept=application/xml,application/json")
public class PaymentServicesController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(PaymentServicesController.class);
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource
	private MplPaymentWebFacade mplPaymentWebFacade;
	//	@Resource(name = "mplPaymentWebDAO")
	//	private MplPaymentWebDAO mplPaymentWebDAO;
	//	@Resource(name = "cartService")
	//	private CartService cartService;
	@Resource(name = "mplCheckoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;
	//	@Resource(name = "customerFacade")
	//	private CustomerFacade customerFacade;
	@Resource(name = "discountUtility")
	private DiscountUtility discountUtility;
	@Resource(name = "binService")
	private BinService binService;
	@Resource(name = "mplPaymentService")
	private MplPaymentService mplPaymentService;
	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;
	@Resource
	private ModelService modelService;
	@Resource
	private ExtendedUserService extUserService;
	@Resource
	private Converter<CartModel, CartData> mplExtendedCartConverter;
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "mplCouponFacade")
	private MplCouponFacade mplCouponFacade;

	private static final String CUSTOMER = "ROLE_CUSTOMERGROUP";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final String MPLCARTVOUCHER = "mplcartvoucher";
	private static final String PROMOVOUCHER = "promovoucher";
	private static final String DECIMAL_FORMAT = "0.00";

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "cartService")
	protected CartService cartService;

	@Resource(name = "voucherService")
	private VoucherService voucherService;

	@Resource
	private MplCartWebService mplCartWebService;

	@Autowired
	private CommonI18NService commonI18NService;
	private static final String INR = "INR";

	@Autowired
	private MplWalletFacade mplWalletFacade;

	@Resource(name = "mplDefaultPriceDataFactory")
	private DefaultPriceDataFactory PriceDataFactory;

	//	@Autowired
	//	private MplEGVCartService mplEGVCartService;

	@Autowired
	private NotificationFacade notificationFacade;

	@Autowired
	private PriceDataFactory priceDataFactory;

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

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	//  COD Eligible Check
	/**
	 * @Description COD Eligibility check for all the items in the cart (check Bleack list Consumer Sailor Fulfillment
	 *              Item Eligibility and Pin Code Eligibility)
	 * @param pinCode
	 * @param cartID
	 *
	 * @return PaymentServiceWsDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.CODELIGIBILITYURL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public PaymentServiceWsDTO getCODEligibility(@RequestParam final String cartGuid, @PathVariable final String userId,
			final HttpServletRequest request, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		PaymentServiceWsData codCheck = new PaymentServiceWsData();
		PaymentServiceWsDTO codCheckDTO = new PaymentServiceWsDTO();
		CustomerModel customer = null;
		LOG.debug(String.format("getCODEligibility : cartGuid:  %s | userId : %s ", cartGuid, userId));
		//INC144316663
		final Long codUpperLimit = getBaseStoreService().getCurrentBaseStore().getCodUpperLimit();
		final Long codLowerLimit = getBaseStoreService().getCurrentBaseStore().getCodLowerLimit();

		try
		{
			//COD form handled for both cart and order
			OrderModel orderModel = null;
			if (StringUtils.isNotEmpty(cartGuid))
			{
				//final String orderGuid = decryptKey(guid);
				orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}

			//final String ip = getBlacklistByIPStatus(); TISPT-204 Point No 2
			final String ip = getMplPaymentFacade().getBlacklistByIPStatus(request);
			LOG.debug("The ip of the system is::::::::::::::::::::::::" + ip);

			//CAR Project performance issue fixed
			//customer = getMplPaymentWebFacade().getCustomer(userId);
			customer = (CustomerModel) userService.getCurrentUser();

			if (null == orderModel)
			{
				//CAR Project performance issue fixed
				final CartModel cart = getMplPaymentWebFacade().findCartAnonymousValues(cartGuid);
				//final CartModel cart = cartService.getSessionCart();

				if (null != cart)
				{
					final boolean mplCustomerIsBlackListed = null != customer ? getMplPaymentFacade().isBlackListed(ip, cart) : true;

					//INC144316663

					final boolean isCodLimitFailed = ((cart.getTotalPrice().longValue() <= codUpperLimit.longValue()) && (cart
							.getTotalPrice().longValue() >= codLowerLimit.longValue())) ? false : true;

					final boolean isCodEligible = (isCodLimitFailed || !cart.getIsCODEligible().booleanValue()) ? false : true;

					//To check if the customer is a black listed customer and cod upper/lower limit checking
					if (!mplCustomerIsBlackListed && isCodEligible)
					{
						//Getting COD details
						codCheck = getMplPaymentWebFacade().getCODDetails(cart, customer.getUid());
						//codCheck.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					}
					else if (!isCodEligible && !mplCustomerIsBlackListed) //COD LIMIT CHECKING
					{
						//Message to display COD not eligible
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9202);
					}
					else
					{
						//Message to display Customer is Black list Consumer
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9301);
					}

					//Putting data into DTOs
					codCheckDTO = dataMapper.map(codCheck, PaymentServiceWsDTO.class, fields);

				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9064);
				}
			}
			else
			{
				final boolean mplCustomerIsBlackListed = getMplPaymentFacade().isBlackListed(ip, orderModel);
				//SDI-5095
				final boolean isCodLimitFailed = ((orderModel.getTotalPrice().longValue() <= codUpperLimit.longValue()) && (orderModel
						.getTotalPrice().longValue() >= codLowerLimit.longValue())) ? false : true;

				//SDI-5095
				final boolean isCodEligible = (isCodLimitFailed || !orderModel.getIsCODEligible().booleanValue()) ? false : true;
				//To check if the customer is a black listed customer and cod upper/lower limit checking
				//SDI-5095
				if (!mplCustomerIsBlackListed && isCodEligible)
				{
					//Getting COD details
					codCheck = getMplPaymentWebFacade().getCODDetails(orderModel, customer.getUid());
					//codCheck.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
				//SDI-5095
				else if (!isCodEligible && !mplCustomerIsBlackListed) //COD LIMIT CHECKING
				{
					//Message to display COD not eligible
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9202);
				}
				else
				{
					//Message to display Customer is Black list Consumer
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9301);
				}
				//Putting data into DTOs
				codCheckDTO = dataMapper.map(codCheck, PaymentServiceWsDTO.class, fields);

			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				codCheckDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				codCheckDTO.setErrorCode(e.getErrorCode());
			}
			codCheckDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				codCheckDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				codCheckDTO.setErrorCode(e.getErrorCode());
			}
			codCheckDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			codCheckDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
			codCheckDTO.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			codCheckDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return codCheckDTO;
	}

	/**
	 * Check Valid Bin Number and Apply promotion for new char and saved card --TPR-629
	 *
	 * @param binNo
	 * @param bankName
	 * @param paymentMode
	 * @return MplPromotionDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.BINVALIDATIONURL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public MplPromoPriceWsDTO binValidation(@PathVariable final String userId, @RequestParam final String paymentMode,
			@RequestParam final String cartGuid, @RequestParam(required = false) final String binNo,
			@RequestParam(required = false) final String bankName, @RequestParam(required = false) final boolean isPwa)
	{
		LOG.debug(String.format("binValidation : binNo :  %s | paymentMode : %s | cartGuid : %s | userId : %s | bankName : %s ",
				binNo, paymentMode, cartGuid, userId, bankName));
		MplPromoPriceWsDTO promoPriceData = new MplPromoPriceWsDTO(); //The New Returning DTO
		//final PriceWsPwaDTO pricePwa = new PriceWsPwaDTO();

		OrderModel orderModel = null;
		CartModel cart = null;
		try
		{
			if (StringUtils.isNotEmpty(cartGuid))
			{
				//final String orderGuid = decryptKey(guid);
				orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}

			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				cartService.setSessionCart(cart);
				//TISPT-29
				if (null != cart)
				{
					if (StringUtils.isNotEmpty(paymentMode)
							&& (paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.CREDIT)
									|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.DEBIT)
									|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.NETBANKING)
									|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.EMI)
									|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.MRUPEE)
									|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD) || paymentMode
										.equalsIgnoreCase(MarketplacewebservicesConstants.PAYTM)))
					{
						if (!paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD))
						{
							//setting in cartmodel
							cart.setConvenienceCharges(Double.valueOf(0));
							//saving cartmodel
							modelService.save(cart);
						}

						if (getMplCheckoutFacade().isPromotionValid(cart))
						{
							//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION, paymentMode);
							promoPriceData = getMplPaymentWebFacade().binValidation(binNo, paymentMode, cart, userId, bankName);
							if (isPwa)
							{
								final PriceWsPwaDTO pricePwa = mplCartWebService.configureCartAmountPwa(cart);
								promoPriceData.setCartAmount(pricePwa);

								//								final Double mrp = mplCartWebService.calculateCartTotalMrp(cart);
								//								final PriceData totalMrp = createPriceObj(mrp.toString());
								//								pricePwa.setBagTotal(totalMrp);
								//								final PriceData amountInclDelCharge = promoPriceData.getTotalPriceInclConv();
								//								final PriceData payableAmount = promoPriceData.getTotalPrice();
								//								//	final PriceData amountInclDelCharge = createPriceObj(promoPriceData.getTotalPriceInclConv().toString());
								//								pricePwa.setPaybleAmount(payableAmount);
								//								final PriceData delCharge = promoPriceData.getDeliveryCost();
								//								final double payableamtWdDelCharge = amountInclDelCharge.getDoubleValue().doubleValue()
								//										- delCharge.getDoubleValue().doubleValue();
								//								final double discount = mrp.doubleValue() - payableamtWdDelCharge;
								//								pricePwa.setTotalDiscountAmount(createPriceObj(Double.valueOf(discount).toString()));
								//								//pricePwa.setTotalDiscountAmount(totalDiscount);
								//								promoPriceData.setCartAmount(pricePwa);
							}

						}
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9075);
						}

						if (null != cart.getSplitModeInfo()
								&& cart.getSplitModeInfo().equalsIgnoreCase(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT))
						{

							if (null != cart.getPayableWalletAmount() && cart.getPayableWalletAmount().doubleValue() > 0.0D)
							{
								//	promoPriceData.setPaybleAmount(Double.valueOf(cart.getTotalPrice().doubleValue()-cart.getPayableWalletAmount().doubleValue()));
								final Double juspayAmount = Double.valueOf(cart.getTotalPrice().doubleValue()
										- cart.getPayableWalletAmount().doubleValue());

								if (null != juspayAmount)
								{
									final Double amount = juspayAmount;
									final BigDecimal bigDecimal = new BigDecimal(amount.doubleValue());
									final String decimalFormat = DECIMAL_FORMAT;
									final DecimalFormat df = new DecimalFormat(decimalFormat);
									final String totalPayableAmount = df.format(bigDecimal);
									promoPriceData.setPaybleAmount(totalPayableAmount);
								}
								promoPriceData.setCliqCashApplied(true);
							}
						}
						else if (null != cart.getSplitModeInfo()
								&& cart.getSplitModeInfo().equalsIgnoreCase(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH))
						{
							//	promoPriceData.setPaybleAmount(Double.valueOf(0.0D));

							final Double amount = Double.valueOf(0.0D);
							final BigDecimal bigDecimal = new BigDecimal(amount.doubleValue());
							final String decimalFormat = DECIMAL_FORMAT;
							final DecimalFormat df = new DecimalFormat(decimalFormat);
							final String totalPayableAmount = df.format(bigDecimal);
							promoPriceData.setPaybleAmount(totalPayableAmount);
							promoPriceData.setCliqCashApplied(true);
						}
						else
						{
							//promoPriceData.setPaybleAmount(cart.getTotalPrice());
							if (null != cart.getTotalPrice())
							{
								final Double amount = cart.getTotalPrice();
								final BigDecimal bigDecimal = new BigDecimal(amount.doubleValue());
								final String decimalFormat = DECIMAL_FORMAT;
								final DecimalFormat df = new DecimalFormat(decimalFormat);
								final String totalPayableAmount = df.format(bigDecimal);
								promoPriceData.setPaybleAmount(totalPayableAmount);
							}
							promoPriceData.setCliqCashApplied(false);
						}
						setTotalPrice(promoPriceData, cart);
					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9053);
					}
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
				}

			}
			else
			{
				if (StringUtils.isNotEmpty(bankName) && !bankName.equalsIgnoreCase("null"))
				{
					getMplPaymentFacade().setBankForSavedCard(bankName);
				}

				if (StringUtils.isNotEmpty(paymentMode)
						&& (paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.CREDIT)
								|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.DEBIT)
								|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.NETBANKING)
								|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.EMI)
								|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.MRUPEE)
								|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD) || paymentMode
									.equalsIgnoreCase(MarketplacewebservicesConstants.PAYTM)))

				{
					if (!paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD))
					{
						//setting in cartmodel
						orderModel.setConvenienceCharges(Double.valueOf(0));
						//saving cartmodel
						modelService.save(orderModel);
					}

					if (getMplCheckoutFacade().isPromotionValid(orderModel))
					{
						//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION, paymentMode);
						promoPriceData = getMplPaymentWebFacade().binValidation(binNo, paymentMode, orderModel, userId, bankName);
						if (isPwa)
						{
							final PriceWsPwaDTO pricePwa = mplCartWebService.configureCartAmountPwa(orderModel);
							promoPriceData.setCartAmount(pricePwa);
						}
					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9075);
					}
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9053);
				}

				if (null != orderModel.getSplitModeInfo()
						&& orderModel.getSplitModeInfo().equalsIgnoreCase(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT))
				{

					if (null != orderModel.getPayableWalletAmount() && orderModel.getPayableWalletAmount().doubleValue() > 0.0D)
					{
						///	promoPriceData.setPaybleAmount(Double.valueOf(orderModel.getTotalPrice().doubleValue()-orderModel.getPayableWalletAmount().doubleValue()));
						final Double juspayAmount = Double.valueOf(orderModel.getTotalPrice().doubleValue()
								- orderModel.getPayableWalletAmount().doubleValue());
						if (null != juspayAmount)
						{
							final Double amount = juspayAmount;
							final BigDecimal bigDecimal = new BigDecimal(amount.doubleValue());
							final String decimalFormat = DECIMAL_FORMAT;
							final DecimalFormat df = new DecimalFormat(decimalFormat);
							final String totalPayableAmount = df.format(bigDecimal);
							promoPriceData.setPaybleAmount(totalPayableAmount);
						}

						promoPriceData.setCliqCashApplied(true);
					}
				}
				else if (null != orderModel.getSplitModeInfo()
						&& orderModel.getSplitModeInfo().equalsIgnoreCase(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH))
				{
					//	promoPriceData.setPaybleAmount(Double.valueOf(0.0D));
					final Double amount = Double.valueOf(0.0D);
					final BigDecimal bigDecimal = new BigDecimal(amount.doubleValue());
					final String decimalFormat = DECIMAL_FORMAT;
					final DecimalFormat df = new DecimalFormat(decimalFormat);
					final String totalPayableAmount = df.format(bigDecimal);
					promoPriceData.setPaybleAmount(totalPayableAmount);
					promoPriceData.setCliqCashApplied(true);
				}
				else
				{
					if (null != orderModel.getTotalPrice())
					{
						final Double amount = orderModel.getTotalPrice();
						final BigDecimal bigDecimal = new BigDecimal(amount.doubleValue());
						final String decimalFormat = DECIMAL_FORMAT;
						final DecimalFormat df = new DecimalFormat(decimalFormat);
						final String totalPayableAmount = df.format(bigDecimal);
						promoPriceData.setPaybleAmount(totalPayableAmount);
					}
					promoPriceData.setCliqCashApplied(false);
				}
				setTotalPrice(promoPriceData, orderModel);

			}

		}
		catch (final ModelSavingException ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.getCustomizedExceptionTrace(ex);
			// Error message for All Exceptions
			if (null != ex.getMessage())
			{
				promoPriceData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				promoPriceData.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				promoPriceData.setError(ex.getErrorMessage());
				promoPriceData.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				promoPriceData.setError(ex.getErrorMessage());
				promoPriceData.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			// Error message for All Exceptions
			if (null != e.getMessage())
			{
				promoPriceData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				promoPriceData.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
		}
		return promoPriceData;
	}

	/**
	 * @param promoPriceData
	 * @param cart
	 */
	private void setTotalPrice(final MplPromoPriceWsDTO promoPriceData, final AbstractOrderModel cartModel)
	{
		final double payableWalletAmount = cartModel.getPayableWalletAmount().doubleValue();
		double bankCouponDiscount = 0.0D;
		double userCouponDiscount = 0.0D;
		double otherDiscount = 0.0D;
		double totalDiscount = 0.0D;
		double productDiscount = 0.0D;
		final List<DiscountValue> discountList = cartModel.getGlobalDiscountValues(); // discounts on the cart itself
		final List<DiscountModel> voucherList = cartModel.getDiscounts();

		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			if (null != entry.getGiveAway() && !entry.getGiveAway().booleanValue())
			{
				productDiscount = (null != entry.getTotalProductLevelDisc() && entry.getTotalProductLevelDisc().doubleValue() > 0) ? entry
						.getTotalProductLevelDisc().doubleValue() : 0;
			}
		}
		if (CollectionUtils.isNotEmpty(discountList))
		{
			for (final DiscountValue discount : discountList)
			{
				totalDiscount += discount.getAppliedValue();
				for (final DiscountModel voucher : voucherList)
				{
					if (discount.getCode().equalsIgnoreCase(voucher.getCode()))
					{

						final double value = discount.getAppliedValue();
						if ((voucher instanceof PromotionVoucherModel) && !(voucher instanceof MplCartOfferVoucherModel))
						{
							if (value > 0.0d)
							{
								userCouponDiscount += value;
							}
						}
						else if (voucher instanceof MplCartOfferVoucherModel)
						{
							if (value > 0.0d)
							{
								bankCouponDiscount += value;
							}
						}


					}
				}

			}
		}
		otherDiscount = totalDiscount + productDiscount - userCouponDiscount;
		BigDecimal total = new BigDecimal(0);
		final double remainingWalletAmount = cartModel.getTotalWalletAmount().doubleValue() - payableWalletAmount;
		if (null != cartModel.getSubtotal())
		{
			total = new BigDecimal(cartModel.getSubtotal().doubleValue());
			final PriceData subTotalPriceData = priceDataFactory.create(PriceDataType.BUY, total,
					MarketplacecommerceservicesConstants.INR);
			promoPriceData.setSubTotalPrice(subTotalPriceData);

		}

		if (null != cartModel.getDeliveryCost())
		{
			total = new BigDecimal(cartModel.getDeliveryCost().doubleValue());
			final PriceData deliveryChargesPriceData = priceDataFactory.create(PriceDataType.BUY, total,
					MarketplacecommerceservicesConstants.INR);
			promoPriceData.setDeliveryCharges(deliveryChargesPriceData);
		}
		total = new BigDecimal(otherDiscount);
		final PriceData otherDiscountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		promoPriceData.setOtherDiscount(otherDiscountPriceData);
		if (bankCouponDiscount > 0.0D)
		{
			promoPriceData.setIsBankPromotionApplied(true);
		}
		total = new BigDecimal(userCouponDiscount);
		final PriceData couponPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		promoPriceData.setCouponDiscount(total.toString());

		promoPriceData.setAppliedCouponDiscount(couponPriceData);

		if (payableWalletAmount > 0.0D)
		{
			promoPriceData.setCliqCashApplied(true);

		}

		total = new BigDecimal(payableWalletAmount);
		final PriceData payableWalletAmountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		promoPriceData.setCliqCashPaidAmount(payableWalletAmountPriceData);

		total = new BigDecimal(remainingWalletAmount);
		final PriceData remainingWalletAmountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		promoPriceData.setCliqCashBalance(remainingWalletAmountPriceData);

		if (null != cartModel.getTotalPrice())
		{
			total = new BigDecimal(cartModel.getTotalPrice().doubleValue() - payableWalletAmount);
			final PriceData cartTotalPriceData = priceDataFactory.create(PriceDataType.BUY, total,
					MarketplacecommerceservicesConstants.INR);
			promoPriceData.setTotalPrice(cartTotalPriceData);

			if ((cartModel.getTotalPrice().doubleValue() - payableWalletAmount) > 0.0D)
			{
				promoPriceData.setIsRemainingAmount(true);
			}
		}
	}

	/**
	 * This method Provided all the Saved Card Details
	 *
	 * @param cardType
	 * @param userId
	 * @return MplSavedCardDTO
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.SAVEDCARDS, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public MplSavedCardDTO savedCards(@RequestParam final String cardType, @PathVariable final String userId,
			@RequestParam(required = false) final String bankName)
	{
		LOG.debug(String.format("savedCards :  cardType : %s bankName : %s userId : %s| ", cardType, bankName, userId));


		final MplSavedCardDTO savedCards = new MplSavedCardDTO();
		// Create Map to get saved card details
		Map<Date, SavedCardData> savedCardsMap = new TreeMap<Date, SavedCardData>();
		Map<Date, SavedCardData> savedDebitCards = new TreeMap<Date, SavedCardData>();
		try
		{
			//CAR-104
			//CustomerModel customer = modelService.create(CustomerModel.class);
			//customer = getMplPaymentWebFacade().getCustomer(userId);
			final CustomerModel customer = (CustomerModel) userService.getCurrentUser();

			//validate if all the inputs are available
			//			if (StringUtils.isNotEmpty(cardType) || StringUtils.isNotEmpty(bankName))
			//			{
			// If card type is Credit Card
			if (cardType.equalsIgnoreCase(MarketplacewebservicesConstants.CC) && StringUtils.isEmpty(bankName))
			{
				savedCardsMap = getMplPaymentFacade().listStoredCreditCards(customer);
				savedCards.setSavedCardDetailsMap(savedCardsMap);
			}
			// If card type is Debit Card
			else if (cardType.equalsIgnoreCase(MarketplacewebservicesConstants.DC) && StringUtils.isEmpty(bankName))
			{
				savedCardsMap = getMplPaymentFacade().listStoredDebitCards(customer);
				savedCards.setSavedCardDetailsMap(savedCardsMap);
			}
			// if the card type is EMI
			else if (cardType.equalsIgnoreCase(MarketplacewebservicesConstants.CC) && StringUtils.isNotEmpty(bankName))
			{
				savedCardsMap = getMplPaymentFacade().listStoredEMICards(customer, bankName);
				savedCards.setSavedCardDetailsMap(savedCardsMap);
			}
			// If card type is Both
			else if (cardType.equalsIgnoreCase(MarketplacewebservicesConstants.CCDC_BOTH) && StringUtils.isEmpty(bankName))
			{
				savedCardsMap = getMplPaymentFacade().listStoredCreditCards(customer);
				savedDebitCards = getMplPaymentFacade().listStoredDebitCards(customer);
				// Add everything in savedCardsMap from savedDebitCards
				savedCardsMap.putAll(savedDebitCards);
				//Adding details into DTO
				savedCards.setSavedCardDetailsMap(savedCardsMap);
			}
			else if (StringUtils.isEmpty(cardType) && StringUtils.isEmpty(bankName))
			{
				// else provide error message
				savedCards.setError(MarketplacewebservicesConstants.CARDTYPE_CONID);
			}


			if (null == savedCards.getSavedCardDetailsMap() || savedCards.getSavedCardDetailsMap().isEmpty())
			{
				savedCards.setStatus(MarketplacewebservicesConstants.CARDDATA);
			}
			//			}

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions

			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				savedCards.setError(ex.getErrorMessage());
				savedCards.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				savedCards.setError(ex.getErrorMessage());
				savedCards.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				savedCards.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
				savedCards.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}
		return savedCards;
	}

	/**
	 * This method fetches delete the saved cards
	 *
	 * @param cardToken
	 * @return MplSavedCardDTO
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.REMOVESAVEDCARDS, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public MplSavedCardDTO removeSavedCards(@RequestParam final String cardToken)
	{
		LOG.debug(String.format("removeSavedCards : cardToken: %s| ", cardToken));
		MplSavedCardDTO deleteCardDetails = new MplSavedCardDTO();
		try
		{
			// Return if deleted or not
			if (StringUtils.isNotEmpty(cardToken))
			{
				deleteCardDetails = getMplPaymentWebFacade().deleteSavedCards(cardToken);
			}
			else
			{
				deleteCardDetails.setError(MarketplacewebservicesConstants.NOCARDTOKEN);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				deleteCardDetails.setError(ex.getErrorMessage());
				deleteCardDetails.setErrorCode(ex.getErrorCode());
			}
		}

		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				deleteCardDetails.setError(ex.getErrorMessage());
				deleteCardDetails.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				deleteCardDetails.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
				deleteCardDetails.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}
		return deleteCardDetails;
	}


	/**
	 * @description Update Transaction and related Retails for COD also create Order --TPR-629
	 * @param otpPin
	 * @return PaymentServiceWsData
	 * @throws EtailNonBusinessExceptions
	 */

	//GetOrderStatusResponse
	@Secured(
	{ "ROLE_CLIENT", CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.UPDATETRANSACTIONFORCODURL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public PaymentServiceWsData updateTransactionDetailsforCOD(@PathVariable final String userId,
			@RequestParam(required = false) final String otpPin, @RequestParam final String cartGuid)
	{
		final PaymentServiceWsData updateTransactionDtls = new PaymentServiceWsData();
		OrderModel orderModel = null;
		//OrderData orderData = null;
		CartModel cart = null;
		String failErrorCode = "";
		String validationMsg = "";
		boolean failFlag = false;
		String orderCode = null;
		LOG.debug(String.format("updateTransactionDetailsforCOD : CartId: %s | UserId : %s |", cartGuid, userId));
		try
		{
			//updateTransactionDtls = getMplPaymentWebFacade().updateCODTransactionDetails(cartGuid, userId);
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			if (null == customerData)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			//final UserModel user = getExtUserService().getUserForOriginalUid(userId);
			//final String validationMsg = getMplPaymentFacade().validateOTPforCODWeb(userId, otpPin);
			if (StringUtils.isNotEmpty(otpPin) && null != otpPin)
			{
				validationMsg = getMplPaymentFacade().validateOTPforCODWV(customerData.getDisplayUid(), otpPin);
			}
			else
			{
				validationMsg = MarketplacecommerceservicesConstants.OTPVALIDITY;
			}
			//IF valid then proceed saving COD payment
			if (validationMsg.equalsIgnoreCase(MarketplacecommerceservicesConstants.OTPVALIDITY))
			{
				if (StringUtils.isNotEmpty(cartGuid))
				{
					//final String orderGuid = decryptKey(guid);
					orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
				}
				if (null == orderModel)
				{
					//getting Cartdata
					cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);

					//Logic when Payment mode is COD
					if (null != cart)
					{

						//TPR-4461 COUPON FOR COD WHEN ORDER MODEL IS NULL STARTS HERE
						final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService()
								.getAppliedVouchers(cart));

						if (CollectionUtils.isNotEmpty(voucherList))
						{
							VoucherModel appliedVoucher = null;
							//TPR-7486
							boolean mplCartVoucher = false;
							final Map<String, Boolean> voucherMap = new HashMap<String, Boolean>();

							//final DiscountModel discount = voucherList.get(0);
							for (final DiscountModel discount : voucherList)
							{

								if (discount instanceof PromotionVoucherModel)
								{
									//final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
									//appliedVoucher = promotionVoucherModel;
									//TPR-7486
									if (discount instanceof PromotionVoucherModel && !(discount instanceof MplCartOfferVoucherModel))
									{
										final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
										appliedVoucher = promotionVoucherModel;
										mplCartVoucher = false;
									}
									else
									{
										final MplCartOfferVoucherModel promotionVoucherModel = (MplCartOfferVoucherModel) discount;
										appliedVoucher = promotionVoucherModel;
										mplCartVoucher = true;
									}

									final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
									for (final RestrictionModel restriction : restrictions)
									{
										if (restriction instanceof PaymentModeRestrictionModel)
										{
											boolean willApply = false;


											final String paymentModeCard = cart.getModeOfPayment();//Card Payment Mode


											final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
													.getPaymentTypeData(); //Voucher Payment mode


											if (CollectionUtils.isNotEmpty(paymentTypeList))
											{
												if (StringUtils.isNotEmpty(paymentModeCard))
												{
													for (final PaymentTypeModel paymentType : paymentTypeList)
													{
														if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
														{
															willApply = true;
															break;
														}

													}
												}

											}
											if (mplCartVoucher)
											{ //MplCartOfferVoucherModel
												voucherMap.put(MPLCARTVOUCHER, Boolean.valueOf(willApply));
											}
											else
											{ //PromotionVoucherModel
												voucherMap.put(PROMOVOUCHER, Boolean.valueOf(willApply));
											}

											//if (willApply == false)
											/*
											 * if (!willApply)//SonarFix {
											 * updateTransactionDtls.setErrorMessage(MarketplacecommerceservicesConstants
											 * .COUPONFAILUREMESSAGE); failFlag = true; failErrorCode =
											 * MarketplacecommerceservicesConstants.B9078; }
											 */
										}

									}
								}
							}
							//ERROR MESSAGE FOR COUPON AND VOUCHER
							boolean checkcartVoucher1 = true;
							boolean checkPromovoucher2 = true;
							for (final Map.Entry<String, Boolean> voucherentry : voucherMap.entrySet())
							{

								if (voucherentry.getKey().equals(MPLCARTVOUCHER))
								{
									if (!voucherentry.getValue().booleanValue())
									{
										checkcartVoucher1 = false;
									}
								}
								if (voucherentry.getKey().equals(PROMOVOUCHER))
								{
									if (!voucherentry.getValue().booleanValue())
									{
										checkPromovoucher2 = false;
									}
								}
							}
							if (!checkcartVoucher1 && !checkPromovoucher2)
							{ //both coupon and voucher
								updateTransactionDtls
										.setErrorMessage(MarketplacecommerceservicesConstants.CARTANDCOUPONBOTHFAILUREMESSAGE);
								failFlag = true;
								failErrorCode = MarketplacecommerceservicesConstants.B9078;
							}
							else if (!checkcartVoucher1)
							{ // only voucher
								updateTransactionDtls.setErrorMessage(MarketplacecommerceservicesConstants.CARTCOUPONFAILUREMESSAGE);
								failFlag = true;
								failErrorCode = MarketplacecommerceservicesConstants.B9078;
							}
							else if (!checkPromovoucher2)
							{ //only coupon
							  //return "coupon";
								updateTransactionDtls.setErrorMessage(MarketplacecommerceservicesConstants.COUPONFAILUREMESSAGE);
								failFlag = true;
								failErrorCode = MarketplacecommerceservicesConstants.B9078;
							}
							//ERROR MESSAGE FOR COUPON AND VOUCHER
						}

						//TPR-4461 COUPON FOR COD END WHEN ORDER MODEL IS NULL ENDS HERE




						//Adding cartdata into model
						final Double cartValue = cart.getSubtotal();
						final Double totalCODCharge = cart.getConvenienceCharges();



						//Mandatory checks agains cart
						if (!getMplPaymentFacade().checkCart(cart))
						{
							failFlag = true;
							failErrorCode = MarketplacecommerceservicesConstants.B9064;
						}

						if (!getMplCheckoutFacade().isPromotionValid(cart))
						{
							failFlag = true;
							failErrorCode = MarketplacecommerceservicesConstants.B9075;
						}

						//TISST-13012
						final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cart);
						if (!failFlag && cartItemDelistedStatus)
						{
							failFlag = true;
							failErrorCode = MarketplacecommerceservicesConstants.B9325;
						}

						//TISUTO-12 , TISUTO-11
						/*
						 * if (!failFlag && !mplCartFacade.isInventoryReservedMobile(
						 * MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel, pincode))
						 * { //getSessionService().setAttribute(MarketplacecclientservicesConstants.
						 * OMS_INVENTORY_RESV_SESSION_ID,"TRUE"); //getMplCartFacade().recalculate(cart); failFlag = true;
						 * failErrorCode = MarketplacecommerceservicesConstants.B9047; }
						 */

						if (!failFlag && !getMplCheckoutFacade().isCouponValid(cart))
						{
							//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
							failFlag = true;
							failErrorCode = MarketplacecommerceservicesConstants.B9509;
						}


						if (failFlag)
						{
							throw new EtailBusinessExceptions(failErrorCode);
						}
						else
						{
							//saving COD Payment related info
							getMplPaymentFacade().saveCODPaymentInfo(cartValue, totalCODCharge, cart);
							//CAR-110
							//orderData = mplCheckoutFacade.placeOrderByCartId(cartGuid);
							orderCode = mplCheckoutFacade.placeOrderMobile(cart);
							//Please note: order data is now just order code
							if (orderCode == null)
							{
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9321);
							}
							else
							{
								updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
								updateTransactionDtls.setOrderId(orderCode);
							}
						}

					}
					else
					{
						LOG.error("Exception while completing COD Payment in /view");
						updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
						updateTransactionDtls.setError(MarketplacecommerceservicesConstants.INVALID_CART);
					}
				}
				else
				{
					//INC144315486
					boolean alreadyProcessed = false;
					final List<PaymentTransactionModel> paymentTransactionList = orderModel.getPaymentTransactions();

					if (CollectionUtils.isNotEmpty(paymentTransactionList))
					{
						for (final PaymentTransactionModel payTranModel : paymentTransactionList)
						{
							if (payTranModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
							{
								alreadyProcessed = true;
								break;
							}
						}
					}


					//TPR-4461 COUPON FOR COD WHEN ORDER MODEL IS NOT NULL STARTS HERE
					final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService().getAppliedVouchers(
							orderModel));

					if (CollectionUtils.isNotEmpty(voucherList))
					{
						VoucherModel appliedVoucher = null;
						//TPR-7486
						boolean mplCartVoucher = false;
						final Map<String, Boolean> voucherMap = new HashMap<String, Boolean>();

						//final DiscountModel discount = voucherList.get(0);
						for (final DiscountModel discount : voucherList)
						{


							if (discount instanceof PromotionVoucherModel)
							{
								//final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
								//appliedVoucher = promotionVoucherModel;
								//TPR-7486
								if (discount instanceof PromotionVoucherModel && !(discount instanceof MplCartOfferVoucherModel))
								{
									final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
									appliedVoucher = promotionVoucherModel;
									mplCartVoucher = false;
								}
								else
								{
									final MplCartOfferVoucherModel promotionVoucherModel = (MplCartOfferVoucherModel) discount;
									appliedVoucher = promotionVoucherModel;
									mplCartVoucher = true;
								}

								final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
								for (final RestrictionModel restriction : restrictions)
								{
									if (restriction instanceof PaymentModeRestrictionModel)
									{
										boolean willApply = false;


										final String paymentModeCard = orderModel.getModeOfOrderPayment();//Card Payment Mode


										final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
												.getPaymentTypeData(); //Voucher Payment mode


										if (CollectionUtils.isNotEmpty(paymentTypeList))
										{
											if (StringUtils.isNotEmpty(paymentModeCard))
											{
												for (final PaymentTypeModel paymentType : paymentTypeList)
												{
													if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
													{
														willApply = true;
														break;
													}

												}
											}

										}

										if (mplCartVoucher)
										{ //MplCartOfferVoucherModel
											voucherMap.put(MPLCARTVOUCHER, Boolean.valueOf(willApply));
										}
										else
										{ //PromotionVoucherModel
											voucherMap.put(PROMOVOUCHER, Boolean.valueOf(willApply));
										}

										//if (willApply == false)//SonarFix

									}

								}
							}
						}
						//ERROR MESSAGE FOR COUPON AND VOUCHER
						boolean checkcartVoucher1 = true;
						boolean checkPromovoucher2 = true;
						for (final Map.Entry<String, Boolean> voucherentry : voucherMap.entrySet())
						{

							if (voucherentry.getKey().equals(MPLCARTVOUCHER))
							{
								if (!voucherentry.getValue().booleanValue())
								{
									checkcartVoucher1 = false;
								}
							}
							if (voucherentry.getKey().equals(PROMOVOUCHER))
							{
								if (!voucherentry.getValue().booleanValue())
								{
									checkPromovoucher2 = false;
								}
							}
						}
						if (!checkcartVoucher1 && !checkPromovoucher2)
						{ //both coupon and voucher
							updateTransactionDtls.setErrorMessage(MarketplacecommerceservicesConstants.CARTANDCOUPONBOTHFAILUREMESSAGE);
							failFlag = true;
							failErrorCode = MarketplacecommerceservicesConstants.B9078;
						}
						else if (!checkcartVoucher1)
						{ // only voucher
							updateTransactionDtls.setErrorMessage(MarketplacecommerceservicesConstants.CARTCOUPONFAILUREMESSAGE);
							failFlag = true;
							failErrorCode = MarketplacecommerceservicesConstants.B9078;
						}
						else if (!checkPromovoucher2)
						{ //only coupon
						  //return "coupon";
							updateTransactionDtls.setErrorMessage(MarketplacecommerceservicesConstants.COUPONFAILUREMESSAGE);
							failFlag = true;
							failErrorCode = MarketplacecommerceservicesConstants.B9078;
						}
						//ERROR MESSAGE FOR COUPON AND VOUCHER
					}

					//INC144315486
					// TPR-4461 WHEN ORDER MODEL IS NOT NULL ENDS HERE
					final Double orderValue = orderModel.getSubtotal();
					final Double totalCODCharge = orderModel.getConvenienceCharges();



					//Mandatory checks agains order
					if (!getMplCheckoutFacade().isPromotionValid(orderModel))
					{
						failFlag = true;
						failErrorCode = MarketplacecommerceservicesConstants.B9075;
					}

					if (!alreadyProcessed)
					{
						//TISUTO-12 , TISUTO-11
						/*
						 * if (!failFlag && !mplCartFacade .isInventoryReservedMobile(
						 * MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel, pincode))
						 * { //getSessionService().setAttribute(MarketplacecclientservicesConstants.
						 * OMS_INVENTORY_RESV_SESSION_ID ,"TRUE"); //getMplCartFacade().recalculate(cart); failFlag = true;
						 * failErrorCode = MarketplacecommerceservicesConstants.B9047; }
						 */

						if (!failFlag && !getMplCheckoutFacade().isCouponValid(orderModel))
						{
							//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
							failFlag = true;
							failErrorCode = MarketplacecommerceservicesConstants.B9509;
						}

						if (failFlag)
						{
							throw new EtailBusinessExceptions(failErrorCode);
						}
						else
						{
							//saving COD Payment related info
							getMplPaymentFacade().saveCODPaymentInfo(orderValue, totalCODCharge, orderModel);
							// OrderIssues:-  multiple Payment Response from juspay restriction
							//adding Payment id to model
							if (mplPaymentWebFacade.updateOrder(orderModel))
							{
								updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
								updateTransactionDtls.setOrderId(orderModel.getCode());
							}
							else
							{
								updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
								updateTransactionDtls.setError(MarketplacecommerceservicesConstants.INVALIDORDERID);
							}
						}
					}
				}

				//				else
				//				{
				//					final Double orderValue = orderModel.getSubtotal();
				//					final Double totalCODCharge = orderModel.getConvenienceCharges();
				//
				//					//saving COD Payment related info
				//					getMplPaymentFacade().saveCODPaymentInfo(orderValue, totalCODCharge, orderModel);
				//
				//					//Mandatory checks agains order
				//					if (!getMplCheckoutFacade().isPromotionValid(orderModel))
				//					{
				//						failFlag = true;
				//						failErrorCode = MarketplacecommerceservicesConstants.B9075;
				//					}
				//
				//					//TISUTO-12 , TISUTO-11
				//					/*
				//					 * if (!failFlag && !mplCartFacade .isInventoryReservedMobile(
				//					 * MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel, pincode)) {
				//					 * //getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID
				//					 * ,"TRUE"); //getMplCartFacade().recalculate(cart); failFlag = true; failErrorCode =
				//					 * MarketplacecommerceservicesConstants.B9047; }
				//					 */
				//
				//					if (!failFlag && !getMplCheckoutFacade().isCouponValid(orderModel))
				//					{
				//						//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
				//						failFlag = true;
				//						failErrorCode = MarketplacecommerceservicesConstants.B9509;
				//					}
				//
				//					if (failFlag)
				//					{
				//						throw new EtailBusinessExceptions(failErrorCode);
				//					}
				//					else
				//					{
				//						final Map<String, String> duplicateJuspayResMap = getSessionService().getAttribute(
				//								MarketplacecommerceservicesConstants.DUPLICATEJUSPAYRESONSE);
				//						// OrderIssues:-  multiple Payment Response from juspay restriction
				//						if (MapUtils.isNotEmpty(duplicateJuspayResMap) && duplicateJuspayResMap.get(cartGuid).equalsIgnoreCase("False"))
				//						{
				//							//adding Payment id to model
				//							if (mplPaymentWebFacade.updateOrder(orderModel))
				//							{
				//								updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				//								updateTransactionDtls.setOrderId(orderModel.getCode());
				//							}
				//							else
				//							{
				//								updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				//								updateTransactionDtls.setError(MarketplacecommerceservicesConstants.INVALIDORDERID);
				//							}
				//						}
				//					}
				//				}

				updateTransactionDtls.setOtpStatus(MarketplacecommerceservicesConstants.OTP_SENT);
			}
			else if (validationMsg.equalsIgnoreCase(MarketplacecommerceservicesConstants.OTPEXPIRY))
			{
				updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				updateTransactionDtls.setError(MarketplacecommerceservicesConstants.OTP_EXPIRY_MESSAGE);
			}
			else
			{
				updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				updateTransactionDtls.setError(MarketplacecommerceservicesConstants.INVALID_OTP);
			}

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDtls.setError(ex.getErrorMessage());
				updateTransactionDtls.setErrorCode(ex.getErrorCode());
			}
			updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDtls.setError(ex.getErrorMessage());
				updateTransactionDtls.setErrorCode(ex.getErrorCode());
			}
			updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacewebservicesConstants.UPDATE_COD_TRAN_FAILED, e);
			// Error message for All Exceptions
			if (null != e.getMessage())
			{
				updateTransactionDtls.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				updateTransactionDtls.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return updateTransactionDtls;

	}

	/**
	 * @Description Update Transaction and related Retails for Credit Card /Debit Card / EMI and Create Card --TPR-629
	 * @param paymentMode
	 *           (Json)
	 * @return PaymentServiceWsData
	 * @throws EtailNonBusinessExceptions
	 */
	//GetOrderStatusResponse

	@Secured(
	{ "ROLE_CLIENT", CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.UPDATETRANSACTIONFORCARDURL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public PaymentServiceWsData updateTransactionDetailsforCard(@RequestParam(required = false) final String juspayOrderID,
			@RequestParam final String paymentMode, @PathVariable final String userId, @RequestParam final String cartGuid)
	{
		final PaymentServiceWsData updateTransactionDetail = new PaymentServiceWsData();
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("PaymentMode: %s | cartGuid: %s | UserId : %s | juspayOrderID : %s", paymentMode, cartGuid,
					userId, juspayOrderID));
		}
		OrderModel orderToBeUpdated = mplPaymentFacade.getOrderByGuid(cartGuid);
		String statusResponse = "";
		boolean alreadyProcessed = false;

		//		// Buying Of EGV Changes Start
		//		final CartModel cart = mplEGVCartService.getEGVCartModel(cartGuid);
		if (orderToBeUpdated != null && null != orderToBeUpdated.getIsEGVCart() && orderToBeUpdated.getIsEGVCart().booleanValue())
		{
			OrderData orderData = null;

			try
			{
				orderData = getEGVOrderStatus(cartGuid, paymentMode, juspayOrderID);
				if (null != orderData)
				{
					updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
					updateTransactionDetail.setOrderId(orderData.getCode());
				}
				else
				{
					updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_FAILURE);
				}
				return updateTransactionDetail;
			}
			catch (final InvalidCartException e)
			{
				LOG.error("Exception occurred while updateTransactionDetailsforCard " + e.getMessage());
				updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_FAILURE);
				updateTransactionDetail.setError(e.getMessage());
			}
			catch (final CalculationException e)
			{
				LOG.error("Exception occurred while updateTransactionDetailsforCard " + e.getMessage());
				updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_FAILURE);
			}
			return updateTransactionDetail;
		}

		// Buying Of EGV Changes End

		try
		{
			//final String orderGuid = decryptKey(guid);
			orderToBeUpdated = mplPaymentFacade.getOrderByGuid(cartGuid);
			//OrderIssue:- If PaymentTransaction with Success already been created, we wont allow to re process the order
			if (null != orderToBeUpdated)
			{
				if (CollectionUtils.isNotEmpty(orderToBeUpdated.getPaymentTransactions()))
				{
					for (final PaymentTransactionModel payTranModel : orderToBeUpdated.getPaymentTransactions())
					{
						if (payTranModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
						{
							alreadyProcessed = true;
							break;
						}
					}
				}

				if (!alreadyProcessed)
				{
					//  INC144314180  PRDI-25
					if (null == orderToBeUpdated.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(orderToBeUpdated.getStatus()))
					{
						//TODO Wallet amount assigned. Will be changed after release1
						//final double walletAmount = MarketplacewebservicesConstants.WALLETAMOUNT;
						final Map<String, Double> paymentInfo = new HashMap<String, Double>();
						//paymentInfo.put(paymentMode, Double.valueOf(orderToBeUpdated.getTotalPriceWithConv().doubleValue() - walletAmount));
						paymentInfo.put(paymentMode, Double.valueOf(orderToBeUpdated.getTotalPriceWithConv().doubleValue()));

						statusResponse = mplPaymentFacade.getOrderStatusFromJuspay(cartGuid, paymentInfo, orderToBeUpdated,
								juspayOrderID);
						//Redirection when transaction is successful i.e. CHARGED
						if (null != statusResponse)
						{
							if (MarketplacewebservicesConstants.CHARGED.equalsIgnoreCase(statusResponse))
							{
								//return placeOrder(model, redirectAttributes);

								// Paying The remaining amount through Wallet
								try
								{
									LOG.debug(" Paying Amount From QC ");
									if (null != orderToBeUpdated.getSplitModeInfo()
											&& orderToBeUpdated.getSplitModeInfo().equalsIgnoreCase(
													MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT))
									{
										//	double amountDeducted = payAmountThroughWallet(orderToBeUpdated);
										final double amountDeducted = orderToBeUpdated.getPayableWalletAmount().doubleValue();
										final boolean qcFlag = payAmountThroughWallet(orderToBeUpdated);

										if (qcFlag && amountDeducted > 0.0D)
										{
											updateTransactionDetail.setCliqCashAmountDeducted(Double.valueOf(amountDeducted));
											updateTransactionDetail.setCliqCashApplied(true);
										}
										if (!qcFlag)
										{
											updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
											updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());
											return updateTransactionDetail;
										}
									}
								}
								catch (final Exception e)
								{
									LOG.error("Exception occurred whil paying from QC" + e.getMessage());
								}

								if (mplPaymentWebFacade.updateOrder(orderToBeUpdated))
								{
									updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
									updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());
								}
								else
								{
									updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
									updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());
								}
							}
							else if (MarketplacewebservicesConstants.JUSPAY_DECLINED.equalsIgnoreCase(statusResponse))
							{
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9322);
							}
							else if (MarketplacewebservicesConstants.AUTHORIZATION_FAILED.equalsIgnoreCase(statusResponse)
									|| MarketplacewebservicesConstants.AUTHENTICATION_FAILED.equalsIgnoreCase(statusResponse)
									|| MarketplacewebservicesConstants.PENDING_VBV.equalsIgnoreCase(statusResponse))
							{
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9323);
							}
							else
							{
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9324);
							}
						}
						//Redirection when transaction is failed
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9324);

						}
					}
					else
					{
						if (mplPaymentWebFacade.updateOrder(orderToBeUpdated))
						{
							updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
							updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());
						}
						else
						{
							updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
							updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());
						}

					}
				}
				else
				{
					LOG.error("For GUID:- " + cartGuid + " order already been processed");
					updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
					updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());

				}
			}
			else
			{
				LOG.error("For GUID:- " + cartGuid + " order already been processed");
				updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				updateTransactionDetail.setOrderId("");
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDetail.setError(ex.getErrorMessage());
			}
			if (null != ex.getErrorCode())
			{
				updateTransactionDetail.setErrorCode(ex.getErrorCode());
			}
			updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDetail.setError(ex.getErrorMessage());
			}
			if (null != ex.getErrorCode())
			{
				updateTransactionDetail.setErrorCode(ex.getErrorCode());
			}
			updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ex)
		{
			ExceptionUtil.getCustomizedExceptionTrace(ex);
			// Error message for All Exceptions
			if (null != ex.getMessage())
			{
				updateTransactionDetail.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				updateTransactionDetail.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return updateTransactionDetail;
	}




	public boolean payAmountThroughWallet(final OrderModel order)
	{
		boolean qcFlag = true;
		LOG.info("paying amount from EGV Wallet");
		//final OrderData orderData;
		//final OrderModel orderToBeUpdated = getMplPaymentFacade().getOrderByGuid(cart.getGuid());
		QCRedeeptionResponse qcResponse = new QCRedeeptionResponse();
		try
		{
			final String qcUniqueCode = mplPaymentFacade.generateQCCode();
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

			final double WalletAmt = order.getPayableWalletAmount().doubleValue();
			final double totalAmt = order.getTotalPrice().doubleValue();
			final double juspayAmount = totalAmt - WalletAmt;

			qcResponse = mplPaymentFacade.createQCOrderRequest(order.getGuid(), order, currentCustomer.getCustomerWalletDetail()
					.getWalletId(), MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH, qcUniqueCode,
					MarketplacewebservicesConstants.CHANNEL_MOBILE, WalletAmt, juspayAmount);
			if (null != qcResponse && null != qcResponse.getResponseCode() && qcResponse.getResponseCode().intValue() == 0)
			{
				qcFlag = true;
			}
			else
			{
				qcFlag = false;
				order.setStatus(OrderStatus.RMS_VERIFICATION_FAILED);
				modelService.save(order);
			}

		}
		catch (final Exception ex)
		{
			qcFlag = false;
			order.setStatus(OrderStatus.RMS_VERIFICATION_FAILED);
			modelService.save(order);
		}
		return qcFlag;
	}

	// Buying Of EGV  Changes START
	private OrderData getEGVOrderStatus(final String guid, final String paymentMode, final String juspayOrderID)
			throws InvalidCartException, CalculationException
	{
		OrderData orderData = new OrderData();
		final OrderModel orderToBeUpdated = getMplPaymentFacade().getOrderByGuid(guid);
		String orderStatusResponse = null;
		final Map<String, Double> paymentInfo = new HashMap<String, Double>();
		paymentInfo.put(paymentMode, Double.valueOf(orderToBeUpdated.getTotalPriceWithConv().doubleValue()));
		orderStatusResponse = getMplPaymentFacade().getOrderStatusFromJuspay(guid, paymentInfo, orderToBeUpdated, juspayOrderID);
		//Redirection when transaction is successful i.e. CHARGED
		if (null != orderStatusResponse)
		{
			if (MarketplacewebservicesConstants.CHARGED.equalsIgnoreCase(orderStatusResponse))
			{
				orderData = updateOrder(orderToBeUpdated);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9322);
			}
		}
		else
		{
			return updateOrder(orderToBeUpdated);
		}
		return orderData;
	}



	/**
	 * This method updates already created order as per new Payment Soln - Order before Payment TPR-629
	 *
	 * @param orderToBeUpdated
	 * @return String
	 * @throws InvalidCartException
	 * @throws CalculationException
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	private OrderData updateOrder(final OrderModel orderToBeUpdated) throws InvalidCartException, CalculationException,
			EtailNonBusinessExceptions
	{
		LOG.debug("========================Inside Update Order============================");
		OrderData orderData = null;
		try
		{
			if (null != orderToBeUpdated && null != orderToBeUpdated.getPaymentInfo()
					&& CollectionUtils.isEmpty(orderToBeUpdated.getChildOrders()))
			{
				getMplCheckoutFacade().beforeSubmitOrder(orderToBeUpdated);
				getMplCheckoutFacade().submitOrder(orderToBeUpdated);

				//order confirmation email and sms
				notificationFacade.sendOrderConfirmationNotification(orderToBeUpdated);

				orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderToBeUpdated);

			}
			else if (null != orderToBeUpdated && null != orderToBeUpdated.getPaymentInfo()
					&& CollectionUtils.isNotEmpty(orderToBeUpdated.getChildOrders()))
			{
				orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderToBeUpdated);
			}
			else if (null != orderToBeUpdated && null == orderToBeUpdated.getPaymentInfo()
					&& OrderStatus.PAYMENT_TIMEOUT.equals(orderToBeUpdated.getStatus()))
			{
				LOG.error("Issue with update order...redirecting to payment page only");
			}
			else
			{
				LOG.error("Issue with update order...redirecting to payment page only");
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return orderData;
	}


	// Buying Of EGV  Changes END

	/**
	 * @desc This method fetches delete the saved cards --TPR-629
	 *
	 * @return MplSavedCardDTO
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = MarketplacewebservicesConstants.GETPAYMENTMODE, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public PaymentServiceWsData getPaymentModes(@RequestParam final String cartGuid) //629
	{
		PaymentServiceWsData paymentModesData = new PaymentServiceWsData();
		CartModel cart = null;
		OrderModel orderModel = null;
		boolean isEgvOrder = false;
		//CAR-111
		//final CartData cartData = null;
		//OrderData orderData = null;
		try
		{
			if (StringUtils.isNotEmpty(cartGuid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}
			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				if (null != cart && null != cart.getIsEGVCart() && cart.getIsEGVCart().booleanValue())
				{
					isEgvOrder = true;
				}
				//CAR-111
				//cartData = getMplExtendedCartConverter().convert(cart);
				final Map<String, Boolean> paymentMode = getMplPaymentFacade().getPaymentModes(
						MarketplacewebservicesConstants.MPLSTORE, cart);
				paymentModesData = getMplPaymentWebFacade().potentialPromotionOnPaymentMode(cart);
				paymentModesData.setPaymentModes(paymentMode);
				if (!isEgvOrder)
				{
					paymentModesData.setPaymentOffers(mplCouponFacade.getAllOffersForMobile());
				}
				paymentModesData.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
				cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
				cart.setPayableNonWalletAmount(Double.valueOf(cart.getTotalPrice().doubleValue()));
				cart.setPayableWalletAmount(Double.valueOf(0.0D));
				modelService.save(cart);
				modelService.refresh(cart);
			}
			else
			{
				if (null != orderModel.getIsEGVCart() && orderModel.getIsEGVCart().booleanValue())
				{
					isEgvOrder = true;
				}
				//CAR-111
				//orderData = mplCheckoutFacade.getOrderDetailsForCode(orderModel);
				//Getting Payment modes
				final Map<String, Boolean> paymentMode = getMplPaymentFacade().getPaymentModes(
						MarketplacewebservicesConstants.MPLSTORE, orderModel);
				paymentModesData = getMplPaymentWebFacade().potentialPromotionOnPaymentMode(orderModel);
				paymentModesData.setPaymentModes(paymentMode);
				if (!isEgvOrder)
				{
					paymentModesData.setPaymentOffers(mplCouponFacade.getAllOffersForMobile());
				}
				paymentModesData.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
				orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
				orderModel.setPayableNonWalletAmount(Double.valueOf(orderModel.getTotalPrice().doubleValue()));
				orderModel.setPayableWalletAmount(Double.valueOf(0.0D));
				modelService.save(orderModel);
				modelService.refresh(orderModel);
			}
			final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
			boolean isWalletOtpVerified = false;
			if (null != customer && null != customer.getIsWalletActivated() && customer.getIsWalletActivated().booleanValue())
			{
				paymentModesData.setIsWalletCreated(true);
			}
			if (null != customer.getIsqcOtpVerify() && customer.getIsqcOtpVerify().booleanValue())
			{
				isWalletOtpVerified = true;
				paymentModesData.setIsWalletOtpVerified(true);
			}
			if (!isWalletOtpVerified)
			{
				final WalletCreateData walletCreateData = mplWalletFacade.getWalletCreateData(customer);
				if (null != walletCreateData)
				{
					if (null != walletCreateData.getQcVerifyFirstName()
							&& StringUtils.isNotBlank(walletCreateData.getQcVerifyFirstName()))
					{
						paymentModesData.setFirstName(walletCreateData.getQcVerifyFirstName());
					}
					if (null != walletCreateData.getQcVerifyLastName()
							&& StringUtils.isNotBlank(walletCreateData.getQcVerifyLastName()))
					{
						paymentModesData.setLastName(walletCreateData.getQcVerifyLastName());
					}
					if (null != walletCreateData.getQcVerifyMobileNo()
							&& StringUtils.isNotBlank(walletCreateData.getQcVerifyMobileNo()))
					{
						paymentModesData.setMobileNumber(walletCreateData.getQcVerifyMobileNo());
					}
				}
			}
			final String juspayMerchantKey = !getConfigurationService().getConfiguration()
					.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY).isEmpty() ? getConfigurationService()
					.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)
					: MarketplacecommerceservicesConstants.JUSPAYMERCHANTKEYNOTFOUND;

			final String juspayMerchantId = !getConfigurationService().getConfiguration()
					.getString(MarketplacecommerceservicesConstants.MARCHANTID).isEmpty() ? getConfigurationService()
					.getConfiguration().getString(MarketplacecommerceservicesConstants.MARCHANTID)
					: MarketplacecommerceservicesConstants.JUSPAYMERCHANTIDNOTFOUND;
			paymentModesData.setMerchantID(juspayMerchantId);
			paymentModesData.setMerchantKey(juspayMerchantKey);

			/* Added for cliq Cash Functionality start */
			try
			{
				LOG.debug("Getting saved Card Details");
				final MplSavedCardDTO savedCards = new MplSavedCardDTO();

				Map<Date, SavedCardData> savedCardsMap = new TreeMap<Date, SavedCardData>();
				Map<Date, SavedCardData> savedDebitCards = new TreeMap<Date, SavedCardData>();
				savedCardsMap = getMplPaymentFacade().listStoredCreditCards(customer);
				LOG.debug("savedCardsMap " + savedCardsMap);
				savedDebitCards = getMplPaymentFacade().listStoredDebitCards(customer);
				// Add everything in savedCardsMap from savedDebitCards
				savedCardsMap.putAll(savedDebitCards);
				//Adding details into DTO
				savedCards.setSavedCardDetailsMap(savedCardsMap);

				paymentModesData.setSavedCardResponse(savedCards);
				paymentModesData.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			catch (final Exception e)
			{
				LOG.error("Exception occurred while getting the saved credit card details " + e.getMessage());
			}

			if (!isEgvOrder)
			{
				boolean EnabledSplitPaymentModeForMobile = false;
				if (null != configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.ENABLED_SPILT_PAYMENT_FORMOBILE))
				{
					final String splitModeEnabledStringValue = configurationService.getConfiguration()
							.getString(MarketplacecommerceservicesConstants.ENABLED_SPILT_PAYMENT_FORMOBILE).trim();
					if (null != splitModeEnabledStringValue)
					{
						EnabledSplitPaymentModeForMobile = Boolean.valueOf(splitModeEnabledStringValue).booleanValue();
					}
				}

				if (EnabledSplitPaymentModeForMobile)
				{

					LOG.debug("Split Payment Mode Enabled For Mobile");
					try
					{
						LOG.debug("Getting saved Clish Cash Details");
						final CliqCashWsDto cliqCash = new CliqCashWsDto();
						final TotalCliqCashBalanceWsDto totalCliqCashBalanceWsDto = new TotalCliqCashBalanceWsDto();
						if (null != customer.getIsWalletActivated() && customer.getIsWalletActivated().booleanValue())
						{

							final CustomerWalletDetailResponse responce = mplWalletFacade.getCustomerWallet(customer
									.getCustomerWalletDetail().getWalletId());
							if (null != responce && responce.getResponseCode() == Integer.valueOf(0) && null != responce.getWallet())
							{

								if (null != responce.getApiWebProperties() && null != responce.getApiWebProperties().getDateAtClient())
								{
									cliqCash.setBalanceClearedAsOf(responce.getApiWebProperties().getDateAtClient());
								}

								final BigDecimal walletAmount = new BigDecimal(responce.getWallet().getBalance().doubleValue());
								if (null != cart)
								{
									cart.setTotalWalletAmount(responce.getWallet().getBalance());
									modelService.save(cart);
								}
								else if (null != orderModel)
								{
									orderModel.setTotalWalletAmount(responce.getWallet().getBalance());
									modelService.save(orderModel);
								}
								final PriceData priceData = PriceDataFactory.create(PriceDataType.BUY, walletAmount,
										MarketplacecommerceservicesConstants.INR);

								if (null != priceData)
								{
									totalCliqCashBalanceWsDto.setCurrencyIso(priceData.getCurrencyIso());
									totalCliqCashBalanceWsDto.setDoubleValue(priceData.getDoubleValue());
									totalCliqCashBalanceWsDto.setFormattedValue(priceData.getFormattedValue());
									totalCliqCashBalanceWsDto.setPriceType(priceData.getPriceType());
									totalCliqCashBalanceWsDto.setFormattedValueNoDecimal(priceData.getFormattedValueNoDecimal());
									totalCliqCashBalanceWsDto.setValue(priceData.getValue());
									paymentModesData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
								}
								cliqCash.setTotalCliqCashBalance(totalCliqCashBalanceWsDto);
								paymentModesData.setCliqCash(cliqCash);
							}
						}
						else
						{
							//							paymentModesData.setFirstName(customer.getQcVerifyFirstName());
							//							paymentModesData.setLastName(customer.getQcVerifyLastName());
							//							paymentModesData.setMobileNumber(customer.getMobileNumber());
							final BigDecimal walletAmount = new BigDecimal(0);
							final PriceData priceData = PriceDataFactory.create(PriceDataType.BUY, walletAmount,
									MarketplacecommerceservicesConstants.INR);

							if (null != priceData)
							{
								totalCliqCashBalanceWsDto.setCurrencyIso(priceData.getCurrencyIso());
								totalCliqCashBalanceWsDto.setDoubleValue(priceData.getDoubleValue());
								totalCliqCashBalanceWsDto.setFormattedValue(priceData.getFormattedValue());
								totalCliqCashBalanceWsDto.setPriceType(priceData.getPriceType());
								totalCliqCashBalanceWsDto.setFormattedValueNoDecimal(priceData.getFormattedValueNoDecimal());
								totalCliqCashBalanceWsDto.setValue(priceData.getValue());
								paymentModesData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							}
							cliqCash.setTotalCliqCashBalance(totalCliqCashBalanceWsDto);
							paymentModesData.setCliqCash(cliqCash);
						}
					}
					catch (final Exception e)
					{
						LOG.debug("Exception occurred while getting customer QC wallet Amount" + e.getMessage());
					}
				}
				else
				{
					LOG.debug("Split Payment Mode Not Enabled For Mobile");
				}

			}
			/* Added for cliq Cash Functionality end */
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				paymentModesData.setError(ex.getErrorMessage());
				paymentModesData.setErrorCode(ex.getErrorCode());
			}
			paymentModesData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				paymentModesData.setError(ex.getErrorMessage());
				paymentModesData.setErrorCode(ex.getErrorCode());
			}
			paymentModesData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacewebservicesConstants.GETPAYMENTMODE, ex);
			// Error message for All Exceptions
			if (null != ex.getMessage())
			{
				paymentModesData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				paymentModesData.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			paymentModesData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return paymentModesData;
	}

	//Commenting due to SONAR Fix
	//	/**
	//	 * @Description : Populating Promotion Details
	//	 * @param promoData
	//	 * @param data
	//	 * @return dtoData
	//	 */
	//	private MplPromotionDTO getPromoDetails(final MplPromotionData promoData, final MplPromoPriceData data)
	//	{
	//		final MplPromotionDTO dtoData = new MplPromotionDTO();
	//		final Map<String, String> promoIdMsg = new HashMap<String, String>();
	//
	//		if (null != promoData)
	//		{
	//			if (promoData.getPromoTypeIdentifier().equalsIgnoreCase(MarketplacewebservicesConstants.POTENTIALPROMOTION)
	//					&& null != promoData.getPotentialPromotion() && null != promoData.getPotentialPromotion().getPromoMessage())
	//			{
	//				promoIdMsg.put(promoData.getPromoTypeIdentifier(), promoData.getPotentialPromotion().getPromoMessage());
	//				dtoData.setPromoIdMessage(promoIdMsg);
	//			}
	//			else
	//			{
	//				if (data.getTotalDiscount().getValue().compareTo(BigDecimal.ZERO) > 0)
	//				{
	//					if (null != promoData.getFiredPromotion() && null != promoData.getFiredPromotion().getPromoMessage())
	//					{
	//						promoIdMsg.put(promoData.getPromoTypeIdentifier(), promoData.getFiredPromotion().getPromoMessage());
	//						dtoData.setPromoIdMessage(promoIdMsg);
	//					}
	//
	//					if (null != data.getCurrency() && null != data.getCurrency())
	//					{
	//						dtoData.setDiscountPriceCurrency(data.getCurrency());
	//					}
	//
	//					if (null != promoData.getIsPercentage())
	//					{
	//						dtoData.setIsPercentage(promoData.getIsPercentage());
	//					}
	//					if (null != promoData.getPercentagePromotion())
	//					{
	//						dtoData.setPercentagePromotion(promoData.getPercentagePromotion());
	//					}
	//				}
	//			}
	//		}
	//
	//		return dtoData;
	//	}

	/**
	 * @return the mplCustomAddressFacade
	 */
	public MplCustomAddressFacade getMplCustomAddressFacade()
	{
		return mplCustomAddressFacade;
	}

	/**
	 * @param mplCustomAddressFacade
	 *           the mplCustomAddressFacade to set
	 */
	public void setMplCustomAddressFacade(final MplCustomAddressFacade mplCustomAddressFacade)
	{
		this.mplCustomAddressFacade = mplCustomAddressFacade;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the mplPaymentFacade
	 */
	public MplPaymentFacade getMplPaymentFacade()
	{
		return mplPaymentFacade;
	}

	/**
	 * @param mplPaymentFacade
	 *           the mplPaymentFacade to set
	 */
	public void setMplPaymentFacade(final MplPaymentFacade mplPaymentFacade)
	{
		this.mplPaymentFacade = mplPaymentFacade;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the binService
	 */
	public BinService getBinService()
	{
		return binService;
	}

	/**
	 * @param binService
	 *           the binService to set
	 */
	public void setBinService(final BinService binService)
	{
		this.binService = binService;
	}

	/**
	 * @return the mplPaymentService
	 */
	public MplPaymentService getMplPaymentService()
	{
		return mplPaymentService;
	}

	/**
	 * @param mplPaymentService
	 *           the mplPaymentService to set
	 */
	public void setMplPaymentService(final MplPaymentService mplPaymentService)
	{
		this.mplPaymentService = mplPaymentService;
	}

	/**
	 * @return the discountUtility
	 */
	public DiscountUtility getDiscountUtility()
	{
		return discountUtility;
	}

	/**
	 * @param discountUtility
	 *           the discountUtility to set
	 */
	public void setDiscountUtility(final DiscountUtility discountUtility)
	{
		this.discountUtility = discountUtility;
	}

	/**
	 * @return the mplPaymentWebFacade
	 */
	public MplPaymentWebFacade getMplPaymentWebFacade()
	{
		return mplPaymentWebFacade;
	}

	/**
	 * @param mplPaymentWebFacade
	 *           the mplPaymentWebFacade to set
	 */
	public void setMplPaymentWebFacade(final MplPaymentWebFacade mplPaymentWebFacade)
	{
		this.mplPaymentWebFacade = mplPaymentWebFacade;
	}

	/**
	 * @return the mplCheckoutFacade
	 */
	public MplCheckoutFacade getMplCheckoutFacade()
	{
		return mplCheckoutFacade;
	}

	/**
	 * @param mplCheckoutFacade
	 *           the mplCheckoutFacade to set
	 */
	public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade)
	{
		this.mplCheckoutFacade = mplCheckoutFacade;
	}

	/**
	 * @return the extUserService
	 */
	public ExtendedUserService getExtUserService()
	{
		return extUserService;
	}

	/**
	 * @param extUserService
	 *           the extUserService to set
	 */
	public void setExtUserService(final ExtendedUserService extUserService)
	{
		this.extUserService = extUserService;
	}

	/**
	 * @return the mplExtendedCartConverter
	 */
	public Converter<CartModel, CartData> getMplExtendedCartConverter()
	{
		return mplExtendedCartConverter;
	}

	/**
	 * @param mplExtendedCartConverter
	 *           the mplExtendedCartConverter to set
	 */
	public void setMplExtendedCartConverter(final Converter<CartModel, CartData> mplExtendedCartConverter)
	{
		this.mplExtendedCartConverter = mplExtendedCartConverter;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	private PriceData createPriceObj(final String cost)
	{
		// YTODO Auto-generated method stub
		final PriceData priceData = new PriceData();
		priceData.setFormattedValue(cost);
		priceData.setDoubleValue(Double.valueOf(cost));

		final CurrencyModel currency = commonI18NService.getCurrency(INR);
		priceData.setCurrencyIso(currency.getIsocode());
		priceData.setCurrencySymbol(currency.getSymbol());

		return priceData;
	}
}
