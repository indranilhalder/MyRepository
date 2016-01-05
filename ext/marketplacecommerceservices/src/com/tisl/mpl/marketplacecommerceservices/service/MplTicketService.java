/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tisl.mpl.model.CRMTicketDetailModel;


/**
 * @author 765463
 *
 */
public interface MplTicketService
{

	public List<CRMTicketDetailModel> findCRMTicketDetail(boolean isTicketCreatedInCRM);
}
