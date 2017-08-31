package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

/**
 * @author Techouts
 */

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

import bsh.ParseException;

import com.hybris.oms.domain.changedeliveryaddress.TransactionEddDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCallContextController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MplDeliveryAddressController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.populators.CustomAddressReversePopulator;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.sms.facades.SendSMSFacade;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.details.WidgetDetailRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

public class MplChangeDeliveryOTPWidgetRenderer
		extends
		AbstractCsWidgetRenderer<Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController>> {

	private static final Logger LOG = Logger
			.getLogger(MplChangeDeliveryOTPWidgetRenderer.class);
	private static final String CUSTOMER_DETAILS_UPDATED = "customerdetailsupdated";
	private static final String FAILED_AT_OMS = "failedAtOms";
	private static final String AN_ERROR_OCCURRED = "erroroccurred";
	private static final String INFO = "info";
	private static final String ADDRESS_NOT_CHANGABLE = "addressNotChangable";
	
	@Autowired
	private SendSMSFacade sendSMSFacade;
	@Autowired
	private OTPGenericService oTPGenericService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MarketplaceCallContextController marketplaceCallContextController;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private PopupWidgetHelper popupWidgetHelper;
	@Autowired
	private MplDeliveryAddressController mplDeliveryAddressController;
	@Autowired
	private MplDeliveryAddressFacade mplDeliveryAddressFacade;
	@Autowired
	private MplConfigFacade mplConfigFacade;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private CustomAddressReversePopulator addressReversePopulator;
	private WidgetDetailRenderer<TypedObject, Widget> detailRenderer;
	
	@Autowired
	@Qualifier("marketPlaceCsAssociatedOrderController")
	private MarketPlaceOrderController controller; 

	protected WidgetDetailRenderer<TypedObject, Widget> getDetailRenderer() {
		return detailRenderer;
	}

	@Required
	public void setDetailRenderer(
			WidgetDetailRenderer<TypedObject, Widget> detailRenderer) {
		this.detailRenderer = detailRenderer;
	}

	private CallContextController callContextController;

	protected CallContextController getCallContextController() {
		return callContextController;
	}

	@Required
	public void setCallContextController(
			CallContextController callContextController) {
		this.callContextController = callContextController;
	}

	public TypedObject getOrder() {
		return getCallContextController().getCurrentOrder();
	}

	int otpResendCount ;
	protected HtmlBasedComponent createContentInternal(
			final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			HtmlBasedComponent rootContainer) {	
		otpResendCount= 0;
		Div content = new Div();	
		final Div SDAreaDiv = new Div();
		content.appendChild(SDAreaDiv);
		OrderModel  orderModel = (OrderModel) getOrder().getObject();
		de.hybris.platform.commercefacades.user.data.AddressData addressData;
		addressData=sessionService.getAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS); 
		AddressModel deliveryAddress = modelService.create(AddressModel.class);
		if(null != addressData) {
			addressReversePopulator.populate(addressData, deliveryAddress);
		}
		
		boolean isScheduleDeliveryPossible = isScheduledeliveryPossible(deliveryAddress,orderModel);
	
		if(isScheduleDeliveryPossible) {
			try {
				createScheduledDeliveryArea(widget,deliveryAddress,content,deliveryAddress.getPostalcode());
			}catch(Exception e) {
				LOG.error("Exception while creating delivery timings "+e.getMessage());
			}

		} else {
			createOtpArea(widget,deliveryAddress,content,null);
		}
		return content;
	}

	private void createScheduledDeliveryArea(
			final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,final AddressModel deliveryAddress,
			final Div content,String pinCode) {
		LOG.info("Inside createScheduledDeliveryArea method ");
		final Div SDAreaDiv = new Div();
		SDAreaDiv.setParent(content);
		final OrderModel  orderModel = (OrderModel) getOrder().getObject();
		List<TransactionEddDto> transactionEddDto = new ArrayList<TransactionEddDto>();
		try {
			LOG.debug("Getting Schedule Delivery Dates ");
			transactionEddDto = mplDeliveryAddressFacade
					.getScheduledDeliveryDate(orderModel.getParentReference(),
							pinCode);
		} catch (Exception e) {
			LOG.error("Exception occurred while getting the delivery Dates from oms " + e.getMessage());
		}
		
        if(null != transactionEddDto &&  !transactionEddDto.isEmpty()) {
        	final 	List<TransactionSDDto>  ScheduleDeliverydDtoList = new ArrayList<TransactionSDDto>();
        	
    		for (TransactionEddDto eddDto:   transactionEddDto) {
    			TransactionSDDto sdDto = new TransactionSDDto();
    			sdDto.setTransactionID(eddDto.getTransactionID());
    			ScheduleDeliverydDtoList.add(sdDto);
    		}
    			try {
    				Listbox listBox = new Listbox();
    				listBox.setParent(SDAreaDiv);
    				listBox.setVflex(false);
    				listBox.setFixedLayout(true);
    				listBox.setSclass("csWidgetListbox");

    				renderScheduledDeliveryListbox(ScheduleDeliverydDtoList,transactionEddDto,listBox, widget);

    			}catch(Exception e) {
    				LOG.error("Exception occurred  while displaying delivery slots"+e.getMessage());
    			}
    			 Button SdConfirmbutton = new Button(LabelUtils.getLabel(widget,
    		   				"sdDeliveryButton"));
    			 Div sdButtonDiv = new Div();
    			 sdButtonDiv.setClass("sdDeliveryButton");
    			 
    			 sdButtonDiv.appendChild(SdConfirmbutton);
    			 SDAreaDiv.appendChild(sdButtonDiv);
    			 SdConfirmbutton.addEventListener(Events.ON_CLICK, new EventListener() {
    		   			@Override
    		   			public void onEvent(Event arg0) throws Exception {
    		   				handleSdbuttonButtonClickEvent(widget,deliveryAddress, content,SDAreaDiv,ScheduleDeliverydDtoList);
    		   			}
    		   		});
        }
		
	}

	protected void handleSdbuttonButtonClickEvent(Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,AddressModel  deliveryAddress ,Div content,Div sDAreaDiv,
			List<TransactionSDDto>  ScheduledeliveryDtoList) {
		sDAreaDiv.setVisible(false);
		createOtpArea(widget, deliveryAddress,content,ScheduledeliveryDtoList);
		
	}

	private void createOtpArea(final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,final AddressModel deliveryAddress, Div content,final List<TransactionSDDto>  ScheduledeliveryDtoList) {
		final Div otpAreaDiv = new Div();
		content.appendChild(otpAreaDiv);
		final OrderModel  orderModel = (OrderModel) getOrder().getObject();
		try {
			sendSmsToCustomer(orderModel,deliveryAddress);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		final Div otpLimitsDiv = new Div();
		otpLimitsDiv.setParent(otpAreaDiv);
		final Hbox otpHbox = createHbox(widget, "OTP", false, true);
		otpHbox.setClass("hbox");
		final Textbox OTPTextBox = createTextbox(otpHbox);
		otpAreaDiv.setClass("changeDeliveryotpArea");
		otpHbox.setClass("otpHbox");
		otpHbox.setParent(otpAreaDiv);
		otpAreaDiv.setVisible(true);
		OTPTextBox.setMaxlength(8);
		final Toolbarbutton resendOtp = new Toolbarbutton("Resend OTP");
		resendOtp.setParent(otpHbox);
		resendOtp.setClass("changeDeliveryResentOtp");
		Div validateOtpButtonDiv = new Div();
		validateOtpButtonDiv.setClass("validateOTP");
		Button validateOTP = new Button(LabelUtils.getLabel(widget,
				"validateButton"));
		validateOtpButtonDiv.appendChild(validateOTP);
		validateOtpButtonDiv.setParent(otpAreaDiv);
		
		resendOtp.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				handleResendOtpButtonClickEvent(orderModel, deliveryAddress,resendOtp);
				if(null != otpLimitsDiv && null != otpLimitsDiv.getChildren()){
					otpLimitsDiv.getChildren().clear();
				}
				int  remainingotpLimits= 10 - otpResendCount;
				if(LOG.isDebugEnabled()){
					LOG.debug("Limits Remaining  For Resending OTP:"+remainingotpLimits);
				}
				final Label otpLimitsMessageLabel = new Label(LabelUtils.getLabel(widget,
						"resendOtpLimits",
						new Object[] { remainingotpLimits}));
				otpLimitsMessageLabel.setClass("otpLimitsDiv");
				otpLimitsMessageLabel.setParent(otpLimitsDiv);
				if(otpResendCount>=10) {
					resendOtp.setVisible(false);
					if(null != otpLimitsDiv && null != otpLimitsDiv.getChildren()){
						otpLimitsDiv.getChildren().clear();
					}
				}
			}
		});
		validateOTP.addEventListener(
				Events.ON_CLICK,
				createValidateOTPEventListener(widget, OTPTextBox, orderModel,
						deliveryAddress,ScheduledeliveryDtoList));
		
	}

	private void renderScheduledDeliveryListbox(List<TransactionSDDto>  SdDtoList,
			List<TransactionEddDto> transactionEddDtoList, Listbox listBox,
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget) throws java.text.ParseException {
		 Listhead listHead = new Listhead();
         listHead.setParent(listBox);
         try {
        	 // Displaying headers for Schedule delivery 
        	 renderScheduledDeliveryHeaders(widget, listHead);

        	 listHead.setParent(listBox);
        	 //  Displaying Schedule delivery Timings 
        	 OrderModel order = (OrderModel) getOrder().getObject();
        	 TransactionEddDto transactionEddDto = new TransactionEddDto();
        	 OrderModel mainOrder= order.getParentReference();
        	 
        	 for(AbstractOrderEntryModel entry : mainOrder.getEntries()) {
        		 if(null != entry.getEdScheduledDate()) {
        			 Listitem row = new Listitem();
        			 row.setSclass("listbox-row-item");
        			 row.setParent(listBox);
        			 for (OrderModel orderModel : order.getParentReference().getChildOrders()) {
                		 for (AbstractOrderEntryModel entryModel :orderModel.getEntries()) {
                			 for (TransactionEddDto transactionEdDto : transactionEddDtoList) {
                				 if(transactionEdDto.getTransactionID().equalsIgnoreCase(entryModel.getTransactionID())) {
                					 transactionEddDto = transactionEdDto;
                					 break;
                				 }
                			 }
                		 }
                	 }
        			 renderDeliverySlots(entry,widget, row,transactionEddDto,SdDtoList);
        		 }

        	 }
         }catch(Exception e) {
        	 LOG.error("Exception Occurred while rendering schedule Delivery ListBox "+e.getMessage());
         }
     }

	private void renderDeliverySlots(
			final AbstractOrderEntryModel orderEntry,final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Listitem parent, final TransactionEddDto transactionEddDto,final List<TransactionSDDto>  SdDto) throws java.text.ParseException {
		OrderModel order = (OrderModel) getOrder().getObject();
	
		// Entry number 
		Listcell entryNoCell = new Listcell();
		entryNoCell.setParent(parent);
		Div entryNoDiv = new Div();
		entryNoDiv.setParent(entryNoCell);
		entryNoDiv.setSclass("editorWidgetEditor");
		Label entryNoLabel = new Label(String.valueOf(orderEntry.getEntryNumber()));
		entryNoLabel.setParent(entryNoDiv);

		// USSID
		Listcell transactionIDCell = new Listcell();
		transactionIDCell.setParent(parent);
		Div transactionIdDiv = new Div();
		transactionIdDiv.setParent(transactionIDCell);
		transactionIdDiv.setSclass("editorWidgetEditor");
		Label transactionIdLabel = new Label(String.valueOf(orderEntry.getSelectedUSSID()));
		transactionIdLabel.setParent(transactionIdDiv);
		
		// Product Name 
		Listcell productCell = new Listcell();
		productCell.setParent(parent);
		Div productDiv = new Div();
		productDiv.setParent(productCell);
		productDiv.setSclass("editorWidgetEditor");
		Label productLabel = new Label(String.valueOf(orderEntry.getProduct().getArticleDescription()));
		productLabel.setParent(productDiv);
		
		
		// Map<String, List<String>> dateTimeslotMapList = getDateAndTimeMap(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD,transactionEddDto.getEDD());
		Map<String, List<String>> datesList = null ;
		if(orderEntry.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplaceCockpitsConstants.HOME_DELIVERY)) {
			if(null != transactionEddDto.getEDD()) {
				try {
					datesList = getDateAndTimeMap(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD,transactionEddDto.getEDD());
				} catch (Exception e) {
					LOG.error("Exception While getting the time slots "+e.getMessage());
				}
			}
		}
		else if (orderEntry.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplaceCockpitsConstants.EXPRESS_DELIVERY)) {
			if(null != transactionEddDto.getEDD()) {
				try {
					datesList = getDateAndTimeMap(MarketplacecommerceservicesConstants.DELIVERY_MODE_ED,transactionEddDto.getEDD());
				} catch (Exception e) {
					LOG.error("Exception While getting the time slots "+e.getMessage() );
				}
			}
		}
		
		final Map<String, List<String>> dateTimeslotMapList =datesList;

		// Date Cell 
		try {
			Listcell Datecell = new Listcell();
			Datecell.setParent(parent);
			final Radiogroup dateGroup = new Radiogroup();
			for (String date : dateTimeslotMapList.keySet()) {
				 Div DateDiv = new Div();
				 Row row =  new Row();
				 row.appendChild(DateDiv);
				DateDiv.setParent(Datecell);
				DateDiv.appendChild(dateGroup);
				Radio Radio = new Radio();
				Radio.setLabel(date);
				Radio.setParent(DateDiv);
				dateGroup.appendChild(Radio);
			}
			dateGroup.setSelectedIndex(0);
			
			// Time cell
			final Listcell Timecell = new Listcell();
			Timecell.setParent(parent);
			final Radiogroup radioTimeGroup = new Radiogroup();
			List<String> timeList = dateTimeslotMapList.get(dateGroup.getSelectedItem().getLabel());
			for (String time : timeList) {
				 Div timeDiv1 = new Div();
				timeDiv1.appendChild(radioTimeGroup);
				timeDiv1.setParent(Timecell);
				Radio radio = new Radio();
				radio.setLabel(time);
				radio.setParent(timeDiv1);
				radioTimeGroup.appendChild(radio);
			}
			radioTimeGroup.setSelectedIndex(0);
			String selectedTime = radioTimeGroup.getSelectedItem().getLabel();
			String[] fromAndToTime =  selectedTime.split("TO");
			String fromTime = fromAndToTime[0];
			String toTime   = fromAndToTime[1];
			for (TransactionSDDto sdDto : SdDto) {
				if(sdDto.getTransactionID().equalsIgnoreCase(transactionEddDto.getTransactionID())) {
					sdDto.setPickupDate(dateGroup.getSelectedItem().getLabel());
					DateUtilHelper dateutilHelper = new DateUtilHelper();
					sdDto.setTimeSlotFrom(dateutilHelper.convertTo24Hour(fromTime));
					sdDto.setTimeSlotTo(dateutilHelper.convertTo24Hour(toTime));
				}
			}
			OrderModel mainOrder = order.getParentReference();
			for(OrderModel subOrder: mainOrder.getChildOrders()) {
				for(AbstractOrderEntryModel subOrderEntry : subOrder.getEntries()) {
					if(subOrderEntry.getSelectedUSSID().equalsIgnoreCase(orderEntry.getSelectedUSSID()))  {
						for (TransactionSDDto sdDto : SdDto) {
							if(sdDto.getTransactionID().equalsIgnoreCase(subOrderEntry.getTransactionID())) {
								sdDto.setPickupDate(dateGroup.getSelectedItem().getLabel());
								DateUtilHelper dateutilHelper = new DateUtilHelper();
								sdDto.setTimeSlotFrom(dateutilHelper.convertTo24Hour(fromTime));
								sdDto.setTimeSlotTo(dateutilHelper.convertTo24Hour(toTime));
							}

						}
					}
				}
			}
			
			
			dateGroup.addEventListener(Events.ON_CHECK, new EventListener() {
				@Override
				public void onEvent(final Event event) throws InterruptedException,
						ParseException, InvalidKeyException,
						NoSuchAlgorithmException {
					createDateChangeEventListener(widget, orderEntry,radioTimeGroup,dateGroup,
							Timecell,dateTimeslotMapList,transactionEddDto,SdDto);
				}

				
			});
			
			radioTimeGroup.addEventListener(Events.ON_CHECK, new EventListener() {
				@Override
				public void onEvent(final Event event) throws InterruptedException,
						ParseException, InvalidKeyException,
						NoSuchAlgorithmException {
					createTimeChangeEventListener(widget,orderEntry, radioTimeGroup,dateGroup,
							SdDto,transactionEddDto.getTransactionID());
				}
			});
		
		}catch(Exception e) {
			LOG.error("Exception "+e.getMessage());
		}
	}

	
	
	private void createDateChangeEventListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,final AbstractOrderEntryModel mainOrderEntry,
			Radiogroup radioTimeGroup, Radiogroup dateGroup, Listcell timecell, Map<String, List<String>> dateTimeslotMapList, TransactionEddDto transactionEddDto,List<TransactionSDDto> sdDtoList) {
		LOG.info("Inside date change Even Listener");
		List<String> timeList = dateTimeslotMapList.get(dateGroup.getSelectedItem().getLabel());
		LOG.debug("Selected date :"+dateGroup.getSelectedItem().getLabel());
		int size = radioTimeGroup.getItemCount();
		if(null !=timecell && null != timecell.getChildren()) {
			timecell.getChildren().clear();
		}
		while(size >0) {
			radioTimeGroup.removeItemAt(size-1);
			size--;
		}
		radioTimeGroup.detach();
		radioTimeGroup.applyProperties();
		radioTimeGroup.removeChild(radioTimeGroup);
		for (String time : timeList) {
			 Div timeDiv1 = new Div();
			timeDiv1.appendChild(radioTimeGroup);
			timeDiv1.setParent(timecell);
			Radio radio = new Radio();
			radio.setLabel(time);
			radio.setParent(timeDiv1);
			radioTimeGroup.appendChild(radio);
		}
		radioTimeGroup.setSelectedIndex(0);
		OrderModel order = (OrderModel) getOrder().getObject();
		OrderModel mainOrder = order.getParentReference();
		for(OrderModel subOrder: mainOrder.getChildOrders()) {
			for(AbstractOrderEntryModel subOrderEntry : subOrder.getEntries()) {
				if(subOrderEntry.getSelectedUSSID().equalsIgnoreCase(mainOrderEntry.getSelectedUSSID())) {
					for (TransactionSDDto sdDto : sdDtoList) {
						if(sdDto.getTransactionID().equalsIgnoreCase(subOrderEntry.getTransactionID())) {
							sdDto.setPickupDate(dateGroup.getSelectedItem().getLabel());
							if (null != radioTimeGroup && null != radioTimeGroup.getSelectedItem()) {
								LOG.debug("Selected time = "+radioTimeGroup.getSelectedItem().getLabel());
								String selectedTime = radioTimeGroup.getSelectedItem().getLabel();
								String[] fromAndToTime =  selectedTime.split("TO");
								String fromTime = fromAndToTime[0];
								String toTime   = fromAndToTime[1];
								DateUtilHelper dateutilHelper = new DateUtilHelper();
								if(null !=fromTime ) {
									sdDto.setTimeSlotFrom(dateutilHelper.convertTo24Hour(fromTime));
								}
								if(null !=toTime ) {
									sdDto.setTimeSlotTo(dateutilHelper.convertTo24Hour(toTime));
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void createTimeChangeEventListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,final AbstractOrderEntryModel mainOrderEntry,
			Radiogroup radioTimeGroup, Radiogroup dateGroup,List<TransactionSDDto> sdDtoList,String transactionId) {
		LOG.info("Inside TimeChangeEventListener");
		if (null != radioTimeGroup && null != radioTimeGroup.getSelectedItem()) {
			LOG.debug("Selected time = "+radioTimeGroup.getSelectedItem().getLabel());
			String selectedTime = radioTimeGroup.getSelectedItem().getLabel();
			String[] fromAndToTime =  selectedTime.split("TO");
			String fromTime = fromAndToTime[0];
			String toTime   = fromAndToTime[1];
			
			OrderModel order = (OrderModel) getOrder().getObject();
			OrderModel mainOrder = order.getParentReference();
			for(OrderModel subOrder: mainOrder.getChildOrders()) {
				for(AbstractOrderEntryModel subOrderEntry : subOrder.getEntries()) {
					if(subOrderEntry.getSelectedUSSID().equalsIgnoreCase(mainOrderEntry.getSelectedUSSID())) {
						for (TransactionSDDto sdDto : sdDtoList) {
							if(sdDto.getTransactionID().equalsIgnoreCase(subOrderEntry.getTransactionID())) {
								DateUtilHelper dateutilHelper = new DateUtilHelper();
								if(null !=fromTime ) {
									sdDto.setTimeSlotFrom(dateutilHelper.convertTo24Hour(fromTime));
								}
								if(null !=toTime ) {
									sdDto.setTimeSlotTo(dateutilHelper.convertTo24Hour(toTime));
								}
							}
						}
					}
				}
			}
		}
	}
	
	private Map<String, List<String>> getDateAndTimeMap(String timeSlotType,
			String edd) throws java.text.ParseException {
		return mplDeliveryAddressController.getDateAndTimeMap(timeSlotType,edd);
	}

	private void renderScheduledDeliveryHeaders(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Listhead parent) {
		parent.setSclass(CssUtils.combine(new String[] { parent.getSclass(), "cancelableOrderEntryHeaders" }));
		     
		Listheader colEntryNoHeader = new Listheader(LabelUtils.getLabel(
				widget, "entryNumber", new Object[0]));
		colEntryNoHeader.setWidth("20px");
		colEntryNoHeader.setParent(parent);

		Listheader colTransactionIdHeader = new Listheader(LabelUtils.getLabel(
				widget, "Ussid", new Object[0]));
		colTransactionIdHeader.setWidth("200px");
		colTransactionIdHeader.setParent(parent);

		Listheader cell = new Listheader();
		cell.setSclass("productDetailsCell");
		cell.setParent(parent);
		Label productDescriptionLabel = new Label(LabelUtils.getLabel(widget,
				"productDescription", new Object[0]));
		productDescriptionLabel.setParent(cell);

		Listheader colEntryDateNoHeader = new Listheader(LabelUtils.getLabel(
				widget, "Date", new Object[0]));
		colEntryDateNoHeader.setParent(parent);
		
		Listheader colEntryTimeNoHeader = new Listheader(LabelUtils.getLabel(
				widget, "Time", new Object[0]));
		colEntryTimeNoHeader.setWidth("270px");
		colEntryTimeNoHeader.setParent(parent);
		
	}
 
	private boolean isScheduledeliveryPossible(AddressModel changeDeliveryAddress,
			OrderModel order) {
		boolean scheduleDeliveryPossible = false;
		try {
			LOG.info("Checking Whether order is eligible for schedule delivery or not for order id :"+order.getCode());
			scheduleDeliveryPossible = false;
			if(!changeDeliveryAddress.getPostalcode().trim().equalsIgnoreCase(order.getDeliveryAddress().getPostalcode().trim())) {
				scheduleDeliveryPossible = mplDeliveryAddressController.checkScheduledDeliveryForOrder(order.getParentReference());
				return scheduleDeliveryPossible;
			}
			return scheduleDeliveryPossible;
		}catch(Exception e ) {
			LOG.error("Exception While Checking whether order is eligible for scheduling or not for order Id :"+order.getCode());
		}
		return scheduleDeliveryPossible;
	}

	private Textbox createTextbox(final Hbox parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("150px");
		textBox.setParent(parent);
		return textBox;
	}

	private void handleResendOtpButtonClickEvent(OrderModel ordermodel,
			AddressModel deliveryAddress, Toolbarbutton resendOtp) throws InvalidKeyException,
			NoSuchAlgorithmException {
		try {
			sendSmsToCustomer(ordermodel,deliveryAddress);
			otpResendCount++;
		}catch(Exception e) {
			LOG.error("Exception while sending sms to customer for change delivery :"+e.getMessage());
		}
		
	}

	// To send SMS
	private void sendSmsToCustomer(OrderModel ordermodel, AddressModel deliveryAddress)
			throws InvalidKeyException, NoSuchAlgorithmException {
		String oTPMobileNumber = ordermodel.getDeliveryAddress().getPhone1();
		OTPModel OTP = oTPGenericService.getLatestOTPModel(ordermodel.getUser()
				.getUid(), OTPTypeEnum.CDA);
		String userId = ordermodel.getUser().getUid();
		final String contactNumber = configurationService
				.getConfiguration()
				.getString(
						MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);
		String mplCustomerName = (null == ordermodel.getUser().getName()) ? ""
				: ordermodel.getUser().getName();
		String smsContent = oTPGenericService.generateOTP(userId,
				OTPTypeEnum.CDA.getCode(), oTPMobileNumber);

		sendSMSFacade
				.sendSms(
						MarketplacecommerceservicesConstants.SMS_SENDER_ID,
						MarketplacecommerceservicesConstants.SMS_MESSAGE_CD_OTP
								.replace(
										MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
										mplCustomerName != null ? mplCustomerName
												: "There")
								.replace(
										MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE,
										smsContent)
								.replace(
										MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO,
										contactNumber), oTPMobileNumber);
		if(null !=oTPMobileNumber && null != deliveryAddress && null != deliveryAddress.getPhone1()) {
			if(!oTPMobileNumber.equalsIgnoreCase(deliveryAddress.getPhone1())) {
				sendSMSFacade
				.sendSms(
						MarketplacecommerceservicesConstants.SMS_SENDER_ID,
						MarketplacecommerceservicesConstants.SMS_MESSAGE_CD_OTP
								.replace(
										MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
										mplCustomerName != null ? mplCustomerName
												: "There")
								.replace(
										MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE,
										smsContent)
								.replace(
										MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO,
										contactNumber), deliveryAddress.getPhone1());
			}
		}

	}

	public EventListener createValidateOTPEventListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Textbox oTPTextBox, OrderModel ordermodel,
			AddressModel changeDeliveryAddress,final List<TransactionSDDto>  ScheduledeliveryDtoList) {
		return new ValidateOTPEventListener(widget, oTPTextBox, ordermodel,
				changeDeliveryAddress,ScheduledeliveryDtoList);
	}

	// Validate OTP

	protected class ValidateOTPEventListener implements EventListener {
		private Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget;
		private Textbox oTPTextBox;
		private OrderModel orderModel;
		private AddressModel changeDeliveryAddress;
		private List<TransactionSDDto>  ScheduledeliveryDtoList;
		public ValidateOTPEventListener(
				Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
				Textbox oTPTextBox, OrderModel orderModel,
				AddressModel changeDeliveryAddress,List<TransactionSDDto>  ScheduledeliveryDtoList) {
			super();
			this.widget = widget;
			this.oTPTextBox = oTPTextBox;
			this.orderModel = orderModel;
			this.changeDeliveryAddress = changeDeliveryAddress;
			this.ScheduledeliveryDtoList = ScheduledeliveryDtoList;
		}

		@Override
		public void onEvent(Event event) throws InterruptedException {
			if (!oTPTextBox.getText().isEmpty()) {
				handleValidateOTPEvent(widget, oTPTextBox, event, orderModel,
						changeDeliveryAddress,ScheduledeliveryDtoList);
			} else {
				Messagebox
						.show(LabelUtils.getLabel(widget, "ENTER_OTP",
								new Object[0]), INFO, Messagebox.OK,
								Messagebox.ERROR);
			}
		}

		private void handleValidateOTPEvent(
				Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
				Textbox oTPTextBox, Event event, OrderModel orderModel,
				AddressModel changeDeliveryAddress, List<TransactionSDDto>  ScheduledeliveryDtoList)
				throws InterruptedException {
			long time = 0l;
			try {
				time = Long.parseLong(configurationService.getConfiguration()
						.getString("OTP_Valid_Time_milliSeconds"));

			} catch (NumberFormatException exp) {
				LOG.error("on Time limit defined");
			}
			
	
			OTPResponseData otpResponse = oTPGenericService.validateOTP(
					orderModel.getUser().getUid(), orderModel
							.getDeliveryAddress().getPhone1(), oTPTextBox
							.getValue(), OTPTypeEnum.CDA, time);
			boolean validate = otpResponse.getOTPValid();
			if (validate) {
				try {
					TypedObject orderObject = getOrder();
					if(mplDeliveryAddressController
					.isDeliveryAddressChangable(orderObject)) {
					String omsStatus = null;
					TypedObject customer = marketplaceCallContextController
							.getCurrentCustomer();
					CustomerModel customermodel = (CustomerModel) customer
							.getObject();
					String customerId = customermodel.getUid();
					
					changeDeliveryAddress.setOwner(customermodel);
					String interfaceType = getChangeDeliveryInterfacetype(changeDeliveryAddress,orderModel.getDeliveryAddress());
					LOG.debug("interface type :"+interfaceType);
					final List<TransactionSDDto>  scheduleList = new ArrayList<TransactionSDDto>();
					if(CollectionUtils.isNotEmpty(ScheduledeliveryDtoList)) {
						ScheduledeliveryDtoList.forEach(new Consumer<TransactionSDDto>() {

						@Override
						public void accept(TransactionSDDto tDto) {
							TransactionSDDto dto = new TransactionSDDto();
							dto.setPickupDate(tDto.getPickupDate());
							dto.setTimeSlotFrom(tDto.getTimeSlotFrom());
							dto.setTimeSlotTo(tDto.getTimeSlotTo());
							dto.setTransactionID(tDto.getTransactionID());
							scheduleList.add(dto);
						}
					});
					}
					
					omsStatus = mplDeliveryAddressController
							.changeDeliveryAddressCallToOMS(orderModel
									.getParentReference().getCode(), changeDeliveryAddress,interfaceType,ScheduledeliveryDtoList);
					if (omsStatus.equalsIgnoreCase(MarketplaceCockpitsConstants.SUCCESS)) {
						try {
							if(null !=ScheduledeliveryDtoList) {
								try {
									saveDeliveryDates (orderModel,scheduleList);
								}catch(Exception e) {
									LOG.error("Exception while saving schedule delivery dates");
								}
							}
							mplDeliveryAddressController
							.saveDeliveryAddress(orderModel.getParentReference(),changeDeliveryAddress,false);
							LOG.debug("Delivery Address Changed Successfully");
						} catch (ModelSavingException e) {
							LOG.debug("Model saving Exception" + e.getMessage());
						} catch (Exception e) {
							LOG.debug("Exception while saving Address"
									+ e.getMessage());
						}
						//removeTempororyAddress(newDeliveryAddress);

						String ticketSubType = MarketplacecommerceservicesConstants.TICKET_SUB_TYPE_CDA;
						if(interfaceType.equalsIgnoreCase(MarketplaceCockpitsConstants.CU)) {
							ticketSubType = MarketplacecommerceservicesConstants.TICKET_SUB_TYPE_DMC;
						}
						boolean isEDScheduled = false;
						if(CollectionUtils.isNotEmpty(ScheduledeliveryDtoList)) {
							isEDScheduled=true;
						}
						mplDeliveryAddressController.ticketCreateToCrm(
								orderModel.getParentReference(), customerId,
								MarketplaceCockpitsConstants.SOURCE,ticketSubType,isEDScheduled);
						LOG.debug("CRM Ticket Created for Change Delivery Request");
						Messagebox.show(LabelUtils.getLabel(widget,
								CUSTOMER_DETAILS_UPDATED, new Object[0]), INFO,
								Messagebox.OK, Messagebox.INFORMATION);
						try {
							closePopUp();
						}catch(Exception e) {
							e.printStackTrace();
						}
						try {
							controller.getContextController().dispatchEvent(null, null, null);
						}catch(Exception e) {
							e.printStackTrace();
						}

					} else if (omsStatus.equalsIgnoreCase(MarketplaceCockpitsConstants.FAILED)) {
						try {

							// Saving no_of Total requests and rejects 
							mplDeliveryAddressController.saveChangeDeliveryRequests(orderModel.getParentReference());
							Messagebox.show(LabelUtils.getLabel(widget,
									FAILED_AT_OMS, new Object[0]), INFO,
									Messagebox.OK, Messagebox.ERROR);
							try {
								closePopUp();
							}catch(Exception e) {
								e.printStackTrace();
							}
							try {
								controller.getContextController().dispatchEvent(null, null, null);
							}catch(Exception e) {
								e.printStackTrace();
							}
						} catch (ModelRemovalException e) {
							LOG.error("ModelRemovalException  while removing temprory Address"
									+ e.getMessage());
						} catch (Exception e) {
							LOG.error("Exception occurred " + e.getMessage());
						}
					} else {
						// Saving no_of Total requests and rejects 
						mplDeliveryAddressController.saveChangeDeliveryRequests(orderModel.getParentReference());
						closePopUp();
						Map data = Collections.singletonMap("refresh", Boolean.TRUE);
						((OrderController) widget.getWidgetController()).dispatchEvent(null,
								null, data);
						Messagebox.show(LabelUtils.getLabel(widget,
								AN_ERROR_OCCURRED, new Object[0]), INFO,
								Messagebox.OK, Messagebox.ERROR);
					}
				}else {
					mplDeliveryAddressController.saveChangeDeliveryRequests(orderModel.getParentReference());
					
					Messagebox.show(LabelUtils.getLabel(widget, ADDRESS_NOT_CHANGABLE,
							new Object[0]), INFO, Messagebox.OK, Messagebox.ERROR);
					try {
						closePopUp();
					}catch(Exception e) {
						e.printStackTrace();
					}
					try {
						controller.getContextController().dispatchEvent(null, null, null);
					}catch(Exception e) {
						e.printStackTrace();
					}
					Map data = Collections.singletonMap("refresh", Boolean.TRUE);
					((OrderController) widget.getWidgetController()).dispatchEvent(null,
							null, data);
				}
				}catch (Exception e) {
					LOG.error("Exception in changeDeliveryAddressCallToOMS"
							+ e.getMessage());
				}
			} else {
				Messagebox.show(LabelUtils.getLabel(widget, "INVALID_OTP",
						new Object[0]), INFO, Messagebox.OK, Messagebox.ERROR);
			}
		}

		

		private String getChangeDeliveryInterfacetype(
				AddressModel changeDeliveryAddress,
				AddressModel deliveryAddress) {
			String interfacetype = MarketplaceCockpitsConstants.CU;
			if(!changeDeliveryAddress.getPostalcode().equalsIgnoreCase(deliveryAddress.getPostalcode())) {
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			} else if (!changeDeliveryAddress.getLine1().equalsIgnoreCase(deliveryAddress.getLine1())) {
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			} else if (!changeDeliveryAddress.getLine2().equalsIgnoreCase(deliveryAddress.getLine2())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getAddressLine3().equalsIgnoreCase(deliveryAddress.getAddressLine3())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getLandmark().equalsIgnoreCase(deliveryAddress.getLandmark())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getCity().equalsIgnoreCase(deliveryAddress.getCity())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getState().equalsIgnoreCase(deliveryAddress.getState())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getCountry().getName().equalsIgnoreCase(deliveryAddress.getCountry().getName())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}
			return interfacetype;
			
		}

		private void saveDeliveryDates(OrderModel orderModel,
				List<TransactionSDDto> sdDtoList) {
			
			for (TransactionSDDto dto : sdDtoList)
			{
				for (AbstractOrderEntryModel entry :orderModel.getEntries() ) {
					if (entry.getTransactionID().equalsIgnoreCase(dto.getTransactionID())) {
						entry.setEdScheduledDate(dto.getPickupDate());
						DateUtilHelper dateUtilHelper = new DateUtilHelper();
						if(null !=dto.getTimeSlotFrom() ) {
							entry.setTimeSlotFrom(dateUtilHelper.convertTo12Hour(dto.getTimeSlotFrom()));
						}
						if(null !=dto.getTimeSlotTo() ) {
							entry.setTimeSlotTo(dateUtilHelper.convertTo12Hour(dto.getTimeSlotTo()));
						}
						modelService.save(entry);
						if(null != orderModel.getParentReference() && null != orderModel.getParentReference().getEntries()) {
							for(AbstractOrderEntryModel parentOrderEntry : orderModel.getParentReference().getEntries()) {
								if(null != parentOrderEntry.getProduct() && null !=entry.getProduct() && parentOrderEntry.getSelectedUSSID().equalsIgnoreCase(entry.getSelectedUSSID())) {
									parentOrderEntry.setEdScheduledDate(dto.getPickupDate());
									if(null !=dto.getTimeSlotFrom() ) {
										parentOrderEntry.setTimeSlotFrom(dateUtilHelper.convertTo12Hour(dto.getTimeSlotFrom()));
									}
									if(null !=dto.getTimeSlotFrom() ) {
										parentOrderEntry.setTimeSlotTo(dateUtilHelper.convertTo12Hour(dto.getTimeSlotTo()));
									}
									modelService.save(parentOrderEntry);
								}
							}
						}
					}
				}
			}
			modelService.save(orderModel);
		}
	}

	private void closePopUp() {
		int i = popupWidgetHelper.getCurrentPopup().getParent().getChildren()
				.size();
		while (i >= 1) {
			popupWidgetHelper
					.getCurrentPopup()
					.getParent()
					.getChildren()
					.remove(popupWidgetHelper.getCurrentPopup().getParent()
							.getChildren().size() - 1);
			i--;

		}
	}


	private Hbox createHbox(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			String attributeLabel, boolean hidden, final boolean overwriteWidth) {
		final Hbox hbox = new Hbox();
		if (overwriteWidth) {
			hbox.setWidths("9em, none");
		}
		hbox.setAlign("center");
		if (hidden) {
			hbox.setVisible(false);
		}
		final Label label = new Label(LabelUtils.getLabel(widget,
				attributeLabel));
		label.setParent(hbox);
		return hbox;
	}
}
