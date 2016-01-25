package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.EventListener;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jfree.util.Log;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cscockpit.model.data.DataObject;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.search.SearchCommandController;
import de.hybris.platform.cscockpit.widgets.models.impl.TextSearchWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractSearcherWidgetRenderer;

import org.zkoss.zul.Toolbarbutton;






import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.impl.MarketplaceCustomerSearchQueryBuilder;

public class MarketplaceCustomerSearchWidgetRenderer extends AbstractSearcherWidgetRenderer {

	private static final String SELECT_ATLEAST_ONE_FIELD = "selectatleastonefield";
	private static final String SELECT_ATLEAST_ONE_FIELD_TITLE = "selectatleastonefieldtitle";
	
	protected static final String COCKPIT_ID_SEARCHFORCUSTOMER_CUSTOMERFIRSTNAME_INPUT = "SearchForCustomer_CustomerFirstName_input";
	protected static final String COCKPIT_ID_SEARCHFORCUSTOMER_CUSTOMERLASTNAME_INPUT = "SearchForCustomer_CustomerLastName_input";
	protected static final String COCKPIT_ID_SEARCHFORCUSTOMER_CUSTOMEREMAIL_INPUT = "SearchForCustomer_CustomerEmail_input";
	protected static final String COCKPIT_ID_SEARCHFORCUSTOMER_CUSTOMERPHONE_INPUT = "SearchForCustomer_CustomerPhone_input";
	protected static final String COCKPIT_ID_SEARCHFORCUSTOMER_CUSTOMERCREATIONDATE_INPUT = "SearchForCustomer_CustomerCreationDate_input";
	protected static final String COCKPIT_ID_SEARCHFORCUSTOMER_SEARCH_BUTTON = "SearchForCustomer_Search_button";

	@Override
	protected HtmlBasedComponent createSearchPane(
			ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget) {
		// TODO Auto-generated method stub
		// create 4 text boxes and a button with an event
		final Div CustomersearchPane = new Div();
 
		Div firstNameRowDiv = new Div();
		firstNameRowDiv.setSclass("searchCustomerRow");
		firstNameRowDiv.setParent(CustomersearchPane);
		Div lastNameRowDiv = new Div();
		lastNameRowDiv.setSclass("searchCustomerRow");
		lastNameRowDiv.setParent(CustomersearchPane);
		Div emailRowDiv = new Div();
		emailRowDiv.setSclass("searchCustomerRow");
		emailRowDiv.setParent(CustomersearchPane);
		Div phoneNumberRowDiv = new Div();
		phoneNumberRowDiv.setSclass("searchCustomerRow");
		phoneNumberRowDiv.setParent(CustomersearchPane);
		
		final Div firstNameField = new Div();
		firstNameField.setSclass("searchCustomer");
		final Div lastNameField = new Div();
		lastNameField.setSclass("searchCustomer");
		final Div emailField = new Div();
		emailField.setSclass("searchCustomer");
		final Div phoneNumberField = new Div();
		phoneNumberField.setSclass("searchCustomer");
		final Textbox searchFirstNameInput = createCustomerSearchTextField(widget, firstNameField, "firstname",
				MarketplaceCustomerSearchQueryBuilder.TextFields.FirstName, COCKPIT_ID_SEARCHFORCUSTOMER_CUSTOMERFIRSTNAME_INPUT);
		CustomersearchPane.appendChild(new Br());
		final Textbox searchLastNameInput =createCustomerSearchTextField(widget, lastNameField, "lastname",
				MarketplaceCustomerSearchQueryBuilder.TextFields.LastName, COCKPIT_ID_SEARCHFORCUSTOMER_CUSTOMERLASTNAME_INPUT);
		CustomersearchPane.appendChild(new Br());
		final Textbox searchEmailInput =createCustomerSearchTextField(widget, emailField, "email",
				MarketplaceCustomerSearchQueryBuilder.TextFields.Email, COCKPIT_ID_SEARCHFORCUSTOMER_CUSTOMEREMAIL_INPUT);
		CustomersearchPane.appendChild(new Br());
		final Textbox searchPhoneInput =createCustomerSearchTextField(widget, phoneNumberField, "phone",
				MarketplaceCustomerSearchQueryBuilder.TextFields.Phone, COCKPIT_ID_SEARCHFORCUSTOMER_CUSTOMERPHONE_INPUT);
		CustomersearchPane.appendChild(new Br());
    
		firstNameField.setParent(firstNameRowDiv);
		lastNameField.setParent(lastNameRowDiv);
		emailField.setParent(emailRowDiv);
		phoneNumberField.setParent(phoneNumberRowDiv);
		
		Div searchBtnRowDiv = new Div();
		searchBtnRowDiv.setSclass("searchCustomerRowDifferent");
		searchBtnRowDiv.setParent(CustomersearchPane);
		final Div searchBtn = new Div();
		final Button searchButton = createSearchButton(widget, searchBtn);
		searchBtn.setSclass("searchCustomerDifferent");
		searchBtn.setParent(searchBtnRowDiv);
		
		final Div blank = new Div();
		blank.setSclass("clear");
		blank.setParent(CustomersearchPane);
		
		// Attach event listeners
		attachSearchEventListener(widget,createMerchandiseSearchEventListener(widget, searchFirstNameInput, searchLastNameInput,searchEmailInput,searchPhoneInput), searchFirstNameInput, searchLastNameInput,searchEmailInput,searchPhoneInput,searchButton);
		return CustomersearchPane;
	}


