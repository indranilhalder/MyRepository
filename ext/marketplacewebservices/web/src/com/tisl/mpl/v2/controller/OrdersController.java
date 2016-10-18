/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.acceleratorfacades.flow.impl.SessionOverrideCheckoutFlowFacade;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.ProductLowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.exceptions.NoCheckoutCartException;
import com.tisl.mpl.exceptions.PaymentAuthorizationException;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.account.register.impl.DefaultMplOrderFacade;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.SendInvoiceData;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.order.facade.GetOrderDetailsFacade;
import com.tisl.mpl.service.MplProductWebService;
import com.tisl.mpl.strategies.OrderCodeIdentificationStrategy;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.v2.helper.OrdersHelper;
import com.tisl.mpl.wsdto.GetOrderHistoryListWsDTO;
import com.tisl.mpl.wsdto.OrderConfirmationWsDTO;
import com.tisl.mpl.wsdto.OrderDataWsDTO;
import com.tisl.mpl.wsdto.OrderProductWsDTO;
import com.tisl.mpl.wsdto.OrderTrackingWsDTO;
import com.tisl.mpl.wsdto.SelectedDeliveryModeWsDTO;
import com.tisl.mpl.wsdto.UserResultWsDto;
import com.tisl.mpl.wsdto.WebSerResponseWsDTO;


/**
 * Web Service Controller for the ORDERS resource. Most methods check orders of the user. Methods require authentication
 * and are restricted to https channel.
 *
 * @pathparam code Order GUID (Globally Unique Identifier) or order CODE
 * @pathparam userId User identifier or one of the literals below :
 *            <ul>
 *            <li>'current' for currently authenticated user</li>
 *            <li>'anonymous' for anonymous user</li>
 *            </ul>
 */
