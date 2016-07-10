/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.user.CustomerModel;

import javax.xml.bind.JAXBException;

import com.tisl.mpl.xml.pojo.MplCustomerWsData;


/**
 * @author TCS
 *
 */
public interface MplCustomerWebService
{
	/**
	 * @param customerWsDTO
	 * @throws JAXBException
	 * @throws Exception
	 */
	public void customerDataToCRM(MplCustomerWsData customerWsDTO) throws Exception;

	/**
	 * @param customerModel
	 * @param create_update_flag
	 */
	public void customerModeltoWsData(CustomerModel customerModel, String create_update_flag, boolean isBlackListed);


}
