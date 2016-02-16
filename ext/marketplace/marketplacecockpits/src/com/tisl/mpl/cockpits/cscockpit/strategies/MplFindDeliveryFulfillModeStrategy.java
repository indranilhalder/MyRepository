package com.tisl.mpl.cockpits.cscockpit.strategies;


public interface MplFindDeliveryFulfillModeStrategy {

	
	String findDeliveryFulfillMode(String selectedUssid);
	
	boolean isTShip(String selectedUssid);
}
