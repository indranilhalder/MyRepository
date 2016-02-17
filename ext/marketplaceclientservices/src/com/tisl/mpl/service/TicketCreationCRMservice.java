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
}
