package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

//import com.hybris.oms.api.comm.dto.PincodeServiceabilityCheck;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
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
	private static final String ED = "ED";

	/**
	 *
	 */
	private static final String HD = "HD";

	/**
	 *
	 */
	private static final String CNC = "CNC";


	private static final String TSHIP = "TSHIP".intern();
	private static final String SSHIP = "SSHIP".intern();
	private static final String BOTH = "BOTH".intern();



	private static final Logger LOG = Logger.getLogger(PinCodeDeliveryModeServiceImpl.class);
	//TISPT-401 Start
	static final JAXBContext context = initContext();
	static final Client client = initClient();

	private static JAXBContext initContext()
	{
		try
		{
			return JAXBContext.newInstance(PinCodeDeliveryModeListResponse.class, PinCodeDeliveryModeListRequest.class,
					StoreLocatorAtsResponseObject.class, StoreLocatorATS.class);
		}

		catch (final ClientHandlerException cex)
		{
			LOG.error("Error While Creating JAXB context " + MarketplacecclientservicesConstants.EXCEPTION_IS, cex);
			throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.O0001_EXCEP, cex);
		}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error("Error While Creating JAXB context - " + ex.getMessage());
			throw ex;
		}
		catch (final Exception ex)
		{
			LOG.error("Error While Creating JAXB context  " + MarketplacecclientservicesConstants.EXCEPTION_IS, ex);
			throw new ClientEtailNonBusinessExceptions(ex);
		}
	}

	public static Client initClient()
	{
		if (client == null)
		{
			return Client.create();
		}
		else
		{
			return client;
		}
	}

	//TISPT-401 End

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

	@Autowired
	private CartService cartService;

	//public MplCartFacade MplCartFacade;

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
		final String configSellerHandlingTime = configurationService.getConfiguration().getString("buybox.sellerhandling.time");
		try
		{

			if (reqData != null)
			{

				final PinCodeDeliveryModeListRequest pincodeRequest = new PinCodeDeliveryModeListRequest();

				final List<PinCodeDeliveryModeRequest> pincodeList = new ArrayList<PinCodeDeliveryModeRequest>();

				for (final PincodeServiceData pincodeServiceData : reqData)
				{
					final PinCodeDeliveryModeRequest pincodereqObj = new PinCodeDeliveryModeRequest();
					if (null != pincodeServiceData)
					{
						final List<String> fulfilmentTypeList = new ArrayList<String>();
						/*
						 * if (null != reqData.get(i).getDeliveryFulfillModeByP1()) {
						 * fulfilmentTypeList.add(reqData.get(i).getDeliveryFulfillModeByP1().toUpperCase());
						 * //pincodereqObj.setFulfilmentType(reqData.get(i).getFullFillmentType().toUpperCase()); }
						 */
						if (null != pincodeServiceData.getFullFillmentType())
						{
							if (pincodeServiceData.getFullFillmentType().equalsIgnoreCase(BOTH))
							{
								if (pincodeServiceData.getDeliveryFulfillModeByP1().equalsIgnoreCase(TSHIP))
								{
									fulfilmentTypeList.add(TSHIP);
									fulfilmentTypeList.add(SSHIP);
								}
								else
								{
									fulfilmentTypeList.add(SSHIP);
									fulfilmentTypeList.add(TSHIP);
								}
							}
							else
							{
								fulfilmentTypeList.add(pincodeServiceData.getFullFillmentType().toUpperCase());
							}
						}
						if (fulfilmentTypeList.size() > 0)
						{
							pincodereqObj.setFulfilmentType(fulfilmentTypeList);
						}
						if (null != pincodeServiceData.getIsCOD())
						{
							pincodereqObj.setIsCOD(pincodeServiceData.getIsCOD());
						}
						if (null != pincodeServiceData.getPrice())
						{
							pincodereqObj.setPrice(pincodeServiceData.getPrice().doubleValue());
						}
						if (null != pincodeServiceData.getSellerId())
						{
							pincodereqObj.setSellerID(pincodeServiceData.getSellerId());
						}
						if (null != pincodeServiceData.getUssid())
						{
							pincodereqObj.setUSSID(pincodeServiceData.getUssid());
						}
						if (null != pincodeServiceData.getTransportMode())
						{
							pincodereqObj.setTransportMode(pincodeServiceData.getTransportMode().toUpperCase());
						}

						if (null != pincodeServiceData.getDeliveryModes())
						{
							final List<MarketplaceDeliveryModeData> marketplaceDeliveryModes = pincodeServiceData.getDeliveryModes();
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
									if (null != pincodeServiceData && null != pincodeServiceData.getSellerHandlingTime())
									{
										//configure the seller handling time
										final int sellerHandlingTimeForConfig = configSellerHandlingTime == null ? 0 : Integer
												.parseInt(configSellerHandlingTime);
										if (pincodeServiceData.getSellerHandlingTime().intValue() >= 0
												&& pincodeServiceData.getSellerHandlingTime().intValue() <= sellerHandlingTimeForConfig)
										{
											deliveryModes.add(ED);
										}
									}

								}
								if (deliveryMode.equalsIgnoreCase(CLICK_AND_COLLECT))
								{
									deliveryModes.add(CNC);

									if (null != pincodeServiceData.getStore())
									{
										final List<String> reqStreNames = new ArrayList<String>();
										for (final String storeName : pincodeServiceData.getStore())
										{
											reqStreNames.add(storeName);
										}
										pincodereqObj.setStore(reqStreNames);
									}
									//		pincodereqObj.setStore(reqStreNames);
								}
							}
							//TISHS-133 Starts
							final Set<String> s = new HashSet<String>();
							s.addAll(deliveryModes);
							final List<String> deliveryModesList = new ArrayList<String>();
							deliveryModesList.addAll(s);
							//TISHS-133 Ends
							pincodereqObj.setDeliveryMode(deliveryModesList);
						}
						if (null != pincodeServiceData.getIsDeliveryDateRequired())
						{
							pincodereqObj.setIsDeliveryDateRequired(pincodeServiceData.getIsDeliveryDateRequired());
						}


						if (null != pincodeServiceData.getIsFragile() || StringUtils.isNotEmpty(pincodeServiceData.getIsFragile()))
						{
							if (pincodeServiceData.getIsFragile().equalsIgnoreCase(MarketplacecclientservicesConstants.YES)
									|| pincodeServiceData.getIsFragile().equalsIgnoreCase(MarketplacecclientservicesConstants.Y))
							{
								pincodereqObj.setIsFragile(MarketplacecclientservicesConstants.Y);
							}
							else
							{
								pincodereqObj.setIsFragile(MarketplacecclientservicesConstants.N);
							}
						}
						else
						{
							pincodereqObj.setIsFragile(MarketplacecclientservicesConstants.N);
						}

						if (null != pincodeServiceData.getIsPrecious() || StringUtils.isNotEmpty(pincodeServiceData.getIsPrecious()))
						{
							if (pincodeServiceData.getIsPrecious().equalsIgnoreCase(MarketplacecclientservicesConstants.YES)
									|| pincodeServiceData.getIsPrecious().equalsIgnoreCase(MarketplacecclientservicesConstants.Y))
							{
								pincodereqObj.setIsPrecious(MarketplacecclientservicesConstants.Y);
							}
							else
							{
								pincodereqObj.setIsPrecious(MarketplacecclientservicesConstants.N);
							}
						}
						else
						{
							pincodereqObj.setIsPrecious(MarketplacecclientservicesConstants.N);
						}
						pincodeList.add(pincodereqObj);
					}

				}
				if (null != pin)
				{
					pincodeRequest.setPincode(pin);
				}
				if (null != reqData.get(0) && StringUtils.isNotEmpty(reqData.get(0).getCartId()))
				{
					pincodeRequest.setCartId(reqData.get(0).getCartId());
				}
				if (CollectionUtils.isNotEmpty(pincodeList))
				{
					pincodeRequest.setItem(pincodeList);
				}
				LOG.debug("****************sendPinCodeDeliveryModetoOMS for this pincode:" + pin);
				pincodeResfromOMS = sendPinCodeDeliveryModetoOMS(pincodeRequest);
			}
		}
		catch (final ClientEtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			//pincodeResfromOMS = null;
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS, e);
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
				//Commented as a part of TISPT-401
				//Single Instance Declared for the whole class
				//final JAXBContext jaxbContext = JAXBContext.newInstance(PinCodeDeliveryModeListResponse.class);
				final Unmarshaller unmarshaller = context.createUnmarshaller();
				final StringReader reader = new StringReader(output);
				responsefromOMS = (PinCodeDeliveryModeListResponse) unmarshaller.unmarshal(reader);
			}
			else
			{
				ClientResponse response = null;
				try
				{
					//Commented as a part of TISPT-401
					//Single Instance Declared for the whole class
					//final Client client = Client.create();

					//Start : Code added for OMS fallback cases
					final String connectionTimeout = configurationService.getConfiguration()
							.getString(MarketplacecclientservicesConstants.OMS_PINCODESERVICEABILITY_CON_TIMEOUT, "5000").trim();
					final String readTimeout = configurationService.getConfiguration()
							.getString(MarketplacecclientservicesConstants.OMS_PINCODESERVICEABILITY_READ_TIMEOUT, "5000").trim();
					final String httpErrorCode = configurationService.getConfiguration()
							.getString(MarketplacecclientservicesConstants.OMS_HTTP_ERROR_CODE, "404,503").trim();

					client.setConnectTimeout(Integer.valueOf(connectionTimeout));
					client.setReadTimeout(Integer.valueOf(readTimeout));
					//End : Code added for OMS fallback cases

					final WebResource webResource = client.resource(UriBuilder.fromUri(
							configurationService.getConfiguration().getString(
									MarketplacecclientservicesConstants.PIN_CODE_DELIVERY_MODE_OMS_URL)).build());
					//Commented as a part of TISPT-401
					//Single Instance Declared for the whole class
					//final JAXBContext context = JAXBContext.newInstance(PinCodeDeliveryModeListRequest.class);

					final Marshaller m = context.createMarshaller(); //for pretty-print XML in JAXB
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					final StringWriter sw = new StringWriter();

					m.marshal(pincodeRequest, sw);
					final String xmlString = sw.toString();

					LOG.info("*********************** Pincode serviceability request xml :" + xmlString);
					response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("X-tenantId", "single")
							.entity(xmlString).post(ClientResponse.class);


					LOG.info("*****Pincode serviceability response status code :" + response.getStatus());
					if (httpErrorCode.contains(String.valueOf(response.getStatus())))
					{
						throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.O0007_EXCEP);
					}
				}
				catch (final ClientEtailNonBusinessExceptions ex)
				{
					LOG.error("Http Error in calling OMS - " + ex.getMessage());
					throw ex;
				}
				catch (final Exception ex)
				{
					LOG.error("Error in calling OMS - " + ex.getMessage());
					throw ex;
				}

				final String output = response.getEntity(String.class);
				LOG.debug("****** Pincode serviceability response for real time call:" + output);
				//Commented as a part of TISPT-401
				//Single Instance Declared for the whole class
				//final JAXBContext jaxbContext = JAXBContext.newInstance(PinCodeDeliveryModeListResponse.class);
				final Unmarshaller unmarshaller = context.createUnmarshaller();

				final StringReader reader = new StringReader(output);
				responsefromOMS = (PinCodeDeliveryModeListResponse) unmarshaller.unmarshal(reader);
			}
		}
		catch (final ClientHandlerException cex)
		{
			LOG.error("ClientHandlerException " + MarketplacecclientservicesConstants.EXCEPTION_IS, cex);
			throw new ClientEtailNonBusinessExceptions(MarketplacecclientservicesConstants.O0001_EXCEP, cex);
		}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error("Http Error in calling OMS - " + ex.getMessage());
			throw ex;
		}
		catch (final Exception ex)
		{
			LOG.debug("PinCodeDeliveryModeListResponse  " + MarketplacecclientservicesConstants.EXCEPTION_IS, ex);
			validateOMSException(ex, MarketplacecclientservicesConstants.EXCEPTION_TYPE_PINCODE);
			throw new ClientEtailNonBusinessExceptions(ex);
		}
		return responsefromOMS;

	}



	/*
	 * @desc used for validate connect timeout and read time out exceptions from oms rest call for pincode serviceabilty
	 * and inventory reservation
	 *
	 * @param ex
	 *
	 * @param exceptionType
	 *
	 * @return void
	 *
	 * @throws ClientEtailNonBusinessExceptions
	 */
	@Override
	public void validateOMSException(final Exception ex, final String exceptionType) throws ClientEtailNonBusinessExceptions
	{
		final String connectTimeoutException = getConfigurationService().getConfiguration().getString(
				MarketplacecclientservicesConstants.OMS_FALLBACK_CONNECT_TIMEOUT_EXCEP);
		final String readTimeoutException = getConfigurationService().getConfiguration().getString(
				MarketplacecclientservicesConstants.OMS_FALLBACK_READ_TIMEOUT_EXCEP);

		boolean readErrorFlag = false;
		boolean connectErrorFlag = false;

		final String[] connectionExceptions = (StringUtils.isNotEmpty(connectTimeoutException)) ? connectTimeoutException
				.split(",") : null;
		final String[] readExceptions = (StringUtils.isNotEmpty(readTimeoutException)) ? readTimeoutException.split(",") : null;

		if (connectionExceptions != null && readExceptions != null)
		{
			for (final String exception : connectionExceptions)
			{
				if (StringUtils.isNotEmpty(ex.getMessage()) && ex.getMessage().contains(exception))
				{
					connectErrorFlag = true;
					break;
				}
			}
			if (connectErrorFlag)
			{
				final String excep = (exceptionType.equalsIgnoreCase(MarketplacecclientservicesConstants.EXCEPTION_TYPE_PINCODE)) ? MarketplacecclientservicesConstants.O0001_EXCEP
						: MarketplacecclientservicesConstants.O0003_EXCEP;
				throw new ClientEtailNonBusinessExceptions(excep, ex);
			}

			for (final String exception : readExceptions)
			{
				if (StringUtils.isNotEmpty(ex.getMessage()) && ex.getMessage().contains(exception))
				{
					readErrorFlag = true;
					break;
				}
			}
			if (readErrorFlag)
			{
				final String excep = (exceptionType.equalsIgnoreCase(MarketplacecclientservicesConstants.EXCEPTION_TYPE_PINCODE)) ? MarketplacecclientservicesConstants.O0002_EXCEP
						: MarketplacecclientservicesConstants.O0004_EXCEP;
				throw new ClientEtailNonBusinessExceptions(excep, ex);
			}
		}
	}

	/**
	 * this method prepares request object and calls oms to get inventories for stores.
	 *
	 * @param storeLocationRequestDataList
	 * @return StoreLocatorAtsResponseObject
	 *
	 */
	@Override
	public StoreLocatorAtsResponseObject prepStoreLocationsToOMS(
			final List<StoreLocationRequestData> storeLocationRequestDataList, final CartModel cartModel)
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
				//Fix for anonymous cart from mobile
				/*
				 * if (cartModel == null) { cartModel = cartService.getSessionCart(); }
				 */
				if (null != cartModel && null != cartModel.getGuid())
				{
					storeLocatorRequest.setCartId(cartModel.getGuid());
					LOG.debug("Cart ID is :" + cartModel.getGuid());
				}
				storeLocatorRequest.setItem(storeLocatorList);
				LOG.debug("****************calls to oms to get valid stores having inventories*************");
				storeLocatorResfromOMS = sendStoreLocatorstoOMS(storeLocatorRequest);
			}
		}
		catch (final Exception e)
		{
			//pincodeResfromOMS = null;
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
			throw new ClientEtailNonBusinessExceptions("O0001", e);
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

			// Comented below code Because no StoreATS Call to OMS
			//			final String omsstoreServiceability = configurationService.getConfiguration()
			//					.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_REALCALL).trim();
			//			String mockXMLFirstPhase = configurationService.getConfiguration()
			//					.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_FIRSTPHASE).trim();
			//			final String mockXMLSecondPhase = configurationService.getConfiguration()
			//					.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_SECONDPHASE).trim();
			//			final String mockXMLThirdPhase = configurationService.getConfiguration()
			//					.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_THIRDPHASE).trim();

			//			if (omsstoreServiceability != null && mockXMLFirstPhase != null && !mockXMLFirstPhase.isEmpty()
			//					&& mockXMLSecondPhase != null && !mockXMLSecondPhase.isEmpty() && mockXMLThirdPhase != null
			//					&& !mockXMLThirdPhase.isEmpty() && omsstoreServiceability.equalsIgnoreCase("N"))
			//			{
			//				LOG.debug("Try to prepare non-real time OMS call get some stores and inventories");
			//				for (final StoreLocatorItem entry : storeLocatorRequest.getItem())
			//				{
			//					String mockXMLSecond = configurationService.getConfiguration()
			//							.getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_SECONDPHASE).trim();
			//					mockXMLSecond = mockXMLSecond.replaceAll("<replaceUssid>", entry.getUssId());
			//					mockXMLFirstPhase += mockXMLSecond;
			//				}
			//				final String output = mockXMLFirstPhase + mockXMLThirdPhase;
			//				LOG.debug("*********************** StoreLocator  non- real time response xml :" + output);
			//				//Commented as a part of TISPT-401
			//				//Single Instance Declared for the whole class
			//				//final JAXBContext jaxbContext = JAXBContext.newInstance(StoreLocatorAtsResponseObject.class);
			//				final Unmarshaller unmarshaller = context.createUnmarshaller();
			//				final StringReader reader = new StringReader(output);
			//				responsefromOMS = (StoreLocatorAtsResponseObject) unmarshaller.unmarshal(reader);
			//			}
			//	else
			//	{
			ClientResponse response = null;
			try
			{ //Commented as a part of TISPT-401
			  //Single Instance Declared for the whole class
			  //final Client client = Client.create();

				//Start : Code added for OMS fallback cases
				final String connectionTimeout = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_PINCODESERVICEABILITY_CON_TIMEOUT, "5000").trim();
				final String readTimeout = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_PINCODESERVICEABILITY_READ_TIMEOUT, "5000").trim();
				final String httpErrorCode = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_HTTP_ERROR_CODE, "404,503").trim();

				client.setConnectTimeout(Integer.valueOf(connectionTimeout));
				client.setReadTimeout(Integer.valueOf(readTimeout));
				//End : Code added for OMS fallback cases

				final WebResource webResource = client.resource(UriBuilder.fromUri(
						configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.URLFOR_STORELOC_URL))
						.build());
				//Commented as a part of TISPT-401
				//Single Instance Declared for the whole class
				//final JAXBContext context = JAXBContext.newInstance(StoreLocatorATS.class);
				final Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				final StringWriter sw = new StringWriter();
				m.marshal(storeLocatorRequest, sw);
				final String xmlString = sw.toString();

				LOG.debug("*********************** StoreLocator Real Time Serviceability request xml :" + xmlString);
				response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("X-tenantId", "single")
						.entity(xmlString).post(ClientResponse.class);

				LOG.info("*****StoreATS response status code :" + response.getStatus());
				if (httpErrorCode.contains(String.valueOf(response.getStatus())))
				{
					throw new ClientEtailNonBusinessExceptions("O0007");
				}
			}
			catch (final ClientEtailNonBusinessExceptions ex)
			{
				LOG.error("Http Error in calling OMS - " + ex.getMessage());
				throw ex;
			}
			catch (final Exception ex)
			{
				LOG.error("Error in calling OMS - " + ex.getMessage());
				throw ex;
			}

			final String output = response.getEntity(String.class);
			LOG.debug("*********************** StoreLocator Real Time Serviceability response xml :" + output);
			//Commented as a part of TISPT-401
			//Single Instance Declared for the whole class
			//final JAXBContext jaxbContext = JAXBContext.newInstance(StoreLocatorAtsResponseObject.class);
			final Unmarshaller unmarshaller = context.createUnmarshaller();

			final StringReader reader = new StringReader(output);
			responsefromOMS = (StoreLocatorAtsResponseObject) unmarshaller.unmarshal(reader);
		}
		//	}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error("Http Error in calling OMS - " + ex.getMessage());
			throw ex;
		}
		catch (final Exception ex)
		{
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS, ex);

			if (ex.getMessage().contains("connect timed out") || ex.getMessage().contains("Connection refused"))
			{
				throw new ClientEtailNonBusinessExceptions("O0001", ex);
			}
			if (ex.getMessage().contains("read timed out"))
			{
				throw new ClientEtailNonBusinessExceptions("O0002", ex);
			}
			throw new ClientEtailNonBusinessExceptions(ex);
		}
		return responsefromOMS;
	}
}
