/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.data.ReturnItemAddressData;


/**
 * @author Dileep
 *
 */
public class ReturnItemAddressDataPopulator implements Populator<AddressData, ReturnItemAddressData>
{

	private static final Logger LOG = Logger.getLogger(ReturnItemAddressDataPopulator.class);

	/**
	 * @Description Populating LandMarks Model to LandMarks Data
	 * @param source
	 * @param target
	 */

	@Override
	public void populate(final AddressData source, final ReturnItemAddressData target) throws ConversionException
	{

		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);
		LOG.debug("Populating LandMarksData from LandMarksModel");
		target.setAddressLane1(source.getLine1());
		target.setAddressLane2(source.getLine2());
		target.setCity(source.getCity());
		target.setCountry(source.getCountry().getName());
		target.setFirstName(source.getFirstName());
		target.setLandmark(source.getLandmark());
		target.setLastName(source.getLastName());
		target.setMobileNo(source.getPhone());
		target.setPincode(source.getPostalCode());
		target.setState(source.getState());
	}

}
