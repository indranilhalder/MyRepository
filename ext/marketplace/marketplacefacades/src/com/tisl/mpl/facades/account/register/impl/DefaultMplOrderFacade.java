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
import de.hybris.platform.core.model.JewelleryInformationModel;
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.CRMTicketDetailModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.service.MplAwbStatusService;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.CustomerOrderInfoWsDTO;
import com.tisl.mpl.wsdto.OrderInfoWsDTO;
import com.tisl.mpl.wsdto.TicketMasterXMLData;



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

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;
	@Autowired
	private MplAwbStatusService mplAwbStatusService;

	/* sonar fix */
	/* private final int queryCount = 0; */

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

	//TPR-5954
	@Override
	public List<ReturnReasonData> getCatSpecificRetReason(final String L2CatCode) throws Exception
	{
		return mplOrderService.getCatspecificRetReason(L2CatCode);
	}

	//TPR-5954
	@Override
	public List<ReturnReasonData> getSubReasonCode(final String parentReturnReasonCode) throws Exception
	{
		return mplOrderService.fetchSubReturnReason(parentReturnReasonCode);
	}

	//TPR-5954
	@Override
	public String fetchReasonDesc(final String reasonCode) throws Exception
	{
		return mplOrderService.fetchReasonDesc(reasonCode);
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
		String ussid = null;
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

		if ((MarketplacecommerceservicesConstants.FINEJEWELLERY).equalsIgnoreCase(productModel.getProductCategoryType()))

		{
			final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(orderEntryData
					.getSelectedUssid());
			ussid = jewelleryInfo.get(0).getPCMUSSID();
			//sellerInfoModel = getMplSellerInformationService().getSellerDetail(jewelleryInfo.get(0).getPCMUSSID());
			//added for jewellery
		}
		else
		{
			ussid = orderEntryData.getSelectedUssid();
		}

		List<RichAttributeModel> richAttributeModel = new ArrayList<RichAttributeModel>();
		/*
		 * final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
		 * orderEntryData.getSelectedUssid());
		 */

		final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(ussid);


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
	public List<OrderModel> getOrderWithMobileNo(final String mobileNo, final int queryCount, final int transactionLimit)
	{
		final List<OrderModel> orderModel = mplOrderService.fetchOrderByMobile(mobileNo, queryCount, transactionLimit);
		return orderModel;
	}

	//TPR-5225
	@Override
	public String getL4CategoryNameForProduct(final String productCode)
	{
		final String pCode = mplOrderService.getL4CategoryNameOfProduct(productCode);
		return pCode;
	}

	//TPR-4841
	@Override
	public OrderModel fetchOrderInfoByTransactionId(final String transactionId)
	{
		final OrderModel orderModel = mplOrderService.fetchOrderByTransactionId(transactionId);
		return orderModel;

	}

	//TPR-4840
	@Override
	public OrderModel getOrderByParentOrderNo(final String orderRefNo)
	{
		final OrderModel orderModel = mplOrderService.getOrderByParentOrderId(orderRefNo);
		return orderModel;
	}

	//TPR-5225
	@Override
	public OrderInfoWsDTO storeOrderInfoByMobileNo(final List<OrderModel> orderModels, final int countLimit)
	{
		final OrderInfoWsDTO orderInfoWsDTO = new OrderInfoWsDTO();
		final List<CustomerOrderInfoWsDTO> custdto = new ArrayList<CustomerOrderInfoWsDTO>();
		List<OrderModel> subordermodels = new ArrayList<OrderModel>();
		OrderModel parentOrderModel = null;
		int count = 0;
		boolean errorFlag = true;
		try
		{
			if (CollectionUtils.isNotEmpty(orderModels))
			{
				for (final OrderModel parentOrder : orderModels)
				{
					parentOrderModel = parentOrder;
					//Fetching suborders under parent order
					subordermodels = parentOrderModel.getChildOrders();

					if (CollectionUtils.isNotEmpty(subordermodels))
					{
						List<AbstractOrderEntryModel> subOrderEntryModels = new ArrayList<AbstractOrderEntryModel>();
						//Fetching entries under suborder
						for (final OrderModel subOrder : subordermodels)
						{
							subOrderEntryModels = subOrder.getEntries();
							for (final AbstractOrderEntryModel entry : subOrderEntryModels)
							{
								if (count < countLimit)
								{
									final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
									customerOrderInfoWsDTO.setTransactionId(null != entry.getTransactionID() ? entry.getTransactionID()
											: MarketplacecommerceservicesConstants.NULL_VALUE);
									customerOrderInfoWsDTO.setProductName(null != entry.getProduct().getName() ? entry.getProduct()
											.getName() : MarketplacecommerceservicesConstants.NULL_VALUE);
									if (StringUtils.isNotEmpty(entry.getProduct().getCode()))
									{
										customerOrderInfoWsDTO.setL4CategoryName(mplOrderService.getL4CategoryNameOfProduct(entry
												.getProduct().getCode()));
									}
									else
									{
										customerOrderInfoWsDTO.setL4CategoryName(MarketplacecommerceservicesConstants.NULL_VALUE);
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
					//For payment pending order
					else
					{
						if (count < countLimit)
						{
							final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
							count++;
							customerOrderInfoWsDTO.setTransactionId(MarketplacecommerceservicesConstants.NULL_VALUE);
							customerOrderInfoWsDTO.setProductName(MarketplacecommerceservicesConstants.NULL_VALUE);
							customerOrderInfoWsDTO.setL4CategoryName(MarketplacecommerceservicesConstants.NULL_VALUE);
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
				orderInfoWsDTO.setError(MarketplacecommerceservicesConstants.MOBILE_NO_NOT_PRESENT);
				LOG.error("parent orderModels are null");
			}
			errorFlag = false;
		}
		catch (final NullPointerException e)
		{
			LOG.error("Null pointer Exception occured: ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured: ", e);
		}
		if (errorFlag)//Setting Error Status in case exception thrown
		{
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
		//final DeliveryTrackingInfoWsDTO deliveryTrackingInfoAllWsDTO = new DeliveryTrackingInfoWsDTO();
		//final List<DeliveryTrackingInfoWsDTO> deliveryTrackingListInfoWsDTO = new ArrayList<DeliveryTrackingInfoWsDTO>();
		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		boolean flag = false;
		//final boolean consignmentFlag = false;
		boolean errorFlag = true;
		try
		{
			if (null != orderModel)
			{
				List<AbstractOrderEntryModel> orderEntryModel = new ArrayList<AbstractOrderEntryModel>();
				//fetching entry of suborder specific to transaction id
				orderEntryModel = orderModel.getEntries();
				if (CollectionUtils.isNotEmpty(orderEntryModel))
				{
					for (final AbstractOrderEntryModel entry : orderEntryModel)
					{
						LOG.debug("**********inside entry*********");
						final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
						if (StringUtils.isNotEmpty(entry.getTransactionID())
								&& entry.getTransactionID().equalsIgnoreCase(transactionId))
						{
							customerOrderInfoWsDTO.setProductName(null != entry.getProduct().getName() ? entry.getProduct().getName()
									: MarketplacecommerceservicesConstants.NULL_VALUE);
							customerOrderInfoWsDTO.setSellerName(null != entry.getSellerInfo() ? entry.getSellerInfo()
									: MarketplacecommerceservicesConstants.NULL_VALUE);
							if (null != entry.getExpectedDeliveryDate())
							{
								customerOrderInfoWsDTO.setEdd(formatter.format(entry.getExpectedDeliveryDate()));
							}
							else
							{
								customerOrderInfoWsDTO.setEdd(MarketplacecommerceservicesConstants.NULL_VALUE);
							}
							customerOrderInfoWsDTO.setApportionedPrice(null != entry.getNetAmountAfterAllDisc() ? entry
									.getNetAmountAfterAllDisc().toString() : MarketplacecommerceservicesConstants.NULL_VALUE);
							customerOrderInfoWsDTO.setShippingType(null != entry.getFulfillmentType() ? entry.getFulfillmentType()
									.toString().toUpperCase() : MarketplacecommerceservicesConstants.NULL_VALUE);
							if (null != entry.getMplDeliveryMode() && null != entry.getMplDeliveryMode().getDeliveryMode()
									&& StringUtils.isNotEmpty(entry.getMplDeliveryMode().getDeliveryMode().getCode()))
							{
								customerOrderInfoWsDTO.setShippingMode(entry.getMplDeliveryMode().getDeliveryMode().getCode());
							}
							else
							{
								customerOrderInfoWsDTO.setShippingMode(MarketplacecommerceservicesConstants.NULL_VALUE);
							}
							if (CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions()))
							{
								final List<PaymentTransactionModel> paymlists = orderModel.getPaymentTransactions();
								final PaymentTransactionModel paytm = paymlists.get(paymlists.size() - 1);

								final List<PaymentTransactionEntryModel> paytmentry = paytm.getEntries();

								final PaymentTransactionEntryModel pt = paytmentry.get(paytmentry.size() - 1);

								customerOrderInfoWsDTO.setTransactionStatusDetails(null != pt.getTransactionStatusDetails() ? pt
										.getTransactionStatusDetails() : MarketplacecommerceservicesConstants.NULL_VALUE);//Transaction status details

								customerOrderInfoWsDTO.setTransactionStatus(null != pt.getTransactionStatus() ? pt.getTransactionStatus()
										: MarketplacecommerceservicesConstants.NULL_VALUE);//Transaction Status

								if (null != pt.getTransactionStatusDetails()
										&& pt.getTransactionStatusDetails().equalsIgnoreCase(
												MarketplacecommerceservicesConstants.REFUND_SUCCESSFUL_))
								{
									customerOrderInfoWsDTO.setRefundDate(formatter.format(pt.getTime()));
								}
								else
								{
									customerOrderInfoWsDTO.setRefundDate(MarketplacecommerceservicesConstants.NULL_VALUE);
								}


								if (null != pt.getModifiedtime())
								{
									customerOrderInfoWsDTO.setTransactionTimestamp(formatter.format(pt.getModifiedtime()));//Transaction Timestamp
								}
								else
								{
									customerOrderInfoWsDTO.setTransactionTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
								}
								if (null != pt.getCreationtime())
								{
									customerOrderInfoWsDTO.setTransactionCreationDate(formatter.format(pt.getCreationtime()));
								}
								else
								{
									customerOrderInfoWsDTO.setTransactionCreationDate(MarketplacecommerceservicesConstants.NULL_VALUE);
								}
							}
							else
							{
								customerOrderInfoWsDTO.setTransactionStatusDetails(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setTransactionStatus(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setRefundDate(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setTransactionTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setTransactionCreationDate(MarketplacecommerceservicesConstants.NULL_VALUE);
							}

							customerOrderInfoWsDTO.setOrderStatus(null != orderModel.getStatus().getCode() ? orderModel.getStatus()
									.getCode() : MarketplacecommerceservicesConstants.NULL_VALUE);//Order Status

							if (null != orderModel.getModifiedtime())
							{
								customerOrderInfoWsDTO.setOrderTimestamp(formatter.format(orderModel.getModifiedtime()));//Order timestamp
							}
							else
							{
								customerOrderInfoWsDTO.setOrderTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
							}

							if (CollectionUtils.isNotEmpty(entry.getConsignmentEntries()))
							{
								for (final ConsignmentEntryModel consg : entry.getConsignmentEntries())
								{
									customerOrderInfoWsDTO.setCarrierName(null != consg.getConsignment().getCarrier() ? consg
											.getConsignment().getCarrier() : MarketplacecommerceservicesConstants.NULL_VALUE);//Carrier Name
								}
							}
							else
							{
								customerOrderInfoWsDTO.setCarrierName(MarketplacecommerceservicesConstants.NULL_VALUE);
							}
							customerOrderInfoWsDTO.setPaymentMode(null != orderModel.getModeOfOrderPayment() ? orderModel
									.getModeOfOrderPayment() : MarketplacecommerceservicesConstants.NULL_VALUE);
							//SDI-1193 starts
							if (CollectionUtils.isNotEmpty(orderModel.getConsignments()))
							{
								for (final ConsignmentModel cng : orderModel.getConsignments())
								{
									//									if (!consignmentFlag)
									//									{
									//										LOG.debug("**********inside false consignment flag*************");
									if (cng.getCode().equalsIgnoreCase(transactionId))
									{
										//											//Delivery tracking Details
										//											if (StringUtils.isNotEmpty(cng.getTrackingID()))
										//											{
										//												LOG.debug("**********tracking id*********" + cng.getTrackingID());
										//												LOG.debug("**********carrier*********" + cng.getCarrier());
										//												final AWBStatusResponse aWBStatusResponse = mplAwbStatusService.prepAwbNumbertoOMS(
										//														cng.getTrackingID(), cng.getCarrier());
										//												if (null != aWBStatusResponse
										//														&& CollectionUtils.isNotEmpty(aWBStatusResponse.getAWBResponseInfo()))
										//												{
										//													LOG.debug("**********AWB STATUS RESPONSE*********" + aWBStatusResponse);
										//													LOG.debug("**********AWB RESPONSE INFO*********" + aWBStatusResponse.getAWBResponseInfo());
										//													for (final AWBResponseInfo awbResponseInfo : aWBStatusResponse.getAWBResponseInfo())
										//													{
										//														if (CollectionUtils.isNotEmpty(awbResponseInfo.getStatusRecords()))
										//														{
										//															deliveryTrackingListInfoWsDTO.clear();//Removing pre stored values
										//															LOG.debug("********Removing pre stored values in delivery tracking list dto****");
										//															for (final StatusRecords statusRecords : awbResponseInfo.getStatusRecords())
										//															{
										//																final DeliveryTrackingInfoWsDTO deliveryTrackingInfoWsDTO = new DeliveryTrackingInfoWsDTO();
										//																LOG.debug("********delivery tracking date" + statusRecords.getDate());
										//																LOG.debug("********delivery tracking location" + statusRecords.getLocation());
										//																LOG.debug("********delivery tracking description"
										//																		+ statusRecords.getStatusDescription());
										//																deliveryTrackingInfoWsDTO
										//																		.setDeliveryTrackingDate(null != statusRecords.getDate() ? statusRecords
										//																				.getDate() : MarketplacecommerceservicesConstants.NULL_VALUE);
										//																deliveryTrackingInfoWsDTO.setDeliveryTrackingLocation(null != statusRecords
										//																		.getLocation() ? statusRecords.getLocation()
										//																		: MarketplacecommerceservicesConstants.NULL_VALUE);
										//																deliveryTrackingInfoWsDTO.setDeliveryTrackingDescription(null != statusRecords
										//																		.getStatusDescription() ? statusRecords.getStatusDescription()
										//																		: MarketplacecommerceservicesConstants.NULL_VALUE);
										//																LOG.debug("+++++++delivery Tracking details+++++++++" + deliveryTrackingInfoWsDTO);
										//																deliveryTrackingListInfoWsDTO.add(deliveryTrackingInfoWsDTO);
										//																customerOrderInfoWsDTO.setDeliverytrackingDetails(deliveryTrackingListInfoWsDTO);
										//																LOG.debug("-----delivery Tracking details in response inside for loop------"
										//																		+ customerOrderInfoWsDTO.getDeliverytrackingDetails());
										//															}
										//														}
										//														else
										//														{
										//															deliveryTrackingListInfoWsDTO.clear();//Removing pre stored values
										//															//final DeliveryTrackingInfoWsDTO deliveryTrackingInfoWsDTO = new DeliveryTrackingInfoWsDTO();
										//															LOG.debug("**********Inside else part of status records*********");
										//															deliveryTrackingInfoAllWsDTO
										//																	.setDeliveryTrackingDate(MarketplacecommerceservicesConstants.NULL_VALUE);
										//															deliveryTrackingInfoAllWsDTO
										//																	.setDeliveryTrackingLocation(MarketplacecommerceservicesConstants.NULL_VALUE);
										//															deliveryTrackingInfoAllWsDTO
										//																	.setDeliveryTrackingDescription(MarketplacecommerceservicesConstants.NULL_VALUE);
										//															deliveryTrackingListInfoWsDTO.add(deliveryTrackingInfoAllWsDTO);
										//															customerOrderInfoWsDTO.setDeliverytrackingDetails(deliveryTrackingListInfoWsDTO);
										//														}
										//													}
										//												}
										//												else
										//												{
										//													deliveryTrackingListInfoWsDTO.clear();//Removing pre stored values
										//													LOG.debug("**********Inside else part of awb response*********");
										//													//final DeliveryTrackingInfoWsDTO deliveryTrackingInfoWsDTO = new DeliveryTrackingInfoWsDTO();
										//													deliveryTrackingInfoAllWsDTO
										//															.setDeliveryTrackingDate(MarketplacecommerceservicesConstants.NULL_VALUE);
										//													deliveryTrackingInfoAllWsDTO
										//															.setDeliveryTrackingLocation(MarketplacecommerceservicesConstants.NULL_VALUE);
										//													deliveryTrackingInfoAllWsDTO
										//															.setDeliveryTrackingDescription(MarketplacecommerceservicesConstants.NULL_VALUE);
										//													deliveryTrackingListInfoWsDTO.add(deliveryTrackingInfoAllWsDTO);
										//													customerOrderInfoWsDTO.setDeliverytrackingDetails(deliveryTrackingListInfoWsDTO);
										//												}
										//											}
										//											else
										//											{
										//												deliveryTrackingListInfoWsDTO.clear();//Removing pre stored values
										//												LOG.debug("**********Inside else part of null tracking id*********");
										//												//final DeliveryTrackingInfoWsDTO deliveryTrackingInfoWsDTO = new DeliveryTrackingInfoWsDTO();
										//												deliveryTrackingInfoAllWsDTO
										//														.setDeliveryTrackingDate(MarketplacecommerceservicesConstants.NULL_VALUE);
										//												deliveryTrackingInfoAllWsDTO
										//														.setDeliveryTrackingLocation(MarketplacecommerceservicesConstants.NULL_VALUE);
										//												deliveryTrackingInfoAllWsDTO
										//														.setDeliveryTrackingDescription(MarketplacecommerceservicesConstants.NULL_VALUE);
										//												deliveryTrackingListInfoWsDTO.add(deliveryTrackingInfoAllWsDTO);
										//												customerOrderInfoWsDTO.setDeliverytrackingDetails(deliveryTrackingListInfoWsDTO);
										//											}
										customerOrderInfoWsDTO.setAwbNumber(null != cng.getTrackingID() ? cng.getTrackingID()
												: MarketplacecommerceservicesConstants.NULL_VALUE);//AWB number
										customerOrderInfoWsDTO.setReturnCarrier(null != cng.getReturnCarrier() ? cng.getReturnCarrier()
												: MarketplacecommerceservicesConstants.NULL_VALUE);//Return carrier
										customerOrderInfoWsDTO.setReturnAwbNumber(null != cng.getReturnAWBNum() ? cng.getReturnAWBNum()
												: MarketplacecommerceservicesConstants.NULL_VALUE);//Return AWB number
										customerOrderInfoWsDTO.setShippingStatus((null != cng.getShipmentStatus() ? cng.getShipmentStatus()
												: MarketplacecommerceservicesConstants.NULL_VALUE));//Shipping status
										if (cng.getShippingDate() != null)
										{
											customerOrderInfoWsDTO.setShippingTimestamp(formatter.format(cng.getShippingDate()));
										}
										else
										{
											customerOrderInfoWsDTO.setShippingTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
										}
										break;
									}
									else
									{
										customerOrderInfoWsDTO.setAwbNumber(MarketplacecommerceservicesConstants.NULL_VALUE);
										customerOrderInfoWsDTO.setReturnCarrier(MarketplacecommerceservicesConstants.NULL_VALUE);
										customerOrderInfoWsDTO.setReturnAwbNumber(MarketplacecommerceservicesConstants.NULL_VALUE);
										customerOrderInfoWsDTO.setShippingStatus(MarketplacecommerceservicesConstants.NULL_VALUE);
										customerOrderInfoWsDTO.setShippingTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
									}
								}
							}
							//											//break;
							//											consignmentFlag = true;
							//											LOG.debug("**********consignment flag set to true*************");
							//										}
							else
							{
								//											deliveryTrackingListInfoWsDTO.clear();//Removing pre stored values
								//											LOG.debug("**********Inside else part consignment not matching transaction id*********");
								//											//final DeliveryTrackingInfoWsDTO deliveryTrackingInfoWsDTO = new DeliveryTrackingInfoWsDTO();
								//											deliveryTrackingInfoAllWsDTO
								//													.setDeliveryTrackingDate(MarketplacecommerceservicesConstants.NULL_VALUE);
								//											deliveryTrackingInfoAllWsDTO
								//													.setDeliveryTrackingLocation(MarketplacecommerceservicesConstants.NULL_VALUE);
								//											deliveryTrackingInfoAllWsDTO
								//													.setDeliveryTrackingDescription(MarketplacecommerceservicesConstants.NULL_VALUE);
								//											deliveryTrackingListInfoWsDTO.add(deliveryTrackingInfoAllWsDTO);
								//											customerOrderInfoWsDTO.setDeliverytrackingDetails(deliveryTrackingListInfoWsDTO);
								customerOrderInfoWsDTO.setAwbNumber(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setReturnCarrier(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setReturnAwbNumber(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setShippingStatus(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setShippingTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
							}
							//									}
							//								}
							//							}
							//							else
							//							{
							//								LOG.debug("**********Inside else part consignment*********");
							//								final DeliveryTrackingInfoWsDTO deliveryTrackingInfoWsDTO = new DeliveryTrackingInfoWsDTO();
							//								deliveryTrackingInfoWsDTO.setDeliveryTrackingDate(MarketplacecommerceservicesConstants.NULL_VALUE);
							//								deliveryTrackingInfoWsDTO.setDeliveryTrackingLocation(MarketplacecommerceservicesConstants.NULL_VALUE);
							//								deliveryTrackingInfoWsDTO.setDeliveryTrackingDescription(MarketplacecommerceservicesConstants.NULL_VALUE);
							//								deliveryTrackingListInfoWsDTO.add(deliveryTrackingInfoWsDTO);
							//								customerOrderInfoWsDTO.setDeliverytrackingDetails(deliveryTrackingListInfoWsDTO);
							//								customerOrderInfoWsDTO.setAwbNumber(MarketplacecommerceservicesConstants.NULL_VALUE);
							//								customerOrderInfoWsDTO.setReturnCarrier(MarketplacecommerceservicesConstants.NULL_VALUE);
							//								customerOrderInfoWsDTO.setReturnAwbNumber(MarketplacecommerceservicesConstants.NULL_VALUE);
							//								customerOrderInfoWsDTO.setShippingStatus(MarketplacecommerceservicesConstants.NULL_VALUE);
							//								customerOrderInfoWsDTO.setShippingTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
							//							}
							//SDI-1193 ends
							if (StringUtils.isNotEmpty(orderModel.getModeOfOrderPayment()))
							{
								if (orderModel.getModeOfOrderPayment().equalsIgnoreCase(MarketplacecommerceservicesConstants.COD_PAYMENT))
								{
									customerOrderInfoWsDTO.setPaymentType(MarketplacecommerceservicesConstants.POSTPAID);
								}
								else
								{
									customerOrderInfoWsDTO.setPaymentType(MarketplacecommerceservicesConstants.PREPAID);
								}
							}
							else
							{
								customerOrderInfoWsDTO.setPaymentType(MarketplacecommerceservicesConstants.NULL_VALUE);
							}
							if (CollectionUtils.isNotEmpty(orderModel.getReturnRequests()))
							{
								LOG.debug("**********inside not empty return request*********");
								for (final ReturnRequestModel rq : orderModel.getReturnRequests())
								{
									LOG.debug("**********inside return request loop*********");
									if (!flag)
									{
										LOG.debug("**********inside flag equals to false*********");
										if (CollectionUtils.isNotEmpty(rq.getReturnEntries()))
										{
											for (final ReturnEntryModel rte : rq.getReturnEntries())
											{
												LOG.debug("**********inside return entry loop*********");
												if (rte.getOrderEntry().getTransactionID().equalsIgnoreCase(transactionId))
												{
													LOG.debug("**********fetching return request and return entry details for matching transaction id*********");
													customerOrderInfoWsDTO.setQcRejectionReason(null != rq.getRejectionReason() ? rq
															.getRejectionReason() : MarketplacecommerceservicesConstants.NULL_VALUE);//QC rejection reason
													LOG.debug("**********QC rejection reason setting done*********");
													customerOrderInfoWsDTO.setReturnType(null != rq.getTypeofreturn() ? rq.getTypeofreturn()
															.toString() : MarketplacecommerceservicesConstants.NULL_VALUE);//Type of Return
													LOG.debug("**********Type of Return setting done*********");
													customerOrderInfoWsDTO.setReturnRequestStatus(null != rte.getStatus() ? rte.getStatus()
															.toString() : MarketplacecommerceservicesConstants.NULL_VALUE);//Return request status
													LOG.debug("**********Return request status setting done*********");
													if (null != rte.getCreationtime())
													{
														customerOrderInfoWsDTO.setReturnRequestTimestamp(formatter.format((rte
																.getCreationtime())));//Return timeStamp
														LOG.debug("**********Return timeStamp setting done*********");
													}
													else
													{
														customerOrderInfoWsDTO
																.setReturnRequestTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
													}
													flag = true;
													LOG.debug("**********flag set to true*********");
												}
												else
												{
													LOG.debug("**********inside not matching transacion id of return entry*********");
													customerOrderInfoWsDTO
															.setReturnRequestStatus(MarketplacecommerceservicesConstants.NULL_VALUE);
													customerOrderInfoWsDTO
															.setReturnRequestTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
													customerOrderInfoWsDTO
															.setQcRejectionReason(MarketplacecommerceservicesConstants.NULL_VALUE);
													customerOrderInfoWsDTO.setReturnType(MarketplacecommerceservicesConstants.NULL_VALUE);
												}
											}
										}
										else
										{
											LOG.debug("**********inside else part of return entry loop*********");
											customerOrderInfoWsDTO.setReturnRequestStatus(MarketplacecommerceservicesConstants.NULL_VALUE);
											customerOrderInfoWsDTO
													.setReturnRequestTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
											customerOrderInfoWsDTO.setQcRejectionReason(MarketplacecommerceservicesConstants.NULL_VALUE);
											customerOrderInfoWsDTO.setReturnType(MarketplacecommerceservicesConstants.NULL_VALUE);

										}
									}
								}
							}
							else
							{
								LOG.debug("**********No return request and return entry details exist*********");
								customerOrderInfoWsDTO.setReturnRequestStatus(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setReturnRequestTimestamp(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setQcRejectionReason(MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setReturnType(MarketplacecommerceservicesConstants.NULL_VALUE);
							}
							LOG.debug("**********setting final dto*********");
							custdto.add(customerOrderInfoWsDTO);
							orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);
							break;
						}

					}
				}
			}
			else
			{
				orderInfoWsDTO.setError(MarketplacecommerceservicesConstants.TRANSACTION_ID_NOT_PRESENT);
				LOG.error("subOrderModel is null");
			}
			errorFlag = false;
		}
		catch (final NullPointerException e)
		{
			LOG.error("Null pointer Exception occured: ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured: ", e);
		}
		if (errorFlag)//Setting Error Status in case exception thrown
		{
			orderInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		LOG.debug("**********returning final dto*********");
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
		boolean errorFlag = true;
		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			if (null != orderModel)
			{
				orderInfoWsDTO.setOrderTotal(null != orderModel.getTotalPriceWithConv() ? orderModel.getTotalPriceWithConv()
						.toString() : MarketplacecommerceservicesConstants.NULL_VALUE);
				if (null != orderModel.getPaymentAddress() && StringUtils.isNotEmpty(orderModel.getPaymentAddress().getFirstname())
						&& (StringUtils.isNotEmpty(orderModel.getPaymentAddress().getLastname())))
				{
					custFirstName = new StringBuilder(orderModel.getPaymentAddress().getFirstname());
					custLastName = new StringBuilder(orderModel.getPaymentAddress().getLastname());
					orderInfoWsDTO.setCustName(custFirstName.append(MarketplacecommerceservicesConstants.EMPTY_SPACE)
							.append(custLastName).toString());
				}
				else if (null != orderModel.getDeliveryAddress()
						&& StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getFirstname())
						&& (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getLastname())))
				{
					custFirstName = new StringBuilder(orderModel.getDeliveryAddress().getFirstname());
					custLastName = new StringBuilder(orderModel.getDeliveryAddress().getLastname());
					orderInfoWsDTO.setCustName(custFirstName.append(MarketplacecommerceservicesConstants.EMPTY_SPACE)
							.append(custLastName).toString());
				}
				else if (StringUtils.isNotEmpty(orderModel.getPickupPersonName()))
				{
					orderInfoWsDTO.setCustName(orderModel.getPickupPersonName());
				}
				else
				{
					orderInfoWsDTO.setCustName(MarketplacecommerceservicesConstants.NULL_VALUE);
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
				else if (StringUtils.isNotEmpty(orderModel.getPickupPersonMobile()))
				{
					orderInfoWsDTO.setCustMobileNo(orderModel.getPickupPersonMobile());
				}
				else
				{
					orderInfoWsDTO.setCustMobileNo(MarketplacecommerceservicesConstants.NULL_VALUE);
				}
				if (null != orderModel.getCreationtime())
				{
					orderInfoWsDTO.setOrderDate(formatter.format(orderModel.getCreationtime()));
				}
				else
				{
					orderInfoWsDTO.setOrderDate(MarketplacecommerceservicesConstants.NULL_VALUE);
				}
				List<OrderModel> subOrderModel = new ArrayList<OrderModel>();

				List<AbstractOrderEntryModel> orderEntryModel = new ArrayList<AbstractOrderEntryModel>();
				//Fetching sub orders under parent order
				subOrderModel = orderModel.getChildOrders();
				if (CollectionUtils.isNotEmpty(subOrderModel))
				{
					for (final OrderModel subOrder : subOrderModel)
					{
						//Fetching all entries of all sub orders
						orderEntryModel = subOrder.getEntries();
						if (CollectionUtils.isNotEmpty(orderEntryModel))
						{
							for (final AbstractOrderEntryModel entry : orderEntryModel)
							{
								final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
								customerOrderInfoWsDTO.setApportionedPrice((null != entry.getNetAmountAfterAllDisc() ? entry
										.getNetAmountAfterAllDisc().toString() : MarketplacecommerceservicesConstants.NULL_VALUE));
								customerOrderInfoWsDTO.setSellerName(null != entry.getSellerInfo() ? entry.getSellerInfo()
										: MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setShippingType(null != entry.getFulfillmentType() ? entry.getFulfillmentType()
										.toString().toUpperCase() : MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setTransactionId(null != entry.getTransactionID() ? entry.getTransactionID()
										: MarketplacecommerceservicesConstants.NULL_VALUE);
								customerOrderInfoWsDTO.setProductName(null != entry.getProduct().getName() ? entry.getProduct().getName()
										: MarketplacecommerceservicesConstants.NULL_VALUE);
								if (entry.getExpectedDeliveryDate() != null)
								{
									customerOrderInfoWsDTO.setEdd(formatter.format(entry.getExpectedDeliveryDate()));
								}
								else
								{
									customerOrderInfoWsDTO.setEdd(MarketplacecommerceservicesConstants.NULL_VALUE);
								}
								if (null != entry.getMplDeliveryMode() && null != entry.getMplDeliveryMode().getDeliveryMode()
										&& StringUtils.isNotEmpty(entry.getMplDeliveryMode().getDeliveryMode().getCode()))
								{
									customerOrderInfoWsDTO.setShippingMode(entry.getMplDeliveryMode().getDeliveryMode().getCode());
								}
								else
								{
									customerOrderInfoWsDTO.setShippingMode(MarketplacecommerceservicesConstants.NULL_VALUE);
								}
								if (StringUtils.isNotEmpty(subOrder.getModeOfOrderPayment()))
								{
									if (subOrder.getModeOfOrderPayment()
											.equalsIgnoreCase(MarketplacecommerceservicesConstants.COD_PAYMENT))
									{
										customerOrderInfoWsDTO.setPaymentType(MarketplacecommerceservicesConstants.POSTPAID);
									}
									else
									{
										customerOrderInfoWsDTO.setPaymentType(MarketplacecommerceservicesConstants.PREPAID);
									}
								}
								else
								{
									customerOrderInfoWsDTO.setPaymentType(MarketplacecommerceservicesConstants.NULL_VALUE);
								}
								if (CollectionUtils.isNotEmpty(entry.getConsignmentEntries()))
								{
									for (final ConsignmentEntryModel consg : entry.getConsignmentEntries())
									{
										customerOrderInfoWsDTO.setOrderStatus(consg.getConsignment().getStatus().getCode());//Orderline status against each transaction
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
				//For payment pending order
				else
				{
					final CustomerOrderInfoWsDTO customerOrderInfoWsDTO = new CustomerOrderInfoWsDTO();
					customerOrderInfoWsDTO.setApportionedPrice(MarketplacecommerceservicesConstants.NULL_VALUE);
					customerOrderInfoWsDTO.setSellerName(MarketplacecommerceservicesConstants.NULL_VALUE);
					customerOrderInfoWsDTO.setShippingType(MarketplacecommerceservicesConstants.NULL_VALUE);
					customerOrderInfoWsDTO.setTransactionId(MarketplacecommerceservicesConstants.NULL_VALUE);
					customerOrderInfoWsDTO.setProductName(MarketplacecommerceservicesConstants.NULL_VALUE);
					customerOrderInfoWsDTO.setEdd(MarketplacecommerceservicesConstants.NULL_VALUE);
					customerOrderInfoWsDTO.setShippingMode(MarketplacecommerceservicesConstants.NULL_VALUE);
					customerOrderInfoWsDTO.setPaymentType(MarketplacecommerceservicesConstants.NULL_VALUE);
					customerOrderInfoWsDTO.setOrderStatus(null != orderModel.getStatus().getCode() ? orderModel.getStatus().getCode()
							: MarketplacecommerceservicesConstants.NULL_VALUE);
					custdto.add(customerOrderInfoWsDTO);
					orderInfoWsDTO.setCustomerOrderInfoWsDTO(custdto);
				}
			}
			else
			{
				orderInfoWsDTO.setError(MarketplacecommerceservicesConstants.ORDER_ID_NOT_PRESENT);
				LOG.error("suborderModel is null");
			}
			errorFlag = false;
		}
		catch (final NullPointerException e)
		{
			LOG.error("Null pointer Exception occured: ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured: ", e);
		}
		if (errorFlag)//Setting Error Status in case exception thrown
		{
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
