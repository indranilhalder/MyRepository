/**
 *
 */
package com.tisl.mpl.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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




	/**
	 * This method recalculates cart or order. Changes incorporated for TPR-629
	 *
	 * @param parameter
	 */
	@Override
	public boolean recalculateCart(final CommerceCartParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		//Payment Changes
		final OrderModel orderModel = parameter.getOrder();
		try
		{
			parameter.setRecalculate(true);
			beforeCalculate(parameter);
			if (null != orderModel)
			{
				//Recalculation based on cartModel
				resetCartModel(orderModel);
				getCalculationService().recalculate(orderModel);
				getPromotionsService().updatePromotions(getPromotionGroups(), orderModel, true,
						PromotionsManager.AutoApplyMode.APPLY_ALL, PromotionsManager.AutoApplyMode.APPLY_ALL,
						getTimeService().getCurrentTime());
				resetNetAmtAftrAllDisc(orderModel);
			}
			else
			{
				//Recalculation based on orderModel
				resetCartModel(cartModel);
				getCalculationService().recalculate(cartModel);
				getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true,
						PromotionsManager.AutoApplyMode.APPLY_ALL, PromotionsManager.AutoApplyMode.APPLY_ALL,
						getTimeService().getCurrentTime());
				resetNetAmtAftrAllDisc(cartModel);
			}


		}
		catch (final CalculationException calculationException)
		{
			throw new IllegalStateException(String.format("Cart model %s was not calculated due to: %s ", new Object[]
			{ null != orderModel ? orderModel.getCode() : cartModel.getCode(), calculationException.getMessage() }));
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



	/**
	 * Resets Discount Attributes of Cart Model
	 *
	 * Method Modified for Performance Fix : TISPT-148
	 *
	 * @param abstractOrderModel
	 * @return AbstractOrderModel
	 */
	private AbstractOrderModel resetCartModel(final AbstractOrderModel abstractOrderModel) //Changed to abstractOrderModel for TPR-629
	{
		final List<AbstractOrderEntryModel> cartEntryList = new ArrayList<AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel cartEntry : abstractOrderModel.getEntries())
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

			//TPR-7408 starts here
			cartEntry.setPromoProductCostCentreOnePercentage(Double.valueOf(0.00D));
			cartEntry.setPromoProductCostCentreTwoPercentage(Double.valueOf(0.00D));
			cartEntry.setPromoProductCostCentreThreePercentage(Double.valueOf(0.00D));
			cartEntry.setPromoCartCostCentreOnePercentage(Double.valueOf(0.00D));
			cartEntry.setPromoCartCostCentreTwoPercentage(Double.valueOf(0.00D));
			cartEntry.setPromoCartCostCentreThreePercentage(Double.valueOf(0.00D));
			//TPR-7408 ends here

			//			cartEntry.setCouponCode("");
			//			cartEntry.setCouponValue(Double.valueOf(0.00D));

			//modelService.save(cartEntry); //Blocked for TISPT-148
			cartEntryList.add(cartEntry); // Code Added for TISPT-148
		}

		if (CollectionUtils.isNotEmpty(cartEntryList))
		{
			modelService.saveAll(cartEntryList);
		}

		modelService.refresh(abstractOrderModel);
		modelService.save(abstractOrderModel);
		return abstractOrderModel;
	}

	/**
	 * Reset Net amount after Discount for Coupon
	 *
	 * Modified for TISPT-148
	 *
	 * @param abstractOrderModel
	 */

	private void resetNetAmtAftrAllDisc(final AbstractOrderModel abstractOrderModel) //Changed to abstractOrderModel for TPR-629
	{
		// Code Added for TISPT-148
		final List<AbstractOrderEntryModel> cartEntryList = new ArrayList<AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
		{
			if (StringUtils.isNotEmpty(entry.getCouponCode()))
			{
				double currNetAmtAftrAllDisc = 0.00D;
				final double entryTotalPrice = entry.getTotalPrice().doubleValue();
				final double couponValue = entry.getCouponValue().doubleValue();

				if ((StringUtils.isNotEmpty(entry.getProductPromoCode())) || (StringUtils.isNotEmpty(entry.getCartPromoCode())))
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
				cartEntryList.add(entry);
				//getModelService().save(entry); // Code Blocked for TISPT-148
			}
		}

		// Code Added for TISPT-148
		if (CollectionUtils.isNotEmpty(cartEntryList))
		{
			getModelService().saveAll(cartEntryList);
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
