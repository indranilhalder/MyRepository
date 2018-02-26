/**
 * 
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.CustomerWalletDetailModel;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.cms.data.WalletCreateData;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.model.MplCartOfferVoucherModel;
import com.tisl.mpl.pojo.request.Customer;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.service.MplEgvWalletService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.AmountOptionsWSDTO;
import com.tisl.mpl.wsdto.ApplyCartCouponsDTO;
import com.tisl.mpl.wsdto.ApplyCliqCashWsDto;
import com.tisl.mpl.wsdto.ApplyCouponsDTO;
import com.tisl.mpl.wsdto.EgvCheckMobileNumberWsDto;
import com.tisl.mpl.wsdto.EgvProductInfoWSDTO;
import com.tisl.mpl.wsdto.EgvWalletCreateRequestWsDTO;
import com.tisl.mpl.wsdto.EgvWalletCreateResponceWsDTO;
import com.tisl.mpl.wsdto.TotalCliqCashBalanceWsDto;
import com.tisl.mpl.wsdto.UserCliqCashWsDto;

/**
 * @author IT
 *
 */
public class MplEgvWalletServiceImpl implements MplEgvWalletService
{
	
	@Autowired
	private MplWalletFacade mplWalletFacade;
   @Autowired
   private ModelService modelService;
   @Autowired
   private UserService userService;
   @Autowired
   private PriceDataFactory priceDataFactory;
   
   @Autowired
   private CommerceCartService commerceCartService;
   
   @Autowired
   private MplCouponFacade mplCouponFacade;
   
   @Autowired
   private RegisterCustomerFacade registerCustomerFacade;
   
   @Autowired
   private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(MplEgvWalletServiceImpl.class);

