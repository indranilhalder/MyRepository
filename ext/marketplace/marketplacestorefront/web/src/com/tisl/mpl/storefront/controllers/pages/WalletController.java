/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.core.model.CustomerWalletDetailModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.exception.QCServiceCallException;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.cms.data.WalletCreateData;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.pojo.request.Customer;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
import com.tisl.mpl.pojo.response.WalletTransacationsList;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.web.forms.AddToCardWalletForm;
import com.tisl.mpl.storefront.web.forms.WalletCreateForm;


/**
 * @author TUL
 *
 */

@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LINK_WALLET)
@RequireHardLogIn
public class WalletController extends AbstractPageController
{

	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "checkoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	private SessionService sessionService;

	@Resource(name = "mplWalletFacade")
	private MplWalletFacade mplWalletFacade;

	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;
	@Autowired
	private ExtendedUserService extendedUserService;
	@Autowired
	RegisterCustomerFacade registerCustomerFacade;


	private static final Logger LOG = Logger.getLogger(WalletController.class);
	protected static final String REDIM_WALLET_CODE_PATTERN = "/redimWallet";
	protected static final String REDIM_WALLET_FROM_EMAIL = "/redimWalletFromEmail/";
	protected static final String WALLET_CREATE_VALIDATE_OTP_URL = "/validateWalletOTP";
	protected static final String WALLET_CREATE_OTP_POPUP = "/walletOTPPopup";
	protected static final String WALLET_CREATE_OTP_GENERATE = "/walletCreateOTP";


	final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

	@ModelAttribute("getCurrentDate")
	public String checkDisplayOffer()
	{
		return dateFormat.format(new Date());
	}


	@SuppressWarnings("boxing")
	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String getWalletView(final Model model)

