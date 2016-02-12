/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.model.CRMTicketDetailModel;


/**
 * @author 765463
 *
 */
public interface MplTicketDao
{
	public List<CRMTicketDetailModel> findCRMTicketDetail(final boolean isTicketCreatedInCRM);
}
