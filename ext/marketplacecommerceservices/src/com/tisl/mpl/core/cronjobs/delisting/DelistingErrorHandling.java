package com.tisl.mpl.core.cronjobs.delisting;

import de.hybris.platform.util.CSVWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class DelistingErrorHandling
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DelistingErrorHandling.class);

	/**
	 * @Description : Checks for Invalid Column
	 * @param invalidColumns
	 * @param columnName
	 * @param columnValue
	 */
	public boolean addInvalidColumnName(final StringBuilder invalidColumns, final String columnName, final String columnValue)
	{
		boolean result = false;
		if (StringUtils.isEmpty(columnValue))
		{
			if (StringUtils.isEmpty(invalidColumns.toString()))
			{
				invalidColumns.append(columnName);
				LOG.error(columnName + " Not Present");
				result = true;
			}
			else
			{
				invalidColumns.append(columnName).append(',');
			}
		}
		result = false;
		return result;
	}

	/**
	 * @Description : Process Error Description
	 * @param writer
	 * @param errorHeaderLine
	 * @param errorColumnPosition
	 * @param headerRowIncluded
	 * @param line
	 * @param invalidColumns
	 * @param errorMessage
	 * @throws IOException
	 */
	public boolean processError(final CSVWriter writer, final Map<Integer, String> errorHeaderLine,
			final Integer errorColumnPosition, final boolean headerRowIncluded, final Map<Integer, String> line,
			final StringBuilder invalidColumns, final String errorMessage) throws IOException
	{
		boolean isHeaderRowIncluded = false;
		if (!headerRowIncluded)
		{
			writer.write(errorHeaderLine);
			isHeaderRowIncluded = true;
		}
		line.put(errorColumnPosition, errorMessage);
		writer.write(line);
		return isHeaderRowIncluded;
	}

	/**
	 * @Description : Prints Error Message to file
	 * @param writer
	 * @param errorHeaderLine
	 * @param errorColumnPosition
	 * @param line
	 * @param errorMessage
	 * @throws IOException
	 */
	public void writeErrorData(final CSVWriter writer, final Map<Integer, String> errorHeaderLine,
			final Integer errorColumnPosition, final Map<Integer, String> line, final String errorMessage) throws IOException
	{
		writer.write(errorHeaderLine);
		line.put(errorColumnPosition, errorMessage);
		writer.write(line);
	}

	/**
	 * @Description : Prints Error Message to file
	 * @param writer
	 * @param errorHeaderLine
	 * @param errorColumnList
	 * @param line
	 * @param errorMessage
	 * @throws IOException
	 */
	public void writeErrorData(final CSVWriter writer, final Map<Integer, String> errorHeaderLine,
			final List<Integer> errorColumnList, final Map<Integer, String> line, final String errorMessage) throws IOException
	{
		writer.write(errorHeaderLine);
		for (int i = 0; i < errorColumnList.size(); i++)
		{
			line.put(errorColumnList.get(i), errorMessage);
		}
		writer.write(line);

	}
}
