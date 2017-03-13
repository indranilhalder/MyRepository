/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.voucher.impl.DefaultVoucherService;
import de.hybris.platform.voucher.jalo.VoucherInvalidation;
import de.hybris.platform.voucher.jalo.VoucherManager;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;


/**
 * @author TCS
 *
 */
public class MplDefaultVoucherService extends DefaultVoucherService
{
	@Override
	public boolean redeemVoucher(final String voucherCode, final CartModel cart) throws JaloPriceFactoryException
	{
		synchronized (cart)
		{
			return VoucherManager.getInstance().redeemVoucher(voucherCode, getCart(cart));
		}
	}

	@Override
	public VoucherInvalidationModel redeemVoucher(final String voucherCode, final OrderModel order)
	{
		synchronized (order)
		{
			final VoucherInvalidation voucherInvalidation = VoucherManager.getInstance().redeemVoucher(voucherCode, getOrder(order));
			if (voucherInvalidation == null)
			{
				return null;
			}

			return ((VoucherInvalidationModel) getModelService().get(voucherInvalidation));
		}
	}

}
