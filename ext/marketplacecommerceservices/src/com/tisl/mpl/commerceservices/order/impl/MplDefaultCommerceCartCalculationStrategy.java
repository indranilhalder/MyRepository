/**
 *
 */
package com.tisl.mpl.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;


/**
 * @author TCS
 *
 */

public class MplDefaultCommerceCartCalculationStrategy extends DefaultCommerceCartCalculationStrategy implements
		MplCommerceCartCalculationStrategy
{

	/*
	 * @Autowired private CalculationService calculationService;
	 */

	@Autowired
	private ModelService modelService;

	@Override
	public boolean calculateCart(final CommerceCartParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();

		ServicesUtil.validateParameterNotNull(cartModel, "Cart model cannot be null");

		final CalculationService calcService = getCalculationService();
		if (calcService.requiresCalculation(cartModel))
		{
			try
			{
				parameter.setRecalculate(false);
				beforeCalculate(parameter);
				//Todo resetting of
				resetCartModel(cartModel);
				calcService.calculate(cartModel);
				getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true,
						PromotionsManager.AutoApplyMode.APPLY_ALL, PromotionsManager.AutoApplyMode.APPLY_ALL,
						getTimeService().getCurrentTime());
				resetNetAmtAftrAllDisc(cartModel);
			}
			catch (final CalculationException calculationException)
			{
				throw new IllegalStateException("Cart model " + cartModel.getCode() + " was not calculated due to: "
						+ calculationException.getMessage());
			}
			finally
			{
				afterCalculate(parameter);
			}
			return true;
		}
		return false;
	}




	@Override
	public boolean recalculateCart(final CommerceCartParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		try
		{
			parameter.setRecalculate(true);
			beforeCalculate(parameter);
			resetCartModel(cartModel);
			getCalculationService().recalculate(cartModel);
			getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true,
					PromotionsManager.AutoApplyMode.APPLY_ALL, PromotionsManager.AutoApplyMode.APPLY_ALL,
					getTimeService().getCurrentTime());
			resetNetAmtAftrAllDisc(cartModel);
		}
		catch (final CalculationException calculationException)
		{
			throw new IllegalStateException(String.format("Cart model %s was not calculated due to: %s ", new Object[]
			{ cartModel.getCode(), calculationException.getMessage() }));
		}
		finally
		{
			afterCalculate(parameter);
		}

		return true;
	}

	@Override
	protected void beforeCalculate(final CommerceCartParameter parameter)
	{
		if ((getCommerceCartCalculationMethodHooks() == null)
				|| (!(parameter.isEnableHooks()))
				|| (!(getConfigurationService().getConfiguration().getBoolean(
						"commerceservices.commercecartcalculationmethodhook.enabled", true))))
		{
			return;
		}
		for (final CommerceCartCalculationMethodHook commerceCartCalculationMethodHook : getCommerceCartCalculationMethodHooks())
		{
			commerceCartCalculationMethodHook.beforeCalculate(parameter);
		}
	}

	@Override
	protected void afterCalculate(final CommerceCartParameter parameter)
	{
		if ((getCommerceCartCalculationMethodHooks() == null)
				|| (!(parameter.isEnableHooks()))
				|| (!(getConfigurationService().getConfiguration().getBoolean(
						"commerceservices.commercecartcalculationmethodhook.enabled", true))))
		{
			return;
		}
		for (final CommerceCartCalculationMethodHook commerceCartCalculationMethodHook : getCommerceCartCalculationMethodHooks())
		{
			commerceCartCalculationMethodHook.afterCalculate(parameter);
		}
	}



	private CartModel resetCartModel(final CartModel cartModel)
	{
		for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
		{
			cartEntry.setQualifyingCount(Integer.valueOf(0));
			cartEntry.setFreeCount(Integer.valueOf(0));
			cartEntry.setAssociatedItems(Collections.<String> emptyList());
			cartEntry.setCartLevelDisc(Double.valueOf(0.00D));
			cartEntry.setTotalSalePrice(Double.valueOf(0.00D));
			cartEntry.setNetSellingPrice(Double.valueOf(0.00D));
			cartEntry.setIsBOGOapplied(Boolean.FALSE);
			cartEntry.setProdLevelPercentageDisc(Double.valueOf(0.00D));
			cartEntry.setCartLevelPercentageDisc(Double.valueOf(0.00D));
			cartEntry.setNetAmountAfterAllDisc(Double.valueOf(0.00D));
			cartEntry.setProductPromoCode("");
			cartEntry.setCartPromoCode("");
			cartEntry.setIsPercentageDisc(Boolean.FALSE);
			cartEntry.setTotalProductLevelDisc(Double.valueOf(0.00D));
			//			cartEntry.setCouponCode("");
			//			cartEntry.setCouponValue(Double.valueOf(0.00D));
			modelService.save(cartEntry);
		}

		modelService.refresh(cartModel);
		modelService.save(cartModel);
		return cartModel;
	}

	private void resetNetAmtAftrAllDisc(final CartModel cartModel)
	{
		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			if (null != entry.getCouponCode() && !entry.getCouponCode().isEmpty())
			{
				double currNetAmtAftrAllDisc = 0.00D;
				final double entryTotalPrice = entry.getTotalPrice().doubleValue();
				final double couponValue = entry.getCouponValue().doubleValue();

				if ((null != entry.getProductPromoCode() && !entry.getProductPromoCode().isEmpty())
						|| (null != entry.getCartPromoCode() && !entry.getCartPromoCode().isEmpty()))
				{
					final double netAmtAftrAllDisc = entry.getNetAmountAfterAllDisc() != null ? entry.getNetAmountAfterAllDisc()
							.doubleValue() : 0.00D;
					currNetAmtAftrAllDisc = netAmtAftrAllDisc - couponValue;
				}
				else
				{
					currNetAmtAftrAllDisc = entryTotalPrice - couponValue;
				}
				entry.setNetAmountAfterAllDisc(Double.valueOf(currNetAmtAftrAllDisc));
				getModelService().save(entry);
			}

		}
	}




	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}




	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
