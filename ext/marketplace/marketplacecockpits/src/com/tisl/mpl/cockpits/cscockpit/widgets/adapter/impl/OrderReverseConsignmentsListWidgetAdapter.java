package com.tisl.mpl.cockpits.cscockpit.widgets.adapter.impl;

import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.adapters.AbstractInitialisingWidgetAdapter;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

public class OrderReverseConsignmentsListWidgetAdapter
		extends
		AbstractInitialisingWidgetAdapter<DefaultMasterDetailListWidgetModel<TypedObject>, OrderController> {
	protected boolean updateModel() {
		boolean changed = true;
		TypedObject orderObject = ((OrderController) getWidgetController())
				.getCurrentOrder();

		if (orderObject != null) {
			OrderModel orderModel = (OrderModel) orderObject.getObject();
			List<TypedObject> consignments = new ArrayList<>();
			if(orderModel.getReturnRequests()!=null && orderModel.getReturnRequests().size()>0){
				for(ReturnRequestModel rrequest : orderModel.getReturnRequests()){
					
					for(ReturnEntryModel rRequestEntry : rrequest.getReturnEntries()){
						for(ConsignmentModel consgment: orderModel.getConsignments()){
							if(consgment.getCode().equalsIgnoreCase(rRequestEntry.getOrderEntry().getTransactionID())){
								consignments.add(getCockpitTypeService().wrapItem(
										consgment));
							}
						}
					}
				}
				changed |= ((DefaultMasterDetailListWidgetModel<TypedObject>) getWidgetModel())
						.setItems(consignments);
				
			} else{
				changed |= ((DefaultMasterDetailListWidgetModel<TypedObject>) getWidgetModel())
						.setItems(consignments);
			}
		}
		return changed;
	}
}