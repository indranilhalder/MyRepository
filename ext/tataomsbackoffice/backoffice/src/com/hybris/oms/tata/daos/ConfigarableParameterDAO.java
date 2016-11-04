/**
 *
 */
package com.hybris.oms.tata.daos;

import java.util.List;

import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;


/**
 * This incorporates the CRUD functionality we require for ConfigrableSearch Parameters
 *
 * @author prabhakar
 */
public interface ConfigarableParameterDAO
{

	public List<MplTimeSlotsModel> onLoadMplTimeSlots();

	public void saveMplTimeSlots(List<MplTimeSlotsModel> mplTimeSlots, String timeSlotType);

	public void saveMplBUCConfigurations(MplBUCConfigurationsModel mplBucConfigurations);
}
