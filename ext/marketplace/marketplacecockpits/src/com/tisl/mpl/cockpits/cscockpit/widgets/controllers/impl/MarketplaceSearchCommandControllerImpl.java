package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceBasketController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceSearchCommandController;
import com.tisl.mpl.cockpits.cscockpit.widgets.helpers.MarketplaceServiceabilityCheckHelper;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeRestrictionService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.service.PinCodeDeliveryModeService;

import de.hybris.platform.catalog.impl.DefaultCatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.services.search.CsSearchCommand;
import de.hybris.platform.cscockpit.widgets.controllers.search.impl.DefaultSearchCommandController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.BasketResultWidgetRenderer;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.PriceValue;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketplaceSearchCommandControllerImpl.
 */
public class MarketplaceSearchCommandControllerImpl extends
		DefaultSearchCommandController<CsSearchCommand> implements
		MarketplaceSearchCommandController {

	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MarketplaceSearchCommandControllerImpl.class);

	
	/** The pin code. */
	private Long pinCode;

	/** The mpl pincode restriction service. */
	@Resource(name = "mplPincodeRestrictionService")
	private MplPincodeRestrictionService mplPincodeRestrictionService;

	/** The pin code delivery mode service. */
	@Resource(name = "pinCodeDeliveryModeService")
	private PinCodeDeliveryModeService pinCodeDeliveryModeService;

	@Autowired
	private MarketplaceServiceabilityCheckHelper marketplaceServiceabilityCheckHelper;
	
	@Autowired
	private SessionService sessionService;
	
	/** The catalog version service. */
	@Autowired
	private DefaultCatalogVersionService catalogVersionService;

	@Autowired
	private MarketPlaceBasketController defaultMarketplaceBasketController;
	
	@Autowired
	private MplPriceRowService mplPriceRowService;
	
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private BuyBoxService buyBoxService;
	
	@Autowired
	private CommonI18NService commonI18NService;
	
	@Autowired
	private FormatFactory formatFactory;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.cockpits.cscockpit.widgets.controllers.
	 * MarketplaceSearchCommandController#getPinCode()
	 */
	@Override
	public Long getPinCode() {
		if(null!=sessionService.getAttribute("pincode")) {
			this.pinCode = Long.valueOf(sessionService.getAttribute("pincode").toString()).longValue();
		}
		return pinCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.cockpits.cscockpit.widgets.controllers.
	 * MarketplaceSearchCommandController#setPinCode(java.lang.Long)
	 */
	@Override
	public void setPinCode(final Long pinCode) {
		sessionService.setAttribute("pincode", pinCode.toString());
		this.pinCode = pinCode;
		
	}

	/**
	 * Gets the product value.
	 *
	 * @param productModel
	 *            the product model
	 * @return the product value
	 */
	@Override
	public List<String> getSellerDetails(ProductModel productModel) {
		List<String> sellerNames = marketplaceServiceabilityCheckHelper
				.getSellerDetails(productModel);

		return sellerNames;

	}

	

	/**
	 * Gets the response for pin code.
	 *
	 * @param productCode
	 *            the product code
	 * @param pin
	 *            the pin
	 * @param requestData
	 *            the request data
	 * @return listingId
	 * @throws EtailNonBusinessExceptions
	 *             the etail non business exceptions
	 * @throws ClientEtailNonBusinessExceptions
	 *             the client etail non business exceptions
	 * @description this method checks the restriction list and calls pincode
	 *              service accordingly
	 */
	@Override
	public List<PinCodeResponseData> getResponseForPinCode(
			final ProductModel product, final String pin,
			final String isDeliveryDateRequired, final String ussid)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions {

		List<PinCodeResponseData> responseData = marketplaceServiceabilityCheckHelper
				.getResponseForPinCode(product, pin, isDeliveryDateRequired, ussid);		
		return responseData;
	}

	@Override
	public void setCurrentSite() {
		catalogVersionService.getSessionCatalogVersions();
		final CatalogVersionModel catalogVersion = catalogVersionService
				.getCatalogVersion("mplProductCatalog", "Online");
		catalogVersionService.setSessionCatalogVersions(Collections
				.singleton(catalogVersion));

	}
	
	@Override
	public Map<String,PriceRowModel> getsellerSpecificPrices(final ProductModel product) {
		
		//List<String> aticleSKUIDs = new WeakArrayList<String>();
		List<String> lst = new ArrayList<String>();
		
		for(SellerInformationModel sellerInfo : product.getSellerInformationRelator()) {

			lst.add(sellerInfo.getSellerArticleSKU());

		}

		LOG.info("getsellerSpecificPrices articleSKUIDs="+lst.toString());
		Map<String,PriceRowModel> sellerPriceMap = mplPriceRowService.getAllPriceRowDetail(lst);

		return sellerPriceMap;
		
	}
	
	
	
	/**
	 * Ge2tseller specific buy box prices.
	 *
	 * @param product the product
	 * @return the map
	 */
	@Override
	public List<BuyBoxModel> getsellerSpecificBuyBoxPrices(final ProductModel product) {
		return buyBoxService.buyboxPrice(product.getCode());
	}

	
	/**
	 * Adds the to market place cart.
	 *
	 * @param productObject the product object
	 * @param quantity the quantity
	 * @param ussId the uss id
	 * @return true, if successful
	 * @throws CommerceCartModificationException the commerce cart modification exception
	 */
	@Override
	public boolean addToMarketPlaceCart(TypedObject productObject, long quantity,
			String ussId) throws CommerceCartModificationException {

		ProductModel product =(ProductModel) productObject.getObject();
		boolean isProductAddedToCart=false;
		try {
			setCurrentSite();
			LOG.info("Bypass the pincode serviceability:"+configurationService.getConfiguration().getBoolean(MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS));
			if(configurationService.getConfiguration().getBoolean(MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS, false)) {
				defaultMarketplaceBasketController.addToMarketPlaceCart(productObject, quantity, ussId);
				isProductAddedToCart=true;
			} else{
				List<PinCodeResponseData> pincodeResponse=getResponseForPinCode(product, String.valueOf(getPinCode()), MarketplaceCockpitsConstants.NO, ussId);
				if(CollectionUtils.isNotEmpty(pincodeResponse)) {
					for(PinCodeResponseData response:pincodeResponse) {
						if(StringUtils.equals(ussId, response.getUssid()) && 
								StringUtils.equalsIgnoreCase(response.getIsServicable(),MarketplaceCockpitsConstants.YES) &&
								StringUtils.equalsIgnoreCase(response.getCod(),MarketplaceCockpitsConstants.YES)) {
							defaultMarketplaceBasketController.addToMarketPlaceCart(productObject, quantity, ussId);
							isProductAddedToCart=true;
							break;
						} else {
							LOG.info("ussid:"+response.getUssid()+";serviceable:"+response.getIsServicable()+";cod:"+response.getCod());
						}
					}
				}
			}
		}catch(CommerceCartModificationException ex) {
			LOG.error("CommerceCartModificationException in addToMarketPlaceCart",ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		return isProductAddedToCart;
	}

	
	/**
	 * Dispatch event.
	 */
	@Override
	public void dispatchEvent() {
		Map data = Collections.singletonMap("refresh", Boolean.TRUE);
		defaultMarketplaceBasketController.dispatchEvent(null, null, data);
	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceSearchCommandController#getPricingFlag()
	 */
	@Override
	public boolean getPricingFlag() {
		return configurationService.getConfiguration().getBoolean(MarketplaceCockpitsConstants.PRICING_FROM_BUY_BOX,true);
		
	}
	
	/**
	 * Format product price.
	 *
	 * @param price the price
	 * @return the string
	 */
	@Override
	public String formatProductPrice(final Double price) {
		final PriceValue priceValue=new PriceValue(MarketplaceCockpitsConstants.INR,price,  false);
	    if (priceValue != null)
	    {
	      Object result = sessionService.executeInLocalView(new SessionExecutionBody() {
	    	
	    	  @Override
	        public Object execute()
	        {
	    		  commonI18NService.setCurrentCurrency(commonI18NService.getCurrency(priceValue.getCurrencyIso()));
	         return formatFactory.createCurrencyFormat().format(priceValue.getValue());

	       }
	     });
	     if (result instanceof String)
	     {
	       return ((String)result);
	     }
	   }
	   return "";
	 }
}
