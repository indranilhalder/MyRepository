package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;

public class OrderingReferenceNumberColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	@Override
	protected Object getItemValue(ItemModel item, Locale paramLocale)
			throws ValueHandlerException {
		if (item instanceof OrderModel) {
			OrderModel order = (OrderModel) item;
			if (null != order.getParentReference()) {
				return order.getParentReference().getCode();
			} else {
				return order.getCode();
			}
		}
		return null;
	}
}