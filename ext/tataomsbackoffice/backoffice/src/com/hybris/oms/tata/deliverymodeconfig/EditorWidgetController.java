/**
 *
 */
package com.hybris.oms.tata.deliverymodeconfig;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Timebox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.deliverymodeconfig.DeliveryModeConfigFacade;
import com.hybris.oms.domain.deliverymode.dto.DeliveryModeConfig;


/**
 * @author nagarjuna
 *
 */
public class EditorWidgetController extends DefaultWidgetController
{

	private static final Logger LOG = LoggerFactory.getLogger(EditorWidgetController.class);

	@WireVariable("deliveryModeConfigRestClient")
	private DeliveryModeConfigFacade deliveryModeConfigFacade;

	private Grid gridForm;

	private Label deliverymode;
	private Timebox cutofftime;

	private Label sellerresponsetathr;
	private Label sellerresponsetatmin;
	//R2.3 new filed Order Processing Tat
	private Spinner orderProcessingTatHr;
	private Spinner orderProcessingTatMin;
	//R2.3 new filed Lp Handover Time
	private Timebox lpHandOverTime;

	private Spinner hotctathr;
	private Spinner hotctatmin;

	private Spinner shipmenttathr;
	private Spinner shipmenttatmin;

	private Spinner dfmtathr;
	private Spinner dfmtatmin;

	//R2.3  SAP-248 cr code
	private Intbox orderAcceptanceEdAir;
	private Intbox orderAcceptanceHdAir;
	private Intbox orderAcceptanceEdSurface;
	private Intbox orderAcceptanceHdSurface;
	private Intbox orderProcessingEdAir;
	private Intbox orderProcessingHdAir;
	private Intbox orderProcessingEdSurface;
	private Intbox orderProcessingHdSurface;
	private Timebox lpHandOverTimeEdAir;
	private Timebox lpHandOverTimeHdAir;
	private Timebox lpHandOverTimeEdSurface;
	private Timebox lpHandOverTimeHdSurface;

	final DateFormat formatter = new SimpleDateFormat("hh:mm a");

	@Override
	public void initialize(final Component comp)
	{
		LOG.info("************************DeliveryMode Cofig Editor zul Intializing...............................");
		super.initialize(comp);
		gridForm.setVisible(false);

	}

	/**
	 * Get the editor object from listwidget using input socket id.
	 *
	 * @param editorObject
	 *           editor object
	 * @throws InterruptedException
	 */

