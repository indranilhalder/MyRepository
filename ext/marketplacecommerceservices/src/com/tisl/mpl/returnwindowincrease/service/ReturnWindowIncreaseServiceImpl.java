/**
 *
 */
package com.tisl.mpl.returnwindowincrease.service;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.returnwindowincrease.dao.ReturnWindowIncreaseDao;


/**
 * @author TCS
 *
 */
public class ReturnWindowIncreaseServiceImpl implements ReturnWindowIncreaseService
{


	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ReturnWindowIncreaseService.class);
	private ReturnWindowIncreaseDao returnWindowIncreaseDao;

	//@Resource(name = "modelService")
	//private ModelService modelService;

	public ReturnWindowIncreaseDao getReturnWindowIncreaseDao()
	{
		return returnWindowIncreaseDao;
	}

	public void setReturnWindowIncreaseDao(final ReturnWindowIncreaseDao returnWindowIncreaseDao)
	{
		this.returnWindowIncreaseDao = returnWindowIncreaseDao;
	}

	@Override
	public List<ConsignmentModel> getConsignment(final List<String> list)
	{
		return getReturnWindowIncreaseDao().getConsignment(list);
	}
}