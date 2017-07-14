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

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PromotionalPriceRowModel;


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
			final Collection<PromotionalPriceRowModel> promoPriceRow = priceRow.getPromotionalPriceRow(); //channel specific promotion changes

			double specialPrice = 0.0D;
			double discount = 0.0D;
			if (CollectionUtils.isNotEmpty(promoPriceRow))
			{
			for (final PromotionalPriceRowModel promoPrice : promoPriceRow) //channel specific promotion changes loop needed here
			{
				//final Date sysdate = new Date();
				if (promoPrice.getPromotionStartDate() != null && promoPrice.getPromotionEndDate() != null
				//&& sysdate.after(priceRow.getPromotionStartDate()) && sysdate.before(priceRow.getPromotionEndDate())
				)
				{
					if (promoPrice.getIsPercentage() != null && !promoPrice.getIsPercentage().booleanValue())
					{
						promoPrice.setSpecialPrice(
								Double.valueOf(priceRow.getPrice().doubleValue() - promoPrice.getPromotionValue().doubleValue()));
					}
					else
					{
						discount = (priceRow.getPrice().doubleValue() * promoPrice.getPromotionValue().doubleValue()) / 100;

						if (null != promoPrice.getMaxDiscount() && promoPrice.getMaxDiscount().doubleValue() > 0.0
								&& discount > promoPrice.getMaxDiscount().doubleValue())
						{
							specialPrice = priceRow.getPrice().doubleValue() - promoPrice.getMaxDiscount().doubleValue();
							LOG.debug("Special Price > Max Discount : Special Price:" + specialPrice);
							promoPrice.setSpecialPrice(Double.valueOf(specialPrice));
						}
						else if (promoPrice.getPromotionChannel() != null)
						{
							specialPrice = priceRow.getPrice().doubleValue()
									- ((priceRow.getPrice().doubleValue() * promoPrice.getPromotionValue().doubleValue()) / 100);
							LOG.debug("Special Price < Max Discount : Special Price: " + specialPrice);
							promoPrice.setSpecialPrice(Double.valueOf(specialPrice));
						}
						else
						{
							specialPrice = priceRow.getPrice().doubleValue()
									- ((priceRow.getPrice().doubleValue() * promoPrice.getPromotionValue().doubleValue()) / 100);
							LOG.debug("Special Price < Max Discount : Special Price: " + specialPrice);
							promoPrice.setSpecialPrice(Double.valueOf(specialPrice));
						}
					}

				}
				if (promoPrice.getPromotionStartDate() == null && promoPrice.getPromotionEndDate() == null)
				{
					promoPrice.setSpecialPrice(null);
				}
			}
			}

			if (CollectionUtils.isNotEmpty(promoPriceRow))
			{
				modelService.saveAll(promoPriceRow);
			}
		}
	}
}
