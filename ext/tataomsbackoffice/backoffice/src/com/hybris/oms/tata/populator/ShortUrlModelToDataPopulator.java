/**
 *
 */
package com.hybris.oms.tata.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.techouts.backoffice.ShortUrlReportData;
import com.tisl.mpl.core.model.OrderShortUrlInfoModel;


/**
 * @author prabhakar
 *
 */
public class ShortUrlModelToDataPopulator implements Populator<OrderShortUrlInfoModel, ShortUrlReportData>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final OrderShortUrlInfoModel source, final ShortUrlReportData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setOrderId(source.getOrderId());
		target.setCliks(source.getClicks());
		target.setLogins(source.getLogin());
		target.setLongUrl(source.getLongURL());
		target.setShortUrl(source.getShortURL());



	}



}