			throws CMSItemNotFoundException, QCServiceCallException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId("TULWalletPage"));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId("TULWalletPage"));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		model.addAttribute("addToCardWalletForm", new AddToCardWalletForm());
		return getViewForPage(model);
	}



	/**
	 * @param model
	 */
	@RequestMapping(value = REDIM_WALLET_CODE_PATTERN, method = RequestMethod.POST)
	public String getRedimWalletView(@ModelAttribute("addToCardWalletForm") final AddToCardWalletForm addToCardWalletForm,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, Exception
	{

		try
		{
			final RedimGiftCardResponse response = mplWalletFacade.getAddEGVToWallet(addToCardWalletForm.getCardNumber(),
					addToCardWalletForm.getCardPin());

			if (null != response && null != response.getResponseCode() && null == Integer.valueOf(0))
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
						"text.cliqcash.add.money.success", null);
				LOG.info("card Added Sucesss " + response.getResponseMessage());
				return REDIRECT_PREFIX + "/wallet/getcliqcashPage";
			}
			else if (null != response && null != response.getResponseCode() && response.getResponseCode() != Integer.valueOf(0))
			{
				setValidErrorCodeHandling(response.getResponseCode().intValue(), redirectAttributes);
				LOG.error("card Add Error " + response.getResponseMessage());
				return REDIRECT_PREFIX + "/wallet/getcliqcashPage";
			}
		}
		catch (final Exception ex)
		{
			GlobalMessages.addMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER, "text.cliqcash.add.money.Fail",
					null);
			ex.printStackTrace();
			/*
			 * if(ex.getMessage().contains("SocketTimeoutException")){ GlobalMessages.addErrorMessage(model,
			 * "text.cliqcash.timeout.massege"); }
			 */
		}
		return REDIRECT_PREFIX + "/wallet/getcliqcashPage";
	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = "/getcliqcashPage", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getCliqCash(final Model model, @SuppressWarnings("unused") final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, QCServiceCallException
	{
		double balanceAmount = 0;
		CustomerWalletDetailResponse customerWalletDetailData = new CustomerWalletDetailResponse();
		WalletTransacationsList walletTrasacationsListData1 = new WalletTransacationsList();
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

		try
		{
			if (null != currentCustomer && null != currentCustomer.getIsWalletActivated() && currentCustomer.getIsWalletActivated())
			{

				customerWalletDetailData = mplWalletFacade.getCustomerWallet(currentCustomer.getCustomerWalletDetail().getWalletId());
				if (null != customerWalletDetailData.getWallet() && customerWalletDetailData.getWallet().getBalance() > 0)
				{
					balanceAmount = customerWalletDetailData.getWallet().getBalance();
				}
				final WalletTransacationsList walletTrasacationsListData = mplWalletFacade.getWalletTransactionList();
				if (null != walletTrasacationsListData && null != walletTrasacationsListData.getResponseCode()
						&& walletTrasacationsListData.getResponseCode() == 0)
				{
					walletTrasacationsListData1 = walletTrasacationsListData;

				}

			}
			else
			{
				model.addAttribute("isOTPValidtion",Boolean.FALSE);
				/*final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
				final Customer custInfo = new Customer();
				custInfo.setEmail(currentCustomer.getOriginalUid());
				custInfo.setEmployeeID(currentCustomer.getUid());
				custInfo.setCorporateName("Tata Unistore Ltd");

				if (null != currentCustomer.getFirstName())
				{
					custInfo.setFirstname(currentCustomer.getFirstName());
				}
				if (null != currentCustomer.getLastName())
				{
					custInfo.setLastName(currentCustomer.getLastName());
				}

				customerRegisterReq.setExternalwalletid(currentCustomer.getUid());
				customerRegisterReq.setCustomer(custInfo);
				customerRegisterReq.setNotes("Activating Customer " + currentCustomer.getUid());
				final QCCustomerRegisterResponse customerRegisterResponse = mplWalletFacade
						.createWalletContainer(customerRegisterReq);
				if (null != customerRegisterResponse.getResponseCode() && customerRegisterResponse.getResponseCode() == 0)
				{
					final CustomerWalletDetailModel custWalletDetail = modelService.create(CustomerWalletDetailModel.class);
					custWalletDetail.setWalletId(customerRegisterResponse.getWallet().getWalletNumber());
					custWalletDetail.setWalletState(customerRegisterResponse.getWallet().getStatus());
					custWalletDetail.setCustomer(currentCustomer);
					custWalletDetail.setServiceProvider("Tata Unistore Ltd");

					modelService.save(custWalletDetail);

					currentCustomer.setCustomerWalletDetail(custWalletDetail);
					currentCustomer.setIsWalletActivated(true);
					modelService.save(currentCustomer);

					GlobalMessages.addMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
							"text.cliqcash.use.wallet.active", null);

					customerWalletDetailData = mplWalletFacade
							.getCustomerWallet(currentCustomer.getCustomerWalletDetail().getWalletId());
					if (null != customerWalletDetailData.getWallet() && customerWalletDetailData.getWallet().getBalance() > 0)
					{
						balanceAmount = customerWalletDetailData.getWallet().getBalance();
					}
					final WalletTransacationsList walletTrasacationsListData = mplWalletFacade.getWalletTransactionList();

					if (null != walletTrasacationsListData && walletTrasacationsListData.getResponseCode() == 0)
					{

						walletTrasacationsListData1 = walletTrasacationsListData;
					}

				}
				else if (null != customerRegisterResponse.getResponseCode() && customerRegisterResponse.getResponseCode() == 10545)
				{
					GlobalMessages.addErrorMessage(model, "text.cliq.cash.payment.wallet.disable.label");
				}

				else
				{
					
					 * GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					 * "text.cliqcash.use.wallet.fail", null);
					 
					setValidErrorCodeHandling(customerRegisterResponse.getResponseCode().intValue(), redirectAttributes);
					System.out.println("Fail To active user wallet" + (null != customerRegisterResponse.getResponseMessage()
							? customerRegisterResponse.getResponseMessage() : "QC Not Responding"));
					LOG.error("Fail To active user wallet" + (null != customerRegisterResponse.getResponseMessage()
							? customerRegisterResponse.getResponseMessage() : "QC Not Responding"));
				}*/
			}
			boolean checkUserWalletStatus = true;
			if (currentCustomer.getIsWalletActivated() != null)
			{
				if (currentCustomer.getIsqcOtpVerify() != null && currentCustomer.getIsqcOtpVerify().booleanValue())
				{
					checkUserWalletStatus=true;
				}
				else
				{
			   	checkUserWalletStatus=false;
				}
			}
			else
			{
				checkUserWalletStatus=false;
			}

			model.addAttribute("isCustomerWalletActive", checkUserWalletStatus);
			
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			//			GlobalMessages.addErrorMessage(model, "text.cliqcash.add.money.Fail");
			/*
			 * if(ex.getMessage().contains("SocketTimeoutException")){ GlobalMessages.addErrorMessage(model,
			 * "text.cliqcash.timeout.massege"); }
			 */
			final ContentPageModel contentPage = getContentPageForLabelOrId("cliqcashPage");
			storeCmsPageInModel(model, contentPage);
			setUpMetaDataForContentPage(model, contentPage);
			model.addAttribute("WalletBalance", balanceAmount);
			model.addAttribute("walletTrasacationsListData", walletTrasacationsListData1.getWalletTransactions());

			return "addon:/marketplacecheckoutaddon/pages/checkout/single/cliqcash";
		}
		final ContentPageModel contentPage = getContentPageForLabelOrId("cliqcashPage");
		storeCmsPageInModel(model, contentPage);
		setUpMetaDataForContentPage(model, contentPage);
		model.addAttribute("WalletBalance", balanceAmount);
		model.addAttribute("walletTrasacationsListData", walletTrasacationsListData1.getWalletTransactions());
		model.addAttribute("dateFormat", dateFormat);
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/cliqcash";
	}

	private void setValidErrorCodeHandling(final int errorCode, final RedirectAttributes redirectAttributes)
	{
		if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10004).intValue())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.ERROR_CODE_10004_DESC, null);
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10027).intValue())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.ERROR_CODE_10027_DESC, null);
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10528).intValue())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.ERROR_CODE_10528_DESC, null);
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10086).intValue())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.ERROR_CODE_10086_DESC, null);
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10096).intValue())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.ERROR_CODE_10096_DESC, null);
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10550).intValue())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.ERROR_CODE_10550_DESC, null);
		}
		else
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER, "text.cliqcash.add.money.Fail",
					null);
		}
	}




	/* Add Cliq cash amount from Email */
	@RequestMapping(value = REDIM_WALLET_FROM_EMAIL, method = RequestMethod.GET)
	@RequireHardLogIn
	public String getRedimWalletForEmail(
			@RequestParam(value = ModelAttributetConstants.ORDERCODE, required = false) final String orderCode, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, Exception
	{
		try
		{
			final OrderModel orderModel = orderModelService.getOrderModel(orderCode);
			final String recipientId = orderModel.getRecipientId();
			CustomerModel walletCustomer = null;
			try
			{
				walletCustomer = extendedUserService.getUserForOriginalUid(recipientId);
			}
			catch (final Exception exception)
			{
				LOG.error("Exception occur while adding money for customer wallet" + exception.getMessage());
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"mpl.gift.card.add.error.message.newuser");
				return REDIRECT_PREFIX + "/login";
			}
			if (walletCustomer == null)
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"mpl.gift.card.add.error.message.newuser");
				return REDIRECT_PREFIX + "/login";
			}

			
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			if (!currentCustomer.getOriginalUid().equalsIgnoreCase(walletCustomer.getOriginalUid()))
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"mpl.gift.card.add.error.message.anotheruser");
				return REDIRECT_PREFIX + "/login";
			}
			
			if (currentCustomer.getIsWalletActivated() == null || !currentCustomer.getIsWalletActivated().booleanValue())
			{
				return REDIRECT_PREFIX + "/wallet/getcliqcashPage";
			}
			
			final String cardNumber = orderModel.getEntries().get(0).getWalletApportionPaymentInfo().getWalletCardList().get(0)
					.getCardNumber();
			final String cardPin = orderModel.getEntries().get(0).getWalletApportionPaymentInfo().getWalletCardList().get(0)
					.getCardPinNumber();

			final RedimGiftCardResponse response = mplWalletFacade.getAddEGVToWallet(cardNumber, cardPin);
			if (response != null && response.getResponseCode() != null)
			{
				LOG.info("Code Response" + response.getResponseMessage() + response.getResponseCode().intValue());
			}
			if (null != response && null != response.getResponseCode() && null == Integer.valueOf(0))
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
						"text.cliqcash.add.money.success", null);
				LOG.info("card Added Sucesss " + response.getResponseMessage());
				return REDIRECT_PREFIX + "/wallet/getcliqcashPage";
			}
			else if (null != response && null != response.getResponseCode() && response.getResponseCode() != Integer.valueOf(0))
			{
				setValidErrorCodeHandling(response.getResponseCode().intValue(), redirectAttributes);
				LOG.error("card Add Error " + response.getResponseMessage());
				return REDIRECT_PREFIX + "/wallet/getcliqcashPage";
			}
		}
		catch (final Exception ex)
		{
			GlobalMessages.addMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER, "text.cliqcash.add.money.Fail",
					null);
			ex.printStackTrace();
		}
		return REDIRECT_PREFIX + "/wallet/getcliqcashPage";
	}

	@RequestMapping(value = WALLET_CREATE_OTP_POPUP, method = RequestMethod.GET)
	public String getWalletCreateForm(final Model model)
	{
		final WalletCreateForm walletForm = new WalletCreateForm();
		final WalletCreateData walletCreateData = mplWalletFacade.getWalletCreateData();
		populateWalateCreateData(walletForm, walletCreateData);
		model.addAttribute("walletForm", walletForm);
		return "pages/account/walletCreateOtpPopup";
	}



	@RequestMapping(value = WALLET_CREATE_OTP_GENERATE, method = RequestMethod.POST)
	@ResponseBody
	public String createOTP(@RequestParam(value = "mobileNumber") final String mobileNumber)
	{
		String isNewOTPCreated;
		LOG.debug("Create  OTP For QC Verifaction");
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		final boolean isUsed = registerCustomerFacade.checkUniquenessOfMobileForWallet(mobileNumber);
		if (!isUsed)
		{
			return "isUsed";
		}
		isNewOTPCreated = mplWalletFacade.generateOTP(currentCustomer, mobileNumber);
		return isNewOTPCreated;
	}


	@ResponseBody
	@RequestMapping(value = WALLET_CREATE_VALIDATE_OTP_URL, method =
	{ RequestMethod.POST, RequestMethod.GET })
	public String getVerificationOTP(@ModelAttribute("walletForm") final WalletCreateForm walletForm, final Model model)
			throws CMSItemNotFoundException, UnsupportedEncodingException

	{
		LOG.info("OTP Verification ");
		if (StringUtils.isNotEmpty(walletForm.getOtpNumber()))
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final OTPResponseData response = mplWalletFacade.validateOTP(currentCustomer.getUid(), walletForm.getOtpNumber());
			//OTP Validation Check
			if (response.getOTPValid().booleanValue())
			{
				final boolean isUsed = registerCustomerFacade.checkUniquenessOfMobileForWallet(walletForm.getQcVerifyMobileNo());
				if (!isUsed)
				{
					return "isUsed";
				}
				return getNewCustomerWallet(currentCustomer, walletForm.getQcVerifyFirstName(), walletForm.getQcVerifyLastName(),
						walletForm.getQcVerifyMobileNo());
			}
			else
			{
				return "OTPERROR";
			}
		}
		else
		{
			return "OTPERROR";
		}
	}
	/**
	 * @param walletForm
	 * @param walletCreateData
	 */
	private void populateWalateCreateData(final WalletCreateForm walletForm, final WalletCreateData walletCreateData)
	{
		walletForm.setQcVerifyFirstName(walletCreateData.getQcVerifyFirstName());
		walletForm.setQcVerifyLastName(walletCreateData.getQcVerifyLastName());
		walletForm.setQcVerifyMobileNo(walletCreateData.getQcVerifyMobileNo());
	}




	public String getNewCustomerWallet(final CustomerModel currentCustomer, final String firstName, final String lastName,
			final String mobileNumber)
	{
		if (currentCustomer.getIsWalletActivated() != null && currentCustomer.getIsWalletActivated().booleanValue())
		{
			final CustomerWalletDetailResponse customerUpdateResponse = mplWalletFacade.editWalletInformtion(currentCustomer,
					firstName, lastName, mobileNumber);
			if (null != customerUpdateResponse.getResponseCode() && customerUpdateResponse.getResponseCode() == Integer.valueOf(0))
			{
				currentCustomer.setIsqcOtpVerify(Boolean.valueOf(true));
				currentCustomer.setQcVerifyFirstName(firstName);
				currentCustomer.setQcVerifyLastName(lastName);
				currentCustomer.setQcVerifyMobileNo(mobileNumber);
				currentCustomer.setFirstName(firstName);
				currentCustomer.setLastName(lastName);
				currentCustomer.setMobileNumber(mobileNumber);
				modelService.save(currentCustomer);
				return "success";
			}
			else
			{
				return "qcDown";
			}
		}
		else
		{

			final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
			final Customer custInfo = new Customer();
			custInfo.setEmail(currentCustomer.getOriginalUid());
			custInfo.setEmployeeID(currentCustomer.getUid());
			custInfo.setCorporateName("Tata Unistore Ltd");
			if (null != firstName)
			{
				custInfo.setFirstname(firstName);
			}
			if (null != lastName)
			{
				custInfo.setLastName(lastName);
			}
			if (null != mobileNumber)
			{
				custInfo.setPhoneNumber(mobileNumber);
			}

			customerRegisterReq.setExternalwalletid(currentCustomer.getUid());
			customerRegisterReq.setCustomer(custInfo);
			customerRegisterReq.setNotes("Activating Customer " + currentCustomer.getUid());
			final QCCustomerRegisterResponse customerRegisterResponse = mplWalletFacade.createWalletContainer(customerRegisterReq);
			if (null != customerRegisterResponse.getResponseCode() && customerRegisterResponse.getResponseCode() == 0)
			{
				final CustomerWalletDetailModel custWalletDetail = modelService.create(CustomerWalletDetailModel.class);
				custWalletDetail.setWalletId(customerRegisterResponse.getWallet().getWalletNumber());
				custWalletDetail.setWalletState(customerRegisterResponse.getWallet().getStatus());
				custWalletDetail.setCustomer(currentCustomer);
				custWalletDetail.setServiceProvider("Tata Unistore Ltd");
				modelService.save(custWalletDetail);
				currentCustomer.setCustomerWalletDetail(custWalletDetail);
				currentCustomer.setIsWalletActivated(true);
				currentCustomer.setQcVerifyFirstName(firstName);
				currentCustomer.setQcVerifyLastName(lastName);
				currentCustomer.setIsqcOtpVerify(true);
				currentCustomer.setQcVerifyMobileNo(mobileNumber);
				currentCustomer.setFirstName(firstName);
				currentCustomer.setLastName(lastName);
				currentCustomer.setMobileNumber(mobileNumber);
				modelService.save(currentCustomer);
				return "success";
			}
		}
		return "qcDown";
	}

}


