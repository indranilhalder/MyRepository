/**
 *
 */
package marketplaceclientservices.process.sms.actions;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class RemoveSentSMSAction extends AbstractProceduralAction

{

	private static final Logger LOG = Logger.getLogger(RemoveSentSMSAction.class);

	/**
	 * @param businessProcessModel
	 * @description This method is used to override the executeAction method of AbstractProceduralAction
	 */
	@Override
	public void executeAction(final BusinessProcessModel businessProcessModel)
	{
		LOG.info("Remove Sent Sms");
	}


}
