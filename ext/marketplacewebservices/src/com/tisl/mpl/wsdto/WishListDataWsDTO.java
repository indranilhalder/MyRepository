/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at May 30, 2015 7:06:18 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;


public class WishListDataWsDTO implements java.io.Serializable
{

	/**
	 * <i>Generated property</i> for <code>WishListDataWsDTO.name</code> property defined at extension
	 * <code>marketplacewebservices</code>.
	 */
	private String name;
	/**
	 * <i>Generated property</i> for <code>WishListDataWsDTO.count</code> property defined at extension
	 * <code>marketplacewebservices</code>.
	 */
	private int count;
	/**
	 * <i>Generated property</i> for <code>WishListDataWsDTO.products</code> property defined at extension
	 * <code>marketplacewebservices</code>.
	 */
	private List<WishListProductWsDTO> products;


	public void setName(final String name)
	{
		this.name = name;
	}


	public String getName()
	{
		return name;
	}


	public void setCount(final int i)
	{
		this.count = i;
	}


	public int getCount()
	{
		return count;
	}


	public void setProducts(final List<WishListProductWsDTO> products)
	{
		this.products = products;
	}


	public List<WishListProductWsDTO> getProducts()
	{
		return products;
	}


}
