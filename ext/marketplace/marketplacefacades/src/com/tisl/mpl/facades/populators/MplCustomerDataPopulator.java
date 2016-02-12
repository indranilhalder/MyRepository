/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.tisl.mpl.facades.data.CustomerDataForOrderXML;


/**
 * @author TCS
 *
 */
public class MplCustomerDataPopulator implements Populator<OrderModel, com.tisl.mpl.facades.data.CustomerDataForOrderXML>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final OrderModel source, final CustomerDataForOrderXML target) throws ConversionException
	{
		if (source.getUser() != null)
		{
			final CustomerModel customer = (CustomerModel) source.getUser();
			final AddressModel address = source.getDeliveryAddress();
			if (customer != null)
			{
				target.setCustomerID(customer.getUid());
				target.setEmailID(customer.getOriginalUid());
				target.setFirstName(customer.getFirstName());
				target.setLastName(customer.getLastName());
			}
			//logic has been moved to avoid sonar issue
			setAddressToTarget(address, customer, target);

			//			if (address != null)
			//			{
			//				target.setAddress1(address.getLine1());
			//				target.setAddress2(address.getLine2());
			//				target.setCity(address.getTown());
			//				target.setPincode(address.getPostalcode());
			//				target.setPhoneNumber(address.getPhone1());
			//				if (address.getCountry() != null)
			//				{
			//					target.setCountry(address.getCountry().getName());
			//				}
			//				target.setState(address.getDistrict());
			//			}

			//			if (address == customer.getDefaultShipmentAddress())
			//			{
			//				target.setDefaultFlag("Y");
			//			}
			//			else
			//			{
			//				target.setDefaultFlag("N");
			//			}

		}
	}

	/**
	 * @description method created to avoid sonar
	 * @param address
	 * @param customer
	 * @param target
	 */
	private void setAddressToTarget(final AddressModel address, final CustomerModel customer, final CustomerDataForOrderXML target)
	{
		if (address != null)
		{
			target.setAddress1(address.getLine1());
			target.setAddress2(address.getLine2());
			target.setCity(address.getTown());
			target.setPincode(address.getPostalcode());
			target.setPhoneNumber(address.getPhone1());
			if (address.getCountry() != null)
			{
				target.setCountry(address.getCountry().getName());
			}
			target.setState(address.getDistrict());
			setDefaultFlagForShipmentAddress(address, customer, target);

		}

	}

	/**
	 * @description method created to avoid sonar
	 * @param address
	 * @param customer
	 * @param target
	 */
	private void setDefaultFlagForShipmentAddress(final AddressModel address, final CustomerModel customer,
			final CustomerDataForOrderXML target)
	{
		if (address == customer.getDefaultShipmentAddress())
		{
			target.setDefaultFlag("Y");
		}
		else
		{
			target.setDefaultFlag("N");
		}

	}
}
