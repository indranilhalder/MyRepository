/**
 * 
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.localization.Localization;

import java.math.BigDecimal;
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
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.model.MplCartOfferVoucherModel;
import com.tisl.mpl.pojo.request.Customer;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.service.MplEgvWalletService;
import com.tisl.mpl.wsdto.ApplyCartCouponsDTO;
import com.tisl.mpl.wsdto.ApplyCliqCashWsDto;
import com.tisl.mpl.wsdto.ApplyCouponsDTO;
import com.tisl.mpl.wsdto.EgvCheckMobileNumberWsDto;
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
			custInfo.setCorporateName("Tata Unistore Ltd");

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

			customerRegisterReq.setExternalwalletid(currentCustomer.getOriginalUid());
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
	public EgvWalletCreateResponceWsDTO verifyOtpAndCreateWallet(CustomerModel currentCustomer, String otp)
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
							responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
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
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5014);
				}
			}else {
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5014);

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
		UserCliqCashWsDto responce = null;
		if (null != currentCustomer)
		{
			if (null != currentCustomer.getIsWalletActivated() && currentCustomer.getIsWalletActivated().booleanValue())
			{

				customerWalletDetailData = mplWalletFacade.getCustomerWallet(currentCustomer.getCustomerWalletDetail().getWalletId());

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
				if(null != currentCustomer.getIsqcOtpVerify() && currentCustomer.getIsqcOtpVerify().booleanValue() )
				{
					responce.setIsWalletOtpVerified(true);
				}
			}
			else
			{
				try
				{
					responce = new UserCliqCashWsDto();
					responce.setIsWalletCreated(false);
					WalletCreateData walletCreateData = mplWalletFacade.getWalletCreateData();
					if (null != walletCreateData)
					{
						responce.setFirstName(walletCreateData.getQcVerifyFirstName());
						responce.setLastName(walletCreateData.getQcVerifyLastName());
						responce.setMobileNumber(walletCreateData.getQcVerifyMobileNo());
					}
					responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
				catch (Exception e)
				{
					LOG.error("Exception occurred while gettig  cliqCash details");
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
		boolean isCartVoucherPresent =false;
		String cartCouponCode = MarketplacecommerceservicesConstants.EMPTY;
		try {
		if(null == walletAmount) {
				walletAmount = cart.getTotalWalletAmount();
				
			}
			final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(cart.getDiscounts());

			 isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();
			 if(isCartVoucherPresent) {
				 cart = mplCouponFacade.removeLastCartCoupon(cart);
			 }
			
			applyCliqCashWsDto.setDiscount(Double.valueOf(0));
			applyCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
			if (null != walletAmount &&  walletAmount.doubleValue() > 0.0D)
			{
				LOG.debug("Bucket Balance =" + walletAmount);
				 Double totalAmt = cart.getTotalPrice();

				 // If  Customer is Having Enough money in Cliq Cash Then  pay Using Cliq Cash   
				 // Otherwise He needs to Pay the Remaining Amount using Other Payment Methods( Net Banking ,Debit Cart ... ) 
				
				//	 if Customer Is having Enough Money In Cliq Cash , Then Saving SplitModeInfo as CLIQ_CASH 
				if (null != totalAmt && walletAmount.doubleValue() >= totalAmt.doubleValue())
				{
					cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH);
					cart.setPayableWalletAmount(totalAmt);
					cart.setTotalWalletAmount(walletAmount);
					cart.setPayableNonWalletAmount(Double.valueOf(0.0D));
					//cart.setTotalPrice(value);
					modelService.save(cart);
					modelService.refresh(cart);

					 commerceCartService.recalculateCart((CartModel)cart);
					 applyCliqCashWsDto.setDiscount(cart.getTotalDiscounts());
					applyCliqCashWsDto.setIsRemainingAmount(false);
					//applyCliqCashWsDto.setCliqCashApplied(totalAmt);
					applyCliqCashWsDto.setPaybleAmount(Double.valueOf(0));
					applyCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
					
					applyCliqCashWsDto =setTotalPrice(applyCliqCashWsDto,cart);
					applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					
				}
				
				//  else if Customer Is Not having Enough Money In Cliq Cash , Then Saving SplitModeInfo as SPLIT_MODE 
				else
				{
					applyCliqCashWsDto.setIsRemainingAmount(true);
					if (isCartVoucherPresent)
						{
							cartCouponCode = cartCouponObj.getSecond();
							cart.setCheckForBankVoucher("true");
							modelService.save(cart);
							mplCouponFacade.applyCartVoucher(cartCouponCode,(CartModel) cart, null);
							cart.setCheckForBankVoucher("false");
							
						}
							cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT);
							cart.setPayableWalletAmount(walletAmount);
							cart.setTotalWalletAmount(walletAmount);
							totalAmt = cart.getTotalPrice();
							double payableJuspayAmount = totalAmt.doubleValue() - walletAmount.doubleValue();
							cart.setPayableNonWalletAmount(Double.valueOf(payableJuspayAmount));
							modelService.save(cart);
						
					double juspayTotalAmt = 0.0D;
					
					if (totalAmt.doubleValue() > 0.0D && walletAmount.doubleValue() > 0.0D)
					{
						juspayTotalAmt = totalAmt.doubleValue() - walletAmount.doubleValue();
					}
					else if (null != cart.getTotalPrice() && cart.getTotalPrice().doubleValue() > 0.0D)
					{
						juspayTotalAmt = cart.getTotalPrice().doubleValue();
					}
					applyCliqCashWsDto.setDiscount(cart.getTotalDiscounts());
					applyCliqCashWsDto.setPaybleAmount(Double.valueOf(juspayTotalAmt));
					applyCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
					applyCliqCashWsDto = setTotalPrice(applyCliqCashWsDto,cart);

					applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}

			}else {
				applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				applyCliqCashWsDto.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B5001));
				applyCliqCashWsDto.setErrorCode(MarketplacecommerceservicesConstants.B5001);
			}
		}catch (Exception e) {
			LOG.error("Exception occurred while applying cliqCash");
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
		double couponDiscount = 0.0D;
		if (!cartModel.getDiscounts().isEmpty())
		{
			for (final DiscountModel discount : cartModel.getDiscounts())
			{
				if (discount instanceof MplCartOfferVoucherModel)
				{
					bankCouponDiscount += discount.getValue().doubleValue();
				}
				else
				{
					couponDiscount += discount.getValue().doubleValue();
				}
			}
		}

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
		total = new BigDecimal(bankCouponDiscount);
		final PriceData otherDiscountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applyCliqCashWsDto.setOtherDiscount(otherDiscountPriceData);

		total = new BigDecimal(couponDiscount);
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
				}
			}else {
				validateRequest(request);
			}
			if(null != customer) {
				boolean isWalletOtpVerified = false ;
				boolean isMobileNumberChanged = true;
				boolean isNameChanged = true;
			
				if( null != customer.getIsqcOtpVerify() && customer.getIsqcOtpVerify().booleanValue())
				{
					isWalletOtpVerified = true;
				}
				if( null != customer.getQcVerifyMobileNo() && 
							(customer.getQcVerifyMobileNo().trim().equalsIgnoreCase(request.getMobileNumber().trim())))
				{
					isMobileNumberChanged = false;
				}
				
				if( ( null != customer.getQcVerifyFirstName() && 
						(customer.getQcVerifyFirstName().trim().equalsIgnoreCase(request.getFirstName())) )|| 
						(  null != customer.getQcVerifyLastName() && 
						(customer.getQcVerifyLastName().trim().equalsIgnoreCase(request.getLastName())) ))
			{
					isNameChanged = false;
			}
				
				if( !isWalletCreated || !isWalletOtpVerified ) {
					
					if(isMobileNumberChanged) {
						if (registerCustomerFacade.checkUniquenessOfMobileForWallet(request.getMobileNumber()))
						{
							registerCustomerFacade.registerWalletMobileNumber(request.getFirstName(),request.getLastName(),request.getMobileNumber());//TPR-6272 parameter platformNumber passed
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
						if(isNameChanged){
							registerCustomerFacade.registerWalletMobileNumber(request.getFirstName(),request.getLastName(),request.getMobileNumber());//TPR-6272 parameter platformNumber passed
						}
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
		double couponDiscount = 0.0D;
		if (!cartModel.getDiscounts().isEmpty())
		{
			for (final DiscountModel discount : cartModel.getDiscounts())
			{
				if (discount instanceof MplCartOfferVoucherModel)
				{
					bankCouponDiscount += discount.getValue().doubleValue();
				}
				else
				{
					couponDiscount += discount.getValue().doubleValue();
				}
			}
		}

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
		total = new BigDecimal(bankCouponDiscount);
		final PriceData otherDiscountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
				MarketplacecommerceservicesConstants.INR);
		applycouponDto.setOtherDiscount(otherDiscountPriceData);

		total = new BigDecimal(couponDiscount);
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
	double couponDiscount = 0.0D;
	if (!cartModel.getDiscounts().isEmpty())
	{
		for (final DiscountModel discount : cartModel.getDiscounts())
		{
			if (discount instanceof MplCartOfferVoucherModel)
			{
				bankCouponDiscount += discount.getValue().doubleValue();
			}
			else
			{
				couponDiscount += discount.getValue().doubleValue();
			}
		}
	}

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
	total = new BigDecimal(bankCouponDiscount);
	final PriceData otherDiscountPriceData = priceDataFactory.create(PriceDataType.BUY, total,
			MarketplacecommerceservicesConstants.INR);
	applycouponDto.setOtherDiscount(otherDiscountPriceData);

	total = new BigDecimal(couponDiscount);
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
	nameChanged = mplWalletFacade.checkWalletDetailsChanged(customerToSave);
	if (null != otp && !customer.getMobileNumber().equalsIgnoreCase(customerToSave.getMobileNumber()))
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
			registerCustomerRequest.setExternalwalletid(customerToSave.getEmailId());
		}else if (null != customer.getOriginalUid()){
			customerModel.setOriginalUid(customer.getOriginalUid());
			registerCustomerRequest.setExternalwalletid(customer.getOriginalUid());
		}



		String walletId = customer.getCustomerWalletDetail().getWalletId();
		CustomerWalletDetailResponse responce = mplWalletFacade.updateCustomerWallet(registerCustomerRequest, walletId,
				customerModel.getUid());
		if (null != responce && null != responce.getResponseCode() && responce.getResponseCode().intValue() == 0)
		{
			customer.setQcVerifyFirstName(customerToSave.getMobileNumber());
			customer.setQcVerifyLastName(customerToSave.getFirstName());
			customer.setQcVerifyMobileNo(customerToSave.getLastName());
			modelService.save(customerModel);
			return true;
		}
		else if (null != responce && null != responce.getResponseCode())
		{
			throw new EtailBusinessExceptions(responce.getResponseCode().toString());
		}
		return false;
	}

	
}
