package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.jalo.VoucherManager;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class MplNoCostEMIVoucher extends GeneratedMplNoCostEMIVoucher
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplNoCostEMIVoucher.class.getName());

	/**
	 * The Method is Called during Item Creation
	 *
	 * @param ctx
	 * @param type
	 * @param allAttributes
	 */
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		LOG.debug("Inside Create Item");
		final String voucherCode = (String) allAttributes.get("voucherCode");
		if (StringUtils.isNotBlank(voucherCode))
		{
			final Voucher voucher = VoucherManager.getInstance(getSession()).getVoucher(voucherCode);
			if (voucher != null)
			{
				throw new JaloInvalidParameterException(MessageFormat
						.format(Localization.getLocalizedString("type.promotionvoucher.error.vouchercode.not.unique"), new Object[]
				{ voucherCode, voucher.getName() }), 0);
			}
		}

		return super.createItem(ctx, type, allAttributes);
	}

	/**
	 * The Method Checks the Voucher Code
	 *
	 * @param voucherCode
	 */
	@Override
	public boolean checkVoucherCode(final String voucherCode)
	{
		LOG.debug("Checking Voucher Code");
		return voucherCode.equals(getVoucherCode());
	}

	/**
	 * Returns Next Voucher Number
	 *
	 * @param ctx
	 */
	@Override
	protected int getNextVoucherNumber(final SessionContext ctx)
	{
		return 1;
	}

	/**
	 * This set is Reservable Request
	 *
	 * @param voucherCode
	 * @param user
	 */
	@Override
	public boolean isReservable(final String voucherCode, final User user)
	{
		LOG.debug("Checking if Voucher is Reservable");
		return ((getInvalidations(voucherCode, user).size() < getRedemptionQuantityLimitPerUserAsPrimitive())
				&& (getInvalidations(voucherCode).size() < getRedemptionQuantityLimitAsPrimitive()));
	}

	/**
	 * The Method checks the Voucher code for its Duplicate existence
	 *
	 * @param ctx
	 * @param voucherCode
	 */
	@Override
	public void setVoucherCode(final SessionContext ctx, final String voucherCode)
	{
		if (StringUtils.isNotBlank(voucherCode))
		{
			LOG.debug("Validating Voucher Code for its Duplicate Existence");
			final Voucher voucher = VoucherManager.getInstance(getSession()).getVoucher(voucherCode);
			if ((voucher != null) && (voucher != this))
			{
				LOG.debug("Error <<<Duplicate Voucher Exist>>>");
				throw new JaloInvalidParameterException(MessageFormat
						.format(Localization.getLocalizedString("type.promotionvoucher.error.vouchercode.not.unique"), new Object[]
				{ voucherCode, voucher.getName() }), 0);
			}
		}
		super.setVoucherCode(ctx, voucherCode);
	}

}
