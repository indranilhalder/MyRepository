/**
 *
 */
package com.tisl.mpl.facades.webform;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import java.util.List;

import com.tis.mpl.facade.data.TicketStatusUpdate;
import com.tisl.mpl.facades.cms.data.WebForm;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.wsdto.CRMWebFormDataRequest;
import com.tisl.mpl.wsdto.CRMWebFormDataResponse;
import com.tisl.mpl.wsdto.CRMWsData;




/**
 * @author TCS
 *
 */
public interface MplWebFormFacade
{

	public WebForm getWebCRMForm(PageableData pageableData);

	public WebForm getWebCRMChildren(final String parentNode);

	public boolean checkDuplicateWebCRMTickets(WebFormData formData);

	/**
	 * This nethod is created to update the ticket status in commerce DB, from the realtime response received from CRM
	 * (TPR-5989)
	 *
	 * @param ticketStatusUpdate
	 * @return success/failure boolean response
	 */
	public boolean webFormticketStatusUpdate(final TicketStatusUpdate ticketStatusUpdate);

	/**
	 * This method is created to send the ticket to CRM via PI
	 *
	 * @param formData
	 * @return the success/failure boolean response
	 */
	public String sendWebformTicket(final WebFormData formData) throws Exception;

	/**
	 * @return
	 */
	public List<CRMWsData> getAllWebCRMTreedata();

	/**
	 * @param crmTicket
	 * @return
	 */
	public CRMWebFormDataResponse getTicketSubmitForm(CRMWebFormDataRequest crmTicket);

}
