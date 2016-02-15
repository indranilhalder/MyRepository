/**
 *
 */
package com.tisl.mpl.sms;

import javax.xml.bind.JAXBException;

import com.tisl.mpl.core.model.OrderUpdateSmsProcessModel;
import com.tisl.mpl.data.SendSMSRequestData;



/**
 * @author TCS
 *
 */
public interface SendSmsService
{
	/**
	 * @description Method is called to implement SMS Service
	 * @param request
	 * @throws JAXBException
	 */
	public void sendSMS(final SendSMSRequestData request) throws JAXBException;

	/**
	 * @description Method is called to implement SMS Service for orderStatusUpdate
	 * @param orderUpdateSmsProcessModel
	 * @throws JAXBException
	 */
	public void sendSMSForOrderStatus(final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel) throws JAXBException;

}
