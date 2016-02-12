/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceCancellationController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl.MarketPlaceDefaultCancellationController;

import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.CockpitUiConfigLoader;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.SafeUnbox;
import de.hybris.platform.cscockpit.widgets.controllers.CancellationController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.PartialOrderCancellationWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.edit.PojoPropertyRendererUtil;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

/**
 * @author 1006687
 *
 */
public class MarketPlacePartialOrderCancellationWidgetRenderer extends
		PartialOrderCancellationWidgetRenderer {

	List<ObjectValueContainer> cancellableEnteries = new ArrayList<>();

	protected org.zkoss.zk.ui.api.HtmlBasedComponent createContentInternal(
			InputWidget<DefaultListWidgetModel<TypedObject>, CancellationController> widget,
			org.zkoss.zk.ui.api.HtmlBasedComponent rootContainer) {
		this.cancellableEnteries = new ArrayList<>();
		return super.createContentInternal(widget, rootContainer);
	}

	protected ObjectValueContainer createOrderCancelRequestEditors(
			InputWidget<DefaultListWidgetModel<TypedObject>, CancellationController> widget,
			org.zkoss.zk.ui.HtmlBasedComponent content) {
		List propertyDescriptors = getOrderCancelRequestProperties();
		if ((propertyDescriptors != null) && (!(propertyDescriptors.isEmpty()))) {
			ObjectValueContainer orderCancelRequestObjectValues = buildObjectValueContainer(
					null, propertyDescriptors, new HashSet<String>());

			Map editorMapping = getOrderCancelRequestPropertyEditorRowConfigurations();
			if ((editorMapping != null) && (!(editorMapping.isEmpty()))) {
				Iterator localIterator = propertyDescriptors.iterator();
				while (true) {
					PropertyDescriptor propertyDescriptor = (PropertyDescriptor) localIterator
							.next();

					Div editorDiv = new Div();
					editorDiv.setSclass("editorWidgetEditor");
					editorDiv.setVisible(false);
					PojoPropertyRendererUtil.renderEditor(editorDiv,
							propertyDescriptor,
							(EditorRowConfiguration) editorMapping
									.get(propertyDescriptor), null,
							orderCancelRequestObjectValues, widget, true);

					if (!(editorDiv.getChildren().isEmpty())) {
						content.appendChild(editorDiv);
					}
					if (!(localIterator.hasNext())) {
						return orderCancelRequestObjectValues;
					}
				}
			}
		}
		return null;
	}

	protected void handleAttemptCancellationEvent(
			final InputWidget<DefaultListWidgetModel<TypedObject>, CancellationController> widget,
			Event event, final ObjectValueContainer orderCancelRequestOVC,
			List<ObjectValueContainer> orderEntryCancelRecordEntries)
			throws Exception {

		if (!("onOK".equals(event.getName())))
			return;
		

			if (((MarketPlaceCancellationController) widget
					.getWidgetController())
					.isFreebieAvaialble(cancellableEnteries)) {
				Messagebox.show(LabelUtils.getLabel(widget, "freeBieAvaialble",
						new Object[0]), LabelUtils.getLabel(widget,
						"freeBieAvaialbleTitle", new Object[0]), Messagebox.OK
						| Messagebox.CANCEL, Messagebox.INFORMATION,
						new EventListener() {

							@Override
							public void onEvent(Event event) throws Exception {
								if(!event.getName().equals("onOK")){
									return;
								}else{
									proccedCancellation(widget, orderCancelRequestOVC);
								}
							}
						});
			}else{
				proccedCancellation(widget, orderCancelRequestOVC);
			}
			

	}

	private void proccedCancellation(
			InputWidget<DefaultListWidgetModel<TypedObject>, CancellationController> widget,
			ObjectValueContainer orderCancelRequestOVC)
			throws InterruptedException {
		try {
		TypedObject cancellationRequest = ((MarketPlaceCancellationController) widget
				.getWidgetController())
				.createPartialOrderCancellationRequest(cancellableEnteries,
						orderCancelRequestOVC);
		if (cancellationRequest != null) {
			getPopupWidgetHelper().dismissCurrentPopup();

			OrderCancelRecordEntryModel orderCancelRecordEntryModel = (OrderCancelRecordEntryModel) cancellationRequest
					.getObject();
			if (orderCancelRecordEntryModel.getStatus().equals(
					OrderModificationEntryStatus.SUCCESSFULL)) {
				Messagebox.show(LabelUtils.getLabel(
						widget,
						"cancellationNumber",
						new Object[] {
								orderCancelRecordEntryModel
										.getTransactionCode(),
								orderCancelRecordEntryModel
										.getRefundableAmount() }),
						LabelUtils.getLabel(widget,
								"cancellationNumberTitle", new Object[0]),
						1, "z-msgbox z-msgbox-information");
			} else {
				// Messagebox.show(LabelUtils.getLabel(widget,
				// "errorCreatingRequest", new Object[0]), LabelUtils
				// .getLabel(widget, "refund.failed",
				// new Object[] { orderCancelRecordEntryModel
				// .getCode() }), 1,
				// "z-msgbox z-msgbox-error");
				//
				//

				Messagebox.show(LabelUtils.getLabel(widget,
						"refund.failed",
						new Object[] { orderCancelRecordEntryModel
								.getCode() }),
						MarketplaceCockpitsConstants.ERROR, Messagebox.OK,
						Messagebox.ERROR);

			}
			((CancellationController) widget.getWidgetController())
					.dispatchEvent(null, widget, null);
			return;
		}

		Messagebox.show(LabelUtils.getLabel(widget, "errorCreatingRequest",
				new Object[0]), LabelUtils.getLabel(widget, "failed",
				new Object[0]), 1, "z-msgbox z-msgbox-error");

} catch (OrderCancelException e) {
		Messagebox.show(e.getMessage()
				+ ((e.getCause() == null) ? "" : new StringBuilder(" - ")
						.append(e.getCause().getMessage()).toString()),
				LabelUtils.getLabel(widget, "failedToValidate",
						new Object[0]), 1, "z-msgbox z-msgbox-error");
} catch (ValidationException e) {
		Messagebox.show(e.getMessage()
				+ ((e.getCause() == null) ? "" : new StringBuilder(" - ")
						.append(e.getCause().getMessage()).toString()),
				LabelUtils.getLabel(widget, "failedToValidate",
						new Object[0]), 1, "z-msgbox z-msgbox-error");
}
	}

	protected void renderCancelableOrderEntryHeaders(
			InputWidget<DefaultListWidgetModel<TypedObject>, CancellationController> widget,
			Listhead parent, List<PropertyDescriptor> columns) {
		parent.setSclass(CssUtils.combine(new String[] { parent.getSclass(),
				"cancelableOrderEntryHeaders" }));
		Listheader colEntryNoHeader = new Listheader(LabelUtils.getLabel(
				widget, "entryNumber", new Object[0]));
		colEntryNoHeader.setWidth("20px");
		colEntryNoHeader.setParent(parent);

		Listheader colTransactionIdHeader = new Listheader(LabelUtils.getLabel(
				widget, "transactionId", new Object[0]));
		// colTransactionIdHeader.setWidth("20px");
		colTransactionIdHeader.setParent(parent);

		Listheader cell = new Listheader();
		cell.setSclass("productDetailsCell");
		cell.setParent(parent);
		Label productDescriptionLabel = new Label(LabelUtils.getLabel(widget,
				"productDescription", new Object[0]));
		productDescriptionLabel.setParent(cell);

		/*
		 * Listheader colUSSIDHeader = new
		 * Listheader(LabelUtils.getLabel(widget, "USSID", new Object[0])); //
		 * colUSSIDHeader.setWidth("20px"); colUSSIDHeader.setParent(parent);
		 */

		Listheader colSellerHeader = new Listheader(LabelUtils.getLabel(widget,
				"seller", new Object[0]));
		// colSellerHeader.setWidth("20px");
		colSellerHeader.setParent(parent);

		// cell = new Listheader();
		// cell.setSclass("maxCancellationQtyCell");
		// cell.setParent(parent);
		// Label maxQuantityLabel = new Label(LabelUtils.getLabel(widget,
		// "maxQuantityLabel", new Object[0]));
		// maxQuantityLabel.setParent(cell);

		for (PropertyDescriptor propertyDescriptor : columns) {
			Listheader colHeader = new Listheader(getPropertyRendererHelper()
					.getPropertyDescriptorName(propertyDescriptor));
			colHeader.setParent(parent);
			colHeader.setTooltiptext(propertyDescriptor.getQualifier());
		}
	}

	protected void renderCancelableOrderEntry(
			InputWidget<DefaultListWidgetModel<TypedObject>, CancellationController> widget,
			Listitem parent, TypedObject orderEntry, Long cancelableAmount,
			List<PropertyDescriptor> columns,
			Map<PropertyDescriptor, EditorRowConfiguration> editorMapping) {
		if ((orderEntry == null)
				|| (!(orderEntry.getObject() instanceof OrderEntryModel)))
			return;
		OrderEntryModel orderEntryModel = (OrderEntryModel) orderEntry
				.getObject();
		long cancelableAmountLong = SafeUnbox.toLong(cancelableAmount);
		if (cancelableAmountLong <= 0L)
			return;
		Listcell entryNoCell = new Listcell();
		entryNoCell.setParent(parent);
		Div entryNoDiv = new Div();
		entryNoDiv.setParent(entryNoCell);
		entryNoDiv.setSclass("editorWidgetEditor");
		Label entryNoLabel = new Label(String.valueOf(orderEntryModel
				.getEntryNumber()));
		entryNoLabel.setParent(entryNoDiv);

		Listcell transactionIDCell = new Listcell();
		transactionIDCell.setParent(parent);
		Div transactionIdDiv = new Div();
		transactionIdDiv.setParent(transactionIDCell);
		transactionIdDiv.setSclass("editorWidgetEditor");
		Label transactionIdLabel = new Label(String.valueOf(orderEntryModel
				.getTransactionID()));
		transactionIdLabel.setParent(transactionIdDiv);

		Listcell cell = new Listcell();
		cell.setSclass("productDetailsCell");
		cell.setParent(parent);
		getPropertyRendererHelper()
				.buildPropertyValuesFromColumnConfigs(
						orderEntry,
						CockpitUiConfigLoader
								.getDirectVisibleColumnConfigurations(CockpitUiConfigLoader.getColumnGroupConfiguration(
										UISessionUtils.getCurrentSession(),
										getProductInfoConfigurationCode(),
										orderEntry.getType().getCode())), cell);

		/*
		 * Listcell ussidCell = new Listcell(); ussidCell.setParent(parent); Div
		 * ussidDiv = new Div(); ussidDiv.setParent(ussidCell);
		 * ussidDiv.setSclass("editorWidgetEditor"); Label ussidLabel = new
		 * Label(String.valueOf(orderEntryModel .getSelectedUSSID()));
		 * ussidLabel.setParent(ussidDiv);
		 */
		Listcell sellerCell = new Listcell();
		sellerCell.setParent(parent);
		Div sellerDiv = new Div();
		sellerDiv.setParent(sellerCell);
		sellerDiv.setSclass("editorWidgetEditor");

		Label sellerLabel = new Label(
				((MarketPlaceDefaultCancellationController) widget
						.getWidgetController()).getSellerName(orderEntryModel
						.getSelectedUSSID()));
		sellerLabel.setParent(sellerDiv);

		// cell = new Listcell();
		// cell.setSclass("maxCancellationQtyCell");
		// cell.setParent(parent);
		// Label maxQuantityLabel = new Label(String.valueOf(cancelableAmount));
		// maxQuantityLabel.setParent(cell);

		ObjectValueContainer orderCancelEntryObjectValueContainer = super
				.buildOrderEntryCancelRecordEntryObjectValueContainer(
						orderEntry, null, columns, Collections.EMPTY_SET);
		this.cancellableEnteries.add(orderCancelEntryObjectValueContainer);

		for (PropertyDescriptor propertyDescriptor : columns) {
			cell = new Listcell();
			cell.setParent(parent);
			Div editorDiv = new Div();
			editorDiv.setParent(cell);
			editorDiv.setSclass("editorWidgetEditor");
			PojoPropertyRendererUtil.renderEditor(editorDiv,
					propertyDescriptor, (EditorRowConfiguration) editorMapping
							.get(propertyDescriptor), null,
					orderCancelEntryObjectValueContainer, widget, false);
		}
	}

}
