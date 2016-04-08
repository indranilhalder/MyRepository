/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.wsdto.AddressInfoDTO;
import com.tisl.mpl.wsdto.TicketMasterXMLData;
import com.tisl.mpl.wsdto.TicketlineItemsXMLData;



/**
 * @author TCS
 * @Description: Generate XML Data when ticket has been created
 * @param sendTicketRequestData
 * @retun void
 */
@Component
@Qualifier("ticketCreate")
public class TicketCreationCRMserviceImpl implements TicketCreationCRMservice
{

	private static final Logger LOG = Logger.getLogger(TicketCreationCRMserviceImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public void ticketCreationModeltoWsDTO(final SendTicketRequestData sendTicketRequestData) throws JAXBException

	{
		final AddressInfoDTO addressInfo = new AddressInfoDTO();
		try
		{
			LOG.debug("....called ticket create to crm interface.....");
			final TicketMasterXMLData ticket = new TicketMasterXMLData();
			if (null != sendTicketRequestData.getCustomerID())
			{
				ticket.setCustomerID(sendTicketRequestData.getCustomerID());
				LOG.debug("ticket create: customer Id>>>>> " + sendTicketRequestData.getCustomerID());
			}
			if (null != sendTicketRequestData.getOrderId())
			{
				ticket.setOrderId(sendTicketRequestData.getOrderId());
				LOG.debug("ticket create:order Id>>>>> " + sendTicketRequestData.getOrderId());
			}
			if (null != sendTicketRequestData.getSubOrderId())
			{
				ticket.setSubOrderId(sendTicketRequestData.getSubOrderId());
				LOG.debug("ticket create:suborder Id>>>>> " + sendTicketRequestData.getSubOrderId());
			}
			if (null != sendTicketRequestData.getTicketType())
			{
				ticket.setTicketType(sendTicketRequestData.getTicketType());
				LOG.debug("ticket create:TicketType>>>>> " + sendTicketRequestData.getTicketType());
			}
			if (null != sendTicketRequestData.getTicketSubType())
			{
				ticket.setTicketSubType(sendTicketRequestData.getTicketSubType());
				LOG.debug("ticket create:TicketSubType>>>>> " + sendTicketRequestData.getTicketSubType());

			}
			if (null != sendTicketRequestData.getSource())
			{
				ticket.setSource(sendTicketRequestData.getSource());
				LOG.debug("ticket create:Ticket Source>>>>> " + sendTicketRequestData.getSource());

			}

			if (null != sendTicketRequestData.getAlternateContactName())
			{
				ticket.setAlternateContactName(sendTicketRequestData.getAlternateContactName());
				LOG.debug("ticket create:Ticket AlternateContactName>>>>> " + sendTicketRequestData.getAlternateContactName());
			}

			if (null != sendTicketRequestData.getAlternatePhoneNo())
			{
				ticket.setAlternatePhoneNo(sendTicketRequestData.getAlternatePhoneNo());
				LOG.debug("ticket create:Ticket AlternatePhoneNo>>>>> " + sendTicketRequestData.getAlternatePhoneNo());

			}

			if (null != sendTicketRequestData.getRefundType())
			{
				ticket.setRefundType(sendTicketRequestData.getRefundType());
				LOG.debug("ticket create:Ticket RefundType>>>>> " + sendTicketRequestData.getRefundType());

			}
			if (null != sendTicketRequestData.getReturnCategory())
			{
				ticket.setReturnCategory(sendTicketRequestData.getReturnCategory());
				LOG.debug("ticket create:Ticket ReturnCategory>>>>> " + sendTicketRequestData.getReturnCategory());

			}
			if (null != sendTicketRequestData.getAddressInfo())
			{
				addressInfo.setShippingFirstName(sendTicketRequestData.getAddressInfo().getShippingFirstName());
				addressInfo.setShippingLastName(sendTicketRequestData.getAddressInfo().getShippingLastName());
				addressInfo.setPhoneNo(sendTicketRequestData.getAddressInfo().getPhoneNo());
				addressInfo.setAddress1(sendTicketRequestData.getAddressInfo().getAddress1());
				addressInfo.setAddress2(sendTicketRequestData.getAddressInfo().getAddress2());
				addressInfo.setAddress3(sendTicketRequestData.getAddressInfo().getAddress3());
				addressInfo.setCountry(sendTicketRequestData.getAddressInfo().getCountry());
				addressInfo.setCity(sendTicketRequestData.getAddressInfo().getCity());
				addressInfo.setState(sendTicketRequestData.getAddressInfo().getState());
				addressInfo.setPincode(sendTicketRequestData.getAddressInfo().getPincode());
				addressInfo.setLandmark(sendTicketRequestData.getAddressInfo().getLandmark());
			}
			ticket.setAddressInfo(addressInfo);
			final List<SendTicketLineItemData> sendTicketLineItemDataList = sendTicketRequestData.getLineItemDataList();
			final ArrayList<TicketlineItemsXMLData> ticketlineItemsXMLDataList = new ArrayList<TicketlineItemsXMLData>();
			if (null != sendTicketLineItemDataList)
			{
				for (final SendTicketLineItemData sendTicketLineItemData : sendTicketLineItemDataList)
				{
					final TicketlineItemsXMLData ticketLineObj = new TicketlineItemsXMLData();
					if (null != sendTicketLineItemData.getLineItemId())
					{
						ticketLineObj.setLineItemId(sendTicketLineItemData.getLineItemId());
					}
					if (ticket.getTicketType().equalsIgnoreCase(MarketplacecclientservicesConstants.CANCEL))
					{
						ticketLineObj.setCancelReasonCode(sendTicketLineItemData.getCancelReasonCode());
					}
					else
					{
						ticketLineObj.setReturnReasonCode(sendTicketLineItemData.getReturnReasonCode());
					}
					ticketlineItemsXMLDataList.add(ticketLineObj);
				}
				ticket.setLineItemDataList(ticketlineItemsXMLDataList);
			}

			ticketCreationCRM(ticket);
		}
		catch (final JAXBException e)
		{
			LOG.info(MarketplacecclientservicesConstants.JAXB_EXCEPTION);
			throw e;
		}
		catch (final Exception ex)
		{
			LOG.info(MarketplacecclientservicesConstants.EXCEPTION_IS);
			throw ex;
		}
	}

	@Override
	public void ticketCreationCRM(final TicketMasterXMLData ticketMasterXml) throws JAXBException
	{

		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
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
					.fromUri(configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.TICKET_CREATE_URL))
					.build());
			LOG.debug("::::::::::::::::::::::::::::::::::::webResource:::" + webResource);
		}
		final JAXBContext context = JAXBContext.newInstance(TicketMasterXMLData.class);
		final Marshaller m = context.createMarshaller(); //for pretty-print XML in JAXB
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		LOG.info("Marshalling to file!!!!");
		final StringWriter sw = new StringWriter();
		m.marshal(ticketMasterXml, sw);
		LOG.debug(" <<<<<<<<<<<<<< CRM Ticket Xml File >>>>>>>>>>>>>>>> " + m);
		final String xmlString = sw.toString();
		LOG.debug(xmlString);
		if (null != xmlString && webResource != null)
		{
			response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
					.post(ClientResponse.class);
			LOG.debug(":::::::::::::::::::::response:::" + response);
		}
		final String output = response.getEntity(String.class);
		LOG.debug("output " + output);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.TicketCreationCRMservice#ticketCreationModeltoXMLData(com.tisl.mpl.data.
	 * SendTicketRequestData)
	 */
	@Override
	public TicketMasterXMLData ticketCreationModeltoXMLData(final SendTicketRequestData sendTicketRequestData)
	{
		try
		{
			LOG.debug("....called ticket create to crm interface.....");
			final TicketMasterXMLData ticket = new TicketMasterXMLData();
			if (null != sendTicketRequestData.getCustomerID())
			{
				ticket.setCustomerID(sendTicketRequestData.getCustomerID());
				LOG.debug("ticket create: customer Id>>>>> " + sendTicketRequestData.getCustomerID());
			}
			if (null != sendTicketRequestData.getOrderId())
			{
				ticket.setOrderId(sendTicketRequestData.getOrderId());
				LOG.debug("ticket create:order Id>>>>> " + sendTicketRequestData.getOrderId());
			}
			if (null != sendTicketRequestData.getSubOrderId())
			{
				ticket.setSubOrderId(sendTicketRequestData.getSubOrderId());
				LOG.debug("ticket create:suborder Id>>>>> " + sendTicketRequestData.getSubOrderId());
			}
			if (null != sendTicketRequestData.getTicketType())
			{
				ticket.setTicketType(sendTicketRequestData.getTicketType());
				LOG.debug("ticket create:TicketType>>>>> " + sendTicketRequestData.getTicketType());
			}
			if (null != sendTicketRequestData.getRefundType())
			{
				ticket.setRefundType(sendTicketRequestData.getRefundType());
				LOG.debug("ticket create:RefundType>>>>> " + sendTicketRequestData.getRefundType());

			}
			if (null != sendTicketRequestData.getReturnCategory())
			{
				ticket.setReturnCategory(sendTicketRequestData.getReturnCategory());
				LOG.debug("ticket create: ReturnCategory>>>>> " + sendTicketRequestData.getReturnCategory());

			}


			if (null != sendTicketRequestData.getAlternateContactName())
			{
				ticket.setAlternateContactName(sendTicketRequestData.getAlternateContactName());
				LOG.debug("ticket create: AlternateContactName>>>>> " + sendTicketRequestData.getAlternateContactName());

			}
			if (null != sendTicketRequestData.getAlternatePhoneNo())
			{
				ticket.setAlternatePhoneNo(sendTicketRequestData.getAlternatePhoneNo());
				LOG.debug("ticket create: AlternatePhoneNo>>>>> " + sendTicketRequestData.getAlternatePhoneNo());

			}
			if (null != sendTicketRequestData.getSource())
			{
				ticket.setSource(sendTicketRequestData.getSource());
				LOG.debug("ticket create: Source>>>>> " + sendTicketRequestData.getSource());

			}
			if (null != sendTicketRequestData.getTicketSubType())
			{
				ticket.setTicketSubType(sendTicketRequestData.getTicketSubType());
				LOG.debug("ticket create:TicketSubType>>>>> " + sendTicketRequestData.getTicketSubType());

			}



			final List<SendTicketLineItemData> sendTicketLineItemDataList = sendTicketRequestData.getLineItemDataList();
			final ArrayList<TicketlineItemsXMLData> ticketlineItemsXMLDataList = new ArrayList<TicketlineItemsXMLData>();
			if (null != sendTicketLineItemDataList)
			{
				for (final SendTicketLineItemData sendTicketLineItemData : sendTicketLineItemDataList)
				{
					final TicketlineItemsXMLData ticketLineObj = new TicketlineItemsXMLData();
					if (null != sendTicketLineItemData.getLineItemId())
					{
						ticketLineObj.setLineItemId(sendTicketLineItemData.getLineItemId());
					}
					if (ticket.getTicketType().equalsIgnoreCase(MarketplacecclientservicesConstants.CANCEL))
					{
						ticketLineObj.setCancelReasonCode(sendTicketLineItemData.getCancelReasonCode());
					}
					else
					{
						ticketLineObj.setReturnReasonCode(sendTicketLineItemData.getReturnReasonCode());
					}
					ticketlineItemsXMLDataList.add(ticketLineObj);
				}
				ticket.setLineItemDataList(ticketlineItemsXMLDataList);
			}
			return ticket;
		}
		catch (final Exception ex)
		{
			LOG.info(MarketplacecclientservicesConstants.EXCEPTION_IS);
			throw ex;
		}


	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.TicketCreationCRMservice#createTicketInCRM(com.tisl.mpl.wsdto.TicketMasterXMLData)
	 */
	@Override
	public int createTicketInCRM(final String xmlString) throws JAXBException
	{
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
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
					.fromUri(configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.TICKET_CREATE_URL))
					.build());
			LOG.debug("::::::::::::::::::::::::::::::::::::webResource:::" + webResource);
		}
		LOG.debug(xmlString);
		if (null != xmlString && webResource != null)
		{
			response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
					.post(ClientResponse.class);
			LOG.debug(":::::::::::::::::::::response:::" + response);
		}

		final int responseCode = response.getStatus();

		LOG.debug("output " + responseCode);

		LOG.debug("The Response status is  " + response.getClientResponseStatus().getFamily());

		return responseCode;
	}

	@Override
	public String createCRMRequestXml(final TicketMasterXMLData ticketMasterXml) throws JAXBException
	{
		final JAXBContext context = JAXBContext.newInstance(TicketMasterXMLData.class);
		final Marshaller m = context.createMarshaller(); //for pretty-print XML in JAXB
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		LOG.info("Marshalling to file!!!!");
		final StringWriter sw = new StringWriter();
		m.marshal(ticketMasterXml, sw);
		final String xmlString = sw.toString();
		LOG.debug(xmlString);
		return xmlString;
	}

}
