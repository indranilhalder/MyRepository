/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringReader;
import java.io.StringWriter;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.xml.pojo.AWBStatusRequest;
import com.tisl.mpl.xml.pojo.AWBStatusResponse;


/**
 * @author TCS
 *
 */
public class MplAwbStatusServiceImpl implements MplAwbStatusService
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(MplAwbStatusServiceImpl.class);

	/*
	 * @JavaDoc
	 *
	 * @param awbNumber
	 *
	 * @param tplCode
	 *
	 * @return AWBStatusResponse
	 */
	@Override
	public AWBStatusResponse prepAwbNumbertoOMS(final String awbNumber, final String tplCode)
	{
		final Client client = Client.create();
		WebResource webResource = null;
		AWBStatusResponse awbResponseFromOms = new AWBStatusResponse();
		final AWBStatusRequest awbStatusRequest = new AWBStatusRequest();
		final AWBStatusRequest.AWBRequestInfo awbRequestInfo = new AWBStatusRequest.AWBRequestInfo();
		String xmlString = "";
		Marshaller marshaller = null;
		String output = null;
		ClientResponse response = null;
		StringReader reader = null;
		Unmarshaller unmarshaller = null;
		if (null != client)
		{ //Consuming AWBStatus URL
			webResource = client.resource(UriBuilder.fromUri(configurationService.getConfiguration().getString("oms.awb.url"))
					.build());

		}
		LOG.debug("********Inside awb service*************");
		try
		{

			if (StringUtils.isNotEmpty(awbNumber))
			{
				awbRequestInfo.setAWBNumber(awbNumber);
			}
			if (StringUtils.isNotEmpty(tplCode))
			{
				awbStatusRequest.setTPLCode(tplCode);
			}
			awbStatusRequest.setAWBRequestInfo(awbRequestInfo);

			final JAXBContext context = JAXBContext.newInstance(AWBStatusRequest.class);
			if (null != context)
			{
				marshaller = context.createMarshaller(); //for pretty-print XML in JAXB
			}
			if (null != marshaller)
			{
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			}
			final StringWriter sw = new StringWriter();
			if (null != marshaller)
			{
				marshaller.marshal(awbStatusRequest, sw);
			}
			xmlString = sw.toString();
			if (LOG.isDebugEnabled())
			{
				LOG.debug(xmlString);
			}
			if (null != webResource)
			{ //Sending xmlString to OMS
				response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("x-tenantId", "single")
						.entity(xmlString).post(ClientResponse.class);
			}
			if (null != response)
			{ //Receiving output from OMS
				output = response.getEntity(String.class);
				if (LOG.isDebugEnabled())
				{
					LOG.debug(output);
				}
			}
			final JAXBContext jaxbContext = JAXBContext.newInstance(AWBStatusResponse.class);
			if (null != jaxbContext)
			{
				unmarshaller = jaxbContext.createUnmarshaller();
			}
			if (StringUtils.isNotEmpty(output))
			{
				reader = new StringReader(output);
			}
			if (null != reader && null != unmarshaller)
			{
				awbResponseFromOms = (AWBStatusResponse) unmarshaller.unmarshal(reader);

			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
		}
		return awbResponseFromOms;
	}


}
