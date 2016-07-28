/**
 *
 */
package com.hybris.oms.tata.facade;

import java.util.List;
import java.util.Set;

import com.hybris.oms.tata.data.MplBUCConfigurationsData;
import com.hybris.oms.tata.data.MplTimeSlotsData;


/**
 * @author prabhakar
 *
 */
public interface ConfigarableParameterFacade
{

	public List<MplTimeSlotsData> onLoadMplTimeSlots();

	public void saveMplTimeSlots(Set<MplTimeSlotsData> mplTimeSlots, String type);

	public void saveMplBUCConfigurations(MplBUCConfigurationsData mplBucConfigurations);


}