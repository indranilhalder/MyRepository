/**
 * 
 */
package com.hybris.oms.tata.orders;

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
 */
public class OrdersSearchController extends DefaultWidgetController
{

	private Textbox searchText;


	private static final Logger LOG = LoggerFactory.getLogger(OrdersSearchController.class);

	/**
	 * This method is to search the inventory using USSID
	 */
	@ViewEvent(componentID = "searchButton", eventName = Events.ON_CLICK)
	public void searchQuery()
	{
		final String search_text = searchText.getValue().trim();
		if (search_text == null || search_text.equals(""))
		{

			Messagebox.show("Please Enter OrderId");

		}
		else
		{
			//sendOutput("searchedText", search_text);
			LOG.info("Search Button Clicked");

		}

	}
}
