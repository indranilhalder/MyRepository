/**
 *
 */
package com.tisl.mpl.service;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.daos.MplClientAccountAddressDao;
import com.tisl.mpl.model.StateModel;


/**
 * @author TCS
 *
 */
public class MplClientAccountAddressServiceImpl implements MplClientAccountAddressService
{
	@Resource(name = "mplClientAccountAddressDao")
	private MplClientAccountAddressDao mplClientAccountAddressDao;

	/*
	 * @description:Method to get all states
	 */
	@Override
	public List<StateModel> getStates()
	{
		final List<StateModel> stateModelList = mplClientAccountAddressDao.getStates();
		return stateModelList;
	}

	/*
	 * @param description
	 *
	 * @return StateModel
	 *
	 * @description:Method to get state model based on state description
	 */
	@Override
	public StateModel getStateByDescription(final String description)
	{
		final StateModel stateModel = mplClientAccountAddressDao.getStateByDescription(description);
		return stateModel;
	}
}
