/**
 *
 */
package com.tisl.mpl.integration.job;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author 884206
 *
 */
public class IAFeedUtility
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(IAFeedUtility.class.getName());


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	@Resource
	private DataSource iaDataSource;

	public void executeExport(final String dataExportQuery)
	{
		LOG.info("Current export query key: " + dataExportQuery);
		final String productExportQuery = getDataExportQuery(dataExportQuery);
		LOG.info("Analytics - Product Export query :" + productExportQuery);
		//	HybrisDataSource currentDataSource = null;
		Connection vjdbcConnection = null;
		Statement vjdbcStmt = null;
		ResultSet analyticsResult = null;
		try
		{
			// getting database connection from vjdbc
			//	currentDataSource = Registry.getCurrentTenantNoFallback().getDataSource(VjdbcDataSourceImplFactory.class.getName());
			vjdbcConnection = iaDataSource.getConnection();
			vjdbcStmt = vjdbcConnection.createStatement();
			analyticsResult = vjdbcStmt.executeQuery(productExportQuery);

			// Getting the result set and assigned the values in MAP array.

			final List<Map<Integer, String>> listOfMaps = new ArrayList<Map<Integer, String>>();
			final int columnCount = analyticsResult.getMetaData() != null ? analyticsResult.getMetaData().getColumnCount() : 0;
			final Map<Integer, String> dataColumnMap = new HashMap<Integer, String>();

			if (columnCount > 0)
			{
				for (int i = 1; i < columnCount; i++)
				{
					if (i == 1)
					{
						dataColumnMap.put(Integer.valueOf(i - 1), '#' + analyticsResult.getMetaData().getColumnName(i));
					}
					else
					{
						dataColumnMap.put(Integer.valueOf(i - 1), analyticsResult.getMetaData().getColumnName(i));
					}

					LOG.info("Export table column name ::::" + analyticsResult.getMetaData().getColumnName(i));
				}
				listOfMaps.add(dataColumnMap);
				Map<Integer, String> dataMap = null;

				while (analyticsResult.next())
				{
					dataMap = new HashMap<Integer, String>();

					for (int i = 1; i < columnCount; i++)
					{
						dataMap.put(Integer.valueOf(i - 1), analyticsResult.getString(i));
					}
					listOfMaps.add(dataMap);
				}
				writeExportDataIntoFile(dataExportQuery, listOfMaps);
			}
		}
		catch (final Exception e)
		{
			LOG.warn("Error occurred while getting data from database in Data Analytics export job" + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (analyticsResult != null)
				{
					analyticsResult.close();
				}
				if (vjdbcStmt != null)
				{
					vjdbcStmt.close();
				}
				if (vjdbcConnection != null)
				{
					vjdbcConnection.close();
				}

			}
			catch (final Exception e)
			{
				LOG.warn("Error occurred while closing the database connection in IA Feed export job" + e.getMessage());
			}
		}

	}

	public String getDataExportQuery(final String queryName)
	{
		//		final InputStream propFileValue = null;
		final String query = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.IAFEED_QUERY + queryName);

		return query;
	}

	public void writeExportDataIntoFile(final String dataExportQuery, final List<Map<Integer, String>> listOfMaps)
	{

		final String exportFilePath = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.IA_EXPORT_FOLDER);
		final File exportDir = new File(exportFilePath);
		// Creating export directory if not exists.
		if (!exportDir.isDirectory())
		{
			exportDir.mkdir();
		}
		String exportFileName = null;
		final Date date = new Date();
		//	final SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_S");
		final SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd-HHmm-");

		if (MarketplacecommerceservicesConstants.IA_CATEGORY_PRODUCT.equalsIgnoreCase(dataExportQuery))
		{
			exportFileName = exportFilePath
					+ ft.format(date)
					+ configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.IA_FILENAME_PRODUCTCATEGORY) + MarketplacecommerceservicesConstants.DOT
					+ MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
		}
		else if (MarketplacecommerceservicesConstants.IA_BRAND_PRODUCT.equalsIgnoreCase(dataExportQuery))
		{
			exportFileName = exportFilePath + ft.format(date)
					+ configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.IA_FILENAME_BRANDPRODUCT)
					+ MarketplacecommerceservicesConstants.DOT + MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
		}
		else if (MarketplacecommerceservicesConstants.IA_PRICE_INVENTORY.equalsIgnoreCase(dataExportQuery))
		{
			exportFileName = exportFilePath
					+ ft.format(date)
					+ configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.IA_FILENAME_PRICEINVENTORY) + MarketplacecommerceservicesConstants.DOT
					+ MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
		}
		if (exportFileName != null)
		{
			final File exportFile = new File(exportFileName);
			if (!exportFile.exists())
			{
				try
				{
					exportFile.createNewFile();
				}
				catch (final IOException e)
				{
					LOG.error("error occurred while creating the export file" + e.getMessage());
				}
			}
			CSVWriter DataImpexScriptWriter;
			try
			{
				DataImpexScriptWriter = new CSVWriter(exportFile, MarketplacecommerceservicesConstants.ENCODING, true);
				DataImpexScriptWriter.setFieldseparator('^');
				DataImpexScriptWriter.setCommentchar('#');
				DataImpexScriptWriter.setLinebreak("\r\n");
				if (listOfMaps != null && listOfMaps.size() > 0)
				{
					for (final Map<Integer, String> dataMap : listOfMaps)
					{
						DataImpexScriptWriter.write(dataMap);
					}
					DataImpexScriptWriter.close();

				}
			}
			catch (UnsupportedEncodingException | FileNotFoundException e)
			{
				LOG.error("Error occurred while creating the export file" + e.getMessage());
			}
			catch (final IOException e)
			{
				LOG.error("Error occurred while writing into the export file" + e.getMessage());
			}

		}
	}

}
