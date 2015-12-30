/**
 *
 */
package com.hybris.oms.tata.pincodeservice;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;


/**
 * @author Pradeep
 * 
 */
public class PincodeServiceListItemRenderer implements ListitemRenderer
{


	private static final Logger LOG = LoggerFactory.getLogger(PincodeServiceListItemRenderer.class);

	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index) throws InterruptedException
	{
		final File logisticsCalendar = (File) value;


		listitem.setValue(value);

		addLabelListcell(listitem, logisticsCalendar.getName());
		addDownlodListcell(listitem, logisticsCalendar);



	}

	private void addLabelListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label cellLabel = new Label(value);
		cellLabel.setParent(listCell);
		listCell.setParent(listitem);
	}

	private void addDownlodListcell(final Listitem listitem, final File file)
	{
		final Listcell listCell = new Listcell();
		final Button cellBtn = new Button();
		cellBtn.setLabel("Download");
		cellBtn.addEventListener("onClick", new EventListener()
		{

			public void onEvent(final Event arg0)
			{
				try
				{
					Filedownload.save(file, null);
				}
				catch (final FileNotFoundException e)
				{
					// YTODO Auto-generated catch block
					LOG.info(e.getMessage());
				}
			}
		});
		cellBtn.setStyle("font-size: 12px;");
		cellBtn.setParent(listCell);
		listCell.setParent(listitem);
	}





}