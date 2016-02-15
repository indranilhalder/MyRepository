/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.voucher.model.VoucherModel;

import javax.annotation.Resource;

import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;


/**
 * @author TCS
 *
 */
public class CouponSaveInterceptor implements PrepareInterceptor
{

	@Resource(name = "notificationService")
	private NotificationService notificationService;


	/**
	 * @Description : This Method is evaluated when voucher is created
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	public void onPrepare(final Object param, final InterceptorContext paramInterceptorContext) throws InterceptorException
	{
		if (null != param && param instanceof VoucherModel)
		{
			final VoucherModel voucher = (VoucherModel) param;
			getNotificationService().saveToVoucherStatusNotification(voucher);
		}
	}


	/**
	 * @return the notificationService
	 */
	public NotificationService getNotificationService()
	{
		return notificationService;
	}


	/**
	 * @param notificationService
	 *           the notificationService to set
	 */
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}





}
