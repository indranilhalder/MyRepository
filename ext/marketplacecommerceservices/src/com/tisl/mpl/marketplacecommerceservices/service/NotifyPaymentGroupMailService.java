/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tisl.mpl.core.model.NotifyPaymentGroupProcessModel;


/**
 * @author TCS
 *
 */
public interface NotifyPaymentGroupMailService
{
	/**
	 *
	 * @param juspayOrderId
	 */
	public void sendMail(final String juspayOrderId);

	/**
	 *
	 * @param juspayOrderId
	 * @param tat
	 * @return boolean
	 */
	public boolean sendMailToTakeAction(final String juspayOrderId, final Double tat);

	/**
	 *
	 * @param orderId
	 * @return List<NotifyPaymentGroupProcessModel>
	 */
	List<NotifyPaymentGroupProcessModel> checkOrderIdForSentMail(final String orderId);
}
