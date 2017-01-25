/**
 *
 */
package com.tisl.mpl.integration.oms.order.populators;

import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.oms.domain.address.Address;
import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.model.StateModel;




/**
 * @author TCS
 *
 */
public class CustomOmsAddressPopulator implements Populator<AddressModel, Address>
{
	private static final Logger LOG = Logger.getLogger(CustomOmsAddressPopulator.class);
	private CustomerNameStrategy customerNameStrategy;

	@Autowired
	private MplCommerceCartService mplCommerceCartService;



	public void populate(final AddressModel source, final Address target) throws ConversionException
	{
		Assert.notNull(source, "source Address can't be null");
		Assert.notNull(target, "target Address can't be null");

		if (StringUtils.isNotBlank(source.getFirstname()))
		{
			target.setFirstName(source.getFirstname());
		}
		if (StringUtils.isNotBlank(source.getLastname()))
		{
			target.setLastName(source.getLastname());
		}

		//TPR-3402 starts

		String addressLine = (source.getLine1() != null ? source.getLine1() : source.getStreetname());
		if (StringUtils.isEmpty(addressLine))
		{
			LOG.info("AddressLine is null " + addressLine);
			addressLine = StringUtils.EMPTY;
		}
		final String addressLine2 = (source.getLine2() != null ? source.getLine2() : source.getStreetnumber());

		if (StringUtils.isNotEmpty(addressLine2))
		{
			addressLine = addressLine.concat(" ").concat(addressLine2).trim();
		}

		if (StringUtils.isNotEmpty(source.getAddressLine3()))
		{
			addressLine = addressLine.concat(" ").concat(source.getAddressLine3()).trim();
		}

		String addrLine1 = StringUtils.EMPTY;
		String addrLine2 = StringUtils.EMPTY;
		String addrLine3 = StringUtils.EMPTY;

		int pointer = MarketplaceomsordersConstants.ZERO_INT;
		addressLine = addressLine.trim();

		if (addressLine.length() <= MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
		{
			addrLine1 = addressLine.trim();
			//addrLine2 = StringUtils.EMPTY;
			//addrLine3 = StringUtils.EMPTY;
		}
		else if (addressLine.length() <= MarketplaceomsordersConstants.MAX_LEN_OF_ADDR_LINE)
		{
			pointer = addressLine.substring(MarketplaceomsordersConstants.ZERO_INT,
					MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE + 1).lastIndexOf(MarketplaceomsordersConstants.SPACE);
			addrLine1 = addressLine.substring(MarketplaceomsordersConstants.ZERO_INT, pointer);

			addrLine1 = addrLine1.trim();
			addressLine = addressLine.substring(pointer + 1, addressLine.length());

			addrLine2 = addressLine;
			addrLine2 = addrLine2.trim();

			if (addrLine2.length() > MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
			{
				pointer = addressLine.substring(MarketplaceomsordersConstants.ZERO_INT,
						MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE + 1).lastIndexOf(MarketplaceomsordersConstants.SPACE);

				addrLine2 = addressLine.substring(MarketplaceomsordersConstants.ZERO_INT, pointer).trim();

				addressLine = addressLine.substring(pointer + 1, addressLine.length()).trim();

				if (addressLine.length() > MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
				{
					addrLine3 = addressLine.substring(MarketplaceomsordersConstants.ZERO_INT,
							MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE).trim();
				}
				else
				{
					addrLine3 = addressLine.trim();
				}
			}
		}

		target.setAddressLine1(addrLine1);
		target.setAddressLine2(addrLine2);
		target.setAddressLine3(addrLine3);

		//TPR-3402 ends

		//target.setAddressLine1(source.getLine1() != null ? source.getLine1() : source.getStreetname());
		//target.setAddressLine2(source.getLine2() != null ? source.getLine2() : source.getStreetnumber());
		//	target.setAddressLine3(source.getAddressLine3());
		//target.setPostalZone(source.getPostalcode());
		target.setCityName(source.getTown());
		target.setPinCode(source.getPostalcode());
		target.setLandmark(source.getLandmark());

		if (StringUtil.isNotEmpty(source.getDistrict()))
		{
			final StateModel stateModel = mplCommerceCartService.fetchStateDetails(source.getDistrict());
			if (stateModel != null)
			{
				target.setStateCode(stateModel.getRegion() != null ? stateModel.getRegion() : stateModel.getDescription());
			}
			else
			{
				target.setStateCode(source.getDistrict());
			}
		}

		target.setPhoneNumber(source.getPhone1() != null ? source.getPhone1() : source.getPhone2());
		if (source.getCountry() != null)
		{
			target.setCountryName(source.getCountry().getName());
			target.setCountryIso3166Alpha2Code(source.getCountry().getIsocode());
			target.setCountryCode(source.getCountry().getIsocode());
		}
		if (source.getRegion() != null)
		{
			target.setCountrySubentity(source.getRegion().getIsocodeShort());
		}

		target.setLatitudeValue(null);
		target.setLongitudeValue(null);
		target.setName(getCustomerNameStrategy().getName(source.getFirstname(), source.getLastname()));
	}

	public CustomerNameStrategy getCustomerNameStrategy()
	{
		return this.customerNameStrategy;
	}

	@Required
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}

	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}


}