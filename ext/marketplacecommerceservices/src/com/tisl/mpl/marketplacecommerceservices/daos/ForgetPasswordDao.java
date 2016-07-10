/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;


/**
 * @author 682160
 * 
 */
public interface ForgetPasswordDao
{
	List<CustomerModel> findCustomer(String email);
}
