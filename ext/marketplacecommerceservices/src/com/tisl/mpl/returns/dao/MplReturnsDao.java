/**
 * 
 */
package com.tisl.mpl.returns.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;
import com.tisl.mpl.core.model.MplReturnPickUpAddressInfoModel;
import com.tisl.mpl.model.CRMTicketDetailModel;

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
  
  public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByDates(Date fromDate, Date toDate);

	public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByParams(String orderID, String customerId, String pincode);
	
	public CRMTicketDetailModel getCRMTicketDetail(String transactionId);
}
