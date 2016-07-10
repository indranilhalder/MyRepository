/**
 *
 */
package marketplaceclientservices.process.sms.actions;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;

import com.tisl.mpl.core.model.OrderUpdateSmsProcessModel;
import com.tisl.mpl.sms.MplSendSMSService;


/**
 * @author TCS
 *
 */
public class SendSMSAction extends AbstractProceduralAction<OrderUpdateSmsProcessModel>
{
	/**
	 * @return the sendSMSService
	 */
	public MplSendSMSService getSendSMSService()
	{
		return sendSMSService;
	}



	/**
	 * @param sendSMSService
	 *           the sendSMSService to set
	 */
	public void setSendSMSService(final MplSendSMSService sendSMSService)
	{
		this.sendSMSService = sendSMSService;
	}



	private MplSendSMSService sendSMSService;


	/**
	 * @param orderUpdateSmsProcessModel
	 * @description This method is used to override the execute method of AbstractProceduralAction
	 */
	@Override
	public void executeAction(final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel) throws RetryLaterException, Exception
	{
		getSendSMSService().sendSMSForOrderStatus(orderUpdateSmsProcessModel);

	}


}
