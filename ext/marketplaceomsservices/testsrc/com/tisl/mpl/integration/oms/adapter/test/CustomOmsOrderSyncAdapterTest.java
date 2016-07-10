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
		//TISSEC-50
		order.setBaseStoreName("");//TODO : Please enter store name
		order.setCancellable(true);
		order.setCartID("");//TODO : Please enter cart id
		order.setChannel("");//TODO : Please enter channel
		order.setCurrencyCode("");//TODO : Please enter currency code
		order.setCustomerID("");//TODO : Please enter customer id
		order.setEmailid("");//TODO : Please enter email id
		order.setCustomerLocale("");//TODO : Please enter customer locale
		order.setFirstName("");//TODO : Please enter fisrt name
		final Date issueDate = new Date();
		issueDate.setDate(1);
		issueDate.setHours(13);
		issueDate.setMonth(10);
		issueDate.setYear(2015);
		order.setIssueDate(issueDate);
		order.setLastName("");//TODO : Please enter last name
		order.setMiddleName("");//TODO : Please enter middle name
		order.setOrderId("");//TODO : Please enter order id
		order.setOrderType("");//TODO : Please enter order type
		final List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();
		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setId("");//TODO : Please enter id
		paymentInfo.setPaymentCost(1234);
		paymentInfo.setPaymentMode("");//TODO : Please enter payment mode
		paymentInfos.add(paymentInfo);
		order.setPaymentInfos(paymentInfos);
		order.setPhoneNumber("");//TODO : Please enter phone number
		order.setState("");//TODO : Please enter state
		order.setUsername("");//TODO : Please enter username
		order.setUssId("");//TODO : Please enter ussid

		final List<OrderLine> orderLines = new ArrayList<OrderLine>();
		final OrderLine orderLine = new OrderLine();
		orderLine.setApprotionedPrice(1234);
		orderLine.setDeliveryType("");//TODO : Please enter delivery type
		orderLine.setExchangeAllowed("Y");
		orderLine.setFulfillmentMode("");//TODO : Please enter fulfillment mode
		orderLine.setGiftMessage("");//TODO : Please enter giftMessage
		orderLine.setGiftPrice(2000);
		orderLine.setInvoiceNo("");//TODO : Please enter invoice no
		orderLine.setIsCOD(true);
		orderLine.setIsAFreebie("N");
		orderLine.setOrderLineId("");//TODO : Please enter orderline id
		orderLine.setOrderLineStatus("");//TODO : Please enter orderline status
		orderLine.setParentTransactionID("");//TODO : Please enter parent transaction id
		orderLine.setSkuId("");//TODO : Please enter sku id
		orderLine.setShippingCharge(125);
		orderLine.setTransportMode("");//TODO : Please enter transport mode
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
		order.setBaseStoreName("");//TODO : Please enter store name
		order.setCancellable(true);
		order.setCartID("");//TODO : Please enter cart id
		order.setChannel("");//TODO : Please enter channel
		order.setCurrencyCode("");//TODO : Please enter currency
		order.setCustomerID("");//TODO : Please enter customer id
		order.setEmailid("");//TODO : Please enter email id
		order.setCustomerLocale("");//TODO : Please enter customer locale
		order.setFirstName("");//TODO : Please enter first name
		final Date issueDate = new Date();
		issueDate.setDate(1);
		issueDate.setHours(13);
		issueDate.setMonth(10);
		issueDate.setYear(2015);
		order.setIssueDate(issueDate);
		order.setLastName("");//TODO : Please enter last name
		order.setMiddleName("");//TODO : Please enter middle name
		order.setOrderId("");//TODO : Please enter order id
		order.setOrderType("");//TODO : Please enter order type
		final List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();
		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setId("");//TODO : Please enter id
		paymentInfo.setPaymentCost(1234);
		paymentInfo.setPaymentMode("");//TODO : Please enter payment mode
		paymentInfos.add(paymentInfo);
		order.setPaymentInfos(paymentInfos);
		order.setPhoneNumber("");//TODO : Please enter phone number
		order.setState("");//TODO : Please enter state
		order.setUsername("");//TODO : Please enter username
		order.setUssId("");//TODO : Please enter ussid

		final List<OrderLine> orderLines = new ArrayList<OrderLine>();
		final OrderLine orderLine = new OrderLine();
		orderLine.setApprotionedPrice(1234);
		orderLine.setDeliveryType("");//TODO : Please enter delivery type
		orderLine.setExchangeAllowed("Y");
		orderLine.setFulfillmentMode("");//TODO : Please enter fulfillment mode
		orderLine.setGiftMessage("");//TODO : Please enter giftMessage
		orderLine.setGiftPrice(2000);
		orderLine.setInvoiceNo("");//TODO : Please enter invoice no
		orderLine.setIsCOD(true);
		orderLine.setIsAFreebie("N");
		orderLine.setOrderLineId("");//TODO : Please enter orderline id
		orderLine.setOrderLineStatus("");//TODO : Please enter orderline status
		orderLine.setParentTransactionID("");//TODO : Please enter parent transaction id
		orderLine.setSkuId("");//TODO : Please enter sku id
		orderLine.setShippingCharge(125);
		orderLine.setTransportMode("");//TODO : Please enter transport mode
		orderLines.add(orderLine);
		order.setOrderLines(orderLines);

		final OrderStatus newOrderStatus = null;
		OrderStatus.CANCELLED.equals(newOrderStatus);
	}
}
