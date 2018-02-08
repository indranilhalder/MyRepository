/**
 *
 */
package com.tisl.mpl.facades.payment;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import junit.framework.Assert;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.payment.impl.MplPaymentFacadeImpl;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.constants.MarketplaceJuspayServicesConstants;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.request.InitOrderRequest;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.juspay.response.InitOrderResponse;
import com.tisl.mpl.marketplacecommerceservices.service.BlacklistService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.sms.facades.SendSMSFacade;


/**
 * @author TCS
 *
 */
public class MplPaymentFacadeImplUnitTest
{
	private static final Logger LOG = Logger.getLogger(MplPaymentFacadeImplUnitTest.class);

	private MplPaymentFacadeImpl mplPaymentFacadeImpl;
	private MplPaymentService mplPaymentService;
	private OTPGenericService otpGenericService;
	private BlacklistService blacklistService;
	private PaymentTypeModel paymentTypeModel;
	private ModelService modelService;
	private CartModel cartModel;
	private OrderModel orderModel;
	private AddressModel addressModel;
	private ConfigurationService configService;
	private Configuration configuration;
	private SendSMSFacade sendSmsFacade;
	private AbstractOrderModel abstractOrderModel;
	private CustomerModel customerModel;
	//private PK pk;
	private UserService userService;
	private SessionService sessionService;
	private CartService cartService;
	private AbstractOrderEntryModel abstractOrderEntryModel;
	private CartData cartData;
	private OrderData orderData;
	private MplPromoPriceData mplPromoPriceData;
	private OrderEntryData orderEntryData;
	private DeliveryModeData mplDelvModeData;
	private GetOrderStatusRequest orderStatusRequest;
	private GetOrderStatusResponse orderStatusResponse;
	private PaymentService juspayService;
	private InitOrderRequest initOrderRequest;
	private InitOrderResponse initOrderResponse;
	private PointOfServiceModel posModel;
	private MplPriceRowService mplPriceRowService;
	private MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel;
	private DeliveryModeModel deliveryModeModel;
	private MplCommerceCartService mplCommerceCartService;
	private PromotionResultData promotionResultData;
	private HttpsURLConnection httpsURLConnection;
	//private URL url;
	private OutputStream outputStream;
	private InputStream inputStream;

	//private FlexibleSearchService flexibleSearchService;

