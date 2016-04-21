/**
 *
 */
package com.tisl.mpl.coupon.service;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;


//import com.tisl.mpl.model.CustomerVoucherLinkModel;


/**
 * @author TCS
 *
 */
public interface CronJobDataService
{
	/**
	 *
	 * @param oCusModel
	 * @param start
	 * @param end
	 * @return boolean
	 */
	boolean birthdayVoucherDetails(CustomerModel oCusModel, Date start, Date end);


	/**
	 *
	 * @param oCusModel
	 * @param start
	 * @param end
	 * @return boolean
	 */
	boolean anniversaryVoucherDetails(CustomerModel oCusModel, Date start, Date end);


	/**
	 *
	 * @param oCusModel
	 * @param start
	 * @param end
	 * @param amount
	 * @return boolean
	 */
	boolean purchaseBasedVoucherDetails(CustomerModel oCusModel, Date start, Date end, Double amount);


	/**
	 *
	 * @param oCusModel
	 * @param start
	 * @param end
	 * @param days
	 * @return boolean
	 */
	boolean firstTimeRegVoucherDetails(CustomerModel oCusModel, Date start, Date end, int days);


	/**
	 *
	 * @param oCusModel
	 * @param days
	 * @return boolean
	 */
	boolean cartNotShoppedVoucherDetails(CustomerModel oCusModel, int days);


	/**
	 *
	 * @param oCusModel
	 * @param start
	 * @param end
	 * @param isForPayment
	 * @param isGreater
	 * @param cartValue
	 * @return boolean
	 */
	boolean cartAbandonmentVoucherDetails(CustomerModel oCusModel, Date start, Date end, boolean isForPayment, boolean isGreater,
			double cartValue);

}