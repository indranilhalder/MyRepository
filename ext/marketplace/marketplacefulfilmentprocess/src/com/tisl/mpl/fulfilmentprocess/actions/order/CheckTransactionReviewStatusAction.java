/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.fulfilmentprocess.actions.order;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.util.localization.Localization;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.fulfilmentprocess.constants.MarketplaceFulfilmentProcessConstants;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplNotificationService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCancelOrderTicketImpl;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * This action check if authorization has review status
 */
public class CheckTransactionReviewStatusAction extends AbstractAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CheckTransactionReviewStatusAction.class);
	private TicketBusinessService ticketBusinessService;

	/*
	 * @Autowired private MplOmsStockReservation mplOmsStockReservation;
	 */

	@Autowired
	private CustomOmsOrderService customOmsOrderService;
	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "mplNotificationSaveService")
	private MplNotificationService notificationService;
	@Autowired
	private MplCommerceCartService mplCommerceCartService;
	@Autowired
	private MplCancelOrderTicketImpl mplCancelOrderTicketImpl;
	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;



	public enum Transition
	{
		OK, NOK, WAIT;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();
			for (final Transition transitions : Transition.values())
			{
				res.add(transitions.toString());
			}
			return res;
		}
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	@Override
	public final String execute(final OrderProcessModel process) throws RetryLaterException, JAXBException
	{
		LOG.debug("===========================Inside CheckTransactionReviewStatusAction======================");
		return executeAction(process).toString();
	}

	protected Transition executeAction(final OrderProcessModel process) throws JAXBException
	{
		Transition result = Transition.WAIT;

		final OrderModel order = process.getOrder();

		if (null != order && null != order.getStatus())
		{
			result = checkOrderStatus(order, process);
			//Track Order Notification Start
			final String useNotification = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.USE_NOTIFICATION);

			if (useNotification.equalsIgnoreCase("Y"))
			{
				final OrderStatusNotificationModel osn = new OrderStatusNotificationModel();
				final OrderStatus orderStatus = order.getStatus();

				osn.setOrderNumber(order.getCode());
				osn.setOrderStatus(orderStatus.getCode());
				osn.setCustomerUID(order.getUser().getUid());
				final Boolean isRead = Boolean.FALSE;
				osn.setIsRead(isRead);
				final String fireStatusAll = configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.FIRE_NOTIFICATION);

				saveToNotification(fireStatusAll, osn);
			}
		}
		return result;
	}


	/**
	 * @param fireStatusAll
	 * @param osn
	 */
	private void saveToNotification(final String fireStatusAll, final OrderStatusNotificationModel osn)
	{
		final String[] fireStatusList = fireStatusAll.split(",");
		boolean isFireStatus = false;
		for (final String fireStatus : fireStatusList)
		{
			if (osn.getOrderStatus().equalsIgnoreCase(fireStatus))
			{
				isFireStatus = true;
				break;
			}
		}

		if (isFireStatus)
		{
			notificationService.saveToNotification(osn);
		}
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	/**
	 * This method checks the order status and returns a Transition response to the order process
	 *
	 * @param orderModel
	 * @param process
	 * @return Transition
	 * @throws JAXBException
	 */
	protected Transition checkOrderStatus(final OrderModel orderModel, final OrderProcessModel process) throws JAXBException
	{
		final String orderStatus = orderModel.getStatus().toString();
		String defaultPinCode = "".intern();
		if (null != orderModel.getDeliveryAddress() && StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPostalcode()))
		{
			defaultPinCode = orderModel.getDeliveryAddress().getPostalcode();
		}
		else
		{
			/*
			 * TISOMSII-86
			 * 
			 * This block will execute only incase of standalone CNC cart and OMS is not using pincode to deallocate cart
			 * reservation. As pincode is mandatory in Inventory reservation adding dummy pincode for cart deallocation
			 */
			defaultPinCode = MarketplaceFulfilmentProcessConstants.PINCODE;
		}

		//returning OK for order status "PAYMENT_SUCCESSFUL"
		if (orderStatus.equalsIgnoreCase(MarketplaceFulfilmentProcessConstants.PAYMENT_SUCCESSFUL))
		{
			return Transition.OK;
		}

		//returning NOK for order status "PAYMENT_FAILED"
		else if (orderStatus.equalsIgnoreCase(MarketplaceFulfilmentProcessConstants.PAYMENT_FAILED))
		{
			if (StringUtils.isNotEmpty(defaultPinCode))
			{
				//OMS Deallocation call for failed order
				mplCommerceCartService.isInventoryReserved(orderModel,
						MarketplaceFulfilmentProcessConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);

				//Creating cancel order ticket
				final boolean ticketstatus = mplCancelOrderTicketImpl.createCancelTicket(orderModel);
				if (ticketstatus)
				{
					orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.CANCELLATION_INITIATED);
				}
			}
			return Transition.NOK;
		}

		//returning NOK for order status "CANCELLED"
		else if (orderStatus.equalsIgnoreCase(MarketplaceFulfilmentProcessConstants.ORDER_CANCELLED))
		{
			return Transition.NOK;
		}

		//returning NOK for order status "RMS_VERIFICATION_FAILED"
		else if (orderStatus.equalsIgnoreCase(MarketplaceFulfilmentProcessConstants.RMS_VERIFICATION_FAILED))
		{
			if (StringUtils.isNotEmpty(defaultPinCode))
			{
				//OMS Deallocation call for failed order
				mplCommerceCartService.isInventoryReserved(orderModel,
						MarketplaceFulfilmentProcessConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);

				//Creating cancel order ticket
				final boolean ticketstatus = mplCancelOrderTicketImpl.createCancelTicket(orderModel);
				if (ticketstatus)
				{
					orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.CANCELLATION_INITIATED);
				}

				//Initiating refund
				final PaymentTransactionModel paymentTransactionModel = initiateRefund(orderModel);

				//Refund model mapping for initiated refund
				if (null != paymentTransactionModel && StringUtils.isNotEmpty(paymentTransactionModel.getCode()))
				{
					final String status = paymentTransactionModel.getStatus();
					if (StringUtils.isNotEmpty(status) && status.equalsIgnoreCase("SUCCESS"))
					{
						orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.ORDER_CANCELLED);
					}
					else if (StringUtils.isNotEmpty(status) && status.equalsIgnoreCase("FAILURE"))
					{
						orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.REFUND_IN_PROGRESS);
					}
					else if (StringUtils.isNotEmpty(status) && status.equalsIgnoreCase("PENDING"))
					{
						orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.REFUND_INITIATED);
						mplCancelOrderTicketImpl.refundMapping(paymentTransactionModel.getCode(), orderModel);
					}
				}
				else
				{
					orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.REFUND_INITIATED);
				}
			}
			return Transition.NOK;
		}

		//returning OK for order status "RMS_VERIFICATION_PENDING"
		else if (orderStatus.equalsIgnoreCase(MarketplaceFulfilmentProcessConstants.RMS_VERIFICATION_PENDING))
		{
			if (StringUtils.isNotEmpty(defaultPinCode))
			{
				//OMS Allocation 6 hours call for failed order
				mplCommerceCartService.isInventoryReserved(orderModel,
						MarketplaceFulfilmentProcessConstants.OMS_INVENTORY_RESV_TYPE_ORDERHELD, defaultPinCode);

				//Order Creation in CRM for held orders
				orderCreationInCRM(orderModel);
				process.setState(ProcessState.WAITING);
				getModelService().save(process);
				return Transition.WAIT;
			}
			else
			{
				return Transition.NOK;
			}
		}
		else
		{
			return Transition.WAIT;
		}
	}

	protected Transition checkPaymentTransaction(final PaymentTransactionModel transaction, final OrderModel orderModel)
	{
		final List<PaymentTransactionEntryModel> transactionEntries = transaction.getEntries();
		for (int index = transactionEntries.size() - 1; index >= 0; index--)
		{
			final PaymentTransactionEntryModel entry = transactionEntries.get(index);

			if (isReviewDecision(entry))
			{
				if (isReviewAccepted(entry))
				{
					orderModel.setStatus(OrderStatus.PAYMENT_AUTHORIZED);
					getModelService().save(orderModel);
					return Transition.OK;
				}
				else
				{
					orderModel.setStatus(OrderStatus.PAYMENT_NOT_AUTHORIZED);
					getModelService().save(orderModel);
					return Transition.NOK;
				}
			}
			else if (isAuthorization(entry))
			{
				if (isAuthorizationInReview(entry))
				{
					final String ticketTitle = Localization.getLocalizedString("message.ticket.orderinreview.title");
					final String ticketMessage = Localization.getLocalizedString("message.ticket.orderinreview.content", new Object[]
					{ orderModel.getCode() });
					createTicket(ticketTitle, ticketMessage, orderModel, CsTicketCategory.FRAUD, CsTicketPriority.HIGH);

					orderModel.setStatus(OrderStatus.SUSPENDED);
					getModelService().save(orderModel);
					return Transition.WAIT;
				}
				else
				{
					return Transition.OK;
				}
			}

			// Continue onto next entry
		}
		return Transition.OK;
	}


	/**
	 * This method initiates refund if the order is cancelled
	 *
	 * @param order
	 * @return PaymentTransactionModel
	 */
	private PaymentTransactionModel initiateRefund(final OrderModel order)
	{
		PaymentTransactionModel paymentTransactionModel = null;
		final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
		try
		{
			paymentTransactionModel = mplJusPayRefundService.doRefund(order, order.getTotalPriceWithConv().doubleValue(),
					PaymentTransactionType.CANCEL, uniqueRequestId);
			if (null != paymentTransactionModel)
			{
				mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);
			}
			else
			{
				LOG.error("Failed to Refund");
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(order, "FAILURE",
					order.getTotalPriceWithConv(), PaymentTransactionType.CANCEL, "FAILURE", uniqueRequestId);
			mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);

			// TISSIT-1784 Code addition started
			if (CollectionUtils.isNotEmpty(order.getChildOrders()))
			{
				for (final OrderModel subOrderModel : order.getChildOrders())
				{
					if (subOrderModel != null && CollectionUtils.isNotEmpty(subOrderModel.getEntries()))
					{
						for (final AbstractOrderEntryModel subOrderEntryModel : subOrderModel.getEntries())
						{
							if (subOrderEntryModel != null)
							{
								// Making RTM entry to be picked up by webhook job
								final RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(
										RefundTransactionMappingModel.class);
								refundTransactionMappingModel.setRefundedOrderEntry(subOrderEntryModel);
								refundTransactionMappingModel.setJuspayRefundId(uniqueRequestId);
								refundTransactionMappingModel.setCreationtime(new Date());
								refundTransactionMappingModel.setRefundType(JuspayRefundType.CANCELLED_FOR_RISK);
								getModelService().save(refundTransactionMappingModel);
							}
						}
					}
				}
			}
			// TISSIT-1784 Code addition ended
		}
		return paymentTransactionModel;
	}

	/**
	 * This method creates a ticket in CRM
	 *
	 * @param order
	 */
	private void orderCreationInCRM(final OrderModel order)
	{
		try
		{
			customOmsOrderService.createCRMTicket(order);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex.getMessage());
		}
	}



	protected CsTicketModel createTicket(final String subject, final String description, final OrderModel order,
			final CsTicketCategory category, final CsTicketPriority priority)
	{
		final CsTicketModel newTicket = modelService.create(CsTicketModel.class);
		newTicket.setHeadline(subject);

		newTicket.setCategory(category);
		newTicket.setPriority(priority);
		newTicket.setOrder(order);
		newTicket.setCustomer(order.getUser());

		final CsCustomerEventModel newTicketEvent = new CsCustomerEventModel();
		newTicketEvent.setText(description);

		return getTicketBusinessService().createTicket(newTicket, newTicketEvent);
	}

	protected boolean isReviewDecision(final PaymentTransactionEntryModel entry)
	{
		return PaymentTransactionType.REVIEW_DECISION.equals(entry.getType());
	}

	protected boolean isReviewAccepted(final PaymentTransactionEntryModel entry)
	{
		return TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus());
	}

	protected boolean isAuthorization(final PaymentTransactionEntryModel entry)
	{
		return PaymentTransactionType.AUTHORIZATION.equals(entry.getType());
	}

	protected boolean isAuthorizationInReview(final PaymentTransactionEntryModel entry)
	{
		return TransactionStatus.REVIEW.name().equals(entry.getTransactionStatus());
	}

	protected TicketBusinessService getTicketBusinessService()
	{
		return ticketBusinessService;
	}

	@Required
	public void setTicketBusinessService(final TicketBusinessService ticketBusinessService)
	{
		this.ticketBusinessService = ticketBusinessService;
	}



	//Getters and setters
	/**
	 * @return the customOmsOrderService
	 */
	public CustomOmsOrderService getCustomOmsOrderService()
	{
		return customOmsOrderService;
	}

	/**
	 * @param customOmsOrderService
	 *           the customOmsOrderService to set
	 */
	public void setCustomOmsOrderService(final CustomOmsOrderService customOmsOrderService)
	{
		this.customOmsOrderService = customOmsOrderService;
	}

	/**
	 * @return the mplJusPayRefundService
	 */
	public MplJusPayRefundService getMplJusPayRefundService()
	{
		return mplJusPayRefundService;
	}

	/**
	 * @param mplJusPayRefundService
	 *           the mplJusPayRefundService to set
	 */
	public void setMplJusPayRefundService(final MplJusPayRefundService mplJusPayRefundService)
	{
		this.mplJusPayRefundService = mplJusPayRefundService;
	}

	/**
	 * @return the notificationService
	 */
	public MplNotificationService getNotificationService()
	{
		return notificationService;
	}

	/**
	 * @param notificationService
	 *           the notificationService to set
	 */
	public void setNotificationService(final MplNotificationService notificationService)
	{
		this.notificationService = notificationService;
	}

	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}

	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}

	/**
	 * @return the mplCancelOrderTicketImpl
	 */
	public MplCancelOrderTicketImpl getMplCancelOrderTicketImpl()
	{
		return mplCancelOrderTicketImpl;
	}

	/**
	 * @param mplCancelOrderTicketImpl
	 *           the mplCancelOrderTicketImpl to set
	 */
	public void setMplCancelOrderTicketImpl(final MplCancelOrderTicketImpl mplCancelOrderTicketImpl)
	{
		this.mplCancelOrderTicketImpl = mplCancelOrderTicketImpl;
	}

	/**
	 * @return the orderStatusSpecifier
	 */
	public OrderStatusSpecifier getOrderStatusSpecifier()
	{
		return orderStatusSpecifier;
	}

	/**
	 * @param orderStatusSpecifier
	 *           the orderStatusSpecifier to set
	 */
	public void setOrderStatusSpecifier(final OrderStatusSpecifier orderStatusSpecifier)
	{
		this.orderStatusSpecifier = orderStatusSpecifier;
	}



}