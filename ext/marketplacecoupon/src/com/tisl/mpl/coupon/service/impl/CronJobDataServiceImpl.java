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

	@SuppressWarnings("unused")
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

		return (null != startDate && null != endDate && null != customerBdate)
				? CouponUtilityMethods.doDateValidation(startDate, endDate, customerBdate) : false;
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

		return (null != startDate && null != endDate && null != customerAnnivDate)
				? CouponUtilityMethods.doDateValidation(startDate, endDate, customerAnnivDate) : false;
	}

	/*
	 * @Description: Purchase Based special Vouchers (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.CronJobDataService#purchaseBasedVoucherDetails(de.hybris.platform
	 * .core.model.user.CustomerModel, java.util.Date, java.util.Date, java.lang.Double)
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
				if (!orderList.isEmpty())
				{
					Collections.sort(orderList, new LatestOrderModelCompare());

					final OrderModel latestOrder = orderList.get(0);

					//final Date sysDate = new Date();
					final Date lastOrderDate = latestOrder.getDate();

					if (CouponUtilityMethods.doDateValidation(startDate, endDate, lastOrderDate))
					{
						if (null != latestOrder.getTotalPrice())
						{
							final Double amount = latestOrder.getTotalPrice();
							if (amount.doubleValue() > specifiedAmount.doubleValue())
							{
								flag = true;
							}
						}
					}
				}
			}
		}

		return flag;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.CronJobDataService#firstTimeRegVoucherDetails(de.hybris.platform
	 * .core.model.user.CustomerModel, java.util.Date, java.util.Date, int)
	 */
	@Override
	public boolean firstTimeRegVoucherDetails(final CustomerModel oCusModel, final Date restrictionStartDate,
			final Date restrictionEndDate, final int noOfDays)
	{
		final Date currentDate = new Date();
		boolean flag = false;
		if (null != oCusModel.getCreationtime())
		{
			final Date userCreationTime = oCusModel.getCreationtime();

			if (CouponUtilityMethods.doDateValidation(restrictionStartDate, restrictionEndDate, userCreationTime))
			{
				if (oCusModel.getOrders() != null && oCusModel.getOrders().isEmpty())
				{
					if (noOfDays == 0
							|| CouponUtilityMethods.noOfDaysCalculatorBetweenDates(userCreationTime, currentDate) >= noOfDays)
					{
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.CronJobDataService#cartNotShoppedVoucherDetails(de.hybris.
	 * platform .core.model.user.CustomerModel, int)
	 */
	@Override
	public boolean cartNotShoppedVoucherDetails(final CustomerModel oCusModel, final int noOfDays)
	{
		boolean flag = false;

		if (noOfDays == 0)
		{
			LOG.debug("No. of Days is 0. Please input No. of Days");
		}
		else
		{
			final Date currentDate = new Date();

			ArrayList<OrderModel> orderList = null;

			if (null != oCusModel.getOrders() && !oCusModel.getOrders().isEmpty())
			{
				orderList = new ArrayList<OrderModel>(oCusModel.getOrders());
				Collections.sort(orderList, new LatestOrderModelCompare());
				final Date lastOrderDate = orderList.get(0).getDate();
				final int noOfInactiveDays = CouponUtilityMethods.noOfDaysCalculatorBetweenDates(currentDate, lastOrderDate);

				if (noOfInactiveDays >= noOfDays)
				{
					flag = true;
				}

			}
		}


		return flag;
	}


	@Override
	public boolean cartAbandonmentVoucherDetails(final CustomerModel oCusModel, final Date startDate, final Date endDate,
			final boolean isForPayment, final boolean isGreater, final double cartValue)
	{
		boolean flag = false;

		if (null != oCusModel.getCarts() && !oCusModel.getCarts().isEmpty())
		{
			final ArrayList<CartModel> orderList = new ArrayList<CartModel>(oCusModel.getCarts());
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