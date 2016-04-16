/**
 *
 */
package com.tisl.mpl.integration.oms.order.process.action;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;

import org.apache.log4j.Logger;

import com.tisl.mpl.integration.oms.order.service.impl.OrderToQueue;


/**
 * @author TCS
 *
 */
public class CustomSendOrderToQueueAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CustomSendOrderToQueueAction.class);

	private OrderToQueue mplOrderToQueue;


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.processengine.action.AbstractSimpleDecisionAction#executeAction(de.hybris.platform.processengine
	 * .model.BusinessProcessModel)
	 */
	@Override
	public de.hybris.platform.processengine.action.AbstractSimpleDecisionAction.Transition executeAction(
			final OrderProcessModel orderProcessModel) throws RetryLaterException, Exception
	{
		LOG.debug("Inside CustomSendOrderToQueueAction class............... ");

		final OrderModel order = orderProcessModel.getOrder();
		//		if (order.getOmsSubmitStatus() == null || !order.getOmsSubmitStatus().equals(MarketplaceomsservicesConstants.SUCCESS))
		//		{

		LOG.debug("********Sending to order Queue with id::**********" + order.getCode());

		final String orderXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:order xmlns:ns2=\"http://api.hybris.com/oms/order\" xmlns:ns3=\"http://api.hybris.com/oms/address\" xmlns:ns4=\"http://api.hybris.com/oms/shipping\"><properties/><customerID>1000098001</customerID><orderType>01</orderType><submissionDateTime>2015-12-15T12:35:03Z</submissionDateTime><channel>01</channel><cartID>edfff54a-a3ab-4fdd-80a4-fc659ce69612</cartID><phoneNumber>9833511776</phoneNumber><emailid>ssrinivasan@tataunistore.com</emailid><firstName>Sidharth</firstName><issueDate>2015-12-15T12:14:37Z</issueDate><lastName>Srinivasan</lastName><orderId>200001170</orderId><orderLines><properties/><sellerId>500112</sellerId><fulfillmentType>SSHIP</fulfillmentType><ussId>500112300540840004</ussId><transportMode>AIR</transportMode><isCOD>false</isCOD><sellerOrderId>151215-000-200964</sellerOrderId><approtionedPrice>999.0</approtionedPrice><approtionedCODPrice>0.0</approtionedCODPrice><isaGift>false</isaGift><ShippingCharge>0.0</ShippingCharge><storeID>Store1</storeID><productSize>L</productSize><cancellationAllowed>1</cancellationAllowed><returnsAllowed>15</returnsAllowed><replacementAllowed>77</replacementAllowed><exchangeAllowed>1</exchangeAllowed><collectName>name</collectName><collectPhoneNumber>9681684233</collectPhoneNumber><estimatedDelivery>2015-12-15T12:14:38Z</estimatedDelivery><apportionedShippingCharge>0.0</apportionedShippingCharge><giftPrice>0.0</giftPrice><isAFreebie>false</isAFreebie><productName>Westside Mid Blue Kurta</productName><shippedQuantity>0</shippedQuantity><orderLineId>500112000203101</orderLineId><locationRoles>SHIPPING</locationRoles><orderLineStatus>PYMTSCSS</orderLineStatus><skuId>300540840004</skuId><taxCategory>N/A</taxCategory><quantity><unitCode>pieces</unitCode><value>1</value></quantity><quantityUnassigned><unitCode>pieces</unitCode><value>1</value></quantityUnassigned><unitPrice><currencyCode>INR</currencyCode><value>999.0</value></unitPrice><unitTax><currencyCode>INR</currencyCode><value>0.0</value></unitTax><fulfillmentMode>REGULAR</fulfillmentMode></orderLines><paymentInfos><properties/><paymentCost>949.05</paymentCost><paymentMode>CC</paymentMode><paymentStatus>S</paymentStatus><paymentInfo>999999-1450181677155</paymentInfo><paymentDate>2015-12-15T12:14:37Z</paymentDate><billingAddress><addressLine1>C/O TISL UniStore, Tower 3, Floor 5, Equ</addressLine1><addressLine2>Marg, Kurla West</addressLine2><addressLine3>Mumbai</addressLine3><cityName>Mumbai</cityName><countryCode>IN</countryCode><countryIso3166Alpha2Code>IN</countryIso3166Alpha2Code><countryName>India</countryName><firstName>Sidharth</firstName><lastName>Srinivasan</lastName><name>Sidharth Srinivasan</name><phoneNumber>9833511776</phoneNumber><pinCode>600050</pinCode><stateCode>13</stateCode></billingAddress><id>410001606-1450181677172</id></paymentInfos><shippingAddress><addressLine1>C/O TISL UniStore, Tower 3, Floor 5, Equ</addressLine1><addressLine2>Marg, Kurla West</addressLine2><addressLine3>Mumbai</addressLine3><cityName>Mumbai</cityName><countryCode>IN</countryCode><countryIso3166Alpha2Code>IN</countryIso3166Alpha2Code><countryName>India</countryName><firstName>Sidharth</firstName><lastName>Srinivasan</lastName><name>Sidharth Srinivasan</name><phoneNumber>9833511776</phoneNumber><pinCode>600050</pinCode><stateCode>13</stateCode></shippingAddress><shippingFirstName>Sidharth</shippingFirstName><shippingLastName>Srinivasan</shippingLastName><username>60d94b5c-38da-48a7-9374-26d669e10e22</username><cancellable>false</cancellable></ns2:order>";


		LOG.debug("************orderToQueue Object***********" + mplOrderToQueue);
		mplOrderToQueue.sendMessage(orderXML);

		//		}

		return AbstractSimpleDecisionAction.Transition.OK;
	}


	/**
	 * @return the mplOrderToQueue
	 */
	public OrderToQueue getMplOrderToQueue()
	{
		return mplOrderToQueue;
	}


	/**
	 * @param mplOrderToQueue
	 *           the mplOrderToQueue to set
	 */
	public void setMplOrderToQueue(final OrderToQueue mplOrderToQueue)
	{
		this.mplOrderToQueue = mplOrderToQueue;
	}

}
