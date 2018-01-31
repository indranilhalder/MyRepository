/**
 *
 */
package com.tisl.mpl.facades.wallet.impl;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.facades.cms.data.WalletCreateData;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.pojo.request.QCCreditRequest;
import com.tisl.mpl.pojo.request.QCCustomerPromotionRequest;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.request.QCRefundRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
import com.tisl.mpl.pojo.response.WalletTransacationsList;
import com.tisl.mpl.service.MplWalletServices;
import com.tisl.mpl.sms.facades.SendSMSFacade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.pojo.request.Customer;



/**
 * @author TUL
 *
 */
public class MplWalletFacadeImpl implements MplWalletFacade
{

	private static final Logger LOG = Logger.getLogger(MplWalletFacadeImpl.class);
	@Resource(name = "mplWalletServices")
	private MplWalletServices mplWalletServices;

	@Resource
	private MplPaymentService mplPaymentService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;
	
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private OTPGenericService otpGenericService;
	
	@Autowired
	private SendSMSFacade sendSMSFacade;
	
	@Autowired
	RegisterCustomerFacade registerCustomerFacade;
	
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
	 * @return the mplWalletServices
	 */
	public MplWalletServices getMplWalletServices()
	{
		return mplWalletServices;
	}



	/**
	 * @param mplWalletServices
	 *           the mplWalletServices to set
	 */
	public void setMplWalletServices(final MplWalletServices mplWalletServices)
	{
		this.mplWalletServices = mplWalletServices;
	}



