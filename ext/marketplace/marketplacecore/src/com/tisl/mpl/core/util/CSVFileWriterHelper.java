package com.tisl.mpl.core.util;

import de.hybris.platform.util.CSVWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class CSVFileWriterHelper
{
	private final CSVWriter writer;
	private final static char FIELD_SEPARATOR = ',';
	private final static char TEXT_SEPARATOR = '\"';

	/**
	 * Instantiates a new cSV file writer helper.
	 *
	 * @param filePath
	 *           the file path
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public CSVFileWriterHelper(final String filePath) throws IOException
	{
		final File output_csv_file = new File(filePath);
		final OutputStream output = new BufferedOutputStream(new FileOutputStream(output_csv_file));
		writer = getCSVWriter(output);
	}

	/**
	 * Writes a csv line.
	 *
	 * @param line
	 *           the line to be written to the file
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public void writeCSVLine(final String line) throws IOException
	{
		if (StringUtils.isNotBlank(line))
		{
			writer.writeSrcLine(line);
		}
	}

	/**
	 * Write a map line.
	 *
	 * @param line
	 *           the line to be written to the file
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public void writeMapLine(final Map line) throws IOException
	{
		writer.write(line);
	}

	/**
	 * Write a list of lines to the file.
	 *
	 * @param lines
	 *           the lines to be written to the file
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public void writeList(final List<Map<Integer, String>> lines) throws IOException
	{
		for (final Map<Integer, String> line : lines)
		{
			writeMapLine(line);
		}
	}

	/**
	 * Closes the csv file.
	 *
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public void closeCSVFile() throws IOException
	{
		writer.close();
	}

	private CSVWriter getCSVWriter(final OutputStream output) throws UnsupportedEncodingException, FileNotFoundException
	{
		final CSVWriter csvWriter = new CSVWriter(output, "UTF-8");
		csvWriter.setFieldseparator(FIELD_SEPARATOR);
		csvWriter.setTextseparator(TEXT_SEPARATOR);
		return csvWriter;
	}

}
