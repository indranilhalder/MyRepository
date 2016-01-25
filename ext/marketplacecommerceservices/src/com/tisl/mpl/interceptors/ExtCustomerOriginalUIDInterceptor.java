/**
 * 
 */
package com.tisl.mpl.interceptors;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.springframework.util.StringUtils;


/**
 * @author 314180
 * 
 */
public class ExtCustomerOriginalUIDInterceptor implements PrepareInterceptor
{
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (!(model instanceof CustomerModel))
		{
			return;
		}
		final CustomerModel customer = (CustomerModel) model;

		if (ctx.isNew(customer))
		{
			adjustUid(customer);
		}
		else
		{
			if ((!(ctx.isModified(model, "originalUid"))) && (!(ctx.isModified(model, "uid"))))
			{
				return;
			}
			adjustUid(customer);
		}
	}

	protected void adjustUid(final CustomerModel customer)
	{
		final String original = customer.getOriginalUid();
		final String uid = customer.getUid();
		/*
		 * System.out.println("original" + original.toString()); System.out.println("uid" + uid.toString());
		 */
		/*
		 * if (StringUtils.isNotEmpty(uid)) { if (!(uid.equals(uid.toLowerCase()))) { customer.setUid(uid.toLowerCase());
		 * //customer.setOriginalUid(uid); } else { if (uid.equalsIgnoreCase(original)) { return; }
		 * //customer.setOriginalUid(uid); } } else { if (!(StringUtils.isNotEmpty(original))) { return; }
		 * //customer.setUid(original.toLowerCase()); }
		 */

		if (!StringUtils.hasText(original))
		{
			customer.setOriginalUid(uid);
		}
	}
}
