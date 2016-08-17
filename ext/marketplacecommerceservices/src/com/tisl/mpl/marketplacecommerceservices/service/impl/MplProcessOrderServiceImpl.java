/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
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
	private static final Logger LOG = Logger.getLogger(MplProcessOrderServiceImpl.class);

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
	@Resource(name = "mplPaymentAuditDao")
	private MplOrderDao mplOrderDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService#getPaymentPedingOrders()
	 */
	@Override
	public void processPaymentPedingOrders() throws EtailNonBusinessExceptions
	{
		try
		{

			//DAO call to fetch PAYMENT PENDING orders
			final List<OrderModel> orders = getMplProcessOrderDao().getPaymentPedingOrders(OrderStatus.PAYMENT_PENDING.toString());

			if (CollectionUtils.isNotEmpty(orders))
			{
				//getting list of Juspay req ids for Payment Pending orders
				for (final OrderModel orderModel : orders)
				{
					orderModel.setIsOrderUnderProcess(Boolean.TRUE);
					getModelService().save(orderModel);
					//final List<PaymentTransactionModel> paymentTransactions = new ArrayList<PaymentTransactionModel>();
					final String juspayOrder = "";

					Date orderTATForTimeout = new Date();
					//getting the TAT for 25mins
					final Double juspayWebhookRetryTAT = getJuspayWebhookRetryTAT();
					if (null != juspayWebhookRetryTAT && juspayWebhookRetryTAT.doubleValue() > 0)
					{
						final Calendar cal = Calendar.getInstance();
						cal.setTime(orderModel.getCreationtime());
						//adding the TAT to order modified time to get the time upto which the Payment Pending Orders will be checked for Processing
						cal.add(Calendar.MINUTE, +juspayWebhookRetryTAT.intValue());
						orderTATForTimeout = cal.getTime();
					}
					//Check if time-out is exceeded or not
					if (orderTATForTimeout.before(new Date()))
					{
						//						paymentTransactions = orderModel.getPaymentTransactions();
						//
						//						if (CollectionUtils.isNotEmpty(paymentTransactions)
						//								&& paymentTransactions.get(paymentTransactions.size() - 1).getStatus().equalsIgnoreCase("success"))
						//						{
						//							//getting the juspay order against order
						//							juspayOrder = paymentTransactions.get(paymentTransactions.size() - 1).getRequestId();
						//						}

						final String cartGuid = orderModel.getGuid();

						final MplPaymentAuditModel auditModel = getMplOrderDao().getAuditList(cartGuid);

						if (null != auditModel && StringUtils.isNotEmpty(auditModel.getAuditId()))
						{
							//to check webhook entry status at Juspay corresponding to Payment Pending orders
							final List<JuspayWebhookModel> hooks = checkstatusAtJuspay(juspayOrder);

							final List<JuspayWebhookModel> uniqueList = new ArrayList<JuspayWebhookModel>();
							if (CollectionUtils.isNotEmpty(hooks))
							{
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
					}
					else
					{
						//getting PinCode against Order
						final String defaultPinCode = getPinCodeForOrder(orderModel);

						//OMS Deallocation call for failed order
						getMplCommerceCartService().isInventoryReserved(orderModel,
								MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);

						getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_TIMEOUT);
					}
				}
			}

		}
		catch (final NullPointerException e)
		{
			LOG.error("NullPointerException in getPaymentPedingOrders================================", e);
		}
		catch (final Exception e)
		{
			LOG.error("Error in getPaymentPedingOrders================================", e);
		}
	}



	/**
	 * This method returns list of JuspayWebhookModel against juspayOrderId
	 *
	 * @param juspayOrder
	 * @return List<JuspayWebhookModel>
	 */
	private List<JuspayWebhookModel> checkstatusAtJuspay(final String juspayOrder)
	{
		return getMplProcessOrderDao().getEventsForPendingOrders(juspayOrder);
	}



	/**
	 * This method returns pincode for the orderModel
	 *
	 * @param orderModel
	 * @return String
	 */
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



	/**
	 * This method takes action against successful orders
	 *
	 * @param juspayWebhookModel
	 * @param orderModel
	 */
	private void takeActionAgainstOrder(final JuspayWebhookModel juspayWebhookModel, final OrderModel orderModel)
	{
		if (juspayWebhookModel.getEventName().equalsIgnoreCase("ORDER_SUCCEEDED")
				&& !juspayWebhookModel.getIsExpired().booleanValue())
		{
			getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);

			//Re-trigger submit order process from Payment_Pending to Payment_Successful
			getJuspayEBSService().initiateProcess(orderModel);

			juspayWebhookModel.setIsExpired(Boolean.TRUE);
		}
		//		else if (juspayWebhookModel.getEventName().equalsIgnoreCase("ORDER_FAILED")
		//				&& !juspayWebhookModel.getIsExpired().booleanValue())
		//		{
		//			//getting PinCode against Order
		//			final String defaultPinCode = getPinCodeForOrder(orderModel);
		//
		//			//OMS Deallocation call for failed order
		//			mplCommerceCartService.isInventoryReserved(orderModel,
		//					MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);
		//
		//			//Creating cancel order ticket
		//			final boolean ticketstatus = mplCancelOrderTicketImpl.createCancelTicket(orderModel);
		//			if (ticketstatus)
		//			{
		//				//change status to Payment Failed
		//				orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.PAYMENT_FAILED);
		//			}
		//			juspayWebhookModel.setIsExpired(Boolean.TRUE);
		//		}
	}


	/**
	 * This method returns the tat for processing the order in the job
	 *
	 * @return Double
	 */
	private Double getJuspayWebhookRetryTAT()
	{
		final Double juspayWebhookRetryTAT = getMplProcessOrderDao().getJuspayWebhookRetryTAT();
		if (null != juspayWebhookRetryTAT && juspayWebhookRetryTAT.doubleValue() > 0)
		{
			return juspayWebhookRetryTAT;
		}
		else
		{
			return Double.valueOf(0.0);
		}
	}

	/**
	 * @return the mplProcessOrderDao
	 */
	public MplProcessOrderDao getMplProcessOrderDao()
	{
		return mplProcessOrderDao;
	}

	/**
	 * @param mplProcessOrderDao
	 *           the mplProcessOrderDao to set
	 */
	public void setMplProcessOrderDao(final MplProcessOrderDao mplProcessOrderDao)
	{
		this.mplProcessOrderDao = mplProcessOrderDao;
	}

	/**
	 * @return the juspayEBSService
	 */
	public JuspayEBSService getJuspayEBSService()
	{
		return juspayEBSService;
	}

	/**
	 * @param juspayEBSService
	 *           the juspayEBSService to set
	 */
	public void setJuspayEBSService(final JuspayEBSService juspayEBSService)
	{
		this.juspayEBSService = juspayEBSService;
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

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the mplOrderDao
	 */
	public MplOrderDao getMplOrderDao()
	{
		return mplOrderDao;
	}

	/**
	 * @param mplOrderDao
	 *           the mplOrderDao to set
	 */
	public void setMplOrderDao(final MplOrderDao mplOrderDao)
	{
		this.mplOrderDao = mplOrderDao;
	}






}
