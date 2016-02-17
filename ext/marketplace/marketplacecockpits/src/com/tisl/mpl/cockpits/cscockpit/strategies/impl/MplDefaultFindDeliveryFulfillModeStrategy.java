/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.strategies.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;
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
				 return findDeliveryCostStrategy.findDeliveryFulfillMode(selectedUssid).toUpperCase();
	}


	@Override
	public boolean isTShip(String selectedUssid) {
		 	 LOG.debug("MplDefaultFindDeliveryFulfillModeStrategy for SKUID:: " + selectedUssid);
		return  findDeliveryCostStrategy.isTShip(selectedUssid);
	}

}
