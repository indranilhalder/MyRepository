/**
 *
 */
package com.hybris.oms.tata.forms;

import java.util.List;

import com.hybris.oms.tata.data.MplBUCConfigurationsData;
import com.hybris.oms.tata.data.MplTimeSlotsData;


/**
 * this is used to hold the data when ConfigurableParamsForms form submited
 *
 * @author prabhakar
 */
public class ConfigarableParamsForm
{
	private List<MplTimeSlotsData> edTimeSlots;
	private List<MplTimeSlotsData> sdTimeSlots;
	private List<MplTimeSlotsData> rdTimeSlots;
	private MplBUCConfigurationsData mplBucConfigurations;

	/**
	 * @return the edTimeSlots
	 */
	public List<MplTimeSlotsData> getEdTimeSlots()
	{
		return edTimeSlots;
	}

	/**
	 * @param edTimeSlots
	 *           the edTimeSlots to set
	 */
	public void setEdTimeSlots(final List<MplTimeSlotsData> edTimeSlots)
	{
		this.edTimeSlots = edTimeSlots;
	}

	/**
	 * @return the sdTimeSlots
	 */
	public List<MplTimeSlotsData> getSdTimeSlots()
	{
		return sdTimeSlots;
	}

	/**
	 * @param sdTimeSlots
	 *           the sdTimeSlots to set
	 */
	public void setSdTimeSlots(final List<MplTimeSlotsData> sdTimeSlots)
	{
		this.sdTimeSlots = sdTimeSlots;
	}

	/**
	 * @return the rdTimeSlots
	 */
	public List<MplTimeSlotsData> getRdTimeSlots()
	{
		return rdTimeSlots;
	}

	/**
	 * @param rdTimeSlots
	 *           the rdTimeSlots to set
	 */
	public void setRdTimeSlots(final List<MplTimeSlotsData> rdTimeSlots)
	{
		this.rdTimeSlots = rdTimeSlots;
	}

	/**
	 * @return the mplBucConfigurations
	 */
	public MplBUCConfigurationsData getMplBucConfigurations()
	{
		return mplBucConfigurations;
	}

	/**
	 * @param mplBucConfigurations
	 *           the mplBucConfigurations to set
	 */
	public void setMplBucConfigurations(final MplBUCConfigurationsData mplBucConfigurations)
	{
		this.mplBucConfigurations = mplBucConfigurations;
	}



}
