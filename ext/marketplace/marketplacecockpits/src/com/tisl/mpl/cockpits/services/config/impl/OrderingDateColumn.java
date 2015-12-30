package com.tisl.mpl.cockpits.services.config.impl;

import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;

public class OrderingDateColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	@Override
	protected Object getItemValue(ItemModel item, Locale paramLocale)
			throws ValueHandlerException {
		
		if (item instanceof AbstractOrderModel) {
			AbstractOrderModel order = (AbstractOrderModel) item;
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"MMM  d yyyy HH:mm:ss");
			return dateFormat.format(order.getCreationtime());

		}
		return null;
	}

}
