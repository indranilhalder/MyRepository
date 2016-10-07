/**
 *
 */
package com.hybris.oms.tata.renderer;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.tis.mpl.facade.shorturl.ShortUrlReportData;


/**
 * this is used for short url item render purpose
 *
 * @author prabhakar
 *
 */
public class ShortUrlReportItemRenderer implements ListitemRenderer
{





	@Override
	public void render(final Listitem listitem, final Object value, final int index) throws Exception
	{
		// YTODO Auto-generated method stub

		final ShortUrlReportData shortUrlData = (ShortUrlReportData) value;
		// keeping the values in listitem
		listitem.setValue(value);
		addListcell(listitem, shortUrlData.getOrderId());
		addListcell(listitem, shortUrlData.getShortUrl());
		addListcell(listitem, String.valueOf(shortUrlData.getCliks()));
		addListcell(listitem, String.valueOf(shortUrlData.getLogins()));
		//addListcell(listitem, shortUrlData.getLongUrl());

	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}

}
