/**
 *
 */
package com.tisl.mpl.services;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.EMIPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.NetbankingPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.configuration.Configuration;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.EMITermRowModel;
import com.tisl.mpl.core.model.JuspayEBSResponseDataModel;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.SavedCardModel;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.CardResponse;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.juspay.response.RiskResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPaymentDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplFraudModelService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentTransactionService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplPaymentServiceImpl;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "unused", "deprecation" })
@JsonAutoDetect
public class MplPaymentServiceImplUnitTest
{
	private MplPaymentServiceImpl mplPaymentServiceImpl;
	private MplPaymentDao mplPaymentDao;
	private PaymentTypeModel paymentType;
	private BankforNetbankingModel bankForNB;

	private EMIBankModel emiBankModel;
	private BankModel bank;
	private EMITermRowModel emiTermRowModel;


	private GetOrderStatusResponse orderStatusResponse;
	private CardResponse cardResponse;
	private ModelService modelService;
	private AbstractOrderModel abstractOrderModel;
	private PaymentTransactionModel paymentTransactionModel;
	private PaymentTransactionEntryModel paymentTransactionEntryModel;
	private CurrencyModel currencyModel;
	private PaymentInfoModel paymentInfoModel;
	private MplPaymentTransactionService mplPaymentTransactionService;
	private AddressModel addressModel;
	private I18NService i18NService;
	private CountryModel countryModel;
	private UserModel userModel;
	private SavedCardModel savedCardModel;
	private UserService userService;
	private CreditCardPaymentInfoModel creditCardPaymentInfoModel;
	private BankforNetbankingModel bankforNetbankingModel;
	private FlexibleSearchService flexibleSearchService;
	private PersistentKeyGenerator codCodeGenerator;
	private Object object;
	private PaymentTypeModel paymentTypeModel;
	private ConfigurationService configurationService;
	private Configuration configuration;
	private CustomerModel customerModel;
	private DebitCardPaymentInfoModel debitCardPaymentInfoModel;
	private NetbankingPaymentInfoModel netbankingPaymentInfoModel;
	private EMIPaymentInfoModel eMIPaymentInfoModel;
	private EMIBankModel eMIBankModel;
	private OrderModel orderModel;
	private MplPaymentAuditModel mplPaymentAuditModel;
	private MplPaymentAuditEntryModel mplPaymentAuditEntryModel;
	//changes for JuspayEBSResponseFIX
	private JuspayEBSResponseDataModel juspayEBSResponseModel;
	private RiskResponse riskResponse;
	//private ObjectMapper objectMapper;
	private GetOrderStatusRequest orderStatusRequest;
	private MplFraudModelService mplFraudModelService;


