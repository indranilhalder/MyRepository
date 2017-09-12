/**
 *
 */
package com.tisl.mpl.sitemap.generator.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class SiteMapZipperJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SiteMapZipperJob.class.getName());

	List<File> fileList = new ArrayList<>();

	@Autowired
	private MplCmsPageService cmsPageService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		LOG.info("Entering SiteMap Zipper Job.");
		//final MplConfigurationModel configModel = getFetchSalesOrderService().getCronDetails(cronJob.getCode());


		try
		{
			final String siteMapFileName = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.SITEMAP_NAME, MarketplacecommerceservicesConstants.SITEMAP_NAME_DEFAULT);
			final String siteMapFileLocation = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.SITEMAP_LOCATION,
					MarketplacecommerceservicesConstants.SITEMAP_LOCATION_DEFAULT);
			final String customFileLocation = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.SITEMAP_FILE_LOCATION_CUSTOM);
			final String productFileLocation = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.SITEMAP_FILE_LOCATION_PRODUCT);
			//PRDI-423
			final String brandFileLocation = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.SITEMAP_FILE_LOCATION_BRAND);
			final String zipFileLocation = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.SITEMAP_ZIP_LOCATION,
					MarketplacecommerceservicesConstants.SITEMAP_ZIP_LOCATION_DEFAULT);
			createXML(siteMapFileName, siteMapFileLocation);
			generateFileList(new File(customFileLocation));
			generateFileList(new File(productFileLocation));
			//PRDI-423
			generateFileList(new File(brandFileLocation));
			if (zipFileLocation.equalsIgnoreCase("/"))
			{
				LOG.debug("Cannot Clean Directory Starting with root");
			}
			else
			{
				FileUtils.cleanDirectory(new File(zipFileLocation));

				zipIt(zipFileLocation);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected void createXML(final String siteMapFileName, final String siteMapFileLocation)
	{
		LOG.debug("Inside CreateXML");
		final String siteUid = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SITEMAP_UID,
				MarketplacecommerceservicesConstants.SITEMAP_UID_DEFAULT);
		final String mediaUrlForSite = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SITEMAP_URL, MarketplacecommerceservicesConstants.SITEMAP_URL_DEFAULT);
		final String siteMapsFolderName = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SITEMAP_FOLDER, MarketplacecommerceservicesConstants.SITEMAP_FOLDER_DEFAULT);
		CMSSiteModel currentSite = null;
		try
		{
			currentSite = cmsPageService.getSiteforId(siteUid);
		}
		catch (final CMSItemNotFoundException e1)
		{
			LOG.debug("CmsSite Not Found");
		}

		if (currentSite != null)
		{
			final Collection<MediaModel> siteMaps = currentSite.getSiteMaps();

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(siteMapFileLocation + siteMapFileName)))
			{
				bw.write(MarketplacecommerceservicesConstants.XML_SITEMAP_HEADER);
				//bw.append('');

				for (final MediaModel siteMap : siteMaps)
				{

					bw.write(MarketplacecommerceservicesConstants.SITEMAP_TAG_OPEN);
					bw.write(MarketplacecommerceservicesConstants.LOC_TAG_OPEN);
					bw.write(mediaUrlForSite + siteMapsFolderName + siteMap.getCode()
							+ MarketplacecommerceservicesConstants.ZIP_FORMAT);
					bw.write(MarketplacecommerceservicesConstants.LOC_TAG_CLOSE);
					bw.write(MarketplacecommerceservicesConstants.SITEMAP_TAG_CLOSE);

				}
				bw.write(MarketplacecommerceservicesConstants.XML_SITEMAP_END);
			}

			catch (final IOException e)
			{

				LOG.debug("Error While Zipping");
			}
		}

	}

	public void zipIt(final String zipFile)
	{
		LOG.debug("Inside zipIt");
		final byte[] buffer = new byte[1024];

		try
		{
			for (final File file : this.fileList)
			{
				final FileOutputStream fos = new FileOutputStream(zipFile + file.getName()
						+ MarketplacecommerceservicesConstants.ZIP_FORMAT);
				final GZIPOutputStream zos = new GZIPOutputStream(fos);

				final FileInputStream in = new FileInputStream(file.getParent() + File.separator + file.getName());

				int len;
				while ((len = in.read(buffer)) > 0)
				{
					zos.write(buffer, 0, len);
				}

				in.close();
				zos.finish();
				zos.close();
			}

		}
		catch (final IOException ex)
		{
			LOG.debug("Error While Zipping");
		}
	}

	public void generateFileList(final File node)
	{
		LOG.debug("Inside generateFileList");

		if (node.isFile())
		{

			fileList.add(node);
		}

		if (node.isDirectory())
		{
			final String[] subNote = node.list();
			for (final String filename : subNote)
			{
				generateFileList(new File(node, filename));
			}
		}

	}

}
