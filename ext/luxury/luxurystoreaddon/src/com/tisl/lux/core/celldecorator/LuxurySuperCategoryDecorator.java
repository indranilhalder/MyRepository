/**
 *
 */
package com.tisl.lux.core.celldecorator;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.CSVCellDecorator;

import java.util.ArrayList;
import java.util.Map;

import com.tisl.lux.constants.LuxurystoreaddonConstants;


/**
 * @author manoj.girachh
 *
 */
public class LuxurySuperCategoryDecorator implements CSVCellDecorator
{


	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(LuxurystoreaddonConstants.CONFIGURATION_SER, ConfigurationService.class);
	}

	/*
	 * @Javadoc
	 * 
	 * @param position,srcLine
	 * 
	 * @return finalvalue
	 */
	@Override
	public String decorate(final int position, final Map<Integer, String> srcLine)
	{

		final int position2 = 1;
		final String parsedValue = srcLine.get(position);
		final String checkCategoryCode = srcLine.get(position2);

		if (!parsedValue.isEmpty() && !checkCategoryCode.isEmpty())
		{
			final String classificationCatalog = getConfigurationService().getConfiguration().getString(
					LuxurystoreaddonConstants.CLASSIFICATIONCATEGORYCATALOG);
			final String marketplaceCatalog = getConfigurationService().getConfiguration().getString(
					LuxurystoreaddonConstants.LUXURYCLASSIFICATIONCATALOG);

			final String primaryhierarchy = getConfigurationService().getConfiguration().getString(
					LuxurystoreaddonConstants.PRIMARYHIERARCHY);


			final ArrayList<String> finalList = new ArrayList<>();
			final String list[] = parsedValue.split(LuxurystoreaddonConstants.COMMA);

			//Check if it is a primary category
			if (checkCategoryCode.startsWith(primaryhierarchy))
			{
				for (final String category : list)
				{
					//check if its a primary hiercarchy or a classification category
					if (category.startsWith(primaryhierarchy))
					{

						finalList.add(category + marketplaceCatalog);

					}

					//for classification category
					else
					{
						finalList.add(category + classificationCatalog);
					}

				}

			}
			//for every other Category eg-MBH,MSH
			else
			{
				for (final String category : list)
				{

					finalList.add(category + marketplaceCatalog);

				}
			}
			//IF no matches are found return the same value
			if (finalList.isEmpty())
			{
				return parsedValue;
			}

			String finalvalue = "";
			for (final String temp : finalList)
			{
				if (finalvalue.isEmpty())
				{
					finalvalue = temp;

				}
				else
				{
					finalvalue = finalvalue + LuxurystoreaddonConstants.COMMA + temp;

				}
			}

			return finalvalue;
		}


		else
		{
			return parsedValue;
		}

	}

}
