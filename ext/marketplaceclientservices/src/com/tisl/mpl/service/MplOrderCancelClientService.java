/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.wsdto.TicketUpdateRequestXML;
import com.tisl.mpl.wsdto.TicketUpdateResponseXML;
import com.tisl.mpl.xml.pojo.CODSelfShipmentRequest;
import com.tisl.mpl.xml.pojo.CODSelfShipmentResponse;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest;
import com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse;
import com.tisl.mpl.xml.pojo.RTSAndRSSReturnInfoRequest;
import com.tisl.mpl.xml.pojo.RTSAndRSSReturnInfoResponse;


/**
 * @author TCS
 *
 */
public interface MplOrderCancelClientService
{

	/**
	 * @param cancelOrderRequest
	 * @return MplOrderIsCancellableResponse
	 */
	MplOrderIsCancellableResponse orderCancelDataToOMS(MplCancelOrderRequest cancelOrderRequest);
	
	/**
	 * @author TECHOUTS
	 * 
	 * @param returnOrderRequest
	 * @return MplOrderIsCancellableResponse
	 */
	RTSAndRSSReturnInfoResponse orderReturnInfoOMS(RTSAndRSSReturnInfoRequest returnOrderRequest);
	
	/**
	 * @author TECHOUTS
	 * 
	 * @param codPaymentInfo
	 * @return CODSelfShipmentResponse
	 */
	CODSelfShipmentResponse codPaymentInfoToFICO(CODSelfShipmentRequest codPaymentInfo);
	
	/**
	 * @author TECHOUTS
	 * 
	 * @param codPaymentInfo
	 * @return CODSelfShipmentResponse
	 */
	TicketUpdateResponseXML updateCRMTicket(TicketUpdateRequestXML codPaymentInfo);

}
