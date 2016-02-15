package com.tisl.mpl.marketplacecommerceservices.service;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCommerceCartDao;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCommerceCartServiceImpl;
import com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData;
import com.tisl.mpl.strategy.service.impl.MplDefaultCommerceAddToCartStrategyImpl;
import com.tisl.mpl.wsdto.DeliveryModeResOMSWsDto;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeResponse;
import com.tisl.mpl.wsdto.ReservationItemWsDTO;
import com.tisl.mpl.wsdto.ReservationListWsDTO;




/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MplCommerceCartServiceImplTest
{
	@Mock
	private MplCommerceCartServiceImpl mplCommerceCartServiceImpl;
	private MplDefaultCommerceAddToCartStrategyImpl mplDefaultCommerceAddToCartStrategyImpl;

	/*
	 * @Mock private final Map<String, List<String>> deliveryModeDataMapMock = new HashMap<String, List<String>>();
	 */


	@Mock
	private final MplCommerceCartDao mplCommerceCartDao = Mockito.mock(MplCommerceCartDao.class);
	@Autowired
	private UserService userService;

	@Autowired
	private MplCustomerProfileService mplCustomerProfileService;
	@Autowired
	private ModelService modelService;
	@Mock
	private KeyGenerator keyGenerator;
	@Autowired
	private MplDeliveryCostService mplDeliveryCostService;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.mplCommerceCartServiceImpl = Mockito.mock(MplCommerceCartServiceImpl.class);
		mplDefaultCommerceAddToCartStrategyImpl = Mockito.mock(MplDefaultCommerceAddToCartStrategyImpl.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.mplCommerceCartServiceImpl.setModelService(modelService);
	}

	/**
	 * JUnit Testing for mplCommerceCartServiceImplTest
	 *
	 * @throws CommerceCartModificationException
	 */

	@Test
	public void testAddToCartWithUSSID() throws CommerceCartModificationException
	{
		CommerceCartParameter parameterMock = new CommerceCartParameter();
		parameterMock = new CommerceCartParameter();
		final CommerceCartModification parameter = new CommerceCartModification();

		given(mplDefaultCommerceAddToCartStrategyImpl.addToCart(parameterMock)).willReturn(parameter);
	}

	@Test
	public void testGetSellerInfo() throws CMSItemNotFoundException
	{
		final SellerInformationData sellerInformationData = Mockito.mock(SellerInformationData.class);
		final CartData cartDataMock = Mockito.mock(CartData.class);
		cartDataMock.setDescription("DESC");
		final String ussid = "1234";
		final List<SellerInformationData> sellerInfoDataMock = new ArrayList<SellerInformationData>();
		sellerInformationData.setSellerID(ussid);
		sellerInfoDataMock.add(sellerInformationData);
		final Map<String, String> returnMap = new HashMap<String, String>();
		given(mplCommerceCartServiceImpl.getSellerInfo(cartDataMock, ussid)).willReturn(returnMap);
	}

	@Test
	public void testGetAddress() throws CMSItemNotFoundException
	{
		final AddressData addressDataMock = Mockito.mock(AddressData.class);
		final List<AddressData> addressDatasMock = new ArrayList<AddressData>();
		addressDataMock.setFirstName("abc");
		addressDataMock.setLastName("mehra");
		addressDataMock.setLine1("newtown");
		addressDataMock.setLine2("sarachi");
		addressDataMock.setPostalCode("700156");
		addressDataMock.setTown("newtown");
		addressDataMock.setCompanyName("tcs");
		addressDataMock.setEmail("abc@gmail.com");
		addressDataMock.setId("user123");
		addressDatasMock.add(0, addressDataMock);
		final Map<String, String> expressCheckoutAddressMap = new HashMap<String, String>();
		given(mplCommerceCartServiceImpl.getAddress(addressDatasMock)).willReturn(expressCheckoutAddressMap);
	}

	@Test
	public void testGetFullfillmentMode() throws CMSItemNotFoundException
	{
		final CartData cartDataMock = Mockito.mock(CartData.class);
		final Map<String, String> returnMap = new HashMap<String, String>();
		given(mplCommerceCartServiceImpl.getFullfillmentMode(cartDataMock)).willReturn(returnMap);
	}

	@Test
	public void testGetDeliveryMode() throws CMSItemNotFoundException
	{
		final OrderEntryData orderEntryData = Mockito.mock(OrderEntryData.class);
		final PriceData priceData = Mockito.mock(PriceData.class);
		final MarketplaceDeliveryModeData deliveryModeData = Mockito.mock(MarketplaceDeliveryModeData.class);
		final MplZoneDeliveryModeValueModel deliveryModel = Mockito.mock(MplZoneDeliveryModeValueModel.class);
		final DeliveryDetailsData deliveryDetailsData = Mockito.mock(DeliveryDetailsData.class);
		final SellerInformationData sellerInformationData = Mockito.mock(SellerInformationData.class);
		final PinCodeResponseData pinCodeResponseData = Mockito.mock(PinCodeResponseData.class);
		final CartData cartDataMock = Mockito.mock(CartData.class);

		final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		final List<PinCodeResponseData> omsDeliveryResponse = new ArrayList<PinCodeResponseData>();

		final String pinCode = "123456";
		final String fulfilmentType = "TSHIP";
		final String ussid = "123654098765485130011713";
		final Integer stockCount = Integer.valueOf(67);
		pinCodeResponseData.setStockCount(stockCount);
		pinCodeResponseData.setFulfilmentType(fulfilmentType);
		pinCodeResponseData.setPinCode(pinCode);
		pinCodeResponseData.setUssid(ussid);
		pinCodeResponseData.setIsServicable("Y");

		final List<DeliveryDetailsData> deliveryData = new ArrayList<DeliveryDetailsData>();
		final String inventory = "35";
		final Boolean isCOD = Boolean.TRUE;
		deliveryDetailsData.setInventory(inventory);
		deliveryDetailsData.setIsCOD(isCOD);
		deliveryData.add(deliveryDetailsData);
		pinCodeResponseData.setValidDeliveryModes(deliveryData);

		omsDeliveryResponse.add(pinCodeResponseData);

		final List<OrderEntryData> orderentry = new ArrayList<OrderEntryData>();
		final Integer entryNumber = Integer.valueOf(123);
		final String orderLineId = "254645";
		final Long quantity = Long.valueOf(34);
		orderEntryData.setEntryNumber(entryNumber);
		orderEntryData.setOrderLineId(orderLineId);
		orderEntryData.setQuantity(quantity);
		orderentry.add(orderEntryData);

		given(cartDataMock.getEntries()).willReturn(orderentry);
		final List<PinCodeResponseData> pincodeRes = new ArrayList<PinCodeResponseData>();
		pincodeRes.add(pinCodeResponseData);
		sellerInformationData.setUssid("123654098765485130011713");
		deliveryDetailsData.setType("HD");
		final String deliveryMode = "home-delivery";
		given(mplDeliveryCostService.getDeliveryCost(deliveryMode, "INR", ussid)).willReturn(deliveryModel);
		final Double value = Double.valueOf(12);
		final BigDecimal cost = BigDecimal.valueOf(34);
		priceData.setValue(cost);
		deliveryModel.setValue(value);
		deliveryModeData.setCode("HD");
		deliveryModeData.setName("home-delivery");
		deliveryModeData.setSellerArticleSKU(ussid);
		deliveryModeData.setDeliveryCost(priceData);

		final List<MarketplaceDeliveryModeData> deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
		deliveryModeDataList.add(deliveryModeData);
		deliveryModeDataMap.put("entryNumber", deliveryModeDataList);
		given(mplCommerceCartServiceImpl.getDeliveryMode(cartDataMock, omsDeliveryResponse)).willReturn(deliveryModeDataMap);
	}


	@Test
	public void testGetCartDetails()
	{
		final UserModel userModelMock = Mockito.mock(UserModel.class);
		final String ussid = "1234";
		final Collection<CartModel> cartModelMock = null;
		given(userService.getUserForUID(ussid)).willReturn(userModelMock);
		given(userModelMock.getCarts()).willReturn(cartModelMock);
	}


	@Test
	public void testGetDefaultPinCode() throws CMSItemNotFoundException
	{
		final AddressData addressDataMock = Mockito.mock(AddressData.class);
		final String defaultPinCodeId = "700160";
		final String postalCodeMock = "700156";
		addressDataMock.setPostalCode(postalCodeMock);
		given(mplCommerceCartServiceImpl.getDefaultPinCode(addressDataMock, defaultPinCodeId)).willReturn(postalCodeMock);
	}

	/*
	 * @Test public void getGiftYourselfDetails() throws CMSItemNotFoundException { final int minGiftQuantity = 2; final
	 * String pincode = "700156"; final List<Wishlist2Model> allWishlists = new ArrayList<Wishlist2Model>(); final
	 * List<Wishlist2EntryModel> finalgiftList = new ArrayList<Wishlist2EntryModel>();
	 * given(mplCommerceCartServiceImpl.getGiftYourselfDetails(minGiftQuantity, allWishlists,
	 * pincode)).willReturn(finalgiftList); }
	 */

	@Test
	public void createCart() throws CommerceCartModificationException, Exception
	{
		final MplCustomerProfileData mplCustDataMock = Mockito.mock(MplCustomerProfileData.class);
		final CartModel cartModelMock = Mockito.mock(CartModel.class);
		CartData cartDataMock = Mockito.mock(CartData.class);
		final String emailId = "abc@tcs.com";
		final String baseSiteId = "base123";
		final String uid = "uid34";
		final Collection<CartModel> cartModelList = new ArrayList<CartModel>();
		final Double value = Double.valueOf(67);
		final Double deliveryCost = Double.valueOf(150);
		cartModelMock.setCode("465");
		cartModelMock.setConvenienceCharges(value);
		cartModelMock.setDeliveryCost(deliveryCost);
		cartModelList.add(cartModelMock);
		mplCustDataMock.setDisplayUid(emailId);
		Mockito.when(mplCustomerProfileService.getCustomerProfileDetail(emailId)).thenReturn(mplCustDataMock);
		Mockito.when(mplCommerceCartServiceImpl.getCartDetails(uid)).thenReturn(cartModelList);
		final String cartModelTypeCode = "Cart";
		cartDataMock = modelService.create(cartModelTypeCode);
		cartDataMock.setCode(String.valueOf(keyGenerator.generate()));
		modelService.save(cartDataMock);
		given(mplCommerceCartServiceImpl.createCart(emailId, baseSiteId)).willReturn(cartModelMock);
	}

	@Test
	public void addItemToCart() throws CommerceCartModificationException, Exception/* , InvalidCartException */
	{
		final CartModel cartModelMock = Mockito.mock(CartModel.class);
		final CartData cartDataMock = Mockito.mock(CartData.class);
		final Boolean success = Boolean.TRUE;
		final String cartId = "c123";
		final String productCode = "987654321";
		final long quantity = 2;
		final String ussid = "1234";
		cartDataMock.setName("user");
		cartDataMock.setSite("site");
		Mockito.when(mplCommerceCartDao.getCart(cartId)).thenReturn(cartModelMock);
		Mockito.when(Boolean.valueOf(mplCommerceCartServiceImpl.addItemToCart(cartId, productCode, quantity, ussid)))
				.thenReturn((success));
	}

	@Test
	public void getSellerUssid()
	{
		final AbstractOrderEntryModel abstractOrderEntryModel = Mockito.mock(AbstractOrderEntryModel.class);
		final CartModel cartModelMock = Mockito.mock(CartModel.class);
		final String ussid = "123";
		cartModelMock.setName("abc");
		cartModelMock.setGuid("gu123");
		final List<AbstractOrderEntryModel> cartEntryModels = Arrays.asList(abstractOrderEntryModel);
		Mockito.when(abstractOrderEntryModel.getSelectedUSSID()).thenReturn("123654098765485130011713");
		cartEntryModels.add(abstractOrderEntryModel);
		Mockito.when(cartModelMock.getEntries()).thenReturn(cartEntryModels);
		Mockito.when(abstractOrderEntryModel.getSelectedUSSID()).thenReturn(ussid);
		given(mplCommerceCartServiceImpl.getSellerUssid(cartModelMock)).willReturn(ussid);
	}

	@Test
	public void getOMSPincodeResponseData()
	{
		final OrderEntryData orderEntryData = Mockito.mock(OrderEntryData.class);
		final PincodeServiceData pincodeServiceData = Mockito.mock(PincodeServiceData.class);
		final CartData cartDataMock = Mockito.mock(CartData.class);
		final String pincode = "500030";
		cartDataMock.setName("user");
		cartDataMock.setSite("site");
		final List<OrderEntryData> entryData = new ArrayList<OrderEntryData>();
		final List<PinCodeResponseData> pinCodeResponseData = new ArrayList<PinCodeResponseData>();
		final List<PincodeServiceData> reqData = new ArrayList<PincodeServiceData>();
		final String transactionId = "1234567";
		final Integer entryNumber = Integer.valueOf(23);
		final String orderLineId = "57978";
		final Long quantity = Long.valueOf(4);
		orderEntryData.setEntryNumber(entryNumber);
		orderEntryData.setOrderLineId(pincode);
		orderEntryData.setQuantity(quantity);
		orderEntryData.setTransactionId(transactionId);
		orderEntryData.setTransactionId(orderLineId);
		entryData.add(orderEntryData);
		cartDataMock.setEntries(entryData);

		final Double price = Double.valueOf(1000);
		pincodeServiceData.setProductCode("987654321");
		pincodeServiceData.setFullFillmentType("TSHIP");
		pincodeServiceData.setTransportMode("Air");
		pincodeServiceData.setSellerId("123654");
		pincodeServiceData.setUssid("123654098765485130011713");
		pincodeServiceData.setIsCOD("Y");
		pincodeServiceData.setPrice(price);
		pincodeServiceData.setIsDeliveryDateRequired("Y");

		reqData.add(pincodeServiceData);

		given(mplCommerceCartServiceImpl.getServiceablePinCodeCart(pincode, reqData)).willReturn(pinCodeResponseData);
		given(mplCommerceCartServiceImpl.getOMSPincodeResponseData(pincode, cartDataMock)).willReturn(pinCodeResponseData);
	}

	@Test
	public void getServiceablePinCodeCart()
	{
		final PincodeServiceData pincodeServiceData = Mockito.mock(PincodeServiceData.class);
		final String pincode = "700156";
		final List<PincodeServiceData> requestData = new ArrayList<PincodeServiceData>();
		final String fullFillmentType = "TSHIP";
		final String productCode = "987654321";
		final String sellerId = "123654";
		final String ussid = "123654098765485130011713";
		final String isCOD = "Y";
		final String transportMode = "homedelivery";
		final Double price = Double.valueOf(900);
		pincodeServiceData.setFullFillmentType(fullFillmentType);
		pincodeServiceData.setProductCode(productCode);
		pincodeServiceData.setSellerId(sellerId);
		pincodeServiceData.setUssid(ussid);
		pincodeServiceData.setIsCOD(isCOD);
		pincodeServiceData.setTransportMode(transportMode);
		pincodeServiceData.setPrice(price);
		requestData.add(pincodeServiceData);
		final List<PinCodeResponseData> response = new ArrayList<PinCodeResponseData>();
		given(mplCommerceCartServiceImpl.getServiceablePinCodeCart(pincode, requestData)).willReturn(response);
	}


	@Test
	public void getAllResponsesForPinCode() throws Exception
	{
		final DeliveryModeResOMSWsDto deliveryMode = Mockito.mock(DeliveryModeResOMSWsDto.class);
		final PinCodeDeliveryModeResponse pincodeResponse = Mockito.mock(PinCodeDeliveryModeResponse.class);
		final DeliveryDetailsData deliveryDetailsData = Mockito.mock(DeliveryDetailsData.class);
		final PinCodeResponseData pinCodeResponseData = Mockito.mock(PinCodeResponseData.class);
		final String pin = "700156";
		final List<PincodeServiceData> reqData = new ArrayList<PincodeServiceData>();
		final List<PinCodeResponseData> responseList = new ArrayList<PinCodeResponseData>();
		boolean servicable = false;
		final List<PinCodeDeliveryModeResponse> deliveryModeResponse = new ArrayList<PinCodeDeliveryModeResponse>();

		final String fullFillmentType = "TSHIP";
		final String ussid = "123654098765485130011713";
		pincodeResponse.setFulfilmentType(fullFillmentType);
		pincodeResponse.setUSSID(ussid);
		deliveryModeResponse.add(pincodeResponse);

		final List<Integer> stockCount = new ArrayList<Integer>();
		final List<DeliveryDetailsData> deliveryDataList = new ArrayList<DeliveryDetailsData>();
		final List<DeliveryModeResOMSWsDto> deliveryModeMock = new ArrayList<DeliveryModeResOMSWsDto>();

		final String inventory = "46";
		final String isPincodeServiceable = "Y";
		final String isCOD = "Y";
		deliveryMode.setInventory(inventory);
		deliveryMode.setIsCOD(isCOD);
		deliveryMode.setIsPincodeServiceable(isPincodeServiceable);
		deliveryModeMock.add(deliveryMode);

		pincodeResponse.setDeliveryMode(deliveryModeMock);

		servicable = true;
		pinCodeResponseData.setIsPrepaidEligible("Y");
		pinCodeResponseData.setIsCODLimitFailed("N");
		pinCodeResponseData.setCod("Y");

		stockCount.add(Integer.valueOf(200));
		deliveryDetailsData.setType("Home-Delivery");
		deliveryDetailsData.setInventory("200");
		deliveryDetailsData.setIsCOD(Boolean.TRUE);

		deliveryDataList.add(deliveryDetailsData);

		pinCodeResponseData.setValidDeliveryModes(deliveryDataList);
		pinCodeResponseData.setStockCount(Integer.valueOf(200));
		pinCodeResponseData.setIsServicable("Y");
		pinCodeResponseData.setUssid("123654098765485130011713");
		if (!servicable)
		{
			pinCodeResponseData.setIsServicable("N");
			pinCodeResponseData.setUssid(ussid);
		}
		responseList.add(pinCodeResponseData);
		given(mplCommerceCartServiceImpl.getAllResponsesForPinCode(pin, reqData)).willReturn(responseList);
	}

	@Test
	public void getReservation()
	{
		final ReservationItemWsDTO itemWsDto = Mockito.mock(ReservationItemWsDTO.class);
		final CartSoftReservationData cartSoftReservationDataMock = Mockito.mock(CartSoftReservationData.class);
		final CartData cartDataMock = Mockito.mock(CartData.class);
		final String cartId = "750159";
		final String pincode = "700156";
		final ReservationListWsDTO wsDto = Mockito.mock(ReservationListWsDTO.class);
		final List<ReservationItemWsDTO> reservationData = new ArrayList<ReservationItemWsDTO>();
		final String reservationStatus = "SUCCESS";
		final String USSID = "123654098765485130011713";
		final String availableQuantity = "112";
		itemWsDto.setAvailableQuantity(availableQuantity);
		itemWsDto.setUSSID(USSID);
		itemWsDto.setReservationStatus(reservationStatus);
		reservationData.add(itemWsDto);

		final List<CartSoftReservationData> cartdatalistMock = new ArrayList<CartSoftReservationData>();

		final String fulfillmentType = "SSHIP";
		final String deliveryMode = "Home-delivery";
		final Integer quantity = Integer.valueOf(10);
		final String isAFreebie = "Y";
		final String type = "payment";

		cartSoftReservationDataMock.setFulfillmentType(fulfillmentType);
		cartSoftReservationDataMock.setDeliveryMode(deliveryMode);
		cartSoftReservationDataMock.setQuantity(quantity);
		cartSoftReservationDataMock.setIsAFreebie(isAFreebie);
		cartSoftReservationDataMock.setUSSID(USSID);

		cartdatalistMock.add(cartSoftReservationDataMock);

		given(mplCommerceCartServiceImpl.populateDataForSoftReservation(cartDataMock)).willReturn(cartdatalistMock);
		given(mplCommerceCartServiceImpl.getReservation(cartId, cartDataMock, pincode, type)).willReturn(wsDto);
	}

	@Test
	public void populateDataForSoftReservation()
	{
		final OrderEntryData orderEntryData = Mockito.mock(OrderEntryData.class);
		final PriceData priceData = Mockito.mock(PriceData.class);
		final MarketplaceDeliveryModeData deliveryModeData = Mockito.mock(MarketplaceDeliveryModeData.class);
		final CartSoftReservationData cartSoftReservationDataMock = Mockito.mock(CartSoftReservationData.class);
		final CartData cartDataMock = Mockito.mock(CartData.class);
		final List<CartSoftReservationData> cartSoftReservationDataList = new ArrayList<CartSoftReservationData>();
		priceData.setValue(BigDecimal.valueOf(100));
		cartDataMock.setCode("2345");
		cartDataMock.setDescription("desc");
		final List<OrderEntryData> entry = new ArrayList<OrderEntryData>();

		final String transactionId = "";
		final String orderLineId = "";
		final Long quantity = Long.valueOf(45);
		orderEntryData.setOrderLineId(orderLineId);
		orderEntryData.setQuantity(quantity);
		orderEntryData.setTransactionId(transactionId);
		String deliveryModeGlobalCode = null;
		deliveryModeData.setCode("home - delivery");
		entry.add(orderEntryData);
		Mockito.when(cartDataMock.getEntries()).thenReturn(entry);
		final String sellerArticleSKU = "5567678";
		deliveryModeData.setSellerArticleSKU(sellerArticleSKU);
		cartSoftReservationDataMock.setUSSID(sellerArticleSKU);
		cartSoftReservationDataMock.setQuantity(Integer.valueOf(45));
		deliveryModeGlobalCode = "HD";
		cartSoftReservationDataMock.setDeliveryMode(deliveryModeGlobalCode);
		cartSoftReservationDataList.add(cartSoftReservationDataMock);
		given(mplCommerceCartServiceImpl.populateDataForSoftReservation(cartDataMock)).willReturn(cartSoftReservationDataList);
	}


	/*
	 * @Test public void getTopTwoWishlistForUser() throws CMSItemNotFoundException { final BrandModel brandModel =
	 * Mockito.mock(BrandModel.class); final BuyBoxModel buyBoxModel = Mockito.mock(BuyBoxModel.class); final
	 * GetWishListProductWsDTO getWishListProductWsObj = Mockito.mock(GetWishListProductWsDTO.class); final
	 * GetWishListDataWsDTO getWishListDataWsDTOObj = Mockito.mock(GetWishListDataWsDTO.class); final GetWishListWsDTO
	 * getWishListWsDTO = Mockito.mock(GetWishListWsDTO.class); final Wishlist2Model wishlist2Model =
	 * Mockito.mock(Wishlist2Model.class); final Wishlist2EntryModel wishlist2EntryModel =
	 * Mockito.mock(Wishlist2EntryModel.class); final UserModel userModelMock = Mockito.mock(UserModel.class); final
	 * ProductModel productModelMock = Mockito.mock(ProductModel.class); userModelMock.setEncodedPassword("psd234");
	 * userModelMock.setDescription("desc"); wishlist2Model.setName("wishlist1");
	 * wishlist2Model.setDescription("description"); final String pincode = "700156"; final List<Wishlist2Model>
	 * allWishlists = new ArrayList<Wishlist2Model>();
	 * given(wishlistService.getWishlists(userModelMock)).willReturn(allWishlists); final int minGiftQuantity = 2; final
	 * List<Wishlist2EntryModel> sortedWishList = new ArrayList<Wishlist2EntryModel>();
	 * wishlist2EntryModel.setDesired(Integer.valueOf(4)); wishlist2EntryModel.setReceived(Integer.valueOf(7));
	 * wishlist2EntryModel.setUssid("123654098765485130011713");
	 * given(mplCommerceCartServiceImpl.getGiftYourselfDetails(minGiftQuantity, allWishlists,
	 * pincode)).willReturn(sortedWishList);
	 *
	 * final Map<String, List<Wishlist2EntryModel>> sortedWishListMap = new HashMap<String, List<Wishlist2EntryModel>>();
	 * final List<GetWishListDataWsDTO> getWishListDataWsDTOList = new ArrayList<GetWishListDataWsDTO>(); final String
	 * wishListName = "DefaultWishlist"; final List<Wishlist2EntryModel> wishlist2EntryModelList = new
	 * ArrayList<Wishlist2EntryModel>();
	 *
	 * wishlist2EntryModelList.add(wishlist2EntryModel); sortedWishListMap.put((wishListName), wishlist2EntryModelList);
	 * final List<GetWishListProductWsDTO> getWishListProductWsList = new ArrayList<GetWishListProductWsDTO>();
	 * productModelMock.setArticleDescription("article 12"); productModelMock.setCode("987654321");
	 * productModelMock.setManufacturerName("manufacturer21"); wishlist2EntryModel.setProduct(productModelMock); final
	 * List<BrandModel> brandModelList = new ArrayList<BrandModel>(); brandModel.setDescription(
	 * "Leading electronic Brand "); brandModel.setName("Samsung");
	 *
	 * brandModelList.add(brandModel); productModelMock.setBrands(brandModelList); final List<BuyBoxModel>
	 * buyBoxModelList = buyBoxDao.getBuyBoxPriceForUssId(wishlist2EntryModel.getUssid());
	 * buyBoxModel.setAvailable(Integer.valueOf(71)); buyBoxModel.setPrice(Double.valueOf(1234));
	 * buyBoxModel.setSellerId("12345"); buyBoxModel.setSellerArticleSKU("123654098765485130011713");
	 * buyBoxModel.setProduct("electronic");
	 *
	 * buyBoxModelList.add(buyBoxModel);
	 * given(buyBoxDao.getBuyBoxPriceForUssId("123654098765485130011713")).willReturn(buyBoxModelList); Double finalPrice
	 * = Double.valueOf(0.0); final Double mrpPrice = Double.valueOf(800); finalPrice = mrpPrice; final String color =
	 * "red"; final String size = "34"; final String imageUrl = "www.getMyPic.com";
	 * getWishListProductWsObj.setUSSID((wishlist2EntryModel.getUssid())); getWishListProductWsObj.setColor(color);
	 * getWishListProductWsObj.setDate(wishlist2EntryModel.getAddedDate());
	 *
	 * getWishListProductWsObj.setImageURL(imageUrl); getWishListProductWsObj.setPrice(finalPrice);
	 * getWishListProductWsObj.setProductBrand((brandModelList.get(0).getName()));
	 *
	 * getWishListProductWsObj.setProductcode("987654321");
	 * getWishListProductWsObj.setSellerId((buyBoxModelList.get(0).getSellerId()));
	 * getWishListProductWsObj.setSellerName((buyBoxModelList.get(0).getSellerName()));
	 * getWishListProductWsObj.setSize(size); getWishListProductWsList.add(getWishListProductWsObj);
	 *
	 * getWishListDataWsDTOObj.setName(wishListName); getWishListDataWsDTOObj.setProducts(getWishListProductWsList);
	 * getWishListDataWsDTOList.add(getWishListDataWsDTOObj);
	 * given(mplCommerceCartServiceImpl.getTopTwoWishlistForUser(userModelMock, pincode)).willReturn(getWishListWsDTO); }
	 */

	@Test
	public void addCartCodEligible()
	{
		final PriceData priceData = Mockito.mock(PriceData.class);
		final MarketplaceDeliveryModeData deliveryModeData = Mockito.mock(MarketplaceDeliveryModeData.class);
		final DeliveryDetailsData deliveryDetailsData = Mockito.mock(DeliveryDetailsData.class);
		final PinCodeResponseData pinCodeResponseData = Mockito.mock(PinCodeResponseData.class);

		final List<MarketplaceDeliveryModeData> mpldeliveryModeData = new ArrayList<MarketplaceDeliveryModeData>();
		final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		deliveryModeData.setName("home-delivery");
		deliveryModeData.setSellerArticleSKU("123654098765485130011713");
		deliveryModeData.setDeliveryCost(priceData);

		mpldeliveryModeData.add(deliveryModeData);
		deliveryModeMap.put("HD", mpldeliveryModeData);
		Boolean codEligible = Boolean.TRUE;
		final List<PinCodeResponseData> pinCodeEntry = new ArrayList<PinCodeResponseData>();
		pinCodeResponseData.setCod("Y");
		pinCodeResponseData.setIsServicable("Y");
		final String ussid = "123654098765485130011713";
		pinCodeResponseData.setUssid(ussid);
		final List<DeliveryDetailsData> validDeliveryModes = new ArrayList<>();
		deliveryDetailsData.setType("Home-delivery");
		deliveryDetailsData.setIsCOD(Boolean.TRUE);
		deliveryDetailsData.setInventory("46");
		pinCodeResponseData.setValidDeliveryModes(validDeliveryModes);

		pinCodeEntry.add(pinCodeResponseData);
		codEligible = Boolean.FALSE;


		Mockito.when(Boolean.valueOf(mplCommerceCartServiceImpl.addCartCodEligible(deliveryModeMap, pinCodeEntry)))
				.thenReturn(Boolean.valueOf((codEligible.booleanValue())));
	}
}
