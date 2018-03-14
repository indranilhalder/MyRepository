/**
 *
 */
package com.tisl.mpl.facade.cms;


import de.hybris.platform.acceleratorcms.model.components.FooterComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSContentSlotService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;
import de.hybris.platform.commercesearch.model.SolrHeroProductDefinitionModel;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.HeroProductDefinitionService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.lux.model.LuxuryHomePagePreferenceModel;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.AmpMenifestModel;
import com.tisl.mpl.core.model.AmpServiceworkerModel;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.MplAdvancedCategoryCarouselComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplFooterLinkModel;
import com.tisl.mpl.core.model.MplImageCategoryComponentModel;
import com.tisl.mpl.core.model.MplNavigationNodeComponentModel;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.core.model.MplShowcaseComponentModel;
import com.tisl.mpl.core.model.MplShowcaseItemComponentModel;
import com.tisl.mpl.core.model.OurJourneyComponentModel;
import com.tisl.mpl.core.model.SignColComponentModel;
import com.tisl.mpl.core.model.SignColItemComponentModel;
import com.tisl.mpl.core.model.VideoComponentModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.facades.cms.data.AmpMenifestData;
import com.tisl.mpl.facades.cms.data.AmpServiceWorkerData;
import com.tisl.mpl.facades.cms.data.BannerComponentData;
import com.tisl.mpl.facades.cms.data.CollectionComponentData;
import com.tisl.mpl.facades.cms.data.CollectionHeroComponentData;
import com.tisl.mpl.facades.cms.data.CollectionPageData;
import com.tisl.mpl.facades.cms.data.CollectionProductData;
import com.tisl.mpl.facades.cms.data.CollectionSectionData;
import com.tisl.mpl.facades.cms.data.ComponentData;
import com.tisl.mpl.facades.cms.data.FooterLinkData;
import com.tisl.mpl.facades.cms.data.HeroComponentData;
import com.tisl.mpl.facades.cms.data.HeroProductData;
import com.tisl.mpl.facades.cms.data.HomePageComponentData;
import com.tisl.mpl.facades.cms.data.ImageListComponentData;
import com.tisl.mpl.facades.cms.data.LinkedCollectionsData;
import com.tisl.mpl.facades.cms.data.MplPageData;
import com.tisl.mpl.facades.cms.data.PageData;
import com.tisl.mpl.facades.cms.data.ProductComponentData;
import com.tisl.mpl.facades.cms.data.ProductListComponentData;
import com.tisl.mpl.facades.cms.data.PromotionComponentData;
import com.tisl.mpl.facades.cms.data.SectionData;
import com.tisl.mpl.facades.cms.data.TextComponentData;
import com.tisl.mpl.facades.data.FooterComponentData;
import com.tisl.mpl.facades.data.SimpleBannerComponentData;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.marketplacecommerceservice.url.ExtDefaultCategoryModelUrlResolver;
import com.tisl.mpl.marketplacecommerceservice.url.ExtDefaultProductModelUrlResolver;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerMasterService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.cms.components.CMSMediaParagraphComponentModel;
import com.tisl.mpl.model.cms.components.ImageCarouselComponentModel;
import com.tisl.mpl.model.cms.components.MobileAppCollectionHeroComponentModel;
import com.tisl.mpl.model.cms.components.MobileAppComponentModel;
import com.tisl.mpl.model.cms.components.MobileAppHeroComponentModel;
import com.tisl.mpl.model.cms.components.MobileBannerComponentModel;
import com.tisl.mpl.model.cms.components.MobileCollectionBannerComponentModel;
import com.tisl.mpl.model.cms.components.MobileCollectionLinkComponentModel;
import com.tisl.mpl.model.cms.components.NeedHelpComponentModel;
import com.tisl.mpl.model.cms.components.PromotionalProductsComponentModel;
import com.tisl.mpl.model.cms.components.SmallBrandMobileAppComponentModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.LuxBlpCompListWsDTO;
import com.tisl.mpl.wsdto.LuxBlpCompWsDTO;
import com.tisl.mpl.wsdto.LuxBlpStripWsDTO;
import com.tisl.mpl.wsdto.LuxBrandProductsListWsDTO;
import com.tisl.mpl.wsdto.LuxComponentsListWsDTO;
import com.tisl.mpl.wsdto.LuxEngagementcomponentWsDTO;
import com.tisl.mpl.wsdto.LuxHeroBannerWsDTO;
import com.tisl.mpl.wsdto.LuxHomePageCompWsDTO;
import com.tisl.mpl.wsdto.LuxJourneyTimeLineListWsDTO;
import com.tisl.mpl.wsdto.LuxLevelFourWsDTO;
import com.tisl.mpl.wsdto.LuxLevelOneWsDTO;
import com.tisl.mpl.wsdto.LuxLevelThreeWsDTO;
import com.tisl.mpl.wsdto.LuxLevelTwoSliderWsDTO;
import com.tisl.mpl.wsdto.LuxLevelTwoWsDTO;
import com.tisl.mpl.wsdto.LuxNavigationWsDTO;
import com.tisl.mpl.wsdto.LuxOurJourneyComponentWsDTO;
import com.tisl.mpl.wsdto.LuxOurMissionComponentWsDTO;
import com.tisl.mpl.wsdto.LuxShopByListWsDTO;
import com.tisl.mpl.wsdto.LuxShopTheLookWsDTO;
import com.tisl.mpl.wsdto.LuxShopYourFavListWsDTO;
import com.tisl.mpl.wsdto.LuxShowcasecomponentWsDTO;
import com.tisl.mpl.wsdto.LuxSignatureWsDTO;
import com.tisl.mpl.wsdto.LuxSocialFeedComponentWsDTO;
import com.tisl.mpl.wsdto.LuxSpringCollectionComponentWsDTO;
import com.tisl.mpl.wsdto.LuxVideocomponentWsDTO;
import com.tisl.mpl.wsdto.TextComponentWsDTO;


/**
 * @author TCS
 * 
 */
public class MplCmsFacadeImpl implements MplCmsFacade
{

	private final String ELECTRONICS_CODE = "MSH12";
	private final String BRAND_ELECTRONICS_CODE = "MBH12";
	private final String APPAREL = "Apparel";
	private final String ELECTRONICS = "Electronics";
	private final String BRAND_CODE = "MBH";

	private final String BRAND = "Brand";
	private final String CATEGORY = "Category";
	private final String SUBBRAND = "Subbrand";
	private final String SELLER = "Seller";
	private final String OFFER = "Offer";
	//private final String SIMPLEBANNERCOMPONENT = "SimpleBannerComponent"; //Sonar fix

	private final String HEROSTATUS = "Success";

	private final String HEROERROR_NOPROD = "No Hero Products available";

	private final String HEROERROR_NOCAT = "Invalid Category Id";

	private final String CONTENTPAGE = "Content";
	private final String SIMPLE_BANNER = "SimpleBannerComponent";

	private MplCMSPageServiceImpl mplCMSPageService;

	private DefaultCategoryService categoryService;

	private ConfigurationService configurationService;

	private Converter<MobileAppHeroComponentModel, HeroComponentData> mobileHeroComponentConverter;

	private Converter<SmallBrandMobileAppComponentModel, ComponentData> mobileSubBrandComponentConverter;

	private Converter<MobileCollectionBannerComponentModel, CollectionComponentData> mobileCollectionComponentConverter;

	private Converter<SmallBrandMobileAppComponentModel, ComponentData> mobileCategoryComponentConverter;

	private Converter<MobileAppCollectionHeroComponentModel, CollectionHeroComponentData> mobileCollectionHeroComponentConverter;

	private Converter<ProductModel, CollectionProductData> mobileCollectionProductConverter;

	@Resource(name = "mplBannerConverter")
	private Converter<SimpleBannerComponentModel, SimpleBannerComponentData> mplBannerConverter;

	private Converter<MplFooterLinkModel, FooterLinkData> footerLinkConverter;

	/**
	 * @return the footerLinkConverter
	 */
	public Converter<MplFooterLinkModel, FooterLinkData> getFooterLinkConverter()
	{
		return footerLinkConverter;
	}

	/**
	 * @param footerLinkConverter
	 *           the footerLinkConverter to set
	 */
	public void setFooterLinkConverter(final Converter<MplFooterLinkModel, FooterLinkData> footerLinkConverter)
	{
		this.footerLinkConverter = footerLinkConverter;
	}

	private HeroProductDefinitionService heroProductDefinitionService;

	private MplSellerMasterService sellerMasterService;

	private Map<String, String> brandLandingPageSlotMapping;

	private static final Logger LOG = Logger.getLogger(MplCmsFacadeImpl.class);


	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;

	@Resource(name = "productService")
	private ProductService productService;

	@Autowired
	private DefaultCMSContentSlotService contentSlotService;


	@Resource(name = "defaultCategoryModelUrlResolver")
	private ExtDefaultCategoryModelUrlResolver defaultCategoryModelUrlResolver;

	@Resource(name = "defaultProductModelUrlResolver")
	private ExtDefaultProductModelUrlResolver defaultProductModelUrlResolver;

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	/**
	 * @return the sellerMasterService
	 */
	public MplSellerMasterService getSellerMasterService()
	{
		return sellerMasterService;
	}

	/**
	 * @param sellerMasterService
	 *           the sellerMasterService to set
	 */
	public void setSellerMasterService(final MplSellerMasterService sellerMasterService)
	{
		this.sellerMasterService = sellerMasterService;
	}


	/**
	 * @return the heroProductDefinitionService
	 */
	public HeroProductDefinitionService getHeroProductDefinitionService()
	{
		return heroProductDefinitionService;
	}

	/**
	 * @param heroProductDefinitionService
	 *           the heroProductDefinitionService to set
	 */
	public void setHeroProductDefinitionService(final HeroProductDefinitionService heroProductDefinitionService)
	{
		this.heroProductDefinitionService = heroProductDefinitionService;
	}

	/**
	 * @return the mobileCollectionProductConverter
	 */
	public Converter<ProductModel, CollectionProductData> getMobileCollectionProductConverter()
	{
		return mobileCollectionProductConverter;
	}

	/**
	 * @param mobileCollectionProductConverter
	 *           the mobileCollectionProductConverter to set
	 */
	public void setMobileCollectionProductConverter(
			final Converter<ProductModel, CollectionProductData> mobileCollectionProductConverter)
	{
		this.mobileCollectionProductConverter = mobileCollectionProductConverter;
	}





	/**
	 * @return the mobileCollectionComponentConverter
	 */
	public Converter<MobileCollectionBannerComponentModel, CollectionComponentData> getMobileCollectionComponentConverter()
	{
		return mobileCollectionComponentConverter;
	}

	/**
	 * @param mobileCollectionComponentConverter
	 *           the mobileCollectionComponentConverter to set
	 */
	public void setMobileCollectionComponentConverter(
			final Converter<MobileCollectionBannerComponentModel, CollectionComponentData> mobileCollectionComponentConverter)
	{
		this.mobileCollectionComponentConverter = mobileCollectionComponentConverter;
	}

	/**
	 * @return the mobileSubBrandComponentConverter
	 */
	public Converter<SmallBrandMobileAppComponentModel, ComponentData> getMobileSubBrandComponentConverter()
	{
		return mobileSubBrandComponentConverter;
	}

	/**
	 * @param mobileSubBrandComponentConverter
	 *           the mobileSubBrandComponentConverter to set
	 */
	public void setMobileSubBrandComponentConverter(
			final Converter<SmallBrandMobileAppComponentModel, ComponentData> mobileSubBrandComponentConverter)
	{
		this.mobileSubBrandComponentConverter = mobileSubBrandComponentConverter;
	}

	/**
	 * @return the mobileHeroComponentConverter
	 */
	public Converter<MobileAppHeroComponentModel, HeroComponentData> getMobileHeroComponentConverter()
	{
		return mobileHeroComponentConverter;
	}

	/**
	 * @param mobileHeroComponentConverter
	 *           the mobileHeroComponentConverter to set
	 */
	public void setMobileHeroComponentConverter(
			final Converter<MobileAppHeroComponentModel, HeroComponentData> mobileHeroComponentConverter)
	{
		this.mobileHeroComponentConverter = mobileHeroComponentConverter;
	}

	/**
	 * @return the mobileAppComponentConverter
	 */
	public Converter<MobileAppComponentModel, ComponentData> getMobileAppComponentConverter()
	{
		return mobileAppComponentConverter;
	}

	/**
	 * @param mobileAppComponentConverter
	 *           the mobileAppComponentConverter to set
	 */
	public void setMobileAppComponentConverter(final Converter<MobileAppComponentModel, ComponentData> mobileAppComponentConverter)
	{
		this.mobileAppComponentConverter = mobileAppComponentConverter;
	}

	private Converter<MobileAppComponentModel, ComponentData> mobileAppComponentConverter;

	/**
	 * @return the categoryService
	 */
	public DefaultCategoryService getCategoryService()
	{
		return categoryService;
	}

