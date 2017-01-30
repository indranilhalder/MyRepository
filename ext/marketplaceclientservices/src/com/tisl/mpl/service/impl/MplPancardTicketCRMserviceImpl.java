/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringReader;
import java.io.StringWriter;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.service.MplPancardTicketCRMservice;
import com.tisl.mpl.wsdto.PancardCRMticketXMLData;
import com.tisl.mpl.xml.pojo.CRMpancardResponse;


/**
 * @author TCS
 *
 */
public class MplPancardTicketCRMserviceImpl implements MplPancardTicketCRMservice
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.MplPancardTicketCRMservice#ticketPancardModeltoDTO(de.hybris.platform.core.model.
	 * PancardInformationModel)
	 */
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	private static final Logger LOG = Logger.getLogger(MplPancardTicketCRMserviceImpl.class);


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.MplPancardTicketCRMservice#createticketPancardModeltoDTO(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createticketPancardModeltoDTO(final String orderreferancenumber, final String newpath, final String customername,
			final String pancardnumber) throws JAXBException
	{
		// YTODO Auto-generated method stub
		final PancardCRMticketXMLData ticket = new PancardCRMticketXMLData();

		try
		{

			ticket.setOrderId(orderreferancenumber);
			ticket.setPath(newpath);
			ticket.setName(customername);
			ticket.setPancardNumber(pancardnumber);
			ticket.setStatus(" ");
			ticket.setTicketNo(" ");

			final JAXBContext context = JAXBContext.newInstance(PancardCRMticketXMLData.class);
			final Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


			//m.marshal(ticket, System.out);
			//m.marshal(ticket, new File("c:/temp/pancardticket.xml"));



			final StringWriter sw = new StringWriter();
			m.marshal(ticket, sw);

			final String xmlString = sw.toString();
			//System.out.println("*******************" + xmlString);
			LOG.info("*******************" + xmlString);


			/*
			 * if (null != xmlString && webResource != null) { response =
			 * webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
			 * .post(ClientResponse.class);
			 *
			 * }
			 *
			 * final String output = response.getEntity(String.class);
			 */
			//System.out.println("*****************RESPONSE FROM CRM" + xmlString);
		}

		catch (final Exception e)
		{
			e.printStackTrace();
		}


	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplPancardTicketCRMservice#ticketPancardModeltoDTO(java.lang.String, java.lang.String)
	 */

	@Override
	public CRMpancardResponse ticketPancardModeltoDTO(final PancardInformationModel oModel) throws JAXBException
	{
		// YTODO Auto-generated method stub
		//final Client client = Client.create();
		//final ClientResponse response = null;
		//WebResource webResource = null;
		Unmarshaller unmarshaller = null;
		StringReader reader = null;
		final PancardCRMticketXMLData ticket = new PancardCRMticketXMLData();
		CRMpancardResponse crmpancardResponse = new CRMpancardResponse();

		/*
		 * webResource = client.resource(UriBuilder.fromUri(
		 * configurationService.getConfiguration().getString(MarketplacecclientservicesConstants
		 * .TICKET_CREATE_URL)).build());
		 */
		try
		{



			ticket.setOrderId(oModel.getOrderId());
			ticket.setPath(oModel.getPath());
			ticket.setName(oModel.getName());
			ticket.setPancardNumber(oModel.getPancardNumber());
			ticket.setStatus("approved");
			ticket.setTicketNo("T12345");

			final JAXBContext context = JAXBContext.newInstance(PancardCRMticketXMLData.class);
			final Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


			//m.marshal(ticket, System.out);
			//.marshal(ticket, new File("c:/temp/pancardticket.xml"));


			final StringWriter sw = new StringWriter();
			m.marshal(ticket, sw);

			final String xmlString = sw.toString();
			//System.out.println("*******************" + xmlString);
			LOG.info("*******************" + xmlString);

			/*
			 * if (null != xmlString && webResource != null) { response =
			 * webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
			 * .post(ClientResponse.class);
			 * 
			 * }
			 * 
			 * final String output = response.getEntity(String.class);
			 */
			//	System.out.println("*****************RESPONSE FROM CRM" + xmlString);


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

			/*
			 * System.out.println("**************STATUS:" + crmpancardResponse.getStatus() + "*****orderId:" +
			 * crmpancardResponse.getTicketNo());
			 */


		}

		catch (final Exception e)
		{
			e.printStackTrace();
		}

		return crmpancardResponse;
	}




}
