/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;

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
	 * @return
	 */
	List<OrderModel> getRefundClearOrders(Date refundClearTAT);


}
