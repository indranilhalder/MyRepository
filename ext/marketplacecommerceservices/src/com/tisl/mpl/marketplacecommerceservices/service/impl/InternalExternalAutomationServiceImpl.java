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

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
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
	private static final String COMMA_DELIMITER = ",";
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
							campaignDataSeqBanner = new InternalCampaignReportData();
							// Storing data for generating Internal Report
							campaignDataSeqBanner.setAssetName(componentItr.getName());
							if (contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
							{
								campaignDataSeqBanner.setSourcePage(contentPageItr.getLabel().substring(
										contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
							}
							else
							{
								campaignDataSeqBanner.setSourcePage(contentPageItr.getLabel());
							}
							campaignDataSeqBanner.setIcid(componentItr.getPk().toString());

							final MplSequentialBannerComponentModel seqBannerComponent = (MplSequentialBannerComponentModel) componentItr;
							final Collection<BannerComponentModel> bannerComponentModels = seqBannerComponent.getBannersList();
							for (final BannerComponentModel banner : bannerComponentModels)
							{
								if (banner instanceof MplBigPromoBannerComponentModel)
								{
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
										if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
												&& !bigPromoBanner.getBannerImage().getURL().startsWith(HTTP))
										{
											sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
											sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
											imageUrl = sb.toString();
											imageSize = findIamgeSize(imageUrl);
										}
										else if (null != bigPromoBanner.getBannerImage()
												&& null != bigPromoBanner.getBannerImage().getURL()
												&& !bigPromoBanner.getBannerImage().getURL().startsWith(HTTPS))
										{

											sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
											sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
											imageUrl = sb.toString();
											imageSize = findIamgeSize(imageUrl);
										}
										campaignDataSeqBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
										campaignDataSeqBanner.setSize(imageSize);
										CampaignDataList.add(campaignDataSeqBanner);
									}
									catch (final Exception e)
									{
										LOG.error(e.getMessage());
									}
								}
								else if (banner instanceof MplBigFourPromoBannerComponentModel)
								{
									final MplBigFourPromoBannerComponentModel bigFourBanner = (MplBigFourPromoBannerComponentModel) banner;
									LOG.info("MplBigFourPromoBannerComponentModel -->> " + bigFourBanner.getBannerImage());
								}
								else
								{
									LOG.info("MplBigFourPromoBannerComponentModel ......" + banner.getMedia());
								}
							}
						}
						//2.Mpl Big Promo BannerComponent Model only
						if (componentItr instanceof MplBigPromoBannerComponentModel)
						{
							campaignDataBigPromoBanner = new InternalCampaignReportData();
							final MplBigPromoBannerComponentModel bigPromoBanner = (MplBigPromoBannerComponentModel) componentItr;
							String CategoryBigPromoBanner = "";
							if (contentPageItr.getCategoryAssociated() != null)
							{
								CategoryBigPromoBanner = contentPageItr.getCategoryAssociated().getCode();
							}
							else
							{
								CategoryBigPromoBanner = "";
							}
							campaignDataBigPromoBanner.setIcid(componentItr.getPk().toString());
							if (contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
							{
								campaignDataBigPromoBanner.setSourcePage(contentPageItr.getLabel().substring(
										contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
							}
							else
							{
								campaignDataBigPromoBanner.setSourcePage(contentPageItr.getLabel());
							}
							campaignDataBigPromoBanner.setAssetName(componentItr.getName());
							campaignDataBigPromoBanner.setCategory(CategoryBigPromoBanner);

							try
							{

								if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
										&& !bigPromoBanner.getBannerImage().getURL().startsWith(HTTP))
								{
									sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
									sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
									imageUrl = sb.toString();
									imageSize = findIamgeSize(imageUrl);
								}
								else if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
										&& !bigPromoBanner.getBannerImage().getURL().startsWith(HTTPS))
								{
									sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
									sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
									imageUrl = sb.toString();
									imageSize = findIamgeSize(imageUrl);
								}
								campaignDataBigPromoBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
								campaignDataBigPromoBanner.setSize(imageSize);
								CampaignDataList.add(campaignDataBigPromoBanner);
							}
							catch (final Exception e)
							{
								LOG.error(e.getMessage());
							}
							CampaignDataList.add(campaignDataBigPromoBanner);
						}
						//3. Mpl BigFour PromoBanner ComponentModel
						if (componentItr instanceof MplBigFourPromoBannerComponentModel)
						{
							campaignDataBigFourPromoBanner = new InternalCampaignReportData();
							final MplBigFourPromoBannerComponentModel bigPromoBanner = (MplBigFourPromoBannerComponentModel) componentItr;
							String CategoryBigFourPromoBanner = "";
							if (contentPageItr.getCategoryAssociated() != null)
							{
								CategoryBigFourPromoBanner = contentPageItr.getCategoryAssociated().getCode();
							}

							else
							{
								CategoryBigFourPromoBanner = "";
							}
							campaignDataBigFourPromoBanner.setIcid(componentItr.getPk().toString());
							campaignDataBigFourPromoBanner.setAssetName(componentItr.getName());
							if (contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
							{
								campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel().substring(
										contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
							}
							else
							{
								campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel());
							}
							campaignDataBigFourPromoBanner.setCategory(CategoryBigFourPromoBanner);
							try
							{

								if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
										&& !bigPromoBanner.getBannerImage().getURL().startsWith(HTTP))
								{
									sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
									sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
									imageUrl = sb.toString();
									imageSize = findIamgeSize(imageUrl);
								}
								else if (null != bigPromoBanner.getBannerImage() && null != bigPromoBanner.getBannerImage().getURL()
										&& !bigPromoBanner.getBannerImage().getURL().startsWith(HTTPS))
								{

									sb = new StringBuffer(bigPromoBanner.getBannerImage().getURL());
									sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
									imageUrl = sb.toString();
									imageSize = findIamgeSize(imageUrl);
								}
								campaignDataBigFourPromoBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
								campaignDataBigFourPromoBanner.setSize(imageSize);
							}
							catch (final Exception e)
							{
								LOG.error(e.getMessage());
							}
							CampaignDataList.add(campaignDataBigFourPromoBanner);
						}
						//4. Rotating Banner Component
						if (componentItr instanceof RotatingImagesComponentModel)
						{
							final RotatingImagesComponentModel banner = (RotatingImagesComponentModel) componentItr;
							final List<BannerComponentModel> bannerComponentList = banner.getBanners();
							for (final BannerComponentModel differentBanner : bannerComponentList)
							{
								if (null != differentBanner)
								{
									campaignDataBigFourPromoBanner = new InternalCampaignReportData();
									String rotaingImagesBannerCategory = "";

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
									if (contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
									{
										campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel().substring(
												contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
									}
									else
									{
										campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel());
									}
									campaignDataBigFourPromoBanner.setCategory(rotaingImagesBannerCategory);
									try
									{
										if (null != differentBanner.getMedia() && null != differentBanner.getMedia().getURL()
												&& !differentBanner.getMedia().getURL().startsWith(HTTP))
										{
											sb = new StringBuffer(differentBanner.getMedia().getURL());
											sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
											imageUrl = sb.toString();
											imageSize = findIamgeSize(imageUrl);
											campaignDataBigFourPromoBanner.setSize(imageSize);
										}
										else if (null != differentBanner.getMedia() && null != differentBanner.getMedia().getURL()
												&& !differentBanner.getMedia().getURL().startsWith(HTTPS))
										{
											sb = new StringBuffer(differentBanner.getMedia().getURL());
											sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
											imageUrl = sb.toString();
											imageSize = findIamgeSize(imageUrl);
											campaignDataBigFourPromoBanner.setSize(imageSize);
										}
										if (differentBanner instanceof MplBigFourPromoBannerComponentModel)
										{
											final MediaModel special = ((MplBigFourPromoBannerComponentModel) differentBanner)
													.getBannerImage();
											campaignDataBigFourPromoBanner.setMediaType(special.getMime());
											if (null != special.getURL())
											{
												sb = new StringBuffer(((MplBigFourPromoBannerComponentModel) differentBanner)
														.getBannerImage().getURL());
												sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
												imageUrl = sb.toString();
												imageSize = findIamgeSize(imageUrl);
											}
											else if (special.getURL().startsWith(HTTPS))
											{
												sb = new StringBuffer(((MplBigFourPromoBannerComponentModel) differentBanner)
														.getBannerImage().getURL());
												sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
												imageUrl = sb.toString();
												imageSize = findIamgeSize(imageUrl);
											}
											campaignDataBigFourPromoBanner.setSize(imageSize);
											campaignDataBigFourPromoBanner.setMediaType(special.getMime());
										}
										else if (differentBanner instanceof MplBigPromoBannerComponentModel)
										{
											final MediaModel special = ((MplBigPromoBannerComponentModel) differentBanner).getBannerImage();
											campaignDataBigFourPromoBanner.setMediaType(special.getMime());
											if (null != special.getURL())
											{
												sb = new StringBuffer(((MplBigPromoBannerComponentModel) differentBanner).getBannerImage()
														.getURL());
												sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
												imageUrl = sb.toString();
												imageSize = findIamgeSize(imageUrl);
											}
											else if (special.getURL().startsWith(HTTPS))
											{
												sb = new StringBuffer(((MplBigPromoBannerComponentModel) differentBanner).getBannerImage()
														.getURL());
												sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
												imageUrl = sb.toString();
												imageSize = findIamgeSize(imageUrl);
											}
											campaignDataBigFourPromoBanner.setSize(imageSize);
											campaignDataBigFourPromoBanner.setMediaType(special.getMime());
										}
										else
										{
											campaignDataBigFourPromoBanner.setMediaType(differentBanner.getMedia().getMime());
										}
									}
									catch (final Exception e)
									{
										LOG.error(e.getMessage());
									}
									CampaignDataList.add(campaignDataBigFourPromoBanner);
								}
							}
						}
						//5. Simple banner component
						if (componentItr instanceof SimpleBannerComponentModel)
						{
							final SimpleBannerComponentModel simple = (SimpleBannerComponentModel) componentItr;
							campaignDataBigFourPromoBanner = new InternalCampaignReportData();
							String imagesBannerCategory = "";

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
							if (contentPageItr.getLabel().contains(MICROSITE_SEPARATOR))
							{
								campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel().substring(
										contentPageItr.getLabel().lastIndexOf("/") + 1, contentPageItr.getLabel().length()));
							}
							else
							{
								campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel());
							}
							campaignDataBigFourPromoBanner.setCategory(imagesBannerCategory);
							try
							{
								if (null != simple.getMedia() && null != simple.getMedia().getURL()
										&& !simple.getMedia().getURL().startsWith(HTTP))
								{
									sb = new StringBuffer(simple.getMedia().getURL());
									sb.insert(0, MarketplacecommerceservicesConstants.HTTP);
									imageUrl = sb.toString();
									imageSize = findIamgeSize(imageUrl);
									campaignDataBigFourPromoBanner.setSize(imageSize);
								}
								else if (null != simple.getMedia() && null != simple.getMedia().getURL()
										&& !simple.getMedia().getURL().startsWith(HTTPS))
								{
									sb = new StringBuffer(simple.getMedia().getURL());
									sb.insert(0, MarketplacecommerceservicesConstants.HTTPS);
									imageUrl = sb.toString();
									imageSize = findIamgeSize(imageUrl);
									campaignDataBigFourPromoBanner.setSize(imageSize);
								}
								campaignDataBigFourPromoBanner.setMediaType(simple.getMedia().getMime());
							}
							catch (final Exception e)
							{
								LOG.error("Cron Job Simple Banner Component Error ", e);
							}
							CampaignDataList.add(campaignDataBigFourPromoBanner);
						}
					}
				}
			}
			createCSVExcel(CampaignDataList);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
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
			final File file = new File(getOutputFilePath());
			file.getParentFile().mkdirs();
			populateCSV(campaignDataConsolidatedList, file);
		}

		catch (final Exception e)
		{
			LOG.info("Exception writing" + e.getMessage());
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
		FileWriter fileWriter = null;
		String CSVHeader = "";
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
		try
		{
			fileWriter = new FileWriter(file, false);
			CSVHeader = getCSVHeaderLine();

			fileWriter.append(CSVHeader);
			fileWriter.append(NEW_LINE_SEPARATOR);


			for (final InternalCampaignReportData internalCampaignData : campaignDataConsolidatedList)
			{

				if (internalCampaignData.getAssetName() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getAssetName()).append(COMMA_DELIMITER);
				}


				if (internalCampaignData.getCategory() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append("\"").append(internalCampaignData.getCategory()).append("\"").append(COMMA_DELIMITER);
				}

				if (internalCampaignData.getMediaType() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getMediaType()).append(COMMA_DELIMITER);
				}

				if (internalCampaignData.getSize() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getSize()).append(COMMA_DELIMITER);
				}

				if (internalCampaignData.getSourcePage() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getSourcePage()).append(COMMA_DELIMITER);
				}

				if (internalCampaignData.getIcid() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getIcid()).append(COMMA_DELIMITER);
				}

				fileWriter.append(NEW_LINE_SEPARATOR);
			}
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

	public String findIamgeSize(final String urlString)
	{

		final String username = "siteadmin";
		final String password = "ASDF!@#$asdf1234";
		String size = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			//final String urlString = "https://upload.wikimedia.org/wikipedia/commons/7/7a/Pollock_to_Hussey.jpg";
			final URL object = new URL(urlString);

			//final HttpsURLConnection connection = (HttpsURLConnection) object.openConnection();
			final URLConnection connection = object.openConnection();
			// int timeOut = connection.getReadTimeout();
			connection.setReadTimeout(60 * 1000);
			connection.setConnectTimeout(60 * 1000);
			final sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			final String authorization = username + ":" + password;
			final String encodedAuth = "Basic " + encoder.encode(authorization.getBytes());
			connection.setRequestProperty("Authorization", encodedAuth);
			//final int responseCode = connection.getResponseCode();
			//final int responseCode = connection.
			//System.out.println("" + responseCode);
			//final BufferedImage bimg = ImageIO.read(openURLForInput(url,username,password));
			final BufferedImage bimg = ImageIO.read(connection.getInputStream());
			final int width = bimg.getWidth();
			final int height = bimg.getHeight();
			size = width + " X " + height;

			//size = String.valueOf(width) + " X " + String.valueOf(height);
			LOG.info("Size is :::::::" + size);


		}
		catch (final MalformedURLException e)
		{
			LOG.info("Malformed URL: " + e.getMessage());
		}
		catch (final IOException e)
		{
			LOG.info("IO Exception: " + e.getMessage());
			e.printStackTrace();
		}
		catch (final Exception e)
		{
			LOG.info("Exception is: " + e.getMessage());
			e.printStackTrace();
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

			//	username = "siteadmin";
			//password = "ASDF!@#$asdf1234";

			// Return the information (a data holder that is used by Authenticator)
			return new PasswordAuthentication(username, password.toCharArray());

		}

	}

}