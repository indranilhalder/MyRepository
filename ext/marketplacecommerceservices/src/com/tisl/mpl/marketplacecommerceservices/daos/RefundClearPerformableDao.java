/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author 1079689
 *
 */
public interface RefundClearPerformableDao
{

	MplConfigurationModel getCronDetails(String code);

	/**
	 * @param refundClearTAT
	 * @param queryStartTime
	 * @return
	 */
	//	List<ConsignmentModel> getRefundClearOrders(Date refundClearTAT, Date queryStartTime);

	/**
	 * @param guid
	 */
	List<MplPaymentAuditModel> fetchAuditDataList(String guid);


	List<JuspayOrderStatusModel> fetchWebhookTableStatus(String reqId);

	/**
	 * @param juspayReqiestID
	 * @return
	 */
	List<RefundTransactionMappingModel> fetchRefundTransactionMapping(String juspayReqiestID);

	/**
	 * @param abstractOrderEntryModel
	 */
	RefundTransactionMappingModel fetchRefundTransactionByEntry(AbstractOrderEntryModel abstractOrderEntryModel);

	/**
	 * @param queryTAT
	 * @param queryStartTime
	 * @return
	 */
	List<ConsignmentModel> getRefundClearConsignments(Date queryTAT, Date queryStartTime);


}
