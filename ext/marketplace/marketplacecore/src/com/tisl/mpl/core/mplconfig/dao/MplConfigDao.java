/**
 *
 */
package com.tisl.mpl.core.mplconfig.dao;

import java.util.List;

//import com.hybris.oms.tata.data.MplTimeSlotsData; sonar issue
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tisl.mpl.core.model.MplConfigModel;
import com.tisl.mpl.core.model.MplLPHolidaysModel;


/**
 * The MplConfigDao interface interacts with the hybris data and retrieves the result set on the basis of requested id.
 *
 * @author SAP
 * @version 1.0
 */
public interface MplConfigDao
{
	/**
	 * The getConfigValueById method retrieves the data from the hybris data on the basis of the id pass in dynamic
	 * query.
	 *
	 *
	 * @param id
	 * @return MplConfigModel
	 */
	public MplConfigModel getConfigValueById(final String id);
	
	/**
	 * This method is used to save values to the config. entries w.r.t the key.
	 *
	 * @param id
	 * @param value
	 */
	List<MplTimeSlotsModel> getDeliveryTimeSlotByKey(String configKey);

	/**
	 * @return
	 */
	public MplBUCConfigurationsModel getDeliveryCharges();

	/**
	 * @param configKey
	 * @return
	 */
	public MplLPHolidaysModel getMplLPHolidays(String configKey);

}