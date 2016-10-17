/**
 *
 */
package com.tisl.mpl.integration.job;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.virtualjdbc.db.VjdbcDataSourceImplFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	Connection vjdbcConnection = null;
	Connection connection = null;
	Statement vjdbcStmt = null;
	PreparedStatement pst = null;

	public static final String COMMA = ",";

	public void executeExport(final String dataExportQuery)
	{
		HybrisDataSource currentDataSource = null;
		LOG.info("Current export query key: " + dataExportQuery);
		final String productExportQuery = getDataExportQuery(dataExportQuery);
		LOG.info("Analytics - Product Export query :" + productExportQuery);
		Connection vjdbcConnection = null;
		Statement vjdbcStmt = null;
		ResultSet analyticsResult = null;
		try
		{
			// getting database connection from vjdbc
			currentDataSource = Registry.getCurrentTenantNoFallback().getDataSource(VjdbcDataSourceImplFactory.class.getName());
			vjdbcConnection = currentDataSource.getConnection();
			vjdbcStmt = vjdbcConnection.createStatement();
			analyticsResult = vjdbcStmt.executeQuery(productExportQuery);

			// Getting the result set and assigned the values in MAP array.

			final List<Map<Integer, String>> listOfMaps = new ArrayList<Map<Integer, String>>();
			final int columnCount = analyticsResult.getMetaData() != null ? analyticsResult.getMetaData().getColumnCount() : 0;
			final Map<Integer, String> dataColumnMap = new HashMap<Integer, String>();
			final List<String> rowIdList = new ArrayList<String>();

			if (columnCount > 0)
			{
				for (int i = 1; i < columnCount; i++)
				{
					if (i == 1)
					{
						dataColumnMap.put(Integer.valueOf(i - 1), analyticsResult.getMetaData().getColumnName(i));
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
					rowIdList.add(analyticsResult.getString(columnCount));
					for (int i = 1; i < columnCount; i++)
					{
						dataMap.put(Integer.valueOf(i - 1), analyticsResult.getString(i));
					}
					listOfMaps.add(dataMap);
				}

				LOG.debug("data export query : " + dataExportQuery);
				writeExportDataIntoFile(dataExportQuery, listOfMaps);
				if (null != dataExportQuery && dataExportQuery.equals(MarketplacecommerceservicesConstants.PRICEINVENTORY_FEED))
				{
					final String spDetailsHeader = configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.IA_SPDETAILSHEADER);
					final String[] spDetailsHeaderList = spDetailsHeader.split(COMMA);
					final ArrayList<String> rsHeaderList = new ArrayList<String>();
					if (columnCount > 0)
					{
						for (int i = 1; i < columnCount; i++)
						{
							LOG.debug("Column name to be matched" + analyticsResult.getMetaData().getColumnName(i));
							rsHeaderList.add(analyticsResult.getMetaData().getColumnName(i).toUpperCase());

						}
					}
					final String spDetailsQueryHeader = configurationService.getConfiguration()
							.getString(MarketplacecommerceservicesConstants.IA_SPDETAILSQUERYHEADER).toUpperCase();
					final String[] spDetailsQueryHeaderList = spDetailsQueryHeader.split(COMMA);
					final int spdColumnCount = spDetailsQueryHeaderList.length;
					final int[] headerMapper = new int[spdColumnCount];
					for (int i = 0; i < spdColumnCount; i++)
					{
						headerMapper[i] = rsHeaderList.indexOf(spDetailsQueryHeaderList[i]);
					}

					final List<Map<Integer, String>> spdListOfMaps = new ArrayList<Map<Integer, String>>();
					final Map<Integer, String> spdDataColumnMap = new HashMap<Integer, String>();
					for (int i = 0; i < spdColumnCount; i++)
					{
						spdDataColumnMap.put(Integer.valueOf(i), spDetailsHeaderList[i]);
					}
					spdListOfMaps.add(spdDataColumnMap);
					Map<Integer, String> spdDataMap = null;
					for (final Map<Integer, String> map : listOfMaps)
					{
						if (listOfMaps.indexOf(map) == 0)
						{
							continue;
						}
						spdDataMap = new HashMap<Integer, String>();
						for (int i = 0; i < spdColumnCount; i++)
						{
							spdDataMap.put(Integer.valueOf(i), map.get(Integer.valueOf(headerMapper[i])));
						}
						spdListOfMaps.add(spdDataMap);
					}

					writeExportforSellerPriceDetails(spdListOfMaps);
					updateProcessed(rowIdList, dataExportQuery);
				}
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



	/**
	 * @param rowIdList
	 * @param dataExportQuery
	 */
	private void updateProcessed(final List<String> rowIdList, final String dataExportQuery)
	{
		// YTODO Auto-generated method stub
		try
		{
			//	vjdbcConnection = iaDataSource.getConnection();
			connection = iaDataSource.getConnection();
			pst = connection.prepareStatement(getDataUpdateQuery(dataExportQuery));
			final int batchValue = configurationService.getConfiguration()
					.getInt(MarketplacecommerceservicesConstants.IA_BATCHVALUE);
			int count = 0;
			LOG.debug("No of rows to be updated: " + rowIdList.size());

			for (int i = 0; i < rowIdList.size(); i++)
			{
				pst.setString(1, rowIdList.get(i));
				pst.addBatch();
				count++;
				if (count == batchValue || i == (rowIdList.size() - 1))
				{
					pst.executeBatch();
					pst.clearBatch();
					LOG.debug("Batch Updated. No of Rows: " + count);
					count = 0;
				}
			}



		}
		catch (final SQLException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{

				if (pst != null)
				{
					pst.close();
				}
				if (connection != null)
				{
					connection.close();
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
		LOG.info("query" + query);
		return query;
	}

	public String getDataUpdateQuery(final String queryName)
	{
		//		final InputStream propFileValue = null;
		final String query = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.IAFEED_UPDATEQUERY + queryName);
		LOG.info("query" + query);
		return query;
	}

	public void writeExportDataIntoFile(final String dataExportQuery, final List<Map<Integer, String>> listOfMaps)
	{


		String exportFileName = null;
		final Date date = new Date();
		//	final SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_S");
		final SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");

		if (MarketplacecommerceservicesConstants.IA_CATEGORY_PRODUCT.equalsIgnoreCase(dataExportQuery))
		{

			final String exportFilePath = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.IA_CATEGORYEXPORT_FOLDER);
			final File exportDir = new File(exportFilePath);
			// Creating export directory if not exists.
			if (!exportDir.isDirectory())
			{
				exportDir.mkdir();
			}
			exportFileName = exportFilePath

			+ configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.IA_FILENAME_PRODUCTCATEGORY)
					+ ft.format(date) + MarketplacecommerceservicesConstants.DOT
					+ MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
		}
		else if (MarketplacecommerceservicesConstants.IA_BRAND_PRODUCT.equalsIgnoreCase(dataExportQuery))
		{
			final String exportFilePath = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.IA_BRANDEXPORT_FOLDER);
			final File exportDir = new File(exportFilePath);
			// Creating export directory if not exists.
			if (!exportDir.isDirectory())
			{
				exportDir.mkdir();
			}
			exportFileName = exportFilePath
					+ configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.IA_FILENAME_BRANDPRODUCT)
					+ ft.format(date) + MarketplacecommerceservicesConstants.DOT
					+ MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
		}
		else if (MarketplacecommerceservicesConstants.IA_PRICE_INVENTORY.equalsIgnoreCase(dataExportQuery))
		{

			final String exportFilePath = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.IA_PRICE_INVENTORYEXPORT_FOLDER);

			final File exportDir = new File(exportFilePath);
			// Creating export directory if not exists.
			if (!exportDir.isDirectory())
			{
				exportDir.mkdir();
			}
			exportFileName = exportFilePath

			+ configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.IA_FILENAME_PRICEINVENTORY)
					+ ft.format(date) + MarketplacecommerceservicesConstants.DOT
					+ MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
		}
		else if (MarketplacecommerceservicesConstants.IA_PRICEINVENTORY_CONTROL.equalsIgnoreCase(dataExportQuery))
		{
			LOG.debug("/////inside file writer//////");
			final String exportFilePath = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.IA_PRICEINVENTORYCONTROL_FOLDER);

			final File exportDir = new File(exportFilePath);
			// Creating export directory if not exists.
			if (!exportDir.isDirectory())
			{
				exportDir.mkdir();
			}
			exportFileName = exportFilePath

					+ configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.IA_FILENAME_PRICEINVENTORYCONTROL) + ft.format(date)
					+ MarketplacecommerceservicesConstants.DOT + MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
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
				DataImpexScriptWriter.setFieldseparator(',');
				//	DataImpexScriptWriter.setCommentchar('#');
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

	/**
	 * @param listOfMaps
	 */
	private void writeExportforSellerPriceDetails(final List<Map<Integer, String>> listOfMaps)
	{
		String exportFileName = null;
		final Date date = new Date();

		final SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
		final String exportFilePath = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.IA_SELLERPRICEDETAILSEXPORT_FOLDER);
		final File exportDir = new File(exportFilePath);
		// Creating export directory if not exists.
		if (!exportDir.isDirectory())
		{
			exportDir.mkdir();
		}
		exportFileName = exportFilePath

		+ configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.IA_FILENAME_SELLERPRICEDETAILS)
				+ ft.format(date) + MarketplacecommerceservicesConstants.DOT + MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;


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
			DataImpexScriptWriter.setFieldseparator(',');
			//	DataImpexScriptWriter.setCommentchar('#');
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

	public void executeExportForLuxury(final String dataExportQuery)
	{

		HybrisDataSource currentDataSource = null;
		LOG.info("Current export query key: " + dataExportQuery);
		final String productExportQuery = getDataExportQueryForLuxury(dataExportQuery);
		LOG.info("Analytics - Product Export query :" + productExportQuery);
		Connection vjdbcConnection = null;
		Statement vjdbcStmt = null;
		ResultSet analyticsResult = null;
		try
		{
			// getting database connection from vjdbc
			currentDataSource = Registry.getCurrentTenantNoFallback().getDataSource(VjdbcDataSourceImplFactory.class.getName());
			vjdbcConnection = currentDataSource.getConnection();
			vjdbcStmt = vjdbcConnection.createStatement();
			analyticsResult = vjdbcStmt.executeQuery(productExportQuery);

			// Getting the result set and assigned the values in MAP array.

			final List<Map<Integer, String>> listOfMaps = new ArrayList<Map<Integer, String>>();
			final int columnCount = analyticsResult.getMetaData() != null ? analyticsResult.getMetaData().getColumnCount() : 0;
			final Map<Integer, String> dataColumnMap = new HashMap<Integer, String>();
			final List<String> rowIdList = new ArrayList<String>();

			if (columnCount > 0)
			{
				for (int i = 1; i < columnCount; i++)
				{
					if (i == 1)
					{
						dataColumnMap.put(Integer.valueOf(i - 1), analyticsResult.getMetaData().getColumnName(i));
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
					rowIdList.add(analyticsResult.getString(columnCount));
					for (int i = 1; i < columnCount; i++)
					{
						dataMap.put(Integer.valueOf(i - 1), analyticsResult.getString(i));
					}
					listOfMaps.add(dataMap);
				}

				LOG.debug("data export query : " + dataExportQuery);
				writeExportDataForLuxuryIntoFile(dataExportQuery, listOfMaps);
				if (null != dataExportQuery && dataExportQuery.equals(MarketplacecommerceservicesConstants.PRICEINVENTORY_FEED))
				{
					final String spDetailsHeader = configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.IA_SPDETAILSHEADER);
					final String[] spDetailsHeaderList = spDetailsHeader.split(COMMA);
					final ArrayList<String> rsHeaderList = new ArrayList<String>();
					if (columnCount > 0)
					{
						for (int i = 1; i < columnCount; i++)
						{
							LOG.debug("Column name to be matched" + analyticsResult.getMetaData().getColumnName(i));
							rsHeaderList.add(analyticsResult.getMetaData().getColumnName(i).toUpperCase());

						}
					}
					final String spDetailsQueryHeader = configurationService.getConfiguration()
							.getString(MarketplacecommerceservicesConstants.IA_SPDETAILSQUERYHEADER).toUpperCase();
					final String[] spDetailsQueryHeaderList = spDetailsQueryHeader.split(COMMA);
					final int spdColumnCount = spDetailsQueryHeaderList.length;
					final int[] headerMapper = new int[spdColumnCount];
					for (int i = 0; i < spdColumnCount; i++)
					{
						headerMapper[i] = rsHeaderList.indexOf(spDetailsQueryHeaderList[i]);
					}

					final List<Map<Integer, String>> spdListOfMaps = new ArrayList<Map<Integer, String>>();
					final Map<Integer, String> spdDataColumnMap = new HashMap<Integer, String>();
					for (int i = 0; i < spdColumnCount; i++)
					{
						spdDataColumnMap.put(Integer.valueOf(i), spDetailsHeaderList[i]);
					}
					spdListOfMaps.add(spdDataColumnMap);
					Map<Integer, String> spdDataMap = null;
					for (final Map<Integer, String> map : listOfMaps)
					{
						if (listOfMaps.indexOf(map) == 0)
						{
							continue;
						}
						spdDataMap = new HashMap<Integer, String>();
						for (int i = 0; i < spdColumnCount; i++)
						{
							spdDataMap.put(Integer.valueOf(i), map.get(Integer.valueOf(headerMapper[i])));
						}
						spdListOfMaps.add(spdDataMap);
					}

					writeExportforSellerPriceDetails(spdListOfMaps);
					updateProcessed(rowIdList, dataExportQuery);
				}
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


	/**
	 * @param dataExportQuery
	 * @return
	 */
	private String getDataExportQueryForLuxury(final String dataExportQuery)
	{
		final String query = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.IAFEED_QUERY_LUXURY + dataExportQuery);
		LOG.info("query" + query);
		return query;
	}


	/**
	 * @param dataExportQuery
	 * @param listOfMaps
	 */
	private void writeExportDataForLuxuryIntoFile(final String dataExportQuery, final List<Map<Integer, String>> listOfMaps)
	{



		String exportFileName = null;
		final Date date = new Date();
		//	final SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_S");
		final SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");

		if (MarketplacecommerceservicesConstants.IA_CATEGORY_PRODUCT.equalsIgnoreCase(dataExportQuery))
		{

			final String exportFilePath = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.IA_CATEGORYEXPORT_LUXURYFOLDER);
			final File exportDir = new File(exportFilePath);
			// Creating export directory if not exists.
			if (!exportDir.isDirectory())
			{
				exportDir.mkdir();
			}
			exportFileName = exportFilePath

			+ configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.IA_FILENAME_PRODUCTCATEGORY)
					+ ft.format(date) + MarketplacecommerceservicesConstants.DOT
					+ MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
		}
		else if (MarketplacecommerceservicesConstants.IA_BRAND_PRODUCT.equalsIgnoreCase(dataExportQuery))
		{
			final String exportFilePath = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.IA_BRANDEXPORT_LUXURYFOLDER);
			final File exportDir = new File(exportFilePath);
			// Creating export directory if not exists.
			if (!exportDir.isDirectory())
			{
				exportDir.mkdir();
			}
			exportFileName = exportFilePath
					+ configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.IA_FILENAME_BRANDPRODUCT)
					+ ft.format(date) + MarketplacecommerceservicesConstants.DOT
					+ MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
		}
		else if (MarketplacecommerceservicesConstants.IA_PRICE_INVENTORY.equalsIgnoreCase(dataExportQuery))
		{

			final String exportFilePath = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.IA_PRICE_INVENTORYEXPORT_LUXURYFOLDER);

			final File exportDir = new File(exportFilePath);
			// Creating export directory if not exists.
			if (!exportDir.isDirectory())
			{
				exportDir.mkdir();
			}
			exportFileName = exportFilePath

			+ configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.IA_FILENAME_PRICEINVENTORY)
					+ ft.format(date) + MarketplacecommerceservicesConstants.DOT
					+ MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
		}
		else if (MarketplacecommerceservicesConstants.IA_PRICEINVENTORY_CONTROL.equalsIgnoreCase(dataExportQuery))
		{
			LOG.debug("/////inside file writer//////");
			final String exportFilePath = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.IA_PRICEINVENTORYCONTROL_FOLDER);

			final File exportDir = new File(exportFilePath);
			// Creating export directory if not exists.
			if (!exportDir.isDirectory())
			{
				exportDir.mkdir();
			}
			exportFileName = exportFilePath

					+ configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.IA_FILENAME_PRICEINVENTORYCONTROL) + ft.format(date)
					+ MarketplacecommerceservicesConstants.DOT + MarketplacecommerceservicesConstants.IA_FILE_EXTENSION;
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
				DataImpexScriptWriter.setFieldseparator(',');
				//	DataImpexScriptWriter.setCommentchar('#');
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
