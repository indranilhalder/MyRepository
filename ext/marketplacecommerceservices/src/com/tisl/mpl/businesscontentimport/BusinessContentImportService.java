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

	void processUpdateForContentImport(CSVReader reader, CSVWriter writer, Map<Integer, String> map, Integer errorPosition,
			boolean headerRowIncluded);

	void processUpdateForProductMappingImport(CSVReader reader, CSVWriter writer, Map<Integer, String> map, Integer errorPosition,
			boolean headerRowIncluded);
}
