/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	//static Logger LOG = Logger.getLogger(this.getClass());
	private static final Logger LOG = Logger.getLogger(InternalExternalAutomationServiceImpl.class);

	@Autowired
	private CMSPageService cmsPageService;
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CatalogVersionService catalogversionservice;

	@Autowired
	private MplCmsPageService mplCmsPageService;

	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String COMMA_DELIMITER = ",";

	/*
	 * All banner components are scanned and results are returned as Map
	 */
	@Override
	public List<InternalCampaignReportData> automationGetAllBanner()
	{
		final CatalogVersionModel catalogmodel = catalogversionservice.getCatalogVersion(configurationService.getConfiguration()
				.getString("internal.campaign.catelog"),
				configurationService.getConfiguration().getString("internal.campaign.catalogVersionName"));


		final Collection<CatalogVersionModel> catalogmodelCollection = new ArrayList<CatalogVersionModel>();
		catalogmodelCollection.add(catalogmodel);

		final Collection<ContentPageModel> contentPages = mplCmsPageService.getAllContentPages(catalogmodelCollection);
		final List<InternalCampaignReportData> CampaignDataList = new ArrayList<InternalCampaignReportData>();

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
							final InternalCampaignReportData campaignDataSeqBanner = new InternalCampaignReportData();
							automationMap.put("asset_name", componentItr.getName());
							automationMap.put("source_page", contentPageItr.getLabel());

							// Storing data for generating Internal Report
							campaignDataSeqBanner.setAssetName(componentItr.getName());
							campaignDataSeqBanner.setSourcePage(contentPageItr.getLabel());
							campaignDataSeqBanner.setIcid(componentItr.getPk().toString());

							final MplSequentialBannerComponentModel seqBannerComponent = (MplSequentialBannerComponentModel) componentItr;
							final Collection<BannerComponentModel> bannerComponentModels = seqBannerComponent.getBannersList();
							for (final BannerComponentModel banner : bannerComponentModels)
							{
								if (null != banner && banner instanceof MplBigPromoBannerComponentModel)
								{
									final MplBigPromoBannerComponentModel bigPromoBanner = (MplBigPromoBannerComponentModel) banner;

									String CategorySeqBanner = findCategoryLink(bigPromoBanner.getMajorPromoText() + "|"
											+ bigPromoBanner.getMinorPromo1Text() + "|" + bigPromoBanner.getMinorPromo2Text());

									CategorySeqBanner = CategorySeqBanner.substring(CategorySeqBanner.lastIndexOf("/") + 1,
											CategorySeqBanner.length());


									automationMap.put("category_id",
											bigPromoBanner.getMajorPromoText() + "|" + bigPromoBanner.getMinorPromo1Text() + "|"
													+ bigPromoBanner.getMinorPromo2Text());
									campaignDataSeqBanner.setCategory(CategorySeqBanner);
									try
									{
										if (null != bigPromoBanner.getBannerImage())
										{
											String ImageUrl = bigPromoBanner.getBannerImage().getURL();
											//System.out.println("url is +++++++++++++++++++++++" + ImageUrl);
											LOG.debug("+++++++++++++ 1111 Image URL:::::" + ImageUrl);

											if (!ImageUrl.startsWith("http://"))
											{
												ImageUrl = "http:" + ImageUrl;
												LOG.debug("1111.1 Image URL with http::::::::" + ImageUrl);

											}
											else if (!ImageUrl.startsWith("https://"))
											{

												ImageUrl = "https:" + ImageUrl;
												LOG.debug("1111.11  Image URL with https:::::" + ImageUrl);
											}


											/*
											 * try {
											 */

											// Sets the authenticator that will be used by the networking code
											// when a proxy or an HTTP server asks for authentication.

											/*
											 * Authenticator.setDefault(new Authenticator() {
											 *
											 * @Override public PasswordAuthentication getPasswordAuthentication() { final String
											 * username = "siteadmin"; final String password = "ASDF!@#$asdf1234";
											 * LOG.info("Authenticating Login......"); return new PasswordAuthentication(username,
											 * password.toCharArray());
											 *
											 * } });
											 */


											//	final URL url = new URL("https://assetssprint.tataunistore.com/medias/sys_master/images/8802948644894.png");
											/*
											 * final URL url = new URL(ImageUrl);
											 * 
											 * LOG.info("Trying to access url:::::" + url.toString() + "URI" + url.toURI());
											 * 
											 * final BufferedImage bimg = ImageIO.read(url.openStream()); final int width =
											 * bimg.getWidth(); final int height = bimg.getHeight();
											 * 
											 * final String size = String.valueOf(width) + " X " + String.valueOf(height);
											 * 
											 * automationMap.put("size", bigPromoBanner.getBannerImage().getSize().toString());
											 */
											automationMap.put("media_type", bigPromoBanner.getBannerImage().getMime());

											campaignDataSeqBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
											//campaignDataSeqBanner.setSize(size);
											//}
											/*
											 * catch (final MalformedURLException e) { LOG.error("Malformed URL: " +
											 * e.getMessage()); } catch (final IOException e) { LOG.error("IO Exception: " +
											 * e.getMessage()); }
											 */
										}
									}

									catch (final Exception e)
									{
										LOG.error(e.getMessage());
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
							final InternalCampaignReportData campaignDataBigPromoBanner = new InternalCampaignReportData();
							final MplBigPromoBannerComponentModel bigPromoBanner = (MplBigPromoBannerComponentModel) componentItr;

							String CategoryBigPromoBanner = findCategoryLink(bigPromoBanner.getMajorPromoText() + "|"
									+ bigPromoBanner.getMinorPromo1Text() + "|" + bigPromoBanner.getMinorPromo2Text());
							CategoryBigPromoBanner = CategoryBigPromoBanner.substring(CategoryBigPromoBanner.lastIndexOf("/") + 1,
									CategoryBigPromoBanner.length());

							automationMap.put("asset_name", componentItr.getName());
							automationMap.put("source_page", contentPageItr.getLabel());
							automationMap.put(
									"category_id",
									bigPromoBanner.getMajorPromoText() + "|" + bigPromoBanner.getMinorPromo1Text() + "|"
											+ bigPromoBanner.getMinorPromo2Text());

							campaignDataBigPromoBanner.setIcid(componentItr.getPk().toString());
							campaignDataBigPromoBanner.setSourcePage(contentPageItr.getLabel());
							campaignDataBigPromoBanner.setAssetName(componentItr.getName());
							campaignDataBigPromoBanner.setCategory(CategoryBigPromoBanner);

							try
							{

								if (null != bigPromoBanner.getBannerImage())
								{
									String ImageUrl = bigPromoBanner.getBannerImage().getURL();
									LOG.debug("+++++++++++++2222 +Image URL:::::" + ImageUrl);
									//System.out.println("url is +++++++++++++++++++++++" + ImageUrl);
									if (!ImageUrl.startsWith("http://"))
									{

										ImageUrl = "http:" + ImageUrl;
										LOG.debug("2222.2+++++++++++++Image URL:::::" + ImageUrl);

									}
									else if (!ImageUrl.startsWith("https://"))
									{
										ImageUrl = "https:" + ImageUrl;
										LOG.debug("2222.22+++++++++++++Image URL:::::" + ImageUrl);
									}

									try
									{

										// Sets the authenticator that will be used by the networking code
										// when a proxy or an HTTP server asks for authentication.
										Authenticator.setDefault(new CustomAuthenticator());



										//final URL url = new URL("https://assetssprint.tataunistore.com/medias/sys_master/images/8802948644894.png");

										/*
										 * final URL url = new URL(ImageUrl);
										 *
										 * final BufferedImage bimg = ImageIO.read(url.openStream());
										 *
										 * final int width = bimg.getWidth(); final int height = bimg.getHeight();
										 *
										 * final String size = String.valueOf(width) + " X " + String.valueOf(height);
										 */



										automationMap.put("media_type", bigPromoBanner.getBannerImage().getMime());
										automationMap.put("size", bigPromoBanner.getBannerImage().getInternalURL().toString());

										campaignDataBigPromoBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
										//campaignDataBigPromoBanner.setSize(size);


										CampaignDataList.add(campaignDataBigPromoBanner);
									}
									/*
									 * catch (final MalformedURLException e) { LOG.error("Malformed URL: " + e.getMessage()); }
									 * catch (final IOException e) { LOG.error("IO Exception: " + e.getMessage()); }
									 */
									catch (final Exception e)
									{
										LOG.error(e.getMessage());
									}
								}
							}
							catch (final Exception e)
							{
								LOG.error(e.getMessage());
							}
						}
						//3. Mpl BigFour PromoBanner ComponentModel
						if (null != componentItr && componentItr instanceof MplBigFourPromoBannerComponentModel)
						{
							final InternalCampaignReportData campaignDataBigFourPromoBanner = new InternalCampaignReportData();
							final MplBigFourPromoBannerComponentModel bigPromoBanner = (MplBigFourPromoBannerComponentModel) componentItr;

							String CategoryBigFourPromoBanner = findCategoryLink(bigPromoBanner.getPromoText1() + "|"
									+ bigPromoBanner.getPromoText2() + "|" + bigPromoBanner.getPromoText3() + ""
									+ bigPromoBanner.getPromoText4());

							CategoryBigFourPromoBanner = CategoryBigFourPromoBanner.substring(
									CategoryBigFourPromoBanner.lastIndexOf("/") + 1, CategoryBigFourPromoBanner.length());

							automationMap.put("asset_name", componentItr.getName());
							automationMap.put("source_page", contentPageItr.getLabel());
							automationMap.put("category_id", bigPromoBanner.getPromoText1() + "|" + bigPromoBanner.getPromoText2() + "|"
									+ bigPromoBanner.getPromoText3() + "" + bigPromoBanner.getPromoText4());

							campaignDataBigFourPromoBanner.setIcid(componentItr.getPk().toString());
							campaignDataBigFourPromoBanner.setAssetName(componentItr.getName());
							campaignDataBigFourPromoBanner.setSourcePage(contentPageItr.getLabel());
							campaignDataBigFourPromoBanner.setCategory(CategoryBigFourPromoBanner);
							try
							{

								if (null != bigPromoBanner.getBannerImage())
								{
									String ImageUrl = bigPromoBanner.getBannerImage().getURL();
									//System.out.println("url is +++++++++++++++++++++++" + ImageUrl);
									LOG.debug("++++++++ 3333 +++++Image URL:::::" + ImageUrl);

									if (!ImageUrl.startsWith("http://"))
									{
										ImageUrl = "http:" + ImageUrl;
										LOG.debug("++++ 3333.1+++++++++Image URL:::::" + ImageUrl);
									}
									else if (!ImageUrl.startsWith("https://"))
									{
										ImageUrl = "https:" + ImageUrl;
										LOG.debug("3333.2+++++++++++++Image URL:::::" + ImageUrl);

									}

									try
									{

										// Sets the authenticator that will be used by the networking code
										// when a proxy or an HTTP server asks for authentication.
										//CustomAuthenticator.getPasswordAuthentication();
										//final CustomAuthenticator customAuth = new CustomAuthenticator();
										//Authenticator.setDefault(new CustomAuthenticator());



										//final URL url = new URL("https://assetssprint.tataunistore.com/medias/sys_master/images/8802948644894.png");
										/*
										 * final URL url = new URL(ImageUrl);
										 * 
										 * 
										 * final BufferedImage bimg = ImageIO.read(url.openStream());
										 * 
										 * LOG.info("Connection Successful!!!!!!!"); final int width = bimg.getWidth(); final int
										 * height = bimg.getHeight();
										 * 
										 * final String size = String.valueOf(width) + " X " + String.valueOf(height);
										 */

										automationMap.put("media_type", bigPromoBanner.getBannerImage().getMime());
										automationMap.put("size", bigPromoBanner.getBannerImage().getSize().toString());

										campaignDataBigFourPromoBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
										//campaignDataBigFourPromoBanner.setSize(size);

									}
									/*
									 * catch (final MalformedURLException e) { LOG.error("Malformed URL: " + e.getMessage()); }
									 * catch (final IOException e) { LOG.error("IO Exception: " + e.getMessage()); }
									 */
									catch (final Exception e)
									{
										LOG.error(e.getMessage());
									}
								}

							}

							catch (final Exception e)
							{
								LOG.error(e.getMessage());
							}
							LOG.info("componentItr.getName() " + componentItr.getName());
						}
					}
					//LOG.info("banner componenets found " + contentPageItr.getco);

				}

				createCSVExcel(CampaignDataList);

			}
		}
		/*
		 * catch (final IOException e) { LOG.error(e); }
		 */
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
		//final String path = "D:\\Arunava\\tmp2\\Internal_Campaign2\\internalCampaign.csv";

		//final String path = configurationService.getConfiguration().getString("cronjob.internalcampaign.feed.path");

		try
		{
			//final File file = new File(path);
			final File file = new File(getOutputFilePath());
			file.getParentFile().mkdirs();
			//populateCSV(campaignDataConsolidatedList, path, file);
			populateCSV(campaignDataConsolidatedList, file);
		}

		catch (final Exception e)
		{
			// YTODO Auto-generated catch block
			LOG.info("Exception writing" + e.getMessage());
		}
		//` false;
	}

	public String getCategoryOnAnchorTag(final String categoryId)
	{
		final Pattern p = Pattern.compile("<a href='(.*?)'>");
		final Matcher m = p.matcher(categoryId);
		return m.group(1);
	}

	//public void populateCSV(final List<InternalCampaignReportData> campaignDataConsolidatedList, final String path, final File file)
	public void populateCSV(final List<InternalCampaignReportData> campaignDataConsolidatedList, final File file)
	{
		FileWriter fileWriter = null;
		String CSVHeader = "";

		try
		{
			fileWriter = new FileWriter(file, false);
			CSVHeader = getCSVHeaderLine();

			fileWriter.append(CSVHeader);
			fileWriter.append(NEW_LINE_SEPARATOR);


			//for (final Map.Entry<String, String> entry : exportMap.entrySet())
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

			/*
			 * final Iterator it = exportMap.entrySet().iterator(); while (it.hasNext()) { //
			 * fileWriter.append(report.getOrderNo()); fileWriter.append(exportMap.get("asset_name"));
			 * fileWriter.append(COMMA_DELIMITER); fileWriter.append(exportMap.get("source_page"));
			 * fileWriter.append(COMMA_DELIMITER); fileWriter.append(exportMap.get("category_id"));
			 * fileWriter.append(COMMA_DELIMITER); fileWriter.append(exportMap.get("media_type"));
			 * fileWriter.append(COMMA_DELIMITER); fileWriter.append(exportMap.get("si ze"));
			 *
			 * fileWriter.append(NEW_LINE_SEPARATOR);
			 * //System.out.println("value in map is--------------------------------------------------------------" +
			 * it.next()); //final FileWriter writer = new FileWriter(path, true); //writer.write(it.next().toString()); }
			 */
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

		//final DateFormat df = new SimpleDateFormat(MarketplacecommerceservicesConstants.DATE_FORMAT_REPORT);
		//final String timestamp = df.format(new Date());
		final StringBuilder output_file_path = new StringBuilder();
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.internalcampaign.feed.path", ""));
		output_file_path.append(File.separator);
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.internalcampaign.prefix", ""));
		output_file_path.append(MarketplacecommerceservicesConstants.FILE_PATH);
		//output_file_path.append(timestamp);
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.internalcampaign.extension", ""));

		return output_file_path.toString();
	}

	public static class CustomAuthenticator extends Authenticator
	{

		// Called when password authorization is needed
		@Override
		protected PasswordAuthentication getPasswordAuthentication()
		{
			//System.out.println("=======+++++++++===============================");
			LOG.debug("=======+++++++++===============================");

			// Get information about the request





			final String username = "siteadmin";
			final String password = "ASDF!@#$asdf1234";


			// Return the information (a data holder that is used by Authenticator)
			return new PasswordAuthentication(username, password.toCharArray());

		}

	}

}
