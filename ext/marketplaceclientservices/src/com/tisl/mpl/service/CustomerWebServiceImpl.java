/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.wsdto.CustomerCreateWSDTO;
import com.tisl.mpl.wsdto.CustomerUpdateAddressWSDTO;
import com.tisl.mpl.wsdto.CustomerUpdateTrialWSDTO;


/**
 * @author TCS
 *
 */
public class CustomerWebServiceImpl implements CustomerWebService
{
	private static final Logger LOG = Logger.getLogger(CustomerWebServiceImpl.class);
	private static final String CUSTOMER_CREATE_INDICATOR = "I";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	private final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecclientservicesConstants.DMY_DATE_FORMAT);

	/**
	 * @description method is called to get data from customerModel of commerce and set those data into CRM customer
	 */
	@Override
	public void customerModeltoWsDTO(final CustomerModel customerModel)
	{

		final CustomerCreateWSDTO customer = new CustomerCreateWSDTO();
		customer.setCustCreationFlag(CUSTOMER_CREATE_INDICATOR);
		if (null != customerModel && null != customerModel.getUid())
		{
			customer.setCustomerID(customerModel.getUid());
		}
		if (null != customerModel && null != customerModel.getOriginalUid())
		{
			customer.setEmailID(customerModel.getOriginalUid());
		}
		customerCreateCRM(customer);

	}

	/**
	 * @description method is called to create CRM Customer
	 */
	@Override
	public void customerCreateCRM(final CustomerCreateWSDTO customerTrialWSDTO)
	{

		final Client client = Client.create();
		final WebResource webResource = client.resource(UriBuilder.fromUri(
				configurationService.getConfiguration().getString("customer_create_pi_url")).build());

		try
		{
			final JAXBContext context = JAXBContext.newInstance(CustomerCreateWSDTO.class);
			final Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			final StringWriter sw = new StringWriter();
			m.marshal(customerTrialWSDTO, sw);
			final String xmlString = sw.toString();
			final ClientResponse response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
					.post(ClientResponse.class);
			LOG.debug(response);
		}

		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);

		}
	}

	/**
	 * @description method is called to update details of CRM customer
	 */
	@Override
	public void customerprofiledetailsupdate(final CustomerModel customerModel)
	{
		final CustomerUpdateTrialWSDTO customer = new CustomerUpdateTrialWSDTO();
		customer.setCustCreationFlag("U");
		if (null != customerModel && null != customerModel.getCustomerID())
		{
			customer.setCustomerID(customerModel.getCustomerID());
		}
		if (null != customerModel && null != customerModel.getOriginalUid())
		{
			customer.setEmailID(customerModel.getOriginalUid());
		}
		if (null != customerModel && null != customerModel.getFirstName())
		{
			customer.setFirstName(customerModel.getFirstName());
		}

		if (null != customerModel && null != customerModel.getLastName())
		{
			customer.setLastName(customerModel.getLastName());
		}
		if (null != customerModel && null != customerModel.getDateOfAnniversary())
		{
			customer.setDateOfAnniversary(sdf.format(customerModel.getDateOfAnniversary()));
		}
		if (null != customerModel && null != customerModel.getDateOfBirth())
		{
			customer.setDateOfBirth(sdf.format(customerModel.getDateOfBirth()));
		}
		if (null != customerModel && null != customerModel.getGender())
		{
			if (customerModel.getGender().equals(Gender.MALE))
			{
				customer.setGender(MarketplacecclientservicesConstants.MALE);
			}
			else if (customerModel.getGender().equals(Gender.FEMALE))
			{
				customer.setGender(MarketplacecclientservicesConstants.FEMALE);
			}
		}
		else
		{
			customer.setGender(MarketplacecclientservicesConstants.EMPTY);
		}
		if (null != customerModel && null != customerModel.getMobileNumber())
		{
			customer.setMobileNumber(customerModel.getMobileNumber());
		}
		customerUpdateCRM(customer);
	}

	/**
	 * @description method is called to update Address details of CRM customer
	 */
	@Override
	public void customeraddressdetailsupdate(final AddressModel addressModel, final CustomerModel customerModel)
	{
		final CustomerUpdateAddressWSDTO customer = new CustomerUpdateAddressWSDTO();
		customer.setCustCreationFlag("U");
		if (null != addressModel && null != addressModel.getLine1())
		{
			customer.setAddress1(addressModel.getLine1());
		}
		else
		{
			customer.setAddress1(MarketplacecclientservicesConstants.EMPTY);
		}

		if (null != addressModel && null != addressModel.getLine2())
		{
			customer.setAddress2(addressModel.getLine2());
		}
		else
		{
			customer.setAddress2(MarketplacecclientservicesConstants.EMPTY);
		}

		if (null != addressModel && null != addressModel.getFirstname())
		{
			customer.setFirstName(addressModel.getFirstname());
		}
		if (null != addressModel && null != addressModel.getLastname())
		{
			customer.setLastName(addressModel.getLastname());
		}
		if (null != addressModel && null != addressModel.getPhone1())
		{
			customer.setMobileNumber(addressModel.getPhone1());
		}
		if (null != addressModel && null != addressModel.getPostalcode())
		{
			customer.setPincode(addressModel.getPostalcode());
		}

		if (null != addressModel && null != addressModel.getDistrict())
		{
			customer.setDistrict(addressModel.getDistrict());
		}
		if (null != addressModel && null != addressModel.getTown())
		{
			customer.setCity(addressModel.getTown());
		}

		if (null != addressModel && null != addressModel.getCountry() && null != addressModel.getCountry().getIsocode())
		{
			customer.setCountry(addressModel.getCountry().getIsocode());
		}

		if (null != customerModel && null != customerModel.getDefaultShipmentAddress())
		{
			String defaultflag = "N";
			if (customerModel.getDefaultShipmentAddress().equals(addressModel))
			{
				defaultflag = "Y";
			}
			customer.setDefaultFlag(defaultflag);
		}
		customerAddressUpdateCRM(customer);

	}

	/**
	 * @description method is called to update CRM Customer
	 */
	@Override
	public void customerUpdateCRM(final CustomerUpdateTrialWSDTO customerUpdateTrialWSDTO)
	{
		final Client client = Client.create();
		final WebResource webResource = client.resource(UriBuilder.fromUri(
				configurationService.getConfiguration().getString("customer_create_pi_url")).build());

		try
		{
			final JAXBContext context = JAXBContext.newInstance(CustomerUpdateTrialWSDTO.class);
			final Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			final StringWriter sw = new StringWriter();
			final String xmlString = sw.toString();
			final ClientResponse response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
					.post(ClientResponse.class);
			LOG.info(response);
		}

		catch (final Exception ex)
		{
			LOG.info("EXCEPTION IS" + ex);

		}

	}

	/**
	 * @description method is called to update address details of CRM Customer
	 */
	@Override
	public void customerAddressUpdateCRM(final CustomerUpdateAddressWSDTO customerUpdateAddressWSDTO)
	{
		final Client client = Client.create();
		final WebResource webResource = client.resource(UriBuilder.fromUri(
				configurationService.getConfiguration().getString("customer_create_pi_url")).build());
		try
		{
			final JAXBContext context = JAXBContext.newInstance(CustomerUpdateAddressWSDTO.class);
			final Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			final StringWriter sw = new StringWriter();
			final String xmlString = sw.toString();
			final ClientResponse response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
					.post(ClientResponse.class);
			LOG.info(response);
		}

		catch (final Exception ex)
		{
			LOG.info("EXCEPTION IS" + ex);

		}
	}
}
