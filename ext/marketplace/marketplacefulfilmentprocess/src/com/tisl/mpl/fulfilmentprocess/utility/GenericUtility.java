/**
 *
 */
package com.tisl.mpl.fulfilmentprocess.utility;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class GenericUtility
{
	@Autowired
	private ModelService modelService;

	public void setOrderStatus(final OrderModel order, final OrderStatus orderStatus)
	{
		boolean flag = false;
		final List<OrderModel> subOrderList = order.getChildOrders();
		if (null != subOrderList)
		{
			for (final OrderModel subOrder : subOrderList)
			{
				int quantity = 0;
				final List<AbstractOrderEntryModel> subOrderEntryList = subOrder.getEntries();
				//SONAR FIx
				//				if (null != subOrderEntryList)
				//				{
				//					for (final AbstractOrderEntryModel subOrderEntry : subOrderEntryList)
				//					{
				//						if (subOrderEntry.getQuantity().doubleValue() > 0)
				//						{
				//							quantity++;
				//						}
				//					}
				//				}

				quantity = getQuantity(quantity, subOrderEntryList);
				if (quantity > 0)
				{
					subOrder.setStatus(orderStatus);
					modelService.save(subOrder);
					flag = true;
				}
			}
		}
		if (flag) //flag == true
		{
			order.setStatus(orderStatus);
			modelService.save(order);
		}

	}


	private int getQuantity(int quantity, final List<AbstractOrderEntryModel> subOrderEntryList)
	{
		if (null != subOrderEntryList)
		{
			for (final AbstractOrderEntryModel subOrderEntry : subOrderEntryList)
			{
				if (subOrderEntry.getQuantity().doubleValue() > 0)
				{
					quantity++;
				}
			}
		}

		return quantity;
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