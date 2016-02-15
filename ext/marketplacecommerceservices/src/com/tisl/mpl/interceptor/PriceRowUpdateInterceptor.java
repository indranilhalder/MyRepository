package com.tisl.mpl.interceptor;

/**
 *
 */

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class PriceRowUpdateInterceptor implements PrepareInterceptor
{
	private ModelService modelService;
	private static final Logger LOG = Logger.getLogger(PriceRowUpdateInterceptor.class);


	//	@Resource(name = "mplUpdatePromotionPriceService")
	//	private UpdatePromotionalPriceService promotionalPriceService;

	public ModelService getModelService()
	{
		return modelService;
	}


	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @Description : The Method checks the Promotion Priority prior to its creation from HMC
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{
		LOG.debug(Localization.getLocalizedString("promotion.pricerowupdateinterceptor.message"));
		if (object instanceof PriceRowModel)
		{
			final PriceRowModel priceRow = (PriceRowModel) object;
			//final Date sysdate = new Date();
			if (priceRow.getPromotionStartDate() != null && priceRow.getPromotionEndDate() != null
			//&& sysdate.after(priceRow.getPromotionStartDate()) && sysdate.before(priceRow.getPromotionEndDate())
			)
			{


				if (priceRow.getIsPercentage() != null && !priceRow.getIsPercentage().booleanValue())
				{

					priceRow.setSpecialPrice(
							Double.valueOf(priceRow.getPrice().doubleValue() - priceRow.getPromotionValue().doubleValue()));
				}

				else
				{

					priceRow.setSpecialPrice(Double.valueOf(priceRow.getPrice().doubleValue()
							- ((priceRow.getPrice().doubleValue() * priceRow.getPromotionValue().doubleValue()) / 100)));
				}

			}
			if (priceRow.getPromotionStartDate() == null && priceRow.getPromotionEndDate() == null)
			{
				priceRow.setSpecialPrice(null);
			}
		}

	}
}
