/**
 *
 */
package com.tisl.mpl.marketplaceomsservices.daos;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;

import java.util.List;

import com.tisl.mpl.core.model.OrderUpdateProcessModel;


/**
 * @author TCS
 * @param <OrderUpdateSmsProcessModel>
 *
 */
public interface EmailAndSmsNotification<OrderUpdateSmsProcessModel>
{
	/**
	 * @param awbNumber
	 * @param shipmentStatus
	 * @return OrderUpdateProcessModel
	 * @description This method is used to check whether an Email is sent corresponding to the awbNumber taken as
	 *              parameter
	 */
	public List<OrderUpdateProcessModel> checkEmailSent(String awbNumber, ConsignmentStatus shipmentStatus);

	/**
	 * @param awbNumber
	 * @param shipmentStatus
	 * @return OrderUpdateProcessModel
	 * @description This method is used to check whether an SMS is sent corresponding to the awbNumber taken as parameter
	 */
	public List<OrderUpdateSmsProcessModel> checkSmsSent(String awbNumber, ConsignmentStatus shipmentStatus);
}
