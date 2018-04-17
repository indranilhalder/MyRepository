
package com.tisl.mpl.v2.controller;


/**
 * @author TUL
 *
 */
import de.hybris.platform.acceleratorcms.model.components.AccountNavigationComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.tisl.mpl.marketplacecommerceservice.url.ExtDefaultCategoryModelUrlResolver;
import com.tisl.mpl.marketplacecommerceservices.service.MplCMSComponentService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.model.cms.components.AdobeTargetComponentModel;
import com.tisl.mpl.model.cms.components.AutoProductRecommendationComponentModel;
import com.tisl.mpl.model.cms.components.AutoProductRecommendationElementModel;
import com.tisl.mpl.model.cms.components.AutomatedBrandProductCarElementModel;
import com.tisl.mpl.model.cms.components.AutomatedBrandProductCarouselComponentModel;
import com.tisl.mpl.model.cms.components.BannerProdCarouselElementCompModel;
import com.tisl.mpl.model.cms.components.BannerProductCarouselComponentModel;
import com.tisl.mpl.model.cms.components.BannerSeparatorComponentModel;
import com.tisl.mpl.model.cms.components.BrandTabAZBrandElementModel;
import com.tisl.mpl.model.cms.components.BrandsTabAZElementModel;
import com.tisl.mpl.model.cms.components.BrandsTabAZListComponentModel;
import com.tisl.mpl.model.cms.components.CMSTextComponentModel;
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
import com.tisl.mpl.model.cms.components.LandingPageHeaderComponentModel;
import com.tisl.mpl.model.cms.components.LandingPageHeaderElementModel;
import com.tisl.mpl.model.cms.components.LandingPageHierarchyComponentModel;
import com.tisl.mpl.model.cms.components.LandingPageHierarchyElementListModel;
import com.tisl.mpl.model.cms.components.LandingPageHierarchyElementModel;
import com.tisl.mpl.model.cms.components.LandingPageTitleComponentModel;
import com.tisl.mpl.model.cms.components.MSDComponentModel;
import com.tisl.mpl.model.cms.components.MonoBLPBannerComponentModel;
import com.tisl.mpl.model.cms.components.MonoBLPBannerElementModel;
import com.tisl.mpl.model.cms.components.OffersWidgetComponentModel;
import com.tisl.mpl.model.cms.components.OffersWidgetElementModel;
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
import com.tisl.wsdto.SeoContentData;


