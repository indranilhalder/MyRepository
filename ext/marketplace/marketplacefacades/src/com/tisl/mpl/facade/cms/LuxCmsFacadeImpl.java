/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSImageComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.lux.model.LuxuryMediaModel;
import com.tisl.lux.model.LuxuryVideoComponentModel;
import com.tisl.lux.model.cms.components.LuxShowCaseCollectionComponentModel;
import com.tisl.lux.model.cms.components.LuxShowCaseComponentModel;
import com.tisl.lux.model.cms.components.LuxuryLookBookComponentModel;
import com.tisl.lux.model.cms.components.LuxurySimpleBannerComponentModel;
import com.tisl.lux.model.cms.components.ShopOnLuxuryElementModel;
import com.tisl.lux.model.cms.components.ShopOnLuxuryModel;
import com.tisl.lux.model.cms.components.WeeklySpecialBannerModel;
import com.tisl.lux.model.cms.components.WeeklySpecialModel;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.LuxProductCarouselComponentModel;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.model.cms.components.LuxCMSMediaParagraphComponentModel;
import com.tisl.mpl.model.cms.components.ShopByCategoryModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.wsdto.LuxBannerComponentWsDTO;
import com.tisl.mpl.wsdto.LuxCMSMediaParagraphComponentListWsDTO;
import com.tisl.mpl.wsdto.LuxMediaContainerWsDTO;
import com.tisl.mpl.wsdto.LuxProductCarouselComponentWsDTO;
import com.tisl.mpl.wsdto.LuxProductCarouselProductWsDTO;
import com.tisl.mpl.wsdto.LuxRotatingImagesComponentWsDTO;
import com.tisl.mpl.wsdto.LuxShopByCategoryWsDTO;
import com.tisl.mpl.wsdto.LuxShowCaseCollectionComponentListWsDTO;
import com.tisl.mpl.wsdto.LuxShowCaseComponentListWsDTO;
import com.tisl.mpl.wsdto.LuxVideocomponentWsDTO;
import com.tisl.mpl.wsdto.LuxuryComponentsListWsDTO;
import com.tisl.mpl.wsdto.LuxuryLookBookComponentListWsDTO;
import com.tisl.mpl.wsdto.LuxuryMediaListWsDTO;
import com.tisl.mpl.wsdto.LuxuryMediaWsDTO;
import com.tisl.mpl.wsdto.LuxurySimpleBannerComponentWsDTO;
import com.tisl.mpl.wsdto.ShopOnLuxuryElementListWsDTO;
import com.tisl.mpl.wsdto.ShopOnLuxuryElementWsDTO;
import com.tisl.mpl.wsdto.WeeklySpecialBannerListWsDTO;
import com.tisl.mpl.wsdto.WeeklySpecialBannerWsDTO;

import net.sourceforge.pmd.util.StringUtil;



public class LuxCmsFacadeImpl implements LuxCmsFacade
{
	@Resource(name = "cmsRestrictionService")
	private CMSRestrictionService cmsRestrictionService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Autowired
	private BuyBoxFacade buyBoxFacade;

	private MplCMSPageServiceImpl mplCMSPageService;


