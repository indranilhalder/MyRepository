/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import org.springframework.util.Assert;

import com.tisl.mpl.core.model.TemproryAddressModel;


/**
 * @author pankajk
 *
 */
public class TemporaryAddressReversePopulator implements Populator<AddressData, TemproryAddressModel>
{
	private CommonI18NService commonI18NService;

	@Override
	public void populate(final AddressData addressData, final TemproryAddressModel temproryAddressModel)
			throws ConversionException
	{

		Assert.notNull(addressData, "Parameter addressData cannot be null.");
		Assert.notNull(temproryAddressModel, "Parameter addressModel cannot be null.");

		temproryAddressModel.setFirstname(addressData.getFirstName());
		temproryAddressModel.setLastname(addressData.getLastName());
		temproryAddressModel.setAddressType(addressData.getAddressType());
		temproryAddressModel.setStreetname(addressData.getLine1());
		temproryAddressModel.setStreetnumber(addressData.getLine2());
		temproryAddressModel.setAddressLine3(addressData.getLine3());
		temproryAddressModel.setTown(addressData.getTown());
		temproryAddressModel.setLandmark(addressData.getLandmark());
		temproryAddressModel.setPostalcode(addressData.getPostalCode());
		temproryAddressModel.setPhone1(addressData.getPhone());
		temproryAddressModel.setState(addressData.getState());
		temproryAddressModel.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
		temproryAddressModel.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));

		if (addressData.getCountry() != null)
		{
			final String isocode = addressData.getCountry().getIsocode();
			try
			{
				final CountryModel countryModel = getCommonI18NService().getCountry(isocode);
				temproryAddressModel.setCountry(countryModel);
			}
			catch (final UnknownIdentifierException e)
			{
				throw new ConversionException("No country with the code " + isocode + " found.", e);
			}
			catch (final AmbiguousIdentifierException e)
			{
				throw new ConversionException("More than one country with the code " + isocode + " found.", e);
			}
		}


		if (addressData.getRegion() != null)
		{
			final String isocode = addressData.getRegion().getIsocode();
			try
			{
				final RegionModel regionModel = getCommonI18NService().getRegion(
						getCommonI18NService().getCountry(addressData.getCountry().getIsocode()), isocode);
				temproryAddressModel.setRegion(regionModel);
			}
			catch (final UnknownIdentifierException e)
			{
				throw new ConversionException("No region with the code " + isocode + " found.", e);
			}
			catch (final AmbiguousIdentifierException e)
			{
				throw new ConversionException("More than one region with the code " + isocode + " found.", e);
			}
		}
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}




}