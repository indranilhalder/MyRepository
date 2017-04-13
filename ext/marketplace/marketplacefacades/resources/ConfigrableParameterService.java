/**
 *
 */
package com.hybris.oms.tata.services;

import java.util.List;

import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;


/**
 * @author prabhakar this is service class used for configarable Search parms
 */
public interface ConfigrableParameterService
{

	public List<MplTimeSlotsModel> onLoadMplTimeSlots();

	public void saveMplTimeSlots(List<MplTimeSlotsModel> mplTimeSlots, String timeSlotType);

	public void saveMplBUCConfigurations(MplBUCConfigurationsModel mplBucConfigurations);
}