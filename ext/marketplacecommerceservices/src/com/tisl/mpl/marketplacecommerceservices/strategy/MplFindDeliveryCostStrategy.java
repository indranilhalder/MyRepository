/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.order.strategies.calculation.FindDeliveryCostStrategy;

import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;


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

	//String findDeliveryFulfillMode(String selectedUssid);
	//TPR-622,627--- CSCOCKPIT Add to cart to get and isSshipCodEligble checking need to done for SSHIP Products hence return type changed
	RichAttributeModel findDeliveryFulfillMode(String selectedUssid);

	/**
	 * @param ussid
	 * @return
	 */
	boolean getIsSshipCodEligible(String ussid);
}
