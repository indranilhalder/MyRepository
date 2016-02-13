/**
 *
 */
package com.tisl.mpl.sms;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.springframework.remoting.RemoteAccessException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.OrderUpdateSmsProcessModel;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.wsdto.SmsECommReqWsDTO;


/**
 * @author TCS
 *
 */
public class MplSendSMSService implements SendSmsService
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(MplSendSMSService.class);

	/**
	 * @description Method is called to implement SMS Service
	 * @param SendSMSRequestData
	 * @throws JAXBException
	 * @throws RemoteAccessException
	 */


	@Override
	public void sendSMS(final SendSMSRequestData request) throws JAXBException
	{
		try
		{
			// Checking if SMS Service is enabled
			if (Boolean.valueOf(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.SMS_SERVICE_ENABLED))
					.equals(Boolean.TRUE))
			{
				// Setting  the username & password for connecting the SMS Server
				final Client client = Client.create();
				final String username = configurationService.getConfiguration().getString(
						MarketplacecclientservicesConstants.SMS_SERVICE_USERNAME);
				final String password = configurationService.getConfiguration().getString(
						MarketplacecclientservicesConstants.SMS_SERVICE_PASSWORD);

				final HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(username, password);
				client.addFilter(authFilter);
				client.addFilter(new LoggingFilter());


				final WebResource webResource = client
						.resource(UriBuilder.fromUri(
								configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.SMS_SERVICE_URL))
								.build());
				//Validate if logs are enabled then print the details
				if (LOG.isInfoEnabled())
				{
					LOG.info("Sending SMS using below details");
					LOG.info("Sender = " + request.getSenderID());
					LOG.info("Number = " + request.getRecipientPhoneNumber());
					LOG.info("Content = " + request.getContent());
				}

				if (!request.getRecipientPhoneNumber().isEmpty())
				{
					final JAXBContext context = JAXBContext.newInstance(SmsECommReqWsDTO.class);
					final Marshaller marshaller = context.createMarshaller();

					final JSONMarshaller marshallers = JSONJAXBContext.getJSONMarshaller(marshaller, context);
					marshallers.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, Boolean.TRUE);
					marshallers.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

					final Map smsSendMap = new HashMap<String, Map<String, String>>();
					final Map sendSmsWsMap = new HashMap<String, String>();

					//Putting values in the map SMSECommReq



					sendSmsWsMap.put(MarketplacecclientservicesConstants.SMS_SENDER_TAG, request.getSenderID());
					sendSmsWsMap.put(MarketplacecclientservicesConstants.SMS_MOBILE_NO_TAG, request.getRecipientPhoneNumber());

					/*
					 * sendSmsWsMap.put(MarketplacecclientservicesConstants.SMS_MESSAGE_TAG, URLEncoder
					 * .encode(request.getContent().replaceAll(" ",
					 * MarketplacecclientservicesConstants.SMS_MESSAGE_BLANK_SPACE_ONE), "UTF-8") .replaceAll("%7C",
					 * MarketplacecclientservicesConstants.SMS_MESSAGE_BLANK_SPACE_TWO));
					 * smsSendMap.put(MarketplacecclientservicesConstants.SMS_ECOMMREQ_TAG, sendSmsWsMap);
					 */

					sendSmsWsMap.put(MarketplacecclientservicesConstants.SMS_MESSAGE_TAG, request.getContent());

					smsSendMap.put(MarketplacecclientservicesConstants.SMS_ECOMMREQ_TAG, sendSmsWsMap);



					final ObjectMapper obj = new ObjectMapper();
					final String jsonString = obj.writeValueAsString(smsSendMap);

					final ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
							.entity(jsonString).post(ClientResponse.class);


					LOG.debug("Output from Server .... \n" + response);

					if (response.getStatus() != 200)
					{
						throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
					}
					else
					{
						LOG.info("SMS has been Sent ................");
					}
				}
			}
			else
			{
				LOG.info("SMS service is not enabled ................");
			}

		}
		catch (final RemoteAccessException rae)
		{
			LOG.error("Error = " + rae.getMessage(), rae);
		}
		catch (final Exception rae)
		{
			LOG.error("Error = " + rae.getMessage(), rae);
		}

	}

	/**
	 * @param orderUpdateSmsProcessModel
	 * @throws JAXBException
	 * @description This method is used to send SMS for orderStatus Update
	 */
	@Override
	public void sendSMSForOrderStatus(final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel) throws JAXBException
	{

		LOG.info("Setting SendSMSRequestData ....");
		final SendSMSRequestData smsRequestData = new SendSMSRequestData();
		smsRequestData.setSenderID(orderUpdateSmsProcessModel.getSenderID());
		smsRequestData.setContent(orderUpdateSmsProcessModel.getMessage());
		smsRequestData.setRecipientPhoneNumber(orderUpdateSmsProcessModel.getRecipientPhoneNumber());
		this.sendSMS(smsRequestData);
	}

}