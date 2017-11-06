/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tis.mpl.facade.data.TicketStatusUpdate;
import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.wsdto.TicketMasterXMLData;


/**
 * @author TCS
 *
 */
public interface MplWebFormService
{
	/**
	 * @return List
	 */
	public List<MplWebCrmModel> getWebCRMParentNodes() throws Exception;

	public List<MplWebCrmModel> getWebCRMByNodes(String nodeParent) throws Exception;

	public MplWebCrmTicketModel getWebCRMTicket(String commerceTicketId) throws Exception;

	public boolean checkDuplicateWebCRMTickets(final WebFormData formData) throws Exception;

	/**
	 * This method is created to send the web form ticket to PI after duplication check ( TPR- 5989 )
	 *
	 * @param mplWebCrmTicketModel
	 * @return the success/failure message
	 * @throws Exception
	 */
	public String sendTicketToPI(final MplWebCrmTicketModel mplWebCrmTicketModel) throws Exception;

	public TicketMasterXMLData populateWebformTicketData(final MplWebCrmTicketModel mplWebCrmTicketModel) throws Exception;

	public boolean webformTicketStatusUpdate(final TicketStatusUpdate ticketStatusUpdate) throws Exception;

	public boolean sendWebFormTicket(final WebFormData formData) throws Exception;




}