	@Before
	public void setUp()
	{
		//TISSEC-50
		MockitoAnnotations.initMocks(this);

		this.mplPaymentServiceImpl = new MplPaymentServiceImpl();
		this.mplPaymentDao = Mockito.mock(MplPaymentDao.class);
		this.mplPaymentServiceImpl.setMplPaymentDao(mplPaymentDao);
		this.paymentType = Mockito.mock(PaymentTypeModel.class);
		this.bankForNB = Mockito.mock(BankforNetbankingModel.class);
		this.emiBankModel = Mockito.mock(EMIBankModel.class);
		this.bank = Mockito.mock(BankModel.class);
		this.emiTermRowModel = Mockito.mock(EMITermRowModel.class);
		this.orderStatusResponse = Mockito.mock(GetOrderStatusResponse.class);
		this.cardResponse = Mockito.mock(CardResponse.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.mplPaymentServiceImpl.setModelService(modelService);
		this.abstractOrderModel = Mockito.mock(AbstractOrderModel.class);
		this.paymentTransactionModel = Mockito.mock(PaymentTransactionModel.class);
		this.paymentTransactionEntryModel = Mockito.mock(PaymentTransactionEntryModel.class);
		this.currencyModel = Mockito.mock(CurrencyModel.class);
		this.paymentInfoModel = Mockito.mock(PaymentInfoModel.class);
		this.mplPaymentTransactionService = Mockito.mock(MplPaymentTransactionService.class);
		this.mplPaymentServiceImpl.setMplPaymentTransactionService(mplPaymentTransactionService);
		this.addressModel = Mockito.mock(AddressModel.class);
		this.i18NService = Mockito.mock(I18NService.class);
		this.mplPaymentServiceImpl.setI18NService(i18NService);
		this.countryModel = Mockito.mock(CountryModel.class);
		this.userModel = Mockito.mock(UserModel.class);
		this.savedCardModel = Mockito.mock(SavedCardModel.class);
		this.userService = Mockito.mock(UserService.class);
		this.mplPaymentServiceImpl.setUserService(userService);
		this.creditCardPaymentInfoModel = Mockito.mock(CreditCardPaymentInfoModel.class);
		this.bankforNetbankingModel = Mockito.mock(BankforNetbankingModel.class);
		this.flexibleSearchService = Mockito.mock(FlexibleSearchService.class);
		this.mplPaymentServiceImpl.setFlexibleSearchService(flexibleSearchService);
		this.codCodeGenerator = Mockito.mock(PersistentKeyGenerator.class);
		this.mplPaymentServiceImpl.setCodCodeGenerator(codCodeGenerator);
		this.object = Mockito.mock(Object.class);
		this.paymentTypeModel = Mockito.mock(PaymentTypeModel.class);
		this.configurationService = Mockito.mock(ConfigurationService.class);
		this.mplPaymentServiceImpl.setConfigurationService(configurationService);
		this.configuration = Mockito.mock(Configuration.class);
		this.customerModel = Mockito.mock(CustomerModel.class);
		this.debitCardPaymentInfoModel = Mockito.mock(DebitCardPaymentInfoModel.class);
		this.netbankingPaymentInfoModel = Mockito.mock(NetbankingPaymentInfoModel.class);
		this.eMIPaymentInfoModel = Mockito.mock(EMIPaymentInfoModel.class);
		this.emiBankModel = Mockito.mock(EMIBankModel.class);
		this.orderModel = Mockito.mock(OrderModel.class);
		this.mplPaymentAuditModel = Mockito.mock(MplPaymentAuditModel.class);
		//changes for JuspayEBSResponseFIX
		this.juspayEBSResponseModel = Mockito.mock(JuspayEBSResponseDataModel.class);
		this.mplPaymentAuditEntryModel = Mockito.mock(MplPaymentAuditEntryModel.class);
		this.riskResponse = Mockito.mock(RiskResponse.class);
		//this.objectMapper = Mockito.mock(ObjectMapper.class);
		this.orderStatusRequest = Mockito.mock(GetOrderStatusRequest.class);
		this.mplFraudModelService = Mockito.mock(MplFraudModelService.class);
		this.mplPaymentServiceImpl.setMplFraudModelService(mplFraudModelService);
	}


	@Test
	public void testGetPaymentModes()
	{
		final List<PaymentTypeModel> paymentTypeList = Arrays.asList(paymentType);
		Mockito.when(mplPaymentDao.getPaymentTypes("mpl")).thenReturn(paymentTypeList);
		final List<PaymentTypeModel> actual = mplPaymentServiceImpl.getPaymentModes("mpl");
		Assert.assertEquals(actual, paymentTypeList);
	}


	@Test
	public void testGetBanksByPriority()
	{
		final List<BankforNetbankingModel> bankForNBList = Arrays.asList(bankForNB);
		Mockito.when(mplPaymentDao.getBanksByPriority()).thenReturn(bankForNBList);
		final List<BankforNetbankingModel> actual = mplPaymentServiceImpl.getBanksByPriority();
		Assert.assertEquals(actual, bankForNBList);
	}


	@Test
	public void testGetOtherBanks()
	{
		final List<BankforNetbankingModel> bankForNBList = Arrays.asList(bankForNB);
		Mockito.when(mplPaymentDao.getOtherBanks()).thenReturn(bankForNBList);
		final List<BankforNetbankingModel> actual = mplPaymentServiceImpl.getOtherBanks();
		Assert.assertEquals(actual, bankForNBList);
	}


	//	@Test
	//	public void testSaveCODPaymentInfo()
	//	{
	//		Mockito.when(userService.getCurrentUser()).thenReturn(userModel);
	//		Mockito.doNothing().when(modelService).save(codPaymentInfoModel);
	//		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
	//		Mockito.doNothing().when(modelService).save(cartModel);
	//		mplPaymentServiceImpl.saveCODPaymentInfo("Test Name");
	//	}


	//	@Test
	//	public void testGetEMIBanks()
	//	{
	//		final List<EMIBankModel> emiBankList = Arrays.asList(emiBankModel);
	//		Mockito.when(mplPaymentDao.getEMIBanks(Double.valueOf(100), null)).thenReturn(emiBankList);
	//		Mockito.when(emiBankModel.getName()).thenReturn(bank);
	//		Mockito.when(bank.getBankName()).thenReturn("");//TODO : Please enter bank
	//		mplPaymentServiceImpl.getEMIBanks(Double.valueOf(100));
	//	}
	//
	//
	//	@Test
	//	public void testGetBankTerms()
	//	{
	//		final List<EMIBankModel> emiTermList = Arrays.asList(emiBankModel);
	//		Mockito.when(mplPaymentDao.getEMIBankTerms("")).thenReturn(emiTermList);//TODO : Please enter bank
	//		final Collection<EMITermRowModel> emiTerms = Arrays.asList(emiTermRowModel);
	//		Mockito.when(emiBankModel.getEMITermRates()).thenReturn(emiTerms);
	//		Mockito.when(emiTermRowModel.getTermInMonths()).thenReturn(Integer.valueOf(12));
	//		Mockito.when(emiTermRowModel.getInterestRate()).thenReturn(Double.valueOf(12));
	//		mplPaymentServiceImpl.getBankTerms("", Double.valueOf(1000));//TODO : Please enter bank
	//	}








	@Test
	public void testSaveCardDetailsFromJuspay()
	{
		final Map<String, Double> paymentMode = new HashMap<String, Double>();
		paymentMode.put("Credit Card", Double.valueOf(900.00));
		Mockito.when(orderStatusResponse.getCardResponse()).thenReturn(cardResponse);
		Mockito.when(cardResponse.getCardType()).thenReturn("CREDIT");
		Mockito.doNothing().when(modelService).save(abstractOrderModel);
		Mockito.when(modelService.create(CreditCardPaymentInfoModel.class)).thenReturn(creditCardPaymentInfoModel);
		Mockito.when(modelService.create(AddressModel.class)).thenReturn(addressModel);
		Mockito.when(orderStatusResponse.getUdf10()).thenReturn("false|false");
		Mockito.when(orderStatusResponse.getUdf1()).thenReturn("test1");
		final List<SavedCardModel> savedCardList = Arrays.asList(savedCardModel);
		Mockito.when(customerModel.getSavedCard()).thenReturn(savedCardList);
		Mockito.when(cardResponse.getCardReference()).thenReturn("TestRef2163");
		Mockito.when(savedCardModel.getCardReferenceNumber()).thenReturn("TestRef2163");
		Mockito.when(savedCardModel.getBillingAddress()).thenReturn(addressModel);
		Mockito.when(abstractOrderModel.getDeliveryAddress()).thenReturn(addressModel);

		Mockito.when(orderStatusResponse.getUdf2()).thenReturn("Test2");
		Mockito.when(orderStatusResponse.getUdf3()).thenReturn("Test3");
		Mockito.when(orderStatusResponse.getUdf4()).thenReturn("Test4");
		Mockito.when(orderStatusResponse.getUdf7()).thenReturn("Test7");
		Mockito.when(orderStatusResponse.getUdf8()).thenReturn("Test8");
		Mockito.when(orderStatusResponse.getUdf9()).thenReturn("Test9");
		Mockito.when(orderStatusResponse.getUdf5()).thenReturn("Test5");
		Mockito.when(orderStatusResponse.getUdf6()).thenReturn("India");
		Mockito.when(mplPaymentDao.getCountryISO(orderStatusResponse.getUdf6())).thenReturn("IND");

		Mockito.when(i18NService.getCountry(mplPaymentDao.getCountryISO(orderStatusResponse.getUdf6()))).thenReturn(countryModel);
		Mockito.when(abstractOrderModel.getUser()).thenReturn(userModel);
		Mockito.doNothing().when(modelService).save(addressModel);
		Mockito.when(abstractOrderModel.getCode()).thenReturn("testCode");
		Mockito.when(userService.getCurrentUser()).thenReturn(userModel);
		Mockito.when(cardResponse.getNameOnCard()).thenReturn("Test1");
		Mockito.when(cardResponse.getCardNumber()).thenReturn("4242");
		Mockito.when(cardResponse.getExpiryMonth()).thenReturn("10");
		Mockito.when(cardResponse.getExpiryYear()).thenReturn("2020");
		Mockito.when(cardResponse.getCardBrand()).thenReturn("VISA");
		Mockito.when(addressModel.getPhone1()).thenReturn("9999999999");
		Mockito.when(addressModel.getCellphone()).thenReturn("9999999999");

		Mockito.doNothing().when(modelService).save(creditCardPaymentInfoModel);
		Mockito.doNothing().when(modelService).save(debitCardPaymentInfoModel);
		Mockito.doNothing().when(modelService).save(netbankingPaymentInfoModel);
		Mockito.doNothing().when(modelService).save(eMIPaymentInfoModel);

		Mockito.when(modelService.create(DebitCardPaymentInfoModel.class)).thenReturn(debitCardPaymentInfoModel);
		Mockito.when(modelService.create(NetbankingPaymentInfoModel.class)).thenReturn(netbankingPaymentInfoModel);
		Mockito.when(modelService.create(EMIPaymentInfoModel.class)).thenReturn(eMIPaymentInfoModel);

		Mockito.when(abstractOrderModel.getGuid()).thenReturn("testGUID");
		Mockito.when(userModel.getName()).thenReturn("test name");
		Mockito.when(modelService.create(BankforNetbankingModel.class)).thenReturn(bankforNetbankingModel);
		Mockito.when(flexibleSearchService.getModelByExample(bankforNetbankingModel)).thenReturn(bankforNetbankingModel);

		Mockito.when(modelService.create(EMIBankModel.class)).thenReturn(emiBankModel);
		Mockito.when(flexibleSearchService.getModelByExample(emiBankModel)).thenReturn(emiBankModel);
		Mockito.when(orderStatusResponse.getBankTenure()).thenReturn("12");


		mplPaymentServiceImpl.saveCardDetailsFromJuspay(orderStatusResponse, paymentMode, abstractOrderModel);

	}

	@Test
	public void testSetPaymentTransaction()
	{
		final List<PaymentTransactionModel> paymentTransactionList = Arrays.asList(paymentTransactionModel);
		Mockito.when(abstractOrderModel.getPaymentTransactions()).thenReturn(paymentTransactionList);
		Mockito.when(orderStatusResponse.getUdf10()).thenReturn("false|false");
		final Map<String, Double> paymentMode = new HashMap<String, Double>();
		paymentMode.put("Credit Card", Double.valueOf(900.00));
		Mockito.when(orderStatusResponse.getOrderId()).thenReturn("111111111");
		Mockito.when(abstractOrderModel.getCurrency()).thenReturn(currencyModel);
		Mockito.when(abstractOrderModel.getPaymentInfo()).thenReturn(null);
		Mockito.doNothing().when(modelService).save(paymentTransactionEntryModel);
		final List<PaymentTransactionEntryModel> paymentTransactionEntryList = Arrays.asList(paymentTransactionEntryModel);
		for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
		{
			Mockito.when(
					mplPaymentTransactionService.createPaymentTranEntry(orderStatusResponse, abstractOrderModel, entry,
							paymentTransactionEntryList)).thenReturn(paymentTransactionEntryList);
		}
		Mockito.when(
				mplPaymentTransactionService.createPaymentTransaction(abstractOrderModel, orderStatusResponse,
						paymentTransactionEntryList)).thenReturn(paymentTransactionModel);
		Mockito.doNothing().when(abstractOrderModel).setPaymentTransactions(paymentTransactionList);
		Mockito.doNothing().when(modelService).save(abstractOrderModel);
		Mockito.when(orderStatusResponse.getCardResponse()).thenReturn(cardResponse);
		Mockito.when(cardResponse.getCardReference()).thenReturn("TestRef2163");
		Mockito.when(cardResponse.getCardType()).thenReturn("CREDIT");
		Mockito.when(orderStatusResponse.getPaymentMethodType()).thenReturn("CARD");
		Mockito.when(orderStatusResponse.getUdf1()).thenReturn("test1");
		Mockito.when(abstractOrderModel.getDeliveryAddress()).thenReturn(addressModel);


		Mockito.when(orderStatusResponse.getUdf2()).thenReturn("Test2");
		Mockito.when(orderStatusResponse.getUdf3()).thenReturn("Test3");
		Mockito.when(orderStatusResponse.getUdf4()).thenReturn("Test4");
		Mockito.when(orderStatusResponse.getUdf7()).thenReturn("Test7");
		Mockito.when(orderStatusResponse.getUdf8()).thenReturn("Test8");
		Mockito.when(orderStatusResponse.getUdf9()).thenReturn("Test9");
		Mockito.when(orderStatusResponse.getUdf5()).thenReturn("Test5");
		Mockito.when(orderStatusResponse.getUdf6()).thenReturn("India");
		Mockito.when(mplPaymentDao.getCountryISO(orderStatusResponse.getUdf6())).thenReturn("IND");

		Mockito.when(i18NService.getCountry(mplPaymentDao.getCountryISO(orderStatusResponse.getUdf6()))).thenReturn(countryModel);
		Mockito.when(abstractOrderModel.getUser()).thenReturn(userModel);
		Mockito.doNothing().when(modelService).save(addressModel);

		mplPaymentServiceImpl.setPaymentTransaction(orderStatusResponse, paymentMode, abstractOrderModel);

	}



	@Test
	public void testSetPaymentTransactionForCOD()
	{
		final Map<String, Double> paymentMode = new HashMap<String, Double>();
		paymentMode.put("COD", Double.valueOf(999.00));

		final List<PaymentTransactionModel> paymentTransactionList = Arrays.asList(paymentTransactionModel);
		Mockito.when(abstractOrderModel.getPaymentTransactions()).thenReturn(paymentTransactionList);
		Mockito.when(codCodeGenerator.generate()).thenReturn(object);
		Mockito.when(object.toString()).thenReturn("111111111");
		final double totalPriceWithConv = 999.00;
		Mockito.when(abstractOrderModel.getTotalPriceWithConv()).thenReturn(Double.valueOf(totalPriceWithConv));
		Mockito.when(abstractOrderModel.getCurrency()).thenReturn(currencyModel);
		Mockito.when(flexibleSearchService.getModelByExample(paymentTypeModel)).thenReturn(paymentTypeModel);
		Mockito.when(abstractOrderModel.getPaymentInfo()).thenReturn(null);
		Mockito.doNothing().when(modelService).save(paymentTransactionEntryModel);
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString("payment.cod")).thenReturn("Test Provider");
		Mockito.when(paymentTransactionEntryModel.getTransactionStatus()).thenReturn("Success");
		Mockito.doNothing().when(modelService).save(paymentTransactionModel);
		Mockito.doNothing().when(modelService).save(abstractOrderModel);
		Mockito.when(modelService.create(PaymentTransactionEntryModel.class)).thenReturn(paymentTransactionEntryModel);
		Mockito.when(modelService.create(PaymentTransactionModel.class)).thenReturn(paymentTransactionModel);
		Mockito.when(modelService.create(PaymentTypeModel.class)).thenReturn(paymentTypeModel);

		mplPaymentServiceImpl.setPaymentTransactionForCOD(abstractOrderModel);

	}



