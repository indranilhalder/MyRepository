/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.model.StateModel;



/**
 * @author TCS
 *
 */
public interface AccountAddressDao
{

	/**
	 * @return List<StateModel>
	 */
	List<StateModel> getStates();
}
