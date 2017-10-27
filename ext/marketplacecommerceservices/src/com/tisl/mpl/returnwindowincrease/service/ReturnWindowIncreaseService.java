/**
 *
 */
package com.tisl.mpl.returnwindowincrease.service;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface ReturnWindowIncreaseService
{
	public List<ConsignmentModel> getConsignment(List<String> list);
}