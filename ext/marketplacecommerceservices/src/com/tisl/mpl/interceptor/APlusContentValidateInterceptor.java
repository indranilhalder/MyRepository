/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.APlusContentModel;


/**
 * @author TCS
 *
 */
public class APlusContentValidateInterceptor implements ValidateInterceptor
{

	/**
	 * @author TCS
	 * @description This method validates the mime type to be .csv and empty ness
	 */
	@Override
	public void onValidate(final Object o, final InterceptorContext itx) throws InterceptorException
	{
		if (o instanceof APlusContentModel)
		{
			final APlusContentModel aPlusModel = (APlusContentModel) o;
			if (StringUtils.isEmpty(aPlusModel.getCsvFile().getURL()))
			{
				throw new InterceptorException("Please upload file");
			}
			else if (!aPlusModel.getCsvFile().getMime().equals("text/csv"))
			{
				throw new InterceptorException("Please upload a valid CSV file. MIME type is not text/csv. Current MINE type is "
						+ aPlusModel.getCsvFile().getMime());
			}
		}
	}
}
