/**
 *
 */
package com.tisl.mpl.integration.oms.adapter;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.integration.oms.order.populators.OmsShipmentReversePopulator;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collections;
import java.util.List;

import com.hybris.oms.domain.shipping.Shipment;


/**
 * @author 397968
 *
 */
public class CustomOmsShipmentReversePopulator extends OmsShipmentReversePopulator
{
	@Override
	public void populate(final Shipment source, final ConsignmentModel target) throws ConversionException
	{
		final PointOfServiceModel pointOfService = getPointOfServiceService().getPointOfServiceForName(source.getLocation());
		target.setDeliveryPointOfService(pointOfService);

		final AddressModel addressModel = (AddressModel) getModelService().create(AddressModel.class);
		if (addressModel != null)
		{
			getOmsAddressReverseConverter().convert(source.getDelivery().getDeliveryAddress(), addressModel);
			target.setShippingAddress(addressModel);
		}

		final ConsignmentStatus consignmentStatus = getConsignmentStatusMapping().get(source.getOlqsStatus());

		target.setStatus(consignmentStatus);

		if (source.getDelivery() != null)
		{
			target.setTrackingID(source.getDelivery().getTrackingID());
			target.setTrackingURL(source.getDelivery().getTrackingUrl());
			target.setShippingDate(source.getDelivery().getActualDeliveryDate());
		}

		final WarehouseModel wareHouse = getWarehouseService().getWarehouseForCode(source.getLocation());
		target.setWarehouse(wareHouse);

		target.setCode(source.getShipmentId());

		final List orderModel = this.getOrderDao().find(Collections.singletonMap("code", source.getOrderId()));
		if (orderModel.size() <= 0)
		{
			return;
		}
		target.setOrder((AbstractOrderModel) orderModel.get(0));
	}


}
