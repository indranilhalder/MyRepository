/**
 *
 */
package com.tisl.mpl.service;

import java.util.List;

import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.xml.pojo.ReturnLogisticsResponse;


/**
 * @author TCS
 *
 */
public interface ReturnLogisticsService
{
	public ReturnLogisticsResponse returnLogisticsCheck(final List<ReturnLogistics> returnLogisticsList);
}
