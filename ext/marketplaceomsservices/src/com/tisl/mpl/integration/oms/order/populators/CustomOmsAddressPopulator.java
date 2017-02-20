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
	private CustomerNameStrategy customerNameStrategy;

	@Autowired
	private MplCommerceCartService mplCommerceCartService;

	private int updatePointer(final StringBuilder addressLine)
	{
		int pointer = MarketplaceomsordersConstants.ZERO_INT;
		if (addressLine.length() > MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE
				&& addressLine.length() <= MarketplaceomsordersConstants.MAX_LEN_OF_ADDR_LINE
				&& addressLine.toString().substring(0, MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE + 1)
						.contains(MarketplaceomsordersConstants.SINGLE_SPACE))
		{

			pointer = addressLine.toString().substring(0, MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE + 1)
					.lastIndexOf(MarketplaceomsordersConstants.SINGLE_SPACE);

		}
		return pointer;
	}

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

		StringBuilder addressLine = new StringBuilder();
		if (StringUtils.isNotEmpty(source.getLine1() != null ? source.getLine1() : source.getStreetname()))
		{
			addressLine = new StringBuilder(source.getLine1() != null ? source.getLine1() : source.getStreetname());
		}
		if (StringUtils.isNotEmpty(source.getLine2() != null ? source.getLine2() : source.getStreetnumber()))
		{
			final StringBuilder addressLine2 = new StringBuilder((source.getLine2() != null ? source.getLine2()
					: source.getStreetnumber()));
			addressLine = addressLine.append(MarketplaceomsordersConstants.SINGLE_SPACE).append(addressLine2);
		}
		if (StringUtils.isNotEmpty(source.getAddressLine3()))
		{
			final StringBuilder addressLine3 = new StringBuilder(source.getAddressLine3());
			addressLine = addressLine.append(MarketplaceomsordersConstants.SINGLE_SPACE).append(addressLine3.toString());
		}

		String addrLine1 = StringUtils.EMPTY;
		String addrLine2 = StringUtils.EMPTY;
		String addrLine3 = StringUtils.EMPTY;

		int pointer = updatePointer(addressLine);
		addressLine = new StringBuilder(addressLine.toString().trim());

		if (addressLine.length() <= MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
		{
			addrLine1 = addressLine.toString().trim();
			//addrLine2 = StringUtils.EMPTY;
			//addrLine3 = StringUtils.EMPTY;
		}
		else if (addressLine.length() <= MarketplaceomsordersConstants.MAX_LEN_OF_ADDR_LINE
				&& addressLine.toString()
						.substring(MarketplaceomsordersConstants.ZERO_INT, MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE + 1)
						.contains(MarketplaceomsordersConstants.SINGLE_SPACE))

		{

			addrLine1 = addressLine.toString().substring(MarketplaceomsordersConstants.ZERO_INT, pointer);

			//addrLine1 = addrLine1.trim();
			addressLine = new StringBuilder(addressLine.toString().substring(pointer + 1, addressLine.length()));

			addrLine2 = addressLine.toString();
			if (addrLine2.length() > MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE
					&& addrLine2.substring(MarketplaceomsordersConstants.ZERO_INT,
							MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE + 1)
							.contains(MarketplaceomsordersConstants.SINGLE_SPACE))
			{
				pointer = updatePointer(addressLine);

				addrLine2 = addressLine.toString().substring(MarketplaceomsordersConstants.ZERO_INT, pointer).trim();

				addressLine = new StringBuilder(addressLine.toString().substring(pointer + 1, addressLine.length()));

				if (addressLine.length() > MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
				{
					addrLine3 = addressLine.toString()
							.substring(MarketplaceomsordersConstants.ZERO_INT, MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
							.trim();
				}
				else
				{
					addrLine3 = addressLine.toString();
				}
			}
			if (addrLine2.length() > MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
			{
				addrLine2 = addressLine.toString()
						.substring(MarketplaceomsordersConstants.ZERO_INT, MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE).trim();
				addressLine = new StringBuilder(addressLine.toString().substring(MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE,
						addressLine.length()));
				if (addressLine.length() > MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
				{
					addrLine3 = addressLine.toString().substring(MarketplaceomsordersConstants.ZERO_INT,
							MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE);
				}
				else
				{
					addrLine3 = addressLine.toString();
				}
			}
		}
		else
		{
			addrLine1 = addressLine.toString()
					.substring(MarketplaceomsordersConstants.ZERO_INT, MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE).trim();
			if (addressLine.length() <= MarketplaceomsordersConstants.DOUBLE_MAX_LEN_PER_ADDR_LINE)
			{
				addrLine2 = addressLine.toString()
						.substring(MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE, addressLine.length()).trim();
			}
			if (addressLine.length() > MarketplaceomsordersConstants.DOUBLE_MAX_LEN_PER_ADDR_LINE
					&& addressLine.length() <= MarketplaceomsordersConstants.MAX_LEN_OF_ADDR_LINE
					&& addressLine
							.toString()
							.substring(MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE,
									MarketplaceomsordersConstants.DOUBLE_MAX_LEN_PER_ADDR_LINE + 1)
							.contains(MarketplaceomsordersConstants.SINGLE_SPACE))
			{
				pointer = addressLine
						.toString()
						.substring(MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE,
								MarketplaceomsordersConstants.DOUBLE_MAX_LEN_PER_ADDR_LINE + 1)
						.lastIndexOf(MarketplaceomsordersConstants.SINGLE_SPACE);
				addrLine2 = addressLine.toString().substring(MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE,
						MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE + pointer);
				addressLine = new StringBuilder(addressLine.substring(MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE + pointer
						+ 1, addressLine.length()));
				if (addressLine.length() > MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
				{
					addrLine3 = addressLine.toString()
							.substring(MarketplaceomsordersConstants.ZERO_INT, MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE)
							.trim();
				}
				else
				{
					addrLine3 = addressLine.toString();
				}
			}
			if (addressLine.length() > MarketplaceomsordersConstants.DOUBLE_MAX_LEN_PER_ADDR_LINE
					&& addressLine.length() <= MarketplaceomsordersConstants.MAX_LEN_OF_ADDR_LINE)
			{
				addrLine2 = addressLine.toString().substring(MarketplaceomsordersConstants.MAX_LEN_PER_ADDR_LINE,
						MarketplaceomsordersConstants.DOUBLE_MAX_LEN_PER_ADDR_LINE);
				addrLine3 = addressLine.toString().substring(MarketplaceomsordersConstants.DOUBLE_MAX_LEN_PER_ADDR_LINE,
						addressLine.length());
			}

		}
		target.setAddressLine1(addrLine1.trim());
		target.setAddressLine2(addrLine2.trim());
		target.setAddressLine3(addrLine3.trim());

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