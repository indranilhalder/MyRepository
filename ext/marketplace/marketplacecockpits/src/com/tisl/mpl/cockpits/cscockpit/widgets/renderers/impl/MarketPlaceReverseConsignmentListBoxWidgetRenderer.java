package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import com.tisl.mpl.xml.pojo.AWBStatusResponse;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public class MarketPlaceReverseConsignmentListBoxWidgetRenderer extends MarketPlaceConfigurableCsMasterDetailListboxWidgetRenderer{
	
	@Override
	protected AWBStatusResponse getAWBStatusResponse(final TypedObject item)  {
		ConsignmentModel consignmentModel = (ConsignmentModel) item.getObject();
		return getMplAwbStatusService().prepAwbNumbertoOMS(consignmentModel.getReturnAWBNum(),
				consignmentModel.getReturnCarrier());
	}

}
