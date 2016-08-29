/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.strategies.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplFindDeliveryCostStrategy;

/**
 * @author 890223
 *
 */
public class MplDefaultFindDeliveryFulfillModeStrategy implements MplFindDeliveryFulfillModeStrategy {

	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MplDefaultFindDeliveryFulfillModeStrategy.class);
	
	@Autowired
	private MplFindDeliveryCostStrategy findDeliveryCostStrategy;
	
				
	@Override
	public String findDeliveryFulfillMode(String selectedUssid) {
		 LOG.debug("MplDefaultFindDeliveryFulfillModeStrategy for SKUID:: " + selectedUssid);
				 //return findDeliveryCostStrategy.findDeliveryFulfillMode(selectedUssid).toUpperCase();
		 
		//TPR-622,627--- CSCOCKPIT Add to cart to get and isSshipCodEligble checking need to done for SSHIP Products hence return type changed
		 RichAttributeModel richAttribute = findDeliveryCostStrategy.findDeliveryFulfillMode(selectedUssid);
		 return richAttribute.getDeliveryFulfillModes().getCode().toUpperCase();
	}


	@Override
	public boolean isTShip(String selectedUssid) {
		 	 LOG.debug("MplDefaultFindDeliveryFulfillModeStrategy for SKUID:: " + selectedUssid);
		return  findDeliveryCostStrategy.isTShip(selectedUssid);
	}

}
