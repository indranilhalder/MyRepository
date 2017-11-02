/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.OrderModel;

import javax.xml.bind.JAXBException;

import com.tisl.mpl.core.model.MplWebCrmTicketModel;
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
	public void ticketCreationModeltoWsDTO(final SendTicketRequestData sendTicketRequestData, Boolean overloadParam)
			throws JAXBException;

	/**
	 * This method is created to check the duplicate web from ticket ( TPR-5989 )
	 *
	 * @param mplWebCrmTicketModel
	 * @return String message success or failure
	 * @throws JAXBException
	 */
	public String checkDuplicateWebFormTicket(final MplWebCrmTicketModel mplWebCrmTicketModel) throws Exception;

	/**
	 * This method is created to populate the data for crm ticket
	 *
	 * @param mplWebCrmTicketModel
	 * @param subOrderModel
	 * @param orderData
	 * @param orderEntry
	 * @return TicketMasterXMLData
	 * @throws Exception
	 */
	public TicketMasterXMLData populateWebFormData(final MplWebCrmTicketModel mplWebCrmTicketModel,
			final OrderModel subOrderModel, final OrderData orderData, final OrderEntryData orderEntry) throws Exception;

}
