/**
 *
 */
package com.tisl.mpl.returnwindowincrease.dao;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface ReturnWindowIncreaseDao
{
	public List<ConsignmentModel> getConsignment(List<String> list);
}