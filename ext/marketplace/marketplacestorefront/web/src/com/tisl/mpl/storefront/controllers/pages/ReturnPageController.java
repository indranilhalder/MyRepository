/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;


import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplReturnPickUpAddressInfoModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.data.CODSelfShipResponseData;
import com.tisl.mpl.data.RTSAndRSSReturnInfoRequestData;
import com.tisl.mpl.data.ReturnInfoData;
import com.tisl.mpl.data.ReturnLogisticsResponseData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.impl.MplCheckoutFacadeImpl;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.data.ReturnItemAddressData;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
//import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;
import com.tisl.mpl.storefront.web.forms.MplCRMTicketUpdateForm;
import com.tisl.mpl.storefront.web.forms.MplReturnInfoForm;
import com.tisl.mpl.storefront.web.forms.MplReturnsForm;
import com.tisl.mpl.storefront.web.forms.validator.MplAddressValidator;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.MplTimeconverUtility;


/**
 * @author TECHOUTS
 *
 *         Controller for returns page
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LINK_MY_ACCOUNT_RETRUNS)
public class ReturnPageController extends AbstractMplSearchPageController
{
	private static final String REDIRECT_MY_ACCOUNT = REDIRECT_PREFIX + "/my-account";
	private static final Logger LOG = Logger.getLogger(ReturnPageController.class);
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private CancelReturnFacade cancelReturnFacade;
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;
	@Resource(name = ModelAttributetConstants.CUSTOMER_FACADE)
	private CustomerFacade customerFacade;
	@Autowired
	private MplOrderFacade mplOrderFacade;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private MplAddressValidator mplAddressValidator;
	@Resource(name = ModelAttributetConstants.ACCOUNT_BREADCRUMB_BUILDER)
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;
	@Resource(name = ModelAttributetConstants.I18N_FACADE)
	private I18NFacade i18NFacade;
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	@Resource(name = ModelAttributetConstants.USER_FACADE)
	private UserFacade userFacade;

	@Autowired
	private MplConfigFacade mplConfigFacade;
	@Autowired
	private PincodeServiceFacade pincodeServiceFacade;
	@Autowired
	private MplCheckoutFacadeImpl mplCheckoutFacadeImpl;
	/*
	 * @Resource(name = "frontEndErrorHelper") private FrontEndErrorHelper frontEndErrorHelper;
	 */

	@Autowired
	private DateUtilHelper dateUtilHelper;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	ModelService modelService;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;

	public static final String RETURN_TYPE_COD = "01";

	private static final String RETURN_SUCCESS = "returnSuccess";
	private static final String RETURN_SUBMIT = "returnSubmit";

	/**
	 *
	 * @param returnForm
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws Exception
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_INITIATE_RETURN, method = RequestMethod.POST)
	@RequireHardLogIn
	public String initiateReturn(final MplReturnsForm returnForm, final Model model, final HttpServletRequest request,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, Exception
	{
		boolean cancellationStatus;
		LOG.info(returnForm);
		boolean quickdrop = false;
		final ReturnInfoData returnData = new ReturnInfoData();
		final ReturnItemAddressData returnAddrData = new ReturnItemAddressData();

		String productRichAttrOfQuickDrop = null;
		String sellerRichAttrOfQuickDrop = null;
		String ussid = "";
		boolean isFineJew = false;
		String sellerName = "";
		final String revSealSellerList = configurationService.getConfiguration().getString("finejewellery.reverseseal.sellername");

		try
		{
			OrderEntryData subOrderEntry = new OrderEntryData();
			//	final HttpSession session = request.getSession();
			final String orderCode = returnForm.getOrderCode();
			final String pinCode = returnForm.getPincode();
			sessionService.setAttribute("transactionId", returnForm.getTransactionId());
			sessionService.setAttribute("ussid", returnForm.getUssid());
			//	final String ussid = returnForm.getUssid();
			final String transactionId = returnForm.getTransactionId();
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			List<OrderEntryData> returnOrderEntry = new ArrayList<OrderEntryData>();
			final Map<String, List<OrderEntryData>> returnProductMap = new HashMap<>();
			final List<OrderEntryData> subOrderEntries = subOrderDetails.getEntries();
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			//TATA-823 Start
			final List<StateData> stateDataList = getAccountAddressFacade().getStates();
			final List<StateData> stateDataListNew = getFinalStateList(stateDataList);
			//TATA-823 end
			//TPR-5954
			ProductModel productModel = null;
			String L2Cat = null;
			for (final OrderEntryData entry : subOrderEntries)
			{
				if (entry.getTransactionId().equalsIgnoreCase(transactionId))
				{
					subOrderEntry = entry;
					returnOrderEntry = cancelReturnFacade.associatedEntriesData(orderModelService.getOrder(orderCode), transactionId);
					returnProductMap.put(subOrderEntry.getTransactionId(), returnOrderEntry);

					productModel = mplOrderFacade.getProductForCode(entry.getProduct().getCode());
					List<RichAttributeModel> productRichAttributeModel = null;
					if (null != productModel && productModel.getRichAttribute() != null)
					{
						productRichAttributeModel = (List<RichAttributeModel>) productModel.getRichAttribute();
						if (productRichAttributeModel != null && !productRichAttributeModel.isEmpty()
								&& productRichAttributeModel.get(0).getReturnAtStoreEligible() != null)
						{
							productRichAttrOfQuickDrop = productRichAttributeModel.get(0).getReturnAtStoreEligible().toString();
						}
					}

					if (productModel.getProductCategoryType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))

					{ //SellerInformationModel sellerInfoModel = null;
						isFineJew = true;
						final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(entry
								.getSelectedUssid());
						ussid = (CollectionUtils.isNotEmpty(jewelleryInfo)) ? jewelleryInfo.get(0).getPCMUSSID() : "";

						LOG.debug("PCMUSSID FOR JEWELLERY :::::::::: " + "for " + entry.getSelectedUssid() + " is "
								+ jewelleryInfo.get(0).getPCMUSSID());
					}
					else
					{
						ussid = entry.getSelectedUssid();
					}

					final List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel
							.getSellerInformationRelator();

					for (final SellerInformationModel sellerInformationModel : sellerInfo)
					{
						/* if (sellerInformationModel.getSellerArticleSKU().equals(entry.getSelectedUssid())) */
						if (sellerInformationModel.getSellerArticleSKU().equals(ussid))
						{
							List<RichAttributeModel> sellerRichAttributeModel = null;
							if (sellerInformationModel.getRichAttribute() != null)
							{
								sellerRichAttributeModel = (List<RichAttributeModel>) sellerInformationModel.getRichAttribute();
								if (sellerRichAttributeModel != null && !sellerRichAttributeModel.isEmpty()
										&& sellerRichAttributeModel.get(0).getReturnAtStoreEligible() != null)
								{
									sellerRichAttrOfQuickDrop = sellerRichAttributeModel.get(0).getReturnAtStoreEligible().toString();
								}
							}
							sellerName = sellerInformationModel.getSellerName();
						}
					}
					model.addAttribute(ModelAttributetConstants.QUCK_DROP_PROD_LEVEL, productRichAttrOfQuickDrop);
					model.addAttribute(ModelAttributetConstants.QUCK_DROP_SELLER_LEVEL, sellerRichAttrOfQuickDrop);
					break;
				}
				boolean returnLogisticsAvailability = false;
				if (!(entry.isGiveAway() || entry.isIsBOGOapplied()))
				{
					returnLogisticsAvailability = true;
				}
				model.addAttribute(ModelAttributetConstants.RETURNLOGAVAIL, returnLogisticsAvailability);
			}


			//TPR-5954 || Category specific return reason || Start
			Collection<CategoryModel> superCategories = productModel.getSupercategories();

			outer: for (final CategoryModel category : superCategories)
			{
				if (category.getCode().startsWith("MPH"))
				{
					superCategories = category.getSupercategories();
					for (final CategoryModel category1 : superCategories)
					{
						if (category1.getCode().startsWith("MPH"))
						{
							superCategories = category1.getSupercategories();
							for (final CategoryModel category2 : superCategories)
							{
								if (category2.getCode().startsWith("MPH"))
								{
									L2Cat = category2.getCode();
									break outer;
								}
							}
						}
					}

				}
			}
			//TPR-5954 || Category specific return reason || End
			List<ReturnReasonData> reasonDataList = null;
			reasonDataList = mplOrderFacade.getCatSpecificRetReason(L2Cat);
			if (null != reasonDataList)
			{
				model.addAttribute(ModelAttributetConstants.REASON_DATA_LIST, reasonDataList);
				//TPR-5954
				final String reasonDesc = mplOrderFacade.fetchReasonDesc(returnForm.getReturnReason());
				if (null != reasonDesc)
				{
					model.addAttribute(ModelAttributetConstants.REASON_DESCRIPTION, reasonDesc);
				}
			}
			else
			{ //Fall back for return reason code
				reasonDataList = mplOrderFacade.getReturnReasonForOrderItem();
				if (!reasonDataList.isEmpty())
				{
					for (final ReturnReasonData reason : reasonDataList)
					{
						if (null != reason.getCode() && reason.getCode().equalsIgnoreCase(returnForm.getReturnReason()))
						{
							model.addAttribute(ModelAttributetConstants.REASON_DESCRIPTION, reason.getReasonDescription());
						}
					}
				}

				model.addAttribute(ModelAttributetConstants.REASON_DATA_LIST, reasonDataList);
			}
			if (null != returnForm.getSubReturnReason())
			{
				model.addAttribute(ModelAttributetConstants.SUB_REASON,
						cancelReturnFacade.fetchSubReasonDesc(returnForm.getSubReturnReason()));
			}
			if (null != returnForm.getComments())
			{
				model.addAttribute(ModelAttributetConstants.NEW_COMMENTS, returnForm.getComments());
			}
			//JWLSPCUAT-282
			model.addAttribute(ModelAttributetConstants.ORDERCODE, orderCode);
			//if logistic partner not available for the given pin code
			model.addAttribute(ModelAttributetConstants.PINCODE_NOT_SERVICEABLE,
					MarketplacecommerceservicesConstants.REVERCE_LOGISTIC_PINCODE_SERVICEABLE_NOTAVAIL_MESSAGE);
			model.addAttribute(ModelAttributetConstants.RETURN_FORM, returnForm);
			model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, new AccountAddressForm());
			model.addAttribute(ModelAttributetConstants.SUB_ORDER, subOrderDetails);
			model.addAttribute(ModelAttributetConstants.REFUNDTYPE, returnForm.getRefundType());
			model.addAttribute(ModelAttributetConstants.RETURN_PRODUCT_MAP, returnProductMap);
			model.addAttribute(ModelAttributetConstants.SUBORDER_ENTRY, subOrderEntry);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataListNew);
			//get available time slots for return pickup
			final List<String> timeSlots = mplConfigFacade.getDeliveryTimeSlots(ModelAttributetConstants.RETURN_SLOT_TYPE);

			model.addAttribute(ModelAttributetConstants.SCHEDULE_TIMESLOTS, timeSlots);

			model.addAttribute(ModelAttributetConstants.ADDRESS_DATA,
					mplCheckoutFacadeImpl.rePopulateDeliveryAddress(getAccountAddressFacade().getAddressBook()));
			List<PointOfServiceData> returnableStores = new ArrayList<PointOfServiceData>();

			final String sellerId = subOrderEntry.getSelectedUssid().substring(0, 6);
			String pincode;
			if (subOrderEntry.getDeliveryPointOfService() != null && subOrderEntry.getDeliveryPointOfService().getAddress() != null)
			{
				pincode = subOrderEntry.getDeliveryPointOfService().getAddress().getPostalCode();
				returnableStores = pincodeServiceFacade.getAllReturnableStores(subOrderEntry.getDeliveryPointOfService().getAddress()
						.getPostalCode(), sellerId);
			}
			else
			{
				pincode = subOrderDetails.getDeliveryAddress().getPostalCode();
				returnableStores = pincodeServiceFacade.getAllReturnableStores(subOrderDetails.getDeliveryAddress().getPostalCode(),
						sellerId);
			}

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Bellow Store were eligible for Return ");
				if (CollectionUtils.isNotEmpty(returnableStores))
				{
					for (final PointOfServiceData pointOfServiceData : returnableStores)
					{
						LOG.debug(pointOfServiceData.getDisplayName());
					}
				}
				else
				{
					LOG.debug("could not found Returnable Store for the seller Id :" + sellerId + "Pincode :" + pincode);
				}
			}

			try
			{
				//get next available schedule return pickup dates for order entry
				final List<String> returnableDates = cancelReturnFacade.getReturnableDates(subOrderEntry);

				model.addAttribute(ModelAttributetConstants.RETURN_DATES, returnableDates);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error(e);
				ExceptionUtil.etailNonBusinessExceptionHandler(e);

			}
			catch (final Exception e)
			{
				LOG.error(e);
				ExceptionUtil.getCustomizedExceptionTrace(e);

			}


			model.addAttribute(ModelAttributetConstants.RETURNABLE_SLAVES, returnableStores);
			if (isFineJew && StringUtils.isNotEmpty(revSealSellerList))
			{
				final List<String> sellerList = Arrays.asList(revSealSellerList.split(","));
				//Checking if seller contains the values
				if (sellerList.contains(sellerName))
				{
					model.addAttribute(ModelAttributetConstants.SHOW_REVERSESEAL_JWLRY, "true");
				}
			}

			//for schedule pickup
			if (StringUtils.isNotBlank(returnForm.getReturnMethod())
					&& MarketplacecommerceservicesConstants.RETURN_SCHEDULE.equalsIgnoreCase(returnForm.getReturnMethod()))
			{

				boolean returnLogisticsCheck = true;
				String returnFulfillmentType = null;
				final List<ReturnLogisticsResponseData> returnLogisticsRespList = cancelReturnFacade.checkReturnLogistics(
						subOrderDetails, pinCode);
				if (CollectionUtils.isNotEmpty(returnLogisticsRespList))
				{
					//CustomerData customerData=customerFacade.getCurrentCustomer();
					for (final ReturnLogisticsResponseData responeData : returnLogisticsRespList)
					{
						final MplReturnPickUpAddressInfoModel mplReturnReport = modelService
								.create(MplReturnPickUpAddressInfoModel.class);
						mplReturnReport.setOrderId(responeData.getOrderId());
						mplReturnReport.setPincode(pinCode);
						mplReturnReport.setTransactionId(responeData.getTransactionId());
						mplReturnReport.setCustomerId(customerData.getUid());
						if (StringUtils.isNotEmpty(responeData.getIsReturnLogisticsAvailable())
								&& responeData.getIsReturnLogisticsAvailable().equalsIgnoreCase("Y"))
						{
							mplReturnReport.setStatus("Sucess");
						}
						else
						{
							mplReturnReport.setStatus("Fail");
						}

						modelService.save(mplReturnReport);
					}
				}


				for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
				{
					model.addAttribute(ModelAttributetConstants.PINCODE_NOT_SERVICEABLE, response.getResponseMessage());
					if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase(ModelAttributetConstants.N_CAPS_VAL))
					{
						returnLogisticsCheck = false;
						//returnFulfillmentType = response.getReturnFulfillmentType();
					}
					else if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase(ModelAttributetConstants.Y_CAPS_VAL))
					{
						returnFulfillmentType = response.getReturnFulfillmentType();
					}
				}
				storeContentPageTitleInModel(model, MessageConstants.RETURN_REQUEST);
				storeCmsPageInModel(model, getContentPageForLabelOrId(RETURN_SUBMIT));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(RETURN_SUBMIT));

				if (!returnLogisticsCheck)
				{
					GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER,
							ModelAttributetConstants.LPNOTAVAILABLE_ERRORMSG, null);
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							ModelAttributetConstants.LPNOTAVAILABLE_ERRORMSG);
					model.addAttribute("disableRsp", Boolean.TRUE);
					return ControllerConstants.Views.Pages.Account.AccountOrderReturnPincodeServiceCheck;
				}


				final List<String> times = MplTimeconverUtility.splitTime(returnForm.getScheduleReturnTime());

				String timeSlotFrom = null;
				String timeSlotto = null;
				for (final String time : times)
				{
					if (null == timeSlotFrom)
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Return Pickup Slot From Time :" + timeSlotFrom + " for the TransactionId :"
									+ returnForm.getTransactionId());
						}
						timeSlotFrom = time;
					}
					else
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Return Pickup Slot From Time :" + timeSlotto + " for the TransactionId :"
									+ returnForm.getTransactionId());
						}
						timeSlotto = time;
					}

				}

				final String returnPickupDate = returnForm.getScheduleReturnDate();
				returnData.setReasonCode(returnForm.getReturnReason());
				if (returnForm.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
				{
					returnData.setRefundType(MarketplacecommerceservicesConstants.N);
				}
				else
				{
					returnData.setRefundType(MarketplacecommerceservicesConstants.S);
				}
				returnData.setReturnPickupDate(dateUtilHelper.convertDateWithFormat(returnPickupDate));
				returnData.setTicketTypeCode(MarketplacecommerceservicesConstants.RETURN_TYPE);
				returnData.setTimeSlotFrom(timeSlotFrom);
				returnData.setTimeSlotTo(timeSlotto);
				returnData.setUssid(returnForm.getUssid());
				returnData.setReturnMethod(returnForm.getReturnMethod());
				returnData.setReturnFulfillmentMode(returnFulfillmentType);
				//TPR-5954
				if (null != returnForm.getComments())
				{
					returnData.setComments(returnForm.getComments());
				}
				if (null != returnForm.getSubReturnReason())
				{
					returnData.setSubReasonCode(returnForm.getSubReturnReason());
				}
				//				if (null != returnForm.getImagePath())
				//				{
				//					returnData.getImageUrl();
				//				}

				// TPR-4134
				if (null != returnForm.getReverseSeal())
				{
					returnData.setReverseSealLostflag(returnForm.getReverseSeal());
				}
				returnAddrData.setAddressLane1(returnForm.getAddrLine1());
				returnAddrData.setAddressLane2(returnForm.getAddrLine2());
				returnAddrData.setAddressLine3(returnForm.getAddrLine3());
				returnAddrData.setLandmark(returnForm.getLandMark());
				returnAddrData.setCity(returnForm.getCity());
				returnAddrData.setCountry(returnForm.getCountry());
				returnAddrData.setFirstName(returnForm.getFirstName());
				returnAddrData.setLastName(returnForm.getLastName());
				returnAddrData.setMobileNo(returnForm.getPhoneNumber());
				//TATA-823 Start
				if (!StringUtils.isEmpty(returnForm.getState()))
				{
					for (final StateData state : stateDataListNew)
					{
						if (state.getName().equalsIgnoreCase(returnForm.getState()))
						{
							returnAddrData.setState(state.getCode());
							break;
						}
					}
				}
				//	TATA-823 Close
				returnAddrData.setPincode(returnForm.getPincode());

				if (returnForm.getRefundType().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_TYPE))
				{
					cancellationStatus = cancelReturnFacade.implementReturnItem(subOrderDetails, subOrderEntry, returnData,
							customerData, SalesApplication.WEB, returnAddrData);
				}
				else
				{
					cancellationStatus = cancelReturnFacade.implementCancelOrReturn(subOrderDetails, subOrderEntry,
							returnForm.getReturnReason(), returnForm.getUssid(), returnForm.getRefundType(), customerData,
							returnForm.getReturnMethod(), true, SalesApplication.WEB);
				}

				if (!cancellationStatus)
				{
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							ModelAttributetConstants.RETURN_ERRORMSG);
					return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
				}
				//TISPRDT-2546
				if (isFineJew)
				{
					final List<ReturnRequestModel> returnRequestModelList = cancelReturnFacade.getListOfReturnRequest(orderCode);
					if (null != returnRequestModelList && returnRequestModelList.size() > 0)
					{
						for (final ReturnRequestModel mm : returnRequestModelList)
						{
							for (final ReturnEntryModel mmmodel : mm.getReturnEntries())
							{
								if (subOrderEntry.getTransactionId().equalsIgnoreCase(mmmodel.getOrderEntry().getTransactionID()))
								{
									if (null != mm.getTypeofreturn() && null != mm.getTypeofreturn().getCode())
									{
										if (mm.getTypeofreturn().getCode()
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.SELF_COURIER))
										{
											model.addAttribute(ModelAttributetConstants.FINEJWLRY_SELFCOURIER_ERROR, true);
										}
									}
								}
							}

						}
					}
				}
			}

			//for quick drop
			if (returnForm.getReturnMethod().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_METHOD_QUICKDROP))
			{
				quickdrop = true;
				final RTSAndRSSReturnInfoRequestData infoRequestData = new RTSAndRSSReturnInfoRequestData();
				final OrderModel orderModel = mplOrderFacade.getOrder(returnForm.getOrderCode());
				final List<String> stores = Arrays.asList(returnForm.getStoreIds());
				if (null != orderModel.getParentReference())
				{
					infoRequestData.setOrderId(orderModel.getParentReference().getCode());
				}
				else
				{
					infoRequestData.setOrderId(returnForm.getOrderCode());
				}
				infoRequestData.setRTSStore(stores);
				infoRequestData.setTransactionId(transactionId);
				infoRequestData.setReturnType(MarketplacecommerceservicesConstants.RETURN_TYPE_RTS);
				//return info call to OMS
				cancelReturnFacade.retrunInfoCallToOMS(infoRequestData);
				model.addAttribute(MarketplacecommerceservicesConstants.RETURN_METHOD_QUICKDROP, quickdrop);
			}

			/*
			 * if(returnForm.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y)) { CODSelfShipData
			 * selfShipData=new CODSelfShipData(); final OrderModel orderModel =
			 * orderModelService.getParentOrder(orderCode); if(null!=orderModel.getParentReference() &&
			 * null!=orderModel.getParentReference().getCode()){
			 * selfShipData.setOrderRefNo(orderModel.getParentReference().getCode()); }
			 * selfShipData.setCustomerNumber(customerData.getUid()); selfShipData.setTitle(returnForm.getTitle());
			 * selfShipData.setName(returnForm.getAccountHolderName());
			 * selfShipData.setBankAccount(returnForm.getAccountNumber());
			 * selfShipData.setBankName(returnForm.getBankName()); selfShipData.setBankKey(returnForm.getiFSCCode());
			 * selfShipData.setOrderNo(returnForm.getOrderCode());
			 * selfShipData.setTransactionID(returnForm.getTransactionId());
			 * selfShipData.setPaymentMode(returnForm.getRefundMode()); selfShipData.setTransactionType("01"); if(null!=
			 * subOrderDetails.getCreated()){ SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			 * selfShipData.
			 * setOrderDate(dateUtilHelper.convertDateWithFormat(formatter.format(subOrderDetails.getCreated())));
			 * selfShipData
			 * .setTransactionDate(dateUtilHelper.convertDateWithFormat(formatter.format(subOrderDetails.getCreated()))); }
			 * selfShipData.setTransactionType(RETURN_TYPE_COD); if(null != returnForm.getIsCODorder() &&
			 * returnForm.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y) ) { //set ordertag
			 * POSTPAIDRRF for COD orders
			 * selfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_POSTPAID); } else { //set
			 * ordertag POSTPAIDRRF for PREPAID orders
			 * selfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_PREPAID); }
			 */

			//for self Courier
			if (returnForm.getReturnMethod().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_SELF))
			{
				LOG.debug(" returnForm>>>>>>>>>>>>>>>>>>>>>>>>>>>>: " + returnForm.toString());
				final ReturnInfoData returnInfoDataObj = new ReturnInfoData();
				returnInfoDataObj.setTicketTypeCode(MarketplacecommerceservicesConstants.RETURN_TYPE);
				returnInfoDataObj.setReasonCode(returnForm.getReturnReason());
				returnInfoDataObj.setUssid(returnForm.getUssid());
				returnInfoDataObj.setReturnMethod(returnForm.getReturnMethod());
				//TPR-5954
				if (null != returnForm.getComments())
				{
					returnInfoDataObj.setComments(returnForm.getComments());
				}
				if (null != returnForm.getSubReturnReason())
				{
					returnInfoDataObj.setSubReasonCode(returnForm.getSubReturnReason());
				}
				//				if (null != returnForm.getImagePath())
				//				{
				//					returnInfoDataObj.setImageUrl(returnForm.getImagePath());
				//				}
				final boolean cancellationStatusForSelfShip = cancelReturnFacade.implementReturnItem(subOrderDetails, subOrderEntry,
						returnInfoDataObj, customerData, SalesApplication.WEB, returnAddrData);
				if (!cancellationStatusForSelfShip)
				{
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							ModelAttributetConstants.RETURN_ERRORMSG);
					return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
				}
				else
				{
					//RETURN_METHOD = "returnMethod";
					model.addAttribute(ModelAttributetConstants.RETURN_METHOD, returnForm.getReturnMethod());
				}

			}

			try
			{
				//insert or update Customer Bank Details
				if (null != returnForm.getIsCODorder()
						&& returnForm.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
				{
					final CODSelfShipData selfShipData = sendBankInformationToFico(customerData, returnForm, orderCode,
							subOrderDetails);
					cancelReturnFacade.insertUpdateCustomerBankDetails(selfShipData);
					cancelReturnFacade.codPaymentInfoToFICO(selfShipData);
				}

			}
			catch (final Exception e)
			{
				LOG.error("Exception Occured during saving Customer BankDetails for COD order" + orderCode + e.getMessage());
			}


			storeCmsPageInModel(model, getContentPageForLabelOrId(RETURN_SUCCESS));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(RETURN_SUCCESS));
			return ControllerConstants.Views.Pages.Account.AccountReturnSuccessPage;

		}
		catch (final EtailNonBusinessExceptions e)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.RETURN_ERRORMSG);
			LOG.error(e);
			return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
		}
		catch (final Exception e)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.RETURN_ERRORMSG);
			LOG.error(e);
			return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
		}


	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UPDATE_RETURNINFO, method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateReturnInfo(final MplReturnInfoForm mplReturnInfoForm, final Model model, final HttpServletRequest request,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, Exception
	{
		try
		{
			//to DO Implementeation
			final MultipartFile filename = mplReturnInfoForm.getDispatchProof();
			LOG.debug("***************:" + filename.getOriginalFilename());
			String fileUploadLocation = null;
			String shipmentCharge = null;
			String typeofReturn = null;
			String date = null;
			Path path = null;
			//TISRLUAT-50
			Double configShippingCharge = 0.0d;
			if (null != configurationService)
			{
				fileUploadLocation = configurationService.getConfiguration().getString(RequestMappingUrlConstants.FILE_UPLOAD_PATH);
				shipmentCharge = configurationService.getConfiguration().getString(RequestMappingUrlConstants.SHIPMENT_CHARGE_AMOUNT);
				typeofReturn = configurationService.getConfiguration().getString(RequestMappingUrlConstants.TYPE_OF_RETURN_FOR_RSS);
				if (null != shipmentCharge && !shipmentCharge.isEmpty())
				{
					configShippingCharge = Double.parseDouble(shipmentCharge);
				}

				if (null != fileUploadLocation && !fileUploadLocation.isEmpty())
				{
					try
					{
						final byte barr[] = filename.getBytes();
						final SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
						date = sdf.format(new Date());
						path = Paths.get(fileUploadLocation + File.separator + date);
						//if directory exists?
						if (!Files.exists(path))
						{
							try
							{
								Files.createDirectories(path);
							}
							catch (final IOException e)
							{
								//fail to create directory
								LOG.error("Exception ,While creating the Directory " + e.getMessage());
							}
						}
						final BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + File.separator
								+ typeofReturn + mplReturnInfoForm.getTransactionId() + filename.getOriginalFilename()));
						bout.write(barr);
						bout.flush();
						bout.close();
						LOG.debug("FileUploadLocation   :" + fileUploadLocation);
					}
					catch (final Exception e)
					{
						LOG.error("Exception is:" + e);
					}
				}
			}
			final RTSAndRSSReturnInfoRequestData returnInfoRequestData = new RTSAndRSSReturnInfoRequestData();
			returnInfoRequestData.setAWBNum(mplReturnInfoForm.getAwbNumber());
			returnInfoRequestData.setLPNameOther(mplReturnInfoForm.getLpname());
			final OrderModel orderModel = orderModelService.getParentOrder(mplReturnInfoForm.getOrderId());
			/*
			 * if(null!=orderModel.getParentReference() && null!=orderModel.getParentReference().getCode()){
			 * returnInfoRequestData.setOrderId(orderModel.getParentReference().getCode()); }
			 */
			returnInfoRequestData.setOrderId(mplReturnInfoForm.getOrderId());
			returnInfoRequestData.setTransactionId(mplReturnInfoForm.getTransactionId());
			if (mplReturnInfoForm.getAmount() != null && !mplReturnInfoForm.getAmount().isEmpty())
			{
				final Double enterdShppingCharge = Double.parseDouble(mplReturnInfoForm.getAmount());
				if (enterdShppingCharge.doubleValue() < configShippingCharge.doubleValue())
				{
					returnInfoRequestData.setShipmentCharge(mplReturnInfoForm.getAmount());
				}
				else
				{
					returnInfoRequestData.setShipmentCharge(String.valueOf(configShippingCharge));
				}
			}

			LOG.debug("Final Path for ShipmentProofURL :" + path + File.separator + typeofReturn
					+ mplReturnInfoForm.getTransactionId() + filename.getOriginalFilename());
			//returnInfoRequestData.setShipmentProofURL(fileUploadLocation+File.separator+filename.getOriginalFilename());
			returnInfoRequestData.setShipmentProofURL(path + File.separator + typeofReturn + mplReturnInfoForm.getTransactionId()
					+ filename.getOriginalFilename());
			returnInfoRequestData.setLogisticsID(mplReturnInfoForm.getLpNameOther());
			returnInfoRequestData.setReturnType(RequestMappingUrlConstants.RSS);


			cancelReturnFacade.retrunInfoCallToOMS(returnInfoRequestData);



			final CustomerData customerData = customerFacade.getCurrentCustomer();
			CODSelfShipData codSelfShipData = null;
			try
			{

				if (null != customerData)
				{
					codSelfShipData = cancelReturnFacade.getCustomerBankDetailsByCustomerId(customerData.getUid());
				}
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error("Exception occured for fecting CUstomer Bank details for customer ID :" + customerData.getUid()
						+ " Actual Stack trace " + e);
			}
			try
			{
				final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(mplReturnInfoForm.getOrderId());

				final CODSelfShipData finalCODSelfShipData = new CODSelfShipData();
				if (null != codSelfShipData)
				{
					finalCODSelfShipData.setTitle(codSelfShipData.getTitle());
					finalCODSelfShipData.setName(codSelfShipData.getName());
					finalCODSelfShipData.setBankKey(codSelfShipData.getBankKey());
					finalCODSelfShipData.setBankName(codSelfShipData.getBankName());
					finalCODSelfShipData.setBankAccount(codSelfShipData.getBankAccount());
				}
				else
				{
					finalCODSelfShipData.setTitle(MarketplacecommerceservicesConstants.NA);
					finalCODSelfShipData.setName(MarketplacecommerceservicesConstants.NA);
					finalCODSelfShipData.setBankAccount(MarketplacecommerceservicesConstants.ZERO);
					finalCODSelfShipData.setBankName(MarketplacecommerceservicesConstants.NA);
					finalCODSelfShipData.setBankKey(MarketplacecommerceservicesConstants.NA);
				}

				if (subOrderDetails.getMplPaymentInfo().getPaymentOption().equalsIgnoreCase(RequestMappingUrlConstants.COD))
				{
					finalCODSelfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_POSTPAID);
				}
				else
				{
					finalCODSelfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_PREPAID);
				}

				final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				finalCODSelfShipData.setAmount(returnInfoRequestData.getShipmentCharge());
				finalCODSelfShipData.setOrderRefNo(mplReturnInfoForm.getOrderId());
				finalCODSelfShipData
						.setOrderDate(dateUtilHelper.convertDateWithFormat(formatter.format(subOrderDetails.getCreated())));
				finalCODSelfShipData.setTransactionDate(dateUtilHelper.convertDateWithFormat(formatter.format(subOrderDetails
						.getCreated())));
				finalCODSelfShipData.setTransactionID(mplReturnInfoForm.getTransactionId());
				finalCODSelfShipData.setTransactionType(RequestMappingUrlConstants.RETURN_TYPE);
				//finalCODSelfShipData.setOrderRefNo(mplReturnInfoForm.getTransactionId());
				// finalCODSelfShipData.setOrderNo();
				if (orderModel.getChildOrders().size() > 0)
				{
					for (final OrderModel chileOrders : orderModel.getChildOrders())
					{
						for (final AbstractOrderEntryModel entry : chileOrders.getEntries())
						{
							if (entry.getTransactionID().equalsIgnoreCase(mplReturnInfoForm.getTransactionId()))
							{
								finalCODSelfShipData.setOrderNo(chileOrders.getCode());
							}
						}
					}

				}
				if (null != orderModel.getParentReference() && null != orderModel.getParentReference().getCode())
				{
					finalCODSelfShipData.setOrderRefNo(orderModel.getParentReference().getCode());
				}
				finalCODSelfShipData.setPaymentMode(codSelfShipData.getPaymentMode());
				finalCODSelfShipData.setCustomerNumber(codSelfShipData.getCustomerNumber());
				final CODSelfShipResponseData responseData = cancelReturnFacade.codPaymentInfoToFICO(finalCODSelfShipData);

				if (responseData.getSuccess() == null
						|| !responseData.getSuccess().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					//saving bank details failed payment details in commerce
					cancelReturnFacade.saveCODReturnsBankDetails(finalCODSelfShipData);
					LOG.debug("Failed to post COD return paymnet details to FICO Order No:" + mplReturnInfoForm.getOrderId());
				}
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error("Exception Occured during saving Customer BankDetails for COD order : " + mplReturnInfoForm.getOrderId()
						+ " Exception cause :" + e);
			}
			catch (final Exception e)
			{
				LOG.error("Exception Occured during saving Customer BankDetails for COD order : " + mplReturnInfoForm.getOrderId()
						+ " Exception cause :" + e);
			}
		}
		catch (final Exception exp)
		{
			LOG.error("Exception Oucer While sending value " + exp.getMessage());
		}
		// isRefundalbe disable
		LOG.info("REtrun page controller TransactionId::::::::    " + mplReturnInfoForm.getTransactionId());
		cancelReturnFacade.saveRTSAndRSSFInfoflag(mplReturnInfoForm.getTransactionId());
		return REDIRECT_PREFIX + RequestMappingUrlConstants.LOGIN_TRACKING_PAGE_URL + mplReturnInfoForm.getOrderId();
	}

	@RequestMapping(value = RequestMappingUrlConstants.LINK_TICKET_UPDATE, method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateCRMTicketInfo(final MplCRMTicketUpdateForm ticketUpdate, final Model model,
			final HttpServletRequest request, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException,
			Exception
	{

		//to do implementation
		final RTSAndRSSReturnInfoRequestData returnInfoRequestData = new RTSAndRSSReturnInfoRequestData();

		cancelReturnFacade.retrunInfoCallToOMS(returnInfoRequestData);

		return "String";
	}

	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_RETURN_ADDRESS, method = RequestMethod.POST)
	@ResponseBody
	public AddressData editReturnAddress(final AccountAddressForm addressForm, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request) throws Exception
	{
		final AddressData errorAddress = new AddressData();
		try
		{
			if (null != request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE))
			{
				if (request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE).equalsIgnoreCase(
						ModelAttributetConstants.TYPE_RESIDENTIAL))
				{
					addressForm.setAddressType(ModelAttributetConstants.TYPE_HOME);
				}
				else if (request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE).equalsIgnoreCase(
						ModelAttributetConstants.TYPE_COMMERCIAL))
				{
					addressForm.setAddressType(ModelAttributetConstants.TYPE_WORK);
				}
			}
			final String errorMsg = mplAddressValidator.validate(addressForm);
			if (!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
			{
				GlobalMessages.addErrorMessage(model, errorMsg);
				final List<StateData> stateDataList = getAccountAddressFacade().getStates();
				final List<StateData> stateDataListNew = getFinalStateList(stateDataList);
				model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataListNew);
				model.addAttribute(ModelAttributetConstants.ACCOUNT_ADDRESS, Boolean.TRUE);
				final List<String> AddressRadioTypeList = getAddressRadioTypeList();
				model.addAttribute(ModelAttributetConstants.ADDRESS_RADIO_TYPE_LIST, AddressRadioTypeList);

				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_ADDRESSBOOK));
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);

				errorAddress.setTitle(errorMsg);
				return errorAddress;
			}

			//return logistics check
			final String orderCode = addressForm.getOrderCode();
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			boolean returnLogisticsCheck = true;
			final List<ReturnLogisticsResponseData> returnLogisticsRespList = cancelReturnFacade.checkReturnLogistics(
					subOrderDetails, addressForm.getPostcode());

			if (CollectionUtils.isNotEmpty(returnLogisticsRespList))
			{
				final CustomerData customerData = customerFacade.getCurrentCustomer();
				for (final ReturnLogisticsResponseData responeData : returnLogisticsRespList)
				{
					final MplReturnPickUpAddressInfoModel mplReturnReport = modelService.create(MplReturnPickUpAddressInfoModel.class);
					mplReturnReport.setOrderId(responeData.getOrderId());
					mplReturnReport.setPincode(addressForm.getPostcode());
					mplReturnReport.setTransactionId(responeData.getTransactionId());
					mplReturnReport.setCustomerId(customerData.getUid());
					if (StringUtils.isNotEmpty(responeData.getIsReturnLogisticsAvailable())
							&& responeData.getIsReturnLogisticsAvailable().equalsIgnoreCase("Y"))
					{
						mplReturnReport.setStatus("Sucess");
					}
					else
					{
						mplReturnReport.setStatus("Fail");
					}

					modelService.save(mplReturnReport);
				}
			}
			for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
			{
				model.addAttribute(ModelAttributetConstants.PINCODE_NOT_SERVICEABLE, response.getResponseMessage());
				if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase(ModelAttributetConstants.N_CAPS_VAL))
				{
					returnLogisticsCheck = false;
				}
			}

			if (!returnLogisticsCheck)
			{
				errorAddress.setTitle(ModelAttributetConstants.LPNOTAVAILABLE_ERRORMSG);

				return errorAddress;
			}

			final AddressData newAddress = new AddressData();
			newAddress.setId(addressForm.getAddressId());
			newAddress.setTitleCode(addressForm.getTitleCode());
			newAddress.setFirstName(addressForm.getFirstName());
			newAddress.setLastName(addressForm.getLastName());

			//TISPRDT-1238 starts
			final String fullAddress = addressForm.getLine1();

			String addressLine1 = "";
			String addressLine2 = "";
			String addressLine3 = "";

			if (fullAddress.length() <= 40)
			{
				addressLine1 = fullAddress.substring(0, fullAddress.length());
			}
			else if (fullAddress.length() <= 80 && fullAddress.length() > 40)
			{
				addressLine1 = fullAddress.substring(0, 40);
				addressLine2 = fullAddress.substring(40, fullAddress.length());
			}
			else if (fullAddress.length() > 80 && fullAddress.length() <= 120)
			{
				addressLine1 = fullAddress.substring(0, 40);
				addressLine2 = fullAddress.substring(40, 80);
				addressLine3 = fullAddress.substring(80, fullAddress.length());
			}

			newAddress.setLine1(addressLine1);
			newAddress.setLine2(addressLine2);
			newAddress.setLine3(addressLine3);
			//TISPRDT-1238 ends

			//newAddress.setLine1(addressForm.getLine1());
			//TISUATSE-125
			//newAddress.setLine2(addressForm.getLine2());
			//newAddress.setLine3(addressForm.getLine3());
			if (StringUtils.isBlank(addressForm.getLandmark()))
			{
				newAddress.setLandmark(addressForm.getOtherLandmark());
			}
			else
			{
				newAddress.setLandmark(addressForm.getLandmark());
			}
			newAddress.setTown(addressForm.getTownCity());
			newAddress.setPostalCode(addressForm.getPostcode());
			newAddress.setBillingAddress(false);
			newAddress.setShippingAddress(true);
			newAddress.setVisibleInAddressBook(true);
			newAddress.setAddressType(addressForm.getAddressType());
			newAddress.setPhone(addressForm.getMobileNo());
			newAddress.setState(addressForm.getState());
			newAddress.setCountry(getI18NFacade().getCountryForIsocode(ModelAttributetConstants.INDIA_ISO_CODE));
			//newAddress.setLine3(addressForm.getLine3());
			newAddress.setLocality(addressForm.getLocality());

			if (addressForm.getRegionIso() != null && !StringUtils.isEmpty(addressForm.getRegionIso()))
			{
				newAddress.setRegion(getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso()));
			}

			if (userFacade.isAddressBookEmpty())
			{
				newAddress.setDefaultAddress(Boolean.TRUE);
				newAddress.setVisibleInAddressBook(Boolean.TRUE);
			}
			else
			{
				if (null != request.getParameter(ModelAttributetConstants.DEFAULT_ADDRESS_CHECKBOX)
						&& request.getParameter(ModelAttributetConstants.DEFAULT_ADDRESS_CHECKBOX).equalsIgnoreCase(
								ModelAttributetConstants.TRUE))
				{
					addressForm.setDefaultAddress(Boolean.TRUE);
				}

				newAddress.setDefaultAddress(addressForm.getDefaultAddress() != null
						&& addressForm.getDefaultAddress().booleanValue());
			}
			try
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Customer Editing  new addrees for Return Pincode :+" + newAddress.getPostalCode());

				}
				getAccountAddressFacade().editAddress(newAddress);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error(e);
				return errorAddress;
			}


			return newAddress;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return errorAddress;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return errorAddress;
		}
	}

	@RequestMapping(value = RequestMappingUrlConstants.LINK_ADD_RETURN_ADDRESS, method = RequestMethod.GET)
	@ResponseBody
	public AddressData addReturnAddress(final AccountAddressForm addressForm, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request) throws Exception
	{
		final AddressData errorAddress = new AddressData();
		try
		{
			if (null != request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE))
			{
				if (request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE).equalsIgnoreCase(
						ModelAttributetConstants.TYPE_RESIDENTIAL))
				{
					addressForm.setAddressType(ModelAttributetConstants.TYPE_HOME);
				}
				else if (request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE).equalsIgnoreCase(
						ModelAttributetConstants.TYPE_COMMERCIAL))
				{
					addressForm.setAddressType(ModelAttributetConstants.TYPE_WORK);
				}
			}
			final String errorMsg = mplAddressValidator.validate(addressForm);
			if (!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
			{
				GlobalMessages.addErrorMessage(model, errorMsg);
				final List<StateData> stateDataList = getAccountAddressFacade().getStates();
				final List<StateData> stateDataListNew = getFinalStateList(stateDataList);
				model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataListNew);
				model.addAttribute(ModelAttributetConstants.ACCOUNT_ADDRESS, Boolean.TRUE);
				final List<String> AddressRadioTypeList = getAddressRadioTypeList();
				model.addAttribute(ModelAttributetConstants.ADDRESS_RADIO_TYPE_LIST, AddressRadioTypeList);

				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_ADDRESSBOOK));
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);

				errorAddress.setTitle(errorMsg);
				return errorAddress;
			}

			final AddressData newAddress = new AddressData();
			newAddress.setTitleCode(addressForm.getTitleCode());
			newAddress.setFirstName(addressForm.getFirstName());
			newAddress.setLastName(addressForm.getLastName());
			newAddress.setLine1(addressForm.getLine1());
			newAddress.setLine2(addressForm.getLine2());
			newAddress.setTown(addressForm.getTownCity());
			newAddress.setPostalCode(addressForm.getPostcode());
			newAddress.setBillingAddress(false);
			newAddress.setShippingAddress(true);
			newAddress.setVisibleInAddressBook(true);
			newAddress.setAddressType(addressForm.getAddressType());
			newAddress.setPhone(addressForm.getMobileNo());
			newAddress.setState(addressForm.getState());
			newAddress.setCountry(getI18NFacade().getCountryForIsocode(ModelAttributetConstants.INDIA_ISO_CODE));
			newAddress.setLine3(addressForm.getLine3());
			newAddress.setLocality(addressForm.getLocality());

			if (addressForm.getRegionIso() != null && !StringUtils.isEmpty(addressForm.getRegionIso()))
			{
				newAddress.setRegion(getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso()));
			}

			if (userFacade.isAddressBookEmpty())
			{
				newAddress.setDefaultAddress(Boolean.TRUE);
				newAddress.setVisibleInAddressBook(Boolean.TRUE);
			}
			else
			{
				if (null != request.getParameter(ModelAttributetConstants.DEFAULT_ADDRESS_CHECKBOX)
						&& request.getParameter(ModelAttributetConstants.DEFAULT_ADDRESS_CHECKBOX).equalsIgnoreCase(
								ModelAttributetConstants.TRUE))
				{
					addressForm.setDefaultAddress(Boolean.TRUE);
				}

				newAddress.setDefaultAddress(addressForm.getDefaultAddress() != null
						&& addressForm.getDefaultAddress().booleanValue());
			}
			try
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Customer Addding new addrees for Return Pincode :+" + newAddress.getPostalCode());
				}
				getAccountAddressFacade().addaddress(newAddress);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error(e);
				return errorAddress;
			}
			catch (final Exception e)
			{
				LOG.error(e);
				return errorAddress;
			}


			return newAddress;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return errorAddress;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return errorAddress;
		}
	}

	//LINK_PINCODE_CHECK="/pincodeServiceCheck"
	@ResponseBody
	@RequestMapping(value = RequestMappingUrlConstants.LINK_PINCODE_CHECK, method = RequestMethod.GET)
	public List<PointOfServiceData> getReturnPincodeServiceForQuikDrop(@RequestParam(value = "pin") final String pin,
			@RequestParam(value = "ussid") final String ussid)
	{
		List<PointOfServiceData> returnableStores = new ArrayList<PointOfServiceData>();
		returnableStores = pincodeServiceFacade.getAllReturnableStores(pin, StringUtils.substring(ussid, 0, 6));
		return returnableStores;
	}

	/**
	 * @return List<String>
	 */
	private List<String> getAddressRadioTypeList()
	{
		final List<String> list = new ArrayList<String>();
		list.add(ModelAttributetConstants.LIST_VAL_RESIDENTIAL);
		list.add(ModelAttributetConstants.LIST_VAL_COMMERCIAL);
		return list;
	}

	/**
	 * @description adding a value at zeroth index of state list
	 * @param stateDataList
	 * @return List<StateData>
	 */
	private List<StateData> getFinalStateList(final List<StateData> stateDataList)
	{
		final StateData newdata = new StateData();
		newdata.setCode("00");
		newdata.setCountryKey("IN");
		newdata.setName("Select");
		stateDataList.add(0, newdata);
		return stateDataList;
	}

	//RETURN_FILE_DOWNLOAD="/returnFileDownload";
	@ResponseBody
	@RequestMapping(value = RequestMappingUrlConstants.RETURN_FILE_DOWNLOAD, method = RequestMethod.GET)
	protected void returnFileDownload(@RequestParam(ModelAttributetConstants.ORDERCODE) final String orderCode,
			@RequestParam(ModelAttributetConstants.TRANSACTION_ID) final String transactionId, final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String fileDownloadLocation = null;
			String returnDownloadFileName = null;
			final OrderModel orderModel = orderModelService.getOrder(orderCode);
			LOG.debug("Return And Refund Upload File Path:+1032");
			if (orderModel != null && orderModel.getEntries() != null)
			{
				for (final AbstractOrderEntryModel entry : orderModel.getEntries())
				{
					if (StringUtils.isNotEmpty(entry.getTransactionID()) && entry.getTransactionID().equalsIgnoreCase(transactionId))
					{
						//Fetching invoice from consignment entries
						for (final ConsignmentEntryModel c : entry.getConsignmentEntries())
						{
							if (null != c.getConsignment().getInvoice())
							{
								fileDownloadLocation = c.getConsignment().getInvoice().getInvoiceUrl();
								LOG.info("ReturnPageController:::::::InvoiceURL    " + fileDownloadLocation);
								if (fileDownloadLocation == null)
								{
									fileDownloadLocation = configurationService.getConfiguration().getString(
											MessageConstants.DEFAULT_INVOICE_URL);
									LOG.info("ReturnPageController::::::: Properties Url   " + fileDownloadLocation);
								}
							}
							else
							{
								LOG.debug("Return c.getConsignment().getInvoice():::Null");
								fileDownloadLocation = configurationService.getConfiguration().getString(
										MessageConstants.DEFAULT_INVOICE_URL);
								LOG.info("ReturnPageController:::::::  Properties Url " + fileDownloadLocation);
							}
						}
					}
				}
			}

			if (fileDownloadLocation == null)
			{
				fileDownloadLocation = request.getServletContext().getRealPath("/")
						+ ModelAttributetConstants.RETURN_FILE_UPLOAD_FILE_PATH_WEB_INF + File.separator
						+ ModelAttributetConstants.RETURN_FILE_UPLOAD_FILE_PATH_DOC + File.separator;
				returnDownloadFileName = ModelAttributetConstants.RETURN_FILE_UPLOAD_FILE_NAME;
				fileDownloadLocation = fileDownloadLocation.concat(returnDownloadFileName);
			}

			if (fileDownloadLocation != null)
			{
				final File pdfFile = new File(fileDownloadLocation);
				if (pdfFile.exists())
				{
					LOG.debug("Return page controller: inside file available  " + fileDownloadLocation);
					final String preInvoiceFileName = pdfFile.getName();
					try
					{
						if (!preInvoiceFileName.isEmpty())
						{
							final int index = preInvoiceFileName.lastIndexOf('.');
							if (index > 0)
							{
								returnDownloadFileName = preInvoiceFileName.substring(0, index) + "_" + transactionId + "_"
										+ new Timestamp(System.currentTimeMillis()) + "." + preInvoiceFileName.substring(index + 1);
							}
						}
					}
					catch (final Exception exception)
					{
						LOG.error("Exception oucre getting file name" + exception.getMessage());
					}
					LOG.debug("Return And Refund Upload File Name:" + returnDownloadFileName);
					if (!fileDownloadLocation.isEmpty())
					{
						response.setContentType("application/pdf");
						if (null != returnDownloadFileName && !returnDownloadFileName.isEmpty())
						{
							response.addHeader("Content-Disposition", "attachment; filename=" + returnDownloadFileName);
						}
						response.setContentLength((int) pdfFile.length());
						final FileInputStream fileInputStream = new FileInputStream(pdfFile);
						final OutputStream responseOutputStream = response.getOutputStream();
						int bytes;
						while ((bytes = fileInputStream.read()) != -1)
						{
							responseOutputStream.write(bytes);
						}
						fileInputStream.close();
						responseOutputStream.flush();
						responseOutputStream.close();
					}

				}
				else
				{
					LOG.info("Return And Refund Upload File :::Not available LineNo 1108");
				}
			}
			else
			{
				LOG.info("Return And Refund Upload File Path Not available LineNo 1113 ");
			}
		}
		catch (final NullPointerException nullPointer)
		{
			LOG.error("Return page controller::::" + nullPointer.getMessage());
		}
	}

	private CODSelfShipData sendBankInformationToFico(final CustomerData customerData, final MplReturnsForm returnForm,
			final String orderCode, final OrderData subOrderDetails)
	{

		final CODSelfShipData selfShipData = new CODSelfShipData();
		final OrderModel orderModel = orderModelService.getParentOrder(orderCode);
		if (null != orderModel.getParentReference() && null != orderModel.getParentReference().getCode())
		{
			selfShipData.setOrderRefNo(orderModel.getParentReference().getCode());
		}
		selfShipData.setCustomerNumber(customerData.getUid());
		selfShipData.setOrderNo(returnForm.getOrderCode());
		selfShipData.setTransactionID(returnForm.getTransactionId());
		selfShipData.setPaymentMode(returnForm.getRefundMode());
		selfShipData.setTransactionType(RETURN_TYPE_COD);
		for (final AbstractOrderEntryModel entry : orderModel.getEntries())
		{
			if (entry.getTransactionID().trim().equalsIgnoreCase(returnForm.getTransactionId().trim()))
			{
				selfShipData.setAmount(entry.getNetAmountAfterAllDisc().toString());
			}
		}

		if (null != subOrderDetails.getCreated())
		{
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			selfShipData.setOrderDate(dateUtilHelper.convertDateWithFormat(formatter.format(subOrderDetails.getCreated())));
			selfShipData.setTransactionDate(dateUtilHelper.convertDateWithFormat(formatter.format(subOrderDetails.getCreated())));
		}
		if (null != returnForm.getIsCODorder()
				&& returnForm.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
		{
			selfShipData.setTitle(returnForm.getTitle());
			selfShipData.setName(returnForm.getAccountHolderName());
			selfShipData.setBankAccount(returnForm.getAccountNumber());
			selfShipData.setBankName(returnForm.getBankName());
			selfShipData.setBankKey(returnForm.getiFSCCode());
			selfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_POSTPAID);
		}
		return selfShipData;
	}

	/**
	 * @return the i18NFacade
	 */
	public I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	/**
	 * @param i18nFacade
	 *           the i18NFacade to set
	 */
	public void setI18NFacade(final I18NFacade i18nFacade)
	{
		i18NFacade = i18nFacade;
	}

	/**
	 * @return the accountAddressFacade
	 */
	public AccountAddressFacade getAccountAddressFacade()
	{
		return accountAddressFacade;
	}

	/**
	 * @param accountAddressFacade
	 *           the accountAddressFacade to set
	 */
	public void setAccountAddressFacade(final AccountAddressFacade accountAddressFacade)
	{
		this.accountAddressFacade = accountAddressFacade;
	}

	//TPR-5954
	@ResponseBody
	@RequireHardLogIn
	@RequestMapping(value = "/fetchSubReason", method = RequestMethod.GET)
	public List<ReturnReasonData> fetchSubReturnReason(@RequestParam final String parentReasonCode)
	{
		List<ReturnReasonData> returnReasonData = null;
		try
		{
			returnReasonData = mplOrderFacade.getSubReasonCode(parentReasonCode);
		}
		catch (final Exception ex)
		{
			returnReasonData = new ArrayList<ReturnReasonData>();
		}
		return returnReasonData;
	}

	//TPR-5954
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	//@RequireHardLogIn
	@ResponseBody
	public String uploadImages(@RequestParam final ArrayList<MultipartFile> files, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException, Exception
	{
		try
		{
			//final List<MultipartFile> files = new ArrayList<MultipartFile>
			System.out.println(files.size());
			String fileUploadLocation = null;
			String date = null;
			Path path = null;
			//TISRLUAT-50
			if (null != configurationService)
			{
				fileUploadLocation = configurationService.getConfiguration().getString(RequestMappingUrlConstants.IMG_UPLOAD_PATH);
				if (null != fileUploadLocation && !fileUploadLocation.isEmpty())
				{
					try
					{

						//HttpSession session = request.getSession();
						//session.setAttribute("UserName", username);

						for (final MultipartFile fileObj : files)
						{

							final byte barr[] = fileObj.getBytes();
							final SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
							date = sdf.format(new Date());
							path = Paths.get(fileUploadLocation + File.separator + date);
							if (!Files.exists(path))
							{
								try
								{
									Files.createDirectories(path);
								}
								catch (final IOException e)
								{
									//fail to create directory
									LOG.error("Exception ,While creating the Directory " + e.getMessage());
								}
							}
							final BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + File.separator
									+ fileObj.getOriginalFilename()));
							bout.write(barr);
							bout.flush();
							bout.close();
							LOG.debug("FileUploadLocation   :" + fileUploadLocation);
						}
					}
					catch (final Exception e)
					{
						LOG.error("Exception is:" + e);
					}
				}

			}


		}
		catch (final Exception ex)
		{
			LOG.error(ex.getStackTrace());
		}
		return "OK";
	}
}
