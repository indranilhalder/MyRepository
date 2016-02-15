package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CustomerController;
import de.hybris.platform.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.details.WidgetDetailRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCockpitEditorWidgetRenderer;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.Dates;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleDateConstraint;
import org.zkoss.zul.Textbox;

import com.tisl.mpl.cockpits.cscockpit.services.ItemModificationHistoryService;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.account.register.ForgetPasswordFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ForgetPasswordService;


public class MarketplaceCustomerEditDetailsWidgetRenderer extends
		AbstractCockpitEditorWidgetRenderer<InputWidget<CustomerItemWidgetModel, CustomerController>>
{

	private WidgetDetailRenderer<TypedObject, Widget> footerRenderer;
	private static final String EMPTY_SPACE = " ";
	private static final String SENDING_EMAIL_TO = "sendingemailto";
	private static final String CUSTOMER_DETAILS_UPDATED = "customerdetailsupdated";
	private static final String INFO = "info";
	private static final String SENDING_EMAIL_TO_TITLE = "sendingemailtotitle";

	@Autowired
	private BusinessProcessService businessProcessService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private ForgetPasswordFacade forgetPasswordFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private EnumerationService enumService;

	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private BaseStoreService baseStoreService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private I18NService i18NService;

	@Autowired
	private ForgetPasswordService forgetPasswordService;

	@Autowired
	private ItemModificationHistoryService itemModificationHistoryService;

	@Autowired
	private ConfigurationService configurationService;

	/**
	 * Creates a horizontal box for the field.
	 *
	 * @param widget
	 * @param attributeLabel
	 * @param hidden
	 * @param overwriteWidth
	 * @return
	 */

	private Hbox createHbox(final InputWidget<CustomerItemWidgetModel, CustomerController> widget, final String attributeLabel,
			final boolean hidden, final boolean overwriteWidth)
	{
		final Hbox hbox = new Hbox();
		hbox.setWidth("96%");
		if (overwriteWidth)
		{
			hbox.setWidths("9em, none");
		}
		hbox.setAlign("center");

		if (hidden)
		{
			hbox.setVisible(false);
		}

		hbox.setClass("editorWidgetEditor");
		final Label label = new Label(LabelUtils.getLabel(widget, attributeLabel));
		label.setParent(hbox);

		return hbox;
	}

	/**
	 * Creates a textbox
	 *
	 * @param id
	 * @param parent
	 * @return
	 */
	private Textbox createTextbox(final Hbox parent)
	{
		final Textbox textBox = new Textbox();
		textBox.setWidth("40%");
		textBox.setParent(parent);
		return textBox;
	}

	private Textbox createPrefixTextbox(final Div parent)
	{
		final Textbox textBox = new Textbox();
		textBox.setWidth("6%");
		textBox.setParent(parent);
		textBox.setValue("+91");
		textBox.setDisabled(true);
		return textBox;
	}

	private Textbox createMobileNumberTextbox(final Div parent)
	{
		final Textbox textBox = new Textbox();
		textBox.setWidth("32.73%");
		textBox.setParent(parent);
		return textBox;
	}

	/**
	 * Creates a datebox
	 *
	 * @param id
	 * @param parent
	 * @return
	 */
	private Datebox createDatebox(final Hbox parent,boolean allowFetureDate)
	{
		final Datebox dateBox = new Datebox();
		dateBox.setFormat("dd/MM/yyyy");
		dateBox.setWidth("25%");
		if(!allowFetureDate){
			SimpleDateConstraint simpleDateConstraint = new SimpleDateConstraint(1,null,Dates.today(),"Can not be a future Date");
			dateBox.setConstraint(simpleDateConstraint);
		} 
		dateBox.setParent(parent);
		return dateBox;
	}

	/**
	 * Creates an object of genderDropdownField
	 *
	 * @param widget
	 * @param genderHbox
	 * @param gender
	 * @return
	 */

	private Listbox createGenderListbox(final InputWidget<CustomerItemWidgetModel, CustomerController> widget,
			final Hbox genderHbox, final Gender gender)
	{
		// TODO Auto-generated method stub
		return genderDropdownField(widget, genderHbox, gender);
	}

	/**
	 * Creates Dropdown for gender
	 *
	 * @param widget
	 * @param genderHbox
	 * @param gender
	 * @return
	 */

	private Listbox genderDropdownField(final InputWidget<CustomerItemWidgetModel, CustomerController> widget,
			final Hbox genderHbox, final Gender gender)
	{
		final Listbox listbox = new Listbox();
		genderHbox.appendChild(listbox);
		listbox.setMultiple(false);
		listbox.setMold("select");
		listbox.setWidth("27.8%");

		final List<Gender> values = enumService.getEnumerationValues(Gender.class);

		listbox.appendItem(LabelUtils.getLabel(widget, "defaultSelectOption"), StringUtils.EMPTY);
		for (final Gender value : values)
		{
			final Listitem listItem = new Listitem(value.getCode());
			listItem.setParent(listbox);
			if (gender == null)
			{
				continue;
			}

			if (gender.getCode() != null && listItem.getLabel().equalsIgnoreCase(gender.getCode()))
			{
				listbox.setSelectedItem(listItem);
			}

		}
		return listbox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer
	 * #createContentInternal(de.hybris.platform.cockpit.widgets.Widget, org.zkoss.zk.ui.api.HtmlBasedComponent)
	 */
	@Override
	protected org.zkoss.zk.ui.api.HtmlBasedComponent createContentInternal(
			final InputWidget<CustomerItemWidgetModel, CustomerController> widget,
			final org.zkoss.zk.ui.api.HtmlBasedComponent rootContainer)
	{
		// TODO Auto-generated method stub
		final Div content = new Div();
		content.setClass("customerDetails");

		final TypedObject customer = widget.getWidgetModel().getCustomer();
		if (customer != null)
		{

			final CustomerModel customer1 = (CustomerModel) customer.getObject();

			// Add First Name hbox
			final Hbox firstNameHbox = createHbox(widget, "FirstName", false, true);
			final Textbox firstNameFieldTextBox = createTextbox(firstNameHbox);
			if (customer1.getFirstName() != null)
			{
				firstNameFieldTextBox.setValue(customer1.getFirstName());
			}
			content.appendChild(firstNameHbox);

			// the Last name hbox
			final Hbox lastNameHbox = createHbox(widget, "LastName", false, true);
			final Textbox lastNameFieldTextBox = createTextbox(lastNameHbox);
			if (customer1.getLastName() != null)
			{
				lastNameFieldTextBox.setValue(customer1.getLastName());
			}
			content.appendChild(lastNameHbox);

			// Add Nick Name hbox
			final Hbox nickNameHbox = createHbox(widget, "NickName", false, true);
			final Textbox nickNameFieldTextBox = createTextbox(nickNameHbox);
			if (customer1.getNickName() != null && !customer1.getNickName().isEmpty())
			{
				nickNameFieldTextBox.setValue(customer1.getNickName());
			}
			content.appendChild(nickNameHbox);

			// Email
			final Hbox emailHbox = createHbox(widget, "Email", false, true);

			final Textbox emailFieldTextBox = createTextbox(emailHbox);
			if (customer1.getOriginalUid() != null)
			{
				emailFieldTextBox.setValue(customer1.getOriginalUid());
			}
			content.appendChild(emailHbox);

			// Phone number
			final Hbox phoneNumberHbox = createHbox(widget, "PhoneNumber", false, true);
			final Div phoneNumberRowDiv = new Div();
			phoneNumberRowDiv.setParent(phoneNumberHbox);
			createPrefixTextbox(phoneNumberRowDiv);
			final Textbox mobileNumberFieldTextBox = createMobileNumberTextbox(phoneNumberRowDiv);
			if (customer1.getMobileNumber() != null)
			{
				mobileNumberFieldTextBox.setValue(customer1.getMobileNumber());
			}
			content.appendChild(phoneNumberHbox);

			final Hbox dobHbox = createHbox(widget, "DateOfBirth", false, true);
			final Datebox dobDateBox = createDatebox(dobHbox,false);
			if (customer1.getDateOfBirth() != null)
			{
				dobDateBox.setValue(customer1.getDateOfBirth());
			}
			content.appendChild(dobHbox);

			// Marriage Anniversary Date
			final Hbox domaHbox = createHbox(widget, "DateOfMarriageAnniversary", false, true);
			final Datebox domaDateBox = createDatebox(domaHbox,false);
			if (customer1.getDateOfAnniversary() != null)
			{
				domaDateBox.setValue(customer1.getDateOfAnniversary());
			}
			content.appendChild(domaHbox);

			// Gender
			final Hbox genderHbox = createHbox(widget, "Gender", false, true);
			final Listbox genderListBox = createGenderListbox(widget, genderHbox, customer1.getGender());
			content.appendChild(genderHbox);

			// put all data in Map for further comparison
			final Map<String, String> preSavedDetailMap = new HashMap<String, String>();
			final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
			if (customer1.getFirstName() != null && !customer1.getFirstName().isEmpty())
			{
				preSavedDetailMap.put("fName", customer1.getFirstName());
			}
			else
			{

				preSavedDetailMap.put("fName", MarketplacecommerceservicesConstants.EMPTY);

			}
			if (customer1.getLastName() != null && !customer1.getLastName().isEmpty())
			{
				preSavedDetailMap.put("lName", customer1.getLastName());
			}
			else
			{

				preSavedDetailMap.put("lName", MarketplacecommerceservicesConstants.EMPTY);

			}
			if (customer1.getNickName() != null && !customer1.getNickName().isEmpty())
			{
				preSavedDetailMap.put("nName", customer1.getNickName());
			}
			else
			{

				preSavedDetailMap.put("nName", MarketplacecommerceservicesConstants.EMPTY);

			}
			if (customer1.getDateOfBirth() != null)
			{
				preSavedDetailMap.put("dOfBirth", sdfDate.format(customer1.getDateOfBirth()));
			}
			else
			{
				preSavedDetailMap.put("dOfBirth", MarketplacecommerceservicesConstants.EMPTY);
			}
			if (customer1.getDateOfAnniversary() != null)
			{
				preSavedDetailMap.put("dOfAnniversary", sdfDate.format(customer1.getDateOfAnniversary()));
			}
			else
			{
				preSavedDetailMap.put("dOfAnniversary", MarketplacecommerceservicesConstants.EMPTY);
			}
			if (customer1.getMobileNumber() != null && !customer1.getMobileNumber().isEmpty())
			{
				preSavedDetailMap.put("mNumber", customer1.getMobileNumber());
			}
			else
			{

				preSavedDetailMap.put("mNumber", MarketplacecommerceservicesConstants.EMPTY);

			}
			if (customer1.getGender() != null)
			{
				preSavedDetailMap.put("gender", genderListBox.getSelectedItem().getLabel());
			}
			else
			{
				preSavedDetailMap.put("gender", MarketplacecommerceservicesConstants.EMPTY);
			}
			if (customer1.getOriginalUid() != null && !customer1.getOriginalUid().isEmpty())
			{
				preSavedDetailMap.put("emailId", customer1.getOriginalUid());
			}
			else
			{

				preSavedDetailMap.put("emailId", MarketplacecommerceservicesConstants.EMPTY);

			}

			// Reset Password button
			final Button reset = new Button(LabelUtils.getLabel(widget, "resetButton"));
			reset.setParent(content);
			reset.setSclass("updateBtn");
			reset.addEventListener(Events.ON_CLICK, createResetPasswordEventListener(widget, customer1.getUid()));

			final Div actionContainer = new Div();
			actionContainer.setSclass("updateBtn");
			actionContainer.setParent(content);

			// Update button
			final Button update = new Button(LabelUtils.getLabel(widget, "Update"));
			update.setParent(content);

			update.addEventListener(
					Events.ON_CLICK,
					createUpdateDetailsEventListener(widget, customer1, firstNameFieldTextBox, lastNameFieldTextBox,
							nickNameFieldTextBox, emailFieldTextBox, mobileNumberFieldTextBox, dobDateBox, domaDateBox, genderListBox,
							preSavedDetailMap));

			renderFooter(content, widget);

			return content;
		}
		else
		{
			content.appendChild(new Label(LabelUtils.getLabel(widget, "noCustomerSelected")));
		}
		return content;
	}


	/**
	 * Creates an object of UpdateDetailsEventListener
	 *
	 * @param widget
	 * @param customer1
	 * @param firstNameFieldTextBox
	 * @param lastNameFieldTextBox
	 * @param mobileNumberFieldTextBox
	 * @param dobDateBox
	 * @param domaDateBox
	 * @param genderListBox
	 * @param preSavedDetailMap
	 * @return
	 */

	private EventListener createUpdateDetailsEventListener(final InputWidget<CustomerItemWidgetModel, CustomerController> widget,
			final CustomerModel customer1, final Textbox firstNameFieldTextBox, final Textbox lastNameFieldTextBox,
			final Textbox nickNameFieldTextBox, final Textbox emailFieldTextBox, final Textbox mobileNumberFieldTextBox,
			final Datebox dobDateBox, final Datebox domaDateBox, final Listbox genderListBox,
			final Map<String, String> preSavedDetailMap)
	{

		// TODO Auto-generated method stub
		return new UpdateDetailsEventListener(widget, customer1, firstNameFieldTextBox, lastNameFieldTextBox, nickNameFieldTextBox,
				emailFieldTextBox, mobileNumberFieldTextBox, dobDateBox, domaDateBox, genderListBox, preSavedDetailMap);
	}

	/**
	 * Creates an object of PasswordResetEventListener
	 *
	 * @param widget
	 * @param customerID
	 * @param customerPK
	 * @return
	 */

	private EventListener createResetPasswordEventListener(final InputWidget<CustomerItemWidgetModel, CustomerController> widget,
			final String customerID)
	{
		// TODO Auto-generated method stub
		return new PasswordResetEventListener(widget, customerID);
	}

	/**
	 * Creates a footer
	 *
	 * @param content
	 * @param widget
	 */
	protected void renderFooter(final HtmlBasedComponent content,
			final InputWidget<CustomerItemWidgetModel, CustomerController> widget)
	{
		final Div footerContent = new Div();
		footerContent.setSclass("csCustomerFooter");
		footerContent.setParent(content);

		final TypedObject customer = widget.getWidgetModel().getCustomer();
		if ((customer == null) || (!(customer.getObject() instanceof CustomerModel)))
		{
			return;
		}
		final HtmlBasedComponent detailContent = (HtmlBasedComponent) getFooterRenderer().createContent(null, customer, widget);
		if (detailContent == null)
		{
			return;
		}
		footerContent.appendChild(detailContent);
	}

	protected WidgetDetailRenderer<TypedObject, Widget> getFooterRenderer()
	{
		return this.footerRenderer;
	}

	/**
	 * Creates the footer in edit customer section
	 *
	 * @param footerRenderer
	 */
	@Required
	public void setFooterRenderer(final WidgetDetailRenderer<TypedObject, Widget> footerRenderer)
	{
		this.footerRenderer = footerRenderer;
	}

	protected class PasswordResetEventListener implements EventListener
	{

		private final InputWidget<CustomerItemWidgetModel, CustomerController> widget;
		private final String customerID;

		public PasswordResetEventListener(final InputWidget<CustomerItemWidgetModel, CustomerController> widget,
				final String customerID)
		{

			super();
			this.widget = widget;
			this.customerID = customerID;
		}

		@Override
		public void onEvent(final Event event) throws InterruptedException
		{
			// TODO Auto-generated method stub

			handlePasswordResetEvent(widget, customerID, event);
			widget.getWidgetController().dispatchEvent(null, widget, null);
		}

	}

	/**
	 *
	 * @param key
	 * @return String
	 */

	//	protected String lookupConfig(final String key)
	//	{
	//		return configurationService.getConfiguration().getString(key, null);
	//	}

	/**
	 * Creates the event to reset the password
	 *
	 * @param widget
	 * @param customerID
	 * @param customerPK
	 * @param event
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InterruptedException
	 */

	public void handlePasswordResetEvent(final InputWidget<CustomerItemWidgetModel, CustomerController> widget,
			final String customerID, final Event event) throws InterruptedException
	{

		final CustomerModel customer = (CustomerModel) userService.getUserForUID(customerID);
		if (customer != null)
		{
			final String securePwdUrl = configurationService.getConfiguration().getString("cockpit.forgot.url");
			final String securePwdUrlFinal = securePwdUrl + "/store/mpl/en/login/pw/change";
			forgetPasswordFacade.forgottenPasswordForEmail(customer.getOriginalUid(), securePwdUrlFinal);
			Messagebox.show(LabelUtils.getLabel(widget, SENDING_EMAIL_TO), LabelUtils.getLabel(widget, SENDING_EMAIL_TO_TITLE),
					Messagebox.OK, Messagebox.INFORMATION + customer.getOriginalUid());
		}

	}

	protected class UpdateDetailsEventListener implements EventListener
	{

		private final InputWidget<CustomerItemWidgetModel, CustomerController> widget;
		private static final String FAILED_TO_UPDATE_CUSTOMER_FORM = "failedToUpdateCustomerForm";
		private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		private static final String MOBILENUMBER_REGEX = "^[0-9]{10}";
		private final CustomerModel customer1;
		private final Textbox firstNameFieldTextBox;
		private final Textbox lastNameFieldTextBox;
		private final Textbox nickNameFieldTextBox;
		private final Textbox emailFieldTextBox;
		private final Textbox mobileNumberFieldTextBox;

		private final Datebox dobDateBox;
		private final Datebox domaDateBox;
		private final Listbox genderListBox;
		private final Map<String, String> preSavedDetailMap;

		/**
		 * Parameterized constructor for update event listener
		 *
		 * @param widget
		 * @param customer1
		 * @param firstNameFieldTextBox
		 * @param lastNameFieldTextBox
		 * @param mobileNumberFieldTextBox
		 * @param dobDateBox
		 * @param domaDateBox
		 * @param genderListBox
		 * @param preSavedDetailMap2
		 */
		public UpdateDetailsEventListener(final InputWidget<CustomerItemWidgetModel, CustomerController> widget,
				final CustomerModel customer1, final Textbox firstNameFieldTextBox, final Textbox lastNameFieldTextBox,
				final Textbox nickNameFieldTextBox, final Textbox emailFieldTextBox, final Textbox mobileNumberFieldTextBox,
				final Datebox dobDateBox, final Datebox domaDateBox, final Listbox genderListBox,
				final Map<String, String> preSavedDetailMap2)
		{
			this.widget = widget;
			this.customer1 = customer1;

			this.firstNameFieldTextBox = firstNameFieldTextBox;
			this.lastNameFieldTextBox = lastNameFieldTextBox;
			this.nickNameFieldTextBox = nickNameFieldTextBox;
			this.emailFieldTextBox = emailFieldTextBox;
			this.mobileNumberFieldTextBox = mobileNumberFieldTextBox;
			this.dobDateBox = dobDateBox;
			this.domaDateBox = domaDateBox;
			this.genderListBox = genderListBox;
			this.preSavedDetailMap = preSavedDetailMap2;
		}

		@Override
		public void onEvent(final Event event) throws InterruptedException, ParseException
		{
			handleUpdateDetails(event, customer1, firstNameFieldTextBox, lastNameFieldTextBox, nickNameFieldTextBox,
					emailFieldTextBox, mobileNumberFieldTextBox, dobDateBox, domaDateBox, genderListBox, preSavedDetailMap);
		}

		/**
		 * Updates the customer details
		 *
		 * @param <E>
		 * @param event
		 * @param customer
		 * @param firstNameFieldTextBox
		 * @param lastNameFieldTextBox
		 * @param mobileNumberFieldTextBox
		 * @param dobDateBox
		 * @param domaDateBox
		 * @param genderListBox
		 * @param preSavedDetailMap2
		 * @throws InterruptedException
		 * @throws ParseException
		 */
		@SuppressWarnings({ "deprecation", "null" })
		private void handleUpdateDetails(final Event event, final CustomerModel customer, final Textbox firstNameFieldTextBox,
				final Textbox lastNameFieldTextBox, final Textbox nickNameFieldTextBox, final Textbox emailFieldTextBox,
				final Textbox mobileNumberFieldTextBox, final Datebox dobDateBox, final Datebox domaDateBox,
				final Listbox genderListBox, final Map<String, String> preSavedDetailMap2) throws InterruptedException,
				ParseException
		{

			modelService.removeAll(customer.getCommPreferences());

			final String changedFirstName = firstNameFieldTextBox.getValue();
			final String changedLastName = lastNameFieldTextBox.getValue();
			final String changedNickName = nickNameFieldTextBox.getValue();
			final String changedEmail = emailFieldTextBox.getValue();
			final String changedMobile = mobileNumberFieldTextBox.getValue();

			final Date changedDateOfBirth = dobDateBox.getValue();
			final Date changedDateOfMarriageAnniversary = domaDateBox.getValue();
			String changedGender = null;
			if (genderListBox.getSelectedItem() != null)
			{
				changedGender = genderListBox.getSelectedItem().getLabel();
			}

			boolean error = false;

			if (changedMobile != null && !changedMobile.isEmpty()
					&& !(mobileNumberFieldTextBox.getValue().matches(MOBILENUMBER_REGEX)))
			{
				error = true;
				Messagebox.show(LabelUtils.getLabel(widget, "mobileNumberIncorrectFormat"),
						LabelUtils.getLabel(widget, FAILED_TO_UPDATE_CUSTOMER_FORM), Messagebox.OK, Messagebox.ERROR);
				return;

			}

			if (changedEmail != null && !changedEmail.isEmpty() && !(emailFieldTextBox.getValue().matches(EMAIL_REGEX)))
			{
				error = true;
				Messagebox.show(LabelUtils.getLabel(widget, "emailIncorrectFormat"),
						LabelUtils.getLabel(widget, FAILED_TO_UPDATE_CUSTOMER_FORM), Messagebox.OK, Messagebox.ERROR);
				return;

			}

		    if (changedEmail != null && !changedEmail.isEmpty() && emailFieldTextBox.getValue().matches(EMAIL_REGEX)
					&& changedEmail != customer.getOriginalUid() && !(forgetPasswordService.getCustomer(changedEmail).isEmpty()))
			{
				error = true;
				Messagebox.show(LabelUtils.getLabel(widget, "duplicateemail"),
						LabelUtils.getLabel(widget, FAILED_TO_UPDATE_CUSTOMER_FORM), Messagebox.OK, Messagebox.ERROR);
				return;
			}
			
			if(changedEmail.isEmpty()) {
				error = true;
				Messagebox.show(LabelUtils.getLabel(widget, "emailidblank"),
						LabelUtils.getLabel(widget, FAILED_TO_UPDATE_CUSTOMER_FORM), Messagebox.OK, Messagebox.ERROR);
				return;
			}

			if (!error)
			{

				if (changedFirstName != null && !changedFirstName.isEmpty())
				{
					customer.setFirstName(changedFirstName);

				}
				else
				{
					customer1.setFirstName(MarketplacecommerceservicesConstants.EMPTY);
				}

				if (changedLastName != null && !changedLastName.isEmpty())
				{
					customer.setLastName(changedLastName);
				}
				else
				{
					customer1.setLastName(MarketplacecommerceservicesConstants.EMPTY);
				}
				if (changedNickName != null && !changedNickName.isEmpty())
				{
					customer.setNickName(changedNickName);
				}
				else
				{
					customer1.setNickName(MarketplacecommerceservicesConstants.EMPTY);
				}
				if (changedEmail != null && !changedEmail.isEmpty())
				{
					customer.setOriginalUid(changedEmail);
				}
				else
				{
					customer1.setOriginalUid(MarketplacecommerceservicesConstants.EMPTY);
				}
				if (changedMobile != null && !changedMobile.isEmpty())
				{
					customer.setMobileNumber(changedMobile);
				}
				else
				{
					customer1.setMobileNumber(MarketplacecommerceservicesConstants.EMPTY);
				}

				if (changedDateOfBirth != null)
				{
					customer.setDateOfBirth(changedDateOfBirth);
				}
				else
				{
					customer1.setDateOfBirth(null);
				}
				if (changedDateOfMarriageAnniversary != null)
				{
					customer.setDateOfAnniversary(changedDateOfMarriageAnniversary);
				}
				else
				{
					customer1.setDateOfAnniversary(null);
				}
				if (changedGender != null && !changedGender.isEmpty())
				{
					customer.setGender(enumService.getEnumerationValue(Gender.class, changedGender));
				}
				else
				{
					customer1.setGender(null);
				}

				setCustomerName(customer);
				itemModificationHistoryService.logItemModification(itemModificationHistoryService.createModificationInfo(customer));
				modelService.save(customer);

				Messagebox.show(LabelUtils.getLabel(widget, CUSTOMER_DETAILS_UPDATED, new Object[0]), INFO, Messagebox.OK,
						Messagebox.INFORMATION);

				// Sends email for any changes in customer account
				sendEmailForUpdateProfile(customer, preSavedDetailMap2);

				Executions.sendRedirect("/index.zul");
			}

		}

		/**
		 * Sends email for any changes in customer account
		 *
		 * @param customerCurrentData
		 * @param preSavedDetailMap
		 */
		private void sendEmailForUpdateProfile(final CustomerModel customerCurrentData, final Map<String, String> preSavedDetailMap)
		{
			final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
			Integer preDoa = 0;
			Integer currDoa = 0;
			final List<String> updatedDetailList = new ArrayList<String>();

			final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = (StoreFrontCustomerProcessModel) businessProcessService
					.createProcess("customerProfileUpdateEmailProcess" + System.currentTimeMillis(),
							"customerProfileUpdateEmailProcess");
			final Iterator<Map.Entry<String, String>> entries = preSavedDetailMap.entrySet().iterator();

			while (entries.hasNext())
			{
				final Map.Entry<String, String> entry = entries.next();

				if (entry.getKey().equalsIgnoreCase("fName")
						&& !entry.getValue().equalsIgnoreCase(customerCurrentData.getFirstName()))
				{
					updatedDetailList.add("fn");
				}
				if (entry.getKey().equalsIgnoreCase("lName") && !entry.getValue().equalsIgnoreCase(customerCurrentData.getLastName()))
				{
					updatedDetailList.add("ln");
				}
				if (entry.getKey().equalsIgnoreCase("nName") && !entry.getValue().equalsIgnoreCase(customerCurrentData.getNickName()))
				{
					updatedDetailList.add("nn");
				}
				if (entry.getKey().equalsIgnoreCase("dOfBirth"))
				{

					if (null != entry.getValue() && !MarketplacecommerceservicesConstants.EMPTY.equals(entry.getValue()))
					{
						final String doa = entry.getValue();
						preDoa = Integer.valueOf(doa);
					}
					else
					{
						if (null != customerCurrentData.getDateOfBirth())
						{
							updatedDetailList.add("dob");
						}
					}
					if (null != customerCurrentData.getDateOfBirth())
					{
						final String curDoa = sdfDate.format(customerCurrentData.getDateOfBirth());
						currDoa = Integer.valueOf(curDoa);
					}
					else
					{
						if (null != entry.getValue() && !MarketplacecommerceservicesConstants.EMPTY.equals(entry.getValue()))
						{
							updatedDetailList.add("dob");
						}
					}

					if (null != entry.getValue() && !MarketplacecommerceservicesConstants.EMPTY.equals(entry.getValue())
							&& null != customerCurrentData.getDateOfBirth())
					{
						if ((preDoa.compareTo(currDoa) > 0) || (preDoa.compareTo(currDoa) < 0))
						{
							updatedDetailList.add("dob");
						}
					}

				}
				if (entry.getKey().equalsIgnoreCase("dOfAnniversary"))
				{

					if (null != entry.getValue() && !MarketplacecommerceservicesConstants.EMPTY.equals(entry.getValue()))
					{
						final String doa = entry.getValue();
						preDoa = Integer.valueOf(doa);
					}
					else
					{
						if (null != customerCurrentData.getDateOfAnniversary())
						{
							updatedDetailList.add("doa");
						}
					}
					if (null != customerCurrentData.getDateOfAnniversary())
					{
						final String curDoa = sdfDate.format(customerCurrentData.getDateOfAnniversary());
						currDoa = Integer.valueOf(curDoa);
					}
					else
					{
						if (null != entry.getValue() && !MarketplacecommerceservicesConstants.EMPTY.equals(entry.getValue()))
						{
							updatedDetailList.add("doa");
						}
					}

					if (null != entry.getValue() && !MarketplacecommerceservicesConstants.EMPTY.equals(entry.getValue())
							&& null != customerCurrentData.getDateOfAnniversary())
					{
						if ((preDoa.compareTo(currDoa) > 0) || (preDoa.compareTo(currDoa) < 0))
						{
							updatedDetailList.add("doa");
						}
					}
				}

				if (entry.getKey().equalsIgnoreCase("mNumber")
						&& !entry.getValue().equalsIgnoreCase(customerCurrentData.getMobileNumber()))
				{
					updatedDetailList.add("mob");
				}
				if (entry.getKey().equalsIgnoreCase("gender"))
				{
					boolean flag = Boolean.FALSE;
					if (null != entry.getValue() && !MarketplacecommerceservicesConstants.EMPTY.equals(entry.getValue()))
					{
						if ((entry.getValue().equalsIgnoreCase("MALE")) && (customerCurrentData.getGender().equals(Gender.MALE)))
						{
							flag = Boolean.FALSE;
						}
						else if ((entry.getValue().equalsIgnoreCase("FEMALE"))
								&& (customerCurrentData.getGender().equals(Gender.FEMALE)))
						{
							flag = Boolean.FALSE;
						}
						else
						{
							flag = Boolean.TRUE;
						}

						if (flag)
						{
							updatedDetailList.add("gen");
						}
					}
					else
					{
						if (null != customerCurrentData.getGender())
						{
							updatedDetailList.add("gen");
						}
					}
				}
				if (entry.getKey().equalsIgnoreCase("emailId")
						&& !entry.getValue().equalsIgnoreCase(customerCurrentData.getOriginalUid()))
				{
					updatedDetailList.add("email");
				}
			}

			storeFrontCustomerProcessModel.setSite(baseSiteService.getCurrentBaseSite());
			storeFrontCustomerProcessModel.setStore(baseStoreService.getCurrentBaseStore());
			storeFrontCustomerProcessModel.setCustomer(customerCurrentData);
			storeFrontCustomerProcessModel.setLanguage(commonI18NService.getCurrentLanguage());
			storeFrontCustomerProcessModel.setCurrency(i18NService.getCurrentCurrency());
			storeFrontCustomerProcessModel.setCustomerProfileList(updatedDetailList);
			modelService.save(storeFrontCustomerProcessModel);
			businessProcessService.startProcess(storeFrontCustomerProcessModel);

		}




		/**
		 * Appends the customer's first and last name to customer's name
		 *
		 * @param customerModel
		 */
		private void setCustomerName(final CustomerModel customerModel)
		{

			final StringBuilder name = new StringBuilder();

			if (((StringUtils.isEmpty(customerModel.getFirstName())) || (StringUtils.isBlank(customerModel.getFirstName().trim())))
					&& ((StringUtils.isEmpty(customerModel.getLastName())) || (StringUtils.isBlank(customerModel.getLastName().trim()))))
			{
				customerModel.setName(customerModel.getOriginalUid());

			}
			else
			{

				if (StringUtils.isNotEmpty(customerModel.getFirstName()))
				{
					name.append(customerModel.getFirstName());

					name.append(EMPTY_SPACE);
				}

				if (StringUtils.isNotEmpty(customerModel.getLastName()))
				{
					name.append(customerModel.getLastName());

				}

				customerModel.setName(name.toString());
			}

		}
	}

}
