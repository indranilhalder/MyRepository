/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.facades.data.OrderDataSubOrderForXML;
import com.tisl.mpl.facades.data.OrderDataTransactionForXML;


/**
 * @author TCS
 *
 */
public class MplSubOrderPopulator implements Populator<OrderModel, OrderDataSubOrderForXML>
{

	private Converter<OrderModel, OrderDataTransactionForXML> childOrderConverter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final OrderModel source, final OrderDataSubOrderForXML target) throws ConversionException
	{
		final List<OrderDataTransactionForXML> childOrders = new ArrayList<OrderDataTransactionForXML>();
		target.setOrderNo(source.getCode());
		for (final OrderModel order : source.getChildOrders())
		{
			childOrders.add(getChildOrderConverter().convert(order));
		}

		target.setTransaction(childOrders);

	}

	/**
	 * @return the childOrderConverter
	 */
	public Converter<OrderModel, OrderDataTransactionForXML> getChildOrderConverter()
	{
		return childOrderConverter;
	}

	/**
	 * @param childOrderConverter
	 *           the childOrderConverter to set
	 */
	@Required
	public void setChildOrderConverter(final Converter<OrderModel, OrderDataTransactionForXML> childOrderConverter)
	{
		this.childOrderConverter = childOrderConverter;
	}


}
