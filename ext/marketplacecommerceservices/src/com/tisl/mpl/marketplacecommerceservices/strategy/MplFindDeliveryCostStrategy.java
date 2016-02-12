/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.order.strategies.calculation.FindDeliveryCostStrategy;

import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;


/**
 * @author 397907
 * 
 */
public interface MplFindDeliveryCostStrategy extends FindDeliveryCostStrategy
{
	/**
	 *
	 */
	String getDeliveryModeDesc(MplZoneDeliveryModeValueModel deliveryEntry, String selectedUssid);

	/**
	 * 
	 */
	boolean isTShip(String selectedUssid);

	/**
	 * 
	 */
	String findDeliveryFulfillMode(String selectedUssid);
}