	/* (non-Javadoc)
	 * @see com.tisl.mpl.service.MplEgvWalletService#createWalletContainer(de.hybris.platform.core.model.user.CustomerModel)
	 */
	@Override
	public QCCustomerRegisterResponse createWalletContainer(CustomerModel currentCustomer)
	{
		
			LOG.debug("Customer Is not Regitered with QC .. Registering with email " + currentCustomer.getOriginalUid());
			final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
			final Customer custInfo = new Customer();
			custInfo.setEmail(currentCustomer.getOriginalUid());
			custInfo.setEmployeeID(currentCustomer.getUid());
			custInfo.setCorporateName(configurationService.getConfiguration().getString("CorporateName"));

			if (null != currentCustomer.getQcVerifyFirstName())
			{
				custInfo.setFirstname(currentCustomer.getQcVerifyFirstName());
			}
			if (null != currentCustomer.getQcVerifyLastName())
			{
				custInfo.setLastName(currentCustomer.getQcVerifyLastName());
			}

			if (null != currentCustomer.getQcVerifyMobileNo())
			{
				custInfo.setPhoneNumber(currentCustomer.getQcVerifyMobileNo());
			}
			custInfo.setCorporateName("Tata Unistore Ltd");
			customerRegisterReq.setExternalwalletid(currentCustomer.getUid());
			customerRegisterReq.setCustomer(custInfo);
			customerRegisterReq.setNotes("Activating Customer " + currentCustomer.getOriginalUid());
			final QCCustomerRegisterResponse customerRegisterResponse = mplWalletFacade
					.createWalletContainer(customerRegisterReq);
			if(null != customerRegisterResponse && null != customerRegisterResponse.getResponseCode() && customerRegisterResponse.getResponseCode().intValue() == 0) {
				final CustomerWalletDetailModel custWalletDetail = modelService.create(CustomerWalletDetailModel.class);
				custWalletDetail.setWalletId(customerRegisterResponse.getWallet().getWalletNumber());
				custWalletDetail.setWalletState(customerRegisterResponse.getWallet().getStatus());
				custWalletDetail.setCustomer(currentCustomer);
				custWalletDetail.setServiceProvider("Tata Unistore Ltd");
				modelService.save(custWalletDetail);
				currentCustomer.setCustomerWalletDetail(custWalletDetail);
				currentCustomer.setIsWalletActivated(Boolean.TRUE);
				modelService.save(currentCustomer);
				return customerRegisterResponse;
			}else if(null != customerRegisterResponse && null != customerRegisterResponse.getResponseMessage()){
				throw new EtailBusinessExceptions(customerRegisterResponse.getResponseMessage());
			}
		return null;
		
	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.service.MplEgvWalletService#verifyOtpAndCreateWallet(de.hybris.platform.core.model.user.CustomerModel, java.lang.String)
	 */
	@Override
	public EgvWalletCreateResponceWsDTO verifyOtpAndCreateWallet(CustomerModel currentCustomer,String otp,String firstName,String lastName,String mobileNumber)
	{
		EgvWalletCreateResponceWsDTO responce = new EgvWalletCreateResponceWsDTO();
			if (null != otp && !otp.isEmpty())
			{
				boolean customerWalletCreated = false;

				if(null !=currentCustomer.getIsWalletActivated() && currentCustomer.getIsWalletActivated().booleanValue())
				{
					customerWalletCreated =true;
				}
				OTPResponseData response = mplWalletFacade.validateOTP(currentCustomer.getUid(), otp);
				
				if (response.getOTPValid().booleanValue())
				{
				  if(customerWalletCreated) {

					  CustomerWalletDetailResponse customerWalletDetailData = mplWalletFacade.getCustomerWallet(currentCustomer
								.getCustomerWalletDetail().getWalletId());
						UserCliqCashWsDto userCliqCashData = getCustomerWalletAmount(customerWalletDetailData);
						registerCustomerFacade.registerWalletMobileNumber(firstName,lastName,mobileNumber);//TPR-6272 parameter platformNumber passed

						responce.setIsWalletCreated(true);
						if(null != currentCustomer.getIsqcOtpVerify() && currentCustomer.getIsqcOtpVerify().booleanValue() )
						{
							responce.setIsWalletOtpVerified(true);
						}
						responce.setBalanceClearedAsOf(userCliqCashData.getBalanceClearedAsOf());
						responce.setTotalCliqCashBalance(userCliqCashData.getTotalCliqCashBalance());
						responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						currentCustomer.setIsqcOtpVerify(Boolean.TRUE);
						modelService.save(currentCustomer);
						responce.setIsWalletOtpVerified(true);
				  }else {
						final QCCustomerRegisterResponse customerRegisterResponse = createWalletContainer(currentCustomer);
						if (null != customerRegisterResponse && null != customerRegisterResponse.getResponseCode()
								&& customerRegisterResponse.getResponseCode().intValue() == 0)
						{
							registerCustomerFacade.registerWalletMobileNumber(firstName,lastName,mobileNumber);//TPR-6272 parameter platformNumber passed

							CustomerWalletDetailResponse customerWalletDetailData = mplWalletFacade.getCustomerWallet(currentCustomer
									.getCustomerWalletDetail().getWalletId());
							UserCliqCashWsDto userCliqCashData = getCustomerWalletAmount(customerWalletDetailData);
							responce.setIsWalletCreated(true);
							responce.setIsWalletOtpVerified(true);

							responce.setBalanceClearedAsOf(userCliqCashData.getBalanceClearedAsOf());
							responce.setTotalCliqCashBalance(userCliqCashData.getTotalCliqCashBalance());
							responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							currentCustomer.setIsqcOtpVerify(Boolean.TRUE);
							modelService.save(currentCustomer);
						} else if ( null != customerRegisterResponse && null != customerRegisterResponse.getResponseCode() ) {
							throw new EtailBusinessExceptions(customerRegisterResponse.getResponseCode().toString());
						}
						else 
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9047);
						}
				  }
				
				}
				else
				{
					if(null != response.getInvalidErrorMessage()) {
						
						if(response.getInvalidErrorMessage().equalsIgnoreCase(MarketplacecommerceservicesConstants.OTPEXPIRY)) {
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9217);
						}else if(response.getInvalidErrorMessage().equalsIgnoreCase(MarketplacecommerceservicesConstants.INVALID_WALLET_OTP)) {
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5014);
						}
					}
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9047);
				}
			}else {
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9047);

			}
		return responce;
	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.service.MplEgvWalletService#getCustomerWallet(de.hybris.platform.core.model.user.CustomerModel)
	 */
	@Override
	public UserCliqCashWsDto getCustomerWalletAmount(CustomerWalletDetailResponse customerWalletDetailData)
	{
		try
		{
			UserCliqCashWsDto userCliqCashWsDto = new UserCliqCashWsDto();
			TotalCliqCashBalanceWsDto totalCliqCashBalance = new TotalCliqCashBalanceWsDto();
			BigDecimal walletAmount;
			if (null != customerWalletDetailData && null != customerWalletDetailData.getWallet()
					&& null != customerWalletDetailData.getWallet().getBalance()
					&& customerWalletDetailData.getWallet().getBalance().doubleValue() > 0.0D)
			{
				walletAmount = new BigDecimal(customerWalletDetailData.getWallet().getBalance().doubleValue());
			}
			else
			{
				walletAmount = new BigDecimal(0.0D);
			}
			PriceData priceData = null;
			if (walletAmount.doubleValue() > 0.0D)
			{
				priceData = priceDataFactory.create(PriceDataType.BUY, walletAmount, MarketplacecommerceservicesConstants.INR);
			}
			else
			{
				priceData = priceDataFactory.create(PriceDataType.BUY, walletAmount, MarketplacecommerceservicesConstants.INR);
			}

			if (null != priceData)
			{
				totalCliqCashBalance.setCurrencyIso(priceData.getCurrencyIso());
				totalCliqCashBalance.setDoubleValue(priceData.getDoubleValue());
				totalCliqCashBalance.setFormattedValue(priceData.getFormattedValue());
				totalCliqCashBalance.setPriceType(priceData.getPriceType());
				totalCliqCashBalance.setFormattedValueNoDecimal(priceData.getFormattedValueNoDecimal());
				totalCliqCashBalance.setValue(priceData.getValue());
			}
			userCliqCashWsDto.setBalanceClearedAsOf(customerWalletDetailData.getApiWebProperties().getDateAtClient());
			userCliqCashWsDto.setTotalCliqCashBalance(totalCliqCashBalance);
			return userCliqCashWsDto;
		}catch(Exception e){
			LOG.error("Exception occurred while getting userCliqCash Details"+e.getMessage());
		}
		return null;
		
	}

	
	/* (non-Javadoc)
	 * @see com.tisl.mpl.service.MplCartWebService#getUserCliqCashDetails(java.lang.String)
	 */
	@Override
	public UserCliqCashWsDto getUserCliqCashDetails(CustomerModel currentCustomer)
	{
		CustomerWalletDetailResponse customerWalletDetailData = new CustomerWalletDetailResponse();
		UserCliqCashWsDto responce = new UserCliqCashWsDto();
		boolean isWalletOtpVerified = false;
		boolean isWalletCreated = false;

		if (null != currentCustomer)
		{
			if (null != currentCustomer.getIsWalletActivated() && currentCustomer.getIsWalletActivated().booleanValue())
			{
				isWalletCreated = true;
				 responce.setIsWalletCreated(true);
			}
			if (null != currentCustomer.getIsqcOtpVerify() && currentCustomer.getIsqcOtpVerify().booleanValue())
			{
				isWalletOtpVerified = true;
		   	responce.setIsWalletOtpVerified(true);
			}
			 if (isWalletCreated)
				{
					customerWalletDetailData = mplWalletFacade.getCustomerWallet(currentCustomer.getCustomerWalletDetail()
							.getWalletId());

					if (null != customerWalletDetailData && null != customerWalletDetailData.getResponseCode()
							&& customerWalletDetailData.getResponseCode().intValue() == 0)
					{
						responce = getCustomerWalletAmount(customerWalletDetailData);
						responce.setIsWalletCreated(true);
						responce.setTotalCliqCashBalance(responce.getTotalCliqCashBalance());
						responce.setBalanceClearedAsOf(customerWalletDetailData.getApiWebProperties().getDateAtClient());
						responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					}
					else if (null != customerWalletDetailData && null != customerWalletDetailData.getResponseCode())
					{
						throw new EtailBusinessExceptions(customerWalletDetailData.getResponseCode().toString());
					}
				}
			 
			   if(isWalletOtpVerified) {
			   	responce.setIsWalletOtpVerified(true);
			   }else {
					WalletCreateData walletCreateData = mplWalletFacade.getWalletCreateData(currentCustomer);
					responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					if(null != walletCreateData) {
						if(null != walletCreateData.getQcVerifyFirstName() && StringUtils.isNotBlank(walletCreateData.getQcVerifyFirstName())){
							responce.setFirstName(walletCreateData.getQcVerifyFirstName());
						}
						if(null != walletCreateData.getQcVerifyLastName() && StringUtils.isNotBlank(walletCreateData.getQcVerifyLastName())){
							responce.setLastName(walletCreateData.getQcVerifyLastName());
						}
						if(null != walletCreateData.getQcVerifyMobileNo() && StringUtils.isNotBlank(walletCreateData.getQcVerifyMobileNo())){
							responce.setMobileNumber(walletCreateData.getQcVerifyMobileNo());
						}
					}
				
			   }
			}
			return responce;
	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.service.MplCartWebService#applyCLiqCash(de.hybris.platform.core.model.order.AbstractOrderModel)
	 */
	@Override
	public ApplyCliqCashWsDto applyCLiqCash(AbstractOrderModel cart,Double walletAmount)
	{
		ApplyCliqCashWsDto applyCliqCashWsDto = new ApplyCliqCashWsDto();
		boolean isCartVoucherPresent = false;
		String cartCouponCode = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			if (null == walletAmount)
			{
				walletAmount = cart.getTotalWalletAmount();
			}
			final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(cart.getDiscounts());

			isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();
			if (isCartVoucherPresent)
			{
				cart = mplCouponFacade.removeLastCartCoupon(cart);
			}

			if (null != walletAmount && walletAmount.doubleValue() > 0.0D)
			{
				LOG.debug("Bucket Balance =" + walletAmount);
				Double cartTotalAmount = cart.getTotalPrice();

				// If  Customer is Having Enough money in Cliq Cash Then  pay Using Cliq Cash   
				// Otherwise He needs to Pay the Remaining Amount using Other Payment Methods( Net Banking ,Debit Cart ... ) 

				//	 if Customer Is having Enough Money In Cliq Cash , Then Saving SplitModeInfo as CLIQ_CASH 
				if (null != cartTotalAmount && walletAmount.doubleValue() >= cartTotalAmount.doubleValue())
				{
					cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH);
					cart.setPayableWalletAmount(cartTotalAmount);
					cart.setTotalWalletAmount(walletAmount);
					cart.setPayableNonWalletAmount(Double.valueOf(0.0D));
					modelService.save(cart);
					modelService.refresh(cart);
					applyCliqCashWsDto.setDiscount(cart.getTotalDiscounts());
					applyCliqCashWsDto.setIsRemainingAmount(false);
					Double amount = Double.valueOf(0.0D);
					BigDecimal bigDecimal = new BigDecimal(amount.doubleValue());
					final String decimalFormat = "0.00";
					final DecimalFormat df = new DecimalFormat(decimalFormat);
					final String totalPayableAmount = df.format(bigDecimal);
					applyCliqCashWsDto.setPaybleAmount(totalPayableAmount);
					applyCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
					applyCliqCashWsDto = setTotalPrice(applyCliqCashWsDto, cart);
					applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);

				}

				//  else if Customer Is Not having Enough Money In Cliq Cash , Then Saving SplitModeInfo as SPLIT_MODE 
				else
				{
					double juspayTotalAmt = 0.0D;
					if (cartTotalAmount.doubleValue() > 0.0D && walletAmount.doubleValue() > 0.0D)
					{
						juspayTotalAmt = cartTotalAmount.doubleValue() - walletAmount.doubleValue();
					}
					else if (null != cart.getTotalPrice() && cart.getTotalPrice().doubleValue() > 0.0D)
					{
						juspayTotalAmt = cart.getTotalPrice().doubleValue();
					}

					cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT);
					cart.setPayableWalletAmount(walletAmount);
					cart.setTotalWalletAmount(walletAmount);
					cartTotalAmount = cart.getTotalPrice();
					double payableJuspayAmount = cartTotalAmount.doubleValue() - walletAmount.doubleValue();
					if(payableJuspayAmount >= 0.0D) {
						cart.setPayableNonWalletAmount(Double.valueOf(payableJuspayAmount));
					}else {
						cart.setPayableNonWalletAmount(cartTotalAmount);
					}
					modelService.save(cart);
					modelService.refresh(cart);
					//applyCliqCashWsDto.setPaybleAmount(Double.valueOf(juspayTotalAmt));

					// Applying Already Existing Bank Promotion Start 
					try
					{
						if (isCartVoucherPresent)
						{
							cartCouponCode = cartCouponObj.getSecond();
							cart.setCheckForBankVoucher("true");
							modelService.save(cart);
							modelService.refresh(cart);
							if (cart instanceof CartModel)
							{
								mplCouponFacade.applyCartVoucher(cartCouponCode, (CartModel) cart, null);
							}
							else
							{
								mplCouponFacade.applyCartVoucher(cartCouponCode, null, (OrderModel) cart);
							}
							cart.setCheckForBankVoucher("false");
						}
					}
					catch (Exception e)
					{
						LOG.error("Exception occurred while applying already selected bank Promotion " + e.getMessage(), e);
					}
					//  Applying Already Existing Bank Promotion End 

					juspayTotalAmt = 0.0D;
					cartTotalAmount = cart.getTotalPrice();
					if (cartTotalAmount.doubleValue() > 0.0D && walletAmount.doubleValue() > 0.0D)
					{
						juspayTotalAmt = cartTotalAmount.doubleValue() - walletAmount.doubleValue();
					}
					else if (null != cart.getTotalPrice() && cart.getTotalPrice().doubleValue() > 0.0D)
					{
						juspayTotalAmt = cart.getTotalPrice().doubleValue();
					}
					if (cartTotalAmount.doubleValue() >= walletAmount.doubleValue())
					{
						payableJuspayAmount = cartTotalAmount.doubleValue() - walletAmount.doubleValue();
						cart.setPayableNonWalletAmount(Double.valueOf(payableJuspayAmount));
					}
					Double amount = Double.valueOf(juspayTotalAmt);
					BigDecimal bigDecimal = new BigDecimal(amount.doubleValue());
					final String decimalFormat = "0.00";
					final DecimalFormat df = new DecimalFormat(decimalFormat);
					final String totalPayableAmount = df.format(bigDecimal);
					applyCliqCashWsDto.setPaybleAmount(totalPayableAmount);
					applyCliqCashWsDto.setIsRemainingAmount(true);
					applyCliqCashWsDto.setDiscount(cart.getTotalDiscounts());
					applyCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
					applyCliqCashWsDto = setTotalPrice(applyCliqCashWsDto, cart);
					applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
			}
			else
			{
				applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				applyCliqCashWsDto.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B5001));
				applyCliqCashWsDto.setErrorCode(MarketplacecommerceservicesConstants.B5001);
			}
		}
		catch (Exception e)
		{
			LOG.error("Exception occurred while applying cliqCash" + e.getMessage(), e);
		}

		return applyCliqCashWsDto;
	}

	
	@Override
	public Tuple2<Boolean, String> isCartVoucherPresent(final List<DiscountModel> discounts)
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

	/* (non-Javadoc)
	 * @see com.tisl.mpl.service.MplEgvWalletService#useCliqCash(de.hybris.platform.core.model.order.AbstractOrderModel)
	 */
	@Override
	public void useCliqCash(AbstractOrderModel order)
	{
		try
		{
			double cartTotal = order.getTotalPrice().doubleValue();
			double totalWalletAmount = order.getTotalWalletAmount().doubleValue();
			double payableWalletAmount = 0.0D;
			if (totalWalletAmount > 0.0D)
			{
				if (cartTotal > totalWalletAmount)
				{
					payableWalletAmount = totalWalletAmount;
					order.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT);
				}
				else if (cartTotal <= totalWalletAmount)
				{
					payableWalletAmount = cartTotal;
					order.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH);
				}else {
					order.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
				}
				double payableJuspayAmount = cartTotal - payableWalletAmount;
				order.setPayableNonWalletAmount(Double.valueOf(payableJuspayAmount));
				order.setPayableWalletAmount(Double.valueOf(payableWalletAmount));
				modelService.save(order);
			}
			LOG.debug("Cart Total Amount "+cartTotal);
			LOG.debug("Wallet Total Amount "+totalWalletAmount);
			LOG.debug("payableWallet  Amount "+payableWalletAmount);
		}
		catch (Exception e)
		{
			LOG.error("Exception occurred while using cliqCash " + e.getMessage(), e);
		}

	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.service.MplEgvWalletService#generateOtpForUpdateWallet(java.lang.String)
	 */
	@Override
	public boolean generateOtpForUpdateWallet(String mobileNumber,CustomerModel customer)
	{
		if (null != customer && null != customer.getIsWalletActivated() && customer.getIsWalletActivated().booleanValue())
		{
			if (null != customer.getQcVerifyMobileNo() && !customer.getQcVerifyMobileNo().trim().equalsIgnoreCase(mobileNumber.trim()))
			{
				if (registerCustomerFacade.checkUniquenessOfMobileForWallet(mobileNumber))
				{
					mplWalletFacade.generateOTP(customer, mobileNumber);
					return true;
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5010);
				}
			}else {
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5017);
			}
		}else {
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5018);

		}
	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.service.MplEgvWalletService#setTotalPrice(com.tisl.mpl.wsdto.ApplyCliqCashWsDto, de.hybris.platform.core.model.order.AbstractOrderModel)
	 */
	@Override
	public ApplyCliqCashWsDto setTotalPrice(ApplyCliqCashWsDto applyCliqCashWsDto, AbstractOrderModel cartModel)
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
				 productDiscount = (null != entry.getTotalProductLevelDisc() && entry.getTotalProductLevelDisc()
						.doubleValue() > 0) ? entry.getTotalProductLevelDisc().doubleValue() : 0;
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
		BigDecimal total = new BigDecimal(0.0D);
		final double remainingWalletAmount = cartModel.getTotalWalletAmount().doubleValue() - payableWalletAmount;
		if (null != cartModel.getSubtotal())
		{
			total = new BigDecimal(cartModel.getSubtotal().doubleValue());
			final PriceData subTotalPriceData = priceDataFactory.create(PriceDataType.BUY, total,
					MarketplacecommerceservicesConstants.INR);
			applyCliqCashWsDto.setSubTotalPrice(subTotalPriceData);

		}

		if (null != cartModel.getDeliveryCost())
		{
			total = new BigDecimal(cartModel.getDeliveryCost().doubleValue());
			final PriceData deliveryChargesPriceData = priceDataFactory.create(PriceDataType.BUY, total,
					MarketplacecommerceservicesConstants.INR);
			applyCliqCashWsDto.setDeliveryCharges(deliveryChargesPriceData);
		}
		total = new BigDecimal(otherDiscount);
		final PriceData otherDiscountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applyCliqCashWsDto.setOtherDiscount(otherDiscountPriceData);
      if(bankCouponDiscount > 0.0D) {
      	applyCliqCashWsDto.setIsBankPromotionApplied(true);
      }
		total = new BigDecimal(userCouponDiscount);
		final PriceData couponPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applyCliqCashWsDto.setCouponDiscount(total.toString());

		applyCliqCashWsDto.setAppliedCouponDiscount(couponPriceData);

		if (payableWalletAmount > 0.0D)
		{
			applyCliqCashWsDto.setCliqCashApplied(true);

		}

		total = new BigDecimal(payableWalletAmount);
		final PriceData payableWalletAmountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applyCliqCashWsDto.setCliqCashPaidAmount(payableWalletAmountPriceData);

		total = new BigDecimal(remainingWalletAmount);
		final PriceData remainingWalletAmountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applyCliqCashWsDto.setCliqCashBalance(remainingWalletAmountPriceData);

		if (null != cartModel.getTotalPrice())
		{
			total = new BigDecimal(cartModel.getTotalPrice().doubleValue() - payableWalletAmount);
			final PriceData cartTotalPriceData = priceDataFactory.create(PriceDataType.BUY, total,
					MarketplacecommerceservicesConstants.INR);
			applyCliqCashWsDto.setTotalPrice(cartTotalPriceData);
			
			if((cartModel.getTotalPrice().doubleValue() - payableWalletAmount) > 0.0D) 
			{
				applyCliqCashWsDto.setIsRemainingAmount(true);
			}
		}
		return applyCliqCashWsDto;
	}
	
	/* (non-Javadoc)
	 * @see com.tisl.mpl.service.MplEgvWalletService#checkWalletMobileNumber(com.tisl.mpl.wsdto.EgvWalletCreateRequestWsDTO, boolean)
	 */
	@Override
	public EgvCheckMobileNumberWsDto checkWalletMobileNumber(EgvWalletCreateRequestWsDTO request, boolean isUpdateProfile)
	{
		EgvCheckMobileNumberWsDto responce = new EgvCheckMobileNumberWsDto();

		if (null != request)
		{
			CustomerModel customer = (CustomerModel) userService.getCurrentUser();
			boolean isWalletCreated = false;
			if (null != customer && null != customer.getIsWalletActivated() && customer.getIsWalletActivated().booleanValue())
			{
				isWalletCreated = true;
			}
			if(isUpdateProfile && isWalletCreated ) 
			{
				if (null == request.getMobileNumber() || ( StringUtils.length(request.getMobileNumber()) != MarketplacecommerceservicesConstants.MOBLENGTH
						&& !request.getMobileNumber().matches(MarketplacecommerceservicesConstants.MOBILE_REGEX)))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9023);
				}
				boolean isOtpGenerated =	this.generateOtpForUpdateWallet(request.getMobileNumber(),customer);
				if(isOtpGenerated) 
				{
					responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					responce.setOtpExpiryTime(configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.TIMEFOROTP));
					return responce;
				}else {
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9047);
				}
			}else {
				validateRequest(request);
			}
			if(null != customer) {
				boolean isWalletOtpVerified = false ;
				boolean isMobileNumberChanged = true;			
				if( null != customer.getIsqcOtpVerify() && customer.getIsqcOtpVerify().booleanValue())
				{
					isWalletOtpVerified = true;
				}
				if( null != customer.getQcVerifyMobileNo() && 
							(customer.getQcVerifyMobileNo().trim().equalsIgnoreCase(request.getMobileNumber().trim())))
				{
					isMobileNumberChanged = false;
				}
				
				if( !isWalletCreated || !isWalletOtpVerified ) {
					
					if(isMobileNumberChanged) {
						if (registerCustomerFacade.checkUniquenessOfMobileForWallet(request.getMobileNumber()))
						{
						//	registerCustomerFacade.registerWalletMobileNumber(request.getFirstName(),request.getLastName(),request.getMobileNumber());//TPR-6272 parameter platformNumber passed
							mplWalletFacade.generateOTP(customer,request.getMobileNumber());
							//Set success flag
							responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							responce.setOtpExpiryTime(configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.TIMEFOROTP));

						}
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5010);
						}
					}else {
						mplWalletFacade.generateOTP(customer,request.getMobileNumber());
						//Set success flag
						responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						responce.setOtpExpiryTime(configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.TIMEFOROTP));
					}
							
				}else {
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5016);

				}
				
			}
		}else
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5015);
		}
		return responce;
	
}
	
	/**
	 * @param applycouponDto
	 * @param cartModel
	 * @return
	 */
	
	@Override
	public ApplyCouponsDTO setTotalPrice(final ApplyCouponsDTO applycouponDto, final AbstractOrderModel cartModel)
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
				 productDiscount = (null != entry.getTotalProductLevelDisc() && entry.getTotalProductLevelDisc()
						.doubleValue() > 0) ? entry.getTotalProductLevelDisc().doubleValue() : 0;
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

		BigDecimal total = new BigDecimal(0.0D);
		final double remainingWalletAmount = cartModel.getTotalWalletAmount().doubleValue() - payableWalletAmount;
		if (null != cartModel.getSubtotal())
		{
			total = new BigDecimal(cartModel.getSubtotal().doubleValue());
			final PriceData subTotalPriceData = priceDataFactory.create(PriceDataType.BUY, total,
					MarketplacecommerceservicesConstants.INR);
			applycouponDto.setSubTotalPrice(subTotalPriceData);

		}

		if (null != cartModel.getDeliveryCost())
		{
			total = new BigDecimal(cartModel.getDeliveryCost().doubleValue());
			final PriceData deliveryChargesPriceData = priceDataFactory.create(PriceDataType.BUY, total,
					MarketplacecommerceservicesConstants.INR);
			applycouponDto.setDeliveryCharges(deliveryChargesPriceData);
		}
		total = new BigDecimal(otherDiscount);
		final PriceData otherDiscountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applycouponDto.setOtherDiscount(otherDiscountPriceData);
		if(bankCouponDiscount > 0.0D) {
			applycouponDto.setIsBankPromotionApplied(true);
      }
		total = new BigDecimal(userCouponDiscount);
		final PriceData couponPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applycouponDto.setAppliedCouponDiscount(couponPriceData);
		applycouponDto.setCouponDiscount(total.toString());

		if (payableWalletAmount > 0.0D)
		{
			applycouponDto.setCliqCashApplied(true);

		}

		total = new BigDecimal(payableWalletAmount);
		final PriceData payableWalletAmountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applycouponDto.setCliqCashPaidAmount(payableWalletAmountPriceData);

		total = new BigDecimal(remainingWalletAmount);
		final PriceData remainingWalletAmountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applycouponDto.setCliqCashBalance(remainingWalletAmountPriceData);

		if (null != cartModel.getTotalPrice())
		{
			total = new BigDecimal(cartModel.getTotalPrice().doubleValue() - payableWalletAmount);
			final PriceData cartTotalPriceData = priceDataFactory.create(PriceDataType.BUY, total,
					MarketplacecommerceservicesConstants.INR);
			applycouponDto.setTotalPrice(cartTotalPriceData);
			if((cartModel.getTotalPrice().doubleValue() - payableWalletAmount) > 0.0D) 
			{
				applycouponDto.setIsRemainingAmount(true);
			}
		}
		return applycouponDto;
	}
	

