/**
 *
 */
package com.tisl.mpl.v2.controller;

/**
 * @author TUL
 *
 */

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.core.model.SmartFilterWidgetComponentModel;
import com.tisl.mpl.core.model.SmartFilterWidgetElementModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.marketplacecommerceservices.service.MplCMSComponentService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.model.cms.components.AutomatedBrandProductCarElementModel;
import com.tisl.mpl.model.cms.components.AutomatedBrandProductCarouselComponentModel;
import com.tisl.mpl.model.cms.components.BannerProdCarouselElementCompModel;
import com.tisl.mpl.model.cms.components.BannerProductCarouselComponentModel;
import com.tisl.mpl.model.cms.components.BannerSeparatorComponentModel;
import com.tisl.mpl.model.cms.components.ConnectBannerComponentModel;
import com.tisl.mpl.model.cms.components.ContentWidgetComponentModel;
import com.tisl.mpl.model.cms.components.ContentWidgetElementModel;
import com.tisl.mpl.model.cms.components.CuratedListingStripComponentModel;
import com.tisl.mpl.model.cms.components.CuratedProductsWidgetComponentModel;
import com.tisl.mpl.model.cms.components.CuratedProductsWidgetElementModel;
import com.tisl.mpl.model.cms.components.FlashSalesComponentModel;
import com.tisl.mpl.model.cms.components.FlashSalesElementModel;
import com.tisl.mpl.model.cms.components.FlashSalesItemElementModel;
import com.tisl.mpl.model.cms.components.HeroBannerComponentModel;
import com.tisl.mpl.model.cms.components.HeroBannerElementModel;
import com.tisl.mpl.model.cms.components.MonoBLPBannerComponentModel;
import com.tisl.mpl.model.cms.components.MonoBLPBannerElementModel;
import com.tisl.mpl.model.cms.components.OffersWidgetComponentModel;
import com.tisl.mpl.model.cms.components.OffersWidgetElementModel;
import com.tisl.mpl.model.cms.components.ProductCapsulesComponentModel;
import com.tisl.mpl.model.cms.components.ProductCapsulesElementModel;
import com.tisl.mpl.model.cms.components.SubBrandBannerBLPComponentModel;
import com.tisl.mpl.model.cms.components.SubBrandBannerBLPElementModel;
import com.tisl.mpl.model.cms.components.ThemeOffersCompOfferElementModel;
import com.tisl.mpl.model.cms.components.ThemeOffersComponentModel;
import com.tisl.mpl.model.cms.components.ThemeOffersItemsElementModel;
import com.tisl.mpl.model.cms.components.ThemeProductWidgetComponentModel;
import com.tisl.mpl.model.cms.components.ThemeProductWidgetElementModel;
import com.tisl.mpl.model.cms.components.TopCategoriesWidgetComponentModel;
import com.tisl.mpl.model.cms.components.TopCategoriesWidgetElementModel;
import com.tisl.mpl.model.cms.components.VideoProductCarouselComponentModel;
import com.tisl.mpl.model.cms.components.VideoProductCarouselElementModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.wsdto.*;


@Controller
@RequestMapping(value = "/{baseSiteId}/cms")
public class DefaultCMSComponentControler
{
	@Autowired
	private MplCMSPageServiceImpl mplCMSPageService;

	@Autowired
	private CMSRestrictionService cmsRestrictionService;

	@Autowired
	private BuyBoxFacade buyBoxFacade;

