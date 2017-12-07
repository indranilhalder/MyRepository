package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MplDefaultOrderController;
import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplUtrNoArnNoDao;
import com.tisl.mpl.marketplacecommerceservices.service.MPLReturnService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplUtrNoArnNoService;

//CKD:TPR-3809 : PAN Card Changes
public class MplDefaultOrderControllerImpl extends MarketPlaceDefaultOrderController implements MplDefaultOrderController {

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService mplJewelleryService;
	
	@Resource(name = "mplUtrNoArnNoService")
	private MplUtrNoArnNoService mplUtrNoArnNoService;
	 
	@Autowired
	private MPLReturnService mplReturnService;
	
	@Override
	public String getPanCardStatus(String orderLineId) {
		return mplJewelleryService.getPanCardStatus(orderLineId);
	}
   
	
	// Added for TPR-7412 
	@Override
	public String getUtrNoArnNo(String orderLineId) {
		// TODO Auto-generated method stub
		return mplUtrNoArnNoService.getUtrNoArnNo(orderLineId);
		
	}

	@Override
	public MplCustomerBankAccountDetailsModel getCustomerBankdetails(String customerId) {
		MplCustomerBankAccountDetailsModel customerBankDetailsModel = null;
		customerBankDetailsModel=mplReturnService.getCustomerBakDetailsById(customerId);
		return customerBankDetailsModel;
	}

}
