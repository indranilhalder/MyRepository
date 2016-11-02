/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.voucher.model.VoucherInvalidationModel;

import java.util.List;


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
	List<VoucherInvalidationModel> findVoucherInvalidation(String voucherIdentifier, String customerUid, String orderCode);

}
