package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.AddressHistoryService;
import com.tisl.mpl.cockpits.cscockpit.widgets.models.impl.AddressHistoryListWidgetModel;
import com.tisl.mpl.core.model.MplDeliveryAddressInfoModel;
import com.tisl.mpl.marketplacecommerceservices.daos.changedeliveryaddress.MplDeliveryAddressDao;

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
		OrderModel subOrderModel = (OrderModel) order.getObject();
		OrderModel orderModel = null;
		if(null != subOrderModel.getParentReference()) {
			orderModel=subOrderModel.getParentReference();
		}else {
			orderModel = subOrderModel;
		}
			Listhead header = new Listhead();
			header.setParent(listBox);
			List<ColumnConfiguration> columns = getColumnConfigurations();
			populateHeaderRow(widget, header, columns);
			if(null != orderModel && null != orderModel.getDeliveryAddresses()) {
				AddressModel bollingAddress = orderModel.getPaymentAddress();

				int size = 0;
				if(null != orderModel.getDeliveryAddresses() ) {
					size = orderModel.getDeliveryAddresses().size();
				}
				
				for ( ; size>0;size--) {
					AddressModel addressModel = orderModel.getDeliveryAddresses().get(size-1);
					TypedObject addressHistory = UISessionUtils.getCurrentSession().getTypeService().wrapItem(addressModel);
					if(null != addressHistory) {
						renderOrderHistory(widget, addressHistory, listBox, columns);
					}
				}
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
							/*TISPRDT-889 START*/ 
							AddressModel addressModel = (AddressModel) address.getObject();
							if(null != orderModel.getDeliveryAddresses() ) {
								size = orderModel.getDeliveryAddresses().size();
							}
							AddressModel deliveryAddress = null;
							if(size>0) {
								deliveryAddress=orderModel.getDeliveryAddresses().get(0);
							}
							if(null != addressModel && null !=  deliveryAddress && null != addressModel.getPk() && null != deliveryAddress.getPk()) {
								if(!addressModel.getPk().equals(deliveryAddress.getPk())){
									renderOrderHistory(widget, address, listBox, columns);
								}
							}
							/*TISPRDT-889 END*/
							
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
	protected void populateHeaderRow(DefaultListboxWidget<AddressHistoryListWidgetModel, OrderManagementActionsWidgetController> widget, Listhead row,
			List<ColumnConfiguration> columns) {
		if (!(CollectionUtils.isNotEmpty(columns)))
			return;
		for (ColumnConfiguration col : columns) {
			Listheader header = new Listheader(getPropertyRendererHelper()
					.getPropertyDescriptorName(col));
			header.setWidth("100px");
			header.setTooltiptext(col.getName());
			row.appendChild(header);
		}
	}

	protected void populateDataRow(DefaultListboxWidget<AddressHistoryListWidgetModel, OrderManagementActionsWidgetController> widget, Listitem row,
			List<ColumnConfiguration> columns, TypedObject item) {
		
		if (CollectionUtils.isNotEmpty(columns)) {
			 
			for (ColumnConfiguration col : columns) {
				String value = ObjectGetValueUtils.getValue(
						col.getValueHandler(), item);
				if(null == value || value.isEmpty() ) {
					value = MarketplaceCockpitsConstants.NA;
				}
				Listcell cell = new Listcell(value);
				cell.setParent(row);
			}
		}
	}

}
