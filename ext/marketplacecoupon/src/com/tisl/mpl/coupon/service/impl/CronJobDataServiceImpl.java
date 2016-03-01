/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.coupon.service.CronJobDataService;
import com.tisl.mpl.util.CouponUtilityMethods;
import com.tisl.mpl.util.LatestOrderModelCompare;


/**
 * @author TCS
 *
 */
public class CronJobDataServiceImpl implements CronJobDataService
{
	private final static Logger LOG = Logger.getLogger(CronJobDataServiceImpl.class.getName());

	/**
	 * @Description: checks whether customer DOB lies between startDate, endDate
	 * @param startDate
	 * @param endDate
	 * @return true if DOB lies between startDate, endDate
	 */
	@Override
	public boolean birthdayVoucherDetails(final CustomerModel oCusModel, final Date startDate, final Date endDate)
	{
		final Date customerBdate = oCusModel.getDateOfBirth();

		return (null != startDate && null != endDate && null != customerBdate) ? CouponUtilityMethods.doDateValidation(startDate,
				endDate, customerBdate) : false;
	}

	/**
	 * @Description: checks whether customer Date of Anniversary lies between startDate, endDate
	 * @param startDate
	 * @param endDate
	 * @return true if Date of Anniversary lies between startDate, endDate
	 */
	@Override
	public boolean anniversaryVoucherDetails(final CustomerModel oCusModel, final Date startDate, final Date endDate)
	{
		final Date customerAnnivDate = oCusModel.getDateOfAnniversary();

		return (null != startDate && null != endDate && null != customerAnnivDate) ? CouponUtilityMethods.doDateValidation(
				startDate, endDate, customerAnnivDate) : false;
	}

	/**
	 * @Description: Purchase Based special Vouchers
	 *
	 * @param oCusModel
	 * @param startDate
	 * @param endDate
	 * @param specifiedAmount
	 * @return boolean
	 *
	 */
	@Override
	public boolean purchaseBasedVoucherDetails(final CustomerModel oCusModel, final Date startDate, final Date endDate,
			final Double specifiedAmount)
	{
		boolean flag = false;

		if (specifiedAmount.doubleValue() == 0.00D)
		{
			LOG.debug("Threshold Amount is 0, Please input orderThresholdVal");
		}
		else
		{
			if (null != oCusModel.getOrders())
			{
				final ArrayList<OrderModel> orderList = new ArrayList<OrderModel>(oCusModel.getOrders());
				if (CollectionUtils.isNotEmpty(orderList))
				{
					Collections.sort(orderList, new LatestOrderModelCompare());

					final OrderModel latestOrder = orderList.get(0);

					//final Date sysDate = new Date();
					final Date lastOrderDate = latestOrder.getDate();

					if (CouponUtilityMethods.doDateValidation(startDate, endDate, lastOrderDate)
							&& null != latestOrder.getTotalPrice()
							&& latestOrder.getTotalPrice().doubleValue() > specifiedAmount.doubleValue())
					{
						flag = true;
					}
				}
			}
		}

		return flag;
	}



	/**
	 * @Description: Checks whether the customer has newly registered and has no order placed before
	 *
	 * @param oCusModel
	 * @param restrictionStartDate
	 * @param restrictionEndDate
	 * @param noOfDays
	 * @return boolean
	 *
	 */
	@Override
	public boolean firstTimeRegVoucherDetails(final CustomerModel oCusModel, final Date restrictionStartDate,
			final Date restrictionEndDate, final int noOfDays)
	{
		final Date currentDate = new Date();
		return ((null != oCusModel.getCreationtime()
				&& CouponUtilityMethods.doDateValidation(restrictionStartDate, restrictionEndDate, oCusModel.getCreationtime())
				&& oCusModel.getOrders() != null && oCusModel.getOrders().isEmpty() && (noOfDays == 0 || CouponUtilityMethods
				.noOfDaysCalculatorBetweenDates(oCusModel.getCreationtime(), currentDate) >= noOfDays)) ? true : false);
	}



	/**
	 * @Description: Checks whether the cart is not shopped for specific no of days
	 *
	 * @param oCusModel
	 * @param noOfDays
	 * @return boolean
	 *
	 */
	@Override
	public boolean cartNotShoppedVoucherDetails(final CustomerModel oCusModel, final int noOfDays)
	{
		boolean flag = false;
		final ArrayList<OrderModel> orderList = new ArrayList<OrderModel>(oCusModel.getOrders());

		if (noOfDays == 0)
		{
			LOG.debug("No. of Days is 0. Please input No. of Days");
		}
		else if (CollectionUtils.isNotEmpty(orderList))
		{
			final Date currentDate = new Date();

			Collections.sort(orderList, new LatestOrderModelCompare());
			final Date lastOrderDate = orderList.get(0).getDate();
			final int noOfInactiveDays = CouponUtilityMethods.noOfDaysCalculatorBetweenDates(currentDate, lastOrderDate);

			if (noOfInactiveDays >= noOfDays)
			{
				flag = true;
			}

		}

		return flag;
	}



	/**
	 * @Description: Checks whether the cart is abandoned at cart page or at payment page
	 *
	 * @param oCusModel
	 * @param startDate
	 * @param endDate
	 * @param isForPayment
	 * @param isGreater
	 * @param cartValue
	 * @return boolean
	 *
	 */
	@Override
	public boolean cartAbandonmentVoucherDetails(final CustomerModel oCusModel, final Date startDate, final Date endDate,
			final boolean isForPayment, final boolean isGreater, final double cartValue)
	{
		boolean flag = false;
		final ArrayList<CartModel> orderList = new ArrayList<CartModel>(oCusModel.getCarts());

		if (CollectionUtils.isNotEmpty(orderList))
		{
			for (final CartModel cart : orderList)
			{
				if (CouponUtilityMethods.doDateValidation(startDate, endDate, cart.getCreationtime()))
				{
					//Cann't add nested if here if SONAR error comes, if 'if(isForPayment && null != cart.getDeliveryAddress())' added the logic would be tampered
					//Suppose there are two customers, one for cartAbandonment at cart page and one for cartAbandonment at payment page, for this case logic won't work
					if (isForPayment)
					{
						if (null != cart.getDeliveryAddress())
						{
							flag = checkCartValue(cart, isGreater, cartValue);
							break;
						}
					}
					else if (cart.getDeliveryAddress() == null)
					{
						flag = checkCartValue(cart, isGreater, cartValue);
						break;
					}
				}
			}

		}

		return flag;
	}


	/**
	 * This method checks the cart value and returns whether it is greater than required cart value
	 *
	 * @param cart
	 * @param isGreater
	 * @param cartValue
	 * @return boolean
	 */
	private boolean checkCartValue(final CartModel cart, final boolean isGreater, final double cartValue)
	{
		boolean flag = false;
		if (cartValue == 0)
		{
			LOG.debug("Threshold Amount is 0, Please input orderThresholdVal");
		}
		else
		{
			final double price = (null != cart.getSubtotal()) ? cart.getSubtotal().doubleValue() : 0.00D;
			final double restrictionAmt = cartValue;
			flag = (isGreater && price >= restrictionAmt) ? true : (restrictionAmt >= price ? true : false);
		}

		return flag;

	}
}