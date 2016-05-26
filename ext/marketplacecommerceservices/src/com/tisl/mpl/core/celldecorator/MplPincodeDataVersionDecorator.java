/**
 *
 */
package com.tisl.mpl.core.celldecorator;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.CSVCellDecorator;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */
public class MplPincodeDataVersionDecorator implements CSVCellDecorator
{

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(MarketplacecommerceservicesConstants.CONFIGURATION_SER,
				ConfigurationService.class);
	}

	/**
	 * @Description For Version Data Population of Pincode Data
	 * @param position
	 * @param srcLine
	 */
	@Override
	public String decorate(final int position, final Map<Integer, String> srcLine)
	{
		/*String parsedValue = srcLine.get(Integer.valueOf(position));
		final String input = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.PINCODE_DATA_UPLOAD_VERSION, MarketplacecommerceservicesConstants.EMPTY);
		if (StringUtils.isNotEmpty(input))
		{
			parsedValue = input;
		}*/
		return "a";
	}

}
