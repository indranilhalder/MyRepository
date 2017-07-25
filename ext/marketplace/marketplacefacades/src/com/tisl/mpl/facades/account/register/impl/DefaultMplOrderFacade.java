///**
// *
// */
package com.tisl.mpl.facades.account.register.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.facades.product.data.ReturnReasonDetails;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.CRMTicketDetailModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.service.MplAwbStatusService;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.CustomerOrderInfoWsDTO;
import com.tisl.mpl.wsdto.DeliveryTrackingInfoWsDTO;
import com.tisl.mpl.wsdto.OrderInfoWsDTO;
import com.tisl.mpl.wsdto.TicketMasterXMLData;
import com.tisl.mpl.xml.pojo.AWBStatusResponse;
import com.tisl.mpl.xml.pojo.AWBStatusResponse.AWBResponseInfo;
import com.tisl.mpl.xml.pojo.AWBStatusResponse.AWBResponseInfo.StatusRecords;


/**
 *
 * @author TCS
 *
 */
public class DefaultMplOrderFacade implements MplOrderFacade
{
	@Autowired
	private MplOrderService mplOrderService;
	@Autowired
	private UserService userService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private Converter<OrderModel, OrderHistoryData> orderHistoryConverter;
	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;
	@Autowired
	private MplSellerInformationService mplSellerInformationService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private CustomerAccountService customerAccountService;

	@Autowired
	private CustomOmsOrderService customOmsOrderService;

	@Autowired
	private CustomerFacade customerFacade;

	@Autowired
	private OrderModelService orderModelService;


	@Autowired
	private TicketCreationCRMservice ticketCreate;

	@Autowired
	private ModelService modelService;

	@Autowired
	private OrderModelDao orderModelDao;

	@Autowired
	private CheckoutCustomerStrategy checkoutCustomerStrategy; //TISPT-175

	@Autowired
	private MplAwbStatusService mplAwbStatusService;

	protected static final Logger LOG = Logger.getLogger(DefaultMplOrderFacade.class);

	/**
	 * @return the mplOrderService
	 */
	public MplOrderService getMplOrderService()
	{
		return mplOrderService;
	}

