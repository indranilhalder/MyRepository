/**
 *
 */
package com.tisl.mpl.storefront.web.data;

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.AutocompleteResultData;

import java.util.List;


/**
 * @author TCS
 *
 */
public class MplAutocompleteResultData extends AutocompleteResultData
{
	private List<CategoryData> categories;

	private List<ProductData> productNames;

	private String searchTerm;

	private List<CategoryData> brands;

	private List<CategoryData> seller;

	/**
	 * @return the searchTerm
	 */
	public String getSearchTerm()
	{
		return searchTerm;
	}

	/**
	 * @param searchTerm
	 *           the searchTerm to set
	 */
	public void setSearchTerm(final String searchTerm)
	{
		this.searchTerm = searchTerm;
	}

	/**
	 * @return the productNames
	 */
	public List<ProductData> getProductNames()
	{
		return productNames;
	}

	/**
	 * @param productNames
	 *           the productNames to set
	 */
	public void setProductNames(final List<ProductData> productNames)
	{
		this.productNames = productNames;
	}

	/**
	 * @return the categories
	 */
	public List<CategoryData> getCategories()
	{
		return categories;
	}

	/**
	 * @param categories
	 *           the categories to set
	 */
	public void setCategories(final List<CategoryData> categories)
	{
		this.categories = categories;
	}

	/**
	 * @return the brands
	 */
	public List<CategoryData> getBrands()
	{
		return brands;
	}

	/**
	 * @param brands
	 *           the brands to set
	 */
	public void setBrands(final List<CategoryData> brands)
	{
		this.brands = brands;
	}

	/**
	 * @return the seller
	 */
	public List<CategoryData> getSeller()
	{
		return seller;
	}

	/**
	 * @param list
	 *           the seller to set
	 */
	public void setSeller(final List<CategoryData> list)
	{
		this.seller = list;
	}

}