	@SocketEvent(socketId = "editorObject")
	public void editListView(final Object editorObject) throws InterruptedException
	{

		LOG.info(
				"************************DeliveryMode Cofig Editor Object Input taking by Socket event...............................");

		final DeliveryModeConfig deliveryModeConfig = (DeliveryModeConfig) editorObject;

		gridForm.setVisible(true);

		deliverymode.setValue(deliveryModeConfig.getDeliverymode());

		try
		{

			final String date = deliveryModeConfig.getCutofftime();
			final Date date1 = formatter.parse(date);
			final String formatedDate = formatter.format(date1);
			final Date date2 = formatter.parse(formatedDate);
			cutofftime.setValue(date2);

		}
		catch (final ParseException e)
		{
			LOG.info("Error: " + e.getMessage());
		}

		Integer sellerResTatHM[] = getHourMinutes(deliveryModeConfig.getSellerresponsetat());
		sellerresponsetathr.setValue(String.valueOf(sellerResTatHM[0].intValue()));
		sellerresponsetatmin.setValue(String.valueOf(sellerResTatHM[1].intValue()));
		//R2.3 Added new Filed Order Processing TAT
		sellerResTatHM = getHourMinutes(deliveryModeConfig.getOrderprocessingtat());
		orderProcessingTatHr.setValue(sellerResTatHM[0].intValue());
		orderProcessingTatMin.setValue(sellerResTatHM[1].intValue());
		//R2.3 Added new Filed Lp Handover Time
		try
		{

			final String date = deliveryModeConfig.getLphandovertime();
			final Date date1 = formatter.parse(date);
			final String formatedDate = formatter.format(date1);
			final Date date2 = formatter.parse(formatedDate);
			lpHandOverTime.setValue(date2);

		}
		catch (final ParseException e)
		{
			LOG.info("Error: " + e.getMessage());
		}

		sellerResTatHM = getHourMinutes(deliveryModeConfig.getHotctat());
		hotctathr.setValue(sellerResTatHM[0].intValue());
		hotctatmin.setValue(sellerResTatHM[1].intValue());

		sellerResTatHM = getHourMinutes(deliveryModeConfig.getShiptat());
		shipmenttathr.setValue(sellerResTatHM[0].intValue());
		shipmenttatmin.setValue(sellerResTatHM[1].intValue());

		sellerResTatHM = getHourMinutes(deliveryModeConfig.getDfmtat());
		dfmtathr.setValue(sellerResTatHM[0]);
		dfmtatmin.setValue(sellerResTatHM[1]);
		//R2.3  SAP-248 cr code
		orderAcceptanceEdAir.setValue(deliveryModeConfig.getOrderAcceptanceTATEDAir());
		orderAcceptanceHdAir.setValue(deliveryModeConfig.getOrderAcceptanceTATHDAir());
		orderAcceptanceEdSurface.setValue(deliveryModeConfig.getOrderAcceptanceTATEDSur());
		orderAcceptanceHdSurface.setValue(deliveryModeConfig.getOrderAcceptanceTATHDSur());
		orderProcessingEdAir.setValue(deliveryModeConfig.getOrderProcessingTATEDAir());
		orderProcessingHdAir.setValue(deliveryModeConfig.getOrderProcessingTATHDAir());
		orderProcessingEdSurface.setValue(deliveryModeConfig.getOrderProcessingTATEDSurface());
		orderProcessingHdSurface.setValue(deliveryModeConfig.getOrderProcessingTATHDSurface());

		try
		{

			final String date = deliveryModeConfig.getLphandoverTimeEDAir(); //ed air
			final Date date1 = formatter.parse(date);
			final String formatedDate = formatter.format(date1);
			final Date date2 = formatter.parse(formatedDate);
			lpHandOverTimeEdAir.setValue(date2);

		}
		catch (final ParseException e)
		{
			LOG.info("Error: " + e.getMessage());
		}
		try
		{

			final String date = deliveryModeConfig.getLphandovertimeHDAir(); //hdAir
			final Date date1 = formatter.parse(date);
			final String formatedDate = formatter.format(date1);
			final Date date2 = formatter.parse(formatedDate);
			lpHandOverTimeHdAir.setValue(date2);

		}
		catch (final ParseException e)
		{
			LOG.info("Error: " + e.getMessage());
		}
		try
		{

			final String date = deliveryModeConfig.getLphandoverTimeEDSurface(); //ed surface
			final Date date1 = formatter.parse(date);
			final String formatedDate = formatter.format(date1);
			final Date date2 = formatter.parse(formatedDate);
			lpHandOverTimeEdSurface.setValue(date2);

		}
		catch (final ParseException e)
		{
			LOG.info("Error: " + e.getMessage());
		}
		try
		{

			final String date = deliveryModeConfig.getLphandoverTimeHDSurface(); //hd surface
			final Date date1 = formatter.parse(date);
			final String formatedDate = formatter.format(date1);
			final Date date2 = formatter.parse(formatedDate);
			lpHandOverTimeHdSurface.setValue(date2);

		}
		catch (final ParseException e)
		{
			LOG.info("Error: " + e.getMessage());
		}

	}

	/**
	 * Function used to save the editor object from form and updated to DB.
	 *
	 * @throws InterruptedException
	 */

