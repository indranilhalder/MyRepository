package com.tisl.mpl.cockpits.cscockpit.data;

import java.io.Serializable;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

public class RefundDeliveryData implements Serializable {

	private static final long serialVersionUID = 1L;
	private AbstractOrderEntryModel abstractOrderEntryModel;
	private String reason;
	private String notes;
	private boolean isChecked;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public AbstractOrderEntryModel getAbstractOrderEntryModel() {
		return abstractOrderEntryModel;
	}

	public void setAbstractOrderEntryModel(
			AbstractOrderEntryModel abstractOrderEntryModel) {
		this.abstractOrderEntryModel = abstractOrderEntryModel;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}