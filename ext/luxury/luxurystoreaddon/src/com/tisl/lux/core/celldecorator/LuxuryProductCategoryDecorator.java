/**
 *
 */
package com.tisl.lux.core.celldecorator;

import de.hybris.platform.util.CSVCellDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tisl.lux.constants.LuxurystoreaddonConstants;


/**
 * @author TCS
 *
 */
public class LuxuryProductCategoryDecorator implements CSVCellDecorator
{

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

		//final Integer position2 = new Integer(12);
		final Integer position2 = Integer.valueOf(12); //Sonar fix

		final String categories = srcLine.get(position);
		final String luxIndicator = srcLine.get(position2);

		final StringBuilder returnValue = new StringBuilder(200);


		//if Lux Indicator Is Blank By Default marketplace
		if (StringUtils.isBlank(luxIndicator) && StringUtils.isNotBlank(categories))
		{
			returnValue.append(stripCategories(categories, LuxurystoreaddonConstants.MARKETPLACE));

		}
		//
		else if (StringUtils.isNotBlank(luxIndicator) && StringUtils.isNotBlank(categories))
		{
			returnValue.append(stripCategories(categories, luxIndicator));
		}
		else
		{
			returnValue.append(categories);
		}

		return returnValue.toString();
	}

	String stripCategories(final String categoryList, final String luxIndicator)
	{
		final List<String> finalCategoryList = new ArrayList<>();
		//Splitting Comma Separted Entries into Array
		final String[] categoryListLessInvertedcomma = categoryList.split(",");

		//if luxury remove MSH
		if (luxIndicator.equalsIgnoreCase(LuxurystoreaddonConstants.LUXURY))
		{

			for (final String category : categoryListLessInvertedcomma)
			{
				if (!category.contains(LuxurystoreaddonConstants.MSH))
				{
					finalCategoryList.add(category);
				}

			}
		}

		return StringUtils.join(finalCategoryList, ',');

	}
}
