/**
 *
 */
package com.tisl.mpl.standardizationfactory;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.SizesystemModel;


/**
 * @author TCS
 *
 */
public class StandardizationServiceImpl implements StandardizationService
{

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private StandardizationDao sizeStandard;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.StandardizationFactory.StandardizationService#getStandardValue(java.lang.String)
	 */
	@Override
	public Double getStandardValue(final String stringValue, final String unitType)
	{
		final String globalConversionMemoryto = configurationService.getConfiguration().getString(
				"globalConversion." + unitType + ".to");
		final String globalConversionMemoryfrom = configurationService.getConfiguration().getString(
				"globalConversion." + unitType + ".from");
		//final String globalConversionLengthto = configurationService.getConfiguration().getString("globalConversion.length");
		Double finalValue = null;
		//Find if the values are in other units

		for (final String actualElement : globalConversionMemoryfrom.split(MarketplacecommerceservicesConstants.COMMA))
		{

			if (stringValue.toUpperCase().contains(actualElement.toUpperCase()))
			{
				finalValue = Double
						.valueOf((stringValue.substring(0, stringValue.toUpperCase().indexOf(actualElement.toUpperCase()))).trim());
				//Finds out the applicable conversion unit
				final Double conversionUnit = sizeStandard.findConversionUnit(globalConversionMemoryto, actualElement);

				return finalValue * conversionUnit;

			}
		}
		//if it needs no conversion
		if (stringValue.toUpperCase().contains(globalConversionMemoryto))
		{
			finalValue = Double.valueOf((stringValue.substring(0, stringValue.toUpperCase().indexOf(globalConversionMemoryto)))
					.trim());
			return finalValue;
		}

		return finalValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.StandardizationFactory.StandardizationService#getStandardValueNonNumeric(java.lang.String,
	 * boolean)
	 */
	@Override
	public String getStandardValueNonNumeric(final String stringValue, final String propertyName, final String unitType)
	{
		String finalValue = stringValue;
		final List<SizesystemModel> standardList = sizeStandard.getStandardValue(propertyName);

		for (final SizesystemModel single : standardList)
		{
			if (stringValue.toUpperCase().contains(single.getFrom().toUpperCase().trim()))
			{

				finalValue = single.getTo().trim();

			}

		}

		return finalValue;
	}
}