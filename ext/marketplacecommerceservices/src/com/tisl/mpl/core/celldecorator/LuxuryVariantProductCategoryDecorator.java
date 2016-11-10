/**
 *
 */
package com.tisl.mpl.core.celldecorator;

import de.hybris.platform.util.CSVCellDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */
public class LuxuryVariantProductCategoryDecorator implements CSVCellDecorator
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

		final Integer position2 = new Integer(15);
		final String categories = srcLine.get(position);
		final String luxIndicator = srcLine.get(position2);

		final StringBuilder returnValue = new StringBuilder(200);


		//if Lux Indicator Is Blank By Default marketplace
		if (StringUtils.isBlank(luxIndicator) && StringUtils.isNotBlank(categories))
		{
			returnValue.append(stripCategories(categories, MarketplacecommerceservicesConstants.MARKETPLACE));

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
		if (luxIndicator.equalsIgnoreCase(MarketplacecommerceservicesConstants.LUXURY))
		{

			for (final String category : categoryListLessInvertedcomma)
			{
				if (!category.contains(MarketplacecommerceservicesConstants.MSH))
				{
					finalCategoryList.add(category);
				}

			}
		}
		//if marketplace remove LSH
		else if (luxIndicator.equalsIgnoreCase(MarketplacecommerceservicesConstants.MARKETPLACE))
		{
			for (final String category : categoryListLessInvertedcomma)
			{
				if (!category.contains(MarketplacecommerceservicesConstants.LSH))
				{
					finalCategoryList.add(category);
				}

			}

		}

		return StringUtils.join(finalCategoryList, ',');

	}
}
