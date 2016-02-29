/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.hmc.model.SavedValuesModel;

import java.util.Date;
import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplSellerPriorityReportDAO
{
	/**
	 * @Description : Returns data within a date range
	 * @param startDate
	 * @param endDate
	 * @return List<SavedValuesModel>
	 */
	List<SavedValuesModel> getSellerPriorityDetails(final Date startDate, final Date endDate);

	/**
	 * @Description : Returns data within any date range
	 * @return List<SavedValuesModel>
	 */
	public List<SavedValuesModel> getAllSellerPriorityDetails();

}
