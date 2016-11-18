/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.acceleratorcms.component.cache.impl.DefaultCmsCacheService;
import de.hybris.platform.acceleratorcms.model.components.FooterComponentModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import javax.servlet.http.HttpServletRequest;


/**
 * @author TCS
 *
 */
public class MplCmsCacheServiceImpl extends DefaultCmsCacheService
{
	@Override
	public boolean useCache(final HttpServletRequest request, final AbstractCMSComponentModel component)
	{
		boolean cacheEnabled = request != null && component != null && useCacheInternal() && !isPreviewOrLiveEditEnabled(request);
		/* TPR-1282 : For footer component, the cms-cache-enablity is set to be false */
		if (component instanceof FooterComponentModel)
		{
			cacheEnabled = false;
		}
		return cacheEnabled;
	}
}
