package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;

public class DeliveryModeColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	@Override
	protected Object getItemValue(ItemModel model, Locale arg1)
			throws ValueHandlerException {
		if (model instanceof AbstractOrderEntryModel) {
			AbstractOrderEntryModel entry = (AbstractOrderEntryModel) model;
			if (entry.getMplDeliveryMode() !=null  && entry.getMplDeliveryMode().getDeliveryMode()!=null){
				return entry.getMplDeliveryMode().getDeliveryMode().getName();
			}
		
		}
		return StringUtils.EMPTY;
	}
}