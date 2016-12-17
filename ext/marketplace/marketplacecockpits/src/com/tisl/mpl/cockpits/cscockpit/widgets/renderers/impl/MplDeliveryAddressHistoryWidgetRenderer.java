package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listitem;

import com.tisl.mpl.cockpits.cscockpit.services.AddressHistoryService;
import com.tisl.mpl.cockpits.cscockpit.widgets.models.impl.AddressHistoryListWidgetModel;
import com.tisl.mpl.core.model.MplDeliveryAddressInfoModel;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplDeliveryAddressDao;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractConfigurableCsListboxWidgetRenderer;

public class MplDeliveryAddressHistoryWidgetRenderer
		extends
		AbstractConfigurableCsListboxWidgetRenderer<DefaultListboxWidget<AddressHistoryListWidgetModel, OrderManagementActionsWidgetController>> {

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

	@Autowired
	private AddressHistoryService addressHistoryService;
	@Autowired
	private MplDeliveryAddressDao mplDeliveryAddressDao;
	protected static final String CSS_ORDER_HISTORY = "csOrderHistory";

	protected HtmlBasedComponent createContentInternal(
			DefaultListboxWidget<AddressHistoryListWidgetModel, OrderManagementActionsWidgetController> widget,
			HtmlBasedComponent rootContainer) {
		Div content = new Div();
		content.setSclass("csOrderHistory");

		Listbox listBox = new Listbox();
		listBox.setParent(content);
		listBox.setSclass("csWidgetListbox");

		renderListbox(listBox, widget, rootContainer);

		return content;
	}

	protected void renderListbox(
			Listbox listBox,
			DefaultListboxWidget<AddressHistoryListWidgetModel, OrderManagementActionsWidgetController> widget,
			HtmlBasedComponent rootContainer) {
		TypedObject order = getOrder();
		OrderModel orderModel = (OrderModel) order.getObject();
			Listhead header = new Listhead();
			header.setParent(listBox);
			List<ColumnConfiguration> columns = getColumnConfigurations();
			populateHeaderRow(widget, header, columns);
			if(null != orderModel && null != orderModel.getDeliveryAddresses()) {
				for (AddressModel addressModel : orderModel.getDeliveryAddresses()) {
					TypedObject addressHistory = UISessionUtils.getCurrentSession().getTypeService().wrapItem(addressModel);
					if(null != addressHistory) {
						renderOrderHistory(widget, addressHistory, listBox, columns);
					}
				}
				AddressModel bollingAddress = orderModel.getPaymentAddress();
				try {
					MplDeliveryAddressInfoModel  mplDeliveryAddressInfoModel = null;
					if(null != orderModel.getType() && orderModel.getType().equalsIgnoreCase("PARENT")) {
						mplDeliveryAddressInfoModel = mplDeliveryAddressDao
								.getMplDeliveryAddressReportModelByOrderId(orderModel.getCode());
					}else {
						mplDeliveryAddressInfoModel = mplDeliveryAddressDao
								.getMplDeliveryAddressReportModelByOrderId(orderModel.getParentReference().getCode());
					}
					if(null !=bollingAddress && null != mplDeliveryAddressInfoModel) {
						int rejectsCount = mplDeliveryAddressInfoModel.getChangeDeliveryRejectsCount();
						int totalRequests = mplDeliveryAddressInfoModel.getChangeDeliveryTotalRequests();
						if(totalRequests != 0 && totalRequests != rejectsCount) {
							TypedObject address = UISessionUtils.getCurrentSession().getTypeService().wrapItem(bollingAddress);
							if(null !=address ) {
								renderOrderHistory(widget, address, listBox, columns);
							}
						}
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else {
					Listitem row = new Listitem();
					row.setParent(listBox);
					Listcell cell = new Listcell(LabelUtils.getLabel(widget,
							"noEntries", new Object[0]));
					cell.setParent(row);
				}
      	}	

	protected void renderOrderHistory(
			DefaultListboxWidget<AddressHistoryListWidgetModel, OrderManagementActionsWidgetController> widget,
			TypedObject orderHistory, Listbox listBox,
			List<ColumnConfiguration> columns) {
		Listitem row = new Listitem();
		row.setParent(listBox);
        
		populateDataRow(widget, row, columns, orderHistory);
	}

	protected void populateDataRow(DefaultListboxWidget<AddressHistoryListWidgetModel, OrderManagementActionsWidgetController> widget, Listitem row,
			List<ColumnConfiguration> columns, TypedObject item) {
		
		if (CollectionUtils.isNotEmpty(columns)) {
			 
			for (ColumnConfiguration col : columns) {
				String value = ObjectGetValueUtils.getValue(
						col.getValueHandler(), item);
				Listcell cell = new Listcell(value);
				cell.setParent(row);
			}
		}
	}

}
