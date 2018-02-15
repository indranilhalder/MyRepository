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
import com.tisl.mpl.model.cms.components.BannerProdCarouselElementCompModel;
import com.tisl.mpl.model.cms.components.BannerProductCarouselComponentModel;
import com.tisl.mpl.model.cms.components.ContentWidgetComponentModel;
import com.tisl.mpl.model.cms.components.ContentWidgetElementModel;
import com.tisl.mpl.model.cms.components.HeroBannerComponentModel;
import com.tisl.mpl.model.cms.components.HeroBannerElementModel;
import com.tisl.mpl.wsdto.BannerProCarouselElementWsDTO;
import com.tisl.mpl.wsdto.BannerProductCarouselWsDTO;
import com.tisl.mpl.wsdto.ContentWidgetCompWsDTO;
import com.tisl.mpl.wsdto.ContentWidgetElementWsDTO;
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

						if (abstractCMSComponentModel instanceof ContentWidgetComponentModel)
						{
							final ContentWidgetCompWsDTO contentWidgetCompWsDTO = new ContentWidgetCompWsDTO();

							final List<ContentWidgetElementWsDTO> contentWidgetElementList = new ArrayList<ContentWidgetElementWsDTO>();

							final ContentWidgetComponentModel contentWidgetComponentModel = (ContentWidgetComponentModel) abstractCMSComponentModel;
							for (final ContentWidgetElementModel contentWidgetElementModel : contentWidgetComponentModel.getItems())
							{
								final ContentWidgetElementWsDTO contentWidgetElementWsDTO = new ContentWidgetElementWsDTO();

								contentWidgetElementWsDTO.setImageURL(contentWidgetElementModel.getImageURL().getURL());
								contentWidgetElementWsDTO.setDescription(contentWidgetElementModel.getDescription());
								contentWidgetElementWsDTO.setAppURL(contentWidgetElementModel.getAppURL());
								contentWidgetElementWsDTO.setTitle(contentWidgetElementModel.getTitle());
								contentWidgetElementWsDTO.setWebURL(contentWidgetElementModel.getWebURL());
								contentWidgetElementWsDTO.setBtnText(contentWidgetElementModel.getBtnText());

								contentWidgetElementList.add(contentWidgetElementWsDTO);
							}
							contentWidgetCompWsDTO.setItems(contentWidgetElementList);
							contentWidgetCompWsDTO.setType(contentWidgetComponentModel.getType());

							uiCompPageWiseList.add(contentWidgetCompWsDTO);
						}

						if (abstractCMSComponentModel instanceof BannerProductCarouselComponentModel)
						{
							final BannerProductCarouselWsDTO bannerProductCarouselWsDTO = new BannerProductCarouselWsDTO();

							final List<BannerProCarouselElementWsDTO> bannerProCarouselList = new ArrayList<BannerProCarouselElementWsDTO>();

							final BannerProductCarouselComponentModel bannerProComponentModel = (BannerProductCarouselComponentModel) abstractCMSComponentModel;
							for (final BannerProdCarouselElementCompModel productObj : bannerProComponentModel.getItems())
							{
								final BannerProCarouselElementWsDTO bannerProCarouselElementWsDTO = new BannerProCarouselElementWsDTO();
								//final BannerProDiscountPriceWsDTO bannerProDiscountPriceWsDTO = new BannerProDiscountPriceWsDTO();
								//final BannerProMRPPriceWsDTO bannerProMRPPriceWsDTO = new BannerProMRPPriceWsDTO();

								//	bannerProDiscountPriceWsDTO.setCurrencyIso(productObj.getProductCode().get);

								bannerProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
								//bannerProCarouselElementWsDTO.setMrpPrice(mrpPrice);
								//bannerProCarouselElementWsDTO.setDiscountedPrice(discountedPrice);
								bannerProCarouselElementWsDTO.setAppURL(productObj.getAppURL());
								bannerProCarouselElementWsDTO.setTitle(productObj.getTitle());
								bannerProCarouselElementWsDTO.setWebURL(productObj.getWebURL());
								bannerProCarouselElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());




								bannerProCarouselList.add(bannerProCarouselElementWsDTO);
							}
							bannerProductCarouselWsDTO.setAppURL(bannerProComponentModel.getAppURL());
							bannerProductCarouselWsDTO.setBtnText(bannerProComponentModel.getBtnText());
							bannerProductCarouselWsDTO.setImageURL(bannerProComponentModel.getImageURL().getUrl());
							bannerProductCarouselWsDTO.setItems(bannerProCarouselList);
							bannerProductCarouselWsDTO.setType(bannerProComponentModel.getType());
							bannerProductCarouselWsDTO.setDescription(bannerProComponentModel.getDescription());
							bannerProductCarouselWsDTO.setTitle(bannerProComponentModel.getTitle());
							bannerProductCarouselWsDTO.setWebURL(bannerProComponentModel.getWebURL());

							uiCompPageWiseList.add(bannerProductCarouselWsDTO);
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