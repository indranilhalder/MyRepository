/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tisl.mpl.core.model.NPSMailerModel;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface FetchSalesOrderDao
{
	public MplConfigurationModel getCronDetails(String code);

	public List<OrderModel> fetchOrderDetails();

	public List<OrderModel> fetchCancelOrderDetails();

	public List<OrderModel> fetchSpecifiedData(Date startTime, Date endTime);

	public List<OrderModel> fetchSpecifiedCancelData(Date startTime, Date endTime);

	//TPR-1984 Start
	public Map<OrderModel, AbstractOrderEntryModel> fetchOrderDetailsforDeliveryMail();

	public Map<String, Integer> getTransactionIdCount();

	public Map<String, NPSMailerModel> getTransactionIdList();

	public Map<String, Integer> getorderModelTransactionCount(final Set<String> parentOrderIds);

	public List<Map> getOrderModelTransactionId(final Set<String> parentOrderIds);

	public Map<String, String> fetchOrderIdsToday();
}
