/**
 *
 */
package com.tisl.mpl.facades.account.cancelreturn;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.MplReturnPickUpAddressInfoModel;
import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.data.CODSelfShipResponseData;
import com.tisl.mpl.data.CRMTicketUpdateData;
import com.tisl.mpl.data.CRMTicketUpdateResponseData;
import com.tisl.mpl.data.OrderLineData;
import com.tisl.mpl.data.RTSAndRSSReturnInfoRequestData;
import com.tisl.mpl.data.RTSAndRSSReturnInfoResponseData;
import com.tisl.mpl.data.ReturnInfoData;
import com.tisl.mpl.data.ReturnLogisticsResponseData;
import com.tisl.mpl.facades.data.ReturnItemAddressData;
import com.tisl.mpl.wsdto.ReturnPincodeDTO;
import com.tisl.mpl.wsdto.ReturnRequestDTO;



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
			final CustomerData customerData, final OrderModel subOrderModel, final boolean returnLogisticsCheck);

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
	 * @param returnAddress
	 * @param returnInfoData
	 * @return CRMTicketStatus
	 */
	public boolean createTicketInCRM(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String ticketTypeCode, final String reasonCode, final String refundType, final String ussid,
			final CustomerData customerData, final OrderModel subOrderModel, ReturnItemAddressData returnAddress,
			ReturnInfoData returnInfoData);


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
	 * @param customerData
	 * @param salesApplication
	 * @return Return Item Status
	 */
	public boolean implementReturnItem(OrderData subOrderDetails, OrderEntryData subOrderEntry, ReturnInfoData returnData,
			CustomerData customerData, SalesApplication salesApplication, ReturnItemAddressData returnAddress);


	/**
	 *
	 * @param returnRequestData
	 * @return RTSAndRSSReturnInfoRequestData
	 */
	public RTSAndRSSReturnInfoResponseData retrunInfoCallToOMS(final RTSAndRSSReturnInfoRequestData returnRequestData);

	/**
	 *
	 * @param codSelfShipData
	 * @return RTSAndRSSReturnInfoRequestData
	 */
	public CODSelfShipResponseData codPaymentInfoToFICO(final CODSelfShipData codSelfShipData);

	/**
	 *
	 * @param updateTicketData
	 * @return CRMTicketUpdateResponseData
	 */
	public CRMTicketUpdateResponseData updateCRMTicket(final CRMTicketUpdateData updateTicketData);



	/**
	 * @param orderEntryStatus
	 * @return String
	 */
	String getOrderStatusStage(String orderEntryStatus);


	/**
	 *
	 * @param ussid
	 * @return List<String>
	 */
	List<String> getReturnableDates(OrderEntryData ussid);

	/**
	 * @param codSelfShipData
	 */
	public void saveCODReturnsBankDetails(CODSelfShipData codSelfShipData);

	/**
	 * @param codSelfShipData
	 */
	public void insertUpdateCustomerBankDetails(CODSelfShipData codSelfShipData);

	/**
	 * @param customerId
	 */
	public CODSelfShipData getCustomerBankDetailsByCustomerId(String customerId);


	public List<ReturnRequestModel> getListOfReturnRequest(String orderId);


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
	 * @param revSealJwlry
	 * @param pinCode
	 * @return Return Item Status
	 */
	public boolean implementReturnItem(OrderData subOrderDetails, OrderEntryData subOrderEntry, String reasonCode, String ussid,
			String ticketTypeCode, CustomerData customerData, String refundType, boolean isReturn,
			SalesApplication salesApplication, ReturnItemAddressData returnAddress, String revSealJwlry);


	/**
	 * @param orderDetails
	 * @param pincode
	 * @param transId
	 * @return
	 */

	List<ReturnLogisticsResponseData> checkReturnLogistics(OrderData orderDetails, String pincode, String transId);


	/**
	 * @param subOrderDetails
	 * @param subOrderEntry
	 * @param ticketTypeCode
	 * @param reasonCode
	 * @param refundType
	 * @param ussid
	 * @param customerData
	 * @param subOrderModel
	 * @param returnAddress
	 * @return
	 */
	boolean createTicketInCRM(OrderData subOrderDetails, OrderEntryData subOrderEntry, String ticketTypeCode, String reasonCode,
			String refundType, String ussid, CustomerData customerData, OrderModel subOrderModel,
			ReturnItemAddressData returnAddress, String revSealJwlry);

	/**
	 *
	 * @param orerLines
	 * @return List of Order Lines
	 */
	public List<OrderLineData> returnInitiationForRTS(List<OrderLineData> orerLines);

	public void saveRTSAndRSSFInfoflag(String transactionId);

	public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByDates(Date fromDate, Date toDate);

	public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByParams(String orderID, String customerId, String pincode);


	/**
	 * @param orderCode
	 * @param transactionId
	 * @return
	 * @throws Exception
	 */
	boolean orderCancellationFromBackoffice(String orderCode, String transactionId) throws Exception;

	public void returnRssCRMRequest(ReturnRequestDTO returnRequestDTO);

	public ReturnPincodeDTO checkReturnLogisticsForApp(final OrderData orderDetails, final String pincode,
			final String returntransactionId);


	//TPR-1345:One touch cancel return--START
	/**
	 * Method: for cancellation part of one touch CRM--TPR-1345
	 *
	 * @param subOrderDetails
	 * @param orderEntry
	 * @param reasonCode
	 * @param ussid
	 * @param ticketTypeCode
	 * @param refundType
	 * @param isReturn
	 * @param salesApplication
	 * @return OMS cancellation status
	 */
	public boolean oneTouchCancel(OrderModel subOrderModel, OrderData subOrderDetails, OrderEntryData orderEntry,
			String reasonCode, String ussid, String ticketTypeCode, String refundType, boolean isReturn,
			SalesApplication salesApplication, List<AbstractOrderEntryModel> abstractOrderEntryModel);

	/**
	 * Method: for Return part of one touch CRM--TPR-1345
	 *
	 * @param subOrderDetails
	 * @param orderEntry
	 * @param reasonCode
	 * @param ticketTypeCode
	 * @param isReturn
	 * @param salesApplication
	 * @param returnPincode
	 * @return OMS cancellation status
	 */
	public boolean oneTouchReturn(OrderData subOrderDetails, OrderEntryData orderEntry, String reasonCode, String ticketTypeCode,
			SalesApplication salesApplication, String returnPincode, List<AbstractOrderEntryModel> orderEntriesModel,
			OrderModel subOrderModel, CODSelfShipData codSelfShipData, final String ussid, String txnId);

	/**
	 * Method: for pincode serviceability part of one touch CRM--TPR-1345
	 *
	 * @param subOrderDetails
	 * @param returnPincode
	 * @param txnId
	 * @return true, if serviceable
	 * @throws Exception
	 */
	public boolean oneTouchPincodeCheck(OrderData subOrderDetails, String returnPincode, String txnId) throws Exception;

	public List<AbstractOrderEntryModel> associatedEntries(final OrderModel subOrderDetails, final String transactionId)
			throws Exception;
	//TPR-1345:One touch cancel return--END

}