/**
 *
 */
package com.tisl.mpl.integration.oms.adapter;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.integration.oms.OrderLineQuantityWrapper;
import de.hybris.platform.integration.oms.adapter.OmsSyncAdapter;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.shipping.Shipment;


/**
 * @author TCS
 *
 */
public class CustomOmsOLQSyncAdapter implements OmsSyncAdapter<OrderLineQuantityWrapper, ConsignmentEntryModel>
{
	private Converter<OrderLineQuantity, ConsignmentEntryModel> omsOLQReverseConverter;
	private ModelService modelService;

	public ConsignmentEntryModel update(final OrderLineQuantityWrapper olqWrapper, final ItemModel parent)
	{
		final ConsignmentModel consigment = (ConsignmentModel) parent;
		ConsignmentEntryModel existingConsignmentEntry = getConsignmentEntry(olqWrapper.getOrderLineEntryNumber(), consigment);

		if (existingConsignmentEntry == null)
		{
			existingConsignmentEntry = (ConsignmentEntryModel) getModelService().create(ConsignmentEntryModel.class);
			final AbstractOrderEntryModel orderEntry = getOrderEntryByOrderLine(olqWrapper.getOrderLineEntryNumber(),
					consigment.getOrder());
			existingConsignmentEntry.setOrderEntry(orderEntry);
		}

		getOmsOLQReverseConverter().convert(olqWrapper.getOrderLineQuantity(), existingConsignmentEntry);

		getModelService().save(existingConsignmentEntry);

		return existingConsignmentEntry;
	}

	protected ConsignmentEntryModel getConsignmentEntry(final Integer entryNumber, final ConsignmentModel consignment)
	{
		for (final ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries())
		{
			if (consignmentEntry.getOrderEntry().getEntryNumber().equals(entryNumber))
			{
				return consignmentEntry;
			}
		}

		return null;
	}

	protected List<Shipment> getShipments(final Order order)
	{
		final List shipments = new ArrayList();
		final Set shipmentIds = new HashSet();
		for (final OrderLineQuantity olq : order.getOrderLineQuantities())
		{
			if (!(shipmentIds.add(olq.getShipment().getId())))
			{
				continue;
			}
			shipments.add(olq.getShipment());
		}

		return shipments;
	}

	protected AbstractOrderEntryModel getOrderEntryByOrderLine(final Integer entryNumber, final AbstractOrderModel orderModel)
	{
		for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
		{
			if (orderEntry.getEntryNumber().equals(entryNumber))
			{
				return orderEntry;
			}
		}

		return null;
	}

	protected Converter<OrderLineQuantity, ConsignmentEntryModel> getOmsOLQReverseConverter()
	{
		return this.omsOLQReverseConverter;
	}

	@Required
	public void setOmsOLQReverseConverter(final Converter<OrderLineQuantity, ConsignmentEntryModel> omsOLQReverseConverter)
	{
		this.omsOLQReverseConverter = omsOLQReverseConverter;
	}

	protected ModelService getModelService()
	{
		return this.modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.integration.oms.adapter.OmsSyncAdapter#update(com.hybris.commons.dto.EntityDto,
	 * java.util.Date)
	 */
	@Override
	public ConsignmentEntryModel update(final OrderLineQuantityWrapper arg0, final Date arg1)
	{
		// YTODO Auto-generated method stub
		return null;
	}
}