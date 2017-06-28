/**
 *
 */
package com.tisl.mpl.facade.stw;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.facades.data.STWJsonRecomendationData;


/**
 * @author TCS
 *
 */
public interface STWWidgetFacade
{

	public List<STWJsonRecomendationData> getSTWWidgetFinalData(Map<String, String[]> stwParamsMap);
}
