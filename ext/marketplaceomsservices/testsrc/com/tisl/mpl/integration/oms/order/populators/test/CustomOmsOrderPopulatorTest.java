/**
 *
 */
package com.tisl.mpl.integration.oms.order.populators.test;

import static org.mockito.BDDMockito.given;

import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.externaltax.TaxCodeStrategy;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.NetbankingPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.integration.commons.services.OndemandTaxCalculationService;
import de.hybris.platform.omsorders.services.delivery.OmsZoneDeliveryModeValueStrategy;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.PaymentInfo;
import com.tisl.mpl.integration.oms.order.populators.CustomOmsOrderPopulator;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
public class CustomOmsOrderPopulatorTest
{
	@Mock
	private CustomOmsOrderPopulator customOmsOrderPopulator;
	@Mock
	private CustomerNameStrategy customerNameStrategy;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;
	@Mock
	private OndemandTaxCalculationService ondemandTaxCalculationService;
	@Mock
	private OmsZoneDeliveryModeValueStrategy omsZoneDeliveryModeValueStrategy;
	@Mock
	private TaxCodeStrategy taxCodeStrategy;

	@Before
	public void setUp()
	{
		customOmsOrderPopulator = new CustomOmsOrderPopulator();
		customOmsOrderPopulator.setCustomerNameStrategy(customerNameStrategy);
		customOmsOrderPopulator.setCustomerEmailResolutionService(customerEmailResolutionService);
		customOmsOrderPopulator.setOndemandTaxCalculationService(ondemandTaxCalculationService);
		customOmsOrderPopulator.setOmsZoneDeliveryModeValueStrategy(omsZoneDeliveryModeValueStrategy);
		customOmsOrderPopulator.setTaxCodeStrategy(taxCodeStrategy);

	}

	// public void populate(final OrderModel source, final Order target) throws ConversionException

	@Test
	public void testPopulate() throws ConversionException
	{
		//final List<OrderLine> ondemandOrderEntrysTest = new ArrayList<OrderLine>();

		final OrderModel orderMOdelMOck = Mockito.mock(OrderModel.class);
		final Order orderMock = Mockito.mock(Order.class);

		final AddressModel addressModelMock = Mockito.mock(AddressModel.class);
		final UserModel userModelMock = Mockito.mock(UserModel.class);
		final CustomerModel custModelMock = Mockito.mock(CustomerModel.class);
		final PaymentInfoModel paymentInfoModelMock = Mockito.mock(CreditCardPaymentInfoModel.class);
		final PaymentInfo paymentInfo = Mockito.mock(PaymentInfo.class);
		//final SalesApplication salesAppMock = Mockito.mock(SalesApplication.class);
		final PaymentTransactionModel ptmodelMock = Mockito.mock(PaymentTransactionModel.class);
		final PaymentTransactionEntryModel ptmEntryModelMock = Mockito.mock(PaymentTransactionEntryModel.class);

		final PaymentTypeModel ptmTypeModelMock = Mockito.mock(PaymentTypeModel.class);

		//final PaymentInfoModel paymentInfoModelMock = new CreditCardPaymentInfoModel();


		final Date modifiedtime = new Date();
		final Date creationtime = new Date();

		final BigDecimal BigDec1 = new BigDecimal("1238126387123");



		final List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();
		//final List<OrderLine> ondemandOrderEntrys = new ArrayList<OrderLine>();
		final List<PaymentTransactionModel> ptmodelListMock = new ArrayList<PaymentTransactionModel>();

		addressModelMock.setFirstname("Address Bijay");
		addressModelMock.setLastname("Address Kumar");
		addressModelMock.setPhone1("8850554563");
		addressModelMock.setPhone2("7896541235");

		custModelMock.setCustomerID("cust1234");
		custModelMock.setUid("CustUID1234");
		custModelMock.setMiddleName("mahajan");

		userModelMock.setDefaultPaymentAddress(addressModelMock);
		userModelMock.setPasswordQuestion("What is the name of your pet ?");
		userModelMock.setUid("UserUID789");

		paymentInfoModelMock.setCode("C001");
		paymentInfoModelMock.setUser(userModelMock);

		ptmTypeModelMock.setMode("COD");

		ptmEntryModelMock.setPaymentMode(ptmTypeModelMock);
		ptmEntryModelMock.setAmount(BigDec1);
		ptmEntryModelMock.setCode("CD009");


		ptmodelMock.setPaymentProvider("Provided by User");
		ptmodelMock.setStatus("SUCCESS");
		ptmodelMock.setCode("ptmc1234");


		ptmodelListMock.add(ptmodelMock);

		orderMOdelMOck.setUser(userModelMock);
		orderMOdelMOck.setCode("ordermodelcode1234");
		orderMOdelMOck.setPaymentInfo(paymentInfoModelMock);
		orderMOdelMOck.setGuid("gui1234");
		orderMOdelMOck.setDeliveryAddress(addressModelMock);
		orderMOdelMOck.setModifiedtime(modifiedtime);
		orderMOdelMOck.setCreationtime(creationtime);
		orderMOdelMOck.setPaymentTransactions(ptmodelListMock);
		orderMOdelMOck.setPaymentInfo(paymentInfoModelMock);

		testgetPaymentMode(paymentInfoModelMock);

		paymentInfos.add(paymentInfo);

		final Double code = new Double(55000.23);

		// final ItemModel itemModelMock = Mockito.mock(ItemModel.class);

		orderMOdelMOck.setCode("C001");
		orderMOdelMOck.setConvenienceCharges(code);

		orderMock.setEmailid("arun.k@gmail.com");
		orderMock.setBaseStoreName("Tata Store");

		customOmsOrderPopulator.populate(orderMOdelMOck, orderMock);


		/*
		 * public void populate(final OrderModel source, final Order target) throws ConversionException
		 * given(mplCommerceCartServiceImpl.getAddress(addressDatasMock)).willReturn(expressCheckoutAddressMap);
		 */
	}