/**
 * @param request
 */
private void validateRequest(EgvWalletCreateRequestWsDTO request)
	{
		if (null == request)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5015);
		}
		else if (null == request.getFirstName() || request.getFirstName().isEmpty())
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5011);
		}
		else if (null == request.getLastName() || request.getLastName().isEmpty())
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5012);
		}
		else if (null == request.getMobileNumber() || request.getMobileNumber().isEmpty())
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5013);

		}
		else if (StringUtils.length(request.getMobileNumber()) != MarketplacecommerceservicesConstants.MOBLENGTH
				&& !request.getMobileNumber().matches(MarketplacecommerceservicesConstants.MOBILE_REGEX))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9023);
		}
	}

/* (non-Javadoc)
 * @see com.tisl.mpl.service.MplEgvWalletService#setTotalPrice(com.tisl.mpl.wsdto.ApplyCartCouponsDTO, de.hybris.platform.core.model.order.CartModel)
 */
@Override
public ApplyCartCouponsDTO setTotalPrice(ApplyCartCouponsDTO applycouponDto, AbstractOrderModel cartModel)
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
			 productDiscount = (null != entry.getTotalProductLevelDisc() && entry.getTotalProductLevelDisc()
					.doubleValue() > 0) ? entry.getTotalProductLevelDisc().doubleValue() : 0;
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

	BigDecimal total = new BigDecimal(0.0D);
	final double remainingWalletAmount = cartModel.getTotalWalletAmount().doubleValue() - payableWalletAmount;
	if (null != cartModel.getSubtotal())
	{
		total = new BigDecimal(cartModel.getSubtotal().doubleValue());
		final PriceData subTotalPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applycouponDto.setSubTotalPrice(subTotalPriceData);

	}

	if (null != cartModel.getDeliveryCost())
	{
		total = new BigDecimal(cartModel.getDeliveryCost().doubleValue());
		final PriceData deliveryChargesPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applycouponDto.setDeliveryCharges(deliveryChargesPriceData);
	}
	total = new BigDecimal(otherDiscount);
	final PriceData otherDiscountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
			MarketplacecommerceservicesConstants.INR);
	applycouponDto.setOtherDiscount(otherDiscountPriceData);
  if(bankCouponDiscount > 0.0D) {
	  applycouponDto.setIsBankPromotionApplied(true);
  }
	total = new BigDecimal(userCouponDiscount);
	final PriceData couponPriceData = priceDataFactory.create(PriceDataType.BUY, total,
			MarketplacecommerceservicesConstants.INR);
	applycouponDto.setAppliedCouponDiscount(couponPriceData);
	applycouponDto.setCouponDiscount(total.toString());

	if (payableWalletAmount > 0.0D)
	{
		applycouponDto.setCliqCashApplied(true);

	}

	total = new BigDecimal(payableWalletAmount);
	final PriceData payableWalletAmountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
			MarketplacecommerceservicesConstants.INR);
	applycouponDto.setCliqCashPaidAmount(payableWalletAmountPriceData);

	total = new BigDecimal(remainingWalletAmount);
	final PriceData remainingWalletAmountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
			MarketplacecommerceservicesConstants.INR);
	applycouponDto.setCliqCashBalance(remainingWalletAmountPriceData);

	if (null != cartModel.getTotalPrice())
	{
		total = new BigDecimal(cartModel.getTotalPrice().doubleValue() - payableWalletAmount);
		final PriceData cartTotalPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applycouponDto.setTotalPrice(cartTotalPriceData);
		if((cartModel.getTotalPrice().doubleValue() - payableWalletAmount) > 0.0D) 
		{
			applycouponDto.setIsRemainingAmount(true);
		}
	}
	return applycouponDto;
}

