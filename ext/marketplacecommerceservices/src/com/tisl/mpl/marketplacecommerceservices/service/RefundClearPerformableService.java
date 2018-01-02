/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.Date;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author 1079689
 *
 */
public interface RefundClearPerformableService
{
	MplConfigurationModel getCronDetails(String code);

	void processRefundOrders(Date lastStartDate);

	/**
	 * @param startTime
	 * @param code
	 */
	void saveCronDetails(Date startTime, String code);


}
