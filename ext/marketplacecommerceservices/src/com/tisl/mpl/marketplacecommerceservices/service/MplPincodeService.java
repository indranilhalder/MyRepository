/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.core.model.PincodeServiceabilityDataModel;



/**
 * @author TCS
 *
 */
public interface MplPincodeService
{

	/**
	 * Keeps record of Cron Last run time
	 *
	 * @param code
	 * @return Map<Boolean, Date>
	 */
	Map<String, Date> getConfigurationData(String code);

	/**
	 * Data to invalidate from Cache
	 *
	 * @param jobLastRunDate
	 * @param date
	 * @return List<PincodeServiceabilityDataModel>
	 */
	List<PincodeServiceabilityDataModel> getPincodeData(Date jobLastRunDate, Date date);

	/**
	 * Save Cron Data in Mpl Configuration Table
	 *
	 * @param startTime
	 * @param code
	 */
	void saveCronData(Date startTime, String code);

	/**
	 * Data to invalidate from Cache
	 *
	 * @return List<PincodeServiceabilityDataModel>
	 */
	List<PincodeServiceabilityDataModel> fetchData();

}
