package com.tisl.mpl.service;

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringWriter;

import javax.annotation.Resource;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.wsdto.CustomerCreateWSDTO;


@UnitTest
public class CustomerWebServiceImplTest
{

	private MplCustomerWebServiceImpl customerService;
	private CustomerCreateWSDTO customerCreateWSDTO;
	private Customer customerModel;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.customerService = Mockito.mock(MplCustomerWebServiceImpl.class);
		this.customerCreateWSDTO = Mockito.mock(CustomerCreateWSDTO.class);
		this.customerCreateWSDTO.setCustCreationFlag("I");
		this.customerCreateWSDTO.setCustomerID("000-237823-89289");
		this.customerCreateWSDTO.setEmailID("ryan@tcs.com");

	}

	@Test
	public void testcustomerModeltoWsDTO()
	{
		assertNotNull(customerModel.getName());
		assertNotNull(customerModel.getCustomerID());
		//customerService.customerCreateCRM(customerCreateWSDTO);

	}


	@Test
	public void testcustomerCreateCRM()
	{
		try
		{
			final Client client = Client.create();
			final StringWriter sw = new StringWriter();
			final WebResource webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString("customer_create_pi_url")).build());
			final JAXBContext context = JAXBContext.newInstance(CustomerCreateWSDTO.class);
			final Marshaller m = context.createMarshaller();
			final String xmlString = sw.toString();
			/*
			 * final ClientResponse response =
			 * webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
			 * .post(ClientResponse.class);
			 */

			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(customerCreateWSDTO, sw);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

	}
}