	//private BankforNetbankingModel bankForNetbankingModel;
	//private BankModel bank;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.mplPaymentFacadeImpl = new MplPaymentFacadeImpl();
		this.mplPaymentService = Mockito.mock(MplPaymentService.class);
		this.mplPaymentFacadeImpl.setMplPaymentService(mplPaymentService);
		this.otpGenericService = Mockito.mock(OTPGenericService.class);
		this.mplPaymentFacadeImpl.setOtpGenericService(otpGenericService);
		this.blacklistService = Mockito.mock(BlacklistService.class);
		this.mplPaymentFacadeImpl.setBlacklistService(blacklistService);
		this.paymentTypeModel = Mockito.mock(PaymentTypeModel.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.mplPaymentFacadeImpl.setModelService(modelService);
		this.cartModel = Mockito.mock(CartModel.class);
		this.orderModel = Mockito.mock(OrderModel.class);
		this.addressModel = Mockito.mock(AddressModel.class);
		this.configService = Mockito.mock(ConfigurationService.class);
		this.mplPaymentFacadeImpl.setConfigurationService(configService);
		this.configuration = Mockito.mock(Configuration.class);
		this.sendSmsFacade = Mockito.mock(SendSMSFacade.class);
		this.mplPaymentFacadeImpl.setSendSMSFacade(sendSmsFacade);
		this.abstractOrderModel = Mockito.mock(AbstractOrderModel.class);
		this.customerModel = Mockito.mock(CustomerModel.class);
		//this.pk = Mockito.mock(PK.class);
		this.userService = Mockito.mock(UserService.class);
		this.mplPaymentFacadeImpl.setUserService(userService);
		this.sessionService = Mockito.mock(SessionService.class);
		this.mplPaymentFacadeImpl.setSessionService(sessionService);
		this.cartService = Mockito.mock(CartService.class);
		this.mplPaymentFacadeImpl.setCartService(cartService);
		this.abstractOrderEntryModel = Mockito.mock(AbstractOrderEntryModel.class);
		this.cartData = Mockito.mock(CartData.class);
		this.orderData = Mockito.mock(OrderData.class);
		this.mplPromoPriceData = Mockito.mock(MplPromoPriceData.class);
		this.orderEntryData = Mockito.mock(OrderEntryData.class);
		this.mplDelvModeData = Mockito.mock(DeliveryModeData.class);
		this.orderStatusRequest = Mockito.mock(GetOrderStatusRequest.class);
		this.orderStatusResponse = Mockito.mock(GetOrderStatusResponse.class);
		this.juspayService = Mockito.mock(PaymentService.class);
		this.initOrderRequest = Mockito.mock(InitOrderRequest.class);
		this.initOrderResponse = Mockito.mock(InitOrderResponse.class);
		this.posModel = Mockito.mock(PointOfServiceModel.class);
		this.mplPriceRowService = Mockito.mock(MplPriceRowService.class);
		this.mplZoneDeliveryModeValueModel = Mockito.mock(MplZoneDeliveryModeValueModel.class);
		this.deliveryModeModel = Mockito.mock(DeliveryModeModel.class);
		this.mplCommerceCartService = Mockito.mock(MplCommerceCartService.class);
		this.mplPaymentFacadeImpl.setMplCommerceCartService(mplCommerceCartService);
		this.promotionResultData = Mockito.mock(PromotionResultData.class);
		this.httpsURLConnection = Mockito.mock(HttpsURLConnection.class);
		//this.url = Mockito.mock(URL.class);
		this.outputStream = Mockito.mock(OutputStream.class);
		this.inputStream = Mockito.mock(InputStream.class);
		//this.flexibleSearchService = Mockito.mock(FlexibleSearchService.class);
		//this.bankForNetbankingModel = Mockito.mock(BankforNetbankingModel.class);
		//this.bank = Mockito.mock(BankModel.class);
	}

	//	@Test
	//	public void testGetPaymentModes()
	//	{
	//		final List<PaymentTypeModel> paymentTypeList = Arrays.asList(paymentType);
	//		Mockito.when(mplPaymentService.getPaymentModes("mpl")).thenReturn(paymentTypeList);
	//		Mockito.when(paymentType.getMode()).thenReturn("COD");
	//		Mockito.when(paymentType.getIsAvailable()).thenReturn(Boolean.TRUE);
	//		mplPaymentFacadeImpl.getPaymentModes("mpl", false, null);
	//	}


	//	@Test
	//	public void testGenerateOTPForCODWeb() throws InvalidKeyException, NoSuchAlgorithmException
	//	{
	//		Mockito.doNothing().when(otpGenericService).generateOTP("1234567890", "COD");
	//		mplPaymentFacadeImpl.generateOTPforCODWeb("1234567890");
	//	}


	//	@Test
	//	public void testValidateOTPForCODWeb()
	//	{
	//		Mockito.when(Boolean.valueOf(otpGenericService.validateOTP("1123456789", "123456", OTPTypeEnum.COD, 600000))).thenReturn(
	//				Boolean.TRUE);
	//		mplPaymentFacadeImpl.validateOTPforCODWeb("1123456789", "123456");
	//	}


	//	@Test
	//	public void testIsBlacklisted()
	//	{
	//		Mockito.when(Boolean.valueOf(blacklistService.blacklistedOrNot("1123456789"))).thenReturn(Boolean.TRUE);
	//
	//		mplPaymentFacadeImpl.isBlackListed("1123456789", cartModel);
	//	}

	//	@Test
	//	public void testSaveCODPaymentInfo()
	//	{
	//		Mockito.when((CustomerModel) userService.getCurrentUser()).thenReturn(customerModel);
	//		Mockito.when(customerModel.getName()).thenReturn("Test Name");
	//		Mockito.doNothing().when(mplPaymentService).saveCODPaymentInfo("Test Name");
	//		mplPaymentFacadeImpl.saveCODPaymentInfo();
	//	}

