/**
 *
 */
package com.hybris.oms.tata.facade;

import java.util.List;

import com.hybris.oms.tata.data.MplBUCConfigurationsData;
import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.model.MplTimeSlotsModel;


/**
 * @author prabhakar
 *
 */
public interface ConfigarableParameterFacade
{

	public List<MplTimeSlotsData> onLoadMplTimeSlots();

	public void saveMplTimeSlots(List<MplTimeSlotsModel> mplTimeSlots);

	public void saveMplBUCConfigurations(MplBUCConfigurationsData mplBucConfigurations);


}
