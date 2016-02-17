package com.tisl.mpl.juspay.example;

import org.apache.log4j.Logger;

import com.tisl.mpl.juspay.Environment;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.request.AddCardRequest;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.request.InitOrderRequest;
import com.tisl.mpl.juspay.request.ListCardsRequest;
import com.tisl.mpl.juspay.request.PaymentRequest;
import com.tisl.mpl.juspay.response.AddCardResponse;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.juspay.response.InitOrderResponse;
import com.tisl.mpl.juspay.response.ListCardsResponse;
import com.tisl.mpl.juspay.response.PaymentResponse;


public class JuspayPaymentExample
{

	private static final String TEST_KEY = "782CB4B3F5B84BDDB3C9EAFA6A134DC3";
	private static final Logger log = Logger.getLogger(JuspayPaymentExample.class);

	public static void main(final String[] args) throws Exception
	{

		final PaymentService juspayService = new PaymentService().withEnvironment(Environment.PRODUCTION).withKey(TEST_KEY)
				.withMerchantId("juspay");

		//InitOrder example calls
		final InitOrderRequest request = new InitOrderRequest().withOrderId(Long.toString(System.currentTimeMillis()))
				.withAmount(Double.valueOf(100.0)).withCustomerId("customer_id").withEmail("customer@shop.in");
		final InitOrderResponse initOrderResponse = juspayService.initOrder(request);
		log.info("Init order response:  " + initOrderResponse);


		//AddCard example call
		final AddCardRequest addCardRequest = new AddCardRequest().withNameOnCard("Customer Name").withCardExpMonth("11")
				.withCardExpYear("2020").withCardNumber("4111222233334444").withCustomerEmail("customer@shop.in")
				.withCustomerId("customer_id");
		final AddCardResponse addCardResponse = juspayService.addCard(addCardRequest);
		log.info("Add card response: " + addCardResponse);

		//ListCard example call
		final ListCardsRequest listCardsRequest = new ListCardsRequest().withCustomerId("customerId");
		final ListCardsResponse listCardsResponse = juspayService.listCards(listCardsRequest);
		log.info("List card response: " + listCardsResponse);

		// OrderStatus example call
		final GetOrderStatusRequest orderStatusRequest = new GetOrderStatusRequest();
		orderStatusRequest.withOrderId("1355728384");
		final GetOrderStatusResponse orderStatusResponse = juspayService.getOrderStatus(orderStatusRequest);
		log.info("Order status response: " + orderStatusResponse);

		// PaymentStatus example call
		final PaymentRequest paymentRequest = new PaymentRequest();
		//merchantId=juspay&orderId=1359115924989&cardNumber=4375511234567899&cardExpYear=2013&cardExpMonth=05&cardSecurityCode=111
		paymentRequest.withMerchantId("juspay").withOrderId("1359115924989").withCardNumber("4375511234567899")
				.withCardExpYear("2013").withCardExpMonth("05").withCardSecurityCode("111");
		final PaymentResponse paymentResponse = juspayService.makePayment(paymentRequest);
		log.info("Payment Response is:" + paymentResponse);

		/*
		 * String specialTestKey = "enter-the-special-key"; PaymentService juspayExtendedService = new
		 * PaymentService().withEnvironment(Environment.PRODUCTION) .withKey(specialTestKey); GetCardRequest
		 * getCardRequest = new GetCardRequest(); getCardRequest.setCardToken("d52fe2d2-83b3-4513-a46a-7b7dbdbc4cf0");
		 * GetCardResponse getCardResponse = juspayExtendedService.getCard(getCardRequest);
		 * System.out.println(getCardResponse);
		 */


	}
}
