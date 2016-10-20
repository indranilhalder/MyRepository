package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

/**
 * @author Techouts
 */

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
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
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
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

	protected HtmlBasedComponent createContentInternal(
			final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			HtmlBasedComponent rootContainer) {	
		Div content = new Div();	
		final Div SDAreaDiv = new Div();
		content.appendChild(SDAreaDiv);
		OrderModel  orderModel = (OrderModel) getOrder().getObject();
		AddressData addressData;
		addressData=sessionService.getAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS); 

		AddressModel deliveryAddress = modelService.create(AddressModel.class);
		addressReversePopulator.populate(addressData, deliveryAddress);
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
		if(transactionEddDto== null || transactionEddDto.isEmpty() ) {
			LOG.debug("EDD responce is empty");
		}

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

	protected void handleSdbuttonButtonClickEvent(Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,AddressModel  deliveryAddress ,Div content,Div sDAreaDiv,
			List<TransactionSDDto>  ScheduledeliveryDtoList) {
		sDAreaDiv.setVisible(false);
		createOtpArea(widget, deliveryAddress,content,ScheduledeliveryDtoList);
		
	}

	private void createOtpArea(Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,AddressModel deliveryAddress, Div content,final List<TransactionSDDto>  ScheduledeliveryDtoList) {
		final Div otpAreaDiv = new Div();
		content.appendChild(otpAreaDiv);
		final OrderModel  orderModel = (OrderModel) getOrder().getObject();
		try {
			sendSmsToCustomer(orderModel);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
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
				handleResendOtpButtonClickEvent(orderModel, resendOtp);
			}
		});
		validateOTP.addEventListener(
				Events.ON_CLICK,
				createValidateOTPEventListener(widget, OTPTextBox, orderModel,
						deliveryAddress,ScheduledeliveryDtoList));
		
	}

	private void renderScheduledDeliveryListbox(List<TransactionSDDto>  SdDtoList,
			List<TransactionEddDto> transactionEddDto, Listbox listBox,
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget) throws java.text.ParseException {
		 Listhead listHead = new Listhead();
         listHead.setParent(listBox);
         // Displaying headers for Schedule delivery 
         renderScheduledDeliveryHeaders(widget, listHead);

	           listHead.setParent(listBox);
	           OrderModel order = (OrderModel) getOrder().getObject();	           
	           //  Displaying Schedule delivery Timings 
	           for (TransactionEddDto transactionEdDto : transactionEddDto) {
	        	   Listitem row = new Listitem();
		           row.setSclass("listbox-row-item");
		           row.setParent(listBox);
	        	   renderDeliverySlots(widget, row,transactionEdDto,SdDtoList);
	           }          
     }

	private void renderDeliverySlots(
			final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Listitem parent, final TransactionEddDto transactionEddDto,final List<TransactionSDDto>  SdDto) throws java.text.ParseException {
		OrderModel order = (OrderModel) getOrder().getObject();

		AbstractOrderEntryModel orderEntry = modelService.create(AbstractOrderEntryModel.class);
		for (AbstractOrderEntryModel entryModel : order.getEntries()) {
			if(entryModel.getTransactionID().equalsIgnoreCase(transactionEddDto.getTransactionID()))
			{
				orderEntry = entryModel;
			}
		}
		// Entry number 
		Listcell entryNoCell = new Listcell();
		entryNoCell.setParent(parent);
		Div entryNoDiv = new Div();
		entryNoDiv.setParent(entryNoCell);
		entryNoDiv.setSclass("editorWidgetEditor");
		Label entryNoLabel = new Label(String.valueOf(orderEntry.getEntryNumber()));
		entryNoLabel.setParent(entryNoDiv);

		// Transaction Id 
		Listcell transactionIDCell = new Listcell();
		transactionIDCell.setParent(parent);
		Div transactionIdDiv = new Div();
		transactionIdDiv.setParent(transactionIDCell);
		transactionIdDiv.setSclass("editorWidgetEditor");
		Label transactionIdLabel = new Label(String.valueOf(transactionEddDto
				.getTransactionID()));
		transactionIdLabel.setParent(transactionIdDiv);
		
		// Product Name 
		Listcell productCell = new Listcell();
		productCell.setParent(parent);
		Div productDiv = new Div();
		productDiv.setParent(productCell);
		productDiv.setSclass("editorWidgetEditor");
		Label productLabel = new Label(String.valueOf(orderEntry.getProduct().getArticleDescription()));
		productLabel.setParent(productDiv);
		
		
		 final Map<String, List<String>> dateTimeslotMapList = getDateAndTimeMap(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD,transactionEddDto.getEDD());
//		if(orderEntry.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplaceCockpitsConstants.HOME_DELIVERY)) {
//			if(null != transactionEddDto.getEDD()) {
//				try {
//			//		dateTimeslotMapList = getDateAndTimeMap(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD,transactionEddDto.getEDD());
//				} catch (java.text.ParseException e) {
//					LOG.error("ParseException While getting the time slots "+e.getMessage());
//				}
//			}
//		}
//		else if (orderEntry.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplaceCockpitsConstants.EXPRESS_DELIVERY)) {
//			if(null != transactionEddDto.getEDD()) {
//			try {
//			//	dateTimeslotMapList = getDateAndTimeMap(MarketplacecommerceservicesConstants.DELIVERY_MODE_ED,transactionEddDto.getEDD());
//			} catch (java.text.ParseException e) {
//				LOG.error("ParseException While getting the time slots "+e.getMessage() );
//			}
//			}
//		}
		
		

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
			String[] fromAndToTime =  selectedTime.split("-");
			String fromTime = fromAndToTime[0];
			String toTime   = fromAndToTime[1];
			for (TransactionSDDto sdDto : SdDto) {
				if(sdDto.getTransactionID().equalsIgnoreCase(transactionEddDto.getTransactionID())) {
					sdDto.setPickupDate(dateGroup.getSelectedItem().getLabel());
					sdDto.setTimeSlotFrom(fromTime);
					sdDto.setTimeSlotTo(toTime);
				}
			}
			dateGroup.addEventListener(Events.ON_CHECK, new EventListener() {
				@Override
				public void onEvent(final Event event) throws InterruptedException,
						ParseException, InvalidKeyException,
						NoSuchAlgorithmException {
					createDateChangeEventListener(widget, radioTimeGroup,dateGroup,
							Timecell,dateTimeslotMapList,transactionEddDto,SdDto);
				}

				
			});
			
			radioTimeGroup.addEventListener(Events.ON_CHECK, new EventListener() {
				@Override
				public void onEvent(final Event event) throws InterruptedException,
						ParseException, InvalidKeyException,
						NoSuchAlgorithmException {
					createTimeChangeEventListener(widget, radioTimeGroup,dateGroup,
							SdDto,transactionEddDto.getTransactionID());
				}
			});
		
		}catch(Exception e) {
			LOG.error("Exception "+e.getMessage());
		}
	}

	
	
	private void createDateChangeEventListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
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
		
		for (TransactionSDDto sdDto : sdDtoList) {
			if(sdDto.getTransactionID().equalsIgnoreCase(transactionEddDto.getTransactionID())) {
				sdDto.setPickupDate(dateGroup.getSelectedItem().getLabel());
			}
		}
	}
	
	private void createTimeChangeEventListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Radiogroup radioTimeGroup, Radiogroup dateGroup,List<TransactionSDDto> sdDtoList,String transactionId) {
		LOG.info("Inside TimeChangeEventListener");
		if (null != radioTimeGroup && null != radioTimeGroup.getSelectedItem()) {
			LOG.debug("Selected time = "+radioTimeGroup.getSelectedItem().getLabel());
			String selectedTime = radioTimeGroup.getSelectedItem().getLabel();
			String[] fromAndToTime =  selectedTime.split("-");
			String fromTime = fromAndToTime[0];
			String toTime   = fromAndToTime[1];
			for (TransactionSDDto sdDto : sdDtoList) {
				if(sdDto.getTransactionID().equalsIgnoreCase(transactionId)) {
					sdDto.setTimeSlotFrom(fromTime);
					sdDto.setTimeSlotTo(toTime);
				}
			}
		}
	}
	
	private Map<String, List<String>> getDateAndTimeMap(String timeSlotType,
			String edd) throws java.text.ParseException {

		DateUtilHelper dateUtilHelper = new DateUtilHelper();
		String estDeliveryDateAndTime= edd;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String  deteWithOutTIme=dateUtilHelper.getDateFromat(estDeliveryDateAndTime,format);
		String timeWithOutDate=dateUtilHelper.getTimeFromat(estDeliveryDateAndTime);
		List<String>   calculatedDateList=new ArrayList<String>();
		List<MplTimeSlotsModel> modelList=null;
		if(timeSlotType.equalsIgnoreCase(MarketplaceCockpitsConstants.SD)){
			modelList=mplConfigFacade.getDeliveryTimeSlotByKey(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD);
			LOG.debug("********* Delivery Mode :" + MarketplacecommerceservicesConstants.DELIVERY_MODE_SD);
			calculatedDateList=dateUtilHelper.getDeteList(deteWithOutTIme,format,3);
		}else if (timeSlotType.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)){
			modelList=mplConfigFacade.getDeliveryTimeSlotByKey(MarketplacecommerceservicesConstants.ED);
			LOG.debug("********* Delivery Mode :" + MarketplacecommerceservicesConstants.ED);
			calculatedDateList=dateUtilHelper.getDeteList(deteWithOutTIme,format,2);
		}
		if(null!= modelList){
			Date startTime =null;
			Date endTIme = null;
			Date searchTime=null;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			List<MplTimeSlotsModel> timeList=new ArrayList<MplTimeSlotsModel>();
			for(MplTimeSlotsModel mplTimeSlotsModel:modelList){
				for(String selectedDate: calculatedDateList){
					if(selectedDate.equalsIgnoreCase(deteWithOutTIme) ){
						try {
							startTime = sdf.parse(mplTimeSlotsModel.getToTime());
							endTIme=sdf.parse(mplTimeSlotsModel.getFromTime());
							searchTime = sdf.parse(timeWithOutDate);
						} catch (java.text.ParseException e) {
							e.printStackTrace();
						}
						if (startTime.compareTo(searchTime) > 0  && endTIme.compareTo(searchTime) > 0  && startTime.compareTo(searchTime) != 0 && endTIme.compareTo(searchTime) != 0) {
							LOG.debug("startDate:"+  DateFormatUtils.format(startTime, "HH:mm") + "endDate:"+  DateFormatUtils.format(sdf.parse(mplTimeSlotsModel.getFromTime()), "HH:mm"));
							timeList.add(mplTimeSlotsModel);
						} 
					}
				}
			}
			LOG.debug("timeList.size()**************"+timeList.size());
			if(timeList.size()==0){
				String nextDate= dateUtilHelper.getNextDete(deteWithOutTIme,format);
				calculatedDateList=dateUtilHelper.getDeteList(nextDate,format,2);
				timeList.addAll(modelList);
			}
			List<String> finalTimeSlotList=null;
			Map<String, List<String>> dateTimeslotMapList=new LinkedHashMap<String, List<String>>();
			for(String selectedDate: calculatedDateList){

				if(selectedDate.equalsIgnoreCase(deteWithOutTIme)){
					finalTimeSlotList= dateUtilHelper.convertFromAndToTimeSlots(timeList);
				}else{
					finalTimeSlotList= dateUtilHelper.convertFromAndToTimeSlots(modelList);
				}
				dateTimeslotMapList.put(selectedDate, finalTimeSlotList);  
			}
			return dateTimeslotMapList;
		}

		return null;
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
				widget, "transactionId", new Object[0]));
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
				if(!changeDeliveryAddress.getPostalcode().trim().equalsIgnoreCase(order.getDeliveryAddress().getPostalcode().trim())) {
					scheduleDeliveryPossible = mplDeliveryAddressController.checkScheduledDeliveryForOrder(order.getParentReference());
					return scheduleDeliveryPossible;
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
			Toolbarbutton resendOtp) throws InvalidKeyException,
			NoSuchAlgorithmException {
		sendSmsToCustomer(ordermodel);
	}

	// To send SMS
	private void sendSmsToCustomer(OrderModel ordermodel)
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

