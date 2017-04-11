/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;


/**
 * @author TO-OW107
 *
 */
public class CustomAddressReversePopulator extends AddressReversePopulator
{


	@Override
	public void populate(final AddressData source, final AddressModel target) throws ConversionException
	{
		Assert.notNull(source, "Parameter addressData cannot be null.");
		Assert.notNull(source, "Parameter addressModel cannot be null.");
		super.populate(source, target);
		target.setState(source.getState());
		target.setDistrict(source.getState());
		target.setAddressType(source.getAddressType());
		target.setAddressLine3(source.getLine3());
		target.setLocality(source.getLocality());
		if (null != source.getCity())
		{
			target.setCity(source.getCity());
			target.setTown(source.getCity());
		}
		else if (null != source.getTown())
		{
			target.setCity(source.getTown());
			target.setTown(source.getTown());
		}
		if (null != source.getLandmark())
		{
			target.setLandmark(source.getLandmark());
		}
		target.setCellphone(source.getPhone());

	}
}
