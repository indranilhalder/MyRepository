/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at May 30, 2015 7:06:19 PM
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


public class WishListWsDTO implements java.io.Serializable
{

	/**
	 * <i>Generated property</i> for <code>WishListWsDTO.wishList</code> property defined at extension
	 * <code>marketplacewebservices</code>.
	 */
	private List<WishListDataWsDTO> wishList;
	/**
	 * <i>Generated property</i> for <code>WishListWsDTO.status</code> property defined at extension
	 * <code>marketplacewebservices</code>.
	 */
	private String status;


	public void setWishList(final List<WishListDataWsDTO> wishList)
	{
		this.wishList = wishList;
	}


	public List<WishListDataWsDTO> getWishList()
	{
		return wishList;
	}


	public void setStatus(final String status)
	{
		this.status = status;
	}


	public String getStatus()
	{
		return status;
	}


}