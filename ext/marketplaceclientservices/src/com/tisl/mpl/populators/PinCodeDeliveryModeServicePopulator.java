package com.tisl.mpl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import com.hybris.oms.api.comm.dto.PincodeServiceabilityCheck;
import com.hybris.oms.api.comm.dto.PincodeServiceabilityCheckRequestItem;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeListRequest;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeRequest;


/**
 * @author TCS
 *
 */
public class PinCodeDeliveryModeServicePopulator implements Populator<PinCodeDeliveryModeListRequest, PincodeServiceabilityCheck>
{
	/**
	 * @Description : Populating OMS DTO PincodeServiceabilityCheck from commerce DTO PinCodeDeliveryModeListRequest for
	 *              Pincode Serviceability
	 * @param source
	 * @param target
	 */
	@Override
	public void populate(final PinCodeDeliveryModeListRequest source, final PincodeServiceabilityCheck target)
			throws ConversionException
	{
		if (source.getPincode() != null)
		{
			target.setPincode(source.getPincode());
		}

		if (source.getItem() != null)
		{
			final List<PinCodeDeliveryModeRequest> itemListCommerce = source.getItem();
			final List<PincodeServiceabilityCheckRequestItem> itemListOMS = new ArrayList<PincodeServiceabilityCheckRequestItem>();

			for (final PinCodeDeliveryModeRequest singleCommerceItem : itemListCommerce)
			{
				final PincodeServiceabilityCheckRequestItem singleOMSItem = new PincodeServiceabilityCheckRequestItem();
				if (singleCommerceItem.getUSSID() != null)
				{
					singleOMSItem.setUssID(singleCommerceItem.getUSSID());
				}
				if (singleCommerceItem.getSellerID() != null)
				{
					singleOMSItem.setSellerID(singleCommerceItem.getSellerID());
				}
				singleOMSItem.setPrice((float) singleCommerceItem.getPrice());

				if (singleCommerceItem.getIsCOD() != null)
				{
					singleOMSItem.setIsCOD(singleCommerceItem.getIsCOD());
				}
				if (singleCommerceItem.getFulfilmentType() != null)
				{
					singleOMSItem.setFulfilmentType(singleCommerceItem.getFulfilmentType());
				}
				if (singleCommerceItem.getTransportMode() != null)
				{
					singleOMSItem.setTransportMode(singleCommerceItem.getTransportMode());
				}
				if (singleCommerceItem.getIsDeliveryDateRequired() != null)
				{
					singleOMSItem.setDeliveryDateRequired(singleCommerceItem.getIsDeliveryDateRequired());
				}
				if (singleCommerceItem.getDeliveryMode() != null)
				{
					singleOMSItem.setDeliveryModes(singleCommerceItem.getDeliveryMode());
				}

				itemListOMS.add(singleOMSItem);
			}

			target.setItem(itemListOMS);

		}

	}
}
