/**
 *
 */
package com.tisl.mpl.integration.oms.order.process.action;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author TCS
 *
 */
public class CustomCancelWholeOrderAction extends AbstractProceduralAction<OrderProcessModel>
{

	private static final Logger LOG = Logger.getLogger(CustomCancelWholeOrderAction.class);
	private PaymentService paymentService;
	//	@Autowired
	//	private CustomOmsOrderService customOmsOrderService;
	//	@Autowired
	//	private MplJusPayRefundService mplJusPayRefundService;

	@Override
	public void executeAction(final OrderProcessModel process)
	{
		final OrderModel order = process.getOrder();

		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Cancelling Order [%s]. Cancelling payment authorisations.", new Object[]
			{ order.getCode() }));
		}
	}

	protected PaymentService getPaymentService()
	{
		return this.paymentService;
	}

	@Required
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

}