/* (non-Javadoc)
 * @see com.tisl.mpl.service.MplEgvWalletService#updateWallet(de.hybris.platform.core.model.user.CustomerModel, java.lang.String, com.tisl.mpl.facades.product.data.MplCustomerProfileData)
 */
@Override
public boolean updateWallet(CustomerModel customer, String otp, MplCustomerProfileData customerToSave)
{
	boolean isWalletUpdated = true;
	
	boolean nameChanged = true ; 
	try {
		nameChanged = mplWalletFacade.checkWalletDetailsChanged(customerToSave);
		if (null != otp && StringUtils.isNotEmpty(otp) )
		{
			OTPResponseData response = mplWalletFacade.validateOTP(customer.getUid(), otp);
				if (null !=response && response.getOTPValid().booleanValue())
				{
					if (registerCustomerFacade.checkUniquenessOfMobileForWallet(customerToSave.getMobileNumber()))
					{

						 isWalletUpdated = updateCustomerWallet(customerToSave, customer);
					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5010);

					}
				}else {
					if(null != response.getInvalidErrorMessage()) {
						if(response.getInvalidErrorMessage().equalsIgnoreCase(MarketplacecommerceservicesConstants.OTPEXPIRY)) {
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9217);
						}else if(response.getInvalidErrorMessage().equalsIgnoreCase(MarketplacecommerceservicesConstants.INVALID_WALLET_OTP)) {
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5014);
						}
					}
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5014);
				
				}
		}else if (nameChanged) {
			 isWalletUpdated = updateCustomerWallet(customerToSave, customer);

		}
	}catch (final EtailBusinessExceptions e)
	{
		ExceptionUtil.etailBusinessExceptionHandler(e, null);
		if (null != e.getErrorCode())
		{
			throw new EtailBusinessExceptions(e.getErrorCode());
		}
	}
	catch(Exception e) {
		LOG.error("Exception occurred while updating Customer Wallet"+e.getMessage(),e);
		throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5004);

	}
	
	return isWalletUpdated;
}


