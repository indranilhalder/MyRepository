/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.search.SearchResult;

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
public interface FetchSalesOrderService
{

	public MplConfigurationModel getCronDetails(String code);

	public void saveCronDetails(Date startTime, String code);

	public List<OrderModel> fetchOrderDetails();

	public List<OrderModel> fetchCancelOrderDetails();

	public List<OrderModel> fetchSpecifiedData(Date startTime, Date endTime);

	public List<OrderModel> fetchSpecifiedCancelData(Date startTime, Date endTime);

	//TPR-1984 Start //Change for TPR-TPR-6033
	public Map<OrderEntryModel, OrderModel> fetchOrderDetailsforDeliveryMail(Date mplConfigDate);

	public Map<String, Integer> getTransactionIdCount();

	public Map<String, NPSMailerModel> getTransactionIdList();

	public Map<String, Integer> getorderModelTransactionCount(Set<String> parentOrderIds);

	public List<Map> getOrderModelTransactionId(Set<String> parentOrderIds);

	public Map<String, String> fetchOrderIdsToday();

	public CronJobModel getCronDetailsCode(String code);

	/**
	 * TPR-7415
	 *
	 * @param mplConfigDate
	 * @param startTime
	 * @return List<OrderModel>
	 */
	public SearchResult<List<Object>> fetchSpecifiedDataForPymntScss(Date mplConfigDate, Date startTime);

	/**
	 * TPR-7415
	 *
	 * @return List<OrderModel>
	 */
	public SearchResult<List<Object>> fetchSpecifiedDataForPymntScss();
}
