/**
 *
 */
package com.tisl.mpl.service;

import java.util.List;

import com.tisl.mpl.model.StateModel;


/**
 * @author TCS
 *
 */
public interface MplClientAccountAddressService
{
	/**
	 * @return List<StateModel>
	 */
	public List<StateModel> getStates();

	/**
	 * @param description
	 * @return StateModel
	 */
	public StateModel getStateByDescription(String description);

}
