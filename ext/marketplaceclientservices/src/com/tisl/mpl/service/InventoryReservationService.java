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
	 * @description send inventory reservation data to oms and receive response
	 * @param req
	 * @return InventoryReservListResponse
	 * @throws JAXBException
	 */
	public InventoryReservListResponse reserveInventoryAtCheckout(final InventoryReservListRequest request) throws JAXBException;

	/**
	 * @param cartSoftReservationDatalist
	 * @param abstractOrderModel
	 * @param defaultPinCodeId
	 * @param requestType
	 * @return
	 */
	public InventoryReservListRequest convertDatatoWsdto(List<CartSoftReservationData> cartSoftReservationDatalist,
			String cartGuid, String defaultPinCodeId, String requestType);
	
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