	/**
	 * @param mplOrderService
	 *           the mplOrderService to set
	 */
	public void setMplOrderService(final MplOrderService mplOrderService)
	{
		this.mplOrderService = mplOrderService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the orderHistoryConverter
	 */
	public Converter<OrderModel, OrderHistoryData> getOrderHistoryConverter()
	{
		return orderHistoryConverter;
	}

	/**
	 * @param orderHistoryConverter
	 *           the orderHistoryConverter to set
	 */
	public void setOrderHistoryConverter(final Converter<OrderModel, OrderHistoryData> orderHistoryConverter)
	{
		this.orderHistoryConverter = orderHistoryConverter;
	}



	@Override
	public List<ReturnReasonData> getReturnReasonForOrderItem()
	{
		try
		{
			List<ReturnReasonData> reasonList = new ArrayList<ReturnReasonData>();
			reasonList = mplOrderService.getReturnReasonForOrderItem();

			return reasonList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public SearchPageData<OrderHistoryData> getPagedSubOrderHistoryForStatuses(final PageableData pageableData,
			final OrderStatus... statuses)
	{
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
			final SearchPageData<OrderModel> orderResults = mplOrderService.getSubOrderList(currentCustomer, currentBaseStore,
					statuses, pageableData);
			return convertPageData(orderResults, orderHistoryConverter);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public SearchPageData<OrderHistoryData> getPagedParentOrderHistoryForStatuses(final PageableData pageableData,
			final OrderStatus... statuses)
	{
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
			final SearchPageData<OrderModel> orderResults = mplOrderService.getParentOrderList(currentCustomer, currentBaseStore,
					statuses, pageableData);
			return convertPageData(orderResults, orderHistoryConverter);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * Returns the order history with duration as filter TISEE-1855
	 *
	 * @param pageableData
	 *           paging information
	 * @return The order history of the current user.
	 */
	@Override
	public SearchPageData<OrderHistoryData> getPagedFilteredParentOrderHistory(final PageableData pageableData)
	{
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();

			final String orderHistoryDuration = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.ORDER_HISTORY_DURATION_DAYS,
					MarketplacecommerceservicesConstants.ORDER_HISTORY_DEFAULT_DURATION_DAYS);


			LOG.debug(">> Order History duration : " + orderHistoryDuration);

			final Date fromDate = subtractDays(new Date(), Integer.parseInt(orderHistoryDuration));

			LOG.debug(">> Order History duration From Date : " + fromDate);


			final SearchPageData<OrderModel> orderResults = mplOrderService.getPagedFilteredParentOrderHistory(currentCustomer,
					currentBaseStore, pageableData, fromDate);
			return convertPageData(orderResults, orderHistoryConverter);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Desc : subtract days to date for order history TISEE-1855
	 *
	 * @param date
	 * @param days
	 * @return Date
	 */
	public Date subtractDays(final Date date, final int days)
	{
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, -days);

		return cal.getTime();

	}

	protected <S, T> SearchPageData<T> convertPageData(final SearchPageData<S> source, final Converter<S, T> converter)
	{
		final SearchPageData<T> result = new SearchPageData<T>();
		result.setPagination(source.getPagination());
		result.setSorts(source.getSorts());
		result.setResults(Converters.convertAll(source.getResults(), converter));
		return result;
	}


	@Override
	public List<CancellationReasonModel> getCancellationReason()
	{
		try
		{
			return mplOrderService.getCancellationReason();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public ReturnReasonDetails getReturnReasonForOrderItem(@RequestParam final String returnCancelFlag)
	{

		List<ReturnReasonData> returnReasonDataList = new ArrayList<ReturnReasonData>();
		List<CancellationReasonModel> cancellationReasonModel = new ArrayList<CancellationReasonModel>();
		final ReturnReasonDetails returnReasonDetails = new ReturnReasonDetails();
		try
		{
			if (returnCancelFlag.equalsIgnoreCase("R"))
			{
				returnReasonDataList = getReturnReasonForOrderItem();
			}
			else if (returnCancelFlag.equalsIgnoreCase("C"))
			{
				cancellationReasonModel = getCancellationReason();
				//if (null != cancellationReasonModel && cancellationReasonModel.size() > 0)
				if (CollectionUtils.isNotEmpty(cancellationReasonModel))
				{
					for (final CancellationReasonModel newModel : cancellationReasonModel)
					{
						final ReturnReasonData returnReasonDataObj = new ReturnReasonData();

						returnReasonDataObj.setReasonDescription(newModel.getReasonDescription());
						returnReasonDataObj.setCode(newModel.getReasonCode());

						returnReasonDataList.add(returnReasonDataObj);
					}
				}
			}

			returnReasonDetails.setReturnReasonDetailsList(returnReasonDataList);
		}
		catch (final UsernameNotFoundException notFound)
		{ //User name not found
			throw new EtailNonBusinessExceptions(notFound, MarketplacecommerceservicesConstants.B0006);
		}
		catch (final DataAccessException repositoryProblem)
		{
			throw new EtailNonBusinessExceptions(repositoryProblem, MarketplacecommerceservicesConstants.B0005);
		}
		catch (final AuthenticationServiceException authServiceException)
		{
			throw new EtailNonBusinessExceptions(authServiceException, MarketplacecommerceservicesConstants.B0004);
		}
		catch (final AuthenticationException authException)
		{
			throw new EtailNonBusinessExceptions(authException, MarketplacecommerceservicesConstants.B0003);
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return returnReasonDetails;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.account.register.MplOrderFacade#getPagedParentOrderHistory(de.hybris.platform.
	 * commerceservices .search.pagedata.PageableData, de.hybris.platform.core.enums.OrderStatus[],
	 * de.hybris.platform.core.model.user.CustomerModel)
	 */
	@Override
	public SearchPageData<OrderData> getPagedParentOrderHistory(final PageableData pageableData, final CustomerModel customer,
			final OrderStatus... statuses)
	{
		try
		{
			/*
			 * final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore(); final
			 * SearchPageData<OrderModel> orderResults = mplOrderService.getParentOrderList(customer, currentBaseStore,
			 * statuses, pageableData);
			 */

			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();

			final String orderHistoryDuration = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.ORDER_HISTORY_DURATION_DAYS,
					MarketplacecommerceservicesConstants.ORDER_HISTORY_DEFAULT_DURATION_DAYS);

			LOG.debug(">> Order History duration Mobile Web service: " + orderHistoryDuration);

			final Date fromDate = subtractDays(new Date(), Integer.parseInt(orderHistoryDuration));

			LOG.debug(">> Order History duration From Date Mobile web service: " + fromDate);


			final SearchPageData<OrderModel> orderResults = mplOrderService.getPagedFilteredParentOrderHistory(currentCustomer,
					currentBaseStore, pageableData, fromDate);
			return convertPageData(orderResults, orderConverter);

		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public ProductModel getProductForCode(final String productCode)
	{
		final ProductModel products = mplOrderService.findProductsByCode(productCode);
		return products;
	}

	/*
	 * @Desc : Used to fetch IMEI details for Account Page order history
	 *
	 * @return Map<String, Map<String, String>>
	 *
	 * @ throws EtailNonBusinessExceptions
	 */
	@Override
	public Map<String, Map<String, String>> fetchOrderSerialNoDetails(final List<OrderModel> orderModelList)
			throws EtailNonBusinessExceptions
	{
		final Map<String, Map<String, String>> productSerrialNumber = new HashMap<String, Map<String, String>>();

		if (CollectionUtils.isNotEmpty(orderModelList))
		{
			for (final OrderModel orderModel : orderModelList)
			{
				final Map<String, String> entryImeiNumber = new HashMap<String, String>();
				for (final AbstractOrderEntryModel entry : orderModel.getEntries())
				{
					if (entry.getImeiDetail() != null)
					{
						entryImeiNumber.put(entry.getEntryNumber().toString(), entry.getImeiDetail().getSerialNum());
					}
					else
					{
						entryImeiNumber.put(entry.getEntryNumber().toString(), "");
					}
				}
				productSerrialNumber.put(orderModel.getCode(), entryImeiNumber);
			}
		}
		else
		{
			LOG.debug("Order History >> fetchOrderSerialNoDetails >> OrderModels is null or empty for Customer ");
		}
		return productSerrialNumber;
	}

	/*
	 * @Desc : Used to fetch Invoice details for Account Page order history
	 *
	 * @param : orderModelList
	 *
	 * @return Map<String, Boolean>
	 *
	 * @ throws EtailNonBusinessExceptions
	 */
	@Override
	public Map<String, Boolean> fetchOrderInvoiceDetails(final List<OrderModel> orderModelList) throws EtailNonBusinessExceptions
	{
		final Map<String, Boolean> sortInvoice = new HashMap<>();
		if (CollectionUtils.isNotEmpty(orderModelList))
		{
			for (final OrderModel orderModel : orderModelList)
			{
				for (final AbstractOrderEntryModel entry : orderModel.getEntries())
				{
					for (final ConsignmentEntryModel c : entry.getConsignmentEntries())
					{
						if (null != c.getConsignment() && null != c.getConsignment().getInvoice()
								&& null != c.getConsignment().getInvoice().getInvoiceUrl())
						{
							sortInvoice.put(entry.getTransactionID(), Boolean.TRUE);
						}
					}
				}
			}
		}
		else
		{
			LOG.debug("Order History >> fetchOrderInvoiceDetails >> OrderModels is null or empty for Customer ");
		}
		return sortInvoice;
	}


	/*
	 * @Desc : Used to fetch and populate details for Account Page order history
	 *
	 * @param : orderEntryData
	 *
	 * @return OrderEntryData
	 *
	 * @ throws EtailNonBusinessExceptions
	 */
	@Override
	public OrderEntryData fetchOrderEntryDetails(final OrderEntryData orderEntryData, final OrderData subOrder)
			throws EtailNonBusinessExceptions
	{
		ConsignmentModel consignmentModel = null;
		if (null != orderEntryData.getConsignment() && orderEntryData.getConsignment().getStatus() != null)
		{

			//TISEE-1067
			if (null != orderEntryData.getConsignment()
					&& orderEntryData.getConsignment().getStatus() != null
					&& (orderEntryData.getConsignment().getStatus().getCode()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED) || orderEntryData.getConsignment()
							.getStatus().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_COLLECTED)))
			{
				consignmentModel = mplOrderService.fetchConsignment(orderEntryData.getConsignment().getCode());
				//TISPT-194
				//		final String tranSactionId = orderEntryData.getTransactionId();
				//TISEE-1067
				if (null != consignmentModel
						&& null != consignmentModel.getInvoice()
						&& null != consignmentModel.getInvoice().getInvoiceUrl()
						&& null != orderEntryData.getConsignment()
						&& orderEntryData.getConsignment().getStatus() != null
						&& (orderEntryData.getConsignment().getStatus().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED) || orderEntryData.getConsignment()
								.getStatus().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_COLLECTED)))
				{
					orderEntryData.setShowInvoiceStatus(true);
				}
			}
		}
		//getting the product code
		ProductModel productModel = new ProductModel();
		try
		{
			productModel = getProductForCode(orderEntryData.getProduct().getCode());
			LOG.debug("Step4-************************Order History: After Product:" + subOrder.getCode());
		}
		catch (final Exception e)
		{
			LOG.error("**********Product " + orderEntryData.getProduct().getCode()
					+ " is having some data issue. Please validate the product!");
			return null;
		}


		if (CollectionUtils.isNotEmpty(productModel.getBrands()))
		{
			for (final BrandModel brand : productModel.getBrands())
			{
				orderEntryData.setBrandName(brand.getName());
				break;
			}
		}

		List<RichAttributeModel> richAttributeModel = new ArrayList<RichAttributeModel>();
		final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
				orderEntryData.getSelectedUssid());

		if (sellerInfoModel != null && CollectionUtils.isNotEmpty(sellerInfoModel.getRichAttribute()))
		{
			richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();

			LOG.debug("Step 5************************Order History:  setting setItemCancellationStatus for " + subOrder.getCode());
			//TISEE-5389 - Cancellation window check while initiating a cancel request need not be performed.
			//Commenting cancellation window check, as not all seller will set this value. If it is not set,
			//the default value would be '0' and as per the below check cancellation link won't shown at all.
			/*
			 * if (richAttributeModel.get(0).getCancellationWindow() != null &&
			 * Integer.parseInt(richAttributeModel.get(0).getCancellationWindow()) == 0) {
			 * orderEntryData.setItemCancellationStatus(false); } else
			 */
			//TISEE-6419
			if (!isChildCancelleable(subOrder, orderEntryData.getTransactionId()))
			{
				orderEntryData.setItemCancellationStatus(false);
			}
			else if (null == orderEntryData.getConsignment() && orderEntryData.getQuantity().longValue() != 0)
			{
				if (subOrder.getStatus() != null)
				{
					orderEntryData.setItemCancellationStatus(checkCancelStatus(subOrder.getStatus().getCode(),
							MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS));
				}
			}
			else if (null != orderEntryData.getConsignment() && null != orderEntryData.getConsignment().getStatus())
			{
				final String consignmentStatus = orderEntryData.getConsignment().getStatus().getCode();
				orderEntryData.setItemCancellationStatus(checkCancelStatus(consignmentStatus,
						MarketplacecommerceservicesConstants.CANCEL_STATUS));
			}

			LOG.debug("Step 6************************Order History:  setting setItemReturnStatus for " + subOrder.getCode());


			if (null != orderEntryData.getConsignment() && null != orderEntryData.getConsignment().getStatus()
					&& null != richAttributeModel.get(0).getReturnWindow()
					&& Integer.parseInt(richAttributeModel.get(0).getReturnWindow()) > 0)
			{
				consignmentModel = mplOrderService.fetchConsignment(orderEntryData.getConsignment().getCode());
				if (null != consignmentModel)
				{

					//					if (richAttributeModel.get(0).getReturnWindow() != null
					//							&& Integer.parseInt(richAttributeModel.get(0).getReturnWindow()) == 0)
					//					{
					//						orderEntryData.setItemReturnStatus(false);
					//					}
					final Date sDate = new Date();
					final int returnWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(consignmentModel.getDeliveryDate(),
							sDate);
					final int actualReturnWindow = Integer.parseInt(richAttributeModel.get(0).getReturnWindow());
					if (null != orderEntryData.getConsignment()
							&& null != orderEntryData.getConsignment().getStatus()
							&& (orderEntryData.getConsignment().getStatus().getCode()
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED) || orderEntryData.getConsignment()
									.getStatus().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.COLLECTED))
							&& returnWindow <= actualReturnWindow)
					{
						orderEntryData.setItemReturnStatus(true);
					}
				}
				else
				{
					orderEntryData.setItemReturnStatus(false);
				}
			}
			else
			{
				orderEntryData.setItemReturnStatus(false);
			}
		}
		else
		{
			LOG.debug("Order History : Seller Info model or rich attribute model is null or empty for ussid "
					+ orderEntryData.getSelectedUssid());
		}

		return orderEntryData;
	}


	/**
	 * @description: To find the Cancellation is enabled/disabled
	 * @param: currentStatus
	 * @return: currentStatus
	 */
	@Override
	public boolean checkCancelStatus(final String currentStatus, final String status)
	{
		String cancelStatus = "";
		cancelStatus = configurationService.getConfiguration().getString(status);
		return (cancelStatus.indexOf(currentStatus) == -1) ? false : true;
	}

	/**
	 * TISEE-6419
	 *
	 * @param orderData
	 * @param transactionId
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean isChildCancelleable(final OrderData orderData, final String transactionId) throws EtailNonBusinessExceptions
	{
		boolean isCheckChildCancellable = true;
		String consignmentStatus = "";
		final OrderModel subOrderModel = customerAccountService.getOrderForCode((CustomerModel) userService.getCurrentUser(),
				orderData.getCode(), baseStoreService.getCurrentBaseStore());

		for (final AbstractOrderEntryModel subEntryModel : subOrderModel.getEntries())
		{
			boolean isChildEntry = false;

			final String parentTransactionId = subEntryModel.getParentTransactionID();
			if (StringUtils.isNotEmpty(parentTransactionId)
					&& (subEntryModel.getIsBOGOapplied().booleanValue() || subEntryModel.getGiveAway().booleanValue())
					&& parentTransactionId.split(",").length > 1 && parentTransactionId.contains(transactionId))
			{
				isChildEntry = true;
			}
			else if (StringUtils.isNotEmpty(parentTransactionId)
					&& (subEntryModel.getIsBOGOapplied().booleanValue() || subEntryModel.getGiveAway().booleanValue())
					&& parentTransactionId.equalsIgnoreCase(transactionId))
			{
				isChildEntry = true;
			}
			LOG.debug(">>>>> transactionId " + transactionId + " >> parentTransactionId : " + parentTransactionId
					+ ">> isChildEntry " + isChildEntry);

			if (isChildEntry && CollectionUtils.isEmpty(subEntryModel.getConsignmentEntries())
					&& subEntryModel.getQuantity() != Long.valueOf(0) && orderData.getStatus() != null
					&& orderData.getStatus().getCode() != null)
			{
				consignmentStatus = orderData.getStatus().getCode();
				LOG.debug(" >> isChildCancelleable >> order: Consignemnt is null or empty : Order code :" + orderData.getCode()
						+ MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);
				if (!checkCancelStatus(orderData.getStatus().getCode(), MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS))
				{
					LOG.debug(" >> isChildCancelleable >>order: Consignemnt is null or empty : Setting cancel status to true for  Order code :"
							+ orderData.getCode() + MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);
					isCheckChildCancellable = false;
					break;
				}
			}
			else if (isChildEntry && CollectionUtils.isNotEmpty(subEntryModel.getConsignmentEntries()))
			{
				for (final ConsignmentEntryModel consignmentEntryModel : subEntryModel.getConsignmentEntries())
				{
					final ConsignmentModel cosignmentModel = consignmentEntryModel.getConsignment();
					if (cosignmentModel != null
							&& cosignmentModel.getStatus() != null
							&& cosignmentModel.getStatus().getCode() != null
							&& !checkCancelStatus(cosignmentModel.getStatus().getCode(),
									MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS))
					{
						LOG.debug(" >> isChildCancelleable >> order: Consignemnt is null or empty : Setting cancel status to true for  Order code :"
								+ orderData.getCode() + MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);
						isCheckChildCancellable = false;
						break;
					}
				}
			}
		}

		return isCheckChildCancellable;
	}




	@Override
	public String editPickUpInfo(final String orderId, final String name, final String mobile)
	{


		try
		{
			LOG.debug("Send PickUpDetails Based On OrderId");
			orderModelService.updatePickUpDetailService(orderId, name, mobile);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			LOG.error("Update PickDetails that time Error Rasing");
			return MarketplaceFacadesConstants.STATUS_FAILURE;
		}
		LOG.info("Update PickUp Details successfully");
		return MarketplaceFacadesConstants.STATUS_SUCESS;
	}

	@Override
	public void createCrmTicketUpdatePickDetails(final String orderId)
	{
		OrderModel orderModel = null;
		try
		{
			orderModel = orderModelDao.getOrderModel(orderId);

			if (orderModel != null)
			{
				try
				{
					LOG.info(" OMS Call From Commerece when PickUp Person Details Updated");
					customOmsOrderService.upDatePickUpDetails(orderModel);

				}
				catch (final Exception e)
				{
					LOG.error("update pickup_person details then ");

				}
				for (final OrderModel childOrders : orderModel.getChildOrders())
				{
					for (final AbstractOrderEntryModel entry : childOrders.getEntries())
					{
						if (null != entry
								&& entry.getMplDeliveryMode().getDeliveryMode().getCode()
										.equalsIgnoreCase(MarketplaceFacadesConstants.C_C))
						{
							final SendTicketRequestData ticket = new SendTicketRequestData();
							final CustomerData customerData = customerFacade.getCurrentCustomer();
							if (null != customerData)
							{
								ticket.setCustomerID(customerData.getUid());
							}
							final List<SendTicketLineItemData> lineItemData = new ArrayList<SendTicketLineItemData>();
							final SendTicketLineItemData reqData = new SendTicketLineItemData();
							reqData.setLineItemId(entry.getOrderLineId());
							lineItemData.add(reqData);
							ticket.setLineItemDataList(lineItemData);
							ticket.setSource(MarketplacecommerceservicesConstants.SOURCE);
							ticket.setOrderId(orderModel.getCode());
							ticket.setTicketType(MarketplacecommerceservicesConstants.TICKET_TYPE);
							ticket.setTicketSubType(MarketplacecommerceservicesConstants.TICKET_SUB_TYPE);
							ticket.setAlternateContactName(orderModel.getPickupPersonName());
							ticket.setAlternatePhoneNo(orderModel.getPickupPersonMobile());

							final String asyncEnabled = configurationService.getConfiguration()
									.getString(MarketplacecommerceservicesConstants.ASYNC_ENABLE).trim();
							//create ticket only if async is not working
							if (asyncEnabled.equalsIgnoreCase("N"))
							{
								LOG.debug(" ****** Before CRM Ticket Creatin *********");
								ticketCreate.ticketCreationModeltoWsDTO(ticket);
								LOG.debug(" ******* After CRM Ticket Creatin ********");
							}
							else
							{
								// CRM ticket Cron JOB data preparation
								saveTicketDetailsInCommerce(ticket);
							}

						}

					}
				}
			}
		}
		catch (final JAXBException ex)
		{
			LOG.error(" >> Exception occured while CRM ticket creation", ex);
		}
	}




	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.account.register.MplOrderFacade#createcrmTicketForCockpit()
	 */
	@Override
	public void createcrmTicketForCockpit(final OrderModel mainOrder, final String costomerId, final String source)

	{
		LOG.debug("Insise createcrmTicketForCockpit method");
		/* final OrderModel orderModel = null; */
		try
		{
			customOmsOrderService.upDatePickUpDetails(mainOrder);
		}
		catch (final Exception e)
		{
			LOG.debug("Excepton at OMS Calling  >>>>>" + e);
		}

		final String mainOrderId = mainOrder.getCode();
		try
		{
			final List<OrderModel> ordermodel = mainOrder.getChildOrders();
			for (final OrderModel model : ordermodel)
			{
				final String subOrderId = model.getCode();
				final List<AbstractOrderEntryModel> entries = model.getEntries();
				for (final AbstractOrderEntryModel entry : entries)
				{
					String orderStatus = entry.getOrder().getStatus().getCode();
					try
					{
						if (CollectionUtils.isNotEmpty(entry.getConsignmentEntries()))
						{
							final ConsignmentStatus consignmentStatus = entry.getConsignmentEntries().iterator().next().getConsignment()
									.getStatus();
							orderStatus = consignmentStatus.getCode();

						}
					}
					catch (final Exception e)
					{
						LOG.debug("Consignment Entries Null" + e);
					}
					if (entry.getMplDeliveryMode().getDeliveryMode().getCode()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)
							&& entry.getQuantity().intValue() > 0 && checkStastus(orderStatus))


					{

						final SendTicketRequestData ticket = new SendTicketRequestData();

						final List<SendTicketLineItemData> lineItemData = new ArrayList<SendTicketLineItemData>();
						final SendTicketLineItemData reqData = new SendTicketLineItemData();
						reqData.setLineItemId(entry.getOrderLineId());
						lineItemData.add(reqData);
						ticket.setLineItemDataList(lineItemData);
						if (null != entry.getOrderLineId())
						{
							ticket.setOrderId(mainOrderId);
						}
						if (null != subOrderId)
						{
							ticket.setSubOrderId(subOrderId);
						}
						if (null != costomerId)
						{
							ticket.setCustomerID(costomerId);
						}
						if (null != source)
						{
							ticket.setSource(source);
						}

						ticket.setTicketType(MarketplacecommerceservicesConstants.TICKET_TYPE);
						ticket.setTicketSubType(MarketplacecommerceservicesConstants.TICKET_SUB_TYPE);

						if (null != mainOrder.getPickupPersonName())
						{
							ticket.setAlternateContactName(mainOrder.getPickupPersonName());
						}
						if (null != mainOrder.getPickupPersonMobile())
						{
							ticket.setAlternatePhoneNo(mainOrder.getPickupPersonMobile());
						}

						final String asyncEnabled = configurationService.getConfiguration()
								.getString(MarketplacecommerceservicesConstants.ASYNC_ENABLE).trim();
						//create ticket only if async is not working
						if (asyncEnabled.equalsIgnoreCase("N"))
						{
							LOG.debug(" ****** Before CRM Ticket Creatin *********");
							ticketCreate.ticketCreationModeltoWsDTO(ticket);
							LOG.debug(" ******* After CRM Ticket Creatin ********");
						}
						else
						{
							// CRM ticket Cron JOB data preparation
							saveTicketDetailsInCommerce(ticket);
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			LOG.error("<<<<<<<<<<<Exception Rasing convert OrderModel to SendTicketRequestData Wto of class>>>>>>>>" + e);
		}
	}


	// For checking order entry status

	private boolean checkStastus(final String orderEntryStatus)
	{
		LOG.debug("Checking order status for CRM Ticket");
		final List<String> nonChangableOrdeStatus = Arrays.asList(OrderStatus.PAYMENT_FAILED.getCode(),
				OrderStatus.RETURNINITIATED_BY_RTO.getCode());

		final List<String> nonChangableOrdeStatusList = Arrays.asList(ConsignmentStatus.CANCELLATION_INITIATED.getCode(),
				ConsignmentStatus.CANCELLED.getCode(), ConsignmentStatus.CLOSED_ON_CANCELLATION.getCode(),
				ConsignmentStatus.CLOSED_ON_RETURN_TO_ORIGIN.getCode(), ConsignmentStatus.COD_CLOSED_WITHOUT_REFUND.getCode(),
				ConsignmentStatus.ORDER_CANCELLED.getCode(), ConsignmentStatus.ORDER_COLLECTED.getCode(),
				ConsignmentStatus.ORDER_REJECTED.getCode(), ConsignmentStatus.ORDER_UNCOLLECTED.getCode(),
				ConsignmentStatus.QC_FAILED.getCode(), ConsignmentStatus.REFUND_IN_PROGRESS.getCode(),
				ConsignmentStatus.REFUND_INITIATED.getCode(), ConsignmentStatus.RETURN_CANCELLED.getCode(),
				ConsignmentStatus.RETURN_CLOSED.getCode(), ConsignmentStatus.RETURN_INITIATED.getCode(),
				ConsignmentStatus.RETURN_RECEIVED.getCode(), ConsignmentStatus.RETURN_TO_ORIGIN.getCode(),
				ConsignmentStatus.RETURN_COMPLETED.getCode());

		if (nonChangableOrdeStatus.contains(orderEntryStatus)
				|| nonChangableOrdeStatusList.contains(orderEntryStatus.toUpperCase()))
		{
			return false;
		}

		return true;

	}




	private void saveTicketDetailsInCommerce(final SendTicketRequestData sendTicketRequestData)
	{
		String crmRequest = null;

		final CRMTicketDetailModel ticket = modelService.create(CRMTicketDetailModel.class);
		if (null != sendTicketRequestData.getCustomerID())
		{
			ticket.setCustomerID(sendTicketRequestData.getCustomerID());
			LOG.debug("ticket create: customer Id>>>>> " + sendTicketRequestData.getCustomerID());
		}
		if (null != sendTicketRequestData.getOrderId())
		{
			ticket.setOrderId(sendTicketRequestData.getOrderId());
			LOG.debug("ticket create:order Id>>>>> " + sendTicketRequestData.getOrderId());
		}
		if (null != sendTicketRequestData.getSubOrderId())
		{
			ticket.setSubOrderId(sendTicketRequestData.getSubOrderId());
			LOG.debug("ticket create:suborder Id>>>>> " + sendTicketRequestData.getSubOrderId());
		}

		if (null != sendTicketRequestData.getTicketType())
		{
			ticket.setTicketType(sendTicketRequestData.getTicketType());
			LOG.debug("ticket create: TicketType>>>>> " + sendTicketRequestData.getTicketType());
		}
		if (null != sendTicketRequestData.getTicketSubType())
		{
			ticket.setTicketSubType(sendTicketRequestData.getTicketSubType());
			LOG.debug("ticket create: TicketSubType>>>>> " + sendTicketRequestData.getTicketSubType());
		}
		if (null != sendTicketRequestData.getRefundType())
		{
			ticket.setRefundType(sendTicketRequestData.getRefundType());
			LOG.debug("ticket create: RefundType>>>>> " + sendTicketRequestData.getRefundType());

		}
		if (null != sendTicketRequestData.getReturnCategory())
		{
			ticket.setReturnCategory(sendTicketRequestData.getReturnCategory());
			LOG.debug("ticket create: ReturnCategory>>>>> " + sendTicketRequestData.getReturnCategory());

		}
		if (null != sendTicketRequestData.getSource())
		{
			ticket.setSource(sendTicketRequestData.getSource());
			LOG.debug("ticket create: Source>>>>> " + sendTicketRequestData.getSource());

		}


		if (null != sendTicketRequestData.getAlternateContactName())
		{
			ticket.setAlternateContactName(sendTicketRequestData.getAlternateContactName());
			LOG.debug("ticket create: AlternateContactName>>>>> " + sendTicketRequestData.getAlternateContactName());

		}
		if (null != sendTicketRequestData.getAlternatePhoneNo())
		{
			ticket.setAlternatePhoneNo(sendTicketRequestData.getAlternatePhoneNo());
			LOG.debug("ticket create: AlternatePhoneNo>>>>> " + sendTicketRequestData.getAlternatePhoneNo());

		}

		final TicketMasterXMLData ticketXmlData = ticketCreate.ticketCreationModeltoXMLData(sendTicketRequestData);
		if (ticketXmlData != null)
		{
			try
			{
				crmRequest = ticketCreate.createCRMRequestXml(ticketXmlData);
			}
			catch (final JAXBException ex)
			{
				LOG.info(MarketplacecclientservicesConstants.EXCEPTION_IS);

			}
			ticket.setCRMRequest(crmRequest);
		}
		modelService.save(ticket);
	}

	/**
	 * This method used for sorting deliveryMode in sequence of HD ED CNC
	 *
	 * @return List Of deliveryMode
	 */
	@Override
	public List<String> filterDeliveryMode()
	{
		final List<String> deliveryMode = new ArrayList<String>();
		deliveryMode.add(MarketplaceFacadesConstants.HD);
		deliveryMode.add(MarketplaceFacadesConstants.EXPRESS);
		deliveryMode.add(MarketplaceFacadesConstants.C_C);
		return deliveryMode;
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
	 * This method returns the order model based on order code, base store and customer for TISPT-175
	 *
	 * @param orderCode
	 * @return OrderModel
	 */
	@Override
	public OrderModel getOrder(final String orderCode)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore(); //TISPT-175 --- baseStore model : reduce same call from two places
		final OrderModel orderModel = getCheckoutCustomerStrategy().isAnonymousCheckout() ? getCustomerAccountService()
				.getOrderDetailsForGUID(orderCode, baseStoreModel) : getCustomerAccountService().getOrderForCode(
				(CustomerModel) getUserService().getCurrentUser(), orderCode, baseStoreModel); //TISPT-175 --- order model : reduce same call from two places
		if (orderModel == null)
		{
			throw new UnknownIdentifierException("Order with orderGUID " + orderCode
					+ " not found for current user in current BaseStore");
		}

		return orderModel;

	}

	//TPR-5225
	/**
	 * This method returns the order model based on mobile number
	 *
	 * @param mobileNo
	 * @return OrderModel
	 */
	@Override
	public List<OrderModel> getOrderWithMobileNo(final String mobileNo)
	{
		List<OrderModel> orderModel = null;
		orderModel = mplOrderService.fetchOrderByMobile(mobileNo);
		return orderModel;
	}

	//TPR-5225
	@Override
	public String getL4CategoryIdForProduct(final String productCode)
	{
		String pCode = null;
		pCode = mplOrderService.getL4CategoryIdOfProduct(productCode);
		return pCode;
	}

	//TPR-4841
	@Override
	public OrderModel fetchOrderInfoByTransactionId(final String transactionId)
	{
		OrderModel orderModel = null;
		orderModel = mplOrderService.fetchOrderByTransactionId(transactionId);
		return orderModel;

	}

	//TPR-4840
	@Override
	public OrderModel getOrderByParentOrderNo(final String orderRefNo)
	{
		OrderModel orderModel = null;
		orderModel = mplOrderService.getOrderByParentOrderId(orderRefNo);
		return orderModel;
	}

	//TPR-5225
	@Override
	public OrderInfoWsDTO storeOrderInfoByMobileNo(final List<OrderModel> orderModels)
	{
		final OrderInfoWsDTO orderInfoWsDTO = new OrderInfoWsDTO();
		final List<CustomerOrderInfoWsDTO> custdto = new ArrayList<CustomerOrderInfoWsDTO>();
		List<OrderModel> subordermodels = new ArrayList<OrderModel>();
		OrderModel parentOrderModel = null;
		int count = 0;

		try
		{
			//			final List<OrderModel> orderModels = new ArrayList<OrderModel>();
			//
			//			for (final OrderEntryModel orderentry : orderEntryModels)
			//			{
			//				final OrderModel oModel = orderentry.getOrder().getParentReference();
			//				if (!orderModels.contains(oModel))
			//				{
			//					orderModels.add(orderentry.getOrder().getParentReference());
			//				}
			//			}


			if (CollectionUtils.isNotEmpty(orderModels))
			{
				for (final OrderModel parentOrder : orderModels)
				{
					parentOrderModel = parentOrder;
					subordermodels = parentOrderModel.getChildOrders();

					if (CollectionUtils.isNotEmpty(subordermodels))
					{
						//subOrderModel = orderModels.getChildOrders();
						List<AbstractOrderEntryModel> subOrderEntryModels = new ArrayList<AbstractOrderEntryModel>();
						for (final OrderModel subOrder : subordermodels)
						{

							subOrderEntryModels = subOrder.getEntries();

							for (final AbstractOrderEntryModel entry : subOrderEntryModels)
							{
								if (count < 3)
								{
									final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
									customerOrderInfoWsDTO.setTransactionId(null != entry.getTransactionID() ? entry.getTransactionID()
											: "NULL");
									customerOrderInfoWsDTO.setProductName(null != entry.getProduct().getName() ? entry.getProduct()
											.getName() : "NULL");
									if (StringUtils.isNotEmpty(entry.getProduct().getCode()))
									{
										customerOrderInfoWsDTO.setL4CategoryName(mplOrderService.getL4CategoryIdOfProduct(entry
												.getProduct().getCode()));
									}
									else
									{
										customerOrderInfoWsDTO.setL4CategoryName("NULL");
									}
									count++;
									custdto.add(customerOrderInfoWsDTO);
									orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);
								}
								else
								{
									break;
								}
							}
						}
					}
					else
					{
						if (count < 3)
						{
							final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
							count++;
							customerOrderInfoWsDTO.setTransactionId("NULL");
							customerOrderInfoWsDTO.setProductName("NULL");
							customerOrderInfoWsDTO.setL4CategoryName("NULL");
							custdto.add(customerOrderInfoWsDTO);
							orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);
							//orderInfoWsDTO.setError("Order Ref No:" + parentOrder.getCode() + " doesn't have any details");
						}
						else
						{
							break;
						}
					}
				}
			}
			else
			{
				orderInfoWsDTO.setError("Mobile number is not present in Commerce System");
				LOG.error("orderEntryModel is null");
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured: ", e);
			orderInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return orderInfoWsDTO;
	}

	//TPR-4841
	@Override
	public OrderInfoWsDTO storeOrderInfoByTransactionId(final OrderModel orderModel, final String transactionId)
	{
		final OrderInfoWsDTO orderInfoWsDTO = new OrderInfoWsDTO();
		final List<CustomerOrderInfoWsDTO> custdto = new ArrayList<CustomerOrderInfoWsDTO>();
		ConsignmentModel cng = null;

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			if (orderModel != null)
			{
				List<AbstractOrderEntryModel> subOrderModel = new ArrayList<AbstractOrderEntryModel>();
				subOrderModel = orderModel.getEntries();
				if (CollectionUtils.isNotEmpty(subOrderModel))
				{
					for (final AbstractOrderEntryModel entry : subOrderModel)
					{
						final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
						if (StringUtils.isNotEmpty(entry.getTransactionID())
								&& entry.getTransactionID().equalsIgnoreCase(transactionId))
						{
							customerOrderInfoWsDTO.setProductName(null != entry.getProduct().getName() ? entry.getProduct().getName()
									: "NULL");
							customerOrderInfoWsDTO.setSellerName(null != entry.getSellerInfo() ? entry.getSellerInfo() : "NULL");
							if (null != entry.getExpectedDeliveryDate())
							{
								customerOrderInfoWsDTO.setEdd(formatter.format(entry.getExpectedDeliveryDate()));
							}
							else
							{
								customerOrderInfoWsDTO.setEdd("NULL");
							}
							customerOrderInfoWsDTO.setApportionedPrice(null != entry.getNetAmountAfterAllDisc() ? entry
									.getNetAmountAfterAllDisc().toString() : "NULL");
							customerOrderInfoWsDTO.setShippingType(null != entry.getFulfillmentType() ? entry.getFulfillmentType()
									.toString().toUpperCase() : "NULL");
							if (null != entry.getMplDeliveryMode() && null != entry.getMplDeliveryMode().getDeliveryMode()
									&& StringUtils.isNotEmpty(entry.getMplDeliveryMode().getDeliveryMode().getCode()))
							{
								customerOrderInfoWsDTO.setShippingMode(entry.getMplDeliveryMode().getDeliveryMode().getCode());
							}
							else
							{
								customerOrderInfoWsDTO.setShippingMode("NULL");
							}
							if (CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions()))
							{
								final List<PaymentTransactionModel> paymlists = orderModel.getPaymentTransactions();
								final PaymentTransactionModel paytm = paymlists.get(paymlists.size() - 1);

								final List<PaymentTransactionEntryModel> paytmentry = paytm.getEntries();

								final PaymentTransactionEntryModel pt = paytmentry.get(paytmentry.size() - 1);

								customerOrderInfoWsDTO.setTransactionStatusDetails(null != pt.getTransactionStatusDetails() ? pt
										.getTransactionStatusDetails() : "NULL");//Transaction status details

								customerOrderInfoWsDTO.setTransactionStatus(null != pt.getTransactionStatus() ? pt.getTransactionStatus()
										: "NULL");//Transaction Status

								if (null != pt.getTransactionStatus() && pt.getTransactionStatus().equalsIgnoreCase("REFUND_SUCCESSFUL"))
								{
									customerOrderInfoWsDTO.setRefundDate(formatter.format(pt.getTime()));
								}
								else
								{
									customerOrderInfoWsDTO.setRefundDate("NULL");
								}


								if (null != pt.getModifiedtime())
								{
									customerOrderInfoWsDTO.setTransactionTimestamp(formatter.format(pt.getModifiedtime()));//Transaction Timestamp
								}
								else
								{
									customerOrderInfoWsDTO.setTransactionTimestamp("NULL");
								}
								if (null != pt.getCreationtime())
								{
									customerOrderInfoWsDTO.setTransactionCreationDate(formatter.format(pt.getCreationtime()));
								}
								else
								{
									customerOrderInfoWsDTO.setTransactionCreationDate("NULL");
								}
							}
							else
							{
								customerOrderInfoWsDTO.setTransactionStatusDetails("NULL");
								customerOrderInfoWsDTO.setTransactionStatus("NULL");
								customerOrderInfoWsDTO.setRefundDate("NULL");
								customerOrderInfoWsDTO.setTransactionTimestamp("NULL");
								customerOrderInfoWsDTO.setTransactionCreationDate("NULL");
							}

							customerOrderInfoWsDTO.setOrderStatus(null != orderModel.getStatus().getCode() ? orderModel.getStatus()
									.getCode() : "NULL");//Order Status

							if (null != orderModel.getModifiedtime())
							{
								customerOrderInfoWsDTO.setOrderTimestamp(formatter.format(orderModel.getModifiedtime()));//Order timestamp
							}
							else
							{
								customerOrderInfoWsDTO.setOrderTimestamp("NULL");
							}

							if (CollectionUtils.isNotEmpty(entry.getConsignmentEntries()))
							{
								final Iterator it = entry.getConsignmentEntries().iterator();

								while (it.hasNext())
								{
									final ConsignmentEntryModel consg = (ConsignmentEntryModel) it.next();
									customerOrderInfoWsDTO.setCarrierName(null != consg.getConsignment().getCarrier() ? consg
											.getConsignment().getCarrier() : "NULL");//Carrier Name
								}
							}
							else
							{
								customerOrderInfoWsDTO.setCarrierName("NULL");
							}
							customerOrderInfoWsDTO.setPaymentMode(null != orderModel.getModeOfOrderPayment() ? orderModel
									.getModeOfOrderPayment() : "NULL");
							if (CollectionUtils.isNotEmpty(orderModel.getConsignments()))
							{
								final Iterator itr = orderModel.getConsignments().iterator();
								while (itr.hasNext())
								{
									cng = (ConsignmentModel) itr.next();
									//Delivery tracking Details
									if (StringUtils.isNotEmpty(cng.getTrackingID()) && StringUtils.isNotEmpty(cng.getCarrier()))
									{
										final AWBStatusResponse aWBStatusResponse = mplAwbStatusService.prepAwbNumbertoOMS(
												cng.getTrackingID(), cng.getCarrier());

										if (null != aWBStatusResponse && CollectionUtils.isNotEmpty(aWBStatusResponse.getAWBResponseInfo()))
										{
											for (final AWBResponseInfo awbResponseInfo : aWBStatusResponse.getAWBResponseInfo())
											{
												for (final StatusRecords statusRecords : awbResponseInfo.getStatusRecords())
												{
													final DeliveryTrackingInfoWsDTO deliveryTrackingInfoWsDTO = new DeliveryTrackingInfoWsDTO();
													final List<DeliveryTrackingInfoWsDTO> deliveryTrackingListInfoWsDTO = new ArrayList<DeliveryTrackingInfoWsDTO>();
													deliveryTrackingInfoWsDTO
															.setDeliveryTrackingDate(null != statusRecords.getDate() ? statusRecords.getDate()
																	: "NULL");
													deliveryTrackingInfoWsDTO
															.setDeliveryTrackingLocation(null != statusRecords.getLocation() ? statusRecords
																	.getLocation() : "NULL");
													deliveryTrackingInfoWsDTO.setDeliveryTrackingDescription(null != statusRecords
															.getStatusDescription() ? statusRecords.getStatusDescription() : "NULL");
													deliveryTrackingListInfoWsDTO.add(deliveryTrackingInfoWsDTO);
													customerOrderInfoWsDTO.setDeliverytrackingDetails(deliveryTrackingListInfoWsDTO);
													//												custdto.add(customerOrderInfoWsDTO);
													//												orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);
												}
											}
										}
									}
									else
									{
										final DeliveryTrackingInfoWsDTO deliveryTrackingInfoWsDTO = new DeliveryTrackingInfoWsDTO();
										final List<DeliveryTrackingInfoWsDTO> deliveryTrackingListInfoWsDTO = new ArrayList<DeliveryTrackingInfoWsDTO>();
										deliveryTrackingInfoWsDTO.setDeliveryTrackingDate("NULL");
										deliveryTrackingInfoWsDTO.setDeliveryTrackingLocation("NULL");
										deliveryTrackingInfoWsDTO.setDeliveryTrackingDescription("NULL");
										deliveryTrackingListInfoWsDTO.add(deliveryTrackingInfoWsDTO);
										customerOrderInfoWsDTO.setDeliverytrackingDetails(deliveryTrackingListInfoWsDTO);
										//										custdto.add(customerOrderInfoWsDTO);
										//										orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);
									}
									customerOrderInfoWsDTO.setAwbNumber(null != cng.getTrackingID() ? cng.getTrackingID() : "NULL");//AWB number
									customerOrderInfoWsDTO.setReturnCarrier(null != cng.getReturnCarrier() ? cng.getReturnCarrier()
											: "NULL");//Return carrier
									customerOrderInfoWsDTO.setReturnAwbNumber(null != cng.getReturnAWBNum() ? cng.getReturnAWBNum()
											: "NULL");//Return AWB number
									customerOrderInfoWsDTO.setShippingStatus((null != cng.getShipmentStatus() ? cng.getShipmentStatus()
											: "NULL"));//Shipping status
									if (cng.getShippingDate() != null)
									{
										customerOrderInfoWsDTO.setShippingTimestamp(formatter.format(cng.getShippingDate()));
									}
									else
									{
										customerOrderInfoWsDTO.setShippingTimestamp("NULL");
									}

								}
							}
							else
							{
								final DeliveryTrackingInfoWsDTO deliveryTrackingInfoWsDTO = new DeliveryTrackingInfoWsDTO();
								final List<DeliveryTrackingInfoWsDTO> deliveryTrackingListInfoWsDTO = new ArrayList<DeliveryTrackingInfoWsDTO>();
								deliveryTrackingInfoWsDTO.setDeliveryTrackingDate("NULL");
								deliveryTrackingInfoWsDTO.setDeliveryTrackingLocation("NULL");
								deliveryTrackingInfoWsDTO.setDeliveryTrackingDescription("NULL");
								deliveryTrackingListInfoWsDTO.add(deliveryTrackingInfoWsDTO);
								customerOrderInfoWsDTO.setDeliverytrackingDetails(deliveryTrackingListInfoWsDTO);
								customerOrderInfoWsDTO.setAwbNumber("NULL");
								customerOrderInfoWsDTO.setReturnCarrier("NULL");
								customerOrderInfoWsDTO.setReturnAwbNumber("NULL");
								customerOrderInfoWsDTO.setShippingStatus("NULL");
								customerOrderInfoWsDTO.setShippingTimestamp("NULL");
								//								custdto.add(customerOrderInfoWsDTO);
								//								orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);
							}
							if (StringUtils.isNotEmpty(orderModel.getModeOfOrderPayment()))
							{
								if (orderModel.getModeOfOrderPayment().equalsIgnoreCase("COD"))
								{
									customerOrderInfoWsDTO.setPaymentType("POSTPAID");
								}
								else
								{
									customerOrderInfoWsDTO.setPaymentType("PREPAID");
								}
							}
							else
							{
								customerOrderInfoWsDTO.setPaymentType("NULL");
							}
							if (CollectionUtils.isNotEmpty(orderModel.getReturnRequests()))
							{
								final Iterator it1 = orderModel.getReturnRequests().iterator();
								while (it1.hasNext())
								{
									final ReturnRequestModel rq = (ReturnRequestModel) it1.next();
									customerOrderInfoWsDTO.setQcRejectionReason(null != rq.getRejectionReason() ? rq.getRejectionReason()
											: "NULL");//QC rejection reason
									customerOrderInfoWsDTO.setReturnType(null != rq.getTypeofreturn() ? rq.getTypeofreturn().toString()
											: "NULL");//Type of Return
									if (CollectionUtils.isNotEmpty(rq.getReturnEntries()))
									{
										final Iterator it2 = rq.getReturnEntries().iterator();

										while (it2.hasNext())
										{
											final ReturnEntryModel rte = (ReturnEntryModel) it2.next();

											if (rte.getOrderEntry().getTransactionID().equalsIgnoreCase(transactionId))
											{
												customerOrderInfoWsDTO.setReturnRequestStatus(null != rte.getStatus() ? rte.getStatus()
														.toString() : "NULL");//Return request status
												if (null != rte.getCreationtime())
												{
													customerOrderInfoWsDTO
															.setReturnRequestTimestamp(formatter.format((rte.getCreationtime())));//Return timeStamp
												}
												else
												{
													customerOrderInfoWsDTO.setReturnRequestTimestamp("NULL");
												}
												break;
											}

										}
									}
									else
									{
										customerOrderInfoWsDTO.setReturnRequestStatus("NULL");
										customerOrderInfoWsDTO.setReturnRequestTimestamp("NULL");
									}
								}
							}
							else
							{
								customerOrderInfoWsDTO.setReturnRequestStatus("NULL");
								customerOrderInfoWsDTO.setReturnRequestTimestamp("NULL");
								customerOrderInfoWsDTO.setQcRejectionReason("NULL");
								customerOrderInfoWsDTO.setReturnType("NULL");
							}
							custdto.add(customerOrderInfoWsDTO);
							orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);
							break;

						}

					}
				}
			}
			else
			{
				orderInfoWsDTO.setError("TransactionId is not present in Commerce System");
				LOG.error("subOrderModel is null");
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured: ", e);
			orderInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return orderInfoWsDTO;
	}

	//TPR-4840
	@Override
	public OrderInfoWsDTO storeOrderInfoByOrderNo(final OrderModel orderModel)
	{
		final OrderInfoWsDTO orderInfoWsDTO = new OrderInfoWsDTO();
		final List<CustomerOrderInfoWsDTO> custdto = new ArrayList<CustomerOrderInfoWsDTO>();
		StringBuilder custFirstName = null;
		StringBuilder custLastName = null;
		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			if (orderModel != null)
			{
				orderInfoWsDTO.setOrderTotal(null != orderModel.getTotalPriceWithConv() ? orderModel.getTotalPriceWithConv()
						.toString() : "NULL");
				if (null != orderModel.getPaymentAddress() && StringUtils.isNotEmpty(orderModel.getPaymentAddress().getFirstname())
						&& (StringUtils.isNotEmpty(orderModel.getPaymentAddress().getLastname())))
				{
					custFirstName = new StringBuilder(orderModel.getPaymentAddress().getFirstname());
					custLastName = new StringBuilder(orderModel.getPaymentAddress().getLastname());
					orderInfoWsDTO.setCustName(custFirstName.append(" ").append(custLastName).toString());
				}
				else if (null != orderModel.getDeliveryAddress()
						&& StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getFirstname())
						&& (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getLastname())))
				{
					custFirstName = new StringBuilder(orderModel.getDeliveryAddress().getFirstname());
					custLastName = new StringBuilder(orderModel.getDeliveryAddress().getLastname());
					orderInfoWsDTO.setCustName(custFirstName.append(" ").append(custLastName).toString());
				}
				else
				{
					orderInfoWsDTO.setCustName("NULL");
				}
				if (null != orderModel.getPaymentAddress() && StringUtils.isNotEmpty(orderModel.getPaymentAddress().getCellphone()))
				{
					orderInfoWsDTO.setCustMobileNo(orderModel.getPaymentAddress().getCellphone());
				}
				else if (null != orderModel.getDeliveryAddress()
						&& StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getCellphone()))
				{
					orderInfoWsDTO.setCustMobileNo(orderModel.getDeliveryAddress().getCellphone());
				}
				else
				{
					orderInfoWsDTO.setCustMobileNo("NULL");
				}
				if (null != orderModel.getCreationtime())
				{
					orderInfoWsDTO.setOrderDate(formatter.format(orderModel.getCreationtime()));
				}
				else
				{
					orderInfoWsDTO.setOrderDate("NULL");
				}
				List<OrderModel> subOrderModel = new ArrayList<OrderModel>();

				List<AbstractOrderEntryModel> orderEntryModel = new ArrayList<AbstractOrderEntryModel>();
				subOrderModel = orderModel.getChildOrders();
				if (CollectionUtils.isNotEmpty(subOrderModel))
				{
					for (final OrderModel subOrder : subOrderModel)
					{
						orderEntryModel = subOrder.getEntries();
						if (CollectionUtils.isNotEmpty(orderEntryModel))
						{
							for (final AbstractOrderEntryModel entry : orderEntryModel)
							{
								final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
								customerOrderInfoWsDTO.setApportionedPrice((null != entry.getNetAmountAfterAllDisc() ? entry
										.getNetAmountAfterAllDisc().toString() : "NULL"));
								customerOrderInfoWsDTO.setSellerName(null != entry.getSellerInfo() ? entry.getSellerInfo() : "NULL");
								customerOrderInfoWsDTO.setShippingType(null != entry.getFulfillmentType() ? entry.getFulfillmentType()
										.toString().toUpperCase() : "NULL");
								customerOrderInfoWsDTO.setTransactionId(null != entry.getTransactionID() ? entry.getTransactionID()
										: "NULL");
								customerOrderInfoWsDTO.setProductName(null != entry.getProduct().getName() ? entry.getProduct().getName()
										: "NULL");
								if (entry.getExpectedDeliveryDate() != null)
								{
									customerOrderInfoWsDTO.setEdd(formatter.format(entry.getExpectedDeliveryDate()));
								}
								else
								{
									customerOrderInfoWsDTO.setEdd("NULL");
								}
								if (null != entry.getMplDeliveryMode() && null != entry.getMplDeliveryMode().getDeliveryMode()
										&& StringUtils.isNotEmpty(entry.getMplDeliveryMode().getDeliveryMode().getCode()))
								{
									customerOrderInfoWsDTO.setShippingMode(entry.getMplDeliveryMode().getDeliveryMode().getCode());
								}
								else
								{
									customerOrderInfoWsDTO.setShippingMode("NULL");
								}
								if (StringUtils.isNotEmpty(subOrder.getModeOfOrderPayment()))
								{
									if (subOrder.getModeOfOrderPayment().equalsIgnoreCase("COD"))
									{
										customerOrderInfoWsDTO.setPaymentType("POSTPAID");
									}
									else
									{
										customerOrderInfoWsDTO.setPaymentType("PREPAID");
									}
								}
								else
								{
									customerOrderInfoWsDTO.setPaymentType("NULL");
								}
								if (CollectionUtils.isNotEmpty(entry.getConsignmentEntries()))
								{
									final Iterator it = entry.getConsignmentEntries().iterator();
									while (it.hasNext())
									{
										final ConsignmentEntryModel consg = (ConsignmentEntryModel) it.next();
										customerOrderInfoWsDTO.setOrderStatus(consg.getConsignment().getStatus().getCode());//Transaction status
									}
								}
								else
								{
									customerOrderInfoWsDTO.setOrderStatus(entry.getOrder().getStatus().getCode());
								}
								custdto.add(customerOrderInfoWsDTO);
								orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);
							}
						}
					}
				}
				else
				{
					final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
					customerOrderInfoWsDTO.setApportionedPrice("NULL");
					customerOrderInfoWsDTO.setSellerName("NULL");
					customerOrderInfoWsDTO.setShippingType("NULL");
					customerOrderInfoWsDTO.setTransactionId("NULL");
					customerOrderInfoWsDTO.setProductName("NULL");
					customerOrderInfoWsDTO.setEdd("NULL");
					customerOrderInfoWsDTO.setShippingMode("NULL");
					customerOrderInfoWsDTO.setPaymentType("NULL");
					customerOrderInfoWsDTO.setOrderStatus(null != orderModel.getStatus().toString() ? orderModel.getStatus()
							.toString() : "NULL");
					custdto.add(customerOrderInfoWsDTO);
					orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);

				}
			}
			else
			{
				orderInfoWsDTO.setError("Order Reference Number is not present in Commerce System");
				LOG.error("suborderModel is null");
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured: ", e);
			orderInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return orderInfoWsDTO;
	}

	/**
	 * This method returns the order model based on orderNumber, without customer check
	 *
	 * @param orderNumber
	 * @return OrderModel
	 */
	@Override
	public OrderModel getOrderForAnonymousUser(final String orderNumber)
	{
		LOG.debug("Searching for order with order id:" + orderNumber);
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
		final OrderModel orderModel = getCustomerAccountService().getOrderForCode(orderNumber, baseStoreModel);
		if (orderModel == null)
		{
			LOG.debug("Couldn't found order id DB for :" + orderNumber);
			throw new UnknownIdentifierException("Order with orderGUID " + orderNumber
					+ " not found for current user in current BaseStore");
		}
		LOG.debug("Order found for given order id :" + orderNumber);
		return orderModel;
	}

	/**
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	/**
	 * @return the checkoutCustomerStrategy
	 */
	public CheckoutCustomerStrategy getCheckoutCustomerStrategy()
	{
		return checkoutCustomerStrategy;
	}

	/**
	 * @param checkoutCustomerStrategy
	 *           the checkoutCustomerStrategy to set
	 */
	public void setCheckoutCustomerStrategy(final CheckoutCustomerStrategy checkoutCustomerStrategy)
	{
		this.checkoutCustomerStrategy = checkoutCustomerStrategy;
	}

	/**
	 * @return the orderConverter
	 */
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	/**
	 * @param orderConverter
	 *           the orderConverter to set
	 */
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}





}
