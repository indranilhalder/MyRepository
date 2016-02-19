package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.xml.pojo.OrderLineDataResponse;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.cscockpit.widgets.controllers.ReturnsController;

public interface MarketPlaceReturnsController extends ReturnsController {

	public Map<Boolean, List<OrderLineDataResponse>> validateReverseLogistics(
			List<ReturnLogistics> returnLogisticsList);

	public String getSellerName(String ussid);

	public List<ReturnLogistics> getReturnLogisticsList(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<ObjectValueContainer> returnObjectValueContainers);

	public boolean isFreebieAvaialble(
			List<ObjectValueContainer> orderEntryReturnsRecords);

}
