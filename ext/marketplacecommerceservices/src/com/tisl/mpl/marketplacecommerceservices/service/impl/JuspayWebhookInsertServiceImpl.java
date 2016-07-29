/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.EBSResponseStatus;
import com.tisl.mpl.core.enums.EBSRiskLevelEnum;
import com.tisl.mpl.core.model.JuspayCardResponseModel;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.JuspayPGResponseModel;
import com.tisl.mpl.core.model.JuspayRefundResponseModel;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayWebhookInsertService;


/**
 * @author TCS
 *
 */
public class JuspayWebhookInsertServiceImpl implements JuspayWebhookInsertService
{

	private static final Logger LOG = Logger.getLogger(JuspayWebhookInsertServiceImpl.class);
	@Resource
	private ModelService modelService;

	/**
	 * This method is used to insert data in Webhook table when Juspay will post data to Commerce everytime there will a
	 * Payment/Refund Successful
	 *
	 * @param webhookData
	 */
	@Override
	public void insertWekhookData(final String webhookData) throws ParseException
	{
		final JuspayWebhookModel juspayWebhook = getModelService().create(JuspayWebhookModel.class);
		JuspayOrderStatusModel orderResponse = getModelService().create(JuspayOrderStatusModel.class);
		JuspayPGResponseModel pgresponse = getModelService().create(JuspayPGResponseModel.class);
		JuspayCardResponseModel cardResponse = getModelService().create(JuspayCardResponseModel.class);
		JuspayEBSResponseModel ebsResponse = getModelService().create(JuspayEBSResponseModel.class);

		try
		{
			JSONObject juspayWebhookRes = null;
			if (null != webhookData)
			{
				// Fetch Details from Json for Webhook Response
				juspayWebhookRes = (JSONObject) JSONValue.parse(webhookData);
			}

			if (null != (juspayWebhookRes.get(MarketplacecommerceservicesConstants.EVENTID)))
			{
				juspayWebhook.setEventId((String) juspayWebhookRes.get(MarketplacecommerceservicesConstants.EVENTID));
			}

			// Declare Date format (yyyy-MM-dd)
			final DateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.DATEFORMAT);

			Date creationDate = null;
			if (null != (juspayWebhookRes.get(MarketplacecommerceservicesConstants.DATECREATED)))
			{
				// Converting String into util date
				creationDate = sdf.parse((String) juspayWebhookRes.get(MarketplacecommerceservicesConstants.DATECREATED));
			}

			juspayWebhook.setDateCreated(creationDate);

			if (null != (juspayWebhookRes.get(MarketplacecommerceservicesConstants.EVENTNAME)))
			{
				juspayWebhook.setEventName((String) juspayWebhookRes.get(MarketplacecommerceservicesConstants.EVENTNAME));
			}

			JSONObject jobjectContent = null;
			if (null != juspayWebhookRes.get(MarketplacecommerceservicesConstants.CONTENT))
			{
				jobjectContent = (JSONObject) juspayWebhookRes.get(MarketplacecommerceservicesConstants.CONTENT);
			}

			JSONObject jobjectOrdrRes = null;
			if (null != (jobjectContent.get(MarketplacecommerceservicesConstants.ORDER)))
			{
				jobjectOrdrRes = (JSONObject) jobjectContent.get(MarketplacecommerceservicesConstants.ORDER);
			}

			//validating order response
			orderResponse = validateOrderResponse(orderResponse, jobjectOrdrRes);

			if (StringUtils.isNotEmpty(orderResponse.getPaymentMethodType())
					&& !orderResponse.getPaymentMethodType().equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_METHOD_NB))
			{
				JSONObject jobjectCard = null;
				if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CARD))
				{
					// Fetch Details from Json for cardResponse
					jobjectCard = (JSONObject) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CARD);
				}

				if (null != jobjectCard)
				{
					// Set values into cardResponse
					cardResponse = validateCardResponse(cardResponse, jobjectCard);

					// Set cardResponce into orderResponse
					orderResponse.setCardResponse(cardResponse);

				}

				JSONObject jobjectEBS = null;
				if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.RISK))
				{
					// Fetch Details from Json for EBS Response
					jobjectEBS = (JSONObject) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.RISK);
				}

				if (null != jobjectEBS)
				{
					// Set values into EBSResponse
					ebsResponse = validateEbsResponse(ebsResponse, jobjectEBS);

					// Set EBS Response into orderStatusResponseJuspay
					orderResponse.setEbsResponse(ebsResponse);
				}

			}

			JSONObject jobjectPayment = null;
			if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.PAYMENT_GATEWAY_RESPONSE))
			{
				// Fetch Details from Json for paymentGatewayResponse
				jobjectPayment = (JSONObject) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.PAYMENT_GATEWAY_RESPONSE);
			}

			if (null != jobjectPayment)
			{
				// Set values into paymentGatewayResponse
				pgresponse = validatePgResponse(pgresponse, jobjectPayment);

				// Set paymentGatewayResponse into orderStatusResponseJuspay
				orderResponse.setPgResponse(pgresponse);
			}

			ArrayList<JSONObject> refundList = new ArrayList<JSONObject>();
			if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.REFUNDS))
			{
				//Refund block capture
				refundList = (ArrayList<JSONObject>) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.REFUNDS);
			}

			List<JuspayRefundResponseModel> refunds = null;
			if (null != refundList)
			{
				refunds = validateRefundResponse(refundList);
				//iterating through the list and adding in the refund list in JuspayOrderStatus in Webhook

			}

			orderResponse.setRefunds(refunds);
			getModelService().save(orderResponse);

			juspayWebhook.setOrderStatus(orderResponse);
			getModelService().save(juspayWebhook);

		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving payment info model with ", e);
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}

	}

	/**
	 *
	 * @param orderResponse
	 * @param jobjectOrdrRes
	 */
	private JuspayOrderStatusModel validateOrderResponse(final JuspayOrderStatusModel orderResponse,
			final JSONObject jobjectOrdrRes)
	{
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.MERCHENTID))
		{
			orderResponse.setMerchantId((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.MERCHENTID));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.ORDERID))
		{
			orderResponse.setOrderId((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.ORDERID));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CUSTOMER_ID))
		{
			orderResponse.setCustomerId((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CUSTOMER_ID));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CUSTOMEREMAIL))
		{
			orderResponse.setCustomerEmail((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CUSTOMEREMAIL));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CUSTOMERPHONE))
		{
			orderResponse.setCustomerPhone((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CUSTOMERPHONE));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.PRODUCTID))
		{
			orderResponse.setProductId((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.PRODUCTID));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.AMOUNT))
		{
			orderResponse.setAmount(getDoubleValue(jobjectOrdrRes.get(MarketplacecommerceservicesConstants.AMOUNT)));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CURRENCY))
		{
			orderResponse.setCurrency((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.CURRENCY));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.REFUNDEDED))
		{
			orderResponse.setRefunded((Boolean) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.REFUNDEDED));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.AMOUNTREFUNDED))
		{
			orderResponse.setAmountRefunded(getDoubleValue(jobjectOrdrRes.get(MarketplacecommerceservicesConstants.AMOUNTREFUNDED)));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.RETURN_URL))
		{
			orderResponse.setReturnUrl((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.RETURN_URL));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.ORDERSTATUS))
		{
			orderResponse.setStatus((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.ORDERSTATUS));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.STATUSID))
		{
			orderResponse.setStatusId((Long) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.STATUSID));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.TXNID))
		{
			orderResponse.setTxnId((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.TXNID));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.GATEWAYID))
		{
			orderResponse.setGatewayId((Long) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.GATEWAYID));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.BANKERRORCODE))
		{
			orderResponse.setBankErroCode((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.BANKERRORCODE));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.BANKERRORMESSAGE))
		{
			orderResponse.setBankErroMessage((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.BANKERRORMESSAGE));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD1))
		{
			orderResponse.setUdf1((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD1));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD2))
		{
			orderResponse.setUdf2((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD2));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD3))
		{
			orderResponse.setUdf3((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD3));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD4))
		{
			orderResponse.setUdf4((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD4));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD5))
		{
			orderResponse.setUdf5((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD5));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD6))
		{
			orderResponse.setUdf6((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD6));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD7))
		{
			orderResponse.setUdf7((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD7));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD8))
		{
			orderResponse.setUdf8((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD8));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD9))
		{
			orderResponse.setUdf9((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD9));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD10))
		{
			orderResponse.setUdf10((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.UFD10));
		}
		//Extra field for Payment mode type handled
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.PAYMENTMETHOD))
		{
			orderResponse.setPaymentMethod((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.PAYMENTMETHOD));
		}
		if (null != jobjectOrdrRes.get(MarketplacecommerceservicesConstants.PAYMENTMETHODTYPE))
		{
			orderResponse.setPaymentMethodType((String) jobjectOrdrRes.get(MarketplacecommerceservicesConstants.PAYMENTMETHODTYPE));
		}

		return orderResponse;
	}

	/**
	 *
	 * @param cardResponse
	 * @param jobjectCard
	 */
	private JuspayCardResponseModel validateCardResponse(final JuspayCardResponseModel cardResponse, final JSONObject jobjectCard)
	{
		if (null != jobjectCard.get(MarketplacecommerceservicesConstants.LASTFOURDIGITS))
		{
			cardResponse.setLastFourDigits((String) jobjectCard.get(MarketplacecommerceservicesConstants.LASTFOURDIGITS));
		}
		if (null != jobjectCard.get(MarketplacecommerceservicesConstants.CARDISIN))
		{
			cardResponse.setCardIsin((String) jobjectCard.get(MarketplacecommerceservicesConstants.CARDISIN));
		}
		if (null != jobjectCard.get(MarketplacecommerceservicesConstants.EXPIRYMONTH))
		{
			cardResponse.setExpMonth((String) jobjectCard.get(MarketplacecommerceservicesConstants.EXPIRYMONTH));
		}
		if (null != jobjectCard.get(MarketplacecommerceservicesConstants.EXPIRYYEAR))
		{
			cardResponse.setExpYear((String) jobjectCard.get(MarketplacecommerceservicesConstants.EXPIRYYEAR));
		}
		if (null != jobjectCard.get(MarketplacecommerceservicesConstants.NAMEOFCARD))
		{
			cardResponse.setNameOnCard((String) jobjectCard.get(MarketplacecommerceservicesConstants.NAMEOFCARD));
		}
		if (null != jobjectCard.get(MarketplacecommerceservicesConstants.CARDTYPE))
		{
			cardResponse.setCardType((String) jobjectCard.get(MarketplacecommerceservicesConstants.CARDTYPE));
		}
		if (null != jobjectCard.get(MarketplacecommerceservicesConstants.CARDBRAND))
		{
			cardResponse.setCardBrand((String) jobjectCard.get(MarketplacecommerceservicesConstants.CARDBRAND));
		}
		if (null != jobjectCard.get(MarketplacecommerceservicesConstants.CARDISSUER))
		{
			cardResponse.setCardIssuer((String) jobjectCard.get(MarketplacecommerceservicesConstants.CARDISSUER));
		}
		//saving card response
		getModelService().save(cardResponse);

		return cardResponse;
	}

	/**
	 *
	 * @param pgresponse
	 * @param jobjectPayment
	 * @return
	 */
	private JuspayPGResponseModel validatePgResponse(final JuspayPGResponseModel pgresponse, final JSONObject jobjectPayment)
	{
		if (null != jobjectPayment.get(MarketplacecommerceservicesConstants.CREATED))
		{
			pgresponse.setCreated((String) jobjectPayment.get(MarketplacecommerceservicesConstants.CREATED));
		}
		if (null != jobjectPayment.get(MarketplacecommerceservicesConstants.EPGTXNID))
		{
			pgresponse.setEpgTxnId((String) jobjectPayment.get(MarketplacecommerceservicesConstants.EPGTXNID));
		}
		if (null != jobjectPayment.get(MarketplacecommerceservicesConstants.RRN))
		{
			pgresponse.setRootReferenceNumber((String) jobjectPayment.get(MarketplacecommerceservicesConstants.RRN));
		}
		if (null != jobjectPayment.get(MarketplacecommerceservicesConstants.AUTHIDCODE))
		{
			pgresponse.setAuthIdCode((String) jobjectPayment.get(MarketplacecommerceservicesConstants.AUTHIDCODE));
		}
		if (null != jobjectPayment.get(MarketplacecommerceservicesConstants.TXNID_P))
		{
			pgresponse.setTxnId((String) jobjectPayment.get(MarketplacecommerceservicesConstants.TXNID_P));
		}
		if (null != jobjectPayment.get(MarketplacecommerceservicesConstants.RESPCODE))
		{
			pgresponse.setResponseCode((String) jobjectPayment.get(MarketplacecommerceservicesConstants.RESPCODE));
		}
		if (null != jobjectPayment.get(MarketplacecommerceservicesConstants.RESPMSG))
		{
			pgresponse.setResponseMessage((String) jobjectPayment.get(MarketplacecommerceservicesConstants.RESPMSG));
		}
		//saving in pgresponse
		getModelService().save(pgresponse);

		return pgresponse;
	}

	/**
	 *
	 * @param ebsResponse
	 * @param jobjectEBS
	 * @return
	 */
	private JuspayEBSResponseModel validateEbsResponse(final JuspayEBSResponseModel ebsResponse, final JSONObject jobjectEBS)
	{
		if (null != jobjectEBS.get(MarketplacecommerceservicesConstants.EBSBINCOUNTRY))
		{
			ebsResponse.setEbs_bin_country((String) jobjectEBS.get(MarketplacecommerceservicesConstants.EBSBINCOUNTRY));
		}
		if (null != jobjectEBS.get(MarketplacecommerceservicesConstants.EBSRISKLEVEL))
		{
			ebsResponse.setEbsRiskLevel(EBSRiskLevelEnum.valueOf(jobjectEBS.get(MarketplacecommerceservicesConstants.EBSRISKLEVEL)
					.toString()));
		}
		if (null != jobjectEBS.get(MarketplacecommerceservicesConstants.EBSRISKPERCENTAGE))
		{
			final Long scoreLong = (Long) jobjectEBS.get(MarketplacecommerceservicesConstants.EBSRISKPERCENTAGE);
			final Double scoreDouble = Double.valueOf(scoreLong.doubleValue());
			ebsResponse.setEbsRiskPercentage(scoreDouble.toString());
		}
		if (null != jobjectEBS.get(MarketplacecommerceservicesConstants.EBSRISKSTATUS))
		{
			ebsResponse.setEbsRiskStatus(EBSResponseStatus.valueOf(jobjectEBS
					.get(MarketplacecommerceservicesConstants.EBSRISKSTATUS).toString().toUpperCase()));
		}

		getModelService().save(ebsResponse);

		return ebsResponse;
	}

	/**
	 *
	 * @param refundList
	 * @return
	 */
	private List<JuspayRefundResponseModel> validateRefundResponse(final ArrayList<JSONObject> refundList)
	{
		final List<JuspayRefundResponseModel> refunds = new ArrayList<JuspayRefundResponseModel>();
		final DateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.DATEFORMAT);
		for (final JSONObject refundObj : refundList)
		{
			final JuspayRefundResponseModel refund = new JuspayRefundResponseModel();
			if (null != refundObj.get(MarketplacecommerceservicesConstants.ID))
			{
				refund.setId((String) refundObj.get(MarketplacecommerceservicesConstants.ID));
			}
			if (null != refundObj.get(MarketplacecommerceservicesConstants.REF))
			{
				refund.setReference((String) refundObj.get(MarketplacecommerceservicesConstants.REF));
			}
			if (null != refundObj.get(MarketplacecommerceservicesConstants.AMOUNT))
			{
				Double amountDouble;
				if (refundObj.get(MarketplacecommerceservicesConstants.AMOUNT) instanceof Double)
				{
					amountDouble = (Double) refundObj.get(MarketplacecommerceservicesConstants.AMOUNT);
					refund.setAmount(amountDouble);
				}
				else
				{
					final Long amount = (Long) refundObj.get(MarketplacecommerceservicesConstants.AMOUNT);
					amountDouble = new Double(amount.doubleValue());
					refund.setAmount(amountDouble);
				}
			}

			Date createdDate = null;
			try
			{
				if (null != refundObj.get(MarketplacecommerceservicesConstants.CREATED))
				{
					createdDate = sdf.parse((String) refundObj.get(MarketplacecommerceservicesConstants.CREATED));
					refund.setCreated(createdDate);
				}
			}
			catch (final ParseException e)
			{
				LOG.error("Parse Exception= ", e);
			}

			if (null != refundObj.get(MarketplacecommerceservicesConstants.REFUND_STATUS))
			{
				refund.setStatus((String) refundObj.get(MarketplacecommerceservicesConstants.REFUND_STATUS));
			}
			if (null != refundObj.get(MarketplacecommerceservicesConstants.UNIQUE_REQUEST_ID))
			{
				refund.setUniqueRequestId((String) refundObj.get(MarketplacecommerceservicesConstants.UNIQUE_REQUEST_ID));
			}

			getModelService().save(refund);
			refunds.add(refund);
		}
		return refunds;
	}

	/**
	 * @description Double Check
	 * @param inputObject
	 * @return
	 */
	protected Double getDoubleValue(final Object inputObject)
	{
		if (inputObject instanceof Long)
		{
			return new Double(((Long) inputObject).doubleValue());
		}
		else if (inputObject instanceof Double)
		{
			return ((Double) inputObject);
		}
		else
		{
			LOG.error(MarketplacecommerceservicesConstants.DECIMALERROR);
			return null;
		}
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

}
