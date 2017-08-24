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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.MplbrandfilterModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.SiteMapUpdateModeEnum;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCategoryDao;
import com.tisl.mpl.marketplacecommerceservices.service.CustomMediaService;
import com.tisl.mpl.sitemap.generator.impl.MplBrandPageSiteMapGenerator;
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

	private MplBrandPageSiteMapGenerator mplbrandPageSiteMapGenerator;

	/**
	 * @return the mplbrandPageSiteMapGenerator
	 */
	public MplBrandPageSiteMapGenerator getMplbrandPageSiteMapGenerator()
	{
		return mplbrandPageSiteMapGenerator;
	}

	/**
	 * @param mplbrandPageSiteMapGenerator
	 *           the mplbrandPageSiteMapGenerator to set
	 */
	public void setMplbrandPageSiteMapGenerator(final MplBrandPageSiteMapGenerator mplbrandPageSiteMapGenerator)
	{
		this.mplbrandPageSiteMapGenerator = mplbrandPageSiteMapGenerator;
	}

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
					final CatalogVersionModel activeCatalog = contentSite.getDefaultCatalog().getActiveCatalogVersion();
					final List<CategoryModel> L1Category = fetchL1fromL0(activeCatalog);

					if (CollectionUtils.isNotEmpty(L1Category))
					{
						for (final CategoryModel category : L1Category)
						{
							final List<CategoryModel> L2Category = fetchL2fromL1(category);
							if (CollectionUtils.isNotEmpty(L2Category))
							{
								for (final CategoryModel categoryl2 : L2Category)
								{
									final String categoryName = getSiteMapNamefromCategories(category, categoryl2);

									//fetchBrand(category.getCode(), categoryl2.getCode());

									final List models = fetchProductforL2code(activeCatalog, categoryl2);
									if (CollectionUtils.isNotEmpty(models) && StringUtils.isNotEmpty(categoryName))
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
											generateSiteMapFiles(siteMapFiles, contentSite, generator, siteMapConfig, models, pageType,
													null, categoryName);
										}
									}
									else
									{
										LOG.debug("Product Model or Category Not available");
									}
								}
							}
							else
							{
								LOG.debug("L2 Category Not available");
							}
						}
					}
					else
					{
						LOG.debug("L1 Category Not available");
					}

					LOG.debug("**START**BRAND***");

					final List modelsFinal = new ArrayList();
					if (CollectionUtils.isNotEmpty(L1Category))
					{
						LOG.debug("100...");
						for (final CategoryModel category : L1Category)
						{
							LOG.debug("200...");
							final List<CategoryModel> L2Category = fetchL2fromL1(category);
							if (CollectionUtils.isNotEmpty(L2Category))
							{
								LOG.debug("300...");
								for (final CategoryModel categoryl2 : L2Category)
								{
									//Logic For Brand Filter Sitemap
									final List brandLists = fetchBrand(category.getCode(), categoryl2.getCode());
									if (CollectionUtils.isNotEmpty(brandLists))
									{
										LOG.debug("1*******");
										final List models = getMplbrandPageSiteMapGenerator().getBrandData(contentSite, category,
												categoryl2, brandLists);
										modelsFinal.add(models);

										LOG.debug("2*******");
										//final String categoryName = getSiteMapNamefromCategories(category, categoryl2);

										//										if (CollectionUtils.isNotEmpty(models))
										//										{
										//											//Logic for splitting files based on model size
										//											final Integer MAX_SITEMAP_LIMIT = cronJob.getSiteMapUrlLimitPerFile();
										//											if (models.size() > MAX_SITEMAP_LIMIT.intValue())
										//											{
										//												final List<List> modelsList = splitUpTheListIfExceededLimit(models, MAX_SITEMAP_LIMIT);
										//												for (int modelIndex = 0; modelIndex < modelsList.size(); modelIndex++)
										//												{
										//													LOG.debug("3*******");
										//													generateSiteMapFiles(siteMapFiles, contentSite, getMplbrandPageSiteMapGenerator(),
										//															siteMapConfig, modelsList.get(modelIndex), SiteMapPageEnum.CATEGORY,
										//															Integer.valueOf(modelIndex), categoryName);
										//												}
										//											}
										//											else
										//											{
										//												LOG.debug("4*******");
										//												generateSiteMapFiles(siteMapFiles, contentSite, getMplbrandPageSiteMapGenerator(),
										//														siteMapConfig, models, SiteMapPageEnum.CATEGORY, null, categoryName);
										//											}
										//
										//
										//										}
										//										else
										//										{
										//											LOG.debug("models are empty");
										//										}
									}
									else
									{
										LOG.debug("brand filter model is empty");
									}

								}
							}
						}
						if (CollectionUtils.isNotEmpty(modelsFinal))
						{
							//Logic for splitting files based on model size
							final Integer MAX_SITEMAP_LIMIT = cronJob.getSiteMapUrlLimitPerFile();
							if (modelsFinal.size() > MAX_SITEMAP_LIMIT.intValue())
							{
								final List<List> modelsList = splitUpTheListIfExceededLimit(modelsFinal, MAX_SITEMAP_LIMIT);
								for (int modelIndex = 0; modelIndex < modelsList.size(); modelIndex++)
								{
									LOG.debug("3*******");
									generateSiteMapFiles(siteMapFiles, contentSite, getMplbrandPageSiteMapGenerator(), siteMapConfig,
											modelsList.get(modelIndex), SiteMapPageEnum.CATEGORY, Integer.valueOf(modelIndex), null);
								}
							}
							else
							{
								LOG.debug("4*******");
								generateSiteMapFiles(siteMapFiles, contentSite, getMplbrandPageSiteMapGenerator(), siteMapConfig,
										modelsFinal, SiteMapPageEnum.CATEGORY, null, null);
							}


						}
						else
						{
							LOG.debug("models are empty");
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
					System.out.println("**Inside pageType Product**");

					siteMapFiles.add(generator.render(contentSite, siteMapLanguageCurrency.getCurrency(),
							siteMapLanguageCurrency.getLanguage(), siteMapConfig.getSiteMapTemplate(), models, fileIndex, index));
				}
				else if (pageType.equals(SiteMapPageEnum.CATEGORY))
				{
					System.out.println("**Inside pageType Category**");
					siteMapFiles.add(generator.render(contentSite, siteMapLanguageCurrency.getCurrency(),
							siteMapLanguageCurrency.getLanguage(), siteMapConfig.getSiteMapTemplate(), models, fileIndex, index));
				}

				else
				{
					System.out.println("**Inside pageType else**");
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



	protected String getHierarchy()
	{
		final String query = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SITEMAP_HIERARCHY,
				MarketplacecommerceservicesConstants.SITEMAP_HIERARCHY_DEFAULT);

		return query;
	}

	protected List<CategoryModel> fetchL1fromL0(final CatalogVersionModel catalog)
	{
		final CategoryModel L0Category = categoryService.getCategoryForCode(catalog, getHierarchy());

		List<CategoryModel> L1Category = null;
		if (L0Category != null && L0Category.getCategories() != null)
		{
			L1Category = L0Category.getCategories();

		}

		return L1Category;
	}

	protected List<CategoryModel> fetchL2fromL1(final CategoryModel l1Category)
	{
		List<CategoryModel> L2Category = null;
		if (l1Category != null)
		{
			L2Category = l1Category.getCategories();
		}

		return L2Category;
	}

	protected List<ProductModel> fetchProductforL2code(final CatalogVersionModel catalog, final CategoryModel categoryl2)
	{
		List<ProductModel> productList = null;

		if (catalog != null && categoryl2 != null && StringUtils.isNotEmpty(categoryl2.getCode()))
		{
			productList = getMplCategoryDao().getProductForL2code(catalog, categoryl2.getCode());
		}
		return productList;

	}

	protected String getSiteMapNamefromCategories(final CategoryModel level1Category, final CategoryModel level2Category)
	{
		String categoryName = "";
		if (level1Category != null && level2Category != null)
		{
			if (StringUtils.isNotEmpty(level1Category.getName()) && StringUtils.isNotEmpty(level2Category.getName()))
			{
				categoryName = level1Category.getName() + " " + level2Category.getName();
			}
			else if (StringUtils.isNotEmpty(level1Category.getName()))
			{
				categoryName = level1Category.getName();
			}
		}
		return categoryName;
	}

	protected List<String> fetchBrand(final String categoryl1, final String categoryl2)
	{
		List<MplbrandfilterModel> brandFilterList = null;
		final List<String> brandfilterurl = new ArrayList<>();
		//		final FileWriter fw = new FileWriter("/brandOriginal.txt");

		if (StringUtils.isNotEmpty(categoryl1) && StringUtils.isNotEmpty(categoryl2))
		{
			brandFilterList = getMplCategoryDao().fetchBrandFilterforL1L2(categoryl1, categoryl2);

			if (CollectionUtils.isNotEmpty(brandFilterList))
			{
				for (final MplbrandfilterModel brandFilter : brandFilterList)
				{
					brandfilterurl.add(brandFilter.getUrl1());
					brandfilterurl.add(brandFilter.getUrl2());
					brandfilterurl.add(brandFilter.getUrl3());
					//					LOG.debug("111" + brandFilter.getUrl1());
					//					LOG.debug("22222" + brandFilter.getUrl2());
					//					LOG.debug("333333333" + brandFilter.getUrl3());

					//					fw.write(brandFilter.getUrl1() + "\n");
					//					fw.write(brandFilter.getUrl2() + "\n");
					//					fw.write(brandFilter.getUrl3() + "\n");
				}

				//				fw.close();
				//				for (int i = 0; i < 2; i++)
				//				{
				//					brandfilterurl.add("URL1-" + i + 1);
				//					brandfilterurl.add("URL2-" + i + 1);
				//					brandfilterurl.add("URL3-" + i + 1);
				//				}
				final Set<String> set = new HashSet<String>(brandfilterurl);

				for (final String myset : set)
				{
					LOG.debug("******brandfilterurl in cronjob*******" + myset);
				}
			}
			else
			{
				LOG.debug("******Empty Brandlist*******");
			}
		}
		else
		{
			LOG.debug("*****Empty l1categorycode or l2categorycode ********");
		}

		return brandfilterurl;

	}
}