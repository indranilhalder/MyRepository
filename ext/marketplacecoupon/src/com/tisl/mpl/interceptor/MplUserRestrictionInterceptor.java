/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class MplUserRestrictionInterceptor implements PrepareInterceptor
{
	private ModelService modelService;

	private static final Logger LOG = Logger.getLogger(MplUserRestrictionInterceptor.class);

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * The Method attaches a flag to Voucher is it contains User Restriction
	 *
	 * @param obj
	 * @param arg1
	 */
	@Override
	public void onPrepare(final Object obj, final InterceptorContext arg1) throws InterceptorException
	{
		if (obj instanceof UserRestrictionModel)
		{
			final UserRestrictionModel restrictModel = (UserRestrictionModel) obj;

			if (CollectionUtils.isNotEmpty(restrictModel.getUsers()))
			{
				final VoucherModel oModel = restrictModel.getVoucher();

				LOG.debug("************Adding User Retricted Flag*****Voucher:::" + oModel.getCode());
				oModel.setIsUserRestricted(true);

				getModelService().save(oModel);

				LOG.debug("************Added User Retricted Flag**********************");
			}
		}

	}

}
