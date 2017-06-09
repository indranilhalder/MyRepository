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
package com.tisl.mpl.service;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.wsdto.GetWishListProductWsDTO;


@UnitTest
public class MplCartWebServiceImplTest
{
	@Resource
	private MplCartWebService mplCartWebService;

	@Before
	public void setUp() throws ParseException
	{
		MockitoAnnotations.initMocks(this);
		this.mplCartWebService = Mockito.mock(MplCartWebService.class);
	}

	@Test
	public void testProductDetails()
	{
		final AbstractOrderModel abstractOrderModel = Mockito.mock(CartModel.class);
		final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		final boolean isPinCodeCheckRequired = true;
		final boolean resetRequired = true;
		final List<GetWishListProductWsDTO> listProducts = new ArrayList<GetWishListProductWsDTO>();
		final List<PinCodeResponseData> pincodeList = new ArrayList<PinCodeResponseData>();
		final String pincode = "";

		Mockito.when(
				mplCartWebService.productDetails(abstractOrderModel, deliveryModeDataMap, isPinCodeCheckRequired, resetRequired,
						pincodeList, pincode)).thenReturn(listProducts);
	}
}
