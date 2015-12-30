/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.returns.dao.ReplacementOrderDao;
import de.hybris.platform.returns.model.ReplacementEntryModel;

import java.util.Date;
import java.util.List;


/**
 * @author 890223
 *
 */
public interface MPLReplacementDao extends ReplacementOrderDao
{

	/**
	 * @description Get all Refund request made
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<ReplacementEntryModel> getAllReplacement(Date startDate) throws Exception;

	/**
	 * @description Get all Refund request made
	 * @return List<RefundEntryModel>
	 * @throws Exception
	 */
	List<ReplacementEntryModel> getAllReplacement(Date startDate, Date endDate) throws Exception;
}
