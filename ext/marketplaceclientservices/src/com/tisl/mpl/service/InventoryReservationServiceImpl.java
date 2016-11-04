/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.product.data.CNCServiceableSlavesData;
import de.hybris.platform.commercefacades.product.data.ServiceableSlavesData;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.wsdto.EDDRequestWsDTO;
import com.tisl.mpl.wsdto.EDDResponseWsDTO;
import com.tisl.mpl.wsdto.InventoryReservListRequest;
import com.tisl.mpl.wsdto.InventoryReservListResponse;
import com.tisl.mpl.wsdto.InventoryReservRequest;
import com.tisl.mpl.wsdto.ServiceableSlavesDTO;


/**
 * @author TCS
 *
 */
public class InventoryReservationServiceImpl implements InventoryReservationService
{
	private static final Logger LOG = Logger.getLogger(InventoryReservationServiceImpl.class);
	@Autowired
	private ConfigurationService configurationService;

	@Resource(name = "pinCodeDeliveryModeService")
	private PinCodeDeliveryModeService pinCodeDeliveryModeService;

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
	 * @Description : For storing soft reservation details to InventoryReservListResponse object
	 * @param: cartdatalist
	 * @param: cartId
	 * @param: pincode
	 * @param: requestType
	 * @return: InventoryReservListResponse
	 */
	@Override
	public InventoryReservListResponse convertDatatoWsdto(final List<CartSoftReservationData> cartdatalist, final String cartId,
			final String pincode, final String requestType)
	{
		InventoryReservListResponse response = new InventoryReservListResponse();
		final InventoryReservListRequest reqdata = new InventoryReservListRequest();
		final List<InventoryReservRequest> reqlist = new ArrayList<InventoryReservRequest>();
		final List<InventoryReservRequest> freebieItemslist = new ArrayList<InventoryReservRequest>();
		InventoryReservRequest reqObj = null;

		try
		{
			for (final CartSoftReservationData cartObj : cartdatalist)
			{
				LOG.debug("inside cart soft reservation data list");
				reqObj = new InventoryReservRequest();
				if (StringUtils.isNotEmpty(cartObj.getUSSID()))
				{
					reqObj.setUSSID(cartObj.getUSSID());
				}
				if (StringUtils.isNotEmpty(cartObj.getParentUSSID()))
				{
					reqObj.setParentUSSID(cartObj.getParentUSSID());
				}
				if (StringUtils.isNotEmpty(cartObj.getIsAFreebie()))
				{
					reqObj.setIsAFreebie(cartObj.getIsAFreebie().toUpperCase());
				}
				if (StringUtils.isNotEmpty(cartObj.getStoreId()))
				{
					reqObj.setStoreId(cartObj.getStoreId());
				}
				if (StringUtils.isNotEmpty(cartObj.getFulfillmentType()))
				{
					reqObj.setFulfillmentType(cartObj.getFulfillmentType().toUpperCase());
				}
				if (StringUtils.isNotEmpty(cartObj.getDeliveryMode()))
				{
					reqObj.setDeliveryMode(cartObj.getDeliveryMode().toUpperCase());
				}
				if (cartObj.getQuantity() != null)
				{
					reqObj.setQuantity(cartObj.getQuantity().toString());
				}
				
				if (cartObj.getTransportMode() != null)
				{
					reqObj.setTransportMode(cartObj.getTransportMode().toString());
				}
				
				
				// Added code for Inventory Reservation Request change
				if ((null != cartObj.getServiceableSlaves() && cartObj.getServiceableSlaves().size() > 0))
				{
					reqObj.setServiceableSlaves(populateServiceableSlaves(cartObj.getServiceableSlaves()));
				}
				// Added code for Inventory Reservation Request change
				if (cartObj.getDeliveryMode().equalsIgnoreCase(MarketplacecclientservicesConstants.CNC))
				{
					if ((null != cartObj.getCncServiceableSlaves() && cartObj.getCncServiceableSlaves().size() > 0))
					{
						List<ServiceableSlavesDTO> serviceableSlavesDTOList = new ArrayList<ServiceableSlavesDTO>();
						for (final CNCServiceableSlavesData data : cartObj.getCncServiceableSlaves())
						{
							if(cartObj.getStoreId().equalsIgnoreCase(data.getStoreId())){
							serviceableSlavesDTOList = populateServiceableSlaves(data.getServiceableSlaves());
							}
						}
						reqObj.setServiceableSlaves(serviceableSlavesDTOList);
					}
				}
				if (reqObj.getIsAFreebie() != null && reqObj.getIsAFreebie().equals("Y"))
				{
					freebieItemslist.add(reqObj);
				}
				else
				{
					reqlist.add(reqObj);
					LOG.debug("Added in Inventory reservation request list");
				}

			}
			reqlist.addAll(freebieItemslist);
			
			if (freebieItemslist.size()>0)
			{
				reqdata.setIsFreebieCart(Boolean.TRUE);
			}else{
				reqdata.setIsFreebieCart(Boolean.FALSE);
			}

			if (StringUtils.isNotEmpty(cartId))
			{
				reqdata.setCartId(cartId);
			}
			if (StringUtils.isNotEmpty(pincode))
			{
				reqdata.setPinCode(pincode);
			}
			if (getDuration(requestType).length() > 0)
			{
				reqdata.setDuration(getDuration(requestType));
			}
			reqdata.setIsNewCart(Boolean.TRUE);
			reqdata.setItem(reqlist);
			response = reserveInventoryAtCheckout(reqdata);
		}
		catch (final ClientEtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final JAXBException e)
		{
			LOG.error(MarketplacecclientservicesConstants.JAXB_EXCEPTION);

		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());

		}
		return response;
	}
	
	private List<ServiceableSlavesDTO> populateServiceableSlaves(List<ServiceableSlavesData> serviceableSlavesDataList ){
		
		List<ServiceableSlavesDTO> serviceableSlavesDTOList=new ArrayList<ServiceableSlavesDTO>();
		ServiceableSlavesDTO dto=null;
		for(ServiceableSlavesData data:serviceableSlavesDataList){
			dto=new ServiceableSlavesDTO();
			if (StringUtils.isNotEmpty(data.getSlaveId()))
			dto.setSlaveId(data.getSlaveId());
			if (StringUtils.isNotEmpty(data.getLogisticsID()))
			dto.setLogisticsID(data.getLogisticsID());
			if (StringUtils.isNotEmpty(data.getPriority()))
			dto.setPriority(data.getPriority());
			if (StringUtils.isNotEmpty(data.getCodEligible()))
			dto.setCODEligible(data.getCodEligible());
			if (StringUtils.isNotEmpty(data.getTransactionType()))
			dto.setTransactionType(data.getTransactionType());
			serviceableSlavesDTOList.add(dto);
		}
	return serviceableSlavesDTOList;		
	}

	/**
	 * @Description : Populate Duration
	 * @param requestType
	 * @return: duration
	 */
	private String getDuration(final String requestType)
	{
		String duration = MarketplacecclientservicesConstants.EMPTY;

		if (requestType.equalsIgnoreCase(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART)
				&& configurationService.getConfiguration().getString(
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_CART) != null)
		{
			duration = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_CART);
		}
		else if (requestType.equalsIgnoreCase(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENT)
				&& configurationService.getConfiguration().getString(
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_PAYMENT) != null)
		{
			duration = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_PAYMENT);
		}
		else if (requestType.equalsIgnoreCase(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERHELD)
				&& configurationService.getConfiguration().getString(
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_ORDERHELD) != null)
		{
			duration = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_ORDERHELD);
		}
		else if (requestType.equalsIgnoreCase(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE)
				&& configurationService.getConfiguration().getString(
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE) != null)
		{
			duration = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_ORDERDEALLOCATE);
		}
		else
		{
			duration = "0";
		}

		return duration;
	}

	/**
	 * @Description : Method to fetch the response from OMS regarding soft reservation
	 * @param: req
	 * @throws JAXBException
	 */
	@Override
	public InventoryReservListResponse reserveInventoryAtCheckout(final InventoryReservListRequest request) throws JAXBException
	{

		final String realTimeCall = configurationService.getConfiguration().getString(
				MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_REALTIMECALL);
		InventoryReservListResponse responsefromOMS = new InventoryReservListResponse();

		if (StringUtils.isNotEmpty(realTimeCall) && realTimeCall.equalsIgnoreCase(MarketplacecclientservicesConstants.Y))
		{
			try
			{

				final Client client = Client.create();

				//Start : Code added for OMS fallback cases
				final String connectionTimeout = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_INVETNORY_SOFTRESERV_CON_TIMEOUT, "5000").trim();
				final String readTimeout = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_INVETNORY_SOFTRESERV_READ_TIMEOUT, "5000").trim();
				final String httpErrorCode = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_HTTP_ERROR_CODE, "404,503").trim();
				client.setConnectTimeout(Integer.valueOf(connectionTimeout));
				client.setReadTimeout(Integer.valueOf(readTimeout));
				//End : Code added for OMS fallback cases


				WebResource webResource = null;

				if (null != configurationService
						&& null != configurationService.getConfiguration()
						&& null != configurationService.getConfiguration().getString(
								MarketplacecclientservicesConstants.OMS_INVENTORY_RESERV_URL))
				{
					webResource = client.resource(UriBuilder.fromUri(
							configurationService.getConfiguration().getString(
									MarketplacecclientservicesConstants.OMS_INVENTORY_RESERV_URL)).build());
				}
				final JAXBContext context = JAXBContext.newInstance(InventoryReservListRequest.class);
				final Marshaller marshaller = context.createMarshaller(); //for pretty-print XML in JAXB
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				final StringWriter stringWriter = new StringWriter();
				if (null != request)
				{
					marshaller.marshal(request, stringWriter);
				}
				final String xmlString = stringWriter.toString();
				LOG.debug(xmlString);
				ClientResponse response = null;
				if (null != webResource && StringUtils.isNotEmpty(xmlString))
				{
					response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("X-tenantId", "single")
							.entity(xmlString).post(ClientResponse.class);
				}
				if (null != response)
				{
					if (httpErrorCode.contains(String.valueOf(response.getStatus())))
					{
						throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.O0007_EXCEP);
					}
					else if (response.getStatus() == 200 && response.hasEntity())
					{
						final String output = response.getEntity(String.class);
						LOG.debug("*********************** Inventory Reservation response xml :" + output);
						final JAXBContext jaxbContext = JAXBContext.newInstance(InventoryReservListResponse.class);
						Unmarshaller unmarshaller = null;
						if (null != jaxbContext)
						{
							unmarshaller = jaxbContext.createUnmarshaller();
						}
						final StringReader reader = new StringReader(output);
						responsefromOMS = (InventoryReservListResponse) unmarshaller.unmarshal(reader);
					}
					else
					{
						LOG.debug("***Error occured while getting response for inventory reservation with reservation :"
								+ response.getStatus());
					}
				}
				else
				{
					LOG.debug("***Error occured while connecting to OMS for inventory reservation ");
				}

			}
			//**ExceptionHandling OMS FallBack..
			catch (final ClientHandlerException cex)
			{
				LOG.error("ClientHandlerException " + MarketplacecclientservicesConstants.EXCEPTION_IS, cex);
				throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.O0003_EXCEP, cex);
			}
			catch (final ClientEtailNonBusinessExceptions ex)
			{
				LOG.error("Http Error in calling OMS - " + ex.getMessage());
				throw ex;
			}
			catch (final Exception ex)
			{
				LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS, ex);
				getPinCodeDeliveryModeService()
						.validateOMSException(ex, MarketplacecclientservicesConstants.EXCEPTION_TYPE_INVENTORY);
				throw new ClientEtailNonBusinessExceptions(ex);
			}

		}
		else
		{

			final String mockXmlFirstPhase = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_REALTIMECALL_MOCK_URLFIRSTPHASE);
			String mockXmlSecondPhase = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_REALTIMECALL_MOCK_URLSECONDPHASE);
			final String mockXmlThirdPhase = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_REALTIMECALL_MOCK_URLTHIRDPHASE);


			if (StringUtils.isNotEmpty(mockXmlFirstPhase) && StringUtils.isNotEmpty(mockXmlSecondPhase)
					&& StringUtils.isNotEmpty(mockXmlThirdPhase))
			{
				String outputXml = mockXmlFirstPhase;
				for (final InventoryReservRequest entry : request.getItem())
				{
					if (null != entry.getUSSID() && !entry.getUSSID().isEmpty())
					{
						mockXmlSecondPhase = mockXmlSecondPhase.replaceAll("<replaceussid>", entry.getUSSID());
						outputXml += mockXmlSecondPhase;
					}
				}
				final JAXBContext jaxbContext = JAXBContext.newInstance(InventoryReservListResponse.class);
				final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				final String output = outputXml + mockXmlThirdPhase;
				LOG.debug("*********************** Inventory Reservation response xml (Mock) :" + output);
				//final StringReader reader = new StringReader(output.toString());
				final StringReader reader = new StringReader(output);
				responsefromOMS = (InventoryReservListResponse) unmarshaller.unmarshal(reader);
			}
			else
			{
				LOG.debug(" Error occured as necessary properties files entries are not present to mock");
			}
		}
		return responsefromOMS;
	}

	
	@Override
	public EDDResponseWsDTO convertDeliverySlotsDatatoWsdto(final InvReserForDeliverySlotsRequestData cartdata)
	{
		EDDResponseWsDTO response = new EDDResponseWsDTO();
		EDDRequestWsDTO reqObj = null;
		try
		{

				LOG.debug("inside reservation delivery slot  data list");
				if (null!= cartdata)
				reqObj = new EDDRequestWsDTO();
				if (StringUtils.isNotEmpty(cartdata.getCartId()))
				{
					reqObj.setCartId(cartdata.getCartId());
				}
			response = getInventoryReservationForDeliverySlots(reqObj);
		}
		catch (final ClientEtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final JAXBException e)
		{
			LOG.error(MarketplacecclientservicesConstants.JAXB_EXCEPTION);

		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());

		}
		return response;
	}
	
	
	@Override
	public EDDResponseWsDTO getInventoryReservationForDeliverySlots(final EDDRequestWsDTO request) throws JAXBException
	{

		final String realTimeCall = configurationService.getConfiguration().getString(
				MarketplacecclientservicesConstants.URL_FOR_DELIVERYSLOT_REALCALL);
		EDDResponseWsDTO responsefromOMS = new EDDResponseWsDTO();

		if (StringUtils.isNotEmpty(realTimeCall) && realTimeCall.equalsIgnoreCase(MarketplacecclientservicesConstants.Y))
		{
			try
			{

				final Client client = Client.create();

				//Start : Code added for OMS fallback cases
				final String connectionTimeout = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_DELIVERY_SLOT_CON_TIMEOUT, "5000").trim();
				final String readTimeout = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_DELIVERY_SLOT_READ_TIMEOUT, "5000").trim();
				final String httpErrorCode = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_HTTP_ERROR_CODE, "404,503").trim();
				client.setConnectTimeout(Integer.valueOf(connectionTimeout));
				client.setReadTimeout(Integer.valueOf(readTimeout));
				//End : Code added for OMS fallback cases


				WebResource webResource = null;

				if (null != configurationService
						&& null != configurationService.getConfiguration()
						&& null != configurationService.getConfiguration().getString(
								MarketplacecclientservicesConstants.OMS_DELIVERY_SLOT_URL))
				{
					webResource = client.resource(UriBuilder.fromUri(
							configurationService.getConfiguration().getString(
									MarketplacecclientservicesConstants.OMS_DELIVERY_SLOT_URL)).build());
				}
				final JAXBContext context = JAXBContext.newInstance(EDDRequestWsDTO.class);
				final Marshaller marshaller = context.createMarshaller(); //for pretty-print XML in JAXB
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				final StringWriter stringWriter = new StringWriter();
				if (null != request)
				{
					marshaller.marshal(request, stringWriter);
				}
				final String xmlString = stringWriter.toString();
				LOG.debug(xmlString);
				ClientResponse response = null;
				if (null != webResource && StringUtils.isNotEmpty(xmlString))
				{
					response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("X-tenantId", "single")
							.entity(xmlString).post(ClientResponse.class);
				}
				if (null != response)
				{
					if (httpErrorCode.contains(String.valueOf(response.getStatus())))
					{
						throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.O0007_EXCEP);
					}
					else if (response.getStatus() == 200 && response.hasEntity())
					{
						final String output = response.getEntity(String.class);
						LOG.debug("*********************** Inventory Reservation For Delivery Slot response xml :" + output);
						final JAXBContext jaxbContext = JAXBContext.newInstance(EDDResponseWsDTO.class);
						Unmarshaller unmarshaller = null;
						if (null != jaxbContext)
						{
							unmarshaller = jaxbContext.createUnmarshaller();
						}
						final StringReader reader = new StringReader(output);
						LOG.debug(reader.toString());
						responsefromOMS = (EDDResponseWsDTO) unmarshaller.unmarshal(reader);
					}
					else
					{
						LOG.debug("***Error occured while getting response for inventory reservation with Delivery Slot :"
								+ response.getStatus());
					}
				}
				else
				{
					LOG.debug("***Error occured while connecting to OMS for inventory reservation with Delivery Slot ");
				}

			}
			//**ExceptionHandling OMS FallBack..
			catch (final ClientHandlerException cex)
			{
				LOG.error("ClientHandlerException " + MarketplacecclientservicesConstants.EXCEPTION_IS, cex);
				throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.O0003_EXCEP, cex);
			}
			catch (final ClientEtailNonBusinessExceptions ex)
			{
				LOG.error("Http Error in calling OMS - " + ex.getMessage());
				throw ex;
			}
			catch (final Exception ex)
			{
				LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS, ex);
				getPinCodeDeliveryModeService()
						.validateOMSException(ex, MarketplacecclientservicesConstants.EXCEPTION_TYPE_INVENTORY);
				throw new ClientEtailNonBusinessExceptions(ex);
			}

		}
		else
		{
			
			String mockXmlFirstPhase = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.URL_FOR_DELIVERYSLOT_FIRSTPHASE);
			String mockXmlSecondPhase = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.URL_FOR_DELIVERYSLOT_SECONDPHASE);
			final String mockXmlThirdPhase = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.URL_FOR_DELIVERYSLOT_THIRDPHASE);


			if (StringUtils.isNotEmpty(mockXmlFirstPhase) && StringUtils.isNotEmpty(mockXmlSecondPhase)
					&& StringUtils.isNotEmpty(mockXmlThirdPhase))
			{
				String outputXml = null;
					if (null != request.getCartId() && !request.getCartId().isEmpty())
					{
						outputXml = mockXmlFirstPhase.replaceAll("<replaceCartId>", request.getCartId());
						outputXml += mockXmlSecondPhase;
					}
				
				final JAXBContext jaxbContext = JAXBContext.newInstance(EDDResponseWsDTO.class);
				final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				final String output = outputXml + mockXmlThirdPhase;
				System.out.println("*********************** Inventory Reservation With Delivery Slots response xml (Mock) :" + output);
				LOG.debug("*********************** Inventory Reservation With Delivery Slots response xml (Mock) :" + output);
				//final StringReader reader = new StringReader(output.toString());
				final StringReader reader = new StringReader(output);
				responsefromOMS = (EDDResponseWsDTO) unmarshaller.unmarshal(reader);
			}
			else
			{
				LOG.debug(" Error occured as necessary properties files entries are not present to mock");
			}
		}
		return responsefromOMS;
	}
	
	
	
	
	

	/**
	 * @return the pinCodeDeliveryModeService
	 */
	public PinCodeDeliveryModeService getPinCodeDeliveryModeService()
	{
		return pinCodeDeliveryModeService;
	}

	/**
	 * @param pinCodeDeliveryModeService
	 *           the pinCodeDeliveryModeService to set
	 */
	public void setPinCodeDeliveryModeService(final PinCodeDeliveryModeService pinCodeDeliveryModeService)
	{
		this.pinCodeDeliveryModeService = pinCodeDeliveryModeService;
	}

}