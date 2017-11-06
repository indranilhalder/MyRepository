/**
 *
 */
package com.tisl.mpl.service;

import javax.xml.bind.JAXBException;

import com.tisl.mpl.core.model.MplWebCrmTicketModel;


/**
 * @author TCS
 *
 */
public interface ClientIntegration
{
	public boolean sendWebFormTicket(final MplWebCrmTicketModel mplWebCrmTicketModel);

	/**
	 * This method is created to check the duplicate tickets in CRM, using a realtime web service call (TPR-5989)
	 *
	 * @param stringXml
	 * @return success/failure message
	 * @throws JAXBException
	 */
	public String checkDuplicateWebFormTicket(String stringXml) throws JAXBException;

}