@Controller
@RequestMapping(value = "/{baseSiteId}")
@SuppressWarnings(
{ "PMD" })
public class OrdersController extends BaseCommerceController
{
	private final static Logger LOG = Logger.getLogger(OrdersController.class);

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;
	@Resource(name = "orderCodeIdentificationStrategy")
	private OrderCodeIdentificationStrategy orderCodeIdentificationStrategy;
	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;
	//TODO avoid using following services
	//	@Resource(name = "commerceCartService")
	//	private CommerceCartService commerceCartService;
	//	@Resource(name = "cartService")
	//	private CartService cartService;
	@Resource(name = "userService")
	private UserService userService;
	//	@Resource(name = "baseSiteService")
	//	private BaseSiteService baseSiteService;
	@Resource(name = "ordersHelper")
	private OrdersHelper ordersHelper;
	@Resource(name = "registerCustomerFacade")
	private RegisterCustomerFacade registerCustomerFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource
	private WishlistFacade wishlistFacade;
	@Resource
	private MplCheckoutFacade mplCheckoutFacade;
	//	@Resource(name = "productFacade")
	//	private ProductFacade productFacade;
	@Resource(name = "productService")
	private ProductService productService;
	//	@Resource(name = "defaultPromotionManager")
	//	private DefaultPromotionManager defaultPromotionManager;
	@Resource(name = "getOrderDetailsFacade")
	private GetOrderDetailsFacade getOrderDetailsFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "defaultEmailService")
	private EmailService emailService;

	//	@Resource(name = "eventService")
	//	private EventService eventService;
	@Resource
	private MplOrderService mplOrderService;
	@Resource
	private MplOrderFacade mplOrderFacade;
	@Resource
	private MplPaymentWebFacade mplPaymentWebFacade;
	/*
	 * @Autowired private BaseStoreService baseStoreService;
	 * 
	 * @Autowired private CheckoutCustomerStrategy checkoutCustomerStrategy;
	 * 
	 * @Autowired private CustomerAccountService customerAccountService;
	 */
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;
	@Resource
	private ModelService modelService;
	@Resource
	private MplProductWebService productWebService;
	@Resource
	private DefaultMplOrderFacade defaultMplOrderFacade;
	@Resource
	private MplSellerInformationService mplSellerInformationService;
	@Resource(name = "mplDataMapper")
	protected DataMapper mplDataMapper;

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
	 * Returns details of a specific order based on order GUID (Globally Unique Identifier) or order CODE. The response
	 * contains a detailed order information.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Order data
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/orders/{code}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'getOrder',#code,#fields)")
	@ResponseBody
	public OrderWsDTO getOrder(@PathVariable final String code, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		OrderData orderData;
		if (orderCodeIdentificationStrategy.isID(code))
		{
			orderData = orderFacade.getOrderDetailsForGUID(code);
		}
		else
		{
			orderData = orderFacade.getOrderDetailsForCodeWithoutUser(code);
		}

		return dataMapper.map(orderData, OrderWsDTO.class, fields);
	}

	/**
	 * Returns specific order details based on a specific order code. The response contains detailed order information.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Order data
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/users/{userId}/orders/{code}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'getOrderForUserByCode',#code,#fields)")
	@ResponseBody
	public OrderWsDTO getOrderForUserByCode(@PathVariable final String code,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final OrderData orderData = orderFacade.getOrderDetailsForCode(code);
		final OrderWsDTO dto = dataMapper.map(orderData, OrderWsDTO.class, fields);
		return dto;
	}

	/**
	 * Returns order history data for all orders placed by the specific user for the specific base store. Response
	 * contains orders search result displayed in several pages if needed.
	 *
	 * @queryparam statuses Filters only certain order statuses. It means: statuses=CANCELLED,CHECKED_VALID would only
	 *             return orders with status CANCELLED or CHECKED_VALID.
	 * @queryparam currentPage The current result page requested.
	 * @queryparam pageSize The number of results returned per page.
	 * @queryparam sort Sorting method applied to the return results.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Order history data.
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.GET)
	@ResponseBody
	public OrderHistoryListWsDTO getOrdersForUser(@RequestParam(required = false) final String statuses,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false) final String sort, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletResponse response)
	{
		final OrderHistoryListWsDTO orderHistoryList = ordersHelper.searchOrderHistory(statuses, currentPage, pageSize, sort,
				addPaginationField(fields));

		// X-Total-Count header
		setTotalCountHeader(response, orderHistoryList.getPagination());

		return orderHistoryList;
	}

	/**
	 * Returns {@value BaseController#HEADER_TOTAL_COUNT} header with a total number of results (orders history for all
	 * orders placed by the specific user for the specific base store).
	 *
	 * @queryparam statuses Filters only certain order statuses. It means: statuses=CANCELLED,CHECKED_VALID would only
	 *             return orders with status CANCELLED or CHECKED_VALID.
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.HEAD)
	@ResponseBody
	public void getCountOrdersForUser(@RequestParam(required = false) final String statuses, final HttpServletResponse response)
	{
		final OrderHistoriesData orderHistoriesData = ordersHelper.searchOrderHistory(statuses, 0, 1, null);

		setTotalCountHeader(response, orderHistoriesData.getPagination());
	}

	/**
	 * Authorizes cart and places the order. Response contains the new order data.
	 *
	 * @formparam cartId Cart code for logged in user, cart GUID for guest checkout
	 * @formparam securityCode CCV security code.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Created order data
	 * @throws PaymentAuthorizationException
	 *            When there are problems with the payment authorization. For example: there is no session cart or no
	 *            payment information set for the cart.
	 * @throws InvalidCartException
	 * @throws WebserviceValidationException
	 *            When the cart is not filled properly (e. g. delivery mode is not set, payment method is not set)
	 * @throws ProductLowStockException
	 *            When product is out of stock in store
	 * @throws StockSystemException
	 *            When there is no information about stock for stores.
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public OrderWsDTO placeOrder(@RequestParam(required = true) final String cartId,
			@RequestParam(required = false) final String securityCode,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) throws PaymentAuthorizationException,
			InvalidCartException, WebserviceValidationException, NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.info("placeOrder");
		}

		cartLoaderStrategy.loadCart(cartId);

		validateCartForPlaceOrder();

		//authorize
		if (!checkoutFacade.authorizePayment(securityCode))
		{
			throw new PaymentAuthorizationException();
		}

		//placeorder
		final OrderData orderData = checkoutFacade.placeOrder();
		final OrderWsDTO dto = dataMapper.map(orderData, OrderWsDTO.class, fields);
		return dto;
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@RequestMapping(value = "/users/{userId}/getOrdersDetails", method = RequestMethod.GET)
	@ResponseBody
	public OrderHistoryListWsDTO getOrdersDetailsofUser(@RequestParam(required = false) final String statuses,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false) final String sort, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletResponse response)
	{
		final OrderHistoryListWsDTO orderHistoryList = ordersHelper.searchOrderHistory(statuses, currentPage, pageSize, sort,
				addPaginationField(fields));
		try
		{
			setTotalCountHeader(response, orderHistoryList.getPagination());
			return orderHistoryList;
		}
		catch (final EtailBusinessExceptions e)
		{
			return orderHistoryList;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			return orderHistoryList;
		}
	}

	/*
	 * @description Send invoice for mobile service
	 * 
	 * @param orderNumber
	 * 
	 * @param lineID
	 */

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/users/{emailID}/sendInvoice", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@ResponseBody
	public UserResultWsDto sendInvoice(@PathVariable final String emailID,
			@RequestParam(required = true) final String orderNumber, @RequestParam(required = true) final String lineID)
	{
		final UserResultWsDto response = new UserResultWsDto();
		try
		{
			final SendInvoiceData sendInvoiceData = new SendInvoiceData();
			// invoiceFile emailAttachedment
			final OrderModel orderModel = orderModelService.getOrder(orderNumber);
			if (orderModel != null)
			{
				int count = 0;
				for (final AbstractOrderEntryModel entry : orderModel.getEntries())
				{
					if (entry.getOrderLineId() != null && entry.getConsignmentEntries() != null)
					{
						if (entry.getOrderLineId().equalsIgnoreCase(lineID))
						{
							//Fetching invoice from consignment entries
							for (final ConsignmentEntryModel c : entry.getConsignmentEntries())
							{
								if (null != c.getConsignment().getInvoice())
								{

									final String invoicePathURL = c.getConsignment().getInvoice().getInvoiceUrl();

									//Fix for defect TISPIT-145
									if (null == invoicePathURL)
									{
										LOG.error("***************INVOICE URL is missing******************");
										throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0015);
									}
									else
									{
										sendInvoiceData.setInvoiceUrl(invoicePathURL);
									}

									/*
									 * final File invoiceFile = new File(invoicePathURL); FileInputStream input = null;
									 * 
									 * if (invoiceFile.exists()) { String invoiceFileName = null; final String preInvoiceFileName
									 * = invoiceFile.getName(); if (!preInvoiceFileName.isEmpty()) { final int index =
									 * preInvoiceFileName.lastIndexOf('.'); if (index > 0) { invoiceFileName =
									 * preInvoiceFileName.substring(0, index) + "_" + lineID + "_" + new
									 * Timestamp(System.currentTimeMillis()) + "." + preInvoiceFileName.substring(index + 1); } }
									 */
									try
									{
										/*
										 * input = new FileInputStream(invoiceFile); final EmailAttachmentModel emailAttachment =
										 * emailService.createEmailAttachment( new DataInputStream(input), invoiceFileName,
										 * "application/octet-stream"); sendInvoiceData.setInvoiceUrl(emailAttachment.getCode());
										 */
										sendInvoiceData.setCustomerEmail(emailID);
										sendInvoiceData.setOrdercode(orderNumber);
										sendInvoiceData.setLineItemId(lineID);
										sendInvoiceData.setTransactionId(lineID);
										registerCustomerFacade.sendInvoice(sendInvoiceData, null);
										response.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);


									}
									catch (final Exception ex)
									{
										ExceptionUtil.getCustomizedExceptionTrace(ex);
										response.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
										response.setErrorCode(MarketplacecommerceservicesConstants.E0000);
										response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
										return response;
									}

									//}

								}
							}
							break;
						}

					}
					else
					{

						response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9033);
					}
					count++;
				}
				if (count == orderModel.getEntries().size())
				{
					//invalid lineID entry
					response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9058);
				}

			}
			else
			{
				//invalid lineID entry
				response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9034);
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				response.setError(e.getErrorMessage());
				response.setErrorCode(e.getErrorCode());

			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				response.setError(e.getErrorMessage());
				response.setErrorCode(e.getErrorCode());
			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return response;

	}

	/**
	 * @description Confirm the Order and send response as success
	 * @queryparam Order reference code
	 * @return Order Confirmation DTO
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/users/{userId}/orderConfirmation/{orderCode}", method = RequestMethod.GET)
	//@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@ResponseBody
	public OrderConfirmationWsDTO orderConfirmation(@PathVariable final String orderCode, final HttpServletRequest request)
	{
		OrderConfirmationWsDTO response = new OrderConfirmationWsDTO();
		OrderModel orderModel = null;
		OrderData orderDetail = null;
		try
		{
			//removeProductFromWL(orderCode);
			orderModel = mplOrderFacade.getOrder(orderCode); //TISPT-175 --- order model changes : reduce same call from two places
			orderDetail = mplCheckoutFacade.getOrderDetailsForCode(orderModel); //TISPT-175 --- order details : reduce same call from two places
			wishlistFacade.remProdFromWLForConf(orderDetail, orderModel.getUser()); //TISPT-175 --- removing products from wishlist : passing order data as it was fetching order data based on code again inside the method
			SessionOverrideCheckoutFlowFacade.resetSessionOverrides();
			response = processOrderCode(orderCode, orderModel, orderDetail, request);
		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				response.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				response.setErrorCode(e.getErrorCode());
			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				response.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				response.setErrorCode(e.getErrorCode());
			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return response;
	}

	//TODO It was added in respect of CheckoutController.java
	/*
	 * protected void removeProductFromWL(final List<OrderEntryData> orderEntryDatas) { if (null !=
	 * sessionService.getAttribute("wishlistDatas")) { final Collection<WishlistData> wishlistDatas =
	 * sessionService.getAttribute("wishlistDatas"); final List<WishlistData> list = new ArrayList<>(); final
	 * Iterator<WishlistData> iterator = wishlistDatas.iterator(); while (iterator.hasNext()) {
	 * list.add(iterator.next()); } if (null != orderEntryDatas && !orderEntryDatas.isEmpty()) { for (final
	 * OrderEntryData orderEntryData : orderEntryDatas) { final String productCode =
	 * orderEntryData.getProduct().getCode(); for (final WishlistData data : list) { if
	 * (data.getProductCode().equals(productCode)) { final Wishlist2Model wishlist2Model =
	 * wishlistFacade.removeProductFromWl(data.getProductCode(), data.getParticularWishlistName(), data.getUssid());
	 * LOG.debug("wishlist which has been modified is >>>>>>>>>>\t" + wishlist2Model.getName()); } } } }
	 * sessionService.removeAttribute("wishlistDatas"); } else { LOG.debug("no session attribute wishlist"); } }
	 */

	/**
	 * @description method is to set Thumbnail image on Product
	 * @param product
	 * @return String
	 */
	protected String setImageURL(final ProductData product)
	{
		String image = "";
		final List<ImageData> images = (List<ImageData>) product.getImages();
		if (null != images)
		{
			for (final ImageData imageData : product.getImages())
			{
				if (imageData.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.THUMBNAIL))
				{
					image = imageData.getUrl();
					break;
				}
			}
			if (image.equalsIgnoreCase(""))
			{
				image = images.get(0).getUrl();
			}
		}
		return image;
	}

	/*
	 * private static String stripNonDigits(final CharSequence input) { final StringBuilder sb = new
	 * StringBuilder(input.length()); for (int i = 0; i < input.length(); i++) { final char c = input.charAt(i); if ((c >
	 * 47 && c < 58) || (c == 46)) { sb.append(c); } } return sb.toString(); }
	 */
	//TODO It was added in respect of CheckoutController.java
	protected OrderConfirmationWsDTO processOrderCode(final String orderCode, final OrderModel orderModel,
			final OrderData orderDetail, final HttpServletRequest request) throws EtailNonBusinessExceptions
	{
		final OrderConfirmationWsDTO orderWsDTO = new OrderConfirmationWsDTO();
		OrderProductWsDTO orderProductDTO = null;
		ProductData product = null;
		//final String paymentMethod = "";
		MarketplaceDeliveryModeData mplDeliveryMode = null;
		final List<OrderProductWsDTO> orderProductDTOList = new ArrayList<>();
		String isGiveAway = "N";
		double deliveryTotal = 0.0d;
		double totalDiscount = 0.0d;
		try
		{
			//final Commented for Delivery cost fix final in order final confirmation page
			//orderDetail = orderFacade.getOrderDetailsForCode(orderCode);
			//orderDetail = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			/*
			 * if (orderDetails.isGuestCustomer() && !StringUtils.substringBefore(orderDetails.getUser().getUid(),
			 * "|").equals( getSessionService().getAttribute(WebConstants.ANONYMOUS_CHECKOUT_GUID))) { return
			 * getCheckoutRedirectUrl(); }
			 */
			if (null != orderDetail && orderDetail.getEntries() != null && !orderDetail.getEntries().isEmpty())
			{
				final SimpleDateFormat sdformat = new SimpleDateFormat(MarketplacewebservicesConstants.DATEFORMAT_FULL);
				orderWsDTO.setOrderRefNo(orderDetail.getCode());
				orderWsDTO.setPlacedDate((sdformat.format(orderDetail.getCreated())).toString());
				if (orderDetail.getSubTotal() != null)
				{
					orderWsDTO.setSubTotal(orderDetail.getSubTotal().getValue().toString());
				}
				if (null != orderDetail.getCouponDiscount() && null != orderDetail.getCouponDiscount().getValue())
				{
					orderWsDTO.setCouponDiscount(orderDetail.getCouponDiscount().getValue().toString());
				}

				if (null != orderDetail.getPickupName())
				{
					orderWsDTO.setPickupPersonName(orderDetail.getPickupName());
				}
				if (null != orderDetail.getPickupPhoneNumber())
				{
					orderWsDTO.setPickupPersonMobile(orderDetail.getPickupPhoneNumber());
				}
				/*
				 * if (orderDetail.getTotalPriceWithTax() != null) {
				 * orderWsDTO.setFinalAmount(orderDetail.getTotalPriceWithTax().getValue().toString()); } if
				 * (orderDetail.getDeliveryCost() != null) {
				 * orderWsDTO.setDeliveryTotal(orderDetail.getDeliveryCost().getValue().toString()); } else {
				 * orderWsDTO.setDeliveryTotal("0.0"); } double discount = 0.0; if (null != orderDetail.getSubTotal() &&
				 * null != orderDetail.getTotalPrice()) { discount = (orderDetail.getSubTotal().getValue().doubleValue() -
				 * orderDetail.getTotalPrice().getValue() .doubleValue()); }
				 */
				if (orderDetail.getDeliveryCost() != null)
				{
					deliveryTotal = orderDetail.getDeliveryCost().getValue().doubleValue();

				}
				orderWsDTO.setDeliveryTotal(Double.toString(deliveryTotal));
				//Change for ConvenienceCharge
				if (orderDetail.getConvenienceChargeForCOD() != null)
				{
					orderWsDTO.setConvenienceCharge(orderDetail.getConvenienceChargeForCOD().getValue().toString());
				}

				//Change for DiscountPrice
				if (orderDetail.getAppliedOrderPromotions() != null)
				{
					/*
					 * for (final PromotionResultData promotionResultData : orderDetail.getAppliedOrderPromotions()) { final
					 * String st = promotionResultData.getDescription(); final String result = stripNonDigits(st);
					 * totalDiscount = totalDiscount + Double.parseDouble(result); }
					 */
					if (null != orderDetail.getTotalDiscounts() && null != orderDetail.getTotalDiscounts().getValue())
					{
						totalDiscount = orderDetail.getTotalDiscounts().getValue().doubleValue();
					}
				}
				orderWsDTO.setDiscountPrice(Double.toString(totalDiscount));
				if (null != orderDetail.getTotalPriceWithConvCharge()
						&& StringUtils.isNotEmpty(orderDetail.getTotalPriceWithConvCharge().getValue().toString()))
				{
					orderWsDTO.setFinalAmount(orderDetail.getTotalPriceWithConvCharge().getValue().toString());
				}
				//// Move entire payment info code to new method.
				GenericUtilityMethods.setPaymentInfo(orderDetail, orderWsDTO); //TODO set payment Info
				if (null != orderDetail.getMplPaymentInfo() && null != orderDetail.getMplPaymentInfo().getBillingAddress())
				{
					orderWsDTO.setBillingAddress(GenericUtilityMethods.setAddress(orderDetail, 1));
				}
				if (null != orderDetail.getDeliveryAddress())
				{
					orderWsDTO.setShippingAddress(GenericUtilityMethods.setAddress(orderDetail, 2));
				}
				for (final OrderData sellerOrder : orderDetail.getSellerOrderList())
				{
					if (CollectionUtils.isNotEmpty(sellerOrder.getEntries()))
					{
						for (final OrderEntryData entry : sellerOrder.getEntries())
						{

							orderProductDTO = new OrderProductWsDTO();
							product = entry.getProduct();
							orderProductDTO.setProductcode(product.getCode());
							orderProductDTO.setProductName(product.getName());
							if (null != product.getBrand())
							{
								orderProductDTO.setProductBrand(product.getBrand().getBrandname());
							}
							orderProductDTO.setProductDescription(product.getArticleDescription());
							orderProductDTO.setImageURL(setImageURL(product));
							if (entry.isGiveAway())
							{
								isGiveAway = "Y";
							}
							else
							{
								isGiveAway = "N";
							}
							orderProductDTO.setIsGiveAway(isGiveAway);
							orderProductDTO.setAssociatedProducts(entry.getAssociatedItems());
							if (StringUtils.isNotEmpty(sellerOrder.getCode()))
							{
								orderProductDTO.setSellerOrderNo(sellerOrder.getCode());
							}
							//product Price TISEE-949
							if (null != entry.getAmountAfterAllDisc())
							{
								orderProductDTO.setPricevalue(Double.valueOf(entry.getAmountAfterAllDisc().getValue().toString()));
							}
							if (null != entry.getQuantity())
							{
								orderProductDTO.setQuantity(entry.getQuantity().toString());
							}

							if (null != product.getRootCategory())
							{
								orderProductDTO.setRootCategory(product.getRootCategory());
							}
							final String productCategory = productWebService.getCategoryCodeOfProduct(product);

							if (null != productCategory)
							{
								orderProductDTO.setProductCategory(productCategory);
							}

							if (StringUtils.isNotEmpty(product.getSize()))
							{
								orderProductDTO.setSize(product.getSize());
							}
							if (StringUtils.isNotEmpty(product.getColour()))
							{
								orderProductDTO.setProductColour(product.getColour());
							}
							/* capacity */
							ProductModel productModel = null;
							if (null != entry.getProduct() && null != entry.getProduct().getCode())
							{
								productModel = productService.getProductForCode(entry.getProduct().getCode());
							}
							if (productModel instanceof PcmProductVariantModel)
							{
								final PcmProductVariantModel selectedVariantModel = (PcmProductVariantModel) productModel;
								final String selectedCapacity = selectedVariantModel.getCapacity();
								final ProductModel baseProduct = selectedVariantModel.getBaseProduct();
								if (null != baseProduct.getVariants() && null != selectedCapacity)
								{
									for (final VariantProductModel vm : baseProduct.getVariants())
									{
										final PcmProductVariantModel pm = (PcmProductVariantModel) vm;
										if (!selectedCapacity.isEmpty() && null != pm.getCapacity()
												&& selectedCapacity.equals(pm.getCapacity()))
										{
											orderProductDTO.setCapacity(pm.getCapacity());
										}
									}
								}
							}

							/* capacity */

							SellerInformationModel sellerInfoModel = null;
							String fulfillmentType = null;
							if (StringUtils.isNotEmpty(entry.getSelectedUssid()))
							{
								sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
							}
							if (sellerInfoModel != null
									&& CollectionUtils.isNotEmpty(sellerInfoModel.getRichAttribute())
									&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
							{
								/* Fulfillment type */
								fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
										.getDeliveryFulfillModes().getCode();
								if (StringUtils.isNotEmpty(fulfillmentType))
								{
									orderProductDTO.setFulfillment(fulfillmentType);
								}
							}

							if (entry.getMplDeliveryMode() != null)
							{
								mplDeliveryMode = entry.getMplDeliveryMode();
								final SelectedDeliveryModeWsDTO selectedDeliveryModeWsDTO = new SelectedDeliveryModeWsDTO();
								if (StringUtils.isNotEmpty(mplDeliveryMode.getCode()))
								{
									selectedDeliveryModeWsDTO.setCode(mplDeliveryMode.getCode());
								}
								if (StringUtils.isNotEmpty(mplDeliveryMode.getName()))
								{
									selectedDeliveryModeWsDTO.setName(mplDeliveryMode.getName());
								}
								if (!entry.isGiveAway() && entry.getCurrDelCharge() != null
										&& StringUtils.isNotEmpty(entry.getCurrDelCharge().getFormattedValue()))
								{
									//selectedDeliveryModeWsDTO.setDeliveryCost(mplDeliveryMode.getDeliveryCost().getValue().toString());
									selectedDeliveryModeWsDTO.setDeliveryCost(entry.getCurrDelCharge().getFormattedValue());
								}
								else
								{
									selectedDeliveryModeWsDTO.setDeliveryCost("0.0");
								}
								if (StringUtils.isNotEmpty(mplDeliveryMode.getDescription()))
								{
									selectedDeliveryModeWsDTO.setDesc(mplDeliveryMode.getDescription());
								}
								orderProductDTO.setSelectedDeliveryMode(selectedDeliveryModeWsDTO);
							}
							//add store details
							if (null != entry.getDeliveryPointOfService())
							{
								orderProductDTO.setStoreDetails(mplDataMapper.map(entry.getDeliveryPointOfService(),
										PointOfServiceWsDTO.class, "DEFAULT"));
							}
							setSellerInfo(entry, orderProductDTO);
							orderProductDTOList.add(orderProductDTO);



						}
					}
				}
				//Handling of Order Status Message display
				if (null != orderDetail.getStatus() && StringUtils.isNotEmpty(orderDetail.getStatus().toString()))
				{
					if (orderDetail.getStatus().equals(OrderStatus.RMS_VERIFICATION_PENDING))
					{
						orderWsDTO.setOrderStatusMessage(configurationService.getConfiguration().getString(
								MarketplacecommerceservicesConstants.ORDER_CONF_HELD));
					}
					else if (orderDetail.getStatus().equals(OrderStatus.PAYMENT_SUCCESSFUL))
					{
						orderWsDTO.setOrderStatusMessage(configurationService.getConfiguration().getString(
								MarketplacecommerceservicesConstants.ORDER_CONF_SUCCESS));
					}
				}

				orderWsDTO.setProducts(orderProductDTOList);
				orderWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				//saving IP of the Customer
				try
				{
					final String userIpAddress = request.getHeader("X-Forwarded-For");
					orderModel.setIpAddress(userIpAddress);
					modelService.save(orderModel);
				}
				catch (final Exception e)
				{
					LOG.debug("Exception during IP save", e);
				}

			}
			else
			{
				orderWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				orderWsDTO.setError(MarketplacecommerceservicesConstants.ORDER_NOT_CONFIRMED);
			}

			/*
			 * TISEE-5333 , TISEE-5332 if (null != orderDetail) { final BaseStoreModel baseStoreModel =
			 * baseStoreService.getCurrentBaseStore(); final OrderModel orderModel =
			 * checkoutCustomerStrategy.isAnonymousCheckout() ? customerAccountService .getOrderDetailsForGUID(orderCode,
			 * baseStoreModel) : customerAccountService.getOrderForCode( (CustomerModel) userService.getCurrentUser(),
			 * orderCode, baseStoreModel); mplCheckoutFacade.sendMobileNotifications(orderDetail);
			 * mplCheckoutFacade.triggerEmailAndSmsOnOrderConfirmation(orderModel, orderDetail, " "); }
			 */

		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9074);
		}
		return orderWsDTO;
	}

	/* Setting Seller Information */
	protected void setSellerInfo(final OrderEntryData product, final OrderProductWsDTO reportDTO)
	{
		//Freebie and non-freebie seller detail population
		SellerInformationModel sellerInfoModel = null;
		if (StringUtils.isNotEmpty(product.getSelectedUssid()))
		{
			sellerInfoModel = getMplSellerInformationService().getSellerDetail(product.getSelectedUssid());

			if (null != sellerInfoModel && StringUtils.isNotEmpty(sellerInfoModel.getSellerID()))
			{
				reportDTO.setSellerID(sellerInfoModel.getSellerID());
			}
			if (null != sellerInfoModel && StringUtils.isNotEmpty(sellerInfoModel.getUSSID()))
			{
				reportDTO.setUSSID(sellerInfoModel.getUSSID());
			}
			if (null != sellerInfoModel && StringUtils.isNotEmpty(sellerInfoModel.getSellerName()))
			{
				reportDTO.setSellerName(sellerInfoModel.getSellerName());
			}
		}
	}




	/**
	 * @description method is called to fetch the details of a particular orders for the user
	 * @param orderCode
	 * @return OrderTrackingWsDTO
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/users/{userId}/getSelectedOrder/{orderCode}", method = RequestMethod.GET)
	@ResponseBody
	public OrderTrackingWsDTO getOrdertracking(@PathVariable final String orderCode)
	{
		OrderTrackingWsDTO orderTrackingWsDTO = new OrderTrackingWsDTO();
		try
		{
			orderTrackingWsDTO = getOrderDetailsFacade.getOrderDetailsWithTracking(orderCode);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				orderTrackingWsDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				orderTrackingWsDTO.setErrorCode(e.getErrorCode());
			}
			orderTrackingWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				orderTrackingWsDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				orderTrackingWsDTO.setErrorCode(e.getErrorCode());
			}
			orderTrackingWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			orderTrackingWsDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			orderTrackingWsDTO.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			orderTrackingWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return orderTrackingWsDTO;

	}




	/**
	 * @description method is called to fetch the history details of all orders for the user
	 * @param statuses
	 * @param currentPage
	 * @param pageSize
	 * @param sort
	 * @param fields
	 * @return GetOrderHistoryListWsDTO
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@RequestMapping(value = "/users/{userId}/orderhistorylist", method = RequestMethod.GET)
	@ResponseBody
	public GetOrderHistoryListWsDTO getOrders(@RequestParam(required = false) final String statuses,
			@RequestParam final int currentPage, @RequestParam final int pageSize,
			@RequestParam(required = false) final String sort, @PathVariable final String userId,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{

		final GetOrderHistoryListWsDTO getOrderHistoryListWsDTO = new GetOrderHistoryListWsDTO();
		final List<OrderDataWsDTO> orderTrackingListWsDTO = new ArrayList<OrderDataWsDTO>();
		int orderCount = 0, start = 0, end = 0;
		try
		{
			//			final SearchPageData<OrderData> searchPageDataParentOrder = ordersHelper.getParentOrders(currentPage, pageSize, sort,
			//					userId);
			//TISEE-6323
			final SearchPageData<OrderHistoryData> searchPageDataParentOrder = ordersHelper.getParentOrders(0, pageSize, sort);

			if (null == searchPageDataParentOrder.getResults())
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E9046);
			}
			else
			{
				//LOG.info("Number of Orders " + orderCount);
				for (final OrderHistoryData orderData : searchPageDataParentOrder.getResults())
				{
					final OrderDataWsDTO order = getOrderDetailsFacade.getOrderdetails(orderData.getCode());
					if (null != order)
					{
						orderTrackingListWsDTO.add(order);
						orderCount++;
					}
				}
				if (orderTrackingListWsDTO.size() > 0)
				{
					/*
					 * if (searchPageDataParentOrder.getPagination() != null &&
					 * searchPageDataParentOrder.getPagination().getTotalNumberOfResults() > 0l) { final Long total =
					 * Long.valueOf(searchPageDataParentOrder.getPagination().getTotalNumberOfResults());
					 * getOrderHistoryListWsDTO.setTotalNoOfOrders(Integer.valueOf(total.toString())); }
					 */
					getOrderHistoryListWsDTO.setTotalNoOfOrders(Integer.valueOf(orderCount));
					//setting start end
					start = currentPage * pageSize;
					end = start + pageSize;
					if (end > orderTrackingListWsDTO.size())
					{
						end = orderTrackingListWsDTO.size();
					}
					if (start < orderTrackingListWsDTO.size() && start <= end)
					{
						getOrderHistoryListWsDTO.setOrderData(orderTrackingListWsDTO.subList(start, end));
						getOrderHistoryListWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					}
					else
					{
						getOrderHistoryListWsDTO.setStatus(MarketplacecommerceservicesConstants.CARTDATA);
					}
				}
				else
				{
					getOrderHistoryListWsDTO.setStatus(MarketplacecommerceservicesConstants.CARTDATA);
				}

			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				getOrderHistoryListWsDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				getOrderHistoryListWsDTO.setErrorCode(e.getErrorCode());
			}
			getOrderHistoryListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				getOrderHistoryListWsDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				getOrderHistoryListWsDTO.setErrorCode(e.getErrorCode());
			}
			getOrderHistoryListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			getOrderHistoryListWsDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			getOrderHistoryListWsDTO.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			getOrderHistoryListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return getOrderHistoryListWsDTO;
	}

	/**
	 * @description method is to update the pickup person details for an order
	 * @param orderId
	 * @param name
	 * @param mobile
	 * @return WebSerResponseWsDTO
	 */

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@RequestMapping(value = "/users/{userId}/updatePickupDetails", method = RequestMethod.POST)
	@ResponseBody
	public WebSerResponseWsDTO updatePickupDetails(@RequestParam(required = true, value = "orderId") final String orderId,
			@RequestParam(required = true, value = "name") final String name,
			@RequestParam(required = true, value = "mobile") final String mobile)
	{

		LOG.debug("UpdatePickupDetails mobile method :" + orderId + "name :" + name + "mobile :" + mobile);
		final WebSerResponseWsDTO result = new WebSerResponseWsDTO();
		try
		{
			if (orderId != null && name != null && mobile != null)
			{
				final String message = mplOrderFacade.editPickUpInfo(orderId, name, mobile);
				if ("sucess".equalsIgnoreCase(message))
				{
					result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					LOG.info("PickUpDetails Updated Successfully for order id :" + orderId);
					try
					{
						mplOrderFacade.createCrmTicketUpdatePickDetails(orderId);
					}
					catch (final Exception e)
					{
						LOG.error("Error while creating crm ticket" + e);
					}
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9519);
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9521);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			result.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			result.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return result;
	}

	/**
	 * @description: To find the Cancellation is enabled/disabled on Order status
	 * @param: currentStatus
	 * @return: currentStatus
	 */
	public Boolean checkOrderStatus(final String currentStatus, final String status)
	{
		String cancelStatus = "";
		cancelStatus = configurationService.getConfiguration().getString(status);
		if (cancelStatus.indexOf(currentStatus) == -1)
		{
			return Boolean.FALSE;
		}
		else
		{
			return Boolean.TRUE;
		}


	}
}
