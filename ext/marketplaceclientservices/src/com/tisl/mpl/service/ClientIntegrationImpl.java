/**
 *
 */
package com.tisl.mpl.service;


import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.webform.model.WebFormTicketProcessModel;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.wsdto.DuplicateCheckResponse;


/**
 * @author TCS
 *
 */



public class ClientIntegrationImpl implements ClientIntegration
{
	private static final Logger LOG = Logger.getLogger(TicketCreationCRMserviceImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource
	private BusinessProcessService businessProcessService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.ClientIntegration#sendWebFormTicket()
	 */
	@Override
	public boolean sendWebFormTicket(final MplWebCrmTicketModel mplWebCrmTicketModel)
	{
		try
		{
			final WebFormTicketProcessModel webFormTicketProcessModel = (WebFormTicketProcessModel) businessProcessService
					.createProcess("WebFormTicket-process-" + System.currentTimeMillis(), "WebFormTicket-process");
			webFormTicketProcessModel.setMplWebCrmTicket(mplWebCrmTicketModel);
			businessProcessService.startProcess(webFormTicketProcessModel);
		}
		catch (final Exception e)
		{
			LOG.error(e.getStackTrace());
		}
		return true;
	}

	/**
	 * This method is created to check the duplicate tickets in CRM, using a realtime web service call (TPR-5989)
	 *
	 * @param stringXml
	 * @return success/failure message
	 * @throws JAXBException
	 */
	@Override
	public String checkDuplicateWebFormTicket(final String stringXml) throws JAXBException
	{
		LOG.info("Executing method checkDuplicateWebFormTicket in clientIntegrationImpl java class >>>>>>>");
		DuplicateCheckResponse duplicateCheckResponse = null;
		String responseMsg = "success";
		try
		{
			final String globalResponse = configurationService.getConfiguration().getString("global.client.reponse");
			final Client client = Client.create();
			final String username = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.WEBFORM_DUPLICATECHECK_USERNAME);
			final String password = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.WEBFORM_DUPLICATECHECK_PASSWORD);

			LOG.debug("========== Step:1==========");

			final HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(username, password);
			client.addFilter(authFilter);
			client.addFilter(new LoggingFilter());


			final WebResource webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.WEBFORM_DUPLICATECHECK_URL))
					.build());
			LOG.debug("========== Step:2==========");
			final ClientResponse response = webResource.type(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
					.entity(stringXml).post(ClientResponse.class);
			LOG.debug("========== Step:3==========" + response.getStatus());
			if (checkResponseStatus(String.valueOf(response.getStatus()), globalResponse))
			{
				duplicateCheckResponse = response.getEntity(DuplicateCheckResponse.class);
				if (null != duplicateCheckResponse && null != duplicateCheckResponse.getTicketPresent()
						&& duplicateCheckResponse.getTicketPresent().equalsIgnoreCase("Y"))
				{
					responseMsg = "failure";
				}

			}
			return responseMsg;

		}
		catch (final Exception ex)
		{
			LOG.error(ex.getStackTrace());
			return "Failed to check the duplicate ticket in crm via webservice";
		}
	}


	/**
	 * Method to check the global response code of the web service response
	 *
	 * @param receivedResponse
	 * @param globalResponse
	 * @return true/false
	 */
	private boolean checkResponseStatus(final String receivedResponse, final String globalResponse)
	{
		return (globalResponse.indexOf(receivedResponse) == -1) ? false : true;
	}
}
