/**
 *
 */
package com.tisl.mpl.service;

import java.util.List;

import javax.xml.bind.JAXBException;

import com.tisl.mpl.mplcommerceservices.service.data.RefundInfo;
import com.tisl.mpl.xml.pojo.RefundInfoResponse;
import com.tisl.mpl.xml.pojo.RefundStatusXMLData;


/**
 * @author TCS
 *
 */
public interface MplRefundStatusService
{
	/**
	 * @Description : For Refund Status service,need to receive data from commerce and send to oms
	 * @param: refundInfolist
	 * @param: orderRefNo
	 * @param: transactionId
	 * @param: oMSStatusCode
	 * @return: response from oms
	 */
	public RefundInfoResponse refundStatusDatatoWsdto(final List<RefundInfo> refundInfolist, final String orderRefNo,
			final String transactionId, final String oMSStatusCode);

	/**
	 * @Description : Method to fetch the response from OMS regarding refund information
	 * @param: req
	 * @return: responsefromOMS
	 */

	public RefundInfoResponse refundStatus(final RefundStatusXMLData req) throws JAXBException;

}
