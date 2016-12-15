/**
 *
 */
package com.tisl.mpl.sitemap.generator.impl;

import de.hybris.platform.acceleratorservices.enums.SiteMapChangeFrequencyEnum;
import de.hybris.platform.acceleratorservices.enums.SiteMapPageEnum;
import de.hybris.platform.acceleratorservices.model.SiteMapPageModel;
import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.acceleratorservices.sitemap.generator.impl.ProductPageSiteMapGenerator;
import de.hybris.platform.acceleratorservices.sitemap.renderer.SiteMapContext;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class MplProductPageSiteMapGenerator extends ProductPageSiteMapGenerator
{
	private final static Logger LOG = Logger.getLogger(MplProductPageSiteMapGenerator.class.getName());
	private ApplicationContext applicationContext;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;



	/**
	 * This method renders the xml file for product site map
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
			final RendererTemplateModel rendererTemplateModel, final List<ProductModel> models, final String filePrefix,
			final Integer index) throws IOException
	{

		LOG.debug("::::::::::Inside MplProductPageSiteMapGenerator generator::::::::::");
		String prefix = (index != null) ? String.format(filePrefix.toLowerCase() + "-%s-%s", "sitemap",
				Integer.valueOf(index.intValue() + 1)) : String.format(filePrefix.toLowerCase() + "-%s", "sitemap");
		prefix = GenericUtilityMethods.changePrefix(prefix);
		final File siteMap = new File(configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SITEMAP_FILE_LOCATION_PRODUCT), prefix + ".xml");


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

				//Added for Product Page priority
				final Collection<SiteMapPageModel> siteMapPages = site.getSiteMapConfig().getSiteMapPages();
				Double priority = new Double(0);
				SiteMapChangeFrequencyEnum sfrequency = SiteMapChangeFrequencyEnum.DAILY;


				//Added for Product Page priority
				for (final SiteMapPageModel sm : siteMapPages)
				{
					if (sm.getCode().equals(SiteMapPageEnum.PRODUCT))
					{
						priority = sm.getPriority();
						sfrequency = sm.getFrequency();
					}
				}
				//Added for Product Page priority
				for (final SiteMapUrlData data : siteMapUrlDataList)
				{
					data.setChangeFrequency(sfrequency.getCode());
					data.setPriority(priority.toString());
				}
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

	/**
	 * @param applicationContext
	 *           the applicationContext to set
	 */
	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}
}