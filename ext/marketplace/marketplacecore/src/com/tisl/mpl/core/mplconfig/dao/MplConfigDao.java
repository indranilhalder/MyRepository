/**
 *
 */
package com.tisl.mpl.core.mplconfig.dao;

import com.tisl.mpl.core.model.MplConfigModel;


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


}