package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
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

//import com.hybris.oms.api.comm.dto.PincodeServiceabilityCheck;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeListRequest;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeListResponse;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeRequest;
import com.tisl.mpl.wsdto.StoreLocatorATS;
import com.tisl.mpl.wsdto.StoreLocatorAtsResponseObject;
import com.tisl.mpl.wsdto.StoreLocatorItem;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class PinCodeDeliveryModeServiceImpl implements PinCodeDeliveryModeService
{

	/**
	 *
	 */
	//private static final String PIN_CODE_DELIVERY_MODE_OMS_URL = "pin_code_delivery_mode_oms_url";

	/**
	 *
	 */
	private static final String EXPRESS_DELIVERY = "Express Delivery";

	/**
	 *
	 */
	private static final String HOME_DELIVERY = "Home Delivery";

	/**
	 *
	 */
	private static final String CLICK_AND_COLLECT = "Click And Collect";

	/**
	 *
	 */
	private static final String ED = "ED";

	/**
	 *
	 */
	private static final String HD = "HD";

	/**
	 *
	 */
	private static final String CNC = "CNC";

	/**
	 *
	 */
	//private static final String ERROR_400 = "ERROR_400";

	private static final Logger LOG = Logger.getLogger(PinCodeDeliveryModeServiceImpl.class);
	//OMS Pin code serviceablity
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
	 * OMS Pin code serviceablity converting the data object to the request format
	 */
	@Override
	public PinCodeDeliveryModeListResponse prepPinCodeDeliveryModetoOMS(final String pin, final List<PincodeServiceData> reqData)
	{
		PinCodeDeliveryModeListResponse pincodeResfromOMS = null;

		try
		{

			if (reqData != null)
			{

				final PinCodeDeliveryModeListRequest pincodeRequest = new PinCodeDeliveryModeListRequest();

				final List<PinCodeDeliveryModeRequest> pincodeList = new ArrayList<PinCodeDeliveryModeRequest>();

				for (int i = 0; i < reqData.size(); i++)
				{
					final PinCodeDeliveryModeRequest pincodereqObj = new PinCodeDeliveryModeRequest();
					if (null != reqData.get(i))
					{
						if (null != reqData.get(i).getFullFillmentType())
						{
							pincodereqObj.setFulfilmentType(reqData.get(i).getFullFillmentType().toUpperCase());
						}
						if (null != reqData.get(i).getIsCOD())
						{
							pincodereqObj.setIsCOD(reqData.get(i).getIsCOD());
						}
						if (null != reqData.get(i).getPrice())
						{
							pincodereqObj.setPrice(reqData.get(i).getPrice().doubleValue());
						}
						if (null != reqData.get(i).getSellerId())
						{
							pincodereqObj.setSellerID(reqData.get(i).getSellerId());
						}
						if (null != reqData.get(i).getUssid())
						{
							pincodereqObj.setUSSID(reqData.get(i).getUssid());
						}
						if (null != reqData.get(i).getTransportMode())
						{
							pincodereqObj.setTransportMode(reqData.get(i).getTransportMode().toUpperCase());
						}

						if (null != reqData.get(i).getDeliveryModes())
						{
							final List<MarketplaceDeliveryModeData> marketplaceDeliveryModes = reqData.get(i).getDeliveryModes();
							final List<String> deliveryModes = new ArrayList<String>();
							String deliveryMode = null;
							for (final MarketplaceDeliveryModeData marketplaceDeliveryModeData : marketplaceDeliveryModes)
							{
								deliveryMode = marketplaceDeliveryModeData.getName();
								if (deliveryMode.equalsIgnoreCase(HOME_DELIVERY))
								{
									deliveryModes.add(HD);
								}
								if (deliveryMode.equalsIgnoreCase(EXPRESS_DELIVERY))
								{
									deliveryModes.add(ED);
								}
								if (deliveryMode.equalsIgnoreCase(CLICK_AND_COLLECT))
								{
									deliveryModes.add(CNC);

									if (null != reqData.get(i).getStore())
									{
										final List<String> reqStreNames = new ArrayList<String>();
										for (final String storeName : reqData.get(i).getStore())
										{
											reqStreNames.add(storeName);
										}
										pincodereqObj.setStore(reqStreNames);
									}
								}
							}
							pincodereqObj.setDeliveryMode(deliveryModes);
						}
						if (null != reqData.get(i).getIsDeliveryDateRequired())
						{
							pincodereqObj.setIsDeliveryDateRequired(reqData.get(i).getIsDeliveryDateRequired());
						}
						pincodeList.add(pincodereqObj);
					}
				}
				if (null != pin)
				{
					pincodeRequest.setPincode(pin);
				}
				if (null != reqData.get(0).getCartId())
				{
					pincodeRequest.setCartId(reqData.get(0).getCartId());

				}
				if (!(pincodeList.isEmpty()))
				{
					pincodeRequest.setItem(pincodeList);
				}
				LOG.debug("****************sendPinCodeDeliveryModetoOMS for this pincode:" + pin);
				pincodeResfromOMS = sendPinCodeDeliveryModetoOMS(pincodeRequest);
			}
		}
		catch (final Exception e)
		{
			//pincodeResfromOMS = null;
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS, e);
			throw new ClientEtailNonBusinessExceptions(e);
		}
		return pincodeResfromOMS;
	}

	/**
	 * PIN code serviceablity call to OMS
	 *
	 * @param pincodeRequest
	 * @return PinCodeDeliveryModeListResponse1
	 */
	private PinCodeDeliveryModeListResponse sendPinCodeDeliveryModetoOMS(final PinCodeDeliveryModeListRequest pincodeRequest)
	{
		PinCodeDeliveryModeListResponse responsefromOMS = new PinCodeDeliveryModeListResponse();
		try
		{
			final String omsPincodeServiceability = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.PIN_CODE_DELIVERY_MODE_OMS_MOCK).trim();
			String mockXMLFirstPhase = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.URLFIRSTPHASE).trim();
			final String mockXMLSecondPhase = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.URLSECONDPHASE).trim();
			final String mockXMLThirdPhase = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.URLTHIRDPHASE).trim();

			if (omsPincodeServiceability != null && mockXMLFirstPhase != null && !mockXMLFirstPhase.isEmpty()
					&& mockXMLSecondPhase != null && !mockXMLSecondPhase.isEmpty() && mockXMLThirdPhase != null
					&& !mockXMLThirdPhase.isEmpty() && omsPincodeServiceability.equalsIgnoreCase("N"))
			{
				final String pincode = pincodeRequest.getPincode();
				mockXMLFirstPhase = mockXMLFirstPhase.replaceAll("<replacePincode>", pincode);

				for (final PinCodeDeliveryModeRequest entry : pincodeRequest.getItem())
				{
					String mockXMLSecond = configurationService.getConfiguration()
							.getString(MarketplacecclientservicesConstants.URLSECONDPHASE).trim();
					mockXMLSecond = mockXMLSecond.replaceAll("<replaceUssid>", entry.getUSSID());
					mockXMLFirstPhase += mockXMLSecond;
				}
				final String output = mockXMLFirstPhase + mockXMLThirdPhase;
				LOG.debug("****** Pincode serviceability response for non-real time call :" + output);
				final JAXBContext jaxbContext = JAXBContext.newInstance(PinCodeDeliveryModeListResponse.class);
				final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				final StringReader reader = new StringReader(output);
				responsefromOMS = (PinCodeDeliveryModeListResponse) unmarshaller.unmarshal(reader);
			}
			else
			{
				final Client client = Client.create();
				final WebResource webResource = client.resource(UriBuilder.fromUri(
						configurationService.getConfiguration().getString(
								MarketplacecclientservicesConstants.PIN_CODE_DELIVERY_MODE_OMS_URL)).build());
				final JAXBContext context = JAXBContext.newInstance(PinCodeDeliveryModeListRequest.class);

				final Marshaller m = context.createMarshaller(); //for pretty-print XML in JAXB
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				final StringWriter sw = new StringWriter();

				m.marshal(pincodeRequest, sw);
				final String xmlString = sw.toString();

				LOG.debug("********Pincode serviceability  request  :" + xmlString);
				final ClientResponse response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml")
						.header("X-tenantId", "single").entity(xmlString).post(ClientResponse.class);

				final String output = response.getEntity(String.class);
				LOG.debug("****** Pincode serviceability response for real time call:" + output);
				final JAXBContext jaxbContext = JAXBContext.newInstance(PinCodeDeliveryModeListResponse.class);
				final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				final StringReader reader = new StringReader(output);
				responsefromOMS = (PinCodeDeliveryModeListResponse) unmarshaller.unmarshal(reader);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS, ex);
			throw new ClientEtailNonBusinessExceptions(ex);
		}
		return responsefromOMS;
	}

	/**
	 * this method prepares request object and calls oms to get inventories for stores.
	 *
	 * @param storeLocationRequestDataList
	 * @return StoreLocatorAtsResponseObject
	 *
	 */
	@Override
	public StoreLocatorAtsResponseObject prepStoreLocationsToOMS(final List<StoreLocationRequestData> storeLocationRequestDataList)
	{
		LOG.debug("from prepStoreLocationsToOMS method in serive");
		StoreLocatorAtsResponseObject storeLocatorResfromOMS = null;
		try
		{
			//check null condition
			if (storeLocationRequestDataList != null && storeLocationRequestDataList.size() > 0)
			{
				final StoreLocatorATS storeLocatorRequest = new StoreLocatorATS();
				final List<StoreLocatorItem> storeLocatorList = new ArrayList<StoreLocatorItem>();
				for (int i = 0; i < storeLocationRequestDataList.size(); i++)
				{
					final StoreLocatorItem storelocreqObj = new StoreLocatorItem();
					if (null != storeLocationRequestDataList.get(i))
					{
						if (null != storeLocationRequestDataList.get(i).getUssId())
						{
							storelocreqObj.setUssId(storeLocationRequestDataList.get(i).getUssId());
						}
						if (null != storeLocationRequestDataList.get(i).getStoreId())
						{
							final List<String> storeLocationList = new ArrayList<String>();
							for (final String storeLocation : storeLocationRequestDataList.get(i).getStoreId())
							{
								storeLocationList.add(storeLocation);
							}
							storelocreqObj.setStoreId(storeLocationList);
						}
						if (null != storeLocationRequestDataList.get(i).getSellerId())
						{
							storelocreqObj.setSellerID(storeLocationRequestDataList.get(i).getSellerId());
						}
						if (null != storeLocationRequestDataList.get(i).getFulfillmentType())
						{
							storelocreqObj.setFulfillmentType(storeLocationRequestDataList.get(i).getFulfillmentType());
						}
						if (null != storeLocationRequestDataList.get(i).getTransportMode())
						{
							storelocreqObj.setTransportMode(storeLocationRequestDataList.get(i).getTransportMode());
						}
						if (storeLocationRequestDataList.get(i).getPrice() > 0.0)
						{
							storelocreqObj.setPrice(storeLocationRequestDataList.get(i).getPrice());
						}
						storeLocatorList.add(storelocreqObj);
					}
				}
				storeLocatorRequest.setItem(storeLocatorList);
				LOG.debug("****************calls to oms to get valid stores having inventories*************");
				storeLocatorResfromOMS = sendStoreLocatorstoOMS(storeLocatorRequest);
			}
		}
		catch (final Exception e)
		{
			//pincodeResfromOMS = null;
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions(e);
		}

		return storeLocatorResfromOMS;
	}

	/**
	 * This method calls to oms to get inventories for stores.
	 *
	 * @param storeLocatorRequest
	 * @return StoreLocatorAtsResponseObject
	 * @throws JAXBException
	 */
	public StoreLocatorAtsResponseObject sendStoreLocatorstoOMS(final StoreLocatorATS storeLocatorRequest) throws JAXBException
	{
		LOG.debug("from sendStoreLocatorstoOMS in service");
		StoreLocatorAtsResponseObject responsefromOMS = new StoreLocatorAtsResponseObject();
		try
		{
			final String omsstoreServiceability = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_REALCALL).trim();
			String mockXMLFirstPhase = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_FIRSTPHASE).trim();
			final String mockXMLSecondPhase = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_SECONDPHASE).trim();
			final String mockXMLThirdPhase = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_THIRDPHASE).trim();

			if (omsstoreServiceability != null && mockXMLFirstPhase != null && !mockXMLFirstPhase.isEmpty()
					&& mockXMLSecondPhase != null && !mockXMLSecondPhase.isEmpty() && mockXMLThirdPhase != null
					&& !mockXMLThirdPhase.isEmpty() && omsstoreServiceability.equalsIgnoreCase("N"))
			{
				LOG.debug("Try to prepare non-real time OMS call get some stores and inventories");
				for (final StoreLocatorItem entry : storeLocatorRequest.getItem())
				{
					String mockXMLSecond = configurationService.getConfiguration()
							.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_SECONDPHASE).trim();
					mockXMLSecond = mockXMLSecond.replaceAll("<replaceUssid>", entry.getUssId());
					mockXMLFirstPhase += mockXMLSecond;
				}
				final String output = mockXMLFirstPhase + mockXMLThirdPhase;
				LOG.debug("*********************** StoreLocator  non- real time response xml :" + output);
				final JAXBContext jaxbContext = JAXBContext.newInstance(StoreLocatorAtsResponseObject.class);
				final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				final StringReader reader = new StringReader(output);
				responsefromOMS = (StoreLocatorAtsResponseObject) unmarshaller.unmarshal(reader);
			}
			else
			{
				final Client client = Client.create();
				final WebResource webResource = client.resource(UriBuilder.fromUri(
						configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_URL))
						.build());
				final JAXBContext context = JAXBContext.newInstance(StoreLocatorATS.class);
				final Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				final StringWriter sw = new StringWriter();
				m.marshal(storeLocatorRequest, sw);
				final String xmlString = sw.toString();

				LOG.debug("*********************** StoreLocator Real Time Serviceability request xml :" + xmlString);
				final ClientResponse response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml")
						.header("X-tenantId", "single").entity(xmlString).post(ClientResponse.class);

				final String output = response.getEntity(String.class);
				LOG.debug("*********************** StoreLocator Real Time Serviceability response xml :" + output);
				final JAXBContext jaxbContext = JAXBContext.newInstance(StoreLocatorAtsResponseObject.class);
				final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				final StringReader reader = new StringReader(output);
				responsefromOMS = (StoreLocatorAtsResponseObject) unmarshaller.unmarshal(reader);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
			throw new ClientEtailNonBusinessExceptions(ex);
		}
		return responsefromOMS;
	}
}
