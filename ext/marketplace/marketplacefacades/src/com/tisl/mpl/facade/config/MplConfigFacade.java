/**
 * 
 */
package com.tisl.mpl.facade.config;

import java.util.List;

import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;

/**
 * @author Techouts
 *
 */
public interface MplConfigFacade
{
String getCongigValue(final String configKey);

List<MplTimeSlotsModel> getDeliveryTimeSlotByKey(String configKey);

List<String> getDeliveryTimeSlots(String configKey);

MplBUCConfigurationsModel getDeliveryCharges();

}
