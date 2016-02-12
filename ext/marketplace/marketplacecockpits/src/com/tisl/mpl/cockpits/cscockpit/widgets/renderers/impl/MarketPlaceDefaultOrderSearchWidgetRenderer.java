package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.tisl.mpl.cockpits.cscockpit.services.impl.MarketplaceDefaultOrderSearchQueryBuilder;
import com.tisl.mpl.cockpits.cscockpit.services.impl.MarketplaceDefaultOrderSearchQueryBuilder.TextField;

import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.models.impl.TextSearchWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractSearcherWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderSearchWidgetRenderer;
import de.hybris.platform.servicelayer.config.ConfigurationService;

public class MarketPlaceDefaultOrderSearchWidgetRenderer extends
		OrderSearchWidgetRenderer {
	private Textbox orderNumber;
	private Textbox customerFirstName;
	private Textbox customerLastName;
	private Textbox customerMobileNumber;
	private Textbox customerEmailId;
	private Datebox fromDateDatebox;
	private Datebox toDateDatebox;
	private Listbox orderStatus;
	private Listbox deliveryMode;
	private Listbox salesApplications;

	private static final String EMPTY_STRING_CONSTANT = "Empty";

	@Autowired
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceDefaultOrderSearchWidgetRenderer.class);

	protected HtmlBasedComponent createSearchPane(final ListboxWidget widget) {
		final Div searchPane = new Div();

		orderNumber = createSearchTextField(widget, searchPane,
				"orderIdOrCustomerName",
				MarketplaceDefaultOrderSearchQueryBuilder.TextField.OrderId,
				null);
		orderNumber.setSclass("orderNumber");
		orderNumber.setMaxlength(17);
		String errorMessageOrder = LabelUtils.getLabel(widget,
				"error.msg.order", new Object[0]);
		orderNumber.setConstraint("/^[0-9-]*$/:" + errorMessageOrder);

		final Label custDetailHeading = new Label(LabelUtils.getLabel(widget,
				"customer.detail.heading", new Object[0]));
		custDetailHeading.setSclass("customerDetailsHeading");
		searchPane.appendChild(custDetailHeading);
		customerFirstName = createSearchTextField(
				widget,
				searchPane,
				"firstname.label",
				MarketplaceDefaultOrderSearchQueryBuilder.TextField.CustomerFirstName,
				"customer_first_name");
		customerFirstName.setSclass("customerFirstName");
		customerLastName = createSearchTextField(
				widget,
				searchPane,
				"lastname.label",
				MarketplaceDefaultOrderSearchQueryBuilder.TextField.CustomerLastName,
				"customer_last_name");
		customerLastName.setSclass("customerLastName");

		customerMobileNumber = createSearchTextField(
				widget,
				searchPane,
				"mobile.label",
				MarketplaceDefaultOrderSearchQueryBuilder.TextField.CustomerMobile,
				null);
		customerMobileNumber.setSclass("customerMobileNumber");
		customerMobileNumber.setMaxlength(10);
		String errorMsgMobile = LabelUtils.getLabel(widget, "error.msg.mobile",
				new Object[0]);
		customerMobileNumber.setConstraint("/[0-9]*$/:" + errorMsgMobile);

		customerEmailId = createSearchTextField(
				widget,
				searchPane,
				"email.label",
				MarketplaceDefaultOrderSearchQueryBuilder.TextField.CustomerEmail,
				null);
		customerEmailId.setSclass("customerEmailId");

		final Label orderDateHeading = new Label(LabelUtils.getLabel(widget,
				"customer.order.details.heading", new Object[0]));
		orderDateHeading.setSclass("orderDateHeading");
		searchPane.appendChild(orderDateHeading);

		final Label fromDateHeading = new Label(LabelUtils.getLabel(widget,
				"customer.from.order.heading", new Object[0]));
		fromDateHeading.setSclass("fromDateHeading");
		searchPane.appendChild(fromDateHeading);

		fromDateDatebox = new Datebox();
		fromDateDatebox.setSclass("fromDatebox");
		fromDateDatebox.setAttribute("size", "21");
		String currentValue = getCurrentValue(
				widget,
				MarketplaceDefaultOrderSearchQueryBuilder.TextField.OrderDateInit);
		if (StringUtils.isNotEmpty(currentValue)) {
			DateFormat dateFormat = DateFormat.getInstance();
			try {
				Date currentDate = dateFormat.parse(currentValue);
				fromDateDatebox.setValue(currentDate);
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
			}

		}

		searchPane.appendChild(fromDateDatebox);

		final Label toDateHeading = new Label(LabelUtils.getLabel(widget,
				"customer.to.order.heading", new Object[0]));
		toDateHeading.setSclass("toDateHeading");
		searchPane.appendChild(toDateHeading);

		toDateDatebox = new Datebox();
		toDateDatebox.setSclass("todateDatebox");
		toDateDatebox.setAttribute("size", "21");
		currentValue = getCurrentValue(
				widget,
				MarketplaceDefaultOrderSearchQueryBuilder.TextField.OrderDateEnd);
		if (StringUtils.isNotEmpty(currentValue)) {
			DateFormat dateFormat = DateFormat.getInstance();
			try {
				Date currentDate = dateFormat.parse(currentValue);
				toDateDatebox.setValue(currentDate);
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
			}

		}

		searchPane.appendChild(toDateDatebox);

		toDateDatebox.addEventListener(Events.ON_CHANGE, new EventListener() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				validateDate();

			}
		});

		orderStatus = createSearchDropdownField(widget, searchPane,
				"order.status",
				MarketplaceDefaultOrderSearchQueryBuilder.OrderStatuses.class,
				"orderStatus", null);

		deliveryMode = createSearchDropdownField(widget, searchPane,
				"delivery.mode",
				MarketplaceDefaultOrderSearchQueryBuilder.DeliveryModes.class,
				"deliveryModes", null);

		salesApplications = createSearchDropdownField(
				widget,
				searchPane,
				"sales.application",
				MarketplaceDefaultOrderSearchQueryBuilder.SalesApplications.class,
				"salesApplication", null);

		Button searchBtn = createSearchButton(widget, searchPane);
		attachSearchEventListener(widget, createSearchEventListener(widget),
				new AbstractComponent[] { searchBtn });

		String countText = createCountField(widget);
		if (StringUtils.isNotEmpty(countText)) {
			Label countLabel = new Label(countText);
			countLabel.setSclass("countLabel");
			searchPane.appendChild(countLabel);
		}

		return searchPane;
	}

	private Date getLastThresholdDate() {
		int thresholddays = configurationService.getConfiguration().getInt(
				"cscockpit.order.search.date.threshold")
				* -1;
		Calendar calender = Calendar.getInstance();
		calender.add(Calendar.DAY_OF_YEAR, thresholddays);
		return calender.getTime();

	}

	protected String getCurrentValue(ListboxWidget widget, Enum currentValueKey) {
		return (((TextSearchWidgetModel) widget.getWidgetModel())
				.getSearchCommand() == null) ? ""
				: ((TextSearchWidgetModel) widget.getWidgetModel())
						.getSearchCommand().getText(currentValueKey, "");
	}

	private String createCountField(ListboxWidget widget) {

		if (widget.getWidgetModel() != null
				&& CollectionUtils.isNotEmpty(((TextSearchWidgetModel) widget
						.getWidgetModel()).getItems())) {
			int currentPage = ((TextSearchWidgetModel) widget.getWidgetModel())
					.getCurrentPageNumber();
			currentPage++;
			int pageSize = ((TextSearchWidgetModel) widget.getWidgetModel())
					.getPageSize();
			long totalResults = ((TextSearchWidgetModel) widget
					.getWidgetModel()).getTotalResultsCount();
			int upperCount = currentPage * pageSize > totalResults ? (int) totalResults
					: currentPage * pageSize;
			int lowerCount = ((currentPage - 1) * pageSize) + 1;

			String result = LabelUtils.getLabel(widget, "result.count",
					new Object[] { lowerCount, upperCount, totalResults });
			return result;
		}
		return StringUtils.EMPTY;
	}

	protected Textbox createOrderSearchField(ListboxWidget widget, Div parent,
			String id) {
		return createSearchTextField(
				widget,
				parent,
				id,
				de.hybris.platform.cscockpit.services.search.generic.query.DefaultOrderSearchQueryBuilder.TextField.OrderId,
				"SearchForOrders_OrderIDCustomerName_input");
	}

	protected EventListener createSearchEventListener(ListboxWidget widget) {
		return new SearchEventListener(widget);
	}

	private final void validateDate() throws InterruptedException {
		Date toDate = toDateDatebox.getValue();
		Date fromDate = fromDateDatebox.getValue();
		if (toDate != null && fromDate != null && toDate.before(fromDate)) {

			Messagebox.show(
					Labels.getLabel("general.error.fromdate.after.toDate"),
					"validation failed", Messagebox.OK, Messagebox.ERROR);
		}
		if (getLastThresholdDate() != null && toDate != null
				&& toDate.before(getLastThresholdDate())) {
			Messagebox
					.show(Labels
							.getLabel(
									"general.threshold.date.error",
									new Object[] { configurationService
											.getConfiguration()
											.getInt("cscockpit.order.search.date.threshold") }),
							"validation failed", Messagebox.OK,
							Messagebox.ERROR);
		}
	}

	protected class SearchEventListener extends
			AbstractSearcherWidgetRenderer.AbstractSearchEventListener {

		public SearchEventListener(ListboxWidget widget) {
			super(widget);

		}

		@Override
		protected void fillSearchCommand(DefaultCsTextSearchCommand command) {
			

				try {
					validateDate();
				} catch (InterruptedException e1) {
					LOG.error(e1);
				}

				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.OrderId,
						orderNumber.getText());

				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.CustomerFirstName,
						customerFirstName.getText());
				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.CustomerLastName,
						customerLastName.getText());
				DateFormat dateFormat = DateFormat.getInstance();
				String fromDateValue = fromDateDatebox.getValue() != null ? dateFormat
						.format(fromDateDatebox.getValue()) : null;
				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.OrderDateInit,
						fromDateValue);
				String toDateValue = toDateDatebox.getValue() != null ? dateFormat
						.format(toDateDatebox.getValue()) : null;
				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.OrderDateEnd,
						toDateValue);
				Listitem selectedItem = orderStatus.getSelectedItem() != null ? orderStatus
						.getSelectedItem() : orderStatus.getItemAtIndex(0);

				if ((StringUtils.isNotBlank(toDateValue) && StringUtils
						.isBlank(fromDateValue))
						|| (StringUtils.isBlank(toDateValue) && StringUtils
								.isNotBlank(fromDateValue))) {

					try {
						Messagebox
								.show(Labels
										.getLabel("general.error.fromdate.toDate.missing"),
										"Validation failed", Messagebox.OK,
										Messagebox.ERROR);
					} catch (InterruptedException e) {
						LOG.error(e);
					}

				}

				command.addFlag(getSelectedValue(
						MarketplaceDefaultOrderSearchQueryBuilder.OrderStatuses.class,
						orderStatus));
				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.CustomerEmail,
						customerEmailId.getText());

				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.CustomerMobile,
						customerMobileNumber.getText());

				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.SearchableOrderStatuses,
						(String) selectedItem.getValue());

				selectedItem = deliveryMode.getSelectedItem() != null ? deliveryMode
						.getSelectedItem() : deliveryMode.getItemAtIndex(0);
				command.addFlag(getSelectedValue(
						MarketplaceDefaultOrderSearchQueryBuilder.DeliveryModes.class,
						deliveryMode));
				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.DeliveryMode,
						(String) selectedItem.getValue());

				selectedItem = salesApplications.getSelectedItem() != null ? salesApplications
						.getSelectedItem() : salesApplications
						.getItemAtIndex(0);
				command.addFlag(getSelectedValue(
						MarketplaceDefaultOrderSearchQueryBuilder.SalesApplications.class,
						salesApplications));

				command.setText(
						MarketplaceDefaultOrderSearchQueryBuilder.TextField.SalesApplication,
						(String) selectedItem.getValue());

				if (!validateUI(command)) {
				try {
					Messagebox.show(Labels
							.getLabel("general.error.search.data.missing"),
							"Validation failed", Messagebox.OK,
							Messagebox.ERROR);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
				return;
			}
		}

	}

	protected <E extends Enum<E>> E getSelectedValue(Class<E> enumType,
			Listbox listbox) {
		if (listbox != null) {
			Listitem selectedItem = listbox.getSelectedItem();
			if (selectedItem != null) {
				String valueText = (String) selectedItem.getValue();
				if ((valueText != null) && (!(valueText.isEmpty()))) {
					return Enum.valueOf(enumType, valueText);
				}
			}
		}
		return null;
	}

	private boolean validateUI(DefaultCsTextSearchCommand command) {
		String orderId = command.getText(TextField.OrderId);
		String customerFirstName = command.getText(TextField.CustomerFirstName);
		String customerLastName = command.getText(TextField.CustomerLastName);
		final String orderDateInit = command.getText(TextField.OrderDateInit);
		final String orderDateEnd = command.getText(TextField.OrderDateEnd);
		final String mobileNumber = command.getText(TextField.CustomerMobile);
		final String emailId = command.getText(TextField.CustomerEmail);
		final String orderStatus = command
				.getText(TextField.SearchableOrderStatuses) != null ? command
				.getText(TextField.SearchableOrderStatuses) : "";
		final String deliveryMode = command.getText(TextField.DeliveryMode);
		final String salesApplication = command
				.getText(TextField.SalesApplication);
		
		boolean searchOrderId = StringUtils.isNotEmpty(orderId);
		boolean searchCustomerFirstName = StringUtils
				.isNotEmpty(customerFirstName);
		boolean searchCustomerLastName = StringUtils
				.isNotEmpty(customerLastName);
		final boolean searchOrderDateInit = StringUtils
				.isNotEmpty(orderDateInit);
		final boolean searchOrderDateEnd = StringUtils.isNotEmpty(orderDateEnd);
		final boolean searchOrderStatus = StringUtils.isNotEmpty(orderStatus)
				&& !StringUtils.equalsIgnoreCase(orderStatus,
						EMPTY_STRING_CONSTANT);
		final boolean searchdeliveryMode = StringUtils.isNotEmpty(deliveryMode)
				&& !StringUtils.equalsIgnoreCase(deliveryMode,
						EMPTY_STRING_CONSTANT) ;
		final boolean searchSalesApplication = StringUtils
				.isNotEmpty(salesApplication) && !StringUtils.equalsIgnoreCase(salesApplication,
						EMPTY_STRING_CONSTANT);
		final boolean searchMobileNumer = StringUtils.isNotEmpty(mobileNumber);
		final boolean searchEmailId = StringUtils.isNotEmpty(emailId);

		if (searchOrderId || searchCustomerFirstName || searchCustomerLastName
				|| searchOrderDateInit || searchOrderDateEnd
				|| searchOrderStatus || searchdeliveryMode
				|| searchSalesApplication || searchMobileNumer || searchEmailId) {
			return true;
		}
		return false;
	}
}
