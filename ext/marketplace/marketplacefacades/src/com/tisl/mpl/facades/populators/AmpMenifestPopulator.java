/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.tisl.mpl.core.model.AmpMenifestModel;
import com.tisl.mpl.facades.cms.data.AmpMenifestData;


/**
 * @author TCS
 *
 */
public class AmpMenifestPopulator implements Populator<AmpMenifestModel, AmpMenifestData>
{

	@Override
	public void populate(final AmpMenifestModel source, final AmpMenifestData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (source.getCode() != null)
		{
			target.setCode(source.getCode());
		}
		if (source.getMenifestJson() != null)
		{
			target.setMenifestJson(source.getMenifestJson());
		}

	}
}