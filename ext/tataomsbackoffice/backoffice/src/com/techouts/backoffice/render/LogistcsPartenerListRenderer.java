/**
 *
 */
package com.techouts.backoffice.render;

import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;


/**
 * @author prabhakar
 *
 */
public class LogistcsPartenerListRenderer implements ListitemRenderer<String>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see org.zkoss.zul.ListitemRenderer#render(org.zkoss.zul.Listitem, java.lang.Object, int)
	 */
	@Override
	public void render(final Listitem listitem, final String value, final int index) throws Exception
	{
		listitem.setLabel(value);

	}

}
