/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.hmc.model.SavedValuesModel;

import java.util.Date;
import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplSellerPriorityReportService
{
	/**
	 * @Description : Returns data within a date range
	 * @param startDate
	 * @param endDate
	 * @return List<SavedValuesModel>
	 */
	List<SavedValuesModel> getSellerPriorityDetails(final Date startDate, final Date endDate);

	/**
	 * @Description : Returns data without any date range
	 * @return List<SavedValuesModel>
	 */
	List<SavedValuesModel> getAllSellerPriorityDetails();

}
