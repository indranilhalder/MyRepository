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
import org.springframework.util.Assert;

import com.tisl.mpl.facades.data.CustomerDataForOrderXML;
import com.tisl.mpl.facades.data.OrderDataForXML;
import com.tisl.mpl.facades.data.OrderDataSubOrderForXML;


/**
 * @author TCS
 * 
 */
public class ParentOrderPopulator implements Populator<OrderModel, OrderDataForXML>
{


	private Converter<OrderModel, OrderDataSubOrderForXML> mplSubOrderConverter;
	private Converter<OrderModel, CustomerDataForOrderXML> mplCustomerDataConverter;


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final OrderModel source, final OrderDataForXML target) throws ConversionException
	{

		final List<CustomerDataForOrderXML> customerAddress = new ArrayList<CustomerDataForOrderXML>();
		final List<OrderDataSubOrderForXML> subOrders = new ArrayList<OrderDataSubOrderForXML>();
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setOrderRefNo(source.getCode());
		target.setOrderDate(source.getCreationtime());
		/*if (source.getChannel() != null)
		{
			target.setChannel(source.getChannel().getCode());
		}*/

		if (source.getSalesApplication() != null)
		{
			target.setChannel(source.getSalesApplication().getCode());
		}


		for (final OrderModel order : source.getChildOrders())
		{
			subOrders.add(getMplSubOrderConverter().convert(order));
		}
		customerAddress.add(getMplCustomerDataConverter().convert(source));
		target.setCustomerAddress(customerAddress);
		target.setSubOrder(subOrders);


	}

	/**
	 * @return the mplSubOrderConverter
	 */
	public Converter<OrderModel, OrderDataSubOrderForXML> getMplSubOrderConverter()
	{
		return mplSubOrderConverter;
	}

	/**
	 * @param mplSubOrderConverter
	 *           the mplSubOrderConverter to set
	 */
	@Required
	public void setMplSubOrderConverter(final Converter<OrderModel, OrderDataSubOrderForXML> mplSubOrderConverter)
	{
		this.mplSubOrderConverter = mplSubOrderConverter;
	}

	/**
	 * @return the mplCustomerDataConverter
	 */
	public Converter<OrderModel, CustomerDataForOrderXML> getMplCustomerDataConverter()
	{
		return mplCustomerDataConverter;
	}

	/**
	 * @param mplCustomerDataConverter
	 *           the mplCustomerDataConverter to set
	 */
	@Required
	public void setMplCustomerDataConverter(final Converter<OrderModel, CustomerDataForOrderXML> mplCustomerDataConverter)
	{
		this.mplCustomerDataConverter = mplCustomerDataConverter;
	}

}
