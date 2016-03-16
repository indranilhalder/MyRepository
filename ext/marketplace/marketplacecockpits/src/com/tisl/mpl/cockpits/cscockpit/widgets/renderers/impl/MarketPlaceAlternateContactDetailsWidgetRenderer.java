package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultItemWidgetModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderItemsWidgetRenderer;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hamcrest.text.IsEmptyString;
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
import org.zkoss.zul.Space;
import org.zkoss.zul.Textbox;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.ItemModificationHistoryService;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCallContextController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.account.register.MplOrderFacade;

public class MarketPlaceAlternateContactDetailsWidgetRenderer extends
		OrderDetailsOrderItemsWidgetRenderer {

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceAlternateContactDetailsWidgetRenderer.class);
	private static final String CUSTOMER_DETAILS_UPDATED = "customerdetailsupdated";
	private static final String INFO = "info";
	private static final String FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM = "failedToValidatepickupdetailsForm";
	private Boolean valid = Boolean.TRUE;

	@Autowired
	private FlexibleSearchService flexibleSearhService;
	@Autowired
	private MplOrderFacade mplOrderFacade;

	@Autowired
	private ModelService modelService;
	@Autowired
	private MarketplaceCallContextController marketplaceCallContextController;
	@Autowired
	private ItemModificationHistoryService itemModificationHistoryService;

	@Autowired
	private ConfigurationService configurationService;

	// added
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

	private Hbox createHbox(
			final Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
			final String attributeLabel, final boolean hidden,
			final boolean overwriteWidth) {

		final Hbox hbox = new Hbox();
		hbox.setWidth("36%");
		if (overwriteWidth) {
			hbox.setWidths("9em, none");
		}
		hbox.setAlign("center");

		if (hidden) {
			hbox.setVisible(false);
		}

		hbox.setClass("editorWidgetEditor");
		final Label label = new Label(LabelUtils.getLabel(widget,
				attributeLabel));
		label.setParent(hbox);
		return hbox;

	}// createHbox

	private Textbox createTextbox(final Hbox parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("50%");
		textBox.setParent(parent);
		return textBox;
	}// Textbox

	private Textbox createPrefixTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("12%");
		textBox.setParent(parent);
		textBox.setValue("+91");
		textBox.setDisabled(true);
		return textBox;
	}// createPrefixTextbox

	private Textbox createMobileNumberTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("38%");
		textBox.setParent(parent);
		return textBox;
	}// createMobileNumberTextbox

	protected HtmlBasedComponent createContentInternal(
			Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
			HtmlBasedComponent rootContainer) {

		Div content = new Div();
		TypedObject order = getOrder();
		content.setClass("customerDetails");

		// content.appendChild(createTicketContent(widget));
		try {
			if (valid) {
				final OrderModel ordermodel = (OrderModel) order.getObject();
				OrderModel mainOrder = ordermodel.getParentReference();
				ordermodel.getStatusDisplay();
				ordermodel.getItemModelContext().getSource();
				final Hbox pickupNameHbox = createHbox(widget, "PickupName",
						false, true);
				final Textbox pickupNameFieldTextBox = createTextbox(pickupNameHbox);
				try {
					if (ordermodel.getPickupPersonName() != null) {
						pickupNameFieldTextBox.setValue(mainOrder
								.getPickupPersonName());
						pickupNameFieldTextBox
								.setSclass("pickupNameFieldTextBox");
						String errorMsgName = LabelUtils.getLabel(widget,
								"error.msg.name", new Object[0]);
						/*pickupNameFieldTextBox.setConstraint("/[a-zA-Z]*$/:"
								+ errorMsgName);*/
						//pickupNameFieldTextBox.setConstraint("/[a-zA-Z, ]*$/:"+ errorMsgName);
						pickupNameFieldTextBox.setMaxlength(30);
						pickupNameFieldTextBox.setConstraint("/^[a-zA-Z]+[ ]?[a-zA-Z]*$/:"+ errorMsgName);
						
					}// if
				}// try
				catch (final Exception e) {
					LOG.error("Pickup Name is null", e);
				}// catch
				content.appendChild(pickupNameHbox);

				final Hbox pickupPhoneHbox = createHbox(widget,
						"pickupPersonMobile", false, true);
				final Div phoneNumberRowDiv = new Div();
				phoneNumberRowDiv.setParent(pickupPhoneHbox);
				createPrefixTextbox(phoneNumberRowDiv);
				final Textbox pickupPhoneFieldTextBox = createMobileNumberTextbox(phoneNumberRowDiv);
				try {
					if (ordermodel.getPickupPersonMobile() != null) {
						pickupPhoneFieldTextBox.setValue(mainOrder
								.getPickupPersonMobile());
						pickupPhoneFieldTextBox
								.setSclass("pickupNameFieldTextBox");
						pickupPhoneFieldTextBox.setMaxlength(10);
						String errorMsgMobile = LabelUtils.getLabel(widget,
								"error.msg.mobile", new Object[0]);
						pickupPhoneFieldTextBox.setConstraint("/[0-9]*$/:"
								+ errorMsgMobile);
					}// if
				}// try
				catch (final Exception e) {
					LOG.error("Pick Phone Name is null", e);
				}// catch
				content.appendChild(pickupPhoneHbox);

				final Div actionContainer = new Div();
				actionContainer.setSclass("updateBtn");
				actionContainer.setParent(content);
				// Update button
				final Button update = new Button(LabelUtils.getLabel(widget,
						"Update", new Object[0]));
				update.setParent(content);
				actionContainer.setAlign("center");
				update.addEventListener(
						Events.ON_CLICK,
						createUpdateDetailsEventListener(widget, ordermodel,
								mainOrder, pickupNameFieldTextBox,
								pickupPhoneFieldTextBox));
			}// if

		}// try
		catch (final Exception e) {
			LOG.error("unable to render alternate contacts widget", e);
		}// catch

		return content;

	}// createContentInternal

	/**
	 * 
	 * @param widget
	 * @param orderModel
	 * @param pickupNameFieldTextBox
	 * @param pickupPhoneFieldTextBox
	 * @return
	 */
	private EventListener createUpdateDetailsEventListener(
			Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
			final OrderModel orderModel, final OrderModel mainOrder,
			final Textbox pickupNameFieldTextBox,
			final Textbox pickupPhoneFieldTextBox) {
		return new UpdateDetailsEventListener(widget, orderModel, mainOrder,
				pickupNameFieldTextBox, pickupPhoneFieldTextBox);
	}// createUpdateDetailsEventListener

	protected class UpdateDetailsEventListener implements EventListener {
		final Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget;
		private static final String PICKUP_PHONE_REGEX = "^[0-9]{10}";
		private final OrderModel orderModel;
		private final OrderModel mainOrder;
		private final Textbox pickupNameFieldTextBox;
		private final Textbox pickupPhoneFieldTextBox;

		public UpdateDetailsEventListener(

				final Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
				final OrderModel orderModel, final OrderModel mainOrder,
				final Textbox pickupNameFieldTextBox,
				final Textbox pickupPhoneFieldTextBox) {
			this.widget = widget;
			this.orderModel = orderModel;
			this.mainOrder = mainOrder;
			this.pickupNameFieldTextBox = pickupNameFieldTextBox;
			this.pickupPhoneFieldTextBox = pickupPhoneFieldTextBox;
		}// UpdateDetailsEventListener()

		@Override
		public void onEvent(final Event event) throws InterruptedException,
				ParseException {
			try {
				boolean updateSuccess = handleUpdateDetails(widget, orderModel, mainOrder,
						pickupNameFieldTextBox, pickupPhoneFieldTextBox);
                if(updateSuccess) {
				TypedObject customer = marketplaceCallContextController
						.getCurrentCustomer();
				CustomerModel customermodel = (CustomerModel) customer
						.getObject();
				String customerId = customermodel.getUid();
				// Added for ticket creation
				((MarketplaceCallContextController) widget
						.getWidgetController()).crmTicketGeneration(mainOrder,
						customerId, MarketplaceCockpitsConstants.SOURCE);

				LOG.debug("Pick Up Details Store In CRM successfully");
				Messagebox.show(LabelUtils.getLabel(widget,
						CUSTOMER_DETAILS_UPDATED, new Object[0]), INFO,
						Messagebox.OK, Messagebox.INFORMATION);
                }
			} catch (final Exception e) {
				LOG.error("unable to render listner", e);
			}
		}// onEvent

		private boolean handleUpdateDetails(
				final Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
				final OrderModel order, final OrderModel mainOrder,
				final Textbox pickupNameFieldTextBox,
				final Textbox pickupPhoneFieldTextBox)
				throws InterruptedException, ParseException {

			if (StringUtils.isBlank(pickupNameFieldTextBox.getValue())
					|| StringUtils.isBlank(pickupNameFieldTextBox.getValue()
							.trim())) {
				Messagebox.show(LabelUtils.getLabel(widget,
						"pickupNameFieldvalue"), LabelUtils.getLabel(widget,
						FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM), Messagebox.OK,
						Messagebox.ERROR);
				return false;
			}
			
			 else if (pickupNameFieldTextBox.getValue().startsWith(" ")) {
					Messagebox.show(LabelUtils.getLabel(widget,
							"Doesn't_start_with_space"), LabelUtils.getLabel(widget,
							FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM), Messagebox.OK,
							Messagebox.ERROR);
					return false;
				}

			else if (pickupNameFieldTextBox.getValue().length() > 225) {
				Messagebox.show(LabelUtils.getLabel(widget,
						"invalidpickupNameLength"), LabelUtils.getLabel(widget,
						FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM), Messagebox.OK,
						Messagebox.ERROR);
				return false;
			}

			else if (StringUtils.isBlank(pickupPhoneFieldTextBox.getValue())
					|| StringUtils.isBlank(pickupPhoneFieldTextBox.getValue()
							.trim())) {
				Messagebox.show(LabelUtils.getLabel(widget,
						"pickupPhoneValueField"), LabelUtils.getLabel(widget,
						FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM), Messagebox.OK,
						Messagebox.ERROR);

				return false;
			} else if (pickupPhoneFieldTextBox.getValue().length() > 10) {
				Messagebox.show(LabelUtils.getLabel(widget,
						"invalidPhoneLength"), LabelUtils.getLabel(widget,
						FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM), Messagebox.OK,
						Messagebox.ERROR);
				return false;
			} else if (!(pickupPhoneFieldTextBox.getValue()
					.matches(PICKUP_PHONE_REGEX))) {

				Messagebox.show(LabelUtils.getLabel(widget,
						"mobileNumberValueIncorrect"), LabelUtils.getLabel(
						widget, FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM),
						Messagebox.OK, Messagebox.ERROR);
				return false;
			}

			else {
				valid = true;
			}

			if (valid) {

				final String changedPickupName = pickupNameFieldTextBox
						.getValue();
				final String changedPickupPhone = pickupPhoneFieldTextBox
						.getValue();

				boolean error = false;

				if (!error) {

					if (changedPickupName != null
							&& !changedPickupName.isEmpty()) {
						order.setPickupPersonName(changedPickupName);
						mainOrder.setPickupPersonName(changedPickupName);
					} else {
						orderModel
								.setPickupPersonName(MarketplacecommerceservicesConstants.EMPTY);

					}

					if (changedPickupPhone != null
							&& !changedPickupPhone.isEmpty()) {
						order.setPickupPersonMobile(changedPickupPhone);
						mainOrder.setPickupPersonMobile(changedPickupPhone);
					} else {
						orderModel
								.setPickupPersonMobile(MarketplacecommerceservicesConstants.EMPTY);
					}

					itemModificationHistoryService
							.logItemModification(itemModificationHistoryService
									.createModificationInfo(order));
					modelService.save(order);
					modelService.save(mainOrder);
					
					/*Messagebox.show(LabelUtils.getLabel(widget,
							CUSTOMER_DETAILS_UPDATED, new Object[0]), INFO,
							Messagebox.OK, Messagebox.INFORMATION);*/

				}// if(!error)
				return valid;
			}// if(valid)
			return valid;

		}// handleUpdateDetails

	}// class UpdateDetailsEventListener()

}// class MarketPlaceAlternateContactDetailsWidgetRenderer