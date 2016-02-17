/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import java.util.List;

import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cscockpit.widgets.controllers.CancellationController;

/**
 * @author 1006687
 *
 */
public interface MarketPlaceCancellationController extends CancellationController{

	public boolean isFreebieAvaialble(
			List<ObjectValueContainer> cancellableItems);
}
