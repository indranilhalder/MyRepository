/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;



/**
 * @author TCS
 *
 */
public interface MplProcessOrderService
{
	void processPaymentPedingOrders() throws EtailNonBusinessExceptions;

	void removePromotionInvalidation(final OrderModel orderModel) throws EtailNonBusinessExceptions;

}
