package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

//CKD:TPR-3809 : PAN Card Changes
public interface MplDefaultOrderController {

	public String getPanCardStatus(String orderLineId);
	
	public String getUtrNoArnNo(String orderLineId);

}
