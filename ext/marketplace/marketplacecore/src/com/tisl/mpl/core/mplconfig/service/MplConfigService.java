/**
 *
 */
package com.tisl.mpl.core.mplconfig.service;

import java.util.List;

import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;



/**
 * The MplConfigService interface invoke the MplConfigDao by passes the requested id to method of DAO.
 * 
 * @author SAP
 * @version 1.0
 *
 */
public interface MplConfigService
{
	/**
	 * The getConfigValueById method passes the id to the dao class and return the value string which is taken out from
	 * the Config Model object.
	 *
	 * @param id
	 * @return string
	 */

	public String getConfigValueById(final String id);

	/**
	 * The getConfigValueById method passes the id to the dao class and return the list of string which contains the
	 * different values corresponding to the requested id.
	 *
	 * @param id
	 * @param separator
	 * @return List of String
	 */
	public List<String> getConfigValuesById(final String id, final String seperator);

	/**
	 * This method is used to save values to the config. entries w.r.t the key.
	 *
	 * @param id
	 * @param value
	 */
	void saveConfigValues(String key, String value);
	/**
	 * This method is used to save values to the config. entries w.r.t the key.
	 *
	 *
	 * @param configKey
	 */
	List<MplTimeSlotsModel> getDeliveryTimeSlotByKey(String configKey);
	/**
	 * @return
	 */
	public MplBUCConfigurationsModel getDeliveryCharges();
	
	/**
	 * This method is used to save values to the config. entries w.r.t the key.
	 *
	 * @param id
	 * @param value
	 */
	List<String> getDeliveryTimeSlots(String configKey);

}