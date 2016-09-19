package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
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
import org.zkoss.zul.Textbox;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceReturnsController;
import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.wsdto.ReturnLogistics;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.ReturnsController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.ReturnRequestCreateWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class MarketplaceReturnRequestCreditDetailsWidgetRenderer extends
		ReturnRequestCreateWidgetRenderer {

	@Autowired
	private PopupWidgetHelper popupWidgetHelper;
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private TypeService cockpitTypeService;
	@Autowired
	private ModelService modelService;

	private static final String LABEL = "returnlabel";
	private static final String LABELSECOND = "secondLabel";
	private static final String TEXTBOX = "returntext";
	private static final String CONTINUE = "continue";
	private static final String BUTTON = "creditButton";
	private static final String BOLD_TEXT = "boldText";
	private static final String QUICK_DROP = "Qiuck Drop";
	private static final String SCHEDULE_PICKUP = "Schedule Pickup";

	private List<PointOfServiceData> returnableStores;

	@Override
	protected HtmlBasedComponent createContentInternal(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			HtmlBasedComponent rootContainer) {

		Div creditDetailsDiv = null;
		String paymentType = null;
		PaymentTransactionModel paymentTransation = null;
		final Div bankDetailsArea = new Div();
		final List<AbstractOrderEntryModel> entry = new ArrayList<AbstractOrderEntryModel>();
		Session session = Executions.getCurrent().getDesktop().getSession();

		final String pincode = (String) session.getAttribute("pinCode");
		List<ObjectValueContainer> returnObjectValueContainers = (List<ObjectValueContainer>) session
				.getAttribute("returnEntrys");

		final OrderModel subOrder = (OrderModel) ((ReturnsController) widget
				.getWidgetController()).getCurrentOrder().getObject();
		List<ReturnLogistics> returnLogisticsList = ((MarketPlaceReturnsController) widget
				.getWidgetController()).getReturnLogisticsList(widget,
				returnObjectValueContainers, pincode);
		for (AbstractOrderEntryModel entry1 : subOrder.getEntries()) {
			for (ReturnLogistics returnItem : returnLogisticsList) {
				if (returnItem.getTransactionId().trim()
						.equalsIgnoreCase(entry1.getTransactionID().trim())) {
					entry.add(entry1);
				}
			}
		}

		if (subOrder != null && subOrder.getParentReference() != null) {
			paymentTransation = subOrder.getPaymentTransactions().get(0);
			paymentType = paymentTransation.getEntries().get(0)
					.getPaymentMode().getMode();
		}

		if (paymentType != null
				&& paymentType
						.equalsIgnoreCase(MarketplaceCockpitsConstants.PAYMENT_MODE_COD)) {
			creditDetailsDiv = collectBankDetailsForCodOrder(widget, entry,
					pincode, subOrder);
			creditDetailsDiv.setParent(bankDetailsArea);
		} else if (paymentType != null) {

			creditDetailsDiv = populatePrePaidPaymentDetails(widget, subOrder,
					bankDetailsArea, pincode, entry);
			creditDetailsDiv.setParent(bankDetailsArea);
		}
		return bankDetailsArea;
	}

	private Textbox createTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("30%");
		textBox.setParent(parent);
		return textBox;
	}

	private Div collectBankDetailsForCodOrder(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> entry, final String pincode,
			final OrderModel orderModel) {

		final Div codDetails = new Div();

		final Div accNoDiv = new Div();
		accNoDiv.setParent(codDetails);
		accNoDiv.setClass(TEXTBOX);
		final Label accNo = new Label(LabelUtils.getLabel(widget, "accNo"));
		accNo.setParent(accNoDiv);
		accNo.setClass(LABEL);
		final Textbox accNoTextbox = createTextbox(accNoDiv);
		accNoTextbox.setParent(accNoDiv);
		accNoTextbox.setClass(TEXTBOX);
		accNoTextbox.setMaxlength(16);
		accNoTextbox.setConstraint("/[0-9]*$/:");
		
		final Label re_accNo = new Label(
				LabelUtils.getLabel(widget, "re_accNo"));
		re_accNo.setParent(accNoDiv);
		re_accNo.setClass(LABELSECOND);
		final Textbox re_accNoTextBox = createTextbox(accNoDiv);
		re_accNoTextBox.setParent(accNoDiv);
		re_accNoTextBox.setClass(TEXTBOX);
		re_accNoTextBox.setMaxlength(16);
		re_accNoTextBox.setConstraint("/[0-9]*$/:");

		final Br br = new Br();
		br.setParent(codDetails);

		final Div accHolderNameAndModeDiv = new Div();
		accHolderNameAndModeDiv.setParent(codDetails);
		accHolderNameAndModeDiv.setClass(TEXTBOX);
		final Label accHolderName = new Label(LabelUtils.getLabel(widget,
				"accName"));
		accHolderName.setParent(accHolderNameAndModeDiv);
		accHolderName.setClass(LABEL);
		final Textbox accHolderNameTextbox = createTextbox(accHolderNameAndModeDiv);
		accHolderNameTextbox.setParent(accHolderNameAndModeDiv);
		accHolderNameTextbox.setClass(TEXTBOX);
		accHolderNameTextbox.setConstraint("/[a-zA-Z ]*$/:");
		
		final Label refundMode = new Label(LabelUtils.getLabel(widget,
				"refund_mode"));
		refundMode.setParent(accHolderNameAndModeDiv);
		refundMode.setClass(LABELSECOND);
		final Listbox refundModeListbox = new Listbox();
		refundModeListbox.setParent(accHolderNameAndModeDiv);
		refundModeListbox.setClass(LABEL);
		refundModeListbox.setVisible(true);
		refundModeListbox.setMold("select");

		final List<String> refundModeList = new ArrayList<String>();
		refundModeList.add("NEFT");
		refundModeList.add("IFSC");
		refundModeList.add("RGFT");

		for (String modes : refundModeList) {
			final Listitem refundModeItems = new Listitem(modes);
			refundModeItems.setParent(refundModeListbox);
			refundModeItems.setSelected(true);
		}
		final Label titleLabel = new Label("Title");
		titleLabel.setParent(accHolderNameAndModeDiv);
		titleLabel.setClass(LABELSECOND);

		final Listbox titleListBox = new Listbox();
		titleListBox.setParent(accHolderNameAndModeDiv);
		titleListBox.setClass(LABEL);
		titleListBox.setMold("select");

		List<String> titleList = new ArrayList<String>();
		titleList.add("MR");
		titleList.add("MRs");
		titleList.add("Company");

		for (String title : titleList) {
			final Listitem titleItem = new Listitem(title);
			titleItem.setParent(titleListBox);
			titleItem.setSelected(true);
			
		}

		final Br br1 = new Br();
		br1.setParent(codDetails);

		final Div bankNameDiv = new Div();
		bankNameDiv.setParent(codDetails);
		bankNameDiv.setClass(TEXTBOX);

		final Label bankName = new Label(LabelUtils.getLabel(widget,
				"bank_name"));
		bankName.setParent(bankNameDiv);
		bankName.setClass(LABEL);
		final Textbox bankNameTextbox = createTextbox(bankNameDiv);
		bankNameTextbox.setParent(bankNameDiv);
		bankNameTextbox.setClass(TEXTBOX);
		bankNameTextbox.setConstraint("/[a-zA-Z ]*$/:");
		
		final Label ifscCode = new Label(LabelUtils.getLabel(widget, "IFCS"));
		ifscCode.setParent(bankNameDiv);
		ifscCode.setClass(LABELSECOND);
		final Textbox ifscTextbox = createTextbox(bankNameDiv);
		ifscTextbox.setParent(bankNameDiv);
		ifscTextbox.setClass(TEXTBOX);
		ifscTextbox.setConstraint("/[a-zA-Z0-9]*$/:");
		
		final Div codButtonDiv = new Div();
		codButtonDiv.setParent(codDetails);
		codButtonDiv.setClass(BUTTON);
		final Button codButton = new Button(LabelUtils.getLabel(widget,
				CONTINUE));
		codButton.setParent(codButtonDiv);
		codButton.addEventListener(Events.ON_CLICK, new EventListener() {

			@Override
			public void onEvent(Event arg0) throws Exception {
					codUpadatesToFico(widget, entry, accNoTextbox,
							accHolderNameTextbox, refundModeListbox, titleListBox,
							bankNameTextbox, ifscTextbox);
					codButton.setDisabled(true);
					codDetails.appendChild(getReturnModeDetails(widget, entry,
							pincode, orderModel));
			}
		});
		return codDetails;
	}

	private Div populatePrePaidPaymentDetails(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final OrderModel orderModel, final Div mainDiv,
			final String pincode, final List<AbstractOrderEntryModel> entry) {

		final Div prepaidOrdeDetials = new Div();
		CreditCardPaymentInfoModel cCard = null;
		PaymentTransactionModel payment = orderModel.getPaymentTransactions()
				.get(0);

		if (orderModel.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {

			cCard = (CreditCardPaymentInfoModel) orderModel.getPaymentInfo();

			final Div retunDetails = new Div();
			retunDetails.setParent(prepaidOrdeDetials);
			retunDetails.setClass("detailsClasss");
			final Label returnHead = new Label(LabelUtils.getLabel(widget,
					"returnDetails"));
			returnHead.setParent(retunDetails);
			returnHead.setClass(BOLD_TEXT);
			final Br br1 = new Br();
			br1.setParent(retunDetails);

			final Label returnDescription = new Label(LabelUtils.getLabel(
					widget, "return_Description"));
			returnDescription.setParent(retunDetails);

			final Div cardDetailsldiv = new Div();
			cardDetailsldiv.setParent(prepaidOrdeDetials);
			cardDetailsldiv.setClass("cardDetailsClass");
			final Label cardDetails = new Label(LabelUtils.getLabel(widget,
					"card_Details"));
			cardDetails.setParent(cardDetailsldiv);
			cardDetails.setClass(BOLD_TEXT);

			final Br br2 = new Br();
			br2.setParent(cardDetailsldiv);

			final Label paymentType = new Label(payment.getEntries().get(0)
					.getPaymentMode().getMode());
			paymentType.setParent(cardDetailsldiv);

			final Br br3 = new Br();
			br3.setParent(cardDetailsldiv);

			final Label cardNo = new Label(cCard.getNumber());
			cardNo.setParent(cardDetailsldiv);
			cardNo.setClass(BOLD_TEXT);
			final Br br4 = new Br();
			br4.setParent(cardDetailsldiv);

			final Label deliveyDetails = new Label(LabelUtils.getLabel(widget,
					"delivey_Details"));
			deliveyDetails.setParent(cardDetailsldiv);

			final Br br5 = new Br();
			br5.setParent(prepaidOrdeDetials);
			
			final Div prepaidButtonDiv = new Div();
			prepaidButtonDiv.setParent(prepaidOrdeDetials);
			prepaidButtonDiv.setClass(BOLD_TEXT);
			final Button processButton = new Button(LabelUtils.getLabel(widget,
					CONTINUE));
			processButton.setParent(prepaidButtonDiv);
			processButton.addEventListener(Events.ON_CLICK,
					new EventListener() {

						@Override
						public void onEvent(Event arg0) throws Exception {
							prepaidOrdeDetials
									.appendChild(getReturnModeDetails(widget,
											entry, pincode, orderModel));
						}
					});
		}
		return prepaidOrdeDetials;
	}

	private Div getReturnModeDetails(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> entry, final String pincode,
			final OrderModel subOrder) {

		final Div returnMethodDiv = new Div();

		final Div modelSelctionDiv = new Div();
		modelSelctionDiv.setParent(returnMethodDiv);
		modelSelctionDiv.setClass("modeDetails");
		final Label selectTitle = new Label(LabelUtils.getLabel(widget,
				"return_mode"));
		selectTitle.setParent(modelSelctionDiv);
		selectTitle.setClass(BOLD_TEXT);

		final Br br = new Br();
		br.setParent(modelSelctionDiv);

		final Div quickDropArea = new Div();
		quickDropArea.setParent(returnMethodDiv);

		final Div schedulePickupArea = new Div();
		schedulePickupArea.setParent(returnMethodDiv);
		schedulePickupArea.setClass("displayArea");

		final Div modes = new Div();
		modes.setParent(modelSelctionDiv);
		modes.setClass("returnModels");
		final Radiogroup radioGroup = new Radiogroup();
		modes.appendChild(radioGroup);
		Radio quickDrop = new Radio();
		quickDrop.setLabel(QUICK_DROP);
		quickDrop.setClass(BOLD_TEXT);
		quickDrop.addEventListener(Events.ON_CHECK, new EventListener() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				if (null != schedulePickupArea
						&& null != schedulePickupArea.getChildren()) {
					schedulePickupArea.getChildren().clear();
				}
				schedulePickupArea.appendChild(getQuickDropDetails(widget,
						entry, pincode));
			}
		});

		final Br br9 = new Br();
		br9.setParent(modelSelctionDiv);
		radioGroup.appendChild(quickDrop);
		Radio schedulePickup1 = new Radio();
		schedulePickup1.setLabel(SCHEDULE_PICKUP);
		schedulePickup1.addEventListener(Events.ON_CHECK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				if (null != schedulePickupArea
						&& null != schedulePickupArea.getChildren()) {
					schedulePickupArea.getChildren().clear();

				}
				schedulePickupArea.appendChild(getSchedulePicupDetails(widget,
						entry, subOrder));
			}
		});
		radioGroup.appendChild(schedulePickup1);
		radioGroup.setSelectedIndex(0);
		if (radioGroup.getSelectedIndex() == 0) {
			schedulePickupArea.appendChild(getQuickDropDetails(widget, entry,
					pincode));
		}

		return returnMethodDiv;
	}

	private Div getQuickDropDetails(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> entry, final String pincode) {

		final Div storeAdressDiv = new Div();
		storeAdressDiv.setClass("displayArea");

		Label quickDrop = new Label(LabelUtils.getLabel(widget, "quick_drop"));
		quickDrop.setParent(storeAdressDiv);
		quickDrop.setClass(BOLD_TEXT);

		final Br br5 = new Br();
		br5.setParent(storeAdressDiv);

		final Div listOfReturnItems = new Div();
		listOfReturnItems.setParent(storeAdressDiv);

		final Listbox listbox = new Listbox();
		listbox.setParent(listOfReturnItems);
		listbox.setClass("selectedMode");

		final Listhead listHead = new Listhead();
		listHead.setParent(listbox);

		final Listheader transId = new Listheader("TransactionID");
		transId.setParent(listHead);

		final Listheader productDesc = new Listheader("Product Name");
		productDesc.setParent(listHead);

		final Listheader returnStore = new Listheader("Available Stores");
		returnStore.setParent(listHead);
		final List<Checkbox> storeChecks = new ArrayList<Checkbox>();
		for (AbstractOrderEntryModel e : entry) {

			final Listitem listItem = new Listitem();
			listItem.setParent(listbox);

			returnableStores = ((MarketPlaceReturnsController) widget
					.getWidgetController()).getAllReturnableStores(pincode,
					StringUtils.substring(e.getSelectedUSSID(), 0, 6));
			final Listcell cellTransId = new Listcell(e.getTransactionID());
			cellTransId.setParent(listItem);

			final Listcell cellProdDesc = new Listcell(e.getProduct().getName());
			cellProdDesc.setParent(listItem);

			final Listcell cellAvailStores = new Listcell();
			cellAvailStores.setParent(listItem);

			final Div storesAvailList = new Div();
			storesAvailList.setParent(cellAvailStores);
			for(PointOfServiceData stores : returnableStores) {
				final Div storeDetails = new Div();
				storeDetails.setParent(storesAvailList);
				Checkbox storesSelection = new Checkbox();
				storesSelection.setAttribute(stores.getSlaveId(), stores);
				storesSelection.setLabel(stores.getDisplayName());
				storesSelection.setParent(storeDetails);
				final Br storeAddBr = new Br();
				storeAddBr.setParent(storeDetails);
				final Label storeLabel = new Label(stores.getAddress().getCity()+ "," + stores.getAddress().getLine1()+ ", "+stores.getAddress().getPostalCode());
				storeLabel.setParent(storeDetails);
				storeChecks.add(storesSelection);
			}
			final Br storeBr = new Br();
			storeBr.setParent(listOfReturnItems);
		}

		final Br br4 = new Br();
		br4.setParent(storeAdressDiv);
		final Div buttonDiv = new Div();
		buttonDiv.setParent(storeAdressDiv);
		buttonDiv.setClass(BUTTON);
		final Button continueBt = new Button(LabelUtils.getLabel(widget,
				CONTINUE));
		continueBt.setParent(buttonDiv);
		continueBt.setClass("creditButton");
		continueBt.setClass(BUTTON);
		continueBt.addEventListener(Events.ON_CLICK, new EventListener() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				retrunInfoCallToOMS(widget, entry, returnableStores, storeChecks);
				Messagebox.show(LabelUtils.getLabel(widget,
						"quickDropMessage", new Object[0]), LabelUtils
						.getLabel(widget, "quickDropStatus",
								new Object[0]), Messagebox.OK
						| Messagebox.CANCEL, Messagebox.INFORMATION,
						new EventListener() {

							@Override
							public void onEvent(Event event)
									throws Exception {
								if (event.getName().equals("onOK")) {
									widget.cleanup();
									return;
								} 
							}
						});
			}
		});
		return storeAdressDiv;
	}

	private Div getSchedulePicupDetails(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> entry, OrderModel subOrder) {

		final Div scheduleDiv = new Div();

		final Label scheduleHeader = new Label(LabelUtils.getLabel(widget,
				"schedule_pickUp"));
		scheduleHeader.setParent(scheduleDiv);
		scheduleHeader.setClass(BOLD_TEXT);

		final Br br6 = new Br();
		br6.setParent(scheduleDiv);

		final Div listBoxOfSchedulepick = new Div();
		listBoxOfSchedulepick.setParent(scheduleDiv);

		final Listbox scheduleList = new Listbox();
		scheduleList.setParent(listBoxOfSchedulepick);

		final Listhead listhead = new Listhead();
		listhead.setParent(scheduleList);

		final Listheader headerTransId = new Listheader("Transaction Id");
		headerTransId.setParent(listhead);

		final Listheader headerProdName = new Listheader("Product Name");
		headerProdName.setParent(listhead);
		
		final Listheader headerPrice = new Listheader("Total Price");
		headerPrice.setParent(listhead);
		for (AbstractOrderEntryModel e : entry) {

			Listitem listItem = new Listitem();
			listItem.setParent(scheduleList);

			Listcell transIdCell = new Listcell(e.getTransactionID());
			transIdCell.setParent(listItem);

			Listcell productCell = new Listcell(e.getProduct().getName());
			productCell.setParent(listItem);

			Listcell productCost = new Listcell();
			productCost.setParent(listItem);
			
			Listcell costCell = new Listcell(e.getTotalPrice().toString());
			costCell.setParent(listItem);
		}

		final Br br7 = new Br();
		br7.setParent(scheduleDiv);
		final Div buttonDiv = new Div();
		buttonDiv.setParent(scheduleDiv);
		buttonDiv.setClass(BUTTON);
		final Button buttonBr1 = new Button(LabelUtils.getLabel(widget,
				CONTINUE));
		buttonBr1.setParent(buttonDiv);
		buttonBr1.setClass("creditButton");
		buttonBr1.setClass(BUTTON);
		buttonBr1.addEventListener(Events.ON_CLICK,
				createReturnRequestCreateEventListener(widget));
		return scheduleDiv;
	}

	protected EventListener createReturnRequestCreateEventListener(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget) {
		return new ReturnRequestCreateEventListener(widget);
	}

	protected class ReturnRequestCreateEventListener implements EventListener {
		private final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget;

		public ReturnRequestCreateEventListener(
				InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget) {
			this.widget = widget;
		}

		public void onEvent(Event event) throws Exception {
			createRefundConfirmationPopupWindow(widget, getPopupWidgetHelper()
					.getCurrentPopup().getParent());
		}
	}

	private void retrunInfoCallToOMS(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> returnEntry,
			List<PointOfServiceData> listOfStores,List<Checkbox> storeChecks ) {
		for (AbstractOrderEntryModel entry : returnEntry) {
			((MarketPlaceReturnsController) widget.getWidgetController())
					.retrunInfoCallToOMSFromCsCockpit(widget, entry, storeChecks);
		}
	}

	private void codUpadatesToFico(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			List<AbstractOrderEntryModel> returnEntry, Textbox accNoTextbox,
			Textbox accHolderNameTextbox, Listbox refundModeListbox,
			Listbox titleListBox, Textbox bankNameTextbox, Textbox ifscTextbox) {
		CODSelfShipData codData = new CODSelfShipData();
		codData.setTitle((String) titleListBox.getSelectedItem().getLabel());
		codData.setName(accHolderNameTextbox.getValue());
		codData.setBankAccount(accNoTextbox.getValue());
		codData.setBankName(bankNameTextbox.getValue());
		codData.setBankKey(ifscTextbox.getValue());
		codData.setPaymentMode((String) refundModeListbox.getSelectedItem()
				.getLabel());
		((MarketPlaceReturnsController) widget.getWidgetController())
				.getCodPaymentInfoToFICO(codData, returnEntry);
	}
}
