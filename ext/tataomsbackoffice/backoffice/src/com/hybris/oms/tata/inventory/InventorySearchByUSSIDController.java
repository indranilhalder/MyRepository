/**
 *
 */
package com.hybris.oms.tata.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;


/**
 * @author Saood
 * 
 *         This class is to search the inventory using USSID
 * 
 */
public class InventorySearchByUSSIDController extends DefaultWidgetController
{


	private Textbox searchText;


	private static final Logger LOG = LoggerFactory.getLogger(InventorySearchByUSSIDController.class);

	/**
	 * This method is to search the inventory using USSID
	 */
	@ViewEvent(componentID = "searchButton", eventName = Events.ON_CLICK)
	public void searchQuery()
	{

		LOG.info("*****************************Search Button Clicked...................");
		final String search_text = searchText.getValue().trim();
		if (search_text == null || search_text.equals(""))
		{
			Messagebox.show("Please Enter USSID");
		}
		else
		{
			LOG.info("************************send search test socket...............................");
			sendOutput("searchedText", search_text);
			LOG.info("Search Button Clicked");

		}

	}
}