	@Test
	public void testSaveCart()
	{
		Mockito.doNothing().when(modelService).save(cartModel);
		mplPaymentFacadeImpl.saveCart(cartModel);
	}

	//	@Test
	//	public void testGetBanksByPriority()
	//	{
	//		final List<BankforNetbankingModel> bankForNetbankingList = Arrays.asList(bankForNetbankingModel);
	//		Mockito.when(mplPaymentService.getBanksByPriority()).thenReturn(bankForNetbankingList);
	//		Mockito.when(bankForNetbankingModel.getName()).thenReturn(bank);
	//		Mockito.when(bank.getBankName()).thenReturn("HDFC Bank");
	//		Mockito.when(bankForNetbankingModel.getNbCode()).thenReturn("HDFB");
	//		//mplPaymentFacadeImpl.getBanksByPriority();
	//	}

	//	@Test
	//	public void testGetEMIBankNames()
	//	{
	//		final List<String> emiBankList = new ArrayList<String>();
	//		Mockito.when(mplPaymentService.getEMIBanks(Double.valueOf(1500.00))).thenReturn(emiBankList);
	//		mplPaymentFacadeImpl.getEMIBankNames(Double.valueOf(1500.00));
	//	}


	//	@Test
	//	public void testGetBankTerms()
	//	{
	//		//TISSEC-50
	//		final List<EMITermRateData> emiTermRateList = new ArrayList<EMITermRateData>();
	//		Mockito.when(mplPaymentService.getBankTerms("", Double.valueOf(1500.00))).thenReturn(emiTermRateList);//TODO : Please enter bank
	//		mplPaymentFacadeImpl.getBankTerms("", Double.valueOf(1500.00));//TODO : Please enter bank
	//	}


	//	@Test
	//	public void testGetOtherBanks()
	//	{
	//		final List<BankforNetbankingModel> bankForNetbankingList = Arrays.asList(bankForNetbankingModel);
	//		Mockito.when(mplPaymentService.getOtherBanks()).thenReturn(bankForNetbankingList);
	//		Mockito.when(bankForNetbankingModel.getName()).thenReturn(bank);
	//		Mockito.when(bank.getBankName()).thenReturn("HDFC Bank");
	//		Mockito.when(bankForNetbankingModel.getNbCode()).thenReturn("HDFB");
	//		//mplPaymentFacadeImpl.getBanksByPriority();
	//	}



	@SuppressWarnings("deprecation")
	@Test
	public void testGenerateOTPforCOD() throws InvalidKeyException, NoSuchAlgorithmException
	{
		final String otp = "223913";
		Mockito.when(otpGenericService.generateOTP("sumit.haldar@tcs.com", "COD", "9836927004")).thenReturn(otp);
		Mockito.when(orderModel.getDeliveryAddress()).thenReturn(addressModel);
		Mockito.when(addressModel.getPhone1()).thenReturn("9681637838");
		addressModel.setPhone1("9836927004");
		Mockito.when(addressModel.getCellphone()).thenReturn("9681637838");
		addressModel.setCellphone("9836927004");
		Mockito.doNothing().when(modelService).save(addressModel);
		Mockito.doNothing().when(modelService).save(orderModel);
		Mockito.when(configService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO)).thenReturn("123456");
		Mockito.doNothing().when(sendSmsFacade)
				.sendSms(MarketplacecommerceservicesConstants.SMS_SENDER_ID, "Test Content", "9836927004");

		final String actualOtp = mplPaymentFacadeImpl.generateOTPforCOD("sumit.haldar@tcs.com", "9836927004", "Sumit", orderModel);

