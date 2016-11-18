/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;



/**
 * @author TCS
 *
 */
public interface MplProcessOrderService
{
	void processPaymentPedingOrders() throws EtailNonBusinessExceptions;

}
