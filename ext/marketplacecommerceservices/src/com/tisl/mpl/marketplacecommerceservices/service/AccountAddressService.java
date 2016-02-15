/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tisl.mpl.facades.product.data.StateData;


/**
 * @author TCS
 *
 */
public interface AccountAddressService
{

	/**
	 * @return List
	 */
	List<StateData> getStates();
}
