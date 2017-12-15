/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.wsdto.AddressInfoDTO;
import com.tisl.mpl.wsdto.DuplicateTicketMasterXMLData;
import com.tisl.mpl.wsdto.TicketMasterXMLData;
import com.tisl.mpl.wsdto.TicketlineItemsXMLData;
import com.tisl.mpl.wsdto.UploadImage;



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
	@Resource(name = "clientIntegration")
	private ClientIntegration clientIntegration;



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
			if (StringUtils.isNotBlank(sendTicketRequestData.getEcomRequestId()))
			{
				ticket.setEcomRequestId(sendTicketRequestData.getEcomRequestId());
			}

			if (null != sendTicketRequestData.getAddressInfo())
			{
				if (StringUtils.isNotBlank(sendTicketRequestData.getReturnPickupDate()))
				{
					addressInfo.setReturnPickupDate(sendTicketRequestData.getReturnPickupDate());
				}
				if (StringUtils.isNotBlank(sendTicketRequestData.getTimeSlotFrom()))
				{
					addressInfo.setTimeSlotFrom(sendTicketRequestData.getTimeSlotFrom());
				}
				if (StringUtils.isNotBlank(sendTicketRequestData.getTimeSlotTo()))
				{
					addressInfo.setTimeSlotTo(sendTicketRequestData.getTimeSlotTo());
				}

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
					else if (ticket.getTicketType().equalsIgnoreCase("A"))
					{
						ticketLineObj.setCancelReasonCode(sendTicketLineItemData.getCancelReasonCode());
					}
					else
					{
						ticketLineObj.setReturnReasonCode(sendTicketLineItemData.getReturnReasonCode());
					}
					//R2.3 start 02-03-2017
					if (StringUtils.isNotEmpty(sendTicketLineItemData.getTimeSlotFrom()))
					{
						ticketLineObj.setTimeSlotFrom(sendTicketLineItemData.getTimeSlotFrom());
					}
					if (StringUtils.isNotEmpty(sendTicketLineItemData.getTimeSlotTo()))
					{
						ticketLineObj.setTimeSlotTo(sendTicketLineItemData.getTimeSlotTo());
					}
					//R2.3 start

					//TPR-4134
					if (StringUtils.isNotEmpty(sendTicketLineItemData.getReverseSealLostflag()))
					{
						ticketLineObj.setReverseSealLostflag(sendTicketLineItemData.getReverseSealLostflag());
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
			final String password = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_PASSWORD);
			final String userId = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_USERID);
			client.addFilter(new HTTPBasicAuthFilter(userId, password));
			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.TICKET_CREATE_URL)).build());
			LOG.debug("::::::::::::::::::::::::::::::::::::webResource:::" + webResource);
		}
		final JAXBContext context = JAXBContext.newInstance(TicketMasterXMLData.class);
		final Marshaller m = context.createMarshaller(); //for pretty-print XML in JAXB
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		LOG.debug("Marshalling to file!!!!");
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
					//TPR-4134
					if (null != sendTicketLineItemData.getReverseSealLostflag())
					{
						ticketLineObj.setReverseSealLostflag(sendTicketLineItemData.getReverseSealLostflag());
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
			final String password = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_PASSWORD);
			final String userId = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_USERID);
			client.addFilter(new HTTPBasicAuthFilter(userId, password));
			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.TICKET_CREATE_URL)).build());
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

	/**
	 * The existing ticket population method has been overloaded to incorporate the changes need for ticket structure for
	 * return and cancel scenarios || TPR-6778
	 *
	 * @param sendTicketRequestData
	 * @param overloadParam
	 * @throws JAXBException
	 */
	@Override
	public void ticketCreationModeltoWsDTO(final SendTicketRequestData sendTicketRequestData, final Boolean overloadParam)
			throws JAXBException
	{
		LOG.info("Inside overloaded method ticketCreationModeltoWsDTO....");
		final AddressInfoDTO addressInfo = new AddressInfoDTO();
		try
		{
			//one touch--start
			TicketMasterXMLData ticket = null;
			final List<SendTicketLineItemData> sendTicketLineItemDataList = sendTicketRequestData.getLineItemDataList();
			ArrayList<TicketlineItemsXMLData> ticketlineItemsXMLDataList = null;
			if (null != sendTicketLineItemDataList)
			{
				for (final SendTicketLineItemData sendTicketLineItemData : sendTicketLineItemDataList)
				{
					ticketlineItemsXMLDataList = new ArrayList<TicketlineItemsXMLData>();
					//one touch--end
					ticket = new TicketMasterXMLData();
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
					if (StringUtils.isNotBlank(sendTicketRequestData.getEcomRequestId()))
					{
						ticket.setEcomRequestId(sendTicketRequestData.getEcomRequestId());
					}
					//TPR-5954
					if (StringUtils.isNotBlank(sendTicketRequestData.getComments()))
					{
						ticket.setComments(sendTicketRequestData.getComments());
					}

					if (null != sendTicketRequestData.getAddressInfo())
					{
						if (StringUtils.isNotBlank(sendTicketRequestData.getReturnPickupDate()))
						{
							addressInfo.setReturnPickupDate(sendTicketRequestData.getReturnPickupDate());
						}
						if (StringUtils.isNotBlank(sendTicketRequestData.getTimeSlotFrom()))
						{
							addressInfo.setTimeSlotFrom(sendTicketRequestData.getTimeSlotFrom());
						}
						if (StringUtils.isNotBlank(sendTicketRequestData.getTimeSlotTo()))
						{
							addressInfo.setTimeSlotTo(sendTicketRequestData.getTimeSlotTo());
						}

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
					final TicketlineItemsXMLData ticketLineObj = new TicketlineItemsXMLData();
					if (null != sendTicketLineItemData.getLineItemId())
					{
						ticketLineObj.setLineItemId(sendTicketLineItemData.getLineItemId());
					}
					if (ticket.getTicketType().equalsIgnoreCase(MarketplacecclientservicesConstants.CANCEL))
					{
						ticketLineObj.setCancelReasonCode(sendTicketLineItemData.getCancelReasonCode());
					}
					else if (ticket.getTicketType().equalsIgnoreCase("A"))
					{
						ticketLineObj.setCancelReasonCode(sendTicketLineItemData.getCancelReasonCode());
					}
					else
					{
						ticketLineObj.setReturnReasonCode(sendTicketLineItemData.getReturnReasonCode());
					}
					//R2.3 start 02-03-2017
					if (StringUtils.isNotEmpty(sendTicketLineItemData.getTimeSlotFrom()))
					{
						ticketLineObj.setTimeSlotFrom(sendTicketLineItemData.getTimeSlotFrom());
					}
					if (StringUtils.isNotEmpty(sendTicketLineItemData.getTimeSlotTo()))
					{
						ticketLineObj.setTimeSlotTo(sendTicketLineItemData.getTimeSlotTo());
					}
					//R2.3 start
					//TPR-4134
					if (StringUtils.isNotEmpty(sendTicketLineItemData.getReverseSealLostflag()))
					{
						ticketLineObj.setReverseSealLostflag(sendTicketLineItemData.getReverseSealLostflag());
					}
					//TPR-5954
					if (StringUtils.isNotEmpty(sendTicketLineItemData.getSubReasonCode()))
					{
						ticketLineObj.setSubReturnReasonCode(sendTicketLineItemData.getSubReasonCode());
					}
					ticketlineItemsXMLDataList.add(ticketLineObj);
					ticket.setLineItemDataList(ticketlineItemsXMLDataList);
					ticketCreationCRM(ticket);
				}
			}
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
		LOG.info("Finished executing overloaded method ticketCreationModeltoWsDTO....");
	}

	/**
	 * This method is created to send the web form tickets to CRM via PI ( TPR-5989 )
	 *
	 * @return String message success or failure
	 * @param mplWebCrmTicketModel
	 * @throws JAXBException
	 */
	private DuplicateTicketMasterXMLData populateDuplicateReq(final MplWebCrmTicketModel mplWebCrmTicketModel) throws Exception
	{
		final DuplicateTicketMasterXMLData duplicateTicketRequestData = new DuplicateTicketMasterXMLData();
		if (null != mplWebCrmTicketModel.getTicketSubType() && mplWebCrmTicketModel.getTicketSubType().equalsIgnoreCase("NO"))
		{
			duplicateTicketRequestData.setCustomerID(mplWebCrmTicketModel.getCustomerId());
		}
		if (null != mplWebCrmTicketModel.getL0code())
		{
			duplicateTicketRequestData.setL0CatCode(mplWebCrmTicketModel.getL0code());
		}
		if (null != mplWebCrmTicketModel.getL1code())
		{
			duplicateTicketRequestData.setL1CatCode(mplWebCrmTicketModel.getL1code());
		}
		if (null != mplWebCrmTicketModel.getL2code())
		{
			duplicateTicketRequestData.setL2CatCode(mplWebCrmTicketModel.getL2code());
		}
		if (null != mplWebCrmTicketModel.getL3code())
		{
			duplicateTicketRequestData.setL3CatCode(mplWebCrmTicketModel.getL3code());
		}
		if (null != mplWebCrmTicketModel.getOrderCode())
		{
			duplicateTicketRequestData.setParentOrderId(mplWebCrmTicketModel.getOrderCode());
		}
		if (null != mplWebCrmTicketModel.getSubOrderCode())
		{
			duplicateTicketRequestData.setSubOrderId(mplWebCrmTicketModel.getSubOrderCode());
		}
		if (null != mplWebCrmTicketModel.getTransactionId())
		{
			duplicateTicketRequestData.setTransactionId(mplWebCrmTicketModel.getTransactionId());
		}
		return duplicateTicketRequestData;
	}

	/**
	 * This method is created to check the duplicate web from ticket ( TPR-5989 )
	 *
	 * @param mplWebCrmTicketModel
	 * @return String message success or failure
	 * @throws JAXBException
	 */
	@Override
	public String checkDuplicateWebFormTicket(final MplWebCrmTicketModel mplWebCrmTicketModel) throws Exception
	{
		LOG.debug("Starting to execute checkDuplicateWebFormTicket method....");
		String result = null;
		DuplicateTicketMasterXMLData duplicateReq = null;
		final StringWriter duplicateXmlString = new StringWriter();
		try
		{
			duplicateReq = populateDuplicateReq(mplWebCrmTicketModel);
			final JAXBContext context = JAXBContext.newInstance(DuplicateTicketMasterXMLData.class);
			final Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(duplicateReq, duplicateXmlString);

			LOG.debug(" CRM Duplicate Ticket Xml File >>>>>>>>>>>>>>>> " + duplicateXmlString);
			result = clientIntegration.checkDuplicateWebFormTicket(duplicateXmlString.toString());

			LOG.debug("Finished to execute checkDuplicateWebFormTicket method....");
		}
		catch (final Exception ex)
		{
			LOG.error(ex);
			//even if duplicate failed we create ticket in CRM
			result = "success";
		}
		return result;
	}

	/**
	 * This method is created to populate the data for crm ticket
	 *
	 * @param mplWebCrmTicketModel
	 * @param subOrderModel
	 * @param orderData
	 * @param orderEntry
	 * @return TicketMasterXMLData
	 * @throws Exception
	 */
	@Override
	public TicketMasterXMLData populateWebFormData(final MplWebCrmTicketModel mplWebCrmTicketModel) throws Exception
	{
		final AddressInfoDTO addressInfo = new AddressInfoDTO();
		final TicketMasterXMLData ticket = new TicketMasterXMLData();
		final UploadImage uploadImage = new UploadImage();
		final List<UploadImage> uploadImageList = new ArrayList<UploadImage>();

		try
		{
			if (null != mplWebCrmTicketModel.getCustomerId())
			{
				ticket.setCustomerID(mplWebCrmTicketModel.getCustomerId());
			}
			if (null != mplWebCrmTicketModel.getOrderCode())
			{
				ticket.setOrderId(mplWebCrmTicketModel.getOrderCode());
			}
			if (null != mplWebCrmTicketModel.getSubOrderCode())
			{
				ticket.setSubOrderId(mplWebCrmTicketModel.getSubOrderCode());
			}
			if (null != mplWebCrmTicketModel.getTicketType())
			{
				ticket.setTicketCat(mplWebCrmTicketModel.getTicketType());
			}
			//setting default as W for WEb form
			ticket.setTicketType(MarketplacecclientservicesConstants.CRM_WEBFORM_TICKET_TYPE);

			if (null != mplWebCrmTicketModel.getTicketSubType())
			{
				ticket.setTicketSubType(mplWebCrmTicketModel.getTicketSubType());
			}
			if (StringUtils.isNotEmpty(mplWebCrmTicketModel.getCommerceTicketId())) //to-do
			{
				ticket.setEcomRequestId(mplWebCrmTicketModel.getCommerceTicketId());
			}
			if (null != mplWebCrmTicketModel.getL0code())
			{
				ticket.setL0CatCode(mplWebCrmTicketModel.getL0code());
			}
			if (null != mplWebCrmTicketModel.getL1code())
			{
				ticket.setL1CatCode(mplWebCrmTicketModel.getL1code());
			}
			if (null != mplWebCrmTicketModel.getL2code())
			{
				ticket.setL2CatCode(mplWebCrmTicketModel.getL2code());
			}
			if (null != mplWebCrmTicketModel.getL3code())
			{
				ticket.setL3CatCode(mplWebCrmTicketModel.getL3code());
			}
			if (null != mplWebCrmTicketModel.getL4code())
			{
				ticket.setL4CatCode(mplWebCrmTicketModel.getL4code());
			}
			if (null != mplWebCrmTicketModel.getTicketType())
			{
				ticket.setTicketCat(mplWebCrmTicketModel.getTicketType());
			}
			if (null != mplWebCrmTicketModel.getComment())
			{
				ticket.setComments(mplWebCrmTicketModel.getComment());
			}
			if (null != mplWebCrmTicketModel.getAttachments())
			{
				final List<String> items = Arrays.asList(mplWebCrmTicketModel.getAttachments().split(","));
				for (final String path : items)
				{
					uploadImage.setImagePath(path);
					uploadImageList.add(uploadImage);
				}
				ticket.setUploadImage(uploadImageList);
			}
			if (null != mplWebCrmTicketModel.getCustomerMobile())// to -do
			{
				addressInfo.setPhoneNo(mplWebCrmTicketModel.getCustomerMobile());
				ticket.setAlternatePhoneNo(mplWebCrmTicketModel.getCustomerMobile());
			}
			ticket.setAddressInfo(addressInfo);
			if (null != mplWebCrmTicketModel.getCustomerName())
			{
				ticket.setAlternateContactName(mplWebCrmTicketModel.getCustomerName());
			}

			//Line item details loop
			final ArrayList<TicketlineItemsXMLData> ticketlineItemsXMLDataList = new ArrayList<TicketlineItemsXMLData>();
			final TicketlineItemsXMLData ticketLineObj = new TicketlineItemsXMLData();
			if (null != mplWebCrmTicketModel.getTransactionId())
			{
				ticketLineObj.setLineItemId(mplWebCrmTicketModel.getTransactionId());
			}
			ticketlineItemsXMLDataList.add(ticketLineObj);
			ticket.setLineItemDataList(ticketlineItemsXMLDataList);

			//call for sending it to PI
			//ticketCreationCRM(ticket);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS);
			throw ex;
		}
		return ticket;
	}
}
