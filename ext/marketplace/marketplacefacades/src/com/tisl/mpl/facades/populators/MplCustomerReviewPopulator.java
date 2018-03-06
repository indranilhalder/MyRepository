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
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Converter implementation for {@link de.hybris.platform.customerreview.model.CustomerReviewModel} as source and
 * {@link de.hybris.platform.commercefacades.product.data.ReviewData} as target type.
 */
public class MplCustomerReviewPopulator implements Populator<CustomerReviewModel, ReviewData>
{
	private Converter<PrincipalModel, PrincipalData> principalConverter;
	@Autowired
	private UserService userService;


	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected Converter<PrincipalModel, PrincipalData> getPrincipalConverter()
	{
		return principalConverter;
	}

	@Required
	public void setPrincipalConverter(final Converter<PrincipalModel, PrincipalData> principalConverter)
	{
		this.principalConverter = principalConverter;
	}

	@Override
	public void populate(final CustomerReviewModel source, final ReviewData target)
	{

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final UserModel userModel = getUserService().getCurrentUser();

		target.setId(source.getPk().getLongValueAsString());
		target.setComment(source.getComment());
		target.setDate(source.getCreationtime());
		target.setHeadline(source.getHeadline());
		target.setRating(source.getRating());
		if (StringUtils.isNotBlank(source.getAlias()))
		{
			target.setAlias(source.getAlias());
		}
		else if (source.getUser() != null)
		{
			final CustomerModel customer = (CustomerModel) source.getUser();
			if (StringUtils.isNotBlank(customer.getFirstName()))
			{
				target.setAlias(customer.getFirstName());
			}
			else
			{
				target.setAlias(customer.getOriginalUid().substring(0, customer.getOriginalUid().indexOf("@")));
			}
		}

		if (!userModel.getUid().equals("anonymous") && source.getUser().equals(userModel))
		{
			target.setCanEditDelete(true);
		}
		else
		{
			target.setCanEditDelete(false);
		}

		target.setPrincipal(getPrincipalConverter().convert(source.getUser()));
	}
}