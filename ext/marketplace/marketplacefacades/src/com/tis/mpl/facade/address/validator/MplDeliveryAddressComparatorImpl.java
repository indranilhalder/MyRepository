/**
 * 
 */
package com.tis.mpl.facade.address.validator;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;

import org.apache.commons.lang.StringUtils;



/**
 * @author Comparing Old Address to new address
 *
 *return boolean 
 */
public class MplDeliveryAddressComparatorImpl implements MplDeliveryAddressComparator
{
	/***
	 * compare old address to new address
	 * @param oldAddress
	 * @param newAddress
	 * @return boolean
	 */
	@Override
	public boolean compareAddress(AddressData oldAddress, AddressData newAddress)
	{
		boolean isChanged = false;
		if (StringUtils.isNotEmpty(oldAddress.getLine1()) && StringUtils.isNotEmpty(newAddress.getLine1()))
		{

			if (!oldAddress.getLine1().trim().equalsIgnoreCase(newAddress.getLine1().trim()))
			{
				return true;
			}

		}
		if (StringUtils.isNotEmpty(oldAddress.getLine2())
				&& StringUtils.isNotEmpty(newAddress.getLine2()))
		{

			if (!oldAddress.getLine2().trim().equalsIgnoreCase(newAddress.getLine2().trim()))
			{
				return true;
			}

		}
		if (StringUtils.isNotEmpty(oldAddress.getLine3())
				&& StringUtils.isNotEmpty(newAddress.getLine3()))
		{

			if (!oldAddress.getLine3().trim().equalsIgnoreCase(newAddress.getLine3().trim()))
			{
				return true;
			}

		}
		if (StringUtils.isNotEmpty(newAddress.getLandmark()))
		{
			if (StringUtils.isNotEmpty(oldAddress.getLandmark()))
			{
				if (!oldAddress.getLandmark().trim().equalsIgnoreCase(newAddress.getLandmark().trim()))
				{
					return true;
				}
			}
			else
			{
				return true;
			}
		}
		if (StringUtils.isNotEmpty(oldAddress.getState()) && StringUtils.isNotEmpty(newAddress.getState()))
		{

			if (!oldAddress.getState().trim().equalsIgnoreCase(newAddress.getState().trim()))
			{
				return true;
			}
		}
		if (StringUtils.isNotEmpty(oldAddress.getCity()) && StringUtils.isNotEmpty(newAddress.getCity()))
		{
			if (!oldAddress.getCity().trim().equalsIgnoreCase(newAddress.getCity().trim()))
			{
				return true;
			}
		}

		if (StringUtils.isNotEmpty(oldAddress.getPostalCode()) && StringUtils.isNotEmpty(newAddress.getPostalCode()))
		{
			if (!oldAddress.getPostalCode().trim().equalsIgnoreCase(newAddress.getPostalCode().trim()))
			{
				return true;
			}
		}
		return isChanged;
	}
	

	/***
	 * compare contact Details 
	 * @param oldAddress
	 * @param newAddress
	 * @return boolean 
	 */
	@Override
	public boolean compareContactDetails(AddressData oldAddress, AddressData newAddress)
	{
		boolean isChanged = false;
		if (StringUtils.isNotEmpty(oldAddress.getPhone()) && StringUtils.isNotEmpty(newAddress.getPhone()))
		{
			if (!oldAddress.getPhone().trim().equalsIgnoreCase(newAddress.getPhone().trim()))
			{
				return true;
			}
		}

		if (StringUtils.isNotEmpty(oldAddress.getFirstName()) && StringUtils.isNotEmpty(newAddress.getFirstName()))
		{
			if (!oldAddress.getFirstName().trim().equalsIgnoreCase(newAddress.getFirstName().trim()))
			{
				return true;
			}
		}
		if (StringUtils.isNotEmpty(oldAddress.getLastName()) && StringUtils.isNotEmpty(newAddress.getLastName()))
		{
			if (!oldAddress.getLastName().trim().equalsIgnoreCase(newAddress.getLastName().trim()))
			{
				return true;
			}
		}
		return isChanged;
	}


	/***
	 * compare old address to new address
	 * @param oldAddress
	 * @param newDelivery
	 * @return boolean
	 */
	@Override
	public boolean compareAddressModel(AddressModel oldAddress,AddressModel newDelivery)
	{
		boolean isChanged = false;
		if (StringUtils.isNotEmpty(oldAddress.getStreetname()) && StringUtils.isNotEmpty(newDelivery.getStreetname()))
		{

			if (oldAddress.getStreetname().trim().equalsIgnoreCase(newDelivery.getStreetname().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}

		}
		if (StringUtils.isNotEmpty(oldAddress.getStreetnumber())
				&& StringUtils.isNotEmpty(newDelivery.getStreetnumber()))
		{

			if (oldAddress.getStreetnumber().trim().equalsIgnoreCase(newDelivery.getStreetnumber().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}

		}
		if (StringUtils.isNotEmpty(oldAddress.getAddressLine3())
				&& StringUtils.isNotEmpty(newDelivery.getAddressLine3()))
		{

			if (oldAddress.getAddressLine3().trim().equalsIgnoreCase(newDelivery.getAddressLine3().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}

		}
		if (StringUtils.isNotEmpty(oldAddress.getLandmark()) && StringUtils.isNotEmpty(newDelivery.getLandmark()))
		{

			if (oldAddress.getLandmark().trim().equalsIgnoreCase(newDelivery.getLandmark().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}
		}
		if (StringUtils.isNotEmpty(oldAddress.getDistrict()) && StringUtils.isNotEmpty(newDelivery.getDistrict()))
		{

			if (oldAddress.getDistrict().trim().equalsIgnoreCase(newDelivery.getDistrict().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}
		}
		if (StringUtils.isNotEmpty(oldAddress.getCity()) && StringUtils.isNotEmpty(newDelivery.getCity()))
		{
			if (oldAddress.getCity().trim().equalsIgnoreCase(newDelivery.getCity().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}
		}


		if (StringUtils.isNotEmpty(oldAddress.getPostalcode()) && StringUtils.isNotEmpty(newDelivery.getPostalcode()))
		{
			if (oldAddress.getPostalcode().trim().equalsIgnoreCase(newDelivery.getPostalcode().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}
		}
		return isChanged;
	}

}
