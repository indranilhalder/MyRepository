/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplBigFourPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.marketplacecommerceservices.service.InternalExternalAutomationService;
import com.tisl.mpl.model.cms.components.MplSequentialBannerComponentModel;


/**
 * @author TCS
 *
 */
public class InternalExternalAutomationServiceImpl implements InternalExternalAutomationService
{

	Logger LOG = Logger.getLogger(this.getClass());
	@Autowired
	private CMSPageService cmsPageService;

	/*
	 * All banner components are scanned and results are returned as Map
	 */
	@Override
	public Map<String, String> automationGetAllBanner()
	{
		final Collection<ContentPageModel> contentPages = cmsPageService.getAllContentPages();
		try
		{
			for (final ContentPageModel contentPageItr : contentPages)
			{
				final Map<String, String> automationMap = new HashMap<String, String>();
				@SuppressWarnings("deprecation")
				final List<ContentSlotForPageModel> allSlots = contentPageItr.getContentSlots();
				for (final ContentSlotForPageModel eachSlot : allSlots)
				{
					final ContentSlotModel slot = eachSlot.getContentSlot();
					final List<AbstractCMSComponentModel> components = slot.getCmsComponents();
					for (final AbstractCMSComponentModel componentItr : components)
					{
						// 1.For Mpl Sequntial Banners only
						if (null != componentItr && componentItr instanceof MplSequentialBannerComponentModel)
						{
							automationMap.put("asset_name", componentItr.getName());
							automationMap.put("source_page", contentPageItr.getLabel());
							final MplSequentialBannerComponentModel seqBannerComponent = (MplSequentialBannerComponentModel) componentItr;
							final Collection<BannerComponentModel> bannerComponentModels = seqBannerComponent.getBannersList();
							for (final BannerComponentModel banner : bannerComponentModels)
							{
								if (null != banner && banner instanceof MplBigPromoBannerComponentModel)
								{
									final MplBigPromoBannerComponentModel bigPromoBanner = (MplBigPromoBannerComponentModel) banner;
									automationMap.put("category_id",
											bigPromoBanner.getMajorPromoText() + "|" + bigPromoBanner.getMinorPromo1Text() + "|"
													+ bigPromoBanner.getMinorPromo2Text());
									if (null != bigPromoBanner.getBannerImage())
									{
										automationMap.put("media_type", bigPromoBanner.getBannerImage().getMime());
										automationMap.put("size", bigPromoBanner.getBannerImage().getSize().toString());
									}
								}
								else if (null != banner && banner instanceof MplBigFourPromoBannerComponentModel)
								{
									final MplBigFourPromoBannerComponentModel bigFourBanner = (MplBigFourPromoBannerComponentModel) banner;
									LOG.info("MplBigFourPromoBannerComponentModel -->> " + bigFourBanner.getBannerImage());
								}
								else
								{
									LOG.info("" + banner.getMedia());
								}
							}
						}
						//2.Mpl Big Promo BannerComponent Model only
						if (null != componentItr && componentItr instanceof MplBigPromoBannerComponentModel)
						{
							final MplBigPromoBannerComponentModel bigPromoBanner = (MplBigPromoBannerComponentModel) componentItr;
							automationMap.put(
									"category_id",
									bigPromoBanner.getMajorPromoText() + "|" + bigPromoBanner.getMinorPromo1Text() + "|"
											+ bigPromoBanner.getMinorPromo2Text());

							if (null != bigPromoBanner.getBannerImage())
							{
								automationMap.put("media_type", bigPromoBanner.getBannerImage().getMime());
								automationMap.put("size", bigPromoBanner.getBannerImage().getSize().toString());
							}
						}
						//3. Mpl BigFour PromoBanner ComponentModel
						if (null != componentItr && componentItr instanceof MplBigFourPromoBannerComponentModel)
						{
							final MplBigFourPromoBannerComponentModel bigPromoBanner = (MplBigFourPromoBannerComponentModel) componentItr;
							automationMap.put("category_id", bigPromoBanner.getPromoText1() + "|" + bigPromoBanner.getPromoText2() + "|"
									+ bigPromoBanner.getPromoText3() + "" + bigPromoBanner.getPromoText4());
							if (null != bigPromoBanner.getBannerImage())
							{
								automationMap.put("media_type", bigPromoBanner.getBannerImage().getMime());
								automationMap.put("size", bigPromoBanner.getBannerImage().getSize().toString());
							}
						}
						LOG.info("componentItr.getName() " + componentItr.getName());
					}
				}
				//LOG.info("banner componenets found " + contentPageItr.getco);
			}
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return null;
	}

	/*
	 * All banner components are fed to create excel or csv
	 */
	@Override
	public boolean createCSVExcel(final Map<String, String> exportMap)
	{
		return false;
	}


}