//		sendSMSFacade
//				.sendSms(
//						MarketplacecommerceservicesConstants.SMS_SENDER_ID,
//						MarketplacecommerceservicesConstants.SMS_MESSAGE_CD_OTP
//								.replace(
//										MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
//										mplCustomerName != null ? mplCustomerName
//												: "There")
//								.replace(
//										MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE,
//										smsContent)
//								.replace(
//										MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO,
//										contactNumber), oTPMobileNumber);

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
					String omsStatus = null;
					TypedObject customer = marketplaceCallContextController
							.getCurrentCustomer();
					CustomerModel customermodel = (CustomerModel) customer
							.getObject();
					String customerId = customermodel.getUid();
					
					changeDeliveryAddress.setOwner(customermodel);
					String interfaceType = getChangeDeliveryInterfacetype(changeDeliveryAddress,orderModel.getDeliveryAddress());
					LOG.debug("interface type :"+interfaceType);
					omsStatus = mplDeliveryAddressController
							.changeDeliveryAddressCallToOMS(orderModel
									.getParentReference().getCode(), changeDeliveryAddress,interfaceType,ScheduledeliveryDtoList);
					if (omsStatus.equalsIgnoreCase(MarketplaceCockpitsConstants.SUCCESS)) {
						try {
							if(null !=ScheduledeliveryDtoList) {
								try {
									saveDeliveryDates (orderModel,ScheduledeliveryDtoList);
								}catch(Exception e) {
									LOG.error("Exception while saving schedule delivery dates");
								}
							}
							mplDeliveryAddressController
									.saveDeliveryAddress(orderModel.getParentReference(),changeDeliveryAddress);
							// Saving no_of Total requests and rejects 
							mplDeliveryAddressController.saveChangeDeliveryRequests(orderModel.getParentReference());
							LOG.debug("Delivery Address Changed Successfully");
						} catch (ModelSavingException e) {
							LOG.debug("Model saving Exception" + e.getMessage());
						} catch (Exception e) {
							LOG.debug("Exception while saving Address"
									+ e.getMessage());
						}
						//removeTempororyAddress(newDeliveryAddress);

						mplDeliveryAddressController.ticketCreateToCrm(
								orderModel.getParentReference(), customerId,
								MarketplaceCockpitsConstants.SOURCE);
						LOG.debug("CRM Ticket Created for Change Delivery Request");
						Messagebox.show(LabelUtils.getLabel(widget,
								CUSTOMER_DETAILS_UPDATED, new Object[0]), INFO,
								Messagebox.OK, Messagebox.INFORMATION);
						closePopUp();

					} else if (omsStatus.equalsIgnoreCase(MarketplaceCockpitsConstants.FAILED)) {
						try {
							
							// Saving no_of Total requests and rejects 
							mplDeliveryAddressController.saveChangeDeliveryRequests(orderModel.getParentReference());
							Messagebox.show(LabelUtils.getLabel(widget,
									FAILED_AT_OMS, new Object[0]), INFO,
									Messagebox.OK, Messagebox.ERROR);
							closePopUp();
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
						Messagebox.show(LabelUtils.getLabel(widget,
								AN_ERROR_OCCURRED, new Object[0]), INFO,
								Messagebox.OK, Messagebox.ERROR);
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
			} else if (!changeDeliveryAddress.getLine1().trim().equalsIgnoreCase(deliveryAddress.getLine1().trim())) {
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			} else if (!changeDeliveryAddress.getLine2().trim().equalsIgnoreCase(deliveryAddress.getLine2().trim())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getAddressLine3().trim().equalsIgnoreCase(deliveryAddress.getAddressLine3().trim())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getLandmark().trim().equalsIgnoreCase(deliveryAddress.getLandmark().trim())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getCity().trim().equalsIgnoreCase(deliveryAddress.getCity().trim())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getState().trim().equalsIgnoreCase(deliveryAddress.getState().trim())){
				interfacetype = MarketplaceCockpitsConstants.CA;
				return interfacetype;
			}else if (!changeDeliveryAddress.getCountry().getName().trim().equalsIgnoreCase(deliveryAddress.getCountry().getName().trim())){
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