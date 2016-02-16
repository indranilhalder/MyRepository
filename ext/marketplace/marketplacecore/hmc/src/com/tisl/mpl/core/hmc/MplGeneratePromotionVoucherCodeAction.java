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

import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;


/**
 * @author TCS
 *
 */
public class MplGeneratePromotionVoucherCodeAction extends ItemAction
{
	@Override
	public ActionResult perform(final ActionEvent event) throws JaloBusinessException
	{
		final PromotionVoucher voucher = (PromotionVoucher) getItem(event);
		try
		{
			voucher.setVoucherCode(voucher.generateVoucherCode());
			getModelService().save(getModelService().get(voucher));

			//getNotificationService().saveToVoucherStatusNotification((VoucherModel) getModelService().get(voucher));

			return new ActionResult(0, true, false);
		}
		catch (final Exception e)
		{
			throw new JaloBusinessException(e, 0);
		}
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
