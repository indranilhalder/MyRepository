/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.OrderShortUrlInfoModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.data.AWBResponseData;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
//import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.order.facade.GetOrderDetailsFacade;
import com.tisl.mpl.shorturl.service.ShortUrlService;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.data.MplTrackOrderValidationData;
import com.tisl.mpl.storefront.web.forms.TrackOrderForm;
import com.tisl.mpl.storefront.web.forms.validator.TrackOrderFormValidator;
import com.tisl.mpl.util.ExceptionUtil;

/**
 * @author Techouts
 *
 *         Controller used for track order functionality for non-login users
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.ANONYMOUS_ORDER_TRACKING_URL)
public class TrackOrderPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(TrackOrderPageController.class);
	private static final String ORDER_DETAIL_CMS_PAGE = "order";
	public static final String PAGE_ROOT = "pages/";
	

	@Autowired
	private UserFacade userFacade;
	@Resource(name = "googleShortUrlService")
	private ShortUrlService googleShortUrlService;
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private MplOrderFacade mplOrderFacade;
	@Resource(name = "getOrderDetailsFacade")
	private GetOrderDetailsFacade getOrderDetailsFacade;
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	/*@Resource(name = "orderModelService")
	private OrderModelService orderModelService;*/
	@Autowired
	private ModelService modelService;
	@Autowired
	private MplOrderService mplOrderService;
	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;
	@Resource(name = "trackOrderFormValidator")
	private TrackOrderFormValidator trackOrderFormValidator;
	@Resource(name = "themeSource")
	private MessageSource messageSource;
	@Resource(name = "localeResolver")
	private LocaleResolver localeResolver;
	@Autowired
	UserService userService;

	/**
	 * @Description This method is used to update the TULShortUrlreportModel 
	 * @param orderCode
	 * @param request
	 * @return shortUrl
	 */
	@RequestMapping(value = RequestMappingUrlConstants.BEFORE_ANONYMOUS_USER_TRACK_URL, method = RequestMethod.GET)
	public String beforeShortOrderTrack(@PathVariable("orderId") final String orderCode, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		if(LOG.isDebugEnabled()){
			LOG.debug("In Before showing the track order :"+orderCode);
		}
		if (null == orderCode)
		{
			return REDIRECT_PREFIX + RequestMappingUrlConstants.LINK_404;
		}
		//Retrieve the short URL report model
		final OrderShortUrlInfoModel shortUrlReport = googleShortUrlService.getShortUrlReportModelByOrderId(orderCode);
		//PRDI-757 Removed anonymous order track
		//boolean isAnonymous = false;
		/*
		final OrderModel orderModel = mplOrderFacade.getOrderForAnonymousUser(orderCode);
		
		if(null != orderModel && null != orderModel.getUser()) {
			isAnonymous=userFacade.isAnonymousUser();
		}
		if(!isAnonymous) {
			if(orderModel.getUser().equals(userService.getCurrentUser())) 
			{
				isAnonymous = false;
			}else {
				isAnonymous = true;
			}
		}*/
		//final boolean isAnonymous = userFacade.isAnonymousUser();
		if (null != shortUrlReport)
		{
			LOG.debug("Short url report model is not NUll .So update it");
			int clicks = shortUrlReport.getClicks().intValue();
			clicks += 1;
			if(LOG.isDebugEnabled()){
				LOG.debug("No of clicks ===" + clicks);
			}
			shortUrlReport.setClicks(clicks);

			//if the user is logged in redirect the user to existing order detail page
			//else redirect to new non login short order details page
			//if (!isAnonymous)
			//{
			int noOfLoginClicks = shortUrlReport.getLogin().intValue();
			noOfLoginClicks += 1;
			LOG.debug("No of login clicks===" + noOfLoginClicks);
			shortUrlReport.setLogin(noOfLoginClicks);
			modelService.save(shortUrlReport);
			//return REDIRECT_PREFIX + RequestMappingUrlConstants.LOGIN_TRACKING_PAGE_URL + orderCode;

			//}
			//modelService.save(shortUrlReport);
		}
		//if (!isAnonymous)
		//{
		//	return REDIRECT_PREFIX + RequestMappingUrlConstants.LOGIN_TRACKING_PAGE_URL + orderCode;
		//}
		//return REDIRECT_PREFIX + RequestMappingUrlConstants.ANONYMOUS_TRACKING_PAGE_URL + orderCode;
		return REDIRECT_PREFIX + RequestMappingUrlConstants.LOGIN_TRACKING_PAGE_URL + orderCode;
	}

	/**
	 * @Description This Method used for showing the track order page data
	 * @param trackKey
	 * @param model
	 * @return trackOrderPage
	 */
	@RequestMapping(value = RequestMappingUrlConstants.TRACK_ORDER_DETAILS_PAGE_URL, method = RequestMethod.GET)
	public String shortOrderTrack(@RequestParam("trackKey") final String trackKey, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			final String orderCode = getOrderCode(trackKey);
			if(LOG.isDebugEnabled()){
				LOG.debug("In track order - orderCode ***"+orderCode);
			}
			//if the user is logged in redirect to exsiting account order detail page
			boolean isAnonymous = false;
			final OrderModel orderModel = mplOrderFacade.getOrderForAnonymousUser(orderCode);
			if(null != orderModel && null != orderModel.getUser()) {
				final CustomerModel custModel = (CustomerModel) orderModel.getUser();
				if (!validateTrackKey(custModel.getOriginalUid(), trackKey))
				{
					throw new IllegalArgumentException("Invalid track key: " + trackKey);
				}
				isAnonymous=userFacade.isAnonymousUser();
			}
			if(orderModel.getUser().equals(userService.getCurrentUser())) {
				isAnonymous = false;
			}else {
				isAnonymous = true;
			}
			if (!isAnonymous)
				{
				   	return REDIRECT_PREFIX + RequestMappingUrlConstants.LOGIN_TRACKING_PAGE_URL + orderCode;
				}
			
//			if (!userFacade.isAnonymousUser())
//			{
//				return REDIRECT_PREFIX + RequestMappingUrlConstants.LOGIN_TRACKING_PAGE_URL + orderCode;
//			}
			final OrderData orderDetail = mplCheckoutFacade.getOrderDetailsForAnonymousUser(orderCode);

			final Map<String, Map<String, List<AWBResponseData>>> trackStatusMap = new HashMap<>();
			final Map<String, String> currentStatusMap = new HashMap<>();
			String consignmentStatus = ModelAttributetConstants.EMPTY, formattedProductDate = ModelAttributetConstants.EMPTY, formattedActualProductDate = ModelAttributetConstants.EMPTY;

			final Map<String, String> formattedDeliveryDates = new HashMap<>();
			final Map<String, String> formattedActualDeliveryDates = new HashMap<>();
			final Map<String, String> trackStatusAWBMap = new HashMap<>();
			final Map<String, String> trackStatusLogisticMap = new HashMap<>();
			final Map<String, String> trackStatusReturnAWBMap = new HashMap<>();
			final Map<String, String> trackStatusReturnLogisticMap = new HashMap<>();
			final Map<String, String> trackStatusTrackingURLMap = new HashMap<>();

			final Map<String, Boolean> sortInvoice = new HashMap<>();
			ConsignmentModel consignmentModel = null;
			Map<String, List<AWBResponseData>> statusTrackMap = new HashMap<>();

			Map<String, String> fullfillmentDataMap = new HashMap<String, String>();

			final String finalOrderDate = getFormattedDate(orderDetail.getCreated());
			final List<OrderData> subOrderList = orderDetail.getSellerOrderList();

			for (final OrderData subOrder : subOrderList)
			{
				for (final OrderEntryData orderEntry : subOrder.getEntries())
				{
					final ProductModel productModel = mplOrderFacade.getProductForCode(orderEntry.getProduct().getCode());
					if (CollectionUtils.isNotEmpty(productModel.getBrands()))
					{
						for (final BrandModel brand : productModel.getBrands())
						{
							orderEntry.setBrandName(brand.getName());
							break;
						}
					}

					//Fetching invoice from consignment entries
					if (null != orderEntry.getConsignment() && orderEntry.getConsignment().getStatus() != null)
					{
						consignmentModel = mplOrderService.fetchConsignment(orderEntry.getConsignment().getCode());

						consignmentStatus = orderEntry.getConsignment().getStatus().getCode();
						if (null != consignmentModel.getInvoice()
								&& null != consignmentModel.getInvoice().getInvoiceUrl()
								&& (consignmentStatus.equalsIgnoreCase(ModelAttributetConstants.DELIVERED) || consignmentStatus
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_COLLECTED)))
						{
							sortInvoice.put(orderEntry.getTransactionId(), true);
							final String tranSactionId = orderEntry.getTransactionId();
							if (sortInvoice.containsKey(tranSactionId))
							{
								orderEntry.setShowInvoiceStatus(sortInvoice.get(tranSactionId));
							}
						}
					}

					final List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel
							.getSellerInformationRelator();

					for (final SellerInformationModel sellerInformationModel : sellerInfo)
					{
						if (sellerInformationModel.getSellerArticleSKU().equals(orderEntry.getSelectedUssid()))
						{
							final SellerInformationData sellerInfoData = new SellerInformationData();
							sellerInfoData.setSellername(sellerInformationModel.getSellerName());
							sellerInfoData.setUssid(sellerInformationModel.getSellerArticleSKU());
							orderEntry.setSelectedSellerInformation(sellerInfoData);

							statusTrackMap = getOrderDetailsFacade.getOrderStatusTrack(orderEntry, subOrder, orderDetail);
							trackStatusMap.put(orderEntry.getOrderLineId(), statusTrackMap);
							currentStatusMap.put(orderEntry.getOrderLineId(), consignmentStatus);
							if (consignmentModel != null)
							{
								formattedProductDate = getFormattedDate(consignmentModel.getEstimatedDelivery());
								formattedDeliveryDates.put(orderEntry.getOrderLineId(), formattedProductDate);
								formattedActualProductDate = getFormattedDate(consignmentModel.getDeliveryDate());
								formattedActualDeliveryDates.put(orderEntry.getOrderLineId(), formattedActualProductDate);

								trackStatusAWBMap.put(orderEntry.getOrderLineId(), consignmentModel.getTrackingID());
								trackStatusLogisticMap.put(orderEntry.getOrderLineId(), consignmentModel.getCarrier());
								trackStatusReturnAWBMap.put(orderEntry.getOrderLineId(), consignmentModel.getReturnAWBNum());
								trackStatusReturnLogisticMap.put(orderEntry.getOrderLineId(), consignmentModel.getReturnCarrier());
								trackStatusTrackingURLMap.put(orderEntry.getOrderLineId(), consignmentModel.getTrackingURL());
							}
						}
					}
				}
			}

			fullfillmentDataMap = mplCartFacade.getOrderEntryFullfillmentMode(orderDetail);
			model.addAttribute(ModelAttributetConstants.CART_FULFILMENTDATA, fullfillmentDataMap);
			model.addAttribute(ModelAttributetConstants.TRACK_STATUS, trackStatusMap);
			model.addAttribute(ModelAttributetConstants.CURRENT_STATUS, currentStatusMap);
			model.addAttribute(ModelAttributetConstants.ORDER_DELIVERY_DATE, formattedDeliveryDates);
			model.addAttribute(ModelAttributetConstants.ORDER_DELIVERY_DATE_ACTUAL, formattedActualDeliveryDates);
			model.addAttribute(ModelAttributetConstants.SUB_ORDER, orderDetail);
			model.addAttribute(ModelAttributetConstants.SUB_ORDER_STATUS, getOrderDetailsFacade.getPickUpButtonDisableOptions());
			model.addAttribute(ModelAttributetConstants.FILTER_DELIVERYMODE, mplOrderFacade.filterDeliveryMode());
			model.addAttribute(ModelAttributetConstants.ORDER_DATE_FORMATED, finalOrderDate);
			model.addAttribute(ModelAttributetConstants.AWBNUM, trackStatusAWBMap);
			model.addAttribute(ModelAttributetConstants.LOGISCTIC, trackStatusLogisticMap);
			model.addAttribute(ModelAttributetConstants.RETURN_AWBNUM, trackStatusReturnAWBMap);
			model.addAttribute(ModelAttributetConstants.RETURN_LOGISCTIC, trackStatusReturnLogisticMap);
			model.addAttribute(ModelAttributetConstants.TRACKINGURL, trackStatusTrackingURLMap);
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Exception::::TrackOrderPageController "+e.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final NoSuchMessageException e)
		{
			LOG.error(" NoSuchMessageException "+e.getMessage() +"In Tracking the trackKey 1 =="+trackKey);
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(" UnknownIdentifierException "+e.getMessage() +"In Tracking the trackKey 2 =="+trackKey);
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(" EtailBusinessExceptions "+e.getMessage() +"In Tracking the trackKey 3 =="+trackKey);
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(" EtailNonBusinessExceptions "+e.getMessage() +"In Tracking the trackKey 4 =="+trackKey);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final Exception e)
		{
			LOG.error(" Exception "+e.getMessage() +"In Tracking the trackKey 5 =="+trackKey);
			ExceptionUtil.getCustomizedExceptionTrace(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));

		return ControllerConstants.Views.Pages.Order.TrackOrderDetailsPage;
	}
	
	

	/**
	 * @Description This method used for validating the Non-Login User details
	 * @param orderCode
	 * @param emailId
	 * @param captchaCode
	 * @param request
	 * @return result
	 */

	@RequestMapping(value = RequestMappingUrlConstants.ANONYMOUS_TRACK_ORDER_VALIDATE_URL, method = RequestMethod.GET)
	@ResponseBody
	public MplTrackOrderValidationData trackOrderWihoutLogin(@RequestParam("orderCode") final String orderCode,
			@RequestParam("emailId") final String emailId, @RequestParam("captchaCode") final String captchaCode,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final MplTrackOrderValidationData resultData = new MplTrackOrderValidationData();
		try
		{
			final TrackOrderForm trackOrderForm = new TrackOrderForm();
			trackOrderForm.setOrderCode(orderCode);
			trackOrderForm.setEmailId(emailId);
			trackOrderForm.setCaptcha(captchaCode);
			LOG.debug("Track Order Form Field values***** " + trackOrderForm);
			//validate the input form fields using validator class
			final String result = trackOrderFormValidator.validate(trackOrderForm);

			if (!StringUtils.isEmpty(result) && !result.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
			{
				final String message = messageSource.getMessage(new DefaultMessageSourceResolvable(result),
						localeResolver.resolveLocale(request));
				resultData.setValidationResult(false);
				resultData.setErrorMessage(message);
				return resultData;
			}
			//if the result is success ,check for order id and email combination
			final OrderModel orderModel = mplOrderFacade.getOrderForAnonymousUser(orderCode);
			final UserModel userModel = orderModel.getUser();
			final CustomerModel custModel = (CustomerModel) userModel;
			if (null != custModel && null != custModel.getOriginalUid() && custModel.getOriginalUid().equals(emailId))
			{
				resultData.setValidationResult(true);
				resultData.setTrackKey(generatetrackKey(orderCode, custModel.getOriginalUid()));
				return resultData;
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error while tracking order for " + orderCode + " and " + emailId + " " + e.getMessage());
		   String message = messageSource.getMessage(new DefaultMessageSourceResolvable(
					MessageConstants.ORDERID_EMAILID_MISMATCH_MESSAGE_KEY), localeResolver.resolveLocale(request));
		   resultData.setValidationResult(false);
			resultData.setErrorMessage(message);
			return resultData;
		}
			String message = messageSource.getMessage(new DefaultMessageSourceResolvable(
			MessageConstants.ORDERID_EMAILID_MISMATCH_MESSAGE_KEY), localeResolver.resolveLocale(request));
			resultData.setValidationResult(false);
			resultData.setErrorMessage(message);
			return resultData;
	}

	/**
	 * @param fmtDate
	 * @return String
	 */
	private String getFormattedDate(final Date fmtDate)
	{

		String finalOrderDate = ModelAttributetConstants.EMPTY;
		if (fmtDate != null)
		{
			final Calendar cal = Calendar.getInstance();
			cal.setTime(fmtDate);
			final int year = cal.get(Calendar.YEAR);
			final int month = cal.get(Calendar.MONTH);
			final int day = cal.get(Calendar.DAY_OF_MONTH);

			final String strMonth = getMonthFromInt(month);
			String daySuffix = ModelAttributetConstants.SUFFIX_TH;
			if (String.valueOf(day).endsWith(ModelAttributetConstants.NUM_1))
			{
				if (!String.valueOf(day).equals(ModelAttributetConstants.NUM_11))
				{
					daySuffix = ModelAttributetConstants.SUFFIX_ST;
				}
			}
			else if (String.valueOf(day).endsWith(ModelAttributetConstants.NUM_2))
			{
				if (!String.valueOf(day).equals(ModelAttributetConstants.NUM_12))
				{
					daySuffix = ModelAttributetConstants.SUFFIX_ND;
				}
			}
			else if (String.valueOf(day).endsWith(ModelAttributetConstants.NUM_3))
			{
				if (!String.valueOf(day).equals(ModelAttributetConstants.NUM_13))
				{
					daySuffix = ModelAttributetConstants.SUFFIX_RD;
				}
			}
			else
			{
				daySuffix = ModelAttributetConstants.SUFFIX_TH;
			}

			finalOrderDate = strMonth + ModelAttributetConstants.SINGLE_SPACE + day + daySuffix + ModelAttributetConstants.COMMA
					+ ModelAttributetConstants.SINGLE_SPACE + year;
		}
		return finalOrderDate;
	}

	/**
	 * @param month
	 * @return String
	 */
	private String getMonthFromInt(final int month)
	{
		final List<String> months = Arrays.asList(ModelAttributetConstants.JANUARY, ModelAttributetConstants.FEBRUARY,
				ModelAttributetConstants.MARCH, ModelAttributetConstants.APRIL, ModelAttributetConstants.MAY,
				ModelAttributetConstants.JUNE, ModelAttributetConstants.JULY, ModelAttributetConstants.AUGUST,
				ModelAttributetConstants.SEPTEMBER, ModelAttributetConstants.OCTOBER, ModelAttributetConstants.NOVEMBER,
				ModelAttributetConstants.DECEMBER);
		final String strMonth = months.get(month);
		return strMonth;
	}
	
	private String generatetrackKey(final String orderCode, final String emailID)
	{
		if (null != orderCode && null != emailID)
		{
			final String hash = DigestUtils.md5Hex(orderCode.concat(emailID));
			return new String(Base64.encodeBase64((orderCode + ModelAttributetConstants.COLON + hash).getBytes()));
		}
		return null;
	}

	private boolean validateTrackKey(final String emailID, final String trackKey)
	{
		if (null != trackKey && null != emailID)
		{
			final String decodedString = new String(Base64.decodeBase64(trackKey.getBytes()));
			final String[] splitString = decodedString.split(ModelAttributetConstants.COLON);
			if (null != splitString && splitString.length > 1)
			{
				return DigestUtils.md5Hex(splitString[0].concat(emailID)).equals(splitString[1]) ? true : false;
			}
		}
		return false;
	}

	private String getOrderCode(final String trackKey)
	{
		if (null != trackKey)
		{
			final String decodedString = new String(Base64.decodeBase64(trackKey.getBytes()));
			final String[] splitString = decodedString.split(ModelAttributetConstants.COLON);
			if (null != splitString && splitString.length > 0)
			{
				return splitString[0];
			}
		}
		return null;
	}
}
