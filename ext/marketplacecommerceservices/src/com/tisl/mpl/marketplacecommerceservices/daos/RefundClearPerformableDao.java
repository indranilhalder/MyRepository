/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;


/**
 * @author TCS
 *
 */
public interface RefundClearPerformableDao
{

	List<MplPaymentAuditModel> fetchAuditDataList(String guid);


	List<JuspayOrderStatusModel> fetchWebhookTableStatus(String reqId);


	Map<String, RefundTransactionMappingModel> fetchRefundTransactionMapping(AbstractOrderEntryModel abstractOrderEntryModel);


	List<ConsignmentModel> getRefundClearConsignments(Date queryTAT, Date queryStartTime);


	List<String> fetchRtmRequestIds(List<String> requestIds);

}
