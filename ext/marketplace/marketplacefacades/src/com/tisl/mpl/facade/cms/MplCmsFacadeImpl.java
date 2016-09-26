/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercesearch.model.SolrHeroProductDefinitionModel;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.HeroProductDefinitionService;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.MplAdvancedCategoryCarouselComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplImageCategoryComponentModel;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.core.model.MplShowcaseComponentModel;
import com.tisl.mpl.core.model.MplShowcaseItemComponentModel;
import com.tisl.mpl.core.model.SignColComponentModel;
import com.tisl.mpl.core.model.SignColItemComponentModel;
import com.tisl.mpl.core.model.VideoComponentModel;
import com.tisl.mpl.facades.cms.data.BannerComponentData;
import com.tisl.mpl.facades.cms.data.CollectionComponentData;
import com.tisl.mpl.facades.cms.data.CollectionHeroComponentData;
import com.tisl.mpl.facades.cms.data.CollectionPageData;
import com.tisl.mpl.facades.cms.data.CollectionProductData;
import com.tisl.mpl.facades.cms.data.CollectionSectionData;
import com.tisl.mpl.facades.cms.data.ComponentData;
import com.tisl.mpl.facades.cms.data.HeroComponentData;
import com.tisl.mpl.facades.cms.data.HeroProductData;
import com.tisl.mpl.facades.cms.data.HomePageComponentData;
import com.tisl.mpl.facades.cms.data.ImageListComponentData;
import com.tisl.mpl.facades.cms.data.LinkedCollectionsData;
import com.tisl.mpl.facades.cms.data.MplPageData;
import com.tisl.mpl.facades.cms.data.PageData;
import com.tisl.mpl.facades.cms.data.ProductListComponentData;
import com.tisl.mpl.facades.cms.data.PromotionComponentData;
import com.tisl.mpl.facades.cms.data.SectionData;
import com.tisl.mpl.facades.cms.data.TextComponentData;
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
import com.tisl.mpl.model.cms.components.PromotionalProductsComponentModel;
import com.tisl.mpl.model.cms.components.SmallBrandMobileAppComponentModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.LuxBlpCompListWsDTO;
import com.tisl.mpl.wsdto.LuxBlpCompWsDTO;
import com.tisl.mpl.wsdto.LuxBrandProductsListWsDTO;
import com.tisl.mpl.wsdto.LuxComponentsListWsDTO;
import com.tisl.mpl.wsdto.LuxEngagementcomponentWsDTO;
import com.tisl.mpl.wsdto.LuxHeroBannerWsDTO;
import com.tisl.mpl.wsdto.LuxHomePageCompWsDTO;
import com.tisl.mpl.wsdto.LuxShopByListWsDTO;
import com.tisl.mpl.wsdto.LuxShopYourFavListWsDTO;
import com.tisl.mpl.wsdto.LuxShowcasecomponentWsDTO;
import com.tisl.mpl.wsdto.LuxSignatureWsDTO;
import com.tisl.mpl.wsdto.LuxVideocomponentWsDTO;
import com.tisl.mpl.wsdto.TextComponentWsDTO;


