package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


public class UnregisteredUserRestriction extends GeneratedUnregisteredUserRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(UnregisteredUserRestriction.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item

		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}


	@Override
	protected String[] getMessageAttributeValues()
	{
		return new String[]
		{ Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive()), getEmailIds() };
	}

	protected String getEmailIds()
	{
		return super.getEmailList();
	}


	/**
	 *
	 * @Desc checks and applies voucher only to users present in the unregistered email list
	 * @param cart
	 * @return boolean
	 */
	@Override
	protected boolean isFulfilledInternal(final AbstractOrder cart)
	{
		boolean result = false;
		final Boolean positive = super.isPositive();

		//include  these unregistered email list  for the voucher applicability
		final ArrayList<String> emailIdList = new ArrayList<String>();
		final StringTokenizer emailIdListToken = new StringTokenizer(super.getEmailList(),
				MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);
		while (emailIdListToken.hasMoreTokens())
		{
			emailIdList.add(emailIdListToken.nextToken().trim());
		}

		final User user = cart.getUser();
		if (user instanceof Customer)
		{
			final Object originalUidObj = ((Customer) user).getProperty("originalUid");
			if (null != originalUidObj)
			{
				final String originalUid = originalUidObj.toString();
				if (positive.booleanValue())
				{
					if (CollectionUtils.isNotEmpty(emailIdList) && emailIdList.contains(originalUid))
					{
						result = true;
						LOG.debug("selected  'Valid' field in hmc is ::::" + positive.booleanValue() + "emailid list  size is"
								+ emailIdList.size());
					}
				}
				else
				{
					if (!emailIdList.contains(originalUid) || CollectionUtils.isEmpty(emailIdList))
					{
						result = true;
						LOG.debug("selected  'Valid' field in hmc is ::::" + positive.booleanValue() + "emailid list  size is"
								+ emailIdList.size());
					}
				}
			}
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.voucher.jalo.Restriction#isFulfilledInternal(de.hybris.platform.jalo.product.Product)
	 */
	@Override
	protected boolean isFulfilledInternal(final Product paramProduct)
	{
		// YTODO Auto-generated method stub
		return false;
	}
}
