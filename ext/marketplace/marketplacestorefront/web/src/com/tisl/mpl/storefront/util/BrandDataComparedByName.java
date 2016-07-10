/**
 * 
 */
package com.tisl.mpl.storefront.util;

import de.hybris.platform.commercefacades.product.data.BrandData;

import java.util.Comparator;


/**
 * @author TCS
 * 
 */
public class BrandDataComparedByName implements Comparator<BrandData>
{
	@Override
	public int compare(final BrandData brandData1, final BrandData brandData2)
	{
		if (brandData1.getBrandname() != null && brandData2.getBrandname() != null)
		{
			return brandData1.getBrandname().compareTo(brandData2.getBrandname());
		}

		return 0;
	}


}
