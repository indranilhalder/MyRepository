/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface ForwardPaymentCleanUpService
{
	List<OrderModel> fetchSpecificOrders(Date startTime, Date endTime);

	MplConfigurationModel fetchConfigDetails(final String code);

	void cleanUpMultiplePayments(final OrderModel orderModel);
}