	@Override
	public void getWalletInitilization()
	{

		getMplWalletServices().walletInitilization();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#createWalletContainer()
	 */
	@Override
	public QCCustomerRegisterResponse createWalletContainer(final QCCustomerRegisterRequest registerCustomerRequest)
	{
		return getMplWalletServices().registerCustomerWallet(registerCustomerRequest, generateQCTransactionId());

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#addEGVToWallet()
	 */
	@Override
	public void addEGVToWallet()
	{

		getMplWalletServices().addEgvToWallet();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#getWalletBalance()
	 */
	@Override
	public BalanceBucketWise getQCBucketBalance(final String customerWalletId)

	{

		return getMplWalletServices().getQCBucketBalance(customerWalletId, generateQCTransactionId());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#getWalletRefundRedeem()
	 */
	@Override
	public QCRedeeptionResponse  getWalletRefundRedeem(String walletId, QCRefundRequest qcRefundRequest)
	{
		return getMplWalletServices().getWalletRefundRedeem(walletId,qcRefundRequest);
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#addTULWalletCashBack()
	 */
	@Override
	public QCRedeeptionResponse addTULWalletCashBack(final String walletId, final QCCustomerPromotionRequest request)
	{
		final String transactionId = generateQCTransactionId();
		request.setInvoiceNumber(transactionId);
		return getMplWalletServices().addTULWalletCashBack(walletId, request);
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#refundTULPromotionalCash()
	 */
	@Override
	public QCRedeeptionResponse refundTULPromotionalCash(final String walletId, final String transactionId)
	{

		return getMplWalletServices().refundTULPromotionalCash(walletId, transactionId);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#getCustomerWallet()
	 */
	@Override
	public CustomerWalletDetailResponse getCustomerWallet(final String customerWalletId)
	{
		return getMplWalletServices().getCustomerWallet(customerWalletId, generateQCTransactionId());

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#getWalletRedeem(java.lang.String, java.lang.String,
	 * com.tisl.mpl.pojo.request.QCRedeemRequest)
	 */
	@Override
	public QCRedeeptionResponse getWalletRedeem(final String customerWalletId, final QCRedeemRequest qcRedeemRequest)
	{

		final String transactionId = generateQCTransactionId();

		return getMplWalletServices().getWalletRedeem(customerWalletId, transactionId, qcRedeemRequest);
	}

	@Override
	public String generateQCTransactionId()
	{
		return getMplPaymentService().createQCPaymentId();
	}

	@Override
	public RedimGiftCardResponse getAddEGVToWallet(final String cardNumber, final String cardPin)
	{
		final String transactionId = generateQCTransactionId();
		RedimGiftCardResponse balance = new RedimGiftCardResponse();
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		if (null != currentCustomer.getIsWalletActivated())
		{
			try {
				LOG.debug("Calling to QC For Adding Amount to Wallet ");
				balance = getMplWalletServices().getAddEGVToWallet(cardNumber, cardPin, transactionId,
						currentCustomer.getCustomerWalletDetail().getWalletId());
			}catch (Exception e) {
				LOG.error("Exception occurred in QC CAll While Adding Money To Wallet"+e.getMessage());
			}
			try {
				final WalletCardApportionDetailModel walletCardApportionDetailModel  =getMplWalletServices().getOrderFromWalletCardNumber(cardNumber);
				OrderModel orderModel = null;
				if(null != walletCardApportionDetailModel && null!= walletCardApportionDetailModel.getOrderId()){
					orderModel =orderModelService.getParentOrder(walletCardApportionDetailModel.getOrderId());
				}
				if(null != orderModel && null != balance && balance.getResponseCode().intValue() == 0){
					if ( null != orderModel.getCode())
					{
						LOG.debug("Update Order Status to Redeemed order ID "+orderModel.getCode());
						orderModel.setStatus(OrderStatus.REDEEMED);
						modelService.save(orderModel);
						System.out.println(" ************** Order Status updated Success Fully to  ----:" + OrderStatus.REDEEMED
								+ "Card Number :" + cardNumber);
					}
				}
			}catch (Exception e) {
				LOG.error("Exception occurred in While Changing Status to Redeemed"+e.getMessage());
			}
			
			if (null != balance)
			{
				return balance;
			}
		}
		balance.setResponseMessage("error");
		return balance;

	}

	@Override
	public WalletTransacationsList getWalletTransactionList()
	{
		//List<WalletTrasacationsListData> walletTrasacationsListDataList=new ArrayList<WalletTrasacationsListData>();
		WalletTransacationsList walletTransacationsList = null;
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		//		if (null != currentCustomer && null != currentCustomer.getIsWalletActivated()){
		//			System.out.println("Customer has Actived try to redim the card");
		//			if(null != currentCustomer.getCustomerWalletDetail() && null!= currentCustomer.getCustomerWalletDetail().getWalletId()){
		final String transactionId = generateQCTransactionId();
		walletTransacationsList = getMplWalletServices()
				.getWalletTransactionList(currentCustomer.getCustomerWalletDetail().getWalletId(), transactionId);
		//			}
		//		 }

		if (null != walletTransacationsList)
		{
			//		for(WalletTransactions trasaction :walletTransacationsList.getWalletTransactions()){
			//			WalletTrasacationsListData data = new WalletTrasacationsListData();
			//			data.setWalletNumber(trasaction.getWalletNumber());
			//			data.setInvoiceNumber(trasaction.getInvoiceNumber());
			//			data.setDateAtServer(trasaction.getDateAtServer());
			//			data.setBatchNumber(trasaction.getBatchNumber());
			//			data.setAmount(trasaction.getAmount());
			//			data.setBalance(trasaction.getBalance());
			//			data.setBillAmount(trasaction.getBillAmount());
			//			data.setMerchantOutletName(trasaction.getMerchantOutletName());
			//			data.setTransactionPostDate(trasaction.getTransactionPostDate());
			//			data.setTransactionStatus(trasaction.getTransactionStatus());
			//			data.setUser(trasaction.getUser());
			//			data.setMerchantName(trasaction.getMerchantName());
			//			data.setpOSName(trasaction.getpOSName());
			//			data.setCustomerName(trasaction.getCustomerName());
			//			data.setWalletPIN(trasaction.getWalletPIN());
			//			data.setNotes(trasaction.getNotes());
			//			data.setApprovalCode(trasaction.getApprovalCode());
			//			data.setResponseCode(trasaction.getResponseCode());
			//			data.setResponseMessage(trasaction.getResponseMessage());
			//			data.setTransactionId(trasaction.getTransactionId());
			//			data.setTransactionType(trasaction.getTransactionType());
			//			data.setErrorCode(trasaction.getErrorCode());
			//			data.setErrorDescription(trasaction.getErrorDescription());
			//			
			//			walletTrasacationsListDataList.add(data);
			//		  }

			return walletTransacationsList;
		}
		return (WalletTransacationsList) CollectionUtils.EMPTY_COLLECTION;
	}



	@Override
	public QCRedeeptionResponse createPromotion(final String walletId, final QCCustomerPromotionRequest request)
	{
		final String transactionId = generateQCTransactionId();
		request.setInvoiceNumber(transactionId);
		return getMplWalletServices().createPromotion(walletId, request);
	}

	
	@Override
	public QCRedeeptionResponse qcCredit(final String walletId, final QCCreditRequest request)
	{
		return getMplWalletServices().qcCredit(walletId, request);
	}


	@Override
	public CustomerWalletDetailResponse activateQCUserAccount(String walletId)
	{
		final String transactionId = generateQCTransactionId();
		return getMplWalletServices().activateQCUserAccount(walletId,transactionId);
	}

	@Override
	public CustomerWalletDetailResponse deactivateQCUserAccount(String walletId)
	{
		final String transactionId = generateQCTransactionId();
		return getMplWalletServices().deactivateQCUserAccount(walletId,transactionId);
	}
	
	@Override
	public WalletCreateData getWalletCreateData()
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		WalletCreateData walletCreateData = new WalletCreateData();
	   if (StringUtils.isNotEmpty(currentCustomer.getQcVerifyFirstName()))
		{
			walletCreateData.setQcVerifyFirstName(currentCustomer.getQcVerifyFirstName());
		}
		else
		{
			walletCreateData.setQcVerifyFirstName(currentCustomer.getFirstName());
		}
		if (StringUtils.isNotEmpty(currentCustomer.getQcVerifyLastName()))
		{
			walletCreateData.setQcVerifyLastName(currentCustomer.getQcVerifyLastName());
		}
		else
		{
			walletCreateData.setQcVerifyLastName(currentCustomer.getLastName());
		}
		if (StringUtils.isNotEmpty(currentCustomer.getQcVerifyMobileNo()))
		{
			walletCreateData.setQcVerifyMobileNo(currentCustomer.getQcVerifyMobileNo());
		}
		else
		{
			walletCreateData.setQcVerifyMobileNo(currentCustomer.getMobileNumber());
		}
		return walletCreateData;
	}
	
	@Override
	public OTPResponseData validateOTP(final String customerID, final String enteredOTPNumber)
	{

		final OTPResponseData otpResponse = otpGenericService.validateLatestOTP(customerID, null, enteredOTPNumber,
				OTPTypeEnum.QC_OTP,
				Long.parseLong(configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.TIMEFOROTP)));
		return otpResponse;
	}
	
	@Override
	public String generateOTP(final CustomerModel customerModel, final String mobileNumber)
	{
		String otp = null;
		try
		{
			otp = otpGenericService.generateOTP(customerModel.getUid(), OTPTypeEnum.QC_OTP.getCode(), mobileNumber);
			sendNotificationForWalletCreate(customerModel, otp, mobileNumber);
			
		}

		catch (final Exception exception)
		{
			LOG.error("MplWalletFacadeImpl:...:OTP Genrate" + exception.getMessage());
		}
		return otp;
	}
	
	/***
	 * Send Notification For Wallet_Create 
	 *
	 */
	@Override
	public void sendNotificationForWalletCreate(final CustomerModel customerModel, final String otPNumber, final String mobileNumber)
	{
		final String mplCustomerName = customerModel.getFirstName();
		final String contactNumber = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);
		sendSMSFacade.sendSms(
				MarketplacecommerceservicesConstants.SMS_SENDER_ID,
				MarketplacecommerceservicesConstants.SMS_MESSAGE_WALLET_CREATE_OTP
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
								mplCustomerName != null ? mplCustomerName : "There")
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, otPNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, contactNumber), mobileNumber);

	}
	
	@Override
	public CustomerWalletDetailResponse updateCustomerWallet(QCCustomerRegisterRequest registerCustomerRequest, String walletId,
			String transactionId){
		return getMplWalletServices().updateCustomerWallet(registerCustomerRequest,walletId,generateQCTransactionId());

	}
	
	@Override
	public CustomerWalletDetailResponse editWalletInformtion(CustomerModel currentCustomer, String qcFirstName, String qcLastName,
			String qcMobileNo)
	{
		final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
		final Customer custInfo = new Customer();
		custInfo.setEmail(currentCustomer.getOriginalUid());
		custInfo.setEmployeeID(currentCustomer.getUid());
		custInfo.setCorporateName("Tata Unistore Ltd");
		if (null != qcFirstName)
		{
			custInfo.setFirstname(qcFirstName);
		}
		if (null != qcLastName)
		{
			custInfo.setLastName(qcLastName);
		}
		if (null != qcMobileNo)
		{
			custInfo.setPhoneNumber(qcMobileNo);
		}

		customerRegisterReq.setExternalwalletid(currentCustomer.getUid());
		customerRegisterReq.setCustomer(custInfo);
		customerRegisterReq.setNotes("Activating Customer " + currentCustomer.getUid());
		
		final CustomerWalletDetailResponse customerRegisterResponse =updateCustomerWallet(customerRegisterReq, currentCustomer.getCustomerWalletDetail().getWalletId(), currentCustomer.getUid());
		return customerRegisterResponse;
	}
	
	
	@Override
	public boolean customerWalletUpdate(MplCustomerProfileData mplCustomerProfileData){
		CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		if(currentCustomer.getIsWalletActivated()!=null && currentCustomer.getIsWalletActivated().booleanValue()){
			boolean isChanged=false;
			String qcFirstName=null;
			String qcLastName=null;
			String qcMobileNo=null;
			if(StringUtils.isNotBlank(currentCustomer.getQcVerifyFirstName()) && !currentCustomer.getQcVerifyFirstName().equalsIgnoreCase(mplCustomerProfileData.getFirstName()) &&  StringUtils.isNotBlank(mplCustomerProfileData.getFirstName())){
				qcFirstName=mplCustomerProfileData.getFirstName();
						isChanged=true;
				}else{
					qcFirstName=currentCustomer.getQcVerifyFirstName();
				}
			if(StringUtils.isNotBlank(currentCustomer.getQcVerifyLastName()) && !currentCustomer.getQcVerifyLastName().equalsIgnoreCase(mplCustomerProfileData.getLastName()) &&  StringUtils.isNotBlank(mplCustomerProfileData.getLastName())){
				qcLastName=mplCustomerProfileData.getLastName();
						isChanged=true;
				}else{
					qcLastName=currentCustomer.getQcVerifyFirstName();
				}
			
			if(StringUtils.isNotBlank(currentCustomer.getQcVerifyMobileNo()) && !currentCustomer.getQcVerifyFirstName().equalsIgnoreCase(mplCustomerProfileData.getMobileNumber()) &&  StringUtils.isNotBlank(mplCustomerProfileData.getMobileNumber())){
				qcMobileNo=mplCustomerProfileData.getMobileNumber();
						isChanged=true;
				} else if(StringUtils.isEmpty(currentCustomer.getQcVerifyMobileNo()) && StringUtils.isNotBlank(mplCustomerProfileData.getMobileNumber())){
					qcMobileNo=mplCustomerProfileData.getMobileNumber();
					isChanged=true;
				}else{
					qcMobileNo=currentCustomer.getQcVerifyMobileNo();
				}
			
			
			if(isChanged){
				final CustomerWalletDetailResponse customerRegisterResponse = editWalletInformtion(currentCustomer, qcFirstName,
						qcLastName, qcMobileNo);
				if (null != customerRegisterResponse.getResponseCode() && customerRegisterResponse.getResponseCode() == Integer.valueOf(0))
				{
					currentCustomer.setIsqcOtpVerify(Boolean.valueOf(true));
					currentCustomer.setQcVerifyFirstName(qcFirstName);
					currentCustomer.setQcVerifyLastName(qcLastName);
					currentCustomer.setQcVerifyMobileNo(qcMobileNo);
					currentCustomer.setFirstName(qcFirstName);
					currentCustomer.setLastName(qcLastName);
					currentCustomer.setMobileNumber(qcMobileNo);
					modelService.save(currentCustomer);
					return true;
				}else{
					return false;
				}
			}
			}
		return true;
	}

	
	@Override
	public String qcValidationMobileNo(String mobileNo){
		CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		if(currentCustomer.getIsWalletActivated()!=null && currentCustomer.getIsWalletActivated().booleanValue()){
			if(!StringUtils.isNotBlank(mobileNo)){
				return "MOBILEERROR";
			}
		 boolean isUsed = registerCustomerFacade.checkUniquenessOfMobileForWallet(mobileNo);
	
		 if(isUsed){
			 if(!mobileNo.equalsIgnoreCase(currentCustomer.getMobileNumber())){
				     generateOTP(currentCustomer, mobileNo);
					return "OTPCREATED";
			 }else{
				 return "success";
			 }
			
		 }else{
			 if(mobileNo.equalsIgnoreCase(currentCustomer.getMobileNumber())) {
				 return "success";
			 } else {
				 return "USED";
			 }
		 }
		}
		return "success";

	}
}
