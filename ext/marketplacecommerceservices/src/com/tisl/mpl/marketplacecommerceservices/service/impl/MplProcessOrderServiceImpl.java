/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplProcessOrderDao;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayEBSService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author TCS
 *
 */
public class MplProcessOrderServiceImpl implements MplProcessOrderService
{
	//private static final Logger LOG = Logger.getLogger(MplProcessOrderServiceImpl.class);

	@Resource(name = "mplProcessOrderDao")
	private MplProcessOrderDao mplProcessOrderDao;
	@Autowired
	private JuspayEBSService juspayEBSService;
	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;
	@Autowired
	private MplCommerceCartService mplCommerceCartService;
	@Autowired
	private MplCancelOrderTicketImpl mplCancelOrderTicketImpl;
	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;
	@Resource(name = "modelService")
	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService#getPaymentPedingOrders()
	 */
	@Override
	public void getPaymentPedingOrders()
	{
		//DAO call to fetch PAYMENT PENDING orders
		final List<OrderModel> orders = mplProcessOrderDao.getPaymentPedingOrders(OrderStatus.PAYMENT_PENDING.toString());
		List<PaymentTransactionModel> paymentTransactions = new ArrayList<PaymentTransactionModel>();
		String juspayOrder = "";

		//getting the TAT for 25mins
		final Double juspayWebhookRetryTAT = getJuspayWebhookRetryTAT();
		Date orderTATForTimeout = new Date();

		//getting list of Juspay req ids for Payment Pending orders
		for (final OrderModel orderModel : orders)
		{
			if (null != juspayWebhookRetryTAT && juspayWebhookRetryTAT.doubleValue() > 0)
			{
				final Calendar cal = Calendar.getInstance();
				cal.setTime(orderModel.getModifiedtime());
				//adding the TAT to order modified time to get the time upto which the Payment Pending Orders will be checked for Processing
				cal.add(Calendar.MINUTE, +juspayWebhookRetryTAT.intValue());
				orderTATForTimeout = cal.getTime();
			}
			if (orderTATForTimeout.before(new Date()))
			{
				paymentTransactions = orderModel.getPaymentTransactions();

				if (CollectionUtils.isNotEmpty(paymentTransactions)
						&& paymentTransactions.get(paymentTransactions.size() - 1).getStatus().equalsIgnoreCase("success"))
				{
					//adding the request ids to a list
					//juspayOrders.add(paymentTransactions.get(paymentTransactions.size() - 1).getRequestId());

					//getting the juspay order against order
					juspayOrder = paymentTransactions.get(paymentTransactions.size() - 1).getRequestId();
				}

				if (StringUtils.isNotEmpty(juspayOrder))
				{
					//to check webhook entry status at Juapay corresponding to Payment Pending orders
					final List<JuspayWebhookModel> hooks = checkstatusAtJuspay(juspayOrder);

					final List<JuspayWebhookModel> uniqueList = new ArrayList<JuspayWebhookModel>();
					for (final JuspayWebhookModel oModel : hooks)
					{
						if (null != oModel.getOrderStatus() && oModel.getIsExpired().booleanValue())
						{
							//getting all the webhook data where isExpired is Y and adding into a list
							uniqueList.add(oModel);
						}
					}

					for (final JuspayWebhookModel juspayWebhookModel : hooks)
					{
						if (CollectionUtils.isNotEmpty(uniqueList))
						{
							//iterating through the new list against the whole webhook data list
							boolean duplicateFound = false;
							for (final JuspayWebhookModel unique : uniqueList)
							{
								//if there is duplicate order id which is not expired(N) then setting it to Y
								if (unique != null
										&& unique.getOrderStatus() != null
										&& juspayWebhookModel.getOrderStatus() != null
										&& StringUtils.equalsIgnoreCase(unique.getOrderStatus().getOrderId(), juspayWebhookModel
												.getOrderStatus().getOrderId())
										&& ((StringUtils.equalsIgnoreCase(juspayWebhookModel.getEventName(), "ORDER_SUCCEEDED") || (StringUtils
												.equalsIgnoreCase(juspayWebhookModel.getEventName(), "ORDER_FAILED")))))

								{
									duplicateFound = true;
									break;
								}
							}
							if (duplicateFound)
							{
								juspayWebhookModel.setIsExpired(Boolean.TRUE);
								modelService.save(juspayWebhookModel);
							}
							else
							{
								//Change order status & process order based on Webhook status
								takeActionAgainstOrder(juspayWebhookModel, orderModel);

								uniqueList.add(juspayWebhookModel);
							}
						}
						else
						{
							//Change order status & process order based on Webhook status
							takeActionAgainstOrder(juspayWebhookModel, orderModel);
							uniqueList.add(juspayWebhookModel);
						}
					}
				}
			}
			else
			{
				//getting PinCode against Order
				final String defaultPinCode = getPinCodeForOrder(orderModel);

				//OMS Deallocation call for failed order
				mplCommerceCartService.isInventoryReserved(orderModel,
						MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);

				//Creating cancel order ticket
				final boolean ticketstatus = mplCancelOrderTicketImpl.createCancelTicket(orderModel);
				if (ticketstatus)
				{
					//change status to Payment Failed
					orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.PAYMENT_FAILED);
				}
				//juspayWebhookModel.setIsExpired(Boolean.TRUE);
			}

		}
	}

	private List<JuspayWebhookModel> checkstatusAtJuspay(final String juspayOrder)
	{
		List<JuspayWebhookModel> hookList = new ArrayList<JuspayWebhookModel>();
		//		for (final String reqId : juspayOrders)
		//		{
		//			//DAO call to fetch webhook entries corresponding to the Payment Pending orders & adding in a list
		//			hookList.addAll(mplProcessOrderDao.getEventsForPendingOrders(reqId));
		//		}

		hookList = mplProcessOrderDao.getEventsForPendingOrders(juspayOrder);

		return hookList;
	}

	private String getPinCodeForOrder(final OrderModel orderModel)
	{
		String defaultPinCode = "".intern();
		if (null != orderModel.getDeliveryAddress() && StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPostalcode()))
		{
			defaultPinCode = orderModel.getDeliveryAddress().getPostalcode();
		}
		else
		{
			/*
			 * This block will execute only incase of standalone CNC cart and OMS is not using pincode to deallocate cart
			 * reservation. As pincode is mandatory in Inventory reservation adding dummy pincode for cart deallocation
			 */
			defaultPinCode = MarketplacecommerceservicesConstants.PINCODE;
		}

		return defaultPinCode;
	}

	private void takeActionAgainstOrder(final JuspayWebhookModel juspayWebhookModel, final OrderModel orderModel)
	{
		if (juspayWebhookModel.getEventName().equalsIgnoreCase("ORDER_SUCCEEDED")
				&& !juspayWebhookModel.getIsExpired().booleanValue())
		{
			orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);

			//Re-trigger submit order process from Payment_Pending to Payment_Successful
			juspayEBSService.initiateProcess(orderModel);

			juspayWebhookModel.setIsExpired(Boolean.TRUE);
		}
		else if (juspayWebhookModel.getEventName().equalsIgnoreCase("ORDER_FAILED")
				&& !juspayWebhookModel.getIsExpired().booleanValue())
		{
			//getting PinCode against Order
			final String defaultPinCode = getPinCodeForOrder(orderModel);

			//OMS Deallocation call for failed order
			mplCommerceCartService.isInventoryReserved(orderModel,
					MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);

			//Creating cancel order ticket
			final boolean ticketstatus = mplCancelOrderTicketImpl.createCancelTicket(orderModel);
			if (ticketstatus)
			{
				//change status to Payment Failed
				orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.PAYMENT_FAILED);
			}
			juspayWebhookModel.setIsExpired(Boolean.TRUE);
		}
	}

	private Double getJuspayWebhookRetryTAT()
	{
		Double juspayWebhookRetryTAT = Double.valueOf(0);

		juspayWebhookRetryTAT = mplProcessOrderDao.getJuspayWebhookRetryTAT();
		if (null != juspayWebhookRetryTAT && juspayWebhookRetryTAT.doubleValue() > 0)
		{
			return juspayWebhookRetryTAT;
		}
		else
		{
			return juspayWebhookRetryTAT;
		}
	}
}
