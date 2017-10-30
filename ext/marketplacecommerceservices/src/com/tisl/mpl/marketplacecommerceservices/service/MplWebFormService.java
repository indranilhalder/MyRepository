/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;


/**
 * @author TCS
 *
 */
public interface MplWebFormService
{
	/**
	 * @return List
	 */
	public List<MplWebCrmModel> getWebCRMParentNodes();

	public List<MplWebCrmModel> getWebCRMByNodes(String nodeParent);

	public MplWebCrmTicketModel getWebCRMTicket(String commerceTicketId);

	public boolean checkDuplicateWebCRMTickets(String ticketType, String orderCode, String subOrderCode, String transactionId,
			String L0code, String L1code, String L2code, String L3code, String L4code, String customerId);
}
