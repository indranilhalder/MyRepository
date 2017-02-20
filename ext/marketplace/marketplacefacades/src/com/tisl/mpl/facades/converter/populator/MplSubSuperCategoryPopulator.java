/**
 *
 */
package com.tisl.mpl.facades.converter.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;


/**
 * @author TCS
 *
 */
public class MplSubSuperCategoryPopulator implements Populator<CategoryModel, CategoryData>
{

	private Converter<MediaModel, ImageData> imageConverter;
	private UrlResolver<CategoryModel> categoryModelUrlResolver;

	@Override
	public void populate(final CategoryModel source, final CategoryData target) throws ConversionException
	{
		addCategoryData(source, target);
		populateImages(source.getLogo(), target);
		populateUrlDetails(source, target);
	}

	/**
	 * @param source
	 * @param target
	 */
	private void populateUrlDetails(final CategoryModel source, final CategoryData target)
	{
		target.setUrl(getCategoryModelUrlResolver().resolve(source));
	}

	/**
	 * @param logo
	 * @param target
	 */
	private void populateImages(final Collection<MediaModel> logo, final CategoryData target)
	{
		if (CollectionUtils.isNotEmpty(logo))
		{
			target.setImage(getImageConverter().convert(logo.iterator().next()));
		}
	}

	/**
	 * @param source
	 * @param target
	 */
	private void addCategoryData(final CategoryModel source, final CategoryData target)
	{
		target.setCode(source.getCode());
		target.setDescription(source.getDescription());
		target.setName(source.getName());

	}

	/**
	 * @return the imageConverter
	 */
	public Converter<MediaModel, ImageData> getImageConverter()
	{
		return imageConverter;
	}

	/**
	 * @param imageConverter
	 *           the imageConverter to set
	 */
	public void setImageConverter(final Converter<MediaModel, ImageData> imageConverter)
	{
		this.imageConverter = imageConverter;
	}

	/**
	 * @return the categoryModelUrlResolver
	 */
	public UrlResolver<CategoryModel> getCategoryModelUrlResolver()
	{
		return categoryModelUrlResolver;
	}

	/**
	 * @param categoryModelUrlResolver
	 *           the categoryModelUrlResolver to set
	 */
	public void setCategoryModelUrlResolver(final UrlResolver<CategoryModel> categoryModelUrlResolver)
	{
		this.categoryModelUrlResolver = categoryModelUrlResolver;
	}

}
