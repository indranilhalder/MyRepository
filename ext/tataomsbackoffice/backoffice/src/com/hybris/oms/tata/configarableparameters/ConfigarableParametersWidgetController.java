/**
 *
 */
package com.hybris.oms.tata.configarableparameters;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.facade.ConfigarableParameterFacade;


/**
 * @author prabhakar
 *
 */
public class ConfigarableParametersWidgetController extends DefaultWidgetController
{
	private static final Logger LOG = Logger.getLogger(ConfigarableParametersWidgetController.class);

	@Wire
	private Listbox sdListbox;
	@Wire
	private Listbox edListbox;
	@Wire
	private Listbox rdListbox;

	ArrayList<MplTimeSlotsData> sdTimeSlots;
	ArrayList<MplTimeSlotsData> edTimeSlots;
	ArrayList<MplTimeSlotsData> rdTimeSlots;


	@Resource(name = "configarableParameterFacade")
	private ConfigarableParameterFacade configarableParameterFacade;


	/**
	 * @param configarableParameterFacade
	 *           the configarableParameterFacade to set
	 */
	public void setConfigarableParameterFacade(final ConfigarableParameterFacade configarableParameterFacade)
	{
		this.configarableParameterFacade = configarableParameterFacade;
	}


	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		LOG.info("configarable facade" + configarableParameterFacade);

		final List<MplTimeSlotsData> mplTimeSlotsList = configarableParameterFacade.onLoadMplTimeSlots();

		if (sdTimeSlots == null)
		{
			sdTimeSlots = new ArrayList<MplTimeSlotsData>();

		}
		if (edTimeSlots == null)
		{
			edTimeSlots = new ArrayList<MplTimeSlotsData>();

		}

		if (rdTimeSlots == null)
		{
			rdTimeSlots = new ArrayList<MplTimeSlotsData>();

		}
		for (final MplTimeSlotsData mplTimeSlots : mplTimeSlotsList)
		{
			if (mplTimeSlots.getTimeslotType().equalsIgnoreCase("SD"))
			{
				sdTimeSlots.add(mplTimeSlots);
			}
			else if (mplTimeSlots.getTimeslotType().equalsIgnoreCase("ED"))
			{
				edTimeSlots.add(mplTimeSlots);
			}
			else if (mplTimeSlots.getTimeslotType().equalsIgnoreCase("RD"))
			{
				rdTimeSlots.add(mplTimeSlots);
			}

		}
		sdListbox.setModel(new ListModelList<MplTimeSlotsData>(sdTimeSlots));
		edListbox.setModel(new ListModelList<MplTimeSlotsData>(edTimeSlots));
		rdListbox.setModel(new ListModelList<MplTimeSlotsData>(rdTimeSlots));

	}

}