	private static final Logger LOG = Logger.getLogger(LuxCmsFacadeImpl.class);

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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.cms.LuxCmsFacade#getLuxuryHomePage()
	 */
	@Override
	public LuxuryComponentsListWsDTO getLuxuryPage(final ContentPageModel contentPage) throws CMSItemNotFoundException
	{
		LuxuryComponentsListWsDTO luxuryAllComponents = new LuxuryComponentsListWsDTO();
		if (contentPage != null)
		{
			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				final ContentSlotModel contentSlotModel = contentSlotForPage.getContentSlot();
				luxuryAllComponents = getLuxuryComponentDtoForSlot(contentSlotModel, luxuryAllComponents);
			}
			for (final ContentSlotForTemplateModel contentSlotForTemplate : contentPage.getMasterTemplate().getContentSlots())
			{
				final ContentSlotModel contentSlotModel = contentSlotForTemplate.getContentSlot();
				luxuryAllComponents = getLuxuryComponentDtoForSlot(contentSlotModel, luxuryAllComponents);
			}

			luxuryAllComponents.setPageTitle(contentPage.getName());
			luxuryAllComponents.setMetaDescription(contentPage.getDescription());
			luxuryAllComponents.setMetaKeywords(contentPage.getKeywords());

		}
		return luxuryAllComponents;
	}

	public LuxuryComponentsListWsDTO getLuxuryComponentDtoForSlot(final ContentSlotModel contentSlot,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO) throws CMSItemNotFoundException
	{

		final List<AbstractCMSComponentModel> abstractCMSComponentModelList = cmsRestrictionService.evaluateCMSComponents(
				contentSlot.getCmsComponents(), null);
		LuxuryComponentsListWsDTO luxuryComponentsList = luxuryComponentsListWsDTO;

		if (null != abstractCMSComponentModelList)
		{
			for (final AbstractCMSComponentModel abstractCMSComponentModel : abstractCMSComponentModelList)
			{
				final String typecode = abstractCMSComponentModel.getTypeCode();

				switch (typecode)
				{
					case "ShopOnLuxury":
						final ShopOnLuxuryModel luxuryShopOnLuxuryComponent = (ShopOnLuxuryModel) abstractCMSComponentModel;
						luxuryComponentsList = getShopOnLuxuryWsDTO(luxuryShopOnLuxuryComponent, luxuryComponentsListWsDTO);
						break;
					case "WeeklySpecial":
						final WeeklySpecialModel weeklySpecialComponent = (WeeklySpecialModel) abstractCMSComponentModel;
						luxuryComponentsList = getWeeklySpecialWsDTO(weeklySpecialComponent, luxuryComponentsListWsDTO);
						break;
					case "ShopByCategory":
						final ShopByCategoryModel LuxShopByCategoryComponent = (ShopByCategoryModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxShopByCategoryWsDTO(LuxShopByCategoryComponent, luxuryComponentsListWsDTO);
						break;
					case "LuxCMSMediaParagraphComponent":
						final LuxCMSMediaParagraphComponentModel LuxCMSMediaParagraphComponent = (LuxCMSMediaParagraphComponentModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxCMSMediaParagraphWsDTO(LuxCMSMediaParagraphComponent, luxuryComponentsListWsDTO);
						break;
					case "LuxuryLookBookComponent":
						final LuxuryLookBookComponentModel luxuryLookBookComponent = (LuxuryLookBookComponentModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxuryLookBookComponentWsDTO(luxuryLookBookComponent, luxuryComponentsListWsDTO);
						break;
					case "LuxuryVideoComponent":
						final LuxuryVideoComponentModel luxuryVideoComponent = (LuxuryVideoComponentModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxuryVideoComponenWsDTO(luxuryVideoComponent, luxuryComponentsListWsDTO);
						break;
					case "LuxShowCaseComponent":
						final LuxShowCaseComponentModel luxShowCaseComponent = (LuxShowCaseComponentModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxShowCaseComponentWsDTO(luxShowCaseComponent, luxuryComponentsListWsDTO);
						break;
					case "RotatingImagesComponent":
						final RotatingImagesComponentModel rotatingImagesComponent = (RotatingImagesComponentModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxRotatingImagesComponentWsDTO(rotatingImagesComponent, luxuryComponentsListWsDTO);
						break;
					case "CMSImageComponent":
						final CMSImageComponentModel CMSImageComponent = (CMSImageComponentModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxCMSImagesComponentWsDTO(CMSImageComponent, luxuryComponentsListWsDTO);
						break;
					case "LuxProductCarouselComponent":
						final LuxProductCarouselComponentModel luxProductCarouselComponent = (LuxProductCarouselComponentModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxProductCarouselComponentWsDTO(luxProductCarouselComponent,
								luxuryComponentsListWsDTO);
						break;
					case "LuxurySimpleBannerComponent":
						final LuxurySimpleBannerComponentModel luxurySimpleBannerComponent = (LuxurySimpleBannerComponentModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxurySimpleBannerComponentWsDTO(luxurySimpleBannerComponent,
								luxuryComponentsListWsDTO);
						break;
					default:
						break;
				}
			}

		}
		return luxuryComponentsList;
	}

	private LuxuryComponentsListWsDTO getLuxurySimpleBannerComponentWsDTO(
			final LuxurySimpleBannerComponentModel luxurySimpleBannerComponent, final LuxuryComponentsListWsDTO luxuryComponent)
	{
		final List<LuxurySimpleBannerComponentWsDTO> objectList = new ArrayList<LuxurySimpleBannerComponentWsDTO>();
		final LuxurySimpleBannerComponentWsDTO luxurySimpleBannerComponentWsDTO = new LuxurySimpleBannerComponentWsDTO();

		if (luxurySimpleBannerComponent.getMedia() != null)
		{

			final LuxuryMediaWsDTO mediaWsDTO = new LuxuryMediaWsDTO();

			if (luxurySimpleBannerComponent.getMedia().getAltText() != null)
			{
				mediaWsDTO.setAltText(luxurySimpleBannerComponent.getMedia().getAltText());
			}

			if (luxurySimpleBannerComponent.getMedia().getMediaFormat() != null
					&& StringUtils.isNotEmpty(luxurySimpleBannerComponent.getMedia().getMediaFormat().getName()))
			{
				mediaWsDTO.setMediaFormat(luxurySimpleBannerComponent.getMedia().getMediaFormat().getName());
			}

			if (luxurySimpleBannerComponent.getMedia().getMediaType() != null)
			{
				mediaWsDTO.setMediaType(luxurySimpleBannerComponent.getMedia().getMediaType().getCode());
			}

			if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getName()))
			{
				mediaWsDTO.setName(luxurySimpleBannerComponent.getName());
			}

			if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getMedia().getURL()))
			{
				mediaWsDTO.setURL(luxurySimpleBannerComponent.getMedia().getURL());
			}

			if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getUrlLink()))
			{
				mediaWsDTO.setUrlLink(luxurySimpleBannerComponent.getUrlLink());
			}

			luxurySimpleBannerComponentWsDTO.setMedia(mediaWsDTO);
		}

		if (luxurySimpleBannerComponent.getAssociatedCategory() != null)
		{
			luxurySimpleBannerComponentWsDTO.setAssociatedCategory(luxurySimpleBannerComponent.getAssociatedCategory().getCode());
		}

		if (luxurySimpleBannerComponent.getBannerForCategory() != null)
		{
			luxurySimpleBannerComponentWsDTO.setBannerForCategory(luxurySimpleBannerComponent.getBannerForCategory().getCode());
		}

		if (StringUtils.isNotEmpty(luxurySimpleBannerComponent.getAppPosition()))
		{
			luxurySimpleBannerComponentWsDTO.setComponentPosition(luxurySimpleBannerComponent.getAppPosition());
		}
		else
		{
			luxurySimpleBannerComponentWsDTO.setComponentPosition(MarketplaceFacadesConstants.SPACE);
		}

		if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getAppPosition()))
		{
			luxurySimpleBannerComponentWsDTO.setComponentPosition(luxurySimpleBannerComponent.getAppPosition());
		}

		if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getDeeplinkType()))
		{
			luxurySimpleBannerComponentWsDTO.setDeeplinkType(luxurySimpleBannerComponent.getDeeplinkType());
		}

		if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getDeeplinkTypeId()))
		{
			luxurySimpleBannerComponentWsDTO.setDeeplinkTypeId(luxurySimpleBannerComponent.getDeeplinkTypeId());
		}

		if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getDeeplinkTypeVal()))
		{
			luxurySimpleBannerComponentWsDTO.setDeeplinkTypeVal(luxurySimpleBannerComponent.getDeeplinkTypeVal());
		}

		if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getDescription()))
		{
			luxurySimpleBannerComponentWsDTO.setDescription(luxurySimpleBannerComponent.getDescription());
		}

		if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getIcid2()))
		{
			luxurySimpleBannerComponentWsDTO.setIcid2(luxurySimpleBannerComponent.getIcid2());
		}

		if (StringUtil.isNotEmpty(luxurySimpleBannerComponent.getTitle()))
		{
			luxurySimpleBannerComponentWsDTO.setTitle(luxurySimpleBannerComponent.getTitle());
		}

		luxurySimpleBannerComponentWsDTO.setExternal(luxurySimpleBannerComponent.isExternal());

		if (luxuryComponent.getLuxurySimpleBannerComponent() != null)
		{
			luxuryComponent.getLuxurySimpleBannerComponent().add(luxurySimpleBannerComponentWsDTO);
		}
		else
		{
			objectList.add(luxurySimpleBannerComponentWsDTO);
			luxuryComponent.setLuxurySimpleBannerComponent(objectList);
		}
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getLuxProductCarouselComponentWsDTO(
			final LuxProductCarouselComponentModel luxProductCarouselComponent, final LuxuryComponentsListWsDTO luxuryComponent)
	{
		final List<LuxProductCarouselComponentWsDTO> luxProductCarouselComponentWsDTOList = new ArrayList<LuxProductCarouselComponentWsDTO>();
		final LuxProductCarouselComponentWsDTO luxProductCarouselComponentWsDTO = new LuxProductCarouselComponentWsDTO();
		final List<LuxProductCarouselProductWsDTO> luxProductCarouselProductWsDTOList = new ArrayList<LuxProductCarouselProductWsDTO>();

		setValue(luxProductCarouselComponentWsDTO::setTitle, luxProductCarouselComponent.getTitle());
		setValue(luxProductCarouselComponentWsDTO::setBrandTitle, luxProductCarouselComponent.getBrandTitle());
		if (null != luxProductCarouselComponent.getBrandImage())
		{
			setValue(luxProductCarouselComponentWsDTO::setBrandImage, luxProductCarouselComponent.getBrandImage().getURL());
		}
		setValue(luxProductCarouselComponentWsDTO::setShopNowName, luxProductCarouselComponent.getShopNowName());
		setValue(luxProductCarouselComponentWsDTO::setShopNowLink, luxProductCarouselComponent.getShopNowLink());

		if (StringUtils.isNotEmpty(luxProductCarouselComponent.getAppPosition()))
		{
			luxProductCarouselComponentWsDTO.setComponentPosition(luxProductCarouselComponent.getAppPosition());
		}
		else
		{
			luxProductCarouselComponentWsDTO.setComponentPosition(" ");
		}

		if (luxProductCarouselComponent.getProducts() != null)
		{
			for (final ProductModel product : luxProductCarouselComponent.getProducts())
			{
				final LuxProductCarouselProductWsDTO luxProductCarouselProductWsDTO = new LuxProductCarouselProductWsDTO();
				ProductData productData = null;
				productData = productFacade.getProductForOptions(product,
						Arrays.asList(ProductOption.BASIC, ProductOption.GALLERY, ProductOption.SELLER));

				setValue(luxProductCarouselProductWsDTO::setPrdCode, productData.getCode());
				setValue(luxProductCarouselProductWsDTO::setPrdName, productData.getName());
				setValue(luxProductCarouselProductWsDTO::setPrdBrandName, productData.getBrand().getBrandname());
				setValue(luxProductCarouselProductWsDTO::setPrdURL, productData.getUrl());

				if (null != productData && null != productData.getImages())
				{
					for (final ImageData img : productData.getImages())
					{
						if (null != img && StringUtils.isNotEmpty(img.getFormat())
								&& img.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.SEARCHPAGE))
						{
							setValue(luxProductCarouselProductWsDTO::setPrdImage, img.getUrl());
						}
						else if (null != img && StringUtils.isNotEmpty(img.getFormat())
								&& img.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.LUXURYSEARCHPAGE))
						{
							setValue(luxProductCarouselProductWsDTO::setPrdImage, img.getUrl());
						}

					}
				}
				final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productData.getCode());
				if (buyboxdata != null)
				{
					final PriceData priceValue = buyboxdata.getPrice();
					final PriceData mrpPriceValue = buyboxdata.getMrpPriceValue();
					setValue(luxProductCarouselProductWsDTO::setPrdPrice, priceValue.getFormattedValue());
					setValue(luxProductCarouselProductWsDTO::setPrdMrpPrice, mrpPriceValue.getFormattedValue());
					if (null != mrpPriceValue && null != priceValue)
					{
						final double savingsAmt = mrpPriceValue.getDoubleValue().doubleValue()
								- priceValue.getDoubleValue().doubleValue();
						final double calculatedPerSavings = (savingsAmt / mrpPriceValue.getDoubleValue().doubleValue()) * 100;
						//final double roundedOffValuebefore = Math.round(calculatedPerSavings * 100.0) / 100.0;
						final double floorValue = Math.floor((calculatedPerSavings * 100.0) / 100.0);
						luxProductCarouselProductWsDTO.setPrdSavings(floorValue);

					}
				}


				luxProductCarouselProductWsDTOList.add(luxProductCarouselProductWsDTO);
			}
		}
		luxProductCarouselComponentWsDTO.setCarouselProducts(luxProductCarouselProductWsDTOList);
		if (null == luxuryComponent.getLuxuryProductCarouselComponent())
		{
			luxuryComponent.setLuxuryProductCarouselComponent(luxProductCarouselComponentWsDTOList);
		}
		luxuryComponent.getLuxuryProductCarouselComponent().add(luxProductCarouselComponentWsDTO);


		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getLuxCMSImagesComponentWsDTO(final CMSImageComponentModel CMSImageComponent,
			final LuxuryComponentsListWsDTO luxuryComponent)
	{
		final List<LuxuryMediaListWsDTO> luxuryCMSImageComponent = new ArrayList<LuxuryMediaListWsDTO>();
		final LuxuryMediaListWsDTO luxuryMediaListWsDTO = new LuxuryMediaListWsDTO();

		if (null != CMSImageComponent.getMedia() && StringUtils.isNotEmpty(CMSImageComponent.getMedia().getUrl()))
		{
			luxuryMediaListWsDTO.setURL(CMSImageComponent.getMedia().getURL());

		}
		if (null == luxuryComponent.getLuxuryCMSImageComponent())
		{
			luxuryComponent.setLuxuryCMSImageComponent(luxuryCMSImageComponent);
		}
		if (StringUtils.isNotEmpty(CMSImageComponent.getAppPosition()))
		{
			luxuryMediaListWsDTO.setComponentPosition(CMSImageComponent.getAppPosition());
		}
		else
		{
			luxuryMediaListWsDTO.setComponentPosition(" ");
		}
		luxuryComponent.getLuxuryCMSImageComponent().add(luxuryMediaListWsDTO);
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getLuxRotatingImagesComponentWsDTO(
			final RotatingImagesComponentModel RotatingImagesComponentModel, final LuxuryComponentsListWsDTO luxuryComponent)
	{
		final List<LuxRotatingImagesComponentWsDTO> luxRotatingImagesDTOs = new ArrayList<LuxRotatingImagesComponentWsDTO>();
		final LuxBannerComponentWsDTO LuxBannerComponentWsDTO = new LuxBannerComponentWsDTO();
		final List<LuxBannerComponentWsDTO> LuxBannerComponentWsDTOList = new ArrayList<LuxBannerComponentWsDTO>();
		final LuxRotatingImagesComponentWsDTO luxRotatingImagesDTO = new LuxRotatingImagesComponentWsDTO();


		if (StringUtils.isNotEmpty(RotatingImagesComponentModel.getAppPosition()))
		{
			luxRotatingImagesDTO.setComponentPosition(RotatingImagesComponentModel.getAppPosition());
		}
		else
		{
			luxRotatingImagesDTO.setComponentPosition(" ");
		}

		for (final BannerComponentModel bannerComp : RotatingImagesComponentModel.getBanners())
		{
			if (null != bannerComp)
			{
				setValue(LuxBannerComponentWsDTO::setUrlLink, bannerComp.getUrlLink());
				if (null != bannerComp.getMedia() && StringUtils.isNotEmpty(bannerComp.getMedia().getUrl()))
				{
					LuxBannerComponentWsDTO.setMedia(bannerComp.getMedia().getURL());

				}
				LuxBannerComponentWsDTOList.add(LuxBannerComponentWsDTO);
			}
		}

		luxRotatingImagesDTO.setBanners(LuxBannerComponentWsDTOList);
		luxRotatingImagesDTO.setTimeout(RotatingImagesComponentModel.getTimeout());

		if (StringUtils.isNotEmpty(RotatingImagesComponentModel.getEffect().getCode()))
		{
			luxRotatingImagesDTO.setEffect(RotatingImagesComponentModel.getEffect().getCode());
		}
		if (null != (RotatingImagesComponentModel.getTimeout()))
		{
			luxRotatingImagesDTO.setEffect(RotatingImagesComponentModel.getEffect().getCode());
		}


		if (null == luxuryComponent.getLuxuryRotatingImagesComponentWsDTO())
		{
			luxuryComponent.setLuxuryRotatingImagesComponentWsDTO(luxRotatingImagesDTOs);
		}
		luxuryComponent.getLuxuryRotatingImagesComponentWsDTO().add(luxRotatingImagesDTO);
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getLuxShowCaseComponentWsDTO(final LuxShowCaseComponentModel luxShowCaseComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
		final List<LuxShowCaseComponentListWsDTO> luxShowCaseComponentListObjs = new ArrayList<LuxShowCaseComponentListWsDTO>();
		final LuxuryComponentsListWsDTO luxuryComponent = luxuryComponentsListWsDTO;
		final List<LuxShowCaseCollectionComponentListWsDTO> luxShowCaseCollectionComponentList = new ArrayList<LuxShowCaseCollectionComponentListWsDTO>();
		final LuxShowCaseComponentListWsDTO luxShowCaseComponentListObj = new LuxShowCaseComponentListWsDTO();

		if (StringUtils.isNotEmpty(luxShowCaseComponent.getTitle()))
		{
			luxShowCaseComponentListObj.setTitle(luxShowCaseComponent.getTitle());
		}
		if (StringUtils.isNotEmpty(luxShowCaseComponent.getAppPosition()))
		{
			luxShowCaseComponentListObj.setComponentPosition(luxShowCaseComponent.getAppPosition());
		}
		else
		{
			luxShowCaseComponentListObj.setComponentPosition(" ");
		}
		for (final LuxShowCaseCollectionComponentModel showCaseComponent : luxShowCaseComponent.getShowCase())
		{
			final LuxShowCaseCollectionComponentListWsDTO LuxShowCaseComponent = new LuxShowCaseCollectionComponentListWsDTO();

			if (null != showCaseComponent.getBannerImagePosition()
					&& StringUtils.isNotEmpty(showCaseComponent.getBannerImagePosition().getCode()))
			{
				LuxShowCaseComponent.setBannerImagePosition(showCaseComponent.getBannerImagePosition().getCode());
			}

			LuxShowCaseComponent.setTitle(showCaseComponent.getTitle());
			LuxShowCaseComponent.setDescription(showCaseComponent.getDescription());
			LuxShowCaseComponent.setShopNowName(showCaseComponent.getShopNowName());
			LuxShowCaseComponent.setShopNowLink(showCaseComponent.getShopNowLink());

			LuxShowCaseComponent.setBannerImage(getBannerComponent(showCaseComponent.getBannerImage()));
			LuxShowCaseComponent.setProductImages(getBannerComponent(showCaseComponent.getProductImages()));

			luxShowCaseCollectionComponentList.add(LuxShowCaseComponent);

		}
		if (null == luxuryComponent.getLuxShowCaseCollectionComponent())
		{
			luxuryComponent.setLuxShowCaseCollectionComponent(luxShowCaseComponentListObjs);
		}
		luxShowCaseComponentListObj.setShowCase(luxShowCaseCollectionComponentList);
		luxuryComponent.getLuxShowCaseCollectionComponent().add(luxShowCaseComponentListObj);
		return luxuryComponent;
	}

	private List<LuxBannerComponentWsDTO> getBannerComponent(final Collection<BannerComponentModel> bannerCoponentModelCollection)
	{
		final List<LuxBannerComponentWsDTO> bannerList = new ArrayList<LuxBannerComponentWsDTO>();
		for (final BannerComponentModel banner : bannerCoponentModelCollection)
		{
			final LuxBannerComponentWsDTO luxBannerComponent = new LuxBannerComponentWsDTO();
			if (null != banner.getBannerView() && StringUtils.isNotEmpty(banner.getBannerView().getCode()))
			{
				luxBannerComponent.setBannerView(banner.getBannerView().getCode());
			}
			if (null != banner.getMedia() && StringUtils.isNotEmpty(banner.getMedia().getUrl()))
			{
				luxBannerComponent.setMedia(banner.getMedia().getUrl());
			}
			luxBannerComponent.setHeadline(banner.getHeadline());
			luxBannerComponent.setContent(banner.getContent());
			luxBannerComponent.setPageLabelOrId(banner.getPageLabelOrId());
			luxBannerComponent.setUrlLink(banner.getUrlLink());
			bannerList.add(luxBannerComponent);
		}
		return bannerList;
	}

	private LuxuryComponentsListWsDTO getLuxuryVideoComponenWsDTO(final LuxuryVideoComponentModel luxuryVideoComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
		final LuxuryComponentsListWsDTO luxuryComponent = luxuryComponentsListWsDTO;
		final LuxVideocomponentWsDTO luxVideocomponentWsDTO = new LuxVideocomponentWsDTO();
		final List<LuxVideocomponentWsDTO> luxVideocomponentList = new ArrayList<LuxVideocomponentWsDTO>();


		setValue(luxVideocomponentWsDTO::setVideoTitle, luxuryVideoComponent.getVideoTitle());
		setValue(luxVideocomponentWsDTO::setVideoUrl, luxuryVideoComponent.getVideoUrl());
		setValue(luxVideocomponentWsDTO::setVideoDescription, luxuryVideoComponent.getVideoDescription());
		setValue(luxVideocomponentWsDTO::setVideoSectionHeading, luxuryVideoComponent.getVideoSectionHeading());
		setValue(luxVideocomponentWsDTO::setPreviewUrl, luxuryVideoComponent.getPreviewImageUrl());

		if (StringUtils.isNotEmpty(luxuryVideoComponent.getAppPosition()))
		{
			luxVideocomponentWsDTO.setComponentPosition(luxuryVideoComponent.getAppPosition());
		}
		else
		{
			luxVideocomponentWsDTO.setComponentPosition(" ");
		}

		luxVideocomponentList.add(luxVideocomponentWsDTO);

		luxuryComponent.setLuxuryvideocomponent(luxVideocomponentList);

		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getWeeklySpecialWsDTO(final WeeklySpecialModel weeklySpecialComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{

		final List<WeeklySpecialBannerListWsDTO> weeklySpecialBanners = new ArrayList<WeeklySpecialBannerListWsDTO>();
		final LuxuryComponentsListWsDTO luxuryComponent = luxuryComponentsListWsDTO;
		final List<WeeklySpecialBannerWsDTO> weeklySpecialBannerList = new ArrayList<WeeklySpecialBannerWsDTO>();
		final WeeklySpecialBannerListWsDTO weeklySpecialBannerListObj = new WeeklySpecialBannerListWsDTO();

		if (StringUtils.isNotEmpty(weeklySpecialComponent.getTitle()))
		{
			weeklySpecialBannerListObj.setSeeklySpecialTitle(weeklySpecialComponent.getTitle());
		}
		if (StringUtils.isNotEmpty(weeklySpecialComponent.getAppPosition()))
		{
			weeklySpecialBannerListObj.setComponentPosition(weeklySpecialComponent.getAppPosition());
		}
		else
		{
			weeklySpecialBannerListObj.setComponentPosition(" ");
		}

		for (final WeeklySpecialBannerModel banner : weeklySpecialComponent.getWeeklySpecialBanners())
		{
			final WeeklySpecialBannerWsDTO weeklySpecialBanner = new WeeklySpecialBannerWsDTO();
			if (null != banner.getImage() && StringUtils.isNotEmpty(banner.getImage().getURL()))
			{
				weeklySpecialBanner.setImageUrl(banner.getImage().getURL());
			}
			if (null != banner.getSvglogo() && StringUtils.isNotEmpty(banner.getSvglogo().getURL()))
			{
				weeklySpecialBanner.setSvglogoUrl(banner.getSvglogo().getURL());
			}
			weeklySpecialBanner.setUrl(banner.getUrl());
			weeklySpecialBannerList.add(weeklySpecialBanner);
		}

		if (null == luxuryComponent.getLuxuryWeeklySpecialBanner())
		{
			luxuryComponent.setLuxuryWeeklySpecialBanner(weeklySpecialBanners);
		}

		weeklySpecialBannerListObj.setWeeklySpecialBannerList(weeklySpecialBannerList);
		luxuryComponent.getLuxuryWeeklySpecialBanner().add(weeklySpecialBannerListObj);
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getShopOnLuxuryWsDTO(final ShopOnLuxuryModel luxuryShopOnLuxuryComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
		final List<ShopOnLuxuryElementListWsDTO> shopOnLuxuryElementListObjs = new ArrayList<ShopOnLuxuryElementListWsDTO>();
		final LuxuryComponentsListWsDTO luxuryComponent = luxuryComponentsListWsDTO;
		final List<ShopOnLuxuryElementWsDTO> shopOnLuxuryElementList = new ArrayList<ShopOnLuxuryElementWsDTO>();
		final ShopOnLuxuryElementListWsDTO shopOnLuxuryElementListObj = new ShopOnLuxuryElementListWsDTO();

		setValue(shopOnLuxuryElementListObj::setShopOnLuxuryTitle, luxuryShopOnLuxuryComponent.getShopOnLuxuryTitle());
		if (StringUtils.isNotEmpty(luxuryShopOnLuxuryComponent.getAppPosition()))
		{
			shopOnLuxuryElementListObj.setComponentPosition(luxuryShopOnLuxuryComponent.getAppPosition());
		}
		else
		{
			shopOnLuxuryElementListObj.setComponentPosition(" ");
		}

		for (final ShopOnLuxuryElementModel element : luxuryShopOnLuxuryComponent.getShopOnLuxuryElements())
		{
			final ShopOnLuxuryElementWsDTO shopOnLuxuryelement = new ShopOnLuxuryElementWsDTO();

			if (null != element.getImage() && StringUtils.isNotEmpty(element.getImage().getURL()))
			{
				shopOnLuxuryelement.setImagePath(element.getImage().getURL());
			}
			shopOnLuxuryelement.setTitle(element.getTitle());
			shopOnLuxuryelement.setDescription(element.getDescription());
			shopOnLuxuryElementList.add(shopOnLuxuryelement);

		}
		if (null == luxuryComponent.getLuxuryShopOnLuxury())
		{
			luxuryComponent.setLuxuryShopOnLuxury(shopOnLuxuryElementListObjs);
		}
		shopOnLuxuryElementListObj.setShopOnLuxuryElements(shopOnLuxuryElementList);
		luxuryComponent.getLuxuryShopOnLuxury().add(shopOnLuxuryElementListObj);
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getLuxShopByCategoryWsDTO(final ShopByCategoryModel luxShopByCategoryComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
		final List<LuxShopByCategoryWsDTO> luxShopByCategoryWsDTOs = new ArrayList<LuxShopByCategoryWsDTO>();
		final LuxuryComponentsListWsDTO luxuryComponent = luxuryComponentsListWsDTO;
		final LuxShopByCategoryWsDTO luxShopByCategoryWsDTO = new LuxShopByCategoryWsDTO();
		final List<LuxBannerComponentWsDTO> relatedImageList = new ArrayList<LuxBannerComponentWsDTO>();

		setValue(luxShopByCategoryWsDTO::setTitle, luxShopByCategoryComponent.getTitle());
		setValue(luxShopByCategoryWsDTO::setShopNowLink, luxShopByCategoryComponent.getShopNowLink());
		setValue(luxShopByCategoryWsDTO::setShopNowName, luxShopByCategoryComponent.getShopNowName());
		setValue(luxShopByCategoryWsDTO::setComponentPosition, luxShopByCategoryComponent.getAppPosition());

		if (StringUtils.isNotEmpty(luxShopByCategoryComponent.getAppPosition()))
		{
			luxShopByCategoryWsDTO.setComponentPosition(luxShopByCategoryComponent.getAppPosition());
		}
		else
		{
			luxShopByCategoryWsDTO.setComponentPosition(" ");
		}

		for (final BannerComponentModel bannerComponent : luxShopByCategoryComponent.getRelatedImage())
		{
			final LuxBannerComponentWsDTO luxBannerComponentelement = new LuxBannerComponentWsDTO();
			luxBannerComponentelement.setContent(bannerComponent.getContent());
			luxBannerComponentelement.setHeadline(bannerComponent.getHeadline());
			luxBannerComponentelement.setPageLabelOrId(bannerComponent.getPageLabelOrId());
			luxBannerComponentelement.setBannerView(bannerComponent.getBannerView().getCode());
			luxBannerComponentelement.setUrlLink(bannerComponent.getUrlLink());
			luxBannerComponentelement.setMedia(bannerComponent.getMedia().getURL());
			relatedImageList.add(luxBannerComponentelement);
		}
		if (null == luxuryComponent.getLuxShopByCategory())
		{
			luxuryComponent.setLuxShopByCategory(luxShopByCategoryWsDTOs);
		}
		luxShopByCategoryWsDTO.setRelatedImage(relatedImageList);
		luxuryComponent.getLuxShopByCategory().add(luxShopByCategoryWsDTO);
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getLuxCMSMediaParagraphWsDTO(
			final LuxCMSMediaParagraphComponentModel LuxCMSMediaParagraphComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
		final List<LuxCMSMediaParagraphComponentListWsDTO> LuxCMSMediaParagraphComponentListObjs = new ArrayList<LuxCMSMediaParagraphComponentListWsDTO>();
		final LuxuryComponentsListWsDTO luxuryComponent = luxuryComponentsListWsDTO;
		final List<LuxBannerComponentWsDTO> LuxBannerComponentList = new ArrayList<LuxBannerComponentWsDTO>();
		final LuxCMSMediaParagraphComponentListWsDTO LuxCMSMediaParagraphComponentListObj = new LuxCMSMediaParagraphComponentListWsDTO();

		setValue(LuxCMSMediaParagraphComponentListObj::setUrl, LuxCMSMediaParagraphComponent.getUrl());

		if (null != LuxCMSMediaParagraphComponent.getMedia()
				&& StringUtils.isNotEmpty(LuxCMSMediaParagraphComponent.getMedia().getURL()))
		{
			LuxCMSMediaParagraphComponentListObj.setMedia(LuxCMSMediaParagraphComponent.getMedia().getURL());
		}
		if (StringUtils.isNotEmpty(LuxCMSMediaParagraphComponent.getAppPosition()))
		{
			LuxCMSMediaParagraphComponentListObj.setComponentPosition(LuxCMSMediaParagraphComponent.getAppPosition());
		}
		else
		{
			LuxCMSMediaParagraphComponentListObj.setComponentPosition(" ");
		}

		setValue(LuxCMSMediaParagraphComponentListObj::setContent, LuxCMSMediaParagraphComponent.getContent());

		for (final BannerComponentModel element : LuxCMSMediaParagraphComponent.getMedias())
		{
			final LuxBannerComponentWsDTO elements = new LuxBannerComponentWsDTO();

			if (null != element.getMedia() && StringUtils.isNotEmpty(element.getMedia().getUrl()))
			{
				elements.setMedia(element.getMedia().getURL());
			}

			elements.setHeadline(element.getHeadline());
			elements.setContent(element.getContent());
			elements.setBannerView(element.getBannerView().getCode());
			elements.setPageLabelOrId(element.getPageLabelOrId());
			elements.setUrlLink(element.getUrlLink());

			LuxBannerComponentList.add(elements);

		}
		if (null == luxuryComponent.getLuxCMSMediaParagraphComponent())
		{
			luxuryComponent.setLuxCMSMediaParagraphComponent(LuxCMSMediaParagraphComponentListObjs);
		}

		LuxCMSMediaParagraphComponentListObj.setMedias(LuxBannerComponentList);
		luxuryComponent.getLuxCMSMediaParagraphComponent().add(LuxCMSMediaParagraphComponentListObj);
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getLuxuryLookBookComponentWsDTO(final LuxuryLookBookComponentModel luxuryLookBookComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
		final List<LuxuryLookBookComponentListWsDTO> luxuryLookBookComponentListObjs = new ArrayList<LuxuryLookBookComponentListWsDTO>();
		final LuxuryComponentsListWsDTO luxuryComponent = luxuryComponentsListWsDTO;
		final LuxuryLookBookComponentListWsDTO luxuryLookBookComponentListObj = new LuxuryLookBookComponentListWsDTO();

		setValue(luxuryLookBookComponentListObj::setTitle, luxuryLookBookComponent.getTitle());
		setValue(luxuryLookBookComponentListObj::setLayoutCode, luxuryLookBookComponent.getLayoutCode());
		setValue(luxuryLookBookComponentListObj::setDescription, luxuryLookBookComponent.getDescription());
		setValue(luxuryLookBookComponentListObj::setShopNowName, luxuryLookBookComponent.getShopNowName());
		setValue(luxuryLookBookComponentListObj::setShopNowLink, luxuryLookBookComponent.getShopNowLink());

		if (null != luxuryLookBookComponent.getTextArea())
		{
			luxuryLookBookComponentListObj.setTextArea(luxuryLookBookComponent.getTextArea().getContent());
		}
		if (StringUtils.isNotEmpty(luxuryLookBookComponent.getAppPosition()))
		{
			luxuryLookBookComponentListObj.setComponentPosition(luxuryLookBookComponent.getAppPosition());
		}
		else
		{
			luxuryLookBookComponentListObj.setComponentPosition(" ");
		}

		if (null == luxuryComponent.getLuxuryLookBookComponent())
		{
			luxuryComponent.setLuxuryLookBookComponent(luxuryLookBookComponentListObjs);
		}
		luxuryLookBookComponentListObj.setFirstCol(getMediaContainerList(luxuryLookBookComponent.getFirstCol()));
		luxuryLookBookComponentListObj.setSecondCol(getMediaContainerList(luxuryLookBookComponent.getSecondCol()));
		luxuryComponent.getLuxuryLookBookComponent().add(luxuryLookBookComponentListObj);

		return luxuryComponent;
	}

	private List<LuxMediaContainerWsDTO> getMediaContainerList(final Collection<MediaContainerModel> mediaContainerModelCollection)
	{
		final List<LuxMediaContainerWsDTO> LuxMediaContainerList = new ArrayList<LuxMediaContainerWsDTO>();
		for (final MediaContainerModel mediaContainer : mediaContainerModelCollection)
		{
			final LuxMediaContainerWsDTO luxMediaContainer = new LuxMediaContainerWsDTO();
			final List<LuxuryMediaListWsDTO> medias = new ArrayList<LuxuryMediaListWsDTO>();

			if (null != mediaContainer.getMedias())
			{
				for (final MediaModel media : mediaContainer.getMedias())
				{

					final LuxuryMediaListWsDTO luxuryMediaList = new LuxuryMediaListWsDTO();
					setValue(luxuryMediaList::setURL, media.getURL());

					final LuxuryMediaModel luxuryMedia = (LuxuryMediaModel) media;
					setValue(luxuryMediaList::setUrlLink, luxuryMedia.getUrlLink());

					if (null != luxuryMedia.getMediaFormat() && StringUtils.isNotEmpty(luxuryMedia.getMediaFormat().getQualifier()))
					{
						luxuryMediaList.setMediaFormat(luxuryMedia.getMediaFormat().getQualifier());

					}
					medias.add(luxuryMediaList);

				}
			}

			luxMediaContainer.setQualifier(mediaContainer.getQualifier());
			luxMediaContainer.setName(mediaContainer.getName());
			luxMediaContainer.setMedias(medias);

			LuxMediaContainerList.add(luxMediaContainer);

		}
		return LuxMediaContainerList;
	}

	private <T> void setValue(final Consumer<T> setterMethod, final T value)
	{
		if (null != value && StringUtils.isNotEmpty((String) value))
		{
			setterMethod.accept(value);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.cms.LuxCmsFacade#getBrandById(java.lang.String)
	 */
	@Override
	public LuxuryComponentsListWsDTO getBrandById(final String brandCode)
	{
		final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public LuxuryComponentsListWsDTO execute()
			{
				sessionService.setAttribute("detectedUI", UiExperienceLevel.MOBILE);
				sessionService.setAttribute("UiExperienceService-Detected-Level", UiExperienceLevel.MOBILE);
				sessionService.setAttribute("UiExperienceService-Override-Level", UiExperienceLevel.MOBILE);


				final CategoryModel category = categoryService.getCategoryForCode(brandCode);
				try
				{
					final ContentPageModel contentPage = mplCMSPageService.getLandingPageForCategory(category);
					return getLuxuryPage(contentPage);
				}
				catch (final CMSItemNotFoundException e)
				{
					LOG.error("CMSItemNotFoundException : ", e);
				}
				return null;

			}
		});
		return luxuryComponentsListWsDTO;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.cms.LuxCmsFacade#getContentPagesBylable(java.lang.String)
	 */
	@Override
	public LuxuryComponentsListWsDTO getContentPagesBylableOrId(final String label)
	{
		final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public LuxuryComponentsListWsDTO execute()
			{
				sessionService.setAttribute("detectedUI", UiExperienceLevel.MOBILE);
				sessionService.setAttribute("UiExperienceService-Detected-Level", UiExperienceLevel.MOBILE);
				sessionService.setAttribute("UiExperienceService-Override-Level", UiExperienceLevel.MOBILE);

				try
				{
					final ContentPageModel contentPage = mplCMSPageService.getPageByLabelOrId(label);
					return getLuxuryPage(contentPage);
				}
				catch (final CMSItemNotFoundException e)
				{
					LOG.error("CMSItemNotFoundException : ", e);
				}
				return null;
			}
		});
		return luxuryComponentsListWsDTO;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.cms.LuxCmsFacade#getLuxHomepage()
	 */
	@Override
	public LuxuryComponentsListWsDTO getLuxHomepage()
	{
		final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public LuxuryComponentsListWsDTO execute()
			{
				sessionService.setAttribute("detectedUI", UiExperienceLevel.MOBILE);
				sessionService.setAttribute("UiExperienceService-Detected-Level", UiExperienceLevel.MOBILE);
				sessionService.setAttribute("UiExperienceService-Override-Level", UiExperienceLevel.MOBILE);

				try
				{
					final ContentPageModel contentPage = mplCMSPageService.getHomepage();
					return getLuxuryPage(contentPage);
				}
				catch (final CMSItemNotFoundException e)
				{
					LOG.error("CMSItemNotFoundException : ", e);
				}
				return null;
			}
		});
		return luxuryComponentsListWsDTO;
	}
}