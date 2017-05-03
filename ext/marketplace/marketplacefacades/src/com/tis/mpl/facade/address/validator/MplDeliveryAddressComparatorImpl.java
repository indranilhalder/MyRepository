/**
 * 
 */
package com.tis.mpl.facade.address.validator;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



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
	 * 
	 */
	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressComparatorImpl.class);
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
		else if (StringUtils.isNotEmpty(oldAddress.getLine2()))
		{
			return true;
		}
		else if (StringUtils.isNotEmpty(newAddress.getLine2()))
		{
			return true;
		}

		if (StringUtils.isNotEmpty(oldAddress.getLine3())
				&& StringUtils.isNotEmpty(newAddress.getLine3()))
		{

			if (!oldAddress.getLine3().trim().equalsIgnoreCase(newAddress.getLine3().trim()))
			{
				return true;
			}

		}
		else if (StringUtils.isNotEmpty(oldAddress.getLine3()))
		{
			return true;
		}
		else if (StringUtils.isNotEmpty(newAddress.getLine3()))
		{
			return true;
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

		if (StringUtils.isNotEmpty(oldAddress.getTown()) && StringUtils.isNotEmpty(newAddress.getTown()))
		{
			if (!oldAddress.getTown().trim().equalsIgnoreCase(newAddress.getTown().trim()))
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
	 * @return boolean
	 */
	@Override
	public boolean compareAddressModel(AddressModel oldAddress,AddressModel newDeliveryAddress)
	{
		boolean isChanged = false;
		if (StringUtils.isNotEmpty(oldAddress.getStreetname()) && StringUtils.isNotEmpty(newDeliveryAddress.getStreetname()))
		{

			if (!oldAddress.getStreetname().trim().equalsIgnoreCase(newDeliveryAddress.getStreetname().trim()))
			{	
				return true;
			}

		}
		if (StringUtils.isNotEmpty(oldAddress.getStreetnumber())
				&& StringUtils.isNotEmpty(newDeliveryAddress.getStreetnumber()))
		{

			if (!oldAddress.getStreetnumber().trim().equalsIgnoreCase(newDeliveryAddress.getStreetnumber().trim()))
			{
				return true;
			}

		}
		else if (StringUtils.isNotEmpty(oldAddress.getStreetnumber()))
		{
			return true;
		}
		else if (StringUtils.isNotEmpty(newDeliveryAddress.getStreetnumber()))
		{
			return true;
		}
		
		if (StringUtils.isNotEmpty(oldAddress.getAddressLine3())
				&& StringUtils.isNotEmpty(newDeliveryAddress.getAddressLine3()))
		{
			if (!oldAddress.getAddressLine3().trim().equalsIgnoreCase(newDeliveryAddress.getAddressLine3().trim()))
			{
				return true;
			}	

		}
		else if (StringUtils.isNotEmpty(oldAddress.getAddressLine3()))
		{
			return true;
		}
		else if (StringUtils.isNotEmpty(newDeliveryAddress.getAddressLine3()))
		{
			return true;
		}
		
		
		if (StringUtils.isNotEmpty(oldAddress.getLandmark()) && StringUtils.isNotEmpty(newDeliveryAddress.getLandmark()))
		{

			if (!oldAddress.getLandmark().trim().equalsIgnoreCase(newDeliveryAddress.getLandmark().trim()))
			{
				return true;
			}
		}
		else if (StringUtils.isNotEmpty(newDeliveryAddress.getLandmark()))
		{
			return true;
		}else if(StringUtils.isNotEmpty(oldAddress.getLandmark())){
			return true;
		}
		
		
		if (StringUtils.isNotEmpty(oldAddress.getDistrict()) && StringUtils.isNotEmpty(newDeliveryAddress.getDistrict()))
		{
			if (!oldAddress.getDistrict().trim().equalsIgnoreCase(newDeliveryAddress.getDistrict().trim()))
			{
				return true;
			}
		
		}
		if (StringUtils.isNotEmpty(oldAddress.getCity()) && StringUtils.isNotEmpty(newDeliveryAddress.getCity()))
		{
			if (!oldAddress.getCity().trim().equalsIgnoreCase(newDeliveryAddress.getCity().trim()))
			{
				return true;
			}
			
		}

		if (StringUtils.isNotEmpty(oldAddress.getTown()) && StringUtils.isNotEmpty(newDeliveryAddress.getTown()))
		{
			if (!oldAddress.getTown().trim().equalsIgnoreCase(newDeliveryAddress.getTown().trim()))
			{
				return true;
			}
			
		}
		
		if (StringUtils.isNotEmpty(oldAddress.getPostalcode()) && StringUtils.isNotEmpty(newDeliveryAddress.getPostalcode()))
		{
			if (!oldAddress.getPostalcode().trim().equalsIgnoreCase(newDeliveryAddress.getPostalcode().trim()))
			{
				return true;
			}
			
		}
		return isChanged;
	}
	
	
	@Override
	public boolean compareMobileNumber(AddressModel oldAddress, AddressModel newDeliveryAddress)
	{
		try
		{
			String mobileNumber=null;
			String newMobileNumber=null;
			
			if (oldAddress != null)
			{
				mobileNumber = StringUtils.isNotEmpty(oldAddress.getPhone1()) ? oldAddress.getPhone1() : (StringUtils
						.isNotEmpty(oldAddress.getPhone2()) ? oldAddress.getPhone2() : oldAddress.getCellphone());
			}
			if (newDeliveryAddress != null)
			{

				newMobileNumber = StringUtils.isNotEmpty(newDeliveryAddress.getPhone1()) ? newDeliveryAddress.getPhone1()
						: (StringUtils.isNotEmpty(newDeliveryAddress.getPhone2()) ? newDeliveryAddress.getPhone2() : newDeliveryAddress
								.getCellphone());
			}
			if (mobileNumber.equalsIgnoreCase(newMobileNumber))
			{
				return false;
			}
		}
		catch (Exception exception)
		{
			LOG.error("Ecxeption MplDeliveryAddressComparatorImpl:::"+exception.getMessage());
		}
		return true;
	}


	
	@Override
	public boolean compareNameDetails(AddressModel oldAddress, AddressModel newDeliveryAddress)
	{
		
		try
		{
			if (!oldAddress.getFirstname().equalsIgnoreCase(newDeliveryAddress.getFirstname()))
			{
				return true;
			}
			if (!oldAddress.getLastname().equalsIgnoreCase(newDeliveryAddress.getLastname()))
			{
				return true;
			}
		}
		catch (Exception exception)
		{
			LOG.error("Ecxeption MplDeliveryAddressComparatorImpl:::" + exception.getMessage());
		}
		return false;
	}
	

}
