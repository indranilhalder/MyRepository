/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import org.zkoss.zul.Div;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.widgets.renderers.details.impl.OrderDetailsListRenderer;

/**
 * @author 1006687
 *
 */
public class MarketPlaceOrderDetailsListRenderer extends
		OrderDetailsListRenderer {
	
	@Override
	protected void populateKnownDetails(Object context, TypedObject item, Widget widget, Div detailContainer){
		//Overriding this method to do nothing.
	}

}
