package com.tisl.mpl.core.strategies.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.jalo.BasecommerceManager;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.jalo.ConsignmentEntry;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.strategy.impl.DefaultConsignmentBasedReturnableCheck;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.tisl.mpl.core.model.RichAttributeModel;


public class MPLDefaultConsignmentBasedReturnableCheck extends DefaultConsignmentBasedReturnableCheck
{

	@Resource
	ModelService modelService;

	//	private static final Logger LOG = Logger.getLogger(MPLDefaultConsignmentBasedReturnableCheck.class.getName());

	@Override
	public boolean perform(final OrderModel order, final AbstractOrderEntryModel orderentry, final long returnQuantity)
	{
		//final int replacementWindow = 0;
		int refundWindow = 0, finalWindow = 0;
		final DateTime currentTime = new DateTime(new Date().getTime());
		DateTime deliveryTime = new DateTime(new Date().getTime());
		if (returnQuantity < 1L || orderentry.getQuantity().longValue() < returnQuantity)
		{
			return false;
		}
		for (final RichAttributeModel richAttribute : orderentry.getProduct().getRichAttribute())
		{
			//			if (StringUtils.isNotEmpty(richAttribute.getReplacementWindow()))
			//			{
			//				replacementWindow = Integer.parseInt(richAttribute.getReplacementWindow());
			//			}
			if (StringUtils.isNotEmpty(richAttribute.getReturnWindow()))
			{
				refundWindow = Integer.parseInt(richAttribute.getReturnWindow());
				break;
			}
		}
		//		if (replacementWindow < refundWindow)
		//		{
		//			finalWindow = replacementWindow;
		//		}
		//		else
		//		{
		//			finalWindow = refundWindow;
		//		}
		finalWindow = refundWindow;
		final Set consignments = BasecommerceManager.getInstance().getConsignments(
				(Order) getModelService().toPersistenceLayer(order));
		boolean isReturnable = false;
		if (!consignments.isEmpty())
		{
			for (final Iterator iterator = consignments.iterator(); iterator.hasNext();)
			{
				final Consignment consignment = (Consignment) iterator.next();
				final ConsignmentModel consignmentModel = modelService.get(consignment);
				if (consignment.getStatus().getCode().equals(ConsignmentStatus.DELIVERED.getCode()))
				{
					if (null != consignmentModel.getDeliveryDate())
					{
						deliveryTime = new DateTime(consignmentModel.getDeliveryDate().getTime());
					}
					final int totalDaysPassed = Days.daysBetween(deliveryTime, currentTime).getDays();
					if (totalDaysPassed <= finalWindow)
					{
						final Set entries = consignment.getConsignmentEntries();
						for (final Iterator iterator1 = entries.iterator(); iterator1.hasNext();)
						{
							final ConsignmentEntry entry = (ConsignmentEntry) iterator1.next();
							if (modelService.toModelLayer(entry.getOrderEntry()).equals(orderentry))
							{
								isReturnable = entry.getShippedQuantityAsPrimitive() >= returnQuantity;
							}
						}
					}
				}

			}
		}
		else
		{
			return false;
		}
		return isReturnable;
	}

	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}