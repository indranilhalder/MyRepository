/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;


/**
 * @author TCS
 *
 */
public interface JuspayEBSService
{
	public List<MplPaymentAuditModel> fetchAUDITonEBS();

	GetOrderStatusResponse getOrderStatusFromJuspay(String auditID);

	void actionOnResponse(GetOrderStatusResponse getOrderStatusResponse, MplPaymentAuditModel audit);

	OrderModel fetchOrderOnGUID(String guid);

	boolean initiateProcess(OrderModel oModel);

	void createAuditEntry(MplPaymentAuditModel audit, boolean isCompleted);

	/**
	 * @return
	 */
	List<MplPaymentAuditModel> fetchAuditForEmptyRisk();
}
