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
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.MarketplaceclientservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.mplcommerceservices.service.data.RefundInfo;
import com.tisl.mpl.xml.pojo.RefundInfoListXMLData;
import com.tisl.mpl.xml.pojo.RefundInfoResponse;
import com.tisl.mpl.xml.pojo.RefundStatusXMLData;


/**
 * @author TCS
 *
 */
@Component
@Qualifier("mplRefundStatusService")
public class MplRefundStatusServiceImpl implements MplRefundStatusService
{
	private static final Logger LOG = Logger.getLogger(MplRefundStatusServiceImpl.class);
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

	/**
	 * @Description : For Refund Status
	 * @param: refundInfolist
	 * @param: orderRefNo
	 * @param: transactionId
	 * @param: oMSStatusCode
	 * @return: response
	 */

	@Override
	public RefundInfoResponse refundStatusDatatoWsdto(final List<RefundInfo> refundInfolist, final String orderRefNo,
			final String transactionId, final String oMSStatusCode)
	{
		RefundInfoResponse response = new RefundInfoResponse();
		final RefundStatusXMLData reqdata = new RefundStatusXMLData();
		final List<RefundInfoListXMLData> reqlist = new ArrayList<RefundInfoListXMLData>();

		try
		{
			if (!refundInfolist.isEmpty())//false == refundInfolist.isEmpty()
			{
				final RefundInfoListXMLData reqObj = new RefundInfoListXMLData();
				for (final RefundInfo refundObj : refundInfolist)
				{
					//removing instantiation inside loop
					if (null != refundObj.getRefundedBy())
					{
						reqObj.setRefundedBy(refundObj.getRefundedBy());
					}
					if (refundObj.getRefundedAmt() > 0)
					{
						reqObj.setRefundedAmt(refundObj.getRefundedAmt());
					}
					if (null != refundObj.getRefundedBankTrxID())
					{
						reqObj.setRefundedBankTrxId(refundObj.getRefundedBankTrxID());
					}
					if (null != refundObj.getRefundType())
					{
						reqObj.setRefundedType(refundObj.getRefundType());
					}
					if (null != refundObj.getRefundedBankTrxStatus())
					{
						reqObj.setRefundedBankTrxStatus(refundObj.getRefundedBankTrxStatus());
					}
					if (null != refundObj.getRefundTriggeredDate())
					{
						reqObj.setRefundTriggeredDate(refundObj.getRefundTriggeredDate());
					}
					reqlist.add(reqObj);
				}
			}

			if (null != orderRefNo && orderRefNo.length() > 0)
			{
				reqdata.setOrderRefNo(orderRefNo);
			}
			if (null != transactionId && transactionId.length() > 0)
			{
				reqdata.setTransactionId(transactionId);
			}
			if (null != oMSStatusCode && oMSStatusCode.length() > 0)
			{
				reqdata.setoMSStatusCode(oMSStatusCode);
			}
			reqdata.setRefundInfoList(reqlist);

			response = refundStatus(reqdata);


		}
		catch (final JAXBException ex)
		{
			LOG.error(MarketplacecclientservicesConstants.JAXB_EXCEPTION);
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return response;
	}

	/**
	 * @Description : Method to fetch the response from OMS regarding refund information
	 * @param: req
	 * @return: responsefromOMS
	 */
	@Override
	public RefundInfoResponse refundStatus(final RefundStatusXMLData req) throws JAXBException
	{
		final Client client = Client.create();
		WebResource webResource = null;
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != configurationService.getConfiguration().getString(MarketplaceclientservicesConstants.REFUNDURL))
		{
			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString(MarketplaceclientservicesConstants.REFUNDURL)).build());
		}
		RefundInfoResponse responsefromOMS = new RefundInfoResponse();

		try
		{
			final JAXBContext context = JAXBContext.newInstance(RefundStatusXMLData.class);
			final Marshaller m = context.createMarshaller(); //for pretty-print XML in JAXB
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			final StringWriter sw = new StringWriter();
			if (null != req)
			{
				m.marshal(req, sw);
			}
			final String xmlString = sw.toString();
			LOG.debug("Refund Status Update Call to OMS Request---------> " + xmlString);
			ClientResponse response = null;
			if (null != webResource)
			{
				response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("x-tenantId", "single")
						.entity(xmlString).post(ClientResponse.class);
			}
			String output = "";

			if (null != response)
			{
				output = response.getEntity(String.class);
			}

			final JAXBContext jaxbContext = JAXBContext.newInstance(RefundInfoResponse.class);
			Unmarshaller unmarshaller = null;
			if (null != jaxbContext)
			{
				unmarshaller = jaxbContext.createUnmarshaller();
			}
			final StringReader reader = new StringReader(output);
			LOG.debug(output);
			responsefromOMS = (RefundInfoResponse) unmarshaller.unmarshal(reader);
			LOG.debug(responsefromOMS);
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return responsefromOMS;
	}
}
