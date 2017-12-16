/**
 *
 */
package com.tisl.lux.core.celldecorator;

import de.hybris.platform.util.CSVCellDecorator;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tisl.lux.constants.LuxurystoreaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


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

		final String luxurySaleIndicatorMapping = Config.getString("luxurySaleIndicatorMapping", "luxury-LSH,indiluxe-ISH");
		final Map<String, String> luxurySaleIndicatorMap = new HashMap<String, String>();
		for (final String actualElement : luxurySaleIndicatorMapping.split(MarketplacecommerceservicesConstants.COMMA))
		{
			luxurySaleIndicatorMap.put(actualElement.split(MarketplacecommerceservicesConstants.HYPHEN)[Integer
					.parseInt(MarketplacecommerceservicesConstants.KEY)], actualElement
					.split(MarketplacecommerceservicesConstants.HYPHEN)[Integer.parseInt(MarketplacecommerceservicesConstants.VALUE)]);
		}

		final String saleCategoryCode = luxurySaleIndicatorMap.get(luxIndicator);

		//if luxury remove MSH
		if (luxurySaleIndicatorMap.keySet().contains(luxIndicator))
		{
			for (final String category : categoryListLessInvertedcomma)
			{
				if (!category.contains(LuxurystoreaddonConstants.MSH))
				{
					if (luxurySaleIndicatorMap.values().contains(category.substring(0, 3)) && category.contains(saleCategoryCode))
					{
						finalCategoryList.add(category);
					}
					else if (!luxurySaleIndicatorMap.values().contains(category.substring(0, 3)))
					{
						finalCategoryList.add(category);
					}
				}
			}
		}
		return StringUtils.join(finalCategoryList, ',');
	}
}
