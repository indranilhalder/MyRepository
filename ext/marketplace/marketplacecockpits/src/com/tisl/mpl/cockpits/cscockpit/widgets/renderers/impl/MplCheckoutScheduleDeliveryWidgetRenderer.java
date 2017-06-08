package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;

import bsh.ParseException;

import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCheckoutController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MplDeliveryAddressController;
import com.tisl.mpl.cockpits.cscockpit.widgets.helpers.MarketplaceServiceabilityCheckHelper;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsItemEDDInfoData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsResponseData;

import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutCartWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsListboxWidgetRenderer;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

public class MplCheckoutScheduleDeliveryWidgetRenderer extends AbstractCsListboxWidgetRenderer<ListboxWidget<CheckoutCartWidgetModel, CheckoutController>>{
	@Autowired
	private ModelService modelService;
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MarketplaceCheckoutPaymentWidgetRenderer.class);

	@Autowired
	private MplConfigFacade mplConfigFacade;
	
	@Autowired
	private MarketplaceServiceabilityCheckHelper marketplaceServiceabilityCheckHelper;
	
	@Autowired
	private SessionService sessionService;
	@Autowired
	private MplDeliveryAddressController mplDeliveryAddressController;
	@Autowired
	MplDeliveryAddressFacade mplDeliveryAddressFacade;
	
	@Autowired 
	private MplSellerInformationService mplSellerInformationService;
	@Autowired
	private CommerceCartService commerceCartService;
	/**
	 * Creates the content internal.
	 *
	 * @param widget the widget
	 * @param rootContainer the root container
	 * @return the html based component
	 */
	
	@Override
	protected void renderListbox(final Listbox paramListbox,
			final ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			HtmlBasedComponent rootContainer) {
		LOG.info("Inside renderListbox Method");
		final Div content = new Div();
		rootContainer.appendChild(content);
		CartModel  cart = null;
		if(null != widget.getWidgetController().getBasketController() && null != widget.getWidgetController().getBasketController().getCart()) {
			cart = (CartModel)widget.getWidgetController().getBasketController().getCart().getObject();
		}
		if( null != cart) { 
			for (AbstractOrderEntryModel entry : cart.getEntries()) {
				if(null == entry.getMplDeliveryMode()) {
					String label = LabelUtils.getLabel(
							widget, "noEntries", new Object[0]);
					Label noEntriesLable = new Label(label);
					content.appendChild(noEntriesLable);
					return;
				}
			}
			if(null==cart.getDeliveryAddress() || null==cart.getCartReservationDate()) {
				String label = LabelUtils.getLabel(
						widget, "noEntries", new Object[0]);
				Label noEntriesLable = new Label(label);
				content.appendChild(noEntriesLable);

				return;
			}
			try {
				createScheduledDeliveryArea(widget,content);
			}catch(Exception e) {
				LOG.error("Exception while Displaying Delivery slots"+e.getMessage());
			}
		}else {
			LOG.debug("Cart is null");
			String label = LabelUtils.getLabel(
					widget, "noEntries", new Object[0]);
			Label noEntriesLable = new Label(label);
			content.appendChild(noEntriesLable);
		}

	}
	private void createScheduledDeliveryArea(
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			Div content) {
		try {
		CartModel  cart = (CartModel)widget.getWidgetController().getBasketController().getCart().getObject();
		Listbox listBox = new Listbox();
		content.appendChild(listBox);
		Listhead listHead = new Listhead();
		listHead.setParent(listBox);
		InvReserForDeliverySlotsRequestData deliverySlotsRequestData=new InvReserForDeliverySlotsRequestData();
		deliverySlotsRequestData.setCartId(cart.getGuid());
		if(LOG.isDebugEnabled()){
			LOG.debug("calling oms For InvReserForDeliverySlots for cart id"+cart.getGuid());
		}
		InvReserForDeliverySlotsResponseData deliverySlotsResponseData = new InvReserForDeliverySlotsResponseData();
		if(null != cart.getIsInventoryChanged() && cart.getIsInventoryChanged()) {
			 deliverySlotsResponseData=((MarketplaceCheckoutController) widget.getWidgetController()).deliverySlotsRequestDataCallToOms(deliverySlotsRequestData,cart);
			 if(null !=deliverySlotsResponseData ) {
				 if(LOG.isDebugEnabled()){
						LOG.debug("After calling oms For InvReserForDeliverySlots for cart id"+cart.getGuid());
					}
				 sessionService.setAttribute(MarketplacecommerceservicesConstants.SCHEDULE_DELIVRY_DATA, deliverySlotsResponseData);
				 for (AbstractOrderEntryModel cartEntry : cart.getEntries()) {
						if(null != cartEntry) {
							 if(LOG.isDebugEnabled()){
									LOG.debug("EddDateBetween for cartEntry "+cartEntry.getSelectedUSSID()+" with cart guid "+cart.getGuid()+ " is" +cartEntry.getSddDateBetween());
									LOG.debug("ExpectedDeliveryDate for cartEntry "+cartEntry.getSelectedUSSID()+" with cart guid "+cart.getGuid()+ " is" +cartEntry.getExpectedDeliveryDate());
								}
						}
			 }
			 cart.setIsInventoryChanged(Boolean.FALSE);
			 modelService.save(cart);
		}
		}else {
			 deliverySlotsResponseData= sessionService.getAttribute(MarketplacecommerceservicesConstants.SCHEDULE_DELIVRY_DATA);
			 if(null == deliverySlotsResponseData) {
				 deliverySlotsResponseData=((MarketplaceCheckoutController) widget.getWidgetController()).deliverySlotsRequestDataCallToOms(deliverySlotsRequestData,cart); 			
			 }
		}
		
      
		boolean eligigleForScheduleSlots = Boolean.FALSE;
	 	if(null != deliverySlotsResponseData && null != deliverySlotsResponseData.getInvReserForDeliverySlotsItemEDDInfoData()) {
	 		for(AbstractOrderEntryModel orderEntry: cart.getEntries()) {
 				if(!orderEntry.getGiveAway() && !orderEntry.getFulfillmentType().equalsIgnoreCase(MarketplaceCockpitsConstants.SSHIP) ) {
 					Listitem row = new Listitem();
 					row.setSclass("listbox-row-item");
 					row.setParent(listBox);
 					for( InvReserForDeliverySlotsItemEDDInfoData deliverySlotsItemEDDInfo :deliverySlotsResponseData.getInvReserForDeliverySlotsItemEDDInfoData() ) {
 						if(deliverySlotsItemEDDInfo.getUssId().trim().equalsIgnoreCase(orderEntry.getSelectedUSSID().trim())) {
 							if(null != deliverySlotsItemEDDInfo.getIsScheduled() && deliverySlotsItemEDDInfo.getIsScheduled().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
 							{
 								boolean eligibleForSlots = getSlotsEligibility(orderEntry); // checking from productRichAttributes 
 								if(eligibleForSlots) {
 	 								eligigleForScheduleSlots = true;
 	 								break;
 								}
 							}
 						}
 					}
 				}
 			}
	 		
	 		if(eligigleForScheduleSlots) {
	 			populateScheduledDeliveryHeaders(widget, listHead);
	 			for(AbstractOrderEntryModel orderEntry: cart.getEntries()) {
	 				if(!orderEntry.getGiveAway() &&  !orderEntry.getFulfillmentType().equalsIgnoreCase(MarketplaceCockpitsConstants.SSHIP) ) {
	 					Listitem row = new Listitem();
	 					row.setSclass("listbox-row-item");
	 					row.setParent(listBox);
	 					for( InvReserForDeliverySlotsItemEDDInfoData deliverySlotsItemEDDInfo :deliverySlotsResponseData.getInvReserForDeliverySlotsItemEDDInfoData() ) {
	 						if(deliverySlotsItemEDDInfo.getUssId().trim().equalsIgnoreCase(orderEntry.getSelectedUSSID().trim())) {
	 							if(null != deliverySlotsItemEDDInfo.getIsScheduled() && deliverySlotsItemEDDInfo.getIsScheduled().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y)) {
	 								boolean eligibleForSlots = getSlotsEligibility(orderEntry); // checking from productRichAttributes 
	 								if(eligibleForSlots) {
		 								renderDeliverySlots(widget,orderEntry,deliverySlotsItemEDDInfo, row);
	 								}
		 							break;
	 							}
	 						}
	 					}
	 				}

	 			}
	 		}else {
				String label = LabelUtils.getLabel(
						widget, "noEntries", new Object[0]);
				Label noEntriesLable = new Label(label);
				content.appendChild(noEntriesLable);
	 		}
		}else {
			String label = LabelUtils.getLabel(
					widget, "noEntries", new Object[0]);
			Label noEntriesLable = new Label(label);
			content.appendChild(noEntriesLable);
		}
		}catch(Exception e) {
			LOG.error("Exception occurred"+e.getMessage() );
		}
	}
	

	private boolean getSlotsEligibility(AbstractOrderEntryModel orderEntry) {
		boolean eligibleForSlots = false;
		String productRichAttr = null;
		String sellerRichAttr = null;
		List<RichAttributeModel> productRichAttributeModel = null;
		if(null != orderEntry) {
			ProductModel productModel = orderEntry.getProduct();
			if (null != orderEntry && null != orderEntry.getProduct() && null != orderEntry.getProduct().getRichAttribute())
			{
				productRichAttributeModel = (List<RichAttributeModel>) productModel.getRichAttribute();
				if (productRichAttributeModel != null && !productRichAttributeModel.isEmpty() && productRichAttributeModel.get(0).getScheduledDelivery() != null)
				{
					productRichAttr = productRichAttributeModel.get(0).getScheduledDelivery().toString();
				}
			}
			final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(orderEntry.getSelectedUSSID());
			List<RichAttributeModel> sellerRichAttributeModel = null;
			if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
			{
				sellerRichAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
				if (sellerRichAttributeModel != null && !sellerRichAttributeModel.isEmpty() && sellerRichAttributeModel.get(0).getScheduledDelivery() != null)
				{
					sellerRichAttr = sellerRichAttributeModel.get(0).getScheduledDelivery().toString();
				}
			}
		}
		if(null !=productRichAttr && null != sellerRichAttr) {
			if(sellerRichAttr.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES) && productRichAttr.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)) {
				eligibleForSlots = true;
			}
		}
		return eligibleForSlots;

	}
	
	private void renderDeliverySlots(ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,AbstractOrderEntryModel orderEntry, InvReserForDeliverySlotsItemEDDInfoData deliverySlotsItemEDDInfo,Listitem parent) {

		LOG.info("rendering deliverySlots ");
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
		Label productLabel = new Label(String.valueOf(orderEntry.getProduct().getArticleDescription()));
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
					dateTimeslotMapList = getDateAndTimeMap(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD,Edd);
				} catch (java.text.ParseException e) {
					LOG.error("ParseException While getting the time slots "+e.getMessage());
				}
			}
		}
		else if (orderEntry.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplaceCockpitsConstants.EXPRESS_DELIVERY)) {
			try {
				dateTimeslotMapList = getDateAndTimeMap(MarketplacecommerceservicesConstants.DELIVERY_MODE_ED,Edd);
			} catch (java.text.ParseException e) {
				LOG.error("ParseException While getting the time slots "+e.getMessage() );
			}
		}
		createRadiobuttonForDateAndTime(widget,orderEntry,dateTimeslotMapList,parent);
		
	}
	private void populateScheduledDeliveryHeaders(
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
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

	

	private void createRadiobuttonForDateAndTime(
			final ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget, final AbstractOrderEntryModel orderEntry,
			final Map<String, List<String>> dateTimeslotMapList, Listitem parent) {
		try {
			Listcell datecell = new Listcell();
			Div div = new Div();
			div.setParent(datecell);
			datecell.setParent(parent);
			
			String selectedDate = null;
			if(null != orderEntry.getEdScheduledDate()) {
				selectedDate = orderEntry.getEdScheduledDate();
			}
			String selectedTime = null;
			
			if(null != orderEntry.getTimeSlotFrom() && null != orderEntry.getTimeSlotTo()){
				 selectedTime = orderEntry.getTimeSlotFrom().toString();
				 selectedTime = selectedTime.concat("TO");
				 selectedTime = selectedTime.concat(orderEntry.getTimeSlotTo().toString());
			}
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
				if(date.equalsIgnoreCase(selectedDate)) {
					Radio.setSelected(true);
				}
			}

			// Time cell
			final Listcell timeCell = new Listcell();
			Div div1 = new Div();
			div1.setParent(datecell);
			timeCell.setParent(parent);
			final Radiogroup radioTimeGroup = new Radiogroup();
			List<String> timeList= new ArrayList<String>();
			
			if(null != dateGroup.getSelectedItem() && null !=dateGroup.getSelectedItem().getLabel()) {
				if(null != dateGroup.getSelectedItem()) {
					 timeList = dateTimeslotMapList.get(dateGroup.getSelectedItem().getLabel());
				}
				if(null != timeList) {
					for (String time : timeList) {
						Div timeDiv1 = new Div();
						timeDiv1.appendChild(radioTimeGroup);
						timeDiv1.setParent(timeCell);
						Radio radio = new Radio();
						radio.setLabel(time.trim());
						radio.setParent(timeDiv1);
						radioTimeGroup.appendChild(radio);
						if(null != selectedTime) {
							if(time.trim().equalsIgnoreCase(selectedTime.trim())){
								radio.setSelected(true);
							}
						}
					}
				}
				
				if(null !=dateGroup.getSelectedItem() && null == radioTimeGroup.getSelectedItem()) {
					radioTimeGroup.setSelectedIndex(0);
				}
				if(null !=dateGroup.getSelectedItem() && null ==radioTimeGroup.getSelectedItem()) {
					radioTimeGroup.setSelectedIndex(0);
					String time = radioTimeGroup.getSelectedItem().getLabel();
					String[] fromAndToTime =  time.split("TO");
					DateUtilHelper dateUtilhelper = new DateUtilHelper();
					String fromTime=fromAndToTime[0];
					String toTime=fromAndToTime[1];
					orderEntry.setTimeSlotFrom(fromTime);
					orderEntry.setTimeSlotTo(toTime);
					try {
						modelService.save(orderEntry);
					}catch(Exception e) {
						LOG.error("Exception occurred ");
					}
				}
			}
			
		//	radioTimeGroup.setSelectedIndex(0);
			
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
					createResetbuttonClickEventListener(widget,dateGroup,radioTimeGroup,orderEntry);
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

	protected void createResetbuttonClickEventListener(ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget, Radiogroup dateGroup,
			Radiogroup radioTimeGroup, AbstractOrderEntryModel orderEntry) {
		if(null != dateGroup.getSelectedItem() || null != radioTimeGroup.getSelectedItem()) {

			try {
				if(null != dateGroup.getSelectedItem()) {
					dateGroup.getSelectedItem().setChecked(false);
					if(null != orderEntry.getEdScheduledDate()) {
						orderEntry.setEdScheduledDate(null);
					}
				} 
				if(null != radioTimeGroup.getSelectedItem()) {
					radioTimeGroup.getSelectedItem().setChecked(false);
					if(null != orderEntry.getTimeSlotFrom() && null != orderEntry.getTimeSlotTo()) {
						if(null !=orderEntry.getTimeSlotFrom()) {
							orderEntry.setTimeSlotFrom(null);
						} 
						if(null != orderEntry.getTimeSlotTo()) {
							orderEntry.setTimeSlotTo(null);
						}  
					}
				}
				
				CartModel cartModel = (CartModel) orderEntry.getOrder();
				orderEntry.setScheduledDeliveryCharge(0.0D);
				modelService.save(orderEntry);
				// INC-144316545- Delivery Charge Issue START
				  recalculateCart(widget,cartModel);
				// INC-144316545- Delivery Charge Issue END
			}catch(ModelSavingException e) {
				LOG.error("Exception while saving the date and tme"+e.getMessage());
			}catch (Exception e) {
				LOG.error("Exception while saving EdScheduledDate"+e.getMessage());
			}
		}
	}

	protected void createTimeChangeEventListener(
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			Radiogroup radioTimeGroup, AbstractOrderEntryModel orderEntry,final Button resetButton) {
		LOG.info("Inside TimeChangeEventListener");
		if (null != radioTimeGroup && null != radioTimeGroup.getSelectedItem()) {
			resetButton.setDisabled(false);
			LOG.debug("Selected time = "+radioTimeGroup.getSelectedItem().getLabel());
			String selectedTime = radioTimeGroup.getSelectedItem().getLabel();
			String[] fromAndToTime =  selectedTime.split(MarketplaceCockpitsConstants.TO);
			String fromTime=fromAndToTime[0];
			String toTime=fromAndToTime[1];
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
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			Radiogroup dateGroup, Radiogroup radioTimeGroup, Listcell timecell,
			Map<String, List<String>> dateTimeslotMapList,
			AbstractOrderEntryModel orderEntry,final Button resetButton) {
		resetButton.setDisabled(false);
		LOG.info("Inside date change Even Listener");
		List<String> timeList = dateTimeslotMapList.get(dateGroup.getSelectedItem().getLabel());
		LOG.debug("Selected date :"+dateGroup.getSelectedItem().getLabel());
		String date = dateGroup.getSelectedItem().getLabel();
		orderEntry.setEdScheduledDate(date);
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
			radio.setLabel(time.trim());
			radio.setParent(timeDiv1);
			radioTimeGroup.appendChild(radio);
		}
		radioTimeGroup.setSelectedIndex(0);
		String selectedTime = radioTimeGroup.getSelectedItem().getLabel();
		String[] fromAndToTime =  selectedTime.split(MarketplaceCockpitsConstants.TO);
		String fromTime=fromAndToTime[0];
		String toTime=fromAndToTime[1];
		orderEntry.setTimeSlotFrom(fromTime);
		orderEntry.setTimeSlotTo(toTime);
		CartModel cartModel = (CartModel) orderEntry.getOrder();
		try {
			Double ScheduleDeliveryCharges = 0.0D;
			ScheduleDeliveryCharges = ((MarketplaceCheckoutController) widget.getWidgetController()).getScheduleDeliveryCharges();
			orderEntry.setScheduledDeliveryCharge(ScheduleDeliveryCharges);
			modelService.save(orderEntry);
			// INC-144316545- Delivery Charge Issue START
			recalculateCart(widget,cartModel);
			// INC-144316545- Delivery Charge Issue END
		}catch(Exception e) {
			LOG.error("Exception occurred While Saving Order Entry"+e.getMessage());
		}
	}

	// Recalculating order After Selecting/Re-Setting Delivery Slot
	private void recalculateCart(ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget, CartModel cartModel) {
		Double totalDeliverycost=0.0D;
		Double deliveryCost = 0.0D;
		Double scheduleDelCharges = 0.0D;
		if(null !=cartModel && null !=cartModel.getEntries() ) {
			for (AbstractOrderEntryModel cartEntry : cartModel.getEntries()) {
				if(null != cartEntry.getMplDeliveryMode() && null !=cartEntry.getMplDeliveryMode().getValue()) {
					deliveryCost+=cartEntry.getMplDeliveryMode().getValue();
				}
				if(null != cartEntry.getScheduledDeliveryCharge() && cartEntry.getScheduledDeliveryCharge()>0.0D) {
					scheduleDelCharges+=cartEntry.getScheduledDeliveryCharge();
				}
			}
		}
		if(null != deliveryCost && deliveryCost >0.0) {
			totalDeliverycost+=deliveryCost;
		}
		if(null != scheduleDelCharges && scheduleDelCharges >0.0) {
			totalDeliverycost+=scheduleDelCharges;
		}
		cartModel.setDeliveryCost(totalDeliverycost);
		modelService.save(cartModel);
		try {
			commerceCartService.recalculateCart(cartModel);
		} catch (CalculationException e) {
			LOG.error("Exception occurred while recalculating cart"+e.getMessage());
		}
		((BasketController)widget.getWidgetController().getBasketController()).dispatchEvent(null, widget, null);
	}
	
	
	private Map<String, List<String>> getDateAndTimeMap(String timeSlotType,
			String edd) throws java.text.ParseException {
		return mplDeliveryAddressController.getDateAndTimeMap(timeSlotType,edd); 
	}

	

	
}
