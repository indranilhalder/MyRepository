/**
 *
 */
package com.tisl.mpl.businesscontentimport;

import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import java.util.Map;


/**
 * @author TCS
 *
 */
public interface BusinessContentImportService
{

	String processUpdateForContentImport(CSVReader reader, CSVWriter writer, Map<Integer, String> map, Integer errorPosition,
			boolean headerRowIncluded);

	void processUpdateForProductMappingImport(CSVReader reader, CSVWriter writer, Map<Integer, String> map, Integer errorPosition,
			boolean headerRowIncluded);

	/**
	 * @param reader
	 * @param writer
	 * @param map
	 * @param errorPosition
	 * @param headerRowIncluded
	 * @param site
	 * @return
	 */
	String processUpdateForContentImport(CSVReader reader, CSVWriter writer, Map<Integer, String> map, Integer errorPosition,
			boolean headerRowIncluded, String site);
}
