package com.tisl.mpl.marketplacecommerceservices.strategy;

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

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;


public class MPLDefaultConsignmentBasedReturnableCheck extends DefaultConsignmentBasedReturnableCheck
{

	@Resource
	ModelService modelService;
	@Resource
	MplSellerInformationService mplSellerInformationService;

	/* private static final Logger LOG = Logger.getLogger(MPLDefaultConsignmentBasedReturnableCheck.class.getName()); */

	@SuppressWarnings("deprecation")
	@Override
	public boolean perform(final OrderModel order, final AbstractOrderEntryModel orderentry, final long returnQuantity)
	{
		//final int replacementWindow = 0;
		int refundWindow = -1, finalWindow = -1;
		final DateTime currentTime = new DateTime(new Date().getTime());
		DateTime deliveryTime = new DateTime(new Date().getTime());
		if (returnQuantity < 1L || orderentry.getQuantity().longValue() < returnQuantity)
		{
			return false;
		}
		//TISUAT-4519 Take from seller rich attribute instead product
		final SellerInformationModel sellerInfo = mplSellerInformationService.getSellerDetail(orderentry.getSelectedUSSID());

		for (final RichAttributeModel richAttribute : sellerInfo.getRichAttribute())
		{
			//			if (StringUtils.isNotEmpty(richAttribute.getReplacementWindow()))
			//			{
			//				replacementWindow = Integer.parseInt(richAttribute.getReplacementWindow());
			//			}
			if (richAttribute.getReturnWindow() != null && Integer.parseInt(richAttribute.getReturnWindow()) > 0)
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
}