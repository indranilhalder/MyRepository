/**
 *
 */
package com.tisl.mpl.integration.oms.adapter.test;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.PaymentInfo;
import com.tisl.mpl.integration.oms.adapter.CustomOmsOrderSyncAdapter;


/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomOmsOrderSyncAdapterTest
{
	@Mock
	private CustomOmsOrderSyncAdapter customOmsOrderSyncAdapter;
	@Mock
	private final OrderModel orderModel = Mockito.mock(OrderModel.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.customOmsOrderSyncAdapter = Mockito.mock(CustomOmsOrderSyncAdapter.class);
	}


	@Test
	public void update()
	{
		final Order order = new Order();
		order.setBaseStoreName("store");
		order.setCancellable(true);
		order.setCartID("123456");
		order.setChannel("web");
		order.setCurrencyCode("INR");
		order.setCustomerID("CUST1234");
		order.setEmailid("ABC@tcs.com");
		order.setCustomerLocale("locale123");
		order.setFirstName("x");
		final Date issueDate = new Date();
		issueDate.setDate(1);
		issueDate.setHours(13);
		issueDate.setMonth(10);
		issueDate.setYear(2015);
		order.setIssueDate(issueDate);
		order.setLastName("y");
		order.setMiddleName("z");
		order.setOrderId("123456789");
		order.setOrderType("type");
		final List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();
		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setId("id123");
		paymentInfo.setPaymentCost(1234);
		paymentInfo.setPaymentMode("Credit card");
		paymentInfos.add(paymentInfo);
		order.setPaymentInfos(paymentInfos);
		order.setPhoneNumber("987654321");
		order.setState("Delhi");
		order.setUsername("abc");
		order.setUssId("S00000000001");

		final List<OrderLine> orderLines = new ArrayList<OrderLine>();
		final OrderLine orderLine = new OrderLine();
		orderLine.setApprotionedPrice(1234);
		orderLine.setDeliveryType("ED");
		orderLine.setExchangeAllowed("Y");
		orderLine.setFulfillmentMode("TSHIP");
		orderLine.setGiftMessage("giftMessage");
		orderLine.setGiftPrice(2000);
		orderLine.setInvoiceNo("8905");
		orderLine.setIsCOD(true);
		orderLine.setIsAFreebie("N");
		orderLine.setOrderLineId("7895432123");
		orderLine.setOrderLineStatus("Pending");
		orderLine.setParentTransactionID("S0000000000000000001");
		orderLine.setSkuId("123456789");
		orderLine.setShippingCharge(125);
		orderLine.setTransportMode("AIR");
		orderLines.add(orderLine);
		order.setOrderLines(orderLines);

		final Date orderUpdateTime = new Date();
		orderUpdateTime.setDate(1);
		orderUpdateTime.setHours(14);
		orderUpdateTime.setMinutes(30);
		orderUpdateTime.setMonth(10);
		orderUpdateTime.setYear(2015);

		given(customOmsOrderSyncAdapter.update(order, orderUpdateTime)).willReturn(orderModel);
	}


	@Test
	public void processOrderStatus()
	{
		final Order order = new Order();
		order.setBaseStoreName("store");
		order.setCancellable(true);
		order.setCartID("123456");
		order.setChannel("web");
		order.setCurrencyCode("INR");
		order.setCustomerID("CUST1234");
		order.setEmailid("ABC@tcs.com");
		order.setCustomerLocale("locale123");
		order.setFirstName("x");
		final Date issueDate = new Date();
		issueDate.setDate(1);
		issueDate.setHours(13);
		issueDate.setMonth(10);
		issueDate.setYear(2015);
		order.setIssueDate(issueDate);
		order.setLastName("y");
		order.setMiddleName("z");
		order.setOrderId("123456789");
		order.setOrderType("type");
		final List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();
		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setId("id123");
		paymentInfo.setPaymentCost(1234);
		paymentInfo.setPaymentMode("Credit card");
		paymentInfos.add(paymentInfo);
		order.setPaymentInfos(paymentInfos);
		order.setPhoneNumber("987654321");
		order.setState("Delhi");
		order.setUsername("abc");
		order.setUssId("S00000000001");

		final List<OrderLine> orderLines = new ArrayList<OrderLine>();
		final OrderLine orderLine = new OrderLine();
		orderLine.setApprotionedPrice(1234);
		orderLine.setDeliveryType("ED");
		orderLine.setExchangeAllowed("Y");
		orderLine.setFulfillmentMode("TSHIP");
		orderLine.setGiftMessage("giftMessage");
		orderLine.setGiftPrice(2000);
		orderLine.setInvoiceNo("8905");
		orderLine.setIsCOD(true);
		orderLine.setIsAFreebie("N");
		orderLine.setOrderLineId("7895432123");
		orderLine.setOrderLineStatus("Pending");
		orderLine.setParentTransactionID("S0000000000000000001");
		orderLine.setSkuId("123456789");
		orderLine.setShippingCharge(125);
		orderLine.setTransportMode("AIR");
		orderLines.add(orderLine);
		order.setOrderLines(orderLines);

		final OrderStatus newOrderStatus = null;
		OrderStatus.CANCELLED.equals(newOrderStatus);
	}
}
