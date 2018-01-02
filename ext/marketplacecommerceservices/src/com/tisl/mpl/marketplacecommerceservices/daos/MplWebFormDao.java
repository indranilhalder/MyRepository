/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.facades.cms.data.WebFormData;


/**
 * @author TCS
 *
 */
public interface MplWebFormDao
{
	public List<MplWebCrmModel> getWebCRMParentNodes();

	public List<MplWebCrmModel> getWebCRMByNodes(String nodeParent);

	public MplWebCrmTicketModel getWebCRMTicket(String commerceTicketId);

	public boolean checkDuplicateWebCRMTickets(WebFormData formData);

}