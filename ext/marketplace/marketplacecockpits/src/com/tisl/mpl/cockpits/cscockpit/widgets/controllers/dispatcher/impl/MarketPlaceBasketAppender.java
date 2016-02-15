package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.dispatcher.impl;

import org.apache.log4j.Logger;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceBasketController;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.impl.AbstractItemAppenderEventDispatcher;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketPlaceBasketAppender.
 */
public class MarketPlaceBasketAppender
		extends
		AbstractItemAppenderEventDispatcher<MarketPlaceBasketController, TypedObject> {

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(MarketPlaceBasketAppender.class);
	
	/* (non-Javadoc)
	 * @see de.hybris.platform.cscockpit.widgets.controllers.dispatcher.impl.AbstractItemAppenderEventDispatcher#addItem(java.lang.Object, long)
	 */
	@Override
	protected boolean addItem(TypedObject item, long qty) {
			 getWidgetController().addToCart(item, qty);
		return true;
	}

}
