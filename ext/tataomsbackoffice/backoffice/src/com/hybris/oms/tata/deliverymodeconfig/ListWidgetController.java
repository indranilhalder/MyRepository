/**
 *
 */
package com.hybris.oms.tata.deliverymodeconfig;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.deliverymodeconfig.DeliveryModeConfigFacade;
import com.hybris.oms.domain.deliverymode.dto.DeliveryModeConfig;


/**
 * @author Pradeep
 * 
 */
public class ListWidgetController extends DefaultWidgetController
{


	@WireVariable("deliveryModeConfigRestClient")
	private DeliveryModeConfigFacade deliveryModeConfigFacade;

	private static final Logger LOG = LoggerFactory.getLogger(ListWidgetController.class);

	// listbox to capture one row
	private Listbox listview;

	private Label errorLabel;



	@Override
	public void initialize(final Component comp)
	{
		LOG.info("************************DeliveryMode Cofig List zul Intializing...............................");
		super.initialize(comp);
		errorLabel.setValue("");
		List<DeliveryModeConfig> list = null;

		//This try handle for only ConnectException purpose   
		try
		{
			if (deliveryModeConfigFacade != null)
			{
				list = (List<DeliveryModeConfig>) deliveryModeConfigFacade.getAll();
			}
		}
		catch (final RuntimeException rtx)
		{
			Messagebox.show(getRootCause(rtx), "Error", Messagebox.OK, Messagebox.ERROR);

		}

		if (list != null)
		{
			listview.setModel(new ListModelList(list));
			listview.setItemRenderer(new DeliveryModeConfigListRenderer());
		}
	}






	@SocketEvent(socketId = "editorStatus")
	public void editListView(final Boolean editStatus)
	{
		LOG.info("************************DeliveryMode Cofig List ZUL Updating ...............................");


		if (editStatus.booleanValue() && deliveryModeConfigFacade != null)
		{
			final List<DeliveryModeConfig> list = (List<DeliveryModeConfig>) deliveryModeConfigFacade.getAll();
			listview.setModel(new ListModelList(list));
			listview.setItemRenderer(new DeliveryModeConfigListRenderer());
		}


	}

	@ViewEvent(componentID = "listview", eventName = Events.ON_SELECT)
	public void itemSelect()
	{

		LOG.info("************************DeliveryMode Cofig List zul  Sending selected object from list through output socket...............................");

		sendOutput("editorObject", listview.getSelectedItem().getValue());

	}

	public static String getRootCause(final Throwable throwable)
	{
		if (throwable.getCause() != null)
		{
			return getRootCause(throwable.getCause());
		}
		{
			if (throwable.getClass().getName().equals("java.net.ConnectException"))
			{
				return "WebServer is not running (OR) not listening on port(OR) "
						+ "Firewall is not permitted for host-port combination ";
			}


		}


		return "";
	}


}
