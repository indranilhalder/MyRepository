/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;


/**
 * @author TCS
 *
 */
public class SaveDeliveryCostInterceptor implements ValidateInterceptor
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.interceptor.ValidateInterceptor#onValidate(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	private static final Logger LOG = Logger.getLogger(SaveDeliveryCostInterceptor.class);

	@Override
	public void onValidate(final Object model, final InterceptorContext arg1) throws InterceptorException
	{
		double deliveryCost = 0.0D;
		LOG.debug("##########################inside SaveDeliveryCostInterceptor");
		if (model instanceof AbstractOrderModel)
		{
			final AbstractOrderModel abstractOrder = (AbstractOrderModel) model;
			if (validateForShippingPromo(abstractOrder.getAllPromotionResults()))
			{

				if (CollectionUtils.isNotEmpty(abstractOrder.getEntries()))
				{
					final List<AbstractOrderEntryModel> entries = abstractOrder.getEntries();
					for (final AbstractOrderEntryModel entry : entries)
					{
						if (null != entry && !entry.getGiveAway().booleanValue() && null != entry.getCurrDelCharge()
								&& entry.getCurrDelCharge().doubleValue() > 0)
						{
							deliveryCost += entry.getCurrDelCharge().doubleValue();
						}
					}
					abstractOrder.setDeliveryCost(Double.valueOf(deliveryCost));
				}

			}


		}

	}

	/**
	 * Validate Cart for Shipping Promotion
	 *
	 * @param allPromotionResults
	 * @return flag
	 */
	private boolean validateForShippingPromo(final Set<PromotionResultModel> allPromotionResults)
	{
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(allPromotionResults))
		{
			final List<PromotionResultModel> promotionList = new ArrayList<PromotionResultModel>(allPromotionResults);
			for (final PromotionResultModel oModel : promotionList)
			{
				if ((oModel.getCertainty().floatValue() == 1.0F && null != oModel.getPromotion())
						&& (oModel.getPromotion() instanceof BuyAboveXGetPromotionOnShippingChargesModel
								|| oModel.getPromotion() instanceof BuyAGetPromotionOnShippingChargesModel || oModel.getPromotion() instanceof BuyAandBGetPromotionOnShippingChargesModel))
				{
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

}
