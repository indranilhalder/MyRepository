/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
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
import com.tisl.mpl.model.CityModel;
import com.tisl.mpl.model.StateModel;



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


			if (validateForShippingPromo(abstractOrder.getAllPromotionResults()))
			{
				double deliveryOffChrg = 0.0D;
				double promoDiscount = 0.0D;
				double couponDiscount = 0.0D;

				Double totalPrice = Double.valueOf(0);

				final List<AbstractOrderEntryModel> entries = abstractOrder.getEntries();

				if (CollectionUtils.isNotEmpty(entries))
				{
					for (final AbstractOrderEntryModel oModel : entries)
					{
						final Double currentDelCharge = oModel.getCurrDelCharge();
						final Double prevDelCharge = oModel.getPrevDelCharge();
						final Double couponValue = oModel.getCouponValue();
						final Double totalproductLevelDisc = oModel.getTotalProductLevelDisc();
						final Double totalcartLevelDisc = oModel.getCartLevelDisc();


						deliveryOffChrg += (currentDelCharge.doubleValue() - prevDelCharge.doubleValue()) < 0
								? (-1) * (currentDelCharge.doubleValue() - prevDelCharge.doubleValue())
								: (currentDelCharge.doubleValue() - prevDelCharge.doubleValue());

						couponDiscount += (null == couponValue ? 0.0d : couponValue.doubleValue());

						promoDiscount += (null == totalproductLevelDisc ? 0.0d : totalproductLevelDisc.doubleValue())
								+ (null == totalcartLevelDisc ? 0.0d : totalcartLevelDisc.doubleValue());
					}

					if (deliveryOffChrg > 0.0D)
					{
						totalPrice = Double.valueOf(abstractOrder.getSubtotal().doubleValue()
								+ abstractOrder.getDeliveryCost().doubleValue() - (deliveryOffChrg + couponDiscount + promoDiscount));
						if (totalPrice.doubleValue() > 0)
						{
							abstractOrder.setTotalPrice(totalPrice);
						}
					}

				}
			}


			final AddressModel deliveryAddress = abstractOrder.getDeliveryAddress();
			if (null != deliveryAddress && null != deliveryAddress.getPostalcode())
			{
				LOG.debug("Setting data for Pincode related Information");
				final PincodeModel pinCodeModelObj = pincodeService.getDetailsOfPincode(deliveryAddress.getPostalcode());
				if (null != pinCodeModelObj)
				{
					final StateModel state = pinCodeModelObj.getState();
					final CityModel city = pinCodeModelObj.getCity();

					abstractOrder.setStateForPincode(state == null ? "" : state.getCountrykey());
					abstractOrder.setCityForPincode(city == null ? "" : city.getCityName());
					abstractOrder.setPincodeNumber(deliveryAddress.getPostalcode());
				}
			}
		}


	}



	/**
	 * @param abstractOrder
	 * @return
	 */
	//	private boolean validateForParent(final AbstractOrderModel abstractOrder)
	//	{
	//		boolean flag = false;
	//		if (abstractOrder instanceof OrderModel)
	//		{
	//			final OrderModel order = (OrderModel) abstractOrder;
	//			if (StringUtils.isNotEmpty(order.getType()) && order.getType().equals("Parent"))
	//			{
	//				flag = true;
	//			}
	//		}
	//		return flag;
	//	}



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
				final AbstractPromotionModel promotion = oModel.getPromotion();
				if ((oModel.getCertainty().floatValue() == 1.0F && null != promotion)
						&& (promotion instanceof BuyAboveXGetPromotionOnShippingChargesModel
								|| promotion instanceof BuyAGetPromotionOnShippingChargesModel
								|| promotion instanceof BuyAandBGetPromotionOnShippingChargesModel))
				{
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

}
