
package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;
import de.hybris.platform.returns.model.RefundEntryModel;

public class OrderStatusColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	@Override
	protected Object getItemValue(ItemModel model, Locale arg1)
			throws ValueHandlerException {
		ConsignmentStatus consignmentStatus = null;
		AbstractOrderEntryModel entry = null;
		if (model instanceof AbstractOrderEntryModel) {
			entry = (AbstractOrderEntryModel) model;
		} else if (model instanceof RefundEntryModel) {
			entry = ((RefundEntryModel) model).getOrderEntry();
		}
		AbstractOrderModel orderModel = entry.getOrder();
		if (entry.getQuantity() <= 0
				&& (CollectionUtils.isEmpty(((AbstractOrderEntryModel) model)
						.getConsignmentEntries()))) {
			return "Order Cancelled";
		}
		OrderStatus orderStatus = orderModel.getStatus();
		if (CollectionUtils.isNotEmpty(entry
				.getConsignmentEntries())) {
			consignmentStatus = entry
					.getConsignmentEntries().iterator().next().getConsignment()
					.getStatus();
		}
		if (null != consignmentStatus) {
			return TypeTools.getEnumName(consignmentStatus);
		} else if (null != orderStatus) {
			return TypeTools.getEnumName(orderStatus);
		}
		return StringUtils.EMPTY;
	}

}
