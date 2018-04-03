/**
 *
 */
package com.tisl.mpl.order.facade;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tisl.mpl.facades.data.AWBResponseData;
import com.tisl.mpl.wsdto.OrderDataWsDTO;
import com.tisl.mpl.wsdto.OrderProductWsDTO;
import com.tisl.mpl.wsdto.OrderTrackingWsDTO;


/**
 * @author TCS
 *
 */
public interface GetOrderDetailsFacade
{
	/**
	 * @param orderDetails
	 * @return
	 */
	public OrderDataWsDTO getOrderdetails(final OrderData orderDetails, boolean isPwa);

	/**
	 * @param orderCode
	 * @return
	 */
	public OrderTrackingWsDTO getOrderDetailsWithTracking(final HttpServletRequest request, String orderCode);

	/**
	 * @param orderEntryDetail
	 * @param subOrder
	 * @param subOrderModel
	 * @return
	 */
	public Map<String, List<AWBResponseData>> getOrderStatusTrack(OrderEntryData orderEntryDetail, OrderData subOrder,
			OrderModel subOrderModel);

	/**
	 * @param parentOrder
	 * @return
	 */
	public boolean isPickUpButtonEditable(OrderData parentOrder);

	/**
	 * @return
	 */
	public List<ConsignmentStatus> getPickUpButtonDisableOptions();



	public List<OrderProductWsDTO> getOrderdetailsForApp(final String orderCode);

	public List<OrderProductWsDTO> getOrderdetailsForApp(final String orderCode, final String returnCancelFlag,
			final String transactionId);


	/**
	 * @param orderEntryDetail
	 * @param subOrder
	 * @param parentOrder
	 * @return
	 */
	Map<String, List<AWBResponseData>> getOrderStatusTrack(OrderEntryData orderEntryDetail, OrderData subOrder,
			OrderData parentOrder);

}