@Controller
@RequestMapping(value = "/{baseSiteId}/cms")
public class DefaultCMSComponentControler
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCMSComponentControler.class);

	@Autowired
	private MplCMSPageServiceImpl mplCMSPageService;

	@Autowired
	private CMSRestrictionService cmsRestrictionService;

	@Autowired
	private BuyBoxFacade buyBoxFacade;

	@Autowired
	private MplCMSComponentService mplCmsComponentService;

	@Autowired
	private ConfigurationService configurationService;

	@Resource(name = "productModelUrlResolver")
	private UrlResolver<ProductModel> productModelUrlResolver;

	@Resource(name = "defaultCategoryModelUrlResolver")
	private ExtDefaultCategoryModelUrlResolver defaultCategoryModelUrlResolver;

	@Autowired
	private DefaultCategoryService categoryService;


	private final String Success = "SUCCESS";
	private final String pageComponent = "pageComponent";
	private static final String heroBannerComp = "Hero Banner Component";

	public SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

	public static final String heroBannerError = "Error in getting HeroBannerComponent with id:";
	public static final String connectBannerComponentError = "Error in getting connectBannerComponentModel with id:";
	public static final String offerWidgetComponentError = "Error in getting OffersWidgetComponent with id: ";
	public static final String fscProductError = "Flash Sales Component Product is not properly enriched or either out of stock: code-";
	public static final String fscError = "Error in getting FlashSalesComponent with id: ";
	public static final String contentWidgetError = "Error in getting ContentWidgetComponent with id: ";
	public static final String bannerProductCarouseError = "BannerProductCarouselComponent Product is not properly enriched or either out of stock, code:-";
	public static final String bpcError = "Error in getting BannerProductCarouselComponent with id: ";
	public static final String VideoProductCarouselError = "VideoProductCarouselComponentModel Product is not properly enriched or either out of stock, code:-";
	public static final String vpcError = "Error in getting VideoProductCarouselComponent with id: ";
	public static final String themeOffersError = "ThemeOffersComponentModel Product is not properly enriched or either out of stock code:-";
	public static final String toError = "Error in getting ThemeOffersComponent with id: ";
	public static final String themeProductWidgetError = "ThemeProductWidgetComponentModel Product is not properly enriched or either out of stock code:-";
	public static final String tpwError = "Error in getting ThemeProductWidgetComponent with id: ";
	public static final String BannerSeparatorError = "Error in getting BannerSeparatorComponent with id: ";
	public static final String AutomatedBrandProCarError = "AutomatedBrandProCarCompModel Product is not properly enriched or either out of stock code:- ";
	public static final String abpcError = "Error in getting AutomatedBrandProductCarouselComponent with id: ";
	public static final String CuratedListingStripError = "Error in getting CuratedListingStripComponent with id: ";
	public static final String MonoBLPBannerError = "Error in getting MonoBLPBannerComponent with id: ";
	public static final String SubBrandBLPBannerError = "Error in getting SubBrandBLPBannerComponent with id: ";
	public static final String TopCategoriesWidgetEror = "Error in getting TopCategoriesWidgetComponent with id: ";
	public static final String CuratedProductWidgetEror = "CuratedProductWidgetComponent Product is not properly enriched or either out of stock code:-";
	public static final String cpwError = "Error in getting CuratedProductWidgetComponent with id: ";
	public static final String SmartFilterWidgetError = "Error in getting SmartFilterWidgetComponent with id: ";
	public static final String MsdError = "Error in getting MsdComponent with id: ";
	public static final String AdobeTargetError = "Error in getting AdobeTargetComponent with id: ";
	public static final String BrandsTabAZListError = "Error in getting BrandsTabAZListComponent with id: ";
	public static final String LandingPageTitleError = "Error in getting LandingPageTitleComponent with id: ";
	public static final String LandingPageHeaderError = "Error in getting LandingPageHeaderComponent with id: ";
	public static final String LandingPageHierError = "Error in getting LandingPageHierarchyComponent with id: ";
	public static final String cmsParagraphError = "Error in getting cmsParagraphComponentModel with id: ";
	public static final String simpleBannerError = "Error in getting simpleBannerComponentModel with id: ";
	public static final String accountNavigationError = "Error in getting accountNavigationComponentModel with id: ";


	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/defaultpage", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public UICompPageWiseWsDTO getComponentsForPage(@RequestParam final String pageId,
			@RequestParam(defaultValue = StringUtils.EMPTY, required = false) final String categoryId)
			throws CMSItemNotFoundException, EtailNonBusinessExceptions
	{
		final ContentPageModel contentPage = mplCMSPageService.getPageByLabelOrId(pageId);

		final List<UICompPageElementWsDTO> genericUICompPageWsDTO = new ArrayList<UICompPageElementWsDTO>();
		final List<BreadcrumbListWsDTO> breadcrumbs = new ArrayList<BreadcrumbListWsDTO>();

		Collection<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
		CategoryModel toDisplay = null;
		final BreadcrumbListWsDTO breadcrumbDTO = new BreadcrumbListWsDTO();

		final UICompPageWiseWsDTO uiCompPageObj = new UICompPageWiseWsDTO();
		final String siteUrl = configurationService.getConfiguration().getString("website.mpl.https");
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
							final HeroBannerComponentModel heroBannerCompObj = (HeroBannerComponentModel) abstractCMSComponentModel;
							if (null != heroBannerCompObj.getVisible() && heroBannerCompObj.getVisible().booleanValue())
							{
								final HeroBannerCompWsDTO heroBannerCompWsDTO = new HeroBannerCompWsDTO();
								final UICompPageElementWsDTO uiCompPageElementObj = new UICompPageElementWsDTO();
								final List<HeroBannerCompListWsDTO> heroBannerCompListWsDTO = new ArrayList<HeroBannerCompListWsDTO>();


								if (null != heroBannerCompObj.getItems() && heroBannerCompObj.getItems().size() > 0)
								{
									try
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
												heroBannerCompListObj.setImageURL(StringUtils.EMPTY);
											}
											if (null != heroBannerElementModel.getBrandLogo()
													&& null != heroBannerElementModel.getBrandLogo().getURL())
											{
												heroBannerCompListObj.setBrandLogo(heroBannerElementModel.getBrandLogo().getURL());
											}
											else
											{
												heroBannerCompListObj.setBrandLogo(StringUtils.EMPTY);
											}
											heroBannerCompListObj.setTitle(null != heroBannerElementModel.getTitle()
													? heroBannerElementModel.getTitle() : StringUtils.EMPTY);
											heroBannerCompListObj.setWebURL(null != heroBannerElementModel.getWebURL()
													? heroBannerElementModel.getWebURL() : StringUtils.EMPTY);
											heroBannerCompListWsDTO.add(heroBannerCompListObj);
										}
									}
									catch (final EtailNonBusinessExceptions e)
									{
										heroBannerCompWsDTO.setComponentId(
												null != heroBannerCompObj.getUid() ? heroBannerCompObj.getUid() : StringUtils.EMPTY);
										LOG.error(heroBannerError + heroBannerCompObj.getUid(), e);
										continue;
									}
									catch (final Exception e)
									{
										heroBannerCompWsDTO.setComponentId(
												null != heroBannerCompObj.getUid() ? heroBannerCompObj.getUid() : StringUtils.EMPTY);
										LOG.error(heroBannerError + heroBannerCompObj.getUid(), e);
										continue;
									}
								}
								heroBannerCompWsDTO.setItems(heroBannerCompListWsDTO);
								heroBannerCompWsDTO.setType(heroBannerComp);
								uiCompPageElementObj.setComponentName("heroBannerComponent");
								heroBannerCompWsDTO.setComponentId(
										null != heroBannerCompObj.getUid() ? heroBannerCompObj.getUid() : StringUtils.EMPTY);
								uiCompPageElementObj.setHeroBannerComponent(heroBannerCompWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementObj);
							}
						}

						if (abstractCMSComponentModel instanceof ConnectBannerComponentModel)
						{
							final ConnectBannerComponentModel connectBannerComponentModel = (ConnectBannerComponentModel) abstractCMSComponentModel;
							if (null != connectBannerComponentModel.getVisible()
									&& connectBannerComponentModel.getVisible().booleanValue())
							{
								final UICompPageElementWsDTO uiCompPageElementObj = new UICompPageElementWsDTO();
								final ConnectBannerWsDTO connectBannerWsDTO = new ConnectBannerWsDTO();
								try
								{
									if (null != connectBannerComponentModel.getBackgroundImageURL()
											&& null != connectBannerComponentModel.getBackgroundImageURL().getURL())
									{
										connectBannerWsDTO
												.setBackgroundImageURL(connectBannerComponentModel.getBackgroundImageURL().getURL());
									}
									else
									{
										connectBannerWsDTO.setBackgroundImageURL(StringUtils.EMPTY);
									}
									if (null != connectBannerComponentModel.getIconImageURL()
											&& null != connectBannerComponentModel.getIconImageURL().getURL())
									{
										connectBannerWsDTO.setIconImageURL(connectBannerComponentModel.getIconImageURL().getURL());
									}
									else
									{
										connectBannerWsDTO.setIconImageURL(StringUtils.EMPTY);
									}
									connectBannerWsDTO.setBtnText(null != connectBannerComponentModel.getBtnText()
											? connectBannerComponentModel.getBtnText() : StringUtils.EMPTY);
									connectBannerWsDTO.setDescription(null != connectBannerComponentModel.getDescription()
											? connectBannerComponentModel.getDescription() : StringUtils.EMPTY);
									connectBannerWsDTO.setSubType(null != connectBannerComponentModel.getSubType()
											? connectBannerComponentModel.getSubType() : StringUtils.EMPTY);
									connectBannerWsDTO.setTitle(null != connectBannerComponentModel.getTitle()
											? connectBannerComponentModel.getTitle() : StringUtils.EMPTY);
									connectBannerWsDTO.setWebURL(null != connectBannerComponentModel.getWebURL()
											? connectBannerComponentModel.getWebURL() : StringUtils.EMPTY);
									connectBannerWsDTO.setStartHexCode(null != connectBannerComponentModel.getStartHexCode()
											? connectBannerComponentModel.getStartHexCode() : StringUtils.EMPTY);
									connectBannerWsDTO.setEndHexCode(null != connectBannerComponentModel.getEndHexCode()
											? connectBannerComponentModel.getEndHexCode() : StringUtils.EMPTY);
									connectBannerWsDTO.setType("Multipurpose Banner Component");
									uiCompPageElementObj.setComponentName("multiPurposeBanner");
									connectBannerWsDTO.setComponentId(null != connectBannerComponentModel.getUid()
											? connectBannerComponentModel.getUid() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									connectBannerWsDTO.setComponentId(null != connectBannerComponentModel.getUid()
											? connectBannerComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(connectBannerComponentError + connectBannerComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									connectBannerWsDTO.setComponentId(null != connectBannerComponentModel.getUid()
											? connectBannerComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(connectBannerComponentError + connectBannerComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementObj.setMultiPurposeBanner(connectBannerWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementObj);
							}
						}

						if (abstractCMSComponentModel instanceof OffersWidgetComponentModel)
						{
							final OffersWidgetComponentModel offersWidgetComponentModel = (OffersWidgetComponentModel) abstractCMSComponentModel;
							if (null != offersWidgetComponentModel.getVisible()
									&& offersWidgetComponentModel.getVisible().booleanValue())
							{
								final OffersWidgetWsDTO offersWidgetWsDTO = new OffersWidgetWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								final List<OffersWidgetElementWsDTO> offersWidgetElementList = new ArrayList<OffersWidgetElementWsDTO>();

								try
								{
									if (null != offersWidgetComponentModel.getItems() && offersWidgetComponentModel.getItems().size() > 0)
									{
										for (final OffersWidgetElementModel offersWidgetElementModel : offersWidgetComponentModel
												.getItems())
										{
											final OffersWidgetElementWsDTO offersWidgetElementWsDTO = new OffersWidgetElementWsDTO();

											if (null != offersWidgetElementModel.getImageURL()
													&& null != offersWidgetElementModel.getImageURL().getURL())
											{
												offersWidgetElementWsDTO.setImageURL(offersWidgetElementModel.getImageURL().getURL());
											}
											else
											{
												offersWidgetElementWsDTO.setImageURL(StringUtils.EMPTY);
											}
											offersWidgetElementWsDTO.setTitle(null != offersWidgetElementModel.getTitle()
													? offersWidgetElementModel.getTitle() : StringUtils.EMPTY);
											offersWidgetElementWsDTO.setBtnText(null != offersWidgetElementModel.getBtnText()
													? offersWidgetElementModel.getBtnText() : StringUtils.EMPTY);
											offersWidgetElementWsDTO.setDiscountText(null != offersWidgetElementModel.getDiscountText()
													? offersWidgetElementModel.getDiscountText() : StringUtils.EMPTY);
											offersWidgetElementWsDTO.setWebURL(null != offersWidgetElementModel.getWebURL()
													? offersWidgetElementModel.getWebURL() : StringUtils.EMPTY);
											offersWidgetElementList.add(offersWidgetElementWsDTO);
										}
									}
									offersWidgetWsDTO.setItems(offersWidgetElementList);
									offersWidgetWsDTO.setTitle(null != offersWidgetComponentModel.getTitle()
											? offersWidgetComponentModel.getTitle() : StringUtils.EMPTY);
									offersWidgetWsDTO.setType("Offers Component");
									uiCompPageElementWsDTO.setComponentName("offersComponent");
									offersWidgetWsDTO.setComponentId(null != offersWidgetComponentModel.getUid()
											? offersWidgetComponentModel.getUid() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									offersWidgetWsDTO.setComponentId(null != offersWidgetComponentModel.getUid()
											? offersWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(offerWidgetComponentError + offersWidgetComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									offersWidgetWsDTO.setComponentId(null != offersWidgetComponentModel.getUid()
											? offersWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(offerWidgetComponentError + offersWidgetComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setOffersComponent(offersWidgetWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}

						}

						if (abstractCMSComponentModel instanceof FlashSalesComponentModel)
						{
							final FlashSalesComponentModel flashSalesComponentModel = (FlashSalesComponentModel) abstractCMSComponentModel;
							if (null != flashSalesComponentModel.getVisible() && flashSalesComponentModel.getVisible().booleanValue())
							{
								final FlashSalesWsDTO flashSalesWsDTO = new FlashSalesWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								final List<FlashSalesOffersWsDTO> flashSalesOffersWsDTOList = new ArrayList<FlashSalesOffersWsDTO>();
								final List<FlashSalesElementWsDTO> flashSalesElementWsDTOList = new ArrayList<FlashSalesElementWsDTO>();
								try
								{
									if (null != flashSalesComponentModel.getOffers() && flashSalesComponentModel.getOffers().size() > 0)
									{
										for (final FlashSalesElementModel flashSalesOffersModel : flashSalesComponentModel.getOffers())
										{
											final FlashSalesOffersWsDTO flashSalesOffersWsDTO = new FlashSalesOffersWsDTO();
											flashSalesOffersWsDTO.setTitle(null != flashSalesOffersModel.getTitle()
													? flashSalesOffersModel.getTitle() : StringUtils.EMPTY);
											flashSalesOffersWsDTO.setDescription(null != flashSalesOffersModel.getDescription()
													? flashSalesOffersModel.getDescription() : StringUtils.EMPTY);
											if (null != flashSalesOffersModel.getImageURL()
													&& null != flashSalesOffersModel.getImageURL().getURL())
											{
												flashSalesOffersWsDTO.setImageURL(flashSalesOffersModel.getImageURL().getURL());
											}
											else
											{
												flashSalesOffersWsDTO.setImageURL(StringUtils.EMPTY);
											}
											flashSalesOffersWsDTO.setWebURL(null != flashSalesOffersModel.getWebURL()
													? flashSalesOffersModel.getWebURL() : StringUtils.EMPTY);

											flashSalesOffersWsDTOList.add(flashSalesOffersWsDTO);
										}
									}
									if (null != flashSalesComponentModel.getItems() && flashSalesComponentModel.getItems().size() > 0)
									{
										String productCode = StringUtils.EMPTY;
										try
										{
											for (final FlashSalesItemElementModel flashSalesElementModel : flashSalesComponentModel
													.getItems())
											{
												try
												{
													final FlashSalesElementWsDTO flashSalesElementWsDTO = new FlashSalesElementWsDTO();

													if (null != flashSalesElementModel && null != flashSalesElementModel.getProductCode()
															&& null != flashSalesElementModel.getProductCode().getCode())
													{
														productCode = flashSalesElementModel.getProductCode().getCode();
														final BuyBoxData buyboxdata = buyBoxFacade
																.buyboxPrice(flashSalesElementModel.getProductCode().getCode());
														String productUnitPrice = StringUtils.EMPTY;
														String productPrice = StringUtils.EMPTY;

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

														flashSalesDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
														flashSalesDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
														if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
														{
															flashSalesDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
															flashSalesMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
														}
														flashSalesDiscountPriceWsDTO.setCurrencySymbol("₹");
														flashSalesMRPPriceWsDTO.setCurrencySymbol("₹");
														flashSalesMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
														flashSalesMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));

														flashSalesElementWsDTO.setPrdId(flashSalesElementModel.getProductCode().getCode());
														flashSalesElementWsDTO.setMrpPrice(flashSalesMRPPriceWsDTO);
														flashSalesElementWsDTO.setDiscountedPrice(flashSalesDiscountPriceWsDTO);
														flashSalesElementWsDTO.setTitle(null != flashSalesElementModel.getProductCode()
																? flashSalesElementModel.getProductCode().getName() : StringUtils.EMPTY);
														if (null != productModelUrlResolver)
														{
															flashSalesElementWsDTO.setWebURL(siteUrl
																	+ productModelUrlResolver.resolve(flashSalesElementModel.getProductCode()));
														}
														else
														{
															flashSalesElementWsDTO
																	.setWebURL(siteUrl + "/" + flashSalesElementModel.getProductCode().getCode());
														}
														if (flashSalesElementModel.getProductCode().getPicture() != null
																&& flashSalesElementModel.getProductCode().getPicture().getURL() != null)
														{
															flashSalesElementWsDTO
																	.setImageURL(flashSalesElementModel.getProductCode().getPicture().getURL());
														}
														else
														{
															flashSalesElementWsDTO.setImageURL(StringUtils.EMPTY);
														}
														flashSalesElementWsDTOList.add(flashSalesElementWsDTO);
													}
												}
												catch (final EtailNonBusinessExceptions e)
												{
													LOG.error(fscProductError + productCode, e);
													continue;
												}
												catch (final Exception e)
												{
													LOG.error(fscProductError + productCode, e);
													continue;
												}
											}
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(fscProductError + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(fscProductError + productCode, e);
											continue;
										}
									}
									flashSalesWsDTO.setBackgroundHexCode(null != flashSalesComponentModel.getBackgroundHexCode()
											? flashSalesComponentModel.getBackgroundHexCode() : StringUtils.EMPTY);
									if (null != flashSalesComponentModel.getBackgroundImageURL()
											&& null != flashSalesComponentModel.getBackgroundImageURL().getURL())
									{
										flashSalesWsDTO.setBackgroundImageURL(flashSalesComponentModel.getBackgroundImageURL().getURL());
									}
									else
									{
										flashSalesWsDTO.setBackgroundImageURL(StringUtils.EMPTY);
									}
									flashSalesWsDTO.setBtnText(null != flashSalesComponentModel.getBtnText()
											? flashSalesComponentModel.getBtnText() : StringUtils.EMPTY);
									flashSalesWsDTO.setDescription(null != flashSalesComponentModel.getDescription()
											? flashSalesComponentModel.getDescription() : StringUtils.EMPTY);

									flashSalesWsDTO.setEndDate(null != flashSalesComponentModel.getEndDate()
											? formatter.format(flashSalesComponentModel.getEndDate()) : StringUtils.EMPTY);
									flashSalesWsDTO.setStartDate(null != flashSalesComponentModel.getStartDate()
											? formatter.format(flashSalesComponentModel.getStartDate()) : StringUtils.EMPTY);

									flashSalesWsDTO.setTitle(null != flashSalesComponentModel.getTitle()
											? flashSalesComponentModel.getTitle() : StringUtils.EMPTY);
									flashSalesWsDTO.setWebURL(null != flashSalesComponentModel.getWebURL()
											? flashSalesComponentModel.getWebURL() : StringUtils.EMPTY);
									flashSalesWsDTO.setOffers(flashSalesOffersWsDTOList);
									flashSalesWsDTO.setItems(flashSalesElementWsDTOList);
									flashSalesWsDTO.setType("Flash Sales Component");
									uiCompPageElementWsDTO.setComponentName("flashSalesComponent");
									flashSalesWsDTO.setComponentId(null != flashSalesComponentModel.getUid()
											? flashSalesComponentModel.getUid() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									flashSalesWsDTO.setComponentId(null != flashSalesComponentModel.getUid()
											? flashSalesComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(fscError + flashSalesComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									flashSalesWsDTO.setComponentId(null != flashSalesComponentModel.getUid()
											? flashSalesComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(fscError + flashSalesComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setFlashSalesComponent(flashSalesWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}


						if (abstractCMSComponentModel instanceof ContentWidgetComponentModel)
						{
							final ContentWidgetComponentModel contentWidgetComponentModel = (ContentWidgetComponentModel) abstractCMSComponentModel;
							if (null != contentWidgetComponentModel.getVisible()
									&& contentWidgetComponentModel.getVisible().booleanValue())
							{

								final ContentWidgetCompWsDTO contentWidgetCompWsDTO = new ContentWidgetCompWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								final List<ContentWidgetElementWsDTO> contentWidgetElementList = new ArrayList<ContentWidgetElementWsDTO>();
								try
								{
									if (null != contentWidgetComponentModel.getItems()
											&& contentWidgetComponentModel.getItems().size() > 0)
									{
										for (final ContentWidgetElementModel contentWidgetElementModel : contentWidgetComponentModel
												.getItems())
										{
											final ContentWidgetElementWsDTO contentWidgetElementWsDTO = new ContentWidgetElementWsDTO();

											if (null != contentWidgetElementModel.getImageURL()
													&& null != contentWidgetElementModel.getImageURL().getURL())
											{
												contentWidgetElementWsDTO.setImageURL(contentWidgetElementModel.getImageURL().getURL());
											}
											else
											{
												contentWidgetElementWsDTO.setImageURL(StringUtils.EMPTY);
											}
											contentWidgetElementWsDTO.setDescription(null != contentWidgetElementModel.getDescription()
													? contentWidgetElementModel.getDescription() : StringUtils.EMPTY);
											contentWidgetElementWsDTO.setTitle(null != contentWidgetElementModel.getTitle()
													? contentWidgetElementModel.getTitle() : StringUtils.EMPTY);
											contentWidgetElementWsDTO.setWebURL(null != contentWidgetElementModel.getWebURL()
													? contentWidgetElementModel.getWebURL() : StringUtils.EMPTY);
											contentWidgetElementWsDTO.setBtnText(null != contentWidgetElementModel.getBtnText()
													? contentWidgetElementModel.getBtnText() : StringUtils.EMPTY);

											contentWidgetElementList.add(contentWidgetElementWsDTO);
										}
									}
									contentWidgetCompWsDTO.setItems(contentWidgetElementList);
									contentWidgetCompWsDTO.setTitle(null != contentWidgetComponentModel.getTitle()
											? contentWidgetComponentModel.getTitle() : StringUtils.EMPTY);
									contentWidgetCompWsDTO.setType("Content Component");
									uiCompPageElementWsDTO.setComponentName("contentComponent");
									contentWidgetCompWsDTO.setComponentId(null != contentWidgetComponentModel.getUid()
											? contentWidgetComponentModel.getUid() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									contentWidgetCompWsDTO.setComponentId(null != contentWidgetComponentModel.getUid()
											? contentWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(contentWidgetError + contentWidgetComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									contentWidgetCompWsDTO.setComponentId(null != contentWidgetComponentModel.getUid()
											? contentWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(contentWidgetError + contentWidgetComponentModel.getUid(), e);
									continue;

								}
								uiCompPageElementWsDTO.setContentComponent(contentWidgetCompWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof BannerProductCarouselComponentModel)
						{
							final BannerProductCarouselComponentModel bannerProComponentModel = (BannerProductCarouselComponentModel) abstractCMSComponentModel;
							if (null != bannerProComponentModel.getVisible() && bannerProComponentModel.getVisible().booleanValue())
							{
								final BannerProductCarouselWsDTO bannerProductCarouselWsDTO = new BannerProductCarouselWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								final List<BannerProCarouselElementWsDTO> bannerProCarouselList = new ArrayList<BannerProCarouselElementWsDTO>();
								try
								{
									if (null != bannerProComponentModel.getItems() && bannerProComponentModel.getItems().size() > 0)
									{
										String productCode = StringUtils.EMPTY;

										for (final BannerProdCarouselElementCompModel productObj : bannerProComponentModel.getItems())
										{
											try
											{
												final BannerProCarouselElementWsDTO bannerProCarouselElementWsDTO = new BannerProCarouselElementWsDTO();
												if (null != productObj && null != productObj.getProductCode()
														&& null != productObj.getProductCode().getCode())
												{
													productCode = productObj.getProductCode().getCode();
													final BuyBoxData buyboxdata = buyBoxFacade
															.buyboxPrice(productObj.getProductCode().getCode());
													String productUnitPrice = StringUtils.EMPTY;
													String productPrice = StringUtils.EMPTY;

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

													bannerProDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
													bannerProDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
													if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
													{
														bannerProMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
														bannerProDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
													}
													bannerProDiscountPriceWsDTO.setCurrencySymbol("₹");
													bannerProMRPPriceWsDTO.setCurrencySymbol("₹");
													bannerProMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
													bannerProMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));

													bannerProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
													bannerProCarouselElementWsDTO.setMrpPrice(bannerProMRPPriceWsDTO);
													bannerProCarouselElementWsDTO.setDiscountedPrice(bannerProDiscountPriceWsDTO);
													bannerProCarouselElementWsDTO.setTitle(null != productObj.getProductCode()
															? productObj.getProductCode().getName() : StringUtils.EMPTY);
													if (null != productModelUrlResolver)
													{
														bannerProCarouselElementWsDTO.setWebURL(
																siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
													}
													else
													{
														bannerProCarouselElementWsDTO
																.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
													}
													if (null != productObj.getProductCode().getPicture()
															&& null != productObj.getProductCode().getPicture().getURL())
													{
														bannerProCarouselElementWsDTO
																.setImageURL(productObj.getProductCode().getPicture().getURL());
													}
													else
													{
														bannerProCarouselElementWsDTO.setImageURL(StringUtils.EMPTY);
													}
												}
												bannerProCarouselList.add(bannerProCarouselElementWsDTO);

											}
											catch (final EtailNonBusinessExceptions e)
											{
												LOG.error(bannerProductCarouseError + productCode, e);
												continue;
											}
											catch (final Exception e)
											{
												LOG.error(bannerProductCarouseError + productCode, e);
												continue;
											}
										}

									}
									bannerProductCarouselWsDTO.setBtnText(null != bannerProComponentModel.getBtnText()
											? bannerProComponentModel.getBtnText() : StringUtils.EMPTY);
									if (null != bannerProComponentModel.getImageURL()
											&& null != bannerProComponentModel.getImageURL().getURL())
									{
										bannerProductCarouselWsDTO.setImageURL(bannerProComponentModel.getImageURL().getURL());
									}
									else
									{
										bannerProductCarouselWsDTO.setImageURL(StringUtils.EMPTY);
									}
									bannerProductCarouselWsDTO.setItems(bannerProCarouselList);
									bannerProductCarouselWsDTO.setType("Banner Product Carousel Component");
									uiCompPageElementWsDTO.setComponentName("bannerProductCarouselComponent");
									bannerProductCarouselWsDTO.setComponentId(
											null != bannerProComponentModel.getUid() ? bannerProComponentModel.getUid() : StringUtils.EMPTY);
									bannerProductCarouselWsDTO.setDescription(null != bannerProComponentModel.getDescription()
											? bannerProComponentModel.getDescription() : StringUtils.EMPTY);
									bannerProductCarouselWsDTO.setTitle(null != bannerProComponentModel.getTitle()
											? bannerProComponentModel.getTitle() : StringUtils.EMPTY);
									bannerProductCarouselWsDTO.setWebURL(null != bannerProComponentModel.getWebURL()
											? bannerProComponentModel.getWebURL() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									bannerProductCarouselWsDTO.setComponentId(
											null != bannerProComponentModel.getUid() ? bannerProComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(bpcError + bannerProComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									bannerProductCarouselWsDTO.setComponentId(
											null != bannerProComponentModel.getUid() ? bannerProComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(bpcError + bannerProComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setBannerProductCarouselComponent(bannerProductCarouselWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);

							}
						}

						if (abstractCMSComponentModel instanceof VideoProductCarouselComponentModel)
						{
							final VideoProductCarouselComponentModel videoProComponentModel = (VideoProductCarouselComponentModel) abstractCMSComponentModel;
							if (null != videoProComponentModel.getVisible() && videoProComponentModel.getVisible().booleanValue())
							{
								final VideoProductCarouselWsDTO videoProductCarouselWsDTO = new VideoProductCarouselWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								final List<VideoProductCarElementWsDTO> videoProCarouselList = new ArrayList<VideoProductCarElementWsDTO>();
								try
								{
									if (null != videoProComponentModel.getItems() && videoProComponentModel.getItems().size() > 0)
									{
										String productCode = StringUtils.EMPTY;
										try
										{
											for (final VideoProductCarouselElementModel productObj : videoProComponentModel.getItems())
											{
												try
												{

													final VideoProductCarElementWsDTO videoProCarouselElementWsDTO = new VideoProductCarElementWsDTO();
													if (null != productObj && null != productObj.getProductCode()
															&& null != productObj.getProductCode().getCode())
													{
														productCode = productObj.getProductCode().getCode();
														final BuyBoxData buyboxdata = buyBoxFacade
																.buyboxPrice(productObj.getProductCode().getCode());
														String productUnitPrice = StringUtils.EMPTY;
														String productPrice = StringUtils.EMPTY;

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

														videoProDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
														videoProDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
														if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
														{
															videoProDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
															videoProMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
														}
														videoProDiscountPriceWsDTO.setCurrencySymbol("₹");
														videoProMRPPriceWsDTO.setCurrencySymbol("₹");
														videoProMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
														videoProMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));
														if (productObj.getProductCode() != null
																&& productObj.getProductCode().getCode() != null)
														{
															videoProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
														}
														else
														{
															videoProCarouselElementWsDTO.setPrdId(StringUtils.EMPTY);
														}
														videoProCarouselElementWsDTO.setMrpPrice(videoProMRPPriceWsDTO);
														videoProCarouselElementWsDTO.setDiscountedPrice(videoProDiscountPriceWsDTO);
														videoProCarouselElementWsDTO.setTitle(null != productObj.getProductCode()
																? productObj.getProductCode().getName() : StringUtils.EMPTY);
														if (null != productModelUrlResolver)
														{
															videoProCarouselElementWsDTO.setWebURL(
																	siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
														}
														else
														{
															videoProCarouselElementWsDTO
																	.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
														}
														if (productObj.getProductCode() != null
																&& productObj.getProductCode().getPicture() != null
																&& productObj.getProductCode().getPicture().getURL() != null)
														{
															videoProCarouselElementWsDTO
																	.setImageURL(productObj.getProductCode().getPicture().getURL());
														}
														else
														{
															videoProCarouselElementWsDTO.setImageURL(StringUtils.EMPTY);
														}
													}
													videoProCarouselList.add(videoProCarouselElementWsDTO);
												}
												catch (final EtailNonBusinessExceptions e)
												{
													LOG.error(VideoProductCarouselError + productCode, e);
													continue;
												}
												catch (final Exception e)
												{
													LOG.error(VideoProductCarouselError + productCode, e);
													continue;
												}
											}
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(VideoProductCarouselError + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(VideoProductCarouselError + productCode, e);
											continue;
										}
									}
									videoProductCarouselWsDTO.setBtnText(null != videoProComponentModel.getBtnText()
											? videoProComponentModel.getBtnText() : StringUtils.EMPTY);
									if (null != videoProComponentModel.getImageURL()
											&& null != videoProComponentModel.getImageURL().getURL())
									{
										videoProductCarouselWsDTO.setImageURL(videoProComponentModel.getImageURL().getURL());
									}
									else
									{
										videoProductCarouselWsDTO.setImageURL(StringUtils.EMPTY);
									}
									videoProductCarouselWsDTO.setItems(videoProCarouselList);
									videoProductCarouselWsDTO.setType("Video Product Carousel Component");
									uiCompPageElementWsDTO.setComponentName("videoProductCarouselComponent");
									videoProductCarouselWsDTO.setComponentId(
											null != videoProComponentModel.getUid() ? videoProComponentModel.getUid() : StringUtils.EMPTY);
									videoProductCarouselWsDTO.setDescription(null != videoProComponentModel.getDescription()
											? videoProComponentModel.getDescription() : StringUtils.EMPTY);
									videoProductCarouselWsDTO.setTitle(null != videoProComponentModel.getTitle()
											? videoProComponentModel.getTitle() : StringUtils.EMPTY);
									videoProductCarouselWsDTO.setWebURL(null != videoProComponentModel.getWebURL()
											? videoProComponentModel.getWebURL() : StringUtils.EMPTY);
									videoProductCarouselWsDTO.setVideoURL(null != videoProComponentModel.getVideoURL()
											? videoProComponentModel.getVideoURL() : StringUtils.EMPTY);
									if (videoProComponentModel.getBrandLogo() != null
											&& videoProComponentModel.getBrandLogo().getURL() != null)
									{
										videoProductCarouselWsDTO.setBrandLogo(videoProComponentModel.getBrandLogo().getURL());
									}
									else
									{
										videoProductCarouselWsDTO.setBrandLogo(StringUtils.EMPTY);
									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									videoProductCarouselWsDTO.setComponentId(
											null != videoProComponentModel.getUid() ? videoProComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(vpcError + videoProComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									videoProductCarouselWsDTO.setComponentId(
											null != videoProComponentModel.getUid() ? videoProComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(vpcError + videoProComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setVideoProductCarouselComponent(videoProductCarouselWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof ThemeOffersComponentModel)
						{
							final ThemeOffersComponentModel themeOffersComponentModel = (ThemeOffersComponentModel) abstractCMSComponentModel;
							if (null != themeOffersComponentModel.getVisible() && themeOffersComponentModel.getVisible().booleanValue())
							{

								final ThemeOffersWsDTO themeOffersWsDTO = new ThemeOffersWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								final List<ThemeOffersElementWsDTO> themeOffersElementList = new ArrayList<ThemeOffersElementWsDTO>();
								final List<ThemeOffersCompOfferWsDTO> themeOffersCompOfferList = new ArrayList<ThemeOffersCompOfferWsDTO>();

								try
								{
									if (null != themeOffersComponentModel.getOffers() && themeOffersComponentModel.getOffers().size() > 0)
									{
										for (final ThemeOffersCompOfferElementModel themeOffersElementModel : themeOffersComponentModel
												.getOffers())
										{
											final ThemeOffersCompOfferWsDTO themeOffersCompOfferWsDTO = new ThemeOffersCompOfferWsDTO();
											themeOffersCompOfferWsDTO.setTitle(null != themeOffersElementModel.getTitle()
													? themeOffersElementModel.getTitle() : StringUtils.EMPTY);
											themeOffersCompOfferWsDTO.setDescription(null != themeOffersElementModel.getDescription()
													? themeOffersElementModel.getDescription() : StringUtils.EMPTY);
											if (null != themeOffersElementModel.getImageURL()
													&& null != themeOffersElementModel.getImageURL().getURL())
											{
												themeOffersCompOfferWsDTO.setImageURL(themeOffersElementModel.getImageURL().getURL());
											}
											else
											{
												themeOffersCompOfferWsDTO.setImageURL(StringUtils.EMPTY);
											}
											themeOffersCompOfferWsDTO.setWebURL(null != themeOffersElementModel.getWebURL()
													? themeOffersElementModel.getWebURL() : StringUtils.EMPTY);

											themeOffersCompOfferList.add(themeOffersCompOfferWsDTO);
										}
									}
									if (null != themeOffersComponentModel.getItems() && themeOffersComponentModel.getItems().size() > 0)
									{
										String productCode = StringUtils.EMPTY;
										try
										{
											for (final ThemeOffersItemsElementModel productObj : themeOffersComponentModel.getItems())
											{
												try
												{
													final ThemeOffersElementWsDTO themeOffersElementWsDTO = new ThemeOffersElementWsDTO();

													if (null != productObj && null != productObj.getProductCode()
															&& null != productObj.getProductCode().getCode())
													{
														productCode = productObj.getProductCode().getCode();
														final BuyBoxData buyboxdata = buyBoxFacade
																.buyboxPrice(productObj.getProductCode().getCode());
														String productUnitPrice = StringUtils.EMPTY;
														String productPrice = StringUtils.EMPTY;

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

														thDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
														thDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
														if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
														{
															thDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
															thMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
														}
														thDiscountPriceWsDTO.setCurrencySymbol("₹");
														thMrpPriceWsDTO.setCurrencySymbol("₹");
														thMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
														thMrpPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));
														themeOffersElementWsDTO.setPrdId(productObj.getProductCode().getCode());
														themeOffersElementWsDTO.setMrpPrice(thMrpPriceWsDTO);
														themeOffersElementWsDTO.setDiscountedPrice(thDiscountPriceWsDTO);
														themeOffersElementWsDTO.setTitle(null != productObj.getProductCode()
																? productObj.getProductCode().getName() : StringUtils.EMPTY);
														if (null != productModelUrlResolver)
														{
															themeOffersElementWsDTO.setWebURL(
																	siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
														}
														else
														{
															themeOffersElementWsDTO
																	.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
														}
														if (productObj.getProductCode() != null
																&& productObj.getProductCode().getPicture() != null
																&& productObj.getProductCode().getPicture().getURL() != null)
														{
															themeOffersElementWsDTO
																	.setImageURL(productObj.getProductCode().getPicture().getURL());
														}
														else
														{
															themeOffersElementWsDTO.setImageURL(StringUtils.EMPTY);
														}
													}
													themeOffersElementList.add(themeOffersElementWsDTO);

												}
												catch (final EtailNonBusinessExceptions e)
												{
													LOG.error(themeOffersError + productCode, e);
													continue;
												}
												catch (final Exception e)
												{
													LOG.error(themeOffersError + productCode, e);
													continue;
												}
											}
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(themeOffersError + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(themeOffersError + productCode, e);
											continue;
										}
									}
									themeOffersWsDTO.setBackgroundHexCode(null != themeOffersComponentModel.getBackgroundHexCode()
											? themeOffersComponentModel.getBackgroundHexCode() : StringUtils.EMPTY);
									if (themeOffersComponentModel.getBackgroundImageURL() != null
											&& themeOffersComponentModel.getBackgroundImageURL().getURL() != null)
									{
										themeOffersWsDTO.setBackgroundImageURL(themeOffersComponentModel.getBackgroundImageURL().getURL());
									}
									else
									{
										themeOffersWsDTO.setBackgroundImageURL(StringUtils.EMPTY);
									}
									themeOffersWsDTO.setBtnText(null != themeOffersComponentModel.getBtnText()
											? themeOffersComponentModel.getBtnText() : StringUtils.EMPTY);
									themeOffersWsDTO.setItems(themeOffersElementList);
									themeOffersWsDTO.setOffers(themeOffersCompOfferList);
									themeOffersWsDTO.setTitle(null != themeOffersComponentModel.getTitle()
											? themeOffersComponentModel.getTitle() : StringUtils.EMPTY);
									themeOffersWsDTO.setType("Theme Offers Component");
									uiCompPageElementWsDTO.setComponentName("themeOffersComponent");
									themeOffersWsDTO.setComponentId(null != themeOffersComponentModel.getUid()
											? themeOffersComponentModel.getUid() : StringUtils.EMPTY);
									themeOffersWsDTO.setWebURL(null != themeOffersComponentModel.getWebURL()
											? themeOffersComponentModel.getWebURL() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									themeOffersWsDTO.setComponentId(null != themeOffersComponentModel.getUid()
											? themeOffersComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(toError + themeOffersComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									themeOffersWsDTO.setComponentId(null != themeOffersComponentModel.getUid()
											? themeOffersComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(toError + themeOffersComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setThemeOffersComponent(themeOffersWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof ThemeProductWidgetComponentModel)
						{

							final ThemeProductWidgetComponentModel themeProductWidgetComponentModel = (ThemeProductWidgetComponentModel) abstractCMSComponentModel;
							if (null != themeProductWidgetComponentModel.getVisible()
									&& themeProductWidgetComponentModel.getVisible().booleanValue())
							{
								final List<ThemeProWidElementWsDTO> themeProWidElementList = new ArrayList<ThemeProWidElementWsDTO>();
								final ThemeProductWidgetWsDTO themeProductWidgetWsDTO = new ThemeProductWidgetWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									if (null != themeProductWidgetComponentModel.getItems()
											&& themeProductWidgetComponentModel.getItems().size() > 0)
									{
										String productCode = StringUtils.EMPTY;
										try
										{
											for (final ThemeProductWidgetElementModel productObj : themeProductWidgetComponentModel
													.getItems())
											{
												try
												{
													final ThemeProWidElementWsDTO themeProWidElementWsDTO = new ThemeProWidElementWsDTO();

													if (null != productObj && null != productObj.getProductCode()
															&& null != productObj.getProductCode().getCode())
													{
														productCode = productObj.getProductCode().getCode();
														final BuyBoxData buyboxdata = buyBoxFacade
																.buyboxPrice(productObj.getProductCode().getCode());
														String productUnitPrice = StringUtils.EMPTY;
														String productPrice = StringUtils.EMPTY;

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

														thDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
														thDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
														if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
														{
															thDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
															thMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
														}
														thDiscountPriceWsDTO.setCurrencySymbol("₹");
														thMrpPriceWsDTO.setCurrencySymbol("₹");
														thMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
														thMrpPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));
														themeProWidElementWsDTO.setPrdId(productObj.getProductCode().getCode());
														themeProWidElementWsDTO.setMrpPrice(thMrpPriceWsDTO);
														themeProWidElementWsDTO.setDiscountedPrice(thDiscountPriceWsDTO);
														themeProWidElementWsDTO.setTitle(null != productObj.getProductCode().getName()
																? productObj.getProductCode().getName() : StringUtils.EMPTY);
														if (null != productModelUrlResolver)
														{
															themeProWidElementWsDTO.setWebURL(
																	siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
														}
														else
														{
															themeProWidElementWsDTO
																	.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
														}
														if (productObj.getProductCode() != null
																&& productObj.getProductCode().getPicture() != null
																&& productObj.getProductCode().getPicture().getURL() != null)
														{
															themeProWidElementWsDTO
																	.setImageURL(productObj.getProductCode().getPicture().getURL());
														}
														else
														{
															themeProWidElementWsDTO.setImageURL(StringUtils.EMPTY);
														}
													}
													themeProWidElementList.add(themeProWidElementWsDTO);
												}
												catch (final EtailNonBusinessExceptions e)
												{
													LOG.error(themeProductWidgetError + productCode, e);
													continue;
												}
												catch (final Exception e)
												{
													LOG.error(themeProductWidgetError + productCode, e);
													continue;
												}
											}
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(themeProductWidgetError + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(themeProductWidgetError + productCode, e);
											continue;
										}
									}
									if (themeProductWidgetComponentModel.getImageURL() != null
											&& themeProductWidgetComponentModel.getImageURL().getURL() != null)
									{
										themeProductWidgetWsDTO.setImageURL(themeProductWidgetComponentModel.getImageURL().getURL());
									}
									else
									{
										themeProductWidgetWsDTO.setImageURL(StringUtils.EMPTY);
									}
									if (themeProductWidgetComponentModel.getBrandLogo() != null
											&& themeProductWidgetComponentModel.getBrandLogo().getURL() != null)
									{
										themeProductWidgetWsDTO.setBrandLogo(themeProductWidgetComponentModel.getBrandLogo().getURL());
									}
									else
									{
										themeProductWidgetWsDTO.setBrandLogo(StringUtils.EMPTY);
									}
									themeProductWidgetWsDTO.setBtnText(null != themeProductWidgetComponentModel.getBtnText()
											? themeProductWidgetComponentModel.getBtnText() : StringUtils.EMPTY);
									themeProductWidgetWsDTO.setItems(themeProWidElementList);
									themeProductWidgetWsDTO.setTitle(null != themeProductWidgetComponentModel.getTitle()
											? themeProductWidgetComponentModel.getTitle() : StringUtils.EMPTY);
									themeProductWidgetWsDTO.setType("Multi Click Component");
									uiCompPageElementWsDTO.setComponentName("multiClickComponent");
									themeProductWidgetWsDTO.setComponentId(null != themeProductWidgetComponentModel.getUid()
											? themeProductWidgetComponentModel.getUid() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									themeProductWidgetWsDTO.setComponentId(null != themeProductWidgetComponentModel.getUid()
											? themeProductWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(tpwError + themeProductWidgetComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									themeProductWidgetWsDTO.setComponentId(null != themeProductWidgetComponentModel.getUid()
											? themeProductWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(tpwError + themeProductWidgetComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setMultiClickComponent(themeProductWidgetWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						/*
						 * if (abstractCMSComponentModel instanceof ProductCapsulesComponentModel) { final
						 * ProductCapsulesComponentModel productCapsulesComponentModel = (ProductCapsulesComponentModel)
						 * abstractCMSComponentModel; final UICompPageElementWsDTO uiCompPageElementWsDTO = new
						 * UICompPageElementWsDTO(); final List<ProductCapsulesElementWsDTO> productCapsulesElementList = new
						 * ArrayList<ProductCapsulesElementWsDTO>(); final ProductCapsulesWsDTO productCapsulesWsDTO = new
						 * ProductCapsulesWsDTO();
						 *
						 * if (null != productCapsulesComponentModel.getItems() &&
						 * productCapsulesComponentModel.getItems().size() > 0) { for (final ProductCapsulesElementModel
						 * productCapsulesElementModel : productCapsulesComponentModel .getItems()) { final
						 * ProductCapsulesElementWsDTO productCapsulesElementWsDTO = new ProductCapsulesElementWsDTO(); if
						 * (null != productCapsulesElementModel.getImageURL() && null !=
						 * productCapsulesElementModel.getImageURL().getURL()) {
						 * productCapsulesElementWsDTO.setImageURL(productCapsulesElementModel.getImageURL().getURL()); } else
						 * { productCapsulesElementWsDTO.setImageURL(""); } productCapsulesElementWsDTO.setWebURL( null !=
						 * productCapsulesElementModel.getWebURL() ? productCapsulesElementModel.getWebURL() : "");
						 * productCapsulesElementList.add(productCapsulesElementWsDTO); } } productCapsulesWsDTO.setBtnText(
						 * null != productCapsulesComponentModel.getBtnText() ? productCapsulesComponentModel.getBtnText() :
						 * ""); productCapsulesWsDTO.setDescription(null != productCapsulesComponentModel.getDescription() ?
						 * productCapsulesComponentModel.getDescription() : "");
						 * productCapsulesWsDTO.setItems(productCapsulesElementList); productCapsulesWsDTO.setTitle( null !=
						 * productCapsulesComponentModel.getTitle() ? productCapsulesComponentModel.getTitle() : "");
						 * productCapsulesWsDTO.setType( null != productCapsulesComponentModel.getName() ?
						 * productCapsulesComponentModel.getName() : ""); productCapsulesWsDTO.setWebURL( null !=
						 * productCapsulesComponentModel.getWebURL() ? productCapsulesComponentModel.getWebURL() : "");
						 * uiCompPageElementWsDTO.setProductCapsules(productCapsulesWsDTO);
						 * genericUICompPageWsDTO.add(uiCompPageElementWsDTO); }
						 */

						if (abstractCMSComponentModel instanceof BannerSeparatorComponentModel)
						{

							final BannerSeparatorComponentModel bannerSeparatorComponentModel = (BannerSeparatorComponentModel) abstractCMSComponentModel;
							if (null != bannerSeparatorComponentModel.getVisible()
									&& bannerSeparatorComponentModel.getVisible().booleanValue())
							{
								final BannerSeparatorWsDTO bannerSeperatorWsDTO = new BannerSeparatorWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

								try
								{
									if (null != bannerSeparatorComponentModel.getIconImageURL()
											&& null != bannerSeparatorComponentModel.getIconImageURL().getURL())
									{
										bannerSeperatorWsDTO.setIconImageURL(bannerSeparatorComponentModel.getIconImageURL().getURL());
									}
									else
									{
										bannerSeperatorWsDTO.setIconImageURL(StringUtils.EMPTY);
									}
									bannerSeperatorWsDTO.setEndHexCode(null != bannerSeparatorComponentModel.getEndHexCode()
											? bannerSeparatorComponentModel.getEndHexCode() : StringUtils.EMPTY);
									bannerSeperatorWsDTO.setStartHexCode(null != bannerSeparatorComponentModel.getStartHexCode()
											? bannerSeparatorComponentModel.getStartHexCode() : StringUtils.EMPTY);
									bannerSeperatorWsDTO.setDescription(null != bannerSeparatorComponentModel.getDescription()
											? bannerSeparatorComponentModel.getDescription() : StringUtils.EMPTY);
									bannerSeperatorWsDTO.setTitle(null != bannerSeparatorComponentModel.getTitle()
											? bannerSeparatorComponentModel.getTitle() : StringUtils.EMPTY);
									bannerSeperatorWsDTO.setWebURL(null != bannerSeparatorComponentModel.getWebURL()
											? bannerSeparatorComponentModel.getWebURL() : StringUtils.EMPTY);
									bannerSeperatorWsDTO.setType("Banner Separator Component");
									uiCompPageElementWsDTO.setComponentName("bannerSeparatorComponent");

									bannerSeperatorWsDTO.setComponentId(null != bannerSeparatorComponentModel.getUid()
											? bannerSeparatorComponentModel.getUid() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									bannerSeperatorWsDTO.setComponentId(null != bannerSeparatorComponentModel.getUid()
											? bannerSeparatorComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(BannerSeparatorError + bannerSeparatorComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									bannerSeperatorWsDTO.setComponentId(null != bannerSeparatorComponentModel.getUid()
											? bannerSeparatorComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(BannerSeparatorError + bannerSeparatorComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setBannerSeparatorComponent(bannerSeperatorWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof AutomatedBrandProductCarouselComponentModel)
						{
							final AutomatedBrandProductCarouselComponentModel automatedBrandProCarCompModel = (AutomatedBrandProductCarouselComponentModel) abstractCMSComponentModel;
							if (null != automatedBrandProCarCompModel.getVisible()
									&& automatedBrandProCarCompModel.getVisible().booleanValue())
							{
								final List<AutomatedBrandProCarEleWsDTO> automatedBrandProCarEleList = new ArrayList<AutomatedBrandProCarEleWsDTO>();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								final AutomatedBrandProCarWsDTO automatedBrandProCarWsDTO = new AutomatedBrandProCarWsDTO();
								try
								{
									if (automatedBrandProCarCompModel.getItems() != null
											&& automatedBrandProCarCompModel.getItems().size() > 0)
									{
										for (final AutomatedBrandProductCarElementModel productObj : automatedBrandProCarCompModel
												.getItems())
										{
											try
											{

												final AutomatedBrandProCarEleWsDTO automatedBrandProCarEleWsDTO = new AutomatedBrandProCarEleWsDTO();
												String productCode = StringUtils.EMPTY;
												try
												{
													if (null != productObj && null != productObj.getProductCode()
															&& null != productObj.getProductCode().getCode())
													{
														productCode = productObj.getProductCode().getCode();
														final BuyBoxData buyboxdata = buyBoxFacade
																.buyboxPrice(productObj.getProductCode().getCode());
														String productUnitPrice = StringUtils.EMPTY;
														String productPrice = StringUtils.EMPTY;

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

														autoDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
														autoDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
														if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
														{
															autoDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
															autoMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
														}
														autoDiscountPriceWsDTO.setCurrencySymbol("₹");
														autoMrpPriceWsDTO.setCurrencySymbol("₹");
														autoMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
														autoMrpPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));
														automatedBrandProCarEleWsDTO.setPrdId(productObj.getProductCode().getCode());
														automatedBrandProCarEleWsDTO.setMrpPrice(autoMrpPriceWsDTO);
														automatedBrandProCarEleWsDTO.setDiscountedPrice(autoDiscountPriceWsDTO);
														automatedBrandProCarEleWsDTO.setTitle(null != productObj.getProductCode()
																? productObj.getProductCode().getName() : StringUtils.EMPTY);
														if (null != productModelUrlResolver)
														{
															automatedBrandProCarEleWsDTO.setWebURL(
																	siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
														}
														else
														{
															automatedBrandProCarEleWsDTO
																	.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
														}
														if (productObj.getProductCode() != null
																&& productObj.getProductCode().getPicture() != null
																&& productObj.getProductCode().getPicture().getURL() != null)
														{
															automatedBrandProCarEleWsDTO
																	.setImageURL(productObj.getProductCode().getPicture().getURL());
														}
														else
														{
															automatedBrandProCarEleWsDTO.setImageURL(StringUtils.EMPTY);
														}
													}
												}
												catch (final EtailNonBusinessExceptions e)
												{
													LOG.error(AutomatedBrandProCarError + productCode, e);
													continue;
												}
												catch (final Exception e)
												{
													LOG.error(AutomatedBrandProCarError + productCode, e);
													continue;
												}
												automatedBrandProCarEleList.add(automatedBrandProCarEleWsDTO);
											}
											catch (final EtailNonBusinessExceptions e)
											{
												automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
														? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
												LOG.error(abpcError + automatedBrandProCarCompModel.getUid(), e);
												continue;
											}
											catch (final Exception e)
											{
												automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
														? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
												LOG.error(abpcError + automatedBrandProCarCompModel.getUid(), e);
												continue;
											}
										}
									}
									if (null != automatedBrandProCarCompModel.getBrandLogo()
											&& null != automatedBrandProCarCompModel.getBrandLogo().getURL())
									{
										automatedBrandProCarWsDTO.setBrandLogo(automatedBrandProCarCompModel.getBrandLogo().getURL());
									}
									else
									{
										automatedBrandProCarWsDTO.setBrandLogo(StringUtils.EMPTY);
									}
									automatedBrandProCarWsDTO.setBtnText(null != automatedBrandProCarCompModel.getBtnText()
											? automatedBrandProCarCompModel.getBtnText() : StringUtils.EMPTY);
									automatedBrandProCarWsDTO.setDescription(null != automatedBrandProCarCompModel.getDescription()
											? automatedBrandProCarCompModel.getDescription() : StringUtils.EMPTY);
									if (automatedBrandProCarCompModel.getImageURL() != null
											&& automatedBrandProCarCompModel.getImageURL().getURL() != null)
									{
										automatedBrandProCarWsDTO.setImageURL(automatedBrandProCarCompModel.getImageURL().getURL());
									}
									else
									{
										automatedBrandProCarWsDTO.setImageURL(StringUtils.EMPTY);
									}
									automatedBrandProCarWsDTO.setItems(automatedBrandProCarEleList);
									automatedBrandProCarWsDTO.setType("Automated Banner Product Carousel Component");
									uiCompPageElementWsDTO.setComponentName("automatedBannerProductCarouselComponent");
									automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
											? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
									automatedBrandProCarWsDTO.setWebURL(null != automatedBrandProCarCompModel.getWebURL()
											? automatedBrandProCarCompModel.getWebURL() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
											? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(abpcError + automatedBrandProCarCompModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
											? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(abpcError + automatedBrandProCarCompModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setAutomatedBannerProductCarouselComponent(automatedBrandProCarWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof CuratedListingStripComponentModel)
						{
							final CuratedListingStripComponentModel curatedListStripCompModel = (CuratedListingStripComponentModel) abstractCMSComponentModel;
							if (null != curatedListStripCompModel.getVisible() && curatedListStripCompModel.getVisible().booleanValue())
							{
								final CuratedListingStripWsDTO curatedListingStripWsDTO = new CuratedListingStripWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									curatedListingStripWsDTO.setStartHexCode(null != curatedListStripCompModel.getStartHexCode()
											? curatedListStripCompModel.getStartHexCode() : StringUtils.EMPTY);
									curatedListingStripWsDTO.setTitle(null != curatedListStripCompModel.getTitle()
											? curatedListStripCompModel.getTitle() : StringUtils.EMPTY);
									curatedListingStripWsDTO.setType("Curated Listing Strip Component");
									uiCompPageElementWsDTO.setComponentName("curatedListingStripComponent");
									curatedListingStripWsDTO.setComponentId(null != curatedListStripCompModel.getUid()
											? curatedListStripCompModel.getUid() : StringUtils.EMPTY);
									curatedListingStripWsDTO.setWebURL(null != curatedListStripCompModel.getWebURL()
											? curatedListStripCompModel.getWebURL() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									curatedListingStripWsDTO.setComponentId(null != curatedListStripCompModel.getUid()
											? curatedListStripCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(CuratedListingStripError + curatedListStripCompModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									curatedListingStripWsDTO.setComponentId(null != curatedListStripCompModel.getUid()
											? curatedListStripCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(CuratedListingStripError + curatedListStripCompModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setCuratedListingStripComponent(curatedListingStripWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof MonoBLPBannerComponentModel)
						{
							final MonoBLPBannerComponentModel monoBLPBannerComponentModel = (MonoBLPBannerComponentModel) abstractCMSComponentModel;
							if (null != monoBLPBannerComponentModel.getVisible()
									&& monoBLPBannerComponentModel.getVisible().booleanValue())
							{
								final List<MonoBLPBannerElementWsDTO> monoBLPBannerElementList = new ArrayList<MonoBLPBannerElementWsDTO>();
								final MonoBLPBannerWsDTO moBannerWsDTO = new MonoBLPBannerWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

								try
								{
									if (null != monoBLPBannerComponentModel.getItems()
											&& monoBLPBannerComponentModel.getItems().size() > 0)
									{
										for (final MonoBLPBannerElementModel monoBLPBannerElementModel : monoBLPBannerComponentModel
												.getItems())
										{
											final MonoBLPBannerElementWsDTO monoBLPBannerElementWsDTO = new MonoBLPBannerElementWsDTO();
											monoBLPBannerElementWsDTO.setBtnText(null != monoBLPBannerElementModel.getBtnText()
													? monoBLPBannerElementModel.getBtnText() : StringUtils.EMPTY);
											monoBLPBannerElementWsDTO.setHexCode(null != monoBLPBannerElementModel.getHexCode()
													? monoBLPBannerElementModel.getHexCode() : StringUtils.EMPTY);
											monoBLPBannerElementWsDTO.setTitle(null != monoBLPBannerElementModel.getTitle()
													? monoBLPBannerElementModel.getTitle() : StringUtils.EMPTY);
											monoBLPBannerElementWsDTO.setWebURL(null != monoBLPBannerElementModel.getWebURL()
													? monoBLPBannerElementModel.getWebURL() : StringUtils.EMPTY);
											if (monoBLPBannerElementModel.getImageURL() != null
													&& monoBLPBannerElementModel.getImageURL().getURL() != null)
											{
												monoBLPBannerElementWsDTO.setImageURL(monoBLPBannerElementModel.getImageURL().getURL());
											}
											else
											{
												monoBLPBannerElementWsDTO.setImageURL(StringUtils.EMPTY);
											}
											monoBLPBannerElementList.add(monoBLPBannerElementWsDTO);
										}
									}
									moBannerWsDTO.setType("Single Banner Component");
									uiCompPageElementWsDTO.setComponentName("singleBannerComponent");
									moBannerWsDTO.setComponentId(null != monoBLPBannerComponentModel.getUid()
											? monoBLPBannerComponentModel.getUid() : StringUtils.EMPTY);
									moBannerWsDTO.setItems(monoBLPBannerElementList);
									moBannerWsDTO.setTitle(null != monoBLPBannerComponentModel.getTitle()
											? monoBLPBannerComponentModel.getTitle() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									moBannerWsDTO.setComponentId(null != monoBLPBannerComponentModel.getUid()
											? monoBLPBannerComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(MonoBLPBannerError + monoBLPBannerComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									moBannerWsDTO.setComponentId(null != monoBLPBannerComponentModel.getUid()
											? monoBLPBannerComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(MonoBLPBannerError + monoBLPBannerComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setSingleBannerComponent(moBannerWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof SubBrandBannerBLPComponentModel)
						{
							final SubBrandBannerBLPComponentModel subBrandBLPBannerCompModel = (SubBrandBannerBLPComponentModel) abstractCMSComponentModel;
							if (null != subBrandBLPBannerCompModel.getVisible()
									&& subBrandBLPBannerCompModel.getVisible().booleanValue())
							{
								final List<SubBrandBannerBLPElementWsDTO> subBrandBannerBLPEleList = new ArrayList<SubBrandBannerBLPElementWsDTO>();
								final SubBrandBannerBLPWsDTO subBrandBannerBLPWsDTO = new SubBrandBannerBLPWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									if (null != subBrandBLPBannerCompModel.getItems() && subBrandBLPBannerCompModel.getItems().size() > 0)
									{
										for (final SubBrandBannerBLPElementModel subBannerBLPElementModel : subBrandBLPBannerCompModel
												.getItems())
										{
											final SubBrandBannerBLPElementWsDTO subBannerBLPEleWsDTO = new SubBrandBannerBLPElementWsDTO();
											subBannerBLPEleWsDTO.setWebURL(null != subBannerBLPElementModel.getWebURL()
													? subBannerBLPElementModel.getWebURL() : StringUtils.EMPTY);
											if (subBannerBLPElementModel.getImageURL() != null
													&& subBannerBLPElementModel.getImageURL().getURL() != null)
											{
												subBannerBLPEleWsDTO.setImageURL(subBannerBLPElementModel.getImageURL().getURL());
											}
											else
											{
												subBannerBLPEleWsDTO.setImageURL(StringUtils.EMPTY);
											}
											if (subBannerBLPElementModel.getBrandLogo() != null
													&& subBannerBLPElementModel.getBrandLogo().getURL() != null)
											{
												subBannerBLPEleWsDTO.setBrandLogo(subBannerBLPElementModel.getBrandLogo().getURL());
											}
											else
											{
												subBannerBLPEleWsDTO.setBrandLogo(StringUtils.EMPTY);
											}
											subBrandBannerBLPEleList.add(subBannerBLPEleWsDTO);
										}
									}
									subBrandBannerBLPWsDTO.setType("Sub Brands Banner Component");
									uiCompPageElementWsDTO.setComponentName("subBrandsBannerComponent");
									subBrandBannerBLPWsDTO.setComponentId(null != subBrandBLPBannerCompModel.getUid()
											? subBrandBLPBannerCompModel.getUid() : StringUtils.EMPTY);
									subBrandBannerBLPWsDTO.setItems(subBrandBannerBLPEleList);
									subBrandBannerBLPWsDTO.setTitle(null != subBrandBLPBannerCompModel.getTitle()
											? subBrandBLPBannerCompModel.getTitle() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									subBrandBannerBLPWsDTO.setComponentId(null != subBrandBLPBannerCompModel.getUid()
											? subBrandBLPBannerCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(SubBrandBLPBannerError + subBrandBLPBannerCompModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									subBrandBannerBLPWsDTO.setComponentId(null != subBrandBLPBannerCompModel.getUid()
											? subBrandBLPBannerCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(SubBrandBLPBannerError + subBrandBLPBannerCompModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setSubBrandsBannerComponent(subBrandBannerBLPWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof TopCategoriesWidgetComponentModel)
						{
							final TopCategoriesWidgetComponentModel topCategoriesWidgetComponentModel = (TopCategoriesWidgetComponentModel) abstractCMSComponentModel;
							if (null != topCategoriesWidgetComponentModel.getVisible()
									&& topCategoriesWidgetComponentModel.getVisible().booleanValue())
							{

								final List<TopCategoriesWidgetElementWsDTO> topCategoriesWidgetElementList = new ArrayList<TopCategoriesWidgetElementWsDTO>();
								final TopCategoriesWidgetWsDTO topCategoriesWidgetWsDTO = new TopCategoriesWidgetWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

								try
								{
									if (null != topCategoriesWidgetComponentModel.getItems()
											&& topCategoriesWidgetComponentModel.getItems().size() > 0)
									{
										for (final TopCategoriesWidgetElementModel topCategoriesWidgetElementModel : topCategoriesWidgetComponentModel
												.getItems())
										{
											final TopCategoriesWidgetElementWsDTO topCategoriesWidgetElementWsDTO = new TopCategoriesWidgetElementWsDTO();
											topCategoriesWidgetElementWsDTO.setWebURL(null != topCategoriesWidgetElementModel.getWebURL()
													? topCategoriesWidgetElementModel.getWebURL() : StringUtils.EMPTY);
											if (topCategoriesWidgetElementModel.getImageURL() != null
													&& topCategoriesWidgetElementModel.getImageURL().getURL() != null)
											{
												topCategoriesWidgetElementWsDTO
														.setImageURL(topCategoriesWidgetElementModel.getImageURL().getURL());
											}
											else
											{
												topCategoriesWidgetElementWsDTO.setImageURL(StringUtils.EMPTY);
											}
											topCategoriesWidgetElementWsDTO.setTitle(null != topCategoriesWidgetElementModel.getTitle()
													? topCategoriesWidgetElementModel.getTitle() : StringUtils.EMPTY);
											topCategoriesWidgetElementList.add(topCategoriesWidgetElementWsDTO);
										}
									}
									topCategoriesWidgetWsDTO.setType("Top Categories Component");
									uiCompPageElementWsDTO.setComponentName("topCategoriesComponent");
									topCategoriesWidgetWsDTO.setComponentId(null != topCategoriesWidgetComponentModel.getUid()
											? topCategoriesWidgetComponentModel.getUid() : StringUtils.EMPTY);
									topCategoriesWidgetWsDTO.setItems(topCategoriesWidgetElementList);
									topCategoriesWidgetWsDTO.setTitle(null != topCategoriesWidgetComponentModel.getTitle()
											? topCategoriesWidgetComponentModel.getTitle() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									topCategoriesWidgetWsDTO.setComponentId(null != topCategoriesWidgetComponentModel.getUid()
											? topCategoriesWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(TopCategoriesWidgetEror + topCategoriesWidgetComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									topCategoriesWidgetWsDTO.setComponentId(null != topCategoriesWidgetComponentModel.getUid()
											? topCategoriesWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(TopCategoriesWidgetEror + topCategoriesWidgetComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setTopCategoriesComponent(topCategoriesWidgetWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof CuratedProductsWidgetComponentModel)
						{
							final CuratedProductsWidgetComponentModel curatedProWidgetCompModel = (CuratedProductsWidgetComponentModel) abstractCMSComponentModel;

							if (null != curatedProWidgetCompModel.getVisible() && curatedProWidgetCompModel.getVisible().booleanValue())
							{
								final List<CuratedProWidgetElementWsDTO> curatedProWidgetElementList = new ArrayList<CuratedProWidgetElementWsDTO>();
								final CuratedProductsWidgetWsDTO curatedProductsWidgetWsDTO = new CuratedProductsWidgetWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

								try
								{
									if (null != curatedProWidgetCompModel.getItems() && curatedProWidgetCompModel.getItems().size() > 0)
									{
										String productCode = StringUtils.EMPTY;
										try
										{
											for (final CuratedProductsWidgetElementModel productObj : curatedProWidgetCompModel.getItems())
											{
												try
												{
													final CuratedProWidgetElementWsDTO curatedProWidgetElementWsDTO = new CuratedProWidgetElementWsDTO();
													if (null != productObj && null != productObj.getProductCode()
															&& null != productObj.getProductCode().getCode())
													{
														productCode = productObj.getProductCode().getCode();
														final BuyBoxData buyboxdata = buyBoxFacade
																.buyboxPrice(productObj.getProductCode().getCode());
														String productUnitPrice = StringUtils.EMPTY;
														String productPrice = StringUtils.EMPTY;

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

														curatedProWidgetEleDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
														curatedProWidgetEleDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
														if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
														{
															curatedProWidgetEleMRPPriceWsDTO
																	.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
															curatedProWidgetEleDiscountPriceWsDTO
																	.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
														}
														curatedProWidgetEleDiscountPriceWsDTO.setCurrencySymbol("₹");
														curatedProWidgetEleMRPPriceWsDTO.setCurrencySymbol("₹");
														curatedProWidgetEleMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
														curatedProWidgetEleMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));

														curatedProWidgetElementWsDTO.setPrdId(productObj.getProductCode().getCode());
														curatedProWidgetElementWsDTO.setMrpPrice(curatedProWidgetEleMRPPriceWsDTO);
														curatedProWidgetElementWsDTO.setDiscountedPrice(curatedProWidgetEleDiscountPriceWsDTO);
														curatedProWidgetElementWsDTO.setTitle(null != productObj.getProductCode()
																? productObj.getProductCode().getName() : StringUtils.EMPTY);
														curatedProWidgetElementWsDTO.setDescription(null != productObj.getDescription()
																? productObj.getDescription() : StringUtils.EMPTY);
														if (null != productModelUrlResolver)
														{
															curatedProWidgetElementWsDTO.setWebURL(
																	siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
														}
														else
														{
															curatedProWidgetElementWsDTO
																	.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
														}
														if (null != productObj.getProductCode().getPicture()
																&& null != productObj.getProductCode().getPicture().getURL())
														{
															curatedProWidgetElementWsDTO
																	.setImageURL(productObj.getProductCode().getPicture().getURL());
														}
														else
														{
															curatedProWidgetElementWsDTO.setImageURL("");
														}
													}
													curatedProWidgetElementList.add(curatedProWidgetElementWsDTO);
												}
												catch (final EtailNonBusinessExceptions e)
												{
													LOG.error(CuratedProductWidgetEror + productCode, e);
													continue;
												}
												catch (final Exception e)
												{
													LOG.error(CuratedProductWidgetEror + productCode, e);
													continue;
												}
											}
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(CuratedProductWidgetEror + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(CuratedProductWidgetEror + productCode, e);
											continue;
										}
									}

									curatedProductsWidgetWsDTO.setBtnText(null != curatedProWidgetCompModel.getBtnText()
											? curatedProWidgetCompModel.getBtnText() : StringUtils.EMPTY);
									curatedProductsWidgetWsDTO.setItems(curatedProWidgetElementList);
									curatedProductsWidgetWsDTO.setTitle(null != curatedProWidgetCompModel.getTitle()
											? curatedProWidgetCompModel.getTitle() : StringUtils.EMPTY);
									curatedProductsWidgetWsDTO.setType("Curated Products Component");
									uiCompPageElementWsDTO.setComponentName("curatedProductsComponent");
									curatedProductsWidgetWsDTO.setComponentId(null != curatedProWidgetCompModel.getUid()
											? curatedProWidgetCompModel.getUid() : StringUtils.EMPTY);
									curatedProductsWidgetWsDTO.setWebURL(null != curatedProWidgetCompModel.getWebURL()
											? curatedProWidgetCompModel.getWebURL() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									curatedProductsWidgetWsDTO.setComponentId(null != curatedProWidgetCompModel.getUid()
											? curatedProWidgetCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(cpwError + curatedProWidgetCompModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									curatedProductsWidgetWsDTO.setComponentId(null != curatedProWidgetCompModel.getUid()
											? curatedProWidgetCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(cpwError + curatedProWidgetCompModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setCuratedProductsComponent(curatedProductsWidgetWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof SmartFilterWidgetComponentModel)
						{
							final SmartFilterWidgetComponentModel smartFilterWidgetComponentModel = (SmartFilterWidgetComponentModel) abstractCMSComponentModel;
							if (null != smartFilterWidgetComponentModel.getVisible()
									&& smartFilterWidgetComponentModel.getVisible().booleanValue())
							{
								final SmartFilterWidgetWsDTO smartFilterWsDTO = new SmartFilterWidgetWsDTO();
								final List<SmartFilterWidgetElementWsDTO> smartFilterWidgetElementList = new ArrayList<SmartFilterWidgetElementWsDTO>();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									if (null != smartFilterWidgetComponentModel.getItems()
											&& smartFilterWidgetComponentModel.getItems().size() > 0)
									{
										for (final SmartFilterWidgetElementModel smartFilterWidgetElementModel : smartFilterWidgetComponentModel
												.getItems())
										{
											final SmartFilterWidgetElementWsDTO smartFilterWidgetElementWsDTO = new SmartFilterWidgetElementWsDTO();
											smartFilterWidgetElementWsDTO
													.setDescription(null != smartFilterWidgetElementModel.getDescription()
															? smartFilterWidgetElementModel.getDescription() : StringUtils.EMPTY);
											if (smartFilterWidgetElementModel.getImageURL() != null
													&& smartFilterWidgetElementModel.getImageURL().getURL() != null)
											{
												smartFilterWidgetElementWsDTO
														.setImageURL(smartFilterWidgetElementModel.getImageURL().getURL());
											}
											else
											{
												smartFilterWidgetElementWsDTO.setImageURL(StringUtils.EMPTY);
											}
											smartFilterWidgetElementWsDTO.setTitle(null != smartFilterWidgetElementModel.getTitle()
													? smartFilterWidgetElementModel.getTitle() : StringUtils.EMPTY);
											smartFilterWidgetElementWsDTO.setWebURL(null != smartFilterWidgetElementModel.getWebURL()
													? smartFilterWidgetElementModel.getWebURL() : StringUtils.EMPTY);
											smartFilterWidgetElementList.add(smartFilterWidgetElementWsDTO);
										}
									}
									smartFilterWsDTO.setItems(smartFilterWidgetElementList);
									smartFilterWsDTO.setTitle(null != smartFilterWidgetComponentModel.getTitle()
											? smartFilterWidgetComponentModel.getTitle() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									smartFilterWsDTO.setComponentId(null != smartFilterWidgetComponentModel.getUid()
											? smartFilterWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(SmartFilterWidgetError + smartFilterWidgetComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									smartFilterWsDTO.setComponentId(null != smartFilterWidgetComponentModel.getUid()
											? smartFilterWidgetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(SmartFilterWidgetError + smartFilterWidgetComponentModel.getUid(), e);
									continue;
								}
								smartFilterWsDTO.setType("Two by Two Banner Component");
								uiCompPageElementWsDTO.setComponentName("twoByTwoBannerComponent");
								smartFilterWsDTO.setComponentId(null != smartFilterWidgetComponentModel.getUid()
										? smartFilterWidgetComponentModel.getUid() : StringUtils.EMPTY);
								uiCompPageElementWsDTO.setTwoByTwoBannerComponent(smartFilterWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof MSDComponentModel)
						{
							final MSDComponentModel msdComponentModel = (MSDComponentModel) abstractCMSComponentModel;
							if (null != msdComponentModel.getVisible() && msdComponentModel.getVisible().booleanValue())
							{
								final MSDComponentWsDTO msdComponentWsDTO = new MSDComponentWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									msdComponentWsDTO.setDetails(
											null != msdComponentModel.getDetails() ? msdComponentModel.getDetails() : Boolean.FALSE);
									msdComponentWsDTO.setNum_results(null != msdComponentModel.getNum_results()
											? msdComponentModel.getNum_results() : Integer.valueOf(0));
									msdComponentWsDTO.setSubType(
											null != msdComponentModel.getSubType() ? msdComponentModel.getSubType() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									msdComponentWsDTO.setComponentId(
											null != msdComponentModel.getUid() ? msdComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(MsdError + msdComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									msdComponentWsDTO.setComponentId(
											null != msdComponentModel.getUid() ? msdComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(MsdError + msdComponentModel.getUid(), e);
									continue;
								}
								msdComponentWsDTO.setType("MSD Component");
								uiCompPageElementWsDTO.setComponentName("msdComponent");
								msdComponentWsDTO.setComponentId(
										null != msdComponentModel.getUid() ? msdComponentModel.getUid() : StringUtils.EMPTY);
								uiCompPageElementWsDTO.setMsdComponent(msdComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof AdobeTargetComponentModel)
						{
							final AdobeTargetComponentModel adobeTargetComponentModel = (AdobeTargetComponentModel) abstractCMSComponentModel;
							if (null != adobeTargetComponentModel.getVisible() && adobeTargetComponentModel.getVisible().booleanValue())
							{
								final AdobeTargetComponentWsDTO adobeTargetComponentWsDTO = new AdobeTargetComponentWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									adobeTargetComponentWsDTO.setMbox(null != adobeTargetComponentModel.getMbox()
											? adobeTargetComponentModel.getMbox() : StringUtils.EMPTY);
									adobeTargetComponentWsDTO.setType("Adobe Target Component");
									uiCompPageElementWsDTO.setComponentName("adobeTargetComponent");
									adobeTargetComponentWsDTO.setComponentId(null != adobeTargetComponentModel.getUid()
											? adobeTargetComponentModel.getUid() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									adobeTargetComponentWsDTO.setComponentId(null != adobeTargetComponentModel.getUid()
											? adobeTargetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(AdobeTargetError + adobeTargetComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									adobeTargetComponentWsDTO.setComponentId(null != adobeTargetComponentModel.getUid()
											? adobeTargetComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(AdobeTargetError + adobeTargetComponentModel.getUid(), e);
									continue;
								}
								uiCompPageElementWsDTO.setAdobeTargetComponent(adobeTargetComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof BrandsTabAZListComponentModel)
						{
							final BrandsTabAZListComponentModel brandsTabAZListComponentModel = (BrandsTabAZListComponentModel) abstractCMSComponentModel;
							if (null != brandsTabAZListComponentModel.getVisible()
									&& brandsTabAZListComponentModel.getVisible().booleanValue())
							{
								final BrandsTabAZListComponentWsDTO brandsTabAZListComponentWsDTO = new BrandsTabAZListComponentWsDTO();
								final List<BrandsTabAZListWsDTO> brandsTabAZList = new ArrayList<BrandsTabAZListWsDTO>();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									for (final BrandsTabAZElementModel brandsTabAZElementModel : brandsTabAZListComponentModel.getItems())
									{
										final List<BrandsTabAZHeroBannerWsDTO> heroBannerCompList = new ArrayList<BrandsTabAZHeroBannerWsDTO>();
										final BrandsTabAZHeroBannerWsDTO brandsTabAZHeroBannerWsDTO = new BrandsTabAZHeroBannerWsDTO();
										final BrandsTabAZListWsDTO brandsTabAZListWsDTO = new BrandsTabAZListWsDTO();
										brandsTabAZListWsDTO.setSubType(null != brandsTabAZElementModel.getSubType()
												? brandsTabAZElementModel.getSubType() : StringUtils.EMPTY);
										for (final HeroBannerComponentModel heroBannerComponentModel : brandsTabAZElementModel.getItems())
										{
											final HeroBannerCompWsDTO heroBannerCompWsDTO = new HeroBannerCompWsDTO();
											final List<HeroBannerCompListWsDTO> heroBannerCompListWsDTO = new ArrayList<HeroBannerCompListWsDTO>();
											if (null != heroBannerComponentModel.getItems()
													&& heroBannerComponentModel.getItems().size() > 0)
											{
												for (final HeroBannerElementModel heroBannerElementModel : heroBannerComponentModel
														.getItems())
												{
													final HeroBannerCompListWsDTO heroBannerCompListObj = new HeroBannerCompListWsDTO();
													if (null != heroBannerElementModel.getImageURL()
															&& null != heroBannerElementModel.getImageURL().getURL())
													{
														heroBannerCompListObj.setImageURL(heroBannerElementModel.getImageURL().getURL());
													}
													else
													{
														heroBannerCompListObj.setImageURL(StringUtils.EMPTY);
													}
													if (null != heroBannerElementModel.getBrandLogo()
															&& null != heroBannerElementModel.getBrandLogo().getURL())
													{
														heroBannerCompListObj.setBrandLogo(heroBannerElementModel.getBrandLogo().getURL());
													}
													else
													{
														heroBannerCompListObj.setBrandLogo(StringUtils.EMPTY);
													}
													heroBannerCompListObj.setTitle(null != heroBannerElementModel.getTitle()
															? heroBannerElementModel.getTitle() : StringUtils.EMPTY);
													heroBannerCompListObj.setWebURL(null != heroBannerElementModel.getWebURL()
															? heroBannerElementModel.getWebURL() : StringUtils.EMPTY);
													heroBannerCompListWsDTO.add(heroBannerCompListObj);
												}
											}
											heroBannerCompWsDTO.setItems(heroBannerCompListWsDTO);
											heroBannerCompWsDTO.setType(heroBannerComp);
											brandsTabAZHeroBannerWsDTO.setHeroBannerComponent(heroBannerCompWsDTO);
											heroBannerCompList.add(brandsTabAZHeroBannerWsDTO);
										}
										final List<BrandsTabAZListElementWsDTO> brandsTabAZElementList = new ArrayList<BrandsTabAZListElementWsDTO>();
										for (final BrandTabAZBrandElementModel brandTabAZBrandElementModel : brandsTabAZElementModel
												.getBrands())
										{
											final BrandsTabAZListElementWsDTO brandsTabAZListElement = new BrandsTabAZListElementWsDTO();
											brandsTabAZListElement.setBrandName(null != brandTabAZBrandElementModel.getBrandName()
													? brandTabAZBrandElementModel.getBrandName() : StringUtils.EMPTY);
											brandsTabAZListElement.setWebURL(null != brandTabAZBrandElementModel.getWebURL()
													? brandTabAZBrandElementModel.getWebURL() : StringUtils.EMPTY);
											brandsTabAZElementList.add(brandsTabAZListElement);
										}
										brandsTabAZListWsDTO.setItems(heroBannerCompList);
										brandsTabAZListWsDTO.setBrands(brandsTabAZElementList);
										brandsTabAZList.add(brandsTabAZListWsDTO);

									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									brandsTabAZListComponentWsDTO.setComponentId(null != brandsTabAZListComponentModel.getUid()
											? brandsTabAZListComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(BrandsTabAZListError + brandsTabAZListComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									brandsTabAZListComponentWsDTO.setComponentId(null != brandsTabAZListComponentModel.getUid()
											? brandsTabAZListComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(BrandsTabAZListError + brandsTabAZListComponentModel.getUid(), e);
									continue;
								}
								brandsTabAZListComponentWsDTO.setItems(brandsTabAZList);
								brandsTabAZListComponentWsDTO.setType("Brands Tab AZ List Component");
								brandsTabAZListComponentWsDTO.setComponentId(null != brandsTabAZListComponentModel.getUid()
										? brandsTabAZListComponentModel.getUid() : StringUtils.EMPTY);
								uiCompPageElementWsDTO.setComponentName("brandsTabAZListComponent");
								uiCompPageElementWsDTO.setBrandsTabAZListComponent(brandsTabAZListComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof LandingPageTitleComponentModel)
						{

							final LandingPageTitleComponentModel landingPageTitleCompModel = (LandingPageTitleComponentModel) abstractCMSComponentModel;
							if (null != landingPageTitleCompModel.getVisible() && landingPageTitleCompModel.getVisible().booleanValue())
							{
								final LandingPageTitleComponentWsDTO landingPageTitleComponentWsDTO = new LandingPageTitleComponentWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									landingPageTitleComponentWsDTO.setComponentId(null != landingPageTitleCompModel.getUid()
											? landingPageTitleCompModel.getUid() : StringUtils.EMPTY);
									landingPageTitleComponentWsDTO.setTitle(null != landingPageTitleCompModel.getTitle()
											? landingPageTitleCompModel.getTitle() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									landingPageTitleComponentWsDTO.setComponentId(null != landingPageTitleCompModel.getUid()
											? landingPageTitleCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(LandingPageTitleError + landingPageTitleCompModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									landingPageTitleComponentWsDTO.setComponentId(null != landingPageTitleCompModel.getUid()
											? landingPageTitleCompModel.getUid() : StringUtils.EMPTY);
									LOG.error(LandingPageTitleError + landingPageTitleCompModel.getUid(), e);
									continue;
								}
								landingPageTitleComponentWsDTO.setType("Landing Page Title Component");
								uiCompPageElementWsDTO.setComponentName("landingPageTitleComponent");
								uiCompPageElementWsDTO.setLandingPageTitleComponent(landingPageTitleComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof CMSParagraphComponentModel)
						{

							final CMSParagraphComponentModel cmsParagraphComponentModel = (CMSParagraphComponentModel) abstractCMSComponentModel;
							if (null != cmsParagraphComponentModel.getVisible()
									&& cmsParagraphComponentModel.getVisible().booleanValue())
							{
								final CMSParagraphComponentWsDTO cmsParagraphComponentWsDTO = new CMSParagraphComponentWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

								try
								{
									cmsParagraphComponentWsDTO.setContent(null != cmsParagraphComponentModel.getContent()
											? cmsParagraphComponentModel.getContent() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{

									LOG.error(cmsParagraphError + cmsParagraphComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(cmsParagraphError + cmsParagraphComponentModel.getUid(), e);
									continue;
								}
								cmsParagraphComponentWsDTO.setType("CMS Paragraph Component");
								uiCompPageElementWsDTO.setComponentName("cmsParagraphComponent");
								uiCompPageElementWsDTO.setCmsParagraphComponent(cmsParagraphComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof CMSTextComponentModel)
						{

							final CMSTextComponentModel cmsTextComponentModel = (CMSTextComponentModel) abstractCMSComponentModel;
							if (null != cmsTextComponentModel.getVisible() && cmsTextComponentModel.getVisible().booleanValue())
							{
								final CMSTextComponentWsDTO cmsTextComponentWsDTO = new CMSTextComponentWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

								try
								{
									cmsTextComponentWsDTO.setContent(null != cmsTextComponentModel.getTextValue()
											? cmsTextComponentModel.getTextValue() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{

									LOG.error(cmsParagraphError + cmsTextComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(cmsParagraphError + cmsTextComponentModel.getUid(), e);
									continue;
								}
								cmsTextComponentWsDTO.setType("CMS Text Component");
								uiCompPageElementWsDTO.setComponentName("cmsTextComponent");
								uiCompPageElementWsDTO.setCmsTextComponent(cmsTextComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof SimpleBannerComponentModel)
						{

							final SimpleBannerComponentModel simpleBannerComponentModel = (SimpleBannerComponentModel) abstractCMSComponentModel;
							if (null != simpleBannerComponentModel.getVisible()
									&& simpleBannerComponentModel.getVisible().booleanValue())
							{
								final SimpleBannerComponentWsDTO simpleBannerComponentWsDTO = new SimpleBannerComponentWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

								try
								{
									simpleBannerComponentWsDTO.setTitle(null != simpleBannerComponentModel.getTitle()
											? simpleBannerComponentModel.getTitle() : StringUtils.EMPTY);
									simpleBannerComponentWsDTO.setDescription(null != simpleBannerComponentModel.getDescription()
											? simpleBannerComponentModel.getDescription() : StringUtils.EMPTY);
									if (null != simpleBannerComponentModel.getMedia())
									{
										simpleBannerComponentWsDTO.setMedia(null != simpleBannerComponentModel.getMedia().getURL()
												? simpleBannerComponentModel.getMedia().getURL() : StringUtils.EMPTY);
									}
									else
									{
										simpleBannerComponentWsDTO.setMedia(StringUtils.EMPTY);
									}
									simpleBannerComponentWsDTO.setUrlLink(null != simpleBannerComponentModel.getUrlLink()
											? simpleBannerComponentModel.getUrlLink() : StringUtils.EMPTY);
								}
								catch (final EtailNonBusinessExceptions e)
								{

									LOG.error(simpleBannerError + simpleBannerComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(simpleBannerError + simpleBannerComponentModel.getUid(), e);
									continue;
								}
								simpleBannerComponentWsDTO.setType("Simple Banner Component");
								uiCompPageElementWsDTO.setComponentName("simpleBannerComponent");
								uiCompPageElementWsDTO.setSimpleBannerComponent(simpleBannerComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof AccountNavigationComponentModel)
						{

							final AccountNavigationComponentModel accountNavigationComponentModel = (AccountNavigationComponentModel) abstractCMSComponentModel;
							if (null != accountNavigationComponentModel.getVisible()
									&& accountNavigationComponentModel.getVisible().booleanValue())
							{
								final AccountNavigationComponentWsDTO accountNavigationComponentWsDTO = new AccountNavigationComponentWsDTO();
								final List<CMSNavigationNodeWsDTO> cmsNavigationNodeWsDTOList = new ArrayList<CMSNavigationNodeWsDTO>();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();

								try
								{

									if (null != accountNavigationComponentModel.getNavigationNode()
											&& accountNavigationComponentModel.getNavigationNode().getLinks().size() > 0)
									{

										for (final CMSLinkComponentModel cmsLinkComponentModel : accountNavigationComponentModel
												.getNavigationNode().getLinks())
										{
											final CMSNavigationNodeWsDTO cmsNavigationNodeWsDTO = new CMSNavigationNodeWsDTO();
											cmsNavigationNodeWsDTO.setLinkName(null != cmsLinkComponentModel.getLinkName()
													? cmsLinkComponentModel.getLinkName() : StringUtils.EMPTY);
											cmsNavigationNodeWsDTO.setUrl(null != cmsLinkComponentModel.getUrl()
													? cmsLinkComponentModel.getUrl() : StringUtils.EMPTY);
											cmsNavigationNodeWsDTOList.add(cmsNavigationNodeWsDTO);
										}
									}
									accountNavigationComponentWsDTO.setNodeList(cmsNavigationNodeWsDTOList);
								}
								catch (final EtailNonBusinessExceptions e)
								{

									LOG.error(accountNavigationError + accountNavigationComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(accountNavigationError + accountNavigationComponentModel.getUid(), e);
									continue;
								}
								accountNavigationComponentWsDTO.setType("Account Navigation Component");
								uiCompPageElementWsDTO.setComponentName("accountNavigationComponent");
								uiCompPageElementWsDTO.setAccountNavigationComponent(accountNavigationComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof LandingPageHeaderComponentModel)
						{
							final LandingPageHeaderComponentModel landingPageHeaderComponentModel = (LandingPageHeaderComponentModel) abstractCMSComponentModel;
							if (null != landingPageHeaderComponentModel.getVisible()
									&& landingPageHeaderComponentModel.getVisible().booleanValue())
							{
								final LandingPageHeaderComponentWsDTO landingPageHeaderComponentWsDTO = new LandingPageHeaderComponentWsDTO();
								final List<LandingPageHeaderListWsDTO> landingPageHeaderList = new ArrayList<LandingPageHeaderListWsDTO>();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									if (null != landingPageHeaderComponentModel.getItems()
											&& landingPageHeaderComponentModel.getItems().size() > 0)
									{
										for (final LandingPageHeaderElementModel landingPageHeaderElementModel : landingPageHeaderComponentModel
												.getItems())
										{
											final LandingPageHeaderListWsDTO landingPageHeaderListWsDTO = new LandingPageHeaderListWsDTO();
											if (landingPageHeaderElementModel.getBrandLogo() != null
													&& landingPageHeaderElementModel.getBrandLogo().getURL() != null)
											{
												landingPageHeaderListWsDTO
														.setBrandLogo(landingPageHeaderElementModel.getBrandLogo().getURL());
											}
											else
											{
												landingPageHeaderListWsDTO.setBrandLogo(StringUtils.EMPTY);
											}
											landingPageHeaderListWsDTO.setTitle(null != landingPageHeaderElementModel.getTitle()
													? landingPageHeaderElementModel.getTitle() : StringUtils.EMPTY);
											if (landingPageHeaderElementModel.getImageURL() != null
													&& landingPageHeaderElementModel.getImageURL().getURL() != null)
											{
												landingPageHeaderListWsDTO.setImageURL(landingPageHeaderElementModel.getImageURL().getURL());
											}
											else
											{
												landingPageHeaderListWsDTO.setImageURL(StringUtils.EMPTY);
											}

											landingPageHeaderListWsDTO.setWebURL(landingPageHeaderElementModel.getWebURL());
											landingPageHeaderList.add(landingPageHeaderListWsDTO);
										}
									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									landingPageHeaderComponentWsDTO.setComponentId(null != landingPageHeaderComponentModel.getUid()
											? landingPageHeaderComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(LandingPageHeaderError + landingPageHeaderComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									landingPageHeaderComponentWsDTO.setComponentId(null != landingPageHeaderComponentModel.getUid()
											? landingPageHeaderComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error(LandingPageHeaderError + landingPageHeaderComponentModel.getUid(), e);
									continue;
								}
								landingPageHeaderComponentWsDTO.setComponentId(null != landingPageHeaderComponentModel.getUid()
										? landingPageHeaderComponentModel.getUid() : StringUtils.EMPTY);
								landingPageHeaderComponentWsDTO.setItems(landingPageHeaderList);
								landingPageHeaderComponentWsDTO.setType("Landing Page Header Component");
								uiCompPageElementWsDTO.setComponentName("landingPageHeaderComponent");
								uiCompPageElementWsDTO.setLandingPageHeaderComponent(landingPageHeaderComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof AutoProductRecommendationComponentModel)
						{
							final AutoProductRecommendationComponentModel autoProductRecommendationComponentModel = (AutoProductRecommendationComponentModel) abstractCMSComponentModel;
							if (null != autoProductRecommendationComponentModel.getVisible()
									&& autoProductRecommendationComponentModel.getVisible().booleanValue())
							{
								final AutoProductRecommendationComponentWsDTO autoProductRecommendationComponentWsDTO = new AutoProductRecommendationComponentWsDTO();
								final List<AutoProductRecommendationListWsDTO> autoProductRecommendationList = new ArrayList<AutoProductRecommendationListWsDTO>();
								final AutoProductRecomListPostParamsWsDTO autoProductRecomListPostParamsWsDTO = new AutoProductRecomListPostParamsWsDTO();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									if (null != autoProductRecommendationComponentModel.getItems()
											&& autoProductRecommendationComponentModel.getItems().size() > 0)
									{
										String productCode = StringUtils.EMPTY;
										try
										{
											for (final AutoProductRecommendationElementModel productObj : autoProductRecommendationComponentModel
													.getItems())
											{
												try
												{
													final AutoProductRecommendationListWsDTO autoProductRecommendationListWsDTO = new AutoProductRecommendationListWsDTO();
													if (null != productObj && null != productObj.getProductCode()
															&& null != productObj.getProductCode().getCode())
													{
														productCode = productObj.getProductCode().getCode();
														final BuyBoxData buyboxdata = buyBoxFacade
																.buyboxPrice(productObj.getProductCode().getCode());
														String productUnitPrice = StringUtils.EMPTY;
														String productPrice = StringUtils.EMPTY;

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

														final AutoProductRecomDiscountPriceWsDTO autoProductRecomDiscountPriceWsDTO = new AutoProductRecomDiscountPriceWsDTO();
														final AutoProductRecomMRPPriceWsDTO autoProductRecomMRPPriceWsDTO = new AutoProductRecomMRPPriceWsDTO();

														autoProductRecomDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
														autoProductRecomDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
														if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
														{
															autoProductRecomMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
															autoProductRecomDiscountPriceWsDTO
																	.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
														}
														autoProductRecomDiscountPriceWsDTO.setCurrencySymbol("₹");
														autoProductRecomMRPPriceWsDTO.setCurrencySymbol("₹");
														autoProductRecomMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
														autoProductRecomMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));

														autoProductRecommendationListWsDTO.setPrdId(productObj.getProductCode().getCode());
														autoProductRecommendationListWsDTO.setMrpPrice(autoProductRecomMRPPriceWsDTO);
														autoProductRecommendationListWsDTO
																.setDiscountedPrice(autoProductRecomDiscountPriceWsDTO);
														autoProductRecommendationListWsDTO.setTitle(null != productObj.getProductCode()
																? productObj.getProductCode().getName() : StringUtils.EMPTY);

														if (null != productModelUrlResolver)
														{
															autoProductRecommendationListWsDTO.setWebURL(
																	siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
														}
														else
														{
															autoProductRecommendationListWsDTO
																	.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
														}
														if (null != productObj.getProductCode().getPicture()
																&& null != productObj.getProductCode().getPicture().getURL())
														{
															autoProductRecommendationListWsDTO
																	.setImageURL(productObj.getProductCode().getPicture().getURL());
														}
														else
														{
															autoProductRecommendationListWsDTO.setImageURL(StringUtils.EMPTY);
														}
													}
													autoProductRecommendationList.add(autoProductRecommendationListWsDTO);
												}
												catch (final EtailNonBusinessExceptions e)
												{
													LOG.error(
															"AutoProductRecommendationComponentModel Product is not properly enriched or either out of stock code:-"
																	+ productCode,
															e);
													continue;
												}
												catch (final Exception e)
												{
													LOG.error(
															"AutoProductRecommendationComponentModel Product is not properly enriched or either out of stock code:-"
																	+ productCode,
															e);
													continue;
												}
											}
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(
													"AutoProductRecommendationComponentModel Product is not properly enriched or either out of stock code:-"
															+ productCode,
													e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(
													"AutoProductRecommendationComponentModel Product is not properly enriched or either out of stock code:-"
															+ productCode,
													e);
											continue;
										}
									}
									autoProductRecomListPostParamsWsDTO
											.setWidgetPlatform(null != autoProductRecommendationComponentModel.getWidgetPlatform()
													? autoProductRecommendationComponentModel.getWidgetPlatform() : StringUtils.EMPTY);
									autoProductRecommendationComponentWsDTO
											.setBackupURL(null != autoProductRecommendationComponentModel.getBackupURL()
													? autoProductRecommendationComponentModel.getBackupURL() : StringUtils.EMPTY);
									autoProductRecommendationComponentWsDTO
											.setBtnText(null != autoProductRecommendationComponentModel.getBtnText()
													? autoProductRecommendationComponentModel.getBtnText() : StringUtils.EMPTY);
									autoProductRecommendationComponentWsDTO
											.setComponentId(null != autoProductRecommendationComponentModel.getUid()
													? autoProductRecommendationComponentModel.getUid() : StringUtils.EMPTY);
									autoProductRecommendationComponentWsDTO
											.setFetchURL(null != autoProductRecommendationComponentModel.getFetchURL()
													? autoProductRecommendationComponentModel.getFetchURL() : StringUtils.EMPTY);
									autoProductRecommendationComponentWsDTO
											.setTitle(null != autoProductRecommendationComponentModel.getTitle()
													? autoProductRecommendationComponentModel.getTitle() : StringUtils.EMPTY);
									autoProductRecommendationComponentWsDTO.setType("Auto Product Recommendation Component");
									autoProductRecommendationComponentWsDTO.setPostParams(autoProductRecomListPostParamsWsDTO);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									autoProductRecommendationComponentWsDTO
											.setComponentId(null != autoProductRecommendationComponentModel.getUid()
													? autoProductRecommendationComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error("Error in getting AutoProductRecommendationComponent with id: "
											+ autoProductRecommendationComponentModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									autoProductRecommendationComponentWsDTO
											.setComponentId(null != autoProductRecommendationComponentModel.getUid()
													? autoProductRecommendationComponentModel.getUid() : StringUtils.EMPTY);
									LOG.error("Error in getting AutoProductRecommendationComponent with id: "
											+ autoProductRecommendationComponentModel.getUid(), e);
									continue;
								}
								autoProductRecommendationComponentWsDTO.setItems(autoProductRecommendationList);
								uiCompPageElementWsDTO.setComponentName("autoProductRecommendationComponent");
								uiCompPageElementWsDTO.setAutoProductRecommendationComponent(autoProductRecommendationComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							}
						}

						if (abstractCMSComponentModel instanceof LandingPageHierarchyComponentModel)
						{
							final LandingPageHierarchyComponentModel landingPageHierarchyModel = (LandingPageHierarchyComponentModel) abstractCMSComponentModel;
							if (null != landingPageHierarchyModel.getVisible() && landingPageHierarchyModel.getVisible().booleanValue())
							{
								final LandingPageHierarchyComponentWsDTO landingPageHierarchyComponentWsDTO = new LandingPageHierarchyComponentWsDTO();
								final List<LandingPageHierarchyListWsDTO> landingPageHierarchyList = new ArrayList<LandingPageHierarchyListWsDTO>();
								final UICompPageElementWsDTO uiCompPageElementWsDTO = new UICompPageElementWsDTO();
								try
								{
									if (null != landingPageHierarchyModel.getItems() && landingPageHierarchyModel.getItems().size() > 0)
									{
										for (final LandingPageHierarchyElementModel landingPageHierarchyElementModel : landingPageHierarchyModel
												.getItems())
										{
											final LandingPageHierarchyListWsDTO landingPageHierarchyListWsDTO = new LandingPageHierarchyListWsDTO();
											final List<LandingPageHierarchyItemListWsDTO> landingPageHierarchyItemList = new ArrayList<LandingPageHierarchyItemListWsDTO>();
											landingPageHierarchyListWsDTO.setTitle(null != landingPageHierarchyElementModel.getTitle()
													? landingPageHierarchyElementModel.getTitle() : StringUtils.EMPTY);
											landingPageHierarchyListWsDTO.setWebURL(null != landingPageHierarchyElementModel.getWebURL()
													? landingPageHierarchyElementModel.getWebURL() : StringUtils.EMPTY);
											if (null != landingPageHierarchyElementModel.getItems()
													&& landingPageHierarchyElementModel.getItems().size() > 0)
											{
												for (final LandingPageHierarchyElementListModel landingPageHierarchyElementListModel : landingPageHierarchyElementModel
														.getItems())
												{
													final LandingPageHierarchyItemListWsDTO landingPageHierarchyItemListWsDTO = new LandingPageHierarchyItemListWsDTO();
													landingPageHierarchyItemListWsDTO
															.setTitle(null != landingPageHierarchyElementListModel.getTitle()
																	? landingPageHierarchyElementListModel.getTitle() : StringUtils.EMPTY);
													landingPageHierarchyItemListWsDTO
															.setWebURL(null != landingPageHierarchyElementListModel.getWebURL()
																	? landingPageHierarchyElementListModel.getWebURL() : StringUtils.EMPTY);
													landingPageHierarchyItemList.add(landingPageHierarchyItemListWsDTO);
												}
											}
											landingPageHierarchyListWsDTO.setItems(landingPageHierarchyItemList);
											landingPageHierarchyList.add(landingPageHierarchyListWsDTO);
										}
									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									landingPageHierarchyComponentWsDTO.setComponentId(null != landingPageHierarchyModel.getUid()
											? landingPageHierarchyModel.getUid() : StringUtils.EMPTY);
									LOG.error(LandingPageHierError + landingPageHierarchyModel.getUid(), e);
									continue;
								}
								catch (final Exception e)
								{
									landingPageHierarchyComponentWsDTO.setComponentId(null != landingPageHierarchyModel.getUid()
											? landingPageHierarchyModel.getUid() : StringUtils.EMPTY);
									LOG.error(LandingPageHierError + landingPageHierarchyModel.getUid(), e);
									continue;
								}
								landingPageHierarchyComponentWsDTO.setComponentId(null != landingPageHierarchyModel.getUid()
										? landingPageHierarchyModel.getUid() : StringUtils.EMPTY);
								landingPageHierarchyComponentWsDTO.setItems(landingPageHierarchyList);
								landingPageHierarchyComponentWsDTO.setTitle(null != landingPageHierarchyModel.getTitle()
										? landingPageHierarchyModel.getTitle() : StringUtils.EMPTY);
								landingPageHierarchyComponentWsDTO.setType("Landing Page Hierarchy Component");
								uiCompPageElementWsDTO.setComponentName("landingPageHierarchyComponent");
								uiCompPageElementWsDTO.setLandingPageHierarchyComponent(landingPageHierarchyComponentWsDTO);
								genericUICompPageWsDTO.add(uiCompPageElementWsDTO);

							}
						}
					}
				}
			}
			try
			{
				final SeoContentData seoContentData = new SeoContentData();
				if (null != categoryId && !categoryId.isEmpty())
				{
					if (categoryId
							.startsWith(configurationService.getConfiguration().getString("marketplace.mplcatalog.salescategory.code"))
							|| categoryId.startsWith(
									configurationService.getConfiguration().getString("marketplace.mplcatalog.salesbrand.code")))
					{
						categoryModels = mplCmsComponentService.getCategoryByCode(categoryId);

						if (null != categoryModels && !categoryModels.isEmpty())
						{
							for (final CategoryModel categoryModel : categoryModels)
							{
								if (toDisplay == null)
								{
									toDisplay = categoryModel;
								}
							}

							if (toDisplay != null)
							{
								if (!categoryService.isRoot(toDisplay))
								{
									breadcrumbs.add(getCategoryBreadcrumb(toDisplay, breadcrumbDTO));
								}
							}
						}
					}
				}
				seoContentData.setAlternateURL(StringUtils.EMPTY);
				seoContentData.setCanonicalURL(StringUtils.EMPTY);
				seoContentData.setBreadcrumbs(breadcrumbs);
				seoContentData
						.setDescription(null != contentPage.getDescription() ? contentPage.getDescription() : StringUtils.EMPTY);
				seoContentData.setImageURL(StringUtils.EMPTY);
				seoContentData.setKeywords(null != contentPage.getKeywords() ? contentPage.getKeywords() : StringUtils.EMPTY);
				seoContentData.setTitle(null != contentPage.getTitle() ? contentPage.getTitle() : StringUtils.EMPTY);
				uiCompPageObj.setSeo(seoContentData);
			}
			catch (final Exception e)
			{
				LOG.error("Error in getting SEO: ", e);
			}
			uiCompPageObj.setItems(genericUICompPageWsDTO);
			uiCompPageObj.setMessage(contentPage.getUid());
			if (null != contentPage.getReactPageType() && !contentPage.getReactPageType().isEmpty())
			{
				uiCompPageObj.setPageType(contentPage.getReactPageType());
			}
			else
			{
				uiCompPageObj.setPageType(StringUtils.EMPTY);
			}
			uiCompPageObj.setStatus(Success);
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
		final String siteUrl = configurationService.getConfiguration().getString("website.mpl.https");

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
							try
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
										heroBannerCompListObj.setImageURL(StringUtils.EMPTY);
									}
									if (null != heroBannerElementModel.getBrandLogo()
											&& null != heroBannerElementModel.getBrandLogo().getURL())
									{
										heroBannerCompListObj.setBrandLogo(heroBannerElementModel.getBrandLogo().getURL());
									}
									else
									{
										heroBannerCompListObj.setBrandLogo(StringUtils.EMPTY);
									}
									heroBannerCompListObj.setTitle(null != heroBannerElementModel.getTitle()
											? heroBannerElementModel.getTitle() : StringUtils.EMPTY);
									heroBannerCompListObj.setWebURL(null != heroBannerElementModel.getWebURL()
											? heroBannerElementModel.getWebURL() : StringUtils.EMPTY);
									heroBannerCompListWsDTO.add(heroBannerCompListObj);
								}
							}
							catch (final EtailNonBusinessExceptions e)
							{
								heroBannerCompWsDTO.setComponentId(
										null != heroBannerCompObj.getUid() ? heroBannerCompObj.getUid() : StringUtils.EMPTY);
								LOG.error(heroBannerError + heroBannerCompObj.getUid(), e);
								continue;
							}
							catch (final Exception e)
							{
								heroBannerCompWsDTO.setComponentId(
										null != heroBannerCompObj.getUid() ? heroBannerCompObj.getUid() : StringUtils.EMPTY);
								LOG.error(heroBannerError + heroBannerCompObj.getUid(), e);
								continue;
							}
						}
						heroBannerCompWsDTO.setItems(heroBannerCompListWsDTO);
						heroBannerCompWsDTO.setType(heroBannerComp);
						heroBannerCompWsDTO
								.setComponentId(null != heroBannerCompObj.getUid() ? heroBannerCompObj.getUid() : StringUtils.EMPTY);
						uiCompPageElementWsDTO.setHeroBannerComponent(heroBannerCompWsDTO);
						uiCompPageElementWsDTO.setComponentName("heroBannerComponent");
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof ConnectBannerComponentModel)
					{
						final ConnectBannerWsDTO connectBannerWsDTO = new ConnectBannerWsDTO();
						final ConnectBannerComponentModel connectBannerComponentModel = (ConnectBannerComponentModel) abstractCMSComponentModel;
						try
						{
							if (null != connectBannerComponentModel.getBackgroundImageURL()
									&& null != connectBannerComponentModel.getBackgroundImageURL().getURL())
							{
								connectBannerWsDTO.setBackgroundImageURL(connectBannerComponentModel.getBackgroundImageURL().getURL());
							}
							else
							{
								connectBannerWsDTO.setBackgroundImageURL(StringUtils.EMPTY);
							}
							if (null != connectBannerComponentModel.getIconImageURL()
									&& null != connectBannerComponentModel.getIconImageURL().getURL())
							{
								connectBannerWsDTO.setIconImageURL(connectBannerComponentModel.getIconImageURL().getURL());
							}
							else
							{
								connectBannerWsDTO.setIconImageURL(StringUtils.EMPTY);
							}

							connectBannerWsDTO.setBtnText(null != connectBannerComponentModel.getBtnText()
									? connectBannerComponentModel.getBtnText() : StringUtils.EMPTY);
							connectBannerWsDTO.setDescription(null != connectBannerComponentModel.getDescription()
									? connectBannerComponentModel.getDescription() : StringUtils.EMPTY);
							connectBannerWsDTO.setSubType(null != connectBannerComponentModel.getSubType()
									? connectBannerComponentModel.getSubType() : StringUtils.EMPTY);
							connectBannerWsDTO.setTitle(null != connectBannerComponentModel.getTitle()
									? connectBannerComponentModel.getTitle() : StringUtils.EMPTY);
							connectBannerWsDTO.setWebURL(null != connectBannerComponentModel.getWebURL()
									? connectBannerComponentModel.getWebURL() : StringUtils.EMPTY);
							connectBannerWsDTO.setStartHexCode(null != connectBannerComponentModel.getStartHexCode()
									? connectBannerComponentModel.getStartHexCode() : StringUtils.EMPTY);
							connectBannerWsDTO.setEndHexCode(null != connectBannerComponentModel.getEndHexCode()
									? connectBannerComponentModel.getEndHexCode() : StringUtils.EMPTY);
							connectBannerWsDTO.setType("Multipurpose Banner Component");
							connectBannerWsDTO.setComponentId(null != connectBannerComponentModel.getUid()
									? connectBannerComponentModel.getUid() : StringUtils.EMPTY);
							uiCompPageElementWsDTO.setMultiPurposeBanner(connectBannerWsDTO);
							uiCompPageElementWsDTO.setComponentName("multiPurposeBanner");
						}
						catch (final EtailNonBusinessExceptions e)
						{
							connectBannerWsDTO.setComponentId(null != connectBannerComponentModel.getUid()
									? connectBannerComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(connectBannerComponentError + connectBannerComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							connectBannerWsDTO.setComponentId(null != connectBannerComponentModel.getUid()
									? connectBannerComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(connectBannerComponentError + connectBannerComponentModel.getUid(), e);
							continue;
						}
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof OffersWidgetComponentModel)
					{
						final OffersWidgetWsDTO offersWidgetWsDTO = new OffersWidgetWsDTO();
						final List<OffersWidgetElementWsDTO> offersWidgetElementList = new ArrayList<OffersWidgetElementWsDTO>();

						final OffersWidgetComponentModel offersWidgetComponentModel = (OffersWidgetComponentModel) abstractCMSComponentModel;
						try
						{
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
										offersWidgetElementWsDTO.setImageURL(StringUtils.EMPTY);
									}
									offersWidgetElementWsDTO.setTitle(null != offersWidgetElementModel.getTitle()
											? offersWidgetElementModel.getTitle() : StringUtils.EMPTY);
									offersWidgetElementWsDTO.setBtnText(null != offersWidgetElementModel.getBtnText()
											? offersWidgetElementModel.getBtnText() : StringUtils.EMPTY);
									offersWidgetElementWsDTO.setDiscountText(null != offersWidgetElementModel.getDiscountText()
											? offersWidgetElementModel.getDiscountText() : StringUtils.EMPTY);
									offersWidgetElementWsDTO.setWebURL(null != offersWidgetElementModel.getWebURL()
											? offersWidgetElementModel.getWebURL() : StringUtils.EMPTY);
									offersWidgetElementList.add(offersWidgetElementWsDTO);
								}
							}
							offersWidgetWsDTO.setItems(offersWidgetElementList);
							offersWidgetWsDTO.setTitle(null != offersWidgetComponentModel.getTitle()
									? offersWidgetComponentModel.getTitle() : StringUtils.EMPTY);
							offersWidgetWsDTO.setType("Offers Component");
							offersWidgetWsDTO.setComponentId(
									null != offersWidgetComponentModel.getUid() ? offersWidgetComponentModel.getUid() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							offersWidgetWsDTO.setComponentId(
									null != offersWidgetComponentModel.getUid() ? offersWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(offerWidgetComponentError + offersWidgetComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							offersWidgetWsDTO.setComponentId(
									null != offersWidgetComponentModel.getUid() ? offersWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(offerWidgetComponentError + offersWidgetComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setOffersComponent(offersWidgetWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiCompPageElementWsDTO.setComponentName("offersComponent");
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof FlashSalesComponentModel)
					{
						final FlashSalesWsDTO flashSalesWsDTO = new FlashSalesWsDTO();
						final List<FlashSalesOffersWsDTO> flashSalesOffersWsDTOList = new ArrayList<FlashSalesOffersWsDTO>();
						final List<FlashSalesElementWsDTO> flashSalesElementWsDTOList = new ArrayList<FlashSalesElementWsDTO>();

						final FlashSalesComponentModel flashSalesComponentModel = (FlashSalesComponentModel) abstractCMSComponentModel;
						try
						{
							if (null != flashSalesComponentModel.getOffers() && flashSalesComponentModel.getOffers().size() > 0)
							{
								for (final FlashSalesElementModel flashSalesOffersModel : flashSalesComponentModel.getOffers())
								{
									final FlashSalesOffersWsDTO flashSalesOffersWsDTO = new FlashSalesOffersWsDTO();

									flashSalesOffersWsDTO.setTitle(
											null != flashSalesOffersModel.getTitle() ? flashSalesOffersModel.getTitle() : StringUtils.EMPTY);
									flashSalesOffersWsDTO.setDescription(null != flashSalesOffersModel.getDescription()
											? flashSalesOffersModel.getDescription() : StringUtils.EMPTY);
									if (null != flashSalesOffersModel.getImageURL()
											&& null != flashSalesOffersModel.getImageURL().getURL())
									{
										flashSalesOffersWsDTO.setImageURL(flashSalesOffersModel.getImageURL().getURL());
									}
									else
									{
										flashSalesOffersWsDTO.setImageURL(StringUtils.EMPTY);
									}
									flashSalesOffersWsDTO.setWebURL(null != flashSalesOffersModel.getWebURL()
											? flashSalesOffersModel.getWebURL() : StringUtils.EMPTY);

									flashSalesOffersWsDTOList.add(flashSalesOffersWsDTO);
								}
							}
							if (null != flashSalesComponentModel.getItems() && flashSalesComponentModel.getItems().size() > 0)
							{
								String productCode = StringUtils.EMPTY;
								try
								{
									for (final FlashSalesItemElementModel flashSalesElementModel : flashSalesComponentModel.getItems())
									{
										try
										{
											final FlashSalesElementWsDTO flashSalesElementWsDTO = new FlashSalesElementWsDTO();

											if (null != flashSalesElementModel && null != flashSalesElementModel.getProductCode()
													&& null != flashSalesElementModel.getProductCode().getCode())
											{
												productCode = flashSalesElementModel.getProductCode().getCode();
												final BuyBoxData buyboxdata = buyBoxFacade
														.buyboxPrice(flashSalesElementModel.getProductCode().getCode());
												//f]i;nal DecimalFormat df = new DecimalFormat("0.00");
												String productUnitPrice = StringUtils.EMPTY;
												String productPrice = StringUtils.EMPTY;

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

												flashSalesDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
												if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
												{
													flashSalesDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
													flashSalesMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
												}
												flashSalesDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
												flashSalesDiscountPriceWsDTO.setCurrencySymbol("₹");
												flashSalesMRPPriceWsDTO.setCurrencySymbol("₹");
												flashSalesMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
												flashSalesMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));

												flashSalesElementWsDTO.setPrdId(flashSalesElementModel.getProductCode().getCode());
												flashSalesElementWsDTO.setMrpPrice(flashSalesMRPPriceWsDTO);
												flashSalesElementWsDTO.setDiscountedPrice(flashSalesDiscountPriceWsDTO);
												flashSalesElementWsDTO.setTitle(null != flashSalesElementModel.getProductCode()
														? flashSalesElementModel.getProductCode().getName() : StringUtils.EMPTY);
												if (null != productModelUrlResolver)
												{
													flashSalesElementWsDTO.setWebURL(
															siteUrl + productModelUrlResolver.resolve(flashSalesElementModel.getProductCode()));
												}
												else
												{
													flashSalesElementWsDTO
															.setWebURL(siteUrl + "/" + flashSalesElementModel.getProductCode().getCode());
												}

												if (flashSalesElementModel.getProductCode().getPicture() != null
														&& flashSalesElementModel.getProductCode().getPicture().getURL() != null)
												{
													flashSalesElementWsDTO
															.setImageURL(flashSalesElementModel.getProductCode().getPicture().getURL());
												}
												else
												{
													flashSalesElementWsDTO.setImageURL(StringUtils.EMPTY);
												}
												flashSalesElementWsDTOList.add(flashSalesElementWsDTO);
											}
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(fscProductError + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(fscProductError + productCode, e);
											continue;
										}
									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									LOG.error(fscProductError + productCode, e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(fscProductError + productCode, e);
									continue;
								}
							}
							flashSalesWsDTO.setBackgroundHexCode(null != flashSalesComponentModel.getBackgroundHexCode()
									? flashSalesComponentModel.getBackgroundHexCode() : StringUtils.EMPTY);
							if (null != flashSalesComponentModel.getBackgroundImageURL()
									&& null != flashSalesComponentModel.getBackgroundImageURL().getURL())
							{
								flashSalesWsDTO.setBackgroundImageURL(flashSalesComponentModel.getBackgroundImageURL().getURL());
							}
							else
							{
								flashSalesWsDTO.setBackgroundImageURL(StringUtils.EMPTY);
							}
							flashSalesWsDTO.setBtnText(null != flashSalesComponentModel.getBtnText()
									? flashSalesComponentModel.getBtnText() : StringUtils.EMPTY);
							flashSalesWsDTO.setDescription(null != flashSalesComponentModel.getDescription()
									? flashSalesComponentModel.getDescription() : StringUtils.EMPTY);

							flashSalesWsDTO.setEndDate(null != flashSalesComponentModel.getEndDate()
									? formatter.format(flashSalesComponentModel.getEndDate()) : StringUtils.EMPTY);
							flashSalesWsDTO.setStartDate(null != flashSalesComponentModel.getStartDate()
									? formatter.format(flashSalesComponentModel.getStartDate()) : StringUtils.EMPTY);

							flashSalesWsDTO.setTitle(
									null != flashSalesComponentModel.getTitle() ? flashSalesComponentModel.getTitle() : StringUtils.EMPTY);
							flashSalesWsDTO.setWebURL(null != flashSalesComponentModel.getWebURL() ? flashSalesComponentModel.getWebURL()
									: StringUtils.EMPTY);
							flashSalesWsDTO.setOffers(flashSalesOffersWsDTOList);
							flashSalesWsDTO.setItems(flashSalesElementWsDTOList);
							flashSalesWsDTO.setType("Flash Sales Component");
							flashSalesWsDTO.setComponentId(
									null != flashSalesComponentModel.getUid() ? flashSalesComponentModel.getUid() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							flashSalesWsDTO.setComponentId(
									null != flashSalesComponentModel.getUid() ? flashSalesComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(fscError + flashSalesComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							flashSalesWsDTO.setComponentId(
									null != flashSalesComponentModel.getUid() ? flashSalesComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(fscError + flashSalesComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setFlashSalesComponent(flashSalesWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiCompPageElementWsDTO.setComponentName("flashSalesComponent");
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}


					if (abstractCMSComponentModel instanceof ContentWidgetComponentModel)
					{
						final ContentWidgetCompWsDTO contentWidgetCompWsDTO = new ContentWidgetCompWsDTO();
						final List<ContentWidgetElementWsDTO> contentWidgetElementList = new ArrayList<ContentWidgetElementWsDTO>();

						final ContentWidgetComponentModel contentWidgetComponentModel = (ContentWidgetComponentModel) abstractCMSComponentModel;
						try
						{
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
										contentWidgetElementWsDTO.setImageURL(StringUtils.EMPTY);
									}
									contentWidgetElementWsDTO.setDescription(null != contentWidgetElementModel.getDescription()
											? contentWidgetElementModel.getDescription() : StringUtils.EMPTY);
									contentWidgetElementWsDTO.setTitle(null != contentWidgetElementModel.getTitle()
											? contentWidgetElementModel.getTitle() : StringUtils.EMPTY);
									contentWidgetElementWsDTO.setWebURL(null != contentWidgetElementModel.getWebURL()
											? contentWidgetElementModel.getWebURL() : StringUtils.EMPTY);
									contentWidgetElementWsDTO.setBtnText(null != contentWidgetElementModel.getBtnText()
											? contentWidgetElementModel.getBtnText() : StringUtils.EMPTY);

									contentWidgetElementList.add(contentWidgetElementWsDTO);
								}
							}
							contentWidgetCompWsDTO.setItems(contentWidgetElementList);
							contentWidgetCompWsDTO.setTitle(null != contentWidgetComponentModel.getTitle()
									? contentWidgetComponentModel.getTitle() : StringUtils.EMPTY);
							contentWidgetCompWsDTO.setType("Content Component");
							contentWidgetCompWsDTO.setComponentId(null != contentWidgetComponentModel.getUid()
									? contentWidgetComponentModel.getUid() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							contentWidgetCompWsDTO.setComponentId(null != contentWidgetComponentModel.getUid()
									? contentWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(contentWidgetError + contentWidgetComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							contentWidgetCompWsDTO.setComponentId(null != contentWidgetComponentModel.getUid()
									? contentWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(contentWidgetError + contentWidgetComponentModel.getUid(), e);
							continue;

						}
						uiCompPageElementWsDTO.setContentComponent(contentWidgetCompWsDTO);
						uiCompPageElementWsDTO.setComponentName("contentComponent");
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof BannerProductCarouselComponentModel)
					{
						final BannerProductCarouselWsDTO bannerProductCarouselWsDTO = new BannerProductCarouselWsDTO();
						final List<BannerProCarouselElementWsDTO> bannerProCarouselList = new ArrayList<BannerProCarouselElementWsDTO>();

						final BannerProductCarouselComponentModel bannerProComponentModel = (BannerProductCarouselComponentModel) abstractCMSComponentModel;
						try
						{
							if (null != bannerProComponentModel.getItems() && bannerProComponentModel.getItems().size() > 0)
							{
								String productCode = StringUtils.EMPTY;

								for (final BannerProdCarouselElementCompModel productObj : bannerProComponentModel.getItems())
								{
									try
									{
										final BannerProCarouselElementWsDTO bannerProCarouselElementWsDTO = new BannerProCarouselElementWsDTO();
										if (null != productObj && null != productObj.getProductCode()
												&& null != productObj.getProductCode().getCode())
										{
											productCode = productObj.getProductCode().getCode();
											final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productObj.getProductCode().getCode());
											//f]i;nal DecimalFormat df = new DecimalFormat("0.00");
											String productUnitPrice = StringUtils.EMPTY;
											String productPrice = StringUtils.EMPTY;

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

											bannerProDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
											bannerProDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
											if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
											{
												bannerProMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
												bannerProDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
											}
											bannerProDiscountPriceWsDTO.setCurrencySymbol("₹");
											bannerProMRPPriceWsDTO.setCurrencySymbol("₹");
											bannerProMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
											bannerProMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));

											bannerProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
											bannerProCarouselElementWsDTO.setMrpPrice(bannerProMRPPriceWsDTO);
											bannerProCarouselElementWsDTO.setDiscountedPrice(bannerProDiscountPriceWsDTO);
											bannerProCarouselElementWsDTO.setTitle(null != productObj.getProductCode()
													? productObj.getProductCode().getName() : StringUtils.EMPTY);
											if (null != productModelUrlResolver)
											{
												bannerProCarouselElementWsDTO
														.setWebURL(siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
											}
											else
											{
												bannerProCarouselElementWsDTO
														.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
											}

											if (null != productObj.getProductCode().getPicture()
													&& null != productObj.getProductCode().getPicture().getURL())
											{
												bannerProCarouselElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
											}
											else
											{
												bannerProCarouselElementWsDTO.setImageURL(StringUtils.EMPTY);
											}
										}
										bannerProCarouselList.add(bannerProCarouselElementWsDTO);

									}
									catch (final EtailNonBusinessExceptions e)
									{
										LOG.error(bannerProductCarouseError + productCode, e);
										continue;
									}
									catch (final Exception e)
									{
										LOG.error(bannerProductCarouseError + productCode, e);
										continue;
									}
								}

							}
							bannerProductCarouselWsDTO.setBtnText(null != bannerProComponentModel.getBtnText()
									? bannerProComponentModel.getBtnText() : StringUtils.EMPTY);
							if (null != bannerProComponentModel.getImageURL() && null != bannerProComponentModel.getImageURL().getURL())
							{
								bannerProductCarouselWsDTO.setImageURL(bannerProComponentModel.getImageURL().getURL());
							}
							else
							{
								bannerProductCarouselWsDTO.setImageURL(StringUtils.EMPTY);
							}
							bannerProductCarouselWsDTO.setItems(bannerProCarouselList);
							bannerProductCarouselWsDTO.setType("Banner Product Carousel Component");
							bannerProductCarouselWsDTO.setComponentId(
									null != bannerProComponentModel.getUid() ? bannerProComponentModel.getUid() : StringUtils.EMPTY);
							bannerProductCarouselWsDTO.setDescription(null != bannerProComponentModel.getDescription()
									? bannerProComponentModel.getDescription() : StringUtils.EMPTY);
							bannerProductCarouselWsDTO.setTitle(
									null != bannerProComponentModel.getTitle() ? bannerProComponentModel.getTitle() : StringUtils.EMPTY);
							bannerProductCarouselWsDTO.setWebURL(
									null != bannerProComponentModel.getWebURL() ? bannerProComponentModel.getWebURL() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							bannerProductCarouselWsDTO.setComponentId(
									null != bannerProComponentModel.getUid() ? bannerProComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(bpcError + bannerProComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							bannerProductCarouselWsDTO.setComponentId(
									null != bannerProComponentModel.getUid() ? bannerProComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(bpcError + bannerProComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setBannerProductCarouselComponent(bannerProductCarouselWsDTO);
						uiCompPageElementWsDTO.setComponentName("bannerProductCarouselComponent");
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof VideoProductCarouselComponentModel)
					{
						final VideoProductCarouselWsDTO videoProductCarouselWsDTO = new VideoProductCarouselWsDTO();
						final List<VideoProductCarElementWsDTO> videoProCarouselList = new ArrayList<VideoProductCarElementWsDTO>();

						final VideoProductCarouselComponentModel videoProComponentModel = (VideoProductCarouselComponentModel) abstractCMSComponentModel;
						try
						{
							if (null != videoProComponentModel.getItems() && videoProComponentModel.getItems().size() > 0)
							{
								String productCode = StringUtils.EMPTY;
								try
								{
									for (final VideoProductCarouselElementModel productObj : videoProComponentModel.getItems())
									{
										try
										{
											final VideoProductCarElementWsDTO videoProCarouselElementWsDTO = new VideoProductCarElementWsDTO();
											if (null != productObj && null != productObj.getProductCode()
													&& null != productObj.getProductCode().getCode())
											{
												productCode = productObj.getProductCode().getCode();
												final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productObj.getProductCode().getCode());
												String productUnitPrice = StringUtils.EMPTY;
												String productPrice = StringUtils.EMPTY;

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

												videoProDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
												videoProDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
												if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
												{
													videoProDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
													videoProMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
												}
												videoProDiscountPriceWsDTO.setCurrencySymbol("₹");
												videoProMRPPriceWsDTO.setCurrencySymbol("₹");
												videoProMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
												videoProMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));
												if (productObj.getProductCode() != null && productObj.getProductCode().getCode() != null)
												{
													videoProCarouselElementWsDTO.setPrdId(productObj.getProductCode().getCode());
												}
												else
												{
													videoProCarouselElementWsDTO.setPrdId(StringUtils.EMPTY);
												}
												videoProCarouselElementWsDTO.setMrpPrice(videoProMRPPriceWsDTO);
												videoProCarouselElementWsDTO.setDiscountedPrice(videoProDiscountPriceWsDTO);
												videoProCarouselElementWsDTO.setTitle(null != productObj.getProductCode()
														? productObj.getProductCode().getName() : StringUtils.EMPTY);
												if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
														&& productObj.getProductCode().getPicture().getURL() != null)
												{
													videoProCarouselElementWsDTO
															.setImageURL(productObj.getProductCode().getPicture().getURL());
												}
												else
												{
													videoProCarouselElementWsDTO.setImageURL(StringUtils.EMPTY);
												}
												if (null != productModelUrlResolver)
												{
													videoProCarouselElementWsDTO
															.setWebURL(siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
												}
												else
												{
													videoProCarouselElementWsDTO
															.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
												}
											}
											videoProCarouselList.add(videoProCarouselElementWsDTO);
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(VideoProductCarouselError + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(VideoProductCarouselError + productCode, e);
											continue;
										}
									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									LOG.error(VideoProductCarouselError + productCode, e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(VideoProductCarouselError + productCode, e);
									continue;
								}
							}
							videoProductCarouselWsDTO.setBtnText(
									null != videoProComponentModel.getBtnText() ? videoProComponentModel.getBtnText() : StringUtils.EMPTY);
							if (null != videoProComponentModel.getImageURL() && null != videoProComponentModel.getImageURL().getURL())
							{
								videoProductCarouselWsDTO.setImageURL(videoProComponentModel.getImageURL().getURL());
							}
							else
							{
								videoProductCarouselWsDTO.setImageURL(StringUtils.EMPTY);
							}

							videoProductCarouselWsDTO.setItems(videoProCarouselList);
							videoProductCarouselWsDTO.setType("Video Product Carousel Component");
							videoProductCarouselWsDTO.setComponentId(
									null != videoProComponentModel.getUid() ? videoProComponentModel.getUid() : StringUtils.EMPTY);
							videoProductCarouselWsDTO.setDescription(null != videoProComponentModel.getDescription()
									? videoProComponentModel.getDescription() : StringUtils.EMPTY);
							videoProductCarouselWsDTO.setTitle(
									null != videoProComponentModel.getTitle() ? videoProComponentModel.getTitle() : StringUtils.EMPTY);
							videoProductCarouselWsDTO.setWebURL(
									null != videoProComponentModel.getWebURL() ? videoProComponentModel.getWebURL() : StringUtils.EMPTY);
							videoProductCarouselWsDTO.setVideoURL(null != videoProComponentModel.getVideoURL()
									? videoProComponentModel.getVideoURL() : StringUtils.EMPTY);
							if (videoProComponentModel.getBrandLogo() != null && videoProComponentModel.getBrandLogo().getURL() != null)
							{
								videoProductCarouselWsDTO.setBrandLogo(videoProComponentModel.getBrandLogo().getURL());
							}
							else
							{
								videoProductCarouselWsDTO.setBrandLogo(StringUtils.EMPTY);
							}
						}
						catch (final EtailNonBusinessExceptions e)
						{
							videoProductCarouselWsDTO.setComponentId(
									null != videoProComponentModel.getUid() ? videoProComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(vpcError + videoProComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							videoProductCarouselWsDTO.setComponentId(
									null != videoProComponentModel.getUid() ? videoProComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(vpcError + videoProComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setVideoProductCarouselComponent(videoProductCarouselWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("videoProductCarouselComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof ThemeOffersComponentModel)
					{
						final ThemeOffersWsDTO themeOffersWsDTO = new ThemeOffersWsDTO();
						final List<ThemeOffersElementWsDTO> themeOffersElementList = new ArrayList<ThemeOffersElementWsDTO>();
						final List<ThemeOffersCompOfferWsDTO> themeOffersCompOfferList = new ArrayList<ThemeOffersCompOfferWsDTO>();

						final ThemeOffersComponentModel themeOffersComponentModel = (ThemeOffersComponentModel) abstractCMSComponentModel;
						try
						{
							if (null != themeOffersComponentModel.getOffers() && themeOffersComponentModel.getOffers().size() > 0)
							{
								for (final ThemeOffersCompOfferElementModel themeOffersElementModel : themeOffersComponentModel
										.getOffers())
								{
									final ThemeOffersCompOfferWsDTO themeOffersCompOfferWsDTO = new ThemeOffersCompOfferWsDTO();
									themeOffersCompOfferWsDTO.setTitle(null != themeOffersElementModel.getTitle()
											? themeOffersElementModel.getTitle() : StringUtils.EMPTY);
									themeOffersCompOfferWsDTO.setDescription(null != themeOffersElementModel.getDescription()
											? themeOffersElementModel.getDescription() : StringUtils.EMPTY);
									if (null != themeOffersElementModel.getImageURL()
											&& null != themeOffersElementModel.getImageURL().getURL())
									{
										themeOffersCompOfferWsDTO.setImageURL(themeOffersElementModel.getImageURL().getURL());
									}
									else
									{
										themeOffersCompOfferWsDTO.setImageURL(StringUtils.EMPTY);
									}
									themeOffersCompOfferWsDTO.setWebURL(null != themeOffersElementModel.getWebURL()
											? themeOffersElementModel.getWebURL() : StringUtils.EMPTY);

									themeOffersCompOfferList.add(themeOffersCompOfferWsDTO);
								}
							}
							if (null != themeOffersComponentModel.getItems() && themeOffersComponentModel.getItems().size() > 0)
							{
								String productCode = StringUtils.EMPTY;
								try
								{
									for (final ThemeOffersItemsElementModel productObj : themeOffersComponentModel.getItems())
									{
										try
										{
											final ThemeOffersElementWsDTO themeOffersElementWsDTO = new ThemeOffersElementWsDTO();

											if (null != productObj && null != productObj.getProductCode()
													&& null != productObj.getProductCode().getCode())
											{
												productCode = productObj.getProductCode().getCode();
												final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productObj.getProductCode().getCode());
												String productUnitPrice = StringUtils.EMPTY;
												String productPrice = StringUtils.EMPTY;

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

												thDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
												thDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
												if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
												{
													thDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
													thMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
												}
												thDiscountPriceWsDTO.setCurrencySymbol("₹");
												thMrpPriceWsDTO.setCurrencySymbol("₹");
												thMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
												thMrpPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));
												themeOffersElementWsDTO.setPrdId(productObj.getProductCode().getCode());
												themeOffersElementWsDTO.setMrpPrice(thMrpPriceWsDTO);
												themeOffersElementWsDTO.setDiscountedPrice(thDiscountPriceWsDTO);
												themeOffersElementWsDTO.setTitle(null != productObj.getProductCode()
														? productObj.getProductCode().getName() : StringUtils.EMPTY);
												if (null != productModelUrlResolver)
												{
													themeOffersElementWsDTO
															.setWebURL(siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
												}
												else
												{
													themeOffersElementWsDTO.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
												}
												if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
														&& productObj.getProductCode().getPicture().getURL() != null)
												{
													themeOffersElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
												}
												else
												{
													themeOffersElementWsDTO.setImageURL(StringUtils.EMPTY);
												}

											}
											themeOffersElementList.add(themeOffersElementWsDTO);

										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(themeOffersError + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(themeOffersError + productCode, e);
											continue;
										}
									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									LOG.error(themeOffersError + productCode, e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(themeOffersError + productCode, e);
									continue;
								}
							}
							themeOffersWsDTO.setBackgroundHexCode(null != themeOffersComponentModel.getBackgroundHexCode()
									? themeOffersComponentModel.getBackgroundHexCode() : StringUtils.EMPTY);
							if (themeOffersComponentModel.getBackgroundImageURL() != null
									&& themeOffersComponentModel.getBackgroundImageURL().getURL() != null)
							{
								themeOffersWsDTO.setBackgroundImageURL(themeOffersComponentModel.getBackgroundImageURL().getURL());
							}
							else
							{
								themeOffersWsDTO.setBackgroundImageURL(StringUtils.EMPTY);
							}
							themeOffersWsDTO.setBtnText(null != themeOffersComponentModel.getBtnText()
									? themeOffersComponentModel.getBtnText() : StringUtils.EMPTY);
							themeOffersWsDTO.setItems(themeOffersElementList);
							themeOffersWsDTO.setOffers(themeOffersCompOfferList);
							themeOffersWsDTO.setTitle(null != themeOffersComponentModel.getTitle() ? themeOffersComponentModel.getTitle()
									: StringUtils.EMPTY);
							themeOffersWsDTO.setType("Theme Offers Component");
							themeOffersWsDTO.setComponentId(
									null != themeOffersComponentModel.getUid() ? themeOffersComponentModel.getUid() : StringUtils.EMPTY);
							themeOffersWsDTO.setWebURL(null != themeOffersComponentModel.getWebURL()
									? themeOffersComponentModel.getWebURL() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							themeOffersWsDTO.setComponentId(
									null != themeOffersComponentModel.getUid() ? themeOffersComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(toError + themeOffersComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							themeOffersWsDTO.setComponentId(
									null != themeOffersComponentModel.getUid() ? themeOffersComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(toError + themeOffersComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setThemeOffersComponent(themeOffersWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("themeOffersComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof ThemeProductWidgetComponentModel)
					{
						final ThemeProductWidgetWsDTO themeProductWidgetWsDTO = new ThemeProductWidgetWsDTO();
						final ThemeProductWidgetComponentModel themeProductWidgetComponentModel = (ThemeProductWidgetComponentModel) abstractCMSComponentModel;
						final List<ThemeProWidElementWsDTO> themeProWidElementList = new ArrayList<ThemeProWidElementWsDTO>();
						try
						{
							if (null != themeProductWidgetComponentModel.getItems()
									&& themeProductWidgetComponentModel.getItems().size() > 0)
							{
								String productCode = StringUtils.EMPTY;
								try
								{
									for (final ThemeProductWidgetElementModel productObj : themeProductWidgetComponentModel.getItems())
									{
										try
										{
											final ThemeProWidElementWsDTO themeProWidElementWsDTO = new ThemeProWidElementWsDTO();

											if (null != productObj && null != productObj.getProductCode()
													&& null != productObj.getProductCode().getCode())
											{
												productCode = productObj.getProductCode().getCode();
												final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productObj.getProductCode().getCode());
												//f]i;nal DecimalFormat df = new DecimalFormat("0.00");
												String productUnitPrice = StringUtils.EMPTY;
												String productPrice = StringUtils.EMPTY;

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

												thDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
												thDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
												if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
												{
													thDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
													thMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
												}
												thMrpPriceWsDTO.setCurrencySymbol("₹");
												thDiscountPriceWsDTO.setCurrencySymbol("₹");
												thMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
												thMrpPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));
												themeProWidElementWsDTO.setPrdId(productObj.getProductCode().getCode());
												themeProWidElementWsDTO.setMrpPrice(thMrpPriceWsDTO);
												themeProWidElementWsDTO.setDiscountedPrice(thDiscountPriceWsDTO);
												themeProWidElementWsDTO.setTitle(null != productObj.getProductCode()
														? productObj.getProductCode().getName() : StringUtils.EMPTY);
												if (null != productModelUrlResolver)
												{
													themeProWidElementWsDTO
															.setWebURL(siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
												}
												else
												{
													themeProWidElementWsDTO.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
												}
												if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
														&& productObj.getProductCode().getPicture().getURL() != null)
												{
													themeProWidElementWsDTO.setImageURL(productObj.getProductCode().getPicture().getURL());
												}
												else
												{
													themeProWidElementWsDTO.setImageURL(StringUtils.EMPTY);
												}
											}
											themeProWidElementList.add(themeProWidElementWsDTO);
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(themeProductWidgetError + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(themeProductWidgetError + productCode, e);
											continue;
										}
									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									LOG.error(themeProductWidgetError + productCode, e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(themeProductWidgetError + productCode, e);
									continue;
								}
							}
							if (themeProductWidgetComponentModel.getImageURL() != null
									&& themeProductWidgetComponentModel.getImageURL().getURL() != null)
							{
								themeProductWidgetWsDTO.setImageURL(themeProductWidgetComponentModel.getImageURL().getURL());
							}
							else
							{
								themeProductWidgetWsDTO.setImageURL(StringUtils.EMPTY);
							}
							if (themeProductWidgetComponentModel.getBrandLogo() != null
									&& themeProductWidgetComponentModel.getBrandLogo().getURL() != null)
							{
								themeProductWidgetWsDTO.setBrandLogo(themeProductWidgetComponentModel.getBrandLogo().getURL());
							}
							else
							{
								themeProductWidgetWsDTO.setBrandLogo(StringUtils.EMPTY);
							}
							themeProductWidgetWsDTO.setBtnText(null != themeProductWidgetComponentModel.getBtnText()
									? themeProductWidgetComponentModel.getBtnText() : StringUtils.EMPTY);
							themeProductWidgetWsDTO.setItems(themeProWidElementList);
							themeProductWidgetWsDTO.setTitle(null != themeProductWidgetComponentModel.getTitle()
									? themeProductWidgetComponentModel.getTitle() : StringUtils.EMPTY);
							themeProductWidgetWsDTO.setType("Multi Click Component");
							themeProductWidgetWsDTO.setComponentId(null != themeProductWidgetComponentModel.getUid()
									? themeProductWidgetComponentModel.getUid() : StringUtils.EMPTY);

						}
						catch (final EtailNonBusinessExceptions e)
						{
							themeProductWidgetWsDTO.setComponentId(null != themeProductWidgetComponentModel.getUid()
									? themeProductWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(tpwError + themeProductWidgetComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							themeProductWidgetWsDTO.setComponentId(null != themeProductWidgetComponentModel.getUid()
									? themeProductWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(tpwError + themeProductWidgetComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setMultiClickComponent(themeProductWidgetWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("multiClickComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					/*
					 * if (abstractCMSComponentModel instanceof ProductCapsulesComponentModel) { final
					 * ProductCapsulesComponentModel productCapsulesComponentModel = (ProductCapsulesComponentModel)
					 * abstractCMSComponentModel; final List<ProductCapsulesElementWsDTO> productCapsulesElementList = new
					 * ArrayList<ProductCapsulesElementWsDTO>(); final ProductCapsulesWsDTO productCapsulesWsDTO = new
					 * ProductCapsulesWsDTO();
					 *
					 * if (null != productCapsulesComponentModel.getItems() &&
					 * productCapsulesComponentModel.getItems().size() > 0) { for (final ProductCapsulesElementModel
					 * productCapsulesElementModel : productCapsulesComponentModel .getItems()) { final
					 * ProductCapsulesElementWsDTO productCapsulesElementWsDTO = new ProductCapsulesElementWsDTO(); if (null
					 * != productCapsulesElementModel.getImageURL() && null !=
					 * productCapsulesElementModel.getImageURL().getURL()) {
					 * productCapsulesElementWsDTO.setImageURL(productCapsulesElementModel.getImageURL().getURL()); } else {
					 * productCapsulesElementWsDTO.setImageURL(StringUtils.EMPTY); } productCapsulesElementWsDTO.setWebURL(
					 * null != productCapsulesElementModel.getWebURL() ? productCapsulesElementModel.getWebURL() :
					 * StringUtils.EMPTY); productCapsulesElementList.add(productCapsulesElementWsDTO); } }
					 * productCapsulesWsDTO.setBtnText( null != productCapsulesComponentModel.getBtnText() ?
					 * productCapsulesComponentModel.getBtnText() : StringUtils.EMPTY);
					 * productCapsulesWsDTO.setDescription(null != productCapsulesComponentModel.getDescription() ?
					 * productCapsulesComponentModel.getDescription() : StringUtils.EMPTY);
					 * productCapsulesWsDTO.setItems(productCapsulesElementList); productCapsulesWsDTO.setTitle( null !=
					 * productCapsulesComponentModel.getTitle() ? productCapsulesComponentModel.getTitle() :
					 * StringUtils.EMPTY); productCapsulesWsDTO .setType(null != productCapsulesComponentModel.getName() ?
					 * productCapsulesComponentModel.getName() : StringUtils.EMPTY); productCapsulesWsDTO.setWebURL( null !=
					 * productCapsulesComponentModel.getWebURL() ? productCapsulesComponentModel.getWebURL() :
					 * StringUtils.EMPTY); uiCompPageElementWsDTO.setProductCapsules(productCapsulesWsDTO);
					 * genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
					 * uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO); uiComponentWiseWsDTO.setMessage(Success);
					 * uiComponentWiseWsDTO.setStatus(pageComponent); return uiComponentWiseWsDTO; }
					 */

					if (abstractCMSComponentModel instanceof BannerSeparatorComponentModel)
					{
						final BannerSeparatorWsDTO bannerSeperatorWsDTO = new BannerSeparatorWsDTO();
						final BannerSeparatorComponentModel bannerSeparatorComponentModel = (BannerSeparatorComponentModel) abstractCMSComponentModel;
						try
						{
							if (null != bannerSeparatorComponentModel.getIconImageURL()
									&& null != bannerSeparatorComponentModel.getIconImageURL().getURL())
							{
								bannerSeperatorWsDTO.setIconImageURL(bannerSeparatorComponentModel.getIconImageURL().getURL());
							}
							else
							{
								bannerSeperatorWsDTO.setIconImageURL(StringUtils.EMPTY);
							}
							bannerSeperatorWsDTO.setEndHexCode(null != bannerSeparatorComponentModel.getEndHexCode()
									? bannerSeparatorComponentModel.getEndHexCode() : StringUtils.EMPTY);
							bannerSeperatorWsDTO.setStartHexCode(null != bannerSeparatorComponentModel.getStartHexCode()
									? bannerSeparatorComponentModel.getStartHexCode() : StringUtils.EMPTY);
							bannerSeperatorWsDTO.setDescription(null != bannerSeparatorComponentModel.getDescription()
									? bannerSeparatorComponentModel.getDescription() : StringUtils.EMPTY);
							bannerSeperatorWsDTO.setTitle(null != bannerSeparatorComponentModel.getTitle()
									? bannerSeparatorComponentModel.getTitle() : StringUtils.EMPTY);
							bannerSeperatorWsDTO.setType("Banner Separator Component");
							bannerSeperatorWsDTO.setComponentId(null != bannerSeparatorComponentModel.getUid()
									? bannerSeparatorComponentModel.getUid() : StringUtils.EMPTY);
							bannerSeperatorWsDTO.setWebURL(null != bannerSeparatorComponentModel.getWebURL()
									? bannerSeparatorComponentModel.getWebURL() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							bannerSeperatorWsDTO.setComponentId(null != bannerSeparatorComponentModel.getUid()
									? bannerSeparatorComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(BannerSeparatorError + bannerSeparatorComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							bannerSeperatorWsDTO.setComponentId(null != bannerSeparatorComponentModel.getUid()
									? bannerSeparatorComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(BannerSeparatorError + bannerSeparatorComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setBannerSeparatorComponent(bannerSeperatorWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("bannerSeparatorComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof AutomatedBrandProductCarouselComponentModel)
					{
						final AutomatedBrandProductCarouselComponentModel automatedBrandProCarCompModel = (AutomatedBrandProductCarouselComponentModel) abstractCMSComponentModel;
						final List<AutomatedBrandProCarEleWsDTO> automatedBrandProCarEleList = new ArrayList<AutomatedBrandProCarEleWsDTO>();
						final AutomatedBrandProCarWsDTO automatedBrandProCarWsDTO = new AutomatedBrandProCarWsDTO();
						try
						{
							if (automatedBrandProCarCompModel.getItems() != null && automatedBrandProCarCompModel.getItems().size() > 0)
							{
								for (final AutomatedBrandProductCarElementModel productObj : automatedBrandProCarCompModel.getItems())
								{
									try
									{
										final AutomatedBrandProCarEleWsDTO automatedBrandProCarEleWsDTO = new AutomatedBrandProCarEleWsDTO();
										String productCode = StringUtils.EMPTY;
										try
										{
											if (null != productObj && null != productObj.getProductCode()
													&& null != productObj.getProductCode().getCode())
											{
												productCode = productObj.getProductCode().getCode();
												final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productObj.getProductCode().getCode());
												String productUnitPrice = StringUtils.EMPTY;
												String productPrice = StringUtils.EMPTY;

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

												autoDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
												autoDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
												if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
												{
													autoDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
													autoMrpPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
												}
												autoDiscountPriceWsDTO.setCurrencySymbol("₹");
												autoMrpPriceWsDTO.setCurrencySymbol("₹");
												autoMrpPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
												autoMrpPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));
												automatedBrandProCarEleWsDTO.setPrdId(productObj.getProductCode().getCode());
												automatedBrandProCarEleWsDTO.setMrpPrice(autoMrpPriceWsDTO);
												automatedBrandProCarEleWsDTO.setDiscountedPrice(autoDiscountPriceWsDTO);
												automatedBrandProCarEleWsDTO.setTitle(null != productObj.getProductCode()
														? productObj.getProductCode().getName() : StringUtils.EMPTY);
												if (null != productModelUrlResolver)
												{
													automatedBrandProCarEleWsDTO
															.setWebURL(siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
												}
												else
												{
													automatedBrandProCarEleWsDTO
															.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
												}
												if (productObj.getProductCode() != null && productObj.getProductCode().getPicture() != null
														&& productObj.getProductCode().getPicture().getURL() != null)
												{
													automatedBrandProCarEleWsDTO
															.setImageURL(productObj.getProductCode().getPicture().getURL());
												}
												else
												{
													automatedBrandProCarEleWsDTO.setImageURL(StringUtils.EMPTY);
												}
											}
											automatedBrandProCarEleList.add(automatedBrandProCarEleWsDTO);
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(AutomatedBrandProCarError + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(AutomatedBrandProCarError + productCode, e);
											continue;
										}
									}
									catch (final EtailNonBusinessExceptions e)
									{
										automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
												? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
										LOG.error(abpcError + automatedBrandProCarCompModel.getUid(), e);
										continue;
									}
									catch (final Exception e)
									{
										automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
												? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
										LOG.error(abpcError + automatedBrandProCarCompModel.getUid(), e);
										continue;
									}
								}
							}
							if (null != automatedBrandProCarCompModel.getBrandLogo()
									&& null != automatedBrandProCarCompModel.getBrandLogo().getURL())
							{
								automatedBrandProCarWsDTO.setBrandLogo(automatedBrandProCarCompModel.getBrandLogo().getURL());
							}
							else
							{
								automatedBrandProCarWsDTO.setBrandLogo(StringUtils.EMPTY);
							}
							automatedBrandProCarWsDTO.setBtnText(null != automatedBrandProCarCompModel.getBtnText()
									? automatedBrandProCarCompModel.getBtnText() : StringUtils.EMPTY);
							automatedBrandProCarWsDTO.setDescription(null != automatedBrandProCarCompModel.getDescription()
									? automatedBrandProCarCompModel.getDescription() : StringUtils.EMPTY);
							if (automatedBrandProCarCompModel.getImageURL() != null
									&& automatedBrandProCarCompModel.getImageURL().getURL() != null)
							{
								automatedBrandProCarWsDTO.setImageURL(automatedBrandProCarCompModel.getImageURL().getURL());
							}
							else
							{
								automatedBrandProCarWsDTO.setImageURL(StringUtils.EMPTY);
							}
							automatedBrandProCarWsDTO.setItems(automatedBrandProCarEleList);
							automatedBrandProCarWsDTO.setType("Automated Banner Product Carousel Component");
							automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
									? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
							automatedBrandProCarWsDTO.setWebURL(null != automatedBrandProCarCompModel.getWebURL()
									? automatedBrandProCarCompModel.getWebURL() : StringUtils.EMPTY);

						}
						catch (final EtailNonBusinessExceptions e)
						{
							automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
									? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(abpcError + automatedBrandProCarCompModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							automatedBrandProCarWsDTO.setComponentId(null != automatedBrandProCarCompModel.getUid()
									? automatedBrandProCarCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(abpcError + automatedBrandProCarCompModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setAutomatedBannerProductCarouselComponent(automatedBrandProCarWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("automatedBannerProductCarouselComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof CuratedListingStripComponentModel)
					{
						final CuratedListingStripComponentModel curatedListStripCompModel = (CuratedListingStripComponentModel) abstractCMSComponentModel;
						final CuratedListingStripWsDTO curatedListingStripWsDTO = new CuratedListingStripWsDTO();
						try
						{
							curatedListingStripWsDTO.setStartHexCode(null != curatedListStripCompModel.getStartHexCode()
									? curatedListStripCompModel.getStartHexCode() : StringUtils.EMPTY);
							curatedListingStripWsDTO.setTitle(null != curatedListStripCompModel.getTitle()
									? curatedListStripCompModel.getTitle() : StringUtils.EMPTY);
							curatedListingStripWsDTO.setType("Curated Listing Strip Component");
							curatedListingStripWsDTO.setComponentId(
									null != curatedListStripCompModel.getUid() ? curatedListStripCompModel.getUid() : StringUtils.EMPTY);
							curatedListingStripWsDTO.setWebURL(null != curatedListStripCompModel.getWebURL()
									? curatedListStripCompModel.getWebURL() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							curatedListingStripWsDTO.setComponentId(
									null != curatedListStripCompModel.getUid() ? curatedListStripCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(CuratedListingStripError + curatedListStripCompModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							curatedListingStripWsDTO.setComponentId(
									null != curatedListStripCompModel.getUid() ? curatedListStripCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(CuratedListingStripError + curatedListStripCompModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setCuratedListingStripComponent(curatedListingStripWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("curatedListingStripComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof MonoBLPBannerComponentModel)
					{
						final MonoBLPBannerComponentModel monoBLPBannerComponentModel = (MonoBLPBannerComponentModel) abstractCMSComponentModel;
						final List<MonoBLPBannerElementWsDTO> monoBLPBannerElementList = new ArrayList<MonoBLPBannerElementWsDTO>();
						final MonoBLPBannerWsDTO moBannerWsDTO = new MonoBLPBannerWsDTO();
						try
						{
							if (null != monoBLPBannerComponentModel.getItems() && monoBLPBannerComponentModel.getItems().size() > 0)
							{
								for (final MonoBLPBannerElementModel monoBLPBannerElementModel : monoBLPBannerComponentModel.getItems())
								{
									final MonoBLPBannerElementWsDTO monoBLPBannerElementWsDTO = new MonoBLPBannerElementWsDTO();
									monoBLPBannerElementWsDTO.setBtnText(null != monoBLPBannerElementModel.getBtnText()
											? monoBLPBannerElementModel.getBtnText() : StringUtils.EMPTY);
									monoBLPBannerElementWsDTO.setHexCode(null != monoBLPBannerElementModel.getHexCode()
											? monoBLPBannerElementModel.getHexCode() : StringUtils.EMPTY);
									monoBLPBannerElementWsDTO.setTitle(null != monoBLPBannerElementModel.getTitle()
											? monoBLPBannerElementModel.getTitle() : StringUtils.EMPTY);
									monoBLPBannerElementWsDTO.setWebURL(null != monoBLPBannerElementModel.getWebURL()
											? monoBLPBannerElementModel.getWebURL() : StringUtils.EMPTY);
									if (monoBLPBannerElementModel.getImageURL() != null
											&& monoBLPBannerElementModel.getImageURL().getURL() != null)
									{
										monoBLPBannerElementWsDTO.setImageURL(monoBLPBannerElementModel.getImageURL().getURL());
									}
									else
									{
										monoBLPBannerElementWsDTO.setImageURL(StringUtils.EMPTY);
									}
									monoBLPBannerElementList.add(monoBLPBannerElementWsDTO);
								}
							}
							moBannerWsDTO.setType("Single Banner Component");
							moBannerWsDTO.setComponentId(null != monoBLPBannerComponentModel.getUid()
									? monoBLPBannerComponentModel.getUid() : StringUtils.EMPTY);
							moBannerWsDTO.setItems(monoBLPBannerElementList);
							moBannerWsDTO.setTitle(null != monoBLPBannerComponentModel.getTitle()
									? monoBLPBannerComponentModel.getTitle() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							moBannerWsDTO.setComponentId(null != monoBLPBannerComponentModel.getUid()
									? monoBLPBannerComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(MonoBLPBannerError + monoBLPBannerComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							moBannerWsDTO.setComponentId(null != monoBLPBannerComponentModel.getUid()
									? monoBLPBannerComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(MonoBLPBannerError + monoBLPBannerComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setSingleBannerComponent(moBannerWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("singleBannerComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof SubBrandBannerBLPComponentModel)
					{
						final SubBrandBannerBLPComponentModel subBrandBLPBannerCompModel = (SubBrandBannerBLPComponentModel) abstractCMSComponentModel;
						final List<SubBrandBannerBLPElementWsDTO> subBrandBannerBLPEleList = new ArrayList<SubBrandBannerBLPElementWsDTO>();
						final SubBrandBannerBLPWsDTO subBrandBannerBLPWsDTO = new SubBrandBannerBLPWsDTO();
						try
						{
							if (null != subBrandBLPBannerCompModel.getItems() && subBrandBLPBannerCompModel.getItems().size() > 0)
							{
								for (final SubBrandBannerBLPElementModel subBannerBLPElementModel : subBrandBLPBannerCompModel.getItems())
								{
									final SubBrandBannerBLPElementWsDTO subBannerBLPEleWsDTO = new SubBrandBannerBLPElementWsDTO();
									subBannerBLPEleWsDTO.setWebURL(null != subBannerBLPElementModel.getWebURL()
											? subBannerBLPElementModel.getWebURL() : StringUtils.EMPTY);
									if (subBannerBLPElementModel.getImageURL() != null
											&& subBannerBLPElementModel.getImageURL().getURL() != null)
									{
										subBannerBLPEleWsDTO.setImageURL(subBannerBLPElementModel.getImageURL().getURL());
									}
									else
									{
										subBannerBLPEleWsDTO.setImageURL(StringUtils.EMPTY);
									}
									if (subBannerBLPElementModel.getBrandLogo() != null
											&& subBannerBLPElementModel.getBrandLogo().getURL() != null)
									{
										subBannerBLPEleWsDTO.setBrandLogo(subBannerBLPElementModel.getBrandLogo().getURL());
									}
									else
									{
										subBannerBLPEleWsDTO.setBrandLogo(StringUtils.EMPTY);
									}
									subBrandBannerBLPEleList.add(subBannerBLPEleWsDTO);
								}
							}
							subBrandBannerBLPWsDTO.setType("Sub Brands Banner Component");
							subBrandBannerBLPWsDTO.setComponentId(
									null != subBrandBLPBannerCompModel.getUid() ? subBrandBLPBannerCompModel.getUid() : StringUtils.EMPTY);
							subBrandBannerBLPWsDTO.setItems(subBrandBannerBLPEleList);
							subBrandBannerBLPWsDTO.setTitle(null != subBrandBLPBannerCompModel.getTitle()
									? subBrandBLPBannerCompModel.getTitle() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							subBrandBannerBLPWsDTO.setComponentId(
									null != subBrandBLPBannerCompModel.getUid() ? subBrandBLPBannerCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(SubBrandBLPBannerError + subBrandBLPBannerCompModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							subBrandBannerBLPWsDTO.setComponentId(
									null != subBrandBLPBannerCompModel.getUid() ? subBrandBLPBannerCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(SubBrandBLPBannerError + subBrandBLPBannerCompModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setSubBrandsBannerComponent(subBrandBannerBLPWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("subBrandsBannerComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof TopCategoriesWidgetComponentModel)
					{
						final TopCategoriesWidgetComponentModel topCategoriesWidgetComponentModel = (TopCategoriesWidgetComponentModel) abstractCMSComponentModel;
						final List<TopCategoriesWidgetElementWsDTO> topCategoriesWidgetElementList = new ArrayList<TopCategoriesWidgetElementWsDTO>();
						final TopCategoriesWidgetWsDTO topCategoriesWidgetWsDTO = new TopCategoriesWidgetWsDTO();
						try
						{
							if (null != topCategoriesWidgetComponentModel.getItems()
									&& topCategoriesWidgetComponentModel.getItems().size() > 0)
							{
								for (final TopCategoriesWidgetElementModel topCategoriesWidgetElementModel : topCategoriesWidgetComponentModel
										.getItems())
								{
									final TopCategoriesWidgetElementWsDTO topCategoriesWidgetElementWsDTO = new TopCategoriesWidgetElementWsDTO();
									topCategoriesWidgetElementWsDTO.setWebURL(null != topCategoriesWidgetElementModel.getWebURL()
											? topCategoriesWidgetElementModel.getWebURL() : StringUtils.EMPTY);
									if (topCategoriesWidgetElementModel.getImageURL() != null
											&& topCategoriesWidgetElementModel.getImageURL().getURL() != null)
									{
										topCategoriesWidgetElementWsDTO.setImageURL(topCategoriesWidgetElementModel.getImageURL().getURL());
									}
									else
									{
										topCategoriesWidgetElementWsDTO.setImageURL(StringUtils.EMPTY);
									}
									topCategoriesWidgetElementWsDTO.setTitle(null != topCategoriesWidgetElementModel.getTitle()
											? topCategoriesWidgetElementModel.getTitle() : StringUtils.EMPTY);
									topCategoriesWidgetElementList.add(topCategoriesWidgetElementWsDTO);
								}
							}
							topCategoriesWidgetWsDTO.setType("Top Categories Component");
							topCategoriesWidgetWsDTO.setComponentId(null != topCategoriesWidgetComponentModel.getUid()
									? topCategoriesWidgetComponentModel.getUid() : StringUtils.EMPTY);
							topCategoriesWidgetWsDTO.setItems(topCategoriesWidgetElementList);
							topCategoriesWidgetWsDTO.setTitle(null != topCategoriesWidgetComponentModel.getTitle()
									? topCategoriesWidgetComponentModel.getTitle() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							topCategoriesWidgetWsDTO.setComponentId(null != topCategoriesWidgetComponentModel.getUid()
									? topCategoriesWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(TopCategoriesWidgetEror + topCategoriesWidgetComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							topCategoriesWidgetWsDTO.setComponentId(null != topCategoriesWidgetComponentModel.getUid()
									? topCategoriesWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(TopCategoriesWidgetEror + topCategoriesWidgetComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setTopCategoriesComponent(topCategoriesWidgetWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("topCategoriesComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof CuratedProductsWidgetComponentModel)
					{
						final CuratedProductsWidgetComponentModel curatedProWidgetCompModel = (CuratedProductsWidgetComponentModel) abstractCMSComponentModel;
						final List<CuratedProWidgetElementWsDTO> curatedProWidgetElementList = new ArrayList<CuratedProWidgetElementWsDTO>();
						final CuratedProductsWidgetWsDTO curatedProductsWidgetWsDTO = new CuratedProductsWidgetWsDTO();
						try
						{
							if (null != curatedProWidgetCompModel.getItems() && curatedProWidgetCompModel.getItems().size() > 0)
							{
								String productCode = StringUtils.EMPTY;
								try
								{
									for (final CuratedProductsWidgetElementModel productObj : curatedProWidgetCompModel.getItems())
									{
										try
										{
											final CuratedProWidgetElementWsDTO curatedProWidgetElementWsDTO = new CuratedProWidgetElementWsDTO();
											if (null != productObj && null != productObj.getProductCode()
													&& null != productObj.getProductCode().getCode())
											{
												productCode = productObj.getProductCode().getCode();
												final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productObj.getProductCode().getCode());
												String productUnitPrice = StringUtils.EMPTY;
												String productPrice = StringUtils.EMPTY;

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

												curatedProWidgetEleDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
												curatedProWidgetEleDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
												if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
												{
													curatedProWidgetEleMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
													curatedProWidgetEleDiscountPriceWsDTO
															.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
												}
												curatedProWidgetEleDiscountPriceWsDTO.setCurrencySymbol("₹");
												curatedProWidgetEleMRPPriceWsDTO.setCurrencySymbol("₹");
												curatedProWidgetEleMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
												curatedProWidgetEleMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));

												curatedProWidgetElementWsDTO.setPrdId(productObj.getProductCode().getCode());
												curatedProWidgetElementWsDTO.setMrpPrice(curatedProWidgetEleMRPPriceWsDTO);
												curatedProWidgetElementWsDTO.setDiscountedPrice(curatedProWidgetEleDiscountPriceWsDTO);
												curatedProWidgetElementWsDTO.setTitle(null != productObj.getProductCode()
														? productObj.getProductCode().getName() : StringUtils.EMPTY);
												if (null != productModelUrlResolver)
												{
													curatedProWidgetElementWsDTO
															.setWebURL(siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
												}
												else
												{
													curatedProWidgetElementWsDTO
															.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
												}
												if (null != productObj.getProductCode().getPicture()
														&& null != productObj.getProductCode().getPicture().getURL())
												{
													curatedProWidgetElementWsDTO
															.setImageURL(productObj.getProductCode().getPicture().getURL());
												}
												else
												{
													curatedProWidgetElementWsDTO.setImageURL(StringUtils.EMPTY);
												}
											}
											curatedProWidgetElementList.add(curatedProWidgetElementWsDTO);
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(CuratedProductWidgetEror + productCode, e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(CuratedProductWidgetEror + productCode, e);
											continue;
										}
									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									LOG.error(CuratedProductWidgetEror + productCode, e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(CuratedProductWidgetEror + productCode, e);
									continue;
								}
							}
							curatedProductsWidgetWsDTO.setBtnText(null != curatedProWidgetCompModel.getBtnText()
									? curatedProWidgetCompModel.getBtnText() : StringUtils.EMPTY);
							curatedProductsWidgetWsDTO.setItems(curatedProWidgetElementList);
							curatedProductsWidgetWsDTO.setTitle(null != curatedProWidgetCompModel.getTitle()
									? curatedProWidgetCompModel.getTitle() : StringUtils.EMPTY);
							curatedProductsWidgetWsDTO.setType("Curated Products Component");
							curatedProductsWidgetWsDTO.setComponentId(
									null != curatedProWidgetCompModel.getUid() ? curatedProWidgetCompModel.getUid() : StringUtils.EMPTY);
							curatedProductsWidgetWsDTO.setWebURL(null != curatedProWidgetCompModel.getWebURL()
									? curatedProWidgetCompModel.getWebURL() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							curatedProductsWidgetWsDTO.setComponentId(
									null != curatedProWidgetCompModel.getUid() ? curatedProWidgetCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(cpwError + curatedProWidgetCompModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							curatedProductsWidgetWsDTO.setComponentId(
									null != curatedProWidgetCompModel.getUid() ? curatedProWidgetCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(cpwError + curatedProWidgetCompModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setCuratedProductsComponent(curatedProductsWidgetWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("curatedProductsComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof SmartFilterWidgetComponentModel)
					{
						final SmartFilterWidgetWsDTO smartFilterWsDTO = new SmartFilterWidgetWsDTO();
						final List<SmartFilterWidgetElementWsDTO> smartFilterWidgetElementList = new ArrayList<SmartFilterWidgetElementWsDTO>();
						final SmartFilterWidgetComponentModel smartFilterWidgetComponentModel = (SmartFilterWidgetComponentModel) abstractCMSComponentModel;
						try
						{
							if (null != smartFilterWidgetComponentModel.getItems()
									&& smartFilterWidgetComponentModel.getItems().size() > 0)
							{
								for (final SmartFilterWidgetElementModel smartFilterWidgetElementModel : smartFilterWidgetComponentModel
										.getItems())
								{
									final SmartFilterWidgetElementWsDTO smartFilterWidgetElementWsDTO = new SmartFilterWidgetElementWsDTO();
									smartFilterWidgetElementWsDTO.setDescription(null != smartFilterWidgetElementModel.getDescription()
											? smartFilterWidgetElementModel.getDescription() : StringUtils.EMPTY);
									if (smartFilterWidgetElementModel.getImageURL() != null
											&& smartFilterWidgetElementModel.getImageURL().getURL() != null)
									{
										smartFilterWidgetElementWsDTO.setImageURL(smartFilterWidgetElementModel.getImageURL().getURL());
									}
									else
									{
										smartFilterWidgetElementWsDTO.setImageURL(StringUtils.EMPTY);
									}
									smartFilterWidgetElementWsDTO.setTitle(null != smartFilterWidgetElementModel.getTitle()
											? smartFilterWidgetElementModel.getTitle() : StringUtils.EMPTY);
									smartFilterWidgetElementWsDTO.setWebURL(null != smartFilterWidgetElementModel.getWebURL()
											? smartFilterWidgetElementModel.getWebURL() : StringUtils.EMPTY);
									smartFilterWidgetElementList.add(smartFilterWidgetElementWsDTO);
								}
							}
							smartFilterWsDTO.setItems(smartFilterWidgetElementList);
							smartFilterWsDTO.setTitle(null != smartFilterWidgetComponentModel.getTitle()
									? smartFilterWidgetComponentModel.getTitle() : StringUtils.EMPTY);
							smartFilterWsDTO.setType("Two by Two Banner Component");
							smartFilterWsDTO.setComponentId(null != smartFilterWidgetComponentModel.getUid()
									? smartFilterWidgetComponentModel.getUid() : StringUtils.EMPTY);

						}
						catch (final EtailNonBusinessExceptions e)
						{
							smartFilterWsDTO.setComponentId(null != smartFilterWidgetComponentModel.getUid()
									? smartFilterWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(SmartFilterWidgetError + smartFilterWidgetComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							smartFilterWsDTO.setComponentId(null != smartFilterWidgetComponentModel.getUid()
									? smartFilterWidgetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(SmartFilterWidgetError + smartFilterWidgetComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setTwoByTwoBannerComponent(smartFilterWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("twoByTwoBannerComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}
					if (abstractCMSComponentModel instanceof MSDComponentModel)
					{
						final MSDComponentWsDTO msdComponentWsDTO = new MSDComponentWsDTO();
						final MSDComponentModel msdComponentModel = (MSDComponentModel) abstractCMSComponentModel;
						try
						{
							msdComponentWsDTO
									.setDetails(null != msdComponentModel.getDetails() ? msdComponentModel.getDetails() : Boolean.FALSE);
							msdComponentWsDTO.setNum_results(
									null != msdComponentModel.getNum_results() ? msdComponentModel.getNum_results() : Integer.valueOf(0));
							msdComponentWsDTO.setSubType(
									null != msdComponentModel.getSubType() ? msdComponentModel.getSubType() : StringUtils.EMPTY);
							msdComponentWsDTO.setType("MSD Component");
							msdComponentWsDTO
									.setComponentId(null != msdComponentModel.getUid() ? msdComponentModel.getUid() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							msdComponentWsDTO
									.setComponentId(null != msdComponentModel.getUid() ? msdComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(MsdError + msdComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							msdComponentWsDTO
									.setComponentId(null != msdComponentModel.getUid() ? msdComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(MsdError + msdComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setMsdComponent(msdComponentWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("msdComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof AdobeTargetComponentModel)
					{
						final AdobeTargetComponentWsDTO adobeTargetComponentWsDTO = new AdobeTargetComponentWsDTO();
						final AdobeTargetComponentModel adobeTargetComponentModel = (AdobeTargetComponentModel) abstractCMSComponentModel;
						try
						{
							adobeTargetComponentWsDTO.setMbox(
									null != adobeTargetComponentModel.getMbox() ? adobeTargetComponentModel.getMbox() : StringUtils.EMPTY);
							adobeTargetComponentWsDTO.setType("Adobe Target Component");
							adobeTargetComponentWsDTO.setComponentId(
									null != adobeTargetComponentModel.getUid() ? adobeTargetComponentModel.getUid() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							adobeTargetComponentWsDTO.setComponentId(
									null != adobeTargetComponentModel.getUid() ? adobeTargetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(AdobeTargetError + adobeTargetComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							adobeTargetComponentWsDTO.setComponentId(
									null != adobeTargetComponentModel.getUid() ? adobeTargetComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(AdobeTargetError + adobeTargetComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setAdobeTargetComponent(adobeTargetComponentWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("adobeTargetComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}
					if (abstractCMSComponentModel instanceof BrandsTabAZListComponentModel)
					{
						final BrandsTabAZListComponentWsDTO brandsTabAZListComponentWsDTO = new BrandsTabAZListComponentWsDTO();
						final BrandsTabAZListComponentModel brandsTabAZListComponentModel = (BrandsTabAZListComponentModel) abstractCMSComponentModel;
						final List<BrandsTabAZListWsDTO> brandsTabAZList = new ArrayList<BrandsTabAZListWsDTO>();
						try
						{
							for (final BrandsTabAZElementModel brandsTabAZElementModel : brandsTabAZListComponentModel.getItems())
							{
								final List<BrandsTabAZHeroBannerWsDTO> heroBannerCompList = new ArrayList<BrandsTabAZHeroBannerWsDTO>();
								final BrandsTabAZHeroBannerWsDTO brandsTabAZHeroBannerWsDTO = new BrandsTabAZHeroBannerWsDTO();
								final BrandsTabAZListWsDTO brandsTabAZListWsDTO = new BrandsTabAZListWsDTO();
								brandsTabAZListWsDTO.setSubType(null != brandsTabAZElementModel.getSubType()
										? brandsTabAZElementModel.getSubType() : StringUtils.EMPTY);
								for (final HeroBannerComponentModel heroBannerComponentModel : brandsTabAZElementModel.getItems())
								{
									final HeroBannerCompWsDTO heroBannerCompWsDTO = new HeroBannerCompWsDTO();
									final List<HeroBannerCompListWsDTO> heroBannerCompListWsDTO = new ArrayList<HeroBannerCompListWsDTO>();
									if (null != heroBannerComponentModel.getItems() && heroBannerComponentModel.getItems().size() > 0)
									{
										for (final HeroBannerElementModel heroBannerElementModel : heroBannerComponentModel.getItems())
										{
											final HeroBannerCompListWsDTO heroBannerCompListObj = new HeroBannerCompListWsDTO();
											if (null != heroBannerElementModel.getImageURL()
													&& null != heroBannerElementModel.getImageURL().getURL())
											{
												heroBannerCompListObj.setImageURL(heroBannerElementModel.getImageURL().getURL());
											}
											else
											{
												heroBannerCompListObj.setImageURL(StringUtils.EMPTY);
											}
											if (null != heroBannerElementModel.getBrandLogo()
													&& null != heroBannerElementModel.getBrandLogo().getURL())
											{
												heroBannerCompListObj.setBrandLogo(heroBannerElementModel.getBrandLogo().getURL());
											}
											else
											{
												heroBannerCompListObj.setBrandLogo(StringUtils.EMPTY);
											}
											heroBannerCompListObj.setTitle(null != heroBannerElementModel.getTitle()
													? heroBannerElementModel.getTitle() : StringUtils.EMPTY);
											heroBannerCompListObj.setWebURL(null != heroBannerElementModel.getWebURL()
													? heroBannerElementModel.getWebURL() : StringUtils.EMPTY);
											heroBannerCompListWsDTO.add(heroBannerCompListObj);
										}

									}
									heroBannerCompWsDTO.setItems(heroBannerCompListWsDTO);
									heroBannerCompWsDTO.setType(heroBannerComp);
									brandsTabAZHeroBannerWsDTO.setHeroBannerComponent(heroBannerCompWsDTO);
									heroBannerCompList.add(brandsTabAZHeroBannerWsDTO);
								}
								final List<BrandsTabAZListElementWsDTO> brandsTabAZElementList = new ArrayList<BrandsTabAZListElementWsDTO>();
								for (final BrandTabAZBrandElementModel brandTabAZBrandElementModel : brandsTabAZElementModel.getBrands())
								{
									final BrandsTabAZListElementWsDTO brandsTabAZListElement = new BrandsTabAZListElementWsDTO();
									brandsTabAZListElement.setBrandName(null != brandTabAZBrandElementModel.getBrandName()
											? brandTabAZBrandElementModel.getBrandName() : StringUtils.EMPTY);
									brandsTabAZListElement.setWebURL(null != brandTabAZBrandElementModel.getWebURL()
											? brandTabAZBrandElementModel.getWebURL() : StringUtils.EMPTY);
									brandsTabAZElementList.add(brandsTabAZListElement);
								}
								brandsTabAZListWsDTO.setItems(heroBannerCompList);
								brandsTabAZListWsDTO.setBrands(brandsTabAZElementList);
								brandsTabAZList.add(brandsTabAZListWsDTO);

							}
							brandsTabAZListComponentWsDTO.setItems(brandsTabAZList);
							brandsTabAZListComponentWsDTO.setType("Brands Tab AZ List Component");
							brandsTabAZListComponentWsDTO.setComponentId(null != brandsTabAZListComponentModel.getUid()
									? brandsTabAZListComponentModel.getUid() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							brandsTabAZListComponentWsDTO.setComponentId(null != brandsTabAZListComponentModel.getUid()
									? brandsTabAZListComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(BrandsTabAZListError + brandsTabAZListComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							brandsTabAZListComponentWsDTO.setComponentId(null != brandsTabAZListComponentModel.getUid()
									? brandsTabAZListComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(BrandsTabAZListError + brandsTabAZListComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setComponentName("brandsTabAZListComponent");
						uiCompPageElementWsDTO.setBrandsTabAZListComponent(brandsTabAZListComponentWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}
					if (abstractCMSComponentModel instanceof LandingPageTitleComponentModel)
					{
						final LandingPageTitleComponentModel landingPageTitleCompModel = (LandingPageTitleComponentModel) abstractCMSComponentModel;
						final LandingPageTitleComponentWsDTO landingPageTitleComponentWsDTO = new LandingPageTitleComponentWsDTO();
						try
						{
							landingPageTitleComponentWsDTO.setComponentId(
									null != landingPageTitleCompModel.getUid() ? landingPageTitleCompModel.getUid() : StringUtils.EMPTY);
							landingPageTitleComponentWsDTO.setTitle(null != landingPageTitleCompModel.getTitle()
									? landingPageTitleCompModel.getTitle() : StringUtils.EMPTY);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							landingPageTitleComponentWsDTO.setComponentId(
									null != landingPageTitleCompModel.getUid() ? landingPageTitleCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(LandingPageTitleError + landingPageTitleCompModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							landingPageTitleComponentWsDTO.setComponentId(
									null != landingPageTitleCompModel.getUid() ? landingPageTitleCompModel.getUid() : StringUtils.EMPTY);
							LOG.error(LandingPageTitleError + landingPageTitleCompModel.getUid(), e);
							continue;
						}
						landingPageTitleComponentWsDTO.setType("Landing Page Title Component");
						uiCompPageElementWsDTO.setLandingPageTitleComponent(landingPageTitleComponentWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiCompPageElementWsDTO.setComponentName("landingPageTitleComponent");
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}
					if (abstractCMSComponentModel instanceof LandingPageHeaderComponentModel)
					{
						final LandingPageHeaderComponentModel landingPageHeaderComponentModel = (LandingPageHeaderComponentModel) abstractCMSComponentModel;
						final LandingPageHeaderComponentWsDTO landingPageHeaderComponentWsDTO = new LandingPageHeaderComponentWsDTO();
						final List<LandingPageHeaderListWsDTO> landingPageHeaderList = new ArrayList<LandingPageHeaderListWsDTO>();
						try
						{
							if (null != landingPageHeaderComponentModel.getItems()
									&& landingPageHeaderComponentModel.getItems().size() > 0)
							{
								for (final LandingPageHeaderElementModel landingPageHeaderElementModel : landingPageHeaderComponentModel
										.getItems())
								{
									final LandingPageHeaderListWsDTO landingPageHeaderListWsDTO = new LandingPageHeaderListWsDTO();
									if (landingPageHeaderElementModel.getBrandLogo() != null
											&& landingPageHeaderElementModel.getBrandLogo().getURL() != null)
									{
										landingPageHeaderListWsDTO.setBrandLogo(landingPageHeaderElementModel.getBrandLogo().getURL());
									}
									else
									{
										landingPageHeaderListWsDTO.setBrandLogo(StringUtils.EMPTY);
									}
									landingPageHeaderListWsDTO.setTitle(null != landingPageHeaderElementModel.getTitle()
											? landingPageHeaderElementModel.getTitle() : StringUtils.EMPTY);
									if (landingPageHeaderElementModel.getImageURL() != null
											&& landingPageHeaderElementModel.getImageURL().getURL() != null)
									{
										landingPageHeaderListWsDTO.setImageURL(landingPageHeaderElementModel.getImageURL().getURL());
									}
									else
									{
										landingPageHeaderListWsDTO.setImageURL(StringUtils.EMPTY);
									}

									landingPageHeaderListWsDTO.setWebURL(landingPageHeaderElementModel.getWebURL());
									landingPageHeaderList.add(landingPageHeaderListWsDTO);
								}
							}
							landingPageHeaderComponentWsDTO.setComponentId(null != landingPageHeaderComponentModel.getUid()
									? landingPageHeaderComponentModel.getUid() : StringUtils.EMPTY);
							landingPageHeaderComponentWsDTO.setItems(landingPageHeaderList);
							landingPageHeaderComponentWsDTO.setType("Landing Page Header Component");
						}
						catch (final EtailNonBusinessExceptions e)
						{
							landingPageHeaderComponentWsDTO.setComponentId(null != landingPageHeaderComponentModel.getUid()
									? landingPageHeaderComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(LandingPageHeaderError + landingPageHeaderComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							landingPageHeaderComponentWsDTO.setComponentId(null != landingPageHeaderComponentModel.getUid()
									? landingPageHeaderComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error(LandingPageHeaderError + landingPageHeaderComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setComponentName("landingPageHeaderComponent");
						uiCompPageElementWsDTO.setLandingPageHeaderComponent(landingPageHeaderComponentWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;

					}

					if (abstractCMSComponentModel instanceof AutoProductRecommendationComponentModel)
					{
						final AutoProductRecommendationComponentModel autoProductRecommendationComponentModel = (AutoProductRecommendationComponentModel) abstractCMSComponentModel;
						final AutoProductRecommendationComponentWsDTO autoProductRecommendationComponentWsDTO = new AutoProductRecommendationComponentWsDTO();
						final List<AutoProductRecommendationListWsDTO> autoProductRecommendationList = new ArrayList<AutoProductRecommendationListWsDTO>();
						final AutoProductRecomListPostParamsWsDTO autoProductRecomListPostParamsWsDTO = new AutoProductRecomListPostParamsWsDTO();
						try
						{
							if (null != autoProductRecommendationComponentModel.getItems()
									&& autoProductRecommendationComponentModel.getItems().size() > 0)
							{
								String productCode = StringUtils.EMPTY;
								try
								{
									for (final AutoProductRecommendationElementModel productObj : autoProductRecommendationComponentModel
											.getItems())
									{
										try
										{
											final AutoProductRecommendationListWsDTO autoProductRecommendationListWsDTO = new AutoProductRecommendationListWsDTO();
											if (null != productObj && null != productObj.getProductCode()
													&& null != productObj.getProductCode().getCode())
											{
												productCode = productObj.getProductCode().getCode();
												final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productObj.getProductCode().getCode());
												//f]i;nal DecimalFormat df = new DecimalFormat("0.00");
												String productUnitPrice = StringUtils.EMPTY;
												String productPrice = StringUtils.EMPTY;

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

												final AutoProductRecomDiscountPriceWsDTO autoProductRecomDiscountPriceWsDTO = new AutoProductRecomDiscountPriceWsDTO();
												final AutoProductRecomMRPPriceWsDTO autoProductRecomMRPPriceWsDTO = new AutoProductRecomMRPPriceWsDTO();

												autoProductRecomDiscountPriceWsDTO.setFormattedValue(stringUtil(productPrice));
												autoProductRecomDiscountPriceWsDTO.setDoubleValue(Double.valueOf(productPrice));
												if (buyboxdata.getPrice() != null && buyboxdata.getPrice().getCurrencyIso() != null)
												{
													autoProductRecomMRPPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
													autoProductRecomDiscountPriceWsDTO.setCurrencyIso(buyboxdata.getPrice().getCurrencyIso());
												}
												autoProductRecomDiscountPriceWsDTO.setCurrencySymbol("₹");
												autoProductRecomMRPPriceWsDTO.setCurrencySymbol("₹");
												autoProductRecomMRPPriceWsDTO.setDoubleValue(Double.valueOf(productUnitPrice));
												autoProductRecomMRPPriceWsDTO.setFormattedValue(stringUtil(productUnitPrice));

												autoProductRecommendationListWsDTO.setPrdId(productObj.getProductCode().getCode());
												autoProductRecommendationListWsDTO.setMrpPrice(autoProductRecomMRPPriceWsDTO);
												autoProductRecommendationListWsDTO.setDiscountedPrice(autoProductRecomDiscountPriceWsDTO);
												autoProductRecommendationListWsDTO.setTitle(null != productObj.getProductCode()
														? productObj.getProductCode().getName() : StringUtils.EMPTY);

												if (null != productModelUrlResolver)
												{
													autoProductRecommendationListWsDTO
															.setWebURL(siteUrl + productModelUrlResolver.resolve(productObj.getProductCode()));
												}
												else
												{
													autoProductRecommendationListWsDTO
															.setWebURL(siteUrl + "/" + productObj.getProductCode().getCode());
												}
												if (null != productObj.getProductCode().getPicture()
														&& null != productObj.getProductCode().getPicture().getURL())
												{
													autoProductRecommendationListWsDTO
															.setImageURL(productObj.getProductCode().getPicture().getURL());
												}
												else
												{
													autoProductRecommendationListWsDTO.setImageURL(StringUtils.EMPTY);
												}
											}
											autoProductRecommendationList.add(autoProductRecommendationListWsDTO);
										}
										catch (final EtailNonBusinessExceptions e)
										{
											LOG.error(
													"AutoProductRecommendationComponentModel Product is not properly enriched or either out of stock code:-"
															+ productCode,
													e);
											continue;
										}
										catch (final Exception e)
										{
											LOG.error(
													"AutoProductRecommendationComponentModel Product is not properly enriched or either out of stock code:-"
															+ productCode,
													e);
											continue;
										}
									}
								}
								catch (final EtailNonBusinessExceptions e)
								{
									LOG.error(
											"AutoProductRecommendationComponentModel Product is not properly enriched or either out of stock code:-"
													+ productCode,
											e);
									continue;
								}
								catch (final Exception e)
								{
									LOG.error(
											"AutoProductRecommendationComponentModel Product is not properly enriched or either out of stock code:-"
													+ productCode,
											e);
									continue;
								}
							}
							autoProductRecomListPostParamsWsDTO
									.setWidgetPlatform(null != autoProductRecommendationComponentModel.getWidgetPlatform()
											? autoProductRecommendationComponentModel.getWidgetPlatform() : StringUtils.EMPTY);
							autoProductRecommendationComponentWsDTO
									.setBackupURL(null != autoProductRecommendationComponentModel.getBackupURL()
											? autoProductRecommendationComponentModel.getBackupURL() : StringUtils.EMPTY);
							autoProductRecommendationComponentWsDTO
									.setBtnText(null != autoProductRecommendationComponentModel.getBtnText()
											? autoProductRecommendationComponentModel.getBtnText() : StringUtils.EMPTY);
							autoProductRecommendationComponentWsDTO
									.setComponentId(null != autoProductRecommendationComponentModel.getUid()
											? autoProductRecommendationComponentModel.getUid() : StringUtils.EMPTY);
							autoProductRecommendationComponentWsDTO
									.setFetchURL(null != autoProductRecommendationComponentModel.getFetchURL()
											? autoProductRecommendationComponentModel.getFetchURL() : StringUtils.EMPTY);
							autoProductRecommendationComponentWsDTO.setTitle(null != autoProductRecommendationComponentModel.getTitle()
									? autoProductRecommendationComponentModel.getTitle() : StringUtils.EMPTY);
							autoProductRecommendationComponentWsDTO.setType("Auto Product Recommendation Component");
							autoProductRecommendationComponentWsDTO.setPostParams(autoProductRecomListPostParamsWsDTO);
							autoProductRecommendationComponentWsDTO.setItems(autoProductRecommendationList);
						}
						catch (final EtailNonBusinessExceptions e)
						{
							autoProductRecommendationComponentWsDTO
									.setComponentId(null != autoProductRecommendationComponentModel.getUid()
											? autoProductRecommendationComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error("Error in getting AutoProductRecommendationComponent with id: "
									+ autoProductRecommendationComponentModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							autoProductRecommendationComponentWsDTO
									.setComponentId(null != autoProductRecommendationComponentModel.getUid()
											? autoProductRecommendationComponentModel.getUid() : StringUtils.EMPTY);
							LOG.error("Error in getting AutoProductRecommendationComponent with id: "
									+ autoProductRecommendationComponentModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setComponentName("autoProductRecommendationComponent");
						uiCompPageElementWsDTO.setAutoProductRecommendationComponent(autoProductRecommendationComponentWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						uiComponentWiseWsDTO.setMessage(Success);
						uiComponentWiseWsDTO.setStatus(pageComponent);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof LandingPageHierarchyComponentModel)
					{
						final LandingPageHierarchyComponentModel landingPageHierarchyModel = (LandingPageHierarchyComponentModel) abstractCMSComponentModel;
						final LandingPageHierarchyComponentWsDTO landingPageHierarchyComponentWsDTO = new LandingPageHierarchyComponentWsDTO();
						final List<LandingPageHierarchyListWsDTO> landingPageHierarchyList = new ArrayList<LandingPageHierarchyListWsDTO>();
						try
						{
							if (null != landingPageHierarchyModel.getItems() && landingPageHierarchyModel.getItems().size() > 0)
							{
								for (final LandingPageHierarchyElementModel landingPageHierarchyElementModel : landingPageHierarchyModel
										.getItems())
								{
									final LandingPageHierarchyListWsDTO landingPageHierarchyListWsDTO = new LandingPageHierarchyListWsDTO();
									final List<LandingPageHierarchyItemListWsDTO> landingPageHierarchyItemList = new ArrayList<LandingPageHierarchyItemListWsDTO>();
									landingPageHierarchyListWsDTO.setTitle(null != landingPageHierarchyElementModel.getTitle()
											? landingPageHierarchyElementModel.getTitle() : StringUtils.EMPTY);
									landingPageHierarchyListWsDTO.setWebURL(null != landingPageHierarchyElementModel.getWebURL()
											? landingPageHierarchyElementModel.getWebURL() : StringUtils.EMPTY);
									if (null != landingPageHierarchyElementModel.getItems()
											&& landingPageHierarchyElementModel.getItems().size() > 0)
									{
										for (final LandingPageHierarchyElementListModel landingPageHierarchyElementListModel : landingPageHierarchyElementModel
												.getItems())
										{
											final LandingPageHierarchyItemListWsDTO landingPageHierarchyItemListWsDTO = new LandingPageHierarchyItemListWsDTO();
											landingPageHierarchyItemListWsDTO
													.setTitle(null != landingPageHierarchyElementListModel.getTitle()
															? landingPageHierarchyElementListModel.getTitle() : StringUtils.EMPTY);
											landingPageHierarchyItemListWsDTO
													.setWebURL(null != landingPageHierarchyElementListModel.getWebURL()
															? landingPageHierarchyElementListModel.getWebURL() : StringUtils.EMPTY);
											landingPageHierarchyItemList.add(landingPageHierarchyItemListWsDTO);
										}
									}
									landingPageHierarchyListWsDTO.setItems(landingPageHierarchyItemList);
									landingPageHierarchyList.add(landingPageHierarchyListWsDTO);
								}
							}
							landingPageHierarchyComponentWsDTO.setComponentId(
									null != landingPageHierarchyModel.getUid() ? landingPageHierarchyModel.getUid() : StringUtils.EMPTY);
							landingPageHierarchyComponentWsDTO.setItems(landingPageHierarchyList);
							landingPageHierarchyComponentWsDTO.setTitle(null != landingPageHierarchyModel.getTitle()
									? landingPageHierarchyModel.getTitle() : StringUtils.EMPTY);
							landingPageHierarchyComponentWsDTO.setType("Landing Page Hierarchy Component");
						}
						catch (final EtailNonBusinessExceptions e)
						{
							landingPageHierarchyComponentWsDTO.setComponentId(
									null != landingPageHierarchyModel.getUid() ? landingPageHierarchyModel.getUid() : StringUtils.EMPTY);
							LOG.error(LandingPageHierError + landingPageHierarchyModel.getUid(), e);
							continue;
						}
						catch (final Exception e)
						{
							landingPageHierarchyComponentWsDTO.setComponentId(
									null != landingPageHierarchyModel.getUid() ? landingPageHierarchyModel.getUid() : StringUtils.EMPTY);
							LOG.error(LandingPageHierError + landingPageHierarchyModel.getUid(), e);
							continue;
						}
						uiCompPageElementWsDTO.setComponentName("landingPageHierarchyComponent");
						uiCompPageElementWsDTO.setLandingPageHierarchyComponent(landingPageHierarchyComponentWsDTO);
						genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
						uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
						return uiComponentWiseWsDTO;
					}

					if (abstractCMSComponentModel instanceof CMSParagraphComponentModel)
					{

						final CMSParagraphComponentModel cmsParagraphComponentModel = (CMSParagraphComponentModel) abstractCMSComponentModel;
						if (null != cmsParagraphComponentModel.getVisible() && cmsParagraphComponentModel.getVisible().booleanValue())
						{
							final CMSParagraphComponentWsDTO cmsParagraphComponentWsDTO = new CMSParagraphComponentWsDTO();


							try
							{
								cmsParagraphComponentWsDTO.setContent(null != cmsParagraphComponentModel.getContent()
										? cmsParagraphComponentModel.getContent() : StringUtils.EMPTY);
							}
							catch (final EtailNonBusinessExceptions e)
							{

								LOG.error(cmsParagraphError + cmsParagraphComponentModel.getUid(), e);
								continue;
							}
							catch (final Exception e)
							{
								LOG.error(cmsParagraphError + cmsParagraphComponentModel.getUid(), e);
								continue;
							}
							cmsParagraphComponentWsDTO.setType("CMS Paragraph Component");
							uiCompPageElementWsDTO.setComponentName("cmsParagraphComponent");
							uiCompPageElementWsDTO.setCmsParagraphComponent(cmsParagraphComponentWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
							return uiComponentWiseWsDTO;
						}
					}

					if (abstractCMSComponentModel instanceof CMSTextComponentModel)
					{

						final CMSTextComponentModel cmsTextComponentModel = (CMSTextComponentModel) abstractCMSComponentModel;
						if (null != cmsTextComponentModel.getVisible() && cmsTextComponentModel.getVisible().booleanValue())
						{
							final CMSTextComponentWsDTO cmsTextComponentWsDTO = new CMSTextComponentWsDTO();

							try
							{
								cmsTextComponentWsDTO.setContent(null != cmsTextComponentModel.getTextValue()
										? cmsTextComponentModel.getTextValue() : StringUtils.EMPTY);
							}
							catch (final EtailNonBusinessExceptions e)
							{

								LOG.error(cmsParagraphError + cmsTextComponentModel.getUid(), e);
								continue;
							}
							catch (final Exception e)
							{
								LOG.error(cmsParagraphError + cmsTextComponentModel.getUid(), e);
								continue;
							}
							cmsTextComponentWsDTO.setType("CMS Text Component");
							uiCompPageElementWsDTO.setComponentName("cmsTextComponent");
							uiCompPageElementWsDTO.setCmsTextComponent(cmsTextComponentWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
							uiComponentWiseWsDTO.setMessage(Success);
							uiComponentWiseWsDTO.setStatus(pageComponent);
							return uiComponentWiseWsDTO;
						}
					}

					if (abstractCMSComponentModel instanceof SimpleBannerComponentModel)
					{

						final SimpleBannerComponentModel simpleBannerComponentModel = (SimpleBannerComponentModel) abstractCMSComponentModel;
						if (null != simpleBannerComponentModel.getVisible() && simpleBannerComponentModel.getVisible().booleanValue())
						{
							final SimpleBannerComponentWsDTO simpleBannerComponentWsDTO = new SimpleBannerComponentWsDTO();


							try
							{
								simpleBannerComponentWsDTO.setTitle(null != simpleBannerComponentModel.getTitle()
										? simpleBannerComponentModel.getTitle() : StringUtils.EMPTY);
								simpleBannerComponentWsDTO.setDescription(null != simpleBannerComponentModel.getDescription()
										? simpleBannerComponentModel.getDescription() : StringUtils.EMPTY);
								if (null != simpleBannerComponentModel.getMedia())
								{
									simpleBannerComponentWsDTO.setMedia(null != simpleBannerComponentModel.getMedia().getURL()
											? simpleBannerComponentModel.getMedia().getURL() : StringUtils.EMPTY);
								}
								else
								{
									simpleBannerComponentWsDTO.setMedia(StringUtils.EMPTY);
								}
								simpleBannerComponentWsDTO.setUrlLink(null != simpleBannerComponentModel.getUrlLink()
										? simpleBannerComponentModel.getUrlLink() : StringUtils.EMPTY);
							}
							catch (final EtailNonBusinessExceptions e)
							{

								LOG.error(simpleBannerError + simpleBannerComponentModel.getUid(), e);
								continue;
							}
							catch (final Exception e)
							{
								LOG.error(simpleBannerError + simpleBannerComponentModel.getUid(), e);
								continue;
							}
							simpleBannerComponentWsDTO.setType("Simple Banner Component");
							uiCompPageElementWsDTO.setComponentName("simpleBannerComponent");
							uiCompPageElementWsDTO.setSimpleBannerComponent(simpleBannerComponentWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
							return uiComponentWiseWsDTO;
						}
					}

					if (abstractCMSComponentModel instanceof AccountNavigationComponentModel)
					{

						final AccountNavigationComponentModel accountNavigationComponentModel = (AccountNavigationComponentModel) abstractCMSComponentModel;
						if (null != accountNavigationComponentModel.getVisible()
								&& accountNavigationComponentModel.getVisible().booleanValue())
						{
							final AccountNavigationComponentWsDTO accountNavigationComponentWsDTO = new AccountNavigationComponentWsDTO();
							final List<CMSNavigationNodeWsDTO> cmsNavigationNodeWsDTOList = new ArrayList<CMSNavigationNodeWsDTO>();


							try
							{

								if (null != accountNavigationComponentModel.getNavigationNode()
										&& accountNavigationComponentModel.getNavigationNode().getLinks().size() > 0)
								{

									for (final CMSLinkComponentModel cmsLinkComponentModel : accountNavigationComponentModel
											.getNavigationNode().getLinks())
									{
										final CMSNavigationNodeWsDTO cmsNavigationNodeWsDTO = new CMSNavigationNodeWsDTO();
										cmsNavigationNodeWsDTO.setLinkName(null != cmsLinkComponentModel.getLinkName()
												? cmsLinkComponentModel.getLinkName() : StringUtils.EMPTY);
										cmsNavigationNodeWsDTO.setUrl(
												null != cmsLinkComponentModel.getUrl() ? cmsLinkComponentModel.getUrl() : StringUtils.EMPTY);
										cmsNavigationNodeWsDTOList.add(cmsNavigationNodeWsDTO);
									}
								}
								accountNavigationComponentWsDTO.setNodeList(cmsNavigationNodeWsDTOList);
							}
							catch (final EtailNonBusinessExceptions e)
							{

								LOG.error(accountNavigationError + accountNavigationComponentModel.getUid(), e);
								continue;
							}
							catch (final Exception e)
							{
								LOG.error(accountNavigationError + accountNavigationComponentModel.getUid(), e);
								continue;
							}
							accountNavigationComponentWsDTO.setType("Account Navigation Component");
							uiCompPageElementWsDTO.setComponentName("accountNavigationComponent");
							uiCompPageElementWsDTO.setAccountNavigationComponent(accountNavigationComponentWsDTO);
							genericUICompPageWsDTO.add(uiCompPageElementWsDTO);
							uiComponentWiseWsDTO.setItems(genericUICompPageWsDTO);
							return uiComponentWiseWsDTO;
						}
					}

				}
			}
		}
		return uiComponentWiseWsDTO;
	}

	public String stringUtil(final String number)
	{
		String intStr = StringUtils.EMPTY;
		if (number.contains("."))
		{
			final String[] arrOfStr = number.split("\\.");
			intStr = arrOfStr[0];
			return intStr;
		}
		return number;
	}

	/**
	 * @param toDisplay
	 * @param breadCrumDto
	 * @return
	 */
	private BreadcrumbListWsDTO getCategoryBreadcrumb(final CategoryModel toDisplay, final BreadcrumbListWsDTO breadCrumDto)
	{
		// YTODO Auto-generated method stub
		final String categoryUrl = defaultCategoryModelUrlResolver.resolve(toDisplay);
		breadCrumDto.setName(toDisplay.getName());
		breadCrumDto.setUrl(categoryUrl);

		return breadCrumDto;
	}

}