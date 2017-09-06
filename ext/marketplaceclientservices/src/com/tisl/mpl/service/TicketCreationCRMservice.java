/**
 *
 */
package com.tisl.mpl.service;

import javax.xml.bind.JAXBException;

import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.wsdto.TicketMasterXMLData;


/**
 * @author TCS
 * @Description: Generate XML Data when ticket has been created
 * @param sendTicketRequestData
 * @retun void
 */
public interface TicketCreationCRMservice
{
	public void ticketCreationModeltoWsDTO(final SendTicketRequestData sendTicketRequestData) throws JAXBException;

	public void ticketCreationCRM(final TicketMasterXMLData ticketMasterXml) throws JAXBException;

	public TicketMasterXMLData ticketCreationModeltoXMLData(final SendTicketRequestData sendTicketRequestData);

	public int createTicketInCRM(final String requestXml) throws JAXBException;

	public String createCRMRequestXml(final TicketMasterXMLData ticketMasterXml) throws JAXBException;

	/**
	 * The existing ticket population method has been overloaded to incorporate the changes need for ticket structure for
	 * return and cancel scenarios || TPR-6778
	 * 
	 * @param sendTicketRequestData
	 * @param overloadParam
	 * @throws JAXBException
	 */
	public void ticketCreationModeltoWsDTO(final SendTicketRequestData sendTicketRequestData, boolean overloadParam)
			throws JAXBException;
}
