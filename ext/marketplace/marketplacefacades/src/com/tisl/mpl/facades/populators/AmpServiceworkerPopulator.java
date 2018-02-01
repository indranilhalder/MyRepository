/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.tisl.mpl.core.model.AmpServiceworkerModel;
import com.tisl.mpl.facades.cms.data.AmpServiceWorkerData;


/**
 * @author TCS
 *
 */
public class AmpServiceworkerPopulator implements Populator<AmpServiceworkerModel, AmpServiceWorkerData>
{

	@Override
	public void populate(final AmpServiceworkerModel source, final AmpServiceWorkerData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (source.getCode() != null)
		{
			target.setCode(source.getCode());
		}
		if (source.getCacheName() != null)
		{
			target.setCacheName(source.getCacheName());
		}
		if (source.getCacheVersion() != null)
		{
			target.setCacheVersion(source.getCacheVersion());
		}
		if (source.getFilesToCache() != null)
		{
			target.setFilesToCache(source.getFilesToCache());
		}
		if (source.getCachingStrategy() != null)
		{
			target.setCachingStrategy(source.getCachingStrategy());
		}
		if (source.getServiceworkerJs() != null)
		{
			target.setServiceworkerJs(source.getServiceworkerJs());
		}
	}
}
