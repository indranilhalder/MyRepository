/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.tisl.mpl.facades.data.ProductDataForOrderXML;


/**
 * @author TCS
 *
 */
public class MplProductDataPopulator implements Populator<OrderModel, com.tisl.mpl.facades.data.ProductDataForOrderXML>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final OrderModel source, final ProductDataForOrderXML target) throws ConversionException
	{


		target.setCancellationAllowed("1");
		target.setReturnsAllowed("1");
		target.setReplacementAllowed("1");

		/*
		 * if (source.getEntries() != null) { final ProductModel poduct = source.getEntries().get(0).getProduct();
		 * target.setProductName(poduct.getName()); if (poduct.getCancellationWindow() != null &&
		 * !poduct.getCancellationWindow().isEmpty()) { target.setCancellationAllowed("1"); } else {
		 * target.setCancellationAllowed("0"); }
		 * 
		 * if (poduct.getReturnsAllowedWindow() != null && !poduct.getReturnsAllowedWindow().isEmpty()) {
		 * target.setReturnsAllowed("1"); } else { target.setReturnsAllowed("0"); }
		 * 
		 * if (poduct.getExchangeAllowedWindow() != null && !poduct.getExchangeAllowedWindow().isEmpty()) {
		 * target.setExchangeAllowed("1"); } else { target.setExchangeAllowed("0"); }
		 * 
		 * if (poduct.getReplacementWindow() != null && !poduct.getReplacementWindow().isEmpty()) {
		 * target.setReplacementAllowed("1"); } else { target.setReplacementAllowed("0"); }
		 * 
		 * }
		 */


	}
}
