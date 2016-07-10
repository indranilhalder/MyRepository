package com.tisl.mpl.cockpits.cscockpit.widgets.models.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;

import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.cscockpit.widgets.models.impl.SearchResultWidgetModel;
import de.hybris.platform.servicelayer.session.SessionService;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketplaceSearchResultWidgetModel.
 */
public class MarketplaceSearchResultWidgetModel extends SearchResultWidgetModel {
	
	/** The pin code. */
	private Long pinCode;
	
	/** The delivery modes. */
	List<DeliveryDetailsData> deliveryModes;
	
	@Autowired
	private SessionService sessionService;

	
	/**
	 * Gets the delivery modes.
	 *
	 * @return the delivery modes
	 */
	public List<DeliveryDetailsData> getDeliveryModes() {
		return deliveryModes;
	}


	/**
	 * Sets the delivery modes.
	 *
	 * @param deliveryModes the new delivery modes
	 */
	public void setDeliveryModes(List<DeliveryDetailsData> deliveryModes) {
		this.deliveryModes = deliveryModes;
	}


	/**
	 * Gets the pin code.
	 *
	 * @return the pin code
	 */
	public Long getPinCode() {
		if(null!=sessionService.getAttribute(MarketplaceCockpitsConstants.PIN_CODE)) {
			return Long.valueOf(sessionService.getAttribute(MarketplaceCockpitsConstants.PIN_CODE).toString()).longValue();
		}
		return pinCode;
	}

	
	/**
	 * Sets the pin code.
	 *
	 * @param pinCode the new pin code
	 */
	public void setPinCode(final Long pinCode) {
		this.pinCode = pinCode;
	}
}