	@Test
	public void testSetFirstAndLastName() throws ConversionException
	{
		final OrderModel orderMOdelMOck = Mockito.mock(OrderModel.class);
		final Order orderMock = Mockito.mock(Order.class);

		final AddressModel addressModelMock = Mockito.mock(AddressModel.class);
		addressModelMock.setFirstname("Address Bijay");
		addressModelMock.setLastname("Address Kumar");
		//orderMOdelMOck.setDe

		orderMOdelMOck.setPaymentAddress(addressModelMock);

		orderMock.setFirstName("Bijay");
		orderMock.setLastName("Kumar");
		orderMock.setShippingFirstName("ShipingBijay");
		orderMock.setShippingLastName("ShippingKumar");

		customOmsOrderPopulator.setFirstAndLastName(orderMOdelMOck, orderMock);
	}

	// private String getPaymentMode(final PaymentInfoModel paymentInfoModel)

	@Test
	public void testgetPaymentMode(final PaymentInfoModel paymentInfoModelMock)
	{
		//final PaymentInfoModel paymentInfoModelMock = Mockito.mock(CreditCardPaymentInfoModel.class);
		//final PaymentInfoModel paymentInfoModelMock = Mockito.mock(NetbankingPaymentInfoModel.class);
		final UserModel userModelMock = Mockito.mock(UserModel.class);
		final CreditCardPaymentInfoModel creditCardInfo = Mockito.mock(CreditCardPaymentInfoModel.class);

		final NetbankingPaymentInfoModel netBankingInfo = Mockito.mock(NetbankingPaymentInfoModel.class);

		//final String response = "CC";

		userModelMock.setPasswordQuestion("What is the name of your pet ?");
		userModelMock.setUid("UserUID789");

		creditCardInfo.setUser(userModelMock);

		netBankingInfo.setUser(userModelMock);

		paymentInfoModelMock.setCode("C001");
		paymentInfoModelMock.setUser(userModelMock);

		// given(customOmsOrderPopulator.getPaymentMode(paymentInfoModelMock)).willReturn(response);
	}

	// protected String getShippingMethod(final OrderModel source)

	@Test
	public void testgetShippingMethod()
	{
		final OrderModel orderMOdelMOck = Mockito.mock(OrderModel.class);
		final AddressModel addressModelMock = Mockito.mock(AddressModel.class);
		final DeliveryModeModel deliveryModelMock = Mockito.mock(DeliveryModeModel.class);
		final String mode = "COD";

		addressModelMock.setFirstname("Address Bijay");
		addressModelMock.setLastname("Address Kumar");

		deliveryModelMock.setDescription("Delivered by abcd");

		//orderMOdelMOck.setDe

		orderMOdelMOck.setDeliveryMode(deliveryModelMock);
		orderMOdelMOck.setPaymentAddress(addressModelMock);

		//getShippingMethod

		//when(customOmsOrderPopulator.getShippingMethod(orderMOdelMOck)).thenRe(mode);
		given(customOmsOrderPopulator.getShippingMethod(orderMOdelMOck)).willReturn(mode);

	}



















}
