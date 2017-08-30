/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.lux.model.LuxuryMediaModel;
import com.tisl.lux.model.LuxuryVideoComponentModel;
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
import com.tisl.mpl.wsdto.LuxShopByCategoryWsDTO;
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

	private LuxuryComponentsListWsDTO luxuryAllComponents;

	private MplCMSPageServiceImpl mplCMSPageService;



	/**
	 * @return the luxuryAllComponents
	 */
	public LuxuryComponentsListWsDTO getLuxuryAllComponents()
	{
		return luxuryAllComponents;
	}

	/**
	 * @param luxuryAllComponents
	 *           the luxuryAllComponents to set
	 */
	public void setLuxuryAllComponents(final LuxuryComponentsListWsDTO luxuryAllComponents)
	{
		this.luxuryAllComponents = luxuryAllComponents;
	}



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


		if (contentPage != null)
		{

			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				final ContentSlotModel contentSlotModel = contentSlotForPage.getContentSlot();
				luxuryAllComponents = getLuxuryComponentDtoForSlot(contentSlotModel, getLuxuryAllComponents());

			}


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


					default:
						break;
				}
			}

		}
		//}
		return luxuryComponentsList;
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
		weeklySpecialBannerListObj.setWeeklySpecialBannerList(weeklySpecialBannerList);
		weeklySpecialBanners.add(weeklySpecialBannerListObj);
		luxuryComponent.setLuxuryWeeklySpecialBanner(weeklySpecialBanners);
		return luxuryComponent;
	}




	private LuxuryComponentsListWsDTO getShopOnLuxuryWsDTO(final ShopOnLuxuryModel luxuryShopOnLuxuryComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
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
		shopOnLuxuryElementListObj.setShopOnLuxuryElements(shopOnLuxuryElementList);
		luxuryComponent.setLuxuryShopOnLuxury(shopOnLuxuryElementListObj);
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getLuxShopByCategoryWsDTO(final ShopByCategoryModel luxShopByCategoryComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
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
		luxShopByCategoryWsDTO.setRelatedImage(relatedImageList);
		luxuryComponent.setLuxShopByCategory(luxShopByCategoryWsDTO);
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getLuxCMSMediaParagraphWsDTO(
			final LuxCMSMediaParagraphComponentModel LuxCMSMediaParagraphComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{

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


		LuxCMSMediaParagraphComponentListObj.setMedias(LuxBannerComponentList);
		luxuryComponent.setLuxCMSMediaParagraphComponent(LuxCMSMediaParagraphComponentListObj);
		return luxuryComponent;
	}



	private LuxuryComponentsListWsDTO getLuxuryLookBookComponentWsDTO(final LuxuryLookBookComponentModel luxuryLookBookComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
		final LuxuryComponentsListWsDTO luxuryComponent = luxuryComponentsListWsDTO;
		final List<LuxMediaContainerWsDTO> LuxMediaContainerList = new ArrayList<LuxMediaContainerWsDTO>();
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
					final LuxuryMediaModel luxuryMedia = (LuxuryMediaModel) media;
					final LuxuryMediaListWsDTO luxuryMediaList = new LuxuryMediaListWsDTO();
					if (null != luxuryMedia.getURL() && StringUtils.isNotEmpty(luxuryMedia.getURL()))
					{
						luxuryMediaList.setURL(luxuryMedia.getURL());

					}
					if (null != luxuryMedia.getUrlLink() && StringUtils.isNotEmpty(luxuryMedia.getUrlLink()))
					{
						luxuryMediaList.setURL(luxuryMedia.getUrlLink());

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
						luxuryMediaList.setURL(luxuryMedia.getURL());

					}
					if (null != luxuryMedia.getUrlLink() && StringUtils.isNotEmpty(luxuryMedia.getUrlLink()))
					{
						luxuryMediaList.setURL(luxuryMedia.getUrlLink());

					}
					medias.add(luxuryMediaList);

				}
			}

			luxMediaContainer1.setQualifier(qualifier);
			luxMediaContainer1.setName(name);
			luxMediaContainer1.setMedias(medias);

			LuxMediaContainerList.add(luxMediaContainer1);


		}
		luxuryLookBookComponentListObj.setFirstCol(LuxMediaContainerList);
		luxuryComponent.setLuxuryLookBookComponent(luxuryLookBookComponentListObj);

		return luxuryComponent;
	}



}
