/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPaymentDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentTransactionService;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
public class MplPaymentTransactionServiceImpl implements MplPaymentTransactionService
{
	private static final Logger LOG = Logger.getLogger(MplPaymentTransactionServiceImpl.class);

	@Autowired
	private ModelService modelService;
	@Autowired
	private MplPaymentDao mplPaymentDao;
//	@Autowired
//	private BaseStoreService baseStoreService;//Sonar Fix


	/**
	 * This method creates a new Payment Transaction Entry after payment has been completed
	 *
	 * @param getOrderStatusResponse
	 * @param cart
	 * @param entry
	 * @param paymentTransactionEntryList
	 * @return List<PaymentTransactionEntryModel>
	 */
	@Override
	public List<PaymentTransactionEntryModel> createPaymentTranEntry(final GetOrderStatusResponse getOrderStatusResponse,
			final AbstractOrderModel cart, final Map.Entry<String, Double> entry,
			final List<PaymentTransactionEntryModel> paymentTransactionEntryList) //Changed to abstractOrderModel for TPR-629
	{
		//OrderIssues:- Null check added
		final PaymentTransactionEntryModel paymentTransactionEntry = getModelService().create(PaymentTransactionEntryModel.class);
		try
		{
			if (null != getOrderStatusResponse.getPaymentGatewayResponse())
			{
				if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentGatewayResponse().getRootReferenceNumber()))
				{
					paymentTransactionEntry.setSubscriptionID(getOrderStatusResponse.getPaymentGatewayResponse()
							.getRootReferenceNumber());
				}

				paymentTransactionEntry.setTransactionStatusDetails(null != getOrderStatusResponse.getPaymentGatewayResponse()
						.getResponseMessage() ? getOrderStatusResponse.getPaymentGatewayResponse().getResponseMessage() : "");
				paymentTransactionEntry
						.setRequestToken(null != getOrderStatusResponse.getPaymentGatewayResponse().getTxnId() ? getOrderStatusResponse
								.getPaymentGatewayResponse().getTxnId() : "");
				paymentTransactionEntry.setRequestId(null != getOrderStatusResponse.getPaymentGatewayResponse()
						.getExternalGatewayTxnId() ? getOrderStatusResponse.getPaymentGatewayResponse().getExternalGatewayTxnId() : "");

				if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentGatewayResponse().getAuthIdCode()))
				{
					paymentTransactionEntry.setCode(getOrderStatusResponse.getPaymentGatewayResponse().getAuthIdCode() + "-"
							+ System.currentTimeMillis());
				}
				else
				{
					paymentTransactionEntry.setCode(cart.getCode() + "-" + System.currentTimeMillis());
				}
			}
			else
			{
				LOG.error("Payment Gateway Response is empty");
				paymentTransactionEntry.setCode(cart.getCode() + "-" + System.currentTimeMillis());
			}

			final String orderStatus = getOrderStatusResponse.getStatus();

			if (StringUtils.isNotEmpty(orderStatus))
			{
				setEntryStatus(orderStatus, paymentTransactionEntry);
			}

			//paymentTransactionEntry.setAmount(BigDecimal.valueOf(entry.getValue().doubleValue()));
			//TISPRO-540 - Getting amount from CartModel

			//Changes Added ***********************
			//paymentTransactionEntry.setAmount(BigDecimal.valueOf(cart.getTotalPrice().doubleValue()));
			paymentTransactionEntry.setAmount(BigDecimal.valueOf(getOrderStatusResponse.getAmount().doubleValue()));

			paymentTransactionEntry.setTime(new Date());
			paymentTransactionEntry.setCurrency(cart.getCurrency());

			//final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode(entry.getKey());
			//TISPRO-540 - Getting Payment mode from CartModel
			//		if (StringUtils.isNotEmpty(cart.getModeOfPayment()))
			//		{
			//			final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode(cart.getModeOfPayment());
			//			paymentTransactionEntry.setPaymentMode(paymenttype);
			//		}
			final BaseStoreModel baseStore = cart.getStore();
			if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentMethodType())
					&& getOrderStatusResponse.getPaymentMethodType().equalsIgnoreCase("NB"))
			{
				final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("Netbanking", baseStore);
				paymentTransactionEntry.setPaymentMode(paymenttype);
			}
			else if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentMethodType())
					&& getOrderStatusResponse.getPaymentMethodType().equalsIgnoreCase("CARD"))
			{
				final String cardType = getOrderStatusResponse.getCardResponse().getCardType();
				if (StringUtils.isEmpty(cardType))
				{
					//:- Check with Barun Da wt to set the payment Mode, Since paymntmode is mandatory
					final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("UNKNOWN", baseStore);
					paymentTransactionEntry.setPaymentMode(paymenttype);
				}
				else
				{
					if (cardType.equalsIgnoreCase("DEBIT"))
					{
						final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("Debit Card", baseStore);
						paymentTransactionEntry.setPaymentMode(paymenttype);
					}
					else if (cardType.equalsIgnoreCase("CREDIT"))
					{
						final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("Credit Card", baseStore);
						paymentTransactionEntry.setPaymentMode(paymenttype);
					}
					else
					{
						final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("EMI", baseStore);
						paymentTransactionEntry.setPaymentMode(paymenttype);
					}
				}

			}

			//Check handled to remove concurrent scenario - TPR-629
			if (null == cart.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(cart.getStatus()))
			{
				getModelService().save(paymentTransactionEntry);
				paymentTransactionEntryList.add(paymentTransactionEntry);
			}
			else if (null != cart.getPaymentInfo())
			{
				LOG.error("Order already has payment info -- not saving paymentTransactionEntry>>>" + cart.getPaymentInfo().getCode());
			}
			else
			{
				LOG.error("Payment_Timeout order status for orderCode>>>" + cart.getCode());
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_EXC_LOG + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_EXC_LOG + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return paymentTransactionEntryList;

	}

	/**
	 * This method sets the Payment Transaction entry status based on the Juspay Order Status Response
	 *
	 * @param orderStatus
	 * @param paymentTransactionEntry
	 */
	private void setEntryStatus(final String orderStatus, final PaymentTransactionEntryModel paymentTransactionEntry)
	{
		if (orderStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
		{
			paymentTransactionEntry.setType(PaymentTransactionType.CAPTURE);
			paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.SUCCESS);
		}
		else if (orderStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.AUTHORIZATION_FAILED))
		{
			paymentTransactionEntry.setType(PaymentTransactionType.AUTHORIZATION_FAILED);
			paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.FAILURE);
		}
		else if (orderStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.AUTHENTICATION_FAILED))
		{
			paymentTransactionEntry.setType(PaymentTransactionType.AUTHENTICATION_FAILED);
			paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.FAILURE);
		}
		else if (orderStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.JUSPAY_DECLINED))
		{
			paymentTransactionEntry.setType(PaymentTransactionType.JUSPAY_DECLINED);
			paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.FAILURE);
		}
		else
		{
			paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.FAILURE);
		}
	}


	/**
	 * This method creates a new Payment Transaction Model after Payment /// Commented as it is not used anymore after
	 * TPR-629
	 *
	 * @param cart
	 * @param orderStatusResponse
	 * @param paymentTransactionEntryList
	 * @param paymentTransactionList
	 * @return List<PaymentTransactionModel>
	 */
	//	@Override
	//	public List<PaymentTransactionModel> createPaymentTransaction(final AbstractOrderModel cart,
	//			final GetOrderStatusResponse orderStatusResponse, final List<PaymentTransactionEntryModel> paymentTransactionEntryList,
	//			final List<PaymentTransactionModel> paymentTransactionList)
	//	{
	//		final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
	//		if (null != cart.getPaymentInfo())
	//		{
	//			paymentTransactionModel.setInfo(cart.getPaymentInfo());
	//		}
	//
	//		if (StringUtils.isNotEmpty(orderStatusResponse.getOrderId()))
	//		{
	//			paymentTransactionModel.setCode(orderStatusResponse.getOrderId() + "-" + System.currentTimeMillis());
	//		}
	//		else
	//		{
	//			paymentTransactionModel.setCode(cart.getCode() + "-" + System.currentTimeMillis());//TODO:Change required when Order Ref No. is ready
	//		}
	//
	//		paymentTransactionModel.setCreationtime(new Date());
	//		paymentTransactionModel.setCurrency(cart.getCurrency());
	//		paymentTransactionModel.setEntries(paymentTransactionEntryList);
	//		paymentTransactionModel.setOrder(cart);
	//
	//		if (StringUtils.isNotEmpty(orderStatusResponse.getGatewayId().toString()))
	//		{
	//			paymentTransactionModel.setPaymentProvider(orderStatusResponse.getGatewayId().toString());
	//		}
	//
	//		paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(cart.getTotalPrice().doubleValue()));
	//
	//		if (StringUtils.isNotEmpty(orderStatusResponse.getTxnId()))
	//		{
	//			paymentTransactionModel.setRequestId(orderStatusResponse.getTxnId());
	//		}
	//
	//		if (StringUtils.isNotEmpty(orderStatusResponse.getBankErrorCode()))
	//		{
	//			paymentTransactionModel.setRequestToken(orderStatusResponse.getBankErrorCode());
	//		}
	//
	//		//the flag is used to identify whether all the entries in the PaymentTransactionModel are successful or not. If all are successful then flag is set as true and status against paymentTransactionModel is set as success
	//		boolean flag = false;
	//		for (final PaymentTransactionEntryModel entry : paymentTransactionEntryList)
	//		{
	//			if (entry.getTransactionStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
	//			{
	//				flag = true;
	//			}
	//			else
	//			{
	//				flag = false;
	//			}
	//		}
	//		if (flag) //flag == true
	//		{
	//			paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
	//		}
	//		else
	//		{
	//			paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.FAILURE);
	//		}
	//		try
	//		{
	//			getModelService().save(paymentTransactionModel);
	//			paymentTransactionList.add(paymentTransactionModel);
	//		}
	//		catch (final ModelSavingException e)
	//		{
	//			LOG.error("Exception while saving payment transaction with ", e);
	//		}
	//
	//		return paymentTransactionList;
	//	}






	/**
	 * This method creates a new Payment Transaction Model after Payment
	 *
	 * @param cart
	 * @param orderStatusResponse
	 * @param paymentTransactionEntryList
	 * @return PaymentTransactionModel
	 */
	@Override
	public PaymentTransactionModel createPaymentTransaction(final AbstractOrderModel cart,
			final GetOrderStatusResponse orderStatusResponse, final List<PaymentTransactionEntryModel> paymentTransactionEntryList)
	//Changed to abstractOrderModel for TPR-629
	{
		final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
		try
		{
			if (null != cart.getPaymentInfo())
			{
				paymentTransactionModel.setInfo(cart.getPaymentInfo());
			}

			if (StringUtils.isNotEmpty(orderStatusResponse.getOrderId()))
			{
				paymentTransactionModel.setCode(orderStatusResponse.getOrderId() + "-" + System.currentTimeMillis());
			}
			else
			{
				paymentTransactionModel.setCode(cart.getCode() + "-" + System.currentTimeMillis());//TODO:Change required when Order Ref No. is ready
			}

			paymentTransactionModel.setCreationtime(new Date());
			paymentTransactionModel.setCurrency(cart.getCurrency());
			paymentTransactionModel.setEntries(paymentTransactionEntryList);
			paymentTransactionModel.setOrder(cart);

			if (StringUtils.isNotEmpty(orderStatusResponse.getGatewayId().toString()))
			{
				paymentTransactionModel.setPaymentProvider(orderStatusResponse.getGatewayId().toString());
			}

			//paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(cart.getTotalPrice().doubleValue()));
			paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(orderStatusResponse.getAmount().doubleValue()));


			if (StringUtils.isNotEmpty(orderStatusResponse.getTxnId()))
			{
				paymentTransactionModel.setRequestId(orderStatusResponse.getTxnId());
			}

			if (StringUtils.isNotEmpty(orderStatusResponse.getBankErrorCode()))
			{
				paymentTransactionModel.setRequestToken(orderStatusResponse.getBankErrorCode());
			}

			//the flag is used to identify whether all the entries in the PaymentTransactionModel are successful or not. If all are successful then flag is set as true and status against paymentTransactionModel is set as success
			boolean flag = false;
			for (final PaymentTransactionEntryModel entry : paymentTransactionEntryList)
			{
				if (entry.getTransactionStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					flag = true;
				}
				else
				{
					flag = false;
				}
			}
			if (flag) //flag == true
			{
				paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			else
			{
				paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.FAILURE);
			}

			//Check handled to remove concurrent scenario - TPR-629
			if (null == cart.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(cart.getStatus()))
			{
				getModelService().save(paymentTransactionModel);
				//paymentTransactionList.add(paymentTransactionModel);
			}
			else if (null != cart.getPaymentInfo())
			{
				LOG.error("Order already has payment info -- not saving paymentTransaction>>>" + cart.getPaymentInfo().getCode());
			}
			else
			{
				LOG.error("Payment_Timeout order status for orderCode>>>" + cart.getCode());
			}

		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_ERR_LOG + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_ERR_LOG + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return paymentTransactionModel;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 *
	 * @desc SprintPaymentFixes:-:- To handle missing paymentTransaction for specific order
	 */
	@Override
	public PaymentTransactionModel createPaymentTranFromSubmitOrderJob(final OrderModel order,
			final GetOrderStatusResponse orderStatusResponse, final List<PaymentTransactionEntryModel> paymentTransactionEntryList)
	{
		final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
		try
		{
			if (null != order.getPaymentInfo())
			{
				paymentTransactionModel.setInfo(order.getPaymentInfo());
			}

			if (StringUtils.isNotEmpty(orderStatusResponse.getOrderId()))
			{
				paymentTransactionModel.setCode(orderStatusResponse.getOrderId() + "-" + System.currentTimeMillis());
			}
			else
			{
				paymentTransactionModel.setCode(order.getCode() + "-" + System.currentTimeMillis());
			}

			paymentTransactionModel.setCreationtime(new Date());
			paymentTransactionModel.setCurrency(order.getCurrency());
			paymentTransactionModel.setEntries(paymentTransactionEntryList);
			paymentTransactionModel.setOrder(order);

			if (StringUtils.isNotEmpty(orderStatusResponse.getGatewayId().toString()))
			{
				paymentTransactionModel.setPaymentProvider(orderStatusResponse.getGatewayId().toString());
			}

			//paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(cart.getTotalPrice().doubleValue()));
			paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(orderStatusResponse.getAmount().doubleValue()));


			if (StringUtils.isNotEmpty(orderStatusResponse.getTxnId()))
			{
				paymentTransactionModel.setRequestId(orderStatusResponse.getTxnId());
			}

			if (StringUtils.isNotEmpty(orderStatusResponse.getBankErrorCode()))
			{
				paymentTransactionModel.setRequestToken(orderStatusResponse.getBankErrorCode());
			}

			//the flag is used to identify whether all the entries in the PaymentTransactionModel are successful or not. If all are successful then flag is set as true and status against paymentTransactionModel is set as success
			boolean flag = false;
			for (final PaymentTransactionEntryModel entry : paymentTransactionEntryList)
			{
				if (entry.getTransactionStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					flag = true;
				}
				else
				{
					flag = false;
				}
			}
			if (flag) //flag == true
			{
				paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			else
			{
				paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.FAILURE);
			}

			//Check handled to remove concurrent scenario - TPR-629
			getModelService().save(paymentTransactionModel);
			//paymentTransactionList.add(paymentTransactionModel);
		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_ERR_LOG + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_ERR_LOG + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return paymentTransactionModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * 
	 * @desc SprintPaymentFixes:-:- To handle missing paymentTransaction for specific order
	 */
	@Override
	public List<PaymentTransactionEntryModel> createPaymentTranEntryFromSubmitOrderJob(
			final GetOrderStatusResponse getOrderStatusResponse, final OrderModel order, final Entry<String, Double> entry,
			final List<PaymentTransactionEntryModel> paymentTransactionEntryList)
	{

		final PaymentTransactionEntryModel paymentTransactionEntry = getModelService().create(PaymentTransactionEntryModel.class);
		try
		{
			if (null != getOrderStatusResponse.getPaymentGatewayResponse())
			{
				if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentGatewayResponse().getRootReferenceNumber()))
				{
					paymentTransactionEntry.setSubscriptionID(getOrderStatusResponse.getPaymentGatewayResponse()
							.getRootReferenceNumber());
				}

				paymentTransactionEntry.setTransactionStatusDetails(null != getOrderStatusResponse.getPaymentGatewayResponse()
						.getResponseMessage() ? getOrderStatusResponse.getPaymentGatewayResponse().getResponseMessage() : "");
				paymentTransactionEntry
						.setRequestToken(null != getOrderStatusResponse.getPaymentGatewayResponse().getTxnId() ? getOrderStatusResponse
								.getPaymentGatewayResponse().getTxnId() : "");
				paymentTransactionEntry.setRequestId(null != getOrderStatusResponse.getPaymentGatewayResponse()
						.getExternalGatewayTxnId() ? getOrderStatusResponse.getPaymentGatewayResponse().getExternalGatewayTxnId() : "");

				if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentGatewayResponse().getAuthIdCode()))
				{
					paymentTransactionEntry.setCode(getOrderStatusResponse.getPaymentGatewayResponse().getAuthIdCode() + "-"
							+ System.currentTimeMillis());
				}
				else
				{
					paymentTransactionEntry.setCode(order.getCode() + "-" + System.currentTimeMillis());
				}
			}
			else
			{
				paymentTransactionEntry.setCode(order.getCode() + "-" + System.currentTimeMillis());
			}

			final String orderStatus = getOrderStatusResponse.getStatus();

			if (StringUtils.isNotEmpty(orderStatus))
			{
				setEntryStatus(orderStatus, paymentTransactionEntry);
			}

			paymentTransactionEntry.setAmount(BigDecimal.valueOf(getOrderStatusResponse.getAmount().doubleValue()));

			paymentTransactionEntry.setTime(new Date());
			paymentTransactionEntry.setCurrency(order.getCurrency());


			if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentMethodType())
					&& getOrderStatusResponse.getPaymentMethodType().equalsIgnoreCase("NB"))
			{
				final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("Netbanking", order.getStore());
				paymentTransactionEntry.setPaymentMode(paymenttype);
			}
			else if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentMethodType())
					&& getOrderStatusResponse.getPaymentMethodType().equalsIgnoreCase("CARD"))
			{
				final String cardType = getOrderStatusResponse.getCardResponse().getCardType();
				if (StringUtils.isEmpty(cardType))
				{
					//:- Check with Barun Da wt to set the payment Mode, Since paymntmode is mandatory
					final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("UNKNOWN", order.getStore());
					paymentTransactionEntry.setPaymentMode(paymenttype);
				}
				else
				{
					if (cardType.equalsIgnoreCase("DEBIT"))
					{
						final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("Debit Card", order.getStore());
						paymentTransactionEntry.setPaymentMode(paymenttype);
					}
					else if (cardType.equalsIgnoreCase("CREDIT"))
					{
						final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("Credit Card", order.getStore());
						paymentTransactionEntry.setPaymentMode(paymenttype);
					}
					else
					{
						final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode("EMI", order.getStore());
						paymentTransactionEntry.setPaymentMode(paymenttype);
					}
				}

			}

			//Check handled to remove concurrent scenario - TPR-629
			getModelService().save(paymentTransactionEntry);
			paymentTransactionEntryList.add(paymentTransactionEntry);
		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_EXC_LOG + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_EXC_LOG + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return paymentTransactionEntryList;

	}





	//Getters and setters

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
	 * @return the mplPaymentDao
	 */
	public MplPaymentDao getMplPaymentDao()
	{
		return mplPaymentDao;
	}

	/**
	 * @param mplPaymentDao
	 *           the mplPaymentDao to set
	 */
	public void setMplPaymentDao(final MplPaymentDao mplPaymentDao)
	{
		this.mplPaymentDao = mplPaymentDao;
	}





}
