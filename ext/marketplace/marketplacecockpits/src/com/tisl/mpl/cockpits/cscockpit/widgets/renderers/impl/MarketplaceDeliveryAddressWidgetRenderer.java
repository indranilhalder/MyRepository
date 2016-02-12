package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.daos.AccountAddressDao;
import com.tisl.mpl.model.StateModel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CustomerController;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AddressCreateWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;


public class MarketplaceDeliveryAddressWidgetRenderer extends AddressCreateWidgetRenderer
{

	protected static final String CSS_CREATE_ADDRESS_ACTIONS = "csCreateAddressActions";
	protected static final String FAILED_ADDRESS_LOOKUP = "failedAddressLookup";
	protected static final String FAILED_VALIDATION = "failedValidation";

	@Autowired
	private PopupWidgetHelper popupWidgetHelper;

	@Autowired
	private ModelService modelService;

	@Autowired
	private AccountAddressDao accountAddressDao;

	@Autowired
	private DefaultCommonI18NService commonI18NService;

	@Autowired
	private EnumerationService enumService;
	
	@Autowired
	private AccountAddressFacade accountAddressFacade;

	@Override
	protected HtmlBasedComponent createContentInternal(
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
			final HtmlBasedComponent rootContainer) {

		final Div content = new Div();

		final Div customerAddressContent = new Div();
		customerAddressContent.setParent(content);

		// Create First Name field
		final Br br = new Br();
		br.setParent(customerAddressContent);
		final Div firstNameDiv = new Div();
		firstNameDiv.setParent(customerAddressContent);
		firstNameDiv.setSclass("createNewAddress");
		final Label firstNamelabel = new Label(LabelUtils.getLabel(widget,
				"firstName"));
		firstNamelabel.setParent(firstNameDiv);
		final Textbox firstNameField = createTextbox(firstNameDiv);
		firstNameField.setSclass("addressForFN");
		firstNameField.setMaxlength(40);

		// Create Last Name field
		final Br br2 = new Br();
		br2.setParent(customerAddressContent);
		final Div lastNameDiv = new Div();
		lastNameDiv.setParent(customerAddressContent);
		lastNameDiv.setSclass("createNewAddress");
		final Label lastNamelabel = new Label(LabelUtils.getLabel(widget,
				"lastName"));
		lastNamelabel.setParent(lastNameDiv);
		final Textbox lastNameField = createTextbox(lastNameDiv);
		lastNameField.setSclass("addressForLN");
		lastNameField.setMaxlength(40);

		// Creates Address Line1 field
		final Br br3 = new Br();
		br3.setParent(customerAddressContent);
		final Div addressDiv = new Div();
		addressDiv.setParent(customerAddressContent);
		addressDiv.setSclass("createNewAddress");
		final Label addresslabel = new Label(LabelUtils.getLabel(widget,
				"address"));
		addresslabel.setParent(addressDiv);
		final Textbox addressField = createTextbox(addressDiv);
		addressField.setSclass("address1ForAddressField");
		addressField.setMaxlength(30);

		// Creates Address Line2 field
		final Br br4 = new Br();
		br4.setParent(customerAddressContent);
		final Div address2Div = new Div();
		address2Div.setParent(customerAddressContent);
		address2Div.setSclass("createNewAddress");
		final Label address2label = new Label(LabelUtils.getLabel(widget,
				"address2"));
		address2label.setParent(address2Div);
		final Textbox address2Field = createTextbox(address2Div);
		address2Field.setSclass("address2ForAddressField");
		address2Field.setMaxlength(30);

		// Creates LandMark field
		final Br br5 = new Br();
		br5.setParent(customerAddressContent);
		final Div address3Div = new Div();
		address3Div.setParent(customerAddressContent);
		address3Div.setSclass("createNewAddress");
		final Label address3label = new Label(LabelUtils.getLabel(widget,
				"address3"));
		address3label.setParent(address3Div);
		final Textbox address3Field = createTextbox(address3Div);
		address3Field.setSclass("address3ForAddressField");
		address3Field.setMaxlength(30);

		// Creates City/District field
		final Br br6 = new Br();
		br6.setParent(customerAddressContent);
		final Div cityDiv = new Div();
		cityDiv.setParent(customerAddressContent);
		cityDiv.setSclass("createNewAddress");
		final Label citylabel = new Label(LabelUtils.getLabel(widget, "city"));
		citylabel.setParent(cityDiv);
		final Textbox cityField = createTextbox(cityDiv);
		cityField.setSclass("addressForCityField");
		cityField.setMaxlength(30);


		// Creates State List Box
		final Br br7 = new Br();
		br7.setParent(customerAddressContent);
		final Div stateDiv = new Div();
		stateDiv.setParent(customerAddressContent);
		stateDiv.setSclass("stateForAddressType");
		final Label statelabel = new Label(LabelUtils.getLabel(widget, "state"));
		statelabel.setParent(stateDiv);

		final Listbox stateFieldListBox = new Listbox();
		stateFieldListBox.setMultiple(false);
		stateFieldListBox.setMold("select");
		stateFieldListBox.setParent(stateDiv);

		final List<StateData> stateDataList = accountAddressFacade.getStates();

		stateFieldListBox.appendItem(LabelUtils.getLabel(widget, "defaultSelectOption"), StringUtils.EMPTY);
		for (final StateData value : stateDataList)
		{
			final Listitem stateListItem = new Listitem(value.getName());
			stateListItem.setParent(stateFieldListBox);
		}
		

		// Creates Postal Code field
		final Br br8 = new Br();
		br8.setParent(customerAddressContent);
		final Div postalCodeDiv = new Div();
		postalCodeDiv.setParent(customerAddressContent);
		postalCodeDiv.setSclass("createNewAddress");
		final Label postalCodelabel = new Label(LabelUtils.getLabel(widget,
				"postalCode"));
		postalCodelabel.setParent(postalCodeDiv);
		final Textbox postalCodeField = createTextbox(postalCodeDiv);
		postalCodeField.setMaxlength(6);
		postalCodeField.setSclass("addressForPostalCodeField");
		postalCodeField.setMaxlength(6);
		
		// Creates Mobile Number field
		final Br br9 = new Br();
		br9.setParent(customerAddressContent);
		final Div mobileNumberDiv = new Div();
		mobileNumberDiv.setParent(customerAddressContent);
		mobileNumberDiv.setSclass("createNewAddress");
		final Label mobileNumberlabel = new Label(LabelUtils.getLabel(widget,
				"mobileNumber"));
		mobileNumberlabel.setParent(mobileNumberDiv);
		final Textbox mobileNumberPrefixField = createPrefixTextbox(mobileNumberDiv);
		final Textbox mobileNumberField = createMobileNumberTextbox(mobileNumberDiv);
		mobileNumberField.setMaxlength(10);
		mobileNumberField.setSclass("addressForMobileNumberField");
		mobileNumberPrefixField.setSclass("addressForMobileNumberPrefixField");

		// Create List Box for Address type
		final Br br10 = new Br();
		br10.setParent(customerAddressContent);
		final Div addressTypeDiv = new Div();
		addressTypeDiv.setParent(customerAddressContent);
		addressTypeDiv.setSclass("addressForAddressType");
		final Label label = new Label(
				LabelUtils.getLabel(widget, "addressType"));
		label.setParent(addressTypeDiv);
		final Listbox listbox = new Listbox();
		listbox.setMultiple(false);
		listbox.setMold("select");
		final List<String> AddressRadioTypeList = getAddressRadioTypeList();
//		final List<AddressCSEnum> values = enumService.getEnumerationValues(AddressCSEnum.class);
		listbox.appendItem(LabelUtils.getLabel(widget, "defaultSelectOption"), StringUtils.EMPTY);
		for (final String value : AddressRadioTypeList)
		{
			final Listitem listItem = new Listitem(value);
			listItem.setParent(listbox);
		}
		listbox.setParent(addressTypeDiv);

		// Create Country List box
		final Br br11 = new Br();
		br11.setParent(customerAddressContent);
		final Div countryDiv = new Div();
		countryDiv.setParent(customerAddressContent);
		countryDiv.setSclass("addressForCountry");
		final List<CountryModel> countries = commonI18NService
				.getAllCountries();
		final Label countryLabel = new Label(LabelUtils.getLabel(widget,
				"country"));
		countryLabel.setParent(countryDiv);
		final Listbox countryListbox = new Listbox();
		countryListbox.setMultiple(false);
		countryListbox.setMold("select");
		countryListbox.setParent(countryDiv);
		for (final CountryModel country : countries) {
			final Listitem listItem = new Listitem(country.getName());
			listItem.setParent(countryListbox);
			if (country.getName().equalsIgnoreCase("INDIA")) {
				countryListbox.setSelectedItem(listItem);
			}
		}
		countryListbox.setDisabled(true);
		final Br br12 = new Br();
		br12.setParent(customerAddressContent);

		final Button createButton = new Button(LabelUtils.getLabel(widget,
				"createButton", new Object[0]));

		createButton.setParent(customerAddressContent);
		createButton.setSclass("addressForButton");

		createButton.addEventListener(
				"onClick",
				createCreateClickEventListener(widget, firstNameField, lastNameField, addressField, address2Field, address3Field,
						cityField, postalCodeField, stateFieldListBox, mobileNumberPrefixField, mobileNumberField, listbox, countryListbox));

		return content;
	}

