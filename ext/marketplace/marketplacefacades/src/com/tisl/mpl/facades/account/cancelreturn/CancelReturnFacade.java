/**
 *
 */
package com.tisl.mpl.facades.account.cancelreturn;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import com.tisl.mpl.data.ReturnLogisticsResponseData;
import com.tisl.mpl.facades.data.ReturnItemAddressData;


/**
 * @author TCS
 *
 */
public interface CancelReturnFacade
{

	/**
	 * @param subOrderDetails
	 * @param orderEntry
	 * @param reasonCode
	 * @param ticketTypeCode
	 * @param ussid
	 * @param customerData
	 * @param refundType
	 * @return boolean
	 */
	public boolean implementCancelOrReturn(OrderData subOrderDetails, OrderEntryData orderEntry, String reasonCode, String ussid,
			String ticketTypeCode, CustomerData customerData, String refundType, boolean isReturn, SalesApplication salesApplication);


	/**
	 * @param orderDetails
	 * @return List<ReturnLogisticsResponseData>
	 */
	public List<ReturnLogisticsResponseData> checkReturnLogistics(OrderData orderDetails);

	/**
	 * @param subOrderEntry
	 * @param subOrderDetails
	 * @param ticketTypeCode
	 * @param reasonCode
	 * @param refundType
	 * @param ussid
	 * @param subOrderModel
	 * @param customerData
	 *
	 */
	public boolean createTicketInCRM(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String ticketTypeCode, final String reasonCode, final String refundType, final String ussid,
			final CustomerData customerData, final OrderModel subOrderModel);
	
	/**
	 * @author Techouts
	 * @param subOrderDetails
	 * @param subOrderEntry
	 * @param ticketTypeCode
	 * @param reasonCode
	 * @param refundType
	 * @param ussid
	 * @param customerData
	 * @param subOrderModel
	 * @param retunAddress
	 * @return CRMTicketStatus
	 */
	public boolean createTicketInCRM(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String ticketTypeCode, final String reasonCode, final String refundType, final String ussid,
			final CustomerData customerData, final OrderModel subOrderModel,ReturnItemAddressData returnAddress);


	/**
	 * @param order
	 * @param orderLineId
	 * @return
	 * @throws Exception
	 */
	public List<OrderEntryData> associatedEntriesData(OrderModel order, String orderLineId) throws Exception;
	
	/**
	 * @author Techouts
	 * @param orderDetails
	 * @param pincode
	 * @return ReturnLogisticsResponseData List
	 */
	public List<ReturnLogisticsResponseData> checkReturnLogistics(OrderData orderDetails, String pincode);
	
	/**
	 * @author Techouts
	 * @param subOrderDetails
	 * @param subOrderEntry
	 * @param reasonCode
	 * @param ussid
	 * @param ticketTypeCode
	 * @param customerData
	 * @param refundType
	 * @param isReturn
	 * @param salesApplication
	 * @param pinCode
	 * @return Return Item Status
	 */
	public boolean implementReturnItem(OrderData subOrderDetails, OrderEntryData subOrderEntry, String reasonCode, String ussid,
			String ticketTypeCode, CustomerData customerData, String refundType, boolean isReturn,
			SalesApplication salesApplication,ReturnItemAddressData returnAddress);

	
}