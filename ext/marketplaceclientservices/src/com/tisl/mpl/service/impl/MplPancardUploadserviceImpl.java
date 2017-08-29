/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.core.model.PancardInformationModel;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.service.MplPancardUploadService;
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
		//final OrderLine orderLine = new OrderLine();
		OrderLine orderLine = null;
		final List<OrderLine> orderLineList = new ArrayList<OrderLine>();

		if (null != pModelList)
		{
			for (final PancardInformationModel pModel : pModelList)
			{
				orderLine = new OrderLine();
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
					if (MarketplacecclientservicesConstants.PAN_REJECTED.equalsIgnoreCase(pModel.getStatus())
							|| MarketplacecclientservicesConstants.NA.equalsIgnoreCase(pModel.getStatus()))
					{
						orderLine.setPancardStatus(MarketplacecclientservicesConstants.PENDING_FOR_VERIFICATION);
					}
					if (MarketplacecclientservicesConstants.APPROVED.equalsIgnoreCase(pModel.getStatus()))
					{
						orderLine.setPancardStatus(MarketplacecclientservicesConstants.APPROVED);
					}
				}
				orderLineList.add(orderLine);
			}
			//orderLineList.add(orderLine);
		}
		else
		{
			if (CollectionUtils.isNotEmpty(transactionidList))
			{
				for (final String transId : transactionidList)
				{
					orderLine = new OrderLine();
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
		String output = "";
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
				LOG.debug("*********************response:::" + response.getStatus());
			}
		}
		if (response.getStatus() == 200)
		{
			//output = response.getEntity(String.class);
			output = "success";
			if (LOG.isDebugEnabled())
			{
				LOG.debug("output " + output);
			}
		}
		else
		{
			output = "faliure";
			if (LOG.isDebugEnabled())
			{
				LOG.debug("output " + output);
			}
		}
		return output;
	}
}
