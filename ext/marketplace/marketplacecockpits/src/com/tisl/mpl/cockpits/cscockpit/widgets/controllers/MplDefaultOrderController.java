package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;

//CKD:TPR-3809 : PAN Card Changes
public interface MplDefaultOrderController {

	public String getPanCardStatus(String orderLineId);
	
	public String getUtrNoArnNo(String orderLineId);
	
	public MplCustomerBankAccountDetailsModel getCustomerBankdetails(String customerId);

}