	@Test
	public void testFetchOrderOnGUID()
	{
		final String guid = "ywqgdu6t138-h13be63";
		Mockito.when(mplPaymentDao.fetchOrderOnGUID(guid)).thenReturn(orderModel);

		final OrderModel actual = mplPaymentServiceImpl.fetchOrderOnGUID(guid);

		Assert.assertEquals(orderModel, actual);
	}



	@Test
	public void testUpdateAuditEntry() throws JsonGenerationException, JsonMappingException, IOException
	{
		final Map<String, Double> paymentMode = new HashMap<String, Double>();
		paymentMode.put("Credit Card", Double.valueOf(999.00));

		Mockito.when(orderStatusResponse.getOrderId()).thenReturn("111111111");
		Mockito.when(mplPaymentDao.getAuditEntries(orderStatusResponse.getOrderId())).thenReturn(mplPaymentAuditModel);
		//changes for JuspayEBSResponseFIX
		Mockito.when(modelService.create(JuspayEBSResponseDataModel.class)).thenReturn(juspayEBSResponseModel);
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString("payment.ebs.downtime")).thenReturn("N");

		final List<MplPaymentAuditEntryModel> auditEntryist = Arrays.asList(mplPaymentAuditEntryModel);
		Mockito.when(mplPaymentAuditModel.getAuditEntries()).thenReturn(auditEntryist);
		Mockito.when(modelService.create(MplPaymentAuditEntryModel.class)).thenReturn(mplPaymentAuditEntryModel);

