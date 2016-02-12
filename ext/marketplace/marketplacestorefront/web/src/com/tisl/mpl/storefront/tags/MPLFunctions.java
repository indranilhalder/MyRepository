/**
 *
 */
package com.tisl.mpl.storefront.tags;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.Collection;


/**
 * @author 361234
 *
 */
public class MPLFunctions
{


	/**
	 * JSP EL Function to get a primary Image for a Product in a specific format and index
	 *
	 * @param product
	 *           the product
	 * @param format
	 *           the desired format
	 * @param index
	 *           the desired index
	 * @return the image
	 */
	public static ImageData getPrimaryImageForProductAndFormat(final ProductData product, final String format, final Integer index)
	{
		if (product != null && format != null && index != null)
		{
			final Collection<ImageData> images = product.getImages();
			if (images != null && !images.isEmpty())
			{
				final ImageData imageData = updateImageData(images, format, index);
				return imageData;
			}
		}
		return null;
	}

	/**
	 *
	 * @param images
	 * @param format
	 * @param index
	 * @return
	 */
	private static ImageData updateImageData(final Collection<ImageData> images, final String format, final Integer index)
	{
		if (images.toArray()[index] != null)
		{
			final ImageData imageData = (ImageData) images.toArray()[index];

			if (ImageDataType.PRIMARY.equals(imageData.getImageType()) && format.equals(imageData.getFormat()))
			{
				return imageData;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	public static String determineAgeBand(final Integer age)
	{
		final String ageBand;
		if (age != null)
		{
			if (age.intValue() == 1)
			{

				ageBand = "0 - 3 Months,3 - 6 Months,6 - 9 Months,9 - 12 Months";
			}
			else if (age.intValue() == 2)
			{

				ageBand = "12 - 18 Months,18 - 24 Months,1 - 2 Years";
			}
			else if (age.intValue() == 3)
			{

				ageBand = "2 - 3 Years,24 - 48 Months";
			}
			else if (age.intValue() == 4)
			{

				ageBand = "3 - 4 Years,24 - 48 Months";
			}
			else if (age.intValue() == 5)
			{

				ageBand = "4 - 5 Years";
			}
			else if (age.intValue() == 6)
			{

				ageBand = "5 - 6 Years";
			}
			else if (age.intValue() == 7)
			{

				ageBand = "6 -7 Years";
			}
			else if (age.intValue() == 8)
			{

				ageBand = "7 - 8 Years";
			}
			else if (age.intValue() == 9)
			{

				ageBand = "8 - 9 Years";
			}
			else if (age.intValue() == 10)
			{

				ageBand = "9 - 10 Years";
			}
			else if (age.intValue() == 11)
			{

				ageBand = "10 - 11 Years";
			}
			else if (age.intValue() == 12)
			{

				ageBand = "11 - 12 Years";
			}
			else if (age.intValue() == 13)
			{

				ageBand = "12 - 13 Years";
			}
			else if (age.intValue() == 14)
			{

				ageBand = "13 - 14 Years";
			}
			else if (age.intValue() >= 15 && age.intValue() <= 22)
			{

				ageBand = "16 to 22";
			}
			else if (age.intValue() > 22 && age.intValue() <= 35)
			{

				ageBand = "22-35";
			}
			else if (age.intValue() > 35 && age.intValue() <= 55)
			{

				ageBand = "35-55";
			}
			else
			{

				ageBand = "55+";
			}

			return ageBand;
		}
		return null;
	}
}