	/**
	 * @param categoryService
	 *           the categoryService to set
	 */
	public void setCategoryService(final DefaultCategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	/**
	 * @return the mplCMSPageService
	 */
	public MplCMSPageServiceImpl getMplCMSPageService()
	{
		return mplCMSPageService;
	}

	/**
	 * @param mplCMSPageService
	 *           the mplCMSPageService to set
	 */
	public void setMplCMSPageService(final MplCMSPageServiceImpl mplCMSPageService)
	{
		this.mplCMSPageService = mplCMSPageService;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	private Converter<AmpServiceworkerModel, AmpServiceWorkerData> ampServiceworkerConverter;
	private Converter<AmpMenifestModel, AmpMenifestData> ampMenifestJsonConverter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#getLandingPageForCategory(java.lang.String)
	 */
	@Override
	public PageData getCategoryLandingPageForMobile(final String categoryCode) throws CMSItemNotFoundException,
			NullPointerException
	{
		final CategoryModel category = getCategoryService().getCategoryForCode(categoryCode);
		final ContentPageModel contentPage = getMplCMSPageService().getCategoryLandingPageForMobile(category, CMSChannel.MOBILE);

		PageData contentPageData = null;

		if (contentPage.getMasterTemplate().getUid().equals("MobileAppSmallBrandTemplate"))
		{
			contentPageData = populateSubBrandLandingPageForMobile(contentPage, categoryCode);
		}
		else
		{
			contentPageData = populateCategoryLandingPageForMobile(contentPage, categoryCode);
		}
		return contentPageData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#getHomePageForMobile()
	 */
	@Override
	public MplPageData getHomePageForMobile()
	{
		final ContentPageModel contentPage = getMplCMSPageService().getHomePageForMobile("");

		final List<HomePageComponentData> componentDatas = new ArrayList<HomePageComponentData>();

		int count = 0;

		if (contentPage != null)
		{
			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlotForPage.getContentSlot()
						.getCmsComponents())
				{
					if (abstractCMSComponentModel instanceof MobileBannerComponentModel)
					{
						final MobileBannerComponentModel mobileBannerComponent = (MobileBannerComponentModel) abstractCMSComponentModel;
						final HomePageComponentData homePageData = new HomePageComponentData();
						homePageData.setImage(mobileBannerComponent.getMedia() != null ? mobileBannerComponent.getMedia().getURL()
								: null);
						homePageData.setContentId(mobileBannerComponent.getUid());
						if (mobileBannerComponent.getAssociatedCategory() != null
								&& !mobileBannerComponent.getAssociatedCategory().isEmpty())
						{
							final Collection<CategoryModel> categoryCollection = mobileBannerComponent.getAssociatedCategory();
							for (final CategoryModel category : categoryCollection)
							{
								homePageData.setTargetCode(category.getCode());
								homePageData.setTargetType("Category");
							}
						}

						else if (mobileBannerComponent.getAssociatedProduct() != null
								&& !mobileBannerComponent.getAssociatedProduct().isEmpty())
						{
							final Collection<ProductModel> productCollection = mobileBannerComponent.getAssociatedProduct();
							for (final ProductModel product : productCollection)
							{
								homePageData.setTargetCode(product.getCode());
								homePageData.setTargetType("Product");
							}
						}
						else if (mobileBannerComponent.getAssociatedCollection() != null
								&& !mobileBannerComponent.getAssociatedCollection().isEmpty())
						{
							final Collection<MplShopByLookModel> shopByLookCollection = mobileBannerComponent.getAssociatedCollection();
							for (final MplShopByLookModel shopByLook : shopByLookCollection)
							{
								homePageData.setTargetCode(shopByLook.getCollectionId());
								homePageData.setTargetType("Collection");
							}
						}
						//homePageData.setSequence(new Integer(count));  // Sonar Fixes
						homePageData.setSequence(Integer.valueOf(count));
						count++;
						componentDatas.add(homePageData);
					}

				}
			}
			final MplPageData homePage = new MplPageData();
			//homePage.setComponents(componentDatas);
			return homePage;
		}

		return null;
	}


	public List<LuxComponentsListWsDTO> getComponentDtoForSlot(final ContentSlotModel contentSlot) throws CMSItemNotFoundException
	{

		final List<LuxComponentsListWsDTO> componentListForASlot = new ArrayList<LuxComponentsListWsDTO>();
		LuxComponentsListWsDTO luxuryComponent = new LuxComponentsListWsDTO();
		LuxComponentsListWsDTO luxComponentObj = new LuxComponentsListWsDTO();
		if (null != contentSlot)
		{
			int count = 0;
			for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlot.getCmsComponents())
			{
				final String typecode = abstractCMSComponentModel.getTypeCode();
				if (typecode.equalsIgnoreCase("RotatingImagesComponent"))
				{
					final RotatingImagesComponentModel luxuryBannerComponent = (RotatingImagesComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxHeroBannerWsDTO(luxuryBannerComponent);
					componentListForASlot.add(luxuryComponent);
				}
				else if (typecode.equalsIgnoreCase("SignColComponent"))
				{
					final SignColComponentModel luxurySignatureCollectionComponent = (SignColComponentModel) abstractCMSComponentModel;
					luxuryComponent = getSignatureCollectionWsDTO(luxurySignatureCollectionComponent);
					componentListForASlot.add(luxuryComponent);
				}
				else if (typecode.equalsIgnoreCase("MplAdvancedCategoryCarouselComponent"))
				{
					final MplAdvancedCategoryCarouselComponentModel luxuryCategoryComponent = (MplAdvancedCategoryCarouselComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxShopYourFavListWsDTO(luxuryCategoryComponent);
					componentListForASlot.add(luxuryComponent);
				}
				else if (typecode.equalsIgnoreCase("VideoComponent"))
				{
					final VideoComponentModel luxuryVideoComponent = (VideoComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxVideocomponentWsDTO(luxuryVideoComponent);
					componentListForASlot.add(luxuryComponent);
				}
				else if (typecode.equalsIgnoreCase("MplBigPromoBannerComponent"))
				{
					// To do for this
					final MplBigPromoBannerComponentModel engagementComponent = (MplBigPromoBannerComponentModel) abstractCMSComponentModel;
					if (null != luxuryComponent.getEngagementcomponent())
					{
						luxComponentObj = getLuxEngagementcomponentWsDTO(engagementComponent, luxuryComponent.getEngagementcomponent());
						luxComponentObj.setSectionid(contentSlot.getUid());
						componentListForASlot.add(luxComponentObj);
					}
					else
					{
						luxuryComponent = getLuxEngagementcomponentWsDTO(engagementComponent, luxuryComponent.getEngagementcomponent());
					}
				}
				else if (typecode.equalsIgnoreCase("ProductCarouselComponent"))
				{
					final ProductCarouselComponentModel luxuryProductListComponent = (ProductCarouselComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxProductsListWsDTO(luxuryProductListComponent);
					componentListForASlot.add(luxuryComponent);
				}
				else if (typecode.equalsIgnoreCase("ImageCarouselComponent")
						&& contentSlot.getUid().equalsIgnoreCase("Section7-shopby"))
				{
					final ImageCarouselComponentModel luxuryShopByComponent = (ImageCarouselComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxShopByListWsDTO(luxuryShopByComponent);
					componentListForASlot.add(luxuryComponent);
				}
				// Social Feed Component added

				else if (typecode.equalsIgnoreCase("ImageCarouselComponent")
						&& contentSlot.getUid().equalsIgnoreCase("Section8-socialFeed"))
				{
					final ImageCarouselComponentModel luxurySocialFeedBannerComponent = (ImageCarouselComponentModel) abstractCMSComponentModel;

					if (null == luxuryComponent.getSocialfeedcomponent())
					{
						luxuryComponent = getSocialFeedBannerWsDTO(luxurySocialFeedBannerComponent,
								luxuryComponent.getSocialfeedcomponent());

					}
					else
					{
						if (null != luxComponentObj.getSocialfeedcomponent())
						{
							luxComponentObj = getSocialFeedBannerWsDTO(luxurySocialFeedBannerComponent,
									luxComponentObj.getSocialfeedcomponent());

						}
						else
						{
							luxComponentObj = getSocialFeedBannerWsDTO(luxurySocialFeedBannerComponent,
									luxuryComponent.getSocialfeedcomponent());
						}


					}
					count++;
					if (contentSlot.getCmsComponents().size() == count)
					{
						luxComponentObj.setSectionid(contentSlot.getUid());
						componentListForASlot.add(luxComponentObj);
					}
				}


				else if (typecode.equalsIgnoreCase("CMSParagraphComponent")
						&& contentSlot.getUid().equalsIgnoreCase("Section9-socialFeedHeaderSection"))
				{
					final CMSParagraphComponentModel socialFeedComponent = (CMSParagraphComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxSocialFeedHeaderWsDTO(socialFeedComponent);
					componentListForASlot.add(luxuryComponent);
				}


				LOG.debug("Adding component" + abstractCMSComponentModel.getUid() + "for section" + contentSlot.getUid());
				luxuryComponent.setSectionid(contentSlot.getUid());
				//		componentListForASlot.add(luxuryComponent);

			}

		}
		//}
		return componentListForASlot;
	}

	private LuxComponentsListWsDTO getSocialFeedBannerWsDTO(final ImageCarouselComponentModel luxurySocialFeedBannerComponent,
			final LuxSocialFeedComponentWsDTO luxSocialFeedComponentWsDTO)
	{
		final ArrayList<LuxHeroBannerWsDTO> socialFeedComponentDtoList = new ArrayList<LuxHeroBannerWsDTO>();
		final LuxSocialFeedComponentWsDTO socialFeedComponentDto = new LuxSocialFeedComponentWsDTO();
		final LuxComponentsListWsDTO luxComponent = new LuxComponentsListWsDTO();
		final List<CMSMediaParagraphComponentModel> socialFeedImgList = luxurySocialFeedBannerComponent.getCollectionItems();

		if (null != luxSocialFeedComponentWsDTO)
		{
			if (null != luxSocialFeedComponentWsDTO.getLuxSocialFeedFacebook())
			{
				socialFeedComponentDto.setLuxSocialFeedFacebook(luxSocialFeedComponentWsDTO.getLuxSocialFeedFacebook());
			}

			if (null != luxSocialFeedComponentWsDTO.getLuxSocialFeedGooglePlus())
			{
				socialFeedComponentDto.setLuxSocialFeedGooglePlus(luxSocialFeedComponentWsDTO.getLuxSocialFeedGooglePlus());
			}

			if (null != luxSocialFeedComponentWsDTO.getLuxSocialFeedInstagram())
			{
				socialFeedComponentDto.setLuxSocialFeedInstagram(luxSocialFeedComponentWsDTO.getLuxSocialFeedInstagram());
			}

			if (null != luxSocialFeedComponentWsDTO.getLuxSocialFeedTwitter())
			{
				socialFeedComponentDto.setLuxSocialFeedTwitter(luxSocialFeedComponentWsDTO.getLuxSocialFeedTwitter());
			}

			for (final CMSMediaParagraphComponentModel cmsMediaPara : socialFeedImgList)
			{

				final LuxHeroBannerWsDTO luxHeroBannerWsDTO = new LuxHeroBannerWsDTO();
				if (null != cmsMediaPara.getMedia())
				{
					if (cmsMediaPara.getMedia().getURL() != null)
					{
						luxHeroBannerWsDTO.setBannerMedia(cmsMediaPara.getMedia().getURL());
					}
					if (null != cmsMediaPara.getUrl())
					{
						luxHeroBannerWsDTO.setBannerUrl(cmsMediaPara.getUrl());
					}
				}
				socialFeedComponentDtoList.add(luxHeroBannerWsDTO);
			}

			if (luxurySocialFeedBannerComponent.getUid().equalsIgnoreCase("LuxSocialFeedFacebook"))
			{
				socialFeedComponentDto.setLuxSocialFeedFacebook(socialFeedComponentDtoList);
			}
			if (luxurySocialFeedBannerComponent.getUid().equalsIgnoreCase("LuxSocialFeedTwitter"))
			{
				socialFeedComponentDto.setLuxSocialFeedTwitter(socialFeedComponentDtoList);
			}
			if (luxurySocialFeedBannerComponent.getUid().equalsIgnoreCase("LuxSocialFeedGooglePlus"))
			{
				socialFeedComponentDto.setLuxSocialFeedGooglePlus(socialFeedComponentDtoList);
			}
			if (luxurySocialFeedBannerComponent.getUid().equalsIgnoreCase("LuxSocialFeedInstagram"))
			{
				socialFeedComponentDto.setLuxSocialFeedInstagram(socialFeedComponentDtoList);
			}

		}
		else
		{
			for (final CMSMediaParagraphComponentModel cmsMediaPara : socialFeedImgList)
			{

				final LuxHeroBannerWsDTO luxHeroBannerWsDTO = new LuxHeroBannerWsDTO();
				if (null != cmsMediaPara.getMedia())
				{
					if (cmsMediaPara.getMedia().getURL() != null)
					{
						luxHeroBannerWsDTO.setBannerMedia(cmsMediaPara.getMedia().getURL());
					}
					if (null != cmsMediaPara.getUrl())
					{
						luxHeroBannerWsDTO.setBannerUrl(cmsMediaPara.getUrl());
					}
				}
				socialFeedComponentDtoList.add(luxHeroBannerWsDTO);
			}

			if (luxurySocialFeedBannerComponent.getUid().equalsIgnoreCase("LuxSocialFeedFacebook"))
			{
				socialFeedComponentDto.setLuxSocialFeedFacebook(socialFeedComponentDtoList);
			}
			if (luxurySocialFeedBannerComponent.getUid().equalsIgnoreCase("LuxSocialFeedTwitter"))
			{
				socialFeedComponentDto.setLuxSocialFeedTwitter(socialFeedComponentDtoList);
			}
			if (luxurySocialFeedBannerComponent.getUid().equalsIgnoreCase("LuxSocialFeedGooglePlus"))
			{
				socialFeedComponentDto.setLuxSocialFeedGooglePlus(socialFeedComponentDtoList);
			}
			if (luxurySocialFeedBannerComponent.getUid().equalsIgnoreCase("LuxSocialFeedInstagram"))
			{
				socialFeedComponentDto.setLuxSocialFeedInstagram(socialFeedComponentDtoList);
			}
		}
		luxComponent.setSocialfeedcomponent(socialFeedComponentDto);
		return luxComponent;
	}


	@Override
	public LuxHomePageCompWsDTO getHomePageForLuxury() throws CMSItemNotFoundException
	{

		final ContentPageModel contentPage = getMplCMSPageService().getPageByLabelOrId("luxuryhomepage");
		final LuxHomePageCompWsDTO luxuryHomePageDto = new LuxHomePageCompWsDTO();

		if (contentPage != null)
		{

			final ArrayList<LuxComponentsListWsDTO> listComp = new ArrayList<LuxComponentsListWsDTO>();
			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				final ContentSlotModel contentSlot = contentSlotForPage.getContentSlot();
				final List<LuxComponentsListWsDTO> luxuryComponentsForASlot = getComponentDtoForSlot(contentSlot);
				listComp.addAll(luxuryComponentsForASlot);
			}

			luxuryHomePageDto.setComponents(listComp);
			luxuryHomePageDto.setPageTitle(contentPage.getTitle());
			luxuryHomePageDto.setMetaDescription(contentPage.getDescription());
			luxuryHomePageDto.setMetaKeywords(contentPage.getKeywords());
		}
		return luxuryHomePageDto;

	}


	private LuxComponentsListWsDTO getSignatureCollectionWsDTO(final SignColComponentModel luxurySignatureColComponent)
	{


		final List<LuxSignatureWsDTO> signColList = new ArrayList<LuxSignatureWsDTO>();
		//final List<LuxComponentsListWsDTO> component = new ArrayList<LuxComponentsListWsDTO>();
		final LuxComponentsListWsDTO luxComponent = new LuxComponentsListWsDTO();

		for (final SignColItemComponentModel signColItem : luxurySignatureColComponent.getShowcaseItems())
		{
			final LuxSignatureWsDTO signColItemWsDto = new LuxSignatureWsDTO();
			final List<LuxBrandProductsListWsDTO> productDtoList = new ArrayList<LuxBrandProductsListWsDTO>();

			if (null != signColItem.getBannerText())
			{
				signColItemWsDto.setBrandBannerText(signColItem.getBannerText());
			}

			if (null != signColItem.getBannerUrl())
			{
				signColItemWsDto.setBrandBannerUrl(signColItem.getBannerUrl());
			}

			if (null != signColItem.getBannerImage() && null != signColItem.getBannerImage().getURL())
			{
				signColItemWsDto.setBrandBannerImageUrl(signColItem.getBannerImage().getURL());
			}

			if (null != signColItem.getShowByDefault())
			{
				signColItemWsDto.setShowByDefault(signColItem.getShowByDefault().booleanValue());
			}

			if (null != signColItem.getLogo() && null != signColItem.getLogo().getURL())
			{
				signColItemWsDto.setBrandLogoUrl(signColItem.getLogo().getURL());
			}


			if (null != signColItem.getProduct1())
			{
				LuxBrandProductsListWsDTO prodDto = new LuxBrandProductsListWsDTO();
				prodDto = getProductDetail(signColItem.getProduct1());
				productDtoList.add(prodDto);
			}

			if (null != signColItem.getProduct2())
			{
				LuxBrandProductsListWsDTO prodDto = new LuxBrandProductsListWsDTO();
				prodDto = getProductDetail(signColItem.getProduct2());
				productDtoList.add(prodDto);
			}

			if (null != signColItem.getProduct3())
			{
				LuxBrandProductsListWsDTO prodDto = new LuxBrandProductsListWsDTO();
				prodDto = getProductDetail(signColItem.getProduct3());
				productDtoList.add(prodDto);
			}

			if (null != signColItem.getProduct4())
			{
				LuxBrandProductsListWsDTO prodDto = new LuxBrandProductsListWsDTO();
				prodDto = getProductDetail(signColItem.getProduct4());
				productDtoList.add(prodDto);
			}



			signColItemWsDto.setBrandProducts(productDtoList);

			signColList.add(signColItemWsDto);
		}

		if (null != luxurySignatureColComponent.getTitle())
		{
			luxComponent.setTitle(luxurySignatureColComponent.getTitle());
		}
		luxComponent.setSubsections(signColList);
		return luxComponent;




	}

	/**
	 * @param product1
	 * @return
	 */
	private LuxBrandProductsListWsDTO getProductDetail(final ProductModel product)
	{
		final LuxBrandProductsListWsDTO productDto = new LuxBrandProductsListWsDTO();

		if (null != product.getCode())
		{
			productDto.setProductId(product.getCode());
		}

		if (null != product.getTitle())
		{
			productDto.setProductTitle(product.getTitle());
		}

		if (StringUtils.isNotEmpty(defaultProductModelUrlResolver.resolveInternal(product)))
		{
			productDto.setProductUrl(defaultProductModelUrlResolver.resolveInternal(product));
		}

		if (null != product.getPicture() && null != product.getPicture().getURL())
		{
			productDto.setProductImageUrl(product.getPicture().getURL());
		}

		if (StringUtils.isNotEmpty(product.getCode()))
		{
			try
			{
				final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(product.getCode());
				if (null != buyboxdata && null != buyboxdata.getMrp())
				{
					productDto.setProductMRP(buyboxdata.getMrp().getFormattedValue());
				}
			}
			catch (final Exception ex)

			{
				LOG.error("buybox data null " + ex);
			}
		}
		return productDto;

	}

	/**
	 * @param abstractCMSComponentModel
	 */

	private final LuxComponentsListWsDTO getLuxShopByListWsDTO(final ImageCarouselComponentModel luxuryShopByComponent)
	{
		// YTODO Auto-generated method stub
		//	final String brandUrl = luxuryShopByComponent.getUrl();
		//	final String brandLogoUrl = luxuryShopByComponent.getMedia().getURL();
		//final String title = luxuryShopByComponent.getT
		final ArrayList<LuxShopByListWsDTO> shopByListComponentDtoList = new ArrayList<LuxShopByListWsDTO>();

		final LuxComponentsListWsDTO luxComponent = new LuxComponentsListWsDTO();

		final List<CMSMediaParagraphComponentModel> mediaParaList = luxuryShopByComponent.getCollectionItems();

		for (final CMSMediaParagraphComponentModel cmsMediaPara : mediaParaList)
		{
			final LuxShopByListWsDTO shopByList = new LuxShopByListWsDTO();
			if (null != cmsMediaPara.getMedia())
			{
				if (cmsMediaPara.getMedia().getUrl() != null)
				{
					shopByList.setBrandLogoUrl(cmsMediaPara.getMedia().getUrl());
				}
				if (null != cmsMediaPara.getUrl())
				{
					shopByList.setBrandUrl(cmsMediaPara.getUrl());
				}
				shopByListComponentDtoList.add(shopByList);
			}
			//shopByListComponentDtoList.add(shopByList);
		}
		/*
		 * luxComponent.setTitle("Shop by");
		 */if (null != luxuryShopByComponent.getTitle())
		{
			luxComponent.setTitle(luxuryShopByComponent.getTitle());
		}
		luxComponent.setShopbycomponents(shopByListComponentDtoList);
		return luxComponent;
	}


	/**
	 * @param abstractCMSComponentModel
	 */
	private LuxComponentsListWsDTO getLuxProductsListWsDTO(final ProductCarouselComponentModel luxuryProductListComponent)
	{

		final ArrayList<LuxBrandProductsListWsDTO> productList = new ArrayList<LuxBrandProductsListWsDTO>();
		//final List<LuxComponentsListWsDTO> component = new ArrayList<LuxComponentsListWsDTO>();
		final LuxComponentsListWsDTO luxComponent = new LuxComponentsListWsDTO();

		for (final ProductModel product : luxuryProductListComponent.getProducts())
		{
			final LuxBrandProductsListWsDTO productDto = new LuxBrandProductsListWsDTO();
			String productCode = "";
			if (null != product)
			{
				if (null != product.getCode())
				{
					productDto.setProductId(product.getCode());
					productCode = product.getCode();
				}
				if (null != product.getTitle())
				{
					productDto.setProductTitle(product.getTitle());
				}

				if (null != product.getPicture() && null != product.getPicture().getURL())
				{
					productDto.setProductImageUrl(product.getPicture().getURL());
				}
				if (StringUtils.isNotEmpty(productCode))
				{
					try
					{
						final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode);
						if (null != buyboxdata)
						{
							if (buyboxdata.getPrice() != null)
							{
								productDto.setProductMOP(buyboxdata.getPrice().getFormattedValue());
							}
							if (buyboxdata.getSpecialPrice() != null)
							{
								productDto.setProductSpecialPrice(buyboxdata.getSpecialPrice().getFormattedValue());
							}
							if (buyboxdata.getMrp() != null)
							{
								productDto.setProductMRP(buyboxdata.getMrp().getFormattedValue());
							}
						}
					}
					catch (final Exception ex)
					{
						LOG.error("Buybox data not available" + ex);
					}
				}
				if (StringUtils.isNotEmpty(defaultProductModelUrlResolver.resolveInternal(product)))
				{
					productDto.setProductUrl(defaultProductModelUrlResolver.resolveInternal(product));
					LOG.debug("****url For Product***** " + defaultProductModelUrlResolver.resolveInternal(product));
				}

				if (CollectionUtils.isNotEmpty(product.getBrands()))
				{
					final List<BrandModel> brands = new ArrayList<BrandModel>(product.getBrands());
					if (!brands.isEmpty())
					{
						productDto.setProductBrand(brands.get(0).getName());
					}

				}
				productList.add(productDto);
			}

		}
		//LW-273
		if (null != luxuryProductListComponent.getTitle())
		{
			luxComponent.setTitle(luxuryProductListComponent.getTitle());
		}
		luxComponent.setFeaturedproducts(productList);
		return luxComponent;



	}


	/**
	 * @param bigPromoBannerModel
	 * @param list
	 * @return
	 */
	private LuxComponentsListWsDTO getLuxEngagementcomponentWsDTO(final MplBigPromoBannerComponentModel bigPromoBannerModel,
			final List<LuxEngagementcomponentWsDTO> engagementlist)
	{
		final ArrayList<LuxEngagementcomponentWsDTO> engagementDtoList = new ArrayList<LuxEngagementcomponentWsDTO>();
		//	final ArrayList<LuxEngagementcomponentWsDTO> previousEngagementList = new ArrayList<LuxEngagementcomponentWsDTO>();
		final LuxEngagementcomponentWsDTO engagementDto = new LuxEngagementcomponentWsDTO();
		final LuxEngagementcomponentWsDTO prevEngagementDto = new LuxEngagementcomponentWsDTO();
		final LuxComponentsListWsDTO luxcomponentObj = new LuxComponentsListWsDTO();
		if (null != bigPromoBannerModel)
		{

			if (null != bigPromoBannerModel.getBannerImage() && null != bigPromoBannerModel.getBannerImage().getURL())
			{
				engagementDto.setBannerMedia(bigPromoBannerModel.getBannerImage().getURL());
				//bigPromoBannerModel.getMedia().getAltText();
			}
			if (null != bigPromoBannerModel.getUrlLink())
			{
				engagementDto.setBannerUrl(bigPromoBannerModel.getUrlLink());
			}
			if (null != bigPromoBannerModel.getMajorPromoText())
			{
				engagementDto.setTextArea(bigPromoBannerModel.getMajorPromoText());
			}
			if (null != bigPromoBannerModel.getMinorPromo1Text())
			{
				engagementDto.setLinkArea(bigPromoBannerModel.getMinorPromo1Text());
			}

			if (null != engagementlist && engagementlist.size() > 0)
			{
				for (final LuxEngagementcomponentWsDTO luxEngagementcomponentWsDTO : engagementlist)
				{
					prevEngagementDto.setBannerUrl(luxEngagementcomponentWsDTO.getBannerUrl());
					prevEngagementDto.setBannerMedia(luxEngagementcomponentWsDTO.getBannerMedia());
					prevEngagementDto.setLinkArea(luxEngagementcomponentWsDTO.getLinkArea());
					prevEngagementDto.setTextArea(luxEngagementcomponentWsDTO.getTextArea());
				}
				engagementDtoList.add(prevEngagementDto);
			}
			engagementDtoList.add(engagementDto);
			luxcomponentObj.setEngagementcomponent(engagementDtoList);

		}
		return luxcomponentObj;
	}

	/**
	 * @param socialFeedComponent
	 */


	private LuxComponentsListWsDTO getLuxSocialFeedHeaderWsDTO(final CMSParagraphComponentModel socialFeedComponent)
	{
		final TextComponentWsDTO text = new TextComponentWsDTO();
		final LuxComponentsListWsDTO luxComponent = new LuxComponentsListWsDTO();
		if (null != socialFeedComponent)
		{
			if (null != socialFeedComponent.getContent())
			{
				text.setText(socialFeedComponent.getContent());
			}
		}
		luxComponent.setSocialfeedHeadercomponent(text);
		return luxComponent;
	}



	/**
	 * @param luxuryVideoComponent
	 * @return
	 */
	private LuxComponentsListWsDTO getLuxVideocomponentWsDTO(final VideoComponentModel luxuryVideoComponent)
	{
		final ArrayList<LuxVideocomponentWsDTO> videoComponentDtoList = new ArrayList<LuxVideocomponentWsDTO>();
		final LuxVideocomponentWsDTO video = new LuxVideocomponentWsDTO();
		final LuxComponentsListWsDTO luxComponent = new LuxComponentsListWsDTO();
		if (null != luxuryVideoComponent)
		{
			if (null != luxuryVideoComponent.getVideoUrl())
			{
				video.setVideoUrl(luxuryVideoComponent.getVideoUrl());
			}
			if (null != luxuryVideoComponent.getVideoDescription())
			{
				video.setVideoDescription(luxuryVideoComponent.getVideoDescription());
			}

			if (null != luxuryVideoComponent.getVideoTitle())
			{
				video.setVideoTitle(luxuryVideoComponent.getVideoTitle());
			}
			if (null != luxuryVideoComponent.getPreviewUrl())
			{
				video.setPreviewUrl(luxuryVideoComponent.getPreviewUrl());
			}
			if (null != luxuryVideoComponent.getVideoSectionHeading())
			{
				video.setVideoSectionHeading(luxuryVideoComponent.getVideoSectionHeading());
			}


			videoComponentDtoList.add(video);

			luxComponent.setVideocomponent(videoComponentDtoList);
		}
		return luxComponent;

	}


	/**
	 * @param luxuryCategoryComponent
	 * @return
	 */
	private LuxComponentsListWsDTO getLuxShopYourFavListWsDTO(
			final MplAdvancedCategoryCarouselComponentModel luxuryCategoryComponent)
	{
		// YTODO Auto-generated method stub
		final ArrayList<LuxShopYourFavListWsDTO> shopYourFavList = new ArrayList<LuxShopYourFavListWsDTO>();
		//	final LuxShopYourFavListWsDTO luxshopYourFav = new LuxShopYourFavListWsDTO();
		final LuxComponentsListWsDTO luxComponentents = new LuxComponentsListWsDTO();

		for (final MplImageCategoryComponentModel catCompObj : luxuryCategoryComponent.getCategories())
		{
			final LuxShopYourFavListWsDTO luxshopYourFav = new LuxShopYourFavListWsDTO();
			if (null != catCompObj.getCategory() && null != catCompObj.getCategory().getCode())
			{
				luxshopYourFav.setCategoryId(catCompObj.getCategory().getCode());
			}
			if (catCompObj.getIsImageFromPCM().booleanValue())
			{
				luxshopYourFav.setCategoryImageUrl(getCategoryMediaUrl(catCompObj.getCategory()));
			}
			else
			{
				if (null != catCompObj.getImage() && StringUtils.isNotEmpty(catCompObj.getImage().getURL()))
				{
					luxshopYourFav.setCategoryImageUrl(catCompObj.getImage().getURL());
				}
			}
			if (null != catCompObj.getCategory() && null != catCompObj.getCategory().getName())
			{
				luxshopYourFav.setCategoryName(catCompObj.getCategory().getName());
			}
			if (null != catCompObj.getCategory())
			{
				luxshopYourFav.setCategoryUrl(defaultCategoryModelUrlResolver.resolve(catCompObj.getCategory()));
			}
			shopYourFavList.add(luxshopYourFav);
		}
		//LW-281
		if (null != luxuryCategoryComponent.getTitle())
		{
			luxComponentents.setTitle(luxuryCategoryComponent.getTitle());
		}
		luxComponentents.setCategorylist(shopYourFavList);
		return luxComponentents;


	}

	private String getCategoryMediaUrl(final CategoryModel category)
	{

		String mediaUrl = GenericUtilityMethods.getMissingImageUrl();
		if (null != category.getMedias())
		{
			for (final MediaModel categoryMedia : category.getMedias())
			{
				if (null != categoryMedia.getMediaFormat()
						&& categoryMedia.getMediaFormat().getQualifier().equalsIgnoreCase("324Wx324H")
						&& null != categoryMedia.getURL2())
				{
					mediaUrl = getMediaUrlStrategy(categoryMedia.getURL2());

				}
			}

		}
		return mediaUrl;



	}

	private String getMediaUrlStrategy(final String mediaUrl)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Media Url is :::::::" + mediaUrl);
		}

		String newMediaUrl = mediaUrl;
		if (StringUtils.isNotEmpty(mediaUrl))
		{
			if (mediaUrl.contains("http") || mediaUrl.contains("https"))
			{
				newMediaUrl = mediaUrl.substring((mediaUrl.lastIndexOf(':') + 1));
			}


		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Media Url without protocol is :::::::" + newMediaUrl);
		}

		return newMediaUrl;

	}


	/**
	 * @param luxuryBannerComponent
	 * @return
	 */
	private LuxComponentsListWsDTO getLuxHeroBannerWsDTO(final RotatingImagesComponentModel luxuryBannerComponent)
	{
		final ArrayList<LuxHeroBannerWsDTO> heroBannerDtoList = new ArrayList<LuxHeroBannerWsDTO>();
		final LuxComponentsListWsDTO luxComponent = new LuxComponentsListWsDTO();
		int countMobile = 1;
		int countDesktop = 1;
		for (final BannerComponentModel banner : luxuryBannerComponent.getBanners())
		{
			//final MplBigPromoBannerComponentModel heroBanner = (MplBigPromoBannerComponentModel) banner;
			final LuxHeroBannerWsDTO heroBanner = new LuxHeroBannerWsDTO();
			MplBigPromoBannerComponentModel promotionalBanner = null;
			String bannertext = null;
			String bannerMediaUrl = null;
			String altText = null;
			if (banner instanceof MplBigPromoBannerComponentModel)
			{
				promotionalBanner = (MplBigPromoBannerComponentModel) banner;
				if (StringUtils.isNotEmpty(promotionalBanner.getMajorPromoText()))
				{
					bannertext = promotionalBanner.getMajorPromoText();

				}
				if (null != promotionalBanner.getBannerImage() && StringUtils.isNotEmpty(promotionalBanner.getBannerImage().getURL()))
				{
					bannerMediaUrl = promotionalBanner.getBannerImage().getURL();
					altText = promotionalBanner.getBannerImage().getAltText();

				}
			}
			else
			{
				if (null != banner.getMedia())
				{
					bannerMediaUrl = banner.getMedia().getURL();
					altText = banner.getMedia().getAltText();
				}
			}
			heroBanner.setBannerMedia(bannerMediaUrl);
			heroBanner.setBannerText(bannertext);
			heroBanner.setAltText(altText);
			heroBanner.setBannerUrl(banner.getUrlLink());
			heroBanner.setIcid(banner.getPk().getLongValueAsString());

			if (null != banner.getBannerView())
			{
				heroBanner.setResolution(banner.getBannerView().getCode());
				if (banner.getBannerView().getCode().equalsIgnoreCase("desktop"))
				{
					heroBanner.setBannerNumber(countDesktop);
					countDesktop++;
				}
				else
				{
					heroBanner.setBannerNumber(countMobile);
					countMobile++;
				}
			}
			if (null != luxuryBannerComponent.getTimeout())
			{
				heroBanner.setTimeout(luxuryBannerComponent.getTimeout().intValue());
			}
			heroBannerDtoList.add(heroBanner);
		}

		luxComponent.setBannerlist(heroBannerDtoList);
		return luxComponent;
	}

	@Override
	public List<MplPageData> getPageInformationForPageId(final String pageUid, final PageableData pageableData)
	{
		//Modified for TPR-798
		//final ContentPageModel contentPage = getMplCMSPageService().getPageForAppById(pageUid);
		final SearchPageData<ContentSlotForPageModel> contentPage = getMplCMSPageService().getContentSlotsForAppById(pageUid,
				pageableData);

		final List<MplPageData> componentDatas = new ArrayList<MplPageData>();
		final List<Date> lastModifiedTimes = new ArrayList<Date>();
		//final int count = 0;

		if (contentPage != null)
		{
			//Modified for TPR-798
			//for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getResults())
			{
				final MplPageData homePageData = new MplPageData();
				final List<TextComponentData> texts = new ArrayList<TextComponentData>();
				final List<BannerComponentData> banners = new ArrayList<BannerComponentData>();
				final List<ProductListComponentData> productForShowCase = new ArrayList<ProductListComponentData>();
				final List<ProductComponentData> productCollection = new ArrayList<ProductComponentData>();
				final List<ImageListComponentData> imageCarousels = new ArrayList<ImageListComponentData>();

				for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlotForPage.getContentSlot()
						.getCmsComponents())
				{
					lastModifiedTimes.add(abstractCMSComponentModel.getModifiedtime());
					homePageData.setSectionId(contentSlotForPage.getPosition());
					if (abstractCMSComponentModel instanceof CMSParagraphComponentModel)
					{
						final CMSParagraphComponentModel paragraphComponent = (CMSParagraphComponentModel) abstractCMSComponentModel;
						//homePageData.setSectionName(sectionDescription.getContent());
						final TextComponentData textComponent = new TextComponentData();
						if (paragraphComponent.getContent() != null)
						{
							textComponent.setText(paragraphComponent.getContent());
						}
						if (paragraphComponent.getDeeplinkType() != null)
						{
							textComponent.setType(paragraphComponent.getDeeplinkType());
						}
						if (paragraphComponent.getDeeplinkTypeId() != null)
						{
							textComponent.setTypeId(paragraphComponent.getDeeplinkTypeId());
						}
						if (paragraphComponent.getDeeplinkTypeVal() != null)
						{
							textComponent.setTypeVal(paragraphComponent.getDeeplinkTypeVal());
						}
						if (paragraphComponent.getComponentType() != null)
						{
							textComponent.setComponentType(paragraphComponent.getComponentType().getCode());
						}
						texts.add(textComponent);
					}
					else if (abstractCMSComponentModel instanceof SimpleBannerComponentModel)
					{
						final SimpleBannerComponentModel bannerComponent = (SimpleBannerComponentModel) abstractCMSComponentModel;
						final BannerComponentData banner = new BannerComponentData();
						if (bannerComponent.getMedia() != null)
						{
							if (bannerComponent.getMedia().getDescription() != null)
							{
								banner.setImageDiscription(bannerComponent.getMedia().getDescription());
							}
							// TPR-472
							if (bannerComponent.getPk() != null)
							{
								banner.setIcid(bannerComponent.getPk().getLongValueAsString());
							}
							if (bannerComponent.getMedia().getUrl2() != null)
							{
								banner.setUrl(bannerComponent.getMedia().getUrl2());
							}
							if (bannerComponent.getDeeplinkType() != null)
							{
								banner.setType(bannerComponent.getDeeplinkType());
							}
							if (bannerComponent.getDeeplinkTypeId() != null)
							{
								banner.setTypeId(bannerComponent.getDeeplinkTypeId());
							}
							if (bannerComponent.getDeeplinkType() != null)
							{
								banner.setTypeVal(bannerComponent.getDeeplinkTypeVal());
							}
							if (bannerComponent.getComponentType() != null)
							{
								banner.setComponentType(bannerComponent.getComponentType().getCode());
							}
							//TPR-5168
							if (bannerComponent.getIcid2() != null)
							{
								banner.setIcid2(bannerComponent.getIcid2());
							}
							banners.add(banner);
						}
						//homePageData.setBannerComponents(bannerComponents);
					}
					else if (abstractCMSComponentModel instanceof ImageCarouselComponentModel)
					{
						final ImageCarouselComponentModel carouselComponent = (ImageCarouselComponentModel) abstractCMSComponentModel;
						final ImageListComponentData imageListData = new ImageListComponentData();
						final List<BannerComponentData> carouselBanners = new ArrayList<BannerComponentData>();
						final List<CMSMediaParagraphComponentModel> mediaParaList = carouselComponent.getCollectionItems();
						if (carouselComponent.getComponentType() != null)
						{
							imageListData.setComponentType(carouselComponent.getComponentType().getCode());
						}
						for (final CMSMediaParagraphComponentModel cmsMediaPara : mediaParaList)
						{
							if (null != cmsMediaPara.getMedia())
							{
								final BannerComponentData banner = new BannerComponentData();
								if (cmsMediaPara.getMedia().getDescription() != null)
								{
									banner.setImageDiscription(cmsMediaPara.getMedia().getDescription());
								}
								// TPR-472
								if (cmsMediaPara.getPk() != null)
								{
									banner.setIcid(cmsMediaPara.getPk().getLongValueAsString());
								}
								// TPR-472
								if (cmsMediaPara.getPk() != null)
								{
									banner.setIcid(cmsMediaPara.getPk().getLongValueAsString());
								}
								if (cmsMediaPara.getMedia().getUrl2() != null)
								{
									banner.setUrl(cmsMediaPara.getMedia().getUrl2());
								}
								if (cmsMediaPara.getDeeplinkType() != null)
								{
									banner.setType(cmsMediaPara.getDeeplinkType());
								}
								if (cmsMediaPara.getDeeplinkTypeId() != null)
								{
									banner.setTypeId(cmsMediaPara.getDeeplinkTypeId());
								}
								if (cmsMediaPara.getDeeplinkType() != null)
								{
									banner.setTypeVal(cmsMediaPara.getDeeplinkTypeVal());
								}
								//TPR-5168
								if (cmsMediaPara.getIcid2() != null)
								{
									banner.setIcid2(cmsMediaPara.getIcid2());
								}
								carouselBanners.add(banner);
							}
						}
						imageListData.setBannerslist(carouselBanners);
						imageCarousels.add(imageListData);
					}
					else if (abstractCMSComponentModel instanceof ProductCarouselComponentModel)
					{
						///start TPR-1316
						final ProductComponentData prodCompData = new ProductComponentData();
						final List<ProductListComponentData> productCompList = new ArrayList<ProductListComponentData>();
						final ProductCarouselComponentModel productCarousel = (ProductCarouselComponentModel) abstractCMSComponentModel;
						if (productCarousel.getComponentType() != null)
						{
							prodCompData.setComponentType(productCarousel.getComponentType().getCode());
						}
						for (final String productCode : productCarousel.getProductCodes())
						{
							final ProductListComponentData productComp = new ProductListComponentData();
							ProductModel productModel = null;
							//final ProductData product = null;
							BuyBoxData buyboxdata = null;
							//IQA
							try
							{
								//CAR Project performance issue fixed

								//								product = productFacade.getProductForCodeAndOptions(productCode,
								//										Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.CATEGORIES));
								buyboxdata = buyBoxFacade.buyboxPrice(productCode);
								productModel = productService.getProductForCode(productCode);
								if (null != productModel && null != productModel.getPicture())
								{
									productComp.setImage(productModel.getPicture().getUrl2());
								}
								//IQA
								//								if (StringUtils.isNotEmpty(product.getName()))
								//								{
								//									productComp.setName(product.getName());
								//								}

								//CAR Project performance issue fixed

								if (null != productModel && StringUtils.isNotEmpty(productModel.getName()))
								{
									productComp.setName(productModel.getName());
								}

								if (buyboxdata.getMrp() != null)
								{
									productComp.setPrice(buyboxdata.getMrp().getFormattedValue());
								}
								// changes for INC144318868 - Offer prize is not coming for WCMS component
								/*
								 * if (buyboxdata.getPrice() != null) {
								 * productComp.setSlashedPrice(buyboxdata.getPrice().getFormattedValue()); }
								 */
								if (null != buyboxdata.getSpecialPriceMobile())
								{
									productComp.setSlashedPrice(buyboxdata.getSpecialPriceMobile().getFormattedValue());
								}
								else if (null != buyboxdata.getPrice())
								{
									productComp.setSlashedPrice(buyboxdata.getPrice().getFormattedValue());
								}

								if (productModel != null)
								{
									if (productModel.getDeeplinkType() != null)
									{
										productComp.setType(productModel.getDeeplinkType());
									}
									if (productModel.getDeeplinkTypeId() != null)
									{
										productComp.setTypeId(productModel.getDeeplinkTypeId());
									}
									if (productModel.getDeeplinkTypeVal() != null)
									{
										productComp.setTypeVal(productModel.getDeeplinkTypeVal());
									}
									if (productModel.getCode() != null)
									{
										productComp.setProductID(productModel.getCode());
									}
								}
								productCompList.add(productComp);
							}
							catch (final Exception e)
							{
								LOG.warn("Encountered error while seting product image. ", e);
								ExceptionUtil.getCustomizedExceptionTrace(e);
							}
						}
						prodCompData.setProductList(productCompList);
						productCollection.add(prodCompData);
						///End TPR-1316
					}
				}
				if (lastModifiedTimes.size() >= 1)
				{
					Collections.sort(lastModifiedTimes);
					homePageData.setLastModifiedTime(lastModifiedTimes.get(lastModifiedTimes.size() - 1));
				}
				else
				{
					//Modified for TPR-798
					//homePageData.setLastModifiedTime(contentPage.getModifiedtime());
					homePageData.setLastModifiedTime(contentSlotForPage.getModifiedtime());
				}
				homePageData.setTextComponents(texts);
				homePageData.setProductComponents(productForShowCase);
				homePageData.setProductSliderComponents(productCollection);
				homePageData.setBannerComponents(banners);
				homePageData.setBannerslistComponents(imageCarousels);
				componentDatas.add(homePageData);
			}
		}

		return componentDatas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#populateCategoryLandingPageForMobile()
	 */
	@Override
	public PageData populateCategoryLandingPageForMobile(final ContentPageModel contentPage, final String categoryCode)
	{
		List<ComponentData> componentDatas = null;
		final List<SectionData> contentSlotDatas = new ArrayList<SectionData>(10);
		final PageData contentPageData = populatePageType(categoryCode, false, CONTENTPAGE);

		for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
		{
			final Integer sequence = contentSlotForPage.getSequenceNumber();
			SectionData contentSlotData = null;
			String title = null;
			componentDatas = new ArrayList<ComponentData>();
			for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlotForPage.getContentSlot().getCmsComponents())
			{
				if (abstractCMSComponentModel instanceof MobileAppComponentModel)
				{
					contentSlotData = new SectionData();
					final MobileAppComponentModel mobileAppComponentModel = (MobileAppComponentModel) abstractCMSComponentModel;
					final ComponentData componentData = getMobileAppComponentConverter().convert(mobileAppComponentModel);
					componentDatas.add(componentData);
				}
				else if (abstractCMSComponentModel instanceof CMSParagraphComponentModel)
				{
					title = ((CMSParagraphComponentModel) abstractCMSComponentModel).getContent();
				}
				else if (abstractCMSComponentModel instanceof MobileAppHeroComponentModel)
				{
					final MobileAppHeroComponentModel mobileHeroComponent = (MobileAppHeroComponentModel) abstractCMSComponentModel;
					final HeroComponentData heroComponentData = getMobileHeroComponentConverter().convert(mobileHeroComponent);
					contentPageData.setHeroSection(heroComponentData);
				}
				else if (abstractCMSComponentModel instanceof SmallBrandMobileAppComponentModel)
				{
					contentSlotData = new SectionData();
					final SmallBrandMobileAppComponentModel smallBrandMobileComponentModel = (SmallBrandMobileAppComponentModel) abstractCMSComponentModel;
					final ComponentData componentData = getMobileCategoryComponentConverter().convert(smallBrandMobileComponentModel);
					componentDatas.add(componentData);

				}
				else if (abstractCMSComponentModel instanceof PromotionalProductsComponentModel)
				{
					final PromotionalProductsComponentModel promotionMobileComponentModel = (PromotionalProductsComponentModel) abstractCMSComponentModel;
					final PromotionComponentData promotionComponentData = new PromotionComponentData();
					final ProductPromotionModel promotion = (ProductPromotionModel) promotionMobileComponentModel.getPromotion();
					if (null != promotion)
					{
						final Date today = new Date();
						if ((null != promotion.getStartDate() && null != promotion.getEndDate()
								&& promotion.getStartDate().before(today) && promotion.getEndDate().after(today))
								|| (null == promotion.getStartDate() && null != promotion.getEndDate() && promotion.getEndDate().after(
										today))
								|| (null != promotion.getStartDate() && promotion.getStartDate().before(today) && null == promotion
										.getEndDate()) || null == promotion.getStartDate() && null == promotion.getEndDate())

						{
							promotionComponentData.setTitle(promotion.getName());
							promotionComponentData.setDescription(promotion.getDescription());
							promotionComponentData.setTargetCode(promotion.getCode());
							final List<CollectionProductData> collectionProduct = Converters.convertAll(promotion.getProducts(),
									getMobileCollectionProductConverter());
							promotionComponentData.setProducts(collectionProduct);
							contentPageData.setPromotionSection(promotionComponentData);
						}
					}
				}

			}
			if (contentSlotData != null)
			{
				contentSlotData.setTitle(title);
				contentSlotData.setSequence(sequence);
				contentSlotData.setComponents(componentDatas);
				contentSlotDatas.add(contentSlotData);
			}
		}

		Collections.sort(contentSlotDatas, new Comparator<SectionData>()
		{

			@Override
			public int compare(final SectionData o1, final SectionData o2)
			{

				return o1.getSequence().intValue() - o2.getSequence().intValue();
			}
		});

		contentPageData.setSections(contentSlotDatas);
		// YTODO Auto-generated method stub
		return contentPageData;
	}

	/**
	 * @return the mobileCategoryComponentConverter
	 */
	public Converter<SmallBrandMobileAppComponentModel, ComponentData> getMobileCategoryComponentConverter()
	{
		return mobileCategoryComponentConverter;
	}

	/**
	 * @param mobileCategoryComponentConverter
	 *           the mobileCategoryComponentConverter to set
	 */
	public void setMobileCategoryComponentConverter(
			final Converter<SmallBrandMobileAppComponentModel, ComponentData> mobileCategoryComponentConverter)
	{
		this.mobileCategoryComponentConverter = mobileCategoryComponentConverter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.facade.cms.MplCmsFacade#populateSubBrandLandingPageForMobile(de.hybris.platform.cms2.model.pages.
	 * ContentPageModel, java.lang.String)
	 */
	@Override
	public PageData populateSubBrandLandingPageForMobile(final ContentPageModel contentPage, final String categoryCode)
	{
		final PageData contentPageData = populatePageType(categoryCode, true, CONTENTPAGE);

		final List<SectionData> contentSlotDatas = new ArrayList<SectionData>();

		List<ComponentData> componentDatas = null;

		for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
		{
			SectionData sectionData = null;
			componentDatas = new ArrayList<ComponentData>();
			for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlotForPage.getContentSlot().getCmsComponents())
			{

				if (abstractCMSComponentModel instanceof SmallBrandMobileAppComponentModel)
				{
					sectionData = new SectionData();
					final SmallBrandMobileAppComponentModel smallBrandMobileComponentModel = (SmallBrandMobileAppComponentModel) abstractCMSComponentModel;
					final ComponentData componentData = getMobileSubBrandComponentConverter().convert(smallBrandMobileComponentModel);
					componentDatas.add(componentData);
				}
				else if (abstractCMSComponentModel instanceof MobileAppHeroComponentModel)
				{
					final MobileAppHeroComponentModel mobileHeroComponent = (MobileAppHeroComponentModel) abstractCMSComponentModel;
					final HeroComponentData heroComponentData = getMobileHeroComponentConverter().convert(mobileHeroComponent);
					contentPageData.setHeroSection(heroComponentData);
				}

			}
			if (sectionData != null)
			{
				sectionData.setSequence(contentSlotForPage.getSequenceNumber());
				sectionData.setComponents(componentDatas);
				contentSlotDatas.add(sectionData);
			}

		}
		contentPageData.setSections(contentSlotDatas);

		return contentPageData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#populatePageType(java.lang.String, boolean)
	 */
	@Override
	public PageData populatePageType(final String categoryCode, final boolean isSubBrand, final String PageType)
	{
		final PageData contentPageData = new PageData();
		if (categoryCode.startsWith(ELECTRONICS_CODE) || categoryCode.startsWith(BRAND_ELECTRONICS_CODE))
		{
			contentPageData.setRootCategory(ELECTRONICS);
		}
		else
		{
			contentPageData.setRootCategory(APPAREL);
		}

		if (isSubBrand)
		{
			contentPageData.setCategoryType(SUBBRAND);
		}
		else
		{
			contentPageData.setCategoryType(categoryCode.startsWith(BRAND_CODE) ? BRAND : CATEGORY);
		}

		contentPageData.setPageType(PageType);

		return contentPageData;
	}

	@Override
	public CollectionPageData populateCollectionPage(final String collectionId, final HttpServletRequest request)
			throws CMSItemNotFoundException, NullPointerException
	{
		//final CategoryModel category = getCategoryService().getCategoryForCode(categoryCode);
		final ContentPageModel contentPage = getMplCMSPageService().getCollectionLandingPageForMobile(collectionId,
				CMSChannel.MOBILE);
		final CollectionPageData collectionPageData = new CollectionPageData();
		if (contentPage != null)
		{

			final List<CollectionSectionData> collectionSectionDatas = new ArrayList<CollectionSectionData>();
			CollectionSectionData collectionSectionData = null;

			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlotForPage.getContentSlot()
						.getCmsComponents())
				{

					if (abstractCMSComponentModel instanceof MobileCollectionBannerComponentModel)
					{
						collectionSectionData = new CollectionSectionData();
						final MobileCollectionBannerComponentModel mobileCollectionComponent = (MobileCollectionBannerComponentModel) abstractCMSComponentModel;
						final CollectionComponentData collectionData = getMobileCollectionComponentConverter().convert(
								mobileCollectionComponent);
						collectionSectionData.setComponents(collectionData);
						collectionSectionData.setSequence(contentSlotForPage.getSequenceNumber());
						collectionSectionDatas.add(collectionSectionData);

					}
					else if (abstractCMSComponentModel instanceof MobileCollectionLinkComponentModel)
					{

						final MobileCollectionLinkComponentModel mobileCollectionLinkComponent = (MobileCollectionLinkComponentModel) abstractCMSComponentModel;
						final LinkedCollectionsData previousCollectionData = new LinkedCollectionsData();
						final LinkedCollectionsData nextCollectionData = new LinkedCollectionsData();
						previousCollectionData.setName(mobileCollectionLinkComponent.getPreviousCollectionName());
						previousCollectionData
								.setTargetCollection(mobileCollectionLinkComponent.getPreviousCollection() != null ? mobileCollectionLinkComponent
										.getPreviousCollection().getCollectionId() : null);
						nextCollectionData.setName(mobileCollectionLinkComponent.getNextCollectionName());
						nextCollectionData
								.setTargetCollection(mobileCollectionLinkComponent.getNextCollection() != null ? mobileCollectionLinkComponent
										.getNextCollection().getCollectionId() : null);
						collectionPageData.setPreviousCollection(previousCollectionData);
						collectionPageData.setNextCollection(nextCollectionData);

					}
					else if (abstractCMSComponentModel instanceof MobileAppCollectionHeroComponentModel)
					{

						final MobileAppCollectionHeroComponentModel mobileHeroComponent = (MobileAppCollectionHeroComponentModel) abstractCMSComponentModel;
						final CollectionHeroComponentData collectionHeroComponentData = getMobileCollectionHeroComponentConverter()
								.convert(mobileHeroComponent);
						collectionPageData.setHeroSection(collectionHeroComponentData);

					}
					else if (abstractCMSComponentModel instanceof CMSParagraphComponentModel)
					{
						collectionSectionData = new CollectionSectionData();
						final CMSParagraphComponentModel paragraphComponent = (CMSParagraphComponentModel) abstractCMSComponentModel;
						final CollectionComponentData collectionData = new CollectionComponentData();
						collectionData.setTitle(paragraphComponent.getContent());
						collectionSectionData.setComponents(collectionData);
						collectionSectionData.setSequence(contentSlotForPage.getSequenceNumber());

						collectionSectionDatas.add(collectionSectionData);

					}

				}
			}

			Collections.sort(collectionSectionDatas, new Comparator<CollectionSectionData>()
			{

				@Override
				public int compare(final CollectionSectionData o1, final CollectionSectionData o2)
				{

					return o1.getSequence().intValue() - o2.getSequence().intValue();
				}
			});
			collectionPageData.setSections(collectionSectionDatas);
			collectionPageData.setShareURL(getUrlForCollection(request, collectionId));
			collectionPageData.setStatus("Success");
		}
		else
		{
			collectionPageData.setStatus("Collection Page Not Available or Expired");
		}
		return collectionPageData;


	}

	/**
	 * @return the mobileCollectionHeroComponentConverter
	 */
	public Converter<MobileAppCollectionHeroComponentModel, CollectionHeroComponentData> getMobileCollectionHeroComponentConverter()
	{
		return mobileCollectionHeroComponentConverter;
	}

	/**
	 * @param mobileCollectionHeroComponentConverter
	 *           the mobileCollectionHeroComponentConverter to set
	 */
	public void setMobileCollectionHeroComponentConverter(
			final Converter<MobileAppCollectionHeroComponentModel, CollectionHeroComponentData> mobileCollectionHeroComponentConverter)
	{
		this.mobileCollectionHeroComponentConverter = mobileCollectionHeroComponentConverter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#getCategoryNameForCode(java.lang.String)
	 */
	@Override
	public String getCategoryNameForCode(final String categoryId)
	{
		final CategoryModel category = getCategoryService().getCategoryForCode(categoryId);
		return category.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#getHeroProducts(java.lang.String)
	 */
	@Override
	public HeroProductData getHeroProducts(final String categoryId)
	{
		final CategoryModel categoryModel = getCategoryService().getCategoryForCode(categoryId);
		final HeroProductData heroProduct = new HeroProductData();
		if (null != categoryModel)
		{

			final SolrHeroProductDefinitionModel solrModel = getHeroProductDefinitionService()
					.getSolrHeroProductDefinitionForCategory(categoryModel);
			if (solrModel != null && !solrModel.getProducts().isEmpty())
			{
				final List<CollectionProductData> products = Converters.convertAll(solrModel.getProducts(),
						getMobileCollectionProductConverter());
				heroProduct.setProducts(products);
				heroProduct.setStatus(HEROSTATUS);
			}
			else
			{
				heroProduct.setError(HEROERROR_NOPROD);
			}

		}
		else
		{
			heroProduct.setError(HEROERROR_NOCAT);
		}
		return heroProduct;
	}

	public String getUrlForCollection(final HttpServletRequest request, final String collectionId) throws CMSItemNotFoundException
	{
		URL requestUrl;
		String collectionUrl = null;
		try
		{
			requestUrl = new URL(request.getRequestURL().toString());
			final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
			//final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString + "";
			final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString;
			final String mplWebroot = getConfigurationService().getConfiguration().getString("marketplacestorefront.webroot");
			final String mplCollectionPage = getConfigurationService().getConfiguration().getString("marketplace.collectionpage");
			collectionUrl = baseUrl + mplWebroot + mplCollectionPage + "/" + collectionId;

		}
		catch (final MalformedURLException e)
		{
			//e.printStackTrace();
			LOG.error("MalformedURLException ", e);
		}
		return collectionUrl;
	}

	@Override
	public PageData getSellerLandingPageForMobile(final String sellerId) throws CMSItemNotFoundException, NullPointerException
	{
		final SellerMasterModel sellerMasterModel = getSellerMasterService().getSellerMaster(sellerId);
		final ContentPageModel contentPage = getMplCMSPageService().getSellerLandingPageForMobile(sellerMasterModel,
				CMSChannel.MOBILE);

		PageData contentPageData = null;

		if (contentPage.getMasterTemplate().getUid().equals("MobileAppSellerPageTemplate"))
		{
			contentPageData = populateSellerLandingPageForMobile(contentPage, sellerId);
		}
		return contentPageData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#populateSellerLandingPageForMobile()
	 */
	@Override
	public PageData populateSellerLandingPageForMobile(final ContentPageModel contentPage, final String sellerId)
	{
		List<ComponentData> componentDatas = null;
		final List<SectionData> contentSlotDatas = new ArrayList<SectionData>(10);
		final PageData contentPageData = populateSellerPageType(sellerId, CONTENTPAGE);

		for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
		{
			final Integer sequence = contentSlotForPage.getSequenceNumber();
			SectionData contentSlotData = null;
			String title = null;
			componentDatas = new ArrayList<ComponentData>();
			for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlotForPage.getContentSlot().getCmsComponents())
			{
				if (abstractCMSComponentModel instanceof MobileAppComponentModel)
				{
					contentSlotData = new SectionData();
					final MobileAppComponentModel mobileAppComponentModel = (MobileAppComponentModel) abstractCMSComponentModel;
					final ComponentData componentData = getMobileAppComponentConverter().convert(mobileAppComponentModel);
					componentDatas.add(componentData);
				}
				else if (abstractCMSComponentModel instanceof CMSParagraphComponentModel)
				{
					title = ((CMSParagraphComponentModel) abstractCMSComponentModel).getContent();
				}
				else if (abstractCMSComponentModel instanceof MobileAppHeroComponentModel)
				{
					final MobileAppHeroComponentModel mobileHeroComponent = (MobileAppHeroComponentModel) abstractCMSComponentModel;
					final HeroComponentData heroComponentData = getMobileHeroComponentConverter().convert(mobileHeroComponent);
					contentPageData.setHeroSection(heroComponentData);
				}
				/*
				 * else if (abstractCMSComponentModel instanceof SmallBrandMobileAppComponentModel) { contentSlotData = new
				 * SectionData(); final SmallBrandMobileAppComponentModel smallBrandMobileComponentModel =
				 * (SmallBrandMobileAppComponentModel) abstractCMSComponentModel; final ComponentData componentData =
				 * getMobileCategoryComponentConverter().convert(smallBrandMobileComponentModel);
				 * componentDatas.add(componentData);
				 * 
				 * }
				 */
				else if (abstractCMSComponentModel instanceof PromotionalProductsComponentModel)
				{
					final PromotionalProductsComponentModel promotionMobileComponentModel = (PromotionalProductsComponentModel) abstractCMSComponentModel;
					final PromotionComponentData promotionComponentData = new PromotionComponentData();
					final ProductPromotionModel promotion = (ProductPromotionModel) promotionMobileComponentModel.getPromotion();
					if (null != promotion)
					{
						final Date today = new Date();
						if ((null != promotion.getStartDate() && null != promotion.getEndDate()
								&& promotion.getStartDate().before(today) && promotion.getEndDate().after(today))
								|| (null == promotion.getStartDate() && null != promotion.getEndDate() && promotion.getEndDate().after(
										today))
								|| (null != promotion.getStartDate() && promotion.getStartDate().before(today) && null == promotion
										.getEndDate()) || null == promotion.getStartDate() && null == promotion.getEndDate())

						{
							promotionComponentData.setTitle(promotion.getName());
							promotionComponentData.setDescription(promotion.getDescription());
							promotionComponentData.setTargetCode(promotion.getCode());
							final List<CollectionProductData> collectionProduct = Converters.convertAll(promotion.getProducts(),
									getMobileCollectionProductConverter());
							promotionComponentData.setProducts(collectionProduct);
							contentPageData.setPromotionSection(promotionComponentData);
						}
					}
				}

			}
			if (contentSlotData != null)
			{
				contentSlotData.setTitle(title);
				contentSlotData.setSequence(sequence);
				contentSlotData.setComponents(componentDatas);
				contentSlotDatas.add(contentSlotData);
			}
		}

		Collections.sort(contentSlotDatas, new Comparator<SectionData>()
		{

			@Override
			public int compare(final SectionData o1, final SectionData o2)
			{

				return o1.getSequence().intValue() - o2.getSequence().intValue();
			}
		});

		contentPageData.setSections(contentSlotDatas);
		// YTODO Auto-generated method stub
		return contentPageData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#getSellerMasterName(java.lang.String)
	 */
	@Override
	public String getSellerMasterName(final String sellerId)
	{
		final SellerMasterModel sellerMasterModel = getSellerMasterService().getSellerMaster(sellerId);
		return sellerMasterModel.getLegalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#populateSellerPageType(java.lang.String, boolean)
	 */
	@Override
	public PageData populateSellerPageType(final String sellerId, final String PageType)
	{
		final PageData contentPageData = new PageData();
		//contentPageData.setRootCategory(APPAREL);
		contentPageData.setCategoryType(SELLER);
		contentPageData.setPageType(PageType);

		return contentPageData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#populateOfferPageType(java.lang.String, boolean)
	 */
	@Override
	public PageData populateOfferPageType(final String offerId, final String PageType)
	{
		final PageData contentPageData = new PageData();
		//contentPageData.setRootCategory(APPAREL);
		contentPageData.setCategoryType(OFFER);
		contentPageData.setPageType(PageType);

		return contentPageData;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#getlandingForBrand()
	 */
	@Override
	public LuxBlpCompWsDTO getlandingForBrand(final String brandCode) throws CMSItemNotFoundException
	{
		// YTODO Auto-generated method stub
		final LuxBlpCompWsDTO luxBlpPageDto = new LuxBlpCompWsDTO();
		try
		{
			final CategoryModel category = getCategoryService().getCategoryForCode(brandCode);

			final ContentPageModel contentPage = getLandingPageForCategory(category);


			if (contentPage != null)
			{

				final ArrayList<LuxBlpCompListWsDTO> listComp = new ArrayList<LuxBlpCompListWsDTO>();
				for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
				{
					final ContentSlotModel contentSlot = contentSlotForPage.getContentSlot();

					final List<LuxBlpCompListWsDTO> luxuryComponentsForASlot = getComponentDtoforASlot(contentSlot,
							contentSlotForPage.getPosition(), brandCode);
					listComp.addAll(luxuryComponentsForASlot);
				}

				luxBlpPageDto.setComponents(listComp);
				luxBlpPageDto.setPageTitle(contentPage.getTitle());
				luxBlpPageDto.setMetaDescription(contentPage.getDescription());
				luxBlpPageDto.setMetaKeywords(contentPage.getKeywords());
			}
		}
		catch (final CMSItemNotFoundException ex)
		{
			throw new CMSItemNotFoundException("Could not find a landing page for the category");
		}
		catch (final Exception ex)
		{
			throw new EtailBusinessExceptions("Exception occured while populating data:::");
		}
		return luxBlpPageDto;

	}

	private ContentPageModel getLandingPageForCategory(final CategoryModel category) throws CMSItemNotFoundException
	{

		final ContentPageModel landingPage = getMplCMSPageService().getLandingPageForCategory(category);

		if (landingPage == null)
		{
			throw new CMSItemNotFoundException("Could not find a landing page for the category  " + category.getCode());
		}

		return landingPage;

	}

	/**
	 * @param contentSlot
	 * @param postion
	 * @return
	 */
	private List<LuxBlpCompListWsDTO> getComponentDtoforASlot(final ContentSlotModel contentSlot, final String position,
			final String brandCode) throws CMSItemNotFoundException
	{
		// YTODO Auto-generated method stub
		final List<LuxBlpCompListWsDTO> componentListForASlot = new ArrayList<LuxBlpCompListWsDTO>();
		LuxBlpCompListWsDTO blpComponent = new LuxBlpCompListWsDTO();
		final String section = getBrandLandingPageSlotMapping().get(position);

		if (null != contentSlot)
		{

			for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlot.getCmsComponents())
			{
				final String typecode = abstractCMSComponentModel.getTypeCode();
				if (typecode.equalsIgnoreCase("RotatingImagesComponent") && position.equalsIgnoreCase("Section2"))
				{
					final RotatingImagesComponentModel luxuryBannerComponent = (RotatingImagesComponentModel) abstractCMSComponentModel;
					blpComponent = getForHimHerBannerWsDTO(luxuryBannerComponent);
					componentListForASlot.add(blpComponent);
				}


				if (typecode.equalsIgnoreCase(SIMPLE_BANNER) && position.equalsIgnoreCase("BottomHeaderSlot"))

				{
					final SimpleBannerComponentModel headerslot = (SimpleBannerComponentModel) abstractCMSComponentModel;
					blpComponent = getHeaderComponent(headerslot);
					componentListForASlot.add(blpComponent);
				}

				if (typecode.equalsIgnoreCase("RotatingImagesComponent") && position.equalsIgnoreCase("Section1"))
				{
					final RotatingImagesComponentModel luxBlpBannerComponent = (RotatingImagesComponentModel) abstractCMSComponentModel;
					blpComponent = getBlpBannerWsDTO(luxBlpBannerComponent);
					componentListForASlot.add(blpComponent);
				}

				if (typecode.equalsIgnoreCase(SIMPLE_BANNER) && position.equalsIgnoreCase("Section3"))

				{
					final SimpleBannerComponentModel springComponent = (SimpleBannerComponentModel) abstractCMSComponentModel;
					final LuxSpringCollectionComponentWsDTO springDto = getSpringWsDTO(springComponent);
					final LuxBlpCompListWsDTO luxBlpComponent = new LuxBlpCompListWsDTO();
					luxBlpComponent.setSpring_component(springDto);
					luxBlpComponent.setSectionid(section);
					componentListForASlot.add(luxBlpComponent);
				}

				if (typecode.equalsIgnoreCase(SIMPLE_BANNER) && position.equalsIgnoreCase("Section4"))

				{
					final SimpleBannerComponentModel springComponent = (SimpleBannerComponentModel) abstractCMSComponentModel;
					final LuxSpringCollectionComponentWsDTO featuredComponentDto = getSpringWsDTO(springComponent);
					final LuxBlpCompListWsDTO luxBlpComponent = new LuxBlpCompListWsDTO();
					luxBlpComponent.setFeatured_component(featuredComponentDto);
					luxBlpComponent.setSectionid(section);
					componentListForASlot.add(luxBlpComponent);
				}

				else if (typecode.equalsIgnoreCase("VideoComponent"))
				{
					final VideoComponentModel BlpVideoComponent = (VideoComponentModel) abstractCMSComponentModel;
					blpComponent = getLuxBlpVideocomponentWsDTO(BlpVideoComponent);
					componentListForASlot.add(blpComponent);
				}
				else if (typecode.equalsIgnoreCase("MplBigPromoBannerComponent") && position.equalsIgnoreCase("Section6"))
				{
					LuxBlpCompListWsDTO blppromoComponent = new LuxBlpCompListWsDTO();
					final MplBigPromoBannerComponentModel promobannerComponent = (MplBigPromoBannerComponentModel) abstractCMSComponentModel;
					if (null != blpComponent.getShop_the_look_component())
					{
						blppromoComponent = getBlpShopThelookComponentMedia(promobannerComponent,
								blpComponent.getShop_the_look_component());
						blppromoComponent.setSectionid(section);
						componentListForASlot.add(blppromoComponent);
					}
					else
					{
						blpComponent = getBlpShopThelookComponentMedia(promobannerComponent, blpComponent.getShop_the_look_component());
					}

				}
				else if (typecode.equalsIgnoreCase("ProductCarouselComponent") && position.equalsIgnoreCase("Section6"))
				{
					LuxBlpCompListWsDTO blpproductComponent = new LuxBlpCompListWsDTO();
					final ProductCarouselComponentModel productCarouselComponent = (ProductCarouselComponentModel) abstractCMSComponentModel;
					if (null != blpComponent.getShop_the_look_component())
					{
						blpproductComponent = getBlpShopThelookComponent(productCarouselComponent,
								blpComponent.getShop_the_look_component());
						blpproductComponent.setSectionid(section);
						componentListForASlot.add(blpproductComponent);
					}
					else
					{
						blpComponent = getBlpShopThelookComponent(productCarouselComponent, blpComponent.getShop_the_look_component());
					}
				}
				else if (typecode.equalsIgnoreCase("MplAdvancedCategoryCarouselComponent"))
				{
					final MplAdvancedCategoryCarouselComponentModel BlpVideoComponent = (MplAdvancedCategoryCarouselComponentModel) abstractCMSComponentModel;
					blpComponent = getBlpAdvanceCategory(BlpVideoComponent, brandCode);
					componentListForASlot.add(blpComponent);
				}
				if (typecode.equalsIgnoreCase("MplShowcaseComponent"))
				{
					final MplShowcaseComponentModel luxurywhyourproductsComponent = (MplShowcaseComponentModel) abstractCMSComponentModel;
					blpComponent = getwhyourproductsWsDTO(luxurywhyourproductsComponent);
					componentListForASlot.add(blpComponent);
				}

				if (typecode.equalsIgnoreCase("OurJourneyComponent") && position.equalsIgnoreCase("Section9"))
				{
					final OurJourneyComponentModel seeOurJourneyComponent = (OurJourneyComponentModel) abstractCMSComponentModel;
					blpComponent = getSeeOurJourneyWsDTO(seeOurJourneyComponent);
					componentListForASlot.add(blpComponent);
				}


				if (typecode.equalsIgnoreCase(SIMPLE_BANNER) && position.equalsIgnoreCase("Section8"))

				{
					final SimpleBannerComponentModel luxuryOurMissionComponent = (SimpleBannerComponentModel) abstractCMSComponentModel;
					blpComponent = getOurMissionWsDTO(luxuryOurMissionComponent);
					componentListForASlot.add(blpComponent);
				}

				LOG.debug("Adding component" + abstractCMSComponentModel.getUid() + "for section" + section + "for position"
						+ position);

				blpComponent.setSectionid(section);
				//	componentListForASlot.add(blpComponent);
			}



		}
		return componentListForASlot;

	}

	/**
	 * @param headerslot
	 * @return
	 */
	private LuxBlpCompListWsDTO getHeaderComponent(final SimpleBannerComponentModel headerslot)
	{
		// YTODO Auto-generated method stub
		final LuxBlpCompListWsDTO luxComponent = new LuxBlpCompListWsDTO();
		final LuxBlpStripWsDTO luxblpStrip = new LuxBlpStripWsDTO();

		if (null != headerslot.getMedia())
		{
			luxblpStrip.setUrl(headerslot.getMedia().getURL());

		}
		luxComponent.setBanner_strip_component(luxblpStrip);
		return luxComponent;

	}

	/**
	 * @param springComponent
	 * @return
	 */
	private LuxSpringCollectionComponentWsDTO getSpringWsDTO(final SimpleBannerComponentModel springComponent)
	{
		final LuxSpringCollectionComponentWsDTO springDto = new LuxSpringCollectionComponentWsDTO();

		final LuxHeroBannerWsDTO bannerDto = new LuxHeroBannerWsDTO();
		if (null != springComponent)
		{
			if (null != springComponent.getTitle())

			{
				springDto.setText_heading(springComponent.getTitle());
			}

			if (null != springComponent.getDescription())
			{
				springDto.setText(springComponent.getDescription());
			}

			if (null != springComponent.getMedia())
			{
				bannerDto.setBannerMedia(springComponent.getMedia().getURL());
				bannerDto.setAltText(springComponent.getMedia().getAltText());
			}
			if (null != springComponent.getUrlLink())
			{
				bannerDto.setBannerUrl(springComponent.getUrlLink());
			}
			springDto.setImage(bannerDto);

		}
		return springDto;
	}

	private LuxBlpCompListWsDTO getOurMissionWsDTO(final SimpleBannerComponentModel luxuryOurMissionComponent)
	{
		final LuxBlpCompListWsDTO luxOuMissionComponent = new LuxBlpCompListWsDTO();
		final LuxOurMissionComponentWsDTO our_mission_component = new LuxOurMissionComponentWsDTO();
		if (null != luxuryOurMissionComponent)
		{
			if (null != luxuryOurMissionComponent.getTitle())
			{
				our_mission_component.setSectionHeading(luxuryOurMissionComponent.getTitle());
			}
			if (null != luxuryOurMissionComponent.getDescription())
			{
				our_mission_component.setText(luxuryOurMissionComponent.getDescription());
			}
		}
		luxOuMissionComponent.setOur_mission_component(our_mission_component);
		return luxOuMissionComponent;
	}

	/**
	 * @param productcomponent
	 * @param luxShopTheLookWsDTO
	 * @return LuxBlpCompListWsDTO
	 */
	private LuxBlpCompListWsDTO getBlpShopThelookComponent(final ProductCarouselComponentModel productcomponent,
			final LuxShopTheLookWsDTO luxShopTheLookWsDTO)
	{
		final ArrayList<LuxBrandProductsListWsDTO> productList = new ArrayList<LuxBrandProductsListWsDTO>();
		//final List<LuxComponentsListWsDTO> component = new ArrayList<LuxComponentsListWsDTO>();
		final LuxShopTheLookWsDTO stlProductList = new LuxShopTheLookWsDTO();
		final LuxBlpCompListWsDTO luxComponent = new LuxBlpCompListWsDTO();
		int count = 1;
		for (final ProductModel product : productcomponent.getProducts())
		{
			final LuxBrandProductsListWsDTO productDto = new LuxBrandProductsListWsDTO();
			String productCode = "";
			if (null != product)
			{
				if (null != product.getCode())
				{
					productDto.setProductId(product.getCode());
					productCode = product.getCode();
				}
				if (null != product.getTitle())
				{
					productDto.setProductTitle(product.getTitle());
				}

				if (null != product.getPicture())
				{
					productDto.setProductImageUrl(product.getPicture().getURL());
				}
				if (StringUtils.isNotEmpty(productCode))
				{
					try
					{
						final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode);
						if (null != buyboxdata)
						{
							if (buyboxdata.getPrice() != null)
							{
								productDto.setProductMOP(buyboxdata.getPrice().getFormattedValue());
							}
							if (buyboxdata.getSpecialPrice() != null)
							{
								productDto.setProductSpecialPrice(buyboxdata.getSpecialPrice().getFormattedValue());
							}
							if (buyboxdata.getMrp() != null)
							{
								productDto.setProductMRP(buyboxdata.getMrp().getFormattedValue());
							}
						}
					}
					catch (final Exception ex)
					{
						LOG.error("Buybox data not available" + ex);
					}
				}
				if (StringUtils.isNotEmpty(defaultProductModelUrlResolver.resolveInternal(product)))
				{
					productDto.setProductUrl(defaultProductModelUrlResolver.resolveInternal(product));
					LOG.debug("****url For Product***** " + defaultProductModelUrlResolver.resolveInternal(product));
				}

				if (CollectionUtils.isNotEmpty(product.getBrands()))
				{
					final List<BrandModel> brands = new ArrayList<BrandModel>(product.getBrands());
					if (!brands.isEmpty())
					{
						productDto.setProductBrand(brands.get(0).getName());
					}

				}
				productDto.setPosition(Integer.valueOf(count));
				productList.add(productDto);
				count++;
			}

		}
		if (null != luxShopTheLookWsDTO)
		{
			if (null != luxShopTheLookWsDTO.getHeader())
			{
				stlProductList.setHeader(luxShopTheLookWsDTO.getHeader());
			}
			if (null != luxShopTheLookWsDTO.getSection_text())
			{
				stlProductList.setSection_text(luxShopTheLookWsDTO.getSection_text());
			}
			if (null != luxShopTheLookWsDTO.getCta())
			{
				stlProductList.setCta(luxShopTheLookWsDTO.getCta());
			}

			if (null != luxShopTheLookWsDTO.getImage())
			{
				stlProductList.setImage(luxShopTheLookWsDTO.getImage());
			}
			if (null != luxShopTheLookWsDTO.getDestination())
			{
				stlProductList.setDestination(luxShopTheLookWsDTO.getDestination());
			}


		}
		stlProductList.setProductlisting(productList);
		luxComponent.setShop_the_look_component(stlProductList);
		return luxComponent;
	}

	/**
	 * @param promobannerComponent
	 * @param luxShopTheLookWsDTO
	 * @return LuxBlpCompListWsDTO
	 */
	private LuxBlpCompListWsDTO getBlpShopThelookComponentMedia(final MplBigPromoBannerComponentModel promobannerComponent,
			final LuxShopTheLookWsDTO luxShopTheLookWsDTO)
	{
		// YTODO Auto-generated method stub
		final ArrayList<LuxBrandProductsListWsDTO> productList = new ArrayList<LuxBrandProductsListWsDTO>();
		final LuxShopTheLookWsDTO luxStlComponentObj = new LuxShopTheLookWsDTO();
		final LuxBlpCompListWsDTO luxComponent = new LuxBlpCompListWsDTO();
		if (null != promobannerComponent)
		{
			if (null != promobannerComponent.getMajorPromoText())
			{
				luxStlComponentObj.setHeader(promobannerComponent.getMajorPromoText());
			}
			if (null != promobannerComponent.getMinorPromo1Text())
			{
				luxStlComponentObj.setSection_text(promobannerComponent.getMinorPromo1Text());
			}
			if (null != promobannerComponent.getMinorPromo2Text())
			{
				luxStlComponentObj.setCta(promobannerComponent.getMinorPromo2Text());
			}

			if (null != promobannerComponent.getBannerImage() && null != promobannerComponent.getBannerImage().getURL())
			{
				luxStlComponentObj.setImage(promobannerComponent.getBannerImage().getURL());
			}

			luxStlComponentObj.setDestination(promobannerComponent.getUrlLink());
		}
		if (null != luxShopTheLookWsDTO)
		{
			for (final LuxBrandProductsListWsDTO products : luxShopTheLookWsDTO.getProductlisting())
			{
				final LuxBrandProductsListWsDTO productDto = new LuxBrandProductsListWsDTO();
				if (null != products.getProductId())
				{
					productDto.setProductId(products.getProductId());
				}
				if (null != products.getProductImageUrl())
				{
					productDto.setProductImageUrl(products.getProductImageUrl());
				}
				if (null != products.getProductTitle())
				{
					productDto.setProductTitle(products.getProductTitle());
				}
				if (null != products.getProductMRP())
				{
					productDto.setProductMRP(products.getProductMRP());
				}
				if (null != products.getProductMOP())
				{
					productDto.setProductMOP(products.getProductMOP());
				}
				if (null != products.getProductSpecialPrice())
				{
					productDto.setProductSpecialPrice(products.getProductSpecialPrice());
				}
				if (null != products.getProductUrl())
				{
					productDto.setProductUrl(products.getProductUrl());
				}
				if (null != products.getProductBrand())
				{
					productDto.setProductBrand(products.getProductBrand());
				}

				productList.add(productDto);
			}
			luxStlComponentObj.setProductlisting(productList);

		}
		luxComponent.setShop_the_look_component(luxStlComponentObj);
		return luxComponent;
	}

	/**
	 * @param seeOurJourneyComponent
	 * @return
	 */
	private LuxBlpCompListWsDTO getSeeOurJourneyWsDTO(final OurJourneyComponentModel seeOurJourneyComponent)
	{

		final ArrayList<LuxJourneyTimeLineListWsDTO> ourJourneyComponentDtoList = new ArrayList<LuxJourneyTimeLineListWsDTO>();
		final LuxOurJourneyComponentWsDTO ourJourneyDto = new LuxOurJourneyComponentWsDTO();
		final LuxBlpCompListWsDTO luxBlpComponent = new LuxBlpCompListWsDTO();
		if (null != seeOurJourneyComponent)
		{
			if (null != seeOurJourneyComponent.getTitle())

			{
				ourJourneyDto.setTimelineBeginText(seeOurJourneyComponent.getTitle());
			}

			if (null != seeOurJourneyComponent.getHeaderText())
			{
				ourJourneyDto.setSectionHeading(seeOurJourneyComponent.getHeaderText());
			}
			for (final SimpleBannerComponentModel banner : seeOurJourneyComponent.getFooterImageList())
			{
				final LuxJourneyTimeLineListWsDTO seeOurJourneyDto = new LuxJourneyTimeLineListWsDTO();
				if (null != banner.getMedia() && null != banner.getMedia().getURL())
				{
					seeOurJourneyDto.setImage(banner.getMedia().getURL());
				}

				if (null != banner.getMedia() && null != banner.getMedia().getAltText())
				{
					seeOurJourneyDto.setImg_alt(banner.getMedia().getAltText());
				}

				if (null != banner.getTitle())
				{
					seeOurJourneyDto.setYear(banner.getTitle());
				}

				if (null != banner.getDescription())
				{
					seeOurJourneyDto.setText(banner.getDescription());
				}
				ourJourneyComponentDtoList.add(seeOurJourneyDto);
			}

			ourJourneyDto.setTimelines(ourJourneyComponentDtoList);

			luxBlpComponent.setOur_journey_component(ourJourneyDto);
		}
		return luxBlpComponent;

	}



	/**
	 * @param luxBlpBannerComponent
	 * @return
	 */
	private LuxBlpCompListWsDTO getBlpBannerWsDTO(final RotatingImagesComponentModel luxBlpBannerComponent)
	{

		final ArrayList<LuxHeroBannerWsDTO> blpDisplayBannerList = new ArrayList<LuxHeroBannerWsDTO>();


		final LuxBlpCompListWsDTO herocomponents = new LuxBlpCompListWsDTO();
		//	final LuxComponentsListWsDTO luxComponent = new LuxComponentsListWsDTO();
		int countMobile = 1;
		int countDesktop = 1;
		for (final BannerComponentModel banner : luxBlpBannerComponent.getBanners())
		{
			//final MplBigPromoBannerComponentModel heroBanner = (MplBigPromoBannerComponentModel) banner;
			final LuxHeroBannerWsDTO heroBanner = new LuxHeroBannerWsDTO();
			MplBigPromoBannerComponentModel promotionalBanner = null;
			String bannertext = null;
			String bannerMediaUrl = null;
			String altText = null;
			if (banner instanceof MplBigPromoBannerComponentModel)
			{
				promotionalBanner = (MplBigPromoBannerComponentModel) banner;
				if (StringUtils.isNotEmpty(promotionalBanner.getMajorPromoText()))
				{
					bannertext = promotionalBanner.getMajorPromoText();

				}
				if (null != promotionalBanner.getBannerImage() && StringUtils.isNotEmpty(promotionalBanner.getBannerImage().getURL()))
				{
					bannerMediaUrl = promotionalBanner.getBannerImage().getURL();
					altText = promotionalBanner.getBannerImage().getAltText();

				}
			}
			else
			{
				if (null != banner.getMedia())
				{
					bannerMediaUrl = banner.getMedia().getURL();
					altText = banner.getMedia().getAltText();
				}
			}
			heroBanner.setBannerMedia(bannerMediaUrl);
			heroBanner.setBannerText(bannertext);
			heroBanner.setAltText(altText);
			heroBanner.setBannerUrl(banner.getUrlLink());
			heroBanner.setIcid(banner.getPk().getLongValueAsString());

			if (null != banner.getBannerView())
			{
				heroBanner.setResolution(banner.getBannerView().getCode());
				if (banner.getBannerView().getCode().equalsIgnoreCase("desktop"))
				{
					heroBanner.setBannerNumber(countDesktop);
					countDesktop++;
				}
				else
				{
					heroBanner.setBannerNumber(countMobile);
					countMobile++;
				}
			}
			if (null != luxBlpBannerComponent.getTimeout())
			{
				heroBanner.setTimeout(luxBlpBannerComponent.getTimeout().intValue());
			}
			blpDisplayBannerList.add(heroBanner);
		}

		herocomponents.setHero_banner_component(blpDisplayBannerList);
		return herocomponents;
	}

	/**
	 * @param luxuryBannerComponent
	 * @return
	 */
	private LuxBlpCompListWsDTO getwhyourproductsWsDTO(final MplShowcaseComponentModel luxurywhyourproductsComponent)
	{

		final ArrayList<LuxShowcasecomponentWsDTO> showcaseComponentDtoList = new ArrayList<LuxShowcasecomponentWsDTO>();
		final LuxBlpCompListWsDTO luxComponent = new LuxBlpCompListWsDTO();

		if (null != luxurywhyourproductsComponent)
		{
			final List<MplShowcaseItemComponentModel> showcaseParaList = luxurywhyourproductsComponent.getShowcaseItems();

			int count = 1;
			for (final MplShowcaseItemComponentModel cmsshowcasePara : showcaseParaList)
			{
				final LuxShowcasecomponentWsDTO showcase = new LuxShowcasecomponentWsDTO();

				if (null != cmsshowcasePara.getBannerImage() && null != cmsshowcasePara.getBannerImage().getUrl())
				{
					showcase.setImage_url(cmsshowcasePara.getBannerImage().getUrl());
				}
				if (null != cmsshowcasePara.getBannerImage() && null != cmsshowcasePara.getBannerImage().getAlttext())
				{
					showcase.setImg_alt(cmsshowcasePara.getBannerImage().getAlttext());
				}
				if (null != cmsshowcasePara.getShowByDefault())
				{
					showcase.setShowByDefault(cmsshowcasePara.getShowByDefault().booleanValue());
				}
				if (null != cmsshowcasePara.getHeaderText())
				{
					showcase.setHeading(cmsshowcasePara.getHeaderText());
				}
				if (null != cmsshowcasePara.getText())
				{
					showcase.setText(cmsshowcasePara.getText());
				}
				showcase.setPosition(Integer.valueOf(count++));
				showcaseComponentDtoList.add(showcase);
			}
			if (null != luxurywhyourproductsComponent.getTitle())
			{
				luxComponent.setTitle(luxurywhyourproductsComponent.getTitle());
			}
			else
			{
				luxComponent.setTitle("Why Our Products");
			}
			luxComponent.setWhy_our_product_component(showcaseComponentDtoList);
		}
		return luxComponent;
	}

	private LuxBlpCompListWsDTO getBlpAdvanceCategory(final MplAdvancedCategoryCarouselComponentModel luxuryCategoryComponent,
			final String brandCode)
	{
		// YTODO Auto-generated method stub
		final ArrayList<LuxShopYourFavListWsDTO> shopYourFavList = new ArrayList<LuxShopYourFavListWsDTO>();
		//	final LuxShopYourFavListWsDTO luxshopYourFav = new LuxShopYourFavListWsDTO();
		final LuxBlpCompListWsDTO luxComponentents = new LuxBlpCompListWsDTO();
		int position = 1;
		for (final MplImageCategoryComponentModel catCompObj : luxuryCategoryComponent.getCategories())
		{
			final LuxShopYourFavListWsDTO luxshopYourFav = new LuxShopYourFavListWsDTO();
			if (null != catCompObj.getCategory() && null != catCompObj.getCategory().getCode())
			{
				luxshopYourFav.setCategoryId(catCompObj.getCategory().getCode());
			}
			if (catCompObj.getIsImageFromPCM().booleanValue())
			{
				luxshopYourFav.setCategoryImageUrl(getCategoryMediaUrl(catCompObj.getCategory()));
			}
			else
			{
				if (null != catCompObj.getImage() && StringUtils.isNotEmpty(catCompObj.getImage().getURL()))
				{
					luxshopYourFav.setCategoryImageUrl(catCompObj.getImage().getURL());
					if (null != catCompObj.getMobileImage() && StringUtils.isNotEmpty(catCompObj.getMobileImage().getURL()))
					{
						luxshopYourFav.setMobileCategoryImageUrl(catCompObj.getMobileImage().getURL());
					}
				}
			}
			//TISPRD-9374
			if (StringUtils.isNotEmpty(catCompObj.getFilterByBrandName()))
			{
				luxshopYourFav.setBrandId(catCompObj.getFilterByBrandName());
			}

			else
			{

				if (StringUtils.isNotEmpty(brandCode))
				{
					luxshopYourFav.setBrandId(brandCode);

				}
			}


			if (null != catCompObj.getCategory() && null != catCompObj.getCategory().getName())
			{
				luxshopYourFav.setCategoryName(catCompObj.getCategory().getName());
			}
			if (null != catCompObj.getCategory())
			{
				luxshopYourFav.setCategoryUrl(defaultCategoryModelUrlResolver.resolve(catCompObj.getCategory()));
			}

			if (null != catCompObj.getImage() && null != catCompObj.getImage().getAltText())
			{
				luxshopYourFav.setAltText(catCompObj.getImage().getAltText());
			}
			luxshopYourFav.setPosition(Integer.valueOf(position));
			position++;

			shopYourFavList.add(luxshopYourFav);
		}
		luxComponentents.setCategory_component(shopYourFavList);
		return luxComponentents;


	}

	/**
	 * @param luxuryBlpVideoComponent
	 * @return
	 */

	private LuxBlpCompListWsDTO getLuxBlpVideocomponentWsDTO(final VideoComponentModel luxuryBlpVideoComponent)
	{
		final ArrayList<LuxVideocomponentWsDTO> videoComponentDtoList = new ArrayList<LuxVideocomponentWsDTO>();
		final LuxVideocomponentWsDTO video = new LuxVideocomponentWsDTO();
		final LuxBlpCompListWsDTO luxBlpComponent = new LuxBlpCompListWsDTO();
		if (null != luxuryBlpVideoComponent)
		{
			if (null != luxuryBlpVideoComponent.getVideoDescription())

			{

				video.setVideoDescription(luxuryBlpVideoComponent.getVideoDescription());
			}

			if (null != luxuryBlpVideoComponent.getVideoUrl())
			{
				video.setVideoUrl(luxuryBlpVideoComponent.getVideoUrl());
			}
			if (null != luxuryBlpVideoComponent.getVideoTitle())
			{
				video.setVideoTitle(luxuryBlpVideoComponent.getVideoTitle());
			}

			if (null != luxuryBlpVideoComponent.getPreviewUrl())
			{
				video.setPreviewUrl(luxuryBlpVideoComponent.getPreviewUrl());
			}

			if (null != luxuryBlpVideoComponent.getVideoSectionHeading())
			{
				video.setVideoSectionHeading(luxuryBlpVideoComponent.getVideoSectionHeading());
			}

			videoComponentDtoList.add(video);

			luxBlpComponent.setVideocomponent(videoComponentDtoList);
		}
		return luxBlpComponent;
	}



	/**
	 * @param luxuryBannerComponent
	 * @return
	 */
	/**
	 * @param luxuryBannerComponent
	 * @return
	 */
	private LuxBlpCompListWsDTO getForHimHerBannerWsDTO(final RotatingImagesComponentModel luxuryBannerComponent)
	{
		final LuxBlpCompListWsDTO luxComponentforGender = new LuxBlpCompListWsDTO();
		final ArrayList<LuxHeroBannerWsDTO> genderlist = new ArrayList<LuxHeroBannerWsDTO>();
		int sequenceno = 1;
		for (final BannerComponentModel banner : luxuryBannerComponent.getBanners())
		{
			final LuxHeroBannerWsDTO heroBanner = new LuxHeroBannerWsDTO();
			MplBigPromoBannerComponentModel promotionalBanner = null;

			String bannerMediaUrl = null;
			String altText = null;
			String resolution = null;
			if (banner instanceof MplBigPromoBannerComponentModel)
			{
				promotionalBanner = (MplBigPromoBannerComponentModel) banner;

				if (null != promotionalBanner.getBannerImage() && StringUtils.isNotEmpty(promotionalBanner.getBannerImage().getURL()))
				{
					bannerMediaUrl = promotionalBanner.getBannerImage().getURL();
					altText = promotionalBanner.getBannerImage().getAltText();
					if (null != promotionalBanner.getBannerView() && null != promotionalBanner.getBannerView().getCode())
					{
						resolution = promotionalBanner.getBannerView().getCode().toString();
					}
					LOG.debug("added for him/her banner by MplBigPromoBannerComponentModel");
				}
			}
			else
			{
				if (null != banner.getMedia())
				{
					bannerMediaUrl = banner.getMedia().getURL();
					altText = banner.getMedia().getAltText();
					if (null != banner.getBannerView() && null != banner.getBannerView().getCode())
					{
						resolution = banner.getBannerView().getCode().toString();
					}
				}
			}
			heroBanner.setBannerNumber(sequenceno);
			heroBanner.setBannerMedia(bannerMediaUrl);
			heroBanner.setAltText(altText);
			heroBanner.setBannerUrl(banner.getUrlLink());
			heroBanner.setResolution(resolution);
			sequenceno++;

			genderlist.add(heroBanner);
		}
		luxComponentforGender.setGender_component(genderlist);
		return luxComponentforGender;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#getMegaNavigation()
	 */
	@Override
	public LuxNavigationWsDTO getMegaNavigation() throws CMSItemNotFoundException
	{
		LuxNavigationWsDTO megaNavigationDto = new LuxNavigationWsDTO();
		NavigationBarCollectionComponentModel cmsMegaNav = new NavigationBarCollectionComponentModel();
		cmsMegaNav = getMegaNavNode();
		if (null != cmsMegaNav)
		{
			megaNavigationDto = getMegaNavigationWsDTO(cmsMegaNav);
		}
		return megaNavigationDto;
	}

	/**
	 * Fetched the shop by department component
	 * 
	 * @return DepartmentCollectionComponentModel
	 * @throws CMSItemNotFoundException
	 */
	private NavigationBarCollectionComponentModel getMegaNavNode() throws CMSItemNotFoundException
	{
		String navNodeId = null;
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.MEGANAVNODE)
		{
			navNodeId = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.MEGANAVNODE, "");
		}

		final NavigationBarCollectionComponentModel negaNavNode = cmsComponentService.getSimpleCMSComponent(navNodeId);
		return negaNavNode;
	}


	/**
	 * @param cmsMegaNav
	 * @return
	 */
	private LuxNavigationWsDTO getMegaNavigationWsDTO(final NavigationBarCollectionComponentModel cmsMegaNav)
	{
		final LuxNavigationWsDTO megaNavDto = new LuxNavigationWsDTO();
		final ArrayList<LuxLevelOneWsDTO> levelOneList = new ArrayList<LuxLevelOneWsDTO>();
		if (null != cmsMegaNav)
		{
			for (final NavigationBarComponentModel navBar : cmsMegaNav.getComponents())
			{
				final ArrayList<LuxLevelTwoWsDTO> levelTwoListDto = new ArrayList<LuxLevelTwoWsDTO>();
				CMSNavigationNodeModel cmsNav = new CMSNavigationNodeModel();
				final LuxLevelOneWsDTO cmsChildNavDto = new LuxLevelOneWsDTO();
				cmsNav = navBar.getNavigationNode();
				if (null != cmsNav)
				{
					if (null != cmsNav.getTitle())
					{
						cmsChildNavDto.setName(cmsNav.getTitle());
					}

					if (CollectionUtils.isNotEmpty(cmsNav.getLinks()))
					{
						if (null != cmsNav.getLinks().get(0).getCategoryCode())
						{
							cmsChildNavDto.setValue(cmsNav.getLinks().get(0).getCategoryCode());
						}

						if (null != cmsNav.getLinks().get(0).getUrl())
						{
							cmsChildNavDto.setDestination(cmsNav.getLinks().get(0).getUrl());
						}

						else if (null != cmsNav.getLinks().get(0).getCategoryCode())
						{
							final CategoryModel categoryL1 = getCategoryService().getCategoryForCode(
									cmsNav.getLinks().get(0).getCategoryCode());
							cmsChildNavDto.setDestination(defaultCategoryModelUrlResolver.resolve(categoryL1));
						}

						else if (null != cmsNav.getLinks().get(0).getCategory())
						{
							cmsChildNavDto.setDestination(defaultCategoryModelUrlResolver
									.resolve(cmsNav.getLinks().get(0).getCategory()));
						}
					}

					if (CollectionUtils.isNotEmpty(cmsNav.getChildren()))
					{
						for (final CMSNavigationNodeModel levelTwoComp : cmsNav.getChildren())
						{
							//levelTwo is for clothings,essential
							//final MplNavigationNodeComponentModel levelTwoComp = (MplNavigationNodeComponentModel) levelTwo;
							final LuxLevelTwoWsDTO level2Dto = new LuxLevelTwoWsDTO();
							final ArrayList<LuxLevelThreeWsDTO> levelThreeListDto = new ArrayList<LuxLevelThreeWsDTO>();
							final ArrayList<LuxLevelTwoSliderWsDTO> levelTwoSliderListDto = new ArrayList<LuxLevelTwoSliderWsDTO>();
							if (null != levelTwoComp.getTitle())
							{
								level2Dto.setName(levelTwoComp.getTitle());
							}
							if (CollectionUtils.isNotEmpty(levelTwoComp.getLinks()))
							{
								if (null != levelTwoComp.getLinks().get(0).getCategoryCode())
								{
									level2Dto.setValue(levelTwoComp.getLinks().get(0).getCategoryCode());
								}

								if (null != levelTwoComp.getLinks().get(0).getCategory())
								{
									level2Dto.setImage(getCategoryMediaUrl(levelTwoComp.getLinks().get(0).getCategory()));
								}

								if (null != levelTwoComp.getLinks().get(0).getUrl())
								{
									level2Dto.setDestination(levelTwoComp.getLinks().get(0).getUrl());
								}
								else if (null != levelTwoComp.getLinks().get(0).getCategoryCode())
								{
									final CategoryModel categoryL2 = getCategoryService().getCategoryForCode(
											levelTwoComp.getLinks().get(0).getCategoryCode());
									level2Dto.setDestination(defaultCategoryModelUrlResolver.resolve(categoryL2));
								}
								else if (null != levelTwoComp.getLinks().get(0).getCategory())
								{
									level2Dto.setDestination(defaultCategoryModelUrlResolver.resolve(levelTwoComp.getLinks().get(0)
											.getCategory()));
								}

							}
							if (levelTwoComp instanceof MplNavigationNodeComponentModel)
							{
								if (CollectionUtils.isNotEmpty(((MplNavigationNodeComponentModel) levelTwoComp).getSliders()))
								{
									for (final SimpleBannerComponentModel luxLevelTwoSlider : ((MplNavigationNodeComponentModel) levelTwoComp)
											.getSliders())
									{
										final LuxLevelTwoSliderWsDTO sliderDto = new LuxLevelTwoSliderWsDTO();
										if (null != luxLevelTwoSlider.getTitle())
										{
											sliderDto.setTitle(luxLevelTwoSlider.getTitle());
										}

										if (null != luxLevelTwoSlider.getMedia() && null != luxLevelTwoSlider.getMedia().getURL())
										{
											sliderDto.setImage(luxLevelTwoSlider.getMedia().getURL());
										}

										if (null != luxLevelTwoSlider.getUrlLink())
										{
											sliderDto.setDestination(luxLevelTwoSlider.getUrlLink());
										}
										//TISLUX1491
										if (null != luxLevelTwoSlider.getDescription())
										{
											sliderDto.setCta(luxLevelTwoSlider.getDescription());
										}

										levelTwoSliderListDto.add(sliderDto);
									}
									level2Dto.setSliders(levelTwoSliderListDto);
								}
							}

							if (CollectionUtils.isNotEmpty(levelTwoComp.getChildren()))
							{
								for (final CMSNavigationNodeModel levelThreeComp : levelTwoComp.getChildren())
								{
									//For Clothing
									final LuxLevelThreeWsDTO level3Dto = new LuxLevelThreeWsDTO();
									final ArrayList<LuxLevelFourWsDTO> levelFourListDto = new ArrayList<LuxLevelFourWsDTO>();
									if (null != levelThreeComp.getTitle())
									{
										level3Dto.setName(levelThreeComp.getTitle());
									}
									if (CollectionUtils.isNotEmpty(levelThreeComp.getLinks()))
									{
										for (final CMSLinkComponentModel levelFourLink : levelThreeComp.getLinks())
										{
											final LuxLevelFourWsDTO level4Dto = new LuxLevelFourWsDTO();
											if (null != levelFourLink.getLinkName())
											{
												level4Dto.setName(levelFourLink.getLinkName());
											}

											if (null != levelFourLink.getCategoryCode())
											{
												level4Dto.setValue(levelFourLink.getCategoryCode());
											}

											if (null != levelFourLink.getCategory())
											{
												level4Dto.setImage(getCategoryMediaUrl(levelFourLink.getCategory()));
											}

											if (null != levelFourLink.getUrl())
											{
												level4Dto.setDestination(levelFourLink.getUrl());
											}

											else if (null != levelFourLink.getCategoryCode())
											{
												final CategoryModel categoryL4 = getCategoryService().getCategoryForCode(
														levelFourLink.getCategoryCode());
												level4Dto.setDestination(defaultCategoryModelUrlResolver.resolve(categoryL4));
											}
											else if (null != levelFourLink.getCategory())
											{
												level4Dto
														.setDestination(defaultCategoryModelUrlResolver.resolve(levelFourLink.getCategory()));
											}

											levelFourListDto.add(level4Dto);
										}

										level3Dto.setSubCategory(levelFourListDto);
									}

									levelThreeListDto.add(level3Dto);
								}
								level2Dto.setCollections(levelThreeListDto);
							}



							levelTwoListDto.add(level2Dto);
						}

						cmsChildNavDto.setSubcategories(levelTwoListDto);
					}
				}

				levelOneList.add(cmsChildNavDto);
			}
			megaNavDto.setIsLuxury(true);
			megaNavDto.setValues(levelOneList);
		}
		return megaNavDto;
	}

	/**
	 * @return the brandLandingPageSlotMapping
	 */
	public Map<String, String> getBrandLandingPageSlotMapping()
	{
		return brandLandingPageSlotMapping;
	}

	/**
	 * @param brandLandingPageSlotMapping
	 *           the brandLandingPageSlotMapping to set
	 */
	public void setBrandLandingPageSlotMapping(final Map<String, String> brandLandingPageSlotMapping)
	{
		this.brandLandingPageSlotMapping = brandLandingPageSlotMapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#getContentSlotData(java.lang.String)
	 */
	@Override
	public FooterComponentData getContentSlotData(final String slotId)
	{
		final FooterComponentData fData = new FooterComponentData();
		FooterComponentModel footer = null;
		final ContentSlotModel footerSlot = contentSlotService.getContentSlotForId(slotId);
		final NeedHelpComponentModel needHelpFooter = null;

		if (null != footerSlot && CollectionUtils.isNotEmpty(footerSlot.getCmsComponents()))
		{
			for (final AbstractCMSComponentModel cmsComponentModel : footerSlot.getCmsComponents())
			{
				if (cmsComponentModel instanceof FooterComponentModel)
				{
					footer = (FooterComponentModel) cmsComponentModel;
				}

			}
			//footerData = footerDataConverter.convert(footer);
			final List<SimpleBannerComponentModel> footerAppImageModelList = (List<SimpleBannerComponentModel>) footer
					.getFooterAppImageList();
			final List<SimpleBannerComponentData> footerAppImageDataList = new ArrayList<SimpleBannerComponentData>();

			for (final SimpleBannerComponentModel source : footerAppImageModelList)
			{
				final SimpleBannerComponentData target = new SimpleBannerComponentData();
				mplBannerConverter.convert(source, target);
				footerAppImageDataList.add(target);
			}

			fData.setFooterAppImageList(footerAppImageDataList);
		}

		fData.setContactNumber((needHelpFooter == null) ? "" : needHelpFooter.getContactNumber());
		//fData.setFooterAppImageList(footerData.getFooterAppImageList());
		//fData.setFooterSocialIconList(footerData.getFooterSocialIconList());
		fData.setFooterText(footer.getFooterText());
		fData.setNavigationNodes(footer.getNavigationNodes());
		fData.setNotice(footer.getNotice());
		if (null != footer.getWrapAfter())
		{
			fData.setWrapAfter(footer.getWrapAfter().toString());
		}
		else
		{
			fData.setWrapAfter("");
		}

		return fData;
	}

	/**
	 * 
	 * 
	 * 
	 * @param void
	 * @return Map<Integer, Map<Integer, FooterLinkData>>
	 */
	@Override
	public Map<Integer, Map<Integer, FooterLinkData>> getFooterLinkData()
	{
		final List<MplFooterLinkModel> footerLinkModelList = getMplCMSPageService().getAllMplFooterLinks();
		final List<FooterLinkData> mplFooterLinkDataList = new ArrayList<FooterLinkData>();
		if (CollectionUtils.isNotEmpty(footerLinkModelList))
		{
			for (final MplFooterLinkModel footerLink : footerLinkModelList)
			{
				final FooterLinkData footerLinkData = getFooterLinkConverter().convert(footerLink);
				mplFooterLinkDataList.add(footerLinkData);
			}
		}

		Map<Integer, Map<Integer, FooterLinkData>> outerMap = null;

		int rowCount = -1;
		if (CollectionUtils.isNotEmpty(mplFooterLinkDataList))
		{
			outerMap = new LinkedHashMap<Integer, Map<Integer, FooterLinkData>>();
			Map<Integer, FooterLinkData> innerMap = null;
			for (final FooterLinkData footerLinkData : mplFooterLinkDataList)
			{
				if (footerLinkData.getFooterLinkRow().intValue() > rowCount)
				{
					if (MapUtils.isNotEmpty(innerMap))
					{
						outerMap.put(Integer.valueOf(rowCount), innerMap);
					}
					innerMap = new LinkedHashMap<Integer, FooterLinkData>();
					rowCount = footerLinkData.getFooterLinkRow().intValue();
				}
				innerMap.put(footerLinkData.getFooterLinkColumn(), footerLinkData);
			}
			outerMap.put(Integer.valueOf(rowCount), innerMap);
		}

		return outerMap;
	}

	/**
	 * @return AmpServiceWorkerData
	 */
	@Override
	public AmpServiceWorkerData getAmpServiceWorkerData()
	{
		// The call for fetching list of serviceworker models
		final AmpServiceworkerModel ampServiceWorkerModel = getMplCMSPageService().getAmpServiceworkers();

		//The data object to convert model to data
		AmpServiceWorkerData ampServiceWorkerData = null;
		if (null != ampServiceWorkerModel)
		{
			ampServiceWorkerData = getAmpServiceworkerConverter().convert(ampServiceWorkerModel);
		}
		return ampServiceWorkerData;
	}

	/**
	 * @return the ampServiceworkerConverter
	 */
	public Converter<AmpServiceworkerModel, AmpServiceWorkerData> getAmpServiceworkerConverter()
	{
		return ampServiceworkerConverter;
	}

	/**
	 * @param ampServiceworkerConverter
	 *           the ampServiceworkerConverter to set
	 */
	public void setAmpServiceworkerConverter(final Converter<AmpServiceworkerModel, AmpServiceWorkerData> ampServiceworkerConverter)
	{
		this.ampServiceworkerConverter = ampServiceworkerConverter;
	}

	/**
	 * @return the ampMenifestJsonConverter
	 */
	public Converter<AmpMenifestModel, AmpMenifestData> getAmpMenifestJsonConverter()
	{
		return ampMenifestJsonConverter;
	}

	/**
	 * @param ampMenifestJsonConverter
	 *           the ampMenifestJsonConverter to set
	 */
	public void setAmpMenifestJsonConverter(final Converter<AmpMenifestModel, AmpMenifestData> ampMenifestJsonConverter)
	{
		this.ampMenifestJsonConverter = ampMenifestJsonConverter;
	}

	/**
	 * @return AmpMenifestData
	 */
	@Override
	public AmpMenifestData getAmpMenifestData()
	{
		// The call for fetching list of AmpMenifest models
		final AmpMenifestModel ampMenifestModel = getMplCMSPageService().getAmpMenifestJsons();

		//The data object to convert model to data
		AmpMenifestData ampMenifestData = null;
		if (null != ampMenifestModel)
		{
			ampMenifestData = getAmpMenifestJsonConverter().convert(ampMenifestModel);
		}

		return ampMenifestData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.cms.MplCmsFacade#getHomePagePreferece(java.lang.String, java.lang.String)
	 */
	@Override
	public LuxuryHomePagePreferenceModel getHomePagePreference(final String gender, final String category)
	{
		// YTODO Auto-generated method stub
		return mplCMSPageService.getHomePagePreference(gender, category);
	}

}
