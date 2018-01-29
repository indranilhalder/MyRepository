/**
 *
 */
package com.tisl.lux.setup;

import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.initialization.SystemSetupContext;

import java.util.Iterator;


/**
 * @author Manoj G.
 *
 */
public class LuxurySampleDataImportService extends SampleDataImportService
{

	public static final String LUXURYHOMEPAGEURL = "/%s/import/coredata/contentCatalogs/%sContentCatalog/luxuryHomePage.impex";
	public static final String LUXURYHOMEPAGEENURL = "/%s/import/coredata/contentCatalogs/%sContentCatalog/luxuryHomePage_en.impex";
	public static final String LUXURYNAVIGATIONURL = "/%s/import/coredata/contentCatalogs/%sContentCatalog/luxuryNavigation.impex";
	public static final String LUXURYNAVIGATIONENURL = "/%s/import/coredata/contentCatalogs/%sContentCatalog/luxuryNavigation_en.impex";
	public static final String SEARCHRESULTSURL = "/%s/import/coredata/contentCatalogs/%sContentCatalog/searchResults.impex";
	public static final String LUXURYCATEGORYLANDINGPAGEURL = "/%s/import/coredata/contentCatalogs/%sContentCatalog/luxuryCategoryLandingPage.impex";
	public static final String LUXURYBRANDLANDINGURL = "/%s/import/coredata/contentCatalogs/%sContentCatalog/luxuryBrandLanding.impex";
	public static final String LUXURYCARTPAGEURL = "/%s/import/coredata/contentCatalogs/%sContentCatalog/luxuryCartPage.impex";
	public static final String LUXURYERRORPAGEURL = "/%s/import/coredata/contentCatalogs/%sContentCatalog/luxuryErrorPage.impex";

	@Override
	protected void importAllData(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final ImportData importData, final boolean syncCatalogs)
	{
		systemSetup.logInfo(context, "Begin importing sample data");
		systemSetup.logInfo(context, String.format("Begin importing common data for [%s]", new Object[]
		{ context.getExtensionName() }));
		importCommonData(context.getExtensionName());

		final boolean importProductCatalogData = systemSetup.getBooleanSystemSetupParameter(context, "importProductCatalogData");

		if (importProductCatalogData)
		{
			systemSetup.logInfo(context, String.format("Begin importing product catalog data for [%s]", new Object[]
			{ importData.getProductCatalogName() }));
			importProductCatalog(context.getExtensionName(), importData.getProductCatalogName());
		}

		final boolean importContentCatalogData = systemSetup.getBooleanSystemSetupParameter(context, "importContentCatalogData");

		if (importContentCatalogData)
		{
			for (final String contentCatalogName : importData.getContentCatalogNames())
			{
				systemSetup.logInfo(context, String.format("Begin importing content catalog data for [%s]", new Object[]
				{ contentCatalogName }));
				importContentCatalog(context.getExtensionName(), contentCatalogName);
				getSetupImpexService().importImpexFile(String.format(LUXURYHOMEPAGEURL, new Object[]
				{ context.getExtensionName(), contentCatalogName }), false);
				getSetupImpexService().importImpexFile(String.format(LUXURYHOMEPAGEENURL, new Object[]
				{ context.getExtensionName(), contentCatalogName }), false);
				getSetupImpexService().importImpexFile(String.format(LUXURYNAVIGATIONURL, new Object[]
				{ context.getExtensionName(), contentCatalogName }), false);
				getSetupImpexService().importImpexFile(String.format(LUXURYNAVIGATIONENURL, new Object[]
				{ context.getExtensionName(), contentCatalogName }), false);
				getSetupImpexService().importImpexFile(String.format(SEARCHRESULTSURL, new Object[]
				{ context.getExtensionName(), contentCatalogName }), false);
				getSetupImpexService().importImpexFile(String.format(LUXURYCATEGORYLANDINGPAGEURL, new Object[]
				{ context.getExtensionName(), contentCatalogName }), false);
				getSetupImpexService().importImpexFile(String.format(LUXURYBRANDLANDINGURL, new Object[]
				{ context.getExtensionName(), contentCatalogName }), false);
				getSetupImpexService().importImpexFile(String.format(LUXURYCARTPAGEURL, new Object[]
				{ context.getExtensionName(), contentCatalogName }), false);
				getSetupImpexService().importImpexFile(String.format(LUXURYERRORPAGEURL, new Object[]
				{ context.getExtensionName(), contentCatalogName }), false);
			}
		}
		if (importProductCatalogData)
		{
			synchronizeProductCatalog(systemSetup, context, importData.getProductCatalogName(), false);
		}
		if (importContentCatalogData)
		{
			for (final String contentCatalog : importData.getContentCatalogNames())
			{
				synchronizeContentCatalog(systemSetup, context, contentCatalog, false);
			}
		}
		assignDependent(importData.getProductCatalogName(), importData.getContentCatalogNames());

		if (syncCatalogs)
		{
			boolean productSyncSuccess = false;
			if (importProductCatalogData)
			{
				systemSetup.logInfo(context, String.format("Synchronizing product catalog for [%s]", new Object[]
				{ importData.getProductCatalogName() }));
				productSyncSuccess = synchronizeProductCatalog(systemSetup, context, importData.getProductCatalogName(), true);
			}
			if (importContentCatalogData)
			{
				for (final Iterator localIterator2 = importData.getContentCatalogNames().iterator(); localIterator2.hasNext();)
				{
					final String contentCatalogName = (String) localIterator2.next();

					systemSetup.logInfo(context, String.format("Synchronizing content catalog for [%s]", new Object[]
					{ contentCatalogName }));
					synchronizeContentCatalog(systemSetup, context, contentCatalogName, true);
				}
			}
			if (!(productSyncSuccess))
			{
				systemSetup.logInfo(context, String.format("Rerunning product catalog synchronization for [%s]", new Object[]
				{ importData.getProductCatalogName() }));
				if (!(synchronizeProductCatalog(systemSetup, context, importData.getProductCatalogName(), true)))
				{
					systemSetup.logInfo(context,
							String.format(
									"Rerunning product catalog synchronization for [%s], failed. Please consult logs for more details.",
									new Object[]
									{ importData.getProductCatalogName() }));
				}
			}
		}
		final boolean importStoreData = systemSetup.getBooleanSystemSetupParameter(context, "importStoreData");

		if (importStoreData)
		{
			for (final Object contentCatalogName = importData.getStoreNames().iterator(); ((Iterator) contentCatalogName).hasNext();)
			{
				final String storeName = (String) ((Iterator) contentCatalogName).next();

				systemSetup.logInfo(context, String.format("Begin importing store data for [%s]", new Object[]
				{ storeName }));
				importStore(context.getExtensionName(), storeName, importData.getProductCatalogName());

				systemSetup.logInfo(context, String.format("Begin importing job data for [%s]", new Object[]
				{ storeName }));
				importJobs(context.getExtensionName(), storeName);

				systemSetup.logInfo(context, String.format("Begin importing solr index data for [%s]", new Object[]
				{ storeName }));
				importSolrIndex(context.getExtensionName(), storeName);

				if (!(systemSetup.getBooleanSystemSetupParameter(context, "activateSolrCronJobs")))
				{
					continue;
				}
				systemSetup.logInfo(context, String.format("Activating solr index for [%s]", new Object[]
				{ storeName }));
				runSolrIndex(context.getExtensionName(), storeName);
			}
		}
		systemSetup.logInfo(context, "Finished importing sample data");
	}

}
