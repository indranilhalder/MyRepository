/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;


/**
 * @author 594031
 * 
 */
public interface MplCustomerProfileDao
{

	/**
	 * @param oCustomerModel
	 * @return
	 */
	List<CustomerModel> getCustomerProfileDetail(CustomerModel oCustomerModel);

}
