/**
 *
 */
package com.tisl.mpl.service.impl;


import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.nio.charset.Charset;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.samsung.wsdto.OrderResponseWsDTO;
import com.tisl.mpl.service.MplSendOrderFromCommerceToSamsung;


/**
 * @author TCS
 *
 */
public class MplSendOrderFromCommerceToSamsungImpl implements MplSendOrderFromCommerceToSamsung
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "modelService")
	private ModelService modelService;

	private static final String ENCODING = "UTF-8";

	private static final Logger LOG = Logger.getLogger(MplSendOrderFromCommerceToSamsungImpl.class);

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	//For TPR-5667
	@Override
	public JSONObject postResponseToSamsung(final OrderResponseWsDTO orderResponse, final OrderModel orderModel)
			throws ClientEtailNonBusinessExceptions
	{
		String responseString = null;
		final JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;

		//Object to JSON in String
		final ObjectMapper mapper = new ObjectMapper();

		final Client client = Client.create();

		try
		{
			final String jsonInString = mapper.writeValueAsString(orderResponse);
			orderModel.setSamsungOrderRequestJSON(jsonInString);
			LOG.info(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD + "SAMSUNG JSON request is : " + jsonInString);

			final WebResource webResource = client.resource(getConfigurationService().getConfiguration().getString(
					MarketplacecclientservicesConstants.SAMSUNG_API_URL));
			final String apiUsername = getConfigurationService().getConfiguration().getString(
					MarketplacecclientservicesConstants.SAMSUNG_API_USERNAME);
			final String apiPassword = getConfigurationService().getConfiguration().getString(
					MarketplacecclientservicesConstants.SAMSUNG_API_PASSWORD);

			final String connectionTimeout = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.SAMSUNG_CONNECTION_TIMEOUT, "1000").trim();
			final String readTimeout = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.SAMSUNG_READ_TIMEOUT, "1000").trim();
			final String httpErrorCode = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.SAMSUNG_HTTP_ERROR_CODE, "400,401,402,403,404,500,501,502,503")
					.trim();

			client.setConnectTimeout(Integer.valueOf(connectionTimeout));
			client.setReadTimeout(Integer.valueOf(readTimeout));
			client.addFilter(new HTTPBasicAuthFilter(apiUsername, apiPassword));

			webResource.type(MediaType.APPLICATION_JSON);
			webResource.accept(MediaType.APPLICATION_JSON);
			webResource.header("Content-Length", String.valueOf(jsonInString.getBytes(Charset.forName(ENCODING)).length));
			final ClientResponse response = webResource.put(ClientResponse.class, jsonInString);


			LOG.info(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD + "samsung response code :" + response.getStatus());
			if (httpErrorCode.contains(String.valueOf(response.getStatus())))
			{
				if (String.valueOf(response.getStatus()).startsWith("4"))
				{
					throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.S0001_EXCEP);
				}
				else if (String.valueOf(response.getStatus()).startsWith("5"))
				{
					throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.S0002_EXCEP);
				}
				else
				{
					throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.S0000_EXCEP);
				}
			}

			responseString = response.getEntity(String.class);
			LOG.debug(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD + "Response from samsung end :" + responseString);
			// Read the response
			/*
			 * jsonObject = (JSONObject) parser.parse(responseString.toString());
			 * orderModel.setSamsungOrderResponseJSON(responseString.toString());
			 */
			//SONAR FIX
			jsonObject = (JSONObject) parser.parse(responseString);
			orderModel.setSamsungOrderResponseJSON(responseString);
			modelService.save(orderModel);

		}
		catch (final ModelSavingException mse)
		{
			LOG.error(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD + "ModelSavingException "
					+ MarketplacecclientservicesConstants.EXCEPTION_IS, mse);
			throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.S0003_EXCEP, mse);
		}
		catch (final ClientHandlerException cex)
		{
			LOG.error(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD + "ClientHandlerException "
					+ MarketplacecclientservicesConstants.EXCEPTION_IS, cex);
			throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.S0000_EXCEP, cex);
		}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD + "ClientEtailNonBusinessExceptions - "
					+ ex.getMessage());
			throw ex;
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD + "Exception occured while placing order due to  "
					+ ex.getMessage());
			throw new ClientEtailNonBusinessExceptions(ex);
		}
		return jsonObject;
	}
}