/**
 * @param customerToSave 
 * 
 */
public boolean updateCustomerWallet(MplCustomerProfileData customerToSave,CustomerModel customer)
	{

		QCCustomerRegisterRequest registerCustomerRequest = new QCCustomerRegisterRequest();
		CustomerModel customerModel = new CustomerModel();
		if (null != customerToSave.getMobileNumber())
		{
			customerModel.setMobileNumber(customerToSave.getMobileNumber());
		}else if(null != customer.getQcVerifyMobileNo()){
			customerModel.setMobileNumber(customer.getQcVerifyMobileNo());
		}
		if (null != customerToSave.getFirstName())
		{
			customerModel.setFirstName(customerToSave.getFirstName());
		}else if(null != customer.getQcVerifyFirstName()){
			customerModel.setFirstName(customer.getQcVerifyFirstName());
		}
		if (null != customerToSave.getLastName())
		{
			customerModel.setLastName(customerToSave.getLastName());
		}else if(null != customer.getQcVerifyLastName()){
			customerModel.setLastName(customer.getQcVerifyLastName());
		}
		if (null != customerToSave.getEmailId())
		{
			customerModel.setOriginalUid(customerToSave.getEmailId());
		}else if (null != customer.getOriginalUid()){
			customerModel.setOriginalUid(customer.getOriginalUid());
		}
		registerCustomerRequest.setExternalwalletid(customer.getUid());


		String walletId = customer.getCustomerWalletDetail().getWalletId();
		CustomerWalletDetailResponse responce = mplWalletFacade.updateCustomerWallet(registerCustomerRequest, walletId,
				customerModel.getUid());
		if (null != responce && null != responce.getResponseCode() && responce.getResponseCode().intValue() == 0)
		{
			customer.setQcVerifyFirstName(customerToSave.getFirstName());
			customer.setQcVerifyLastName(customerToSave.getLastName());
			customer.setQcVerifyMobileNo(customerToSave.getMobileNumber());
			modelService.save(customer);
			return true;
		}
		else if (null != responce && null != responce.getResponseCode())
		{
			throw new EtailBusinessExceptions(responce.getResponseCode().toString());
		}
		return false;
	}

