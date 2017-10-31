/**
 *
 */
package com.tisl.mpl.fulfilmentprocess.actions.order;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.webform.model.WebFormTicketProcessModel;

import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class WebFormTicketAction extends AbstractProceduralAction<WebFormTicketProcessModel>
{
	private static final Logger LOG = Logger.getLogger(WebFormTicketAction.class);

	/**
	 * This method is used to override the executeAction method for the web form tickets
	 */
	@Override
	public void executeAction(final WebFormTicketProcessModel arg0) throws RetryLaterException, Exception
	{
		System.out.println("==========================================  Working fine now====================================== ");

	}

}
