/**
 *
 */
package com.tisl.mpl.core.sitemap.jobs;


import de.hybris.platform.acceleratorservices.cronjob.SiteMapMediaJob;
import de.hybris.platform.acceleratorservices.enums.SiteMapPageEnum;
import de.hybris.platform.acceleratorservices.model.SiteMapConfigModel;
import de.hybris.platform.acceleratorservices.model.SiteMapLanguageCurrencyModel;
import de.hybris.platform.acceleratorservices.model.SiteMapMediaCronJobModel;
import de.hybris.platform.acceleratorservices.model.SiteMapPageModel;
import de.hybris.platform.acceleratorservices.sitemap.generator.SiteMapGenerator;
import de.hybris.platform.acceleratorservices.sitemap.generator.impl.CustomPageSiteMapGenerator;
import de.hybris.platform.acceleratorservices.sitemap.generator.impl.ProductPageSiteMapGenerator;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.SiteMapUpdateModeEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCategoryDao;
import com.tisl.mpl.marketplacecommerceservices.service.CustomMediaService;
import com.tisl.mpl.sitemap.generator.impl.MplCustomPageSiteMapGenerator;


/**
 * @author TCS
 *
 */
public class CustomSiteMapMediaJob extends SiteMapMediaJob
{
	private final static Logger LOG = Logger.getLogger(CustomSiteMapMediaJob.class.getName());

	private static final String SITE_MAP_MIME_TYPE = "text/plain";

	private CustomMediaService mediaService;
	private MplCategoryDao mplCategoryDao;
	private MplCustomPageSiteMapGenerator mplCustomPageSiteMapGenerator;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Autowired
	private CategoryService categoryService;

