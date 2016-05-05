/**
 *
 */
package com.tisl.mpl.core.mplconfig.service;

import java.util.List;



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

}