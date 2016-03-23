package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultItemWidgetModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderManagementActionsWidgetRenderer;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

public class MarketPlaceOrderManagementActionsWidgetRenderer extends
		OrderManagementActionsWidgetRenderer {

	static Logger log = Logger
			.getLogger(MarketPlaceOrderManagementActionsWidgetRenderer.class
					.getName());
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

	public TypedObject getOrder()
	/*     */{
		/* 80 */return getCallContextController().getCurrentOrder();
		/*     */}

	@Override
	protected HtmlBasedComponent createContentInternal(
			Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
			HtmlBasedComponent rootContainer) {

		HtmlBasedComponent component = createRequiredButtons(widget,
				rootContainer);

		if (isUserInRole(configurationService.getConfiguration().getString(
				"cscockpit.user.group.sendinvcsagentgroup"))) {
			createButton(widget, (Div) component, "requestInvoice",
					"csInvoiceRequestCreateWidgetConfig",
					"invoicerequest-popup", "invoiceRequest",
					"invoice.request", !isInvoiceAvailable(widget
							.getWidgetController().getOrder()));
		}

		// Added for CNC button
		if (isUserInRole(configurationService
				.getConfiguration()
				.getString(
						MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_ALTERNATECONTACTCSAGENTGROUP))) {

			log.info("^^^^^^^^^in new button^^^^^^^^^^^^^^^");
			createButton(widget, (Div) component, "alternateContactDetails",
					"csAlternateContactDetailsCreateWidgetConfig",
					"alternateContactDetails-popup", "alternateContactDetails",
					"alternateContactDetails.request", !isCnCAvailable(widget
							.getWidgetController().getOrder()));
		}

		if (isUserInRole(configurationService
				.getConfiguration()
				.getString(
						MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_REFUNDCSAGENTGROUP))) {
			createButton(widget, (Div) component, "makePayment",
					"csMakeManualRefundCreateWidgetConfig",
					"makePayment-popup", "makePayment", "refund.request", false);
		}

		if (isUserInRole(configurationService.getConfiguration().getString(
				"cscockpit.user.group.refunddelcsagentgroup"))) {

			createButton(widget, (Div) component, "refundDeliveryCharge",
					"csRefundDeliveryChargeWidgetConfig",
					"refundDeliveryCharge-popup", "refundDeliveryCharge",
					"refunddeliverycharge.request", false);
		}
		return component;
	}

	protected boolean isInvoiceAvailable(TypedObject orderObject) {
		if (orderObject != null && orderObject.getObject() != null
				&& orderObject.getObject() instanceof AbstractOrderModel) {
			AbstractOrderModel orderModel = (AbstractOrderModel) orderObject
					.getObject();
			int thresholdTime = configurationService.getConfiguration().getInt(
					"cscockpit.order.invoice.threshold.months")
					* -1;
			Date creationDate = orderModel.getCreationtime();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, thresholdTime);
			boolean isAfter = creationDate.after(cal.getTime());

			boolean isInvoiceAvaialble = false;

			for (AbstractOrderEntryModel entry : orderModel.getEntries()) {
				Set<ConsignmentEntryModel> entries = new HashSet<>(
						entry.getConsignmentEntries());
				try{
				if (CollectionUtils.isNotEmpty(entries)
						&& MarketplaceCockpitsConstants.validInvoiceStatus
								.contains(entries.iterator().next()
										.getConsignment().getStatus())) {
					isInvoiceAvaialble = true;
					break;
				}
				}catch(Exception e) {
					Log.debug("Entries null"+ e);
				}
			}
			return isInvoiceAvaialble && isAfter;
		}
		return false;
	}

	// Added for to test whether cnc product is present or not

	protected boolean isCnCAvailable(TypedObject orderObject) {
		AbstractOrderModel orderModel = (AbstractOrderModel) orderObject
				.getObject();

		Boolean isCnCAvailable = Boolean.FALSE;
		for (AbstractOrderEntryModel entry : orderModel.getEntries()) {
			if (entry.getMplDeliveryMode() != null
					&& entry.getMplDeliveryMode().getDeliveryMode() != null) {

				String orderStatus = entry.getOrder().getStatus().getCode();
				if (entry
						.getMplDeliveryMode()
						.getDeliveryMode()
						.getName()
						.equalsIgnoreCase(
								MarketplaceCockpitsConstants.delNameMap
										.get("CnC"))) {
					
					isCnCAvailable = true;

					orderStatus = orderModel.getStatus().getCode();
					if (CollectionUtils.isNotEmpty(entry
							.getConsignmentEntries())) {
						try{
						ConsignmentStatus consignmentStatus = entry
								.getConsignmentEntries().iterator().next()
								.getConsignment().getStatus();
						orderStatus = consignmentStatus.getCode();
						}catch(Exception e)
						{
							Log.debug("Exception "+e);
						}
					}

					List<String> nonChangableOrdeStatus = Arrays.asList(
							OrderStatus.PAYMENT_FAILED.getCode(),
							OrderStatus.RETURNINITIATED_BY_RTO.getCode(),
					OrderStatus.REFUND_INITIATED.getCode(),
					OrderStatus.RETURN_INITIATED.getCode());
					List<String> nonChangableOrdeStatusList = Arrays.asList(
							ConsignmentStatus.CANCELLATION_INITIATED.getCode(),
							ConsignmentStatus.CANCELLED.getCode(),
							ConsignmentStatus.CLOSED_ON_CANCELLATION.getCode(),
							ConsignmentStatus.CLOSED_ON_RETURN_TO_ORIGIN
									.getCode(),
							ConsignmentStatus.COD_CLOSED_WITHOUT_REFUND
									.getCode(),
							ConsignmentStatus.ORDER_CANCELLED.getCode(),
							ConsignmentStatus.ORDER_COLLECTED.getCode(),
							ConsignmentStatus.ORDER_REJECTED.getCode(),
							ConsignmentStatus.ORDER_UNCOLLECTED.getCode(),
							ConsignmentStatus.QC_FAILED.getCode(),
							ConsignmentStatus.REFUND_IN_PROGRESS.getCode(),
							ConsignmentStatus.REFUND_INITIATED.getCode(),
							ConsignmentStatus.REVERSE_AWB_ASSIGNED.getCode(),
							ConsignmentStatus.RETURN_CANCELLED.getCode(),
							ConsignmentStatus.RETURN_CLOSED.getCode(),
							ConsignmentStatus.RETURN_INITIATED.getCode(),
							ConsignmentStatus.RETURN_RECEIVED.getCode(),
							ConsignmentStatus.RETURN_TO_ORIGIN.getCode(),
							ConsignmentStatus.RETURN_COMPLETED.getCode(),
							ConsignmentStatus.RETURNINITIATED_BY_RTO.getCode());
					
					if (entry.getQuantity() <= 0
							|| nonChangableOrdeStatus.contains(orderStatus.toUpperCase())
							|| nonChangableOrdeStatusList.contains(orderStatus.toUpperCase())) {
						isCnCAvailable = false;

					}

					else {
						return isCnCAvailable;
					}
				}
			}
		}
		return isCnCAvailable;
	}

	protected HtmlBasedComponent createRequiredButtons(
			Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
			HtmlBasedComponent rootContainer) {
		Div component = new Div();
		component.setSclass("orderManagementActionsWidget");

		if (isUserInRole(configurationService
				.getConfiguration()
				.getString(
						MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_CANCELCSAGENTGROUP))) {
			// createButton(widget, component, "cancelWholeOrder",
			// "csFullOrderCancellationWidgetConfig", "csFullOrderCancel-Popup",
			// "csFullCancelPopup", "popup.fullCancellationRequestCreate",
			// !(((OrderManagementActionsWidgetController)widget.getWidgetController()).isFullCancelPossible()));
			createButton(widget, component, "cancelPartialOrder",
					"csPartialOrderCancellationWidgetConfig",
					"csPartialOrderCancellationWidgetConfig-Popup",
					"csPartialCancelPopup",
					"popup.partialCancellationRequestCreate",
					!(((OrderManagementActionsWidgetController) widget
							.getWidgetController()).isPartialCancelPossible()));

		}

		if (isUserInRole(configurationService
				.getConfiguration()
				.getString(
						MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_REFUNDCSAGENTGROUP))) {
			createButton(widget, component, "refundOrder",
					"csRefundRequestCreateWidgetConfig",
					"csRefundRequestCreateWidget-Popup",
					"csReturnRequestCreateWidget", "popup.refundRequestCreate",
					!(((OrderManagementActionsWidgetController) widget
							.getWidgetController()).isRefundPossible()));

		}
		if (isUserInRole(configurationService
				.getConfiguration()
				.getString(
						MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_REPLACECSAGENTGROUP))) {
			createButton(widget, component, "replaceOrder",
					"csReplacementRequestCreateWidgetConfig",
					"csReplacementRequestCreateWidget-Popup",
					"csReturnRequestCreateWidget",
					"popup.replacementRequestCreate",
					!(((OrderManagementActionsWidgetController) widget
							.getWidgetController()).isReplacePossible()));
		}
		/**
		 * Create the Button for Return Request
		 * 
		 **/
		if (isUserInRole(configurationService.getConfiguration().getString(
				"cscockpit.user.group.rejectreturncsagentgroup"))) {
			createButton(widget, (Div) component, "rejectReturn",
					"csReturnRequestCreateWidgetConfig", "returnrequest-popup",
					"returnRequest", "rejectReturnRequest", false);
		}
		return component;
	}

	private boolean isUserInRole(String groupName) {
		Set<PrincipalGroupModel> userGroups = UISessionUtils
				.getCurrentSession().getUser().getAllGroups();
		for (PrincipalGroupModel ug : userGroups) {
			if (ug.getUid().equalsIgnoreCase(groupName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void handleButtonClickEvent(
			Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Event event, Div container, String springWidgetName,
			String popupCode, String cssClass, String popupTitleLabelName) {

		getPopupWidgetHelper()
				.createPopupWidget(
						container,
						springWidgetName,
						popupCode,
						cssClass,
						LabelUtils.getLabel(widget, popupTitleLabelName,
								new Object[0]), 1300);
	}

	@Override
	protected void createButton(Widget widget, Div container,
			String buttonLabelName, EventListener eventListener,
			boolean disabled) {

		OrderManagementActionsWidgetController oc = (OrderManagementActionsWidgetController) widget
				.getWidgetController();
		if (oc.getOrder() != null) {
			OrderModel om = (OrderModel) oc.getOrder().getObject();
			disabled = disabled | om.getParentReference() == null;
		}
		super.createButton(widget, container, buttonLabelName, eventListener,
				disabled);
	}

}