		Mockito.when(orderStatusResponse.getRiskResponse()).thenReturn(riskResponse);
		Mockito.when(orderStatusResponse.getStatus()).thenReturn("CHARGED");
		Mockito.when(riskResponse.getEbsRiskLevel()).thenReturn("GREEN");
		Mockito.when(riskResponse.getEbsBinCountry()).thenReturn("INDIA");
		Mockito.when(mplPaymentAuditEntryModel.getStatus()).thenReturn(MplPaymentAuditStatusEnum.COMPLETED);
		final long riskPercent = 20;
		Mockito.when(riskResponse.getEbsRiskPercentage()).thenReturn(Long.valueOf(riskPercent));
		Mockito.when(riskResponse.getEbsPaymentStatus()).thenReturn("Paid");

		//Mockito.when(objectMapper.writeValueAsString(orderStatusResponse)).thenReturn("TestResponse");
		//Mockito.when(objectMapper.writeValueAsString(orderStatusRequest)).thenReturn("TestRequest");

		Mockito.doNothing().when(modelService).save(mplPaymentAuditEntryModel);
		Mockito.doNothing().when(modelService).save(juspayEBSResponseModel);
		Mockito.doNothing().when(modelService).save(mplPaymentAuditModel);

		Mockito.when(juspayEBSResponseModel.getEbsRiskPercentage()).thenReturn("10");
		Mockito.doNothing().when(mplFraudModelService).updateFraudModel(orderModel, mplPaymentAuditModel);

