/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author 1209718
 *
 */
public class MidPageController extends AbstractPageController
{
/*This class acts as a bridge between AbstractPageController and Marketplace page controllers
 *
 */
	
	protected static final Logger LOG = Logger.getLogger(MidPageController.class);

	@Override
	protected ContentPageModel getContentPageForLabelOrId(final String labelOrId)
	{
		String key = labelOrId;
		ContentPageModel contentPage;

		final CMSSiteService cmsSiteService = getCmsSiteService();

		final CMSPageService cmsPageService = getCmsPageService();
		try
		{
			if (StringUtils.isEmpty(labelOrId))
			{
				// Fallback to site home page
				final ContentPageModel homePage = cmsPageService.getHomepage();
				if (homePage != null)
				{
					key = cmsPageService.getLabelOrId(homePage);
				}
				else
				{
					// Fallback to site start page label
					final CMSSiteModel site = cmsSiteService.getCurrentSite();
					if (site != null)
					{
						key = cmsSiteService.getStartPageLabelOrId(site);
					}
				}
			}
			
			contentPage = cmsPageService.getPageForLabelOrId(key);
			 
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in MidPageController::getContentPageForLabelOrId");
			throw new EtailNonBusinessExceptions(ex);
		}
		// Actually resolve the label or id - running cms restrictions
		return contentPage;
	}
}
