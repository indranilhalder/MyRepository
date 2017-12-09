/**
 *
 */
package com.tisl.mpl.servicelayer;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.impex.jalo.imp.DefaultDumpHandler;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.CSVReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
public class MplServicelayerTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(MplServicelayerTest.class);


	/**
	 * Add dependency in class path for following platform/bootstrap
	 * platform/ext/platformservices/classes/de/hybris/platform/order/exceptions
	 * commercefacades/classes/de/hybris/platform/commercefacades/product/converters/populator
	 * platform/ext/platformservices/classes/de/hybris/platform/catalog/synchronization
	 * platform/bootstrap/modelclasses/de/hybris/platform/core/model/order
	 *
	 *
	 * @Description : Method to create Core Data & Store
	 * @throws Exception
	 */
	public static void createCoreData() throws Exception
	{
		LOG.info("Creating essential data for core ..");
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAdminEmployee());
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);

		/**
		 * TODO: Add the Complete of the csv files before Running
		 */
		importCsv(
				"D:\\Production_Support\\hybris\\bin\\custom\\marketplacecommerceservices\\resources\\servicelayer\\test\\productcatalog.csv",
				"windows-1252");
		importCsv(
				"D:\\Production_Support\\hybris\\bin\\custom\\marketplacecommerceservices\\resources\\servicelayer\\test\\store.csv",
				"windows-1252");
		importCsv(
				"D:\\Production_Support\\hybris\\bin\\custom\\marketplacecommerceservices\\resources\\servicelayer\\test\\essentialData.csv",
				"windows-1252");

		LOG.info("Finished creating essential data for core in " + (System.currentTimeMillis() - startTime) + "ms");
		testCoreData();
	}

	/**
	 * @Description : Method to create Core Data & Store
	 * @throws Exception
	 */
	public static void createProductData() throws Exception
	{
		LOG.info("Creating Product & Category data for Store ..");
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAdminEmployee());
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);

		/**
		 * TODO: Add the Complete of the csv files before Running
		 */
		importCsv(
				"D:\\Production_Support\\hybris\\bin\\custom\\marketplacecommerceservices\\resources\\servicelayer\\test\\mplcategory.csv",
				"windows-1252");
		importCsv(
				"D:\\Production_Support\\hybris\\bin\\custom\\marketplacecommerceservices\\resources\\servicelayer\\test\\mplProduct.csv",
				"windows-1252");

		LOG.info("Finished creating Product data for store in " + (System.currentTimeMillis() - startTime) + "ms");
		testProductData();
	}

	public static void createDefaultUsers() throws Exception
	{
		LOG.info("Creating test users ..");
		final long startTime = System.currentTimeMillis();

		/**
		 * TODO: Add the Complete of the csv files before Running
		 */
		importCsv(
				"D:\\Production_Support\\hybris\\bin\\custom\\marketplacecommerceservices\\resources\\servicelayer\\test\\customer.csv",
				"windows-1252");

		final User user = UserManager.getInstance().getUserByLogin("2000000018");
		junit.framework.Assert.assertNotNull(user);
		//System.out.println("User>>>>>>" + user.getName());

		LOG.info("Finished creating test users in " + (System.currentTimeMillis() - startTime) + "ms");
	}

	/**
	 * @Description : Method to test Product Data
	 */
	protected static void testProductData()
	{
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);

		final CategoryModel category = (CategoryModel) flexibleSearchService
				.search("SELECT {PK} FROM {Category} WHERE {code}='MPH1'").getResult().get(0);
		junit.framework.Assert.assertNotNull(category);
		//System.out.println("Category>>>>>>>>>>>" + category.getCode());

		final ProductModel product = (ProductModel) flexibleSearchService
				.search("SELECT {PK} FROM {Product} WHERE {code}='MP000000000009962'").getResult().get(0);
		junit.framework.Assert.assertNotNull(product);
		//	System.out.println("Product>>>>>>>>>>>" + product.getArticleDescription());

	}

	/**
	 * @Description : Method to test Core Data
	 *
	 */
	protected static void testCoreData()
	{
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);

		final BaseStoreModel oModel = (BaseStoreModel) flexibleSearchService
				.search("SELECT {PK} FROM {BaseStore} WHERE {uid}='mpl'").getResult().get(0);
		junit.framework.Assert.assertNotNull(oModel);
		//System.out.println("Base-Store>>>>>>>>>>>" + oModel);

		final CatalogModel catalog = (CatalogModel) flexibleSearchService
				.search("SELECT {PK} FROM {Catalog} WHERE {id}='mplProductCatalog'").getResult().get(0);
		junit.framework.Assert.assertNotNull(catalog);
		//System.out.println("Catalog>>>>>>>>>>>" + catalog.getId());


	}


	/**
	 * Import csv
	 *
	 * @param csvFile
	 * @param encoding
	 * @throws ImpExException
	 * @throws FileNotFoundException
	 */
	protected static void importCsv(final String csvFile, final String encoding) throws ImpExException, FileNotFoundException
	{
		LOG.info("importing resource " + csvFile);

		org.junit.Assert.assertNotNull("Given file is null", csvFile);
		//final InputStream is = MplServicelayerTest.class.getResourceAsStream(csvFile);

		final InputStream is = new FileInputStream(csvFile);
		org.junit.Assert.assertNotNull("Given file " + csvFile + " can not be found in classpath", is);

		importStream(is, encoding, csvFile);
	}

	/**
	 *
	 * Import csv data
	 *
	 * @param is
	 * @param encoding
	 * @param resourceName
	 * @throws ImpExException
	 */
	protected static void importStream(final InputStream is, final String encoding, final String resourceName)
			throws ImpExException
	{
		importStream(is, encoding, resourceName, true);
	}

	/**
	 * Import csv data
	 *
	 * @param is
	 * @param encoding
	 * @param resourceName
	 * @param hijackExceptions
	 * @throws ImpExException
	 */
	protected static void importStream(final InputStream is, final String encoding, final String resourceName,
			final boolean hijackExceptions) throws ImpExException
	{
		CSVReader reader = null;
		try
		{
			reader = new CSVReader(is, encoding);
		}
		catch (final UnsupportedEncodingException localUnsupportedEncodingException)
		{
			org.junit.Assert.fail("Given encoding " + encoding + " is not supported");
		}

		MediaDataTranslator.setMediaDataHandler(new DefaultMediaDataHandler());
		Importer importer = null;
		ImpExException error = null;
		try
		{
			importer = new Importer(reader);
			importer.getReader().enableCodeExecution(true);
			importer.setMaxPass(-1);
			importer.setDumpHandler(new FirstLinesDumpReader());
			importer.importAll();
		}
		catch (final ImpExException e)
		{
			if (!(hijackExceptions))
			{
				throw e;
			}
			error = e;
		}
		finally
		{
			MediaDataTranslator.unsetMediaDataHandler();
		}

		if (importer.hasUnresolvedLines())
		{
			org.junit.Assert.fail("Import has " + importer.getDumpedLineCountPerPass() + "+unresolved lines, first lines are:\n"
					+ importer.getDumpHandler().getDumpAsString());
		}
		org.junit.Assert.assertNull("Import of resource " + resourceName + " failed" + ((error == null) ? "" : error.getMessage()),
				error);
		org.junit.Assert.assertFalse("Import of resource " + resourceName + " failed", importer.hadError());
	}


	private static class FirstLinesDumpReader extends DefaultDumpHandler
	{
		private static final Logger LOG = Logger.getLogger(FirstLinesDumpReader.class);

		FirstLinesDumpReader()
		{
			LOG.info("Inside FirstLinesDumpReader Constructor");
		}

		@Override
		public String getDumpAsString()
		{
			final StringBuffer result = new StringBuffer(100);
			try
			{
				final BufferedReader reader = new BufferedReader(new FileReader(getDumpAsFile()));
				result.append(reader.readLine() + "\n");
				result.append(reader.readLine() + "\n");
				result.append(reader.readLine() + "\n");
				reader.close();
			}
			catch (final FileNotFoundException e)
			{
				result.append("Error while reading dump " + e.getMessage());
			}
			catch (final IOException e)
			{
				result.append("Error while reading dump " + e.getMessage());
			}
			return result.toString();
		}
	}

	/**
	 * JUnit Test Method
	 *
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void testPrerequisite() throws Exception
	{
		createDefaultUsers();
		createCoreData();
		createProductData();
	}



}
