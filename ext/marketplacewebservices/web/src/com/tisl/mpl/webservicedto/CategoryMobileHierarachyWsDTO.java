/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.webservicedto;

import java.util.List;


/**
 *
 */
@SuppressWarnings(
{ "PMD" })
public class CategoryMobileHierarachyWsDTO
{

	private String category_id;
	private String category_name;
	List<CategoryMobileHierarachyWsDTO> sub_category_list;

	/**
	 * @return the category_id
	 */
	public String getCategory_id()
	{
		return category_id;
	}

	/**
	 * @param category_id
	 *           the category_id to set
	 */
	public void setCategory_id(final String category_id)
	{
		this.category_id = category_id;
	}

	/**
	 * @return the category_name
	 */
	public String getCategory_name()
	{
		return category_name;
	}

	/**
	 * @param category_name
	 *           the category_name to set
	 */
	public void setCategory_name(final String category_name)
	{
		this.category_name = category_name;
	}

	/**
	 * @return the sub_category_list
	 */
	public List<CategoryMobileHierarachyWsDTO> getSub_category_list()
	{
		return sub_category_list;
	}

	/**
	 * @param sub_category_list
	 *           the sub_category_list to set
	 */
	public void setSub_category_list(final List<CategoryMobileHierarachyWsDTO> sub_category_list)
	{
		this.sub_category_list = sub_category_list;
	}

}
