/**
 *
 */
package com.tisl.mpl.BuyBoxDataImport;

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
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */
public class BuyBoxImportUtility
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyBoxImportUtility.class.getName());


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	//@Resource
	//private DataSource buyBoxDataSource;
	
	Connection vjdbcConnection = null;
	Connection connection = null;
	Statement vjdbcStmt = null;
	PreparedStatement pst = null;

	public static final String COMMA = ",";

	public void executeExtraction()
	{
		final HybrisDataSource currentDataSource = null;
		final String productExportQuery = getDataExportQuery();
		LOG.debug("Buybox Export query :" + productExportQuery);
		Connection vjdbcConnection = null;
		//final Statement vjdbcStmt = null;
		ResultSet analyticsResult = null;
		try
		{
			// getting database connection from vjdbc
			currentDataSource = Registry.getCurrentTenantNoFallback().getDataSource(VjdbcDataSourceImplFactory.class.getName());
			vjdbcConnection = currentDataSource.getConnection();
			//Please Comment the above two lines in case of local and uncomment the below line
			//vjdbcConnection = buyBoxDataSource.getConnection();
			pst = vjdbcConnection.prepareStatement(productExportQuery);
			
			//set last Run time from MplConfig Table
			pst.setTimestamp(1, getLastRunTime());

			//Fetching Result
			analyticsResult = pst.executeQuery();

			// Getting the result set and assigned the values in MAP array.

			final List<Map<Integer, String>> listOfMaps = new ArrayList<Map<Integer, String>>();
			final int columnCount = analyticsResult.getMetaData() != null ? analyticsResult.getMetaData().getColumnCount() : 0;
			final List<String> rowIdList = new ArrayList<String>();
			Map<Integer, String> dataMap = null;

			while (analyticsResult.next())
			{
				dataMap = new HashMap<Integer, String>();
				rowIdList.add(analyticsResult.getString(columnCount));
				for (int i = 1; i <= columnCount; i++)
				{
					dataMap.put(Integer.valueOf(i - 1), analyticsResult.getString(i));
				}
				listOfMaps.add(dataMap);
			}


			writeExportDataIntoFile(listOfMaps);

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
				if (pst != null)
				{
					pst.close();
				}
				if (vjdbcConnection != null)
				{
					vjdbcConnection.close();
				}

			}
			catch (final Exception e)
			{
				LOG.warn("Error ocured while Data Extraction" + e.getMessage());
			}
		}

	}


	public Timestamp getLastRunTime()
	{

		HybrisDataSource currentDataSource = null;
		final String productExportQuery = getLastRunTimeQuery();
		LOG.debug("Buybox Export query :" + productExportQuery);
		Connection vjdbcConnection = null;
		Statement vjdbcStmt = null;
		ResultSet analyticsResult = null;
		try
		{
			// getting database connection from vjdbc
			currentDataSource = Registry.getCurrentTenantNoFallback().getDataSource(VjdbcDataSourceImplFactory.class.getName());
			vjdbcConnection = currentDataSource.getConnection();
			
			vjdbcStmt = vjdbcConnection.createStatement();
			//Fetching Result

			analyticsResult = vjdbcStmt.executeQuery(productExportQuery);


			while (analyticsResult.next())
			{
				return analyticsResult.getTimestamp(1);
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



		return null;
	}

	public String getDataExportQuery()
	{
		final String query = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.BUYBOX + MarketplacecommerceservicesConstants.QUERY);
		LOG.debug("query" + query);
		return query;
	}

	public String getLastRunTimeQuery()
	{
		final String query = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.BUYBOX + MarketplacecommerceservicesConstants.LASTRUNTIME);
		LOG.debug("query" + query);
		return query;
	}

	public void writeExportDataIntoFile(final List<Map<Integer, String>> listOfMaps)
	{


		String exportFileName = null;
		final Date date = new Date();
		//	final SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_S");
		final SimpleDateFormat ft = new SimpleDateFormat("-yyMMddHHmmssSSS");

		final String exportFilePath = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.BUYBOX + MarketplacecommerceservicesConstants.HOTFOLDERLOCATION)
				+ MarketplacecommerceservicesConstants.FRONTSLASH;
		final File exportDir = new File(exportFilePath);
		// Creating export directory if not exists.
		if (!exportDir.isDirectory())
		{
			exportDir.mkdir();
		}
		exportFileName = exportFilePath

				+ configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.BUYBOX + MarketplacecommerceservicesConstants.BUYBOX_FILE_NAME)
				+ ft.format(date) + MarketplacecommerceservicesConstants.DOT
				+ MarketplacecommerceservicesConstants.BUYBOX_FILE_EXTENSION;

		if (StringUtils.isNotEmpty(exportFileName))
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