		//final ObjectMapper objectMapper = new ObjectMapper();
		//Mockito.when(objectMapper.writeValueAsString(orderStatusResponse)).thenReturn("TestResponse");
		//Mockito.when(objectMapper.writeValueAsString(orderStatusRequest)).thenReturn("TestRequest");
		//Mockito.when(new ObjectMapper()).thenReturn(objectMapper);
		final boolean actual = mplPaymentServiceImpl.updateAuditEntry(orderStatusResponse, orderStatusRequest, orderModel,
				paymentMode);

		Assert.assertEquals(true, actual);

	}




	@Test
	public void testSaveDebitCard()
	{
		Mockito.when(orderStatusResponse.getUdf10()).thenReturn("true|true");
		Mockito.when(abstractOrderModel.getUser()).thenReturn(userModel);
		final List<SavedCardModel> savedCardList = Arrays.asList(savedCardModel);
		Mockito.when(customerModel.getSavedCard()).thenReturn(savedCardList);

		Mockito.when(cardResponse.getCardReference()).thenReturn("TestRef2163");
		Mockito.when(savedCardModel.getCardReferenceNumber()).thenReturn("TestRef2163");
		Mockito.doNothing().when(modelService).save(savedCardModel);
		Mockito.doNothing().when(modelService).save(customerModel);

		mplPaymentServiceImpl.saveDebitCard(orderStatusResponse, abstractOrderModel);
	}



	@Test
	public void testSaveCreditCard()
	{
		Mockito.when(orderStatusResponse.getUdf1()).thenReturn("test1");
		final List<SavedCardModel> savedCardList = Arrays.asList(savedCardModel);
		Mockito.when(customerModel.getSavedCard()).thenReturn(savedCardList);
		Mockito.when(cardResponse.getCardReference()).thenReturn("TestRef2163");
		Mockito.when(savedCardModel.getCardReferenceNumber()).thenReturn("TestRef2163");
		Mockito.when(savedCardModel.getBillingAddress()).thenReturn(addressModel);
		Mockito.when(abstractOrderModel.getDeliveryAddress()).thenReturn(addressModel);

		Mockito.when(orderStatusResponse.getUdf2()).thenReturn("Test2");
		Mockito.when(orderStatusResponse.getUdf3()).thenReturn("Test3");
		Mockito.when(orderStatusResponse.getUdf4()).thenReturn("Test4");
		Mockito.when(orderStatusResponse.getUdf7()).thenReturn("Test7");
		Mockito.when(orderStatusResponse.getUdf8()).thenReturn("Test8");
		Mockito.when(orderStatusResponse.getUdf9()).thenReturn("Test9");
		Mockito.when(orderStatusResponse.getUdf5()).thenReturn("Test5");
		Mockito.when(orderStatusResponse.getUdf6()).thenReturn("India");
		Mockito.when(mplPaymentDao.getCountryISO(orderStatusResponse.getUdf6())).thenReturn("IND");

		Mockito.when(i18NService.getCountry(mplPaymentDao.getCountryISO(orderStatusResponse.getUdf6()))).thenReturn(countryModel);
		Mockito.when(abstractOrderModel.getUser()).thenReturn(userModel);
		Mockito.doNothing().when(modelService).save(addressModel);

		Mockito.when(orderStatusResponse.getUdf10()).thenReturn("true|true");
		Mockito.when(abstractOrderModel.getUser()).thenReturn(userModel);

		Mockito.doNothing().when(modelService).save(savedCardModel);
		Mockito.doNothing().when(modelService).save(customerModel);

		mplPaymentServiceImpl.saveCreditCard(orderStatusResponse, abstractOrderModel, "false");

	}






}