		Assert.assertEquals(actualOtp, otp);
	}



	//TODO: Uncomment after pk mocking is solved
	@SuppressWarnings("deprecation")
	@Test
	public void testIsBlackListed()
	{
		final String query = "Select {o:pk} from {Order as o} where {o.code}='100000001'";

		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		final OrderModel orderModel = (OrderModel) flexibleSearchService.search(query).getResult().get(0);
		junit.framework.Assert.assertNotNull(orderModel);

		final CustomerModel user = (CustomerModel) orderModel.getUser();
		Mockito.when(abstractOrderModel.getUser()).thenReturn(user);
		Mockito.when(customerModel.getPk()).thenReturn(user.getPk()); //TODO:Cannot mock final class
		//Mockito.when(pk.toString()).thenReturn(user.getPk());
		Mockito.when(customerModel.getOriginalUid()).thenReturn(user.getOriginalUid());
		Mockito.when(abstractOrderModel.getDeliveryAddress()).thenReturn(addressModel);
		Mockito.when(addressModel.getPhone1()).thenReturn("9681637838");
		Mockito.when(addressModel.getCellphone()).thenReturn("9681637838");
		Mockito.when(mplPaymentFacadeImpl.fetchPhoneNumber(abstractOrderModel)).thenReturn("9836927004");
		Mockito.when(
				blacklistService.getBlacklistedCustomerforCOD("9987869524", user.getOriginalUid(), "9836927004", "10.10.64.33"))
				.thenReturn(Boolean.TRUE);
		final boolean isCustomerBlacklisted = blacklistService.getBlacklistedCustomerforCOD("9987869524", user.getOriginalUid(),
				"9836927004", "10.10.64.33").booleanValue();

		//abstractOrderModel.setUser(customerModel);
		final boolean actual = mplPaymentFacadeImpl.isBlackListed("10.10.64.33", orderModel);

		Assert.assertEquals(isCustomerBlacklisted, actual);
	}

	@Test
	public void testSaveCODPaymentInfo()
	{
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		final Map<String, Double> paymentMode = new HashMap<String, Double>();
		Mockito.when(sessionService.getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODE)).thenReturn(paymentMode);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		final List<AbstractOrderEntryModel> entryList = Arrays.asList(abstractOrderEntryModel);
		Mockito.when(cartModel.getEntries()).thenReturn(entryList);
		Mockito.doNothing().when(mplPaymentService).setPaymentTransactionForCOD(cartModel);
		Mockito.when(customerModel.getName()).thenReturn("Test Name");
		final String custName = customerModel.getName();
		final double cartValue = 999.00;
		final double convCharge = 50.00;
		Mockito.doNothing().when(mplPaymentService)
				.saveCODPaymentInfo(custName, Double.valueOf(cartValue), Double.valueOf(convCharge), entryList, abstractOrderModel);
		Mockito.doNothing().when(mplPaymentService).paymentModeApportion(cartModel);

		try
		{
			mplPaymentFacadeImpl.saveCODPaymentInfo(Double.valueOf(cartValue), Double.valueOf(convCharge), orderModel);
		}
		catch (final Exception e)
		{
			LOG.error("Exception");
		}


	}




	@SuppressWarnings("deprecation")
	@Test
	public void testFetchPhoneNumber()
	{
		Mockito.when(abstractOrderModel.getDeliveryAddress()).thenReturn(addressModel);
		Mockito.when(abstractOrderModel.getDeliveryAddress().getPhone1()).thenReturn("9999999999");
		final String phoneNo = abstractOrderModel.getDeliveryAddress().getPhone1();

		final String actualPhoneNo = mplPaymentFacadeImpl.fetchPhoneNumber(abstractOrderModel);

		Assert.assertEquals(phoneNo, actualPhoneNo);
	}




	@SuppressWarnings("deprecation")
	@Test
	public void testApplyPromotion() throws ModelSavingException, NumberFormatException, JaloInvalidParameterException,
			VoucherOperationException, CalculationException, JaloSecurityException, JaloPriceFactoryException,
			EtailNonBusinessExceptions
	{
		Mockito.when(mplPaymentService.applyPromotions(cartData, orderData, cartModel, orderModel, mplPromoPriceData)).thenReturn(
				mplPromoPriceData);
		final MplPromoPriceData pricaData = mplPaymentService.applyPromotions(cartData, orderData, cartModel, orderModel,
				mplPromoPriceData);

		final MplPromoPriceData actualData = mplPaymentFacadeImpl.applyPromotions(cartData, orderData, cartModel, orderModel,
				mplPromoPriceData);

		Assert.assertEquals(pricaData, actualData);
	}



	@SuppressWarnings("deprecation")
	@Test
	public void testGetOrderByGuid()
	{
		final String guid = "658464687dcwdcb-webfwkb5w84c6-qhbb";
		Mockito.when(mplPaymentService.fetchOrderOnGUID(guid)).thenReturn(orderModel);
		final OrderModel order = mplPaymentService.fetchOrderOnGUID("658464687dcwdcb-webfwkb5w84c6-qhbb");

		final OrderModel actualOrder = mplPaymentFacadeImpl.getOrderByGuid(guid);

		Assert.assertEquals(order, actualOrder);
	}



	@SuppressWarnings("deprecation")
	@Test
	public void testGetPaymentModes()
	{
		final Map<String, Boolean> data = new HashMap<String, Boolean>();
		final List<PaymentTypeModel> paymentTypeList = Arrays.asList(paymentTypeModel);
		Mockito.when(mplPaymentService.getPaymentModes("mpl")).thenReturn(paymentTypeList);
		final List<OrderEntryData> entryList = Arrays.asList(orderEntryData);
		Mockito.when(orderData.getEntries()).thenReturn(entryList);
		Mockito.when(orderEntryData.getDeliveryMode()).thenReturn(mplDelvModeData);
		Mockito.when(mplDelvModeData.getCode()).thenReturn("express-delivery");
		Mockito.when(paymentTypeModel.getMode()).thenReturn("Credit Card");
		Mockito.when(paymentTypeModel.getIsAvailable()).thenReturn(Boolean.TRUE);

		data.put(paymentTypeModel.getMode(), paymentTypeModel.getIsAvailable());

		final Map<String, Boolean> actualData = mplPaymentFacadeImpl.getPaymentModes("mpl", orderData, false);

		Assert.assertEquals(data, actualData);

	}



	@SuppressWarnings("deprecation")
	@Test
	public void testGetOrderStatusFromJuspay()
	{
		Mockito.when(configService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.JUSPAYBASEURL)).thenReturn(
				"https://test.juspay.in");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)).thenReturn(
				"6D779CFC5726ARDYFC345A3EBCEC");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID)).thenReturn("testID");
		final Map<String, Double> paymentMode = new HashMap<String, Double>();
		Mockito.when(sessionService.getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODE)).thenReturn(paymentMode);
		final String juspayOrderId = "111111111";
		Mockito.when(sessionService.getAttribute(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID)).thenReturn(juspayOrderId);
		final String guid = "7621348923ygdtyfcu-HJVghfchgvc";
		Mockito.when(mplPaymentService.getAuditId(guid)).thenReturn(juspayOrderId);
		Mockito.when(juspayService.getOrderStatus(orderStatusRequest)).thenReturn(orderStatusResponse);
		Mockito
				.when(Boolean.valueOf(mplPaymentService.updateAuditEntry(orderStatusResponse, orderStatusRequest, orderModel,
						paymentMode))).thenReturn(Boolean.TRUE);
		final double totalPrice = 999.00;
		Mockito.when(orderModel.getTotalPrice()).thenReturn(Double.valueOf(totalPrice));
		Mockito.when(orderStatusResponse.getAmount()).thenReturn(Double.valueOf(totalPrice));
		Mockito.doNothing().when(mplPaymentService).setPaymentTransaction(orderStatusResponse, paymentMode, orderModel);
		Mockito.when(orderStatusResponse.getStatus()).thenReturn("CHARGED");
		Mockito.doNothing().when(mplPaymentService).saveCardDetailsFromJuspay(orderStatusResponse, paymentMode, orderModel);
		Mockito.doNothing().when(mplPaymentService).paymentModeApportion(orderModel);
		Mockito.doNothing().when(sessionService).removeAttribute(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID);
		//final String orderStatus = orderStatusResponse.getStatus();

		//TODO: issue with makeServiceCall
		//		final String actualOrderStatus = mplPaymentFacadeImpl
		//				.getOrderStatusFromJuspay(guid, paymentMode, orderModel, juspayOrderId);
		//
		//		Assert.assertEquals(orderStatus, actualOrderStatus);

	}




	@SuppressWarnings("deprecation")
	@Test
	public void testCreateJuspayOrder() throws IOException
	{
		Mockito.when(configService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.JUSPAYBASEURL)).thenReturn(
				"https://test.juspay.in");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)).thenReturn(
				"6D779CFC5726ARDYFC345A3EBCEC");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID)).thenReturn("testID");

		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.PROXYENABLED)).thenReturn("true");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.GENPROXY)).thenReturn("proxy.tcs.com");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.GENPROXYPORT)).thenReturn("10.10.12.10");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.PROXYENABLED)).thenReturn("true");
		//final String proxyName = configuration.getString(MarketplacecommerceservicesConstants.GENPROXY);
		//final String proxyPort = configuration.getString(MarketplacecommerceservicesConstants.GENPROXYPORT);
		//final SocketAddress addr = new InetSocketAddress(proxyName, Integer.parseInt(proxyPort));
		//final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
		//Mockito.when(url.openConnection(proxy)).thenReturn(httpsURLConnection);
		Mockito.when(configuration.getString(MarketplaceJuspayServicesConstants.VERSION)).thenReturn("testV1");
		Mockito.when(httpsURLConnection.getOutputStream()).thenReturn(outputStream);
		Mockito.when(httpsURLConnection.getInputStream()).thenReturn(inputStream);

		final String guid = "7621348923ygdtyfcu-HJVghfchgvc";
		Mockito.when(cartModel.getGuid()).thenReturn(guid);
		Mockito.when(orderModel.getGuid()).thenReturn(guid);
		final String uid = "100000000";
		Mockito.when(mplPaymentService.getCustomer(uid)).thenReturn(customerModel);
		Mockito.when(cartModel.getDeliveryAddress()).thenReturn(addressModel);
		Mockito.when(orderModel.getDeliveryAddress()).thenReturn(addressModel);
		Mockito.when(customerModel.getDefaultShipmentAddress()).thenReturn(addressModel);
		Mockito.when(addressModel.getPhone1()).thenReturn("9999999999");
		Mockito.when(addressModel.getPhone2()).thenReturn("9999999999");
		Mockito.when(customerModel.getOriginalUid()).thenReturn("test@tcs.com");
		final String juspayOrderId = "111111111";
		final String channel = "WEB";
		Mockito.when(mplPaymentService.createPaymentId()).thenReturn(juspayOrderId);
		Mockito.when(Boolean.valueOf(mplPaymentService.createEntryInAudit(juspayOrderId, channel, guid))).thenReturn(Boolean.TRUE);

		Mockito.when(sessionService.getAttribute(MarketplacecommerceservicesConstants.EBS_SESSION_ID)).thenReturn("testid");
		Mockito.doNothing().when(sessionService).removeAttribute(MarketplacecommerceservicesConstants.EBS_SESSION_ID);

		final double totalPrice = 900.00;
		Mockito.when(cartModel.getTotalPrice()).thenReturn(Double.valueOf(totalPrice));
		Mockito.when(orderModel.getTotalPrice()).thenReturn(Double.valueOf(totalPrice));

		Mockito.when(juspayService.initOrder(initOrderRequest)).thenReturn(initOrderResponse);
		Mockito.when(initOrderResponse.getOrderId()).thenReturn(juspayOrderId);
		final String retJuspayOrderId = initOrderResponse.getOrderId();


		Mockito.when(initOrderResponse.getStatus()).thenReturn("NEW");
		Mockito.doNothing().when(sessionService)
				.setAttribute(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID, retJuspayOrderId);

		Mockito.when(Boolean.valueOf(mplPaymentService.createEntryInAudit(retJuspayOrderId, channel, guid))).thenReturn(
				Boolean.TRUE);

		//TODO: issue with makeServiceCall
		//final String actualId = mplPaymentFacadeImpl.createJuspayOrder(cartModel, orderModel, "Test", "Test", "Test", "Test",
		//		"Test", "India", "WB", "Test", "111111", "true|true", "testurl", uid, channel);

		//Assert.assertEquals(retJuspayOrderId, actualId);
	}



	@Test
	public void testPopulateDeliveryPointOfServ()
	{
		final List<AbstractOrderEntryModel> entryList = Arrays.asList(abstractOrderEntryModel);
		Mockito.when(abstractOrderModel.getEntries()).thenReturn(entryList);
		Mockito.when(abstractOrderEntryModel.getGiveAway()).thenReturn(Boolean.TRUE);
		final List<String> associatedEntryList = Arrays.asList("1111111111");
		Mockito.when(abstractOrderEntryModel.getAssociatedItems()).thenReturn(associatedEntryList);
		Mockito.when(abstractOrderEntryModel.getSelectedUSSID()).thenReturn("1111111111");
		Mockito.when(abstractOrderEntryModel.getDeliveryPointOfService()).thenReturn(posModel);
		Mockito.doNothing().when(abstractOrderEntryModel).setDeliveryPointOfService(posModel);
		Mockito.doNothing().when(modelService).save(abstractOrderEntryModel);
		Mockito.when(abstractOrderEntryModel.getQuantity()).thenReturn(Long.valueOf(1L));
		Mockito.when(abstractOrderEntryModel.getMplDeliveryMode()).thenReturn(mplZoneDeliveryModeValueModel);
		Mockito.when(mplZoneDeliveryModeValueModel.getDeliveryMode()).thenReturn(deliveryModeModel);
		Mockito.when(deliveryModeModel.getCode()).thenReturn("home-delivery");


		mplPaymentFacadeImpl.populateDeliveryPointOfServ(abstractOrderModel);

	}


	@Test
	public void testPopulateDelvPOSForFreebie()
	{
		final List<AbstractOrderEntryModel> entryList = Arrays.asList(abstractOrderEntryModel);
		Mockito.when(abstractOrderModel.getEntries()).thenReturn(entryList);
		Mockito.when(abstractOrderEntryModel.getGiveAway()).thenReturn(Boolean.TRUE);
		final List<String> associatedEntryList = Arrays.asList("1111111111");
		Mockito.when(abstractOrderEntryModel.getAssociatedItems()).thenReturn(associatedEntryList);
		final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
		final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
		Mockito.doNothing().when(mplCommerceCartService)
				.saveDeliveryMethForFreebie(abstractOrderModel, freebieModelMap, freebieParentQtyMap);
		Mockito.when(abstractOrderEntryModel.getSelectedUSSID()).thenReturn("1111111111");
		Mockito.when(abstractOrderEntryModel.getDeliveryPointOfService()).thenReturn(posModel);
		Mockito.doNothing().when(abstractOrderEntryModel).setDeliveryPointOfService(posModel);
		Mockito.doNothing().when(modelService).save(abstractOrderEntryModel);
		Mockito.when(abstractOrderEntryModel.getQuantity()).thenReturn(Long.valueOf(1L));
		Mockito.when(abstractOrderEntryModel.getMplDeliveryMode()).thenReturn(mplZoneDeliveryModeValueModel);
		Mockito.when(mplZoneDeliveryModeValueModel.getDeliveryMode()).thenReturn(deliveryModeModel);
		Mockito.when(deliveryModeModel.getCode()).thenReturn("home-delivery");

		mplPaymentFacadeImpl.populateDelvPOSForFreebie(abstractOrderModel, freebieModelMap, freebieParentQtyMap);
	}



	@SuppressWarnings("deprecation")
	@Test
	public void testCalculateTotalDiscount()
	{
		final List<PromotionResultData> promoResultList = Arrays.asList(promotionResultData);
		Mockito.when(promotionResultData.getDescription()).thenReturn("Test description with 10 off");

		final double actual = mplPaymentFacadeImpl.calculateTotalDiscount(promoResultList);

		Assert.assertEquals(10, actual, 1);
	}

}
