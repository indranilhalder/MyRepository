/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;


/**
 * @author TCS
 *
 */
public interface MplVoucherDao
{


	/**
	 * @param voucherIdentifier
	 * @param customerUid
	 * @param orderCode
	 * @return List<VoucherInvalidationModel>
	 */
	//List<VoucherInvalidationModel> findVoucherInvalidation(String voucherIdentifier, String customerUid, String orderCode);
	public VoucherInvalidationModel findVoucherInvalidation(final VoucherModel voucher, final UserModel user,
			final OrderModel order);

}
