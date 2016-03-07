/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.xml.pojo.OrderLineData;
import com.tisl.mpl.xml.pojo.ReturnLogisticsRequest;
import com.tisl.mpl.xml.pojo.ReturnLogisticsResponse;


/**
 * @author TCS
 * @Description: This service send data to oms and receives a response
 *
 */

public class ReturnLogisticsServiceImpl implements ReturnLogisticsService
{
	private static final Logger LOG = Logger.getLogger(ReturnLogisticsServiceImpl.class);
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

	/*
	 * @Description: Generate XML Data for Return Logistics
	 * 
	 * @param returnLogisticsList
	 * 
	 * @return response
	 */


	@Override
	public ReturnLogisticsResponse returnLogisticsCheck(final List<ReturnLogistics> returnLogisticsList)
	{
		ReturnLogisticsResponse response = new ReturnLogisticsResponse();
		final ReturnLogisticsRequest reqdata = new ReturnLogisticsRequest();
		final List<OrderLineData> reqlist = new ArrayList<OrderLineData>();
		try
		{
			if (null != returnLogisticsList)
			{
				for (final ReturnLogistics returnLogisticsObj : returnLogisticsList)
				{
					final OrderLineData reqObj = new OrderLineData();
					if(null!=returnLogisticsObj.getPinCode())
					{
						reqObj.setPinCode(returnLogisticsObj.getPinCode());
					}
					if (null != returnLogisticsObj.getOrderId())
					{
						reqObj.setOrderId(returnLogisticsObj.getOrderId());
					}
					if (null != returnLogisticsObj.getTransactionId())
					{
						reqObj.setTransactionId(returnLogisticsObj.getTransactionId());
					}
					reqlist.add(reqObj);
				}
				reqdata.setOrderlines(reqlist);
				response = reverseLogistics(reqdata);
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
			throw e;
		}

		return response;
	}

	private ReturnLogisticsResponse reverseLogistics(final ReturnLogisticsRequest req)
	{
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();

		try
		{
			if (null != configurationService && null != configurationService.getConfiguration()
					&& null != configurationService.getConfiguration().getString("comerce.oms.revereseLogistics.url"))
			{
				webResource = client.resource(UriBuilder.fromUri(
						configurationService.getConfiguration().getString("comerce.oms.revereseLogistics.url")).build());
			}
			if (null != webResource)
			{
				final JAXBContext context = JAXBContext.newInstance(ReturnLogisticsRequest.class);
				final Marshaller marshaller = context.createMarshaller(); //for pretty-print XML in JAXB
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				final StringWriter stringWriter = new StringWriter();
				if (null != req)
				{
					marshaller.marshal(req, stringWriter);
				}
				final String xmlString = stringWriter.toString();

				LOG.debug(" ************** Checking reverse logisticts avaliablity request xml" + xmlString);

				response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("x-tenantId", "single")
						.entity(xmlString).post(ClientResponse.class);
			}
			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** Reverse logisticts avaliablity response xml" + output);

				final JAXBContext jaxbContext = JAXBContext.newInstance(ReturnLogisticsResponse.class);
				Unmarshaller unmarshaller = null;
				if (null != jaxbContext)
				{
					unmarshaller = jaxbContext.createUnmarshaller();
				}
				final StringReader reader = new StringReader(output);
				responsefromOMS = (ReturnLogisticsResponse) unmarshaller.unmarshal(reader);
			}
			else
			{
				LOG.debug(" ************** Reverse logisticts avaliablity response is null");
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return responsefromOMS;
	}
}