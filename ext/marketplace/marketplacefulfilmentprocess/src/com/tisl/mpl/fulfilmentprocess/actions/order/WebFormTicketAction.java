/**
 *
 */
package com.tisl.mpl.fulfilmentprocess.actions.order;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.webform.model.WebFormTicketProcessModel;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService;


/**
 * @author TCS
 *
 */
public class WebFormTicketAction extends AbstractProceduralAction<WebFormTicketProcessModel>
{
	private static final Logger LOG = Logger.getLogger(WebFormTicketAction.class);

	@Resource
	private MplWebFormService mplWebFormService;

	/**
	 * This method is used to override the executeAction method for the web form tickets
	 */
	@Override
	public void executeAction(final WebFormTicketProcessModel webFormTicketProcessModel) throws RetryLaterException, Exception
	{
		LOG.debug("Starting to send ticket data to CRM >>>>>>>>>>");

		mplWebFormService.sendTicketToPI(webFormTicketProcessModel.getMplWebCrmTicket());

		LOG.debug("Finished sending ticket data to CRM >>>>>>>>>>");
	}
}
