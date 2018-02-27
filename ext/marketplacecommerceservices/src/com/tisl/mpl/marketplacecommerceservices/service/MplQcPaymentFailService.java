/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * @author Techouts
 *
 */
public interface MplQcPaymentFailService
{

	/**
	 *
	 * The Method is used to generate the .csv file data If Refund Failed From  QC 
	 *
	 */
	public void generateData();
	
	public void processQcPaymentFailedOrders() throws EtailNonBusinessExceptions;

	public void processQcRefund(OrderModel orderModel);
}
