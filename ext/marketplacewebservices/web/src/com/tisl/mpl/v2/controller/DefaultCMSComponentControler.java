/**
 *
 */
package com.tisl.mpl.v2.controller;

/**
 * @author Nirav Bhanushali
 *
 */

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.model.cms.components.HeroBannerComponentModel;
import com.tisl.mpl.model.cms.components.HeroBannerElementModel;
import com.tisl.mpl.wsdto.HeroBannerCompListWsDTO;
import com.tisl.mpl.wsdto.HeroBannerCompWsDTO;
import com.tisl.mpl.wsdto.UICompPageWiseListWsDTO;


/**
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/cms")
public class DefaultCMSComponentControler
{
	@Autowired
	private MplCMSPageServiceImpl mplCMSPageService;

	@Autowired
	private CMSRestrictionService cmsRestrictionService;


	@RequestMapping(value = "/defaultpage", method = RequestMethod.GET)
	@ResponseBody
	public UICompPageWiseListWsDTO getComponentsForPage(@RequestParam final String pageId)
			throws EtailNonBusinessExceptions, CMSItemNotFoundException
	{
		final ContentPageModel contentPage = mplCMSPageService.getPageByLabelOrId(pageId);
		final UICompPageWiseListWsDTO uiCompPageWiseListWsDTO = new UICompPageWiseListWsDTO();
		final List<Object> uiCompPageWiseList = new ArrayList<Object>();

		if (contentPage != null)
		{
			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				final ContentSlotModel contentSlotModel = contentSlotForPage.getContentSlot();
				final List<AbstractCMSComponentModel> abstractCMSComponentModelList = cmsRestrictionService
						.evaluateCMSComponents(contentSlotModel.getCmsComponents(), null);

				if (null != abstractCMSComponentModelList)
				{

					for (final AbstractCMSComponentModel abstractCMSComponentModel : abstractCMSComponentModelList)
					{

						if (abstractCMSComponentModel instanceof HeroBannerComponentModel)
						{
							final HeroBannerCompWsDTO heroBannerCompWsDTO = new HeroBannerCompWsDTO();

							final List<HeroBannerCompListWsDTO> heroBannerCompListWsDTO = new ArrayList<HeroBannerCompListWsDTO>();

							final HeroBannerComponentModel heroBannerCompObj = (HeroBannerComponentModel) abstractCMSComponentModel;
							for (final HeroBannerElementModel heroBannerElementModel : heroBannerCompObj.getItems())
							{
								final HeroBannerCompListWsDTO heroBannerCompListObj = new HeroBannerCompListWsDTO();

								heroBannerCompListObj.setImageURL(heroBannerElementModel.getImageURL().getURL());
								heroBannerCompListObj.setBrandLogo(heroBannerElementModel.getBrandLogo().getURL());
								heroBannerCompListObj.setAppURL(heroBannerElementModel.getAppURL());
								heroBannerCompListObj.setTitle(heroBannerElementModel.getTitle());
								heroBannerCompListObj.setWebURL(heroBannerElementModel.getWebURL());
								heroBannerCompListWsDTO.add(heroBannerCompListObj);
							}
							heroBannerCompWsDTO.setItems(heroBannerCompListWsDTO);
							heroBannerCompWsDTO.setType(heroBannerCompObj.getType());

							uiCompPageWiseList.add(heroBannerCompWsDTO);
						}

					}
				}

			}
			uiCompPageWiseListWsDTO.setComponents(uiCompPageWiseList);
			return uiCompPageWiseListWsDTO;
		}
		return uiCompPageWiseListWsDTO;
	}


}