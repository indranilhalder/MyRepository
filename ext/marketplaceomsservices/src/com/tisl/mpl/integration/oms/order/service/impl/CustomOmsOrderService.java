package com.tisl.mpl.integration.oms.order.service.impl;

//import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.integration.commons.hystrix.OndemandHystrixCommandConfiguration;
import de.hybris.platform.integration.commons.hystrix.OndemandHystrixCommandFactory;
import de.hybris.platform.integration.oms.order.data.OrderPlacementResult;
import de.hybris.platform.integration.oms.order.service.impl.DefaultOmsOrderService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.util.localization.Localization;

import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.commons.client.RestCallException;
import com.hybris.oms.api.changedeliveryaddress.ChangeDeliveryAddressFacade;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressDto;
import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressResponseDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionEddDto;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.RestClientException;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.UpdatedSinceList;
import com.hybris.oms.domain.pickupinfo.PickupInfo;
import com.hybris.oms.picupinfo.facade.PickupInfoFacade;
import com.sun.jersey.api.client.ClientHandlerException;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.samsung.wsdto.ChargeWsDTO;
import com.tisl.mpl.samsung.wsdto.DeliveryModeWsDTO;
import com.tisl.mpl.samsung.wsdto.OrderListWsDTO;
import com.tisl.mpl.samsung.wsdto.OrderResponseWsDTO;
import com.tisl.mpl.samsung.wsdto.ProductWsDTO;
import com.tisl.mpl.service.MplCustomerWebService;
import com.tisl.mpl.service.MplSendOrderFromCommerceToCRM;
import com.tisl.mpl.service.MplSendOrderFromCommerceToSamsung;
import com.tisl.mpl.service.OrderWebService;



