/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
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
			final CartModel cart, final Map.Entry<String, Double> entry,
			final List<PaymentTransactionEntryModel> paymentTransactionEntryList)
	{

		final PaymentTransactionEntryModel paymentTransactionEntry = getModelService().create(PaymentTransactionEntryModel.class);
		if (null != getOrderStatusResponse.getPaymentGatewayResponse())
		{
			if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentGatewayResponse().getRootReferenceNumber()))
			{
				paymentTransactionEntry
						.setSubscriptionID(getOrderStatusResponse.getPaymentGatewayResponse().getRootReferenceNumber());
			}

			paymentTransactionEntry
					.setTransactionStatusDetails(getOrderStatusResponse.getPaymentGatewayResponse().getResponseMessage());
			paymentTransactionEntry.setRequestToken(getOrderStatusResponse.getPaymentGatewayResponse().getTxnId());
			paymentTransactionEntry.setRequestId(getOrderStatusResponse.getPaymentGatewayResponse().getExternalGatewayTxnId());

			if (StringUtils.isNotEmpty(getOrderStatusResponse.getPaymentGatewayResponse().getAuthIdCode()))
			{
				paymentTransactionEntry
						.setCode(getOrderStatusResponse.getPaymentGatewayResponse().getAuthIdCode() + "-" + System.currentTimeMillis());
			}
			else
			{
				paymentTransactionEntry.setCode(cart.getCode() + "-" + System.currentTimeMillis());
			}
		}
		else
		{
			paymentTransactionEntry.setCode(cart.getCode() + "-" + System.currentTimeMillis());
		}

		final String orderStatus = getOrderStatusResponse.getStatus();

		if (StringUtils.isNotEmpty(orderStatus))
		{
			setEntryStatus(orderStatus, paymentTransactionEntry);
		}

		paymentTransactionEntry.setAmount(BigDecimal.valueOf(entry.getValue().doubleValue()));
		paymentTransactionEntry.setTime(new Date());
		paymentTransactionEntry.setCurrency(cart.getCurrency());

		final PaymentTypeModel paymenttype = getMplPaymentDao().getPaymentMode(entry.getKey());
		paymentTransactionEntry.setPaymentMode(paymenttype);

		try
		{
			getModelService().save(paymentTransactionEntry);
			paymentTransactionEntryList.add(paymentTransactionEntry);
		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_EXC_LOG + e);
			throw new ModelSavingException(e + ": Exception while saving payment transaction entry with");
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
	 * This method creates a new Payment Transaction Model after Payment
	 *
	 * @param cart
	 * @param orderStatusResponse
	 * @param paymentTransactionEntryList
	 * @param paymentTransactionList
	 * @return List<PaymentTransactionModel>
	 */
	@Override
	public List<PaymentTransactionModel> createPaymentTransaction(final CartModel cart,
			final GetOrderStatusResponse orderStatusResponse, final List<PaymentTransactionEntryModel> paymentTransactionEntryList,
			final List<PaymentTransactionModel> paymentTransactionList)
	{
		final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
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

		paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(cart.getTotalPriceWithConv().doubleValue()));

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
		try
		{
			getModelService().save(paymentTransactionModel);
			paymentTransactionList.add(paymentTransactionModel);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving payment transaction with ", e);
		}

		return paymentTransactionList;
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

