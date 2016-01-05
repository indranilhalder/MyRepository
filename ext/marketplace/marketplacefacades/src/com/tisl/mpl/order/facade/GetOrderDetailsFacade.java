/**
 *
 */
package com.tisl.mpl.order.facade;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.facades.data.AWBResponseData;
import com.tisl.mpl.wsdto.OrderDataWsDTO;


/**
 * @author TCS
 *
 */
public interface GetOrderDetailsFacade
{
	public OrderDataWsDTO getOrderdetails(String orderCode);

	public Map<String, List<AWBResponseData>> getOrderStatusTrack(OrderEntryData orderEntryDetail, OrderData subOrder,
			OrderData parentOrder);

}
