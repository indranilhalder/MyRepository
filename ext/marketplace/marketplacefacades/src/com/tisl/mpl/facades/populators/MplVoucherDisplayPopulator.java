/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.marketplacecommerceservices.service.CouponRestrictionService;


/**
 * @author TCS
 *
 */
public class MplVoucherDisplayPopulator implements Populator<VoucherModel, VoucherDisplayData>
{

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "voucherModelService")
	private VoucherModelService voucherModelService;
	@Resource(name = "couponRestrictionService")
	private CouponRestrictionService couponRestrictionService;


	/**
	 * @param source
	 * @param target
	 */
	@Override
	public void populate(final VoucherModel source, final VoucherDisplayData target)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);

		final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.COUPONS_DATE_FORMAT);

		if (source instanceof PromotionVoucherModel)
		{
			final PromotionVoucherModel promoVoucher = (PromotionVoucherModel) source;
			final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
			final UserRestrictionModel userRestrObj = getCouponRestrictionService().getUserRestriction(promoVoucher);
			final List<PrincipalModel> restrCustomerList = userRestrObj != null ? getCouponRestrictionService()
					.getRestrictionCustomerList(userRestrObj) : new ArrayList<PrincipalModel>();

			if (null != promoVoucher.getVoucherCode()
					&& getVoucherModelService().isReservable(promoVoucher, promoVoucher.getVoucherCode(), customer)
					&& restrCustomerList.contains(customer))
			{

				final DateRestrictionModel dateRestriction = getCouponRestrictionService().getDateRestriction(promoVoucher);
				final Date endDate = dateRestriction.getEndDate() != null ? dateRestriction.getEndDate() : new Date();
				final Date startDate = dateRestriction.getStartDate();

				target.setVoucherCode(promoVoucher.getVoucherCode());
				target.setVoucherDescription(promoVoucher.getDescription());
				target.setReedemCouponCount(String.valueOf(promoVoucher.getRedemptionQuantityLimitPerUser()));
				target.setVoucherExpiryDate(sdf.format(endDate));
				target.setVoucherCreationDate(startDate);

			}
		}

	}//PromotionVoucher

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the voucherModelService
	 */
	public VoucherModelService getVoucherModelService()
	{
		return voucherModelService;
	}

	/**
	 * @param voucherModelService
	 *           the voucherModelService to set
	 */
	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
	}

	/**
	 * @return the couponRestrictionService
	 */
	public CouponRestrictionService getCouponRestrictionService()
	{
		return couponRestrictionService;
	}

	/**
	 * @param couponRestrictionService
	 *           the couponRestrictionService to set
	 */
	public void setCouponRestrictionService(final CouponRestrictionService couponRestrictionService)
	{
		this.couponRestrictionService = couponRestrictionService;
	}




}
