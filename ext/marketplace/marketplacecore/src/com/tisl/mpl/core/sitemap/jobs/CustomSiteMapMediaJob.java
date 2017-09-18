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
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.enums.SiteMapUpdateModeEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCategoryDao;
import com.tisl.mpl.marketplacecommerceservices.service.CustomMediaService;
import com.tisl.mpl.sitemap.generator.impl.MplBrandPageSiteMapGenerator;
import com.tisl.mpl.sitemap.generator.impl.MplCustomPageSiteMapGenerator;
import com.tisl.mpl.util.ExceptionUtil;


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
		try
		{
			final List<MediaModel> siteMapMedias = new ArrayList<MediaModel>();
			final CMSSiteModel contentSite = cronJob.getContentSite();
			getCmsSiteService().setCurrentSite(contentSite);
			// set the catalog version for the current session
			getActivateBaseSiteInSession().activate(contentSite);
			final SiteMapConfigModel siteMapConfig = contentSite.getSiteMapConfig();
			final Collection<SiteMapPageModel> siteMapPages = siteMapConfig.getSiteMapPages();
			final SiteMapUpdateModeEnum updateType = cronJob.getUpdateType();
			final List<String> brandLists = new ArrayList<String>();//PRDI-423
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
										//PRDI-423

										if (useBrandFilter())
										{
											LOG.debug("Inside useBrandFilter");
											brandLists.addAll(fetchBrand(category.getCode(), categoryl2.getCode()));
										}
										else
										{
											LOG.debug("Not using BrandFilter");
										}

										//PRDI-423
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
						//PRDI-423
						//Logic For Brand Filter Sitemap
						if (CollectionUtils.isNotEmpty(brandLists))
						{
							List modelsFinal = null;//PRDI-423
							LOG.debug("Inside not empty brandlist*******");
							final List models = getMplbrandPageSiteMapGenerator().getBrandData(contentSite, brandLists);
							if (CollectionUtils.isNotEmpty(models))
							{
								LOG.debug("Adding Models*******");
								modelsFinal = new ArrayList();
								modelsFinal.add(models);
								if (CollectionUtils.isNotEmpty(modelsFinal))
								{
									final List<List> modelUltimate = new ArrayList();
									LOG.debug("Inside not empty modelsFinal*******");
									for (int modelIndex = 0; modelIndex < modelsFinal.size(); modelIndex++)
									{
										LOG.debug("Inside for loop of modelsFinal*******");
										final List<List> mod = (List<List>) modelsFinal.get(modelIndex);
										modelUltimate.addAll(mod);
									}
									//Logic for splitting files based on model size
									final Integer MAX_SITEMAP_LIMIT = cronJob.getSiteMapUrlLimitPerFile();
									if (modelUltimate.size() > MAX_SITEMAP_LIMIT.intValue())
									{
										final List<List> modelsList = splitUpTheListIfExceededLimit(modelUltimate, MAX_SITEMAP_LIMIT);
										for (int modelIndex = 0; modelIndex < modelsList.size(); modelIndex++)
										{
											LOG.debug("FOR size greater than 1000*******");
											generateSiteMapFiles(siteMapFiles, contentSite, getMplbrandPageSiteMapGenerator(),
													siteMapConfig, modelsList.get(modelIndex), SiteMapPageEnum.CATEGORY,
													Integer.valueOf(modelIndex), null);
										}
									}
									else
									{
										LOG.debug("FOR size less than 1000*******");
										generateSiteMapFiles(siteMapFiles, contentSite, getMplbrandPageSiteMapGenerator(), siteMapConfig,
												modelUltimate, SiteMapPageEnum.CATEGORY, null, null);
									}

								}
								else
								{
									LOG.debug(" Final models are empty");
								}
							}
							else
							{
								LOG.debug("models are empty");
							}
						}
						else
						{
							LOG.debug("brand filter model is empty");
						}
						//PRDI-423
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
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			LOG.error("EtailNonBusinessException Occured", exception);
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
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
					LOG.debug("**Inside SiteMapPageEnum Product**");
					siteMapFiles.add(generator.render(contentSite, siteMapLanguageCurrency.getCurrency(),
							siteMapLanguageCurrency.getLanguage(), siteMapConfig.getSiteMapTemplate(), models, fileIndex, index));
				}
				//PRDI-423
				else if (pageType.equals(SiteMapPageEnum.CATEGORY))
				{
					LOG.debug("**Inside SiteMapPageEnum Category**");
					siteMapFiles.add(generator.render(contentSite, siteMapLanguageCurrency.getCurrency(),
							siteMapLanguageCurrency.getLanguage(), siteMapConfig.getSiteMapTemplate(), models, fileIndex, index));
				}
				//PRDI-423

				else
				{
					LOG.debug("**Inside SiteMapPageEnum else**");
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


	//PRDI-423
	protected boolean useBrandFilter()
	{
		return configurationService.getConfiguration().getBoolean(MarketplacecommerceservicesConstants.SITEMAP_BRANDFILTER, true);
	}

	//PRDI-423

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

	//PRDI-423
	protected List<String> fetchBrand(final String categoryl1, final String categoryl2)
	{
		List<MplbrandfilterModel> brandFilterList = null;
		final List<String> brandfilterurl = new ArrayList<String>();
		if (StringUtils.isNotEmpty(categoryl1) && StringUtils.isNotEmpty(categoryl2))
		{
			brandFilterList = getMplCategoryDao().fetchBrandFilterforL1L2(categoryl1, categoryl2);

			if (CollectionUtils.isNotEmpty(brandFilterList))
			{
				for (final MplbrandfilterModel brandFilter : brandFilterList)
				{
					//As per nausheer's comment
					String beginningBrandUrl2 = null;
					String beginningBrandUrl3 = null;
					String finalFilterUrl2 = null;
					String finalFilterUrl3 = null;
					final int indexOfUrl2 = brandFilter.getUrl2().indexOf(MarketplaceCoreConstants.BRAND_TAG);
					if (indexOfUrl2 > MarketplaceCoreConstants.ZERO_INT)
					{
						beginningBrandUrl2 = brandFilter.getUrl2().substring(MarketplaceCoreConstants.ZERO_INT, indexOfUrl2)
								.replaceAll(MarketplaceCoreConstants.APOSTROPHE, MarketplaceCoreConstants.EMPTY)
								.replaceAll(MarketplaceCoreConstants.REGEX, MarketplaceCoreConstants.SINGLE_HYPHEN);
						beginningBrandUrl2 = beginningBrandUrl2.replaceAll(MarketplaceCoreConstants.WHOLE_WORD_REGEX,
								MarketplaceCoreConstants.SINGLE_HYPHEN);
						final String endBrandUrl2 = brandFilter.getUrl2().substring(indexOfUrl2, brandFilter.getUrl2().length());
						final String finalBrandUrl2 = beginningBrandUrl2 + MarketplaceCoreConstants.SINGLE_HYPHEN + endBrandUrl2;
						if (finalBrandUrl2.contains(MarketplaceCoreConstants.BRAND_TAG))
						{
							finalFilterUrl2 = finalBrandUrl2.replace(MarketplaceCoreConstants.BRAND_TAG, MarketplaceCoreConstants.EMPTY);
						}
					}
					finalFilterUrl2 = finalFilterUrl2.replaceAll(MarketplaceCoreConstants.HYPHEN_REGEX,
							MarketplaceCoreConstants.SINGLE_HYPHEN);
					final int indexOfUrl3 = brandFilter.getUrl3().indexOf(MarketplaceCoreConstants.BRAND_TAG);
					if (indexOfUrl3 > MarketplaceCoreConstants.ZERO_INT)
					{
						beginningBrandUrl3 = brandFilter.getUrl3().substring(MarketplaceCoreConstants.ZERO_INT, indexOfUrl3)
								.replaceAll(MarketplaceCoreConstants.APOSTROPHE, MarketplaceCoreConstants.EMPTY)
								.replaceAll(MarketplaceCoreConstants.REGEX, MarketplaceCoreConstants.SINGLE_HYPHEN);
						beginningBrandUrl3 = beginningBrandUrl3.replaceAll(MarketplaceCoreConstants.WHOLE_WORD_REGEX,
								MarketplaceCoreConstants.SINGLE_HYPHEN);
						final String endBrandUrl3 = brandFilter.getUrl3().substring(indexOfUrl3, brandFilter.getUrl3().length());
						final String finalBrandUrl3 = beginningBrandUrl3 + MarketplaceCoreConstants.SINGLE_HYPHEN + endBrandUrl3;
						if (finalBrandUrl3.contains(MarketplaceCoreConstants.BRAND_TAG))
						{
							finalFilterUrl3 = finalBrandUrl3.replace(MarketplaceCoreConstants.BRAND_TAG, MarketplaceCoreConstants.EMPTY);
						}
					}
					finalFilterUrl3 = finalFilterUrl3.replaceAll(MarketplaceCoreConstants.HYPHEN_REGEX,
							MarketplaceCoreConstants.SINGLE_HYPHEN);
					//As per nausheer's comment
					if (!isCheck(finalFilterUrl2))
					{
						brandfilterurl.add(finalFilterUrl2);
					}
					if (!isCheck(finalFilterUrl3))
					{
						brandfilterurl.add(finalFilterUrl3);
					}
					//brandfilterurl.add(brandFilter.getUrl1());
					//As per nausheer's comment
				}
				final Set<String> brandfilterurlset = new HashSet<String>(brandfilterurl);
				brandfilterurl.clear();
				brandfilterurl.addAll(brandfilterurlset);
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

	public boolean isCheck(final String url)
	{
		return url.contains(MarketplaceCoreConstants.AMPERSAND);
	}
	//PRDI-423
}