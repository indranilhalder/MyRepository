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
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercesearch.model.SolrHeroProductDefinitionModel;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.HeroProductDefinitionService;
import de.hybris.platform.converters.Converters;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.MplShopByLookModel;
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
import com.tisl.mpl.facades.cms.data.LinkedCollectionsData;
import com.tisl.mpl.facades.cms.data.MplPageData;
import com.tisl.mpl.facades.cms.data.PageData;
import com.tisl.mpl.facades.cms.data.ProductListComponentData;
import com.tisl.mpl.facades.cms.data.PromotionComponentData;
import com.tisl.mpl.facades.cms.data.SectionData;
import com.tisl.mpl.facades.cms.data.TextComponentData;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerMasterService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.cms.components.MobileAppCollectionHeroComponentModel;
import com.tisl.mpl.model.cms.components.MobileAppComponentModel;
import com.tisl.mpl.model.cms.components.MobileAppHeroComponentModel;
import com.tisl.mpl.model.cms.components.MobileBannerComponentModel;
import com.tisl.mpl.model.cms.components.MobileCollectionBannerComponentModel;
import com.tisl.mpl.model.cms.components.MobileCollectionLinkComponentModel;
import com.tisl.mpl.model.cms.components.PromotionalProductsComponentModel;
import com.tisl.mpl.model.cms.components.SmallBrandMobileAppComponentModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;


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


	@Override
	public List<MplPageData> getPageInformationForPageId(final String homePageUid)
	{
		final ContentPageModel contentPage = getMplCMSPageService().getHomePageForMobile(homePageUid);

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
							banners.add(banner);
						}
						//homePageData.setBannerComponents(bannerComponents);
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
								productComp.setImage(productModel.getPicture().getUrl2());
							}
							catch (final Exception e)
							{
								//handle exception logic TBD
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
							productForShowCase.add(productComp);

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
							}
						}

						//homePageData.setBannerComponents(bannerComponents);
					}
					//homePageData.setSequence(new Integer(count));  // Sonar Fixes

					//componentDatas.add(homePageData);
				}
				Collections.sort(lastModifiedTimes);
				homePageData.setLastModifiedTime(lastModifiedTimes.get(lastModifiedTimes.size() - 1));
				homePageData.setTextComponents(texts);
				homePageData.setProductComponents(productForShowCase);
				homePageData.setBannerComponents(banners);
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



}
