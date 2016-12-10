package com.tisl.mpl.cockpits.cscockpit.widgets.helpers.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.CNCServiceableSlavesData;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.product.data.ServiceableSlavesData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.util.WeakArrayList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl.MarketplaceSearchCommandControllerImpl;
import com.tisl.mpl.cockpits.cscockpit.widgets.helpers.MarketplaceServiceabilityCheckHelper;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.mplconfig.service.MplConfigService;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeRestrictionService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.service.PinCodeDeliveryModeService;
import com.tisl.mpl.wsdto.CNCServiceableSlavesWsDTO;
import com.tisl.mpl.wsdto.DeliveryModeResOMSWsDto;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeListResponse;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeResponse;
import com.tisl.mpl.wsdto.ServiceableSlavesDTO;


// TODO: Auto-generated Javadoc
/**
 * The Class MarketplaceServiceabilityCheckHelperImpl.
 */
public class MarketplaceServiceabilityCheckHelperImpl implements MarketplaceServiceabilityCheckHelper
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(MarketplaceSearchCommandControllerImpl.class);

	/** The Constant ZERO. */
	// private static final String ZERO = "0";

	/** The Constant ERROR_400. */

	/** The mpl pincode restriction service. */
	@Resource(name = "mplPincodeRestrictionService")
	private MplPincodeRestrictionService mplPincodeRestrictionService;

	/** The pin code delivery mode service. */
	@Resource(name = "pinCodeDeliveryModeService")
	private PinCodeDeliveryModeService pinCodeDeliveryModeService;

	/** The price data factory. */
	@Autowired
	private PriceDataFactory priceDataFactory;

	@Autowired
	private BuyBoxService buyBoxService;
	@Autowired
	private BuyBoxFacade buyBoxFacade;

	@Resource(name = "mplCommerceCartService")
	private MplCommerceCartService mplCommerceCartService;


	@Autowired
	private PincodeService pincodeService;

	public BuyBoxService getBuyBoxService()
	{
		return buyBoxService;
	}

	@Autowired
	private SessionService sessionService;

	@Required
	public void setBuyBoxService(final BuyBoxService buyBoxService)
	{
		this.buyBoxService = buyBoxService;
	}

	@Autowired
	private MplConfigService mplConfigService;
	
	@Autowired
	 private MplSellerInformationService mplSellerInformationService;

	/**
	 * Gets the response for pin code.
	 *
	 * @param product
	 *           the product
	 * @param pin
	 *           the pin
	 * @param isDeliveryDateRequired
	 *           the is delivery date required
	 * @return listingId
	 * @throws EtailNonBusinessExceptions
	 *            the etail non business exceptions
	 * @throws ClientEtailNonBusinessExceptions
	 *            the client etail non business exceptions
	 * @description this method checks the restriction list and calls pincode service accordingly
	 */
	@Override
	public List<PinCodeResponseData> getResponseForPinCode(final String cartId,final ProductModel product, final String pin,
			final String isDeliveryDateRequired, final String ussid) throws EtailNonBusinessExceptions,
			ClientEtailNonBusinessExceptions
	{
		List<PinCodeResponseData> response = null;
		LOG.debug("productCode:" + product.getCode() + "pinCode:" + pin);
		final PincodeModel pinCodeModelObj = pincodeService.getLatAndLongForPincode(pin);
		final LocationDTO dto = new LocationDTO();
		Location myLocation = null;
		final boolean isPincodeServicable = Boolean.TRUE;
		sessionService.setAttribute("isPincodeServicable", isPincodeServicable);
		if (null == pinCodeModelObj)
		{

			sessionService.setAttribute("isPincodeServicable", false);
		}

		if (null != pinCodeModelObj)
		{
			try
			{
				//	final String configurableRadius = Config.getParameter("marketplacestorefront.configure.radius");
				final String configurableRadius = mplConfigService
						.getConfigValueById(MarketplaceFacadesConstants.CONFIGURABLE_RADIUS);
				LOG.debug("configurableRadius is:" + configurableRadius);
				dto.setLongitude(pinCodeModelObj.getLongitude().toString());
				dto.setLatitude(pinCodeModelObj.getLatitude().toString());
				myLocation = new LocationDtoWrapper(dto);
				LOG.debug("Selected Location for Latitude:" + myLocation.getGPS().getDecimalLatitude());
				LOG.debug("Selected Location for Longitude:" + myLocation.getGPS().getDecimalLongitude());

				/*
				 * final List<PincodeServiceData> requestData = populatePinCodeServiceData(product, isDeliveryDateRequired,
				 * ussid);
				 */
				final List<PincodeServiceData> requestData = populatePinCodeServiceData(cartId,product, isDeliveryDateRequired, ussid,
						myLocation.getGPS(), Double.parseDouble(configurableRadius));

				final List<String> ussidList = new ArrayList<String>();
				final List<String> sellerIdList = new ArrayList<String>();
				for (final PincodeServiceData reqData : requestData)
				{
					ussidList.add(reqData.getUssid());
					sellerIdList.add(reqData.getSellerId());
				}

				//checing if any restricted pincodes are present
				final List<PincodeServiceData> validReqData = mplPincodeRestrictionService.getRestrictedPincode(ussidList,
						sellerIdList, product.getCode(), pin, requestData);
				/* List<PinCodeResponseData> response = null; */
				if ((null != validReqData))
				{

					response = getAllResponsesForPinCode(pin, validReqData);

				}

				return response;
			}

			catch (final Exception e)
			{
				LOG.error("configurableRadius values is empty please add radius property in properties file ");
			}
		}
		return response;
	}

	/**
	 * Gets the product value.
	 *
	 * @param productModel
	 *           the product model
	 * @return the product value
	 */
	@Override
	public List<String> getSellerDetails(final ProductModel productModel)
	{

		if (null != productModel)
		{
			final Collection<SellerInformationModel> sellerInfoList = productModel.getSellerInformationRelator();
			final List<String> sellerNames = new WeakArrayList<String>();
			if (CollectionUtils.isNotEmpty(sellerInfoList))
			{
				for (final SellerInformationModel sellerInfo : sellerInfoList)
				{
					sellerNames.add(sellerInfo.getSellerID());
				}
			}
			return sellerNames;
		}
		return null;
	}

	/**
	 * Gets the all responses for pin code.
	 *
	 * @param pin
	 *           the pin
	 * @param reqData
	 *           the req data
	 * @return the all responses for pin code
	 * @throws EtailNonBusinessExceptions
	 *            the etail non business exceptions
	 * @throws ClientEtailNonBusinessExceptions
	 *            the client etail non business exceptions
	 * @description this method gets all the responses about servicable pincodes from OMS
	 */

	private List<PinCodeResponseData> getAllResponsesForPinCode(final String pin, final List<PincodeServiceData> reqData)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions
	{

		List<PinCodeResponseData> responseList = new ArrayList<PinCodeResponseData>();
		try
		{
			//fetching response   from oms  against the pincode
			PinCodeDeliveryModeListResponse response = null;
			try
			{
				response = pinCodeDeliveryModeService.prepPinCodeDeliveryModetoOMS(pin, reqData);
			}
			catch (final ClientEtailNonBusinessExceptions e)
			{
				LOG.error("::::::Exception in calling OMS Pincode service::CSCOCKPIT:::::::" + e.getErrorCode());
				if (null != e.getErrorCode()
						&& (MarketplacecclientservicesConstants.O0001_EXCEP.equalsIgnoreCase(e.getErrorCode())
								|| MarketplacecclientservicesConstants.O0002_EXCEP.equalsIgnoreCase(e.getErrorCode()) || MarketplacecclientservicesConstants.O0007_EXCEP
									.equalsIgnoreCase(e.getErrorCode())))
				{
					response = getMplCommerceCartService().callPincodeServiceabilityCommerce(pin, reqData);
				}
			}

			if (null != response.getItem())
			{
				PinCodeResponseData responseData = null;
				for (final PinCodeDeliveryModeResponse deliveryModeResponse : response.getItem())
				{
					boolean servicable = false;
					final List<Integer> stockCount = new ArrayList<Integer>();
					List<DeliveryDetailsData> deliveryDataList = null;
					responseData = new PinCodeResponseData();
					//responseData.set
					responseData.setTransportMode(deliveryModeResponse.getTransportMode());
					if (null != deliveryModeResponse.getDeliveryMode())
					{
						deliveryDataList = new ArrayList<DeliveryDetailsData>();
						for (final DeliveryModeResOMSWsDto deliveryMode : deliveryModeResponse.getDeliveryMode())
						{
							if (deliveryMode.getIsPincodeServiceable().equalsIgnoreCase(MarketplaceCockpitsConstants.YES))
							{
								servicable = true;
							}

							if (deliveryMode.getIsCOD().equalsIgnoreCase(MarketplaceCockpitsConstants.YES))
							{
								responseData.setCod(deliveryMode.getIsCOD());
							}

							final DeliveryDetailsData data = new DeliveryDetailsData();
							stockCount.add(Integer.valueOf(Integer.parseInt(deliveryMode.getInventory())));

							// Added By Prasad
							if (deliveryMode.getIsPincodeServiceable().equalsIgnoreCase(MarketplaceCockpitsConstants.YES))
							{
								data.setType(deliveryMode.getType());
							}
							data.setInventory(deliveryMode.getInventory());
							data.setIsCODLimitFailed((MarketplaceCockpitsConstants.YES).equals(deliveryMode.getIsCODLimitFailed()) ? true
									: false);
							data.setIsCOD((MarketplaceCockpitsConstants.YES).equals(deliveryMode.getIsCOD()) ? true : false);
							data.setIsPincodeServiceable((MarketplaceCockpitsConstants.YES).equals(deliveryMode
									.getIsPincodeServiceable()) ? true : false);
							data.setIsPrepaidEligible(deliveryMode.getIsPrepaidEligible().equals(MarketplaceCockpitsConstants.YES) ? true
									: false);
							responseData.setIsPrepaidEligible(deliveryMode.getIsPrepaidEligible());// set payment mode
							data.setFulfilmentType(deliveryMode.getFulfillmentType());
							if (null != deliveryMode.getServiceableSlaves() && deliveryMode.getServiceableSlaves().size() > 0)
							{
								data.setServiceableSlaves(populatePincodeServiceableData(deliveryMode.getServiceableSlaves()));
							}

							if (null != deliveryMode.getCNCServiceableSlaves() && deliveryMode.getCNCServiceableSlaves().size() > 0)
							{
								final List<CNCServiceableSlavesData> cncServiceableSlavesDataList = new ArrayList<CNCServiceableSlavesData>();
								CNCServiceableSlavesData cncServiceableSlavesData = null;
								for (final CNCServiceableSlavesWsDTO dto : deliveryMode.getCNCServiceableSlaves())
								{
									cncServiceableSlavesData = new CNCServiceableSlavesData();
									cncServiceableSlavesData.setStoreId(dto.getStoreId());
									cncServiceableSlavesData.setQty(dto.getQty());
									cncServiceableSlavesData.setFulfillmentType(dto.getFulfillmentType());
									cncServiceableSlavesData.setServiceableSlaves(populatePincodeServiceableData(dto.getServiceableSlaves()));
									cncServiceableSlavesDataList.add(cncServiceableSlavesData);
								}
								data.setCNCServiceableSlavesData(cncServiceableSlavesDataList);
							}
							deliveryDataList.add(data);

							if (!(stockCount.isEmpty()))
							{
								responseData.setStockCount(Collections.max(stockCount));
							}
							else
							{
								responseData.setStockCount(Integer.valueOf(0));
							}
							if (servicable)
							{
								responseData.setIsServicable(MarketplaceCockpitsConstants.YES);
							}
							responseData.setUssid(deliveryModeResponse.getUSSID());
							responseData.setValidDeliveryModes(deliveryDataList);

						}
					}
					if (!servicable)
					{
						// responseData = new PinCodeResponseData();
						responseData.setIsServicable(MarketplaceCockpitsConstants.NO);
					}
					// responseData.setUssid(deliveryModeResponse.getUSSID());
					responseList.add(responseData);

				}
			}
		}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error("*********OMS service is down");
			responseList = null;
		}
		return responseList;
	}






	/**
	 * Populate pin code service data1.
	 *
	 * @param productModel
	 *           the product model
	 * @param isDeliveryDateRequired
	 *           the is delivery date required
	 * @return the list
	 */
	private List<PincodeServiceData> populatePinCodeServiceData(final ProductModel productModel,
			final String isDeliveryDateRequired, final String ussid)
	{
		final List<PincodeServiceData> requestData = new WeakArrayList<>();
		PincodeServiceData data = new PincodeServiceData();
		final List<SellerInformationData> sellers = buyBoxFacade.getsellersDetails(productModel.getCode());
		try
		{
			final List<BuyBoxModel> lst = getBuyBoxService().getBuyboxPricesForSearch(productModel.getCode());
			for (final BuyBoxModel buybox : lst)
			{
				if (StringUtils.isNotEmpty(ussid))
				{
					if (buybox.getSellerArticleSKU().equalsIgnoreCase(ussid))
					{
						for (final SellerInformationData sd : sellers)
						{
							if (sd.getSellerID().equalsIgnoreCase(buybox.getSellerId()))
							{
								// Added For Seller Handling Time
								SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(ussid);
								List<RichAttributeModel> sellerRichAttributeModel = null;
							   	int sellerHandlingTime=0;
							   	String sellerRichAttrForHandlingTime=null;
									if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
									{
										sellerRichAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
										if (sellerRichAttributeModel != null && sellerRichAttributeModel.get(0).getSellerHandlingTime() != null)
										{
											sellerRichAttrForHandlingTime = sellerRichAttributeModel.get(0).getSellerHandlingTime().toString();
											if( StringUtils.isNotEmpty(sellerRichAttrForHandlingTime)){
												sellerHandlingTime=Integer.parseInt(sellerRichAttrForHandlingTime);
											}
											
										}
										data.setSellerHandlingTime(Integer.valueOf(sellerHandlingTime));
									}else{
										data.setSellerHandlingTime(Integer.valueOf(sellerHandlingTime));
									}
								// Close seller Handling Time
								data = new PincodeServiceData();
								data.setIsCOD(sd.getIsCod());
								data.setDeliveryModes(sd.getDeliveryModes());
								data.setTransportMode(sd.getShippingMode());
								data.setFullFillmentType(sd.getFullfillment());
								data.setDeliveryFulfillModeByP1(sd.getDeliveryFulfillModebyP1());
								data.setSellerId(buybox.getSellerId());
								data.setUssid(buybox.getSellerArticleSKU());
								data.setIsDeliveryDateRequired(isDeliveryDateRequired);
								data.setPrice(buybox.getPrice());
								data.setMopPrice(formPriceData(buybox.getPrice()));
								requestData.add(data);
							}
						}
					}
				}
				else
				{
					for (final SellerInformationData sd : sellers)
					{
						if (sd.getSellerID().equalsIgnoreCase(buybox.getSellerId()))
						{
							data = new PincodeServiceData();
							data.setIsCOD(sd.getIsCod());
							data.setDeliveryModes(sd.getDeliveryModes());
							data.setTransportMode(sd.getShippingMode());
							data.setFullFillmentType(sd.getFullfillment());
							data.setSellerId(buybox.getSellerId());
							data.setUssid(buybox.getSellerArticleSKU());
							data.setIsDeliveryDateRequired(isDeliveryDateRequired);
							data.setPrice(buybox.getPrice());
							data.setMopPrice(formPriceData(buybox.getPrice()));
							requestData.add(data);
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception in populatePinCodeServiceData", e);
			throw new EtailNonBusinessExceptions(e);
		}

		return requestData;
	}




	// overriding method to get stores and radius For entered pincode

	/**
	 * Populate pin code service data1.
	 *
	 * @param productModel
	 *           the product model
	 * @param isDeliveryDateRequired
	 *           the is delivery date required
	 * @return the list
	 */

	private List<PincodeServiceData> populatePinCodeServiceData(final String cartId,final ProductModel productModel,
			final String isDeliveryDateRequired, final String ussid, final GPS gps, final Double configurableRadius)
	{
		final List<PincodeServiceData> requestData = new WeakArrayList<>();
		PincodeServiceData data = new PincodeServiceData();
		final List<SellerInformationData> sellers = buyBoxFacade.getsellersDetails(productModel.getCode());
		try
		{
			final List<BuyBoxModel> lst = getBuyBoxService().getBuyboxPricesForSearch(productModel.getCode());
			for (final BuyBoxModel buybox : lst)
			{
				if (StringUtils.isNotEmpty(ussid))
				{
					if (buybox.getSellerArticleSKU().equalsIgnoreCase(ussid))
					{
						for (final SellerInformationData sd : sellers)
						{
							if (sd.getSellerID().equalsIgnoreCase(buybox.getSellerId()))
							{
								// Added For Seller Handling Time
								SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(ussid);
								List<RichAttributeModel> sellerRichAttributeModel = null;
							   	int sellerHandlingTime=0;
							   	String sellerRichAttrForHandlingTime=null;
									if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
									{
										sellerRichAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
										if (sellerRichAttributeModel != null && sellerRichAttributeModel.get(0).getSellerHandlingTime() != null)
										{
											sellerRichAttrForHandlingTime = sellerRichAttributeModel.get(0).getSellerHandlingTime().toString();
											if( StringUtils.isNotEmpty(sellerRichAttrForHandlingTime)){
												sellerHandlingTime=Integer.parseInt(sellerRichAttrForHandlingTime);
											}
											
										}
										data.setSellerHandlingTime(Integer.valueOf(sellerHandlingTime));
									}else{
										data.setSellerHandlingTime(Integer.valueOf(sellerHandlingTime));
									}
								// Close seller Handling Time
								data = new PincodeServiceData();
								data.setIsCOD(sd.getIsCod());
								data.setDeliveryModes(sd.getDeliveryModes());
								if(null != sd.getShippingMode()) {
									data.setTransportMode(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(sd.getShippingMode().toUpperCase()));
								}
								data.setDeliveryFulfillModeByP1(sd.getDeliveryFulfillModebyP1());
								data.setFullFillmentType(sd.getFullfillment());
								data.setSellerId(buybox.getSellerId());
								data.setUssid(buybox.getSellerArticleSKU());
								data.setIsDeliveryDateRequired(isDeliveryDateRequired);
								data.setPrice(buybox.getPrice());
								data.setMopPrice(formPriceData(buybox.getPrice()));
								data.setIsFragile(sd.getIsFragile());
								data.setIsPrecious(sd.getIsPrecious());
								data.setSellerHandlingTime(123654);
								if(null != cartId) {
									data.setCartId(cartId);
								}
								// Added To get Near By Stores
								final List<Location> storeList = pincodeService.getSortedLocationsNearby(gps, configurableRadius,
										sd.getSellerID());
								if (null != storeList && storeList.size() > 0)
								{
									final List<String> locationList = new ArrayList<String>();
									for (final Location location : storeList)
									{
										locationList.add(location.getName());
									}
									LOG.debug("locationList:" + locationList.size());
									data.setStore(locationList);
								}

								requestData.add(data);
							}
						}
					}
				}
				else
				{
					for (final SellerInformationData sd : sellers)
					{
						if (sd.getSellerID().equalsIgnoreCase(buybox.getSellerId()))
						{
							data = new PincodeServiceData();
							data.setIsCOD(sd.getIsCod());
							data.setDeliveryModes(sd.getDeliveryModes());
							if(null != sd.getShippingMode()) {
							  data.setTransportMode(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(sd.getShippingMode().toUpperCase()));
							}
							data.setDeliveryFulfillModeByP1(sd.getDeliveryFulfillModebyP1());
							data.setFullFillmentType(sd.getFullfillment());
							data.setSellerId(buybox.getSellerId());
							data.setUssid(buybox.getSellerArticleSKU());
							data.setIsDeliveryDateRequired(isDeliveryDateRequired);
							data.setPrice(buybox.getPrice());
							data.setMopPrice(formPriceData(buybox.getPrice()));
                            if(null != cartId) {
                            	data.setCartId(cartId);
                            }
							// Added To get Near By Stores
							final List<Location> storeList = pincodeService.getSortedLocationsNearby(gps, configurableRadius,
									sd.getSellerID());
							if (null != storeList && storeList.size() > 0)
							{
								final List<String> locationList = new ArrayList<String>();
								for (final Location location : storeList)
								{
									locationList.add(location.getName());
								}
								LOG.debug("locationList:" + locationList.size());
								data.setStore(locationList);
							}
							requestData.add(data);
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception in populatePinCodeServiceData", e);
			throw new EtailNonBusinessExceptions(e);
		}

		return requestData;
	}

	@Override
	public List<SellerInformationData> getSellerInformation(final ProductModel productModel)
	{
		final List<SellerInformationData> sellers = buyBoxFacade.getsellersDetails(productModel.getCode());
		return sellers;

	}

	/**
	 * Converting datatype of price.
	 *
	 * @param price
	 *           the price
	 * @return pData
	 */

	private PriceData formPriceData(final Double price)
	{
		final PriceData priceData = new PriceData();
		priceData.setPriceType(PriceDataType.BUY);
		priceData.setValue(new BigDecimal(price.doubleValue()));
		priceData.setCurrencyIso(MarketplaceCockpitsConstants.INR);
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(priceData.getCurrencyIso());
		currency.setSymbol(priceData.getCurrencyIso());
		final PriceData pData = priceDataFactory.create(PriceDataType.BUY, priceData.getValue(), currency);
		return pData;
	}

	
	private List<ServiceableSlavesData> populatePincodeServiceableData(final List<ServiceableSlavesDTO> serviceableSlavesDTOList)
	{

		final List<ServiceableSlavesData> serviceableSlavesDataList = new ArrayList<ServiceableSlavesData>();
		ServiceableSlavesData serviceableSlavesData = null;
		for (final ServiceableSlavesDTO dto : serviceableSlavesDTOList)
		{
			serviceableSlavesData = new ServiceableSlavesData();
			serviceableSlavesData.setSlaveId(dto.getSlaveId());
			serviceableSlavesData.setLogisticsID(dto.getLogisticsID());
			serviceableSlavesData.setPriority(dto.getPriority());
			serviceableSlavesData.setCodEligible(dto.getCODEligible());
			serviceableSlavesDataList.add(serviceableSlavesData);
		}
		return serviceableSlavesDataList;
	}
	
	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}

	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}


}