/**
 * @author 584443
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

	private final String HEROSTATUS = "Success";

	private final String HEROERROR_NOPROD = "No Hero Products available";

	private final String HEROERROR_NOCAT = "Invalid Category Id";

	private final String CONTENTPAGE = "Content";

	private MplCMSPageServiceImpl mplCMSPageService;

	private DefaultCategoryService categoryService;

	private ConfigurationService configurationService;

	private Converter<MobileAppHeroComponentModel, HeroComponentData> mobileHeroComponentConverter;

	private Converter<SmallBrandMobileAppComponentModel, ComponentData> mobileSubBrandComponentConverter;

	private Converter<MobileCollectionBannerComponentModel, CollectionComponentData> mobileCollectionComponentConverter;

	private Converter<SmallBrandMobileAppComponentModel, ComponentData> mobileCategoryComponentConverter;

	private Converter<MobileAppCollectionHeroComponentModel, CollectionHeroComponentData> mobileCollectionHeroComponentConverter;

	private Converter<ProductModel, CollectionProductData> mobileCollectionProductConverter;

	private HeroProductDefinitionService heroProductDefinitionService;

	private MplSellerMasterService sellerMasterService;

	private static final Logger LOG = Logger.getLogger(MplCmsFacadeImpl.class);
	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;

	@Resource(name = "productService")
	private ProductService productService;


	@Resource(name = "defaultCategoryModelUrlResolver")
	private ExtDefaultCategoryModelUrlResolver defaultCategoryModelUrlResolver;

	@Resource(name = "defaultProductModelUrlResolver")
	private ExtDefaultProductModelUrlResolver defaultProductModelUrlResolver;

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
		if (null != contentSlot)
		{

			for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlot.getCmsComponents())
			{
				final String typecode = abstractCMSComponentModel.getTypeCode();
				if (typecode.equalsIgnoreCase("RotatingImagesComponent"))
				{
					final RotatingImagesComponentModel luxuryBannerComponent = (RotatingImagesComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxHeroBannerWsDTO(luxuryBannerComponent);
				}
				else if (typecode.equalsIgnoreCase("SignColComponent"))
				{
					final SignColComponentModel luxurySignatureCollectionComponent = (SignColComponentModel) abstractCMSComponentModel;
					luxuryComponent = getSignatureCollectionWsDTO(luxurySignatureCollectionComponent);
				}
				else if (typecode.equalsIgnoreCase("MplAdvancedCategoryCarouselComponent"))
				{
					final MplAdvancedCategoryCarouselComponentModel luxuryCategoryComponent = (MplAdvancedCategoryCarouselComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxShopYourFavListWsDTO(luxuryCategoryComponent);
				}
				else if (typecode.equalsIgnoreCase("VideoComponent"))
				{
					final VideoComponentModel luxuryVideoComponent = (VideoComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxVideocomponentWsDTO(luxuryVideoComponent);
				}
				else if (typecode.equalsIgnoreCase("MplBigPromoBannerComponent"))
				{
					// To do for this
					final MplBigPromoBannerComponentModel engagementComponent = (MplBigPromoBannerComponentModel) abstractCMSComponentModel;

					luxuryComponent = getLuxEngagementcomponentWsDTO(engagementComponent);
				}
				else if (typecode.equalsIgnoreCase("ProductCarouselComponent"))
				{
					final ProductCarouselComponentModel luxuryProductListComponent = (ProductCarouselComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxProductsListWsDTO(luxuryProductListComponent);
				}
				else if (typecode.equalsIgnoreCase("ImageCarouselComponent"))
				{
					final ImageCarouselComponentModel luxuryShopByComponent = (ImageCarouselComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxShopByListWsDTO(luxuryShopByComponent);
				}
				// Social Feed Component added
				else if (typecode.equalsIgnoreCase("CMSParagraphComponent"))
				{
					final CMSParagraphComponentModel socialFeedComponent = (CMSParagraphComponentModel) abstractCMSComponentModel;
					luxuryComponent = getLuxSocialFeedcomponentWsDTO(socialFeedComponent);
				}
				LOG.debug("Adding component" + abstractCMSComponentModel.getUid() + "for section" + contentSlot.getUid());
				luxuryComponent.setSectionid(contentSlot.getUid());
				componentListForASlot.add(luxuryComponent);

			}

		}
		//}
		return componentListForASlot;
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

			if (null != signColItem.getBannerImage() && null != signColItem.getBannerImage().getUrl2())
			{
				signColItemWsDto.setBrandBannerImageUrl(signColItem.getBannerImage().getUrl2());
			}

			if (null != signColItem.getShowByDefault())
			{
				signColItemWsDto.setShowByDefault(signColItem.getShowByDefault().booleanValue());
			}

			if (null != signColItem.getLogo() && null != signColItem.getLogo().getUrl2())
			{
				signColItemWsDto.setBrandLogoUrl(signColItem.getLogo().getUrl2());
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

		luxComponent.setTitle("Signature Collections");
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

		if (null != product.getPicture() && null != product.getPicture().getUrl2())
		{
			productDto.setProductImageUrl(product.getPicture().getUrl2());
		}

		if (StringUtils.isNotEmpty(product.getCode()))
		{
			final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(product.getCode());
			if (buyboxdata.getMrp() != null)
			{
				productDto.setProductPrice(buyboxdata.getMrp().getFormattedValue());
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
		luxComponent.setTitle("Shop by");
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

				if (null != product.getPicture())
				{
					productDto.setProductImageUrl(product.getPicture().getUrl2());
				}
				if (StringUtils.isNotEmpty(productCode))
				{
					final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode);
					if (buyboxdata.getPrice() != null)
					{
						productDto.setProductMOP(buyboxdata.getPrice().getFormattedValue());
					}
					if (buyboxdata.getSpecialPrice() != null)
					{
						productDto.setProductMOP(buyboxdata.getSpecialPrice().getFormattedValue());
					}
					if (buyboxdata.getMrp() != null)
					{
						productDto.setProductPrice(buyboxdata.getMrp().getFormattedValue());
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
		luxComponent.setTitle("Featured Products");
		luxComponent.setFeaturedproducts(productList);
		return luxComponent;



	}


	/**
	 * @param bigPromoBannerModel
	 * @return
	 */
	private LuxComponentsListWsDTO getLuxEngagementcomponentWsDTO(final MplBigPromoBannerComponentModel bigPromoBannerModel)
	{
		final ArrayList<LuxEngagementcomponentWsDTO> engagementDtoList = new ArrayList<LuxEngagementcomponentWsDTO>();
		final LuxEngagementcomponentWsDTO engagementDto = new LuxEngagementcomponentWsDTO();
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
			engagementDtoList.add(engagementDto);
			luxcomponentObj.setEngagementcomponent(engagementDtoList);
		}
		return luxcomponentObj;
	}

	/**
	 * @param socialFeedComponent
	 */
	private LuxComponentsListWsDTO getLuxSocialFeedcomponentWsDTO(final CMSParagraphComponentModel socialFeedComponent)
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
		luxComponent.setSocialfeedcomponent(text);
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
	public List<MplPageData> getPageInformationForPageId(final String pageUid)
	{
		final ContentPageModel contentPage = getMplCMSPageService().getPageForAppById(pageUid);

		final List<MplPageData> componentDatas = new ArrayList<MplPageData>();
		final List<Date> lastModifiedTimes = new ArrayList<Date>();
		//final int count = 0;

		if (contentPage != null)
		{
			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				final MplPageData homePageData = new MplPageData();
				final List<TextComponentData> texts = new ArrayList<TextComponentData>();
				final List<BannerComponentData> banners = new ArrayList<BannerComponentData>();
				final List<ProductListComponentData> productForShowCase = new ArrayList<ProductListComponentData>();
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
								carouselBanners.add(banner);
							}
						}
						imageListData.setBannerslist(carouselBanners);
						imageCarousels.add(imageListData);
					}


					else if (abstractCMSComponentModel instanceof ProductCarouselComponentModel)
					{
						final ProductCarouselComponentModel productCarousel = (ProductCarouselComponentModel) abstractCMSComponentModel;
						final List<String> products = productCarousel.getProductCodes();

						for (final String productCode : products)
						{
							final ProductListComponentData productComp = new ProductListComponentData();
							final ProductData product = productFacade.getProductForCodeAndOptions(productCode,
									Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.CATEGORIES));
							ProductModel productModel = null;
							final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode);
							try
							{
								productModel = productService.getProductForCode(productCode);
								if (null != productModel && null != productModel.getPicture())
								{
									productComp.setImage(productModel.getPicture().getUrl2());
								}
							}
							catch (final Exception e)
							{
								LOG.warn("Encountered error while seting product image. ", e);
							}
							if (product.getName() != null)
							{
								productComp.setName(product.getName());
							}
							if (buyboxdata.getPrice() != null)
							{
								productComp.setPrice(buyboxdata.getMrp().getFormattedValue());
							}
							if (buyboxdata.getMrp() != null)
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
							productForShowCase.add(productComp);

						}

						//homePageData.setBannerComponents(bannerComponents);
					}
					//homePageData.setSequence(new Integer(count));  // Sonar Fixes

					//componentDatas.add(homePageData);
				}
				if (lastModifiedTimes.size() >= 1)
				{
					Collections.sort(lastModifiedTimes);
					homePageData.setLastModifiedTime(lastModifiedTimes.get(lastModifiedTimes.size() - 1));
				}
				else
				{
					homePageData.setLastModifiedTime(contentPage.getModifiedtime());
				}
				homePageData.setTextComponents(texts);
				homePageData.setProductComponents(productForShowCase);
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
	public LuxBlpCompWsDTO getlandingForBrand() throws CMSItemNotFoundException
	{
		// YTODO Auto-generated method stub
		final ContentPageModel contentPage = getMplCMSPageService().getPageByLabelOrId("luxurybrandlandingpage");
		final LuxBlpCompWsDTO luxBlpPageDto = new LuxBlpCompWsDTO();

		if (contentPage != null)
		{

			final ArrayList<LuxBlpCompListWsDTO> listComp = new ArrayList<LuxBlpCompListWsDTO>();
			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				final ContentSlotModel contentSlot = contentSlotForPage.getContentSlot();
				final List<LuxBlpCompListWsDTO> luxuryComponentsForASlot = getComponentDtoforASlot(contentSlot);
				listComp.addAll(luxuryComponentsForASlot);
			}

			luxBlpPageDto.setComponents(listComp);
			luxBlpPageDto.setPageTitle(contentPage.getTitle());
			luxBlpPageDto.setMetaDescription(contentPage.getDescription());
			luxBlpPageDto.setMetaKeywords(contentPage.getKeywords());
		}
		return luxBlpPageDto;

	}

	/**
	 * @param contentSlot
	 * @return
	 */
	private List<LuxBlpCompListWsDTO> getComponentDtoforASlot(final ContentSlotModel contentSlot) throws CMSItemNotFoundException
	{
		// YTODO Auto-generated method stub
		final List<LuxBlpCompListWsDTO> componentListForASlot = new ArrayList<LuxBlpCompListWsDTO>();
		LuxBlpCompListWsDTO blpComponent = new LuxBlpCompListWsDTO();

		if (null != contentSlot)
		{

			for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlot.getCmsComponents())
			{
				final String typecode = abstractCMSComponentModel.getTypeCode();
				if (typecode.equalsIgnoreCase("RotatingImagesComponent")
						&& contentSlot.getUid().equalsIgnoreCase("Section2-luxurybrandlandingpage"))
				{
					final RotatingImagesComponentModel luxuryBannerComponent = (RotatingImagesComponentModel) abstractCMSComponentModel;
					blpComponent = getForHimHerBannerWsDTO(luxuryBannerComponent);
				}
				else if (typecode.equalsIgnoreCase("VideoComponent"))
				{
					final VideoComponentModel BlpVideoComponent = (VideoComponentModel) abstractCMSComponentModel;
					blpComponent = getLuxBlpVideocomponentWsDTO(BlpVideoComponent);
				}
				else if (typecode.equalsIgnoreCase("MplAdvancedCategoryCarouselComponent"))
				{
					final MplAdvancedCategoryCarouselComponentModel BlpVideoComponent = (MplAdvancedCategoryCarouselComponentModel) abstractCMSComponentModel;
					blpComponent = getBlpAdvanceCategory(BlpVideoComponent);
				}
				if (typecode.equalsIgnoreCase("MplShowcaseComponent"))
				{
					final MplShowcaseComponentModel luxurywhyourproductsComponent = (MplShowcaseComponentModel) abstractCMSComponentModel;
					blpComponent = getwhyourproductsWsDTO(luxurywhyourproductsComponent);
				}
				LOG.debug("Adding component" + abstractCMSComponentModel.getUid() + "for section" + contentSlot.getUid());

				blpComponent.setSectionid(contentSlot.getUid());
				componentListForASlot.add(blpComponent);
			}



		}
		return componentListForASlot;

	}

	/**
	 * @param luxuryBannerComponent
	 * @return
	 */
	private LuxBlpCompListWsDTO getwhyourproductsWsDTO(final MplShowcaseComponentModel luxurywhyourproductsComponent)
	{
		// YTODO Auto-generated method stub
		/*
		 * final ArrayList<LuxShowcasecomponentWsDTO> showcaseComponentDtoList = new
		 * ArrayList<LuxShowcasecomponentWsDTO>();
		 * 
		 * final LuxBlpCompListWsDTO luxComponent = new LuxBlpCompListWsDTO();
		 * 
		 * final List<MplShowcaseItemComponentModel> showcaseParaList = luxurywhyourproductsComponent.getShowcaseItems();
		 * 
		 * final ArrayList<TabWsDTO> tabDtoList = new ArrayList<TabWsDTO>(); //final LuxShowcasecomponentWsDTO showcase =
		 * new LuxShowcasecomponentWsDTO();
		 * 
		 * for (final MplShowcaseItemComponentModel cmsshowcasePara : showcaseParaList) { final LuxShowcasecomponentWsDTO
		 * showcase = new LuxShowcasecomponentWsDTO();
		 * 
		 * //final ArrayList<TabWsDTO> tabDtoList = new ArrayList<TabWsDTO>();
		 * 
		 * final TabWsDTO tab = new TabWsDTO();
		 * 
		 * if (null != cmsshowcasePara.getBannerImage().getUrl()) {
		 * tab.setImage_url(cmsshowcasePara.getBannerImage().getUrl()); } if (null != cmsshowcasePara.getText()) {
		 * tab.setText(cmsshowcasePara.getText()); } if (null != cmsshowcasePara.getHeaderText()) {
		 * tab.setHeading(cmsshowcasePara.getHeaderText()); } if (null != cmsshowcasePara.getBannerImage().getAlttext()) {
		 * tab.setImg_alt(cmsshowcasePara.getBannerImage().getAlttext()); } if (null !=
		 * cmsshowcasePara.getShowByDefault()) { tab.setShowByDefault(cmsshowcasePara.getShowByDefault().booleanValue());
		 * }
		 * 
		 * tabDtoList.add(tab);
		 * 
		 * showcase.setTabs(tabDtoList);
		 * 
		 * 
		 * if (null != luxurywhyourproductsComponent.getTitle()) {
		 * showcase.setSection_header(luxurywhyourproductsComponent.getTitle()); }
		 * 
		 * showcaseComponentDtoList.add(showcase); }
		 * 
		 * if(null!= luxurywhyourproductsComponent.getTitle()) {
		 * showcase.setSection_header(luxurywhyourproductsComponent.getTitle()); }
		 * 
		 * if (null != luxurywhyourproductsComponent.getTitle()) {
		 * luxComponent.setTitle(luxurywhyourproductsComponent.getTitle()); }
		 * //luxComponent.setWhy_our_product_component(showcaseComponentDtoList); return luxComponent;
		 */
		final ArrayList<LuxShowcasecomponentWsDTO> showcaseComponentDtoList = new ArrayList<LuxShowcasecomponentWsDTO>();
		final LuxBlpCompListWsDTO luxComponent = new LuxBlpCompListWsDTO();

		if (null != luxurywhyourproductsComponent)
		{
			final List<MplShowcaseItemComponentModel> showcaseParaList = luxurywhyourproductsComponent.getShowcaseItems();


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

	private LuxBlpCompListWsDTO getBlpAdvanceCategory(final MplAdvancedCategoryCarouselComponentModel luxuryCategoryComponent)
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

			if (null != luxuryBlpVideoComponent.getPreviewUrl())
			{
				video.setPreviewUrl(luxuryBlpVideoComponent.getPreviewUrl());
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
			if (banner instanceof MplBigPromoBannerComponentModel)
			{
				promotionalBanner = (MplBigPromoBannerComponentModel) banner;

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
			heroBanner.setBannerNumber(sequenceno);
			heroBanner.setBannerMedia(bannerMediaUrl);
			heroBanner.setAltText(altText);
			heroBanner.setBannerUrl(banner.getUrlLink());
			sequenceno++;

			genderlist.add(heroBanner);
		}
		luxComponentforGender.setGender_component(genderlist);
		return luxComponentforGender;

	}

}
