/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;


/**
 * @author TCS
 *
 */
public class SaveDeliveryCostInterceptor implements ValidateInterceptor
{
	@Autowired
	private PincodeService pincodeService;

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
		//final double deliveryCost = 0.0D;
		LOG.debug("##########################inside SaveDeliveryCostInterceptor");
		if (model instanceof AbstractOrderModel)
		{
			final AbstractOrderModel abstractOrder = (AbstractOrderModel) model;

			//************************************************************************************************************//
			// ***Blocked for TPR-3486 + TPR-3487 : Please do not open : Code implemented in Strategy Calculation Class***
			//************************************************************************************************************//

			//			if (validateForShippingPromo(abstractOrder.getAllPromotionResults()))
			//			{
			//
			//				if (CollectionUtils.isNotEmpty(abstractOrder.getEntries()))
			//				{
			//					final List<AbstractOrderEntryModel> entries = abstractOrder.getEntries();
			//					for (final AbstractOrderEntryModel entry : entries)
			//					{
			//						if (null != entry && !entry.getGiveAway().booleanValue() && null != entry.getCurrDelCharge()
			//								&& entry.getCurrDelCharge().doubleValue() > 0)
			//						{
			//							deliveryCost += entry.getCurrDelCharge().doubleValue();
			//						}
			//					}
			//					abstractOrder.setDeliveryCost(Double.valueOf(deliveryCost));
			//				}
			//
			//			}

			//TPR-3571 : Fix for Pincode Specific Promotion
			if (null != abstractOrder.getDeliveryAddress() && null != abstractOrder.getDeliveryAddress().getPostalcode())
			{
				LOG.debug("Setting data for Pincode related Information");
				final PincodeModel pinCodeModelObj = pincodeService.getLatAndLongForPincode(abstractOrder.getDeliveryAddress()
						.getPostalcode());
				if (null != pinCodeModelObj)
				{
					abstractOrder.setStateForPincode(pinCodeModelObj.getState() == null ? "" : pinCodeModelObj.getState()
							.getCountrykey());
					abstractOrder.setCityForPincode(pinCodeModelObj.getCity() == null ? "" : pinCodeModelObj.getCity().getCityName());
					abstractOrder.setPincodeNumber(abstractOrder.getDeliveryAddress().getPostalcode());
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
/*	private boolean validateForShippingPromo(final Set<PromotionResultModel> allPromotionResults)
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
	}*/

}