	/**
	 * @return List<String>
	 */
	private List<String> getAddressRadioTypeList()
	{
		final List<String> list = new ArrayList<String>();
		list.add(MarketplacecommerceservicesConstants.LIST_VAL_RESIDENTIAL);
		list.add(MarketplacecommerceservicesConstants.LIST_VAL_COMMERCIAL);
		return list;
	}

	/**
	 * It creates a textbox for mobile number
	 * 
	 * @param parent
	 * @return
	 */
	private Textbox createMobileNumberTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("26%");
		textBox.setParent(parent);
		return textBox;
	}

	

	/**
	 * It creates a textbox for mobile number where +91 will be displayed
	 * 
	 * @param parent
	 * @return
	 */
	private Textbox createPrefixTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("3%");
		textBox.setParent(parent);
		textBox.setValue("+91");
		textBox.setDisabled(true);
		return textBox;
	}

	/**
	 * method to create a Textbox and assign it to the hbox parent
	 * 
	 * @param parent
	 * @return
	 */
	private Textbox createTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("30%");
		textBox.setParent(parent);
		return textBox;
	}

	/**
	 * To create the address
	 * 
	 * @param widget
	 * @param firstNameField
	 * @param middleNameField
	 * @param lastNameField
	 * @param addressField
	 * @param address2Field
	 * @param address3Field
	 * @param cityField
	 * @param postalCodeField
	 * @param stateFieldListBox
	 * @param mobileNumberPrefixField
	 * @param mobileNumberField
	 * @param listbox
	 * @param countryListbox
	 * @return
	 */
	private EventListener createCreateClickEventListener(
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
			final Textbox firstNameField, final Textbox lastNameField, final Textbox addressField, final Textbox address2Field,
			final Textbox address3Field, final Textbox cityField, final Textbox postalCodeField, final Listbox stateFieldListBox,
			final Textbox mobileNumberPrefixField, final Textbox mobileNumberField, final Listbox listbox,
			final Listbox countryListbox)
	{
		// TODO Auto-generated method stub
		return new MarketplaceCreateClickEventListener(widget, firstNameField, lastNameField, addressField, address2Field,
				address3Field, cityField, postalCodeField, stateFieldListBox, mobileNumberPrefixField, mobileNumberField, listbox,
				countryListbox);
	}

	protected class MarketplaceCreateClickEventListener implements
			EventListener {

		private final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget;
		private static final String PIN_REGEX = "^([0-9]{6})$";
		private static final String MOBILENUMBER_REGEX = "^[0-9]{10}";
		private final Listbox listbox;
		private final Listbox stateFieldListBox;
		private final Listbox countryListbox;
		private final Textbox firstNameField;
		private final Textbox lastNameField;
		private final Textbox addressField;
		private final Textbox address2Field;
		private final Textbox address3Field;
		private final Textbox cityField;
		private final Textbox postalCodeField;
		private final Textbox mobileNumberField;
		private Boolean valid = Boolean.TRUE;
		private final Textbox mobileNumberPrefixField;

		public MarketplaceCreateClickEventListener(
				final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
				final Textbox firstNameField, final Textbox lastNameField, final Textbox addressField, final Textbox address2Field,
				final Textbox address3Field, final Textbox cityField, final Textbox postalCodeField, final Listbox stateFieldListBox,
				final Textbox mobileNumberPrefixField, final Textbox mobileNumberField, final Listbox listbox,
				final Listbox countryListbox)
		{
			this.widget = widget;
			this.listbox = listbox;
			this.stateFieldListBox = stateFieldListBox;
			this.countryListbox = countryListbox;
			this.firstNameField = firstNameField;
			this.lastNameField = lastNameField;
			this.addressField = addressField;
			this.address2Field = address2Field;
			this.address3Field = address3Field;
			this.cityField = cityField;
			this.postalCodeField = postalCodeField;
			this.mobileNumberField = mobileNumberField;
			this.mobileNumberPrefixField = mobileNumberPrefixField;
		}

		@Override
		public void onEvent(final Event event) throws InterruptedException {
			// TODO Auto-generated method stub
			handleCreateClickEvent(widget, firstNameField, lastNameField, addressField, address2Field, address3Field, cityField,
					postalCodeField, stateFieldListBox, mobileNumberPrefixField, mobileNumberField, listbox, countryListbox);
		}

		private void handleCreateClickEvent(
				final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
				final Textbox firstNameField, final Textbox lastNameField, final Textbox addressField, final Textbox address2Field,
				final Textbox address3Field, final Textbox cityField, final Textbox postalCodeField, final Listbox stateFieldListBox,
				final Textbox mobileNumberPrefixField, final Textbox mobileNumberField, final Listbox listbox,
				final Listbox countryListbox) throws InterruptedException
		{
			// TODO Auto-generated method stub

			if (StringUtils.isBlank(firstNameField.getValue()) || StringUtils.isBlank(firstNameField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "firstNameValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (firstNameField.getValue().length() > 255)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidFirstNameLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				return;
			}
			
			else if (StringUtils.isBlank(lastNameField.getValue()) || StringUtils.isBlank(lastNameField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "lastNameValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (lastNameField.getValue().length() > 255)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidLastNameLength"), LabelUtils.getLabel(widget, FAILED_VALIDATION), 
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			else if (StringUtils.isBlank(addressField.getValue()) || StringUtils.isBlank(addressField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "addressLine1ValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (addressField.getValue().length() > 255)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidAddress1Length"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
					return;
			}

			else if (StringUtils.isBlank(address2Field.getValue()) || StringUtils.isBlank(address2Field.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "addressLine2ValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (address2Field.getValue().length() > 255)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidAddress2Length"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				return;
			}
			else if (StringUtils.isBlank(address3Field.getValue()) || StringUtils.isBlank(address3Field.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "landmarkValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (address3Field.getValue().length() > 255)
			{
					Messagebox.show(LabelUtils.getLabel(widget, "invalidLandMarkLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION), 
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			else if (StringUtils.isBlank(cityField.getValue()) || StringUtils.isBlank(cityField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "cityValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (cityField.getValue().length() > 255)
			{
					Messagebox.show(LabelUtils.getLabel(widget, "invalidCityLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			else if (stateFieldListBox.getSelectedItem() == null || stateFieldListBox.getSelectedItem().getLabel().equalsIgnoreCase("Select"))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "stateTypeValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (StringUtils.isBlank(postalCodeField.getValue()) || StringUtils.isBlank(postalCodeField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "postalCodeValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (postalCodeField.getValue().length() > 6)
			{
					Messagebox.show(LabelUtils.getLabel(widget, "invalidPinCodeLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION), 
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			else if (!(postalCodeField.getValue().matches(PIN_REGEX)))
			{
				//valid = true;
				Messagebox.show(LabelUtils.getLabel(widget, "postalCodeValueIncorrect"),LabelUtils.getLabel(widget, FAILED_VALIDATION), 
					   Messagebox.OK, Messagebox.ERROR);
				return;
			}
			else if (StringUtils.isBlank(mobileNumberField.getValue()) || StringUtils.isBlank(mobileNumberField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "mobileNoValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (mobileNumberField.getValue().length() > 10)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidMobileLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				return;
			}
			else if (!(mobileNumberField.getValue().matches(MOBILENUMBER_REGEX)))
			{
				//valid = true;
				Messagebox.show(LabelUtils.getLabel(widget, "mobileNumberValueIncorrect"),
						LabelUtils.getLabel(widget, FAILED_VALIDATION), Messagebox.OK, Messagebox.ERROR);
				return;
			}
			else if (listbox.getSelectedItem() == null || listbox.getSelectedItem().getLabel().equalsIgnoreCase("Select"))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "addressTypeValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (countryListbox.getSelectedItem() == null || countryListbox.getSelectedItem().getLabel().equalsIgnoreCase("Select"))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "countryValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else
			{
				valid = true;
			}
			
			if (valid)
			{
				
				// Saving the address
				saveShippingAddress(widget, firstNameField.getValue(), lastNameField.getValue(), addressField.getValue(),
						address2Field.getValue(), address3Field.getValue(), cityField.getValue(), postalCodeField.getValue(),
						stateFieldListBox.getSelectedItem().getLabel().toString(), mobileNumberField.getValue(), listbox.getSelectedItem().getLabel().toString(),
						countryListbox.getSelectedItem().getLabel().toString());

				// kill the popup
				popupWidgetHelper.dismissCurrentPopup();

				// fire a dispatch event to refresh the page/widget
				widget.getWidgetController().dispatchEvent(widget.getControllerCtx(), this, null);
			}

		}

		/**
		 * Method to save the address
		 * 
		 * @param widget2
		 * @param firstNameField
		 * @param middleNameField
		 * @param lastNameField
		 * @param addressField
		 * @param address2Field
		 * @param address3Field
		 * @param cityField
		 * @param postalCodeField
		 * @param stateField
		 * @param mobileNumberField
		 * @param addressType
		 * @param country
		 */
		public void saveShippingAddress(
				final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget2,
				final String firstNameField, final String lastNameField,
				final String addressField, final String address2Field,
				final String address3Field, final String cityField,
				final String postalCodeField, final String stateField,
				final String mobileNumberField, final String addressType,
				final String country) {

			final CustomerModel customerModel = (CustomerModel) widget
					.getWidgetController().getCurrentCustomer().getObject();

			// set the address to the model
			final AddressModel deliveryAddress = modelService
					.create(AddressModel.class);

			deliveryAddress.setFirstname(firstNameField);
			deliveryAddress.setLastname(lastNameField);
			deliveryAddress.setLine1(addressField);
			deliveryAddress.setLine2(address2Field);
			deliveryAddress.setAddressLine3(address3Field);
			deliveryAddress.setTown(cityField);
			deliveryAddress.setDistrict(stateField);
			deliveryAddress.setPostalcode(postalCodeField);
			deliveryAddress.setBillingAddress(false);
			deliveryAddress.setShippingAddress(true);
			deliveryAddress.setPhone1(mobileNumberField);
			deliveryAddress.setCellphone(mobileNumberField);
			deliveryAddress.setShippingAddress(Boolean.TRUE);//TODO
			deliveryAddress.setBillingAddress(Boolean.TRUE);//TODO
			deliveryAddress.setVisibleInAddressBook(Boolean.TRUE);//TODO
			if (listbox.getSelectedItem() != null) {
				if(MarketplacecommerceservicesConstants.LIST_VAL_RESIDENTIAL.equalsIgnoreCase(addressType)){
					deliveryAddress.setAddressType("Home");
				}
				else if(MarketplacecommerceservicesConstants.LIST_VAL_COMMERCIAL.equalsIgnoreCase(addressType)){
					deliveryAddress.setAddressType("Work");
				}
			}
			if (countryListbox.getSelectedItem() != null) {
				for (final CountryModel countryModel : commonI18NService
						.getAllCountries()) {
					if (countryListbox.getSelectedItem().getLabel()
							.equalsIgnoreCase(countryModel.getName())) {
						deliveryAddress.setCountry(countryModel);
					}
				}
			}
			deliveryAddress.setOwner(customerModel);
			modelService.save(deliveryAddress);

		}
	}

}
