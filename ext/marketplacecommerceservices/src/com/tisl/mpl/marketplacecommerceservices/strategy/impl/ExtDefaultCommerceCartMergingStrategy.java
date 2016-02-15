/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy.impl;

import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartMergingStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.AccessDeniedException;

import com.tisl.mpl.marketplacecommerceservices.strategy.ExtCommerceCartMergingStrategy;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplCommerceAddToCartStrategy;


/**
 * @author TCS
 *
 */
public class ExtDefaultCommerceCartMergingStrategy extends DefaultCommerceCartMergingStrategy implements
		ExtCommerceCartMergingStrategy
{
	@Resource
	private UserService userService;
	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private BaseSiteService baseSiteService;
	@Autowired
	private MplCommerceAddToCartStrategy mplCommerceAddToCartStrategy;

	private static final String MOBILE = "MOBILE";

	@Override
	public void mergeCarts(final CartModel fromCart, final CartModel toCart, final List<CommerceCartModification> modifications)
			throws CommerceCartMergingException
	{
		final UserModel currentUser = this.userService.getCurrentUser();

		if ((currentUser == null) || (this.userService.isAnonymousUser(currentUser)))
		{
			throw new AccessDeniedException("Only logged user can merge carts!");
		}

		ServicesUtil.validateParameterNotNull(fromCart, "fromCart can not be null");
		ServicesUtil.validateParameterNotNull(toCart, "toCart can not be null");

		if (!(getBaseSiteService().getCurrentBaseSite().equals(fromCart.getSite())))
		{
			throw new CommerceCartMergingException(String.format("Current site %s is not equal to cart %s site %s", new Object[]
			{ getBaseSiteService().getCurrentBaseSite(), fromCart, fromCart.getSite() }));
		}

		if (!(getBaseSiteService().getCurrentBaseSite().equals(toCart.getSite())))
		{
			throw new CommerceCartMergingException(String.format("Current site %s is not equal to cart %s site %s", new Object[]
			{ getBaseSiteService().getCurrentBaseSite(), toCart, toCart.getSite() }));
		}

		if (fromCart.getGuid().equals(toCart.getGuid()))
		{
			throw new CommerceCartMergingException("Cannot merge cart to itself!");
		}

		try
		{
			for (final AbstractOrderEntryModel entry : fromCart.getEntries())
			{
				boolean isProductAddRequired = false;
				if (null != fromCart.getChannel() && StringUtils.isNotEmpty(fromCart.getChannel().getCode())
						&& !fromCart.getChannel().getCode().equalsIgnoreCase(MOBILE))
				{
					//For other channels add as it is
					isProductAddRequired = true;
				}
				else
				{
					// freebie addition as normal product mobile issue fix TISEE-915
					if (null != entry.getGiveAway() && !entry.getGiveAway().booleanValue())
					{
						isProductAddRequired = true;
					}
				}
				if (isProductAddRequired)
				{
					final CommerceCartParameter newCartParameter = new CommerceCartParameter();
					newCartParameter.setEnableHooks(true);
					newCartParameter.setCart(toCart);
					newCartParameter.setProduct(entry.getProduct());
					newCartParameter.setPointOfService(entry.getDeliveryPointOfService());
					newCartParameter.setQuantity((entry.getQuantity() == null) ? 0L : entry.getQuantity().longValue());
					newCartParameter.setUssid((null != entry.getSelectedUSSID()) ? entry.getSelectedUSSID() : "");
					newCartParameter.setUnit(entry.getUnit());
					newCartParameter.setCreateNewEntry(false);

					mergeModificationToList(mplCommerceAddToCartStrategy.addToCart(newCartParameter), modifications);
				}
			}
		}
		catch (final CommerceCartModificationException e)
		{
			throw new CommerceCartMergingException(e.getMessage(), e);
		}

		toCart.setCalculated(Boolean.FALSE);

		getModelService().save(toCart);
		getModelService().remove(fromCart);
	}

	private void mergeModificationToList(final CommerceCartModification modificationToAdd,
			final List<CommerceCartModification> toModificationList)
	{
		if (modificationToAdd.getEntry().getPk() != null)
		{
			for (final CommerceCartModification finalModification : toModificationList)
			{
				if (finalModification.getEntry().getPk() == null)
				{
					continue;
				}

				if (!(finalModification.getEntry().getPk().equals(modificationToAdd.getEntry().getPk())))
				{
					continue;
				}
				finalModification.setQuantity(finalModification.getQuantity() + modificationToAdd.getQuantity());
				finalModification.setQuantityAdded(finalModification.getQuantityAdded() + modificationToAdd.getQuantityAdded());
				finalModification
						.setStatusCode(mergeStatusCodes(modificationToAdd.getStatusCode(), finalModification.getStatusCode()));
				return;
			}

		}

		toModificationList.add(modificationToAdd);
	}

	private String mergeStatusCodes(final String statusCode, final String statusCode1)
	{
		if ("success".equals(statusCode))
		{
			return statusCode1;
		}

		return statusCode;
	}


	@Override
	public UserService getUserService()
	{
		return this.userService;
	}


	@Override
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	@Override
	public CommerceCartService getCommerceCartService()
	{
		return this.commerceCartService;
	}

	@Override
	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}


	@Override
	public BaseSiteService getBaseSiteService()
	{
		return this.baseSiteService;
	}


	@Override
	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the mplCommerceAddToCartStrategy
	 */
	public MplCommerceAddToCartStrategy getMplCommerceAddToCartStrategy()
	{
		return mplCommerceAddToCartStrategy;
	}

	/**
	 * @param mplCommerceAddToCartStrategy
	 *           the mplCommerceAddToCartStrategy to set
	 */
	public void setMplCommerceAddToCartStrategy(final MplCommerceAddToCartStrategy mplCommerceAddToCartStrategy)
	{
		this.mplCommerceAddToCartStrategy = mplCommerceAddToCartStrategy;
	}






}
