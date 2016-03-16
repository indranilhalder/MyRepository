/**
 *
 */
package com.tisl.mpl.integration.oms.order.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.integration.oms.order.data.OrderPlacementResult;
import de.hybris.platform.integration.oms.order.service.OmsOrderService;


/**
 * @author TCS
 *
 */
public interface MplOmsOrderService extends OmsOrderService
{
	public abstract OrderPlacementResult createCrmOrder(OrderModel paramOrderModel);

}
