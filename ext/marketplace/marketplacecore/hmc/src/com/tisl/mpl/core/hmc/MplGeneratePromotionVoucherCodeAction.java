/**
 *
 */
package com.tisl.mpl.core.hmc;

import de.hybris.platform.core.Registry;
import de.hybris.platform.hmc.util.action.ActionEvent;
import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.hmc.util.action.ItemAction;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.voucher.jalo.PromotionVoucher;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.model.VoucherModel;

import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.model.MplNoCostEMIVoucherModel;


/**
 * @author TCS
 *
 */
public class MplGeneratePromotionVoucherCodeAction extends ItemAction
{
	@Override
	public ActionResult perform(final ActionEvent event) throws JaloBusinessException
	{
		final Voucher coupon = (Voucher) getItem(event);
		try
		{
			if (coupon instanceof PromotionVoucher)
			{
				final PromotionVoucher voucher = (PromotionVoucher) coupon;
				voucher.setVoucherCode(voucher.generateVoucherCode());
				getModelService().save(getModelService().get(voucher));

				getNotificationService().saveToVoucherStatusNotification((VoucherModel) getModelService().get(voucher));

				return new ActionResult(0, true, false);
			}
			else
			{
				final VoucherModel otherCoupon = getModelService().get(coupon);

				if (otherCoupon instanceof MplNoCostEMIVoucherModel)
				{
					final MplNoCostEMIVoucherModel emiCoupon = (MplNoCostEMIVoucherModel) otherCoupon;
					emiCoupon.setVoucherCode(coupon.generateVoucherCode());
					getModelService().save(emiCoupon);
					getNotificationService().saveToVoucherStatusNotification(emiCoupon);
					return new ActionResult(0, true, false);
				}
			}

		}
		catch (final Exception e)
		{
			throw new JaloBusinessException(e, 0);
		}

		return new ActionResult(0, false, false);

	}

	protected ModelService getModelService()
	{
		return Registry.getApplicationContext().getBean("modelService", ModelService.class);
	}

	protected NotificationService getNotificationService()
	{
		return Registry.getApplicationContext().getBean("notificationService", NotificationService.class);
	}


}
