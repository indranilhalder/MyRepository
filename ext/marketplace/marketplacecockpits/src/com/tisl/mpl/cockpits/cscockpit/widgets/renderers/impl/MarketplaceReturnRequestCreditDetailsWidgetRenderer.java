package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
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
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import bsh.ParseException;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceReturnsController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.core.constants.GeneratedMarketplaceCoreConstants.Enumerations.TypeofReturn;
import com.tisl.mpl.core.enums.RefundMode;
import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.impl.MplCheckoutFacadeImpl;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.data.RescheduleData;
import com.tisl.mpl.facades.populators.CustomAddressReversePopulator;
import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.xml.pojo.OrderLineDataResponse;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetContainer;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetContainer;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetFactory;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.commercefacades.storelocator.converters.populator.PointOfServicePopulator;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.ThirdPartyWalletInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.TypeUtils;
import de.hybris.platform.cscockpit.widgets.controllers.ReturnsController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.ReturnRequestCreateWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class MarketplaceReturnRequestCreditDetailsWidgetRenderer extends
ReturnRequestCreateWidgetRenderer {
	private static final Logger LOG = Logger.getLogger(MarketplaceReturnRequestCreditDetailsWidgetRenderer.class);
	@Autowired
	private PopupWidgetHelper popupWidgetHelper;
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private TypeService cockpitTypeService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private Populator<AddressModel, AddressData> addressPopulator;
	@Autowired
	private PointOfServicePopulator pointOfServicePopulator;
	@Autowired
	private CustomAddressReversePopulator addressReversePopulator; 
    @Autowired
    private AccountAddressFacade accountAddressFacade;
    
    @Autowired 
    private CustomerAccountService customerAccountService;
    @Autowired
	private MplCheckoutFacadeImpl mplCheckoutFacadeImpl;
    
	private static final String TEXTBOX = "returntext";
	private static final String CONTINUE = "continue";
	private static final String BUTTON = "creditButton";
	private static final String BOLD_TEXT = "boldText";
	private static final String ADD = "Add";
	public static final String REVERSE_LOGISTICS_NOTAVAILABLE = "reverselogisticnotavailable";
	private static final String REVERSE_LOGISTICS_AVAILABLE = "reverselogisticavailable";
	private static final String REVERSE_LOGISTICS_PARTIALAVAILABLE = "reverselogisticpartialavailable";
	private static final String NO_RESPONSE_FROM_SERVER = "noResponseFromServer";
	private static final String SELECT_RETURN_ADDRESS = "selectReturnAddress";

	private static final String FAILED_TO_VALIDATE_PINCODE_FEILD = "FailedToValidatePinCode";
	private static final String ENTER_VALUES_IN_ALLFIELDS = "enterValuesInAllFields";
	private static final String PIN_REGEX = "^[1-9][0-9]{5}";
	private static final String IFSC_REGEX="^[A-Za-z][A-Za-z]{3}[0-0][A-Za-z0-9]{6}";
	
	private static final String INFO = "info";
	protected static final String NO_STORE_AVAILABLE = "noStoresAvailable";
	protected static final String NO_STORE_SELECTED = "noStoreSelected";
	protected static final String FORM_ERROS = null;
	protected static final String FORM_ERRORS = null;
	protected static final String FAILED_VALIDATION ="validationErrors";
	
	protected static final String TIME 	= "  Working Hours : ";
	protected static final String AM	= " am  to ";
	protected static final String PM	= " pm ";


	@Override
	protected HtmlBasedComponent createContentInternal(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			HtmlBasedComponent rootContainer) {

		Div AccountDetailsDiv = null;
		String paymentType = null;
		PaymentTransactionModel paymentTransation = null;
		final Div bankDetailsArea = new Div();
		
		final TypedObject order = (TypedObject) ((ReturnsController) widget
				.getWidgetController()).getRefundOrderPreview();
		final OrderModel refundOrder = (OrderModel) order.getObject();
		 List<AbstractOrderEntryModel> orderEntries = new ArrayList<AbstractOrderEntryModel>();

		for (AbstractOrderEntryModel entry : refundOrder.getEntries()) {
			if(entry.getQuantity() == 0){
				orderEntries.add(entry);
			}
		}
		if (refundOrder != null && refundOrder.getParentReference() != null) {
			paymentTransation = refundOrder.getPaymentTransactions().get(0);
			paymentType = paymentTransation.getEntries().get(0)
					.getPaymentMode().getMode();
		}
		if (paymentType != null
				&& paymentType
						.equalsIgnoreCase(MarketplaceCockpitsConstants.PAYMENT_MODE_COD)) {
			AccountDetailsDiv = collectBankDetailsForCodOrder(widget, orderEntries,
					refundOrder);
			AccountDetailsDiv.setParent(bankDetailsArea);
		} else if (paymentType != null) {
			AccountDetailsDiv = populatePrePaidPaymentDetails(widget, refundOrder,
					bankDetailsArea, orderEntries);
			AccountDetailsDiv.setParent(bankDetailsArea);
		}
		return bankDetailsArea;
	}

	private Textbox createTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("30%");
		textBox.setParent(parent);
		return textBox;
	}
	
	private Longbox createLongBox(final Div parent) {
		final Longbox longBox = new Longbox();
		longBox.setWidth("30%");
		longBox.setParent(parent);
		return longBox;
	}

	private Div collectBankDetailsForCodOrder(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> entries, 
			final OrderModel orderModel) {

		CODSelfShipData codSelfShipData = null;
		try{
			if(null!=orderModel.getUser()){
				codSelfShipData = ((MarketPlaceReturnsController)widget.getWidgetController()).getCustomerBankDetailsByCustomerId(orderModel.getUser().getUid());
			}
		}
		catch(EtailNonBusinessExceptions e)
		{
			LOG.error("Exception occured for fecting CUstomer Bank details for customer ID :"+orderModel.getUser().getUid()+" Actual Stack trace "+e);
		}

		final Div bankDetailsDiv = new Div();	
		// Account Number 
		Div accNoDiv = new Div();
		accNoDiv.setParent(bankDetailsDiv);
		accNoDiv.setClass(TEXTBOX);
		Label accNo = new Label(LabelUtils.getLabel(widget, "accNo"));
		accNo.setClass("AcntNumberLabel");
		accNo.setParent(accNoDiv);
		final Textbox accNoTextbox = createTextbox(accNoDiv);      
		accNoTextbox.setParent(accNoDiv);
		if(null != codSelfShipData && null !=codSelfShipData.getBankAccount()) {
			accNoTextbox.setValue(codSelfShipData.getBankAccount());
		}
		accNoTextbox.setClass("accHolderNameTextbox");
		accNoTextbox.setMaxlength(24);
		accNoTextbox.setType("password");
		String errorMsgAcntNumber = LabelUtils.getLabel(widget,
				"error.msg.acntNumber", new Object[0]);
		accNoTextbox.setConstraint("/[0-9][0-9]*$/:"+errorMsgAcntNumber);
		// Re - Enter Account Number 
		Label re_accNo = new Label(LabelUtils.getLabel(widget, "re_accNo"));
		re_accNo.setParent(accNoDiv);
		re_accNo.setClass("reEntrAcntnumber");
		final Textbox re_accNoTextBox = createTextbox(accNoDiv);
		re_accNoTextBox.setParent(accNoDiv);
		if(null != codSelfShipData && null !=codSelfShipData.getBankAccount()) {
			re_accNoTextBox.setValue(codSelfShipData.getBankAccount());
		}
		re_accNoTextBox.setClass("accHolderNameTextbox");
		re_accNoTextBox.setMaxlength(24);
		 String errorMsgReEnterAcntNumber = LabelUtils.getLabel(widget,
					"error.msg.acntNumber", new Object[0]);
		re_accNoTextBox.setConstraint("/[0-9][0-9]*$/:"+errorMsgReEnterAcntNumber);

		// Account Holder's Name 
		Div acntholderDiv = new Div();
		acntholderDiv.setParent(bankDetailsDiv);
		acntholderDiv.setClass(TEXTBOX);
		final Label accHolderName = new Label(LabelUtils.getLabel(widget,"accName"));
		accHolderName.setParent(acntholderDiv);
		accHolderName.setClass("acntHolderName");
		accHolderName.setWidth("10px");
		final Textbox accHolderNameTextbox = createTextbox(acntholderDiv);
		accHolderNameTextbox.setParent(acntholderDiv);
		accHolderNameTextbox.setMaxlength(30);
		if(null != codSelfShipData && null !=codSelfShipData.getName()) {
			accHolderNameTextbox.setValue(codSelfShipData.getName());
		}
		accHolderNameTextbox.setClass("accHolderNameTextbox");
		String errorMsgName = LabelUtils.getLabel(widget,
				"error.msg.name", new Object[0]);
		accHolderNameTextbox.setConstraint("/[a-zA-Z][a-zA-Z ]*$/:"+errorMsgName);
		
		// RefundMode 
		final Label refundMode = new Label(LabelUtils.getLabel(widget,"refund_mode"));
		refundMode.setParent(acntholderDiv);
		refundMode.setClass("reEntrAcntnumber");
		final Listbox refundModeListbox = new Listbox();
		refundModeListbox.setParent(acntholderDiv);
		refundModeListbox.setClass("refundModedropDown");
		refundModeListbox.setWidth("80px");
		refundModeListbox.setVisible(true);
		refundModeListbox.setMold("select");
		final List<String> refundModeList = new ArrayList<String>();
		refundModeList.add(RefundMode.NEFT.getCode());
		for (String modes : refundModeList) {
			final Listitem refundModeItems = new Listitem(modes);
			refundModeItems.setParent(refundModeListbox);
			refundModeItems.setValue(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(refundModeItems.getLabel()));
			refundModeItems.setSelected(true);
		}
		
		// Title 
		final Label titleLabel = new Label("Title");
		titleLabel.setParent(acntholderDiv);
		titleLabel.setClass("reEntrAcntnumber");
		final Listbox titleListBox = new Listbox();
		titleListBox.setParent(acntholderDiv);
		titleListBox.setClass("refundModedropDown");
		titleListBox.setWidth("100px");
		titleListBox.setMold("select");
		List<String> titleList = new ArrayList<String>();
		titleList.add("MR");
		titleList.add("MRs");
		titleList.add("Company");
		for (String title : titleList) {
			final Listitem titleItem = new Listitem(title);
			titleItem.setParent(titleListBox);
			if(null != codSelfShipData && null !=codSelfShipData.getTitle()) {
				if(title.equalsIgnoreCase(codSelfShipData.getTitle())) {
					titleItem.setSelected(true);
				}
			}
		}
		if(null == titleListBox.getSelectedItem() ) {
			titleListBox.setSelectedIndex(0);
		}
		
		// Bank Name 
		final Div banNameDiv = new Div();
		banNameDiv.setParent(bankDetailsDiv);
		banNameDiv.setClass(TEXTBOX);
		final Label bankName = new Label(LabelUtils.getLabel(widget,"bank_name"));
		bankName.setParent(banNameDiv);
		bankName.setClass("bankNameLabel");
		final Textbox bankNameTextbox = createTextbox(banNameDiv);
		bankNameTextbox.setParent(banNameDiv);
		bankNameTextbox.setMaxlength(30);
		if(null != codSelfShipData && null !=codSelfShipData.getBankName()) {
			bankNameTextbox.setValue(codSelfShipData.getBankName());
		}
		bankNameTextbox.setClass("accHolderNameTextbox");
		String errorMsgBankName = LabelUtils.getLabel(widget,
				"error.msg.bankName", new Object[0]);
		bankNameTextbox.setConstraint("/[a-zA-Z][a-zA-Z ]*$/:"+errorMsgBankName);
		
		// IFSC CODE Details
		final Label ifscCode = new Label(LabelUtils.getLabel(widget, "IFCS"));
		ifscCode.setParent(banNameDiv);
		ifscCode.setClass("reEntrAcntnumber");
		final Textbox ifscTextbox = createTextbox(banNameDiv);
		ifscTextbox.setMaxlength(11);
		ifscTextbox.setParent(banNameDiv);
		if(null != codSelfShipData && null !=codSelfShipData.getBankKey()) {
			ifscTextbox.setValue(codSelfShipData.getBankKey());
		}
		ifscTextbox.setClass("accHolderNameTextbox");	
		final Div codButtonDiv = new Div();
		codButtonDiv.setParent(bankDetailsDiv);
		codButtonDiv.setClass(BUTTON);
		final Button codButton = new Button(LabelUtils.getLabel(widget,
				CONTINUE));
		codButton.setParent(codButtonDiv);
		codButton.addEventListener(Events.ON_CLICK, new EventListener() {
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				
				if(null == accNoTextbox.getValue() || StringUtils.isEmpty(accNoTextbox.getValue().trim()) || accNoTextbox.getValue().length()<4) {
					
					Messagebox.show(
							LabelUtils.getLabel(widget, "InvalidAcntNumber"),
							LabelUtils.getLabel(widget, FAILED_VALIDATION),
							Messagebox.OK, Messagebox.ERROR);
					accNoTextbox.setFocus(true);
					return;
				}else if  (null==re_accNoTextBox.getValue() || StringUtils.isEmpty(re_accNoTextBox.getValue().trim())) {
					Messagebox.show(LabelUtils.getLabel(widget,
							"InvalidAcntNumber",
							Messagebox.OK, Messagebox.ERROR
			     	));
					re_accNoTextBox.setFocus(true);
					return;
				} 
				else if  (!re_accNoTextBox.getValue().trim().equalsIgnoreCase(accNoTextbox.getValue().trim()) ) {
					Messagebox.show(
							LabelUtils.getLabel(widget, "AcntNumbersDoes'tMatch"),
							LabelUtils.getLabel(widget, FAILED_VALIDATION),
							Messagebox.OK, Messagebox.ERROR);
					re_accNoTextBox.setFocus(true);
					return;
				} else if (null == accHolderNameTextbox.getValue() || StringUtils.isEmpty(accHolderNameTextbox.getValue().trim()) || accHolderNameTextbox.getValue().length()<3) {
					Messagebox.show(
							LabelUtils.getLabel(widget, "invalidAcntHolderName"),
							LabelUtils.getLabel(widget, FAILED_VALIDATION),
							Messagebox.OK, Messagebox.ERROR);
					accHolderNameTextbox.setFocus(true);
						return;
				}else if (null ==bankNameTextbox.getValue() || StringUtils.isEmpty(bankNameTextbox.getValue().trim()) ||  bankNameTextbox.getValue().length()<3) {
					Messagebox.show(
							LabelUtils.getLabel(widget, "invalidBankName"),
							LabelUtils.getLabel(widget, FAILED_VALIDATION),
							Messagebox.OK, Messagebox.ERROR);
					bankNameTextbox.setFocus(true);
						return;
				} else if ( null ==ifscTextbox.getValue() ||   StringUtils.isEmpty(ifscTextbox.getValue().trim()) || ifscTextbox.getValue().length()<11 ||  !ifscTextbox.getValue().trim().matches(IFSC_REGEX)) {
					Messagebox.show(
							LabelUtils.getLabel(widget, "invaliIfscCode"),
							LabelUtils.getLabel(widget, FAILED_VALIDATION),
							Messagebox.OK, Messagebox.ERROR);
					ifscTextbox.setFocus(true);
					return;
				}
				saveCODReturnsBankDetails(widget, entries, accNoTextbox,re_accNoTextBox,
							accHolderNameTextbox, refundModeListbox, titleListBox,
							bankNameTextbox, ifscTextbox);
					codButton.setDisabled(true);
					bankDetailsDiv.appendChild(getReturnModeDetails(widget, entries, orderModel));
			}
		});
		return bankDetailsDiv;
	}

	private Div populatePrePaidPaymentDetails(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final OrderModel orderModel,
			Div bankDetailsArea, final List<AbstractOrderEntryModel> entry) {
		final Div prepaidOrdeDetials = new Div();
		CreditCardPaymentInfoModel cCard = null;
		ThirdPartyWalletInfoModel mRupeeCart=null;
		DebitCardPaymentInfoModel dCart = null;
		PaymentTransactionModel payment=null;
		if(null != orderModel.getPaymentTransactions() && null != orderModel.getPaymentTransactions().get(0)) {
		 payment = orderModel.getPaymentTransactions()
				.get(0);
		}
		if (orderModel.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {
			cCard = (CreditCardPaymentInfoModel) orderModel.getPaymentInfo();
			final Div retunDetails = new Div();
			retunDetails.setParent(prepaidOrdeDetials);
			retunDetails.setClass("detailsClasss");
			final Label returnHead = new Label(LabelUtils.getLabel(widget,
					"returnDetails"));
			returnHead.setParent(retunDetails);
			returnHead.setClass(BOLD_TEXT);
			final Div Div1 = new Div();
			Div1.setParent(retunDetails);
			final Label returnDescription = new Label(LabelUtils.getLabel(widget,
					"return_Description",
					new Object[] {
							payment.getEntries().get(0)
							.getPaymentMode().getMode()}));

			returnDescription.setParent(retunDetails);
		   final Div cardDetailsldiv = new Div();
			cardDetailsldiv.setParent(prepaidOrdeDetials);
			cardDetailsldiv.setClass("cardDetailsClass");
			final Label cardDetails = new Label(LabelUtils.getLabel(widget,
					"card_Details",
					new Object[] {
					cCard.getType()  }));
			
			cardDetails.setParent(cardDetailsldiv);
			cardDetails.setClass(BOLD_TEXT);
			
			final Div Div2 = new Div();
			Div2.setParent(cardDetailsldiv);
			final Label nameOnCart = new Label(LabelUtils.getLabel(widget,
				"nameOnCart",
				new Object[] {
				cCard.getCcOwner() }));
		    nameOnCart.setParent(cardDetailsldiv);
		    
			final Div Div3 = new Div();
			Div3.setParent(cardDetailsldiv);
			final Label cardNo = new Label(LabelUtils.getLabel(widget,
					"cartNumber",
					new Object[] {
					cCard.getType(),cCard.getNumber()  }));
			cardNo.setParent(cardDetailsldiv);
			cardNo.setClass(BOLD_TEXT);
			
			final Div Div4 = new Div();
			Div4.setParent(cardDetailsldiv);
			final Label deliveyDetails = new Label(LabelUtils.getLabel(widget,
					"expiresOn",
					new Object[] {
					cCard.getType(),cCard.getValidToMonth(),cCard.getValidToMonth(),cCard.getValidToYear() }));
			deliveyDetails.setParent(cardDetailsldiv);
			
			final Div Div5 = new Div();
			Div5.setParent(prepaidOrdeDetials);
			
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
							processButton.setDisabled(true);
							prepaidOrdeDetials
									.appendChild(getReturnModeDetails(widget,
											entry,orderModel));
						}
					});
		}else  if(orderModel.getPaymentInfo() instanceof DebitCardPaymentInfoModel){

			dCart = (DebitCardPaymentInfoModel) orderModel.getPaymentInfo();
			final Div retunDetails = new Div();
			retunDetails.setParent(prepaidOrdeDetials);
			retunDetails.setClass("detailsClasss");
			final Label returnHead = new Label(LabelUtils.getLabel(widget,
					"returnDetails"));
			returnHead.setParent(retunDetails);
			returnHead.setClass(BOLD_TEXT);
			final Div Div1 = new Div();
			Div1.setParent(retunDetails);
			final Label returnDescription = new Label(LabelUtils.getLabel(widget,
					"return_Description",
					new Object[] {
							payment.getEntries().get(0)
							.getPaymentMode().getMode()}));

			returnDescription.setParent(retunDetails);
		   final Div cardDetailsldiv = new Div();
			cardDetailsldiv.setParent(prepaidOrdeDetials);
			cardDetailsldiv.setClass("cardDetailsClass");
			final Label cardDetails = new Label(LabelUtils.getLabel(widget,
					"card_Details",
					new Object[] {
					dCart.getType()  }));
			
			cardDetails.setParent(cardDetailsldiv);
			cardDetails.setClass(BOLD_TEXT);
			
			final Div Div2 = new Div();
			Div2.setParent(cardDetailsldiv);
			final Label nameOnCart = new Label(LabelUtils.getLabel(widget,
				"nameOnCart",
				new Object[] {
					dCart.getCcOwner() }));
		    nameOnCart.setParent(cardDetailsldiv);
		    
			final Div Div3 = new Div();
			Div3.setParent(cardDetailsldiv);
			final Label cardNo = new Label(LabelUtils.getLabel(widget,
					"cartNumber",
					new Object[] {
					dCart.getType(),dCart.getNumber()  }));
			cardNo.setParent(cardDetailsldiv);
			cardNo.setClass(BOLD_TEXT);
			
			final Div Div4 = new Div();
			Div4.setParent(cardDetailsldiv);
			final Label deliveyDetails = new Label(LabelUtils.getLabel(widget,
					"expiresOn",
					new Object[] {
					dCart.getType(),dCart.getValidToMonth(),dCart.getValidToMonth(),dCart.getValidToYear() }));
			deliveyDetails.setParent(cardDetailsldiv);
			
			final Div Div5 = new Div();
			Div5.setParent(prepaidOrdeDetials);
			
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
							processButton.setDisabled(true);
							prepaidOrdeDetials
									.appendChild(getReturnModeDetails(widget,
											entry,orderModel));
						}
					});
		
		}
		//Mrupee changes Sprint 8 started
		
		else  if(orderModel.getPaymentInfo() instanceof ThirdPartyWalletInfoModel){

			mRupeeCart = (ThirdPartyWalletInfoModel) orderModel.getPaymentInfo();
			final Div retunDetails = new Div();
			retunDetails.setParent(prepaidOrdeDetials);
			retunDetails.setClass("detailsClasss");
			final Label returnHead = new Label(LabelUtils.getLabel(widget,
					"returnDetails"));
			returnHead.setParent(retunDetails);
			returnHead.setClass(BOLD_TEXT);
			final Div Div1 = new Div();
			Div1.setParent(retunDetails);
			final Label returnDescription = new Label(LabelUtils.getLabel(widget,
					"return_Description",
					new Object[] {
							payment.getEntries().get(0)
							.getPaymentMode().getMode()}));

			returnDescription.setParent(retunDetails);
		   final Div cardDetailsldiv = new Div();
			cardDetailsldiv.setParent(prepaidOrdeDetials);
			cardDetailsldiv.setClass("cardDetailsClass");
			final Label cardDetails = new Label(LabelUtils.getLabel(widget,
					"card_Details",
					new Object[] {
							mRupeeCart.getProviderName()}));
			
			cardDetails.setParent(cardDetailsldiv);
			cardDetails.setClass(BOLD_TEXT);
			
			final Div Div2 = new Div();
			Div2.setParent(cardDetailsldiv);
			final Label nameOnCart = new Label(LabelUtils.getLabel(widget,
				"nameOnCart",
				new Object[] {
						mRupeeCart.getWalletOwner() }));
		    nameOnCart.setParent(cardDetailsldiv);

		    final Div Div5 = new Div();
			Div5.setParent(prepaidOrdeDetials);
			
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
							processButton.setDisabled(true);
							prepaidOrdeDetials
									.appendChild(getReturnModeDetails(widget,
											entry,orderModel));
						}
					});	
		}
		//Mrupee changes Sprint 8 end
		 // PRDI-133 START 
		else {
			final Div retunDetails = new Div();
			retunDetails.setParent(prepaidOrdeDetials);
			retunDetails.setClass("detailsClasss");
			final Label returnHead = new Label(LabelUtils.getLabel(widget,
					"returnDetails"));
			returnHead.setParent(retunDetails);
			returnHead.setClass(BOLD_TEXT);
			final Div Div1 = new Div();
			Div1.setParent(retunDetails);
			final Label returnDescription = new Label(LabelUtils.getLabel(widget,
					"return_Description",
					new Object[] {
							payment.getEntries().get(0)
							.getPaymentMode().getMode()}));

			returnDescription.setParent(retunDetails);
		    final Div cardDetailsldiv = new Div();
			cardDetailsldiv.setParent(prepaidOrdeDetials);
			cardDetailsldiv.setClass("cardDetailsClass");
			
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
							processButton.setDisabled(true);
							prepaidOrdeDetials
									.appendChild(getReturnModeDetails(widget,
											entry,orderModel));
						}
					});
		}
		// PRDI-133 END 
		return prepaidOrdeDetials;
	}

	private Div getReturnModeDetails(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> entries,
			final OrderModel subOrder) {
		final Div returnMethodDiv = new Div();
		final Div modelSelctionDiv = new Div();
		modelSelctionDiv.setParent(returnMethodDiv);
		modelSelctionDiv.setClass("modeDetails");
		final Label selectTitle = new Label(LabelUtils.getLabel(widget,
				"return_mode"));
		selectTitle.setParent(modelSelctionDiv);
		selectTitle.setClass(BOLD_TEXT);
		final Div Div = new Div();
		Div.setParent(modelSelctionDiv);
		//final Div quickDropArea = new Div();
		//quickDropArea.setParent(returnMethodDiv);
		final Div schedulePickupArea = new Div();
		schedulePickupArea.setParent(returnMethodDiv);
		schedulePickupArea.setClass("displayArea");
		final Div modes = new Div();
		modes.setParent(modelSelctionDiv);
		modes.setClass("returnModels");
		final Radiogroup radioGroup = new Radiogroup();
		modes.appendChild(radioGroup);
		boolean isOrderEligibleForQuickDrop = ((MarketPlaceReturnsController)widget.getWidgetController()).checkProductEligibilityForRTS(entries);
		boolean isFineJewellery = ((MarketPlaceReturnsController)widget.getWidgetController()).checkIfFineJewellery(entries);
		if(isOrderEligibleForQuickDrop) {
			Radio quickDrop = new Radio();
			radioGroup.appendChild(quickDrop);
			quickDrop.setLabel(TypeofReturn.QUICK_DROP);
			quickDrop.setClass(BOLD_TEXT);
			quickDrop.addEventListener(Events.ON_CHECK, new EventListener() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					if (null != schedulePickupArea
							&& null != schedulePickupArea.getChildren()) {
						schedulePickupArea.getChildren().clear();
					}
					schedulePickupArea.appendChild(getQuickDropDetails(widget,
							entries));
				}
			});
		}
		final Div Div9 = new Div();
		Div9.setParent(modelSelctionDiv);
		Radio schedulePickup = new Radio();
		radioGroup.appendChild(schedulePickup);
		schedulePickup.setLabel(TypeofReturn.SCHEDULE_PICKUP);
		schedulePickup.addEventListener(Events.ON_CHECK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				if (null != schedulePickupArea
						&& null != schedulePickupArea.getChildren()) {
					schedulePickupArea.getChildren().clear();
				}
				schedulePickupArea.appendChild(getSchedulePicupDetails(widget,
						entries, subOrder));
			}
		});
		
		//JWLSPCUAT-1405 If condition added
		if(!isFineJewellery){
		Div9.setParent(modelSelctionDiv);
		Radio selfShipRadio = new Radio();
		selfShipRadio.setLabel(TypeofReturn.SELF_COURIER);
		radioGroup.appendChild(selfShipRadio);
		selfShipRadio.addEventListener(Events.ON_CHECK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				if (null != schedulePickupArea
						&& null != schedulePickupArea.getChildren()) {
					schedulePickupArea.getChildren().clear();
				}
				schedulePickupArea.appendChild(getselfShipDetails(widget,
						entries, subOrder,TypeofReturn.SELF_COURIER));
			}
		});
		}
		radioGroup.setSelectedIndex(0);
		if (null != radioGroup.getSelectedItem() && null != radioGroup.getSelectedItem().getLabel()) {
			try {
				if(radioGroup.getSelectedItem().getLabel().equalsIgnoreCase(TypeofReturn.QUICK_DROP)) {
					schedulePickupArea.appendChild(getQuickDropDetails(widget, entries));
				}else if(radioGroup.getSelectedItem().getLabel().equalsIgnoreCase(TypeofReturn.SCHEDULE_PICKUP)){
					schedulePickupArea.appendChild(getSchedulePicupDetails(widget, entries,subOrder));
				}else {
					schedulePickupArea.appendChild(getselfShipDetails(widget, entries,subOrder,TypeofReturn.SELF_COURIER));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return returnMethodDiv;
	}

	protected Component getselfShipDetails(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			List<AbstractOrderEntryModel> entry, OrderModel subOrder,final String typeOfReturn) {
		final Div content = new Div();
		content.setClass("displayArea");
		Label selfCourierHeading = new Label(LabelUtils.getLabel(widget, "self-courier"));
		selfCourierHeading.setClass(BOLD_TEXT);
		selfCourierHeading.setParent(content);
		      
		final Div selfCourierAreaDiv = new Div();
		selfCourierAreaDiv.setParent(content);
		Label selfCourier = new Label(LabelUtils.getLabel(widget, "sendViaCourier"));
		selfCourier.setParent(selfCourierAreaDiv);
		Div div1 = new Div();
		div1.setParent(selfCourierAreaDiv);
		Label step1SelfCourier = new Label(LabelUtils.getLabel(widget, "Step1InSelfCourier"));
		step1SelfCourier.setParent(div1);
		Div div2 = new Div();
		div2.setParent(selfCourierAreaDiv);
		Label step2SelfCourier = new Label(LabelUtils.getLabel(widget, "Step2InSelfCourier"));
		step2SelfCourier.setParent(div2);
		
		Div div3 = new Div();
		div3.setParent(selfCourierAreaDiv);
		Label step3SelfCourier = new Label(LabelUtils.getLabel(widget, "Step3InSelfCourier"));
		step3SelfCourier.setParent(div3);
		
		final Div buttonDiv = new Div();
		buttonDiv.setParent(content);
		buttonDiv.setClass(BUTTON);
		final Button continueButton = new Button(LabelUtils.getLabel(widget,
				CONTINUE));
		continueButton.setParent(buttonDiv);
		continueButton.setClass("creditButton");
		continueButton.setClass(BUTTON);
		continueButton.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(final Event event) throws InterruptedException,
			ParseException, InvalidKeyException,
			NoSuchAlgorithmException {
				createReturnRequestEventListener(widget,typeOfReturn);
			}
		});
		
		selfCourier.setClass(BOLD_TEXT);
		return content;
	}

	

	protected void createReturnRequestEventListener(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget, String typeOfReturn) {
		proceedToReturnItem(widget,typeOfReturn);
			
	}

	private void proceedToReturnItem(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget, String typeOfReturn) {
		Session session = Executions.getCurrent().getDesktop().getSession();
		session.setAttribute("typeofReturn", typeOfReturn);
		createRefundConfirmationPopupWindow(widget, getPopupWidgetHelper()
				.getCurrentPopup().getParent());
		
	}
	
	@Override
	protected Window createRefundConfirmationPopupWindow(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> parentWidget,
			Component parentWindow) {
		WidgetContainer widgetContainer = new DefaultWidgetContainer(
				new DefaultWidgetFactory());
		Widget popupWidget = createPopupWidget(widgetContainer,
				"csRefundConfirmationWidgetConfig",
				"csRefundConfirmationWidget-Popup");

		Window popup = new Window();
		popup.setSclass("csRefundConfirmationWidget");
		popup.appendChild(popupWidget);
		popup.setTitle(LabelUtils.getLabel(popupWidget,
				"popup.refundConfirmationTitle", new Object[0]));
		popup.setParent(parentWindow);
		popup.doHighlighted();
		popup.setClosable(true);
		popup.setWidth("900px");

		return popup;
	}

	private Div getQuickDropDetails(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> entries) throws InterruptedException {
		final Map<String, List<String>>  storesMap = new HashMap<String, List<String>>();
		final Div storeAdressDiv = new Div();
		storeAdressDiv.setClass("displayArea");
		Label quickDrop = new Label(LabelUtils.getLabel(widget, "quick_drop"));
		quickDrop.setParent(storeAdressDiv);
		quickDrop.setClass(BOLD_TEXT);
		final Br br5 = new Br();
		br5.setParent(storeAdressDiv);
		
		final Div pincodeDiv = new Div();
		pincodeDiv.setParent(storeAdressDiv);
		
		Label re_accNo = new Label(LabelUtils.getLabel(widget, "pincode"));
		re_accNo.setParent(pincodeDiv);
		final Longbox pincodeLongbox = createLongBox(pincodeDiv);
		pincodeLongbox.setMaxlength(6);
		pincodeLongbox.setParent(pincodeDiv);
		TypedObject order = widget.getWidgetController().getCurrentOrder();
		OrderModel orderModel = (OrderModel) order.getObject();
		String pinCode = null;
		if(null != orderModel && null != orderModel.getDeliveryAddress() && null != orderModel.getDeliveryAddress().getPostalcode()) {
			
			 pinCode= orderModel.getDeliveryAddress().getPostalcode();
			pincodeLongbox.setValue(Long.valueOf(pinCode));
		}
		else if(CollectionUtils.isNotEmpty(entries) && entries.get(0).getDeliveryPointOfService() != null && entries.get(0).getDeliveryPointOfService().getAddress() != null) {
			pinCode= entries.get(0).getDeliveryPointOfService().getAddress().getPostalcode();
			pincodeLongbox.setValue(Long.valueOf(pinCode));
		}
		
		final Div listOfReturnItemsDiv = new Div();
		listOfReturnItemsDiv.setParent(pincodeDiv);
		
		
		if(null != pinCode) {
			String sellerId =StringUtils.substring(entries.get(0).getSelectedUSSID(), 0, 6);
			
			List<PointOfServiceData> returnableStores =  ((MarketPlaceReturnsController) widget
					.getWidgetController()).getAllReturnableStores(pinCode,sellerId);
			if(null !=returnableStores && !returnableStores.isEmpty()){
				if(null != listOfReturnItemsDiv && null !=listOfReturnItemsDiv.getChildren()) {
					listOfReturnItemsDiv.getChildren().clear();
				}
				Div div = new Div();
			    div.setParent(listOfReturnItemsDiv);
			    final Label label = new Label(LabelUtils.getLabel(widget,
			      "selectingMultipleStores"));
			    label.setClass("storeSelectionmsg");
			    label.setParent(div);
				renderStoreDetails(widget,listOfReturnItemsDiv,returnableStores,entries,storesMap);
			}else {
				pincodeDiv.setTooltiptext("No stores Available");
				if(null !=storesMap) {
					storesMap.clear();
				}
			}
		}
		
		 pincodeLongbox.addEventListener(Events.ON_BLUR, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				Long pincodeValue = pincodeLongbox.getValue();
				String pinCode= pincodeValue.toString();
				if(pinCode.matches(PIN_REGEX)) {
					String sellerId =StringUtils.substring(entries.get(0).getSelectedUSSID(), 0, 6);
					
					List<PointOfServiceData> returnableStores =  ((MarketPlaceReturnsController) widget
							.getWidgetController()).getAllReturnableStores(pinCode,sellerId);
					if(null !=returnableStores && !returnableStores.isEmpty()){
						if(null != listOfReturnItemsDiv && null !=listOfReturnItemsDiv.getChildren()) {
							listOfReturnItemsDiv.getChildren().clear();
						}
						Div div = new Div();
					    div.setParent(listOfReturnItemsDiv);
					    final Label label = new Label(LabelUtils.getLabel(widget,
					      "selectingMultipleStores"));
					    label.setClass("storeSelectionmsg");
					    label.setParent(div);
						renderStoreDetails(widget,listOfReturnItemsDiv,returnableStores,entries,storesMap);
					}else {
						if(null != listOfReturnItemsDiv && null !=listOfReturnItemsDiv.getChildren()) {
							listOfReturnItemsDiv.getChildren().clear();
						}
						if(null !=storesMap) {
							storesMap.clear();
						}
						Messagebox.show(NO_STORE_AVAILABLE);
						return;
					}
				}else {
					pincodeLongbox.setFocus(true);
					if(null != listOfReturnItemsDiv && null !=listOfReturnItemsDiv.getChildren()) {
						listOfReturnItemsDiv.getChildren().clear();
					}
					if(null !=storesMap) {
						storesMap.clear();
					}
					popupMessage(widget,
							MarketplaceCockpitsConstants.PIN_CODE_INVALID);
				}
			}
		});
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
					if(null != storesMap && !storesMap.isEmpty()) {
						LOG.debug("return Store Map  : "+storesMap.entrySet());
						Long pincodeValue = pincodeLongbox.getValue();
						String pinCode= pincodeValue.toString();
						Session session = Executions.getCurrent().getDesktop().getSession();
						session.setAttribute("pinCode", pinCode);
						retrunInfoCallToOMS(widget, entries, storesMap);
						proceedToReturnItem(widget,TypeofReturn.QUICK_DROP);
					}else {
						Messagebox.show(NO_STORE_SELECTED);
					}
					
			}
		});
		
		return storeAdressDiv;
	}
	protected void renderStoreDetails(final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget, Div listOfReturnItems, List<PointOfServiceData> returnableStores, List<AbstractOrderEntryModel> entries, final Map<String, List<String>> storesMap) {
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
		for (final AbstractOrderEntryModel e : entries) {
			final Listitem listItem = new Listitem();
			listItem.setParent(listbox);
			// Transaction Id 
			final Listcell cellTransId = new Listcell(e.getTransactionID());
			cellTransId.setParent(listItem);
			
			// Product 
			final Listcell cellProdDesc = new Listcell(e.getProduct().getName());
			cellProdDesc.setParent(listItem);
			
			// Stores 
			final Listcell storesListCell = new Listcell();
			storesListCell.setParent(listItem);

			Div storeLiDiv = new Div();
			storeLiDiv.setParent(storesListCell);
			final Listbox storesListBox = new Listbox();
			storesListBox.setParent(storeLiDiv);
			storesListBox.setMultiple(true);
			storesListBox.setMold("select");
			
			for(final PointOfServiceData store : returnableStores) {
				
				final String storeLabel = new String(store.getSlaveId()+ "," + store.getDisplayName() + '\n' +store.getAddress().getFormattedAddress()
						+ TIME + store.getMplOpeningTime() + " - " + store.getMplClosingTime());
				final Listitem item = new Listitem();
				item.setLabel(storeLabel);
				item.setParent(storesListBox);
				item.setAttribute(store.getSlaveId(), e.getTransactionID());
				item.setValue(store.getSlaveId());
				storesListBox.addItemToSelection(item);
			}
			storesListBox.setSelectedIndex(0);
			List<String> stores = new ArrayList<String>();
			if(null != storesListBox.getSelectedItem() && null != storesListBox.getSelectedItem().getValue()) {
				stores.add(storesListBox.getSelectedItem().getValue().toString());
			}
			if(null != stores && !stores.isEmpty()) {
				storesMap.put(e.getTransactionID(), stores);
			}
			
			storesListBox.addEventListener(Events.ON_SELECT, new EventListener() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					addStoresToReturnInfoList(widget,storesListBox,e,storesMap);
				}
			});
			final Br storeBr = new Br();
			storeBr.setParent(listOfReturnItems);
		}
	}

	protected void addStoresToReturnInfoList(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			Listbox storesListBox, AbstractOrderEntryModel entry,
			Map<String, List<String>> storesMap1) {
	     Set<Listitem> items = storesListBox.getSelectedItems();
	     List<String> stores= new ArrayList<String>();
	     if(null !=storesMap1 && !storesMap1.isEmpty() && storesMap1.containsKey(entry.getTransactionID())) {
	    	 storesMap1.get(entry.getTransactionID()).clear();
	     }
	     for (Listitem item : items) {
	    	 	stores.add(item.getValue().toString());
	     }
	     storesMap1.put(entry.getTransactionID(), stores);	   
	     LOG.debug(storesMap1);
		}

	private void popupMessage(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final String message) {
		try {
			Messagebox.show(
					LabelUtils.getLabel(widget, message, new Object[0]), INFO,
					Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private Div getSchedulePicupDetails(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> entries, final OrderModel subOrder) throws InterruptedException {
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
		headerTransId.setWidth("23px");
		headerTransId.setParent(listhead);
		final Listheader headerProdName = new Listheader("Product Name");
		headerProdName.setParent(listhead);
		headerProdName.setWidth("260px");
		final Listheader headerPrice = new Listheader("Total Price");
		headerPrice.setParent(listhead);
		headerPrice.setWidth("80px");
		final Listheader headerReturnDate = new Listheader(
				"Select Pick-up Date");
		headerReturnDate.setWidth("280px");
		headerReturnDate.setParent(listhead);
		final Listheader pickupDateAndTime = new Listheader(
				"Select Pick-up Time");
		pickupDateAndTime.setWidth("390px");
		pickupDateAndTime.setParent(listhead);
		final List<RescheduleData> scheduleDeliveryDates = new ArrayList<RescheduleData>();
		List<String> timeSlots = ((MarketPlaceReturnsController) widget
				.getWidgetController()).getReturnTimeSlotsByKey("RD");
		for (final AbstractOrderEntryModel entry : entries) {
			Listitem listItem = new Listitem();
			listItem.setParent(scheduleList);
			Listcell transIdCell = new Listcell(entry.getTransactionID());
			transIdCell.setParent(listItem);
			Listcell productCell = new Listcell(entry.getProduct().getName());
			productCell.setParent(listItem);
			Listcell costCell = new Listcell(entry.getNetAmountAfterAllDisc().toString());
			costCell.setParent(listItem);		
			List<String> returnDates = ((MarketPlaceReturnsController) widget
					.getWidgetController()).getReturnScheduleDates(entry);

			Listcell pickupDateCell = new Listcell();
			pickupDateCell.setParent(listItem);
			final Div pickupDateDiv = new Div();
			pickupDateDiv.setParent(pickupDateCell);
			final Radiogroup dateGroup = new Radiogroup();
			dateGroup.setOrient("vertical");
			dateGroup.setClass("dateSelection");
			dateGroup.setParent(pickupDateDiv);
			if(null != returnDates && !returnDates.isEmpty()){
				for (String date : returnDates) {
					final Div dateDiv = new Div();
					dateDiv.setParent(pickupDateDiv);
					final Radio dateRadio = new Radio();
					dateRadio.setParent(dateDiv);
					dateRadio.setLabel(date);
					dateGroup.appendChild(dateRadio);
				}
				dateGroup.setSelectedIndex(0);
			}
			
			Listcell pickupTimeCell = new Listcell();
			pickupTimeCell.setParent(listItem);
			final Div pickupTimeDiv = new Div();
			pickupTimeDiv.setParent(pickupTimeCell);
			final Radiogroup timeGruop = new Radiogroup();
			timeGruop.setOrient("vertical");
			timeGruop.setParent(pickupTimeDiv);
			if(null != timeSlots && !timeSlots.isEmpty()) {
				for (String time : timeSlots) {
					final Div timeDiv = new Div();
					timeDiv.setParent(pickupTimeDiv);
					final Radio rio = new Radio();
					rio.setParent(timeDiv);
					rio.setLabel(time);
					timeGruop.appendChild(rio);
				}			
				timeGruop.setSelectedIndex(0);
			}
						
			RescheduleData scheduleData = new RescheduleData();	
			if(null != dateGroup && null != dateGroup.getSelectedItem() && null != dateGroup.getSelectedItem().getLabel()) {
				scheduleData.setDate(dateGroup.getSelectedItem().getLabel());
			}
			if(null != dateGroup && null != dateGroup.getSelectedItem() && null != dateGroup.getSelectedItem().getLabel()) {
				scheduleData.setTime(timeGruop.getSelectedItem().getLabel());
			}
			if(null != dateGroup && null != dateGroup.getSelectedItem() && null != dateGroup.getSelectedItem().getLabel()) {
				scheduleData.setProductCode(entry.getProduct().getCode());
			}
			scheduleDeliveryDates.add(scheduleData);
			dateGroup.addEventListener(Events.ON_CHECK, new EventListener() {
				@Override
				public void onEvent(final Event event) throws InterruptedException,
				ParseException, InvalidKeyException,
				NoSuchAlgorithmException {
					createDateChangeEventListener(widget,entry,dateGroup,scheduleDeliveryDates);
				}
			});	
			timeGruop.addEventListener(Events.ON_CHECK, new EventListener() {
				@Override
				public void onEvent(final Event event) throws InterruptedException,
				ParseException, InvalidKeyException,
				NoSuchAlgorithmException {
					createTimeChangeEventListener(widget,entry,timeGruop,scheduleDeliveryDates);
				}
			});	
		}	
	
		Label selectAddressLabel  = new Label("selectAddress");
		selectAddressLabel.setClass("selectaddress");
		selectAddressLabel.setParent(scheduleDiv);
		final Listbox deliveryAddressList = new Listbox();
		deliveryAddressList.setParent(scheduleDiv);
		deliveryAddressList.setMold("select");
	
		final TypedObject order = (TypedObject) ((ReturnsController) widget
				.getWidgetController()).getRefundOrderPreview();
		OrderModel orderModel = (OrderModel) order.getObject();
		final Collection<AddressModel> addresses = customerAccountService
				.getAddressBookDeliveryEntries((CustomerModel)orderModel.getUser());
		List<AddressModel> addrssesList= new ArrayList<AddressModel>(addresses);
		LOG.info("AddressData size :"+addrssesList.size());
		
		Listitem item = new Listitem(" ");
		item.setParent(deliveryAddressList);
		 AddressModel deliveryAddress = subOrder.getDeliveryAddress();
	       TypedObject currentAddressObject = getCockpitTypeService().wrapItem(deliveryAddress);

		for (AddressModel add : addrssesList) {
			try {
				TypedObject address = cockpitTypeService.wrapItem(add);
				String text = TypeTools.getValueAsString(getLabelService(),
						address);
				item = new Listitem(text, address);
				item.setParent(deliveryAddressList);
				address.equals(currentAddressObject);
				item.setSelected(address.getObject().equals(currentAddressObject.getObject()));
			} catch (Exception e1) {
				e1.getStackTrace();
			}
		}
		deliveryAddressList.setSelectedIndex(0);
		final Br br7 = new Br();
		br7.setParent(scheduleDiv);
		
		final Div addAddrDiv = new Div();
		addAddrDiv.setParent(scheduleDiv);
		addAddrDiv.setClass(BUTTON);
		final Button addAddrButton = new Button(LabelUtils.getLabel(widget,
				ADD));
		addAddrButton.setParent(addAddrDiv);
		addAddrButton.setClass("returnAddresBtn");
		//addAddrButton.setClass(BUTTON);
		addAddrButton.addEventListener(Events.ON_CLICK, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				
				createReturnAddAddressPopupWindow(widget, getPopupWidgetHelper().getCurrentPopup().getParent()); 
			}
			
		});
		
		final Br br8 = new Br();
		br8.setParent(scheduleDiv);
		final Div buttonDiv = new Div();
		buttonDiv.setParent(scheduleDiv);
		buttonDiv.setClass(BUTTON);
		final Button continueButton = new Button(LabelUtils.getLabel(widget,
				CONTINUE));
		continueButton.setParent(buttonDiv);
		continueButton.setClass("creditButton");
		continueButton.setClass(BUTTON);
		continueButton.addEventListener(Events.ON_CLICK,
				createReturnRequestCreateEventListener(widget,deliveryAddressList,scheduleDeliveryDates));	
	
		deliveryAddressList.addEventListener(Events.ON_SELECT, new EventListener() {
			@Override
			public void onEvent(final Event event) throws InterruptedException,
			ParseException, InvalidKeyException,
			NoSuchAlgorithmException {
				createselectDeliveryAddressCreateEventListener(widget,deliveryAddressList);
			}
		});
		return scheduleDiv;
	}
	
	private void createselectDeliveryAddressCreateEventListener(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			Listbox deliveryAddressList) throws InterruptedException {
		if(null !=deliveryAddressList.getSelectedItem() && null != deliveryAddressList.getSelectedItem().getValue()) {
			Object selectedItem=deliveryAddressList.getSelectedItem().getValue();
			TypedObject address= ( (TypedObject) selectedItem);
			final AddressModel returnAddress =  TypeUtils.unwrapItem(address, AddressModel.class);
			String pinCode = returnAddress.getPostalcode();
			List<ReturnLogistics> returnLogisticsList = new ArrayList();
			final TypedObject order = (TypedObject) ((ReturnsController) widget
					.getWidgetController()).getRefundOrderPreview();
			OrderModel orderModel = (OrderModel) order.getObject();
			for (AbstractOrderEntryModel orderEntry : orderModel.getEntries()) {
				if(orderEntry.getQuantity() == 0) {
					ReturnLogistics returnLogistics = new ReturnLogistics();
					returnLogistics.setOrderId(orderModel.getParentReference().getCode());
					returnLogistics.setTransactionId(orderEntry.getTransactionID());
					returnLogistics.setPinCode(pinCode);
					String fullfillmentType=((MarketPlaceReturnsController) widget
							.getWidgetController()).getReturnFulFillmenttype(orderEntry.getProduct());
					String returnFulfillModeByP1=((MarketPlaceReturnsController) widget
							.getWidgetController()).getReturnFulfillModeByP1(orderEntry.getProduct());
					returnLogistics.setReturnFulfillmentType(fullfillmentType);
					returnLogistics.setReturnFulfillmentByP1(returnFulfillModeByP1);
					returnLogisticsList.add(returnLogistics);
				}
			}
			Map<Boolean, List<OrderLineDataResponse>> responseMap =((MarketPlaceReturnsController) widget.getWidgetController())
					.validateReverseLogistics(returnLogisticsList);
			if (MapUtils.isNotEmpty(responseMap)) {
				 if (CollectionUtils.isEmpty(responseMap.get(Boolean.TRUE))) {
					Messagebox.show(LabelUtils.getLabel(widget,
							REVERSE_LOGISTICS_NOTAVAILABLE, new Object[0]), "Error",
							Messagebox.OK, Messagebox.ERROR,
							new org.zkoss.zk.ui.event.EventListener() {
								public void onEvent(Event e)
										throws InterruptedException {
									if (e.getName().equals("onOK")) {
										return;
									}
								}
							});
				} else if(CollectionUtils.isNotEmpty(responseMap.get(Boolean.TRUE)) && CollectionUtils.isNotEmpty(responseMap.get(Boolean.FALSE))){
					String notAvailabletransactionList = null;
					if (CollectionUtils.isNotEmpty(responseMap.get(Boolean.FALSE))) {
						for (OrderLineDataResponse orderEntry : responseMap
								.get(Boolean.FALSE)) {
							if (StringUtils.isNotEmpty(notAvailabletransactionList))
								notAvailabletransactionList = notAvailabletransactionList
										+ System.getProperty("line.separator")
										+ orderEntry.getTransactionId();
							else
								notAvailabletransactionList = orderEntry
										.getTransactionId();
						}
					}
					String availabletransactionList = null;
					if (CollectionUtils.isNotEmpty(responseMap.get(Boolean.TRUE))) {
						for (OrderLineDataResponse orderEntry : responseMap
								.get(Boolean.TRUE)) {
							if (StringUtils.isNotEmpty(availabletransactionList))
								availabletransactionList = availabletransactionList
										+ System.getProperty("line.separator")
										+ orderEntry.getTransactionId();
							else
								availabletransactionList = orderEntry
										.getTransactionId();
						}
					}
					String finalMessage = LabelUtils.getLabel(widget,
							REVERSE_LOGISTICS_PARTIALAVAILABLE);
					if (null != availabletransactionList)
						finalMessage = finalMessage
								+ System.getProperty("line.separator")
								+ "The Transaction Id's for which Reverse Logisitic is available: "
								+ System.getProperty("line.separator")
								+ availabletransactionList;
					if (null != notAvailabletransactionList)
						finalMessage = finalMessage
								+ System.getProperty("line.separator")
								+ "The Transaction Id's for which Reverse Logisitic is not available: "
								+ System.getProperty("line.separator")
								+ notAvailabletransactionList;

					Messagebox.show(finalMessage, "Error", Messagebox.OK,
							Messagebox.ERROR,
							new org.zkoss.zk.ui.event.EventListener() {
								public void onEvent(Event e)
										throws InterruptedException {
									if (e.getName().equals("onOK")) {
										return;
									}
								}
							});
				}
			} else {
				Messagebox.show(LabelUtils.getLabel(widget,
						NO_RESPONSE_FROM_SERVER, new Object[0]), "Error",
						Messagebox.OK, Messagebox.ERROR,
						new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event e)
									throws InterruptedException {
								if (e.getName().equals("onOK")) {
									return;
								}
							}
						});
			}
			return;
		}
	}

	protected void createDateChangeEventListener(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			AbstractOrderEntryModel entry, Radiogroup dateGroup, List<RescheduleData> scheduleDeliveryDatesList) {
		if (null != scheduleDeliveryDatesList) {
			for (RescheduleData scheduleData : scheduleDeliveryDatesList) {
				if (scheduleData.getProductCode().equalsIgnoreCase(
						entry.getProduct().getCode())) {
					scheduleData
							.setDate(dateGroup.getSelectedItem().getLabel());
					return;
				}
			}
		}
	} 
		
	protected void createTimeChangeEventListener(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			AbstractOrderEntryModel entry,
			Radiogroup timeGruop, List<RescheduleData> scheduleDeliveryDates) {
		if (null != scheduleDeliveryDates) {
			for (RescheduleData scheduleData : scheduleDeliveryDates) {
				if (scheduleData.getProductCode().equalsIgnoreCase(
						entry.getProduct().getCode())) {
					scheduleData
							.setTime(timeGruop.getSelectedItem().getLabel());
					return;
				}
			}
		}
	}

	protected EventListener createReturnRequestCreateEventListener(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget, Listbox deliveryAddressList, List<RescheduleData> scheduleDeliveryDates) {
		return new ReturnRequestCreateEventListener(widget,deliveryAddressList,scheduleDeliveryDates);
	}

	protected class ReturnRequestCreateEventListener implements EventListener {
		private final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget;
		private final Listbox deliveryAddressList;
		private final List<RescheduleData> scheduleDeliveryDates;
		public ReturnRequestCreateEventListener(
				InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget, Listbox deliveryAddressList,List<RescheduleData> scheduleDeliveryDates) {
			this.widget = widget;
			this.deliveryAddressList=deliveryAddressList;
			this.scheduleDeliveryDates =scheduleDeliveryDates;		
		}

		public void onEvent(Event event) throws Exception {
		//	System.out.println("Inside Event"); // SISA, CAR -339
			if(null != deliveryAddressList.getSelectedItem() && null != deliveryAddressList.getSelectedItem().getValue()) {
				Object selectedItem=deliveryAddressList.getSelectedItem().getValue();
				TypedObject address= ( (TypedObject) selectedItem);
				final AddressModel returnAddress =  TypeUtils.unwrapItem(address, AddressModel.class);
//		SISA, CAR -339		if(null != returnAddress) {
//					System.out.println(returnAddress.getAddressLine3()+""+returnAddress.getCity()+""+returnAddress.getFirstname()+""+returnAddress.getLastname()+""+returnAddress.getLandmark()+""+returnAddress.getPhone1());
//				}
				String pinCode = null;		
				if(null !=returnAddress) {
					pinCode = returnAddress.getPostalcode();
				}
				List<ReturnLogistics> returnLogisticsList = new ArrayList();
								final TypedObject order = (TypedObject) ((ReturnsController) widget
						.getWidgetController()).getRefundOrderPreview();
				OrderModel orderModel = (OrderModel) order.getObject();
				for (AbstractOrderEntryModel orderEntry : orderModel.getEntries()) {
					ReturnLogistics returnLogistics = new ReturnLogistics();
					returnLogistics.setOrderId(orderModel.getParentReference().getCode());
					returnLogistics.setTransactionId(orderEntry.getTransactionID());
					returnLogistics.setPinCode(pinCode);
					String fullfillmentType=((MarketPlaceReturnsController) widget
							.getWidgetController()).getReturnFulFillmenttype(orderEntry.getProduct());
					String returnFulfillModeByP1=((MarketPlaceReturnsController) widget
							.getWidgetController()).getReturnFulfillModeByP1(orderEntry.getProduct());
					returnLogistics.setReturnFulfillmentType(fullfillmentType);
					returnLogistics.setReturnFulfillmentByP1(returnFulfillModeByP1);
					returnLogisticsList.add(returnLogistics);
				}
				final Map<Boolean, List<OrderLineDataResponse>> responseMap = ((MarketPlaceReturnsController) widget
						.getWidgetController())
						.validateReverseLogistics(returnLogisticsList);
				if (MapUtils.isNotEmpty(responseMap)) {
					if (CollectionUtils.isEmpty(responseMap.get(Boolean.FALSE))) {
						Messagebox.show(LabelUtils.getLabel(widget,
								REVERSE_LOGISTICS_AVAILABLE, new Object[0]),
								"Question", Messagebox.OK | Messagebox.CANCEL,
								Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event e)
									throws InterruptedException {
								if (e.getName().equals("onOK")) {
									proceedToReturn(returnAddress, scheduleDeliveryDates);
								} else {
									return;
								}
							}
						});
					}  if (CollectionUtils.isEmpty(responseMap.get(Boolean.TRUE))) {
						Messagebox.show(LabelUtils.getLabel(widget,
								REVERSE_LOGISTICS_NOTAVAILABLE, new Object[0]), "Error",
								Messagebox.OK, Messagebox.ERROR,
								new org.zkoss.zk.ui.event.EventListener() {
									public void onEvent(Event e)
											throws InterruptedException {
										if (e.getName().equals("onOK")) {
											return;
										}
									}
								});
					} 
				} else {
					Messagebox.show(LabelUtils.getLabel(widget,
							NO_RESPONSE_FROM_SERVER, new Object[0]), "Error",
							Messagebox.OK, Messagebox.ERROR,
							new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event e)
								throws InterruptedException {
							if (e.getName().equals("onOK")) {
								return;
							}
						}
					});
				}
			} else {
				Messagebox.show(LabelUtils.getLabel(widget,
						SELECT_RETURN_ADDRESS, new Object[0]), "Error",
						Messagebox.OK, Messagebox.ERROR,
						new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event e)
							throws InterruptedException {
						if (e.getName().equals("onOK")) {
							return;
						}
					}
				});
			
			}
	}

		protected void proceedToReturn(AddressModel returnAddress,List<RescheduleData> scheduleDeliveryDates) {		
			AddressData returnAddressData = new AddressData();
			addressPopulator.populate(returnAddress, returnAddressData);
			Session session = Executions.getCurrent().getDesktop().getSession();
			session.setAttribute("returnAddress",returnAddressData);
			session.setAttribute("scheduleDeliveryDates", scheduleDeliveryDates);
			createRefundConfirmationPopupWindow(widget, getPopupWidgetHelper()
					.getCurrentPopup().getParent());
			proceedToReturnItem(widget,TypeofReturn.SCHEDULE_PICKUP);
		}	
	}

	private void retrunInfoCallToOMS(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<AbstractOrderEntryModel> returnEntry,Map<String, List<String>>storeMap ) {
		for (AbstractOrderEntryModel entry : returnEntry) {
			((MarketPlaceReturnsController) widget.getWidgetController())
					.retrunInfoCallToOMSFromCsCockpit(widget, entry, storeMap);
		}
	}

	private void saveCODReturnsBankDetails(   
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			List<AbstractOrderEntryModel> returnEntry, final Textbox accNoTextbox,final Textbox reEnteAaccNoTextbox,
			Textbox accHolderNameTextbox, Listbox refundModeListbox,
			Listbox titleListBox, Textbox bankNameTextbox, Textbox ifscTextbox) throws InterruptedException {
		try {
			OrderModel order = (OrderModel) returnEntry.get(0).getOrder();
		accNoTextbox.setDisabled(true);
		reEnteAaccNoTextbox.setDisabled(true);
		accHolderNameTextbox.setDisabled(true);
		bankNameTextbox.setDisabled(true);
		ifscTextbox.setDisabled(true);
		refundModeListbox.setDisabled(true);
		titleListBox.setDisabled(true);
		CustomerModel customerModel = null;
		if(null != order.getUser()) {
			customerModel = (CustomerModel) order.getUser(); 
		}
			CODSelfShipData codData = new CODSelfShipData();
			if(null != customerModel) {
				codData.setCustomerNumber(customerModel.getUid());
			}
			codData.setName(accHolderNameTextbox.getValue().trim());
			codData.setTitle((String) titleListBox.getSelectedItem().getLabel());
			codData.setBankAccount(accNoTextbox.getValue().trim());
			codData.setBankKey(ifscTextbox.getValue().trim());
			codData.setBankName(bankNameTextbox.getValue().trim());
			String paymentMode = (String) refundModeListbox.getSelectedItem()
					.getLabel();
			if(paymentMode.equalsIgnoreCase(RefundMode.NEFT.getCode())) {
				codData.setPaymentMode(MarketplaceCockpitsConstants.RETURN_REFUND_MODE_N);
			}
			codData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_POSTPAID);
		  	((MarketPlaceReturnsController) widget.getWidgetController())
			.saveCODReturnsBankDetails(codData);
	
	}catch(Exception e) {
		LOG.error("Exception occurred while saving bank Details"+e.getMessage());
	}
	}
	
	
	private Window createReturnAddAddressPopupWindow(final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> parentWidget,Component parentWindow) throws InterruptedException {
		@SuppressWarnings("rawtypes")
		WidgetContainer widgetContainer = new DefaultWidgetContainer(
				new DefaultWidgetFactory());
		@SuppressWarnings("unchecked")
		Widget<DefaultListWidgetModel<TypedObject>, ReturnsController> popupWidget = createPopupWidget(widgetContainer,
				"csReturnAddAddressWidgetConfig",
				"csReturnAddAddressWidgetConfig-Popup");
		Window popup = new Window();
		popup.appendChild(popupWidget);	
		popup.setTitle(LabelUtils.getLabel(popupWidget,
				"popup.returnCreditTitle", new Object[0]));
		popup.setParent(parentWindow);
		popup.doHighlighted();
		popup.setClosable(true);
		popup.setWidth("600px");	
		return popup;
	}
}
