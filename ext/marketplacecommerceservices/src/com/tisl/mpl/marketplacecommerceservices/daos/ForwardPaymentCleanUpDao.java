/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.FPCRefundEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface ForwardPaymentCleanUpDao
{
	List<OrderModel> fetchOrdersWithMultiplePayments(final Date startTime, final Date endTime);

	List<OrderModel> fetchPaymentFailedOrders(final Date startTime, final Date endTime);

	List<MplPaymentAuditModel> fetchAuditsWithoutOrder(final Date startTime, final Date endTime);

	MplConfigurationModel fetchConfigDetails(final String code);

	List<MplPaymentAuditModel> fetchAuditsForGUID(final String guid);

	FPCRefundEntryModel fetchRefundEntryForAuditId(final String auditId);

	List<FPCRefundEntryModel> fetchSpecificRefundEntries(String expiredFlag);

	OrderModel fetchParentOrderByGUID(String orderCode);
}
