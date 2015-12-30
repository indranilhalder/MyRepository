/**
 *
 */
package com.tisl.mpl.marketplaceomsservices.event;

/**
 * @author 884206
 *
 */

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.order.Order;
import com.tisl.mpl.service.MplCustomerWebService;
import com.tisl.mpl.service.MplSendOrderFromCommerceToCRM;


public class SendOrderToCRMEventListner extends AbstractEventListener<SendOrderToCRMEvent>
{

	private static final Logger LOG = Logger.getLogger(SendOrderToCRMEventListner.class);
	private Converter<OrderModel, Order> orderConverter;
	@Autowired
	private MplSendOrderFromCommerceToCRM ordercreation;
	@Autowired
	private MplCustomerWebService mplCustomerWebService;

	public Converter<OrderModel, Order> getOrderConverter()
	{
		return this.orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, Order> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	Order order = null;


	@Override
	protected void onEvent(final SendOrderToCRMEvent sendOrderToCRMEvent)
	{
		LOG.info("inside event listner");
		final OrderModel orderModel = sendOrderToCRMEvent.getOrderModel();
		LOG.info("inside event listner: got order model");

		order = getOrderConverter().convert(orderModel);
		ordercreation.orderCreationDataToCRM(order);
		LOG.debug(">>>>>>>>>>>>> After CRM order call <<<<<<<<<<<<<<<<");
		if (null != orderModel.getUser().getUid())
		{
			LOG.info(">>>>>>>>>>>>> calling customer update after order place <<<<<<<<<<<<<<<<");
			mplCustomerWebService.customerModeltoWsData((CustomerModel) orderModel.getUser(), "U", false);
			LOG.info(">>>>>>>>>>>>>******* customer update success *********<<<<<<<<<<<<<<<<");
		}
	}

}
