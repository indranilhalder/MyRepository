/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.order.AbstractOrderModel;
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
import com.tisl.mpl.wsdto.InventoryReservJewelleryRequest;
import com.tisl.mpl.wsdto.InventoryReservListRequest;
import com.tisl.mpl.wsdto.InventoryReservListResponse;
import com.tisl.mpl.wsdto.InventoryReservRequest;




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
	public InventoryReservListRequest convertDatatoWsdto(final List<CartSoftReservationData> cartdatalist,
			final AbstractOrderModel cart, final String pincode, final String requestType)
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


		boolean set1 = true;

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
					reqObj.setJewellery(cartObj.isJewellery());
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

					if (StringUtils.endsWithIgnoreCase(requestType, OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING))
					{
						if (set1)
						{
							set1 = false;
							reqObj = new InventoryReservRequest();
							if (StringUtils.isNotEmpty(cartObj.getUSSID()))
							{
								reqObj.setUSSID(cartObj.getUSSID());
							}
							reqObj.setJewellery(cartObj.isJewellery());
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

					}

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
					reqJewelleryObj.setJewellery(cartObj.isJewellery());
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

			if (StringUtils.isNotEmpty(cart.getGuid()))
			{
				reqdata.setCartId(cart.getGuid());
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
					if (null != entry.getUSSID() && !entry.getUSSID().isEmpty() && entry.isJewellery() == false)
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