/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSImageComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.lux.model.LuxuryMediaModel;
import com.tisl.lux.model.LuxuryVideoComponentModel;
import com.tisl.lux.model.cms.components.LuxShowCaseCollectionComponentModel;
import com.tisl.lux.model.cms.components.LuxShowCaseComponentModel;
import com.tisl.lux.model.cms.components.LuxuryLookBookComponentModel;
import com.tisl.lux.model.cms.components.ShopOnLuxuryElementModel;
import com.tisl.lux.model.cms.components.ShopOnLuxuryModel;
import com.tisl.lux.model.cms.components.WeeklySpecialBannerModel;
import com.tisl.lux.model.cms.components.WeeklySpecialModel;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.model.cms.components.LuxCMSMediaParagraphComponentModel;
import com.tisl.mpl.model.cms.components.ShopByCategoryModel;
import com.tisl.mpl.wsdto.LuxBannerComponentWsDTO;
import com.tisl.mpl.wsdto.LuxCMSMediaParagraphComponentListWsDTO;
import com.tisl.mpl.wsdto.LuxMediaContainerWsDTO;
import com.tisl.mpl.wsdto.LuxRotatingImagesComponentWsDTO;
import com.tisl.mpl.wsdto.LuxShopByCategoryWsDTO;
import com.tisl.mpl.wsdto.LuxShowCaseCollectionComponentListWsDTO;
import com.tisl.mpl.wsdto.LuxShowCaseComponentListWsDTO;
import com.tisl.mpl.wsdto.LuxVideocomponentWsDTO;
import com.tisl.mpl.wsdto.LuxuryComponentsListWsDTO;
import com.tisl.mpl.wsdto.LuxuryLookBookComponentListWsDTO;
import com.tisl.mpl.wsdto.LuxuryMediaListWsDTO;
import com.tisl.mpl.wsdto.ShopOnLuxuryElementListWsDTO;
import com.tisl.mpl.wsdto.ShopOnLuxuryElementWsDTO;
import com.tisl.mpl.wsdto.WeeklySpecialBannerListWsDTO;
import com.tisl.mpl.wsdto.WeeklySpecialBannerWsDTO;



public class LuxCmsFacadeImpl implements LuxCmsFacade
{


	private MplCMSPageServiceImpl mplCMSPageService;



	private static final Logger LOG = Logger.getLogger(MplCmsFacadeImpl.class);

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

		LuxuryComponentsListWsDTO luxuryComponentsList = luxuryComponentsListWsDTO;

