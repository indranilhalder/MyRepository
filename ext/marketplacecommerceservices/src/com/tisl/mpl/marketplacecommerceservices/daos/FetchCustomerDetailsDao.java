/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface FetchCustomerDetailsDao
{
	public MplConfigurationModel fetchConfigDetails(String code);

	public List<CustomerModel> fetchCustomerDetails();

	public List<CustomerModel> specificCustomerDetails(Date earlierDate, Date presentDate);
}
