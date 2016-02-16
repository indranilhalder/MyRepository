/**
 *
 */
package com.tisl.mpl.integration.oms.mapping;


import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.integration.oms.mapping.OmsHybrisEnumMappingStrategy;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.shipping.Shipment;


public class CustomConsignmentStatusMappingStrategy implements OmsHybrisEnumMappingStrategy<ConsignmentStatus, Shipment>
{
	private Map<String, ConsignmentStatus> consignmentStatusMapping;
	private Map<String, ConsignmentStatus> pickupOverrideStatusMapping;

	public ConsignmentStatus getHybrisEnumFromDto(final Shipment shipment)
	{
		if ((shipment.getPickupInStore()) && (getPickupOverrideStatusMapping() != null)
				&& (getPickupOverrideStatusMapping().containsKey(shipment.getOlqsStatus())))
		{
			return (getPickupOverrideStatusMapping().get(shipment.getOlqsStatus()));
		}

		return (getConsignmentStatusMapping().get(shipment.getOlqsStatus()));
	}

	protected Map<String, ConsignmentStatus> getConsignmentStatusMapping()
	{
		return this.consignmentStatusMapping;
	}

	protected Map<String, ConsignmentStatus> getPickupOverrideStatusMapping()
	{
		return this.pickupOverrideStatusMapping;
	}

	@Required
	public void setConsignmentStatusMapping(final Map<String, ConsignmentStatus> consignmentStatusMapping)
	{
		this.consignmentStatusMapping = consignmentStatusMapping;
	}

	public void setPickupOverrideStatusMapping(final Map<String, ConsignmentStatus> pickupOverrideStatusMapping)
	{
		this.pickupOverrideStatusMapping = pickupOverrideStatusMapping;
	}
}
