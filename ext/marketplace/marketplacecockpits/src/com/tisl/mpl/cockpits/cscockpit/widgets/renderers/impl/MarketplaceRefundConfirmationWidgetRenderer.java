package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.models.ListWidgetModel;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.utils.SafeUnbox;
import de.hybris.platform.cscockpit.widgets.controllers.ReturnsController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.RefundConfirmationWidgetRenderer;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;

public class MarketplaceRefundConfirmationWidgetRenderer extends
		RefundConfirmationWidgetRenderer {

	private static final Logger LOG = Logger.getLogger(MarketplaceRefundConfirmationWidgetRenderer.class);

	protected HtmlBasedComponent createContentInternal(ListboxWidget widget,
			HtmlBasedComponent rootContainer) {
		Div container = new Div();
		container.setSclass("refundConfirmationWidget");
		Div originalOrderContent = new Div();
		originalOrderContent.setParent(container);
		OrderModel originalOrderModel = (OrderModel) ((ReturnsController) widget
				.getWidgetController()).getCurrentOrder().getObject();
		final CurrencyModel currencyModel = originalOrderModel.getCurrency();
		NumberFormat currencyInstance = (NumberFormat) getSessionService()
				.executeInLocalView(new SessionExecutionBody() {
					public Object execute() {
						getCommonI18NService()
								.setCurrentCurrency(currencyModel);
						return getFormatFactory().createCurrencyFormat();
					}
				});
		Set<String> cancelId = new HashSet<>();
		double originalTotal = SafeUnbox.toDouble(originalOrderModel
				.getTotalPriceWithConv());
		String orderTotalText = LabelUtils.getLabel(widget,
				"originalOrderTotal",
				new Object[] { currencyInstance.format(originalTotal) });
		Label orderTotal = new Label(orderTotalText);
		orderTotal.setParent(originalOrderContent);

		HtmlBasedComponent modifiedOrderDetails = createContentInternalNew(
				widget, rootContainer);
		String modifiedOrderLabelText = LabelUtils.getLabel(widget,
				"modifiedOrder", new Object[0]);
		Label modifiedOrderLabel = new Label(modifiedOrderLabelText);
		container.appendChild(modifiedOrderLabel);
		container.appendChild(modifiedOrderDetails);

		TypedObject refundOrder = ((ReturnsController) widget
				.getWidgetController()).getRefundOrderPreview();
		for (AbstractOrderEntryModel e : originalOrderModel.getEntries()){
			if(e.getQuantity() <=0L){
				cancelId.add(e.getTransactionID());
			}
		}
		if (refundOrder != null) {
			Div refundOrderContent = new Div();
			refundOrderContent.setParent(container);
			double newTotal = SafeUnbox.toDouble(((OrderModel) refundOrder
					.getObject()).getTotalPrice());

			double refundTotal = Double.valueOf(0);
			List<AbstractOrderEntryModel> entry = (List<AbstractOrderEntryModel>) ((OrderModel) refundOrder
					.getObject()).getEntries();
			for (AbstractOrderEntryModel entryOrder : entry) {
				if(!cancelId.contains(entryOrder.getTransactionID()) &&
						entryOrder.getQuantity() == 0){
					refundTotal = refundTotal + entryOrder.getNetAmountAfterAllDisc();
				}
				
			}

			/*
			 * String refundOrderTotalText = LabelUtils.getLabel(widget,
			 * "refundOrderTotal", new Object[] {
			 * currencyInstance.format(newTotal) }); Label refundOrderTotal =
			 * new Label(refundOrderTotalText);
			 * refundOrderTotal.setParent(refundOrderContent);
			 */
			Div refundContent = new Div();
			refundContent.setParent(container);
			String refundAmountText = LabelUtils.getLabel(widget,
					"refundAmount",
					new Object[] { currencyInstance.format(refundTotal) });
			Label refund = new Label(refundAmountText);
			refund.setParent(refundContent);
		}
		Div confirmContent = new Div();
		confirmContent.setParent(container);
		Button confirmButton = new Button(LabelUtils.getLabel(widget,
				"confirmButton", new Object[0]));
		confirmButton.setParent(confirmContent);
		confirmButton.addEventListener("onClick",
				createRefundConfirmedEventListener(widget));
		return container;
	}

	protected HtmlBasedComponent createContentInternalNew(ListboxWidget widget,
			HtmlBasedComponent rootContainer) {
		Div container = new Div();
		container.setSclass("csListboxContainer");
		Listbox listBox = new Listbox();
		listBox.setParent(container);
		widget.setListBox(listBox);
		listBox.setVflex(false);
		listBox.setFixedLayout(true);
		listBox.setSclass("csWidgetListbox");
		renderListboxNew(listBox, widget, rootContainer);
		if (isLazyLoadingEnabled()) {
			UITools.applyLazyload(listBox);
		}
		if ((listBox.getItemCount() > 0) && (listBox.getSelectedIndex() <= 0)) {
			listBox.setSelectedIndex(0);
		}
		return container;
	}
	
	protected void renderListboxNew(Listbox listBox,
			ListboxWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			HtmlBasedComponent rootContainer) {
		ListWidgetModel<TypedObject> widgetModel = (ListWidgetModel<TypedObject>) widget.getWidgetModel();
		if (widgetModel == null)
			return;
		TypedObject refundOrder = ((ReturnsController) widget.getWidgetController()).getRefundOrderPreview();
		if (refundOrder == null)
			return;
		OrderModel refundOrderModel = (OrderModel) refundOrder.getObject();

		if ((refundOrderModel.getEntries() == null) || (refundOrderModel.getEntries().isEmpty()))
			return;
		try {
			List<ColumnConfiguration> columns = getColumnConfigurations();
			Listheader listheader;
			if (CollectionUtils.isNotEmpty(columns)) {
				Listhead headRow = new Listhead();
				headRow.setParent(listBox);

				Listheader colProductHeader = new Listheader(LabelUtils.getLabel(widget, "product", new Object[0]));
				colProductHeader.setWidth("300px");
				colProductHeader.setParent(headRow);

				listheader = new Listheader(LabelUtils.getLabel(widget, "basePrice", new Object[0]));
				listheader.setWidth("80px");
				headRow.appendChild(listheader);

				/*listheader = new Listheader(LabelUtils.getLabel(widget, "totalPrice", new Object[0]));
				listheader.setWidth("80px");
				headRow.appendChild(listheader);*/

				listheader = new Listheader(LabelUtils.getLabel(widget, "qty", new Object[0]));
				listheader.setWidth("40px");
				headRow.appendChild(listheader);
			}

			List<TypedObject> items = getCockpitTypeService().wrapItems(refundOrderModel.getEntries());
			for (TypedObject item : items) {
				Listitem row = new Listitem();
				row.setParent(listBox);
				row.setSclass("listbox-row-item");

				Listcell productCell = new Listcell();
				productCell.setParent(row);
				Div productDiv = new Div();
				productDiv.setParent(productCell);
				getPropertyRendererHelper().buildPropertyValuesFromColumnConfigs(item, columns, productDiv);

				PropertyDescriptor basePricePD = getCockpitTypeService()
						.getPropertyDescriptor("AbstractOrderEntry.basePrice");
				PropertyDescriptor totalPricePD = getCockpitTypeService()
						.getPropertyDescriptor("AbstractOrderEntry.netAmountAfterAllDisc");
				PropertyDescriptor qtyPD = getCockpitTypeService().getPropertyDescriptor("AbstractOrderEntry.quantity");

				ObjectValueContainer valueContainer = getValueContainer(item,
						Arrays.asList(new PropertyDescriptor[] { basePricePD, totalPricePD, qtyPD }));

				final CurrencyModel cartCurrencyModel = refundOrderModel.getCurrency();

				NumberFormat currencyInstance = (NumberFormat) getSessionService()
						.executeInLocalView(new SessionExecutionBody() {
							public Object execute() {
								getCommonI18NService()
										.setCurrentCurrency(cartCurrencyModel);
								return getFormatFactory().createCurrencyFormat();
							}
						});
				Double basePriceValue = ObjectGetValueUtils.getDoubleValue(valueContainer, basePricePD);
				String basePriceString = (basePriceValue != null) ? currencyInstance.format(basePriceValue) : "";
				row.appendChild(new Listcell(basePriceString));

				/*Double totalPriceValue = ObjectGetValueUtils.getDoubleValue(valueContainer, totalPricePD);
				String totalPriceString = (totalPriceValue != null) ? currencyInstance.format(totalPriceValue) : "";
				row.appendChild(new Listcell(totalPriceString));*/

				Long qty = ObjectGetValueUtils.getLongValue(valueContainer, qtyPD);
				String qtyString = (qty != null) ? qty.toString() : "";
				row.appendChild(new Listcell(qtyString));
			}
		} catch (Exception e) {
			LOG.error("failed to render return entries list", e);
		}
	}
}