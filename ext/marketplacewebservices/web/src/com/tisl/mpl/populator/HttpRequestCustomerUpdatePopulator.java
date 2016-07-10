/**
 *
 */
package com.tisl.mpl.populator;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * @author TCS
 *
 */
/**
 * @description method is called to populate data from HttpServletRequest to Customer
 *
 */
@Component("httpRequestCustomerUpdatePopulator")
public class HttpRequestCustomerUpdatePopulator extends AbstractPopulatingConverter<HttpServletRequest, CustomerData>
{
	private static final String EMAILID = "Emailid";
	private static final String FIRSTNAME = "firstName";
	private static final String LASTNAME = "lastName";
	//	private static final String CHANNEL = "channel";
	//	private static final String CTOKEN = "cToken";
	private static final String MOBILENUMBER = "mobilenumber";

	@SuppressWarnings("deprecation")
	@Override
	protected CustomerData createTarget()
	{
		return new CustomerData();
	}

	@Override
	public void populate(final HttpServletRequest source, final CustomerData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (null != source && null != source.getParameter(EMAILID))
		{
			target.setEmail(source.getParameter(EMAILID));
		}
		if (null != source && null != source.getParameter(FIRSTNAME))
		{
			target.setFirstName(source.getParameter(FIRSTNAME));
		}
		if (null != source && null != source.getParameter(LASTNAME))
		{
			target.setLastName(source.getParameter(LASTNAME));
		}
		if (null != source && null != source.getParameter(MOBILENUMBER))
		{
			target.setContactNumber(source.getParameter(MOBILENUMBER));
		}
		target.setEmail(StringUtils.defaultString(source.getParameter(EMAILID), target.getEmail()));
		target.setFirstName(StringUtils.defaultString(source.getParameter(FIRSTNAME), target.getFirstName()));
		target.setLastName(StringUtils.defaultString(source.getParameter(LASTNAME), target.getLastName()));
		target.setContactNumber(StringUtils.defaultString(source.getParameter(MOBILENUMBER), target.getContactNumber()));

	}

}
