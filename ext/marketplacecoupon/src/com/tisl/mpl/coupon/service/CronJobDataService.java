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
	boolean birthdayVoucherDetails(CustomerModel oCusModel, Date start, Date end);

	boolean anniversaryVoucherDetails(CustomerModel oCusModel, Date start, Date end);

	boolean purchaseBasedVoucherDetails(CustomerModel oCusModel, Date start, Date end, Double amount);

	boolean firstTimeRegVoucherDetails(CustomerModel oCusModel, Date start, Date end, int days);

	boolean cartNotShoppedVoucherDetails(CustomerModel oCusModel, int days);

	boolean cartAbandonmentVoucherDetails(CustomerModel oCusModel, Date start, Date end, boolean isForPayment, boolean isGreater,
			double cartValue);

}