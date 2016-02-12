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
package com.tisl.mpl.mapping.mappers;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercewebservicescommons.dto.order.CardTypeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.mapping.mappers.AbstractCustomMapper;

import ma.glasnost.orika.MappingContext;


/**
 *   
 */
public class CCPaymentInfoDataMapper extends AbstractCustomMapper<CCPaymentInfoData, PaymentDetailsWsDTO>
{
	private static final String CARD_TYPE="cardType";
	@Override
	public void mapAtoB(final CCPaymentInfoData a, final PaymentDetailsWsDTO b, final MappingContext context)
	{
		// other fields are mapped automatically
		mapCartTypeAtoB(a, b, context);
		mapdDefaultPaymentAtoB(a, b, context);
	}

	protected void mapCartTypeAtoB(final CCPaymentInfoData a, final PaymentDetailsWsDTO b, final MappingContext context)
	{
		context.beginMappingField(CARD_TYPE, getAType(), a, CARD_TYPE, getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				if (a.getCardTypeData() != null && a.getCardTypeData().getCode() != null)
				{
					b.setCardType(mapperFacade.map(a.getCardTypeData(), CardTypeWsDTO.class, context));
				}
				else if (a.getCardType() != null)
				{
					final CardTypeWsDTO cardType = new CardTypeWsDTO();
					cardType.setCode(a.getCardType());
					b.setCardType(cardType);
				}
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapdDefaultPaymentAtoB(final CCPaymentInfoData a, final PaymentDetailsWsDTO b, final MappingContext context)
	{
		context.beginMappingField("defaultPaymentInfo", getAType(), a, "defaultPayment", getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				if (a.isDefaultPaymentInfo())
				{
					b.setDefaultPayment(Boolean.TRUE);
				}
				else
				{
					b.setDefaultPayment(Boolean.FALSE);
				}
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final PaymentDetailsWsDTO b, final CCPaymentInfoData a, final MappingContext context)
	{
		// other fields are mapped automatically

		mapCartTypeBtoA(b, a, context);
		mapDefaultPaymentBtoA(b, a, context);
	}

	protected void mapCartTypeBtoA(final PaymentDetailsWsDTO b, final CCPaymentInfoData a, final MappingContext context)
	{
		context.beginMappingField(CARD_TYPE, getBType(), b, CARD_TYPE, getAType(), a);
		try
		{
			if (shouldMap(b, a, context))
			{
				if (b.getCardType() != null)
				{
					a.setCardType(b.getCardType().getCode());
					a.setCardTypeData(mapperFacade.map(b.getCardType(), CardTypeData.class, context));
				}
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapDefaultPaymentBtoA(final PaymentDetailsWsDTO b, final CCPaymentInfoData a, final MappingContext context)
	{
		context.beginMappingField("defaultPayment", getBType(), b, "defaultPaymentInfo", getAType(), a);
		try
		{
			if (shouldMap(b, a, context))
			{
				if (b.getDefaultPayment() != null)
				{
					a.setDefaultPaymentInfo(b.getDefaultPayment().booleanValue());
				}
			}
		}
		finally
		{
			context.endMappingField();
		}
	}
}
