/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.service.MplPancardUploadService;
import com.tisl.mpl.wsdto.PancardCRMticketXMLData;
import com.tisl.mpl.xml.pojo.CRMpancardResponse;
import com.tisl.mpl.xml.pojo.LPAWBUpdate;
import com.tisl.mpl.xml.pojo.OrderLine;


/**
 * @author TCS
 *
 */
public class MplPancardUploadserviceImpl implements MplPancardUploadService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.MplPancardTicketCRMservice#ticketPancardModeltoDTO(de.hybris.platform.core.model.
	 * PancardInformationModel)
	 */
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	private static final Logger LOG = Logger.getLogger(MplPancardUploadserviceImpl.class);


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.MplPancardTicketCRMservice#createticketPancardModeltoDTO(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String createticketPancardModeltoDTO(final String orderreferancenumber, final String transactionid,
			final String newpath, final String pancardnumber) throws JAXBException
	{
		// YTODO Auto-generated method stub
		final PancardCRMticketXMLData ticket = new PancardCRMticketXMLData();
		String xmlString = null;
		try
		{

			ticket.setOrderId(orderreferancenumber);
			ticket.setTransactionId(transactionid);
			ticket.setPath(newpath);
			ticket.setPancardNumber(pancardnumber);

			final JAXBContext context = JAXBContext.newInstance(PancardCRMticketXMLData.class);
			final Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			//m.marshal(ticket, System.out);
			//m.marshal(ticket, new File("c:/temp/pancardticket.xml"));

			final StringWriter sw = new StringWriter();
			m.marshal(ticket, sw);

			xmlString = sw.toString();
			LOG.info("*******************" + xmlString);
			return xmlString;

		}

		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return xmlString;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.MplPancardTicketCRMservice#getCrmStatusForPancardDetails(de.hybris.platform.core.model.
	 * PancardInformationModel)
	 */
	@Override
	public CRMpancardResponse getCrmStatusForPancardDetails(final PancardInformationModel oModel)
	{
		// YTODO Auto-generated method stub
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		Unmarshaller unmarshaller = null;
		StringReader reader = null;
		final PancardCRMticketXMLData ticket = new PancardCRMticketXMLData();
		CRMpancardResponse crmpancardResponse = new CRMpancardResponse();

		try
		{
			ticket.setOrderId(oModel.getOrderId());
			ticket.setTransactionId(oModel.getTransactionId());
			ticket.setPancardNumber(oModel.getPancardNumber());
			ticket.setPath(oModel.getPath());

			final JAXBContext context = JAXBContext.newInstance(PancardCRMticketXMLData.class);
			final Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			//m.marshal(ticket, System.out);
			//.marshal(ticket, new File("c:/temp/pancardticket.xml"));

			final StringWriter sw = new StringWriter();
			m.marshal(ticket, sw);
			final String xmlString = sw.toString();
			LOG.info("*******************PANCARD REQUEST TO CRM**" + xmlString);


			LOG.debug("********************Ticket create CRM called for pancard********************************** ");
			if (null != configurationService
					&& null != configurationService.getConfiguration()
					&& null != configurationService.getConfiguration()
							.getString(MarketplacecclientservicesConstants.TICKET_CREATE_URL))
			{
				final String password = configurationService.getConfiguration().getString(
						MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_PASSWORD);
				final String userId = configurationService.getConfiguration().getString(
						MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_USERID);
				client.addFilter(new HTTPBasicAuthFilter(userId, password));
				webResource = client.resource(UriBuilder.fromUri(
						configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.TICKET_CREATE_URL))
						.build());
				LOG.debug("::::::::::::::::::::::::::::::::::::webResource:::" + webResource);
			}

			if (null != xmlString && webResource != null)
			{
				response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
						.post(ClientResponse.class);
			}

			final int output = response.getStatus();
			LOG.debug("*****************RESPONSE CODE FROM CRM FOR PANCARD" + output);

			//unmarshalling

			final JAXBContext jaxbContext = JAXBContext.newInstance(CRMpancardResponse.class);
			if (null != jaxbContext)
			{
				unmarshaller = jaxbContext.createUnmarshaller();
			}
			if (StringUtils.isNotEmpty(xmlString))
			{
				reader = new StringReader(xmlString);
			}
			if (null != reader && null != unmarshaller)
			{
				crmpancardResponse = (CRMpancardResponse) unmarshaller.unmarshal(reader);

			}
			LOG.debug("**************CRM Response FOR PANCARD:**" + crmpancardResponse);

		}

		catch (final Exception e)
		{
			e.printStackTrace();
		}

		return crmpancardResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.MplPancardTicketCRMservice#ticketPancardModeltoDTO(java.lang.String, java.lang.String)
	 */

	@Override
	public String ticketPancardModeltoDTO(final PancardInformationModel oModel, final String newpath, final String pancardnumber)
			throws JAXBException
	{
		// YTODO Auto-generated method stub
		final PancardCRMticketXMLData ticket = new PancardCRMticketXMLData();
		String xmlString = null;
		try
		{
			ticket.setOrderId(oModel.getOrderId());
			ticket.setTransactionId(oModel.getTransactionId());
			ticket.setPath(newpath);
			ticket.setPancardNumber(pancardnumber);

			final JAXBContext context = JAXBContext.newInstance(PancardCRMticketXMLData.class);
			final Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			//m.marshal(ticket, System.out);
			//.marshal(ticket, new File("c:/temp/pancardticket.xml"));
			final StringWriter sw = new StringWriter();
			m.marshal(ticket, sw);
			xmlString = sw.toString();
			LOG.info("*******************" + xmlString);

			return xmlString;

		}

		catch (final Exception e)
		{
			e.printStackTrace();
		}

		return xmlString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.MplPancardTicketCRMservice#createCrmTicket(java.lang.String)
	 */
	@Override
	public String createCrmTicket(final String ticket)
	{
		// YTODO Auto-generated method stub
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		LOG.debug("********************Ticket create CRM called for pancard********************************** ");
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.TICKET_CREATE_URL))
		{
			final String password = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_PASSWORD);
			final String userId = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_USERID);
			client.addFilter(new HTTPBasicAuthFilter(userId, password));
			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.TICKET_CREATE_URL)).build());
			LOG.debug("::::::::::::::::::::::::::::::::::::webResource:::" + webResource);
		}
		if (null != ticket && webResource != null)
		{
			response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(ticket)
					.post(ClientResponse.class);
			LOG.debug(":::::::::::::::::::::response from CRM For PANCARD:::" + response);
		}
		final String output = String.valueOf(response.getStatus());

		LOG.debug("**********CRM status for pancard ***" + output);
		return output;
	}

	//For sending pancard details to SP through PI and save data into database for new pancard entry
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.MplPancardUploadserviceImpl#generateXmlForPanCard(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public String generateXmlForPanCard(final List<PancardInformationModel> pModelList, final String orderReferanceNumber,
			final List<String> transactionidList, final String panCardImagePath) throws JAXBException
	{
		// YTODO Auto-generated method stub
		final LPAWBUpdate lpAwbUpdate = new LPAWBUpdate();
		final OrderLine orderLine = new OrderLine();
		final List<OrderLine> orderLineList = new ArrayList<OrderLine>();

		if (null != pModelList)
		{
			for (final PancardInformationModel pModel : pModelList)
			{
				orderLine.setInterfaceType(MarketplacecclientservicesConstants.PANCARD);
				if (StringUtils.isNotEmpty(panCardImagePath))
				{
					orderLine.setPancardPath(panCardImagePath);
				}
				if (StringUtils.isNotEmpty(pModel.getTransactionId()))
				{
					orderLine.setTransactionId(pModel.getTransactionId());
				}
				if (StringUtils.isNotEmpty(pModel.getStatus()))
				{
					if (MarketplacecclientservicesConstants.REJECTED.equalsIgnoreCase(pModel.getStatus())
							|| MarketplacecclientservicesConstants.NA.equalsIgnoreCase(pModel.getStatus()))
					{
						orderLine.setPancardStatus(MarketplacecclientservicesConstants.PENDING_FOR_VERIFICATION);
					}
					if (MarketplacecclientservicesConstants.APPROVED.equalsIgnoreCase(pModel.getStatus()))
					{
						orderLine.setPancardStatus(MarketplacecclientservicesConstants.APPROVED);
					}
				}
			}
			orderLineList.add(orderLine);
		}
		else
		{
			if (CollectionUtils.isNotEmpty(transactionidList))
			{
				for (final String transId : transactionidList)
				{
					orderLine.setInterfaceType(MarketplacecclientservicesConstants.PANCARD);
					if (StringUtils.isNotEmpty(transId))
					{
						orderLine.setTransactionId(transId);
					}
					if (StringUtils.isNotEmpty(panCardImagePath))
					{
						orderLine.setPancardPath(panCardImagePath);
					}
					orderLine.setPancardStatus(MarketplacecclientservicesConstants.PENDING_FOR_VERIFICATION);
					orderLineList.add(orderLine);
				}
			}
		}
		//		orderLine.setInterfaceType(MarketplacecclientservicesConstants.PANCARD);
		//		orderLine.setTransactionId(transactionid);
		//		orderLine.setPancardPath(panCardImagePath);
		//		orderLine.setPancardStatus(MarketplacecclientservicesConstants.PENDING_FOR_VERIFICATION);
		//orderLineList.add(orderLine);

		if (StringUtils.isNotEmpty(orderReferanceNumber))
		{
			lpAwbUpdate.setOrderId(orderReferanceNumber);
		}
		lpAwbUpdate.setOrderLine(orderLineList);

		final String response = getResponseFromPIforPanCard(lpAwbUpdate);
		return response;
	}

	/**
	 * @param lpAwbUpdate
	 * @return
	 * @throws JAXBException
	 */
	private String getResponseFromPIforPanCard(final LPAWBUpdate lpAwbUpdate) throws JAXBException
	{
		// YTODO Auto-generated method stub
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		String output = null;
		LOG.debug("********************Pancard upload PI-->SP called********************************** ");
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.PANCARD_UPLOAD_URL))
		{
			final String password = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_PASSWORD);

			final String userId = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_USERID);

			client.addFilter(new HTTPBasicAuthFilter(userId, password));

			webResource = client
					.resource(UriBuilder.fromUri(
							configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.PANCARD_UPLOAD_URL))
							.build());

			LOG.debug("::::::::::::::::::::::::::::::::::::webResource:::" + webResource);
		}
		final JAXBContext context = JAXBContext.newInstance(LPAWBUpdate.class);

		final Marshaller m = context.createMarshaller(); //for pretty-print XML in JAXB

		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		LOG.info("Marshalling to file!!!!");

		final StringWriter sw = new StringWriter();

		m.marshal(lpAwbUpdate, sw);

		final String xmlString = sw.toString();

		if (LOG.isDebugEnabled())
		{
			LOG.debug(xmlString);
		}
		if (null != xmlString && webResource != null)
		{
			response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
					.post(ClientResponse.class);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(":::::::::::::::::::::response:::" + response);
			}
		}
		if (response.getStatus() != 200 || response.getStatus() != 202)
		{
			output = "faliure";
		}
		else
		{
			output = response.getEntity(String.class);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("output " + output);
			}
		}
		return output;
	}
}
