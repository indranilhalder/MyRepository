/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringReader;
import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.wsdto.TicketUpdateRequestXML;
import com.tisl.mpl.wsdto.TicketUpdateResponseXML;
import com.tisl.mpl.xml.pojo.CODSelfShipmentRequest;
import com.tisl.mpl.xml.pojo.CODSelfShipmentResponse;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest;
import com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse;
import com.tisl.mpl.xml.pojo.RTSAndRSSReturnInfoRequest;
import com.tisl.mpl.xml.pojo.RTSAndRSSReturnInfoResponse;



/**
 * @author TCS
 *
 */
public class MplOrderCancelClientServiceImpl implements MplOrderCancelClientService
{

	/**
	 * 
	 */
	private static final String LOG_MSG_XML_RESPONSE = "xml response<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";

	/**
	 * 
	 */
	private static final String X_TENANT_ID = "x-tenantId";

	/**
	 * 
	 */
	private static final String SINGLE = "single";

	/**
	 * 
	 */
	private static final String APPLICATION_XML = "application/xml";

	/**
	 * 
	 */
	private static final String LOG_MSG_POSTING_TO = "Posting to >>>>>>>>>>>>>>>>>>>>";

	@Autowired
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(MplOrderCancelClientServiceImpl.class);

	/*
	 * Call Service to understand if the Order can be cancelled as per order status
	 * 
	 * @param cancelOrderRequest
	 * 
	 * @return MplOrderIsCancellableResponse
	 */
	@Override
	public MplOrderIsCancellableResponse orderCancelDataToOMS(final MplCancelOrderRequest cancelOrderRequest)
	{

		final Client client = Client.create();
		WebResource webResource = null;
		Marshaller marshaller = null;
		String xmlString;
		ClientResponse response = null;
		MplOrderIsCancellableResponse responsefromOMS = null;
		final StringWriter stringWriter = new StringWriter();
		if (null != client && null != configurationService)
		{
			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString("oms.ordercancel.endpoint.url")).build());
		}

		try
		{
			if (null != webResource)
			{
				final JAXBContext context = JAXBContext.newInstance(MplCancelOrderRequest.class);
				if (null != context)
				{
					marshaller = context.createMarshaller();
				}
				if (null != marshaller)
				{
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				}
				if (null != marshaller)
				{
					marshaller.marshal(cancelOrderRequest, stringWriter);
				}
				xmlString = stringWriter.toString();
				LOG.debug(xmlString);
				LOG.debug(LOG_MSG_POSTING_TO + webResource.getURI());
				response = webResource.type(MediaType.APPLICATION_XML.intern()).accept(APPLICATION_XML.intern()).header(X_TENANT_ID.intern(), SINGLE.intern())
						.entity(xmlString).post(ClientResponse.class);
			}
			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(LOG_MSG_XML_RESPONSE + output);
				final JAXBContext jaxbContext = JAXBContext.newInstance(MplOrderIsCancellableResponse.class);
				Unmarshaller unmarshaller = null;
				if (null != jaxbContext)
				{
					unmarshaller = jaxbContext.createUnmarshaller();
				}
				final StringReader reader = new StringReader(output);
				responsefromOMS = (MplOrderIsCancellableResponse) unmarshaller.unmarshal(reader);
				LOG.debug(response);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		return responsefromOMS;
	}

	
	/**
	 * @author Techouts
	 * 
	 * @param cancelOrderRequest
	 * @return RTSAndRSSReturnInfoResponse
	 */
	@Override
	public RTSAndRSSReturnInfoResponse orderReturnInfoOMS(RTSAndRSSReturnInfoRequest cancelOrderRequest)
	{
		
		if(LOG.isDebugEnabled())
		{
			
			JAXBContext context;
			try
			{
				context = JAXBContext.newInstance(RTSAndRSSReturnInfoRequest.class);
			
	        Marshaller m = context.createMarshaller();

	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

	        StringWriter sw = new StringWriter();
	        m.marshal(cancelOrderRequest, sw);
	        
	        LOG.debug(sw.toString());
			}
			catch (JAXBException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final Client client = Client.create();
		WebResource webResource = null;
		Marshaller marshaller = null;
		String xmlString;
		ClientResponse response = null;
		RTSAndRSSReturnInfoResponse responsefromOMS = null;
		final StringWriter stringWriter = new StringWriter();
		if (null != client && null != configurationService)
		{
					webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString("oms.returns.returninfo.url")).build());
		}
	
		try
		{
			if (null != webResource)
			{
				final JAXBContext context = JAXBContext.newInstance(RTSAndRSSReturnInfoRequest.class);
				if (null != context)
				{
					marshaller = context.createMarshaller();
				}
				if (null != marshaller)
				{
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				}
				if (null != marshaller)
				{
					marshaller.marshal(cancelOrderRequest, stringWriter);
				}
				xmlString = stringWriter.toString();
				LOG.debug(xmlString);
				LOG.debug(LOG_MSG_POSTING_TO + webResource.getURI());
				response = webResource.type(MediaType.APPLICATION_XML.intern()).accept(APPLICATION_XML.intern()).header(X_TENANT_ID.intern(), SINGLE.intern())
						.entity(xmlString).post(ClientResponse.class);
			}
			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(LOG_MSG_XML_RESPONSE + output);
				final JAXBContext jaxbContext = JAXBContext.newInstance(MplOrderIsCancellableResponse.class);
				Unmarshaller unmarshaller = null;
				if (null != jaxbContext)
				{
					unmarshaller = jaxbContext.createUnmarshaller();
				}
				final StringReader reader = new StringReader(output);
				responsefromOMS = (RTSAndRSSReturnInfoResponse) unmarshaller.unmarshal(reader);
				LOG.debug(response);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		return responsefromOMS;
	}


	/**
	 * @param codPaymentInfo
	 * @return CODSelfShipmentResponse
	 */
	@Override
	public CODSelfShipmentResponse codPaymentInfoToFICO(CODSelfShipmentRequest codPaymentInfo)
	{
		
		if(LOG.isDebugEnabled())
		{
			
			JAXBContext context;
			try
			{
				context = JAXBContext.newInstance(CODSelfShipmentRequest.class);
			
	        Marshaller m = context.createMarshaller();

	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

	        StringWriter sw = new StringWriter();
	        m.marshal(codPaymentInfo, sw);
	        
	        LOG.debug(sw.toString());
			}
			catch (JAXBException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		final Client client = Client.create();
		WebResource webResource = null;
		Marshaller marshaller = null;
		String xmlString;
		ClientResponse response = null;
		CODSelfShipmentResponse responsefromFICO = null;
		final StringWriter stringWriter = new StringWriter();
		if (null != client && null != configurationService)
		{
			
			final String password = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_PASSWORD);
			final String userId = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_USERID);
			client.addFilter(new HTTPBasicAuthFilter(userId, password));
			
			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString("oms.returns.paymentInfo.endpoint.url")).build());
		}

		try
		{
			if (null != webResource)
			{
				final JAXBContext context = JAXBContext.newInstance(CODSelfShipmentRequest.class);
				if (null != context)
				{
					marshaller = context.createMarshaller();
				}
				if (null != marshaller)
				{
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				}
				if (null != marshaller)
				{
					marshaller.marshal(codPaymentInfo, stringWriter);
				}
				xmlString = stringWriter.toString();
				LOG.debug(xmlString);
				LOG.debug(LOG_MSG_POSTING_TO + webResource.getURI());
				response = webResource.type(MediaType.APPLICATION_XML.intern()).accept(APPLICATION_XML.intern()).header(X_TENANT_ID.intern(), SINGLE.intern())
						.entity(xmlString).post(ClientResponse.class);
			}
			if (null != response)
			{
				if (response.getStatus() == 200 || response.getStatus() == 201)
				{
					responsefromFICO = new CODSelfShipmentResponse();
					responsefromFICO.setSuccess("Success");
					return responsefromFICO;
				}
				final String output = response.getEntity(String.class);
				LOG.debug(LOG_MSG_XML_RESPONSE + output);
				final JAXBContext jaxbContext = JAXBContext.newInstance(MplOrderIsCancellableResponse.class);
				Unmarshaller unmarshaller = null;
				if (null != jaxbContext)
				{
					unmarshaller = jaxbContext.createUnmarshaller();
				}
				final StringReader reader = new StringReader(output);
				responsefromFICO = (CODSelfShipmentResponse) unmarshaller.unmarshal(reader);
				LOG.debug(response);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		return responsefromFICO;
		
	}
	
	/**
	 * @param ticketUpdateData
	 * @return TicketUpdateResponseXML
	 */
	@Override
	public TicketUpdateResponseXML updateCRMTicket(TicketUpdateRequestXML ticketUpdateData)
	{
	
		final Client client = Client.create();
		WebResource webResource = null;
		Marshaller marshaller = null;
		String xmlString;
		ClientResponse response = null;
		TicketUpdateResponseXML responsefromCRM = null;
		final StringWriter stringWriter = new StringWriter();
		
		LOG.debug("********************Ticket create CRM called********************************** ");
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.TICKET_CREATE_URL))
		{
			final String password = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_PASSWORD);
			final String userId = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_USERID);
			client.addFilter(new HTTPBasicAuthFilter(userId, password));
			webResource = client.resource(UriBuilder
					.fromUri(configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.CUSTOMERMASTER_TICKETUPDATE))
					.build());
			LOG.debug("::::::::::::::::::::::::::::::::::::webResource:::" + webResource);
		}
		
		try
		{
			if (null != webResource)
			{
				final JAXBContext context = JAXBContext.newInstance(TicketUpdateRequestXML.class);
				if (null != context)
				{
					marshaller = context.createMarshaller();
				}
				if (null != marshaller)
				{
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				}
				if (null != marshaller)
				{
					marshaller.marshal(ticketUpdateData, stringWriter);
				}
				xmlString = stringWriter.toString();
				LOG.debug(xmlString);
				LOG.debug(LOG_MSG_POSTING_TO + webResource.getURI());
				response = webResource.type(MediaType.APPLICATION_XML.intern()).accept(APPLICATION_XML.intern()).header(X_TENANT_ID.intern(), SINGLE.intern())
						.entity(xmlString).post(ClientResponse.class);
			}
			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(LOG_MSG_XML_RESPONSE + output);
				final JAXBContext jaxbContext = JAXBContext.newInstance(TicketUpdateResponseXML.class);
				Unmarshaller unmarshaller = null;
				if (null != jaxbContext)
				{
					unmarshaller = jaxbContext.createUnmarshaller();
				}
				final StringReader reader = new StringReader(output);
				responsefromCRM = (TicketUpdateResponseXML) unmarshaller.unmarshal(reader);
				LOG.debug(response);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		return responsefromCRM;
	}


	
}