	@ViewEvent(componentID = "update", eventName = Events.ON_CLICK)
	public void doUpdateDeliveyMode() throws InterruptedException
	{

		LOG.info("************************DeliveryMode Cofig Editor update button ...............................");

		if (cutofftime.getValue() == null || cutofftime.getValue().equals("") || sellerresponsetathr.getValue() == null
				|| sellerresponsetathr.getValue().equals("") || sellerresponsetatmin.getValue() == null
				|| sellerresponsetatmin.getValue().equals("") || hotctatmin.getValue() == null || hotctatmin.getValue().equals("")
				|| hotctathr.getValue() == null || hotctathr.getValue().equals("") || dfmtatmin.getValue() == null
				|| dfmtatmin.getValue().equals("") || dfmtathr.getValue() == null || dfmtathr.getValue().equals("")
				|| shipmenttathr.getValue() == null || shipmenttathr.getValue().equals("") || shipmenttatmin.getValue() == null
				|| shipmenttatmin.getValue().equals("") || orderProcessingTatHr.getValue().equals("")
				|| orderProcessingTatHr.getValue() == null || orderProcessingTatMin.getValue().equals("")
				|| orderProcessingTatMin.getValue() == null || lpHandOverTime.getValue() == null
				|| lpHandOverTime.getValue().equals("") || orderAcceptanceEdAir.getValue() == null
				|| orderAcceptanceEdAir.getValue() < 0 || orderAcceptanceHdAir.getValue() == null
				|| orderAcceptanceHdAir.getValue() < 0 || orderAcceptanceEdSurface.getValue() == null
				|| orderAcceptanceEdSurface.getValue() < 0 || orderAcceptanceHdSurface.getValue() == null
				|| orderAcceptanceEdSurface.getValue() < 0 || orderProcessingEdAir.getValue() == null
				|| orderProcessingEdAir.getValue() < 0 || orderProcessingHdAir.getValue() == null
				|| orderProcessingHdAir.getValue() < 0 || orderProcessingEdSurface.getValue() == null
				|| orderProcessingEdSurface.getValue() < 0 || orderProcessingHdSurface.getValue() == null
				|| orderProcessingHdSurface.getValue() < 0 || lpHandOverTime.getValue() == null
				|| lpHandOverTime.getValue().equals("") || lpHandOverTimeEdAir.getValue() == null
				|| lpHandOverTimeEdAir.getValue().equals("") || lpHandOverTimeEdSurface.getValue() == null
				|| lpHandOverTimeEdSurface.getValue().equals("") || lpHandOverTimeHdAir.getValue() == null
				|| lpHandOverTimeHdAir.getValue().equals("") || lpHandOverTimeHdSurface.getValue() == null
				|| lpHandOverTimeHdSurface.getValue().equals(""))
		{

			Messagebox.show("Please filled, all the fields.");

		}
		else
		{
			final DeliveryModeConfig config = new DeliveryModeConfig();

			config.setDeliverymode(deliverymode.getValue());
			final String formatedDate = formatter.format(cutofftime.getValue());
			config.setCutofftime(formatedDate);
			config.setSellerresponsetat(getTimeInMinutes(Integer.parseInt(sellerresponsetathr.getValue()),
					Integer.parseInt(sellerresponsetatmin.getValue())));
			//R2.3 new code added
			config.setOrderprocessingtat(getTimeInMinutes(orderProcessingTatHr.getValue(), orderProcessingTatMin.getValue()));
			//R2.3 new code added
			final String lpHandeOverTime = formatter.format(lpHandOverTime.getValue());
			config.setLphandovertime(lpHandeOverTime);

			config.setHotctat(getTimeInMinutes(hotctathr.getValue(), hotctatmin.getValue()));
			config.setDfmtat(getTimeInMinutes(dfmtathr.getValue(), dfmtatmin.getValue()));
			config.setShiptat(getTimeInMinutes(shipmenttathr.getValue(), shipmenttatmin.getValue()));

			//R2.3  SAP-248 cr code
			config.setOrderAcceptanceTATEDAir(orderAcceptanceEdAir.getValue());
			config.setOrderAcceptanceTATHDAir(orderAcceptanceHdAir.getValue());
			config.setOrderAcceptanceTATEDSur(orderAcceptanceEdSurface.getValue());
			config.setOrderAcceptanceTATHDSur(orderAcceptanceHdSurface.getValue());
			config.setOrderProcessingTATEDAir(orderProcessingEdAir.getValue());
			config.setOrderProcessingTATHDAir(orderProcessingHdAir.getValue());
			config.setOrderProcessingTATEDSurface(orderProcessingEdSurface.getValue());
			config.setOrderProcessingTATHDSurface(orderProcessingHdSurface.getValue());
			final String lpHandOverTimeEdAirValue = formatter.format(lpHandOverTimeEdAir.getValue());
			final String lpHandOverTimeHdAirValue = formatter.format(lpHandOverTimeHdAir.getValue());
			final String lpHandOverTimeEdSurfaceValue = formatter.format(lpHandOverTimeEdSurface.getValue());
			final String lpHandOverTimeHdSurfaceValue = formatter.format(lpHandOverTimeHdSurface.getValue());
			config.setLphandoverTimeEDAir(lpHandOverTimeEdAirValue);
			config.setLphandovertimeHDAir(lpHandOverTimeHdAirValue);
			config.setLphandoverTimeEDSurface(lpHandOverTimeEdSurfaceValue);
			config.setLphandoverTimeHDSurface(lpHandOverTimeHdSurfaceValue);
			try
			{

				if (deliveryModeConfigFacade != null)
				{
					//calling the webservice to get updated value from DB
					deliveryModeConfigFacade.updateDeliveryModeConfig(config);
					Messagebox.show("Successfully Updated");
					sendOutput("editorStatus", Boolean.TRUE);
				}
			}
			catch (final Exception e)
			{
				LOG.info(e.getMessage());
				sendOutput("editorStatus", Boolean.FALSE);
				Messagebox.show("Failed to insert the values");
			}
		}

	}


	/**
	 * Used to split the minutes into hours and minutes.
	 *
	 * @param time
	 * @return
	 */
	public Integer[] getHourMinutes(final String time)
	{

		final Integer[] HrsMints = new Integer[2];
		final int timeDurationinInt = Integer.parseInt(time);
		HrsMints[0] = timeDurationinInt / 60;
		HrsMints[1] = timeDurationinInt % 60;

		return HrsMints;

	}

	/**
	 * Used to change the hours and minutes in to total minutes wrap a string..
	 *
	 * @param hours
	 * @param minutes
	 * @return
	 */

	public String getTimeInMinutes(final int hours, final int minutes)
	{

		return String.valueOf(hours * 60 + minutes);
	}

}
