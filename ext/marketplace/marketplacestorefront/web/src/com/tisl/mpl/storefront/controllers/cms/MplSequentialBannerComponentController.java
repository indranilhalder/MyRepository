/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.MplBigFourPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.model.cms.components.MplSequentialBannerComponentModel;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@Controller("MplSequentialBannerComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.MplSequentialBannerComponent)
public class MplSequentialBannerComponentController extends AbstractCMSComponentController<MplSequentialBannerComponentModel>
{


	@Resource(name = "sessionService")
	private SessionService sessionService;

	private static final String SEQUENCE_NUMBER = "sequenceNumber";

	/**
	 * This method calculates the sequence number and fetches the appropriate banner for the sequence number.
	 *
	 * @param request
	 *           HttpServletRequest
	 * @param model
	 *           Model
	 * @param component
	 *           MplSequentialBannerComponentModel
	 * @return
	 */
	@SuppressWarnings("boxing")
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final MplSequentialBannerComponentModel component)
	{
		final int firstSequenceNumber = 1;
		//Show the default banner for a new session
		if (sessionService.getAttribute(SEQUENCE_NUMBER) == null)
		{
			model.addAttribute(ModelAttributetConstants.BANNER, getBannerforSequenceNumber(firstSequenceNumber, component));
			sessionService.setAttribute(SEQUENCE_NUMBER, firstSequenceNumber);
		}


		else
		{

			final int lastSequenceNumber = (int) sessionService.getAttribute(SEQUENCE_NUMBER);
			final int nextSequenceNumber = lastSequenceNumber + 1;

			if (getBannerforSequenceNumber(nextSequenceNumber, component) != null)
			{
				model.addAttribute(ModelAttributetConstants.BANNER, getBannerforSequenceNumber(nextSequenceNumber, component));
				sessionService.setAttribute(SEQUENCE_NUMBER, nextSequenceNumber);
			}
			else
			{
				model.addAttribute(ModelAttributetConstants.BANNER, getBannerforSequenceNumber(firstSequenceNumber, component));
				sessionService.setAttribute(SEQUENCE_NUMBER, firstSequenceNumber);
			}

		}

	}

	/**
	 * This method takes the sequence number and fetches the banner for that sequence number
	 *
	 * @param sequenceNumber
	 * @param component
	 * @return displayBanner
	 */
	private BannerComponentModel getBannerforSequenceNumber(final int sequenceNumber,
			final MplSequentialBannerComponentModel component)
	{
		BannerComponentModel displayBanner = null;
		if (component.getBannersList() != null)
		{
			for (final BannerComponentModel banner : component.getBannersList())
			{

				if (banner instanceof MplBigPromoBannerComponentModel)
				{
					final MplBigPromoBannerComponentModel promoBanner = (MplBigPromoBannerComponentModel) banner;
					if (promoBanner.getSequenceNumber() == Integer.valueOf(sequenceNumber))
					{
						displayBanner = banner;
					}
				}
				if (banner instanceof MplBigFourPromoBannerComponentModel)
				{
					final MplBigFourPromoBannerComponentModel promoBanner = (MplBigFourPromoBannerComponentModel) banner;

					if (promoBanner.getSequenceNumber() == Integer.valueOf(sequenceNumber))
					{
						displayBanner = banner;
					}

				}
			}

		}

		return displayBanner;
	}
}