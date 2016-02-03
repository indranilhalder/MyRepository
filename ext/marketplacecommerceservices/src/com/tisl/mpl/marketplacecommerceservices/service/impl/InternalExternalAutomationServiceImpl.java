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

	Logger LOG = Logger.getLogger(this.getClass());
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
	//public Map<String, String> automationGetAllBanner()
	//public void automationGetAllBanner()
	public List<InternalCampaignReportData> automationGetAllBanner()
	{
		final CatalogVersionModel catalogmodel = catalogversionservice.getCatalogVersion(configurationService.getConfiguration()
				.getString("internal.campaign.catelog"),
				configurationService.getConfiguration().getString("internal.campaign.catalogVersionName"));


		final Collection<CatalogVersionModel> catalogmodelCollection = new ArrayList<CatalogVersionModel>();
		catalogmodelCollection.add(catalogmodel);
		//final Collection<ContentPageModel> contentPages = cmsPageService.getAllContentPages(catalogmodel);

		//final Collection<ContentPageModel> contentPages = cmsPageService.getAllContentPages(catalogmodelCollection);
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
									if (null != bigPromoBanner.getBannerImage())
									{
										automationMap.put("media_type", bigPromoBanner.getBannerImage().getMime());
										automationMap.put("size", bigPromoBanner.getBannerImage().getSize().toString());

										campaignDataSeqBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
										campaignDataSeqBanner.setSize(bigPromoBanner.getBannerImage().getSize().toString());
										CampaignDataList.add(campaignDataSeqBanner);

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

							if (null != bigPromoBanner.getBannerImage())
							{
								automationMap.put("media_type", bigPromoBanner.getBannerImage().getMime());
								automationMap.put("size", bigPromoBanner.getBannerImage().getSize().toString());

								campaignDataBigPromoBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
								campaignDataBigPromoBanner.setSize(bigPromoBanner.getBannerImage().getSize().toString());

								CampaignDataList.add(campaignDataBigPromoBanner);
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
							if (null != bigPromoBanner.getBannerImage())
							{
								automationMap.put("media_type", bigPromoBanner.getBannerImage().getMime());
								automationMap.put("size", bigPromoBanner.getBannerImage().getSize().toString());

								campaignDataBigFourPromoBanner.setMediaType(bigPromoBanner.getBannerImage().getMime());
								campaignDataBigFourPromoBanner.setSize(bigPromoBanner.getBannerImage().getSize().toString());
								CampaignDataList.add(campaignDataBigFourPromoBanner);
							}
						}
						LOG.info("componentItr.getName() " + componentItr.getName());
					}
				}
				//LOG.info("banner componenets found " + contentPageItr.getco);

			}

			createCSVExcel(CampaignDataList);

		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return CampaignDataList;
	}

	/*
	 * All banner components are fed to create excel or csv
	 */
	@Override
	public void createCSVExcel(final List<InternalCampaignReportData> campaignDataConsolidatedList)
	{
		//	final String path = "D:\\Arunava\\tmp2\\Internal_Campaign2\\internalCampaign.csv";

		final String path = configurationService.getConfiguration().getString("cronjob.internalcampaign.feed.path");

		try
		{
			final File file = new File(path);
			file.getParentFile().mkdirs();
			populateCSV(campaignDataConsolidatedList, path, file);

			//final Map.Entry<String, String> pair = (Map.Entry) it.next();
			/*
			 * System.out.println("#########################################" + pair.getValue() +
			 */
			//writer.write(pair.getValue());
			//writer.close();
		}

		catch (final Exception e)
		{
			// YTODO Auto-generated catch block
			LOG.info("Exception writing" + e);
		}
		//` false;
	}

	public String getCategoryOnAnchorTag(final String categoryId)
	{
		//String html = "foo <a href='link1'>bar</a> baz <a href='link2'>qux</a> foo";
		final Pattern p = Pattern.compile("<a href='(.*?)'>");
		final Matcher m = p.matcher(categoryId);
		/*
		 * while(m.find()) { System.out.println(m.group(0));
		 */
		//System.out.println(m.group(1));
		return m.group(1);
		//}


	}

	public void populateCSV(final List<InternalCampaignReportData> campaignDataConsolidatedList, final String path, final File file)
	{
		FileWriter fileWriter = null;
		String CSVHeader = "";

		try
		{
			fileWriter = new FileWriter(file, false);
			CSVHeader = getCSVHeaderLine();

			fileWriter.append(CSVHeader);
			fileWriter.append(NEW_LINE_SEPARATOR);

			//final ArrayList tempList = null;

			//for (final Map.Entry<String, String> entry : exportMap.entrySet())
			for (final InternalCampaignReportData internalCampaignData : campaignDataConsolidatedList)
			{
				//if (entry.getKey().equals("asset_name"))
				if (internalCampaignData.getAssetName() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getAssetName()).append(COMMA_DELIMITER);
					//	System.out.println("###########" + entry.getValue() + "#########");
				}



				//if (entry.getKey().equals("category_id"))
				//if ("category_id".equalsIgnoreCase(entry.getKey()))
				//{
				if (internalCampaignData.getCategory() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append("\"").append(internalCampaignData.getCategory()).append("\"").append(COMMA_DELIMITER);
				}
				//fileWriter.append(COMMA_DELIMITER);
				//System.out.println("###########" + entry.getValue() + "#########");

				//}

				//if (entry.getKey().equals("media_type"))
				//{
				if (internalCampaignData.getMediaType() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getMediaType()).append(COMMA_DELIMITER);
				}
				//fileWriter.append(COMMA_DELIMITER);
				//System.out.println("###########" + entry.getValue() + "#########");

				//}

				//if (entry.getKey().equals("size"))
				//{
				if (internalCampaignData.getSize() == null)
				{
					fileWriter.append("").append(COMMA_DELIMITER);
				}
				else
				{
					fileWriter.append(internalCampaignData.getSize()).append(COMMA_DELIMITER);
				}
				//fileWriter.append(COMMA_DELIMITER);
				//System.out.println("###########" + entry.getValue() + "#########");

				//}


				//if (entry.getKey().equals("source_page"))
				//{
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

				//fileWriter.append(COMMA_DELIMITER);
				//System.out.println("###########" + entry.getValue() + "#########");
				//}

				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			/*
			 * final Iterator it = exportMap.entrySet().iterator(); while (it.hasNext()) { //
			 * fileWriter.append(report.getOrderNo()); fileWriter.append(exportMap.get("asset_name"));
			 * fileWriter.append(COMMA_DELIMITER); fileWriter.append(exportMap.get("source_page"));
			 * fileWriter.append(COMMA_DELIMITER); fileWriter.append(exportMap.get("category_id"));
			 * fileWriter.append(COMMA_DELIMITER); fileWriter.append(exportMap.get("media_type"));
			 * fileWriter.append(COMMA_DELIMITER); fileWriter.append(exportMap.get("size"));
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
		//System.out.println(catId.indexOf("href='"));
		final int ind1 = categoryId.indexOf("href='");
		//System.out.println("ind1 : " + ind1);
		if (ind1 != -1)
		{
			final int ind2 = categoryId.indexOf("'", ind1 + 6);
			//System.out.println("ind2 : " + ind2);
			finalCategoryId = categoryId.substring(ind1 + 6, ind2);
			//System.out.println("Final : " + finalString);
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

}
