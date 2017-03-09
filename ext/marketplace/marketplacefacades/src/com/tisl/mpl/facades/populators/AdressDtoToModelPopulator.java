/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;



/**
 * @author pankajk
 *
 */
public class AdressDtoToModelPopulator implements Populator<AddressData,AddressModel>
{
	private CommonI18NService commonI18NService;
	private static final Logger LOG = Logger.getLogger(AdressDtoToModelPopulator.class);
	@Override
	public void populate(AddressData addressData,AddressModel addressModel)
			throws ConversionException
	{
		Assert.notNull(addressData, "Parameter addressData cannot be null.");
		Assert.notNull(addressModel, "Parameter addressModel cannot be null.");
		addressModel.setFirstname(addressData.getFirstName());
		addressModel.setLastname(addressData.getLastName());
		addressModel.setAddressType(addressData.getAddressType());
		addressModel.setStreetname(addressData.getLine1());
		addressModel.setStreetnumber(addressData.getLine2());
		addressModel.setAddressLine3(addressData.getLine3());
		addressModel.setTown(addressData.getTown());
		addressModel.setLandmark(addressData.getLandmark());
		addressModel.setPostalcode(addressData.getPostalCode());
		addressModel.setPhone1(addressData.getPhone());
		addressModel.setCellphone(addressData.getPhone());
		addressModel.setState(addressData.getState());
		addressModel.setDistrict(addressData.getState());
		addressModel.setCity(addressData.getCity());
		addressModel.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
		addressModel.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
		addressModel.setPublicKey(addressData.getId());
		if (addressData.getCountry() != null)
		{
			final String isocode = addressData.getCountry().getIsocode();
			try
			{
				final CountryModel countryModel = getCommonI18NService().getCountry(isocode);
				addressModel.setCountry(countryModel);
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.error("No country with the code " + isocode + " found."+e.getMessage());
			}
			catch (final AmbiguousIdentifierException e)
			{
				LOG.error("More than one country with the code " + isocode + " found."+e.getMessage());
			}
		}


		if (addressData.getRegion() != null)
		{
			final String isocode = addressData.getRegion().getIsocode();
			try
			{
				 RegionModel regionModel = getCommonI18NService().getRegion(
						getCommonI18NService().getCountry(addressData.getCountry().getIsocode()), isocode);
				addressModel.setRegion(regionModel);
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.error("No region with the code " + isocode + " found."+e.getMessage());
			}
			catch (final AmbiguousIdentifierException e)
			{
				LOG.error("More than one region with the code " + isocode + " found."+e.getMessage());
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