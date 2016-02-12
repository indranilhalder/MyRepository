/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.refund.dao.RefundDao;
import de.hybris.platform.returns.model.RefundEntryModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.OrderRefundConfigModel;


/**
 * @author 890223
 *
 */
public interface MPLRefundDao extends RefundDao
{

	/**
	 * @return
	 * @throws Exception
	 */
	OrderRefundConfigModel getOrderRefundConfiguration() throws Exception;

	/**
	 * @description Get all Refund request made
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */


	List<RefundEntryModel> getAllRefunds(Date startDate) throws Exception;

	/**
	 * @description Get all Refund request made
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<RefundEntryModel> getAllRefunds(Date startDate, Date endDate) throws Exception;

	/**
	 * @description Get all Refund request made
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<RefundEntryModel> getAllRefunds() throws Exception;
}
