/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress;


import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;



/**
 * @author pankajk
 *
 */

public interface MplDeliveryAddressDao
{

   /**
    * Change deliveryAddressReport
    * @param fromDate
    * @param toDate
    * @return
    */
	public List<OrderModel> getOrderModelList(Date fromDate,Date toDate);
}