/* (non-Javadoc)
 * @see com.tisl.mpl.service.MplEgvWalletService#getEgvProductDetails(de.hybris.platform.core.model.user.CustomerModel)
 */
@Override
public EgvProductInfoWSDTO getEgvProductDetails()
{
	LOG.debug("Getting EGV product Details");
	EgvProductInfoWSDTO egvProductData = new EgvProductInfoWSDTO();
	AmountOptionsWSDTO amountOptions = new AmountOptionsWSDTO();
	double minPrice = 1.0D;
	double maxPrice = 30000.0D;
	String priceOptions = null;
	try {
		CustomerModel customer = (CustomerModel) userService.getCurrentUser();
		if(null != customer) {
			LOG.error(" FIrst Name "+customer.getFirstName());
			LOG.error("QC FIrst Name "+customer.getQcVerifyFirstName());

			if(null != customer.getIsWalletActivated() && customer.getIsWalletActivated().booleanValue() ){
				egvProductData.setIsWalletCreated(true);
			}
			if(null != customer.getIsqcOtpVerify() && customer.getIsqcOtpVerify().booleanValue() )
			{
				egvProductData.setIsWalletOtpVerified(true);
			}else {
				WalletCreateData walletCreateData = mplWalletFacade.getWalletCreateData(customer);
				if(null != walletCreateData) {
					if(null != walletCreateData.getQcVerifyFirstName() && StringUtils.isNotBlank(walletCreateData.getQcVerifyFirstName())){
						egvProductData.setFirstName(walletCreateData.getQcVerifyFirstName());
					}
					if(null != walletCreateData.getQcVerifyLastName() && StringUtils.isNotBlank(walletCreateData.getQcVerifyLastName())){
						egvProductData.setLastName(walletCreateData.getQcVerifyLastName());
					}
					if(null != walletCreateData.getQcVerifyMobileNo() && StringUtils.isNotBlank(walletCreateData.getQcVerifyMobileNo())){
						egvProductData.setMobileNumber(walletCreateData.getQcVerifyMobileNo());
					}
				}
			}
				
			
		}
		
		if (null != configurationService.getConfiguration().getString(MarketplacewebservicesConstants.BUYING_EGV_MIN_PRICE))
		{
			minPrice = configurationService.getConfiguration().getDouble(MarketplacewebservicesConstants.BUYING_EGV_MIN_PRICE);
			LOG.debug("Configurable Buying EGV Min Price " +minPrice);
		}
		if (null != configurationService.getConfiguration().getString(MarketplacewebservicesConstants.BUYING_EGV_MAX_PRICE))
		{
			maxPrice = configurationService.getConfiguration().getDouble(MarketplacewebservicesConstants.BUYING_EGV_MAX_PRICE);
			LOG.debug("Configurable Buying EGV Max Price " +maxPrice);
		}
		if (null != configurationService.getConfiguration().getString(MarketplacewebservicesConstants.BUYING_EGV_PRICE_OPTIONS))
		{
			priceOptions = configurationService.getConfiguration()
					.getString(MarketplacewebservicesConstants.BUYING_EGV_PRICE_OPTIONS);
			LOG.debug("Configurable Buying EGV price options " +priceOptions);
		}
		if (minPrice > 0.0D)
		{
			TotalCliqCashBalanceWsDto minPriceWsDto = new TotalCliqCashBalanceWsDto();
			final BigDecimal minPriceBigDecimal = new BigDecimal(minPrice);
			final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, minPriceBigDecimal,
					MarketplacecommerceservicesConstants.INR);
			if (null != priceData)
			{
				minPriceWsDto.setCurrencyIso(priceData.getCurrencyIso());
				minPriceWsDto.setDoubleValue(priceData.getDoubleValue());
				minPriceWsDto.setFormattedValue(priceData.getFormattedValue());
				minPriceWsDto.setPriceType(priceData.getPriceType());
				minPriceWsDto.setFormattedValueNoDecimal(priceData.getFormattedValueNoDecimal());
				minPriceWsDto.setValue(priceData.getValue());
				amountOptions.setMinPrice(minPriceWsDto);
			}
		}

		if (maxPrice > 0.0D)
		{
			TotalCliqCashBalanceWsDto minPriceWsDto = new TotalCliqCashBalanceWsDto();
			final BigDecimal minPriceBigDecimal = new BigDecimal(maxPrice);
			final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, minPriceBigDecimal,
					MarketplacecommerceservicesConstants.INR);
			if (null != priceData)
			{
				minPriceWsDto.setCurrencyIso(priceData.getCurrencyIso());
				minPriceWsDto.setDoubleValue(priceData.getDoubleValue());
				minPriceWsDto.setFormattedValue(priceData.getFormattedValue());
				minPriceWsDto.setPriceType(priceData.getPriceType());
				minPriceWsDto.setFormattedValueNoDecimal(priceData.getFormattedValueNoDecimal());
				minPriceWsDto.setValue(priceData.getValue());
				amountOptions.setMaxPrice(minPriceWsDto);
			}
		}

		if (null != priceOptions)
		{
			final String[] configurablePriceOptions = priceOptions.split(",");
			List<TotalCliqCashBalanceWsDto> configurablePrices = new ArrayList<>();
			if (null != configurablePriceOptions)
			{
				for (final String price : configurablePriceOptions)
				{
					TotalCliqCashBalanceWsDto PriceWsDto = new TotalCliqCashBalanceWsDto();
					final BigDecimal PriceBigDecimal = new BigDecimal(Double.valueOf(price).doubleValue());
					final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, PriceBigDecimal,
							MarketplacecommerceservicesConstants.INR);
					if (null != priceData)
					{
						PriceWsDto.setCurrencyIso(priceData.getCurrencyIso());
						PriceWsDto.setDoubleValue(priceData.getDoubleValue());
						PriceWsDto.setFormattedValue(priceData.getFormattedValue());
						PriceWsDto.setPriceType(priceData.getPriceType());
						PriceWsDto.setFormattedValueNoDecimal(priceData.getFormattedValueNoDecimal());
						PriceWsDto.setValue(priceData.getValue());
						configurablePrices.add(PriceWsDto);
					}
				}
				
				if(CollectionUtils.isNotEmpty(configurablePrices)){
					amountOptions.setOptions(configurablePrices);
				}
			}
		}
		
		egvProductData.setAmountOptions(amountOptions);
		egvProductData.setIsCustomizationAvailable(true);
		egvProductData.setIsMoreDesigns(false);
		egvProductData.setProductDisclaimerForGC(MarketplacewebservicesConstants.BUYING_EGV_PRODUCT_DISCLAIMER);
		egvProductData.setGiftCartImageUrl("https://qa2.tataunistore.com/_ui/responsive/theme-blue/images/GiftCard.jpg");
	//	egvProductData.setSellerimageUrl("https://qa2.tataunistore.com/_ui/responsive/theme-blue/images/GiftCard.jpg");

	}catch (Exception e) {
		LOG.error("Exception occurredd while getting EGV Product Details "+e.getMessage());
	}

	return egvProductData;
}

	
}
