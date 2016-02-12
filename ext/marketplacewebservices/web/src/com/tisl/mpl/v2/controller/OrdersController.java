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
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.ProductLowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.tisl.mpl.data.MplPaymentInfoData;
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
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.AWBResponseData;
import com.tisl.mpl.facades.data.StatusRecordData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.SendInvoiceData;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.OrderStatusCodeMasterModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.order.facade.GetOrderDetailsFacade;
import com.tisl.mpl.service.MplProductWebService;
import com.tisl.mpl.strategies.OrderCodeIdentificationStrategy;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.v2.helper.OrdersHelper;
import com.tisl.mpl.wsdto.BillingAddressWsDTO;
import com.tisl.mpl.wsdto.GetOrderHistoryListWsDTO;
import com.tisl.mpl.wsdto.OrderConfirmationWsDTO;
import com.tisl.mpl.wsdto.OrderDataWsDTO;
import com.tisl.mpl.wsdto.OrderProductWsDTO;
import com.tisl.mpl.wsdto.OrderTrackingWsDTO;
import com.tisl.mpl.wsdto.Ordershipmentdetailstdto;
import com.tisl.mpl.wsdto.SelectedDeliveryModeWsDTO;
import com.tisl.mpl.wsdto.StatusResponseDTO;
import com.tisl.mpl.wsdto.StatusResponseListDTO;
import com.tisl.mpl.wsdto.StatusResponseMessageDTO;
import com.tisl.mpl.wsdto.UserResultWsDto;


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
	@Autowired
	private WishlistFacade wishlistFacade;
	@Autowired
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
	@Autowired
	private MplOrderService mplOrderService;
	@Resource
	private MplOrderFacade mplOrderFacade;
	@Autowired
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
	@Autowired
	private MplProductWebService productWebService;
	@Autowired
	private DefaultMplOrderFacade defaultMplOrderFacade;

	@Autowired
	private MplSellerInformationService mplSellerInformationService;


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
										LOG.error("Exception is" + ex);
										response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
										response.setError(MarketplacewebservicesConstants.ETAIL_NON_BUSINESS_EXCEPTION + ex);
										LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
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
	public OrderConfirmationWsDTO orderConfirmation(@PathVariable final String orderCode)
	{
		OrderConfirmationWsDTO response = new OrderConfirmationWsDTO();
		try
		{
			//removeProductFromWL(orderCode);
			LOG.debug("*********** Order confirmation mobile web service for ********** : " + orderCode);
			wishlistFacade.removeProductFromWL(orderCode);
			SessionOverrideCheckoutFlowFacade.resetSessionOverrides();
			response = processOrderCode(orderCode);
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
	protected OrderConfirmationWsDTO processOrderCode(final String orderCode) throws EtailNonBusinessExceptions
	{
		final OrderConfirmationWsDTO orderWsDTO = new OrderConfirmationWsDTO();
		OrderData orderDetail = null;
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
			orderDetail = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
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
				setPaymentInfo(orderDetail, orderWsDTO); //TODO set payment Info
				orderWsDTO.setBillingAddress(setAddress(orderDetail, 1));
				orderWsDTO.setShippingAddress(setAddress(orderDetail, 2));
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

	/*
	 * @description Setting DeliveryAddress
	 * 
	 * @param orderDetail
	 * 
	 * @param type (1-Billing, 2-Shipping)
	 * 
	 * @return BillingAddressWsDTO
	 */
	protected BillingAddressWsDTO setAddress(final OrderData orderDetail, final int type)
	{
		final BillingAddressWsDTO billingAddress = new BillingAddressWsDTO();
		final String countrycode = "91";
		if (null != orderDetail.getDeliveryAddress().getId() && StringUtils.isNotEmpty(orderDetail.getDeliveryAddress().getId())
				&& type == 2)
		{
			billingAddress.setDefaultAddress(Boolean.valueOf(orderDetail.getDeliveryAddress().isDefaultAddress()));
			billingAddress.setAddressType(orderDetail.getDeliveryAddress().getAddressType());
			billingAddress.setFirstName(orderDetail.getDeliveryAddress().getFirstName());
			billingAddress.setLastName(orderDetail.getDeliveryAddress().getLastName());
			if (null != orderDetail.getDeliveryAddress().getCountry())
			{
				billingAddress.setCountry(orderDetail.getDeliveryAddress().getCountry().getName());
			}
			billingAddress.setTown(orderDetail.getDeliveryAddress().getTown());
			billingAddress.setPostalcode(orderDetail.getDeliveryAddress().getPostalCode());
			billingAddress.setState(orderDetail.getDeliveryAddress().getState());
			billingAddress.setAddressLine1(orderDetail.getDeliveryAddress().getLine1());
			billingAddress.setAddressLine2(orderDetail.getDeliveryAddress().getLine2());
			billingAddress.setAddressLine3(orderDetail.getDeliveryAddress().getLine3());
			billingAddress.setPhone(countrycode + orderDetail.getDeliveryAddress().getPhone());

			billingAddress.setShippingFlag(Boolean.valueOf(orderDetail.getDeliveryAddress().isShippingAddress()));
			billingAddress.setId(orderDetail.getDeliveryAddress().getId());
		}

		if (null != orderDetail.getMplPaymentInfo() && null != orderDetail.getMplPaymentInfo().getBillingAddress() && type == 1)
		{
			final AddressData billAddress = orderDetail.getMplPaymentInfo().getBillingAddress();
			billingAddress.setFirstName(billAddress.getFirstName());
			billingAddress.setLastName(billAddress.getLastName());
			if (null != billAddress.getCountry())
			{
				billingAddress.setCountry(billAddress.getCountry().getName());
			}
			billingAddress.setTown(billAddress.getTown());
			billingAddress.setPostalcode(billAddress.getPostalCode());
			billingAddress.setState(billAddress.getState());
			billingAddress.setAddressLine1(billAddress.getLine1());
			billingAddress.setAddressLine2(billAddress.getLine2());
			billingAddress.setAddressLine3(billAddress.getLine3());
			billingAddress.setPhone(countrycode + billAddress.getPhone());
			billingAddress.setShippingFlag(Boolean.valueOf(billAddress.isShippingAddress()));
			billingAddress.setId(billAddress.getId());
		}

		return billingAddress;

	}

	/* Checking payment type and then setting payment info */
	protected void setPaymentInfo(final OrderData orderDetail, final OrderConfirmationWsDTO orderWsDTO)
	{
		MplPaymentInfoData paymentInfo = null;

		if (null != orderDetail.getMplPaymentInfo())
		{
			paymentInfo = orderDetail.getMplPaymentInfo();

			if (null != paymentInfo.getPaymentOption())
			{
				orderWsDTO.setPaymentMethod(paymentInfo.getPaymentOption());
			}
			if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.CREDIT))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}

				if (StringUtils.isNotEmpty(paymentInfo.getCardIssueNumber()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getCardIssueNumber());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardExpirationMonth().toString())
						&& StringUtils.isNotEmpty(paymentInfo.getCardExpirationYear().toString()))
				{
					orderWsDTO.setPaymentCardExpire(paymentInfo.getCardExpirationMonth() + "/" + paymentInfo.getCardExpirationYear());
				}
			}

			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardIssueNumber()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getCardIssueNumber());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardExpirationMonth().toString())
						&& StringUtils.isNotEmpty(paymentInfo.getCardExpirationYear().toString()))
				{
					orderWsDTO.setPaymentCardExpire(paymentInfo.getCardExpirationMonth() + "/" + paymentInfo.getCardExpirationYear());
				}
			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.NETBANKING))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getBank()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getBank());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}

				orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.DEBIT))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardIssueNumber()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getCardIssueNumber());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardExpirationMonth().toString())
						&& StringUtils.isNotEmpty(paymentInfo.getCardExpirationYear().toString()))
				{
					orderWsDTO.setPaymentCardExpire(paymentInfo.getCardExpirationMonth() + "/" + paymentInfo.getCardExpirationYear());
				}
			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.COD))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}

			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.WALLET))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				orderWsDTO.setPaymentCardDigit(MarketplacecommerceservicesConstants.NA);
				orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			}
		}
		else
		{

			orderWsDTO.setPaymentCard(MarketplacecommerceservicesConstants.NA);
			orderWsDTO.setPaymentCardDigit(MarketplacecommerceservicesConstants.NA);
			orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			orderWsDTO.setCardholdername(MarketplacecommerceservicesConstants.NA);
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

		final OrderTrackingWsDTO orderTrackingWsDTO = new OrderTrackingWsDTO();
		final List<OrderProductWsDTO> orderproductdtos = new ArrayList<OrderProductWsDTO>();
		List<Ordershipmentdetailstdto> ordershipmentdetailstdtos = null;
		OrderProductWsDTO orderproductdto = null;
		String consignmentStatus = "";
		OrderStatusCodeMasterModel customerStatusModel = null;
		OrderData orderDetail = null;
		OrderModel orderModel = null;
		String isGiveAway = "N", formattedProductDate = MarketplacecommerceservicesConstants.EMPTY, formattedActualProductDate = MarketplacecommerceservicesConstants.EMPTY;
		ConsignmentModel consignmentModel = null;
		try
		{

			orderDetail = mplCheckoutFacade.getOrderDetailsForCode(orderCode);

			if (null == orderDetail)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E9047);
			}

			else
			{
				orderTrackingWsDTO.setBillingAddress(setAddress(orderDetail, 1));
				orderTrackingWsDTO.setDeliveryAddress(setAddress(orderDetail, 2));

				orderTrackingWsDTO.setGiftWrapCharge(MarketplacecommerceservicesConstants.ZERO);
				if (null != orderDetail.getCreated())
				{
					orderTrackingWsDTO.setOrderDate(orderDetail.getCreated());
				}
				if (StringUtils.isNotEmpty(orderDetail.getCode()))
				{
					orderTrackingWsDTO.setOrderId(orderDetail.getCode());
				}
				//not required
				//orderTrackingWsDTO.setCancelflag(MarketplacecommerceservicesConstants.YES);
				if (StringUtils.isNotEmpty(orderDetail.getDeliveryAddress().getLastName())
						&& StringUtils.isNotEmpty(orderDetail.getDeliveryAddress().getFirstName()))
				{
					final String name = orderDetail.getDeliveryAddress().getFirstName().concat(" ")
							.concat(orderDetail.getDeliveryAddress().getLastName());
					orderTrackingWsDTO.setRecipientname(name);
				}
				if (null != orderDetail.getDeliveryCost() && StringUtils.isNotEmpty(orderDetail.getDeliveryCost().toString()))
				{
					orderTrackingWsDTO.setDeliveryCharge(orderDetail.getDeliveryCost().getValue().toString());
				}
				//TISST-13769
				//				if (null != orderDetail.getTotalPrice() && StringUtils.isNotEmpty(orderDetail.getTotalPrice().getValue().toString()))
				//				{
				//					orderTrackingWsDTO.setTotalOrderAmount(orderDetail.getTotalPrice().getValue().toString());
				//				}
				if (null != orderDetail.getTotalPriceWithConvCharge()
						&& StringUtils.isNotEmpty(orderDetail.getTotalPriceWithConvCharge().getValue().toString()))
				{
					orderTrackingWsDTO.setTotalOrderAmount(orderDetail.getTotalPriceWithConvCharge().getValue().toString());
				}
				//TISEE-4660 starts
				if (null != orderDetail.getConvenienceChargeForCOD()
						&& StringUtils.isNotEmpty(orderDetail.getConvenienceChargeForCOD().getValue().toString()))
				{
					orderTrackingWsDTO.setConvenienceCharge(orderDetail.getConvenienceChargeForCOD().getValue().toString());
				}
				if (null != orderDetail.getSubTotal() && StringUtils.isNotEmpty(orderDetail.getSubTotal().getValue().toString()))
				{
					orderTrackingWsDTO.setSubTotal(orderDetail.getSubTotal().getValue().toString());
				}
				//TISEE-4660 ends

				//TISST-13769
				if (null != orderDetail.getTotalDiscounts()
						&& StringUtils.isNotEmpty(orderDetail.getTotalDiscounts().getValue().toString()))
				{
					orderTrackingWsDTO.setTotalDiscount(orderDetail.getTotalDiscounts().getValue().toString());
				}
				if (null != orderDetail.getMplPaymentInfo())
				{
					if (null != orderDetail.getMplPaymentInfo().getPaymentOption())
					{

						final String paymentOption = orderDetail.getMplPaymentInfo().getPaymentOption();

						orderTrackingWsDTO.setPaymentMethod(paymentOption);

						if (paymentOption.equalsIgnoreCase(MarketplacecommerceservicesConstants.CREDIT)
								|| paymentOption.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI)
								|| paymentOption.equalsIgnoreCase(MarketplacecommerceservicesConstants.DEBIT))
						{

							orderTrackingWsDTO.setAccountHolderName(orderDetail.getMplPaymentInfo().getCardAccountHolderName());
							orderTrackingWsDTO.setPaymentCard(orderDetail.getMplPaymentInfo().getCardCardType());
							orderTrackingWsDTO.setPaymentCardDigit(orderDetail.getMplPaymentInfo().getCardIssueNumber());
							orderTrackingWsDTO.setPaymentCardExpire(orderDetail.getMplPaymentInfo().getCardExpirationMonth()
									+ MarketplacecommerceservicesConstants.FRONTSLASH
									+ orderDetail.getMplPaymentInfo().getCardExpirationYear());
						}
					}
				}

				final List<OrderData> subOrderList = orderDetail.getSellerOrderList();
				if (orderDetail.getSellerOrderList() != null && !orderDetail.getSellerOrderList().isEmpty())
				{
					for (final OrderData subOrder : subOrderList)
					{
						orderModel = orderModelService.getOrder(subOrder.getCode());
						for (final OrderEntryData entry : subOrder.getEntries())
						{
							List<String> parentTransactionIds = new ArrayList<>();
							final ProductData product = entry.getProduct();
							//getting the product code
							//							final ProductModel productModel = productService.getProductForCode(entry.getProduct().getCode());
							final ProductModel productModel = defaultMplOrderFacade.getProductForCode(entry.getProduct().getCode());


							if (null == product)
							{
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E9048);
							}
							else
							{
								orderproductdto = new OrderProductWsDTO();
								ordershipmentdetailstdtos = new ArrayList<Ordershipmentdetailstdto>();
								//								if (!entry.isGiveAway())
								//								{
								orderproductdto.setImageURL(setImageURL(product));
								if (StringUtils.isNotEmpty(entry.getAmountAfterAllDisc().toString()))
								{
									orderproductdto.setPrice(entry.getAmountAfterAllDisc().getValue().toString());
								}
								if (null != product.getBrand() && StringUtils.isNotEmpty(product.getBrand().toString()))
								{
									orderproductdto.setProductBrand(product.getBrand().getBrandname());
								}
								if (null != product.getCode() && StringUtils.isNotEmpty(product.getCode()))
								{
									orderproductdto.setProductcode(product.getCode());
								}
								if (StringUtils.isNotEmpty(product.getName()))
								{
									orderproductdto.setProductName(product.getName());
								}
								if (StringUtils.isNotEmpty(product.getSize()))
								{
									orderproductdto.setProductSize(product.getSize());
								}
								if (StringUtils.isNotEmpty(product.getVariantType()))
								{

									orderproductdto.setVariantOptions(product.getVariantType());
								}
								if (StringUtils.isNotEmpty(product.getColour()))
								{

									orderproductdto.setProductColour(product.getColour());
								}
								/* Fulfillment type */
								/*
								 * final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) productModel
								 * .getRichAttribute(); if (richAttributeModel != null &&
								 * richAttributeModel.get(0).getDeliveryFulfillModes() != null) { final String fullfillmentData
								 * = richAttributeModel.get(0).getDeliveryFulfillModes().getCode() .toUpperCase(); if
								 * (fullfillmentData != null && !fullfillmentData.isEmpty()) {
								 * orderproductdto.setFulfillment(fullfillmentData); } }
								 */
								if (entry.isGiveAway())
								{
									isGiveAway = "Y";
								}
								else
								{
									isGiveAway = "N";
								}
								orderproductdto.setIsGiveAway(isGiveAway);
								if (null != entry.getAssociatedItems())
								{
									orderproductdto.setAssociatedProducts(entry.getAssociatedItems());
								}
								//Delivery date is the final delivery date
								/*
								 * if (null != entry.getMplDeliveryMode()) {
								 *
								 * if (null != entry.getMplDeliveryMode().getDescription() &&
								 * StringUtils.isNotEmpty(entry.getMplDeliveryMode().getDescription())) {
								 *
								 * orderproductdto.setDeliveryDate(entry.getMplDeliveryMode().getDescription()); } }
								 */

								/* capacity */
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
											if (selectedCapacity.equals(pm.getCapacity()))
											{
												orderproductdto.setCapacity(pm.getCapacity());
											}

										}
									}
								}
								/*
								 * if (null != orderDetail.getSellerOrderList()) { for (final OrderData childOrder :
								 * orderDetail.getSellerOrderList()) {
								 */
								if (null != subOrder.getCode())
								{
									orderproductdto.setSellerorderno(subOrder.getCode());
								}
								//}

								/*
								 * if (null != orderproductdto.getUSSID()) {
								 *
								 * orderproductdto.setSerialno(orderproductdto.getUSSID()); } else {
								 * orderproductdto.setSerialno(MarketplacecommerceservicesConstants.NA); }
								 */

								//}
								SellerInformationModel sellerInfoModel = null;
								if (StringUtils.isNotEmpty(entry.getSelectedUssid()))
								{
									sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
								}
								if (sellerInfoModel != null
										&& sellerInfoModel.getRichAttribute() != null
										&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
								{
									/* Fulfillment type */
									final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
											.getDeliveryFulfillModes().getCode();
									if (StringUtils.isNotEmpty(fulfillmentType))
									{
										orderproductdto.setFulfillment(fulfillmentType);
									}
									//Seller info
									if (sellerInfoModel.getUSSID() != null
											&& sellerInfoModel.getUSSID().equalsIgnoreCase(entry.getSelectedUssid()))
									{
										if (null != sellerInfoModel.getSellerID())
										{
											orderproductdto.setSellerID(sellerInfoModel.getSellerID());
										}
										else
										{
											orderproductdto.setSellerID(MarketplacecommerceservicesConstants.NA);
										}

										if (null != sellerInfoModel.getSellerName())
										{
											orderproductdto.setSellerName(sellerInfoModel.getSellerName());
										}
										else
										{
											orderproductdto.setSellerName(MarketplacecommerceservicesConstants.NA);
										}

										if (null != sellerInfoModel.getUSSID())
										{
											orderproductdto.setUSSID(sellerInfoModel.getUSSID());
											//orderproductdto.setSerialno(orderproductdto.getUSSID());
										}
										else
										{
											orderproductdto.setUSSID(MarketplacecommerceservicesConstants.NA);
											//orderproductdto.setSerialno(MarketplacecommerceservicesConstants.NA);
										}
										for (final RichAttributeModel rm : sellerInfoModel.getRichAttribute())
										{
											if (!mplOrderFacade.isChildCancelleable(subOrder, entry.getTransactionId()))
											{
												orderproductdto.setCancel(Boolean.FALSE);
											}
											if (null == entry.getConsignment() && entry.getQuantity().doubleValue() != 0
													&& null != subOrder.getStatus())
											{
												consignmentStatus = subOrder.getStatus().getCode();
												//cancellation window not required
												/*
												 * if (null != rm.getCancellationWindow()) { final Date sysDate = new Date(); final
												 * int cancelWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
												 * subOrder.getCreated(), sysDate); final int actualCancelWindow =
												 * Integer.parseInt(rm.getCancellationWindow()); if (cancelWindow <
												 * actualCancelWindow && checkOrderStatus(subOrder.getStatus().getCode(),
												 * MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS).booleanValue() &&
												 * !entry.isGiveAway() && !entry.isIsBOGOapplied()) {
												 * orderproductdto.setCancel(Boolean.TRUE);
												 *
												 * } else { orderproductdto.setCancel(Boolean.FALSE); } } else {
												 * orderproductdto.setCancel(Boolean.FALSE); }
												 */
												if (checkOrderStatus(subOrder.getStatus().getCode(),
														MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS).booleanValue()
														&& !entry.isGiveAway() && !entry.isIsBOGOapplied())
												{
													orderproductdto.setCancel(Boolean.TRUE);

												}
												else
												{
													orderproductdto.setCancel(Boolean.FALSE);
												}
											}
											else if (null != entry.getConsignment() && null != entry.getConsignment().getStatus())
											{
												consignmentStatus = entry.getConsignment().getStatus().getCode();
												//cancellation window not required
												/*
												 * if (null != rm.getCancellationWindow()) { final Date sysDate = new Date(); final
												 * int cancelWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
												 * subOrder.getCreated(), sysDate); final int actualCancelWindow =
												 * Integer.parseInt(rm.getCancellationWindow()); if (cancelWindow <
												 * actualCancelWindow && checkOrderStatus(consignmentStatus,
												 * MarketplacecommerceservicesConstants.CANCEL_STATUS).booleanValue() &&
												 * !entry.isGiveAway() && !entry.isIsBOGOapplied())
												 *
												 * { orderproductdto.setCancel(Boolean.TRUE);
												 *
												 * } else { orderproductdto.setCancel(Boolean.FALSE); } } else {
												 * orderproductdto.setCancel(Boolean.FALSE); }
												 */
												if (checkOrderStatus(consignmentStatus, MarketplacecommerceservicesConstants.CANCEL_STATUS)
														.booleanValue() && !entry.isGiveAway() && !entry.isIsBOGOapplied())

												{
													orderproductdto.setCancel(Boolean.TRUE);
												}
												else
												{
													orderproductdto.setCancel(Boolean.FALSE);
												}
											}
											else
											{
												orderproductdto.setCancel(Boolean.FALSE);
											}

											if (null != rm.getExchangeAllowedWindow())
											{
												orderproductdto.setExchangePolicy(rm.getExchangeAllowedWindow());

											}
											/*
											 * if (null != sellerEntry.getReplacement()) {
											 * orderproductdto.setReplacement(sellerEntry.getReplacement());
											 *
											 * }
											 */
											//for return
											if (null != entry.getConsignment() && null != entry.getConsignment().getStatus())
											{
												consignmentStatus = entry.getConsignment().getStatus().getCode();
												consignmentModel = mplOrderService.fetchConsignment(entry.getConsignment().getCode());

												if (null != consignmentModel && rm.getReturnWindow() != null)
												{
													final Date sDate = new Date();
													final int returnWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
															consignmentModel.getDeliveryDate(), sDate);
													final int actualReturnWindow = Integer.parseInt(rm.getReturnWindow());
													if (!entry.isGiveAway()
															&& !entry.isIsBOGOapplied()
															&& returnWindow < actualReturnWindow
															&& !checkOrderStatus(consignmentStatus,
																	MarketplacecommerceservicesConstants.VALID_RETURN).booleanValue()
															&& consignmentStatus
																	.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED))

													{
														//orderproductdto.setReturnPolicy(sellerEntry.getReturnPolicy());
														orderproductdto.setIsReturned(Boolean.TRUE);
													}
													else
													{
														orderproductdto.setIsReturned(Boolean.FALSE);
													}
												}
												else
												{
													orderproductdto.setIsReturned(Boolean.FALSE);
												}
												// set window
												if (rm.getReturnWindow() != null)
												{
													orderproductdto.setReturnPolicy(rm.getReturnWindow());
												}
											}
											else
											{
												orderproductdto.setIsReturned(Boolean.FALSE);
											}
										}
									}
								}

								// display order tracking messages
								if (null != consignmentModel)
								{
									formattedProductDate = GenericUtilityMethods.getFormattedDate(consignmentModel.getEstimatedDelivery());
									formattedActualProductDate = GenericUtilityMethods
											.getFormattedDate(consignmentModel.getDeliveryDate());
									if (null != consignmentModel.getTrackingID())
									{
										orderproductdto.setTrackingAWB(consignmentModel.getTrackingID());
									}
									if (null != consignmentModel.getReturnAWBNum())
									{
										orderproductdto.setReturnAWB(consignmentModel.getReturnAWBNum());
									}
									if (null != consignmentModel.getCarrier())
									{
										orderproductdto.setLogisticName(consignmentModel.getCarrier());
									}
									if (null != consignmentModel.getReturnCarrier())
									{
										orderproductdto.setReverseLogisticName(consignmentModel.getReturnCarrier());
									}
								}

								//Showing Delivery date for freebies and give aways TISEE-5520
								/*
								 * if (null != entry.getConsignment() && (entry.isGiveAway() || entry.isIsBOGOapplied())) {
								 * consignmentModel = mplOrderService.fetchConsignment(entry.getConsignment().getCode());
								 * formattedProductDate =
								 * GenericUtilityMethods.getFormattedDate(consignmentModel.getEstimatedDelivery());
								 * formattedActualProductDate = GenericUtilityMethods
								 * .getFormattedDate(consignmentModel.getDeliveryDate()); if (null !=
								 * consignmentModel.getTrackingID()) {
								 * orderproductdto.setTrackingAWB(consignmentModel.getTrackingID()); } if (null !=
								 * consignmentModel.getReturnAWBNum()) {
								 * orderproductdto.setReturnAWB(consignmentModel.getReturnAWBNum()); } if (null !=
								 * consignmentModel.getCarrier()) {
								 * orderproductdto.setLogisticName(consignmentModel.getCarrier()); } if (null !=
								 * consignmentModel.getReturnCarrier()) {
								 * orderproductdto.setReverseLogisticName(consignmentModel.getReturnCarrier()); }
								 *
								 * }
								 */
								//End
								final Map<String, List<AWBResponseData>> returnMap = getOrderDetailsFacade.getOrderStatusTrack(entry,
										subOrder, orderDetail);
								orderproductdto.setStatusDisplayMsg(setStatusDisplayMessage(returnMap, consignmentModel));
								//setting current product status Display
								if ((consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_INITIATED) || consignmentStatus
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_IN_PROGRESS))
										&& returnMap.get(MarketplaceFacadesConstants.CANCEL) != null
										&& returnMap.get(MarketplaceFacadesConstants.CANCEL).size() > 0)
								{
									orderproductdto.setStatusDisplay(MarketplaceFacadesConstants.CANCEL);
								}
								else if ((consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_INITIATED) || consignmentStatus
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_IN_PROGRESS))
										&& returnMap.get(MarketplaceFacadesConstants.RETURN) != null
										&& returnMap.get(MarketplaceFacadesConstants.RETURN).size() > 0)
								{
									orderproductdto.setStatusDisplay(MarketplaceFacadesConstants.RETURN);
								}
								else
								{
									customerStatusModel = orderModelService.getOrderStausCodeMaster(consignmentStatus);
									if (customerStatusModel != null)
									{
										orderproductdto.setStatusDisplay(customerStatusModel.getStage());
									}
									else
									{
										orderproductdto.setStatusDisplay(MarketplacecommerceservicesConstants.NA);
									}
								}
								//set Serial no
								if (null != entry.getImeiDetails() && null != entry.getImeiDetails().getSerialNum())
								{
									orderproductdto.setSerialno(entry.getImeiDetails().getSerialNum());
								}
								else
								{
									orderproductdto.setSerialno(MarketplacecommerceservicesConstants.EMPTY);
								}
								//Set the transaction id
								if (entry.getTransactionId() != null)
								{
									orderproductdto.setTransactionId(entry.getTransactionId());
								}
								else
								{
									orderproductdto.setTransactionId(MarketplacecommerceservicesConstants.EMPTY);
								}
								//Setting parent transaction ID
								for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
								{
									if (entry.getTransactionId().equalsIgnoreCase(orderEntry.getTransactionID())
											&& null != orderEntry.getParentTransactionID())
									{
										parentTransactionIds = Arrays.asList(orderEntry.getParentTransactionID().split("\\s*,\\s*"));
										break;
									}
								}
								orderproductdto.setParentTransactionId(parentTransactionIds);
								//Check if invoice is available
								if (entry.getConsignment() != null)
								{
									if (entry.getConsignment().getStatus() != null
											&& (entry.getConsignment().getStatus().equals(ConsignmentStatus.HOTC)
													|| entry.getConsignment().getStatus().equals(ConsignmentStatus.OUT_FOR_DELIVERY)
													|| entry.getConsignment().getStatus().equals(ConsignmentStatus.REACHED_NEAREST_HUB) || entry
													.getConsignment().getStatus().equals(ConsignmentStatus.DELIVERED)))
									{
										orderproductdto.setIsInvoiceAvailable(Boolean.TRUE);
									}
									else
									{
										orderproductdto.setIsInvoiceAvailable(Boolean.FALSE);
									}
								}
								else
								{
									orderproductdto.setIsInvoiceAvailable(Boolean.FALSE);
								}
								//End
								//estimated delivery date
								if (!formattedProductDate.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMPTY))
								{
									orderproductdto.setEstimateddeliverydate(formattedProductDate);
								}
								//delivery date
								if (!formattedActualProductDate.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMPTY))
								{
									orderproductdto.setDeliveryDate(formattedActualProductDate);
								}

								if (null != orderDetail.getConsignments())
								{
									for (final ConsignmentData shipdetails : orderDetail.getConsignments())
									{
										final Ordershipmentdetailstdto ordershipmentdetailstdto = new Ordershipmentdetailstdto();
										if (null != shipdetails.getStatus() && StringUtils.isNotEmpty(shipdetails.getStatus().toString()))
										{
											ordershipmentdetailstdto.setStatus(shipdetails.getStatus().toString());
										}

										if (null != shipdetails.getStatusDate())
										{
											ordershipmentdetailstdto.setStatusDate(shipdetails.getStatusDate());
										}

										ordershipmentdetailstdtos.add(ordershipmentdetailstdto);
									}
									if (ordershipmentdetailstdtos.size() > 0)
									{
										orderproductdto.setShipmentdetails(ordershipmentdetailstdtos.get(0));
									}
									else
									{
										final Ordershipmentdetailstdto ordershipmentdetailstdto1 = new Ordershipmentdetailstdto();
										ordershipmentdetailstdto1.setStatus(MarketplacecommerceservicesConstants.NA);
										ordershipmentdetailstdto1.setStatusDate(new Date());
										ordershipmentdetailstdtos.add(ordershipmentdetailstdto1);
										orderproductdto.setShipmentdetails(ordershipmentdetailstdtos.get(0));
									}
								}
								orderproductdtos.add(orderproductdto);
							}
						}
					}
					orderTrackingWsDTO.setProducts(orderproductdtos);
					orderTrackingWsDTO.setStatusDisplay(orderDetail.getStatusDisplay());
					orderTrackingWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
			}
		}
		catch (

		final EtailNonBusinessExceptions e)

		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				orderTrackingWsDTO.setError(e.getErrorMessage());
			}
			orderTrackingWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (

		final EtailBusinessExceptions e)

		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				orderTrackingWsDTO.setError(e.getErrorMessage());
			}
			orderTrackingWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		catch (

		final Exception e)

		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return orderTrackingWsDTO;

	}

	/**
	 * @param statusResponse
	 * @param consignment
	 * @param awbEnabled
	 * @param reverseawbEnabled
	 * @return statusMessages
	 */
	public List<StatusResponseDTO> setStatusResponse(final List<AWBResponseData> statusResponse,
			final ConsignmentModel consignment, final boolean awbEnabled, final boolean reverseawbEnabled)
	{
		final List<StatusResponseDTO> statusMessages = new ArrayList<StatusResponseDTO>();
		List<StatusResponseMessageDTO> statusMessageList = null;
		AWBResponseData responseData = null;
		for (final AWBResponseData resp : statusResponse)
		{
			final StatusResponseDTO statusMessage = new StatusResponseDTO();
			statusMessageList = new ArrayList<StatusResponseMessageDTO>();
			statusMessage.setCurrentFlag(true);
			statusMessage.setResponseCode(resp.getResponseCode());
			statusMessage.setShipmentStatus(resp.getShipmentStatus());
			if (resp.getStatusRecords().size() > 0)
			{
				for (final StatusRecordData statusResp : resp.getStatusRecords())
				{
					final StatusResponseMessageDTO statusRespMessage = new StatusResponseMessageDTO();
					statusRespMessage.setDate(statusResp.getDate());
					statusRespMessage.setTime(statusResp.getTime());
					statusRespMessage.setLocation(statusResp.getLocation());
					statusRespMessage.setStatusDescription(statusResp.getStatusDescription());
					statusMessageList.add(statusRespMessage);
				}
			}
			if (null != consignment && null != consignment.getTrackingID() && null != consignment.getCarrier() && awbEnabled)
			{
				responseData = mplOrderService.prepAwbStatus(consignment.getTrackingID(), consignment.getCarrier());
				for (final StatusRecordData statusResp : responseData.getStatusRecords())
				{
					final StatusResponseMessageDTO statusRespMessage = new StatusResponseMessageDTO();
					statusRespMessage.setDate(statusResp.getDate());
					statusRespMessage.setTime(statusResp.getTime());
					statusRespMessage.setLocation(statusResp.getLocation());
					statusRespMessage.setStatusDescription(statusResp.getStatusDescription());
					statusMessageList.add(statusRespMessage);
				}
			}
			if (null != consignment && null != consignment.getReturnAWBNum() && null != consignment.getReturnCarrier()
					&& reverseawbEnabled)
			{
				responseData = mplOrderService.prepAwbStatus(consignment.getReturnAWBNum(), consignment.getReturnCarrier());
				for (final StatusRecordData statusResp : responseData.getStatusRecords())
				{
					final StatusResponseMessageDTO statusRespMessage = new StatusResponseMessageDTO();
					statusRespMessage.setDate(statusResp.getDate());
					statusRespMessage.setTime(statusResp.getTime());
					statusRespMessage.setLocation(statusResp.getLocation());
					statusRespMessage.setStatusDescription(statusResp.getStatusDescription());
					statusMessageList.add(statusRespMessage);
				}
			}
			statusMessage.setStatusMessageList(statusMessageList);
			statusMessages.add(statusMessage);

		}
		return statusMessages;
	}

	/**
	 * @Description set Mobile end tracking message
	 * @param returnMap
	 * @param consignment
	 * @return responseList
	 */
	public Map<String, StatusResponseListDTO> setStatusDisplayMessage(final Map<String, List<AWBResponseData>> returnMap,
			final ConsignmentModel consignment)

	{
		StatusResponseListDTO responseList = new StatusResponseListDTO();
		final Map<String, StatusResponseListDTO> displayMsg = new HashMap<>();
		List<StatusResponseDTO> statusMessages = null;
		List<AWBResponseData> statusResponse = null;
		try
		{
			//final boolean flag = false;
			if (returnMap.get(MarketplaceFacadesConstants.APPROVED) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.APPROVED);
				statusMessages = setStatusResponse(statusResponse, consignment, false, false);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.APPROVED, responseList);
				}
			}
			if (returnMap.get(MarketplaceFacadesConstants.PROCESSING) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.PROCESSING);
				statusMessages = setStatusResponse(statusResponse, consignment, false, false);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.PROCESSING, responseList);
				}
			}
			if (returnMap.get(MarketplaceFacadesConstants.SHIPPING) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.SHIPPING);
				statusMessages = setStatusResponse(statusResponse, consignment, true, false);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.SHIPPING, responseList);
				}
			}
			if (returnMap.get(MarketplaceFacadesConstants.CANCEL) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.CANCEL);
				statusMessages = setStatusResponse(statusResponse, consignment, false, false);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.CANCEL, responseList);
				}
			}
			if (returnMap.get(MarketplaceFacadesConstants.RETURN) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.RETURN);
				statusMessages = setStatusResponse(statusResponse, consignment, false, true);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.RETURN, responseList);
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.debug("----------AWB Serivce Error-----------------");
			return displayMsg;
		}
		catch (final Exception e)
		{
			LOG.debug("----------Conversion Error-----------------");
			return displayMsg;
		}

		return displayMsg;
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
		return getOrderHistoryListWsDTO;
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
