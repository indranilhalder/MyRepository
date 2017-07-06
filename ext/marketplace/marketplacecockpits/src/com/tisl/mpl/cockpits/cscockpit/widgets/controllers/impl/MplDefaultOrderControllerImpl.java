package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import javax.annotation.Resource;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MplDefaultOrderController;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;

//CKD:TPR-3809 : PAN Card Changes
public class MplDefaultOrderControllerImpl extends MarketPlaceDefaultOrderController implements MplDefaultOrderController {

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService mplJewelleryService;
	 
	@Override
	public String getPanCardStatus(String orderLineId) {
		return mplJewelleryService.getPanCardStatus(orderLineId);
	}

}
