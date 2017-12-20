/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.voucher.model.VoucherModel;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class CouponCostCentreValidateInterceptor implements ValidateInterceptor
{

	private ModelService modelService;

	private static final String COST_CENTRE_PERCENTAGE = "Cost Centre Pecentage Attribution Total should be 100%";

	@Autowired
	private ConfigurationService configurationService;

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
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

	@Override
	public void onValidate(final Object object, final InterceptorContext arg1) throws InterceptorException
	{

		String errorMsg = null;


		final String costcentreflag = configurationService.getConfiguration().getString("promotion.coupon.costcentre.flag");


		if (StringUtils.isNotEmpty(costcentreflag) && StringUtils.equalsIgnoreCase(costcentreflag, "false"))
		{
			final boolean checkTotalCostCentrePercentage = checkCostCentreData(object);

			if (!checkTotalCostCentrePercentage)
			{
				errorMsg = COST_CENTRE_PERCENTAGE;
				throw new InterceptorException(errorMsg);
			}
		}
	}

	/**
	 * @param object
	 * @return boolean
	 */
	private boolean checkCostCentreData(final Object object)
	{
		if (object instanceof VoucherModel)
		{
			//final Integer titleLength = getPromotionTitleLength();
			final VoucherModel voucher = (VoucherModel) object;

			final Double couponCostCentreOnePercentageValue = voucher.getCouponCostCentreOnePercentage();
			final Double couponCostCentreTwoPercentageValue = voucher.getCouponCostCentreTwoPercentage();
			final Double couponCostCentreThreePercentageValue = voucher.getCouponCostCentreThreePercentage();

			if (couponCostCentreOnePercentageValue == null || couponCostCentreTwoPercentageValue == null
					|| couponCostCentreThreePercentageValue == null)
			{
				return false;
			}
			else
			{
				final double percentageTotal = couponCostCentreOnePercentageValue.doubleValue()
						+ couponCostCentreTwoPercentageValue.doubleValue() + couponCostCentreThreePercentageValue.doubleValue();
				if (percentageTotal != 100.0D)
				{
					return false;
				}
			}
		}
		return true;
	}


}