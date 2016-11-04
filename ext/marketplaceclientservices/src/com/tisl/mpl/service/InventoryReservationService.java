/**
 *
 */
package com.tisl.mpl.service;

import java.util.List;

import javax.xml.bind.JAXBException;

import com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.wsdto.EDDRequestWsDTO;
import com.tisl.mpl.wsdto.EDDResponseWsDTO;
import com.tisl.mpl.wsdto.InventoryReservListRequest;
import com.tisl.mpl.wsdto.InventoryReservListResponse;


/**
 * @author TCS
 *
 */
public interface InventoryReservationService
{

	/**
	 * @description convert cart soft reservation data to InventoryReservListResponse object
	 * @param cartdatalist
	 * @param cartId
	 * @param pincode
	 * @param requestType
	 * @return InventoryReservListResponse
	 */
	public InventoryReservListResponse convertDatatoWsdto(List<CartSoftReservationData> cartdatalist, final String cartId,
			final String pincode, final String requestType);

	/**
	 * @description send inventory reservation data to oms and receive response
	 * @param req
	 * @return InventoryReservListResponse
	 * @throws JAXBException
	 */
	public InventoryReservListResponse reserveInventoryAtCheckout(final InventoryReservListRequest request) throws JAXBException;
	
	/**
	 * @param cartdata
	 * @return
	 */
	public EDDResponseWsDTO convertDeliverySlotsDatatoWsdto(InvReserForDeliverySlotsRequestData cartdata);

	/**
	 * @param request
	 * @return
	 * @throws JAXBException
	 */
	public EDDResponseWsDTO getInventoryReservationForDeliverySlots(EDDRequestWsDTO request) throws JAXBException;
}
