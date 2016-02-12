/**
 *
 */
package com.tisl.mpl.wsdto;

import de.hybris.platform.commercewebservicescommons.dto.product.ProductListWsDTO;



/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class ProductForCategoryWsDTO
{

	private String category_type;
	private String banner_image;
	private String title;
	private ProductListWsDTO productList;

	/**
	 * @return the category_type
	 */
	public String getCategory_type()
	{
		return category_type;
	}

	/**
	 * @param category_type
	 *           the category_type to set
	 */
	public void setCategory_type(final String category_type)
	{
		this.category_type = category_type;
	}

	/**
	 * @return the banner_image
	 */
	public String getBanner_image()
	{
		return banner_image;
	}

	/**
	 * @param banner_image
	 *           the banner_image to set
	 */
	public void setBanner_image(final String banner_image)
	{
		this.banner_image = banner_image;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *           the title to set
	 */
	public void setTitle(final String title)
	{
		this.title = title;
	}

	/**
	 * @return the productList
	 */
	public ProductListWsDTO getProductList()
	{
		return productList;
	}

	/**
	 * @param productList
	 *           the productList to set
	 */
	public void setProductList(final ProductListWsDTO productList)
	{
		this.productList = productList;
	}


}
