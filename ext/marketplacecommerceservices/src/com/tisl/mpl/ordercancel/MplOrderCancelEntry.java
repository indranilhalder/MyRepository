/**
 *
 */
package com.tisl.mpl.ordercancel;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;


/**
 * @author TCS
 *
 */
public class MplOrderCancelEntry extends OrderCancelEntry
{
	private String cancellationReason;

	/**
	 * @return the cancellationReason
	 */
	public String getCancellationReason()
	{
		return cancellationReason;
	}


	/**
	 * @param cancellationReason
	 *           the cancellationReason to set
	 */
	public void setCancellationReason(final String cancellationReason)
	{
		this.cancellationReason = cancellationReason;
	}


	/**
	 * @param orderEntry
	 */
	public MplOrderCancelEntry(final AbstractOrderEntryModel orderEntry)
	{
		super(orderEntry);
		// YTODO Auto-generated constructor stub
	}


	public MplOrderCancelEntry(final AbstractOrderEntryModel orderEntry, final long cancelQuantity, final String notes,
			final CancelReason cancelReason)
	{
		super(orderEntry, cancelQuantity, notes, CancelReason.NA);

	}

	public MplOrderCancelEntry(final AbstractOrderEntryModel orderEntry, final long cancelQuantity, final String notes,
			final String cancelReason)
	{

		super(orderEntry, cancelQuantity, notes, CancelReason.valueOf(cancelReason));


	}


}
