package com.tisl.mpl.cockpits.cscockpit.widgets.models.impl;

import org.springframework.util.ObjectUtils;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;

public class AddressHistoryListWidgetModel extends
		DefaultListWidgetModel<TypedObject> {
	private TypedObject order;

	public TypedObject getOrder() {
		return order;
	}

	public boolean setOrder(TypedObject order) {
		boolean changed = false;
		if (!ObjectUtils.nullSafeEquals(this.order, order)) {
			changed = true;
			this.order = order;
		}
		return changed;
	}
}
