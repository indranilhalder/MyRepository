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
import com.tisl.mpl.wsdto.InventoryReservJewelleryRequest;
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

	private static final String OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING = "paymentPending";

	/**
	 * @Description : For storing soft reservation details to InventoryReservListResponse object
	 * @param: cartdatalist
	 * @param: cartId
	 * @param: pincode
	 * @param: requestType
	 * @return: InventoryReservListResponse
	 */
	@Override
	public InventoryReservListRequest convertDatatoWsdto(final List<CartSoftReservationData> cartdatalist, final String cartGuid,
			final String pincode, final String requestType)
	{
		final InventoryReservListRequest reqdata = new InventoryReservListRequest();
		final List<InventoryReservRequest> reqlist = new ArrayList<InventoryReservRequest>();
		List<InventoryReservRequest> jewlleryReqItemlist = null;
		final List<InventoryReservRequest> freebieItemslist = new ArrayList<InventoryReservRequest>();
		final List<InventoryReservRequest> freebieItemsJewellerylist = new ArrayList<InventoryReservRequest>();
		final List<InventoryReservJewelleryRequest> jewelleryReqList = new ArrayList<InventoryReservJewelleryRequest>();

		InventoryReservRequest reqObj = null;
		InventoryReservRequest reqJewelleryObj = null;
		InventoryReservJewelleryRequest jewelleryReqObj = null;
		String oldListing = "1";


		//final boolean set1 = true;

		try
		{

			for (final CartSoftReservationData cartObj : cartdatalist)
			{
				if (!cartObj.isJewellery())
				{
					LOG.debug("inside cart soft reservation data list");
					reqObj = new InventoryReservRequest();
					if (StringUtils.isNotEmpty(cartObj.getUSSID()))
					{
						reqObj.setUSSID(cartObj.getUSSID());
					}
					//reqObj.setJewellery((Boolean) null);
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
								if (cartObj.getStoreId().equalsIgnoreCase(data.getStoreId()))
								{
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
				else
				{

					LOG.debug("inside cart soft reservation data list for Jewellery");
					// JWLSPCUAT-342 commenting
					//					if (StringUtils.equalsIgnoreCase(requestType, OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING))
					//					{
					//						if (set1)
					//						{
					//							set1 = false;
					//							reqObj = new InventoryReservRequest();
					//							if (StringUtils.isNotEmpty(cartObj.getUSSID()))
					//							{
					//								reqObj.setUSSID(cartObj.getUSSID());
					//							}
					//							//	reqObj.setJewellery((Boolean) null);
					//							if (StringUtils.isNotEmpty(cartObj.getParentUSSID()))
					//							{
					//								reqObj.setParentUSSID(cartObj.getParentUSSID());
					//							}
					//							if (StringUtils.isNotEmpty(cartObj.getIsAFreebie()))
					//							{
					//								reqObj.setIsAFreebie(cartObj.getIsAFreebie().toUpperCase());
					//							}
					//							if (StringUtils.isNotEmpty(cartObj.getStoreId()))
					//							{
					//								reqObj.setStoreId(cartObj.getStoreId());
					//							}
					//							if (StringUtils.isNotEmpty(cartObj.getFulfillmentType()))
					//							{
					//								reqObj.setFulfillmentType(cartObj.getFulfillmentType().toUpperCase());
					//							}
					//							if (StringUtils.isNotEmpty(cartObj.getDeliveryMode()))
					//							{
					//								reqObj.setDeliveryMode(cartObj.getDeliveryMode().toUpperCase());
					//							}
					//							if (cartObj.getQuantity() != null)
					//							{
					//								reqObj.setQuantity(cartObj.getQuantity().toString());
					//							}
					//
					//
					//							if (cartObj.getTransportMode() != null)
					//							{
					//								reqObj.setTransportMode(cartObj.getTransportMode().toString());
					//							}
					//
					//
					//							// Added code for Inventory Reservation Request change
					//							if ((null != cartObj.getServiceableSlaves() && cartObj.getServiceableSlaves().size() > 0))
					//							{
					//								reqObj.setServiceableSlaves(populateServiceableSlaves(cartObj.getServiceableSlaves()));
					//							}
					//							// Added code for Inventory Reservation Request change
					//							if (cartObj.getDeliveryMode().equalsIgnoreCase(MarketplacecclientservicesConstants.CNC))
					//							{
					//								if ((null != cartObj.getCncServiceableSlaves() && cartObj.getCncServiceableSlaves().size() > 0))
					//								{
					//									List<ServiceableSlavesDTO> serviceableSlavesDTOList = new ArrayList<ServiceableSlavesDTO>();
					//									for (final CNCServiceableSlavesData data : cartObj.getCncServiceableSlaves())
					//									{
					//										if (cartObj.getStoreId().equalsIgnoreCase(data.getStoreId()))
					//										{
					//											serviceableSlavesDTOList = populateServiceableSlaves(data.getServiceableSlaves());
					//										}
					//									}
					//									reqObj.setServiceableSlaves(serviceableSlavesDTOList);
					//								}
					//							}
					//							if (reqObj.getIsAFreebie() != null && reqObj.getIsAFreebie().equals("Y"))
					//							{
					//								freebieItemslist.add(reqObj);
					//							}
					//							else
					//							{
					//								reqlist.add(reqObj);
					//								LOG.debug("Added in Inventory reservation request list");
					//							}
					//						}
					//
					//					}

					boolean set = false;
					if (!oldListing.equalsIgnoreCase(cartObj.getListingId()))
					{
						jewelleryReqObj = new InventoryReservJewelleryRequest();
						jewlleryReqItemlist = new ArrayList<InventoryReservRequest>();
						jewelleryReqObj.setListingID(cartObj.getListingId());
						oldListing = cartObj.getListingId();
						set = true;
					}
					reqJewelleryObj = new InventoryReservRequest();


					if (StringUtils.isNotEmpty(cartObj.getUSSID()))
					{
						reqJewelleryObj.setUSSID(cartObj.getUSSID());
					}
					//	reqJewelleryObj.setJewellery((Boolean) null);
					if (StringUtils.isNotEmpty(cartObj.getParentUSSID()))
					{
						reqJewelleryObj.setParentUSSID(cartObj.getParentUSSID());
					}
					if (StringUtils.isNotEmpty(cartObj.getIsAFreebie()))
					{
						reqJewelleryObj.setIsAFreebie(cartObj.getIsAFreebie().toUpperCase());
					}
					if (StringUtils.isNotEmpty(cartObj.getStoreId()))
					{
						reqJewelleryObj.setStoreId(cartObj.getStoreId());
					}
					if (StringUtils.isNotEmpty(cartObj.getFulfillmentType()))
					{
						reqJewelleryObj.setFulfillmentType(cartObj.getFulfillmentType().toUpperCase());
					}
					if (StringUtils.isNotEmpty(cartObj.getDeliveryMode()))
					{
						reqJewelleryObj.setDeliveryMode(cartObj.getDeliveryMode().toUpperCase());
					}
					if (cartObj.getQuantity() != null)
					{
						reqJewelleryObj.setQuantity(cartObj.getQuantity().toString());
					}

					if (cartObj.getTransportMode() != null)
					{
						reqJewelleryObj.setTransportMode(cartObj.getTransportMode().toString());
					}


					// Added code for Inventory Reservation Request change
					if ((null != cartObj.getServiceableSlaves() && cartObj.getServiceableSlaves().size() > 0))
					{
						reqJewelleryObj.setServiceableSlaves(populateServiceableSlaves(cartObj.getServiceableSlaves()));
					}
					// Added code for Inventory Reservation Request change
					if (cartObj.getDeliveryMode().equalsIgnoreCase(MarketplacecclientservicesConstants.CNC))
					{
						if ((null != cartObj.getCncServiceableSlaves() && cartObj.getCncServiceableSlaves().size() > 0))
						{
							List<ServiceableSlavesDTO> serviceableSlavesDTOList = new ArrayList<ServiceableSlavesDTO>();
							for (final CNCServiceableSlavesData data : cartObj.getCncServiceableSlaves())
							{
								if (cartObj.getStoreId().equalsIgnoreCase(data.getStoreId()))
								{
									serviceableSlavesDTOList = populateServiceableSlaves(data.getServiceableSlaves());
								}
							}
							reqJewelleryObj.setServiceableSlaves(serviceableSlavesDTOList);
						}
					}

					if (reqJewelleryObj.getIsAFreebie() != null && reqJewelleryObj.getIsAFreebie().equals("Y"))
					{
						freebieItemsJewellerylist.add(reqJewelleryObj);
					}
					else
					{
						jewlleryReqItemlist.add(reqJewelleryObj);
						LOG.debug("Added in Inventory reservation request list");
					}
					if (set)
					{
						jewelleryReqObj.setItem(jewlleryReqItemlist);
						jewelleryReqList.add(jewelleryReqObj);
					}

				}

			}
			reqlist.addAll(freebieItemslist);

			if (freebieItemslist.size() > 0)
			{
				reqdata.setIsFreebieCart(Boolean.TRUE);
			}
			else
			{
				reqdata.setIsFreebieCart(Boolean.FALSE);
			}

			if (StringUtils.isNotEmpty(cartGuid))
			{
				reqdata.setCartId(cartGuid);
			}
			if (StringUtils.isNotEmpty(pincode))
			{
				reqdata.setPinCode(pincode);
			}
			if (getDuration(requestType).length() > 0)
			{
				reqdata.setDuration(getDuration(requestType));
			}
			/******* Jewllery ***/
			reqdata.setIsNewCart(Boolean.TRUE);
			reqdata.setItem(reqlist);
			reqdata.setJewelleryItem(jewelleryReqList);
			/******* Jewellery End ************/
		}
		catch (final ClientEtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());

		}
		return reqdata;
	}

	private List<ServiceableSlavesDTO> populateServiceableSlaves(final List<ServiceableSlavesData> serviceableSlavesDataList)
	{

		final List<ServiceableSlavesDTO> serviceableSlavesDTOList = new ArrayList<ServiceableSlavesDTO>();
		ServiceableSlavesDTO dto = null;
		for (final ServiceableSlavesData data : serviceableSlavesDataList)
		{
			dto = new ServiceableSlavesDTO();
			if (StringUtils.isNotEmpty(data.getSlaveId()))
			{
				dto.setSlaveId(data.getSlaveId());
			}
			if (StringUtils.isNotEmpty(data.getLogisticsID()))
			{
				dto.setLogisticsID(data.getLogisticsID());
			}
			if (StringUtils.isNotEmpty(data.getPriority()))
			{
				dto.setPriority(data.getPriority());
			}
			if (StringUtils.isNotEmpty(data.getCodEligible()))
			{
				dto.setCODEligible(data.getCodEligible());
			}
			if (StringUtils.isNotEmpty(data.getTransactionType()))
			{
				dto.setTransactionType(data.getTransactionType());
			}
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
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_ORDERDEALLOCATE) != null)
		{
			duration = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_ORDERDEALLOCATE);
		}
		//Added for TPR-629
		else if (requestType.equalsIgnoreCase(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING)
				&& configurationService.getConfiguration().getString(
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_PAYMENTPENDING) != null)
		{
			duration = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_DURATION_PAYMENTPENDING);
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
				final String connectionTimeout = configurationService
						.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_INVETNORY_SOFTRESERV_CON_TIMEOUT,
								MarketplacecclientservicesConstants.OMSTIMEOUT).trim();
				final String readTimeout = configurationService
						.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_INVETNORY_SOFTRESERV_READ_TIMEOUT,
								MarketplacecclientservicesConstants.OMSTIMEOUT).trim();
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
			String mockXmlJewelPhase = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_REALTIMECALL_MOCK_URLJEWELPHASE);
			final String mockXmlThirdPhase = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_REALTIMECALL_MOCK_URLTHIRDPHASE);
			final String resevedUssid = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_REALTIMECALL_MOCK_JEWLUSSID);

			if (StringUtils.isNotEmpty(mockXmlFirstPhase) && StringUtils.isNotEmpty(mockXmlSecondPhase)
					&& StringUtils.isNotEmpty(mockXmlThirdPhase))
			{
				String outputXml = mockXmlFirstPhase;
				for (final InventoryReservRequest entry : request.getItem())
				{
					//	if (null != entry.getUSSID() && !entry.getUSSID().isEmpty() && entry.isJewellery() == false)//SONAR FIX JEWELLERY
					if ((null != entry.getUSSID() && !entry.getUSSID().isEmpty()))
					{
						mockXmlSecondPhase = mockXmlSecondPhase.replaceAll("<replaceussid>", entry.getUSSID());
						outputXml += mockXmlSecondPhase;
					}
				}
				/* mock service for Jewellery added */
				for (final InventoryReservJewelleryRequest jewelentry : request.getJewelleryItem())
				{
					if (StringUtils.isEmpty(resevedUssid) && null != jewelentry.getItem() && null != jewelentry.getItem().get(0))
					{
						mockXmlJewelPhase = mockXmlJewelPhase.replaceAll("<jewlussid>", jewelentry.getItem().get(0).getUSSID());
						outputXml += mockXmlJewelPhase;
					}
					for (final InventoryReservRequest entry1 : jewelentry.getItem())
					{
						if (null != entry1.getUSSID() && entry1.getUSSID().equalsIgnoreCase(resevedUssid))
						{
							mockXmlJewelPhase = mockXmlJewelPhase.replaceAll("<jewlussid>", entry1.getUSSID());
							outputXml += mockXmlJewelPhase;
						}
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
			if (null != cartdata)
			{
				reqObj = new EDDRequestWsDTO();
			}
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
				final String connectionTimeout = configurationService
						.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_DELIVERY_SLOT_CON_TIMEOUT,
								MarketplacecclientservicesConstants.OMSTIMEOUT).trim();
				final String readTimeout = configurationService
						.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_DELIVERY_SLOT_READ_TIMEOUT,
								MarketplacecclientservicesConstants.OMSTIMEOUT).trim();
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
					webResource = client.resource(UriBuilder
							.fromUri(
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

			final String mockXmlFirstPhase = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.URL_FOR_DELIVERYSLOT_FIRSTPHASE);
			final String mockXmlSecondPhase = configurationService.getConfiguration().getString(
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
				System.out
						.println("*********************** Inventory Reservation With Delivery Slots response xml (Mock) :" + output);
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
