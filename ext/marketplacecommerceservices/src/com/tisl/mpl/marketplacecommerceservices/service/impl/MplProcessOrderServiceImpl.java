/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.util.ArrayList;
import java.util.List;

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
	@Autowired
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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService#getPaymentPedingOrders()
	 */
	@Override
	public List<OrderModel> getPaymentPedingOrders()
	{
		//DAO call to fetch PAYMENT PENDING orders
		final List<OrderModel> orders = mplProcessOrderDao.getPaymentPedingOrders(OrderStatus.PAYMENT_PENDING.toString());
		List<PaymentTransactionModel> paymentTransactions = new ArrayList<PaymentTransactionModel>();
		final List<String> juspayOrders = new ArrayList<String>();


		//getting list of Juspay req ids for Payment Pending orders
		for (final OrderModel orderModel : orders)
		{
			paymentTransactions = orderModel.getPaymentTransactions();

			if (CollectionUtils.isNotEmpty(paymentTransactions)
					&& paymentTransactions.get(paymentTransactions.size() - 1).getStatus().equalsIgnoreCase("success"))
			{
				//adding the request ids to a list
				juspayOrders.add(paymentTransactions.get(paymentTransactions.size() - 1).getRequestId());
			}

			if (CollectionUtils.isNotEmpty(juspayOrders))
			{
				//to check webhook entry status at Juapay corresponding to Payment Pending orders
				final List<JuspayWebhookModel> hooks = checkstatusAtJuspay(juspayOrders);

				if (CollectionUtils.isNotEmpty(hooks))
				{
					for (final JuspayWebhookModel juspayWebhookModel : hooks)
					{
						if (juspayWebhookModel.getEventName().equalsIgnoreCase("ORDER_SUCCEEDED"))
						{
							//Re-trigger submit order process from Payment_Pending to Payment_Successful
							juspayEBSService.initiateProcess(orderModel);
						}
						else if (juspayWebhookModel.getEventName().equalsIgnoreCase("ORDER_FAILED"))
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
						}
						else
						{
							continue;
						}
					}
				}
			}
		}

		return orders;
	}

	private List<JuspayWebhookModel> checkstatusAtJuspay(final List<String> juspayOrders)
	{
		final List<JuspayWebhookModel> hookList = new ArrayList<JuspayWebhookModel>();
		for (final String reqId : juspayOrders)
		{
			//DAO call to fetch webhook entries corresponding to the Payment Pending orders & adding in a list
			hookList.addAll(mplProcessOrderDao.getEventsForPendingOrders(reqId));
		}

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
}
