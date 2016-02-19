/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.xml.pojo.MplCustomerWsData;


/**
 * @author TCS
 *
 */
public interface MplCustomerWebService
{
	/**
	 * @param customerWsDTO
	 */
	public void customerDataToCRM(MplCustomerWsData customerWsDTO);

	/**
	 * @param customerModel
	 * @param create_update_flag
	 */
	public void customerModeltoWsData(CustomerModel customerModel, String create_update_flag, boolean isBlackListed);


}
