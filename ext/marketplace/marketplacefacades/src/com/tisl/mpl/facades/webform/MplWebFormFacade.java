/**
 *
 */
package com.tisl.mpl.facades.webform;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import com.tisl.mpl.facades.cms.data.WebForm;




/**
 * @author TCS
 *
 */
public interface MplWebFormFacade
{

	public WebForm getWebCRMForm(PageableData pageableData);

	public WebForm getWebCRMChildren(final String parentNode);

	public boolean checkDuplicateWebCRMTickets(String ticketType, String orderCode, String subOrderCode, String transactionId,
			String L0code, String L1code, String L2code, String L3code, String L4code, String customerId);
}