		if (null != contentSlot)
		{
			//			final int count = 0;
			for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlot.getCmsComponents())
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
					default:
						break;
				}
			}

		}
		//}
		return luxuryComponentsList;
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


		for (final BannerComponentModel bannerComp : RotatingImagesComponentModel.getBanners())
		{
			if (null != bannerComp)
			{

				if (StringUtils.isNotEmpty(bannerComp.getUrlLink()))
				{
					LuxBannerComponentWsDTO.setUrlLink(bannerComp.getUrlLink());
				}
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
		for (final LuxShowCaseCollectionComponentModel ShowCaseComponent : luxShowCaseComponent.getShowCase())
		{
			final LuxShowCaseCollectionComponentListWsDTO LuxShowCaseComponent = new LuxShowCaseCollectionComponentListWsDTO();
			String title = null;
			String description = null;
			String shopNowName = null;
			String shopNowLink = null;
			String BannerImagePosition = null;
			final List<LuxBannerComponentWsDTO> bannerImage = new ArrayList<LuxBannerComponentWsDTO>();
			final List<LuxBannerComponentWsDTO> productImages = new ArrayList<LuxBannerComponentWsDTO>();




			if (StringUtils.isNotEmpty(ShowCaseComponent.getTitle()))
			{
				title = ShowCaseComponent.getTitle();

			}
			if (StringUtils.isNotEmpty(ShowCaseComponent.getDescription()))
			{
				description = ShowCaseComponent.getDescription();

			}
			if (StringUtils.isNotEmpty(ShowCaseComponent.getShopNowName()))
			{
				shopNowName = ShowCaseComponent.getShopNowName();

			}
			if (StringUtils.isNotEmpty(ShowCaseComponent.getShopNowLink()))
			{
				shopNowLink = ShowCaseComponent.getShopNowLink();

			}
			if (null != ShowCaseComponent.getBannerImagePosition()
					&& StringUtils.isNotEmpty(ShowCaseComponent.getBannerImagePosition().getCode()))
			{
				BannerImagePosition = ShowCaseComponent.getBannerImagePosition().getCode();

			}
			for (final BannerComponentModel banner : ShowCaseComponent.getBannerImage())
			{
				final LuxBannerComponentWsDTO luxBannerComponent = new LuxBannerComponentWsDTO();
				String headline = null;
				String content = null;
				String pageLabelOrId = null;
				String bannerView = null;
				String media = null;
				String urlLink = null;

				if (StringUtils.isNotEmpty(banner.getHeadline()))
				{
					headline = banner.getHeadline();

				}
				if (StringUtils.isNotEmpty(banner.getContent()))
				{
					content = banner.getContent();

				}
				if (StringUtils.isNotEmpty(banner.getPageLabelOrId()))
				{
					pageLabelOrId = banner.getPageLabelOrId();

				}
				if (null != banner.getBannerView() && StringUtils.isNotEmpty(banner.getBannerView().getCode()))
				{
					bannerView = banner.getBannerView().getCode();

				}
				if (null != banner.getMedia() && StringUtils.isNotEmpty(banner.getMedia().getUrl()))
				{
					media = banner.getMedia().getUrl();

				}
				if (StringUtils.isNotEmpty(banner.getUrlLink()))
				{
					urlLink = banner.getUrlLink();

				}

				luxBannerComponent.setHeadline(headline);
				luxBannerComponent.setContent(content);
				luxBannerComponent.setPageLabelOrId(pageLabelOrId);
				luxBannerComponent.setBannerView(bannerView);
				luxBannerComponent.setMedia(media);
				luxBannerComponent.setUrlLink(urlLink);
				bannerImage.add(luxBannerComponent);
			}


			for (final BannerComponentModel banner1 : ShowCaseComponent.getProductImages())
			{
				final LuxBannerComponentWsDTO luxBannerComponent1 = new LuxBannerComponentWsDTO();
				String headline = null;
				String content = null;
				String pageLabelOrId = null;
				String bannerView = null;
				String media = null;
				String urlLink = null;

				if (StringUtils.isNotEmpty(banner1.getHeadline()))
				{
					headline = banner1.getHeadline();

				}
				if (StringUtils.isNotEmpty(banner1.getContent()))
				{
					content = banner1.getContent();

				}
				if (StringUtils.isNotEmpty(banner1.getPageLabelOrId()))
				{
					pageLabelOrId = banner1.getPageLabelOrId();

				}
				if (null != banner1.getBannerView() && StringUtils.isNotEmpty(banner1.getBannerView().getCode()))
				{
					bannerView = banner1.getBannerView().getCode();

				}
				if (null != banner1.getMedia() && StringUtils.isNotEmpty(banner1.getMedia().getUrl()))
				{
					media = banner1.getMedia().getUrl();

				}
				if (StringUtils.isNotEmpty(banner1.getUrlLink()))
				{
					urlLink = banner1.getUrlLink();

				}

				luxBannerComponent1.setHeadline(headline);
				luxBannerComponent1.setContent(content);
				luxBannerComponent1.setPageLabelOrId(pageLabelOrId);
				luxBannerComponent1.setBannerView(bannerView);
				luxBannerComponent1.setMedia(media);
				luxBannerComponent1.setUrlLink(urlLink);
				productImages.add(luxBannerComponent1);
			}


			LuxShowCaseComponent.setTitle(title);
			LuxShowCaseComponent.setDescription(description);
			LuxShowCaseComponent.setShopNowName(shopNowName);
			LuxShowCaseComponent.setShopNowLink(shopNowLink);
			LuxShowCaseComponent.setBannerImagePosition(BannerImagePosition);
			LuxShowCaseComponent.setBannerImage(bannerImage);
			LuxShowCaseComponent.setProductImages(productImages);

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

	private LuxuryComponentsListWsDTO getLuxuryVideoComponenWsDTO(final LuxuryVideoComponentModel luxuryVideoComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
		final LuxuryComponentsListWsDTO luxuryComponent = luxuryComponentsListWsDTO;
		final LuxVideocomponentWsDTO luxVideocomponentWsDTO = new LuxVideocomponentWsDTO();
		final List<LuxVideocomponentWsDTO> luxVideocomponentList = new ArrayList<LuxVideocomponentWsDTO>();


		if (StringUtils.isNotEmpty(luxuryVideoComponent.getVideoTitle()))
		{
			luxVideocomponentWsDTO.setVideoTitle(luxuryVideoComponent.getVideoTitle());
		}
		if (StringUtils.isNotEmpty(luxuryVideoComponent.getVideoUrl()))
		{
			luxVideocomponentWsDTO.setVideoUrl(luxuryVideoComponent.getVideoUrl());
		}
		if (StringUtils.isNotEmpty(luxuryVideoComponent.getVideoDescription()))
		{
			luxVideocomponentWsDTO.setVideoDescription(luxuryVideoComponent.getVideoDescription());
		}
		if (StringUtils.isNotEmpty(luxuryVideoComponent.getVideoSectionHeading()))
		{
			luxVideocomponentWsDTO.setVideoSectionHeading(luxuryVideoComponent.getVideoSectionHeading());
		}
		if (StringUtils.isNotEmpty(luxuryVideoComponent.getPreviewImageUrl()))
		{
			luxVideocomponentWsDTO.setPreviewUrl(luxuryVideoComponent.getPreviewImageUrl());
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
		for (final WeeklySpecialBannerModel banner : weeklySpecialComponent.getWeeklySpecialBanners())
		{
			final WeeklySpecialBannerWsDTO weeklySpecialBanner = new WeeklySpecialBannerWsDTO();
			String svglogoUrl = null;
			String imageUrl = null;
			String url = null;

			if (null != banner.getImage() && StringUtils.isNotEmpty(banner.getImage().getURL()))
			{
				imageUrl = banner.getImage().getURL();

			}
			if (null != banner.getSvglogo() && StringUtils.isNotEmpty(banner.getSvglogo().getURL()))
			{
				svglogoUrl = banner.getSvglogo().getURL();

			}
			if (StringUtils.isNotEmpty(banner.getUrl()))
			{
				url = banner.getUrl();
			}

			weeklySpecialBanner.setImageUrl(imageUrl);
			weeklySpecialBanner.setSvglogoUrl(svglogoUrl);
			weeklySpecialBanner.setUrl(url);
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

		if (StringUtils.isNotEmpty(luxuryShopOnLuxuryComponent.getShopOnLuxuryTitle()))
		{
			shopOnLuxuryElementListObj.setShopOnLuxuryTitle(luxuryShopOnLuxuryComponent.getShopOnLuxuryTitle());
		}
		for (final ShopOnLuxuryElementModel element : luxuryShopOnLuxuryComponent.getShopOnLuxuryElements())
		{
			final ShopOnLuxuryElementWsDTO shopOnLuxuryelement = new ShopOnLuxuryElementWsDTO();
			String elementTitle = null;
			String elementImagePath = null;
			String elementDescription = null;

			if (StringUtils.isNotEmpty(element.getTitle()))
			{
				elementTitle = element.getTitle();
			}

			if (null != element.getImage() && StringUtils.isNotEmpty(element.getImage().getURL()))
			{
				elementImagePath = element.getImage().getURL();

			}
			if (StringUtils.isNotEmpty(element.getDescription()))
			{
				elementDescription = element.getDescription();
			}

			shopOnLuxuryelement.setTitle(elementTitle);
			shopOnLuxuryelement.setImagePath(elementImagePath);
			shopOnLuxuryelement.setDescription(elementDescription);

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

		if (StringUtils.isNotEmpty(luxShopByCategoryComponent.getTitle()))
		{
			luxShopByCategoryWsDTO.setTitle(luxShopByCategoryComponent.getTitle());
		}

		for (final BannerComponentModel bannerComponent : luxShopByCategoryComponent.getRelatedImage())
		{
			final LuxBannerComponentWsDTO luxBannerComponentelement = new LuxBannerComponentWsDTO();
			String bannerComponentContent = null;
			String bannerComponentHeadLine = null;
			String bannerComponentPageLabelOrId = null;

			String bannerComponentbannerView = null;
			String bannerComponenturlLink = null;
			String bannerComponentmedia = null;
			//	final String bannerComponentexternal = null;

			if (StringUtils.isNotEmpty(bannerComponent.getContent()))
			{
				bannerComponentContent = bannerComponent.getContent();
			}

			if (StringUtils.isNotEmpty(bannerComponent.getHeadline()))
			{
				bannerComponentHeadLine = bannerComponent.getHeadline();
			}
			if (StringUtils.isNotEmpty(bannerComponent.getPageLabelOrId()))
			{
				bannerComponentPageLabelOrId = bannerComponent.getPageLabelOrId();
			}
			if (StringUtils.isNotBlank(bannerComponent.getBannerView().getCode()))
			{
				bannerComponentbannerView = bannerComponent.getBannerView().getCode();
			}
			if (StringUtils.isNotBlank(bannerComponent.getUrlLink()))
			{
				bannerComponenturlLink = bannerComponent.getUrlLink();
			}
			if (StringUtils.isNotBlank(bannerComponent.getMedia().getURL()))
			{
				bannerComponentmedia = bannerComponent.getMedia().getURL();
			}
			luxBannerComponentelement.setContent(bannerComponentContent);
			luxBannerComponentelement.setHeadline(bannerComponentHeadLine);
			luxBannerComponentelement.setPageLabelOrId(bannerComponentPageLabelOrId);
			luxBannerComponentelement.setBannerView(bannerComponentbannerView);
			luxBannerComponentelement.setUrlLink(bannerComponenturlLink);
			luxBannerComponentelement.setUrlLink(bannerComponentmedia);
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


		if (StringUtils.isNotEmpty(LuxCMSMediaParagraphComponent.getUrl()))
		{
			LuxCMSMediaParagraphComponentListObj.setUrl(LuxCMSMediaParagraphComponent.getUrl());
		}

		if (null != LuxCMSMediaParagraphComponent.getMedia()
				&& StringUtils.isNotEmpty(LuxCMSMediaParagraphComponent.getMedia().getURL()))
		{

			LuxCMSMediaParagraphComponentListObj.setMedia(LuxCMSMediaParagraphComponent.getMedia().getURL());

		}

		if (StringUtils.isNotEmpty(LuxCMSMediaParagraphComponent.getContent()))
		{
			LuxCMSMediaParagraphComponentListObj.setContent(LuxCMSMediaParagraphComponent.getContent());
		}
		for (final BannerComponentModel element : LuxCMSMediaParagraphComponent.getMedias())
		{
			final LuxBannerComponentWsDTO elements = new LuxBannerComponentWsDTO();


			String headline = null;
			String content = null;
			String pageLabelOrId = null;
			String bannerView = null;
			String media = null;
			String urlLink = null;




			if (StringUtils.isNotEmpty(element.getHeadline()))
			{
				headline = element.getHeadline();
			}



			if (StringUtils.isNotEmpty(element.getContent()))
			{
				content = element.getContent();
			}

			if (StringUtils.isNotEmpty(element.getPageLabelOrId()))
			{
				pageLabelOrId = element.getPageLabelOrId();
			}

			if (StringUtils.isNotEmpty(element.getBannerView().getCode()))
			{
				bannerView = element.getBannerView().getCode();
			}

			if (null != element.getMedia() && StringUtils.isNotEmpty(element.getMedia().getUrl()))
			{
				media = element.getMedia().getURL();
			}


			if (StringUtils.isNotEmpty(element.getUrlLink()))
			{
				urlLink = element.getUrlLink();
			}

			elements.setHeadline(headline);
			elements.setContent(content);
			elements.setBannerView(bannerView);
			elements.setPageLabelOrId(pageLabelOrId);
			elements.setMedia(media);
			elements.setUrlLink(urlLink);



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
		final List<LuxMediaContainerWsDTO> LuxMediaContainerList = new ArrayList<LuxMediaContainerWsDTO>();
		final List<LuxMediaContainerWsDTO> LuxMediaContainerSecondColList = new ArrayList<LuxMediaContainerWsDTO>();
		final LuxuryLookBookComponentListWsDTO luxuryLookBookComponentListObj = new LuxuryLookBookComponentListWsDTO();

		if (StringUtils.isNotEmpty(luxuryLookBookComponent.getTitle()))
		{
			luxuryLookBookComponentListObj.setTitle(luxuryLookBookComponent.getTitle());
		}

		if (StringUtils.isNotEmpty(luxuryLookBookComponent.getLayoutCode()))
		{
			luxuryLookBookComponentListObj.setLayoutCode(luxuryLookBookComponent.getLayoutCode());
		}

		if (StringUtils.isNotEmpty(luxuryLookBookComponent.getDescription()))
		{
			luxuryLookBookComponentListObj.setDescription(luxuryLookBookComponent.getDescription());
		}

		if (StringUtils.isNotEmpty(luxuryLookBookComponent.getShopNowName()))
		{
			luxuryLookBookComponentListObj.setShopNowName(luxuryLookBookComponent.getShopNowName());
		}

		if (StringUtils.isNotEmpty(luxuryLookBookComponent.getShopNowLink()))
		{
			luxuryLookBookComponentListObj.setShopNowLink(luxuryLookBookComponent.getShopNowLink());
		}


		if (null != luxuryLookBookComponent.getTextArea()
				&& StringUtils.isNotEmpty(luxuryLookBookComponent.getTextArea().getContent()))
		{
			luxuryLookBookComponentListObj.setTextArea(luxuryLookBookComponent.getTextArea().getContent());

		}
		for (final MediaContainerModel mediaContainer : luxuryLookBookComponent.getFirstCol())
		{
			final LuxMediaContainerWsDTO luxMediaContainer = new LuxMediaContainerWsDTO();
			String qualifier = null;
			String name = null;
			final List<LuxuryMediaListWsDTO> medias = new ArrayList<LuxuryMediaListWsDTO>();

			if (StringUtils.isNotEmpty(mediaContainer.getQualifier()))
			{
				qualifier = mediaContainer.getQualifier();

			}
			if (StringUtils.isNotEmpty(mediaContainer.getName()))
			{
				name = mediaContainer.getName();

			}
			if (null != mediaContainer.getMedias())
			{
				for (final MediaModel media : mediaContainer.getMedias())
				{

					final LuxuryMediaListWsDTO luxuryMediaList = new LuxuryMediaListWsDTO();
					if (null != media.getURL() && StringUtils.isNotEmpty(media.getURL()))
					{
						luxuryMediaList.setURL(media.getURL());

					}
					final LuxuryMediaModel luxuryMedia = (LuxuryMediaModel) media;
					if (null != luxuryMedia.getUrlLink() && StringUtils.isNotEmpty(luxuryMedia.getUrlLink()))
					{
						luxuryMediaList.setUrlLink(luxuryMedia.getUrlLink());

					}
					if (null != luxuryMedia.getMediaFormat() && StringUtils.isNotEmpty(luxuryMedia.getMediaFormat().getQualifier()))
					{
						luxuryMediaList.setMdeiaFormat(luxuryMedia.getMediaFormat().getQualifier());

					}
					medias.add(luxuryMediaList);

				}
			}

			luxMediaContainer.setQualifier(qualifier);
			luxMediaContainer.setName(name);
			luxMediaContainer.setMedias(medias);

			LuxMediaContainerList.add(luxMediaContainer);


		}


		for (final MediaContainerModel mediaContainer1 : luxuryLookBookComponent.getSecondCol())
		{
			final LuxMediaContainerWsDTO luxMediaContainer1 = new LuxMediaContainerWsDTO();
			String qualifier = null;
			String name = null;
			final List<LuxuryMediaListWsDTO> medias = new ArrayList<LuxuryMediaListWsDTO>();

			if (StringUtils.isNotEmpty(mediaContainer1.getQualifier()))
			{
				qualifier = mediaContainer1.getQualifier();

			}
			if (StringUtils.isNotEmpty(mediaContainer1.getName()))
			{
				name = mediaContainer1.getName();

			}
			if (null != mediaContainer1.getMedias())
			{
				for (final MediaModel media : mediaContainer1.getMedias())
				{
					final LuxuryMediaModel luxuryMedia = (LuxuryMediaModel) media;
					final LuxuryMediaListWsDTO luxuryMediaList = new LuxuryMediaListWsDTO();
					if (null != luxuryMedia.getURL() && StringUtils.isNotEmpty(luxuryMedia.getURL()))
					{
						luxuryMediaList.setURL(luxuryMedia.getLocation());

					}
					if (null != luxuryMedia.getUrlLink() && StringUtils.isNotEmpty(luxuryMedia.getUrlLink()))
					{
						luxuryMediaList.setUrlLink(luxuryMedia.getUrlLink());

					}
					if (null != luxuryMedia.getMediaFormat() && StringUtils.isNotEmpty(luxuryMedia.getMediaFormat().getQualifier()))
					{
						luxuryMediaList.setMdeiaFormat(luxuryMedia.getMediaFormat().getQualifier());

					}
					medias.add(luxuryMediaList);

				}
			}

			luxMediaContainer1.setQualifier(qualifier);
			luxMediaContainer1.setName(name);
			luxMediaContainer1.setMedias(medias);

			LuxMediaContainerSecondColList.add(luxMediaContainer1);


		}
		if (null == luxuryComponent.getLuxuryLookBookComponent())
		{
			luxuryComponent.setLuxuryLookBookComponent(luxuryLookBookComponentListObjs);
		}
		luxuryLookBookComponentListObj.setFirstCol(LuxMediaContainerList);
		luxuryLookBookComponentListObj.setSecondCol(LuxMediaContainerSecondColList);
		luxuryComponent.getLuxuryLookBookComponent().add(luxuryLookBookComponentListObj);

		return luxuryComponent;
	}



}
