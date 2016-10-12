/**
 * 
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
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
import de.hybris.platform.servicelayer.session.SessionService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.data.RTSAndRSSReturnInfoRequestData;
import com.tisl.mpl.data.CODSelfShipResponseData;
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
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;
import com.tisl.mpl.storefront.web.forms.MplCRMTicketUpdateForm;
import com.tisl.mpl.storefront.web.forms.MplReturnsForm;
import com.tisl.mpl.storefront.web.forms.validator.MplAddressValidator;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.MplTimeconverUtility;

/**
 * @author TECHOUTS
 *
 * Controller for returns page 
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
    private  MplConfigFacade mplConfigFacade;
	@Autowired
	private PincodeServiceFacade pincodeServiceFacade;
	@Autowired
	private MplCheckoutFacadeImpl mplCheckoutFacadeImpl;
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	
	@Autowired
	private DateUtilHelper dateUtilHelper;

	private static final String RETURN_SUCCESS = "returnSuccess";
	private static final String RETURN_SUBMIT = "returnSubmit";
	
	/**
	 * 
	 * @param returnForm
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return  String
	 * @throws CMSItemNotFoundException
	 * @throws Exception
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_INITIATE_RETURN, method = RequestMethod.POST)
	@RequireHardLogIn
	public String initiateReturn(final MplReturnsForm returnForm, final Model model,
			final HttpServletRequest request,final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, Exception
	{
		final boolean cancellationStatus;
		LOG.info(returnForm);
		try{
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
		for (final OrderEntryData entry : subOrderEntries)
		{
			if (entry.getTransactionId().equalsIgnoreCase(transactionId))
			{
				subOrderEntry = entry;
				returnOrderEntry = cancelReturnFacade.associatedEntriesData(orderModelService.getOrder(orderCode), transactionId);
				returnProductMap.put(subOrderEntry.getTransactionId(), returnOrderEntry);
				break;
			}
			boolean returnLogisticsAvailability = false;
			if (!(entry.isGiveAway() || entry.isIsBOGOapplied()))
			{
				returnLogisticsAvailability = true;
			}
			model.addAttribute(ModelAttributetConstants.RETURNLOGAVAIL, returnLogisticsAvailability);
		}
		final List<ReturnReasonData> reasonDataList = mplOrderFacade.getReturnReasonForOrderItem();
		
		
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
		//if logistic partner not available for the given pin code 
		model.addAttribute(ModelAttributetConstants.PINCODE_NOT_SERVICEABLE,
				MarketplacecommerceservicesConstants.REVERCE_LOGISTIC_PINCODE_SERVICEABLE_NOTAVAIL_MESSAGE);
		model.addAttribute(ModelAttributetConstants.RETURN_FORM, returnForm);
		model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, new AccountAddressForm());
		model.addAttribute(ModelAttributetConstants.SUB_ORDER, subOrderDetails);
		model.addAttribute(ModelAttributetConstants.REFUNDTYPE,returnForm.getRefundType());
		model.addAttribute(ModelAttributetConstants.RETURN_PRODUCT_MAP, returnProductMap);
		model.addAttribute(ModelAttributetConstants.SUBORDER_ENTRY, subOrderEntry);
		
		 //get available time slots for return pickup
		 List<String> timeSlots = mplConfigFacade.getDeliveryTimeSlots(ModelAttributetConstants.RETURN_SLOT_TYPE);

			model.addAttribute(ModelAttributetConstants.SCHEDULE_TIMESLOTS, timeSlots);

			model.addAttribute(ModelAttributetConstants.ADDRESS_DATA,
					mplCheckoutFacadeImpl.rePopulateDeliveryAddress(getAccountAddressFacade().getAddressBook()));
			List<PointOfServiceData> returnableStores = new ArrayList<PointOfServiceData>();
			
			final String sellerId=subOrderEntry.getSelectedUssid().substring(0, 6);
			String  pincode;
			if (subOrderEntry.getDeliveryPointOfService() != null && subOrderEntry.getDeliveryPointOfService().getAddress() != null)
			{
				pincode=subOrderEntry.getDeliveryPointOfService().getAddress().getPostalCode();
				returnableStores = pincodeServiceFacade.getAllReturnableStores(subOrderEntry.getDeliveryPointOfService().getAddress()
						.getPostalCode(),sellerId );
			}
			else
			{
				pincode=subOrderDetails.getDeliveryAddress().getPostalCode();
				returnableStores = pincodeServiceFacade.getAllReturnableStores(subOrderDetails.getDeliveryAddress().getPostalCode(),
						sellerId);
			}
			
			if(LOG.isDebugEnabled())
			{
				LOG.debug("Bellow Store were eligible for Return ");
				if(CollectionUtils.isNotEmpty(returnableStores))
				{
				for (PointOfServiceData pointOfServiceData : returnableStores)
				{
					LOG.debug(pointOfServiceData.getDisplayName());
				}
				}
				else
				{
					LOG.debug("could not found Returnable Store for the seller Id :"+sellerId +"Pincode :"+pincode );
				}
			}
			
			try
			{
				 //get next available schedule return pickup dates for order entry 
				List<String> returnableDates =cancelReturnFacade.getReturnableDates(subOrderEntry);
				
				model.addAttribute(ModelAttributetConstants.RETURN_DATES,returnableDates);
			}catch(EtailNonBusinessExceptions e)
			{
				LOG.error(e);
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				
			}
			catch (Exception e)
			{
				LOG.error(e);
				ExceptionUtil.getCustomizedExceptionTrace(e);
				
			}
        
			
			model.addAttribute(ModelAttributetConstants.RETURNABLE_SLAVES, returnableStores);
		
	   //for schedule pickup
		if(StringUtils.isNotBlank(returnForm.getReturnMethod()) &&  MarketplacecommerceservicesConstants.RETURN_SCHEDULE.equalsIgnoreCase(returnForm.getReturnMethod()))
		{
			
		boolean returnLogisticsCheck = true;
		String returnFulfillmentType =null;
		final List<ReturnLogisticsResponseData> returnLogisticsRespList = cancelReturnFacade.checkReturnLogistics(subOrderDetails,
				pinCode);
		for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
		{
			model.addAttribute(ModelAttributetConstants.PINCODE_NOT_SERVICEABLE, response.getResponseMessage());
			if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase(ModelAttributetConstants.N_CAPS_VAL))
			{
				returnLogisticsCheck = false;
				returnFulfillmentType=response.getReturnFulfillmentType();
			}
		}
		storeContentPageTitleInModel(model, MessageConstants.RETURN_REQUEST);
		storeCmsPageInModel(model, getContentPageForLabelOrId(RETURN_SUBMIT));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(RETURN_SUBMIT));
		
		if (!returnLogisticsCheck)
		{
			GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER,
						ModelAttributetConstants.LPNOTAVAILABLE_ERRORMSG,null);
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						ModelAttributetConstants.LPNOTAVAILABLE_ERRORMSG);
				
			return ControllerConstants.Views.Pages.Account.AccountOrderReturnPincodeServiceCheck;
		}	   
		
		ReturnInfoData returnData=new ReturnInfoData();
		
		List<String> times=MplTimeconverUtility.splitTime(returnForm.getScheduleReturnTime());
		
		String timeSlotFrom=null;
		String timeSlotto=null;
		for (String time : times)
		{
			if(null==timeSlotFrom)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Return Pickup Slot From Time :"+timeSlotFrom+" for the TransactionId :"+returnForm.getTransactionId());
				}
			timeSlotFrom=time;
			}
			else
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Return Pickup Slot From Time :"+timeSlotto+" for the TransactionId :"+returnForm.getTransactionId());
				}
			 timeSlotto=time;
			}
			
		}
		
		String returnPickupDate=returnForm.getScheduleReturnDate();
		returnData.setReasonCode(returnForm.getReturnReason());
		if(returnForm.getIsCODorder().equalsIgnoreCase("Y")){
			returnData.setRefundType("N");
		}else{
			returnData.setRefundType("S");
		}
		returnData.setReturnPickupDate(dateUtilHelper.convertDateWithFormat(returnPickupDate));
		returnData.setTicketTypeCode(MarketplacecommerceservicesConstants.RETURN_TYPE);
		returnData.setTimeSlotFrom(dateUtilHelper.convertTo24HourWithSecodnds(timeSlotFrom));
		returnData.setTimeSlotTo(dateUtilHelper.convertTo24HourWithSecodnds(timeSlotto));
		returnData.setUssid(returnForm.getUssid());
		returnData.setReturnMethod(returnForm.getReturnMethod());
		returnData.setReturnFulfillmentMode(returnFulfillmentType);
		
		final ReturnItemAddressData returnAddrData = new ReturnItemAddressData();
		
		returnAddrData.setAddressLane1(returnForm.getAddrLine1());
		returnAddrData.setAddressLane2(returnForm.getAddrLine2());
		returnAddrData.setLandmark(returnForm.getLandMark());
		returnAddrData.setCity(returnForm.getCity());
		returnAddrData.setCountry(returnForm.getCountry());
		returnAddrData.setFirstName(returnForm.getFirstName());
		returnAddrData.setLastName(returnForm.getLastName());
		returnAddrData.setMobileNo(returnForm.getPhoneNumber());
		returnAddrData.setState(returnForm.getState());
		returnAddrData.setPincode(returnForm.getPincode());
	
		if (returnForm.getRefundType().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_TYPE))
		{
			cancellationStatus = cancelReturnFacade.implementReturnItem(subOrderDetails, subOrderEntry,returnData, customerData, SalesApplication.WEB, returnAddrData);
		}
		else
		{
			cancellationStatus = cancelReturnFacade.implementCancelOrReturn(subOrderDetails, subOrderEntry, returnForm.getReturnReason(), returnForm.getUssid(),
					returnForm.getRefundType(), customerData, returnForm.getReturnMethod(), true, SalesApplication.WEB);
		}

		if (!cancellationStatus)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.RETURN_ERRORMSG);
			return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
		}
		}
		
		//for quick drop
		if(returnForm.getReturnMethod().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_METHOD_QUICKDROP))
		{
		RTSAndRSSReturnInfoRequestData infoRequestData=new RTSAndRSSReturnInfoRequestData();
		
		List<String> stores=Arrays.asList(returnForm.getStoreIds());
		infoRequestData.setOrderId(returnForm.getOrderCode());
		infoRequestData.setRTSStore(stores);
		infoRequestData.setTransactionId(transactionId);
		infoRequestData.setReturnType(MarketplacecommerceservicesConstants.RETURN_TYPE_RTS);
		//return info call to OMS 
			cancelReturnFacade.retrunInfoCallToOMS(infoRequestData);
		}
		
		if(returnForm.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
		{
		CODSelfShipData selfShipData=new CODSelfShipData();
		selfShipData.setCustomerNumber(customerData.getUid());
		selfShipData.setTitle(returnForm.getTitle());
		selfShipData.setName(returnForm.getAccountHolderName());
		selfShipData.setBankAccount(returnForm.getAccountNumber());
		selfShipData.setBankName(returnForm.getBankName());
		selfShipData.setBankKey(returnForm.getiFSCCode());
		selfShipData.setOrderNo(returnForm.getOrderCode());
		selfShipData.setTransactionID(returnForm.getTransactionId());
		selfShipData.setPaymentMode(returnForm.getRefundMode());
		
		if(null != returnForm.getIsCODorder() &&  returnForm.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y) )
		{
			//set ordertag POSTPAIDRRF for COD orders
		selfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_POSTPAID);
		}
		else 
		{
			//set ordertag POSTPAIDRRF for PREPAID orders
			selfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_PREPAID);
		}
		
				try
				{
					//inser or update Customer Bank Details
					cancelReturnFacade.insertUpdateCustomerBankDetails(selfShipData);
					CODSelfShipResponseData responseData=cancelReturnFacade.codPaymentInfoToFICO(selfShipData);
					
					if(responseData.getSuccess() == null || !responseData.getSuccess().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS) )
					{
						//saving bank details failed payment details in commerce 
						cancelReturnFacade.saveCODReturnsBankDetails(selfShipData);	
						LOG.debug("Failed to post COD return paymnet details to FICO Order No:"+orderCode);
					}
				}
				catch (EtailNonBusinessExceptions e)
				{
					LOG.error("Exception Occured during saving Customer BankDetails for COD order : " + orderCode
							+ " Exception cause :" + e);
				}
				catch (Exception e)
				{
					LOG.error("Exception Occured during saving Customer BankDetails for COD order : " + orderCode
							+ " Exception cause :" + e);
				}
		
		
		}
		
		storeCmsPageInModel(model, getContentPageForLabelOrId(RETURN_SUCCESS));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(RETURN_SUCCESS));
		return ControllerConstants.Views.Pages.Account.AccountReturnSuccessPage;
		
		}
		catch(EtailNonBusinessExceptions e)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.RETURN_ERRORMSG);
					LOG.error(e);
			return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
		}
		catch (Exception e)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					ModelAttributetConstants.RETURN_ERRORMSG);
					LOG.error(e);
			return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
		}
		
		 
	}
	
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UPDATE_RETURNINFO, method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateReturnInfo(final MplReturnsForm returnForm, final Model model,
			final HttpServletRequest request,final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, Exception
	{
	
		//to DO Implementeation 
		
		RTSAndRSSReturnInfoRequestData  returnInfoRequestData=new RTSAndRSSReturnInfoRequestData();
		
		cancelReturnFacade.retrunInfoCallToOMS(returnInfoRequestData);

		return "String";
	}
	
	@RequestMapping(value = RequestMappingUrlConstants.LINK_TICKET_UPDATE, method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateCRMTicketInfo(final MplCRMTicketUpdateForm ticketUpdate, final Model model,
			final HttpServletRequest request,final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, Exception
	{
	
		//to do implementation
		RTSAndRSSReturnInfoRequestData  returnInfoRequestData=new RTSAndRSSReturnInfoRequestData();
		
		cancelReturnFacade.retrunInfoCallToOMS(returnInfoRequestData);
		
		return "String";
	}
	
	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_RETURN_ADDRESS, method = RequestMethod.POST)
	@ResponseBody
	public AddressData editReturnAddress(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request) throws Exception
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
			final List<ReturnLogisticsResponseData> returnLogisticsRespList = cancelReturnFacade.checkReturnLogistics(subOrderDetails,
					addressForm.getPostcode());
			for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
			{
				model.addAttribute(ModelAttributetConstants.PINCODE_NOT_SERVICEABLE, response.getResponseMessage());
				if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase(ModelAttributetConstants.N_CAPS_VAL))
				{
					returnLogisticsCheck = false;
				}
			}

			if(! returnLogisticsCheck)
			{
				errorAddress.setTitle(ModelAttributetConstants.LPNOTAVAILABLE_ERRORMSG);
				return errorAddress;
			}

			final AddressData newAddress = new AddressData();
			newAddress.setId(addressForm.getAddressId());
			newAddress.setTitleCode(addressForm.getTitleCode());
			newAddress.setFirstName(addressForm.getFirstName());
			newAddress.setLastName(addressForm.getLastName());
			newAddress.setLine1(addressForm.getLine1());
			newAddress.setLine2(addressForm.getLine2());
			newAddress.setLine3(addressForm.getLine3());
			if(StringUtils.isBlank(addressForm.getLandmark()))
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
				if(LOG.isDebugEnabled())
				{
					LOG.debug("Customer Editing  new addrees for Return Pincode :+"+newAddress.getPostalCode());
					
				}	
				getAccountAddressFacade().editAddress(newAddress);
			}
			catch (EtailNonBusinessExceptions e)
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
	
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ADD_RETURN_ADDRESS, method = RequestMethod.POST)
	@ResponseBody
	public AddressData addReturnAddress(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request) throws Exception
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
				if(LOG.isDebugEnabled())
				{
					LOG.debug("Customer Addding new addrees for Return Pincode :+"+newAddress.getPostalCode());
				}
				getAccountAddressFacade().addaddress(newAddress);
			}
			catch (EtailNonBusinessExceptions e)
			{
				LOG.error(e);
				return errorAddress;
			}
			catch (Exception e) {
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

	/**
	 * @return the i18NFacade
	 */
	public I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	/**
	 * @param i18nFacade the i18NFacade to set
	 */
	public void setI18NFacade(I18NFacade i18nFacade)
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
	 * @param accountAddressFacade the accountAddressFacade to set
	 */
	public void setAccountAddressFacade(AccountAddressFacade accountAddressFacade)
	{
		this.accountAddressFacade = accountAddressFacade;
	}
	
	
}
