/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.List;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface MplDeliveryInformationService
{
	public List<DeliveryModeModel> getDeliveryInformation(final List<String> code) throws EtailNonBusinessExceptions;
}
