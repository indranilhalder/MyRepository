/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface CouponRestrictionService
{
	List<PrincipalModel> getRestrictionCustomerList(final UserRestrictionModel userRestrObj);

	DateRestrictionModel getDateRestriction(final VoucherModel promoVoucher);

	UserRestrictionModel getUserRestriction(final VoucherModel promoVoucher);

}
