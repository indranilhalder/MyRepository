/**
 *
 */
package com.tisl.mpl.facade.checkout.impl;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class MplCustomAddressFacadeImpl extends DefaultCheckoutFacade implements MplCustomAddressFacade
{

	private CartFacade cartFacade;
	private Converter<AddressModel, AddressData> customAddressConverter;
	private Converter<CartModel, CartData> mplExtendedCartConverter;

	@Autowired
	private MplDeliveryCostService mplDeliveryCostService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private CartService cartService;

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;

	@Autowired
	private SessionService sessionService;

	private static final Logger LOG = Logger.getLogger(MplCustomAddressFacadeImpl.class);

	private static final String FINEJEWELLERY = "FineJewellery";

	/**
	 * @return the cartFacade
	 */
	@Override
	public CartFacade getCartFacade()
	{
		return cartFacade;
	}

	/**
	 * @param cartFacade
	 *           the cartFacade to set
	 */
	@Override
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	/**
	 * @return the customAddressConverter
	 */
	public Converter<AddressModel, AddressData> getCustomAddressConverter()
	{
		return customAddressConverter;
	}

	/**
	 * @param customAddressConverter
	 *           the customAddressConverter to set
	 */
	public void setCustomAddressConverter(final Converter<AddressModel, AddressData> customAddressConverter)
	{
		this.customAddressConverter = customAddressConverter;
	}

	/**
	 * @return the mplExtendedCartConverter
	 */
	public Converter<CartModel, CartData> getMplExtendedCartConverter()
	{
		return mplExtendedCartConverter;
	}

	/**
	 * @param mplExtendedCartConverter
	 *           the mplExtendedCartConverter to set
	 */
	public void setMplExtendedCartConverter(final Converter<CartModel, CartData> mplExtendedCartConverter)
	{
		this.mplExtendedCartConverter = mplExtendedCartConverter;
	}

	/**
	 * @return the mplDeliveryCostService
	 */
	public MplDeliveryCostService getMplDeliveryCostService()
	{
		return mplDeliveryCostService;
	}


	/**
	 * @param mplDeliveryCostService
	 *           the mplDeliveryCostService to set
	 */
	public void setMplDeliveryCostService(final MplDeliveryCostService mplDeliveryCostService)
	{
		this.mplDeliveryCostService = mplDeliveryCostService;
	}


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
	 * @return the cartService
	 */
	@Override
	public CartService getCartService()
	{
		return cartService;
	}


	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Override
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}





	/**
	 * @description: It is responsible for fetching Cart Data
	 * @return CartData
	 */
	@Override
	public CartData getCheckoutCart()
	{
		final CartModel cartModel = getCart();
		CartData cartData = null;
		if (null != cartModel)
		{
			cartData = getMplExtendedCartConverter().convert(cartModel);

			if (cartData != null)
			{
				cartData.setDeliveryAddress(getDeliveryAddress());
				cartData.setPaymentInfo(getPaymentDetails());

			}
			if (null != cartModel.getConvenienceCharges())
			{
				cartData.setConvenienceChargeForCOD(createPrice(cartModel, cartModel.getConvenienceCharges()));
			}
			if (null != cartModel.getTotalPriceWithConv())
			{
				cartData.setTotalPriceWithConvCharge(createPrice(cartModel, cartModel.getTotalPriceWithConv()));
			}
		}
		else
		{
			LOG.error(">>>> getCheckoutCart() CartModel is null ");
		}
		return cartData;
	}


	/**
	 * @description: It is creating price data for a price value
	 * @param source
	 * @param val
	 *
	 * @return PriceData
	 */
	protected PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}

		final CurrencyModel currency = source.getCurrency();
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}


	/**
	 * @description: It is responsible for fetching cart delivery address
	 * @return AddressData
	 */
	@Override
	public AddressData getDeliveryAddress()
	{
		final CartModel cart = getCart();
		if (cart != null)
		{
			final AddressModel deliveryAddress = cart.getDeliveryAddress();
			if (deliveryAddress != null)
			{
				// Ensure that the delivery address is in the set of supported addresses
				final AddressModel supportedAddress = getDeliveryAddressModelForCode(deliveryAddress.getPk().toString());
				if (supportedAddress != null)
				{
					return getCustomAddressConverter().convert(supportedAddress);
				}
			}
		}

		return null;
	}

	/**
	 * @description: It is responsible for fetching Delivery address
	 * @param selectedAddressData
	 * @return List<? extends AddressData>
	 */
	//	@Override                                  //CAR-194
	public List<AddressData> getDeliveryAddresses(final AddressData selectedAddressData, final CartModel cartModel) //CAR-194
	{
		List<AddressData> deliveryAddresses = null;
		if (selectedAddressData != null)
		{
			deliveryAddresses = new ArrayList<AddressData>(getSupportedDeliveryAddresses(true, cartModel));

			if (!isAddressOnList(deliveryAddresses, selectedAddressData))
			{
				deliveryAddresses.add(selectedAddressData);
			}
		}

		return deliveryAddresses == null ? Collections.<AddressData> emptyList() : deliveryAddresses;
	}

	/**
	 * @description: It is responsible for fetching Cart supported delivery address
	 * @param visibleAddressesOnly
	 * @return List<AddressData>
	 */
	//@Override                                  //CAR-194
	public List<AddressData> getSupportedDeliveryAddresses(final boolean visibleAddressesOnly, final CartModel cartModel) //CAR-194
	{
		//final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			final List<AddressModel> addresses = getDeliveryService().getSupportedDeliveryAddressesForOrder(cartModel,
					visibleAddressesOnly);
			return Converters.convertAll(addresses, getCustomAddressConverter());
		}
		return Collections.emptyList();
	}

	/**
	 * @description: It is responsible for checking is address is in list or not
	 * @param deliveryAddresses
	 * @param selectedAddressData
	 *
	 * @return boolean
	 */
	protected boolean isAddressOnList(final List<AddressData> deliveryAddresses, final AddressData selectedAddressData)
	{
		if (deliveryAddresses == null || selectedAddressData == null)
		{
			return false;
		}

		for (final AddressData address : deliveryAddresses)
		{
			if (address.getId().equals(selectedAddressData.getId()))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * @description: It is responsible for fetching Address data for a address code
	 * @param code
	 * @return AddressData
	 */
	@Override
	public AddressData getDeliveryAddressForCode(final String code)
	{
		for (final AddressData address : getSupportedDeliveryAddresses(false))
		{
			if (code.equals(address.getId()))
			{
				return address;
			}
		}
		return null;
	}

	//For Payment
	@Override
	public PriceData addConvCharge(final CartModel source, final CartData prototype)
	{
		prototype.setConvenienceChargeForCOD(createPrice(source, source.getConvenienceCharges()));
		return prototype.getConvenienceChargeForCOD();
	}

	@Override
	public PriceData setTotalWithConvCharge(final CartModel source, final CartData prototype)
	{
		prototype.setTotalPriceWithConvCharge(createPrice(source, source.getTotalPriceWithConv()));
		return prototype.getTotalPriceWithConvCharge();
	}



	/**
	 * Method adds convenience charge to orderModel TPR-629
	 *
	 */
	@Override
	public PriceData addConvCharge(final OrderModel source, final OrderData prototype)
	{
		prototype.setConvenienceChargeForCOD(createPrice(source, source.getConvenienceCharges()));
		return prototype.getConvenienceChargeForCOD();
	}


	/**
	 * Method sets total with conv charge for orderModel TPR-629
	 *
	 */
	@Override
	public PriceData setTotalWithConvCharge(final OrderModel source, final OrderData prototype)
	{
		prototype.setTotalPriceWithConvCharge(createPrice(source, source.getTotalPriceWithConv()));
		return prototype.getTotalPriceWithConvCharge();
	}




	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.checkout.MplCustomAddressFacade#populateDeliveryMethodData(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Double populateDeliveryMethodData(final String deliveryCode, final String sellerArticleSKU)
	{
		//// Returning deliveryCost. But method name is different????
		//TODO method name change
		ServicesUtil.validateParameterNotNull(deliveryCode, "deliveryCode cannot be null");
		ServicesUtil.validateParameterNotNull(sellerArticleSKU, "sellerArticleSKU cannot be null");

		final CartModel cartModel = getCart();
		Double deliveryCost = Double.valueOf(0.0);
		String fulfillmentType = "";
		String tshipThresholdValue = configurationService.getConfiguration()
				.getString(MarketplaceFacadesConstants.TSHIPTHRESHOLDVALUE);
		tshipThresholdValue = (tshipThresholdValue != null && !tshipThresholdValue.isEmpty()) ? tshipThresholdValue
				: Integer.toString(0);

		if (cartModel != null)
		{
			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				if (sellerArticleSKU.equals(entry.getSelectedUSSID()) && !entry.getGiveAway().booleanValue())
				{
					//Retrieve delivery modes and delivery charges for a USSID and saving them in cart entry.This will be taken forward to Order entry
					final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = mplDeliveryCostService
							.getDeliveryCost(deliveryCode, MarketplacecommerceservicesConstants.INR, sellerArticleSKU);

					//TISEE-289
					//final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(sellerArticleSKU);
					SellerInformationModel sellerInfoModel = null;
					if (entry.getProduct().getProductCategoryType().equalsIgnoreCase(FINEJEWELLERY))
					{
						final List<JewelleryInformationModel> jewelleryInfo = jewelleryService
								.getJewelleryInfoByUssid(sellerArticleSKU);
						if (CollectionUtils.isNotEmpty(jewelleryInfo))
						{
							sellerInfoModel = getMplSellerInformationService().getSellerDetail(jewelleryInfo.get(0).getPCMUSSID());
						}
						else
						{
							LOG.error("No entry for JewelleryInformationModel for " + sellerArticleSKU);
						}
					}
					else
					{
						sellerInfoModel = getMplSellerInformationService().getSellerDetail(sellerArticleSKU);
					}

					if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null
							&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
					{
						fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
								.getDeliveryFulfillModes().getCode();

						//						if (fulfillmentType.equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE))
						//						{
						//							mplZoneDeliveryModeValueModel.setValue(Double.valueOf(0.0));
						//						}

						// For Release 1 , TShip delivery cost will always be zero . Hence , commneting the below code which check configuration from HAC
						if (fulfillmentType.equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE)
								&& entry.getTotalPrice().doubleValue() > Double.parseDouble(tshipThresholdValue))

						{
							mplZoneDeliveryModeValueModel.setValue(Double.valueOf(0.0));
						}
					}

					entry.setMplDeliveryMode(mplZoneDeliveryModeValueModel);
					if (mplZoneDeliveryModeValueModel.getValue() != null)
					{
						if (entry.getIsBOGOapplied().booleanValue())
						{
							// PRDI-378 starts here
							/*
							 * deliveryCost = Double.valueOf(entry.getQualifyingCount().doubleValue()
							 * mplZoneDeliveryModeValueModel.getValue().doubleValue());
							 */

							deliveryCost = Double.valueOf((entry.getQuantity().doubleValue() - entry.getFreeCount().doubleValue())
									* mplZoneDeliveryModeValueModel.getValue().doubleValue());
							// PRDI-378 ends here
						}
						else
						{
							deliveryCost = Double
									.valueOf(entry.getQuantity().doubleValue() * mplZoneDeliveryModeValueModel.getValue().doubleValue());
						}

					}

					entry.setCurrDelCharge(deliveryCost);
					LOG.debug(" >>> Delivery cost for ussid  " + sellerArticleSKU + " of fulfilment type " + fulfillmentType + " is "
							+ deliveryCost);

					//if delivery mode is changed from  c-n-c to other and if it contains POS then we need to remove the POS from that entry
					if (null != entry.getMplDeliveryMode() && null != entry.getMplDeliveryMode().getDeliveryMode()
							&& !entry.getMplDeliveryMode().getDeliveryMode().getCode().equals(MarketplaceFacadesConstants.C_C)
							&& null != entry.getDeliveryPointOfService())
					{
						entry.setDeliveryPointOfService(null);
					}

					getModelService().save(entry);
					break;
				}
			}
		}
		return deliveryCost;
	}

	/*
	 * Set delivery mode using USSID
	 *
	 * @param deliveryCode
	 *
	 * @param sellerArticleSKUID
	 */
	@Override
	public void populateDeliveryMethod(final String deliveryCode, final String sellerArticleSKU) throws EtailNonBusinessExceptions
	{
		//// Returning deliveryCost. But method name is different????
		//TODO method name change
		ServicesUtil.validateParameterNotNull(deliveryCode, "deliveryCode cannot be null");
		ServicesUtil.validateParameterNotNull(sellerArticleSKU, "sellerArticleSKU cannot be null");
		try
		{
			final CartModel cartModel = getCart();

			String tshipThresholdValue = configurationService.getConfiguration()
					.getString(MarketplaceFacadesConstants.TSHIPTHRESHOLDVALUE);
			tshipThresholdValue = (tshipThresholdValue != null && !tshipThresholdValue.isEmpty()) ? tshipThresholdValue
					: Integer.toString(0);

			if (cartModel != null)
			{
				for (final AbstractOrderEntryModel entry : cartModel.getEntries())
				{
					if (sellerArticleSKU.equals(entry.getSelectedUSSID()))
					{
						//Retrieve delivery modes and delivery charges for a USSID and saving them in cart entry.This will be taken forward to Order entry
						final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = mplDeliveryCostService
								.getDeliveryCost(deliveryCode, MarketplacecommerceservicesConstants.INR, sellerArticleSKU);

						if (mplZoneDeliveryModeValueModel.getDeliveryFulfillModes() != null
								&& mplZoneDeliveryModeValueModel.getDeliveryFulfillModes().getCode() != null
								&& mplZoneDeliveryModeValueModel.getDeliveryFulfillModes().getCode()
										.equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE)
								&& cartModel.getSubtotal().intValue() > Integer.parseInt(tshipThresholdValue))
						{
							mplZoneDeliveryModeValueModel.setValue(Double.valueOf(0.0));
						}
						entry.setMplDeliveryMode(mplZoneDeliveryModeValueModel);
						getModelService().save(entry);
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}

	}

	@Override
	public boolean hasValidCart(final CartData cart)
	{
		//final boolean validCart = false;
		//final CartData cartData = getCheckoutCart();
		/*
		 * if (null != cartData) { validCart = cartData.getEntries() != null && !cartData.getEntries().isEmpty(); } return
		 * validCart;
		 */
		return (cart != null && CollectionUtils.isNotEmpty(cart.getEntries())) ? true : false;
	}

	@Override
	public boolean hasNoDeliveryAddress(final CartData cartData)
	{
		//final CartModel cartModel = cartService.getSessionCart();

		boolean flag = false;
		if (null != cartData && cartData.getDeliveryAddress() == null)
		{
			final List<OrderEntryData> entryList = cartData.getEntries();
			if (CollectionUtils.isNotEmpty(entryList))
			{
				for (final OrderEntryData entry : entryList)
				{
					if (entry.getDeliveryPointOfService() == null) // Entry having CNC Delivery Mode will always have POS
					{
						flag = true;
						break;
					}
				}
			}
		}

		return flag;

		//Blocked for
		//return hasShippingItems() && (cartData == null || cartData.getDeliveryAddress() == null);
	}

	/**
	 * @description: It is responsible to check if cart has payment information
	 * @return boolean
	 */
	@Override
	public boolean hasNoPaymentInfo()
	{
		final CartData cartData = getCheckoutCart();
		return (cartData == null || cartData.getPaymentInfo() == null);
	}

	/**
	 * @description: It is responsible to check if cart contains delivery mode or not
	 * @return boolean
	 */
	@Override
	public boolean hasNoDeliveryMode(final CartData cartData)
	{
		//final CartModel cartModel = cartService.getSessionCart();
		//boolean deliveryModeNotSelected = false;
		if (cartData != null)
		{
			final List<OrderEntryData> entryList = cartData.getEntries();
			if (CollectionUtils.isNotEmpty(entryList))
			{
				for (final OrderEntryData cartEntryModel : entryList)
				{
					if (cartEntryModel != null && cartEntryModel.getMplDeliveryMode() == null)
					{
						return true;
					}
				}
			}
		}
		return false;
	}


	@Override
	public boolean setDeliveryAddress(final AddressData addressData)
	{
		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			AddressModel addressModel = null;
			if (addressData != null)
			{
				if (addressData.getId() != null)
				{
					addressModel = getDeliveryAddressModelForCode(addressData.getId());
				}
				else
				{
					addressModel = createDeliveryAddressModel(addressData, cartModel);
				}
			}

			getModelService().refresh(cartModel);
			final UserModel user = cartModel.getUser();
			getModelService().refresh(user);
			cartModel.setDeliveryAddress(addressModel);
			if (isValidDeliveryAddress(cartModel, addressModel))
			{
				getModelService().save(cartModel);
				if (null != addressModel)
				{
					getModelService().save(addressModel);
				}
				getModelService().refresh(cartModel);
				return true;
			}
		}
		return false;
	}



	private boolean isValidDeliveryAddress(final CartModel cartModel, final AddressModel addressModel)
	{
		if (addressModel != null)
		{
			final List supportedAddresses = getDeliveryService().getSupportedDeliveryAddressesForOrder(cartModel, false);
			return ((supportedAddresses != null) && (supportedAddresses.contains(addressModel)));
		}

		return true;
	}


	/**
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}


	/**
	 * @param mplSellerInformationService
	 *           the mplSellerInformationService to set
	 */
	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}




	/**
	 * This method populates delivery method for web cart TISPT-400
	 *
	 * @param deliveryCode
	 * @param sellerArticleSKU
	 * @param cartModel
	 * @return Double
	 *
	 */
	@Override
	public Double populateDeliveryMethodData(final String deliveryCode, final String sellerArticleSKU, final CartModel cartModel)
	{
		ServicesUtil.validateParameterNotNull(deliveryCode, "deliveryCode cannot be null");
		ServicesUtil.validateParameterNotNull(sellerArticleSKU, "sellerArticleSKU cannot be null");

		Double deliveryCost = Double.valueOf(0.0);
		String fulfillmentType = "";
		String tshipThresholdValue = configurationService.getConfiguration()
				.getString(MarketplaceFacadesConstants.TSHIPTHRESHOLDVALUE);
		tshipThresholdValue = (tshipThresholdValue != null && !tshipThresholdValue.isEmpty()) ? tshipThresholdValue
				: Integer.toString(0);

		List<PinCodeResponseData> pincoderesponseDataList = null;
		pincoderesponseDataList = sessionService
				.getAttribute(MarketplacecommerceservicesConstants.PINCODE_RESPONSE_DATA_TO_SESSION);

		LOG.debug("******responceData******** " + pincoderesponseDataList);

		if (cartModel != null)
		{
			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				if (sellerArticleSKU.equals(entry.getSelectedUSSID()) && !entry.getGiveAway().booleanValue())
				{
					//Retrieve delivery modes and delivery charges for a USSID and saving them in cart entry.This will be taken forward to Order entry
					MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = null;

					//TISEE-289
					//for fine jewellery
					SellerInformationModel sellerInfoModel = null;
					if (entry.getProduct().getProductCategoryType().equalsIgnoreCase(FINEJEWELLERY))
					{
						final List<JewelleryInformationModel> jewelleryInfo = jewelleryService
								.getJewelleryInfoByUssid(sellerArticleSKU);
						if (CollectionUtils.isNotEmpty(jewelleryInfo))
						{
							sellerInfoModel = getMplSellerInformationService().getSellerDetail(jewelleryInfo.get(0).getPCMUSSID());
							mplZoneDeliveryModeValueModel = mplDeliveryCostService.getDeliveryCost(deliveryCode,
									MarketplacecommerceservicesConstants.INR, jewelleryInfo.get(0).getPCMUSSID());
						}
						else
						{
							LOG.error("No entry for JewelleryInformationModel for " + sellerArticleSKU);
						}

					}
					else
					{
						sellerInfoModel = getMplSellerInformationService().getSellerDetail(sellerArticleSKU);
						mplZoneDeliveryModeValueModel = mplDeliveryCostService.getDeliveryCost(deliveryCode,
								MarketplacecommerceservicesConstants.INR, sellerArticleSKU);
					}

					if (null != mplZoneDeliveryModeValueModel && sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null
							&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
					{
						fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
								.getDeliveryFulfillModes().getCode();


						//Blocked for TPR-579

						// For Release 1 , TShip delivery cost will always be zero . Hence , commenting the below code which check configuration from HAC
						if (null != pincoderesponseDataList && pincoderesponseDataList.size() > 0)
						{
							for (final PinCodeResponseData responseData : pincoderesponseDataList)
							{
								if (entry.getSelectedUSSID().equals(responseData.getUssid()))
								{
									for (final DeliveryDetailsData detailsData : responseData.getValidDeliveryModes())
									{
										if (null != detailsData.getFulfilmentType()
												&& detailsData.getFulfilmentType().equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE)
												&& entry.getTotalPrice().doubleValue() > Double.parseDouble(tshipThresholdValue))

										{
											mplZoneDeliveryModeValueModel.setValue(Double.valueOf(0.0)); // If entry price greater than threshold no charge must be taken
										}
									}
								}
							}
						}
						/*
						 * if (fulfillmentType.equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE) &&
						 * entry.getTotalPrice().doubleValue() > Double.parseDouble(tshipThresholdValue))
						 *
						 *
						 *
						 * // For Release 1 , TShip delivery cost will always be zero . Hence , commenting the below code
						 * which check configuration from HAC // if
						 * (fulfillmentType.equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE) // &&
						 * entry.getTotalPrice().doubleValue() > Double.parseDouble(tshipThresholdValue)) // // { //
						 * mplZoneDeliveryModeValueModel.setValue(Double.valueOf(0.0)); // }
						 *
						 * { mplZoneDeliveryModeValueModel.setValue(Double.valueOf(0.0)); }
						 */
					}

					entry.setMplDeliveryMode(mplZoneDeliveryModeValueModel);
					if (null != mplZoneDeliveryModeValueModel && mplZoneDeliveryModeValueModel.getValue() != null
							&& null != mplZoneDeliveryModeValueModel.getDeliveryFulfillModes()
							&& fulfillmentType.equalsIgnoreCase(mplZoneDeliveryModeValueModel.getDeliveryFulfillModes().getCode()))
					{
						if (entry.getIsBOGOapplied().booleanValue())
						{
							// PRDI-378 starts here
							/*
							 * deliveryCost = Double.valueOf(entry.getQualifyingCount().doubleValue()
							 * mplZoneDeliveryModeValueModel.getValue().doubleValue());
							 */

							deliveryCost = Double.valueOf((entry.getQuantity().doubleValue() - entry.getFreeCount().doubleValue())
									* mplZoneDeliveryModeValueModel.getValue().doubleValue());
							// PRDI-378 ends here
						}
						else
						{
							deliveryCost = Double
									.valueOf(entry.getQuantity().doubleValue() * mplZoneDeliveryModeValueModel.getValue().doubleValue());
						}

					}

					entry.setCurrDelCharge(deliveryCost);
					LOG.debug(" >>> Delivery cost for ussid  " + sellerArticleSKU + " of fulfilment type " + fulfillmentType + " is "
							+ deliveryCost);

					//if delivery mode is changed from  c-n-c to other and if it contains POS then we need to remove the POS from that entry
					if (null != entry.getMplDeliveryMode() && null != entry.getMplDeliveryMode().getDeliveryMode()
							&& !entry.getMplDeliveryMode().getDeliveryMode().getCode().equals(MarketplaceFacadesConstants.C_C)
							&& null != entry.getDeliveryPointOfService())
					{
						entry.setDeliveryPointOfService(null);
					}

					getModelService().save(entry);
					break;
				}
			}
		}
		return deliveryCost;
	}

	/**
	 * It is responsible for fetching Cart Data
	 *
	 * @param cartModel
	 * @return
	 */
	//TISPRD-9350--  New Method created for Web service to pass the cartModel in place of fetching data from session
	@Override
	public CartData getCheckoutCartWS(final CartModel cart)
	{
		final CartModel cartModel = cart;
		CartData cartData = null;
		if (null != cartModel)
		{
			cartData = getMplExtendedCartConverter().convert(cartModel);

			if (cartData != null)
			{
				cartData.setDeliveryAddress(getDeliveryAddressWS(cartModel));
				cartData.setPaymentInfo(getPaymentDetailsWS(cartModel));
				cartData.setDeliveryMode(getDeliveryModeWS(cartModel));
			}
			if (null != cartModel.getConvenienceCharges())
			{
				cartData.setConvenienceChargeForCOD(createPrice(cartModel, cartModel.getConvenienceCharges()));
			}
			if (null != cartModel.getTotalPriceWithConv())
			{
				cartData.setTotalPriceWithConvCharge(createPrice(cartModel, cartModel.getTotalPriceWithConv()));
			}
		}
		else
		{
			LOG.error(">>>> getCheckoutCart() CartModel is null ");
		}
		return cartData;
	}

	//TISPRD-9350--  New Method created for Web service to pass the cartModel in place of fetching data from session and calculate the delivery address
	/**
	 *
	 * @param cartModel
	 * @return
	 */
	protected AddressData getDeliveryAddressWS(final CartModel cartModel)
	{
		final CartModel cart = cartModel;
		if (cart != null)
		{
			final AddressModel deliveryAddress = cart.getDeliveryAddress();
			if (deliveryAddress != null)
			{
				// Ensure that the delivery address is in the set of supported addresses
				final AddressModel supportedAddress = getDeliveryAddressModelForCodeWS(deliveryAddress.getPk().toString(), cartModel);
				if (supportedAddress != null)
				{
					return getCustomAddressConverter().convert(supportedAddress);
				}
			}
		}

		return null;
	}

	//TISPRD-9350--  New Method created for Web service to pass the cartModel in place of fetching data from session and get the delivery address Model
	/**
	 *
	 * @param code
	 * @param cart
	 * @return
	 */
	protected AddressModel getDeliveryAddressModelForCodeWS(final String code, final CartModel cart)
	{
		Assert.notNull(code, "Parameter code cannot be null.");

		final CartModel cartModel = cart;
		if (cartModel != null)
		{
			final List<AddressModel> addresses = getDeliveryService().getSupportedDeliveryAddressesForOrder(cartModel, false);
			if (addresses != null)
			{
				for (final AddressModel address : addresses)
				{
					if (code.equals(address.getPk().toString()))
					{
						return address;
					}
				}
			}
		}
		return null;
	}

	//TISPRD-9350--  New Method created for Web service to pass the cartModel in place of fetching data from session and get the payment details
	/**
	 *
	 * @param cartModel
	 * @return
	 */
	protected CCPaymentInfoData getPaymentDetailsWS(final CartModel cartModel)
	{
		final CartModel cart = cartModel;
		if (cart != null)
		{
			final PaymentInfoModel paymentInfo = cart.getPaymentInfo();
			if (paymentInfo instanceof CreditCardPaymentInfoModel)
			{
				return getCreditCardPaymentInfoConverter().convert((CreditCardPaymentInfoModel) paymentInfo);
			}
		}

		return null;
	}


	//TISPRD-9350--  New Method created for Web service to pass the cartModel in place of fetching data from session and get the delivery mode
	/**
	 *
	 * @param cartModel
	 * @return
	 */
	protected DeliveryModeData getDeliveryModeWS(final CartModel cartModel)
	{
		final CartModel cart = cartModel;
		if (cart != null)
		{
			final DeliveryModeModel deliveryModeModel = cart.getDeliveryMode();
			if (deliveryModeModel != null)
			{
				return convert(deliveryModeModel);
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.facade.checkout.MplCustomAddressFacade#getDeliveryAddresses(de.hybris.platform.commercefacades.user
	 * .data.AddressData)
	 */
	@Override
	public List<? extends AddressData> getDeliveryAddresses(final AddressData selectedAddressData)
	{
		// YTODO Auto-generated method stub
		return null;
	}
}
