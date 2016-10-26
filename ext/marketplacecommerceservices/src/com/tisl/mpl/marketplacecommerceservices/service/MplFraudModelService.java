/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;


/**
 * @author TCS
 *
 */
public interface MplFraudModelService
{

	/**
	 * This method updates the fraudModel against the order
	 *
	 * @param orderModel
	 * @param mplAudit
	 */
	public void updateFraudModel(final OrderModel orderModel, final MplPaymentAuditModel mplAudit);

	/**
	 * @param orderModel
	 * @param mplAudit
	 * @param juspayEBSResponseModel
	 */
	void updateFraudModel(OrderModel orderModel, MplPaymentAuditModel mplAudit, JuspayEBSResponseModel juspayEBSResponseModel);
}
