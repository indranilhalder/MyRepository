/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.NPSEmailerModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface FetchSalesOrderService
{

	public MplConfigurationModel getCronDetails(String code);

	public void saveCronDetails(Date startTime, String code);

	public List<OrderModel> fetchOrderDetails();

	public List<OrderModel> fetchCancelOrderDetails();

	public List<OrderModel> fetchSpecifiedData(Date startTime, Date endTime);

	public List<OrderModel> fetchSpecifiedCancelData(Date startTime, Date endTime);

	//TPR-1984 Start
	public Map<OrderModel, List<String>> fetchOrderDetailsforDeliveryMail();

	public Map<String, Integer> getTransactionIdCount();

	public Map<String, NPSEmailerModel> getTransactionIdList();

	public Map<String, Integer> getorderModelTransactionCount(Set<String> parentOrderIds);

	public List<Map> getOrderModelTransactionId(Set<String> parentOrderIds);

	public Map<String, String> fetchOrderIdsToday();
}
