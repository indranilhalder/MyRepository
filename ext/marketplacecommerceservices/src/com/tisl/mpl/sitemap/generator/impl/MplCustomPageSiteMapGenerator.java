/**
 *
 */
package com.tisl.mpl.sitemap.generator.impl;

import de.hybris.platform.acceleratorservices.enums.SiteMapPageEnum;
import de.hybris.platform.acceleratorservices.model.SiteMapPageModel;
import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.acceleratorservices.sitemap.generator.impl.AbstractSiteMapGenerator;
import de.hybris.platform.acceleratorservices.sitemap.renderer.SiteMapContext;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.commerceservices.url.impl.DefaultCategoryModelUrlResolver;
import de.hybris.platform.commerceservices.url.impl.DefaultContentPageUrlResolver;
import de.hybris.platform.commerceservices.url.impl.DefaultHomepageContentPageUrlResolver;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.CustomPageData;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class MplCustomPageSiteMapGenerator extends AbstractSiteMapGenerator<CustomPageData>
{


	private final static Logger LOG = Logger.getLogger(MplCustomPageSiteMapGenerator.class.getName());

	private CMSPageService cmsPageService;
	@Autowired
	private DefaultHomepageContentPageUrlResolver homepageUrlResolver;
	@Autowired
	private DefaultCategoryModelUrlResolver categoryModelUrlResolver;
	@Autowired
	private DefaultContentPageUrlResolver contentPageUrlResolver;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	private ApplicationContext applicationContext;
	@Autowired
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;



	/**
	 * This method returns custom data for all categry, category landing, custom and home pages for TPR-1285 Dynamic
	 * sitemap
	 *
	 * @param siteModel
	 * @return List<CustomPageData>
	 */
	public List<CustomPageData> getCustomData(final CMSSiteModel siteModel)
	{
		List<CustomPageData> mainSiteMapUrlList = new ArrayList<CustomPageData>();
		final Collection<SiteMapPageModel> siteMapPages = siteModel.getSiteMapConfig().getSiteMapPages();
		for (final SiteMapPageModel siteMapPage : siteMapPages)
		{
			if (siteMapPage.getCode().equals(SiteMapPageEnum.HOMEPAGE))
			{
				//Homepage
				mainSiteMapUrlList = homePageSiteMap(mainSiteMapUrlList, siteMapPage);
			}
			else if (siteMapPage.getCode().equals(SiteMapPageEnum.CATEGORY))
			{
				//CategoryPage
				mainSiteMapUrlList = categoryPageSiteMap(siteModel, mainSiteMapUrlList, siteMapPage);
			}
			else if (siteMapPage.getCode().equals(SiteMapPageEnum.CONTENT))
			{
				//ContentPage
				mainSiteMapUrlList = contentPageSiteMap(mainSiteMapUrlList, siteMapPage);
			}
			//			else if (siteMapPage.getCode().equals(SiteMapPageEnum.CUSTOM))
			//			{
			//				//Custom
			//				final CustomPageData data = new CustomPageData();
			//				for (final String url : siteModel.getSiteMapConfig().getCustomUrls())
			//				{
			//					data.setUrl(url);
			//					if (null != siteMapPage.getFrequency())
			//					{
			//						data.setChangeFrequency(siteMapPage.getFrequency().getCode());
			//					}
			//					if (null != siteMapPage.getPriority())
			//					{
			//						data.setPriority(siteMapPage.getPriority().toString());
			//					}
			//					mainSiteMapUrlList.add(data);
			//				}
			//			}
		}

		return mainSiteMapUrlList;
	}




	/**
	 * This method returns the custom data for Homepage for TPR-1285 Dynamic sitemap
	 *
	 * @param mainSiteMapUrlList
	 * @param siteMapPage
	 * @return List<CustomPageData>
	 */
	private List<CustomPageData> homePageSiteMap(final List<CustomPageData> mainSiteMapUrlList, final SiteMapPageModel siteMapPage)
	{
		final ContentPageModel homepage = getCmsPageService().getHomepage();
		final String relUrl = StringEscapeUtils.escapeXml(getHomepageUrlResolver().resolve(homepage));
		final CustomPageData data = new CustomPageData();
		data.setUrl(relUrl);
		if (null != siteMapPage.getFrequency())
		{
			data.setChangeFrequency(siteMapPage.getFrequency().getCode());
		}
		if (null != siteMapPage.getPriority())
		{
			data.setPriority(siteMapPage.getPriority().toString());
		}

		mainSiteMapUrlList.add(data);
		return mainSiteMapUrlList;
	}



	/**
	 * This method returns the custom data for category pages for TPR-1285 Dynamic sitemap
	 *
	 * @param siteModel
	 * @param mainSiteMapUrlList
	 * @param siteMapPage
	 * @return List<CustomPageData>
	 */
	private List<CustomPageData> categoryPageSiteMap(final CMSSiteModel siteModel, final List<CustomPageData> mainSiteMapUrlList,
			final SiteMapPageModel siteMapPage)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT {c.pk} FROM {Category AS c JOIN CatalogVersion AS cv ON {c.catalogVersion}={cv.pk} ")
				.append(" JOIN Catalog AS cat ON {cv.pk}={cat.activeCatalogVersion} ")
				.append(" JOIN CMSSite AS site ON {cat.pk}={site.defaultCatalog}}  WHERE {site.pk} = ?site ")
				.append(" AND NOT exists ({{select {cr.pk} from {CategoriesForRestriction as cr} where {cr.target} = {c.pk} }})");
		final String query = queryBuilder.toString();

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", siteModel);
		final List<CategoryModel> categoryList = doSearch(query, params, CategoryModel.class);
		for (final CategoryModel categoryModel : categoryList)
		{
			final String relUrl = StringEscapeUtils.escapeXml(getCategoryModelUrlResolver().resolve(categoryModel));
			final CustomPageData data = new CustomPageData();
			data.setUrl(relUrl);
			if (null != siteMapPage.getFrequency())
			{
				data.setChangeFrequency(siteMapPage.getFrequency().getCode());
			}
			if (null != siteMapPage.getPriority())
			{
				data.setPriority(siteMapPage.getPriority().toString());
			}

			mainSiteMapUrlList.add(data);
		}
		return mainSiteMapUrlList;
	}




	/**
	 * This method returns the custom data for content pages for TPR-1285 Dynamic sitemap
	 *
	 * @param mainSiteMapUrlList
	 * @param siteMapPage
	 * @return List<CustomPageData>
	 */
	private List<CustomPageData> contentPageSiteMap(final List<CustomPageData> mainSiteMapUrlList,
			final SiteMapPageModel siteMapPage)
	{

		final StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("select {cp.pk} from {ContentPage as cp},{CmsApprovalStatus as cas},{catalogversion as cat} ")
				.append("where {cp.approvalstatus}={cas.pk} and {cas.code}='approved' ")
				.append("and {cp.catalogversion}={cat.pk} and {cat.version}='Online'");

		final List<ContentPageModel> contentPageList = doSearch(queryBuilder.toString(), null, ContentPageModel.class);
		for (final ContentPageModel contentPageModel : contentPageList)
		{
			final CustomPageData data = new CustomPageData();
			final String contentPageTemplateUid = contentPageModel.getMasterTemplate().getUid();
			final String templateTypeForLabel = configurationService.getConfiguration().getString("mpl.contentPage.template.label");
			final String categoryTemplateType = configurationService.getConfiguration().getString(
					"mpl.contentPage.template.category");
			final String sellerTemplateType = configurationService.getConfiguration().getString("mpl.contentPage.template.seller");

			//Logic for templates with url picked up from label
			if (templateTypeForLabel.indexOf(contentPageTemplateUid) != -1)
			{
				data.setUrl(contentPageModel.getLabel());
				if (null != siteMapPage.getFrequency())
				{
					data.setChangeFrequency(siteMapPage.getFrequency().getCode());
				}
				if (null != siteMapPage.getPriority())
				{
					data.setPriority(siteMapPage.getPriority().toString());
				}

				mainSiteMapUrlList.add(data);
			}
			//Logic for category templates
			else if (categoryTemplateType.indexOf(contentPageTemplateUid) != -1 && null != contentPageModel.getCategoryAssociated())
			{
				final String relUrl = StringEscapeUtils.escapeXml(getCategoryModelUrlResolver().resolve(
						contentPageModel.getCategoryAssociated()));
				data.setUrl(relUrl);
				if (null != siteMapPage.getFrequency())
				{
					data.setChangeFrequency(siteMapPage.getFrequency().getCode());
				}
				if (null != siteMapPage.getPriority())
				{
					data.setPriority(siteMapPage.getPriority().toString());
				}

				mainSiteMapUrlList.add(data);
			}
			//Logic for seller templates
			else if (sellerTemplateType.indexOf(contentPageTemplateUid) != -1 && null != contentPageModel.getAssociatedSeller())
			{
				final StringBuilder urlBuilder = new StringBuilder();
				urlBuilder.append("s/").append(contentPageModel.getAssociatedSeller().getId());
				data.setUrl(urlBuilder.toString());
				if (null != siteMapPage.getFrequency())
				{
					data.setChangeFrequency(siteMapPage.getFrequency().getCode());
				}
				if (null != siteMapPage.getPriority())
				{
					data.setPriority(siteMapPage.getPriority().toString());
				}

				mainSiteMapUrlList.add(data);
			}

		}
		return mainSiteMapUrlList;
	}





	/**
	 * This method renders the xml file for custom site map for TPR-1285 Dynamic sitemap
	 *
	 * @param site
	 * @param currencyModel
	 * @param languageModel
	 * @param rendererTemplateModel
	 * @param models
	 * @param filePrefix
	 * @param index
	 * @return File
	 * @throws IOException
	 *
	 */
	@Override
	public File render(final CMSSiteModel site, final CurrencyModel currencyModel, final LanguageModel languageModel,
			final RendererTemplateModel rendererTemplateModel, final List<CustomPageData> models, final String filePrefix,
			final Integer index) throws IOException
	{

		LOG.debug("::::::::::Inside MplCustomPageSiteMapGenerator generator::::::::::");
		String prefix = (index != null) ? String.format("main" + "-%s-%s", "sitemap", Integer.valueOf(index.intValue() + 1))
				: String.format("main" + "-%s", "sitemap");
		prefix = GenericUtilityMethods.changePrefix(prefix);
		final File siteMap = new File(configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SITEMAP_FILE_LOCATION_CUSTOM), prefix + ".xml");


		final ImpersonationContext context = new ImpersonationContext();
		context.setSite(site);
		context.setCurrency(currencyModel);
		context.setLanguage(languageModel);

		return getImpersonationService().executeInContext(context, new ImpersonationService.Executor<File, IOException>()
		{
			@Override
			public File execute() throws IOException
			{
				final List<SiteMapUrlData> siteMapUrlDataList = getSiteMapUrlData(models);

				final SiteMapContext context = (SiteMapContext) applicationContext.getBean("siteMapContext");

				context.init(site, getSiteMapPageEnum());
				context.setSiteMapUrlData(siteMapUrlDataList);

				final BufferedWriter output = new BufferedWriter(new FileWriter(siteMap));
				try
				{
					// the template media is loaded only for english language.
					getCommonI18NService().setCurrentLanguage(getCommonI18NService().getLanguage("en"));
					getRendererService().render(rendererTemplateModel, context, output);
				}
				finally
				{
					IOUtils.closeQuietly(output);
				}

				return siteMap;
			}

		});
	}






	protected CMSPageService getCmsPageService()
	{
		return cmsPageService;
	}

	@Required
	public void setCmsPageService(final CMSPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}


	/**
	 * @param applicationContext
	 *           the applicationContext to set
	 */
	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}


	/**
	 * @return the homepageUrlResolver
	 */
	public DefaultHomepageContentPageUrlResolver getHomepageUrlResolver()
	{
		return homepageUrlResolver;
	}


	/**
	 * @param homepageUrlResolver
	 *           the homepageUrlResolver to set
	 */
	public void setHomepageUrlResolver(final DefaultHomepageContentPageUrlResolver homepageUrlResolver)
	{
		this.homepageUrlResolver = homepageUrlResolver;
	}


	/**
	 * @return the categoryModelUrlResolver
	 */
	public DefaultCategoryModelUrlResolver getCategoryModelUrlResolver()
	{
		return categoryModelUrlResolver;
	}


	/**
	 * @param categoryModelUrlResolver
	 *           the categoryModelUrlResolver to set
	 */
	public void setCategoryModelUrlResolver(final DefaultCategoryModelUrlResolver categoryModelUrlResolver)
	{
		this.categoryModelUrlResolver = categoryModelUrlResolver;
	}



	@Override
	public List<SiteMapUrlData> getSiteMapUrlData(final List<CustomPageData> models)
	{
		return Converters.convertAll(models, getSiteMapUrlDataConverter());
	}



	@Override
	protected List<CustomPageData> getDataInternal(final CMSSiteModel siteModel)
	{
		return null;
	}

	/**
	 * @return the contentPageUrlResolver
	 */
	public DefaultContentPageUrlResolver getContentPageUrlResolver()
	{
		return contentPageUrlResolver;
	}

	/**
	 * @param contentPageUrlResolver
	 *           the contentPageUrlResolver to set
	 */
	public void setContentPageUrlResolver(final DefaultContentPageUrlResolver contentPageUrlResolver)
	{
		this.contentPageUrlResolver = contentPageUrlResolver;
	}

	/**
	 * @return the siteBaseUrlResolutionService
	 */
	public SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	/**
	 * @param siteBaseUrlResolutionService
	 *           the siteBaseUrlResolutionService to set
	 */
	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

}