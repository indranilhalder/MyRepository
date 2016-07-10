/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface FetchCustomerDetailsService
{
	public void saveCronDetails(Date endTime, String cronCode);

	public MplConfigurationModel getCronDetails(String cronCode);

	public List<CustomerModel> fetchCustomerDetails();

	public List<CustomerModel> specificCustomerDetails(Date earlierDate, Date presentDate);

}
