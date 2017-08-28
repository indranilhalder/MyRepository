/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.lux.model.cms.components.LuxShopByCategoryModel;
import com.tisl.lux.model.cms.components.ShopOnLuxuryElementModel;
import com.tisl.lux.model.cms.components.ShopOnLuxuryModel;
import com.tisl.lux.model.cms.components.WeeklySpecialBannerModel;
import com.tisl.lux.model.cms.components.WeeklySpecialModel;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.wsdto.LuxBannerComponentWsDTO;
import com.tisl.mpl.wsdto.LuxShopByCategoryWsDTO;
import com.tisl.mpl.wsdto.LuxuryComponentsListWsDTO;
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
	public LuxuryComponentsListWsDTO getLuxuryHomePage() throws CMSItemNotFoundException
	{

		LuxuryComponentsListWsDTO luxuryAllComponents = new LuxuryComponentsListWsDTO();
		final ContentPageModel contentPage = getMplCMSPageService().getPageByLabelOrId("luxuryMenlandingPage");
		if (contentPage != null)
		{

			for (final ContentSlotForTemplateModel contentSlotForPage : contentPage.getMasterTemplate().getContentSlots())
			{
				final ContentSlotModel contentSlot = contentSlotForPage.getContentSlot();
				luxuryAllComponents = getLuxuryComponentDtoForSlot(contentSlot, luxuryAllComponents);

			}
			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				final ContentSlotModel contentSlot = contentSlotForPage.getContentSlot();
				luxuryAllComponents = getLuxuryComponentDtoForSlot(contentSlot, luxuryAllComponents);

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
					case "LuxShopByCategory":
						final LuxShopByCategoryModel LuxShopByCategoryComponent = (LuxShopByCategoryModel) abstractCMSComponentModel;
						luxuryComponentsList = getLuxShopByCategoryWsDTO(LuxShopByCategoryComponent, luxuryComponentsListWsDTO);
						break;

					default:
						break;
				}
			}

		}
		//}
		return luxuryComponentsList;
	}

	private LuxuryComponentsListWsDTO getWeeklySpecialWsDTO(final WeeklySpecialModel weeklySpecialComponent,
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO)
	{
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
		luxuryComponent.setLuxuryWeeklySpecialBanner(weeklySpecialBannerListObj);
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

	private LuxuryComponentsListWsDTO getLuxShopByCategoryWsDTO(final LuxShopByCategoryModel luxShopByCategoryComponent,
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
}
