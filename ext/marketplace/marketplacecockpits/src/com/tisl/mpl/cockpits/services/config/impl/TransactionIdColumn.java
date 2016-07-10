package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public class TransactionIdColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	@Override
	protected Object getItemValue(ItemModel model, Locale arg1)
			throws ValueHandlerException {
		String id = StringUtils.EMPTY;
		if (model instanceof ConsignmentModel) {
			ConsignmentModel csg = (ConsignmentModel )model;
			for(ConsignmentEntryModel ce: csg.getConsignmentEntries()){
				id = ce.getOrderEntry().getTransactionID();
				break;// always one entry for each consignment
			}
		}
		return id;
	}
}