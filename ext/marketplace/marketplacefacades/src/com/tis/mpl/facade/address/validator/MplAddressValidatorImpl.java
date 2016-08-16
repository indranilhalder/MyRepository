/**
 * 
 */
package com.tis.mpl.facade.address.validator;

import de.hybris.platform.core.model.user.AddressModel;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.TemproryAddressModel;


/**
 * @author Comparing Old Address to new address
 *
 *return boolean 
 */
public class MplAddressValidatorImpl implements MplAddressValidator
{
	/***
	 * compare old address to new address
	 * @param addressModel
	 * @param temproryAddressModel
	 * @return boolean
	 */
	@Override
	public boolean compareAddress(AddressModel addressModel, TemproryAddressModel temproryAddressModel)
	{
		boolean isChanged = false;
		if (StringUtils.isNotEmpty(addressModel.getStreetname()) && StringUtils.isNotEmpty(temproryAddressModel.getStreetname()))
		{

			if (addressModel.getStreetname().trim().equalsIgnoreCase(temproryAddressModel.getStreetname().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}

		}
		if (StringUtils.isNotEmpty(addressModel.getStreetnumber())
				&& StringUtils.isNotEmpty(temproryAddressModel.getStreetnumber()))
		{

			if (addressModel.getStreetnumber().trim().equalsIgnoreCase(temproryAddressModel.getStreetnumber().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}

		}
		if (StringUtils.isNotEmpty(addressModel.getAddressLine3())
				&& StringUtils.isNotEmpty(temproryAddressModel.getAddressLine3()))
		{

			if (addressModel.getAddressLine3().trim().equalsIgnoreCase(temproryAddressModel.getAddressLine3().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}

		}
		if (StringUtils.isNotEmpty(addressModel.getLandmark()) && StringUtils.isNotEmpty(temproryAddressModel.getLandmark()))
		{

			if (addressModel.getLandmark().trim().equalsIgnoreCase(temproryAddressModel.getLandmark().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}
		}
		if (StringUtils.isNotEmpty(addressModel.getDistrict()) && StringUtils.isNotEmpty(temproryAddressModel.getDistrict()))
		{

			if (addressModel.getDistrict().trim().equalsIgnoreCase(temproryAddressModel.getDistrict().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}
		}
		if (StringUtils.isNotEmpty(addressModel.getCity()) && StringUtils.isNotEmpty(temproryAddressModel.getCity()))
		{
			if (addressModel.getCity().trim().equalsIgnoreCase(temproryAddressModel.getCity().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}
		}


		if (StringUtils.isNotEmpty(addressModel.getPostalcode()) && StringUtils.isNotEmpty(temproryAddressModel.getPostalcode()))
		{
			if (addressModel.getPostalcode().trim().equalsIgnoreCase(temproryAddressModel.getPostalcode().trim()))
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
	

	/***
	 * compare contact Details 
	 * @param addressModel
	 * @param temproryAddressModel
	 * @return boolean 
	 */
	@Override
	public boolean compareContactDetails(AddressModel addressModel, TemproryAddressModel temproryAddressModel)
	{
		boolean isChanged = false;
		if (StringUtils.isNotEmpty(addressModel.getPhone1()) && StringUtils.isNotEmpty(temproryAddressModel.getPhone1()))
		{
			if (addressModel.getPhone1().trim().equalsIgnoreCase(temproryAddressModel.getPhone1().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}
		}

		if (StringUtils.isNotEmpty(addressModel.getFirstname()) && StringUtils.isNotEmpty(temproryAddressModel.getFirstname()))
		{
			if (addressModel.getFirstname().trim().equalsIgnoreCase(temproryAddressModel.getFirstname().trim()))
			{
				isChanged = false;
			}
			else
			{
				isChanged = true;
				return isChanged;
			}
		}
		if (StringUtils.isNotEmpty(addressModel.getLastname()) && StringUtils.isNotEmpty(temproryAddressModel.getLastname()))
		{
			if (addressModel.getLastname().trim().equalsIgnoreCase(temproryAddressModel.getLastname().trim()))
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
