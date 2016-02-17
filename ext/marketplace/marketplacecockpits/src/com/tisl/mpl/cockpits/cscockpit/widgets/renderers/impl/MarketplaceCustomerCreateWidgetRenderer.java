package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.Dates;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleDateConstraint;
import org.zkoss.zul.Textbox;

import com.tisl.mpl.core.enums.CommPref;
import com.tisl.mpl.core.model.CommunicationPreferencesModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CustomerCreateController;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.CustomerCreateWidgetRenderer;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

public class MarketplaceCustomerCreateWidgetRenderer extends
		CustomerCreateWidgetRenderer {

	private static final Logger LOG = Logger
			.getLogger(MarketplaceCustomerCreateWidgetRenderer.class);
	
	private static final String EMPTY_SPACE = " ";
	
	private static final String EMPTY = "";

	@Autowired
	private SessionService sessionService;

	@Autowired
	private EnumerationService enumService;
	
	@Autowired
	private ModelService modelService;

	/**
	 * It creates the contents that will be displayed in the renderer.
	 * @param widget
	 * @param rootContainer
	 * @return
	 */
	@Override
	protected HtmlBasedComponent createContentInternal(
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerCreateController> widget,
			final HtmlBasedComponent rootContainer) {

		final Div content = new Div();
		final ObjectValueContainer customerObjectValueContainer;

		try {
			if (StringUtils.isNotBlank(getEditorConfigurationCode())
					&& StringUtils.isNotBlank(getEditorConfigurationType())) {

				final Div customerEditorContent = new Div();
				customerEditorContent.setParent(content);

				// It adds Personal Details Group Header
				Div personalDetailsRowDiv = new Div();
				personalDetailsRowDiv.setSclass("editCustomerRow");
				personalDetailsRowDiv.setParent(content);
				Label customerGroup = new Label(LabelUtils.getLabel(widget,
						"personalDetails"));
				customerGroup.setStyle("font-weight:bold");
				customerEditorContent.appendChild(customerGroup);
				customerGroup.setClass("editCustomer");
				customerGroup.setParent(personalDetailsRowDiv);

				// It processes xml fields
				customerObjectValueContainer = loadAndCreateEditors(widget,
						customerEditorContent, widget.getWidgetController()
								.getAutoFilledPropertyQualifiers());

				// It sets a custom name for xml fields
				List<AbstractComponent> children = customerEditorContent
						.getChildren();
				setLabelsForCreateCustomerFields(children, widget);

				// It adds Email ID
				final Hbox emailHbox = createHbox(widget, "originalUID", false);
				Div emailDiv = new Div();
				emailDiv.setSclass("editCustomerForEmail");
				emailDiv.setParent(emailHbox);
				content.appendChild(emailHbox);

				final Textbox eMailField = createTextbox(emailDiv);
				eMailField.setSclass("editEmailTextbox");
				
				// It adds First Name
				final Hbox firstNameHbox = createHbox(widget, "firstName", false);
				Div firstNameDiv = new Div();
				firstNameDiv.setParent(firstNameHbox);
				firstNameDiv.setSclass("editCustomerForFirstName");
				content.appendChild(firstNameHbox);
				final Textbox firstNameField = createTextbox(firstNameDiv);
				firstNameField.setSclass("editfirstNameFieldTextbox");

				// It adds Last Name
				final Hbox lastNameHbox = createHbox(widget, "lastName", false);
				Div lastNameDiv = new Div();
				lastNameDiv.setParent(lastNameHbox);
				lastNameDiv.setSclass("editCustomerForLastName");
				content.appendChild(lastNameHbox);
				final Textbox lastNameField = createTextbox(lastNameDiv);
				lastNameField.setSclass("editlastNameFieldTextbox");
				
				// It adds Nick Name
				final Hbox nickNameHbox = createHbox(widget, "nickName", false);
				Div nickNameDiv = new Div();
				nickNameDiv.setParent(nickNameHbox);
				nickNameDiv.setSclass("editCustomerForNickName");
				content.appendChild(nickNameHbox);
				final Textbox nickNameField = createTextbox(nickNameDiv);
				nickNameField.setSclass("editnickNameFieldTextbox");

				// It adds Mobile Number
				final Hbox mobileNumberHbox = createHbox(widget, "mobileNumber",
						false);
				Div mobileNumberDiv = new Div();
				mobileNumberDiv.setParent(mobileNumberHbox);
				mobileNumberDiv.setSclass("editCustomerForMobileNum");
				content.appendChild(mobileNumberHbox);
				final Textbox mobileNumberField = createTextbox(mobileNumberDiv);
				mobileNumberField.setSclass("editmobileNumberFieldTextbox");

				final List<Checkbox> commPreferencesChkBoxes= null;
				// It adds Date Of Birth
				final Hbox dateOfBirthHbox = createHbox(widget, "dateOfBirth",
						false);
				Div dateOfBirthDiv = new Div();
				dateOfBirthDiv.setParent(dateOfBirthHbox);
				dateOfBirthDiv.setSclass("editCustomerForDob");
				content.appendChild(dateOfBirthHbox);
				final Datebox dobDateBox = createDatebox(dateOfBirthDiv,false);
				dobDateBox.setSclass("editdobDateBox");
			
				//It adds Date of marriage anniversary
				final Hbox domaHbox = createHbox(widget, "dateOfMarriageAnniversary",
						false);
				Div domaDiv = new Div();
				domaDiv.setParent(domaHbox);
				domaDiv.setSclass("editCustomerForDoma");
				content.appendChild(domaHbox);
				final Datebox domaDateBox = createDatebox(domaDiv,false);
				domaDateBox.setSclass("editdomaDateBox");

				// It adds Gender
				final Hbox genderHbox = createHbox(widget, "gender",
						false);
				Div genderDiv = new Div();
				genderDiv.setParent(genderHbox);
				genderDiv.setSclass("editCustomerForGender");
				content.appendChild(genderHbox);
				final Listbox genderListBox = createGenderListbox(widget,
						genderDiv);

				// It creates customer button
				final Div itemCreationContent = new Div();
				itemCreationContent.setParent(content);

				final Button createButton = new Button(LabelUtils.getLabel(
						widget, "createButton"));
				createButton.setParent(content);
				createButton.setSclass("editCustomer");

				createButton.addEventListener(
						Events.ON_CLICK,
						createNewCustomerEventListener(widget,
								customerObjectValueContainer, firstNameField, lastNameField,nickNameField,
								mobileNumberField, commPreferencesChkBoxes, dobDateBox,
								domaDateBox, genderListBox, eMailField));

			} else {
				LOG.warn("Can not load editor widget configuration. Reason: No configuration code specified.");
			}
		} catch (final ValueHandlerException e) {
			LOG.error("unable to render new customer creation widget", e);
		}
		return content;
	}


	/**
	 * It creates the listbox for gender.
	 * @param widget
	 * @param genderDiv
	 * @return
	 */
	private Listbox createGenderListbox(
			InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerCreateController> widget,
			Div genderDiv) {
		// TODO Auto-generated method stub
		return createGenderDropdownField(widget, genderDiv, "genderText", null);
	}

	/**
	 * It creates the dropdown for gender.
	 * @param widget
	 * @param parent
	 * @param labelKey
	 * @param cssClass
	 * @return
	 */
	private Listbox createGenderDropdownField(
			InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerCreateController> widget,
			final Div parent, final String labelKey, final String cssClass) {

		final Listbox listbox = new Listbox();
		parent.appendChild(listbox);
		listbox.setMultiple(false);
		listbox.setMold("select");

		List<Gender> values = enumService.getEnumerationValues(Gender.class);

		listbox.appendItem(LabelUtils.getLabel(widget, "defaultSelectOption"),
				StringUtils.EMPTY);
		for (Gender value : values) {
			Listitem listItem = new Listitem(value.getCode());

			listItem.setParent(listbox);
		}
		return listbox;
	}

	/**
	 * It creates horizontal box.
	 * @param widget
	 * @param attributeLabel
	 * @param hidden
	 * @return
	 */
	private Hbox createHbox(
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerCreateController> widget,
			String attributeLabel, boolean hidden) {

		Hbox hbox = new Hbox();
		hbox.setWidth("96%");
		hbox.setAlign("center");

		if (hidden) {
			hbox.setVisible(false);	
		}

		Label label = new Label(LabelUtils.getLabel(widget, attributeLabel));
		label.setParent(hbox);

		return hbox;
	}

	/**
	 * It creates the textbox
	 * @param parent
	 * @return
	 */
	private Textbox createTextbox(Div parent) {
		Textbox textBox = new Textbox();
		textBox.setParent(parent);
		return textBox;
	}
	
	/**
	 * It creates the datebox
	 * @param parent
	 * @return
	 */
	private Datebox createDatebox(Div parent,boolean  allowFetureDate) {
		Datebox dateBox = new Datebox();
		dateBox.setFormat("dd/MM/yyyy");
		if(!allowFetureDate){
			SimpleDateConstraint simpleDateConstraint = new SimpleDateConstraint(1,null,Dates.today(),"Can not be a future Date");
			dateBox.setConstraint(simpleDateConstraint);
		} 
		dateBox.setParent(parent);
		return dateBox;
	}

	/**
	 * Customising labels for the create customer widget.
	 */
	private void setLabelsForCreateCustomerFields(
			List children,
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerCreateController> widget) {
		for (int i = 1; i < children.size(); i++) {
			Label currentLabel = ((Label) ((Hbox) ((Div) children.get(i))
					.getChildren().get(0)).getChildren().get(0));
			currentLabel.setValue(LabelUtils.getLabel(widget, currentLabel
					.getValue().replace(EMPTY_SPACE, EMPTY)));
		}
	}

	/**
	 * It creates a new customer
	 * @param widget
	 * @param customerObjectValueContainer
	 * @param firstNameField
	 * @param lastNameField
	 * @param nickNameField
	 * @param mobileNumberField
	 * @param commPreferencesChkBoxes
	 * @param dobDateBox
	 * @param domaDateBox
	 * @param genderListBox
	 * @param eMailField
	 * @return
	 */
	protected EventListener createNewCustomerEventListener(
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerCreateController> widget,
			ObjectValueContainer customerObjectValueContainer,
			Textbox firstNameField, Textbox lastNameField,Textbox nickNameField, Textbox mobileNumberField,
			List<Checkbox> commPreferencesChkBoxes, Datebox dobDateBox, Datebox domaDateBox,
			Listbox genderListBox, Textbox eMailField) {

		return new CreateNewCustomerEventListener(widget,
				customerObjectValueContainer, firstNameField,
				lastNameField,nickNameField, mobileNumberField, commPreferencesChkBoxes, dobDateBox,
				domaDateBox, genderListBox, eMailField);
	}

	protected class CreateNewCustomerEventListener implements EventListener {
		private static final String FAILED_TO_VALIDATE_CUSTOMER_FORM = "failedToValidateCustomerForm";
		private static final String MOBILENUMBER_REGEX = "^[0-9]{10}";
		private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		private static final String INVALID_LENGTH = "invalidLength";
		private static final String EMAIL = "originalUid";
		private static final String FIRST_NAME = "firstName";
		private static final String LAST_NAME = "lastName";
		private static final String NICK_NAME = "nickName";
		private static final String MOBILE_NUMBER = "mobileNumber";
		private static final String DOB = "dateOfBirth";
		private static final String DOMA = "dateOfAnniversary";
		private static final String GENDER = "gender";
		private final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerCreateController> widget;
		private final ObjectValueContainer customerObjectValueContainer;
		private final Textbox firstNameField;
		private final Textbox lastNameField;
		private final Textbox nickNameField;
		private final Textbox mobileNumberField;
		private final List<Checkbox> commPreferencesChkBoxes;
		private Datebox dobDateBox;
		private Datebox domaDateBox;
		private final Listbox genderListBox;
		private final Textbox eMailField;

		public CreateNewCustomerEventListener(
				final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerCreateController> widget,
				ObjectValueContainer customerObjectValueContainer,
				Textbox firstNameField, Textbox lastNameField,Textbox nickNameField, Textbox mobileNumberField,
				List<Checkbox> commPreferencesChkBoxes, Datebox dobDateBox, Datebox domaDateBox,
				Listbox genderListBox, Textbox eMailField) {
			this.widget = widget;
			this.customerObjectValueContainer = customerObjectValueContainer;
			this.firstNameField = firstNameField;
			this.lastNameField = lastNameField;
			this.nickNameField = nickNameField;
			this.mobileNumberField = mobileNumberField;
			this.commPreferencesChkBoxes = commPreferencesChkBoxes;
			this.dobDateBox = dobDateBox;
			this.domaDateBox = domaDateBox;
			this.genderListBox = genderListBox;
			this.eMailField = eMailField;

		}

		@Override
		public void onEvent(final Event event) throws Exception {
			/*String phone1 = "";*/

			for (final ObjectValueContainer.ObjectValueHolder objectValueHolder : customerObjectValueContainer
					.getAllValues()) {
				final PropertyDescriptor propertyDescriptor = objectValueHolder
						.getPropertyDescriptor();

				if (propertyDescriptor instanceof ItemAttributePropertyDescriptor) {
					final ItemAttributePropertyDescriptor attributePropertyDescriptor = (ItemAttributePropertyDescriptor) propertyDescriptor;
					if (FIRST_NAME.equals(attributePropertyDescriptor
							.getAttributeQualifier()) && firstNameField.getValue() != null) {
							objectValueHolder.setLocalValue(firstNameField
									.getValue());
					}
					if (LAST_NAME.equals(attributePropertyDescriptor
							.getAttributeQualifier()) && lastNameField.getValue() != null) {
							objectValueHolder.setLocalValue(lastNameField
									.getValue());
					}
					if (NICK_NAME.equals(attributePropertyDescriptor
							.getAttributeQualifier()) && nickNameField.getValue() != null) {
							objectValueHolder.setLocalValue(nickNameField
									.getValue());
					}
					if (MOBILE_NUMBER.equals(attributePropertyDescriptor
							.getAttributeQualifier())) {
						if (mobileNumberField.getValue() != null && !(mobileNumberField.getValue().isEmpty())){
							if(mobileNumberField.getValue().matches(MOBILENUMBER_REGEX)) {
								objectValueHolder.setLocalValue(mobileNumberField
										.getValue());
							}
							else {
								Messagebox.show(LabelUtils.getLabel(widget, "mobileNumberIncorrectFormat"), LabelUtils.getLabel(widget, FAILED_TO_VALIDATE_CUSTOMER_FORM),
										Messagebox.OK, Messagebox.ERROR);
									return;
								}
						}
					}
					
					
					if (DOB.equals(attributePropertyDescriptor
							.getAttributeQualifier()) && dobDateBox.getValue() != null) {
							objectValueHolder.setLocalValue(dobDateBox
									.getValue());
					}
					if (DOMA.equals(attributePropertyDescriptor
							.getAttributeQualifier()) && domaDateBox.getValue() != null) {
							objectValueHolder.setLocalValue(domaDateBox
									.getValue());
					}
					if (GENDER.equals(attributePropertyDescriptor
							.getAttributeQualifier()) && genderListBox.getSelectedItem() != null) {
							objectValueHolder.setLocalValue(enumService
									.getEnumerationValue(Gender.class,
											genderListBox.getSelectedItem()
													.getLabel()));
					}
					if (EMAIL.equals(attributePropertyDescriptor
							.getAttributeQualifier())) {
						if (!StringUtils.isEmpty(eMailField.getValue())) {

							if(eMailField.getValue().length() > 255 ) {
								Messagebox.show(LabelUtils.getLabel(widget, INVALID_LENGTH, LabelUtils.getLabel(widget, attributePropertyDescriptor.getName().replace(" ", ""))), LabelUtils.getLabel(widget, FAILED_TO_VALIDATE_CUSTOMER_FORM),
										Messagebox.OK, Messagebox.ERROR);
								return;
							}
							if (!eMailField.getValue().matches(EMAIL_REGEX)) {
								Messagebox.show(LabelUtils.getLabel(widget, "emailIncorrectFormat"), LabelUtils.getLabel(widget, FAILED_TO_VALIDATE_CUSTOMER_FORM),
										Messagebox.OK, Messagebox.ERROR);
								return;
							}
							
							objectValueHolder.setLocalValue(eMailField
									.getValue().toLowerCase());

						} else {
							Messagebox.show(LabelUtils.getLabel(widget,
									"eMailEmpty"), LabelUtils.getLabel(widget,
									FAILED_TO_VALIDATE_CUSTOMER_FORM),
									Messagebox.OK, Messagebox.ERROR);
							return;
						}

					}
				}
			}

			handleCreateItemEvent(widget, event, customerObjectValueContainer,
					getEditorConfigurationType(),commPreferencesChkBoxes);
		}

		private void handleCreateItemEvent(
				InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerCreateController> widget2,
				Event event,
				ObjectValueContainer customerObjectValueContainer,
				String customerConfigurationTypeCode,
				List<Checkbox> commPreferencesChkBoxes) throws InterruptedException {
			// TODO Auto-generated method stub
			try {
				if(CollectionUtils.isNotEmpty(commPreferencesChkBoxes)) {
					final List<CommunicationPreferencesModel> commPreferencesList = new ArrayList<CommunicationPreferencesModel>();
					for (Checkbox commPreferenceChkBox : commPreferencesChkBoxes)
					{
						CommunicationPreferencesModel commPrefModel= modelService.create(CommunicationPreferencesModel.class);
						if(commPreferenceChkBox.isChecked()){
							commPrefModel.setCommPreferences(enumService.getEnumerationValue(CommPref.class, commPreferenceChkBox.getId()));
							commPrefModel.setSubscribed(Boolean.TRUE);
							modelService.save(commPrefModel);
							commPreferencesList.add(commPrefModel);
						}
						
					}
					sessionService.setAttribute("commPrefList", commPreferencesList);
				} else {
					LOG.warn("commPreferencesChkBoxes is null as the checkboxes are removed from the customer create widget");
				}
				
				TypedObject customerItem = widget.getWidgetController().createNewCustomer(customerObjectValueContainer,
						customerConfigurationTypeCode);
				if (customerItem == null) {
					return;
				}
				else{
					getItemAppender().add(customerItem, 1L);
				}
				
			} catch (ValueHandlerException ex) {
				Messagebox.show(ex.getMessage(), LabelUtils.getLabel(widget,
						"unableToCreateCustomer", new Object[0]), 1,
						"z-msgbox z-msgbox-error");
				LOG.debug("unable to create item", ex);
			} catch (EtailNonBusinessExceptions  | DuplicateUidException e) {
				Messagebox.show(LabelUtils.getLabel(widget,
						"accountAlreadyExists", new Object[0]), "Account Already Exists", 1,
						"z-msgbox z-msgbox-error");
				LOG.debug("unable to create item", e);
			}
			
		}

	}
}