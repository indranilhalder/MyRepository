/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.strategies.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
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


	@Override
	public boolean getIsShipCODEligible(String ussid) {
		boolean codEligible = true;
		LOG.info("Checking SSHIP cod eligible or not for USSID "+ussid);
		try {
			RichAttributeModel richAttribute = findDeliveryCostStrategy.findDeliveryFulfillMode(ussid);
			if(null != richAttribute) {
				String sShipcodEligibe = null;
				if(null != richAttribute.getIsSshipCodEligible() && null != richAttribute.getIsSshipCodEligible().getCode()) {
					sShipcodEligibe = richAttribute.getIsSshipCodEligible().getCode();
					if(LOG.isDebugEnabled()){
						LOG.info("sShipcodEligibe for USSID "+ussid+" "+sShipcodEligibe);
					}
					if(null != sShipcodEligibe) {
						if(sShipcodEligibe.equalsIgnoreCase(MarketplacecommerceservicesConstants.NO) || sShipcodEligibe.equalsIgnoreCase(MarketplacecommerceservicesConstants.FALSE) ){
							codEligible = false;
						}
					}
				}else {
					if(LOG.isDebugEnabled()){
						LOG.info("sShipcodEligibe for USSID "+ussid+" empty");
					}
				}
			}
		}catch(Exception e) {
			LOG.error("Exception while checking cod eligibility for :"+ussid);
		}
		return codEligible;


	}

}
