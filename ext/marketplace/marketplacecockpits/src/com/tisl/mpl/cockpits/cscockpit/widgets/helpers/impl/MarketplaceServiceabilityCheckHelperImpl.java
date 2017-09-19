package com.tisl.mpl.cockpits.cscockpit.widgets.helpers.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CNCServiceableSlavesData;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.product.data.ServiceableSlavesData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.util.WeakArrayList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl.MarketplaceSearchCommandControllerImpl;
import com.tisl.mpl.cockpits.cscockpit.widgets.helpers.MarketplaceServiceabilityCheckHelper;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.mplconfig.service.MplConfigService;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeRestrictionService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pincode.facade.PinCodeServiceAvilabilityFacade;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.service.PinCodeDeliveryModeService;
import com.tisl.mpl.util.ExceptionUtil;
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
	private static final Logger LOG = Logger.getLogger(MarketplaceServiceabilityCheckHelperImpl.class);

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
	@Resource
	private PriceDataFactory priceDataFactory;

	@Resource(name = "buyBoxService")
	private BuyBoxService buyBoxService;
	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;

	@Resource(name = "mplCommerceCartService")
	private MplCommerceCartService mplCommerceCartService;

	@Resource(name = "pincodeService")
	private PincodeService pincodeService;
	
	@Resource(name = "pinCodeFacade")
	private PinCodeServiceAvilabilityFacade pinCodeFacade;
	
	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "mplConfigService")
	private MplConfigService mplConfigService;
	
	@Resource(name = "mplSellerInformationService")
	private MplSellerInformationService mplSellerInformationService;
	@Resource(name = "mplCheckoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;
	@Resource(name = "productFacade")
	private ProductFacade productFacade;
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
		final LocationDTO dto = new LocationDTO();
		Location myLocation = null;
		final boolean isPincodeServicable = Boolean.FALSE;
		List<ResourceMessage> errorMessages = new ArrayList<ResourceMessage>();
		//TISSEC-11
		final String regex = "\\d{6}";
		try
		{
		if (pin.matches(regex))
		{
			LOG.debug("productCode:" + product.getCode()  + "pinCode:" + pin);
			final PincodeModel pinCodeModelObj = pincodeServiceFacade.getLatAndLongForPincode(pin);
			if (null == pinCodeModelObj)
			{
				sessionService.setAttribute("isPincodeServicable", false);
			}
			else
			{
				try
				{
					sessionService.setAttribute("isPincodeServicable", isPincodeServicable);
					
					dto.setLongitude(pinCodeModelObj.getLongitude().toString());
					dto.setLatitude(pinCodeModelObj.getLatitude().toString());
					myLocation = new LocationDtoWrapper(dto);
					LOG.debug("Selected Location for Latitude:" + myLocation.getGPS().getDecimalLatitude());
					LOG.debug("Selected Location for Longitude:" + myLocation.getGPS().getDecimalLongitude());
					sessionService.setAttribute(MarketplaceCockpitsConstants.PIN_CODE, pin);
					final String configRadius = mplConfigService.getConfigValueById(MarketplaceFacadesConstants.CONFIGURABLE_RADIUS);
					final double configurableRadius = Double.parseDouble(configRadius);
					LOG.debug("**********configrableRadius:" + configurableRadius);
					response = getAllResponsesForPinCode(product.getCode() , pin,
							populatePinCodeServiceData(cartId,product,
									isDeliveryDateRequired, ussid, myLocation.getGPS(), configurableRadius));

					return response;
				}
				catch (final Exception e)
				{
					LOG.debug("configurableRadius values is empty please add radius property in properties file ");
					ExceptionUtil.getCustomizedExceptionTrace(e);
				}
			}

		}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
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
	
	private List<PinCodeResponseData> getAllResponsesForPinCode(final String productCode, final String pin, final List<PincodeServiceData> requestData)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions
	{

		List<PinCodeResponseData> responseList = new ArrayList<PinCodeResponseData>();
		//fetching response   from oms  against the pincode
		PinCodeDeliveryModeListResponse response = null;
		try
		{
			final List<String> ussidList = new ArrayList<String>();
			final List<String> sellerIdList = new ArrayList<String>();
			for (final PincodeServiceData reqData : requestData)
			{
				ussidList.add(reqData.getUssid());
				sellerIdList.add(reqData.getSellerId());
			}

			//checing if any restricted pincodes are present
			final List<PincodeServiceData> validReqData = mplPincodeRestrictionService.getRestrictedPincode(ussidList, sellerIdList,
					productCode, pin, requestData);
			
			if (CollectionUtils.isNotEmpty(validReqData))
			{
				try
				{
					response = pinCodeDeliveryModeService.prepPinCodeDeliveryModetoOMS(pin, requestData);
				}
				catch (final ClientEtailNonBusinessExceptions e)
				{
					LOG.error("::::::Exception in calling OMS Pincode service::CSCOCKPIT:::::::" + e.getErrorCode());
					if (null != e.getErrorCode()
							&& (MarketplacecclientservicesConstants.O0001_EXCEP.equalsIgnoreCase(e.getErrorCode())
									|| MarketplacecclientservicesConstants.O0002_EXCEP.equalsIgnoreCase(e.getErrorCode()) || MarketplacecclientservicesConstants.O0007_EXCEP
										.equalsIgnoreCase(e.getErrorCode())))
					{
						response = getMplCommerceCartService().callPincodeServiceabilityCommerce(pin, requestData);
					}
				}
				//TISPRDT-1177
				boolean isCod = false;
				if (CollectionUtils.isNotEmpty(response.getItem()))
				{
					for (final PinCodeDeliveryModeResponse deliveryModeResponse : response.getItem())
					{
						final List<Integer> stockCount = new ArrayList<Integer>();
						List<DeliveryDetailsData> deliveryDataList = null;
						PinCodeResponseData responseData = null;
						boolean servicable = false;

						if (null != deliveryModeResponse.getDeliveryMode())
						{
							deliveryDataList = new ArrayList<DeliveryDetailsData>();
							for (final DeliveryModeResOMSWsDto deliveryMode : deliveryModeResponse.getDeliveryMode())
							{

								if (null != deliveryMode.getIsPincodeServiceable()
										&& deliveryMode.getIsPincodeServiceable().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
								{

									servicable = true;
									responseData = new PinCodeResponseData();
									responseData.setIsPrepaidEligible(deliveryMode.getIsPrepaidEligible());
									responseData.setIsCODLimitFailed(deliveryMode.getIsCODLimitFailed());

									if ((deliveryMode.getIsCOD() != null)
											&& (deliveryMode.getIsCOD().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y)))
									{
										isCod = true;
									}

									/*
									 * if (!(deliveryMode.getInventory().equals(MarketplacecommerceservicesConstants.ZERO))) {
									 */
									final DeliveryDetailsData data = new DeliveryDetailsData();
									stockCount.add(Integer.valueOf(deliveryMode.getInventory()));
									data.setType(deliveryMode.getType());
									data.setInventory(deliveryMode.getInventory());

									if (deliveryMode.getIsCOD() != null
											&& deliveryMode.getIsCOD().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
									{
										data.setIsCOD(Boolean.TRUE);
									}
									else if (deliveryMode.getIsCOD() != null
											&& deliveryMode.getIsCOD().equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
									{
										data.setIsCOD(Boolean.FALSE);
									}

									if (deliveryMode.getFulfillmentType() != null)
									{
										data.setFulfilmentType(deliveryMode.getFulfillmentType());
									}

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
											cncServiceableSlavesData.setServiceableSlaves(populatePincodeServiceableData(dto
													.getServiceableSlaves()));
											cncServiceableSlavesDataList.add(cncServiceableSlavesData);
										}
										data.setCNCServiceableSlavesData(cncServiceableSlavesDataList);
									}
									//
									data.setIsPincodeServiceable(Boolean.TRUE);
									if (deliveryMode.getFulfillmentType() != null && deliveryMode.getIsCODLimitFailed().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
									{	
										data.setIsCODLimitFailed(Boolean.TRUE); 
									}else{
										data.setIsCODLimitFailed(Boolean.FALSE); 
									}
									
									
									deliveryDataList.add(data);
									//	}
									responseData.setValidDeliveryModes(deliveryDataList);
									if (!(stockCount.isEmpty()))
									{
										responseData.setStockCount(Collections.max(stockCount));
									}
									/*
									 * else { responseData.setStockCount(Integer.valueOf(0)); }
									 */
									responseData.setIsServicable(MarketplacecommerceservicesConstants.Y);
									responseData.setUssid(deliveryModeResponse.getUSSID());
									LOG.debug(new StringBuffer().append("********************servicable ussids for pincode*******")
											.append(pin).append("are:").append(deliveryModeResponse.getUSSID()).toString());
									if (deliveryMode.getDeliveryDate() != null && !deliveryMode.getDeliveryDate().isEmpty())
									{
										responseData.setDeliveryDate(deliveryMode.getDeliveryDate());
									}
									if (isCod)
									{
										responseData.setCod(MarketplacecommerceservicesConstants.Y);
									}
									else
									{
										responseData.setCod(MarketplacecommerceservicesConstants.N);
									}
								}

							}
						}
						if (!servicable)
						{
							responseData = new PinCodeResponseData();
							responseData.setIsServicable(MarketplacecommerceservicesConstants.N);
							responseData.setUssid(deliveryModeResponse.getUSSID());
						}
						if (responseData != null)
						{
							responseList.add(responseData);
						}
					}
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
		PincodeServiceData data = null;
		//CKD: TPR-3809
		//final List<SellerInformationData> sellers = buyBoxFacade.getsellersDetails(productModel.getCode());
		final List<SellerInformationData> sellers = buyBoxFacade.getsellersDetails(productModel.getCode(),productModel.getProductCategoryType());
		try
		{
			final List<BuyBoxModel> lst = buyBoxService.getBuyboxPricesForSearch(productModel.getCode());
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
								if(null != sd.getSellerHandlingTime()) {
									data.setSellerHandlingTime(Integer.valueOf(sd.getSellerHandlingTime()));
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
							data.setTransportMode(sd.getShippingMode());
							data.setFullFillmentType(sd.getFullfillment());
							data.setSellerId(buybox.getSellerId());
							data.setUssid(buybox.getSellerArticleSKU());
							data.setIsDeliveryDateRequired(isDeliveryDateRequired);
							data.setPrice(buybox.getPrice());
							data.setMopPrice(formPriceData(buybox.getPrice()));
							if(null != sd.getSellerHandlingTime()) {
								data.setSellerHandlingTime(Integer.valueOf(sd.getSellerHandlingTime()));
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


	/**
	 *
	 * @param deliveryMode
	 * @param ussid
	 * @return
	 */
	private MarketplaceDeliveryModeData fetchDeliveryModeDataForUSSID(final String deliveryMode, final String ussid)
	{
		final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
		final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = mplCheckoutFacade
				.populateDeliveryCostForUSSIDAndDeliveryMode(deliveryMode, MarketplaceFacadesConstants.INR, ussid);

		final PriceData priceData = productDetailsHelper.formPriceData(mplZoneDeliveryModeValueModel.getValue());
		deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
		deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
		deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
		deliveryModeData.setSellerArticleSKU(ussid);
		deliveryModeData.setDeliveryCost(priceData);
		return deliveryModeData;
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
		PincodeServiceData data = null;
		MarketplaceDeliveryModeData deliveryModeData = null;
		try
		{
			final ProductData productData = productFacade.getProductForOptions(productModel,
					Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));

			for (final SellerInformationData seller : productData.getSeller())
			{
				final List<MarketplaceDeliveryModeData> deliveryModeList = new ArrayList<MarketplaceDeliveryModeData>();
				data = new PincodeServiceData();
				if ((null != seller.getDeliveryModes()) && !(seller.getDeliveryModes().isEmpty()))
				{
					for (final MarketplaceDeliveryModeData deliveryMode : seller.getDeliveryModes())
					{
						//CKD:TPR-3809//TISPRDT-2671
//						if (productModel.getProductCategoryType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY)){
//							deliveryModeData = fetchDeliveryModeDataForUSSID(deliveryMode.getCode(), ussid);
//						}
//						else{
						deliveryModeData = fetchDeliveryModeDataForUSSID(deliveryMode.getCode(), seller.getUssid());
						//}
						//deliveryModeData = fetchDeliveryModeDataForUSSID(deliveryMode.getCode(), seller.getUssid());
						deliveryModeList.add(deliveryModeData);
					}
					data.setDeliveryModes(deliveryModeList);
				}
				if (null != seller.getFullfillment() && StringUtils.isNotEmpty(seller.getFullfillment()))
				{
					data.setFullFillmentType(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getFullfillment().toUpperCase()));
				}
				LOG.debug("seller.getFullfillment() :" + seller.getFullfillment());
				LOG.debug("seller.getDeliveryFulfillModebyP1():" + seller.getDeliveryFulfillModebyP1());

				if (null != seller.getDeliveryFulfillModebyP1() && StringUtils.isNotEmpty(seller.getDeliveryFulfillModebyP1()))
				{
					data.setDeliveryFulfillModeByP1(seller.getDeliveryFulfillModebyP1().toUpperCase());
				}
				LOG.debug("seller.getDeliveryFulfillModebyP1()******:" + seller.getDeliveryFulfillModebyP1());
				LOG.debug("seller.getIsFragile()******:" + seller.getIsFragile());
				if (null != seller.getIsFragile() && StringUtils.isNotEmpty(seller.getIsFragile()))
				{
					data.setIsFragile(seller.getIsFragile().toUpperCase());
				}
				LOG.debug("seller.getIsPrecious()******:" + seller.getIsPrecious());
				if (null != seller.getIsPrecious() && StringUtils.isNotEmpty(seller.getIsPrecious()))
				{
					data.setIsPrecious(seller.getIsPrecious().toUpperCase());
				}


				if (null != seller.getShippingMode() && (StringUtils.isNotEmpty(seller.getShippingMode())))
				{
					data.setTransportMode(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getShippingMode().toUpperCase()));
				}
				if (null != seller.getSpPrice() && !(seller.getSpPrice().equals("")))
				{
					data.setPrice(new Double(seller.getSpPrice().getValue().doubleValue()));
				}
				else if (null != seller.getMopPrice() && !(seller.getMopPrice().equals("")))
				{
					data.setPrice(new Double(seller.getMopPrice().getValue().doubleValue()));
				}
				else if (null != seller.getMrpPrice() && !(seller.getMrpPrice().equals("")))
				{
					data.setPrice(new Double(seller.getMrpPrice().getValue().doubleValue()));
				}
				else
				{
					LOG.debug("No price avaiable for seller :" + seller.getSellerID());
					continue;
				}
				if (null != seller.getIsCod() && StringUtils.isNotEmpty(seller.getIsCod()))
				{
					data.setIsCOD(seller.getIsCod());
				}
				final List<Location> storeList = pincodeService.getSortedLocationsNearby(gps, configurableRadius,seller.getSellerID());
				if (CollectionUtils.isNotEmpty(storeList))
				{
					final List<String> locationList = new ArrayList<String>();
					for (final Location location : storeList)
					{
						locationList.add(location.getName());
					}
					LOG.debug("locationList:" + locationList.size());
					data.setStore(locationList);
				}

				final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(seller.getUssid());
				List<RichAttributeModel> sellerRichAttributeModel = null;
				int sellerHandlingTime = 0;
				String sellerRichAttrForHandlingTime = null;
				if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
				{
					sellerRichAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
					if (CollectionUtils.isNotEmpty(sellerRichAttributeModel)
							&& sellerRichAttributeModel.get(0).getSellerHandlingTime() != null)
					{
						sellerRichAttrForHandlingTime = sellerRichAttributeModel.get(0).getSellerHandlingTime().toString();
						if (StringUtils.isNotEmpty(sellerRichAttrForHandlingTime))
						{
							sellerHandlingTime = Integer.parseInt(sellerRichAttrForHandlingTime);
						}

					}
					data.setSellerHandlingTime(Integer.valueOf(sellerHandlingTime));
				}

				data.setSellerId(seller.getSellerID());
				//CKD:TPR-3809
				if (productModel.getProductCategoryType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY)){
					data.setUssid(ussid);
				}
				else{
				data.setUssid(seller.getUssid());
				}
				//data.setUssid(seller.getUssid());
				data.setIsDeliveryDateRequired("N");
				if(null != cartId) {
                	data.setCartId(cartId);
                }
				requestData.add(data);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}

		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return requestData;
	}

	@Override
	public List<SellerInformationData> getSellerInformation(final ProductModel productModel)
	{
		//CKD: TPR-3809
		//final List<SellerInformationData> sellers = buyBoxFacade.getsellersDetails(productModel.getCode());
		final List<SellerInformationData> sellers = buyBoxFacade.getsellersDetails(productModel.getCode(),productModel.getProductCategoryType());
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