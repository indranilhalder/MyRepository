package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import java.util.List;
import java.util.Map;

import org.zkoss.zul.Checkbox;

import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.data.CODSelfShipResponseData;
import com.tisl.mpl.data.ReturnInfoData;
import com.tisl.mpl.facades.data.ReturnItemAddressData;
import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.xml.pojo.OrderLineDataResponse;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
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
	
	public List<ReturnLogistics> getReturnLogisticsList(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<ObjectValueContainer> returnObjectValueContainers,
			String pinCode);

	public List<String> getReturnTimeSlotsByKey(String configKey);

	public List<PointOfServiceData> getAllReturnableStores(String pincode,
			String sellerId);

	public List<String> getReturnScheduleDates(AbstractOrderEntryModel entry);

	public CODSelfShipResponseData getCodPaymentInfoToFICO(
			final CODSelfShipData codSelfShipData,
			List<AbstractOrderEntryModel> entry);

	public void retrunInfoCallToOMSFromCsCockpit(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			AbstractOrderEntryModel entry, Map<String, List<String>> storeMap);
	public String getReturnFulFillmenttype(ProductModel product);

	String getReturnFulfillModeByP1(ProductModel productModel);

	public void saveCODReturnsBankDetails(CODSelfShipData codData);
	public CODSelfShipData getCustomerBankDetailsByCustomerId(String customerId);
	public boolean checkProductEligibilityForRTS(List<AbstractOrderEntryModel> entries);

	public boolean checkIfFineJewellery(List<AbstractOrderEntryModel> entries);
}

