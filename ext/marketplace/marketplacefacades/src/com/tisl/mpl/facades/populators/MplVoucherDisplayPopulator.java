/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.VoucherDisplayData;


/**
 * @author TCS
 *
 */
public class MplVoucherDisplayPopulator implements Populator<VoucherModel, VoucherDisplayData>
{

	@Autowired
	private UserService userService;
	@Autowired
	private VoucherModelService voucherModelService;

	@Override
	public void populate(final VoucherModel source, final VoucherDisplayData target)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);

		final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.COUPONS_DATE_FORMAT);

		if (source instanceof PromotionVoucherModel)
		{
			final PromotionVoucherModel promoVoucher = (PromotionVoucherModel) source;
			final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
			final List<PrincipalModel> restrCustomerList = getRestrictionCustomerList(promoVoucher);


			if (null != promoVoucher.getVoucherCode()
					&& voucherModelService.isReservable(promoVoucher, promoVoucher.getVoucherCode(), customer)
					&& restrCustomerList.contains(customer))
			{

				final DateRestrictionModel dateRestriction = getDateRestriction(promoVoucher);
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



	private List<PrincipalModel> getRestrictionCustomerList(final PromotionVoucherModel promoVoucher)
	{
		final List<RestrictionModel> restrictionList = new ArrayList<RestrictionModel>(promoVoucher.getRestrictions());
		UserRestrictionModel userRestrObj = null;
		final List<PrincipalModel> restrCustomerList = new ArrayList<PrincipalModel>();

		for (final RestrictionModel restrictionModel : restrictionList)
		{
			if (restrictionModel instanceof UserRestrictionModel)
			{
				userRestrObj = (UserRestrictionModel) restrictionModel;

				for (final PrincipalModel user : userRestrObj.getUsers())
				{
					if (user instanceof UserGroupModel)
					{
						restrCustomerList.addAll(((UserGroupModel) user).getMembers());

					}
					else if (user instanceof UserModel)
					{
						restrCustomerList.add(user);

					}
				}
				break;

			}
		}

		return restrCustomerList;
	}

	private DateRestrictionModel getDateRestriction(final PromotionVoucherModel promoVoucher)
	{
		final DateRestrictionModel dateRestrictionModel = null;
		for (final RestrictionModel restrictionModel : promoVoucher.getRestrictions())
		{
			if (restrictionModel instanceof DateRestrictionModel)
			{
				return (DateRestrictionModel) restrictionModel;
			}
		}
		return dateRestrictionModel;
	}



}
