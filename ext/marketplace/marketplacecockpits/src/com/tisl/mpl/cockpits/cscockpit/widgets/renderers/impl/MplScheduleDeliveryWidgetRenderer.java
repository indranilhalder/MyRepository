package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

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
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;

import bsh.ParseException;

import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCheckoutController;
import com.tisl.mpl.cockpits.cscockpit.widgets.helpers.MarketplaceServiceabilityCheckHelper;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsItemEDDInfoData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsResponseData;

import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;

public class MplScheduleDeliveryWidgetRenderer extends AbstractCsWidgetRenderer<DefaultListboxWidget<DefaultListWidgetModel, CheckoutController>>{
	@Autowired
	private ModelService modelService;

	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MarketplaceCheckoutPaymentWidgetRenderer.class);

	@Autowired
	private MplConfigFacade mplConfigFacade;
	
	@Autowired
	private MarketplaceServiceabilityCheckHelper marketplaceServiceabilityCheckHelper;
	/**
	 * Creates the content internal.
	 *
	 * @param widget the widget
	 * @param rootContainer the root container
	 * @return the html based component
	 */
	protected HtmlBasedComponent createContentInternal(
			final DefaultListboxWidget<DefaultListWidgetModel, CheckoutController> widget,
			HtmlBasedComponent rootContainer) {
		final Div content = new Div();
		final Button showSdDatesButton = new Button("Show Schedule Delivery Dates");
		showSdDatesButton.setParent(content);
		showSdDatesButton.setStyle("Align:Center");
		showSdDatesButton.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(final Event event) throws InterruptedException,
			ParseException, InvalidKeyException,
			NoSuchAlgorithmException {
				showSdDatesButtonClickEventListener(widget,showSdDatesButton,content);
			}
		});
		
		return content;
	}

	protected void showSdDatesButtonClickEventListener(
			DefaultListboxWidget<DefaultListWidgetModel, CheckoutController> widget,
			Button showSdDatesButton, Div content) throws InterruptedException {
		
		CartModel  cart = (CartModel)widget.getWidgetController().getBasketController().getCart().getObject();
		for (AbstractOrderEntryModel entry : cart.getEntries()) {
			if(null == entry.getMplDeliveryMode()) {
				Messagebox.show("please select a delivery mode for product "+entry.getProduct().getArticleDescription()+" ");
				return;
			}
		}
		if(null==cart.getDeliveryAddress() && null!=cart.getCartReservationDate()) {
			Messagebox.show("no delivey address selected, please select delivery Address and confirm it");
			return;
		}
			try {
				createScheduledDeliveryArea(widget,content);
				showSdDatesButton.setVisible(false);
			}catch(Exception e) {
				LOG.error("Exception while Displaying Delivery slots"+e.getMessage());
			}
	}

	private void createScheduledDeliveryArea(
			DefaultListboxWidget<DefaultListWidgetModel, CheckoutController> widget,
			Div content) {
		CartModel  cart = (CartModel)widget.getWidgetController().getBasketController().getCart().getObject();
		Listbox listBox = new Listbox();
		content.appendChild(listBox);
		Listhead listHead = new Listhead();
		listHead.setParent(listBox);
		InvReserForDeliverySlotsRequestData deliverySlotsRequestData=new InvReserForDeliverySlotsRequestData();
		deliverySlotsRequestData.setCartId(cart.getGuid());
		InvReserForDeliverySlotsResponseData deliverySlotsResponseData=((MarketplaceCheckoutController) widget.getWidgetController()).deliverySlotsRequestDataCallToOms(deliverySlotsRequestData);
		if(null != deliverySlotsResponseData && null != deliverySlotsResponseData.getInvReserForDeliverySlotsItemEDDInfoData()) {
			populateScheduledDeliveryHeaders(widget, listHead);
			for ( InvReserForDeliverySlotsItemEDDInfoData deliverySlotsItemEDDInfo :deliverySlotsResponseData.getInvReserForDeliverySlotsItemEDDInfoData() ) {
				Listitem row = new Listitem();
				row.setSclass("listbox-row-item");
				row.setParent(listBox);
				renderDeliverySlots(widget,cart,deliverySlotsItemEDDInfo, row);
			}
		}
		else {
			String label = LabelUtils.getLabel(
					widget, "noEntries", new Object[0]);
			Label noEntriesLable = new Label(label);
			content.appendChild(noEntriesLable);
		}
	}
	
	
	private void populateScheduledDeliveryHeaders(
			DefaultListboxWidget<DefaultListWidgetModel, CheckoutController> widget,
			Listhead parent) {
		parent.setSclass(CssUtils.combine(new String[] { parent.getSclass(), "cancelableOrderEntryHeaders" }));

		Listheader colEntryNoHeader = new Listheader(LabelUtils.getLabel(
				widget, "entryNumber", new Object[0]));
		colEntryNoHeader.setWidth("23px");
		colEntryNoHeader.setParent(parent);

		Listheader colUssIdHeader = new Listheader(LabelUtils.getLabel(
				widget, "ussId", new Object[0]));
		colUssIdHeader.setWidth("161px");
		colUssIdHeader.setParent(parent);

		Listheader cell = new Listheader();
		cell.setParent(parent);
		Label productDescriptionLabel = new Label(LabelUtils.getLabel(widget,
				"productDescription", new Object[0]));
		cell.setWidth("404px");
		productDescriptionLabel.setParent(cell);

		Listheader colEntryDateNoHeader = new Listheader(LabelUtils.getLabel(
				widget, "date", new Object[0]));
		colEntryDateNoHeader.setWidth("253px");
		colEntryDateNoHeader.setParent(parent);

		Listheader colEntryTimeNoHeader = new Listheader(LabelUtils.getLabel(
				widget, "time", new Object[0]));
		colEntryTimeNoHeader.setWidth("280px");
		colEntryTimeNoHeader.setParent(parent);
		Listheader resetTimeSlotsHeader = new Listheader(LabelUtils.getLabel(
				widget, "reset", new Object[0]));
		resetTimeSlotsHeader.setWidth("88px");
		resetTimeSlotsHeader.setParent(parent);

	}

	private void renderDeliverySlots(DefaultListboxWidget<DefaultListWidgetModel, CheckoutController> widget, CartModel cart, InvReserForDeliverySlotsItemEDDInfoData deliverySlotsItemEDDInfo,Listitem parent) {
		AbstractOrderEntryModel orderEntry = modelService.create(AbstractOrderEntryModel.class);
		for (AbstractOrderEntryModel entryModel : cart.getEntries()) {
			if(entryModel.getSelectedUSSID().equalsIgnoreCase(deliverySlotsItemEDDInfo.getUssId()))
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
		Listcell UssIdCell = new Listcell();
		UssIdCell.setParent(parent);
		Div ussIdDiv = new Div();
		ussIdDiv.setParent(UssIdCell);
		ussIdDiv.setSclass("editorWidgetEditor");
		Label transactionIdLabel = new Label(String.valueOf(deliverySlotsItemEDDInfo.getUssId()));
		transactionIdLabel.setParent(ussIdDiv);

		// Product Name 
		Listcell productCell = new Listcell();
		productCell.setParent(parent);
		Div productDiv = new Div();
		productDiv.setParent(productCell);
		productDiv.setSclass("editorWidgetEditor");
		Label productLabel = new Label(String.valueOf(orderEntry.getInfo()));
		productLabel.setParent(productDiv);
		Map<String, List<String>> dateTimeslotMapList = null ;
		String Edd = null;
		if(null != deliverySlotsItemEDDInfo.getEDD()) {
			Edd = deliverySlotsItemEDDInfo.getEDD();
		}
		else if(null != deliverySlotsItemEDDInfo.getNextEDD()){
			Edd = deliverySlotsItemEDDInfo.getNextEDD();
		}
		if(orderEntry.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplaceCockpitsConstants.HOME_DELIVERY)) {
			if(null != deliverySlotsItemEDDInfo.getEDD()) {
				try {
					dateTimeslotMapList = getDateAndTimeMap("SD",Edd);
				} catch (java.text.ParseException e) {
					LOG.error("ParseException While getting the time slots "+e.getMessage());
				}
			}
		}
		else if (orderEntry.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplaceCockpitsConstants.EXPRESS_DELIVERY)) {
			try {
				dateTimeslotMapList = getDateAndTimeMap("SD",Edd);
			} catch (java.text.ParseException e) {
				LOG.error("ParseException While getting the time slots "+e.getMessage() );
			}
		}
		createRadiobuttonForDateAndTime(widget,orderEntry,dateTimeslotMapList,parent);

	}

	private void createRadiobuttonForDateAndTime(
			final DefaultListboxWidget<DefaultListWidgetModel, CheckoutController> widget, final AbstractOrderEntryModel orderEntry,
			final Map<String, List<String>> dateTimeslotMapList, Listitem parent) {
		try {
			Listcell datecell = new Listcell();
			Div div = new Div();
			div.setParent(datecell);
			datecell.setParent(parent);
			final Radiogroup dateGroup = new Radiogroup();
			for (String date : dateTimeslotMapList.keySet()) {
				Div DateDiv = new Div();
				Row row =  new Row();
				row.appendChild(DateDiv);
				DateDiv.setParent(datecell);
				DateDiv.appendChild(dateGroup);
				Radio Radio = new Radio();
				Radio.setLabel(date);
				Radio.setParent(DateDiv);
				dateGroup.appendChild(Radio);
			}
			dateGroup.setSelectedIndex(0);
			// Time cell
			final Listcell timeCell = new Listcell();
			Div div1 = new Div();
			div1.setParent(datecell);
			timeCell.setParent(parent);
			final Radiogroup radioTimeGroup = new Radiogroup();
			List<String> timeList = dateTimeslotMapList.get(dateGroup.getSelectedItem().getLabel());
			for (String time : timeList) {
				Div timeDiv1 = new Div();
				timeDiv1.appendChild(radioTimeGroup);
				timeDiv1.setParent(timeCell);
				Radio radio = new Radio();
				radio.setLabel(time);
				radio.setParent(timeDiv1);
				radioTimeGroup.appendChild(radio);
			}
			radioTimeGroup.setSelectedIndex(0);
			
			Listcell cell = new Listcell();
			Div resetDiv = new Div();
			resetDiv.setWidth("88px");
			resetDiv.setParent(cell);
			cell.setParent(parent);;
			final Button resetButton = new Button("Reset");
			cell.appendChild(resetButton);
			resetButton.setDisabled(true);
			if(null !=radioTimeGroup.getSelectedItem() || null !=dateGroup.getSelectedItem()) {
				resetButton.setDisabled(false);
			}
			
			resetButton.addEventListener(Events.ON_CLICK, new EventListener() {
				@Override
				public void onEvent(final Event event) throws InterruptedException,
				ParseException, InvalidKeyException,
				NoSuchAlgorithmException {
					createResetbuttonClickEventListener(dateGroup,radioTimeGroup,orderEntry);
				}
			});
			dateGroup.addEventListener(Events.ON_CHECK, new EventListener() {
				@Override
				public void onEvent(final Event event) throws InterruptedException,
				ParseException, InvalidKeyException,
				NoSuchAlgorithmException {
					createDateChangeEventListener(widget,dateGroup,radioTimeGroup,
							timeCell,dateTimeslotMapList,orderEntry,resetButton);
				}
			});		
			radioTimeGroup.addEventListener(Events.ON_CHECK, new EventListener() {
				@Override
				public void onEvent(final Event event) throws InterruptedException,
				ParseException, InvalidKeyException,
				NoSuchAlgorithmException {
					createTimeChangeEventListener(widget, radioTimeGroup,orderEntry,resetButton);
				}
			});

		}catch(Exception e) {
			LOG.error("Exception while displaying Delivery Timings "+e.getMessage());
		}
	}

	protected void createResetbuttonClickEventListener(Radiogroup dateGroup,
			Radiogroup radioTimeGroup, AbstractOrderEntryModel orderEntry) {
		if(null != dateGroup.getSelectedItem() || null != radioTimeGroup.getSelectedItem()) {
			if(null != dateGroup.getSelectedItem()) {
				dateGroup.getSelectedItem().setChecked(false);
				if(null != orderEntry.getEdScheduledDate()) {
					try {
						orderEntry.setEdScheduledDate(null);
						modelService.save(orderEntry);
					}catch(ModelRemovalException e) {
						LOG.error("Exception while removing EdScheduledDate"+e.getMessage());
					}catch (Exception e) {
						LOG.error("Exception while removing EdScheduledDate"+e.getMessage());
					}
				}
			} 
			if(null != radioTimeGroup.getSelectedItem()) {
				radioTimeGroup.getSelectedItem().setChecked(false);
				if(null != orderEntry.getTimeSlotFrom() && null != orderEntry.getTimeSlotTo()) {
					try {
						if(null !=orderEntry.getTimeSlotFrom()) {
							orderEntry.setTimeSlotFrom(null);
							modelService.save(orderEntry);
						} 
						if(null != orderEntry.getTimeSlotTo()) {
							modelService.remove(orderEntry.getTimeSlotTo());
						}
					}catch(ModelRemovalException e) {
						LOG.error("Exception while removing the date and tme"+e.getMessage());
					}catch (Exception e) {
						LOG.error("Exception while removing EdScheduledDate"+e.getMessage());
					}
				}
			}
		}
	}

	protected void createTimeChangeEventListener(
			DefaultListboxWidget<DefaultListWidgetModel, CheckoutController> widget,
			Radiogroup radioTimeGroup, AbstractOrderEntryModel orderEntry,final Button resetButton) {
		LOG.info("Inside TimeChangeEventListener");
		if (null != radioTimeGroup && null != radioTimeGroup.getSelectedItem()) {
			resetButton.setDisabled(false);
			LOG.debug("Selected time = "+radioTimeGroup.getSelectedItem().getLabel());
			String selectedTime = radioTimeGroup.getSelectedItem().getLabel();
			String[] fromAndToTime =  selectedTime.split("-");
			DateUtilHelper dateUtilhelper = new DateUtilHelper();
			String fromTime=dateUtilhelper.convertTo24Hour(fromAndToTime[0]);
			String toTime=dateUtilhelper.convertTo24Hour(fromAndToTime[1]);
			orderEntry.setTimeSlotFrom(fromTime);
			orderEntry.setTimeSlotTo(toTime);
			try {
				modelService.save(orderEntry);
			}catch(Exception e) {
				LOG.error("Exception occurred ");
			}
		}
	}
	protected void createDateChangeEventListener(
			DefaultListboxWidget<DefaultListWidgetModel, CheckoutController> widget,
			Radiogroup dateGroup, Radiogroup radioTimeGroup, Listcell timecell,
			Map<String, List<String>> dateTimeslotMapList,
			AbstractOrderEntryModel orderEntry,final Button resetButton) {
		resetButton.setDisabled(false);
		LOG.info("Inside date change Even Listener");
		List<String> timeList = dateTimeslotMapList.get(dateGroup.getSelectedItem().getLabel());
		LOG.debug("Selected date :"+dateGroup.getSelectedItem().getLabel());
		String date = dateGroup.getSelectedItem().getLabel();
		DateUtilHelper dateUtilHelper = new DateUtilHelper();
		orderEntry.setEdScheduledDate(date);
		try {
			modelService.save(orderEntry);
		}catch(Exception e) {
			LOG.error("Exception occurred ");
		}
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
	}

	private Map<String, List<String>> getDateAndTimeMap(String timeSlotType,
			String edd) throws java.text.ParseException {

		DateUtilHelper dateUtilHelper = new DateUtilHelper();
		String estDeliveryDateAndTime= edd;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String  deteWithOutTIme=dateUtilHelper.getDateFromat(estDeliveryDateAndTime,format);
		String timeWithOutDate=dateUtilHelper.getTimeFromat(estDeliveryDateAndTime);
		List<String>   calculatedDateList=dateUtilHelper.getDeteList(deteWithOutTIme,format,2);
		List<MplTimeSlotsModel> modelList=null;
		if(timeSlotType.equalsIgnoreCase(MarketplaceCockpitsConstants.SD)){
			modelList=mplConfigFacade.getDeliveryTimeSlotByKey(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD);
			LOG.debug("********* Delivery Mode :" + MarketplacecommerceservicesConstants.DELIVERY_MODE_SD);
		}else if (timeSlotType.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)){
			modelList=mplConfigFacade.getDeliveryTimeSlotByKey(MarketplacecommerceservicesConstants.ED);
			LOG.debug("********* Delivery Mode :" + MarketplacecommerceservicesConstants.ED);
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

	
}
