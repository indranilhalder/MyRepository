/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import java.util.Map;


/**
 * @author TCS
 *
 */
public interface BulkPromotionRestrictionService
{
	void processUpdateForPromoRestriction(CSVReader reader, CSVWriter writer, Map<Integer, String> map, Integer errorPosition,
			boolean headerRowIncluded);

}
