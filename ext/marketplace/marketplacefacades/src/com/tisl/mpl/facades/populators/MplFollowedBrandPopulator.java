/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.BrandMasterModel;
import com.tisl.mpl.wsdto.FollowedBrandWsDto;


/**
 * @author TCS
 *
 */
public class MplFollowedBrandPopulator implements Populator<BrandMasterModel, FollowedBrandWsDto>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final BrandMasterModel source, final FollowedBrandWsDto target) throws ConversionException
	{
		// YTODO Auto-generated method stub
		if (StringUtils.isNotEmpty(source.getBrandCode()))
		{
			target.setCode(source.getBrandCode());
		}

		if (StringUtils.isNotEmpty(source.getLogoImageUrl()))
		{
			target.setBrandMediaUrl(source.getLogoImageUrl());
		}

		if (StringUtils.isNotEmpty(source.getName()))
		{
			target.setName(source.getName());
		}
	}

}
