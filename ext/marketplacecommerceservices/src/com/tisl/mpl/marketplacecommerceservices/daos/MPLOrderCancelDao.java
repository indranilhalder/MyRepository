/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.ordercancel.dao.OrderCancelDao;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

import java.util.Date;
import java.util.List;


/**
 * @author TCS
 *
 */
public interface MPLOrderCancelDao extends OrderCancelDao
{

	/**
	 * @description Get all Cancel request made
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<OrderCancelRecordEntryModel> getAllCancelled() throws Exception;

	/**
	 * @description Get all Cancel request made
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<OrderCancelRecordEntryModel> getAllCancelled(Date startDate) throws Exception;

	/**
	 * @description Get all Cancel request made
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<OrderCancelRecordEntryModel> getAllCancelled(Date startDate, Date endDate) throws Exception;
}
