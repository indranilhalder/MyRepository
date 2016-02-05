package com.tisl.mpl.cockpits.services.config.impl;

import java.util.Iterator;
import java.util.Locale;

import net.sourceforge.pmd.util.StringUtil;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractSimpleCustomColumnConfiguration;
import de.hybris.platform.returns.model.RefundEntryModel;

public class ImeiSerialNumberColumn extends
		AbstractSimpleCustomColumnConfiguration<Object, ItemModel> {

	@Override
	protected Object getItemValue(ItemModel item, Locale paramLocale)
			throws ValueHandlerException {
		if (item instanceof RefundEntryModel) {
			RefundEntryModel refundEntry = (RefundEntryModel) item;
			String serialNumber = "";
			if (null != (refundEntry.getOrderEntry().getImeiDetail())) {
				for (Iterator iterator = refundEntry.getOrderEntry()
						.getImeiDetail().getIdentifiers().iterator(); iterator
						.hasNext();) {
					if (StringUtil.isNotEmpty(serialNumber)) {
						serialNumber = serialNumber + "-"
								+ (String) iterator.next();
					} else {
						serialNumber = (String) iterator.next();
					}
				}
				if (StringUtil.isNotEmpty(serialNumber)) {
					serialNumber = serialNumber
							+ "/"
							+ (refundEntry.getOrderEntry().getImeiDetail()
									.getSerialNum());
				} else {
					serialNumber = (refundEntry.getOrderEntry().getImeiDetail()
							.getSerialNum());
				}
			}
			return serialNumber;
		}
		return null;
	}
}