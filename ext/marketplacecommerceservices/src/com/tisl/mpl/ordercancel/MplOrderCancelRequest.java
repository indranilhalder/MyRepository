/**
 *
 */
package com.tisl.mpl.ordercancel;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelRequest;

import java.util.List;


/**
 * @author TCS
 *
 */
public class MplOrderCancelRequest extends OrderCancelRequest
{

	//Constructor calling the super method
	public MplOrderCancelRequest(final OrderModel order, final List<OrderCancelEntry> orderCancelEntries)
	{
		super(order, orderCancelEntries);

	}

	private Double amountToRefund;
	
	private Double amountTORefundForQc;

	/**
	 * @return the amountToRefund
	 */
	public Double getAmountToRefund()
	{
		return amountToRefund;
	}

	/**
	 * @param amountToRefund
	 *           the amountToRefund to set
	 */
	public void setAmountToRefund(final Double amountToRefund)
	{
		this.amountToRefund = amountToRefund;
	}

	/**
	 * @return the amountTORefundForQc
	 */
	public Double getAmountTORefundForQc()
	{
		return amountTORefundForQc;
	}

	/**
	 * @param amountTORefundForQc the amountTORefundForQc to set
	 */
	public void setAmountTORefundForQc(Double amountTORefundForQc)
	{
		this.amountTORefundForQc = amountTORefundForQc;
	}









}
