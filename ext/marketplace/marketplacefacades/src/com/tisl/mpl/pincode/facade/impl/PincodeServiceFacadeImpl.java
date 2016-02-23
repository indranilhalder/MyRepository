/**
 *
 */
package com.tisl.mpl.pincode.facade.impl;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.pincode.facade.PinCodeServiceAvilabilityFacade;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author Techouts
 * 
 */
public class PincodeServiceFacadeImpl implements PincodeServiceFacade
{
	private static final Logger LOG = Logger.getLogger(PincodeServiceFacadeImpl.class);


	private PinCodeServiceAvilabilityFacade pinCodeFacade;
	private ProductService productService;
	private ProductFacade productFacade;
	private MplCheckoutFacade mplCheckoutFacade;
	private ProductDetailsHelper productDetailsHelper;
	private MplCartFacade mplCartFacade;
	private PincodeService pincodeService;

	/**
	 * This method is used to check pincode is serviceable are not
	 * 
	 * @param pincode
	 * @param productCode
	 * @return boolean value is serviceable
	 */
	@Override
	public boolean checkPincodeServiceble(final String pincode, final String productCode)
	{

		LOG.debug("in checkPincodeServiceble ");
		boolean ckeckClicNcollect = false;
		try
		{
			List<PinCodeResponseData> response = null;
			final List<StoreLocationResponseData> storeLocationResponseDataList = null;
			final List<StoreLocationRequestData> storeLocationRequestDataList = new ArrayList<StoreLocationRequestData>();
			//call to commerce db to get the latitude and longitude
			final PincodeModel pinCodeModelObj = pincodeService.getLatAndLongForPincode(pincode);
			if( null != pinCodeModelObj){
			 String configurableRadius = Config.getParameter("marketplacestorefront.configure.radius") != null ? Config.getParameter("marketplacestorefront.configure.radius") : "0";
			LOG.debug("configurableRadius is:" + Double.parseDouble(configurableRadius));
			final LocationDTO dto = new LocationDTO();
			dto.setLongitude(pinCodeModelObj.getLongitude().toString());
			dto.setLatitude(pinCodeModelObj.getLatitude().toString());
			final Location myLocation = new LocationDtoWrapper(dto);
			LOG.debug("Selected Location for Latitude..:" + myLocation.getGPS().getDecimalLatitude());
			LOG.debug("Selected Location for Longitude..:" + myLocation.getGPS().getDecimalLongitude());
			response = pinCodeFacade.getResonseForPinCode(productCode, pincode,
					populatePinCodeServiceData(productCode, myLocation.getGPS(), Double.parseDouble(configurableRadius)));

			for (final PinCodeResponseData pinCodeResData : response)
			{
				for (final DeliveryDetailsData deliveryData : pinCodeResData.getValidDeliveryModes())
				{
					LOG.info("Type:" + deliveryData.getType() + "---Inventory" + deliveryData.getInventory());
					if (deliveryData.getType().equalsIgnoreCase("CNC"))
					{
						ckeckClicNcollect = true;
					}
				}
			}
			return ckeckClicNcollect;
		 }
		}
		catch (final Exception e)
		{
			LOG.error("Exception for Pincode Serviceble check" + e);
			return ckeckClicNcollect;
		}
		return ckeckClicNcollect;
	}



	/**
	 * This method is used to prepare Storelocator response data
	 * 
	 * @param pincode
	 * @param sellerUssId
	 * @param productCode
	 * @return StoreLocationResponseData
	 */
	@Override
	public List<StoreLocationResponseData> getListofStoreLocationsforPincode(final String pincode, final String sellerUssId,
			final String productCode)
	{
		List<StoreLocationResponseData> storeLocationResponseDataList = null;
		try
		{
			final List<StoreLocationRequestData> storeLocationRequestDataList = new ArrayList<StoreLocationRequestData>();
			final PincodeModel pinCodeModelObj = pincodeService.getLatAndLongForPincode(pincode);
			if (null != pinCodeModelObj){
			final String configurableRadius = Config.getParameter("marketplacestorefront.configure.radius")!=null ? Config.getParameter("marketplacestorefront.configure.radius") : "0";
			LOG.debug("configurableRadius is:" + Double.parseDouble(configurableRadius));
			final LocationDTO dto = new LocationDTO();
			dto.setLongitude(pinCodeModelObj.getLongitude().toString());
			dto.setLatitude(pinCodeModelObj.getLatitude().toString());
			final Location myLocation = new LocationDtoWrapper(dto);

			final StoreLocationRequestData storeLocationRequestData = papulateClicknCollectRequestData(sellerUssId,
					myLocation.getGPS(), Double.parseDouble(configurableRadius));
			storeLocationRequestDataList.add(storeLocationRequestData);
			//call to OMS get the storelocations for given pincode
			storeLocationResponseDataList = mplCartFacade.getStoreLocationsforCnC(storeLocationRequestDataList);
			return storeLocationResponseDataList;
		 }
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}


		return null;
	}

