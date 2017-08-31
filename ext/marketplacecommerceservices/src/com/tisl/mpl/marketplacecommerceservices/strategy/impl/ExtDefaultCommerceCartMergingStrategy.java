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
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.AccessDeniedException;

import com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
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

	@Resource(name = "exchangeGuideService")
	private ExchangeGuideService exchangeService;
	//SONAR FIX JEWELLERY
	//	@Resource(name = "commerceCartService")
	//	private MplCommerceCartService mplCommerceCartService;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;

	private static final String MOBILE = "MOBILE";
	//JEWELLERY CHANGES
	private static final String FINEJEWELLERY = "FineJewellery";

	//ENDS

	@Override
	public void mergeCarts(final CartModel fromCart, final CartModel toCart, final List<CommerceCartModification> modifications)
			throws CommerceCartMergingException
	{
		final UserModel currentUser = this.userService.getCurrentUser();
		boolean cartMerged = false;

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
			for (final AbstractOrderEntryModel entryFromCart : fromCart.getEntries())
			{
				//JEWELLERY CHANGES
				boolean jewelleryMarge = false;
				String fromCartUssid = entryFromCart.getSelectedUSSID();
				if (FINEJEWELLERY.equalsIgnoreCase(entryFromCart.getProduct().getProductCategoryType()))
				{
					final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(fromCartUssid);
					if (org.apache.commons.collections.CollectionUtils.isNotEmpty(jewelleryInfo))
					{
						fromCartUssid = jewelleryInfo.get(0).getPCMUSSID();
					}
				}
				//ENDS
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
					if (null != entryFromCart.getGiveAway() && !entryFromCart.getGiveAway().booleanValue())
					{
						isProductAddRequired = true;
					}
				}
				if (isProductAddRequired)
				{
					//JEWELLERY CHANGES
					for (final AbstractOrderEntryModel entryToCart : toCart.getEntries())
					{
						String toCartUssid = entryToCart.getSelectedUSSID();
						if (FINEJEWELLERY.equalsIgnoreCase(entryToCart.getProduct().getProductCategoryType()))
						{
							final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(toCartUssid);
							if (org.apache.commons.collections.CollectionUtils.isNotEmpty(jewelleryInfo))
							{
								toCartUssid = jewelleryInfo.get(0).getPCMUSSID();
							}
							if (fromCartUssid.equalsIgnoreCase(toCartUssid)
									&& FINEJEWELLERY.equalsIgnoreCase(entryToCart.getProduct().getProductCategoryType()))
							{
								jewelleryMarge = true;
							}
						}
						//ENDS

					}
					if (!jewelleryMarge)
					{

						final CommerceCartParameter newCartParameter = new CommerceCartParameter();

						newCartParameter.setEnableHooks(true);
						newCartParameter.setCart(toCart);
						newCartParameter.setProduct(entryFromCart.getProduct());
						newCartParameter.setPointOfService(entryFromCart.getDeliveryPointOfService());
						newCartParameter.setQuantity((entryFromCart.getQuantity() == null) ? 0L : entryFromCart.getQuantity()
								.longValue());
						newCartParameter.setUssid((null != entryFromCart.getSelectedUSSID()) ? entryFromCart.getSelectedUSSID() : "");
						newCartParameter.setUnit(entryFromCart.getUnit());
						newCartParameter.setCreateNewEntry(false);
						if (fromCart.getExchangeAppliedCart() != null && fromCart.getExchangeAppliedCart().booleanValue()
								&& StringUtils.isNotEmpty(entryFromCart.getExchangeId()))
						{
							newCartParameter.setExchangeParam(entryFromCart.getExchangeId());

						}

						//TPR-174

						cartMerged = true;
						//getModelService().save(toCart);
						mergeModificationToList(mplCommerceAddToCartStrategy.addToCart(newCartParameter), modifications);

					}


				}


			}
		}
		catch (final CommerceCartModificationException e)
		{
			throw new CommerceCartMergingException(e.getMessage(), e);
		}

		toCart.setCalculated(Boolean.FALSE);
		//TPR-174
		if (cartMerged)
		{
			toCart.setMerged(Boolean.TRUE);
		}
		getModelService().save(toCart);
		if (cartMerged)
		{
			final List<Integer> entryNumberList = new ArrayList<>();
			for (final AbstractOrderEntryModel entry : toCart.getEntries())
			{
				if (StringUtils.isNotEmpty(entry.getExchangeId()))
				{
					entryNumberList.add(entry.getEntryNumber());
				}

			}
			if (CollectionUtils.isNotEmpty(entryNumberList))
			{
				changeQuantitieswithExchangeOffer(toCart, entryNumberList);
				exchangeService.changeGuidforCartMerge(toCart);
			}
		}

		getModelService().remove(fromCart);
	}

	private void changeQuantitieswithExchangeOffer(final CartModel cartModel, final List<Integer> entryNumberList)
	{
		for (final Integer entryNumber : entryNumberList)
		{
			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			parameter.setEntryNumber(entryNumber.longValue());
			parameter.setQuantity(1);

			try
			{
				getCommerceCartService().updateQuantityForCartEntry(parameter);
			}
			catch (final CommerceCartModificationException e)
			{
				e.printStackTrace();
			}
		}

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