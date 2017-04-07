/**
 *
 */
package com.tisl.mpl.facades.converter.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.CategoryPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;


/**
 * @author TCS
 *
 */

public class MplCategoryPopulator extends CategoryPopulator
{
	//private Converter<CategoryModel, CategoryData> categoryConverter;

	private Converter<CategoryModel, CategoryData> mplSubSuperCategoryConverter;

	@Override
	public void populate(final CategoryModel source, final CategoryData target)
	{
		super.populate(source, target);
		populateSuperCategories(source.getAllSupercategories(), target);
		populateSubCategories(source.getAllSubcategories(), target);
	}

	/**
	 * @param allSubcategories
	 * @param target
	 */
	private void populateSubCategories(final Collection<CategoryModel> allSubcategories, final CategoryData target)
	{
		target.setSubCategories(Converters.convertAll(allSubcategories, getMplSubSuperCategoryConverter()));
	}

	/**
	 * @param allSupercategories
	 * @param target
	 */
	private void populateSuperCategories(final Collection<CategoryModel> allSupercategories, final CategoryData target)
	{
		target.setSuperCategories(Converters.convertAll(allSupercategories, getMplSubSuperCategoryConverter()));
	}

	/**
	 * @return the mplSubSuperCategoryConverter
	 */
	public Converter<CategoryModel, CategoryData> getMplSubSuperCategoryConverter()
	{
		return mplSubSuperCategoryConverter;
	}

	/**
	 * @param mplSubSuperCategoryConverter
	 *           the mplSubSuperCategoryConverter to set
	 */
	public void setMplSubSuperCategoryConverter(final Converter<CategoryModel, CategoryData> mplSubSuperCategoryConverter)
	{
		this.mplSubSuperCategoryConverter = mplSubSuperCategoryConverter;
	}


}
