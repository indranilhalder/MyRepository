/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.VoucherCardPerOfferInvalidationModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.voucher.model.CouponUserRestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.JuspayCardStatusModel;


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

	/**
	 * @param voucher
	 * @param cardFingerprint
	 * @return List<VoucherCardPerOfferInvalidationModel>
	 */
	public List<VoucherCardPerOfferInvalidationModel> findCardPerOfferInvalidation(String guid, VoucherModel voucher,
			String cardFingerprint);

	/**
	 * @param voucher
	 * @param cardFingerprint
	 * @return List<VoucherCardPerOfferInvalidationModel>
	 */
	public List<VoucherCardPerOfferInvalidationModel> findInvalidationMaxAmtPMnth(String guid, VoucherModel voucher,
			String cardFingerprint);

	/**
	 * @param guid
	 * @param customerId
	 * @return List<JuspayCardStatus>
	 */
	public List<JuspayCardStatusModel> findJuspayCardStatus(String guid, String customerId);

	/**
	 * @param voucher
	 * @param cardFingerprint
	 * @return List<VoucherCardPerOfferInvalidationModel>
	 */
	public List<VoucherCardPerOfferInvalidationModel> findInvalidationMaxAmtPDay(String guid, VoucherModel voucher,
			String cardFingerprint);

	/**
	 * @param voucher
	 * @param cardFingerprint
	 * @return List<VoucherCardPerOfferInvalidationModel>
	 */
	public List<VoucherCardPerOfferInvalidationModel> findInvalidationMaxAmtPWeek(String guid, VoucherModel voucher,
			String cardFingerprint);

	/**
	 * @param voucher
	 * @param cardFingerprint
	 * @return List<VoucherCardPerOfferInvalidationModel>
	 */
	public List<VoucherCardPerOfferInvalidationModel> findInvalidationMaxAmtPYear(String guid, VoucherModel voucher,
			String cardFingerprint);


	/* CAR-330 starts here */
	public List<UserRestrictionModel> fetchUserRestrictionDetails(final Date mplConfigDate);

	public List<CouponUserRestrictionModel> fetchExistingVoucherData(final VoucherModel voucher);
	/* CAR-330 ends here */

}
