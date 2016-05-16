/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.latestoffers.LatestOffersFacade;
import com.tisl.mpl.facades.data.LatestOffersData;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
@RequestMapping(value = "/listOffers")
public class LatestOffersController
{

	@Resource(name = "cmsPageService")
	private MplCmsPageService cmsPageService;
	@Autowired
	private LatestOffersFacade latestOffersFacade;

	private static final String HOMEPAGE = "homepage";
	private static final String VERSION = "version";

	@RequestMapping(method = RequestMethod.GET)
	public String get(final Model model, @RequestParam(VERSION) final String version)
	{
		LatestOffersData latestOffersData = new LatestOffersData();
		try
		{
			final ContentSlotModel homepageHeaderConcierge = cmsPageService.getContentSlotByUidForPage(HOMEPAGE, "HeaderLinksSlot",
					version);
			latestOffersData = latestOffersFacade.getLatestOffers(homepageHeaderConcierge);
			model.addAttribute("latestOffersData", latestOffersData);
		}

		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);

		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}
		//return getBestPicksJson;

		return ControllerConstants.Views.Fragments.Home.HelpMeShop;
	}


}
