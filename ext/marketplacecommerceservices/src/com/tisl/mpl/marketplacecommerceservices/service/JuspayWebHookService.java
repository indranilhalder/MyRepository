/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;

import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface JuspayWebHookService
{

	void fetchWebHookData();

	MplConfigurationModel getCronDetails(String code);

	void saveCronDetails(Date startTime, String code);

	void fetchSpecificWebHookData(Date mplConfigDate, Date startTime);

	Double getWebHookJobTAT();

	Double getEBSJobTAT();

	public OrderModel updateAuditEntry(final GetOrderStatusResponse orderStatusResponse, final MplPaymentAuditModel auditModel,
			final OrderModel orderModel);

	/**
	 * This method returns the JuspayEBSTAT1 for the current basestore
	 *
	 * @return Double
	 */
	Double getEmptyEBSJobTAT();

	/**
	 * This method returns the JuspayEBSTAT1 for the current basestore
	 *
	 * @return Double
	 */
	Double getEBSTatExpiryAlertTime();

	/**
	 * @param orderStatusResponse
	 * @param auditModel
	 * @param orderModel
	 * @return OrderModel
	 */
	OrderModel updAuditForEmptyRisk(GetOrderStatusResponse orderStatusResponse, MplPaymentAuditModel auditModel,
			OrderModel orderModel);
}