	/**
	 * This method performs the sitemap job for TPR-1285 Dynamic sitemap
	 *
	 * @param cronJob
	 * @return PerformResult
	 */
	@Override
	public PerformResult perform(final SiteMapMediaCronJobModel cronJob)
	{

		final List<MediaModel> siteMapMedias = new ArrayList<MediaModel>();
		final CMSSiteModel contentSite = cronJob.getContentSite();

		getCmsSiteService().setCurrentSite(contentSite);
		// set the catalog version for the current session
		getActivateBaseSiteInSession().activate(contentSite);

		final SiteMapConfigModel siteMapConfig = contentSite.getSiteMapConfig();
		final Collection<SiteMapPageModel> siteMapPages = siteMapConfig.getSiteMapPages();
		final SiteMapUpdateModeEnum updateType = cronJob.getUpdateType();
		for (final SiteMapPageModel siteMapPage : siteMapPages)
		{
			final List<File> siteMapFiles = new ArrayList<File>();
			final SiteMapPageEnum pageType = siteMapPage.getCode();
			final SiteMapGenerator generator = this.getGeneratorForSiteMapPage(pageType);

			if (BooleanUtils.isTrue(siteMapPage.getActive()) && generator != null)
			{
				//Logic for ProductPageSiteMapGenerator
				if (generator instanceof ProductPageSiteMapGenerator
						&& (updateType.equals(SiteMapUpdateModeEnum.PRODUCT) || updateType.equals(SiteMapUpdateModeEnum.ALL)))
				{
					//to do read from local properties
					final CategoryModel L0Category = categoryService.getCategoryForCode(contentSite.getDefaultCatalog()
							.getActiveCatalogVersion(), getHierarchy());

					final List<CategoryModel> L1Category = L0Category.getCategories();

					for (final CategoryModel category : L1Category)
					{
						final List<CategoryModel> L2Category = category.getCategories();
						for (final CategoryModel categoryl2 : L2Category)
						{
							String categoryName = category.getName();
							if (StringUtils.isNotEmpty(categoryl2.getName()))
							{
								categoryName = category.getName() + " " + categoryl2.getName();
							}

							final List models = getMplCategoryDao().getProductForL2code(
									contentSite.getDefaultCatalog().getActiveCatalogVersion(), categoryl2.getCode());
							if (CollectionUtils.isNotEmpty(models))
							{
								//Logic for splitting files based on model size
								final Integer MAX_SITEMAP_LIMIT = cronJob.getSiteMapUrlLimitPerFile();
								if (models.size() > MAX_SITEMAP_LIMIT.intValue())
								{
									final List<List> modelsList = splitUpTheListIfExceededLimit(models, MAX_SITEMAP_LIMIT);
									for (int modelIndex = 0; modelIndex < modelsList.size(); modelIndex++)
									{
										generateSiteMapFiles(siteMapFiles, contentSite, generator, siteMapConfig,
												modelsList.get(modelIndex), pageType, Integer.valueOf(modelIndex), categoryName);
									}
								}
								else
								{
									generateSiteMapFiles(siteMapFiles, contentSite, generator, siteMapConfig, models, pageType, null,
											categoryName);
								}
							}
						}
					}

				}
				//					final List<CategoryModel> categoryList = getMplCategoryDao().getLowestPrimaryCategories();
				//
				//					for (final CategoryModel categoryModel : categoryList)
				//					{
				//						final int count = 4;
				//						final CategoryModel l2Cat = findCategoryLevel(categoryModel, count);
				//						final CategoryModel l1Cat = findL2CategoryLevel(categoryModel, count);
				//						String categoryName = l2Cat.getName();
				//						if (StringUtils.isNotEmpty(l1Cat.getName()))
				//						{
				//							categoryName = l1Cat.getName() + " " + l2Cat.getName();
				//						}
				//						final List models = categoryModel.getProducts();
				//						if (CollectionUtils.isNotEmpty(models))
				//						{
				//							//Logic for splitting files based on model size
				//							final Integer MAX_SITEMAP_LIMIT = cronJob.getSiteMapUrlLimitPerFile();
				//							if (models.size() > MAX_SITEMAP_LIMIT.intValue())
				//							{
				//								final List<List> modelsList = splitUpTheListIfExceededLimit(models, MAX_SITEMAP_LIMIT);
				//								for (int modelIndex = 0; modelIndex < modelsList.size(); modelIndex++)
				//								{
				//									generateSiteMapFiles(siteMapFiles, contentSite, generator, siteMapConfig, modelsList.get(modelIndex),
				//											pageType, Integer.valueOf(modelIndex), categoryName);
				//								}
				//							}
				//							else
				//							{
				//								generateSiteMapFiles(siteMapFiles, contentSite, generator, siteMapConfig, models, pageType, null,
				//										categoryName);
				//							}
				//						}
				//					}
				//				}
				//Logic for MplCustomPageSiteMapGenerator
				else if (generator instanceof CustomPageSiteMapGenerator
						&& (updateType.equals(SiteMapUpdateModeEnum.CUSTOM) || updateType.equals(SiteMapUpdateModeEnum.ALL)))
				{
					//Logic for splitting files based on model size
					final List models = getMplCustomPageSiteMapGenerator().getCustomData(contentSite);
					final Integer MAX_SITEMAP_LIMIT = cronJob.getSiteMapUrlLimitPerFile();
					if (models.size() > MAX_SITEMAP_LIMIT.intValue())
					{
						final List<List> modelsList = splitUpTheListIfExceededLimit(models, MAX_SITEMAP_LIMIT);
						for (int modelIndex = 0; modelIndex < modelsList.size(); modelIndex++)
						{
							generateSiteMapFiles(siteMapFiles, contentSite, getMplCustomPageSiteMapGenerator(), siteMapConfig,
									modelsList.get(modelIndex), pageType, Integer.valueOf(modelIndex), null);
						}
					}
					else
					{
						generateSiteMapFiles(siteMapFiles, contentSite, getMplCustomPageSiteMapGenerator(), siteMapConfig, models,
								pageType, null, null);
					}
				}

			}
			else
			{
				LOG.warn(String.format("Skipping SiteMap page %s active %s", siteMapPage.getCode(), siteMapPage.getActive()));
			}
			if (!siteMapFiles.isEmpty())
			{
				for (final File siteMapFile : siteMapFiles)
				{
					siteMapMedias.add(createCatalogUnawareMediaModel(siteMapFile));
				}
			}
		}

		if (!siteMapMedias.isEmpty())
		{
			final Collection<MediaModel> existingSiteMaps = contentSite.getSiteMaps();

			contentSite.setSiteMaps(siteMapMedias);
			modelService.save(contentSite);

			// clean up old sitemap medias
			if (CollectionUtils.isNotEmpty(existingSiteMaps))
			{
				modelService.removeAll(existingSiteMaps);
			}
		}


		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * finding a category level corresponding to a category id
	 *
	 * @param categoryId
	 * @param count
	 * @return count
	 */
	private CategoryModel findCategoryLevel(final CategoryModel categoryId, int count)
	{

		final int finalCount = 2;
		CategoryModel cat = categoryId;
		try
		{
			if (count == 1)
			{
				return cat;
			}
			else
			{
				for (final CategoryModel superCategory : categoryId.getSupercategories())
				{
					count--;
					if (count == finalCount)
					{
						break;
					}
					else
					{
						cat = superCategory;
						return findCategoryLevel(superCategory, count);
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return cat;
	}

	private CategoryModel findL2CategoryLevel(final CategoryModel categoryId, int count)
	{

		final int finalCount = 1;
		CategoryModel cat = categoryId;
		try
		{
			if (count == 1)
			{
				return cat;
			}
			else
			{
				for (final CategoryModel superCategory : categoryId.getSupercategories())
				{
					count--;
					if (count == finalCount)
					{
						break;
					}
					else
					{
						cat = superCategory;
						return findL2CategoryLevel(superCategory, count);
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return cat;
	}



	/**
	 * This method creates CatalogUnawareMediaModel with the file generated for TPR-1285 Dynamic sitemap
	 *
	 * @param siteMapFile
	 * @return CatalogUnawareMediaModel
	 */
	@Override
	protected CatalogUnawareMediaModel createCatalogUnawareMediaModel(final File siteMapFile)
	{
		final CatalogUnawareMediaModel media = modelService.create(CatalogUnawareMediaModel.class);
		final MediaModel existingFile = getMediaService().getMediaByCode(siteMapFile.getName());
		if (null != existingFile)
		{
			modelService.remove(existingFile);
		}
		media.setCode(siteMapFile.getName());
		modelService.save(media);
		try
		{
			getMediaService().setStreamForMedia(media, new FileInputStream(siteMapFile), siteMapFile.getName(), SITE_MAP_MIME_TYPE);
		}
		catch (final FileNotFoundException e)
		{
			LOG.error(e.getMessage());
		}
		return media;
	}




	/**
	 * This method generates the sitemap xml for TPR-1285 Dynamic sitemap
	 *
	 * @param siteMapFiles
	 * @param contentSite
	 * @param generator
	 * @param siteMapConfig
	 * @param models
	 * @param pageType
	 * @param index
	 * @param fileIndex
	 */
	protected void generateSiteMapFiles(final List<File> siteMapFiles, final CMSSiteModel contentSite,
			final SiteMapGenerator generator, final SiteMapConfigModel siteMapConfig, final List<List> models,
			final SiteMapPageEnum pageType, final Integer index, final String fileIndex)
	{
		for (final SiteMapLanguageCurrencyModel siteMapLanguageCurrency : siteMapConfig.getSiteMapLanguageCurrencies())
		{
			try
			{
				if (pageType.equals(SiteMapPageEnum.PRODUCT))
				{
					siteMapFiles.add(generator.render(contentSite, siteMapLanguageCurrency.getCurrency(),
							siteMapLanguageCurrency.getLanguage(), siteMapConfig.getSiteMapTemplate(), models, fileIndex, index));
				}
				else
				{
					siteMapFiles.add(generator.render(contentSite, siteMapLanguageCurrency.getCurrency(),
							siteMapLanguageCurrency.getLanguage(), siteMapConfig.getSiteMapTemplate(), models, pageType.toString(),
							index));
				}

			}
			catch (final IOException e)
			{
				LOG.error(e.getMessage());
			}
		}
	}




	//Getters and setters

	@Override
	protected CustomMediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final CustomMediaService mediaService)
	{
		this.mediaService = mediaService;
	}


	/**
	 * @return the mplCategoryDao
	 */
	public MplCategoryDao getMplCategoryDao()
	{
		return mplCategoryDao;
	}

	/**
	 * @param mplCategoryDao
	 *           the mplCategoryDao to set
	 */
	public void setMplCategoryDao(final MplCategoryDao mplCategoryDao)
	{
		this.mplCategoryDao = mplCategoryDao;
	}



	/**
	 * @return the mplCustomPageSiteMapGenerator
	 */
	public MplCustomPageSiteMapGenerator getMplCustomPageSiteMapGenerator()
	{
		return mplCustomPageSiteMapGenerator;
	}



	/**
	 * @param mplCustomPageSiteMapGenerator
	 *           the mplCustomPageSiteMapGenerator to set
	 */
	public void setMplCustomPageSiteMapGenerator(final MplCustomPageSiteMapGenerator mplCustomPageSiteMapGenerator)
	{
		this.mplCustomPageSiteMapGenerator = mplCustomPageSiteMapGenerator;
	}



	public String getHierarchy()
	{
		final String query = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SITEMAP_HIERARCHY,
				MarketplacecommerceservicesConstants.SITEMAP_HIERARCHY_DEFAULT);

		return query;
	}


}