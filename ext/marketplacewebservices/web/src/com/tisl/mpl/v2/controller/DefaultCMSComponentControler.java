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
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.model.cms.components.BannerProdCarouselElementCompModel;
import com.tisl.mpl.model.cms.components.BannerProductCarouselComponentModel;
import com.tisl.mpl.model.cms.components.ContentWidgetComponentModel;
import com.tisl.mpl.model.cms.components.ContentWidgetElementModel;
import com.tisl.mpl.model.cms.components.HeroBannerComponentModel;
import com.tisl.mpl.model.cms.components.HeroBannerElementModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.wsdto.BannerProCarouselElementWsDTO;
import com.tisl.mpl.wsdto.BannerProDiscountPriceWsDTO;
import com.tisl.mpl.wsdto.BannerProMRPPriceWsDTO;
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

	@Autowired
	private ProductFacade productFacade;

	@Autowired
	private BuyBoxFacade buyBoxFacade;


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
							if (null != heroBannerCompObj.getItems() && heroBannerCompObj.getItems().size() > 0)
							{
								for (final HeroBannerElementModel heroBannerElementModel : heroBannerCompObj.getItems())
								{
									final HeroBannerCompListWsDTO heroBannerCompListObj = new HeroBannerCompListWsDTO();
									if (null != heroBannerElementModel.getImageURL()
											&& null != heroBannerElementModel.getImageURL().getURL())
									{
										heroBannerCompListObj.setImageURL(heroBannerElementModel.getImageURL().getURL());
									}
									else
									{
										heroBannerCompListObj.setImageURL("");
									}
									if (null != heroBannerElementModel.getBrandLogo()
											&& null != heroBannerElementModel.getBrandLogo().getURL())
									{
										heroBannerCompListObj.setBrandLogo(heroBannerElementModel.getBrandLogo().getURL());
									}
									else
									{
										heroBannerCompListObj.setBrandLogo("");
									}
									heroBannerCompListObj
											.setAppURL(null != heroBannerElementModel.getAppURL() ? heroBannerElementModel.getAppURL() : "");
									heroBannerCompListObj
											.setTitle(null != heroBannerElementModel.getTitle() ? heroBannerElementModel.getTitle() : "");
									heroBannerCompListObj
											.setWebURL(null != heroBannerElementModel.getWebURL() ? heroBannerElementModel.getWebURL() : "");
									heroBannerCompListWsDTO.add(heroBannerCompListObj);
								}
							}
							heroBannerCompWsDTO.setItems(heroBannerCompListWsDTO);
							heroBannerCompWsDTO.setType(null != heroBannerCompObj.getType() ? heroBannerCompObj.getType() : "");

							uiCompPageWiseList.add(heroBannerCompWsDTO);
						}

						if (abstractCMSComponentModel instanceof ContentWidgetComponentModel)
						{
							final ContentWidgetCompWsDTO contentWidgetCompWsDTO = new ContentWidgetCompWsDTO();

							final List<ContentWidgetElementWsDTO> contentWidgetElementList = new ArrayList<ContentWidgetElementWsDTO>();

							final ContentWidgetComponentModel contentWidgetComponentModel = (ContentWidgetComponentModel) abstractCMSComponentModel;
							if (null != contentWidgetComponentModel.getItems() && contentWidgetComponentModel.getItems().size() > 0)
							{
								for (final ContentWidgetElementModel contentWidgetElementModel : contentWidgetComponentModel.getItems())
								{
									final ContentWidgetElementWsDTO contentWidgetElementWsDTO = new ContentWidgetElementWsDTO();

									if (null != contentWidgetElementModel.getImageURL()
											&& null != contentWidgetElementModel.getImageURL().getURL())
									{
										contentWidgetElementWsDTO.setImageURL(contentWidgetElementModel.getImageURL().getURL());
									}
									else
									{
										contentWidgetElementWsDTO.setImageURL("");
									}
									contentWidgetElementWsDTO.setDescription(null != contentWidgetElementModel.getDescription()
											? contentWidgetElementModel.getDescription() : "");
									contentWidgetElementWsDTO.setAppURL(
											null != contentWidgetElementModel.getAppURL() ? contentWidgetElementModel.getAppURL() : "");
									contentWidgetElementWsDTO.setTitle(
											null != contentWidgetElementModel.getTitle() ? contentWidgetElementModel.getTitle() : "");
									contentWidgetElementWsDTO.setWebURL(
											null != contentWidgetElementModel.getWebURL() ? contentWidgetElementModel.getWebURL() : "");
									contentWidgetElementWsDTO.setBtnText(contentWidgetElementModel.getBtnText());

									contentWidgetElementList.add(contentWidgetElementWsDTO);
								}
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

							if (null != bannerProComponentModel.getItems() && bannerProComponentModel.getItems().size() > 0)
							{
								for (final BannerProdCarouselElementCompModel productObj : bannerProComponentModel.getItems())
								{
									final BannerProCarouselElementWsDTO bannerProCarouselElementWsDTO = new BannerProCarouselElementWsDTO();
									if (null != productObj && null != productObj.getProductCode()
											&& null != productObj.getProductCode().getCode())
									{
										final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productObj.getProductCode().getCode());
										//f]i;nal DecimalFormat df = new DecimalFormat("0.00");
										String productUnitPrice = "";
										String productPrice = "";

										if (buyboxdata != null)
										{
											final PriceData specialPrice = buyboxdata.getSpecialPrice();
											final PriceData mrp = buyboxdata.getMrp();
											final PriceData mop = buyboxdata.getPrice();

											if (mrp != null)
											{
												productUnitPrice = mrp.getValue().toPlainString();
											}
											if (specialPrice != null)
											{
												productPrice = specialPrice.getValue().toPlainString();
											}
											else if (null != mop && null != mop.getValue())
											{
												productPrice = mop.getValue().toPlainString();
											}
										}

										final BannerProDiscountPriceWsDTO bannerProDiscountPriceWsDTO = new BannerProDiscountPriceWsDTO();
										final BannerProMRPPriceWsDTO bannerProMRPPriceWsDTO = new BannerProMRPPriceWsDTO();

										bannerProDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										bannerProDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
										bannerProDiscountPriceWsDTO.setDoubleValue(productPrice);

										bannerProMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										bannerProMRPPriceWsDTO.setDoubleValue(productUnitPrice);
										bannerProMRPPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));

										bannerProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
										bannerProCarouselElementWsDTO.setMrpPrice(bannerProMRPPriceWsDTO);
										bannerProCarouselElementWsDTO.setDiscountedPrice(bannerProDiscountPriceWsDTO);
										bannerProCarouselElementWsDTO.setAppURL(productObj.getAppURL());
										bannerProCarouselElementWsDTO.setTitle(productObj.getTitle());
										bannerProCarouselElementWsDTO.setWebURL(productObj.getWebURL());
										bannerProCarouselElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
									}
									bannerProCarouselList.add(bannerProCarouselElementWsDTO);
								}
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