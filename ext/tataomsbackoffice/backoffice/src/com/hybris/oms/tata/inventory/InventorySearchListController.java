/**
 * 
 */
package com.hybris.oms.tata.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.inventory.SearchInventoryFacade;
import com.hybris.oms.domain.inventorySearchByUssid.dto.ListInventorySearchByUssid;


/**
 * @author Saood
 * 
 */
public class InventorySearchListController extends DefaultWidgetController
{
	Label pendingAssignment, totalAts;


	@WireVariable("searchInventoryByUssidRestClient")
	private SearchInventoryFacade searchInventoryFacade;

	private static final Logger LOG = LoggerFactory.getLogger(InventorySearchListController.class);
	Listbox listView;

	@SocketEvent(socketId = "ussid")
	public void searchByUssidList(final String ussid)
	{
		LOG.info("Input socket......................");
		try
		{
			final ListInventorySearchByUssid inventories = searchInventoryFacade.searchStockRoomLocationByUSSID(ussid);
			if (inventories != null)
			{
				listView.setModel(new ListModelList(inventories.getInventorySearchByUssid()));
				listView.setItemRenderer(new InventoryListRenderer());
				pendingAssignment.setValue("PENDING ASSIGNMENTS  : " + inventories.getPendingAssignments());
				totalAts.setValue("TOTAL ATS : " + inventories.getTotalATS());
			}

		}
		catch (final Exception e)
		{
			Messagebox.show("USSID IS NOT FOUND");
			LOG.info("USSID IS NOT FOUND");
		}
	}


}