public class CustomOmsOrderService extends DefaultOmsOrderService implements MplOmsOrderService
{
	private static final Logger LOG = Logger.getLogger(CustomOmsOrderService.class);
	private OndemandHystrixCommandConfiguration hystrixCommandConfig;
	private Converter<OrderModel, Order> orderConverter;
	private OrderFacade orderRestClient;
	private TicketBusinessService ticketBusinessService;
	private ModelService modelService;
	private OndemandHystrixCommandFactory ondemandHystrixCommandFactory;
	private MplSendOrderFromCommerceToCRM ordercreation;
	private MplCustomerWebService mplCustomerWebService;
	@Autowired
	private PickupInfoFacade pickupInfoRestClient;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "orderWebService")
	OrderWebService orderWebService;

	@Autowired
	private ChangeDeliveryAddressFacade changeDeliveryAddressFacade;

	//SONAR FIX
	//@Resource(name = "accProductFacade")
	//private ProductFacade productFacade;

	@Resource(name = "mplCommerceCartService")
	private MplCommerceCartService mplCommerceCartService;

	//SONAR FIX
	//@Resource(name = "mplCategoryServiceImpl")
	//private MplCategoryService mplCategoryService;

	@Resource(name = "mplSellerInformationService")
	private MplSellerInformationService mplSellerInformationService;

	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;

	@Resource(name = "mplSendOrderFromCommerceToSamsung")
	private MplSendOrderFromCommerceToSamsung mplSendOrderFromCommerceToSamsung;

	//SONAR FIX
	/*
	 * private static final String COLON = ":"; private final int connectionTimeout = 5 * 10000; private final int
	 * readTimeout = 5 * 1000;
	 */

	@Override
	public OrderPlacementResult createCrmOrder(final OrderModel orderModel)
	{
		//orderModel.setExportedToCrmRetryCount(Integer.valueOf(orderModel.getExportedToCrmRetryCount().intValue() + 1));
		//getModelService().save(orderModel);
		OrderPlacementResult result = null;
		Order order = null;
		try
		{
			order = getOrderConverter().convert(orderModel);
			LOG.debug("Before CRM order call for : " + order.getOrderId());
			// SprintPaymentFixes:- To avoid 26 digit Malformed Ordert ID processing
			if (!order.getOrderId().contains("-"))
			{
				getOrdercreation().orderCreationDataToCRM(order);
				LOG.debug("After CRM order call for : " + order.getOrderId());
				if (orderModel.getUser() != null && null != orderModel.getUser().getUid())
				{
					LOG.debug("Customer update after order place for Order : " + order.getOrderId() + " and Customer"
							+ orderModel.getUser().getUid());
					getMplCustomerWebService().customerModeltoWsData((CustomerModel) orderModel.getUser(), "U", false);
					LOG.debug("Customer update success");
				}
				result = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
			}
			else
			{
				LOG.error("CreateOmsOrder -- Wrong Order Processed to CRM Order ID:- " + order.getOrderId());
				result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, new Exception("Malformed OrderId to CRM:: "
						+ order.getOrderId()));
			}
		}
		catch (final Exception ex)
		{
			LOG.error("CreateOmsOrder -- Exception occured while placing order due to  ", ex);
			result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, ex);
		}
		return result;
	}

	@Override
	public OrderPlacementResult createOmsOrder(final OrderModel orderModel)
	{
		OrderPlacementResult result = null;
		Order order = null;
		String httpErrorCode = MarketplacecommerceservicesConstants.EMPTY;

		//Exceptions declared in properties file
		boolean errorFlag = false;
		String[] exceptions = null;

		try
		{
			exceptions = getOmsException();//getting Fallback exceptions

			LOG.info("Fallback Exceptions Are**::" + exceptions);

			httpErrorCode = getConfigurationService().getConfiguration()
					.getString(MarketplacecclientservicesConstants.OMS_HTTP_ERROR_CODE, "404,503").trim();
			order = getOrderConverter().convert(orderModel);
			// SprintPaymentFixes:- To avoid 26 digit Malformed Ordert ID processing
			if (!order.getOrderId().contains("-"))
			{
				//Order request xml and response xml changes made for Audit purpose
				final String requestXml = getOrderAuditXml(order);
				if (StringUtils.isNotEmpty(requestXml))
				{
					orderModel.setRequestXML(requestXml);
				}
				else
				{
					LOG.debug("createOmsOrder requestXml is null or empty ");
				}
				getModelService().save(orderModel);

				//final Order orderResponse = getOrderRestClient().createOrder(order);
				final Order orderResponse = orderWebService.createOmsOrder(order);
				final String responseXml = getOrderAuditXml(orderResponse);

				if (StringUtils.isNotEmpty(responseXml))
				{
					orderModel.setResponseXML(responseXml);
				}
				else
				{
					LOG.debug("createOmsOrder responseXml is null or empty ");
				}
				//PaymentFix2017
				orderModel.setIsSentToOMS(Boolean.TRUE);
				getModelService().save(orderModel);
				result = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
			}
			else
			{
				LOG.error("CreateOmsOrder -- Wrong Order Processed to OMS Order ID:- " + order.getOrderId());
				result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, new Exception("Malformed OrderId to OMS:: "
						+ order.getOrderId()));

			}
		}
		catch (final DuplicateEntityException dee)
		{
			LOG.error("DuplicateEntityException occured due to order already created for OrderID : " + order.getOrderId(), dee);
			//PaymentFix2017
			orderModel.setIsSentToOMS(Boolean.TRUE);
			getModelService().save(orderModel);
			result = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
		}
		catch (final RestClientException rce)
		{
			LOG.error("RestClientException occured while creating order : " + order.getOrderId(), rce);
			result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, rce);

		}
		catch (final ClientHandlerException cex)
		{
			LOG.error("ClientHandlerException occured while creating order : " + order.getOrderId(), cex);
			result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, cex);
			if (cex.getCause() instanceof SocketTimeoutException)
			{
				LOG.error("SocketTimeoutException occured while creating order :" + order.getOrderId(), cex);
				result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, cex);

			}
		}
		catch (final RestCallException ex)
		{
			LOG.error("RestCallException occured while creating order", ex);

			for (final String exception : exceptions)
			{
				if (ex.getMessage().contains(exception))
				{
					errorFlag = true;
					break;
				}
			}
			if ((ex.getResponse() != null && ex.getResponse().getStatus() != null && (httpErrorCode.contains(String.valueOf(ex
					.getResponse().getStatus().getStatusCode()))))
					|| errorFlag)
			{
				result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, ex);
			}
		}
		catch (final EntityValidationException evx)
		{
			LOG.error("EntityValidationException>> Exception occured while placing order : ", evx);
		}
		catch (final Exception ex)
		{
			LOG.error("CreateOmsOrder >> Exception occured while placing order due to ", ex);
			LOG.error("***CreateOmsOrder >> Error Is***" + ex.getMessage());
			result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, ex);
		}
		if (OrderPlacementResult.Status.SUCCESS.equals(result.getResult()))
		{
			orderModel.setOrderExportTime(new Date());
			getModelService().save(orderModel);
		}

		return result;

	}

	/* For TPR-5667 */
	public OrderPlacementResult createSamsungOrder(final OrderModel orderModel)
	{
		final OrderPlacementResult result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED);
		try
		{
			LOG.debug(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD + "Before Samsung order call for : "
					+ orderModel.getCode());
			final OrderResponseWsDTO orderResponse = getOrderResponse(orderModel);
			final JSONObject samsungResponse = mplSendOrderFromCommerceToSamsung.postResponseToSamsung(orderResponse, orderModel);
			final String responseMessage = samsungResponse.get(MarketplaceomsservicesConstants.MESSAGE).toString();
			if (responseMessage.equalsIgnoreCase(MarketplaceomsservicesConstants.SUCCESS))
			{
				result.setResult(OrderPlacementResult.Status.SUCCESS);
			}
		}
		catch (final ClientEtailNonBusinessExceptions cenbex)
		{
			LOG.error(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD
					+ "ClientEtailNonBusinessExceptions occured while creating samsung order  ", cenbex);
			result.setCause(cenbex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.SAMSUNG_LOGGER_HEAD + "Exception occured while creating samsung order  ",
					ex);
			result.setCause(ex);
		}

		return result;

	}


	//	@Override
	//	public OrderPlacementResult createOmsOrder(final OrderModel orderModel)
	//	{
	//		OrderPlacementResult result = null;
	//		Order order = null;
	//		try
	//		{
	//			order = getOrderConverter().convert(orderModel);
	//			//Order request xml and response xml changes made for Audit purpose
	//			final String requestXml = getOrderAuditXml(order);
	//			if (StringUtils.isNotEmpty(requestXml))
	//			{
	//				orderModel.setRequestXML(requestXml);
	//			}
	//			else
	//			{
	//				LOG.debug("createOmsOrder requestXml is null or empty ");
	//			}
	//			getModelService().save(orderModel);
	//
	//			final Order orderResponse = getOrderRestClient().createOrder(order);
	//			final String responseXml = getOrderAuditXml(orderResponse);
	//
	//			if (StringUtils.isNotEmpty(responseXml))
	//			{
	//				orderModel.setResponseXML(responseXml);
	//			}
	//			else
	//			{
	//				LOG.debug("createOmsOrder responseXml is null or empty ");
	//			}
	//			getModelService().save(orderModel);
	//			result = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
	//
	//		}
	//		catch (final RestCallException e)
	//		{
	//			if ((e.getResponse() != null) && (Response.Status.SERVICE_UNAVAILABLE.equals(e.getResponse().getStatus())))
	//			{
	//				result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, e);
	//			}
	//		}
	//		catch (final Exception ex)
	//		{
	//			LOG.error("CreateOmsOrder >> Exception occured while placing order due to ", ex);
	//			result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, ex);
	//		}
	//		if (OrderPlacementResult.Status.SUCCESS.equals(result.getResult()))
	//		{
	//			orderModel.setOrderExportTime(new Date());
	//			getModelService().save(orderModel);
	//		}
	//
	//		return result;
	//
	//	}



	/**
	 * @param orderModel
	 * @return orderResponseWsDTO
	 */
	private OrderResponseWsDTO getOrderResponse(final OrderModel orderModel)
	{
		final OrderResponseWsDTO orderResponseWsDTO = new OrderResponseWsDTO();
		final List<OrderListWsDTO> orderListWsDTO = new ArrayList<OrderListWsDTO>();
		orderResponseWsDTO.setORN(orderModel.getCode());
		orderResponseWsDTO.setPlacedDate(orderModel.getDate().toString());
		if (orderModel.getChildOrders() != null)
		{
			for (final OrderModel childOrder : orderModel.getChildOrders())
			{
				orderListWsDTO.add(getChildOrderResponse(childOrder));
			}
			orderResponseWsDTO.setOrder(orderListWsDTO);
		}

		return orderResponseWsDTO;
	}

	/**
	 * @param childOrder
	 * @return orderListWsDTO
	 */
	private OrderListWsDTO getChildOrderResponse(final OrderModel childOrder)
	{
		final OrderListWsDTO orderListWsDTO = new OrderListWsDTO();
		final List<ProductWsDTO> productListWsDTO = new ArrayList<ProductWsDTO>();
		orderListWsDTO.setOrderId(childOrder.getCode());
		int freeBuyProduct = 0;
		int totalEntries = 0;
		final double convenienceChargeForOrder = childOrder.getConvenienceCharges().doubleValue();
		double convenienceChargeForActiveProduct = 0;
		// Loop for finding no. of freeBuy products and calculate convenience charge
		if (CollectionUtils.isNotEmpty(childOrder.getEntries()) && convenienceChargeForOrder > 0)
		{
			totalEntries = childOrder.getEntries().size();
			for (final AbstractOrderEntryModel entry : childOrder.getEntries())
			{
				if (entry.getGiveAway().booleanValue())
				{
					freeBuyProduct++;
				}
			}
			final int totalActiveProducts = (totalEntries - freeBuyProduct);
			if (totalActiveProducts > 0)
			{
				convenienceChargeForActiveProduct = (convenienceChargeForOrder / totalActiveProducts);
			}
		}

		// Data assignment
		if (CollectionUtils.isNotEmpty(childOrder.getEntries()))
		{
			for (final AbstractOrderEntryModel entry : childOrder.getEntries())
			{
				final ProductWsDTO productWsDTO = getProductResponse(entry);
				//Charges
				if (StringUtils.isNotEmpty(childOrder.getModeOfOrderPayment()))
				{
					final ChargeWsDTO charges = new ChargeWsDTO();
					charges.setCharges(childOrder.getModeOfOrderPayment());
					if (entry.getGiveAway().booleanValue())
					{
						charges.setCost(Double.valueOf(0));
					}
					else
					{
						charges.setCost(Double.valueOf(convenienceChargeForActiveProduct));
					}
					if (productWsDTO != null)
					{
						productWsDTO.setCharges(charges);
					}
				}
				productListWsDTO.add(productWsDTO);
			}
			orderListWsDTO.setProducts(productListWsDTO);

		}
		orderListWsDTO.setStatus(childOrder.getStatus().toString());
		orderListWsDTO.setSubTotal(childOrder.getSubtotal());
		return orderListWsDTO;
	}

	/**
	 * @param entry
	 * @return productListWsDTO
	 */
	private ProductWsDTO getProductResponse(final AbstractOrderEntryModel entry)
	{
		final ProductWsDTO productListWsDTO = new ProductWsDTO();
		final ProductModel product = entry.getProduct();
		// USSID
		productListWsDTO.setUSSID(entry.getSelectedUSSID());
		// Price
		productListWsDTO.setPrice(entry.getBasePrice());
		//PromoDiscount
		productListWsDTO.setPromoDiscount(Double.valueOf(entry.getCartLevelDisc().doubleValue()
				+ entry.getTotalProductLevelDisc().doubleValue()));
		//CouponDIscoun
		productListWsDTO.setCouponDiscoun(entry.getCouponValue());
		//ApportionedPrice
		productListWsDTO.setApportionedPrice(entry.getNetAmountAfterAllDisc());
		//productBrand
		if (product.getBrands() != null)
		{
			for (final BrandModel brand : product.getBrands())
			{
				productListWsDTO.setProductBrand(brand.getName());
				break;
			}
		}

		//product Category code
		//Setting the version of sessioncatalog
		catalogVersionService.setSessionCatalogVersion(MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);

		final Collection<CategoryModel> categoryList = product.getSupercategories();
		if (categoryList != null)
		{
			for (final CategoryModel categoy : categoryList)
			{
				if (StringUtils.startsWith(categoy.getCode(), MarketplacecommerceservicesConstants.MSH)
						|| StringUtils.startsWith(categoy.getCode(), MarketplacecommerceservicesConstants.LSH))
				{
					productListWsDTO.setProductCategory(categoy.getCode());
					break;
				}
			}
		}
		//Description
		productListWsDTO.setProductDescription(product.getArticleDescription());
		// Name
		productListWsDTO.setProductName(product.getName());
		// Code
		productListWsDTO.setListingId(product.getCode());
		// Quantity
		productListWsDTO.setQuantity(Integer.valueOf(entry.getQuantity().intValue()));
		//rootcategory
		productListWsDTO.setRootCategory(product.getProductCategoryType());
		//Delivery mode
		if (entry.getMplDeliveryMode() != null && entry.getMplDeliveryMode().getDeliveryMode() != null)
		{
			final DeliveryModeWsDTO deliveryModeWsDTO = new DeliveryModeWsDTO();
			deliveryModeWsDTO.setCode(entry.getMplDeliveryMode().getDeliveryMode().getCode());
			deliveryModeWsDTO.setName(entry.getMplDeliveryMode().getDeliveryMode().getName());
			// delivery mode desc
			final String deliveryStart = entry.getMplDeliveryMode().getDeliveryMode().getStart() != null ? entry
					.getMplDeliveryMode().getDeliveryMode().getStart().toString()
					: MarketplacecommerceservicesConstants.DEFAULT_START_TIME;
			final String deliveryEnd = entry.getMplDeliveryMode().getDeliveryMode().getEnd() != null ? entry.getMplDeliveryMode()
					.getDeliveryMode().getEnd().toString() : MarketplacecommerceservicesConstants.DEFAULT_END_TIME;
			final String deliveryDesc = mplCommerceCartService.getDeliveryModeDescription(entry.getSelectedUSSID(), entry
					.getMplDeliveryMode().getDeliveryMode().getCode(), deliveryStart, deliveryEnd);
			deliveryModeWsDTO.setDesc(deliveryDesc);
			// Delivery cost
			if (entry.getCurrDelCharge() != null)
			{
				deliveryModeWsDTO.setDeliveryCost(entry.getCurrDelCharge());
			}
			productListWsDTO.setSelectedDeliveryMode(deliveryModeWsDTO);
		}
		//sellerID
		final String sellerID = mplSellerInformationService.getSellerIdByUssid(entry.getSelectedUSSID());
		productListWsDTO.setSellerID(sellerID);
		//sellerName
		productListWsDTO.setSellerName(entry.getSellerInfo());

		return productListWsDTO;
	}

	@Override
	public UpdatedSinceList<String> getUpdatedOrderIds(final Date updatedSince)
	{
		UpdatedSinceList<String> listofOrders = null;
		final Date newSinceDate = new Date(updatedSince.getTime() - 180000L);
		LOG.warn("time offset added: " + updatedSince + " -> " + newSinceDate);
		listofOrders = getOrderRestClient().getOrderIdsUpdatedAfter(updatedSince);
		return listofOrders;
	}

	@Override
	public void flagTheOrderAsFailed(final OrderModel orderModel, final Throwable cause)
	{
		final String ticketTitle = Localization.getLocalizedString("message.ticket.ordernotsent.title");
		final String ticketMessage = Localization.getLocalizedString("message.ticket.ordernotsent.content", new Object[]
		{ orderModel.getCode(), cause.getLocalizedMessage() });
		createTicket(ticketTitle, ticketMessage, orderModel, CsTicketCategory.PROBLEM, CsTicketPriority.HIGH);
	}

	@Override
	public Order getOrderByOrderId(final String orderId)
	{
		Order order = null;
		order = getOrderRestClient().getOrderByOrderId(orderId); //Open the commented line before Push
		return order;
	}

	@Override
	protected CsTicketModel createTicket(final String subject, final String description, final OrderModel orderModel,
			final CsTicketCategory category, final CsTicketPriority priority)
	{
		final CsTicketModel newTicket = (CsTicketModel) getModelService().create(CsTicketModel.class);
		newTicket.setHeadline(subject);
		newTicket.setCategory(category);
		newTicket.setPriority(priority);
		newTicket.setOrder(orderModel);
		newTicket.setCustomer(orderModel.getUser());

		final CsCustomerEventModel newTicketEvent = new CsCustomerEventModel();
		newTicketEvent.setText(description);

		return getTicketBusinessService().createTicket(newTicket, newTicketEvent);
	}


	public void createCRMTicket(final OrderModel orderModel)
	{
		final Order order = returnOrder(orderModel);
		try
		{
			getOrdercreation().orderCreationDataToCRM(order);
			LOG.info(" CRM order call for RMS verfication pending");
			orderModel.setCrmSubmitStatus(MarketplaceomsservicesConstants.SUCCESS);
			getModelService().save(orderModel);
			LOG.debug("After CRM order call for Ticket for order :" + order.getOrderId());
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex.getMessage());
		}
	}


	private Order returnOrder(final OrderModel orderModel)
	{
		final Order order = getOrderConverter().convert(orderModel);
		return order;
	}

	/*
	 * @Desc Used for generating xml
	 * 
	 * @param order
	 * 
	 * @return String
	 */
	protected String getOrderAuditXml(final Order order)
	{
		Marshaller marshaller = null;
		String xmlString = "";
		final StringWriter writer = new StringWriter();
		try
		{
			final JAXBContext context = JAXBContext.newInstance(Order.class);
			if (null != context)
			{
				marshaller = context.createMarshaller();
				if (null != marshaller)
				{
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					marshaller.marshal(order, writer);
				}
			}
			xmlString = writer.toString();
			LOG.debug("Order create xml end");
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in getOrderAuditXml due to ", ex);
		}
		return xmlString;
	}

	@Override
	public OndemandHystrixCommandConfiguration getHystrixCommandConfig()
	{
		return this.hystrixCommandConfig;
	}

	@Override
	public void setHystrixCommandConfig(final OndemandHystrixCommandConfiguration hystrixCommandConfig)
	{
		this.hystrixCommandConfig = hystrixCommandConfig;
	}

	@Override
	public Converter<OrderModel, Order> getOrderConverter()
	{
		return this.orderConverter;
	}

	@Override
	public void setOrderConverter(final Converter<OrderModel, Order> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	@Override
	public OrderFacade getOrderRestClient()
	{
		return this.orderRestClient;
	}

	@Override
	public void setOrderRestClient(final OrderFacade orderRestClient)
	{
		this.orderRestClient = orderRestClient;
	}

	@Override
	public TicketBusinessService getTicketBusinessService()
	{
		return this.ticketBusinessService;
	}

	@Override
	public void setTicketBusinessService(final TicketBusinessService ticketBusinessService)
	{
		this.ticketBusinessService = ticketBusinessService;
	}

	@Override
	public ModelService getModelService()
	{
		return this.modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	protected OndemandHystrixCommandFactory getOndemandHystrixCommandFactory()
	{
		return this.ondemandHystrixCommandFactory;
	}

	@Override
	public void setOndemandHystrixCommandFactory(final OndemandHystrixCommandFactory ondemandHystrixCommandFactory)
	{
		this.ondemandHystrixCommandFactory = ondemandHystrixCommandFactory;
	}

	/**
	 * @return the ordercreation
	 */
	public MplSendOrderFromCommerceToCRM getOrdercreation()
	{
		return ordercreation;
	}

	/**
	 * @param ordercreation
	 *           the ordercreation to set
	 */
	public void setOrdercreation(final MplSendOrderFromCommerceToCRM ordercreation)
	{
		this.ordercreation = ordercreation;
	}

	//Update PickUpDetails OMS Call
	public void upDatePickUpDetails(final OrderModel orderModel)
	{
		final PickupInfo pickInfo = new PickupInfo();
		if (null != orderModel.getCode())
		{
			pickInfo.setOrderId(orderModel.getCode());
		}
		if (null != orderModel.getPickupPersonName())
		{
			pickInfo.setPickupPerson(orderModel.getPickupPersonName());
		}
		if (null != orderModel.getPickupPersonMobile())
		{
			pickInfo.setAlternateContactNumber(orderModel.getPickupPersonMobile());
		}
		try
		{
			LOG.info("OMS PickUpDetails Upadet Call");
			//orderRestClient.createOrder(pickInfo);
			pickupInfoRestClient.updatePickupInfo(pickInfo);
		}
		catch (final Exception e)
		{

			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	public ChangeDeliveryAddressResponseDto changeDeliveryRequestCallToOMS(final ChangeDeliveryAddressDto request)
	{
		LOG.debug("Calling OMS for change delivery request ");
		ChangeDeliveryAddressResponseDto responce = new ChangeDeliveryAddressResponseDto();
		try
		{

			try
			{
				responce = changeDeliveryAddressFacade.update(request);
			}
			catch (final Exception e)
			{
				LOG.error("Exception while calling to OMS ");
			}
			final List<TransactionEddDto> transactionEddDto = new ArrayList<TransactionEddDto>();

			if (null != responce.getTransactionEddDtos() && !responce.getTransactionEddDtos().isEmpty())
			{
				for (final TransactionEddDto dto : responce.getTransactionEddDtos())
				{
					final TransactionEddDto dto1 = new TransactionEddDto();
					dto1.setTransactionID(dto.getTransactionID());
					dto1.setEDD(dto.getEDD());
					transactionEddDto.add(dto1);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(" Exception While calling to OMS " + e.getCause());
		}
		return responce;
	}

	private String[] getOmsException()
	{
		final String connectTimeoutException = getConfigurationService().getConfiguration().getString(
				MarketplacecclientservicesConstants.OMS_FALLBACK_CONNECT_TIMEOUT_EXCEP);
		final String readTimeoutException = getConfigurationService().getConfiguration().getString(
				MarketplacecclientservicesConstants.OMS_FALLBACK_READ_TIMEOUT_EXCEP);

		final StringBuffer stb = new StringBuffer(300);

		if (StringUtils.isNotEmpty(connectTimeoutException))
		{
			stb.append(connectTimeoutException);
		}
		if (StringUtils.isNotEmpty(readTimeoutException))
		{
			stb.append(',');
			stb.append(readTimeoutException);
		}
		return stb.toString().split(",");
	}

	/**
	 * @return the mplCustomerWebService
	 */
	public MplCustomerWebService getMplCustomerWebService()
	{
		return mplCustomerWebService;
	}

	/**
	 * @param mplCustomerWebService
	 *           the mplCustomerWebService to set
	 */
	public void setMplCustomerWebService(final MplCustomerWebService mplCustomerWebService)
	{
		this.mplCustomerWebService = mplCustomerWebService;
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
}