	/**
	 * Craetes an object of createButton 
	 * @param widget
	 * @param parent
	 * @return
	 */
	private Button createSearchButton(
			ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			final Div parent) {
		// TODO Auto-generated method stub
		return createButton(widget, parent, "searchBtn", COCKPIT_ID_SEARCHFORCUSTOMER_SEARCH_BUTTON);
	}

	/**
	 * creates an object of createSearchTextField
	 * @param widget
	 * @param parent
	 * @param textFieldName
	 * @param queryBuilderTextField
	 * @param searchBoxField
	 * @return
	 */
	private Textbox createCustomerSearchTextField(
			ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			final Div parent, final String textFieldName, final MarketplaceCustomerSearchQueryBuilder.TextFields queryBuilderTextField,
			final String searchBoxField) {
		// TODO Auto-generated method stub
		return createSearchTextField(widget, parent, textFieldName, queryBuilderTextField, searchBoxField);
	}


	/**
	 * creates an object of createMerchandiseSearchEventListener
	 * @param widget
	 * @param firstNameInput
	 * @param lastNameInput
	 * @param emailInput
	 * @param phoneInput
	 * @return
	 */
	protected EventListener createMerchandiseSearchEventListener(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			final InputElement firstNameInput, final InputElement lastNameInput, final InputElement emailInput,final InputElement phoneInput) {
		return new SearchEventListener(widget,firstNameInput,lastNameInput,emailInput,phoneInput);
	}

	//Search Event Listener Class
	protected class SearchEventListener extends AbstractSearchEventListener implements EventListener {
		private final transient InputElement customerFirstNameInput;
		private final transient InputElement customerLastNameInput;
		private final transient InputElement customerEmailInput;
		private final transient InputElement customerPhoneInput;


		public SearchEventListener(
				final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
				final InputElement customerFirstNameInput, final InputElement customerLastNameInput,final InputElement customerEmailInput,final InputElement customerPhoneInput) {
			super(widget);
			this.customerFirstNameInput = customerFirstNameInput;
			this.customerLastNameInput = customerLastNameInput;
			this.customerEmailInput = customerEmailInput;
			this.customerPhoneInput = customerPhoneInput;

		}




		@Override
		protected void fillSearchCommand(DefaultCsTextSearchCommand command) {
			// TODO Auto-generated method stub

			if (customerFirstNameInput.getText().isEmpty()
					&& customerLastNameInput.getText().isEmpty()
					&& customerEmailInput.getText().isEmpty()
					&& customerPhoneInput.getText().isEmpty()) {
				try {
					Messagebox
							.show(LabelUtils.getLabel(getWidget(), SELECT_ATLEAST_ONE_FIELD), LabelUtils.getLabel(getWidget(), SELECT_ATLEAST_ONE_FIELD_TITLE), Messagebox.OK, Messagebox.INFORMATION);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.error(MarketplaceCockpitsConstants.EXCEPTION_IS, e);
				}
			}
			else {
			if (customerFirstNameInput != null && !customerFirstNameInput.getText().trim().equals("")) {
				command.setText(MarketplaceCustomerSearchQueryBuilder.TextFields.FirstName, customerFirstNameInput.getText());
			}
			if (customerLastNameInput != null && !customerLastNameInput.getText().trim().equals("")) {
				command.setText(MarketplaceCustomerSearchQueryBuilder.TextFields.LastName, customerLastNameInput.getText());
			}

			if (customerEmailInput != null && !customerEmailInput.getText().trim().equals("")) {
				command.setText(MarketplaceCustomerSearchQueryBuilder.TextFields.Email, customerEmailInput.getText());
			}
			if (customerPhoneInput != null && !customerPhoneInput.getText().trim().equals("")) {
				command.setText(MarketplaceCustomerSearchQueryBuilder.TextFields.Phone, customerPhoneInput.getText());
			}
		 }
		}


	}

