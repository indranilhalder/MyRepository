/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;

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

}
