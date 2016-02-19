/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.refund.RefundService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.OrderRefundConfigModel;


/**
 * @author 890223
 *
 */
public interface MPLRefundService extends RefundService
{

	/**
	 * @return
	 * @throws Exception
	 */
	OrderRefundConfigModel getOrderRefundConfiguration() throws Exception;

	/**
	 * @description Fetch all Refunds for Report
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<RefundEntryModel> getAllRefund() throws Exception;

	/**
	 * @description Fetch all Refunds for Report
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<RefundEntryModel> getAllRefund(Date startDate) throws Exception;

	/**
	 * @description Fetch all Refunds for Report
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<RefundEntryModel> getAllRefund(Date startDate, Date endDate) throws Exception;

	/**
	 * @description Fetch all Replacement for Report
	 * @return List<ReplacementEntryModel>
	 * @throws Exception
	 */
	List<ReplacementEntryModel> getAllReplacement() throws Exception;

	/**
	 * @description Fetch all Replacement for Report
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<ReplacementEntryModel> getAllReplacement(Date startDate, Date endDate) throws Exception;

	/**
	 * @description Fetch all Replacement for Report
	 * @return List<ReplacementEntryModel>
	 * @throws Exception
	 */
	List<ReplacementEntryModel> getAllReplacement(Date startDate) throws Exception;

	/**
	 * @description Fetch all Cancelled for Report
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<OrderCancelRecordEntryModel> getAllCancelled(Date startDate, Date endDate) throws Exception;

	/**
	 * @description Fetch all Cancelled for Report
	 * @return List<ReplacementEntryModel>
	 * @throws Exception
	 */
	List<OrderCancelRecordEntryModel> getAllCancelled(Date startDate) throws Exception;

	/**
	 * @description Fetch all Cancelled for Report
	 * @return List<ReplacementEntryModel>
	 * @throws Exception
	 */
	List<OrderCancelRecordEntryModel> getAllCancelled() throws Exception;
}
