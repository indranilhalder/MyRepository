/**
 *
 */
package com.hybris.oms.tata.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;


/**
 * @author Pradeep
 * 
 */
public class CsvFileToZipFileService
{
	private static final Logger LOG = Logger.getLogger(CsvFileToZipFileService.class.getName());

	public void convert(final File sourceFile)
	{

		ZipOutputStream out = null;
		BufferedInputStream inputStream = null;
		try
		{
			final ZipEntry zenrty = new ZipEntry(sourceFile.getName());
			zenrty.setTime(sourceFile.lastModified());
			out = new ZipOutputStream(new FileOutputStream(new File(sourceFile.getParent() + "/"
					+ sourceFile.getName().replace(".csv", ".zip"))));
			out.putNextEntry(zenrty);
			final byte[] buffer = new byte[8192];
			inputStream = new BufferedInputStream(new FileInputStream(sourceFile), buffer.length);
			int count = 0;
			while ((count = inputStream.read(buffer, 0, buffer.length)) >= 0)
			{
				if (count != 0)
				{
					out.write(buffer, 0, count);
				}
			}

		}
		catch (final IOException excep)
		{
			LOG.error(excep.getMessage());
		}
		finally
		{
			try
			{
				inputStream.close();
				out.close();
			}
			catch (final IOException excep)
			{
				LOG.error(excep.getMessage());
			}
		}
	}

	public String convertCSVFileToString(final File file)
	{
		StringBuilder sb = null;
		try
		{
			sb = new StringBuilder();
			final Scanner scanner = new Scanner(file);
			LOG.info("File Length" + file.length());
			LOG.info("************BEFORE DELIMITER**************");
			scanner.useDelimiter("\\n");
			LOG.info("************AFTER DELIMITER**************");
			while (scanner.hasNext())
			{
				sb.append(scanner.next());
			}
			scanner.close();
			final String csvData = sb.toString();

			final String[] lines = csvData.split("\r\n|\r|\n");
			final int noOfLines = lines.length;
			LOG.info("************No Of in String**************" + noOfLines);
			LOG.info("************CSV DATA**************" + csvData);



		}
		catch (final Exception e)
		{
			LOG.info(e.getMessage(), e);
		}



		return sb.toString();

	}



}
