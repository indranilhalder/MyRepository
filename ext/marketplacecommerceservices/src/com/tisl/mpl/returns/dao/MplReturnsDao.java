/**
 * 
 */
package com.tisl.mpl.returns.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

import java.util.List;

import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;

/**
 * @author TECHOUTS
 *
 */
public interface MplReturnsDao
{
	/**
	 * 
	 * @param customerId
	 * @return MplCustomerBankAccountDetailsModel
	 */
  MplCustomerBankAccountDetailsModel getCustomerBankDetailsById(String customerId);
  
  List<ReturnRequestModel> getListOfReturnRequest(String orlderId);
  
  /**
   *  Fetching OrderModel
   * @param orderId
   * @return OrderModel
   */
  public List<OrderModel> getOrder(final String orderId);
}