	/**
	 * This methd prepares request data to send to oms for cnc.
	 * 
	 * @param sellerUssId
	 * @param gps
	 * @param configurableRadius
	 * @return
	 */
	public StoreLocationRequestData papulateClicknCollectRequestData(final String sellerUssId, final GPS gps,
			final Double configurableRadius)
	{
		LOG.debug("sellerUssId:" + sellerUssId);
		final String pincodeSellerId = sellerUssId.substring(0, 6);
		final StoreLocationRequestData storeLocationRequestData = new StoreLocationRequestData();
		final List<Location> storeList = pincodeService.getSortedLocationsNearby(gps, configurableRadius, pincodeSellerId);
		LOG.debug("StoreList size is :" + storeList.size());
		if (null != storeList && storeList.size() > 0)
		{
			final List<String> locationList = new ArrayList<String>();
			for (final Location location : storeList)
			{
				locationList.add(location.getName());
			}
			storeLocationRequestData.setStoreId(locationList);
		}
		storeLocationRequestData.setUssId(sellerUssId);
		return storeLocationRequestData;
	}

	/**
	 * This method gets pos based on pincode.
	 * 
	 * @param productCode
	 * @param gps
	 * @param configurableRadius
	 * @return
	 */
	private List<PincodeServiceData> populatePinCodeServiceData(final String productCode, final GPS gps,
			final Double configurableRadius)
	{
		LOG.debug("in populatePinCodeServiceData method");
		final List<PincodeServiceData> requestData = new ArrayList<PincodeServiceData>();
		PincodeServiceData data = null;
		MarketplaceDeliveryModeData deliveryModeData = null;
		try
		{
			final ProductModel productModel = productService.getProductForCode(productCode);
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
						deliveryModeData = fetchDeliveryModeDataForUSSID(deliveryMode.getCode(), seller.getUssid());
						deliveryModeList.add(deliveryModeData);
					}
					data.setDeliveryModes(deliveryModeList);
				}
				if (null != seller.getFullfillment() && StringUtils.isNotEmpty(seller.getFullfillment()))
				{
					data.setFullFillmentType(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getFullfillment().toUpperCase()));
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
				final List<Location> storeList = pincodeService.getSortedLocationsNearby(gps, configurableRadius,
						seller.getSellerID());
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
				data.setSellerId(seller.getSellerID());
				data.setUssid(seller.getUssid());
				data.setIsDeliveryDateRequired("N");
				requestData.add(data);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}

		catch (final Exception e)
		{

			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
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

	/**
	 * @return the pinCodeFacade
	 */
	public PinCodeServiceAvilabilityFacade getPinCodeFacade()
	{
		return pinCodeFacade;
	}

	/**
	 * @param pinCodeFacade
	 *           the pinCodeFacade to set
	 */
	public void setPinCodeFacade(final PinCodeServiceAvilabilityFacade pinCodeFacade)
	{
		this.pinCodeFacade = pinCodeFacade;
	}

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/**
	 * @return the mplCheckoutFacade
	 */
	public MplCheckoutFacade getMplCheckoutFacade()
	{
		return mplCheckoutFacade;
	}

	/**
	 * @param mplCheckoutFacade
	 *           the mplCheckoutFacade to set
	 */
	public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade)
	{
		this.mplCheckoutFacade = mplCheckoutFacade;
	}

	/**
	 * @return the productDetailsHelper
	 */
	public ProductDetailsHelper getProductDetailsHelper()
	{
		return productDetailsHelper;
	}

	/**
	 * @param productDetailsHelper
	 *           the productDetailsHelper to set
	 */
	public void setProductDetailsHelper(final ProductDetailsHelper productDetailsHelper)
	{
		this.productDetailsHelper = productDetailsHelper;
	}

	/**
	 * @return the mplCartFacade
	 */
	public MplCartFacade getMplCartFacade()
	{
		return mplCartFacade;
	}

	/**
	 * @param mplCartFacade
	 *           the mplCartFacade to set
	 */
	public void setMplCartFacade(final MplCartFacade mplCartFacade)
	{
		this.mplCartFacade = mplCartFacade;
	}

	/**
	 * @return the pincodeService
	 */
	public PincodeService getPincodeService()
	{
		return pincodeService;
	}

	/**
	 * @param pincodeService
	 *           the pincodeService to set
	 */
	public void setPincodeService(final PincodeService pincodeService)
	{
		this.pincodeService = pincodeService;
	}

}
