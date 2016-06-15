/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplBigFourPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.data.InternalCampaignReportData;
import com.tisl.mpl.marketplacecommerceservices.service.InternalExternalAutomationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.model.cms.components.MplSequentialBannerComponentModel;


/**
 * @author TCS
 *
 */
public class InternalExternalAutomationServiceImpl implements InternalExternalAutomationService
{

	//final Logger LOG = Logger.getLogger(this.getClass());
	private static final Logger LOG = Logger.getLogger(InternalExternalAutomationServiceImpl.class);
	private static final String encryptionKey = "MZygpewJsCpRrfOr";
	/*
	 * @Autowired private CMSPageService cmsPageService;
	 */
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CatalogVersionService catalogversionservice;

	@Autowired
	private MplCmsPageService mplCmsPageService;

	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String TAB_DELIMITER = "\t";
	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String MICROSITE_SEPARATOR = "/m/";

	/*
	 * All banner components are scanned and results are returned as Map
	 */
	@Override
	public List<InternalCampaignReportData> automationGetAllBanner()
	{

		final Collection<CatalogVersionModel> catalogmodelCollection = new ArrayList<CatalogVersionModel>();
		final List<InternalCampaignReportData> CampaignDataList = new ArrayList<InternalCampaignReportData>();

		InternalCampaignReportData campaignDataSeqBanner = null;
		InternalCampaignReportData campaignDataBigPromoBanner = null;
		InternalCampaignReportData campaignDataBigFourPromoBanner = null;
		StringBuffer sb = null;
		String imageUrl = MarketplacecommerceservicesConstants.EMPTY;
		String imageSize = MarketplacecommerceservicesConstants.EMPTY;
		try
		{

			final CatalogVersionModel catalogmodel = catalogversionservice.getCatalogVersion(configurationService.getConfiguration()
					.getString("internal.campaign.catelog"),
					configurationService.getConfiguration().getString("internal.campaign.catalogVersionName"));

			catalogmodelCollection.add(catalogmodel);

			final Collection<ContentPageModel> contentPages = mplCmsPageService.getAllContentPages(catalogmodelCollection);


			for (final ContentPageModel contentPageItr : contentPages)
			{
				@SuppressWarnings("deprecation")
				final List<ContentSlotForPageModel> allSlots = contentPageItr.getContentSlots();
				for (final ContentSlotForPageModel eachSlot : allSlots)
				{

					final ContentSlotModel slot = eachSlot.getContentSlot();
					final List<AbstractCMSComponentModel> components = slot.getCmsComponents();
					for (final AbstractCMSComponentModel componentItr : components)
					{

						// 1.For Mpl Sequntial Banners only
						if (componentItr instanceof MplSequentialBannerComponentModel)
						{
							LOG.info("Inside MplSequentialBannerComponentModel starts");
							campaignDataSeqBanner = new InternalCampaignReportData();
							// Storing data for generating Internal Report
							campaignDataSeqBanner.setAssetName(componentItr.getName());
							if (contentPageItr.getLabel() != null && contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
							{
								campaignDataSeqBanner.setSourcePage(contentPageItr.getLabel().substring(
										contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
								LOG.info("Inside MplSequentialBannerComponentModel.MICROSITE_SEPARATOR");
							}
							else
							{
								LOG.info("Inside MplSequentialBannerComponentModel.notMICROSITE_SEPARATOR");
								campaignDataSeqBanner.setSourcePage(MarketplacecommerceservicesConstants.EMPTY);
								//campaignDataBigPromoBanner.setSourcePage(MarketplacecommerceservicesConstants.EMPTY);
							}
							campaignDataSeqBanner.setIcid(componentItr.getPk().toString());

							final MplSequentialBannerComponentModel seqBannerComponent = (MplSequentialBannerComponentModel) componentItr;
							final Collection<BannerComponentModel> bannerComponentModels = seqBannerComponent.getBannersList();
							for (final BannerComponentModel banner : bannerComponentModels)
							{
								if (banner instanceof MplBigPromoBannerComponentModel)
								{
									LOG.info("Inside MplSequentialBannerComponentModel.MplBigPromoBannerComponentModel starts");
									String CategorySeqBanner = "";
									final MplBigPromoBannerComponentModel bigPromoBanner = (MplBigPromoBannerComponentModel) banner;
									if (contentPageItr.getCategoryAssociated() != null)
									{
										CategorySeqBanner = contentPageItr.getCategoryAssociated().getCode();
									}
									else
									{
										CategorySeqBanner = "";
									}
									campaignDataSeqBanner.setCategory(CategorySeqBanner);
									try
									{
										//										if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
										//												&& bigPromoBanner.getBannerImage().getURL().startsWith(HTTP))
										//										{
										//											LOG.info("Inside MplSequentialBannerComponentModel.MplBigPromoBannerComponentModel.HTTP");
										//											sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
										//											sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
										//											imageUrl = sb.toString();
										//											imageSize = findIamgeSize(imageUrl);
										//										}
										//										else if (null != bigPromoBanner.getBannerImage()
										//												&& null != bigPromoBanner.getBannerImage().getURL()
										//												&& bigPromoBanner.getBannerImage().getURL().startsWith(HTTPS))
										//										{
										//											LOG.info("Inside MplSequentialBannerComponentModel.MplBigPromoBannerComponentModel.HTTPS");
										//											sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
										//											sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
										//											imageUrl = sb.toString();
										//											imageSize = findIamgeSize(imageUrl);
										//										}

										if (null != bigPromoBanner.getBannerImage()
												&& StringUtils.isNotEmpty(bigPromoBanner.getBannerImage().getURL()))
										{
											final String imageURL = bigPromoBanner.getBannerImage().getURL();
											LOG.info("Image url is:::" + imageURL);
											//											if (imageURL.startsWith(HTTP))
											//											{
											//												LOG.info("Inside MplSequentialBannerComponentModel.MplBigPromoBannerComponentModel.HTTP");
											//												sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
											//												sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
											//												imageUrl = sb.toString();
											//												imageSize = findIamgeSize(imageUrl);
											//											}
											//else if (imageURL.startsWith(HTTPS))
											//{
											LOG.info("Inside MplSequentialBannerComponentModel.MplBigPromoBannerComponentModel.HTTPS");
											sb = new StringBuffer(imageURL);
											sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
											imageUrl = sb.toString();
											imageSize = findImageSize(imageUrl);
											//}
										}
										if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getMime())
										{
											LOG.info("Inside MplSequentialBannerComponentModel.MplBigPromoBannerComponentModel.getMimeNotNull");
											campaignDataSeqBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
										}
										else
										{
											campaignDataSeqBanner.setMediaType(MarketplacecommerceservicesConstants.EMPTY);
										}

										if (null != imageSize)
										{
											LOG.info("Inside MplSequentialBannerComponentModel.MplBigPromoBannerComponentModel.imageSizeNotNull:::"
													+ imageSize);
											campaignDataSeqBanner.setSize(imageSize);
										}
										else
										{
											campaignDataSeqBanner.setSize(MarketplacecommerceservicesConstants.EMPTY);
										}

										CampaignDataList.add(campaignDataSeqBanner);
										LOG.info("Inside MplSequentialBannerComponentModel.MplBigPromoBannerComponentModel ends");
									}
									catch (final Exception e)
									{
										LOG.error("MplBigPromoBannerComponentModel Exception: ", e);

									}
								}
								//								else if (banner instanceof MplBigFourPromoBannerComponentModel)
								//								{
								//									final MplBigFourPromoBannerComponentModel bigFourBanner = (MplBigFourPromoBannerComponentModel) banner;
								//									LOG.info("MplBigFourPromoBannerComponentModel -->> " + bigFourBanner.getBannerImage());
								//								}
								//								else
								//								{
								//									LOG.info("MplBigFourPromoBannerComponentModel ......" + banner.getMedia());
								//								}
							}
							LOG.info("Stepping out from MplSequentialBannerComponentModel");
						}
						//2.Mpl Big Promo BannerComponent Model only
						if (componentItr instanceof MplBigPromoBannerComponentModel)
						{
							LOG.info("Inside MplBigPromoBannerComponentModel starts");
							campaignDataBigPromoBanner = new InternalCampaignReportData();
							final MplBigPromoBannerComponentModel bigPromoBanner = (MplBigPromoBannerComponentModel) componentItr;
							String CategoryBigPromoBanner = MarketplacecommerceservicesConstants.EMPTY;
							if (contentPageItr.getCategoryAssociated() != null)
							{
								CategoryBigPromoBanner = contentPageItr.getCategoryAssociated().getCode();
							}
							else
							{
								CategoryBigPromoBanner = MarketplacecommerceservicesConstants.EMPTY;
							}
							campaignDataBigPromoBanner.setIcid(componentItr.getPk().toString());
							LOG.info("Inside MplBigPromoBannerComponentModel.beforeMICROSITE_SEPARATOR");
							if (contentPageItr.getLabel() != null && contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
							{
								campaignDataBigPromoBanner.setSourcePage(contentPageItr.getLabel().substring(
										contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
								LOG.info("Inside MplBigPromoBannerComponentModel.MICROSITE_SEPARATOR");
							}
							else
							{
								LOG.info("Inside MplBigPromoBannerComponentModel.notMICROSITE_SEPARATOR");
								//campaignDataBigPromoBanner.setSourcePage(contentPageItr.getLabel());
								campaignDataBigPromoBanner.setSourcePage(MarketplacecommerceservicesConstants.EMPTY);
							}
							campaignDataBigPromoBanner.setAssetName(componentItr.getName());
							campaignDataBigPromoBanner.setCategory(CategoryBigPromoBanner);
							LOG.info("Inside MplBigPromoBannerComponentModel.afterMICROSITE_SEPARATOR");
							try
							{

								//								if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
								//										&& bigPromoBanner.getBannerImage().getURL().startsWith(HTTP))
								//								{
								//									LOG.info("Inside MplBigPromoBannerComponentModel.HTTP");
								//									sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
								//									sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
								//									imageUrl = sb.toString();
								//									imageSize = findIamgeSize(imageUrl);
								//								}
								//								else if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
								//										&& bigPromoBanner.getBannerImage().getURL().startsWith(HTTPS))
								//								{
								//									LOG.info("Inside MplBigPromoBannerComponentModel.HTTPS");
								//									sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
								//									sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
								//									imageUrl = sb.toString();
								//									imageSize = findIamgeSize(imageUrl);
								//								}

								if (null != bigPromoBanner.getBannerImage()
										&& StringUtils.isNotEmpty(bigPromoBanner.getBannerImage().getURL()))
								{
									final String imageURL = bigPromoBanner.getBannerImage().getURL();
									LOG.info("Image url is:::" + imageURL);
									//									if (imageURL.startsWith(HTTP))
									//									{
									//										LOG.info("Inside MplBigPromoBannerComponentModel.HTTP");
									//										sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
									//										sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
									//										imageUrl = sb.toString();
									//										imageSize = findIamgeSize(imageUrl);
									//									}
									//else if (imageURL.startsWith(HTTPS))
									//{
									LOG.info("Inside MplBigPromoBannerComponentModel.HTTPS");
									sb = new StringBuffer(imageURL);
									sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
									imageUrl = sb.toString();
									imageSize = findImageSize(imageUrl);
									//}
								}
								if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getMime())
								{
									LOG.info("Inside MplBigPromoBannerComponentModel.getMimeNotNull");
									campaignDataBigPromoBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
								}
								else
								{
									campaignDataBigPromoBanner.setMediaType(MarketplacecommerceservicesConstants.EMPTY);
								}
								if (null != imageSize)
								{
									LOG.info("Inside MplBigPromoBannerComponentModel.imageSizeNotNull:::" + imageSize);
									campaignDataBigPromoBanner.setSize(imageSize);
								}

								else
								{
									campaignDataBigPromoBanner.setSize(MarketplacecommerceservicesConstants.EMPTY);
								}
								//CampaignDataList.add(campaignDataBigPromoBanner);
								LOG.info("Inside MplBigPromoBannerComponentModel ends");
							}
							catch (final Exception e)
							{
								LOG.error("MplBigPromoBannerComponentModel Exception: ", e);
							}
							CampaignDataList.add(campaignDataBigPromoBanner);
							LOG.info("Stepping out from MplBigPromoBannerComponentModel");
						}
						//3. Mpl BigFour PromoBanner ComponentModel
						if (componentItr instanceof MplBigFourPromoBannerComponentModel)
						{
							LOG.info("Inside MplBigFourPromoBannerComponentModel starts");
							campaignDataBigFourPromoBanner = new InternalCampaignReportData();
							final MplBigFourPromoBannerComponentModel bigPromoBanner = (MplBigFourPromoBannerComponentModel) componentItr;
							String CategoryBigFourPromoBanner = MarketplacecommerceservicesConstants.EMPTY;
							if (contentPageItr.getCategoryAssociated() != null)
							{
								CategoryBigFourPromoBanner = contentPageItr.getCategoryAssociated().getCode();
							}

							else
							{
								CategoryBigFourPromoBanner = MarketplacecommerceservicesConstants.EMPTY;
							}
							campaignDataBigFourPromoBanner.setIcid(componentItr.getPk().toString());
							campaignDataBigFourPromoBanner.setAssetName(componentItr.getName());
							if (contentPageItr.getLabel() != null && contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
							{
								campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel().substring(
										contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
								LOG.info("Inside MplBigFourPromoBannerComponentModel.MICROSITE_SEPARATOR");
							}
							else
							{
								//campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel());
								campaignDataBigFourPromoBanner.setSourcePage(MarketplacecommerceservicesConstants.EMPTY);
								LOG.info("Inside MplBigFourPromoBannerComponentModel.notMICROSITE_SEPARATOR");
							}
							campaignDataBigFourPromoBanner.setCategory(CategoryBigFourPromoBanner);
							try
							{

								//								if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
								//										&& bigPromoBanner.getBannerImage().getURL().startsWith(HTTP))
								//								{
								//									LOG.info("Inside MplBigFourPromoBannerComponentModel.HTTP");
								//									sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
								//									sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
								//									imageUrl = sb.toString();
								//									imageSize = findIamgeSize(imageUrl);
								//								}
								//								else if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
								//										&& bigPromoBanner.getBannerImage().getURL().startsWith(HTTPS))
								//								{
								//									LOG.info("Inside MplBigFourPromoBannerComponentModel.HTTPS");
								//									sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
								//									sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
								//									imageUrl = sb.toString();
								//									imageSize = findIamgeSize(imageUrl);
								//								}

								if (null != bigPromoBanner.getBannerImage()
										&& StringUtils.isNotEmpty(bigPromoBanner.getBannerImage().getURL()))
								{
									final String imageURL = bigPromoBanner.getBannerImage().getURL();
									LOG.info("Image url is:::" + imageURL);
									//									if (imageURL.startsWith(HTTP))
									//									{
									//										LOG.info("Inside MplBigFourPromoBannerComponentModel.HTTP");
									//										sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
									//										sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
									//										imageUrl = sb.toString();
									//										imageSize = findIamgeSize(imageUrl);
									//									}
									//									else if (imageURL.startsWith(HTTPS))
									//									{
									LOG.info("Inside MplBigFourPromoBannerComponentModel.HTTPS");
									sb = new StringBuffer(imageURL);
									sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
									imageUrl = sb.toString();
									imageSize = findImageSize(imageUrl);
									//									}
								}

								if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getMime())
								{
									LOG.info("Inside MplBigFourPromoBannerComponentModel.getMimeNotNull");
									campaignDataBigFourPromoBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
								}
								else
								{
									campaignDataBigFourPromoBanner.setMediaType(MarketplacecommerceservicesConstants.EMPTY);
								}
								if (null != imageSize)
								{
									LOG.info("Inside MplBigFourPromoBannerComponentModel.imageSizeNotNull:::" + imageSize);
									campaignDataBigFourPromoBanner.setSize(imageSize);
								}
								else
								{
									campaignDataBigFourPromoBanner.setSize(MarketplacecommerceservicesConstants.EMPTY);
								}

								//CampaignDataList.add(campaignDataBigFourPromoBanner);
								LOG.info("Inside MplBigFourPromoBannerComponentModel ends");
							}
							catch (final Exception e)
							{
								LOG.error("MplBigFourPromoBannerComponentModel Exception: ", e);
							}
							CampaignDataList.add(campaignDataBigFourPromoBanner);
							LOG.info("Stepping out from MplBigFourPromoBannerComponentModel");
						}
						//4. Rotating Banner Component
						if (componentItr instanceof RotatingImagesComponentModel)
						{
							LOG.info("Inside RotatingImagesComponentModel starts");
							final RotatingImagesComponentModel banner = (RotatingImagesComponentModel) componentItr;
							final List<BannerComponentModel> bannerComponentList = banner.getBanners();
							for (final BannerComponentModel differentBanner : bannerComponentList)
							{
								if (null != differentBanner)
								{
									campaignDataBigFourPromoBanner = new InternalCampaignReportData();
									String rotaingImagesBannerCategory = MarketplacecommerceservicesConstants.EMPTY;

									if (contentPageItr.getCategoryAssociated() != null)
									{
										rotaingImagesBannerCategory = contentPageItr.getCategoryAssociated().getCode();
									}
									else
									{
										rotaingImagesBannerCategory = "";
									}
									campaignDataBigFourPromoBanner.setIcid(differentBanner.getPk().toString());
									campaignDataBigFourPromoBanner.setAssetName(differentBanner.getName());
									if (contentPageItr.getLabel() != null && contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
									{
										campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel().substring(
												contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
										LOG.info("Inside RotatingImagesComponentModel.MICROSITE_SEPARATOR");
									}
									else
									{
										//campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel());
										campaignDataBigFourPromoBanner.setSourcePage(MarketplacecommerceservicesConstants.EMPTY);
										LOG.info("Inside RotatingImagesComponentModel.notMICROSITE_SEPARATOR");
									}
									campaignDataBigFourPromoBanner.setCategory(rotaingImagesBannerCategory);
									try
									{
										//										if (null != differentBanner.getMedia() && null != differentBanner.getMedia().getURL()
										//												&& differentBanner.getMedia().getURL().startsWith(HTTP))
										//										{
										//											LOG.info("Inside RotatingImagesComponentModel.HTTP");
										//											sb = new StringBuffer(differentBanner.getMedia().getURL());
										//											sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
										//											imageUrl = sb.toString();
										//											imageSize = findIamgeSize(imageUrl);
										//											campaignDataBigFourPromoBanner.setSize(imageSize);
										//										}
										//										else if (null != differentBanner.getMedia() && null != differentBanner.getMedia().getURL()
										//												&& differentBanner.getMedia().getURL().startsWith(HTTPS))
										//										{
										//											LOG.info("Inside RotatingImagesComponentModel.HTTPS");
										//											sb = new StringBuffer(differentBanner.getMedia().getURL());
										//											sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
										//											imageUrl = sb.toString();
										//											imageSize = findIamgeSize(imageUrl);
										//											campaignDataBigFourPromoBanner.setSize(imageSize);
										//										}


										if (null != differentBanner.getMedia()
												&& StringUtils.isNotEmpty(differentBanner.getMedia().getURL()))
										{
											final String imageURL = differentBanner.getMedia().getURL();
											LOG.info("Image url is:::" + imageURL);
											//											if (imageURL.startsWith(HTTP))
											//											{
											//												LOG.info("Inside RotatingImagesComponentModel.HTTP");
											//												sb = new StringBuffer(differentBanner.getMedia().getURL());
											//												sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
											//												imageUrl = sb.toString();
											//												imageSize = findIamgeSize(imageUrl);
											//											}
											//											else if (imageURL.startsWith(HTTPS))
											//											{
											LOG.info("Inside RotatingImagesComponentModel.HTTPS");
											sb = new StringBuffer(imageURL);
											sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
											imageUrl = sb.toString();
											imageSize = findImageSize(imageUrl);
											//											}
										}

										if (differentBanner instanceof MplBigFourPromoBannerComponentModel)
										{
											LOG.info("Inside RotatingImagesComponentModel.MplBigFourPromoBannerComponentModel");
											final MediaModel special = ((MplBigFourPromoBannerComponentModel) differentBanner)
													.getBannerImage() != null ? ((MplBigFourPromoBannerComponentModel) differentBanner)
													.getBannerImage() : null;

											if (special != null)
											{
												if (null != special.getMime())
												{
													LOG.info("Inside RotatingImagesComponentModel.MplBigFourPromoBannerComponentModel.getMimeNotNull");
													campaignDataBigFourPromoBanner.setMediaType(special.getMime());
												}

												if (null != special.getURL())
												{
													final String imageURL = special.getURL();
													LOG.info("Image url is:::" + imageURL);
													//													if (imageURL.startsWith(HTTP))
													//													{
													//														LOG.info("Inside RotatingImagesComponentModel.MplBigFourPromoBannerComponentModel.HTTP");
													//														sb = new StringBuffer(((MplBigFourPromoBannerComponentModel) differentBanner)
													//																.getBannerImage().getURL());
													//														sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
													//														imageUrl = sb.toString();
													//														imageSize = findIamgeSize(imageUrl);
													//													}
													//													else if (imageURL.startsWith(HTTPS))
													//													{
													LOG.info("Inside RotatingImagesComponentModel.MplBigFourPromoBannerComponentModel.HTTPS");
													sb = new StringBuffer(((MplBigFourPromoBannerComponentModel) differentBanner)
															.getBannerImage().getURL());
													sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
													imageUrl = sb.toString();
													imageSize = findImageSize(imageUrl);
													//													}
												}

												//campaignDataBigFourPromoBanner.setSize(imageSize);
												if (null != imageSize)
												{
													LOG.info("Inside RotatingImagesComponentModel.MplBigFourPromoBannerComponentModel.imageSizeNotNull:::"
															+ imageSize);
													campaignDataBigFourPromoBanner.setSize(imageSize);
												}
												else
												{
													campaignDataBigFourPromoBanner.setSize(MarketplacecommerceservicesConstants.EMPTY);
												}

												campaignDataBigFourPromoBanner.setMediaType(special.getMime());
											}
										}
										else if (differentBanner instanceof MplBigPromoBannerComponentModel)
										{
											LOG.info("Inside RotatingImagesComponentModel.MplBigPromoBannerComponentModel");
											final MediaModel special = ((MplBigPromoBannerComponentModel) differentBanner).getBannerImage() != null ? ((MplBigPromoBannerComponentModel) differentBanner)
													.getBannerImage() : null;

											if (null != special)
											{
												LOG.info("Inside RotatingImagesComponentModel.MplBigPromoBannerComponentModel.specialNotNull");
												if (null != special.getMime())
												{
													LOG.info("Inside RotatingImagesComponentModel.MplBigPromoBannerComponentModel.getMimeNotNull");
													campaignDataBigFourPromoBanner.setMediaType(special.getMime());
												}


												//												if (null != special.getURL() && special.getURL().startsWith(HTTP))
												//												{
												//													LOG.info("Inside RotatingImagesComponentModel.MplBigPromoBannerComponentModel.HTTP");
												//													sb = new StringBuffer(((MplBigPromoBannerComponentModel) differentBanner).getBannerImage()
												//															.getURL());
												//													sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
												//													imageUrl = sb.toString();
												//
												//													imageSize = findIamgeSize(imageUrl);
												//
												//												}
												//												else if (null != special.getURL() && special.getURL().startsWith(HTTPS))
												//												{
												//													LOG.info("Inside RotatingImagesComponentModel.MplBigPromoBannerComponentModel.HTTPS");
												//													sb = new StringBuffer(((MplBigPromoBannerComponentModel) differentBanner).getBannerImage()
												//															.getURL());
												//													sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
												//													imageUrl = sb.toString();
												//													imageSize = findIamgeSize(imageUrl);
												//												}

												if (null != special.getURL())
												{
													final String imageURL = special.getURL();
													LOG.info("Image url is:::" + imageURL);
													//													if (imageURL.startsWith(HTTP))
													//													{
													//														LOG.info("Inside RotatingImagesComponentModel.MplBigFourPromoBannerComponentModel.HTTP");
													//														sb = new StringBuffer(((MplBigPromoBannerComponentModel) differentBanner)
													//																.getBannerImage().getURL());
													//														sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
													//														imageUrl = sb.toString();
													//														imageSize = findIamgeSize(imageUrl);
													//													}
													//													else if (imageURL.startsWith(HTTPS))
													//													{
													LOG.info("Inside RotatingImagesComponentModel.MplBigFourPromoBannerComponentModel.HTTPS");
													sb = new StringBuffer(((MplBigPromoBannerComponentModel) differentBanner).getBannerImage()
															.getURL());
													sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
													imageUrl = sb.toString();
													imageSize = findImageSize(imageUrl);
													//													}
												}

												if (null != imageSize)
												{
													LOG.info("Inside RotatingImagesComponentModel.MplBigPromoBannerComponentModel.imageSizeNotNull:::"
															+ imageSize);
													campaignDataBigFourPromoBanner.setSize(imageSize);
												}
												else
												{
													campaignDataBigFourPromoBanner.setSize(MarketplacecommerceservicesConstants.EMPTY);
												}

												if (null != special.getMime())
												{
													LOG.info("Inside RotatingImagesComponentModel.MplBigPromoBannerComponentModel.getMimeNotNull");
													campaignDataBigFourPromoBanner.setMediaType(special.getMime());
												}
												else
												{

													campaignDataBigFourPromoBanner.setMediaType(MarketplacecommerceservicesConstants.EMPTY);
												}

											}
											else
											{
												if (null != differentBanner.getMedia() && null != differentBanner.getMedia().getMime())
												{
													campaignDataBigFourPromoBanner.setMediaType(differentBanner.getMedia().getMime());
												}
												else
												{

													campaignDataBigFourPromoBanner.setMediaType(MarketplacecommerceservicesConstants.EMPTY);
												}

											}

										}
										//CampaignDataList.add(campaignDataBigFourPromoBanner);
										LOG.info("Inside RotatingImagesComponentModel ends");
									}
									catch (final Exception e)
									{
										LOG.error("RotatingImagesComponentModel Exception: ", e);
									}
									CampaignDataList.add(campaignDataBigFourPromoBanner);
								}
							}
							LOG.info("Stepping out from RotatingImagesComponentModel");
						}
						//5. Simple banner component
						if (componentItr instanceof SimpleBannerComponentModel)
						{
							LOG.info("Inside SimpleBannerComponentModel");
							campaignDataBigFourPromoBanner = new InternalCampaignReportData();
							String imagesBannerCategory = MarketplacecommerceservicesConstants.EMPTY;

							if (contentPageItr.getCategoryAssociated() != null)
							{
								imagesBannerCategory = contentPageItr.getCategoryAssociated().getCode();
							}
							else
							{
								imagesBannerCategory = "";
							}
							campaignDataBigFourPromoBanner.setIcid(componentItr.getPk().toString());
							campaignDataBigFourPromoBanner.setAssetName(componentItr.getName());
							if (contentPageItr.getLabel() != null && contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
							{
								campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel().substring(
										contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
								LOG.info("Inside SimpleBannerComponentModel.MICROSITE_SEPARATOR");
							}
							else
							{
								//campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel());
								campaignDataBigFourPromoBanner.setSourcePage(MarketplacecommerceservicesConstants.EMPTY);
							}
							campaignDataBigFourPromoBanner.setCategory(imagesBannerCategory);
							try
							{
								final SimpleBannerComponentModel simple = (SimpleBannerComponentModel) componentItr;

								//								if (null != simple.getMedia() && null != simple.getMedia().getURL()
								//										&& simple.getMedia().getURL().startsWith(HTTP))
								//								{
								//									LOG.info("Inside SimpleBannerComponentModel.HTTP");
								//									sb = new StringBuffer(simple.getMedia().getURL());
								//									sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
								//									imageUrl = sb.toString();
								//
								//									imageSize = findIamgeSize(imageUrl);
								//
								//									campaignDataBigFourPromoBanner.setSize(imageSize);
								//								}
								//								else if (null != simple.getMedia() && null != simple.getMedia().getURL()
								//										&& simple.getMedia().getURL().startsWith(HTTPS))
								//								{
								//									LOG.info("Inside SimpleBannerComponentModel.HTTPS");
								//									sb = new StringBuffer(simple.getMedia().getURL());
								//									sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
								//									imageUrl = sb.toString();
								//									imageSize = findIamgeSize(imageUrl);
								//
								//
								//									if (null != imageSize)
								//									{
								//										campaignDataBigFourPromoBanner.setSize(MarketplacecommerceservicesConstants.EMPTY);
								//									}
								//									else
								//									{
								//										campaignDataBigFourPromoBanner.setSize(imageSize);
								//									}
								//								}



								if (null != simple.getMedia() && null != simple.getMedia().getURL())
								{
									final String imageURL = simple.getMedia().getURL();
									LOG.info("Image url is:::" + imageURL);
									//									if (imageURL.startsWith(HTTP))
									//									{
									//										LOG.info("Inside SimpleBannerComponentModel.HTTP");
									//										sb = new StringBuffer(simple.getMedia().getURL());
									//										sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
									//										imageUrl = sb.toString();
									//
									//										imageSize = findIamgeSize(imageUrl);
									//									}
									//									else if (imageURL.startsWith(HTTPS))
									//									{
									LOG.info("Inside SimpleBannerComponentModel.HTTPS");
									sb = new StringBuffer(imageURL);
									sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
									imageUrl = sb.toString();
									imageSize = findImageSize(imageUrl);
									//									}
								}
								if (null != simple.getMedia() && null != simple.getMedia().getMime())
								{
									LOG.info("Inside SimpleBannerComponentModel.getMimeNotNull");
									campaignDataBigFourPromoBanner.setMediaType(simple.getMedia().getMime());
								}
								else
								{
									campaignDataBigFourPromoBanner.setMediaType(MarketplacecommerceservicesConstants.EMPTY);
								}

								if (null != imageSize)
								{
									LOG.info("Inside RotatingImagesComponentModel.MplBigPromoBannerComponentModel.imageSizeNotNull:::"
											+ imageSize);
									campaignDataBigFourPromoBanner.setSize(imageSize);
								}
								else
								{
									campaignDataBigFourPromoBanner.setSize(MarketplacecommerceservicesConstants.EMPTY);
								}

								//CampaignDataList.add(campaignDataBigFourPromoBanner);
								LOG.info("Inside SimpleBannerComponentModel ends");
							}

							catch (final Exception e)
							{
								LOG.error("SimpleBannerComponentModel Exception: ", e);
							}
							CampaignDataList.add(campaignDataBigFourPromoBanner);
							LOG.info("Stepping out from SimpleBannerComponentModel");
						}
					}
				}
			}
			LOG.info("Before calling createCSVExcel()");
			createCSVExcel(CampaignDataList);
			LOG.info("After Completion of createCSVExcel()");
		}
		catch (final Exception e)
		{
			LOG.error("Exception in creating Banner Image: ", e);
		}
		return CampaignDataList;
	}

	/*
	 * All banner components are fed to create excel or csv
	 */
	@Override
	public void createCSVExcel(final List<InternalCampaignReportData> campaignDataConsolidatedList)
	{
		try
		{
			LOG.info("in createCSVExcel::");
			final File file = new File(getOutputFilePath());
			file.getParentFile().mkdirs();
			LOG.info("in createCSVExcel, before populateCSV()::");
			populateCSV(campaignDataConsolidatedList, file);

		}

		catch (final Exception e)
		{
			LOG.error("Exception writing", e);
		}
	}

	public String getCategoryOnAnchorTag(final String categoryId)
	{
		final Pattern p = Pattern.compile("<a href='(.*?)'>");
		final Matcher m = p.matcher(categoryId);
		return m.group(1);
	}

	public void populateCSV(final List<InternalCampaignReportData> campaignDataConsolidatedTmpList, final File file)
	{
		LOG.info("in createCSVExcel.populateCSV()::");
		FileWriter fileWriter = null;
		String CSVHeader = MarketplacecommerceservicesConstants.EMPTY;
		final List<InternalCampaignReportData> campaignDataConsolidatedList = new ArrayList<InternalCampaignReportData>();
		for (final InternalCampaignReportData internalCampaignReportData : campaignDataConsolidatedTmpList)
		{
			if (CollectionUtils.isEmpty(campaignDataConsolidatedList))
			{
				campaignDataConsolidatedList.add(internalCampaignReportData);
			}
			else
			{
				boolean isPresent = false;
				for (final InternalCampaignReportData finalData : campaignDataConsolidatedList)
				{
					if (finalData.getIcid() != null && internalCampaignReportData.getIcid() != null
							&& finalData.getIcid().equalsIgnoreCase(internalCampaignReportData.getIcid()))
					{
						isPresent = true;
						break;
					}
				}
				if (!isPresent)
				{
					campaignDataConsolidatedList.add(internalCampaignReportData);
				}
			}
		}
		LOG.info("in createCSVExcel.populateCSV() 222::");
		try
		{
			fileWriter = new FileWriter(file, false);
			CSVHeader = getCSVHeaderLine();

			fileWriter.append(CSVHeader);
			fileWriter.append(NEW_LINE_SEPARATOR);


			for (final InternalCampaignReportData internalCampaignData : campaignDataConsolidatedList)
			{
				if (internalCampaignData.getIcid() == null)
				{
					fileWriter.append("").append(TAB_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getIcid()).append(TAB_DELIMITER);
				}

				if (internalCampaignData.getAssetName() == null)
				{
					fileWriter.append("").append(TAB_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getAssetName()).append(TAB_DELIMITER);
				}


				if (internalCampaignData.getCategory() == null)
				{
					fileWriter.append("").append(TAB_DELIMITER);
				}
				else
				{
					fileWriter.append("\"").append(internalCampaignData.getCategory()).append("\"").append(TAB_DELIMITER);
				}

				if (internalCampaignData.getMediaType() == null)
				{
					fileWriter.append("").append(TAB_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getMediaType()).append(TAB_DELIMITER);
				}

				if (internalCampaignData.getSize() == null)
				{
					fileWriter.append("").append(TAB_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getSize()).append(TAB_DELIMITER);
				}

				if (internalCampaignData.getSourcePage() == null)
				{
					fileWriter.append("").append(TAB_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getSourcePage()).append(TAB_DELIMITER);
				}

				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			LOG.info("in createCSVExcel.populateCSV() 333::");
		}
		catch (final Exception e)
		{
			LOG.debug(MarketplacecommerceservicesConstants.CSV_ERROR + e);
		}
		finally
		{
			try
			{
				fileWriter.flush();
				fileWriter.close();
			}
			catch (final IOException e)
			{
				LOG.debug(MarketplacecommerceservicesConstants.FILE_WRITER_ERROR + e);
			}
		}
	}

	public String decrypt(final String encrypted) throws Exception
	{
		final Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
		final byte[] plainBytes = cipher.doFinal(Base64.decodeBase64(encrypted.getBytes()));

		return new String(plainBytes);
	}

	private Cipher getCipher(final int cipherMode) throws Exception
	{
		final String encryptionAlgorithm = "AES";
		final SecretKeySpec keySpecification = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), encryptionAlgorithm);
		final Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
		cipher.init(cipherMode, keySpecification);

		return cipher;
	}


	public String findImageSize(final String urlString) throws Exception
	{
		LOG.info("Inside findIamgeSize starts, urlString is: " + urlString);
		String size = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			final URL object = new URL(urlString);
			final URLConnection connection = object.openConnection();
			connection.setReadTimeout(60 * 1000);
			connection.setConnectTimeout(60 * 1000);
			LOG.info("Connection: " + connection);
			final String isAuthenticationRequired = configurationService.getConfiguration().getString(
					"internal.campaign.report.isAuthenticationRequired");
			LOG.info("isAuthenticationRequired flag value is: " + isAuthenticationRequired);
			if (StringUtils.isNotEmpty(isAuthenticationRequired)
					&& isAuthenticationRequired.equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
			{
				final String username = configurationService.getConfiguration().getString("internal.campaign.report.username");
				final String password = configurationService.getConfiguration().getString("internal.campaign.report.password");
				final sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
				final String authorization = decrypt(username) + ":" + decrypt(password);
				final String encodedAuth = "Basic " + encoder.encode(authorization.getBytes());
				connection.setRequestProperty("Authorization", encodedAuth);
			}
			//final int responseCode = connection.getResponseCode();
			//final int responseCode = connection.
			//System.out.println("" + responseCode);
			//final BufferedImage bimg = ImageIO.read(openURLForInput(url,username,password));
			final BufferedImage bimg = ImageIO.read(connection.getInputStream());
			LOG.info("connection InputStream: " + connection.getInputStream() + ", BufferedImage: " + bimg);
			if (null != bimg)
			{
				LOG.info("If bimg is NOT NULL:");
				final int width = bimg.getWidth();
				final int height = bimg.getHeight();
				LOG.info("If bimg is NOT NULL width, height: " + width + ", " + height);
				size = width + " X " + height;
			}
			else
			{
				size = MarketplacecommerceservicesConstants.EMPTY;
			}

			LOG.info("Size is :::::::" + size);


		}
		catch (final MalformedURLException e)
		{
			LOG.info("Malformed URL: ", e);
			throw e;
		}
		catch (final IOException e)
		{
			LOG.info("IO Exception: ", e);
			//e.printStackTrace();
			throw e;
		}
		catch (final Exception e)
		{
			LOG.info("Exception is: ", e);
			//e.printStackTrace();
			throw e;
		}
		return size;


	}

	public String findCategoryLink(final String categoryId)
	{
		String finalCategoryId = "";
		final int ind1 = categoryId.indexOf("href='");

		if (ind1 != -1)
		{
			final int ind2 = categoryId.indexOf("'", ind1 + 6);
			finalCategoryId = categoryId.substring(ind1 + 6, ind2);
		}
		else
		{
			finalCategoryId = categoryId;
		}
		return finalCategoryId;
	}

	protected String getCSVHeaderLine()
	{
		return configurationService.getConfiguration().getString("internal.campaign.report.header", "");
	}

	protected String getOutputFilePath()
	{
		final DateFormat df = new SimpleDateFormat(MarketplacecommerceservicesConstants.DATE_FORMAT_REPORT);
		final String timestamp = df.format(new Date());
		final StringBuilder output_file_path = new StringBuilder();
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.internalcampaign.feed.path", ""));
		output_file_path.append(File.separator);
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.internalcampaign.prefix", ""));
		output_file_path.append(MarketplacecommerceservicesConstants.FILE_PATH);
		output_file_path.append(timestamp);
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.internalcampaign.extension", ""));
		LOG.info("in createCSVExcel.getOutputFilePath()::");
		return output_file_path.toString();
	}

	public static class CustomAuthenticator extends Authenticator
	{

		// Called when password authorization is needed
		@Override
		protected PasswordAuthentication getPasswordAuthentication()
		{
			final String username = "";
			final String password = "";
			//System.out.println("=======+++++++++===============================");

			// Get information about the request

			// Return the information (a data holder that is used by Authenticator)
			return new PasswordAuthentication(username, password.toCharArray());

		}

	}

}
