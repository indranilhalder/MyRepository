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


	/**
	 * Compares two alphaNumeric string
	 *
	 * @param firstString
	 * @param secondString
	 */
	/* Changes for TISPRO-250 */
	public static int alphaNumericCompare(final String firstString, final String secondString)
	{
		if (secondString == null || firstString == null)
		{
			return 0;
		}

		final int lengthFirstStr = firstString.length();
		final int lengthSecondStr = secondString.length();

		int index1 = 0;
		int index2 = 0;

		while (index1 < lengthFirstStr && index2 < lengthSecondStr)
		{
			char ch1 = firstString.charAt(index1);
			char ch2 = secondString.charAt(index2);

			final char[] space1 = new char[lengthFirstStr];
			final char[] space2 = new char[lengthSecondStr];

			int loc1 = 0;
			int loc2 = 0;

			do
			{
				space1[loc1++] = ch1;
				index1++;

				if (index1 < lengthFirstStr)
				{
					ch1 = firstString.charAt(index1);
				}
				else
				{
					break;
				}
			}
			while (Character.isDigit(ch1) == Character.isDigit(space1[0]));

			do
			{
				space2[loc2++] = ch2;
				index2++;

				if (index2 < lengthSecondStr)
				{
					ch2 = secondString.charAt(index2);
				}
				else
				{
					break;
				}
			}
			while (Character.isDigit(ch2) == Character.isDigit(space2[0]));

			final String str1 = new String(space1);
			final String str2 = new String(space2);

			int result;

			if (Character.isDigit(space1[0]) && Character.isDigit(space2[0]))
			{
				final Integer firstNumberToCompare = Integer.valueOf(Integer.parseInt(str1.trim()));
				//new Integer(Integer.parseInt(str1.trim()));
				final Integer secondNumberToCompare = Integer.valueOf(Integer.parseInt(str2.trim()));//;new Integer(Integer.parseInt(str2.trim()));
				result = firstNumberToCompare.compareTo(secondNumberToCompare);
			}
			else
			{
				result = str1.compareTo(str2);
			}

			if (result != 0)
			{
				return result;
			}
		}
		return lengthFirstStr - lengthSecondStr;
	}

}