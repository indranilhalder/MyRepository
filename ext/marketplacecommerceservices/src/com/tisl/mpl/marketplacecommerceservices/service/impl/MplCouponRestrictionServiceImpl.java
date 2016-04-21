/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.List;

import com.tisl.mpl.marketplacecommerceservices.service.CouponRestrictionService;


/**
 * @author TCS
 *
 */
public class MplCouponRestrictionServiceImpl implements CouponRestrictionService
{
	/**
	 * This method returns restriction object for a voucher
	 *
	 * @param promoVoucher
	 * @return list of users
	 *
	 */
	@Override
	public UserRestrictionModel getUserRestriction(final VoucherModel promoVoucher)
	{
		final List<RestrictionModel> restrictionList = new ArrayList<RestrictionModel>(promoVoucher.getRestrictions());
		UserRestrictionModel userRestrObj = null;

		for (final RestrictionModel restrictionModel : restrictionList)
		{
			if (restrictionModel instanceof UserRestrictionModel)
			{
				userRestrObj = (UserRestrictionModel) restrictionModel;
				break;
			}
		}

		return userRestrObj;
	}

	/**
	 * This method returns restriction object for a voucher
	 *
	 * @param userRestrObj
	 * @return list of users
	 *
	 */
	@Override
	public List<PrincipalModel> getRestrictionCustomerList(final UserRestrictionModel userRestrObj)
	{
		final List<PrincipalModel> restrCustomerList = new ArrayList<PrincipalModel>();

		if (userRestrObj != null)
		{
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
		}

		return restrCustomerList;
	}


	/**
	 * This method returns restriction object for a voucher
	 *
	 * @param promoVoucher
	 * @return DateRestrictionModel
	 *
	 */
	@Override
	public DateRestrictionModel getDateRestriction(final VoucherModel promoVoucher)
	{
		DateRestrictionModel dateRestrObj = null;
		final List<RestrictionModel> restrictionList = new ArrayList<RestrictionModel>(promoVoucher.getRestrictions());

		for (final RestrictionModel restrictionModel : restrictionList)
		{
			if (restrictionModel instanceof DateRestrictionModel)
			{
				dateRestrObj = (DateRestrictionModel) restrictionModel;
				break;
			}
		}

		return dateRestrObj;
	}



}