	@Autowired
	private MplCMSComponentService mplCmsComponentService;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/defaultpage", method = RequestMethod.GET)
	@ResponseBody
	public UICompPageWiseWsDTO getComponentsForPage(@RequestParam final String pageId)
			throws EtailNonBusinessExceptions, CMSItemNotFoundException
	{
		final ContentPageModel contentPage = mplCMSPageService.getPageByLabelOrId(pageId);

		final List<UICompPageElementWsDTO> genericUICompPageWsDTO = new ArrayList<UICompPageElementWsDTO>();

		final UICompPageWiseWsDTO uiCompPageObj = new UICompPageWiseWsDTO();

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
							final UICompPageElementWsDTO uiCompPageElementObj = new UICompPageElementWsDTO();
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
											.setTitle(null != heroBannerElementModel.getTitle() ? heroBannerElementModel.getTitle() : "");
									heroBannerCompListObj
											.setWebURL(null != heroBannerElementModel.getWebURL() ? heroBannerElementModel.getWebURL() : "");
									heroBannerCompListWsDTO.add(heroBannerCompListObj);
								}
							}
							heroBannerCompWsDTO.setItems(heroBannerCompListWsDTO);
							heroBannerCompWsDTO.setType(
									null != heroBannerCompObj.getName() ? heroBannerCompObj.getName() : "HeroBannerComponentModel");
							uiCompPageElementObj.setHeroBannerComponent(heroBannerCompWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementObj);
						}

						if (abstractCMSComponentModel instanceof ConnectBannerComponentModel)
						{
							final UICompPageElementWsDTO uiCompPageElementObj = new UICompPageElementWsDTO();
							final ConnectBannerWsDTO connectBannerWsDTO = new ConnectBannerWsDTO();
							final ConnectBannerComponentModel connectBannerComponentModel = (ConnectBannerComponentModel) abstractCMSComponentModel;
							if (null != connectBannerComponentModel.getBackgroundImageURL()
									&& null != connectBannerComponentModel.getBackgroundImageURL().getURL())
							{
								connectBannerWsDTO.setBackgroundImageURL(connectBannerComponentModel.getBackgroundImageURL().getURL());
							}
							else
							{
								connectBannerWsDTO.setBackgroundImageURL("");
							}
							if (null != connectBannerComponentModel.getIconImageURL()
									&& null != connectBannerComponentModel.getIconImageURL().getURL())
							{
								connectBannerWsDTO.setIconImageURL(connectBannerComponentModel.getIconImageURL().getURL());
							}
							else
							{
								connectBannerWsDTO.setIconImageURL("");
							}
							connectBannerWsDTO.setBtnText(
									null != connectBannerComponentModel.getBtnText() ? connectBannerComponentModel.getBtnText() : "");
							connectBannerWsDTO.setDescription(null != connectBannerComponentModel.getDescription()
									? connectBannerComponentModel.getDescription() : "");
							connectBannerWsDTO.setSubType(
									null != connectBannerComponentModel.getSubType() ? connectBannerComponentModel.getSubType() : "");
							connectBannerWsDTO.setTitle(
									null != connectBannerComponentModel.getTitle() ? connectBannerComponentModel.getTitle() : "");
							connectBannerWsDTO.setWebURL(
									null != connectBannerComponentModel.getWebURL() ? connectBannerComponentModel.getWebURL() : "");
							connectBannerWsDTO.setStartHexCode(null != connectBannerComponentModel.getStartHexCode()
									? connectBannerComponentModel.getStartHexCode() : "");
							connectBannerWsDTO.setEndHexCode(null != connectBannerComponentModel.getEndHexCode()
									? connectBannerComponentModel.getEndHexCode() : "");
							connectBannerWsDTO
									.setType(null != connectBannerComponentModel.getName() ? connectBannerComponentModel.getName() : "");

							uiCompPageElementObj.setMultiPurposeBanner(connectBannerWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementObj);
						}

						if (abstractCMSComponentModel instanceof OffersWidgetComponentModel)
						{
							final OffersWidgetWsDTO offersWidgetWsDTO = new OffersWidgetWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

							final List<OffersWidgetElementWsDTO> offersWidgetElementList = new ArrayList<OffersWidgetElementWsDTO>();

							final OffersWidgetComponentModel offersWidgetComponentModel = (OffersWidgetComponentModel) abstractCMSComponentModel;

							if (null != offersWidgetComponentModel.getItems() && offersWidgetComponentModel.getItems().size() > 0)
							{
								for (final OffersWidgetElementModel offersWidgetElementModel : offersWidgetComponentModel.getItems())
								{
									final OffersWidgetElementWsDTO offersWidgetElementWsDTO = new OffersWidgetElementWsDTO();

									if (null != offersWidgetElementModel.getImageURL()
											&& null != offersWidgetElementModel.getImageURL().getURL())
									{
										offersWidgetElementWsDTO.setImageURL(offersWidgetElementModel.getImageURL().getURL());
									}
									else
									{
										offersWidgetElementWsDTO.setImageURL("");
									}
									offersWidgetElementWsDTO.setTitle(
											null != offersWidgetElementModel.getTitle() ? offersWidgetComponentModel.getTitle() : "");
									offersWidgetElementWsDTO.setBtnText(
											null != offersWidgetElementModel.getBtnText() ? offersWidgetElementModel.getBtnText() : "");
									offersWidgetElementWsDTO.setDiscountText(null != offersWidgetElementModel.getDiscountText()
											? offersWidgetElementModel.getDiscountText() : "");
									offersWidgetElementWsDTO.setWebURL(
											null != offersWidgetElementModel.getWebURL() ? offersWidgetElementModel.getWebURL() : "");
									offersWidgetElementList.add(offersWidgetElementWsDTO);
								}
							}
							offersWidgetWsDTO.setItems(offersWidgetElementList);
							offersWidgetWsDTO
									.setTitle(null != offersWidgetComponentModel.getTitle() ? offersWidgetComponentModel.getTitle() : "");
							offersWidgetWsDTO
									.setType(null != offersWidgetComponentModel.getName() ? offersWidgetComponentModel.getName() : "");

							uiCompPageElementWsDTO.setOffersComponent(offersWidgetWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof FlashSalesComponentModel)
						{
							final FlashSalesWsDTO flashSalesWsDTO = new FlashSalesWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
							final List<FlashSalesOffersWsDTO> flashSalesOffersWsDTOList = new ArrayList<FlashSalesOffersWsDTO>();
							final List<FlashSalesElementWsDTO> flashSalesElementWsDTOList = new ArrayList<FlashSalesElementWsDTO>();

							final FlashSalesComponentModel flashSalesComponentModel = (FlashSalesComponentModel) abstractCMSComponentModel;
							if (null != flashSalesComponentModel.getOffers() && flashSalesComponentModel.getOffers().size() > 0)
							{
								for (final FlashSalesElementModel flashSalesOffersModel : flashSalesComponentModel.getOffers())
								{
									final FlashSalesOffersWsDTO flashSalesOffersWsDTO = new FlashSalesOffersWsDTO();
									flashSalesOffersWsDTO
											.setTitle(null != flashSalesOffersModel.getTitle() ? flashSalesOffersModel.getTitle() : "");
									flashSalesOffersWsDTO.setDescription(
											null != flashSalesOffersModel.getDescription() ? flashSalesOffersModel.getDescription() : "");
									if (null != flashSalesOffersModel.getImageURL()
											&& null != flashSalesOffersModel.getImageURL().getURL())
									{
										flashSalesOffersWsDTO.setImageURL(flashSalesOffersModel.getImageURL().getURL());
									}
									else
									{
										flashSalesOffersWsDTO.setImageURL("");
									}
									flashSalesOffersWsDTO
											.setWebURL(null != flashSalesOffersModel.getWebURL() ? flashSalesOffersModel.getWebURL() : "");

									flashSalesOffersWsDTOList.add(flashSalesOffersWsDTO);
								}
							}
							if (null != flashSalesComponentModel.getItems() && flashSalesComponentModel.getItems().size() > 0)
							{
								for (final FlashSalesItemElementModel flashSalesElementModel : flashSalesComponentModel.getItems())
								{
									final FlashSalesElementWsDTO flashSalesElementWsDTO = new FlashSalesElementWsDTO();

									if (null != flashSalesElementModel && null != flashSalesElementModel.getProductCode()
											&& null != flashSalesElementModel.getProductCode().getCode())
									{
										final BuyBoxData buyboxdata = buyBoxFacade
												.buyboxPrice(flashSalesElementModel.getProductCode().getCode());
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

										final FlashSalesDiscountPriceWsDTO flashSalesDiscountPriceWsDTO = new FlashSalesDiscountPriceWsDTO();
										final FlashSalesMRPPriceWsDTO flashSalesMRPPriceWsDTO = new FlashSalesMRPPriceWsDTO();

										flashSalesDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
										flashSalesDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
										if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
										{
											flashSalesDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
											flashSalesMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										}
										flashSalesDiscountPriceWsDTO.setCurrencySymbol("₹");
										flashSalesMRPPriceWsDTO.setCurrencySymbol("₹");
										flashSalesMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
										flashSalesMRPPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));

										flashSalesElementWsDTO.setPrdId(flashSalesElementModel.getProductCode().getCode());
										flashSalesElementWsDTO.setMrpPrice(flashSalesMRPPriceWsDTO);
										flashSalesElementWsDTO.setDiscountedPrice(flashSalesDiscountPriceWsDTO);
										flashSalesElementWsDTO.setTitle(flashSalesElementModel.getTitle());
										flashSalesElementWsDTO.setWebURL(flashSalesElementModel.getWebURL());
										if (flashSalesElementModel.getProductCode().getPicture() != null
												&& flashSalesElementModel.getProductCode().getPicture().getURL() != null)
										{
											flashSalesElementWsDTO
													.setImageURL(flashSalesElementModel.getProductCode().getPicture().getURL());
										}
										else
										{
											flashSalesElementWsDTO.setImageURL("");
										}
										flashSalesElementWsDTOList.add(flashSalesElementWsDTO);
									}
								}
							}
							flashSalesWsDTO.setBackgroundHexCode(null != flashSalesComponentModel.getBackgroundHexCode()
									? flashSalesComponentModel.getBackgroundHexCode() : "");
							if (null != flashSalesComponentModel.getBackgroundImageURL()
									&& null != flashSalesComponentModel.getBackgroundImageURL().getURL())
							{
								flashSalesWsDTO.setBackgroundImageURL(flashSalesComponentModel.getBackgroundImageURL().getURL());
							}
							else
							{
								flashSalesWsDTO.setBackgroundImageURL("");
							}
							flashSalesWsDTO.setBtnText(
									null != flashSalesComponentModel.getBtnText() ? flashSalesComponentModel.getBtnText() : "");
							flashSalesWsDTO.setDescription(
									null != flashSalesComponentModel.getDescription() ? flashSalesComponentModel.getDescription() : "");

							flashSalesWsDTO.setEndDate(null != flashSalesComponentModel.getEndDate()
									? formatter.format(flashSalesComponentModel.getEndDate()) : "");
							flashSalesWsDTO.setStartDate(null != flashSalesComponentModel.getStartDate()
									? formatter.format(flashSalesComponentModel.getStartDate()) : "");

							flashSalesWsDTO
									.setTitle(null != flashSalesComponentModel.getTitle() ? flashSalesComponentModel.getTitle() : "");
							flashSalesWsDTO
									.setWebURL(null != flashSalesComponentModel.getWebURL() ? flashSalesComponentModel.getWebURL() : "");
							flashSalesWsDTO.setOffers(flashSalesOffersWsDTOList);
							flashSalesWsDTO.setItems(flashSalesElementWsDTOList);
							flashSalesWsDTO
									.setType(null != flashSalesComponentModel.getName() ? flashSalesComponentModel.getName() : "");

							uiCompPageElementWsDTO.setFlashSalesComponent(flashSalesWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}


						if (abstractCMSComponentModel instanceof ContentWidgetComponentModel)
						{
							final ContentWidgetCompWsDTO contentWidgetCompWsDTO = new ContentWidgetCompWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
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
									contentWidgetElementWsDTO.setTitle(
											null != contentWidgetElementModel.getTitle() ? contentWidgetElementModel.getTitle() : "");
									contentWidgetElementWsDTO.setWebURL(
											null != contentWidgetElementModel.getWebURL() ? contentWidgetElementModel.getWebURL() : "");
									contentWidgetElementWsDTO.setBtnText(
											null != contentWidgetElementModel.getBtnText() ? contentWidgetElementModel.getBtnText() : "");

									contentWidgetElementList.add(contentWidgetElementWsDTO);
								}
							}
							contentWidgetCompWsDTO.setItems(contentWidgetElementList);
							contentWidgetCompWsDTO.setTitle(
									null != contentWidgetComponentModel.getTitle() ? contentWidgetComponentModel.getTitle() : "");
							contentWidgetCompWsDTO
									.setType(null != contentWidgetComponentModel.getName() ? contentWidgetComponentModel.getName() : "");

							uiCompPageElementWsDTO.setContentComponent(contentWidgetCompWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof BannerProductCarouselComponentModel)
						{
							final BannerProductCarouselWsDTO bannerProductCarouselWsDTO = new BannerProductCarouselWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

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

										bannerProDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
										bannerProDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
										if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
										{
											bannerProMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
											bannerProDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										}
										bannerProDiscountPriceWsDTO.setCurrencySymbol("₹");
										bannerProMRPPriceWsDTO.setCurrencySymbol("₹");
										bannerProMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
										bannerProMRPPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));

										bannerProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
										bannerProCarouselElementWsDTO.setMrpPrice(bannerProMRPPriceWsDTO);
										bannerProCarouselElementWsDTO.setDiscountedPrice(bannerProDiscountPriceWsDTO);
										bannerProCarouselElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
										bannerProCarouselElementWsDTO
												.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
										bannerProCarouselElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
									}
									bannerProCarouselList.add(bannerProCarouselElementWsDTO);
								}
							}
							bannerProductCarouselWsDTO
									.setBtnText(null != bannerProComponentModel.getBtnText() ? bannerProComponentModel.getBtnText() : "");
							bannerProductCarouselWsDTO.setImageURL(null != bannerProComponentModel.getImageURL().getURL()
									? bannerProComponentModel.getImageURL().getURL() : "");
							bannerProductCarouselWsDTO.setItems(bannerProCarouselList);
							bannerProductCarouselWsDTO
									.setType(null != bannerProComponentModel.getName() ? bannerProComponentModel.getName() : "");
							bannerProductCarouselWsDTO.setDescription(
									null != bannerProComponentModel.getDescription() ? bannerProComponentModel.getDescription() : "");
							bannerProductCarouselWsDTO
									.setTitle(null != bannerProComponentModel.getTitle() ? bannerProComponentModel.getTitle() : "");
							bannerProductCarouselWsDTO
									.setWebURL(null != bannerProComponentModel.getWebURL() ? bannerProComponentModel.getWebURL() : "");
							uiCompPageElementWsDTO.setBannerProductCarouselComponent(bannerProductCarouselWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof VideoProductCarouselComponentModel)
						{
							final VideoProductCarouselWsDTO videoProductCarouselWsDTO = new VideoProductCarouselWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
							final List<VideoProductCarElementWsDTO> videoProCarouselList = new ArrayList<VideoProductCarElementWsDTO>();

							final VideoProductCarouselComponentModel videoProComponentModel = (VideoProductCarouselComponentModel) abstractCMSComponentModel;

							if (null != videoProComponentModel.getItems() && videoProComponentModel.getItems().size() > 0)
							{
								for (final VideoProductCarouselElementModel productObj : videoProComponentModel.getItems())
								{
									final VideoProductCarElementWsDTO videoProCarouselElementWsDTO = new VideoProductCarElementWsDTO();
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

										final VideoProductCarDiscountPriceWsDTO videoProDiscountPriceWsDTO = new VideoProductCarDiscountPriceWsDTO();
										final VideoProductCarMRPPriceWsDTO videoProMRPPriceWsDTO = new VideoProductCarMRPPriceWsDTO();

										videoProDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
										videoProDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
										if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
										{
											videoProDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
											videoProMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										}
										videoProDiscountPriceWsDTO.setCurrencySymbol("₹");
										videoProMRPPriceWsDTO.setCurrencySymbol("₹");
										videoProMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
										videoProMRPPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));
										if (productObj.getProductCode() != null && productObj.getProductCode().getCode() != null)
										{
											videoProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
										}
										else
										{
											videoProCarouselElementWsDTO.setPrdId("");
										}
										videoProCarouselElementWsDTO.setMrpPrice(videoProMRPPriceWsDTO);
										videoProCarouselElementWsDTO.setDiscountedPrice(videoProDiscountPriceWsDTO);
										videoProCarouselElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
										videoProCarouselElementWsDTO
												.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
										if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
												&& productObj.getProductCode().getPicture().getURL() != null)
										{
											videoProCarouselElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
										}
										else
										{
											videoProCarouselElementWsDTO.setImageURL("");
										}
									}
									videoProCarouselList.add(videoProCarouselElementWsDTO);
								}
							}
							videoProductCarouselWsDTO
									.setBtnText(null != videoProComponentModel.getBtnText() ? videoProComponentModel.getBtnText() : "");
							if (null != videoProComponentModel.getImageURL() && null != videoProComponentModel.getImageURL().getURL())
							{
								videoProductCarouselWsDTO.setImageURL(videoProComponentModel.getImageURL().getURL());
							}
							else
							{
								videoProductCarouselWsDTO.setImageURL("");
							}
							videoProductCarouselWsDTO.setItems(videoProCarouselList);
							videoProductCarouselWsDTO
									.setType(null != videoProComponentModel.getName() ? videoProComponentModel.getName() : "");
							videoProductCarouselWsDTO.setDescription(
									null != videoProComponentModel.getDescription() ? videoProComponentModel.getDescription() : "");
							videoProductCarouselWsDTO
									.setTitle(null != videoProComponentModel.getTitle() ? videoProComponentModel.getTitle() : "");
							videoProductCarouselWsDTO
									.setWebURL(null != videoProComponentModel.getWebURL() ? videoProComponentModel.getWebURL() : "");
							videoProductCarouselWsDTO
									.setVideoURL(null != videoProComponentModel.getVideoURL() ? videoProComponentModel.getVideoURL() : "");
							if (videoProComponentModel.getBrandLogo() != null && videoProComponentModel.getBrandLogo().getURL() != null)
							{
								videoProductCarouselWsDTO.setBrandLogo(videoProComponentModel.getBrandLogo().getURL());
							}
							else
							{
								videoProductCarouselWsDTO.setBrandLogo("");
							}
							uiCompPageElementWsDTO.setVideoProductCarouselComponent(videoProductCarouselWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);


						}

						if (abstractCMSComponentModel instanceof ThemeOffersComponentModel)
						{
							final ThemeOffersWsDTO themeOffersWsDTO = new ThemeOffersWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
							final List<ThemeOffersElementWsDTO> themeOffersElementList = new ArrayList<ThemeOffersElementWsDTO>();
							final List<ThemeOffersCompOfferWsDTO> themeOffersCompOfferList = new ArrayList<ThemeOffersCompOfferWsDTO>();

							final ThemeOffersComponentModel themeOffersComponentModel = (ThemeOffersComponentModel) abstractCMSComponentModel;

							if (null != themeOffersComponentModel.getOffers() && themeOffersComponentModel.getOffers().size() > 0)
							{
								for (final ThemeOffersCompOfferElementModel themeOffersElementModel : themeOffersComponentModel
										.getOffers())
								{
									final ThemeOffersCompOfferWsDTO themeOffersCompOfferWsDTO = new ThemeOffersCompOfferWsDTO();
									themeOffersCompOfferWsDTO
											.setTitle(null != themeOffersElementModel.getTitle() ? themeOffersElementModel.getTitle() : "");
									themeOffersCompOfferWsDTO.setDescription(null != themeOffersElementModel.getDescription()
											? themeOffersElementModel.getDescription() : "");
									if (null != themeOffersElementModel.getImageURL()
											&& null != themeOffersElementModel.getImageURL().getURL())
									{
										themeOffersCompOfferWsDTO.setImageURL(themeOffersElementModel.getImageURL().getURL());
									}
									else
									{
										themeOffersCompOfferWsDTO.setImageURL("");
									}
									themeOffersCompOfferWsDTO.setWebURL(
											null != themeOffersElementModel.getWebURL() ? themeOffersElementModel.getWebURL() : "");

									themeOffersCompOfferList.add(themeOffersCompOfferWsDTO);
								}
							}
							if (null != themeOffersComponentModel.getItems() && themeOffersComponentModel.getItems().size() > 0)
							{
								for (final ThemeOffersItemsElementModel productObj : themeOffersComponentModel.getItems())
								{
									final ThemeOffersElementWsDTO themeOffersElementWsDTO = new ThemeOffersElementWsDTO();

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

										final ThemeOffersDiscountPriceWsDTO thDiscountPriceWsDTO = new ThemeOffersDiscountPriceWsDTO();
										final ThemeOffersMRPPriceWsDTO thMrpPriceWsDTO = new ThemeOffersMRPPriceWsDTO();

										thDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
										thDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
										if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
										{
											thDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
											thMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										}
										thDiscountPriceWsDTO.setCurrencySymbol("₹");
										thMrpPriceWsDTO.setCurrencySymbol("₹");
										thMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
										thMrpPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));
										themeOffersElementWsDTO.setPrdId(productObj.getProductCode().getCode());
										themeOffersElementWsDTO.setMrpPrice(thMrpPriceWsDTO);
										themeOffersElementWsDTO.setDiscountedPrice(thDiscountPriceWsDTO);
										themeOffersElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
										themeOffersElementWsDTO.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
										if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
												&& productObj.getProductCode().getPicture().getURL() != null)
										{
											themeOffersElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
										}
										else
										{
											themeOffersElementWsDTO.setImageURL("");
										}
									}
									themeOffersElementList.add(themeOffersElementWsDTO);

								}
							}
							themeOffersWsDTO.setBackgroundHexCode(null != themeOffersComponentModel.getBackgroundHexCode()
									? themeOffersComponentModel.getBackgroundHexCode() : "");
							if (themeOffersComponentModel.getBackgroundImageURL() != null
									&& themeOffersComponentModel.getBackgroundImageURL().getURL() != null)
							{
								themeOffersWsDTO.setBackgroundImageURL(themeOffersComponentModel.getBackgroundImageURL().getURL());
							}
							themeOffersWsDTO.setBackgroundImageURL("");
							themeOffersWsDTO.setBtnText(
									null != themeOffersComponentModel.getBtnText() ? themeOffersComponentModel.getBtnText() : "");
							themeOffersWsDTO.setItems(themeOffersElementList);
							themeOffersWsDTO.setOffers(themeOffersCompOfferList);
							themeOffersWsDTO
									.setTitle(null != themeOffersComponentModel.getTitle() ? themeOffersComponentModel.getTitle() : "");
							themeOffersWsDTO
									.setType(null != themeOffersComponentModel.getName() ? themeOffersComponentModel.getName() : "");
							themeOffersWsDTO
									.setWebURL(null != themeOffersComponentModel.getWebURL() ? themeOffersComponentModel.getWebURL() : "");

							uiCompPageElementWsDTO.setThemeOffersComponent(themeOffersWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof ThemeProductWidgetComponentModel)
						{
							final ThemeProductWidgetWsDTO themeProductWidgetWsDTO = new ThemeProductWidgetWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
							final ThemeProductWidgetComponentModel themeProductWidgetComponentModel = (ThemeProductWidgetComponentModel) abstractCMSComponentModel;
							final List<ThemeProWidElementWsDTO> themeProWidElementList = new ArrayList<ThemeProWidElementWsDTO>();

							if (null != themeProductWidgetComponentModel.getItems()
									&& themeProductWidgetComponentModel.getItems().size() > 0)
							{

								for (final ThemeProductWidgetElementModel productObj : themeProductWidgetComponentModel.getItems())
								{
									final ThemeProWidElementWsDTO themeProWidElementWsDTO = new ThemeProWidElementWsDTO();

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

										final ThemeProWidDiscountPriceWsDTO thDiscountPriceWsDTO = new ThemeProWidDiscountPriceWsDTO();
										final ThemeProWidMRPPriceWsDTO thMrpPriceWsDTO = new ThemeProWidMRPPriceWsDTO();

										thDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
										thDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
										if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
										{
											thDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
											thMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										}
										thDiscountPriceWsDTO.setCurrencySymbol("₹");
										thMrpPriceWsDTO.setCurrencySymbol("₹");
										thMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
										thMrpPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));
										themeProWidElementWsDTO.setPrdId(productObj.getProductCode().getCode());
										themeProWidElementWsDTO.setMrpPrice(thMrpPriceWsDTO);
										themeProWidElementWsDTO.setDiscountedPrice(thDiscountPriceWsDTO);
										themeProWidElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
										themeProWidElementWsDTO.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
										if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
												&& productObj.getProductCode().getPicture().getURL() != null)
										{
											themeProWidElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
										}
										else
										{
											themeProWidElementWsDTO.setImageURL("");
										}
									}
									themeProWidElementList.add(themeProWidElementWsDTO);
								}
							}
							if (themeProductWidgetComponentModel.getImageURL() != null
									&& themeProductWidgetComponentModel.getImageURL().getURL() != null)
							{
								themeProductWidgetWsDTO.setImageURL(themeProductWidgetComponentModel.getImageURL().getURL());
							}
							else
							{
								themeProductWidgetWsDTO.setImageURL("");
							}
							if (themeProductWidgetComponentModel.getBrandLogo() != null
									&& themeProductWidgetComponentModel.getBrandLogo().getURL() != null)
							{
								themeProductWidgetWsDTO.setBrandLogo(themeProductWidgetComponentModel.getBrandLogo().getURL());
							}
							else
							{
								themeProductWidgetWsDTO.setBrandLogo("");
							}
							themeProductWidgetWsDTO.setBtnText(null != themeProductWidgetComponentModel.getBtnText()
									? themeProductWidgetComponentModel.getBtnText() : "");
							themeProductWidgetWsDTO.setItems(themeProWidElementList);
							themeProductWidgetWsDTO.setTitle(null != themeProductWidgetComponentModel.getTitle()
									? themeProductWidgetComponentModel.getTitle() : "");
							themeProductWidgetWsDTO.setType(
									null != themeProductWidgetComponentModel.getName() ? themeProductWidgetComponentModel.getName() : "");
							uiCompPageElementWsDTO.setMultiClickComponent(themeProductWidgetWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof ProductCapsulesComponentModel)
						{
							final ProductCapsulesComponentModel productCapsulesComponentModel = (ProductCapsulesComponentModel) abstractCMSComponentModel;
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
							final List<ProductCapsulesElementWsDTO> productCapsulesElementList = new ArrayList<ProductCapsulesElementWsDTO>();
							final ProductCapsulesWsDTO productCapsulesWsDTO = new ProductCapsulesWsDTO();

							if (null != productCapsulesComponentModel.getItems() && productCapsulesComponentModel.getItems().size() > 0)
							{
								for (final ProductCapsulesElementModel productCapsulesElementModel : productCapsulesComponentModel
										.getItems())
								{
									final ProductCapsulesElementWsDTO productCapsulesElementWsDTO = new ProductCapsulesElementWsDTO();
									if (null != productCapsulesElementModel.getImageURL()
											&& null != productCapsulesElementModel.getImageURL().getURL())
									{
										productCapsulesElementWsDTO.setImageURL(productCapsulesElementModel.getImageURL().getURL());
									}
									else
									{
										productCapsulesElementWsDTO.setImageURL("");
									}
									productCapsulesElementWsDTO.setWebURL(
											null != productCapsulesElementModel.getWebURL() ? productCapsulesElementModel.getWebURL() : "");
									productCapsulesElementList.add(productCapsulesElementWsDTO);
								}
							}
							productCapsulesWsDTO.setBtnText(
									null != productCapsulesComponentModel.getBtnText() ? productCapsulesComponentModel.getBtnText() : "");
							productCapsulesWsDTO.setDescription(null != productCapsulesComponentModel.getDescription()
									? productCapsulesComponentModel.getDescription() : "");
							productCapsulesWsDTO.setItems(productCapsulesElementList);
							productCapsulesWsDTO.setTitle(
									null != productCapsulesComponentModel.getTitle() ? productCapsulesComponentModel.getTitle() : "");
							productCapsulesWsDTO.setType(
									null != productCapsulesComponentModel.getName() ? productCapsulesComponentModel.getName() : "");
							productCapsulesWsDTO.setWebURL(
									null != productCapsulesComponentModel.getWebURL() ? productCapsulesComponentModel.getWebURL() : "");
							uiCompPageElementWsDTO.setProductCapsules(productCapsulesWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof BannerSeparatorComponentModel)
						{
							final BannerSeparatorWsDTO bannerSeperatorWsDTO = new BannerSeparatorWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

							final BannerSeparatorComponentModel bannerSeparatorComponentModel = (BannerSeparatorComponentModel) abstractCMSComponentModel;

							if (null != bannerSeparatorComponentModel.getIconImageURL()
									&& null != bannerSeparatorComponentModel.getIconImageURL().getURL())
							{
								bannerSeperatorWsDTO.setIconImageURL(bannerSeparatorComponentModel.getIconImageURL().getURL());
							}
							else
							{
								bannerSeperatorWsDTO.setIconImageURL("");
							}
							bannerSeperatorWsDTO.setEndHexCode(null != bannerSeparatorComponentModel.getEndHexCode()
									? bannerSeparatorComponentModel.getEndHexCode() : "");
							bannerSeperatorWsDTO.setStartHexCode(null != bannerSeparatorComponentModel.getStartHexCode()
									? bannerSeparatorComponentModel.getStartHexCode() : "");
							bannerSeperatorWsDTO.setDescription(null != bannerSeparatorComponentModel.getDescription()
									? bannerSeparatorComponentModel.getDescription() : "");
							bannerSeperatorWsDTO.setTitle(
									null != bannerSeparatorComponentModel.getTitle() ? bannerSeparatorComponentModel.getTitle() : "");
							bannerSeperatorWsDTO.setWebURL(
									null != bannerSeparatorComponentModel.getWebURL() ? bannerSeparatorComponentModel.getWebURL() : "");
							bannerSeperatorWsDTO.setType(
									null != bannerSeparatorComponentModel.getName() ? bannerSeparatorComponentModel.getName() : "");
							uiCompPageElementWsDTO.setBannerSeparatorComponent(bannerSeperatorWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}
						if (abstractCMSComponentModel instanceof AutomatedBrandProductCarouselComponentModel)
						{
							final AutomatedBrandProductCarouselComponentModel automatedBrandProCarCompModel = (AutomatedBrandProductCarouselComponentModel) abstractCMSComponentModel;
							final List<AutomatedBrandProCarEleWsDTO> automatedBrandProCarEleList = new ArrayList<AutomatedBrandProCarEleWsDTO>();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
							final AutomatedBrandProCarWsDTO automatedBrandProCarWsDTO = new AutomatedBrandProCarWsDTO();
							if (automatedBrandProCarCompModel.getItems() != null && automatedBrandProCarCompModel.getItems().size() > 0)
							{
								for (final AutomatedBrandProductCarElementModel productObj : automatedBrandProCarCompModel.getItems())
								{

									final AutomatedBrandProCarEleWsDTO automatedBrandProCarEleWsDTO = new AutomatedBrandProCarEleWsDTO();

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

										final AutoBrandProCarEleDiscountPriceWsDTO autoDiscountPriceWsDTO = new AutoBrandProCarEleDiscountPriceWsDTO();
										final AutoBrandProCarEleMRPPriceWsDTO autoMrpPriceWsDTO = new AutoBrandProCarEleMRPPriceWsDTO();

										autoDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
										autoDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
										if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
										{
											autoDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
											autoMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										}
										autoDiscountPriceWsDTO.setCurrencySymbol("₹");
										autoMrpPriceWsDTO.setCurrencySymbol("₹");
										autoMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
										autoMrpPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));
										automatedBrandProCarEleWsDTO.setPrdId(productObj.getProductCode().getCode());
										automatedBrandProCarEleWsDTO.setMrpPrice(autoMrpPriceWsDTO);
										automatedBrandProCarEleWsDTO.setDiscountedPrice(autoDiscountPriceWsDTO);
										automatedBrandProCarEleWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
										automatedBrandProCarEleWsDTO
												.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
										if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
												&& productObj.getProductCode().getPicture().getURL() != null)
										{
											automatedBrandProCarEleWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
										}
										else
										{
											automatedBrandProCarEleWsDTO.setImageURL("");
										}
									}
									automatedBrandProCarEleList.add(automatedBrandProCarEleWsDTO);
								}
							}
							if (null != automatedBrandProCarCompModel.getBrandLogo()
									&& null != automatedBrandProCarCompModel.getBrandLogo().getURL())
							{
								automatedBrandProCarWsDTO.setBrandLogo(automatedBrandProCarCompModel.getBrandLogo().getURL());
							}
							else
							{
								automatedBrandProCarWsDTO.setBrandLogo("");
							}
							automatedBrandProCarWsDTO.setBtnText(
									null != automatedBrandProCarCompModel.getBtnText() ? automatedBrandProCarCompModel.getBtnText() : "");
							automatedBrandProCarWsDTO.setDescription(null != automatedBrandProCarCompModel.getDescription()
									? automatedBrandProCarCompModel.getDescription() : "");
							if (automatedBrandProCarCompModel.getImageURL() != null
									&& automatedBrandProCarCompModel.getImageURL().getURL() != null)
							{
								automatedBrandProCarWsDTO.setImageURL(automatedBrandProCarCompModel.getImageURL().getURL());
							}
							else
							{
								automatedBrandProCarWsDTO.setImageURL("");
							}
							automatedBrandProCarWsDTO.setItems(automatedBrandProCarEleList);
							automatedBrandProCarWsDTO.setType(
									null != automatedBrandProCarCompModel.getName() ? automatedBrandProCarCompModel.getName() : "");
							automatedBrandProCarWsDTO.setWebURL(
									null != automatedBrandProCarCompModel.getWebURL() ? automatedBrandProCarCompModel.getWebURL() : "");
							uiCompPageElementWsDTO.setAutomatedBannerProductCarouselComponent(automatedBrandProCarWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof CuratedListingStripComponentModel)
						{
							final CuratedListingStripComponentModel curatedListStripCompModel = (CuratedListingStripComponentModel) abstractCMSComponentModel;
							final CuratedListingStripWsDTO curatedListingStripWsDTO = new CuratedListingStripWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

							curatedListingStripWsDTO.setStartHexCode(null != curatedListStripCompModel.getStartHexCode()
									? curatedListStripCompModel.getStartHexCode() : "");
							curatedListingStripWsDTO
									.setTitle(null != curatedListStripCompModel.getTitle() ? curatedListStripCompModel.getTitle() : "");
							curatedListingStripWsDTO
									.setType(null != curatedListStripCompModel.getName() ? curatedListStripCompModel.getName() : "");
							curatedListingStripWsDTO
									.setWebURL(null != curatedListStripCompModel.getWebURL() ? curatedListStripCompModel.getWebURL() : "");

							uiCompPageElementWsDTO.setCuratedListingStripComponent(curatedListingStripWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof MonoBLPBannerComponentModel)
						{
							final MonoBLPBannerComponentModel monoBLPBannerComponentModel = (MonoBLPBannerComponentModel) abstractCMSComponentModel;
							final List<MonoBLPBannerElementWsDTO> monoBLPBannerElementList = new ArrayList<MonoBLPBannerElementWsDTO>();
							final MonoBLPBannerWsDTO moBannerWsDTO = new MonoBLPBannerWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();


							if (null != monoBLPBannerComponentModel.getItems() && monoBLPBannerComponentModel.getItems().size() > 0)
							{
								for (final MonoBLPBannerElementModel monoBLPBannerElementModel : monoBLPBannerComponentModel.getItems())
								{
									final MonoBLPBannerElementWsDTO monoBLPBannerElementWsDTO = new MonoBLPBannerElementWsDTO();
									monoBLPBannerElementWsDTO.setBtnText(
											null != monoBLPBannerElementModel.getBtnText() ? monoBLPBannerElementModel.getBtnText() : "");
									monoBLPBannerElementWsDTO.setHexCode(
											null != monoBLPBannerElementModel.getHexCode() ? monoBLPBannerElementModel.getHexCode() : "");
									monoBLPBannerElementWsDTO.setTitle(
											null != monoBLPBannerElementModel.getTitle() ? monoBLPBannerElementModel.getTitle() : "");
									monoBLPBannerElementWsDTO.setWebURL(
											null != monoBLPBannerElementModel.getWebURL() ? monoBLPBannerElementModel.getWebURL() : "");
									if (monoBLPBannerElementModel.getImageURL() != null
											&& monoBLPBannerElementModel.getImageURL().getURL() != null)
									{
										monoBLPBannerElementWsDTO.setImageURL(monoBLPBannerElementModel.getImageURL().getURL());
									}
									else
									{
										monoBLPBannerElementWsDTO.setImageURL("");
									}
									monoBLPBannerElementList.add(monoBLPBannerElementWsDTO);
								}
							}
							moBannerWsDTO
									.setType(null != monoBLPBannerComponentModel.getName() ? monoBLPBannerComponentModel.getName() : "");
							moBannerWsDTO.setItems(monoBLPBannerElementList);
							moBannerWsDTO.setTitle(
									null != monoBLPBannerComponentModel.getTitle() ? monoBLPBannerComponentModel.getTitle() : "");
							uiCompPageElementWsDTO.setSingleBannerComponent(moBannerWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);

						}
						if (abstractCMSComponentModel instanceof SubBrandBannerBLPComponentModel)
						{
							final SubBrandBannerBLPComponentModel subBrandBLPBannerCompModel = (SubBrandBannerBLPComponentModel) abstractCMSComponentModel;
							final List<SubBrandBannerBLPElementWsDTO> subBrandBannerBLPEleList = new ArrayList<SubBrandBannerBLPElementWsDTO>();
							final SubBrandBannerBLPWsDTO subBrandBannerBLPWsDTO = new SubBrandBannerBLPWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();


							if (null != subBrandBLPBannerCompModel.getItems() && subBrandBLPBannerCompModel.getItems().size() > 0)
							{
								for (final SubBrandBannerBLPElementModel subBannerBLPElementModel : subBrandBLPBannerCompModel.getItems())
								{
									final SubBrandBannerBLPElementWsDTO subBannerBLPEleWsDTO = new SubBrandBannerBLPElementWsDTO();
									subBannerBLPEleWsDTO.setWebURL(
											null != subBannerBLPElementModel.getWebURL() ? subBannerBLPElementModel.getWebURL() : "");
									if (subBannerBLPElementModel.getImageURL() != null
											&& subBannerBLPElementModel.getImageURL().getURL() != null)
									{
										subBannerBLPEleWsDTO.setImageURL(subBannerBLPElementModel.getImageURL().getURL());
									}
									else
									{
										subBannerBLPEleWsDTO.setImageURL("");
									}
									if (subBannerBLPElementModel.getBrandLogo() != null
											&& subBannerBLPElementModel.getBrandLogo().getURL() != null)
									{
										subBannerBLPEleWsDTO.setBrandLogo(subBannerBLPElementModel.getBrandLogo().getURL());
									}
									else
									{
										subBannerBLPEleWsDTO.setBrandLogo("");
									}
									subBrandBannerBLPEleList.add(subBannerBLPEleWsDTO);
								}
							}
							subBrandBannerBLPWsDTO
									.setType(null != subBrandBLPBannerCompModel.getName() ? subBrandBLPBannerCompModel.getName() : "");
							subBrandBannerBLPWsDTO.setItems(subBrandBannerBLPEleList);
							subBrandBannerBLPWsDTO
									.setTitle(null != subBrandBLPBannerCompModel.getTitle() ? subBrandBLPBannerCompModel.getTitle() : "");
							uiCompPageElementWsDTO.setSubBrandsBannerComponent(subBrandBannerBLPWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof TopCategoriesWidgetComponentModel)
						{
							final TopCategoriesWidgetComponentModel topCategoriesWidgetComponentModel = (TopCategoriesWidgetComponentModel) abstractCMSComponentModel;
							final List<TopCategoriesWidgetElementWsDTO> topCategoriesWidgetElementList = new ArrayList<TopCategoriesWidgetElementWsDTO>();
							final TopCategoriesWidgetWsDTO topCategoriesWidgetWsDTO = new TopCategoriesWidgetWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();


							if (null != topCategoriesWidgetComponentModel.getItems()
									&& topCategoriesWidgetComponentModel.getItems().size() > 0)
							{
								for (final TopCategoriesWidgetElementModel topCategoriesWidgetElementModel : topCategoriesWidgetComponentModel
										.getItems())
								{
									final TopCategoriesWidgetElementWsDTO topCategoriesWidgetElementWsDTO = new TopCategoriesWidgetElementWsDTO();
									topCategoriesWidgetElementWsDTO.setWebURL(null != topCategoriesWidgetElementModel.getWebURL()
											? topCategoriesWidgetElementModel.getWebURL() : "");
									if (topCategoriesWidgetElementModel.getImageURL() != null
											&& topCategoriesWidgetElementModel.getImageURL().getURL() != null)
									{
										topCategoriesWidgetElementWsDTO.setImageURL(topCategoriesWidgetElementModel.getImageURL().getURL());
									}
									else
									{
										topCategoriesWidgetElementWsDTO.setImageURL("");
									}
									topCategoriesWidgetElementWsDTO.setTitle(null != topCategoriesWidgetElementModel.getTitle()
											? topCategoriesWidgetElementModel.getTitle() : "");
									topCategoriesWidgetElementList.add(topCategoriesWidgetElementWsDTO);
								}
							}
							topCategoriesWidgetWsDTO.setType(null != topCategoriesWidgetComponentModel.getName()
									? topCategoriesWidgetComponentModel.getName() : "");
							topCategoriesWidgetWsDTO.setItems(topCategoriesWidgetElementList);
							topCategoriesWidgetWsDTO.setTitle(null != topCategoriesWidgetComponentModel.getTitle()
									? topCategoriesWidgetComponentModel.getTitle() : "");
							uiCompPageElementWsDTO.setTopCategoriesComponent(topCategoriesWidgetWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof CuratedProductsWidgetComponentModel)
						{
							final CuratedProductsWidgetComponentModel curatedProWidgetCompModel = (CuratedProductsWidgetComponentModel) abstractCMSComponentModel;
							final List<CuratedProWidgetElementWsDTO> curatedProWidgetElementList = new ArrayList<CuratedProWidgetElementWsDTO>();
							final CuratedProductsWidgetWsDTO curatedProductsWidgetWsDTO = new CuratedProductsWidgetWsDTO();
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

							if (null != curatedProWidgetCompModel.getItems() && curatedProWidgetCompModel.getItems().size() > 0)
							{
								for (final CuratedProductsWidgetElementModel productObj : curatedProWidgetCompModel.getItems())
								{
									final CuratedProWidgetElementWsDTO curatedProWidgetElementWsDTO = new CuratedProWidgetElementWsDTO();
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

										final CuratedProWidgetEleDiscountPriceWsDTO curatedProWidgetEleDiscountPriceWsDTO = new CuratedProWidgetEleDiscountPriceWsDTO();
										final CuratedProWidgetEleMRPPriceWsDTO curatedProWidgetEleMRPPriceWsDTO = new CuratedProWidgetEleMRPPriceWsDTO();

										curatedProWidgetEleDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
										curatedProWidgetEleDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
										if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
										{
											curatedProWidgetEleMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
											curatedProWidgetEleDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										}
										curatedProWidgetEleDiscountPriceWsDTO.setCurrencySymbol("₹");
										curatedProWidgetEleMRPPriceWsDTO.setCurrencySymbol("₹");
										curatedProWidgetEleMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
										curatedProWidgetEleMRPPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));

										curatedProWidgetElementWsDTO.setPrdId(productObj.getProductCode().getCode());
										curatedProWidgetElementWsDTO.setMrpPrice(curatedProWidgetEleMRPPriceWsDTO);
										curatedProWidgetElementWsDTO.setDiscountedPrice(curatedProWidgetEleDiscountPriceWsDTO);
										curatedProWidgetElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
										curatedProWidgetElementWsDTO
												.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
										curatedProWidgetElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
									}
									curatedProWidgetElementList.add(curatedProWidgetElementWsDTO);
								}
							}
							curatedProductsWidgetWsDTO.setBtnText(
									null != curatedProWidgetCompModel.getBtnText() ? curatedProWidgetCompModel.getBtnText() : "");
							curatedProductsWidgetWsDTO.setItems(curatedProWidgetElementList);
							curatedProductsWidgetWsDTO
									.setTitle(null != curatedProWidgetCompModel.getTitle() ? curatedProWidgetCompModel.getTitle() : "");
							curatedProductsWidgetWsDTO
									.setType(null != curatedProWidgetCompModel.getName() ? curatedProWidgetCompModel.getName() : "");
							curatedProductsWidgetWsDTO
									.setWebURL(null != curatedProWidgetCompModel.getWebURL() ? curatedProWidgetCompModel.getWebURL() : "");
							uiCompPageElementWsDTO.setCuratedProductsComponent(curatedProductsWidgetWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}

						if (abstractCMSComponentModel instanceof SmartFilterWidgetComponentModel)
						{
							final SmartFilterWidgetWsDTO smartFilterWsDTO = new SmartFilterWidgetWsDTO();
							final List<SmartFilterWidgetElementWsDTO> smartFilterWidgetElementList = new ArrayList<SmartFilterWidgetElementWsDTO>();
							final SmartFilterWidgetComponentModel smartFilterWidgetComponentModel = (SmartFilterWidgetComponentModel) abstractCMSComponentModel;
							final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

							if (null != smartFilterWidgetComponentModel.getItems()
									&& smartFilterWidgetComponentModel.getItems().size() > 0)
							{
								for (final SmartFilterWidgetElementModel smartFilterWidgetElementModel : smartFilterWidgetComponentModel
										.getItems())
								{
									final SmartFilterWidgetElementWsDTO smartFilterWidgetElementWsDTO = new SmartFilterWidgetElementWsDTO();
									smartFilterWidgetElementWsDTO.setDescription(null != smartFilterWidgetElementModel.getDescription()
											? smartFilterWidgetElementModel.getDescription() : "");
									if (smartFilterWidgetElementModel.getImageURL() != null
											&& smartFilterWidgetElementModel.getImageURL().getURL() != null)
									{
										smartFilterWidgetElementWsDTO.setImageURL(smartFilterWidgetElementModel.getImageURL().getURL());
									}
									else
									{
										smartFilterWidgetElementWsDTO.setImageURL("");
									}
									smartFilterWidgetElementWsDTO.setTitle(null != smartFilterWidgetElementModel.getTitle()
											? smartFilterWidgetElementModel.getTitle() : "");
									smartFilterWidgetElementWsDTO.setWebURL(null != smartFilterWidgetElementModel.getWebURL()
											? smartFilterWidgetElementModel.getWebURL() : "");
									smartFilterWidgetElementList.add(smartFilterWidgetElementWsDTO);
								}
							}
							smartFilterWsDTO.setItems(smartFilterWidgetElementList);
							smartFilterWsDTO.setTitle(
									null != smartFilterWidgetComponentModel.getTitle() ? smartFilterWidgetComponentModel.getTitle() : "");
							smartFilterWsDTO.setType(
									null != smartFilterWidgetComponentModel.getName() ? smartFilterWidgetComponentModel.getName() : "");
							uiCompPageElementWsDTO.setTwoByTwoBannerComponent(smartFilterWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						}
					}
				}
			}
			uiCompPageObj.setItems(genericUICompPageWsDTO);
			uiCompPageObj.setMessage("HOMEPAGE");
			uiCompPageObj.setStatus("SUCCESS");
			return uiCompPageObj;
		}
		return uiCompPageObj;
	}

	@RequestMapping(value = "/component", method = RequestMethod.GET)
	@ResponseBody
	public UICompPageWiseWsDTO getComponentsForId(@RequestParam final String pageId, @RequestParam final String componentId)
			throws CMSItemNotFoundException
	{
		final ContentPageModel contentPage = mplCMSPageService.getPageByLabelOrId(pageId);
		final UICompPageWiseWsDTO uiComponentWiseWsDTO = new UICompPageWiseWsDTO();
		final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
		final List<UICompPageElementWsDTO> genericUICompPageWsDTO = new ArrayList<UICompPageElementWsDTO>();

		if (contentPage != null && componentId != null)
		{
			final List<AbstractCMSComponentModel> abstractCMSComponentModelList = mplCmsComponentService.getPagewiseComponent(pageId,
					componentId);
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
								if (null != heroBannerElementModel.getImageURL() && null != heroBannerElementModel.getImageURL().getURL())
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
										.setTitle(null != heroBannerElementModel.getTitle() ? heroBannerElementModel.getTitle() : "");
								heroBannerCompListObj
										.setWebURL(null != heroBannerElementModel.getWebURL() ? heroBannerElementModel.getWebURL() : "");
								heroBannerCompListWsDTO.add(heroBannerCompListObj);
							}
						}
						heroBannerCompWsDTO.setItems(heroBannerCompListWsDTO);
						heroBannerCompWsDTO
								.setType(null != heroBannerCompObj.getName() ? heroBannerCompObj.getName() : "HeroBannerComponentModel");

						uiCompPageElementWsDTO.setHeroBannerComponent(heroBannerCompWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof ConnectBannerComponentModel)
					{
						final ConnectBannerWsDTO connectBannerWsDTO = new ConnectBannerWsDTO();
						final ConnectBannerComponentModel connectBannerComponentModel = (ConnectBannerComponentModel) abstractCMSComponentModel;
						if (null != connectBannerComponentModel.getBackgroundImageURL()
								&& null != connectBannerComponentModel.getBackgroundImageURL().getURL())
						{
							connectBannerWsDTO.setBackgroundImageURL(connectBannerComponentModel.getBackgroundImageURL().getURL());
						}
						else
						{
							connectBannerWsDTO.setBackgroundImageURL("");
						}
						if (null != connectBannerComponentModel.getIconImageURL()
								&& null != connectBannerComponentModel.getIconImageURL().getURL())
						{
							connectBannerWsDTO.setIconImageURL(connectBannerComponentModel.getIconImageURL().getURL());
						}
						else
						{
							connectBannerWsDTO.setIconImageURL("");
						}

						connectBannerWsDTO.setBtnText(
								null != connectBannerComponentModel.getBtnText() ? connectBannerComponentModel.getBtnText() : "");
						connectBannerWsDTO.setDescription(
								null != connectBannerComponentModel.getDescription() ? connectBannerComponentModel.getDescription() : "");
						connectBannerWsDTO.setSubType(
								null != connectBannerComponentModel.getSubType() ? connectBannerComponentModel.getSubType() : "");
						connectBannerWsDTO
								.setTitle(null != connectBannerComponentModel.getTitle() ? connectBannerComponentModel.getTitle() : "");
						connectBannerWsDTO.setWebURL(
								null != connectBannerComponentModel.getWebURL() ? connectBannerComponentModel.getWebURL() : "");
						connectBannerWsDTO.setStartHexCode(null != connectBannerComponentModel.getStartHexCode()
								? connectBannerComponentModel.getStartHexCode() : "");
						connectBannerWsDTO.setEndHexCode(
								null != connectBannerComponentModel.getEndHexCode() ? connectBannerComponentModel.getEndHexCode() : "");
						connectBannerWsDTO
								.setType(null != connectBannerComponentModel.getName() ? connectBannerComponentModel.getName() : "");

						uiCompPageElementWsDTO.setMultiPurposeBanner(connectBannerWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof OffersWidgetComponentModel)
					{
						final OffersWidgetWsDTO offersWidgetWsDTO = new OffersWidgetWsDTO();
						final List<OffersWidgetElementWsDTO> offersWidgetElementList = new ArrayList<OffersWidgetElementWsDTO>();

						final OffersWidgetComponentModel offersWidgetComponentModel = (OffersWidgetComponentModel) abstractCMSComponentModel;

						if (null != offersWidgetComponentModel.getItems() && offersWidgetComponentModel.getItems().size() > 0)
						{
							for (final OffersWidgetElementModel offersWidgetElementModel : offersWidgetComponentModel.getItems())
							{
								final OffersWidgetElementWsDTO offersWidgetElementWsDTO = new OffersWidgetElementWsDTO();

								if (null != offersWidgetElementModel.getImageURL()
										&& null != offersWidgetElementModel.getImageURL().getURL())
								{
									offersWidgetElementWsDTO.setImageURL(offersWidgetElementModel.getImageURL().getURL());
								}
								else
								{
									offersWidgetElementWsDTO.setImageURL("");
								}
								offersWidgetElementWsDTO
										.setTitle(null != offersWidgetElementModel.getTitle() ? offersWidgetComponentModel.getTitle() : "");
								offersWidgetElementWsDTO.setBtnText(
										null != offersWidgetElementModel.getBtnText() ? offersWidgetElementModel.getBtnText() : "");
								offersWidgetElementWsDTO.setDiscountText(null != offersWidgetElementModel.getDiscountText()
										? offersWidgetElementModel.getDiscountText() : "");
								offersWidgetElementWsDTO.setWebURL(
										null != offersWidgetElementModel.getWebURL() ? offersWidgetElementModel.getWebURL() : "");
								offersWidgetElementList.add(offersWidgetElementWsDTO);
							}
						}
						offersWidgetWsDTO.setItems(offersWidgetElementList);
						offersWidgetWsDTO
								.setTitle(null != offersWidgetComponentModel.getTitle() ? offersWidgetComponentModel.getTitle() : "");
						offersWidgetWsDTO
								.setType(null != offersWidgetComponentModel.getName() ? offersWidgetComponentModel.getName() : "");

						uiCompPageElementWsDTO.setOffersComponent(offersWidgetWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof FlashSalesComponentModel)
					{
						final FlashSalesWsDTO flashSalesWsDTO = new FlashSalesWsDTO();
						final List<FlashSalesOffersWsDTO> flashSalesOffersWsDTOList = new ArrayList<FlashSalesOffersWsDTO>();
						final List<FlashSalesElementWsDTO> flashSalesElementWsDTOList = new ArrayList<FlashSalesElementWsDTO>();

						final FlashSalesComponentModel flashSalesComponentModel = (FlashSalesComponentModel) abstractCMSComponentModel;
						if (null != flashSalesComponentModel.getOffers() && flashSalesComponentModel.getOffers().size() > 0)
						{
							for (final FlashSalesElementModel flashSalesOffersModel : flashSalesComponentModel.getOffers())
							{
								final FlashSalesOffersWsDTO flashSalesOffersWsDTO = new FlashSalesOffersWsDTO();

								flashSalesOffersWsDTO
										.setTitle(null != flashSalesOffersModel.getTitle() ? flashSalesOffersModel.getTitle() : "");
								flashSalesOffersWsDTO.setDescription(
										null != flashSalesOffersModel.getDescription() ? flashSalesOffersModel.getDescription() : "");
								if (null != flashSalesOffersModel.getImageURL() && null != flashSalesOffersModel.getImageURL().getURL())
								{
									flashSalesOffersWsDTO.setImageURL(flashSalesOffersModel.getImageURL().getURL());
								}
								else
								{
									flashSalesOffersWsDTO.setImageURL("");
								}
								flashSalesOffersWsDTO
										.setWebURL(null != flashSalesOffersModel.getWebURL() ? flashSalesOffersModel.getWebURL() : "");

								flashSalesOffersWsDTOList.add(flashSalesOffersWsDTO);
							}
						}
						if (null != flashSalesComponentModel.getItems() && flashSalesComponentModel.getItems().size() > 0)
						{
							for (final FlashSalesItemElementModel flashSalesElementModel : flashSalesComponentModel.getItems())
							{
								final FlashSalesElementWsDTO flashSalesElementWsDTO = new FlashSalesElementWsDTO();

								if (null != flashSalesElementModel && null != flashSalesElementModel.getProductCode()
										&& null != flashSalesElementModel.getProductCode().getCode())
								{
									final BuyBoxData buyboxdata = buyBoxFacade
											.buyboxPrice(flashSalesElementModel.getProductCode().getCode());
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

									final FlashSalesDiscountPriceWsDTO flashSalesDiscountPriceWsDTO = new FlashSalesDiscountPriceWsDTO();
									final FlashSalesMRPPriceWsDTO flashSalesMRPPriceWsDTO = new FlashSalesMRPPriceWsDTO();

									flashSalesDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
									if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
									{
										flashSalesDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										flashSalesMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
									}
									flashSalesDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
									flashSalesDiscountPriceWsDTO.setCurrencySymbol("₹");
									flashSalesMRPPriceWsDTO.setCurrencySymbol("₹");
									flashSalesMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
									flashSalesMRPPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));

									flashSalesElementWsDTO.setPrdId(flashSalesElementModel.getProductCode().getCode());
									flashSalesElementWsDTO.setMrpPrice(flashSalesMRPPriceWsDTO);
									flashSalesElementWsDTO.setDiscountedPrice(flashSalesDiscountPriceWsDTO);
									flashSalesElementWsDTO
											.setTitle(null != flashSalesElementModel.getTitle() ? flashSalesElementModel.getTitle() : "");
									flashSalesElementWsDTO
											.setWebURL(null != flashSalesElementModel.getWebURL() ? flashSalesElementModel.getWebURL() : "");
									if (flashSalesElementModel.getProductCode().getPicture() != null
											&& flashSalesElementModel.getProductCode().getPicture().getURL() != null)
									{
										flashSalesElementWsDTO.setImageURL(flashSalesElementModel.getProductCode().getPicture().getURL());
									}
									else
									{
										flashSalesElementWsDTO.setImageURL("");
									}
									flashSalesElementWsDTOList.add(flashSalesElementWsDTO);
								}
							}
						}
						flashSalesWsDTO.setBackgroundHexCode(null != flashSalesComponentModel.getBackgroundHexCode()
								? flashSalesComponentModel.getBackgroundHexCode() : "");
						if (null != flashSalesComponentModel.getBackgroundImageURL()
								&& null != flashSalesComponentModel.getBackgroundImageURL().getURL())
						{
							flashSalesWsDTO.setBackgroundImageURL(flashSalesComponentModel.getBackgroundImageURL().getURL());
						}
						else
						{
							flashSalesWsDTO.setBackgroundImageURL("");
						}
						flashSalesWsDTO
								.setBtnText(null != flashSalesComponentModel.getBtnText() ? flashSalesComponentModel.getBtnText() : "");
						flashSalesWsDTO.setDescription(
								null != flashSalesComponentModel.getDescription() ? flashSalesComponentModel.getDescription() : "");

						flashSalesWsDTO.setEndDate(null != flashSalesComponentModel.getEndDate()
								? formatter.format(flashSalesComponentModel.getEndDate()) : "");
						flashSalesWsDTO.setStartDate(null != flashSalesComponentModel.getStartDate()
								? formatter.format(flashSalesComponentModel.getStartDate()) : "");

						flashSalesWsDTO
								.setTitle(null != flashSalesComponentModel.getTitle() ? flashSalesComponentModel.getTitle() : "");
						flashSalesWsDTO
								.setWebURL(null != flashSalesComponentModel.getWebURL() ? flashSalesComponentModel.getWebURL() : "");
						flashSalesWsDTO.setOffers(flashSalesOffersWsDTOList);
						flashSalesWsDTO.setItems(flashSalesElementWsDTOList);
						flashSalesWsDTO.setType(null != flashSalesComponentModel.getName() ? flashSalesComponentModel.getName() : "");

						uiCompPageElementWsDTO.setFlashSalesComponent(flashSalesWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
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
								contentWidgetElementWsDTO
										.setTitle(null != contentWidgetElementModel.getTitle() ? contentWidgetElementModel.getTitle() : "");
								contentWidgetElementWsDTO.setWebURL(
										null != contentWidgetElementModel.getWebURL() ? contentWidgetElementModel.getWebURL() : "");
								contentWidgetElementWsDTO.setBtnText(
										null != contentWidgetElementModel.getBtnText() ? contentWidgetElementModel.getBtnText() : "");

								contentWidgetElementList.add(contentWidgetElementWsDTO);
							}
						}
						contentWidgetCompWsDTO.setItems(contentWidgetElementList);
						contentWidgetCompWsDTO
								.setTitle(null != contentWidgetComponentModel.getTitle() ? contentWidgetComponentModel.getTitle() : "");
						contentWidgetCompWsDTO
								.setType(null != contentWidgetComponentModel.getName() ? contentWidgetComponentModel.getName() : "");
						uiCompPageElementWsDTO.setContentComponent(contentWidgetCompWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
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

									bannerProDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
									bannerProDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
									if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
									{
										bannerProMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										bannerProDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
									}
									bannerProDiscountPriceWsDTO.setCurrencySymbol("₹");
									bannerProMRPPriceWsDTO.setCurrencySymbol("₹");
									bannerProMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
									bannerProMRPPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));

									bannerProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
									bannerProCarouselElementWsDTO.setMrpPrice(bannerProMRPPriceWsDTO);
									bannerProCarouselElementWsDTO.setDiscountedPrice(bannerProDiscountPriceWsDTO);
									bannerProCarouselElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
									bannerProCarouselElementWsDTO.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
									bannerProCarouselElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
								}
								bannerProCarouselList.add(bannerProCarouselElementWsDTO);
							}
						}
						bannerProductCarouselWsDTO
								.setBtnText(null != bannerProComponentModel.getBtnText() ? bannerProComponentModel.getBtnText() : "");
						bannerProductCarouselWsDTO.setImageURL(null != bannerProComponentModel.getImageURL().getURL()
								? bannerProComponentModel.getImageURL().getURL() : "");
						bannerProductCarouselWsDTO.setItems(bannerProCarouselList);
						bannerProductCarouselWsDTO
								.setType(null != bannerProComponentModel.getName() ? bannerProComponentModel.getName() : "");
						bannerProductCarouselWsDTO.setDescription(
								null != bannerProComponentModel.getDescription() ? bannerProComponentModel.getDescription() : "");
						bannerProductCarouselWsDTO
								.setTitle(null != bannerProComponentModel.getTitle() ? bannerProComponentModel.getTitle() : "");
						bannerProductCarouselWsDTO
								.setWebURL(null != bannerProComponentModel.getWebURL() ? bannerProComponentModel.getWebURL() : "");
						uiCompPageElementWsDTO.setBannerProductCarouselComponent(bannerProductCarouselWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof VideoProductCarouselComponentModel)
					{
						final VideoProductCarouselWsDTO videoProductCarouselWsDTO = new VideoProductCarouselWsDTO();
						final List<VideoProductCarElementWsDTO> videoProCarouselList = new ArrayList<VideoProductCarElementWsDTO>();

						final VideoProductCarouselComponentModel videoProComponentModel = (VideoProductCarouselComponentModel) abstractCMSComponentModel;

						if (null != videoProComponentModel.getItems() && videoProComponentModel.getItems().size() > 0)
						{
							for (final VideoProductCarouselElementModel productObj : videoProComponentModel.getItems())
							{
								final VideoProductCarElementWsDTO videoProCarouselElementWsDTO = new VideoProductCarElementWsDTO();
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

									final VideoProductCarDiscountPriceWsDTO videoProDiscountPriceWsDTO = new VideoProductCarDiscountPriceWsDTO();
									final VideoProductCarMRPPriceWsDTO videoProMRPPriceWsDTO = new VideoProductCarMRPPriceWsDTO();

									videoProDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
									videoProDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
									if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
									{
										videoProDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										videoProMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
									}
									videoProDiscountPriceWsDTO.setCurrencySymbol("₹");
									videoProMRPPriceWsDTO.setCurrencySymbol("₹");
									videoProMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
									videoProMRPPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));
									if (productObj.getProductCode() != null && productObj.getProductCode().getCode() != null)
									{
										videoProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
									}
									else
									{
										videoProCarouselElementWsDTO.setPrdId("");
									}
									videoProCarouselElementWsDTO.setMrpPrice(videoProMRPPriceWsDTO);
									videoProCarouselElementWsDTO.setDiscountedPrice(videoProDiscountPriceWsDTO);
									videoProCarouselElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
									videoProCarouselElementWsDTO.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
									if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
											&& productObj.getProductCode().getPicture().getURL() != null)
									{
										videoProCarouselElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
									}
									else
									{
										videoProCarouselElementWsDTO.setImageURL("");
									}
								}
								videoProCarouselList.add(videoProCarouselElementWsDTO);
							}
						}
						videoProductCarouselWsDTO
								.setBtnText(null != videoProComponentModel.getBtnText() ? videoProComponentModel.getBtnText() : "");
						if (null != videoProComponentModel.getImageURL() && null != videoProComponentModel.getImageURL().getURL())
						{
							videoProductCarouselWsDTO.setImageURL(videoProComponentModel.getImageURL().getURL());
						}
						else
						{
							videoProductCarouselWsDTO.setImageURL("");
						}

						videoProductCarouselWsDTO.setItems(videoProCarouselList);
						videoProductCarouselWsDTO
								.setType(null != videoProComponentModel.getName() ? videoProComponentModel.getName() : "");
						videoProductCarouselWsDTO.setDescription(
								null != videoProComponentModel.getDescription() ? videoProComponentModel.getDescription() : "");
						videoProductCarouselWsDTO
								.setTitle(null != videoProComponentModel.getTitle() ? videoProComponentModel.getTitle() : "");
						videoProductCarouselWsDTO
								.setWebURL(null != videoProComponentModel.getWebURL() ? videoProComponentModel.getWebURL() : "");
						videoProductCarouselWsDTO
								.setVideoURL(null != videoProComponentModel.getVideoURL() ? videoProComponentModel.getVideoURL() : "");
						if (videoProComponentModel.getBrandLogo() != null && videoProComponentModel.getBrandLogo().getURL() != null)
						{
							videoProductCarouselWsDTO.setBrandLogo(videoProComponentModel.getBrandLogo().getURL());
						}
						else
						{
							videoProductCarouselWsDTO.setBrandLogo("");
						}

						uiCompPageElementWsDTO.setVideoProductCarouselComponent(videoProductCarouselWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof ThemeOffersComponentModel)
					{
						final ThemeOffersWsDTO themeOffersWsDTO = new ThemeOffersWsDTO();
						final List<ThemeOffersElementWsDTO> themeOffersElementList = new ArrayList<ThemeOffersElementWsDTO>();
						final List<ThemeOffersCompOfferWsDTO> themeOffersCompOfferList = new ArrayList<ThemeOffersCompOfferWsDTO>();

						final ThemeOffersComponentModel themeOffersComponentModel = (ThemeOffersComponentModel) abstractCMSComponentModel;

						if (null != themeOffersComponentModel.getOffers() && themeOffersComponentModel.getOffers().size() > 0)
						{
							for (final ThemeOffersCompOfferElementModel themeOffersElementModel : themeOffersComponentModel.getOffers())
							{
								final ThemeOffersCompOfferWsDTO themeOffersCompOfferWsDTO = new ThemeOffersCompOfferWsDTO();
								themeOffersCompOfferWsDTO
										.setTitle(null != themeOffersElementModel.getTitle() ? themeOffersElementModel.getTitle() : "");
								themeOffersCompOfferWsDTO.setDescription(
										null != themeOffersElementModel.getDescription() ? themeOffersElementModel.getDescription() : "");
								if (null != themeOffersElementModel.getImageURL()
										&& null != themeOffersElementModel.getImageURL().getURL())
								{
									themeOffersCompOfferWsDTO.setImageURL(themeOffersElementModel.getImageURL().getURL());
								}
								else
								{
									themeOffersCompOfferWsDTO.setImageURL("");
								}
								themeOffersCompOfferWsDTO
										.setWebURL(null != themeOffersElementModel.getWebURL() ? themeOffersElementModel.getWebURL() : "");

								themeOffersCompOfferList.add(themeOffersCompOfferWsDTO);
							}
						}
						if (null != themeOffersComponentModel.getItems() && themeOffersComponentModel.getItems().size() > 0)
						{
							for (final ThemeOffersItemsElementModel productObj : themeOffersComponentModel.getItems())
							{
								final ThemeOffersElementWsDTO themeOffersElementWsDTO = new ThemeOffersElementWsDTO();

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

									final ThemeOffersDiscountPriceWsDTO thDiscountPriceWsDTO = new ThemeOffersDiscountPriceWsDTO();
									final ThemeOffersMRPPriceWsDTO thMrpPriceWsDTO = new ThemeOffersMRPPriceWsDTO();

									thDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
									thDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
									if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
									{
										thDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										thMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
									}
									thDiscountPriceWsDTO.setCurrencySymbol("₹");
									thMrpPriceWsDTO.setCurrencySymbol("₹");
									thMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
									thMrpPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));
									themeOffersElementWsDTO.setPrdId(productObj.getProductCode().getCode());
									themeOffersElementWsDTO.setMrpPrice(thMrpPriceWsDTO);
									themeOffersElementWsDTO.setDiscountedPrice(thDiscountPriceWsDTO);
									themeOffersElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
									themeOffersElementWsDTO.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
									if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
											&& productObj.getProductCode().getPicture().getURL() != null)
									{
										themeOffersElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
									}
									else
									{
										themeOffersElementWsDTO.setImageURL("");
									}
								}
								themeOffersElementList.add(themeOffersElementWsDTO);

							}
						}
						themeOffersWsDTO.setBackgroundHexCode(null != themeOffersComponentModel.getBackgroundHexCode()
								? themeOffersComponentModel.getBackgroundHexCode() : "");
						if (themeOffersComponentModel.getBackgroundImageURL() != null
								&& themeOffersComponentModel.getBackgroundImageURL().getURL() != null)
						{
							themeOffersWsDTO.setBackgroundImageURL(themeOffersComponentModel.getBackgroundImageURL().getURL());
						}
						themeOffersWsDTO.setBackgroundImageURL("");
						themeOffersWsDTO
								.setBtnText(null != themeOffersComponentModel.getBtnText() ? themeOffersComponentModel.getBtnText() : "");
						themeOffersWsDTO.setItems(themeOffersElementList);
						themeOffersWsDTO.setOffers(themeOffersCompOfferList);
						themeOffersWsDTO
								.setTitle(null != themeOffersComponentModel.getTitle() ? themeOffersComponentModel.getTitle() : "");
						themeOffersWsDTO
								.setType(null != themeOffersComponentModel.getName() ? themeOffersComponentModel.getName() : "");
						themeOffersWsDTO
								.setWebURL(null != themeOffersComponentModel.getWebURL() ? themeOffersComponentModel.getWebURL() : "");
						uiCompPageElementWsDTO.setThemeOffersComponent(themeOffersWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof ThemeProductWidgetComponentModel)
					{
						final ThemeProductWidgetWsDTO themeProductWidgetWsDTO = new ThemeProductWidgetWsDTO();
						final ThemeProductWidgetComponentModel themeProductWidgetComponentModel = (ThemeProductWidgetComponentModel) abstractCMSComponentModel;
						final List<ThemeProWidElementWsDTO> themeProWidElementList = new ArrayList<ThemeProWidElementWsDTO>();

						if (null != themeProductWidgetComponentModel.getItems()
								&& themeProductWidgetComponentModel.getItems().size() > 0)
						{

							for (final ThemeProductWidgetElementModel productObj : themeProductWidgetComponentModel.getItems())
							{
								final ThemeProWidElementWsDTO themeProWidElementWsDTO = new ThemeProWidElementWsDTO();

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

									final ThemeProWidDiscountPriceWsDTO thDiscountPriceWsDTO = new ThemeProWidDiscountPriceWsDTO();
									final ThemeProWidMRPPriceWsDTO thMrpPriceWsDTO = new ThemeProWidMRPPriceWsDTO();

									thDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
									thDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
									if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
									{
										thDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										thMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
									}
									thMrpPriceWsDTO.setCurrencySymbol("₹");
									thDiscountPriceWsDTO.setCurrencySymbol("₹");
									thMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
									thMrpPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));
									themeProWidElementWsDTO.setPrdId(productObj.getProductCode().getCode());
									themeProWidElementWsDTO.setMrpPrice(thMrpPriceWsDTO);
									themeProWidElementWsDTO.setDiscountedPrice(thDiscountPriceWsDTO);
									themeProWidElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
									themeProWidElementWsDTO.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
									if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
											&& productObj.getProductCode().getPicture().getURL() != null)
									{
										themeProWidElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
									}
									else
									{
										themeProWidElementWsDTO.setImageURL("");
									}
								}
								themeProWidElementList.add(themeProWidElementWsDTO);
							}
						}
						if (themeProductWidgetComponentModel.getImageURL() != null
								&& themeProductWidgetComponentModel.getImageURL().getURL() != null)
						{
							themeProductWidgetWsDTO.setImageURL(themeProductWidgetComponentModel.getImageURL().getURL());
						}
						else
						{
							themeProductWidgetWsDTO.setImageURL("");
						}
						if (themeProductWidgetComponentModel.getBrandLogo() != null
								&& themeProductWidgetComponentModel.getBrandLogo().getURL() != null)
						{
							themeProductWidgetWsDTO.setBrandLogo(themeProductWidgetComponentModel.getBrandLogo().getURL());
						}
						else
						{
							themeProductWidgetWsDTO.setBrandLogo("");
						}
						themeProductWidgetWsDTO.setBtnText(null != themeProductWidgetComponentModel.getBtnText()
								? themeProductWidgetComponentModel.getBtnText() : "");
						themeProductWidgetWsDTO.setItems(themeProWidElementList);
						themeProductWidgetWsDTO.setTitle(
								null != themeProductWidgetComponentModel.getTitle() ? themeProductWidgetComponentModel.getTitle() : "");
						themeProductWidgetWsDTO.setType(
								null != themeProductWidgetComponentModel.getName() ? themeProductWidgetComponentModel.getName() : "");
						uiCompPageElementWsDTO.setMultiClickComponent(themeProductWidgetWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof ProductCapsulesComponentModel)
					{
						final ProductCapsulesComponentModel productCapsulesComponentModel = (ProductCapsulesComponentModel) abstractCMSComponentModel;
						final List<ProductCapsulesElementWsDTO> productCapsulesElementList = new ArrayList<ProductCapsulesElementWsDTO>();
						final ProductCapsulesWsDTO productCapsulesWsDTO = new ProductCapsulesWsDTO();

						if (null != productCapsulesComponentModel.getItems() && productCapsulesComponentModel.getItems().size() > 0)
						{
							for (final ProductCapsulesElementModel productCapsulesElementModel : productCapsulesComponentModel
									.getItems())
							{
								final ProductCapsulesElementWsDTO productCapsulesElementWsDTO = new ProductCapsulesElementWsDTO();
								if (null != productCapsulesElementModel.getImageURL()
										&& null != productCapsulesElementModel.getImageURL().getURL())
								{
									productCapsulesElementWsDTO.setImageURL(productCapsulesElementModel.getImageURL().getURL());
								}
								else
								{
									productCapsulesElementWsDTO.setImageURL("");
								}
								productCapsulesElementWsDTO.setWebURL(
										null != productCapsulesElementModel.getWebURL() ? productCapsulesElementModel.getWebURL() : "");
								productCapsulesElementList.add(productCapsulesElementWsDTO);
							}
						}
						productCapsulesWsDTO.setBtnText(
								null != productCapsulesComponentModel.getBtnText() ? productCapsulesComponentModel.getBtnText() : "");
						productCapsulesWsDTO.setDescription(null != productCapsulesComponentModel.getDescription()
								? productCapsulesComponentModel.getDescription() : "");
						productCapsulesWsDTO.setItems(productCapsulesElementList);
						productCapsulesWsDTO.setTitle(
								null != productCapsulesComponentModel.getTitle() ? productCapsulesComponentModel.getTitle() : "");
						productCapsulesWsDTO
								.setType(null != productCapsulesComponentModel.getName() ? productCapsulesComponentModel.getName() : "");
						productCapsulesWsDTO.setWebURL(
								null != productCapsulesComponentModel.getWebURL() ? productCapsulesComponentModel.getWebURL() : "");
						uiCompPageElementWsDTO.setProductCapsules(productCapsulesWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof BannerSeparatorComponentModel)
					{
						final BannerSeparatorWsDTO bannerSeperatorWsDTO = new BannerSeparatorWsDTO();
						final BannerSeparatorComponentModel bannerSeparatorComponentModel = (BannerSeparatorComponentModel) abstractCMSComponentModel;

						if (null != bannerSeparatorComponentModel.getIconImageURL()
								&& null != bannerSeparatorComponentModel.getIconImageURL().getURL())
						{
							bannerSeperatorWsDTO.setIconImageURL(bannerSeparatorComponentModel.getIconImageURL().getURL());
						}
						else
						{
							bannerSeperatorWsDTO.setIconImageURL("");
						}
						bannerSeperatorWsDTO.setEndHexCode(null != bannerSeparatorComponentModel.getEndHexCode()
								? bannerSeparatorComponentModel.getEndHexCode() : "");
						bannerSeperatorWsDTO.setStartHexCode(null != bannerSeparatorComponentModel.getStartHexCode()
								? bannerSeparatorComponentModel.getStartHexCode() : "");
						bannerSeperatorWsDTO.setDescription(null != bannerSeparatorComponentModel.getDescription()
								? bannerSeparatorComponentModel.getDescription() : "");
						bannerSeperatorWsDTO.setTitle(
								null != bannerSeparatorComponentModel.getTitle() ? bannerSeparatorComponentModel.getTitle() : "");
						bannerSeperatorWsDTO
								.setType(null != bannerSeparatorComponentModel.getName() ? bannerSeparatorComponentModel.getName() : "");
						bannerSeperatorWsDTO.setWebURL(
								null != bannerSeparatorComponentModel.getWebURL() ? bannerSeparatorComponentModel.getWebURL() : "");
						uiCompPageElementWsDTO.setBannerSeparatorComponent(bannerSeperatorWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof AutomatedBrandProductCarouselComponentModel)
					{
						final AutomatedBrandProductCarouselComponentModel automatedBrandProCarCompModel = (AutomatedBrandProductCarouselComponentModel) abstractCMSComponentModel;
						final List<AutomatedBrandProCarEleWsDTO> automatedBrandProCarEleList = new ArrayList<AutomatedBrandProCarEleWsDTO>();
						final AutomatedBrandProCarWsDTO automatedBrandProCarWsDTO = new AutomatedBrandProCarWsDTO();
						if (automatedBrandProCarCompModel.getItems() != null && automatedBrandProCarCompModel.getItems().size() > 0)
						{
							for (final AutomatedBrandProductCarElementModel productObj : automatedBrandProCarCompModel.getItems())
							{

								final AutomatedBrandProCarEleWsDTO automatedBrandProCarEleWsDTO = new AutomatedBrandProCarEleWsDTO();

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

									final AutoBrandProCarEleDiscountPriceWsDTO autoDiscountPriceWsDTO = new AutoBrandProCarEleDiscountPriceWsDTO();
									final AutoBrandProCarEleMRPPriceWsDTO autoMrpPriceWsDTO = new AutoBrandProCarEleMRPPriceWsDTO();

									autoDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
									autoDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
									if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
									{
										autoDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										autoMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
									}
									autoDiscountPriceWsDTO.setCurrencySymbol("₹");
									autoMrpPriceWsDTO.setCurrencySymbol("₹");
									autoMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
									autoMrpPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));
									automatedBrandProCarEleWsDTO.setPrdId(productObj.getProductCode().getCode());
									automatedBrandProCarEleWsDTO.setMrpPrice(autoMrpPriceWsDTO);
									automatedBrandProCarEleWsDTO.setDiscountedPrice(autoDiscountPriceWsDTO);
									automatedBrandProCarEleWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
									automatedBrandProCarEleWsDTO.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
									if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
											&& productObj.getProductCode().getPicture().getURL() != null)
									{
										automatedBrandProCarEleWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
									}
									else
									{
										automatedBrandProCarEleWsDTO.setImageURL("");
									}
								}
								automatedBrandProCarEleList.add(automatedBrandProCarEleWsDTO);
							}
						}
						if (null != automatedBrandProCarCompModel.getBrandLogo()
								&& null != automatedBrandProCarCompModel.getBrandLogo().getURL())
						{
							automatedBrandProCarWsDTO.setBrandLogo(automatedBrandProCarCompModel.getBrandLogo().getURL());
						}
						else
						{
							automatedBrandProCarWsDTO.setBrandLogo("");
						}
						automatedBrandProCarWsDTO.setBtnText(
								null != automatedBrandProCarCompModel.getBtnText() ? automatedBrandProCarCompModel.getBtnText() : "");
						automatedBrandProCarWsDTO.setDescription(null != automatedBrandProCarCompModel.getDescription()
								? automatedBrandProCarCompModel.getDescription() : "");
						if (automatedBrandProCarCompModel.getImageURL() != null
								&& automatedBrandProCarCompModel.getImageURL().getURL() != null)
						{
							automatedBrandProCarWsDTO.setImageURL(automatedBrandProCarCompModel.getImageURL().getURL());
						}
						else
						{
							automatedBrandProCarWsDTO.setImageURL("");
						}
						automatedBrandProCarWsDTO.setItems(automatedBrandProCarEleList);
						automatedBrandProCarWsDTO
								.setType(null != automatedBrandProCarCompModel.getName() ? automatedBrandProCarCompModel.getName() : "");
						automatedBrandProCarWsDTO.setWebURL(
								null != automatedBrandProCarCompModel.getWebURL() ? automatedBrandProCarCompModel.getWebURL() : "");
						uiCompPageElementWsDTO.setAutomatedBannerProductCarouselComponent(automatedBrandProCarWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof CuratedListingStripComponentModel)
					{
						final CuratedListingStripComponentModel curatedListStripCompModel = (CuratedListingStripComponentModel) abstractCMSComponentModel;
						final CuratedListingStripWsDTO curatedListingStripWsDTO = new CuratedListingStripWsDTO();

						curatedListingStripWsDTO.setStartHexCode(
								null != curatedListStripCompModel.getStartHexCode() ? curatedListStripCompModel.getStartHexCode() : "");
						curatedListingStripWsDTO
								.setTitle(null != curatedListStripCompModel.getTitle() ? curatedListStripCompModel.getTitle() : "");
						curatedListingStripWsDTO
								.setType(null != curatedListStripCompModel.getName() ? curatedListStripCompModel.getName() : "");
						curatedListingStripWsDTO
								.setWebURL(null != curatedListStripCompModel.getWebURL() ? curatedListStripCompModel.getWebURL() : "");
						uiCompPageElementWsDTO.setCuratedListingStripComponent(curatedListingStripWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof MonoBLPBannerComponentModel)
					{
						final MonoBLPBannerComponentModel monoBLPBannerComponentModel = (MonoBLPBannerComponentModel) abstractCMSComponentModel;
						final List<MonoBLPBannerElementWsDTO> monoBLPBannerElementList = new ArrayList<MonoBLPBannerElementWsDTO>();
						final MonoBLPBannerWsDTO moBannerWsDTO = new MonoBLPBannerWsDTO();

						if (null != monoBLPBannerComponentModel.getItems() && monoBLPBannerComponentModel.getItems().size() > 0)
						{
							for (final MonoBLPBannerElementModel monoBLPBannerElementModel : monoBLPBannerComponentModel.getItems())
							{
								final MonoBLPBannerElementWsDTO monoBLPBannerElementWsDTO = new MonoBLPBannerElementWsDTO();
								monoBLPBannerElementWsDTO.setBtnText(
										null != monoBLPBannerElementModel.getBtnText() ? monoBLPBannerElementModel.getBtnText() : "");
								monoBLPBannerElementWsDTO.setHexCode(
										null != monoBLPBannerElementModel.getHexCode() ? monoBLPBannerElementModel.getHexCode() : "");
								monoBLPBannerElementWsDTO
										.setTitle(null != monoBLPBannerElementModel.getTitle() ? monoBLPBannerElementModel.getTitle() : "");
								monoBLPBannerElementWsDTO.setWebURL(
										null != monoBLPBannerElementModel.getWebURL() ? monoBLPBannerElementModel.getWebURL() : "");
								if (monoBLPBannerElementModel.getImageURL() != null
										&& monoBLPBannerElementModel.getImageURL().getURL() != null)
								{
									monoBLPBannerElementWsDTO.setImageURL(monoBLPBannerElementModel.getImageURL().getURL());
								}
								else
								{
									monoBLPBannerElementWsDTO.setImageURL("");
								}
								monoBLPBannerElementList.add(monoBLPBannerElementWsDTO);
							}
						}
						moBannerWsDTO
								.setType(null != monoBLPBannerComponentModel.getName() ? monoBLPBannerComponentModel.getName() : "");
						moBannerWsDTO.setItems(monoBLPBannerElementList);
						moBannerWsDTO
								.setTitle(null != monoBLPBannerComponentModel.getTitle() ? monoBLPBannerComponentModel.getTitle() : "");
						uiCompPageElementWsDTO.setSingleBannerComponent(moBannerWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof SubBrandBannerBLPComponentModel)
					{
						final SubBrandBannerBLPComponentModel subBrandBLPBannerCompModel = (SubBrandBannerBLPComponentModel) abstractCMSComponentModel;
						final List<SubBrandBannerBLPElementWsDTO> subBrandBannerBLPEleList = new ArrayList<SubBrandBannerBLPElementWsDTO>();
						final SubBrandBannerBLPWsDTO subBrandBannerBLPWsDTO = new SubBrandBannerBLPWsDTO();

						if (null != subBrandBLPBannerCompModel.getItems() && subBrandBLPBannerCompModel.getItems().size() > 0)
						{
							for (final SubBrandBannerBLPElementModel subBannerBLPElementModel : subBrandBLPBannerCompModel.getItems())
							{
								final SubBrandBannerBLPElementWsDTO subBannerBLPEleWsDTO = new SubBrandBannerBLPElementWsDTO();
								subBannerBLPEleWsDTO.setWebURL(
										null != subBannerBLPElementModel.getWebURL() ? subBannerBLPElementModel.getWebURL() : "");
								if (subBannerBLPElementModel.getImageURL() != null
										&& subBannerBLPElementModel.getImageURL().getURL() != null)
								{
									subBannerBLPEleWsDTO.setImageURL(subBannerBLPElementModel.getImageURL().getURL());
								}
								else
								{
									subBannerBLPEleWsDTO.setImageURL("");
								}
								if (subBannerBLPElementModel.getBrandLogo() != null
										&& subBannerBLPElementModel.getBrandLogo().getURL() != null)
								{
									subBannerBLPEleWsDTO.setBrandLogo(subBannerBLPElementModel.getBrandLogo().getURL());
								}
								else
								{
									subBannerBLPEleWsDTO.setBrandLogo("");
								}
								subBrandBannerBLPEleList.add(subBannerBLPEleWsDTO);
							}
						}
						subBrandBannerBLPWsDTO
								.setType(null != subBrandBLPBannerCompModel.getName() ? subBrandBLPBannerCompModel.getName() : "");
						subBrandBannerBLPWsDTO.setItems(subBrandBannerBLPEleList);
						subBrandBannerBLPWsDTO
								.setTitle(null != subBrandBLPBannerCompModel.getTitle() ? subBrandBLPBannerCompModel.getTitle() : "");

						uiCompPageElementWsDTO.setSubBrandsBannerComponent(subBrandBannerBLPWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof TopCategoriesWidgetComponentModel)
					{
						final TopCategoriesWidgetComponentModel topCategoriesWidgetComponentModel = (TopCategoriesWidgetComponentModel) abstractCMSComponentModel;
						final List<TopCategoriesWidgetElementWsDTO> topCategoriesWidgetElementList = new ArrayList<TopCategoriesWidgetElementWsDTO>();
						final TopCategoriesWidgetWsDTO topCategoriesWidgetWsDTO = new TopCategoriesWidgetWsDTO();

						if (null != topCategoriesWidgetComponentModel.getItems()
								&& topCategoriesWidgetComponentModel.getItems().size() > 0)
						{
							for (final TopCategoriesWidgetElementModel topCategoriesWidgetElementModel : topCategoriesWidgetComponentModel
									.getItems())
							{
								final TopCategoriesWidgetElementWsDTO topCategoriesWidgetElementWsDTO = new TopCategoriesWidgetElementWsDTO();
								topCategoriesWidgetElementWsDTO.setWebURL(null != topCategoriesWidgetElementModel.getWebURL()
										? topCategoriesWidgetElementModel.getWebURL() : "");
								if (topCategoriesWidgetElementModel.getImageURL() != null
										&& topCategoriesWidgetElementModel.getImageURL().getURL() != null)
								{
									topCategoriesWidgetElementWsDTO.setImageURL(topCategoriesWidgetElementModel.getImageURL().getURL());
								}
								else
								{
									topCategoriesWidgetElementWsDTO.setImageURL("");
								}
								topCategoriesWidgetElementWsDTO.setTitle(null != topCategoriesWidgetElementModel.getTitle()
										? topCategoriesWidgetElementModel.getTitle() : "");
								topCategoriesWidgetElementList.add(topCategoriesWidgetElementWsDTO);
							}
						}
						topCategoriesWidgetWsDTO.setType(
								null != topCategoriesWidgetComponentModel.getName() ? topCategoriesWidgetComponentModel.getName() : "");
						topCategoriesWidgetWsDTO.setItems(topCategoriesWidgetElementList);
						topCategoriesWidgetWsDTO.setTitle(
								null != topCategoriesWidgetComponentModel.getTitle() ? topCategoriesWidgetComponentModel.getTitle() : "");

						uiCompPageElementWsDTO.setTopCategoriesComponent(topCategoriesWidgetWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof CuratedProductsWidgetComponentModel)
					{
						final CuratedProductsWidgetComponentModel curatedProWidgetCompModel = (CuratedProductsWidgetComponentModel) abstractCMSComponentModel;
						final List<CuratedProWidgetElementWsDTO> curatedProWidgetElementList = new ArrayList<CuratedProWidgetElementWsDTO>();
						final CuratedProductsWidgetWsDTO curatedProductsWidgetWsDTO = new CuratedProductsWidgetWsDTO();

						if (null != curatedProWidgetCompModel.getItems() && curatedProWidgetCompModel.getItems().size() > 0)
						{
							for (final CuratedProductsWidgetElementModel productObj : curatedProWidgetCompModel.getItems())
							{
								final CuratedProWidgetElementWsDTO curatedProWidgetElementWsDTO = new CuratedProWidgetElementWsDTO();
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

									final CuratedProWidgetEleDiscountPriceWsDTO curatedProWidgetEleDiscountPriceWsDTO = new CuratedProWidgetEleDiscountPriceWsDTO();
									final CuratedProWidgetEleMRPPriceWsDTO curatedProWidgetEleMRPPriceWsDTO = new CuratedProWidgetEleMRPPriceWsDTO();

									curatedProWidgetEleDiscountPriceWsDTO.setFormattedValue("" + Integer.parseInt(productPrice));
									curatedProWidgetEleDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
									if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
									{
										curatedProWidgetEleMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
										curatedProWidgetEleDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
									}
									curatedProWidgetEleDiscountPriceWsDTO.setCurrencySymbol("₹");
									curatedProWidgetEleMRPPriceWsDTO.setCurrencySymbol("₹");
									curatedProWidgetEleMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
									curatedProWidgetEleMRPPriceWsDTO.setFormattedValue("" + Integer.parseInt(productUnitPrice));

									curatedProWidgetElementWsDTO.setPrdId(productObj.getProductCode().getCode());
									curatedProWidgetElementWsDTO.setMrpPrice(curatedProWidgetEleMRPPriceWsDTO);
									curatedProWidgetElementWsDTO.setDiscountedPrice(curatedProWidgetEleDiscountPriceWsDTO);
									curatedProWidgetElementWsDTO.setTitle(null != productObj.getTitle() ? productObj.getTitle() : "");
									curatedProWidgetElementWsDTO.setWebURL(null != productObj.getWebURL() ? productObj.getWebURL() : "");
									curatedProWidgetElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
								}
								curatedProWidgetElementList.add(curatedProWidgetElementWsDTO);
							}
						}
						curatedProductsWidgetWsDTO
								.setBtnText(null != curatedProWidgetCompModel.getBtnText() ? curatedProWidgetCompModel.getBtnText() : "");
						curatedProductsWidgetWsDTO.setItems(curatedProWidgetElementList);
						curatedProductsWidgetWsDTO
								.setTitle(null != curatedProWidgetCompModel.getTitle() ? curatedProWidgetCompModel.getTitle() : "");
						curatedProductsWidgetWsDTO
								.setType(null != curatedProWidgetCompModel.getName() ? curatedProWidgetCompModel.getName() : "");
						curatedProductsWidgetWsDTO
								.setWebURL(null != curatedProWidgetCompModel.getWebURL() ? curatedProWidgetCompModel.getWebURL() : "");

						uiCompPageElementWsDTO.setCuratedProductsComponent(curatedProductsWidgetWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof SmartFilterWidgetComponentModel)
					{
						final SmartFilterWidgetWsDTO smartFilterWsDTO = new SmartFilterWidgetWsDTO();
						final List<SmartFilterWidgetElementWsDTO> smartFilterWidgetElementList = new ArrayList<SmartFilterWidgetElementWsDTO>();
						final SmartFilterWidgetComponentModel smartFilterWidgetComponentModel = (SmartFilterWidgetComponentModel) abstractCMSComponentModel;

						if (null != smartFilterWidgetComponentModel.getItems() && smartFilterWidgetComponentModel.getItems().size() > 0)
						{
							for (final SmartFilterWidgetElementModel smartFilterWidgetElementModel : smartFilterWidgetComponentModel
									.getItems())
							{
								final SmartFilterWidgetElementWsDTO smartFilterWidgetElementWsDTO = new SmartFilterWidgetElementWsDTO();
								smartFilterWidgetElementWsDTO.setDescription(null != smartFilterWidgetElementModel.getDescription()
										? smartFilterWidgetElementModel.getDescription() : "");
								if (smartFilterWidgetElementModel.getImageURL() != null
										&& smartFilterWidgetElementModel.getImageURL().getURL() != null)
								{
									smartFilterWidgetElementWsDTO.setImageURL(smartFilterWidgetElementModel.getImageURL().getURL());
								}
								else
								{
									smartFilterWidgetElementWsDTO.setImageURL("");
								}
								smartFilterWidgetElementWsDTO.setTitle(
										null != smartFilterWidgetElementModel.getTitle() ? smartFilterWidgetElementModel.getTitle() : "");
								smartFilterWidgetElementWsDTO.setWebURL(
										null != smartFilterWidgetElementModel.getWebURL() ? smartFilterWidgetElementModel.getWebURL() : "");
								smartFilterWidgetElementList.add(smartFilterWidgetElementWsDTO);
							}
						}
						smartFilterWsDTO.setItems(smartFilterWidgetElementList);
						smartFilterWsDTO.setTitle(
								null != smartFilterWidgetComponentModel.getTitle() ? smartFilterWidgetComponentModel.getTitle() : "");
						smartFilterWsDTO.setType(
								null != smartFilterWidgetComponentModel.getName() ? smartFilterWidgetComponentModel.getName() : "");

						uiCompPageElementWsDTO.setTwoByTwoBannerComponent(smartFilterWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage("SUCCESS");
						uiComponentWiseWsDTO.setStatus("pageComponent");
						return uiComponentWiseWsDTO;
					}
				}
			}
		}
		return uiComponentWiseWsDTO;
	}
}