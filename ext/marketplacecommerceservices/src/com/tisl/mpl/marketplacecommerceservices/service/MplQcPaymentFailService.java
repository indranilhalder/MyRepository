/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;

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
	void generateData();
	
	void processQcPaymentFailedOrders() throws EtailNonBusinessExceptions;
}
