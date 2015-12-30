/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.List;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface MplDeliveryInformationDao
{
	public List<DeliveryModeModel> getDeliveryInformation(List<String> code) throws EtailNonBusinessExceptions;
}
