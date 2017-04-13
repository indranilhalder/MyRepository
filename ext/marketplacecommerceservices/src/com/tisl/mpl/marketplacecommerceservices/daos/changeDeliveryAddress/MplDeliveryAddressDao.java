/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress;



import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.MplDeliveryAddressInfoModel;



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
	public List<MplDeliveryAddressInfoModel> getMplDeliveryAddressReportModels(Date fromDate,Date toDate);
	
	public MplDeliveryAddressInfoModel getMplDeliveryAddressReportModelByOrderId(final String orderCode);
}
