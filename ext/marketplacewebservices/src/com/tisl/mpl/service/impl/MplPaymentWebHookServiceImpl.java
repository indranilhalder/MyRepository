/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.enums.EBSResponseStatus;
import com.tisl.mpl.core.enums.EBSRiskLevelEnum;
import com.tisl.mpl.core.model.JuspayCardResponseModel;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.JuspayPGResponseModel;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplPaymentServiceImpl;
import com.tisl.mpl.service.MplPaymentWebHookService;


/**
 * @author TCS
 *
 */
public class MplPaymentWebHookServiceImpl implements MplPaymentWebHookService
{
	private static final Logger LOG = Logger.getLogger(MplPaymentServiceImpl.class);
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
		final JuspayOrderStatusModel orderResponse = getModelService().create(JuspayOrderStatusModel.class);
		final JuspayPGResponseModel pgresponse = getModelService().create(JuspayPGResponseModel.class);
		final JuspayCardResponseModel cardResponse = getModelService().create(JuspayCardResponseModel.class);
		final JuspayEBSResponseModel ebsResponse = getModelService().create(JuspayEBSResponseModel.class);

		try
		{
			// Fetch Details from Json for Webhook Response
			final JSONObject jusspayWebhookRes = (JSONObject) JSONValue.parse(webhookData);

			// Set cardResponce into Webhook Response
			juspayWebhook.setEventId((String) jusspayWebhookRes.get(MarketplacewebservicesConstants.EVENTID));

			// Declare Date format (yyyy/MM/dd)
			final DateFormat sdf = new SimpleDateFormat(MarketplacewebservicesConstants.DATEFORMAT);

			// Converting String into util date
			final Date creationDate = sdf.parse((String) jusspayWebhookRes.get(MarketplacewebservicesConstants.DATECREATED));
			juspayWebhook.setDateCreated(creationDate);
			juspayWebhook.setEventName((String) jusspayWebhookRes.get(MarketplacewebservicesConstants.EVENTNAME));

			// Fetch Details from Json for Order Response
			final JSONObject jobjectContent = (JSONObject) jusspayWebhookRes.get(MarketplacewebservicesConstants.CONTENT);
			final JSONObject jobjectOrdrRes = (JSONObject) jobjectContent.get(MarketplacewebservicesConstants.ORDER);

			// Set cardResponce into orderResponse
			orderResponse.setMerchantId((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.MERCHENTID));
			orderResponse.setOrderId((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.ORDERID));
			orderResponse.setCustomerId((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.CUSTOMERID));
			orderResponse.setCustomerEmail((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.CUSTOMEREMAIL));
			orderResponse.setCustomerPhone((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.CUSTOMERPHONE));
			orderResponse.setProductId((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.PRODUCTID));
			orderResponse.setAmount(getDoubleValue(jobjectOrdrRes.get(MarketplacewebservicesConstants.AMOUNT)));
			orderResponse.setCurrency((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.CURRENCY));
			orderResponse.setRefunded((Boolean) jobjectOrdrRes.get(MarketplacewebservicesConstants.REFUNDEDED));
			orderResponse.setAmountRefunded(getDoubleValue(jobjectOrdrRes.get(MarketplacewebservicesConstants.AMOUNTREFUNDED)));
			orderResponse.setReturnUrl((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.RETURNURL));
			orderResponse.setStatus((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.STATUS));
			orderResponse.setStatusId((Long) jobjectOrdrRes.get(MarketplacewebservicesConstants.STATUSID));
			orderResponse.setTxnId((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.TXNID));
			orderResponse.setGatewayId((Long) jobjectOrdrRes.get(MarketplacewebservicesConstants.GATEWAYID));
			orderResponse.setBankErroCode((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.BANKERRORCODE));
			orderResponse.setBankErroMessage((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.BANKERRORMESSAGE));
			orderResponse.setUdf1((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD1));
			orderResponse.setUdf2((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD2));
			orderResponse.setUdf3((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD3));
			orderResponse.setUdf4((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD4));
			orderResponse.setUdf5((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD5));
			orderResponse.setUdf6((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD6));
			orderResponse.setUdf7((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD7));
			orderResponse.setUdf8((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD8));
			orderResponse.setUdf9((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD9));
			orderResponse.setUdf10((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD10));

			// Fetch Details from Json for cardResponse
			final JSONObject jobjectCard = (JSONObject) jobjectOrdrRes.get(MarketplacewebservicesConstants.CARD);
			// Set values into cardResponse
			cardResponse.setLastFourDigits((String) jobjectCard.get(MarketplacewebservicesConstants.LASTFOURDIGITS));
			cardResponse.setCardIsin((String) jobjectCard.get(MarketplacewebservicesConstants.CARDISIN));
			cardResponse.setExpMonth((String) jobjectCard.get(MarketplacewebservicesConstants.EXPIRYMONTH));
			cardResponse.setExpYear((String) jobjectCard.get(MarketplacewebservicesConstants.EXPIRYYEAR));
			cardResponse.setNameOnCard((String) jobjectCard.get(MarketplacewebservicesConstants.NAMEOFCARD));
			cardResponse.setCardType((String) jobjectCard.get(MarketplacewebservicesConstants.CARDTYPE));
			cardResponse.setCardBrand((String) jobjectCard.get(MarketplacewebservicesConstants.CARDBRAND));
			cardResponse.setCardIssuer((String) jobjectCard.get(MarketplacewebservicesConstants.CARDISSUER));
			getModelService().save(cardResponse);

			// Set cardResponce into orderResponse
			orderResponse.setCardResponse(cardResponse);

			// Fetch Details from Json for paymentGatewayResponse
			final JSONObject jobjectPayment = (JSONObject) jobjectOrdrRes
					.get(MarketplacewebservicesConstants.PAYMENT_GATEWAY_RESPONSE);

			// Set values into paymentGatewayResponse
			pgresponse.setCreated((String) jobjectPayment.get(MarketplacewebservicesConstants.CREATED));
			pgresponse.setEpgTxnId((String) jobjectPayment.get(MarketplacewebservicesConstants.EPGTXNID));
			pgresponse.setRootReferenceNumber((String) jobjectPayment.get(MarketplacewebservicesConstants.RRN));
			pgresponse.setAuthIdCode((String) jobjectPayment.get(MarketplacewebservicesConstants.AUTHIDCODE));
			pgresponse.setTxnId((String) jobjectPayment.get(MarketplacewebservicesConstants.TXNID_P));
			pgresponse.setResponseCode((String) jobjectPayment.get(MarketplacewebservicesConstants.RESPCODE));
			pgresponse.setResponseMessage((String) jobjectPayment.get(MarketplacewebservicesConstants.RESPMSG));
			getModelService().save(pgresponse);

			// Set paymentGatewayResponse into orderStatusResponseJuspay
			orderResponse.setPgResponse(pgresponse);

			// Fetch Details from Json for EBS Response
			final JSONObject jobjectEBS = (JSONObject) jobjectOrdrRes.get(MarketplacewebservicesConstants.RISK);

			// Set values into EBSResponse
			ebsResponse.setEbs_bin_country((String) jobjectEBS.get(MarketplacewebservicesConstants.EBSBINCOUNTRY));
			ebsResponse.setEbsRiskLevel((EBSRiskLevelEnum) jobjectEBS.get(MarketplacewebservicesConstants.EBSRISKLEVEL));
			final Long scoreLong = (Long) jobjectEBS.get(MarketplacewebservicesConstants.EBSRISKPERCENTAGE);
			final Double scoreDouble = Double.valueOf(scoreLong.doubleValue());
			ebsResponse.setEbsRiskPercentage(scoreDouble.toString());
			ebsResponse
					.setEbsRiskStatus((EBSResponseStatus) jusspayWebhookRes.get(MarketplacewebservicesConstants.EBSPAYMENTSTATUS));


			getModelService().save(ebsResponse);
			// Set EBS Response into orderStatusResponseJuspay
			orderResponse.setEbsResponse(ebsResponse);

			getModelService().save(orderResponse);
			juspayWebhook.setOrderStatus(orderResponse);
			getModelService().save(juspayWebhook);

		}
		catch (final ModelSavingException e)
		{
			e.printStackTrace();

			LOG.error("Exception while saving payment info model with " + e);
			throw new ModelSavingException(e + " :Exception while saving payment info model with");
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

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
			LOG.error(MarketplacewebservicesConstants.DECIMALERROR);
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