	protected HtmlBasedComponent createContentInternal(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			final HtmlBasedComponent rootContainer)
	{
		final Div customerSearchContainer = new Div();
		customerSearchContainer.setSclass(CSS_SEARCH_CONTAINER);

		final HtmlBasedComponent searchPane = createSearchPane(widget);
		searchPane.setSclass(CSS_SEARCH_PANE);
		searchPane.setParent(customerSearchContainer);

		final Div resultContent = new Div();
		resultContent.setSclass(CSS_SEARCH_RESULTS);
		resultContent.setParent(customerSearchContainer);


		if (widget.getWidgetModel() != null && widget.getWidgetModel().getItems() != null
				&& !widget.getWidgetModel().getItems().isEmpty())
		{
			final Div toolbar = new Div();
			toolbar.setSclass(CSS_TOOLBAR);
			toolbar.appendChild(createPager(widget));
			toolbar.setParent(resultContent);
         
			//It displays the search results
			final HtmlBasedComponent container = createSearchResults(widget, rootContainer);
			container.setParent(resultContent);
		}
		else
		{
			
				final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noResults"));
				dummyLabel.setSclass(CSS_NO_SEARCH_RESULTS);
				dummyLabel.setParent(resultContent);

			
		}

		return customerSearchContainer;
	}

	/**
	 * It displays the search results
	 * @param widget
	 * @param rootContainer
	 * @return
	 */
	protected HtmlBasedComponent createSearchResults(final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,final HtmlBasedComponent rootContainer)
	{
		final Div container = new Div();
		container.setSclass(CSS_LISTBOX_CONTAINER);

		final Listbox listBox = new Listbox();
		listBox.setParent(container);
		widget.setListBox(listBox);
		listBox.setVflex(false);
		listBox.setFixedLayout(true);
		listBox.setSclass(CSS_WIDGET_LISTBOX);

		//It creates the headers and populates the header row
		renderListbox(listBox, widget, rootContainer);

		if (isLazyLoadingEnabled())
		{
			UITools.applyLazyload(listBox);
		}

		if (listBox.getItemCount() > 0 && listBox.getSelectedIndex() <= 0)
		{
			listBox.setSelectedIndex(0);
		}

		return container;
	}  

	@Override
	protected void renderListbox(
			Listbox listBox,
			ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			HtmlBasedComponent rootContainer) {
		
		TextSearchWidgetModel widgetModel = (TextSearchWidgetModel) widget
				.getWidgetModel();
		if (widgetModel == null)
		{
			return;
		}
		List<DataObject<TypedObject>> items = widgetModel.getItems();
		if ((items == null) || (items.isEmpty()))
		{
			return;
		}
		List<ColumnConfiguration> columns = getColumnConfigurations();
		
		if (!(CollectionUtils.isNotEmpty(columns)))
		{
			return;
		}
		Listhead headRow = new Listhead();
		headRow.setParent(listBox);

		populateHeaderRow(widget, headRow, columns);





		Listheader colHeader = new Listheader(LabelUtils.getLabel(widget,
				"actions", new Object[0]));
		colHeader.setWidth("80px");
		colHeader.setParent(headRow);





		for (DataObject metaItem : items) {
			TypedObject item = (TypedObject) metaItem.getItem();

			Listitem row = new Listitem();
			row.setParent(listBox);
			row.setSclass("csSearchResultRow");

			populateDataRow(widget, row, columns, item);

			Listcell actionCell = new Listcell();
			actionCell.setParent(row);
			Button actionButton = new Button(LabelUtils.getLabel(widget,
					"selectBtn", new Object[0]));
			actionButton.setParent(actionCell);
			actionButton.setSclass("btngreen");

			if (UISessionUtils.getCurrentSession().isUsingTestIDs()) {
				String testId = buildSelectItemButtonTestID(widget, item);
				if (testId != null) {
					UITools.applyTestID(actionButton, testId);
				}
			}

			//On selecting the customer details will be displayed
			SelectItemEventListener selectItemEventListener = createSelectItemEventListener(
					widget, item);
			row.addEventListener("onOK",selectItemEventListener);
			actionButton.addEventListener("onClick",selectItemEventListener);
		}
	}

	protected SelectItemEventListener createSelectItemEventListener(
			ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			TypedObject item) {
		return new SelectItemEventListener(widget, item);
	}

	protected void handleSelectItem(
			ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			TypedObject item) throws Exception {
		getItemAppender().add(item, 1L);
	}

	protected String buildSelectItemButtonTestID(
			ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			TypedObject item) {
		return null;
	}
	
	protected void attachSearchEventListener(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			final EventListener eventListener, final AbstractComponent... components)
	{
		if (eventListener != null)
		{
        	for (final AbstractComponent component : components)
			{
				if (component instanceof Button || component instanceof Toolbarbutton)
				{
					component.addEventListener(Events.ON_CLICK, (org.zkoss.zk.ui.event.EventListener) eventListener); // Click on search button
				}
				else if (component instanceof InputElement)
				{
					component.addEventListener(Events.ON_OK, (org.zkoss.zk.ui.event.EventListener) eventListener); // pressing <ENTER> in search input



				}
				else if (component instanceof Listbox)
				{
					component.addEventListener(Events.ON_SELECT, (org.zkoss.zk.ui.event.EventListener) eventListener); // selecting item in dropdown
				}
			}
		}
	}

}
